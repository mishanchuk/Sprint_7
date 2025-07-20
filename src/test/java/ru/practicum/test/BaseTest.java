package ru.practicum.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import org.junit.Before;
import ru.practicum.config.RestConfig;

public class BaseTest {
    @Before
    public void startUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(RestConfig.HOST)
                .setContentType(ContentType.JSON)
                .build();
RestAssured.config = RestAssured
        .config()
        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

    }
}
