# KingbaseES 四兼容模式数据源接入流程

## Purpose

验证 CloudDM 将 KingbaseES PostgreSQL、MySQL、Oracle、SQLServer 作为四个独立类型展示，并分别绑定对应 SQL Engine、
Dialect 和 Session 能力。该流程防止只显示类型、只通过连接测试或只证明服务端语法可执行。

## Scope

- 路由：`/#/datasource`、四种 `/#/datasource/add?dsType=...`、`/#/sql`；
- 类型：`KingbaseESPostgreSQL`、`KingbaseESMySQL`、`KingbaseESOracle`、`KingbaseESSQLServer`；
- 能力：创建/编辑、对象树、查询、DML、事务、DDL、Explain、中断恢复、错误模式，以及由
  `/api/entry/dmConsoleSettings` 返回的 Catalog/Schema 层级能力；
- 不覆盖：生产数据源、未经矩阵验证的 KingbaseES/JDBC 版本。open-cdm 前端不再包含 CloudCanal 产品兼容分支。

## Preconditions

- `/Users/pika/docker_opt/localdb/kingbasees` 中四个 Compose 项目均 running/healthy；
- 宿主端口依次为 54321（pg）、54322（mysql）、54323（oracle）、54324（sqlserver）；
- IDEA 中 `DmAloneLauncher` 为 RUNNING，运行时加载的是已复核的 fat JAR；
- Chrome 已登录隔离测试账号，任务标签页位于 `codex` 组；
- 已读取 `.design_docs/kingbasees-datasource-test-spec.md`；
- 不读取浏览器 Cookie、Token、保存密码或 local storage。

## Test Data

| Mode | Data source name | Basic table | Metadata child table |
| --- | --- | --- | --- |
| pg | `kes-v9r1c10b4-codex` | `public.codex_kes_pg_e2e` | `public.codex_kes_pg_meta_child` |
| mysql | `codex-kes-mysql-e2e` | ``public.codex_kes_mysql_e2e`` | ``public.codex_kes_mysql_meta_child`` |
| oracle | `codex-kes-oracle-e2e` | `public.codex_kes_oracle_e2e` | `public.codex_kes_oracle_meta_child` |
| sqlserver | `codex-kes-sqlserver-e2e` | `[public].[codex_kes_sqlserver_e2e]` | `[public].[codex_kes_sqlserver_meta_child]` |

每张表至少包含 `id`、`code`、`amount`，初始数据为 `alpha=10.00`、`beta=20.00`。

## Suites

### KES-UI-01 四类型入口

1. 进入 `/#/datasource`，点击“新增”。
2. 确认出现 KingbaseES PostgreSQL、MySQL、Oracle、SQLServer 四个按钮。
3. 逐个进入表单，确认图标、默认端口 54321、驱动族 `KingbaseES JDBC Driver`、版本 9.0.1。

预期：四个类型独立存在，不显示为一个混合模式类型。

### KES-UI-02 连接、保存与模式匹配

每种类型分别填写对应端口、Catalog `test`、Schema `public` 和隔离账号，点击“测试连接”后保存。

预期：正确模式成功；错误密码/端口有明确错误；把某类型指向另一模式端口时显示 expected/actual 模式不匹配，不自动回退。

### KES-UI-02A Catalog/Schema 能力契约

1. 读取 `/api/entry/dmConsoleSettings`，确认 `data.dsSettingDef[dsType].categories.levels` 是页面层级能力的唯一来源。
2. 对四个 KingbaseES 类型分别记录 `levels`，并检查工单和安全规则范围页面生成的资源 key。
3. Catalog + Schema 类型的 key 必须同时包含 catalog 和 schema；只有 Schema 的类型不得补空 catalog。
4. 新增数据源类型后不修改前端静态类型数组，刷新页面即可按接口返回层级生效。

预期：页面不再依赖 `CATALOG_SCHEMA_TYPES`、`hasSchema` 或数据源类型硬编码；接口层级变更后四个入口使用一致的资源路径，
Chrome Console error 为 0。

### KES-UI-03 对象树和结果类型

1. 进入 `/#/sql`，展开目标实例。
2. MySQL/Oracle 展开到 Schema；PostgreSQL/SQLServer 展开 Catalog `test` 后再展开 Schema。
   MySQL 模式不得显示 `information_schema`、`pg_catalog`、`pg_toast`、`pg_temp_*`、`pg_toast_temp_*`、
   `sys_catalog` 等内部 schema，只保留业务 `public`。
   Oracle 模式不得显示 `information_schema`、`pg_catalog` 和 KingbaseES 内部 schema，必须保留业务 `public`。
   SQLServer 模式不得显示 `pg_catalog`、`pg_toast`、`pg_temp_*`、`sys_catalog` 等内部 schema，必须保留
   `dbo` 和业务 `public`。
   PostgreSQL、SQLServer 模式不得显示 `kingbase`、`security`、`template0`、`template1` 系统 Catalog。
   对已展开的实例和 Catalog 执行右键“刷新”后，旧系统 schema 必须被移除，不得重复追加节点或产生前端 Console 错误。
3. 双击 `public`，确认对应 `codex_kes_*_e2e`、`codex_kes_*_meta_parent` 和 `codex_kes_*_meta_child` 出现。
4. 四个模式分别打开 `meta_child` 表详情，必须逐项确认五列、主键、复合唯一键、普通复合索引和外键；不能只检查表名或
   JDBC metadata 数量。
5. PostgreSQL/Oracle 模式必须真实展开索引详情，回归 `pg_get_indexdef` 表达式解析；不得出现
   `trim set should have only one character`。
6. MySQL 模式确认服务端 `information_schema.columns.DATA_TYPE` 返回 `number` 时表详情仍能完整加载；索引、唯一键和外键
   由 KingbaseES 可执行的 metadata 查询读取；刷新对象树后遍历 `public` 当前全部表、视图、函数、过程和触发器，覆盖
   `character_data`、`sql_identifier`、`cardinal_number`、`yes_or_no`、`oid` 等真实物理类型，确认不出现任何
   `Unsupported KingbaseES MySQL physical type`，也不出现 `AUTO_INCREMENT`、`DEFINER` 保留字语法错误。
7. SQLServer 模式确认 `KingbaseES V009R001C010` 由 `KingbaseESSQLServerMainVersion` 处理，列、主键、复合唯一键、普通索引
   和外键完整显示；打开“视图”分类并逐个展开业务视图，不得访问服务端不存在的 `sys.all_views`。
8. PostgreSQL、Oracle、SQLServer 同样遍历当前可见的表、视图、函数、过程和触发器，不得只验证 `meta_child`。
9. 快速连续切换 MySQL → Oracle → PostgreSQL → SQLServer 的表/视图详情，等待异步请求结束后检查 Chrome Console，确认
   没有 `columnList` 写入旧节点的异常，也没有把前一模式 metadata 写入当前 tab。
10. 执行模式专项 SELECT，检查真实单元格值。

预期：对象树和详情完整；字符串和数值真实显示；不得出现 `trim set should have only one character`、
`Unsupported varchar2/nvarchar/number/numeric type`、
`Unsupported KingbaseES MySQL physical type` 或缺少 `INFORMATION_SCHEMA.STATISTICS` 的错误。
SQLServer 模式不得出现 `Unsupported SqlServer version KingbaseES`、`DBCC USEROPTIONS` 或缺少
`sys.default_constraints`、`sys.all_views` 的错误；四模式快速切换后 Chrome Console error 必须为 0。

### KES-UI-03A MySQL 物理类型矩阵

1. 在隔离 MySQL 模式实例创建一次性表，至少覆盖 TINYINT、SMALLINT、INT、BIGINT、DECIMAL、NUMERIC、FLOAT、DOUBLE、
   BOOLEAN、BIT、CHAR、VARCHAR、TEXT、DATE、TIME、DATETIME、TIMESTAMP、BINARY、VARBINARY、BLOB、JSON。
2. 从 `information_schema.columns` 记录服务端真实 `DATA_TYPE`，重点检查 `number`、`float4`、`double precision`、
   `boolean`、`bpchar`、`character_data`、`sql_identifier`、`cardinal_number`、`yes_or_no`、`oid`。
3. 在 CloudDM 双击该表，确认全部字段可见。
4. 写入并回读 DECIMAL、FLOAT、DOUBLE、BOOLEAN 非空值。

预期：表详情无错误；数值和布尔值以真实值显示，不出现 `Unsupported ... type.` 占位符。测试结束后精确删除类型矩阵表。

### KES-UI-04 模式专项查询

- PostgreSQL：CTE、`RETURNING`、`ON CONFLICT`、dollar quote；
- MySQL：反引号、`IFNULL`、`LIMIT`、`ON DUPLICATE KEY UPDATE`；
- Oracle：`NVL`、`DUAL`、sequence、`MERGE`、`SYSDATE`、`TO_CHAR`；
- SQLServer：方括号、`TOP`、`ISNULL`、`IDENTITY`、`NVARCHAR`。

每个模式至少执行一条只在该兼容族有代表性的 SELECT，并检查实际结果单元格；语句能直接在 KingbaseES 服务端运行，不代表
CloudDM 已通过，必须同时确认 CloudDM 对应 SQL Engine 的解析、切分、安全分析和 Session 执行链路均成功。

预期：CloudDM 使用对应原生 SQL Engine 完成解析和执行，不使用 `sql-kingbasees`；四个模式分别记录通过语句和结果值。

### KES-UI-05 DML

每模式在 `meta_child` 上分别执行：

1. INSERT `dml-check=30.00`；
2. UPDATE `dml-check=31.00`；
3. DELETE `dml-check`；
4. SELECT 回读。

预期：三种 DML 均显示受影响 1 行；最终 `dml-check=0`，基线行 `child-a=20.25`。

### KES-UI-06 事务

1. 在 CloudDM 页签切换手动事务；记录界面显示的实际隔离级别。
2. UPDATE `child-a=99.00`，确认影响 1 行。
3. 点击“回滚”。
4. SELECT 回读。

预期：回滚后 `child-a` 仍为 20.25。不得用自动事务页签中直接执行 `BEGIN/ROLLBACK` 代替手动事务 UI 验证；流程结束前
回滚所有事务并把页签恢复为自动事务。

### KES-UI-07 DDL 与 Explain

1. 使用当前模式语法 ADD COLUMN `note`，再 DROP COLUMN。
2. 提交事务。
3. 对查询点击“执行计划”。

预期：两条 DDL 都成功；执行计划返回 `QUERY PLAN`；刷新对象树没有幽灵列。

### KES-UI-08 中断与恢复

1. 执行 `SELECT pg_sleep(30)`（Oracle 模式可带 `FROM DUAL`）。
2. 点击“中断”，确认取消。
3. 手动事务点击回滚。
4. 执行模式对应的 recovery SELECT。

预期：执行信息显示“正在执行的查询被取消”和服务端 cancel 错误；回滚后同页签恢复 SELECT 成功。MySQL/Oracle/SQLServer
不得因取消而永久关闭连接。

### KES-UI-09 后端/容器恢复

1. 保持四个 SQL 页签，精确重启 IDEA execution。
2. 旧 Session 出现失效提示后点击重新连接。
3. 重启一个目标 Compose 项目，重复连接测试和 SELECT。

预期：旧请求结束 loading；新 Session 正常；其他三个模式不受影响。

### KES-UI-10 最终一致性

刷新数据源列表、对象树和结果页，使用只读 SQL 回查结构与数据。

预期：四个类型、对象、数据和事务终态与服务端一致；每个 `meta_child` 均为 1 行、`score=20.25`、`dml-check=0`、
`note_columns=0`、3 个主键/唯一键/外键约束、3 个索引；无重复数据源、永久 loading 或错误 SQL Engine。

### KES-UI-11 最终制品复测

1. 执行完整 `package/all_build.sh` 和 `package/package.sh --build`。
2. 对比源码 fat JAR、运行目录插件和三种安装包内 `ds-kingbasees-lib.jar` 的 SHA-256。
3. 精确重启 IDEA execution，确认运行时加载最终插件。
4. 在最终运行时重新展开四个 `meta_child` 表详情，并分别执行一条模式专项 SELECT。

预期：构建和发行包均成功；插件 SHA-256 一致；四个模式在最终制品上继续通过。构建前的浏览器结果不能替代本项。

### KES-UI-12 公共模块影响面

当修复修改 `dsc-common-postgres/mysql/oracle/sqlserver` 时，列出该 common 的所有直接运行时消费者；现有环境中可连接的原生
数据源必须至少展开一张带主键/索引的表详情。没有对应 Greenplum、Cloudberry、Hologres 等环境时，只能记录目标模块/完整
构建 PASS 和运行时 `NOT RUN`，不得写成运行时 PASS。

预期：公共实现的直接消费者都有明确结果；本轮 `dsc-common-postgres` TRIM 修改至少在原生 PostgreSQL 和 KingbaseES 相关模式
上完成真实 UI metadata 回归。

### KES-UI-13 CloudDM 单产品前端

1. 进入 `/#/datasource`、`/#/data-access/cluster`、`/#/system/permission` 和 `/#/sql`。
2. 确认页面只走 CloudDM 路由、权限和 `/api/entry/**` API，不请求 `/cloudcanal/**`。
3. 新增数据源弹窗仍显示四个 KingbaseES 类型；集群页只显示 CloudDM 字段和 `DM_WORKER_MANAGE` 对应操作。
4. 账号资源授权只展示数据源资源，不展示 CloudCanal DataJob/产品集群切换入口。
5. 刷新并往返切换上述页面，检查 Chrome Console。
6. 访问已移除的 `/system/data_rules`、`/system/desensitization` 和 `/system/data_code`，确认不会再加载旧页面或请求
   `datahandle`、`datadesensitizerule` 等不存在的接口。

预期：不存在 CloudCanal 产品开关、回退路由、服务定义、DataJob 授权入口或品牌文案；CloudDM 页面正常加载且 Console error 为 0。

## Cleanup

- 回滚全部手动事务；
- 删除 `codex_kes_*_e2e` 表、view 和 sequence；
- 保存四模式日志、截图和矩阵报告；
- 未获得浏览器删除确认时保留 CloudDM 测试数据源，并在报告记录；
- 执行四个 Compose 项目的 `down --remove-orphans`，不使用 `-v`，不删除 data 目录。

## Skip Conditions

- 镜像/license/JDBC 缺失：真实数据库用例为 `BLOCKED`；
- Chrome 未登录：UI 用例为 `BLOCKED`，不得用纯 API 冒充 UI；
- 任一模式容器未 healthy：该模式后续写入用例 `NOT RUN` 或 `BLOCKED`；
- 没有完成对象树、DML、事务、DDL、中断恢复时，不能因为连接成功而将该模式标为 PASS。
