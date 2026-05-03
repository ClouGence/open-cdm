-- 测试控制流函数
SELECT 
    IF(1 > 2, 'true', 'false') as if_result,
    IFNULL(NULL, 'default_value') as ifnull_result,
    NULLIF('value1', 'value2') as nullif_result,
    CASE 
        WHEN 1 = 1 THEN 'one equals one'
        WHEN 2 = 2 THEN 'two equals two'
        ELSE 'no match'
    END as case_result;

-- 测试加密和压缩函数
SELECT 
    MD5('hello world') as md5_result,
    SHA1('hello world') as sha1_result,
    SHA2('hello world', 256) as sha2_result;

-- 测试信息函数
SELECT 
    CONNECTION_ID() as connection_id,
    CURRENT_USER() as current_user1,
    DATABASE() as current_database,
    LAST_INSERT_ID() as last_insert_id,
    ROW_COUNT() as affected_rows,
    VERSION() as mysql_version;

-- 测试系统变量
SELECT @@autocommit as autocommit_status;
SELECT @@max_connections as max_connections;
SELECT @@innodb_version as innodb_version;

-- 测试转换函数
SELECT 
    CAST('123' AS SIGNED) as cast_signed,
    CONVERT('123', SIGNED) as convert_signed,
    CAST('2025-12-15' AS DATE) as cast_date;

-- 测试时间和日期函数补充
SELECT 
    ADDDATE('2025-12-15', INTERVAL 10 DAY) as adddate_result,
    SUBDATE('2025-12-15', INTERVAL 10 DAY) as subdate_result,
    ADDTIME('12:00:00', '01:30:00') as addtime_result,
    TIMEDIFF('12:00:00', '10:30:00') as timediff_result,
    TIMESTAMPDIFF(MONTH, '2024-12-15', '2025-12-15') as timestampdiff_result,
    PERIOD_ADD(202512, 3) as period_add_result,
    PERIOD_DIFF(202603, 202512) as period_diff_result;

-- 测试格式化函数
SELECT 
    FORMAT(12345.6789, 2) as format_result,
    QUOTE('Don\'t!') as quote_result,
    SLEEP(0.01) as sleep_result;

-- 测试空间函数
SELECT 
    POINT(1, 1) as point_result,
    LINESTRING(POINT(0, 0), POINT(1, 1)) as linestring_result;

-- 测试XML函数
SELECT 
    ExtractValue('<person><name>John</name><age>30</age></person>', '/person/name') as extract_result,
    UpdateXML('<person><name>John</name><age>30</age></person>', '/person/age', '<age>35</age>') as update_result;

-- 测试聚合函数补充
SELECT 
    GROUP_CONCAT('A', 'B', 'C' SEPARATOR '-') as group_concat_sep,
    COUNT(DISTINCT 1, 2, 3) as count_distinct,
    APPROX_COUNT_DISTINCT(1, 2, 3) as approx_count_distinct;

-- 测试分区表相关操作
SELECT 
    PARTITION_NAME,
    TABLE_ROWS
FROM INFORMATION_SCHEMA.PARTITIONS 
WHERE TABLE_NAME = 'orders_partitioned' 
  AND PARTITION_NAME IS NOT NULL;

-- 测试子查询中的EXISTS和NOT EXISTS
SELECT * FROM departments d 
WHERE EXISTS (
    SELECT 1 FROM employees e 
    WHERE e.department_id = d.id 
      AND e.salary > 10000
);

SELECT * FROM departments d 
WHERE NOT EXISTS (
    SELECT 1 FROM employees e 
    WHERE e.department_id = d.id
);

-- 测试INSERT IGNORE和ON DUPLICATE KEY UPDATE的不同行为
INSERT IGNORE INTO users (id, name, email) VALUES (1, 'Test User', 'test@example.com');

INSERT INTO users (id, name, email) VALUES (1, 'Updated User', 'updated@example.com') 
ON DUPLICATE KEY UPDATE name = VALUES(name), email = VALUES(email);

-- 测试事务控制语句
START TRANSACTION;
INSERT INTO departments (name) VALUES ('Test Department');
SAVEPOINT sp1;
INSERT INTO employees (name, department_id, salary) VALUES ('Test Employee', LAST_INSERT_ID(), 5000);
ROLLBACK TO sp1;
COMMIT;

-- 测试预处理语句
PREPARE stmt FROM 'SELECT * FROM users WHERE id = ?';
SET @user_id = 1;
EXECUTE stmt USING @user_id;
DEALLOCATE PREPARE stmt;

-- 测试游标（通常在存储过程中使用）
-- 这里仅展示语法结构

CREATE PROCEDURE cursor_example()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE user_name VARCHAR(255);
  DECLARE cur CURSOR FOR SELECT name FROM users;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  
  OPEN cur;
  
  read_loop: LOOP
    FETCH cur INTO user_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    -- Process user_name
  END LOOP;
  
  CLOSE cur;
END;

-- 测试复合语句块
SELECT 
    (SELECT COUNT(*) FROM users) as user_count,
    (SELECT AVG(salary) FROM employees) as avg_salary;

-- 测试全文搜索函数
SELECT 
    MATCH(title, content) AGAINST('MySQL' IN BOOLEAN MODE) as boolean_search,
    MATCH(title, content) AGAINST('MySQL' WITH QUERY EXPANSION) as expanded_search
FROM articles 
WHERE MATCH(title, content) AGAINST('MySQL' IN NATURAL LANGUAGE MODE);

-- 测试用户定义变量
SET @var1 = 10;
SET @var2 = 20;
SELECT @var1 + @var2 as sum_result, @var1 * @var2 as product_result;
