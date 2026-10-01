# SAP HANA 2.0 Platform 支持补齐调研与落地计划

调研日期：2026-09-30。代码基线：`a9f16e78b8288c9a4158ee9df4378ee16b80021e`。

本次目标是补齐已有但因支持不完整而隐藏的 HANA 数据源。用户已确认：**优先 HANA 2.0 Platform，HANA Cloud 单独评估**。调研阶段只检查源码、核对厂商资料和制定计划；当前已推进至第 3 步实现与本地验证，实库和页面验收待环境。

当前实现覆盖了大部分插件接口，但存在明确缺口和链路不一致，不能通过取消隐藏直接交付。建议按下文 **15 步**完成连接、执行、元数据、SQL、编辑、安全治理和部署验收，最后开放入口。此前的[通用数据源调研](datasource-integration-research.cn.md)保留为历史材料，待 HANA 落地后再回看和提炼 skill。

## 完整支持的目标边界

这里的“完整”指 CloudDM 在约定 HANA Platform 版本上的产品闭环：连接管理、查询与事务、元数据浏览、常用对象管理、表数据编辑、导入导出、语言服务、权限审核、血缘脱敏、工单和 CI/CD。不能通过隐藏本应补齐的核心功能来完成验收，也不代表一次性实现 HANA 所有厂商管理功能。

第 1 步已固定首轮验证目标：Express SPS 08 / revision 088，官方镜像 `2.00.088.00.20251110.1` 及 manifest 摘要，主驱动 `2.28.6`、回归驱动 `2.22.12`；见[环境基线](../../tests/datasource/hana/README.md)。这些是待验证组合，不是兼容性已通过的承诺。Platform 的特殊对象、系统库管理、空间类型编辑等在矩阵中单列边界；不适用项必须有产品或技术依据，不能将未实现项直接记为“不适用”。Cloud 的独立评估不阻塞 Platform 主线。

已有实现统一视为“待验证”：类存在、SPI 已注册、驱动可连接或单测通过，都不能单独证明产品支持完整。每个计划步骤都需要自己的验证证据，不能把所有测试拖到最后。

## 当前实现与缺口

以下“确认”表示静态代码事实；“推导”表示沿调用链得到的影响判断，尚未在真实 HANA 上复现；“待实测”表示需要目标数据库和驱动确定行为。代码索引见文末。

| 编号 | 结论与证据 | 计划步骤 |
| --- | --- | --- |
| H01 | **确认**：`HanaDsPlugin` 为 `display=false`，配置、会话、UI 和 SQL 引擎已有注册；`configTeam()` 和 feature 注册没有实际启用相关能力。`DmHomeController` 会过滤隐藏插件。[C1][C2] | 1、13、15 |
| H02 | **确认与推导**：`HanaSqlEngineSpi.dslProvider()` 抛出不支持异常，而 `QueryEditorController.loadDsLanguage()` 直接调用它；该调用不在获取引擎的 catch 内。查询编辑器能力加载存在失败路径。仅把抛异常改成 null 可以缓解入口问题，但无法完成语法和语言能力。[C3][C4] | 6、7、13 |
| H03 | **第 2 步已纠正并实现**：官方手册章节间单位说法不一致；实际 ngdbc 2.22.12/2.28.6 按毫秒读取 `connectTimeout`，700 毫秒本地超时检查通过，因此保留毫秒。属性改用字符串并校验范围；已启用 TLS 模式及证书属性转换，本地 TLS 正反例通过，实库仍待验收。[C5][E1] | 2 |
| H04 | **第 3 步已修正，待实库验收**：catalog 按实际租户回读并核对请求上下文；schema 与对象 SQL 统一转义；隔离级别收敛为 Platform 支持集合，启用只读与位置参数能力声明。连接内 catalog 切换仍明确拒绝。[C6][C7] | 3、5 |
| H05 | **确认与推导**：取消所用 `getQueryID()` 查询所有 RUNNING/Remote 连接，未限定当前连接；`killProcess()` 将全部 `JDBCDriverException` 当成可忽略异常。需要验证目标会话定位、取消权限和失败反馈，不能先宣称已复现误取消。[C6][C8] | 4 |
| H06 | **确认**：`fetchProcedureByPart()` 使用 `PROCEDURE_NAME = ? AND SCHEMA_NAME IN (...)`，绑定顺序却是 schema 在前、过程名在后。查询条件与实参含义颠倒。[C9] | 5 |
| H07 | **确认与推导**：`mapToUkExt()` 用 `CONSTRAINT` 字段作为 map key，此处查询的唯一约束类型值都是 `UNIQUE`；同一表的多条唯一约束会合到同一个对象。索引查询只以 `INDEX_NAME` 关联两张系统视图，存在跨 schema 同名对象混入的风险，需构造样本验证。[C9][C10] | 5、10 |
| H08 | **确认与待实测**：普通表数据编辑统一加入隐藏 `$rowid$`，UPDATE/DELETE 也按它定位；视图不加入该列。需验证列存、行存、无主键、复合主键及视图的实际可编辑边界。厂商值转换工具虽然存在，当前 SPI 的模板方法委托给通用父类，不能据工具类存在断言厂商值转换已经接入。[C11] | 11 |
| H09 | **确认与推导**：`requestObjectScript()` 明确抛出不支持；默认对象组只列表、视图、序列、同义词，但元数据层还有过程/函数/触发器方法，序列/同义词详情未在 `detailLeaf()` 实现。需补齐对象从列表到详情、脚本及菜单的实际闭环。[C7][C9] | 5、10、13 |
| H10 | **确认与待实测**：表结构生成、类型转换与 EXPLAIN 已有实现；清空表注释时 `tableAlter()` 返回 null，需核实完整变更链是否产生清空 SQL；数据编辑和 DDL 对 catalog 的处理不一致。结果列元数据把 `getCatalogName()` 赋给 schema，需核对驱动返回值，避免影响来源识别。[C6][C11][C12] | 3、5、10、11、12 |
| H11 | **确认**：拆句复用 SQL:2003，行为分析含正则补充，`secDomainResolveSpi=null`、血缘为 `EMPTY`；查询规则引擎在没有安全域解析器时返回禁用会话。规则和血缘属于待补齐能力，SQLScript、复杂 DML 与过程体不能默认由通用语法覆盖。[C3][C13] | 6、8、9 |
| H12 | **确认，避免扩大结论**：平台脱敏已有“无来源信息”处理路径。因此空血缘不等于所有脱敏都不工作；仍需验证对象权限、别名、表达式、多表及导出能否正确保护敏感数据。[C14] | 8、9、11 |
| H13 | **确认**：编译驱动为 `2.22.12`，运行时定义多个版本至 `2.28.6`，内置驱动清单未含 HANA。公共测试已绑定 hana，已有 view/trigger 模板样本，但未找到 HANA 专项矩阵和前端复测流程。[C15][C16] | 1、2、14 |

一个已排除的误判：`HanaEditorProvider.indexAddColumn()` 只创建索引、`indexDropColumn()` 只删除索引，不能单独据此判为“漏重建”。公共 `TableEditorImpl` 已在索引变更时按顺序调用删除和重建。第 10 步仍需验证最终 SQL 和结构，但不预设要重写这套流程。[C12]

官方资料需与实际驱动对应。前期读取 Client 2.18 得到的超时单位疑点，已在第 2 步通过实际驱动和本地无响应端点纠正；细节和 Client 2.28 章节差异见[连接/TLS 交付说明](../../tests/datasource/hana/README.md)。本地验证不等于服务器兼容性认证。[E1]

## 15 步落地计划

当前进度：**第 1 步文档已落地；第 2、3 步实现与本地验证完成，实库/页面验收待环境；本轮未推进其他计划步骤**。依赖表示开始该步骤完整验收前需要具备的条件；阅读和方案设计可提前进行。涉及共享模块时沿用现有契约，只修改 HANA 支持确实需要的部分，并回归受影响数据源。

### 第 1 步 固定版本环境和能力矩阵

**进度（2026-09-30）**：[环境与复跑说明](../../tests/datasource/hana/README.md)、[能力矩阵](../../tests/datasource/hana/hana-test-matrix.md)已落地，包含版本组合、角色、样本、逐项源码索引和页面验证节点。实例、账号、实际制品/JDK 版本与连接验证仍待准备；E1/E2 为 BLOCKED，用例均 NOT RUN。本步没有新增可验证页面，后续各步完成时会明确告知页面验证范围与部署要求。

**工作**：确定 HANA 2.0 Platform 的 SPS/revision、租户数据库连接方式、ngdbc 支持版本；准备隔离环境、管理账号和受限账号。盘点每个现有 SPI、入口和产品能力，建立专项矩阵，记录实现位置、验证用例、结果与阻塞原因。测试账号权限按功能列明，不能全程只用 SYSTEM 验收。

**交付与验收**：形成 `tests/datasource/hana/` 环境说明和能力矩阵；已确认版本组合、测试账号角色、可清理样本及不适用依据。没有环境的项目保持待验证，不影响继续做静态和离线工作，但不能进入发布通过状态。依赖：无。

### 第 2 步 补齐驱动和连接配置

**进度（2026-09-30）**：已实现默认驱动、单端点 JDBC 地址、database/schema 属性传递、超时范围和字符串属性、TLS 证书/存储转换、SSH 证书主机名约束及日志/连接释放修正。两个驱动的本地配置、TLS、超时、Maven 准备和离线恢复检查通过；相关现有测试 42 项通过。实际 HANA 登录、页面保存重开、TLS 服务端配置和 SSH 隧道仍待环境，不能标为完整验收通过。详见[交付说明](../../tests/datasource/hana/README.md)与[矩阵本地证据](../../tests/datasource/hana/hana-test-matrix.md)。

**工作**：核实并修正超时单位、默认端口提示、database/schema、自定义 URL 与高级属性传递；补齐 Platform TLS 证书配置和校验，验证 SSH；核对配置保存、序列化、执行侧解码、驱动准备和类加载。按验证结果确定驱动版本集合，不机械把编译依赖更新为最高版本。清理可能把自定义 URL 内凭据写入日志的路径。

**交付与验收**：新增、编辑、保存重开及测试连接成功；错误凭据、不可达端口、慢连接、错误证书均有准确反馈；TLS 不能靠关闭证书校验作为通过条件。在线驱动准备可复现，离线准备方式明确。主要范围：`dsconf/`、`HanaDsFactory`、驱动资源。依赖：1。

### 第 3 步 统一会话上下文和事务语义

**进度（2026-10-01）**：已统一实际租户回读、schema/对象名转义、结果列来源，收敛隔离级别并开放只读和已有位置参数绑定声明；拒绝错误租户上下文和不支持的隔离级别。公共会话提交/回滚保留原有日志处理；本轮新增的异常上抛经调用链复核后撤回，错误反馈列为公共契约待办。已通过构建、公共模块现有测试及临时 JDBC 代理的状态/失败/资源检查；真实并发事务、DDL 边界和页面仍待环境。详见[第 3 步契约与验收范围](../../tests/datasource/hana/README.md#8-第-3-步会话上下文与事务语义)。

**工作**：统一连接目标数据库、当前 catalog/schema、对象路径和 SQL 限定名；正确处理带引号或混合大小写的 schema。按真实 Platform/JDBC 行为声明隔离级别、自动提交、readOnly、catalog 切换及参数能力，不用全枚举代表全部支持。核查提交/回滚、DDL 的事务边界与会话重新初始化。

**交付与验收**：两组同名跨 schema 对象不会串库；切换 schema、刷新页面后上下文与实际连接一致；提交/回滚效果可由另一连接确认；不支持的切换准确禁用并由服务端拒绝。主要范围：`HanaSessionSpi`、`HanaHooks`、`HanaMetaService`、`HanaSupportSpi`。依赖：2。

### 第 4 步 修复取消和资源释放链路

**工作**：使用可验证的当前连接标识设计取消路径，核实是否可复用驱动取消接口或既有独立连接取消机制；避免在忙连接上追加阻塞查询。区分“查询已结束”和权限、网络等真实取消失败，处理重复取消、断网、慢连接、关闭会话、流式读取中止和重连后的终态。连接丢失后不能盲目重试未知结果的写操作。

**交付与验收**：同时运行两个会话，只终止目标会话；取消失败可观察；取消/异常/关闭后连接、Statement、ResultSet 和后台任务释放，事务状态不被错误恢复。主要范围：`HanaHooks`、`HanaSession` 及公共会话调用链。依赖：2、3。

### 第 5 步 修正元数据与类型映射

**工作**：修正过程查询参数、多条唯一约束聚合和索引关联范围；检查系统视图实际字段、普通账号可见性、无参数函数、参数方向、返回类型、identity/default/comment、复合约束顺序及外键规则。统一树层级与详情接口，补齐纳入范围的表、视图、序列、同义词、过程、函数和触发器。核对 JDBC 结果列来源与 schema 类型映射。

**交付与验收**：双 schema 同名表/索引、多 UNIQUE、复合 PK/FK、行存/列存、特殊类型等样本在页面显示与数据库一致；单个不支持对象不能拖垮整个树；未知类型不伪造成另一类型。主要范围：`HanaMetaProviderDm`、`HanaMetaProviderUtils`、`HanaUmiServiceDm`、`cg-schema` HANA 类型。依赖：2、3。

### 第 6 步 建立 HANA 语法与拆句基础

**工作**：以真实 HANA SQL 和 SQLScript 样本决定现有 SQL:2003 的复用边界，补齐需要的 DSL/parser，注册正常的 `dslProvider()`；保证过程体、匿名块、注释、字符串分号及引号标识符不会被错误拆句。定义不支持语法的明确处理，不通过吞异常制造“解析成功”。

**交付与验收**：查询编辑器配置接口不再因 DSL 能力调用抛错；DDL、DML、CTE、MERGE、CALL 和 SQLScript 样本能按约定分类和拆句，复杂块不会被误执行为数条残缺 SQL。主要范围：HANA `sql/parser/`、`SqlEngineSpi`，按实际差异扩展语法模块。依赖：1；运行时验收还依赖 2、3。

### 第 7 步 完成语言服务和查询改写

**工作**：在第 6 步基础上补齐补全、校验、格式化、关键字和对象建议，并使 `supports()`、查询编辑器配置和实际接口一致。验证 LIMIT/TOP 改写对既有限制、CTE、集合查询、排序、锁子句及多语句的行为，避免改坏 SQL 或突破平台结果限制。

**交付与验收**：补全只返回当前授权上下文内对象；光标、错误位置和实际脚本对应；格式化不改语义；结果限制用真实查询验证。不得仅返回非空 DSL 就宣称语法校验可用。主要范围：`HanaLanguageSpi`、completion、rewrite、resource 及能力消费端。依赖：5、6。

### 第 8 步 补齐行为分析权限与 SQL 审核

**工作**：完善 HANA 行为分析和安全域解析，注册与当前规则引擎匹配的规则支持；核查是否需要系统对象注册。覆盖读写对象、跨 schema 引用、子查询、CTE、MERGE、DDL 和程序调用，明确动态 SQL/无法解析语句的处理。平台资源授权与数据库账号授权分别验证。

**交付与验收**：只读账号不能通过复杂 SQL 执行写入；无权对象不能因别名/子查询绕过检查；规则命中可阻断或按配置提示；无法分析不被当成审核通过；HANA 不再因安全域为 null 自动跳过已配置查询规则。主要范围：HANA SQL 分析、规则 SPI 与平台调用契约。依赖：3、5、6。

### 第 9 步 补齐血缘与脱敏

**工作**：实现纳入范围的列来源解析，验证别名、表达式、JOIN、CTE、视图和星号展开；对无法获得来源的 SQL 验证现有平台处理路径，保证不错误解除脱敏。检查普通查询、数据编辑读取和导出的敏感数据行为。

**交付与验收**：同一敏感列在原名、别名、表达式和多表查询中按权限与规则处理；授权豁免只作用于正确来源；未知来源有明确行为且不伪造血缘。不能仅启用 `FUNC_LINES_SUPPORT` 当作完成。主要范围：HANA lineage 与 `QueryAnalysisServiceImpl` 相关契约。依赖：5、6、8。

### 第 10 步 补齐结构编辑和对象脚本

**工作**：打通结构读取、编辑、SQL 预览、执行、重新读取的闭环；覆盖表、列、PK/UK/FK、索引、默认值、identity、注释设置/清空以及行存/列存属性。补齐已有范围内的视图、触发器、过程/函数、序列/同义词脚本和菜单。异构 DDL 只对声明的目标逐项验收，检查精度与约束损失。

**交付与验收**：不修改结构保存不产生意外变更；修改后重新读取的结构与预期一致；多个 UNIQUE 不合并、索引变更只产生正确的删除/重建；对象脚本可重建等价对象。主要范围：`definition/ui/editor/`、`HanaCreateUtils`、`HanaTypeUtils`、`requestObjectScript`、schema 转换。依赖：5、6。

### 第 11 步 完成表数据编辑与导入导出

**工作**：根据行存/列存、主键和视图能力确定安全的行定位策略，验证 `$rowid$` 的可用范围，不能统一假定所有对象都支持它。贯通实际值转换调用链，覆盖 NULL、Unicode、高精度数值、日期时间、二进制和 LOB；检查批量编辑、导入部分失败、导出分页/流式读取及中止。

**交付与验收**：只更新/删除选中的行，不能唯一定位时明确禁止写入；可写值读写往返一致；分页与导出无重复遗漏；部分失败状态可解释，取消后资源回收；导出不绕过敏感权限。主要范围：`HanaDataEditorSpi`、通用数据编辑与导入导出消费链。依赖：4、5、8、9。

### 第 12 步 验证执行计划工单和 CI/CD

**工作**：验证 `EXPLAIN_PLAN_TABLE` 的权限、并发隔离和清理，确认 SELECT/DML 分析不会执行原写入。将前面完成的解析、权限、规则和元数据能力接入实际工单与 CI/CD 流程，检查审批、计划展示、执行、事务、部分失败、取消和重复调度。

**交付与验收**：提交工单后可完成分析、审批和一次实际执行；重试/重复触发不重复写入；分析与执行结果不混淆；并发 EXPLAIN 不串结果，成功/异常后临时计划记录清理，原业务数据未因分析请求改变。主要范围：`HanaSession`、`HanaExplainPlanSpi`、平台现有 approval/cicd 调用链。依赖：4、6、8、9、10。

### 第 13 步 对齐页面入口能力与国际化

**工作**：逐页核对连接表单、对象树、工具栏、右键菜单、编辑器、规则/脱敏、工单及导出入口，消除声明支持但调用报不支持的情况；补齐中英文文案和常见视口布局。优先复用已有组件，不另建 HANA 专属前端架构。

**交付与验收**：用户能完成已约定产品流程，未支持项的前后端行为一致；所有可见文案有正确语言版本。使用 **@Browser 内置浏览器**复测，并形成 `tests/frontend/datasource/hana_datasource.md`；不沿用旧通用调研中的外部 Chrome 规则。主要范围：插件 UI 定义与受影响前端组件。依赖：2—12 对应功能完成。

### 第 14 步 完成跨版本回归与发布包验收

**工作**：汇总各步骤证据，执行选定 Platform/驱动组合及受影响共享模块回归；验证单机和 Console/Sidecar 的插件加载、序列化、驱动/证书准备、重启恢复。明确驱动在线准备与离线交付方式；若要内置，另核实分发条件及清单匹配。

**交付与验收**：实际安装包可运行，普通账号和管理账号的主流程与故障恢复用例通过；没有把 `NO-SOURCE`、未执行或直接 JDBC 连通当成产品验收。前端源码如有改动，`cd package && ./all_build.sh web` 必须退出 0；专项矩阵记录剩余阻塞，浏览器流程只记长期步骤。依赖：2—13。

### 第 15 步 开放 HANA 并回看通用接入文档

**工作**：Platform 约定核心能力没有未解决阻塞后，调整数据源展示及必要能力声明，补充版本与限制说明；验证真实可见入口、插件重载/重启和已有连接配置。结合实际补齐过程修订通用添加数据源文档，区分“全新接入”和“补齐隐藏实现”，再决定 skill 内容。

**交付与验收**：从默认可见的 HANA 入口完成新增连接、查询及关键管理/治理流程；开放后的 @Browser 冒烟通过；通用文档引用实际经验而非预设步骤。本步开放前仍需在隔离开发环境验证 HANA 页面，可使用仅供验收的临时展示调整，不将其提前作为发布变更。依赖：14；开放后的验证失败则仍视为本步未完成。

## 依赖与实施检查点

主链是环境和版本 → 连接 → 会话与资源 → 元数据/SQL → 管理与治理 → 产品回归 → 开放入口。第 6 步的离线语法工作可在连接调通前开展，但不能替代运行时验证。

| 检查点 | 需要拿到的证据 | 不能据此宣称的结果 |
| --- | --- | --- |
| 基础能力可用 | 配置往返、查询、事务、取消和断网恢复通过 | 不能宣称 SQL 审核、数据编辑或完整支持 |
| 管理与治理可用 | 元数据、DDL、数据编辑、规则、血缘脱敏、工单/CI/CD 的对应场景通过 | 不能跳过驱动版本和部署包回归 |
| 可以默认开放 | 全部约定核心能力验证完成，发布包与展示入口复测通过 | 不能顺带宣称 Cloud 或未测版本支持 |

本计划不预设工期。SQLScript 语法覆盖、治理能力和真实版本环境是主要不确定性；第 1、5、6、8 步完成后才能可靠细化工作量。

## 验证资产和执行约束

实施时复用 `tests/ds-test/` 的文本样本和现有测试类，Gradle 模块名为 `s-test`。按仓库要求，用户未主动要求新增测试类时不新增；缺少可复用承载方式的验证先在专项矩阵中明确记录，不为测试扩大生产 API。

专项环境和验收矩阵放 `tests/datasource/hana/`，如需容器环境放 `tests/dbs/`，长期页面流程放 `tests/frontend/datasource/hana_datasource.md`。不得把账号密码、厂商安装包、License 或生成日志纳入 Git。测试使用唯一命名的隔离对象，清理只覆盖本次创建的资源。

各阶段可用的验证命令如下，每行从仓库根目录独立执行；本轮实际执行范围与结果见[矩阵本地证据](../../tests/datasource/hana/hana-test-matrix.md)，并非已执行以下全部命令：

```bash
cd backend && ./gradlew :ds-hana:build
cd package && ./all_build.sh plugin ds-hana
cd backend && ./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.template.TemplateTextTest'
cd backend && ./gradlew :s-test:test
cd frontend && npm run lint
cd frontend && npm run check-i18n
cd package && ./all_build.sh web
cd package && ./package.sh --build
```

模板测试类还包含其他数据源，不应把整类通过解释成全部 HANA 能力通过。插件构建不会自动完成运行时部署/重启。所有步骤的最终验收需走 CloudDM 产品链路，直接 JDBC 仅辅助定位。

## Cloud 单独评估的范围

Cloud 后续需单独核对连接端点、TLS 和认证、`databaseName` 的适用性、系统视图/权限、对象与语法差异以及驱动支持组合。官方 JDBC 手册将 `databaseName` 标记为 Platform 专用，并提示将其用于 Cloud 可能导致连接失败。[E1]

这一部分只保留后续评估范围，本次 15 步不以 Cloud 验收为完成条件，也不因 Platform 通过而自动标记 Cloud 通过。

## 源码与厂商资料索引

以下链接均指向当前仓库文件；类名后的方法名可用于后续快速定位。

- **C1 插件注册**：[HanaDsPlugin.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/HanaDsPlugin.java)。
- **C2 展示过滤**：[DmHomeController.java](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/controller/system/DmHomeController.java)，`displayDsPlugin()`。
- **C3 SQL 能力**：[HanaSqlEngineSpi.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/sql/HanaSqlEngineSpi.java)、[HANA SQL 实现目录](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/sql/)。
- **C4 编辑器配置**：[QueryEditorController.java](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/controller/editor/query/QueryEditorController.java)，`loadDsLanguage()`。
- **C5 配置与驱动工厂**：[dsconf 目录](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/dsconf/)、[HanaDsFactory.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/dsfactory/HanaDsFactory.java)。
- **C6 会话与能力**：[HanaHooks.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaHooks.java)、[HanaSession.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaSession.java)、[HanaSupportSpi.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaSupportSpi.java)。
- **C7 层级与菜单**：[HanaMetaService.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaMetaService.java)、[HanaDsBrowseSpi.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/definition/ui/broswer/HanaDsBrowseSpi.java)。
- **C8 公共取消链路**：[DefaultRdbSession.java](../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/execute/session/rdb/DefaultRdbSession.java)，`getCurrentQueryId()`、`killCurrentQuery()`。
- **C9 元数据查询**：[HanaMetaProviderDm.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaMetaProviderDm.java)、[HanaUmiServiceDm.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaUmiServiceDm.java)。
- **C10 元数据转换**：[HanaMetaProviderUtils.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/execute/HanaMetaProviderUtils.java)，重点为 `mapToUkExt()`、`convertColumns()`。
- **C11 数据编辑**：[HanaDataEditorSpi.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/definition/ui/editor/data/HanaDataEditorSpi.java)、[HanaDataEditorUtils.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/definition/ui/editor/data/HanaDataEditorUtils.java)、[AbstractDialect.java](../../backend/clouddm-plugins/clouddm-ds/dsc-common/src/main/java/com/clougence/clouddm/dsfamily/schema/dialect/AbstractDialect.java)。
- **C12 DDL 与公共编排**：[HanaEditorProvider.java](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/definition/ui/editor/table/HanaEditorProvider.java)、[表结构生成目录](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/java/com/clougence/clouddm/ds/hana/definition/ui/editor/table/)、[TableEditorImpl.java](../../backend/clouddm-utils/cg-schema/src/main/java/com/clougence/schema/editor/builder/TableEditorImpl.java)。
- **C13 查询规则**：[SecRulesEngineImpl.java](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/detectrule/impl/SecRulesEngineImpl.java)，`openQueryCheck()`。
- **C14 血缘与脱敏消费端**：[QueryAnalysisServiceImpl.java](../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/analysis/impl/QueryAnalysisServiceImpl.java)，`lineageColumns()`、`configMasking()` 和两条 masking 分支。
- **C15 驱动与发布**：[ds-hana/build.gradle](../../backend/clouddm-plugins/clouddm-ds/ds-hana/build.gradle)、[drivers.xml](../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/resources/META-INF/clougence/drivers.xml)、[内置驱动清单](../../package/pkg/builtin-drivers/built-in-drivers.xml)。
- **C16 测试与规则**：[SqlTestSupport.java](../../tests/ds-test/src/test/java/com/clougence/clouddm/ds/SqlTestSupport.java)、[HANA 模板样本](../../tests/ds-test/src/test/resources/template/hana/)、[专项测试约定](../../tests/datasource/README.md)、[frontend/AGENTS.md](../../frontend/AGENTS.md)。
- **E1 SAP 官方客户端参考手册**：[Client 2.18 PDF](https://help.sap.com/doc/b01bb8ddc72e41f795034092b9bf541d/2.18/en-US/SAP_HANA_Client_Interface_Programming_Reference_en.pdf)，印刷页 439—441 为通信超时、连接超时、currentSchema 和 databaseName；印刷页 464 包含 JDBC trustStore/validateCertificate。资料于 2026-09-30 核对，结论需在最终选定驱动上实测。

## 本轮交付状态

已完成调研、第 1 步基线文档和第 2、3 步实现与本地验证。本轮代码修改限于 HANA 插件，公共 SDK 的提交/回滚改动已撤回；未修改前端源码、未新增测试类、未取消默认隐藏。公共事务失败反馈仍需单独修复，真实 HANA 环境及页面验收仍待部署；本轮未推进其他计划步骤。
