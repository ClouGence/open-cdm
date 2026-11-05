# Built-in SQL function authorization

Current registration uses product major-version families. Minor and patch versions in historical acceptance records below are observed server versions, not separate permission families. Unknown versions use the enum default and still require a matching registered catalog entry.

Issue: [#422](https://github.com/ClouGence/open-cdm/issues/422).

Ordinary built-in scalar, aggregate and window functions do not require `DM_PROGRAM`. Their input tables, nested custom functions, explicitly qualified user functions, stored procedures and administrative operations retain authorization. Exemption is attached to a known built-in call, never to every `Function` object or every `SELECT` statement.

## Datasource inventory

The table follows the actual `bindSqlEngine` registrations. Compatible products inherit the selected engine's function facts; this is not a claim that every vendor extension or server version implements identical functions.

| Engine family | Datasources / modes | Function handling |
| --- | --- | --- |
| MySQL | MySQL, PolarDB MySQL, GoldenDB MySQL, KingbaseES MySQL, YashanDB MySQL | Existing MySQL versioned catalog; vendor-specific extensions are not assumed to be MySQL built-ins. |
| MariaDB | MariaDB | Conservative shared ordinary-function baseline plus a product-versioned supplemental/window-function catalog; no leakage into MySQL. |
| PostgreSQL | PostgreSQL, PolarDB PostgreSQL, KingbaseES PostgreSQL | Explicit PostgreSQL 11–18 catalog facts; administrative, large-object and sequence mutation functions are excluded from ordinary exemptions. PostgreSQL versions below 11 have not been added. |
| PostgreSQL derivatives | Greenplum, Cloudberry, Hologres, CockroachDB | Product-specific registries; Hologres ordinary aggregates and CockroachDB ordinary scalar/aggregate/window facts supplement PostgreSQL. Management and optional extensions remain protected. |
| T-SQL | SQL Server, KingbaseES SQL Server | Supplement ordinary scalar/aggregate/window names, including `COUNT`; only unqualified built-in calls are exempt. |
| Oracle | Oracle, GoldenDB Oracle, KingbaseES Oracle | Official ordinary SQL function catalog; scan generic calls for custom-function authorization, including calls inside data changes. Packages remain program objects. |
| OceanBase | MySQL mode; Oracle mode | Each mode uses its own product-versioned permission registry and ordinary-function facts; an Oracle compatibility label alone does not identify an OceanBase product release. |
| Doris | Doris, SelectDB | Existing Doris function catalog and separate external/table-function behavior. |
| StarRocks | StarRocks | Register its own SPI name, ordinary functions from release registrations, and minimum release versions. |
| ClickHouse | ClickHouse | System-origin function snapshot, exact versus case-insensitive names, introduction versions, aggregate combinators; custom/executable functions are not included. |
| HANA | SAP HANA | Expand the existing unquoted/unqualified ordinary built-in list; retain custom call behavior. |
| Dameng | DM8 | Existing catalog; only ordinary `CALL` functions receive exemptions, not management actions. |
| Gauss | GaussDB, GaussDB for openGauss | Ordinary function names from the openGauss reference checked against built-in registrations; capture other generic calls as program objects. |
| MaxCompute | MaxCompute | Ordinary category references; capture custom and explicitly qualified calls as program objects. |
| MySQL derivatives | AnalyticDB MySQL, PolarDB-X | Own engine SPI plus product-owned ordinary-function catalogs: XIHE approximate/bitmap aggregates and PolarDB-X time/partition-date/vector calculations. Nested and qualified UDFs remain protected. |
| DB2 | Db2 for i, Db2 for z/OS | Accept built-in keyword names, `COUNT(*)`, zero-argument functions, qualification and common `OVER (...)` clauses; capture custom calls. |
| Non-relational | Elasticsearch, MongoDB, Redis, Valkey, Kafka, Etcd | No ordinary relational-function exemption is introduced. Script execution, commands and administrative operations retain their existing authorization. |
| ISO fallbacks | SQL-92, SQL-99, SQL-2003 | Existing fallback behavior remains; they do not acquire a blanket program-object exemption. |

## Catalog sources and boundaries

Catalog headers record their owning upstream references. The checked-in resources contain names and classification facts, not upstream documentation text. No generation script is required at runtime.

- SQL Server: Microsoft SQL function reference; rowset, AI/external and administrative entry points are excluded. Edition/version support remains a database decision. SQL Server user functions require schema qualification, which is never exempted by this list.
- Oracle: Oracle Database 19c SQL Language Reference, Functions chapter. Packages and external-file/table-function entry points are excluded.
- HANA: SAP HANA 2.0 SPS05 SQL Reference section 4.9. Key extraction, conversion-table access and table functions are excluded.
- Db2: IBM Db2 SQL Reference Volume 1, built-in aggregate/scalar functions, plus OLAP functions. This does not promise complete Db2 expression grammar or every edition-specific function signature.
- Gauss: openGauss 6.0 ordinary function categories, cross-checked with `builtin_funcs.ini`; administrative and external-resource functions are excluded. A live Huawei GaussDB compatibility matrix remains separate from this static coverage.
- MaxCompute: official date/math/string/aggregate/window/array/map/struct/JSON/encryption/network category references. External-resource and table-valued entry points are excluded.
- StarRocks: actual `functions.py` and `FunctionSet.java` registrations from maintained release branches. Unknown server versions are conservative.
- ClickHouse: `system.functions` with `origin = 'System'` on 26.9.5.2, preserving `case_insensitive`, `is_aggregate`, aliases and `introduced_in`. Missing introduction facts default conservatively to the snapshot release unless an official release reference establishes an earlier window-function version. `system.aggregate_function_combinators` supplies the known suffixes; the complete 15-name list was additionally read from a real 26.8.7.19 server, establishing explicit 26.8 facts without inferring earlier release ranges. Dictionary, AI, introspection, file/network and management entry points retain checks.

These are authorization catalogs, not a promise that every listed name/overload parses or executes on every server. Unknown names and unsupported vendor additions remain protected until their built-in status is established. Real execution on a target version and static parsing/permission assertions are recorded separately.

## Compatible-product extension audit

A compatible wire protocol or SQL engine does not prove identical function catalogs. The follow-up audit compares the actual engine bindings with product references and retains extensions in the owning product's registry.

| Product | Confirmed extension handling | Source / boundary |
| --- | --- | --- |
| PolarDB-X | `polardbx-built-in-functions.json`: 10 ordinary date/TSO-conversion/vector functions added. | [Pinned upstream scalar registrations](https://github.com/polardb/polardbx-sql/tree/4b260597d931b98e8269bf1a2cae9d9dccc1591c/polardbx-optimizer/src/main/java/com/alibaba/polardbx/optimizer/core/function/calc/scalar), checked against `TddlOperatorTable`; sequence, lock, routing/table-name metadata, LBAC, allocator, AI and key-management functions excluded. Native vector availability still depends on the server build. |
| AnalyticDB MySQL | `adb-built-in-functions.json`: ordinary approximate and Roaring Bitmap functions added to the XIHE/MySQL engine. | [Approximate functions](https://help.aliyun.com/en/analyticdb/analyticdb-for-mysql/developer-reference/approximate-function), [Roaring Bitmap functions](https://help.aliyun.com/en/analyticdb/analyticdb-for-mysql/developer-reference/roaring-bitmap-functions); Spark SQL, UNNEST, remote UDF invocation and external-table setup are not covered by this catalog. |
| Hologres | `hologres-built-in-functions.json`: 5 ordinary aggregate extensions, recognized consistently by the behavior visitor and registry. | [General-purpose aggregates](https://www.alibabacloud.com/help/en/hologres/developer-reference/general-purpose-aggregate-functions). Optional installed extensions and `hg_*` management/table-property operations are not broadly exempted. |
| MariaDB | `mariadb-built-in-functions.json`: 62 supplemental ordinary/window-function facts. Parser facts and permission facts share the same product-versioned list. | [Function and operator reference](https://mariadb.com/docs/server/reference/sql-functions/function-and-operator-reference) and its linked function pages; conservative baseline 10.6, later additions gated individually. Sequence mutation, replication waits and Spider/plugin execution are excluded. |
| OceanBase MySQL | `oceanbase-mysql-built-in-functions.json`: 82 scalar/aggregate extensions and an engine-name-matched SPI. | [Pinned V4.3.5 reference](https://github.com/oceanbase/oceanbase-doc/tree/2a5ae3a23a51729d065cec716508eb26cb064106/en-US/700.reference/500.sql-reference/100.sql-syntax/200.common-tenant-of-mysql-mode/400.functions-of-mysql-mode). UNNEST, RB_ITERATE, GENERATOR, internal/trace/transaction metadata and KMS calls excluded. |
| CockroachDB | `cockroachdb-built-in-functions.json`: ordinary native functions from the v24.3 reference supplement PostgreSQL and the earlier five-name list. | [v24.3 functions and operators](https://www.cockroachlabs.com/docs/v24.3/functions-and-operators); system, sequence, internal, table-valued, spatial DDL and table-name-based extent calls excluded. These are native CockroachDB functions, not an exemption for PostgreSQL extensions. |
| TiDB | Already has its own product function registry, including approximate, vector and TSO families. | `TiResourceRegistry`; no additional blanket exemption is introduced. |
| Greenplum | `greenplum-built-in-functions.json`: 75 ordinary scalar/aggregate facts, shared by behavior analysis and authorization. | [Pinned upstream catalog](https://github.com/greenplum-db/gpdb-archive/blob/482967c1b49028cf072c15935462f75bc3e4b045/src/include/catalog/pg_proc.dat). Includes array, complex arithmetic/conversion, data hashing, interpolation and native aggregation; `gp_array_agg` requires PostgreSQL kernel >=12 (GP7), not the optional GP6 extension. |
| Cloudberry | `cloudberry-built-in-functions.json`: 75 ordinary scalar/aggregate facts, shared by behavior analysis and authorization. | Released [1.6 catalog](https://github.com/apache/cloudberry/blob/1a40e1f208c54be48e05bbdada21493edd9a1026/src/include/catalog/pg_proc.dat) and [2.0 catalog](https://github.com/apache/cloudberry/blob/8178d4faefeca459f7ef2dd3aa502f23e0d7a5c4/src/include/catalog/pg_proc.dat); includes SM3 and common value operations. Version-dependent internal parallel aggregate entries are not treated as shared 1.x/2.x public functions. |
| OceanBase Oracle | `oceanbase-oracle-built-in-functions.json`: 19 ordinary function facts supplement the Oracle baseline, including the exact `WMSYS.WM_CONCAT` spelling. | [Pinned V4.3.5 documentation](https://github.com/oceanbase/oceanbase-doc/tree/2a5ae3a23a51729d065cec716508eb26cb064106/en-US/700.reference/500.sql-reference/100.sql-syntax/300.common-tenant-of-oracle-mode/500.functions-of-oracle-mode). Handles plain/DISTINCT/window WM_CONCAT and quoted-call permission boundaries. No new SQL engine option. |
| Other shared-engine compatibility modes | MySQL/PG/Oracle/T-SQL baseline verified by engine registration; vendor-specific extensions are **not certified complete**. | PolarDB MySQL/PG, GoldenDB, KingbaseES currently reuse the base engine identity. A vendor-only name must not be added globally to that base catalog. |

The audit establishes concrete extension facts and exclusions; it does not certify every vendor release or overload. Unknown extensions keep their permission check. Motherdata exposes a PolarDB-X image but `get_installation_spec(dsTypeId=30)` returns `DATABASE_INSTALL_SPEC_UNAVAILABLE`. Localdb fallback is permitted by the current project rules when Motherdata cannot supply a test environment, but no replacement PolarDB-X instance has been validated in this run; its native acceptance remains NOT RUN.

## Repeatable verification

The existing `BehaviorRelationsTest` covers scalar/aggregate/window calls across real engines, table READ preservation, unknown and schema-qualified functions, nested calls, and custom calls in INSERT/UPDATE/DELETE. It also checks the privileged-function boundary and ClickHouse aggregate combinators/case sensitivity. No additional test class is needed.

```sh
cd backend
./gradlew :s-test:test --tests '*BehaviorRelationsTest' --tests '*PermissionTextTest'
```

The companion `open-cdm-test` suite includes explicit built-in/UDF fixtures and registers the newly checked engine families. Its PostgreSQL administrative-function expectation follows the required authorization boundary.

```sh
cd ../open-cdm-test
./gradlew runTests -PtestArgs='--domain=behavior --resource=builtin_function_authorization --workers=8'
```

A full source build/local publication and IDEA backend restart are required before runtime acceptance. The browser workflow is [ordinary function authorization](../../tests/frontend/security/builtin_function_authorization.md). Static tests do not replace a low-privilege CloudDM session or database-native execution.

### Unified static regression results

The latest complete static checkpoint ran the full `s-test` suite, MySQL module tests and the affected production-module compilation tasks. It precedes the final YashanDB isolation correction, whose separate final regression is recorded below:

```sh
cd backend
./gradlew :s-test:test :sql-mysql:test :sql-sqlserver:compileJava :sql-doris:compileJava :cgdm-console:compileJava :ds-adb:compileJava :ds-oceanbase:compileJava :ds-polardb:compileJava :ds-greenplum:compileJava :ds-cockroachdb:compileJava :ds-clickhouse:compileJava --continue --console=plain
```

| Scope | Result | Interpretation |
| --- | --- | --- |
| Complete `s-test` suite | 11,884 passed; 0 failed; 0 skipped | Includes permission, behavior, security-domain, rule, metadata, completion and template regressions. |
| `sql-mysql:test` | 1,239 passed; 0 failed; 0 skipped | Includes MySQL version/resource tests and MariaDB grammar compatibility tests. |
| Earlier individual test discovery for `sqlc-common`, `sql-postgres`, `sql-db2`, `sql-hana`, `sql-doris`, `sql-oracle`, `sql-sqlserver` | `NO-SOURCE`; relevant production modules compiled successfully | These modules have no independent test sources. This status is not a separate test-suite pass; their engine behavior is exercised by `s-test`. |

The full formatter and `git diff --check` also passed. This section records static verification only; it does not establish database-native execution or low-privilege browser acceptance for any additional server. The checkpoint ran in 3 minutes 31 seconds; its exact per-class JUnit counts and module task results are retained in `/tmp/all_datasource_final_full_verified_summary.json`, with the complete log in `/tmp/all_datasource_final_full_regression_verified.log`. The preceding run retained one failure for the old unquoted `COUNT` fixture path; only its raw backtick path was corrected, keeping the SQL and both PROGRAM assertions unchanged.

The shared function-permission matrices are intentionally reported separately from the total test count:

| Matrix | Current coverage | Remaining limit |
| --- | --- | --- |
| Ordinary scalar, aggregate and window expressions with table READ preserved | 28 datasource identifiers, six common expression forms | Shared-engine identifiers are not independent server/version executions. MaxCompute expressions without verified availability deliberately retain PROGRAM. |
| Custom, nested and schema-qualified functions | 22 datasource identifiers, plus product-specific checks | The shared matrix does not cover every qualification form on every dialect; ClickHouse custom-schema forms are excluded where the grammar does not support them. |
| Unknown versions | 13 datasource identifiers, plus direct registry assertions | Does not simulate every vendor's raw metadata format or every unsupported release. |
| Quoted built-in names | 12 common-name datasource cases and seven product-specific quoted calls, plus direct registry assertions | Does not certify every engine's quoting syntax. |
| Introduction/removal and product-version boundaries | Explicit PostgreSQL, SQL Server, Oracle, MariaDB, CockroachDB, Hologres, AnalyticDB and derivative cases | Oracle, Db2, HANA, Gauss, Doris, ClickHouse and StarRocks still lack exhaustive introduction-boundary assertions for every catalog entry. |
| Administrative boundary | General action mapping, PostgreSQL administration, ClickHouse exclusions, MariaDB management/MySQL-only negatives and product-specific checks | Not an exhaustive inventory of administrative calls on every datasource. |

The expanded run exposed and corrected these concrete regressions without adding test classes:

- **PostgreSQL 11 compatibility:** the new guard rejected the existing Hologres `11.3` kernel path, including `pg_catalog.abs`. PostgreSQL 11 is now an explicit version family, and 2,246 existing function names receive an `11` fact only where they occur in the [pinned PostgreSQL 11 catalog](https://github.com/postgres/postgres/blob/170e416034ec0231d9e9238f2577eeb76ca8d181/src/include/catalog/pg_proc.dat). Matching metadata/behavior/privileged facts were added separately. `date_bin` remains unavailable before 14; unknown/future versions remain protected. **No PostgreSQL catalog below 11 has been added**, including the older compatibility kernels used by some derivatives.
- **PostgreSQL backup controls:** `pg_start_backup` and `pg_stop_backup` retain PROGRAM on 11–14, consistent with their [official backup-control semantics](https://www.postgresql.org/docs/14/functions-admin.html#FUNCTIONS-ADMIN-BACKUP). Ordinary-function registration does not exempt them.
- **Dameng metadata format:** the guard now recognizes `DM Database Server x64 V8` and `DM Database Server 64 V8` before checking DM8 facts. The former is the `SVR_VERSION` format in the [official `V$INSTANCE` example](https://eco.dameng.com/document/dm/zh-cn/pm/dynamic-management.html). Unknown versions and a future `V9` label are not converted to DM8.
- **MariaDB baseline versus product facts:** recognizable MariaDB releases use the existing common grammar baseline, limited to ordinary names also present in the MySQL 5.6 catalog. This restores common `COUNT`, `SUM`, `MIN`, `LOWER` and `ABS` checks without assuming MySQL 8 additions are MariaDB functions. Supplemental functions continue to use the actual MariaDB version; the [official window-function list](https://mariadb.com/docs/server/reference/sql-functions/special-functions/window-functions/window-functions-overview) supplies explicit facts for ranking/navigation functions such as `ROW_NUMBER`, `DENSE_RANK`, `NTILE`, `LAG` and `LEAD`. Assertions cover the raw `11.4.13-MariaDB-ubu2404` format, the `5.5.5-` prefix, unknown/future versions, quoted/custom calls, management functions and unsupported MySQL-only names such as `ANY_VALUE`.
- **MariaDB test-plan correction:** the shared MySQL FLUSH positive contained `FOR CHANNEL` and `OPTIMIZER_COSTS`. MariaDB's [documented FLUSH syntax](https://mariadb.com/docs/server/reference/sql-statements/administrative-sql-statements/flush-commands/flush) uses a bare connection name and has no `OPTIMIZER_COSTS` option. The original SQL and MySQL positive remain; the MariaDB case now explicitly asserts syntax rejection instead of silently dropping the case or relaxing production grammar.

### Final YashanDB version-isolation regression

A final source check found that the generic registry OR passed YashanDB's product version `23.4.7.106` into the Oracle registry, where the leading `23` was interpreted as Oracle 23. Datasource-specific registries now take precedence; YashanDB is the only datasource-level registry binding in the current source tree. Its owner checks bare product versions only against its own audited catalog. Only an explicitly branded `Oracle Database ... Release ...` input can delegate Function checks to Oracle; neither bare `19` nor a YashanDB `23.x` product value establishes an Oracle compatibility version. Non-Function virtual-resource checks retain the existing DUAL exemption without using a function version.

The final targeted run followed the successful 11,884-case checkpoint and the complete Java formatter:

```sh
cd backend
./gradlew :s-test:test --tests '*BehaviorRelationsTest' --tests '*YashanDBOracleMetaProviderDmTest' --tests '*YashanDBCompatibilityModeTest' :cgdm-plugin-loader:compileJava :cgdm-console:compileJava :ds-yashandb:compileJava --continue --console=plain
```

| Final targeted scope | Result |
| --- | --- |
| `BehaviorRelationsTest` | 1,203 passed |
| `YashanDBOracleMetaProviderDmTest` | 16 passed |
| `YashanDBCompatibilityModeTest` | 5 passed |
| Total | 1,224 passed; 0 failed; 0 skipped |
| Plugin loader, console and YashanDB production compilation | Successful |

Assertions include product `23.4.7.106` ordinary-function/WM_CONCAT positives, Oracle-catalog-only `APPROX_COUNT` and `ANY_VALUE` negatives, unverified `23.2`/`23.5`, unknown and bare-19 negatives, explicit Oracle-19 compatibility positives, quoted/qualified/UDF boundaries and DUAL preservation. This run does not establish live YashanDB server acceptance. Exact JUnit counts are retained in `/tmp/all_datasource_yashan_final_verified_summary.json`; the log is `/tmp/all_datasource_yashan_final_regression.log`. The complete formatter and `git diff --check` passed after the final production changes.

### Native database regression evidence

Database-native execution was expanded beyond SQL Server. The fixtures are isolated `codex_function_permissions` databases/schemas containing `allowed_rows` (Alice/10, Bob/20), `denied_rows` (Hidden/99), and a harmless SQL UDF where the product supports one. These native results establish function availability and returned values; CloudDM authorization and browser acceptance are recorded separately in the [browser workflow](../../tests/frontend/security/builtin_function_authorization.md).

| Product | Observed server version / environment | Native result and evidence |
| --- | --- | --- |
| Greenplum | Greenplum Database 7.1.0 build dev, PostgreSQL 12.12; localdb cached image | Ordinary aggregate/scalar and window queries, a constant UDF and qualified same-named UDF succeeded. `linear_interpolate(5,0,0,10,10)` returned `5`; `complex(1,2)::text` returned `1 + 2i`. Host-port connection was also checked. Evidence: `/tmp/native_product_regression/greenplum_*.log`. |
| Cloudberry | Apache Cloudberry 2.1.0-incubating build bdf90c5518f, PostgreSQL 14.4; localdb cached image | The same native aggregate/scalar/window/UDF, interpolation and complex-value queries succeeded, and the host-port connection was checked. **This does not add a 2.1 permission exemption:** the product supplement retains only its audited 1.6/2.0 facts; a working native 2.1 function or a PG 14.4 kernel is not proof of a registered 2.1 product release. Evidence: `/tmp/native_product_regression/cloudberry_*.log`. |
| CockroachDB | CockroachDB CCL v24.3.0, linux arm64; localdb fallback because the Motherdata installation spec is unavailable | Ordinary aggregate/scalar/window queries and real SQL UDFs succeeded. `fnv32('Alice')` returned `2990309139`, and `fnv32('Bob')` returned `541722618`, matching an independent FNV-1 calculation. Evidence: `/tmp/native_product_regression/cockroach_*.log`. |
| MariaDB | 11.4.13-MariaDB-ubu2404; localdb fallback after Motherdata install_run:85 failed | A read-only recheck over the isolated fixture returned COUNT=`2`, SUM=`30`, MIN(LOWER(name))=`alice` and ABS(-7)=`7`. Evidence: `/tmp/native_product_regression/mariadb_readback.log`. This readback is additional to the 32-assertion file below. |
| ClickHouse | 26.8.7.19; Motherdata install_run:86 succeeded | Ordinary aggregate/scalar, window, constant-UDF and `sumIf` queries succeeded; the published HTTP endpoint returned COUNT/SUM=`2/30`. The full aggregate-combinator table and five additional combinator queries were checked separately. Evidence: `/tmp/native_product_regression/clickhouse_*.log` and the JSON files below. |
| Dameng | `DM Database Server x64 V8`; Motherdata install_run:87 succeeded | The real `SVR_VERSION` string was read, then ordinary aggregate/scalar, window and constant-UDF calls succeeded. LISTAGG returned `Alice,Bob`. Schema: `CODEX_FUNCTION_PERMISSIONS`. Evidence: `/tmp/native_product_regression/dameng_*.log`. |
| TiDB | 8.0.11-TiDB-v8.5.3, unistore test storage; localdb fallback after Motherdata install_run:88 failed | Ordinary aggregate/scalar/window queries succeeded and APPROX_COUNT_DISTINCT returned `2`. SQL CREATE FUNCTION was rejected with native error 1064; no real SQL-UDF fixture is claimed. This is function-execution evidence, not acceptance of a production TiKV cluster. Evidence: `/tmp/native_product_regression/tidb_*.log`, including `tidb_custom_creation.log`. |
| StarRocks | 4.1.4-4a9848e; independent localdb cached-image fallback | Ordinary aggregate/scalar/window queries and the bitmap expression succeeded. A real SQL UDF `codex_constant(value INT) RETURNS 'CUSTOM'` returned CUSTOM for both rows. Motherdata install_run:89 reported SUCCEEDED, but its saved password was rejected with native 1045 both locally and remotely; that instance was preserved. Evidence: `/tmp/native_product_regression/starrocks_*.log`. |

The reproducible evidence files in the local workspace are:

- `/tmp/native_product_regression/assertions.json`: **32/32 exact-result assertions passed** across Greenplum, Cloudberry, CockroachDB, ClickHouse, Dameng, TiDB and StarRocks. Common scalar/aggregate results were `2 / 30 / alice / 7`; window results were Alice/1/1 and Bob/2/2. This count includes version/connection checks and is not a count of every supported function or overload.
- `/tmp/native_product_regression/results.json` and the per-statement logs: actual SQL, return status and output, including intermediate native failures subsequently corrected during fixture setup. The final exact-result assertions are the pass evidence.
- `/tmp/native_product_regression/clickhouse_26_8_combinators.json`: the complete **15-name** `system.aggregate_function_combinators` snapshot from **26.8.7.19**.
- `/tmp/native_product_regression/clickhouse_26_8_combinator_assertions.json`: **5/5** additional native checks passed: `sumIf=10`, `countIf=1`, `avgIf=15`, `sumArray=32`, `sumArrayIf=11`. Together with the first file, this run records **37 exact-result assertions**; it does not infer any earlier ClickHouse version range.

The initial empty Motherdata instance list is no longer a current-environment statement. Successful and failed managed installations, preserved localdb fallback instances and their isolated fixtures now exist. DB2 and HANA native installation requirements demand Linux x86_64 environments that the current ARM Mac node does not provide; their static coverage must not be reported as native execution.

**Doris remains unresolved, not terminally failed:** operation `83ac18f1-b40d-44e1-b0ff-0f71ecb23b99` (install_run:90) was last observed RUNNING while preparing the image (7/8 layers downloaded, 4/8 extracted). Subsequent original-operation polls failed because the native Motherdata MCP endpoint returned HTTP 404 for `/mcp`. No duplicate installation or fallback was created while the original outcome was unknown. Restore the native connection and query this same operation before deciding the next action; no Doris native pass is claimed here.

### YashanDB Oracle compatibility

YashanDB keeps the existing `Oracle SQL` engine and selects its datasource-owned permission registry. `yashandb-oracle-built-in-functions.json` supplies independently audited 23.4 ordinary-function and WM_CONCAT facts; the bare product version is never reused as an Oracle engine version. Explicitly branded Oracle compatibility input is delegated separately, as tested in the final isolation regression above. Plain, DISTINCT and window WM_CONCAT calls retain table READ without PROGRAM. Explicit schemas, quoted names, nested UDFs and management actions remain protected. Oracle datasources do not inherit the product exemption. Query authorization, query-only environment checks and masking checks use the same selected registry. Native YashanDB execution has not been verified because no existing server or supported Motherdata installation type is available.

### Greenplum / Cloudberry / OceanBase Oracle audit boundaries

The PostgreSQL derivative directories contain SQL-callable ordinary value/aggregate functions missing from the PostgreSQL catalog, not a copy of the entire kernel catalog. Native complex conversion, arithmetic/operator functions and deterministic hashes operate only on supplied values and are included. Management and metadata queries, optimizer controls (`enable_xform`, `disable_xform`), aggregate transition/final routines, C-only I/O signatures and optional extensions remain protected. Legacy matrix/MADlib APIs absent from the pinned native catalogs are not guessed into either directory. Cloudberry `gp_array_agg`/`gp_percentile_*` are version-dependent implementation entries and are not shared public native facts across the audited 1.6/2.0 baseline.

OceanBase Oracle's comparison covers 228 function reference pages. Oracle baseline names continue to use the existing catalog or dedicated grammar branches. The product supplement covers missing ordinary value/aggregate entries. GENERATOR/JSON_TABLE/XMLTABLE are table functions; OB_VERSION/OB_TRANSACTION_ID are metadata; SDO and DBMS package APIs retain their existing permission boundary. Only the exact documented WMSYS.WM_CONCAT entry is exempted, with no wildcard package bypass. Quoted schema/name components and other user schemas remain protected.

Motherdata has no registered Greenplum, Cloudberry or OceanBase Oracle installation type on the configured node. Greenplum 7.1 and Cloudberry 2.1 were nevertheless installed through the permitted localdb fallback and exercised natively as recorded above. Their native successes do not certify every product-release exemption or all low-privilege browser paths. OceanBase Oracle native execution remains NOT RUN because no actual matching instance was validated; a compatible PostgreSQL/Oracle server cannot substitute for it.

## JSON catalog and initialization convention

Function resources follow the established PostgreSQL structure: `versions` contains major-version family names, `type` is `FUNCTION`, and `name` is an identifier-parts array. For example, `{"versions":["24"],"type":"FUNCTION","name":["fnv32"]}`. ClickHouse additionally retains its case-sensitivity and aggregate flags.

Existing owners initialize `VersionedResourceRegistry` in constructors through `VersionedFunctionCatalogLoader`. There is no `loadNames()` path or additional function-catalog/version framework. Product-owned release names are mapped only to private registry keys; they are not encoded in JSON. Ordinary calls remain in behavior analysis until the version-aware permission registry classifies them. Name-only analysis recognition does not grant permission.

Recorded releases are grouped by product major version. Empty `versions` means availability is unverified and registers no exemption. Permission checks call the version enum directly, including its `LATEST` default for unknown versions. Product-specific catalogs use their own product enums; compatible-engine functions use the corresponding engine registry.

The Db2 supplemental source is LUW 10.5 and does not certify Db2 for i or z/OS. The Gauss supplement is openGauss 6.0 and does not certify Huawei GaussDB releases. MaxCompute's JDBC version does not identify its server type-system mode, so unverified generic calls retain PROGRAM. Major-family registration and enum defaults do not provide additional native-validation evidence for these products.

## Function classification and authorization wiring

The affected datasource owners now load PostgreSQL-style analysis resources in addition to their ordinary-function availability catalogs. The groups `metadataFunctions`, `privilegedFunctions`, `functionStatementTypes`, and `functionBehaviors` are merged per native release/name and consumed by split analysis, statement behavior, and the existing permission mapper. Compatible products retain their own supplemental classifications while reusing the compatible engine's existing analysis path. YashanDB continues to use the existing Oracle SQL engine.

Operational functions retain their actual actions and targets: session locks require MANAGE, file import/read paths retain MAINTAIN/READ and UNSAFE where specified, and metadata/configuration functions retain explicit READ or configuration requirements. A catalog name alone never suppresses these effects. Unresolved cross-catalog or relation-name arguments retain a broad target instead of assuming the editor's selected database/schema. Exact quoted native administrative identities retain their safety requirements; arbitrary schemas and distinct quoted case remain custom functions.

Stored views and planning-only EXPLAIN retain function dependencies without live function effects. Executing EXPLAIN variants and populated materialized results keep their effects. OceanBase Oracle's unsupported PL/SQL procedure-body forms are not certified by the function fixtures; this change does not extend that grammar. Db2 CREATE VIEW now includes its required AS token, matching the [IBM statement reference](https://www.ibm.com/docs/en/db2/11.5.x?topic=statements-create-view).

Service-only analysis facts without a reliably identifiable server release omit `versions`; they describe effects and do not establish an ordinary-function availability range. Registered availability still follows native major-release enums, including their explicitly accepted LATEST fallback for unknown input. No second function-version parser or name-only permission path is introduced.

Current code-level acceptance covers 1,354 permission-chain cases, 423 rule-domain cases, 1,239 MySQL module cases, and 951 companion function cases across the affected datasource modes. The dedicated Db2 run additionally passed 46 behavior and 37 split cases. These are static results, separate from native/browser execution. Logs and exact JUnit counts are retained in `/tmp/function_classification_*` and `/tmp/function_classification_verified_junit_summary.json`. The earlier broad 12,037-case run exposed three invalid Db2 aliases and an unsupported OceanBase procedure fixture; the aliases were corrected and revalidated, and the unsupported form is excluded from certified procedure coverage rather than changing PL/SQL syntax as part of this task.

The historical browser run recorded 47 matching outcomes across eleven available native datasources (13 query successes and 34 denials), with one separately recorded session-capacity retry. The classified getSetting, gp_read_error_log and SF_GET_PARA_VALUE cases checked denial without validating the actual resource and are not valid resource-authorization acceptance; the corrected resource-target regression and browser workflow supersede those cases. Execution 84 was RUNNING, healthy and worker-ready. See the [browser workflow and evidence](../../tests/frontend/security/builtin_function_authorization.md#classified-function-acceptance-after-analysis-resource-wiring). Remaining unavailable products retain static-only acceptance.

### Correct function call and resource authorization

Read functions follow the existing PostgreSQL model: CALL identifies the function dependency, while a separate READ identifies the configuration key, file, relation or instance. A function call must not manufacture a DM_QUERY request on the Function resource. Pure Dameng parameter getters follow `current_setting`; the [official DM function appendix](https://eco.dameng.com/document/dm/zh-cn/pm/sql-appendix) defines their parameter-reading behavior. Privileged calls and UNSAFE effects remain protected independently of their target READ.

The permission mapper also preserves CALL for function dependencies in CREATE/ALTER and data changes, and preserves explicit unnamed instance targets instead of narrowing them to the editor's current schema. Direct function-definition metadata reads remain READ. Named configuration keys retain their actual instance paths; unresolvable arguments retain broad scope. EXPLAIN retains callable dependencies and suppresses unexecuted operational effects, while external-file schema reads retain their resource permission.

Regression checks assert final permission kind, resource type and path, including the original Dameng getter, protected targetless functions, CTAS/INSERT dependencies, quoted/qualified UDFs, administrative functions and external-file planning. Live acceptance must pass the normal grant path and prove restoration of the original low-privilege boundary; mere denial is insufficient. See BF-PERM-09 in the [browser workflow](../../tests/frontend/security/builtin_function_authorization.md).

### Verified resource-target regression

- Backend permission/RuleDomain suite: **4267/4267**; MySQL module: **1239/1239**. Final changes were rechecked by **1391/1391** focused permission cases and **1381/1381** after the permission-message correction. These overlapping runs are not added together as unique cases.
- Companion function matrix: **1008/1008** across 25 datasource identifiers/modes. The wider classification, EXPLAIN, diagnostic, Dameng MPP/configuration/report and StarRocks pipe/backup checks brought the final run to **2306/2306** across ten commands. Old snapshots were corrected to retain the newly wired real resources and CALL dependencies; the failing inputs and multi-statement/duplicate statement-type blocks were retained.
- The complete native IDEA imports optimizer and `cdm-format` passed on 131 changed Java files. The full `package/all_build.sh` completed clean/build/local publication with exit 0; the final core-only message change also compiled and passed its focused checks. All 22 analysis resources were compared byte-for-byte in their published native plugin archives, including nested libraries, and the shared helper was checked in six affected archives. The final IDEA ConsoleQueryService and BehaviorRelations class bytes matched the Gradle outputs.
- Exact DmAloneLauncher execution 86 was terminated before publication, execution 87 passed startup and the authorization grant cycle, and final execution 88 loaded the message-path correction, reached RUNNING and passed backend/embedded-worker readiness. The read-function behavior and authorization mapping did not change between executions 87 and 88.
- Eleven available native products passed 29 initial read-only probes, with supplementary checks proving ten existing constant UDFs and the actual MySQL UI fixture. The final browser run certified **90 outcomes (26 results, 64 correct denials)** and restored the original low-privilege grants/drafts. Three environment-capacity attempts were excluded and retried; absent product environments remain NOT RUN for native/browser acceptance.

Commands include:

```sh
cd backend
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.permission.*' --tests 'com.clougence.clouddm.ds.secdomain.*' :sql-mysql:test --console=plain
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.permission.BehaviorRelationsTest' --tests 'com.clougence.clouddm.ds.permission.dameng.Dameng8PermissionTextTest' --console=plain
./gradlew :s-test:test --tests 'com.clougence.clouddm.ds.permission.BehaviorRelationsTest' --console=plain
```

The companion commands and exact per-run counts are retained in `/tmp/function_resource_auth_complete_companion_summary.json`; native/browser details and cleanup steps are in the [stable browser workflow](../../tests/frontend/security/builtin_function_authorization.md#resource-target-authorization-acceptance). Build, formatter, focused-test and published-artifact evidence is retained under `/tmp/function_resource_auth_*`.
