## 亮点

- 数据源连接能力全面升级，新增 SSH 隧道、代理和 SSL 证书管理。
- SSO、审批、安全规则和角色管理配置体验整体优化。
- CI/CD 工单流程进一步简化，支持 JDK 17 和 Docker 多架构打包。
- 移除初始化阶段的驱动下载流程，首次部署更简单。
- SQL 解析能力拆分为独立模块。

## 新增

- 新增 SSH 隧道管理，包括 SSH 配置页面、密码/私钥/代理/known_hosts 探测和连接测试（[#23](https://github.com/ClouGence/open-cdm/issues/23)）。
- 新增数据源安全连接配置，支持 SSL 证书和 HTTP、SOCKS4、SOCKS5 代理。
- 新增 `clouddm-sql` 模块体系，将 SQL 解析能力从数据源插件中独立出来。
- 新增 SSO 认证提供商和审批引擎独立配置页面，支持 LDAP、AD、OIDC、钉钉、飞书、微信、企业微信等集成统一管理。
- 新增 AD 与 LDAP 独立配置集，支持同时启用。
- 新增 SQL 审计日志保存天数设置入口。
- 新增 Docker 多架构打包支持。
- 新增集成配置文档入口，方便在 IM、Git、SSO、审批表单中查看对接说明。

## 优化

- 优化数据源配置模型、存储结构和创建/编辑流程，提升插件化配置能力。
- 优化安全规则工作流和角色管理 UI。
- 优化 数据源创建/编辑流程，统一为所有数据源提供测试连接能力（[#36](https://github.com/ClouGence/open-cdm/issues/36)）。
- 优化登录与认证体验，包括 OIDC 登录流程、登录页 provider 切换器和响应式布局。
- 优化前端资源包体积，减少入口包约 1MB。
- 优化 操作审计列表和导出，支持插件化导出格式、全量和限行导出、WebSocket 进度推送。
- 优化 CI/CD 工单流程页面和搜索体验。
- 优化 Docker 启动脚本和构建基础镜像。
- 优化前后端接口、Token Cookie 和认证/审批配置命名的一致性。

## 修复

- 修复一批数据源配置、驱动加载、内置驱动打包和连接配置问题。
- 修复 SQL 工作台查询加载、数据源树展开状态和数据库元数据初始化问题。
- 修复权限、角色、工单、审批调度和数据源创建者授权相关问题。
- 修复若干 SSO、OIDC 和审批配置页面的显示与交互问题。
- 修复 单机版修改服务端口后启动不生效的问题（[#87](https://github.com/ClouGence/open-cdm/issues/87)）。
- 修复 Oracle 11g 驱动无法下载的问题（[#35](https://github.com/ClouGence/open-cdm/issues/35)）。
- 修复 Oracle 11g 连接 ORA-01882 时区错误的问题（[#12](https://github.com/ClouGence/open-cdm/issues/12)）。
- 修复 Redis 数据源 Jedis 驱动适配器加载失败导致查询报错和连接失败的问题（[#78](https://github.com/ClouGence/open-cdm/issues/78)、[#92](https://github.com/ClouGence/open-cdm/issues/92)）。
- 修复 添加阿里云 ADB 数据源失败的问题（[#25](https://github.com/ClouGence/open-cdm/issues/25)）。
- 修复 SQL Server 2012 测试连接报错的问题（[#79](https://github.com/ClouGence/open-cdm/issues/79)）。
- 修复 添加数据源时驱动一直显示未就绪的问题（[#88](https://github.com/ClouGence/open-cdm/issues/88)）。
- 修复 读取配置 bug 和初始化升级向导 UI 问题（[#71](https://github.com/ClouGence/open-cdm/issues/71)）。
