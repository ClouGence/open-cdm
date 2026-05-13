<h1 align="center">CloudDM</h1>

<p align="center">
  A free, open-source database management tool built for teams. It provides access control, data masking, SQL auditing, CI/CD, and multi-region deployment.
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

## Core Capabilities

### Data Query

- Broad data source support covering various databases
  - MySQL, Oracle, MariaDB, PostgreSQL, IBM DB2, SQL Server, OceanBase
  - SAP Hana, StarRocks, Doris, SelectDB, ClickHouse, PolarDB, TiDB, Greenplum
  - Hologres, DM (Dameng), GaussDB, AnalyticDB MySQL, MaxCompute, Redis, MongoDB
- Unified web console for database access with transaction, isolation level, and query plan support
- Query editor with syntax highlighting, intelligent suggestions, execution plans, and result export

### Database Management

- Supported database objects: catalog, schema, table, column, index, view, function, stored procedure, trigger, user, role, etc.
- Visual database object management: create, drop, alter, and inspect properties
- Manage data sources via environments and clusters

### Access Control

- **Resource** and **function** decoupled authorization model
    - Resource permissions can be granted at instance, database, schema, and table levels, depending on statement type
    - Function authorization uses role-based access control (RBAC) with role-to-user assignments
- Supports **permission requests**, **permission grants**, and **temporary permissions**

### Database CI/CD

- Three triggering modes: **Git Push**, **Web Hook**, and **HttpCall**
- Supports Gitee as the change repository

### SQL Auditing

- **Audit rules**, **security specifications**, and **data masking**
  - 54 built-in rules with custom rule scripting for extension
- Pre-execution SQL checks that warn or block risky execution

### Collaboration & Workflows

- Three workflow types: **SQL audit**, **permission tickets**, and **change processes**
- Three execution modes: **manual**, **immediate**, and **scheduled**
- Workflow engines: built-in, DingTalk, Feishu, WeCom
- Unified authentication / SSO: OpenLDAP / OpenID Connect (OIDC) / Windows AD / DingTalk / Feishu / WeCom

## Quick Start

CloudDM supports **Standalone (Alone)** and **Cluster (Console + Sidecar)** modes, with **install package**, **Docker**, and **Kubernetes** deployment options.

The quick start below uses standalone deployment as the shortest path to get started. If you need install-package deployment, cluster deployment, or Kubernetes deployment, you can continue with the generated packages and yml files after building locally. For full deployment details, see [DEPLOY.en.md](./DEPLOY.en.md).

```bash
# Quick start
docker run -d --name cgdm-alone \
  -p 8222:8222 \
  bladepipe/cgdm-alone:3.0.7

# Use Docker volumes
docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  bladepipe/cgdm-alone:3.0.7

# Mount to host directories
docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v /data/cgdm/conf:/root/cgdm/alone/conf \
  -v /data/cgdm/logs:/root/cgdm/alone/logs \
  -v /data/cgdm/data:/root/cgdm/alone/data \
  bladepipe/cgdm-alone:3.0.7

# China registry acceleration
# Replace bladepipe/cgdm-alone:3.0.7 with:
#    cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.0.7
```

### Other Deployment Options

In addition to the standalone Docker quick start above, CloudDM still supports:

- Install packages: `cgdm-alone.tar.gz`, `cgdm-console.tar.gz`, `cgdm-sidecar.tar.gz`
- Docker Compose: `docker-alone-*.yml`, `docker-cluster-*.yml`
- Kubernetes: `k8s-alone-*.yml`, `k8s-cluster-*.yml`
- Runtime modes: both Alone and Console + Sidecar cluster mode

If you build from source locally, these packages and yml files will be generated automatically in `open-cdm/package/build`.

### Access Product

Access the product in your browser after startup.

> On first access, the initialization wizard will launch. Complete database initialization and administrator account creation to get started.

```
http://localhost:8222
```

Add data source

![ds-add-cn.png](.assets/ds-add-cn.png)

Query data

![query-cn.png](.assets/query-cn.png)

## License

Licensed under the business-friendly [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html) license.

A formal LICENSE file is not yet present at the repository root; this README does not imply any default license assumption.
