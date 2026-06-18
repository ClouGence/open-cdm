-- Create a synonym for the 'dbo.employes' table called 'MyEmployes'
CREATE SYNONYM MyEmployees FOR dbo.Employees;
-- Create a sequence called TestSequence
-- Data type is INT, starting with 1 and increasing 1 each time
CREATE SEQUENCE dbo.TestSequence
    AS INT
    START WITH 1
    INCREMENT BY 1;
-- Create a trigger that responds to INSERT operations on the Employes table
CREATE TRIGGER trg_LogNewEmployee
ON Employees
AFTER INSERT
AS
BEGIN
    -- SET NOCOUNT ON Avoid returning affected rows
    SET NOCOUNT ON;

    -- Record newly inserted employee information in the audit form
    -- Get EmployeeID from the `inserved ' virtual table
    INSERT INTO EmployeeAudit (EmployeeID, ActionDescription)
    SELECT
        i.EmployeeID,
        'New employee added: ' + i.FirstName + ' ' + i.LastName
    FROM
        inserted AS i;
END;
CREATE FUNCTION dbo.udf_GetFullName
(
    @FirstName NVARCHAR(50),
    @LastName NVARCHAR(50)
)
RETURNS NVARCHAR(101) -- Type and length of return, which requires adequate accommodation of last name, name and middle spaces
AS
BEGIN
    -- Declare a variable to store the result
    DECLARE @FullName NVARCHAR(101);

    -- Spell names and names, separated by a space. Note the last name.
    SET @FullName = @LastName + N' ' + @FirstName;

    -- Return Result
    RETURN @FullName;
END;
-- Create stored procedure
CREATE PROCEDURE dbo.GetEmployeesByDepartment
    -- Input parameters
    @DeptName NVARCHAR(50)
AS
BEGIN
    -- SET NOCOUNT ON to prevent the return of affected rows and improve performance
    SET NOCOUNT ON;

    -- Query logic: connect Employes and Departments tables and screen according to department name Select
    SELECT
        e.EmployeeID,
        e.FirstName,
        e.LastName,
        d.DepartmentName
    FROM
        Employees AS e
    INNER JOIN
        Departments AS d ON e.DepartmentID = d.DepartmentID
    WHERE
        d.DepartmentName = @DeptName;
END;
