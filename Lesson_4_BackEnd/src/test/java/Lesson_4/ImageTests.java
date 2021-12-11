package Lesson_4;

import Lesson_4.dto.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static Lesson_4.Endpoints.UPLOAD;
import static Lesson_4.Endpoints.IMAGE_DEL_HASH;
import static Lesson_4.Endpoints.UPLOAD_IMAGE;
import static io.restassured.RestAssured.given;

public class ImageTests extends BaseTest {

    private final String PATH_URL = "https://wallbox.ru/resize/1280x800/wallpapers/main/201314/20528719ddeb401.jpg";
    MultiPartSpecification base64MultiPartSpec;
    static String encodedFile;
    String deleteHash;
    RequestSpecification requestSpecificationWithAutoWithBase64;
    RequestSpecification requestSpecificationWithAutoWithUrl;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent(PATH_TO_IMAGE);
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecificationWithAutoWithBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestWithAuth)
                .addMultiPart(base64MultiPartSpec)
                .build();

        requestSpecificationWithAutoWithUrl = new RequestSpecBuilder()
                .addRequestSpecification(requestWithAuth)
                .addMultiPart("image", PATH_URL)
                .addMultiPart("type", "url")
                .build();
    }

    @Test
    void uploadedFileTest() {
        Response response = given()
                .spec(requestSpecificationWithAutoWithBase64)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response();

        deleteHash = response.body().as(PostImageResponse.class).getData().getDeletehash();
    }

    @Test
    void uploadedImageTest() {
        Response response = given()
                .spec(requestUploadImage)
                .post(UPLOAD)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .response();

        deleteHash = response.body().as(PostImageResponse.class).getData().getDeletehash();
    }

    @Test
    void uploadedImageURLTest() {
        Response response = given(requestSpecificationWithAutoWithUrl)
                .post(UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response();

        deleteHash = response.body().as(PostImageResponse.class).getData().getDeletehash();
    }

    @AfterEach
    void tearDown() {
        given()
                .spec(requestWithAuth)
                .delete(IMAGE_DEL_HASH, deleteHash)
                .then()
                .spec(simplePositiveResponseSpecification);

    }

    private byte[] getFileContent(String PATH) {
        byte[] byteArray = new byte[0];

        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
