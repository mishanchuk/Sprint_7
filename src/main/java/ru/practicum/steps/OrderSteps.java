package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.config.RestConfig;
import ru.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создать заказ")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(RestConfig.ORDER_PATH)
                .then();
    }
}