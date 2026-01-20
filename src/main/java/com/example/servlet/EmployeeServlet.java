package com.example.servlet;

import com.example.dao.EmployeeDAO;
import com.example.model.Employee;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/employees/*")
public class EmployeeServlet extends HttpServlet {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            resp.getWriter().write(gson.toJson(employees));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Database error\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ("/login".equals(pathInfo)) {
            handleLogin(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read JSON body (Simplified for brevity, usually involves reading input
        // stream)
        // For now, let's assume form parameters for simplicity or read JSON stream
        // Implementing basic JSON reading:
        try {
            LoginRequest loginRequest = gson.fromJson(req.getReader(), LoginRequest.class);
            Employee emp = employeeDAO.authenticate(loginRequest.email, loginRequest.password);

            resp.setContentType("application/json");
            if (emp != null) {
                // Return employee details (excluding password hash in production ideally)
                emp.setPasswordHash(null);
                resp.getWriter().write(gson.toJson(emp));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\": \"Invalid Credentials\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Use 500 for server errors
            e.printStackTrace(); // Log to server logs
            // Return clean JSON error
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown Login Error";
            resp.getWriter().write(gson.toJson(new ErrorResponse(errorMessage)));
        }
    }

    // Helper class for JSON parsing
    static class LoginRequest {
        String email;
        String password;
    }

    static class ErrorResponse {
        String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
