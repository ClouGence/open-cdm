## Highlights

- Released the first open-source version of Open CDM, providing a unified web-based database access entry point for team-oriented database management.
- Supported both Alone standalone mode and Console + Sidecar cluster mode for individual evaluation, team trials, and production deployment.
- Provided core capabilities including database access, object management, permission control, SQL auditing, workflow collaboration, and database CI/CD.
- Supported delivery through installation packages, Docker, and Kubernetes.
- Supported multiple data sources, including MySQL, Oracle, PostgreSQL, SQL Server, OceanBase, ClickHouse, Doris, Redis, and MongoDB.

## Added

- Added a web query console for unified access to team database resources.
- Added SQL editing capabilities, including syntax highlighting, intelligent suggestions, execution plans, result viewing, and result export.
- Added database object management for databases, schemas, tables, columns, indexes, views, functions, stored procedures, triggers, users, roles, and more.
- Added environment, cluster, and data source management to organize database resources.
- Added an authorization model that separates resource permissions from functional permissions.
- Added multi-level resource authorization for instances, databases, schemas, tables, and more.
- Added role-based access control for roles, users, and permission points.
- Added permission requests, permission grants, and temporary permissions.
- Added SQL auditing with 54 built-in audit rules and script-based rule extensions.
- Added security policy management to warn about or block risky SQL before execution.
- Added data masking to reduce sensitive data exposure risks.
- Added SQL audit workflows, permission workflows, and change workflows.
- Added manual, immediate, and scheduled execution modes for work orders.
- Added a built-in workflow engine and integrations with DingTalk, Feishu, and WeCom.
- Added unified authentication and SSO support for OpenLDAP, OIDC, Windows AD, DingTalk, Feishu, and WeCom.
- Added database CI/CD triggers through Git Push, Web Hook, and HttpCall.
- Added Gitee as a database change repository.

## Improved

- Improved team database access by centralizing data source management, permission control, query operations, and auditing in the web console.
- Improved governance for high-risk database operations through SQL auditing, security policies, data masking, and workflow approvals.
- Improved team collaboration by unifying permission requests, SQL auditing, change execution, and approval workflows.
- Improved open-source collaboration with Chinese and English README files, deployment documents, contribution guides, and license documentation.
- Improved delivery structure by standardizing source builds, installation packages, Docker images, and Kubernetes manifests.

## Delivery

- Supports Alone standalone deployment.
- Supports Console + Sidecar cluster deployment.
- Supports tgz installation packages.
- Supports Docker image delivery.
- Supports Docker Compose manifests.
- Supports Kubernetes manifests.
- Build artifacts are generated under `open-cdm/package/build`.

## Open Source

- License: Apache License 2.0
- Homepage: https://www.cdmgr.com/
- Documentation: https://www.cdmgr.com/docs/intro/product_intro
- Blog: https://www.cdmgr.com/blog
- GitHub: https://github.com/ClouGence/open-cdm
- Gitee: https://gitee.com/clougence/open-cdm
