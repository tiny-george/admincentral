package info.magnolia.admincentral;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import info.magnolia.admincentral.model.ActivateExtension;
import info.magnolia.admincentral.model.AppType;
import info.magnolia.admincentral.model.Application;
import info.magnolia.admincentral.model.Extension;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path("/admincentral")
public class AdmincentralResource {

    private List<Extension> extensions = List.of(
            new Extension("shopify", "Shopify Integration", false, List.of("token")),
            new Extension("hello", "Salute RestEasy", true, List.of()),
            new Extension("color-picker", "Color picker form component", false, List.of())
    );

    @GET
    @Path("/applications")
    @Produces(APPLICATION_JSON)
    public List<Application> applications() {
        return List.of(new Application("watches", "Watches", AppType.CONTENT_TYPE));
    }

    @GET
    @Path("/extensions/{subscription}")
    @Produces(APPLICATION_JSON)
    public List<Extension> subscriptionExtensions(@PathParam("subscription") String subscription) {
        return extensions;
    }

    @POST
    @Path("/extensions/{subscription}/activate")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @PermitAll
    public List<Extension> activateExtension(@PathParam("subscription") String subscription, ActivateExtension activateExtension) {
        List<Extension> newList = new ArrayList<>(extensions);
        newList.add(new Extension(activateExtension.name(), subscription, true, List.of()));
        extensions = newList;
        return extensions;
    }
}