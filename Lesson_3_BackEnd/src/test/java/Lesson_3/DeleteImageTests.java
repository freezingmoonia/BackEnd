package Lesson_3;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class DeleteImageTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/island.jpg";
    private Response response;
    private String imageId;
    private String deleteHash;


    @BeforeEach
    void setUp() {
        response = given()
                .header("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageId = response.jsonPath().getString("data.id");
        deleteHash = response.jsonPath().getString("data.deletehash");
    }

    @Test
    void imageDeleteUnAuthedTest() {
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/image/{deleteHash}", deleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void imageDeletionAuthedTest() {
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/image/{imageId}", imageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
