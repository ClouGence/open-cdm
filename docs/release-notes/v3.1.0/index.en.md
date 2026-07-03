---
id: open_cdm_3_1_0_en
title: Open CDM 3.1.0
sidebar_label: v3.1.0
description: Improves initialization and upgrade, login and SSO, driver preparation, and Docker packaging
date: 2026-05-30
tags: [Version Update, Initialization, Upgrade, Login, SSO, Docker, Drivers]
---

- Release date: May 30, 2026
- Version: v3.1.0

## Product Overview

CloudDM is a free and open-source database management tool for teams, providing unified web database access, permission control, data masking, SQL auditing, workflow collaboration, and database CI/CD capabilities.

## Quick Start

```bash
# Default image
docker run -d --name cgdm-alone -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  bladepipe/cgdm-alone:3.1.0

# China acceleration image
docker run -d --name cgdm-alone -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.1.0
```

Open the following URL after startup:

```text
http://localhost:8222
```

On first access after a fresh deployment, the initialization wizard will open; during an upgrade, the upgrade wizard will open.

## Highlights

- Improved initialization and upgrade experience with runtime MySQL driver preparation, download progress, and clearer failure details.
- Improved login and unified authentication by separating sub-account login from admin login and refining SSO, OIDC, LDAP, and AD flows.
- Improved driver download and driver isolation to reduce troubleshooting cost for missing drivers, failed downloads, and driver-version conflicts.
- Fixed Oracle 23ai data-source compatibility and added an option to filter Oracle-maintained schemas.
- Built a DAO layer and standardized product module naming to reduce direct mapper injection and unify data access boundaries.
- Improved Docker packaging and documentation with persisted volumes, quick upgrade commands, cross-platform builds, and mirror options.

## Added

- Added initialization-time MySQL driver preparation so the runtime MySQL driver is checked before initialization continues.
- Added initialization MySQL driver progress UI and WebSocket progress updates.
- Added in-page completion for first-time SSO login, allowing users to complete phone and email information before continuing login.
- Added the login page `defaultLogin=manage` parameter for direct admin-login access.
- Added frontend route permission metadata so entries such as roles, data sources, and preferences can be guarded at URL level.
- Added the Oracle `excludeOraMaintainedSchemas` option to control whether Oracle-maintained schemas are filtered ([#14](https://github.com/ClouGence/open-cdm/issues/14)).
- Added `all_build.sh plugin <module>` for single-plugin builds.
- Added Docker build `--mirrors` support for using built-in Ubuntu mirrors during image builds.
- Added FAQ, README, and DEPLOY documentation updates for quick start, quick upgrade, and deployment guidance.

## Improved

- Improved login page defaults and labels: sub-account login is the default, and admin accounts use a dedicated admin-login entry.
- Improved SSO / OIDC callback handling so failure reasons are shown on the login page and first-time completion can identify the correct primary account.
- Improved LDAP / AD login by using native JNDI contexts for search and authentication, with special-character escaping.
- Improved LDAP / AD error messages for account-not-found, wrong-password, disabled, locked, expired, password-reset-required, and multiple-match scenarios.
- Improved initialization failure display by automatically expanding and scrolling to failed scripts or global error details.
- Improved driver download failure messages by prioritizing root causes and Maven transfer context.
- Improved normal data-source driver download progress so the frontend can show current file, progress, summary, and details per user.
- Improved PG and other data-source driver isolation and lifecycle handling to reduce class-loading conflicts across driver versions.
- Improved permission service boundaries by consolidating resource permission checks, role permission checks, and data-source permission filtering.
- Improved Alone, Console, and Sidecar shutdown by disabling Spring's default shutdown hook and using a shared shutdown wait mechanism.
- Improved product module naming across authentication, approval, system, data source, execution, and monitoring domains.
- Improved the DAO layer by consolidating mapper, DO, enum, and type-handler access into a unified data access module.
- Improved Docker quick-start and quick-upgrade examples with config, log, application data, and embedded MySQL volumes.
- Improved Docker build scripts to use buildx for x86_64 / arm64 builds and support built-in mirror parameters.

## Fixed

- Fixed standalone Docker restart after initialization, or upgrades that reuse embedded MySQL data volumes, failing with `ERROR 1045 (28000)` because the startup script retried root access without a password ([#21](https://github.com/ClouGence/open-cdm/issues/21)).
- Fixed MySQL driver selection being bypassed by internal dependencies when the driver had not been downloaded ([#5](https://github.com/ClouGence/open-cdm/issues/5)).
- Fixed PostgreSQL driver isolation errors that could cause `NoClassDefFoundError` or `ClassCastException` during configuration or query execution ([#29](https://github.com/ClouGence/open-cdm/issues/29), [#31](https://github.com/ClouGence/open-cdm/issues/31), [#41](https://github.com/ClouGence/open-cdm/issues/41)).
- Fixed empty query result export potentially causing a division-by-zero `ArithmeticException` ([#40](https://github.com/ClouGence/open-cdm/issues/40)).
- Fixed login URLs with query strings such as `#/login?...` being treated as non-login pages ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
- Fixed initialization completion entering the regular login page, requiring users to manually switch to admin login ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
- Fixed inaccurate first-time SSO completion, OIDC callback account display, and bound-account relogin state ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
- Fixed LDAP accounts being unable to log in after LDAP integration ([#33](https://github.com/ClouGence/open-cdm/issues/33)).
- Fixed WebSocket creation, reconnection, or sending while logged out or after login expiration ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
- Fixed OIDC logout not actively closing WebSocket ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
- Fixed system preference, role, and data-source URLs remaining accessible when only menus were hidden ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
- Fixed Oracle 23ai data-source compatibility ([#27](https://github.com/ClouGence/open-cdm/issues/27)).
- Fixed `DataSource (0) not exist` errors when editing data-source parameters ([#13](https://github.com/ClouGence/open-cdm/issues/13)).
- Fixed Oracle data-source configuration edits not fully displaying or saving some parameters.
- Fixed SQL tickets failing to use the built-in approval flow or configure external approval flows ([#18](https://github.com/ClouGence/open-cdm/issues/18), [#22](https://github.com/ClouGence/open-cdm/issues/22)).
- Fixed overly short initialization MySQL driver and normal data-source driver download errors ([#42](https://github.com/ClouGence/open-cdm/issues/42)).
