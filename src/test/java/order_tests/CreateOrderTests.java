package order_tests;

import api_constants.Api_constants;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order_data.IngredientInfo;
import order_data.OrderInfo;
import order_data.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userData.UserInfo;
import userData.UserRandomized;
import userData.UserSteps;
import static order_data.IngredientApi.getIngredientFromArray;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTests {

    private String accessToken;
    private Response response;
    private IngredientInfo ingredientInfo;

    @Before
    public void getData(){
        RestAssured.baseURI = Api_constants.BASE_URL;
        ingredientInfo = getIngredientFromArray();
    }

    @After
    public void deleteUser(){
        if(accessToken!=null){
            UserSteps.userDelete(accessToken);
        }
    }

    @Test
    @Description("Проверка создания заказа с авторизованным пользователем")
    @DisplayName("Проверка ответа сервера после создания заказа с авторизацией")
    public void testCreateOrderWithAuth(){
        UserInfo userInfo = UserRandomized.userWithRandomData();
        OrderInfo orderInfo = new OrderInfo(ingredientInfo);
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response = OrderSteps.createOrderWithAuth(orderInfo,accessToken);
        response
                .then()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @Description("Проверка создания заказа с не авторизованным пользователем")
    @DisplayName("Проверка ответа сервера после создания заказа без авторизации")
    public void testCreateOrderWithOutAuth(){
        OrderInfo orderInfo = new OrderInfo(ingredientInfo);
        response = OrderSteps.createOrderWithAuth(orderInfo,"");
        response
                .then()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @Description("Проверка создания заказа без ингредиентов")
    @DisplayName("Проверка ответа сервера при создании заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients(){
        UserInfo userInfo = UserRandomized.userWithRandomData();
        OrderInfo orderInfo = new OrderInfo();
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response = OrderSteps.createOrderWithoutAuth(orderInfo);
        response
                .then()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @Description("Проверка создания заказа с неверным хэшем ингредиентов")
    @DisplayName("Проверка ответа сервера при попытке создать заказ с неверным хэшем")
    public void testCreateOrderWithBadHash(){
        UserInfo userInfo = UserRandomized.userWithRandomData();
        ingredientInfo.set_id("WeCanSetBadID");
        OrderInfo orderInfo = new OrderInfo(ingredientInfo);
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response = OrderSteps.createOrderWithAuth(orderInfo, accessToken);
        response
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }


}
