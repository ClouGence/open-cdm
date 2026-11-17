# GreptimeDB Web 集成测试

## Purpose

验证 GreptimeDB 从实例配置到 SQL 工作台和表级权限申请的完整流程，覆盖读写边界、DDL 后对象树自动更新和写入回包丢失。

## Scope

实例管理、`/#/sql` 的 SQL 执行与对象树、团队账号的权限申请和 `/#/ticket` 的审批。回包丢失场景覆盖 GreptimeDB MySQL 协议链路。

## Preconditions

- 在 @Browser 打开运行当前源码的测试工作台（本地默认 `http://localhost:8222`）。使用能管理实例、执行 DDL 并审批请求的维护账号。
- 准备隔离的 GreptimeDB 1.3.0 实例，MySQL 协议端口默认 `4002`，初始库使用 `public`。准备一个能登录工作台、能申请数据源权限且没有测试实例授权的临时团队账号。测试环境须支持数据源权限工单审批。

## Test Data

每轮使用同一个唯一后缀创建数据源 `gt_ui_<suffix>`、库 `gt_ui_<suffix>` 和团队账号 `gt_user_<suffix>`。普通表 `metrics` 和 `denied` 使用 `ts TIMESTAMP(3) TIME INDEX`、`host STRING`、`reading DOUBLE` 和 `PRIMARY KEY (host)`。`append_log` 使用同样的列，并设置 `WITH ('append_mode'='true')`。视图 `metrics_view` 从 `metrics` 读取数据。

## Suites

### GT-WRITE-01 Smoke / DDL / State Consistency（P0）

1. 从「实例」添加 GreptimeDB 数据源，填写隔离实例的地址、端口和凭据。点击「测试连接」，确认出现「测试连接成功」，保存后实例列表新增一行。编辑该实例，将名称改为 `gt_ui_<suffix>_edited`，保存后确认列表只显示修改后的名称。
2. 在 `/#/sql` 用新名称找到并连接测试实例。右键实例打开「新查询窗口」，运行 `SELECT pkg_version FROM information_schema.build_info`，结果应为 `1.3.0`。执行建库语句，确认数据源树自动出现新库；双击新库打开查询页签。
3. 创建 `metrics`，确认表列表自动出现该表。右键选择「获取DDL语句」，确认定义包含 `TIME INDEX` 和 `PRIMARY KEY`。展开表及列节点，逐条执行增列、删列，确认不点刷新、保持展开时 `humidity` 出现及消失。再次执行同一删列语句，确认显示数据库错误且列列表没有变化。
4. 执行 `CREATE VIEW <库名>.metrics_view AS SELECT * FROM <库名>.metrics`。切换到视图列表，右键该视图选择「获取创建语句」，确认 DDL 包含视图名与源表。执行 `DROP VIEW`，确认视图自动消失。
5. 保留 `metrics` 和库供后续场景使用。清理时在表列表可见的状态下删除表，确认表自动消失；从实例查询页签删库，确认库从数据源树移除。

### GT-WRITE-02 Main Flow / Boundaries / Permission（P0）

1. 在 `/#/sql` 的测试库页签向 `metrics` 写入同一 `host` 和 `ts` 两次，第二次修改 `reading`。查询该键，确认只有一行且为第二次写入的值。执行多条语句时先选中全部待执行的 SQL，再点击「执行」。
2. 执行无 `WHERE` 的 `DELETE FROM <库名>.metrics`，确认工作台报错且行数不变。再执行匹配该键的条件删除，确认行数变为零。
3. 建立 `append_log`，写入并查询一行，再按该行时间和标签执行条件删除。确认数据库拒绝删除且行保留。
4. 保留 `append_log` 供权限验证，并准备同库的普通表 `denied`。

### GT-WRITE-03 Failure / Recovery（P0）

1. 在已连接的 `/#/sql` 测试库页签中，使用独立的故障注入连接。让代理转发写往 `append_log` 的一条带唯一 `host` 的 `INSERT`，收到 GreptimeDB 成功回包后丢弃该回包并断开这条连接。
2. 确认执行信息包含“写入结果未知”，执行进入终态。确认代理只收到一次该写入。还原故障配置并重新连接，独立查询确认该 `host` 只有一行。

### GT-WRITE-04 Permission / Approval（P0）

1. 用无授权的团队账号进入 `/#/sql`，确认测试实例不可见；从空状态的「申请数据源权限」进入权限申请页，展开测试实例和测试库，确认能选择具体表，当前资源显示完整库表路径。
2. 维护账号在「管理 → 账户」中给临时团队账号授予 `metrics` 的表级 DQL。团队账号重新登录后，确认对象树只列出已授权的表，`SELECT` 能读取 `metrics`；直接提交对 `denied` 的 `SELECT`、对 `metrics` 的 `INSERT` 和 `ALTER TABLE`，分别返回 `DM_QUERY`、`DM_DML`、`DM_DDL` 权限不足提示。
3. 团队账号在 `metrics` 的右键菜单点「申请权限」，确认权限申请页自动定位到该表。在申请页改选 `denied`，勾选「查询权限(DQL)」和「一天」。点击「提交申请」打开预览，再点击「确认申请」。在「工单 → 我发起的」确认新工单等待审批，工单详情列出申请人、测试表路径和只读权限。
4. 维护账号从「工单 → 待办」审批本轮工单，确认执行状态为「已完成」。团队账号重新登录后，确认 `denied` 出现在树上且查询有结果；未授权的 `append_log` 仍不可见且不能直接查询，向 `denied` 写入仍被拒绝。用维护账号确认越权写入和改表没有改变数据或列定义。

### GT-WRITE-05 SELECT 限行改写（P0）

1. 在隔离工作台的「偏好设置 → 数据查询」将「在线查询结果限制」的「最大记录数」设为 `3`，保持「禁用 SELECT 自动改写」关闭。在 `/#/sql` 的测试库页签准备五行时间戳不同的 `metrics` 数据和一张同结构的空表。
2. 执行 `SELECT * FROM metrics ORDER BY ts`。在结果页点「实际执行内容」，确认语句末尾有 `LIMIT 3`，并且只返回三行。
3. 依次给查询加上 `LIMIT 99 OFFSET 1`、`LIMIT 2 OFFSET 1`、`OFFSET 1` 和 `LIMIT ALL OFFSET 1`。确认实际执行的分页子句分别为 `LIMIT 3 OFFSET 1`、`LIMIT 2 OFFSET 1`、`OFFSET 1 LIMIT 3` 和 `LIMIT 3 OFFSET 1`，返回行数依次为三、二、三、三。
4. 执行 `SELECT * FROM (SELECT * FROM metrics LIMIT 99) q ORDER BY ts`，确认实际执行内容保留内层的 `LIMIT 99`，只在最外层加 `LIMIT 3`。执行 `INSERT INTO <目标表> SELECT * FROM metrics`，在「执行信息」核对原 SQL 及影响五行，查询目标表确认五行都已写入。
5. 打开「禁用 SELECT 自动改写」，重新执行步骤 2 的查询。确认实际执行的 SQL 保持原样，平台仍只收集三行。复测后恢复两项偏好设置并删除测试对象。

## Cleanup

用维护账号删除本轮视图、表和唯一测试库，移除测试数据源、权限及临时账号。关闭本轮页签和代理，保留工单及审计记录。

## Skip Conditions

工作台未启动或 @Browser 没有登录时，浏览器复测待补。缺少隔离团队账号时记录权限场景缺口；缺少可用审批流程时仍可验证申请入口及手动授权，工单审批与生效待补。没有可丢弃成功回包的代理时，可在 JDBC 层单独验证断线和未重发，工作台提示仍需复测。
