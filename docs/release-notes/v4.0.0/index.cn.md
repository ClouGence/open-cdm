## 更新亮点

- 重构数据源配置模型，新增 SSH 隧道、代理（HTTP/SOCKS4/SOCKS5）和 SSL 证书管理能力。
- 将 SQL 解析引擎从数据源插件中拆分为独立的 `clouddm-sql` 模块体系。
- 优化安全规则和角色管理工作流，新增审批引擎独立配置页面。
- 重构 SSO 认证体系，AD 与 LDAP 拆分为独立配置，支持同时启用。
- 移除项目概念，简化 CI/CD 工单流程，支持 JDK 17。
- 移除初始化阶段的驱动下载流程，简化首次部署体验。

## 新增

- 新增 SSH 隧道管理，包括 `dm_ssh_config` 表、SSH 配置页面、密码/私钥/代理/known_hosts 探测和连接测试。
- 新增 数据源 SSL 证书配置，支持 CA 证书、客户端证书、客户端密钥、文件格式和密码。
- 新增 代理类型支持 HTTP、SOCKS4、SOCKS5。
- 新增 `clouddm-sql` 模块体系，将 SQL 解析引擎从数据源插件中拆分为独立模块（`sql-mysql`、`sql-oracle`、`sql-postgres`、`sql-db2`、`sql-doris`、`sql-sqlserver`、`sql-redis`、`sql-mongodb`、`sqlc-common`）。
- 新增 SSO 认证提供商独立配置页面，支持 LDAP、AD、OIDC、钉钉、飞书、微信的列表 + 表单页面管理。
- 新增 审批引擎独立配置页面，支持钉钉、飞书、企业微信审批的列表 + 表单管理，列表内联启用开关。
- 新增 AD 与 LDAP 独立配置集，支持同时启用（含 Flyway 升级迁移脚本）。
- 新增 SQL 审计日志保存天数设置入口。
- 新增 CI/CD GitHub Actions 工作流（ci.yml、cd.yml）。
- 新增 Docker 多架构打包支持。
- 新增 `CODEOWNERS` 和 `PULL_REQUEST_TEMPLATE` 协作文件。
- 新增 集成配置文档链接，在 IM/Git/SSO/审批表单中提供「如何对接」文档入口。

## 优化

- 优化 数据源配置模型重构，将 `DataSourceConfig`、`DataSourceType`、`ConfigDef` 等从 base 迁移到 plugin-sdk，统一使用 `DsConfigSpi` 定义插件侧配置。
- 优化 数据源创建/编辑流程，使用 `DmDsConfigUiPanelFactory` 和 `DmDsConfigUiDataFactory` 支持动态配置渲染（GENERAL、OPTIONS、SSH_SSL、ADVANCED、SHADOW 分组）。
- 优化 数据源配置存储，将 `dm_ds_config_kv_4rdp` 迁移到 `dm_ds_config_kv_4dm`，状态/集群绑定/环境字段并入 `dm_ds`。
- 优化 安全规则工作流和角色管理 UI。
- 优化 OIDC 登录流程端到端可用性，恢复登录页 provider 切换器，修复空 scope 导致 access_denied 的问题。
- 优化 前端资源包体积，减少入口包约 1MB。
- 优化 登录页响应式布局。
- 优化 操作审计列表和导出，支持插件化导出格式、全量和限行导出、WebSocket 进度推送。
- 优化 CI/CD 工单流程页面和搜索体验。
- 优化 前后端接口统一驼峰命名。
- 优化 Docker 启动脚本和构建基础镜像。
- 优化 Token Cookie 名称前缀统一。
- 优化 各认证/审批提供商 i18n 资源路径和 key 命名标准化。

## 修复

- 修复 数据源配置、驱动加载、内置驱动打包和连接配置问题（GaussDB、ClickHouse、MaxCompute、DB2、OceanBase 等）。
- 修复 SQL 工作台查询加载、数据源树展开状态、PG catalog 初始化和 SQL Server 证书信任配置问题。
- 修复 权限、角色、工单、审批调度和数据源创建者授权问题。
- 修复 SSO 配置删除时密码字段残留的问题。
- 修复 OIDC/SSO 回调失败时 errorMessage 为 null 导致二次 NPE 的问题。
- 修复 初始化服务器端口不一致的问题（[#91](https://github.com/ClouGence/open-cdm/issues/91)）。
- 修复 读取配置 bug 和初始化升级向导 UI 问题（[#71](https://github.com/ClouGence/open-cdm/issues/71)）。
- 修复 表格行 hover 时链接下划线撑高行高的问题（SSO/审批/集成列表）。
- 修复 危险操作确认按钮颜色未正确渲染为红色的问题。
- 修复 审批引擎列表内联开关切换后 UI 不同步的问题。
