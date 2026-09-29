# CockroachDB 常用 SQL 与查询链路验收

## 范围与实现

本轮范围为常用 SQL 和 CloudDM 查询链路，不包含 BACKUP/RESTORE、CHANGEFEED、多区域、集群管理和完整函数目录。

- 独立 CockroachDB SQL 引擎，复用 PostgreSQL 通用语法；UPSERT、SELECT AS OF SYSTEM TIME 和 SHOW 扩展仅由 CockroachDB 插件启用。
- UPSERT 以 MERGE 行为分析，RuleDomain 保留目标列与 UPDATE 冲突策略。SHOW 按实际对象生成元数据域；单段 SHOW TABLES FROM 名称保守使用实例范围，避免猜测数据库/schema。
- 增加经服务器确认的五个 pg_catalog 内置函数；用户 schema 和大小写敏感名称不享受该豁免。快照时间表达式中的函数仍接受行为权限检查。
- 旧 CockroachDB 配置中的 `PG SQL` 在读取/序列化时归一到 `CockroachDB SQL`，显式 ISO SQL 配置保持原选择。
- 修复公共 JDBC 会话吞掉 commit/rollback 异常的问题；查询入口在操作异常时返回错误提示和 Done。失败不触发客户端自动重放，也不确认事务成功。

## 环境与产物证据

服务器为本机隔离容器 `clouddm_crdb_test-cockroachdb-1`，CockroachDB CCL v24.3.0，aarch64；`SHOW server_version` 为 13.0.0。数据库 `codex_crdb_sql`，端口 26257，原生 CLI 和 JDBC 直连分别验证。连接凭据来源为仓库测试配置，不写入报告。

CloudDM 为 `http://localhost:8222` Alone 模式，数据源 `codex-crdb-sql-24-3`（ID 48），页面明确显示目标数据库/schema 与 127.0.0.1:26257。通过内置浏览器真实操作 SQL 工作台，没有绕过页面提交 API。

最终 `DmAloneLauncher` 由 Pika Control 启动，executionId=49，DEBUG/RUNNING；健康检查返回 `ok`，日志记录 `Embedded worker plugins finished`。完整构建成功；最后的查询入口修改亦经依赖编译和契约测试后加载。

运行目录 `/Users/pika/clouddm/plugins/ds-cockroachdb-lib.jar` 含 `CrdbSqlEngineSpi`、`CrdbSqlParser` 以及内嵌 PostgreSQL/common 依赖。最终 SHA-256：`300847b5ce7c4541ed6c0b6b97e7f53d6079563b87cc65301d99d83781a973c8`。启动日志确认该插件加载目录，进程文件句柄确认使用其中的插件。

## 自动化结果

| 检查 | 结果 |
| --- | --- |
| CockroachDB fixture | 83/83：62 split、21 behavior |
| CockroachDB 契约 | 11/11：RuleDomain、系统函数边界、PostgreSQL 隔离、流关闭、EXPLAIN、旧配置和事务错误终态 |
| 真实 JDBC | 4/4：保存点/提交可见性、错误恢复、原生类型、注入 SQLSTATE 40001 后不落库 |
| PostgreSQL 13 共享回归 | 215144/215144：213863 split、1281 behavior |
| 构建 | 全量 `package/all_build.sh` 成功；插件 build/local 成功；最终全量构建成功 |
| 工作区 | 两仓库 diff --check；没有创建/切换分支、提交或推送 |

本机日志：`/tmp/crdb_pg_regression.log`、`/tmp/crdb_contract_live.log`、`/tmp/crdb_full_build_final.log`。测试源码和可复用清单位于相邻 `open-cdm-test` 的 `CockroachSqlContractTest`、`CockroachLiveTest` 和 `docs/cockroachdb-coverage.md`。临时日志会被系统清理，长期复核应重新运行测试。

## 原生验证

原生通道验证了 SELECT/CTE/window/LIMIT、UPSERT、INSERT ON CONFLICT、UPDATE、DELETE、CREATE/ALTER TABLE、INDEX、VIEW、SEQUENCE、schema、角色 GRANT/REVOKE，以及 SHOW DATABASES/TABLES/SCHEMAS/SEQUENCES/TYPES/ENUMS/ROLES/USERS/COLUMNS/CONSTRAINTS/INDEX/CREATE。常用脚本保存在本机 `/tmp/crdb_common.sql`，输出为 `/tmp/crdb_direct_common.log` 与 `/tmp/crdb_direct_common_resume.log`。

立即对刚建的表查询历史快照时，数据库曾返回 relation does not exist；等表具备历史版本后同类查询成功。SHOW ROLES WITH COMMENT 原生语法不合法，负向 fixture 明确拒绝，未放宽解析器。

独立连接提交前观察使用捕获的历史快照，提交后使用当前查询。CockroachDB 当前快照读可能等待未提交写入，不能把等待误判为连接失败。

## 页面实际执行证据

以下为实际编辑器 SQL。执行信息逐条核对了终态，结果页核对了行列和值；原生覆盖使用独立对象，不能将它视为相同 SQL 已从页面下发。

| SQL / 操作 | 模式与结果 | 证据 |
| --- | --- | --- |
| `SELECT current_database(), current_schema(), version();` | Auto，1 行；目标库、public、24.3.0 | 实例48，15:33:38，result1 |
| `CREATE TABLE public.crdb_ui_0922 (id INT PRIMARY KEY, value STRING, amount DECIMAL(12,2), payload JSONB);` | Auto，建表完成 | 15:34:07 |
| `UPSERT INTO public.crdb_ui_0922 VALUES (1,'中文',12.50,'{"ok":true}'),(2,'O''Reilly',0.01,'{}') RETURNING *;` | Auto，2 行4列；中文、单引号、JSON、小数值正确 | 15:34:16，result2 |
| `UPSERT INTO public.crdb_ui_0922 (id,value,amount,payload) SELECT 1,'updated',20.25,'{}'::JSONB RETURNING id,value,amount;` | Auto，原行更新为 1/updated/20.25 | 15:34:31，result3 |
| `SHOW COLUMNS FROM public.crdb_ui_0922;` | Auto，4 条字段定义 | 15:34:48，result4 |
| `SHOW CREATE TABLE public.crdb_ui_0922;` | Auto，返回实际 DDL 与主键 | 15:35:06，result5 |
| `SHOW TABLES FROM codex_crdb_sql.public;` | Auto，返回隔离表 | 15:35:06，result6 |
| `SELECT id,value FROM public.crdb_ui_0922 AS OF SYSTEM TIME follower_read_timestamp() ORDER BY id;` | Auto，2 行；分页改写追加 LIMIT 50 后仍正确执行 | 15:35:06，result7 |
| `CREATE INDEX crdb_ui_value_0922 ON public.crdb_ui_0922(value);` | Auto，完成 | 15:35:44 |
| `ALTER TABLE public.crdb_ui_0922 ADD COLUMN active BOOL DEFAULT true;` | Auto，完成 | 15:35:44 |
| `CREATE VIEW public.crdb_ui_view_0922 AS SELECT id,value FROM public.crdb_ui_0922;` | Auto，完成 | 15:35:44 |
| `CREATE SEQUENCE public.crdb_ui_seq_0922;` | Auto，完成 | 15:35:44 |
| `INSERT INTO public.crdb_ui_0922(id,value) VALUES(2,'conflict') ON CONFLICT(id) DO UPDATE SET value=excluded.value RETURNING id,value;` | Auto，1 行 | 15:35:44 |
| `UPDATE public.crdb_ui_0922 SET value='restored' WHERE id=2 RETURNING id,value;` | Auto，1 行 | 15:35:44 |
| `WITH q AS (SELECT id,value FROM public.crdb_ui_0922) SELECT id,value,row_number() OVER(ORDER BY id) AS rn FROM q ORDER BY id LIMIT 1 OFFSET 1;` | Auto，1 行 | 15:35:44 |
| `SHOW INDEX FROM public.crdb_ui_0922;` | Auto，7 行索引字段 | 15:35:44 |
| `SELECT unique_rowid(), unordered_unique_rowid(), cluster_logical_timestamp(), experimental_uuid_v4();` | Auto READ COMMITTED 被原生数据库拒绝；Manual SERIALIZABLE 返回1行4列 | 15:35:44错误、15:36:21成功 |
| `SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;` | 新 Manual 会话首条，完成 | 15:36:21 |
| `BEGIN;` | Auto 明确提示先切手动模式，未下发 | 最终实例，15:47 |
| `CREATE ROLE crdb_ui_reader_0922;` | Auto，完成 | 最终实例，15:47:40 |
| `GRANT SELECT ON TABLE public.crdb_ui_0922 TO crdb_ui_reader_0922;` | Auto，完成 | 最终实例，15:47:40 |
| `REVOKE SELECT ON TABLE public.crdb_ui_0922 FROM crdb_ui_reader_0922;` | Auto，完成 | 最终实例，15:47:40 |

最终实例49的事务恢复脚本，15:44:11逐条执行：

```sql
INSERT INTO public.crdb_ui_0922(id,value) VALUES(101,'before');
SAVEPOINT user_checkpoint;
INSERT INTO public.crdb_ui_0922(id,value) VALUES(102,'after');
INSERT INTO public.crdb_ui_0922(id,value) VALUES(101,'duplicate');
ROLLBACK TO SAVEPOINT user_checkpoint;
RELEASE SAVEPOINT user_checkpoint;
```

第四条原生重复主键错误；第五、第六条成功。15:44:19点击提交后，独立连接确认 id=101 存在，id=102 不存在。早先长 SERIALIZABLE 事务的提交重试曾被公共代码吞掉，这是本轮修复的真实缺陷，不能把早先的“递交事务”提示算作成功。

最终实例49又使用当前会话变量注入提交错误：

```sql
SET inject_retry_errors_on_commit_enabled=on;
INSERT INTO public.crdb_ui_0922(id,value) VALUES(201,'must_not_commit');
```

15:45:43点击提交，页面明确显示 `restart transaction` 和注入来源，Run恢复可用。点击回滚后设置变量 off；页面和独立连接均确认 id=201 未落库。该路径同时由真实 JDBC 测试固定，验证 SQLSTATE=40001。未实现自动事务重放。

刷新后 Auto 查询返回 `(1,updated)、(2,restored)、(101,before)`，与独立连接相同；浏览器错误日志为空。最终查询 ID `q1a0c8143e87c490` 是恢复查询之一，后续刷新再次核对相同三行。

## 限制与清理

- 原生 `cluster_logical_timestamp()` 不支持 READ COMMITTED，使用 SERIALIZABLE；这是数据库限制。
- 低权限 CloudDM 账号、完整元数据目录、网络断开/集群重试压力未做页面验收；行为、RuleDomain和系统函数权限边界由自动化验证。
- 血缘保持既有 `LineageAnalysisSpi.EMPTY`，不能宣称支持。BEGIN/SET TRANSACTION AS OF、索引提示、locality/family/storage、专属 ALTER、RETURNING NOTHING 不在本轮承诺内。
- 页面数组值沿用现有类型包装展示，本轮没有改变通用结果协议。
- 已关闭会话错误注入并恢复 Auto；删除本轮临时表、视图、序列、schema和角色。隔离数据库 `codex_crdb_sql`、容器和测试数据源保留用于复测；public 中测试表为0。
- 没有修改任何历史 migration；没有改动前端源码；已有无关文档保留。

长期复测步骤见 [CockroachDB SQL 查询链路](../tests/frontend/datasource/cockroachdb_sql.md)。语法范围与官方入口见相邻测试工程的覆盖清单；[CockroachDB 事务重试说明](https://www.cockroachlabs.com/docs/v24.3/transaction-retry-error-reference.html)解释原生重试错误。
