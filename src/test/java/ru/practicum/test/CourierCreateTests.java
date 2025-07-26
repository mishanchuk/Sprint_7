package ru.practicum.test;

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
@DisplayName("Тесты для создания курьеров")
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
    @Description("Позитивный тест: проверка создания курьера с валидными данными. Ожидается код ответа 201 и ok: true")
    public void successfulCourierCreationTest() {
        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания дубликата курьера")
    @Description("Негативный тест: попытка создания курьера с уже существующим логином. Ожидается код ответа 409 и сообщение об ошибке")
    public void duplicateCourierCreationTest() {
        courierSteps.createCourier(courier);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Проверка создания без логина")
    @Description("Негативный тест: попытка создания курьера без указания логина. Ожидается код ответа 400 и сообщение об ошибке")
    public void creationWithoutLoginTest() {
        courier.setLogin(null);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка создания без пароля")
    @Description("Негативный тест: попытка создания курьера без указания пароля. Ожидается код ответа 400 и сообщение об ошибке")
    public void creationWithoutPasswordTest() {
        courier.setPassword(null);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка создания без имени")
    @Description("Позитивный тест: проверка создания курьера без указания имени. Ожидается код ответа 201, так как имя не является обязательным полем")
    public void creationWithoutFirstNameTest() {
        courier.setFirstName(null);

        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания с существующим логином")
    @Description("Негативный тест: попытка создания другого курьера с уже существующим логином. Ожидается код ответа 409 и сообщение об ошибке")
    public void creationWithExistingLoginTest() {
        courierSteps.createCourier(courier);

        Courier anotherCourier = new Courier()
                .setLogin(courier.getLogin())
                .setPassword("different_password")
                .setFirstName("different_name");

        courierSteps.createCourier(anotherCourier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }
    @After
    @Step("Удаление тестового курьера")
    @Description("Получение ID курьера и его удаление после выполнения теста")
    public void tearDown() {
        try {
            // Получаем ID курьера через авторизацию
            ValidatableResponse loginResponse = courierSteps.loginCourier(courier);
            if (loginResponse.extract().statusCode() == SC_OK) {
                Integer courierId = loginResponse.extract().path("id");
                if (courierId != null) {
                    courierSteps.deleteCourier(courierId)
                            .assertThat()
                            .statusCode(SC_OK);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении курьера: " + e.getMessage());
        }
    }


}