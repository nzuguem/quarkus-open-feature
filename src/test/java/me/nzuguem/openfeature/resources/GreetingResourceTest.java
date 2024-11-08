package me.nzuguem.openfeature.resources;

import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testHelloEnvEndpoint() {
        given()
          .when().get("/env")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testHelloFlagdEndpoint() {
        given()
          .when().get("/flagd")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testHelloGoffEndpoint() {
        given()
          .when().get("/goff")
          .then()
             .statusCode(200)
             .body(is("Hello from Goff !"));
    }

}