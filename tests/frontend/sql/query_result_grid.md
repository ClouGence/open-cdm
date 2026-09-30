# SQL 查询结果表格

## Purpose

验证 SQL 工作台查询结果表格在有数据、单行和空结果情况下均可完整浏览所有列，确保已完成结果集切换时不再出现加载状态，确认执行信息长日志只在消息区内滚动、右侧操作栏始终可见，验证编辑器字号偏好可以在前端持久化，以及
ClickHouse LowCardinality 字符串和空值可以正确显示。

## Scope

- 页面与路由：`/#/sql`。
- 关联功能：SQL 执行、结果页签、结果加载状态、宽表横向滚动、固定序号列、空结果布局、执行信息长日志滚动和编辑器字号偏好。
- 关联源码：`frontend/src/views/sql/components/Result.vue`、`frontend/src/views/sql/components/SqlViewer.vue`、
  `frontend/src/components/editor/index.vue`、
  `backend/clouddm-plugins/clouddm-ds/ds-clickhouse/src/main/java/com/clougence/clouddm/ds/clickhouse/execute/ChColReader.java`。
- 不覆盖：数据编辑、导出文件内容、数据库写入和超大结果集性能。

## Preconditions

- 本地 `DmAloneLauncher` 已通过 IDEA 启动，访问地址为 `http://127.0.0.1:8222/`。
- 验证前端源码改动时，使用 `npm run serve:dm` 输出的 Local URL，并确认其 API 代理仍指向上述后端。
- @Browser 已登录具备 SQL 查询权限的测试账号。
- 已连接一个允许执行只读 `SELECT` 的 MySQL 测试数据源。
- ClickHouse 类型场景另需本地 ClickHouse 测试数据源，记录实际数据库和驱动版本；按本机 `localdb` 文档启动服务，先确认数据库列表和普通查询可用。

## Test Data

| 编号    | 数据说明              | 构造方式                                                                                         | 清理方式                 |
|-------|-------------------|----------------------------------------------------------------------------------------------|----------------------|
| QRG01 | 空结果宽表             | 执行包含至少 12 个别名列且条件为 `WHERE 1=0` 的只读 `SELECT`                                                  | 无数据库写入，关闭结果页签即可      |
| QRG02 | 单行宽表              | 执行包含至少 12 个别名列且返回一行的只读 `SELECT`                                                              | 无数据库写入，关闭结果页签即可      |
| QRG03 | 窄结果               | 执行 `SELECT 1 AS value`                                                                       | 无数据库写入，关闭结果页签即可      |
| QRG04 | 120 行只读执行日志       | 粘贴并执行以 `SELECT 1 AS log_line` 开头、后续每行依次为 `UNION ALL SELECT 2` 至 `UNION ALL SELECT 120` 的单条查询 | 无数据库写入，关闭结果页签即可      |
| QRG05 | 编辑器字号偏好           | 依次选择“小”和“大”字号                                                                                | 测试结束后恢复常用字号          |
| QRG06 | ClickHouse 低基数字符串 | 在带本次唯一标识的隔离测试库创建 `string_types` 表，使用下述三行数据                                                   | 只清理本流程的隔离库表；保留必要复现证据 |

QRG06 由数据库客户端准备；将 `codex_qrg_<runid>` 替换为本次唯一测试库名，不复用已有业务库表：

```sql
CREATE DATABASE codex_qrg_<runid>;
CREATE TABLE codex_qrg_<runid>.string_types (
    id UInt32,
    plain String,
    low_cardinality LowCardinality(String),
    nullable_low LowCardinality(Nullable(String))
) ENGINE = MergeTree ORDER BY id;
INSERT INTO codex_qrg_<runid>.string_types VALUES
    (1, 'hello', 'hello', 'hello'),
    (2, '中文', '中文', NULL),
    (3, '', '', '');
```

## Suites

### QRG-SMOKE-01 空结果宽表可横向浏览

- 风险/目的：P0，确认返回 0 行时表格仍保留横向滚动区域。
- 初始路由与状态：进入 `/#/sql`，选择可用的 MySQL 查询页签，结果区无执行中的任务。
- 测试数据：QRG01。
- @Browser 操作：执行只读空结果宽表 SQL；在结果表格空白区域使用触控板横向滚动或拖动底部横向滚动条，从最左侧移动到最右侧后再返回。
- 预期结果：结果显示“共 0 条”；表头和结果 body 占满剩余高度；存在可操作的横向滚动区域；横向移动时表头同步滚动，右侧最后一列可见；固定序号列保持在左侧；页面本身不产生额外横向滚动。
- 恢复/清理：关闭新增结果页签。

### QRG-MAIN-01 有数据宽表行为不回归

- 风险/目的：P0，确认空结果修复不会改变已有数据表格的滚动和固定列行为。
- 初始路由与状态：同 QRG-SMOKE-01。
- 测试数据：QRG02。
- @Browser 操作：执行单行宽表 SQL；将结果横向滚动到最右侧并返回；点击该行空白处。
- 预期结果：数据行与表头同步横向移动；序号列保持固定；滚动条位于数据区底部；行点击行为和列宽保持正常。
- 恢复/清理：关闭新增结果页签。

### QRG-BOUNDARY-01 不溢出的窄结果

- 风险/目的：P1，确认窄结果不会显示无意义的横向滚动条。
- 初始路由与状态：同 QRG-SMOKE-01。
- 测试数据：QRG03。
- @Browser 操作：执行窄结果 SQL，观察结果表格底部并尝试横向滚动。
- 预期结果：所有列直接完整可见；没有可移动的横向滚动距离；结果区高度和边框与宽表一致。
- 恢复/清理：关闭新增结果页签。

### QRG-EXTREME-01 视口与缩放

- 风险/目的：P1，确认宽表在常用桌面、平板和浏览器缩放场景下保持可浏览。
- 初始路由与状态：空结果宽表结果页签处于选中状态。
- 测试数据：QRG01。
- @Browser 操作：分别在 1440×900、1024×768 视口和 150% 浏览器缩放下，将结果横向滚动到首尾。
- 预期结果：空结果的横向滚动区域始终可操作；右侧列可达；表格不撑开工作台和浏览器页面；结果区工具栏、页签与滚动区域不重叠。
- 恢复/清理：恢复原视口与缩放，关闭新增结果页签。

### QRG-EXTREME-02 长执行日志与固定操作栏

- 风险/目的：P0，防止执行信息内容增长后把右侧滚到顶部、滚到底部和清空操作一起带离可视区域。
- 初始路由与状态：进入 `/#/sql`，选择允许只读查询的数据源和 schema，执行信息区域已展开且具有可用高度。
- 测试数据：QRG04。
- @Browser 操作：执行 QRG04 后切换到“执行信息”；分别在 1440×900 与 1024×768 视口将左侧日志从底部滚到中间和顶部；观察右侧三个操作；依次点击滚到顶部和滚到底部；在
  390×844 视口收起全局数据源侧栏后，再滚动左侧日志。
-
预期结果：外层执行信息区域本身不滚动，只有左侧消息区出现纵向滚动；任意日志位置下右侧三个操作始终位于执行信息区域顶部且可点击；滚到顶部后首行日志可见，滚到底部后最后一条执行结果可见；桌面和平板宽度下日志与操作栏不重叠；移动宽度下收起全局数据源侧栏后操作栏仍在视口内，SQL
工作台既有桌面优先布局不作为本场景的完整移动端可用性断言。
- 恢复/清理：恢复原视口，关闭新增结果页签和查询页签。

### QRG-LIFECYCLE-01 结果切换与重新进入

- 风险/目的：P1，确认空结果与有数据结果切换时滚动容器正确重建。
- 初始路由与状态：已生成 QRG01、QRG02 两个结果页签。
- 测试数据：QRG01、QRG02。
- @Browser 操作：将空结果滚动到最右侧；依次切换到有数据结果、执行信息，再切回空结果；刷新页面后重新执行 QRG01。
- 预期结果：每次切回结果页签后横向滚动仍可操作；表头与 body 不脱节；刷新后重新执行的空结果行为一致；控制台无新增错误。
- 恢复/清理：关闭测试结果和查询页签。

### QRG-LIFECYCLE-02 已完成结果集切换不显示 Loading

- 风险/目的：P0，确认查询已结束后切换不同记录数的结果页签，不会把跨页签的记录数变化误判为数据仍在接收。
- 初始路由与状态：同一查询页签内已生成至少两个 `PAGINATED` 结果集，两个结果集记录数不同，查询执行状态已结束。
- 测试数据：依次执行两个返回记录数不同的只读查询，保留“结果1”和“结果2”页签。
- @Browser 操作：在“结果1”和“结果2”之间连续往返切换，分别观察总数左侧区域和结果表格。
- 预期结果：每次切换都直接展示目标结果集的分页、总数和数据；总数左侧不出现加载圆圈；不发起额外分页请求；结果内容与对应页签一致。
- 恢复/清理：关闭新增结果页签。

### QRG-LIFECYCLE-03 编辑器字号偏好持久化

- 风险/目的：P1，防止用户选择的 SQL 编辑器字号在刷新页面或重新进入工作台后恢复默认值。
- 初始路由与状态：进入 `/#/sql` 并打开一个可编辑的查询页签，编辑器中存在至少一行可见 SQL。
- 测试数据：QRG05。
- @Browser 操作：展开编辑器右上角字号按钮并选择“大”，记录 SQL 文本字号；刷新页面并重新打开恢复的查询页签；离开 SQL
  工作台后再次进入，观察字号；再选择“小”并重复刷新检查。
- 预期结果：选择字号后当前编辑器立即更新；刷新页面和离开后重新进入 SQL 工作台时，编辑器均恢复最近一次选择的字号；切换查询页签不会恢复默认字号；控制台无新增错误。
- 恢复/清理：恢复常用字号，关闭测试查询页签。

### QRG-BOUNDARY-02 ClickHouse LowCardinality 字符串与空值

- 关联 Issue：[ClouGence/open-cdm#361](https://github.com/ClouGence/open-cdm/issues/361)。
- 风险/目的：P0，防止数据库查询成功后，结果读取未识别类型包装，将真实值显示为 `Unsupported ... type.`。
- 初始路由与状态：进入 `/#/sql`，连接 QRG06 的本地 ClickHouse 实例，双击隔离测试库打开查询页签。
- 测试数据与准备：QRG06；先通过数据库客户端执行相同查询，核对普通字符串、中文、空串和 NULL 的原始值。
- @Browser 操作：执行 `SELECT id, plain, low_cardinality, nullable_low FROM codex_qrg_<runid>.string_types ORDER BY id;`
  ；检查三行、各列及加载结束状态。再执行
  `SELECT id, plain, CAST(low_cardinality AS String) AS low_as_string, CAST(nullable_low AS Nullable(String)) AS nullable_as_string FROM codex_qrg_<runid>.string_types ORDER BY id;`
  作为类型解包对照，切换两个结果页签比较；刷新并重新执行原查询，确认结果可重复。
- 预期结果：原查询和 CAST 对照均返回三行；普通列和低基数列的非空值一致，NULL 与空串可区分；原类型及嵌套类型均不出现
  `Unsupported LowCardinality(String) type.` 或 `Unsupported LowCardinality(Nullable(String)) type.`；查询结束后不持续加载，无新增
  Console 错误。
- 恢复/清理：关闭本场景创建的查询及结果页签，仅清理本场景隔离数据；如为驱动兼容临时调整了测试服务压缩参数，记录原值并在不影响复现证据的前提下恢复，禁止修改共享实例配置。

### QRG-BOUNDARY-03 ClickHouse 参数类型、嵌套值与驱动边界

- 关联 Issue：[ClouGence/open-cdm#361](https://github.com/ClouGence/open-cdm/issues/361)。
- 风险/目的：P0，防止包装类型未解包、参数化类型未匹配、几何或 Nested 值显示 Java 对象地址，并记录聚合状态读取的驱动限制。
- 初始路由与状态：同 QRG-BOUNDARY-02，使用具备相关类型的本地 ClickHouse 版本和当前项目配置的 JDBC 驱动。
- 测试数据与准备：通过下面的只读 SQL 构造数据，无须新增表。`Time`、`Time64` 需要服务端支持且启用 `enable_time_time64_type`。

```sql
SELECT CAST(42 AS SimpleAggregateFunction(sum, UInt64)) AS simple_agg,
       CAST('hello' AS Enum8('hello'=1, 'world'=2)) AS enum_value,
       toDateTime('2026-09-30 08:00:00', 'UTC') AS datetime_utc,
       NULL AS null_value;
SELECT CAST((1., 2.) AS Point) AS point_value,
       CAST([[(1., 2.), (3., 4.)]] AS Polygon) AS polygon_value,
       CAST([(1,'中文'),(2,NULL)] AS Nested(id UInt8, name Nullable(String))) AS nested_value;
SELECT toString(CAST('25:34:56.123456789' AS Time64(9))) AS time_value,
       CAST('-01:02:03' AS Time) AS negative_time
SETTINGS enable_time_time64_type=1;
SELECT CAST('{"a":1}' AS JSON(max_dynamic_paths=2)) AS json_value,
       INTERVAL 1 DAY AS interval_value;
SELECT sumState(toUInt64(42)) AS state_value;
SELECT finalizeAggregation(sumState(toUInt64(42))) AS final_value;
```

- @Browser 操作：逐条执行 SQL，核对单行结果及执行信息；几何类型再覆盖 Ring、MultiPolygon、LineString 和 MultiLineString；枚举再覆盖
  Enum16；SimpleAggregateFunction 再覆盖 Nullable 值、Decimal 及带参数函数 `groupUniqArrayArray(2)`。刷新页面后重复首条查询。
- 预期结果：普通结果分别显示 `42`、`hello`、正确时间和 NULL；几何值保留嵌套括号与数值，Nested 显示 `[(1, 中文), (2, null)]`
  ，不出现 Java 数组或对象地址；显式转为字符串的时间保留大于 24 小时和纳秒精度，负整数秒时间为 `-01:02:03`；JSON 和 Interval
  不再显示 Unsupported；显式 finalizeAggregation 查询返回 `42`。新查询可继续执行，加载状态结束。
- 驱动边界：当前 JDBC 0.9.7 读取原始 AggregateFunction 状态时可能将有数据的查询报告为 0 行；应用层没有额外拦截，不能将该结果视为读取成功，需使用
  `finalizeAggregation()` 或相应聚合 Merge 函数读取最终值。该驱动还会错误解码负数 Time64 的小数部分，应用层没有禁止小数 Time64 查询；精度为
  0 的 Time64 正常读取。负数小数时间需使用 `toString(time_column)` 在服务端转换以保真，不能把客户端显示结果作为原始值正确的证据。MultiPoint
  在当前驱动读取元数据之前即报不支持，不能通过列读取器补齐；服务端禁止的 LowCardinality(Enum8)
  仍应报服务端错误。这些边界不属于已通过的原始类型读取场景。
- 恢复/清理：关闭本场景新增结果页签，保留原查询连接；不更改共享库表或驱动配置。

## Cleanup

1. 关闭为验证创建的结果页签和查询页签。
2. 恢复 @Browser 原视口与缩放。
3. 除 QRG06 的隔离数据准备外，查询阶段均使用只读 SQL；QRG06 只清理本流程创建的库表，不删除共享数据。

## Skip Conditions

- @Browser 未登录、无 SQL 查询权限或没有可用只读测试数据源时，浏览器场景标记为 `SKIP`，保留 lint、构建和源码结构检查结果。
- QRG06 的数据库或驱动连接失败时，先区分认证、压缩协议和类型读取问题；未成功执行查询前，不将连接错误当作 LowCardinality
  类型问题的复现证据。
- 当前环境不支持触控板手势时，改用可见横向滚动条拖动完成相同断言。
- 手机宽度下 SQL 工作台若明确不支持完整操作，不执行 SQL，只检查结果区域没有新增页面级横向溢出。
