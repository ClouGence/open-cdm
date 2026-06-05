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
WHERE hire_date >= TO_DATE('2020-01-01', 'YYYY-MM-DD');

WITH dept_costs AS (
    SELECT department_id, SUM(salary) dept_total
    FROM employees
    GROUP BY department_id),
avg_cost AS (
    SELECT SUM(dept_total)/COUNT(*) avg
    FROM dept_costs)
SELECT dc.department_id, dc.dept_total, ac.avg
FROM dept_costs dc, avg_cost ac
WHERE dc.dept_total > ac.avg;


CREATE OR REPLACE PROCEDURE calc_bonus (
    emp_id IN employees.employee_id%TYPE,
    bonus_rate IN NUMBER DEFAULT 0.1,
    bonus OUT NUMBER
) AS
    emp_salary employees.salary%TYPE;
BEGIN
    SELECT salary INTO emp_salary
    FROM employees
    WHERE employee_id = emp_id;

    bonus := emp_salary * bonus_rate;

    INSERT INTO bonus_log (employee_id, calculation_date, rate, bonus_amount)
    VALUES (emp_id, SYSDATE, bonus_rate, bonus);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'Employee not found: ' || emp_id);
    WHEN TOO_MANY_ROWS THEN
        RAISE_APPLICATION_ERROR(-20002, 'Multiple employees found with same ID: ' || emp_id);
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Error calculating bonus for employee ' || emp_id || ': ' || SQLERRM);
END calc_bonus;

CREATE OR REPLACE FUNCTION get_dept_avg_salary (
    p_dept_id IN employees.department_id%TYPE
) RETURN NUMBER AS
    avg_sal NUMBER;
BEGIN
    SELECT AVG(salary) INTO avg_sal
    FROM employees
    WHERE department_id = p_dept_id;

    RETURN NVL(avg_sal, 0);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20004, 'Error calculating average salary for department ' || p_dept_id || ': ' || SQLERRM);
END get_dept_avg_salary;

CREATE OR REPLACE TRIGGER audit_employee_changes
FOR INSERT OR UPDATE OR DELETE ON employees
COMPOUND TRIGGER

    TYPE emp_ids_t IS TABLE OF employees.employee_id%TYPE;
    old_emp_ids emp_ids_t := emp_ids_t();
    new_emp_ids emp_ids_t := emp_ids_t();

    -- 声明变量用于保存操作类型
    action_type VARCHAR2(10);

    BEFORE EACH ROW IS
    BEGIN
        IF INSERTING THEN
            action_type := 'INSERT';
        ELSIF UPDATING THEN
            action_type := 'UPDATE';
        ELSIF DELETING THEN
            action_type := 'DELETE';
        END IF;
    END BEFORE EACH ROW;

    AFTER EACH ROW IS
    BEGIN
        IF INSERTING THEN
            new_emp_ids.EXTEND;
            new_emp_ids(new_emp_ids.COUNT) := :NEW.employee_id;
        ELSIF UPDATING THEN
            old_emp_ids.EXTEND;
            old_emp_ids(old_emp_ids.COUNT) := :OLD.employee_id;
            new_emp_ids.EXTEND;
            new_emp_ids(new_emp_ids.COUNT) := :NEW.employee_id;
        ELSIF DELETING THEN
            old_emp_ids.EXTEND;
            old_emp_ids(old_emp_ids.COUNT) := :OLD.employee_id;
        END IF;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
    BEGIN
        FORALL i IN INDICES OF new_emp_ids
            INSERT INTO employee_audit (employee_id, action_type, action_timestamp)
            VALUES (new_emp_ids(i), action_type, SYSTIMESTAMP);

        FORALL i IN INDICES OF old_emp_ids
            INSERT INTO employee_audit (employee_id, action_type, action_timestamp)
            VALUES (old_emp_ids(i), action_type, SYSTIMESTAMP);
    END AFTER STATEMENT;

END audit_employee_changes;


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
    employee_id,
    first_name,
    last_name,
    email,
    phone_number
FROM employees
WHERE REGEXP_LIKE(email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
   OR REGEXP_INSTR(phone_number, '[^0-9\-\.\(\)\s]') > 0;

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
    SYSDATE AS current_date,
    ROUND(MONTHS_BETWEEN(SYSDATE, hire_date)) AS months_employed,
    ADD_MONTHS(hire_date, 6) AS six_month_review,
    NEXT_DAY(hire_date, 'MONDAY') AS first_monday_after_hire,
    LAST_DAY(hire_date) AS last_day_of_hire_month,
    ROUND(hire_date, 'YEAR') AS hire_year_rounded,
    EXTRACT(YEAR FROM hire_date) AS hire_year,
    EXTRACT(MONTH FROM hire_date) AS hire_month,
    EXTRACT(DAY FROM hire_date) AS hire_day
FROM employees
WHERE hire_date >= ADD_MONTHS(SYSDATE, -12 * 5)
ORDER BY hire_date;

SELECT country, prod, year, s
FROM sales_view
MODEL
    PARTITION BY (country)
    DIMENSION BY (prod, year)
    MEASURES (sale s)
    IGNORE NAV
    UNIQUE DIMENSION
    RULES UPSERT SEQUENTIAL ORDER (
        s['Mouse Pad', 2025] = s['Mouse Pad', 2024] + s['Mouse Pad', 2023],
        s['Standard Mouse', 2025] = s['Standard Mouse', 2024] * 1.1
    )
ORDER BY country, prod, year;

SELECT employee_id, first_name, last_name, salary
FROM employees AS OF TIMESTAMP SYSDATE - INTERVAL '1' HOUR
WHERE department_id = 50;

CREATE TABLE employees_with_bonus (
    employee_id NUMBER PRIMARY KEY,
    first_name VARCHAR2(50),
    last_name VARCHAR2(50),
    salary NUMBER(8,2),
    commission_pct NUMBER(2,2),
    bonus GENERATED ALWAYS AS (
        CASE
            WHEN commission_pct IS NOT NULL THEN salary * commission_pct * 12
            ELSE salary * 0.1
        END
    ) VIRTUAL,
    full_name GENERATED ALWAYS AS (first_name || ' ' || last_name) VIRTUAL
);


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
WHERE hire_date >= TO_DATE('2020-01-01', 'YYYY-MM-DD');

WITH dept_costs AS (
    SELECT department_id, SUM(salary) dept_total
    FROM employees
    GROUP BY department_id),
avg_cost AS (
    SELECT SUM(dept_total)/COUNT(*) avg
    FROM dept_costs)
SELECT dc.department_id, dc.dept_total, ac.avg
FROM dept_costs dc, avg_cost ac
WHERE dc.dept_total > ac.avg;


CREATE OR REPLACE PROCEDURE calc_bonus (
    emp_id IN employees.employee_id%TYPE,
    bonus_rate IN NUMBER DEFAULT 0.1,
    bonus OUT NUMBER
) AS
    emp_salary employees.salary%TYPE;
BEGIN
    SELECT salary INTO emp_salary
    FROM employees
    WHERE employee_id = emp_id;

    bonus := emp_salary * bonus_rate;

    INSERT INTO bonus_log (employee_id, calculation_date, rate, bonus_amount)
    VALUES (emp_id, SYSDATE, bonus_rate, bonus);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001, 'Employee not found: ' || emp_id);
    WHEN TOO_MANY_ROWS THEN
        RAISE_APPLICATION_ERROR(-20002, 'Multiple employees found with same ID: ' || emp_id);
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Error calculating bonus for employee ' || emp_id || ': ' || SQLERRM);
END calc_bonus;

CREATE OR REPLACE FUNCTION get_dept_avg_salary (
    p_dept_id IN employees.department_id%TYPE
) RETURN NUMBER AS
    avg_sal NUMBER;
BEGIN
    SELECT AVG(salary) INTO avg_sal
    FROM employees
    WHERE department_id = p_dept_id;

    RETURN NVL(avg_sal, 0);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20004, 'Error calculating average salary for department ' || p_dept_id || ': ' || SQLERRM);
END get_dept_avg_salary;

CREATE OR REPLACE TRIGGER audit_employee_changes
FOR INSERT OR UPDATE OR DELETE ON employees
COMPOUND TRIGGER

    TYPE emp_ids_t IS TABLE OF employees.employee_id%TYPE;
    old_emp_ids emp_ids_t := emp_ids_t();
    new_emp_ids emp_ids_t := emp_ids_t();

    action_type VARCHAR2(10);

    BEFORE EACH ROW IS
    BEGIN
        IF INSERTING THEN
            action_type := 'INSERT';
        ELSIF UPDATING THEN
            action_type := 'UPDATE';
        ELSIF DELETING THEN
            action_type := 'DELETE';
        END IF;
    END BEFORE EACH ROW;

    AFTER EACH ROW IS
    BEGIN
        IF INSERTING THEN
            new_emp_ids.EXTEND;
            new_emp_ids(new_emp_ids.COUNT) := :NEW.employee_id;
        ELSIF UPDATING THEN
            old_emp_ids.EXTEND;
            old_emp_ids(old_emp_ids.COUNT) := :OLD.employee_id;
            new_emp_ids.EXTEND;
            new_emp_ids(new_emp_ids.COUNT) := :NEW.employee_id;
        ELSIF DELETING THEN
            old_emp_ids.EXTEND;
            old_emp_ids(old_emp_ids.COUNT) := :OLD.employee_id;
        END IF;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
    BEGIN
        FORALL i IN INDICES OF new_emp_ids
            INSERT INTO employee_audit (employee_id, action_type, action_timestamp)
            VALUES (new_emp_ids(i), action_type, SYSTIMESTAMP);

        FORALL i IN INDICES OF old_emp_ids
            INSERT INTO employee_audit (employee_id, action_type, action_timestamp)
            VALUES (old_emp_ids(i), action_type, SYSTIMESTAMP);
    END AFTER STATEMENT;

END audit_employee_changes;


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
    employee_id,
    first_name,
    last_name,
    email,
    phone_number
FROM employees
WHERE REGEXP_LIKE(email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
   OR REGEXP_INSTR(phone_number, '[^0-9\-\.\(\)\s]') > 0;

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
    SYSDATE AS current_date,
    ROUND(MONTHS_BETWEEN(SYSDATE, hire_date)) AS months_employed,
    ADD_MONTHS(hire_date, 6) AS six_month_review,
    NEXT_DAY(hire_date, 'MONDAY') AS first_monday_after_hire,
    LAST_DAY(hire_date) AS last_day_of_hire_month,
    ROUND(hire_date, 'YEAR') AS hire_year_rounded,
    EXTRACT(YEAR FROM hire_date) AS hire_year,
    EXTRACT(MONTH FROM hire_date) AS hire_month,
    EXTRACT(DAY FROM hire_date) AS hire_day
FROM employees
WHERE hire_date >= ADD_MONTHS(SYSDATE, -12 * 5)
ORDER BY hire_date;

SELECT country, prod, year, s
FROM sales_view
MODEL
    PARTITION BY (country)
    DIMENSION BY (prod, year)
    MEASURES (sale s)
    IGNORE NAV
    UNIQUE DIMENSION
    RULES UPSERT SEQUENTIAL ORDER (
        s['Mouse Pad', 2025] = s['Mouse Pad', 2024] + s['Mouse Pad', 2023],
        s['Standard Mouse', 2025] = s['Standard Mouse', 2024] * 1.1
    )
ORDER BY country, prod, year;

SELECT employee_id, first_name, last_name, salary
FROM employees AS OF TIMESTAMP SYSDATE - INTERVAL '1' HOUR
WHERE department_id = 50;

CREATE TABLE employees_with_bonus (
    employee_id NUMBER PRIMARY KEY,
    first_name VARCHAR2(50),
    last_name VARCHAR2(50),
    salary NUMBER(8,2),
    commission_pct NUMBER(2,2),
    bonus GENERATED ALWAYS AS (
        CASE
            WHEN commission_pct IS NOT NULL THEN salary * commission_pct * 12
            ELSE salary * 0.1
        END
    ) VIRTUAL,
    full_name GENERATED ALWAYS AS (first_name || ' ' || last_name) VIRTUAL
);

CREATE TABLE sales (
    salesman_id NUMBER,
    product_id NUMBER,
    sale_date DATE,
    amount NUMBER(10, 2)
)
PARTITION BY RANGE (sale_date) (
    PARTITION sales_q1 VALUES LESS THAN (TO_DATE('01-APR-2025', 'DD-MON-YYYY')),
    PARTITION sales_q2 VALUES LESS THAN (TO_DATE('01-JUL-2025', 'DD-MON-YYYY')),
    PARTITION sales_q3 VALUES LESS THAN (TO_DATE('01-OCT-2025', 'DD-MON-YYYY')),
    PARTITION sales_q4 VALUES LESS THAN (TO_DATE('01-JAN-2026', 'DD-MON-YYYY'))
);

CREATE BITMAP INDEX idx_sales_product
ON sales(product_id);

CREATE INDEX idx_emp_upper_lastname
ON employees(UPPER(last_name));

CREATE INDEX idx_resume_skills
ON employees(resume) INDEXTYPE IS ctxsys.context;

CREATE MATERIALIZED VIEW LOG ON employees
WITH SEQUENCE, ROWID (employee_id, first_name, last_name, department_id)
INCLUDING NEW VALUES;

BEGIN
    DBMS_FGA.ADD_POLICY(
        object_schema => 'HR',
        object_name => 'EMPLOYEES',
        policy_name => 'CHK_EMP_SAL',
        audit_condition => 'SALARY > 10000',
        audit_column => 'SALARY'
    );
END;
/

SELECT employee_id, first_name, last_name
FROM employees AS OF SCN 1234567890;

SELECT versions_starttime, versions_endtime, versions_xid, versions_operation, salary
FROM employees VERSIONS BETWEEN TIMESTAMP
    TO_TIMESTAMP('2025-01-01 09:00:00', 'YYYY-MM-DD HH:MI:SS') AND
    TO_TIMESTAMP('2025-12-31 17:00:00', 'YYYY-MM-DD HH:MI:SS')
WHERE employee_id = 100;


SELECT *
FROM employees SAMPLE (10)
WHERE department_id = 50;


SELECT employee_id, first_name, last_name
FROM employees e
WHERE EXISTS (
    SELECT /*+ UNNEST */ 1
    FROM departments d
    WHERE d.department_id = e.department_id AND d.location_id = 1700
);

SELECT employee_id, first_name, last_name, salary, department_id,
       RATIO_TO_REPORT(salary) OVER (PARTITION BY department_id) AS ratio_in_dept,
       STDDEV(salary) OVER (PARTITION BY department_id) AS stddev_in_dept
FROM employees
ORDER BY department_id, salary DESC;

INSERT ALL
    WHEN salary < 5000 THEN
        INTO low_salary_emps VALUES (employee_id, first_name, last_name, salary)
    WHEN salary >= 5000 AND salary < 10000 THEN
        INTO mid_salary_emps VALUES (employee_id, first_name, last_name, salary)
    WHEN salary >= 10000 THEN
        INTO high_salary_emps VALUES (employee_id, first_name, last_name, salary)
SELECT employee_id, first_name, last_name, salary
FROM employees;

INSERT FIRST
    WHEN salary < 5000 THEN
        INTO low_salary_emps VALUES (employee_id, first_name, last_name, salary)
    WHEN salary < 10000 THEN
        INTO mid_salary_emps VALUES (employee_id, first_name, last_name, salary)
    ELSE
        INTO high_salary_emps VALUES (employee_id, first_name, last_name, salary)
SELECT employee_id, first_name, last_name, salary
FROM employees;

DECLARE
    TYPE emp_id_array IS TABLE OF employees.employee_id%TYPE;
    TYPE salary_array IS TABLE OF employees.salary%TYPE;
    l_emp_ids emp_id_array;
    l_salaries salary_array;
BEGIN
    SELECT employee_id, salary * 1.1
    BULK COLLECT INTO l_emp_ids, l_salaries
    FROM employees
    WHERE department_id = 60;

    FORALL i IN l_emp_ids.FIRST..l_emp_ids.LAST
        UPDATE employees
        SET salary = l_salaries(i)
        WHERE employee_id = l_emp_ids(i);
END;
/

DECLARE
    l_sql VARCHAR2(1000);
    l_cursor SYS_REFCURSOR;
    l_employee_id employees.employee_id%TYPE;
    l_first_name employees.first_name%TYPE;
    l_last_name employees.last_name%TYPE;
BEGIN
    l_sql := 'SELECT employee_id, first_name, last_name FROM employees WHERE department_id = :dept_id';
    OPEN l_cursor FOR l_sql USING 60;
    LOOP
        FETCH l_cursor INTO l_employee_id, l_first_name, l_last_name;
        EXIT WHEN l_cursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(l_employee_id || ' ' || l_first_name || ' ' || l_last_name);
    END LOOP;
    CLOSE l_cursor;
END;
/
