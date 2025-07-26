package ru.practicum.test;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.model.Courier;
import ru.practicum.steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
@DisplayName("Тесты для авторизации курьеров")
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
    @DisplayName("Успешная авторизация")
    @Description("Позитивный тест: Проверка успешной авторизации с валидными учетными данными. Ожидается код 200 и наличие id в ответе")
    public void shouldLoginSuccessfullyTest() {
        ValidatableResponse response = courierSteps.loginCourier(validCourier);

        response
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Негативный тест: Попытка авторизации без указания логина. Ожидается код 400 и сообщение о недостатке данных")
    public void shouldNotLoginWithoutLoginTest() {
        Courier courierWithoutLogin = new Courier()
                .setPassword(validCourier.getPassword());

        courierSteps.loginCourier(courierWithoutLogin)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Негативный тест: Попытка авторизации без указания пароля. Ожидается код 400 и сообщение о недостатке данных")
    public void shouldNotLoginWithoutPasswordTest() {
        Courier courierWithoutPassword = new Courier()
                .setLogin(validCourier.getLogin());

        courierSteps.loginCourier(courierWithoutPassword)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логин")
    @Description("Негативный тест: Попытка авторизации с неверным логином. Ожидается код 404 и сообщение о ненайденной учетной записи")
    public void shouldNotLoginWithWrongLoginTest() {
        Courier courierWithWrongLogin = new Courier()
                .setLogin("wrong_" + validCourier.getLogin())
                .setPassword(validCourier.getPassword());

        courierSteps.loginCourier(courierWithWrongLogin)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Негативный тест: Попытка авторизации с неверным паролем. Ожидается код 404 и сообщение о ненайденной учетной записи")
    public void shouldNotLoginWithWrongPasswordTest() {
        Courier courierWithWrongPassword = new Courier()
                .setLogin(validCourier.getLogin())
                .setPassword("wrong_" + validCourier.getPassword());

        courierSteps.loginCourier(courierWithWrongPassword)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем")
    @Description("Негативный тест: Попытка авторизации несуществующего пользователя. Ожидается код 404 и сообщение о ненайденной учетной записи")
    public void shouldNotLoginNonExistentUserTest() {
        Faker faker = new Faker();
        Courier nonExistentCourier = new Courier()
                .setLogin(faker.name().username())
                .setPassword(faker.internet().password());

        courierSteps.loginCourier(nonExistentCourier)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (validCourier == null || validCourier.getLogin() == null) {
            return;
        }

        try {
            ValidatableResponse loginResponse = courierSteps.loginCourier(validCourier);
            if (loginResponse.extract().statusCode() == SC_OK) {
                Integer id = loginResponse.extract().path("id");
                if (id != null) {
                    courierSteps.deleteCourier(id);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке тестовых данных: " + e.getMessage());
             Allure.addAttachment("Ошибка очистки", e.getMessage());
        }
    }
}