package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(
                System.getProperty("db.url"),
                System.getProperty("db.user"),
                System.getProperty("db.password"));
    }

    @SneakyThrows
    public static void cleanDatabase() {
    }
    @SneakyThrows
    public static String getDebitCardStatus() {
        var statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return null;
    }

    @SneakyThrows
    public static String getCreditCardStatus() {
        var statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return null;
    }

    @SneakyThrows
    public static int getDbRecordAmount() {
        var amountSQL = "SELECT amount FROM payment_entity ORDER BY created DESC LIMIT 1";
        return 0;
    }

}