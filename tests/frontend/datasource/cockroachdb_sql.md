# CockroachDB SQL 查询链路

## Purpose

验证 CockroachDB 独立 SQL 方言从编辑器解析、权限分析、执行到结果展示的常用路径，防止将 UPSERT 当作只读语句或忽略 SHOW 对象权限。

## Scope

- SQL 工作台 `/#/sql`，数据源树、schema 页签、Auto/Manual 模式、Run、结果集和执行信息。
- 查询、UPSERT、常用 DDL、SHOW、AS OF SYSTEM TIME、事务与保存点，以及 CockroachDB DCL。
- 不覆盖 BACKUP/RESTORE、CHANGEFEED、多区域、集群管理、完整函数目录、血缘、网络故障注入或性能压测。

## Preconditions

- Alone 后端和 CockroachDB 插件已从当前源码构建并完成重启；健康检查正常。
- 已登录测试账号，具备隔离 CockroachDB 测试库的查询及写入权限。
- 使用仓库 compose CockroachDB 测试服务或明确指定的非生产环境；连接参数从环境配置获取，文档不记录凭据。
- 核对数据库实际版本和 PostgreSQL 兼容版本。基准为 CockroachDB 24.3，兼容版本 13。
- 无未提交的用户事务；不能在其他数据源或用户已有页签执行测试写入。

## Test Data

使用本轮唯一后缀创建 `crdb_ui_<suffix>` 表，字段 `id INT PRIMARY KEY, value STRING, amount DECIMAL(12,2), payload JSONB`。常用基线写入限于该表；组合套件使用完整的唯一隔离库。保存点名使用 `user_checkpoint`。特殊值含中文、单引号、JSON、精确小数；重复主键用于错误恢复。

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

- 初始状态：Auto，已核对地址和版本的独立测试数据库；数据库账号有建角色与授权权限。
- 数据准备：唯一无登录角色 `crdb_ui_role_<suffix>`、唯一表；仅向本轮无登录角色授权，不改变既有账号权限。
- 操作：逐条执行 CREATE ROLE IF NOT EXISTS ... WITH NOLOGIN；普通 GRANT SELECT ON TABLE；
  GRANT SYSTEM VIEWACTIVITY；限定 schema 的 GRANT USAGE；角色成员授权及撤销 ADMIN OPTION；
  ALTER DEFAULT PRIVILEGES FOR ALL ROLES IN SCHEMA public GRANT SELECT ON TABLES；创建第二张表。
- 预期：每条均有明确完成终态，Run 恢复可用；独立连接 SHOW GRANTS 确认第二张表继承 SELECT。
  函数签名和 EXTERNAL CONNECTION 授权需先在独立环境准备对应对象；不能用“对象不存在”的原生错误当作授权成功。
- 反向操作：按原范围 REVOKE 默认权限、系统权限和对象权限；ALTER ROLE IF EXISTS ... NOLOGIN；
  使用唯一临时用户名验证 CREATE USER IF NOT EXISTS / DROP USER IF EXISTS。
- 失败路径：运行 `REVOKE GRANT OPTION FOR USAGE ON ALL SEQUENCES IN SCHEMA public FROM <role>`，
  24.3 应出现解析拒绝并恢复可执行；随后 SELECT 1 仍成功。该形式的原生 SQLSTATE 为 42601。
- 权限路径：无管理权限的 CloudDM 测试账号执行同类 DCL 应在产品权限检查中拒绝；没有该账号时明确记录未验收，
  不以数据库账号权限或静态 RuleDomain 代替。
- 清理：原生连接撤回本轮默认权限、删除唯一测试库和外部连接后，DROP 本轮临时用户/角色；不得影响其他对象。

### CRDB-SQL-COMBINATIONS（P1）

- 目的：核对官方语法组合从解析、权限分析、自动分页、下发到返回值的完整链路。
- 初始状态：`/#/sql`，已核对 CockroachDB 版本/地址，Auto，无用户事务；使用唯一的隔离数据库。
- 数据准备：运行相邻测试工程的 `scripts/generate_cockroach_live_cases.py --check`，读取
  `src/test/resources/config/cockroachdb-live-combinations.json`；将 `{{db}}`、`{{reader}}`、
  `{{member}}`、`{{user}}` 替换为本轮唯一名称，创建的角色均为 NOLOGIN。
  首先从已有测试库执行 CREATE DATABASE IF NOT EXISTS；右键数据源刷新后打开新库 public，
  SELECT current_database()/current_schema()/version() 必须显示新库、public 和目标版本。
- 操作：按 `steps` 顺序执行，同一批最多 8 条；将 expectedSqlState/expectedNativeError 语句独立成批。
  复用同一个编辑器；粘贴后再次全选再点击执行，防止只执行光标所在语句。
  等 Run 恢复可用，逐条核对执行 SQL、完成信息和结果 tab，不能只数点击或 Done。
- 预期：每条正向语句完成；结果按 expectedRows 或同清单的原生记录逐行逐列核对。
  布尔展示 True/False 与原生 t/f、小数显示精度差异可规范化比较；不能忽略值差异。
  JSON/数组/Unicode/引号/分号、quoted Target、默认权限继承及实际 owner 必须核对。
- 分页：GROUP BY/HAVING/ORDER BY 查询应以 ORDER BY ... LIMIT 结束；不得出现 LIMIT ... ORDER BY。
  同时核对 CTE/window 和显式 LIMIT 1 OFFSET 1，返回行必须与预期一致。
- 失败恢复：视图改变返回列类型、原生不支持的 ALTER PROCEDURE SECURITY、重复主键应有可见原生错误；
  紧接的下一批和 SELECT 恢复完成。产品解析拒绝不能冒充预期原生错误。
- 特有探测：逐条运行 `officialSyntaxProbes`，与原生执行分开记录。
  当前八项 productExpected=success 均应完成，不能再把原有六项解析拒绝视作可接受结果。
- 生命周期：关闭/重开遇到 session pool 上限时记录独立缺陷，确认失败语句未下发后再恢复；
  不能静默跳过或将其归因于语法。未完成的语句从对应 ID 继续，避免重复执行非幂等 DDL。
- 清理：关闭本轮库的查询会话，在准备页签执行 DROP DATABASE IF EXISTS ... CASCADE，再删除本轮角色。
  独立连接确认主库/辅助库/改名库和角色计数为 0；关闭本轮页签，保留原有用户页签。

### CRDB-SQL-INDEX-MUTATION（P1）

- 目的：固定六项解析缺口的对象范围、真实写入及返回值，避免查询分页影响 UPDATE/DELETE。
- 初始状态：同一隔离库 public、Auto、管理员测试账号；版本/地址已核对。
- 数据准备：读取同一 `cockroachdb-live-combinations.json` 的 `sixFeatureSteps`，无需角色或外部对象。
- 操作：按 ID 顺序分批全选执行；哈希列级/表级主键和独立索引、单/多 STORING、JSON/数组倒排索引；
  按清单重置数据后运行排序限量 UPDATE/DELETE，并核对 RETURNING 和操作后 SELECT。
  分别执行 @primary、具名索引、FORCE_INDEX、NO_INDEX_JOIN 和方向选项，再组合 CTE/目标索引提示/DML LIMIT。
- 预期：pg_indexes 中五个具名测试索引可见；哈希表插入/读取一致；每轮 DML 只修改或删除 LIMIT 指定的记录，
  ASC/DESC 确实选择不同记录；每条 SQL 及结果值与清单/原生证据一致。
- 失败恢复：FORCE_INDEX 与 NO_INDEX_JOIN 同时出现时应返回原生语法错误，随后三条 CTE/写入/查询继续完成。
  不能把这种原生约束改为成功预期，也不能把产品解析拒绝当作原生错误。
- 生命周期：刷新后在同一测试库执行 FORCE_INDEX 查询；最终 `(id,amount)` 与刷新前一致，Run 恢复可用。
- 清理：离开测试库页签，在准备库 DROP 本轮隔离库 CASCADE；独立连接计数为 0，保留原有用户页签。

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
