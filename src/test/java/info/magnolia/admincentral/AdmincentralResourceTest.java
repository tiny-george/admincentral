package info.magnolia.admincentral;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AdmincentralResourceTest {

    @Test
    public void getApplicationsEndpoint() {
        given()
            .when().get("/admincentral/applications")
            .then().statusCode(200)
            .log().all()
            .body(
                "size()", is(1),
                "[0].name", equalTo("Name")
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
