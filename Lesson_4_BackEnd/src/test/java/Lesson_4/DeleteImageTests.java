package Lesson_4;

import Lesson_4.dto.PostImageResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Lesson_4.Endpoints.IMAGE_DEL_HASH;
import static Lesson_4.Endpoints.UPLOAD_IMAGE;
import static Lesson_4.Endpoints.IMAGE_ID;
import static io.restassured.RestAssured.given;

public class DeleteImageTests extends BaseTest {

    private Response response;
    private String imageId;
    private String deleteHash;

    @BeforeEach
    void setUp() {
        response = given()
                .spec(requestUploadImage)
                .post(UPLOAD_IMAGE)
                .then()
                .spec(simplePositiveResponseSpecification)
                .extract()
                .response();

        imageId = response.body().as(PostImageResponse.class).getData().getId();
        deleteHash = response.body().as(PostImageResponse.class).getData().getDeletehash();
    }

    @Test
    void imageDeleteUnAuthedTest() {
        given()
                .spec(requestWithAuthAndLogs)
                .delete(IMAGE_DEL_HASH, deleteHash)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Test
    void imageDeletionAuthedTest() {
        given()
                .spec(requestWithAuthAndLogs)
                .delete(IMAGE_ID, imageId)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }
}
