package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getWrongAuthInfo() {
        Faker faker = new Faker();
        return new AuthInfo("vasya", faker.internet().password());

    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getWrongVerificationCode() {
        Faker faker = new Faker();
        return new VerificationCode(faker.internet().password());
    }

    public static VerificationCode getVerificationCode() {
        String usersId = null;
        val idSQL = "SELECT id FROM app.users WHERE login = ?";
        try (val conn = getConnection();
             val idStatement = conn.prepareStatement(idSQL)) {
            idStatement.setString(1, "vasya");
            try (val rs = idStatement.executeQuery()) {
                if (rs.next()) {
                    usersId = rs.getString("id");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String code = null;
        val authSQL = "SELECT code FROM app.auth_codes ac WHERE user_id = ? ORDER by created DESC limit 1";
        try (val conn = getConnection();
             val idStatement = conn.prepareStatement(authSQL)) {
            idStatement.setString(1, usersId);
            try (val rs = idStatement.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new VerificationCode(code);
    }

    public static String getStatus() {
        String status = null;
        val statusSQL = "select status from app.users u where login = ?";
        try (val conn = getConnection();
             val statusStatement = conn.prepareStatement(statusSQL)) {
            statusStatement.setString(1, "vasya");
            try (val rs = statusStatement.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

    public static void cleanDB() {
        val deleteAuthCodes = "DELETE from auth_codes;";
        val deleteCards = "DELETE from app.cards;";
        val deleteUsers = "DELETE from app.users;";

        try (val conn = getConnection();
             val deleteAuthSt = conn.createStatement();
             val deleteCardsStmt = conn.createStatement();
             val deleteUsersStmt = conn.createStatement()) {
            deleteAuthSt.executeUpdate(deleteAuthCodes);
            deleteCardsStmt.executeUpdate(deleteCards);
            deleteUsersStmt.executeUpdate(deleteUsers);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void cleanAuth() {
        val deleteAuthCodes = "DELETE from auth_codes;";
        try (val conn = getConnection();
             val deleteAuthSt = conn.createStatement()) {
            deleteAuthSt.executeUpdate(deleteAuthCodes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}