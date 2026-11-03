# CockroachDB 常用 SQL 与查询链路验收

当前组合覆盖及最新结果见文末「六项解析缺口补齐验收」；前面的基线和 DCL 增量结果保留各自验证范围。

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

## 初始常用 SQL 基线的自动化结果

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

## DCL 扩展范围

以 CockroachDB 24.3.0 官方 GRANT、REVOKE、ALTER DEFAULT PRIVILEGES、CREATE/ALTER/DROP ROLE/USER 文档和
[v24.3.0 sql.y](https://github.com/cockroachdb/cockroach/blob/v24.3.0/pkg/sql/parser/sql.y) 为语法依据。
参考 Greenplum/Cloudberry 的插件内扩展方式，DCL AST、拆句类型、行为和规则域均由 `ds-cockroachdb` 实现；
本次不修改共享 PostgreSQL 语法或另外两个插件。

| 语句类别 | 实现和验证范围 |
| --- | --- |
| 对象 GRANT/REVOKE | 数据库、限定 schema、表、序列、类型、函数/过程；多对象、多受权人、引号名称和 WITH GRANT OPTION/GRANT OPTION FOR。原生 JDBC 覆盖数据库/schema/表/序列/类型/函数；过程签名与 ALL PROCEDURES 仅静态验证。 |
| SYSTEM GRANT/REVOKE | 实例权限，含 GRANT OPTION；行为目标为实例，规则域保留受权人。原生验证 VIEWACTIVITY。其他权限名称及对象适用性由数据库判定。 |
| 外部连接授权 | EXTERNAL CONNECTION，独立命名的实例级 Resource 行为对象，不误记为表或 DBLink。原生验证 USAGE 的授予、撤销授权能力及撤权。 |
| 批量对象授权 | ALL TABLES/SEQUENCES/FUNCTIONS/PROCEDURES IN SCHEMA，及数据库/schema 的表通配符。行为保留真实范围，不生成名为 schema 的虚构表。 |
| 角色成员关系 | GRANT/REVOKE role-list 与 WITH ADMIN OPTION / ADMIN OPTION FOR；行为区分被授予角色和受权人，规则域沿用 GRANT/REVOKE 管理权限。原生验证授权、撤销管理能力及撤销成员关系。 |
| 默认权限 | FOR ROLE/USER/GROUP 或 FOR ALL ROLES，可选限定 schema；TABLES/SEQUENCES/TYPES/SCHEMAS/FUNCTIONS，授予和撤销（含授权能力）。行为保留当前数据库或明确 schema、创建者和受权人。原生验证 FOR ALL ROLES 的表默认权限及新表实际继承。 |
| 用户/角色 | CREATE [IF NOT EXISTS]、ALTER [IF EXISTS]、DROP [IF EXISTS]，GROUP 别名；角色名按原生规则（包括引号名称）归一为小写；支持 24.3 role flags、PASSWORD、VALID UNTIL、SUBJECT，以及 SET/RESET 和 IN DATABASE、ALL 范围。原生验证独立用户/角色、CREATELOGIN、NOLOGIN、NOSQLLOGIN、SUBJECT NULL 和指定数据库配置。 |

`REVOKE GRANT OPTION FOR ... ON ALL SEQUENCES IN SCHEMA` 不在 24.3 原生语法内：真实 JDBC 验证 SQLSTATE 42601，
Parser 负例同步拒绝；普通 ALL SEQUENCES 授予和撤权仍支持。非法 ADMIN/GRANT OPTION 混用、缺少对象、
错误 IF EXISTS 形式、未知角色选项和不合法限定名称均有负例。

这不是整个 CockroachDB SQL 语法的完整实现：函数/过程参数覆盖常用标量、限定类型、精度及数组，
不声称覆盖全部复杂类型声明；未覆盖参数化角色选项值、完整 SET 特殊语法或其他版本新增能力。
不扩展 SHOW GRANTS 等 DAL，也不声称增加血缘能力。新的原生测试使用唯一数据库、角色和外部连接，并清理自身对象。

重复验证入口：相邻 `open-cdm-test` 的 `dcl_crdb_0.txt`、`dcl_crdb_invalid_0.txt`、
`CockroachSqlContractTest` 和 `CockroachLiveTest.dclPrivilegesRolesAndDefaultsRoundTrip`。
页面验证仍需独立登录和实际执行，静态/JDBC 通过不能替代该证据。

### DCL 增量验收结果

- 格式化脚本、`:ds-cockroachdb:compileJava`、`:boot-alone:classes` 和 `:ds-cockroachdb:local` 均成功。
- CockroachDB 静态用例 187/187（129 split、58 behavior），契约测试 14/14，真实 JDBC 测试 5/5。
- 相关回归：Greenplum 382/382、Cloudberry 523/523、PostgreSQL 13 的 DCL 子集 17820/17820。
  本次没有重跑 PostgreSQL 13 的全部 SQL；共享 PostgreSQL 语法未被本次修改。
- 独立插件已部署；`DmAloneLauncher` DEBUG 实例 463 为 RUNNING，健康检查为 ok；
  日志确认 Alone 初始化与 embedded worker 插件加载完成，运行进程打开了新的 CockroachDB 插件 jar。
- 内置浏览器在管理员测试账号下核对了 24.3.0 和目标库，并逐条核对 24 条成功的测试准备/DCL/建表/清理语句：
  CREATE ROLE（引号大写名称）、对象/schema/system/external connection 授权、成员授权与 ADMIN OPTION 撤销、
  FOR ALL ROLES 默认权限、新表继承、反向撤权、角色 SET/RESET 和 DROP ROLE。角色名按原生规则归一为小写。
- 独立连接确认新表继承 SELECT；撤权后新建表无授权，system/external connection 授权和角色成员记录均为零。
  默认权限撤销不移除已存在表的继承授权；页面对 information_schema 的查询准确显示仅 inherited_target 仍有 SELECT。
- 24.3 不支持的 ALL SEQUENCES 授权能力撤销在页面被解析拒绝；紧接着查询和刷新后的重新查询均完成。
  恢复查询 ID 为 `q1a18750af8cabec`，刷新后查询 ID 为 `q1a1875172f0c6d8`。浏览器 Console error 数为 0。
- 首次测试准备时编辑器只执行了光标所在的 CREATE TABLE，因 schema 尚未创建而返回原生错误；
  选中整段脚本重试后四条均完成。该准备错误未计为 DCL 支持失败。
- 本轮唯一 schema、外部连接、两个角色均已删除；独立连接确认角色和 schema 剩余数均为 0；仅关闭本轮新建的 SQL 页签。
- 低权限 CloudDM 账号页面未复测；函数/过程等上表标为静态覆盖的路径未冒称页面通过。

刷新时发现一个原有的事务状态 concern：`ConsoleQueryService.txStatus()` 在已有会话切换/恢复 Auto 状态时，
不判断当前连接是否已经 autoCommit 就调用 `commitSession()`；PostgreSQL JDBC 会报告
`Cannot commit when autoCommit is enabled.`。页面之后可以继续查询并得到正确授权结果。
该路径未被此次 DCL 修改，事务逻辑保持原状；因此不将刷新状态恢复标为无错误通过。

本轮可复核日志：`/tmp/crdb_dcl_fixtures.log`、`/tmp/crdb_dcl_contract.log`、`/tmp/crdb_dcl_live.log`、
`/tmp/crdb_dcl_ui_native_before_revoke.log`、`/tmp/crdb_dcl_ui_native_after_revoke.log`；截图为
`crdb_dcl_ui_success.png`、`crdb_dcl_ui_reject.png`、`crdb_dcl_ui_cleanup.png`（本机 Codex visualization 目录）。


## 官方 DCL / DML / DDL 组合验收

### 官方依据与组合策略

以 [24.3 SQL 语句索引](https://docs.cockroachlabs.com/docs/v24.3/sql-statements)及各命令页面为依据，
目标服务器仍为 CockroachDB CCL v24.3.0，兼容版本 13。每条生成用例保存 `source`、维度、原始 SQL 和独立预期；
生成器及清单位于相邻 `open-cdm-test/scripts/generate_cockroach_combinations.py`、
`generate_cockroach_live_cases.py` 和 `src/test/resources/config/cockroachdb-*-combinations.json`。

| 类别 | 组合维度 | 新增 split |
| --- | --- | --- |
| DCL | 对象类别/多个对象 × 受权人 × GRANT/REVOKE × 授权能力；默认权限创建者/作用域/对象类别；用户与角色的 guard/flags、SET/RESET、成员关系、REASSIGN OWNED | 382 |
| DML | VALUES/SELECT/CTE × 普通 INSERT/冲突处理/UPSERT × 名称限定 × RETURNING；UPDATE 赋值/谓词/返回；DELETE 谓词/返回；连接、窗口、聚合、集合查询、TABLE、VALUES | 382 |
| DDL | 名称与 guard × 表定义/约束/计算列；CTAS、ALTER/DROP、索引/视图/物化视图、数据库/schema/类型/序列/函数/过程/comment | 179 |

在声明的静态维度内生成全组合，共 943 条 split 和 284 条行为断言；未宣称对官方全部参数做笛卡尔积。
真实执行使用确定性的两两组合：INSERT 12/108 个候选覆盖 63 个维度对，UPDATE 9/81 覆盖 54 对，
DELETE 10/27 覆盖 27 对；配合准备、断言和生命周期，共 205 条，再独立探测 8 条官方特有语法。
不覆盖 COPY 流协议、IMPORT/EXPORT 外部文件、BACKUP/RESTORE、CHANGEFEED、多区域、集群管理或完整函数目录。
没有生成 CSV，也没有新增 Java 测试类。

### 发现并修复的问题

- CockroachDB 插件补齐 `CREATE DATABASE IF NOT EXISTS` 和带 `CASCADE/RESTRICT` 的 `DROP DATABASE`，
  拆句、Catalog 行为和规则域同步，非法 guard 与尾部组合保留负例；不放宽 PostgreSQL 共享语法。
- 页面执行 `SELECT id, count(*) AS n FROM public.source GROUP BY id HAVING count(*) = 1 ORDER BY id` 时，
  共享 `PgRewriteSpi` 曾追加为 `HAVING ... LIMIT 50 ORDER BY id`。现在依据外层 ORDER BY 的 AST 位置插入 LIMIT；
  已有外层数值 LIMIT 取与编辑器上限的较小值，较小 LIMIT/OFFSET 保持原样。最终实例再次核对无 LIMIT、LIMIT 100 截断为 50、LIMIT 1 OFFSET 1 三条及返回值。契约覆盖 CockroachDB/PostgreSQL，页面复测返回 `(1,1)、(2,1)`，
  CTE/window 和显式 LIMIT/OFFSET 复测值一致。原失败保留在 UI 证据的 `priorFailure` 中。

### 分层结果

| 层 | 结果与证据 |
| --- | --- |
| 静态 | 1414/1414：1072 split、342 behavior（包含已有 187 条）；生成器 `--check` 一致 |
| 契约 | 17/17，含 943 条逐条解析、数据库规则域、分页顺序与 PostgreSQL 方言隔离 |
| 原生 JDBC | 6/6；组合场景记录 213 条，其中 210 成功、3 条预期原生错误；查询结果逐值断言 |
| CloudDM 管理员 Auto | 205 条主清单全部核对：202 成功、3 条预期原生错误；67 个结果集核对行列和值。分页缺陷修复后复测通过 |
| 官方特有探测 | 原生 8/8 成功；页面 2 项成功、6 项解析缺口，详见下表，不能称全部支持 |
| 相关回归 | Greenplum 382/382、Cloudberry 523/523；PostgreSQL 共享改写由契约测试验证，本轮未重跑 215144 条全量历史语法 |
| 构建/加载 | 完整 imports 优化及格式化成功，`:ds-cockroachdb:local` 成功；Pika 重启 DmAloneLauncher 实例 467 DEBUG/RUNNING，健康检查 ok |

原生错误分别为视图替换改变返回列类型、24.3 不接受 `ALTER PROCEDURE ... SECURITY INVOKER`（42601）、
重复主键（23505）。正向视图显式保持 DECIMAL(12,2)，过程用 RENAME/反向 RENAME；错误后下一批执行与查询均完成。

| 官方特有语法 | 原生 24.3 | CloudDM |
| --- | --- | --- |
| CREATE TABLE ... FAMILY main (...) | 成功 | 成功 |
| INSERT ... RETURNING NOTHING | 成功 | 成功，影响 1 行 |
| PRIMARY KEY ... USING HASH WITH BUCKET_COUNT | 成功 | 解析拒绝，未下发 |
| CREATE INDEX ... STORING (...) | 成功 | 解析拒绝，未下发 |
| CREATE INVERTED INDEX | 成功 | 解析拒绝，未下发 |
| UPDATE ... ORDER BY ... LIMIT ... RETURNING | 成功 | 解析拒绝，未下发 |
| DELETE ... ORDER BY ... LIMIT ... RETURNING | 成功 | 解析拒绝，未下发 |
| SELECT ... FROM table@primary | 成功 | 解析拒绝，未下发 |

以上为初轮探测结果。这 6 项原先作为合法语法缺口保留在 `officialSyntaxProbes`，后续已在文末一轮补齐，当前全部标记 `success`，不属于 SQL 负例。
FAMILY/RETURNING NOTHING 仅证明清单中的形式，不代表全部存储选项或全部 RETURNING 组合已覆盖。

### 页面证据、限制与清理

页面数据源为 `codex-crdb-sql-24-3`，实际测试库 `crdb_combo_ui_mvtgmv8i`，版本及 public schema 由页面查询确认。
唯一临时角色均为 NOLOGIN。测试包含引号表名、中文/单引号/分号、JSON/数组/布尔/枚举、小数、
默认权限继承及撤销、REASSIGN OWNED 后的实际 owner 查询；未验证低权限 CloudDM 账号。

逐条输入、执行 SQL（含产品分页改写）、终态与返回值保存为本机
`open-cdm-test/build/reports/open-cdm-test/cockroachdb-ui-combinations.json`；原生对应文件为
`cockroachdb-native-combinations.json`。这些是可重新生成的本地运行证据，不纳入源码提交。
可复用操作步骤维护在同一份 `tests/frontend/datasource/cockroachdb_sql.md`。

频繁关闭重开编辑器时出现 `exceed session max pool size: 5`，失败批次没有下发；
重启隔离实例后复用单一编辑器完成剩余测试。该会话释放/容量问题未在本次修改，不能宣称多页签生命周期通过。
原先记录的刷新 TxStatus/autoCommit concern 也未在本次修复。最终浏览器 Console error 数为 0。

页面执行 DROP DATABASE ... CASCADE 和 DROP ROLE 清理完成；独立连接确认本轮主库/辅助库/改名库及三个角色均为 0。
关闭本轮 SQL 页签，原有用户页签保留；测试容器和已有数据源继续保留。
没有提交或推送当前改动。


## 六项解析缺口补齐验收

沿用 [24.3 哈希分片](https://docs.cockroachlabs.com/docs/v24.3/hash-sharded-indexes)、
[CREATE INDEX](https://docs.cockroachlabs.com/docs/v24.3/create-index)、
[UPDATE](https://docs.cockroachlabs.com/docs/v24.3/update)、
[DELETE](https://docs.cockroachlabs.com/docs/v24.3/delete)和
[表表达式](https://docs.cockroachlabs.com/docs/v24.3/table-expressions)作为依据。

### 实现和范围

| 原缺口 | 当前覆盖 |
| --- | --- |
| 哈希分片 | 列级/表级 PRIMARY KEY、具名约束及 CREATE INDEX 的 USING HASH；默认桶数和 WITH BUCKET_COUNT；组合 STORING |
| STORING 索引 | 普通/UNIQUE、IF NOT EXISTS、单/多 stored columns、部分索引及排序键 |
| 倒排索引 | CREATE INVERTED INDEX，IF NOT EXISTS、JSONB/数组和部分索引 |
| UPDATE ORDER BY LIMIT | ASC/DESC、1/2 行、RETURNING、子查询谓词、CTE 和索引提示 |
| DELETE ORDER BY LIMIT | 同样保持真实写操作和受影响行数，编辑器 fetchLimit 不改写 DML |
| 索引提示 | @primary、@name、引号索引名、@[id]、@{FORCE_INDEX=...}、方向及常用布尔 flags；SELECT/UPDATE/DELETE 的目标表解析保持准确 |

共享 PostgreSQL AST 增加默认关闭的 indexExtensions/mutationLimit 两个开关；仅 CrdbDslProvider 启用。
没有修改 lexer 的 token 编号。行为沿用真实 AST 的对象、函数扫描、子查询与写入分类；
规则域在 CockroachDB 插件中处理新节点，STORING 不被当作键列，索引提示不被当作新表或列。
RETURNING 中的表达式、函数及子查询继续走分析；没有将整句 SQL 作为不透明字符串放行。
血缘仍未实现，不宣称全部 CockroachDB 索引/存储参数已支持。

### 组合与原生限制

新增 364 条 split、372 条 behavior（含 8 条语法负例），覆盖四种名称限定、guard、桶数、
索引类型/存储列/部分索引、DML 排序/行数/返回形式和嵌套条件；排序里的用户函数与带引号用户函数仍要求 CALL 权限。
静态生成清单变为 1307 split 与 656 behavior，连同既有基线累计 2150 条。
原来 8 个官方特有探测项当前都要求产品成功，清单没有删除原来的失败输入。

真实组合的 5 个二值维度（目标名称、UPDATE/DELETE、ASC/DESC、LIMIT 1/2、RETURNING）
采用 6/32 个确定性样本覆盖全部 40 个维度对。配合哈希/索引/提示/CTE/结果断言及错误恢复，本轮共 52 条。
`FORCE_INDEX` 与 `NO_INDEX_JOIN` 同时使用是原生 42601：保留为明确的原生错误，
两个合法形式分别验证，错误后继续执行 UPDATE、DELETE 和 SELECT。
数值索引 ID、部分布尔 flag 组合和某些静态笛卡尔积变体仅有静态覆盖，不冒充逐条 live 通过。

### 验证结果

- CockroachDB fixtures：2150/2150（1436 split、714 behavior）。
- 契约：18/18，逐条解析 1307 个生成 SQL；直接断言新索引/哈希表、带提示的 SELECT/UPDATE/DELETE 规则域，
  fetchLimit=1 不改变 DML 的 LIMIT 2；PostgreSQL 默认方言拒绝 CockroachDB 新语法。
- 原生 JDBC：6/6；组合场景记录 265 条 SQL（261 成功、4 条预期原生错误），其中本轮 52 条为 51 成功、1 原生错误。
- CloudDM：管理员 Auto 模式，CockroachDB 24.3.0；本轮 52 条逐条核对终态，51 成功、1 预期原生错误，
  20 个结果集逐值核对；刷新后带 FORCE_INDEX 的查询仍返回 `(1,9)、(3,0)`。浏览器 Console error 为 0。
- 完整 imports 优化/格式化及插件 local 构建成功。使用 Pika Control 停止旧实例再构建插件，
  新实例 468 DEBUG/RUNNING；启动日志确认 Alone 初始化及 embedded worker 插件加载完成，健康检查 ok，
  运行进程实际打开 `/Users/pika/clouddm/plugins/ds-cockroachdb-lib.jar`。

共享语法回归全部通过：PostgreSQL 13 为 215144/215144（213863 split、1281 behavior），
Greenplum 382/382、Cloudberry 523/523。`/tmp/crdb_six_pg.log`、`crdb_six_gp.log`、`crdb_six_cb.log` 保留本机日志。
插件 SHA-256：`cb9cb5c47743d9fa3a83b62703d11e8f4790c1b4b2dd3e207373cc66a8805e83`。

本轮本地原生证据在 `open-cdm-test/build/reports/open-cdm-test/cockroachdb-native-combinations.json`，
UI 逐条证据在同目录 `cockroachdb-six-ui.json`；截图为本机 visualization 目录中的 `cockroachdb_six_success.png`。
运行证据不提交到源码；长期复测读取维护中的 JSON 清单及同一份前端流程文档。

隔离库 `crdb_six_ui_mvtjjn2m` 已通过页面 DROP DATABASE CASCADE 清理，独立连接确认数据库计数为 0；
本轮未创建角色。原有三个 SQL 页签保留，测试页签关闭。没有改动前端源码，没有提交或推送。
先前发现的多页签 session pool 上限和 TxStatus/autoCommit concern 未在本轮修改；
本轮复用一个编辑器，没有出现会话容量错误。低权限产品账号仍未验收。
