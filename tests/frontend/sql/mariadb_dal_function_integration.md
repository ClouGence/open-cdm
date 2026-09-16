# MariaDB DAL 与函数集成验证

## SQL 执行明细

判定口径：CloudDM 能解析并将 SQL 发送到数据库即为通过；数据库返回错误不等于 CloudDM 失败。安全或破坏性操作保留待用户执行状态。

验证状态统一为：通过、CloudDM 失败、未执行、证据不足。通过仅表示已有证据确认 SQL 被解析并发送到数据库，数据库报错保留在执行证据中。历史执行与复测记录均保留，每行反映该次记录，不代表相同 SQL 的最新复测结论。

本表记录数：通过 0；CloudDM 失败 0；未执行 1；证据不足 0。包含重复执行记录，不等同于去重用例数。本次为文档证据整理，未重新执行 SQL。

| 编号 | SQL | 验证状态 | 实际结果/执行证据 | 前提/预期 | 来源/备注 |
| --- | --- | --- | --- | --- | --- |
| 1 | — | 未执行 | 原文没有可核实的逐条 SQL 执行结果。 | — | 下方保留复测计划，不计为已执行。 |

## Purpose

验证 MariaDB 使用独立版本语义执行官方管理命令及 MySQL 兼容函数，并验证三种停机语法后的恢复能力。

> 本文的 Suites 是复测步骤，不是逐条已执行 SQL 记录；当前没有可核实的本轮页面执行结果表，不能将 fixture 中的命令计为已测。

## Scope

- 路由：`/#/sql`；数据源 `codex-it-mariadb-11-4`。
- SQL 来源：`open-cdm-test/src/test/resources/split/mariadb/11.4.8/dcl_mariadb_administration_0.txt` 第一条分隔线之前的全部
  SQL，以及 MariaDB/MySQL 通用函数 fixture。
- 不覆盖：真实主从拓扑和生产备份。

## Preconditions

- MariaDB 11.4 隔离容器映射至 `127.0.0.1:3307`，可由测试人员重启。
- `codex_integration.orders` 存在；CloudDM 账号具备执行管理命令的权限。

## Test Data

- 编号：D01；数据说明：MariaDB 查询表；构造方式：建表并插入三行；唯一标识：`codex_integration.orders`；清理方式：删除 schema 或保留为专用 fixture
- 编号：D02；数据说明：复制、插件与线程占位目标；构造方式：使用 administration fixture 原值；唯一标识：fixture 固定值；清理方式：卸载成功安装的测试插件并恢复复制状态

### MARIADB-SMOKE-01 基础查询（P0）

- Chrome 操作：选择 `codex_integration`，执行 `SELECT VERSION();`、`SELECT 1;` 和订单表查询。
- 预期结果：版本包含 MariaDB；三条查询均有独立结果或明确错误。
- 恢复/清理：关闭新增结果页签。

### MARIADB-MAIN-01 管理命令全集（P0）

- Chrome 操作：逐条执行 `dcl_mariadb_administration_0.txt` 第一段全部 SQL。
- 覆盖命令：master/replica 状态与切换、`START/STOP/RESET REPLICA`、`CHANGE MASTER`、backup stage/lock、`KILL`、`ANALYZE`、
  `SET STATEMENT ... FOR`、插件与 SONAME 安装/卸载、`FLUSH`、统计信息，以及三种 shutdown。
- 预期结果：每条语句在执行信息中有记录；缺失拓扑、插件、线程或权限时返回具体错误，后续命令仍可继续。
- 恢复/清理：恢复锁、复制状态和插件状态。

### MARIADB-MAIN-02 函数兼容（P0）

- 数据准备：使用 MariaDB 11.4 官方支持且在本分支新增/调整的函数 SQL；共用语法从 MySQL 通用函数 fixture 复制。
- Chrome 操作：逐条执行 NULL、字符串、数值、日期、JSON 和聚合组合；错误后执行 `SELECT 1;`。
- 预期结果：支持的函数返回结果；MySQL 专有函数不得被错误标记为 MariaDB 成功；错误不污染下一次执行。
- 恢复/清理：无持久化对象。

### MARIADB-FAILURE-01 三种停机恢复（P0）

- Chrome 操作：分别执行 `SHUTDOWN WAIT FOR ALL SLAVES;`、`SHUTDOWN WAIT FOR ALL REPLICAS;`、
  `SET STATEMENT max_statement_time=10 FOR SHUTDOWN;`；每次停机后都重启容器并执行 `SELECT 1;`，再进入下一条。
- 预期结果：每条停机命令都实际触发连接终止；每次恢复后原数据源可继续查询。
- 恢复/清理：确认容器最终为运行状态。

### MARIADB-BOUNDARY-01 状态与权限（P1）

- Boundaries：覆盖空插件名、缺失 channel、无效线程号及函数 NULL/Unicode 输入。
- Repeat And Concurrency：重复执行 SHOW/FLUSH，只读结果不能串页签。
- Permission：只读账号执行管理命令显示权限错误。
- Lifecycle：页面刷新和数据源切换后重新执行 smoke SQL。

## Cleanup

1. 释放锁并恢复成功改变的复制、插件和全局参数状态。
2. 确认 MariaDB 容器运行，`SELECT 1;` 成功。
3. 删除仅供本流程使用的临时对象。

## Skip Conditions

- 没有可重启的隔离 MariaDB 实例时可以跳过 shutdown，但其余命令不可因此跳过。
- 无真实复制拓扑时仍提交复制命令，记录环境错误，不将其标为语法失败。
