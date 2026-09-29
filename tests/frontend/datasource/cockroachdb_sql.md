# CockroachDB SQL 查询链路

## Purpose

验证 CockroachDB 独立 SQL 方言从编辑器解析、权限分析、执行到结果展示的常用路径，防止将 UPSERT 当作只读语句或忽略 SHOW 对象权限。

## Scope

- SQL 工作台 `/#/sql`，数据源树、schema 页签、Auto/Manual 模式、Run、结果集和执行信息。
- 查询、UPSERT、常用 DDL、SHOW、AS OF SYSTEM TIME、事务与保存点。
- 不覆盖 BACKUP/RESTORE、CHANGEFEED、多区域、集群管理、完整函数目录、血缘、网络故障注入或性能压测。

## Preconditions

- Alone 后端和 CockroachDB 插件已从当前源码构建并完成重启；健康检查正常。
- 已登录测试账号，具备隔离 CockroachDB 测试库的查询及写入权限。
- 使用仓库 compose CockroachDB 测试服务或明确指定的非生产环境；连接参数从环境配置获取，文档不记录凭据。
- 核对数据库实际版本和 PostgreSQL 兼容版本。基准为 CockroachDB 24.3，兼容版本 13。
- 无未提交的用户事务；不能在其他数据源或用户已有页签执行测试写入。

## Test Data

使用本轮唯一后缀创建 `crdb_ui_<suffix>` 表，字段 `id INT PRIMARY KEY, value STRING, amount DECIMAL(12,2), payload JSONB`。所有写入限于该表。保存点名使用 `user_checkpoint`。特殊值含中文、单引号、JSON、精确小数；重复主键用于错误恢复。

## Suites

### CRDB-SQL-SMOKE（P0）

- 初始状态：SQL 工作台，过滤目标 CockroachDB 数据源。
- 操作：展开数据库和 schema，打开专属页签，核对地址、数据库及 schema；运行 `SELECT current_database(), current_schema(), version()`。
- 预期：目标地址正确；三列有值，version 为 CockroachDB；结束后 Run 恢复可用。
- 清理：保留页签供后续测试。

### CRDB-SQL-MAIN（P0）

- 初始状态：目标页签 Auto 模式；准备上述唯一表。
- 操作：CREATE TABLE；UPSERT VALUES 两行，再 UPSERT 更新其中一行并 RETURNING；执行 SELECT、CTE、ORDER BY/LIMIT；执行 SHOW COLUMNS、SHOW CREATE TABLE、SHOW TABLES FROM database.schema；执行带 AS OF SYSTEM TIME 的 SELECT。
- 预期：每条语句有完成状态；UPSERT 更新原行而非重复插入；列数、JSON、小数和 Unicode 值正确；SHOW 返回实际字段与 DDL。快照时间必须晚于建表时间。
- 清理：表保留用于事务验证；不要将批量中首条成功视作全批成功。

### CRDB-SQL-TX-RECOVERY（P0）

- 初始状态：目标页签切至 Manual，使用已有测试表。
- 操作：写入 id=101；SAVEPOINT user_checkpoint；写入 id=102；执行重复主键写入产生数据库错误；再 ROLLBACK TO SAVEPOINT user_checkpoint；RELEASE SAVEPOINT user_checkpoint；点击提交。
- 预期：错误有可见终态；用户保存点仍可用；独立连接确认提交后仅 id=101 存在，id=102 不存在。
- 补充：新 Manual 会话首条 SET TRANSACTION 和 Auto 事务语句拦截分别核对，不能相互替代。原生 CockroachDB 当前快照可能等待未提交写入，独立连接观察应设查询超时。
- 清理：结束事务，恢复 Auto。

### CRDB-SQL-COMMIT-FAILURE（P0）

- 初始状态：Manual 模式，隔离测试表已创建。仅在支持该测试变量的 CockroachDB 测试环境执行。
- 操作：设置 `SET inject_retry_errors_on_commit_enabled=on`；向测试表写入一条唯一行；点击提交。
- 预期：页面明确显示 `restart transaction`；Run 恢复可用；独立连接确认该行未提交。不能只看到“递交事务”就视为成功。
- 恢复：点击回滚，设置 `SET inject_retry_errors_on_commit_enabled=off`，查询既有行仍成功。最终恢复 Auto。

### CRDB-SQL-DCL（P1）

- 初始状态：Auto，隔离表和唯一无登录角色名。
- 操作：CREATE ROLE；GRANT SELECT ON TABLE 给该角色；REVOKE SELECT。
- 预期：三条都有执行完成状态；授权仅限隔离表。这不替代 CloudDM 低权限账号的页面验收。
- 清理：原生连接 DROP ROLE，仅删除本轮角色。

### CRDB-SQL-LIFECYCLE（P1）

- 初始状态：已提交且结果可见。
- 操作：刷新页面，重新进入目标 schema 并查询测试表；刷新对象列表。
- 预期：仍可连接，结果与独立连接一致，无遗留执行中状态。仅有静态权限分析测试时，不宣称完成低权限账号的页面验收。
- 清理：关闭本流程创建的页签；通过隔离数据库的原生连接删除本轮测试表，不触及其他对象。仅为本轮创建的数据源按环境约定清理或明确保留用于复测。

## Cleanup

回滚未完成事务；原生连接按唯一名称删除测试表和临时角色。保留测试服务或数据源时须向使用者明确说明。不要删除、重置其他数据源或测试工作。

## Skip Conditions

- 后端、驱动或测试数据库不可用：记录具体阻塞，不能将静态测试代替产品验证。
- 低权限测试账号未提供：记录权限页面验收缺口；保留自动化行为/RuleDomain/系统函数边界测试。
- 本轮没有前端代码变更，布局极限、移动端和文件上传不适用；不将这些项目列为已通过。
