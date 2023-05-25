package order_data;

public class OrderInfo {
    private String[] ingredients;
    private String _id;
    private String status;
    private String number;

    public OrderInfo(){
    }

    public OrderInfo(IngredientInfo ingredientInfo){
        this.ingredients = new String[]{
                ingredientInfo.get_id()
        };
    }




}
