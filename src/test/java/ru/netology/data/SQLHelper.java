package ru.netology.data;

import com.github.javafaker.DateAndTime;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

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
        try (var connection = getConn()) {
            runner.execute(connection, "DELETE FROM credit_request_entity");
            runner.execute(connection, "DELETE FROM order_entity");
            runner.execute(connection, "DELETE FROM payment_entity");
        } catch (SQLException exception) {
            exception.printStackTrace();
            //exception.getErrorCode();
        }
    }
    public static String getDebitCardStatus() {
        var statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var status = runner.query(conn, statusSQL, new ScalarHandler<String>());
            return new String(status);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String getCreditCardStatus() {
        var statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var status = runner.query(conn, statusSQL, new ScalarHandler<String>());
            return new String(status);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static int getDbRecordAmount() {
        var amountSQL = "SELECT amount FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var amount = runner.query(conn, amountSQL, new ScalarHandler<Integer>());
            return amount;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

}