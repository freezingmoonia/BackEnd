package Lesson_4;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public abstract class BaseTest {

    static final String PATH_TO_IMAGE = "src/test/resources/island.jpg";
    static final String TITLE = "island";

    static RequestSpecification requestWithAuth = null;
    static RequestSpecification requestWithOutAuth = null;
    static RequestSpecification requestWithAuthAndLogs = null;
    static RequestSpecification requestUploadImage = null;
    static RequestSpecification requestUploadImageWithTitle = null;
    static ResponseSpecification simplePositiveResponseSpecification = null;
    static ResponseSpecification positiveResponseSpecification = null;

    static Properties properties = new Properties();
    static String token;
    static String accessToken;
    static String username;
    static String baseApiUrl;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token = properties.getProperty("token");
        accessToken = properties.getProperty("accessToken");
        username = properties.getProperty("username");
        baseApiUrl = properties.getProperty("baseApiUrl");
        RestAssured.baseURI = baseApiUrl;

        requestWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestWithOutAuth = new RequestSpecBuilder()
                .addHeader("Authorization", accessToken)
                .build();

        requestWithAuthAndLogs = new RequestSpecBuilder()
                .addRequestSpecification(requestWithAuth)
                .log(LogDetail.METHOD)
                .log(LogDetail.URI)
                .build();

        requestUploadImage = new RequestSpecBuilder()
                .addRequestSpecification(requestWithAuth)
                .addMultiPart("image", new File(PATH_TO_IMAGE))
                .build();

        requestUploadImageWithTitle = new RequestSpecBuilder()
                .addRequestSpecification(requestUploadImage)
                .addMultiPart("title", TITLE)
                .build();

        simplePositiveResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        positiveResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(simplePositiveResponseSpecification)
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectContentType(ContentType.JSON)
                .build();
    }

    private static void getProperties() {
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
