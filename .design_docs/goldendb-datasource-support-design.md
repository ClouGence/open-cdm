# GoldenDB 数据源支持设计文档

## 1. 背景与问题

目标是在 open-cdm 中把 GoldenDB 的实例级兼容模式建模为两个独立数据源：`GoldenDBMySQL` 与 `GoldenDBOracle`
。两类数据源分别绑定对应的驱动、SQL family、类型系统、元信息和表结构能力，不能在同一个 `GoldenDB` 配置中动态切换模式。

GoldenDB 官方 v6.1.03.10 文档说明其 MySQL 模式兼容 MySQL 8.0.25 及以前版本的数据类型、SQL、视图、函数和存储过程；但 GoldenDB
还增加了 `DISTRIBUTED BY`、分片组、复制表、全局索引等分布式能力。官方 JDBC 文档同时要求使用 `com.goldendb.jdbc.Driver` 和
`jdbc:goldendb://host:port/database`。因此，“MySQL 兼容”只能作为复用起点，不能等价为复用整个 MySQL 数据源插件。

厂商客户端包同时提供 `JDBC-MYSQL` 的 `gdb_mysql-connector-java-5.1.46.86.jar` 与 `JDBC-ORACLE` 的
`gdb_mysql-connector-java-oracle-5.1.46.77.jar`。两个 JAR 的入口类都为 `com.goldendb.jdbc.Driver`，但内部实现不同，必须使用两个独立
driver family 形成类加载隔离。

本设计的结论是：

- MySQL 模式直接绑定现有 MySQL SQL family，不新增 GoldenDB SQL Engine。
- MySQL 通用类型、方言、基础编辑器、语言服务和安全规则尽量复用。
- GoldenDB MySQL 与 GoldenDB Oracle 使用独立数据源类型、插件入口、配置、序列化、驱动族和 Session；连接工厂可共用 URL 与模式探针。
- 标准 MySQL 元信息查询经过真实 GoldenDB 验证后逐项继承；分片组使用官方 `SHOW DISTRIBUTION FROM <table>` 读取，不解析
  `SHOW CREATE TABLE` DDL。
- `DISTRIBUTED BY`、全局索引等 GoldenDB 专有 DDL 不扩展 MySQL parser；公开文档未给出稳定全局索引元信息接口前，不猜测
  GLOBAL/LOCAL 范围。
- GoldenDB Oracle 绑定 Oracle SQL family 和 Oracle 类型/对象模型；只复用已由 Oracle 模式实例证明存在的数据字典视图，并覆盖
  GoldenDB 不提供的 Oracle 产品版本与全局名称查询。

首批验证基线定为 GoldenDB v6.1.03.10。v6.1.02.10、v6.1.03.09 只在完成真实回归矩阵后加入支持范围。对外宣称运行支持完成前必须拿到可访问的
GoldenDB 测试实例、官方 JDBC JAR 及其再分发许可；拿不到时只能完成源码和自带驱动上传链路。

### 1.1 当前实施状态（2026-08-27）

当前执行证据、模式矩阵和未覆盖门禁统一维护在 `tests/datasource/goldendb/goldendb-test-matrix.md`；Oracle 专项证据维护在
`tests/datasource/goldendb/goldendb-oracle-test-matrix.md`。本节只保留架构阶段摘要。

- P1~P5 的源码、插件制品、前端入口、独立元信息门面和单层分发表工具已实现。
- P4 收敛为直接复用 MySQL 8.0 parser，页面只暴露 `MySQL`；GoldenDB 专有分布式 DDL 不进入自动 SQL 分析支持范围。
- 双模式边界已收口：只有公共配置、模式枚举、SessionFactory、DsFactory 和公共 i18n 保留无模式后缀；MySQL 专用的
  Session、元信息、编辑器、Dialect 与 SQL 分析类统一使用 `GoldenDBMySQL` 前缀并归入 `mysql` 子包。
- 分发元信息改为官方 `SHOW DISTRIBUTION` 返回的类型、分布键和分片组；删除 DDL 解析、原始分发子句和全局索引范围猜测。
- MySQL 对象分类只展示表/视图，Oracle 只展示表/视图/物化视图/序列；未完成分类不再返回假空列表。
- 当前两种类型均不声明 SSL 和查询中断能力；删除临时 StatementTracker，相关历史探针只作为数据库/驱动证据。
- 历史 P0 已在 GoldenDB Lite 7.3.02.02 ARM64 与官方 Java Connector 5.1.46.86 上完成增强后的真实 JDBC 探针：44 项通过、4
  项环境不支持、0 项失败。连接、MySQL 模式、强制 TLS、负载均衡 URL 单坏端点初始故障、四种隔离级别、标准 DDL/DML、事务、最小只读权限元信息、跨
  schema 同名视图/例程隔离、视图/函数/存储过程/触发器、`MyMetaProviderDm` 全部实际元信息
  SQL、ResultSetMetaData、Statement.cancel、事务内取消回滚和取消后连接恢复已通过；Lite 单 DN 不支持分布式表、全局索引和
  `KILL QUERY`；这些历史结果不用于当前产品的 SSL 和中断能力声明。
- 历史 P5 已在真实 CloudDM 页面完成 Lite 补充链路：独立官方驱动准备、普通与强制 TLS 连接、连接保存与回测、对象树与列/主键/索引元信息、有意义的
  INSERT/UPDATE/DELETE、手动事务 commit/rollback、Explain、
  `Statement.cancel()` 中断和同一 Session 恢复均通过；console/sidecar/alone 三个 tgz 均包含 GoldenDB fat JAR 且不携带未授权厂商
  JDBC JAR，浏览器测试数据已清零；当前产品已根据最新 CN 页面结果关闭 SSL 和查询中断。
- CloudDM 产品验收 SQL 和未通过边界统一维护在
  `tests/datasource/goldendb/goldendb-test-matrix.md`；历史直连报告仅作为数据库/驱动证据保存在本地
  GoldenDB 工程的 `artifacts/open-cdm/`。
- 2026-08-25 已在 GoldenDB RHV6.1.03.12SP5 分布式 CN MySQL 模式执行历史探针：46 项通过、2 项环境不支持、0 项失败。
  `SHOW DISTRIBUTION` 实际返回 `Table/Dist_type/Dist_key/Groups`，HASH、RANGE、LIST、DUPLICATE、全局索引和取消恢复均通过；
  当前服务端所有 JDBC 隔离级别请求均回读 READ COMMITTED，因此插件只声明并初始化 READ COMMITTED。
- 拆分后的 `GoldenDBMySQL` 已完成 CloudDM 测试连接、保存、分发元信息、DML、手动事务和表编辑器；
  全局索引物理表根据厂商 `TABLE_COMMENT` 标记在 GoldenDB Provider SQL 中过滤。`GoldenDBMySQLSession` 参考 Oracle
  Session，在 Explain 请求执行阶段基于原始 SQL 生成 CN 支持的普通 `EXPLAIN`，不修改 Hook 或暴露 GoldenDB SQL Engine。
- 2026-08-21 的 Oracle 专项探针最终为 23 项通过、0 项失败：本地 Lite 实例返回 `ORA_COMPATIBLE_MODE` 与
  `EMPTY_STRING_IS_NULL`；Oracle Connector 5.1.46.77 可连接并执行 DUAL、身份/版本查询、Oracle DDL、Prepared
  DML、事务、非空表/列/索引/PK/UQ/FK/视图/序列元信息、ResultSetMetaData 与 Statement.cancel 恢复。探针同时确认
  `ALL_TAB_COMMENTS.TABLE_TYPE=BASE TABLE`、外键 `CONSTRAINT_TYPE=F`，因此由 GoldenDB Oracle MetaProvider
  覆盖表列表和外键查询；修正后模块构建、SQL 探针、插件部署和 IDEA 后端重启已通过，Chrome 对象树复测仍未执行。

官方依据：

- [GoldenDB 与 MySQL 兼容性](https://www.goldendb.com/docsCenter/GoldenDB/61310/html/aboutGoldenDB_MySQLCompatibility.html)
- [GoldenDB 开发支持：连接数据库](https://www.goldendb.com/docsCenter/GoldenDB/61310/html/developmentSupport_databaseConnection.html)
- [GoldenDB 创建和管理数据库对象](https://www.goldendb.com/docsCenter/GoldenDB/61310/html/developmentSupport_createManageDBobjects.html)
- [GoldenDB JDBC 连接](https://www.goldendb.com/docsCenter/Driver_JDBC/100/html/User_Guide_JDBC_Connection.html)
- [GoldenDB JDBC 参数](https://www.goldendb.com/docsCenter/Driver_JDBC/100/html/User_Guide_JDBC_Parameter_Description.html)

## 2. 范围与影响面

后端和插件影响面：

- `backend/settings.gradle`：注册 `ds-goldendb`。
- `backend/clouddm-platform/cgdm-plugin-sdk/.../DataSourceType.java`：新增 `GoldenDBMySQL` 与 `GoldenDBOracle`，主数据库分组，排在
  KingbaseES 与 OceanBase 之间。
- `backend/clouddm-utils/cg-schema/.../DsType.java`：保留 `GoldenDB` 作为 MySQL 模式专有 UMI 属性命名空间；Oracle 模式使用
  `DsType.Oracle`。
- `backend/clouddm-utils/cg-schema/.../adapter/goldendb/`：定义 GoldenDB 分发属性；基础列类型仍绑定
  `MySQLTypes`。
- `backend/clouddm-plugins/clouddm-ds/ds-goldendb/`：新增独立数据源插件、驱动、Session、元信息和 UI SPI。
- `package/pkg/builtin-drivers/built-in-drivers.xml`：仅在取得官方再分发许可且驱动可以稳定解析时登记内置驱动。
- `tests/ds-test/build.gradle` 和现有文本测试框架：增加 GoldenDB 的 MySQL SQL family 映射与夹具，不新增测试类。

前端和文档影响面：

- `frontend/src/views/dataSourceGroup.json`：将 `GoldenDBMySQL` 放入 MySQL family，将 `GoldenDBOracle` 放入 Oracle family。
- `frontend/src/components/function/CustomIcon.vue` 与 `frontend/src/assets/datasource/goldendb.png`：使用用户提供的
  GoldenDB 字标，不复用 MySQL 图标冒充 GoldenDB；只机械裁掉原图白色留边，不重绘颜色或字形。
- `frontend/src/views/login/LoginHero.vue`：如产品希望在登录页展示支持矩阵，则增加 `GoldenDB`。
- `frontend/src/locales/`：仅新增确实由前端展示的 GoldenDB 文案；插件表单文案优先放在插件 i18n 资源中。
- `README.md`、`docs/README.cn.md`、`docs/README.en.md`、`docs/reference/faq.cn.md`、`docs/reference/faq.en.md`
  ：在真实验收通过后更新支持列表。
- `tests/frontend/datasource/goldendb_datasource.md`：维护唯一、可重复执行的浏览器复测流程。

本设计不修改平台业务表，不新增 Flyway migration，也不修改任何历史 migration。首期不支持运维平台管理、数据重分布任务、CDC
管理和多级 `CASE/SUBDISTRIBUTED` 图形化编辑。Oracle 模式首期只声明已验证的 Oracle SQL、基础对象元信息、DML、事务和取消能力；未通过真实矩阵的
Package、DBLink、Job、物化视图和高级 PL/SQL 不标记为完整支持。

## 3. 总体方案

### 3.1 架构边界

```text
ds-goldendb
├── GoldenDBMySQLDsPlugin
│   ├── GoldenDBMySQLConfig / GoldenDB MySQL JDBC Driver
│   ├── GoldenDBMySQLSession / GoldenDBMySQLHooks / GoldenDBMySQLMetaService
│   ├── MySQL SQL family
│   └── GoldenDB MySQL editor（分发策略、分片组、全局索引）
├── GoldenDBOracleDsPlugin
│   ├── GoldenDBOracleConfig / GoldenDB Oracle JDBC Driver
│   ├── GoldenDBOracleSession / GoldenDBOracleHooks / GoldenDBOracleMetaService
│   └── Oracle SQL、Oracle 类型与 Oracle editor family
└── GoldenDBDsFactory
    ├── jdbc:goldendb URL
    └── 实际 sql_mode 与期望兼容模式校验
```

### 3.2 复用审查结论

| 能力                                     | 结论                          | 实施方式与理由                                                                                                                                                                                                                                                                                                       |
|----------------------------------------|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MySQL 模式 SQL parser、拆分、行为/权限分析、rewrite | 直接 family 复用                | `GoldenDBMySQL` 只绑定 `MySQL`，直接使用现有 MySQL 8.0 parser、拆分、行为/权限分析和 rewrite；GoldenDB 专有分布式 DDL 不扩展该 parser。                                                                                                                                                                                                       |
| Oracle 模式 SQL parser、类型与编辑器            | family 复用                   | `GoldenDBOracle` 绑定 `Oracle SQL`、`OracleSqlTypes`、`OracleDialect` 与 Oracle UI SPI；不让 MySQL parser 解析 Oracle 模式 SQL。厂商手册只声明常用 Oracle 语法与 13 种 Oracle 类型，不宣称完整等同某个 Oracle 版本。                                                                                                                                   |
| MySQL 数据类型                             | 直接复用                        | 官方兼容基线是 MySQL 8.0.25；`DsType.GoldenDB` 绑定 `MySQLTypes`，只有真实驱动返回额外类型时才新增 GoldenDB 类型映射。                                                                                                                                                                                                                        |
| `MySqlDialect`                         | 继承复用                        | 新增 `GoldenDBMySQLDialect extends MySqlDialect`，独立关键字资源继承 MySQL 列表并增加 DISTRIBUTED/SUBDISTRIBUTED/DUPLICATE/GLOBAL/REMAIN/HASH/LIST；名称引用和注释转义继续复用。                                                                                                                                                              |
| JDBC 驱动与 URL                           | 不复用                         | 两种模式都使用 `com.goldendb.jdbc.Driver` 与 `jdbc:goldendb://`，但分别加载 5.1.46.86 MySQL JAR 和 5.1.46.77 Oracle JAR；driver family 必须分离，不能把两个同名类 JAR 放进同一类加载器。                                                                                                                                                            |
| 配置与序列化                                 | 共用抽象、类型独立                   | 公共字段收敛到 `AbstractGoldenDBConfig`，对外使用 `GoldenDBMySQLConfig`/`GoldenDBOracleConfig` 及各自 serialization provider，避免保存后丢失模式身份。                                                                                                                                                                                    |
| Session 基础事务行为                         | 继承后验证                       | `USE`、autocommit、隔离级别、commit/rollback、`CONNECTION_ID()` 逐项验证；由 `GoldenDBMySQLHooks` 暴露真实支持级别。                                                                                                                                                                                                                 |
| 取消查询                                   | 首期不支持                       | 历史 JDBC 探针曾通过，但最新 CloudDM 非 TLS 页面无法定位活动 Statement，并最终由 socket 超时结束；两种模式返回 `No`，不显示中断按钮，不保留临时跟踪实现。 |
| MySQL 标准对象元信息                          | 独立门面、逐项复用                   | `MyMetaProviderDm` 直接查询固定的 `INFORMATION_SCHEMA` 表和列。GoldenDB 官方兼容声明不足以证明所有列、结果标签、权限与 MySQL 完全相同，因此由 GoldenDB 子类承接并按探针结果覆盖。                                                                                                                                                                                    |
| Oracle 标准对象元信息                         | Oracle family + GoldenDB 覆盖 | 手册和 Spike 已确认核心 `ALL_*`、`DBA_*`、`V$VERSION`、`SYS_CONTEXT`、`USERENV` 可用；`GLOBAL_NAME`、`PRODUCT_COMPONENT_VERSION` 不存在，因此由 `GoldenDBOracleMetaService` 覆盖当前 catalog、schema 与 parser 参数。                                                                                                                         |
| 表分发信息                                  | 官方元信息命令                     | MySQL 元信息没有 GoldenDB 分片信息。真实 CN 的 `SHOW DISTRIBUTION FROM <table>` 返回 `Dist_type/Dist_key/Groups`，Provider 将其写入类型、表达式、HASH 列和分片组属性，不解析 DDL 文本。                                                                                                                                                                |
| 全局索引                                   | 标准索引回读、隐藏物理表                | `SHOW INDEX` 能回读索引，但没有稳定 GLOBAL/LOCAL 范围列，因此不猜测范围；GoldenDB 生成的 `t<hash>` 物理表按厂商 `TABLE_COMMENT` 标记从业务表列表过滤。                                                                                                                                                                                                   |
| Explain                                | Session 覆盖后复用               | CN 支持 plain `EXPLAIN`。`GoldenDBMySQLSession.createStatement()` 与 Oracle 实现一致，在执行阶段使用 `originalBody` 生成普通 `EXPLAIN`，继续复用 MySQL Engine 的解析和请求链路；`supportExplain` 返回 `Allow`，不在 Hook 改写 SQL，也不暴露 GoldenDB SQL Engine。                                                                                            |
| 分区                                     | 条件复用                        | MySQL `INFORMATION_SCHEMA.PARTITIONS` 可作为基础，但必须与 GoldenDB 水平分发区分，不能把 partition 当成 distribution。                                                                                                                                                                                                               |
| 视图、函数、存储过程、触发器                         | 条件复用                        | 官方声明支持，仍需验证 `ROUTINES/PARAMETERS/TRIGGERS/VIEWS` 列、`SHOW CREATE` 返回列名与低权限行为。                                                                                                                                                                                                                                  |
| ResultSet 列元信息                         | 直接复用 JDBC 结果                | 继承 `MyHooks.getColumnMetaData()` 并信任厂商 JDBC `ResultSetMetaData`。不再通过正则扫描用户查询文本来改写 JSON 表达式的类型。                                                                                                                                                                                                                |
| 表结构编辑器                                 | 基础复用、分发创建扩展                 | 沿用 MySQL 列、主键、普通索引和分区 UI；GoldenDB 扩展只保留 HASH/RANGE/LIST/DUPLICATE 创建字段与官方 `SHOW DISTRIBUTION` 可读的分片组，不展示未验证的全局索引范围。                                                                                                                                                                                           |

### 3.3 为什么元信息必须有 GoldenDB 自己的实现

当前 `MyMetaProviderDm` 对以下契约存在硬依赖：

- `INFORMATION_SCHEMA.SCHEMATA/TABLES/COLUMNS/VIEWS` 的固定列集合。
- `STATISTICS` 与 `TABLE_CONSTRAINTS` 的连接方式及索引类型语义。
- `KEY_COLUMN_USAGE`、`REFERENTIAL_CONSTRAINTS` 的外键字段。
- `PARTITIONS` 的 MySQL 分区字段。
- `ROUTINES`、`PARAMETERS`、`TRIGGERS` 的列名和权限。
- `SHOW CREATE ...` 的结果列标签，例如 `Create Table`、`Create Procedure`、`SQL Original Statement`。

这些查询可以成为 GoldenDB 实现的父类能力，但不能直接把 `MyMetaService` 挂到 GoldenDB 插件。仓库内
TiDB、OceanBase、PolarDB、Doris、StarRocks 等 MySQL 协议产品也都通过自己的 `*MetaProviderDm`、`*MetaService` 和 `*Hooks`
隔离差异；GoldenDB 应保持同一代码风格。

真实探针进一步确认 GoldenDB `INFORMATION_SCHEMA.ROUTINES` 使用 `ROUTINE_CATALOG/ROUTINE_SCHEMA`，而
`PARAMETERS` 使用 `SPECIFIC_CATALOG/SPECIFIC_SCHEMA`。共享 MySQL 元信息查询必须按这两组字段和 `SPECIFIC_NAME`
联合连接；视图详情也必须
按 catalog、schema、table name 联合连接，不能只按对象名关联，否则不同 schema 的同名对象会互相污染。

### 3.4 分阶段实施

1. **P0 真实兼容性探针**：取得 GoldenDB v6.1.03.10、官方 JDBC JAR 和许可；执行连接、Session、ResultSet、所有 MySQL 元信息 SQL、
   `SHOW DISTRIBUTION`、全局索引官方元信息接口调研和低权限矩阵，形成可复跑报告。P0 未通过前不进入打包承诺。
2. **P1 插件骨架与连接生命周期**：新增类型、模块、配置、序列化、官方驱动工厂、Session/Hook、版本映射和支持级别；完成添加、测试连接、查询、事务、超时与关闭。
3. **P2 标准元信息**：实现 GoldenDB 独立元信息门面，继承经 P0 证明可用的 MySQL 查询，覆盖不兼容 SQL/字段/类型；完成
   database、table、view、column、PK/UK/FK、普通 index、partition、routine、trigger 浏览和 DDL 获取。
4. **P3 分布式元信息与表工具**：使用官方 `SHOW DISTRIBUTION FROM <table>` 读取分片组；全局索引和未公开元信息不从 DDL
   推断。首期图形化创建仅覆盖单层 HASH/LIST/RANGE/DUPLICATE。
5. **P4 SQL 审核边界**：标准 SQL 继续使用 MySQL Engine；GoldenDB 专有分布式 DDL 不扩展 MySQL parser，不标记为已支持自动审核。
6. **P5 前端、打包、文档与端到端验收**：接入图标和 family、处理驱动供给策略，完成 Gradle、前端、插件包、IDEA 运行态和 Chrome
   用户流程验证，最后更新公开支持列表。

Lite 环境已完成标准 JDBC/单节点、历史强制 TLS、最小只读权限、负载均衡初始故障及旧单类型 CloudDM 用户流。真实 CN 已补齐
HASH、RANGE、LIST、DUPLICATE、`SHOW DISTRIBUTION`、全局索引和拆分后 MySQL CloudDM 主流程；CN 低权限、
SSH、活动节点重连及 Oracle CloudDM 高级对象仍是门禁项。

## 4. UI 设计

数据源新增页增加 `GoldenDB MySQL` 和 `GoldenDB Oracle`，位于主数据库分组并相邻展示，分别归入 MySQL family 与 Oracle
family。两者沿用 GoldenDB 的 host、port、默认数据库、用户名、密码、超时、SSH 和驱动版本表单，不使用 Oracle
SID/Service Name/TNS 表单，因为 GoldenDB Oracle Connector 仍使用 `jdbc:goldendb://host:port/database`。

GoldenDB Lite 厂商安装模板和当前实例均使用 `5502`，因此 `defaultPort()` 返回 `5502`
作为表单默认值。分布式部署的对外端口可按节点和环境配置，页面仍允许用户覆盖。页面不提供
自定义 JDBC URL 配置，连接 URL 统一由 host、port 和默认数据库组装。
当前首期不展示 SSL 模式，MySQL 驱动固定 `sslMode=DISABLED`，Oracle 驱动固定
`useSSL=false/requireSSL=false/verifyServerCertificate=false`。

表属性页在基础 MySQL 信息之外展示官方 `SHOW DISTRIBUTION` 返回的分发类型、分布键和分片 group。不从 DDL 推断原始子句或全局索引范围。

表结构创建/编辑首期只暴露能够可靠生成和回读的单层策略。复杂多级分片、数据重分布、残留全局索引清理不提供图形化按钮，避免生成看似合法但不可回滚的
DDL。

驱动不可用时分别显示 GoldenDB MySQL/Oracle 官方 JDBC
驱动未安装或版本不匹配，不允许跨模式回退。选错数据源类型时，连接测试返回期望模式与实际模式，不保存错误类型。元信息权限不足时保留已加载层级，并展示具体失败对象类别。

## 5. 核心模型与接口契约

### 5.1 类型与插件契约

- `DataSourceType.GoldenDBMySQL("gdbmy", "GoldenDBMySQL", 0, 79)`。
- `DataSourceType.GoldenDBOracle("gdbora", "GoldenDBOracle", 0, 80)`。
- `DsType.GoldenDB("gdb", "GoldenDB")`。
- 插件模块：`backend/clouddm-plugins/clouddm-ds/ds-goldendb`。
- 插件类：`GoldenDBMySQLDsPlugin` 与 `GoldenDBOracleDsPlugin`。
- SQL 绑定：MySQL 模式只绑定 `MySQL`；Oracle 模式只绑定 `Oracle SQL`。
- 类型绑定：`GoldenDBMySQL` 使用 `DsType.GoldenDB -> MySQLTypes`；`GoldenDBOracle` 使用 `DsType.Oracle -> OracleSqlTypes`。

### 5.2 配置与驱动契约

`AbstractGoldenDBConfig` 继承 `DataSourceConfig`，字段与当前 GoldenDB 表单对齐；`GoldenDBMySQLConfig` 与
`GoldenDBOracleConfig` 分别设置数据源类型和序列化 provider。`asDriverProperties()` 增加内部期望模式属性，Factory
校验后移除，不传给厂商驱动。

`GoldenDBDsFactory`：

- 使用官方 `com.goldendb.jdbc.Driver` 创建连接。
- 普通 URL 为 `jdbc:goldendb://host:port/database`。
- URL 只由 host、port 和默认数据库生成，不接受配置中的自定义 JDBC URL。
- 连接建立后设置 auto-commit，不在连接创建时修改服务器全局参数。
- 日志包含 instanceId 和已脱敏 URL，不记录用户名密码、证书密码或完整 Properties。

驱动 descriptor 使用两个独立 family：`GoldenDB MySQL JDBC Driver / 5.1.46.86` 与
`GoldenDB Oracle JDBC Driver / 5.1.46.77`。版本号以实际 JAR manifest 和厂商发布信息为准。若无公开 Maven
坐标，则使用用户上传/私有制品库；只有确认许可后才写入 `built-in-drivers.xml`。

当前验证制品来自 `ZXCLOUD-GoldenDB-Client-DriverV1.0.01P2.zip`：MySQL 模式使用 Java Connector V2.1P5 的
`gdb_mysql-connector-java-5.1.46.86.jar`，Oracle 模式使用 Java Oracle Connector V1.3P1 的
`gdb_mysql-connector-java-oracle-5.1.46.77.jar`。两个制品保持用户提供模式，未确认再分发许可前不进入内置驱动包。

### 5.3 元信息契约

新增类：

- `GoldenDBMySQLHooks extends MyHooks`
- `GoldenDBMySQLMetaService extends MyMetaService`
- `GoldenDBMySQLUmiServiceDm extends MyUmiServiceDm`
- `GoldenDBMySQLMetaProvider extends MyMetaProviderDm`

MySQL 标准映射直接使用 `MyMetaProviderDm` 已初始化的 `MyMetaProviderUtils`，不保留无行为的 GoldenDB 空子类。Oracle
外键虽然读取 GoldenDB 可用的 `INFORMATION_SCHEMA`，但在 `GoldenDBOracleMetaProvider` 内按 Oracle 数据源契约映射，
不再借用 MySQL Provider Utils。`GoldenDBOracleUmiServiceDm` 在 GoldenDB 模块内直接管理该 Provider 和 Oracle 对象分发，
不修改 `OraUmiServiceDm` 公共父类。

新增 GoldenDB UMI 属性：

- `DISTRIBUTION_TYPE`
- `DISTRIBUTION_EXPRESSION`
- `DISTRIBUTION_COLUMNS`
- `DISTRIBUTION_GROUPS`

标准属性保持 MySQL 语义。分发信息和 partition 分开存储；运行时元信息根据 `SHOW DISTRIBUTION` 的真实列写入
`DISTRIBUTION_TYPE`、`DISTRIBUTION_EXPRESSION`、HASH 的 `DISTRIBUTION_COLUMNS` 和 `DISTRIBUTION_GROUPS`。

`GoldenDBMySQLMetaService.getSqlParserParameters()` 固定 MySQL grammar 兼容基线为 8.0.25，并单独保存 GoldenDB 精确版本。不能把
`SELECT VERSION()` 返回的 GoldenDB 产品版本直接交给 `MySqlVersion.parse()` 后默默回退到 latest。

### 5.4 表工具契约

参考 Cloudberry 的 `AttributeNames -> MetaProvider -> UiData -> EditorProvider/CreateUtils` 分层，新建 GoldenDB 对应类；标准
MySQL 列、约束、普通索引和 partition 逻辑通过继承复用。创建 DDL 的最后阶段由 `GoldenDBMySQLCreateUtils` 根据结构化属性追加
`DISTRIBUTED BY`，不在 Vue 层拼 SQL。

复杂分发子句无法结构化解析时，编辑器进入只读模式并保留原始 DDL，不能用空默认值覆盖原有分发策略。

### 5.5 建议文件结构

```text
backend/clouddm-plugins/clouddm-ds/ds-goldendb/
├── build.gradle
├── src/main/java/com/clougence/clouddm/ds/goldendb/
│   ├── GoldenDBMySQLDsPlugin.java
│   ├── GoldenDBOracleDsPlugin.java
│   ├── dsconf/
│   │   ├── AbstractGoldenDBConfig.java
│   │   ├── GoldenDBCompatibilityMode.java
│   │   ├── mysql/GoldenDBMySQLConfig.java
│   │   └── oracle/GoldenDBOracleConfig.java
│   ├── dialect/mysql/GoldenDBMySQLDialect.java
│   ├── definition/
│   │   ├── mysql/ui/browser/GoldenDBMySQLDsBrowseSpi.java
│   │   └── oracle/ui/browser/GoldenDBOracleDsBrowseSpi.java
│   ├── execute/
│   │   ├── GoldenDBSessionFactory.java
│   │   ├── dsfactory/GoldenDBDsFactory.java
│   │   ├── mysql/
│   │   │   ├── GoldenDBMySQLSession.java
│   │   │   ├── GoldenDBMySQLHooks.java
│   │   │   ├── GoldenDBMySQLSupportSpi.java
│   │   │   ├── GoldenDBMySQLMetaService.java
│   │   │   ├── GoldenDBMySQLUmiServiceDm.java
│   │   │   └── GoldenDBMySQLMetaProvider.java
│   │   └── oracle/
│   │       ├── GoldenDBOracleSession.java
│   │       ├── GoldenDBOracleHooks.java
│   │       ├── GoldenDBOracleSupportSpi.java
│   │       ├── GoldenDBOracleMetaService.java
│   │       ├── GoldenDBOracleUmiServiceDm.java
│   │       └── GoldenDBOracleMetaProvider.java
│   ├── definition/mysql/
│   │   ├── GoldenDBMySQLDefService.java
│   │   └── ui/editor/table/
│   │       ├── GoldenDBMySQLTableEditorFields.java
│   │       ├── GoldenDBMySQLTableEditorUiDataSpi.java
│   │       ├── GoldenDBMySQLEditorProvider.java
│   │       └── GoldenDBMySQLCreateUtils.java
│   ├── i18n/GoldenDBDsI18nKeys.java
│   └── resource/mysql/GoldenDBMySQLEditorResourceSpi.java
└── src/main/resources/META-INF/
    ├── clougence/drivers.xml
    ├── clougence/db-keywords/goldendb.keywords
    ├── clougence/i18n/goldendb-*.properties
    └── services/com.clougence.drivers.DsFactory
```

两个 GoldenDB 插件入口沿用 KingbaseES 的兼容模式分型方式。生产类和枚举使用独立源文件，不增加内部类，不为测试扩大 public
API。只有探针证明需要差异时才覆盖 family 父类方法。

## 6. 关键流程

### 6.1 新增与测试连接

1. Console 根据 `GoldenDBMySQLConfigSpi` 或 `GoldenDBOracleConfigSpi` 生成表单并保存固定数据源类型。
2. Driver Loader 根据类型只解析对应的 GoldenDB MySQL/Oracle driver family，隔离加载同名入口类的官方 JAR。
3. Factory 建立新的物理连接，读取 `@@SESSION.sql_mode` 并校验实际模式与数据源类型一致。
4. Session 初始化验证当前 database、autocommit、read-only、隔离级别和 query id。
5. 任一步失败都关闭物理连接并返回短而明确的错误；不注册半初始化数据源。

### 6.2 元信息浏览

1. MySQL 模式由 `GoldenDBMySQLUmiServiceDm` 以 database 作为 Schema 层，使用已验证的 `INFORMATION_SCHEMA` 查询，并通过官方
   `SHOW DISTRIBUTION FROM <table>` 追加类型、分布键和分片组。
   全局索引物理表按 `TABLE_COMMENT LIKE 'Global Index Table Name = %'` 在 GoldenDB 表列表 SQL 中排除。
2. Oracle 模式由 `GoldenDBOracleMetaService` 使用 `ALL_*`/`DBA_*` 数据字典；当前 catalog 查询 `DATABASE()`，当前 schema 查询
   `SYS_CONTEXT`，产品版本从 `V$VERSION` 获取。
3. 两种模式分别批量加载列、约束、索引和分区，不允许把 MySQL `SHOW CREATE` 或 Oracle `DBMS_METADATA` 交叉复用。
4. 单个对象类别无权限或系统视图缺失时仅降级对应叶子类型，并记录具体 SQLState/错误码。

### 6.3 SQL 查询与事务

1. MySQL 模式直接使用 MySQL SQL Engine 及 GoldenDB parser 版本参数。
2. Oracle 模式使用 Oracle SQL Engine；基于手册已证明的 `FETCH FIRST` 能力采用 Oracle 12 parser 基线，但不据此宣称完整
   Oracle 12 兼容。
3. MySQL/Oracle Session 分别使用对应 Hook 和结果类型映射。
4. INSERT、UPDATE、DELETE 在 auto-commit 开关两种模式下都必须验证；非自动提交状态正确维护 commit/rollback。
5. MySQL Explain 继续使用 MySQL Engine 分析，执行阶段由 GoldenDB MySQL Session 基于原始 SQL 生成普通 `EXPLAIN`。
6. MySQL 模式只暴露 READ COMMITTED；自动/手动事务切换可用。
7. SSL 和查询中断不在首期支持范围；重新开放前必须补齐真实 CloudDM 产品矩阵。

### 6.4 DDL 与审核

- 普通 MySQL 兼容 DDL 使用现有 MySQL Engine 和 GoldenDB builder。
- Oracle 兼容 DDL 使用 Oracle Engine 和 Oracle builder；未通过真实矩阵的 Package、DBLink、Job、物化视图和高级 PL/SQL
  不进入支持声明。
- `DISTRIBUTED BY HASH/RANGE/LIST/DUPLICATE`、全局索引修饰、`DROP REMAIN INDEX` 与 `UPDATE GLOBAL INDEXES`
  不在 MySQL parser 支持范围内，不标记为已支持自动 SQL 分析。

## 7. 兼容、迁移与降级

新增枚举和插件不需要业务表迁移，现有 MySQL、TiDB、Cloudberry、KingbaseES 配置与行为不变。本分支早期的单一 `GoldenDB`
类型保留为不绑定插件的 deprecated 兼容墓碑，避免历史行导致枚举映射和列表查询失败；它不出现在新增入口，也不能建立新连接。旧草稿配置需按真实模式重新创建为
`GoldenDBMySQL` 或 `GoldenDBOracle`。两个新类型使用独立序列化 provider，未来升级字段时通过忽略未知字段保持向前读取。

驱动策略分两档：

- **用户提供驱动**：默认安全方案。插件可发布，管理员安装厂商 JAR 后启用 GoldenDB。
- **内置驱动**：只有厂商许可允许再分发、JAR 可在构建环境稳定获取、NOTICE/许可证完成审核、family/version/resource 三处完全一致时启用。

不得将 MySQL Connector/J 作为官方 GoldenDB 驱动缺失时的 fallback，也不得在 MySQL/Oracle 两个 GoldenDB driver family
之间回退。官方驱动或测试实例缺失时，UI 可以展示数据源类型，但运行验收状态必须是 blocked/unverified。

首批只承诺 v6.1.03.10。更早版本通过相同兼容矩阵后再扩展 descriptor 和版本映射。回滚时可移除插件包和前端入口；已有 GoldenDB
配置保留但不可连接，不删除用户配置或驱动文件。

## 8. 验收标准与验证方式

### 8.1 P0 探针门禁

在真实 GoldenDB MySQL 与 Oracle 模式连接上分别记录每项 SQL、结果列、SQLState、错误码和耗时：

- `SELECT VERSION(), @@SESSION.sql_mode`、`SELECT DATABASE()`、`SELECT CONNECTION_ID()`。
- autocommit、read-only、服务端实际支持的隔离级别、commit/rollback。
- `SCHEMATA`、`TABLES`、`COLUMNS`、`VIEWS`、`STATISTICS`、`TABLE_CONSTRAINTS`、`KEY_COLUMN_USAGE`、`REFERENTIAL_CONSTRAINTS`、
  `PARTITIONS`、`ROUTINES`、`PARAMETERS`、`TRIGGERS`。
- `SHOW CREATE DATABASE/TABLE/VIEW/FUNCTION/PROCEDURE/TRIGGER` 的列标签和完整 DDL。
- 普通表、HASH/RANGE/LIST/DUPLICATE 表、partition + distribution 表的 `SHOW DISTRIBUTION` 结果，以及普通/唯一索引的标准元信息。
- 官方驱动 `ResultSetMetaData` 的 catalog/schema/table/alias/type/precision。
- 低权限用户、SSH、连接断开和 CN 重连；SSL 和查询中断按首期不支持验证页面无入口。

探针结果必须明确标注每项为“直接继承 MySQL”“GoldenDB override”“首期不支持”，不能只记录连接成功。

### 8.2 构建与静态验证

- `cd backend && ./gradlew :ds-goldendb:build`
- `cd backend && ./gradlew :s-test:test`
- 在现有 `SqlTestSupport` 和文本夹具中验证 GoldenDB 标准 SQL 使用 MySQL Engine；不新增测试类。
- 检查插件 fat JAR 中的 SPI、`drivers.xml`、MySQL family 和 SQL engine 依赖。
- 检查 `driverFamily/version/resource` 在插件 descriptor、内置驱动清单和实际文件索引中一致。
- 前端改动执行 `cd frontend && npm run lint`、`npm run check-i18n` 和相关现有单测。
- 从仓库根目录执行 `cd package && ./all_build.sh web`；成功后按项目规则使用 Pika Control 精确重启当前 IDEA 后端并确认新实例为
  `RUNNING`。
- 插件打包执行 `cd package && ./all_build.sh plugin ds-goldendb` 或等价的实际模块命令；成功后同样重启 IDEA 后端。

### 8.3 真实功能验收

| 类别  | 必须通过的行为                                                                                                                 |
|-----|-------------------------------------------------------------------------------------------------------------------------|
| 连接  | 新增、编辑、测试、删除；普通 URL、自定义 URL、错误密码、不可达、超时、驱动缺失。                                                                            |
| 元信息 | database、table、view、column、PK/UK/FK、普通/唯一 index、partition、distribution groups、routine、trigger 可浏览且详情真实；全局索引范围等待官方元信息接口。 |
| DML | 有意义的 INSERT、UPDATE、DELETE；prepared 参数；大结果流式读取；自动提交与手动事务。                                                                |
| DDL | 创建/修改/删除基础对象；HASH/RANGE/LIST/DUPLICATE 建表并回读；标准 SQL 和专有 DDL 的审核支持边界与文档一致。                                               |
| 会话  | 切换 database、隔离级别、commit、rollback、超时、取消、取消后终态、断线恢复和资源释放。                                                                 |
| 权限  | 管理员和最小权限账号分别验证；局部元信息无权限不会拖垮整个数据源。                                                                                       |
| 打包  | 源码 fat JAR、运行插件目录、tgz/Docker 中插件与驱动策略一致；不携带无许可 JAR。                                                                     |
| UI  | 数据源类型、官方图标、动态表单、数据源树、对象详情、SQL 工作台均通过 Chrome 真实交互，Console error 为 0。                                                     |

浏览器复测前读取并更新 `tests/frontend/datasource/goldendb_datasource.md`，使用真实后端和 GoldenDB 实例完成从新增数据源到
DDL/DML/元信息回读的完整链路。HTTP 200、插件加载成功、服务健康或 SQL 能启动都不能单独作为 GoldenDB 支持完成的证据。
