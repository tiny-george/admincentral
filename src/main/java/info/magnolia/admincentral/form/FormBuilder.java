package info.magnolia.admincentral.form;

import info.magnolia.admincentral.conversor.YamlToObject;
import info.magnolia.admincentral.datasource.DatasourceResolver;
import info.magnolia.admincentral.model.Model;
import info.magnolia.admincentral.model.ModelProperty;
import info.magnolia.response.Response;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FormBuilder {

    private static final String DATASOURCE_TYPE_PREFIX = "datasource:";

    private final YamlToObject conversor;
    private final DatasourceResolver datasourceResolver;

    public FormBuilder() {
        this.conversor = new YamlToObject();
        this.datasourceResolver = new DatasourceResolver();
    }

    public Response<FormBuilderResponse> build(FormBuilderRequest formBuilderRequest) {
        var type = this.conversor.toObject(formBuilderRequest.type(), Model.class);
        if (type.hasError()) {
            return Response.error(new Exception("Can not convert to model: " + formBuilderRequest.form()));
        }
        if (formBuilderRequest.form() != null && !formBuilderRequest.form().isEmpty()) {
            return Response.error(new Exception("Not implemented yet"));
        } else {
            return fieldsFromModel(type.get());
        }
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
            var datasource = type.substring(DATASOURCE_TYPE_PREFIX.length());
            var list = datasourceResolver.values(datasource);
            if (list.hasError()) {
                return Response.error(list.getError());
            }
            type = "select";
            additionalData = list.get();
        }
        if (!Set.of("select", "string").contains(type)) {
            return Response.ok(new Field(property.name(), property.name(), "unknown", "", "Type: " + type + " is not supported at the moment"));
        }
        return Response.ok(new Field(property.name(), property.name(),
                type, "", additionalData));
    }
}
