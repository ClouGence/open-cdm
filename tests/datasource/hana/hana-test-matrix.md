# HANA Platform 能力与验收矩阵

更新时间：2026-10-01。代码起点：`a9f16e78b8288c9a4158ee9df4378ee16b80021e`。环境组合、账号与 F1–F6 样本定义见 [README](README.md)；修复顺序及 C1–C16 源码链接见[15 步补齐计划](../../../docs/guides/hana-completion-plan.cn.md)。

**产品验收总体状态：BLOCKED（尚无真实 HANA 实例，CloudDM 页面服务未启动）。下表产品用例仍为 NOT RUN。** 第 2 步已构建插件并通过下文的本地配置、驱动、TLS 和超时检查；这些证据不替代 E1/E2 实库验收。未新增测试类。

## 状态和执行规则

| 状态 | 使用条件 |
| --- | --- |
| PASS | 已在明确组合、代码与制品版本上执行，该项所有约定断言均满足；产品能力必须有产品链路证据 |
| PARTIAL | 已执行且仅部分路径通过，逐项列出未覆盖/失败场景 |
| FAIL | 已执行并观察到不符合预期的结果；源码发现的问题不直接记为实测 FAIL |
| NOT RUN | 尚未执行；类存在、编译或相邻能力通过都不能代替 |
| BLOCKED | 明确缺少环境、账号、证书、制品或前置修复，并记录解除条件 |
| UNSUPPORTED | 已验证目标数据库/驱动不提供该能力并给出证据；项目漏实现不能按此关闭核心项 |

下表稳定 ID 对应验收项，后续添加子用例时用 `HANA-xxx/a` 等后缀。每项分别记录 E1/E2 结果，禁止用一个 PASS 覆盖两个组合。表中“步骤”为落地计划编号；源码 C 编号可在计划末尾定位实际文件与方法。

## 核心矩阵

| ID | 能力 / 产品入口 | 静态现状与实现位置 | 样本、角色和可观察断言 | 步骤 | E1 / E2 |
| --- | --- | --- | --- | --- | --- |
| HANA-001 | 驱动发现、下载、加载 | C15：2.28.6 已设默认；在线准备/隔离加载/离线恢复本地通过，内置包仍无 HANA 驱动 | 两驱动分别从产品加载；在线准备与离线准备可复现；核对实际 JAR 版本/摘要，无类加载冲突 | 2、14 | NOT RUN / NOT RUN |
| HANA-002 | 新增/编辑/测试连接 | C5：单端点 URL、database/schema 属性已补齐，序列化本地通过 | 正确与错误账号、tenant、schema、自定义 URL；保存重开字段一致；alone 和执行侧解码一致，URL/日志无凭据泄露 | 2 | NOT RUN / NOT RUN |
| HANA-003 | 连接及读取超时 | C5、H03：两个驱动实测按毫秒；字符串属性与范围校验已修正 | 不可达端口、慢连接、查询阻塞分别验证；耗时符合配置且有误差记录；错误可见、线程连接释放 | 2、4 | NOT RUN / NOT RUN |
| HANA-004 | TLS 与证书校验 | C5：TLS 模式/属性转换已补齐，本地合法与错误证书检查通过 | 合法 CA/主机名成功，错误 CA/主机名失败；配置往返；双向 TLS 若声明支持也需对应证书用例 | 2 | NOT RUN / NOT RUN |
| HANA-005 | SSH 转发 | C5：声明支持；已约束 SSH 的证书主机名及自定义 URL，实际隧道待测 | 跳板可达/不可达、隧道断开、SSH 与 TLS 组合；不能泄露密钥；失败终态和隧道释放 | 2、4 | NOT RUN / NOT RUN |
| HANA-006 | database/schema 上下文 | C6/C7：第 3 步已回读实际租户、核对请求上下文并转义 schema；结果列来源改用 JDBC schema | F2；租户与当前 schema 回读一致；特殊名称可切换；A/B 同名对象不串；不支持的 catalog 切换明确拒绝 | 3、5 | NOT RUN / NOT RUN |
| HANA-007 | 查询、结果与异常 | C6/C11：Session、结果转换及异常 SPI 已有实现 | F1/F3；SELECT、空集、NULL、DML affected rows、无效 SQL/约束冲突；结果类型/列来源正确，反馈可理解 | 3、5、11 | NOT RUN / NOT RUN |
| HANA-008 | 自动提交、事务及隔离 | C6：第 3 步收敛隔离声明，补齐只读设置；公共提交/回滚保留日志处理，失败反馈待统一修复 | F1、DATA_RW；双连接观察 commit/rollback、DDL 边界及支持的隔离级别；未支持能力 UI 和服务端一致 | 3 | NOT RUN / NOT RUN |
| HANA-009 | 精确取消及失败反馈 | C6/C8、H05：第 4 步已修复当前连接 ID、失败反馈及迟到/重复取消；本地探针通过 | F6，两会话；只取消目标；权限拒绝可见；重复/已完成取消收敛；取消后事务与连接状态正确 | 4 | NOT RUN / NOT RUN |
| HANA-010 | 断网、关闭、流式中止与重连 | C6/C8：第 4 步已修复部分释放路径、关闭透明重连；公共队列/批次终态单独处理，实库与页面待验收 | F6；资源回到基线；无重复执行；未知结果写入不盲目重试；重新打开会话上下文正确 | 4、11 | NOT RUN / NOT RUN |
| HANA-011 | 对象树与权限过滤 | C7/C9：第 5 步补齐程序对象分组与序列/同义词详情、视图列路由；权限和页面待验收 | F2/F4、OWNER/RO/LIMITED；表/视图/序列/同义词/过程/函数/触发器列表详情闭环；单对象失败不使整树失败 | 5、13 | NOT RUN / NOT RUN |
| HANA-012 | 列、类型、默认值与 identity | C9/C10：第 5 步修正别名、维度/NULL、identity；未知列明确报错，复杂类型结构待实库核对 | F3；精度长度、nullable、comment、default、identity 与数据库一致；未知类型不能伪装 | 5 | NOT RUN / NOT RUN |
| HANA-013 | PK/UK/FK/索引元数据 | C9/C10、H07：第 5 步修正 UNIQUE 聚合、三字段关联和 FK 规则；本地探针通过 | F2；两条 UNIQUE 独立；复合列顺序一致；跨 schema 同名索引不混合；FK 规则正确 | 5、10 | NOT RUN / NOT RUN |
| HANA-014 | 程序对象及参数 | C9、H06：第 5 步修正过程绑定、参数方向和返回识别；表参数嵌套列/复杂触发器仍有边界 | F4；无参数函数仍出现；IN/OUT、返回类型、过程/触发器详情与真实定义一致 | 5、10 | NOT RUN / NOT RUN |
| HANA-015 | 编辑器能力加载、DSL | C3/C4：dslProvider 抛异常 | 页面可打开并加载 HANA 语言能力；能力声明与实际接口一致；不以返回 null 代替完整能力 | 6、7、13 | NOT RUN / NOT RUN |
| HANA-016 | SQL 与 SQLScript 拆句 | C3：复用 SQL:2003 | F5；字符串/注释/块内分号不误切；DDL、DML、CALL/过程体/匿名块执行单元正确 | 6 | NOT RUN / NOT RUN |
| HANA-017 | 补全、校验、格式化、资源 | C3：Language/Resource 存在，校验为空结果 | F2/F5；关键字/授权对象建议、位置/错误信息准确；格式化保语义；声明能力均可用 | 7、13 | NOT RUN / NOT RUN |
| HANA-018 | 查询限制与分页改写 | C3：TOP/LIMIT 实现待实测 | F5；已有 TOP/LIMIT、排序、CTE、UNION、锁子句、多语句；结果上限不绕过，改写后语义一致 | 7 | NOT RUN / NOT RUN |
| HANA-019 | SQL 行为分类与对象授权 | C3/C13：通用分析加正则，无安全域实现 | F5；平台只读配 DB RW、无授权对象、跨 schema/子查询/MERGE/CALL；正确识别读写并拒绝越权 | 8 | NOT RUN / NOT RUN |
| HANA-020 | SQL 审核规则 | C1/C3/C13：规则未注册，null resolver 跳过查询规则 | F5；允许/提示/阻断按规则生效；不可解析与动态 SQL 不自动视为审核通过 | 8 | NOT RUN / NOT RUN |
| HANA-021 | 列血缘 | C3/C14：lineage EMPTY | F5；别名、JOIN、CTE、表达式、视图、星号展开回溯正确；未知来源明确记录 | 9 | NOT RUN / NOT RUN |
| HANA-022 | 脱敏与导出权限 | C14：存在无来源处理分支，不等于全量失效 | 合成敏感列；普通/豁免用户对查询、数据编辑读取、导出分别验证；复杂和未知来源不解除保护 | 9、11 | NOT RUN / NOT RUN |
| HANA-023 | 建表、改表、删表与 DDL 转换 | C12：Editor/UI/DDL SPI 已有实现 | F1/F2/F3；OWNER 页面预览→执行→刷新；列顺序、注释设置/清空、默认值/identity/约束一致 | 10 | NOT RUN / NOT RUN |
| HANA-024 | 索引编辑 | C12：公共编排负责删除/重建 | F2；增加/删除/重排索引列后回读；失败提示与真实结构一致；无错误跨 schema 操作 | 10 | NOT RUN / NOT RUN |
| HANA-025 | 对象脚本、模板与详情 | C7/C9/C12：requestObjectScript 抛异常；Template SPI 已有实现 | F4；各纳入对象列表→详情→取脚本→编辑/创建→刷新，脚本可在隔离 schema 回放且保留语义 | 10、13 | NOT RUN / NOT RUN |
| HANA-026 | 表数据新增、修改、删除 | C11：表统一使用隐藏 rowid，视图路径不同 | F1；单/复合/无 PK、行/列存、重复行；只改变选定目标、受影响数正确；视图编辑边界明确 | 11 | NOT RUN / NOT RUN |
| HANA-027 | 数据值转换与批量失败 | C11：专用转换 helper 未见 SPI 接入 | F3、DATA_RW/RO；高精度/时间/Unicode/二进制/LOB/NULL 往返；批次错误不伪报全部成功 | 11 | NOT RUN / NOT RUN |
| HANA-028 | 导入导出 | C11 及公共 schema/import/export 链路 | F1/F3；页面数据导出再导入隔离表，行数/关键值一致；权限、取消、断网、资源释放与错误记录 | 11 | NOT RUN / NOT RUN |
| HANA-029 | 执行计划 | C6：Explain SPI/Session 与 EXPLAIN_PLAN_TABLE 路径 | 同时两查询计划不串；成功/失败后清理；无权限可见；页面对应原查询，不执行待解释写操作 | 12 | NOT RUN / NOT RUN |
| HANA-030 | 工单与 CI/CD | C1/C3：依赖公共执行、分析、审核链 | F1/F5；提交→审核→执行→结果，失败/重试/部分成功状态一致，不重复写入 | 12 | NOT RUN / NOT RUN |
| HANA-031 | 页面能力、菜单、国际化 | C1/C7：部分能力/菜单缺失，插件默认隐藏 | @Browser；可用入口闭环，未支持操作准确提示；中英文与移动/桌面布局；无假按钮或静默空结果 | 13 | NOT RUN / NOT RUN |
| HANA-032 | 发布包和部署模式 | C15：插件模块存在，驱动另行准备 | alone、console/sidecar 包分别安装/重启，校验插件/驱动摘要；已有连接可用，共享改动回归相邻数据源 | 14 | NOT RUN / NOT RUN |
| HANA-033 | 默认开放与既有配置 | C1/C2：display=false，首页过滤 | 前置核心项通过后；从默认入口新增→查询→管理/治理冒烟；旧配置、重载/重启可用 | 15 | NOT RUN / NOT RUN |

## 需要明确边界的项目

这些项目不能随意写成“不支持”或“不适用”。纳入核心产品承诺后，必须添加子用例并通过；范围调整需记录依据和产品决策。

| ID | 项目 | 本轮边界及后续处理 | 关联步骤 | 状态 |
| --- | --- | --- | --- | --- |
| HANA-034 | 系统库/租户管理、备份恢复、HA/集群 | 首轮为 tenant 内数据库管理；SYSTEMDB 仅用于准备诊断。未承诺数据库运维控制台或 HA 认证 | 1、14 | 未纳入首轮产品目标 |
| HANA-035 | Calculation View、HDI、分析/空间特殊对象 | 保留可识别元数据和只读查询的评估项；创建/图形编辑范围需第 5/10 步结合产品能力定界，未知类型不得伪映射 | 5、10、13 | 待定界、NOT RUN |
| HANA-036 | 空间等特殊类型的数据编辑 | 第 5/11 步逐类型决定读取、编辑、导出的契约；不能因普通字符串测试通过就宣称全类型支持 | 5、11 | 待定界、NOT RUN |
| HANA-037 | readOnly、连接内 catalog 切换、隔离级别 | 第 3 步已声明只读支持、拒绝连接内 catalog 切换及 READ UNCOMMITTED；页面和数据库效果仍需实测 | 3、13 | 待实测、NOT RUN |
| HANA-038 | HANA Cloud | E4 单独评估；不继承 E1/E2 结论，Platform 发布不等待 Cloud | 独立后续评估 | 未纳入本轮验收 |

## 阻塞项与关闭条件

| 编号 | 当前阻塞 | 解除条件 |
| --- | --- | --- |
| B1 | 未提供实际 Express 实例、账号和执行侧网络路径 | README 环境表已回填、版本回读、tenant 登录、角色与隔离 schema 就绪 |
| B2 | 未获得 TLS/SSH 测试条件 | CA/主机名和跳板就绪，证书错误/隧道失败可控；只影响相关用例，不能将其记为通过 |
| B3 | 连接实库验收待环境；取消、元数据、DSL 和治理等仍有已知实现缺口 | 第 2—4 步本地证据见下文；对应计划步骤完成后使用新的代码/制品验收，静态修复不直接关闭实测项 |
| B4 | 默认隐藏，无已验收的 HANA 页面流程 | 隔离环境使用 README 中的直达入口执行 @Browser 流程；当前页面服务未启动，第 15 步才默认开放 |
| B5 | 特殊对象/类型范围未定 | HANA-035/036 逐项给出范围与证据；正式支持说明与矩阵一致，不静默漏项 |

## 证据记录模板

目前没有 E1/E2 产品执行记录；本地检查记录见下节。后续在本文件持续记录简短证据，生成日志放临时目录或约定存储，不入库；长期页面流程维护在 `tests/frontend/datasource/hana_datasource.md`。

```text
用例 ID / 子用例：
执行时间 / 环境组合（E1 或 E2）：
服务器完整版本 / 驱动版本及 JAR SHA-256：
代码 commit / 未提交改动摘要 / 应用和插件 SHA-256 / JDK：
CloudDM 部署模式 / 产品入口 / 长期复测流程位置：
CloudDM 权限角色 / 数据库角色 / 样本对象：
前置条件 / 操作 / 预期断言：
实际结果 / 状态 / 尚未覆盖项：
脱敏后的错误码、结果摘要或证据位置：
资源释放、事务终态和样本清理结果：
```

第 14 步收口时：E1/E2 的 HANA-001–032 均有证据、无未关闭 FAIL/BLOCKED/NOT RUN；PARTIAL 必须补齐；额外保留的驱动版本独立补测。边界项已定界，测试环境限制不能伪装成实现完整。第 15 步执行最终开放用例 HANA-033；该项在开放前保留 NOT RUN，开放后不通过则仍不得认定完成。


## 第 2 步本地证据（不等同于产品用例 PASS）

本地执行环境：macOS、Homebrew OpenJDK 17.0.18；代码为上述基线加本步工作区改动。使用临时 JShell 探针和短期合成证书，本机回环端点不实现 HANA SQL 协议，未使用真实数据库凭据。驱动 JAR 摘要见 README。

| 检查 | 结果与证据边界 |
| --- | --- |
| 构建与打包 | `:ds-hana:build`、`:ds-hana:customFatJar` 成功；HANA 自身没有测试源，不能将该 build 描述为 HANA 集成测试通过 |
| 既有公共验证 | `:dsc-common:test` 的 DsSslSupportTest 16 项、`:cg-drivers:test --tests com.clougence.drivers.factory.DefaultDriverLoaderFunctionalTest` 26 项，0 failures / 0 errors |
| 默认版本、在线准备和隔离加载 | 使用实际 DefaultDriverLoader/MavenResourcePreparer 下载 2.22.12、2.28.6；按插件 includePackages 隔离加载对应 JDBC 与 HanaSslProperties，版本之间 driver Class 不相同，生成 files.idx |
| 离线恢复 | 将完整 family/version 目录复制到新临时根目录；新 loader 恢复索引和两个驱动，未重新下载 |
| 配置/序列化 | 两驱动检查超时、数据库名、schema、autoCommit 原生属性；新增字段 JSON 往返；执行侧证书路径不被序列化；URL 参数/凭据、非法端口、整数溢出、小数、负数被拒绝 |
| TLS 握手 | 两驱动均通过 CA + 主机名、PEM 双向 TLS、JKS TrustStore + PKCS12 客户端 KeyStore；拒绝错误 CA、错误主机名、不匹配私钥、通配符及缺少 CA；显式证书主机名路径通过 |
| 超时和失败释放 | 本地 TCP 接受后不响应，700 毫秒设置分别约 712/711 毫秒报错；对端观察到 socket 关闭；不能据此宣称真实查询中止/取消或第 4 步全部资源路径已通过 |
| 插件制品 | `ds-hana-lib.jar` 含新 TLS helper 和 i18n，不含 `com/sap/db/jdbc`；SHA-256 为 `3003680f646f3b0df2968d623950634b73da3a639f59d546384b0b218674c65c` |
| @Browser | 访问 `http://localhost:8222` 得到 `ERR_CONNECTION_REFUSED`；页面行为及真实持久化未验证 |
| 真实 HANA / SSH | 无实例、证书部署和跳板；登录、租户路由、CA 服务端配置、真实隧道及节点部署仍 BLOCKED |

TLS 探针中的合法连接在握手后因为对端没有 HANA 协议而失败，这是预期的测试边界；成功依据是 TLS 会话与客户端身份握手证据。错误主机名还核对驱动的 `Host name verification failed`，不只依赖服务端握手状态。后续 E1/E2 要在真实实例重复相关正反例，再执行页面测试连接与保存重开。

2026-10-01 校验职责调整：`HanaConfig.asDriverProperties()` 仅转换属性；URL/SSH 冲突由 `HanaDsFactory` 校验，SSH 证书主机名由 `HanaSslProperties` 校验。SSH 标记在生成 JDBC 属性时移除。重新构建插件通过；两个驱动版本的定向检查均确认属性转换不拒绝这两种组合、连接创建时仍拒绝，DISABLED/TRUST 不要求证书主机名，直连允许省略证书主机名覆盖。未重跑此前完整 TLS 握手矩阵，实库/页面状态不变；上表插件摘要已更新。

## 第 3 步本地证据（2026-10-01）

代码基线：`e45aec69570c92e52cc8c7e371c7834f616ac7f2` 加本轮工作区变更。第 2 步的制品摘要是历史快照；公共 SDK 的异常上抛改动经调用链复核后已撤回，当前只交付 HANA 插件修改。

| 检查 | 结果与边界 |
| --- | --- |
| 构建 | `:ds-hana:build`、`:ds-hana:customFatJar` 成功；HANA 无测试源，不能解释为实库集成测试通过 |
| 公共模块既有测试 | `:cgdm-plugin-sdk:test` 4 项、`:dsc-common:test` 16 项通过；分别为 SCM 工具/SSL 测试，并非事务专项测试 |
| 会话定向验证 | 临时 JShell + JDBC 动态代理：租户回读/错配拒绝、schema 引号/大小写/限定名、列来源、四个隔离选项、服务端拒绝 READ UNCOMMITTED、只读初始化及切换、类型化位置参数/NULL 绑定均通过 |
| 事务风险验证 | 提交/回滚恢复原有日志处理、不向调用者抛异常的行为；失败保留待提交标记，成功后清除；自动提交/隔离/只读切换失败保留原状态；schema 失败回读原值；初始化错配关闭连接；新连接重新应用传入上下文。未访问真实数据库，不能据此认定并发可见性或断网恢复通过 |
| 驱动核对 | ngdbc 2.22.12/2.28.6 的 schema 设置及 READ ONLY/READ WRITE 路径均存在；列元数据 getCatalogName 返回空、getSchemaName 返回 schema。用于修正接口选择，不是两组服务器兼容性认证 |
| 页面 | @Browser 本轮访问 localhost:8222 仍返回 ERR_CONNECTION_REFUSED；未执行具体页面流程 |
| 实库门禁 | HANA-006、008、037 仍 NOT RUN；尤其双连接提交/回滚可见性、只读写入拒绝、隔离并发、DDL 隐式提交和刷新/重连必须实际执行 |

当前构建制品 SHA-256：

```text
ds-hana-lib.jar    544bf67061b2e9c2ec50cf236b82c400a9d35969a926f886b6ee28c0eaaf3b61
cgdm-plugin-sdk-4.3.0.jar    c54b88071ae7365410b9496485b3055779f3d22593811a26b43796cf9390babd
```

手工实库验收场景见 [README 第 3 步](README.md#8-第-3-步会话上下文与事务语义)。未新增测试类；未执行全量打包或部署。

公共层复核结论：回滚被 AutoExecJob 异常清理调用，单点增加 throw 会覆盖原始错误并改变任务终态路径，因此已撤回；主动操作错误反馈也尚未闭环。原先“异常向上传递”的探针结果仅适用于已撤回的临时版本，不能作为当前交付证据。后续范围见 README 的“公共事务错误契约待办”。

## 第 4 步本地证据（2026-10-01）

基线 `5719fb22b08fcd1d42983888d5553087d4cccf60` 加本步变更；以下均为构建或本地代理验证，不是 E1/E2 产品用例 PASS。

| 检查 | 结果与限制 |
| --- | --- |
| HANA 构建/插件包、SDK、sidecar | BUILD SUCCESSFUL；SDK 4 项及 dsc-common 16 项现有测试通过；sidecar/HANA test 为 NO-SOURCE |
| 精确定位、空闲取消 | 初始化使用 CURRENT_CONNECTION，空闲时不创建取消连接；活动查询取消只携带自己的 ID |
| 慢连接与重复取消 | CountDownLatch 阻塞临时连接创建；重复取消不重复创建；原查询完成后跳过取消，关闭临时连接；取消仍在进行时新查询明确失败且没有执行 SQL |
| 取消失败及资源 | 注入权限错误，保留 SQLException 原因并经 ThirdPartyApiException 上报；临时 Statement/Connection 均 close，原事务标记不被清空 |
| 查询、流式及初始化释放 | 普通/EXPLAIN Statement 设置失败调用 close；close 二次错误不覆盖首错；流式取消返回后不再 next，ResultSet/Statement 均 close |
| 事务与断线 | 注入 139 并叠加状态刷新失败，仍清除待确认标记；SQLState 08006 关闭旧连接且保留未知事务状态；元数据 callback 断线同样关闭 |
| 执行标记与后台提示 | 查询失败后 isExecuting=false；计时器拒绝调度原样向外抛出且复位执行标记；结束后的定时回调不再发送/续订等待提示 |
| sidecar 关闭 | 取消/回滚通知分别失败仍关闭底层会话并结束结果构建；主异常保留，关闭错误 suppressed；不修改队列、批次终态及通知流程 |
| 第 3 步回归 | schema/tenant、隔离、只读、参数、提交/回滚原契约、初始化释放和新会话上下文探针通过 |
| 环境边界 | ngdbc 2.22.12、2.28.6 类路径下取消代理检查通过；没有 HANA 服务端；localhost:8222 连接拒绝，页面和真实网络取消未验证 |

临时探针和日志位于 `/private/tmp/hana-cancel-verify.jsh`、`/private/tmp/hana-agent-close-verify.jsh`、`/private/tmp/hana-step4-*.log`，未加入仓库。复跑场景与常规构建命令见 [README 第 4 步](README.md#9-第-4-步取消与资源释放)。公共层修改影响其他关系型数据源的异常清理与关闭流程；20 项现有测试覆盖 SCM/SSL，并不覆盖公共生命周期，临时探针也未覆盖完整审计链路。未运行全量数据库集成验收或部署。

公共层范围复核后，AbstractDsSession 恢复原有完成方法及异常传播，仅补充调度失败的复位和异常钩子失败时的清理；SessionAgent 撤回队列/终态调整，保留关闭必达。旧探针中的队列清空和拒绝复用结论不适用于当前版本，此问题转为独立待办，见 README。收窄后的构建及探针日志为 `/private/tmp/hana-step4-narrow-*.log`。

本次制品 SHA-256（公共层收窄后重新构建，部署需使用同一构建）：

```text
99ff9d2b48840be26aa66ce80b69bdcb2a3d96937d47fe118e5bb63a8f79e8fe  ds-hana-lib.jar
2651ad66b477a771e918126dda42b48c6e2f823e6411a9ac31a69b50793bee9c  cgdm-plugin-sdk-4.3.0.jar
71dee18308ecc56b60dcda5bdcde458e47cddeb19c0962731aeb989dc072a64d  cgdm-sidecar-4.3.0.jar
```

## 第 5 步本地证据（2026-10-01）

- 基于 `7e8b4d83b1a882b9eb20f760cff6cb3cb426bf8e` 的本轮工作区修改；范围为 HANA 元数据、树分组和 `HanaTypes`，没有公共会话或前端源码变更。
- `:ds-hana:build :ds-hana:customFatJar :cg-schema:test :dsc-common:test` 离线通过；dsc-common 16 项既有测试零失败，ds-hana/cg-schema 无测试源码。
- `/private/tmp/hana-step5-probe.jsh`：ngdbc 2.22.12 / 2.28.6 类路径各输出 `HANA_STEP5_PASS assertions=72`。覆盖范围见 README 第 10 节；临时探针不纳入源码且不依赖真实数据库，不构成驱动协议/SQL/页面验收。
- 构建日志 `/private/tmp/hana-step5-build.log`；探针日志 `/private/tmp/hana-step5-probe-2.22.12.log`、`/private/tmp/hana-step5-probe-2.28.6.log`。临时目录证据可能被清理，长期复测以 README 场景和本矩阵为准。
- `ds-hana-lib.jar` SHA-256：`0c29b5e992db99ccc154ed670092d798aa39de69ae59674e735756119ebfc025`。
- `cg-schema-4.3.0.jar` SHA-256：`3b71951cb6669786d42808dfd6504c94411be573c8b416ba1ed55dfc4970ff50`。插件不打入 HanaTypes，需更新包含该 jar 的平台包并重启加载。
- @Browser 本地入口返回 `ERR_CONNECTION_REFUSED`；真实 HANA、系统视图普通账号可见性和页面全部未执行，HANA-011～014 保持 NOT RUN。Cloud 不在本轮验收范围。

- 数据库上下文复核：列表、详情及列入口不匹配时统一抛出 `ThirdPartyApiException`（`CONFIG_HANA_CATALOG_MISMATCH`）；补充 20 条错误路径及 3 条正常/空结果断言，错误路径不得继续查询对象。修正后的 HANA 构建和打包通过，日志 `/private/tmp/hana-catalog-check-build.log`。
