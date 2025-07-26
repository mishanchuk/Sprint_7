package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.config.RestConfig;
import ru.practicum.model.Order;

import java.util.Map;

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
    @Step("Получить список заказов")
    public ValidatableResponse getOrders() {
        return given()
                .when()
                .get(RestConfig.ORDER_PATH)
                .then();
    }
    @Step("Отменить заказ по track-номеру {track}")
    public ValidatableResponse cancelOrder(Map<String, Object> body) {
        return given()
                .body(body)
                .when()
                .put(RestConfig.CENCEL_ORDER_PATH)
                .then();
    }
}