package Lesson_3;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetImageTest extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/island.jpg";
    private final String TITLE = "island";
    private String imageId;

    @BeforeEach
    void setUp() {
        imageId = given()
                .header("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .multiPart("title", TITLE)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void getImageTest() {
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
