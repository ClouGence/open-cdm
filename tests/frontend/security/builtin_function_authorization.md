# Ordinary built-in function authorization

Current registration uses product major-version families. Minor and patch versions in historical acceptance records below are observed server versions, not separate permission families. Unknown versions use the enum default and still require a matching registered catalog entry.

## Purpose

Verify Issue #422: an ordinary query-only CloudDM account can use built-in scalar, aggregate and window functions without PROGRAM permission, while table READ, nested custom functions and administrative operations remain protected.

## Scope

- Local test deployment, `/#/sql`, account/role/resource authorization pages.
- Query submission, authorization result, database execution and displayed result.
- SQL Server is the reproducer; repeat on available PostgreSQL, MySQL, MariaDB, Greenplum, Cloudberry, CockroachDB, ClickHouse, Dameng, TiDB and StarRocks instances.
- Does not certify every function overload or server version.

## Preconditions

- Build and locally publish the changed plugins, restart the precise IDEA DmAloneLauncher instance, and wait for backend health plus embedded-worker plugin readiness.
- Use only confirmed test instances and test tables. Preserve existing query tabs and SQL drafts.
- An administrator prepares a dedicated role with only query-console access and a dedicated ordinary account. New credentials must be entered and submitted by the user in the browser; never record passwords in this document or screenshots.
- Grant the account READ only on the selected test table. Do not grant PROGRAM, schema-wide READ, instance-wide READ, MANAGE, MAINTAIN, UNSAFE, DDL or WRITE for the baseline suites. BF-PERM-09 separately verifies a temporary READ scope on the isolated test datasource and restores the baseline. Inspect the saved resource tree before switching accounts.
- Keep a separate administrator session available for setup/cleanup, without reading browser authentication storage.
- Before every query, verify the selected query tab's datasource identity and its visible host/port. A tab title or a successful result from the previously active datasource is insufficient. Discard any result whose actual target was not established.
- Wait for a terminal result belonging to the submitted SQL before starting the next case. A loading view, an earlier result grid, a native error or an expired-session exception is not a passing result. Record session failures separately and reopen the query session before retrying.

## Test Data

- Use a uniquely named account/role prefixed `codex_issue422_` / `codex_issue422_query_`; find the current run's records by their complete names rather than fixed IDs.
- SQL Server may use an existing confirmed `dbo.demo_table` containing `Id`, `Col1`, `Col2`, `Col3`. Validate its actual schema first; do not assume this example exists.
- Prepare one READ-granted test table and one ungranted test table. Use existing disposable test fixtures or administrator-created constant-only tables.
- For a custom-function negative case, use a uniquely named constant-only scalar function in the test schema. Its database connection can execute it, while the CloudDM ordinary account has no PROGRAM grant.
- Destructive/administrative negative cases must be provably harmless if accidentally sent. Prefer a nonexistent backend/session ID or static authorization assertions over a real termination/configuration command.

## Suites

### BF-PERM-01 Ordinary functions retain table READ — P0

- Initial state: ordinary account at `/#/sql`, fresh query tab in the confirmed test schema; selected table has READ, no PROGRAM grant exists.
- Run one SQL statement at a time and explicitly select its complete text before clicking Execute, so a stale cursor position cannot select another statement. Run `SELECT COUNT(*) AS row_count FROM dbo.demo_table;` on SQL Server. Then run scalar and window examples using actual columns: `SELECT TOP (3) LOWER(Col1) AS value_text FROM dbo.demo_table;` and `SELECT TOP (3) ROW_NUMBER() OVER (ORDER BY Id) AS row_num FROM dbo.demo_table;`.
- Expected: all three reach a successful terminal result, show their columns/rows, and do not report DM_PROGRAM. Empty-table results still need a successful execution state.
- Repeat the engine-appropriate variants on available test datasources. ClickHouse additionally exercises `sumIf` with constant-only inputs; do not substitute unsupported function names or infer another engine passed.
- Extend the ordinary matrix with nested string/numeric expressions, AVG/MIN/MAX, SUM/COUNT DISTINCT, date conversion/arithmetic, ranking/navigation windows and JSON/array expressions supported by the confirmed release. A successful COUNT alone does not complete this suite.
- Cleanup: clear only this run's query text/results and retain unrelated drafts.

### BF-PERM-02 Ungranted table remains denied — P0

- Initial state: same ordinary session and granted test table; another test table has no READ grant.
- Replace the FROM target with the ungranted table and run COUNT again. Also place the ungranted table inside a subquery passed to an ordinary aggregate/scalar expression.
- Expected: CloudDM denies the real ungranted table for READ; no result from that table is shown. A built-in wrapper must not exempt its input relations.
- Cleanup: restore the original granted-table query, rerun it and confirm successful recovery.

### BF-PERM-03 Custom and qualified functions remain denied — P0

- Initial state: same ordinary account, READ on its test table, no PROGRAM on the administrator-prepared constant-only function.
- Run the custom function directly, nested inside LOWER/MIN, and with its explicit user schema. Where the engine permits same-named custom functions, test a schema-qualified function named like a built-in.
- Expected: CloudDM rejects the custom Function resource for PROGRAM. The request must be rejected before database execution, rather than merely producing a native “function not found” error.
- Inspect INSERT/UPDATE/DELETE function behavior through static regression when the live account intentionally has no WRITE; do not temporarily broaden its grant for this suite.
- Cleanup: return to the successful ordinary query. Administrator removes only explicitly approved disposable function fixtures.

### BF-PERM-04 Administrative boundary — P0

- Initial state: a confirmed safe negative administrative call and an account lacking management/program permissions.
- Submit the harmless negative call. PostgreSQL examples are tested statically with `pg_reload_conf`, `pg_terminate_backend`, `pg_stat_reset`, `nextval` and `lo_unlink`; do not execute real state-changing versions on a shared database.
- Expected: the appropriate MANAGE/MAINTAIN/UNSAFE/PROGRAM requirement remains, and the operation is not reported as an ordinary-function success.
- Cleanup: rerun a harmless query and verify the session remains usable.

### BF-PERM-05 Refresh and re-entry — P1

- Initial state: BF-PERM-01 and BF-PERM-03 completed with terminal results.
- Refresh the page, reopen the test schema/query tab, then repeat one ordinary positive and one custom-function negative.
- Expected: ordinary calls still succeed and the PROGRAM rejection remains. Check relevant Console errors and request/business outcomes without recording credentials or token-bearing URLs.
- Cleanup: close only this run's tabs.

### BF-PERM-06 Product-specific ordinary functions — P0

- Initial state: the actual product/version is confirmed, its current plugin artifact is loaded, and an ordinary account has READ only on one test table. Do not substitute a compatible MySQL/PostgreSQL instance for the actual product.
- Run these expressions inside a SELECT from the READ-granted test table, one statement at a time: PolarDB-X `TO_MONTHS('2024-01-01')` and `TIME_TO_TSO('2024-01-01 00:00:00')`; MariaDB `JSON_DETAILED('{}')` and version-supported `ADD_MONTHS('2024-01-01', 1)`; OceanBase MySQL `TIME_TO_USEC('2024-01-01 00:00:00')`; CockroachDB `fnv32('issue422')`. For AnalyticDB use `APPROX_DISTINCT` and for Hologres use `MIN_BY`, with real columns from the granted table.
- For Greenplum/Cloudberry, test `linear_interpolate(5,0,0,10,10)`, `complex(1,2)`, `median(amount)` and schema-qualified/nested-UDF negatives. Greenplum `gp_array_agg(amount)` is a positive only on a native GP7/PG12+ kernel; GP6 optional extension calls retain PROGRAM. For OceanBase Oracle, test `WM_CONCAT(name)`, `WM_CONCAT(DISTINCT name)`, its window form, exact `WMSYS.WM_CONCAT(name)`, and quoted/user-schema negatives. If no actual product instance is available, record native/browser acceptance as NOT RUN.
- For YashanDB Oracle mode, retain the existing `Oracle SQL` selection. Confirm that the datasource form contains no additional YashanDB SQL engine option, then test both an existing connection and a newly configured connection without modifying its engine selection. Test `WM_CONCAT(name)`, `WM_CONCAT(DISTINCT name)` and its window form against the granted table, followed by a schema-qualified call and a nested UDF negative. Native YashanDB acceptance is NOT RUN while Motherdata has no supported installation type or an existing server is unavailable.
- Repeat one same-named function qualified by a custom schema and one nested custom function; they still require PROGRAM. Repeat the ordinary expression against the ungranted table; READ remains required.
- On a plain MySQL/PostgreSQL instance, vendor-only names must remain protected unless they independently exist as supported native built-ins. Do not create an optional extension or enable AI/external-resource integration to make a test pass.
- Expected: supported ordinary expressions reach successful database results without PROGRAM; explicit user functions and ungranted tables are denied by CloudDM. Native version/feature/function errors are recorded separately and do not count as successful execution.
- Installation gate: prefer the current Motherdata skill/spec workflow. If installation is unavailable, unsupported or fails, resolve the original operation first, then follow `/Users/pika/docker_opt/localdb/README.md` and the database subdirectory instructions for the permitted local Compose fallback. Preserve existing data.
- Cleanup: remove only this run's disposable fixtures and test authorization according to the existing cleanup procedure.

### BF-PERM-07 YashanDB keeps the existing engine option — P1

- Initial state: an administrator is on `/datasource`, with the current YashanDB plugin loaded. A database server is not required for this form check.
- Click Add, choose YashanDB Oracle compatibility mode, and confirm the type selection. Do not enter credentials or submit a datasource.
- Open Options, then expand Parser Engine. Expected: the selected value and the only dropdown entry are `Oracle SQL`; there is no additional YashanDB SQL engine entry.
- Reopen the form and repeat the dropdown check. If an existing YashanDB connection is available, open its edit form and verify its saved `Oracle SQL` selection remains accepted; execute BF-PERM-06 without changing that selection.
- Cleanup: leave the unsaved form or close only the test tab. Existing datasource configuration must remain unchanged.

### BF-PERM-08 Release-aware registration — P0

- Initial state: the refreshed plugin is loaded, the actual server release is visible/confirmed, and the ordinary account has READ without PROGRAM. Use separate real instances for older/newer releases; do not change stored version fields to simulate a release.
- Check a function with a documented introduction against a server before and at/after that introduction: SQL Server DATETRUNC (2022), PostgreSQL date_bin (14), or Oracle JSON_SERIALIZE (19), according to available environments.
- For an existing SQL Server fixture with an empty saved version, repeat the ordinary CAST/CONVERT query from BF-PERM-01. Resolving the actual server release must preserve ordinary-function authorization; an enum LATEST fallback alone is not proof that the active server uses that catalog.
- Expected: a registered release permits the ordinary call without PROGRAM while retaining table READ. A known older major family without the function retains the function permission requirement. Minor and patch versions in the same major family share catalog facts; unknown versions use the enum default. A database-native syntax/availability error is recorded separately from the CloudDM authorization result.
- For compatible products, verify that product functions use the product enum and compatible-engine functions use the engine registry. Do not substitute a compatible server for the actual product.
- Repeat with a custom schema, quoted function name, nested custom function and harmless administrative negative case; their authorization remains. Unknown-version and unavailable-release cases may be checked statically when a real instance cannot provide them.
- Cleanup: preserve datasource versions/settings, restore the successful ordinary query and close only this run's test tabs.

### BF-PERM-09 Function calls check the actual resource — P0

- Initial state: the same ordinary account has only the single-table READ baseline on a confirmed isolated test datasource. Record its grants before changing any test scope.
- Run Dameng `SELECT SF_GET_PARA_VALUE(1, 'MAX_SESSIONS') AS config_value FROM DUAL;`. Expected without instance READ: DM_QUERY for `/MAX_SESSIONS/`, not for `/CODEX_FUNCTION_PERMISSIONS/SF_GET_PARA_VALUE/`. The getter itself does not require PROGRAM. PostgreSQL `current_setting('max_connections')` and SQL Server `SESSION_CONTEXT(N'codex_context')` likewise check the real configuration key.
- Temporarily grant only READ at the isolated test datasource's instance scope through the normal authorization page. Repeat the exact getter using the ordinary account; require a successful database result. An administrator's successful query alone does not prove the ordinary account's resource authorization works.
- Restore the original single-table grant and repeat the getter. The authorization UI can consolidate descendant grants when an ancestor is granted; after removing the temporary ancestor, explicitly restore only the original test-table READ if it was consolidated. It must again deny the configuration key. Recheck an ordinary aggregate over the granted table, an ungranted-table read, and a nested custom function to prove the original boundary was restored. No PROGRAM or management privilege is added for this suite.
- Privileged functions such as ClickHouse `getSetting` and Greenplum/Cloudberry `gp_read_error_log` retain their existing CALL/PROGRAM gate as well as the actual target READ. Record the first denied permission accurately; a function PROGRAM denial must not be reported as a verified target READ denial. Their complete target/action/scope is checked separately in static authorization regression.
- For non-executing EXPLAIN, ordinary and custom function dependencies use CALL; they do not acquire function READ or execute administrative effects. File table functions that access external structure during planning keep their actual File READ. Check executing ANALYZE separately where supported.
- Cleanup: verify the saved grants match the initial baseline, restore query drafts, disconnect this run's finished sessions, and retain exact SQL/result/permission-path evidence without credentials.

## Cleanup

Restore the previous session when available. Remove this run's READ grants and disable the dedicated account, or ask the user to approve permanent deletion before deleting it. Remove only test objects explicitly designated disposable; preserve existing datasources, roles, accounts and query drafts. Record any deferred cleanup without exposing credentials.

## Skip Conditions

- Missing test datasource/version or an unavailable server: retain that engine's static regression result and record live execution as NOT RUN.
- A required login/new credential step is pending: mark low-privilege browser acceptance BLOCKED until the user completes it; administrator execution cannot replace this evidence.
- A native database denial/error proves dispatch only, not a successful ordinary-function execution.

## SQL Server permission acceptance evidence

- Local SQL Server Developer 16.0.4225.2 at `127.0.0.1:1433`; CloudDM datasource `codex_function_permissions_sqlserver`, Default environment.
- Reused the dedicated `codex_issue422_` account and query-console-only role. Saved resource preview granted only `codex_function_permissions.dbo.allowed_rows` READ; no instance/database/schema READ, PROGRAM, WRITE or management grant was added. The tree exposed only `allowed_rows` in this schema.
- Reproduced the initial failure with COUNT requiring DM_PROGRAM. Runtime datasource version was empty; version lookup now uses actual metadata for program-call checks when that field is empty. The SQL Server registry recognizes the provider's `Microsoft SQL Server 2022 (RTM...` format. Unknown releases remain protected.
- Refreshed all runtime artifacts and restarted IDEA DmAloneLauncher; backend health and embedded worker readiness passed. The final browser run used the ordinary account after restart.

| Scenario | Expected | Observed |
| --- | --- | --- |
| COUNT, SUM, MIN(LOWER(name)), ABS and DATETRUNC over allowed_rows | Successful values without PROGRAM | PASS: 2, 30, alice, 7, 2024-05-16 00:00:00 |
| ROW_NUMBER and DENSE_RANK over allowed_rows | Successful window results | PASS: Alice/1/1, Bob/2/2 |
| COUNT over denied_rows | Table READ denial | PASS: DM_QUERY on denied_rows |
| LOWER wrapping a subquery over denied_rows | Preserve nested table READ | PASS: DM_QUERY on denied_rows |
| dbo.codex_constant() | Custom-function denial | PASS: DM_PROGRAM on codex_constant |
| MIN(LOWER(dbo.codex_constant())) | Preserve nested custom-function check | PASS: DM_PROGRAM on codex_constant |
| dbo.LOWER(name) | Same-named qualified UDF denial | PASS: DM_PROGRAM on LOWER |
| dbo.[LOWER](name), [LOWER](name) | Quoted-name denial | PASS: DM_PROGRAM on LOWER |
| EXEC sys.sp_configure with no arguments | Management procedure remains protected | PASS: DM_PROGRAM on sp_configure; no configuration mutation requested |
| Reload, repeat nested-UDF negative, then repeat ordinary positive | Stable authorization and usable session | PASS: denial followed by correct aggregate/date results |

- Browser Console error list was empty for the final run. Backend logs contained the pre-existing `DsObject` close-interface diagnostic, also present before this fix; this run does not certify its resource lifecycle.
- Existing BehaviorRelationsTest and PermissionTextTest: 3643/3643 passed. Added SQL Server native-product-string and unknown-year assertions to the existing test class. Formatter, full build, local deployment and backend restart passed.
- SQL Server 2019/2022 DATETRUNC introduction and unknown-version cases passed static regression; an older SQL Server instance was NOT RUN. This evidence does not certify other engines' native behavior.
- The isolated test database, two fixture tables, constant-only functions and single-table test grant are retained for review/reproduction. The browser ends on the successful ordinary query; unrelated query drafts are preserved.

## Cross-datasource acceptance evidence

The expanded companion matrix contains 503 independent fixture blocks in 68 text files, across 23 fixture directories and 25 datasource identifiers/modes. Version variants execute **849/849 passing cases**. Coverage includes scalar strings/numbers, dates/conversions, aggregates/DISTINCT, windows, JSON/arrays, product functions, introduction/unknown-version boundaries, nested table reads, direct/nested UDFs, qualified and quoted names, and management/external functions. These static assertions are separate from native and browser execution.

The full build/local publication completed successfully. IDEA execution 81 was terminated before publication; execution 82 started with backend and embedded-worker readiness confirmed. A later capacity-limited test group used a fresh execution 83, also confirmed RUNNING and healthy, with embedded-worker plugins ready. No source changes occurred between these executions.

The same dedicated ordinary account and single-table READ grants were reused. Every browser case checked the selected query tab's datasource identifier and visible endpoint before submission. Earlier records without a confirmed target, stale tab bindings, loading views and old result grids are excluded. Positive queries used distinct result-column aliases; negative cases checked the denied resource and permission, rather than accepting any previous error.

| Datasource | Observed release | Successful ordinary expressions | Additional boundary |
| --- | --- | --- | --- |
| MySQL | 8.0.44 | COUNT/SUM/MIN(LOWER)/ABS and JSON_EXTRACT | LOAD_FILE retained PROGRAM. |
| PostgreSQL | 16.11 | COUNT/SUM/MIN(LOWER)/ABS and date_bin | pg_terminate_backend(0) retained MAINTAIN. |
| SQL Server | 16.0.4225.2 / 2022 | COUNT/SUM/MIN(LOWER)/ABS, DATETRUNC, CAST/CONVERT, ROW_NUMBER/DENSE_RANK | sys.sp_configure retained PROGRAM; refresh, nested-UDF rejection and subsequent successful ordinary query passed. |
| MariaDB | 11.4.13 | COUNT/SUM/MIN(LOWER)/ABS, JSON_EXTRACT and ADD_MONTHS | Harmless KILL QUERY retained MAINTAIN. |
| Greenplum | 7.1.0 / PG 12.12 | COUNT/SUM/MIN(LOWER)/ABS and linear_interpolate | Actual product metadata enabled its registered product function. |
| Cloudberry | 2.1.0 / PG 14.4 | COUNT/SUM/MIN(LOWER)/ABS | linear_interpolate retained PROGRAM because 2.1 is outside the audited product catalog; this is a conservative-version check, not a certified 2.1 product-function exemption. |
| CockroachDB | 24.3.0 | ROW_NUMBER, ABS and fnv32 | Native hashes matched Alice=2990309139 and Bob=541722618. |
| ClickHouse | 26.8.7.19 | COUNT/SUM/MIN(lower)/ABS and sumIf | sumIf returned 10 on the READ-granted fixture. |
| Dameng | DM8 | COUNT/SUM/MIN(LOWER)/ABS and LISTAGG | Used native uppercase fixture identifiers matching the saved READ grant. |
| TiDB | 8.5.3 | COUNT/SUM/MIN(LOWER)/ABS and APPROX_COUNT_DISTINCT | The protected codex_constant call is an unknown-function negative; this fixture does not claim native TiDB SQL UDF support. |
| StarRocks | 4.1.4 | COUNT/SUM/MIN(LOWER)/ABS, BITMAP_COUNT/BITMAP_UNION/TO_BITMAP and ROW_NUMBER | Harmless KILL QUERY retained MAINTAIN. |

All eleven datasources rejected the ungranted fixture table for DM_QUERY, a nested custom/unknown function for DM_PROGRAM, and a mixed ordinary-plus-quoted built-in call for DM_PROGRAM. Real harmless UDFs were available for all of these custom-function checks except TiDB, as noted above. Aggregate success preserved the actual input-table READ requirement.

The final browser evidence contains **54 matching permission/function outcomes**: 14 successful executions and 40 expected denials. Three intermediate attempts hit the worker's configured global session capacity of 5; they are recorded as environment errors and were retried successfully. One Dameng attempt used a lowercase table path different from the uppercase READ grant; it is recorded separately and corrected, not counted as a successful function case. Raw evidence and counts are `/tmp/all_datasource_ui_final_evidence.json` and `/tmp/all_datasource_ui_final_summary.json`.

### Session and environment limits

Closing some active test query pages did not immediately remove their QUERY session records. The local session-capacity failure therefore remains a separate unresolved lifecycle concern. Read-only log analysis for execution 82 found 101 temporary metadata sessions with 101 matching close events, plus ten QUERY sessions; this does not establish a per-call version-lookup leak. A source-level candidate is cancellation of an in-flight closeSession request when closing an active page immediately switches tabs and clears pending requests. Browser network evidence has not established the root cause. Evidence is preserved in `/tmp/native_product_regression/session_capacity_evidence.json`; no session-limit/configuration or unrelated lifecycle code was changed in this task.

The final browser log also contains three workspace-connection restoration errors. Explicitly opening/reconnecting the test connection recovered the affected flow, and the recorded permission scenarios completed. This run does not certify error-free workspace restoration or QUERY-session cleanup. The final page is left on the successful SQL Server ordinary-function query; unrelated query drafts remain.

Doris native installation remains unresolved while its original Motherdata operation cannot be queried because `/mcp` returns HTTP 404. DB2/HANA require an unavailable Linux x86_64 environment; other unavailable proprietary/cloud products retain static-only coverage. These environments are NOT RUN for native/browser acceptance.

## Classified-function acceptance after analysis-resource wiring

### BF-PERM-10 Metadata and getter/setter classification — P0

- Inspect the account's current saved READ scope before deciding which table/configuration calls should be denied. An instance READ grant also authorizes its configuration keys and tables.
- With only the intended READ scope, run VERSION on MySQL/MariaDB, TIDB_VERSION on TiDB, and has_schema_privilege on PostgreSQL/Greenplum/Cloudberry/CockroachDB, together with COUNT over the granted table. Expected: metadata classification alone does not require PROGRAM.
- On MySQL/MariaDB/TiDB, compare LAST_INSERT_ID() with LAST_INSERT_ID(1). The getter succeeds; the setter retains Session CONFIGURE/MANAGE. ADB, PolarDB-X and OceanBase MySQL follow the same rule in static regression while actual product instances are unavailable.
- Repeat an ungranted-table query, nested UDF, quoted function and harmless management/file negative. Actual resource permissions and dangerous effects remain independent of ordinary CALL exemption.
- Refresh and repeat one positive and one PROGRAM negative. Restore the original query drafts and close only task-created pages/connections; preserve pre-existing grants.

Use the same query-only account and single-table READ grants. Ordinary metadata values such as TiDB TIDB_VERSION may remain callable; classification does not imply a management permission unless the resource defines a corresponding action/target.

For each available datasource, confirm its selected query-tab source ID and visible endpoint before submitting SQL. Repeat the ordinary aggregate/scalar/product expression, an ungranted-table negative, and a nested custom function (a nested unknown function on TiDB, which has no native SQL-UDF fixture). Then exercise a harmless classified function: SQL Server SESSION_CONTEXT, MySQL/MariaDB LOAD_FILE with a nonexistent path, PostgreSQL current_setting, ClickHouse getSetting, Greenplum/Cloudberry gp_read_error_log for a nonexistent relation, CockroachDB crdb_internal.force_error, Dameng SF_GET_PARA_VALUE, TiDB RELEASE_LOCK for a lock not held by the fresh session, and StarRocks SLEEP(0). Do not substitute an actually destructive operation.

The historical classification-wiring artifacts were loaded by IDEA DmAloneLauncher execution **84**. FULL-mode startup, health and embedded-worker plugin initialization passed. The local plugin archives were inspected at their actual nested-JAR locations and matched the source classification resources plus the shared FunctionBehavior class.

That historical browser run covered SQL Server, MySQL, PostgreSQL, MariaDB, ClickHouse, Greenplum, Cloudberry, CockroachDB, Dameng, TiDB and StarRocks. It recorded **47 matching outcomes: 13 successful queries and 34 expected denials**, including a refresh/re-entry ordinary positive and nested-UDF negative. This is separate from the historical 54-outcome run above. Evidence: `/tmp/function_classification_runtime_evidence.json` and `/tmp/function_classification_runtime_summary.json`.

The run verified SESSION_CONTEXT denying the concrete configuration key, LOAD_FILE requiring DM_MAINTAIN, current_setting requiring DM_QUERY, force_error requiring DM_UNSAFE, and SLEEP/RELEASE_LOCK retaining program authorization. Its getSetting, gp_read_error_log and SF_GET_PARA_VALUE denials were incorrectly accepted on Function READ paths; those outcomes do not certify correct resource authorization and are superseded by BF-PERM-09 and the resource-target acceptance below. The complete typed target/action requirements, including session MANAGE for locks, are asserted statically; the browser normally displays the first rejected requirement. Cloudberry 2.1's product interpolation function now succeeds under the explicitly requested major-version registration. Every datasource retained the ungranted-table and nested-custom/unknown-function denials.

One ClickHouse attempt hit the existing worker session capacity of 5. The error is retained separately as ENVIRONMENT_ERROR and excluded from the 47 outcomes. Disconnecting completed task connections through the workspace menu released capacity; the classified-function retry then passed, and subsequent datasource checks completed. This run does not establish the root cause or resolution of the earlier session-lifecycle concern. Final browser Console error count was zero. The browser tools did not expose a network request trace, so HTTP status coverage is not certified separately from visible business results and application logs.

Cleanup verified that all ten newly created query tabs were removed, all task-only completed connections were disconnected, the two original query tabs remained, and the original SQL Server draft was restored and executed successfully. The final screenshots are `/tmp/function_classification_ui_success.jpg` and `/tmp/function_classification_ui_denied.jpg`. Products without a usable actual instance, including Oracle/YashanDB, Db2, HANA, Gauss and the unavailable proprietary/cloud modes, remain static-only in this run.

A subsequent minimality audit removed repeated parsing/classification lookups, duplicate identifier normalization, unused wrappers and unreachable branches. The attempted removal of blank-version discovery was rejected after it caused SQL Server CAST to require DM_PROGRAM; the original discovery path was retained. Final permission (1,354), MySQL (1,239) and companion function (956) checks passed, including new qualified/quoted Dameng and escaped-backtick Doris negatives. The rebuilt runtime, IDEA execution 86, passed health and worker readiness; a focused SQL Server/MariaDB/Greenplum browser run then passed 11 outcomes (4 successful queries and 7 expected denials), with zero Console errors. Both original tabs and the original SQL Server draft were preserved. Evidence: `/tmp/function_minimality_verified_junit.json`, `/tmp/function_minimality_runtime_evidence.json` and `/tmp/function_minimality_ui.jpg`. Doris quote-identity coverage in this pass is static, not native acceptance.

## Resource-target authorization acceptance

The final source follows PG CALL dependencies plus separate resource actions. Runtime execution 87 verified the full configuration authorization cycle; execution 88 then loaded the final permission-message correction and completed the remaining checks. The correction makes the displayed denied path use the same `BehaviorRelations.resourcePath` as the actual authorization check, including quoted names. Both executions reached RUNNING, health `ok`, and embedded-worker readiness. Final execution 88 is retained.

This run recorded **90 certified terminal outcomes on eleven actual products: 26 successful queries and 64 expected denials**. Each case checked the active query-tab datasource ID and endpoint, the exact selected SQL, and the actual result or denied resource/permission. Incomplete/loading captures were excluded and repeated to a terminal result. The two original query tabs and original SQL Server draft were retained, all newly opened query tabs were removed, and finished test connections were disconnected. Final browser Console error count was zero.

| Datasource | Observed product/kernel release | Successful queries | Correct denials |
| --- | --- | ---: | ---: |
| MySQL | 8.0.44 | 2 | 5 |
| PostgreSQL | 16.11 | 2 | 6 |
| SQL Server | 2022 / 16.0.4225.2 | 3 | 7 |
| MariaDB | 11.4.13 | 2 | 5 |
| Greenplum | 7.1.0 / PG 12.12 | 2 | 5 |
| Cloudberry | 2.1.0 / PG 14.4 | 2 | 5 |
| CockroachDB | 24.3.0 | 2 | 5 |
| ClickHouse | 26.8.7.19 | 2 | 4 |
| Dameng | 8 | 4 | 10 |
| TiDB | 8.5.3 | 2 | 5 |
| StarRocks | 4.1.4; MySQL compatibility version 8.0.33 | 3 | 7 |

- Ordinary positives cover numeric/string/aggregate calls and windows on every available product; additional expressions include MySQL JSON_EXTRACT, PostgreSQL date_bin, SQL Server CAST/CONVERT/DATETRUNC, MariaDB ADD_MONTHS/JSON_DETAILED, GP/Cloudberry linear_interpolate, CockroachDB fnv32, ClickHouse sumIf, TiDB APPROX_COUNT_DISTINCT and StarRocks bitmap aggregates.
- Dameng `SF_GET_PARA_VALUE(1, 'MAX_SESSIONS')` first denied `/MAX_SESSIONS/` for DM_QUERY. The same ordinary account then returned `10000` after only temporary instance READ was granted. The temporary grant was removed, the original permanent READ on `CODEX_FUNCTION_PERMISSIONS.ALLOWED_ROWS` was restored, and configuration/ungranted-table denial plus granted-table success were rechecked. PROGRAM and management rights were never granted.
- PostgreSQL current_setting and SQL Server SESSION_CONTEXT denied the concrete configuration keys. ClickHouse getSetting and GP/Cloudberry gp_read_error_log correctly retained their first CALL/PROGRAM gate; their additional target READ remains separately asserted statically. These PROGRAM denials are not presented as live proof of the target READ denial.
- Real custom functions, nested calls, qualified names and quoted names remained protected. Existing constant UDFs were independently revalidated natively on ten products; TiDB's negative remains explicitly an unknown-call check. MySQL's actual UI fixture is `test.codex_function_allowed_rows` / `test.codex_function_constant`, separately verified from the other isolated MySQL catalog.
- StarRocks ordinary EXPLAIN returned an actual plan; EXPLAIN custom/FILES calls retained PROGRAM. Dameng EXPLAIN CHECKPOINT retained a CALL/PROGRAM dependency without executing checkpoint effects. Direct management/control negatives remained protected.

Three logical attempts encountered the pre-existing worker session limit of 5 (CockroachDB, ClickHouse, SQL Server). They are retained as ENVIRONMENT_ERROR, excluded from the 90 outcomes, and were retried to verified terminal results after test connection cleanup/idle recovery. This run does not claim to fix or determine the root cause of that session-capacity behavior. The tools did not expose a network request trace, so HTTP status coverage is not certified independently of visible business results and application logs.

Evidence: `/tmp/function_resource_auth_runtime_evidence.json`, `/tmp/function_resource_auth_runtime_summary.json`, `/tmp/function_resource_auth_environment_attempts.json`, `/tmp/function_resource_auth_native.json`, `/tmp/function_resource_auth_native_udf.json`, and the before/allowed/final-denied/final-success screenshots under `/tmp/function_resource_auth_*.png`. The allowed screenshot records a temporary test grant, not the final account grant state. Products without an actual usable instance, including Oracle/YashanDB, Db2, HANA, Gauss, Doris and the unavailable proprietary/cloud modes, remain static-only; a compatible engine was not counted as native product acceptance.

## Acceptance after ordinary-function rule unification

The refreshed artifacts were byte-checked against all 42 changed production classes/resources, including nested plugin libraries. The removed Dameng system-callable catalog is absent from the runtime archives. Full build/local publication and native IDEA formatting passed. DmAloneLauncher executions 89 and 90 reached RUNNING, FULL-mode startup, health `ok` and embedded-worker readiness; execution 90 is retained.

Eleven existing native products passed 33 read-only SELECT probes. Browser verification on those products recorded **55 terminal SQL outcomes: 18 successful queries and 37 authorization denials**. This includes metadata calls together with granted-table aggregates, MySQL/MariaDB/TiDB LAST_INSERT_ID getter/setter distinctions, ungranted tables where the account has table-only READ, nested UDFs, quoted Dameng functions and harmless management/file calls. Refresh/re-entry repeated SQL Server positive and PROGRAM-negative cases. TiDB's UDF case remains an unknown-call permission check.

The current account has permanent **instance-level DQL on Dameng**, confirmed in the saved authorization UI. Its SF_GET_PARA_VALUE call returned 10000 and DENIED_ROWS was readable under that existing grant. These are authorized successes, not ungranted-table denial evidence. Dameng UDF, quoted COUNT and EXPLAIN CHECKPOINT still denied PROGRAM. The concrete ConfigKey/table READ dependencies and ungranted-table boundaries remain covered statically. No authorization was changed to force a browser result.

Three Cloudberry attempts in execution 89 hit the existing five-session limit; all were excluded and rerun successfully after a precise restart. Cleanup thereafter selected the original page before closing inactive test pages; no further capacity error occurred in execution 90. This does not establish or fix the session lifecycle cause. A separate attempt to open the pre-existing local Dameng source 40 failed before any SQL was sent; it is excluded from SQL outcomes and accounts for the one captured browser Console error. The tools do not expose a network trace, so HTTP status coverage is not independently certified.

The two original query pages and original SQL Server draft were preserved. Task-created query pages, completed connections and the temporary administrator browser page were closed. No database objects or grants changed. MaxCompute was explicitly deferred; unavailable native/cloud products retain static-only coverage.

Evidence: `/tmp/unified_function_ui_summary.json`, `/tmp/function_metadata_native_20261106.json`, `/tmp/unified_function_artifact_verification.json`, `/tmp/execution89_session_readonly_evidence.json`, `/tmp/unified_function_ui_success.png`, `/tmp/unified_function_ui_denied.png`, and `/tmp/unified_function_dameng_current_grant.png`.
