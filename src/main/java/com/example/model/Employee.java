package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int empId;
    private String fullName;
    private String email;
    private String passwordHash;
    private int deptId;
    private Date joinDate;
    private String role; // 'ADMIN' or 'EMPLOYEE'
}
