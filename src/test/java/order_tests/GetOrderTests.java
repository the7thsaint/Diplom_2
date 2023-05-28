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
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTests {

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
    @Description("Проверка получения заказов с авторизацией")
    @DisplayName("Проверка ответа сервера при запросе заказов авторизованного пользователя")
    public void testGetOrdersWithAuth(){
        OrderInfo orderInfo = new OrderInfo(ingredientInfo);
        UserInfo userInfo = UserRandomized.userWithRandomData();
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response = OrderSteps.createOrderWithAuth(orderInfo, accessToken);
        response = OrderSteps.getUserOrders(accessToken);
        response
                .then()
                .body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @Description("Проверка получения заказов без авторизации")
    @DisplayName("Проверка ответа сервера при запросе заказов не авторизированным пользователем")
    public void testGetOrdersWithoutAuth(){
        OrderInfo orderInfo = new OrderInfo(ingredientInfo);
        response = OrderSteps.createOrderWithoutAuth(orderInfo);
        response = OrderSteps.getUserOrders("fdfdfd");
        response
                .then()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
}
