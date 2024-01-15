package info.magnolia.admincentral.form;

import info.magnolia.admincentral.conversor.YamlToObject;
import info.magnolia.admincentral.datasource.DatasourceResolver;
import info.magnolia.admincentral.datasource.KeyValue;
import info.magnolia.admincentral.model.Form;
import info.magnolia.admincentral.model.FormProperty;
import info.magnolia.admincentral.model.Model;
import info.magnolia.admincentral.model.ModelProperty;
import info.magnolia.admincentral.template.Mustache;
import info.magnolia.response.Response;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FormBuilder {

    private static final String DATASOURCE_TYPE_PREFIX = "datasource:";

    private final YamlToObject conversor;
    private final DatasourceResolver datasourceResolver;
    private final Mustache mustache;

    public FormBuilder() {
        this.conversor = new YamlToObject();
        this.datasourceResolver = new DatasourceResolver();
        this.mustache = new Mustache();
    }

    public Response<FormBuilderResponse> build(FormBuilderRequest formBuilderRequest) {
        var type = this.conversor.toObject(formBuilderRequest.type(), Model.class);
        if (type.hasError()) {
            return Response.error(new Exception("Can not convert to model: " + formBuilderRequest.type()));
        }
        if (formBuilderRequest.form() != null && !formBuilderRequest.form().isEmpty()) {
            var form = this.conversor.toObject(formBuilderRequest.form(), Form.class);
            if (form.hasError()) {
                return Response.error(new Exception("Can not convert to form: " + formBuilderRequest.form()));
            }
            return fieldsFromModelAndForm(type.get(), form.get());
        } else {
            return fieldsFromModel(type.get());
        }
    }

    private Response<FormBuilderResponse> fieldsFromModelAndForm(Model model, Form form) {
        var formFields = form.properties().stream()
                .map(formProperty -> fieldFromPropertyAndModel(formProperty, model)).toList();
        var error = formFields.stream().filter(Response::hasError).findFirst();
        if (error.isEmpty()) {
            var goodResults = formFields.stream().map(Response::get).toList();
            return Response.ok(new FormBuilderResponse(goodResults.size(), goodResults));
        } else {
            return Response.error(new Exception("Error preparing field " + error.get().getError().getMessage()));
        }
    }

    private Response<Field> fieldFromPropertyAndModel(FormProperty formProperty, Model model) {
        var modelProperty = model.properties().stream().filter(p -> formProperty.name().equals(p.name())).findFirst();
        if (modelProperty.isEmpty()) {
            return Response.error(new Exception("Not found property " + formProperty.name() + " in the model."));
        }
        var field = fieldFromModelProperty(modelProperty.get());
        if (field.isOk()) {
            return fieldWithForm(field.get(), formProperty, modelProperty.get());
        }
        return field;
    }

    private Response<Field> fieldWithForm(Field field, FormProperty formProperty, ModelProperty modelProperty) {
        var label = field.label();
        var type = field.type();
        var additionalData = field.additionalData();
        if (formProperty.label() != null && !formProperty.label().isEmpty()) {
            label = formProperty.label();
        }
        if (formProperty.component() != null) {
            var component = formProperty.component();
            if ("extension".equals(component.type())) {
                type = "externalComponent";
                additionalData = component.value();
            }
            if ("select".equals(component.type())) {
                type = "select";
                var response = selectValues(modelProperty, component.value());
                if (response.hasError()) {
                    return Response.error(response.getError());
                }
                additionalData = response.get();
            }
        }
        return Response.ok(new Field(field.name(), label, type, field.value(), additionalData));
    }

    private Response<FormBuilderResponse> fieldsFromModel(Model model) {
        var fields = model.properties().stream().map(this::fieldFromModelProperty).toList();
        var error = fields.stream().filter(Response::hasError).findFirst();
        if (error.isEmpty()) {
            var goodResults = fields.stream().map(Response::get).toList();
            return Response.ok(new FormBuilderResponse(goodResults.size(), goodResults));
        } else {
            return Response.error(new Exception("Error preparing field " + error.get().getError().getMessage()));
        }
    }

    private Response<Field> fieldFromModelProperty(ModelProperty property) {
        Object additionalData = null;
        var type = property.type() == null || property.type().isEmpty() ? "string" : property.type();
        if (type.startsWith(DATASOURCE_TYPE_PREFIX)) {
            var values = datasourceValues(type.substring(DATASOURCE_TYPE_PREFIX.length()), null).get();
            type = "select";
            additionalData = values;
        }
        if (!Set.of("select", "string").contains(type)) {
            return Response.ok(new Field(property.name(), property.name(), "unknown", "", "Type: " + type + " is not supported at the moment"));
        }
        return Response.ok(new Field(property.name(), property.name(),
                type, "", additionalData));
    }

    private Response<List<KeyValue>> selectValues(ModelProperty property, String labelTemplate) {
        var type = property.type() == null || property.type().isEmpty() ? "string" : property.type();
        if (type.startsWith(DATASOURCE_TYPE_PREFIX)) {
            return datasourceValues(type.substring(DATASOURCE_TYPE_PREFIX.length()), labelTemplate);
        }
        return Response.error(new Exception("No idea how to get select values for property " + property.name()));
    }

    private Response<List<KeyValue>> datasourceValues(String datasource, String labelTemplate) {
        var list = datasourceResolver.values(datasource);
        if (list.hasError()) {
            return Response.error(list.getError());
        }
        var template = Optional.ofNullable(labelTemplate).orElse("{{id}}");
        return Response.ok(list.get().stream()
                .map(item -> new KeyValue((String) item.get("id"), mustache.render(template, item)))
                .toList());
    }
}
