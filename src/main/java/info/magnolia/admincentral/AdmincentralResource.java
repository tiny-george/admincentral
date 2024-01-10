package info.magnolia.admincentral;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import info.magnolia.admincentral.form.FormBuilder;
import info.magnolia.admincentral.form.FormBuilderRequest;
import info.magnolia.admincentral.model.ContentTypeProperty;
import info.magnolia.admincentral.model.EnableExtension;
import info.magnolia.admincentral.model.AppType;
import info.magnolia.admincentral.model.Application;
import info.magnolia.admincentral.model.Extension;
import info.magnolia.extensibility.api.Extensions;
import info.magnolia.extensibility.api.availability.Availability;
import info.magnolia.extensibility.api.availability.DisableExtensionRequest;
import info.magnolia.extensibility.api.availability.EnableExtensionRequest;
import info.magnolia.extensibility.api.availability.SubscriptionExtension;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/admincentral")
public class AdmincentralResource {

    private final Availability availability;
    private final FormBuilder formBuilder;
    private final Extensions extensions;

    @Inject
    public AdmincentralResource(Extensions extensions, Availability availability, FormBuilder formBuilder) {
        this.extensions = extensions;
        this.availability = availability;
        this.formBuilder = formBuilder;
    }

    @GET
    @Path("/applications")
    @Produces(APPLICATION_JSON)
    public List<Application> applications() {
        return List.of(new Application("watches", "Watches", AppType.CONTENT_TYPE));
    }

    @GET
    @Path("/properties/{contentType}/{subscriptionId}")
    @Produces(APPLICATION_JSON)
    public List<ContentTypeProperty> contentTypeProperties(
            @PathParam("contentType") String contentType,
            @PathParam("subscriptionId") String subscriptionId) {
        var colorPickerEnabled = availability.availableExtensions(subscriptionId)
                .anyMatch(se -> "warp-extensions-color-picker".equals(se.name()) && se.enabled());
        return List.of(
                new ContentTypeProperty("name", "Name", "string", null),
                new ContentTypeProperty("description", "Description", "textarea", null),
                new ContentTypeProperty("shopify", "Shopify Item", "remote", "shopify-multi"),
                new ContentTypeProperty("color", "Color", "string", colorPickerEnabled ?
                        "warp-extensions-color-picker": null)
        );
    }

    @GET
    @Path("/extensions")
    @Produces(APPLICATION_JSON)
    public List<Extension> currentExtensions() {
        return allExtensions();
    }

    @POST
    @Path("/extensions/activate/{extensionId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response activateExtension(@PathParam("extensionId") String extensionId) {
        var response = availability.markAsAvailable(extensionId);
        if (response.isOk()) {
            return Response.accepted()
                    .entity(allExtensions())
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response.getError().toString())
                    .build();
        }
    }

    @GET
    @Path("/availableExtensions/{subscription}")
    @Produces(APPLICATION_JSON)
    public List<SubscriptionExtension> subscriptionExtensions(@PathParam("subscription") String subscription) {
        return availableExtensions(subscription);
    }

    @POST
    @Path("/extensions/{subscription}/enable")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @PermitAll
    public Response enableExtension(@PathParam("subscription") String subscription, EnableExtension enableExtension) {
        var enabled = availability.enable(
                new EnableExtensionRequest(enableExtension.extensionId(), subscription,
                        enableExtension.configValues()));
        if (enabled.isOk()) {
            return Response.accepted().entity(availableExtensions(subscription)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(enabled.getError().toString())
                    .build();
        }
    }

    @POST
    @Path("/extensions/{subscription}/disable/{extension}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response disableExtension(@PathParam("subscription") String subscription, @PathParam("extension") String extension) {
        var disabled = availability.disable(
                new DisableExtensionRequest(extension, subscription));
        if (disabled.isOk()) {
            return Response.accepted().entity(availableExtensions(subscription)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(disabled.getError().toString())
                    .build();
        }
    }

    @POST
    @Path("/formBuilder")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response formBuilder(FormBuilderRequest formBuilderRequest) {
        var response = formBuilder.build(formBuilderRequest);
        if (response.isOk()) {
            return Response.ok().entity(response.get()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Input:" + formBuilderRequest + " -> " + response.getError().getMessage())
                .build();
    }

    private List<SubscriptionExtension> availableExtensions(String subscriptionId) {
        return availability.availableExtensions(subscriptionId)
                .collect(Collectors.toList());
    }

    private List<Extension> allExtensions() {
        return extensions.all()
                .map(ext -> new Extension(ext.id(), ext.name(), ext.description(), ext.owner(), ext.status(),
                        ext.deploymentId() != null, ext.multitenantConfigKeys(), availability.isAvailable(ext.id())))
                .collect(Collectors.toList());
    }
}