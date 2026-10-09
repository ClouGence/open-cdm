# 数据编辑器默认值与显式 NULL

## Purpose

统一表数据编辑器的新增语义，防止显式 NULL 被替换成 DEFAULT、只读列被提交、全默认值行丢失操作类型，以及省略自增列后无法回填生成键。

## Scope

- SQL 工作台 `/#/sql` 的表数据页、新增/复制行、右键设置 NULL/空字符串、SQL 预览和保存后刷新。
- `LuckySheetDataView.vue`、`editor/data/generateDml`、`editor/data/saveData`、公共 `DsFamilyDataEditorSpi` 与各方言的全默认值 INSERT。
- 前端所有数据源使用同一契约：未填写的默认值/自增列不传，插入只读列不传；显式 NULL 传 null，空字符串传空字符串，新增行传 `dmlType: INSERT`。
- 数据库原生约束、DEFAULT ON NULL、空字符串规则及 identity 限制仍由目标数据库决定。SQL 中明确写 NULL 不代表所有类型最终都存储 NULL。
- HANA 类型精度、键定位、导出与脱敏仍按 [HANA 数据编辑](hana_data_editor.md) 验证；本流程不重复那些用例。

## Preconditions

- 更新 console、SDK、dsc-common、受测数据源插件及 Web；本地 `http://127.0.0.1:8222` 可达。
- @Browser 已登录有隔离测试库 QUERY/DML 权限的账号；没有登录时由用户在 @Browser 登录。
- 至少准备 MySQL、Oracle、PostgreSQL、SQL Server 中本地可用的实例；其他受影响数据源逐个补验，不以一个实例代表全部。
- 每个场景从重新加载的表数据页开始；隔离表中无并发写入。

## Test Data

用本次唯一后缀创建 `cdm_edit_defaults_<suffix>`，列为 `id INTEGER DEFAULT 1 PRIMARY KEY`、可空 `label VARCHAR(40) DEFAULT 'fallback'`。Oracle 使用 VARCHAR2；按目标方言调整名称和类型。每个新增场景后删除该隔离表中的测试行，避免默认主键重复。

再准备目标方言的仅自增主键表 `cdm_edit_auto_<suffix>`：MySQL 使用 AUTO_INCREMENT，PostgreSQL/Oracle 使用实例支持的 identity，SQL Server 使用 IDENTITY。HANA 复用其测试脚本中的 EDIT_IDENTITY_ONLY。针对支持生成列或 rowversion 的实例额外准备只读生成列，不把伪列当真实列建表。

不在已有业务表上测试。只有版本支持对应定义时才创建；不支持的类型或语法记录为该实例的覆盖缺口。

## Suites

### DED-SMOKE-01 / Smoke（P0）

- 初始状态：`/#/sql`，打开空的默认值测试表，完整权限账号。
- 准备/操作：点击新增行，直接预览 SQL。
- 预期：能识别为空数据的 INSERT；页面不报空操作类型异常。MySQL 家族使用空列 VALUES，PostgreSQL/SQL Server 使用 DEFAULT VALUES；其余公共实现选一个可接受默认值的列使用 DEFAULT；HANA 使用专用逻辑。只读伪列不进入 INSERT。
- 清理：取消预览，不保存。

### DED-MAIN-01 / Main Flow、State Consistency（P0）

- 初始状态：空默认值测试表；每个子场景重新加载。
- 操作：新增一行，label 不填写，预览并保存；清理后新增一行，对 label 右键设置 NULL，再预览保存；清理后用右键设置空字符串重复。
- 预期：未填写显示 DEFAULT，SQL 省略 label 或使用 DEFAULT，回读 fallback；显式 NULL 显示 NULL，SQL 包含该列及 NULL；空字符串的请求保留空字符串，SQL 按方言生成。以刷新后的数据库事实验证，Oracle 等实例的空字符串语义另行核对。
- 清理：每个子场景后仅删除该隔离表中的测试行。

### DED-BOUNDARY-01 / Boundaries（P0）

- 初始状态：默认值测试表新增行。
- 操作：依次设置 NULL、输入普通文本、清空文本；再次设置 NULL。另测引号、Unicode、复制一条 label 为 NULL 的已有行并修改主键后预览。
- 预期：NULL 标记不会污染后续输入；重新输入再清空后恢复默认值语义。复制行的 NULL 保留为显式 NULL；已存在的 NULL 行显示 NULL，不因列有默认值显示 DEFAULT。引号转义不丢失，空字符串不作为字段缺失。
- 清理：取消未保存修改；删除新增测试行。

### DED-AUTO-01 / Main Flow、Boundaries（P0）

- 初始状态：仅自增列的隔离表为空；另一张表含只读生成列。
- 操作：新增全空行并预览保存；刷新检查生成键。对含生成列的表新增/复制行并预览。
- 预期：全默认值 INSERT 可执行且仅增加一行；生成键回填或全表刷新后可见。插入只读列不提交。可写自增列的显式 NULL/值按数据库规则执行，不在公共层悄悄改成默认值；数据库拒绝时显示失败。
- 清理：删除隔离表新增行。

### DED-EXTREME-01 / Extreme（P1）

- 初始状态：分别为空表、单行表、跨一页的隔离数据。
- 操作：新增/复制一行，再切页并重新打开；检查窄视口和 200% 缩放下 NULL/DEFAULT/AUTO 占位显示。
- 预期：空表与已有数据路径均生成相同契约；未引入占位遮挡或额外布局变化。大量行压力测试不适用于本次逐行语义改动。
- 清理：取消修改、恢复页大小和缩放。

### DED-REPEAT-01 / Repeat And Concurrency、Lifecycle（P1）

- 初始状态：新增的默认值行尚未保存。
- 操作：反复预览、取消、设置 NULL，再预览；关闭页签并重新进入。两个页签分别做独立新增预览。
- 预期：每次使用当前值；explicitNull 不跨行或页签泄漏；重开后只展示数据库真实数据，不残留未保存标记。
- 清理：取消预览并关闭测试页签。

### DED-FAILURE-01 / Failure And Recovery（P0）

- 初始状态：新增三行，第二行使用重复主键，第三行使用新主键。
- 操作：保存，观察执行结果；刷新确认已提交数据，修正失败行后重试。若 @Browser 支持，再单独模拟断网/接口拒绝。
- 预期：首行成功，第二行失败，第三行未执行；失败行的 NULL/默认值意图保留。未知网络结果先刷新核对再重试，不重复插入已成功行。
- 清理：恢复网络并删除测试行；无法模拟网络时仅跳过网络分支。

### DED-PERMISSION-01 / Permission（P0）

- 初始状态：相同隔离表，分别使用完整权限与仅查询账号。
- 操作：尝试新增、设置 NULL 和保存。
- 预期：仅查询账号不能通过新请求字段绕过现有写入权限；只读列仍不可编辑。
- 清理：恢复原账号，不修改生产权限。

## Cleanup

确认唯一后缀后删除本流程创建的隔离表；恢复页大小、缩放、网络和账号，关闭本流程的页签。

## Skip Conditions

服务不可达、缺少测试数据库、缺少登录或 DML 权限时，跳过相应实库/页面场景并明确覆盖缺口。替代验证可以使用各 SPI 的 SQL 生成探针、真实组件方法的请求和绘制状态探针以及模块测试；这些不能替代浏览器操作与数据库执行。不得用其他浏览器绕过 @Browser 的登录要求。
