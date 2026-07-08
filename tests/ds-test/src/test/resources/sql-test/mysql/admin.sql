-- mysql_admin_statements.sql
-- Example: administration status statements
-- Including: ALTER USER, CREATE USER, DROP USER, GRANT/REVOKE, CREATE/DROP ROLE,
-- GRANT/REVOKE PROXY, RENAME USER, ANALYZE/CHECK/CHECKSUM/OPTIMIZE/REPAIR TABLE,
-- CREATE UDF, INSTALL/UNISTAL PLUGIN, SET / SHOW / BINLOG / FLUSH / KILL /RESET / SHUTDOWN, etc.

-- Note: Some statements require administrator privileges (root) and are executed in a suitable MySQL version.

-- 1) CREATE USER
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'S3cureP@ss';
CREATE USER 'readonly'@'%' IDENTIFIED WITH mysql_native_password BY 'readonlypwd';

-- 2) ALTER USER
ALTER USER 'app_user'@'localhost' IDENTIFIED BY 'N3wP@ssw0rd';
ALTER USER 'app_user'@'localhost' PASSWORD EXPIRE; -- Force password expired (some versions)
ALTER USER 'readonly'@'%' IDENTIFIED WITH caching_sha2_password BY 'newreadonly';

-- 3) DROP USER
DROP USER IF EXISTS 'temp'@'localhost';

-- 4) RENAME USER
RENAME USER 'old_user'@'localhost' TO 'new_user'@'localhost';

-- 5) CREATE ROLE / DROP ROLE
CREATE ROLE 'reporting_role';
CREATE ROLE 'devops';
DROP ROLE IF EXISTS 'old_role';

-- 6) GRANT / REVOKE
GRANT SELECT, INSERT, UPDATE ON example_db.* TO 'app_user'@'localhost';
GRANT ALL PRIVILEGES ON example_db.* TO 'admin_user'@'localhost' WITH GRANT OPTION;
REVOKE INSERT ON example_db.* FROM 'app_user'@'localhost';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'admin_user'@'localhost';

-- 7) GRANT ROLE / REVOKE ROLE
GRANT 'reporting_role' TO 'analyst'@'localhost';
REVOKE 'reporting_role' FROM 'analyst'@'localhost';

-- 8) GRANT PROXY / REVOKE PROXY (Active Permission)
GRANT PROXY ON 'target_user'@'localhost' TO 'proxy_user'@'localhost';
REVOKE PROXY ON 'target_user'@'localhost' FROM 'proxy_user'@'localhost';

-- 9) GRANT PROXY WITH ROLE
GRANT PROXY ON 'target_user'@'localhost' TO 'proxy_user'@'localhost' WITH GRANT OPTION;

-- 10) SHOW Permissions Related
SHOW GRANTS FOR 'app_user'@'localhost';
SHOW GRANTS FOR CURRENT_USER();

-- 11) ANALYZE / CHECK / CHECKSUM / OPTIMIZE / REPAIR TABLE
ANALYZE TABLE posts;
CHECK TABLE posts;
CHECKSUM TABLE posts;
OPTIMIZE TABLE posts;
REPAIR TABLE archive_table;

-- (12) ALTER TABLE... ANALIZE / OPTIMIZE Example (Managing Operations)
ALTER TABLE posts ENGINE=InnoDB; -- Example: Changed storage engine requires administrator or rational scene

-- 13) CREATE UDF (user-defined function - relies on shared library)
-- Need to place the shared library in the server loadable path ahead of time and have the corresponding C/C++ compiled
CREATE FUNCTION example_udf RETURNS INTEGER SONAME 'example_udf.so';
DROP FUNCTION IF EXISTS example_udf;

-- 14) INSTALL / UNINSTALL PLUGIN
INSTALL PLUGIN auth_socket SONAME 'auth_socket.so';
UNINSTALL PLUGIN auth_socket;
INSTALL PLUGIN validate_password SONAME 'validate_password.so';
UNINSTALL PLUGIN validate_password;

-- 15) SET Statement (SESSION / GLOBAL / PERSIST)
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER';
SET GLOBAL max_connections = 500;
-- MySQL 8+: PERSIST writes values to mySQLd auto.cnf
SET PERSIST innodb_buffer_pool_size = 2147483648;
SET PASSWORD FOR 'app_user'@'localhost' = 'AnotherP@ss';

-- 16) SHOW statement (diagnostic/state/variant)
SHOW VARIABLES LIKE 'max_connections';
SHOW GLOBAL STATUS LIKE 'Threads_connected';
SHOW PROCESSLIST;
SHOW SLAVE STATUS; -- In old version (reproduction status)
SHOW REPLICA STATUS; -- New version alias
SHOW BINARY LOGS;
SHOW BINLOG EVENTS IN 'mysql-bin.000003';

-- 17) BINLOG Associated (CHANGE MASTER / START REPLICA /RESET MASTER / PURGE)
-- Configure copying (show, replacement parameters)
-- STOP REPLICA;
-- CHANGE REPLICA SOURCE TO SOURCE_HOST='master.example', SOURCE_USER='repl', SOURCE_PASSWORD='pwd', SOURCE_AUTO_POSITION=1;
-- START REPLICA;
RESET MASTER; -- Delete binary log index and log (cautionary)
PURGE BINARY LOGS TO 'mysql-bin.000010';
PURGE BINARY LOGS BEFORE NOW() - INTERVAL 7 DAY;

-- 18) FLUSH statement
FLUSH PRIVILEGES; -- Refresh list
FLUSH LOGS; -- Refresh Log File
FLUSH TABLES; -- Close and refresh tables
FLUSH HOSTS; -- Clear the blocked host
FLUSH STATUS; -- Reset Status Statistics

-- 19) CACHE/LOAD INDEX INTO CACHE / CACHE INDEX operation
LOAD INDEX INTO CACHE posts INDEX (idx_user_published);
-- LOAD INDEX INTO CACHE tbl_name [(index_list)] [ENGINE_OPTION]
-- MySQL 8+: This statement is mainly for MyISAM; InnoDB is invalid

-- 20) KILL statement (termination of connection or query)
SHOW PROCESSLIST;
KILL CONNECTION 12345; -- Termination of designation
KILL QUERY 12346; -- Other Organiser

-- 21) RESET Statement (RESET MASER / RERESET SLAVE)
RESET MASTER; -- Reset Main Server Binary Log
RESET REPLICA; -- 8.0+: Reset copy channel state from library
RESET SLAVE ALL; -- Old version: Delete copy channel information (cautionary)

-- 22) SHUTDOWN
-- Only by users with SHUTDOWN privileges
-- SHUTDOWN; -- Shut down the MySQL service (use with caution)

-- 23) CREATE USER WITH RERESOURCE/ACCOUNT OPTIONS Example (partial version)
CREATE USER 'limited'@'localhost' IDENTIFIED BY 'pwd' PASSWORD EXPIRE DEFAULT ACCOUNT UNLOCK;
ALTER USER 'limited'@'localhost' ACCOUNT LOCK;
ALTER USER 'limited'@'localhost' ACCOUNT UNLOCK;

-- 24) CREATE USER WITH REQUIRE SSL / X509
CREATE USER 'ssl_user'@'%' REQUIRE SSL;
CREATE USER 'x509_user'@'%' REQUIRE X509;

-- 25) SHOW PLUGINS / SHOW PLUGIN STATUS
SHOW PLUGINS;

-- 26) INSTALL/UNINTAL COMPONENTS (example, plugin management)
-- INSTALL COMPONENT 'file_key_management' SONAME 'file_key_management.so';
-- UNINSTALL COMPONENT 'file_key_management';

-- 27) CACHE INDEX / ALTER INDEX Example (visibility)
ALTER TABLE products ALTER INDEX idx_products_price_invis VISIBLE; -- Toggle index visibility (8.0+)
ALTER TABLE products ALTER INDEX idx_products_price_invis INVISIBLE;


-- (29) Diagnostic: SHOW ENGINE INNODB STATTUS / PERFORMANCE SCHEMA Query
SHOW ENGINE INNODB STATUS;
SELECT * FROM performance_schema.threads LIMIT 10;

-- 31) Example: LOAD DATA and FLUSH/ANALIZE
-- LOAD DATA INFILE '/tmp/import.csv' INTO TABLE posts FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' (user_id,title,body);
FLUSH TABLES WITH READ LOCK; -- Match backup use
UNLOCK TABLES;

-- 32) Use SHOW to inspect missions / users
SELECT User, Host FROM mysql.user LIMIT 20;
SHOW GRANTS FOR 'ssl_user'@'%';

-- 33) Backup Related (managed with SQL)
-- Use mySQLdump tools more frequently; the example below is the SQL layer hint
-- FLUSH TABLES WITH READ LOCK; -- Freeze tables for a consistent backup
-- SHOW MASTER STATUS; -- Get the binary log position
-- UNLOCK TABLES;

-- 34) Trigger/incident management (administration related)
SHOW EVENTS;
CREATE EVENT ev_cleanup_old_logs
ON SCHEDULE EVERY 1 DAY DO DELETE FROM logs WHERE created_date < DATE_SUB(CURDATE(), INTERVAL 365 DAY);
DROP EVENT IF EXISTS ev_cleanup_old_logs;

-- 35) PERFORMANCE SCHEMA/INFORMATION SCHEMA Management Example
SELECT * FROM information_schema.global_status WHERE VARIABLE_NAME = 'Threads_connected';
SELECT * FROM performance_schema.events_statements_summary_by_digest ORDER BY COUNT_STAR DESC LIMIT 10;

-- 36) Examples of security-related orders: REVOKE GRANT OPTION
REVOKE GRANT OPTION ON example_db.* FROM 'someuser'@'localhost';

-- 37) Example of agent, role audit (see ROLE members)
SELECT * FROM mysql.role_edges LIMIT 20; -- Internal table view, depending on MySQL version

-- 38) INSTALL PLUGIN with initialization
INSTALL PLUGIN keyring_file SONAME 'keyring_file.so';
UNINSTALL PLUGIN keyring_file;

-- 39) Example: CACHE INDEX INTO MEMORY (MyISAM scene)
-- LOAD INDEX INTO CACHE myisam_table INDEX (idx1, idx2);

-- 40) End comment: Please confirm the current environment and permission before running these statements.

-- End of mysql_admin_statements.sql
