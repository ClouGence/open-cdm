## Added

- Added team-based, multi-stage internal approval workflows that can be bound per environment for SQL, change, and permission tickets, with designated approval teams, required vote counts, execution participants, and per-person results, timestamps, and comments ([PR #433](https://github.com/ClouGence/open-cdm/pull/433)).
- Added an etcd key-value datasource with multiple endpoints, authentication, TLS, `get` / `put` / `del`, instance-level key browsing, and read-only sessions, plus ticket handling without Catalog / Schema levels, contributed by community contributor [@48N6E](https://github.com/48N6E)—thank you! ([PR #416](https://github.com/ClouGence/open-cdm/pull/416)).
- Added a transposed query result view for comparing records on the current page by field, with independent toggles per result tab. Copying, cell details, and fetching truncated values remain available; exports retain their original orientation, and streaming results do not offer transposition ([PR #429](https://github.com/ClouGence/open-cdm/pull/429)).
- Added DCL classification and permission analysis for CockroachDB, including object grants and revocations, role membership, default privileges, and user and role management ([PR #364](https://github.com/ClouGence/open-cdm/pull/364)).
- Added SAP HANA management statement parsing and behavior analysis using HANA Platform 2.0 SPS 08 as the baseline, covering sessions and connections, security and privileges, system configuration and workloads, databases and backup/recovery, and execution plans and diagnostics. The existing `SET SCHEMA` restriction remains in place ([PR #431](https://github.com/ClouGence/open-cdm/pull/431)).
- Added DCL classification and permission analysis for Hologres permission procedures, including SPM / SLPM membership, grants and revocations, permission copying, and schema renaming, with distinct schema-wide and default-privilege scopes ([PR #436](https://github.com/ClouGence/open-cdm/pull/436)).
- Added metadata statement classification and behavior analysis for StarRocks `SHOW CREATE TABLE`, `SHOW CREATE VIEW`, and `SHOW CREATE MATERIALIZED VIEW` ([PR #431](https://github.com/ClouGence/open-cdm/pull/431)).
- Added insertion of SQL favorites at the cursor or over the current selection, with undo/redo. Saving explicitly targets a new or existing favorite; each query tab independently remembers its most recently successfully inserted favorite across refreshes ([PR #424](https://github.com/ClouGence/open-cdm/pull/424)).

## Fixed

- Fixed copying AI messages, replies, and SQL text on non-loopback HTTP deployments, and editor initialization issues in browsers without the required clipboard APIs ([PR #424](https://github.com/ClouGence/open-cdm/pull/424)).
- Fixed SQL editor fold arrows appearing as hollow boxes when the icon font was overridden ([PR #419](https://github.com/ClouGence/open-cdm/pull/419)).
- Fixed toast close buttons being positioned outside their containers and becoming obscured ([PR #424](https://github.com/ClouGence/open-cdm/pull/424)).
- Fixed testing one datasource connection disabling the test buttons for other instances ([PR #433](https://github.com/ClouGence/open-cdm/pull/433)).
- Fixed JDBC commit or rollback failures not reaching the query client and queries not receiving a terminal message ([PR #364](https://github.com/ClouGence/open-cdm/pull/364)).
- Fixed automatic pagination inserting LIMIT in the wrong position for PostgreSQL-family queries with an outer ORDER BY after HAVING ([PR #364](https://github.com/ClouGence/open-cdm/pull/364)).
- Fixed loss of fractional-second precision when reading TIME values with drivers that support JDBC 4.2 time conversion ([PR #431](https://github.com/ClouGence/open-cdm/pull/431)).
- Fixed reading and displaying StarRocks BINARY / VARBINARY results when the driver reports BLOB or TEXT types ([PR #431](https://github.com/ClouGence/open-cdm/pull/431)).
- Fixed parsing Oracle column definitions containing `DEFAULT ON NULL`, and duplicate index nodes in the object tree when a unique constraint and its backing index share a name ([PR #431](https://github.com/ClouGence/open-cdm/pull/431)).
- Fixed unbinding an environment approval template potentially clearing its usage while other environments or active tickets still reference it ([PR #433](https://github.com/ClouGence/open-cdm/pull/433)).
- Fixed standalone restarts not triggering interrupted-task recovery for the embedded Worker ([PR #433](https://github.com/ClouGence/open-cdm/pull/433)).
- Fixed specification creation and editing actions remaining visible to accounts with only security-rule viewing permissions ([PR #433](https://github.com/ClouGence/open-cdm/pull/433)).
