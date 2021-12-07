package Lesson_3;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateImageInformationTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/island.jpg";
    private final String TITLE = "island";
    private final String DESCRIPTION = "Island image";
    private String imageId;
    private String deleteHash;

    @BeforeEach
    void setUp() {
        Response response = given()
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
    void updateImageInformationUnAuthedTest() {
        given()
                .header("Authorization", token)
                .multiPart("title", TITLE)
                .multiPart("description", DESCRIPTION)
                .when()
                .post("https://api.imgur.com/3/image/{deleteHash}", deleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);

        Response response = given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageId}", imageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("data.title"), equalTo(TITLE));
        assertThat(response.jsonPath().getString("data.description"), equalTo(DESCRIPTION));
    }

    @Test
    void updateImageInformationAuthedTest() {
        given()
                .header("Authorization", token)
                .multiPart("title", TITLE)
                .multiPart("description", DESCRIPTION)
                .when()
                .post("https://api.imgur.com/3/image/{imageId}", imageId)
                .prettyPeek()
                .then()
                .statusCode(200);

        Response response = given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageId}", imageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("data.title"), equalTo(TITLE));
        assertThat(response.jsonPath().getString("data.description"), equalTo(DESCRIPTION));
    }

    @AfterEach
    void tearDown() {
            given()
                    .header("Authorization", token)
                    .when()
                    .delete("https://api.imgur.com/3/image/{imageId}", imageId)
                    .prettyPeek()
                    .then()
                    .statusCode(200);
    }
}
