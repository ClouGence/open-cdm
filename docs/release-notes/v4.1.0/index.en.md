## Highlights

- SQL approval tickets now support SQL file uploads and stage-specific content for paged preview and analysis, approval, dispatch, and execution.
- Database CI/CD now supports cascading release-flow orchestration for batching database changes through parent-child flows.
- Added Apache Cloudberry and Dameng support, together with MongoDB Atlas, MongoDB SSL, and Redis SSL connectivity.

## Added

- Added SQL file uploads for approval tickets, with UTF-8 validation, configurable size limits, and line-based paged previews.
- Added cascading CI/CD release-flow orchestration with parent-child flows, batch orchestration, change transfer, failure retries, and trigger permissions ([#55](https://github.com/ClouGence/open-cdm/issues/55)).
- Added private, self-hosted GitLab as a CI/CD release source, supporting HTTP/HTTPS, non-standard ports, subpath deployments, and push and merge-request webhooks ([#169](https://github.com/ClouGence/open-cdm/issues/169)).
- Added first-class Apache Cloudberry support for connection configuration, metadata browsing, SQL queries, and PostgreSQL-compatible table tooling ([#183](https://github.com/ClouGence/open-cdm/issues/183)).
- Improved Dameng datasource support with a dedicated SQL engine covering statement splitting, behavior and permission analysis, security rules, and system function and object recognition ([#95](https://github.com/ClouGence/open-cdm/issues/95)).
- Added MongoDB Atlas SRV connection support ([#221](https://github.com/ClouGence/open-cdm/issues/221)).
- Added MongoDB SSL connection support, contributed by community contributor [@BetaCat0](https://github.com/BetaCat0)—thank you! ([#242](https://github.com/ClouGence/open-cdm/issues/242)).
- Added Redis SSL connections with CA certificate, TrustStore, KeyStore, and client-certificate options ([#220](https://github.com/ClouGence/open-cdm/issues/220)).
- Added batch resource-permission grants and revocations for applying the same permissions to multiple accounts ([#176](https://github.com/ClouGence/open-cdm/issues/176), [#177](https://github.com/ClouGence/open-cdm/issues/177)).
- Added 11 commonly used database drivers, bundled with the installation package.

## Improved

- Improved large-SQL processing by streaming approval pre-analysis, security-rule checks, CI/CD SQL processing, task packaging, Sidecar downloads, execution, and reporting without repeatedly materializing complete SQL or task collections.
- Improved the SQL engine architecture with shared cross-database statement splitting, behavior and resource analysis, execution authorization, and auditing; MySQL result masking can now use column lineage to identify source fields.
- Improved ticket and CI/CD details with progress, counts, logs, and failure reasons for SQL recognition, behavior analysis, security-rule checks, and execution stages.
- Improved preferences by organizing account security, CI/CD, data query, and approval settings into tabs with typed controls, dirty-state handling, and server-side validation ([#228](https://github.com/ClouGence/open-cdm/issues/228)).
- Improved MFA login security by replacing the pre-MFA JWT with database-backed one-time challenges, including atomic retry limits across Console nodes, expiry cleanup, and one-time consumption ([#232](https://github.com/ClouGence/open-cdm/issues/232)).
- Improved the SQL workspace datasource tree, empty-result tabs, and column resizing; copying truncated cells now retrieves the complete value, and Monaco SQL diagnostic messages are visible again ([#181](https://github.com/ClouGence/open-cdm/issues/181)).
- Unified page-tab styling across tickets, security rules, the user center, preferences, and management logs, and refined log-table scrolling, SQL editor typography, and paged large-SQL reading.
- Improved SQL audit consistency by creating audit records before dispatch and updating their lifecycle through stable query identifiers, including asynchronous execution and Sidecar reporting.

## Fixed

- Fixed a ClickHouse startup dependency conflict, MariaDB `INT UNSIGNED` reads, MySQL `TINYINT(1)` numeric display, PostgreSQL `CREATE TYPE` execution, and Doris `BUCKETS AUTO` table parsing ([#216](https://github.com/ClouGence/open-cdm/issues/216), [#207](https://github.com/ClouGence/open-cdm/issues/207), [#204](https://github.com/ClouGence/open-cdm/issues/204), [#180](https://github.com/ClouGence/open-cdm/issues/180), [#179](https://github.com/ClouGence/open-cdm/issues/179)).
- Fixed incomplete non-MySQL lineage analyzers causing valid queries to fail before execution while preserving behavior analysis, authorization, and security-rule checks.
- Fixed initialization or upgrade failures caused by global services not being registered before loading the security-rules plugin.
- Fixed concurrent result masking potentially throwing `ConcurrentModificationException` from the atomic-type cache ([#235](https://github.com/ClouGence/open-cdm/issues/235)).
- Fixed SQL audit time filtering, timezone conversion, pagination totals, custom cell rendering, and inserts failing when a username is blank ([#167](https://github.com/ClouGence/open-cdm/issues/167), [#217](https://github.com/ClouGence/open-cdm/issues/217), [#170](https://github.com/ClouGence/open-cdm/issues/170), [#186](https://github.com/ClouGence/open-cdm/issues/186)).
- Fixed duplicate ticket submissions, stale execution status, and restored SQL tabs failing to accept input ([#201](https://github.com/ClouGence/open-cdm/issues/201), [#174](https://github.com/ClouGence/open-cdm/issues/174), [#146](https://github.com/ClouGence/open-cdm/issues/146), [#178](https://github.com/ClouGence/open-cdm/issues/178)).
- Fixed large-SQL ticket details not loading subsequent pages and losing the reading position when new content was appended.
- Fixed forced table refreshes returning stale metadata, enabled global query rules not being applied, manually edited security-rule parameters not taking effect, rule-detail layout errors, environment descriptions not being clearable, and Doris materialized views failing to expand ([#160](https://github.com/ClouGence/open-cdm/issues/160), [#164](https://github.com/ClouGence/open-cdm/issues/164), [#165](https://github.com/ClouGence/open-cdm/issues/165), [#17](https://github.com/ClouGence/open-cdm/issues/17)).
- Fixed PostgreSQL temporary schemas interfering with object browsing, unclear datasource deletion confirmations, and MariaDB query and SSL connection compatibility ([#172](https://github.com/ClouGence/open-cdm/issues/172), [#189](https://github.com/ClouGence/open-cdm/issues/189), [#151](https://github.com/ClouGence/open-cdm/issues/151), [#185](https://github.com/ClouGence/open-cdm/issues/185)).
- Fixed temporary uploaded certificate attachments not being removed after saving a datasource ([#125](https://github.com/ClouGence/open-cdm/issues/125)).
