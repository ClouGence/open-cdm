## 新增

- 新增 Kafka 数据源支持，提供 Topic 浏览与管理、消息消费、命令补全和模板，支持 TLS/SASL 认证、权限控制与审计（[PR #390](https://github.com/ClouGence/open-cdm/pull/390)）。
- 新增 SAP HANA 数据源支持，支持 TLS/SSH 连接、对象浏览、SQL/SQLScript、结构与数据编辑、对象脚本和 SQL 导出，并接入权限校验、SQL 审核、血缘分析与脱敏（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。
- 新增 SQL 工作台全文格式化，支持 HANA、MySQL、Oracle、PostgreSQL 等方言（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。

## 修复

- 修复 Oracle JSON 列识别和读取错误（[PR #377](https://github.com/ClouGence/open-cdm/pull/377)）。
- 修复 Console + Sidecar 部署下 SQL 变更工单无法执行的问题，由社区贡献者 [@theluckystrike](https://github.com/theluckystrike) 提交，感谢贡献（[PR #384](https://github.com/ClouGence/open-cdm/pull/384)）。
- 修复拥有全部数据源权限的子账号执行工单时被错误拒绝的问题，由社区贡献者 [@vantis-zh](https://github.com/vantis-zh) 提交，感谢贡献（[PR #359](https://github.com/ClouGence/open-cdm/pull/359)）。
- 修复环境名称筛选不生效的问题，由社区贡献者 [@fengruiwd](https://github.com/fengruiwd) 提交，感谢贡献（[PR #335](https://github.com/ClouGence/open-cdm/pull/335)）。
- 修复工单详情处理人姓名重叠的问题，支持查看完整名单（[PR #380](https://github.com/ClouGence/open-cdm/pull/380)）。
- 修复数据编辑器默认值、显式 NULL 和全默认值行的处理错误，避免提交只读列（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。
- 修复数据编辑保存失败后未显示错误、仍继续保存后续行的问题（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。
- 修复查询结果长文本和二进制复制不完整、流式行定位错误及二进制 NULL 显示错误（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。
- 修复 SAP HANA 执行计划节点、行数估计和来源分类错误（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。
- 修复英文界面 SQL 语法诊断和执行耗时提示仍显示中文的问题（[PR #381](https://github.com/ClouGence/open-cdm/pull/381)）。
