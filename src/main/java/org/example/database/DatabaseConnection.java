package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getConnection() {

        try {

            Dotenv dotenv = Dotenv.load();

            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Database Connected!");

        } catch (Exception e) {

            e.printStackTrace();
        }

        return connection;
    }
}