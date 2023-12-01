package info.magnolia.admincentral;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import info.magnolia.admincentral.model.ActivateExtension;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AdmincentralResourceTest {

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
                .when().get("/admincentral/extensions/subscription-id")
                .then().statusCode(200)
                .log().all()
                .body(
                        "size()", is(3),
                        "[0].name", equalTo("shopify")
                );
    }

    @Test
    public void activateSubscriptionExtension() {
        given()
                .body(new ActivateExtension("some", Map.of("key", "value")))
                .when().header("Content-Type", "application/json")
                .post("/admincentral/extensions/subscription-id/activate")
                .then().statusCode(200)
                .log().all()
                .body(
                        "size()", is(4),
                        "[3].name", equalTo("some")
                );
    }

    @Test
    public void index() {
        given()
                .when().get("/")
                .then().statusCode(200)
                .log().all();
    }
}
