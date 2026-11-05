# IoTDB 接入基线与复测入口

本目录维护 [版本与能力矩阵](iotdb-test-matrix.md) 和可复跑的实库样本。实库证据按 1.3.x Tree、2.x Tree、2.x Table 分开记录。产品页面、授权和发布包验收在开发完成后统一执行。

## 首批范围

- **树模型**首批覆盖连接、库与设备浏览、测点详情、查询，以及 SQL 方式的受控写入、按测点和时间删除数据、创建和删除测点。设备用于分组，测点授权使用完整路径。
- **表模型**首批覆盖连接、库与表浏览、列角色、查询和受控 DML、基础 DDL。表级授权与树侧测点授权分开。目标版本支持树转表视图时，单列只读展示和授权边界。
- 可视化结构和数据编辑、批量导入、血缘、脱敏、工单及 CI/CD 单列评估。已知语法与语义边界通过实库用例确定。
- 左侧数据源栏为「实例 → 模型 → 库」。1.3.x 展示树模型，2.x 按已验证的模式展开。同一个实例下的会话、方言、元数据和授权都按模型区分。

## 复用与接入点

| 范围 | 复用点 | IoTDB 接入内容 |
| --- | --- | --- |
| 插件、驱动和会话 | [ClickHouse 插件](../../../backend/clouddm-plugins/clouddm-ds/ds-clickhouse/src/main/java/com/clougence/clouddm/ds/clickhouse/ChPlugin.java)、[DefaultRdbSession](../../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/execute/session/rdb/DefaultRdbSession.java) | 独立数据源和 SQL 插件，维护服务端与驱动版本组合。以实际驱动能力验证会话、取消、事务控件和结果转换。 |
| 对象浏览 | [DsMetaService](../../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/execute/meta/DsMetaService.java)、[DsBrowseSpi](../../../backend/clouddm-platform/cgdm-plugin-sdk/src/main/java/com/clougence/clouddm/sdk/ui/browser/DsBrowseSpi.java)、[SQL 工作台](../../../frontend/src/views/sql/index.vue) | 树侧提供设备分组和测点叶子，表侧提供表、视图和列。模型、库与对象标识贯穿查询和浏览。 |
| SQL 与授权 | [QueryAnalysisServiceImpl](../../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/analysis/impl/QueryAnalysisServiceImpl.java)、[DmAuthServiceForBizImpl](../../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/auth/impl/DmAuthServiceForBizImpl.java)、[子账号授权页](../../../frontend/src/views/system/subaccount/auth/authDm.vue) | 按模型拆句和提取对象，树侧按完整路径校验，表侧按库和表校验。数据库授权和 CloudDM 子账号授权分别验收。 |

现有 [SQL 引擎选择](../../../backend/clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/component/dsconfig/impl/DmDsConfigServiceImpl.java#L356) 基于数据源配置，[授权页](../../../frontend/src/views/system/subaccount/auth/authDm.vue#L104) 的叶子页签以表和 Topic 为主。双模型上下文和测点叶子的接入方式先按插件 SPI 评估。涉及核心类修改（枚举除外）或数据源专用分支时，先讨论具体类、调用链和回归范围。

## 插件注册与驱动准备

- `ds-iotdb` 注册 `DataSourceType.IoTDB`、`DsType.IoTDB`、配置 SPI 和 `IoTDB` 序列化 provider。数据源按现有 JDBC/分析库分类登记，入口暂时隐藏。
- `sql-iotdb` 随数据源插件包加载，注册唯一的 `IoTDB SQL` 引擎，通过会话的 `sql_dialect` 选择 Tree 或 Table 解析器。语言服务复用编辑器上下文的模型和数据库。
- `IoTDB JDBC` 通过 `drivers.xml` 注册 1.3.7 与 2.0.11，默认 1.3.7 对应首个树模型基线。平台 Maven 下载器准备 Apache 发布的 `jar-with-dependencies`，让版本绑定一并加载 Thrift 等运行依赖。离线环境复用已准备的版本目录及其 `files.idx`。
- 工厂从版本绑定的类加载器取得 JDBC 驱动。连接字段和会话参数见下节。

构建入口为 `cd backend && ./gradlew :sql-iotdb:build :ds-iotdb:build :ds-iotdb:customFatJar`。用平台插件加载器验证 `ds-iotdb-lib.jar`，结果见[版本与能力矩阵](iotdb-test-matrix.md)。

## 连接配置

| 配置 | 当前支持范围 |
| --- | --- |
| 地址与认证 | 使用 DataNode 的 JDBC RPC 主机和端口，默认端口 `6667`。使用用户名密码认证，凭据经 JDBC 属性传递。自定义 JDBC URL 不在本步范围。 |
| 网络超时 | 高级配置中的 `networkTimeoutMs` 默认为 `5000` 毫秒，范围为 `1–2147483647`。映射为 JDBC `network_timeout`，建立连接与套接字读取共用此值。查询执行超时沿用平台独立配置。 |
| TLS/SSH | 当前支持直连。表单通过配置 SPI 隐藏 TLS 和 SSH 控件，显式启用 TLS 或 SSH 的配置会被拒绝。 |
| 连接模式与库 | 两种模式复用同一份实例配置。默认连接为 Tree，使用完整路径定位。2.x Table 连接按连接参数选择模式和可选初始库。工厂确认实际模式后执行带标识符引用的 `USE`，初始化失败时关闭该连接。 |
| 驱动安装 | 选择与服务端对应的 `IoTDB JDBC` 版本。在线通过平台下载器准备驱动，离线在实际执行节点复制完整版本目录和 `files.idx`。 |

JDBC 两个超时的定义见 [IoTDB 2.0.11 JDBC 连接实现](https://github.com/apache/iotdb/blob/v2.0.11/iotdb-client/jdbc/src/main/java/org/apache/iotdb/jdbc/IoTDBConnection.java#L78)。

## 会话与执行

| 行为 | 当前约定 |
| --- | --- |
| 模式与库 | 会话通过 `sqlParameters.sql_dialect` 选择模式，也可从浏览层级的 `Catalog` 取得模式，缺省为 `TREE`。Table 用 `rdbSchema` 保存当前库，执行 `USE` 后更新会话状态。显式重开时用该状态恢复模式和库。 |
| 执行和取数 | 复用 JDBC 结果读取与分页，JDBC fetch size 以页面取数配置为提示，服务端决定每个实际批次。SQL 投影的重复列名需要不同别名。 |
| 事务与更新计数 | 会话以立即提交模式执行，禁用事务控制、隔离级别切换和只读切换。成功写入沿用 JDBC 的 `-1`，表示无影响行数。 |
| 超时与取消 | 查询超时传给 JDBC，并作为客户端执行截止时间。套接字超时、达到客户端截止时间或取消请求时，释放目标 JDBC 连接和资源配额。取消通过断开目标连接实现，页面后续执行需要显式重开会话。 |
| 丢失响应 | JDBC 收到通信异常时，IoTDB 插件阻止驱动通过重连再次提交 SQL。Sidecar 保留逻辑会话以返回结果未知的错误，用户可先通过独立会话核查实际写入状态。 |

## 元数据与对象标识

| 范围 | 当前约定 |
| --- | --- |
| 层级与缓存 | 复用平台的 `Catalog` 和 `Schema`，在 IoTDB 中分别表示模型和数据库。元数据缓存与资源路径沿用这两段，同一实例的 `TREE`、`TABLE` 各有独立命名空间。 |
| Tree | 用 `SHOW DEVICES` 与 `SHOW TIMESERIES` 读取设备、测点和详情，列表用原生 `LIMIT/OFFSET` 分批取数后返回完整集合。设备和测点保留 IoTDB 返回的完整路径，测点额外返回所属设备与末段名称。 |
| Table | 用 `SHOW TABLES DETAILS` 区分表与视图，用 `DESCRIBE DETAILS` 读取列顺序、类型和角色。树转表视图保留 `VIEW FROM TREE` 类型、只读属性以及 `SHOW CREATE VIEW` 返回的原生定义。 |

树对象详情和列角色已通过 Sidecar 元数据接口返回。Console 的树对象详情转换、列角色展示和按模型组织对象页签纳入后续页面接入讨论。资源授权阶段需覆盖引用标识符中的路径分隔字符。

## SQL 解析与语言服务

| 模型 | 工作台首批语法 |
| --- | --- |
| Tree | `SELECT`、`SELECT LAST`、`ALIGN BY DEVICE`、常用表达式及分页、`INSERT ... VALUES`、按测点及时间条件的 `DELETE FROM`、创建普通或对齐测点、删除测点、基础库 DDL 和 `SHOW`。 |
| Table | `SELECT`、限定表名、别名、常用连接及子查询、`INSERT ... VALUES/SELECT`、`UPDATE`、`DELETE`、基础库及表 DDL、`ALTER TABLE ADD/DROP COLUMN`、`DROP VIEW`、`SHOW` 和 `DESCRIBE`。建表支持原生对象定义中的列角色、注释和属性。服务端决定可更新的列和可用于删除的条件。 |

- 拆句按当前模型识别注释、字符串和引用标识符，语言服务保留原文及范围。提交 JDBC 前移除脚本分隔符，并以空白替换 Tree 注释以保留 SQL 位置。
- Tree 行为分析提取投影和条件中的完整测点路径，分别标识删数据和删测点。通配符保留为选择器，资源范围归到已知库或模型。Table 提取真实库表，别名仅用于 SQL 表达式，子查询和 `INSERT ... SELECT` 分别标识读源和写入目标。
- 语言服务支持语法诊断、模型关键字和当前库缓存中的对象名补全。Tree 支持设备与测点路径，Table 支持表和视图名。列级补全和跨库补全留待后续评估。
- 脚本内切换模式或库、Tree `SELECT INTO`、其他 `ALTER TABLE` 操作、视图创建及绑定参数超出首批解析范围。语义 AST、SQL 重写和血缘单列范围。授权页和完整工作台验收继续覆盖通配符、原生测点别名、视图、跨库和特殊字符路径。

## 平台授权与执行拦截

- 工作台通过 `QueryResourceResolver` 在执行前核对资源。Tree 从实时数据库列表确认完整测点路径的归属，授权路径为 `/TREE/<database>/<full-timeseries-path>/`。Table 按实际限定库名或当前库生成 `/TABLE/<database>/<table>/`，子查询读源单独检查。
- 元数据查询复用 Sidecar 链路及配置的数据库账号。团队账号访问已有对象时，确认物理测点或普通表身份。SQL 投影别名 `AS` 和 Table 表别名保留实际对象授权，原生测点别名在本阶段拒绝执行。
- Tree 的通配路径及通配测点投影、不能确认身份的对象、包含路径分隔字符的资源、范围不明确的团队元数据语句和未列入安全函数集合的函数调用会在执行前拒绝。首批函数为 `NOW`、`COUNT`、`SUM`、`AVG`、`MIN`、`MAX`、`MIN_VALUE`、`MAX_VALUE`、`FIRST_VALUE`、`LAST_VALUE`、`MIN_TIME`、`MAX_TIME`，实际语法及语义仍以服务端为准。
- 团队账号查询树转表视图及 Tree 逻辑视图保持关闭。原生表模型的视图权限与树侧测点权限分别授权，因此访问开放前需另行验证视图来源和底层测点的团队授权。[IoTDB 表模型权限](https://iotdb.apache.org/UserGuide/latest-Table/User-Manual/Authority-Management_apache.html)
- 查询和受控 DML 使用已有查询、DML、DDL、库操作权限标签。团队账号对新对象先执行建库或建表、建测点，再单独提交后续 DML，以便按实时元数据确认对象身份。数据源所有者保留明确目标的 DDL/DML 批次和视图访问。
- Sidecar 在工作台用户 SQL 执行前核对已解析标记、模型及当前 Table 库，并复核团队对象仍为原生物理测点或普通表。覆盖排队期间对象被替换的情况，外部数据库管理员的并发 DDL 仍需要管理侧约束。

## 维护 SQL 与对象脚本

- 命令模板提供 Tree 普通及对齐测点的创建、写入、按时间删除数据和删测点示例，以及 Table 建表、增删列、FIELD 补写、ATTRIBUTE 更新和按 TAG 及时间删除的示例。示例中的 `root.demo` 和 `demo` 需替换为目标测试库。
- 对象列表通过已有 `objAttr.quickQuery` 返回快捷查询。测点查询明确列出当前测点及所属设备，Table 查询按实际库表名正确引用，默认取 20 行。Device 返回设备元数据查询，View 访问继续经过平台授权。
- Tree 的 `requestScript` 支持普通测点、对齐测点及设备。设备脚本包含其直接所属测点，保留对齐方式、类型、编码、压缩、原生别名、标签及属性。模式模板、逻辑视图和含 deadband 的定义返回明确的范围外错误。
- Table 对象脚本使用原生 `SHOW CREATE TABLE`，为返回定义补齐已读取对象的库名，保留列顺序、列角色、注释及 TTL。View 的结构维护和对象脚本尚未开放。
- 菜单按对象类型声明。设备和测点提供命令模板、刷新及复制名称，Table 提供原生定义、删除、刷新及复制名称，View 提供刷新及复制名称。可视化结构编辑、数据编辑和导入仍按首批范围单列。
- 测点 DDL 成功后，通用回调清理所属模型及库下的元数据缓存，覆盖创建首个或删除最后一个测点引起的设备列表变化。Table 增删列复用现有表结构缓存回调。

## 工作台与授权页面

- 实例沿用 Catalog/Schema 层级，按 TREE、TABLE 展开数据库。IoTDB 的模型及库节点在 `objAttr.leafTypes` 声明对象类型：Tree 为 DEVICE/TIMESERIES，Table 为 TABLE/VIEW。共享工作台和授权页据此筛选，未声明的数据源保持现有分类。
- 树侧浏览设备和完整测点路径，复用测点快捷 SQL。表侧浏览表与视图，视图带只读提示，菜单不提供维护或权限申请。现有会话和语言服务携带 Catalog 和 Schema，页签标题及选库选项保留模型。
- 新增 Timeseries 授权类型，插件按独立权限定义注册查询、DML、DDL 和库操作。授权与申请页使用“测点权限”，对象身份仍为 TREE/库/完整测点路径，不按设备授权。IoTDB 的表授权不包含视图。
- 右键申请复用通用权限申请页，传递并定位完整层级及对象；用户自行选择、预览和提交权限，资源不可用时提示重新选择。库表或测点名中的点号不拆分成授权目录。
- 编辑器复用已有语言能力和会话支持声明；事务、执行计划、不支持的格式化及可视化维护不额外开放。数据源图标暂复用通用数据库图标。
- [页面集成流程](../../frontend/datasource/iotdb_datasource.md) 分开覆盖 V1、V2T、V2R，并包含桌面、窄屏、双语及既有数据源回归。按用户安排，编译启动后再执行，当前页面验收保持 NOT RUN。

## 样本准备和清理

从能够访问远端容器 RPC 端口的机器连接，先用 `SHOW VERSION` 回读版本，再选用对应版本的 `org.apache.iotdb:iotdb-jdbc`。2.x 的表会话指定 `sql_dialect=table`。管理员用于准备样本，后续验证使用作用域受限的维护账号和低权限账号。

1. 每轮选一个新的小写标识 `<fixture>`。将 SQL 样本中的 `__FIXTURE_DB__` 全部替换为该标识后执行。树会话使用 [tree.sql](sql/tree.sql)，2.x 表会话使用 [table.sql](sql/table.sql)。两种模型共用名字尾段，分别得到 `root.<fixture>` 和 `<fixture>`。
2. 树侧普通设备 `line1.motor01` 与对齐设备 `line2.zone1.motor02` 深度不同，各有 `temperature` 和 `pressure`。表侧 `motor_samples` 以 `line`、`device` 区分两台设备，包含设备属性和采样字段。`motor_private` 用于授权反例。
3. 两个实例分别创建 `<fixture>_maint` 与 `<fixture>_read`，使用独立密码。树侧维护账号在 `root.<fixture>.**` 具有 `READ_DATA`、`WRITE_DATA`、`READ_SCHEMA`、`WRITE_SCHEMA`，低权限账号只获 `line1.motor01.temperature` 的 `READ_DATA` 和 `READ_SCHEMA`。2.x 表侧维护账号授予测试库权限，低权限账号只获 `<fixture>.motor_samples` 的 `SELECT`。同一 2.x 实例共用这对数据库账号，两种模型分别授权。
4. 维护账号回读两个设备和四个测点，以及表模型的两个表和四种列角色。低权限账号回读授权目标，确认同设备其他测点、`motor_private` 和写入操作被服务端过滤或拒绝。
5. 验收结束后，在各自模式下用管理员删除本轮精确命名的库，最后删除本轮两个账号。清理前核对实例、模式和标识。

后续开发中的验证证据继续回填[矩阵](iotdb-test-matrix.md)。页面用例维护在上述集成流程中，编译启动后的实机结果与离线验证分别报告。
