-- mysql_examples.sql
-- Consolidated MySQL examples (DDL / DML / DCL / TCL / stored procedures / triggers / views / events / JSON / spatial / partitions / indexes / transactions / prepared statements, etc.)

-- Note: Validate in a test database before running in production. Some statements require corresponding permissions (e.g. CREATE USER, GRANT, CHANGE MASTER, etc.).

-- 1) Create and switch databases
DROP DATABASE IF EXISTS example_db;
CREATE DATABASE example_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE example_db;

-- 2) Simple user table (examples of common column types and constraints)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL,
  password_hash CHAR(64) NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  full_name VARCHAR(101) GENERATED ALWAYS AS (CONCAT(first_name, ' ', last_name)) STORED,
  status ENUM('active','inactive','banned') NOT NULL DEFAULT 'active',
  preferences JSON DEFAULT (JSON_OBJECT()),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY ux_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3) Posts and tags examples: foreign keys, full-text indexes, composite indexes
DROP TABLE IF EXISTS posts;
CREATE TABLE posts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  body LONGTEXT NOT NULL,
  tags SET('tech','life','news','guide') DEFAULT NULL,
  views INT UNSIGNED DEFAULT 0,
  published BOOLEAN DEFAULT FALSE,
  published_at DATETIME NULL,
  geom POINT NULL,
  FULLTEXT KEY ft_title_body (title, body),
  KEY idx_user_published (user_id, published),
  CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS tags;
CREATE TABLE tags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS post_tags;
CREATE TABLE post_tags (
  post_id BIGINT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (post_id, tag_id),
  CONSTRAINT fk_pt_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
  CONSTRAINT fk_pt_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) JSON, generated columns, and index examples
DROP TABLE IF EXISTS products;
CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sku VARCHAR(64) NOT NULL UNIQUE,
  attributes JSON NOT NULL,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  color VARCHAR(32) GENERATED ALWAYS AS (JSON_UNQUOTE(JSON_EXTRACT(attributes, '$.color'))) STORED,
  KEY idx_color (color)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) Partition example (by range)
DROP TABLE IF EXISTS logs;
CREATE TABLE logs (
  id BIGINT NOT NULL AUTO_INCREMENT,
  level TINYINT NOT NULL,
  msgContent TEXT,
  created_date DATE NOT NULL,
  PRIMARY KEY (id, created_date)
) ENGINE=InnoDB
PARTITION BY RANGE ( YEAR(created_date) ) (
  PARTITION p2020 VALUES LESS THAN (2021),
  PARTITION p2021 VALUES LESS THAN (2022),
  PARTITION pmax VALUES LESS THAN MAXVALUE
);

-- 6) Example data insertion (multiline, ON DUPLICATE KEY, INSERT IGNORE)
INSERT INTO users (username, email, password_hash, first_name, last_name)
VALUES
  ('alice','alice@example.com', REPEAT('a',64), 'Alice','Anderson'),
  ('bob','bob@example.com', REPEAT('b',64), 'Bob','Brown');

-- Insert data containing JSON
INSERT INTO products (sku, attributes, price) VALUES
  ('SKU1', JSON_OBJECT('color','red','size','M'), 19.99),
  ('SKU2', JSON_OBJECT('color','blue','size','L'), 29.99)
ON DUPLICATE KEY UPDATE price = VALUES(price);

-- 7) Complex query examples: JOIN / subquery / window functions / CTE
-- CTE + Window Function: Recent active user ranking
WITH recent_posts AS (
  SELECT user_id, COUNT(*) AS cnt
  FROM posts
  WHERE published = TRUE
  GROUP BY user_id
)
SELECT u.id, u.username, rp.cnt,
  ROW_NUMBER() OVER (ORDER BY rp.cnt DESC) AS rank1
FROM users u
LEFT JOIN recent_posts rp ON rp.user_id = u.id
ORDER BY rp.cnt DESC
LIMIT 10;

-- Subquery and EXISTS
SELECT p.* FROM posts p
WHERE EXISTS (SELECT 1 FROM users u WHERE u.id = p.user_id AND u.status = 'active');

-- Aggregation and ROLLUP
SELECT user_id, COUNT(*) AS cnt, SUM(views) AS total_views
FROM posts
GROUP BY user_id WITH ROLLUP;

-- FULLTEXT query
SELECT id, title, MATCH(title, body) AGAINST ('+mysql -fulltext' IN BOOLEAN MODE) AS score
FROM posts
WHERE MATCH(title, body) AGAINST ('+mysql -fulltext' IN BOOLEAN MODE)
ORDER BY score DESC;

-- 8) Example of JSON function
SELECT id, JSON_EXTRACT(attributes, '$.size') AS size FROM products;
SELECT id, attributes->"$.color" AS color FROM products; -- - Back to JSON
SELECT id, attributes->>"$.color" AS color_text FROM products; -- Back to Text

-- 9) Examples of spatial functions
INSERT INTO posts (user_id, title, body, geom) VALUES (1, 'Geo post', 'Has location', ST_GeomFromText('POINT(116.38 39.90)'));
SELECT id FROM posts WHERE ST_Contains(ST_GeomFromText('POLYGON((116.0 39.7,117.0 39.7,117.0 40.2,116.0 40.2,116.0 39.7))'), geom);

-- 10) Transactions, savepoints, and isolation levels
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
START TRANSACTION;
  UPDATE users SET status='inactive' WHERE id=999; -- Example update
  SAVEPOINT sp1;
  -- Do some operations
  ROLLBACK TO SAVEPOINT sp1;
COMMIT;

-- (11) Table lock example
LOCK TABLES users WRITE;
  -- Do some writing.
UNLOCK TABLES;

-- (12) Prepared statement and dynamic SQL
PREPARE stmt FROM 'SELECT * FROM users WHERE username = ?';
SET @u = 'alice';
EXECUTE stmt USING @u;
DEALLOCATE PREPARE stmt;

-- 13) Stored procedure (cursor + exception handling)
DROP PROCEDURE IF EXISTS sp_count_posts_by_user;
CREATE PROCEDURE sp_count_posts_by_user(IN p_user_id BIGINT, OUT p_count INT)
BEGIN
  SELECT COUNT(*) INTO p_count FROM posts WHERE user_id = p_user_id;
END;

-- Use stored procedure
CALL sp_count_posts_by_user(1, @cnt); SELECT @cnt;

-- 14) Stored function examples
DROP FUNCTION IF EXISTS fn_user_display_name;
CREATE FUNCTION fn_user_display_name(uid BIGINT) RETURNS VARCHAR(200)
DETERMINISTIC
BEGIN
  DECLARE v_name VARCHAR(200);
  SELECT COALESCE(full_name, username) INTO v_name FROM users WHERE id = uid;
  RETURN v_name;
END;

-- 15) Example of trigger
DROP TRIGGER IF EXISTS trg_posts_before_insert;
CREATE TRIGGER trg_posts_before_insert
BEFORE INSERT ON posts
FOR EACH ROW
BEGIN
  IF NEW.published = TRUE AND NEW.published_at IS NULL THEN
    SET NEW.published_at = NOW();
  END IF;
END;

-- 16) View Example
DROP VIEW IF EXISTS v_user_post_counts;
CREATE VIEW v_user_post_counts AS
SELECT u.id AS user_id, u.username, COUNT(p.id) AS post_count
FROM users u
LEFT JOIN posts p ON p.user_id = u.id
GROUP BY u.id, u.username;

-- 17) Event scheduler example (requires event scheduler to be enabled)
DROP EVENT IF EXISTS ev_cleanup_old_logs;
CREATE EVENT ev_cleanup_old_logs
ON SCHEDULE EVERY 1 DAY STARTS (CURRENT_TIMESTAMP)
DO
  DELETE FROM logs WHERE created_date < DATE_SUB(CURDATE(), INTERVAL 365 DAY);

-- 18) ALTER TABLE Example (multiple changes)
ALTER TABLE users
  ADD COLUMN last_login DATETIME NULL,
  MODIFY COLUMN email VARCHAR(320) NOT NULL,
  DROP INDEX ux_email;

-- 19) Index management
CREATE INDEX idx_posts_title ON posts(title(100));
DROP INDEX idx_posts_title ON posts;

-- 20) Table copy/create table from SELECT
CREATE TABLE posts_backup ENGINE=InnoDB AS SELECT * FROM posts LIMIT 0;

-- 21) Examples of management and diagnostic statements
SHOW VARIABLES LIKE 'max_connections';
SHOW STATUS LIKE 'Threads%';
EXPLAIN FORMAT=JSON SELECT * FROM posts WHERE title LIKE '%test%';
ANALYZE TABLE posts;
OPTIMIZE TABLE posts;
CHECK TABLE posts;

-- 22) Users and Permissions (Administer Permissions required)
-- CREATE USER 'app'@'localhost' IDENTIFIED BY 'secret';
-- GRANT SELECT, INSERT, UPDATE ON example_db.* TO 'app'@'localhost';
-- REVOKE DELETE ON example_db.* FROM 'app'@'localhost';
-- DROP USER 'app'@'localhost';

-- 23) Replication example (for reference only)
-- CHANGE MASTER TO MASTER_HOST='master.example', MASTER_USER='repl', MASTER_PASSWORD='pwd', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=  4;
-- START REPLICA; -- or START SLAVE in older versions

-- 24) Other common terminology: REPLACE, TRUNCATE, DELETE, UPDATE
REPLACE INTO users (id, username, email, password_hash) VALUES (99999, 'temp', 'temp@example.com', REPEAT('t',64));
INSERT IGNORE INTO tags (name) VALUES ('tech'), ('life');

-- 25) Example: Complex UPDATE uses JOIN
UPDATE posts p
JOIN users u ON u.id = p.user_id
SET p.views = p.views + 1
WHERE u.status = 'active' AND p.id = 1;

-- 26) Transaction isolation demonstration query (see current isolation level)
SELECT @@transaction_isolation;


-- 21) WHERE condition examples (common predicates and usage)
-- Basic comparison
SELECT * FROM users WHERE id = 1;
SELECT * FROM users WHERE created_at > '2025-01-01';

-- IN / NOT IN
SELECT * FROM posts WHERE id IN (1,2,3);
SELECT * FROM tags WHERE name IN (SELECT name FROM tags WHERE id < 10);
SELECT * FROM posts WHERE id NOT IN (SELECT post_id FROM post_tags WHERE tag_id = 2);
-- Note: If the subquery returns NULL, NOT IN may have unexpected results; prefer NOT EXISTS.

-- EXISTS / NOT EXISTS (correlated subquery)
SELECT p.* FROM posts p WHERE EXISTS (
  SELECT 1 FROM users u WHERE u.id = p.user_id AND u.status = 'active'
);
SELECT p.* FROM posts p WHERE NOT EXISTS (
  SELECT 1 FROM post_tags pt WHERE pt.post_id = p.id AND pt.tag_id = 5
);

-- ANY / SOME / ALL
SELECT * FROM products WHERE price > ANY (SELECT price FROM products WHERE sku LIKE 'SKU%');
SELECT * FROM products WHERE price > ALL (SELECT price FROM products WHERE color = 'red');

-- LIKE / REGEXP
SELECT * FROM users WHERE username LIKE 'a%';
SELECT * FROM users WHERE email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$';

-- BETWEEN / IS NULL
SELECT * FROM posts WHERE published_at BETWEEN '2024-01-01' AND '2024-12-31';
SELECT * FROM users WHERE last_login IS NULL;

-- JSON related predicates
SELECT * FROM products WHERE JSON_CONTAINS(attributes, '"M"', '$.size'); -- Match size "M".
SELECT * FROM products WHERE attributes->>"$.color" = 'red';

-- Spatial predicate
SELECT * FROM posts WHERE ST_Within(geom, ST_GeomFromText('POLYGON((116.0 39.7,117.0 39.7,117.0 40.2,116.0 40.2,116.0 39.7))'));

-- FULLTEXT usage in WHERE
SELECT * FROM posts WHERE MATCH(title, body) AGAINST ('database' IN NATURAL LANGUAGE MODE);

-- Correlated subquery and JOIN examples
SELECT u.id, u.username
FROM users u
WHERE (
  SELECT COUNT(*) FROM posts p WHERE p.user_id = u.id AND p.published = TRUE
) > 10;


-- mysql_examples_ddl.sql
-- Includes DDL operation examples: create/modify/drop tables, indexes, partitions, tablespaces, sequences, views, temporary tables, etc.

-- Use database
USE example_db;

-- 1) Basic CREATE TABLE Example (common constraints and options)
DROP TABLE IF EXISTS ddl_users;
CREATE TABLE ddl_users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password_hash CHAR(64) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY ux_username (username),
  CONSTRAINT chk_email CHECK (email LIKE '%@%')
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='示例用户表';

-- 2) CREATE TABLE LIKE (copy structure)
DROP TABLE IF EXISTS ddl_users_like;
CREATE TABLE ddl_users_like LIKE ddl_users;

-- 3) Temporary and memory tables
CREATE TEMPORARY TABLE tmp_results (
  id INT PRIMARY KEY,
  val VARCHAR(100)
) ENGINE=MEMORY;

-- 4) Partition examples (RANGE and HASH)
DROP TABLE IF EXISTS ddl_orders;
CREATE TABLE ddl_orders (
  order_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  order_date DATE NOT NULL,
  PRIMARY KEY (order_id, order_date)
) ENGINE=InnoDB
PARTITION BY RANGE ( YEAR(order_date) ) (
  PARTITION p2021 VALUES LESS THAN (2022),
  PARTITION p2022 VALUES LESS THAN (2023),
  PARTITION pmax VALUES LESS THAN MAXVALUE
);

-- Example of HASH partition
DROP TABLE IF EXISTS ddl_sharded;
CREATE TABLE ddl_sharded (
  id BIGINT NOT NULL PRIMARY KEY,
  data VARCHAR(200)
) ENGINE=InnoDB
PARTITION BY HASH(id) PARTITIONS 4;

-- 5) Create indexes: BTREE / FULLTEXT / SPATIAL / INVISIBLE
CREATE INDEX idx_ddl_users_email ON ddl_users(email);
CREATE FULLTEXT INDEX ft_posts_title ON posts(title);
CREATE SPATIAL INDEX sp_posts_geom ON posts(geom);
CREATE INDEX idx_products_color_invisible ON products(color) INVISIBLE;

-- 6) ALTER TABLE operations: add/modify/drop columns, rename, add constraints, modify engines and character sets
ALTER TABLE ddl_users
  ADD COLUMN bio TEXT AFTER email,
  ADD COLUMN nickname VARCHAR(50) GENERATED ALWAYS AS (CONCAT(first_name, '-', last_name)) VIRTUAL,
  MODIFY COLUMN email VARCHAR(320) NOT NULL,
  CHANGE COLUMN password_hash password CHAR(128) NOT NULL,
  DROP INDEX ux_username,
  ADD UNIQUE INDEX ux_email (email),
  COMMENT = '更新后的用户表';

-- Modify table engines and character sets
ALTER TABLE ddl_users ENGINE=InnoDB, CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- Add foreign key constraint
ALTER TABLE posts
  ADD CONSTRAINT fk_posts_user_example FOREIGN KEY (user_id) REFERENCES ddl_users(id) ON DELETE SET NULL;

-- Remove foreign keys and columns
ALTER TABLE posts
  DROP FOREIGN KEY fk_posts_user_example;
ALTER TABLE ddl_users DROP COLUMN bio;

-- Rename Table
RENAME TABLE ddl_users_like TO ddl_users_archive;

-- 7) Rebuild indexes and optimize tables
ALTER TABLE posts DROP INDEX idx_user_published;
ALTER TABLE posts ADD INDEX idx_user_published (user_id, published);
OPTIMIZE TABLE posts;
ANALYZE TABLE posts;

-- 8) Create/drop views
DROP VIEW IF EXISTS v_recent_posts;
CREATE VIEW v_recent_posts AS
SELECT id, title, user_id, published_at FROM posts WHERE published = TRUE ORDER BY published_at DESC LIMIT 100;
-- Update View
CREATE OR REPLACE VIEW v_recent_posts AS
SELECT id, title, user_id, published_at FROM posts WHERE published = TRUE ORDER BY published_at DESC LIMIT 500;
DROP VIEW IF EXISTS v_recent_posts;


-- 10) Tablespace example (requires administrator permissions and valid file paths)
-- CREATE TABLESPACE ts_example ADD DATAFILE '/var/lib/mysql/ts_example.ibd' ENGINE=InnoDB;
-- CREATE TABLE tb_ts (id INT PRIMARY KEY) TABLESPACE ts_example;


-- 12) Fast delete without row logging: TRUNCATE / DROP
TRUNCATE TABLE tmp_results;
DROP TABLE IF EXISTS tmp_results;

-- 13) DROP TABLE and CASCADE note (MySQL has no visible CASCADE clause)
DROP TABLE IF EXISTS ddl_users_archive;

-- 14) CREATE TABLE AS SELECT (copying data)
DROP TABLE IF EXISTS posts_backup;
CREATE TABLE posts_backup AS SELECT * FROM posts LIMIT 0; -- Structure only
CREATE TABLE posts_copy AS SELECT * FROM posts; -- Even data

-- 15) Column comments and table comments
ALTER TABLE products MODIFY COLUMN sku VARCHAR(64) NOT NULL COMMENT '商品 SKU';
ALTER TABLE products COMMENT = '商品主表';

-- 16) CHECK constraints (supported in MySQL 8+)
ALTER TABLE products ADD CONSTRAINT chk_price_nonnegative CHECK (price >= 0);

-- 17) Create examples with default values and generate columns
CREATE TABLE ddl_generated (
  id INT PRIMARY KEY AUTO_INCREMENT,
  data JSON,
  data_key1 VARCHAR(100) GENERATED ALWAYS AS (JSON_UNQUOTE(JSON_EXTRACT(data, '$.key1'))) STORED,
  data_len INT GENERATED ALWAYS AS (JSON_LENGTH(data)) VIRTUAL
) ENGINE=InnoDB;
CREATE INDEX idx_generated_key1 ON ddl_generated(data_key1);

-- 18) Create invisible indexes and switch visibility
CREATE INDEX idx_products_price_invis ON products(price) INVISIBLE;
ALTER TABLE products ALTER INDEX idx_products_price_invis VISIBLE;

-- 19) Permissions and DDL:CREATE USER (example, administrator only)
-- CREATE USER 'ddl_admin'@'localhost' IDENTIFIED BY 'strongpwd';
-- GRANT CREATE, ALTER, DROP ON example_db.* TO 'ddl_admin'@'localhost';

-- 20) DDL inside a transaction (note: most DDL statements commit implicitly)
START TRANSACTION;
  -- DML Operations
  INSERT INTO users (username, email, password_hash) VALUES ('tempuser','temp@example.com', REPEAT('t',64));
  -- The following ALTER will lead to hidden submissions (depending on MySQL version and operation)
  ALTER TABLE users ADD COLUMN temp_flag TINYINT DEFAULT 0;
COMMIT;

-- 21) DDL compatibility and version tips: some syntax (INVISIBLE, JSON TABLE, SEQUENCE, CHECK, GENERATED) requires MySQL 8.0+

-- End of mysql_examples_ddl.sql
