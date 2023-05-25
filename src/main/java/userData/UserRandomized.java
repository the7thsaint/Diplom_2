package userData;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserRandomized {
    public static Faker faker = new Faker();

    @Step("Создание нового пользователя с рандомными данными")
    public static UserInfo userWithRandomData() {
        return new UserInfo(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password());
    }
}
