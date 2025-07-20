package ru.practicum.test;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import net.datafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.model.Order;
import ru.practicum.steps.OrderSteps;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Тесты создания заказа с разными цветами")
public class OrderCreateTest extends BaseTest{

    private final OrderSteps orderSteps = new OrderSteps();
    private final List<String> colors;
    private Order order;
    private static final Faker faker = new Faker(new Locale("ru"));

    public OrderCreateTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Before
    @Step("Подготовка тестовых данных")
    public void setUpOrder() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        order = new Order()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setAddress(faker.address().fullAddress())
                .setMetroStation(faker.number().digits(2))
                .setPhone(faker.phoneNumber().phoneNumber())
                .setRentTime(faker.number().numberBetween(1, 7))
                .setDeliveryDate("2024-03-01")
                .setComment(faker.lorem().sentence())
                .setColor(colors);
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void shouldCreateOrderWithDifferentColors() {
        orderSteps.createOrder(order)
                .assertThat()
                .statusCode(201)
                .body("track", notNullValue());
    }
}