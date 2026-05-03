SELECT id, name, email FROM users WHERE active = 1;

SELECT 
    employee_id,
    department_id,
    salary,
    ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) as row_num,
    RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) as rank_val,
    DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) as dense_rank_val,
    NTILE(4) OVER (ORDER BY salary) as quartile,
    LAG(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY employee_id) as prev_salary,
    LEAD(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY employee_id) as next_salary,
    FIRST_VALUE(employee_name) OVER (PARTITION BY department_id ORDER BY salary DESC) as highest_paid,
    LAST_VALUE(employee_name) OVER (PARTITION BY department_id ORDER BY salary DESC ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as lowest_paid
FROM employees e
JOIN departments d ON e.department_id = d.id
WHERE e.hire_date >= '2020-01-01';

WITH category_tree AS (
    SELECT id, name, parent_id, 0 as level
    FROM categories 
    WHERE parent_id IS NULL
    
    UNION ALL
    
    SELECT c.id, c.name, c.parent_id, ct.level + 1
    FROM categories c
    JOIN category_tree ct ON c.parent_id = ct.id
)
SELECT id, name, level, REPEAT('  ', level) || name as indented_name
FROM category_tree
ORDER BY level, name;

SELECT 
    c.customer_name,
    o.order_date,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    (oi.quantity * oi.unit_price) as line_total,
    SUM(oi.quantity * oi.unit_price) OVER (PARTITION BY c.customer_id) as customer_total,
    AVG(oi.quantity * oi.unit_price) OVER (PARTITION BY p.category_id) as category_avg
FROM customers c
INNER JOIN orders o ON c.customer_id = o.customer_id
LEFT JOIN order_items oi ON o.order_id = oi.order_id
RIGHT JOIN products p ON oi.product_id = p.product_id
WHERE o.order_date BETWEEN '2023-01-01' AND '2023-12-31'
  AND (p.discontinued = 0 OR p.discontinued IS NULL)
ORDER BY c.customer_name, o.order_date;

SELECT 
    supplier_name,
    (SELECT COUNT(*) FROM products p WHERE p.supplier_id = s.supplier_id) as product_count,
    (SELECT AVG(unit_price) FROM products p WHERE p.supplier_id = s.supplier_id) as avg_price
FROM suppliers s
WHERE EXISTS (
    SELECT 1 
    FROM products p 
    INNER JOIN order_items oi ON p.product_id = oi.product_id
    WHERE p.supplier_id = s.supplier_id 
      AND oi.order_id IN (
          SELECT order_id 
          FROM orders 
          WHERE order_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
      )
)
AND s.active = 1;

(SELECT employee_name, salary, 'high earner' as category
 FROM employees 
 WHERE salary > 100000)
UNION ALL
(SELECT employee_name, salary, 'medium earner' as category
 FROM employees 
 WHERE salary BETWEEN 50000 AND 100000)
UNION ALL
(SELECT employee_name, salary, 'low earner' as category
 FROM employees 
 WHERE salary < 50000)
ORDER BY salary DESC;

SELECT *
FROM users
WHERE email REGEXP '^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$'
   OR phone REGEXP '^\(?[0-9]{3}\)?[-. ]?[0-9]{3}[-. ]?[0-9]{4}$';

SELECT product_name, description, MATCH(description) AGAINST('waterproof camera' IN NATURAL LANGUAGE MODE) as relevance
FROM products
WHERE MATCH(description) AGAINST('waterproof camera' IN NATURAL LANGUAGE MODE)
ORDER BY relevance DESC;

SELECT 
    id,
    JSON_EXTRACT(user_data, '$.preferences.theme') as theme,
    JSON_EXTRACT(user_data, '$.profile.firstName') as first_name,
    JSON_UNQUOTE(JSON_EXTRACT(user_data, '$.profile.lastName')) as last_name
FROM user_profiles
WHERE JSON_EXTRACT(user_data, '$.preferences.notifications') = 'true';

SELECT 
    YEAR(order_date) as order_year,
    MONTH(order_date) as order_month,
    COUNT(*) as order_count,
    SUM(total_amount) as total_revenue,
    AVG(total_amount) as avg_order_value,
    MIN(total_amount) as min_order_value,
    MAX(total_amount) as max_order_value
FROM orders
WHERE order_date >= DATE_SUB(NOW(), INTERVAL 2 YEAR)
GROUP BY YEAR(order_date), MONTH(order_date)
HAVING COUNT(*) > 10 AND SUM(total_amount) > 10000
ORDER BY order_year DESC, order_month DESC;

SELECT 
    employee_id,
    employee_name,
    department_id,
    salary,
    CASE 
        WHEN salary > 150000 THEN 'Executive'
        WHEN salary > 100000 THEN 'Senior'
        WHEN salary > 75000 THEN 'Mid-Level'
        WHEN salary > 50000 THEN 'Junior'
        ELSE 'Entry-Level'
    END as salary_level,
    CASE department_id
        WHEN 1 THEN 'Engineering'
        WHEN 2 THEN 'Marketing'
        WHEN 3 THEN 'Sales'
        WHEN 4 THEN 'HR'
        WHEN 5 THEN 'Finance'
        ELSE 'Other'
    END as department_name
FROM employees
ORDER BY department_id, salary DESC;

SET @row_number = 0;
SELECT 
    (@row_number := @row_number + 1) AS row_num,
    employee_name,
    salary
FROM employees
WHERE salary > 80000
ORDER BY salary DESC;

SELECT 
    DATE_FORMAT(created_at, '%Y-%m') as `year_month`,
    COUNT(*) as monthly_users,
    LAG(COUNT(*), 1) OVER (ORDER BY DATE_FORMAT(created_at, '%Y-%m')) as prev_month_users,
    COUNT(*) - LAG(COUNT(*), 1) OVER (ORDER BY DATE_FORMAT(created_at, '%Y-%m')) as user_growth,
    ROUND(
        (COUNT(*) - LAG(COUNT(*), 1) OVER (ORDER BY DATE_FORMAT(created_at, '%Y-%m')))
        / LAG(COUNT(*), 1) OVER (ORDER BY DATE_FORMAT(created_at, '%Y-%m')) * 100, 
        2
    ) as growth_percentage
FROM users
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
GROUP BY DATE_FORMAT(created_at, '%Y-%m')
ORDER BY `year_month`;

SELECT 
    employee_id,
    employee_name,
    GetEmployeeSalary(employee_id) as current_salary,
    GetEmployeeSalary(employee_id) * 1.1 as proposed_salary
FROM employees
WHERE GetEmployeeSalary(employee_id) IS NOT NULL;

SELECT 
    customer_id,
    order_date,
    total_amount
FROM orders_partitioned
WHERE order_date BETWEEN '2023-01-01' AND '2023-03-31'
  AND customer_id IN (
    SELECT customer_id 
    FROM customer_segments 
    WHERE segment_name = 'VIP'
  );

INSERT INTO monthly_sales_summary (year, month, product_category, total_sales, transaction_count)
SELECT 
    YEAR(o.order_date) as year,
    MONTH(o.order_date) as month,
    p.category_id as product_category,
    SUM(oi.quantity * oi.unit_price) as total_sales,
    COUNT(*) as transaction_count
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE o.order_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
  AND o.order_date < NOW()
GROUP BY YEAR(o.order_date), MONTH(o.order_date), p.category_id
ON DUPLICATE KEY UPDATE 
    total_sales = VALUES(total_sales),
    transaction_count = VALUES(transaction_count);

CREATE OR REPLACE VIEW employee_performance_view AS
SELECT 
    e.employee_id,
    e.employee_name,
    d.department_name,
    e.salary,
    COUNT(p.project_id) as project_count,
    AVG(p.completion_score) as avg_completion_score,
    RANK() OVER (PARTITION BY e.department_id ORDER BY e.salary DESC) as salary_rank
FROM employees e
JOIN departments d ON e.department_id = d.department_id
LEFT JOIN employee_projects ep ON e.employee_id = ep.employee_id
LEFT JOIN projects p ON ep.project_id = p.project_id
WHERE e.active = 1
GROUP BY e.employee_id, e.employee_name, d.department_name, e.salary;

SELECT * FROM employee_performance_view WHERE salary_rank <= 5;