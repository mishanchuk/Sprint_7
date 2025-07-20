package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.config.RestConfig;
import ru.practicum.model.Courier;

import static io.restassured.RestAssured.given;

public class CourierSteps {


    @Step("Создать нового курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(RestConfig.COURIER_PATH)
                .then();
    }

    @Step("Авторизовать курьера")
    public ValidatableResponse loginCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(RestConfig.LOGIN_PATH)
                .then();
    }

    @Step("Удалить курьера по ID")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .pathParam("id", courierId)
                .when()
                .delete(RestConfig.DELETE_PATH)
                .then();
    }

    @Step("Получить ID курьера")
    public int getCourierId(Courier courier) {
        return loginCourier(courier)
                .extract()
                .path("id");
    }
}