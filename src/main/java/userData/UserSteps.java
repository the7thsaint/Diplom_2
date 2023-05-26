package userData;

import api_constants.Api_constants;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static api_constants.Api_constants.*;
import static io.restassured.RestAssured.given;

public class UserSteps {
    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    @Step("Регистрация нового пользователя")
    public static Response userCreate(UserInfo user) {
        return requestSpec()
                .body(user)
                .post(CREATE_USER);
    }

    @Step("Изменение профиля пользователя")
    public static  Response userProfileEdit(String accessToken, UserInfo userInfo) {
        return requestSpec()
                .header("Authorization", accessToken)
                .body(userInfo)
                .patch(USER_AUTH);
    }


    @Step("Авторизация пользователя по логину и токену")
    public static Response loginUserWithAuthToken(UserInfo user, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(user)
                .post(LOGIN_USER);
    }
    @Step("Логин пользователя c логином и паролем")
    public static Response loginUser(UserInfo userInfo){
        return requestSpec()
                .body(userInfo)
                .post(LOGIN_USER);
    }

    @Step("Удаление пользователя")
    public static void userDelete(String token) {
        requestSpec()
                .header("Authorization", token)
                .delete(USER_AUTH);
    }
}
