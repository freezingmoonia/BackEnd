package Lesson_3;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class NegativeImageTests extends BaseTest {
    private final String DELETE_HASH = "ABCDEFGHIJKLMNO";
    private final String IMAGE_ID = "ABCDEFG";

    @Test
    void fakeHashImageDeleteUnAuthed() {
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/image/{deleteHash}", DELETE_HASH)
                .prettyPeek()
                .then()
                .statusCode(400);       //Удаляется несмотря ни на что .. баг в общем:)
    }

    @Test
    void emptyUploadedImageTest() {
        given()
                .header("Authorization", token)
                .multiPart("image", "")
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @Test
    void fakeGetImageTest() {
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageId}", IMAGE_ID)
                .prettyPeek()
                .then()
                .statusCode(404);
    }
}