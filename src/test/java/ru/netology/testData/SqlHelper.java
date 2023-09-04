package ru.netology.testData;

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

    @SneakyThrows
    public static void cleanTables() {
        String cleanCreditTables = "DELETE FROM credit_request_entity;";
        String cleanOrderTable = "DELETE FROM order_entity;";
        String cleanPaymentTable = "DELETE FROM payment_entity;";
        Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        Statement statement = connection.createStatement();
        statement.executeUpdate(cleanCreditTables);
        statement.executeUpdate(cleanOrderTable);
        statement.executeUpdate(cleanPaymentTable);
        connection.close();
    }
    @SneakyThrows
   public static String getCreditStatus(){
       var runner = new QueryRunner();
       var getStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
       try (var conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
           return runner.query(conn, getStatus, new ScalarHandler<>());
       }
   }
    @SneakyThrows
   public static String getPaymentStatus(){
       var runner = new QueryRunner();
       var getStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
       try (var conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
           return runner.query(conn, getStatus, new ScalarHandler<>());
       }
   }
}
