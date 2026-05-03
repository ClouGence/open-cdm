-- 补充Doris SQL测试用例
-- 目标：覆盖之前测试文件中缺失或不足的功能

-- JSON相关函数测试
SELECT JSON_EXTRACT('{"id": 1, "name": "test"}', '$.id') AS json_id;
SELECT JSON_UNQUOTE(JSON_EXTRACT('{"name": "test"}', '$.name')) AS json_name;
SELECT JSON_LENGTH('{"id": 1, "tags": ["a", "b"]}') AS json_length;
SELECT JSON_TYPE('{"id": 1}') AS json_type;
SELECT JSON_VALID('{"id": 1}') AS json_valid;
SELECT JSON_CONTAINS('{"id": 1, "name": "test"}', '"test"', '$.name') AS json_contains;

-- 正则表达式函数测试
SELECT REGEXP('^[0-9]+$', '12345') AS regexp_match;
SELECT REGEXP_REPLACE('abc123def', '[0-9]+', 'X') AS regexp_replace;
SELECT REGEXP_SUBSTR('abc123def', '[0-9]+') AS regexp_substr;

-- 位运算函数测试
SELECT BITAND(5, 3) AS bitand_result;
SELECT BITOR(5, 3) AS bitor_result;
SELECT BITXOR(5, 3) AS bitxor_result;
SELECT BITNOT(5) AS bitnot_result;

-- 数学函数测试
SELECT ABS(-10) AS abs_value;
SELECT CEIL(12.3) AS ceil_value;
SELECT FLOOR(12.8) AS floor_value;
SELECT ROUND(12.5) AS round_value;
SELECT SQRT(16) AS sqrt_value;
SELECT POW(2, 3) AS pow_value;
SELECT MOD(10, 3) AS mod_value;

-- 字符串函数测试
SELECT CONCAT_WS('-', 'a', 'b', 'c') AS concat_ws_result;
SELECT LOCATE('bc', 'abcdef') AS locate_result;
SELECT REPLACE('abcabc', 'bc', 'xy') AS replace_result;
SELECT SUBSTRING_INDEX('www.apache.org', '.', 2) AS substring_index_result;
SELECT LPAD('hi', 5, '?') AS lpad_result;
SELECT RPAD('hi', 5, '?') AS rpad_result;
SELECT TRIM(BOTH ' ' FROM '  hello  ') AS trim_result;

-- 地理函数测试
SELECT ST_Point(1, 2) AS point_result;
SELECT ST_X(ST_Point(1, 2)) AS x_result;
SELECT ST_Y(ST_Point(1, 2)) AS y_result;

-- ARRAY相关函数测试
SELECT ARRAY(1, 2, 3) AS array_result;
SELECT ARRAY_APPEND([1, 2], 3) AS array_append_result;
SELECT ARRAY_SIZE([1, 2, 3]) AS array_size_result;
SELECT ARRAY_CONTAINS([1, 2, 3], 2) AS array_contains_result;

-- MAP相关函数测试
SELECT MAP('name', 'test', 'id', 1) AS map_result;
SELECT MAP_KEYS(MAP('name', 'test', 'id', 1)) AS map_keys_result;
SELECT MAP_VALUES(MAP('name', 'test', 'id', 1)) AS map_values_result;

-- STRUCT相关函数测试
SELECT STRUCT('name', 'John', 'age', 30) AS struct_result;
SELECT STRUCT_ELEMENT(STRUCT('name', 'John', 'age', 30), 'name') AS struct_element_result;

-- 时间函数测试
SELECT DATE_ADD('2023-01-01', INTERVAL 1 DAY) AS date_add_result;
SELECT DATE_SUB('2023-01-01', INTERVAL 1 DAY) AS date_sub_result;
SELECT DATEDIFF('2023-01-05', '2023-01-01') AS datediff_result;
SELECT DATE_FORMAT('2023-01-01 12:00:00', '%Y-%m-%d') AS date_format_result;

-- 条件函数测试
SELECT NULLIF(1, 1) AS nullif_result;
SELECT IFNULL(NULL, 'default') AS ifnull_result;
SELECT COALESCE(NULL, NULL, 'first_non_null') AS coalesce_result;

-- 类型转换函数测试
SELECT CAST('123' AS INT) AS cast_int_result;
SELECT PARSE_URL('http://www.apache.org/path', 'HOST') AS parse_url_result;

-- 窗口函数补充测试
SELECT 
    employee_id,
    salary,
    LAG(salary, 2) OVER (ORDER BY employee_id) AS lag_2_result,
    LEAD(salary, 2) OVER (ORDER BY employee_id) AS lead_2_result,
    NTILE(5) OVER (ORDER BY salary) AS ntile_result
FROM employees;

-- 复杂子查询测试
SELECT * FROM employees e1 
WHERE e1.salary > (
    SELECT AVG(e2.salary) FROM employees e2 
    WHERE e2.department_id = e1.department_id
);

-- 相关子查询测试
SELECT department_id, 
       (SELECT COUNT(*) FROM employees e2 WHERE e2.department_id = e1.department_id) AS emp_count
FROM employees e1 
GROUP BY department_id;

-- 标量子查询测试
SELECT employee_id,
       (SELECT department_name FROM departments d WHERE d.department_id = e.department_id) AS dept_name
FROM employees e;

-- 错误处理测试（这些语句应该会导致错误，用于测试解析器的容错能力）
-- SELECT * FROM non_existent_table;
-- SELECT invalid_function('param');
-- SELECT * FROM employees WHERE invalid_column = 1;

-- 特殊数据类型操作测试
-- BITMAP类型测试
SELECT BITMAP_EMPTY() AS bitmap_empty_result;
SELECT TO_BITMAP(123) AS to_bitmap_result;

-- HLL类型测试
SELECT HLL_EMPTY() AS hll_empty_result;

-- IPv4/IPv6函数测试
SELECT IPV4_STRING_TO_NUM('192.168.1.1') AS ipv4_to_num_result;
SELECT IPV4_NUM_TO_STRING(3232235777) AS ipv4_to_string_result;

-- UUID函数测试
SELECT UUID() AS uuid_result;

-- 全文检索函数测试
SELECT MATCH_ANY('Doris is great', 'doris') AS match_any_result;
SELECT MATCH_ALL('Doris is great', 'doris great') AS match_all_result;
SELECT MATCH_PHRASE('Apache Doris is great', 'apache doris') AS match_phrase_result;

-- URL处理函数测试
SELECT URL_DECODE('https://www.apache.org/search?q=doris%20database') AS url_decode_result;
SELECT URL_ENCODE('https://www.apache.org/search?q=doris database') AS url_encode_result;

-- 编码解码函数测试
SELECT FROM_BASE64(TO_BASE64('hello')) AS base64_result;

-- 系统函数测试
SELECT CONNECTION_ID() AS connection_id_result;
SELECT CURRENT_VERSION() AS current_version_result;
SELECT DATABASE() AS current_database_result;
SELECT USER() AS current_user_result;

-- 会话变量测试
SET @custom_var = 'test_value';
SELECT @custom_var AS custom_var_result;

-- 多层嵌套子查询测试
SELECT *
FROM (
    SELECT department_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
) dept_avg
WHERE dept_avg.avg_salary > (
    SELECT AVG(salary) 
    FROM employees
);

-- 复杂JOIN测试
SELECT e.employee_id, e.first_name, d.department_name, l.city
FROM employees e
JOIN departments d ON e.department_id = d.department_id
LEFT JOIN locations l ON d.location_id = l.location_id
RIGHT JOIN jobs j ON e.job_id = j.job_id;

-- GROUPING SETS测试
SELECT department_id, job_id, COUNT(*), AVG(salary)
FROM employees
GROUP BY GROUPING SETS ((department_id, job_id), (department_id), (job_id), ());

-- ROLLUP测试
SELECT department_id, job_id, COUNT(*), AVG(salary)
FROM employees
GROUP BY ROLLUP(department_id, job_id);

-- CUBE测试
SELECT department_id, job_id, COUNT(*), AVG(salary)
FROM employees
GROUP BY CUBE(department_id, job_id);