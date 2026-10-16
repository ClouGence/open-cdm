## 新增

- 新增个人 SQL 收藏，支持保存完整脚本、按名称搜索、打开、覆盖保存、另存、重命名和删除；同一账号可跨设备访问，支持并发修改冲突检测和未保存修改的页签关闭提醒（[PR #365](https://github.com/ClouGence/open-cdm/pull/365)）。
- 新增 MySQL 管理语句扩展支持，覆盖复制管理、持久化变量、表维护和执行计划相关语法的解析、分类与行为分析（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 新增 MariaDB 管理语句扩展支持，覆盖备份阶段、单语句变量设置、复制管理、统计信息和查询终止等语句的拆分、分类与行为分析，并按 MariaDB 版本识别语法（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 新增 PostgreSQL 管理语句扩展支持，覆盖会话与系统设置、用户与角色、授权、维护和执行计划相关语句，并补充内置函数和系统对象识别（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 新增 TiDB 管理语句扩展支持，覆盖 `ADMIN`、集群配置、Region 拆分、统计信息、执行计划绑定与 `PLAN REPLAYER` 等语句的解析、分类与行为分析（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 新增 Doris 管理语句扩展支持，覆盖集群管理、用户与角色、工作负载组、统计信息、作业和物化视图管理语句的拆分与行为分析，并补充内置函数和系统对象识别（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 新增 达梦 管理语句扩展支持，覆盖备份恢复等语句的行为分析与对象引用识别，并补充内置函数支持（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 新增 Greenplum 特有 SQL 语法支持，覆盖外部表、协议、资源队列与资源组等语句的拆分、行为分析和权限分析（[PR #362](https://github.com/ClouGence/open-cdm/pull/362)）。
- 新增 Cloudberry 特有 SQL 语法支持，覆盖外部表、协议、资源管理及角色扩展语句的拆分、行为分析和权限分析（[PR #356](https://github.com/ClouGence/open-cdm/pull/356)）。
- 新增 Oracle 管理语句支持，以 19c 为基线，覆盖会话、系统、角色、审计策略、表空间和数据库管理语句的解析、分类与行为分析（[PR #338](https://github.com/ClouGence/open-cdm/pull/338)）。
- 新增 SQL Server 管理语句支持，以 2022 为基线，支持访问控制、系统管理、维护操作和执行计划相关语句的解析与行为分析（[PR #345](https://github.com/ClouGence/open-cdm/pull/345)）。
- 新增 StarRocks 管理语句支持，以 3.5.21 为基线，覆盖管理操作、作业和执行计划相关语句的解析与行为分析（[PR #348](https://github.com/ClouGence/open-cdm/pull/348)）。
- 新增 ClickHouse 管理语句支持，以 26.8 LTS 为基线，覆盖访问控制、系统管理、维护操作和执行计划相关语句的解析与行为分析（[PR #354](https://github.com/ClouGence/open-cdm/pull/354)）。
- 新增 OceanBase for MySQL 管理语句支持，以 4.2.5 LTS 为基线，覆盖会话、用户与角色、系统管理、维护操作和执行计划相关语句的解析与行为分析（[PR #366](https://github.com/ClouGence/open-cdm/pull/366)）。
- 新增 MongoDB 只读 `rs.*`、`sh.*` Shell 辅助命令支持与自动补全，可查询副本集配置、复制状态、分片和均衡器状态，由社区贡献者 [@48N6E](https://github.com/48N6E) 提交，感谢贡献（[PR #333](https://github.com/ClouGence/open-cdm/pull/333)）。

## 修复

- 修复 Doris 非事务工单执行结束或暂停后状态未正确上报、仍停留在执行中的问题，确保成功、失败和暂停状态正确更新（[PR #368](https://github.com/ClouGence/open-cdm/pull/368)）。
- 修复 ClickHouse `Enum8`、`Enum16`、`LowCardinality` 和 `SimpleAggregateFunction` 等列类型的结果读取问题，并完善带参数类型、嵌套和地理类型的展示及负数、超过 24 小时时间值的格式化（[PR #369](https://github.com/ClouGence/open-cdm/pull/369)、[PR #368](https://github.com/ClouGence/open-cdm/pull/368)）。
- 修复 SQL Server `sql_variant` 列无法正确读取的问题，其中二进制值以十六进制展示（[PR #370](https://github.com/ClouGence/open-cdm/pull/370)）。
- 修复 SQL 执行成功但未返回影响行数时错误显示 `-1` 行受影响的问题，改为提示执行成功，并补齐执行结果提示的中文文案（[PR #371](https://github.com/ClouGence/open-cdm/pull/371)）。
- 修复 MySQL、MariaDB、PostgreSQL、Doris、TiDB 和达梦部分 SQL 的拆分、语句分类与行为分析问题，以及查询行数限制重写时的语句类型判断错误（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 修复 SQL 工作台在自动提交模式下查询后关闭会话、导致会话设置丢失的问题；解析时使用当前会话的 `SQL_MODE`，并完善手动提交模式下事务语句的执行与状态同步（[PR #337](https://github.com/ClouGence/open-cdm/pull/337)）。
- 修复 PostgreSQL 的 `vector`、`halfvec` 列类型带 Schema 前缀时无法识别的问题，由社区贡献者 [@sunjiajie](https://github.com/sunjiajie) 提交，感谢贡献（[PR #280](https://github.com/ClouGence/open-cdm/pull/280)）。
- 修复审批调度反复更新空闲工单修改时间、放大数据库写入和 Binlog 的问题；分开扫描活跃与空闲工单，并减少空闲检查时的 SQL 正文读取（[PR #339](https://github.com/ClouGence/open-cdm/pull/339)）。
- 修复环境管理中配置 SQL 工单时，审批流程模板下拉列表超过 6 项后无法完整浏览的问题，由社区贡献者 [@fengruiwd](https://github.com/fengruiwd) 提交，感谢贡献（[PR #340](https://github.com/ClouGence/open-cdm/pull/340)）。
- 修复 SQL 工作台中数据源备注、工单标题与描述、数据生成“遇到错误时继续”选项的表单绑定问题，确保输入和勾选值正确更新，由社区贡献者 [@fengruiwd](https://github.com/fengruiwd) 提交，感谢贡献（[PR #347](https://github.com/ClouGence/open-cdm/pull/347)）。
- 修复 SQL 工作台数据源连接异常提示不清晰的问题，显示失败数据源的名称与图标，并简化弹窗操作（[PR #365](https://github.com/ClouGence/open-cdm/pull/365)）。
