# CloudDM FAQ and Glossary

## FAQ

### What is CloudDM?

CloudDM is a free and open-source database management platform for teams. It provides database access control, SQL auditing, data masking, workflow approval, database CI/CD, and multi-region deployment.

### Is CloudDM open source?

Yes. CloudDM is released under the Apache License 2.0. See [LICENSE.txt](../../LICENSE.txt).

### What is the fastest way to try CloudDM?

Use the standalone Docker image and open `http://localhost:8222`:

```bash
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:3.0.7
```

### Which deployment mode should I use for evaluation?

Use Standalone mode, also called Alone. In this mode, the web console, sidecar, and metadata database run together.

### Which deployment mode should I use for multi-region database access?

Use Cluster mode, also called Console + Sidecar. It is designed for unified authorized access to databases across multiple regions.

### What databases does CloudDM support?

CloudDM supports MySQL, Oracle, MariaDB, PostgreSQL, IBM DB2, SQL Server, OceanBase, SAP Hana, StarRocks, Doris, SelectDB, ClickHouse, PolarDB, TiDB, Greenplum, Hologres, DM (Dameng), GaussDB, AnalyticDB MySQL, MaxCompute, Redis, MongoDB, and more.

### What does SQL auditing do?

SQL auditing checks SQL before execution or delivery. It can warn about or block risky statements through audit rules, security policies, and data masking.

### What workflow types does CloudDM support?

CloudDM supports SQL audit workflows, permission tickets, and change workflows.

### What triggers database CI/CD workflows?

CloudDM supports Git Push, Web Hook, and HttpCall triggers for database CI/CD workflows.

### Where are build and packaging outputs generated?

Build and packaging outputs are generated under `package/build`.

### What should I do if I forget the administrator account password?

You can reset the administrator account and password through the system initialization flow. Find the system MySQL driver directory under `drivers`, usually `drivers/cgdm-runtime-mysql`, delete that directory, and restart the application. The application will enter the new installation initialization flow because it cannot connect to the metadata database, and you can reset the administrator account and password there.

Important: do not choose to rebuild the database during initialization, otherwise existing metadata will be cleared.

### Which repository is canonical?

The canonical public source repository is [https://github.com/ClouGence/open-cdm](https://github.com/ClouGence/open-cdm). The Gitee repository is a mirror for China users.

## Glossary

| Term | Meaning |
|------|---------|
| Alone | Standalone deployment mode where Console, Sidecar, and metadata database run together. |
| Console | Central web console for database access, management, approval workflows, and configuration. |
| Sidecar | Component used with Console for database access, especially in multi-region deployment. |
| SQL Audit | Rule-based SQL risk checking before execution or change delivery. |
| Data Masking | Protection mechanism that hides or transforms sensitive data in query results or workflows. |
| Resource Permission | Permission granted at instance, database, schema, or table level depending on the statement type. |
| Function Permission | Role-based permission that controls access to product functions. |
| Permission Ticket | Workflow item used to request, approve, and grant access permissions. |
| Change Workflow | Workflow used to review and deliver controlled database changes. |
| Database CI/CD | Controlled database change delivery triggered by Git Push, Web Hook, or HttpCall. |
