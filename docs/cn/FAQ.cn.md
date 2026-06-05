# CloudDM 常见问题与术语表

## 常见问题

### CloudDM 是什么？

CloudDM 是一款免费且开源的团队化数据库管理平台，提供数据库访问控制、SQL 审核、数据脱敏、流程审批、数据库 CI/CD 和跨地区部署能力。

### CloudDM 是开源项目吗？

是。CloudDM 使用 Apache License 2.0 许可协议，详见 [LICENSE.txt](../../LICENSE.txt)。

### 如何最快体验 CloudDM？

使用单机模式 Docker 镜像，并访问 `http://localhost:8222`：

```bash
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:3.0.7
```

### 评估或试用时应该选择哪种部署模式？

建议使用单机模式，也称为 Alone 模式。在该模式下，Web 控制台、Sidecar 和元信息数据库会一起运行。

### 多地区数据库统一访问应该选择哪种部署模式？

建议使用集群模式，也称为 Console + Sidecar 模式。该模式适合跨地区数据库的统一授权访问。

### CloudDM 支持哪些数据库？

CloudDM 支持 MySQL、Oracle、MariaDB、PostgreSQL、IBM DB2、SQL Server、OceanBase、SAP Hana、StarRocks、Doris、SelectDB、ClickHouse、PolarDB、TiDB、Greenplum、Hologres、达梦、高斯数据库、AnalyticDB MySQL、MaxCompute、Redis、MongoDB 等数据源。

### SQL 审核有什么作用？

SQL 审核会在 SQL 执行或变更交付前进行风险检查，并通过审核规则、安全规范和数据脱敏能力提示风险或阻断高风险语句。

### CloudDM 支持哪些流程类型？

CloudDM 支持 SQL 审核、权限工单和变更流程三种流程。

### 数据库 CI/CD 支持哪些触发方式？

CloudDM 支持通过 Git Push、Web Hook 和 HttpCall 触发数据库 CI/CD 流程。

### 构建和打包产物在哪里生成？

构建和打包产物生成在 `package/build` 目录下。

### 忘记管理账号密码怎么办？

可以利用系统初始化安装机制重置管理员账号和密码。先找到 `drivers` 目录下的系统 MySQL 驱动目录 `drivers/cgdm-runtime-mysql`，删除该目录后重启应用。应用会因为无法连接元信息数据库而进入新安装初始化流程，在初始化过程中可以重新设置管理员账号和密码。

注意：初始化时不要选择重建数据库，否则会清理已有元信息数据。

### 哪个仓库是官方主仓库？

官方公开源码主仓库是 [https://github.com/ClouGence/open-cdm](https://github.com/ClouGence/open-cdm)。Gitee 仓库是面向中国用户的镜像仓库。

## 术语表

| 术语 | 含义 |
|------|------|
| Alone | 单机部署模式，Console、Sidecar 和元信息数据库一起运行。 |
| Console | 中央 Web 控制台，用于数据库访问、管理、审批流程和配置。 |
| Sidecar | 与 Console 配合使用的数据库访问组件，常用于跨地区部署。 |
| SQL 审核 | 在 SQL 执行或变更交付前进行的规则化风险检查。 |
| 数据脱敏 | 对查询结果或流程中的敏感数据进行隐藏或转换的保护机制。 |
| 资源权限 | 按实例、数据库、Schema 或表等资源粒度授予的权限，具体粒度取决于语句类型。 |
| 功能权限 | 基于角色访问控制（RBAC）的功能访问权限。 |
| 权限工单 | 用于申请、审批和授予访问权限的流程工单。 |
| 变更流程 | 用于审核和交付受控数据库变更的流程。 |
| 数据库 CI/CD | 通过 Git Push、Web Hook 或 HttpCall 触发的受控数据库变更交付流程。 |
