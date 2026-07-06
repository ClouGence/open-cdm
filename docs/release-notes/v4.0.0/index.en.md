## Highlights

- Refactored the datasource configuration model, adding SSH tunnel, proxy (HTTP/SOCKS4/SOCKS5), and SSL certificate management.
- Split the SQL parser engine from datasource plugins into an independent `clouddm-sql` module system.
- Improved security rule and role management workflows, with a dedicated configuration page for approval engines.
- Restructured the SSO authentication system, separating AD and LDAP into independent configurations that can be enabled simultaneously.
- Removed the project concept, simplified CI/CD ticket workflows, and added JDK 17 support.
- Removed the init-time driver download flow for a simpler first-time deployment experience.

## Added

- Added SSH tunnel management, including the `dm_ssh_config` table, SSH configuration page, password/private-key/proxy/known_hosts probing, and connection testing ([#23](https://github.com/ClouGence/open-cdm/issues/23)).
- Added datasource SSL certificate configuration, supporting CA certificates, client certificates, client keys, file formats, and passwords.
- Added proxy type support for HTTP, SOCKS4, and SOCKS5.
- Added the `clouddm-sql` module system, splitting the SQL parser engine from datasource plugins into independent modules (`sql-mysql`, `sql-oracle`, `sql-postgres`, `sql-db2`, `sql-doris`, `sql-sqlserver`, `sql-redis`, `sql-mongodb`, `sqlc-common`).
- Added Dameng database support ([#95](https://github.com/ClouGence/open-cdm/issues/95)).
- Added dedicated SSO provider configuration pages, supporting list + form page management for LDAP, AD, OIDC, DingTalk, Feishu, and WeChat.
- Added a dedicated approval engine configuration page, supporting list + form management for DingTalk, Feishu, and WeCom approval, with an inline enable switch in the list.
- Added independent AD and LDAP configuration sets, enabling simultaneous use (includes a Flyway upgrade migration script).
- Added an SQL audit log retention days setting entry.
- Added CI/CD GitHub Actions workflows (ci.yml, cd.yml).
- Added Docker multi-architecture packaging support.
- Added `CODEOWNERS` and `PULL_REQUEST_TEMPLATE` collaboration files.
- Added integration configuration documentation links, providing "How to integrate" doc entries in IM/Git/SSO/approval forms.

## Improved

- Refactored the datasource configuration model, migrating `DataSourceConfig`, `DataSourceType`, `ConfigDef`, and related types from base to plugin-sdk, unifying plugin-side configuration via `DsConfigSpi`.
- Improved the datasource create/edit flow using `DmDsConfigUiPanelFactory` and `DmDsConfigUiDataFactory` to support dynamic config rendering (GENERAL, OPTIONS, SSH_SSL, ADVANCED, SHADOW groups).
- Improved datasource config storage by migrating `dm_ds_config_kv_4rdp` to `dm_ds_config_kv_4dm`, moving status/cluster-binding/environment fields into `dm_ds`.
- Improved the security rule workflow and role management UI.
- Improved the datasource create/edit flow, providing test connection capability for all datasources ([#36](https://github.com/ClouGence/open-cdm/issues/36)).
- Improved OIDC login flow end-to-end usability, restored the login page provider switcher, and fixed empty-scope access_denied issues.
- Reduced the frontend entry bundle size by approximately 1 MB.
- Improved login page responsive layout.
- Improved the operation audit list and export, supporting plugin-based export formats, full and limited-row export, and WebSocket progress updates.
- Improved CI/CD ticket workflow pages and search experience.
- Unified camelCase naming for front-end and back-end interfaces.
- Improved Docker startup scripts and build base images.
- Unified token cookie name prefixes.
- Standardized i18n resource paths and key names across authentication and approval providers.

## Fixed

- Fixed datasource configuration, driver loading, built-in driver packaging, and connection config issues (GaussDB, ClickHouse, MaxCompute, DB2, OceanBase, and others).
- Fixed SQL workbench query loading, datasource tree expansion state, PG catalog initialization, and SQL Server certificate trust config issues.
- Fixed permission, role, ticket, approval scheduling, and datasource creator authorization issues.
- Fixed residual password fields when deleting SSO provider configurations.
- Fixed a secondary NPE caused by null errorMessage during OIDC/SSO callback failure.
- Fixed the standalone version not applying port changes after restart ([#87](https://github.com/ClouGence/open-cdm/issues/87)).
- Fixed Oracle 11g driver download failure ([#35](https://github.com/ClouGence/open-cdm/issues/35)).
- Fixed Oracle 11g ORA-01882 timezone connection error ([#12](https://github.com/ClouGence/open-cdm/issues/12)).
- Fixed Redis datasource Jedis driver adapter loading failure causing query errors and connection failures ([#78](https://github.com/ClouGence/open-cdm/issues/78), [#92](https://github.com/ClouGence/open-cdm/issues/92)).
- Fixed addition of Alibaba Cloud ADB data source failing ([#25](https://github.com/ClouGence/open-cdm/issues/25)).
- Fixed SQL Server 2012 test connection error ([#79](https://github.com/ClouGence/open-cdm/issues/79)).
- Fixed driver showing as not ready when adding a data source ([#88](https://github.com/ClouGence/open-cdm/issues/88)).
- Fixed config read bugs and initialization upgrade wizard UI issues ([#71](https://github.com/ClouGence/open-cdm/issues/71)).
- Fixed table row height jitter caused by link underline on hover (SSO/approval/integration lists).
- Fixed danger confirmation buttons not rendering in red.
- Fixed the approval engine list inline switch UI desync after toggling.
