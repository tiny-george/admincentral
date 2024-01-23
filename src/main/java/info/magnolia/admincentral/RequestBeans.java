package info.magnolia.admincentral;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;

@RequestScoped
public class RequestBeans {

    @Produces
    @RequestScoped
    public EnvironmentContext subscriptionInRequest(@Context HttpHeaders httpHeaders) {
        return new EnvironmentContext(httpHeaders);
    }
}
