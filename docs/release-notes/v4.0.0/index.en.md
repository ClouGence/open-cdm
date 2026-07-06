## Highlights

- Upgraded datasource connectivity with SSH tunnels, proxies, and SSL certificate management.
- Improved SSO, approval, security rule, and role management configuration experiences.
- Simplified CI/CD ticket workflows and added JDK 17 plus Docker multi-architecture packaging support.
- Removed init-time driver downloads for a simpler first-time deployment experience.
- Split SQL parsing into independent modules.

## Added

- Added SSH tunnel management, including the `dm_ssh_config` table, SSH configuration page, password/private-key/proxy/known_hosts probing, and connection testing ([#23](https://github.com/ClouGence/open-cdm/issues/23)).
- Added datasource secure connection configuration, including SSL certificates and HTTP, SOCKS4, and SOCKS5 proxies.
- Added the `clouddm-sql` module system, splitting SQL parsing capabilities out of datasource plugins.
- Added Dameng database support ([#95](https://github.com/ClouGence/open-cdm/issues/95)).
- Added dedicated SSO provider and approval engine configuration pages for LDAP, AD, OIDC, DingTalk, Feishu, WeChat, and WeCom integrations.
- Added independent AD and LDAP configuration sets, enabling simultaneous use.
- Added an SQL audit log retention days setting entry.
- Added Docker multi-architecture packaging support.
- Added integration documentation entries in IM, Git, SSO, and approval forms.

## Improved

- Improved the datasource configuration model, storage structure, and create/edit flow for better plugin-based configuration.
- Improved the security rule workflow and role management UI.
- Improved the datasource create/edit flow, providing test connection capability for all datasources ([#36](https://github.com/ClouGence/open-cdm/issues/36)).
- Improved login and authentication experience, including the OIDC login flow, login page provider switcher, and responsive layout.
- Reduced the frontend entry bundle size by approximately 1 MB.
- Improved the operation audit list and export, supporting plugin-based export formats, full and limited-row export, and WebSocket progress updates.
- Improved CI/CD ticket workflow pages and search experience.
- Improved Docker startup scripts and build base images.
- Improved naming consistency for front-end/back-end interfaces, token cookies, and authentication/approval configuration.

## Fixed

- Fixed a set of datasource configuration, driver loading, built-in driver packaging, and connection configuration issues.
- Fixed SQL workbench query loading, datasource tree expansion state, and database metadata initialization issues.
- Fixed permission, role, ticket, approval scheduling, and datasource creator authorization issues.
- Fixed several SSO, OIDC, and approval configuration page display and interaction issues.
- Fixed the standalone version not applying port changes after restart ([#87](https://github.com/ClouGence/open-cdm/issues/87)).
- Fixed Oracle 11g driver download failure ([#35](https://github.com/ClouGence/open-cdm/issues/35)).
- Fixed Oracle 11g ORA-01882 timezone connection error ([#12](https://github.com/ClouGence/open-cdm/issues/12)).
- Fixed Redis datasource Jedis driver adapter loading failure causing query errors and connection failures ([#78](https://github.com/ClouGence/open-cdm/issues/78), [#92](https://github.com/ClouGence/open-cdm/issues/92)).
- Fixed addition of Alibaba Cloud ADB data source failing ([#25](https://github.com/ClouGence/open-cdm/issues/25)).
- Fixed SQL Server 2012 test connection error ([#79](https://github.com/ClouGence/open-cdm/issues/79)).
- Fixed driver showing as not ready when adding a data source ([#88](https://github.com/ClouGence/open-cdm/issues/88)).
- Fixed config read bugs and initialization upgrade wizard UI issues ([#71](https://github.com/ClouGence/open-cdm/issues/71)).
