# Datasource data type template

## Required files

Create tests/dbs/data-types/<datasource>/ with manifest.yaml, setup.sql,
verify.sql, and cleanup.sql.

## Manifest requirements

The manifest must record:

- Exact product versions that are required, optional, unsupported, or blocked.
- Every native type and alias, including modifiers that change range, precision,
  encoding, timezone, or result representation.
- The expected database-normalized metadata name, length, precision, scale,
  unsigned flag, charset, collation, and nullability where applicable.
- The CloudDM metadata type, ValueFetcher or equivalent reader, and the real
  frontend rendering path.
- Stable case identifiers and expected preview/full-content behavior.
- Version gates and objective skip conditions.

Aliases must not be silently omitted. If the database normalizes an alias,
record both the declared spelling and the normalized metadata.

## Fixture requirements

- Use one clearly namespaced schema and table prefix.
- setup.sql must be repeatable and must not touch objects outside that schema.
- cleanup.sql must name the exact schema and contain no wildcard or dynamic
  target.
- Split tables by type family and use case_id on every fixture row.
- Include NULL, EMPTY or ZERO, NORMAL, MIN, MAX, LONG, and product-specific
  cases when meaningful.
- Derive size boundaries from repository configuration or database contracts.
- Keep destructive editor scenarios out of the first read-only acceptance pass.

## Database truth requirements

verify.sql must report:

- VERSION, session timezone, charset, collation, and SQL mode.
- information_schema metadata in a deterministic order.
- Exact numeric text, HEX for bytes, normalized temporal text, JSON type/length,
  and WKT plus SRID for spatial data.
- Length and digest for long values instead of printing megabytes of content.
- Feature availability for conditionally supported types.

## Browser acceptance

Use tests/frontend/sql/datasource_type_display.md. A datasource is PASS only
when metadata, SQL results, and read-only table browsing pass together and no
relevant HTTP, WebSocket, Console, or backend-log error is introduced.
