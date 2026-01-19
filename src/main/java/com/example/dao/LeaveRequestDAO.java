package com.example.dao;

import com.example.model.LeaveRequest;
import com.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {

    public boolean addLeaveRequest(LeaveRequest request) throws SQLException {
        String sql = "INSERT INTO LeaveRequests (emp_id, leave_type, start_date, end_date, reason) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getEmpId());
            stmt.setString(2, request.getLeaveType());
            stmt.setDate(3, request.getStartDate());
            stmt.setDate(4, request.getEndDate());
            stmt.setString(5, request.getReason());
            return stmt.executeUpdate() > 0;
        }
    }

    public List<LeaveRequest> getRequestsByEmployee(int empId) throws SQLException {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequests WHERE emp_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToLeaveRequest(rs));
            }
        }
        return list;
    }

    public List<LeaveRequest> getAllRequests() throws SQLException {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM LeaveRequests";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToLeaveRequest(rs));
            }
        }
        return list;
    }

    public boolean updateStatus(int reqId, String status) throws SQLException {
        String sql = "UPDATE LeaveRequests SET status = ? WHERE req_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, reqId);
            return stmt.executeUpdate() > 0;
        }
    }

    private LeaveRequest mapResultSetToLeaveRequest(ResultSet rs) throws SQLException {
        return new LeaveRequest(
                rs.getInt("req_id"),
                rs.getInt("emp_id"),
                rs.getString("leave_type"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getString("reason"),
                rs.getString("status"),
                rs.getTimestamp("applied_on"));
    }
}
