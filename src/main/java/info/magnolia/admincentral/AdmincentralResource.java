package info.magnolia.admincentral;

import info.magnolia.admincentral.model.AppType;
import info.magnolia.admincentral.model.Application;

import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/admincentral")
public class AdmincentralResource {

    @GET
    @Path("/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Application> applications() {
        return List.of(new Application("Name", "Label App", AppType.CONTENT_TYPE));
    }
}