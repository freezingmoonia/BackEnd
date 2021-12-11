package Lesson_4;

import Lesson_4.dto.PostImageResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Lesson_4.Endpoints.IMAGE_FAVORITE;
import static Lesson_4.Endpoints.IMAGE_ID;
import static Lesson_4.Endpoints.UPLOAD;
import static io.restassured.RestAssured.given;

public class FavoriteImageTest extends BaseTest {

    private String imageId;

    @BeforeEach
    void setUp() {

        Response response = given(requestUploadImage, simplePositiveResponseSpecification)
                .post(UPLOAD)
                .then()
                .extract()
                .response();

        imageId = response.body().as(PostImageResponse.class).getData().getId();
    }

    @Test
    void favoriteImageTest() {
        given(requestWithAuth, positiveResponseSpecification)
                .post(IMAGE_FAVORITE, imageId)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
            given(requestWithOutAuth, simplePositiveResponseSpecification)
                    .delete(IMAGE_ID, imageId)
                    .prettyPeek();
    }
}
