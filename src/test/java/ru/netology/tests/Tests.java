package ru.netology.tests;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @BeforeEach
    void setUp() {
        open("http:localhost:7777");
        DataHelper.cleanAuth();
    }

    @AfterAll
    static void cleanDB(){
        DataHelper.cleanDB();
    }


    @Test
    @DisplayName("Должен логиниться в личный кабинет при валидных данных")
    void shouldLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        verificationPage.validCode(verificationCode);
        val dashboardPage = new DashboardPage();
        dashboardPage.checkIfEverythingFine();
    }

    @Test
    @DisplayName("Не должен логиниться в личный кабинет при вводе невалидного кода")
    void shouldNotLoginWithInvalidCode() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getWrongVerificationCode();
        verificationPage.validCode(verificationCode);
        loginPage.error();
    }

    @Test
    @DisplayName("Не должен логиниться в личный кабинет при вводе невалидного пароля")
    void shouldNotLoginWithInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getWrongAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.error();
    }

    @Test
    @DisplayName("Должен блокировать пользователя после ввода неверного пароля больше 3-х раз")
    void shouldBeBlockedAfterThirdWrongAttempt() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getWrongAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.error();
        loginPage.clear();
        loginPage.invalidLogin(authInfo);
        loginPage.error();
        loginPage.clear();
        loginPage.invalidLogin(authInfo);
        loginPage.error();
        String status = DataHelper.getStatus();
        assertEquals("blocked", status);
    }
}
