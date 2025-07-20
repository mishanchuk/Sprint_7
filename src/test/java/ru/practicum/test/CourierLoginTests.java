package ru.practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.model.Courier;
import ru.practicum.steps.CourierSteps;

import static org.hamcrest.Matchers.*;

public class CourierLoginTests extends BaseTest{
    private CourierSteps courierSteps;
    private Courier validCourier;
    private int createdCourierId;

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        courierSteps = new CourierSteps();
        Faker faker = new Faker();

        validCourier = new Courier()
                .setLogin(faker.name().username())
                .setPassword(faker.internet().password())
                .setFirstName(faker.name().firstName());

        // Создаем курьера для позитивных тестов
        courierSteps.createCourier(validCourier);
    }

    @Test
    @DisplayName("Позитивный тест: успешная авторизация")
    public void shouldLoginSuccessfully() {
        ValidatableResponse response = courierSteps.loginCourier(validCourier);

        response
                .statusCode(200)
                .body("id", notNullValue());

        createdCourierId = response.extract().path("id");
    }

    @Test
    @DisplayName("Негативный тест: авторизация без логина")
    public void shouldNotLoginWithoutLogin() {
        Courier courierWithoutLogin = new Courier()
                .setPassword(validCourier.getPassword());

        courierSteps.loginCourier(courierWithoutLogin)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Негативный тест: авторизация без пароля")
    public void shouldNotLoginWithoutPassword() {
        Courier courierWithoutPassword = new Courier()
                .setLogin(validCourier.getLogin());

        courierSteps.loginCourier(courierWithoutPassword)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Негативный тест: неверный логин")
    public void shouldNotLoginWithWrongLogin() {
        Courier courierWithWrongLogin = new Courier()
                .setLogin("wrong_" + validCourier.getLogin())
                .setPassword(validCourier.getPassword());

        courierSteps.loginCourier(courierWithWrongLogin)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Негативный тест: неверный пароль")
    public void shouldNotLoginWithWrongPassword() {
        Courier courierWithWrongPassword = new Courier()
                .setLogin(validCourier.getLogin())
                .setPassword("wrong_" + validCourier.getPassword());

        courierSteps.loginCourier(courierWithWrongPassword)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Негативный тест: несуществующий пользователь")
    public void shouldNotLoginNonExistentUser() {
        Faker faker = new Faker();
        Courier nonExistentCourier = new Courier()
                .setLogin(faker.name().username())
                .setPassword(faker.internet().password());

        courierSteps.loginCourier(nonExistentCourier)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (createdCourierId != 0) {
            courierSteps.deleteCourier(createdCourierId);
        }
    }
}