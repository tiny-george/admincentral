package info.magnolia.admincentral;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import info.magnolia.admincentral.model.EnableExtension;
import info.magnolia.extensibility.api.Extension;
import info.magnolia.extensibility.api.ExtensionStatus;
import info.magnolia.extensibility.api.Extensions;
import info.magnolia.extensibility.api.availability.Availability;
import info.magnolia.extensibility.api.availability.DisableExtensionRequest;
import info.magnolia.extensibility.api.availability.EnableExtensionRequest;
import info.magnolia.extensibility.api.availability.SubscriptionExtension;
import info.magnolia.response.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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

    @InjectMock
    Extensions extensions;

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
    public void allExtensions() {
        given()
                .when().get("/admincentral/extensions")
                .then().statusCode(200)
                .log().all()
                .body(
                        "size()", is(1),
                        "[0].name", equalTo("first"),
                        "[0].available", equalTo(true)
                );
    }

    @Test
    public void availableSubscriptionExtensions() {
        given()
                .when().get("/admincentral/availableExtensions/" + SUBSCRIPTION_ID)
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
                .body(new EnableExtension(EXTENSION_ID, Map.of("shopify.token", "aToken")))
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
        Mockito.when(availability.isAvailable(EXTENSION_ID))
                .thenReturn(true);
        Mockito.when(extensions.all())
                .thenReturn(Stream.of(
                        new Extension(EXTENSION_ID, "first", "first description", "owner@magnolia-cms.com",
                                ExtensionStatus.READY, null, null, Set.of("someKey"), "someDeployId")
                ));
    }
}
