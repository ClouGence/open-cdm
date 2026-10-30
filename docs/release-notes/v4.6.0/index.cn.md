## 新增

- 新增可按环境绑定的团队多级内置审批流程，覆盖 SQL、变更和权限工单，支持指定审批团队、各环节通过人数及执行参与人，并保留逐人审批结果、时间和意见（[PR #433](https://github.com/ClouGence/open-cdm/pull/433)）。
- 新增 etcd 键值数据源，支持多端点连接、账号认证、TLS、`get` / `put` / `del`、实例级键浏览及只读会话，并支持不含 Catalog / Schema 层级的工单处理，由社区贡献者 [@48N6E](https://github.com/48N6E) 提交，感谢贡献（[PR #416](https://github.com/ClouGence/open-cdm/pull/416)）。
- 新增查询结果转置视图，可按字段对比当前页记录，各结果页签独立切换；支持复制、单元格详情及截断值续取，导出保持原始方向，流式结果不提供转置（[PR #429](https://github.com/ClouGence/open-cdm/pull/429)）。
- 新增 CockroachDB DCL 分类与权限分析，支持对象授权撤销、角色成员管理、默认权限及用户和角色管理（[PR #364](https://github.com/ClouGence/open-cdm/pull/364)）。
- 新增 SAP HANA 管理语句解析与行为分析支持，以 HANA Platform 2.0 SPS 08 为基线覆盖会话连接、安全权限、系统配置与负载、数据库及备份恢复、执行计划与诊断等管理场景，保留现有 `SET SCHEMA` 限制（[PR #431](https://github.com/ClouGence/open-cdm/pull/431)）。
- 新增 Hologres 权限管理过程的 DCL 分类与权限分析，识别 SPM / SLPM 成员管理、授权撤销、权限复制和 Schema 重命名，并区分 Schema 级及默认权限范围（[PR #436](https://github.com/ClouGence/open-cdm/pull/436)）。
- 新增 StarRocks `SHOW CREATE TABLE`、`SHOW CREATE VIEW` 和 `SHOW CREATE MATERIALIZED VIEW` 的元数据语句分类及行为分析支持（[PR #431](https://github.com/ClouGence/open-cdm/pull/431)）。
- 新增 SQL 收藏片段的光标插入与选区替换，支持撤销重做；保存时可明确选择新建或覆盖已有收藏，每个查询页签独立记住最近成功插入的收藏并在刷新后恢复（[PR #424](https://github.com/ClouGence/open-cdm/pull/424)）。

## 修复

- 修复非本机 HTTP 部署下 AI 消息、回复和 SQL 文本复制失败，以及部分浏览器缺少剪贴板 API 时编辑器初始化受影响的问题（[PR #424](https://github.com/ClouGence/open-cdm/pull/424)）。
- 修复 SQL 编辑器折叠箭头因图标字体被覆盖而显示为空心方框的问题（[PR #419](https://github.com/ClouGence/open-cdm/pull/419)）。
- 修复提示消息关闭按钮位于容器外、容易被遮挡的问题（[PR #424](https://github.com/ClouGence/open-cdm/pull/424)）。
- 修复测试某个数据源连接时，其他实例的测试按钮也被禁用的问题（[PR #433](https://github.com/ClouGence/open-cdm/pull/433)）。
- 修复 JDBC 事务提交或回滚失败未反馈给查询端、查询无法收到结束消息的问题（[PR #364](https://github.com/ClouGence/open-cdm/pull/364)）。
- 修复 PostgreSQL 系列查询在 HAVING 后带外层 ORDER BY 时，自动分页将 LIMIT 插入错误位置的问题（[PR #364](https://github.com/ClouGence/open-cdm/pull/364)）。
- 修复支持 JDBC 4.2 时间转换的驱动读取 TIME 值时丢失小数秒精度的问题（[PR #431](https://github.com/ClouGence/open-cdm/pull/431)）。
- 修复 StarRocks BINARY / VARBINARY 结果因驱动上报为 BLOB 或 TEXT 类型而读取和展示异常的问题（[PR #431](https://github.com/ClouGence/open-cdm/pull/431)）。
- 修复 Oracle 列定义中的 `DEFAULT ON NULL` 无法解析的问题，以及对象树中唯一约束与其支撑索引同名时重复显示索引节点的问题（[PR #431](https://github.com/ClouGence/open-cdm/pull/431)）。
- 修复解除环境审批绑定时，仍被其他环境或在途工单使用的模板可能被错误解除使用的问题（[PR #433](https://github.com/ClouGence/open-cdm/pull/433)）。
- 修复单机模式重启时未触发内置 Worker 的中断任务恢复处理的问题（[PR #433](https://github.com/ClouGence/open-cdm/pull/433)）。
- 修复仅有安全规则查看权限的账号仍显示规范创建、编辑入口的问题（[PR #433](https://github.com/ClouGence/open-cdm/pull/433)）。
