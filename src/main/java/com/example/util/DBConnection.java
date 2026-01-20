package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URI;

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
            try {
                // Parse the Render/Neon URI (postgres://user:pass@host:port/db)
                URI uri = new URI(envUrl.trim());
                
                // Extract parts
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String dbName = uri.getPath(); // includes leading /
                String info = uri.getUserInfo();
                String query = uri.getQuery();

                String user = "";
                String pass = "";

                if (info != null) {
                    String[] parts = info.split(":");
                    user = parts[0];
                    if (parts.length > 1) pass = parts[1];
                }

                // Construct Standard JDBC URL (jdbc:postgresql://host:port/db?user=u&password=p)
                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + dbName;
                
                // Append Query Params (sslmode, etc.) and Credentials
                String credentials = "user=" + user + "&password=" + pass;
                if (query != null) {
                    jdbcUrl += "?" + query + "&" + credentials;
                } else {
                    jdbcUrl += "?" + credentials;
                }

                return DriverManager.getConnection(jdbcUrl);

            } catch (Exception e) {
                e.printStackTrace();
                // If parsing fails, try raw or throw suitable error
                throw new SQLException("Failed to parse DATABASE_URL: " + e.getMessage());
            }
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
