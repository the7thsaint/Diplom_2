package user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import userData.UserInfo;
import userData.UserRandomized;
import userData.UserSteps;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RegisterUserTests {
    String accessToken;
    private UserInfo userInfo;
    private Response response;

    @After
    public void deleteTestUser(){
        if (accessToken != null){
            UserSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Проверка регистрации пользователя с валидными данными")
    @Description("Регистрация пользователя рандомайзером с валидными данными")
    public void testRegisterNewUser(){
        userInfo = UserRandomized.userWithRandomData();
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Проверка регистрации с пустым полем name")
    @Description("Проверяем ошибку при пустом имени")
    public void testFailRegisterWithEmptyName(){
        userInfo = new UserInfo("", "eee@saf","fdfdfdf");
        response = UserSteps.userCreate(userInfo);
        response
                .then().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка регистрации с пустым полем email")
    @Description("Проверяем ошибку при пустой почте")
    public void testFailRegisterWithEmptyEmail(){
        userInfo = new UserInfo("fdfdf", "","fdfdfdf");
        response = UserSteps.userCreate(userInfo);
        response
                .then().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка регистрации с пустым полем password")
    @Description("Проверяем ошибку при пустом пароле")
    public void testFailRegisterWithEmptyPassword(){
        userInfo = new UserInfo("fdfdf", "test@test.ru","");
        response = UserSteps.userCreate(userInfo);
        response
                .then().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка попытки регистрации с уже зарегестрированным пользователем")
    @Description("Проверяем правильность отработки апи при попытке регистрации с уже зарегестрированным пользователем")
    public void testFailRegisterWithRegistredUser(){
        userInfo = UserRandomized.userWithRandomData();
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(SC_OK);
        response = UserSteps.userCreate(userInfo);
        response
                .then().body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
}
