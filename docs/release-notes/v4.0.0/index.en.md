## Highlights

- Redesigned the interface and interactions across core pages for datasources, permissions, security, and CI/CD.

  ![Comparison of the old and new CloudDM login pages](assets/new-login-experience.png)

- Separated SQL queries from workflows so development queries no longer interfere with approvals or ticket processes.

  ![CloudDM instance management and SQL query workbench](assets/query-workflow-separation.png)

- Expanded support for SSH tunnels, SSL certificates, proxies, SSO, approval integrations, and multiple sign-in methods.

  ![CloudDM SSH, SSL, role permission, and CI/CD configuration](assets/enhanced-management-capabilities.png)

## Added

- Added SSH tunnel management, including an SSH configuration page, password, private key, proxy, and `known_hosts` probing, and connection testing.
- Added secure connection settings for datasources, with support for SSL certificates and HTTP, SOCKS4, and SOCKS5 proxies.
- Added dedicated configuration pages for SSO authentication providers and approval engines, providing centralized management for LDAP, AD, OIDC, DingTalk, Feishu, WeChat, and WeCom integrations.
- Added a setting for SQL audit log retention days.
- Added JDK 17 support and Docker multi-architecture packaging.
- Added integration documentation links to IM, Git, SSO, and approval forms.

## Improved

- Improved the datasource configuration model, storage structure, and create and edit flows for better plugin-based configuration.
- Improved security rule workflows and the role management UI.
- Improved datasource create and edit flows by making connection testing available for every datasource.
- Improved the sign-in and authentication experience, including the OIDC sign-in flow and responsive layouts.
- Improved the operation audit list and export with pluggable export formats, full or row-limited exports, and streaming progress updates.
- Improved CI/CD ticket workflow pages and search.
- Removed driver downloads from initialization to simplify first-time deployment.

## Fixed

- Fixed datasource configuration, driver loading, built-in driver packaging, and connection issues affecting Oracle, PostgreSQL, Greenplum, Redis, and SQL Server.
- Fixed SQL workbench query loading, datasource tree expansion state, and database metadata initialization issues.
- Fixed issues involving permissions, roles, tickets, approval scheduling, and datasource creator authorization.
- Fixed standalone deployments not applying service port changes after restart.
- Fixed the Oracle 11g ORA-01882 timezone connection error.
- Fixed failures when adding Alibaba Cloud ADB datasources.
- Fixed configuration-reading bugs and initialization and upgrade wizard UI issues.
