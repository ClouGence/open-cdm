## 新增

- 新增 GreptimeDB 数据源，支持连接配置、元数据浏览、SQL 查询、DDL / DML 执行及查询条数限制（[PR #472](https://github.com/ClouGence/open-cdm/pull/472)）。
- 新增 Kafka 单条消息发送命令 `kafka-console-producer`，支持 Key、Value 和分区参数，并接入 Topic 发送权限、只读控制和审计（[PR #473](https://github.com/ClouGence/open-cdm/pull/473)）。
- 新增 Kafka 消费组参数 `--group` 和 `--consumer-property enable.auto.commit=true|false`，支持从已提交位点继续消费及按配置提交已输出消息的位点（[PR #473](https://github.com/ClouGence/open-cdm/pull/473)）。
- 新增 IoTDB Tree 模型的 `CREATE DEVICE TEMPLATE` 和 `SET DEVICE TEMPLATE` 语句解析与行为分类支持（[PR #478](https://github.com/ClouGence/open-cdm/pull/478)）。
- 新增数据源 IPv6 连接地址支持，覆盖直连地址和 SSH 隧道配置（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 新增数据源选择中的云部署类型与数据库类别筛选，便于查找自建及云托管数据库（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 新增管理员创建的本地账号首次登录强制修改初始密码流程，完成修改后才建立登录会话（[PR #456](https://github.com/ClouGence/open-cdm/pull/456)）。
- 新增持久化服务端登录会话，使用 Cookie 验证网页和 WebSocket，并在账号安全信息变更时撤销会话；取消独立操作密码验证，失效的 MFA 绑定可由管理员重置（[PR #463](https://github.com/ClouGence/open-cdm/pull/463)）。
- 新增查询结果单元格详情按内容识别 JSON，文本列中的有效 JSON 也可格式化展示（[PR #463](https://github.com/ClouGence/open-cdm/pull/463)）。

## 修复

- 修复 PostgreSQL SQL 审核无法解析 `DROP INDEX` 和带 `WHERE` 条件的 `CREATE INDEX` 语句的问题，由社区贡献者 [@congshenV](https://github.com/congshenV) 提交，感谢贡献（[PR #467](https://github.com/ClouGence/open-cdm/pull/467)）。
- 修复 IoTDB 元数据查询错误要求数据源归属账号或单对象权限的问题，按实例、模型或库的查询授权校验范围，并在 Tree 库下直接展示测点（[PR #458](https://github.com/ClouGence/open-cdm/pull/458)）。
- 修复 MaxCompute 参数化字段类型的元数据解析问题（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 修复 CI/CD 流程停用后已完成工单未能收尾的问题，恢复状态记录时不重复执行 SQL（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 修复在线查询和任务执行未使用数据源所配置查询超时的问题（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 修复审批流程确认阶段的展示容易被误认为逐阶段配置的问题，明确其为全局唯一确认阶段（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 修复深色模式下部分编辑器、表单、弹窗和查询结果单元格操作按钮的主题适配，并调整参数编辑器间距（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)、[PR #463](https://github.com/ClouGence/open-cdm/pull/463)）。
- 修复 YashanDB 中文数据源标签未使用品牌名称的问题（[PR #454](https://github.com/ClouGence/open-cdm/pull/454)）。
- 修复修改邮箱或手机号时已有 TOTP 绑定被清除的问题，保留现有 MFA 配置（[PR #463](https://github.com/ClouGence/open-cdm/pull/463)）。
- 修复受保护接口的路径匹配和初始化接口的访问限制，初始化接口仅在系统尚未完成初始化时开放（[PR #463](https://github.com/ClouGence/open-cdm/pull/463)）。
