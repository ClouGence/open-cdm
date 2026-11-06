## Added

- Added an OpenSearch 2.x / 3.x datasource with TLS connections, metadata browsing, REST DSL queries and aggregations, document and index management, pagination, and export, integrated with permissions, read-only enforcement, auditing, and masking ([PR #448](https://github.com/ClouGence/open-cdm/pull/448)).
- Added IoTDB datasources for the 1.3.x Tree and 2.x Tree / Table models, with metadata browsing, SQL parsing and completion, data and object operations, object scripts, full-path timeseries or table permissions, and model isolation ([PR #450](https://github.com/ClouGence/open-cdm/pull/450)).
- Added statement classification and behavior analysis for PolarDB-X users and roles, sessions and transactions, system management, maintenance, and execution plans ([PR #447](https://github.com/ClouGence/open-cdm/pull/447)).
- Added light, dark, and system appearance modes with persistent selection and synchronized themes for editors, query results, forms, and dialogs ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Added version-aware authorization exemptions for ordinary built-in functions, removing the extra program permission requirement while preserving independent authorization for table access, custom functions, and privileged or unsafe operations ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Added Create similar ticket for inline SQL tickets, prefilling the title, description, accessible target database and schema, and SQL ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).

## Fixed

- Fixed SSH tunnels overwriting the original datasource address, causing query metadata loading or subsequent connections to fail after a successful connection test ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Fixed transaction state synchronization explicitly committing PostgreSQL JDBC sessions that already have auto-commit enabled, causing errors with CockroachDB ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Fixed Oracle accounts without DBA dictionary access failing to read columns, comments, DBLinks, and job metadata, with corresponding column and job query updates for YashanDB ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Fixed authorization periods in permission applications, administrator grants, and ticket details being affected by the server's default timezone, using millisecond timestamps while preserving the date-reading semantics of historical tickets ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Fixed SQL Server query editors suggesting table names but failing to suggest column names ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Fixed action buttons shifting and Create similar ticket briefly disappearing during ticket refreshes, and query buttons changing size while loading ([PR #439](https://github.com/ClouGence/open-cdm/pull/439)).
- Fixed transaction isolation labels and asynchronous query transaction messages ignoring the selected UI language ([PR #447](https://github.com/ClouGence/open-cdm/pull/447), [PR #448](https://github.com/ClouGence/open-cdm/pull/448)).
- Fixed query sessions showing an inapplicable read-only toggle for Elasticsearch, OpenSearch, Kafka, and etcd ([PR #448](https://github.com/ClouGence/open-cdm/pull/448)).
