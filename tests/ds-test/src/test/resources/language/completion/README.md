# Completion Test Scripts

Completion scripts live under `language/completion/<datasource>/basic.txt`. Each file can contain multiple cases separated by a line with only `----------`.

## Basic Case

```text
[from_table_names]
languageClass: com.clougence.clouddm.dsfamily.mysql.language.MyLanguageSpi
schema: devtester
sql:
select * from |
expect:
[
  {"label":"active_user_view"},
  {"label":"order_table"},
  {"label":"user_table"}
]
```

The runner compares only completion item labels. Other fields in `expect` can be kept as readable notes, but they are not asserted.

## Fields

- `languageClass`: required. The concrete `DsLanguageSpi`.
- `meta`: optional. Defaults to `_meta`.
- `catalog`: optional. Written to `CompletionRequest.catalog` and `levelsParam[Catalog]`.
- `schema`: optional. Written to `CompletionRequest.schema` and `levelsParam[Schema]`.
- `database`: optional alias of `schema`.
- `dataSourceId`: optional. Defaults to `1`.
- `sql`: required. SQL or command text.
- `expect`: required. Expected labels in returned order.

## Cursor

Use `|` to mark the cursor position. The runner removes this marker before sending the request.

```text
sql:
select u.| from user_table u
```

To simulate the editor sending a full text block plus a separate cursor, omit `|` and provide explicit cursor fields.

```text
sql:
select
  u.
from user_table u
cursorLineNumber: 2
cursorColNumber: 4
```

Line numbers are 1-based. Column numbers are 0-based.

## Metadata

Use one of the shared metadata files unless the datasource needs its own shape.

- `_meta/1-layer`: one-level resources, such as Redis keys.
- `_meta/2-layer`: `schema/database -> object -> column`, for MySQL-like datasources and MongoDB collections.
- `_meta/3-layer`: `catalog -> schema -> object -> column`, for PostgreSQL-like datasources.

Keep scripts scoped with `schema` or `catalog + schema` so tests verify metadata isolation instead of accidentally matching global object names.

## SQL Engine

The runner obtains `SqlEngineSpi` from `SqlTestSupport` according to the resource path. The first directory is the base datasource, and filename prefixes select derived datasources, for example:

- `mysql/basic.txt` -> `mysql`
- `mysql/tidb_basic.txt` -> `tidb`
- `postgres/greenplum_basic.txt` -> `greenplum`
- `doris/selectdb_basic.txt` -> `selectdb`

Do not put concrete `SqlEngineSpi` class names in scripts.

## Real Metadata Entry Contracts

The script fixtures above supply column metadata directly. They cannot catch a
missing datasource implementation of `fetchSelectObject`.

Run the separate metadata contracts from the repository root:

```bash
cd backend
./gradlew :s-test:test --tests '*SelectObjectContractTest' --tests '*Db2SelectObjectLookupTest' --tests '*DmSelectObjectLookupTest'
```

`SelectObjectContractTest` covers the 34 data-editing datasource implementations.
It creates each actual MetaService and UMI implementation, rejects default
unsupported service/provider methods, exercises object lookup with empty JDBC
metadata, and checks that SQLExceptions propagate without being replaced by
UnsupportedOperationException. Version responses are fixture inputs for SQL
selection, not proof of real-server compatibility.

The JDBC fixture returns rows only for version/current-database queries and no
object rows. Providers that synthesize an object from SHOW COLUMNS may return an
object with no columns instead of null. `Db2SelectObjectLookupTest` separately
checks successful table/view/materialized lookup, column mapping, fallback order,
and the missing-object path through the real shared DB2 service/provider chain.
`DmSelectObjectLookupTest` checks table/view selection and verifies that a view
uses its own column loader, avoiding table identity decoding for view columns.

When a datasource opens data editing or changes its MetaService binding, update
the parameter matrix. These offline checks complement, rather than replace,
database integration and @Browser acceptance.
