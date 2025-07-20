package ru.practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.steps.OrderSteps;

import static org.hamcrest.Matchers.*;

@DisplayName("Тесты для получения списка заказов")
public class OrderListTest extends BaseTest{

    private OrderSteps orderSteps;

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void shouldReturnOrderList() {
        ValidatableResponse response = orderSteps.getOrders()
                .assertThat()
                .statusCode(200)
                .body("orders", not(empty()))
                .body("orders", everyItem(hasKey("id")))
                .body("orders", everyItem(hasKey("track")))
                .body("orders", everyItem(hasKey("status")));



    }
}