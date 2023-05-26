package user_tests;

import com.github.javafaker.Faker;
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

public class UserEditDataTests {
    String accessToken;
    private Response response;
    private UserInfo userInfo;
    static Faker faker = new Faker();

    @Before
    public void createTestUser(){
        userInfo = UserRandomized.userWithRandomData();
        response = UserSteps.userCreate(userInfo);
        accessToken = response.then().extract().body().path("accessToken");
    }

    @After
    public void deleteTestUser(){
        if(accessToken!=null){
            UserSteps.userDelete(accessToken);}
    }

    @Test
    @DisplayName("Успешное изменение данных авторизированного пользователя")
    @Description("Проверка ответа сервера при изменении данных авторизованного пользователя")
    public void testSuccessEditAuthorizedUserProfile(){
        userInfo.setName(faker.name().firstName());
        userInfo.setPassword(faker.internet().password());
        userInfo.setEmail(faker.internet().emailAddress());
        response = UserSteps.userProfileEdit(accessToken, userInfo);
        response
                .then()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Ошибка изменения данных авторизированного пользователя")
    @Description("Проверка ответа сервера при изменении данных неавторизованного пользователя")
    public void testFailedEditAuthorizedUserProfile(){
        userInfo.setName(faker.name().firstName());
        userInfo.setPassword(faker.internet().password());
        userInfo.setEmail(faker.internet().emailAddress());
        response = UserSteps.userProfileEdit("", userInfo);
        response
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}
