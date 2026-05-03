CREATE OR REPLACE FUNCTION divide_numbers (
    num1 IN NUMBER,
    num2 IN NUMBER
)
RETURN NUMBER
IS
    result NUMBER;
BEGIN
    IF num2 = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, '除数不能为0');
END IF;
    result := num1 / num2;
RETURN result;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20002, '发生未知错误: ' || SQLERRM);
END;
----------------------------
CREATE OR REPLACE VIEW employee_department_view AS
SELECT e.employee_id, e.first_name || ' ' || e.last_name AS full_name,
       d.department_name
FROM employees e
         JOIN departments d ON e.department_id = d.department_id;
--------------------------
CREATE MATERIALIZED VIEW mv_salary_summary
BUILD DEFERRED
REFRESH COMPLETE
ON DEMAND
AS
SELECT department_id, AVG(salary) avg_salary, COUNT(*) employee_count
FROM employees
GROUP BY department_id;
------------------------
CREATE OR REPLACE TRIGGER e.before_emp_insert
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN
SELECT emp_seq.NEXTVAL INTO :NEW.employee_id FROM dual;
END;
----------
SELECT C.cust_last_name, C.country_id
FROM customers C
WHERE EXISTS (SELECT 1
              FROM sales S
              WHERE S.quantity_sold > 1000 and
                  S.cust_id = C.cust_id);
------------------------
 CREATE OR REPLACE EDITIONABLE TRIGGER "SCOTT"."trg_users_bi2"
before insert
on "SCOTT"."USERS"
for each row
begin
  IF :NEW.id IS NULL THEN
    :NEW.id := seq_users.NEXTVAL;
  END IF;
end;
ALTER TRIGGER "SCOTT"."trg_users_bi" ENABLE;
------------------------
 CREATE TABLE "SCOTT"."table_namedawda0"
   (	"column_name" NUMBER,
	"column_name_1" NUMBER NOT NULL ENABLE,
	 PRIMARY KEY ("column_name_1")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
------------------------
drop TRIGGER TRIG_P22K_TafaA6BLE_AdUTO;
------------------------
CREATE MATERIALIZED VIEW mv_daily_cust_amount
BUILD IMMEDIATE
REFRESH COMPLETE ON DEMAND
START WITH TRUNC(SYSDATE) + 1        -- 明天 00:00 首次
NEXT TRUNC(SYSDATE) + 1 + 1/24       -- 之后每小时
AS
SELECT c.customer_id, SUM(o.amount) AS daily_amount
FROM orders o
JOIN customers c ON c.customer_id = o.customer_id
WHERE o.order_date >= TRUNC(SYSDATE)
GROUP BY c.customer_id;
------------------------
CREATE MATERIALIZED VIEW LOG ON customers
WITH ROWID, SEQUENCE (customer_name)
INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW LOG ON orders
WITH ROWID, SEQUENCE (customer_id, amount, order_date)
INCLUDING NEW VALUES;