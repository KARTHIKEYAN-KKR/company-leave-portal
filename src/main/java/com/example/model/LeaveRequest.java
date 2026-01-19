package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    private int reqId;
    private int empId;
    private String leaveType; // SICK, CASUAL, EARNED
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private Timestamp appliedOn;
}
