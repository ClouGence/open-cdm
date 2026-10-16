## 修复

- 修复 SSL 文件延迟加载时过早校验配置的问题，确保 CA 证书、TrustStore、KeyStore 和客户端证书在加载完成后统一校验（[#278](https://github.com/ClouGence/open-cdm/issues/278)、[#311](https://github.com/ClouGence/open-cdm/issues/311)）。
- 修复服务重启后 WebSocket 无法持续重连的问题，使 SQL 工作区可在后端恢复后自动重新连接；鉴权失败时仍会正确停止重连（[#303](https://github.com/ClouGence/open-cdm/issues/303)）。
- 修复表编辑器生成 DDL 时丢失列默认值选项、达梦列默认值无法正常编辑以及限定标识符重复转义的问题（[#313](https://github.com/ClouGence/open-cdm/issues/313)、[#314](https://github.com/ClouGence/open-cdm/issues/314)）。
- 移除已失效的消息中心入口、旧 Console Job 页面及轮询逻辑，避免展示无法使用的功能，并保留现有异步任务 Dock 行为（[#315](https://github.com/ClouGence/open-cdm/issues/315)）。
- 修复单机模式初始化任务缺少必要平台服务、导致已有配置场景无法完成启动恢复的问题（[#317](https://github.com/ClouGence/open-cdm/issues/317)）。
