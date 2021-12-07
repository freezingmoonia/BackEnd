package Lesson_3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FavoriteImageTest extends BaseTest {
    private final String PATH_TO_IMAGE = "src/test/resources/island.jpg";
    private String imageId;

    @BeforeEach
    void setUp() {
        imageId = given()
                .header("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void favoriteImageTest() {
        given()
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/{imageId}//favorite", imageId)
                .prettyPeek()
                .then()
                .statusCode(200);
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
