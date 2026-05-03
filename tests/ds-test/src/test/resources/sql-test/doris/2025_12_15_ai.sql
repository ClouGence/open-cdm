SELECT
    employee_id,
    first_name,
    last_name,
    salary,
    department_id,
    ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) as rn,
    RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) as rank_sal,
    DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) as dense_rank_sal,
    LAG(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY employee_id) as prev_salary,
    LEAD(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY employee_id) as next_salary,
    FIRST_VALUE(salary) OVER (PARTITION BY department_id ORDER BY salary DESC) as highest_salary,
    LAST_VALUE(salary) OVER (PARTITION BY department_id ORDER BY salary DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as lowest_salary
FROM employees
WHERE hire_date >= '2020-01-01';

WITH dept_costs AS (
    SELECT department_id, SUM(salary) as dept_total
    FROM employees
    GROUP BY department_id),
avg_cost AS (
    SELECT SUM(dept_total)/COUNT(*) as avg
    FROM dept_costs)
SELECT dc.department_id, dc.dept_total, ac.avg
FROM dept_costs dc, avg_cost ac
WHERE dc.dept_total > ac.avg;

SELECT
    employee_id,
    first_name,
    last_name,
    salary,
    department_id,
    CASE
        WHEN salary >= (SELECT AVG(salary) FROM employees) THEN
            CASE
                WHEN department_id IN (SELECT department_id FROM departments WHERE department_name LIKE '%Sales%') THEN 'High Earner in Sales'
                WHEN department_id IN (SELECT department_id FROM departments WHERE department_name LIKE '%IT%') THEN 'High Earner in IT'
                ELSE 'High Earner Other Dept'
            END
        WHEN salary >= 0.8 * (SELECT AVG(salary) FROM employees) THEN 'Above Average Earner'
        ELSE 'Below Average Earner'
    END AS salary_category,
    CASE department_id
        WHEN 10 THEN 'Administration'
        WHEN 20 THEN 'Marketing'
        WHEN 30 THEN 'Purchasing'
        WHEN 40 THEN 'Human Resources'
        WHEN 50 THEN 'Shipping'
        WHEN 60 THEN 'IT'
        WHEN 70 THEN 'Public Relations'
        WHEN 80 THEN 'Sales'
        WHEN 90 THEN 'Executive'
        WHEN 100 THEN 'Finance'
        WHEN 110 THEN 'Accounting'
        ELSE 'Other Department'
    END AS dept_name
FROM employees
WHERE commission_pct IS NOT NULL
ORDER BY department_id, salary DESC;

SELECT
    department_id,
    employee_id,
    first_name,
    last_name,
    salary,
    CUME_DIST() OVER (PARTITION BY department_id ORDER BY salary) AS cumulative_dist,
    PERCENT_RANK() OVER (PARTITION BY department_id ORDER BY salary) AS percent_rank,
    NTILE(4) OVER (PARTITION BY department_id ORDER BY salary) AS quartile
FROM employees
WHERE department_id IN (30, 60, 80)
ORDER BY department_id, salary;

SELECT
    employee_id,
    first_name,
    last_name,
    hire_date,
    NOW() AS current_date,
    ROUND(TIMESTAMPDIFF(MONTH, hire_date, NOW())) AS months_employed,
    DATE_ADD(hire_date, INTERVAL 6 MONTH) AS six_month_review,
    hire_date + INTERVAL (7 - WEEKDAY(hire_date)) DAY AS first_monday_after_hire,
    LAST_DAY(hire_date) AS last_day_of_hire_month,
    YEAR(hire_date) AS hire_year,
    MONTH(hire_date) AS hire_month,
    DAY(hire_date) AS hire_day
FROM employees
WHERE hire_date >= DATE_SUB(NOW(), INTERVAL 5 YEAR)
ORDER BY hire_date;

CREATE TABLE employees_with_bonus (
    employee_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    salary DECIMAL(8,2),
    commission_pct DECIMAL(2,2),
    bonus DECIMAL(10,2) AS (
        CASE
            WHEN commission_pct IS NOT NULL THEN salary * commission_pct * 12
            ELSE salary * 0.1
        END
    ) COMMENT "Generated bonus column",
    full_name VARCHAR(101) AS (CONCAT(first_name, ' ', last_name)) COMMENT "Generated full name"
)
ENGINE=OLAP
DUPLICATE KEY(employee_id)
DISTRIBUTED BY HASH(employee_id) BUCKETS 10
PROPERTIES("replication_num" = "1");

CREATE TABLE sales (
    salesman_id INT,
    product_id INT,
    sale_date DATE,
    amount DECIMAL(10, 2)
)
ENGINE=OLAP
DUPLICATE KEY(salesman_id, product_id, sale_date)
PARTITION BY RANGE(sale_date) (
    PARTITION sales_q1 VALUES LESS THAN ("2025-04-01"),
    PARTITION sales_q2 VALUES LESS THAN ("2025-07-01"),
    PARTITION sales_q3 VALUES LESS THAN ("2025-10-01"),
    PARTITION sales_q4 VALUES LESS THAN ("2026-01-01")
)
DISTRIBUTED BY HASH(salesman_id) BUCKETS 10
PROPERTIES("replication_num" = "1");


CREATE MATERIALIZED VIEW emp_salary_mv AS
SELECT 
    department_id,
    AVG(salary) AS avg_salary,
    COUNT(*) AS emp_count
FROM employees
GROUP BY department_id;

SELECT employee_id, first_name, last_name, salary
FROM employees
WHERE department_id = 50;

CREATE TABLE sales_partitioned (
    salesman_id INT,
    product_id INT,
    sale_date DATE,
    amount DECIMAL(10, 2)
)
ENGINE=OLAP
DUPLICATE KEY(salesman_id, product_id, sale_date)
PARTITION BY RANGE(sale_date) (
    PARTITION p2023 VALUES LESS THAN ("2024-01-01"),
    PARTITION p2024 VALUES LESS THAN ("2025-01-01"),
    PARTITION p2025 VALUES LESS THAN ("2026-01-01")
)
DISTRIBUTED BY HASH(salesman_id) BUCKETS 10
PROPERTIES("replication_num" = "1");

SELECT
    employee_id,
    first_name,
    last_name,
    salary,
    department_id,
    ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) as rn,
    RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) as rank_sal,
    DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) as dense_rank_sal,
    LAG(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY employee_id) as prev_salary,
    LEAD(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY employee_id) as next_salary,
    FIRST_VALUE(salary) OVER (PARTITION BY department_id ORDER BY salary DESC) as highest_salary,
    LAST_VALUE(salary) OVER (PARTITION BY department_id ORDER BY salary DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as lowest_salary
FROM employees
WHERE hire_date >= '2020-01-01';

WITH dept_costs AS (
    SELECT department_id, SUM(salary) as dept_total
    FROM employees
    GROUP BY department_id),
avg_cost AS (
    SELECT SUM(dept_total)/COUNT(*) as avg
    FROM dept_costs)
SELECT dc.department_id, dc.dept_total, ac.avg
FROM dept_costs dc, avg_cost ac
WHERE dc.dept_total > ac.avg;

SELECT
    employee_id,
    first_name,
    last_name,
    salary,
    department_id,
    CASE
        WHEN salary >= (SELECT AVG(salary) FROM employees) THEN
            CASE
                WHEN department_id IN (SELECT department_id FROM departments WHERE department_name LIKE '%Sales%') THEN 'High Earner in Sales'
                WHEN department_id IN (SELECT department_id FROM departments WHERE department_name LIKE '%IT%') THEN 'High Earner in IT'
                ELSE 'High Earner Other Dept'
            END
        WHEN salary >= 0.8 * (SELECT AVG(salary) FROM employees) THEN 'Above Average Earner'
        ELSE 'Below Average Earner'
    END AS salary_category,
    CASE department_id
        WHEN 10 THEN 'Administration'
        WHEN 20 THEN 'Marketing'
        WHEN 30 THEN 'Purchasing'
        WHEN 40 THEN 'Human Resources'
        WHEN 50 THEN 'Shipping'
        WHEN 60 THEN 'IT'
        WHEN 70 THEN 'Public Relations'
        WHEN 80 THEN 'Sales'
        WHEN 90 THEN 'Executive'
        WHEN 100 THEN 'Finance'
        WHEN 110 THEN 'Accounting'
        ELSE 'Other Department'
    END AS dept_name
FROM employees
WHERE commission_pct IS NOT NULL
ORDER BY department_id, salary DESC;

SELECT
    department_id,
    employee_id,
    first_name,
    last_name,
    salary,
    CUME_DIST() OVER (PARTITION BY department_id ORDER BY salary) AS cumulative_dist,
    PERCENT_RANK() OVER (PARTITION BY department_id ORDER BY salary) AS percent_rank,
    NTILE(4) OVER (PARTITION BY department_id ORDER BY salary) AS quartile
FROM employees
WHERE department_id IN (30, 60, 80)
ORDER BY department_id, salary;

SELECT
    employee_id,
    first_name,
    last_name,
    hire_date,
    NOW() AS current_date,
    ROUND(TIMESTAMPDIFF(MONTH, hire_date, NOW())) AS months_employed,
    DATE_ADD(hire_date, INTERVAL 6 MONTH) AS six_month_review,
    hire_date + INTERVAL (7 - WEEKDAY(hire_date)) DAY AS first_monday_after_hire,
    LAST_DAY(hire_date) AS last_day_of_hire_month,
    YEAR(hire_date) AS hire_year,
    MONTH(hire_date) AS hire_month,
    DAY(hire_date) AS hire_day
FROM employees
WHERE hire_date >= DATE_SUB(NOW(), INTERVAL 5 YEAR)
ORDER BY hire_date;

SELECT employee_id, first_name, last_name, salary
FROM employees
WHERE department_id = 50;

CREATE TABLE employees_with_bonus (
    employee_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    salary DECIMAL(8,2),
    commission_pct DECIMAL(2,2),
    bonus DECIMAL(10,2) AS (
        CASE
            WHEN commission_pct IS NOT NULL THEN salary * commission_pct * 12
            ELSE salary * 0.1
        END
    ) COMMENT "Generated bonus column",
    full_name VARCHAR(101) AS (CONCAT(first_name, ' ', last_name)) COMMENT "Generated full name"
)
ENGINE=OLAP
DUPLICATE KEY(employee_id)
DISTRIBUTED BY HASH(employee_id) BUCKETS 10
PROPERTIES("replication_num" = "1");