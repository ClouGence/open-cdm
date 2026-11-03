# SQL Server 查询补全

## Purpose

验证 SQL Server 查询编辑器中的表名和字段补全，防止只显示表名或关键字、无法提示真实字段；关联 Issue #225。

## Scope

- 路由：`/#/sql`；SQL Server 数据源的库、Schema 查询窗口。
- 能力：FROM 表名、WHERE 字段、别名限定字段和 SELECT 列表补全。
- 不覆盖：SQL Server 全部版本兼容、业务写入、数据库原生 IntelliSense 和权限规则变更；仅视图夹具涉及可清理的 DDL。

## Preconditions

- 本地 CloudDM 已启动，@Browser 使用现有登录状态；测试期间不修改数据源配置或授权。
- 使用已确认的 SQL Server 测试实例及现有数据库，不操作生产实例。视图场景只允许创建本次命名且仅含常量的测试视图，并在结束后删除。
- 通过只读 `SERVERPROPERTY('ProductVersion')` / `SERVERPROPERTY('Edition')` 查询记录真实版本，不能以驱动版本替代数据库版本。
- 先从实例列表确认连接，再进入数据查询，展开对应环境、实例、库和 Schema。

## Test Data

- 选择一个现有测试表，至少包含一个标识列及三个具有相同名称前缀的普通字段。
- 示例：`pika_test.dbo.demo_table`，字段 `Id`、`Col1`、`Col2`、`Col3`；执行前核对实例中确实存在，不依赖示例名称必然可用。
- 用只读 `INFORMATION_SCHEMA.COLUMNS` 查询核对字段名称；只取目录字段，不读取业务数据。
- 新开测试查询窗口，保留原有其他查询窗口和 SQL 草稿；补全用的不完整 SQL 不执行。
- 视图夹具使用唯一名称 `codex_issue225_v_<suffix>`，定义为 `SELECT CAST(1 AS int) AS view_id, CAST(N'probe' AS nvarchar(20)) AS view_note`，不引用业务表。

## Suites

### SSC-SMOKE-01 表名补全

- 风险/目的：P0，区分补全服务整体不可用与仅字段元数据不可用。
- 初始状态：已打开测试库的 Schema 查询窗口，左侧可见测试表。
- 操作：输入 `SELECT * FROM de`（按实际表名前缀调整），等待自动建议或按 Control+Space 手动触发。
- 预期：建议列表出现真实测试表并标为“表”；不能把仅有关键字建议当成表名补全成功。
- 清理：Esc 收起列表，清空本测试窗口中的 SQL。

### SSC-MAIN-01 普通字段和别名字段

- 风险/目的：P0，验证 #225 的真实风险路径。
- 初始状态：沿用 SSC-SMOKE-01；测试表真实字段已通过目录查询核对。
- 操作：分别输入 `SELECT * FROM dbo.demo_table WHERE Co` 和 `SELECT * FROM dbo.demo_table AS d WHERE d.Co`，在末尾触发补全。再输入 `SELECT Co FROM dbo.demo_table`，把光标放在 `Co` 后触发补全；另测 `SELECT I FROM dbo.demo_table` 的 `I` 后补全。
- 预期：列表包含真实字段 `Col1/Col2/Col3`；SELECT 的 `Co` 前缀包含同样字段，`I` 前缀包含 `Id`；空前缀位置沿用现有优先提示 `*` 的策略。接受建议后插入正确字段名称或别名限定名，不执行不完整 SQL。
- 诊断：若只出现 `coalesce/collate/column` 等关键字，或完全没有字段，检查本地 `alone.log` 同时段的语言服务堆栈。`ConsoleMetaServiceImpl.fetchTableColumns → realTimeFetchSelectObject → AbstractRdbUmiService.fetchSelectObject` 抛出 `UnsupportedOperationException` 表示字段元数据接口缺失，不能据此认定数据库权限不足。
- 清理：Esc 收起建议，清空本测试窗口；不审批、不保存 SQL 收藏，不执行 DDL/DML。

### SSC-MAIN-02 视图字段补全

- 风险/目的：P1，确认查找表无结果时能读取视图元数据，而不是返回空候选或异常。
- 初始状态：已确认隔离的测试库/Schema，当前测试账号具备创建/删除自身测试视图的权限。
- 准备：检查唯一视图名不存在，执行 `CREATE VIEW dbo.[codex_issue225_v_<suffix>] AS SELECT CAST(1 AS int) AS view_id, CAST(N'probe' AS nvarchar(20)) AS view_note;`，确认执行成功。
- 操作：输入 `SELECT * FROM dbo.[codex_issue225_v_<suffix>] WHERE view_`，在末尾触发补全。
- 预期：列表包含 `view_id`、`view_note`，标记为列；无 `UnsupportedOperationException`，接受后插入正确名称。
- 清理：只执行 `DROP VIEW dbo.[codex_issue225_v_<suffix>]` 删除本次创建的常量视图，确认执行成功；不修改原有表或视图。

### SSC-LIFECYCLE-01 重复触发与重新进入

- 风险/目的：P1，排除一次性补全缓存或旧响应干扰。
- 初始状态：SSC-MAIN-01 已操作。
- 操作：收起后重新触发普通字段和别名字段补全；关闭本次新建且已清空的查询窗口，再从同一 Schema 重新打开并重复。
- 预期：重复触发结果一致，库/Schema 上下文保持正确，不混入其他页签字段。
- 清理：只关闭本次创建的测试窗口，不修改或关闭原有草稿。

## Cleanup

- 清空并关闭本次新建查询窗口，保留已有查询草稿和数据源配置。
- 清理本次创建的常量视图；不修改原有数据库对象、业务数据、账号、权限或服务配置。

## Skip Conditions

- 实例不可达或 @Browser 未登录时，报告阻塞，不以源代码阅读代替页面复现。
- 没有足够数据库/CloudDM 读取权限时单独记录权限阻塞；不能将其等同于 #225。
- 未提供 SQL Server 2012 等其他版本环境时，明确列出未验证版本，不用单个版本代表全部版本。
- 没有网络帧观测能力时，使用可见候选、只读元数据查询和对应时间的本地后台堆栈核实，不输出认证数据或完整 WebSocket 地址。
