package Lesson_4;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static Lesson_4.Endpoints.GET_ACCOUNT;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccountTests extends BaseTest{

    @Test
    void getAccountInfoTest() {
        given()
                .spec(requestWithAuth)
                .get(GET_ACCOUNT, username)
                .prettyPeek()
                .then()
                .spec(simplePositiveResponseSpecification);

    }

    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .spec(requestWithAuthAndLogs)
                .get(GET_ACCOUNT, username)
                .prettyPeek()
                .then()
                .spec(simplePositiveResponseSpecification);
    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .spec(requestWithAuthAndLogs)
                .get(GET_ACCOUNT, username)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Test
    void getAccountInfoWithAssertionsAfterTest() {
        Response response = given()
                .spec(requestWithAuthAndLogs)
                .get(GET_ACCOUNT, username)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response();

        assertThat(response.jsonPath().get("data.url"), equalTo(username));
    }
}
