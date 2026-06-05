-- 为 'dbo.Employees' 表创建一个名为 'MyEmployees' 的同义词
CREATE SYNONYM MyEmployees FOR dbo.Employees;
-- 创建一个名为 TestSequence 的序列
-- 数据类型为 INT，起始值为 1，每次递增 1
CREATE SEQUENCE dbo.TestSequence
    AS INT
    START WITH 1
    INCREMENT BY 1;
-- 创建一个在 Employees 表上响应 INSERT 操作的触发器
CREATE TRIGGER trg_LogNewEmployee
ON Employees
AFTER INSERT
AS
BEGIN
    -- SET NOCOUNT ON 避免返回受影响的行数
    SET NOCOUNT ON;

    -- 将新插入的员工信息记录到审计表中
    -- 从 'inserted' 虚拟表中获取新员工的 EmployeeID
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
RETURNS NVARCHAR(101) -- 返回类型和长度，需要足够容纳姓、名和中间的空格
AS
BEGIN
    -- 声明一个变量来存储结果
    DECLARE @FullName NVARCHAR(101);

    -- 拼接姓和名，中间用一个空格隔开。注意姓氏在前。
    SET @FullName = @LastName + N' ' + @FirstName;

    -- 返回结果
    RETURN @FullName;
END;
-- 创建存储过程
CREATE PROCEDURE dbo.GetEmployeesByDepartment
    -- 输入参数
    @DeptName NVARCHAR(50)
AS
BEGIN
    -- SET NOCOUNT ON 用于防止返回受影响的行数，可以提高性能
    SET NOCOUNT ON;

    -- 查询逻辑：联接 Employees 和 Departments 表，并根据部门名称进行筛选
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

