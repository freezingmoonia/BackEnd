package Lesson_4;

import Lesson_4.dto.PostImageResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Lesson_4.Endpoints.IMAGE_DEL_HASH;
import static Lesson_4.Endpoints.UPLOAD_IMAGE;
import static Lesson_4.Endpoints.IMAGE_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateImageInformationTests extends BaseTest {

    private String imageId;
    private String deleteHash;

    @BeforeEach
    void setUp() {
        Response response = given(requestUploadImage, simplePositiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .then()
                .extract()
                .response();

        imageId = response.body().as(PostImageResponse.class).getData().getId();
        deleteHash = response.body().as(PostImageResponse.class).getData().getDeletehash();
    }

    @Test
    void updateImageInformationUnAuthedTest() {
        given()
                .spec(requestUploadImageWithTitle)
                .post(IMAGE_DEL_HASH, deleteHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);

        Response response = given()
                .spec(requestWithAuth)
                .get(IMAGE_ID, imageId)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response();

        assertThat(response.body().as(PostImageResponse.class).getData().getTitle(), equalTo(TITLE));
    }

    @Test
    void updateImageInformationAuthedTest() {
        given()
                .spec(requestUploadImageWithTitle)
                .post(IMAGE_ID, imageId)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);

        Response response = given()
                .spec(requestWithAuth)
                .get(IMAGE_ID, imageId)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response();

        assertThat(response.body().as(PostImageResponse.class).getData().getTitle(), equalTo(TITLE));
    }

    @AfterEach
    void tearDown() {
            given()
                    .spec(requestWithOutAuth)
                    .delete(IMAGE_ID, imageId)
                    .then()
                    .spec(simplePositiveResponseSpecification);
        }
}
