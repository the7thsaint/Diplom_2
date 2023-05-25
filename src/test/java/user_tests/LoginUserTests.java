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

public class LoginUserTests {
    String accessToken;
    protected UserRandomized userRandomized = new UserRandomized();
    private UserInfo userInfo;
    private UserSteps userSteps = new UserSteps();
    private Response response;

    @Before
    public void createTestUser(){
        userInfo = UserRandomized.userWithRandomData();
        response = userSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
    }

    @After
    public void deleteUser(){
        if(accessToken!=null){
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Логин пользователя с валидными значениями")
    @Description("Проверка ответа сервера при успешном логине")
    public void testSuccessUserLogin(){
        response = userSteps.loginUserWithAuthToken(userInfo, accessToken);
        response
                .then().statusCode(SC_OK);
    }
}
