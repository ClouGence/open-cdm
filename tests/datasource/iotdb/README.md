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
- `sql-iotdb` 随数据源插件包加载，注册唯一的 `IoTDB SQL` 引擎。本步只装配引擎，拆句和行为分析在方言实现前明确拒绝执行。Tree/Table 元数据与方言分别接入后续步骤。
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
| 模式与库 | 会话通过 `sqlParameters.sql_dialect` 选择模式，缺省为 `TREE`。Table 用 `rdbSchema` 保存当前库，执行 `USE` 后更新会话状态。显式重开时用该状态恢复模式和库。 |
| 执行和取数 | 复用 JDBC 结果读取与分页，JDBC fetch size 以页面取数配置为提示，服务端决定每个实际批次。SQL 投影的重复列名需要不同别名。 |
| 事务与更新计数 | 会话以立即提交模式执行，禁用事务控制、隔离级别切换和只读切换。成功写入沿用 JDBC 的 `-1`，表示无影响行数。 |
| 超时与取消 | 查询超时传给 JDBC，并作为客户端执行截止时间。套接字超时、达到客户端截止时间或取消请求时，释放目标 JDBC 连接和资源配额。取消通过断开目标连接实现，页面后续执行需要显式重开会话。 |
| 丢失响应 | JDBC 收到通信异常时，IoTDB 插件阻止驱动通过重连再次提交 SQL。Sidecar 保留逻辑会话以返回结果未知的错误，用户可先通过独立会话核查实际写入状态。 |

## 样本准备和清理

从能够访问远端容器 RPC 端口的机器连接，先用 `SHOW VERSION` 回读版本，再选用对应版本的 `org.apache.iotdb:iotdb-jdbc`。2.x 的表会话指定 `sql_dialect=table`。管理员用于准备样本，后续验证使用作用域受限的维护账号和低权限账号。

1. 每轮选一个新的小写标识 `<fixture>`。将 SQL 样本中的 `__FIXTURE_DB__` 全部替换为该标识后执行。树会话使用 [tree.sql](sql/tree.sql)，2.x 表会话使用 [table.sql](sql/table.sql)。两种模型共用名字尾段，分别得到 `root.<fixture>` 和 `<fixture>`。
2. 树侧普通设备 `line1.motor01` 与对齐设备 `line2.zone1.motor02` 深度不同，各有 `temperature` 和 `pressure`。表侧 `motor_samples` 以 `line`、`device` 区分两台设备，包含设备属性和采样字段。`motor_private` 用于授权反例。
3. 两个实例分别创建 `<fixture>_maint` 与 `<fixture>_read`，使用独立密码。树侧维护账号在 `root.<fixture>.**` 具有 `READ_DATA`、`WRITE_DATA`、`READ_SCHEMA`、`WRITE_SCHEMA`，低权限账号只获 `line1.motor01.temperature` 的 `READ_DATA` 和 `READ_SCHEMA`。2.x 表侧维护账号授予测试库权限，低权限账号只获 `<fixture>.motor_samples` 的 `SELECT`。同一 2.x 实例共用这对数据库账号，两种模型分别授权。
4. 维护账号回读两个设备和四个测点，以及表模型的两个表和四种列角色。低权限账号回读授权目标，确认同设备其他测点、`motor_private` 和写入操作被服务端过滤或拒绝。
5. 验收结束后，在各自模式下用管理员删除本轮精确命名的库，最后删除本轮两个账号。清理前核对实例、模式和标识。

后续开发中的直连证据继续回填[矩阵](iotdb-test-matrix.md)。页面用例先在矩阵记录验收点，开发完毕后再按前端规则设计和执行正式流程。
