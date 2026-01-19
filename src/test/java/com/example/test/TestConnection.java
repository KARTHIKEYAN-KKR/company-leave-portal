package com.example.test;

import com.example.util.DBConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing Database Connection...");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("SUCCESS: Connected to EmployeeDB!");
            } else {
                System.out.println("FAILED: Connection object is null.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Could not connect to database.");
            e.printStackTrace();
            System.out.println("\nNOTE: If you get an error about 'integratedSecurity', ensure 'mssql-jdbc_auth.dll' is in your path.");
        }
    }
}
