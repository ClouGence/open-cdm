# PostgreSQL DAL 与函数集成验证

## SQL 执行明细

判定口径：CloudDM 能解析并将 SQL 发送到数据库即为通过；数据库返回错误不等于 CloudDM 失败。安全或破坏性操作保留待用户执行状态。

验证状态统一为：通过、CloudDM 失败、未执行、证据不足。通过仅表示已有证据确认 SQL 被解析并发送到数据库，数据库报错保留在执行证据中。历史执行与复测记录均保留，每行反映该次记录，不代表相同 SQL 的最新复测结论。

本表记录数：通过 98；CloudDM 失败 1；未执行 11；证据不足 0。包含重复执行记录，不等同于去重用例数。本次为文档证据整理，未重新执行 SQL。

| 编号 | SQL | 验证状态 | 实际结果/执行证据 | 前提/预期 | 来源/备注 |
| --- | --- | --- | --- | --- | --- |
| 1 | `SELECT ARRAY[1,NULL,234567]::int[];` | 通过 | 返回包含 `1`、`NULL`、`234567` 的 JSON 数组，无 `ReadException`。 | — | 页面事务模式：自动 |
| 2 | `SELECT ARRAY[repeat('x',5000),NULL]::text[];` | 通过 | 返回可见的序列化数组，无 `ReadException`；浏览器辅助功能视图截短长单元格，未逐字符核对全部 5000 个字符。 | — | 页面事务模式：自动 |
| 3 | `SET work_mem='8MB';` | 通过 | 执行成功；保持当前查询页签。 | — | 页面事务模式：自动 |
| 4 | `SHOW work_mem;` | 通过 | 返回 `8MB`，证明跨点击保留 session 参数。 | — | 页面事务模式：自动 |
| 5 | `PREPARE codex_regression_stmt AS SELECT 42;` | 通过 | 执行成功；保持当前查询页签。 | — | 页面事务模式：自动 |
| 6 | `EXECUTE codex_regression_stmt;` | 通过 | 返回 `42`。 | — | 页面事务模式：自动 |
| 7 | `DEALLOCATE codex_regression_stmt;` | 通过 | 执行成功，清理 prepared statement。 | — | 页面事务模式：自动 |
| 8 | `RESET work_mem;` | 通过 | 执行成功，恢复 session 参数。 | — | 页面事务模式：自动 |
| 9 | `BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 10 | `SHOW transaction_isolation;` | 通过 | 返回 `repeatable read`。 | — | 页面事务模式：手动 |
| 11 | `SHOW transaction_read_only;` | 通过 | 返回 `on`。 | — | 页面事务模式：手动 |
| 12 | `COMMIT;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 13 | `START TRANSACTION READ WRITE;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 14 | `SAVEPOINT codexsp;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 15 | `ROLLBACK TO SAVEPOINT codexsp;` | 通过 | 执行成功；用户保存点仍存在。 | — | 页面事务模式：手动 |
| 16 | `RELEASE SAVEPOINT codexsp;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 17 | `ROLLBACK;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 18 | `BEGIN;` | CloudDM 失败 | 页面拒绝并提示“请先切换到手动事务模式再执行事务命令。”，不是数据库成功执行。 | — | 页面事务模式：自动 |
| 19 | `SELECT 1;` | 通过 | 返回 `1`，确认拒绝后的查询可继续。 | — | 页面事务模式：自动 |
| 20 | `SELECT ABS(-1), ROUND(12.345, 2), WIDTH_BUCKET(5, 0, 10, 5);` | 通过 | 1 行：`1`、`12.35`、`3`。 | — | — |
| 21 | `SELECT LOWER('PostgreSQL'), REGEXP_REPLACE('abc123', '[0-9]+', ''), FORMAT('%s-%s', 'pg', 18);` | 通过 | 1 行：`postgresql`、`abc`、`pg-18`。 | — | — |
| 22 | `SELECT CURRENT_DATE, DATE_TRUNC('hour', CURRENT_TIMESTAMP), EXTRACT(EPOCH FROM TIMESTAMP '2026-01-01');` | 通过 | 1 行：`2026-09-15`、整点时间、`1767225600.000000`。 | — | — |
| 23 | `SELECT JSONB_BUILD_OBJECT('a', 1), JSONB_PATH_QUERY_ARRAY('{"a":[1,2]}'::jsonb, '$.a[*]');` | 通过 | 1 行：`{"a": 1}`、`[1, 2]`。 | — | — |
| 24 | `SELECT ARRAY_APPEND(ARRAY[1,2], 3), ARRAY_POSITION(ARRAY['a','b'], 'b');` | 通过 | 1 行；位置为 `2`，但数组单元格显示 `[{"m":"V","v":"1","t":"F"},...]` 形式的内部结构化数据，而不是 PostgreSQL 数组文本。 | — | — |
| 25 | `SELECT INET_CLIENT_ADDR(), PG_BACKEND_PID(), CURRENT_SETTING('work_mem');` | 通过 | 1 行：客户端地址 `192.168.65.1`、后端 PID、`4MB`。 | — | — |
| 26 | `SELECT MD5('postgres'), SHA256('postgres'::bytea), GEN_RANDOM_UUID();` | 通过 | 1 行：MD5、SHA256 十六进制值与 UUID 均返回。 | — | — |
| 27 | `SELECT COUNT(*), SUM(amount), AVG(amount), MIN(amount), MAX(amount) FROM public.orders;` | 通过 | 1 行：`3`、`61.00`、`20.3333333333333333`、`10.50`、`30.50`。 | — | — |
| 28 | `SELECT ROW_NUMBER() OVER (ORDER BY id), LAG(amount) OVER (ORDER BY id) FROM public.orders;` | 通过 | 3 行；序号 `1,2,3`，前值 `NULL,10.50,20.00`。 | — | — |
| 29 | `SELECT GENERATE_SERIES(1, 3), UNNEST(ARRAY['a','b']);` | 通过 | 3 行：`(1,a)`、`(2,b)`、`(3,NULL)`。 | — | — |
| 30 | `SELECT COALESCE(NULL, 1), NULLIF(1, 1), GREATEST(1, 2), LEAST(1, 2);` | 通过 | 1 行：`1`、`NULL`、`2`、`1`。 | — | — |
| 31 | `SELECT TO_TSVECTOR('english', 'database administration') @@ TO_TSQUERY('english', 'database');` | 通过 | 1 行：`True`。 | — | — |
| 32 | `SELECT TO_CHAR(TIMESTAMP '2026-01-02 03:04:05', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-01-02', 'YYYY-MM-DD');` | 通过 | 1 行：`2026-01-02 03:04:05`、`2026-01-02`。 | — | — |
| 33 | `SELECT LOWER(NUMRANGE(1, 5, '[)')), UPPER(NUMRANGE(1, 5, '[)')), NUMRANGE(1, 5) @> 3::numeric;` | 通过 | 1 行：`1`、`5`、`True`。 | — | — |
| 34 | `SELECT HOST('192.168.1.5/24'::inet), MASKLEN('192.168.1.5/24'::inet), FAMILY('192.168.1.5/24'::inet);` | 通过 | 1 行：`192.168.1.5`、`24`、`4`。 | — | — |
| 35 | `SELECT XMLELEMENT(NAME item, XMLATTRIBUTES(42 AS id), 'ok'), XPATH('/item/@id', '<item id="42">ok</item>'::xml);` | 通过 | 1 行：XML 元素正确，`XPATH` 数组显示为内部结构化 JSON 字符串。 | — | — |
| 36 | `SELECT JSONB_ARRAY_LENGTH('[1,2,3]'::jsonb), JSONB_OBJECT_KEYS('{"a":1,"b":2}'::jsonb);` | 通过 | 2 行：长度 `3`，键分别为 `a`、`b`。 | — | — |
| 37 | `SELECT STRING_AGG(name, ',' ORDER BY id), JSONB_AGG(status ORDER BY id), ARRAY_AGG(amount ORDER BY id) FROM public.orders;` | 通过 | 1 行：名称 `alpha,数据库,omega`，JSON 聚合正确；`ARRAY_AGG` 显示内部结构化 JSON 字符串。 | — | — |
| 38 | `SELECT AREA(BOX(POINT(0,0), POINT(2,3))), POINT(0,0) <-> POINT(3,4);` | 通过 | 1 行：面积 `6.0`，距离 `5.0`。 | — | — |
| 39 | `SELECT DENSE_RANK() OVER (ORDER BY amount), NTILE(2) OVER (ORDER BY id) FROM public.orders;` | 通过 | 3 行：rank `1,2,3`，tile `1,1,2`。 | — | — |
| 40 | `SELECT PG_GET_SERIAL_SEQUENCE('public.orders', 'id'), PG_TABLE_IS_VISIBLE('public.orders'::regclass);` | 通过 | 1 行：本表无 serial 序列，返回 `NULL`；表可见为 `True`。 | — | — |
| 41 | `SELECT STRING_TO_ARRAY('a,b,c', ','), ARRAY_TO_STRING(ARRAY['a','b','c'], ':');` | 通过 | 1 行：转回字符串为 `a:b:c`；数组单元格显示内部结构化 JSON 字符串。 | — | — |
| 42 | `ANALYZE public.orders;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 43 | `VACUUM (ANALYZE, VERBOSE) public.orders;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 44 | `REINDEX TABLE CONCURRENTLY public.orders;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 45 | `CLUSTER public.orders USING orders_pkey;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 46 | `CHECKPOINT;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 47 | `DISCARD PLANS;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 48 | `DISCARD TEMP;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 49 | `SET SESSION work_mem TO '64MB';` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 50 | `SHOW work_mem;` | 通过 | 1 行，`64MB`；跨点击 session 设置生效。 | — | 模式：自动 |
| 51 | `RESET work_mem;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 52 | `SET ROLE reporting_role;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 53 | `RESET ROLE;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 54 | `SET SESSION AUTHORIZATION reporting_user;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 55 | `RESET SESSION AUTHORIZATION;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 56 | `PREPARE dal_plan(bigint) AS SELECT * FROM public.orders WHERE id = $1;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 57 | `EXECUTE dal_plan(42);` | 通过 | 0 行结果；预编译对象跨点击可用。 | — | 模式：自动 |
| 58 | `DEALLOCATE dal_plan;` | 通过 | 0 rows affected。 | — | 模式：自动 |
| 59 | `BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 60 | `SAVEPOINT dal_savepoint;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 61 | `ROLLBACK TO SAVEPOINT dal_savepoint;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 62 | `RELEASE SAVEPOINT dal_savepoint;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 63 | `COMMIT;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 64 | `LOCK TABLE public.orders IN SHARE MODE;` | 通过 | 0 rows affected；手动模式的隐式事务上下文与既有自动模式拒绝场景不同。 | — | 模式：手动 |
| 65 | `LISTEN dal_channel;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 66 | `NOTIFY dal_channel, 'ready';` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 67 | `UNLISTEN dal_channel;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 68 | `REFRESH MATERIALIZED VIEW CONCURRENTLY public.order_summary;` | 通过 | 0 rows affected。 | — | 模式：手动 |
| 69 | `SHOW server_version;` | 通过 | 1 行：`16.15 (Debian 16.15-1.pgdg13+2)`。 | — | — |
| 70 | `SHOW ALL;` | 通过 | 页面接收 364 行配置参数及说明。 | — | — |
| 71 | `DISCARD SEQUENCES;` | 通过 | 执行信息：0 rows affected。 | — | — |
| 72 | `RESET ALL;` | 通过 | 执行信息：0 rows affected；恢复当前 session 设置。 | — | — |
| 73 | `SELECT version();` | 通过 | 1 行，`PostgreSQL 16.15 (Debian 16.15-1.pgdg13+2)` | PostgreSQL 16 实例 | — |
| 74 | `SELECT date_bin(INTERVAL '15 minutes', TIMESTAMP '2026-01-01 10:38:40', TIMESTAMP '2026-01-01 10:00:00') AS binned;` | 通过 | 1 行，`2026-01-01 10:30:00.000000000` | `10:30:00` | — |
| 75 | `SELECT array_to_string(array_remove(ARRAY[1,NULL,2,NULL]::int[], NULL), ',') AS no_nulls;` | 通过 | 1 行，`1,2` | `1,2` | — |
| 76 | `SELECT lower('{[1,2),[3,4)}'::int4multirange), upper('{[1,2),[3,4)}'::int4multirange), range_merge('{[1,2),[3,4)}'::int4multirange);` | 通过 | 1 行，三个值符合预期 | `1,4,[1,4)` | — |
| 77 | `SELECT pg_current_wal_lsn(), pg_current_wal_insert_lsn(), pg_current_wal_flush_lsn();` | 通过 | 1 行，三列均为 `0/1A31368` | 三列 WAL LSN | — |
| 78 | `SELECT jsonb_path_exists('{"a":[1,2]}'::jsonb, '$.a[*] ? (@ > 1)') AS has_two;` | 通过 | 1 行，`True` | true | — |
| 79 | `SELECT percentile_cont(0.5) WITHIN GROUP (ORDER BY amount) FROM public.orders;` | 通过 | 1 行，`20.0`；改写 SQL 追加 LIMIT 1000 | 三行金额中位数 `20` | — |
| 80 | `SELECT first_value(amount) OVER (ORDER BY id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM public.orders;` | 通过 | 3 行均为 `10.50`；改写追加 LIMIT 1000 | 3 行均为首值 10.50 | — |
| 81 | `SELECT bit_count(B'101100') AS set_bits;` | 通过 | 1 行，`3` | 3 | — |
| 82 | `SELECT pg_snapshot_xmin(pg_current_snapshot()) AS xmin;` | 通过 | 1 行，本次值 `764` | 1 行快照 ID | — |
| 83 | `DISCARD ALL;` | 通过 | Execution Information `0 rows affected` | 清空会话状态，不修改表 | — |
| 84 | `SHOW work_mem;` | 通过 | 首次 1 行；本轮页面重提并读取该列为 `4MB` | DISCARD 后默认 `4MB` | — |
| 85 | `EXPLAIN (ANALYZE, BUFFERS) SELECT * FROM public.orders WHERE id=1;` | 通过 | 修复后返回 8 行计划；Execution Information 显示 8 row retrieved | PostgreSQL 执行计划结果 | — |
| 86 | `EXPLAIN SELECT * FROM public.orders WHERE id=1;` | 通过 | 2 行计划；Execution Information 2 row retrieved | 普通执行计划 | — |
| 87 | `EXPLAIN (ANALYZE) SELECT * FROM public.orders WHERE id=1;` | 通过 | 修复后返回 5 行计划；Execution Information 显示 5 row retrieved | ANALYZE 实际执行计划 | — |
| 88 | `SELECT 1 AS recovery_ok;` | 通过 | 1 行，`1` | NPE 后查询仍可执行 | — |
| 89 | `BEGIN TRANSACTION READ ONLY;`（手动事务） | 通过 | 0 rows affected | 开只读事务 | — |
| 90 | `SET TRANSACTION ISOLATION LEVEL READ COMMITTED;` | 通过 | 0 rows affected | 设置当前事务 | — |
| 91 | `DECLARE codex_pg_cursor_20260916 NO SCROLL CURSOR FOR SELECT id,amount FROM public.orders ORDER BY id;` | 通过 | 修复后手动事务内 `0 rows affected` | 建专用只读游标 | — |
| 92 | `FETCH NEXT FROM codex_pg_cursor_20260916;` | 通过 | 返回 1 行：id=`1`、amount=`10.50` | 读取第一行 | — |
| 93 | `FETCH ALL FROM codex_pg_cursor_20260916;` | 通过 | 返回 2 行：id=`2/3`、amount=`20.00/30.50` | 读取剩余行 | — |
| 94 | `CLOSE codex_pg_cursor_20260916;` | 通过 | `0 rows affected` | 关闭游标 | — |
| 95 | `ABORT;`（手动事务） | 通过 | 0 rows affected | 结束只读事务 | — |
| 96 | `BEGIN TRANSACTION READ ONLY;`（再次） | 通过 | 0 rows affected | 新开只读事务 | — |
| 97 | `DECLARE codex_pg_cursor_simple_20260916 CURSOR FOR SELECT id FROM public.orders ORDER BY id;` | 通过 | 修复后 `0 rows affected`；随后执行同名 CLOSE 清理 | 简单形式应能建游标 | — |
| 98 | `END;`（手动事务） | 通过 | 0 rows affected | COMMIT 别名结束事务 | — |
| 99 | `SELECT 1 AS final_ok;` | 通过 | 1 行，`1`；页面已切回 Tx: Auto | 事务/错误后仍可查询 | — |
| 100 | `ANALYZE public.orders;` | 未执行 | 未执行；风险：持久修改优化器统计信息 | 备份统计信息/执行计划基线并准备重新 ANALYZE | 风险：持久修改优化器统计信息 |
| 101 | `VACUUM (ANALYZE, VERBOSE) public.orders;` | 未执行 | 未执行；风险：维护表并更新统计，可能占用 I/O | 独占维护窗口并设置资源监控 | 风险：维护表并更新统计，可能占用 I/O |
| 102 | `REINDEX TABLE CONCURRENTLY public.orders;` | 未执行 | 未执行；风险：重建索引、占用额外磁盘并持有锁 | 确认磁盘空间和无冲突维护任务 | 风险：重建索引、占用额外磁盘并持有锁 |
| 103 | `CLUSTER public.orders USING orders_pkey;` | 未执行 | 未执行；风险：重写表并持有强锁 | 备份表并安排独占维护窗口 | 风险：重写表并持有强锁 |
| 104 | `CHECKPOINT;` | 未执行 | 未执行；风险：强制全实例检查点，可能造成 I/O 峰值 | 独占实例并监控 WAL/I/O | 风险：强制全实例检查点，可能造成 I/O 峰值 |
| 105 | `SET ROLE reporting_role;` | 未执行 | 未执行；风险：改变当前会话权限身份 | 用户确认测试角色权限并准备 `RESET ROLE` | 风险：改变当前会话权限身份 |
| 106 | `RESET ROLE;` | 未执行 | 未执行；风险：恢复会话权限身份，依赖前置状态 | 与 D11 在同一受控会话执行 | 风险：恢复会话权限身份，依赖前置状态 |
| 107 | `SET SESSION AUTHORIZATION reporting_user;` | 未执行 | 未执行；风险：改变 session authorization，属于安全身份切换 | 用户确认目标角色并准备恢复连接/授权 | 风险：改变 session authorization，属于安全身份切换 |
| 108 | `RESET SESSION AUTHORIZATION;` | 未执行 | 未执行；风险：恢复 session authorization，依赖前置状态 | 与 D13 在同一受控会话执行 | 风险：恢复 session authorization，依赖前置状态 |
| 109 | `LOCK TABLE public.orders IN SHARE MODE;` | 未执行 | 未执行；风险：持有表锁，可能阻塞其他会话 | 独占测试窗口、手动事务并准备 ROLLBACK | 风险：持有表锁，可能阻塞其他会话 |
| 110 | `REFRESH MATERIALIZED VIEW CONCURRENTLY public.order_summary;` | 未执行 | 未执行；风险：持久更新物化视图内容并消耗资源 | 确认视图为测试对象、唯一索引存在并准备数据核对 | 风险：持久更新物化视图内容并消耗资源 |

## Purpose

验证 PostgreSQL 函数组合、会话类 DAL、事务命令和维护命令在 CloudDM 中的执行与状态边界。

## Scope

- 路由：`/#/sql`；数据源 `codex-it-postgres-16`。
- SQL 来源：`open-cdm-test/src/test/resources/split/postgres/common/` 下的 `dql_postgres_function_combinations_0.txt` /
  `_1.txt` 与 `dcl_postgres_dal_combinations_batch_0.txt` / `_1.txt`。
- 不覆盖：生产锁、真实长事务和跨用户通知压力。

## Preconditions

- PostgreSQL 隔离容器映射至 `127.0.0.1:5433`，数据库为 `codex_integration`。
- `public.orders`、测试物化视图和测试角色已创建；账号具备维护命令权限。

## Test Data

- 编号：D01；数据说明：查询表；构造方式：建表并插入三行；唯一标识：`public.orders`；清理方式：删除表
- 编号：D02；数据说明：物化视图和角色；构造方式：基于 D01 创建；唯一标识：fixture 名称；清理方式：删除视图和角色

### POSTGRES-SMOKE-01 基础查询（P0）

- Chrome 操作：执行 `SELECT version();`、`SELECT 1;` 和订单表查询。
- 预期结果：版本、常量和表数据可见。
- 恢复/清理：关闭结果页签。

### POSTGRES-MAIN-01 函数组合（P0）

- Chrome 操作：逐条执行 function combinations 原 fixture 12 条和官方函数族补充 fixture 10 条 SQL。
- 预期结果：标量、聚合、数组及 JSON 等函数有结果；数组结果不出现 `ReadException`，短元素、NULL 与超长元素均可序列化。
- 恢复/清理：错误后执行 `SELECT 1;`。

### POSTGRES-MAIN-02 DAL 与管理命令（P0）

- Chrome 操作：逐条执行原 DAL fixture 27 条与补充 fixture 4 条，包括 `SHOW/SET/RESET`、`PREPARE/EXECUTE/DEALLOCATE`、事务、锁、
  `LISTEN/NOTIFY/UNLISTEN` 和 `REFRESH MATERIALIZED VIEW`。事务语句先切换手动事务模式。
- 预期结果：维护和通知命令有执行记录；自动提交模式在连接空闲超时内保持同一物理会话，session 参数和 prepared statement
  跨点击保留；手动模式的 BEGIN/SAVEPOINT/ROLLBACK TO/RELEASE/COMMIT 更新真实事务及页面状态。
- 恢复/清理：释放锁、结束事务、取消监听并恢复 session 参数。

### POSTGRES-STATE-01 会话边界（P0）

- Chrome 操作：自动事务下分次执行 `SET work_mem='8MB';`、`SHOW work_mem;`；再分次执行 `PREPARE codex_stmt AS SELECT 1;`、
  `EXECUTE codex_stmt;`、`DEALLOCATE codex_stmt;`。
- 预期结果：连接空闲超时内 SHOW 返回 `8MB`，EXECUTE 成功，DEALLOCATE 成功；超时后旧 prepared statement 不应被误认为仍存在。
- 恢复/清理：RESET work_mem 并释放 prepared statement；手动事务会话重复时结束事务。

### POSTGRES-FAILURE-01 错误恢复（P1）

- Chrome 操作：在无事务状态执行需要事务的 `LOCK TABLE`，再执行 `SELECT 1;`。
- 预期结果：LOCK 返回明确事务错误；后续查询成功。
- Permission/Lifecycle：只读账号执行维护命令应失败；刷新页面后 smoke SQL 可继续。

## 页面已执行 SQL 明细

选中 `codex-it-postgres-16 / codex_integration.public`。表格按执行顺序排列；每行单独点击“执行”，同一组不要关闭查询页签。
`自动`、`手动`指页面事务模式，不是 SQL 本身的选项。

### 2026-09-15 函数组合 fixture 逐条复测

CloudDM 查询页签为 `codex_integration.public`，连接显示 `@127.0.0.1:5433`；页面 `SELECT version();` 返回 PostgreSQL
16.15。以下 12 条均来自 `dql_postgres_function_combinations_0.txt` 第一段，逐条点击“执行”，不是解析器单测结果。

函数 SQL：页面执行 12/12；数据库执行成功 12/12；按页面展示判定 11 通过、1 条数组显示问题。

#### 官方函数族补充用例

这些 SQL 已加入 `open-cdm-test/src/test/resources/split/postgres/common/dql_postgres_function_combinations_1.txt`，覆盖原
12 条以外的格式化、range、网络、XML、JSON 集合、扩展聚合、几何、窗口、catalog
和数组转换函数。函数族以 [PostgreSQL 16 官方函数目录](https://www.postgresql.org/docs/16/functions.html)
为对照；几何距离采用 [官方 `<->` 运算符](https://www.postgresql.org/docs/16/functions-geometry.html)。

补充函数正式 SQL：页面执行 10/10，数据库执行成功 10/10；按页面展示判定 7 通过、3 条数组显示问题。另有 2 条探索 SQL 在页面失败并修正：
`NUMRANGE(1,5) @> 3` 因右值为 integer 而非 numeric 报 operator 不存在；`DISTANCE(POINT(0,0),POINT(3,4))` 因 PostgreSQL
几何距离是 `<->` 运算符而非同名函数报 function 不存在。这两条错误写法未加入正式 fixture。

### 2026-09-15 DAL fixture 逐条复测

以下 27 条均来自 `dcl_postgres_dal_combinations_batch_0.txt` 第一段。D01–D17 使用自动事务模式，D18–D27
使用手动事务模式；同一查询页签跨点击保留。命令无结果集时按页面“执行信息”中的 `0 rows affected` 判定。

DAL SQL：页面执行 27/27，页面执行信息显示成功 27/27。事务、角色、session 参数和 prepared statement 均已复位或释放；通知监听已取消。

#### 官方管理命令补充用例

以下 4 条已加入 `open-cdm-test/src/test/resources/split/postgres/common/dcl_postgres_dal_combinations_batch_1.txt`
；对照 [SHOW](https://www.postgresql.org/docs/16/sql-show.html)、[DISCARD](https://www.postgresql.org/docs/16/sql-discard.html)
和 [RESET](https://www.postgresql.org/docs/16/sql-reset.html) 官方命令文档。均在自动事务模式逐条执行。

管理 SQL 合计页面执行 31/31，执行成功 31/31。

补充用例后在同一 CloudDM 页签回归执行 `SELECT 1;`（返回 `1`）、`SHOW work_mem;`（返回默认 `4MB`）和
`SELECT CURRENT_USER, SESSION_USER;`（两列均为 `postgres`）。页面事务模式已切回自动。

静态 split 验证：新函数资源 10 条 × 7 个 PostgreSQL 变体 = 70/70 通过；新 DAL 资源 4 条 × 7 个变体 = 28/28 通过。资源中的
`PG_GET_SERIAL_SEQUENCE`/`PG_TABLE_IS_VISIBLE` 组合按现有分类为 `METADATA`，`DISCARD SEQUENCES` 为
`SESSION_SETTING_WRITE`。没有因此修改生产解析代码。

原资源定向回归：函数 12 条 × 7 变体 = 84/84 通过；DAL 27 条 × 7 变体 = 189/189 通过。四个资源合计静态 split
371/371。页面本次正式资源 SQL 53 条（函数 22、DAL 31）全部提交且数据库执行成功；其中 4 条含 PostgreSQL
数组返回值的页面展示需要改进。这里新增的是官方函数族的代表性覆盖，不声称已穷尽 PostgreSQL 16 的全部可调用函数。

### PostgreSQL 16 官方目录追加核对与 CloudDM 页面记录（2026-09-16）

官方依据：[函数目录](https://www.postgresql.org/docs/16/functions.html)、[SQL 命令目录](https://www.postgresql.org/docs/16/sql-commands.html)
。隔离实例 `codex-it-postgres-16 / codex_integration.public` 的 CloudDM 页面 `SELECT version();` 返回 PostgreSQL
16.15；既有表格仅有 22 条函数与 31 条 DAL 页面记录。静态 fixture 目录为
`open-cdm-test/src/test/resources/split/postgres/common/`；下表中“有
fixture、未入表”表示搜到具体输入，但此前页面表格没有该函数或语法形式，不能以静态覆盖代替本轮页面实测。

- 类别 / 官方条目：[日期 `date_bin`](https://www.postgresql.org/docs/16/functions-datetime.html)；现有记录依据：`dql_postgres_source_common_0369.txt` 有输入；原函数表 F01–F22 无 `date_bin`；应补 SQL / 前提：P02；常量时间，无对象副作用；本轮页面实际结果：`2026-01-01 10:30:00`；判定：页面 PASS；此前未入表
- 类别 / 官方条目：[数组 `array_remove`](https://www.postgresql.org/docs/16/functions-array.html)；现有记录依据：`dql_postgres_source_common_2608.txt` 有输入；原表只有 append / position / 转换；应补 SQL / 前提：P03；NULL 与非 NULL 混合数组，经 `array_to_string` 可核对；本轮页面实际结果：`1,2`；判定：页面 PASS；此前未入表
- 类别 / 官方条目：[multirange 函数](https://www.postgresql.org/docs/16/functions-range.html)；现有记录依据：原表 F14 只测 NUMRANGE；common 静态资源已有 multirange 输入；应补 SQL / 前提：P04；int4multirange 字面量；本轮页面实际结果：lower=`1`、upper=`4`、merge=`[1,4)`；判定：页面 PASS；此前主要语法形式未入表
- 类别 / 官方条目：[WAL 位置函数](https://www.postgresql.org/docs/16/functions-admin.html)；现有记录依据：`dql_postgres_source_common_0023.txt` 有输入；原表 F06 仅客户端/会话信息；应补 SQL / 前提：P05；只读查询主库 WAL 位置；本轮页面实际结果：3 列均非 NULL，本次均为 `0/1A31368`；判定：页面 PASS；此前未入表
- 类别 / 官方条目：[JSON path predicate](https://www.postgresql.org/docs/16/functions-json.html)；现有记录依据：common 静态资源有 JSON path，原 F04/F17 只测构建/查询数组/键；应补 SQL / 前提：P06；常量 JSON；本轮页面实际结果：`True`；判定：页面 PASS；此前未入表
- 类别 / 官方条目：[ordered-set 聚合](https://www.postgresql.org/docs/16/functions-aggregate.html)；现有记录依据：`dql_postgres_source_common_2581.txt` 有 `percentile_cont`；原表 F08/F18 只测普通聚合；应补 SQL / 前提：P07；沿用 3 行 `public.orders`；本轮页面实际结果：中位数 `20.0`，工作台追加 `LIMIT 1000`；判定：页面 PASS；此前 `WITHIN GROUP` 未入表
- 类别 / 官方条目：[窗口帧 `first_value`](https://www.postgresql.org/docs/16/functions-window.html)；现有记录依据：`dql_postgres_source_common_2218.txt` 有输入；原表 F09/F20 只测 LAG/排名；应补 SQL / 前提：P08；沿用 `public.orders`，明确 ROWS 帧；本轮页面实际结果：3 行值均为 `10.50`，工作台追加 `LIMIT 1000`；判定：页面 PASS；此前窗口帧未入表
- 类别 / 官方条目：[位字符串 `bit_count`](https://www.postgresql.org/docs/16/functions-bitstring.html)；现有记录依据：`dql_postgres_source_common_1061.txt` 有输入；原表无位串函数；应补 SQL / 前提：P09；常量位串；本轮页面实际结果：`3`；判定：页面 PASS；此前未入表
- 类别 / 官方条目：[事务快照 `pg_snapshot_xmin`](https://www.postgresql.org/docs/16/functions-info.html)；现有记录依据：`dql_postgres_source_common_0430.txt` 有输入；原表无快照函数；应补 SQL / 前提：P10；当前快照，不改变事务；本轮页面实际结果：1 行，本次值 `764`；判定：页面 PASS；此前未入表
- 类别 / 官方条目：[`DISCARD ALL`](https://www.postgresql.org/docs/16/sql-discard.html)；现有记录依据：common 静态资源有语句；原 DAL 表 D06/D07/D30 只测 PLANS/TEMP/SEQUENCES；应补 SQL / 前提：P11；自动事务且不保留 prepared statement；本轮页面实际结果：Execution Information `0 rows affected`；判定：页面 PASS；此前关键 ALL 形式未入表
- 类别 / 官方条目：[`EXPLAIN` 带 ANALYZE/BUFFERS](https://www.postgresql.org/docs/16/sql-explain.html)；现有记录依据：common 静态资源有 EXPLAIN ANALYZE；原 DAL 表无对应页测；应补 SQL / 前提：P13/P14/P15；仅查询隔离表 `public.orders`；本轮页面实际结果：修复后 `(ANALYZE, BUFFERS)` 返回 8 行计划、`(ANALYZE)` 返回 5 行计划、不带选项返回 2 行计划；判定：页面 PASS：三种形式均已进入 PostgreSQL 执行链
- 类别 / 官方条目：[`ABORT`/`END` 事务别名](https://www.postgresql.org/docs/16/sql-abort.html)；现有记录依据：原 DAL D18–D22 测 BEGIN/COMMIT，`ABORT;` 在 common 输入未检出；应补 SQL / 前提：P23/P26；手动只读事务、不更新订单；本轮页面实际结果：`ABORT` 和 `END` 均 `0 rows affected`，最终 SELECT 1 正常；判定：页面 PASS；`ABORT` 静态例子仍缺
- 类别 / 官方条目：[`DECLARE`/`FETCH`/`CLOSE` 游标](https://www.postgresql.org/docs/16/sql-declare.html)；现有记录依据：common 静态资源有输入；原 DAL 表无游标生命周期；应补 SQL / 前提：P19–P22/P25；手动事务、专用游标名、`public.orders`；本轮页面实际结果：修复后 NO SCROLL DECLARE、FETCH NEXT、FETCH ALL、CLOSE 与普通 DECLARE 均成功；FETCH 分别返回 1 行和剩余 2 行；判定：页面 PASS：完整游标生命周期已执行并提交

页面逐条实测 SQL（每行在 CloudDM 编辑器执行，不是 PostgreSQL 直连）：

这只是官方目录的已定位缺口和页面实测，不代表 PostgreSQL 16 全函数名或全 SQL 命令已核对完。P13/P15/P19–P22/P25 的旧失败定位到
`PgRewriteSpi.rewriterLimit`：PostgreSQL 分类器会把带查询语义的 EXPLAIN ANALYZE、DECLARE、FETCH、CLOSE 纳入 SELECT
请求，但这些语句的根 AST 不是顶层 `selectstmt`。修复将 LIMIT 改写限定为顶层 SELECT；管理语句保持原文下发，普通 SELECT 仍追加或收紧
LIMIT。手动游标事务已提交并恢复自动模式，没有持久化数据变更。

### 修复构建与运行产物证据

- 门禁：源码/模块；实际证据：`PgRewriteSpi` 仅对根 AST 为顶层 SELECT 的语句执行 LIMIT 改写；`./gradlew :sql-postgres:test` 构建成功，模块当前 `test NO-SOURCE`；判定：PASS 编译；无独立单测源码
- 门禁：首次单插件部署；实际证据：仅更新 `ds-postgres-lib.jar` 后，13:21 页面仍出现旧 NPE；日志堆栈行号对应旧类；判定：FAIL，证明单插件部署不足，未作为验收结果
- 门禁：共享运行插件部署；实际证据：重建并执行 `local` 的 8 个已注册直接依赖插件：cloudberry、cockroachdb、gauss、greenplum、hologres、kingbasees、polardb、postgres；各运行 JAR 内 `PgRewriteSpi.class` SHA-256 均为 `16cc1fd6b24a7198b6db1b83d554f5d657ae33c650c92f0bb4a69482b3fb1b50`；判定：PASS，运行类与编译产物一致
- 门禁：IDEA 服务；实际证据：通过 Pika Control 启动 `DmAloneLauncherGradle` executionId 15；`/healthcheck` 返回 `ok`，日志显示 `PgDsPlugin` 安装及 `Embedded worker plugins finished`；判定：PASS
- 门禁：页面复测；实际证据：13:24:34 与 13:25:08 的 7 条原失败 SQL 全部出现 Execution Information/结果；13:24 之后服务日志无 `PgRewriteSpi` NPE；判定：PASS

## 2026-09-16 CloudDM 解析/下发全量回归基线

本轮均通过 CloudDM 页面提交；历史页面记录不计本轮回归。已列编号：F01–F22 加 P01–P10 共函数/版本查询 32；D01–D31 加 P11–P27 共
DAL/事务/恢复 48；总计 **80 个表格编号**。P13/P15/P19–P22/P25 在修复并部署全部 PostgreSQL 家族运行插件后重新提交，旧 NPE
证据不再作为当前结果。

- 类型：函数/版本查询；本轮总数：32；PASS 下发：32；FAIL CloudDM：0；BLOCKED：0；NOT RUN：0
- 类型：DAL/事务/恢复；本轮总数：48；PASS 下发：37；FAIL CloudDM：0；BLOCKED：0；NOT RUN：11
- 类型：合计；本轮总数：80；PASS 下发：69；FAIL CloudDM：0；BLOCKED：0；NOT RUN：11

历史结果未机械计入；下表是 2026-09-16 本轮重新提交的页面证据。

- 编号范围：F01–F22、P01–P10；页面时间：11:58:28–11:58:29；本轮实际：32 条函数/版本原文同批提交，产生 32 个结果页签；数组、XML、聚合、窗口、WAL、JSON path 等均进入 Execution Information，恢复查询返回 1；状态：PASS 32/32
- 编号范围：D06–D10、D15–D17、D24–D26、D28–D31、P11–P12、P14、P16、P27；页面时间：11:58:56；本轮实际：20 条自动事务安全命令同批提交；session 参数、PREPARE 生命周期、LISTEN/NOTIFY/UNLISTEN、SHOW/DISCARD/RESET、普通 EXPLAIN 和恢复查询均进入 Execution Information；状态：PASS 20/20
- 编号范围：D18–D22、P17–P18、P23–P24、P26；页面时间：11:59:49；本轮实际：切到 Tx: Manual，同批提交 10 条只读事务生命周期；BEGIN/SAVEPOINT/ROLLBACK TO/RELEASE/COMMIT、SET TRANSACTION、ABORT、END 均为 0 rows affected；随后恢复 Tx: Auto；状态：PASS 10/10
- 编号范围：P13、P15；页面时间：13:24:34；本轮实际：修复后与普通 SELECT、已有 LIMIT、普通 EXPLAIN 同批提交；两条带选项 EXPLAIN 分别返回 8 行和 5 行计划。普通 SELECT 被追加 `LIMIT 1000`，已有 `LIMIT 1` 保持不变，普通 EXPLAIN 返回 2 行计划；状态：PASS 2/2；相邻 LIMIT/EXPLAIN 回归 3/3
- 编号范围：P19–P22、P25；页面时间：13:25:08；本轮实际：Tx: Manual 下按生命周期同批提交；NO SCROLL DECLARE 为 0 rows affected，FETCH NEXT 返回 1 行，FETCH ALL 返回 2 行，CLOSE 为 0 rows affected，普通 DECLARE 为 0 rows affected；额外 CLOSE 清理简单游标；状态：PASS 5/5
- 编号范围：事务恢复；页面时间：13:25:29–13:25:41；本轮实际：提交游标测试事务，页面显示“递交事务”；随后切回自动事务模式；状态：PASS；最终 `Tx: Auto`

### NOT RUN（待用户执行）

1. 结束事务，执行 `UNLISTEN *;` 并恢复 session 参数。
2. 仅删除由本流程新建的测试对象；本次复测沿用隔离环境中已有的表、物化视图和角色，未删除它们。
3. 确认容器运行且 CloudDM `SELECT 1;` 成功。

## Skip Conditions

- 无隔离 PostgreSQL 时不能用 parser 单测代替页面验证。
- 无法保持同一物理连接时，同会话 PREPARE 场景记录为覆盖缺口，不标为成功。
