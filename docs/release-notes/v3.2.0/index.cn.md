## 更新亮点

- 统一登录入口和账号模型，不再区分管理员身份和普通用户身份。
- 新增数据源语言服务能力，SQL 编辑器支持后端插件提供补全、分割和校验。
- 优化 Docker、初始化和项目目录结构，提升部署、升级和开源协作体验。

## 新增

- 新增 统一登录入口和账号模型，不再区分管理员身份和普通用户身份。
- 新增 在 MFA 开通时候可以复制 CODE 方便在没有扫码环境下添加 MFA。
- 新增 可以启用不同的 SSO 认证系统，并通过配置决定默认选项。
- 新增 可以为外部账号启用本地账号的功能。
- 新增 插件资源加载组件，支持 Web 侧读取插件静态资源。
- 新增 `DsLanguageSpi`，支持数据源插件提供编辑器 SQL 补全、分割和校验。
- 新增 操作审计导出进度事件和 WebSocket 推送。
- 新增 通用结果文件读写组件，供 Console、Sidecar 和文件转换复用。
- 新增 `AGENTS.md` 等协作说明文件。

## 优化

- 优化 登录页合并管理登录和账号登录入口。
- 优化 账号登录，支持账号、邮箱、手机号登录。
- 优化 重构全局资源权限实现，其并入已有授权体系。并可以设定授权时间。
- 优化 OIDC、LDAP、AD、钉钉、飞书、微信等外部登录链路。
- 优化 账号管理和个人资料编辑体验。
- 优化 MFA 开通、重置、关闭和登录失效流程。
- 优化 SQL 编辑器补全、语法校验和表权限语义校验。
- 优化 SQL 日志列表，补充数据源标识和备注展示。
- 优化 Docker 启动脚本、默认配置和 embedded MySQL 数据复用。
- 优化 项目源码目录，收敛为 `backend`、`frontend`、`package` 等结构。
- 优化 文档目录，将 README、DEPLOY、FAQ 和贡献说明归档到 docs。

## 修复

- 修复 外部认证首次登录补全流程重复要求补全的问题（[#58](https://github.com/ClouGence/open-cdm/issues/58)）。
- 修复 飞书 SSO 登录调用远程超时的问题（[#45](https://github.com/ClouGence/open-cdm/issues/45)）。
- 修复 初始化失败和初始化页面资源加载异常的问题（[#11](https://github.com/ClouGence/open-cdm/issues/11)、[#46](https://github.com/ClouGence/open-cdm/issues/46)）。
- 修复 初始化后登录定义可能早于插件就绪加载的问题，该问题导致页面 Logo 图片在首次加载失效。
- 修复 Docker 默认配置引导和 embedded MySQL 稳定性问题。
- 修复 初始化页面输入框焦点阴影和暗色模式展示异常的问题（[#4](https://github.com/ClouGence/open-cdm/issues/4)）。
- 修复 操作审计页面无法打开的问题（[#20](https://github.com/ClouGence/open-cdm/issues/20)、[#53](https://github.com/ClouGence/open-cdm/issues/53)）。
- 修复 工单内置流程报错的问题（[#52](https://github.com/ClouGence/open-cdm/issues/52)）。
- 修复 SQL 日志操作人和数据源展示不完整的问题。
- 修复 Kubernetes Worker Pod 名称引用不一致的问题。
