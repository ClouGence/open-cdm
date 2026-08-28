## Added

- Added AWS Valkey datasource support; when managed instances disable the `CONFIG` command, CloudDM can use `SELECT` to detect the actual number of databases, contributed by community contributor [@BetaCat0](https://github.com/BetaCat0)—thank you! ([#249](https://github.com/ClouGence/open-cdm/issues/249)).
- Added CockroachDB as a first-class datasource with connection configuration, SSL, catalog and schema defaults, object browsing, and basic SQL queries, contributed by community contributor [@48N6E](https://github.com/48N6E)—thank you! ([#296](https://github.com/ClouGence/open-cdm/issues/296)).
- Added four independent KingbaseES datasource modes for PostgreSQL, MySQL, Oracle, and SQLServer compatibility, including mode detection and mismatch rejection ([#285](https://github.com/ClouGence/open-cdm/issues/285)).
- Added GoldenDB MySQL and GoldenDB Oracle datasources with their corresponding drivers and SQL families, including connections, object browsing, queries, table tooling, and GoldenDB distributed-table metadata ([#285](https://github.com/ClouGence/open-cdm/issues/285)).
