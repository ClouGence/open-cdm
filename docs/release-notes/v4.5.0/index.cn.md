## 新增

- 新增 AI SQL 助手，可基于已授权的数据库元数据通过自然语言生成 SQL，支持多轮追问、会话历史与分享、流式输出、取消和刷新后恢复，生成的 SQL 可插入查询编辑器后手动执行（[PR #405](https://github.com/ClouGence/open-cdm/pull/405)）。
- 新增管理员统一配置 AI 引擎，支持 OpenAI 兼容接口、DeepSeek、Kimi、MiniMax 和 DashScope，提供连接测试与凭据加密存储（[PR #405](https://github.com/ClouGence/open-cdm/pull/405)）。
- 新增 Elasticsearch 数据源支持，适配 7.10+、8.x 和 9.x，支持精确索引上的文档查询与写入、聚合、索引及 Mapping/Settings 管理、Console 风格请求编辑，并接入权限控制、审计、脱敏和结果导出；7.12+ 支持符合条件的文档查询自动分页（[PR #391](https://github.com/ClouGence/open-cdm/pull/391)）。
- 新增崖山数据库（YashanDB）数据源支持，分别提供 Oracle 和 MySQL 兼容模式的连接配置、对象浏览、SQL 查询与结构管理，由社区贡献者 [@ComingWen](https://github.com/ComingWen) 提交，感谢贡献（[PR #388](https://github.com/ClouGence/open-cdm/pull/388)）。
- 新增 SQL 工作台打开、断开和重新连接操作，保留查询页签、SQL 草稿与结果，断开时中断查询并回滚未提交的手动事务，恢复连接后不自动重放 SQL（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
- 新增 SQL 工作台分区加载骨架屏，减少首次加载空态闪现，并在刷新期间保留已有数据源和对象列表（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
- 新增 SQL 工作台无数据源权限时的申请入口及联系管理员指引（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
- 新增数据源实例列表的环境列，支持长名称提示与横向滚动（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。

## 修复

- 修复自动执行工单未使用所选数据库和 Schema 建立连接，导致 PostgreSQL 等数据源错误访问默认库的问题，由社区贡献者 [@sunjiajie](https://github.com/sunjiajie) 提交，感谢贡献（[PR #279](https://github.com/ClouGence/open-cdm/pull/279)）。
- 修复账号显示名称为空时，工单列表和详情无法识别申请人的问题，改为显示唯一登录账号（[PR #407](https://github.com/ClouGence/open-cdm/pull/407)）。
- 修复 Docker 部署日志轮转后容器不再继续输出应用日志的问题（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
- 修复包含函数唯一索引的 MySQL 表详情加载失败，以及索引访问方法影响 SQL 审核索引类型判断的问题（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
- 修复自动执行工单事务回滚异常导致任务无法正确报告失败，以及不必要的自动提交恢复导致终态上报中断的问题（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
- 修复自动执行工单打包失败时缺少调度错误日志和具体失败原因的问题（[PR #398](https://github.com/ClouGence/open-cdm/pull/398)）。
