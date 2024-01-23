package info.magnolia.admincentral;

import info.magnolia.datasource.api.Context;
import info.magnolia.datasource.api.SubscriptionContext;
import jakarta.ws.rs.core.HttpHeaders;

public class EnvironmentContext {
    private final HttpHeaders headers;

    public EnvironmentContext(HttpHeaders headers) {
        this.headers = headers;
    }

    public Context context() {
        var subscriptionId = this.headers.getHeaderString("subscription-id");
        return (SubscriptionContext) () -> subscriptionId;
    }
}
