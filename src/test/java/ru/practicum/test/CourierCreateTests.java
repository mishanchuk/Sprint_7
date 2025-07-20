package ru.practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import net.datafaker.Faker;

import org.junit.Before;
import org.junit.Test;
import ru.practicum.model.Courier;
import ru.practicum.steps.CourierSteps;

import static org.hamcrest.Matchers.*;

public class CourierCreateTests extends BaseTest{
    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    @Step("Подготовка тестовых данных")
    public void setUpCourier() {
        Faker faker = new Faker();
        courier = new Courier()
                .setLogin(faker.name().username())
                .setPassword(faker.internet().password())
                .setFirstName(faker.name().firstName());
    }

    @Test
    @DisplayName("Проверка успешного создания курьера")
    public void successfulCourierCreation() {
        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания дубликата курьера")
    public void duplicateCourierCreation() {
        courierSteps.createCourier(courier);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Проверка создания без логина")
    public void creationWithoutLogin() {
        courier.setLogin(null);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка создания без пароля")
    public void creationWithoutPassword() {
        courier.setPassword(null);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка создания без имени")
    public void creationWithoutFirstName() {
        courier.setFirstName(null);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания с существующим логином")
    public void creationWithExistingLogin() {
        courierSteps.createCourier(courier);

        Courier anotherCourier = new Courier()
                .setLogin(courier.getLogin())
                .setPassword("different_password")
                .setFirstName("different_name");

        courierSteps.createCourier(anotherCourier)
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }



}