package order_tests;

import io.restassured.response.Response;
import order_data.IngredientInfo;
import userData.UserSteps;

public class CreateOrderTests {
    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private Response response;
    private IngredientInfo ingredientInfo;
}
