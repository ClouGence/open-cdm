## 新增

- 新增 OpenSearch 2.x / 3.x 数据源，支持 TLS 连接、元数据浏览、REST DSL 查询与聚合、文档和索引管理、分页与导出，并接入权限、只读控制、审计和脱敏（[PR #448](https://github.com/ClouGence/open-cdm/pull/448)）。
- 新增 IoTDB 1.3.x Tree 和 2.x Tree / Table 模型数据源，支持元数据浏览、SQL 解析与补全、数据及对象操作、对象脚本，以及按完整时间序列路径或表授权和模型隔离（[PR #450](https://github.com/ClouGence/open-cdm/pull/450)）。
- 新增 PolarDB-X 用户与角色、会话与事务、系统管理、维护和执行计划语句的分类与行为分析支持（[PR #447](https://github.com/ClouGence/open-cdm/pull/447)）。
- 新增浅色、深色和跟随系统的外观模式，支持记住选择并同步编辑器、查询结果、表单和弹窗的主题（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 新增按数据源版本识别普通内置函数的权限豁免，调用普通内置函数不再额外要求程序权限；表访问、自定义函数、特权及不安全操作仍独立鉴权（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 新增内联 SQL 工单的“创建相似工单”操作，预填标题、描述、可访问的目标库与 Schema 及 SQL（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。

## 修复

- 修复 SSH 隧道覆盖原始数据源地址，导致连接测试成功后查询页元数据加载或后续连接失败的问题（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 修复 CockroachDB 等 PostgreSQL JDBC 会话已处于自动提交模式时，事务状态同步仍显式提交而报错的问题（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 修复 Oracle 普通账号因缺少 DBA 字典视图权限而无法读取列、注释、DBLink 和任务元数据的问题，并同步调整 YashanDB 的列与任务查询（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 修复权限申请、管理员授权和工单详情中的授权时间受服务端默认时区影响的问题，使用毫秒时间戳传递时间并保留历史工单日期的读取语义（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 修复 SQL Server 查询编辑器可补全表名却无法补全列名的问题（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 修复工单刷新时操作按钮跳动、相似工单入口短暂消失，以及查询按钮加载时尺寸变化的问题（[PR #439](https://github.com/ClouGence/open-cdm/pull/439)）。
- 修复事务隔离级别标签和异步查询事务消息未遵循所选界面语言的问题（[PR #447](https://github.com/ClouGence/open-cdm/pull/447)、[PR #448](https://github.com/ClouGence/open-cdm/pull/448)）。
- 修复 Elasticsearch、OpenSearch、Kafka 和 etcd 查询会话显示不适用的只读切换控件的问题（[PR #448](https://github.com/ClouGence/open-cdm/pull/448)）。
