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
