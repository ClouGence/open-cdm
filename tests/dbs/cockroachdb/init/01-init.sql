-- Sample objects for CockroachDB metadata acquisition tests.
-- Root has no password by default; set it here (COCKROACH_PASSWORD only applies to COCKROACH_USER).
-- defaultdb is the built-in database; do not recreate it.
ALTER USER root WITH PASSWORD '123456';

SET DATABASE = defaultdb;

CREATE TABLE IF NOT EXISTS public.crdb_meta_users (
    id INT8 NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL DEFAULT 'guest',
    created_at TIMESTAMPTZ NULL
);

CREATE TABLE IF NOT EXISTS public.crdb_meta_orders (
    id INT8 NOT NULL PRIMARY KEY,
    amount DECIMAL(10, 2) NULL
);

COMMENT ON COLUMN public.crdb_meta_users.name IS 'display name';
