# MySQL functional index table details

## Purpose

Prevent table details from failing with a null JSON Map key when MySQL returns a functional index key part. Preserve all key parts and their order, and keep expression-based uniqueness separate from column-based row identification.

## Scope

- SQL Query at `/#/sql`: database selection, single-click table expansion, columns, primary keys, and indexes.
- MySQL `detailLeaf`, index metadata conversion, and JSON serialization.
- Functional unique indexes, mixed column/expression indexes, ordinary unique indexes, and non-unique functional indexes.
- Creating or modifying expression indexes through the table editor is outside this workflow.

## Preconditions

- Build the changed MySQL metadata module and publish the current plugin artifacts with `cd package && ./all_build.sh`.
- Start `DmAloneLauncher` through IDEA and verify the current MySQL plugin is loaded.
- Use the in-app Browser at `http://localhost:8222` with an account permitted to browse data sources and query table metadata.
- Use an isolated MySQL 8.0.13+ instance. Give the browser test connection SELECT access to the fixture schema; do not use a production instance.

## Test Data

Create a fresh schema named `codex_functional_<unique_suffix>` in the isolated instance. Select it before running these statements:

```sql
CREATE TABLE test_soft_delete (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    privilege_id BIGINT NOT NULL,
    deleted_at DATETIME DEFAULT NULL,
    UNIQUE KEY uk_role_privilege_active (
        ((CASE WHEN deleted_at IS NULL THEN role_id ELSE NULL END)),
        ((CASE WHEN deleted_at IS NULL THEN privilege_id ELSE NULL END))
    )
);

CREATE TABLE mixed_first (
    tenant_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    deleted_at DATETIME,
    UNIQUE KEY uk_mixed (
        tenant_id, ((CASE WHEN deleted_at IS NULL THEN role_id ELSE NULL END))
    )
);

CREATE TABLE mixed_last (
    tenant_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    deleted_at DATETIME,
    UNIQUE KEY uk_mixed (
        ((CASE WHEN deleted_at IS NULL THEN role_id ELSE NULL END)), tenant_id
    )
);

CREATE TABLE ordinary (
    id BIGINT PRIMARY KEY,
    label VARCHAR(100),
    second_id BIGINT,
    UNIQUE KEY uk_columns (label(20), second_id),
    KEY idx_label (label(10))
);

CREATE TABLE expression_normal (
    id BIGINT PRIMARY KEY,
    role_id BIGINT,
    KEY idx_expression (((ABS(role_id)))),
    KEY idx_mixed (id, ((ABS(role_id))))
);
```

## Suites

### MFI-SMOKE-01 / Smoke and Main Flow (P0)

- Initial state: SQL Query is open and the isolated connection is visible in the data source tree.
- Data: `test_soft_delete` in the fresh fixture schema.
- Steps: Expand the test connection; double-click the fixture schema to open its query tab; single-click `test_soft_delete` in the table list.
- Expected: The table expands to show `id`, `role_id`, `privilege_id`, `deleted_at`, primary key `PRIMARY`, and index `uk_role_privilege_active`. No error notification or `JsonMappingException` appears. The table is loaded without changing existing query drafts.
- Restore: Leave the table expanded for the next scenario; do not submit DDL or DML.

### MFI-BOUNDARY-01 / Mixed Key Parts and State Consistency (P0)

- Initial state: The fixture schema query tab is selected at `/#/sql`.
- Data: `mixed_first` and `mixed_last`.
- Steps: Single-click each table in turn; inspect its columns and `uk_mixed` index. Through the real JDBC metadata service, compare each index's ordered key parts with `INFORMATION_SCHEMA.STATISTICS`, ordered by `SEQ_IN_INDEX`.
- Expected: Both tables expand without errors. The metadata preserves the ordinary column and expression in database order, marks the index as containing expressions, and retains its unique index type. Neither index becomes a column-based unique key; `tenant_id` is not marked unique for row identification.
- Restore: Keep the fixture data unchanged.

### MFI-REGRESSION-01 / Ordinary and Non-Unique Indexes (P1)

- Initial state: The fixture schema query tab is selected.
- Data: `ordinary` and `expression_normal`.
- Steps: Single-click each table; inspect the primary key and index names. Compare metadata key parts and prefix lengths with the actual database statistics.
- Expected: `ordinary` shows `PRIMARY`, `uk_columns`, and `idx_label`, with key-part order and prefix lengths 20 and 10 preserved. `expression_normal` shows `PRIMARY`, `idx_expression`, and `idx_mixed`; both expression indexes serialize successfully and retain every key part. Ordinary indexes do not acquire the expression flag.
- Restore: Keep the fixture data unchanged.

### MFI-LIFECYCLE-01 / Refresh and Repeat (P1)

- Initial state: The fixture schema query tab is selected and `test_soft_delete` is expanded.
- Steps: Refresh Browser, wait for the table list to load, and single-click `test_soft_delete` again. Switch to another fixture table and return. Inspect Browser Console errors and the relevant table-detail request when network inspection is available.
- Expected: The selected schema and connection are restored; the table list loads; each table can be expanded again with the same column and index names. No null-key serialization error, persistent loading state, or new relevant Console error appears.
- Restore: Close only the fixture query tab.

## Cleanup

- Remove only the temporary connection created for this workflow and close its query tab.
- Stop and remove the disposable database container, or drop only the uniquely named fixture schema if using a shared test instance.
- Preserve pre-existing data sources, schemas, tables, and query drafts.

## Skip Conditions

- MySQL below 8.0.13 cannot create these functional-index fixtures; run ordinary-index coverage and record the missing functional coverage.
- If Browser login, service readiness, or an isolated data source is unavailable, mark Browser acceptance blocked. Direct JDBC `detailLeaf` and JSON serialization checks provide backend evidence but do not replace Browser acceptance.
- If Browser does not expose network inspection, verify the visible table detail and Console errors, and record that HTTP status inspection was unavailable.
- Layout, permission changes, concurrent database writes, and table-editor expression DDL are outside this metadata loading regression.
