# HANA Platform 能力与验收矩阵

更新时间：2026-09-30。代码起点：`a9f16e78b8288c9a4158ee9df4378ee16b80021e`。环境组合、账号与 F1–F6 样本定义见 [README](README.md)；修复顺序及 C1–C16 源码链接见[15 步补齐计划](../../../docs/guides/hana-completion-plan.cn.md)。

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
| HANA-006 | database/schema 上下文 | C6/C7：catalog 返回 null、schema 原样拼接 | F2；租户与当前 schema 回读一致；特殊名称可切换；A/B 同名对象不串；不支持的 catalog 切换明确拒绝 | 3、5 | NOT RUN / NOT RUN |
| HANA-007 | 查询、结果与异常 | C6/C11：Session、结果转换及异常 SPI 已有实现 | F1/F3；SELECT、空集、NULL、DML affected rows、无效 SQL/约束冲突；结果类型/列来源正确，反馈可理解 | 3、5、11 | NOT RUN / NOT RUN |
| HANA-008 | 自动提交、事务及隔离 | C6：声明全部隔离枚举，需核对 | F1、DATA_RW；双连接观察 commit/rollback、DDL 边界及支持的隔离级别；未支持能力 UI 和服务端一致 | 3 | NOT RUN / NOT RUN |
| HANA-009 | 精确取消及失败反馈 | C6/C8、H05：query ID 未限定当前连接，异常被忽略 | F6，两会话；只取消目标；权限拒绝可见；重复/已完成取消收敛；取消后事务与连接状态正确 | 4 | NOT RUN / NOT RUN |
| HANA-010 | 断网、关闭、流式中止与重连 | C6/C8：需追踪公共生命周期 | F6；资源回到基线；无重复执行；未知结果写入不盲目重试；重新打开会话上下文正确 | 4、11 | NOT RUN / NOT RUN |
| HANA-011 | 对象树与权限过滤 | C7/C9：树分组与详情不齐 | F2/F4、OWNER/RO/LIMITED；表/视图/序列/同义词/过程/函数/触发器列表详情闭环；单对象失败不使整树失败 | 5、13 | NOT RUN / NOT RUN |
| HANA-012 | 列、类型、默认值与 identity | C9/C10：类型转换存在未知值路径 | F3；精度长度、nullable、comment、default、identity 与数据库一致；未知类型不能伪装 | 5 | NOT RUN / NOT RUN |
| HANA-013 | PK/UK/FK/索引元数据 | C9/C10、H07：多 UNIQUE 合并、索引关联范围风险 | F2；两条 UNIQUE 独立；复合列顺序一致；跨 schema 同名索引不混合；FK 规则正确 | 5、10 | NOT RUN / NOT RUN |
| HANA-014 | 程序对象及参数 | C9、H06：过程参数绑定颠倒；函数结果待核对 | F4；无参数函数仍出现；IN/OUT、返回类型、过程/触发器详情与真实定义一致 | 5、10 | NOT RUN / NOT RUN |
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
| HANA-037 | readOnly、连接内 catalog 切换、隔离级别 | 当前声明与 HANA Platform 实际能力逐项比对；确认不支持后 UI 禁用且服务端拒绝，不能只隐藏入口 | 3、13 | 待实测、NOT RUN |
| HANA-038 | HANA Cloud | E4 单独评估；不继承 E1/E2 结论，Platform 发布不等待 Cloud | 独立后续评估 | 未纳入本轮验收 |

## 阻塞项与关闭条件

| 编号 | 当前阻塞 | 解除条件 |
| --- | --- | --- |
| B1 | 未提供实际 Express 实例、账号和执行侧网络路径 | README 环境表已回填、版本回读、tenant 登录、角色与隔离 schema 就绪 |
| B2 | 未获得 TLS/SSH 测试条件 | CA/主机名和跳板就绪，证书错误/隧道失败可控；只影响相关用例，不能将其记为通过 |
| B3 | 连接实库验收待环境；取消、元数据、DSL 和治理等仍有已知实现缺口 | 第 2 步本地证据见下文；对应计划步骤完成后使用新的代码/制品验收，静态修复不直接关闭实测项 |
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
