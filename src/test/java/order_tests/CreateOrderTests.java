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
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTests {
    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private Response response;
    private IngredientInfo ingredientInfo;
    private final OrderSteps orderSteps = new OrderSteps();

    @Before
    public void getData(){
        RestAssured.baseURI = Api_constants.BASE_URL;
        ingredientInfo = getIngredientFromArray();
    }

    @After
    public void deleteUser(){
        if(accessToken!=null){
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @Description("Проверка создания заказа с авторизованным пользователем")
    @DisplayName("Проверка ответа сервера после создания заказа с авторизацией")
    public void testCreateOrderWithAuth(){
        UserInfo userInfo = UserRandomized.userWithRandomData();
        OrderInfo orderInfo = new OrderInfo(ingredientInfo);
        response = userSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithAuth(orderInfo,accessToken);
        response
                .then()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }
}
