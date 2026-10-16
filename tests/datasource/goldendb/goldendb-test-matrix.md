# GoldenDB 双兼容模式测试矩阵

## 1. 当前结论

**PARTIAL**。

`GoldenDBMySQL` 与 `GoldenDBOracle` 已完成独立类型、驱动族、SQL family、配置、Session 和元信息实现，
但还不能把“双模式完整支持”标记为 PASS：

- MySQL 旧探针 TLS 前为 46 PASS、2 UNSUPPORTED、0 FAIL；本轮加入元信息非空断言后的最终探针为
  39 PASS、1 UNSUPPORTED、9 FAIL：7 项是 Routine/Parameter/Trigger 元信息为 0 行，2 项是 TLS 下查询取消。
  TLS、CA、HASH/RANGE/LIST/DUPLICATE、`SHOW DISTRIBUTION` 和全局索引的 JDBC 历史路径仍通过；当前 CloudDM
  不再声明 GoldenDB SSL 和查询中断能力。
- 2026-08-25 已清空旧运行数据并按 MySQL 模板重新执行原厂 15 步安装；安装后检查 7/7 PASS。当前 CN/DN
  为全新 16K page、`max_table_record_size=64K` 的 MySQL 数据空间，Oracle 字典表、Oracle 专项 schema、旧组件目录和
  Oracle 切换备份均为 0；CloudDM 新 Session 与完整 MySQL 探针再次通过。
- Oracle 模式 TLS 前探针为 34 PASS、0 FAIL；TLS 后探针为 34 PASS、1 FAIL，唯一失败是查询取消。它覆盖基础对象、
  Procedure/Function/Trigger、Package、物化视图、Scheduler Job、匿名 PL/SQL、事务、ResultSetMetaData 和官方元信息清理；专项报告见
  `goldendb-oracle-test-matrix.md`。
- Oracle 已完成对象树分类、手动 commit/rollback 和 plain EXPLAIN 的历史 UI 验收；但 2026-08-26 从查询控制台直接执行
  Procedure/Function/Trigger、匿名块、Package 和 Scheduler DDL 时被 Oracle SQL Engine 标记为“语句无法解析”，不能把 JDBC
  探针通过继续写成查询控制台通过。
- MySQL 的 RANGE/LIST/DUPLICATE 已由表编辑器真实创建，并通过 `SHOW DISTRIBUTION` 回读；三张表随后清理为 0。
- 2026-08-26 查询控制台复测还发现：MySQL Routine/Parameter/Trigger 的 `INFORMATION_SCHEMA` 查询对已创建对象返回
  0 行；GoldenDB 分布式 DDL/全局索引未通过 MySQL SQL Engine；Oracle `INFORMATION_SCHEMA` 数值元信息在结果页出现
  `Unsupported ... type`。这些页面链路均按 FAIL/PARTIAL 记录。
- 2026-08-26 Chrome 逐库/逐分类复测发现并修复：MySQL 过滤 5 个无法切换的 database，Oracle 过滤 2 个；
  Oracle 用户/角色/DBLink 不再调用不兼容的继承 SQL，物化视图及内部临时表不再泄漏到普通表分类。
- 2026-08-27 两台 ECS 重启后公网 IP 对应角色发生变化；CloudDM 已将 MySQL 更新为 `47.110.46.26:8880`、
  Oracle 更新为 `121.196.147.203:8880`，两个已保存数据源均在列表页重新测试连接通过。
- 2026-08-27 能力边界收口：MySQL 对象分类只保留表/视图，Oracle 只保留表/视图/物化视图/序列；
  用户、角色、DBLink 等空实现已删除。两种模式均关闭 SSL 和查询中断能力，失败不再伪装为空结果或可用能力。
- MySQL 执行计划参考 Oracle Session 实现：页面显示“执行计划”，执行阶段基于原始 SQL 生成普通 `EXPLAIN`。
- `package/package.sh --build` 已成功，alone/console/sidecar 三包均携带相同 SHA 的 GoldenDB 插件且不含厂商 JDBC JAR。
- 当前仍为 PARTIAL：除多 CN 负载均衡、故障切换和 CloudDM SSH 表单外，还存在上述查询控制台 SQL Engine 与结果类型问题。

矩阵更新时间：2026-08-27 CST。

## 2. 状态定义

| Status        | 含义                                      |
|---------------|-----------------------------------------|
| `PASS`        | 已在当前或明确记录的真实环境执行，并满足预期。                 |
| `PARTIAL`     | 已执行部分关键路径，但不足以覆盖该能力的完整契约。               |
| `FAIL`        | 已执行且结果不满足当前能力契约。                        |
| `UNSUPPORTED` | 已执行，当前测试环境明确不提供该能力；不能外推到其他 GoldenDB 部署。 |
| `NOT RUN`     | 本轮没有执行，不能从构建、相邻能力或历史结果推断。               |
| `BLOCKED`     | 缺少所需 CN、多节点、证书、SSH 或隔离账号等环境。            |

## 3. 代码、环境与制品

| Item                   | Value                                                                                                                | Status |
|------------------------|----------------------------------------------------------------------------------------------------------------------|--------|
| Repository             | `/Users/pika/IdeaProjects/clouddm/open-cdm`                                                                          | PASS   |
| Branch                 | `feat/support_goldendb`                                                                                              | PASS   |
| HEAD baseline          | `f2a91bc32d90`，工作区包含未提交 GoldenDB 变更                                                                                  | PASS   |
| Server                 | ECS 上干净重装 GoldenDB RHV6.1.03.12SP5；原厂安装 15/15、验收 7/7，`goldendb-all.service` active/enabled                           | PASS   |
| Topology               | Manager/MDS、ZK、GTM、DN、CN；CN `47.110.46.26:8880`，当前一个 group，不声明生产 HA                                                  | PASS   |
| Compatibility mode     | MySQL；`parse_mode=1`、`oracle_mode_switch=ALL_OFF`                                                                    | PASS   |
| MySQL TLS              | 服务端历史 TLS1.3/1.2 探针保留；当前 CloudDM GoldenDB 数据源不暴露 SSL 配置                                                | UNSUPPORTED |
| Runtime                | IDEA `DmAloneLauncher` executionId `169`，DEBUG/RUNNING；`/healthcheck=ok`                                             | PASS   |
| Plugin source artifact | `backend/clouddm-plugins/clouddm-ds/ds-goldendb/build/libs/ds-goldendb-lib.jar`                                      | PASS   |
| Runtime plugin         | `/Users/pika/clouddm/plugins/ds-goldendb-lib.jar`                                                                    | PASS   |
| Plugin SHA-256         | 源码与运行目录均为 `9af7424cd38dab7ab54b9d28e0f743da3eb8984705c00c20ab413d2358a4ff34`                                         | PASS   |
| Plugin load            | JVM 已加载两个插件；`GoldenDBMySQL` Provider、Session、Support 和表编辑器均由保存的数据源触发                                                 | PASS   |
| MySQL JDBC             | `GoldenDB MySQL JDBC Driver / 5.1.46.86`，SHA-256 `7866aad6ce083ff231e0a9869e17fe0cae667657b3bc0e359666887d3daf5211`  | PASS   |
| Oracle JDBC            | `GoldenDB Oracle JDBC Driver / 5.1.46.77`，SHA-256 `d082aa95fd31b2bd5e80584d187c3b3c1c4736443b12a6282d471d4b3006ecc2` | PASS   |

两个厂商 JAR 都暴露 `com.goldendb.jdbc.Driver`，因此必须位于不同 driver family/version 目录，并由不同 DriverBinding
类加载器加载。
两套目录均已生成 `files.idx`，Chrome 表单显示驱动就绪。

## 4. 构建与静态验证

| Check                                         | Actual                                                                                         | Status  |
|-----------------------------------------------|------------------------------------------------------------------------------------------------|---------|
| `:ds-goldendb:compileJava`                    | 初次发现旧 `DataSourceType.GoldenDB` 引用后修复；最终编译成功                                                   | PASS    |
| `:ds-goldendb:build`                          | 分发元信息、全局索引物理表过滤、READ COMMITTED 和能力声明收口后 BUILD SUCCESSFUL                                       | PASS    |
| `:ds-goldendb:local`                          | customFatJar 与运行目录部署成功                                                                         | PASS    |
| 类边界                                           | 无模式后缀仅保留 CompatibilityMode、SessionFactory、DsFactory、公共 i18n；旧 MySQL 类名未进入 JAR | PASS    |
| 临时取消实现                                        | `GoldenDBStatementTracker` 已删除；两种模式明确声明不支持查询中断                                      | PASS    |
| `:ds-goldendb:customFatJar`                   | 包含两个 Plugin、两个 Config/Session、Oracle MetaProvider、双 driver descriptor                          | PASS    |
| `:s-test:compileTestJava`                     | BUILD SUCCESSFUL                                                                               | PASS    |
| `:s-test:test --tests ...SecDomainTextTest`   | 315 total，315 passed，0 failed，0 skipped                                                        | PASS    |
| `:cgdm-plugin-sdk:test`                       | BUILD SUCCESSFUL                                                                               | PASS    |
| `npm run lint`                                | No lint errors                                                                                 | PASS    |
| `npm run check-i18n`                          | 没有需要检查的文件                                                                                      | PASS    |
| `tests/datasource/goldendb/prepare_driver.sh` | 两个 JAR 的文件名、Driver 类和 SHA-256 均验证并安装                                                           | PASS    |
| `package/all_build.sh plugin ds-goldendb`     | custom fat JAR 构建成功                                                                            | PASS    |
| `package/all_build.sh`                        | 本轮完整 clean build 和本地发布成功，366 个构建任务执行完成                                                         | PASS    |
| 源码/运行插件一致性                                    | 当前源码与运行插件 SHA-256 均为 `9af7424c...a4ff34`                                                      | PASS    |
| `package/package.sh --build`                  | TLS 属性映射修复前 382 个任务成功；本轮未重跑完整安装包                                                               | PARTIAL |
| alone/console/sidecar tgz 内容                  | 布局与驱动排除已验收；包内插件为 TLS 修复前 SHA                                                                   | PARTIAL |

说明：`all_build.sh` 和 `package.sh --build` 都跳过测试，不能替代单独的 `:s-test:test`。

## 5. 模式总矩阵

| Type             | Mode/driver        | Engine       | MODE | META    | QUERY   | DML     | TX   | DDL     | CANCEL | FAILURE | UI      | PACKAGE | Result      |
|------------------|--------------------|--------------|------|---------|---------|---------|------|---------|--------|---------|---------|---------|-------------|
| `GoldenDBMySQL`  | mysql / 5.1.46.86  | `MySQL`      | PASS | PARTIAL | PASS    | PARTIAL | PASS | PARTIAL | UNSUPPORTED | PASS    | PARTIAL | PASS    | **PARTIAL** |
| `GoldenDBOracle` | oracle / 5.1.46.77 | `Oracle SQL` | PASS | PARTIAL | PARTIAL | PASS    | PASS | PARTIAL | UNSUPPORTED | PASS    | PARTIAL | PASS    | **PARTIAL** |

两个模式的基础连接、标准查询和已暴露对象已通过；总结果仍为 `PARTIAL`，原因包括控制台专有/PLSQL 语法、
部分元信息结果类型，以及多 CN 故障切换和 CloudDM 内建 SSH 门禁。SSL、查询中断及被隐藏的对象类型不计入当前支持范围。

## 6. GoldenDB MySQL 证据

### 6.1 历史 JDBC 辅助记录（已不可复跑）

删除仓库内直连探针前的最后一份严格报告：
`/Users/pika/docker_opt/localdb/goldendb/artifacts/open-cdm/goldendb-compatibility-20260826-post-ui-harness-170621.md`。

`20260825-clean-mysql-rerun` 是 TLS 前基线，`20260826-tls-mysql-verified` 是旧 TLS 记录，
`20260826-post-ui-harness-170621` 增加元信息非空断言；三份结果仅作为历史数据库/驱动证据，不再提供仓库内复跑入口。

- Driver：`gdb_mysql-connector-java-5.1.46.86`；
- 实例模式：`parse_mode=1`、`oracle_mode_switch=ALL_OFF`、`STRICT_TRANS_TABLES`；
- 当前严格结果：`PASS=39`、`UNSUPPORTED=1`、`FAIL=9`。

| Category              | Executed evidence                                             | Status      |
|-----------------------|---------------------------------------------------------------|-------------|
| Driver/connection     | Driver load、普通 URL、loadbalance URL 格式                         | PASS        |
| TLS                   | CN/DN/内部链路同 CA；默认 TLSv1.3、强制 TLS1.2、CA 校验通过                   | PASS        |
| Session               | VERSION/DATABASE/CONNECTION_ID/sql_mode；服务端只提供 READ COMMITTED | PASS        |
| Standard DDL          | database、table、view、procedure、function、trigger、partition      | PASS        |
| DML                   | INSERT、UPDATE、DELETE，最终 delete 回读为 0                          | PASS        |
| Transaction           | rollback 后 0 行、commit 后 1 行并清理                                | PASS        |
| Raw metadata          | 表/列/视图/索引/约束/外键/分区非空；Routine/Parameter/Trigger 对已创建对象为 0 行    | **PARTIAL** |
| CloudDM provider SQL  | 表/列/视图/索引/主唯一键/外键非空；过程/函数/参数/触发器查询为 0 行                       | **PARTIAL** |
| Plain `EXPLAIN`       | MySQL Session 基于原始 SQL生成普通 `EXPLAIN`；CloudDM 页面计划通过           | PASS        |
| ResultSetMetaData     | catalog/schema/table/label/type/precision                     | PASS        |
| Least privilege       | 临时只读账号可读元信息且 DML 被拒绝，账号已回收                                    | PASS        |
| Schema collision      | View 保持当前 schema 1 行；Procedure 因当前 schema 元信息为 0 行而失败         | **FAIL**    |
| Cancel/recovery       | TLS 前约 611ms PASS；TLS 后 5 秒超时，Proxy 接近查询自然结束才路由 kill          | **FAIL**    |
| Cancel in transaction | TLS 前 PASS；TLS 后 5 秒超时                                        | **FAIL**    |
| Distributed table     | HASH、RANGE、LIST、DUPLICATE 在 CN 建表并回读成功                        | PASS        |
| `SHOW DISTRIBUTION`   | 返回 `Table/Dist_type/Dist_key/Groups`，HASH 为 `id/g1`           | PASS        |
| Global index          | HASH 表创建全局索引并由 `SHOW INDEX` 回读成功                              | PASS        |
| `KILL QUERY`          | 长查询未被服务端 KILL 中断                                              | UNSUPPORTED |

### 6.2 拆分后的当前状态

- `GoldenDBMySQL` 类型、配置、序列化、Session、driver family 和 `MySQL` SQL Engine 绑定已进入最终运行插件：PASS。
- 用 `GoldenDBMySQL` 连接当前 Oracle 模式实例，Factory 明确返回
  `GoldenDB compatibility mode mismatch, expected: mysql, actual: oracle`：PASS。
- 旧 TLS 报告为 46 PASS、1 UNSUPPORTED、2 FAIL；该版本探针没有校验元信息非空。本轮收紧非空断言后，
  Routine/Parameter/Trigger 及其 Provider 查询暴露 0 行问题，旧 PASS 数量不能覆盖本轮结论。
- 新 `GoldenDBMySQL` 表单、双类型入口、驱动 5.1.46.86 和默认端口已在最新插件运行态复核：PASS。
- CloudDM 对象树、四种分发属性、全局索引、SQL/DML、READ COMMITTED 和表编辑器：PASS；当前表单不再暴露 SSL。
- 最新非 TLS 页面复测仍无法定位活动查询并最终 socket 超时，因此当前明确不支持查询中断。
- MySQL 对象分类只展示表和视图；Procedure/Function/Trigger 在找到官方可枚举元信息前不对外展示。
- GoldenDB 的全局索引物理表带 `Global Index Table Name = ...` 注释，Provider 已过滤，不在业务表对象树显示。
- CloudDM “执行计划”启用：`GoldenDBMySQLSession` 参考 Oracle Session，在执行阶段用 `originalBody` 生成普通
  `EXPLAIN`；不在 Hook 改写 SQL，也不恢复独立 GoldenDB SQL Engine。
- 干净重装后：CN Session 为 `parse_mode=1`，集群模板为 `xa_protocol=1`；DN 为
  `oracle_mode_switch=ALL_OFF`、`STRICT_TRANS_TABLES`、`innodb_page_size=16384`、`max_table_record_size=64K`，
  CN 为 `oracle_mode_switch=0`、`dictionary_info=0`。`_gdb_sysdb._gdb_dictionary_systb_info`、
  `codex_gdb_ora_%` schema、旧组件暂存目录和 Oracle 切换备份均不存在；RDB 的 Oracle 启用基线计数为 0，
  当前只保留通用 `cluster_compare.json`、`mysql_*`、`c1_*`、`lds_template` 和 `sys_gtm_template`。
- `codex_gdb_clean_mysql` 在 CN 上回读 2 行，`SHOW DISTRIBUTION` 为 `HASH/id/g1`，全局索引由 `SHOW INDEX` 回读；
  CloudDM 保存的数据源对象树可见新 schema，SQL 页面回读 2 行。

因此旧单类型的 MySQL E2E 只能作为兼容基线，不能直接把新类型整行标记为 PASS。

## 7. GoldenDB Oracle 证据

专项矩阵：[`goldendb-oracle-test-matrix.md`](goldendb-oracle-test-matrix.md)。

最终自动化报告：
`/Users/pika/docker_opt/localdb/goldendb/artifacts/open-cdm/goldendb-oracle-compatibility-20260826-unified-oracle-complete-164148.md`，
结果 `PASS=34`、`FAIL=1`；唯一失败为 TLS 下查询取消。TLS 前基线仍为 `PASS=34`、`FAIL=0`。

### 7.1 实例与连接

| Check                    | Actual                                                                   | Status |
|--------------------------|--------------------------------------------------------------------------|--------|
| Oracle driver connection | 5.1.46.77 JDBC 建连成功                                                      | PASS   |
| Server mode              | `ORA_COMPATIBLE_MODE`、`EMPTY_STRING_IS_NULL` 可见                          | PASS   |
| Driver isolation         | 实际读取 `gdb_mysql-connector-java-oracle-5.1.46.77`                         | PASS   |
| Wrong type               | MySQL 类型连接同一实例被模式校验拒绝                                                    | PASS   |
| Basic query              | `SELECT 1 FROM DUAL` 返回 1                                                | PASS   |
| Identity                 | `SYS_CONTEXT('USERENV','CURRENT_SCHEMA')`、`USERENV('SID')`               | PASS   |
| Catalog/version          | `DATABASE()`、`SYS_CONTEXT`、`VERSION()`                                   | PASS   |
| Missing Oracle views     | `GLOBAL_NAME`、`PRODUCT_COMPONENT_VERSION` 不存在，已由 GoldenDB MetaService 覆盖 | PASS   |
| Failure matrix           | 错密码、端口、不可达、2 秒超时、错模式和缺失 Driver 类均按预期失败                                   | PASS   |
| TLS production config    | CN/DN/内部链路证书配置完成；CloudDM TRUST 默认 `TLSv1.3 / TLS_AES_256_GCM_SHA384`     | PASS   |
| SSH transport            | SSH 转发到 `192.168.2.1:8880` 后 Oracle Connector 建连并识别 Oracle mode          | PASS   |

### 7.2 元信息

| Check                          | Actual                                                                           | Status   |
|--------------------------------|----------------------------------------------------------------------------------|----------|
| Dictionary availability        | `ALL_USERS`、`ALL_TABLES`、`ALL_OBJECTS`、`ALL_TAB_COLUMNS` 可查                      | PASS     |
| Common dictionary dependencies | `INFORMATION_SCHEMA`、`ALL_CONSTRAINTS/INDEXES`、`SYS.DBA_TAB_COLS`、sequence systb | PASS     |
| GoldenDB provider version      | `SELECT VERSION()`                                                               | PASS     |
| Schema list                    | 当前业务 schema 可查                                                                   | PASS     |
| Table list                     | 非空父子表 2 个；全局索引物理表被过滤                                                             | PASS     |
| Column detail                  | Provider/JDBC 10 列通过；查询控制台读取 unsigned 数值元信息时显示 Unsupported                       | PARTIAL  |
| Index detail                   | Provider/JDBC 5 个索引列通过；查询控制台的数值列显示 Unsupported                                   | PARTIAL  |
| PK/UQ detail                   | 3 个主键/唯一键列记录                                                                     | PASS     |
| Foreign-key detail             | 1 个引用映射；使用 KEY_COLUMN_USAGE/REFERENTIAL_CONSTRAINTS                              | PASS     |
| View/sequence                  | 2 个对象                                                                            | PASS     |
| ResultSetMetaData              | catalog/table/column/label/type/precision 回读                                     | PASS     |
| Procedure/function/trigger     | 直连 JDBC 探针可创建并从字典回读；CloudDM 查询控制台 DDL 无法解析，当前不在对象分类展示                         | UNSUPPORTED |
| Package/advanced PL/SQL        | 直连 JDBC 探针可执行；CloudDM 查询控制台 DDL 无法解析                                             | **FAIL** |
| Materialized view              | `_GDB_SYSDB.DBA_MVIEWS` 列表/详情和 CloudDM 分类通过                                      | PASS     |
| Scheduler Job                  | 直连 JDBC 探针可创建并从系统视图回读；CloudDM 查询控制台 DDL 无法解析，当前不在对象分类展示                       | UNSUPPORTED |
| DBLink                         | 当前没有可用官方枚举接口，不在对象分类展示；历史假路由结果不作为产品支持                                      | UNSUPPORTED |

直连探针只证明数据库与驱动具备相应能力，不能替代 CloudDM 产品验收。executionId 169 的最终运行插件已复验
view/sequence/materialized-view 元信息、物化视图与普通表隔离，以及 4 个已暴露对象分类；
Procedure/Function/Trigger、Package 和 Scheduler 仍受 Oracle SQL Engine 解析限制，当前不进入对象分类。

### 7.3 会话与历史取消证据

| Check                       | Actual                                                          | Status   |
|-----------------------------|-----------------------------------------------------------------|----------|
| Long query                  | `SELECT SLEEP(30) FROM DUAL`                                    | PASS     |
| Oracle DDL                  | 父子表、索引、视图、序列创建成功                                                | PASS     |
| Prepared DML                | INSERT、UPDATE、视图回读、DELETE 清理                                    | PASS     |
| JDBC transaction            | rollback 后 0 行，commit 后 1 行                                     | PASS     |
| JDBC cancel（历史）           | TLS 前约 625ms PASS；TLS 后 cancel 返回但查询 10 秒未结束，服务端约 30 秒后才路由 kill | **FAIL** |
| Same-connection recovery（历史） | TLS 前 PASS；TLS 后因取消未及时终止                                      | **FAIL** |
| Probe cleanup               | 临时数据与对象残留均为 0                                                   | PASS     |
| CloudDM UI cancel（历史）     | TLS 前曾通过；最新非 TLS 页面无法定位活动查询，当前按钮已隐藏                         | UNSUPPORTED |
| Manual transaction rollback | UI rollback 后 0 行、commit 后 1 行、清理后 0                            | PASS     |
| CloudDM explain             | plain `EXPLAIN` 返回 SQLNode/SIMPLE 两行计划                          | PASS     |

## 8. Chrome UI 矩阵

| Check                             | Actual                                                                   | Status       |
|-----------------------------------|--------------------------------------------------------------------------|--------------|
| Type entry                        | 新增弹窗只有 `GoldenDB MySQL`、`GoldenDB Oracle`；旧 `GoldenDB` 不在入口              | PASS         |
| Legacy row                        | deprecated `GoldenDB` 历史行可列出，不再触发 MyBatis enum 映射错误                      | PASS         |
| Logo                              | 两个新类型使用用户提供的 GoldenDB 蓝橙字标                                               | PASS         |
| MySQL form                        | `GoldenDB MySQL JDBC Driver / 5.1.46.86`，驱动就绪                            | PASS         |
| Oracle form                       | `GoldenDB Oracle JDBC Driver / 5.1.46.77`，驱动就绪                           | PASS         |
| Default port                      | MySQL/Oracle 新增表单均默认 `5502`，输入框仍可编辑                                      | PASS         |
| MySQL parser engine               | 新增页和现有编辑页均只有 `MySQL`，旧配置值读取时归一为 `MySQL`                                  | PASS         |
| Oracle parser engine              | 只有 `Oracle SQL`，不显示 MySQL 或 GoldenDB 自建 Engine                           | PASS         |
| Oracle charset i18n               | 显示“连接字符集”和 `characterEncoding` 说明，不再显示 i18n key                          | PASS         |
| MySQL advanced config             | 高级配置只保留连接、Socket、字符集和查询超时，无自定义 JDBC URL                                  | PASS         |
| Oracle advanced config            | 高级配置只保留连接、Socket、字符集和查询超时，无自定义 JDBC URL                                  | PASS         |
| Desktop                           | 两卡均为 148.5×44px，可见且无重叠                                                   | PASS         |
| Tablet                            | 两卡均为 140.5×44px，可见且无重叠                                                   | PASS         |
| Mobile                            | 两卡均为 259×44px，单列显示，无溢出                                                   | PASS         |
| Datasource list alignment         | 48px 统一图标槽，图标中心与名称起始线一致                                                  | PASS         |
| Refresh/re-entry                  | 后端重启后刷新，双入口仍存在，旧行仍可读取                                                    | PASS         |
| Console                           | 业务库查询无新增 console error；对象树 API 错误通过 toast 展示                             | PARTIAL      |
| MySQL test connection/save        | ECS CN 测试连接、保存、列表回读和重启后恢复                                                | PASS         |
| MySQL SSL                         | 配置页不再显示 SSL 模式；驱动属性固定 `sslMode=DISABLED`                                      | UNSUPPORTED  |
| MySQL database tree               | 5 个不可切换系统库已过滤；剩余 7 个 database 逐个打开无错误                                    | PASS         |
| MySQL metadata                    | 对象分类只显示表/视图；过程/函数/触发器不再以假空列表暴露；分发元信息读取失败会显式报错                         | PARTIAL      |
| MySQL SQL/DML                     | SELECT、INSERT、UPDATE、DELETE 影响行数与回读正确                                    | PASS         |
| MySQL transaction                 | 只显示 READ COMMITTED；rollback=0、commit=1、清理后=0                             | PASS         |
| MySQL table editor                | HASH/RANGE/LIST/DUPLICATE 均由 UI 预览和执行，`SHOW DISTRIBUTION` 回读 type/key/g1 | PASS         |
| MySQL cancel/recovery             | 最新非 TLS 页面仍无法定位活动查询并最终 socket 超时；按钮已关闭，临时 StatementTracker 已删除             | UNSUPPORTED  |
| MySQL explain                     | Session 生成 plain `EXPLAIN`，页面显示执行计划按钮并返回结构化计划                            | PASS         |
| Oracle test/save/workbench        | 测试连接、保存、列表、表对象树/详情、SQL 查询 2 行及 NUMBER/VARCHAR2 显示正确                      | PASS         |
| Oracle SSL                        | 配置页不再显示 SSL 模式；驱动属性固定 `useSSL=false/requireSSL=false`                       | UNSUPPORTED  |
| Oracle transaction/cancel/explain | commit/rollback、plain EXPLAIN PASS；查询中断不在当前支持范围                            | PARTIAL      |
| Oracle database tree              | 两个不可切换系统库已过滤；`_gdb_sysdb`、业务库、`sys` 逐个打开无错误                              | PASS         |
| Oracle object categories          | 只展示表/视图/物化视图/序列；其余未完成分类不再暴露                                               | PASS         |
| Oracle table filtering            | 物化视图及 `*_gdb_tmp_mview` 已从普通表分类过滤                                        | PASS         |
| Oracle advanced objects           | JDBC 探针通过；查询控制台仅物化视图通过，PL/SQL/Package/Scheduler DDL 无法解析                 | PARTIAL      |
| Final packages                    | 三个 tar.gz 插件 SHA 一致，厂商 JDBC JAR 为 0                                      | PASS         |
| Cleanup                           | MySQL 编辑器三表及 Oracle 高级对象、任务、字典、mview 残留均为 0；保留基础复测库和数据源                  | PASS         |

UI 截图在本次 Chrome 复验中已检查，但没有作为仓库文件持久化；本矩阵不引用不存在的截图路径。

## 9. 失败、纠偏与边界

| Finding                                                   | Resolution                                                                           | Final status              |
|-----------------------------------------------------------|--------------------------------------------------------------------------------------|---------------------------|
| 旧编辑器资源仍引用单一 `DataSourceType.GoldenDB`，首次模块编译失败            | 改为 `GoldenDBMySQL` 资源类型                                                              | RESOLVED                  |
| Oracle 公共 MetaProvider 查询不存在的 `PRODUCT_COMPONENT_VERSION` | 新增 GoldenDB Oracle MetaProvider，从 `V$VERSION` 读取版本                                   | RESOLVED                  |
| Oracle `ALL_TAB_COMMENTS` 缺失且 `ALL_OBJECTS` 不登记业务对象       | 改用 INFORMATION_SCHEMA 与 sequence systb                                               | RESOLVED，JDBC/CHROME PASS |
| Oracle 外键查询固定使用 `CONSTRAINT_TYPE='R'`，GoldenDB 实际为 `F`    | GoldenDB 覆盖使用 KEY_COLUMN_USAGE/REFERENTIAL_CONSTRAINTS                               | RESOLVED，探针 PASS          |
| Oracle 测试连接使用不支持的 `ALTER SESSION SET CURRENT_SCHEMA`      | GoldenDB Hook 使用 `Connection.setCatalog()`                                           | RESOLVED，CHROME PASS      |
| Oracle 索引类型 `PRIMARY/MULTIPLE` 导致公共枚举 NPE                 | GoldenDB Provider 归一为 `NORMAL`                                                       | RESOLVED，CHROME PASS      |
| Oracle 查询 NUMBER/VARCHAR2 显示 Unsupported                  | GoldenDB ColReader 显式映射厂商物理类型，不依赖 MySQL Reader                                       | RESOLVED，CHROME PASS      |
| Oracle `SYS.ALL_MVIEWS` 不存在，物化视图 UI 空列表                   | 改用 `_GDB_SYSDB.DBA_MVIEWS` 列表/详情                                                     | RESOLVED，JDBC/CHROME PASS |
| GoldenDB SSL 暂未达到产品支持边界                                  | 两种模式配置页不再展示 SSL；驱动属性明确禁用 SSL，历史握手结果仅作数据库证据                                  | UNSUPPORTED               |
| DBProxy TLS 状态回显错误                                        | 底层 SSLSession 为 TLSv1.3/1.2，`SHOW STATUS` 固定回显 TLSv1/旧 cipher                        | DOCUMENTED                |
| 查询取消失效                                                   | 最新非 TLS 页面仍无法定位活动 Statement；关闭两种模式中断能力并删除临时 StatementTracker                          | UNSUPPORTED                |
| MySQL Routine 元信息查询返回 0 行                                 | MySQL 专属 Browse SPI 暂不展示 Procedure/Function/Trigger，底层请求明确 Unsupported              | UNSUPPORTED                |
| MySQL 控制台不解析 GoldenDB 分布式 DDL                             | JDBC 与表编辑器路径通过；查询控制台 MySQL Engine 未将专有语法送到 CN                                        | OPEN，FAIL                 |
| Oracle 控制台不解析高级 PL/SQL DDL                                | JDBC 探针通过；查询控制台直接执行 Procedure/Function/Trigger/匿名块仍提示无法解析                            | OPEN，FAIL                 |
| Oracle 元信息查询结果出现 Unsupported type                         | 业务 NUMBER/VARCHAR2 已修复，但 `INFORMATION_SCHEMA` 的 unsigned 数值列仍未覆盖                     | OPEN，FAIL                 |
| GoldenDB 系统库被当成普通可切换库                                     | 两个模式按真实 `USE/setCatalog` 能力过滤，剩余库 Chrome 逐个通过                                        | RESOLVED，CHROME PASS      |
| Oracle 用户/角色/DBLink 分类报错                                  | 删除空集合/`null` 假实现；Oracle 专属 Browse SPI 不再展示，底层请求明确 Unsupported                    | UNSUPPORTED                |
| Oracle 物化视图泄漏到表分类                                         | 通过 `DBA_MVIEWS` 名单和 `*_gdb_tmp_mview` 后缀过滤普通表                                        | RESOLVED，CHROME PASS      |
| 删除旧枚举后历史行导致整个列表 `No enum constant ...GoldenDB`            | 保留不绑定插件、不出现在新增入口的 deprecated 兼容墓碑                                                    | RESOLVED                  |
| 两个厂商 JAR 入口类同名                                            | 拆分两个 driver family 和版本目录                                                             | RESOLVED                  |
| 从 `SHOW CREATE TABLE` 手写解析分发和全局索引                         | 删除 DDL Parser；直接读取 `SHOW DISTRIBUTION` 的 type/key/groups                             | RESOLVED，CN JDBC PASS     |
| 通过正则扫描查询文本修改 JSON 表达式结果类型                                 | 删除文本猜测，统一信任厂商 JDBC `ResultSetMetaData`                                               | RESOLVED                  |
| Lite 不支持分布式 HASH/DUPLICATE/global index                   | 已在真实 CN 验证 HASH、DUPLICATE 和 global index                                             | RESOLVED                  |
| 全局索引生成 `t<hash>` 物理表并进入业务对象树                              | GoldenDB Provider 按厂商 `TABLE_COMMENT` 标记在 SQL 层过滤                                    | RESOLVED，CHROME PASS      |
| MySQL Engine 生成 `EXPLAIN FORMAT=TRADITIONAL`，CN 拒绝        | 参考 Oracle Session，在执行阶段基于 `originalBody` 生成普通 `EXPLAIN`                              | RESOLVED，CN/CHROME PASS   |
| 手动事务内 cancel 正常提前结束但不抛 SQLException                       | 非 TLS 基线接受两种厂商终态；TLS 下出现新的延迟取消问题                                                     | BASELINE PASS，TLS FAIL    |
| 旧 MySQL 数据空间曾执行 Oracle 字典初始化，无法物理回退到 64K                  | 删除旧组件数据并按纯 MySQL 模板重新安装；新 DN 为 16K page / 64K record                                 | RESOLVED，旧数据与切换备份已永久删除    |
| 干净重装后首次矩阵的自动事务 cancel 恢复项偶发超时                             | 立即完整复跑，`Statement.cancel()` 611ms 中断，最终 46 PASS、2 UNSUPPORTED、0 FAIL                 | TRANSIENT，RERUN PASS      |
| Oracle 高级对象与 PL/SQL 未验收                                   | JDBC 自动探针通过；查询控制台仅物化视图通过，其余 PL/SQL DDL 仍未通过                                          | PARTIAL                   |

## 10. 完整 PASS 前必须补测

| Environment gate         | Current status | Reason                                               |
|--------------------------|----------------|------------------------------------------------------|
| GoldenDB CN 分布式 DDL/全局索引 | PASS           | 四种单层分发、全局索引 JDBC 和 HASH/RANGE/LIST/DUPLICATE 编辑器均通过。 |
| MySQL SSL                | UNSUPPORTED    | 当前表单和驱动属性均关闭 SSL；历史 TLS1.3/1.2/CA 结果仅作数据库证据。 |
| Oracle SSL               | UNSUPPORTED    | 当前表单和驱动属性均关闭 SSL；历史 TLS1.3/1.2/CA 结果仅作数据库证据。 |
| 多地址/活动节点切换               | BLOCKED        | 当前只有一个 `dbproxy`/CN 监听和一个 group。                     |
| SSH 隧道                   | PARTIAL        | 外部 SSH 转发 + JDBC PASS；CloudDM 表单内 SSH 未跑。            |
| Oracle 最小权限矩阵            | BLOCKED        | 未准备可安全创建/回收的独立 Oracle 模式低权限账号。                       |

### P1

1. 准备至少两个 CN，验证坏端点、负载均衡、活动节点切换和事务恢复。
2. 在 CloudDM 表单内复跑 SSH 隧道和最小权限账号的局部元信息降级。
3. 如需重新开放 SSL、查询中断或隐藏对象类型，先补齐对应产品路径实现和矩阵，再恢复能力声明。

## 11. 可复跑命令

```bash
cd backend
./gradlew :ds-goldendb:build
./gradlew :s-test:test --tests com.clougence.clouddm.ds.secdomain.SecDomainTextTest
./gradlew :cgdm-plugin-sdk:test

cd ../frontend
npm run lint
npm run check-i18n

cd ../package
./all_build.sh

cd ..
tests/datasource/goldendb/prepare_driver.sh \
  /Users/pika/docker_opt/localdb/goldendb/ZXCLOUD-GoldenDB-Client-DriverV1.0.01P2.zip
```

数据库连接和 SQL 能力从 CloudDM 数据源表单、查询控制台及前端复测流程执行；仓库不再维护绕过 CloudDM 的直连 JDBC 探针。

## 12. 数据源整体复测 SQL

本章只收录在 CloudDM 产品 `数据查询` 控制台实际执行并满足预期的 SQL。数据库客户端直连结果和仅能解析但未进入执行日志的语句均不进入本章。

本轮页面证据：

- MySQL：数据源 `codex-goldendb-cn-e2e`，Database `codex_goldendb_validation`，执行时间 `2026-08-26 16:50:48`～`16:53:29`；
- Oracle：数据源 `codex-goldendb-oracle-e2e`，Database `goldendb_oracle_demo`，执行时间 `2026-08-26 16:55:26`～`16:57:22`；
- 页面 SELECT 会按产品限制自动追加 `LIMIT 1000` 或 `FETCH FIRST 1000 ROWS ONLY`，下列内容记录用户在编辑器输入的原始 SQL；
- 对象名带 `260826` 后缀；复测前应确认同名对象不存在，或换成新的唯一后缀。

### 12.1 GoldenDB MySQL：页面通过 SQL（29 条）

#### 会话与 TLS

```sql
SELECT VERSION(), DATABASE(), CONNECTION_ID(), @@SESSION.sql_mode;

SHOW SESSION STATUS WHERE Variable_name IN ('Ssl_cipher','Ssl_version');
```

页面结果：版本 `8.9.99`，Database 为 `codex_goldendb_validation`，`sql_mode=STRICT_TRANS_TABLES`；SSL 状态返回非空 cipher。

#### 表、外键、视图和例程对象

```sql
CREATE TABLE codex_gdb_ui_m_260826 (id BIGINT NOT NULL, code VARCHAR(64) NOT NULL, amount DECIMAL(18,2), updated_at DATETIME, payload JSON, PRIMARY KEY(id), UNIQUE KEY uk_ui_m_code(code), KEY idx_ui_m_updated(updated_at)) ENGINE=InnoDB;

ALTER TABLE codex_gdb_ui_m_260826 ADD COLUMN note VARCHAR(128) NULL;

CREATE TABLE codex_gdb_ui_m_child_260826 (id BIGINT NOT NULL, parent_id BIGINT NOT NULL, detail VARCHAR(128), PRIMARY KEY(id), CONSTRAINT fk_ui_m_parent FOREIGN KEY(parent_id) REFERENCES codex_gdb_ui_m_260826(id)) ENGINE=InnoDB;

CREATE VIEW codex_gdb_ui_m_view_260826 AS SELECT id, code, amount FROM codex_gdb_ui_m_260826;

CREATE PROCEDURE codex_gdb_ui_m_proc_260826(IN probe_id BIGINT) SELECT COUNT(*) AS row_count FROM codex_gdb_ui_m_260826 WHERE id=probe_id;

CREATE FUNCTION codex_gdb_ui_m_fn_260826(probe_value BIGINT) RETURNS BIGINT DETERMINISTIC RETURN probe_value + 1;

CREATE TRIGGER codex_gdb_ui_m_trg_260826 BEFORE INSERT ON codex_gdb_ui_m_260826 FOR EACH ROW SET NEW.updated_at = COALESCE(NEW.updated_at, NOW());
```

页面执行信息均为 `0 rows affected`。

#### DML、对象调用和 MySQL 查询语法

```sql
SHOW CREATE TABLE codex_gdb_ui_m_260826;

SHOW INDEX FROM codex_gdb_ui_m_260826;

INSERT INTO codex_gdb_ui_m_260826(id,code,amount,updated_at,payload,note) VALUES (26082601,'ui-mysql-main',10.25,NOW(),JSON_OBJECT('kind','ui'),'initial');

INSERT INTO codex_gdb_ui_m_child_260826(id,parent_id,detail) VALUES (26082611,26082601,'ui-child');

UPDATE codex_gdb_ui_m_260826 SET amount=31.50,note='updated' WHERE id=26082601;

SELECT id,code,amount,updated_at,payload,note FROM codex_gdb_ui_m_260826 WHERE id=26082601;

CALL codex_gdb_ui_m_proc_260826(26082601);

SELECT codex_gdb_ui_m_fn_260826(41) AS function_value;

INSERT INTO codex_gdb_ui_m_260826(id,code,amount,updated_at,note) VALUES (26082602,'ui-mysql-trigger',2.00,NULL,'trigger');

SELECT id,updated_at IS NOT NULL AS trigger_fired FROM codex_gdb_ui_m_260826 WHERE id=26082602;

SELECT id,code,amount FROM codex_gdb_ui_m_view_260826 ORDER BY id;

WITH ranked AS (SELECT id,code,IFNULL(note,'none') AS safe_note,ROW_NUMBER() OVER (ORDER BY id) AS rn FROM `codex_gdb_ui_m_260826`) SELECT id,code,safe_note,rn FROM ranked ORDER BY id LIMIT 10;
```

页面回读：主表为 `amount=31.50`；Procedure 返回 `row_count=1`；Function 返回 `42`；Trigger 回读 `trigger_fired=1`；View 和
CTE/窗口函数均返回 2 行。

#### 表、列、约束、外键、例程定义和分区元信息

```sql
SELECT TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,COLUMN_DEFAULT,IS_NULLABLE,DATA_TYPE,COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME IN ('codex_gdb_ui_m_260826','codex_gdb_ui_m_child_260826') ORDER BY TABLE_NAME,ORDINAL_POSITION;

SELECT CONSTRAINT_SCHEMA,TABLE_SCHEMA,TABLE_NAME,CONSTRAINT_NAME,CONSTRAINT_TYPE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME IN ('codex_gdb_ui_m_260826','codex_gdb_ui_m_child_260826') ORDER BY TABLE_NAME,CONSTRAINT_NAME;

SELECT C.CONSTRAINT_SCHEMA,C.CONSTRAINT_NAME,C.TABLE_SCHEMA,C.TABLE_NAME,C.COLUMN_NAME,C.REFERENCED_TABLE_SCHEMA,R.REFERENCED_TABLE_NAME,C.REFERENCED_COLUMN_NAME,R.UPDATE_RULE,R.DELETE_RULE FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE C LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS T ON C.CONSTRAINT_CATALOG=T.CONSTRAINT_CATALOG AND C.CONSTRAINT_SCHEMA=T.CONSTRAINT_SCHEMA AND C.CONSTRAINT_NAME=T.CONSTRAINT_NAME AND C.TABLE_SCHEMA=T.TABLE_SCHEMA AND C.TABLE_NAME=T.TABLE_NAME LEFT JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R ON C.CONSTRAINT_CATALOG=R.CONSTRAINT_CATALOG AND C.CONSTRAINT_SCHEMA=R.CONSTRAINT_SCHEMA AND C.CONSTRAINT_NAME=R.CONSTRAINT_NAME AND C.TABLE_SCHEMA=T.TABLE_SCHEMA AND C.TABLE_NAME=R.TABLE_NAME WHERE T.TABLE_SCHEMA=DATABASE() AND T.TABLE_NAME='codex_gdb_ui_m_child_260826' AND T.CONSTRAINT_TYPE='FOREIGN KEY' ORDER BY C.POSITION_IN_UNIQUE_CONSTRAINT;

SHOW CREATE PROCEDURE codex_gdb_ui_m_proc_260826;

SHOW CREATE FUNCTION codex_gdb_ui_m_fn_260826;

SHOW CREATE TRIGGER codex_gdb_ui_m_trg_260826;

CREATE TABLE codex_gdb_ui_m_part_260826 (id BIGINT NOT NULL, PRIMARY KEY(id)) ENGINE=InnoDB PARTITION BY RANGE(id) (PARTITION p0 VALUES LESS THAN (1000), PARTITION pmax VALUES LESS THAN MAXVALUE);

SELECT TABLE_SCHEMA,TABLE_NAME,PARTITION_NAME,PARTITION_METHOD,PARTITION_EXPRESSION FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='codex_gdb_ui_m_part_260826' AND PARTITION_NAME IS NOT NULL ORDER BY PARTITION_ORDINAL_POSITION;
```

页面回读：列 9 行、约束 4 行、外键 1 行，三个 `SHOW CREATE` 各 1 行，分区元信息为 `p0/pmax` 2 行。

### 12.2 GoldenDB Oracle：页面通过 SQL（19 条）

#### 身份和 Oracle 表达式

```sql
SELECT 1 FROM DUAL;

SELECT DATABASE(), SYS_CONTEXT('USERENV','CURRENT_SCHEMA'), USERENV('SID') FROM DUAL;

SELECT SYSDATE,NVL(NULL,7),CASE WHEN '' IS NULL THEN 1 ELSE 0 END,'A'||'B',TO_CHAR(TO_DATE('2026-08-26','YYYY-MM-DD'),'YYYY-MM-DD') FROM DUAL;
```

页面结果：当前 Database/Schema 为 `goldendb_oracle_demo`；`SYSDATE`、`NVL`、空字符串为 NULL、字符串连接及日期转换均按 Oracle
语义返回。

#### 表、外键、索引、视图和序列

```sql
CREATE TABLE codex_gdb_ui_o_260826_p (id NUMBER(18) NOT NULL, code VARCHAR2(64) DEFAULT 'PENDING' NOT NULL, amount NUMBER(18,2), created_at DATE, updated_at TIMESTAMP, payload CLOB, raw_data BLOB, CONSTRAINT codex_gdb_ui_o_260826_pk PRIMARY KEY(id), CONSTRAINT codex_gdb_ui_o_260826_uq UNIQUE(code));

CREATE TABLE codex_gdb_ui_o_260826_c (id NUMBER(18) NOT NULL, parent_id NUMBER(18) NOT NULL, detail VARCHAR2(128), CONSTRAINT codex_gdb_ui_o_260826_cpk PRIMARY KEY(id), CONSTRAINT codex_gdb_ui_o_260826_fk FOREIGN KEY(parent_id) REFERENCES codex_gdb_ui_o_260826_p(id) ON DELETE CASCADE);

CREATE INDEX codex_gdb_ui_o_260826_i ON codex_gdb_ui_o_260826_p(updated_at);

CREATE VIEW codex_gdb_ui_o_260826_v AS SELECT id,code,amount FROM codex_gdb_ui_o_260826_p;

CREATE SEQUENCE codex_gdb_ui_o_260826_s START WITH 100 INCREMENT BY 1;
```

页面执行信息均为 `0 rows affected`。

#### DML、结果类型和序列调用

```sql
INSERT INTO codex_gdb_ui_o_260826_p(id,code,amount,created_at,updated_at,payload) VALUES (26082601,'ui-oracle-main',10.25,TO_DATE('2026-08-26','YYYY-MM-DD'),TO_TIMESTAMP('2026-08-26 16:00:00','YYYY-MM-DD HH24:MI:SS'),'oracle-clob');

INSERT INTO codex_gdb_ui_o_260826_c(id,parent_id,detail) VALUES (26082611,26082601,'ui-child');

UPDATE codex_gdb_ui_o_260826_p SET amount=31.50 WHERE id=26082601;

SELECT id,code,amount,created_at,updated_at,payload FROM codex_gdb_ui_o_260826_p WHERE id=26082601;

SELECT id,code,amount FROM codex_gdb_ui_o_260826_v WHERE id=26082601;

SELECT codex_gdb_ui_o_260826_s.NEXTVAL FROM DUAL;
```

页面回读：主表为 `amount=31.5`，DATE/TIMESTAMP/CLOB 正常显示；View 返回 1 行；Sequence `NEXTVAL=100`。

#### 表、约束、外键和物化视图元信息

```sql
SELECT TABLE_SCHEMA,TABLE_NAME,TABLE_TYPE,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME IN ('codex_gdb_ui_o_260826_p','codex_gdb_ui_o_260826_c') ORDER BY TABLE_NAME;

SELECT CONSTRAINT_SCHEMA,TABLE_SCHEMA,TABLE_NAME,CONSTRAINT_NAME,CONSTRAINT_TYPE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME IN ('codex_gdb_ui_o_260826_p','codex_gdb_ui_o_260826_c') ORDER BY TABLE_NAME,CONSTRAINT_NAME;

SELECT CONSTRAINT_SCHEMA,CONSTRAINT_NAME,TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,REFERENCED_TABLE_SCHEMA,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='codex_gdb_ui_o_260826_c' AND REFERENCED_TABLE_NAME IS NOT NULL;

CREATE MATERIALIZED VIEW codex_gdb_ui_o_260826_mv AS SELECT id,code FROM codex_gdb_ui_o_260826_p;

SELECT OWNER,MVIEW_NAME,QUERY,COMPILE_STATE FROM _GDB_SYSDB.DBA_MVIEWS WHERE UPPER(OWNER)=UPPER(DATABASE()) AND UPPER(MVIEW_NAME)='CODEX_GDB_UI_O_260826_MV';
```

页面回读：表 2 行、约束 4 行、外键 1 行；物化视图元信息为 1 行且 `COMPILE_STATE=VALID`。

### 12.3 页面未通过能力（不把对应 SQL 放入通过清单）

| Mode         | Capability                                                    | Page result                              | Matrix status |
|--------------|---------------------------------------------------------------|------------------------------------------|---------------|
| MySQL        | Routine/parameter/trigger 的 `INFORMATION_SCHEMA` 元信息          | 对刚创建对象均返回 0 行；`SHOW CREATE` 可回读          | FAIL          |
| MySQL        | `ON DUPLICATE KEY UPDATE`                                     | GoldenDB 返回 `must be 'SW'`               | UNSUPPORTED   |
| MySQL        | GoldenDB 分布式 DDL、`SHOW DISTRIBUTION`、全局索引                     | MySQL SQL Engine 无法解析，未进入服务端执行日志         | FAIL          |
| Oracle       | MySQL 风格 Session/TLS 状态语法                                     | Oracle SQL Engine 提示“语句无法解析”，未执行         | UNSUPPORTED   |
| Oracle       | Procedure/Function/Trigger、匿名块、Package、Scheduler 的 PL/SQL DDL | Oracle SQL Engine 提示“语句无法解析”，未执行         | FAIL          |
| Oracle       | 列/索引的 `INFORMATION_SCHEMA` 数值元信息                              | SQL 执行成功，但结果单元格出现 `Unsupported ... type` | FAIL          |
| Oracle       | Sequence systb 明细 SQL                                         | 未进入页面执行日志；仅 Sequence `NEXTVAL` 页面通过      | FAIL          |
| Oracle       | `V$VERSION`                                                   | 当前账号访问视图失败                               | BLOCKED       |
| MySQL/Oracle | TLS 下查询中断                                                     | 页面 SQL 不满足成功终态；JDBC 探针同样超时               | FAIL          |

历史直连报告只保留数据库/驱动层证据，不能把其中的 PASS 回填成本章的 CloudDM 控制台 PASS。
