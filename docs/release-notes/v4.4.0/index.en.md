## Added

- Added Kafka datasource support with topic browsing and management, message consumption, command completion and templates, TLS/SASL authentication, access control, and auditing ([PR #390](https://github.com/ClouGence/open-cdm/pull/390)).
- Added SAP HANA datasource support with TLS/SSH connections, object browsing, SQL/SQLScript support, structure and data editing, object scripts, and SQL export, integrated with permission checks, SQL review, lineage analysis, and data masking ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).
- Added full-document SQL formatting for dialects including HANA, MySQL, Oracle, and PostgreSQL ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).

## Fixed

- Fixed Oracle JSON column recognition and reading errors ([PR #377](https://github.com/ClouGence/open-cdm/pull/377)).
- Fixed SQL change tickets failing in Console + Sidecar deployments, contributed by community contributor [@theluckystrike](https://github.com/theluckystrike)—thank you! ([PR #384](https://github.com/ClouGence/open-cdm/pull/384)).
- Fixed ticket execution being incorrectly denied for subaccounts granted access to all datasources, contributed by community contributor [@vantis-zh](https://github.com/vantis-zh)—thank you! ([PR #359](https://github.com/ClouGence/open-cdm/pull/359)).
- Fixed environment name filtering, contributed by community contributor [@fengruiwd](https://github.com/fengruiwd)—thank you! ([PR #335](https://github.com/ClouGence/open-cdm/pull/335)).
- Fixed overlapping processor names in ticket details and added access to the complete list ([PR #380](https://github.com/ClouGence/open-cdm/pull/380)).
- Fixed data editor handling of defaults, explicit NULL values, and inserts using only defaults, and prevented read-only columns from being submitted ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).
- Fixed data editing failures not showing errors and allowing subsequent rows to be saved ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).
- Fixed incomplete copying of long text and binary results, incorrect streamed row lookup, and incorrect binary NULL display ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).
- Fixed SAP HANA execution plan nodes, row estimates, and source classification ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).
- Fixed SQL syntax diagnostics and execution timing messages remaining in Chinese in the English interface ([PR #381](https://github.com/ClouGence/open-cdm/pull/381)).
