package info.magnolia.admincentral;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import info.magnolia.admincentral.model.ActivateExtension;
import info.magnolia.admincentral.model.AppType;
import info.magnolia.admincentral.model.Application;
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

    @Inject
    public AdmincentralResource(Availability availability) {
        this.availability = availability;
    }

    @GET
    @Path("/applications")
    @Produces(APPLICATION_JSON)
    public List<Application> applications() {
        return List.of(new Application("watches", "Watches", AppType.CONTENT_TYPE));
    }

    @GET
    @Path("/extensions/{subscription}")
    @Produces(APPLICATION_JSON)
    public List<SubscriptionExtension> subscriptionExtensions(@PathParam("subscription") String subscription) {
        return availableExtensions(subscription);
    }

    @POST
    @Path("/extensions/{subscription}/enable")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @PermitAll
    public Response enableExtension(@PathParam("subscription") String subscription, ActivateExtension activateExtension) {
        var enabled = availability.enable(
                new EnableExtensionRequest(activateExtension.extensionId(), subscription,
                        activateExtension.configValues()));
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

    private List<SubscriptionExtension> availableExtensions(String subscriptionId) {
        return availability.availableExtensions(subscriptionId)
                .collect(Collectors.toList());
    }
}