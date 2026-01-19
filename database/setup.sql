-- 1. Create Departments Table
CREATE TABLE IF NOT EXISTS Departments (
    dept_id SERIAL PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

-- 2. Create Employees Table
CREATE TABLE IF NOT EXISTS Employees (
    emp_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    dept_id INT,
    join_date DATE DEFAULT CURRENT_DATE,
    role VARCHAR(50) DEFAULT 'EMPLOYEE',
    FOREIGN KEY (dept_id) REFERENCES Departments(dept_id)
);

-- 3. Create LeaveRequests Table
CREATE TABLE IF NOT EXISTS LeaveRequests (
    req_id SERIAL PRIMARY KEY,
    emp_id INT NOT NULL,
    leave_type VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'PENDING',
    applied_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (emp_id) REFERENCES Employees(emp_id)
);

-- 4. Insert Dummy Data (Only if tables are empty)
INSERT INTO Departments (dept_name, location) 
SELECT 'HR', 'New York' WHERE NOT EXISTS (SELECT 1 FROM Departments WHERE dept_name = 'HR');
INSERT INTO Departments (dept_name, location) 
SELECT 'IT', 'San Francisco' WHERE NOT EXISTS (SELECT 1 FROM Departments WHERE dept_name = 'IT');
INSERT INTO Departments (dept_name, location) 
SELECT 'Finance', 'London' WHERE NOT EXISTS (SELECT 1 FROM Departments WHERE dept_name = 'Finance');

-- Note: In real setup, you might want to truncate or be careful with duplicates.
-- For simplicity, let's assume fresh DB or just ignore fails for now.
-- Better yet, simpler inserts for initial setup:

INSERT INTO Employees (full_name, email, password_hash, dept_id, role) VALUES 
('Admin User', 'admin@company.com', 'admin123', 1, 'ADMIN'),
('John Doe', 'john@company.com', 'secret', 2, 'EMPLOYEE'),
('Jane Smith', 'jane@company.com', 'secret', 2, 'EMPLOYEE');

SELECT * FROM Employees;
