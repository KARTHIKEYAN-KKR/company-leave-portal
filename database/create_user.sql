-- Run this in SSMS
USE EmployeeDB;
GO

-- 1. Create a login for the app
CREATE LOGIN app_user WITH PASSWORD = 'Password123!';
GO

-- 2. Create a user in the database for that login
CREATE USER app_user FOR LOGIN app_user;
GO

-- 3. Give permissions
ALTER ROLE db_owner ADD MEMBER app_user;
GO
