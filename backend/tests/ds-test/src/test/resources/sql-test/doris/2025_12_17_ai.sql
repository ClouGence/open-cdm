-- doris_full_queries.sql
-- 目标：尽可能覆盖 DorisParser.g4 中的查询与相关语法特性。
-- 将此文件按语句逐条送入解析器以检测未覆盖的语法规则。

-- =====================
-- Section: DDL - Databases / Tables / Views / MVs
-- =====================
CREATE DATABASE IF NOT EXISTS dw_db PROPERTIES ("owner"='team');
CREATE SCHEMA IF NOT EXISTS dw_db2 PROPERTIES ("owner"='team2');

CREATE TABLE IF NOT EXISTS dw_db.users (
  user_id BIGINT COMMENT '用户ID',
  name VARCHAR(128),
  attrs VARIANT,
  scores ARRAY<DOUBLE>,
  profile STRUCT<age:INT, city:STRING>,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  balance DECIMALV3(18,4) DEFAULT 0
) ENGINE=OLAP DUPLICATE KEY (user_id)
PARTITION BY RANGE (created_at) (
  PARTITION p2020 VALUES LESS THAN ('2021-01-01'),
  PARTITION p_max VALUES LESS THAN (MAXVALUE)
)
DISTRIBUTED BY HASH(user_id) BUCKETS 16
ROLLUP (r1(user_id))
PROPERTIES ("replication_num"='3');

CREATE TABLE dw_db.events (
  id BIGINT,
  user_id BIGINT,
  event_type STRING,
  event_time DATETIME,
  properties MAP<STRING, STRING>
) ENGINE = Olap UNIQUE KEY (id) DISTRIBUTED BY RANDOM BUCKETS AUTO;

CREATE TEMPORARY TABLE tmp_data (k INT, v STRING) ENGINE=OLAP;

CREATE VIEW IF NOT EXISTS dw_db.user_summary AS
SELECT u.user_id, u.name, COUNT(e.id) AS event_count
FROM dw_db.users u LEFT JOIN dw_db.events e ON u.user_id = e.user_id
GROUP BY u.user_id, u.name;

CREATE MATERIALIZED VIEW IF NOT EXISTS mv_user_events
BUILD IMMEDIATE
REFRESH ON SCHEDULE EVERY 1 DAY
DISTRIBUTED BY HASH(user_id) BUCKETS 8
AS SELECT user_id, event_type, COUNT(*) AS cnt FROM dw_db.events GROUP BY user_id, event_type;

-- CREATE TABLE LIKE
CREATE TABLE dw_db.users_copy LIKE dw_db.users;

-- CREATE FILE
CREATE FILE 'import_file' FROM dw_db PROPERTIES ("path"='/tmp/data.csv');


-- =====================
-- Section: DML - INSERT / UPDATE / DELETE / LOAD / EXPORT / COPY
-- =====================
-- simple insert
INSERT INTO dw_db.users (user_id, name, balance) VALUES (1, 'Alice', 100.50);
-- insert from select, with partition and label
INSERT INTO dw_db.events (id, user_id, event_type, event_time)
SELECT id, user_id, event_type, event_time FROM dw_db.events WHERE event_time > '2023-01-01' ;

-- insert overwrite with partition
INSERT OVERWRITE TABLE dw_db.users PARTITION (p2020) SELECT * FROM dw_db.users WHERE created_at < '2021-01-01';

-- update with FROM
UPDATE dw_db.users u SET balance = balance + 10 WHERE EXISTS (SELECT 1 FROM dw_db.events e WHERE e.user_id = u.user_id AND e.event_type='pay');

-- delete with using relations
DELETE FROM dw_db.events WHERE event_time < '2020-01-01';



-- COPY INTO from stage
COPY INTO dw_db.users FROM @my_stage('prefix.*') PROPERTIES ("format"='csv');

-- =====================
-- Section: Queries - SELECT variants
-- =====================
-- basic select
SELECT * FROM dw_db.users;
SELECT u.user_id, u.name AS username, u.profile FROM dw_db.users u;

-- qualified star
SELECT u.* FROM dw_db.users u;

-- DISTINCT / ALL
SELECT DISTINCT name FROM dw_db.users;
SELECT ALL user_id FROM dw_db.users;

-- aggregate and grouping elements
SELECT user_id, SUM(balance) AS tot_balance FROM dw_db.users GROUP BY user_id HAVING tot_balance > 100;
SELECT COUNT(*) FROM dw_db.events;

-- ROLLUP / CUBE / GROUPING SETS
SELECT event_type, user_id, COUNT(*) FROM dw_db.events GROUP BY ROLLUP(event_type, user_id);
SELECT event_type, user_id, COUNT(*) FROM dw_db.events GROUP BY CUBE(event_type, user_id);
SELECT event_type, user_id, COUNT(*) FROM dw_db.events GROUP BY GROUPING SETS( (event_type), (user_id), () );

-- window functions and frames
SELECT user_id, event_time, ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY event_time DESC) rn FROM dw_db.events;
SELECT user_id, SUM(cnt) OVER (PARTITION BY user_id ORDER BY event_time ROWS BETWEEN 1 PRECEDING AND CURRENT ROW) FROM (SELECT user_id, event_time, COUNT(*) AS cnt FROM dw_db.events GROUP BY user_id, event_time) t;

-- QUALIFY clause
SELECT user_id, event_time, RANK() OVER (PARTITION BY user_id ORDER BY event_time DESC) rnk FROM dw_db.events QUALIFY rnk = 1;

-- subqueries and correlated subqueries
SELECT * FROM dw_db.users u WHERE EXISTS (SELECT 1 FROM dw_db.events e WHERE e.user_id = u.user_id AND e.event_type='login');
SELECT * FROM dw_db.users WHERE user_id IN (SELECT DISTINCT user_id FROM dw_db.events WHERE event_type='click');

-- set operations
SELECT user_id FROM dw_db.users UNION SELECT user_id FROM dw_db.events;
SELECT user_id FROM dw_db.users INTERSECT SELECT user_id FROM dw_db.events;
SELECT user_id FROM dw_db.users EXCEPT SELECT user_id FROM dw_db.events;

-- joins: inner, left, right, full, cross, semi, anti
SELECT * FROM dw_db.users u JOIN dw_db.events e ON u.user_id = e.user_id;
SELECT * FROM dw_db.users u LEFT JOIN dw_db.events e ON u.user_id = e.user_id;
SELECT * FROM dw_db.users u RIGHT JOIN dw_db.events e ON u.user_id = e.user_id;
SELECT * FROM dw_db.users u FULL OUTER JOIN dw_db.events e ON u.user_id = e.user_id;
SELECT * FROM dw_db.users u CROSS JOIN tmp_data t;
-- semi/anti via EXISTS/NOT EXISTS
SELECT * FROM dw_db.users u WHERE EXISTS (SELECT 1 FROM dw_db.events e WHERE e.user_id = u.user_id);
SELECT * FROM dw_db.users u WHERE NOT EXISTS (SELECT 1 FROM dw_db.events e WHERE e.user_id = u.user_id);


-- ORDER BY variations and NULLS FIRST/LAST
SELECT * FROM dw_db.users ORDER BY name ASC NULLS LAST;
SELECT * FROM dw_db.users ORDER BY balance DESC NULLS FIRST, created_at ASC;

-- LIMIT variants
SELECT * FROM dw_db.users LIMIT 10;
SELECT * FROM dw_db.users LIMIT 10 OFFSET 5;
SELECT * FROM dw_db.users LIMIT 5, 10;

-- sample / TABLESAMPLE
SELECT * FROM dw_db.events TABLESAMPLE (10 PERCENT);
SELECT * FROM dw_db.events TABLESAMPLE (100 ROWS);

-- hints (simple examples)
SELECT /*+ LEADING(u) */ u.user_id FROM dw_db.users u JOIN dw_db.events e ON u.user_id = e.user_id;

-- expression coverage: arithmetic, bitwise, string, regex, like, null checks
SELECT id, name, balance + 10 AS new_bal, balance - 5 AS bal2, balance * 2, balance / 3 FROM dw_db.users;
SELECT name || '_suf' AS nn FROM dw_db.users;
SELECT id FROM dw_db.users WHERE name LIKE 'A%';
SELECT id FROM dw_db.users WHERE name RLIKE '^A';
SELECT CASE WHEN balance > 0 THEN 'pos' ELSE 'zero' END FROM dw_db.users;
SELECT COALESCE(name, 'unknown') FROM dw_db.users;
SELECT BITMAP_UNION(1) FROM dw_db.events;

-- function calls: CAST, CONVERT, GROUP_CONCAT, TRIM, EXTRACT, INTERVAL
SELECT CAST('2020-01-01' AS DATE) AS d;
SELECT CONVERT('abc' USING utf8);
SELECT GROUP_CONCAT(DISTINCT event_type ORDER BY event_type SEPARATOR ',') FROM dw_db.events;
SELECT TRIM(BOTH ' ' FROM name) FROM dw_db.users;
SELECT EXTRACT(YEAR FROM CURRENT_DATE);
SELECT CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP;
SELECT INTERVAL 1 DAY;

-- aggregation variants and special agg types
SELECT MAX(balance), MIN(balance), SUM(balance), AVG(balance) FROM dw_db.users;
SELECT HLL_UNION('hll_col'), QUANTILE_UNION('qcol') FROM dw_db.events;

-- ARRAY / MAP / STRUCT literals and access
SELECT [1,2,3] AS arr, {'k': 'v'} AS mp, struct('n', 30) AS st FROM dw_db.users LIMIT 1;
SELECT arr[1] FROM (SELECT [1,2] arr) t;

-- placeholder and prepared-like example
SELECT * FROM dw_db.users WHERE user_id = ?;

-- =====================
-- Section: DDL ALTER / DROP / RENAME / INDEX / PROPERTIES
-- =====================
ALTER TABLE dw_db.users ADD COLUMN nickname STRING COMMENT '昵称';
ALTER TABLE dw_db.users MODIFY COLUMN nickname VARCHAR(64) COMMENT '新昵称';
ALTER TABLE dw_db.users DROP COLUMN nickname;
ALTER TABLE dw_db.users ADD INDEX idx_user (user_id) PROPERTIES ("bw"='v');
ALTER TABLE dw_db.users DROP INDEX idx_user;
ALTER TABLE dw_db.users ADD ROLLUP r_new(user_id) PROPERTIES ("p"='v');
ALTER TABLE dw_db.users DROP ROLLUP r_new;
ALTER TABLE dw_db.users SET ("replication_num"='5');

DROP MATERIALIZED VIEW IF EXISTS mv_user_events;
DROP VIEW IF EXISTS dw_db.user_summary;
DROP TABLE IF EXISTS dw_db.users;

-- =====================
-- Section: Security / Users / Roles / Grants
-- =====================
CREATE USER IF NOT EXISTS alice IDENTIFIED BY 'pwd' COMMENT 'test user';
CREATE ROLE IF NOT EXISTS analytics_role COMMENT '分析角色';
GRANT SELECT_PRIV ON dw_db.users TO alice;
GRANT analytics_role TO alice;
SHOW GRANTS FOR alice;
REVOKE INSERT_PRIV ON dw_db.users FROM alice;
DROP USER IF EXISTS alice;
DROP ROLE IF EXISTS analytics_role;

-- =====================
-- Section: Transaction / Locks
-- =====================
BEGIN WITH LABEL t1;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
COMMIT WORK;
ROLLBACK WORK;


-- =====================
-- Section: Admin / System / Show / Describe
-- =====================
ADMIN SHOW REPLICA DISTRIBUTION FROM dw_db.events;
ADMIN REPAIR TABLE dw_db.events;
SHOW TABLES FROM dw_db;
SHOW CREATE TABLE dw_db.events;
SHOW PROCESSLIST;
DESCRIBE dw_db.events;
SHOW CATALOGS;

-- =====================
-- Section: Jobs / Schedules
-- =====================
CREATE JOB job_daily ON SCHEDULE EVERY 1 DAY STARTS '2025-01-01 00:00:00' DO INSERT INTO dw_db.events SELECT * FROM dw_db.events WHERE event_time > '2025-01-01';
PAUSE JOB WHERE jobNameKey = 'jobNameValue';
RESUME JOB WHERE jobNameKey = 'jobNameValue';
DROP JOB WHERE jobNameKey = 'jobNameValue';

-- =====================
-- Section: Analyze / Stats / Histograms
-- =====================
ANALYZE TABLE dw_db.events;
SHOW COLUMN HISTOGRAM dw_db.events (id);
SHOW TABLE STATS dw_db.events;

-- =====================
-- Section: STORAGE / REPOSITORY / S3 / HDFS / BROKER
-- =====================
CREATE REPOSITORY repo1 WITH S3 ON LOCATION '/data' PROPERTIES ("p"='v');
CREATE STORAGE POLICY IF NOT EXISTS cold_policy PROPERTIES ("cold"='true');
CREATE EXTERNAL RESOURCE my_broker PROPERTIES ("type"='broker', "path"='/tmp');

-- =====================
-- Section: COPY/EXPORT/IMPORT with remote storage
-- =====================
EXPORT TABLE dw_db.events TO 's3://bucket/path/events.csv' PROPERTIES ("format"='csv');

-- =====================
-- Section: Complex / Edge cases
-- =====================
-- nested CTEs, recursive-like structures (as allowed)
WITH t1 AS (
  WITH t2 AS (SELECT 1 AS a) SELECT a FROM t2
)
SELECT * FROM t1;

-- function / alias creation
CREATE FUNCTION IF NOT EXISTS math_add (INT, INT) RETURNS INT PROPERTIES ("lang"='java');
CREATE ALIAS FUNCTION add_alias (INT, INT) WITH PARAMETER (p1, p2) AS a + b;

-- final catch-all: many tokens combined
SHOW FULL PROCESSLIST;
SHOW VARIABLES;
SHOW CREATE VIEW dw_db.user_summary;

-- End of file
