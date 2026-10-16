# HANA 查询血缘与脱敏

## Purpose

验证结果列改名、表达式、跨表组合和嵌套查询后仍按真实来源脱敏；部分来源获豁免不能解除其他来源的保护。未知 SELECT 来源应明确失败。

## Scope

- `/#/sql` 的查询结果、血缘入口及查询结果导出；表数据读取作为 JDBC 元数据路径的对照。
- 对应 `HanaLineageAnalysisSpi`、`HanaMetaProviderDm.loadSelectObject`、`QueryAnalysisServiceImpl` 和 `SecValueProcessServiceProvider`。
- 视图追踪到视图列，规则需覆盖视图列；不展开视图定义、程序内部 SQL、SQLScript 变量或同义词。CALL/DO 结果仍走既有 JDBC 路径，不能当作查询级血缘验收通过。
- 对象 READ/PROGRAM 授权沿用 [查询授权流程](hana_query_authorization.md)。

## Preconditions

- 本地 CloudDM 可达、@Browser 已登录；部署本步 console 运行包与第 9 步完整 HANA 插件包（包含新 `sql-hana`、`sqlc-common`）并重启加载。真实 Express tenant 可访问。
- 使用隔离数据源、一次性 schema 和规范，不调整生产规则。数据库账号能读取样本对象。
- 准备平台普通 READ 用户、仅一列获敏感数据豁免的用户、全部目标列获豁免的用户；普通用户不能是 root。准备有规则配置权限的管理员，按当前菜单配置 HANA 脱敏规范及实例关联。
- 为两表 SECRET、T 的 `"foo"`/FOO、视图 V 的 MASKED_VALUE 配置全掩码规则；保留 PUBLIC_VALUE 不命中规则。先核对规则范围的 catalog/schema/table/view/column 与实际名称。

## Test Data

使用唯一后缀创建 `H9_A_<run>`、`H9_B_<run>`，以下 A/B 替换为实际 schema。由 OWNER 在原生客户端逐条准备，使用合成值：

```sql
CREATE SCHEMA H9_A_<run>;
CREATE SCHEMA H9_B_<run>;
CREATE COLUMN TABLE A.T (ID INTEGER PRIMARY KEY, SECRET NVARCHAR(80), PUBLIC_VALUE NVARCHAR(80), "foo" NVARCHAR(80), FOO NVARCHAR(80));
CREATE COLUMN TABLE B.S (ID INTEGER PRIMARY KEY, SECRET NVARCHAR(80));
INSERT INTO A.T VALUES (1, 'synthetic-a', 'public-a', 'lower-secret', 'upper-secret');
INSERT INTO B.S VALUES (1, 'synthetic-b');
CREATE VIEW A.V AS SELECT ID, SECRET AS MASKED_VALUE FROM A.T;
```

长期查询样本见 [09-lineage.sql](../../datasource/hana/sql/09-lineage.sql)。需要 NULL/Unicode 和分页时，在同一隔离表补充合成值，数量以当前页面实际分页大小为准；不要使用真实敏感数据。清理前记录本次创建的对象与规则标识。

## Suites

### H9-SMOKE-01 来源与标准脱敏

- 目的/优先级：P0，规则不能因输出列名变化失效。
- 初始状态：`/#/sql`，普通 READ 用户，选择测试数据源及 A schema；规则已生效。
- 数据与准备：上述 T/S/V 及列范围全掩码规则。
- @Browser 操作：逐条执行样本的别名、UPPER、JOIN、星号、CTE、派生表、标量子查询、UNION 和视图查询；有血缘入口时打开核对来源。
- 预期：SECRET 来源的值均不显示合成原文；PUBLIC_VALUE、常量不误用 SECRET 规则；CTE/派生表显示物理来源，UNION 包含两表来源，V 指向视图列。输出列顺序和行数与原生客户端一致。
- 恢复：关闭结果与血缘视图，保持普通用户。

### H9-PERMISSION-01 精确和部分豁免

- 目的/优先级：P0，多来源表达式不能因一列豁免完全放行。
- 初始状态：`/#/sql`，独立豁免用户，已有两表 READ。
- 数据与准备：仅授予 A.T.SECRET 敏感数据豁免，B.S.SECRET 仍需脱敏。
- @Browser 操作：执行 T 单列、S 单列、JOIN 拼接、UNION；再仅豁免 T 的 `"foo"`，执行大小写列查询；最后用全部来源获豁免用户做对照。
- 预期：T 单列显示原文，S、JOIN、UNION 敏感结果仍掩码；`"foo"` 豁免不影响 FOO；全来源豁免后对应值可见。规则不是按结果别名匹配。
- 恢复：移除本流程临时豁免，重新提交验证掩码恢复，不用旧结果判定新权限。

### H9-EXPORT-01 导出和表数据读取

- 目的/优先级：P0，页面与下载不能产生不同的保护结果。
- 初始状态：普通 READ 用户查询结果已产生，有测试导出权限；数据编辑入口可读。
- 数据与准备：别名、CTE、UNION 查询及 T 表，只有合成数据。
- @Browser 操作：使用当前查询结果导出入口下载结果；进入 T 的表数据读取页面，核对敏感列；再用豁免用户对照。工具支持读取下载时检查实际文件内容。
- 预期：查询结果导出中的敏感值仍掩码；表数据读取以 JDBC 物理列来源命中规则；普通用户不能通过下载/编辑读取原文。若下载不可检查，则文件内容子项保持未验证。
- 恢复：移除本次下载文件、关闭数据读取页；不修改表数据。

### H9-FAILURE-01 未知来源与恢复

- 目的/优先级：P0，空元数据/不支持来源不能被当成已知常量。
- 初始状态：`/#/sql`，普通用户，正常查询已成功。
- 数据与准备：样本负例；缺失对象、列；无显式别名的来源表达式。元数据故障仅在隔离服务可安全模拟时执行。
- @Browser 操作：逐条提交负例和 `SELECT * FROM A.MISSING_TABLE`；随后重新提交正常 SELECT。
- 预期：负例出现分析或对象授权错误，没有成功结果或可导出的新原文；正常查询恢复。缺失元数据不能产生“零列但执行成功”的结果。
- 恢复：清除错误 SQL，恢复模拟故障和测试权限。

### H9-STATE-01 边界、重复和生命周期

- 目的/优先级：P1，页签/账号/上下文切换不能复用错误来源或豁免。
- 初始状态：两个 HANA 查询页签，各指向 A/B；普通用户。
- 数据与准备：NULL、Unicode、空结果、单行及超过一页的合成数据；不增加解析器未声明的长度限制。
- @Browser 操作：重复执行只读查询、切换 schema、刷新并重新进入；分别核对正常/豁免用户的分页和导出。在可安全模拟网络失败时重新执行只读查询。
- 预期：每次列来源/掩码对应当前上下文；空结果不报血缘缺失；NULL 和非敏感列遵循所选规则行为；分页/重复导出不出现未脱敏原文；失败后无假成功。
- 恢复：恢复网络，关闭本次页签，删除补充合成行。

极窄/极宽视口沿用 SQL 工作台既有流程；本步无新布局或可写交互，不开展并发写入/大规模压力测试。

## Cleanup

移除本次规范关联、列规则和豁免；OWNER 按 V、T/S、空 schema 的顺序删除本次唯一名称对象。关闭测试页签并删除合成导出文件，恢复用户/网络；不保留凭据。

## Skip Conditions

- 页面不可达或缺少真实 HANA：所有产品场景 BLOCKED；离线解析/脱敏执行器探针只作补充。
- @Browser 未登录：暂停页面操作，等待用户登录，不改用其他浏览器。
- 缺少规则/豁免用户、导出权限或下载检查能力：对应子项 SKIP，不能用 root 成功代替。
- 无法安全模拟网络/元数据失败：模拟故障子项 SKIP，仍运行正常恢复与未知来源负例。
