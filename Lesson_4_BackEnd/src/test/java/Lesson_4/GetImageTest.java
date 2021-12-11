package Lesson_4;

import Lesson_4.dto.PostImageResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Lesson_4.Endpoints.UPLOAD_IMAGE;
import static Lesson_4.Endpoints.IMAGE_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetImageTest extends BaseTest {

    private String imageId;

    @BeforeEach
    void setUp() {

        Response response = given(requestUploadImageWithTitle, simplePositiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .then()
                .extract()
                .response();

        imageId = response.body().as(PostImageResponse.class).getData().getId();
    }

    @Test
    void getImageTest() {
        Response response = given(requestWithAuth, positiveResponseSpecification)
                .get(IMAGE_ID, imageId)
                .prettyPeek()
                .then()
                .extract()
                .response();

        assertThat(response.body().as(PostImageResponse.class).getData().getTitle(), equalTo(TITLE));
    }

    @AfterEach
    void tearDown() {
        given(requestWithOutAuth, simplePositiveResponseSpecification)
                .delete(IMAGE_ID, imageId);
    }
}
