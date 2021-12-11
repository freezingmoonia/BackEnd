package Lesson_4;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Lesson_4.Endpoints.*;
import static io.restassured.RestAssured.given;

public class NegativeImageTests extends BaseTest {

    private final String FAKE_DELETE_HASH = "ABCDEFGHIJKLMNO";
    private final String FAKE_IMAGE_ID = "ABCDEFG";

    static RequestSpecification requestUploadImageWithNothing;
    static ResponseSpecification negativeResponseSpec400;
    static ResponseSpecification negativeResponseSpec404;

    @BeforeEach
    void beforeTest() {
        requestUploadImageWithNothing = new RequestSpecBuilder()
                .addHeader("Authorization", accessToken)
                .addMultiPart("image", "")
                .build();

        negativeResponseSpec400 = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();

        negativeResponseSpec404 = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build();
    }

    @Test
    void emptyUploadedImageTest() {
        given(requestUploadImageWithNothing, negativeResponseSpec400)
                .post(UPLOAD)
                .prettyPeek();
    }

    @Test
    void fakeGetImageTest() {
        given(requestWithAuth, negativeResponseSpec404)
                .get(IMAGE_ID, FAKE_IMAGE_ID)
                .prettyPeek();
    }

    @Test
    void fakeHashImageDeleteUnAuthed() {
        given()
                .spec(requestWithAuth)
                .delete(IMAGE_DEL_HASH, FAKE_DELETE_HASH)
                .prettyPeek()
                .then()
                .spec(negativeResponseSpec404);      //Удаляется несмотря ни на что .. баг в общем:)
    }
}