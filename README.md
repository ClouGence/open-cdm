<h1 align="center">CloudDM</h1>

<p align="center">
  A free and open-source database management tool designed for team use. It provides access control, data masking, SQL auditing, CI/CD, and cross-region deployment capabilities.
</p>

<p align="center">
	<a href="https://www.cdmgr.com/"><b>Home</b></a> •
	<a href="https://www.cdmgr.com/docs/intro/product_intro"><b>Docs</b></a> •
    <a href="https://www.cdmgr.com/blog"><b>Blog</b></a> •
  <a href="https://gitee.com/clougence/open-cdm"><b>Gitee</b></a> •
  <a href="https://github.com/ClouGence/open-cdm"><b>GitHub</b></a>
</p>

<p align="center">
    [<a target="_blank" href='./README.cn.md'>中文</a>]
    [<a target="_blank" href='./README.en.md'>English</a>]
</p>

![pic_en.png](.assets/pic_en.png)

---

## Project Facts

| Field | Value |
|------|-------|
| Project name | CloudDM |
| Repository | https://github.com/ClouGence/open-cdm |
| Mirror | https://gitee.com/clougence/open-cdm |
| Homepage | https://www.cdmgr.com/ |
| Documentation | https://www.cdmgr.com/docs/intro/product_intro |
| License | Apache License 2.0 |
| Current version | 3.0.7 |
| Main languages | Java, JavaScript / TypeScript |
| Deployment modes | Standalone (Alone), Cluster (Console + Sidecar) |
| Deployment targets | Install package, Docker, Kubernetes |

## Core Capabilities

### Data Query

- Rich data source support covering many database types
  - MySQL, Oracle, MariaDB, PostgreSQL, IBM DB2, SQL Server, OceanBase
  - SAP Hana, StarRocks, Doris, SelectDB, ClickHouse, PolarDB, TiDB, Greenplum
  - Hologres, DM (Dameng), GaussDB, AnalyticDB MySQL, MaxCompute, Redis, MongoDB
- Unified web console access to databases, with support for transactions, isolation levels, and execution plans
- Query editor, syntax highlighting, intelligent suggestions, execution plans, and result export

### Database Management

- Supported database objects include databases, schemas, tables, columns, indexes, views, functions, stored procedures, triggers, users, roles, and more
- Visual management of database objects such as create, delete, modify, and inspect properties
- Management of different data sources through environments and clusters

### Access Control

- Authorization model that separates **resources** and **functions**
  - Resource permissions can be granted at the instance, database, schema, and table levels, depending on the statement type
  - Function authorization uses role-based access control (RBAC) by granting roles to users
- Supports **permission requests**, **permission grants**, and **temporary permissions**

### Database CI/CD

- Provides three ways to trigger CI/CD workflows: **Git Push**, **Web Hook**, and **HttpCall**
- Supports Gitee as the change repository

### SQL Auditing

- Supports **audit rules**, **security policies**, and **data masking**
  - Includes 54 built-in rules and supports custom extensions through rule scripts
- Supports SQL pre-checks before execution to warn about or block risky statements

### Collaboration and Workflow

- Supports three workflow types: **SQL audit**, **permission tickets**, and **change workflows**
- Supports **manual execution**, **immediate execution**, and **scheduled execution** for work orders
- Workflow engines: built-in, DingTalk, Feishu, WeCom
- Unified authentication / SSO: OpenLDAP / OpenID Connect (OIDC) / Windows AD / DingTalk / Feishu / WeCom

## Quick Start

### Install
CloudDM supports **Standalone (Alone)** and **Cluster (Console + Sidecar)** modes, and also supports **install packages**, **Docker**, and **Kubernetes** deployment methods.

The example below demonstrates how to use standalone deployment. If you need install-package deployment, cluster deployment, or Kubernetes deployment, you can continue deploying with the install packages and yml files generated after local packaging. For complete deployment instructions, see [DEPLOY.en.md](./DEPLOY.en.md).

```bash
# Quick start
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:3.0.7

# Faster image pulls in China
docker run -d --name cgdm-alone -p 8222:8222 \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.0.7
```

### Initialization

Access the product in your browser:

```
http://localhost:8222
```

> On first access, the initialization wizard will open.
>
> If you not change the account, the default account is **admin@cdmgr.com**

### Add Data Source

<img src=".assets/ds_add_en.png" alt="ds_add_en.png" style="border: 1px solid #d9d9d9;" />

### Query Data

<img src=".assets/query_en.png" alt="query_en.png" style="border: 1px solid #d9d9d9;" />

## Open Source License

CloudDM is released under the business-friendly [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html). See [LICENSE.txt](./LICENSE.txt) for details.
