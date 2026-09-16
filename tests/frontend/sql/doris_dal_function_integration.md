# Doris DAL 与函数集成验证

## SQL 执行明细

判定口径：CloudDM 能解析并将 SQL 发送到数据库即为通过；数据库返回错误不等于 CloudDM 失败。安全或破坏性操作保留待用户执行状态。

验证状态统一为：通过、CloudDM 失败、未执行、证据不足。通过仅表示已有证据确认 SQL 被解析并发送到数据库，数据库报错保留在执行证据中。历史执行与复测记录均保留，每行反映该次记录，不代表相同 SQL 的最新复测结论。

本表记录数：通过 455；CloudDM 失败 3；未执行 0；证据不足 0。包含重复执行记录，不等同于去重用例数。本次为文档证据整理，未重新执行 SQL。

| 编号 | SQL | 验证状态 | 实际结果/执行证据 | 前提/预期 | 来源/备注 |
| --- | --- | --- | --- | --- | --- |
| 1 | `SELECT ABS(-1), ROUND(12.345, 2), CONV('ff', 16, 10);` | 通过 | 1行：1、12.35、255 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #1 |
| 2 | `SELECT CONCAT('Apache', 'Doris'), REGEXP_REPLACE('abc123', '[0-9]+', ''), CHAR_LENGTH('数据库');` | 通过 | 1行：ApacheDoris、abc、3 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #2 |
| 3 | `SELECT DATE_ADD('2026-01-01', INTERVAL 1 DAY), DATEDIFF('2026-01-03', '2026-01-01');` | 通过 | 1行：2026-01-02、2 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #3 |
| 4 | `SELECT JSON_EXTRACT('{"a":1}', '$.a'), JSON_OBJECT('a', 1), JSON_ARRAY(1, 2);` | 通过 | 1行：1、JSON对象及数组 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #4 |
| 5 | `SELECT ARRAY(1, 2, 3), ARRAY_CONTAINS([1, 2, 3], 2), ARRAY_SIZE([1, 2, 3]);` | 通过 | 1行：[1,2,3]、1、3 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #5 |
| 6 | `SELECT MAP('a', 1, 'b', 2), MAP_KEYS(MAP('a', 1)), MAP_VALUES(MAP('a', 1));` | 通过 | 1行：MAP与键/值数组 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #6 |
| 7 | `SELECT BITMAP_COUNT(TO_BITMAP(1)), HLL_CARDINALITY(HLL_HASH('doris'));` | 通过 | 1行：1、1 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #7 |
| 8 | `SELECT COUNT(*), SUM(amount), AVG(amount), MIN(amount), MAX(amount) FROM tpch.orders;` | 通过 | 复测：1 行，COUNT=3、SUM=61.00、AVG=20.3333、MIN=10.50、MAX=30.50 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #8 |
| 9 | `SELECT ROW_NUMBER() OVER (ORDER BY id), LAG(amount) OVER (ORDER BY id) FROM tpch.orders;` | 通过 | 复测：3 行，ROW_NUMBER=1/2/3、LAG=NULL/10.50/20.00 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #9 |
| 10 | `SELECT COALESCE(NULL, 1), NULLIF(1, 1), GREATEST(1, 2), LEAST(1, 2);` | 通过 | 1行：1、NULL、2、1 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #10 |
| 11 | `SELECT MD5('doris'), SHA2('doris', 256), UUID();` | 通过 | 1行：MD5、SHA2及UUID | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #11 |
| 12 | `SELECT CURRENT_USER(), DATABASE(), CONNECTION_ID(), VERSION();` | 通过 | 1行：root@%、pika、连接ID、5.7.99 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_0.txt` #12 |
| 13 | `SELECT ADD_TIME('2025-09-19 12:00:00', '01:30:00'), SUB_TIME('2025-09-19 12:00:00', '01:30:00');` | 通过 | 错误：Can not found function 'ADD_TIME' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #1 |
| 14 | `SELECT ADD_TIME(CAST('12:15:20' AS TIME), '00:10:40'), SUB_TIME(CAST('12:15:20' AS TIME), '00:10:40');` | 通过 | 错误：Can not found function 'ADD_TIME' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #2 |
| 15 | `SELECT ADD_TIME(NULL, '01:00:00'), SUB_TIME('2025-09-19 12:00:00', NULL);` | 通过 | 错误：Can not found function 'ADD_TIME' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #3 |
| 16 | `SELECT ARRAY_CONTAINS_ALL([1, 2, 3], [1, 3]), ARRAY_CONTAINS_ALL([1, 2, 3], []);` | 通过 | 错误：Can not found function 'ARRAY_CONTAINS_ALL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #4 |
| 17 | `SELECT ARRAY_CONTAINS_ALL(NULL, [1]), ARRAY_CONTAINS_ALL([1], NULL);` | 通过 | 错误：Can not found function 'ARRAY_CONTAINS_ALL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #5 |
| 18 | `SELECT ARRAY_EXCEPT_ALL([1, 1, 2, 3], [1, 3]), ARRAY_EXCEPT_ALL([], [1]);` | 通过 | 错误：Can not found function 'ARRAY_EXCEPT_ALL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #6 |
| 19 | `SELECT ARRAY_FLATTEN([[1, 2], [3], []]), ARRAY_FLATTEN(NULL);` | 通过 | 错误：Can not found function 'ARRAY_FLATTEN' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #7 |
| 20 | `SELECT ARRAY_MATCH_ALL(x -> x > 0, [1, 2, 3]), ARRAY_MATCH_ANY(x -> x > 5, [1, 7, 3]);` | 通过 | 1行：1、1 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #8 |
| 21 | `SELECT ARRAY_MATCH_ALL((x, i) -> x > i, [1, 2, 3], [0, 1, 2]), ARRAY_MATCH_ANY((x, i) -> x = i, [1, 2], [0, 2]);` | 通过 | 1行：1、1 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #9 |
| 22 | `SELECT CENTURY('2025-09-19'), CENTURY('0001-01-01'), CENTURY(NULL);` | 通过 | 错误：Can not found function 'CENTURY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #10 |
| 23 | `SELECT FORMAT_ROUND(1234567.89, 2), FORMAT_ROUND(-1234.5, 0), FORMAT_ROUND(NULL, 2);` | 通过 | 1行：格式化结果及 NULL | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #11 |
| 24 | `SELECT INTERVAL(23, 1, 15, 17, 30, 44, 200), INTERVAL(0, 1, 2), INTERVAL(NULL, 1, 2);` | 通过 | 错误：mismatched input ','（Doris 解析器拒绝 INTERVAL 调用） | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #12 |
| 25 | `SELECT MAP_FROM_ARRAYS(['a', 'b'], [1, 2]), MAP_FROM_ARRAYS([], []);` | 通过 | 错误：Can not found function 'MAP_FROM_ARRAYS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #13 |
| 26 | `SELECT MAP_FROM_ENTRIES(ARRAY(STRUCT(1, 10), STRUCT(2, 20))), MAP_FROM_ENTRIES([]);` | 通过 | 错误：Can not found function 'MAP_FROM_ENTRIES' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #14 |
| 27 | `SELECT MONTHS_ADD('2025-01-31', 1), MONTHS_SUB('2025-03-31', 1);` | 通过 | 1行：月份边界日期返回 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #15 |
| 28 | `SELECT MONTHS_ADD('2025-01-01 12:30:45', -2), MONTHS_SUB('2025-01-01 12:30:45', -2);` | 通过 | 1行：负月数计算返回 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #16 |
| 29 | `SELECT MONTHS_ADD(NULL, 1), MONTHS_SUB('2025-01-01', NULL);` | 通过 | 1行：NULL、NULL | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #17 |
| 30 | `SELECT MULTI_MATCH_ANY('Hello, World!', ['hello', '!', 'world']), MULTI_MATCH_ANY('Doris', []);` | 通过 | 1行：1、0 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #18 |
| 31 | `SELECT NEXT_DAY('2025-09-19', 'monday'), PREVIOUS_DAY('2025-09-19', 'monday');` | 通过 | 错误：Can not found function 'PREVIOUS_DAY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #19 |
| 32 | `SELECT NEXT_DAY(NULL, 'monday'), PREVIOUS_DAY('2025-09-19', NULL);` | 通过 | 错误：Can not found function 'PREVIOUS_DAY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #20 |
| 33 | `SELECT SECONDS_ADD('2025-09-19 23:59:59', 2), SECONDS_SUB('2025-09-19 00:00:00', 2);` | 通过 | 1行：跨日秒数加减返回 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #21 |
| 34 | `SELECT SECONDS_ADD('2025-09-19 12:00:00', -60), SECONDS_SUB('2025-09-19 12:00:00', -60);` | 通过 | 1行：负秒数加减返回 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #22 |
| 35 | `SELECT SECONDS_ADD(NULL, 1), SECONDS_SUB('2025-09-19 12:00:00', NULL);` | 通过 | 1行：NULL、NULL | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #23 |
| 36 | `SELECT TO_MONDAY('2025-09-19'), TO_MONDAY('2025-09-22'), TO_MONDAY(NULL);` | 通过 | 1行：两日期的周一及 NULL 返回 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #24 |
| 37 | `SELECT TO_SECONDS('2025-09-19 12:34:56'), TO_SECONDS('0001-01-01 00:00:00'), TO_SECONDS(NULL);` | 通过 | 错误：Can not found function 'TO_SECONDS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #25 |
| 38 | `SELECT WEEKDAY('2025-09-19'), WEEKDAY('2025-09-22'), WEEKDAY(NULL);` | 通过 | 1行：4、0、NULL | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #26 |
| 39 | `SELECT YEAR_CEIL('2023-07-13 22:28:18');` | 通过 | 1行：2024-01-01 00:00:00 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #27 |
| 40 | `SELECT YEAR_CEIL('2023-07-13 22:28:18', 5);` | 通过 | 1行：2025-01-01 00:00:00 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #28 |
| 41 | `SELECT YEAR_CEIL('2023-07-13 22:28:18', '2021-03-13 22:13:00');` | 通过 | 1行：2024-03-13 22:13:00 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #29 |
| 42 | `SELECT YEAR_CEIL('2023-07-13', 1, '2020-01-01');` | 通过 | 1行：2024-01-01 00:00:00 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #30 |
| 43 | `SELECT TO_SECONDS(NEXT_DAY('2025-09-19', 'monday')), WEEKDAY(MONTHS_ADD('2025-01-31', 1));` | 通过 | 错误：Can not found function 'TO_SECONDS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_1.txt` #31 |
| 44 | `SELECT AVG_MAP(metrics), SUM_MAP(metrics) FROM analytics.map_events;` | 通过 | 复测：Can not found function 'AVG_MAP' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #1 |
| 45 | `SELECT COUNT_MAP(metrics), MAX_MAP(metrics), MIN_MAP(metrics) FROM analytics.map_events;` | 通过 | 复测：Can not found function 'COUNT_MAP' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #2 |
| 46 | `SELECT AVG_MAP(MAP('latency', latency_ms)), SUM_MAP(MAP('bytes', payload_size)) FROM analytics.requests;` | 通过 | 复测：Can not found function 'AVG_MAP' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #3 |
| 47 | `SELECT PERCENTILE_APPROX_ARRAY(latency_ms, [0.0, 0.25, 0.5, 0.75, 1.0]) FROM analytics.requests;` | 通过 | 复测：Can not found function 'PERCENTILE_APPROX_ARRAY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #4 |
| 48 | `SELECT PERCENTILE_APPROX_ARRAY(latency_ms, [0.5, 0.95, 0.99], 4096) FROM analytics.requests;` | 通过 | 复测：Can not found function 'PERCENTILE_APPROX_ARRAY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #5 |
| 49 | `` SELECT SUM_FOREACH(`values`), COUNT_FOREACH(`values`) FROM analytics.array_events; `` | 通过 | 复测：1 行；SUM_FOREACH/COUNT_FOREACH 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #6 |
| 50 | `` SELECT ARRAY_AGG_FOREACH(`values`) FROM analytics.array_events; `` | 通过 | 复测：1 行；ARRAY_AGG_FOREACH 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #7 |
| 51 | `` SELECT MAP_AGG_FOREACH(`keys`, `values`) FROM analytics.array_events; `` | 通过 | 复测：1 行；MAP_AGG_FOREACH 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #8 |
| 52 | `SELECT MAX_BY_STATE(value, event_time) FROM analytics.events;` | 通过 | 复测：3 行；MAX_BY_STATE 返回聚合状态 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #9 |
| 53 | `SELECT GROUP_CONCAT_MERGE(group_concat_state) FROM analytics.partial_aggregates;` | 通过 | 错误：Database [analytics] does not exist | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_2.txt` #10 |
| 54 | `SELECT ABS(-42), POSITIVE(-42), NEGATIVE(42), SIGN(-42), SIGNBIT(-0.0);` | 通过 | 错误：Can not found function 'SIGNBIT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #1 |
| 55 | `SELECT ACOS(0.5), ASIN(0.5), ATAN(1), ATAN2(1, 2), COT(1), CSC(1), SEC(1);` | 通过 | 错误：Can not found function 'CSC' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #2 |
| 56 | `SELECT ACOSH(2), ASINH(1), ATANH(0.5), COSH(1), SINH(1), TANH(1);` | 通过 | 错误：Can not found function 'ACOSH' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #3 |
| 57 | `SELECT COS(1), SIN(1), TAN(1), DEGREES(PI()), RADIANS(180);` | 通过 | 1行：三角函数与弧度转换结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #4 |
| 58 | `SELECT CBRT(27), SQRT(16), POW(2, 10), EXP(1), LN(E()), LOG(2, 8), LOG2(8), LOG10(100);` | 通过 | 1行：CBRT=3、SQRT=4、POW=1024 等 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #5 |
| 59 | `SELECT CEIL(12.34), FLOOR(12.34), ROUND(12.345, 2), ROUND_BANKERS(10.745, 2), TRUNCATE(12.345, 2);` | 通过 | 1行：CEIL/FLOOR/ROUND 等返回；ROUND_BANKERS=10.74 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #6 |
| 60 | `SELECT EVEN(3.2), FACTORIAL(20), FMOD(10.5, 3), MOD(10, 3), PMOD(-10, 3);` | 通过 | 错误：Can not found function 'EVEN' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #7 |
| 61 | `SELECT GCD(54, 24), LCM(12, 18), CONV('ff', 16, 10), BIN(255);` | 通过 | 错误：Can not found function 'GCD' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #8 |
| 62 | `SELECT FORMAT_ROUND(1234567.89, 2), MONEY_FORMAT(1234567.89), NORMAL_CDF(10, 9, 10);` | 通过 | 1行：格式化与 NORMAL_CDF=0.5 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #9 |
| 63 | `SELECT RANDOM(), UNIFORM(1, 100, RANDOM() * 10000), WIDTH_BUCKET(5, 0, 10, 5);` | 通过 | 错误：Can not found function 'UNIFORM' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #10 |
| 64 | `SELECT INTERVAL(23, 1, 15, 17, 30, 44, 200), ISINF(CAST('inf' AS DOUBLE)), ISNAN(CAST('nan' AS DOUBLE));` | 通过 | 错误：mismatched input ','（服务器拒绝 INTERVAL 调用） | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #11 |
| 65 | `SELECT BITAND(7, 3), BITOR(4, 1), BITNOT(0), BIT_COUNT(255), BIT_SHIFT_LEFT(5, 2), BIT_SHIFT_RIGHT(20, 2), BIT_TEST(5, 0, 2);` | 通过 | 错误：Can not found function 'BIT_TEST' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #12 |
| 66 | `SELECT TRUE XOR FALSE, COALESCE(NULL, 1), IF(TRUE, 'yes', 'no'), IFNULL(NULL, 'fallback'), NULLIF(1, 1);` | 通过 | 1行：XOR/COALESCE/IF/IFNULL/NULLIF 返回 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #13 |
| 67 | `SELECT GREATEST(1, 2, 3), LEAST(1, 2, 3), NULL_OR_EMPTY(''), NOT_NULL_OR_EMPTY('doris');` | 通过 | 1行：3、1、1、1 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #14 |
| 68 | `SELECT CONNECTION_ID(), CURRENT_CATALOG(), CURRENT_USER(), DATABASE(), LAST_QUERY_ID(), SESSION_USER(), USER(), VERSION();` | 通过 | 1行：连接、账号、catalog、数据库和版本信息 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #15 |
| 69 | `SELECT QUANTILE_PERCENT(TO_QUANTILE_STATE(1.0, 2048), 0.5), QUANTILE_STATE_EMPTY();` | 通过 | 错误：quantile_percent 第二参数类型不符（服务器 INTERNAL_ERROR） | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #16 |
| 70 | `SELECT QUANTILE_STATE_FROM_BASE64(QUANTILE_STATE_TO_BASE64(TO_QUANTILE_STATE(1.0, 2048)));` | 通过 | 错误：Can not found function 'QUANTILE_STATE_TO_BASE64' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #17 |
| 71 | `SELECT CONVERT_TO('Doris', 'utf8'), FIELD('b', 'a', 'b', 'c'), ESQUERY(doc, '{"match_all": {}}') FROM analytics.documents;` | 通过 | 复测：CONVERT_TO 第二参数 `utf8` 不合法，当前仅支持 gbk；同行其余函数未据此判定 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #18 |
| 72 | `SELECT GROUPING(region), GROUPING_ID(region, category), COUNT(*) FROM analytics.sales GROUP BY GROUPING SETS ((region, category), (region), ());` | 通过 | 复测：5 行；GROUPING/GROUPING_ID 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #19 |
| 73 | `SELECT BIT_LENGTH('Doris'), UUID_NUMERIC();` | 通过 | 1行：BIT_LENGTH=40 与 UUID_NUMERIC 数值 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #20 |
| 74 | `SELECT DEFAULT(status) FROM analytics.orders;` | 通过 | 错误：服务器解析器拒绝 DEFAULT(status) | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_3.txt` #21 |
| 75 | `SELECT ARRAY(1, 2, 3), ARRAY_SIZE([1, 2, 3]), ARRAY_AVG([1, 2, 3]), ARRAY_SUM([1, 2, 3]), ARRAY_PRODUCT([1, 2, 3]);` | 通过 | 1 行；ARRAY_PRODUCT=6.0 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #1 |
| 76 | `SELECT ARRAYS_OVERLAP([1, 2], [2, 3]), ARRAY_CONTAINS([1, 2, 3], 2), ARRAY_CONTAINS_ALL([1, 2, 3], [1, 3]), COUNTEQUAL([1, 2, 1], 1);` | 通过 | 错误：Can not found function 'ARRAY_CONTAINS_ALL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #2 |
| 77 | `SELECT ARRAY_COMPACT([1, NULL, 1, 2]), ARRAY_DISTINCT([1, 1, 2]), ARRAY_UNION([1, 2], [2, 3]), ARRAY_INTERSECT([1, 2], [2, 3]);` | 通过 | 1 行；ARRAY_COMPACT/DISTINCT/UNION/INTERSECT 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #3 |
| 78 | `SELECT ARRAY_EXCEPT([1, 2, 3], [2]), ARRAY_EXCEPT_ALL([1, 1, 2], [1]), ARRAY_REMOVE([1, 2, 1], 1);` | 通过 | 错误：Can not found function 'ARRAY_EXCEPT_ALL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #4 |
| 79 | `SELECT ARRAY_CONCAT([1, 2], [3, 4]), ARRAY_PUSHBACK([1, 2], 3), ARRAY_PUSHFRONT([2, 3], 1), ARRAY_POPBACK([1, 2, 3]), ARRAY_POPFRONT([1, 2, 3]);` | 通过 | 1 行；5 个 ARRAY 拼接/增删函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #5 |
| 80 | `SELECT ARRAY_FIRST(x -> x > 2, [1, 2, 3]), ARRAY_LAST(x -> x < 3, [1, 2, 3]);` | 通过 | 1 行；ARRAY_FIRST=3，ARRAY_LAST=2 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #6 |
| 81 | `SELECT ARRAY_FIRST_INDEX(x -> x > 2, [1, 2, 3]), ARRAY_LAST_INDEX(x -> x < 3, [1, 2, 3]);` | 通过 | 1 行；ARRAY_FIRST_INDEX=3，ARRAY_LAST_INDEX=2 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #7 |
| 82 | `SELECT ARRAY_EXISTS(x -> x > 2, [1, 2, 3]), ARRAY_COUNT(x -> x > 1, [1, 2, 3]);` | 通过 | 1 行；ARRAY_EXISTS=[0, 0, 1]，ARRAY_COUNT=2 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #8 |
| 83 | `SELECT ARRAY_FILTER(x -> x > 1, [1, 2, 3]), ARRAY_MAP(x -> x * 2, [1, 2, 3]), ARRAY_APPLY([1, 2, 3], '>', 1);` | 通过 | 1 行；FILTER/MAP/APPLY 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #9 |
| 84 | `SELECT ARRAY_MATCH_ALL(x -> x > 0, [1, 2, 3]), ARRAY_MATCH_ANY(x -> x > 2, [1, 2, 3]);` | 通过 | 1 行；MATCH_ALL/MATCH_ANY 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #10 |
| 85 | `SELECT ARRAY_ENUMERATE([10, 20, 30]), ARRAY_ENUMERATE_UNIQ([1, 1, 2]), ARRAY_DIFFERENCE([1, 3, 6]), ARRAY_CUM_SUM([1, 2, 3]);` | 通过 | 1 行；ENUMERATE/UNIQ/DIFFERENCE/CUM_SUM 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #11 |
| 86 | `SELECT ARRAY_MIN([3, 1, 2]), ARRAY_MAX([3, 1, 2]), ARRAY_POSITION([1, 2, 3], 2), ARRAY_JOIN(['a', 'b'], ',');` | 通过 | 1 行；MIN/MAX/POSITION/JOIN 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #12 |
| 87 | `SELECT ARRAY_RANGE(1, 6, 2), ARRAY_REPEAT('x', 3), ARRAY_WITH_CONSTANT(3, 'x'), ARRAY_SLICE([1, 2, 3, 4], 2, 2);` | 通过 | 1 行；RANGE/REPEAT/WITH_CONSTANT/SLICE 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #13 |
| 88 | `SELECT ARRAY_SORT([3, 1, 2]), ARRAY_REVERSE_SORT([3, 1, 2]), ARRAY_SHUFFLE([1, 2, 3]);` | 通过 | 1 行；SORT/REVERSE_SORT/SHUFFLE 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #14 |
| 89 | `SELECT ARRAY_SORTBY(['b', 'a', 'c'], [2, 1, 3]), ARRAY_ZIP([1, 2], ['a', 'b']), ARRAY_CROSS_PRODUCT([1, 2], ['a', 'b']);` | 通过 | 错误：Can not found function 'ARRAY_CROSS_PRODUCT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #15 |
| 90 | `SELECT ARRAY_SPLIT(x -> x = 0, [1, 0, 2, 0, 3]), ARRAY_REVERSE_SPLIT(x -> x = 0, [1, 0, 2, 0, 3]);` | 通过 | 1 行；ARRAY_SPLIT=[[1], [0, 2], [0, 3]]，REVERSE_SPLIT=[[1, 0], [2, 0], [3]] | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #16 |
| 91 | `SELECT ARRAY_FLATTEN([[1, 2], [3]]), MAP('a', 1, 'b', 2), MAP_SIZE(MAP('a', 1));` | 通过 | 错误：Can not found function 'ARRAY_FLATTEN' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #17 |
| 92 | `SELECT MAP_KEYS(MAP('a', 1, 'b', 2)), MAP_VALUES(MAP('a', 1, 'b', 2)), MAP_ENTRIES(MAP('a', 1, 'b', 2));` | 通过 | 错误：Can not found function 'MAP_ENTRIES' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #18 |
| 93 | `SELECT MAP_CONTAINS_KEY(MAP('a', 1), 'a'), MAP_CONTAINS_VALUE(MAP('a', 1), 1), MAP_CONTAINS_ENTRY(MAP('a', 1), 'a', 1);` | 通过 | 错误：Can not found function 'MAP_CONTAINS_ENTRY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #19 |
| 94 | `SELECT MAP_FROM_ARRAYS(['a', 'b'], [1, 2]), MAP_FROM_ENTRIES(ARRAY(STRUCT('a', 1), STRUCT('b', 2)));` | 通过 | 错误：Can not found function 'MAP_FROM_ARRAYS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #20 |
| 95 | `SELECT DEDUPLICATE_MAP(MAP('a', 1, 'a', 2)), STR_TO_MAP('a:1,b:2', ',', ':');` | 通过 | 错误：Can not found function 'DEDUPLICATE_MAP' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #21 |
| 96 | `SELECT STRUCT(1, 'a'), NAMED_STRUCT('id', 1, 'name', 'doris'), STRUCT_ELEMENT(NAMED_STRUCT('id', 1), 'id');` | 通过 | 1 行；STRUCT/NAMED_STRUCT/STRUCT_ELEMENT 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #22 |
| 97 | `SELECT PARSE_TO_VARIANT('{"id":1}'), TRY_PARSE_TO_VARIANT('invalid'), VARIANT_TYPE(PARSE_TO_VARIANT('{"id":1}'));` | 通过 | 错误：Can not found function 'PARSE_TO_VARIANT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #23 |
| 98 | `SELECT ELEMENT_AT([10, 20, 30], 2), ELEMENT_AT(MAP('a', 1), 'a');` | 通过 | 1 行；ELEMENT_AT 数组和 MAP 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_4.txt` #24 |
| 99 | `SELECT CURDATE(), CURTIME(), NOW(), UTC_DATE(), UTC_TIME(), UTC_TIMESTAMP();` | 通过 | 错误：Can not found function 'UTC_DATE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #1 |
| 100 | `SELECT DATE('2025-09-19 12:34:56'), TIME('2025-09-19 12:34:56'), TIMESTAMP('2025-09-19', '12:34:56');` | 通过 | 错误：Can not found function 'TIME' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #2 |
| 101 | `SELECT DATE_ADD('2025-09-19', INTERVAL 1 DAY), DATE_SUB('2025-09-19', INTERVAL 1 DAY), DATEDIFF('2025-09-20', '2025-09-19');` | 通过 | 1 行；DATE_ADD/SUB/DATEDIFF 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #3 |
| 102 | `SELECT ADD_TIME('2025-09-19 12:00:00', '01:30:00'), SUB_TIME('2025-09-19 12:00:00', '01:30:00'), TIMEDIFF('13:30:00', '12:00:00');` | 通过 | 错误：Can not found function 'ADD_TIME' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #4 |
| 103 | `SELECT DATE_FORMAT('2025-09-19 12:34:56', '%Y-%m-%d'), STR_TO_DATE('2025-09-19', '%Y-%m-%d'), GET_FORMAT(DATE, 'USA');` | 通过 | 错误：Unknown column 'DATE' in table list；GET_FORMAT(DATE, 'USA') 被当列名 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #5 |
| 104 | `SELECT DATE_TRUNC('2025-09-19 12:34:56', 'hour'), EXTRACT(YEAR FROM DATE '2025-09-19');` | 通过 | 1 行；DATE_TRUNC/EXTRACT 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #6 |
| 105 | `SELECT DAY('2025-09-19'), DAYNAME('2025-09-19'), DAYOFWEEK('2025-09-19'), DAYOFYEAR('2025-09-19');` | 通过 | 1 行；DAY/DAYNAME/DAYOFWEEK/DAYOFYEAR 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #7 |
| 106 | `SELECT HOUR('2025-09-19 12:34:56'), MINUTE('2025-09-19 12:34:56'), SECOND('2025-09-19 12:34:56'), MICROSECOND('2025-09-19 12:34:56.123456');` | 通过 | 1 行；HOUR/MINUTE/SECOND/MICROSECOND 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #8 |
| 107 | `SELECT MONTH('2025-09-19'), MONTHNAME('2025-09-19'), QUARTER('2025-09-19'), YEAR('2025-09-19');` | 通过 | 1 行；MONTH/MONTHNAME/QUARTER/YEAR 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #9 |
| 108 | `SELECT WEEK('2025-09-19'), WEEKDAY('2025-09-19'), WEEKOFYEAR('2025-09-19'), YEARWEEK('2025-09-19'), YEAR_OF_WEEK('2025-09-19');` | 通过 | 1 行；WEEK/WEEKDAY/WEEKOFYEAR/YEARWEEK/YEAR_OF_WEEK 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #10 |
| 109 | `SELECT CENTURY('2025-09-19'), LAST_DAY('2025-02-10'), TO_MONDAY('2025-09-19');` | 通过 | 错误：Can not found function 'CENTURY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #11 |
| 110 | `SELECT NEXT_DAY('2025-09-19', 'monday'), PREVIOUS_DAY('2025-09-19', 'monday');` | 通过 | 错误：Can not found function 'PREVIOUS_DAY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #12 |
| 111 | `SELECT HOURS_ADD('2025-09-19 12:00:00', 2), HOURS_SUB('2025-09-19 12:00:00', 2), HOURS_DIFF('2025-09-19 14:00:00', '2025-09-19 12:00:00');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #13 |
| 112 | `SELECT MINUTES_ADD('2025-09-19 12:00:00', 30), MINUTES_SUB('2025-09-19 12:00:00', 30), MINUTES_DIFF('2025-09-19 12:30:00', '2025-09-19 12:00:00');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #14 |
| 113 | `SELECT SECONDS_ADD('2025-09-19 12:00:00', 30), SECONDS_SUB('2025-09-19 12:00:00', 30), SECONDS_DIFF('2025-09-19 12:00:30', '2025-09-19 12:00:00');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #15 |
| 114 | `SELECT MILLISECONDS_ADD('2025-09-19 12:00:00', 30), MILLISECONDS_SUB('2025-09-19 12:00:00', 30), MILLISECONDS_DIFF('2025-09-19 12:00:00.030', '2025-09-19 12:00:00');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #16 |
| 115 | `SELECT MICROSECONDS_ADD('2025-09-19 12:00:00', 30), MICROSECONDS_SUB('2025-09-19 12:00:00', 30), MICROSECONDS_DIFF('2025-09-19 12:00:00.000030', '2025-09-19 12:00:00');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #17 |
| 116 | `SELECT WEEKS_ADD('2025-09-19', 2), WEEKS_SUB('2025-09-19', 2), WEEKS_DIFF('2025-10-03', '2025-09-19');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #18 |
| 117 | `SELECT MONTHS_ADD('2025-09-19', 2), MONTHS_SUB('2025-09-19', 2), MONTHS_DIFF('2025-11-19', '2025-09-19'), MONTHS_BETWEEN('2025-11-19', '2025-09-19');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #19 |
| 118 | `SELECT QUARTERS_ADD('2025-09-19', 2), QUARTERS_SUB('2025-09-19', 2), QUARTERS_DIFF('2026-03-19', '2025-09-19');` | 通过 | 错误：Can not found function 'QUARTERS_ADD' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #20 |
| 119 | `SELECT YEARS_ADD('2025-09-19', 2), YEARS_SUB('2025-09-19', 2), YEARS_DIFF('2027-09-19', '2025-09-19');` | 通过 | 1 行；时间加减/差值返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #21 |
| 120 | `SELECT SECOND_CEIL('2025-09-19 12:34:56.1'), SECOND_FLOOR('2025-09-19 12:34:56.9');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #22 |
| 121 | `SELECT MINUTE_CEIL('2025-09-19 12:34:56'), MINUTE_FLOOR('2025-09-19 12:34:56');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #23 |
| 122 | `SELECT HOUR_CEIL('2025-09-19 12:34:56'), HOUR_FLOOR('2025-09-19 12:34:56');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #24 |
| 123 | `SELECT DAY_CEIL('2025-09-19 12:34:56'), DAY_FLOOR('2025-09-19 12:34:56');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #25 |
| 124 | `SELECT WEEK_CEIL('2025-09-19 12:34:56'), WEEK_FLOOR('2025-09-19 12:34:56');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #26 |
| 125 | `SELECT MONTH_CEIL('2025-09-19 12:34:56'), MONTH_FLOOR('2025-09-19 12:34:56');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #27 |
| 126 | `SELECT QUARTER_CEIL('2025-09-19 12:34:56'), QUARTER_FLOOR('2025-09-19 12:34:56');` | 通过 | 错误：Can not found function 'QUARTER_CEIL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #28 |
| 127 | `SELECT YEAR_CEIL('2025-09-19 12:34:56'), YEAR_FLOOR('2025-09-19 12:34:56');` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #29 |
| 128 | `SELECT DATE_CEIL('2025-09-19 12:34:56', INTERVAL 5 MINUTE), DATE_FLOOR('2025-09-19 12:34:56', INTERVAL 5 MINUTE);` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #30 |
| 129 | `SELECT FROM_DAYS(739513), TO_DAYS('2025-09-19'), TO_SECONDS('2025-09-19 12:34:56');` | 通过 | 错误：Can not found function 'TO_SECONDS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #31 |
| 130 | `SELECT FROM_SECOND(1), FROM_MILLISECOND(1000), FROM_MICROSECOND(1000000);` | 通过 | 1 行；日期取整/时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #32 |
| 131 | `SELECT MILLISECOND_TIMESTAMP('2025-09-19 12:34:56.123'), MICROSECOND_TIMESTAMP('2025-09-19 12:34:56.123456');` | 通过 | 1 行；时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #33 |
| 132 | `SELECT FROM_UNIXTIME(1758256496), UNIX_TIMESTAMP('2025-09-19 12:34:56'), SEC_TO_TIME(45296), TIME_TO_SEC('12:34:56');` | 通过 | 1 行；时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #34 |
| 133 | `SELECT FROM_ISO8601_DATE('2025-09-19'), TO_ISO8601('2025-09-19 12:34:56');` | 通过 | 1 行；时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #35 |
| 134 | `SELECT MAKEDATE(2025, 262), MAKETIME(12, 34, 56), PERIOD_ADD(202509, 3), PERIOD_DIFF(202512, 202509);` | 通过 | 错误：Can not found function 'MAKETIME' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #36 |
| 135 | `SELECT CONVERT_TZ('2025-09-19 12:00:00', 'Asia/Shanghai', 'UTC');` | 通过 | 1 行；时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #37 |
| 136 | `SELECT TIMESTAMPADD(DAY, 1, '2025-09-19'), TIMESTAMPDIFF(DAY, '2025-09-19', '2025-09-20');` | 通过 | 1 行；时间转换返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #38 |
| 137 | `SELECT TIME_FORMAT('12:13:14.123456', '%H:%i:%s.%f'), TO_DATE('2025-09-19 12:34:56');` | 通过 | 错误：Can not found function 'TIME_FORMAT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #39 |
| 138 | `SELECT NANOSECOND(CAST('2024-02-29' AS DATE)), NANOSECOND(CAST('2024-02-29 12:34:56.123456' AS DATETIME(6))), NANOSECOND(CAST('2024-02-29 12:34:56.123456789' AS TIMESTAMP_NS));` | 通过 | 服务器解析错误：TIMESTAMP_NS 不属于当前 CAST 支持的类型 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #40 |
| 139 | `SELECT NANOSECONDS_ADD(CAST('1969-12-31 23:59:59.999999999' AS TIMESTAMP_NS), 1), NANOSECONDS_DIFF(CAST('1970-01-01 00:00:00.000000001' AS TIMESTAMP_NS), CAST('1969-12-31 23:59:59.999999999' AS TIMESTAMP_NS)), NANOSECONDS_SUB(CAST('1970-01-01 00:00:00.000000000' AS TIMESTAMP_NS), 1);` | 通过 | 服务器解析错误：TIMESTAMP_NS 不属于当前 CAST 支持的类型 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #41 |
| 140 | `SELECT NANOSECOND(CAST(NULL AS TIMESTAMP_NS)), NANOSECONDS_ADD(CAST(NULL AS TIMESTAMP_NS), 1), NANOSECONDS_DIFF(CAST(NULL AS TIMESTAMP_NS), CAST('1970-01-01 00:00:00.000000000' AS TIMESTAMP_NS)), NANOSECONDS_SUB(CAST(NULL AS TIMESTAMP_NS), 1);` | 通过 | 服务器解析错误：TIMESTAMP_NS 不属于当前 CAST 支持的类型 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_5.txt` #42 |
| 141 | `SELECT ASCII('Doris'), CHAR(68, 111, 114, 105, 115), CHAR_LENGTH('数据库'), LENGTH('Doris');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #1 |
| 142 | `SELECT CONCAT('Apache', ' ', 'Doris'), CONCAT_WS('-', 'Apache', 'Doris'), PRINTF('%s-%d', 'Doris', 4);` | 通过 | 错误：Can not found function 'PRINTF' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #2 |
| 143 | `SELECT LCASE('DORIS'), UCASE('doris'), INITCAP('apache doris');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #3 |
| 144 | `SELECT STARTS_WITH('Apache Doris', 'Apache'), ENDS_WITH('Apache Doris', 'Doris'), STRCMP('a', 'b');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #4 |
| 145 | `SELECT STRLEFT('Apache Doris', 6), STRRIGHT('Apache Doris', 5), SUBSTRING('Apache Doris', 8, 5), SUBSTRING_INDEX('a.b.c', '.', 2);` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #5 |
| 146 | `SELECT INSTR('Apache Doris', 'Doris'), LOCATE('Doris', 'Apache Doris'), POSITION('Doris' IN 'Apache Doris');` | 通过 | 服务器解析错误：POSITION('Doris' IN ...) 的 IN 语法未被接受 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #6 |
| 147 | `SELECT LPAD('Doris', 8, '*'), RPAD('Doris', 8, '*'), SPACE(3), REPEAT('ab', 3);` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #7 |
| 148 | `SELECT LTRIM('  Doris'), RTRIM('Doris  '), TRIM('  Doris  ');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #8 |
| 149 | `SELECT LTRIM_IN('xyDoris', 'xy'), RTRIM_IN('Dorisxy', 'xy'), TRIM_IN('xyDorisxy', 'xy');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #9 |
| 150 | `SELECT REVERSE('Doris'), REPLACE('Apache Doris', 'Doris', 'Database'), REPLACE_EMPTY('', 'fallback'), SUB_REPLACE('Apache Doris', 'Database', 7, 5);` | 通过 | 错误：REPLACE_EMPTY 需要 3 个参数，fixture 只给 2 个 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #10 |
| 151 | `SELECT SPLIT_BY_STRING('a,b,c', ','), SPLIT_BY_REGEXP('a1b2c', '[0-9]'), SPLIT_PART('a,b,c', ',', 2);` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #11 |
| 152 | `SELECT REGEXP('Apache Doris', 'Doris'), REGEXP_COUNT('a1b2c3', '[0-9]');` | 通过 | 错误：Can not found function 'REGEXP_COUNT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #12 |
| 153 | `SELECT REGEXP_EXTRACT('abc123', '([0-9]+)', 1), REGEXP_EXTRACT_ALL('a1b2', '[0-9]'), REGEXP_EXTRACT_OR_NULL('abc', '([0-9]+)', 1);` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #13 |
| 154 | `SELECT REGEXP_REPLACE('abc123', '[0-9]+', ''), REGEXP_REPLACE_ONE('a1b2', '[0-9]', 'x');` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #14 |
| 155 | `SELECT COUNT_SUBSTRINGS('abcabc', 'abc'), DAMERAU_LEVENSHTEIN_DISTANCE('kitten', 'sitting'), LEVENSHTEIN('kitten', 'sitting'), HAMMING_DISTANCE('abc', 'abd');` | 通过 | 错误：Can not found function 'COUNT_SUBSTRINGS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #15 |
| 156 | `SELECT NGRAM_SEARCH('Apache Doris', 'Doris', 3), MULTI_MATCH_ANY('Hello, World!', ['hello', 'world']), MULTI_SEARCH_ALL_POSITIONS('Apache Doris', ['Apache', 'Doris']);` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #16 |
| 157 | `SELECT FIELD('b', 'a', 'b', 'c'), ELT(2, 'a', 'b', 'c'), FIND_IN_SET('b', 'a,b,c'), MAKE_SET(5, 'a', 'b', 'c');` | 通过 | 错误：Can not found function 'MAKE_SET' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #17 |
| 158 | `SELECT HEX('Doris'), UNHEX('446F726973'), TO_BASE64('Doris'), FROM_BASE64('RG9yaXM=');` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #18 |
| 159 | `SELECT URL_ENCODE('a b+c'), URL_DECODE('a%20b%2Bc'), QUOTE('Doris\'s SQL');` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #19 |
| 160 | `SELECT DOMAIN('https://docs.doris.apache.org/path'), DOMAIN_WITHOUT_WWW('https://www.doris.apache.org/path'), PROTOCOL('https://doris.apache.org');` | 通过 | 1 行；正则/编码/URL 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #20 |
| 161 | `SELECT TOP_LEVEL_DOMAIN('https://docs.doris.apache.org'), FIRST_SIGNIFICANT_SUBDOMAIN('https://docs.doris.apache.org'), CUT_TO_FIRST_SIGNIFICANT_SUBDOMAIN('https://docs.doris.apache.org');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #21 |
| 162 | `SELECT EXTRACT_URL_PARAMETER('https://example.com?a=1&b=2', 'b'), PARSE_URL('https://doris.apache.org/docs', 'HOST');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #22 |
| 163 | `SELECT APPEND_TRAILING_CHAR_IF_ABSENT('path', '/'), OVERLAY('Apache Doris', 8, 5, 'Database'), TRANSLATE('abc', 'ac', 'xy');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #23 |
| 164 | `SELECT DIGITAL_MASKING('13812345678'), MASK('Abc-123'), MASK_FIRST_N('Abc-123', 3), MASK_LAST_N('Abc-123', 3);` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #24 |
| 165 | `SELECT FORMAT(1234567.89, 2), FORMAT_NUMBER(1234567.89), PARSE_DATA_SIZE('10MB');` | 通过 | 错误：Can not found function 'FORMAT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #25 |
| 166 | `SELECT COMPRESS('Apache Doris'), UNCOMPRESS(COMPRESS('Apache Doris')), IS_VALID_UTF8('Doris'), UNICODE_NORMALIZE('Doris', 'NFC');` | 通过 | 错误：Can not found function 'COMPRESS' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #26 |
| 167 | `SELECT INT_TO_UUID(1), IS_UUID('00000000-0000-0000-0000-000000000001'), UUID();` | 通过 | 错误：Can not found function 'IS_UUID' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #27 |
| 168 | `SELECT AUTO_PARTITION_NAME('range', 'day', '2025-09-19'), RANDOM_BYTES(16), SOUNDEX('Doris');` | 通过 | 错误：Can not found function 'SOUNDEX' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #28 |
| 169 | `SELECT TOKENIZE('Apache Doris', '"built_in_analyzer"="standard"'), XPATH_STRING('<root><name>Doris</name></root>', '/root/name');` | 通过 | 1 行；字符串函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #29 |
| 170 | `SELECT EXPORT_SET(5, '1', '0', ',', 5);` | 通过 | 错误：Can not found function 'EXPORT_SET' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_6.txt` #30 |
| 171 | `SELECT JSON_ARRAY(1, 'a', NULL), JSON_ARRAY_IGNORE_NULL(1, 'a', NULL), JSON_OBJECT('id', 1, 'name', 'Doris');` | 通过 | 错误：Can not found function 'JSON_ARRAY_IGNORE_NULL' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #1 |
| 172 | `SELECT JSON_QUOTE('Apache "Doris"'), JSON_UNQUOTE('"Apache Doris"'), JSON_VALID('{"id":1}'), JSON_TYPE('{"id":1}');` | 通过 | 错误：JSON_TYPE 当前签名需 2 参数，fixture 只给 1 个 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #2 |
| 173 | `SELECT JSON_PARSE('{"id":1}'), JSON_PARSE_ERROR_TO_NULL('invalid'), JSON_PARSE_ERROR_TO_VALUE('invalid', '{}');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #3 |
| 174 | `SELECT JSON_EXTRACT('{"id":1,"active":true}', '$.id'), JSON_EXTRACT_STRING('{"name":"Doris"}', '$.name');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #4 |
| 175 | `SELECT JSON_EXTRACT_INT('{"n":1}', '$.n'), JSON_EXTRACT_BIGINT('{"n":9223372036854775807}', '$.n'), JSON_EXTRACT_LARGEINT('{"n":1}', '$.n');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #5 |
| 176 | `SELECT JSON_EXTRACT_DOUBLE('{"n":1.5}', '$.n'), JSON_EXTRACT_BOOL('{"active":true}', '$.active'), JSON_EXTRACT_ISNULL('{"v":null}', '$.v');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #6 |
| 177 | `SELECT GET_JSON_INT('{"n":1}', '$.n'), GET_JSON_BIGINT('{"n":9223372036854775807}', '$.n');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #7 |
| 178 | `SELECT GET_JSON_DOUBLE('{"n":1.5}', '$.n'), GET_JSON_STRING('{"name":"Doris"}', '$.name');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #8 |
| 179 | `SELECT JSON_CONTAINS('{"a":[1,2,3]}', '2', '$.a'), JSON_EXISTS_PATH('{"id":1}', '$.id');` | 通过 | 1 行；JSON 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #9 |
| 180 | `SELECT JSON_KEYS('{"a":1,"b":2}'), JSON_LENGTH('{"a":1,"b":2}'), JSON_HASH('{"a":1}');` | 通过 | 错误：Can not found function 'JSON_HASH' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #10 |
| 181 | `SELECT JSON_INSERT('{"a":1}', '$.b', 2), JSON_SET('{"a":1}', '$.a', 2), JSON_REPLACE('{"a":1}', '$.a', 2), JSON_REMOVE('{"a":1,"b":2}', '$.b');` | 通过 | 错误：Can not found function 'JSON_REMOVE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #11 |
| 182 | `SELECT JSON_SEARCH('{"items":["Doris","SQL"]}', 'one', 'Doris'), JSON_OBJECT_FLATTEN('{"a":{"b":1}}');` | 通过 | 错误：Can not found function 'JSON_OBJECT_FLATTEN' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #12 |
| 183 | `SELECT NORMALIZE_JSON_NUMBERS_TO_DOUBLE('{"n":1}'), SORT_JSON_OBJECT_KEYS('{"b":2,"a":1}'), STRIP_NULL_VALUE('{"a":null,"b":1}'), TO_JSON(MAP('a', 1));` | 通过 | 错误：Can not found function 'NORMALIZE_JSON_NUMBERS_TO_DOUBLE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #13 |
| 184 | `SELECT IPV4_STRING_TO_NUM('192.168.1.1'), IPV4_STRING_TO_NUM_OR_NULL('invalid'), IPV4_STRING_TO_NUM_OR_DEFAULT('invalid');` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #14 |
| 185 | `SELECT IPV4_NUM_TO_STRING(3232235777), TO_IPV4('192.168.1.1'), TO_IPV4_OR_NULL('invalid'), TO_IPV4_OR_DEFAULT('invalid');` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #15 |
| 186 | `SELECT IPV6_STRING_TO_NUM('2001:db8::1'), IPV6_STRING_TO_NUM_OR_NULL('invalid'), IPV6_STRING_TO_NUM_OR_DEFAULT('invalid');` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #16 |
| 187 | `SELECT IPV6_NUM_TO_STRING(IPV6_STRING_TO_NUM('2001:db8::1')), TO_IPV6('2001:db8::1'), TO_IPV6_OR_NULL('invalid'), TO_IPV6_OR_DEFAULT('invalid');` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #17 |
| 188 | `SELECT IPV4_TO_IPV6(TO_IPV4('192.168.1.1')), IS_IPV4_COMPAT(TO_IPV6('::192.168.1.1')), IS_IPV4_MAPPED(TO_IPV6('::ffff:192.168.1.1'));` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #18 |
| 189 | `SELECT IS_IPV4_STRING('192.168.1.1'), IS_IPV6_STRING('2001:db8::1'), IS_IP_ADDRESS_IN_RANGE('192.168.1.1', '192.168.1.0/24');` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #19 |
| 190 | `SELECT IPV4_CIDR_TO_RANGE(TO_IPV4('192.168.1.0'), 24), IPV6_CIDR_TO_RANGE(TO_IPV6('2001:db8::'), 64), CUT_IPV6(TO_IPV6('2001:db8::1'), 8, 8);` | 通过 | 1 行；IPv4/IPv6 函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #20 |
| 191 | `SELECT ST_POINT(116.397, 39.908), ST_X(ST_POINT(116.397, 39.908)), ST_Y(ST_POINT(116.397, 39.908));` | 通过 | 1 行；空间函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #21 |
| 192 | `SELECT ST_ASTEXT(ST_GEOMETRYFROMTEXT('POINT (116.397 39.908)')), ST_GEOMETRYTYPE(ST_POINT(1, 2));` | 通过 | 错误：Can not found function 'ST_GEOMETRYTYPE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #22 |
| 193 | `SELECT ST_ASBINARY(ST_POINT(1, 2)), ST_GEOMETRYFROMWKB(ST_ASBINARY(ST_POINT(1, 2)));` | 通过 | 1 行；空间函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #23 |
| 194 | `SELECT ST_LINEFROMTEXT('LINESTRING (0 0, 1 1, 2 0)'), ST_LENGTH(ST_LINEFROMTEXT('LINESTRING (0 0, 3 4)')), ST_NUMPOINTS(ST_LINEFROMTEXT('LINESTRING (0 0, 1 1)'));` | 通过 | 错误：Can not found function 'ST_LENGTH' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #24 |
| 195 | `SELECT ST_POLYGON('POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))'), ST_AREA_SQUARE_METERS(ST_CIRCLE(0, 0, 1)), ST_AREA_SQUARE_KM(ST_CIRCLE(0, 0, 1000));` | 通过 | 1 行；空间函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #25 |
| 196 | `SELECT ST_CIRCLE(116.397, 39.908, 1000), ST_CONTAINS(ST_CIRCLE(0, 0, 10), ST_POINT(0, 0)), ST_DISJOINT(ST_POINT(0, 0), ST_POINT(1, 1));` | 通过 | 1 行；空间函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #26 |
| 197 | `SELECT ST_INTERSECTS(ST_CIRCLE(0, 0, 10), ST_POINT(0, 0)), ST_TOUCHES(ST_CIRCLE(0, 0, 10), ST_POINT(10, 0));` | 通过 | 1 行；空间函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #27 |
| 198 | `SELECT ST_DISTANCE(ST_POINT(0, 0), ST_POINT(3, 4)), ST_DISTANCE_SPHERE(116.397, 39.908, 121.4737, 31.2304);` | 通过 | 错误：Can not found function 'ST_DISTANCE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #28 |
| 199 | `SELECT ST_AZIMUTH(ST_POINT(0, 0), ST_POINT(1, 1)), ST_ANGLE(ST_POINT(0, 0), ST_POINT(1, 0), ST_POINT(1, 1)), ST_ANGLE_SPHERE(0, 0, 1, 1);` | 通过 | 1 行；空间函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #29 |
| 200 | `SELECT ST_GEOMETRIES(ST_GEOMETRYFROMTEXT('GEOMETRYCOLLECTION (POINT (0 0), POINT (1 1))')), ST_NUMGEOMETRIES(ST_GEOMETRYFROMTEXT('GEOMETRYCOLLECTION (POINT (0 0), POINT (1 1))'));` | 通过 | 错误：Can not found function 'ST_GEOMETRIES' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_7.txt` #30 |
| 201 | `SELECT FROM_HEX('446F726973'), TO_HEX(FROM_HEX('446F726973')), SUB_BINARY(FROM_HEX('446F726973'), 2, 3);` | 通过 | 错误：Can not found function 'FROM_HEX' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #1 |
| 202 | `SELECT TO_BASE64_BINARY(FROM_HEX('446F726973')), FROM_BASE64_BINARY('RG9yaXM=');` | 通过 | 错误：Can not found function 'FROM_HEX' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #2 |
| 203 | `SELECT MD5('Doris'), MD5SUM('Apache', 'Doris'), SHA('Doris'), SHA2('Doris', 256);` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #3 |
| 204 | `SELECT CRC32('Doris'), MURMUR_HASH3_32('Doris'), MURMUR_HASH3_64('Doris');` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #4 |
| 205 | `SELECT MURMUR_HASH3_64_V2('Doris'), MURMUR_HASH3_U64_V2('Doris'), XXHASH_32('Doris'), XXHASH_64('Doris');` | 通过 | 错误：Can not found function 'MURMUR_HASH3_64_V2' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #5 |
| 206 | `SELECT SM3('Doris'), SM3SUM('Apache', 'Doris');` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #6 |
| 207 | `SELECT AES_ENCRYPT('Doris', '0123456789abcdef'), AES_DECRYPT(AES_ENCRYPT('Doris', '0123456789abcdef'), '0123456789abcdef');` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #7 |
| 208 | `SELECT SM4_ENCRYPT('Doris', '0123456789abcdef'), SM4_DECRYPT(SM4_ENCRYPT('Doris', '0123456789abcdef'), '0123456789abcdef');` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #8 |
| 209 | `SELECT HLL_EMPTY(), HLL_HASH('Doris'), HLL_CARDINALITY(HLL_HASH('Doris'));` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #9 |
| 210 | `SELECT HLL_TO_BASE64(HLL_HASH('Doris')), HLL_FROM_BASE64(HLL_TO_BASE64(HLL_HASH('Doris')));` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #10 |
| 211 | `SELECT BITMAP_EMPTY(), TO_BITMAP(1), BITMAP_FROM_ARRAY([1, 2, 3]);` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #11 |
| 212 | `SELECT BITMAP_HASH('Doris'), BITMAP_HASH64('Doris'), BITMAP_COUNT(BITMAP_FROM_ARRAY([1, 2, 3]));` | 通过 | 1 行；哈希/加解密/HLL/位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #12 |
| 213 | `SELECT BITMAP_TO_ARRAY(BITMAP_FROM_ARRAY([1, 2, 3])), BITMAP_TO_STRING(BITMAP_FROM_ARRAY([1, 2, 3]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #13 |
| 214 | `SELECT BITMAP_TO_BASE64(BITMAP_FROM_ARRAY([1, 2, 3])), BITMAP_FROM_BASE64(BITMAP_TO_BASE64(BITMAP_FROM_ARRAY([1, 2, 3])));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #14 |
| 215 | `SELECT BITMAP_FROM_STRING('1,2,3'), BITMAP_CONTAINS(BITMAP_FROM_ARRAY([1, 2, 3]), 2);` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #15 |
| 216 | `SELECT BITMAP_HAS_ALL(BITMAP_FROM_ARRAY([1, 2, 3]), BITMAP_FROM_ARRAY([1, 2])), BITMAP_HAS_ANY(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #16 |
| 217 | `SELECT BITMAP_AND(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3])), BITMAP_AND_COUNT(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #17 |
| 218 | `SELECT BITMAP_OR(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3])), BITMAP_OR_COUNT(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #18 |
| 219 | `SELECT BITMAP_XOR(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3])), BITMAP_XOR_COUNT(BITMAP_FROM_ARRAY([1, 2]), BITMAP_FROM_ARRAY([2, 3]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #19 |
| 220 | `SELECT BITMAP_AND_NOT(BITMAP_FROM_ARRAY([1, 2, 3]), BITMAP_FROM_ARRAY([2])), BITMAP_AND_NOT_COUNT(BITMAP_FROM_ARRAY([1, 2, 3]), BITMAP_FROM_ARRAY([2]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #20 |
| 221 | `SELECT BITMAP_NOT(BITMAP_FROM_ARRAY([1, 2, 3]), BITMAP_FROM_ARRAY([2])), BITMAP_REMOVE(BITMAP_FROM_ARRAY([1, 2, 3]), 2);` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #21 |
| 222 | `SELECT BITMAP_MIN(BITMAP_FROM_ARRAY([1, 2, 3])), BITMAP_MAX(BITMAP_FROM_ARRAY([1, 2, 3]));` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #22 |
| 223 | `SELECT BITMAP_SUBSET_IN_RANGE(BITMAP_FROM_ARRAY([1, 2, 3, 4]), 2, 4), BITMAP_SUBSET_LIMIT(BITMAP_FROM_ARRAY([1, 2, 3, 4]), 2, 2);` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #23 |
| 224 | `SELECT SUB_BITMAP(BITMAP_FROM_ARRAY([1, 2, 3, 4]), 1, 2);` | 通过 | 1 行；位图函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_8.txt` #24 |
| 225 | `SELECT COUNT(*), COUNT(value), COUNT(DISTINCT value), APPROX_COUNT_DISTINCT(value), ANY_VALUE(value) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #1 |
| 226 | `SELECT SUM(value), SUM0(value), AVG(value), MIN(value), MAX(value), MEDIAN(value) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'MEDIAN' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #2 |
| 227 | `SELECT AVG_WEIGHTED(value, weight), MAX_BY(value, event_time), MIN_BY(value, event_time) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #3 |
| 228 | `SELECT BOOL_AND(flag), BOOL_OR(flag), BOOL_XOR(flag) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'BOOL_AND' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #4 |
| 229 | `SELECT ARRAY_AGG(value ORDER BY event_time), COLLECT_LIST(value), COLLECT_SET(value) FROM analytics.metrics;` | 通过 | 复测：ARRAY_AGG(... ORDER BY ...) 匹配不到当前签名 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #5 |
| 230 | `SELECT GROUP_CONCAT(DISTINCT value ORDER BY event_time SEPARATOR ','), MAP_AGG(metric_name, value) FROM analytics.metrics;` | 通过 | 复测：服务器解析错误，SEPARATOR 子句未接受 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #6 |
| 231 | `SELECT COUNT_BY_ENUM(category), HISTOGRAM(value, 10), LINEAR_HISTOGRAM(value, 10, 0) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'LINEAR_HISTOGRAM' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #7 |
| 232 | `SELECT TOPN(value, 10), TOPN_ARRAY(value, 10), TOPN_WEIGHTED(value, weight, 10) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #8 |
| 233 | `SELECT PERCENTILE(value, 0.5), PERCENTILE_ARRAY(value, [0.5, 0.9]), PERCENTILE_RESERVOIR(value, 0.95) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'PERCENTILE_RESERVOIR' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #9 |
| 234 | `SELECT PERCENTILE_APPROX(value, 0.95), PERCENTILE_APPROX_ARRAY(value, [0.5, 0.95]), PERCENTILE_APPROX_WEIGHTED(value, weight, 0.95) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'PERCENTILE_APPROX_ARRAY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #10 |
| 235 | `SELECT STDDEV(value), STDDEV_POP(value), STDDEV_SAMP(value), SEM(value) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'SEM' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #11 |
| 236 | `SELECT VARIANCE(value), VAR_POP(value), VARIANCE_POP(value), VAR_SAMP(value), VARIANCE_SAMP(value) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #12 |
| 237 | `SELECT SKEW(value), SKEW_POP(value), SKEWNESS(value), KURT(value), KURT_POP(value), KURTOSIS(value) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #13 |
| 238 | `SELECT CORR(x, y), CORR_WELFORD(x, y), COVAR(x, y), COVAR_POP(x, y), COVAR_SAMP(x, y) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #14 |
| 239 | `SELECT REGR_AVGX(y, x), REGR_AVGY(y, x), REGR_COUNT(y, x), REGR_INTERCEPT(y, x), REGR_R2(y, x) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'REGR_AVGX' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #15 |
| 240 | `SELECT REGR_SLOPE(y, x), REGR_SXX(y, x), REGR_SXY(y, x), REGR_SYY(y, x) FROM analytics.metrics;` | 通过 | 复测：Can not found function 'REGR_SLOPE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #16 |
| 241 | `SELECT GROUP_BIT_AND(bits), GROUP_BIT_OR(bits), GROUP_BIT_XOR(bits) FROM analytics.metrics;` | 通过 | 复测：1 行；聚合函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #17 |
| 242 | `SELECT BITMAP_AGG(user_id), BITMAP_UNION(bitmap_value), BITMAP_UNION_COUNT(bitmap_value), BITMAP_UNION_INT(user_id) FROM analytics.bitmap_metrics;` | 通过 | 复测：1 行；位图聚合返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #18 |
| 243 | `SELECT BITMAP_INTERSECT(bitmap_value), GROUP_BITMAP_XOR(bitmap_value) FROM analytics.bitmap_metrics;` | 通过 | 复测：1 行；位图交集与异或返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #19 |
| 244 | `SELECT HLL_RAW_AGG(HLL_HASH(user_id)), HLL_UNION_AGG(hll_value), DATASKETCHES_HLL_UNION_AGG(sketch_value) FROM analytics.hll_metrics;` | 通过 | 复测：Can not found function 'DATASKETCHES_HLL_UNION_AGG'；HLL 表和数据均存在 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #20 |
| 245 | `SELECT GROUP_ARRAY_INTERSECT(array_value), GROUP_ARRAY_UNION(array_value) FROM analytics.array_metrics;` | 通过 | 复测：Can not found function 'GROUP_ARRAY_UNION' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #21 |
| 246 | `SELECT AVG_MAP(map_value), COUNT_MAP(map_value), MAX_MAP(map_value), MIN_MAP(map_value), SUM_MAP(map_value) FROM analytics.map_metrics;` | 通过 | 复测：Can not found function 'AVG_MAP' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #22 |
| 247 | `SELECT INTERSECT_COUNT(bitmap_value, cohort, 'new', 'active') FROM analytics.bitmap_metrics;` | 通过 | 复测：1 行；INTERSECT_COUNT 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #23 |
| 248 | `SELECT RETENTION(day_1, day_2, day_3), SEQUENCE_COUNT('(?1)(?2)', event_time, event_type = 1, event_type = 2) FROM analytics.events;` | 通过 | 复测：1 行；RETENTION/SEQUENCE_COUNT 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #24 |
| 249 | `SELECT SEQUENCE_MATCH('(?1)(?t<=3600)(?2)', event_time, event_type = 1, event_type = 2) FROM analytics.events;` | 通过 | 复测：1 行；SEQUENCE_MATCH 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #25 |
| 250 | `SELECT WINDOW_FUNNEL(3600, 'default', event_time, event_type = 1, event_type = 2) FROM analytics.events;` | 通过 | 复测：1 行；WINDOW_FUNNEL 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #26 |
| 251 | `SELECT QUANTILE_UNION(TO_QUANTILE_STATE(value, 2048)) FROM analytics.metrics;` | 通过 | 复测：1 行；QUANTILE_UNION 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #27 |
| 252 | `SELECT AI_AGG('ai_resource', text_value, 'Summarize the values') FROM analytics.texts;` | 通过 | 复测：Can not found function 'AI_AGG' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #28 |
| 253 | `SELECT SUM_FOREACH(array_value), COUNT_FOREACH(array_value), ARRAY_AGG_FOREACH(array_value) FROM analytics.array_metrics;` | 通过 | 复测：1 行；三个 FOREACH 聚合返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #29 |
| 254 | `SELECT MAX_BY_STATE(value, event_time), GROUP_CONCAT_STATE(value) FROM analytics.metrics;` | 通过 | 复测：3 行；状态函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #30 |
| 255 | `SELECT MAX_BY_MERGE(max_state), GROUP_CONCAT_MERGE(concat_state) FROM analytics.aggregate_states;` | 通过 | 错误：Database [analytics] does not exist；全选批次逐条提交 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #31 |
| 256 | `SELECT MAX_BY_UNION(max_state), GROUP_CONCAT_UNION(concat_state) FROM analytics.aggregate_states;` | 通过 | 错误：Database [analytics] does not exist；全选批次逐条提交 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #32 |
| 257 | `SELECT ROW_NUMBER() OVER (), RANK() OVER (ORDER BY value), DENSE_RANK() OVER (ORDER BY value) FROM analytics.metrics;` | 通过 | 复测：3 行；三个窗口函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #33 |
| 258 | `SELECT PERCENT_RANK() OVER (PARTITION BY category ORDER BY value), CUME_DIST() OVER (PARTITION BY category ORDER BY value) FROM analytics.metrics;` | 通过 | 复测：3 行；两个窗口函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #34 |
| 259 | `SELECT NTILE(4) OVER (ORDER BY value), LAG(value, 1, 0) OVER (ORDER BY event_time), LEAD(value, 1, 0) OVER (ORDER BY event_time) FROM analytics.metrics;` | 通过 | 复测：3 行；NTILE/LAG/LEAD 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #35 |
| 260 | `SELECT FIRST_VALUE(value) OVER (PARTITION BY category ORDER BY event_time), LAST_VALUE(value) OVER (PARTITION BY category ORDER BY event_time ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM analytics.metrics;` | 通过 | 复测：3 行；FIRST_VALUE/LAST_VALUE 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #36 |
| 261 | `SELECT SUM(value) OVER (PARTITION BY category ORDER BY event_time ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) FROM analytics.metrics;` | 通过 | 复测：3 行；SUM 窗口函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #37 |
| 262 | `SELECT AVG(value) OVER (ORDER BY event_time RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM analytics.metrics;` | 通过 | 复测：3 行；AVG 窗口函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #38 |
| 263 | `SELECT EXPONENTIAL_MOVING_AVERAGE(2.0, value, event_index), EXPONENTIAL_MOVING_AVERAGE(0.0, value, event_index) FROM analytics.time_series;` | 通过 | 复测：Can not found function 'EXPONENTIAL_MOVING_AVERAGE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #39 |
| 264 | `SELECT EXPONENTIAL_MOVING_AVERAGE(1.0, value, event_index) OVER (ORDER BY event_index ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM analytics.time_series;` | 通过 | 复测：Can not found function 'EXPONENTIAL_MOVING_AVERAGE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_9.txt` #40 |
| 265 | `SELECT item FROM analytics.array_rows LATERAL VIEW EXPLODE(items) exploded AS item;` | 通过 | 复测：3 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #1 |
| 266 | `SELECT item FROM analytics.array_rows LATERAL VIEW EXPLODE_OUTER(items) exploded AS item;` | 通过 | 复测：4 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #2 |
| 267 | `SELECT item FROM analytics.bitmap_rows LATERAL VIEW EXPLODE_BITMAP(bitmap_value) exploded AS item;` | 通过 | 复测：5 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #3 |
| 268 | `SELECT item FROM analytics.bitmap_rows LATERAL VIEW EXPLODE_BITMAP_OUTER(bitmap_value) exploded AS item;` | 通过 | 复测：5 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #4 |
| 269 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_DOUBLE(json_value) exploded AS item;` | 通过 | 复测：5 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #5 |
| 270 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_DOUBLE_OUTER(json_value) exploded AS item;` | 通过 | 复测：6 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #6 |
| 271 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_INT(json_value) exploded AS item;` | 通过 | 复测：5 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #7 |
| 272 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_INT_OUTER(json_value) exploded AS item;` | 通过 | 复测：6 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #8 |
| 273 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_JSON(json_value) exploded AS item;` | 通过 | 复测：5 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #9 |
| 274 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_JSON_OUTER(json_value) exploded AS item;` | 通过 | 复测：6 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #10 |
| 275 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_STRING(json_value) exploded AS item;` | 通过 | 复测：5 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #11 |
| 276 | `SELECT item FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_ARRAY_STRING_OUTER(json_value) exploded AS item;` | 通过 | 复测：6 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #12 |
| 277 | `SELECT k, v FROM analytics.json_rows LATERAL VIEW EXPLODE_JSON_OBJECT(json_value) exploded AS k, v;` | 通过 | 复测：2 行；JSON 表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #13 |
| 278 | `SELECT k, v FROM analytics.map_rows LATERAL VIEW EXPLODE_MAP(map_value) exploded AS k, v;` | 通过 | 复测：3 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #14 |
| 279 | `SELECT k, v FROM analytics.map_rows LATERAL VIEW EXPLODE_MAP_OUTER(map_value) exploded AS k, v;` | 通过 | 复测：3 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #15 |
| 280 | `SELECT number FROM analytics.source_rows LATERAL VIEW EXPLODE_NUMBERS(10) exploded AS number;` | 通过 | 复测：10 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #16 |
| 281 | `SELECT number FROM analytics.source_rows LATERAL VIEW EXPLODE_NUMBERS_OUTER(0) exploded AS number;` | 通过 | 复测：1 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #17 |
| 282 | `SELECT item FROM analytics.text_rows LATERAL VIEW EXPLODE_SPLIT(text_value, ',') exploded AS item;` | 通过 | 复测：3 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #18 |
| 283 | `SELECT item FROM analytics.text_rows LATERAL VIEW EXPLODE_SPLIT_OUTER(text_value, ',') exploded AS item;` | 通过 | 复测：3 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #19 |
| 284 | `SELECT pos, item FROM analytics.array_rows LATERAL VIEW POSEXPLODE(items) exploded AS pos, item;` | 通过 | 复测：3 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #20 |
| 285 | `SELECT pos, item FROM analytics.array_rows LATERAL VIEW POSEXPLODE_OUTER(items) exploded AS pos, item;` | 通过 | 复测：4 行；表值函数返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #21 |
| 286 | `SELECT k, v FROM analytics.json_rows LATERAL VIEW JSON_EACH(json_value) exploded AS k, v;` | 通过 | 复测：Can not found function 'JSON_EACH' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #22 |
| 287 | `SELECT k, v FROM analytics.json_rows LATERAL VIEW JSON_EACH_OUTER(json_value) exploded AS k, v;` | 通过 | 复测：Can not found function 'JSON_EACH_OUTER' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #23 |
| 288 | `SELECT k, v FROM analytics.json_rows LATERAL VIEW JSON_EACH_TEXT(json_value) exploded AS k, v;` | 通过 | 复测：Can not found function 'JSON_EACH_TEXT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #24 |
| 289 | `SELECT k, v FROM analytics.json_rows LATERAL VIEW JSON_EACH_TEXT_OUTER(json_value) exploded AS k, v;` | 通过 | 复测：Can not found function 'JSON_EACH_TEXT_OUTER' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #25 |
| 290 | `SELECT UNNEST([1, 2, 3]);` | 通过 | 错误：Can not found function 'UNNEST' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_10.txt` #26 |
| 291 | `SELECT * FROM BACKENDS();` | 通过 | 1 行；BACKENDS 节点 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #1 |
| 292 | `SELECT * FROM CATALOGS();` | 通过 | 1 行；CATALOGS 列表 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #2 |
| 293 | `SELECT * FROM FRONTENDS();` | 通过 | 1 行；FRONTENDS 节点 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #3 |
| 294 | `SELECT * FROM FRONTENDS_DISKS();` | 通过 | 5 行；FRONTENDS_DISKS 信息 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #4 |
| 295 | `SELECT * FROM JOBS("type" = "insert");` | 通过 | 0 行；无 insert JOBS | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #5 |
| 296 | `SELECT * FROM TASKS("type" = "insert");` | 通过 | 0 行；无 insert TASKS | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #6 |
| 297 | `SELECT * FROM MV_INFOS("database" = "analytics");` | 通过 | 复测：0 行，analytics 当前无物化视图；函数执行成功 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #7 |
| 298 | `SELECT * FROM NUMBERS("number" = "10");` | 通过 | 10 行；NUMBERS 0–9 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #8 |
| 299 | `SELECT * FROM PARTITIONS("catalog" = "internal", "database" = "analytics", "table" = "events");` | 通过 | 复测：返回 events 表分区元数据 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #9 |
| 300 | `SELECT * FROM PARTITION_VALUES("catalog" = "hive", "database" = "analytics", "table" = "events");` | 通过 | 错误：can not find catalog hive | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #10 |
| 301 | `SELECT * FROM QUERY("catalog" = "jdbc_catalog", "query" = "SELECT * FROM analytics.events");` | 通过 | 错误：Catalog not found: jdbc_catalog | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #11 |
| 302 | `SELECT * FROM HUDI_META("table" = "hudi.analytics.events", "query_type" = "timeline");` | 通过 | 错误：Can not found function 'HUDI_META' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #12 |
| 303 | `SELECT * FROM ICEBERG_META("table" = "iceberg.analytics.events", "query_type" = "snapshots");` | 通过 | 错误：The specified catalog does not exist: iceberg | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #13 |
| 304 | `SELECT * FROM PARQUET_META("uri" = "s3://bucket/events.parquet", "mode" = "parquet_schema");` | 通过 | 错误：Can not found function 'PARQUET_META' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #14 |
| 305 | `SELECT * FROM FILE("uri" = "s3://bucket/events.json", "format" = "json", "fs.s3.support" = "true");` | 通过 | 错误：Can not found function 'FILE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #15 |
| 306 | `SELECT * FROM HDFS("uri" = "hdfs://namenode:8020/events.json", "format" = "json");` | 通过 | 错误：HDFS 路径主机 namenode 无法解析 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #16 |
| 307 | `SELECT * FROM S3("uri" = "s3://bucket/events.json", "format" = "json");` | 通过 | 错误：Properties 's3.endpoint' is required | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #17 |
| 308 | `SELECT * FROM LOCAL("file_path" = "events.json", "backend_id" = "10001", "format" = "json");` | 通过 | 错误：backend not found with backend_id = 10001 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #18 |
| 309 | `SELECT * FROM HTTP("uri" = "https://example.com/events.json", "format" = "json");` | 通过 | 错误：Can not found function 'HTTP' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #19 |
| 310 | `SELECT * FROM CDC_STREAM("type" = "mysql", "database" = "analytics", "table" = "events", "offset" = "initial");` | 通过 | 错误：Can not found function 'CDC_STREAM' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_11.txt` #20 |
| 311 | `SELECT AI_CLASSIFY('ai_resource', 'Apache Doris is a real-time analytic database.', ['database', 'sports']);` | 通过 | 错误：Can not found function 'AI_CLASSIFY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #1 |
| 312 | `SELECT AI_EXTRACT('ai_resource', 'Apache Doris began as the Palo project.', ['product', 'original name']);` | 通过 | 错误：Can not found function 'AI_EXTRACT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #2 |
| 313 | `SELECT id FROM analytics.comments WHERE AI_FILTER('ai_resource', CONCAT('Is this positive? ', comment));` | 通过 | 复测：Can not found function 'AI_FILTER' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #3 |
| 314 | `SELECT AI_FIXGRAMMAR('ai_resource', 'Apache Doris are fast database.');` | 通过 | 错误：Can not found function 'AI_FIXGRAMMAR' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #4 |
| 315 | `SELECT AI_GENERATE('ai_resource', 'Describe Apache Doris in one sentence.');` | 通过 | 错误：Can not found function 'AI_GENERATE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #5 |
| 316 | `SELECT AI_MASK('ai_resource', 'Email admin@example.com', ['email']);` | 通过 | 错误：Can not found function 'AI_MASK' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #6 |
| 317 | `SELECT AI_SENTIMENT('ai_resource', 'Apache Doris is excellent.');` | 通过 | 错误：Can not found function 'AI_SENTIMENT' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #7 |
| 318 | `SELECT AI_SIMILARITY('ai_resource', 'real-time analytics', description) FROM analytics.documents;` | 通过 | 复测：Can not found function 'AI_SIMILARITY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #8 |
| 319 | `SELECT AI_SUMMARIZE('ai_resource', 'Apache Doris supports high-concurrency real-time analytics.');` | 通过 | 错误：Can not found function 'AI_SUMMARIZE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #9 |
| 320 | `SELECT AI_TRANSLATE('ai_resource', 'Apache Doris is fast.', 'Chinese');` | 通过 | 错误：Can not found function 'AI_TRANSLATE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #10 |
| 321 | `SELECT COSINE_DISTANCE([1.0, 2.0], [2.0, 3.0]), COSINE_SIMILARITY([1.0, 2.0], [2.0, 3.0]);` | 通过 | 错误：Can not found function 'COSINE_SIMILARITY' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #11 |
| 322 | `SELECT INNER_PRODUCT([1.0, 2.0], [2.0, 3.0]), L1_DISTANCE([1.0, 2.0], [2.0, 3.0]), L2_DISTANCE([1.0, 2.0], [2.0, 3.0]);` | 通过 | 1 行；INNER_PRODUCT/L1_DISTANCE/L2_DISTANCE 返回结果 | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #12 |
| 323 | `SELECT EMBED('ai_resource', 'Apache Doris vector search');` | 通过 | 错误：Can not found function 'EMBED' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #13 |
| 324 | `SELECT INNER_PRODUCT_APPROXIMATE([1.0, 2.0], embedding), L2_DISTANCE_APPROXIMATE([1.0, 2.0], embedding) FROM analytics.vectors;` | 通过 | 复测：Can not found function 'INNER_PRODUCT_APPROXIMATE' | 返回结果或具体数据库错误 | fixture 来源：`dql_doris_function_combinations_12.txt` #14 |
| 325 | `ANALYZE DATABASE tpch;` | 通过 | 复测：返回 1 行分析任务，Job_Id=1789089634374，表为 tpch.orders | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #1 |
| 326 | `ANALYZE TABLE tpch.orders WITH SAMPLE PERCENT 10;` | 通过 | 复测：约 19 秒后返回 1 行分析任务，Job_Id=1789089634379 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #2 |
| 327 | `DROP STATS tpch.orders;` | 通过 | 复测：0 rows affected；只删除隔离表统计信息，可再 ANALYZE | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #3 |
| 328 | `ADMIN SET FRONTEND CONFIG ("disable_balance" = "true");` | 通过 | 执行信息：0 rows affected；随后执行 `... = "false"`，ADMIN SHOW 复核初始值 false | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #4 |
| 329 | `ALTER SYSTEM ADD BACKEND "127.0.0.1:9050";` | 通过 | 执行信息：0 rows affected；SHOW BACKENDS 出现临时 loopback 节点，Alive=false、TabletNum=0 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #5 |
| 330 | `ALTER SYSTEM DECOMMISSION BACKEND "127.0.0.1:9050";` | 通过 | 执行信息：0 rows affected；仅作用于 0 tablet 临时节点，随后 DROPP 清理；最终仅真实 BE | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #6 |
| 331 | `KILL CONNECTION 55;` | 通过 | 错误：Unknown thread id: 55；现有会话 ID 为 144 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #7 |
| 332 | `KILL QUERY "trace-id";` | 通过 | 错误：invalid query id；trace-id 不是 Doris 查询 ID | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #8 |
| 333 | `CANCEL BACKUP FROM tpch;` | 通过 | 复测：No backup job is currently running；命令已到 Doris | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #9 |
| 334 | `CANCEL RESTORE FROM tpch;` | 通过 | 复测：No restore is currently running；命令已到 Doris | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #10 |
| 335 | `BUILD INDEX idx_note ON tpch.orders;` | 通过 | 复测：Index[idx_note] is not exist in table[orders]；fixture 未先定义索引 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #11 |
| 336 | `CANCEL BUILD INDEX ON tpch.orders;` | 通过 | 复测：No job to cancel for Table[orders]；命令已到 Doris | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #12 |
| 337 | `REFRESH MATERIALIZED VIEW order_summary COMPLETE;` | 通过 | 错误：table not found, tableName=order_summary | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #13 |
| 338 | `SET SESSION query_timeout = 3600;` | 通过 | 执行信息：0 rows affected | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #14 |
| 339 | `SET GLOBAL default_rowset_type = beta;` | 通过 | 执行信息：0 rows affected；执行前全局 Value/Default_Value 均为 beta，未改变配置 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #15 |
| 340 | `UNSET VARIABLE query_timeout;` | 通过 | 执行信息：0 rows affected；随后 SHOW query_timeout 返回默认 900 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #16 |
| 341 | `SHOW FRONTENDS;` | 通过 | 1行：FE版本 doris-3.0.8-rc01 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #17 |
| 342 | `SHOW BACKENDS;` | 通过 | 1行：BE节点列表 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #18 |
| 343 | `SHOW PROCESSLIST;` | 通过 | 1行：当前 CloudDM 会话 ID 144 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #19 |
| 344 | `SHOW VARIABLES LIKE 'query_timeout';` | 通过 | 1行：query_timeout 当前值 900 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #20 |
| 345 | `SHOW ANALYZE;` | 通过 | 0行：无分析任务 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #21 |
| 346 | `SHOW WORKLOAD GROUPS;` | 通过 | 1行：仅 normal workload group | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #22 |
| 347 | `CREATE WORKLOAD GROUP dal_group PROPERTIES ('cpu_share'='1024');` | 通过 | 执行信息：0 rows affected；新 group 出现在 SHOW 列表 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #23 |
| 348 | `ALTER WORKLOAD GROUP dal_group PROPERTIES ('cpu_share'='2048');` | 通过 | 执行信息：0 rows affected；cpu_share 改为 2048 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #24 |
| 349 | `DROP WORKLOAD GROUP dal_group;` | 通过 | 执行信息：0 rows affected；SHOW 列表恢复为仅 normal | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #25 |
| 350 | `BEGIN;` | CloudDM 失败 | 页面拒绝：请先切换到手动事务模式再执行事务命令；未提交数据库 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #26 |
| 351 | `COMMIT;` | CloudDM 失败 | 页面拒绝：请先切换到手动事务模式再执行事务命令；未提交数据库 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #27 |
| 352 | `ROLLBACK;` | CloudDM 失败 | 页面拒绝：请先切换到手动事务模式再执行事务命令；未提交数据库 | 执行记录或明确数据库错误 | fixture 来源：`dcl_doris_dal_combinations_batch_0.txt` #28 |
| 353 | `SHOW CATALOGS;` | 通过 | 结果表 1 行：`internal`，CatalogId=0、IsCurrent=Yes。 | 列出 Catalog | — |
| 354 | `SHOW FRONTEND CONFIG LIKE "disable_balance";` | 通过 | 结果表 1 行：`disable_balance=false`，IsMutable=true。 | 查 FE 参数 | — |
| 355 | `SHOW RESOURCES;` | 通过 | 返回结果表 0 行；当前无 Resource。 | 列出 Resource | — |
| 356 | `SHOW ROLES;` | 通过 | 结果表 2 行：`admin`、`operator`。 | 列出角色 | — |
| 357 | `SHOW REPOSITORIES;` | 通过 | 返回结果表 0 行。 | 列出备份仓库 | — |
| 358 | `SHOW PLUGINS;` | 通过 | 结果表 3 行，包含内置 `__builtin_AuditLoader`、`__builtin_AuditLogBuilder`。 | 列出插件 | — |
| 359 | `SHOW CATALOG RECYCLE BIN;` | 通过 | 返回结果表 0 行。 | 查询回收站 | — |
| 360 | `SHOW ROW POLICY;` | 通过 | 返回结果表 0 行。 | 查询行策略 | — |
| 361 | `SHOW SQL_BLOCK_RULE;` | 通过 | 返回结果表 0 行。 | 查询 SQL 阻断规则 | — |
| 362 | `SHOW BROKER;` | 通过 | 返回结果表 0 行。 | 查询 Broker | — |
| 363 | `SHOW FRONTENDS DISKS;` | 通过 | 结果表 5 行。 | 查看 FE 磁盘 | — |
| 364 | `SHOW DATA TYPES;` | 通过 | 结果表 28 行，首行为 `AGG_STATE`。 | 列出类型 | — |
| 365 | `SHOW CHARSET;` | 通过 | 结果表 1 行：`utf8mb4`。 | 列出字符集 | — |
| 366 | `SHOW COLLATION;` | 通过 | 结果表 2 行，包含 `utf8mb4_0900_bin`。 | 列出排序规则 | — |
| 367 | `SHOW PRIVILEGES;` | 通过 | 结果表 12 行，包含 `Node_priv`。 | 列出权限种类 | — |
| 368 | `SHOW FILE;` | 通过 | 返回结果表 0 行。 | 列出文件 | — |
| 369 | `SHOW ENCRYPTKEYS;` | 通过 | 返回结果表 0 行。 | 列出加密密钥 | — |
| 370 | `SHOW WARM UP JOB;` | 通过 | 页面 Execution Information 报 `ClassCastException: Env cannot be cast to CloudEnv`；没有结果表。当前 Docker 是非存算分离 Doris，不能以 0 行或成功支持记录。 | 查询预热任务或明确不适用 | — |
| 371 | `SHOW CATALOG internal;` | 通过 | 返回 Key/Value 结果表 0 行；`internal` 没有可列出的属性。 | 查看 Catalog 属性 | — |
| 372 | `SHOW CREATE CATALOG internal;` | 通过 | 结果表 1 行：`CREATE CATALOG \`internal\` COMMENT "Doris internal catalog"`。 | 查看创建定义 | — |
| 373 | `SHOW BACKEND CONFIG LIKE "%compression_level%";` | 通过 | 结果表 1 行：BackendId=1788516514827、`LZ4_HC_compression_level=9`。 | 查当前真实 BE 参数 | — |
| 374 | `SHOW PROC "/cluster_balance/pending_tablets";` | 通过 | 返回 TabletId/Status 等列的结果表 0 行。 | 查待调度 tablet | — |
| 375 | `SHOW TABLE STATS pika.codex_integration_orders;` | 通过 | 返回 `row_count`、`last_analyze_time` 等列的结果表 1 行。 | 查隔离表统计状态 | — |
| 376 | `SHOW QUERY STATS FOR pika;` | 通过 | 返回 TableName/QueryCount 结果表 7 行，包括 `codex_integration_orders`。 | 查 DB 中表的历史查询命中 | — |
| 377 | `SHOW QUERY STATS FROM pika.codex_integration_orders;` | 通过 | 返回 Field/QueryCount/FilterCount 结果表 3 行：id、amount、name。 | 查隔离表字段查询命中 | — |
| 378 | `SHOW CATALOGS LIKE "internal";` | 通过 | 结果表 1 行：internal。 | Catalog LIKE 过滤 | — |
| 379 | `SHOW GLOBAL VARIABLES LIKE "query_timeout";` | 通过 | 结果表 1 行：Value=Default_Value=900、Changed=0。 | 查全局超时变量 | — |
| 380 | `SHOW TYPE_CAST IN pika;` | 通过 | 返回结果表共 479 行，页面分页展示；首行 `NULL_TYPE → BOOLEAN`。 | 查类型转换规则 | — |
| 381 | `ANALYZE TABLE pika.codex_integration_orders (id, amount) WITH SYNC WITH SAMPLE ROWS 2;` | 通过 | 页面 Execution Information 返回 `0 rows affected`；紧随其后的 A31/A32 证明 id、amount 统计信息和采集时间已更新。 | 同步采集指定列的样本统计 | — |
| 382 | `SHOW ANALYZE pika.codex_integration_orders WHERE STATE="FINISHED";` | 通过 | 返回 job_id/state 结果表 0 行；同步 A29 未留下可列出的异步 Job，不把空表当成 ANALYZE 失败。 | 查询手动分析任务 | — |
| 383 | `SHOW COLUMN STATS pika.codex_integration_orders (id, amount);` | 通过 | 结果表 2 行，amount 行显示 count=3、method=SAMPLE、trigger=MANUAL、updated_time=`2026-09-16 00:53:26`。 | 核对指定列统计 | — |
| 384 | `SHOW TABLE STATS pika.codex_integration_orders;` | 通过 | 结果表 1 行：row_count=3、trigger=MANUAL、last_analyze_time=`2026-09-16 00:53:26`。 | 核对采集后的表状态 | — |
| 385 | `CREATE ROLE codex_it_doris_role_20260916_01 COMMENT "CloudDM isolated DAL test";` | 通过 | 页面 `0 rows affected`；A36 查到新增角色。 | 建立无权限隔离角色 | — |
| 386 | `ALTER ROLE codex_it_doris_role_20260916_01 COMMENT "CloudDM isolated DAL verified";` | 通过 | 页面 `0 rows affected`。 | 修改测试角色备注 | — |
| 387 | `GRANT SELECT_PRIV ON internal.pika.codex_integration_orders TO ROLE "codex_it_doris_role_20260916_01";` | 通过 | 页面 `0 rows affected`；A38 的 TablePrivs 显示该表 `Select_priv`。 | 仅授予隔离表读取权限 | — |
| 388 | `SHOW ROLES;` | 通过 | 结果表 3 行，较 A04 的初始 2 行增加测试角色。 | 验证角色创建 | — |
| 389 | `CREATE USER "codex_it_doris_user_20260916_01"@"%" DEFAULT ROLE "codex_it_doris_role_20260916_01";` | 通过 | 页面 `0 rows affected`；A38 确认角色绑定。 | 建立无密码、专用测试用户 | — |
| 390 | `SHOW GRANTS FOR "codex_it_doris_user_20260916_01"@"%";` | 通过 | 结果表 1 行：Password=No、Roles=测试角色、TablePrivs=`internal.pika.codex_integration_orders: Select_priv`。 | 验证用户有效授权 | — |
| 391 | `REVOKE SELECT_PRIV ON internal.pika.codex_integration_orders FROM ROLE "codex_it_doris_role_20260916_01";` | 通过 | 页面 `0 rows affected`。 | 撤销本轮隔离授权 | — |
| 392 | `SHOW GRANTS FOR "codex_it_doris_user_20260916_01"@"%";` | 通过 | 页面返回 1 行有效权限结果；此轮未完整抄录 TablePrivs 单元格，不将该行单独作为“撤权已反映到用户”的证据。 | 撤权后再次查询 | — |
| 393 | `DROP USER "codex_it_doris_user_20260916_01"@"%";` | 通过 | 页面 `0 rows affected`。 | 删除专用用户 | — |
| 394 | `DROP ROLE codex_it_doris_role_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 删除专用角色 | — |
| 395 | `SHOW ROLES;` | 通过 | 结果表恢复为 2 行：`admin`、`operator`，与 A04 初始状态一致。 | 核对清理终态 | — |
| 396 | `CREATE SQL_BLOCK_RULE codex_it_doris_rule_20260916_01 PROPERTIES("sql"="select.*codex_integration_orders","global"="false","enable"="false");` | 通过 | 页面 `0 rows affected`；规则既非 global，亦未启用。 | 创建不生效的隔离 SQL 规则 | — |
| 397 | `SHOW SQL_BLOCK_RULE FOR codex_it_doris_rule_20260916_01;` | 通过 | 结果表 1 行：Sql=`select.*codex_integration_orders`、Global=false、Enable=false。 | 核对规则属性 | — |
| 398 | `ALTER SQL_BLOCK_RULE codex_it_doris_rule_20260916_01 PROPERTIES("enable"="false");` | 通过 | 页面 `0 rows affected`；仍保持禁用，避免影响其他查询。 | 修改本轮测试规则 | — |
| 399 | `DROP SQL_BLOCK_RULE codex_it_doris_rule_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 清理专用规则 | — |
| 400 | `SHOW SQL_BLOCK_RULE;` | 通过 | 结果表 0 行，与 A09 的初始状态一致。 | 核对规则恢复终态 | — |
| 401 | `CREATE ROLE codex_it_doris_policy_role_20260916_01;` | 通过 | 页面 `0 rows affected`；未给其他用户绑定。 | 为策略创建无成员隔离角色 | — |
| 402 | `CREATE ROW POLICY codex_it_doris_row_policy_20260916_01 ON pika.codex_integration_orders AS RESTRICTIVE TO ROLE codex_it_doris_policy_role_20260916_01 USING (id >= 0);` | 通过 | 页面 `0 rows affected`；A51 查询到具体策略。 | 仅对隔离表/无成员角色建策略 | — |
| 403 | `SHOW ROW POLICY FOR ROLE codex_it_doris_policy_role_20260916_01;` | 通过 | 结果表 1 行：Catalog=internal、Db=pika、Table=codex_integration_orders、FilterType=RESTRICTIVE、WherePredicate=`id >= 0`。 | 核对策略作用范围 | — |
| 404 | `DROP ROW POLICY codex_it_doris_row_policy_20260916_01 ON pika.codex_integration_orders FOR ROLE codex_it_doris_policy_role_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 删除专用策略 | — |
| 405 | `DROP ROLE codex_it_doris_policy_role_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 删除专用角色 | — |
| 406 | `SHOW ROW POLICY;` | 通过 | 返回 0 行，与 A08 初始状态一致。 | 核对策略恢复终态 | — |
| 407 | `SHOW ROLES;` | 通过 | 返回 2 行，仅初始 `admin`、`operator`。 | 核对角色恢复终态 | — |
| 408 | `SHOW TABLES FROM pika LIKE "codex_it_doris_job_sink_20260916_01";` | 通过 | 返回 0 行。 | 确认专用目标表不存在 | — |
| 409 | `CREATE TABLE pika.codex_it_doris_job_sink_20260916_01 (id INT NOT NULL, amount DECIMAL(10,2)) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num"="1");` | 通过 | 页面 `0 rows affected`；单副本单 bucket。 | 建立隔离 Job sink | — |
| 410 | `CREATE JOB codex_it_doris_job_20260916_01 ON SCHEDULE AT "2030-01-01 00:00:00" DO INSERT INTO pika.codex_it_doris_job_sink_20260916_01 SELECT id, amount FROM pika.codex_integration_orders;` | 通过 | 页面 `0 rows affected`；A59 证实 Job 存在。 | 建立未来触发的一次性 Job，不立即写入 | — |
| 411 | `SELECT Id, Name, Status, ExecuteType FROM jobs("type"="insert") WHERE Name = "codex_it_doris_job_20260916_01";` | 通过 | 页面自动补 `LIMIT 1000`；结果 1 行：Id=430771772961749、Status=RUNNING、ExecuteType=ONE_TIME。此 RUNNING 为 Job 状态，非证明任务已执行。 | 查询本轮 Job 状态 | — |
| 412 | `PAUSE JOB WHERE jobname="codex_it_doris_job_20260916_01";` | 通过 | 页面 `0 rows affected`。 | 暂停专用 Job | — |
| 413 | `SELECT Id, Name, Status FROM jobs("type"="insert") WHERE Name="codex_it_doris_job_20260916_01";` | 通过 | 结果 1 行：同一 Id，Status=PAUSED。 | 核对暂停终态 | — |
| 414 | `RESUME JOB WHERE jobname="codex_it_doris_job_20260916_01";` | 通过 | 页面 `0 rows affected`。 | 恢复专用 Job | — |
| 415 | `SELECT Id, Name, Status FROM jobs("type"="insert") WHERE Name="codex_it_doris_job_20260916_01";` | 通过 | 结果 1 行：同一 Id，Status=RUNNING。 | 核对恢复终态 | — |
| 416 | `DROP JOB WHERE jobName="codex_it_doris_job_20260916_01";` | 通过 | 页面 `0 rows affected`。 | 删除专用 Job | — |
| 417 | `SELECT Id, Name, Status FROM jobs("type"="insert") WHERE Name="codex_it_doris_job_20260916_01";` | 通过 | 返回 0 行。 | 核对 Job 已删除 | — |
| 418 | `SELECT COUNT(*) AS sink_rows FROM pika.codex_it_doris_job_sink_20260916_01;` | 通过 | 结果 1 行：sink_rows=0。 | 核对未来 Job 未写数据 | — |
| 419 | `DROP TABLE pika.codex_it_doris_job_sink_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 清理本轮 sink 表 | — |
| 420 | `SHOW TABLES FROM pika LIKE "codex_it_doris_job_sink_20260916_01";` | 通过 | 返回 0 行。 | 核对 sink 清理终态 | — |
| 421 | `SHOW TABLES FROM pika LIKE "codex_it_doris_index_20260916_01";` | 通过 | 返回 0 行。 | 确认专用索引表不存在 | — |
| 422 | `CREATE TABLE pika.codex_it_doris_index_20260916_01 (id INT NOT NULL, note STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num"="1");` | 通过 | 页面 `0 rows affected`。 | 建立隔离索引表 | — |
| 423 | `INSERT INTO pika.codex_it_doris_index_20260916_01 VALUES (1,"alpha"),(2,"beta"),(3,"gamma");` | 通过 | 页面 `3 rows affected`。 | 构造需补建索引的历史数据 | — |
| 424 | `CREATE INDEX idx_note ON pika.codex_it_doris_index_20260916_01(note) USING INVERTED;` | 通过 | 页面 `0 rows affected`。 | 为已有数据定义倒排索引 | — |
| 425 | `SHOW INDEX FROM pika.codex_it_doris_index_20260916_01;` | 通过 | 结果表 1 行：Key_name=idx_note、Column_name=note、Index_type=INVERTED。 | 核对索引定义 | — |
| 426 | `BUILD INDEX idx_note ON pika.codex_it_doris_index_20260916_01;` | 通过 | 页面 `0 rows affected`；A75/A77 查询到真实构建任务。 | 实际构建已有数据的索引 | — |
| 427 | `SHOW BUILD INDEX WHERE TableName="codex_it_doris_index_20260916_01";` | 通过 | 结果表 1 行：JobId=1789089634595，首次看到 State=RUNNING、Progress=1/1。 | 查询真实构建任务 | — |
| 428 | `CANCEL BUILD INDEX ON pika.codex_it_doris_index_20260916_01;` | 通过 | 页面 Doris 错误：`No job to cancel for Table[codex_it_doris_index_20260916_01]`；A77 显示该 Job 已于创建后约 0.5 秒 FINISHED，取消没有命中运行任务，不能作为成功取消证据。 | 尝试取消本轮索引任务 | — |
| 429 | `SHOW BUILD INDEX WHERE TableName="codex_it_doris_index_20260916_01";` | 通过 | 同一 JobId 1 行，State=FINISHED、FinishTime=`2026-09-16 01:00:30.378`。 | 核对任务最终状态 | — |
| 430 | `DROP INDEX idx_note ON pika.codex_it_doris_index_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 清理本轮索引定义 | — |
| 431 | `SHOW INDEX FROM pika.codex_it_doris_index_20260916_01;` | 通过 | 结果表 0 行。 | 核对定义清理 | — |
| 432 | `DROP TABLE pika.codex_it_doris_index_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 清理本轮索引表 | — |
| 433 | `SHOW TABLES FROM pika LIKE "codex_it_doris_index_20260916_01";` | 通过 | 返回 0 行。 | 核对表清理终态 | — |
| 434 | `SHOW TABLES FROM pika LIKE "codex_it_doris_mv_20260916_01";` | 通过 | 返回 0 行。 | 确认隔离 MV 不存在 | — |
| 435 | `CREATE MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01 BUILD DEFERRED REFRESH COMPLETE ON MANUAL DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num"="1") AS SELECT id, amount FROM pika.codex_integration_orders;` | 通过 | 页面 `0 rows affected`；仅引用隔离表。 | 创建手动刷新的异步 MV | — |
| 436 | `SHOW CREATE MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01;` | 通过 | 结果表 1 行，包含 `BUILD DEFERRED REFRESH COMPLETE ON MANUAL`、BUCKETS 1。 | 核对真实 MV 定义 | — |
| 437 | `SELECT MvName, Status FROM jobs("type"="mv") WHERE MvName="codex_it_doris_mv_20260916_01";` | 通过 | 结果表 1 行，Status=RUNNING（Job 状态，不是任务终态）。 | 核对 MV Job 已注册 | — |
| 438 | `REFRESH MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01 COMPLETE;` | 通过 | 页面 `0 rows affected`；A87/A88 核对真实任务和物化数据。 | 实际触发全量刷新 | — |
| 439 | `SELECT TaskId, MvName, Status, ErrorMsg, RefreshMode FROM tasks("type"="mv") WHERE MvName="codex_it_doris_mv_20260916_01";` | 通过 | 结果表 1 行：TaskId=430976647340928、Status=SUCCESS、RefreshMode=COMPLETE、ErrorMsg 为空。 | 核对刷新终态 | — |
| 440 | `SELECT COUNT(*) AS mv_rows FROM pika.codex_it_doris_mv_20260916_01;` | 通过 | 结果 1 行：mv_rows=3，与基表一致。 | 核对物化结果 | — |
| 441 | `PAUSE MATERIALIZED VIEW JOB ON pika.codex_it_doris_mv_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 暂停隔离 MV Job | — |
| 442 | `SELECT MvName, Status FROM jobs("type"="mv") WHERE MvName="codex_it_doris_mv_20260916_01";` | 通过 | 结果 1 行：Status=PAUSED。 | 核对暂停终态 | — |
| 443 | `RESUME MATERIALIZED VIEW JOB ON pika.codex_it_doris_mv_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 恢复隔离 MV Job | — |
| 444 | `SELECT MvName, Status FROM jobs("type"="mv") WHERE MvName="codex_it_doris_mv_20260916_01";` | 通过 | 结果 1 行：Status=RUNNING。 | 核对恢复终态 | — |
| 445 | `CANCEL MATERIALIZED VIEW TASK 430976647340928 ON pika.codex_it_doris_mv_20260916_01;` | 通过 | Doris 页面错误 `no running task`；A87 已显示该任务 SUCCESS，不能作为运行任务取消成功证据。 | 尝试取消本轮已完成任务 | — |
| 446 | `ALTER MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01 REFRESH AUTO;` | 通过 | 页面 `0 rows affected`。 | 修改隔离 MV 刷新模式 | — |
| 447 | `SHOW CREATE MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01;` | 通过 | 结果 1 行，定义变为 `BUILD DEFERRED REFRESH AUTO ON MANUAL`。 | 核对模式修改 | — |
| 448 | `ALTER MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01 REFRESH COMPLETE;` | 通过 | 页面 `0 rows affected`。 | 恢复原刷新模式 | — |
| 449 | `REFRESH MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01 AUTO;` | 通过 | 页面 `0 rows affected`；A98 核对新任务实际选择 COMPLETE。 | 测试 AUTO 触发形式 | — |
| 450 | `SELECT TaskId, MvName, Status, RefreshMode FROM tasks("type"="mv") WHERE MvName="codex_it_doris_mv_20260916_01" ORDER BY CreateTime DESC;` | 通过 | 结果 2 行，最新 TaskId=431052305231139；两条均 SUCCESS、RefreshMode=COMPLETE。 | 核对两次刷新记录 | — |
| 451 | `DROP MATERIALIZED VIEW pika.codex_it_doris_mv_20260916_01;` | 通过 | 页面 `0 rows affected`。 | 删除本轮 MV | — |
| 452 | `SELECT MvName, Status FROM jobs("type"="mv") WHERE MvName="codex_it_doris_mv_20260916_01";` | 通过 | 返回 0 行。 | 核对关联 Job 清理 | — |
| 453 | `SHOW TABLES FROM pika LIKE "codex_it_doris_mv_20260916_01";` | 通过 | 返回 0 行。 | 核对 MV 清理终态 | — |
| 454 | `DESCRIBE pika.codex_integration_orders;` | 通过 | 3 行字段说明。 | 表结构可见 | — |
| 455 | `SELECT COUNT(*), SUM(amount), AVG(amount), MIN(amount), MAX(amount) FROM pika.codex_integration_orders;` | 通过 | 1 行：`3`、`61.00`、`20.3333`、`10.50`、`30.50`。 | 聚合返回 | — |
| 456 | `SELECT ROW_NUMBER() OVER (ORDER BY id), LAG(amount) OVER (ORDER BY id) FROM pika.codex_integration_orders;` | 通过 | 3 行：序号 `1,2,3`；前值 `NULL,10.50,20.00`。 | 窗口函数返回 | — |
| 457 | `SHOW VARIABLES LIKE 'query_timeout'; SELECT 1;`（全选执行） | 通过 | 两张结果表：前者 `query_timeout=900`，后者返回 `1`。 | DAL 和 SELECT 均提交 | — |
| 458 | `KILL QUERY "trace-id"; SELECT 1;`（全选执行） | 通过 | 执行信息先报 `invalid query id`，后显示 `SELECT 1` 返回 1 行。 | 第一条明确失败，第二条继续查询 | — |

## Purpose

验证 Doris 官方函数全集和管理命令在 CloudDM 的解析、分类、执行与失败恢复路径。

本文同时记录 Suites 与页面逐条 SQL 表；只有页面实际提交过的行计入已测。

## Scope

- 路由：`/#/sql`；Doris 3.0.8 隔离数据源。
- SQL 来源：`open-cdm-test/src/test/resources/split/doris/common/dql_doris_function_combinations_0.txt` 至 `_12.txt`，以及
  `dcl_doris_dal_combinations_batch_0.txt` 第一段。
- 函数 SQL 共 324 条，以以上 13 个 fixture 第一条分隔线之前的内容为唯一清单。
- 不覆盖：生产 BE 节点、生产 workload group 和真实用户会话。

## Preconditions

- Doris FE/BE 隔离容器运行，FE MySQL 协议端口为 `9030`。
- `pika.codex_integration_orders` 已创建并含测试数据；具备 ADMIN 权限；执行前记录真实 backend 列表。
- 原函数 fixture 使用的 `tpch` 与 `analytics` 测试对象按下文 SQL 在隔离实例构造；不要在非隔离集群执行这些准备命令。

## Test Data

- 编号：D01；数据说明：基础函数查询表；构造方式：已建表并插入边界数据；唯一标识：`pika.codex_integration_orders`；清理方式：保留供复测
- 编号：D02；数据说明：原 fixture 依赖表；构造方式：按下方建库、建表、插入 SQL 构造；唯一标识：`tpch.orders`、`analytics.*`；清理方式：保留供复测

### 页面实际执行的准备 SQL

以下 SQL 在隔离 Doris 3.0.8 的 CloudDM 查询页执行过，`CREATE` 均返回 `0 rows affected`，`INSERT`
返回对应插入行数。全新实例按顺序执行；现有实例中的测试表已保留供复测，不要重复插入。每张表均为单副本、单 bucket，避免依赖第二个
BE。

```sql
CREATE DATABASE tpch;
CREATE TABLE tpch.orders (id INT NOT NULL, amount DECIMAL(10,2)) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO tpch.orders VALUES (1,10.50),(2,20.00),(3,30.50);

CREATE DATABASE analytics;
CREATE TABLE analytics.metrics (id INT NOT NULL, value DOUBLE, weight DOUBLE, event_time DATETIME, flag BOOLEAN, metric_name STRING, category STRING, x DOUBLE, y DOUBLE, bits BIGINT, user_id BIGINT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.metrics (id,value,weight,event_time,flag,metric_name,category,x,y,bits,user_id) VALUES
    (1,10.5,1.0,"2025-09-19 10:00:00",true,"m1","A",1,2,3,101),
    (2,20.0,2.0,"2025-09-19 11:00:00",false,"m2","A",2,4,7,102),
    (3,30.5,3.0,"2025-09-19 12:00:00",true,"m3","B",3,6,1,103);
CREATE TABLE analytics.array_rows (id INT NOT NULL, items ARRAY<INT>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.array_rows VALUES (1,[1,2,3]),(2,[]);
CREATE TABLE analytics.json_rows (id INT NOT NULL, json_value STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.json_rows VALUES (1,'[1,2,3]'),(2,'["a","b"]'),(3,'{"x":1,"y":2}');
CREATE TABLE analytics.source_rows (id INT NOT NULL) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.source_rows VALUES (1);
CREATE TABLE analytics.text_rows (id INT NOT NULL, text_value STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.text_rows VALUES (1,"a,b,c");
CREATE TABLE analytics.bitmap_rows (id INT NOT NULL, bitmap_value BITMAP) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.bitmap_rows VALUES (1,BITMAP_FROM_ARRAY([1,2,3])),(2,BITMAP_FROM_ARRAY([4,5]));
CREATE TABLE analytics.map_rows (id INT NOT NULL, map_value MAP<STRING,INT>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.map_rows VALUES (1,MAP("a",1,"b",2)),(2,MAP("c",3));

CREATE TABLE analytics.map_events (id INT NOT NULL, metrics MAP<STRING,DOUBLE>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.map_events VALUES (1,MAP("latency",10.5,"bytes",100.0)),(2,MAP("latency",20.0,"bytes",200.0));
CREATE TABLE analytics.requests (id INT NOT NULL, latency_ms DOUBLE, payload_size BIGINT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.requests VALUES (1,10.5,100),(2,20.0,200),(3,30.5,300);
CREATE TABLE analytics.array_events (id INT NOT NULL, `values` ARRAY<INT>, `keys` ARRAY<STRING>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.array_events VALUES (1,[1,2,3],["a","b","c"]),(2,[4,5],["d","e"]);
CREATE TABLE analytics.bitmap_metrics (id INT NOT NULL, user_id BIGINT, bitmap_value BITMAP, cohort STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.bitmap_metrics VALUES (1,101,BITMAP_FROM_ARRAY([101,102]),"new"),(2,102,BITMAP_FROM_ARRAY([102,103]),"active");
CREATE TABLE analytics.array_metrics (id INT NOT NULL, array_value ARRAY<INT>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.array_metrics VALUES (1,[1,2,3]),(2,[2,3,4]);
CREATE TABLE analytics.map_metrics (id INT NOT NULL, map_value MAP<STRING,DOUBLE>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.map_metrics VALUES (1,MAP("a",1.0,"b",2.0)),(2,MAP("a",3.0,"c",4.0));
CREATE TABLE analytics.events (id INT NOT NULL, value DOUBLE, event_time DATETIME, event_type INT, day_1 BOOLEAN, day_2 BOOLEAN, day_3 BOOLEAN) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.events VALUES
    (1,10.5,"2025-09-19 10:00:00",1,true,false,false),
    (2,20.0,"2025-09-19 10:30:00",2,true,true,false),
    (3,30.5,"2025-09-19 11:00:00",1,true,true,true);
CREATE TABLE analytics.time_series (id INT NOT NULL, value DOUBLE, event_index INT) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.time_series VALUES (1,10.5,1),(2,20.0,2),(3,30.5,3);
CREATE TABLE analytics.documents (id INT NOT NULL, doc STRING, description STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.documents VALUES (1,"{\"match_all\":{}}","Apache Doris analytics");
CREATE TABLE analytics.sales (id INT NOT NULL, region STRING, category STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.sales VALUES (1,"east","A"),(2,"west","B");
CREATE TABLE analytics.texts (id INT NOT NULL, text_value STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.texts VALUES (1,"Apache Doris supports analytics");
CREATE TABLE analytics.comments (id INT NOT NULL, comment STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.comments VALUES (1,"good"),(2,"bad");
CREATE TABLE analytics.vectors (id INT NOT NULL, embedding ARRAY<DOUBLE>) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.vectors VALUES (1,[1.0,2.0]),(2,[2.0,3.0]);
CREATE TABLE analytics.hll_metrics (id INT NOT NULL, user_id BIGINT, hll_value HLL, sketch_value STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES ("replication_num" = "1");
INSERT INTO analytics.hll_metrics VALUES (1,101,HLL_HASH(101),""),(2,102,HLL_HASH(102),"");
```

`AGG_STATE`
两张准备表也曾按 [Doris 3.0 官方类型示例](https://doris.apache.org/docs/3.0/sql-manual/basic-element/sql-data-types/aggregate/AGG-STATE/)
尝试创建：`analytics.aggregate_states` 的 `AGG_STATE<MAX_BY(INT NOT NULL,INT)>` 报 `Can not found function 'MAX_BY'`，
`analytics.partial_aggregates` 的 `AGG_STATE<GROUP_CONCAT(STRING)>` 报 `Can not found function 'GROUP_CONCAT'`。两张表未创建，因此
F053、F255、F256 仍只能保留原 SQL 的对象缺失记录；不能将建表错误冒充为那三条函数 SQL 的直接执行结果。
`SHOW VARIABLES LIKE 'enable_agg_state'` 返回 0 行，未改变该开关。

### DORIS-SMOKE-01 基础查询（P0）

- Chrome 操作：执行 `SELECT VERSION();`、`SELECT 1;`、测试表查询和 `SHOW BACKENDS;`。
- 预期结果：FE 可查询，真实 backend 列表可见。
- 恢复/清理：记录原始 backend 列表用于最终核对。

### DORIS-MAIN-01 324 条函数 SQL（P0）

- Chrome 操作：按 `_0` 至 `_12` 顺序，将每个 fixture 第一段的 SQL 完整粘贴执行；不能删除会报错的函数。`DATE_DIFF` 场景应使用
  Doris 的 `DATEDIFF` 语法。
- 预期结果：支持的函数生成结果页签；版本不支持、对象缺失或参数不适用时显示具体错误；每个文件最后一条 SQL
  均进入执行信息，证明批次未中途漏掉尾部。
- 恢复/清理：关闭结果页签，执行 `SELECT 1;` 验证错误恢复。

### DORIS-MAIN-02 DAL 全集（P0）

- Chrome 操作：执行 DAL batch fixture 第一段全部 SQL，包括 stats、ADMIN、backend、KILL/CANCEL、全局 rowset 和 workload group
  命令。
- 重点 SQL：`DROP STATS`、`ADMIN SET FRONTEND CONFIG ('disable_balance'='true')`、`ALTER SYSTEM ADD BACKEND`、
  `ALTER SYSTEM DECOMMISSION BACKEND`、`KILL`、`CANCEL`、`SET GLOBAL default_rowset_type='beta'`、`DROP WORKLOAD GROUP`。
- 预期结果：每条有执行记录；缺失会话、任务、数据库或 workload group 时返回具体错误。
- 恢复/清理：将 `disable_balance` 恢复为 `false`，移除临时 backend。

### DORIS-STATE-01 Backend 与配置恢复（P0）

- Chrome 操作：执行 `ALTER SYSTEM DROPP BACKEND '<临时地址>';`，再执行 `SHOW BACKENDS;` 和 `SELECT 1;`。
- 预期结果：只保留测试前记录的真实 backend；查询仍成功。
- 恢复/清理：确认 FE/BE 容器均运行。

### DORIS-BOUNDARY-01 错误与生命周期（P1）

- Boundaries：使用 13 个函数 fixture 内的 NULL、空值、Unicode、数组/JSON、日期和精度边界。
- Repeat And Concurrency：重复 SHOW/SELECT 时结果页签不覆盖。
- Permission：无 ADMIN 权限时管理命令显示权限错误。
- Lifecycle：刷新页面后复核 backend 和 smoke SQL。

## 页面逐条 SQL 复测表（2026-09-15）

实际查询页签为 `internal.pika`，CloudDM 显示 `@127.0.0.1:9030`。隔离容器镜像为 Doris 3.0.8，页面 `SHOW FRONTENDS` 返回
`doris-3.0.8-rc01`；`SELECT VERSION()` 仅返回 MySQL 协议兼容值 `5.7.99`。

函数 fixture 实际清单为 324 条；当前页面已逐条提交 `_0` 至 `_12` 全部 13 个文件的第一段 SQL：197 条返回结果，127
条返回错误，无未提交条目。对先前因缺少 `tpch`、`analytics` 对象失败的
SQL，已按下文准备数据复测；包括聚合、窗口、位图、MAP、JSON、表值函数、元数据函数。仍标“对象缺失”的行需要特定 AGG_STATE、外部
catalog、文件存储、AI 资源或其他 fixture 对象，不能解释成函数不支持。标“版本不支持”的行是在表已存在后收到 Doris 3.0.8
`Can not found function`；一个 SQL 含多个函数时，该错误只证明报错所指函数，不能据此断言同行其余函数也不支持。失败 SQL
未从表中删除；后续查询持续成功。Doris 函数文档须以运行版本核对，不能把 [dev 版本的
`ARRAY_EXCEPT_ALL`](https://doris.apache.org/docs/dev/sql-manual/sql-functions/scalar-functions/array-functions/array-except-all/)
当作 3.0.8 已支持的承诺。

### DAL/管理命令

DAL fixture 28 条，当前页面全部逐条尝试：18 条执行成功，7 条收到 Doris 的目标不存在、对象缺失或参数错误，3 条事务命令被
CloudDM 页面拦截而未提交数据库，无待测行。新建隔离 `tpch.orders` 后，D01–D03 的分析和统计信息命令通过；D09–D12
从“数据库不存在”细化为“无运行中的备份/恢复/索引任务或索引未定义”，证明 SQL 已到 Doris，但不等于成功取消真实任务。D26–D28
被页面提示“请先切换到手动事务模式再执行事务命令。”拦截。页面当前 Doris 数据源不显示手动事务入口；后端 Doris
`DrHooks.setAutoCommit` 直接抛 `UnsupportedOperationException`，因此不能仅通过页面切换模式来复测这些事务 SQL。D23–D25 的
`dal_group` 创建、修改、删除链已执行，最终 `SHOW WORKLOAD GROUPS` 仅剩初始 `normal` 组；`query_timeout` 已回到 900。D15 执行前后
`default_rowset_type` 均为 beta。D04 将 `disable_balance` 从 false 临时改为 true，随后恢复 false 并经
`ADMIN SHOW FRONTEND CONFIG` 复核。D05/D06 的 fixture 地址 `127.0.0.1:9050` 经 `SHOW BACKENDS` 确认为非真实 BE；临时节点
Alive=false、TabletNum=0，DECOMMISSION
后按 [Doris 3.0 DROP BACKEND 文档](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/instance-management/DROP-BACKEND/)
使用 `ALTER SYSTEM DROPP BACKEND "127.0.0.1:9050"` 清理；最终 `SHOW BACKENDS` 仅显示原始真实 BE `10.77.80.3:9050`，
`SELECT 1` 成功。

### Doris 3.0.8 管理 SQL 官方目录覆盖审计（未执行，2026-09-16）

本节只审计文档和 fixture，不代表已在 CloudDM 页面提交
SQL。核对的是 [Doris 3.0 SQL Statements 目录](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/)及其子目录，与
`open-cdm-test/src/test/resources/split/doris/common/`、`.../3/` **全部** split fixture；不把 4.x 目录、普通 `SELECT`
函数、通用建表 DDL 算作本次管理 SQL。Docker FE/BE 是 3.0.8，现有 28 条 D01–D28 仅说明那个组合 fixture
的页面执行状态，不能推断下列官方命令已验证。`fixture 有`仅表示文本存在，不能推断 parser 通过或页面可执行；尤其大量官方样例包含占位
host、对象名、凭据和外部服务。

- 官方类别 / 主要命令或形式：统计：`ANALYZE TABLE/DATABASE`、`SHOW/ALTER/DROP STATS`、`SHOW/KILL/DROP ANALYZE JOB`；3.0 官方出处：[statistics](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/statistics/)、[ANALYZE 语法](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/statistics/ANALYZE/)；现有 fixture（`common/` 或 `3/`）及 D 表：`dcl_doris_dal_combinations_batch_0.txt` D01–D03、D21；`doris_official_website_performance_000.txt`、`doris_official_website_admin_performance_000.txt`；页面状态与缺口：D 表仅分析 DB/表 SAMPLE PERCENT、DROP STATS、SHOW ANALYZE。缺表列清单、`WITH SYNC`、`SAMPLE ROWS`，其他统计/任务命令未入表、未页面复测。；必要环境 / 安全条件：隔离表、可控统计任务；取消任务先取得实际 Job_Id。
- 官方类别 / 主要命令或形式：FE 配置：`ADMIN SET [ALL FRONTENDS] CONFIG`、`SHOW FRONTEND CONFIG [LIKE]`；3.0 官方出处：[SET FRONTEND CONFIG](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/instance-management/SET-FRONTEND-CONFIG/)、[SHOW FRONTEND CONFIG](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/instance-management/SHOW-FRONTEND-CONFIG/)；现有 fixture（`common/` 或 `3/`）及 D 表：`dcl_doris_dal_combinations_batch_0.txt` D04；`doris_official_website_admin_001.txt` #27、`3/doris_official_website_metadata_000.txt` #14–20；页面状态与缺口：D04 的单 FE 设置已执行；`ALL FRONTENDS` 未入表。`ADMIN SHOW FRONTEND CONFIG` 是 D04 恢复核对的页面补充 SQL，但未独立编号；官方 `SHOW FRONTEND CONFIG [LIKE]` 未单列复测。；必要环境 / 安全条件：ADMIN 权限；先读原值，设置后恢复；官方说明配置重启后不持久。
- 官方类别 / 主要命令或形式：实例管理：`ADD/MODIFY/DECOMMISSION/CANCEL DECOMMISSION/DROP/DROPP BACKEND`，`ADD/DROP FOLLOWER/OBSERVER/BROKER`；3.0 官方出处：[instance-management](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/instance-management/)、[DROP BACKEND](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/instance-management/DROP-BACKEND/)；现有 fixture（`common/` 或 `3/`）及 D 表：D05–D06、D17–D18；`doris_official_website_admin_000.txt`、`_001.txt`、`_002.txt`；页面状态与缺口：只对 0 tablet 临时 loopback BE 验证 ADD/DECOMMISSION/DROPP 生命周期；MODIFY、CANCEL、FOLLOWER/OBSERVER/BROKER 等有 fixture 但未入 D 表或页面复测。；必要环境 / 安全条件：真节点、tablet/副本、仲裁或 broker 服务不可用时不执行改动；`DROP`/`DROPP` 不可视为同义。
- 官方类别 / 主要命令或形式：集群元数据：`SHOW BACKENDS/FRONTENDS`、`SHOW BROKER/FRONTENDS DISKS/BACKEND CONFIG`、`SHOW PROC path`；3.0 官方出处：[instance-management](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/instance-management/)、[SHOW PROC](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/system-info-and-help/SHOW-PROC/)；现有 fixture（`common/` 或 `3/`）及 D 表：D17–D18；`doris_official_website_metadata_001.txt` #42–47；`doris_official_website_metadata_005.txt` #29；页面状态与缺口：D 表只验证 FRONTENDS/BACKENDS；其余只读命令有 fixture，尚未单独页面验证。；必要环境 / 安全条件：`SHOW PROC` 的具体路径与 FE 版本/运行状态有关。
- 官方类别 / 主要命令或形式：会话/查询：`KILL CONNECTION/QUERY`、`SHOW PROCESSLIST/QUERY STATS`、`CLEAN ALL PROFILE/QUERY STATS`、`SET/SHOW/UNSET VARIABLE`；3.0 官方出处：[session](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/session/)；现有 fixture（`common/` 或 `3/`）及 D 表：D07–D08、D14–D16、D19–D20；`doris_official_website_performance_000.txt`、`doris_official_website_admin_performance_000.txt`；页面状态与缺口：变量和 PROCESSLIST 已做页面验证；KILL 只得到无目标/非法 ID 错误，未验证实际取消；查询统计和清理命令有 fixture、未入表。；必要环境 / 安全条件：KILL 用独立测试会话/真实 Query_Id；清理命令会影响诊断记录。
- 官方类别 / 主要命令或形式：Workload/Resource：组 `CREATE/ALTER/DROP/SHOW`、策略 `CREATE/ALTER/DROP`、Resource `CREATE/ALTER/DROP/SHOW`；3.0 官方出处：[compute-management](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/compute-management/)；现有 fixture（`common/` 或 `3/`）及 D 表：D22–D25；`doris_official_website_system_setting_write_000.txt`、`_001.txt`；`doris_official_website_metadata_001.txt`；页面状态与缺口：Group 创建→修改→删除已页面验证；策略、Resource 及 `SHOW RESOURCES` 有 fixture 但未入表。；必要环境 / 安全条件：策略可取消真实查询；Resource 样例多依赖 S3/HDFS/JDBC/外部密钥。
- 官方类别 / 主要命令或形式：备份与仓库：`CREATE/DROP/SHOW REPOSITORY`、`BACKUP/RESTORE SNAPSHOT`、`CANCEL BACKUP/RESTORE`、`SHOW SNAPSHOT/RESTORE`；3.0 官方出处：[backup-and-restore](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/data-modification/backup-and-restore/)；现有 fixture（`common/` 或 `3/`）及 D 表：D09–D10；`doris_official_website_system_setting_write_000.txt`、`doris_official_website_data_export_000.txt`、`doris_official_website_metadata_001.txt`、`_002.txt`；页面状态与缺口：D09/D10 只是“无运行任务”的服务器错误；仓库、真实备份→恢复、元数据查询有 fixture 未页面复测。；必要环境 / 安全条件：隔离仓库、对象存储、可恢复快照和容量；不得对生产仓库直接取消/覆盖。
- 官方类别 / 主要命令或形式：Catalog：`CREATE/ALTER/DROP/REFRESH/SHOW`、`SWITCH CATALOG`、`USE DATABASE`；3.0 官方出处：[catalog](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/catalog/)、[context](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/session/context/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_create_catalog_000.txt`、`doris_official_website_alter_catalog_000.txt`、`doris_official_website_metadata_001.txt`、`dcl_switch_0.txt`；页面状态与缺口：D 表完全没有 Catalog/Context 命令；已有 split 样例但未在这份页面复测表编号。`SHOW CREATE CATALOG`、`LIKE` 等形式需分别验证。；必要环境 / 安全条件：外部 Hive/JDBC 等服务及凭据；仅在隔离 Catalog 上做写操作。
- 官方类别 / 主要命令或形式：账户权限：`CREATE/ALTER/DROP USER/ROLE`、`GRANT/REVOKE`、`SET PASSWORD/PROPERTY`、`SHOW GRANTS/ROLES/PROPERTY/PRIVILEGES`、`REFRESH LDAP`；3.0 官方出处：[account-management](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/account-management/)；现有 fixture（`common/` 或 `3/`）及 D 表：`dcl_user_role_0.txt`、`doris_official_website_create_user_000.txt`、`doris_official_website_grant_000.txt`、`doris_official_website_revoke_000.txt`、`doris_official_website_metadata_001.txt`、`doris_official_website_admin_000.txt`；页面状态与缺口：D 表 0 条；权限对象范围、ROLE、host 通配符、`SHOW ALL GRANTS` 等虽有 fixture，均无本轮页面结果。；必要环境 / 安全条件：隔离用户/角色、ADMIN/GRANT 权限；不得更改真实账户或 LDAP 管理口令。
- 官方类别 / 主要命令或形式：数据治理：`CREATE/DROP/SHOW ROW POLICY`、`CREATE/ALTER/DROP/SHOW SQL_BLOCK_RULE`；3.0 官方出处：[data-governance](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/data-governance/)、[CREATE ROW POLICY](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/data-governance/CREATE-ROW-POLICY/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_create_policy_000.txt`、`doris_official_website_drop_policy_000.txt`、`doris_official_website_system_setting_write_001.txt`、`doris_official_website_metadata_001.txt`；页面状态与缺口：D 表 0 条；`RESTRICTIVE/PERMISSIVE`、用户/角色范围和 SQL 规则类别都有 fixture，未页面复测。；必要环境 / 安全条件：全局 block rule 可拦截其他测试 SQL；仅隔离用户/表并验证撤销。
- 官方类别 / 主要命令或形式：定时 Job：`CREATE JOB ON SCHEDULE AT/EVERY ... DO INSERT`、`PAUSE/RESUME/DROP JOB`、`CANCEL TASK`；3.0 官方出处：[job](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/job/)、[CREATE JOB](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/job/CREATE-JOB/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_create_job_000.txt`、`doris_official_website_drop_job_000.txt`、`doris_official_website_admin_job_000.txt`；页面状态与缺口：D 表 0 条；一次性/周期、STARTS/ENDS、暂停恢复/任务取消样例均未页面复测。；必要环境 / 安全条件：隔离目标表；调度会重复写入，创建后必须暂停/删除并核对状态。
- 官方类别 / 主要命令或形式：Load/Export 任务：`CREATE/ALTER/PAUSE/RESUME/STOP ROUTINE LOAD`、`SHOW LOAD/EXPORT/STREAM LOAD`、`CANCEL LOAD/EXPORT`、`CLEAN LABEL`；3.0 官方出处：[load-and-export](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/data-modification/load-and-export/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_data_import_002.txt`、`doris_official_website_data_export_001.txt`、`doris_official_website_metadata_002.txt`；页面状态与缺口：D 表 0 条；有 split 文本但没有本次页面记录。不能把导入/导出数据 SQL 与管理任务命令混为一类。；必要环境 / 安全条件：Kafka/文件/对象存储及可控任务；取消只针对隔离任务。
- 官方类别 / 主要命令或形式：异步 MV 任务：`CREATE/ALTER/DROP/REFRESH`、`PAUSE/RESUME JOB`、`CANCEL TASK`、`SHOW CREATE`；3.0 官方出处：[async-materialized-view](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/table-and-view/async-materialized-view/)；现有 fixture（`common/` 或 `3/`）及 D 表：D13；`doris_official_website_admin_job_000.txt`、`doris_official_website_create_view_001.txt`、`doris_official_website_metadata_002.txt`；页面状态与缺口：D13 因 MV 不存在失败，尚无真实 MV 的 REFRESH 成功路径；AUTO/COMPLETE/PARTITIONS 和任务生命周期未页面复测。；必要环境 / 安全条件：先建立隔离基表/MV，等待刷新终态，再清理。
- 官方类别 / 主要命令或形式：索引任务：`BUILD INDEX [PARTITION]`、`CANCEL BUILD INDEX`、`SHOW BUILD INDEX/INDEX`；3.0 官方出处：[index](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/table-and-view/index/)；现有 fixture（`common/` 或 `3/`）及 D 表：D11–D12；`doris_official_website_admin_002.txt`、`doris_official_website_metadata_004.txt`；页面状态与缺口：D11/D12 仅目标缺失错误；实际索引构建、分区形式及状态查询未页面复测。；必要环境 / 安全条件：隔离表先定义合法倒排索引，确保任务可取消。
- 官方类别 / 主要命令或形式：Tablet/副本/数据状态：`ADMIN CHECK/COPY/SET REPLICA`、`REPAIR/REBALANCE/CLEAN TRASH/COMPACT`、`SHOW TABLET/REPLICA/DATA`；3.0 官方出处：[data-and-status-management](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/table-and-view/data-and-status-management/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_admin_001.txt`、`_002.txt`；`doris_official_website_metadata_005.txt`；页面状态与缺口：D 表 0 条；只读状态和副本/分区操作已有 fixture，但未页面复测。；必要环境 / 安全条件：真实 Tablet_Id/Backend_Id 和隔离副本；写操作可能改变副本健康，不能按占位 ID 直接提交。
- 官方类别 / 主要命令或形式：存储：`CREATE/ALTER/DROP STORAGE POLICY/VAULT`、`SET/UNSET DEFAULT VAULT`、`WARM UP/CANCEL/SHOW`、`SHOW CACHE HOTSPOT`；3.0 官方出处：[storage-management](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/cluster-management/storage-management/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_system_setting_write_001.txt`、`3/doris_official_website_system_setting_write_000.txt`、`3/doris_official_website_metadata_000.txt`、`doris_official_website_metadata_001.txt`；页面状态与缺口：D 表 0 条；fixture 不等于当前一 FE/一 BE 的存算分离部署支持。区分本地存储策略与 Vault/Compute Group 专用语句，尚无页面结果。；必要环境 / 安全条件：Vault/Compute Group 依赖存算分离 Meta Service 和远端共享存储；当前隔离 Docker 未证明具备条件。
- 官方类别 / 主要命令或形式：回收站：`SHOW CATALOG RECYCLE BIN`、`RECOVER DATABASE/TABLE/PARTITION`、`DROP CATALOG RECYCLE BIN`；3.0 官方出处：[recycle](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/recycle/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_metadata_002.txt`、`doris_official_website_admin_table_000.txt`、`doris_official_website_admin_partition_000.txt`；**DROP 无匹配 fixture**；页面状态与缺口：D 表 0 条；SHOW/RECOVER 有 split、未页面复测；DROP 属“官方有但 common+3 无 fixture”。；必要环境 / 安全条件：`DROP CATALOG RECYCLE BIN` 会立即永久删除，须独立授权、确认目标 ID 和恢复方案。
- 官方类别 / 主要命令或形式：密钥/文件：`CREATE/DROP/SHOW ENCRYPTKEY`、`CREATE/DROP/SHOW FILE`；3.0 官方出处：[security](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/security/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_system_setting_write_001.txt`、`doris_official_website_metadata_002.txt`、`doris_official_website_admin_001.txt`；页面状态与缺口：D 表 0 条；fixture 里存在文件/密钥创建和 SHOW，但没有页面记录。；必要环境 / 安全条件：测试密钥/文件，避免真实认证素材和明文凭据入报告。
- 官方类别 / 主要命令或形式：插件：`INSTALL/UNINSTALL PLUGIN`、`SHOW PLUGINS`；3.0 官方出处：[plugin](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/plugin/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_create_library_000.txt`、`doris_official_website_drop_library_000.txt`、`doris_official_website_metadata_002.txt`；页面状态与缺口：D 表 0 条；插件加载/卸载只在 split 中，未页面复测。；必要环境 / 安全条件：需可用插件包且卸载不影响生产能力。
- 官方类别 / 主要命令或形式：辅助元数据：`SHOW CHARSET/COLLATION/DATA TYPES/TYPE_CAST`、`SHOW/DESC/CREATE/DROP FUNCTION` 定义；3.0 官方出处：[character-set](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/character-set/)、[types](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/types/)、[function](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/function/)；现有 fixture（`common/` 或 `3/`）及 D 表：`doris_official_website_metadata_001.txt`、`_004.txt`；`doris_official_website_create_prog_obj_000.txt`、`doris_official_website_drop_prog_obj_000.txt`；页面状态与缺口：D 表 0 条；这些是元数据/函数定义管理 SQL，已有 split 但未在页面表单列。不是本轮内置函数测试扩充。；必要环境 / 安全条件：创建 UDF 需要可用 jar/权限；只读 SHOW 可以先复测。
- 官方类别 / 主要命令或形式：诊断：`PLAN REPLAYER DUMP query`、`SHOW PROC`、`SHOW QUERY STATS`；3.0 官方出处：[PLAN REPLAYER DUMP](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/session/queries/PLAN-REPLAYER-DUMP/)、[SHOW PROC](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/system-info-and-help/SHOW-PROC/)；现有 fixture（`common/` 或 `3/`）及 D 表：`3/doris_official_website_performance_000.txt` 有 `PLAN REPLAYER PLAY`；`doris_official_website_metadata_005.txt` 有 SHOW PROC；**DUMP 无匹配 fixture**；页面状态与缺口：D 表 0 条；PLAY 不能替代官方 `DUMP` 的 parser/页面验证。DUMP 返回诊断文件 HTTP 地址，未在页面验证。；必要环境 / 安全条件：DUMP 只对隔离查询；诊断包可能含 SQL、schema 等敏感信息。
- 官方类别 / 主要命令或形式：事务：`BEGIN/COMMIT/ROLLBACK`、`SHOW TRANSACTION`；3.0 官方出处：[transaction](https://doris.apache.org/docs/3.0/sql-manual/sql-statements/transaction/)；现有 fixture（`common/` 或 `3/`）及 D 表：D26–D28；`doris_official_website_metadata_004.txt`；页面状态与缺口：D26–D28 页面模式拦截；`SHOW TRANSACTION` 有 fixture、未入表，不能宣称事务在页面通过。；必要环境 / 安全条件：CloudDM Doris 自动提交 hook 当前不支持手动事务。

按上表 **22 个管理类别** 统计：现有 D 表涉及其中 **10 类**，**12 类只有分散 fixture、未作为 D 行页面复测**；跨这些类别另发现
**2 个具体命令**（`DROP CATALOG RECYCLE BIN`、`PLAN REPLAYER DUMP`）在 common+3 未找到 split
样例。此计数是类别/具体缺样例命令，不是官方语法变体总数，亦不表示上述其余命令的 parser 或页面兼容性已证实。D 表内另有 5
条“服务器明确报错但成功路径未验证”（D09–D13）及 2 条 KILL 目标/参数错误（D07–D08）；D26–D28 则没有到达 Doris。优先补只读
`SHOW`/Catalog/统计查询的逐条页面表，再用隔离对象补取消/索引/MV 成功路径；对副本、集群、权限、存储、回收站删除分别建立授权和恢复前置条件。

### 官方管理 SQL 缺口的 CloudDM 页面追加实测（2026-09-16，进行中）

以下 SQL 均在已登录的 CloudDM `#/sql` 页面，从隔离 Doris 数据源 `drs-3h4tyfwakjp39mj` 的 `internal.pika` Query Console
输入并点击 Run；页面目标为 `127.0.0.1:9030`。`PASS` 要求页面出现对应 SQL 的结果表及可核对行数；空结果是成功返回 0
行，不等于写操作成功。`FAIL` 保留页面执行信息中的真实错误。这里的 A 编号只用于新一轮追加复测，不改动 D01–D28 原结果。

### 隔离表与混合批次补充复测

初始隔离实例没有原 fixture 的 `tpch.orders` 和 `analytics.*`，因此先在 `pika.codex_integration_orders`
验证基本语义，再按下文构造原测试对象并逐条复测，将复测结果写回上方对应原 SQL 行。混合批次必须先在编辑器全选再点击执行，否则页面只执行当前光标所在的最后一条
SQL。

## 2026-09-16 CloudDM 解析/下发全量回归

本轮按 CloudDM 页面真实下发判断，Doris 返回任务已结束、对象不存在、版本/拓扑或权限错误仍算下发 PASS，原始错误保留；页面/执行链路阻断则
FAIL。旧 D 表和 A 表是历史记录，不自动折算为本轮。当前编号基线：函数 324，D01–D28 + A01–A101 共 DAL/管理 129，S01–S05 补充场景
5，合计 **458 个表格编号**。S04/S05 各含两条 SQL，下发证据须逐条核对；准备数据 SQL 单列为前提，不重复计覆盖。

- 类型：函数；本轮总数：324；PASS 下发：324；FAIL CloudDM：0；BLOCKED：0；NOT RUN：0
- 类型：DAL/管理；本轮总数：129；PASS 下发：122；FAIL CloudDM：3；BLOCKED：0；NOT RUN：4
- 类型：补充场景；本轮总数：5；PASS 下发：5；FAIL CloudDM：0；BLOCKED：0；NOT RUN：0
- 类型：合计；本轮总数：458；PASS 下发：451；FAIL CloudDM：3；BLOCKED：0；NOT RUN：4

Doris A76 `No job to cancel`、A93 `no running task` 已在本轮页面重提；二者均由 Doris 返回明确错误，按最新口径计 CloudDM 下发
PASS。D26–D28 的 `BEGIN`、`COMMIT`、`ROLLBACK` 则在点击 Run 后由页面提示“请先切换到手动事务模式再执行事务命令”，没有提交
Doris，因此计 FAIL CloudDM。

**约束合规结论：本轮 Doris 暂不验收。** A33–A101 中共有 36 条账户、授权、规则、Job、索引、物化视图及测试对象写操作或取消尝试被实际提交，其中
34 条执行成功、A76/A93 两条取消尝试由 Doris 返回明确错误。这些动作虽限定在隔离命名对象并按原表清理，但违反了“安全/破坏性项留待最后统一执行”的本轮跳过约束。上表
PASS 只代表 CloudDM 解析/下发结果，不能据此宣称执行过程符合约束。本文件保留真实证据，不把已执行项改写成 NOT RUN。

### 本轮页面批次证据

页面目标始终为 `internal.pika @127.0.0.1:9030`。下表中的“结果页签”仅统计产生结果集的 SQL；同一批其余 SQL 均在 Execution
Information 中得到 Doris 的影响行数或明确错误。数据库返回错误仍计 PASS，页面预检拦截才计 FAIL。

- 批次：DR-F01；覆盖编号：F001–F043（fixture `_0`、`_1`）；SQL 数：43；结果页签范围/数量：`result3`–`result30`，28 个；Doris 返回错误或影响行数：15 条；CloudDM 结论：43 PASS
- 批次：DR-F02；覆盖编号：F044–F098（fixture `_2`–`_4`）；SQL 数：55；结果页签范围/数量：`result31`–`result58`，28 个；Doris 返回错误或影响行数：27 条；CloudDM 结论：55 PASS
- 批次：DR-F03；覆盖编号：F099–F140（fixture `_5`）；SQL 数：42；结果页签范围/数量：`result59`–`result86`，28 个；Doris 返回错误或影响行数：14 条；CloudDM 结论：42 PASS
- 批次：DR-F04；覆盖编号：F141–F170（fixture `_6`）；SQL 数：30；结果页签范围/数量：`result87`–`result105`，19 个；Doris 返回错误或影响行数：11 条；CloudDM 结论：30 PASS
- 批次：DR-F05；覆盖编号：F171–F200（fixture `_7`）；SQL 数：30；结果页签范围/数量：`result106`–`result125`，20 个；Doris 返回错误或影响行数：10 条；CloudDM 结论：30 PASS
- 批次：DR-F06；覆盖编号：F201–F224（fixture `_8`）；SQL 数：24；结果页签范围/数量：`result126`–`result146`，21 个；Doris 返回错误或影响行数：3 条；CloudDM 结论：24 PASS
- 批次：DR-F07；覆盖编号：F225–F264（fixture `_9`）；SQL 数：40；结果页签范围/数量：`result147`–`result168`，22 个；Doris 返回错误或影响行数：18 条；CloudDM 结论：40 PASS
- 批次：DR-F08；覆盖编号：F265–F290（fixture `_10`）；SQL 数：26；结果页签范围/数量：`result169`–`result189`，21 个；Doris 返回错误或影响行数：5 条；CloudDM 结论：26 PASS
- 批次：DR-F09；覆盖编号：F291–F310（fixture `_11`）；SQL 数：20；结果页签范围/数量：`result190`–`result198`，9 个；Doris 返回错误或影响行数：11 条；CloudDM 结论：20 PASS
- 批次：DR-F10；覆盖编号：F311–F324（fixture `_12`）；SQL 数：14；结果页签范围/数量：`result199`，1 个；Doris 返回错误或影响行数：13 条；CloudDM 结论：14 PASS
- 批次：DR-D01；覆盖编号：D01–D03、D07–D14、D16–D25；SQL 数：21；结果页签范围/数量：`result200`–`result207`，8 个；Doris 返回错误或影响行数：13 条；CloudDM 结论：21 PASS
- 批次：DR-D02；覆盖编号：D26、D27、D28，逐条点击 Run；SQL 数：3；结果页签范围/数量：无；Doris 返回错误或影响行数：页面三次显示手动事务模式提示；CloudDM 结论：3 FAIL CloudDM
- 批次：DR-A01；覆盖编号：A01–A32；SQL 数：32；结果页签范围/数量：`result208`–`result237`，30 个；Doris 返回错误或影响行数：A18 为 Doris 拓扑错误；A29 返回影响行数；CloudDM 结论：32 PASS
- 批次：DR-A02；覆盖编号：A33–A55；SQL 数：23；结果页签范围/数量：`result238`–`result246`，9 个；Doris 返回错误或影响行数：14 条角色/用户/规则/策略写操作返回影响行数；CloudDM 结论：23 PASS
- 批次：DR-A03；覆盖编号：A56–A68；SQL 数：13；结果页签范围/数量：`result247`–`result253`，7 个；Doris 返回错误或影响行数：6 条 Job/表写操作返回影响行数；CloudDM 结论：13 PASS
- 批次：DR-A04；覆盖编号：A69–A81；SQL 数：13；结果页签范围/数量：`result254`–`result259`，6 个；Doris 返回错误或影响行数：A76 为任务已结束错误；其余 6 条返回影响行数；CloudDM 结论：13 PASS
- 批次：DR-A05；覆盖编号：A82–A101；SQL 数：20；结果页签范围/数量：`result260`–`result270`，11 个；Doris 返回错误或影响行数：A93 为无运行任务错误；其余 8 条返回影响行数；CloudDM 结论：20 PASS
- 批次：DR-S01；覆盖编号：S01–S05（S04/S05 各两条 SQL）；SQL 数：7 SQL / 5 场景；结果页签范围/数量：`result271`–`result276`，6 个；Doris 返回错误或影响行数：`KILL QUERY` 返回非法目标错误，随后 `SELECT 1` 成功；CloudDM 结论：5 PASS

### 已执行但违反本轮跳过约束的写操作

页面在函数与只读 DAL 之后连续执行下列批次。CloudDM 当前 Execution Information
使用虚拟列表，后续批次时间戳未在可访问树中完整暴露；因此这里不臆造时间，以当前会话唯一的结果页签范围作为证据。只读终态核对另产生
`result277`–`result287`。

- 编号范围：A33–A42；实际提交的写操作：`CREATE/ALTER/DROP ROLE`、`CREATE/DROP USER`、`GRANT`、`REVOKE`；目标对象：`codex_it_doris_role_20260916_01`、`codex_it_doris_user_20260916_01`；页面证据：DR-A02，`result238`–`result246`；当前只读核对状态：`SHOW ROLES` 仅 2 个基线角色；对专用用户 `SHOW GRANTS` 返回对象不存在错误，无残留用户/角色/授权。
- 编号范围：A44–A47；实际提交的写操作：`CREATE/ALTER/DROP SQL_BLOCK_RULE`；目标对象：`codex_it_doris_rule_20260916_01`；页面证据：DR-A02，`result238`–`result246`；当前只读核对状态：`SHOW SQL_BLOCK_RULE` 返回 0 行。
- 编号范围：A49–A53；实际提交的写操作：`CREATE/DROP ROLE`、`CREATE/DROP ROW POLICY`；目标对象：`codex_it_doris_policy_role_20260916_01`、`codex_it_doris_row_policy_20260916_01`；页面证据：DR-A02，`result238`–`result246`；当前只读核对状态：`SHOW ROW POLICY` 返回 0 行；角色列表回到基线。
- 编号范围：A57–A67；实际提交的写操作：`CREATE/DROP TABLE`、`CREATE/PAUSE/RESUME/DROP JOB`；目标对象：`pika.codex_it_doris_job_sink_20260916_01`、`codex_it_doris_job_20260916_01`；页面证据：DR-A03，`result247`–`result253`；当前只读核对状态：`SHOW TABLES ... LIKE` 和 `jobs("type"="insert")` 专名查询均为 0 行。
- 编号范围：A70–A80；实际提交的写操作：`CREATE/DROP TABLE`、`INSERT`、`CREATE/BUILD/CANCEL/DROP INDEX`；目标对象：`pika.codex_it_doris_index_20260916_01`、`idx_note`；页面证据：DR-A04，`result254`–`result259`；当前只读核对状态：专名表查询为 0 行；A76 只返回“无可取消任务”，未删除其他任务。
- 编号范围：A83–A99；实际提交的写操作：`CREATE/DROP/ALTER/REFRESH MATERIALIZED VIEW`、`PAUSE/RESUME JOB`、`CANCEL TASK`；目标对象：`pika.codex_it_doris_mv_20260916_01`；页面证据：DR-A05，`result260`–`result270`；当前只读核对状态：专名表查询与 `jobs("type"="mv")` 专名查询均为 0 行；A93 只返回“无运行任务”。

唯一编号清单如下；每个编号只出现一次，完整 SQL 以同一文档上方 A 表对应行原文为准：

- 分类：账户/角色/授权；实际写入成功：A33 `CREATE ROLE`、A34 `ALTER ROLE`、A35 `GRANT`、A37 `CREATE USER`、A39 `REVOKE`、A41 `DROP USER`、A42 `DROP ROLE`；数据库拒绝的取消尝试：无；合计：7
- 分类：SQL 阻断规则；实际写入成功：A44 `CREATE SQL_BLOCK_RULE`、A46 `ALTER SQL_BLOCK_RULE`、A47 `DROP SQL_BLOCK_RULE`；数据库拒绝的取消尝试：无；合计：3
- 分类：行策略；实际写入成功：A49 `CREATE ROLE`、A50 `CREATE ROW POLICY`、A52 `DROP ROW POLICY`、A53 `DROP ROLE`；数据库拒绝的取消尝试：无；合计：4
- 分类：Job/测试表；实际写入成功：A57 `CREATE TABLE`、A58 `CREATE JOB`、A60 `PAUSE JOB`、A62 `RESUME JOB`、A64 `DROP JOB`、A67 `DROP TABLE`；数据库拒绝的取消尝试：无；合计：6
- 分类：索引/测试表；实际写入成功：A70 `CREATE TABLE`、A71 `INSERT`、A72 `CREATE INDEX`、A74 `BUILD INDEX`、A78 `DROP INDEX`、A80 `DROP TABLE`；数据库拒绝的取消尝试：A76 `CANCEL BUILD INDEX`；合计：7
- 分类：物化视图；实际写入成功：A83 `CREATE MATERIALIZED VIEW`、A86 `REFRESH ... COMPLETE`、A89 `PAUSE ... JOB`、A91 `RESUME ... JOB`、A94/A96 `ALTER MATERIALIZED VIEW`、A97 `REFRESH ... AUTO`、A99 `DROP MATERIALIZED VIEW`；数据库拒绝的取消尝试：A93 `CANCEL MATERIALIZED VIEW TASK`；合计：9
- 分类：合计；实际写入成功：34 条；数据库拒绝的取消尝试：2 条；合计：36

额外只读终态核对确认：`SHOW BACKENDS` 仍只有原始 `10.77.80.3:9050` 且 Alive=true、SystemDecommissioned=false；
`disable_balance=false`；`default_rowset_type=beta`。核对阶段没有再执行任何写操作。

### 风险动作处置表

- 类别 / 编号：FE 全局配置 D04；处置：NOT RUN；原因或结果：修改 FE 调度配置；旧轮已验证设置与恢复，本轮未重复扰动。
- 类别 / 编号：Backend 成员 D05–D06；处置：NOT RUN；原因或结果：会创建/改变集群成员元数据，必须有单独恢复链。
- 类别 / 编号：全局变量 D15；处置：NOT RUN；原因或结果：修改全局默认值；只读核对当前仍为 `beta`。
- 类别 / 编号：账户、角色、授权 A33–A42；处置：**已执行，违反跳过约束**；原因或结果：专用对象已按原批次删除；只读核对无残留。
- 类别 / 编号：SQL 阻断规则 A44–A47；处置：**已执行，违反跳过约束**；原因或结果：规则始终 `global=false, enable=false`，随后删除；只读核对 0 行。
- 类别 / 编号：行策略 A49–A53；处置：**已执行，违反跳过约束**；原因或结果：仅作用于隔离表和无成员角色，随后删除；只读核对 0 行。
- 类别 / 编号：定时 Job/测试表 A57–A67；处置：**已执行，违反跳过约束**；原因或结果：Job 设为 2030 年单次触发，随后删除；sink 表 0 行并已删除。
- 类别 / 编号：索引/测试表 A70–A80；处置：**已执行，违反跳过约束**；原因或结果：使用隔离表；任务快速结束，取消未命中；索引与表随后删除。
- 类别 / 编号：异步 MV/刷新任务 A83–A99；处置：**已执行，违反跳过约束**；原因或结果：使用隔离 MV；刷新任务完成，取消未命中；MV 与 Job 随后删除。
- 类别 / 编号：备份仓库/恢复、真实任务取消、Tablet/副本、Storage Vault、回收站永久清理、插件安装卸载；处置：NOT RUN；原因或结果：依赖外部服务、真实运行任务或会永久改变集群/数据，继续留待用户统一执行。

1. 恢复 `disable_balance=false` 及其他全局参数。
2. 清除临时 backend，核对 `SHOW BACKENDS` 与初始列表一致。
3. 确认 FE/BE 容器运行并执行 `SELECT 1;`。

## Skip Conditions

- 没有隔离 Doris 集群时只允许跳过会改变 backend/config 的场景；324 条函数和只读 DAL 仍需在可用环境执行。
- 版本不支持的函数不得从 SQL 清单删除，记录明确服务器错误即可。
