-- PostgreSQL-specific data type testing
SELECT 
    c_uuid,
    c_json,
    c_jsonb,
    c_xml,
    c_tsvector,
    c_tsquery,
    c_point,
    c_circle,
    c_inet,
    c_cidr,
    c_macaddr
FROM tb_postgre_types 
WHERE c_uuid IS NOT NULL;

-- JSON/JSONB OPERATION TEST
SELECT 
    c_uuid,
    c_jsonb->'name' as jsonb_name,
    c_jsonb->>'name' as jsonb_name_text,
    c_jsonb#>'{address,street}' as jsonb_nested,
    c_jsonb#>>'{address,street}' as jsonb_nested_text,
    jsonb_array_elements(c_jsonb->'tags') as jsonb_tags
FROM tb_postgre_types 
WHERE jsonb_typeof(c_jsonb) = 'object';

-- Full Text Search Test
SELECT 
    c_uuid,
    ts_rank_cd(c_tsvector, to_tsquery('english', 'test')) as rank,
    c_tsvector @@ to_tsquery('english', 'test') as matches
FROM tb_postgre_types 
WHERE c_tsvector IS NOT NULL;

-- Geometric Type Operating Test
SELECT
    c_uuid,
    c_point,
    c_circle,
    point(0,0) <-> c_point as distance_from_origin,
    c_circle @> c_point as circle_contains_point
FROM tb_postgre_types
WHERE c_point IS NOT NULL;

-- Network Address Type Test
SELECT 
    c_uuid,
    c_inet,
    c_cidr,
    set_masklen(c_inet, 24) as subnet,
    c_inet << '192.168.1.0/24' as in_subnet
FROM tb_postgre_types 
WHERE c_inet IS NOT NULL;

-- Range Type Test
SELECT 
    c_uuid,
    c_int4range,
    lower(c_int4range) as range_lower,
    upper(c_int4range) as range_upper,
    c_int4range @> 5 as range_contains_value,
    c_int4range && '[1,10]'::int4range as ranges_overlap
FROM tb_postgre_types 
WHERE c_int4range IS NOT NULL;

-- Window Function Test (PostgreSQL-specific)
SELECT 
    c_uuid,
    row_number() OVER (PARTITION BY c_boolean ORDER BY c_serial),
    lag(c_smallint) OVER (ORDER BY c_serial),
    lead(c_smallint) OVER (ORDER BY c_serial),
    first_value(c_smallint) OVER (PARTITION BY c_boolean ORDER BY c_serial),
    last_value(c_smallint) OVER (PARTITION BY c_boolean ORDER BY c_serial ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),
    nth_value(c_smallint, 2) OVER (PARTITION BY c_boolean ORDER BY c_serial ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)
FROM tb_postgre_types;

-- Recursive CTE testing
WITH RECURSIVE tree AS (
    SELECT c_uuid, c_serial, 0 as level
    FROM tb_postgre_types 
    WHERE c_serial = 1
    
    UNION ALL
    
    SELECT t.c_uuid, t.c_serial, tree.level + 1
    FROM tb_postgre_types t
    JOIN tree ON t.c_serial = tree.level + 1
    WHERE tree.level < 5
)
SELECT * FROM tree;

-- String Function Test (PostgreSQL-specific)
SELECT 
    c_uuid,
    c_text,
    left(c_text, 5) as left_text,
    right(c_text, 5) as right_text,
    substring(c_text from 2 for 4) as substring_text,
    position('test' in c_text) as position_of_test,
    overlay(c_text placing 'new' from 3 for 4) as overlay_text
FROM tb_postgre_types 
WHERE c_text IS NOT NULL;

-- Math function test (PostgreSQL-specific)
SELECT 
    c_uuid,
    c_numeric_p_s,
    round(c_numeric_p_s) as rounded,
    ceil(c_numeric_p_s) as ceiling,
    floor(c_numeric_p_s) as floored,
    abs(c_numeric_p_s) as absolute,
    power(c_numeric_p_s, 2) as squared,
    sqrt(abs(c_numeric_p_s)) as square_root
FROM tb_postgre_types 
WHERE c_numeric_p_s IS NOT NULL;

-- Date time function test (PostgreSQL-specific)
SELECT 
    c_timestamp,
    c_tstzrange,
    date_part('year', c_timestamp) as year_part,
    date_trunc('month', c_timestamp) as month_truncated,
    age(c_timestamp) as age_since_timestamp,
    justify_days(c_interval) as justified_interval
FROM tb_postgre_types 
WHERE c_timestamp IS NOT NULL;

-- Conditional Expression Test
SELECT 
    c_uuid,
    c_integer,
    CASE 
        WHEN c_integer > 100 THEN 'large'
        WHEN c_integer > 50 THEN 'medium'
        WHEN c_integer > 0 THEN 'small'
        ELSE 'zero_or_negative'
    END as size_category,
    NULLIF(c_integer, 0) as null_if_zero,
    COALESCE(c_integer, c_smallint, 0) as coalesced_value
FROM tb_postgre_types;

-- Aggregation function test (including PostgreSQL-specific)
SELECT 
    c_boolean,
    count(*) as count_all,
    count(c_integer) as count_integer,
    array_agg(c_integer) as integer_array,
    string_agg(c_text, ', ') as text_concatenation,
    bool_and(c_boolean) as all_true,
    bool_or(c_boolean) as any_true,
    percentile_cont(0.5) within group (order by c_integer) as median_integer
FROM tb_postgre_types
GROUP BY c_boolean;

-- Sub-Query and Related Sub-Query Test
SELECT 
    c_uuid,
    c_integer,
    (SELECT avg(c_integer) FROM tb_postgre_types) as overall_avg,
    (SELECT avg(c_integer) FROM tb_postgre_types t2 WHERE t2.c_boolean = t1.c_boolean) as group_avg,
    c_integer > (SELECT avg(c_integer) FROM tb_postgre_types) as above_average
FROM tb_postgre_types t1
WHERE c_integer IS NOT NULL;

-- Lock and co-exposure control tests
SELECT * FROM tb_postgre_types WHERE c_serial = 1 FOR UPDATE;
SELECT * FROM tb_postgre_types WHERE c_serial = 2 FOR SHARE;


-- Relevant operating tests of partition table (if partition table exists)
SELECT 
    inhparent::regclass as parent_table,
    inhrelid::regclass as child_table
FROM pg_inherits 
WHERE inhparent = 'tb_postgre_types'::regclass;

-- System table query testing
SELECT 
    schemaname,
    tablename,
    tableowner
FROM pg_tables 
WHERE tablename = 'tb_postgre_types';

-- Custom type and list type tests (if any)
SELECT 
    t.typname as type_name,
    t.typtype as type_type,
    CASE 
        WHEN t.typtype = 'b' THEN 'base'
        WHEN t.typtype = 'c' THEN 'composite'
        WHEN t.typtype = 'd' THEN 'domain'
        WHEN t.typtype = 'e' THEN 'enum'
        WHEN t.typtype = 'p' THEN 'pseudo'
        WHEN t.typtype = 'r' THEN 'range'
        ELSE 'unknown'
    END as type_description
FROM pg_type t
JOIN pg_namespace n ON n.oid = t.typnamespace
WHERE n.nspname = 'public';

-- PostgreSQL Advanced Functional Test Statement
-- Complex statements including stored procedures, triggers, rules, etc.

-- Create Test Table
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    salary NUMERIC(10,2),
    hire_date DATE
);

CREATE TABLE employee_audit (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER,
    action VARCHAR(10),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create stored procedure/function
CREATE OR REPLACE FUNCTION update_employee_salary(emp_id INTEGER, new_salary NUMERIC)
RETURNS VOID AS $$
BEGIN
    UPDATE employees
    SET salary = new_salary
    WHERE id = emp_id;
END;
$$ LANGUAGE plpgsql;

-- Create trigger function
CREATE OR REPLACE FUNCTION audit_employee_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO employee_audit (employee_id, action) VALUES (OLD.id, 'DELETE');
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO employee_audit (employee_id, action) VALUES (NEW.id, 'UPDATE');
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO employee_audit (employee_id, action) VALUES (NEW.id, 'INSERT');
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create Trigger
CREATE TRIGGER employee_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON employees
    FOR EACH ROW EXECUTE FUNCTION audit_employee_changes();

-- Create View
CREATE VIEW high_paid_employees AS
SELECT id, name, department, salary
FROM employees
WHERE salary > 80000;

-- Create an objectistic view
CREATE MATERIALIZED VIEW employee_department_stats AS
SELECT department,
       COUNT(*) as employee_count,
       AVG(salary) as avg_salary
FROM employees
GROUP BY department;

-- Create table with custom type
CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    name full_name,
    email email_address,
    status employee_status DEFAULT 'active'
);

-- Insert Test Data
INSERT INTO employees (name, department, salary, hire_date) VALUES
('John Doe', 'Engineering', 75000, '2022-01-15'),
('Jane Smith', 'Marketing', 65000, '2021-03-22'),
('Bob Johnson', 'Engineering', 95000, '2020-07-10'),
('Alice Brown', 'Sales', 70000, '2023-02-01');

-- Update statement call memory process
SELECT update_employee_salary(1, 80000);

-- Complex query: Window function combined with CTE
WITH department_salaries AS (
    SELECT
        department,
        name,
        salary,
        ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as dept_rank,
        RANK() OVER (ORDER BY salary DESC) as overall_rank
    FROM employees
)
SELECT
    department,
    name,
    salary,
    dept_rank,
    overall_rank,
    FIRST_VALUE(name) OVER (PARTITION BY department ORDER BY salary DESC) as highest_paid_in_dept
FROM department_salaries
WHERE dept_rank <= 2;

-- Example of JSONB operation
CREATE TABLE product_catalog (
    id SERIAL PRIMARY KEY,
    product_data JSONB
);

INSERT INTO product_catalog (product_data) VALUES
('{"name": "Laptop", "price": 1200, "specs": {"cpu": "Intel i7", "ram": "16GB"}}'),
('{"name": "Phone", "price": 800, "specs": {"cpu": "Snapdragon 888", "ram": "8GB"}}');

-- JSONB queries and updates
SELECT product_data->>'name' as product_name,
       product_data->'specs'->>'cpu' as cpu_spec
FROM product_catalog
WHERE product_data @> '{"specs": {"ram": "16GB"}}';

UPDATE product_catalog
SET product_data = product_data || '{"discount": 0.1}'::jsonb
WHERE product_data->>'name' = 'Laptop';

-- Other Organiser
CREATE TABLE articles (
    id SERIAL PRIMARY KEY,
    title TEXT,
    body TEXT,
    search_vector TSVECTOR
);

INSERT INTO articles (title, body) VALUES
('PostgreSQL全文搜索', 'PostgreSQL提供了强大的全文搜索功能，支持多种语言'),
('数据库优化技巧', '本文介绍了一些数据库查询优化的基本技巧');

UPDATE articles
SET search_vector = to_tsvector('chinese', coalesce(title,'') || ' ' || coalesce(body,''));

-- Create full-text search index
CREATE INDEX idx_articles_search ON articles USING GIN (search_vector);

-- Full text search query
SELECT title, body FROM articles
WHERE search_vector @@ to_tsquery('chinese', '数据库');

-- Examples of divisions
CREATE TABLE sales_data (
    id SERIAL,
    sale_date DATE NOT NULL,
    amount NUMERIC(10,2)
) PARTITION BY RANGE (sale_date);

CREATE TABLE sales_data_2023 PARTITION OF sales_data
FOR VALUES FROM ('2023-01-01') TO ('2023-12-31');

CREATE TABLE sales_data_2024 PARTITION OF sales_data
FOR VALUES FROM ('2024-01-01') TO ('2024-12-31');

-- Refresh Image View
REFRESH MATERIALIZED VIEW employee_department_stats;

-- Query Graphical View
SELECT * FROM employee_department_stats;

-- Delete statement
DELETE FROM employees WHERE id = 4;
