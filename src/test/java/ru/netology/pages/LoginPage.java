package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.*;

public class LoginPage {
    private final SelenideElement login = $("[data-test-id=login] input");
    private final SelenideElement password = $("[data-test-id=password] input");
    private final SelenideElement button = $("[data-test-id='action-login']");
    private final SelenideElement error = $("[data-test-id='error-notification']");


    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        login.setValue(authInfo.getLogin());
        password.setValue(authInfo.getPassword());
        button.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo authInfo) {
        login.setValue(authInfo.getLogin());
        password.setValue(authInfo.getPassword());
        button.click();
    }

    public void error() {
        error.shouldBe(Condition.visible);
    }

    public void clear() {
        login.sendKeys(chord(CONTROL, "a"), BACK_SPACE);
        password.sendKeys(chord(CONTROL, "a"), BACK_SPACE);
    }
}
