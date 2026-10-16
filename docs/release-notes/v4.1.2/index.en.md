## Highlights

- SQL approval tickets now provide cross-database DML impact and execution-plan analysis with live preparation, analysis, skip, and failure states plus interruption recovery.
- SQL engines now share version- and session-aware parsing, splitting, behavior analysis, security rules, column lineage, and rewriting.
- Tickets can be searched by description or inline SQL, result downloads can be renamed, and result exports and downloads are audited.
- Oracle adds configurable client character sets, while ClickHouse gains complex-type reads and connection compatibility improvements.

## Added

- Added DML impact and execution-plan analysis for SQL approval tickets across MySQL, TiDB, Doris, Dameng, PostgreSQL, SAP HANA, SQL Server, Oracle, StarRocks, DB2, MariaDB, and ClickHouse, including version-aware plans, affected-row estimates, phase progress, skip reasons, error details, lease-based recovery, and concurrent isolation ([#260](https://github.com/ClouGence/open-cdm/issues/260)).
- Database drivers can now declare a default version in the backend `drivers.xml`, which is preferred when creating a datasource in the frontend ([#260](https://github.com/ClouGence/open-cdm/issues/260)).
- Result-set downloads can now be renamed before downloading, with validation for empty names and invalid characters ([#299](https://github.com/ClouGence/open-cdm/issues/299)).
- Added operation-audit events for result-set exports and downloads, including file and operation details, and improved audit resource display ([#302](https://github.com/ClouGence/open-cdm/issues/302)).
- Ticket lists can now search descriptions and inline SQL content, and display copyable ticket descriptions ([#305](https://github.com/ClouGence/open-cdm/issues/305)).
- Added an Oracle client-character-set option with common and custom values, applying the configured encoding when reading `CHAR` and `VARCHAR2` data ([#295](https://github.com/ClouGence/open-cdm/issues/295)).

## Improved

- Improved the SQL engine architecture by using the live database version and session parameters such as `sql_mode` for parsing, completion, splitting, behavior and permission analysis, column lineage, and SQL rewriting; also expanded multi-version MySQL syntax, Dameng security domains, and Doris and StarRocks table metadata parsing ([#260](https://github.com/ClouGence/open-cdm/issues/260)).
- Improved ticket analysis and execution details with consolidated impact statistics, objects and behaviors, analysis errors, execution phases, and progress, together with more reliable large-SQL previews and task recovery ([#260](https://github.com/ClouGence/open-cdm/issues/260)).
- Improved localization and layout for datasource configuration, security-rule details, SQL logs, and environment pages; ticket execution states, actions, and scheduling logs now follow the user's language ([#300](https://github.com/ClouGence/open-cdm/issues/300), [#304](https://github.com/ClouGence/open-cdm/issues/304)).

## Fixed

- Fixed Docker standalone containers failing to restore the embedded MySQL application account from persisted configuration after recreation; missing accounts are now created and password drift is reconciled for the target database, contributed by community contributor [@sunjiajie](https://github.com/sunjiajie)—thank you! ([#272](https://github.com/ClouGence/open-cdm/issues/272)).
- Fixed ClickHouse `fetchSize` compatibility and added support for reading JSON, Map, Tuple, Dynamic, Variant, and nested Array values, contributed by community contributor [@BetaCat0](https://github.com/BetaCat0)—thank you! ([#253](https://github.com/ClouGence/open-cdm/issues/253)).
- Fixed column-lineage and security-rule analysis potentially failing with a type-cast error when SQL function arguments contain non-`SELECT` domains, contributed by community contributor [@sunjiajie](https://github.com/sunjiajie)—thank you! ([#259](https://github.com/ClouGence/open-cdm/issues/259)).
- Fixed synchronous OpenAPI queries omitting errors and warnings from non-Console display modes and potentially collecting the same message more than once, contributed by community contributor [@sunjiajie](https://github.com/sunjiajie)—thank you! ([#281](https://github.com/ClouGence/open-cdm/issues/281)).
- Fixed duplicate execution-plan wrapping, incorrect `EXPLAIN` behavior classification, and compatibility issues affecting older ClickHouse connections, DB2 for z/OS timezone parameters, and SQL Server `SHOWPLAN` execution ([#260](https://github.com/ClouGence/open-cdm/issues/260)).
- Fixed SQL analysis for ClickHouse `EXISTS` subqueries, Oracle multi-row `INSERT ... VALUES`, and PostgreSQL `INSERT ... SELECT`, and improved `INSERT` affected-row estimates across databases ([#260](https://github.com/ClouGence/open-cdm/issues/260)).
