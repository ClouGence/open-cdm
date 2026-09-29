# open-cdm 数据源接入调研（以 SAP HANA 为例）

> 调研日期：2026-09-29。代码基线：`5f1d2155ae30110d7c88e2cfc34b7a2dbb24954c`。
> 本文是供人工核实的研究记录，不是已执行的接入方案，也不是 skill。只读检查源码，不连接数据库，不修改产品代码。

## 1. 已确认的起点

当前仓库已经存在 `ds-hana`，并已在 `backend/settings.gradle` 注册。HANA 不能作为“仓库完全没有实现”的例子；实际落地应先盘点、复测、补齐现有实现。

- `HanaDsPlugin` 已绑定配置、序列化、会话、驱动、SQL 引擎、元数据与编辑器相关 SPI。
- 驱动家族名为 `SAP Hana JDBC`，SQL 引擎名为 `SAP Hana SQL`。
- `DataSourceType.Hana`、schema 类型体系、前端 HANA 图标已经存在。
- `HanaSqlEngineSpi` 的安全域解析器为 `null`、血缘解析器为 `EMPTY`，`dslProvider` 明确抛出不支持异常。不能据此宣称所有 SQL 安全和治理能力已齐备。
- `HanaConfigSpi.supportSSL()` 返回 `false`；HANA Cloud / TLS 场景需要进一步核实。
- `build.gradle` 的编译驱动版本为 `2.22.12`，`drivers.xml` 列出了 `2.22.12` 至 `2.28.6` 的多个运行时版本；二者职责不同，不应机械地要求版本完全相同。

后续记录将区分“源码已确认”“根据调用链推导”“需要真实环境验证”，不把类或方法存在当作验收通过。

## 2. 接入前先确定范围

“增加一种数据源”不是固定大小的任务。应先确定以下输入，再选参考实现、估算工作量。

| 输入 | 需要明确的内容 | 对工作量的影响 |
| --- | --- | --- |
| 产品与版本 | 厂商、数据库版本、部署形态、兼容模式；HANA Platform 与 HANA Cloud 分开确认 | 决定协议、系统视图、SQL 语法和 TLS 要求 |
| 连接方式 | 官方 JDBC、驱动版本、账号认证、SSH、TLS、自定义 URL、离线环境 | 决定配置、证书、驱动分发和连接工厂工作 |
| 对象层级 | catalog/database/schema 的实际含义、是否允许连接内切换 | 影响树、会话、SQL 全限定名和权限对象路径 |
| 首期能力 | 查询、事务、数据编辑、DDL、导入导出、SQL 审核、脱敏、工单、CI/CD 分别是否纳入 | 决定哪些 SPI 必须实现，哪些入口应保持关闭 |
| 验证条件 | 隔离测试库、目标版本、普通权限账号、测试数据、可验证的部署形态 | 没有环境只能做静态检查，不能承诺产品验收 |

建议按以下交付层级拆分，但基础权限边界与资源释放在每一层都必须成立：

1. **连接与查询闭环**：安装插件和驱动，新增/编辑/保存配置，测试连接，打开查询窗口，浏览基本对象，执行查询，正确结束或取消请求。
2. **日常数据库管理**：完善数据类型、结果展示、事务、表数据编辑、DDL 编辑和导入导出；只开放已验证的功能。
3. **团队治理与发布**：SQL 行为和资源识别、审核规则、脱敏、工单、CI/CD 等逐项验证；明确数据库版本和部署形态支持范围。

兼容 MySQL/PostgreSQL 等现有家族的数据源，通常可以复用家族模块，再补协议和元数据差异；独立方言的数据源则需要更多类型、SQL 和系统视图工作。HANA 当前属于“已有独立实现的补齐与验收”，不能按复制一个 JDBC 配置的工作量估算。

## 3. 当前架构中需要接通的链路

```text
settings.gradle 注册模块 → 构建插件包 → PluginLoadHelper 扫描 @Plugin
  ├─ DsPluginBinder：注册配置、序列化、驱动家族、SQL 引擎、执行/UI SPI
  ├─ drivers.xml + DsFactory 服务声明：驱动定义与连接工厂
  └─ SchemaFramework：类型、表结构模型与转换

Console 设置接口 → dsSupportNames / 配置定义 / 能力声明 → 通用 Vue 页面
保存连接配置 → 序列化 → 执行侧资源管理 → DsFactory → JDBC Connection
  ├─ Session / Hooks：查询、事务、取消、结果与资源释放
  ├─ MetaService / UmiService / MetaProvider：对象树与结构详情
  └─ SQL 引擎：拆句、行为分析、安全域、血缘、查询改写
```

这里有三个容易混淆的边界：

- **注册模块 ≠ 页面可见**：模块要被构建和加载，插件 `display`、菜单和权限过滤还决定页面是否列出它。HANA 当前 `display=false`，`DmHomeController.displayDsPlugin()` 会将其从选择列表中过滤。[S1][S2][S4]
- **编译依赖 ≠ 运行时驱动已经安装**：`compileOnly` 提供编译 API；运行时驱动由 `drivers.xml`、驱动加载器和对应版本的资源准备流程提供。[S2][S3][S5]
- **会话能执行 SQL ≠ SQL 分析支持完整**：查询执行、拆句、补全、权限行为识别、规则检查和血缘是不同能力，必须分别验收。[S7][S8]

## 4. 一般需要做的开发工作

### 4.1 模块、产品标识和插件注册

新数据源通常需要：

- 在 `backend/clouddm-plugins/clouddm-ds/ds-<name>/` 新建模块，使用 `com.clougence.plugin-conventions` 并注册到 `backend/settings.gradle`。
- 在 `DataSourceType` 定义稳定的产品标识、短名、显示分组和排序；涉及 schema 框架时检查 `DsType`、类型绑定和转换注册。
- 实现 `DsPlugin`，使用 `@Plugin(dsProduct=...)` 注册，按既有模式绑定配置、执行、UI 和 SQL 能力。
- 配置 `includePackages` 和插件类加载边界，检查厂商类、共享库与驱动类的加载来源。
- 新增独立 SQL 引擎时注册全局 `SqlEngineSpi`；若复用既有引擎，绑定真实引擎名称，不要求每种数据库都新建 `sql-*` 模块。

参考选择：HANA 展示独立方言接入；`GoldenDBMySQLDsPlugin` 展示复用 `dsc-common-mysql` 的 SQL、数据编辑和安全规则，再覆盖厂商差异的方式。不能因为“兼容 MySQL”就默认系统表、版本探测和所有 DDL 都完全兼容。[S1][S2][S11]

**交付物**：模块可构建、插件可加载、标识与绑定一致；完成验收后再开放展示。

### 4.2 连接配置与驱动

| 工作 | HANA 中的参考位置 | 核实要点 |
| --- | --- | --- |
| 配置模型与动态表单 | `dsconf/HanaConfig.java`、`HanaConfigSpi.java` | `@ConfigDef`、默认端口、必填项、默认库/schema、超时与认证选项 |
| 配置跨层传输 | `HanaSerializationSpi`、`@Serialization` | 保存后重新打开与执行侧解码一致，使用正确插件 ClassLoader |
| 驱动发现 | `META-INF/clougence/drivers.xml` | 家族名、版本、资源坐标、工厂类匹配；支持下载或实际约定的离线准备方式 |
| 连接工厂注册 | `META-INF/services/com.clougence.drivers.DsFactory` | 内容指向真实工厂类 |
| 参数翻译 | `HanaConfig.asDriverProperties()`、`HanaDsFactory` | 通用配置键映射到厂商属性；单位、URL 编码、schema 与 database 不混淆 |
| 安全连接 | 配置 SPI、共享会话工厂、厂商工厂 | 证书格式、校验模式、执行节点文件路径和驱动 TLS 参数连通；声明支持必须有真实实现 |

HANA 当前已支持从工厂接收自定义 URL，并在普通 URL 中拼接 `databaseName`；这只证明工厂有相应分支，还需验证页面保存和资源管理链路是否正确传入。SSH 也不能只检查 `supportSSH()` 返回值，要走产品连接路径。[S2][S3][S6]

**交付物**：配置新增、修改、保存、重载与测试连接闭环；认证失败、端口不可达、慢连接均能在预期时间失败，日志和页面不泄露凭据。

### 4.3 会话、查询结果与资源生命周期

优先沿用 `RdbSessionFactory`、`DefaultRdbSession`、`SessionHook` 与 `DsResourceManager`。例如共享工厂在会话初始化失败时会关闭已申请的 `DsObject`，新实现不能绕过这条释放路径。[S6]

需适配或验证：

- 测试 SQL、版本探测、初始 catalog/schema、自动提交与隔离级别。
- 查询与更新计数、多结果集、流式读取、fetch size、查询时限和参数绑定。
- 数值精度、时间、Unicode、二进制、LOB、NULL、厂商类型的读取和导出。
- 取消查询定位到当前会话，取消所需权限、取消失败反馈，以及取消后事务/会话状态。
- 提交、回滚、断网、驱动自动重连、重复取消、会话关闭和导出中止后的终态与资源回收。
- EXPLAIN 的生成、读取和清理，确保分析请求不执行原本的业务写操作。

`HanaSession` 已有尾部分号处理、更新计数适配及 `EXPLAIN_PLAN_TABLE` 清理逻辑；这些是可复用起点，不代表取消、并发和异常路径已经通过验证。[S2]

**交付物**：查询、事务、取消和导出在成功/失败后都有明确终态；连接、Statement、ResultSet 与临时执行计划记录得到释放。

### 4.4 元数据、对象树和类型体系

HANA 的现有路径是 `HanaMetaService → HanaUmiServiceDm → HanaMetaProviderDm / HanaMetaProviderUtils`。后者读取 `SYS.TABLES`、`SYS.TABLE_COLUMNS`、`SYS.INDEXES` 等系统视图。浏览器层由 `HanaDsBrowseSpi` 定义 catalog/schema 层级、对象组、大小写和右键菜单。[S2]

重点工作：

- 定义真实的对象层级和路径，区分“选择数据库建立连接”与“已有连接切换数据库”。
- 获取表、视图、列、类型、默认值、注释、主键、唯一键、索引与外键；程序、序列、同义词等按首期范围处理。
- 验证 schema 隔离、同名对象、多列约束顺序、引号标识符，以及普通权限账号可见的元数据。
- 补充 `cg-schema` 的厂商类型和必要的表结构导入/导出、类型转换。只做同库管理时，不需要默认实现所有异构目标。
- 保持对象树声明、详情接口、菜单和 SQL 生成器一致，避免“能列出但点开报不支持”。

**交付物**：数据库真实结构、对象树、属性展示和用于后续权限判断的对象路径一致。[S9]

### 4.5 SQL 方言与语言服务

按实际支持范围实现或复用：

| 能力 | 对应职责 | 典型验证样本 |
| --- | --- | --- |
| 引号与名称 | `Dialect`、关键字与大小写规则 | 保留字、双引号、空格、混合大小写、全限定名 |
| 拆句与语句分类 | `SplitAnalysisSpi` | 注释、字符串内分号、多语句、过程体、SQLScript 块 |
| 行为/资源识别 | `BehaviorAnalysisSpi` | DDL/DML、跨 schema、子查询、CTE、MERGE、复杂引用 |
| 查询限制改写 | `RewriteSpi` | LIMIT/TOP、已有限制、排序、集合运算、锁子句、注释 |
| 补全与语法能力 | `DsLanguageSpi`、`DslProvider`、资源 SPI | 光标位置、对象隔离、关键字加载、无支持时的能力返回 |
| 安全域和规则 | `SecDomainResolveSpi`、`SecRulesSupportSpi` | 开启规则后命中/拒绝行为与真实对象一致 |
| 血缘 | `LineageAnalysisSpi` | 输出列与来源的正确关联，未知表达式不伪造结果 |

HANA 当前拆句继承 SQL:2003 实现，行为分析还包含正则补充路径。复用标准语法是起点，不能据此保证 SQLScript 和厂商扩展覆盖完整。独立方言是否需要扩展 `cg-dslparser` 或新增语法模块，应由样本覆盖决定。[S7]

**交付物**：能力声明、解析行为和实际执行一致；遇到不支持语法时，尤其要核实权限和审核调用方如何处理，不能把无法分析当成已验证安全。

### 4.6 数据编辑、DDL 和其他数据库管理操作

- 数据编辑：读写值转换、定位原行、复合主键、无主键表的编辑限制、批量编辑和事务语义。
- 结构编辑：`EditorProvider` / `SqlBuilder`、字段与面板定义、表/索引/约束的 CREATE/ALTER/DROP、生成 SQL 的预览和执行。
- 对象脚本和模板：表、视图、触发器、过程等分别检查。模板存在与“读取现有对象的完整 DDL”是不同能力。
- 类型转换和异构 DDL：逐目标声明支持列表，并检查精度、默认值、约束和特殊类型损失。
- 导入导出及模拟数据：先核实能否复用通用链路，再补厂商差异；`plus-faker` 已有 HANA 注册和类型资源，但不应将其作为最小接入必做项。

HANA 的实现集中在 `definition/ui/`；`HanaConvertTableDDLSpi` 只声明特定转换目标，应以该列表和实测为准。[S2][S9]

### 4.7 前端与国际化

前端已经通过 `dmGlobalSetting.dsSupportNames` 消费后端给出的产品列表，并使用配置定义和通用编辑器。新增数据源优先补后端描述和现有组件需要的数据，不需要先写一套专属 Vue 页面。[S4][S10]

仍需排查：产品名称/图标、分组和排序、连接表单、驱动准备、SSL/SSH 选项、事务和查询按钮、对象菜单、导入导出入口、编辑器资源及前端按类型分支。HANA 当前图标已存在。

前端新增可见文案维护 `frontend/src/locales/`；插件定义的配置、菜单等文案沿用后端 i18n。同步检查中文与英文、桌面与移动端布局。前端源码发生修改后，必须执行根规则要求的 `cd package && ./all_build.sh web` 并成功。[S10][S13]

### 4.8 权限、工单、审核和 CI/CD

通常复用平台流程，不为每种数据源重写控制器和工单状态机；但必须用该数据源验证这些流程所依赖的 SQL、对象路径和执行契约。

- 平台资源授权与数据库账号权限是两层边界，普通账号和管理员都要测试。
- 确认行为识别是否支撑真实读写权限判断，未知语句和分析失败的处理方式是否符合当前协议。
- 查询规则、脱敏、血缘、工单影响分析和执行计划按各自调用链验证，不能以一个 feature 开关代表全部能力。
- 若纳入工单/CI/CD，验证提交前分析、审批后的执行、事务和部分失败、超时/取消，以及重复调度是否重复执行写入。
- 涉及 DAO、API 或持久化协议变更时再扩展平台层；新增数据源本身不意味着必须新增业务表或 Flyway migration。确有升级数据/结构需求时新增 migration，不修改历史文件。

HANA 的 `secDomainResolveSpi=null` 在 `SecRulesEngineImpl.openQueryCheck()` 中会得到禁用的查询规则检查会话。这是明确的能力缺口；不能从这一处推出“平台所有权限都失效”，也不能将它描述为完整支持 SQL 审核。[S8]

### 4.9 构建、驱动分发与部署

- 构建约定提供 `customFatJar`；只编译成功还不足以证明插件可以加载。
- `package/pkg/{alone,console,sidecar}/build.gradle` 按 `ds-`、`plus-`、`inner-` 前缀收集插件包。标准命名且已注册的模块通常进入既有打包流程，但仍需检查实际发布包和运行时加载。[S5]
- 离线驱动由独立的 `built-in-drivers.xml` 决定，**不会因插件自己的 `drivers.xml` 有声明就自动内置**。当前内置清单没有 HANA。
- 若决定内置新驱动，家族名、版本和 Maven 坐标须与插件声明匹配；是否分发厂商二进制及其条款由实际发布方案另行核实，不把“有 Maven 坐标”当成已确认可再分发。
- 单机验证后，若承诺分布式部署支持，继续验证 Console/Sidecar 的插件与配置传输、执行节点驱动和证书准备、重启后恢复。

**交付物**：安装包内插件齐全，实际运行时能准备驱动并建立会话；离线支持范围有明确说明。

## 5. HANA 现状与优先核实项

以下是静态检查结果，不是实测 bug 清单；“调用链推导”应在落地时复现。优先级表示开放 HANA 前的核实顺序。

| 优先级 | 证据与状态 | 影响 / 后续动作 |
| --- | --- | --- |
| P0 | **源码确认**：`HanaDsPlugin` 的 `display=false`；后端按该值过滤 | 解释目前选择入口为何隐藏。先明确隐藏原因和首期能力，再决定开放，不先机械改为 true |
| P0 | **调用链推导**：`QueryEditorController.loadDsLanguage()` 直接调用 `dslProvider()`；HANA 实现抛出异常，该调用不在前面的获取引擎 catch 内 | 查询编辑器配置请求存在失败路径；应优先复测并统一“不支持 DSL”的接口契约，否则即使连接成功也可能无法正常打开查询窗口 |
| P0 | **源码 + 官方资料，待版本实测**：`connectTimeoutMs` 原值写入 `ConnectionProperty.CONNECT_TIMEOUT`；官方 JDBC 文档的 `connectTimeout` 单位是秒 | 核实目标 ngdbc 常量所对应的属性和类型，复测配置 5000 ms 时的实际超时，必要时正确换算。`communicationTimeout` 的官方单位是毫秒，现有 `soTimeoutSec × 1000` 要单独评估，不能一起机械改动 [E1] |
| P0（若要求治理） | **源码确认**：安全域为 null、血缘为 EMPTY，插件未注册 HANA 安全规则支持；查询规则引擎对 null 返回禁用会话 | 明确首期是否允许有限能力开放；若要 SQL 审核/治理，则补齐对应分析与规则并实测，不能只补 UI 开关 |
| P1 | **源码确认**：`supportSSL=false`，`HanaConfig.asDriverProperties()` 未映射通用 SSL 字段 | 核实 HANA Platform、Cloud、普通端口、自定义 URL 和证书校验场景。驱动自身可能启用 TLS，不能据此断言所有 Cloud 连接必然失败 [E2][E3] |
| P1 | **源码 + 官方资料**：`supportIsolation()` 返回全部 `RdbIsolation`，含 READ_UNCOMMITTED；注释引用的是 Data Lake 文档 | 以目标 HANA 产品与驱动实测为准缩小或解释选项。HANA Platform SQL 文档列出 READ COMMITTED、REPEATABLE READ、SERIALIZABLE；不能混用 Data Lake 或其他 SAP 产品规则 [E4] |
| P1 | **源码观察**：`HanaHooks.getQueryID()` 查询所有 RUNNING/Remote 会话，未显式限定传入连接自身；取消通过系统命令执行且捕获驱动异常 | 核实真实查询返回和公共取消链路，验证不会取消其他会话、权限不足不会被误报成功；需多会话实测，不在本次静态调研中宣称已经复现 |
| P1 | **源码确认**：浏览器声明 Catalog/Schema 两层；`HanaMetaService.getCurrentCatalog()` 返回 null；切换 catalog 明确不支持 | 检查默认 database、当前上下文、元数据树和全限定名如何协同。HANA Cloud 不应无条件复用 Platform 的 `databaseName` [E1] |
| P1 | **源码确认**：`requestObjectScript()` 抛出不支持；树默认对象组为表/视图/序列/同义词，而元数据层还有过程/函数/触发器方法 | 按承诺范围统一入口与支持矩阵；不能把底层方法存在当成页面已开放或可编辑 |
| P1 | **源码确认**：已有 SQL:2003 拆句、行为分析、LIMIT/TOP 改写和 EXPLAIN 实现 | 用 HANA 特有语法、SQLScript、复杂 DML 和临时计划清理验证，不能仅跑 SELECT 1 |
| P1 | **源码确认**：编译驱动版本与运行时候选版本不同；内置驱动清单没有 HANA | 验证所承诺驱动版本能加载厂商专有 API；明确在线准备还是离线交付 |
| P1 | **目录检查**：公共测试框架已绑定 hana，已有 view/trigger 模板样本；未找到 `tests/datasource/hana/` 与 HANA 专用前端复测流程 | 补齐能力矩阵、真实环境证据和长期复测流程；不能把缺少专属文件名等同于全仓库完全没有相关测试 |

## 6. 建议的落地顺序与验收清单

### 阶段 A：盘点与范围确认

- [ ] 确认产品/版本、驱动、部署形态、认证、首期能力和隔离测试环境。
- [ ] 建立能力矩阵：每项写明已有实现、计划支持、明确不支持、验证方法和结果。
- [ ] 对 HANA 先复现第 5 节 P0 项，确定显示开关背后的真实阻塞。
- [ ] 对全新数据源选择同家族参考；确认不能复用的部分再独立实现。

### 阶段 B：连接与查询闭环

- [ ] 插件发现、驱动准备、配置保存与重新打开正确。
- [ ] 普通账号连通，错误凭据/错误端口/慢连接在预期时间失败。
- [ ] 查询窗口配置接口成功；对象树能按正确层级展示。
- [ ] SELECT、结果分页、写入计数、提交/回滚、取消和关闭连接行为正确。
- [ ] 断网与重连不会把未知写入结果当成未执行后自动重复提交。

### 阶段 C：补齐首期管理与治理能力

- [ ] 列类型、特殊值、表数据编辑、DDL 预览与执行、导入导出逐项通过。
- [ ] 标识符、SQLScript、多语句、复杂 DML、LIMIT/TOP 等样本覆盖承诺语法。
- [ ] 普通权限与管理员的对象可见性、读写拒绝、规则和脱敏符合协议。
- [ ] 若纳入工单/CI/CD，验证审批、分析、执行、失败恢复和重复触发。
- [ ] 所有未支持功能有准确的能力声明和入口行为。

### 阶段 D：发布与复测

- [ ] 构建产物能在实际运行时加载；更新插件后按真实部署流程重启/加载并复测。
- [ ] 承诺的单机/Console-Sidecar、在线/离线驱动路径分别验证。
- [ ] 前端中英文、常见桌面宽度与移动端检查完成。
- [ ] 更新 `tests/datasource/<name>/` 的专项矩阵和 `tests/frontend/datasource/<name>_datasource.md` 的长期流程。
- [ ] 已支持范围和未验证项写清楚后，再调整展示开关与产品说明。

### 可复用的验证入口

以下是后续开发时的命令示例，**本次调研没有执行构建或数据库测试**：

```bash
# 插件编译与模块已有测试；模块没有测试时不能据退出码宣称功能验收通过。
cd backend && ./gradlew :ds-hana:build

# 真正的插件包；此命令只构建 customFatJar，不自动完成部署和重启。
cd package && ./all_build.sh plugin ds-hana

# 公共数据源测试模块的 Gradle 名是 s-test，不是 ds-test。
# 该筛选执行模板测试类，其中包含 HANA 样本，也包含其他数据源模板。
cd backend && ./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.template.TemplateTextTest'

# 修改公共 SQL/schema/安全相关模块后，按影响面扩大测试。
cd backend && ./gradlew :s-test:test

# 修改前端后按需运行检查；web 构建成功是仓库硬性要求。
cd frontend && npm run lint
cd frontend && npm run check-i18n
cd package && ./all_build.sh web

# 发布包验证阶段，按部署范围执行。
cd package && ./package.sh --build
```

每条命令都从仓库根目录独立执行，不要在同一 shell 中连续执行上面的 `cd ...` 而不恢复工作目录。

现有测试资产可以扩展文本样本和现有测试类；根据根规则，用户未明确要求时不要新建测试类。浏览器复测前需读取 `frontend/AGENTS.md` 和相关流程文档，并遵循其中 Chrome 规则；该规则引用的 skill 若在实施会话不可用，应先解决工具条件，不能虚构完成。浏览器长期文档只记稳定步骤，不写日期化 PASS/FAIL 报告。[S12][S13]

直接 JDBC 探针可辅助定位驱动/数据库行为，但不能替代 CloudDM 页面和运行时链路的接入验收；已有 GoldenDB 流程可作参考。[S12]

## 7. 后续提炼 skill 的边界（尚未创建）

建议 skill 的目标是“按当前代码核实并完成数据源接入”，而不是“复制一个模块并替换类名”。它应包含：

1. **输入检查**：产品、版本、兼容模式、目标能力、环境和驱动来源；缺失信息只阻塞依赖它的步骤。
2. **已有实现发现**：检查产品枚举、插件、驱动、SQL 引擎、前端入口和测试资产，避免重复造一个已有数据源。
3. **复用决策**：选择既有家族或独立方言，输出代码证据及必须覆盖的差异。
4. **改动清单**：逐层列出必改、按能力选改、不需要改；不用固定文件数量衡量完成度。
5. **契约检查**：产品名、短名、序列化 provider、驱动家族、工厂、SQL 引擎、类型体系、UI 能力逐项对照；这些名称承担不同职责，不要求所有字符串相同。
6. **验证顺序**：离线测试 → 实际产品连接/查询 → 失败路径 → 声明支持的管理/治理功能 → 发布形态。
7. **交付门槛**：已验证能力才开放，未执行项保留待核实状态；无测试环境不能宣称完整验收。
8. **维护规则**：不修改历史 migration、不新增未授权测试类、不提交凭据/驱动二进制/生成产物；前端变更必须完成规定构建与复测流程。

可将通用流程放在未来 skill 主文档，将家族选择、文件地图、厂商差异和测试矩阵放在 references。HANA 的具体版本、行号、缺口和当前隐藏状态属于会变化的研究证据，不应永久硬编码成所有数据源都必须执行的规则。

## 8. 证据索引

仓库链接相对本文件解析；用类名和方法名定位优先于依赖易变行号。

- **[S1] 模块与产品标识**：[settings.gradle](../../backend/settings.gradle)、[DataSourceType.java](../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/base/metadata/ds/DataSourceType.java)。
- **[S2] HANA 主实现**：[模块目录](../../backend/clouddm-plugins/clouddm-ds/ds-hana/)、[HanaDsPlugin.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/HanaDsPlugin.java)、[HanaSqlPlugin.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/sql/HanaSqlPlugin.java)。本节前文以模块内相对包路径指向具体类。
- **[S3] 驱动定义与工厂**：[drivers.xml](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/resources/META-INF/clougence/drivers.xml)、[DsFactory 服务文件](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/resources/META-INF/services/com.clougence.drivers.DsFactory)、[HanaDsFactory.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/dsfactory/HanaDsFactory.java)。
- **[S4] 插件发现与页面可见性**：[PluginLoadHelper.java](../../backend/clouddm-platform/cgdm-plugin-loader/src/main/java/com/clougence/clouddm/platform/plugin/PluginLoadHelper.java)、[DmHomeController.java](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/controller/system/DmHomeController.java)。
- **[S5] 构建与分发**：[插件构建约定](../../backend/buildSrc/src/main/groovy/com.clougence.plugin-conventions.gradle)、[all_build.sh](../../package/all_build.sh)、[alone 打包](../../package/pkg/alone/build.gradle)、[console 打包](../../package/pkg/console/build.gradle)、[sidecar 打包](../../package/pkg/sidecar/build.gradle)、[内置驱动清单](../../package/pkg/builtin-drivers/built-in-drivers.xml)。
- **[S6] 会话基础设施**：[RdbSessionFactory.java](../../backend/clouddm-plugins/clouddm-ds/dsc-common/src/main/java/com/clougence/clouddm/dsfamily/execute/RdbSessionFactory.java)、[DefaultRdbSession.java](../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/execute/session/rdb/DefaultRdbSession.java)。
- **[S7] HANA SQL 能力**：[HanaSqlEngineSpi.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/sql/HanaSqlEngineSpi.java)、[查询编辑器能力加载](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/controller/editor/query/QueryEditorController.java)。
- **[S8] 查询规则降级行为**：[SecRulesEngineImpl.java](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/detectrule/impl/SecRulesEngineImpl.java)，关注 `openQueryCheck()`。
- **[S9] schema 框架**：[DsType.java](../../backend/clouddm-utils/cg-schema/src/main/java/com/clougence/schema/DsType.java)、[HANA 类型](../../backend/clouddm-utils/cg-schema/src/main/java/com/clougence/adapter/hana/)、[HANA 源类型映射](../../backend/clouddm-utils/cg-schema/src/main/java/com/clougence/reactor/mappings/hana_src/)。
- **[S10] 前端消费端**：[datasourceSupport.js](../../frontend/src/utils/datasourceSupport.js)、[DataSourceInfo.vue](../../frontend/src/components/function/addDataSource/DataSourceInfo.vue)、[CCDataSourceIcon.vue](../../frontend/src/components/widgets/CCDataSourceIcon.vue)。
- **[S11] 兼容家族参考**：[GoldenDBMySQLDsPlugin.java](../../backend/clouddm-plugins/clouddm-ds/ds-goldendb/src/main/java/com/clougence/clouddm/ds/goldendb/GoldenDbMySqlDsPlugin.java)。
- **[S12] 测试资产**：[数据源专项约定](../../tests/datasource/README.md)、[GoldenDB 实例](../../tests/datasource/goldendb/README.md)、[公共测试依赖](../../tests/ds-test/build.gradle)、[SqlTestSupport.java](../../tests/ds-test/src/test/java/com/clougence/clouddm/ds/SqlTestSupport.java)、[HANA 模板样本](../../tests/ds-test/src/test/resources/template/hana/)、[前端流程索引](../../tests/frontend/README.md)。
- **[S13] 工程规则**：[根 AGENTS.md](../../AGENTS.md)、[frontend/AGENTS.md](../../frontend/AGENTS.md)。根文件引用的 `AGENTS.local.md` 在本次工作树未找到。

外部资料仅选 SAP 官方来源，检索于 2026-09-29；文档版本不等同于项目所选驱动版本，实施时仍需对应版本和真实环境核验。

- **[E1] [JDBC Connection Properties（2.18）](https://help.sap.com/docs/r/f1b440ded6144a54ada97ff95dac7adf/2.18/en-US/109397c2206a4ab2a5386d494f4cf75e.html)**：连接/通信超时单位、currentSchema、databaseName。门户页面直接打开未返回正文，本次依据搜索索引返回的官方正文条目交叉核对，未读取整个版本手册。
- **[E2] [Client-Side TLS/SSL Connection Properties (JDBC)](https://help.sap.com/docs/SAP_HANA_PLATFORM/b3ee5778bc2e4a089d3299b82ec762a7/2624cb2191be4f68897b91023ab41d0c.html)**：厂商 JDBC 的 TLS 配置属性。
- **[E3] [SAP HANA Database Service Keys](https://help.sap.com/docs/hana-cloud/sap-hana-cloud-administration-guide/sap-hana-database-service-keys)**、[Client 2.12 新增/变更记录](https://help.sap.com/docs/SAP_HANA_CLIENT/79ae9d3916b84356a89744c65793b924?locale=en-US&state=PRODUCTION&version=2.12)：Cloud JDBC 端点示例为 443；变更记录说明自 Client 2.6 起到 443 的连接默认加密。因此 `supportSSL=false` 不能直接推导为底层没有 TLS。
- **[E4] [SET TRANSACTION（HANA Platform 2.0 SPS 08）](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20fdf9cb75191014b85aaa9dec841291.html)**：目标产品的 SQL 隔离级别说明，用于识别当前代码引用其他产品文档的问题。

## 9. 本次调研边界与待确认决策

已完成源码链路检查及上述 SAP 官方资料核对。尚未启动 CloudDM、连接 HANA、运行 Gradle/npm 测试或验证发布包；文中没有任何运行时验收 PASS。此次只新增本研究文件。

后续由人工核实的首要决策：

- [ ] 优先补齐现有 HANA，还是选一个仓库尚未实现的数据源验证通用流程？
- [ ] HANA 的目标为 Platform、Cloud，还是二者；具体版本和 ngdbc 支持范围是什么？
- [ ] 首期承诺哪些管理与治理能力，哪些明确保持不支持？
- [ ] 是否要求驱动离线内置、Console/Sidecar 部署和 TLS 证书配置？
- [ ] 提供何种隔离测试环境与账号权限，以完成产品链路验收？

这些决策明确后，再把本文的工作项转成开发任务和 skill；当前不据静态文件数量给出确定工期。
