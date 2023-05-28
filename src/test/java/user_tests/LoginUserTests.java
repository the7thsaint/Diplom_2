package user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userData.UserInfo;
import userData.UserRandomized;
import userData.UserSteps;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class LoginUserTests {
    String accessToken;
    private UserInfo userInfo;
    private Response response;

    @Before
    public void createTestUser() {
        userInfo = UserRandomized.userWithRandomData();
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Логин пользователя с валидными значениями и токеном")
    @Description("Проверка ответа сервера при успешном логине")
    public void testSuccessUserLoginWithToken() {
        response = UserSteps.loginUserWithAuthToken(userInfo, accessToken);
        response
                .then().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Логин пользователя с невалидными данными")
    @Description("Проверка ответа сервера при неуспешном логине")
    public void testFailedLoginWithNotValidData() {
        userInfo.setEmail("FFFFF@ssss.ru");
        userInfo.setPassword("WQWQWQW23233342442244242");
        response = UserSteps.loginUser(userInfo);
        response
                .then().statusCode(SC_UNAUTHORIZED);
    }
}
