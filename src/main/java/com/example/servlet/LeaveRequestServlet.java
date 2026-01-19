package com.example.servlet;

import com.example.dao.LeaveRequestDAO;
import com.example.model.LeaveRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/leaves")
public class LeaveRequestServlet extends HttpServlet {
    private LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    // Use GsonBuilder to handle Date format if necessary, or default
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empIdStr = req.getParameter("empId");
        String role = req.getParameter("role"); // Quick hack: In real app, check session/token

        resp.setContentType("application/json");

        try {
            if ("ADMIN".equals(role)) {
                // Admin fetches ALL requests
                List<LeaveRequest> requests = leaveRequestDAO.getAllRequests();
                resp.getWriter().write(gson.toJson(requests));
            } else if (empIdStr != null) {
                // Employee fetches OWN requests
                int empId = Integer.parseInt(empIdStr);
                List<LeaveRequest> requests = leaveRequestDAO.getRequestsByEmployee(empId);
                resp.getWriter().write(gson.toJson(requests));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing parameters\"}");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid ID\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Database error\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            // Expect JSON: { "reqId": 1, "status": "APPROVED" }
            StatusUpdate update = gson.fromJson(req.getReader(), StatusUpdate.class);

            boolean success = leaveRequestDAO.updateStatus(update.reqId, update.status);
            if (success) {
                resp.getWriter().write("{\"message\": \"Status updated\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Failed update\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            LeaveRequest leaveRequest = gson.fromJson(req.getReader(), LeaveRequest.class);
            // Default status to PENDING if not provided
            if (leaveRequest.getStatus() == null) {
                leaveRequest.setStatus("PENDING");
            }

            boolean success = leaveRequestDAO.addLeaveRequest(leaveRequest);
            if (success) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\": \"Leave applied successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Failed to apply leave\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid Request Data\"}");
            e.printStackTrace();
        }
    }

    // Helper for Status Update
    static class StatusUpdate {
        int reqId;
        String status;
    }
}
