## 更新亮点

- 首次发布 Open CDM 开源版本，面向团队化数据库管理场景提供统一 Web 数据库访问入口。
- 支持 Alone 单机模式和 Console + Sidecar 集群模式，覆盖个人体验、团队试用和正式环境部署。
- 提供数据库访问、对象管理、权限控制、SQL 审核、流程协同、数据库 CI/CD 等核心能力。
- 支持安装包、Docker、Kubernetes 多种交付方式。
- 支持 MySQL、Oracle、PostgreSQL、SQL Server、OceanBase、ClickHouse、Doris、Redis、MongoDB 等多类数据源。

## 新增

- 新增 Web 查询控制台，支持统一访问团队内数据库资源。
- 新增 SQL 编辑能力，支持语法高亮、智能提示、执行计划、结果集查看和结果导出。
- 新增 数据库对象管理能力，支持数据库、Schema、表、列、索引、视图、函数、存储过程、触发器、用户、角色等对象的查看和管理。
- 新增 环境、集群、数据源管理能力，可按环境和集群组织不同数据库资源。
- 新增 资源权限和功能权限分离的授权模型，支持实例、数据库、Schema、表等多层级资源授权。
- 新增 基于角色的访问控制能力，支持角色、用户和权限点管理。
- 新增 权限申请、权限赋予和临时权限能力，支持团队内按需授权。
- 新增 SQL 审核能力，内置 54 条审核规则，并支持规则脚本扩展。
- 新增 安全规范能力，可组合审核规则并在 SQL 执行前进行提示或阻断。
- 新增 数据脱敏能力，用于降低敏感数据查询和展示风险。
- 新增 SQL 审核、权限工单、变更流程三类流程能力。
- 新增 工单手动执行、立即执行、定时执行三种执行方式。
- 新增 内置流程引擎，并支持钉钉、飞书、企业微信等流程集成。
- 新增 OpenLDAP、OIDC、Windows AD、钉钉、飞书、企业微信等统一认证和 SSO 能力。
- 新增 数据库 CI/CD 能力，支持 Git Push、Web Hook、HttpCall 三种触发方式。
- 新增 Gitee 作为数据库变更仓库。

## 优化

- 优化 团队数据库访问方式，通过 Web 控制台集中管理数据源、权限、查询和审计。
- 优化 数据库高风险操作治理，通过 SQL 审核、安全规范、数据脱敏和工单流程降低误操作影响。
- 优化 团队协作流程，将权限申请、SQL 审核、变更执行和流程审批纳入统一入口。
- 优化 开源协作体验，提供中文和英文 README、部署文档、贡献指南和开源协议说明。
- 优化 交付结构，统一源码构建、安装包、Docker 镜像和 Kubernetes 清单的产物输出。

## 交付

- 支持 Alone 单机部署模式。
- 支持 Console + Sidecar 集群部署模式。
- 支持 tgz 安装包交付。
- 支持 Docker 镜像交付。
- 支持 Docker Compose 清单交付。
- 支持 Kubernetes 清单交付。
- 构建输出目录为 `open-cdm/package/build`。

## 开源信息

- 开源协议: Apache License 2.0
- 项目主页: https://www.cdmgr.com/
- 产品文档: https://www.cdmgr.com/docs/intro/product_intro
- 项目博客: https://www.cdmgr.com/blog
- GitHub: https://github.com/ClouGence/open-cdm
- Gitee: https://gitee.com/clougence/open-cdm
