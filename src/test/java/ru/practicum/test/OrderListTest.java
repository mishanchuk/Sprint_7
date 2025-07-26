package ru.practicum.test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.steps.OrderSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты для получения списка заказов")
public class OrderListTest extends BaseTest{

    private OrderSteps orderSteps;

    @Before
    @Step("Подготовка тестовых данных")
    @Description("Инициализация тестового окружения: настройка логирования и создание экземпляра OrderSteps")
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Позитивный тест: проверка корректности ответа при запросе списка заказов. "
            + "Ожидается: HTTP 200 OK, непустой массив заказов, каждый заказ должен содержать обязательные поля: "
            + "id, track и status")
    public void shouldReturnOrderListTest() {
        ValidatableResponse response = orderSteps.getOrders()
                .assertThat()
                .statusCode(SC_OK)
                .body("orders", not(empty()))
                .body("orders", everyItem(hasKey("id")))
                .body("orders", everyItem(hasKey("track")))
                .body("orders", everyItem(hasKey("status")));



    }
}