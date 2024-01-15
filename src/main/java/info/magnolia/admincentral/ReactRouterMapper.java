package info.magnolia.admincentral;

import java.net.URI;

import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class ReactRouterMapper implements ExceptionMapper<NotFoundException> {

    @Context
    HttpServerRequest request;

    @Override
    public Response toResponse(NotFoundException e) {
        if (!request.uri().contains("/admincentral")) {
            return Response.ok().location(URI.create(request.uri())).build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
    }
}
