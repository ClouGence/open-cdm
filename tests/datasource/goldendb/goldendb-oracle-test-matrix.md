# GoldenDB Oracle 模式测试矩阵报告

## 1. 当前结论

**PARTIAL**。

本轮已在独立 GoldenDB RHV6.1.03.12SP5 Oracle 兼容模式分布式 CN 上完成：

- TLS 前自动化探针 `PASS=34`、`FAIL=0`；TLS 后最终探针 `PASS=34`、`FAIL=1`，唯一失败为查询取消；
- CloudDM UI 完成连接、查询、手动事务 commit/rollback 和执行计划；当前产品不暴露 SSL 和查询中断；
- 当前 Chrome 复测已验证 TABLE、VIEW、SEQUENCE、MATERIALIZED VIEW 分类和业务表/视图详情；
- 2026-08-27 ECS 重启后 Oracle 公网 CN 更新为 `121.196.147.203:8880`，CloudDM 保存配置和列表页测试连接均已通过；
  Procedure/Function/Trigger 无法通过查询控制台创建，不能继续沿用历史预建对象结论；
- 错密码、错误端口、不可达地址、2 秒连接超时、模式不匹配和缺失 Driver 类均走生产 `GoldenDBDsFactory` 验证；
- `package/package.sh --build` 成功，alone/console/sidecar 三包插件 SHA 与源码一致，厂商 JDBC JAR 未进入安装包；
- SSH 端口转发到同 VPC CN 后，官方 Oracle Connector 建连并确认 `ORA_COMPATIBLE_MODE`。

不能标记完整 PASS 的原因：

- CN、DN 及 CN→DN 的历史 TLS 探针保留，但当前 CloudDM 不声明 SSL；
- 历史 TLS 和最新非 TLS 页面中断均未达到产品契约，当前能力已关闭并删除临时 StatementTracker；
- 查询控制台可执行表/索引/视图/序列/物化视图及 DML，但 Procedure/Function/Trigger、匿名块、Package、Scheduler
  DDL 被 Oracle SQL Engine 标记为“语句无法解析”；JDBC 探针通过不能替代页面 PASS；
- 查询控制台读取 `INFORMATION_SCHEMA` 列/索引的 unsigned 数值元信息时出现 `Unsupported ... type`；
- 数据源树已过滤无法 `setCatalog()` 的 `information_schema` 和 `performance_schema`；当前只展示表、视图、物化视图和序列；
- 物化视图和 `*_gdb_tmp_mview` 内部表已从普通表分类过滤；
- DBLink 语法已进入远端路由阶段，但未将真实密码写入 DBLink，因此工作链路仍为 **BLOCKED**；
- 当前部署只有一个 `dbproxy`/CN 监听和一个 group，无法构造多 CN 负载均衡、活动节点切换及故障转移；
- CloudDM 表单内 SSH 私钥链路未执行；本轮只验证了真实 SSH 传输与 JDBC 链路。

矩阵更新时间：2026-08-27 CST。

## 2. 状态定义

| Status        | 含义                             |
|---------------|--------------------------------|
| `PASS`        | 已在本报告指定的真实环境直接执行并满足预期。         |
| `PARTIAL`     | 已覆盖主要链路，但仍有已知失败或环境门禁。          |
| `FAIL`        | 已执行且结果不满足当前能力或安全契约。            |
| `UNSUPPORTED` | 当前数据库环境明确不提供该能力。               |
| `BLOCKED`     | 缺少拓扑、证书或不应自动持久化的凭据，无法安全构造完整场景。 |
| `NOT RUN`     | 没有执行，不能从相邻测试推断。                |

## 3. 环境与制品

| Item                   | Value                                                                    | Status  |
|------------------------|--------------------------------------------------------------------------|---------|
| Repository             | `/Users/pika/IdeaProjects/clouddm/open-cdm`                              | PASS    |
| Branch / HEAD          | `feat/support_goldendb` / `f2a91bc32d90`，工作区包含未提交 GoldenDB 变更            | PASS    |
| Server                 | GoldenDB RHV6.1.03.12SP5，Oracle 兼容模式                                     | PASS    |
| CN                     | `121.196.147.203:8880`；同 VPC `192.168.2.1:8880`                          | PASS    |
| Topology               | 单 `dbproxy`/CN 监听、单 group `g1`                                           | PARTIAL |
| Schema                 | `goldendb_oracle_demo`                                                   | PASS    |
| Driver                 | `gdb_mysql-connector-java-oracle-5.1.46.77`                              | PASS    |
| Driver SHA-256         | `d082aa95fd31b2bd5e80584d187c3b3c1c4736443b12a6282d471d4b3006ecc2`       | PASS    |
| TLS backup             | `/root/goldendb-oracle/backups/tls-20260826T113806`                      | PASS    |
| SSL                    | 服务端历史 TLS1.3/1.2 探针保留；当前 CloudDM GoldenDB Oracle 不暴露 SSL 配置            | UNSUPPORTED |
| Source plugin SHA-256  | `9af7424cd38dab7ab54b9d28e0f743da3eb8984705c00c20ab413d2358a4ff34`       | PASS    |
| Runtime plugin SHA-256 | `9af7424cd38dab7ab54b9d28e0f743da3eb8984705c00c20ab413d2358a4ff34`       | PASS    |
| Runtime                | IDEA `DmAloneLauncher` executionId `169`，DEBUG/RUNNING；`/healthcheck=ok` | PASS    |

最终自动化报告：

-
`/Users/pika/docker_opt/localdb/goldendb/artifacts/open-cdm/goldendb-oracle-compatibility-20260826-unified-oracle-complete-164148.md`；
-
`/Users/pika/docker_opt/localdb/goldendb/artifacts/open-cdm/goldendb-oracle-compatibility-20260826-unified-oracle-complete-164148.json`。

## 4. 能力总矩阵

| Category                   | Direct JDBC / server                      | CloudDM Provider / runtime            | Result              |
|----------------------------|-------------------------------------------|---------------------------------------|---------------------|
| Driver/connection/mode     | 正确连接、Oracle mode、错误模式拒绝                   | 保存数据源和重启回读通过                          | PASS                |
| Basic query and types      | NUMBER/VARCHAR2/DATE/TIMESTAMP/CLOB/BLOB  | 显式 Oracle 物理类型 Reader，结果页通过           | PASS                |
| DDL/DML                    | JDBC 覆盖全部对象；页面覆盖表、索引、视图、序列、物化视图和 DML      | 高级 PL/SQL DDL 页面无法解析                  | PARTIAL             |
| Transaction                | JDBC rollback/commit                      | UI rollback=0、commit=1、清理=0           | PASS                |
| Explain                    | plain `EXPLAIN` 返回结构化计划                   | UI “执行计划”返回 2 行                       | PASS                |
| Cancel/recovery            | 历史 TLS 前探针通过，但 TLS 和最新非 TLS 页面均失败        | 当前关闭查询中断能力                         | UNSUPPORTED         |
| Table/view/sequence        | GoldenDB 官方视图/系统表                         | 基础列表通过；物化视图与普通表分类隔离                   | PASS                |
| Procedure/function/trigger | dictionary systb 有数据库直连能力                 | 当前不在对象分类中展示；查询控制台 DDL 仍无法解析          | UNSUPPORTED         |
| Materialized view          | 创建、查询和 `DBA_MVIEWS` 详情通过                  | 专属分类可见，普通表分类已排除对象及内部表                | PASS                |
| Package/advanced PL/SQL    | JDBC Package 返回 42，匿名块通过                  | 查询控制台无法解析                             | PARTIAL             |
| Scheduler Job              | JDBC disabled job 创建、系统视图回读和清理通过          | 当前不在对象分类中展示；页面创建 SQL 无法解析           | UNSUPPORTED         |
| User/role/DBLink           | 当前版本无可用官方枚举元信息                            | 删除空结果假实现并隐藏分类                       | UNSUPPORTED         |
| Failure matrix             | 密码、端口、不可达、超时、错误模式、缺失 Driver 均失败           | 生产 Factory 路径                         | PASS                |
| SSL                        | 服务端 TLS 和 CA 历史探针保留                         | 配置页隐藏 SSL，驱动属性固定关闭                  | UNSUPPORTED         |
| SSH                        | 本机 SSH 转发到 `192.168.2.1:8880` 后 JDBC PASS | CloudDM SSH 表单未跑                      | PARTIAL             |
| Multi-CN/failover          | 当前只有一个监听和一个 group                         | 无法构造节点切换                              | BLOCKED             |
| Overall                    | 数据库/驱动历史 TLS 能力保留                       | 当前已暴露对象通过；高级 PL/SQL 和结果类型仍有失败        | **PARTIAL**         |

## 5. 自动化探针结果

TLS 最终运行 `20260826-unified-oracle-complete-164148`：`PASS=34`、`FAIL=1`。TLS 前基线
`20260825-extended-oracle` 为 `PASS=34`、`FAIL=0`。

| Probe group          | Executed evidence                                                                            | Status   |
|----------------------|----------------------------------------------------------------------------------------------|----------|
| Driver/mode/identity | Driver load、URL、连接、`ORA_COMPATIBLE_MODE`、DATABASE/SYS_CONTEXT/USERENV                        | PASS     |
| TLS                  | JDBC SSLSession=`TLSv1.3/TLS_AES_256_GCM_SHA384`；DBProxy 状态错误回显 `TLSv1/ECDHE-RSA-AES256-SHA` | PASS     |
| Oracle objects       | 父子表、索引、VIEW、SEQUENCE、PROCEDURE、FUNCTION、TRIGGER                                              | PASS     |
| Advanced PL/SQL      | 匿名块变量、FOR、IF、Package spec/body、Package function=42                                           | PASS     |
| Materialized view    | 创建、查询、`_GDB_SYSDB.DBA_MVIEWS` 列表/详情                                                          | PASS     |
| Scheduler            | `FREQ=DAILY;INTERVAL=1` disabled job 创建和回读                                                   | PASS     |
| Prepared DML         | INSERT、子表 INSERT、UPDATE、视图回读、DELETE                                                          | PASS     |
| Transaction          | rollback 后 0 行、commit 后 1 行                                                                  | PASS     |
| Metadata             | schema/table/column/index/PK/UQ/FK/view/sequence/routine/trigger/mview                       | PASS     |
| ResultSetMetaData    | catalog/table/column/label/type/precision                                                    | PASS     |
| Cancel/recovery      | TLS 下 cancel 返回但查询 10 秒未结束；dbproxy 在约 30 秒后才路由 kill                                          | **FAIL** |
| Cleanup              | 官方表、字典、mview 和 scheduler 视图残留均为 0                                                            | PASS     |

## 6. CloudDM UI 与运行态

| UI scenario                  | Actual                                                                   | Status       |
|------------------------------|--------------------------------------------------------------------------|--------------|
| Test/save/reload             | 保存的 `codex-goldendb-oracle-e2e` 在重启后可见                                   | PASS         |
| Query/type rendering         | 业务 NUMBER/VARCHAR2/DATE/TIMESTAMP/CLOB 正确；元信息 unsigned 数值列显示 Unsupported | PARTIAL      |
| Manual rollback              | 插入 `990101` 后回滚，直连回读 0                                                   | PASS         |
| Manual commit                | 插入 `990102` 后提交，直连回读 1；清理后 0                                             | PASS         |
| Cancel/recovery              | 最新非 TLS 页面仍无法满足中断契约；当前按钮已隐藏                                           | UNSUPPORTED  |
| Explain                      | plain `EXPLAIN SELECT ...` 返回 SQLNode 和 SIMPLE 两行                        | PASS         |
| View detail                  | 名称、查询 SQL、状态等回读                                                          | PASS         |
| Sequence detail              | min/max/increment/cache/next number 等回读                                  | PASS         |
| Procedure/function detail    | 当前不在对象分类中展示；历史预建对象结果不作为产品支持                                             | UNSUPPORTED  |
| Trigger detail               | 当前不在对象分类中展示；历史预建对象结果不作为产品支持                                             | UNSUPPORTED  |
| Materialized view            | 专属分类可见，普通表分类只保留业务表                                                       | PASS         |
| Scheduler job                | 当前不在对象分类中展示；页面创建 SQL 无法解析                                                 | UNSUPPORTED  |
| Database tree                | 两个不可切换系统库已过滤，剩余 3 个 database 逐个打开无错误                                     | PASS         |
| User/role/DBLink categories  | 删除空集合/`null` 假实现；分类不再展示                                                  | UNSUPPORTED  |
| Materialized table isolation | mview 专属分类可见，普通表只保留业务表                                                   | PASS         |
| SSL                          | 配置页不再显示 SSL 模式，驱动属性固定 `useSSL=false/requireSSL=false`                    | UNSUPPORTED  |
| Query-console PL/SQL         | Procedure/Function/Trigger、匿名块、Package、Scheduler DDL 未进入执行日志             | **FAIL**     |
| Query-console metadata types | `INFORMATION_SCHEMA` 列/索引的 unsigned 数值字段显示 `Unsupported ... type`        | **FAIL**     |

## 7. 失败矩阵与安全边界

| Case                         | MySQL mode                                                      | Oracle mode                                    | Result       |
|------------------------------|-----------------------------------------------------------------|------------------------------------------------|--------------|
| Correct                      | 204ms 连接成功                                                      | 279ms 连接成功                                     | PASS         |
| Wrong password               | Access denied                                                   | Access denied                                  | PASS         |
| Wrong port `8899`            | CommunicationsException                                         | CommunicationsException                        | PASS         |
| Unreachable `192.0.2.1:8880` | 2001ms 超时                                                       | 2001ms 超时                                      | PASS         |
| Opposite compatibility mode  | expected oracle / actual mysql                                  | expected mysql / actual oracle                 | PASS         |
| Missing driver class         | `Load GoldenDB JDBC driver failed`，cause=ClassNotFoundException | 同一 Factory 路径                                  | PASS         |
| SSL                          | 历史 MySQL/Oracle Connector TLS 探针保留                                 | 当前 CloudDM 两种 GoldenDB 类型均关闭 SSL          | UNSUPPORTED  |

历史 JDBC SSLSession 证明数据库与 Connector 可以协商 TLSv1.3/1.2，但当前产品不声明该能力；
DBProxy 的 `Ssl_version/Ssl_cipher` 仍不能作为真实握手协议证据。

## 8. 元信息差异与修正

| Finding   | GoldenDB evidence                               | Implementation                           | Status  |
|-----------|-------------------------------------------------|------------------------------------------|---------|
| 表/视图      | `ALL_TAB_COMMENTS` 不存在，`ALL_OBJECTS` 不登记业务 VIEW | `INFORMATION_SCHEMA.TABLES/VIEWS`        | PASS    |
| 序列        | `ALL_OBJECTS` 不登记业务 SEQUENCE                    | `_GDB_SEQUENCE_SYSTB_INFO`               | PASS    |
| 过程/函数/触发器 | `ALL_PROCEDURES/ALL_OBJECTS` 不完整                | `_GDB_DICTIONARY_SYSTB_INFO`             | PASS    |
| 物化视图      | `SYS.ALL_MVIEWS` 不存在；对象被当作 BASE TABLE           | `_GDB_SYSDB.DBA_MVIEWS` 列表和详情覆盖          | PASS    |
| 外键        | `CONSTRAINT_TYPE='F'`，不是 Oracle 的 `R`           | KEY_COLUMN_USAGE/REFERENTIAL_CONSTRAINTS | PASS    |
| 索引        | 类型返回 `PRIMARY/MULTIPLE`                         | Provider 归一为 `NORMAL`                    | PASS    |
| 当前 schema | `ALTER SESSION SET CURRENT_SCHEMA` 不支持          | JDBC `Connection.setCatalog()`           | PASS    |
| 查询列类型     | 业务类型已通过；`INFORMATION_SCHEMA` 仍暴露 unsigned 数值类型  | 业务 Reader 已修复，元信息结果页仍缺映射                 | PARTIAL |

## 9. 最终制品

`package/package.sh --build`：382 个任务，`BUILD SUCCESSFUL in 2m 53s`。

| Archive               | Plugin member                              | SHA-256 match | Vendor JDBC JAR |
|-----------------------|--------------------------------------------|---------------|-----------------|
| `cgdm-alone.tar.gz`   | `cgdm/alone/plugins/ds-goldendb-lib.jar`   | PASS          | 0               |
| `cgdm-console.tar.gz` | `cgdm/console/plugins/ds-goldendb-lib.jar` | PASS          | 0               |
| `cgdm-sidecar.tar.gz` | `cgdm/sidecar/plugins/ds-goldendb-lib.jar` | PASS          | 0               |

三包插件 SHA `8806025f...bcd2c` 是历史布局验收制品；当前源码/运行插件为
`9af7424c...a4ff34`。本轮已重新构建并部署插件，未重新生成三个完整安装包。

## 10. 完整 PASS 前剩余项

1. 准备至少两个可控 CN 和可切换流量入口，验证负载均衡、坏端点、节点切换和事务恢复。
2. 补跑 CloudDM 表单内 SSH 隧道；当前只有外部 SSH 转发 + JDBC 的真实链路证据。
3. 修复 Oracle SQL Engine 对高级 PL/SQL DDL 的拆分/解析，并从查询控制台重跑。
4. 补齐 Oracle 查询结果对 `INFORMATION_SCHEMA` unsigned 数值类型的映射。
5. 如需重新开放 SSL、查询中断或隐藏对象类型，先补齐产品实现和矩阵，再恢复能力声明。

## 11. 复跑入口

仓库不再维护绕过 CloudDM 的 Oracle JDBC 直连探针。通过 GoldenDB Oracle 数据源表单建立连接后，
从 CloudDM 查询控制台执行总矩阵中已验证的 SQL，并按前端数据源流程检查元信息、事务、失败路径和清理。

完整的 MySQL/Oracle 数据源复测 SQL 统一维护在
[`goldendb-test-matrix.md` 的“数据源整体复测 SQL”章节](goldendb-test-matrix.md#12-数据源整体复测-sql)，本专项矩阵不再重复复制。
