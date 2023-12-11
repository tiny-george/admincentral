package info.magnolia.admincentral;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import info.magnolia.admincentral.model.ActivateExtension;
import info.magnolia.extensibility.api.availability.Availability;
import info.magnolia.extensibility.api.availability.DisableExtensionRequest;
import info.magnolia.extensibility.api.availability.EnableExtensionRequest;
import info.magnolia.extensibility.api.availability.SubscriptionExtension;
import info.magnolia.response.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AdmincentralResourceTest {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";
    private static final String EXTENSION_ID = "aExtensionId";
    private static final List<SubscriptionExtension> EXTENSIONS = List.of(
            new SubscriptionExtension(EXTENSION_ID, "first", "first description",
                    false, Set.of("shopify.token")),
            new SubscriptionExtension("otherId", "second", "second description",
                    true, Set.of())
    );

    @InjectMock
    Availability availability;

    @Test
    public void applicationsEndpoint() {
        given()
            .when().get("/admincentral/applications")
            .then().statusCode(200)
            .log().all()
            .body(
                "size()", is(1),
                "[0].name", equalTo("watches")
            );
    }

    @Test
    public void subscriptionExtensions() {
        given()
                .when().get("/admincentral/extensions/" + SUBSCRIPTION_ID)
                .then().statusCode(200)
                .log().all()
                .body(
                        "size()", is(2),
                        "[0].name", equalTo("first")
                );
    }

    @Test
    public void enableSubscriptionExtension() {
        given()
                .body(new ActivateExtension(EXTENSION_ID, Map.of("shopify.token", "aToken")))
                .when().header("Content-Type", "application/json")
                .post("/admincentral/extensions/" + SUBSCRIPTION_ID + "/enable")
                .then().statusCode(202)
                .log().all()
                .body("size()", is(2));
    }

    @Test
    public void disableSubscriptionExtension() {
        given()
                .when().header("Content-Type", "application/json")
                .post("/admincentral/extensions/" + SUBSCRIPTION_ID + "/disable/" + EXTENSION_ID)
                .then().statusCode(202)
                .log().all()
                .body("size()", is(2));
    }

    @Test
    public void index() {
        given()
                .when().get("/")
                .then().statusCode(200)
                .log().all();
    }

    @BeforeEach
    public void setup() {
        Mockito.when(availability.availableExtensions(SUBSCRIPTION_ID))
                .thenReturn(EXTENSIONS.stream());
        Mockito.when(availability.enable(new EnableExtensionRequest(
                EXTENSION_ID, SUBSCRIPTION_ID, Map.of("shopify.token", "aToken"))))
                .thenReturn(Response.ok(EXTENSIONS.get(0)));
        Mockito.when(availability.disable(new DisableExtensionRequest(
                        EXTENSION_ID, SUBSCRIPTION_ID)))
                .thenReturn(Response.ok(EXTENSIONS.get(0)));
    }
}
