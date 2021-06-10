package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement code = $("[data-test-id=code] input");
    private final SelenideElement button = $("[data-test-id='action-verify']");

    public DashboardPage validCode() throws SQLException {
        code.setValue(DataHelper.getVerificationCode());
        button.click();
        return new DashboardPage();
    }

    public void invalidCode() {
        code.setValue(DataHelper.getWrongVerificationCode());
        button.click();
    }
}
