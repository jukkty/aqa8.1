package ru.netology.tests;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.LoginPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    @BeforeEach
    void setUp() {
        open("http:localhost:7777");
    }

    @AfterAll
    static void theEnd() throws SQLException {
        DataHelper.cleanAuthCodes();
    }

    @Test
    @DisplayName("Должен логиниться в личный кабинет при валидных данных")
    void shouldLogin() throws SQLException {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = verificationPage.validCode();
        verificationCode.checkIfEverythingFine();
    }

    @Test
    @DisplayName("Не должен логиниться в личный кабинет при вводе невалидного кода")
    void shouldNotLoginWithInvalidCode() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidCode();
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
    void shouldBeBlockedAfterThirdWrongAttempt() throws SQLException {
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
