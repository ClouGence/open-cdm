## 亮点

- SQL 工单新增跨数据库 DML 数据影响与执行计划分析，可持续展示准备、分析、跳过和失败状态，并支持中断恢复。
- SQL 引擎按数据库版本和会话参数统一解析、拆分、行为分析、安全规则、列血缘与改写能力。
- 工单列表支持按描述和内联 SQL 内容搜索，结果集下载支持自定义文件名，并记录导出与下载审计。
- Oracle 支持可配置客户端字符集，ClickHouse 补充复杂数据类型读取及连接兼容性。

## 新增

- 新增 SQL 工单 DML 数据影响与执行计划分析，覆盖 MySQL、TiDB、Doris、达梦、PostgreSQL、SAP HANA、SQL Server、Oracle、StarRocks、DB2、MariaDB 和 ClickHouse；支持版本适配、影响行数估算、阶段进度、跳过原因、错误明细、租约恢复和并发隔离（[#260](https://github.com/ClouGence/open-cdm/issues/260)）。
- 数据库驱动支持在后端 `drivers.xml` 中声明默认版本，前端创建数据源时优先选择该默认版本（[#260](https://github.com/ClouGence/open-cdm/issues/260)）。
- 结果集文件下载前可自定义文件名，并校验空名称和非法字符（[#299](https://github.com/ClouGence/open-cdm/issues/299)）。
- 操作审计新增结果集导出和下载事件，记录文件与操作信息，并优化审计资源字段展示（[#302](https://github.com/ClouGence/open-cdm/issues/302)）。
- 工单列表支持按描述和内联 SQL 内容模糊搜索，同时展示可复制的工单描述（[#305](https://github.com/ClouGence/open-cdm/issues/305)）。
- Oracle 数据源新增客户端字符集配置，支持常用字符集或自定义值，并按配置解码 `CHAR` 和 `VARCHAR2` 数据（[#295](https://github.com/ClouGence/open-cdm/issues/295)）。

## 优化

- 优化 SQL 引擎架构，使用会话实际数据库版本和 `sql_mode` 等参数驱动解析、补全、拆分、行为与权限分析、列血缘和 SQL 改写；增强 MySQL 多版本语法、达梦安全域、Doris 与 StarRocks 建表元数据解析等能力（[#260](https://github.com/ClouGence/open-cdm/issues/260)）。
- 优化工单分析与执行详情，集中展示数据影响统计、对象和行为、分析错误、执行阶段与进度，并提升大 SQL 预览和任务恢复的稳定性（[#260](https://github.com/ClouGence/open-cdm/issues/260)）。
- 完善数据源配置、安全规则详情、SQL 日志和环境页面的国际化与布局；工单执行状态、操作按钮和调度日志会按用户语言展示（[#300](https://github.com/ClouGence/open-cdm/issues/300)、[#304](https://github.com/ClouGence/open-cdm/issues/304)）。

## 修复

- 修复 Docker 单机模式重建容器时未从持久化配置恢复内置 MySQL 账号的问题；现在会按目标数据库创建缺失账号或同步密码，由社区贡献者 [@sunjiajie](https://github.com/sunjiajie) 提交，感谢贡献（[#272](https://github.com/ClouGence/open-cdm/issues/272)）。
- 修复 ClickHouse `fetchSize` 兼容问题，并支持 JSON、Map、Tuple、Dynamic、Variant 和嵌套 Array 等类型读取，由社区贡献者 [@BetaCat0](https://github.com/BetaCat0) 提交，感谢贡献（[#253](https://github.com/ClouGence/open-cdm/issues/253)）。
- 修复 SQL 函数参数中包含非 `SELECT` 域时，列血缘和安全规则分析可能发生类型转换错误的问题，由社区贡献者 [@sunjiajie](https://github.com/sunjiajie) 提交，感谢贡献（[#259](https://github.com/ClouGence/open-cdm/issues/259)）。
- 修复同步 OpenAPI 查询未返回非 Console 展示模式的错误和警告，以及同一消息可能被重复收集的问题，由社区贡献者 [@sunjiajie](https://github.com/sunjiajie) 提交，感谢贡献（[#281](https://github.com/ClouGence/open-cdm/issues/281)）。
- 修复重复包装执行计划语句、`EXPLAIN` 行为分类不准确，以及 ClickHouse 旧版本连接、DB2 for z/OS 时区参数和 SQL Server `SHOWPLAN` 执行兼容性问题（[#260](https://github.com/ClouGence/open-cdm/issues/260)）。
- 修复 ClickHouse `EXISTS` 子查询、Oracle 多行 `INSERT ... VALUES` 和 PostgreSQL `INSERT ... SELECT` 等语法分析问题，并改进多数据库 `INSERT` 影响行数估算（[#260](https://github.com/ClouGence/open-cdm/issues/260)）。
