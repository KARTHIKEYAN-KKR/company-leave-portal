package com.example.dao;

import com.example.model.Employee;
import com.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public Employee authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE email = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        }
        return employees;
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("emp_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getInt("dept_id"),
                rs.getDate("join_date"),
                rs.getString("role"));
    }
}
