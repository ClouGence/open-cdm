## Added

- Added environment grouping and search to the datasource selector on the ticket creation page; already loaded datasources remain selectable when the environment list is incomplete or fails to load ([#293](https://github.com/ClouGence/open-cdm/issues/293)).
- Changed online query result caching to use a configurable capacity limit in MB; when the limit is exceeded, results are removed from oldest to newest by last access time, and setting the limit to `0` disables result caching ([#301](https://github.com/ClouGence/open-cdm/issues/301)).

## Fixed

- Fixed adding the first Feishu or WeCom approval template failing when its template-list configuration did not yet exist ([#214](https://github.com/ClouGence/open-cdm/issues/214)).
- Fixed session initialization for GaussDB and other PostgreSQL-family datasources potentially failing to set the isolation level when manual-commit mode had already started a transaction ([#323](https://github.com/ClouGence/open-cdm/issues/323)).
- Fixed Feishu SSO not loading the role-mapping configuration, which prevented the default role from taking effect ([PR #325](https://github.com/ClouGence/open-cdm/pull/325)).
- Fixed incomplete SQL Server extended-property joins producing duplicate metadata, and plugin driver classes not being loaded correctly ([#224](https://github.com/ClouGence/open-cdm/issues/224), [#327](https://github.com/ClouGence/open-cdm/issues/327)).
- Fixed missing statement splitting, behavior analysis, and security analysis for SQL Server `TRUNCATE TABLE` statements ([#234](https://github.com/ClouGence/open-cdm/issues/234)).
- Fixed rule-validation results not being presented correctly in a modal for tickets and the SQL workspace, while preserving rule level, name, violation message, and line information ([#328](https://github.com/ClouGence/open-cdm/issues/328)).
- Fixed the table context menu's permission-request action not opening the permission application page ([#329](https://github.com/ClouGence/open-cdm/issues/329)).
- Fixed permission-request ticket descriptions incorrectly including requester information ([#162](https://github.com/ClouGence/open-cdm/issues/162)).
