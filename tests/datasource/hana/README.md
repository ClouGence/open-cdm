# HANA Platform 验证环境与复跑入口

更新时间：2026-10-01。对应[HANA 补齐计划](../../../docs/guides/hana-completion-plan.cn.md)。

**第 1 步基线已确定，第 2～8 步实现及本地验证已完成；真实实例和页面验收仍待环境。**后续结果持续维护在[能力矩阵](hana-test-matrix.md)，不按执行日期另建报告。

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

## 9. 第 4 步：取消与资源释放

### 实现边界

- 会话初始化查询 `SELECT CURRENT_CONNECTION FROM SYS.DUMMY` 并缓存自己的连接 ID；取消复用既有独立连接机制，不在忙连接上查询 ID。连接属性固定 `reconnect=false`，断线后显式新建会话并重新初始化上下文，不自动恢复事务或重放 SQL。
- 取消连接建立后校验原请求是否仍在执行。原请求已结束则关闭临时连接并返回；重复取消合并到正在进行的请求。取消尚未返回时，新查询立即报 `HANA cancellation is still in progress`，新的查询工作线程不等待取消 I/O，取消结束后可重新提交新查询。
- 取消命令的权限、网络等错误保留并通过 `ThirdPartyApiException` 交给现有 API 错误链。只有本地确认没有活动请求才直接返回；不猜测未知的服务端错误码，也不把所有 JDBC 错误当作“已结束”。请求结束与服务端处理之间仍可能收到服务端状态变化错误，此时原样反馈。
- 流式读取保留取消标记直到执行退出，先检查标记再调用 `ResultSet.next()`；已阻塞在 JDBC 内的调用依赖服务端取消或配置的通信超时退出。普通查询及 EXPLAIN 的 Statement 参数初始化失败会关闭 Statement；清理失败作为 suppressed exception 保留原始错误。
- 执行错误 **139** 明确表示事务已回滚，此时清除待确认标记。仅发送取消请求成功、流式读取在本地停止、取消失败或断线，都不能凭此断言已回滚。SQLState `08` 关闭旧连接，保留未知事务结果；重新打开会话取得新连接 ID，不复用旧事务。
- 公共 SDK：状态刷新失败不会覆盖原查询错误；计时器调度抛出 RuntimeException 时复位执行标记并原样上抛；异常钩子通过 finally 执行原有 triggerFailed，钩子异常仍向调用者传播。保留 triggerCompleted/triggerFailed 的原有完成处理，结束时停止后续等待提示。`DefaultRdbSession.commit()/rollback()` 保留原有处理契约。
- sidecar：仅修复关闭必达。取消或事务通知失败仍结束结果构建并调用底层 session.close()，保留首个错误及关闭异常。不修改 recycling、队列、批次状态或完成通知流程。

选择独立连接的依据：检查 ngdbc 2.28.6 的 `StatementSapDB._cancel()` / `ConnectionSapDB._cancel(Statement)`，其内部同样建立独立连接并执行 `ALTER SYSTEM CANCEL SESSION`。沿用项目资源工厂可以继续使用现有 TLS/SSH 配置，并在慢连接返回后再次核对请求生命周期。没有用真实服务端验证该驱动内部路径，也没有改用原生取消接口。

依据：[SAP Platform CURRENT_CONNECTION](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20dde16475191014bf3090e6b2e9857f.html)、[Platform 2.0 SPS 06 SQL 参考手册，印刷页 505](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.06/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf)（取消和 139）、[SAP Client 重连说明](https://help.sap.com/docs/SAP_HANA_CLIENT/f1b440ded6144a54ada97ff95dac7adf/197dc47daa1d43efa5a77d3148717842.html)（禁用透明重连）。Cloud 仍单独评估。

### 本地验证

从 `backend/` 执行：

```bash
./gradlew :ds-hana:build :ds-hana:customFatJar :cgdm-plugin-sdk:test :dsc-common:test :cgdm-sidecar:test :cgdm-sidecar:jar --offline --max-workers=4
```

已通过构建和现有测试（SDK 4 项、dsc-common 16 项；HANA/sidecar 没有测试源码）。另使用临时 JShell JDBC 代理、线程池和 CountDownLatch 注入慢连接及错误，验证当前连接定位、重复/迟到取消、取消过程中新查询拒绝、权限错误反馈、流式读取停止、资源 close 次数、139/断网事务状态、计时器拒绝调度的异常传播/执行标记复位，以及取消或通知失败时底层关闭必达。分别使用两个固定 ngdbc 版本类路径执行取消探针；这仅是应用生命周期验证，不代表两个驱动均已通过真实取消协议测试。第 3 步会话/事务探针回归通过，未新增测试类。证据和制品摘要见矩阵。

### 公共队列与终态待办

本轮复核已撤回 SessionAgent 的 recycling 新分支、关闭时清空队列和跳过完成流程的提前返回。队列取消、批次完成事件、审计收尾及关闭/提交并发需要作为独立公共生命周期问题处理，覆盖不同数据源与完整通知链路后再落地。之前探针中“关闭后清空队列、拒绝复用、已排队任务退出”的结果只适用于已撤回版本，不能作为当前交付证据。HANA-010 的这部分门禁仍未完成。

### Express 与页面验收入口

本步可以准备 HANA Express 开始验证**查询取消、关闭会话和断线反馈**。需要部署包含本次 `cgdm-plugin-sdk`、`cgdm-sidecar` 及 `ds-hana` 的完整运行包；只更换 HANA 插件不足以验证本步。默认隐藏不变，隔离环境仍使用 `/#/datasource/add?dsType=Hana`。当前 `localhost:8222` 未运行，未进行页面验收；若编辑器仍受后续 DSL/元数据缺口阻塞，记录具体阻塞，不把直连 JDBC 结果当作页面通过。

| 场景 | 操作与预期 |
| --- | --- |
| 双会话精确取消 | A/B 分别读取 `CURRENT_CONNECTION`，执行可控长查询或隔离测试表上的锁等待；取消 A，A 终止而 B 不受影响，管理连接观察连接/语句数量 |
| 重复、迟到取消 | 连续点击取消；对已结束查询再次取消；延迟取消连接建立，在取消完成前提交新查询，确认明确报忙且随后新查询不被旧取消误杀 |
| 错误反馈 | 使用经实测无取消权限的身份、阻断临时取消连接或关闭网络；错误在页面可见，不能显示为已成功取消；不要预设同用户取消必然需要 SESSION ADMIN |
| 流式与关闭 | 接收大结果期间取消、关闭会话；取消失败时再关闭；检查结果停止和旧连接、Statement、ResultSet 释放；排队批次的终态及审计通知另行验证 |
| 事务结果 | 事务内写入后制造锁等待再取消；收到 139 后由另一会话确认写入回滚。断网或未得到 139 时保留未知结果，先核实数据库结果再决定后续操作 |
| 显式重连 | 断网再恢复，旧会话不得透明执行后续 SQL；重新打开取得新 ID、恢复配置上下文，不恢复旧事务或重复写入 |
| TLS/SSH 与边界 | 对 E1/E2 分别复测独立取消连接的 TLS/SSH、有限连接/通信超时、EXPLAIN 中止和断线；超时设为 0 时不能承诺在有限时间内终止阻塞 I/O |

HANA-009/010 保持 NOT RUN，待以上产品链路验证后逐项更新。

## 10. 第 5 步：元数据与类型映射

### 实现与边界

- 过程详情按 `SCHEMA_NAME`、`PROCEDURE_NAME` 顺序绑定；索引/约束按 schema、table、index 三个字段关联。每条 UNIQUE 按索引名独立聚合，保留复合 PK/UK/索引列的顺序和全部升降序信息；表名按原始大小写匹配。外键补充更新、删除规则并保留引用列顺序。
- 视图列入口直接读取 `SYS.VIEW_COLUMNS`；列表、详情与列入口校验当前租户，不匹配时统一抛出 `CONFIG_HANA_CATALOG_MISMATCH`，不再用空结果掩盖上下文错误。表/视图种类保留系统视图原值，创建时间保留小数秒且允许 NULL。
- 列类型使用 `HanaTypes.valueOfCode()`，支持已有 INT/LONGDATE 别名，并补充 DAYDATE/SECONDTIME。分别设置字符长度、二进制/LOB 字节长度、数值精度/小数位和时间精度，SQL NULL 不再变为 0。原始类型、默认表达式、注释、生成表达式保留；两种 identity 模式均可识别。
- 未知列类型抛出包含 schema、表、列和原始类型的明确异常，不返回空类型，也不伪装成 VARCHAR。列表不读取列类型，因此此错误限于相关详情/列请求；批量详情中包含不支持列时该批次仍会失败，本轮没有修改公共批量接口来提供部分成功。
- 过程参数补充 IN/OUT/INOUT、长度与精度；函数按 OUT 识别返回值，无参数元数据的函数仍保留。UMI 只有一个返回值槽：单个 OUT 放入返回值，多个 OUT 全部保留在参数列表。TABLE_TYPE 参数保留类型名和类型引用；本步不展开表参数的嵌套列，也不据此声明多返回值编辑器或 CALL 绑定完整可用。
- 序列详情读取最小值、最大值及步长，使用字符串避免 long 截断；同义词详情保留目标 database/schema/object。树分组加入过程、函数和触发器，沿用现有前端节点。
- 触发器事件、目标 schema/table、粒度来自系统视图，完整定义保存在 features.definition，定义为空或无 BEGIN/END 时不再抛截取异常。已去除按头部关键字猜事件/UPDATE OF 列的逻辑；现有 SQL body 提取仍是简化实现，UPDATE OF、复杂引号/注释及重建 SQL 必须在第 6/10 步结合语法与模板补齐，不能据本步认定触发器可无损编辑。
- JDBC 结果列来源沿用第 3 步的 `getSchemaName/getTableName` 实现，本步未改写公共结果转换、会话或事务逻辑。普通账号权限过滤、ARRAY 元素结构、空间 SRID 和 LOB 实际值读取仍需 F3/F4 实库核对。

系统字段按 [Platform 2.0 SPS 06 SQL Reference](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.06/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf) 中对应系统视图核对，没有使用 Cloud 独有字段。列维度和生成字段见 [TABLE_COLUMNS](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/2100d33a75191014868bbcd89274199c.html)，外键规则见 [REFERENTIAL_CONSTRAINTS](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20ccc0a175191014901b88e6bc175c44.html)，序列数值字段见 [SEQUENCES](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20cf0e79751910149462bf9e7d571ab8.html)。这些在线页面可能展示较新 SPS，最低基线以固定 SPS 06 PDF 为准；服务端可见性仍须实测。

### 本地验证

```bash
cd backend
./gradlew :ds-hana:build :ds-hana:customFatJar :cg-schema:test :dsc-common:test --offline --max-workers=4
```

构建、插件打包与 dsc-common 16 项现有测试通过；ds-hana/cg-schema 无测试源码。临时 JShell JDBC 代理在 ngdbc 2.22.12、2.28.6 两个类路径下分别通过 72 项断言，覆盖类型维度/NULL/别名/未知类型、LOB、默认值/identity、独立 UNIQUE、复合约束/索引列序及 JSON 转义、FK 规则、过程绑定、函数参数和返回值、视图列路由、对象种类/时间、序列数值、同义词目标、树分组及 Statement 释放。没有新增测试类。这些代理检查不执行服务端 SQL，不代表驱动与真实系统视图已通过兼容性测试。

### Express 部署与页面验收

**可以部署 HANA Express 验证本步元数据链路。需更新 `ds-hana` 插件及包含新 `cg-schema` 的平台运行包**；插件 JAR 不包含 `HanaTypes`，仅替换插件会漏掉新增别名。完整发布打包未在本轮执行。默认隐藏不变，隔离环境仍通过 `/#/datasource/add?dsType=Hana` 建立连接。

| 场景 | 页面/数据库核对 |
| --- | --- |
| 对象树 | 工作台展开租户/schema，查看表、视图、序列、同义词、过程、函数、触发器；空分组可刷新，两组 schema 同名对象不混淆 |
| 表/视图列 | 对 F2/F3 的行存、列存、视图查看列信息；核对 NULL、default/comment、identity、生成列、DECIMAL 精度、NVARCHAR 长度和 LOB；视图列不再为空 |
| 约束和索引 | 同表两条 UNIQUE 必须分别显示；复合 PK/FK/索引列序和引用列一一对应；跨 schema 同名索引不混入，外键更新/删除规则与系统视图一致 |
| 程序对象 | 无输入函数仍列出；IN/OUT/INOUT、单个返回值及 TABLE_TYPE 引用核对真实定义；多输出不丢失。当前页面未消费的详情字段用详情响应核对，不能当成页面已支持 |
| 序列/同义词/触发器 | 核对序列边界与步长、同义词目标、触发器事件/目标/粒度；本轮只验读取，不据不完整模板执行重建 |
| 权限与失败隔离 | OWNER、RO、LIMITED 分别刷新；仅看各自允许的元数据。无权/不支持对象的详情失败后仍能刷新其他对象列表；跨租户路径明确报数据库不匹配，不能显示为空或串对象 |

本轮 @Browser 访问 `http://localhost:8222` 返回 `ERR_CONNECTION_REFUSED`，且没有真实 HANA 实例，页面与 E1/E2 用例均未执行。若查询工作台被第 6 步 DSL 能力缺口阻塞，记录阻塞后再继续，不把 JDBC 或代理检查替代页面结果。HANA-011～014 保持 NOT RUN。
## 11. 第 6 步：HANA 语法与拆句基础

### 实现与复用边界

- `HanaLexer.g4` / `HanaParser.g4` 位于 HANA 插件内，ANTLR 版本沿用工程 4.9.3。生成类进入插件包，ANTLR 生成工具不进入运行包；无需新增或注册 Gradle 子模块。
- 新 `HanaDslProvider` 提供 lexer/parser、拆句和语法树遍历；`HanaSqlEngineSpi.dslProvider()` 正常返回 provider。`doParser()` 的完整语义 AST 转换仍明确抛出不支持异常；语言能力仍只声明 COMPLETE/SPLIT，没有声明 VALIDATE/FORMAT。公共 `QueryEditorController` 增加能力交集过滤，避免仅凭非空 DSL 自动开启 VALIDATE；默认声明全部能力的插件行为不变，达梦同样按自身声明不开放空校验。
- 普通 SQL 保留为结构化 token 序列，识别括号、字符串、双引号标识符、单双引号重复转义、行/块注释和 CASE 表达式。SQLScript 支持 BEGIN/END、IF/ELSEIF/ELSE、FOR/WHILE/LOOP、嵌套块、异常处理器中的块及执行属性；过程、函数、触发器和 DO 的整个块只生成一个顶层执行单元。
- 复用 `AbstractSplitAnalysisSpi` 的流式读取、背压、位置计算和关闭机制，没有重写公共并发/会话代码，也没有 SQL:2003 拆句兜底。顶层语句必须到分号或 EOF 才交付；块、引号、注释、括号未闭合时抛出带行列的 `AntlerSyntaxException`，不输出该坏块的内部语句。之前已交付的完整语句仍遵守公共流式执行语义，本步不承诺整份脚本原子执行。
- `HanaLanguageSpi.split()` 使用同一拆句器并保留异常，避免通用策略将失败转成空成功。行为分析原有手写分号拆句已删除；第 6 步当时对象关系分析仍沿用 SQL:2003 和已有 DML 补充，DO 探针在行为分析失败。第 8 步已替换该实现，当前支持与限制见第 13 节；不能凭拆句通过宣称可执行。

语法边界参考 [Platform 2.0 SPS 06 SQLScript Reference](https://help.sap.com/doc/6254b3bb439c4f409a979dc407b49c9b/2.0.06/en-US/SAP_HANA_SQL_Script_Reference_en.pdf) 的过程/函数、匿名块、控制流、异常处理与事务章节。样本为本仓库编写，尚未在真实 HANA 上执行；Cloud 仍独立评估。

### 分类契约与不支持项

| 输入 | 顶层分类 |
| --- | --- |
| SELECT、WITH … SELECT | SELECT；不把 CTE 子查询另行执行 |
| INSERT、UPDATE、DELETE、MERGE | 对应 DML 类型 |
| CREATE/ALTER/DROP/RENAME/COMMENT | 按对象种类分类；支持 CREATE OR REPLACE、ROW/COLUMN、临时表、索引等常见前缀 |
| CREATE PROCEDURE/FUNCTION、CREATE TRIGGER | CREATE_PROG_OBJ、CREATE_TRIGGER；不将内部 SELECT 当成顶层查询 |
| CALL、DO / BEGIN 块 | CALL_PROG_OBJ、BLOCK |
| SET SCHEMA、SET TRANSACTION、COMMIT/ROLLBACK/SAVEPOINT | SWITCH_SCHEMA、TRANSACTION |
| 其他 SET/UNSET、EXPLAIN、IMPORT/EXPORT | SESSION_SETTING_WRITE、PERFORMANCE、DATA_IMPORT/DATA_EXPORT |
| 未映射头部，例如 UPSERT | UNKNOWN，保留完整语句，不冒充 SELECT |

这是一层**结构拆句和头部分类**，不是完整 HANA 语义校验。即使 `SELECT FROM` 这样的 SQL 可被归为 SELECT，也不代表它能执行；对象名、表达式和选项的合法性由数据库判定。UNKNOWN 不是审核通过，权限/规则/血缘仍按第 8/9 步补齐。动态 SQL 字符串保持原样，不递归执行或分析字符串中的 SQL。客户端 DELIMITER、GO、独立 `/` 等指令不作为分隔符识别，应移除后提交服务端 SQL；未覆盖的控制结构不能通过删除关键字、吞异常或按分号重试绕过。

### 本地验证与样本

[06-split.sql](sql/06-split.sql) 包含 14 个预期执行单元，依次为：CREATE_TABLE、INSERT、UPDATE、DELETE、SELECT、MERGE、CREATE_PROG_OBJ、CALL_PROG_OBJ、CREATE_PROG_OBJ、BLOCK、SELECT、DROP_PROG_OBJ、DROP_PROG_OBJ、DROP_TABLE。它会创建/删除对象，只能在一次性 schema 下执行；过程、函数和匿名块中的每个分号均不能增加顶层语句数。

```bash
cd backend
./gradlew :ds-hana:build :ds-hana:customFatJar :cgdm-console:compileJava :cgdm-console:test :sqlc-common:test :cg-dslparser:test :dsc-common:test --offline --max-workers=4
```

构建和插件打包通过；console 52 项已有测试通过，dsc-common 16 项已有测试通过（Gradle 复用通过结果），HANA/sqlc-common/cg-dslparser 无测试源码。现有测试不覆盖编辑器能力接口，页面仍待验证。临时 JShell 探针通过样本计数/分类、DSL 与流式拆句文本一致性、语言服务请求标识及错误透传、引号/注释分号、Unicode/CRLF/非零起始位置、嵌套块与异常、3000 条语句、提前关闭后的后台线程退出及调用方 Reader 归属。没有新增测试类。本地验证不是 HANA 服务端兼容性认证，也不是页面验收。

### Express 部署与页面验证

**可以部署 HANA Express 验证本步。** 在第 5 步平台包基础上更新 `ds-hana-lib.jar` 和包含能力过滤修正的 console 平台包，并重启加载；仍保持默认隐藏，隔离环境按已有直达路径建立 HANA 数据源。

1. 打开工作台查询页签，确认语言能力加载不再抛出原 `DslProvider` 异常；能得到 COMPLETE/SPLIT，不显示为已支持完整语法校验。
2. 粘贴只读 `DO BEGIN SELECT 'a;b' AS V FROM DUMMY; SELECT 2 AS V FROM DUMMY; END;`，等待服务端拆句返回，将光标移入两个 SELECT，当前语句框均应包围完整 DO 块。
3. 普通 SELECT 可验证执行；静态 DO/过程体按第 13 节支持范围验证，遇到未覆盖语法记录错误，不绕过分析或改为执行内部片段。`06-split.sql` 的服务端语法兼容性可另用原生 HANA 客户端在一次性 schema 验证；平台完整执行闭环仍需实库验收。
4. 缺少 END、引号或括号时，后端拆句应返回错误；修正后重试恢复。错误显示、光标范围、快速编辑及 WebSocket 失败回退按 [SQL 语句选择流程](../../frontend/sql/sql_statement_selection.md) 验证。

**已确认的页面边界**：`frontend/src/components/editor/index.vue` 在服务端结果未返回、失败或版本不匹配时仍使用通用分号切片。该回退不理解 HANA SQLScript，光标执行可能选中块内片段；本步没有更改公共前端逻辑，第 7 步需明确处理并实测。SQLScript 若需提交验证，必须手动选中整个块，且仍受上述行为分析限制。本轮 @Browser 访问 `http://localhost:8222/#/sql` 返回 `ERR_CONNECTION_REFUSED`，以上页面/实库场景均未执行，不能标记 PASS。


## 12. 第 7 步：语言服务与查询改写

**未完成：HANA-017/a 格式化能力统一。**现有 HANA 专属按钮判断和前端空白整理是待收敛实现，不代表完整格式化交付。第 13 步必须同时处理工具栏能力判断、编辑器调用分派、后端能力声明和真实格式化实现，并回归其他数据源；不能只将按钮条件改成 `support.format.conf`。详细关闭条件见[计划第 13 步](../../../docs/guides/hana-completion-plan.cn.md#第-13-步-对齐页面入口能力与国际化)，该项阻塞第 15 步默认开放。

### 实现与限制

- 语言能力声明 COMPLETE/VALIDATE/SPLIT，与编辑器配置一致。VALIDATE 复用 HANA 结构解析，报告未闭合引号、括号、块及结构错误；成功时返回明确的“仅结构校验”提示。`SELECT FROM` 等表达式错误、对象存在性、权限及完整 SQLScript 语义不在此校验范围，不能将提示当作审核通过。
- 补全先通过现有元数据服务获取当前授权对象，再查询其表/视图列，避免直接列查询绕过对象过滤。跨 schema/catalog 引用不提供列建议；未加引号名称按 HANA 大写规则解析，元数据插入使用双引号，保留大小写不同的对象。CALL 提供过程，表达式位置提供函数；字符串和注释内不返回元数据建议。局部 SQLScript 变量、CTE 推导列、复杂别名作用域与未闭合双引号内补全尚非完整语义实现。
- 错误位置和公共补全 token 偏移转换为编辑器 UTF-16；emoji 不再导致后续光标偏移。关键字建议使用独立 `hana-completion.keywords`，不改变用于标识符转义的保留字集合。
- HANA 查询页签开放现有格式化按钮。格式化整份文档，只调整空白和基础 BEGIN/CASE 缩进；保留字面量、引号名称、注释与 token 顺序，可撤销，遇到未闭合引号/注释拒绝替换。它不是完整 SQLScript 美化器，也不以格式化修复无效语法。
- 当前版本服务端 SPLIT 未返回、失败或版本失效时，HANA 不使用通用分号回退框选或光标执行。手动选区仍可执行；语法修复并收到新结果后恢复自动选择。诊断请求发送整份文档，避免将块内片段误当独立 SQL 校验。
- 改写使用 HANA token/结构解析，仅处理单条 SELECT/WITH 查询。已有数字 TOP/LIMIT 取较小上限（保留 0、OFFSET 和较小值）；CTE 子查询不改，集合查询限制整体，保留排序、尾注释、分号，并将新增 LIMIT 放在锁/输出格式/HINT 尾部子句前。INSERT…SELECT、SELECT INTO、DO 和多语句不改写。非数字 TOP/LIMIT 和不识别的外层语句保持原文；公共 JDBC 结果消费仍有 fetchRecordCountLimit，但不能据此声称数据库端计算量已受限。
- EXPLAIN 只包装单条支持的语句，已有 EXPLAIN 按 FOR 提取目标，保留 Unicode 边界；多语句不生成执行计划 SQL。第 8 步已替换 DO 的旧行为分析，具体静态分析边界见第 13 节。

查询尾部规则依据 [Platform 2.0 SPS 06 SQL Reference 的 SELECT 章节](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.06/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf)。Cloud 独立评估，尚无服务端兼容性结论。

### 本地验证

```bash
cd backend
./gradlew :ds-hana:build :ds-hana:customFatJar :dsc-common:test :cgdm-console:test --offline --max-workers=4
cd ../frontend
npm run lint -- --no-fix
npm run check-i18n
npm run test:unit
cd ../package
./all_build.sh web
```

构建与插件打包通过，现有 dsc-common 16 项、console 52 项和前端 8 项测试通过；HANA 无测试源码，未新增测试类。临时 JShell 探针覆盖授权列查询边界、引号/大小写、Unicode、结构诊断、CTE/UNION/TOP/LIMIT/锁/HINT/多语句/EXPLAIN；Node 探针检查格式化文本保留、重复格式化和空白输入。`check-i18n` 只扫描暂存文件，本轮另外直接核对新增中英文 key。以上不替代真实数据库或页面验收。

### Express 部署与页面验收

**可以部署 HANA Express 验证本步页面功能。** 在第 5/6 步基础上更新 HANA 插件、包含 dsc-common/console 修改的平台包和 Web 资源，重启并刷新页面。仍默认隐藏；隔离环境通过 `/#/datasource/add?dsType=Hana` 建立连接。

1. 在查询页签输入 SELECT，触发授权表/列建议；OWNER/RO/LIMITED 分别验证，不可见表的列不得通过手写表名触发建议。检查大小写不同名称和带空格名称的插入文本。
2. 输入 DO 块，检查结构提示、缺少 END/引号/括号时的错误位置，以及 emoji 后的位置；修复后提示恢复。SQLScript 执行按第 13 节静态语法与权限边界验证。
3. 格式化含字符串分号、双引号名称、行/块注释的整份脚本，核对内容和撤销；手动选区不改变“格式化全文”的行为。未闭合引号时不修改文本。
4. 按 [SQL 语句选择流程](../../frontend/sql/sql_statement_selection.md) 验证 SPLIT 延迟/失败/版本失效时无自动执行目标，恢复后可选整块。
5. 使用 [07-language-rewrite.sql](sql/07-language-rewrite.sql)，将页面结果上限设为 1，检查普通 SELECT、TOP/LIMIT、CTE、UNION 的结果以及最终执行 SQL；已有 LIMIT 0 必须仍为 0。锁子句使用隔离测试表单独验证，及时结束事务。

2026-10-01 @Browser 访问 `http://localhost:8222/#/sql` 返回 `ERR_CONNECTION_REFUSED`，没有真实 HANA 实例；页面及 HANA-017/018 的 E1/E2 验收保持 NOT RUN，不能记为 PASS。

## 13. 第 8 步：行为分析、权限与 SQL 审核

### 实现与调用契约

- `HanaBehaviorAnalysisSpi` 不再继承 SQL:2003，也不再用正则补写操作。`HanaBehaviorParserVisitor` 从同一棵语法树生成行为关系与审核域，复用 `sqlc-common` 的公共对象工厂。
- 语法已统一为一份 `HanaLexer.g4` 和一份 `HanaParser.g4`，不再保留 `HanaAnalysisLexer/Parser`。同一 Parser 的 `splitRoot` 入口识别编辑器结构边界，容纳未完成表达式；`statementRoot` 入口供行为/审核严格解析整条语句，绝不回退到 `splitRoot`。两入口共用全部词法、关键字和位置规则，结构校验通过不等于行为分析通过。
- `HanaSecDomainResolveSpi` 已接入 SQL 引擎，`HanaSecRulesSupportSpi` 已注册。查询不再因 HANA 返回 null resolver 而跳过规则。规则支持 Query、Insert、Update、Delete、Call 和已解析的 DDL 对象模型；资源范围只声明当前规则引擎能够消费的 Schema/Table/View。
- 行为关系交给现有平台权限转换：表查询 READ，INSERT/UPDATE/DELETE/MERGE/UPSERT/REPLACE 为 WRITE，过程/函数调用 PROGRAM，DDL 为 DDL。当前编辑器上下文补齐 tenant/schema，未加引号名称按大写处理，带引号名称保留大小写。
- CTE、子查询、跨 schema 表、UPDATE 目标别名和静态 SQLScript 表变量都追踪到真实来源；CTE/别名/变量自身不冒充物理表。静态块遍历全部分支，程序定义同时检查定义本身及其静态语句引用；这是一种保守授权策略。
- 审核域提供 WHERE、JOIN、子查询、WITH/集合操作、TOP、插入列清单等字段。MERGE 同时生成整体与各修改分支的审核域。建表列、行内/表级约束、ALTER、CREATE INDEX 生成对应域；表级主键同步影响列的 primary/nullable。SELECT 与约束审核保留真实表资源，支持按表匹配规则。
- 不增加 SYS/DUMMY 的免鉴权注册。系统对象仍走现有资源授权与数据库权限；仅明确列出的无引号、无限定名内置函数免 PROGRAM 对象权限，其他函数保守按程序对象处理。

### 当前支持边界

| 范围 | 当前行为 |
| --- | --- |
| 常用查询与 DML | SELECT、WITH、JOIN ON/CROSS JOIN、子查询、集合运算、TOP/LIMIT；INSERT、UPDATE、DELETE、MERGE、UPSERT/REPLACE 的已定义语法。后两者拆句类型仍为 UNKNOWN，但行为关系为写操作，不会因此跳过权限检查 |
| 常用 DDL | CREATE TABLE/VIEW/INDEX/SCHEMA/SEQUENCE/SYNONYM、ALTER TABLE 列/约束、DROP 非索引对象、RENAME TABLE、TRUNCATE、COMMENT；高级选项未覆盖时明确拒绝 |
| 静态 SQLScript | DO/BEGIN、过程/函数/基本触发器定义，局部变量、表变量、IF/循环/异常处理块；嵌套 SQL 逐项分析。过程 DEFAULT SCHEMA 用于其静态对象解析 |
| CALL 和函数 | 校验 PROGRAM 权限；不展开数据库中已存在程序的内部定义，也不解析字符串参数中的 SQL。程序内部实际访问仍由数据库定义者/调用者权限控制 |
| 动态 SQL、未知语法 | EXEC/EXECUTE IMMEDIATE、管理/DCL、未覆盖的 SQLScript/高级表达式等抛 `ThirdPartyApiException`，停止当前语句的执行/审核；不转成空关系、空域、只读或审核 PASS |
| DROP/RENAME INDEX | 第 10 步已通过运行时元数据查找实际所属表，按该表授权和审核；纯静态入口、索引不存在或查找失败仍明确拒绝，详见第 15 节 |
| 上下文与名称 | 三段远程/跨库对象名、带 `/` 的资源对象名拒绝；SET SCHEMA 与编辑器 schema 不同则提示通过编辑器切换，防止 SQL 会话与鉴权上下文错位 |
| 系统函数与方言规则 | 内置函数白名单是有限集合；未收录函数可能需要补充。MySQL 专有 engine/charset 等规则不代表 HANA 支持，不能因模型同名就套用所有方言字段 |
| 审核与脱敏 | 本步只接查询/DDL 审核；列血缘及脱敏规则范围仍留第 9 步，不将非空审核域当成血缘实现 |

语法依据核对了 SAP Platform 2.0 SPS 06 的 [SQL Reference](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.06/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf) 中查询/DML/DDL 结构及 [SQLScript Reference](https://help.sap.com/doc/6254b3bb439c4f409a979dc407b49c9b/2.0.06/en-US/SAP_HANA_SQL_Script_Reference_en.pdf) 的静态块和 DEFAULT SCHEMA；约定 SPS 08 Express 的实际兼容性仍须实测。当前语法不是完整厂商语法，也不承担类型、对象存在性等服务端校验。

### 本地验证

```bash
cd backend
./gradlew :sql-hana:build :ds-hana:build :ds-hana:customFatJar :dsc-common:test :cgdm-console:test :plus-sec-rules:test :cg-detectrule:test --offline --max-workers=4
```

- 构建和插件打包通过；现有测试共 112 项（dsc-common 16、console 52、plus-sec-rules 2、cg-detectrule 42）零失败，Gradle 复用未变化模块的有效测试结果。HANA 模块暂无测试类，本轮未新增。
- 临时 JShell 探针覆盖跨 schema/CTE/别名/子查询、MERGE、静态块、变量来源、程序 DEFAULT SCHEMA、序列、约束以及不支持语句的拒绝路径；第 6 步 14 条拆句样本也通过严格分析。第 6/7 步拆句、Unicode、流关闭、补全/改写探针回归通过。
- 调用真实 `BehaviorRelations` 核对 WRITE/PROGRAM 权限类别；调用真实规则执行器及内置 `rule_update_001` / `rule_delete_001` 验证无 WHERE 拒绝，有 WHERE 自定义规则通过，并验证按物理表配置的规则能命中外层 CTE 查询。没有用 mock 规则结果替代执行。
- 长期样本见 [08-analysis.sql](sql/08-analysis.sql)，页面流程见 [HANA 查询授权与审核](../../frontend/security/hana_query_authorization.md)。临时探针与构建日志在 `/private/tmp/hana-step8*`，可能被清理；上述检查不代表平台鉴权服务、真实数据库与 UI 的端到端验收。
- @Browser 打开 `http://localhost:8222/#/sql` 返回 `ERR_CONNECTION_REFUSED`，未完成页面操作；无 HANA 实例，E1/E2 保持 NOT RUN。

### Express 部署与页面验收

现在可在 HANA Express 上验证 **SQL 工作台对象授权、静态 DO 块和查询规则阻断**。SQL 实现已迁至独立 `sql-hana` 子工程，由 HANA 插件包包含其 JAR；首次部署第 6～8 步组合时仍需包含第 6 步平台和第 7 步 Web/dsc-common 修改的运行包，重启加载插件。默认隐藏保持不变，隔离环境新增入口仍可使用 `/#/datasource/add?dsType=Hana`。

按上述页面流程分别验证：平台只读用户搭配数据库可写账号拒绝 UPDATE/块内写入；未授权表经 CTE/JOIN/子查询仍被拒绝；CALL 缺少 PROGRAM 权限拒绝；有写权限时无 WHERE 的 UPDATE/DELETE 命中阻断级规则。另用数据库只读账号验证数据库侧拒绝，区分两层权限。所有写入只针对一次性 schema，失败后由独立只读连接核对数据未变。遇到未覆盖语法记录完整脱敏样本，不绕过分析。

### SQL 模块组织

解析相关代码位于 `backend/clouddm-plugins/clouddm-sql/sql-hana`，包名为 `com.clougence.sql.hana`，包含语法、拆句、SQL 引擎/插件、行为 visitor、安全域、查询改写、版本及 SQL 国际化。`ds-hana` 保留连接、元数据、编辑器服务与规则能力注册，单向依赖 `sql-hana`；SQL 模块不依赖 HANA 数据源或 JDBC 驱动。

可以独立运行 `./gradlew :sql-hana:build`，后续模块测试使用 `:sql-hana:test`，无需加载连接/页面实现。本次未新增测试类，不能把 NO-SOURCE 当成已有 HANA 单测。结构迁移后，第 6～8 步离线探针全部回归通过；插件扫描既有测试通过。打包检查确认嵌套 `sql-hana` JAR 包含新的 SQL 插件、唯一一组 Lexer/Parser 和国际化资源，旧包名及 Analysis Lexer/Parser 不再打包。


## 14. 第 9 步：血缘与脱敏

### 实现与契约

- `sql-hana` 的 `HanaLineageAnalysisSpi` 使用同一份 HANA 严格语法，构建共享血缘模型并解析真实列来源；不再返回 EMPTY。`MetaService` 由 SQL 插件注入，每次分析独立缓存元数据，不跨用户/上下文复用。
- 支持列/表别名、JOIN、CTE 及列改名、派生表、相关/非相关标量子查询、集合查询按位置合并来源、星号展开以及表达式参数来源。引用范围保留原 SQL 位置；未加引号名称折叠为大写，引号名称保持大小写。
- `HanaMetaProviderDm.loadSelectObject` 补齐之前未实现的查询对象入口：表读 `SYS.TABLE_COLUMNS`，视图读 `SYS.VIEW_COLUMNS`；元数据为空不伪造列。查询级血缘据此拒绝无法解析的来源。
- 共用 `LineageResolver`、`CteScope`、`ResolvedRelation` 新增可选大小写敏感/严格解析，HANA 显式启用；旧入口仍不区分大小写且允许原有未解析列处理。共享解析器不涉及会话或线程。平台另补一个小范围脱敏约束：无血缘且含 PROGRAM 调用时，不允许仅凭 READ 对象豁免关闭整条语句的值处理器。
- HANA 声明 `FUNC_LINES_SUPPORT`，脱敏规则开放 schema、table、view、column 的精确/前缀/后缀/包含范围。分析产物按既有 `ColumnConfig.sourceNames` 接入平台：只移除获敏感数据豁免的来源，其他来源继续参与规则；多来源按现有执行器选择脱敏算法。

### 明确边界

| 情形 | 当前行为与验收要求 |
| --- | --- |
| 已知常量、系统值 | 无物理列来源；不得与未知列/元数据失败混为一谈。输出表达式存在物理来源时，最外层需显式别名，保证与 JDBC 标签一致 |
| 未知列、歧义列、缺失元数据、不支持语法 | SELECT 血缘报错停止执行，不能按无来源跳过脱敏；本轮使用的异常包含简短原因 |
| UDF、表函数、会话参数值、SQLScript 变量 | 无法确定来源的 SELECT 明确拒绝；不能只追踪函数入参而忽略函数内部读取。内置函数白名单有限，未收录函数保守拒绝 |
| 视图 | 展开视图列的星号并追踪到视图列，规则/豁免按视图列配置。没有递归展开视图定义，不能承诺基表规则自动覆盖所有视图；底层展开需另补元数据契约与循环/权限处理 |
| 同义词/远程对象 | 当前查询元数据只支持表与视图；来源不可获得时拒绝，不冒充普通表 |
| CALL/DO/SQLScript 结果 | 平台不调用 SELECT 血缘入口，保留既有 JDBC 元数据脱敏路径；程序内部表达式和多结果集未获得完整血缘保护保证。第 11/14 步必须单列验证，不能凭本步查询探针开放这些能力 |
| 表数据读取、查询结果导出 | 复用既有值处理器；表数据读取依赖 JDBC 来源，查询结果导出消费查询配置。代码检查及执行器探针不是完整产品链路证据，仍需实库下载/读取验证 |

平台已修复 DO 同时含普通 SELECT 与 CALL 时的整条语句豁免：程序来源未知，必须保留值处理器；普通只含 READ 的既有豁免行为保持不变。这不等于补齐程序内部列来源，JDBC 无来源的程序表达式仍需专项实现与验收，HANA-022 不能据此结项。

### 验证与部署

```bash
cd backend
./gradlew :sql-hana:build :ds-hana:build :ds-hana:customFatJar :sql-mysql:test :dsc-common:test :cgdm-console:test :plus-sec-rules:test --offline --max-workers=4
```

构建与已有 1308 项测试结果通过（部分任务 UP-TO-DATE）。临时探针实际调用 HANA/共享血缘解析器、平台 `configMaskingWithProvenance` 与 `SecValueProcessServiceProvider`，覆盖别名、表达式、CTE、集合来源、大小写豁免与部分豁免；元数据使用 JDBC 代理验证表/视图入口及资源生命周期。没有新增测试类，没有运行真实 HANA。第 7/8 步相关离线探针回归通过。详细记录见矩阵的第 9 步本地证据。

**现在可部署 Express 验证查询结果脱敏**：使用包含本步 SQL/共享 SQL 模块的新 HANA 插件包，重启加载；本次还需部署包含程序豁免修复的 console 运行包；首次部署仍需要第 6/7 步平台与 Web 组合包。普通用户配置列范围规则后，按 [HANA 血缘与脱敏流程](../../frontend/security/hana_lineage_masking.md) 和 [09-lineage.sql](sql/09-lineage.sql) 验证改名、表达式、CTE、JOIN、UNION、视图列、部分豁免及查询结果导出。HANA 仍默认隐藏，可沿用隔离环境直达新增入口。

本地 @Browser 页面连接拒绝，真实 HANA、浏览器结果和导出文件未验证；HANA-021/022 保持 NOT RUN。格式化硬编码待办 `HANA-017/a` 仍是第 13/15 步门禁，本步没有关闭它。

## 15. 第 10 步：结构编辑与对象脚本

### 实现与范围

- 建表保留物理列顺序、PK 名称、独立 UNIQUE 和 FK；索引唯一性与物理类型分离，支持 BTREE/CPBTREE/INVERTED VALUE/HASH/INDIVIDUAL 及列顺序。唯一约束使用 `DROP CONSTRAINT`，普通索引使用 schema 限定的 `DROP INDEX`。修改索引继续复用公共编辑器删除/重建编排。
- 表/列注释可清空，生成 `COMMENT … IS NULL`；清空默认值生成局部 `ALTER … DEFAULT NULL`。列属性仅修改变化部分，注释变化不会重新声明 identity。默认值自定义框输入 SQL 表达式，字符串必须包含 SQL 引号；空字符串选项对应 `''`。长度/精度越界明确报错，不再静默截断。
- 新列可选择 identity（BY DEFAULT AS IDENTITY）；已有生成列只支持本轮可保留语义的操作，如独立修改注释。修改其生成模式、类型、默认值或可空性明确拒绝，需使用经过核对的原生 DDL。
- 表属性读回 ROW/COLUMN，支持生成存储转换 SQL。UI 往返保留原始排序属性，不为缺省属性合成额外差异；两个 UNIQUE、FK、identity、默认值及顺序属性的无修改往返探针不产生 DDL。
- 表、视图、触发器、过程、函数、序列、同义词的取脚本统一调用 `CALL SYS.GET_OBJECT_DEFINITION(?, ?)`，读取 `OBJECT_CREATION_STATEMENT`。绑定 schema/对象名、校验当前 tenant，完整保留 SQLScript 主体；关闭 statement/result，不关闭借用连接。空结果或调用失败明确报错，不用不完整模板伪造成功。
- 接通表/视图和序列的获取脚本菜单；补过程/函数 DROP 模板及首个 UPDATE 触发器事件的 UPDATE OF 列。未实现的程序 CREATE/ALTER/COMPILE、无法保留复杂定义的触发器 ALTER/COMPILE 和 schema rename 入口不展示。
- 严格语法同步支持编辑器输出的局部 ALTER、DROP PRIMARY KEY、ROW/COLUMN、索引物理类型、约束列顺序、RENAME COLUMN/INDEX；继续使用唯一一套 HANA Lexer/Parser。

**能力边界**：不提供全文/空间索引的图形化重建，不能丢掉其原生选项后创建普通索引；独立 ARRAY 不作为编辑器可选类型。高级生成表达式、identity 序列选项、复杂触发器和过程定义以原生对象脚本为准，表模型转换不能替代完整对象备份。SAP 文档未保证的 CHAR/NCHAR 等旧类型仍需按实际服务器核对。本轮没有实现任意厂商 DDL 的工作台解析；取脚本成功不意味着该脚本已通过工作台严格分析或可等价回放。

原生对象定义入口参考 SAP 的 [GET_OBJECT_DEFINITION 使用说明](https://community.sap.com/t5/human-capital-management-blog-posts-by-sap/hana-most-common-sql-statements-amp-functions/ba-p/13488167)；ALTER/索引语法核对 [Platform 2.0 SPS 06 SQL Reference](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.06/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf)。目前没有实库证据证明目标 revision 对七类对象都返回相同结构和完整定义，需逐类核对；不能把代理 JDBC 返回的模拟脚本作为厂商兼容性证据。

### 索引所属表与公共改动

`DROP INDEX` / `RENAME INDEX` 的语句本身没有表名。本轮通过 `SYS.INDEXES` 的 schema/index 精确查询取得所属表，再生成该表的 ALTER 权限关系和索引审核域。索引不存在、缺少元数据上下文或查找失败时拒绝执行；不按当前 schema 中的同名表猜测，不缓存所属关系。普通 SELECT 不新增索引元数据查询。批次中尚未执行 CREATE 的新索引无法预先查询，需先创建成功再发起 DROP/RENAME。

为复用现有本地/Sidecar 元数据调用链，公共侧只增加：

1. `BehaviorAnalysisSpi.analysisBehaviorWithContextStream` 默认方法，携带用户、数据源和层级；默认转发原入口，现有其他数据源实现继续沿用原逻辑。查询分析入口传入该上下文，未做解析复用优化。
2. `MetaService.fetchIndexedObject` 默认能力及 console 实现，返回 `MetaIndexedObject`（type/catalog/schema/name），表示被索引对象，schema 仅表示命名空间。HANA 的 Index 详情明确返回 Table 类型；`RdbIndex.indexedObjectType` 经现有 `Value` 序列化链传输，console 不推断对象类型。HANA 行为/审核使用返回对象的 schema/名称，拒绝缺失、非 Table 或跨 catalog 的结果；其他数据源后续按真实类型接入。远程 Index 详情绕过对象详情缓存，避免错误套用不存在的索引详情缓存类型。
3. 序列“获取创建语句”的菜单标识、白名单、国际化和前端事件分发，复用其他对象的原有请求链。

未修改公共会话生命周期、取消、事务或队列终态。部署必须同时更新 console/SDK/schema 运行包、HANA 插件（含 sql-hana）及 Web 资源，不能只替换插件。

### 已声明 DDL 转换目标

转换先克隆源表，不能修改当前表编辑器的 catalog/schema 或列约束。同方言 HANA 输出直接使用 HANA 生成器，绕过会合并/重排键列的异构迁移处理器。

| 目标 | 本轮本地检查和明确边界 |
| --- | --- |
| HANA | 探针保留列顺序、PK 名称、两个 UNIQUE、FK 和普通索引；模型含 CHECK 时拒绝转换，高级对象选项仍应取原生脚本 |
| MySQL、TiDB、OceanBase | 简单 INTEGER/NVARCHAR/DECIMAL(18,4) 输出已检查；不代表已在目标库执行或所有 HANA 类型/表达式等价 |
| Doris、StarRocks | 简单列输出已检查；包含 PK 或索引时拒绝，防止源唯一性被改成不同的目标键语义；字符串按既有字符/字节换算输出 |
| AdbForMySQL | INTEGER 简单表可输出；有列长度、PK 或索引时拒绝，避免已观察到的长度/键丢失 |
| 所有异构目标 | 带 FK/CHECK、生成列/identity、SMALLDECIMAL 或未指定精度的 DECIMAL 明确拒绝，要求人工给出目标定义 |

上述拒绝覆盖本轮实际发现的语义损失，不是“所有剩余组合无损”的保证。默认表达式、类型边界、排序规则等仍需按实际迁移需求在目标引擎执行回读；未将“生成非空 SQL”记为等价迁移验收通过。

### 本地验证与待验收

```bash
cd backend
./gradlew --offline :ds-hana:build :sql-hana:build :ds-hana:customFatJar :cgdm-console:test :dsc-common:test :sql-mysql:test :plus-sec-rules:test
cd ../frontend
npm run lint -- --no-fix src/utils/index.js src/views/sql/components/TableList.vue
npm run check-i18n
cd ../package
./all_build.sh web
```

- 后端构建、插件打包、相关既有测试 1308 项及 Web 完整构建通过；HANA 模块无测试源码，本轮未新增测试类。未变化模块复用 Gradle 的有效测试结果。前端修改文件 lint 通过；check-i18n 正常结束但因只扫描暂存文件而未扫描本轮内容，新增菜单和表单文案的默认/中文/英文资源另行核对齐全。
- 临时编辑器探针调用真实 UI 映射及 `TableEditorImpl.diffActions`，覆盖无修改、两个 UNIQUE/FK、索引排序 JSON 往返、重建 SQL、注释/默认值清空、identity 注释、长度/精度拒绝和源对象不变。
- JDBC 代理验证对象脚本多行结果、内部分号、参数绑定、空结果/异常、资源释放，以及索引详情现有/不存在路径。行为/审核探针验证跨 schema 引号名称、真实表关系、索引不存在拒绝和 `RdbIndex` 的实际 JSON `Value` 往返；这些不替代真实平台权限测试。
- 六个异构目标调用实际转换器检查简单输出与已知有损场景拒绝；新长期样本 17 条语句通过严格解析，第 8 步行为分析与第 9 步血缘探针回归通过。日志为 `/private/tmp/hana-step10*.log`，临时证据可能被清理。
- 2026-10-02 @Browser 打开 `http://127.0.0.1:8222/#/sql` 返回 `ERR_CONNECTION_REFUSED`；没有真实 HANA。页面、执行回读、普通账号对象定义权限、七类脚本等价回放、异构目标执行全部保持 **NOT RUN**，HANA-023/024/025 不标 PASS。

### Express 部署与页面验收

**现在可以部署 HANA Express 验证结构编辑和对象脚本。** 更新上述完整运行包后，按 [HANA 结构编辑与对象脚本流程](../../frontend/sql/hana_structure_editor.md) 执行；准备样本见 [10-structure.sql](sql/10-structure.sql)，按区段运行，勿整文件批量执行。

优先验证：无修改预览为空；注释/默认值清空；两个 UNIQUE/FK 不互相影响；复合索引增删重排及刷新；ROW/COLUMN 转换；七类对象脚本与原生结果比较及隔离副本回放；只授一张表 DDL 时，跨 schema 同名索引不能操作未授权表。记录部分 DDL 失败后的真实结构，不能假设整批自动回滚。

HANA 默认隐藏仍保留。格式化特例 **HANA-017/a** 仍由第 13 步完成，并作为第 15 步开放门禁；本轮结构/脚本完成不关闭该待办。

## 16. 第 11 步：数据编辑与导出回放

### 行定位与写入契约

- ROW/COLUMN 表采用完整主键；无主键时选一组所有列均 NOT NULL 的 UNIQUE。独立 UNIQUE 不合并，缺失任意键列拒绝写入。移除对 `$rowid$` 的依赖；未取得其跨请求稳定性证据，不把它作为无键表的替代定位。
- 无可靠键、可空唯一键和视图在数据编辑器中只读；这是本轮安全定位边界，不代表 HANA 原生 SQL 不允许向无键表/部分视图写入。无键表需先建立键，或自行通过审核后的 SQL 操作。
- HANA 预览和保存重新读取数据库元数据，不采用页面声明的类型、主键或只读标志。页面用完整键；键被脱敏、读取失败、缓存截断或达到显示长度边界时保守只读。达到边界的完整键也可能只读，应提高显示长度并重新加载。
- identity/生成列只读；普通列支持 NULL、空字符串与省略使用默认值。新增默认列留空时省略，右键“设置为 NULL”明确提交 NULL；空新增行通过可选 `dmlType=INSERT` 区分。只有 identity 的行使用 `OVERRIDING USER VALUE SELECT NULL FROM SYS.DUMMY`。自动键回读失败只要求刷新，不能把已提交 INSERT 当作失败重试。
- UPDATE/DELETE/INSERT 预期影响一行，0 行明确失败；分页在用户排序后补完整键排序。分页请求各自连接，不承诺并发写入期间 OFFSET 跨页快照或乐观并发控制。

### 类型与 SQL 文件

`HanaSqlValues` 同时服务实际编辑 DML 和 SQL 导出：数值先按整数/BigDecimal 解析，不经过 double；布尔规范化；Unicode/引号转义；DATE/TIME/TIMESTAMP 使用显式格式；TIMESTAMP 保留 7 位小数，超过精度拒绝而不静默截断；二进制检查偶数 HEX 并调用 HEXTOBIN。词法同步支持 `N'...'` 与科学记数，HEXTOBIN 纳入内置函数。

普通字符串、CLOB/NCLOB/TEXT、BINARY/VARBINARY/BLOB 接入转换；ARRAY、ST_POINT、ST_GEOMETRY、BINTEXT 暂不声明可编辑。LOB 的服务器长度限制、非常规时间边界与驱动读取仍需实库验收，不能从短字符串探针推导出任意大 LOB 已完成往返。

服务端 SQL 导出新增 HANA 值处理器，禁用多行 VALUES 合并，UTF-8 输出；保留导出前脱敏。数字导出不受界面显示长度截短；二进制 NULL 与零长度值保持不同。选中列存在读取错误/截断时失败，不写成 NULL 或不完整成功；异常/线程中断关闭读写资源并删除部分 SQL 文件。取消检查发生在转换行循环，不改变公共任务队列和查询会话生命周期。

语法依据见 SAP [Platform 2.0 SPS 08 SQL Reference](https://help.sap.com/doc/9b40bf74f8644b898fb07dabdd2a36ad/2.0.08/en-US/SAP_HANA_SQL_Reference_Guide_en.pdf)，尤其 INSERT 的列省略、单行 VALUES 与 identity override。厂商语义仍需约定 revision 实测。

### 导入入口与边界

本轮贯通“查询结果 → HANA SQL 文件 → SQL 编辑器回放”。现有 SQL 文件上传属于工单/CI/CD，复用 HANA 流式拆句、行为与权限分析；完整审批执行、重试和取消由第 12 步验收。仓库当前没有通用 CSV/XLSX 数据导入向导，本轮不新增此产品能力。表数据页本地 CSV 仅导出已加载值，不作为 LOB/NULL 无损备份；JSON/XLSX 仍走原有展示导出链路。

回放先创建隔离目标表，显式填写带 schema 的目标名称。ALWAYS identity 和计算生成列不能直接回放普通 INSERT，导出时排除这些列或使用接受原值的目标定义；脱敏账号导出的数据不能恢复原值。SQL 文件不自动开启整批事务：错误前已提交的语句不会自动回滚，应核对数据库后只重试未成功部分。

导出转换当前没有独立页面取消 API，关闭页签不会中断服务端转换。本轮验证转换器收到线程中断时的释放；页面查询“取消”仍走第 4 步查询取消链路，不把二者混为一谈。

### 公共改动及验证

1. 数据编辑 SPI 添加默认关闭/空操作的服务端元数据、只读和影响行数校验钩子，仅 HANA 启用；内部编辑结果携带实际显示长度，避免截短的数字键被当作完整键。
2. ChangeRowFO 可选 INSERT 类型支持默认值空行，旧请求仍按原有 whereData/newData 推断。前端所有数据源统一省略未填写的默认值/自增列和插入只读列，显式 NULL 保留为 null；公共 INSERT 按字段是否存在区分省略与 NULL，全默认值行由方言钩子生成。HANA 保留专用值转换和 identity 插入实现。通用回归见 [数据编辑默认值与 NULL](../../frontend/sql/data_editor_defaults.md)。
3. 批量编辑纠正 `res.success` 与 `res.data.success` 混用，业务失败、HTTP 失败和请求异常均停止后续行并保留“未执行”状态；适用于所有数据源。
4. 缓存读取修复二进制 NULL 被转换为空 HEX；SQL 转换统一 UTF-8、查询注释转义、中断检查、失败文件清理及脱敏 finish。所有数据源的 SQL 导出均使用独立的完整读取配置，不受页面显示长度影响；选中列读取失败、缓存截断或尚未读完时终止导出，未选中列不触发该检查。其他方言的值生成和合并 INSERT 限制不变。没有改公共事务、会话取消或 SessionAgent 队列。

本轮未新增测试类。后端构建、HANA 插件打包、1308 项既有测试与完整 Web 构建通过；HANA/导出/结果文件模块没有测试源码。临时真实缓存文件导出探针、编辑/严格解析探针（样本及空 INSERT 共 30 条语句）和 Vue 方法状态探针通过；Java 已按 `codeformat.xml` 格式化并整理 imports，前端修改文件 lint 通过，check-i18n 因无暂存文件未扫描（本轮没有新增前端文案）。临时日志 `/private/tmp/hana-step11*.log` 可被清理。页面服务不可达，真实 HANA 往返、长 LOB、平台权限与工单文件执行仍保持 **NOT RUN**，HANA-026/027/028 不标 PASS。

**现在可以部署 HANA Express 验证本步页面功能。** 需更新 console/SDK/schema、HANA（含 sql-hana）、plus-file-convert 和 Web；测试样本见 [11-data-edit.sql](sql/11-data-edit.sql)，浏览器流程见 [HANA 数据编辑与 SQL 导出回放](../../frontend/sql/hana_data_editor.md)。优先验证复合键只改一行、identity/默认值/NULL、7 位小数秒、导出后隔离回放及第二行失败时第三行不执行。HANA 默认隐藏和 HANA-017/a 格式化门禁保留。


## 17. 第 12 步（部分）：原生计划节点、估算行数与来源

本轮仅修正 `HanaExplainPlanSpi`，不调整公共工单状态、事务、重试、EXPLAIN 支持范围或会话执行流程。

- 原生节点保留 `OPERATOR_ID/PARENT_OPERATOR_ID`、算子名称、执行引擎、表名及原始属性；同时映射 `SUBTREE_COST` 和 `OPERATOR_DETAILS`。不再用行为分析覆盖第一个节点，也不依赖返回顺序判断写入目标。
- `OUTPUT_SIZE` 只表示该算子的估算输出行数。保留合法的零值和小数；缺失、非法、负数或非有限值保持未知，不借用其他节点的正数。语义依据为 SAP [Platform EXPLAIN_PLAN_TABLE](https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20a3dbac75191014b400b72d0f02eabe.html?locale=en-us)。
- 实际解析出具有 ID 和算子名称的节点才标记 `NATIVE`；仅有消息、表头、空结果集或非计划行不构成原生计划。存在原生节点时不叠加语句推算，也不因传入 relations 就标记 `MERGE`。
- 没有原生节点时，按写入关系生成独立语句节点并标记 `STATEMENT`；仅已知的 `insertRows` 提供行数，未知 UPDATE/DELETE 等不填入数字。没有可生成节点的关系时返回空计划，source 保持 null。
- 原生表名不伪装成平台资源路径。当前未实现从 HANA 写入算子到目标对象的可靠影响行数映射，因此工单中此类原生 DML 的影响行数保持未知（`--`）；已知行数的 VALUES INSERT 仍走语句推算。公共工单如何把“未获得原生计划”归类为失败或其他状态，留在第 12 步后续失败反馈处理，本轮没有修改其 SUCCESS 状态规则。

### 回归场景

1. 返回顺序为子节点在前、父节点在后，核对 ID、父子关系、算子、对象、成本和详情原样保留。
2. 写入节点输出为 0，扫描节点输出为正数，零值不能被替换；写入节点输出未知时也不能借用扫描结果。
3. 同时传入 INSERT 行数和原生计划，原生节点不能被 INSERT 行数覆盖，来源仍为 NATIVE。
4. 对消息、仅表头、空行集、缺少元数据及缺少算子 ID/名称的结果，确认不会产生原生来源；已知 INSERT 可退回语句推算，纯读取关系不能凭空生成计划。
5. 语句推算覆盖多个写入关系、已知零行 INSERT、未知 UPDATE；节点 ID 不重复。通过公共工单行数汇总方法核对 INSERT 行数可读取、原生扫描行数不会被误当成目标影响行数。
6. 在 Express 中通过 SQL 编辑器检查原生计划；通过[工单详情流程](../../frontend/ticket/ticket_detail.md)的 TD-MAIN-02 检查已知 INSERT 与未知 DML 的展示。执行计划不能实际写入样本表；同一语句的数据库原生计划作为对照。

本地验证：`:ds-hana:build :ds-hana:customFatJar :dsc-common:test :cgdm-console:test` 离线成功，console 52 项既有测试通过，dsc-common 16 项测试复用 up-to-date 结果；ds-hana 没有测试源码。临时结果集及公共汇总探针通过 57 项断言，未在仓库新增测试类。Java 已执行 `codeformat.xml` 格式化及基于 JDT 语法/引用分析的等效 Optimize Imports。临时日志为 `/private/tmp/hana-explain-build.log` 和 `/private/tmp/hana-explain-probe.log`，不作为长期验证资产。

@Browser 访问 `http://localhost:8222/` 返回 `ERR_CONNECTION_REFUSED`，未执行真实页面或 HANA 计划验证。HANA-029 的 E1/E2 继续保持 NOT RUN；第 12 步整体尚未完成。


## 18. 第 13 步：页面能力与国际化

### 实现范围

- 工具栏和编辑器共用 `canFormatSql(tab)`，以 `tab.support.format.conf` 的 Allow/Hint 决定可用性，未知或 No 不开放；后端 `HanaSupportSpi → DmSupportSpiWrapper → QueryEditorController` 的能力链保持不变。
- 新增统一方言分派 `sqlFormat.js`。HANA 使用其词法保留式排版；其他已声明支持的关系型数据源按实际枚举绑定 `sql-formatter@15.8.2`（精确版本写入 package-lock），包括 MySQL 与 Oracle。格式化失败显示本地化提示并保留原文，不静默退出；重复格式化不新增空撤销记录，单次全文替换保留撤销边界。
- HANA 格式化支持 SQL 子句、字段列表、表定义、子查询、运算符，以及 BEGIN、IF/ELSEIF/ELSE、WHILE、FOR、LOOP 和 CASE 层次；注释、字符串、标识符保持原文本。嵌套块注释、块修饰符、数值指数与 SQLScript 变量保持 token 内容；未闭合词法结构或块、不能保留 token 的输入拒绝替换。它不承担 SQL 语义验证，也不声明支持所有厂商扩展。
- 函数菜单改用明确的函数菜单定义，修复继承公共默认空菜单导致脚本入口缺失；同义词增加脚本、复制、刷新及平台权限入口。移除过程/函数/触发器未实现的属性入口；不支持的图形修改和编译入口继续排除。表的获取 DDL、视图/序列脚本及已有编辑流程保留。
- HANA 插件新增 24 个中英文错误资源，覆盖结构编辑、数据编辑、对象脚本和 DDL 转换限制；保留对象名/行数/范围参数。编辑器语法诊断和格式化反馈统一使用前端中英文资源，删除旧 HANA 专属格式化错误 key。

### 页面入口核对

| 页面 | 当前处理与验收边界 |
| --- | --- |
| 连接表单 | tenant/schema、端口、JDBC/SSH/TLS 与超时已沿用第 2 步本地化配置；需要实库连接验证 |
| 对象树及脚本 | 七类对象脚本入口已核对；函数/同义词补齐，空属性入口移除；定义读取仍需实库 |
| SQL 编辑器 | 格式化按服务端能力控制；HANA、MySQL、Oracle 有实际实现；补全/拆句/结构校验沿用已实现链路 |
| 结构与数据编辑 | 本轮补限制提示国际化；行为验证复用第 10/11 步长期流程 |
| 规则与脱敏 | 已有 HANA 支持范围和分析链保留，复用权限与血缘页面流程，不扩展动态 SQL 承诺 |
| 工单与导出 | 使用现有公共入口；本轮未扩大事务/重试能力，SQL 文件导出边界仍见第 11/12 步 |
| 数据源展示 | `display=false` 保留；默认开放属于第 15 步，不以本次 UI 修正绕过未完成验收 |

### 验证与部署

离线定向检查覆盖 103 项前端断言（包含编辑器真实 formatSql 方法的替换/重复/拒绝路径）、17 份原文与格式化结果的 HANA 原生 lexer token 对比，以及 155 项菜单/能力/国际化资源契约检查；24 条新增前端消息通过 Vue I18n 编译。Java 按 codeformat.xml 格式化并完成基于 JDT 引用分析的等效 Optimize Imports。后端构建与插件打包成功；console/dsc-common 的 68 项既有测试复用 up-to-date 结果，未新增测试类。

前端变更文件 lint 与 2 个既有测试套件的 8 项单测通过，完整 Web 构建 `cd package && ./all_build.sh web` 成功。`npm run check-i18n` 因无暂存文件未扫描，另行执行了新增 key 的中英文存在性和消息编译检查。临时探针和构建日志位于 `/private/tmp/hana-step13-*`，长期复测以上述场景及页面流程为准。

部署需更新 HANA 插件与 Web，重启/刷新后重新打开 SQL tab 获取能力。可以在页面验证格式化按钮、函数/同义词脚本菜单、结构/数据编辑限制提示及中英文错误反馈。

@Browser 访问 `http://localhost:8222/` 返回 `ERR_CONNECTION_REFUSED`，页面操作与实库未执行。长期流程见 [HANA 页面能力与国际化](../../frontend/datasource/hana_datasource.md)。**HANA-017/a 不关闭，第 13 步页面验收仍待可访问的 CloudDM 与测试连接**；第 12 步剩余事务、重试及原生计划生命周期也未因本步完成而通过。
