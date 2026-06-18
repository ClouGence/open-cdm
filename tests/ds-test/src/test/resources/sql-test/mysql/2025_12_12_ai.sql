SELECT 
    5 & 3 as bitwise_and,
    5 | 3 as bitwise_or,
    5 ^ 3 as bitwise_xor,
    ~5 as bitwise_not,
    1 << 2 as left_shift,
    8 >> 2 as right_shift;

SELECT 
    JSON_OBJECT('name', 'John', 'age', 30, 'city', 'New York') as json_obj,
    JSON_ARRAY(1, 2, 3, 'apple', 'banana') as json_arr,
    JSON_MERGE_PRESERVE('[1, 2]', '[true, false]') as json_merged,
    JSON_SET('[1,2]', '$[0]', 10) as json_set,
    JSON_INSERT('[1,2]', '$[2]', 3) as json_insert,
    JSON_REPLACE('[1,2]', '$[1]', 20) as json_replace,
    JSON_REMOVE('[1,2,3]', '$[1]') as json_remove,
    JSON_CONTAINS('[1,2,3]', '2') as json_contains,
    JSON_LENGTH('[1,2,3]') as json_length,
    JSON_TYPE('{"a": 1}') as json_type;

SELECT 
    CONCAT('Hello', ' ', 'World') as concat_result,
    CONCAT_WS('-', 'Apple', 'Banana', 'Cherry') as concat_ws_result,
    FORMAT(123456.789, 2) as formatted_number,
    INSERT('HelloWorld', 6, 0, ' ') as insert_result,
    LOCATE('lo', 'Hello World') as locate_result,
    LPAD('Hello', 10, '*') as lpad_result,
    RPAD('Hello', 10, '*') as rpad_result,
    TRIM(BOTH 'x' FROM 'xxxHelloxxx') as trim_result,
    SUBSTRING_INDEX('www.example.com', '.', 2) as substring_index_result;

SELECT 
    ABS(-10) as abs_result,
    CEIL(12.3) as ceil_result,
    FLOOR(12.7) as floor_result,
    MOD(10, 3) as mod_result,
    POWER(2, 3) as power_result,
    ROUND(12.345, 2) as round_result,
    SQRT(16) as sqrt_result,
    RAND() as rand_result,
    SIGN(-5) as sign_result;

SELECT
    CURDATE() as current_date1,
    CURTIME() as current_time1,
    NOW() as now_datetime,
    DATE_ADD('2025-12-12', INTERVAL 1 MONTH) as date_add_result,
    DATE_SUB('2025-12-12', INTERVAL 3 DAY) as date_sub_result,
    DATEDIFF('2025-12-25', '2025-12-12') as datediff_result,
    DATE_FORMAT('2025-12-12 15:30:00', '%W %M %Y') as date_format_result,
    STR_TO_DATE('December 12, 2025', '%M %d, %Y') as str_to_date_result,
    UNIX_TIMESTAMP('2025-12-12 15:30:00') as unix_timestamp_result,
    FROM_UNIXTIME(1757667600) as from_unixtime_result;

SELECT
    STDDEV(salary) as stddev_result,
    VARIANCE(salary) as variance_result,
    BIT_AND(id) as bit_and_result,
    BIT_OR(id) as bit_or_result,
    BIT_XOR(id) as bit_xor_result,
    GROUP_CONCAT(DISTINCT department_id ORDER BY department_id SEPARATOR ',') as group_concat_result
FROM (
    SELECT 1 as id, 5000 as salary, 10 as department_id
    UNION ALL
    SELECT 2, 6000, 10
    UNION ALL
    SELECT 3, 7000, 20
    UNION ALL
    SELECT 4, 8000, 20
    UNION ALL
    SELECT 5, 9000, 30
) sample_data;

SELECT * FROM departments WHERE id = 1 FOR UPDATE;
SELECT * FROM employees WHERE department_id = 1 LOCK IN SHARE MODE;

SELECT
    REGEXP_REPLACE('Hello World', 'World', 'MySQL') as regexp_replace_result,
    REGEXP_SUBSTR('Email: test@example.com', '[a-z]+@[a-z]+\\.[a-z]+') as regexp_substr_result,
    REGEXP_COUNT('Hello Hello Hello', 'Hello') as regexp_count_result;


SELECT @@version as mysql_version;
SELECT @@session.time_zone as session_timezone;
SHOW VARIABLES LIKE 'character_set%';
SHOW STATUS LIKE 'Threads_connected';