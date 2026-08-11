## 亮点

- 大 SQL 工单全链路升级：支持上传 SQL 文件、分段预览和流式分析、审批、分发与执行，降低大文件处理时的内存和磁盘占用。
- 数据库 CI/CD 支持发布流程级联编排，可通过父子流程批量编排数据库变更，并跟踪传递、重试和执行状态。
- 扩展数据库支持，新增 Apache Cloudberry，完善达梦（DM8）SQL 能力，并补充 MongoDB Atlas、MongoDB SSL 和 Redis SSL 连接能力。
- 重构 SQL 分析与执行链路，统一 SQL 拆分、行为分析、权限校验、安全规则、审计和结果脱敏处理。
- 增强离线交付，安装包内置常用数据库驱动，并提供多架构离线镜像包和部署清单。

## 新增

- 新增 SQL 工单文件上传，支持 UTF-8 SQL 文件、可配置大小限制、按行分段预览，以及在线 SQL 与文件模式切换。
- 新增 CI/CD 发布流程级联编排，支持父子流程、批量编排、变更传递、失败重试和触发权限控制（[#55](https://github.com/ClouGence/open-cdm/issues/55)）。
- 新增私有化 GitLab 作为 CI/CD 发布源，支持 HTTP/HTTPS、非标准端口、子路径部署，以及 Push、Merge Request Webhook（[#169](https://github.com/ClouGence/open-cdm/issues/169)）。
- 新增 Apache Cloudberry 数据源，支持连接配置、元数据浏览、SQL 查询及 PostgreSQL 兼容的表结构管理（[#183](https://github.com/ClouGence/open-cdm/issues/183)）。
- 完善达梦（DM8）数据源支持，新增专用 SQL 引擎，覆盖 SQL 拆分、行为与权限分析、安全规则、系统函数和系统对象识别（[#95](https://github.com/ClouGence/open-cdm/issues/95)）。
- 新增 MongoDB Atlas SRV 连接模式（[#221](https://github.com/ClouGence/open-cdm/issues/221)）。
- 新增 MongoDB SSL 连接能力，由社区贡献者 [@BetaCat0](https://github.com/BetaCat0) 提交，感谢贡献（[#242](https://github.com/ClouGence/open-cdm/issues/242)）。
- 新增 Redis SSL 连接，支持 CA、TrustStore、KeyStore 和客户端证书等配置方式（[#220](https://github.com/ClouGence/open-cdm/issues/220)）。
- 新增账号资源权限批量授权和回收，可一次为多个账号配置相同资源权限（[#176](https://github.com/ClouGence/open-cdm/issues/176)、[#177](https://github.com/ClouGence/open-cdm/issues/177)）。
- 新增离线交付产物：Alone、Console、Sidecar 安装包内置 11 类常用数据库驱动，并提供 x86_64、arm64 离线镜像包及 Docker Compose、Kubernetes 部署清单。

## 优化

- 优化大 SQL 处理链路，审批预分析、安全规则检查、CI/CD SQL 处理、任务打包、Sidecar 下载和执行报告均采用流式处理，减少完整 SQL 和任务集合的重复加载。
- 优化 SQL 引擎架构，统一多数据库 SQL 拆分、行为和资源分析、执行授权及审计状态；MySQL 查询结果脱敏可基于列血缘识别来源字段。
- 优化工单和 CI/CD 详情，展示 SQL 识别、行为分析、安全规则检查及执行阶段的进度、统计、日志和失败原因。
- 优化偏好设置，将账号安全、CI/CD、数据查询和审批配置按页签组织，使用类型化控件、变更状态和服务端校验（[#228](https://github.com/ClouGence/open-cdm/issues/228)）。
- 优化 MFA 登录安全，使用数据库一次性挑战替代 MFA 前置 JWT，支持跨 Console 节点的原子重试次数限制、过期清理和一次性消费。
- 优化 SQL 工作台的数据源树、空结果页签和列宽交互；复制被截断单元格时会读取完整内容，并恢复 Monaco SQL 诊断信息展示（[#181](https://github.com/ClouGence/open-cdm/issues/181)）。
- 统一工单、安全规则、用户中心、偏好设置和管理日志的页面页签样式，优化日志表格滚动、SQL 编辑器字体及大 SQL 分段阅读体验。
- 优化 SQL 审计记录，在执行前创建审计记录并按稳定的查询标识更新运行状态，提升异步执行和 Sidecar 回报场景下的状态一致性。

## 修复

- 修复 ClickHouse 启动依赖冲突、MariaDB `INT UNSIGNED` 读取、MySQL `TINYINT(1)` 数值展示、PostgreSQL `CREATE TYPE` 执行和 Doris `BUCKETS AUTO` 表结构解析问题（[#216](https://github.com/ClouGence/open-cdm/issues/216)、[#207](https://github.com/ClouGence/open-cdm/issues/207)、[#204](https://github.com/ClouGence/open-cdm/issues/204)、[#180](https://github.com/ClouGence/open-cdm/issues/180)、[#179](https://github.com/ClouGence/open-cdm/issues/179)）。
- 修复不完整的非 MySQL 列血缘分析导致正常查询在执行前失败的问题，同时保留 SQL 行为分析、权限校验和安全规则检查。
- 修复初始化升级阶段加载安全规则插件时全局服务未注册，可能导致初始化失败的问题。
- 修复并发结果脱敏时原子类型缓存可能抛出 `ConcurrentModificationException` 的问题（[#235](https://github.com/ClouGence/open-cdm/issues/235)）。
- 修复 SQL 审计时间筛选、时区转换、总数分页、自定义列渲染和空用户名写入失败等问题（[#167](https://github.com/ClouGence/open-cdm/issues/167)、[#217](https://github.com/ClouGence/open-cdm/issues/217)、[#170](https://github.com/ClouGence/open-cdm/issues/170)、[#186](https://github.com/ClouGence/open-cdm/issues/186)）。
- 修复工单重复提交、执行状态不自动刷新，以及恢复的 SQL 页签无法输入的问题（[#201](https://github.com/ClouGence/open-cdm/issues/201)、[#174](https://github.com/ClouGence/open-cdm/issues/174)、[#146](https://github.com/ClouGence/open-cdm/issues/146)、[#178](https://github.com/ClouGence/open-cdm/issues/178)）。
- 修复大 SQL 工单详情未继续分段加载，以及追加内容后阅读位置跳动的问题。
- 修复表结构强制刷新仍读取旧缓存、全局查询安全规则未生效、安全规则手动修改参数不生效、规则详情布局错位、环境备注无法清空和 Doris 物化视图无法展开的问题（[#160](https://github.com/ClouGence/open-cdm/issues/160)、[#164](https://github.com/ClouGence/open-cdm/issues/164)、[#165](https://github.com/ClouGence/open-cdm/issues/165)、[#17](https://github.com/ClouGence/open-cdm/issues/17)）。
- 修复 PostgreSQL 临时 Schema 干扰对象浏览、数据源删除确认信息不直观，以及 MariaDB 查询和 SSL 连接兼容性问题（[#172](https://github.com/ClouGence/open-cdm/issues/172)、[#189](https://github.com/ClouGence/open-cdm/issues/189)、[#151](https://github.com/ClouGence/open-cdm/issues/151)、[#185](https://github.com/ClouGence/open-cdm/issues/185)）。
- 修复数据源保存后临时上传的证书附件未清理的问题（[#125](https://github.com/ClouGence/open-cdm/issues/125)）。
