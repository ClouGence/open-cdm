-- mysql_admin_statements.sql
-- 示例：administrationStatement 各类语句集合
-- 包含：ALTER USER, CREATE USER, DROP USER, GRANT/REVOKE, CREATE/DROP ROLE,
-- GRANT/REVOKE PROXY, RENAME USER, ANALYZE/CHECK/CHECKSUM/OPTIMIZE/REPAIR TABLE,
-- CREATE UDF, INSTALL/UNINSTALL PLUGIN, SET / SHOW / BINLOG / FLUSH / KILL / RESET / SHUTDOWN 等

-- 注意：部分语句需要管理员权限（root）并在合适的 MySQL 版本下执行。

-- 1) CREATE USER
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'S3cureP@ss';
CREATE USER 'readonly'@'%' IDENTIFIED WITH mysql_native_password BY 'readonlypwd';

-- 2) ALTER USER
ALTER USER 'app_user'@'localhost' IDENTIFIED BY 'N3wP@ssw0rd';
ALTER USER 'app_user'@'localhost' PASSWORD EXPIRE; -- 强制密码过期（某些版本）
ALTER USER 'readonly'@'%' IDENTIFIED WITH caching_sha2_password BY 'newreadonly';

-- 3) DROP USER
DROP USER IF EXISTS 'temp'@'localhost';

-- 4) RENAME USER
RENAME USER 'old_user'@'localhost' TO 'new_user'@'localhost';

-- 5) CREATE ROLE / DROP ROLE
CREATE ROLE 'reporting_role';
CREATE ROLE 'devops';
DROP ROLE IF EXISTS 'old_role';

-- 6) GRANT / REVOKE（权限管理）
GRANT SELECT, INSERT, UPDATE ON example_db.* TO 'app_user'@'localhost';
GRANT ALL PRIVILEGES ON example_db.* TO 'admin_user'@'localhost' WITH GRANT OPTION;
REVOKE INSERT ON example_db.* FROM 'app_user'@'localhost';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'admin_user'@'localhost';

-- 7) GRANT ROLE / REVOKE ROLE
GRANT 'reporting_role' TO 'analyst'@'localhost';
REVOKE 'reporting_role' FROM 'analyst'@'localhost';

-- 8) GRANT PROXY / REVOKE PROXY（代理权限）
GRANT PROXY ON 'target_user'@'localhost' TO 'proxy_user'@'localhost';
REVOKE PROXY ON 'target_user'@'localhost' FROM 'proxy_user'@'localhost';

-- 9) GRANT PROXY WITH ROLE（示例：授予并限定代理）
GRANT PROXY ON 'target_user'@'localhost' TO 'proxy_user'@'localhost' WITH GRANT OPTION;

-- 10) SHOW 权限相关
SHOW GRANTS FOR 'app_user'@'localhost';
SHOW GRANTS FOR CURRENT_USER();

-- 11) ANALYZE / CHECK / CHECKSUM / OPTIMIZE / REPAIR TABLE
ANALYZE TABLE posts;
CHECK TABLE posts;
CHECKSUM TABLE posts;
OPTIMIZE TABLE posts;
REPAIR TABLE archive_table;

-- 12) ALTER TABLE ... ANALYZE / OPTIMIZE 示例（管理操作）
ALTER TABLE posts ENGINE=InnoDB; -- 仅示例，改变存储引擎需管理员或合理场景

-- 13) CREATE UDF（用户自定义函数 — 依赖共享库）
-- 需要提前把共享库放置到服务器可加载路径并且有相应的 C/C++ 编译实现
CREATE FUNCTION example_udf RETURNS INTEGER SONAME 'example_udf.so';
DROP FUNCTION IF EXISTS example_udf;

-- 14) INSTALL / UNINSTALL PLUGIN
INSTALL PLUGIN auth_socket SONAME 'auth_socket.so';
UNINSTALL PLUGIN auth_socket;
INSTALL PLUGIN validate_password SONAME 'validate_password.so';
UNINSTALL PLUGIN validate_password;

-- 15) SET 语句（SESSION / GLOBAL / PERSIST）
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER';
SET GLOBAL max_connections = 500;
-- MySQL 8+: PERSIST 将值写入 mysqld_auto.cnf
SET PERSIST innodb_buffer_pool_size = 2147483648;
SET PASSWORD FOR 'app_user'@'localhost' = 'AnotherP@ss';

-- 16) SHOW 语句（诊断/状态/变量）
SHOW VARIABLES LIKE 'max_connections';
SHOW GLOBAL STATUS LIKE 'Threads_connected';
SHOW PROCESSLIST;
SHOW SLAVE STATUS; -- 在旧版本中（复制状态）
SHOW REPLICA STATUS; -- 新版本别名
SHOW BINARY LOGS;
SHOW BINLOG EVENTS IN 'mysql-bin.000003';

-- 17) BINLOG 相关（CHANGE MASTER / START REPLICA / RESET MASTER / PURGE）
-- 配置复制（示意，需替换参数）
-- STOP REPLICA;
-- CHANGE REPLICA SOURCE TO SOURCE_HOST='master.example', SOURCE_USER='repl', SOURCE_PASSWORD='pwd', SOURCE_AUTO_POSITION=1;
-- START REPLICA;
RESET MASTER; -- 删除二进制日志索引与日志（谨慎）
PURGE BINARY LOGS TO 'mysql-bin.000010';
PURGE BINARY LOGS BEFORE NOW() - INTERVAL 7 DAY;

-- 18) FLUSH 语句
FLUSH PRIVILEGES; -- 刷新权限表
FLUSH LOGS; -- 刷新日志文件
FLUSH TABLES; -- 关闭并刷新表
FLUSH HOSTS; -- 清理被阻止的主机
FLUSH STATUS; -- 重置状态统计

-- 19) CACHE/LOAD INDEX INTO CACHE / CACHE INDEX 操作
LOAD INDEX INTO CACHE posts INDEX (idx_user_published);
-- LOAD INDEX INTO CACHE tbl_name [(index_list)] [ENGINE_OPTION]
-- MySQL 8+: 该语句主要用于 MyISAM；InnoDB 无效

-- 20) KILL 语句（终止连接或查询）
SHOW PROCESSLIST;
KILL CONNECTION 12345; -- 终止指定 connection id
KILL QUERY 12346; -- 终止正在运行的查询，但保留连接

-- 21) RESET 语句（RESET MASTER / RESET SLAVE）
RESET MASTER; -- 重置主服务器二进制日志
RESET REPLICA; -- 8.0+: 重置从库复制通道状态
RESET SLAVE ALL; -- 旧版本：删除复制通道信息（谨慎）

-- 22) SHUTDOWN
-- 仅由具有 SHUTDOWN 权限的用户执行
-- SHUTDOWN; -- 关闭 MySQL 服务（谨慎）

-- 23) CREATE USER WITH RESOURCE/ACCOUNT OPTIONS 示例（部分版本）
CREATE USER 'limited'@'localhost' IDENTIFIED BY 'pwd' PASSWORD EXPIRE DEFAULT ACCOUNT UNLOCK;
ALTER USER 'limited'@'localhost' ACCOUNT LOCK;
ALTER USER 'limited'@'localhost' ACCOUNT UNLOCK;

-- 24) CREATE USER WITH REQUIRE SSL / X509
CREATE USER 'ssl_user'@'%' REQUIRE SSL;
CREATE USER 'x509_user'@'%' REQUIRE X509;

-- 25) SHOW PLUGINS / SHOW PLUGIN STATUS
SHOW PLUGINS;

-- 26) INSTALL/UNINSTALL COMPONENTS（示例，插件管理）
-- INSTALL COMPONENT 'file_key_management' SONAME 'file_key_management.so';
-- UNINSTALL COMPONENT 'file_key_management';

-- 27) CACHE INDEX / ALTER INDEX 示例（可见性）
ALTER TABLE products ALTER INDEX idx_products_price_invis VISIBLE; -- 切换索引可见性（8.0+）
ALTER TABLE products ALTER INDEX idx_products_price_invis INVISIBLE;


-- 29) Diagnostic: SHOW ENGINE INNODB STATUS / PERFORMANCE_SCHEMA 查询
SHOW ENGINE INNODB STATUS;
SELECT * FROM performance_schema.threads LIMIT 10;

-- 31) 示例：LOAD DATA 并配合 FLUSH/ANALYZE
-- LOAD DATA INFILE '/tmp/import.csv' INTO TABLE posts FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (user_id,title,body);
FLUSH TABLES WITH READ LOCK; -- 配合备份使用
UNLOCK TABLES;

-- 32) 使用 SHOW to inspect grants / users
SELECT User, Host FROM mysql.user LIMIT 20;
SHOW GRANTS FOR 'ssl_user'@'%';

-- 33) 备份相关（用 SQL 管理）
-- 使用 mysqldump 工具更常见；以下示例为 SQL 层提示
-- FLUSH TABLES WITH READ LOCK; -- 冻结表以做一致性备份
-- SHOW MASTER STATUS; -- 获取二进制日志位点
-- UNLOCK TABLES;

-- 34) 触发器/事件管理（administration 相关）
SHOW EVENTS;
CREATE EVENT ev_cleanup_old_logs
ON SCHEDULE EVERY 1 DAY DO DELETE FROM logs WHERE created_date < DATE_SUB(CURDATE(), INTERVAL 365 DAY);
DROP EVENT IF EXISTS ev_cleanup_old_logs;

-- 35) PERFORMANCE_SCHEMA/INFORMATION_SCHEMA 管理示例
SELECT * FROM information_schema.global_status WHERE VARIABLE_NAME = 'Threads_connected';
SELECT * FROM performance_schema.events_statements_summary_by_digest ORDER BY COUNT_STAR DESC LIMIT 10;

-- 36) 安全相关命令示例：REVOKE GRANT OPTION
REVOKE GRANT OPTION ON example_db.* FROM 'someuser'@'localhost';

-- 37) 代理、角色审计示例（查看 ROLE 的成员）
SELECT * FROM mysql.role_edges LIMIT 20; -- 内置表视图，视 MySQL 版本而定

-- 38) INSTALL PLUGIN with initialization
INSTALL PLUGIN keyring_file SONAME 'keyring_file.so';
UNINSTALL PLUGIN keyring_file;

-- 39) 示例：CACHE INDEX INTO MEMORY（MyISAM 场景）
-- LOAD INDEX INTO CACHE myisam_table INDEX (idx1, idx2);

-- 40) 结束注释：运行这些语句前请确认当前环境与权限。

-- End of mysql_admin_statements.sql
