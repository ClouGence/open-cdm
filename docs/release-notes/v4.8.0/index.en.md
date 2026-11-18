## Added

- Added a GreptimeDB datasource with connection configuration, metadata browsing, SQL queries, DDL / DML execution, and query row limits ([PR #472](https://github.com/ClouGence/open-cdm/pull/472)).
- Added the Kafka single-message command `kafka-console-producer` with key, value, and partition options, integrated with Topic send permissions, read-only enforcement, and auditing ([PR #473](https://github.com/ClouGence/open-cdm/pull/473)).
- Added Kafka consumer options `--group` and `--consumer-property enable.auto.commit=true|false`, supporting consumption from committed offsets and configurable commits for emitted messages ([PR #473](https://github.com/ClouGence/open-cdm/pull/473)).
- Added parsing and behavior classification for IoTDB Tree `CREATE DEVICE TEMPLATE` and `SET DEVICE TEMPLATE` statements ([PR #478](https://github.com/ClouGence/open-cdm/pull/478)).
- Added IPv6 datasource connection addresses for direct connections and SSH tunnel configuration ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Added cloud deployment type and database category filters to datasource selection for locating self-managed and cloud-hosted databases ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Added mandatory initial-password changes on first login for administrator-created local accounts, issuing a login session only after the change is complete ([PR #456](https://github.com/ClouGence/open-cdm/pull/456)).
- Added persistent server-side login sessions with Cookie authentication for web and WebSocket access and revocation on account security changes; removed separate operation-password verification and added administrator resets for invalid MFA bindings ([PR #463](https://github.com/ClouGence/open-cdm/pull/463)).
- Added content-based JSON detection in query result cell details, allowing valid JSON in text columns to be displayed with formatting ([PR #463](https://github.com/ClouGence/open-cdm/pull/463)).

## Fixed

- Fixed PostgreSQL SQL audit rejecting `DROP INDEX` and `CREATE INDEX` statements with `WHERE` predicates, contributed by community contributor [@congshenV](https://github.com/congshenV)—thank you! ([PR #467](https://github.com/ClouGence/open-cdm/pull/467)).
- Fixed IoTDB metadata queries incorrectly requiring datasource ownership or individual-object permissions, checking instance, model, or database query grants for the requested scope and listing timeseries directly under Tree databases ([PR #458](https://github.com/ClouGence/open-cdm/pull/458)).
- Fixed metadata type resolution for parameterized MaxCompute column types ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Fixed completed CI/CD tickets failing to finalize after their flow is disabled, recovering bookkeeping without replaying SQL ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Fixed online queries and task execution ignoring datasource query timeout settings ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Fixed the approval confirmation stage appearing to be configured per stage, clarifying that it is a single global confirmation stage ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Fixed dark-mode styling in editors, forms, dialogs, and query result cell action buttons, and adjusted parameter editor spacing ([PR #454](https://github.com/ClouGence/open-cdm/pull/454), [PR #463](https://github.com/ClouGence/open-cdm/pull/463)).
- Fixed Chinese YashanDB datasource labels not using the brand name ([PR #454](https://github.com/ClouGence/open-cdm/pull/454)).
- Fixed email or phone updates clearing existing TOTP bindings, preserving the current MFA configuration ([PR #463](https://github.com/ClouGence/open-cdm/pull/463)).
- Fixed protected endpoint path matching and initialization endpoint access restrictions, making initialization endpoints available only before bootstrap is complete ([PR #463](https://github.com/ClouGence/open-cdm/pull/463)).
