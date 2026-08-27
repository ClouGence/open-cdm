# GoldenDB 数据源接入复测流程

当前执行状态、真实证据与未覆盖门禁见 `tests/datasource/goldendb/goldendb-test-matrix.md`。本文只维护可重复执行的 Chrome
流程，
不记录某一次执行的 PASS/FAIL。

## Purpose

验证 CloudDM 将 GoldenDB MySQL 与 GoldenDB Oracle 兼容模式作为两个独立数据源接入，并分别使用厂商对应 JDBC 驱动、SQL
family、
类型系统、元信息和会话实现。该流程防止用一个可切换模式的 GoldenDB 类型混用驱动、解析器或元信息。

## Scope

- 路由：`/#/datasource`、`/#/datasource/add?dsType=GoldenDBMySQL`、`/#/datasource/add?dsType=GoldenDBOracle`、`/#/sql`；
- 类型：`GoldenDBMySQL`（MySQL family）、`GoldenDBOracle`（Oracle family）；
- 版本基线：GoldenDB RHV6.1.03.12SP5、官方 `ZXCLOUD-GoldenDB-Java-Connector`；
- 能力：新增/编辑、驱动状态、对象树、表详情、查询、DML、事务、DDL、Explain 和错误路径；
- 不覆盖：生产数据、Insight 运维操作、数据重分布、CDC 管理、多级 CASE 分片图形化编辑，以及需要持久化真实远端密码的
  DBLink 工作链路。Oracle 物化视图由 GDB-UI-11 验收；Package、Job 和高级 PL/SQL 当前必须按 CloudDM 查询控制台解析失败记录，
  不能用直连 JDBC 探针结果替代。

GoldenDB Lite 7.3.02.02 可作为标准 JDBC、元信息、DML 和事务的补充回归环境，但它的单 DN 端口不能替代 CN 分布式 DDL 验收。

## Preconditions

- 使用明确的隔离 GoldenDB RHV6.1.03.12SP5 测试租户和 CN 地址；
- 官方 JDBC JAR 已分别按 `GoldenDB MySQL JDBC Driver / 5.1.46.86` 和 `GoldenDB Oracle JDBC Driver / 5.1.46.77`
  的运行时驱动目录约定安装；
- IDEA 中 `DmAloneLauncher` 为 `RUNNING`，运行时加载的是本次构建的 `ds-goldendb-lib.jar`；
- Chrome 已登录隔离测试账号，任务标签页位于 `codex` 组；
- 不读取浏览器 Cookie、Token、保存密码或 local storage。

## Test Data

- MySQL 数据源名称：`codex-goldendb-cn-e2e`；
- Oracle 数据源名称：`codex-goldendb-oracle-e2e`；
- MySQL Database：`codex_goldendb_validation`；Oracle Database：`goldendb_oracle_demo`；
- 基础表：`codex_gdb_account`，字段至少包含 `account_id`、`account_code`、`balance`、`updated_at`；
- 关联表：`codex_gdb_account_detail`，包含主键、复合唯一键、普通索引和外键；
- 分发表：HASH、RANGE、LIST、DUPLICATE 各一张，统一使用 `codex_gdb_*` 前缀；
- 事务基线行：每次创建唯一 `account_code=codex-ui-<run-id>-baseline`，初始 `balance=10.00`，不得依赖历史固定行。

## Suites

| Risk suite             | Scenarios                                             |
|------------------------|-------------------------------------------------------|
| Smoke                  | GDB-UI-01, GDB-UI-02                                  |
| Main Flow              | GDB-UI-03, GDB-UI-05, GDB-UI-06, GDB-UI-07, GDB-UI-11 |
| Boundaries             | GDB-UI-02, GDB-UI-04, GDB-UI-05                       |
| Extreme                | GDB-UI-05                                             |
| Repeat And Concurrency | GDB-UI-10                                             |
| Failure And Recovery   | GDB-UI-02, GDB-UI-08                                  |
| Lifecycle              | GDB-UI-09, GDB-UI-10                                  |
| Permission             | GDB-UI-03                                             |
| State Consistency      | GDB-UI-05, GDB-UI-06, GDB-UI-08                       |

### GDB-UI-01 类型、图标和驱动

- Priority：P0；Suite：Smoke。
- 初始状态：`/#/datasource`，GoldenDB 官方 JAR 已按脚本准备，未打开新增弹窗。
- 测试数据与准备：使用 `tests/datasource/goldendb/prepare_driver.sh` 安装已校验 SHA-256 的 5.1.46.86 JAR；不得换成 MySQL
  Connector/J。

1. 进入数据源列表并点击“新增”。
2. 确认主数据库分组中相邻出现 GoldenDB MySQL 和 GoldenDB Oracle，两者都显示用户提供的蓝橙 GoldenDB 字标。
3. 返回数据源列表，确认 GoldenDB 横版字标与其他方形图标在统一的 48px 图标槽内居中，所有数据源名称起始位置一致。
4. 分别进入表单，确认 MySQL 类型只提供 `GoldenDB MySQL JDBC Driver / 5.1.46.86`，Oracle 类型只提供
   `GoldenDB Oracle JDBC Driver / 5.1.46.77`，端口默认为 `5502` 且可编辑。
5. 分别移除两个官方 JAR 后刷新驱动状态，确认不会跨模式回退到另一驱动或 MySQL Connector/J。

预期：两个 GoldenDB 类型独立显示；列表图标和名称纵向对齐；驱动存在时状态可用，缺失时明确显示不可用且不能跨模式或静默改用
MySQL Connector/J。

清理：若步骤 5 临时移除了 JAR，重新执行准备脚本并在页面完成驱动准备，确认版本恢复可用。

### GDB-UI-02 连接和保存

- Priority：P0；Suite：Smoke、Boundaries、Failure And Recovery。
- 初始状态：`/#/datasource/add?dsType=GoldenDBMySQL`，使用隔离租户；持久化模式必须是 MySQL 兼容模式。
- 测试数据与准备：数据源名称追加本次唯一后缀；错误密码和错误端口只用于连接测试，不保存。

1. 填写 CN 地址、端口、Database、用户名和密码，测试连接后保存。
2. 分别验证错误密码、错误端口和不可达地址。
3. 使用 Oracle 兼容实例测试连接，确认页面明确提示仅支持 MySQL 兼容模式。
4. 进入“SSH/SSL”，确认只提供 SSH 通道，不显示 SSL 模式；MySQL 驱动属性固定 `sslMode=DISABLED`，
   Oracle 驱动属性固定 `useSSL=false/requireSSL=false`。

预期：正确配置成功；失败信息区分认证、网络和驱动；日志与页面不泄露密码或完整敏感参数。

清理：删除未保存的错误配置；成功配置保留到后续套件，全部套件结束后再按 Cleanup 处理。

### GDB-UI-03 元信息和对象详情

- Priority：P0；Suite：Main Flow、Permission。
- 初始状态：`/#/sql`，`codex-goldendb-cn-e2e` 已连接并能展开 `codex_goldendb_validation`。
- 测试数据与准备：从 `tests/datasource/goldendb/goldendb-test-matrix.md` 第 12 章执行页面已验证 SQL，创建本轮唯一前缀的
  table/view 对象；
  最小权限账号由隔离环境单独准备，没有安全账号时权限场景标记 `SKIP`。

1. 进入 SQL 工作台并展开 `codex-goldendb-cn-e2e`，逐个双击数据源树列出的 database。
2. 验证 `_gdb_audit`、`dbagent`、`information_schema`、`performance_schema`、`recyclebin` 不作为可切换 database 展示；
   `_gdb_query_rewrite`、`_gdb_sysdb`、`heartbeat_info`、`mysql`、`sys` 和业务库能够打开且没有 `listLeaf` 错误。
3. 打开 `codex_goldendb_validation`，确认分类只包含表和视图并逐项刷新；Procedure/Function/Trigger 不应显示。
4. 打开 `codex_gdb_account_detail`，逐项核对列类型、默认值、精度、主键、复合唯一键、普通索引和外键。
5. 打开 HASH/RANGE/LIST/DUPLICATE 表，核对表属性中的分发类型、分布键和分片 group 与服务端
   `SHOW DISTRIBUTION FROM <table>` 的 `Dist_type/Dist_key/Groups` 一致。
6. 使用只具备业务库读取权限的账号复测，记录无权读取 distribution/routine/trigger 时的局部降级行为。

预期：数据源树只展示可切换 database，任何可见节点都能展开且不弹 `Unknown database`；五类业务对象与服务端一致；
partition 与 distribution 不混淆；分发属性来自 `SHOW DISTRIBUTION` 的结构化结果，不通过 DDL 文本或 `SHOW INDEX`
猜测全局索引范围；单个对象类别无权限不会拖垮整个数据源树。

清理：关闭对象详情页；删除本轮唯一前缀的表、视图，并刷新两个分类确认对象均不存在。

### GDB-UI-04 MySQL SQL Engine

- Priority：P0；Suite：Boundaries。
- 初始状态：GoldenDB MySQL 工作台页签已打开，SQL Engine 为 `MySQL`，下拉框无其他候选项。
- 测试数据与准备：只使用 MySQL 8.0 兼容 SQL。

执行并回读：反引号、CTE、`IFNULL`、`LIMIT` 和窗口函数，并覆盖一条多表 UPDATE 和一条多表 DELETE 的分析场景。
单独执行 `ON DUPLICATE KEY UPDATE`，当前 RHV6.1.03.12SP5 返回 `must be 'SW'`，按 `UNSUPPORTED` 记录，
不得写入页面通过 SQL 清单。

预期：标准兼容 SQL 由 MySQL Engine 完成拆分、行为/权限分析和执行；不显示 `GoldenDB SQL`；GoldenDB 专有分布式 DDL 不列入已支持的自动
SQL 分析矩阵。

清理：本场景只读，无额外对象。

### GDB-UI-05 DML 和结果类型

- Priority：P0；Suite：Main Flow、Boundaries、Extreme、State Consistency。
- 初始状态：`/#/sql` 的 `codex_goldendb` 页签处于自动事务模式。
- 测试数据与准备：每次执行生成唯一 `account_id` 与 `account_code=codex-ui-<run-id>`，不得依赖上次遗留固定 ID。

1. INSERT 本次唯一 `account_code`，`balance=30.00`；
2. UPDATE 为 `31.00`；
3. DELETE `dml-check`；
4. SELECT 回读，并检查 DECIMAL、DATETIME、BLOB/VARBINARY、JSON 和 NULL；
5. 分别查询空结果、单条结果、恰好跨过当前分页边界的结果；确认页码、总数和首末行稳定。
6. 查询足够大的结果集，确认分页/流式读取不会一次性占满客户端内存。

预期：INSERT、UPDATE、DELETE 均显示真实影响行数；最终本次唯一行数量为 0，类型显示与官方 JDBC 返回一致。

清理：DELETE 本场景唯一 `account_id/account_code` 并 SELECT 回读为 0；恢复自动事务。

### GDB-UI-06 事务和隔离级别

- Priority：P0；Suite：Main Flow、State Consistency。
- 初始状态：`/#/sql`，使用本次唯一事务测试行，页面当前为自动事务。
- 测试数据与准备：基线值在切换手动事务前 SELECT 记录；只修改隔离测试行。

1. 切换手动事务并记录页面实际隔离级别。
2. UPDATE 本次唯一基线行到 `99.00` 后回滚并回读。
3. 再次 UPDATE 后提交并回读，随后恢复初始值。
4. 依次验证服务端真实支持的隔离级别。

预期：rollback/commit 终态正确；当前 RHV6.1.03.12SP5 CN 只显示并使用 READ COMMITTED，不把服务端会回落的其他隔离级别宣称为可用；测试结束恢复自动事务。

清理：回滚未提交事务，恢复基线值并提交，最后切回自动事务并回读。

### GDB-UI-07 GoldenDB DDL

- Priority：P0；Suite：Main Flow。
- 初始状态：真实 GoldenDB CN MySQL 兼容租户，表结构编辑器可用；Lite 单 DN 不满足本场景前置条件。
- 测试数据与准备：HASH/RANGE/LIST/DUPLICATE 表名均追加唯一后缀；group、分发表达式和索引名来自该 CN 的真实拓扑。

1. 通过表结构编辑器分别创建 HASH、RANGE、LIST、DUPLICATE 表。
2. 刷新对象树并核对生成 DDL、分发属性和 group。
3. 新增并删除普通索引；通过 SQL 创建/删除全局索引只验证服务端 DDL，在找到官方元信息接口前不校验 GLOBAL/LOCAL 回显。
4. ADD COLUMN 后再 DROP COLUMN，确认无幽灵列。
5. 多级 CASE 表只验证只读回显，不进入图形化保存。

预期：支持的单层分发 DDL 能生成和执行；类型、分布键和分片 group 通过 `SHOW DISTRIBUTION` 回读；不从 `SHOW CREATE TABLE`
解析元信息。

清理：先删除全局/普通索引，再删除本次唯一分发表；刷新对象树确认不存在。

### GDB-UI-08 Explain 与不支持能力边界

- Priority：P0；Suite：Failure And Recovery、State Consistency。
- 初始状态：`/#/sql`，GoldenDB MySQL 和 Oracle 页签已打开。
- 测试数据与准备：使用存在的只读业务表。

1. MySQL 模式确认显示“执行计划”；分别对常量查询和存在的只读表查询点击执行计划。
2. 确认执行阶段基于原始 SQL 生成普通 `EXPLAIN`，结果包含 CN 的 SQLNode 和 DN 计划行。
3. 确认 MySQL 和 Oracle 页签均不显示“中断”按钮。
4. 确认配置页不显示 SSL 模式。
5. 重启一个测试 CN 或切换负载均衡节点，复测连接恢复。

预期：GoldenDB MySQL 暴露可用的执行计划按钮，普通 `EXPLAIN` 返回计划结果；两种模式均不声明 SSL 和查询中断，
未通过的能力不会以可点击按钮或可选配置出现。

清理：关闭失效页签。

### GDB-UI-09 最终制品

- Priority：P0；Suite：Lifecycle。
- 初始状态：源码检查和测试已完成，尚未以最终 fat JAR 重启后端。
- 测试数据与准备：记录源码 fat JAR 与运行目录 JAR 的 SHA-256；驱动 JAR 不进入 Git 或内置驱动包。

1. 验证 `:ds-goldendb:build`、`:s-test:test`、前端检查和 `package/all_build.sh web`。
2. 验证 `package/all_build.sh plugin ds-goldendb` 生成 fat JAR。
3. 执行 `package/package.sh --build`，确认 console、sidecar、alone 三个 tgz 都包含 `plugins/ds-goldendb-lib.jar`，且不包含未授权的厂商
   JDBC JAR。
4. 确认运行目录中的插件包含 `GoldenDBMySQLDsPlugin`、`GoldenDBOracleDsPlugin`、两个 driver family、SPI 和两种模式的元信息实现。
5. 精确重启 IDEA execution，在最终运行时重做 GDB-UI-01、03、05、07 的关键步骤。

预期：源码、fat JAR 和运行时加载版本一致；构建前或旧插件上的浏览器结果不能替代本项。

清理：保留最终 `DmAloneLauncher` 实例运行；删除构建过程中产生的非标准临时文件，不删除正常构建目录。

### GDB-UI-10 重复操作、并发与生命周期

- Priority：P1；Suite：Repeat And Concurrency、Lifecycle。
- 初始状态：`/#/datasource` 和 `/#/sql` 各保留一个已加载完成的 GoldenDB 页面，不存在未提交事务。
- 测试数据与准备：只使用 SELECT 和测试连接；多标签页不得执行相互覆盖的 DML。

1. 快速连续点击两次“测试”，确认按钮 loading/禁用阻止重复连接任务或两个结果均正确收敛。
2. 在 SQL 工作台快速连续点击“执行”，确认 loading/禁用状态阻止重复查询且最终只有一个终态。
3. 在两个 Chrome 标签页同时执行不同 SELECT，确认结果不会串页签。
4. 刷新、前进后退、返回工作台再进入 SQL 工作台，确认已保存数据源仍存在；后端重启后先完整刷新 SQL
   工作台，再关闭并重开失效查询页签，确认执行按钮恢复可用。

预期：没有重复保存、旧响应覆盖新状态或跨页签结果污染；按钮结束 loading；浏览器 Console 没有新增 GoldenDB 相关错误。

清理：关闭额外 Chrome 页签与查询页签，不删除保存的数据源，确认没有手动事务遗留。

### GDB-UI-11 Oracle 兼容模式

- Priority：P0；Suite：Main Flow、Failure And Recovery、State Consistency。
- 初始状态：`/#/datasource`，GoldenDB Lite 或 CN 为 Oracle 兼容模式，Oracle Connector 5.1.46.77 已准备。
- 测试数据与准备：只使用隔离 schema/database；基础对象名称使用 `codex_gdb_oracle_*` 前缀。

1. 使用 `GoldenDB Oracle` 类型测试并保存连接，确认 `GoldenDB MySQL` 连接同一实例时返回兼容模式不匹配。
2. 进入 SQL 工作台，确认 SQL Engine 为 `Oracle SQL`，执行 `SELECT 1 FROM DUAL`、`SYS_CONTEXT`、`USERENV` 和 Oracle 类型表达式。
3. 展开数据源并逐个双击可见 database；`information_schema`、`performance_schema` 不应作为可切换 database 展示，
   `_gdb_sysdb`、`sys` 和业务库应能正常打开且没有 `listLeaf` 错误。
4. 在业务库确认只展示表、视图、物化视图和序列，逐项刷新不得报错；其他未完成分类不得显示。
5. 展开 table、view、sequence 和 materialized view；表/视图使用 `INFORMATION_SCHEMA`，序列和物化视图使用
   GoldenDB 对应 systb/DBA 视图，不得依赖该版本不存在的 `ALL_TAB_COMMENTS`、`SYS.ALL_MVIEWS`。
6. 打开业务表详情，确认列、主键和 `PRIMARY/MULTIPLE` 索引类型不会触发 Oracle enum NPE；全局索引物理表不进入对象树。
7. 创建物化视图后刷新表和物化视图分类；物化视图及 `*_gdb_tmp_mview` 内部表只能出现在物化视图分类，不能进入普通表。
8. 查询 NUMBER、VARCHAR2、DATE、TIMESTAMP、CLOB、BLOB，确认结果页不会显示 `Unsupported decimal/varchar type`。
9. 执行有意义的 INSERT、UPDATE、DELETE，并验证 commit/rollback。
10. 确认页面不显示“中断”按钮。
11. 在查询控制台创建物化视图并通过 `_GDB_SYSDB.DBA_MVIEWS` 回读；Procedure/Function/Trigger、Package、
    包含变量/循环/条件的匿名块和 Scheduler Job 当前被 Oracle SQL Engine 标记为“语句无法解析”，按 FAIL 记录。
    数据库客户端直连结果不能替代查询控制台 PASS。
12. 进入数据源“SSH/SSL”配置，确认只提供 SSH 通道，不显示 SSL 模式。

预期：Oracle 类型只加载 Oracle Connector 和 Oracle SQL family；模式选错时连接被拒绝；数据源树不展示无法
`setCatalog()` 的系统 schema；所有可见分类点击不报错；物化视图及内部表不泄漏到普通表；基础 Oracle
表/索引/外键/视图/序列/物化视图、DML 与事务可用。高级 PL/SQL 的数据库直连结果与查询控制台结果分开记录，不互相替代。

清理：回滚未提交事务，删除本场景创建的 `codex_gdb_oracle_*` 对象，保留隔离数据源供后续回归。

## Cleanup

- 回滚全部手动事务并恢复自动事务；
- 删除本流程创建的 `codex_gdb_*` 对象；
- 未确认隔离范围时不删除数据源配置或厂商驱动文件；
- 保存不含敏感信息的构建结果、运行日志和关键截图。

## Skip Conditions

- GoldenDB 实例、license 或官方 JDBC 缺失：真实连接、元信息、DDL/DML 用例为 `BLOCKED`；
- 只有 GoldenDB Lite 单 DN：GDB-UI-07 和活动 CN 重连为 `BLOCKED`，不能替代 CN 验收；
- 隔离租户管理员不能创建临时用户：最小权限矩阵为 `BLOCKED`，不得复用生产账号或降低密码策略；
- 没有可控 SSH 或多 CN 环境：对应安全与重连分支为 `BLOCKED`，普通连接成功不能替代；
- Chrome 未登录：UI 用例为 `BLOCKED`，不得用纯 API 冒充；
- 运行插件不是本次构建：运行态用例为 `NOT RUN`；
- 未完成已暴露元信息、DML、事务和 DDL 时，不能因为连接成功而将 GoldenDB 标为 PASS。
