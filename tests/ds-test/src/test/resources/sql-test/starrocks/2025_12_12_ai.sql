SELECT
    id, 
    name, 
    age, 
    salary
FROM employees
WHERE age > 25
ORDER BY salary DESC;

SELECT 
    department, 
    COUNT(*) AS employee_count,
    AVG(salary) AS avg_salary,
    MAX(salary) AS max_salary,
    MIN(salary) AS min_salary
FROM employees
GROUP BY department;

SELECT 
    id,
    name,
    department,
    salary,
    ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) AS row_num,
    RANK() OVER (PARTITION BY department ORDER BY salary DESC) AS rank_salary,
    DENSE_RANK() OVER (PARTITION BY department ORDER BY salary DESC) AS dense_rank_salary,
    LAG(salary, 1) OVER (PARTITION BY department ORDER BY id) AS prev_salary,
    LEAD(salary, 1) OVER (PARTITION BY department ORDER BY id) AS next_salary
FROM employees;

SELECT 
    e.id,
    e.name,
    e.department,
    d.location,
    e.salary
FROM employees e
JOIN departments d ON e.department = d.name
WHERE e.salary > 5000;

SELECT 
    id,
    name,
    salary
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);

WITH dept_avg AS (
    SELECT 
        department,
        AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department
)
SELECT 
    e.id,
    e.name,
    e.department,
    e.salary,
    d.avg_salary
FROM employees e
JOIN dept_avg d ON e.department = d.department
WHERE e.salary > d.avg_salary;

SELECT 
    id,
    UPPER(name) AS upper_name,
    LOWER(name) AS lower_name,
    CONCAT(first_name, ' ', last_name) AS full_name,
    LENGTH(name) AS name_length,
    SUBSTRING(name, 1, 3) AS name_prefix
FROM employees;

SELECT 
    id,
    name,
    hire_date,
    YEAR(hire_date) AS hire_year,
    MONTH(hire_date) AS hire_month,
    DAY(hire_date) AS hire_day,
    DATEDIFF(CURDATE(), hire_date) AS days_employed
FROM employees;

SELECT 
    id,
    name,
    salary,
    CASE 
        WHEN salary >= 10000 THEN 'High'
        WHEN salary >= 5000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_level
FROM employees;

INSERT INTO employees (id, name, age, department, salary, hire_date)
VALUES (1001, 'John Doe', 30, 'Engineering', 8000, '2023-01-15');

UPDATE employees
SET salary = salary * 1.1
WHERE department = 'Engineering';

DELETE FROM employees
WHERE age > 65;

CREATE TABLE IF NOT EXISTS sales_detail (
    id INT,
    order_id BIGINT,
    customer_id INT,
    product_name VARCHAR(50),
    price DECIMAL(10, 2),
    quantity INT,
    order_date DATE
)
ENGINE=OLAP
DUPLICATE KEY(id, order_id)
DISTRIBUTED BY HASH(order_id) BUCKETS 10
PROPERTIES("replication_num" = "1");

CREATE TABLE IF NOT EXISTS sales_summary (
    order_date DATE,
    product_name VARCHAR(50),
    total_price DECIMAL(15, 2) SUM,
    total_quantity INT SUM,
    order_count BIGINT SUM
)
ENGINE=OLAP
AGGREGATE KEY(order_date, product_name)
DISTRIBUTED BY HASH(order_date) BUCKETS 10
PROPERTIES("replication_num" = "1");

CREATE TABLE IF NOT EXISTS user_profiles (
    user_id BIGINT,
    username VARCHAR(50),
    email VARCHAR(100),
    created_at DATETIME,
    updated_at DATETIME
)
ENGINE=OLAP
UNIQUE KEY(user_id)
DISTRIBUTED BY HASH(user_id) BUCKETS 10
PROPERTIES("replication_num" = "1");

CREATE MATERIALIZED VIEW mv_employee_dept_salary AS
SELECT 
    department,
    COUNT(*) AS employee_count,
    AVG(salary) AS avg_salary
FROM employees
GROUP BY department;

CREATE EXTERNAL TABLE hive_external_table (
    id INT,
    name VARCHAR(50),
    age INT,
    salary DOUBLE
)
ENGINE=HIVE
PROPERTIES (
    "hive.metastore.uris" = "thrift://127.0.0.1:9083",
    "database" = "test_db",
    "table" = "test_table"
);

CREATE EXTERNAL TABLE mysql_external_table (
    id INT,
    name VARCHAR(50),
    age INT,
    salary DOUBLE
)
ENGINE=MYSQL
PROPERTIES (
    "host" = "127.0.0.1",
    "port" = "3306",
    "user" = "root",
    "password" = "password",
    "database" = "test_db",
    "table" = "test_table"
);

CREATE TABLE partitioned_sales (
    id INT,
    product_name VARCHAR(50),
    price DECIMAL(10, 2),
    sale_date DATE
)
ENGINE=OLAP
DUPLICATE KEY(id)
PARTITION BY RANGE(sale_date) (
    PARTITION p2023 VALUES LESS THAN ("2024-01-01"),
    PARTITION p2024 VALUES LESS THAN ("2025-01-01"),
    PARTITION p2025 VALUES LESS THAN ("2026-01-01")
)
DISTRIBUTED BY HASH(id) BUCKETS 10
PROPERTIES("replication_num" = "1");

SELECT 
    department,
    COUNT(DISTINCT user_id) AS user_count,
    BITMAP_UNION(to_bitmap(user_id)) AS user_bitmap
FROM user_activities
GROUP BY department;

SELECT 
    id,
    name,
    tags,
    ARRAY_LENGTH(tags) AS tag_count,
    ARRAY_CONTAINS(tags, 'VIP') AS is_vip
FROM customers;

SELECT 
    id,
    name,
    profile,
    JSON_EXTRACT(profile, '$.age') AS age,
    JSON_EXTRACT(profile, '$.address.city') AS city
FROM users_with_profile;

SELECT * FROM information_schema.tables WHERE table_schema = 'test_db';
SELECT * FROM information_schema.columns WHERE table_name = 'employees';

SHOW DATABASES;
SHOW TABLES;
SHOW CREATE TABLE employees;
DESCRIBE employees;

ANALYZE TABLE employees;

SELECT 
    department,
    avg_salary,
    employee_count
FROM (
    SELECT 
        department,
        AVG(salary) AS avg_salary,
        COUNT(*) AS employee_count
    FROM employees
    WHERE hire_date >= '2020-01-01'
    GROUP BY department
) dept_stats
WHERE avg_salary > 6000
ORDER BY avg_salary DESC;

SELECT 
    department,
    job_title,
    COUNT(*) AS employee_count,
    AVG(salary) AS avg_salary
FROM employees
GROUP BY GROUPING SETS ((department), (job_title), (department, job_title), ());

SELECT 
    department,
    AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > 7000;

SELECT 
    id,
    name,
    salary
FROM employees
ORDER BY salary DESC
LIMIT 10 OFFSET 20;

SELECT
    id,
    name,
    hire_date,
    YEAR(hire_date) AS hire_year,
    MONTH(hire_date) AS hire_month,
    DAY(hire_date) AS hire_day
FROM employees;

SELECT
    start_time,
    TIMESTAMPADD(DAY, 7, start_time) AS one_week_later,
    TIMESTAMPDIFF(HOUR, start_time, end_time) AS hours_diff
FROM events;

SELECT
    id,
    name,
    CHAR(65) AS char_a,
    LEFT(name, 3) AS left_three_chars,
    RIGHT(name, 3) AS right_three_chars,
    REPLACE(name, 'John', 'Jane') AS replaced_name
FROM users;

SELECT
    id,
    description,
    LENGTH(description) AS desc_length,
    SUBSTRING(description, 1, 10) AS short_desc
FROM products;

SELECT
    id,
    price,
    FLOOR(price) AS floor_price,
    CEIL(price) AS ceil_price
FROM products;

SELECT
    id,
    quantity,
    MOD(quantity, 10) AS remainder
FROM inventory;

SELECT
    id,
    score,
    IF(score >= 60, 'PASS', 'FAIL') AS result
FROM exam_scores;

SELECT
    id,
    salary,
    IF(salary > 10000, 'HIGH', IF(salary > 5000, 'MEDIUM', 'LOW')) AS salary_level
FROM employees;

SELECT
    id,
    email,
    LIKE(email, '%@gmail.com') AS is_gmail,
    REGEXP(email, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$') AS valid_email,
    RLIKE(name, '^J.*') AS starts_with_j
FROM users;

SELECT
    username,
    PASSWORD('my_password') AS encrypted_password
FROM user_accounts;

SELECT
    CURRENT_DATE AS current_date_no_paren,
    CURRENT_DATE() AS current_date_with_paren;

SELECT
    CURRENT_TIME AS current_time_no_paren,
    CURRENT_TIME() AS current_time_with_paren;

SELECT
    CURRENT_TIMESTAMP AS current_timestamp_no_paren,
    CURRENT_TIMESTAMP() AS current_timestamp_with_paren,
    CURRENT_TIMESTAMP(3) AS current_timestamp_with_precision;

SELECT
    LOCALTIME AS localtime_no_paren,
    LOCALTIME() AS localtime_with_paren;

SELECT
    LOCALTIMESTAMP AS localtimestamp_no_paren,
    LOCALTIMESTAMP() AS localtimestamp_with_paren;

SELECT
    id,
    CONCAT(first_name, ' ', last_name) AS full_name,
    UPPER(LEFT(last_name, 1)) AS last_name_initial,
    TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) AS age,
    FLOOR(salary/12) AS monthly_salary,
    IF(department = 'IT', 'Technical', 'Non-Technical') AS category,
    CURRENT_DATE AS today
FROM employees
WHERE
    LIKE(email, '%@company.com')
    AND MOD(id, 2) = 0;

SELECT 
    CATALOG() AS current_catalog,
    DATABASE() AS current_database,
    SCHEMA() AS current_schema;

SELECT 
    USER() AS current_user,
    CURRENT_USER AS current_user_no_paren,
    CURRENT_USER() AS current_user_with_paren;

SELECT 
    CURRENT_ROLE AS current_role_no_paren,
    CURRENT_ROLE() AS current_role_with_paren;

SELECT 
    CURRENT_GROUP AS current_group_no_paren,
    CURRENT_GROUP() AS current_group_with_paren;

SELECT TRANSLATE('hello', 'el', 'ip');



CREATE TABLE IF NOT EXISTS sales_record (
    id INT,
    employee_id INT,
    employee_name VARCHAR(50),
    department VARCHAR(50),
    sale_amount DECIMAL(10, 2),
    sale_date DATE
)
ENGINE=OLAP
DUPLICATE KEY(id)
DISTRIBUTED BY HASH(id) BUCKETS 10
PROPERTIES("replication_num" = "1");

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    ROW_NUMBER() OVER (PARTITION BY department ORDER BY sale_amount DESC) AS row_num
FROM sales_record;
SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    RANK() OVER (PARTITION BY department ORDER BY sale_amount DESC) AS rank_num
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    DENSE_RANK() OVER (PARTITION BY department ORDER BY sale_amount DESC) AS dense_rank_num
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    CUME_DIST() OVER (PARTITION BY department ORDER BY sale_amount) AS cumulative_distribution
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    PERCENT_RANK() OVER (PARTITION BY department ORDER BY sale_amount) AS percent_rank
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    NTILE(4) OVER (PARTITION BY department ORDER BY sale_amount) AS quartile
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LAG(sale_amount, 1) OVER (PARTITION BY department ORDER BY sale_date) AS prev_sale_amount
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LAG(sale_amount, 1, 0) OVER (PARTITION BY department ORDER BY sale_date) AS prev_sale_amount_with_default
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LEAD(sale_amount, 1) OVER (PARTITION BY department ORDER BY sale_date) AS next_sale_amount
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LEAD(sale_amount, 1, 0) OVER (PARTITION BY department ORDER BY sale_date) AS next_sale_amount_with_default
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    FIRST_VALUE(sale_amount) OVER (PARTITION BY department ORDER BY sale_date) AS first_sale_amount
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LAST_VALUE(sale_amount) OVER (PARTITION BY department ORDER BY sale_date ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS last_sale_amount
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LAG(sale_amount) IGNORE NULLS OVER (PARTITION BY department ORDER BY sale_date) AS lag_ignore_nulls
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LEAD(sale_amount) IGNORE NULLS OVER (PARTITION BY department ORDER BY sale_date) AS lead_ignore_nulls
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    FIRST_VALUE(sale_amount) IGNORE NULLS OVER (PARTITION BY department ORDER BY sale_date) AS first_value_ignore_nulls
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    LAST_VALUE(sale_amount) IGNORE NULLS OVER (PARTITION BY department ORDER BY sale_date) AS last_value_ignore_nulls
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    SUM(sale_amount) OVER (PARTITION BY department ORDER BY sale_date RANGE BETWEEN INTERVAL 7 DAY PRECEDING AND CURRENT ROW) AS weekly_sum
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    AVG(sale_amount) OVER (PARTITION BY department ORDER BY sale_date ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) AS moving_avg
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    sale_date,
    ROW_NUMBER() OVER (PARTITION BY department ORDER BY sale_amount DESC) AS row_num,
    RANK() OVER (PARTITION BY department ORDER BY sale_amount DESC) AS rank_num,
    DENSE_RANK() OVER (PARTITION BY department ORDER BY sale_amount DESC) AS dense_rank_num,
    LAG(sale_amount, 1) OVER (PARTITION BY department ORDER BY sale_date) AS prev_sale,
    LEAD(sale_amount, 1) OVER (PARTITION BY department ORDER BY sale_date) AS next_sale,
    FIRST_VALUE(sale_amount) OVER (PARTITION BY department ORDER BY sale_date) AS first_sale,
    LAST_VALUE(sale_amount) OVER (PARTITION BY department ORDER BY sale_date ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS last_sale
FROM sales_record;

SELECT
    employee_id,
    employee_name,
    department,
    sale_amount,
    sale_date,
    ROW_NUMBER() OVER (ORDER BY sale_amount DESC) AS overall_row_num,
    ROW_NUMBER() OVER (PARTITION BY employee_id ORDER BY sale_date) AS emp_sale_sequence,
    NTILE(5) OVER (ORDER BY sale_amount) AS quintile
FROM sales_record;

CREATE TABLE IF NOT EXISTS sales_data (
    region VARCHAR(50),
    product VARCHAR(50),
    salesperson VARCHAR(50),
    sale_amount DECIMAL(10, 2),
    sale_date DATE
)
ENGINE=OLAP
DUPLICATE KEY(region, product, salesperson)
DISTRIBUTED BY HASH(region) BUCKETS 10
PROPERTIES("replication_num" = "1");


CREATE TABLE IF NOT EXISTS user_tags (
    user_id BIGINT,
    user_name VARCHAR(50),
    tags ARRAY<VARCHAR(20)>,
    scores ARRAY<INT>
)
ENGINE=OLAP
DUPLICATE KEY(user_id)
DISTRIBUTED BY HASH(user_id) BUCKETS 10
PROPERTIES("replication_num" = "1");


CREATE TABLE IF NOT EXISTS user_properties (
    user_id BIGINT,
    user_name VARCHAR(50),
    properties MAP<VARCHAR(50), VARCHAR(100)>
)
ENGINE=OLAP
DUPLICATE KEY(user_id)
DISTRIBUTED BY HASH(user_id) BUCKETS 10
PROPERTIES("replication_num" = "1");

SELECT
    user_id,
    user_name,
    properties,
    MAP {'city': 'Beijing', 'country': 'China'} AS default_props,
    properties['city'] AS city,
    properties['country'] AS country
FROM user_properties;

CREATE TABLE IF NOT EXISTS user_addresses (
    user_id BIGINT,
    user_name VARCHAR(50),
    address STRUCT<street VARCHAR(100), city VARCHAR(50), zipcode VARCHAR(10)>
)
ENGINE=OLAP
DUPLICATE KEY(user_id)
DISTRIBUTED BY HASH(user_id) BUCKETS 10
PROPERTIES("replication_num" = "1");

SELECT
    user_id,
    user_name,
    address,
    address.street AS street,
    address.city AS city,
    address.zipcode AS zipcode
FROM user_addresses;

SELECT
    user_id,
    user_name,
    (SELECT COUNT(*) FROM sales_data WHERE sales_data.salesperson = user_tags.user_name) AS total_sales_count
FROM user_tags;

SELECT *
FROM sales_data
WHERE (region, product) IN (SELECT region, product FROM sales_data WHERE sale_amount > 1000);

SELECT
    user_id,
    user_name
FROM user_tags u
WHERE EXISTS (
    SELECT 1
    FROM sales_data s
    WHERE s.salesperson = u.user_name AND s.sale_amount > 5000
);

SELECT region, product FROM sales_data
UNION
SELECT region, product FROM sales_data WHERE sale_amount > 1000;

SELECT region, product FROM sales_data
INTERSECT
SELECT region, product FROM sales_data WHERE sale_amount > 1000;

SELECT region, product FROM sales_data
EXCEPT
SELECT region, product FROM sales_data WHERE sale_amount > 1000;

SELECT
    region,
    product,
    sale_amount,
    ROW_NUMBER() OVER (PARTITION BY region ORDER BY sale_amount DESC) AS row_num,
    SUM(sale_amount) OVER (PARTITION BY region ORDER BY sale_amount ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS running_total,
    AVG(sale_amount) OVER (PARTITION BY region) AS avg_per_region
FROM sales_data;

CREATE TABLE IF NOT EXISTS quarterly_sales (
    year INT,
    quarter VARCHAR(10),
    product VARCHAR(50),
    sales DECIMAL(10, 2)
)
ENGINE=OLAP
DUPLICATE KEY(year, quarter, product)
DISTRIBUTED BY HASH(year) BUCKETS 10
PROPERTIES("replication_num" = "1");

SELECT
    user_id,
    user_name,
    (SELECT AVG(total_sales) FROM (
        SELECT salesperson, SUM(sale_amount) AS total_sales
        FROM sales_data
        GROUP BY salesperson
    ) t WHERE t.salesperson = user_tags.user_name) AS avg_sales
FROM user_tags;

SELECT
    region,
    product,
    sale_amount,
    CASE
        WHEN sale_amount > 10000 THEN 'Excellent'
        WHEN sale_amount > 5000 THEN
            CASE
                WHEN region = 'North' THEN 'Good North'
                WHEN region = 'South' THEN 'Good South'
                ELSE 'Good Other'
            END
        ELSE 'Average'
    END AS performance_category
FROM sales_data;

SELECT
    s.region,
    s.product,
    u.user_name,
    s.sale_amount
FROM sales_data s
LEFT JOIN user_tags u ON s.salesperson = u.user_name
RIGHT JOIN user_properties p ON u.user_id = p.user_id
FULL OUTER JOIN user_addresses a ON p.user_id = a.user_id;


SET @var1 = 'test_value';

SHOW VARIABLES LIKE 'sql_mode';
SHOW STATUS LIKE 'Threads_connected';


CREATE TABLE IF NOT EXISTS time_series_data (
    event_time DATETIME,
    event_type VARCHAR(50),
    event_value DECIMAL(10, 2)
)
ENGINE=OLAP
DUPLICATE KEY(event_time)
PARTITION BY RANGE(event_time) (
    PARTITION p2023 VALUES LESS THAN ("2024-01-01 00:00:00"),
    PARTITION p2024_q1 VALUES LESS THAN ("2024-04-01 00:00:00"),
    PARTITION p2024_q2 VALUES LESS THAN ("2024-07-01 00:00:00")
)
DISTRIBUTED BY HASH(event_time) BUCKETS 10
PROPERTIES("replication_num" = "1");

REFRESH MATERIALIZED VIEW mv_employee_dept_salary;

CREATE INDEX idx_sales_region ON sales_data (region);
DROP INDEX idx_sales_region ON sales_data;

ALTER TABLE sales_data DROP COLUMN discount;