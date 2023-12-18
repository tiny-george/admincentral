package info.magnolia.admincentral;

import java.net.URI;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ReactRouterFilter implements ContainerResponseFilter {

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Context
    HttpServerResponse response;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {

        if (responseContext.getStatus() != 404 || request.uri().startsWith("/admincentral")) {
            return;
        }

        response.reset();
        response.setStatusCode(200);
        response.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_TYPE.toString());
        response.end();
    }
}
