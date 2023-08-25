package ru.netology.testData;

import lombok.Getter;

import java.sql.*;

public class SqlHelper {
    private static String dbUrl = System.getProperty("db.url"); // запуск консоль

//    private static final String dbUrl = "jdbc:postgresql://localhost:5432/app"; //запуск Idea postgres
//    private static final String dbUrl = "jdbc:mysql://localhost:3306/app"; //запуск Idea mySql
    private static final String dbUser = "app";
    private static final String dbPass = "pass";

    @Getter
    private static final String payTable = "payment_entity";
    @Getter
    private static final String creditTable = "credit_request_entity";

    public static void cleanTables() throws SQLException {
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

    public static String getOperationStatus(String table) {
        String status = "";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " ORDER BY id DESC LIMIT 1;")) {
                while (resultSet.next()) status = resultSet.getString("status");
            }
        } catch (SQLException error) {
            error.getErrorCode();
        }
        return status;
    }
}
