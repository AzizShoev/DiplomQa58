package ru.netology.testdata;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SqlHelper {
    private static String dbUrl = System.getProperty("db.url"); // запуск консоль
    //    private static final String dbUrl = "jdbc:postgresql://localhost:5432/app"; //запуск Idea
    //    private static final String dbUrl = "jdbc:mysql://localhost:3306/app"; //запуск Idea
    private static final String dbUser = "app";
    private static final String dbPass = "pass";

    private static Connection conn;
    private static QueryRunner runner = new QueryRunner();

    @SneakyThrows
    public static void initConnection() {
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    @SneakyThrows
    public static void cleanTables() {
        initConnection();
        var cleanCreditTables = "DELETE FROM credit_request_entity;";
        var cleanOrderTable = "DELETE FROM order_entity;";
        var cleanPaymentTable = "DELETE FROM payment_entity;";
        runner.update(conn, cleanCreditTables);
        runner.update(conn, cleanOrderTable);
        runner.update(conn, cleanPaymentTable);
        conn.close();
    }

    @SneakyThrows
    public static String getCreditStatus() {
        initConnection();
        var getStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return runner.query(conn, getStatus, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        initConnection();
        var getStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return runner.query(conn, getStatus, new ScalarHandler<>());
    }
}
