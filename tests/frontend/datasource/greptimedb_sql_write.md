# GreptimeDB SQL 工作台写入与 DDL

## Purpose

验证写入权限、DDL 后对象浏览自动更新，以及数据库执行成功但工作台未收到回包时的处理。

## Scope

`/#/sql` 的命令模板、SQL 执行信息、库列表、表与视图列表及列详情。回包丢失场景覆盖 GreptimeDB MySQL 协议链路。

## Preconditions

- 在 @Browser 登录运行当前源码的测试工作台，配置隔离的 GreptimeDB 1.3.0 测试实例。
- 准备有测试库写入和 DDL 权限的维护账号，以及仅有该库查询权限的团队账号。

## Test Data

每轮用唯一库名 `gt_ui_<suffix>` 替换模板中的 `demo`。普通表 `metrics` 使用 `ts TIMESTAMP(3) TIME INDEX`、`host STRING`、`reading DOUBLE` 和 `PRIMARY KEY (host)`。`append_log` 使用同样的列，并设置 `WITH ('append_mode'='true')`。测试视图 `metrics_view` 从 `metrics` 读取数据。

## Suites

### GT-WRITE-01 Smoke / DDL / State Consistency（P0）

1. 在 `/#/sql` 右键测试实例打开控制台，从命令模板执行替换过库名的建库语句。确认数据源树出现新库，在新库中打开查询页签。
2. 用模板创建 `metrics`，确认表列表自动出现该表。展开表及列节点，执行模板中的增列、删列，逐条确认 `humidity` 出现及消失。再次执行同一删列语句，确认显示数据库错误且列列表没有变化。
3. 执行 `CREATE VIEW <库名>.metrics_view AS SELECT * FROM <库名>.metrics`。切换到视图列表，确认视图出现。执行删除视图模板，确认视图自动消失。
4. 保留表和库给后续场景。后续场景结束后在表列表可见时删除表，确认表自动消失；从实例控制台删库，确认库从数据源树及库选择器移除。

### GT-WRITE-02 Main Flow / Boundaries / Permission（P0）

1. 在 `/#/sql` 的测试库页签用模板向 `metrics` 写入同一 `host` 和 `ts` 两次，第二次修改 `reading`。查询该键，确认只有一行且为第二次写入的值。
2. 执行无 `WHERE` 的 `DELETE FROM <库名>.metrics`，确认工作台报错且行数不变。再执行匹配该键的条件删除，确认行数变为零。
3. 建立 `append_log`，写入并查询一行，再按该行时间和标签执行条件删除。确认数据库拒绝删除且行保留。
4. 用只读团队账号重新打开测试库页签。确认查询成功，写入及 DDL 被工作台拒绝。用维护账号核对实际数据与对象未变。

### GT-WRITE-03 Failure / Recovery（P0）

1. 在已连接的 `/#/sql` 测试库页签中，使用独立的故障注入连接。让代理转发写往 `append_log` 的一条带唯一 `host` 的 `INSERT`，收到 GreptimeDB 成功回包后丢弃该回包并断开这条连接。
2. 确认执行信息包含“写入结果未知”，执行进入终态。确认代理只收到一次该写入。还原故障配置并重新连接，独立查询确认该 `host` 只有一行。

## Cleanup

关闭本轮页签与代理，通过独立连接删除本轮视图、表和唯一测试库，并移除仅为本轮创建的数据源与授权。

## Skip Conditions

工作台未启动或 @Browser 没有登录时，浏览器复测待补。缺少只读团队账号时记录权限场景缺口。没有可丢弃成功回包的代理时，可在 JDBC 层单独验证断线和未重发，工作台提示仍需复测。
