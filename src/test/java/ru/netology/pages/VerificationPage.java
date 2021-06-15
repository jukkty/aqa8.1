package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;


import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement code = $("[data-test-id=code] input");
    private final SelenideElement button = $("[data-test-id='action-verify']");

    public DashboardPage validCode(DataHelper.VerificationCode verificationCode) {
        code.setValue(verificationCode.getCode());
        button.click();
        return new DashboardPage();
    }
}
