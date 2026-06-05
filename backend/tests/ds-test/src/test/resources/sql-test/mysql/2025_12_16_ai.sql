-- mysql_examples.sql
-- 综合 MySQL 特性示例（DDL / DML / DCL / TCL / 存储过程 / 触发器 / 视图 / 事件 / JSON / 空间 / 分区 / 索引 / 事务 / 预处理语句 等）

-- 注意：在生产环境运行前请先在测试库验证。部分语句需要相应权限（如 CREATE USER, GRANT, CHANGE MASTER 等）。

-- 1) 创建数据库并切换
DROP DATABASE IF EXISTS example_db;
CREATE DATABASE example_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE example_db;

-- 2) 简要用户表（示例常用列类型与约束）
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

-- 3) 文章与标签示例：外键、全文索引、组合索引
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

-- 4) JSON 与生成列、索引示例
DROP TABLE IF EXISTS products;
CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sku VARCHAR(64) NOT NULL UNIQUE,
  attributes JSON NOT NULL,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  color VARCHAR(32) GENERATED ALWAYS AS (JSON_UNQUOTE(JSON_EXTRACT(attributes, '$.color'))) STORED,
  KEY idx_color (color)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) 分区示例（按范围分区）
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

-- 6) 示例数据插入（多行、ON DUPLICATE KEY、INSERT IGNORE）
INSERT INTO users (username, email, password_hash, first_name, last_name)
VALUES
  ('alice','alice@example.com', REPEAT('a',64), 'Alice','Anderson'),
  ('bob','bob@example.com', REPEAT('b',64), 'Bob','Brown');

-- 插入带 JSON
INSERT INTO products (sku, attributes, price) VALUES
  ('SKU1', JSON_OBJECT('color','red','size','M'), 19.99),
  ('SKU2', JSON_OBJECT('color','blue','size','L'), 29.99)
ON DUPLICATE KEY UPDATE price = VALUES(price);

-- 7) 复杂查询示例：JOIN / 子查询 / 窗口函数 / CTE
-- CTE + 窗口函数: 最近活跃用户排名
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

-- 子查询与 EXISTS
SELECT p.* FROM posts p
WHERE EXISTS (SELECT 1 FROM users u WHERE u.id = p.user_id AND u.status = 'active');

-- 聚合与 ROLLUP
SELECT user_id, COUNT(*) AS cnt, SUM(views) AS total_views
FROM posts
GROUP BY user_id WITH ROLLUP;

-- FULLTEXT 查询
SELECT id, title, MATCH(title, body) AGAINST ('+mysql -fulltext' IN BOOLEAN MODE) AS score
FROM posts
WHERE MATCH(title, body) AGAINST ('+mysql -fulltext' IN BOOLEAN MODE)
ORDER BY score DESC;

-- 8) JSON 函数示例
SELECT id, JSON_EXTRACT(attributes, '$.size') AS size FROM products;
SELECT id, attributes->"$.color" AS color FROM products; -- -> 返回 JSON
SELECT id, attributes->>"$.color" AS color_text FROM products; -- ->> 返回文本

-- 9) 空间函数示例
INSERT INTO posts (user_id, title, body, geom) VALUES (1, 'Geo post', 'Has location', ST_GeomFromText('POINT(116.38 39.90)'));
SELECT id FROM posts WHERE ST_Contains(ST_GeomFromText('POLYGON((116.0 39.7,117.0 39.7,117.0 40.2,116.0 40.2,116.0 39.7))'), geom);

-- 10) 事务、保存点、隔离级别
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
START TRANSACTION;
  UPDATE users SET status='inactive' WHERE id=999; -- 假设
  SAVEPOINT sp1;
  -- 执行一些操作
  ROLLBACK TO SAVEPOINT sp1;
COMMIT;

-- 11) 锁表示例
LOCK TABLES users WRITE;
  -- 做一些写操作
UNLOCK TABLES;

-- 12) 预处理语句与动态 SQL
PREPARE stmt FROM 'SELECT * FROM users WHERE username = ?';
SET @u = 'alice';
EXECUTE stmt USING @u;
DEALLOCATE PREPARE stmt;

-- 13) 存储过程（游标 + 异常处理）
DROP PROCEDURE IF EXISTS sp_count_posts_by_user;
CREATE PROCEDURE sp_count_posts_by_user(IN p_user_id BIGINT, OUT p_count INT)
BEGIN
  SELECT COUNT(*) INTO p_count FROM posts WHERE user_id = p_user_id;
END;

-- 使用存储过程
CALL sp_count_posts_by_user(1, @cnt); SELECT @cnt;

-- 14) 存储函数示例
DROP FUNCTION IF EXISTS fn_user_display_name;
CREATE FUNCTION fn_user_display_name(uid BIGINT) RETURNS VARCHAR(200)
DETERMINISTIC
BEGIN
  DECLARE v_name VARCHAR(200);
  SELECT COALESCE(full_name, username) INTO v_name FROM users WHERE id = uid;
  RETURN v_name;
END;

-- 15) 触发器示例
DROP TRIGGER IF EXISTS trg_posts_before_insert;
CREATE TRIGGER trg_posts_before_insert
BEFORE INSERT ON posts
FOR EACH ROW
BEGIN
  IF NEW.published = TRUE AND NEW.published_at IS NULL THEN
    SET NEW.published_at = NOW();
  END IF;
END;

-- 16) 视图示例
DROP VIEW IF EXISTS v_user_post_counts;
CREATE VIEW v_user_post_counts AS
SELECT u.id AS user_id, u.username, COUNT(p.id) AS post_count
FROM users u
LEFT JOIN posts p ON p.user_id = u.id
GROUP BY u.id, u.username;

-- 17) 事件调度器示例（需开启 event_scheduler）
DROP EVENT IF EXISTS ev_cleanup_old_logs;
CREATE EVENT ev_cleanup_old_logs
ON SCHEDULE EVERY 1 DAY STARTS (CURRENT_TIMESTAMP)
DO
  DELETE FROM logs WHERE created_date < DATE_SUB(CURDATE(), INTERVAL 365 DAY);

-- 18) ALTER TABLE 示例（多种更改）
ALTER TABLE users
  ADD COLUMN last_login DATETIME NULL,
  MODIFY COLUMN email VARCHAR(320) NOT NULL,
  DROP INDEX ux_email;

-- 19) 索引管理
CREATE INDEX idx_posts_title ON posts(title(100));
DROP INDEX idx_posts_title ON posts;

-- 20) 表复制/创建表从 SELECT
CREATE TABLE posts_backup ENGINE=InnoDB AS SELECT * FROM posts LIMIT 0;

-- 21) 管理和诊断语句示例
SHOW VARIABLES LIKE 'max_connections';
SHOW STATUS LIKE 'Threads%';
EXPLAIN FORMAT=JSON SELECT * FROM posts WHERE title LIKE '%test%';
ANALYZE TABLE posts;
OPTIMIZE TABLE posts;
CHECK TABLE posts;

-- 22) 用户与权限（需要管理员权限）
-- CREATE USER 'app'@'localhost' IDENTIFIED BY 'secret';
-- GRANT SELECT, INSERT, UPDATE ON example_db.* TO 'app'@'localhost';
-- REVOKE DELETE ON example_db.* FROM 'app'@'localhost';
-- DROP USER 'app'@'localhost';

-- 23) 复制（示例，仅供参考）
-- CHANGE MASTER TO MASTER_HOST='master.example', MASTER_USER='repl', MASTER_PASSWORD='pwd', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=  4;
-- START REPLICA; -- 或 START SLAVE 在旧版本中

-- 24) 其他常用语法：REPLACE, TRUNCATE, DELETE, UPDATE
REPLACE INTO users (id, username, email, password_hash) VALUES (99999, 'temp', 'temp@example.com', REPEAT('t',64));
INSERT IGNORE INTO tags (name) VALUES ('tech'), ('life');

-- 25) 示例：复杂 UPDATE 使用 JOIN
UPDATE posts p
JOIN users u ON u.id = p.user_id
SET p.views = p.views + 1
WHERE u.status = 'active' AND p.id = 1;

-- 26) 事务隔离演示查询（查看当前隔离级别）
SELECT @@transaction_isolation;


-- 21) WHERE 子句判定示例（常见判断方式与进阶用法）
-- 基本比较
SELECT * FROM users WHERE id = 1;
SELECT * FROM users WHERE created_at > '2025-01-01';

-- IN / NOT IN
SELECT * FROM posts WHERE id IN (1,2,3);
SELECT * FROM tags WHERE name IN (SELECT name FROM tags WHERE id < 10);
SELECT * FROM posts WHERE id NOT IN (SELECT post_id FROM post_tags WHERE tag_id = 2);
-- 注意：若子查询返回 NULL，NOT IN 可能产生意外结果，优先使用 NOT EXISTS

-- EXISTS / NOT EXISTS（相关子查询）
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

-- JSON 相关谓词
SELECT * FROM products WHERE JSON_CONTAINS(attributes, '"M"', '$.size'); -- attributes.size 包含 "M"
SELECT * FROM products WHERE attributes->>"$.color" = 'red';

-- 空间谓词
SELECT * FROM posts WHERE ST_Within(geom, ST_GeomFromText('POLYGON((116.0 39.7,117.0 39.7,117.0 40.2,116.0 40.2,116.0 39.7))'));

-- FULLTEXT 在 WHERE 中的用法
SELECT * FROM posts WHERE MATCH(title, body) AGAINST ('database' IN NATURAL LANGUAGE MODE);

-- 相关子查询与聚合示例
SELECT u.id, u.username
FROM users u
WHERE (
  SELECT COUNT(*) FROM posts p WHERE p.user_id = u.id AND p.published = TRUE
) > 10;


-- mysql_examples_ddl.sql
-- 含各类 DDL 操作示例：创建/修改/删除表、索引、分区、表空间、序列、视图、临时表等

-- 使用数据库
USE example_db;

-- 1) 基本 CREATE TABLE 示例（常见约束与选项）
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

-- 2) CREATE TABLE ... LIKE（拷贝结构）
DROP TABLE IF EXISTS ddl_users_like;
CREATE TABLE ddl_users_like LIKE ddl_users;

-- 3) 临时表与内存表
CREATE TEMPORARY TABLE tmp_results (
  id INT PRIMARY KEY,
  val VARCHAR(100)
) ENGINE=MEMORY;

-- 4) 分区表示例（RANGE 与 HASH）
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

-- HASH 分区示例
DROP TABLE IF EXISTS ddl_sharded;
CREATE TABLE ddl_sharded (
  id BIGINT NOT NULL PRIMARY KEY,
  data VARCHAR(200)
) ENGINE=InnoDB
PARTITION BY HASH(id) PARTITIONS 4;

-- 5) 创建索引：BTREE / FULLTEXT / SPATIAL / INVISIBLE
CREATE INDEX idx_ddl_users_email ON ddl_users(email);
CREATE FULLTEXT INDEX ft_posts_title ON posts(title);
CREATE SPATIAL INDEX sp_posts_geom ON posts(geom);
CREATE INDEX idx_products_color_invisible ON products(color) INVISIBLE;

-- 6) ALTER TABLE 操作合集：添加/修改/删除列、重命名、增加约束、修改引擎与字符集
ALTER TABLE ddl_users
  ADD COLUMN bio TEXT AFTER email,
  ADD COLUMN nickname VARCHAR(50) GENERATED ALWAYS AS (CONCAT(first_name, '-', last_name)) VIRTUAL,
  MODIFY COLUMN email VARCHAR(320) NOT NULL,
  CHANGE COLUMN password_hash password CHAR(128) NOT NULL,
  DROP INDEX ux_username,
  ADD UNIQUE INDEX ux_email (email),
  COMMENT = '更新后的用户表';

-- 修改表引擎与字符集
ALTER TABLE ddl_users ENGINE=InnoDB, CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- 添加外键约束
ALTER TABLE posts
  ADD CONSTRAINT fk_posts_user_example FOREIGN KEY (user_id) REFERENCES ddl_users(id) ON DELETE SET NULL;

-- 删除外键与列
ALTER TABLE posts
  DROP FOREIGN KEY fk_posts_user_example;
ALTER TABLE ddl_users DROP COLUMN bio;

-- 重命名表
RENAME TABLE ddl_users_like TO ddl_users_archive;

-- 7) 重建索引与优化表
ALTER TABLE posts DROP INDEX idx_user_published;
ALTER TABLE posts ADD INDEX idx_user_published (user_id, published);
OPTIMIZE TABLE posts;
ANALYZE TABLE posts;

-- 8) 创建/删除视图
DROP VIEW IF EXISTS v_recent_posts;
CREATE VIEW v_recent_posts AS
SELECT id, title, user_id, published_at FROM posts WHERE published = TRUE ORDER BY published_at DESC LIMIT 100;
-- 更新视图
CREATE OR REPLACE VIEW v_recent_posts AS
SELECT id, title, user_id, published_at FROM posts WHERE published = TRUE ORDER BY published_at DESC LIMIT 500;
DROP VIEW IF EXISTS v_recent_posts;


-- 10) 表空间示例（需管理员权限与文件路径）
-- CREATE TABLESPACE ts_example ADD DATAFILE '/var/lib/mysql/ts_example.ibd' ENGINE=InnoDB;
-- CREATE TABLE tb_ts (id INT PRIMARY KEY) TABLESPACE ts_example;


-- 12) 无日志快速删除：TRUNCATE / DROP
TRUNCATE TABLE tmp_results;
DROP TABLE IF EXISTS tmp_results;

-- 13) DROP TABLE 与 CASCADE 注意（MySQL 无显式 CASCADE 子句）
DROP TABLE IF EXISTS ddl_users_archive;

-- 14) CREATE TABLE ... AS SELECT（复制数据）
DROP TABLE IF EXISTS posts_backup;
CREATE TABLE posts_backup AS SELECT * FROM posts LIMIT 0; -- 仅结构
CREATE TABLE posts_copy AS SELECT * FROM posts; -- 连数据

-- 15) 字段注释与表注释显示
ALTER TABLE products MODIFY COLUMN sku VARCHAR(64) NOT NULL COMMENT '商品 SKU';
ALTER TABLE products COMMENT = '商品主表';

-- 16) 检查约束（MySQL 8+ 支持）
ALTER TABLE products ADD CONSTRAINT chk_price_nonnegative CHECK (price >= 0);

-- 17) 创建带默认值与生成列的示例
CREATE TABLE ddl_generated (
  id INT PRIMARY KEY AUTO_INCREMENT,
  data JSON,
  data_key1 VARCHAR(100) GENERATED ALWAYS AS (JSON_UNQUOTE(JSON_EXTRACT(data, '$.key1'))) STORED,
  data_len INT GENERATED ALWAYS AS (JSON_LENGTH(data)) VIRTUAL
) ENGINE=InnoDB;
CREATE INDEX idx_generated_key1 ON ddl_generated(data_key1);

-- 18) 创建不可见索引，与切换可见性
CREATE INDEX idx_products_price_invis ON products(price) INVISIBLE;
ALTER TABLE products ALTER INDEX idx_products_price_invis VISIBLE;

-- 19) 权限与 DDL：CREATE USER（示例，仅管理员可执行）
-- CREATE USER 'ddl_admin'@'localhost' IDENTIFIED BY 'strongpwd';
-- GRANT CREATE, ALTER, DROP ON example_db.* TO 'ddl_admin'@'localhost';

-- 20) 事务中执行 DDL（注意：多数 DDL 会隐式提交）
START TRANSACTION;
  -- DML 操作
  INSERT INTO users (username, email, password_hash) VALUES ('tempuser','temp@example.com', REPEAT('t',64));
  -- 以下 ALTER 将导致隐式提交（取决于 MySQL 版本与操作）
  ALTER TABLE users ADD COLUMN temp_flag TINYINT DEFAULT 0;
COMMIT;

-- 21) DDL 兼容与版本提示：某些语法（INVISIBLE, JSON_TABLE, SEQUENCE, CHECK, GENERATED）需要 MySQL 8.0+ 或更高版本

-- End of mysql_examples_ddl.sql

