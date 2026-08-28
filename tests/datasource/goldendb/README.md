# GoldenDB compatibility verification

This directory contains the GoldenDB datasource verification matrix, CloudDM retest entry and driver-preparation
helper. Run all state-changing checks only against an isolated test database.

Current coverage, executed evidence, and remaining gates are tracked in:

- [`goldendb-test-matrix.md`](goldendb-test-matrix.md): combined MySQL/Oracle matrix;
- [`goldendb-oracle-test-matrix.md`](goldendb-oracle-test-matrix.md): Oracle-mode report, provider differences, and
  runtime gates.

Only the CloudDM product path can produce a datasource acceptance PASS.

## Prepare the vendor JDBC driver

```bash
tests/datasource/goldendb/prepare_driver.sh \
  /path/to/ZXCLOUD-GoldenDB-Client-DriverV1.0.01P2.zip
```

The script extracts and verifies both vendor drivers:

- `GoldenDB MySQL JDBC Driver / 5.1.46.86` from Java Connector V2.1P5;
- `GoldenDB Oracle JDBC Driver / 5.1.46.77` from Java Oracle Connector V1.3P1.

It installs them into separate family/version directories under the IDEA standalone runtime's writable driver root,
derived as
`<workspace>/data/alone/drivers`. The separation is required because both JARs expose `com.goldendb.jdbc.Driver` but
contain different
implementations. Pass a second argument to target another CloudDM driver root. The script does not add vendor binaries
to Git or the
built-in driver bundle.

After changing the plugin descriptor or preparing the driver, rebuild/deploy `ds-goldendb`, restart `DmAloneLauncher`,
and use the
datasource page's driver preparation action so CloudDM writes its `files.idx` metadata.

## Run the CloudDM verification

GoldenDB acceptance must run through the CloudDM product path:

1. Prepare the vendor driver with `prepare_driver.sh`.
2. Add or edit the GoldenDB MySQL/Oracle datasource in CloudDM and pass the connection test.
3. Execute only the SQL recorded as passed in [`goldendb-test-matrix.md`](goldendb-test-matrix.md) from the CloudDM
   query console.
4. Follow [`../../frontend/datasource/goldendb_datasource.md`](../../frontend/datasource/goldendb_datasource.md) for
   datasource forms, metadata, transactions, failure paths and cleanup.

Historical direct-JDBC reports may be retained as database/driver evidence, but they are not a CloudDM acceptance
result and no direct-JDBC probe is maintained in this repository.

## Safety

- Never print database passwords, connection properties or browser credentials.
- Use an isolated datasource and unique `codex_gdb_*` object names.
- Verify every target before cleanup and remove only objects created by the current run.
- Keep Docker Compose databases, vendor binaries, database data, licenses, credentials and generated reports outside
  this repository.
