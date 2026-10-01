# HANA Platform 验证环境与复跑入口

更新时间：2026-10-01。对应[HANA 补齐计划](../../../docs/guides/hana-completion-plan.cn.md)。

**第 1 步基线已确定，第 2 步连接/TLS 与第 3 步会话/事务实现及本地验证已完成；真实实例和页面验收仍待环境。**后续结果持续维护在[能力矩阵](hana-test-matrix.md)，不按执行日期另建报告。

## 1. 固定验证基线

| 项目 | 本轮决定 | 核实状态 |
| --- | --- | --- |
| 产品主线 | SAP HANA 2.0 Platform；首个验证环境使用 Express server-only，单节点、单业务 tenant | 已确定范围，不包含 Cloud |
| 首选服务器 | HANA Express SPS 08 / revision 088，镜像 `saplabs/hanaexpress:2.00.088.00.20251110.1` | 官方标签已核对，运行版本待回读 |
| 镜像锁定 | `saplabs/hanaexpress@sha256:432be2785506e97cefc413f495393fc22fcdda034c2c80db2f0f88efeb16dfaf`，`linux/amd64` | 官方 manifest 已核对，本地拉取与启动未验证 |
| 主验证驱动 | `com.sap.cloud.db.jdbc:ngdbc:2.28.6` | 已设为默认；本地 Maven 准备与隔离加载通过，部署环境实际加载待核实 |
| 驱动回归基线 | `com.sap.cloud.db.jdbc:ngdbc:2.22.12` | 与当前 compileOnly 版本一致；本地连接/TLS 检查通过，尚未认证 HANA 实库兼容 |
| open-cdm 源码起点 | `a9f16e78b8288c9a4158ee9df4378ee16b80021e` | 当前静态盘点基线，后续每轮记录实际 commit 与未提交改动 |
| 首个应用部署 | alone 模式，JDK 17；具体发行商、patch 和构建产物摘要在首次运行时锁定 | 待准备；console/sidecar 在第 14 步补测 |
| 构建工具 | 仓库 Gradle Wrapper 9.5.0；涉及前端时 Node.js 22.22.1、npm lockfile | 工程基线，未在本步构建 |

服务器标签、架构和摘要来自 [SAP 官方 Docker 镜像标签页](https://hub.docker.com/r/saplabs/hanaexpress/tags)及[该标签的 manifest](https://hub.docker.com/layers/saplabs/hanaexpress/2.00.088.00.20251110.1/images/sha256-432be2785506e97cefc413f495393fc22fcdda034c2c80db2f0f88efeb16dfaf)。SPS 08 产品线参见 [SAP 版本与实例说明](https://developers.sap.com/tutorials/hana-clients-choose-hana-instance.html)。以上于 2026-09-30 核对；固定摘要用于复现，不跟随 `latest`。

驱动选择依据为当前 [drivers.xml](../../../backend/clouddm-plugins/clouddm-ds/ds-hana/src/main/resources/META-INF/clougence/drivers.xml) 和 [build.gradle](../../../backend/clouddm-plugins/clouddm-ds/ds-hana/build.gradle)。第 2 步已将 2.28.6 标记为默认版本，编译依赖保持不变；不将“列在驱动清单中”当作已支持。

| 组合 ID | 服务器 × 驱动 | 必须完成的验证 | 当前结论 |
| --- | --- | --- | --- |
| E1 | 上述 Express revision 088 × ngdbc 2.28.6 | 专项矩阵全部 Platform 核心项，先直连，再 TLS/SSH；alone 全流程 | BLOCKED：缺少运行实例和产品验收包 |
| E2 | 同一 Express 实例 × ngdbc 2.22.12 | 同一核心矩阵回归，重点核实类型、事务、取消、TLS、驱动类加载；与 E1 分别记录 | BLOCKED：同 E1；不能由 E1 通过推定通过 |
| E3 | 其他 HANA Platform SPS/revision 或企业部署拓扑 | 增加精确版本和独立结果后才能声明支持 | 未纳入首轮认证；Express 结果不能证明 HA、多节点或全部企业能力 |
| E4 | SAP HANA Cloud | 独立核对端点、TLS、认证、SQL/对象、系统视图和权限 | 单独评估，不作为 Platform 发布门禁 |

仓库还列出 `2.23.10`、`2.24.8`、`2.25.13`、`2.26.12`、`2.27.7`。它们暂为未验证组合；第 14 步若保留为正式支持版本，应补测并单列结果，不能用两端版本通过代表全部中间版本。服务器或驱动改变后新建组合 ID，不覆盖旧证据。

## 2. 部署准备

建议使用一台独立 Linux x86_64 主机运行上述 server-only 镜像，先不安装 XS Advanced。CloudDM 通过 JDBC 访问数据库，不需要 SAP 的应用服务层。所选官方镜像是 amd64，不把 Apple Silicon 上的模拟运行作为首轮基线。

为本项目的小规模验证预留 **4 vCPU、16 GiB 内存、120 GiB 可用持久磁盘**；这是项目容量预算，不是 SAP 最低要求或所有负载的保证。官方 server-only 指引列出最低 8 GB、推荐 12 GB 内存，支持的 Linux 和其他安装条件以所选版本安装说明为准。参见 [SAP server-only 部署指引](https://developers.sap.com/tutorials/hxe-database-server)和[Docker 安装流程](https://developers.sap.com/tutorials/hxe-ua-install-using-docker/)。旧教程中的示例镜像不要替代上面的固定标签。

部署分两批准备：

- 首批：数据库持久卷、业务 tenant、从 CloudDM **执行侧**可达的 SQL 端口、管理及受限测试账号。先做直连排障；管理账号仅用于准备和诊断。
- 第 2 步连接验收前：TLS 服务端证书、可验证主机名和 CA；SSH 验证用跳板机。未准备的场景保留 BLOCKED，不为通过测试关闭证书校验。

以 `HXE` 为预期业务 tenant 名，默认 schema 使用下文的测试 schema。不要把 SYSTEMDB 当成业务库，也不要直接套用插件默认 `30015`。SAP 示例中 SYSTEMDB 为 `39013`、HXE 为 `39015`，实际值需由部署结果确认，容器端口映射也要记录。端口辨识依据见 [SAP Express 连接说明](https://developers.sap.com/tutorials/hana-clients-choose-hana-instance.html)。

由管理员在 SYSTEMDB 只读检查 SQL 服务端口：

```sql
SELECT * FROM SYS_DATABASES.M_SERVICES WHERE SQL_PORT != 0;
```

连接业务 tenant 后记录以下结果；受限账号若无权读取监控视图，由管理员补录版本信息，不为探测版本扩大其权限：

```sql
SELECT DATABASE_NAME, VERSION FROM SYS.M_DATABASE;
SELECT CURRENT_USER, CURRENT_SCHEMA FROM DUMMY;
```

首次实际环境登记表如下。部署者补齐后，才可将 E1/E2 从环境阻塞转入执行；当前所有“待登记”均表示未获得真实信息。

| 环境字段 | 实际值 |
| --- | --- |
| 环境别名、部署方式、OS/kernel、CPU/内存/持久磁盘 | 待登记 |
| 已拉取镜像 RepoDigest、容器 ID、服务器完整 VERSION | 待登记 |
| SYSTEMDB 与 tenant 名、SQL 内部端口、映射端口 | 待登记 |
| 执行侧网络路径、TLS 主机名/CA 指纹、SSH 环境别名 | 待登记；敏感地址按团队约定保存在本地环境配置 |
| CloudDM commit/工作区差异、alone 或 console/sidecar、JDK 完整版本 | 待登记 |
| 应用包、HANA 插件包、实际 JDBC JAR 的版本与 SHA-256 | 待登记；仅声明依赖不代表已加载 |
| 数据库账号角色、CloudDM 用户权限、测试 schema 前缀 | 待登记；不登记密码、私钥或完整带凭据 URL |
| 实例版本回读、账号登录和环境清理检查 | 待执行 |

本目录不保存厂商二进制、License、数据库数据、真实凭据和生成日志。如后续维护容器配置，放入 `tests/dbs/`；本步不新增或执行部署脚本。

## 3. 账号与权限维度

以下是测试角色契约，名称为逻辑别名，不代表账号已经创建。实际 GRANT/REVOKE 在固定实例上核对，并随相关能力实现记录精确权限；不要猜测权限名或一律授予管理员权限。

| 数据库角色 | 权限边界 | 用途 |
| --- | --- | --- |
| SETUP_ADMIN | 创建/清理测试账号和隔离对象；读取必要服务信息 | 准备样本、回读版本；不作为普通页面验收身份 |
| OBJECT_OWNER | 仅拥有本轮两个测试 schema 及其对象的管理权限 | 表/视图/序列/程序对象 DDL 与脚本往返 |
| DATA_RW | 指定样本的 SELECT/INSERT/UPDATE/DELETE；按场景单独授予 EXECUTE | 数据编辑、事务、导入导出；无全局 DDL 权限 |
| DATA_RO | 仅允许指定 schema 内样本 SELECT，另一 schema 不授权 | 浏览、查询、跨 schema 拒绝、写入拒绝 |
| LIMITED | 可以登录但无样本对象访问权限 | 对象不可见、错误反馈和权限拒绝 |
| CANCEL_OBSERVER | 单独核实会话定位/取消所需最小权限；并保留无此权限的对照身份 | 第 4 步取消成功与权限拒绝，不能静默当作成功 |

CloudDM 用户另外区分：数据源管理员、授权只读用户、授权写入用户、无该数据源权限用户，以及脱敏规则适用/豁免用户。数据库账号和 CloudDM 用户不能混为一层：用 DB DATA_RW 配合平台只读用户检查平台拦截，用平台有权限配合 DB DATA_RO 检查数据库拒绝；敏感样本只使用合成值。

## 4. 可清理的样本契约

每轮使用唯一前缀 `CDM_HANA_<RUN_ID>`，schema 后缀 `_A`、`_B`，RUN_ID 只取大写字母和数字。单独记录本轮创建的账号和对象清单。以下是待创建样本清单，本步没有创建真实对象；生成 SQL 随第 5、6、10、11 步完善。

| 样本组 | 最小样本 | 要揭示的风险 |
| --- | --- | --- |
| F1 基础与定位 | 行存表、列存表、单列 PK、复合 PK、无 PK 且有重复行的表，各 3–10 行 | `$rowid$`、更新/删除定位、重复行误改、事务可见性 |
| F2 约束与命名 | 一表两条 UNIQUE、复合 FK；A/B 内同名表和同名索引；带空格/双引号/混合大小写标识符 | 唯一约束聚合、关联范围、列顺序、转义和 schema 切换 |
| F3 值与类型 | 整数边界、高精度 DECIMAL、浮点、布尔、日期时间、Unicode、NULL/空串、二进制、LOB、default/identity | 类型/长度/精度、截断、空值和字面量转换；空间类型另见矩阵边界项 |
| F4 对象 | 普通视图、序列、同义词、无参数与带参数函数、IN/OUT 过程、触发器 | 树分类、详情、对象脚本、参数/返回类型、模板执行 |
| F5 SQL 与治理 | 字符串/注释内分号、CTE、JOIN、UNION、MERGE、CALL、SQLScript 块、跨 schema 引用、动态 SQL | 拆句、改写、读写分类、权限、审核、列来源；不可分析语句的处理 |
| F6 生命周期 | 两个并发会话、可终止的长查询、可控断网、大结果流、事务内写入 | 取消目标、超时、释放、恢复、未知写入结果；仅使用隔离实例 |

清理先终止本轮任务/关闭会话，再按依赖清理本轮对象，最后清理专用 schema 与账号；核对 A/B 目标对象归零。不得根据宽泛前缀删除他人对象。失败时保留对象清单供排查，再人工确认归属后清理。导入/导出文件只在临时目录保存；校验行数和关键值后移除。

## 5. 执行与页面验证节点

每项执行都登记组合 ID、代码和制品摘要、用户/数据库角色、样本、入口、期望与实际结果，更新矩阵；直接 JDBC/HDBSQL 只用于对照，不能代替 CloudDM 产品链路。复用 `tests/ds-test/` 现有样本和测试类，不新增测试类。涉及前端时遵守 [frontend/AGENTS.md](../../../frontend/AGENTS.md)，使用 @Browser；首次页面复测时维护 `tests/frontend/datasource/hana_datasource.md`，现在不编写未经验证的页面操作步骤。

| 计划节点 | 届时可邀请用户验证的内容 | 必要条件 |
| --- | --- | --- |
| 第 1 步 | 环境/版本说明和矩阵；没有新增页面功能 | 已交付基线文档 |
| 第 2 步 | 新增/编辑连接、保存重开、测试连接、TLS/SSH 配置 | 本步代码已完成，待部署插件、E1 可达；隔离环境可尝试下文直达入口，查询编辑器仍可能受 DSL 缺口阻塞 |
| 第 3 步已实现、后续相关修复后 | schema/事务、取消、对象树、查询编辑器基础加载和执行 | 对应会话/元数据修复及第 6 步 DSL 入口完成，逐项报告实际已通过范围 |
| 第 7–12 步各步后 | 补全与改写、审核权限、脱敏、结构编辑、数据编辑/导入导出、执行计划/工单 | 每步只邀请验证已打通的具体页面和用例，并说明剩余限制 |
| 第 13–15 步 | 页面能力与国际化、发布包回归、最终默认入口 | 核心矩阵和版本组合门禁通过；默认开放最后进行 |

后续每步完成时，在交付说明中明确“可验证页面、所需部署包、数据库/账号条件、尚未支持项”。第 2 步已从源码确认新增页支持直达 HANA 配置，具体入口见下文，仍待浏览器实测；本步保留 `display=false`。

## 6. 第 1 步完成条件与剩余交接

- 已完成：精确服务器镜像目标与摘要、两组驱动组合、环境契约、账号角色、样本清单、能力矩阵和页面验证节点。
- 待实例部署后：回填实际服务器/JDK/驱动版本和摘要、端口、权限及样本创建/清理证据。目前 E1/E2 环境验收均为 BLOCKED。
- 第 2 步实现与本地验证已完成，见下文；真实连接与能力验收必须等待环境就绪，不能据本步文档将 HANA 标为已支持。


## 7. 第 2 步连接配置与 TLS 交付

当前实现已生成 `backend/clouddm-plugins/clouddm-ds/ds-hana/build/libs/ds-hana-lib.jar`。部署时替换对应环境中的 HANA 插件并重启加载该插件的 Console/执行节点；JDBC 驱动单独准备。不要用老进程中的插件来验收新配置。

### 配置契约

| 配置 | 实际行为 |
| --- | --- |
| 默认驱动 | `SAP Hana JDBC / 2.28.6`，仍可选择 `2.22.12` 回归；不改变 compileOnly 基线 |
| 地址/端口 | 单一主机或方括号 IPv6；缺省端口仍是 30015，页面提示实际 tenant 端口须核实，不能把 Express 端口写死为所有 HANA 的默认 |
| 租户数据库 | 通过 `databaseName` 属性传递，不拼到 URL；schema 通过 `currentSchema` 传递 |
| 自定义 JDBC 地址 | `jdbc:sap://host:port/`，覆盖地址/端口；本步明确限定单端点，不接受查询参数、凭据、片段或路径；不能与 SSH 同时启用 |
| 高级配置 | 连接超时单位为毫秒，0–2147483647；通信超时单位为秒，0–2147483，再转为驱动毫秒；0 为无限制。空值默认 5000 毫秒/10 秒；小数、负数和溢出均拒绝 |
| TLS 证书主机名 | 留空时使用连接主机名；启用 SSH 且进行证书校验时必填原服务器证书中的名称。拒绝通配符，避免把校验目标变成模糊匹配 |
| SSH | 复用既有隧道，地址中需有目标端口；直接 IPv6 已在工厂层检查，SSH IPv6 受公共隧道解析器限制，本步不声明支持 |
| 失败与资源 | 不再记录 JDBC URL/账号；保留异常栈和实例 ID。连接成功但后续初始化失败时关闭连接，保留关闭异常为 suppressed |

自定义 JDBC 地址是单端点覆盖功能；数据库、schema、账号、超时、TLS 使用对应字段。页面没有新增任意 JDBC 属性编辑器，也不在本步宣称多节点 URL 支持。原生属性的传递仍由驱动工厂处理。`asDriverProperties()` 仅负责转换属性；URL/SSH 冲突和 SSH 证书主机名要求在连接工厂及 TLS 配置处理处校验，SSH 标记不传给 JDBC 驱动。

### TLS 模式

| 页面模式 | ngdbc 行为与材料 |
| --- | --- |
| DISABLED | `encrypt=false` |
| TRUST | 加密传输但不校验证书；这一模式不能作为 CA/主机名校验通过的证据 |
| CA | 解析上传 CA 为 X.509 后转换为 PEM 字符串 `sslTrustStore`；开启证书和主机名校验 |
| TRUSTSTORE | 使用 JKS/PKCS12 TrustStore 的文件路径、类型与密码；开启证书和主机名校验 |
| CLIENT_CERT | CA + 客户端证书/私钥；复用公共解析器校验密钥匹配并转为 PKCS#8 PEM，传入 `sslKeyStore`；开启服务端身份校验 |
| KEYSTORE_TRUSTSTORE | 独立 TrustStore 与客户端 KeyStore，分别传递路径、类型、密码；开启服务端身份校验 |

证书仍由平台既有逻辑保存、序列化和下发到执行侧，插件不修改 JVM 全局 truststore、不额外生成持久私钥文件。新增文案由 HANA 插件的中英文 i18n 资源提供，前端复用动态表单。

**超时调研纠正**：SAP Client 2.28 同一手册的 JDBC 属性表写秒，应用调优章节写毫秒。已按两个实际驱动的属性读取/Socket 路径核对，并以本地无响应 TCP 端点实测：700 毫秒配置分别约 712 毫秒（2.22.12）、711 毫秒（2.28.6）终止连接，服务端观察到 socket 关闭。因此保留毫秒传递，修复的是属性字符串类型和范围校验，不将其除以 1000。参见 [Client 2.28 手册](https://help.sap.com/doc/b01bb8ddc72e41f795034092b9bf541d/2.28/en-US/SAP_HANA_Client_Interface_Programming_Reference_en.pdf)印刷页 460、920；TLS 属性见印刷页 483–485。第 1 步文档中的单位疑点已据此收口。

### 驱动准备与离线部署

在线环境继续使用产品已有的驱动准备功能；运行时描述符通过 Maven 坐标下载 ngdbc。已直接调用仓库的 `DefaultDriverLoader` + `MavenResourcePreparer` 准备两个版本，并按插件的 includePackages 规则隔离加载驱动及新 TLS 实现，未把 ngdbc 打入插件包。

离线环境先在可联网的隔离环境完成同版本驱动准备，然后将驱动根目录下整个 `SAP Hana JDBC/<版本>/` 目录（包括 `files.idx` 与全部相对路径 JAR）复制到目标**每个需要加载驱动的进程**所用驱动根目录。默认根目录为 `<appDataHome>/drivers`；若配置 `driverDirectory` 则使用其解析路径。不要只复制 JAR 或手写索引。已验证复制到另一临时根目录后恢复索引、加载驱动，无需重新下载；真实部署的文件权限、插件版本和节点完整性仍需检查。

本地核对的 JAR SHA-256：

```text
ngdbc 2.22.12  cf383789d1dea5eaaca304f953bc4b15d631009a44bdfa1f48fcf81f94d698dd
ngdbc 2.28.6   45ad4207c37f5502df4942b76fc1b1a7494d171c8c261f5a9373f7a56a4af59b
```

### 现在可以准备的页面验收

可以部署前述 HANA Express server-only，并准备 tenant SQL 端口、测试账号及 TLS CA。加载本步插件后，使用有新增数据源权限的用户，在隔离 CloudDM 环境访问 `/#/datasource/add?dsType=Hana`。该直达方式依据现有路由与后端能力配置代码，**尚未完成浏览器实测**；默认数据源列表继续隐藏 HANA。

先验收驱动默认值、连接表单、租户/schema 保存重开、直连测试连接，再验收合法/错误 CA、错误证书主机名、客户端证书和 SSH 场景。查询编辑器仍可能被第 6 步 DSL 缺口阻塞，本步不声明查询、事务或取消完整可用。

本机 @Browser 访问 `http://localhost:8222` 返回 `ERR_CONNECTION_REFUSED`；因此未完成连接页面、保存重开、国际化显示、真实登录与 SSH 隧道复测。服务与 HANA 实例就绪后，执行相关矩阵并维护长期页面流程 `tests/frontend/datasource/hana_datasource.md`。

## 8. 第 3 步会话上下文与事务语义

### 已实现的契约

- **catalog = 当前租户**：会话初始化、状态回读、元数据目录及 schema 过滤统一使用 `SELECT CURRENT_DATABASE() FROM SYS.DUMMY`。不再把 JDBC `getCatalog()` 的空值作为产品 catalog，也不为读取租户名称依赖 `M_DATABASE`。请求的非空 catalog 与实际租户不符时拒绝初始化并释放连接；连接内切换 catalog 继续禁用并由服务端明确拒绝。
- **schema = 原始名称**：配置和对象路径传原名，例如 `MiX "quoted".name`，不预先包裹 SQL 引号。初次连接的 `currentSchema` 属性和会话 `SET SCHEMA` 显式使用 `fmtName(true, ...)` 定界；名称中双引号加倍。生成本地对象 SQL 使用 `schema.object`，不添加租户前缀；其他调用沿用公共方言的定界规则和调用方参数，不额外按大小写强制定界。会话状态从数据库回读，不以请求参数冒充切换结果。
- **结果列来源**：使用 JDBC `getSchemaName()`；ngdbc 的 `getCatalogName()` 返回空字符串，不能当作 schema。结果列 catalog 使用初始化确认的当前租户。
- **隔离级别**：仅声明 DEFAULT、READ COMMITTED、REPEATABLE READ、SERIALIZABLE；DEFAULT 映射 READ COMMITTED。服务端拒绝 READ UNCOMMITTED。沿用 `HanaSessionSpi` 已有的默认上下文构造，真实连接初始化先设置事务属性，再读取上下文。
- **自动提交与只读**：调用 JDBC setter/getter；初始化应用只读值，切换成功后回读，失败向上传递且不提前修改本地状态。已开放只读切换声明。只读模式是会话行为，不取代数据库账号授权。
- **提交与回滚**：沿用 JDBC，保留公共 `DefaultRdbSession` 的原有行为：成功才清除待提交标记，失败只记日志并保留标记。本轮曾加入的异常上抛已撤回；主动事务操作的错误反馈与清理回滚的异常隔离需要统一调整调用链，见下文待办。
- **查询参数**：声明支持现有 PreparedStatement 位置参数绑定；本地检查包含带 JDBC 类型的值与 NULL。`QueryEditorController` 的参数支持响应字段目前仍注释，因此本步没有新增参数输入页面，也未承诺 OUT 参数/CallableStatement 能力。
- **新连接初始化**：重新应用传入上下文中的 schema、自动提交、隔离和只读设置，再回读实际值。初始化失败关闭资源。这里不恢复断线前未提交事务，也不宣称第 4 步的重连/取消生命周期已完成。

### DDL 边界

保留 HANA 的 `AUTOCOMMIT DDL ON` 默认行为；手动事务模式不等于 DDL 可回滚。插件不随自动提交开关隐式执行 `SET TRANSACTION AUTOCOMMIT DDL`，因为切换 DDL 模式本身会提交事务。混合 DML/DDL 的结果必须用第二个连接验证，不能只看页面“待提交”提示。公共待提交标记目前是请求级保守标记，不是 HANA 服务端事务状态探针；DDL 隐式提交后提示是否需要进一步同步，保留实库验收项。

依据：[Platform CURRENT_DATABASE](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/7ddcb499036a483ab18ecb19816e6708.html)、[Platform SQL Reference SPS 06](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.06/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf)的 SET SCHEMA / SET TRANSACTION（印刷页 1142、1145–1147），以及 [Platform DDL 自动提交](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/d538d11053bd4f3f847ec5ce817a3d4c.html)。两个指定 ngdbc 版本的只读设置和 schema/列来源实现已从本地驱动核对；服务器执行效果仍待 E1/E2。

### 部署与下一轮页面验收

**本步部署更新后的 HANA 插件，并重启加载该插件的进程。**公共 SDK 的提交/回滚改动已撤回，不再要求因本步更新 SDK；仍须满足部署环境与插件的版本兼容要求。完整部署包可按仓库现有 `package/package.sh --build` 生成，本轮只完成相关模块构建与插件打包，未执行全量发布打包。

可以准备 HANA Express，加载本步版本后，在隔离 CloudDM SQL 工作台验收以下项目（页面入口依赖查询编辑器及后续元数据链路可用；不能据本地代理检查认定页面已通过）：

| 场景 | 操作与预期 |
| --- | --- |
| 当前租户/schema | 分别用指定租户及直连租户端口、未填租户名建立会话；界面与 `SELECT CURRENT_DATABASE(), CURRENT_SCHEMA FROM SYS.DUMMY` 一致；请求不同租户时拒绝 |
| 两组同名对象 | 使用 F2 的 `_A` / `_B` schema 各建同名表，写入不同标记；切换 schema 后无前缀查询返回对应标记，带 schema 的查询稳定返回指定对象 |
| 特殊名称 | 验证包含空格、混合大小写、字面双引号的 schema；默认 schema、下拉切换、生成对象 SQL、刷新与重新建立会话均定位一致；不存在/无权限 schema 切换失败后仍显示原值 |
| 提交/回滚 | A、B 两个独立连接使用 READ COMMITTED；A 关闭自动提交，更新 F1 行；B 提交前看旧值、A 提交后看新值；另一次修改回滚后 B 仍看旧值 |
| 自动提交切换 | A 有未提交 DML 后开启自动提交，核实 JDBC 提交效果与 B 可见值；模拟切换失败时页面不能显示成功 |
| 只读与隔离 | 只显示已声明隔离级别；绕过 UI 请求 READ UNCOMMITTED 被拒绝；新建只读会话及运行中切换只读后 SELECT 可用、DML 失败，提交/回滚后仍只读，恢复读写后 DML 可用 |
| DDL 边界 | 在本轮独立对象上执行 DML 后 DDL，再回滚；由 B 核实默认隐式提交效果，记录待提交提示；另开专用会话显式关闭 DDL 自动提交作对照，不能混入普通数据编辑验收 |
| 失败及恢复 | 控制断线使提交/回滚失败，记录日志、待提交标记及确认通知是否一致；明确错误反馈列入下文公共链路待办；重新建立会话后核对上下文，不自动重放未知结果的写入；确认失败初始化的连接被释放 |

本轮 @Browser 访问 `http://localhost:8222` 仍为 `ERR_CONNECTION_REFUSED`，没有真实 HANA 实例，因此上述页面与双连接实库用例均未执行。环境就绪后维护现有能力矩阵及长期前端流程；本步未生成未经验证的前端流程文档。

### 公共事务错误契约待办（本轮不改）

`DefaultRdbSession.commit()/rollback()` 长期采用日志记录且不抛异常。仅添加 `throw` 不能构成完整修复：

- `AutoExecJob.jobWrap()` 的 catch 内直接回滚，未独立保护回滚异常；新的回滚异常可能覆盖原始执行错误，并跳过后面的日志及 FAILED/PAUSED 返回。finally 中还有自动提交恢复，需要一起审计。
- `SessionAgent.commit()/rollback()` 在方法返回后发送确认通知，现状无法判断数据库操作是否失败；保持兼容不代表该反馈已经正确。
- `DmlExplainPreInitHandler.closeExplainSession()` 已分别捕获回滚和关闭异常，清理路径需要保留原始错误并继续资源释放。

后续应区分“用户主动提交/回滚，需要明确结果”和“异常清理中的尽力回滚，需要保护原始错误”，同步修正通知、任务终态及连接清理，并覆盖断网、提交结果未知、回滚再次失败和自动提交恢复失败。HANA-008 的失败反馈仍待解决，不能标为验收通过。
