## Added

- Added an AI SQL assistant that generates SQL from natural-language requests using authorized database metadata, with follow-up questions, conversation history and sharing, streaming output, cancellation, and recovery after refresh. Generated SQL can be inserted into the query editor for manual execution ([PR #405](https://github.com/ClouGence/open-cdm/pull/405)).
- Added administrator-managed AI engines supporting OpenAI-compatible APIs, DeepSeek, Kimi, MiniMax, and DashScope, with connection testing and encrypted credential storage ([PR #405](https://github.com/ClouGence/open-cdm/pull/405)).
- Added Elasticsearch datasource support for 7.10+, 8.x, and 9.x, including document queries and writes against exact indices, aggregations, index and Mapping/Settings management, Console-style request editing, permission checks, auditing, masking, and result export. Eligible document searches support automatic pagination on 7.12+ ([PR #391](https://github.com/ClouGence/open-cdm/pull/391)).
- Added YashanDB datasource support with separate Oracle and MySQL compatibility modes for connection configuration, object browsing, SQL queries, and structure management, contributed by community contributor [@ComingWen](https://github.com/ComingWen)—thank you! ([PR #388](https://github.com/ClouGence/open-cdm/pull/388)).
- Added SQL workspace actions to open, disconnect, and reconnect connections while preserving query tabs, SQL drafts, and results. Disconnecting interrupts queries and rolls back uncommitted manual transactions; reconnecting does not automatically replay SQL ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
- Added loading skeletons for SQL workspace sections to reduce empty-state flashes during initial loading and retain existing datasource and object lists during refresh ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
- Added an access-request entry and administrator guidance in the SQL workspace when the account has no datasource permissions ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
- Added an environment column to the datasource instance list, with tooltips for long names and horizontal scrolling ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).

## Fixed

- Fixed automatically executed tickets opening connections without the selected database and schema, causing datasources such as PostgreSQL to access the default database instead, contributed by community contributor [@sunjiajie](https://github.com/sunjiajie)—thank you! ([PR #279](https://github.com/ClouGence/open-cdm/pull/279)).
- Fixed missing applicant identities in ticket lists and details when the account display name is blank by showing the unique login account ([PR #407](https://github.com/ClouGence/open-cdm/pull/407)).
- Fixed application logs no longer appearing in container output after log rotation in Docker deployments ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
- Fixed MySQL table details failing to load for functional unique indexes and index access methods affecting index-type classification during SQL review ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
- Fixed transaction rollback errors preventing automatic execution jobs from reporting failure, and unnecessary auto-commit restoration interrupting terminal-state reporting ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
- Fixed missing scheduling error logs and failure details when packaging automatic execution jobs fails ([PR #398](https://github.com/ClouGence/open-cdm/pull/398)).
