package order_data;

import api_constants.Api_constants;

import static io.restassured.RestAssured.given;

public class IngredientApi {
    private boolean success;
    private IngredientInfo[] data;

    //REQUEST

    public static IngredientInfo[] getIngredientsArray() {
        return getIngredientResponse().getIngredients();
    }

    public static IngredientApi getIngredientResponse() {
        return given()
                .get(Api_constants.INGREDIENTS)
                .as(IngredientApi.class);
    }
    public static IngredientInfo getIngredientFromArray() {
        return getIngredientsArray()[0];
    }

    //RESPONSE

    public IngredientInfo[] getIngredients() {
        return data;
    }

    public void setIngredients(IngredientInfo[] ingredients) {
        this.data = ingredients;
    }

}
