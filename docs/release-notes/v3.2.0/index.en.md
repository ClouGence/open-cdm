---
id: open_cdm_3_2_0_en
title: Open CDM 3.2.0
sidebar_label: v3.2.0
description: Improves login accounts, SQL editor, operation audit, and deployment
date: 2026-06-11
tags: [Version Update, Login, SQL Editor, Operation Audit, Docker, Permissions]
---

- Release date: June 11, 2026
- Version: v3.2.0

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
  bladepipe/cgdm-alone:3.2.0

# China acceleration image
docker run -d --name cgdm-alone -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.2.0
```

Open the following URL after startup:

```text
http://localhost:8222
```

On first access after a fresh deployment, the initialization wizard will open; during an upgrade, the upgrade wizard will open.

## Highlights

- Unified the login entry and account model, without separating administrator identity from normal user identity.
- Added datasource language services so the SQL editor can use backend plugins for completion, splitting, and validation.
- Improved Docker, initialization, and project layout for deployment, upgrades, and open-source collaboration.

## Added

- Added a unified login entry and account model, without separating administrator identity from normal user identity.
- Added copyable MFA codes during MFA setup for environments where QR code scanning is unavailable.
- Added support for enabling different SSO authentication systems and choosing the default option by configuration.
- Added support for enabling local-account capabilities for external accounts.
- Added a plugin resource loading component for reading plugin static resources from the web side.
- Added `DsLanguageSpi` for datasource plugins to provide editor SQL completion, splitting, and validation.
- Added operation audit export progress events and WebSocket updates.
- Added a reusable result-file reader and writer component for Console, Sidecar, and file conversion.
- Added `AGENTS.md` and related collaboration guidance files.

## Improved

- Improved the login page by merging admin login and account login into one entry.
- Improved account login with account, email, or phone lookup.
- Improved global resource permission implementation by merging it into the existing authorization system and supporting authorization time settings.
- Improved OIDC, LDAP, AD, DingTalk, Feishu, WeChat, and other external login flows.
- Improved account management and profile editing.
- Improved MFA setup, reset, close, and login invalidation flows.
- Improved SQL editor completion, syntax validation, and table-permission semantic validation.
- Improved SQL log lists with datasource identity and remarks.
- Improved Docker startup scripts, default configuration, and embedded MySQL data reuse.
- Improved source layout by grouping code under `backend`, `frontend`, and `package`.
- Improved documentation layout by moving README, DEPLOY, FAQ, and contribution docs under docs.

## Fixed

- Fixed repeated completion prompts during first-time external login ([#58](https://github.com/ClouGence/open-cdm/issues/58)).
- Fixed Feishu SSO remote-call timeouts during login ([#45](https://github.com/ClouGence/open-cdm/issues/45)).
- Fixed initialization failures and initialization-page resource loading issues ([#11](https://github.com/ClouGence/open-cdm/issues/11), [#46](https://github.com/ClouGence/open-cdm/issues/46)).
- Fixed login definitions loading before plugins were ready after initialization, which could cause the page logo to fail on first load.
- Fixed Docker default configuration bootstrap and embedded MySQL stability issues.
- Fixed extra input focus shadow and dark-mode display issues on the initialization page ([#4](https://github.com/ClouGence/open-cdm/issues/4)).
- Fixed the operation audit page failing to open ([#20](https://github.com/ClouGence/open-cdm/issues/20), [#53](https://github.com/ClouGence/open-cdm/issues/53)).
- Fixed built-in ticket workflow errors ([#52](https://github.com/ClouGence/open-cdm/issues/52)).
- Fixed incomplete operator and datasource display in SQL logs.
- Fixed inconsistent Kubernetes Worker Pod references.
