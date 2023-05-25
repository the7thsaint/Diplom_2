package order_data;

import api_constants.Api_constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создание заказа авторизированным пользователем")
    public Response createOrderWithAuth(OrderInfo orderInfo, String accessToken){
        return given()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .baseUri(Api_constants.BASE_URL)
                .body(orderInfo)
                .post(Api_constants.ORDERS);
    }

    @Step("Создание заказа не авторизированным пользователем")
    public Response createOrderWithoutAuth(OrderInfo orderInfo){
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Api_constants.BASE_URL)
                .body(orderInfo)
                .post(Api_constants.ORDERS);
    }

    @Step("Получение ингредиентов")
    public Response getIngredients(){
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Api_constants.BASE_URL)
                .get(Api_constants.INGREDIENTS);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String accessToken){
        return given()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .baseUri(Api_constants.BASE_URL)
                .get(Api_constants.ORDERS);
    }

}
