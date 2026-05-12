<p align="center">
    <b>CloudDM</b>
    <br>
    A free, open-source database management tool built for teams. It provides access control, data masking, SQL auditing, CI/CD, and multi-region deployment.
</p>

<p align="center">
	<a href="https://www.cdmgr.com/"><b>Home</b></a> •
	<a href="https://www.cdmgr.com/docs/intro/product_intro"><b>Docs</b></a> •
    <a href="https://www.cdmgr.com/blog"><b>Blog</b></a> •
    <a href="https://gitee.com/clougence/CloudDM"><b>Gitee</b></a> •
    <a href="https://github.com/clougence/CloudDM"><b>GitHub</b></a>
</p>

<p align="center">
    [<a target="_blank" href='./README.cn.md'>中文</a>]
    [<a target="_blank" href='./README.en.md'>English</a>]
</p>

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

CloudDM supports **Standalone (Alone)** and **Cluster (Console + Sidecar)** modes, with install packages, Docker, and Kubernetes deployment options.

### Install Package

Extract and launch — the initialization wizard will guide you through configuration.

#### Standalone

```bash
tar -xzf cgdm-alone.tar.gz
cd cgdm-alone && bin/startup.sh
```

Open `http://localhost:8222` in a browser and follow the initialization wizard.

#### Cluster

```bash
# 1. Install Console
tar -xzf cgdm-console.tar.gz
cd cgdm-console && bin/startup.sh

# 2. Add machines in the console
# (omitted)

# 3. Install and configure Sidecar
tar -xzf cgdm-sidecar.tar.gz
cd ../cgdm-sidecar && bin/startup.sh
```

---

### Docker

One-click startup with Compose (current latest version `3.0.7` — replace with your version).

#### Standalone

```bash
# x86_64
docker compose -f docker-alone-x86_64-3.0.7.yml up -d

# arm64
docker compose -f docker-alone-arm64-3.0.7.yml up -d
```

#### Cluster

```bash
# x86_64
docker compose -f docker-cluster-x86_64-3.0.7.yml up -d

# arm64
docker compose -f docker-cluster-arm64-3.0.7.yml up -d
```

---

### Kubernetes

Ensure images are pushed to a registry, then apply directly (current latest version `3.0.7` — replace with your version).

#### Standalone

```bash
# x86_64
kubectl apply -f k8s-alone-x86_64-3.0.7.yml

# arm64
kubectl apply -f k8s-alone-arm64-3.0.7.yml
```

#### Cluster

```bash
# x86_64
kubectl apply -f k8s-cluster-x86_64-3.0.7.yml

# arm64
kubectl apply -f k8s-cluster-arm64-3.0.7.yml
```

---

### Access

After starting in any mode, open:

```
http://localhost:8222
```

The first visit launches the initialization wizard — complete database setup and admin account creation to get started.

## License

Licensed under the business-friendly [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html) license.

A formal LICENSE file is not yet present at the repository root; this README does not imply any default license assumption.
