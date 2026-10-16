## 新增

- 工单创建页的数据源选择器支持按环境分组与搜索；环境列表缺失或加载失败时，已加载的数据源仍可继续选择（[#293](https://github.com/ClouGence/open-cdm/issues/293)）。
- 在线查询结果缓存改为按容量管理，支持配置 MB 上限；超过上限后按最近访问时间从旧到新清理，设置为 `0` 时不缓存查询结果（[#301](https://github.com/ClouGence/open-cdm/issues/301)）。

## 修复

- 修复首次添加飞书或企业微信审批模板时，模板列表配置不存在会导致添加失败的问题（[#214](https://github.com/ClouGence/open-cdm/issues/214)）。
- 修复 GaussDB 等 PostgreSQL 协议数据源在手动提交模式下初始化会话时，活动事务可能导致隔离级别设置失败的问题（[#323](https://github.com/ClouGence/open-cdm/issues/323)）。
- 修复飞书 SSO 登录时未加载角色映射配置、导致默认角色不生效的问题（[PR #325](https://github.com/ClouGence/open-cdm/pull/325)）。
- 修复 SQL Server 扩展属性连接条件不完整导致元数据重复，以及插件驱动相关类未正确加载的问题（[#224](https://github.com/ClouGence/open-cdm/issues/224)、[#327](https://github.com/ClouGence/open-cdm/issues/327)）。
- 修复 SQL Server `TRUNCATE TABLE` 语句缺少拆分、行为分析和安全分析的问题（[#234](https://github.com/ClouGence/open-cdm/issues/234)）。
- 修复工单与 SQL 工作台的规则校验结果未在弹窗中正确展示的问题，并保留规则等级、名称、违规提示和行号信息（[#328](https://github.com/ClouGence/open-cdm/issues/328)）。
- 修复表对象右键菜单中的“申请权限”操作无法进入权限申请页的问题（[#329](https://github.com/ClouGence/open-cdm/issues/329)）。
- 修复权限申请工单描述错误包含申请人信息的问题（[#162](https://github.com/ClouGence/open-cdm/issues/162)）。
