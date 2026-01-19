package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading PostgreSQL JDBC Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // 1. Try to get full URL from Environment (Render / Neon style)
        // Format: jdbc:postgresql://host:port/dbname?user=u&password=p
        String envUrl = System.getenv("DATABASE_URL");

        if (envUrl != null && !envUrl.isEmpty()) {
            envUrl = envUrl.trim(); // Fix: Trim accidental spaces or characters
            // Fix: Render/Neon can provide "postgres://" or "postgresql://"
            // We must handle "postgresql://" FIRST because it is longer.
            if (envUrl.startsWith("postgresql://")) {
                envUrl = envUrl.replace("postgresql://", "jdbc:postgresql://");
            } else if (envUrl.startsWith("postgres://")) {
                envUrl = envUrl.replace("postgres://", "jdbc:postgresql://");
            }
            return DriverManager.getConnection(envUrl);
        }

        // 2. Fallback: Manually constructed URL (Local testing)
        // Note: You must set these ENV VARS locally or hardcode for testing
        String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
        String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "5432";
        String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "EmployeeDB";
        String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
        String dbPass = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "password";

        String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
        return DriverManager.getConnection(url, dbUser, dbPass);
    }
}
