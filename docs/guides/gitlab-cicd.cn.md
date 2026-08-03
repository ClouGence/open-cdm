# GitLab Self-Managed 数据库 CI/CD 集成

CloudDM 支持将私有部署的 GitLab 作为数据库 CI/CD 发布源。当前以 GitLab 19.2.x 作为验收基线，通过 REST API v4 读取
Project、Branch 和指定提交的仓库归档。

## 配置发布源

1. 在 GitLab 中为专用发布账号创建 Personal Access Token，最少授予 `read_api` scope 和目标 Project 读权限。
2. 在 CloudDM 的 **集成 > Git Ops** 中添加 **GitLab**。
3. 填写 GitLab Web 根地址，不要包含 `/api/v4`。支持
   HTTPS/HTTP、非标准端口和子路径，例如 `https://gitlab.example.com:8443/gitlab`。
4. 填写 Token 并运行连接测试。使用 HTTP 时必须显式确认 Token 明文传输风险。

GitLab 使用私有 CA 时，将 CA 导入 CloudDM Console 的 JVM trust store，例如通过 `-Djavax.net.ssl.trustStore=<path>` 和
`-Djavax.net.ssl.trustStorePassword=<password>` 配置。CloudDM 不支持跳过 TLS 校验。

## 配置发布流和 WebHook

1. 创建发布流，选择 GitLab Project 的数字 ID、分支、SQL 脚本目录和目标数据库。
2. 在发布流的 **触发配置 > WebHook** 中启用 WebHook，并复制 URL 和兼容密码。
3. 在 GitLab Project 的 **Settings > Webhooks** 中添加该 URL。
4. GitLab 19.1+ 建议使用 **Generate signing token**，将只显示一次的 `whsec_...` 值立即保存到 CloudDM 的 **Signing Token**
   。旧版 GitLab 可将 CloudDM 兼容密码填入 **Secret token**。
5. 根据发布流选择 Push events 或 Merge request events，保持标准 JSON payload，不使用自定义 WebHook 模板。

Push 发布流只处理目标分支的非删除提交；Merge Request 发布流只处理已合并到目标分支的事件。CloudDM 全程使用 WebHook 对应的不可变
commit SHA，并按 `webhook-id`/`Idempotency-Key` 及发布流 + commit SHA 去重。

## 安全与限制

- Access Token、Webhook 密码和 Signing Token 明文存储在 CloudDM 元数据库中，请严格限制元数据库访问权限；这些凭据不在查询响应、URL
  或日志中返回。
- 一次最多列出 10,000 个 Project 和 10,000 个 Branch。
- 仓库归档最大 1 GiB，解压结果最大 2 GiB，最多 10,000 个文件，单个 SQL 文件最大 50 MiB。
- SQL 必须为 UTF-8，允许 BOM 和 CRLF。
- 支持 Git LFS，不递归下载 Submodule，脚本路径不能位于 Submodule 中。

完整操作说明见 [CloudDM 官方文档](https://www.cdmgr.com/docs/integrations/devops/devops_cicd_gitlab)
和 [GitLab Webhooks 文档](https://docs.gitlab.com/user/project/integrations/webhooks/)。
