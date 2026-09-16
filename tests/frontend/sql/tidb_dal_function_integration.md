# TiDB DAL 与函数集成验证

## SQL 执行明细

判定口径：CloudDM 能解析并将 SQL 发送到数据库即为通过；数据库返回错误不等于 CloudDM 失败。安全或破坏性操作保留待用户执行状态。

验证状态统一为：通过、CloudDM 失败、未执行、证据不足。通过仅表示已有证据确认 SQL 被解析并发送到数据库，数据库报错保留在执行证据中。历史执行与复测记录均保留，每行反映该次记录，不代表相同 SQL 的最新复测结论。

本表记录数：通过 957；CloudDM 失败 9；未执行 1；证据不足 1。包含重复执行记录，不等同于去重用例数。本次为文档证据整理，未重新执行 SQL。

| 编号 | SQL | 验证状态 | 实际结果/执行证据 | 前提/预期 | 来源/备注 |
| --- | --- | --- | --- | --- | --- |
| 1 | `SET CONFIG "127.0.0.1:20180" split.qps-threshold=1000;` | 通过 | 进入 TiDB 执行端，返回数据库错误 `instance 127.0.0.1:20180 is not found in this cluster`；没有解析错误或重写 `NullPointerException`。该地址是隔离环境中不存在的目标，不会修改配置。 | — | — |
| 2 | `SELECT 1;` | 通过 | 返回 `1`，证明上一条服务器错误后仍可查询。 | — | — |
| 3 | `SELECT VERSION();` | 通过 | 返回 1 行，数据源为 `codex-it-tidb-8-5 / catalog1`。 | — | — |
| 4 | `SELECT * FROM catalog1.orders ORDER BY id;` | 通过 | 返回 3 行；执行信息显示自动追加 `LIMIT 1000`。 | — | — |
| 5 | `SELECT * FROM codex_integration.orders ORDER BY id;` | 通过 | 返回 3 行；执行信息显示自动追加 `LIMIT 1000`。 | — | — |
| 6 | `SHOW ENGINES;` + 换行 + `SELECT * FROM catalog1.orders ORDER BY id;`（全选同时执行） | 通过 | 同批次 2 条执行信息：SHOW 返回 1 行，SELECT 返回 3 行并追加 `LIMIT 1000`；未见重写空指针。 | — | — |
| 7 | `` SELECT IFNULL(NULL, 1), NULLIF(1, 1), IF(TRUE, 'yes', 'no'); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 8 | `` SELECT CONCAT('Ti', 'DB'), REGEXP_REPLACE('abc123', '[0-9]+', ''), CHAR_LENGTH('数据库'); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 9 | `` SELECT ABS(-1), ROUND(12.345, 2), CONV('ff', 16, 10), RAND(); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 10 | `` SELECT DATE_ADD('2026-01-01', INTERVAL 1 DAY), TIMESTAMPDIFF(DAY, '2026-01-01', '2026-01-03'); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 11 | `` SELECT JSON_EXTRACT('{"a":1}', '$.a'), JSON_ARRAY(1, 2), JSON_OBJECT('a', 1); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 12 | `` SELECT SHA2('tidb', 256), COMPRESS('tidb'), UNCOMPRESS(COMPRESS('tidb')); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 13 | `` SELECT BIT_COUNT(7), 1 << 3, 8 >> 2; `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 14 | `` SELECT DATABASE(), CURRENT_USER(), CONNECTION_ID(), TIDB_VERSION(); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 15 | `` SELECT COUNT(*), SUM(amount), AVG(amount), MIN(amount), MAX(amount) FROM catalog1.orders; `` | 通过 | 1 行结果；页面执行信息已核实，查询自动追加 LIMIT 1000 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 16 | `` SELECT ROW_NUMBER() OVER (ORDER BY id), LAG(amount) OVER (ORDER BY id) FROM catalog1.orders; `` | 通过 | 3 行结果；页面执行信息已核实，查询自动追加 LIMIT 1000 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 17 | `` SELECT GET_LOCK('dal_lock', 1), RELEASE_LOCK('dal_lock'); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 18 | `` SELECT CAST('42' AS SIGNED), CONVERT('abc' USING utf8mb4); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dql_tidb_function_combinations_0.txt` |
| 19 | `` SELECT COALESCE(NULL, NULL, 'fallback'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 20 | `` SELECT ISNULL(NULL); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 21 | `` SELECT JSON_MERGE('{"a": 1}', '{"b": 2}'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 22 | `` SELECT JSON_PRETTY('{"person": {"name": "TiDB"}}'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 23 | `` SELECT POWER(2, 8); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 24 | `` SELECT REVERSE('TiDB'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 25 | `` SELECT SPACE(3); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 26 | `` SELECT STR_TO_DATE('2026-09-11', '%Y-%m-%d'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 27 | `` SELECT TIDB_DECODE_BINARY_PLAN('binary-plan'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 28 | `` SELECT TIDB_MVCC_INFO('74800000000000007f5f698000000000000001'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 29 | `` SELECT TRANSLATE('abcdef', 'ace', 'xyz'); `` | 通过 | 1 行结果；页面执行信息已核实，未逐项核查返回值 | — | 来源：`dql_tidb_function_official_gaps_1.txt` |
| 30 | `` ANALYZE TABLE catalog1.orders; `` | 通过 | 0 rows affected | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 31 | `` PREPARE stmt1 FROM 'SELECT * FROM catalog1.orders WHERE id = ?'; `` | 通过 | 0 rows affected | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 32 | `` EXECUTE stmt1 USING @id; `` | 通过 | 0 行查询结果（@id 未设置） | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 33 | `` DEALLOCATE PREPARE stmt1; `` | 通过 | 0 rows affected | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 34 | `` EXPLAIN ANALYZE SELECT * FROM catalog1.orders; `` | 通过 | 2 行结果 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 35 | `` ADMIN SHOW DDL; `` | 通过 | 1 行结果 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 36 | `` ADMIN CHECK TABLE catalog1.orders; `` | 通过 | 0 rows affected | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 37 | `` ADMIN CHECKSUM TABLE catalog1.orders; `` | 通过 | 1 行结果 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 38 | `` SHOW CONFIG; `` | 通过 | 200 行结果（分批取回） | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 39 | `` SHOW BUILTINS; `` | 通过 | 303 行结果（分批取回） | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 40 | `` SHOW PROCESSLIST; `` | 通过 | 1 行结果 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 41 | `` SHOW STATS_HEALTHY; `` | 通过 | 2 行结果 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 42 | `` SET @@SESSION.tidb_mem_quota_query = 1073741824; `` | 通过 | 0 rows affected；原值同为 1073741824 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 43 | `` SET CONFIG tikv `raftstore.raft-log-gc-count-limit` = 128000; `` | 通过 | 0 rows affected；需核对集群配置最终值 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 44 | `` FLUSH TABLES catalog1.orders WITH READ LOCK; `` | 通过 | 数据库错误：TiDB 不支持 FLUSH TABLES WITH READ LOCK，未加锁 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 45 | `` FLUSH STATUS; `` | 通过 | 0 rows affected | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 46 | `` KILL TIDB QUERY 42; `` | 通过 | 0 rows affected；无效线程未报告错误 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 47 | `` CACHE INDEX catalog1.orders KEY (`PRIMARY`) IN cache_schema; `` | 通过 | 数据库错误：TiDB 8.5.3 不接受 CACHE INDEX 语法 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 48 | `` LOAD INDEX INTO CACHE catalog1.orders KEY (`PRIMARY`) IGNORE LEAVES; `` | 通过 | 数据库错误：TiDB 8.5.3 不接受 LOAD INDEX INTO CACHE 语法 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 49 | `` RESET QUERY CACHE; `` | 通过 | 数据库错误：TiDB 8.5.3 不接受 RESET QUERY CACHE 语法 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 50 | `` START TRANSACTION READ ONLY; `` | 通过 | 手动事务下数据库错误：READ ONLY 为 noop，未启用；自动模式先提示切换事务模式 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 51 | `` SAVEPOINT dal_savepoint; `` | 通过 | 手动事务：0 rows affected；自动模式先提示切换 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 52 | `` ROLLBACK TO SAVEPOINT dal_savepoint; `` | 通过 | 手动事务：0 rows affected；自动模式先提示切换 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 53 | `` RELEASE SAVEPOINT dal_savepoint; `` | 通过 | 手动事务：0 rows affected；自动模式先提示切换 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 54 | `` COMMIT; `` | 通过 | 手动事务：0 rows affected；自动模式先提示切换 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 55 | `` SHUTDOWN; `` | 通过 | 页面 0 rows affected；状态端口随后不可达、原连接 EOF；精确重启隔离容器并刷新后，手动事务仍报连接已关闭，切回自动事务再执行 `SELECT 1;` 返回 1 行 | — | 来源：`dcl_tidb_dal_combinations_batch_0.txt` |
| 56 | `` SHOW ERRORS; `` | 通过 | 0 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 57 | `` SHOW ERRORS LIKE '42%'; `` | 通过 | 0 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 58 | `` SHOW ERRORS WHERE Code > 1000; `` | 通过 | 0 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 59 | `` SHOW ENGINES; `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 60 | `` SHOW ENGINES LIKE 'InnoDB'; `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 61 | `` SHOW ENGINES LIKE CONCAT('Inno', 'DB'); `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 62 | `` SHOW ENGINES WHERE Support = 'DEFAULT'; `` | 通过 | 1 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 63 | `` SHOW WARNINGS LIKE 'Warning'; `` | 通过 | 0 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 64 | `` SHOW WARNINGS WHERE Code > 1000; `` | 通过 | 0 行结果；页面执行信息已核实 | — | 来源：`dcl_tidb_dal_official_gaps_1.txt` |
| 65 | `` SET CONFIG tikv `split.qps-threshold`=1000; `` | 通过 | 0 rows affected；预检查 PD/TiKV 目标为 0 行 | — | 来源：`admin_set_config_0.txt` |
| 66 | `` SET CONFIG "127.0.0.1:20180" `split.qps-threshold`=1000; `` | 通过 | 数据库错误：该实例不在隔离集群 | — | 来源：`admin_set_config_0.txt` |
| 67 | `` SET CONFIG pd `log.level`='info'; `` | 通过 | 0 rows affected；预检查 PD/TiKV 目标为 0 行 | — | 来源：`admin_set_config_0.txt` |
| 68 | `` SET CONFIG tikv gc.enable-compaction-filter = true; `` | 通过 | 0 rows affected；预检查 PD/TiKV 目标为 0 行 | — | 来源：`admin_set_config_0.txt` |
| 69 | `` WITH data AS (SELECT NULL AS x UNION ALL SELECT 1 ) SELECT x, IFNULL(x,'x has no value') FROM data; `` | 通过 | 2 行结果；页面执行信息核实，原 fixture 在 WITH 与 SELECT 间换行 | — | — |
| 70 | `` WITH RECURSIVE d AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM d WHERE n<10) SELECT n, NULLIF(n+n, n+2) FROM d; `` | 通过 | 10 行结果；页面执行信息核实，原 fixture 在 WITH 与 SELECT 间换行 | — | — |
| 71 | `` WITH RECURSIVE d AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM d WHERE n<10) SELECT n, CASE WHEN n MOD 2 THEN "odd" ELSE "even" END FROM d; `` | 通过 | 10 行结果；页面执行信息核实，原 fixture 在 WITH 与 SELECT 间换行 | — | — |
| 72 | `` WITH RECURSIVE d AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM d WHERE n<10) SELECT n, IF(n MOD 2, "odd", "even") FROM d; `` | 通过 | 10 行结果；页面执行信息核实，原 fixture 在 WITH 与 SELECT 间换行 | — | — |
| 73 | `` SELECT JSON_ARRAY(1,2,3,4,5), JSON_ARRAY("foo", "bar"); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 74 | `` SELECT JSON_OBJECT("database", "TiDB", "distributed", TRUE); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 75 | `` SELECT JSON_QUOTE('The name is "O\'Neil"'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 76 | `` SELECT VALIDATE_PASSWORD_STRENGTH('abcdef'); `` | 通过 | 1 行结果 | — | — |
| 77 | `` SET GLOBAL validate_password.enable=ON; `` | 未执行 | 0 rows affected；页面确认开关由 OFF 变为 ON，恢复 OFF 待用户即时确认 | — | — |
| 78 | `` SELECT MD5('abc'); `` | 通过 | 1 行结果 | — | — |
| 79 | `` SELECT VALIDATE_PASSWORD_STRENGTH('abcdefghi'); `` | 通过 | 1 行结果 | — | — |
| 80 | `` SELECT UNCOMPRESS(0x03000000789C72747206040000FFFF018D00C7); `` | 通过 | 1 行结果 | — | — |
| 81 | `` SELECT SHA2('abc',224); `` | 通过 | 1 行结果 | — | — |
| 82 | `` SELECT AES_DECRYPT(0x28409970815CD536428876175F1A4923, 'secret'); `` | 通过 | 1 行结果 | — | — |
| 83 | `` SELECT VALIDATE_PASSWORD_STRENGTH('Abcdefghi123'); `` | 通过 | 1 行结果 | — | — |
| 84 | `` SHOW VARIABLES LIKE 'validate_password.%'; `` | 通过 | 8 行结果 | — | — |
| 85 | `` SELECT VALIDATE_PASSWORD_STRENGTH('Abcdefghi123%$#'); `` | 通过 | 1 行结果 | — | — |
| 86 | `` SELECT SM3('abc'); `` | 通过 | 1 行结果 | — | — |
| 87 | `` WITH x AS (SELECT REPEAT('a',100) 'a') SELECT LENGTH(a),LENGTH(COMPRESS(a)) FROM x; `` | 通过 | 1 行结果 | — | — |
| 88 | `` SELECT UNCOMPRESSED_LENGTH(0x03000000789C72747206040000FFFF018D00C7); `` | 通过 | 1 行结果 | — | — |
| 89 | `` SELECT COMPRESS(0x414243); `` | 通过 | 1 行结果 | — | — |
| 90 | `` SELECT VALIDATE_PASSWORD_STRENGTH('Abcdefghi'); `` | 通过 | 1 行结果 | — | — |
| 91 | `` SELECT AES_ENCRYPT(0x616263,'secret'); `` | 通过 | 1 行结果 | — | — |
| 92 | `` SELECT SHA1('abc'); `` | 通过 | 1 行结果 | — | — |
| 93 | `` SELECT RANDOM_BYTES(3); `` | 通过 | 1 行结果 | — | — |
| 94 | `` SELECT VALIDATE_PASSWORD_STRENGTH(''); `` | 通过 | 1 行结果 | — | — |
| 95 | `` SELECT INET_NTOA(INET_ATON('192.168.1.2') & INET_ATON('255.255.255.0')); `` | 通过 | 1 行结果 | — | — |
| 96 | `` SELECT BIT_COUNT(b'00101001'); `` | 通过 | 1 行结果 | — | — |
| 97 | `` SELECT CONV(b'1010' & b'1000',10,2); `` | 通过 | 1 行结果 | — | — |
| 98 | `` WITH RECURSIVE cte(n) AS ( SELECT 0 AS n UNION ALL SELECT n+1 FROM cte WHERE n<11 ) SELECT n,1024>>n,LPAD(CONV(1024>>n,10,2),11,0) FROM cte; `` | 通过 | 12 行结果 | — | — |
| 99 | `` WITH RECURSIVE cte(n) AS ( SELECT 0 AS n UNION ALL SELECT 1+n FROM cte WHERE n<10 ) SELECT n,1<<n,LPAD(CONV(1<<n,10,2),11,0) FROM cte; `` | 通过 | 11 行结果 | — | — |
| 100 | `` SELECT CONV(~ b'1111111111111111111111111111111111111111111111110000111100001111',10,2); `` | 通过 | 1 行结果 | — | — |
| 101 | `` SELECT CONV(b'1010' \| b'1100',10,2); `` | 通过 | 1 行结果 | — | — |
| 102 | `` SELECT BIT_COUNT(0x29), CONV(0x29,16,2); `` | 通过 | 1 行结果 | — | — |
| 103 | `` SELECT INET_NTOA(INET_ATON('192.168.1.1') & INET_ATON('255.255.255.0')); `` | 通过 | 1 行结果 | — | — |
| 104 | `` SELECT CONV(b'1010' ^ b'1100',10,2); `` | 通过 | 1 行结果 | — | — |
| 105 | `` SELECT BIT_COUNT(INET_ATON('255.255.255.0')); `` | 通过 | 1 行结果 | — | — |
| 106 | `` SELECT CHAR(65,66,67); `` | 通过 | 1 行结果 | — | — |
| 107 | `` SELECT REGEXP_REPLACE('TooDB', 'o{2}', 'i'); `` | 通过 | 1 行结果 | — | — |
| 108 | `` SELECT 'aaa' LIKE 'a%', 'aaa' NOT LIKE 'a%'; `` | 通过 | 1 行结果 | — | — |
| 109 | `` SELECT REGEXP_INSTR('abcabc','a',2); `` | 通过 | 1 行结果 | — | — |
| 110 | `` SELECT ORD('e'), ORD('ë'), HEX('e'), HEX('ë'); `` | 通过 | 1 行结果 | — | — |
| 111 | `` SELECT TO_BASE64(6); `` | 通过 | 1 行结果 | — | — |
| 112 | `` SELECT REGEXP_INSTR('abcabc','a'); `` | 通过 | 1 行结果 | — | — |
| 113 | `` SELECT REGEXP_REPLACE('TooDB', 'o', 'i',1,1); `` | 通过 | 1 行结果 | — | — |
| 114 | `` SET NAMES 'utf8mb4'; `` | 通过 | 0 rows affected（SET NAMES） | — | — |
| 115 | `` SELECT EXPORT_SET(b'01010101', 'x', '_', '', 8); `` | 通过 | 1 行结果 | — | — |
| 116 | `` SELECT CONCAT_WS(',', 'TiDB Server', NULL, 'PD'); `` | 通过 | 1 行结果 | — | — |
| 117 | `` SELECT 'Ti' 'DB' ' ' 'Server'; `` | 通过 | 1 行结果 | — | — |
| 118 | `` SELECT REPEAT('ha',3); `` | 通过 | 1 行结果 | — | — |
| 119 | `` SELECT UNHEX('54694442'); `` | 通过 | 1 行结果 | — | — |
| 120 | `` SELECT REGEXP_REPLACE('TooDB', 'o', 'i',1,2); `` | 通过 | 1 行结果 | — | — |
| 121 | `` SELECT MAKE_SET(b'001','foo','bar','baz'); `` | 通过 | 1 行结果 | — | — |
| 122 | `` SELECT ELT(3, 'This', 'is', 'TiDB'); `` | 通过 | 1 行结果 | — | — |
| 123 | `` SELECT CONCAT_WS(',', 'TiDB Server', NULL); `` | 通过 | 1 行结果 | — | — |
| 124 | `` SELECT MID('abcdef',2); `` | 通过 | 1 行结果 | — | — |
| 125 | `` SELECT REGEXP_INSTR('abcabc','a',1,2); `` | 通过 | 1 行结果 | — | — |
| 126 | `` SELECT REGEXP_INSTR('abcabc','a',1,1,1); `` | 通过 | 1 行结果 | — | — |
| 127 | `` SELECT REGEXP_INSTR('abcabc','A' COLLATE utf8mb4_bin); `` | 通过 | 1 行结果 | — | — |
| 128 | `` SELECT MID('abcdef',2,3); `` | 通过 | 1 行结果 | — | — |
| 129 | `` SELECT CONCAT('TiDB', NULL, 'Server'); `` | 通过 | 1 行结果 | — | — |
| 130 | `` SELECT SUBSTRING_INDEX('www.tidbcloud.com', '.', 2); `` | 通过 | 1 行结果 | — | — |
| 131 | `` SELECT CONCAT_WS(',', 'TiDB Server', '', 'PD'); `` | 通过 | 1 行结果 | — | — |
| 132 | `` SELECT EXPORT_SET(b'00001111', 'x', '_', '', 8); `` | 通过 | 1 行结果 | — | — |
| 133 | `` SELECT HEX(WEIGHT_STRING('ab' AS CHAR(3))) AS char_result, HEX(WEIGHT_STRING('ab' AS BINARY(3))) AS binary_result; `` | 通过 | 1 行结果 | — | — |
| 134 | `` SELECT CONCAT_WS('', 'TiDB Server', 'TiKV', 'PD'); `` | 通过 | 1 行结果 | — | — |
| 135 | `` SELECT BIN(123), BIN('123q123'); `` | 通过 | 1 行结果 | — | — |
| 136 | `` SELECT REGEXP_LIKE('abc','^A','i'); `` | 通过 | 1 行结果 | — | — |
| 137 | `` SELECT MAKE_SET(b'111','foo','bar','baz'); `` | 通过 | 1 行结果 | — | — |
| 138 | `` SELECT BIN(-7); `` | 通过 | 1 行结果 | — | — |
| 139 | `` SELECT REGEXP_REPLACE('TooDB', 'O{2}','i',1,1); `` | 通过 | 1 行结果 | — | — |
| 140 | `` SELECT REGEXP_REPLACE('TooDB', 'o{2}', 'i',3); `` | 通过 | 1 行结果 | — | — |
| 141 | `` SELECT SUBSTRING_INDEX('www.tidbcloud.com', '.', -1); `` | 通过 | 1 行结果 | — | — |
| 142 | `` SELECT MAKE_SET(b'100','foo','bar','baz'); `` | 通过 | 1 行结果 | — | — |
| 143 | `` WITH RECURSIVE nr(n) AS ( SELECT 1 AS n UNION ALL SELECT n+1 FROM nr WHERE n<20 ) SELECT n, REPEAT('x',n) FROM nr; `` | 通过 | 20 行结果 | — | — |
| 144 | `` SELECT REGEXP_LIKE('abc','^a'); `` | 通过 | 1 行结果 | — | — |
| 145 | `` SELECT REGEXP_LIKE('abc','^A'); `` | 通过 | 1 行结果 | — | — |
| 146 | `` SELECT ASCII('A'), ASCII('TiDB'), ASCII(23); `` | 通过 | 1 行结果 | — | — |
| 147 | `` SELECT 'aaa' LIKE 'b%', 'aaa' NOT LIKE 'b%'; `` | 通过 | 1 行结果 | — | — |
| 148 | `` SELECT REGEXP_INSTR('abcabc','A',1,1,0,'i'); `` | 通过 | 1 行结果 | — | — |
| 149 | `` SELECT LTRIM('    hello'); `` | 通过 | 1 行结果；编辑器可访问值核实引号内四空格 | — | — |
| 150 | `` SELECT REGEXP_INSTR('abcabc','A',1,1,0,''); `` | 通过 | 1 行结果 | — | — |
| 151 | `` SELECT REGEXP_REPLACE('abcd','(.*)(.{2})$','\\1') AS s; `` | 通过 | 1 行结果；编辑器可访问值核实双反斜杠 | — | — |
| 152 | `` SELECT UPPER('bigdata') AS result_upper, UPPER(null) AS result_null; `` | 通过 | 1 行结果 | — | — |
| 153 | `` SELECT REGEXP_SUBSTR('This is TiDB','Ti.{2}'); `` | 通过 | 1 行结果 | — | — |
| 154 | `` SELECT UCASE('bigdata') AS result_upper, UCASE(null) AS result_null; `` | 通过 | 1 行结果 | — | — |
| 155 | `` SELECT CONCAT_WS(NULL, 'TiDB Server', 'TiKV', 'PD'); `` | 通过 | 1 行结果 | — | — |
| 156 | `` SELECT COUNT(*), MIN(id), MAX(id), SUM(id), AVG(id) FROM target_tab; `` | 通过 | 1 行聚合结果（3 行测试数据） | — | — |
| 157 | `` SELECT app_func(id) FROM target_tab; `` | 通过 | 数据库错误：catalog1.app_func 不存在 | — | — |
| 158 | `` SELECT app_aggregate(id) FROM target_tab; `` | 通过 | 数据库错误：catalog1.app_aggregate 不存在 | — | — |
| 159 | `` SELECT app_func(app_aggregate(MAX(id))), COUNT(*) FROM target_tab; `` | 通过 | 数据库错误：catalog1.app_aggregate 不存在 | — | — |
| 160 | `` SELECT analytics.COUNT(id) FROM target_tab; `` | 通过 | 数据库错误：analytics.count 不存在 | — | — |
| 161 | `` SELECT `COUNT`(id) FROM target_tab; `` | 通过 | 数据库错误：catalog1.count 不存在 | — | — |
| 162 | `` UPDATE target_tab SET value_col = app_func(value_col); `` | 通过 | 数据库错误：catalog1.app_func 不存在；UPDATE 未修改数据 | — | — |
| 163 | `` SELECT JSON_OVERLAPS( '{"languages": ["Go","Rust","C#"]}', '{"languages": ["Go","Rust","C#"], "arch": ["arm64"]}' ) AS 'Overlaps'; `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 164 | `` SELECT JSON_KEYS('{"name": {"first": "John", "last": "Doe"}, "type": "Person"}', '$.name'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 165 | `` SELECT j->'$.foo', JSON_EXTRACT(j, '$.foo') FROM ( SELECT '{"foo": "bar", "aaa": 5}' AS j ) AS tbl; `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 166 | `` SELECT JSON_CONTAINS('["a","b","c"]','"a"'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 167 | `` SELECT JSON_CONTAINS('["a","b","c"]','"e"'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 168 | `` SELECT JSON_SEARCH('{"a": ["aa", "bb", "cc"], "b": ["cc", "dd"]}','one','cc'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 169 | `` SELECT JSON_CONTAINS_PATH('{"foo": "bar", "aaa": 5}','all','$.foo', '$.aaa'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 170 | `` SELECT JSON_SEARCH('{"a": ["aa", "bb", "cc"], "b": ["cc", "dd"]}','all','cc'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 171 | `` SELECT JSON_KEYS('{"name": {"first": "John", "last": "Doe"}, "type": "Person"}'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 172 | `` SELECT JSON_EXTRACT('{"foo": "bar", "aaa": 5}', '$.foo'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 173 | `` SELECT JSON_CONTAINS_PATH('{"foo": "bar", "aaa": 5}','all','$.foo'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 174 | `` SELECT JSON_OVERLAPS( '{"languages": ["Go","Rust","C#"]}', '{"languages": ["Go","Rust","C#"]}' ) AS 'Overlaps'; `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 175 | `` SELECT JSON_CONTAINS_PATH('{"foo": "bar", "aaa": 5}','all','$.bar'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 176 | `` SELECT JSON_CONTAINS('{"foo": "bar", "aaa": 5}','"bar"'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 177 | `` SELECT JSON_CONTAINS('{"foo": "bar", "aaa": 5}','{"foo": "bar"}'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 178 | `` SELECT JSON_CONTAINS('{"foo": "bar", "aaa": 5}','"bar"', '$.foo'); `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 179 | `` SELECT JSON_OVERLAPS( '{"languages": ["Go","Rust","C#"]}', '{"languages": ["Go","Rust"]}' ) AS 'Overlaps'; `` | 通过 | 1 行结果；页面执行信息核实 | — | — |
| 180 | `` SELECT JSON_REPLACE('{"a": 41, "b": 62}','$.b',42,'$.c',43); `` | 通过 | 1 行结果 | — | — |
| 181 | `` SELECT JSON_INSERT( '{"language": ["Go", "Rust", "C++"]}', '$.architecture', 'riscv', '$.os', JSON_ARRAY("linux","freebsd") ) AS "Demo"; `` | 通过 | 1 行结果 | — | — |
| 182 | `` SELECT JSON_ARRAY_INSERT('["Car", "Boat", "Train"]', '$[0]', "Airplane") AS "Transport options"; `` | 通过 | 1 行结果 | — | — |
| 183 | `` SELECT JSON_MERGE_PATCH( '{"a": 1, "b": 2}', '{"a": 100}', '{"c": 300}' ); `` | 通过 | 1 行结果 | — | — |
| 184 | `` SELECT JSON_UNQUOTE(JSON_EXTRACT('{"database": "TiDB"}', '$.database')); `` | 通过 | 1 行结果 | — | — |
| 185 | `` SELECT JSON_UNQUOTE('"foo"'); `` | 通过 | 1 行结果 | — | — |
| 186 | `` SELECT JSON_ARRAY_APPEND('["Car", "Boat", "Train"]', '$', "Airplane") AS "Transport options"; `` | 通过 | 1 行结果 | — | — |
| 187 | `` SELECT JSON_EXTRACT('{"database": "TiDB"}', '$.database'); `` | 通过 | 1 行结果 | — | — |
| 188 | `` SELECT JSON_REMOVE('{"a": 61, "b": 62, "c": 63}','$.b'); `` | 通过 | 1 行结果 | — | — |
| 189 | `` SELECT JSON_REMOVE('{"a": 61, "b": 62, "c": 63}','$.b','$.c'); `` | 通过 | 1 行结果 | — | — |
| 190 | `` SELECT JSON_REPLACE('{"a": 41, "b": 62}','$.b',42); `` | 通过 | 1 行结果 | — | — |
| 191 | `` SELECT JSON_SET('{"version": 1.1, "name": "example"}','$.version',1.2,'$.branch', "main"); `` | 通过 | 1 行结果 | — | — |
| 192 | `` SELECT JSON_MERGE_PRESERVE('{"a": 1, "b": 2}','{"a": 100}', '{"c": 300}'); `` | 通过 | 1 行结果 | — | — |
| 193 | `` SELECT JSON_ARRAY_INSERT('["Car", "Boat", "Train"]', '$[1]', "Airplane") AS "Transport options"; `` | 通过 | 1 行结果 | — | — |
| 194 | `` SELECT JSON_ARRAY_APPEND('{"transport_options": ["Car", "Boat", "Train"]}', '$.transport_options', "Airplane") AS "Transport options"; `` | 通过 | 1 行结果 | — | — |
| 195 | `` SELECT JSON_SET('{"version": 1.1, "name": "example"}','$.version',1.2); `` | 通过 | 1 行结果 | — | — |
| 196 | `` SELECT JSON_INSERT('{"a": 61, "b": 62}', '$.a', 41, '$.c', 63); `` | 通过 | 1 行结果 | — | — |
| 197 | `` SELECT JSON_TYPE('"2025-06-14"'),JSON_TYPE(CAST(CAST('2025-06-14' AS date) AS json)); `` | 通过 | 1 行结果 | — | — |
| 198 | `` SELECT JSON_VALID('{"foo"="bar"}'); `` | 通过 | 1 行结果 | — | — |
| 199 | `` SELECT JSON_DEPTH('{"weather": {"current": "sunny"}}'); `` | 通过 | 1 行结果 | — | — |
| 200 | `` SELECT '"2025-06-14"',CAST(CAST('2025-06-14' AS date) AS json); `` | 通过 | 1 行结果 | — | — |
| 201 | `` WITH demo AS ( SELECT 'null' AS 'v' UNION SELECT '"foobar"' UNION SELECT 'true' UNION SELECT '5' UNION SELECT '1.14' UNION SELECT '[]' UNION SELECT '{}' UNION SELECT POW(2,63) ) SELECT v, JSON_TYPE(v) FROM demo ORDER BY 2; `` | 通过 | 8 行结果；CloudDM 自动补 LIMIT 1000 | — | — |
| 202 | `` SELECT JSON_LENGTH('{"weather": {"current": "sunny", "tomorrow": "cloudy"}}','$.weather'); `` | 通过 | 1 行结果 | — | — |
| 203 | `` SELECT JSON_LENGTH('{"weather": {"current": "sunny", "tomorrow": "cloudy"}}','$'); `` | 通过 | 1 行结果 | — | — |
| 204 | `` SELECT JSON_VALID('{"foo": "bar"}'); `` | 通过 | 1 行结果 | — | — |
| 205 | `` SELECT JSON_SCHEMA_VALID('{"type": "object"}',@j); `` | 通过 | 1 行结果 | — | — |
| 206 | `` SELECT JSON_SCHEMA_VALID('{"enum": ["TiDB", "MySQL"]}', '"TiDB"'); `` | 通过 | 1 行结果 | — | — |
| 207 | `` SELECT JSON_SCHEMA_VALID('{"anyOf": [{"type": "string"},{"type": "integer"}]}', '["TiDB", "MySQL"]'); `` | 通过 | 1 行结果 | — | — |
| 208 | `` SELECT JSON_SCHEMA_VALID('{"required": ["fruits","vegetables"]}',@j); `` | 通过 | 1 行结果 | — | — |
| 209 | `` SELECT JSON_SCHEMA_VALID('{"anyOf": [{"type": "string"},{"type": "integer"}]}', '5'); `` | 通过 | 1 行结果 | — | — |
| 210 | `` SELECT JSON_SCHEMA_VALID('{"type": "string", "pattern": "^Ti"}', '"TiDB"'); `` | 通过 | 1 行结果 | — | — |
| 211 | `` SELECT JSON_SCHEMA_VALID('{"enum": ["TiDB", "MySQL"]}', '"SQLite"'); `` | 通过 | 1 行结果 | — | — |
| 212 | `` SELECT JSON_SCHEMA_VALID('{"format": "ipv4"}', '"127.0.0.1"'); `` | 通过 | 1 行结果 | — | — |
| 213 | `` SELECT JSON_SCHEMA_VALID('{"anyOf": [{"type": "string"},{"type": "integer"}]}', '"TiDB"'); `` | 通过 | 1 行结果 | — | — |
| 214 | `` SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "array", "minItems": 3}}}',@j); `` | 通过 | 1 行结果 | — | — |
| 215 | `` SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "array"}}}',@j); `` | 通过 | 1 行结果 | — | — |
| 216 | `` SELECT JSON_SCHEMA_VALID('{"required": ["fruits","vegetables","grains"]}',@j); `` | 通过 | 1 行结果 | — | — |
| 217 | `` SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "string"}}}',@j); `` | 通过 | 1 行结果 | — | — |
| 218 | `` SELECT JSON_SCHEMA_VALID('{"format": "ipv4"}', '"327.0.0.1"'); `` | 通过 | 1 行结果 | — | — |
| 219 | `` SELECT JSON_SCHEMA_VALID('{"type": "string", "pattern": "^Ti"}', '"PingCAP"'); `` | 通过 | 1 行结果 | — | — |
| 220 | `` SELECT JSON_SCHEMA_VALID('{"enum": ["TiDB", "MySQL"]}', '"MySQL"'); `` | 通过 | 1 行结果 | — | — |
| 221 | `` SET @j := '{"fruits": ["orange", "apple", "pear"], "vegetables": ["carrot", "pepper", "kale"]}'; `` | 通过 | 0 行受影响 | — | — |
| 222 | `` SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "array", "minItems": 4}}}',@j); `` | 通过 | 1 行结果 | — | — |
| 223 | `` SELECT JSON_SCHEMA_VALID('{"type": "array"}',@j); `` | 通过 | 1 行结果 | — | — |
| 224 | `` SELECT JSON_SCHEMA_VALID('{"type": "integer", "minimum": 40, "maximum": 45}', '123'); `` | 通过 | 1 行结果 | — | — |
| 225 | `` SELECT JSON_STORAGE_FREE('{}'); `` | 通过 | 1 行结果 | — | — |
| 226 | `` SELECT JSON_STORAGE_SIZE('{}'); `` | 通过 | 1 行结果 | — | — |
| 227 | `` SELECT CONNECTION_ID(); `` | 通过 | 1 行结果 | — | — |
| 228 | `` SELECT DATABASE(); `` | 通过 | 1 行结果 | — | — |
| 229 | `` SELECT CURRENT_USER(); `` | 通过 | 1 行结果 | — | — |
| 230 | `` SELECT BENCHMARK(5, SLEEP(2)); `` | 通过 | 1 行结果 | — | — |
| 231 | `` SELECT FOUND_ROWS(); `` | 通过 | 1 行结果 | — | — |
| 232 | `` SELECT 1 UNION ALL SELECT 2; `` | 通过 | 2 行结果 | — | — |
| 233 | `` SELECT USER(), CURRENT_USER(); `` | 通过 | 1 行结果 | — | — |
| 234 | `` SELECT FORMAT_NANO_TIME(1000000); `` | 通过 | 1 行结果 | — | — |
| 235 | `` SELECT FORMAT_BYTES(10*1024*1024); `` | 通过 | 1 行结果 | — | — |
| 236 | `` SELECT IS_UUID('eb48c08c-eb71-11ee-bacf-5405db7aad56'); `` | 通过 | 1 行结果 | — | — |
| 237 | `` SELECT IS_IPV4('127.0.0.1'); `` | 通过 | 1 行结果 | — | — |
| 238 | `` SELECT UUID(); `` | 通过 | 1 行结果 | — | — |
| 239 | `` SELECT INET6_NTOA(0x00000000000000000000000000000001); `` | 通过 | 1 行结果 | — | — |
| 240 | `` SELECT INET6_ATON('::1'); `` | 通过 | 1 行结果 | — | — |
| 241 | `` SELECT 'value' AS 'column name' UNION ALL SELECT 'another value'; `` | 通过 | 2 行结果 | — | — |
| 242 | `` SELECT INET_ATON('127.0.0.1'); `` | 通过 | 1 行结果 | — | — |
| 243 | `` SELECT IS_IPV4('300.0.0.1'); `` | 通过 | 1 行结果 | — | — |
| 244 | `` SELECT INET_NTOA(2130706433); `` | 通过 | 1 行结果 | — | — |
| 245 | `` SELECT NAME_CONST('column name', 'value') UNION ALL SELECT 'another value'; `` | 通过 | 2 行结果 | — | — |
| 246 | `` SELECT IS_IPV4_COMPAT(INET6_ATON('::127.0.0.1')); `` | 通过 | 1 行结果 | — | — |
| 247 | `` SELECT IS_IPV6('::1'); `` | 通过 | 1 行结果 | — | — |
| 248 | `` SELECT SLEEP(1.5); `` | 通过 | 1 行结果 | — | — |
| 249 | `` SELECT IS_IPV4_MAPPED(INET6_ATON('::ffff:127.0.0.1')); `` | 通过 | 1 行结果 | — | — |
| 250 | `` SELECT NEXTVAL(s1); `` | 通过 | 错误：Table `catalog1.s1` doesn't exist | — | — |
| 251 | `` CREATE SEQUENCE s1; `` | 通过 | 0 行受影响 | — | — |
| 252 | `` SELECT NEXT VALUE FOR s1; `` | 通过 | 1 行结果 | — | — |
| 253 | `` SELECT LASTVAL(s1); `` | 通过 | 1 行结果 | — | — |
| 254 | `` SELECT SETVAL(s1, 10); `` | 通过 | 1 行结果 | — | — |
| 255 | `` ADMIN SHOW DDL; `` | 通过 | 1 行 | — | fixture：`admin_tidb_0.txt` |
| 256 | `` ADMIN CHECK TABLE t; `` | 通过 | 错误：catalog1.t 不存在 | — | fixture：同上 |
| 257 | `` SHOW CONFIG; `` | 通过 | 页面分批取数，至少 200 行 | — | fixture：同上 |
| 258 | `` SHOW TABLE t REGIONS; `` | 通过 | 错误：catalog1.t 不存在 | — | fixture：同上 |
| 259 | `` SHOW STATS_HEALTHY; `` | 通过 | 3 行 | — | fixture：同上 |
| 260 | `` ADMIN SHOW DDL JOB QUERIES 51; `` | 通过 | 0 行 | — | fixture：`tidb_upstream_sql_statement_admin_show_ddl_md_0.txt` |
| 261 | `` ADMIN SHOW DDL JOBS; `` | 通过 | 10 行 | — | fixture：同上 |
| 262 | `` ADMIN CHECK INDEX tbl_name idx_name; `` | 通过 | 错误：catalog1.tbl_name 不存在 | — | fixture：`tidb_upstream_sql_statement_admin_check_table_index_md_0.txt` |
| 263 | `` ADMIN CHECKSUM TABLE t1; `` | 通过 | 首次错误：catalog1.t1 不存在；创建后重跑为 1 行 | — | fixture：`tidb_upstream_sql_statement_admin_checksum_table_md_0.txt` |
| 264 | `` CREATE TABLE t1(id INT PRIMARY KEY); `` | 通过 | 0 行受影响 | — | fixture：同上 |
| 265 | `` INSERT INTO t1 VALUES (1),(2),(3); `` | 通过 | 3 行受影响 | — | fixture：同上 |
| 266 | `` SELECT TIDB_DECODE_SQL_DIGESTS(@digests); `` | 通过 | 1 行结果 | — | — |
| 267 | `` SELECT TIDB_ENCODE_SQL_DIGEST('SELECT 2'); `` | 通过 | 1 行结果 | — | — |
| 268 | `` CREATE TABLE t(id int PRIMARY KEY, a int, KEY `idx` (a)); `` | 通过 | 0 行受影响；创建 `catalog1.t` | — | — |
| 269 | `` SELECT *, TIDB_ROW_CHECKSUM() FROM t WHERE id = 1; `` | 通过 | 0 行（表此时无数据，自动 LIMIT 1000） | — | — |
| 270 | `` SELECT TIDB_DECODE_KEY('7480000000000000845f728000000000000001'); `` | 通过 | 1 行结果 | — | — |
| 271 | `` SELECT TIDB_DECODE_SQL_DIGESTS(@digests, 10); `` | 通过 | 1 行结果 | — | — |
| 272 | `` SELECT TIDB_ENCODE_INDEX_KEY('test', 't', 'idx', 2, 1); `` | 通过 | 数据库错误：`test.t` 不存在 | — | — |
| 273 | `` SELECT TIDB_ENCODE_SQL_DIGEST('SELECT 1'); `` | 通过 | 1 行结果 | — | — |
| 274 | `` CREATE TABLE test(id INT PRIMARY KEY CLUSTERED, a INT, b INT, UNIQUE KEY uk((tidb_shard(a)), a)); `` | 通过 | 0 行受影响；创建 `catalog1.test` | — | — |
| 275 | `` SELECT VITESS_HASH(123); `` | 通过 | 1 行结果 | — | — |
| 276 | `` SELECT @@tidb_current_ts; `` | 通过 | 1 行结果 | — | — |
| 277 | `` SELECT tidb_decode_key('7480000000000000FF3E5F720400000000FF0000000601633430FF3338646232FF2D64FF3531632D3131FF65FF622D386337352DFFFF3830653635303138FFFF61396265000000FF00FB000000000000F9'); `` | 通过 | 1 行结果 | — | — |
| 278 | `` SET @digests = '["e6f07d43b5c21db0fbb9a31feac2dc599787763393dd5acbfad80e247eb02ad5","38b03afa5debbdf0326a014dbe5012a62c51957f1982b3093e748460f8b00821","e5796985ccafe2f71126ed6c0ac939ffa015a8c0744a24b7aee6d587103fd2f7"]'; `` | 通过 | 0 行受影响 | — | — |
| 279 | `` SELECT TIDB_IS_DDL_OWNER(); `` | 通过 | 1 行结果 | — | — |
| 280 | `` CREATE TABLE t (id INT PRIMARY KEY, k INT, c CHAR(1)); `` | 通过 | 数据库错误：`catalog1.t` 已存在 | — | — |
| 281 | `` SET GLOBAL tidb_enable_row_level_checksum = ON; `` | 通过 | 0 行受影响；随后补测 TF4 返回 1 行，已恢复原值 OFF | — | — |
| 282 | `` SELECT TIDB_SHARD(12373743746); `` | 通过 | 1 行结果 | — | — |
| 283 | `` SELECT TIDB_PARSE_TSO_LOGICAL(450456244814610434); `` | 通过 | 1 行结果 | — | — |
| 284 | `` SELECT CURRENT_RESOURCE_GROUP(); `` | 通过 | 1 行结果 | — | — |
| 285 | `` INSERT INTO t VALUES(1,2); `` | 通过 | 1 行受影响 | — | — |
| 286 | `` SELECT TABLE_NAME, TIDB_DECODE_KEY(START_KEY), TIDB_DECODE_KEY(END_KEY) FROM information_schema.TIKV_REGION_STATUS WHERE TABLE_NAME='stock' AND IS_INDEX=0 ORDER BY START_KEY; `` | 通过 | 数据库错误：pd http client unavailable | — | — |
| 287 | `` SELECT TIDB_ENCODE_RECORD_KEY('test', 't', 1); `` | 通过 | 数据库错误：`test.t` 不存在 | — | — |
| 288 | `` INSERT INTO t VALUES (1, 10, 'a'); `` | 通过 | 数据库错误：列数与值数不匹配 | — | — |
| 289 | `` SELECT TIDB_PARSE_TSO(@@tidb_current_ts); `` | 通过 | 1 行结果 | — | — |
| 290 | `` CREATE USER 'user1'; `` | 通过 | 0 行受影响，用户查询为 `user1@%`；复测后删除并核实不存在 | — | — |
| 291 | `` SELECT TIDB_PARSE_TSO_LOGICAL(450456244814610433); `` | 通过 | 1 行结果 | — | — |
| 292 | `` SPLIT TABLE t BETWEEN (-9223372036854775808) AND (9223372036854775807) REGIONS 16; `` | 通过 | 错误：常量 -9223372036854775808 超出 INT | — | — |
| 293 | `` SPLIT TABLE t BY (10000), (90000); `` | 通过 | 1 行结果；无 TiKV 服务，未验证实际 region | — | — |
| 294 | `` SPLIT TABLE t INDEX idx1 BETWEEN ("a") AND ("z") REGIONS 25; `` | 通过 | 错误：idx1 索引不存在 | — | — |
| 295 | `` SPLIT PARTITION TABLE t INDEX idx BETWEEN (1000) AND (10000) REGIONS 2; `` | 通过 | 错误：t 不是分区表 | — | — |
| 296 | `` SPLIT PARTITION TABLE t PARTITION (p1,p2) INDEX idx BETWEEN (0) AND (20000) REGIONS 2; `` | 通过 | 错误：t 不是分区表 | — | — |
| 297 | `` SPLIT REGION FOR TABLE t BY (100); `` | 通过 | 1 行结果；未验证实际 region | — | — |
| 298 | `` SPLIT REGION FOR PARTITION TABLE t PARTITION (p1) BY (1000), (9000); `` | 通过 | 错误：t 不是分区表 | — | — |
| 299 | `` FLUSH CLIENT_ERRORS_SUMMARY; `` | 通过 | 0 行受影响 | — | — |
| 300 | `` ANALYZE TABLE t PARTITION p1; `` | 通过 | 错误：非分区表 | — | — |
| 301 | `` ANALYZE TABLE t INDEX idx_t; `` | 通过 | 错误：idx_t 不存在 | — | — |
| 302 | `` KILL TIDB QUERY 4; `` | 通过 | 0 行受影响；预查目标不存在 | — | — |
| 303 | `` flush privileges, status; `` | 通过 | TiDB 语法错误：逗号后 `status` | — | — |
| 304 | `` SELECT VEC_NEGATIVE_INNER_PRODUCT('[1, 2]', '[3, 4]'); `` | 通过 | 1 行结果 | — | — |
| 305 | `` SELECT VEC_FROM_TEXT('[1, 2]') + VEC_FROM_TEXT('[3, 4]'); `` | 通过 | 1 行结果 | — | — |
| 306 | `` SELECT VEC_L2_DISTANCE('[0, 3]', '[4, 0]'); `` | 通过 | 1 行结果 | — | — |
| 307 | `` SELECT VEC_DIMS('[1, 2, 3]'); `` | 通过 | 1 行结果 | — | — |
| 308 | `` SELECT VEC_AS_TEXT('[1.000, 2.5]'); `` | 通过 | 1 行结果 | — | — |
| 309 | `` SELECT VEC_L2_NORM('[3, 4]'); `` | 通过 | 1 行结果 | — | — |
| 310 | `` SELECT VEC_L1_DISTANCE('[0, 0]', '[3, 4]'); `` | 通过 | 1 行结果 | — | — |
| 311 | `` SELECT VEC_COSINE_DISTANCE('[1, 1]', '[-1, -1]'); `` | 通过 | 1 行结果 | — | — |
| 312 | `` SELECT VEC_DIMS('[]'); `` | 通过 | 1 行结果 | — | — |
| 313 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+3 FROM cte WHERE n<30 ) SELECT n, ROW_NUMBER() OVER () FROM cte; `` | 通过 | 11 行结果 | — | — |
| 314 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, NTILE(5) OVER (), NTILE(2) OVER () FROM cte; `` | 通过 | 10 行结果 | — | — |
| 315 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, LAST_VALUE(n) OVER (PARTITION BY n<=5) FROM cte ORDER BY n; `` | 通过 | 10 行结果 | — | — |
| 316 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+2 FROM cte WHERE n<6 ) SELECT *, CUME_DIST() OVER(ORDER BY n) FROM cte; `` | 通过 | 4 行结果 | — | — |
| 317 | `` SELECT *, RANK() OVER (ORDER BY n), DENSE_RANK() OVER (ORDER BY n) FROM ( SELECT 5 AS 'n' UNION ALL SELECT 8 UNION ALL SELECT 5 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32) a; `` | 通过 | 6 行结果 | — | — |
| 318 | `` SELECT *, PERCENT_RANK() OVER (ORDER BY n), PERCENT_RANK() OVER (ORDER BY n DESC) FROM ( SELECT 5 AS 'n' UNION ALL SELECT 8 UNION ALL SELECT 5 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32) a; `` | 通过 | 6 行结果 | — | — |
| 319 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, FIRST_VALUE(n) OVER w AS 'First', NTH_VALUE(n, 2) OVER w AS 'Second', NTH_VALUE(n, 3) OVER w AS 'Third', LAST_VALUE(n) OVER w AS 'Last' FROM cte WINDOW w AS (PARTITION BY n<=5) ORDER BY n; `` | 通过 | 10 行结果 | — | — |
| 320 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, LEAD(n) OVER () FROM cte; `` | 通过 | 10 行结果 | — | — |
| 321 | `` SELECT n, FIRST_VALUE(n) OVER (PARTITION BY n MOD 2 ORDER BY n), FIRST_VALUE(n) OVER (PARTITION BY n <= 2 ORDER BY n) FROM ( SELECT 1 AS 'n' UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 ) a ORDER BY n; `` | 通过 | 4 行结果 | — | — |
| 322 | `` SELECT *, DENSE_RANK() OVER (ORDER BY n) FROM ( SELECT 5 AS 'n' UNION ALL SELECT 8 UNION ALL SELECT 5 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32) a; `` | 通过 | 6 行结果 | — | — |
| 323 | `` WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, LAG(n) OVER () FROM cte; `` | 通过 | 10 行结果 | — | — |
| 324 | `` CREATE STATISTICS stats2 (DEPENDENCY) ON t(a,b); `` | CloudDM 失败 | 工作台错误：Unsupported type `*resolve.NodeW` | — | fixture：`admin_statistics_calibrate_0.txt` |
| 325 | `` DROP STATISTICS stats1; `` | CloudDM 失败 | 工作台错误：Unsupported type `*resolve.NodeW` | — | fixture：同上 |
| 326 | `` CREATE STATISTICS IF NOT EXISTS stats1 (CARDINALITY) ON t(a,b,c); `` | CloudDM 失败 | 工作台错误：Unsupported type `*resolve.NodeW` | — | fixture：同上 |
| 327 | `` CREATE GLOBAL BINDING FOR SELECT * FROM t WHERE a > 1 USING SELECT * FROM t USE INDEX (ia) WHERE a > 1; `` | 通过 | TiDB 错误：ia 索引不存在 | — | fixture：`admin_binding_0.txt` |
| 328 | `` DROP GLOBAL BINDING FOR SELECT * FROM t WHERE a > 1; `` | 通过 | 0 行受影响；预查/复查 binding 均为 0 条 | — | fixture：同上 |
| 329 | `` PLAN REPLAYER DUMP EXPLAIN SELECT * FROM t; `` | 通过 | 1 行结果 | — | fixture：`admin_plan_replayer_0.txt` |
| 330 | `` PLAN REPLAYER LOAD 'plan_replayer.zip'; `` | 通过 | 错误：当前版本不允许该命令 | — | fixture：同上 |
| 331 | `` SELECT p.name, JSON_OBJECTAGG(attribute,value) FROM plant_attributes pa LEFT JOIN plants p ON pa.plant_id=p.id GROUP BY plant_id; `` | 通过 | 首轮缺 plant_attributes；补数据后 3 行 | — | — |
| 332 | `` INSERT INTO plants VALUES (1,"rose"), (2,"tulip"), (3,"orchid"); `` | 通过 | 首轮缺 plants；创建后重跑插入 3 行 | — | — |
| 333 | `` CREATE TABLE plants ( id INT PRIMARY KEY, name VARCHAR(255) ); `` | 通过 | 0 行受影响 | — | — |
| 334 | `` TABLE plants; `` | 通过 | 0 行结果（刚创建） | — | — |
| 335 | `` INSERT INTO plant_attributes(plant_id,attribute,value) VALUES (1,"color","red"), (1,"thorns","yes"), (2,"color","orange"), (2,"thorns","no"), (2,"grows_from","bulb"), (3,"color","white"), (3, "thorns","no"); `` | 通过 | 首轮缺 plant_attributes；创建后重跑插入 7 行 | — | — |
| 336 | `` TABLE plant_attributes; `` | 证据不足 | 首轮缺 plant_attributes | — | 现有记录不足以确认下发，不计通过。 |
| 337 | `` SELECT JSON_ARRAYAGG(v) FROM (SELECT 1 'v' UNION SELECT 2); `` | 通过 | 1 行结果 | — | — |
| 338 | `` CREATE TABLE plant_attributes ( id INT PRIMARY KEY AUTO_INCREMENT, plant_id INT, attribute VARCHAR(255), value VARCHAR(255), FOREIGN KEY (plant_id) REFERENCES plants(id) ); `` | 通过 | 0 行受影响 | — | — |
| 339 | `` WITH RECURSIVE nr(n) AS ( SELECT 0 AS n UNION ALL SELECT n+1 FROM nr WHERE n<20 ) SELECT n, OCT(n) FROM nr; `` | 通过 | 21 行结果 | — | — |
| 340 | `` SELECT REGEXP_INSTR('abc','^.b.$'); `` | 通过 | 1 行结果 | — | — |
| 341 | `` SELECT CHAR(97), CHAR(65); `` | 通过 | 1 行结果 | — | — |
| 342 | `` SELECT REGEXP_REPLACE('TooDB', 'O{2}','i',1,1,'i'); `` | 通过 | 1 行结果 | — | — |
| 343 | `` SELECT MAKE_SET(b'010','foo','bar','baz'); `` | 通过 | 1 行结果 | — | — |
| 344 | `` SELECT TO_BASE64('abc'); `` | 通过 | 1 行结果 | — | — |
| 345 | `` SELECT ORD('a'), ORD('A'); `` | 通过 | 1 行结果 | — | — |
| 346 | `` SELECT CONCAT_WS(',', 'TiDB Server', 'TiKV', 'PD'); `` | 通过 | 1 行结果 | — | — |
| 347 | `` WITH vals AS ( SELECT 'TiDB' AS v UNION ALL SELECT 'Titanium' UNION ALL SELECT 'Tungsten' UNION ALL SELECT 'Rust' ) SELECT v, v REGEXP '^Ti' AS 'starts with "Ti"', v REGEXP '^.{4}$' AS 'Length is 4 characters' FROM vals; `` | 通过 | 4 行结果 | — | — |
| 348 | `` SELECT EXPORT_SET(b'101',"ON",'off','\|',5); `` | 通过 | 1 行结果 | — | — |
| 349 | `` SELECT QUOTE(0x002774657374); `` | 通过 | 1 行结果 | — | — |
| 350 | `` SELECT CONCAT('«',LTRIM('    hello'),'»'); `` | 通过 | 1 行结果 | — | — |
| 351 | `` SELECT v FROM ( SELECT 'TiDB' AS v ) AS vals WHERE v REGEXP 'DB$'; `` | 通过 | 1 行结果 | — | — |
| 352 | `` SELECT MAKE_SET(b'000','foo','bar','baz'); `` | 通过 | 1 行结果 | — | — |
| 353 | `` SELECT REGEXP_INSTR('abcabc','A' COLLATE utf8mb4_general_ci); `` | 通过 | 1 行结果 | — | — |
| 354 | `` SELECT CONCAT('TiDB', ' ', 'Server', '-', 1, TRUE); `` | 通过 | 1 行结果 | — | — |
| 355 | `` SELECT CONVERT(0x616263 USING utf8mb4); `` | 通过 | 1 行结果 | — | — |
| 356 | `` INSERT INTO t VALUES (2, JSON_OBJECT('a',JSON_ARRAY(4,5,6))); `` | 通过 | 类型错误：JSON 不能写入 INT 列 a | — | — |
| 357 | `` INSERT INTO t VALUES (1, JSON_OBJECT('a',JSON_ARRAY(1,2,3))); `` | 通过 | 类型错误：JSON 不能写入 INT 列 a（原记录“同上”）。 | — | — |
| 358 | `` INSERT INTO t VALUES (3, JSON_OBJECT('a',JSON_ARRAY(7,8,9))); `` | 通过 | 类型错误：JSON 不能写入 INT 列 a（原记录“同上”）。 | — | — |
| 359 | `` SELECT CAST(0x54694442 AS CHAR); `` | 通过 | 1 行结果 | — | — |
| 360 | `` ANALYZE TABLE t; `` | 通过 | 0 行受影响 | — | — |
| 361 | `` DROP TABLE IF EXISTS t; `` | 通过 | 0 行受影响；删除本轮旧 t（1 行） | — | — |
| 362 | `` select name, count(name) from orders group by name having count(name) = 1; `` | 通过 | 3 行结果 | — | — |
| 363 | `` select id, floor(value/100) from tbl_name group by id, floor(value/100); `` | 通过 | 错误：catalog1.tbl_name 不存在 | — | — |
| 364 | `` INSERT INTO t VALUES(1, 1, 1), (2, 1, 1), (2, 2, 1), (3, 1, 1), (5, 1, 2), (5, 1, 2), (6, 1, 2), (7, 1, 2); `` | 通过 | 错误：catalog1.t 不存在 | — | — |
| 365 | `` select distinct a, b from t order by c; `` | 通过 | 错误：catalog1.t 不存在 | — | — |
| 366 | `` select id, floor(value/100) as val from tbl_name group by id, val; `` | 通过 | 错误：catalog1.tbl_name 不存在 | — | — |
| 367 | `` CREATE TABLE t(a INT); `` | 通过 | 0 行受影响；新建单列 t | — | — |
| 368 | `` create table t(a bigint, b bigint, c bigint); `` | 通过 | 错误：catalog1.t 已存在 | — | — |
| 369 | `` SELECT APPROX_PERCENTILE(a, 50) FROM t; `` | 通过 | 1 行结果（空表输入） | — | — |
| 370 | `` insert into t values(1, 2, 1), (1, 2, 2), (1, 3, 1), (1, 3, 2); `` | 通过 | 错误：列数与值数不匹配 | — | — |
| 371 | `` INSERT INTO t VALUES(1), (2), (3); `` | 通过 | 3 行受影响 | — | — |
| 372 | `` drop table if exists t; `` | 通过 | 0 行受影响；删除本轮单列 t（3 行） | — | — |
| 373 | `` select name, count(name) as c from orders group by name having c = 1; `` | 通过 | 3 行结果 | — | — |
| 374 | `` SELECT APPROX_COUNT_DISTINCT(a, b) FROM t GROUP BY c; `` | 通过 | 错误：catalog1.t 不存在 | — | — |
| 375 | `` CREATE TABLE t(a INT, b INT, c INT); `` | 通过 | 0 行受影响；新建三列 t | — | — |
| 376 | `` ADMIN SHOW t NEXT_ROW_ID; `` | 通过 | 1 行 | — | — |
| 377 | `` ADMIN EVOLVE BINDINGS; `` | 通过 | 错误：baseline evolution 尚未开放 | — | — |
| 378 | `` ADMIN RELOAD expr_pushdown_blacklist; `` | 通过 | 0 行受影响 | — | — |
| 379 | `` ADMIN RELOAD BINDINGS; `` | 通过 | 0 行受影响 | — | — |
| 380 | `` ADMIN SHOW DDL JOBS 5; `` | 通过 | 5 行 | — | — |
| 381 | `` ADMIN RELOAD opt_rule_blacklist; `` | 通过 | 0 行受影响 | — | — |
| 382 | `` ADMIN FLUSH BINDINGS; `` | 通过 | 0 行受影响 | — | — |
| 383 | `` ADMIN SHOW DDL JOBS 5 WHERE state != 'synced' AND db_name = 'test'; `` | 通过 | 0 行 | — | — |
| 384 | `` ADMIN CAPTURE BINDINGS; `` | 通过 | 0 行受影响 | — | — |
| 385 | `` ADMIN REPAIR TABLE tbl_name CREATE TABLE STATEMENT; `` | 通过 | 错误：未启用 TiDB REPAIR MODE | — | — |
| 386 | `` ADMIN CLEANUP INDEX tbl idx; `` | 通过 | 错误：catalog1.tbl 不存在 | — | — |
| 387 | `` ADMIN RECOVER INDEX tbl idx; `` | 通过 | 错误：catalog1.tbl 不存在 | — | — |
| 388 | `` DROP STATS t PARTITION p0, p1; `` | 通过 | 错误：非分区表 | — | — |
| 389 | `` LOAD STATS '/tmp/stats.json'; `` | 通过 | 错误：此版本不允许 LOAD STATS | — | — |
| 390 | `` RECOVER TABLE BY JOB 53; `` | 通过 | 错误：JOB 53 为 CREATE TABLE，不能恢复 | — | — |
| 391 | `` FLASHBACK TABLE t TO t1; `` | 通过 | 错误：缺 tikv_gc_safe_point | — | — |
| 392 | `` ANALYZE INCREMENTAL TABLE t INDEX idx; `` | 通过 | 提示：增量 ANALYZE 已在 v7.5 移除，无效果 | — | — |
| 393 | `` ANALYZE TABLE t COLUMNS c1,c2 WITH 4 TOPN; `` | 通过 | 错误：列 c1 不存在 | — | — |
| 394 | `` ANALYZE TABLE t PARTITION p0 PREDICATE COLUMNS WITH 1024 BUCKETS; `` | 通过 | 错误：非分区表 | — | — |
| 395 | `` ANALYZE TABLE t WITH 4 BUCKETS, 4 TOPN, 4 CMSKETCH WIDTH, 4 CMSKETCH DEPTH, 4 SAMPLES; `` | 通过 | 0 行受影响 | — | — |
| 396 | `` ALTER TABLE t ANALYZE PARTITION p0 INDEX idx WITH 4 BUCKETS; `` | 通过 | 错误：非分区表 | — | — |
| 397 | `` SELECT CONVERT("abc" USING laTiN1) ; `` | 通过 | 1 行结果 | — | — |
| 398 | `` SELECT CHAR("abc" USING "binary") ; `` | 通过 | 1 行结果 | — | — |
| 399 | `` SELECT CONVERT("abc" USING "latin1") ; `` | 通过 | 1 行结果 | — | — |
| 400 | `` select json_memberof() ; `` | 通过 | 服务端 panic；当前连接关闭 | — | — |
| 401 | `` SELECT CONVERT("abc" USING biNaRy) ; `` | 通过 | 重连重跑后 1 行结果（初次连接已关闭） | — | — |
| 402 | `` SELECT CHAR("abc" USING binary) ; `` | 通过 | 重连重跑后 1 行结果（初次连接已关闭） | — | — |
| 403 | `` SELECT CHAR("abc" USING "latin1") ; `` | 通过 | 重连重跑后 1 行结果（初次连接已关闭） | — | — |
| 404 | `` SELECT CONVERT("abc" USING "binary") ; `` | 通过 | 重连重跑后 1 行结果（初次连接已关闭） | — | — |
| 405 | `` SELECT CHAR("abc" USING laTiN1) ; `` | 通过 | 重连重跑后 1 行结果（初次连接已关闭） | — | — |
| 406 | `` SELECT * FROM (SELECT * FROM test) t ; `` | 通过 | 0 行 | — | — |
| 407 | `` SELECT * FROM tables LIMIT 2000 OFFSET 0 ; `` | 通过 | 错误：catalog1.tables 不存在；页面重写 LIMIT 1000 | — | — |
| 408 | `` SELECT * FROM t1 EXCEPT SELECT * FROM t2 ; `` | 通过 | 错误：catalog1.t2 不存在 | — | — |
| 409 | `` SELECT * FROM (SELECT * FROM test) ; `` | 通过 | 错误：派生表必须有别名 | — | — |
| 410 | `` UPDATE test1,test2 SET test1.name=test2.name,test1.age=test2.age WHERE test1.id=test2.id ; `` | 通过 | 错误：catalog1.test1 不存在，未更新 | — | — |
| 411 | `SHOW CHARACTER SET LIKE 'utf8%';` | 通过 | 2 行，utf8 / utf8mb4 | — | 来源：`show_character_set_md_0` |
| 412 | `SHOW CHARACTER SET WHERE Description='UTF-8 Unicode';` | 通过 | 2 行 | — | 来源：`show_character_set_md_0` |
| 413 | `SHOW COLLATION WHERE Charset='utf8mb4';` | 通过 | 5 行 | — | 来源：`show_collation_md_0` |
| 414 | `SHOW CONFIG WHERE type = 'tidb' AND name = 'advertise-address';` | 通过 | 1 行，当前 TiDB 实例 | — | 来源：`show_config_md_0` |
| 415 | `SHOW CONFIG LIKE 'tidb';` | 通过 | 页面分批取回并显示 200 行 | — | 来源：`show_config_md_0` |
| 416 | `SHOW CREATE DATABASE catalog1;` | 通过 | 1 行，CREATE DATABASE 定义 | — | 来源：`show_create_database_md_0`（将现有 schema 代入） |
| 417 | `SHOW CREATE SCHEMA IF NOT EXISTS catalog1;` | 通过 | 1 行，含 IF NOT EXISTS | — | 来源：`show_create_database_md_0`（将现有 schema 代入） |
| 418 | `SHOW PLACEMENT LABELS;` | 通过 | Execution Information：`pd http client unavailable` | — | 来源：`show_placement_labels_md_0` |
| 419 | `SHOW PLUGINS;` | 通过 | 0 行，列结构正常 | — | 来源：`show_plugins_md_0` |
| 420 | `SHOW PRIVILEGES;` | 通过 | 分批取回 50 行 | — | 来源：`show_privileges_md_0` |
| 421 | `SHOW PROFILES;` | 通过 | 0 行，列结构正常 | — | 来源：`show_profiles_md_0` |
| 422 | `SHOW STATS_BUCKETS WHERE Table_name='t';` | 通过 | 0 行，列结构正常 | — | 来源：`show_stats_buckets_md_0` |
| 423 | `SHOW STATS_HISTOGRAMS;` | 通过 | 3 行 | — | 来源：`show_stats_histograms_md_0` |
| 424 | `SHOW STATS_HISTOGRAMS WHERE table_name = 't2';` | 通过 | 0 行，列结构正常 | — | 来源：`show_stats_histograms_md_0` |
| 425 | `SHOW STATS_META;` | 通过 | 8 行 | — | 来源：`show_stats_meta_md_0` |
| 426 | `SHOW STATS_META WHERE table_name = 't2';` | 通过 | 0 行，列结构正常 | — | 来源：`show_stats_meta_md_0` |
| 427 | `SHOW STATS_TOPN WHERE Table_name='t';` | 通过 | 2 行 | — | 来源：`show_stats_topn_md_0` |
| 428 | `SHOW TABLE catalog1.orders REGIONS;` | 通过 | 1 行，含 REGION_ID 等区域字段 | — | 来源：[官方 SHOW TABLE REGIONS](https://docs.pingcap.com/tidb/stable/sql-statement-show-table-regions/) |
| 429 | `SHOW WARNINGS;` | 通过 | 0 行，仍可继续查询 | — | 来源：管理查询恢复检查 |
| 430 | `SHOW TABLE catalog1.orders DISTRIBUTION;` | CloudDM 失败 | 两次点击 Run 均无新的结果或执行信息；此 SQL 不是官方语法 | — | 来源：排版/版本边界（误用单数） |
| 431 | `SHOW TABLE catalog1.orders DISTRIBUTIONS;` | 通过 | TiDB 8.5.3 返回语法错误，near `DISTRIBUTIONS` | — | 来源：[官方 SHOW TABLE DISTRIBUTION](https://docs.pingcap.com/tidb/stable/sql-statement-show-table-distribution/) |
| 432 | `` SHOW BACKUPS WHERE `Progress` < 25.0; `` | 通过 | 0 行，列结构正常 | — | 来源：`show_backups_md_0` |
| 433 | `SHOW BACKUPS LIKE 's3://%';` | 通过 | 0 行，列结构正常 | — | 来源：`show_backups_md_0` |
| 434 | `SHOW BACKUPS;` | 通过 | 0 行，列结构正常 | — | 来源：`show_backups_md_0` |
| 435 | `SHOW IMPORT JOBS;` | 通过 | 0 行，列结构正常 | — | 来源：`show_import_job_md_0` |
| 436 | `` ADMIN CHECK INDEX catalog1.t1 `PRIMARY`; `` | 通过 | TiDB 返回 `secondary index PRIMARY does not exist`；目标表只有主键 | — | 来源：`admin_check_table_index_md_0`（将隔离表代入） |
| 437 | `ADMIN SHOW DDL JOB QUERIES 51;` | 通过 | 0 行，列结构正常 | — | 来源：`admin_show_ddl_md_0` |
| 438 | `ADMIN SHOW DDL JOBS;` | 通过 | 10 行 | — | 来源：`admin_show_ddl_md_0` |
| 439 | `SHOW TABLES FROM catalog1 LIKE "codex_it_tidb_admin_index_20260916_01";` | 通过 | 0 行，确认未占用名称 | — | 来源：专用对象预查 |
| 440 | `CREATE TABLE catalog1.codex_it_tidb_admin_index_20260916_01 (id INT PRIMARY KEY, k INT NOT NULL, KEY idx_k (k));` | 通过 | 0 行受影响，新建专用表 | — | 来源：secondary index 前置数据 |
| 441 | `INSERT INTO catalog1.codex_it_tidb_admin_index_20260916_01 VALUES (1,10),(2,20),(3,30);` | 通过 | 3 行受影响 | — | 来源：secondary index 前置数据 |
| 442 | `SHOW INDEX FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 2 行，PRIMARY 和 idx_k | — | 来源：secondary index 前置核对 |
| 443 | `ADMIN CHECK INDEX catalog1.codex_it_tidb_admin_index_20260916_01 idx_k;` | 通过 | 0 行受影响，无错误 | — | 来源：`admin_check_table_index_md_0`（代入专用表/index） |
| 444 | `ADMIN CHECK TABLE catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 0 行受影响，无错误 | — | 来源：`admin_tidb_0`（代入专用表） |
| 445 | `ADMIN CHECKSUM TABLE catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 1 行校验结果 | — | 来源：`admin_checksum_table_md_0`（代入专用表） |
| 446 | `PLAN REPLAYER DUMP EXPLAIN SELECT * FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 1 行 File_token；未下载或公开 token | — | 来源：`admin_plan_replayer_0`（只引用专用表） |
| 447 | `PLAN REPLAYER LOAD 'plan_replayer.zip';` | 通过 | 原文单引号和双引号两种写法各提交一次，均返回 `The used command is not allowed with this MySQL version`；未导入 ZIP | — | 来源：`admin_plan_replayer_0`（fixture 原文路径，无文件） |
| 448 | `SHOW TABLE catalog1.codex_it_tidb_admin_index_20260916_01 INDEX idx_k REGIONS;` | 通过 | 1 行，idx_k 的 Region 元数据 | — | 来源：[官方 SHOW TABLE REGIONS](https://docs.pingcap.com/tidb/stable/sql-statement-show-table-regions/) |
| 449 | `SELECT COUNT(*) AS row_count FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 1 行，值为 3；工作台改写为 `LIMIT 1000` | — | 来源：专用数据最终核对 |
| 450 | `SHOW BACKUPS;` 换行 `SELECT COUNT(*) AS row_count FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 两条均进入 Execution Information；SHOW 0 行、SELECT 1 行值 3，后者追加 `LIMIT 1000`；未见重写空指针 | — | 来源：DAL + SELECT 全选批次 |
| 451 | `SHOW PLACEMENT LABELS;` 换行 `SELECT 1 AS recovery_ok;` | 通过 | 同批次第一条报 `pd http client unavailable`，第二条仍有 1 行且值 1；没有重写空指针 | — | 来源：DAL 错误 + SELECT 全选批次 |
| 452 | `SELECT VERSION();` | 通过 | 10:21:25；1 行，`8.0.11-TiDB-v8.5.3` | — | — |
| 453 | `SHOW PLACEMENT LABELS;` | 通过 | 10:21:51；TiDB 返回 `pd http client unavailable` | — | — |
| 454 | `SHOW TABLE catalog1.orders DISTRIBUTION;` | CloudDM 失败 | 页面提示 `在 1 行 29 列，语句无法解析。`，无新增 Execution Information；此为非官方单数语法 | — | — |
| 455 | `SHOW TABLE catalog1.orders DISTRIBUTIONS;` | 通过 | 10:22:09；TiDB 8.5.3 返回 `You have an error in your SQL syntax; check the manual that corresponds to your TiDB version for the right syntax to use line 1 column 40 near "DISTRIBUTIONS;"` | — | — |
| 456 | `SHOW ENGINES;` | 通过 | 10:22:43；1 行 | — | — |
| 457 | `SHOW BUILTINS;` | 通过 | 10:22:43；分批收到 30、303 行，`303 row retrieved finish` | — | — |
| 458 | `SHOW PROCESSLIST;` | 通过 | 10:22:43；1 行 | — | — |
| 459 | `SHOW STATS_HEALTHY;` | 通过 | 10:22:43；9 行 | — | — |
| 460 | `SELECT 1 AS recovery_ok;` | 通过 | 10:22:43；1 行，值 `1` | — | — |
| 461 | `SHOW ERRORS;` | 通过 | 10:23:33；0 行 | — | — |
| 462 | `SHOW ERRORS LIKE '42%';` | 通过 | 10:23:33；0 行 | — | — |
| 463 | `SHOW ERRORS WHERE Code > 1000;` | 通过 | 10:23:33；0 行 | — | — |
| 464 | `SHOW ENGINES LIKE 'InnoDB';` | 通过 | 10:23:33；1 行 | — | — |
| 465 | `SHOW ENGINES LIKE CONCAT('Inno', 'DB');` | 通过 | 10:23:33；1 行 | — | — |
| 466 | `SHOW ENGINES WHERE Support = 'DEFAULT';` | 通过 | 10:23:33；1 行 | — | — |
| 467 | `SHOW WARNINGS LIKE 'Warning';` | 通过 | 10:23:33；0 行 | — | — |
| 468 | `SHOW WARNINGS WHERE Code > 1000;` | 通过 | 10:23:33；0 行 | — | — |
| 469 | `SELECT IFNULL(NULL, 1), NULLIF(1, 1), IF(TRUE, 'yes', 'no');` | 通过 | 10:23:56；1 行 | — | — |
| 470 | `SELECT CONCAT('Ti', 'DB'), REGEXP_REPLACE('abc123', '[0-9]+', ''), CHAR_LENGTH('数据库');` | 通过 | 10:23:56；1 行 | — | — |
| 471 | `SELECT ABS(-1), ROUND(12.345, 2), CONV('ff', 16, 10), RAND();` | 通过 | 10:23:56；1 行 | — | — |
| 472 | `SELECT DATE_ADD('2026-01-01', INTERVAL 1 DAY), TIMESTAMPDIFF(DAY, '2026-01-01', '2026-01-03');` | 通过 | 10:23:56；1 行 | — | — |
| 473 | `SELECT JSON_EXTRACT('{"a":1}', '$.a'), JSON_ARRAY(1, 2), JSON_OBJECT('a', 1);` | 通过 | 10:23:56；1 行 | — | — |
| 474 | `SELECT SHA2('tidb', 256), COMPRESS('tidb'), UNCOMPRESS(COMPRESS('tidb'));` | 通过 | 10:23:56；1 行 | — | — |
| 475 | `SELECT BIT_COUNT(7), 1 << 3, 8 >> 2;` | 通过 | 10:23:56；1 行 | — | — |
| 476 | `SELECT DATABASE(), CURRENT_USER(), CONNECTION_ID(), TIDB_VERSION();` | 通过 | 10:23:56；1 行 | — | — |
| 477 | `SELECT COUNT(*), SUM(amount), AVG(amount), MIN(amount), MAX(amount) FROM catalog1.orders;` | 通过 | 10:23:56；页面改写追加 `LIMIT 1000`，1 行 | — | — |
| 478 | `SELECT ROW_NUMBER() OVER (ORDER BY id), LAG(amount) OVER (ORDER BY id) FROM catalog1.orders;` | 通过 | 10:23:56；页面改写追加 `LIMIT 1000`，3 行 | — | — |
| 479 | `SELECT GET_LOCK('dal_lock', 1), RELEASE_LOCK('dal_lock');` | 通过 | 10:23:56；1 行 | — | — |
| 480 | `SELECT CAST('42' AS SIGNED), CONVERT('abc' USING utf8mb4);` | 通过 | 10:23:56；1 行，页面值 `42` / `abc` | — | — |
| 481 | `SELECT COALESCE(NULL, NULL, 'fallback');` | 通过 | 10:25:18；1 行 | — | — |
| 482 | `SELECT ISNULL(NULL);` | 通过 | 10:25:18；1 行 | — | — |
| 483 | `SELECT JSON_MERGE('{"a": 1}', '{"b": 2}');` | 通过 | 10:25:18；1 行 | — | — |
| 484 | `SELECT JSON_PRETTY('{"person": {"name": "TiDB"}}');` | 通过 | 10:25:18；1 行 | — | — |
| 485 | `SELECT POWER(2, 8);` | 通过 | 10:25:18；1 行 | — | — |
| 486 | `SELECT REVERSE('TiDB');` | 通过 | 10:25:18；1 行 | — | — |
| 487 | `SELECT SPACE(3);` | 通过 | 10:25:18；1 行 | — | — |
| 488 | `SELECT STR_TO_DATE('2026-09-11', '%Y-%m-%d');` | 通过 | 10:25:18；1 行 | — | — |
| 489 | `SELECT TIDB_DECODE_BINARY_PLAN('binary-plan');` | 通过 | 10:25:18；1 行 | — | — |
| 490 | `SELECT TIDB_MVCC_INFO('74800000000000007f5f698000000000000001');` | 通过 | 10:25:18；1 行 | — | — |
| 491 | `SELECT TRANSLATE('abcdef', 'ace', 'xyz');` | 通过 | 10:25:18；1 行，页面值 `xbydzf` | — | — |
| 492 | `ANALYZE TABLE catalog1.orders;` | 通过 | 10:25:59；0 rows affected | — | — |
| 493 | `PREPARE stmt1 FROM 'SELECT * FROM catalog1.orders WHERE id = ?';` | 通过 | 10:25:59；0 rows affected | — | — |
| 494 | `EXECUTE stmt1 USING @id;` | 通过 | 10:25:59；0 行结果（`@id` 未设置） | — | — |
| 495 | `DEALLOCATE PREPARE stmt1;` | 通过 | 10:25:59；0 rows affected | — | — |
| 496 | `EXPLAIN ANALYZE SELECT * FROM catalog1.orders;` | 通过 | 10:25:59；2 行 | — | — |
| 497 | `ADMIN SHOW DDL;` | 通过 | 10:25:59；1 行 | — | — |
| 498 | `ADMIN CHECK TABLE catalog1.orders;` | 通过 | 10:25:59；0 rows affected | — | — |
| 499 | `ADMIN CHECKSUM TABLE catalog1.orders;` | 通过 | 10:25:59；1 行 | — | — |
| 500 | `SHOW CONFIG;` | 通过 | 10:26:00；分批收到 30、200 行，`200 row retrieved finish` | — | — |
| 501 | `SHOW BUILTINS;` | 通过 | 10:26:00；分批收到 30、303 行，`303 row retrieved finish` | — | — |
| 502 | `SHOW PROCESSLIST;` | 通过 | 10:26:00；1 行 | — | — |
| 503 | `SHOW STATS_HEALTHY;` | 通过 | 10:26:00；9 行 | — | — |
| 504 | `SET @@SESSION.tidb_mem_quota_query = 1073741824;` | 通过 | 10:26:44；0 rows affected，原表记录原值同为 1073741824 | — | — |
| 505 | `FLUSH TABLES catalog1.orders WITH READ LOCK;` | 通过 | 10:26:44；TiDB 返回 `FLUSH TABLES WITH READ LOCK is not supported. Please use @@tidb_snapshot` | — | — |
| 506 | `FLUSH STATUS;` | 通过 | 10:26:44；0 rows affected | — | — |
| 507 | `` CACHE INDEX catalog1.orders KEY (`PRIMARY`) IN cache_schema; `` | 通过 | 10:26:44；TiDB SQL syntax error，line 1 column 5 near `CACHE INDEX ...` | — | — |
| 508 | `` LOAD INDEX INTO CACHE catalog1.orders KEY (`PRIMARY`) IGNORE LEAVES; `` | 通过 | 10:26:44；TiDB SQL syntax error，line 1 column 10 near `INDEX INTO CACHE ...` | — | — |
| 509 | `RESET QUERY CACHE;` | 通过 | 10:26:44；TiDB SQL syntax error，line 1 column 5 near `RESET QUERY CACHE;` | — | — |
| 510 | `SELECT 1 AS recovery_ok;` | 通过 | 10:26:44；1 行 | — | — |
| 511 | `SHOW CHARACTER SET LIKE 'utf8%';` | 通过 | 10:27:20；2 行 | — | — |
| 512 | `SHOW CHARACTER SET WHERE Description='UTF-8 Unicode';` | 通过 | 10:27:21；2 行 | — | — |
| 513 | `SHOW COLLATION WHERE Charset='utf8mb4';` | 通过 | 10:27:21；5 行 | — | — |
| 514 | `SHOW CONFIG WHERE type = 'tidb' AND name = 'advertise-address';` | 通过 | 10:27:21；1 行 | — | — |
| 515 | `SHOW CONFIG LIKE 'tidb';` | 通过 | 10:27:21；分批收到 30、200 行，`200 row retrieved finish` | — | — |
| 516 | `SHOW CREATE DATABASE catalog1;` | 通过 | 10:27:21；1 行 | — | — |
| 517 | `SHOW CREATE SCHEMA IF NOT EXISTS catalog1;` | 通过 | 10:27:21；1 行 | — | — |
| 518 | `SHOW PLUGINS;` | 通过 | 10:27:21；0 行 | — | — |
| 519 | `SHOW PRIVILEGES;` | 通过 | 10:27:21；分批收到 30、50 行，`50 row retrieved finish` | — | — |
| 520 | `SHOW PROFILES;` | 通过 | 10:27:21；0 行 | — | — |
| 521 | `SHOW STATS_BUCKETS WHERE Table_name='t';` | 通过 | 10:27:21；0 行 | — | — |
| 522 | `SHOW STATS_HISTOGRAMS;` | 通过 | 10:27:21；4 行（历史为 3 行，ANALYZE 后统计可能增加） | — | — |
| 523 | `SHOW STATS_HISTOGRAMS WHERE table_name = 't2';` | 通过 | 10:27:21；0 行 | — | — |
| 524 | `SHOW STATS_META;` | 通过 | 10:27:21；9 行（历史为 8 行） | — | — |
| 525 | `SHOW STATS_META WHERE table_name = 't2';` | 通过 | 10:27:21；0 行 | — | — |
| 526 | `SHOW STATS_TOPN WHERE Table_name='t';` | 通过 | 10:27:21；2 行 | — | — |
| 527 | `SHOW TABLE catalog1.orders REGIONS;` | 通过 | 10:27:21；1 行 | — | — |
| 528 | `SHOW WARNINGS;` | 通过 | 10:27:21；0 行 | — | — |
| 529 | `` SHOW BACKUPS WHERE `Progress` < 25.0; `` | 通过 | 10:28:16；0 行 | — | — |
| 530 | `SHOW BACKUPS LIKE 's3://%';` | 通过 | 10:28:16；0 行 | — | — |
| 531 | `SHOW BACKUPS;` | 通过 | 10:28:16；0 行 | — | — |
| 532 | `SHOW IMPORT JOBS;` | 通过 | 10:28:16；0 行 | — | — |
| 533 | `` ADMIN CHECK INDEX catalog1.t1 `PRIMARY`; `` | 通过 | 10:28:16；TiDB 返回 `secondary index PRIMARY does not exist` | — | — |
| 534 | `ADMIN SHOW DDL JOB QUERIES 51;` | 通过 | 10:28:16；0 行 | — | — |
| 535 | `ADMIN SHOW DDL JOBS;` | 通过 | 10:28:16；10 行 | — | — |
| 536 | `SHOW TABLES FROM catalog1 LIKE 'codex_it_tidb_admin_index_20260916_01';` | 通过 | 10:28:17；1 行，专用表已由历史测试创建 | — | — |
| 537 | `CREATE TABLE catalog1.codex_it_tidb_admin_index_20260916_01 (id INT PRIMARY KEY, k INT NOT NULL, KEY idx_k (k));` | 通过 | 10:28:17；TiDB 返回 `Table 'catalog1.codex_it_tidb_admin_index_20260916_01' already exists`，本轮未新建 | — | — |
| 538 | `INSERT INTO catalog1.codex_it_tidb_admin_index_20260916_01 VALUES (1,10),(2,20),(3,30);` | 通过 | 10:28:17；TiDB 返回 `Duplicate entry '1' for key 'codex_it_tidb_admin_index_20260916_01.PRIMARY'`，本轮未插入 | — | — |
| 539 | `SHOW INDEX FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 10:28:17；2 行 | — | — |
| 540 | `ADMIN CHECK INDEX catalog1.codex_it_tidb_admin_index_20260916_01 idx_k;` | 通过 | 10:28:17；0 rows affected | — | — |
| 541 | `ADMIN CHECK TABLE catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 10:28:17；0 rows affected | — | — |
| 542 | `ADMIN CHECKSUM TABLE catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 10:28:17；1 行 | — | — |
| 543 | `SHOW TABLE catalog1.codex_it_tidb_admin_index_20260916_01 INDEX idx_k REGIONS;` | 通过 | 10:28:17；1 行 | — | — |
| 544 | `SELECT COUNT(*) AS row_count FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 10:28:17；页面追加 `LIMIT 1000`，1 行、值 3 | — | — |
| 545 | `SELECT COUNT(*), MIN(id), MAX(id), SUM(id), AVG(id) FROM target_tab;` | 通过 | 10:29:11；页面追加 `LIMIT 1000`，1 行 | — | — |
| 546 | `SELECT app_func(id) FROM target_tab;` | 通过 | 10:29:11；TiDB 返回 `FUNCTION catalog1.app_func does not exist` | — | — |
| 547 | `SELECT app_aggregate(id) FROM target_tab;` | 通过 | 10:29:11；TiDB 返回 `FUNCTION catalog1.app_aggregate does not exist` | — | — |
| 548 | `SELECT app_func(app_aggregate(MAX(id))), COUNT(*) FROM target_tab;` | 通过 | 10:29:11；TiDB 返回 `FUNCTION catalog1.app_aggregate does not exist` | — | — |
| 549 | `SELECT analytics.COUNT(id) FROM target_tab;` | 通过 | 10:29:11；TiDB 返回 `FUNCTION analytics.count does not exist` | — | — |
| 550 | `` SELECT `COUNT`(id) FROM target_tab; `` | 通过 | 10:29:11；TiDB 返回 `FUNCTION catalog1.count does not exist` | — | — |
| 551 | `UPDATE target_tab SET value_col = app_func(value_col);` | 通过 | 10:29:11；TiDB 返回 `FUNCTION catalog1.app_func does not exist`，没有更新 | — | — |
| 552 | `SELECT COUNT(*) AS row_count, SUM(value_col) AS total_value FROM target_tab;` | 通过 | 10:29:11；页面追加 `LIMIT 1000`，结果 `3` / `60` | — | — |
| 553 | `WITH data AS (SELECT NULL AS x UNION ALL SELECT 1 ) SELECT x, IFNULL(x,'x has no value') FROM data;` | 通过 | 10:30:01；追加 `LIMIT 1000`，2 行 | — | — |
| 554 | `WITH RECURSIVE d AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM d WHERE n<10) SELECT n, NULLIF(n+n, n+2) FROM d;` | 通过 | 10:30:01；追加 `LIMIT 1000`，10 行 | — | — |
| 555 | `WITH RECURSIVE d AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM d WHERE n<10) SELECT n, CASE WHEN n MOD 2 THEN "odd" ELSE "even" END FROM d;` | 通过 | 10:30:01；追加 `LIMIT 1000`，10 行 | — | — |
| 556 | `WITH RECURSIVE d AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM d WHERE n<10) SELECT n, IF(n MOD 2, "odd", "even") FROM d;` | 通过 | 10:30:02；追加 `LIMIT 1000`，10 行 | — | — |
| 557 | `SELECT JSON_ARRAY(1,2,3,4,5), JSON_ARRAY("foo", "bar");` | 通过 | 10:30:02；1 行 | — | — |
| 558 | `SELECT JSON_OBJECT("database", "TiDB", "distributed", TRUE);` | 通过 | 10:30:02；1 行 | — | — |
| 559 | `` SELECT JSON_QUOTE('The name is "O\'Neil"'); `` | 通过 | 10:30:02；1 行，编辑器可访问值核实反斜杠 | — | — |
| 560 | `SELECT INET_NTOA(INET_ATON('192.168.1.2') & INET_ATON('255.255.255.0'));` | 通过 | 10:30:33；1 行 | — | — |
| 561 | `SELECT BIT_COUNT(b'00101001');` | 通过 | 10:30:33；1 行 | — | — |
| 562 | `SELECT CONV(b'1010' & b'1000',10,2);` | 通过 | 10:30:33；1 行 | — | — |
| 563 | `WITH RECURSIVE cte(n) AS ( SELECT 0 AS n UNION ALL SELECT n+1 FROM cte WHERE n<11 ) SELECT n,1024>>n,LPAD(CONV(1024>>n,10,2),11,0) FROM cte;` | 通过 | 10:30:33；追加 `LIMIT 1000`，12 行 | — | — |
| 564 | `WITH RECURSIVE cte(n) AS ( SELECT 0 AS n UNION ALL SELECT 1+n FROM cte WHERE n<10 ) SELECT n,1<<n,LPAD(CONV(1<<n,10,2),11,0) FROM cte;` | 通过 | 10:30:33；追加 `LIMIT 1000`，11 行 | — | — |
| 565 | `SELECT CONV(~ b'1111111111111111111111111111111111111111111111110000111100001111',10,2);` | 通过 | 10:30:33；1 行 | — | — |
| 566 | `SELECT CONV(b'1010' \| b'1100',10,2);` | 通过 | 10:30:33；1 行；表格中的反斜杠仅用于 Markdown 转义，页面执行原 `\|` 运算符 | — | — |
| 567 | `SELECT BIT_COUNT(0x29), CONV(0x29,16,2);` | 通过 | 10:30:33；1 行 | — | — |
| 568 | `SELECT INET_NTOA(INET_ATON('192.168.1.1') & INET_ATON('255.255.255.0'));` | 通过 | 10:30:34；1 行 | — | — |
| 569 | `SELECT CONV(b'1010' ^ b'1100',10,2);` | 通过 | 10:30:34；1 行 | — | — |
| 570 | `SELECT BIT_COUNT(INET_ATON('255.255.255.0'));` | 通过 | 10:30:34；1 行 | — | — |
| 571 | `SELECT CHAR(65,66,67);` | 通过 | 10:31:43；1 行 | — | — |
| 572 | `SELECT REGEXP_REPLACE('TooDB', 'o{2}', 'i');` | 通过 | 10:31:43；1 行 | — | — |
| 573 | `SELECT 'aaa' LIKE 'a%', 'aaa' NOT LIKE 'a%';` | 通过 | 10:31:43；1 行 | — | — |
| 574 | `SELECT REGEXP_INSTR('abcabc','a',2);` | 通过 | 10:31:43；1 行 | — | — |
| 575 | `SELECT ORD('e'), ORD('ë'), HEX('e'), HEX('ë');` | 通过 | 10:31:43；1 行 | — | — |
| 576 | `SELECT TO_BASE64(6);` | 通过 | 10:31:43；1 行 | — | — |
| 577 | `SELECT REGEXP_INSTR('abcabc','a');` | 通过 | 10:31:43；1 行 | — | — |
| 578 | `SELECT REGEXP_REPLACE('TooDB', 'o', 'i',1,1);` | 通过 | 10:31:43；1 行 | — | — |
| 579 | `SET NAMES 'utf8mb4';` | 通过 | 10:31:43；0 rows affected | — | — |
| 580 | `SELECT EXPORT_SET(b'01010101', 'x', '_', '', 8);` | 通过 | 10:31:43；1 行 | — | — |
| 581 | `SELECT CONCAT_WS(',', 'TiDB Server', NULL, 'PD');` | 通过 | 10:31:43；1 行 | — | — |
| 582 | `SELECT 'Ti' 'DB' ' ' 'Server';` | 通过 | 10:31:43；1 行 | — | — |
| 583 | `SELECT REPEAT('ha',3);` | 通过 | 10:31:43；1 行 | — | — |
| 584 | `SELECT UNHEX('54694442');` | 通过 | 10:31:43；1 行 | — | — |
| 585 | `SELECT REGEXP_REPLACE('TooDB', 'o', 'i',1,2);` | 通过 | 10:31:43；1 行 | — | — |
| 586 | `SELECT MAKE_SET(b'001','foo','bar','baz');` | 通过 | 10:31:43；1 行 | — | — |
| 587 | `SELECT ELT(3, 'This', 'is', 'TiDB');` | 通过 | 10:31:43；1 行 | — | — |
| 588 | `SELECT CONCAT_WS(',', 'TiDB Server', NULL);` | 通过 | 10:31:43；1 行 | — | — |
| 589 | `SELECT MID('abcdef',2);` | 通过 | 10:31:43；1 行 | — | — |
| 590 | `SELECT REGEXP_INSTR('abcabc','a',1,2);` | 通过 | 10:31:43；1 行 | — | — |
| 591 | `SELECT REGEXP_INSTR('abcabc','a',1,1,1);` | 通过 | 10:31:43；1 行 | — | — |
| 592 | `SELECT REGEXP_INSTR('abcabc','A' COLLATE utf8mb4_bin);` | 通过 | 10:31:43；1 行 | — | — |
| 593 | `SELECT MID('abcdef',2,3);` | 通过 | 10:31:43；1 行 | — | — |
| 594 | `SELECT CONCAT('TiDB', NULL, 'Server');` | 通过 | 10:31:43；1 行 | — | — |
| 595 | `SELECT SUBSTRING_INDEX('www.tidbcloud.com', '.', 2);` | 通过 | 10:31:43；1 行 | — | — |
| 596 | `SELECT CONCAT_WS(',', 'TiDB Server', '', 'PD');` | 通过 | 10:32:56；1 行 | — | — |
| 597 | `SELECT EXPORT_SET(b'00001111', 'x', '_', '', 8);` | 通过 | 10:32:56；1 行 | — | — |
| 598 | `SELECT HEX(WEIGHT_STRING('ab' AS CHAR(3))) AS char_result, HEX(WEIGHT_STRING('ab' AS BINARY(3))) AS binary_result;` | 通过 | 10:32:56；1 行 | — | — |
| 599 | `SELECT CONCAT_WS('', 'TiDB Server', 'TiKV', 'PD');` | 通过 | 10:32:56；1 行 | — | — |
| 600 | `SELECT BIN(123), BIN('123q123');` | 通过 | 10:32:56；1 行 | — | — |
| 601 | `SELECT REGEXP_LIKE('abc','^A','i');` | 通过 | 10:32:56；1 行 | — | — |
| 602 | `SELECT MAKE_SET(b'111','foo','bar','baz');` | 通过 | 10:32:56；1 行 | — | — |
| 603 | `SELECT BIN(-7);` | 通过 | 10:32:56；1 行 | — | — |
| 604 | `SELECT REGEXP_REPLACE('TooDB', 'O{2}','i',1,1);` | 通过 | 10:32:56；1 行 | — | — |
| 605 | `SELECT REGEXP_REPLACE('TooDB', 'o{2}', 'i',3);` | 通过 | 10:32:56；1 行 | — | — |
| 606 | `SELECT SUBSTRING_INDEX('www.tidbcloud.com', '.', -1);` | 通过 | 10:32:56；1 行 | — | — |
| 607 | `SELECT MAKE_SET(b'100','foo','bar','baz');` | 通过 | 10:32:56；1 行 | — | — |
| 608 | `WITH RECURSIVE nr(n) AS ( SELECT 1 AS n UNION ALL SELECT n+1 FROM nr WHERE n<20 ) SELECT n, REPEAT('x',n) FROM nr;` | 通过 | 10:32:56；追加 `LIMIT 1000`，20 行 | — | — |
| 609 | `SELECT REGEXP_LIKE('abc','^a');` | 通过 | 10:32:56；1 行 | — | — |
| 610 | `SELECT REGEXP_LIKE('abc','^A');` | 通过 | 10:32:56；1 行 | — | — |
| 611 | `SELECT ASCII('A'), ASCII('TiDB'), ASCII(23);` | 通过 | 10:32:56；1 行 | — | — |
| 612 | `SELECT 'aaa' LIKE 'b%', 'aaa' NOT LIKE 'b%';` | 通过 | 10:32:56；1 行 | — | — |
| 613 | `SELECT REGEXP_INSTR('abcabc','A',1,1,0,'i');` | 通过 | 10:32:56；1 行 | — | — |
| 614 | `SELECT LTRIM('    hello');` | 通过 | 10:32:56；1 行；执行信息会归一化空格为 `LTRIM(' hello')`，页面提交文本为引号内四空格 | — | — |
| 615 | `SELECT REGEXP_INSTR('abcabc','A',1,1,0,'');` | 通过 | 10:32:56；1 行 | — | — |
| 616 | `` SELECT REGEXP_REPLACE('abcd','(.*)(.{2})$','\\1') AS s; `` | 通过 | 10:32:56；1 行；编辑器可访问值核实引号内双反斜杠 | — | — |
| 617 | `SELECT UPPER('bigdata') AS result_upper, UPPER(null) AS result_null;` | 通过 | 10:32:56；1 行 | — | — |
| 618 | `SELECT REGEXP_SUBSTR('This is TiDB','Ti.{2}');` | 通过 | 10:32:56；1 行 | — | — |
| 619 | `SELECT UCASE('bigdata') AS result_upper, UCASE(null) AS result_null;` | 通过 | 10:32:57；1 行 | — | — |
| 620 | `SELECT CONCAT_WS(NULL, 'TiDB Server', 'TiKV', 'PD');` | 通过 | 10:32:57；1 行 | — | — |
| 621 | `SELECT JSON_OVERLAPS( '{"languages": ["Go","Rust","C#"]}', '{"languages": ["Go","Rust","C#"], "arch": ["arm64"]}' ) AS 'Overlaps';` | 通过 | 10:34:11；1 行 | — | — |
| 622 | `SELECT JSON_KEYS('{"name": {"first": "John", "last": "Doe"}, "type": "Person"}', '$.name');` | 通过 | 10:34:11；1 行 | — | — |
| 623 | `SELECT j->'$.foo', JSON_EXTRACT(j, '$.foo') FROM ( SELECT '{"foo": "bar", "aaa": 5}' AS j ) AS tbl;` | 通过 | 10:34:11；追加 `LIMIT 1000`，1 行 | — | — |
| 624 | `SELECT JSON_CONTAINS('["a","b","c"]','"a"');` | 通过 | 10:34:11；1 行 | — | — |
| 625 | `SELECT JSON_CONTAINS('["a","b","c"]','"e"');` | 通过 | 10:34:11；1 行 | — | — |
| 626 | `SELECT JSON_SEARCH('{"a": ["aa", "bb", "cc"], "b": ["cc", "dd"]}','one','cc');` | 通过 | 10:34:11；1 行 | — | — |
| 627 | `SELECT JSON_CONTAINS_PATH('{"foo": "bar", "aaa": 5}','all','$.foo', '$.aaa');` | 通过 | 10:34:11；1 行 | — | — |
| 628 | `SELECT JSON_SEARCH('{"a": ["aa", "bb", "cc"], "b": ["cc", "dd"]}','all','cc');` | 通过 | 10:34:11；1 行 | — | — |
| 629 | `SELECT JSON_KEYS('{"name": {"first": "John", "last": "Doe"}, "type": "Person"}');` | 通过 | 10:34:11；1 行 | — | — |
| 630 | `SELECT JSON_EXTRACT('{"foo": "bar", "aaa": 5}', '$.foo');` | 通过 | 10:34:11；1 行 | — | — |
| 631 | `SELECT JSON_CONTAINS_PATH('{"foo": "bar", "aaa": 5}','all','$.foo');` | 通过 | 10:34:11；1 行 | — | — |
| 632 | `SELECT JSON_OVERLAPS( '{"languages": ["Go","Rust","C#"]}', '{"languages": ["Go","Rust","C#"]}' ) AS 'Overlaps';` | 通过 | 10:34:11；1 行 | — | — |
| 633 | `SELECT JSON_CONTAINS_PATH('{"foo": "bar", "aaa": 5}','all','$.bar');` | 通过 | 10:34:11；1 行 | — | — |
| 634 | `SELECT JSON_CONTAINS('{"foo": "bar", "aaa": 5}','"bar"');` | 通过 | 10:34:11；1 行 | — | — |
| 635 | `SELECT JSON_CONTAINS('{"foo": "bar", "aaa": 5}','{"foo": "bar"}');` | 通过 | 10:34:11；1 行 | — | — |
| 636 | `SELECT JSON_CONTAINS('{"foo": "bar", "aaa": 5}','"bar"', '$.foo');` | 通过 | 10:34:11；1 行 | — | — |
| 637 | `SELECT JSON_OVERLAPS( '{"languages": ["Go","Rust","C#"]}', '{"languages": ["Go","Rust"]}' ) AS 'Overlaps';` | 通过 | 10:34:11；1 行 | — | — |
| 638 | `SELECT JSON_REPLACE('{"a": 41, "b": 62}','$.b',42,'$.c',43);` | 通过 | 10:35:08；1 行 | — | — |
| 639 | `SELECT JSON_INSERT( '{"language": ["Go", "Rust", "C++"]}', '$.architecture', 'riscv', '$.os', JSON_ARRAY("linux","freebsd") ) AS "Demo";` | 通过 | 10:35:08；1 行 | — | — |
| 640 | `SELECT JSON_ARRAY_INSERT('["Car", "Boat", "Train"]', '$[0]', "Airplane") AS "Transport options";` | 通过 | 10:35:08；1 行 | — | — |
| 641 | `SELECT JSON_MERGE_PATCH( '{"a": 1, "b": 2}', '{"a": 100}', '{"c": 300}' );` | 通过 | 10:35:08；1 行 | — | — |
| 642 | `SELECT JSON_UNQUOTE(JSON_EXTRACT('{"database": "TiDB"}', '$.database'));` | 通过 | 10:35:08；1 行 | — | — |
| 643 | `SELECT JSON_UNQUOTE('"foo"');` | 通过 | 10:35:08；1 行 | — | — |
| 644 | `SELECT JSON_ARRAY_APPEND('["Car", "Boat", "Train"]', '$', "Airplane") AS "Transport options";` | 通过 | 10:35:08；1 行 | — | — |
| 645 | `SELECT JSON_EXTRACT('{"database": "TiDB"}', '$.database');` | 通过 | 10:35:08；1 行 | — | — |
| 646 | `SELECT JSON_REMOVE('{"a": 61, "b": 62, "c": 63}','$.b');` | 通过 | 10:35:08；1 行 | — | — |
| 647 | `SELECT JSON_REMOVE('{"a": 61, "b": 62, "c": 63}','$.b','$.c');` | 通过 | 10:35:08；1 行 | — | — |
| 648 | `SELECT JSON_REPLACE('{"a": 41, "b": 62}','$.b',42);` | 通过 | 10:35:08；1 行 | — | — |
| 649 | `SELECT JSON_SET('{"version": 1.1, "name": "example"}','$.version',1.2,'$.branch', "main");` | 通过 | 10:35:08；1 行 | — | — |
| 650 | `SELECT JSON_MERGE_PRESERVE('{"a": 1, "b": 2}','{"a": 100}', '{"c": 300}');` | 通过 | 10:35:08；1 行 | — | — |
| 651 | `SELECT JSON_ARRAY_INSERT('["Car", "Boat", "Train"]', '$[1]', "Airplane") AS "Transport options";` | 通过 | 10:35:08；1 行 | — | — |
| 652 | `SELECT JSON_ARRAY_APPEND('{"transport_options": ["Car", "Boat", "Train"]}', '$.transport_options', "Airplane") AS "Transport options";` | 通过 | 10:35:08；1 行 | — | — |
| 653 | `SELECT JSON_SET('{"version": 1.1, "name": "example"}','$.version',1.2);` | 通过 | 10:35:08；1 行 | — | — |
| 654 | `SELECT JSON_INSERT('{"a": 61, "b": 62}', '$.a', 41, '$.c', 63);` | 通过 | 10:35:08；1 行 | — | — |
| 655 | `SELECT JSON_TYPE('"2025-06-14"'),JSON_TYPE(CAST(CAST('2025-06-14' AS date) AS json));` | 通过 | 10:36:07；1 行 | — | — |
| 656 | `SELECT JSON_VALID('{"foo"="bar"}');` | 通过 | 10:36:07；1 行 | — | — |
| 657 | `SELECT JSON_DEPTH('{"weather": {"current": "sunny"}}');` | 通过 | 10:36:07；1 行 | — | — |
| 658 | `SELECT '"2025-06-14"',CAST(CAST('2025-06-14' AS date) AS json);` | 通过 | 10:36:07；1 行 | — | — |
| 659 | `WITH demo AS ( SELECT 'null' AS 'v' UNION SELECT '"foobar"' UNION SELECT 'true' UNION SELECT '5' UNION SELECT '1.14' UNION SELECT '[]' UNION SELECT '{}' UNION SELECT POW(2,63) ) SELECT v, JSON_TYPE(v) FROM demo ORDER BY 2;` | 通过 | 10:36:07；8 行，页面追加 `LIMIT 1000` | — | — |
| 660 | `SELECT JSON_LENGTH('{"weather": {"current": "sunny", "tomorrow": "cloudy"}}','$.weather');` | 通过 | 10:36:07；1 行 | — | — |
| 661 | `SELECT JSON_LENGTH('{"weather": {"current": "sunny", "tomorrow": "cloudy"}}','$');` | 通过 | 10:36:07；1 行 | — | — |
| 662 | `SELECT JSON_VALID('{"foo": "bar"}');` | 通过 | 10:36:07；1 行 | — | — |
| 663 | `SELECT JSON_SCHEMA_VALID('{"type": "object"}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 664 | `SELECT JSON_SCHEMA_VALID('{"enum": ["TiDB", "MySQL"]}', '"TiDB"');` | 通过 | 10:36:07；1 行 | — | — |
| 665 | `SELECT JSON_SCHEMA_VALID('{"anyOf": [{"type": "string"},{"type": "integer"}]}', '["TiDB", "MySQL"]');` | 通过 | 10:36:07；1 行 | — | — |
| 666 | `SELECT JSON_SCHEMA_VALID('{"required": ["fruits","vegetables"]}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 667 | `SELECT JSON_SCHEMA_VALID('{"anyOf": [{"type": "string"},{"type": "integer"}]}', '5');` | 通过 | 10:36:07；1 行 | — | — |
| 668 | `SELECT JSON_SCHEMA_VALID('{"type": "string", "pattern": "^Ti"}', '"TiDB"');` | 通过 | 10:36:07；1 行 | — | — |
| 669 | `SELECT JSON_SCHEMA_VALID('{"enum": ["TiDB", "MySQL"]}', '"SQLite"');` | 通过 | 10:36:07；1 行 | — | — |
| 670 | `SELECT JSON_SCHEMA_VALID('{"format": "ipv4"}', '"127.0.0.1"');` | 通过 | 10:36:07；1 行 | — | — |
| 671 | `SELECT JSON_SCHEMA_VALID('{"anyOf": [{"type": "string"},{"type": "integer"}]}', '"TiDB"');` | 通过 | 10:36:07；1 行 | — | — |
| 672 | `SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "array", "minItems": 3}}}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 673 | `SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "array"}}}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 674 | `SELECT JSON_SCHEMA_VALID('{"required": ["fruits","vegetables","grains"]}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 675 | `SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "string"}}}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 676 | `SELECT JSON_SCHEMA_VALID('{"format": "ipv4"}', '"327.0.0.1"');` | 通过 | 10:36:07；1 行 | — | — |
| 677 | `SELECT JSON_SCHEMA_VALID('{"type": "string", "pattern": "^Ti"}', '"PingCAP"');` | 通过 | 10:36:07；1 行 | — | — |
| 678 | `SELECT JSON_SCHEMA_VALID('{"enum": ["TiDB", "MySQL"]}', '"MySQL"');` | 通过 | 10:36:07；1 行 | — | — |
| 679 | `SET @j := '{"fruits": ["orange", "apple", "pear"], "vegetables": ["carrot", "pepper", "kale"]}';` | 通过 | 10:36:07；0 rows affected | — | — |
| 680 | `SELECT JSON_SCHEMA_VALID('{"properties": {"fruits": {"type": "array", "minItems": 4}}}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 681 | `SELECT JSON_SCHEMA_VALID('{"type": "array"}',@j);` | 通过 | 10:36:07；1 行 | — | — |
| 682 | `SELECT JSON_SCHEMA_VALID('{"type": "integer", "minimum": 40, "maximum": 45}', '123');` | 通过 | 10:36:07；1 行 | — | — |
| 683 | `SELECT JSON_STORAGE_FREE('{}');` | 通过 | 10:37:03；1 行 | — | — |
| 684 | `SELECT JSON_STORAGE_SIZE('{}');` | 通过 | 10:37:03；1 行 | — | — |
| 685 | `SELECT CONNECTION_ID();` | 通过 | 10:37:03；1 行 | — | — |
| 686 | `SELECT DATABASE();` | 通过 | 10:37:03；1 行 | — | — |
| 687 | `SELECT CURRENT_USER();` | 通过 | 10:37:03；1 行 | — | — |
| 688 | `SELECT BENCHMARK(5, SLEEP(2));` | 通过 | 10:37:14；等待约 10.7 秒后返回 1 行 | — | — |
| 689 | `SELECT FOUND_ROWS();` | 通过 | 10:37:14；1 行 | — | — |
| 690 | `SELECT 1 UNION ALL SELECT 2;` | 通过 | 10:37:14；2 行 | — | — |
| 691 | `SELECT USER(), CURRENT_USER();` | 通过 | 10:37:14；1 行 | — | — |
| 692 | `SELECT FORMAT_NANO_TIME(1000000);` | 通过 | 10:37:14；1 行 | — | — |
| 693 | `SELECT FORMAT_BYTES(10*1024*1024);` | 通过 | 10:37:14；1 行 | — | — |
| 694 | `SELECT IS_UUID('eb48c08c-eb71-11ee-bacf-5405db7aad56');` | 通过 | 10:37:14；1 行 | — | — |
| 695 | `SELECT IS_IPV4('127.0.0.1');` | 通过 | 10:37:14；1 行 | — | — |
| 696 | `SELECT UUID();` | 通过 | 10:37:14；1 行 | — | — |
| 697 | `SELECT INET6_NTOA(0x00000000000000000000000000000001);` | 通过 | 10:37:14；1 行 | — | — |
| 698 | `SELECT INET6_ATON('::1');` | 通过 | 10:37:14；1 行 | — | — |
| 699 | `SELECT 'value' AS 'column name' UNION ALL SELECT 'another value';` | 通过 | 10:37:14；2 行 | — | — |
| 700 | `SELECT INET_ATON('127.0.0.1');` | 通过 | 10:37:14；1 行 | — | — |
| 701 | `SELECT IS_IPV4('300.0.0.1');` | 通过 | 10:37:14；1 行 | — | — |
| 702 | `SELECT INET_NTOA(2130706433);` | 通过 | 10:37:14；1 行 | — | — |
| 703 | `SELECT NAME_CONST('column name', 'value') UNION ALL SELECT 'another value';` | 通过 | 10:37:14；2 行 | — | — |
| 704 | `SELECT IS_IPV4_COMPAT(INET6_ATON('::127.0.0.1'));` | 通过 | 10:37:14；1 行 | — | — |
| 705 | `SELECT IS_IPV6('::1');` | 通过 | 10:37:14；1 行 | — | — |
| 706 | `SELECT SLEEP(1.5);` | 通过 | 10:37:15；等待约 1.5 秒后返回 1 行 | — | — |
| 707 | `SELECT IS_IPV4_MAPPED(INET6_ATON('::ffff:127.0.0.1'));` | 通过 | 10:37:15；1 行 | — | — |
| 708 | `SELECT NEXTVAL(s1);` | 通过 | 10:38:43；1 行；本轮对象已有值 | — | — |
| 709 | `CREATE SEQUENCE s1;` | 通过 | 10:38:43；TiDB 返回 `Table 'catalog1.s1' already exists`，本轮未新建 | — | — |
| 710 | `SELECT NEXT VALUE FOR s1;` | 通过 | 10:38:43；1 行 | — | — |
| 711 | `SELECT LASTVAL(s1);` | 通过 | 10:38:43；1 行 | — | — |
| 712 | `SELECT SETVAL(s1, 10);` | 通过 | 10:38:43；1 行，测试结束恢复到 fixture 值 10 | — | — |
| 713 | `ADMIN SHOW DDL;` | 通过 | 10:38:43；1 行 | — | — |
| 714 | `ADMIN CHECK TABLE t;` | 通过 | 10:38:43；0 rows affected，历史创建的 `t` 已存在 | — | — |
| 715 | `SHOW CONFIG;` | 通过 | 10:38:43；分批收到 30、200 行，`200 row retrieved finish` | — | — |
| 716 | `SHOW TABLE t REGIONS;` | 通过 | 10:38:43；1 行，历史创建的 `t` 已存在 | — | — |
| 717 | `SHOW STATS_HEALTHY;` | 通过 | 10:38:43；9 行 | — | — |
| 718 | `ADMIN SHOW DDL JOB QUERIES 51;` | 通过 | 10:38:43；0 行 | — | — |
| 719 | `ADMIN SHOW DDL JOBS;` | 通过 | 10:38:43；10 行 | — | — |
| 720 | `ADMIN CHECK INDEX tbl_name idx_name;` | 通过 | 10:38:43；TiDB 返回 `Table 'catalog1.tbl_name' doesn't exist` | — | — |
| 721 | `ADMIN CHECKSUM TABLE t1;` | 通过 | 10:38:43；1 行，历史创建的 `t1` 已存在 | — | — |
| 722 | `CREATE TABLE t1(id INT PRIMARY KEY);` | 通过 | 10:38:43；TiDB 返回 `Table 'catalog1.t1' already exists`，本轮未新建 | — | — |
| 723 | `INSERT INTO t1 VALUES (1),(2),(3);` | 通过 | 10:38:43；TiDB 返回 `Duplicate entry '1' for key 't1.PRIMARY'`，本轮未插入 | — | — |
| 724 | `SELECT COUNT(*) AS row_count FROM t1;` | 通过 | 10:38:43；页面追加 `LIMIT 1000`，1 行 | — | — |
| 725 | `FLUSH CLIENT_ERRORS_SUMMARY;` | 通过 | 10:39:31；0 rows affected | — | — |
| 726 | `ANALYZE TABLE t PARTITION p1;` | 通过 | 10:39:31；TiDB 返回 `Partition management on a not partitioned table is not possible` | — | — |
| 727 | `ANALYZE TABLE t INDEX idx_t;` | 通过 | 10:39:31；TiDB 返回 `Index 'idx_t' in field list does not exist in table 't'` | — | — |
| 728 | `flush privileges, status;` | 通过 | 10:39:31；TiDB 返回 SQL syntax error，line 1 column 17 near `, status;` | — | — |
| 729 | `SELECT VEC_NEGATIVE_INNER_PRODUCT('[1, 2]', '[3, 4]');` | 通过 | 10:39:31；1 行 | — | — |
| 730 | `SELECT VEC_FROM_TEXT('[1, 2]') + VEC_FROM_TEXT('[3, 4]');` | 通过 | 10:39:31；1 行 | — | — |
| 731 | `SELECT VEC_L2_DISTANCE('[0, 3]', '[4, 0]');` | 通过 | 10:39:31；1 行 | — | — |
| 732 | `SELECT VEC_DIMS('[1, 2, 3]');` | 通过 | 10:39:31；1 行 | — | — |
| 733 | `SELECT VEC_AS_TEXT('[1.000, 2.5]');` | 通过 | 10:39:31；1 行 | — | — |
| 734 | `SELECT VEC_L2_NORM('[3, 4]');` | 通过 | 10:39:31；1 行 | — | — |
| 735 | `SELECT VEC_L1_DISTANCE('[0, 0]', '[3, 4]');` | 通过 | 10:39:31；1 行 | — | — |
| 736 | `SELECT VEC_COSINE_DISTANCE('[1, 1]', '[-1, -1]');` | 通过 | 10:39:31；1 行 | — | — |
| 737 | `SELECT VEC_DIMS('[]');` | 通过 | 10:39:31；1 行 | — | — |
| 738 | `SPLIT TABLE t BETWEEN (-9223372036854775808) AND (9223372036854775807) REGIONS 16;` | 通过 | 10:39:31；1 行；与历史缺 `INT` 边界的结果不同，尚未验证物理 Region | — | — |
| 739 | `SPLIT TABLE t BY (10000), (90000);` | 通过 | 10:39:31；1 行；未验证物理 Region | — | — |
| 740 | `SPLIT TABLE t INDEX idx1 BETWEEN ("a") AND ("z") REGIONS 25;` | 通过 | 10:39:31；TiDB 返回 `Key 'idx1' doesn't exist in table 't'` | — | — |
| 741 | `SPLIT PARTITION TABLE t INDEX idx BETWEEN (1000) AND (10000) REGIONS 2;` | 通过 | 10:39:31；TiDB 返回 `PARTITION () clause on non partitioned table` | — | — |
| 742 | `SPLIT PARTITION TABLE t PARTITION (p1,p2) INDEX idx BETWEEN (0) AND (20000) REGIONS 2;` | 通过 | 10:39:31；TiDB 返回 `PARTITION () clause on non partitioned table` | — | — |
| 743 | `SPLIT REGION FOR TABLE t BY (100);` | 通过 | 10:39:31；1 行；未验证物理 Region | — | — |
| 744 | `SPLIT REGION FOR PARTITION TABLE t PARTITION (p1) BY (1000), (9000);` | 通过 | 10:39:31；TiDB 返回 `PARTITION () clause on non partitioned table` | — | — |
| 745 | `SELECT 1 AS recovery_ok;` | 通过 | 10:39:31；1 行 | — | — |
| 746 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+3 FROM cte WHERE n<30 ) SELECT n, ROW_NUMBER() OVER () FROM cte;` | 通过 | 10:40:29；追加 `LIMIT 1000`，11 行 | — | — |
| 747 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, NTILE(5) OVER (), NTILE(2) OVER () FROM cte;` | 通过 | 10:40:29；追加 `LIMIT 1000`，10 行 | — | — |
| 748 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, LAST_VALUE(n) OVER (PARTITION BY n<=5) FROM cte ORDER BY n;` | 通过 | 10:40:29；追加 `LIMIT 1000`，10 行 | — | — |
| 749 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+2 FROM cte WHERE n<6 ) SELECT *, CUME_DIST() OVER(ORDER BY n) FROM cte;` | 通过 | 10:40:29；追加 `LIMIT 1000`，4 行 | — | — |
| 750 | `SELECT *, RANK() OVER (ORDER BY n), DENSE_RANK() OVER (ORDER BY n) FROM ( SELECT 5 AS 'n' UNION ALL SELECT 8 UNION ALL SELECT 5 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32) a;` | 通过 | 10:40:29；追加 `LIMIT 1000`，6 行 | — | — |
| 751 | `SELECT *, PERCENT_RANK() OVER (ORDER BY n), PERCENT_RANK() OVER (ORDER BY n DESC) FROM ( SELECT 5 AS 'n' UNION ALL SELECT 8 UNION ALL SELECT 5 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32) a;` | 通过 | 10:40:29；追加 `LIMIT 1000`，6 行 | — | — |
| 752 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, FIRST_VALUE(n) OVER w AS 'First', NTH_VALUE(n, 2) OVER w AS 'Second', NTH_VALUE(n, 3) OVER w AS 'Third', LAST_VALUE(n) OVER w AS 'Last' FROM cte WINDOW w AS (PARTITION BY n<=5) ORDER BY n;` | 通过 | 10:40:29；追加 `LIMIT 1000`，10 行 | — | — |
| 753 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, LEAD(n) OVER () FROM cte;` | 通过 | 10:40:29；追加 `LIMIT 1000`，10 行 | — | — |
| 754 | `SELECT n, FIRST_VALUE(n) OVER (PARTITION BY n MOD 2 ORDER BY n), FIRST_VALUE(n) OVER (PARTITION BY n <= 2 ORDER BY n) FROM ( SELECT 1 AS 'n' UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 ) a ORDER BY n;` | 通过 | 10:40:29；追加 `LIMIT 1000`，4 行 | — | — |
| 755 | `SELECT *, DENSE_RANK() OVER (ORDER BY n) FROM ( SELECT 5 AS 'n' UNION ALL SELECT 8 UNION ALL SELECT 5 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32) a;` | 通过 | 10:40:29；追加 `LIMIT 1000`，6 行 | — | — |
| 756 | `WITH RECURSIVE cte(n) AS ( SELECT 1 UNION SELECT n+1 FROM cte WHERE n<10 ) SELECT n, LAG(n) OVER () FROM cte;` | 通过 | 10:40:29；追加 `LIMIT 1000`，10 行 | — | — |
| 757 | `SHOW GLOBAL VARIABLES LIKE 'tidb_enable_row_level_checksum';` | 通过 | 10:41:14；1 行，值 `OFF` | — | — |
| 758 | `SELECT TIDB_DECODE_SQL_DIGESTS(@digests);` | 通过 | 10:41:45；1 行 | — | — |
| 759 | `SELECT TIDB_ENCODE_SQL_DIGEST('SELECT 2');` | 通过 | 10:41:45；1 行 | — | — |
| 760 | `CREATE TABLE t(id int PRIMARY KEY, a int, KEY `idx` (a));` | 通过 | 10:41:45；TiDB 返回 `Table 'catalog1.t' already exists`，本轮未新建 | — | — |
| 761 | `SELECT *, TIDB_ROW_CHECKSUM() FROM t WHERE id = 1;` | 通过 | 10:41:45；页面追加 `LIMIT 1000`，TiDB 返回 `Unknown column 'id' in 'where clause'`；当前 `t` 是历史聚合 fixture 的三列结构 | — | — |
| 762 | `SELECT TIDB_DECODE_KEY('7480000000000000845f728000000000000001');` | 通过 | 10:41:45；1 行 | — | — |
| 763 | `SELECT TIDB_DECODE_SQL_DIGESTS(@digests, 10);` | 通过 | 10:41:45；1 行 | — | — |
| 764 | `SELECT TIDB_ENCODE_INDEX_KEY('test', 't', 'idx', 2, 1);` | 通过 | 10:41:45；TiDB 返回 `Table 'test.t' doesn't exist` | — | — |
| 765 | `SELECT TIDB_ENCODE_SQL_DIGEST('SELECT 1');` | 通过 | 10:41:45；1 行 | — | — |
| 766 | `CREATE TABLE test(id INT PRIMARY KEY CLUSTERED, a INT, b INT, UNIQUE KEY uk((tidb_shard(a)), a));` | 通过 | 10:41:45；TiDB 返回 `Table 'catalog1.test' already exists`，本轮未新建 | — | — |
| 767 | `SELECT VITESS_HASH(123);` | 通过 | 10:41:45；1 行 | — | — |
| 768 | `SELECT @@tidb_current_ts;` | 通过 | 10:41:45；1 行 | — | — |
| 769 | `SELECT tidb_decode_key('7480000000000000FF3E5F720400000000FF0000000601633430FF3338646232FF2D64FF3531632D3131FF65FF622D386337352DFFFF3830653635303138FFFF61396265000000FF00FB000000000000F9');` | 通过 | 10:41:45；1 行 | — | — |
| 770 | `SET @digests = '["e6f07d43b5c21db0fbb9a31feac2dc599787763393dd5acbfad80e247eb02ad5","38b03afa5debbdf0326a014dbe5012a62c51957f1982b3093e748460f8b00821","e5796985ccafe2f71126ed6c0ac939ffa015a8c0744a24b7aee6d587103fd2f7"]';` | 通过 | 10:41:45；0 rows affected | — | — |
| 771 | `SELECT TIDB_IS_DDL_OWNER();` | 通过 | 10:41:45；1 行 | — | — |
| 772 | `CREATE TABLE t (id INT PRIMARY KEY, k INT, c CHAR(1));` | 通过 | 10:43:28；TiDB 返回 `Table 'catalog1.t' already exists`，本轮未新建 | — | — |
| 773 | `SET GLOBAL tidb_enable_row_level_checksum = ON;` | 通过 | 10:43:28；0 rows affected；原值 `OFF` 已预查，同批 R331 恢复 | — | — |
| 774 | `SELECT TIDB_SHARD(12373743746);` | 通过 | 10:43:28；1 行 | — | — |
| 775 | `SELECT TIDB_PARSE_TSO_LOGICAL(450456244814610434);` | 通过 | 10:43:28；1 行 | — | — |
| 776 | `SELECT CURRENT_RESOURCE_GROUP();` | 通过 | 10:43:28；1 行 | — | — |
| 777 | `INSERT INTO t VALUES(1,2);` | 通过 | 10:43:28；TiDB 返回 `Column count doesn't match value count at row 1`，本轮未插入 | — | — |
| 778 | `SELECT TABLE_NAME, TIDB_DECODE_KEY(START_KEY), TIDB_DECODE_KEY(END_KEY) FROM information_schema.TIKV_REGION_STATUS WHERE TABLE_NAME='stock' AND IS_INDEX=0 ORDER BY START_KEY;` | 通过 | 10:43:28；TiDB 返回 `pd http client unavailable` | — | — |
| 779 | `SELECT TIDB_ENCODE_RECORD_KEY('test', 't', 1);` | 通过 | 10:43:28；TiDB 返回 `Table 'test.t' doesn't exist` | — | — |
| 780 | `SELECT TIDB_PARSE_TSO(@@tidb_current_ts);` | 通过 | 10:43:28；1 行 | — | — |
| 781 | `SELECT TIDB_PARSE_TSO_LOGICAL(450456244814610433);` | 通过 | 10:43:28；1 行 | — | — |
| 782 | `SET GLOBAL tidb_enable_row_level_checksum = OFF;` | 通过 | 10:43:28；0 rows affected | — | — |
| 783 | `SHOW GLOBAL VARIABLES LIKE 'tidb_enable_row_level_checksum';` | 通过 | 10:43:28；1 行，值 `OFF`，与原值一致 | — | — |
| 784 | `SELECT 1 AS recovery_ok;` | 通过 | 10:43:28；1 行、值 `1` | — | — |
| 785 | `SHOW TABLES FROM catalog1 LIKE 'codex_it_tidb_checksum_20260916_01';` | 通过 | 10:44:31；0 行，确认未占用 | — | — |
| 786 | `CREATE TABLE catalog1.codex_it_tidb_checksum_20260916_01 (id INT PRIMARY KEY, k INT NOT NULL);` | 通过 | 10:44:48；0 rows affected | — | — |
| 787 | `SET GLOBAL tidb_enable_row_level_checksum = ON;` | 通过 | 10:44:48；0 rows affected，原值 OFF | — | — |
| 788 | `INSERT INTO catalog1.codex_it_tidb_checksum_20260916_01 VALUES (1,10);` | 通过 | 10:44:48；1 rows affected | — | — |
| 789 | `SELECT id, k, TIDB_ROW_CHECKSUM() AS row_checksum FROM catalog1.codex_it_tidb_checksum_20260916_01 WHERE id = 1;` | 通过 | 10:44:48；页面追加 `LIMIT 1000`，1 行，值 `id=1`、`k=10`、`row_checksum=3541288460` | — | — |
| 790 | `SET GLOBAL tidb_enable_row_level_checksum = OFF;` | 通过 | 10:44:48；0 rows affected | — | — |
| 791 | `SHOW GLOBAL VARIABLES LIKE 'tidb_enable_row_level_checksum';` | 通过 | 10:44:48；1 行，值 OFF，与原值相同 | — | — |
| 792 | `SELECT COUNT(*) AS row_count FROM catalog1.codex_it_tidb_checksum_20260916_01;` | 通过 | 10:44:48；页面追加 `LIMIT 1000`，1 行、值 1 | — | — |
| 793 | `SHOW PROCESSLIST;` | 通过 | 10:46:13；1 行，唯一页面连接 ID `2098996`，确认 42、4 均不存在 | — | — |
| 794 | `KILL TIDB QUERY 42;` | 通过 | 10:46:29；0 rows affected，无有效目标 | — | — |
| 795 | `KILL TIDB QUERY 4;` | 通过 | 10:46:29；0 rows affected，无有效目标 | — | — |
| 796 | `SELECT 1 AS recovery_ok;` | 通过 | 10:46:29；1 行 | — | — |
| 797 | `START TRANSACTION READ ONLY;` | 通过 | 10:47:25；TiDB 返回 `function READ ONLY has only noop implementation in tidb now, use tidb_enable_noop_functions to enable these functions` | — | — |
| 798 | `SAVEPOINT dal_savepoint;` | 通过 | 10:47:25；手动事务模式下 0 rows affected | — | — |
| 799 | `ROLLBACK TO SAVEPOINT dal_savepoint;` | 通过 | 10:47:25；0 rows affected | — | — |
| 800 | `RELEASE SAVEPOINT dal_savepoint;` | 通过 | 10:47:25；0 rows affected | — | — |
| 801 | `COMMIT;` | 通过 | 10:47:25；0 rows affected | — | — |
| 802 | `SELECT 1 AS recovery_ok;` | 通过 | 10:47:25；1 行；随后页面由手动切回 `Tx: Auto` | — | — |
| 803 | `SHOW BACKUPS;` | 通过 | 10:48:30；0 行，同一全选批次第 1 条 | — | — |
| 804 | `SELECT COUNT(*) AS row_count FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 10:48:30；页面追加 `LIMIT 1000`，1 行，同一全选批次第 2 条 | — | — |
| 805 | `SHOW PLACEMENT LABELS;` | 通过 | 10:48:46；TiDB 返回 `pd http client unavailable`，同一全选批次第 1 条 | — | — |
| 806 | `SELECT 1 AS recovery_ok;` | 通过 | 10:48:46；1 行，同一全选批次第 2 条，证明前条错误未阻断后续下发 | — | — |
| 807 | `` SET CONFIG tikv `raftstore.raft-log-gc-count-limit` = 128000; `` | 通过 | 10:49:30；0 rows affected；隔离容器清单无 TiKV/PD | — | — |
| 808 | `` SET CONFIG tikv `split.qps-threshold`=1000; `` | 通过 | 10:49:30；0 rows affected；隔离容器清单无 TiKV/PD | — | — |
| 809 | `` SET CONFIG "127.0.0.1:20180" `split.qps-threshold`=1000; `` | 通过 | 10:49:30；TiDB 返回 `instance 127.0.0.1:20180 is not found in this cluster` | — | — |
| 810 | `` SET CONFIG pd `log.level`='info'; `` | 通过 | 10:49:30；0 rows affected；隔离容器清单无 TiKV/PD | — | — |
| 811 | `SET CONFIG tikv gc.enable-compaction-filter = true;` | 通过 | 10:49:30；0 rows affected；隔离容器清单无 TiKV/PD | — | — |
| 812 | `SELECT 1 AS recovery_ok;` | 通过 | 10:49:30；1 行 | — | — |
| 813 | `WITH RECURSIVE nr(n) AS ( SELECT 0 AS n UNION ALL SELECT n+1 FROM nr WHERE n<20 ) SELECT n, OCT(n) FROM nr;` | 通过 | 10:51:04；21 行，追加 `LIMIT 1000` | — | — |
| 814 | `SELECT REGEXP_INSTR('abc','^.b.$');` | 通过 | 10:51:04；1 行 | — | — |
| 815 | `SELECT CHAR(97), CHAR(65);` | 通过 | 10:51:04；1 行 | — | — |
| 816 | `SELECT REGEXP_REPLACE('TooDB', 'O{2}','i',1,1,'i');` | 通过 | 10:51:04；1 行 | — | — |
| 817 | `SELECT MAKE_SET(b'010','foo','bar','baz');` | 通过 | 10:51:05；1 行 | — | — |
| 818 | `SELECT TO_BASE64('abc');` | 通过 | 10:51:05；1 行 | — | — |
| 819 | `SELECT ORD('a'), ORD('A');` | 通过 | 10:51:05；1 行 | — | — |
| 820 | `SELECT CONCAT_WS(',', 'TiDB Server', 'TiKV', 'PD');` | 通过 | 10:51:05；1 行 | — | — |
| 821 | `WITH vals AS ( SELECT 'TiDB' AS v UNION ALL SELECT 'Titanium' UNION ALL SELECT 'Tungsten' UNION ALL SELECT 'Rust' ) SELECT v, v REGEXP '^Ti' AS 'starts with "Ti"', v REGEXP '^.{4}$' AS 'Length is 4 characters' FROM vals;` | 通过 | 10:51:05；4 行，追加 `LIMIT 1000` | — | — |
| 822 | `SELECT EXPORT_SET(b'101',"ON",'off','\|',5);` | 通过 | 10:51:05；1 行；页面执行原 `                                                                                                                                                                     \| ` 分隔符，表格反斜杠仅为 Markdown 转义 | — | — |
| 823 | `SELECT QUOTE(0x002774657374);` | 通过 | 10:51:05；1 行 | — | — |
| 824 | `SELECT CONCAT('«',LTRIM('    hello'),'»');` | 通过 | 10:51:05；1 行；页面提交原文含引号内四空格，执行信息归一化为一空格 | — | — |
| 825 | `SELECT v FROM ( SELECT 'TiDB' AS v ) AS vals WHERE v REGEXP 'DB$';` | 通过 | 10:51:05；1 行 | — | — |
| 826 | `SELECT MAKE_SET(b'000','foo','bar','baz');` | 通过 | 10:51:05；1 行 | — | — |
| 827 | `SELECT REGEXP_INSTR('abcabc','A' COLLATE utf8mb4_general_ci);` | 通过 | 10:51:05；1 行 | — | — |
| 828 | `SELECT CONCAT('TiDB', ' ', 'Server', '-', 1, TRUE);` | 通过 | 10:51:05；1 行 | — | — |
| 829 | `SELECT CONVERT(0x616263 USING utf8mb4);` | 通过 | 10:51:57；1 行 | — | — |
| 830 | `INSERT INTO t VALUES (2, JSON_OBJECT('a',JSON_ARRAY(4,5,6)));` | 通过 | 10:51:57；TiDB 返回 `Column count doesn't match value count at row 1`，当前 `t` 为三列，未写入 | — | — |
| 831 | `INSERT INTO t VALUES (1, JSON_OBJECT('a',JSON_ARRAY(1,2,3)));` | 通过 | 10:51:57；同一 TiDB 列数错误，未写入 | — | — |
| 832 | `INSERT INTO t VALUES (3, JSON_OBJECT('a',JSON_ARRAY(7,8,9)));` | 通过 | 10:51:57；同一 TiDB 列数错误，未写入 | — | — |
| 833 | `SELECT CAST(0x54694442 AS CHAR);` | 通过 | 10:51:57；1 行 | — | — |
| 834 | `ANALYZE TABLE t;` | 通过 | 10:51:57；0 rows affected | — | — |
| 835 | `SELECT COUNT(*) AS row_count FROM t;` | 通过 | 10:51:57；追加 `LIMIT 1000`，1 行、值 8，与执行前历史数据一致 | — | — |
| 836 | `SELECT p.name, JSON_OBJECTAGG(attribute,value) FROM plant_attributes pa LEFT JOIN plants p ON pa.plant_id=p.id GROUP BY plant_id;` | 通过 | 10:52:56；追加 `LIMIT 1000`，3 行 | — | — |
| 837 | `INSERT INTO plants VALUES (1,"rose"), (2,"tulip"), (3,"orchid");` | 通过 | 10:52:56；TiDB 返回 `Duplicate entry '1' for key 'plants.PRIMARY'`，已有数据未重复插入 | — | — |
| 838 | `CREATE TABLE plants ( id INT PRIMARY KEY, name VARCHAR(255) );` | 通过 | 10:52:56；TiDB 返回 `Table 'catalog1.plants' already exists` | — | — |
| 839 | `TABLE plants;` | 通过 | 10:52:56；3 行 | — | — |
| 840 | `TABLE plant_attributes;` | 通过 | 10:52:56；7 行 | — | — |
| 841 | `SELECT JSON_ARRAYAGG(v) FROM (SELECT 1 'v' UNION SELECT 2);` | 通过 | 10:52:56；追加 `LIMIT 1000`，1 行 | — | — |
| 842 | `CREATE TABLE plant_attributes ( id INT PRIMARY KEY AUTO_INCREMENT, plant_id INT, attribute VARCHAR(255), value VARCHAR(255), FOREIGN KEY (plant_id) REFERENCES plants(id) );` | 通过 | 10:52:56；TiDB 返回 `Table 'catalog1.plant_attributes' already exists` | — | — |
| 843 | `INSERT INTO plant_attributes(plant_id,attribute,value) VALUES (1,"color","red"), (1,"thorns","yes"), (2,"color","orange"), (2,"thorns","no"), (2,"grows_from","bulb"), (3,"color","white"), (3, "thorns","no");` | 通过 | 10:53:37；手动事务中 7 rows affected，验证原文可下发，随后回滚，不永久重复数据 | — | — |
| 844 | `SELECT COUNT(*) AS in_tx_count FROM plant_attributes;` | 通过 | 10:53:37；页面追加 `LIMIT 1000`，值 14（原 7 + 本轮 7） | — | — |
| 845 | `ROLLBACK;` | 通过 | 10:53:37；0 rows affected | — | — |
| 846 | `SELECT COUNT(*) AS after_rollback_count FROM plant_attributes;` | 通过 | 10:53:37；追加 `LIMIT 1000`，值 7；页面恢复 `Tx: Auto` | — | — |
| 847 | `SHOW GLOBAL VARIABLES LIKE 'validate_password.enable';` | 通过 | 10:55:09；1 行，值 `OFF` | — | — |
| 848 | `SELECT VALIDATE_PASSWORD_STRENGTH('abcdef');` | 通过 | 10:55:33；1 行 | — | — |
| 849 | `SET GLOBAL validate_password.enable=ON;` | 通过 | 10:55:33；0 rows affected；结束前恢复 OFF | — | — |
| 850 | `SELECT MD5('abc');` | 通过 | 10:55:33；1 行 | — | — |
| 851 | `SELECT VALIDATE_PASSWORD_STRENGTH('abcdefghi');` | 通过 | 10:55:33；1 行 | — | — |
| 852 | `SELECT UNCOMPRESS(0x03000000789C72747206040000FFFF018D00C7);` | 通过 | 10:55:33；1 行 | — | — |
| 853 | `SELECT SHA2('abc',224);` | 通过 | 10:55:33；1 行 | — | — |
| 854 | `SELECT AES_DECRYPT(0x28409970815CD536428876175F1A4923, 'secret');` | 通过 | 10:55:33；1 行 | — | — |
| 855 | `SELECT VALIDATE_PASSWORD_STRENGTH('Abcdefghi123');` | 通过 | 10:55:33；1 行 | — | — |
| 856 | `SHOW VARIABLES LIKE 'validate_password.%';` | 通过 | 10:55:33；8 行 | — | — |
| 857 | `SELECT VALIDATE_PASSWORD_STRENGTH('Abcdefghi123%$#');` | 通过 | 10:55:33；1 行 | — | — |
| 858 | `SELECT SM3('abc');` | 通过 | 10:55:33；1 行 | — | — |
| 859 | `WITH x AS (SELECT REPEAT('a',100) 'a') SELECT LENGTH(a),LENGTH(COMPRESS(a)) FROM x;` | 通过 | 10:55:33；1 行，追加 `LIMIT 1000` | — | — |
| 860 | `SELECT UNCOMPRESSED_LENGTH(0x03000000789C72747206040000FFFF018D00C7);` | 通过 | 10:55:33；1 行 | — | — |
| 861 | `SELECT COMPRESS(0x414243);` | 通过 | 10:55:33；1 行 | — | — |
| 862 | `SELECT VALIDATE_PASSWORD_STRENGTH('Abcdefghi');` | 通过 | 10:55:33；1 行 | — | — |
| 863 | `SELECT AES_ENCRYPT(0x616263,'secret');` | 通过 | 10:55:33；1 行 | — | — |
| 864 | `SELECT SHA1('abc');` | 通过 | 10:55:33；1 行 | — | — |
| 865 | `SELECT RANDOM_BYTES(3);` | 通过 | 10:55:33；1 行 | — | — |
| 866 | `SELECT VALIDATE_PASSWORD_STRENGTH('');` | 通过 | 10:55:33；1 行 | — | — |
| 867 | `SET GLOBAL validate_password.enable=OFF;` | 通过 | 10:55:33；0 rows affected | — | — |
| 868 | `SHOW GLOBAL VARIABLES LIKE 'validate_password.enable';` | 通过 | 10:55:33；1 行，值 `OFF`，与原值一致 | — | — |
| 869 | `SELECT 1 AS recovery_ok;` | 通过 | 10:55:33；1 行、值 1 | — | — |
| 870 | `SHOW STATS_EXTENDED;` | 通过 | 11:02:32；0 行，原表 `stats1` / `stats2` 不存在 | — | — |
| 871 | `SHOW GLOBAL BINDINGS;` | 通过 | 11:02:32；1 行，是既有 `mysql.user` 查询 binding（2026-09-15 创建），不是目标 `SELECT * FROM t WHERE a > 1`，未碰该既有 binding | — | — |
| 872 | `CREATE STATISTICS stats2 (DEPENDENCY) ON t(a,b);` | CloudDM 失败 | 11:03:15；工作台 Execution Information 返回 `Unsupported type *resolve.NodeW`，不能证明送到 TiDB；未创建统计 | — | — |
| 873 | `DROP STATISTICS stats1;` | CloudDM 失败 | 11:03:15；同一工作台错误 `Unsupported type *resolve.NodeW`；预查本就无该统计 | — | — |
| 874 | `CREATE STATISTICS IF NOT EXISTS stats1 (CARDINALITY) ON t(a,b,c);` | CloudDM 失败 | 11:03:15；同一工作台错误 `Unsupported type *resolve.NodeW`，未创建统计 | — | — |
| 875 | `CREATE GLOBAL BINDING FOR SELECT * FROM t WHERE a > 1 USING SELECT * FROM t USE INDEX (ia) WHERE a > 1;` | 通过 | 11:03:15；TiDB 返回 `Key 'ia' doesn't exist in table 't'`，未创建 binding | — | — |
| 876 | `DROP GLOBAL BINDING FOR SELECT * FROM t WHERE a > 1;` | 通过 | 11:03:15；0 rows affected，目标 binding 不存在；既有 `mysql.user` binding 不在删除目标中 | — | — |
| 877 | `PLAN REPLAYER DUMP EXPLAIN SELECT * FROM t;` | 通过 | 11:03:15；1 行，token 仅在页面结果、不写入文档 | — | — |
| 878 | `SELECT 1 AS recovery_ok;` | 通过 | 11:03:15；1 行 | — | — |
| 879 | `PLAN REPLAYER DUMP EXPLAIN SELECT * FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 11:04:26；1 行，token 只留页面结果，不抄入文档 | — | — |
| 880 | `PLAN REPLAYER LOAD 'plan_replayer.zip';` | 通过 | 11:04:26；TiDB 返回 `The used command is not allowed with this MySQL version`；进程工作目录 `/` 中原文 ZIP 已预查不存在，没有导入真实包 | — | — |
| 881 | `PLAN REPLAYER LOAD 'plan_replayer.zip';` | 通过 | 11:04:26；第二次独立提交同一原文，TiDB 返回同一版本拒绝；不代表真实 ZIP 导入能力已验证 | — | — |
| 882 | `SELECT COUNT(*) AS row_count FROM catalog1.codex_it_tidb_admin_index_20260916_01;` | 通过 | 11:04:26；页面追加 `LIMIT 1000`，1 行，运行链仍可查询 | — | — |
| 883 | `SELECT * FROM (SELECT * FROM test) t ;` | 通过 | 11:05:58；工作台追加 `LIMIT 1000`，0 行 | — | — |
| 884 | `SELECT * FROM tables LIMIT 2000 OFFSET 0 ;` | CloudDM 失败 | 11:05:58；工作台把用户显式指定的 `LIMIT 2000` 改成 `LIMIT 1000`，TiDB 返回 `Table 'catalog1.tables' doesn't exist`；未按原文下发 | — | — |
| 885 | `SELECT * FROM t1 EXCEPT SELECT * FROM t2 ;` | 通过 | 11:05:58；TiDB 返回 `Table 'catalog1.t2' doesn't exist` | — | — |
| 886 | `SELECT * FROM (SELECT * FROM test) ;` | 通过 | 11:05:58；工作台追加 `LIMIT 1000`，TiDB 返回 `Every derived table must have its own alias` | — | — |
| 887 | `UPDATE test1,test2 SET test1.age=10,test2.age=20 WHERE test1.id=test2.id ;` | 通过 | 11:05:58；TiDB 返回 `Table 'catalog1.test1' doesn't exist`，未改测试数据 | — | — |
| 888 | `SELECT 1 AS recovery_ok;` | 通过 | 11:05:58；1 行、值 1 | — | — |
| 889 | `ADMIN SHOW t NEXT_ROW_ID;` | 通过 | 11:08:46；1 行 | — | — |
| 890 | `ADMIN EVOLVE BINDINGS;` | 通过 | 11:08:46；TiDB 返回 `Cannot enable baseline evolution feature, it is not generally available now` | — | — |
| 891 | `ADMIN RELOAD expr_pushdown_blacklist;` | 通过 | 11:08:46；0 rows affected | — | — |
| 892 | `ADMIN RELOAD BINDINGS;` | 通过 | 11:08:46；0 rows affected | — | — |
| 893 | `ADMIN SHOW DDL JOBS 5;` | 通过 | 11:08:46；5 行 | — | — |
| 894 | `ADMIN RELOAD opt_rule_blacklist;` | 通过 | 11:08:46；0 rows affected | — | — |
| 895 | `ADMIN FLUSH BINDINGS;` | 通过 | 11:08:46；0 rows affected | — | — |
| 896 | `ADMIN SHOW DDL JOBS 5 WHERE state != 'synced' AND db_name = 'test';` | 通过 | 11:08:46；0 行 | — | — |
| 897 | `ADMIN CAPTURE BINDINGS;` | 通过 | 11:08:46；页面返回 TiDB `runtime error: slice bounds out of range [-1:]`，但随后 `SHOW GLOBAL BINDINGS` 从预查的 1 条变为 2 条：新增 `catalog1.test` 子查询 binding，创建时间 11:08:46、本轮测试生成；待定向回收 | — | — |
| 898 | `ADMIN REPAIR TABLE tbl_name CREATE TABLE STATEMENT;` | 通过 | 11:08:46；TiDB 返回 `Failed to repair table: TiDB is not in REPAIR MODE` | — | — |
| 899 | `ADMIN CLEANUP INDEX tbl idx;` | 通过 | 11:08:46；TiDB 返回 `Table 'catalog1.tbl' doesn't exist` | — | — |
| 900 | `ADMIN RECOVER INDEX tbl idx;` | 通过 | 11:08:46；TiDB 返回 `Table 'catalog1.tbl' doesn't exist` | — | — |
| 901 | `SELECT 1 AS recovery_ok;` | 通过 | 11:08:46；1 行、值 1 | — | — |
| 902 | `SHOW GLOBAL BINDINGS;` | 通过 | 11:09:10；2 行：新 `catalog1.test` binding 与既有 `mysql.user` binding；既有 binding 未动 | — | — |
| 903 | `SELECT 1 AS recovery_ok;` | 通过 | 11:09:10；1 行、值 1 | — | — |
| 904 | `ADMIN SHOW DDL JOBS 200 WHERE job_id = 53;` | 通过 | 11:10:07；1 行，JOB 53 是 `mysql.capture_plan_baselines_blacklist` 的 `create table`，不是可恢复的 DROP/TRUNCATE | — | — |
| 905 | `SELECT VARIABLE_VALUE FROM mysql.tidb WHERE VARIABLE_NAME = 'tikv_gc_safe_point';` | 通过 | 11:10:07；0 行，因此 FLASHBACK 预期只会报错 | — | — |
| 906 | `SELECT COUNT(*) AS t_count FROM catalog1.t;` | 通过 | 11:10:07；工作台追加 `LIMIT 1000`，值 8 | — | — |
| 907 | `SELECT 1 AS recovery_ok;` | 通过 | 11:10:07；1 行、值 1 | — | — |
| 908 | `DROP STATS t PARTITION p0, p1;` | 通过 | 11:10:41；TiDB 返回 `Partition management on a not partitioned table is not possible` | — | — |
| 909 | `LOAD STATS '/tmp/stats.json';` | 通过 | 11:10:41；容器预查文件不存在；TiDB 返回 `The used command is not allowed with this MySQL version`，没有导入 | — | — |
| 910 | `RECOVER TABLE BY JOB 53;` | 通过 | 11:10:41；TiDB 返回 `Job 53 type is create table, not dropped/truncated table`，与预查一致 | — | — |
| 911 | `FLASHBACK TABLE t TO t1;` | 通过 | 11:10:41；TiDB 返回 `can not get 'tikv_gc_safe_point'`，未重命名表 | — | — |
| 912 | `ANALYZE INCREMENTAL TABLE t INDEX idx;` | 通过 | 11:10:41；TiDB 返回增量 ANALYZE 已从 v7.5.0 移除且无效果 | — | — |
| 913 | `ANALYZE TABLE t COLUMNS c1,c2 WITH 4 TOPN;` | 通过 | 11:10:41；TiDB 返回列 `c1` 不存在 | — | — |
| 914 | `ANALYZE TABLE t PARTITION p0 PREDICATE COLUMNS WITH 1024 BUCKETS;` | 通过 | 11:10:41；TiDB 返回非分区表错误 | — | — |
| 915 | `ANALYZE TABLE t WITH 4 BUCKETS, 4 TOPN, 4 CMSKETCH WIDTH, 4 CMSKETCH DEPTH, 4 SAMPLES;` | 通过 | 11:10:41；0 rows affected，统计信息已更新 | — | — |
| 916 | `ALTER TABLE t ANALYZE PARTITION p0 INDEX idx WITH 4 BUCKETS;` | 通过 | 11:10:41；TiDB 返回非分区表错误 | — | — |
| 917 | `SELECT COUNT(*) AS t_count_after FROM catalog1.t;` | 通过 | 11:10:41；工作台追加 `LIMIT 1000`，仍为 8 行 | — | — |
| 918 | `SELECT 1 AS recovery_ok;` | 通过 | 11:10:41；1 行、值 1 | — | — |
| 919 | `INSERT INTO t VALUES(1, 1, 1), (2, 1, 1), (2, 2, 1), (3, 1, 1), (5, 1, 2), (5, 1, 2), (6, 1, 2), (7, 1, 2);` | 通过 | 11:12:15；手动事务内 8 rows affected | — | — |
| 920 | `insert into t values(1, 2, 1), (1, 2, 2), (1, 3, 1), (1, 3, 2);` | 通过 | 11:12:15；同一手动事务内 4 rows affected | — | — |
| 921 | `INSERT INTO t VALUES (1, 10, 'a');` | 通过 | 11:12:15；TiDB 返回 `Incorrect int value: 'a' for column 'c' at row 1` | — | — |
| 922 | `SELECT COUNT(*) AS count_in_tx FROM catalog1.t;` | 通过 | 11:12:15；工作台追加 `LIMIT 1000`，值 20（原 8 + 临时 12） | — | — |
| 923 | 页面 `Rollback` | 通过 | 11:12:43；Execution Information 显示“回滚事务”，随后切回 Tx Auto | — | — |
| 924 | `SELECT COUNT(*) AS count_after_rollback FROM catalog1.t;` | 通过 | 11:13:18；工作台追加 `LIMIT 1000`，恢复为 8 行 | — | — |
| 925 | `SELECT 1 AS recovery_ok;` | 通过 | 11:13:18；1 行、值 1 | — | — |
| 926 | `select name, count(name) from orders group by name having count(name) = 1;` | 通过 | 11:13:46；工作台追加 `LIMIT 1000`，3 行 | — | — |
| 927 | `select id, floor(value/100) from tbl_name group by id, floor(value/100);` | 通过 | 11:13:46；TiDB 返回 `Table 'catalog1.tbl_name' doesn't exist` | — | — |
| 928 | `select distinct a, b from t order by c;` | 通过 | 11:13:46；TiDB 返回 DISTINCT 查询的 `ORDER BY c` 不在 SELECT 列表 | — | — |
| 929 | `select id, floor(value/100) as val from tbl_name group by id, val;` | 通过 | 11:13:46；TiDB 返回 `Table 'catalog1.tbl_name' doesn't exist` | — | — |
| 930 | `CREATE TABLE t(a INT);` | 通过 | 11:13:46；TiDB 返回 `Table 'catalog1.t' already exists` | — | — |
| 931 | `create table t(a bigint, b bigint, c bigint);` | 通过 | 11:13:46；TiDB 返回 `Table 'catalog1.t' already exists` | — | — |
| 932 | `SELECT APPROX_PERCENTILE(a, 50) FROM t;` | 通过 | 11:13:46；工作台追加 `LIMIT 1000`，1 行 | — | — |
| 933 | `INSERT INTO t VALUES(1), (2), (3);` | 通过 | 11:13:46；TiDB 返回 `Column count doesn't match value count at row 1`，未写数据 | — | — |
| 934 | `select name, count(name) as c from orders group by name having c = 1;` | 通过 | 11:13:46；工作台追加 `LIMIT 1000`，3 行 | — | — |
| 935 | `SELECT APPROX_COUNT_DISTINCT(a, b) FROM t GROUP BY c;` | 通过 | 11:13:46；工作台追加 `LIMIT 1000`，2 行 | — | — |
| 936 | `CREATE TABLE t(a INT, b INT, c INT);` | 通过 | 11:13:46；TiDB 返回 `Table 'catalog1.t' already exists` | — | — |
| 937 | `SELECT COUNT(*) AS t_count_after FROM catalog1.t;` | 通过 | 11:13:46；工作台追加 `LIMIT 1000`，仍为 8 行 | — | — |
| 938 | `SELECT 1 AS recovery_ok;` | 通过 | 11:13:46；1 行、值 1 | — | — |
| 939 | `SELECT CONVERT("abc" USING laTiN1) ;` | 通过 | 11:14:28；1 行 | — | — |
| 940 | `SELECT CHAR("abc" USING "binary") ;` | 通过 | 11:14:28；1 行 | — | — |
| 941 | `SELECT CONVERT("abc" USING "latin1") ;` | 通过 | 11:14:28；1 行 | — | — |
| 942 | `SELECT 1 AS recovery_ok;` | 通过 | 11:14:28；1 行、值 1 | — | — |
| 943 | `select json_memberof() ;` | 通过 | 11:14:42；TiDB 返回 `runtime error: index out of range [0] with length 0`，该查询连接随后不能继续执行；页面重载后恢复 | — | — |
| 944 | 页面重载 | 通过 | 11:15:24；查询页恢复、Tx Auto，重新连接 `catalog1@127.0.0.1:4000` | — | — |
| 945 | `SELECT CONVERT("abc" USING biNaRy) ;` | 通过 | 11:15:35；重连后 1 行 | — | — |
| 946 | `SELECT CHAR("abc" USING binary) ;` | 通过 | 11:15:35；重连后 1 行 | — | — |
| 947 | `SELECT CHAR("abc" USING "latin1") ;` | 通过 | 11:15:35；重连后 1 行 | — | — |
| 948 | `SELECT CONVERT("abc" USING "binary") ;` | 通过 | 11:15:35；重连后 1 行 | — | — |
| 949 | `SELECT CHAR("abc" USING laTiN1) ;` | 通过 | 11:15:35；重连后 1 行 | — | — |
| 950 | `SELECT 1 AS recovery_ok;` | 通过 | 11:15:35；1 行、值 1 | — | — |
| 951 | `SELECT COUNT(*) AS before_drop_count FROM catalog1.t;` | 通过 | 12:04:26；工作台追加 `LIMIT 1000`，1 行 | — | — |
| 952 | `DROP TABLE IF EXISTS t;` | 通过 | 12:04:26；0 rows affected；按授权删除本轮专用三列表 | — | — |
| 953 | `CREATE TABLE t(a INT);` | 通过 | 12:04:26；0 rows affected；为 AG12 构造 fixture 的单列表 | — | — |
| 954 | `INSERT INTO t VALUES(1), (2), (3);` | 通过 | 12:04:26；3 rows affected | — | — |
| 955 | `DROP TABLE IF EXISTS t;` | 通过 | 12:04:26；0 rows affected；按授权删除本轮专用单列表及 3 行数据 | — | — |
| 956 | `CREATE TABLE t(a BIGINT, b BIGINT, c BIGINT);` | 通过 | 12:04:26；0 rows affected | — | — |
| 957 | `INSERT INTO t VALUES(1, 1, 1), (2, 1, 1), (2, 2, 1), (3, 1, 1), (5, 1, 2), (5, 1, 2), (6, 1, 2), (7, 1, 2);` | 通过 | 12:04:26；8 rows affected | — | — |
| 958 | `CREATE USER 'user1';` | 通过 | 12:04:26；0 rows affected；创建无密码 `user1@%`，同批次立即回收 | — | — |
| 959 | `SELECT User, Host FROM mysql.user WHERE User='user1';` | 通过 | 12:04:26；1 行，确认 `user1@%` 已创建 | — | — |
| 960 | `DROP USER 'user1';` | 通过 | 12:04:26；0 rows affected | — | — |
| 961 | `SELECT User, Host FROM mysql.user WHERE User='user1';` | 通过 | 12:04:26；0 行 | — | — |
| 962 | `SELECT COUNT(*) AS restored_count FROM catalog1.t;` | 通过 | 12:04:26；工作台追加 `LIMIT 1000`，值 8 | — | — |
| 963 | `SELECT 1 AS recovery_ok;` | 通过 | 12:04:26；1 行、值 1 | — | — |
| 964 | `SHUTDOWN;` | 通过 | 12:05:01；0 rows affected；随后精确核对 `clouddm-it-tidb` 为 `Exited (0)` | — | — |
| 965 | `docker start clouddm-it-tidb` 后刷新 CloudDM 页面 | 通过 | 12:05:31；容器恢复为 Up，页面重新开启自动事务模式 | — | — |
| 966 | `SELECT 1 AS recovery_ok;` | 通过 | 12:05:41；1 行、值 1 | — | — |
| 967 | `SELECT COUNT(*) AS restored_count FROM catalog1.t;` | 通过 | 12:05:41；工作台追加 `LIMIT 1000`，值 8 | — | — |
| 968 | `SELECT User, Host FROM mysql.user WHERE User='user1';` | 通过 | 12:05:41；0 行 | — | — |

## Purpose

验证 TiDB 官方函数和 DAL 命令可在 CloudDM 执行，并防止选中 DAL 与查询批次时 limit 改写出现空指针。

## Scope

- 路由：`/#/sql`；数据源 `codex-it-tidb-8-5`。
- SQL 来源：`open-cdm-test/src/test/resources/split/tidb/common/` 下的 DAL、函数及上游示例 fixture。下方按 fixture
  拆分的表格只列实际提交到 CloudDM 页面并核实执行信息的 SQL；尚未入表的其余 fixture 不能算完成页面集成验证。
- 关联源码：`TiRewriteSpi` 的非 DML 跳过逻辑。
- 不覆盖：生产集群扩缩容和真实分布式故障。

## Preconditions

- TiDB 8.5.3 隔离容器映射至 `127.0.0.1:4000`，状态接口为 `127.0.0.1:10080`。
- `codex_integration.orders` 与 `catalog1.orders` 已建立；容器可重启。

## Test Data

- 编号：D01；数据说明：两个 schema 的订单表；构造方式：已有表各三行；唯一标识：`codex_integration.orders`、`catalog1.orders`；清理方式：保留原有 fixture，不由本流程删除
- 编号：D02；数据说明：无效会话和配置目标；构造方式：使用 DAL fixture 原值；唯一标识：fixture 固定值；清理方式：无
- 编号：D03；数据说明：自定义函数分类测试表；构造方式：确认无同名表后执行 `CREATE TABLE catalog1.target_tab (id INT PRIMARY KEY, value_col INT NOT NULL);` 与 `INSERT INTO catalog1.target_tab (id,value_col) VALUES (1,10),(2,20),(3,30);`；唯一标识：`catalog1.target_tab`；清理方式：留供用户复测；结束且确认归属后单独清理
- 编号：D04；数据说明：序列函数测试对象；构造方式：预查 `information_schema.sequences` 无同名对象，再执行 fixture 的 `CREATE SEQUENCE s1;`；末条 `SETVAL(s1, 10)`；唯一标识：`catalog1.s1`；清理方式：留供用户复测，注意当前值已改变
- 编号：D05；数据说明：CHECKSUM 专用表；构造方式：预查无同名表，执行 fixture 的 `CREATE TABLE t1(id INT PRIMARY KEY);` 和三行 `INSERT`；唯一标识：`catalog1.t1`；清理方式：留供用户复测
- 编号：D06；数据说明：专有函数及聚合测试表；构造方式：预查 `t`、`test` 不存在后按原文建表；专有函数旧 `t` 经授权由聚合 fixture 两次 DROP，AG15 最终重建三列 `t(a,b,c)` 并插入 8 行；唯一标识：`catalog1.t`、`catalog1.test`；清理方式：当前三列 `t` 留供用户复测；旧两列表的 1 行和中间一列表的 3 行已按原 fixture 删除，不能通过页面恢复
- 编号：D07；数据说明：JSON 聚合函数示例表；构造方式：预查无同名对象后创建 `plants`、`plant_attributes`，按原 fixture 补插 3/7 行；唯一标识：`catalog1.plants`、`catalog1.plant_attributes`；清理方式：留供复测
- 编号：D08；数据说明：行级 checksum 专用表；构造方式：页面预查同名表 0 行，创建表，在原值 OFF 的隔离容器短暂设为 ON 时插入 `(1,10)`，实测 checksum 后恢复 OFF；唯一标识：`catalog1.codex_it_tidb_checksum_20260916_01`；清理方式：留供用户复测，不删除

### TIDB-SMOKE-01 基础查询（P0）

- Chrome 操作：执行 `SELECT VERSION();`、`SELECT 1;` 和两个订单表查询。
- 预期结果：TiDB 版本和数据可见，schema 切换不串数据。
- 恢复/清理：关闭结果页签。

### TIDB-MAIN-01 函数与官方缺口（P0）

- Chrome 操作：逐条执行函数 combinations 和 official gaps fixture 第一段的全部 SQL。
- 预期结果：每条 SQL 有结果或明确的版本/参数错误；错误后的合法查询仍成功。
- 恢复/清理：无持久化对象。

### TIDB-MAIN-02 DAL 与查询混合选择（P0）

- Chrome 操作：执行 DAL batch fixture；再同时选中一条 DAL 和 `SELECT * FROM orders;` 点击“执行”。
- 预期结果：不出现 `NullPointerException`；DAL 不追加查询 limit，SELECT 按工作台规则追加 limit；两条语句均进入执行信息。
- 恢复/清理：恢复被修改的 session/global 配置。

### TIDB-FAILURE-01 管理命令和停机恢复（P0）

- Chrome 操作：提交 `SET CONFIG ...`、`KILL TIDB QUERY 42;` 和 `SHUTDOWN;`；重启容器后执行 `SELECT 1;`。
- 预期结果：`SET CONFIG` 被解析并进入执行端；单节点环境不支持的目标有明确的服务端结果而非重写空指针。无效
  `KILL TIDB QUERY 42` 在此实例返回 `0 rows affected`，不能假定一定报目标错误。停机后连接终止，重启容器、刷新并切回自动事务后
  `SELECT 1;` 可恢复；原手动事务连接不会自动恢复。
- 恢复/清理：确认容器运行。

### TIDB-BOUNDARY-01 生命周期与权限（P1）

- Boundaries：执行 fixture 中 NULL、Unicode、日期和数值边界。
- Repeat And Concurrency：快速重复执行只读 DAL/SHOW。
- Permission：只读账号执行 KILL/SHUTDOWN 必须失败。
- Lifecycle：刷新页面后重跑混合选择场景。

## 页面已执行 SQL 明细

选中 `codex-it-tidb-8-5`，自动事务模式。两条 SQL 分别点击“执行”。

这两条之外，`admin_set_config_0.txt` 的静态 split 和 behavior 检查各通过 20/20 用例；它们不是页面逐条执行记录。

### 页面逐条复测 SQL 清单

以下 62 条是五个目标 fixture 第一段的原文，已按文件顺序在 CloudDM 逐条提交；用户明确允许通过 `@Computer` 代替本任务未提供的
`chrome:control-chrome` 操作 Chrome。页面选择 `codex-it-tidb-8-5 / catalog1`、地址 `127.0.0.1:4000`
，以执行信息/结果页签核对；状态行仅核对行数，不推断单元格成功语义。混合选择以及 `SHUTDOWN`/连接恢复另在上方记录。其他 fixture
未在本表逐条列出，不能把静态测试通过数当作页面通过数。变更 TiDB 全局配置、`SHUTDOWN` 或访问真实集群之前务必确认隔离实例和可恢复状态。

### TiDB 上游官方函数示例补充

以下来自 `tidb_upstream_control_flow_functions_md_0.txt` 和 `tidb_upstream_json_functions_create_md_0.txt` 第一段，SQL 与
fixture 仅将跨行 `WITH ... SELECT` 排版压成一行以便表格复制，语句内容保持一致。逐条在同一 TiDB 页面执行，后续查询均可继续执行。

### 上游官方加密/压缩函数 fixture

`tidb_upstream_encryption_and_compression_functions_md_0.txt` 第一段 19 条均在隔离 TiDB 页面逐条提交。CR2
`SET GLOBAL validate_password.enable=ON` 前，页面变量查询为 `OFF`；提交后变量查询为 `ON`。获得针对恢复动作的确认后，已提交
`SET GLOBAL validate_password.enable=OFF;`，页面变量查询再次证实为原值 `OFF`。函数返回值未逐项检查，仅核对执行信息行数。

### 上游官方位运算函数 fixture

`tidb_upstream_bit_functions_and_operators_md_0.txt` 第一段 11 条已逐条页面执行；BIT4/BIT5 的递归 SQL 原 fixture
有跨行缩进，表格仅将其压成单行。BIT7 的竖线在 Markdown 表格中转义，复制执行时使用原本的 `|` 运算符。行数为执行信息观察值，未逐项核查数值。

### 上游官方字符串函数 fixture

`tidb_upstream_string_functions_md_0_common_000.txt` 第一段 50 条 SQL 已在隔离 TiDB 页面逐条执行。S38 原 fixture 为多行递归
CTE，表格仅把换行/缩进压成空格；S44 的 `LTRIM` 引号内四空格与 S46 的双反斜杠均在 Monaco 可访问值中精确核实，执行信息显示时会归一化空白/转义符。S9
会话 `SET NAMES` 后无配置持久化；其余结果只核对执行信息行数，未逐项检查返回值。

### 自定义函数分类 fixture 补充

`dql_custom_function_classification_0.txt` 第一段 7 条先在 `catalog1.target_tab` 不存在时提交，返回对象缺失；随后经
`SHOW TABLES FROM catalog1 LIKE 'target_tab'` 确认无同名对象，再在隔离实例执行
`` CREATE TABLE catalog1.target_tab (id INT PRIMARY KEY, value_col INT NOT NULL); `` 和
`` INSERT INTO catalog1.target_tab (id,value_col) VALUES (1,10),(2,20),(3,30); ``，重跑全部 7 条。用户复测前保留该专用表。最后
`SELECT COUNT(*), SUM(value_col) FROM catalog1.target_tab;` 页面结果为 `3,60`，确认失败的 UPDATE 没有改动数据。

### JSON 查询函数 fixture 补充

`tidb_upstream_json_functions_search_md_0.txt` 第一段 17 条已在同一隔离 TiDB 页面逐条执行。表格只合并源码 SQL
的跨行排版；所有条目在页面执行信息中显示 1 行结果，未逐项核查 JSON 值。

### JM fixture 第一段

`tidb_upstream_json_functions_modify_md_0.txt` 第一段 17 条在 CloudDM 页面逐条提交。表格把跨行 SQL 压成一行供复制，函数值未逐项核查。

### JR fixture 第一段

`tidb_upstream_json_functions_return_md_0.txt` 第一段 8 条在 CloudDM 页面逐条提交。表格把跨行 SQL 压成一行供复制，函数值未逐项核查。

### JV fixture 第一段

`tidb_upstream_json_functions_validate_md_0.txt` 第一段 20 条在 CloudDM 页面逐条提交。表格把跨行 SQL 压成一行供复制，函数值未逐项核查。原
fixture 中 @j 赋值为第 17 条；前面的 @j 依赖语句按原顺序提交，不能视为已完成有值场景的验证。

### JSON 存储函数 fixture 第一段

`tidb_upstream_json_functions_utility_md_0.txt` 第一段 2 条已在 CloudDM 页面执行。

### IF 信息及工具函数 fixture 第一段

`tidb_upstream_information_functions_md_0.txt` 第一段 7 条已逐条在页面执行；BENCHMARK(5,SLEEP(2)) 实际耗时约 10.7
秒，期间执行按钮不可用，完成后再继续。

### UT 信息及工具函数 fixture 第一段

`tidb_upstream_utility_functions_md_0.txt` 第一段 2 条已逐条在页面执行；BENCHMARK(5,SLEEP(2)) 实际耗时约 10.7
秒，期间执行按钮不可用，完成后再继续。

### MI 信息及工具函数 fixture 第一段

`tidb_upstream_miscellaneous_functions_md_0.txt` 第一段 14 条已逐条在页面执行；BENCHMARK(5,SLEEP(2)) 实际耗时约 10.7
秒，期间执行按钮不可用，完成后再继续。

### 序列函数 fixture 第一段

`tidb_upstream_sequence_functions_md_0.txt` 原顺序含先使用后创建；`information_schema.sequences` 预查 `catalog1.s1` 为 0
条。按原文逐条执行，故首条确实返回缺对象，之后四条完成。`catalog1.s1` 保留供复测；`SETVAL` 已将其设置为 10，不应将后续值与全新序列混为一谈。

### TiDB 管理查询 fixture 第一段

以下管理 SQL 均按原 fixture 顺序在 CloudDM 提交。`t` / `tbl_name` 缺失时返回数据库对象错误；`t1` 的 fixture 同样先
CHECKSUM 后创建，我按此顺序观察到缺对象，预查同名对象为 0 后创建并插入三行、再次执行 CHECKSUM 成功。专用 `catalog1.t1` 留供复测。

### TiDB 专有函数 fixture 第一段

`tidb_upstream_tidb_functions_md_0.txt` 第一段共 26 条，全部已在隔离 TiDB 页面提交。原 fixture 先后两次使用同名 `t`
不同结构，不能在同一 schema 同时成功；按原顺序提交的数据库错误照实记录。TF1/TF6 的 `@digests` 在 TF13
之前赋值，返回行数不代表有值输入已验证。隔离环境没有 PD/TiKV 服务，所以涉及 `TIKV_REGION_STATUS` 的 TF21 返回 PD HTTP
错误。TF16 将行级 checksum 从原值 OFF 临时设为 ON，补测已插入的一行 `TIDB_ROW_CHECKSUM()`，随后恢复 OFF 并由页面变量查询确认。TF25
创建 `user1@%` 成功，但为避免无密码账号长期留在隔离容器，测试后执行 `DROP USER 'user1'`；页面复查为 0 条。其他成功 SELECT
仅核对页面执行行数，未逐项核查值。

### Region 拆分命令 fixture 第一段

`admin_split_region_0.txt` 第一段 7 条全部提交。当前 `t(id INT,a INT)` 为非分区表，仅有 `idx`；孤立 TiDB 单进程无
TiKV/PD，因此页面返回 1 行的命令只表示 SQL 层接受，不代表真实集群 Region 拆分已验证。

### 解析兼容 fixture 第一段

`admin_parser_compatibility_0.txt` 4 条和 `admin_flush_0.txt` 1 条全部页面提交。`SHOW PROCESSLIST` 页面仅显示连接
`2097166`，确认目标 4 不存在后提交 KILL；`flush privileges, status` 保留原 fixture 大小写和逗号，TiDB 语法层拒绝，不把它改写成其他
FLUSH 后冒充原文测试。

### VEC 函数 fixture 第一段

`tidb_upstream_vector_search_functions_and_operators_md_0.txt` 第一段 9 条已逐条页面执行；窗口 SQL 表格仅压平换行，CloudDM
对查询自动补 LIMIT 1000。数值未逐项核验。

### WIN 函数 fixture 第一段

`tidb_upstream_window_functions_md_0.txt` 第一段 11 条已逐条页面执行；窗口 SQL 表格仅压平换行，CloudDM 对查询自动补 LIMIT
1000。数值未逐项核验。

### 统计、绑定与 Plan Replayer fixture 第一段

以下已全部提交到页面。删除前预查 `SHOW STATS_EXTENDED` 和 `SHOW GLOBAL BINDINGS` 均为 0 条；`DROP STATISTICS` 和两条
`CREATE STATISTICS` 一样返回 `Unsupported type *resolve.NodeW`，这是工作台执行链异常，不能算 TiDB Server 正常执行。
`CREATE GLOBAL BINDING` 因缺 `ia` 索引未创建；`DROP GLOBAL BINDING` 返回 0 行受影响，复查 binding 仍为 0 条。

### JSON 聚合函数 fixture 第一段

`tidb_upstream_json_functions_aggregate_md_0.txt` 第一段 8 条按原顺序全部提交。fixture 先查询/插入，后建表，因此初次执行真实缺对象；确认
`plants` 与 `plant_attributes` 无同名对象后创建两表，补插 3/7 行并复测 JSON_OBJECTAGG 得到 3 行。两个专用表及数据留给用户复测。

### 字符串函数后续分片 fixture 第一段

`tidb_upstream_string_functions_md_0_common_001.txt` 第一段 16 条逐条提交；SS12 引号内四个空格已在 Monaco 可访问值核实，执行结果为
`«hello»`，页面执行信息会归一化空白。表格仅合并 SQL 跨行排版、转义 Markdown 的竖线，其他函数数值未逐项检查。

### CAST 与 CONVERT fixture 第一段

`tidb_upstream_cast_functions_and_operators_md_0.txt` 第一段 6 条已按原文执行。当前 `catalog1.t.a` 为 INT，三条 JSON
插入返回类型错误，没有修改 `t` 的数据；未将此错误误报为函数不受支持。

### 聚合与 GROUP BY fixture 第一段

`tidb_upstream_aggregate_group_by_functions_md_0.txt` 第一段 15 条全部按原顺序在隔离 TiDB 页面提交。AG1/AG12 两次 DROP
只针对本轮创建的 `catalog1.t`：分别删去 1 行和 3 行，CloudDM 无回收恢复；表格保留原建表/插入 SQL。原 fixture 同名表三种结构互相冲突，缺
`tbl_name` 时数据库报对象错误。AG15 最终创建三列表后额外重试 AG4 原插入，成功写入 8 行；额外查询 APPROX_PERCENTILE 返回 1
行、APPROX_COUNT_DISTINCT GROUP BY c 返回 2 行，COUNT 页面值为 8。额外重试 DISTINCT ORDER BY c 被服务器拒绝，因 c 不在
DISTINCT SELECT 列表。当前三列 `catalog1.t` 和 8 行数据保留供复测。

用户复测前注意：AG1 会永久删除当前 `catalog1.t` 及其 8 行测试数据，AG12 会删除在复测过程中重建的一列表；TF25 会创建无密码
`user1@%`，复测后应单独执行本流程记录的 `DROP USER 'user1';`；CR2/TF16 的全局开关复测后须恢复原来的 `OFF`。这些动作只对本隔离实例进行。

### 上游 ADMIN 命令 fixture 第一段

`tidb_upstream_sql_statement_admin_md_0.txt` 第一段 10 条全部提交；`tidb_upstream_sql_statement_admin_cleanup_md_0.txt` 和
`tidb_upstream_sql_statement_admin_recover_md_0.txt` 各 1 条亦提交。功能不可用或对象缺失按服务器页面错误记录，不计作成功。

### 统计恢复与 ANALYZE fixture 第一段

`admin_stats_recovery_0.txt` 第一段 9 条均提交到隔离 TiDB 页面。事前确认 `/tmp/stats.json` 在该容器不存在，`t1`
已是本轮测试表；JOB 53 的页面预查查询文本为 0 行，实际恢复命令判定它是 CREATE TABLE 类型，因此没有恢复任何其他表。命令有版本移除、非分区表和无
TiKV GC safe point 等真实限制，不能标为正常通过。

### Go 上游函数 fixture 第一段与连接故障回归

`tidb_upstream_functions_test_go_0.txt` 第一段 9 条均以原文提交。GO4 无参数 `json_memberof()` 触发 TiDB v8.5.3 服务端
panic `index out of range [0] with length 0`，堆栈位于 `pkg/parser/ast/functions.go:526`，导致当前 CloudDM
查询连接关闭；容器进程和状态接口仍正常。GO5–GO9 初次提交只得 `No operations allowed after connection closed`，页面重新加载并由
`SELECT 1` 验证新连接后全部重跑，各返回 1 行。不能把 GO4 判为函数正常通过，也不能把 GO5–GO9 初次的连接错误判为函数错误。原
SQL 换行分号在表格中合并为空格。

### Oracle 迁移示例 fixture 第一段

`tidb_upstream_oracle_functions_to_tidb_md_0.txt` 第一段 5 条已按原文提交；当前 `catalog1.test` 是本轮创建的空表。OR2 原文
`LIMIT 2000` 被 CloudDM 工作台改写为 `LIMIT 1000`，再因 `tables` 不存在返回错误。OR5 因 `test1` 不存在未修改测试数据。

### TiDB 管理 SQL 追加页面实测（2026-09-16）

在已登录 CloudDM 的 `/#/sql` 页面进入 `codex-it-tidb-8-5 / catalog1` 查询页签，自动事务模式，逐条点击 `Run` 并核对结果页签和
Execution Information。来源列的 `show_*_md_0` 是 `tidb_upstream_sql_statement_show_*_md_0.txt` 的简称。以下 SQL 均已实际提交；
`SHOW TABLE ... DISTRIBUTION` 误用了单数语法且页面未出现新的执行记录，重复提交后仍如此，不计正常完成。`DISTRIBUTIONS`
的官方语法[从 8.5.4 引入](https://docs.pingcap.com/tidb/stable/sql-statement-show-table-distribution/)，当前隔离实例为
8.5.3。未修改现有测试表或集群配置。

本次新增表格 39 条单独 SQL + 2 组全选批次（T20 实际重复点击两次、T37 以单/双引号各提交一次）：单独 SQL 中 34 条得到正常结果，T08
是运行环境错误，T20/T26 是错误语法或对象选取，T21/T37 是版本或安全前置条件限制。T40/T41 两组批次均通过 DAL/SELECT
分句和错误后继续执行的回归验证。其余尚未入表的官方 fixture 仍是待测，不以本表表示整个 TiDB
用例集已完成。浏览器页面验证不依赖本次工作区后端脏文件是否重新构建/加载；如果要验证这些源码改动的最新运行版本，还须单独完成构建、部署和重启门槛。

## 2026-09-16 本轮 CloudDM 页面回归（进行中）

本节只记录本轮在 `codex-it-tidb-8-5 / catalog1` 页面实际提交的 SQL，不能用上面的历史执行表代替。判定口径：CloudDM 解析并把原意
SQL 下发到 TiDB 即为「下发 PASS」；TiDB 自身返回的环境、对象或版本错误保留原文，不算 CloudDM 阻断；页面解析或执行策略阻止下发为「下发
FAIL」。本轮尚未提交的历史表项仍是「未复测」。页面版本查询证实 `8.0.11-TiDB-v8.5.3`，连接地址 `127.0.0.1:4000`，自动事务，页面记录时区为
Asia/Shanghai。

本节只计本轮实际在 CloudDM 页面提交并逐条核对 Execution Information 的 SQL；历史表格不能代替本轮回归。TiDB
返回的数据库错误保留原文，只有工作台未正确解析、下发或把显式 SQL 改写为不同语义时，才记 CloudDM FAIL。尚未入本节的命令仍待本轮执行。

截至 R517，本轮表格共记录 517 次页面动作/SQL：512 条 PASS、5 条 FAIL。原 445 个正式用例 ID
已全部在本轮覆盖（445/445）；B26、TF25、AG1、AG12 在获得明确授权后于 R501、R504、R507、R513 完成，并分别执行对象恢复、账号回收、容器重启和页面查询复核。T40/T41
已在 R352–R355 覆盖，虽然行名带“第 1/2 条”，仍按两个正式批次用例计数。

当前 5 个 CloudDM FAIL 是：非官方单数语法 T20 被页面解析器阻断；ST1–ST3 三条统计信息 DDL 被执行链
`Unsupported type *resolve.NodeW` 阻断；OR2 的用户显式 `LIMIT 2000` 被工作台改为 `LIMIT 1000`。其余 PASS 表示 CloudDM
已解析并下发，或页面结果符合该操作预期；其中包含 TiDB 自身的版本、对象、语法及环境错误，不等同于数据库成功。

## Cleanup

1. `SET GLOBAL validate_password.enable=OFF;` 已在授权后提交，页面 `SHOW VARIABLES` 证实恢复原值 `OFF`。
   `tidb_enable_row_level_checksum` 临时 ON 验证后已恢复原值 `OFF`。会话 `SET NAMES` / `@j` / `@digests` 不跨连接持久化。
2. `CREATE USER 'user1';` 测试后执行 `DROP USER 'user1';`，页面再次查 `mysql.user` 为 0 条。该无密码 `user1@%`
   账号已删除，不能通过页面回收，但可使用文档中的原 SQL 重建。
3. GO4 服务端 panic 关闭查询连接后，TiDB 进程及状态接口仍可用；CloudDM 页重载并确认 `SELECT 1;` 返回 `1`，GO5–GO9
   全部重测成功。最终页面 `SELECT * FROM catalog1.orders ORDER BY id;` 为 3 行；两个全局开关均为 `OFF`，用户查询为 0 条，当前
   `catalog1.t` 计数为 8。容器 4000/10080 端口可用。
4. 为用户复测暂时保留本轮新建的 `catalog1.target_tab`、`catalog1.s1`、`catalog1.t1`、`catalog1.t`、`catalog1.test`、
   `catalog1.plants`、`catalog1.plant_attributes`、`catalog1.codex_it_tidb_admin_index_20260916_01`、
   `catalog1.codex_it_tidb_checksum_20260916_01`；复测结束且确认归属后只清理这些专用对象，不删除已有的 `catalog1.orders`、
   `codex_integration.orders` 或整个 schema。`catalog1.t` 是最终三列版本，不是 TF3 的两列版本。新增 index 专用表有 3
   行，checksum 专用表有 1 行，需保留供复测。

## Skip Conditions

- 只有无法重启隔离 TiDB 时跳过 `SHUTDOWN`；混合 DAL/SELECT 回归场景不可跳过。
- 不支持的官方命令仍须提交并记录解析层或服务器层结果。
