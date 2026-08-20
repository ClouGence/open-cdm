# Data type acceptance assets

This directory contains data-driven, repeatable acceptance assets for database
types. The assets separate database facts from CloudDM presentation so a
display problem is not confused with bad fixture data.

## Contract

Each datasource directory contains:

- manifest.yaml: supported versions, complete type inventory, expected metadata,
  reader path, display contract, and version exceptions.
- setup.sql: idempotently recreates one explicitly named test schema and inserts
  stable boundary data.
- verify.sql: reports database-side facts without relying on CloudDM rendering.
- cleanup.sql: removes only the explicitly named test schema.

TEMPLATE.md defines the contract for adding another datasource. Browser
acceptance is shared in tests/frontend/sql/datasource_type_display.md.

## MySQL quick start

Use a disposable MySQL instance. The checked-in arm64 database stack exposes
MySQL 8.4 on localhost:2330.

~~~bash
cd tests/dbs/dbs_arm64
docker compose up -d mysql

docker compose exec -T mysql \
  mysql --default-character-set=utf8mb4 -uroot -p123456 \
  < ../data-types/mysql/setup.sql

docker compose exec -T mysql \
  mysql --default-character-set=utf8mb4 -uroot -p123456 \
  < ../data-types/mysql/verify.sql
~~~

The setup script recreates only cdm_data_types. It conditionally creates JSON
fixtures on MySQL 5.7+ and VECTOR fixtures on MySQL 9.0+; the repository's
declared VECTOR acceptance target is MySQL 9.7.

For the 4 MiB plus-one fixture, configure max_allowed_packet above 4194305.
When the server packet limit is lower, setup.sql omits only that row and
verify.sql reports the objective environment constraint instead of inserting
corrupted empty data.

To clean up:

~~~bash
docker compose exec -T mysql \
  mysql --default-character-set=utf8mb4 -uroot -p123456 \
  < ../data-types/mysql/cleanup.sql
~~~

Do not run setup.sql against an environment where a schema named
cdm_data_types contains non-test data.
