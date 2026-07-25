# 主客体关系行为汇总

本文件收纳至少包含一个行为客体 `target` 的 testcase。每条 SQL 只出现一次，随后逐行列出该 SQL 的全部行为。

SQL 原文中的换行以 `\n` 显示；行为对象保持 fixture 中的 `TargetType(codeLine) resourcePath` 格式。
跨方言或版本完全相同的“SQL + 行为结果”只保留一次；SQL 相同但行为不同的分别保留。

- 来源脚本：304
- 来源 testcase：33439
- 本类 testcase occurrence：2822
- 去重后条目：920

SQL  create table target_tab as select * from source_tab
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/target_tab/ -> Table(1:41~1:51) /test/1/catalog1/schema1/source_tab/
------
SQL  rename table old_tab to new_tab
行为 RENAME Table(1:13~1:20) /test/1/catalog1/schema1/old_tab/ -> Table(1:24~1:31) /test/1/catalog1/schema1/new_tab/
------
SQL  create index idx_t1_name on t1(name)
行为 CREATE Index(1:13~1:24) /test/1/catalog1/schema1/idx_t1_name/ -> Table(1:28~1:30) /test/1/catalog1/schema1/t1/
------
SQL  rename table old_tab to new_tab
行为 RENAME Table(1:13~1:20) /test/1/catalog1/schema0/old_tab/ -> Table(1:24~1:31) /test/1/catalog1/schema0/new_tab/
------
SQL  create index idx_t1_name on t1(name)
行为 CREATE Index(1:13~1:24) /test/1/catalog1/schema0/idx_t1_name/ -> Table(1:28~1:30) /test/1/catalog1/schema0/t1/
------
SQL  GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON test2.* TO PUBLIC@'%';
行为 GRANT Schema(1:49~1:54) /test/1/catalog1/test2/ -> UserOrRole(1:60~1:70) /test/1/PUBLIC@%/
------
SQL  REVOKE SELECT(c1) ON FUNCTION *.* FROM u1@localhost;
行为 REVOKE Instance(1:30~1:33) /test/1/ -> UserOrRole(1:39~1:51) /test/1/u1@localhost/
------
SQL  GRANT SELECT(c1) ON FUNCTION *.* TO u1@localhost;
行为 GRANT Instance(1:29~1:32) /test/1/ -> UserOrRole(1:36~1:48) /test/1/u1@localhost/
------
SQL  GRANT SELECT, INSERT(note), UPDATE ON split_acl56.t TO 'split_acl_56'@'%';
行为 GRANT Table(1:38~1:51) /test/1/catalog1/split_acl56/t/ -> UserOrRole(1:55~1:73) /test/1/split_acl_56@%/
------
SQL  GRANT EXECUTE ON PROCEDURE split_acl56.p TO 'split_acl_56'@'%';
行为 GRANT Procedure(1:27~1:40) /test/1/catalog1/split_acl56/p/ -> UserOrRole(1:44~1:62) /test/1/split_acl_56@%/
------
SQL  GRANT EXECUTE ON FUNCTION split_acl56.f TO 'split_acl_56'@'%';
行为 GRANT Function(1:26~1:39) /test/1/catalog1/split_acl56/f/ -> UserOrRole(1:43~1:61) /test/1/split_acl_56@%/
------
SQL  GRANT ALL PRIVILEGES ON split_acl56.* TO 'split_acl_56'@'%' WITH GRANT OPTION;
行为 GRANT Schema(1:24~1:35) /test/1/catalog1/split_acl56/ -> UserOrRole(1:41~1:59) /test/1/split_acl_56@%/
------
SQL  GRANT PROXY ON 'root'@'%' TO 'split_acl_56'@'%' WITH GRANT OPTION;
行为 GRANT UserOrRole(1:15~1:25) /test/1/root@%/ -> UserOrRole(1:29~1:47) /test/1/split_acl_56@%/
------
SQL  REVOKE SELECT, INSERT(note), UPDATE ON split_acl56.t FROM 'split_acl_56'@'%';
行为 REVOKE Table(1:39~1:52) /test/1/catalog1/split_acl56/t/ -> UserOrRole(1:58~1:76) /test/1/split_acl_56@%/
------
SQL  REVOKE EXECUTE ON PROCEDURE split_acl56.p FROM 'split_acl_56'@'%';
行为 REVOKE Procedure(1:28~1:41) /test/1/catalog1/split_acl56/p/ -> UserOrRole(1:47~1:65) /test/1/split_acl_56@%/
------
SQL  REVOKE EXECUTE ON FUNCTION split_acl56.f FROM 'split_acl_56'@'%';
行为 REVOKE Function(1:27~1:40) /test/1/catalog1/split_acl56/f/ -> UserOrRole(1:46~1:64) /test/1/split_acl_56@%/
------
SQL  REVOKE PROXY ON 'root'@'%' FROM 'split_acl_56'@'%';
行为 REVOKE UserOrRole(1:16~1:26) /test/1/root@%/ -> UserOrRole(1:32~1:50) /test/1/split_acl_56@%/
------
SQL  GRANT SELECT, INSERT ON cda17db.* TO 'cda04a'@'%', 'cda04b'@'%';
行为 GRANT Schema(1:24~1:31) /test/1/catalog1/cda17db/ -> [UserOrRole(1:37~1:49) /test/1/cda04a@%/ ; UserOrRole(1:51~1:63) /test/1/cda04b@%/]
------
SQL  REVOKE SELECT, INSERT ON cda17db.* FROM 'cda05a'@'%', 'cda05b'@'%';
行为 REVOKE Schema(1:25~1:32) /test/1/catalog1/cda17db/ -> [UserOrRole(1:40~1:52) /test/1/cda05a@%/ ; UserOrRole(1:54~1:66) /test/1/cda05b@%/]
------
SQL  REVOKE GRANT OPTION ON cda17db.* FROM 'cda06a'@'%';
行为 REVOKE Schema(1:23~1:30) /test/1/catalog1/cda17db/ -> UserOrRole(1:38~1:50) /test/1/cda06a@%/
------
SQL  GRANT SELECT ON split_acl56.* TO 'sg56a'@'%' IDENTIFIED BY 'Grant56!' REQUIRE SSL WITH GRANT OPTION MAX_QUERIES_PER_HOUR 10 MAX_UPDATES_PER_HOUR 20 MAX_CONNECTIONS_PER_HOUR 30 MAX_USER_CONNECTIONS 4;
行为 GRANT Schema(1:16~1:27) /test/1/catalog1/split_acl56/ -> UserOrRole(1:33~1:44) /test/1/sg56a@%/
------
SQL  GRANT USAGE ON *.* TO 'sg56b'@'%' IDENTIFIED BY PASSWORD '*2470C0C06DEE42FD1618BB99005ADCA2EC9D1E19' REQUIRE X509;
行为 GRANT Instance(1:15~1:18) /test/1/ -> UserOrRole(1:22~1:33) /test/1/sg56b@%/
------
SQL  GRANT SELECT ON cda17db.* TO 'cda14a'@'%' IDENTIFIED WITH mysql_native_password AS '*6C387FC3893DBA1E3BA155E74754DA6682400F26';
行为 GRANT Schema(1:16~1:23) /test/1/catalog1/cda17db/ -> UserOrRole(1:29~1:41) /test/1/cda14a@%/
------
SQL  GRANT PROXY ON 'split_proxy_from'@'%' TO 'split_proxy_to_a'@'%', 'split_proxy_to_b'@'%';
行为 GRANT UserOrRole(1:15~1:37) /test/1/split_proxy_from@%/ -> [UserOrRole(1:41~1:63) /test/1/split_proxy_to_a@%/ ; UserOrRole(1:65~1:87) /test/1/split_proxy_to_b@%/]
------
SQL  REVOKE PROXY ON 'split_proxy_from'@'%' FROM 'split_proxy_to_a'@'%', 'split_proxy_to_b'@'%';
行为 REVOKE UserOrRole(1:16~1:38) /test/1/split_proxy_from@%/ -> [UserOrRole(1:44~1:66) /test/1/split_proxy_to_a@%/ ; UserOrRole(1:68~1:90) /test/1/split_proxy_to_b@%/]
------
SQL  GRANT PROXY ON 'split_proxy_from'@'%' TO 'split_proxy_to_a'@'%' WITH GRANT OPTION;
行为 GRANT UserOrRole(1:15~1:37) /test/1/split_proxy_from@%/ -> UserOrRole(1:41~1:63) /test/1/split_proxy_to_a@%/
------
SQL  REVOKE PROXY ON 'split_proxy_from'@'%' FROM 'split_proxy_to_a'@'%';
行为 REVOKE UserOrRole(1:16~1:38) /test/1/split_proxy_from@%/ -> UserOrRole(1:44~1:66) /test/1/split_proxy_to_a@%/
------
SQL  RENAME USER 'cdra56a'@'localhost' TO 'cdra56c'@'localhost', 'cdra56b'@'localhost' TO 'cdra56d'@'localhost';
行为 RENAME User(1:12~1:33) /test/1/cdra56a@localhost/ -> User(1:37~1:58) /test/1/cdra56c@localhost/
行为 RENAME User(1:60~1:81) /test/1/cdra56b@localhost/ -> User(1:85~1:106) /test/1/cdra56d@localhost/
------
SQL  RENAME USER 'split_life_56_b'@'localhost' TO 'split_life_56_c'@'localhost';
行为 RENAME User(1:12~1:41) /test/1/split_life_56_b@localhost/ -> User(1:45~1:74) /test/1/split_life_56_c@localhost/
------
SQL  GRANT SELECT(id,c) ON codex_next_audit.base_t TO 'codex_next_u'@'localhost';
行为 GRANT Table(1:22~1:45) /test/1/catalog1/codex_next_audit/base_t/ -> UserOrRole(1:49~1:75) /test/1/codex_next_u@localhost/
------
SQL  GRANT REFERENCES(id) ON codex_next_audit.base_t TO 'codex_next_u'@'localhost';
行为 GRANT Table(1:24~1:47) /test/1/catalog1/codex_next_audit/base_t/ -> UserOrRole(1:51~1:77) /test/1/codex_next_u@localhost/
------
SQL  GRANT USAGE ON *.* TO 'codex_next_tls'@'localhost' REQUIRE NONE;
行为 GRANT Instance(1:15~1:18) /test/1/ -> UserOrRole(1:22~1:50) /test/1/codex_next_tls@localhost/
------
SQL  GRANT USAGE ON *.* TO 'codex_next_tls'@'localhost' REQUIRE CIPHER 'x' SUBJECT 's' ISSUER 'i';
行为 GRANT Instance(1:15~1:18) /test/1/ -> UserOrRole(1:22~1:50) /test/1/codex_next_tls@localhost/
------
SQL  GRANT EXECUTE ON * TO `a@`@localhost;
行为 GRANT Schema(1:17~1:18) /test/1/catalog1/schema1/ -> UserOrRole(1:22~1:36) /test/1/a@@localhost/
------
SQL  GRANT CREATE, INSERT, SELECT ON TABLE test.t1 TO CURRENT_USER();
行为 GRANT Table(1:38~1:45) /test/1/catalog1/test/t1/ -> UserOrRole(1:49~1:61) /test/1/
------
SQL  GRANT INSERT ON *.* TO CURRENT_USER() IDENTIFIED BY 'NewPass!';
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:35) /test/1/
------
SQL  GRANT ALL ON *.*TO 'split_native_gap'@'localhost' WITH GRANT OPTION;
行为 GRANT Instance(1:13~1:16) /test/1/ -> UserOrRole(1:19~1:49) /test/1/split_native_gap@localhost/
------
SQL  RENAME USER CURRENT_USER TO 'renamed'@'localhost';
行为 RENAME User(1:12~1:24) /test/1/ -> User(1:28~1:49) /test/1/renamed@localhost/
------
SQL  GRANT USAGE ON PROCEDURE split_acl_native.split_native_acl_p TO 'split_acl_0720b'@'%' WITH GRANT OPTION;
行为 GRANT Procedure(1:25~1:60) /test/1/catalog1/split_acl_native/split_native_acl_p/ -> UserOrRole(1:64~1:85) /test/1/split_acl_0720b@%/
------
SQL  GRANT USAGE ON *.* TO 'split_tls_and'@'localhost' REQUIRE CIPHER 'DHE-RSA-AES256-SHA' AND ISSUER '/C=US/O=Example/CN=issuer' AND SUBJECT '/C=US/O=Example/CN=client';
行为 GRANT Instance(1:15~1:18) /test/1/ -> UserOrRole(1:22~1:49) /test/1/split_tls_and@localhost/
------
SQL  GRANT ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* TO 'static_56'@'%';
行为 GRANT Instance(1:346~1:349) /test/1/ -> UserOrRole(1:353~1:368) /test/1/static_56@%/
------
SQL  REVOKE ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* FROM 'static_56'@'%';
行为 REVOKE Instance(1:347~1:350) /test/1/ -> UserOrRole(1:356~1:371) /test/1/static_56@%/
------
SQL  /*!50000 GRANT SELECT ON split_exec_comment.* TO 'split_exec_56'@'%' */;
行为 GRANT Schema(1:25~1:43) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:49~1:68) /test/1/split_exec_56@%/
------
SQL  /*!50000 REVOKE SELECT ON split_exec_comment.* FROM 'split_exec_56'@'%' */;
行为 REVOKE Schema(1:26~1:44) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:52~1:71) /test/1/split_exec_56@%/
------
SQL  CREATE TABLE codex_create_audit_key_prefix (c1 VARCHAR(33), KEY USING BTREE (c1));
行为 CREATE Index(1:60~1:80) /test/1/catalog1/schema1/ -> Table(1:13~1:42) /test/1/catalog1/schema1/codex_create_audit_key_prefix/
------
SQL  CREATE TABLE codex_create_audit_key_twice (c1 VARCHAR(33), KEY USING BTREE (c1) USING HASH) ENGINE=MEMORY;
行为 CREATE Index(1:59~1:90) /test/1/catalog1/schema1/ -> Table(1:13~1:41) /test/1/catalog1/schema1/codex_create_audit_key_twice/
------
SQL  CREATE TABLE codex_year_union_result SELECT c1 FROM codex_year_union_source WHERE c1=1 UNION SELECT c2 FROM codex_year_union_source WHERE c2<2000;
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/codex_year_union_result/ -> Table(1:52~1:75) /test/1/catalog1/schema1/codex_year_union_source/
------
SQL  CREATE TABLE bit_widths(b1 BIT(1) NOT NULL DEFAULT b'1',b2 BIT(2) DEFAULT b'10',b7 BIT(7),b9 BIT(9),b13 BIT(13),b31 BIT(31),b63 BIT(63),PRIMARY KEY(b1,b2),UNIQUE KEY uq_b7(b7),KEY idx_b9_b13(b9,b13));
行为 CREATE Index(1:166~1:171) /test/1/catalog1/schema1/uq_b7/ -> Table(1:13~1:23) /test/1/catalog1/schema1/bit_widths/
行为 CREATE Index(1:180~1:190) /test/1/catalog1/schema1/idx_b9_b13/ -> Table(1:13~1:23) /test/1/catalog1/schema1/bit_widths/
行为 CREATE Constraint(1:136~1:154) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:155~1:175) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE bit_widths MODIFY COLUMN b63 BIT(64) NOT NULL DEFAULT b'1',ADD COLUMN b8 BIT(8) NULL AFTER b7,ADD KEY idx_b8(b8);
行为 CREATE Index(1:114~1:120) /test/1/catalog1/schema1/idx_b8/ -> Table(1:12~1:22) /test/1/catalog1/schema1/bit_widths/
------
SQL  CREATE TABLE time_widths(id INT PRIMARY KEY,t TIME,t0 TIME(0),t1 TIME(1),t2 TIME(2),t3 TIME(3),t4 TIME(4),t5 TIME(5),t6 TIME(6),KEY idx_t6(t6));
行为 CREATE Index(1:132~1:138) /test/1/catalog1/schema1/idx_t6/ -> Table(1:13~1:24) /test/1/catalog1/schema1/time_widths/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE time_widths MODIFY t TIME(6) NOT NULL DEFAULT '00:00:00.000000',ADD UNIQUE KEY uk_t5(t5);
行为 CREATE Index(1:91~1:96) /test/1/catalog1/schema1/uk_t5/ -> Table(1:12~1:23) /test/1/catalog1/schema1/time_widths/
------
SQL  CREATE TABLE datetime_widths(id INT PRIMARY KEY,dt DATETIME,dt0 DATETIME(0),dt1 DATETIME(1),dt2 DATETIME(2),dt3 DATETIME(3),dt4 DATETIME(4),dt5 DATETIME(5),dt6 DATETIME(6),ts TIMESTAMP NULL,ts0 TIMESTAMP(0) NULL,ts1 TIMESTAMP(1) NULL,ts2 TIMESTAMP(2) NULL,ts3 TIMESTAMP(3) NULL,ts4 TIMESTAMP(4) NULL,ts5 TIMESTAMP(5) NULL,ts6 TIMESTAMP(6) NULL,KEY idx_dt6(dt6),UNIQUE KEY uk_ts6(ts6));
行为 CREATE Index(1:348~1:355) /test/1/catalog1/schema1/idx_dt6/ -> Table(1:13~1:28) /test/1/catalog1/schema1/datetime_widths/
行为 CREATE Index(1:372~1:378) /test/1/catalog1/schema1/uk_ts6/ -> Table(1:13~1:28) /test/1/catalog1/schema1/datetime_widths/
行为 CREATE Constraint(1:36~1:47) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:361~1:383) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE datetime_widths MODIFY dt DATETIME(6) NOT NULL DEFAULT '2000-01-01 00:00:00.000000',MODIFY ts TIMESTAMP(6) NULL DEFAULT NULL,ADD KEY idx_dt5_ts5(dt5,ts5);
行为 CREATE Index(1:145~1:156) /test/1/catalog1/schema1/idx_dt5_ts5/ -> Table(1:12~1:27) /test/1/catalog1/schema1/datetime_widths/
------
SQL  CREATE TABLE split_type_enum_set.es_core (\n      id INT PRIMARY KEY,\n      e_basic ENUM('','alpha','two words','quote''d','10','trailing ') NOT NULL DEFAULT 'alpha',\n      e_case ENUM('a','A') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,\n      s_basic SET('red','green','blue','two words','quote''d') DEFAULT 'red,blue',\n      s_case SET('a','A') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,\n      KEY idx_e (e_basic),\n      KEY idx_s (s_basic)\n    );
行为 CREATE Index(7:10~7:15) /test/1/catalog1/schema1/idx_e/ -> Table(1:13~1:40) /test/1/catalog1/split_type_enum_set/es_core/
行为 CREATE Index(8:10~8:15) /test/1/catalog1/schema1/idx_s/ -> Table(1:13~1:40) /test/1/catalog1/split_type_enum_set/es_core/
行为 CREATE Constraint(2:13~2:24) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE integer_lifecycle (\n  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,\n  tiny_signed TINYINT SIGNED NOT NULL DEFAULT -1,\n  tiny_unsigned TINYINT(3) UNSIGNED ZEROFILL DEFAULT 1,\n  small_signed SMALLINT SIGNED DEFAULT -2,\n  small_unsigned SMALLINT(5) UNSIGNED DEFAULT 2,\n  medium_signed MEDIUMINT SIGNED DEFAULT -3,\n  medium_unsigned MEDIUMINT(8) UNSIGNED DEFAULT 3,\n  int_signed INT SIGNED DEFAULT -4,\n  int_unsigned INTEGER(10) UNSIGNED DEFAULT 4,\n  big_signed BIGINT SIGNED DEFAULT -5,\n  big_unsigned BIGINT(20) UNSIGNED DEFAULT 5,\n  bool_alias BOOL DEFAULT FALSE,\n  boolean_alias BOOLEAN DEFAULT TRUE,\n  note VARCHAR(32),\n  UNIQUE KEY uq_integer_unsigned (int_unsigned),\n  KEY idx_integer_widths (tiny_signed,small_signed,medium_signed,big_signed)\n);
行为 CREATE Index(16:13~16:32) /test/1/catalog1/schema1/uq_integer_unsigned/ -> Table(1:13~1:30) /test/1/catalog1/schema1/integer_lifecycle/
行为 CREATE Index(17:6~17:24) /test/1/catalog1/schema1/idx_integer_widths/ -> Table(1:13~1:30) /test/1/catalog1/schema1/integer_lifecycle/
行为 CREATE Constraint(2:36~2:47) /test/1/catalog1/schema1/
行为 CREATE Constraint(16:2~16:47) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE numeric_lifecycle (\n  id INT PRIMARY KEY,\n  decimal_value DECIMAL(20,6) UNSIGNED ZEROFILL,\n  numeric_value NUMERIC(12,4) SIGNED,\n  fixed_value FIXED(18,2),\n  float_value FLOAT(24) UNSIGNED,\n  float_scale FLOAT(10,3) ZEROFILL,\n  double_value DOUBLE PRECISION(30,10) UNSIGNED,\n  real_value REAL(12,4) ZEROFILL,\n  note VARCHAR(32),\n  UNIQUE KEY uq_numeric_value (numeric_value),\n  KEY idx_exact_values (decimal_value,fixed_value),\n  KEY idx_approximate_values (float_value,double_value,real_value)\n);
行为 CREATE Index(11:13~11:29) /test/1/catalog1/schema1/uq_numeric_value/ -> Table(1:13~1:30) /test/1/catalog1/schema1/numeric_lifecycle/
行为 CREATE Index(12:6~12:22) /test/1/catalog1/schema1/idx_exact_values/ -> Table(1:13~1:30) /test/1/catalog1/schema1/numeric_lifecycle/
行为 CREATE Index(13:6~13:28) /test/1/catalog1/schema1/idx_approximate_values/ -> Table(1:13~1:30) /test/1/catalog1/schema1/numeric_lifecycle/
行为 CREATE Constraint(2:9~2:20) /test/1/catalog1/schema1/
行为 CREATE Constraint(11:2~11:45) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE codex_constraint_c05 ADD CONSTRAINT UNIQUE INDEX uq_c05 (id);
行为 CREATE Index(1:61~1:67) /test/1/catalog1/schema1/uq_c05/ -> Table(1:12~1:32) /test/1/catalog1/schema1/codex_constraint_c05/
行为 CREATE Constraint(1:33~1:72) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE codex_create_audit_key_ctas (KEY (b)) SELECT 1 AS b;
行为 CREATE Index(1:42~1:49) /test/1/catalog1/schema1/ -> Table(1:13~1:40) /test/1/catalog1/schema1/codex_create_audit_key_ctas/
------
SQL  ALTER TABLE t2 CHANGE b2 b2 VARCHAR(255) CHARSET latin1, DROP FOREIGN KEY fk, ADD CONSTRAINT fk FOREIGN KEY (b2) REFERENCES t1(b), ADD INDEX idx(a2), ALGORITHM=INPLACE;
行为 CREATE Index(1:141~1:144) /test/1/catalog1/schema1/idx/ -> Table(1:12~1:14) /test/1/catalog1/schema1/t2/
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t2/ -> Table(1:124~1:126) /test/1/catalog1/schema1/t1/
行为 DROP Constraint(1:74~1:76) /test/1/catalog1/schema1/fk/
行为 CREATE Constraint(1:93~1:95) /test/1/catalog1/schema1/fk/
------
SQL  ALTER TABLE t2 ADD INDEX(fld2), DROP FOREIGN KEY fidx, ALGORITHM=INPLACE;
行为 CREATE Index(1:15~1:30) /test/1/catalog1/schema1/ -> Table(1:12~1:14) /test/1/catalog1/schema1/t2/
行为 DROP Constraint(1:49~1:53) /test/1/catalog1/schema1/fidx/
------
SQL  CREATE TABLE codex_constraint_c11 (id INT PRIMARY KEY, parent_id INT, CONSTRAINT FOREIGN KEY fk_idx_c11 (parent_id) REFERENCES codex_constraint_c11(id));
行为 CREATE Index(1:93~1:103) /test/1/catalog1/schema1/fk_idx_c11/ -> Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c11/
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c11/ -> Table(1:127~1:147) /test/1/catalog1/schema1/codex_constraint_c11/
行为 CREATE Constraint(1:42~1:53) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:70~1:151) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE split_idx.t_alter ADD INDEX idx_add TYPE BTREE (c(16) ASC, id DESC) COMMENT 'alter index', ALGORITHM=INPLACE, LOCK=NONE;
行为 CREATE Index(1:40~1:47) /test/1/catalog1/schema1/idx_add/ -> Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
------
SQL  ALTER TABLE split_idx.t_alter ADD UNIQUE KEY idx_unique(email(32), id) USING HASH;
行为 CREATE Index(1:45~1:55) /test/1/catalog1/schema1/idx_unique/ -> Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
------
SQL  ALTER TABLE split_idx.t_alter ADD FULLTEXT INDEX idx_ft(content) WITH PARSER ngram COMMENT 'alter fulltext';
行为 CREATE Index(1:49~1:55) /test/1/catalog1/schema1/idx_ft/ -> Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
------
SQL  ALTER TABLE split_idx.t_alter ADD SPATIAL INDEX idx_sp(geo) COMMENT 'alter spatial';
行为 CREATE Index(1:48~1:54) /test/1/catalog1/schema1/idx_sp/ -> Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
------
SQL  ALTER TABLE split_idx.t_alter DROP INDEX idx_add, DROP KEY idx_unique;
行为 DROP Index(1:41~1:48) /test/1/catalog1/schema1/idx_add/ -> Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
行为 DROP Index(1:59~1:69) /test/1/catalog1/schema1/idx_unique/ -> Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
------
SQL  CREATE INDEX idx_type TYPE BTREE ON split_idx.t_common (id);
行为 CREATE Index(1:13~1:21) /test/1/catalog1/schema1/idx_type/ -> Table(1:36~1:54) /test/1/catalog1/split_idx/t_common/
------
SQL  CREATE INDEX idx_common USING BTREE ON split_idx.t_common (name(16) ASC, id DESC) KEY_BLOCK_SIZE=8 COMMENT 'common index' ALGORITHM=DEFAULT LOCK=DEFAULT;
行为 CREATE Index(1:13~1:23) /test/1/catalog1/schema1/idx_common/ -> Table(1:39~1:57) /test/1/catalog1/split_idx/t_common/
------
SQL  CREATE UNIQUE INDEX idx_unique ON split_idx.t_common (email(32), id) USING HASH ALGORITHM=INPLACE LOCK=NONE;
行为 CREATE Index(1:20~1:30) /test/1/catalog1/schema1/idx_unique/ -> Table(1:34~1:52) /test/1/catalog1/split_idx/t_common/
------
SQL  CREATE INDEX idx_rtree USING RTREE ON split_idx.t_common (geo);
行为 CREATE Index(1:13~1:22) /test/1/catalog1/schema1/idx_rtree/ -> Table(1:38~1:56) /test/1/catalog1/split_idx/t_common/
------
SQL  CREATE FULLTEXT INDEX idx_fulltext ON split_idx.t_common (content) WITH PARSER ngram COMMENT 'fulltext parser' ALGORITHM=COPY LOCK=SHARED;
行为 CREATE Index(1:22~1:34) /test/1/catalog1/schema1/idx_fulltext/ -> Table(1:38~1:56) /test/1/catalog1/split_idx/t_common/
------
SQL  CREATE SPATIAL INDEX idx_spatial ON split_idx.t_common (geo) COMMENT 'spatial index' ALGORITHM=DEFAULT LOCK=EXCLUSIVE;
行为 CREATE Index(1:21~1:32) /test/1/catalog1/schema1/idx_spatial/ -> Table(1:36~1:54) /test/1/catalog1/split_idx/t_common/
------
SQL  DROP INDEX idx_type ON split_idx.t_common LOCK DEFAULT ALGORITHM DEFAULT;
行为 DROP Index(1:11~1:19) /test/1/catalog1/schema1/idx_type/ -> Table(1:23~1:41) /test/1/catalog1/split_idx/t_common/
------
SQL  DROP INDEX idx_common ON split_idx.t_common ALGORITHM=INPLACE LOCK=NONE;
行为 DROP Index(1:11~1:21) /test/1/catalog1/schema1/idx_common/ -> Table(1:25~1:43) /test/1/catalog1/split_idx/t_common/
------
SQL  DROP INDEX idx_unique ON split_idx.t_common ALGORITHM=COPY LOCK=SHARED;
行为 DROP Index(1:11~1:21) /test/1/catalog1/schema1/idx_unique/ -> Table(1:25~1:43) /test/1/catalog1/split_idx/t_common/
------
SQL  DROP INDEX `PRIMARY` ON split_index.parent_t;
行为 DROP Index(1:11~1:20) /test/1/catalog1/schema1/PRIMARY/ -> Table(1:24~1:44) /test/1/catalog1/split_index/parent_t/
------
SQL  CREATE VIEW split_opt_hints_index.v1 AS SELECT /*+ NO_INDEX(t1 i_a,i_b) */ a FROM split_opt_hints_index.t1 AS t1 WHERE b IN (SELECT /*+ NO_INDEX(t2 i_ab,i_b) */ a FROM split_opt_hints_index.t1 AS t2 WHERE a>3) ORDER BY a;
行为 CREATE View(1:12~1:36) /test/1/catalog1/split_opt_hints_index/v1/ -> Table(1:82~1:106) /test/1/catalog1/split_opt_hints_index/t1/
------
SQL  CREATE VIEW split_opt_hints_index.v2 AS SELECT /*+ INDEX_MERGE(t1) */ a FROM split_opt_hints_index.t1 AS t1 WHERE a=1 AND b=2 AND c=3;
行为 CREATE View(1:12~1:36) /test/1/catalog1/split_opt_hints_index/v2/ -> Table(1:77~1:101) /test/1/catalog1/split_opt_hints_index/t1/
------
SQL  CREATE TABLE temporal_indexed(dt DATETIME(6) NOT NULL,ts TIMESTAMP(6) NOT NULL,payload VARCHAR(20),PRIMARY KEY(dt),UNIQUE KEY uk_ts(ts),KEY idx_ts_dt(ts,dt));
行为 CREATE Index(1:126~1:131) /test/1/catalog1/schema1/uk_ts/ -> Table(1:13~1:29) /test/1/catalog1/schema1/temporal_indexed/
行为 CREATE Index(1:140~1:149) /test/1/catalog1/schema1/idx_ts_dt/ -> Table(1:13~1:29) /test/1/catalog1/schema1/temporal_indexed/
行为 CREATE Constraint(1:99~1:114) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:115~1:135) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE str_index (\n  c CHAR(20),\n  v VARCHAR(300),\n  b BINARY(20),\n  vb VARBINARY(300),\n  KEY idx_c(c),\n  KEY idx_v(v(12)),\n  KEY idx_b(b),\n  KEY idx_vb(vb(12))\n) CHARACTER SET latin1;
行为 CREATE Index(6:6~6:11) /test/1/catalog1/schema1/idx_c/ -> Table(1:13~1:22) /test/1/catalog1/schema1/str_index/
行为 CREATE Index(7:6~7:11) /test/1/catalog1/schema1/idx_v/ -> Table(1:13~1:22) /test/1/catalog1/schema1/str_index/
行为 CREATE Index(8:6~8:11) /test/1/catalog1/schema1/idx_b/ -> Table(1:13~1:22) /test/1/catalog1/schema1/str_index/
行为 CREATE Index(9:6~9:12) /test/1/catalog1/schema1/idx_vb/ -> Table(1:13~1:22) /test/1/catalog1/schema1/str_index/
------
SQL  ALTER TABLE str_index DROP KEY idx_v, ADD UNIQUE KEY uk_v(v(16));
行为 DROP Index(1:31~1:36) /test/1/catalog1/schema1/idx_v/ -> Table(1:12~1:21) /test/1/catalog1/schema1/str_index/
行为 CREATE Index(1:53~1:57) /test/1/catalog1/schema1/uk_v/ -> Table(1:12~1:21) /test/1/catalog1/schema1/str_index/
------
SQL  CREATE TABLE lob_indexes (\n  tb TINYBLOB,\n  b BLOB,\n  mb MEDIUMBLOB,\n  lb LONGBLOB,\n  tt TINYTEXT,\n  t TEXT,\n  mt MEDIUMTEXT,\n  lt LONGTEXT,\n  KEY idx_tb(tb(8)),\n  KEY idx_b(b(16)),\n  KEY idx_mb(mb(24)),\n  KEY idx_lb(lb(32)),\n  KEY idx_tt(tt(8)),\n  KEY idx_t(t(16)),\n  KEY idx_mt(mt(24)),\n  KEY idx_lt(lt(32))\n) CHARACTER SET latin1;
行为 CREATE Index(10:6~10:12) /test/1/catalog1/schema1/idx_tb/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(11:6~11:11) /test/1/catalog1/schema1/idx_b/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(12:6~12:12) /test/1/catalog1/schema1/idx_mb/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(13:6~13:12) /test/1/catalog1/schema1/idx_lb/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(14:6~14:12) /test/1/catalog1/schema1/idx_tt/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(15:6~15:11) /test/1/catalog1/schema1/idx_t/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(16:6~16:12) /test/1/catalog1/schema1/idx_mt/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(17:6~17:12) /test/1/catalog1/schema1/idx_lt/ -> Table(1:13~1:24) /test/1/catalog1/schema1/lob_indexes/
------
SQL  ALTER TABLE lob_indexes DROP KEY idx_b, ADD UNIQUE KEY uk_b(b(16)), ADD UNIQUE KEY uk_t(t(16));
行为 DROP Index(1:33~1:38) /test/1/catalog1/schema1/idx_b/ -> Table(1:12~1:23) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(1:55~1:59) /test/1/catalog1/schema1/uk_b/ -> Table(1:12~1:23) /test/1/catalog1/schema1/lob_indexes/
行为 CREATE Index(1:83~1:87) /test/1/catalog1/schema1/uk_t/ -> Table(1:12~1:23) /test/1/catalog1/schema1/lob_indexes/
------
SQL  CREATE UNIQUE INDEX uq_es_enum ON split_type_enum_set.es_core(e_case);
行为 CREATE Index(1:20~1:30) /test/1/catalog1/schema1/uq_es_enum/ -> Table(1:34~1:61) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  CREATE INDEX idx_es_set ON split_type_enum_set.es_core(s_case);
行为 CREATE Index(1:13~1:23) /test/1/catalog1/schema1/idx_es_set/ -> Table(1:27~1:54) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  CREATE INDEX idx_integer_mixed\nON integer_lifecycle (tiny_unsigned,small_unsigned,medium_unsigned,int_unsigned,big_unsigned);
行为 CREATE Index(1:13~1:30) /test/1/catalog1/schema1/idx_integer_mixed/ -> Table(2:3~2:20) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  CREATE INDEX idx_numeric_mixed\nON numeric_lifecycle (decimal_value,numeric_value,float_value,double_value);
行为 CREATE Index(1:13~1:30) /test/1/catalog1/schema1/idx_numeric_mixed/ -> Table(2:3~2:20) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  CREATE SPATIAL INDEX gap_sx ON gap_geo(g DESC) KEY_BLOCK_SIZE=4 COMMENT 'spatial';
行为 CREATE Index(1:21~1:27) /test/1/catalog1/schema1/gap_sx/ -> Table(1:31~1:38) /test/1/catalog1/schema1/gap_geo/
------
SQL  CREATE TABLE ft_inline(body TEXT,FULLTEXT KEY ft_body(body) WITH PARSER ngram) ENGINE=InnoDB;
行为 CREATE Index(1:46~1:53) /test/1/catalog1/schema1/ft_body/ -> Table(1:13~1:22) /test/1/catalog1/schema1/ft_inline/
------
SQL  CREATE TABLE idx_inline_block(a INT,KEY k_a(a) KEY_BLOCK_SIZE=8) ENGINE=MyISAM;
行为 CREATE Index(1:40~1:43) /test/1/catalog1/schema1/k_a/ -> Table(1:13~1:29) /test/1/catalog1/schema1/idx_inline_block/
------
SQL  CREATE TABLE idx_inline_type(a INT,KEY k_a TYPE BTREE(a));
行为 CREATE Index(1:39~1:42) /test/1/catalog1/schema1/k_a/ -> Table(1:13~1:28) /test/1/catalog1/schema1/idx_inline_type/
------
SQL  ALTER TABLE codex_alter_audit_t DROP INDEX key1, ADD INDEX key1(fld1) COMMENT 'test', ALGORITHM=INPLACE;
行为 DROP Index(1:43~1:47) /test/1/catalog1/schema1/key1/ -> Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
行为 CREATE Index(1:59~1:63) /test/1/catalog1/schema1/key1/ -> Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  CREATE INDEX idx_repeat_block ON t1 (a) KEY_BLOCK_SIZE = 1 KEY_BLOCK_SIZE = 1;
行为 CREATE Index(1:13~1:29) /test/1/catalog1/schema1/idx_repeat_block/ -> Table(1:33~1:35) /test/1/catalog1/schema1/t1/
------
SQL  CREATE INDEX idx_repeat_comment ON t1 (b) COMMENT 'first' COMMENT 'second';
行为 CREATE Index(1:13~1:31) /test/1/catalog1/schema1/idx_repeat_comment/ -> Table(1:35~1:37) /test/1/catalog1/schema1/t1/
------
SQL  CREATE INDEX idx_dual_using USING BTREE ON t1 (c) USING BTREE;
行为 CREATE Index(1:13~1:27) /test/1/catalog1/schema1/idx_dual_using/ -> Table(1:43~1:45) /test/1/catalog1/schema1/t1/
------
SQL  CREATE INDEX type TYPE BTREE ON t1 (e);
行为 CREATE Index(1:13~1:17) /test/1/catalog1/schema1/type/ -> Table(1:32~1:34) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE split_parser_ansi_b SELECT "blah" - 1 FROM split_parser_ansi_a;
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/split_parser_ansi_b/ -> Table(1:56~1:75) /test/1/catalog1/schema1/split_parser_ansi_a/
------
SQL  CREATE TRIGGER trigger1 BEFORE INSERT ON t1 FOR EACH ROW SET default_storage_engine = NEW.INNODB;
行为 CREATE Trigger(1:15~1:23) /test/1/catalog1/schema1/trigger1/ -> Table(1:41~1:43) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:61~1:83) /test/1/default_storage_engine/
------
SQL  CREATE TABLE split_type_enum_set.es_key_partition (\n      id INT,\n      e ENUM('one','two','three'),\n      s SET('red','green','blue'),\n      KEY idx_e (e),\n      KEY idx_s (s)\n    )\n    PARTITION BY KEY(e) PARTITIONS 3;
行为 CREATE Index(5:10~5:15) /test/1/catalog1/schema1/idx_e/ -> Table(1:13~1:49) /test/1/catalog1/split_type_enum_set/es_key_partition/
行为 CREATE Index(6:10~6:15) /test/1/catalog1/schema1/idx_s/ -> Table(1:13~1:49) /test/1/catalog1/split_type_enum_set/es_key_partition/
------
SQL  CREATE TABLE spatial_partitioned (\n  id INT NOT NULL,\n  g GEOMETRY,\n  p POINT,\n  KEY (id)\n) PARTITION BY HASH(id) PARTITIONS 2;
行为 CREATE Index(5:2~5:10) /test/1/catalog1/schema1/ -> Table(1:13~1:32) /test/1/catalog1/schema1/spatial_partitioned/
------
SQL  CREATE TABLE split_partition_ctas (a INT, b CHAR(55), PRIMARY KEY(a)) ENGINE=InnoDB PARTITION BY KEY() AS SELECT * FROM split_partition_source;
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/split_partition_ctas/ -> Table(1:120~1:142) /test/1/catalog1/schema1/split_partition_source/
行为 CREATE Constraint(1:54~1:68) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE codex_create_audit_union_select UNION=(codex_create_audit_src) SELECT * FROM codex_create_audit_src;
行为 CREATE Table(1:13~1:44) /test/1/catalog1/schema1/codex_create_audit_union_select/ -> Table(1:90~1:112) /test/1/catalog1/schema1/codex_create_audit_src/
------
SQL  CREATE TABLE analyse_ctas SELECT * FROM analyse_int PROCEDURE ANALYSE();
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/analyse_ctas/ -> Table(1:40~1:51) /test/1/catalog1/schema1/analyse_int/
------
SQL  CREATE VIEW analyse_bad_view AS SELECT * FROM analyse_int PROCEDURE ANALYSE();
行为 CREATE View(1:12~1:28) /test/1/catalog1/schema1/analyse_bad_view/ -> Table(1:46~1:57) /test/1/catalog1/schema1/analyse_int/
------
SQL  CREATE TABLE bitwise_native_result CHARACTER SET utf8mb4 AS SELECT vbin1 & vbin2 AS and_value, vbin1 | vbin2 AS or_value, vbin1 ^ vbin2 AS xor_value FROM bitwise_native_t WHERE id = 99;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/bitwise_native_result/ -> Table(1:154~1:170) /test/1/catalog1/schema1/bitwise_native_t/
------
SQL  CREATE TABLE gcat_ctas AS SELECT GROUP_CONCAT(c ORDER BY b) AS gc FROM t1;
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/gcat_ctas/ -> Table(1:71~1:73) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:33~1:45) /test/1/catalog1/schema1/GROUP_CONCAT/
------
SQL  CREATE TABLE agg_ctas SELECT MAX(c),MIN(c) FROM fg;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/agg_ctas/ -> Table(1:48~1:50) /test/1/catalog1/schema1/fg/
行为 CALL Function(1:29~1:32) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:36~1:39) /test/1/catalog1/schema1/MIN/
------
SQL  CREATE TABLE ct_datetime SELECT MAX(b) FROM dt GROUP BY a;
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/ct_datetime/ -> Table(1:44~1:46) /test/1/catalog1/schema1/dt/
行为 CALL Function(1:32~1:35) /test/1/catalog1/schema1/MAX/
------
SQL  CREATE TABLE ct_derived SELECT f2 FROM (SELECT MAX(NOW()) f2 FROM dt) a;
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/ct_derived/ -> Table(1:66~1:68) /test/1/catalog1/schema1/dt/
行为 CALL Function(1:47~1:50) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:51~1:54) /test/1/catalog1/schema1/NOW/
------
SQL  CREATE VIEW codex_func_in.v_not_in AS SELECT * FROM codex_func_in.int_key WHERE a NOT IN (45);
行为 CREATE View(1:12~1:34) /test/1/catalog1/codex_func_in/v_not_in/ -> Table(1:52~1:73) /test/1/catalog1/codex_func_in/int_key/
------
SQL  CREATE TABLE func_least_faq (faq_group_id INT NOT NULL, faq_id INT NOT NULL, access_id SMALLINT, UNIQUE KEY uq_faq_id (faq_id), KEY ix_group_faq (faq_group_id,faq_id));
行为 CREATE Index(1:108~1:117) /test/1/catalog1/schema1/uq_faq_id/ -> Table(1:13~1:27) /test/1/catalog1/schema1/func_least_faq/
行为 CREATE Index(1:132~1:144) /test/1/catalog1/schema1/ix_group_faq/ -> Table(1:13~1:27) /test/1/catalog1/schema1/func_least_faq/
行为 CREATE Constraint(1:97~1:126) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE func_least_access_rank (access_id SMALLINT NOT NULL, `rank` SMALLINT NOT NULL, KEY ix_access_id (access_id));
行为 CREATE Index(1:96~1:108) /test/1/catalog1/schema1/ix_access_id/ -> Table(1:13~1:35) /test/1/catalog1/schema1/func_least_access_rank/
------
SQL  CREATE TABLE math_ctas_round AS SELECT number_value, ROUND(number_value,digits_value) AS rounded_value FROM math_round_context;
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/math_ctas_round/ -> Table(1:108~1:126) /test/1/catalog1/schema1/math_round_context/
行为 CALL Function(1:53~1:58) /test/1/catalog1/schema1/ROUND/
------
SQL  CREATE VIEW rb_func_view AS SELECT rb_fail() FROM rb_select;
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/rb_func_view/ -> Table(1:50~1:59) /test/1/catalog1/schema1/rb_select/
行为 CALL Function(1:35~1:42) /test/1/catalog1/schema1/rb_fail/
------
SQL  CREATE TABLE rb_fail_ctas AS SELECT rb_fail() FROM rb_select;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/rb_fail_ctas/ -> Table(1:51~1:60) /test/1/catalog1/schema1/rb_select/
行为 CALL Function(1:36~1:43) /test/1/catalog1/schema1/rb_fail/
------
SQL  CREATE TABLE base64_roundtrip_ctas AS SELECT FROM_BASE64(TO_BASE64(binary_value)) AS decoded_value FROM base64_binary_values;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/base64_roundtrip_ctas/ -> Table(1:104~1:124) /test/1/catalog1/schema1/base64_binary_values/
行为 CALL Function(1:45~1:56) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:57~1:66) /test/1/catalog1/schema1/TO_BASE64/
------
SQL  CREATE VIEW str_coercibility_view AS SELECT COERCIBILITY(CONCAT(c1,c2)) AS coercibility_value, COLLATION(CONCAT(c1,c2)) AS collation_name FROM str_collation_values;
行为 CREATE View(1:12~1:33) /test/1/catalog1/schema1/str_coercibility_view/ -> Table(1:143~1:163) /test/1/catalog1/schema1/str_collation_values/
行为 CALL Function(1:44~1:56) /test/1/catalog1/schema1/COERCIBILITY/
行为 CALL Function(1:57~1:63) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:95~1:104) /test/1/catalog1/schema1/COLLATION/
------
SQL  CREATE VIEW str_crc_view AS SELECT CRC32(value_text) AS crc_value FROM str_crc;
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/str_crc_view/ -> Table(1:71~1:78) /test/1/catalog1/schema1/str_crc/
行为 CALL Function(1:35~1:40) /test/1/catalog1/schema1/CRC32/
------
SQL  CREATE TABLE str_crc_ctas AS SELECT CRC32(value_text) AS crc_value FROM str_crc;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/str_crc_ctas/ -> Table(1:72~1:79) /test/1/catalog1/schema1/str_crc/
行为 CALL Function(1:36~1:41) /test/1/catalog1/schema1/CRC32/
------
SQL  CREATE VIEW str_find_in_set_not_in_view AS SELECT FIND_IN_SET(1 NOT IN (0), set_value) AS found_value FROM str_set_values;
行为 CREATE View(1:12~1:39) /test/1/catalog1/schema1/str_find_in_set_not_in_view/ -> Table(1:107~1:121) /test/1/catalog1/schema1/str_set_values/
行为 CALL Function(1:50~1:61) /test/1/catalog1/schema1/FIND_IN_SET/
------
SQL  CREATE TABLE func_test_desc_index(c1 VARCHAR(10),c2 VARCHAR(10),c3 DATE NOT NULL,c4 INT,UNIQUE KEY ix(c1,c3 DESC,c4 DESC));
行为 CREATE Index(1:99~1:101) /test/1/catalog1/schema1/ix/ -> Table(1:13~1:33) /test/1/catalog1/schema1/func_test_desc_index/
行为 CREATE Constraint(1:88~1:121) /test/1/catalog1/schema1/
------
SQL  CREATE VIEW temporal_having_view AS SELECT (SELECT 'v' FROM DUAL) AS field1 FROM temporal_view_source GROUP BY field1 HAVING TIME(field1) != 0 AND TIMESTAMP(field1) != 0;
行为 CREATE View(1:12~1:32) /test/1/catalog1/schema1/temporal_having_view/ -> [Table(1:60~1:64) /test/1/catalog1/schema1/DUAL/ ; Table(1:81~1:101) /test/1/catalog1/schema1/temporal_view_source/]
行为 CALL Function(1:125~1:129) /test/1/catalog1/schema1/TIME/
行为 CALL Function(1:147~1:156) /test/1/catalog1/schema1/TIMESTAMP/
------
SQL  CREATE TABLE uuid_replace_ctas AS SELECT REPLACE(UUID(), '-', '=') AS v FROM mysql.user;
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/uuid_replace_ctas/ -> Table(1:77~1:87) /test/1/catalog1/mysql/user/
行为 CALL Function(1:41~1:48) /test/1/catalog1/schema1/REPLACE/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/UUID/
------
SQL  CREATE VIEW weight_char_view AS SELECT WEIGHT_STRING(value_text AS CHAR(2)) AS weight_value FROM weight_source;
行为 CREATE View(1:12~1:28) /test/1/catalog1/schema1/weight_char_view/ -> Table(1:97~1:110) /test/1/catalog1/schema1/weight_source/
行为 CALL Function(1:39~1:52) /test/1/catalog1/schema1/WEIGHT_STRING/
------
SQL  CREATE VIEW weight_binary_view AS SELECT WEIGHT_STRING(value_text AS BINARY(6)) AS weight_value FROM weight_source;
行为 CREATE View(1:12~1:30) /test/1/catalog1/schema1/weight_binary_view/ -> Table(1:101~1:114) /test/1/catalog1/schema1/weight_source/
行为 CALL Function(1:41~1:54) /test/1/catalog1/schema1/WEIGHT_STRING/
------
SQL  CREATE VIEW weight_level_view AS SELECT WEIGHT_STRING(value_text AS CHAR(2) LEVEL 1 DESC) AS weight_value FROM weight_source;
行为 CREATE View(1:12~1:29) /test/1/catalog1/schema1/weight_level_view/ -> Table(1:111~1:124) /test/1/catalog1/schema1/weight_source/
行为 CALL Function(1:40~1:53) /test/1/catalog1/schema1/WEIGHT_STRING/
------
SQL  CREATE VIEW weight_level_list_view AS SELECT WEIGHT_STRING(value_text AS CHAR(2) LEVEL 1,3 REVERSE) AS weight_value FROM weight_source;
行为 CREATE View(1:12~1:34) /test/1/catalog1/schema1/weight_level_list_view/ -> Table(1:121~1:134) /test/1/catalog1/schema1/weight_source/
行为 CALL Function(1:45~1:58) /test/1/catalog1/schema1/WEIGHT_STRING/
------
SQL  CREATE VIEW split_olap_native_b.v_roll AS SELECT a,LENGTH(a) AS a_length,COUNT(*) AS row_count FROM split_olap_native_b.nums GROUP BY a WITH ROLLUP;
行为 CREATE View(1:12~1:38) /test/1/catalog1/split_olap_native_b/v_roll/ -> Table(1:100~1:124) /test/1/catalog1/split_olap_native_b/nums/
行为 CALL Function(1:51~1:57) /test/1/catalog1/schema1/LENGTH/
行为 CALL Function(1:73~1:78) /test/1/catalog1/schema1/COUNT/
------
SQL  CREATE TABLE split_olap_tail.ctas_roll AS SELECT a,COUNT(*) AS row_count FROM split_olap_tail.t GROUP BY a WITH ROLLUP;
行为 CREATE Table(1:13~1:38) /test/1/catalog1/split_olap_tail/ctas_roll/ -> Table(1:78~1:95) /test/1/catalog1/split_olap_tail/t/
行为 CALL Function(1:51~1:56) /test/1/catalog1/schema1/COUNT/
------
SQL  CREATE TABLE t2 SELECT id2 FROM t1 ORDER BY id3;
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t2/ -> Table(1:32~1:34) /test/1/catalog1/schema1/t1/
------
SQL  CREATE ALGORITHM=MERGE VIEW split_scalar_corr_b.v_merge AS SELECT (SELECT MAX(q2.id) FROM split_scalar_corr_b.q AS q2 WHERE q2.v>d.v OR q2.v<>2) AS max_id FROM (SELECT * FROM split_scalar_corr_b.q) AS d;
行为 CREATE View(1:28~1:55) /test/1/catalog1/split_scalar_corr_b/v_merge/ -> Table(1:90~1:111) /test/1/catalog1/split_scalar_corr_b/q/
行为 CALL Function(1:74~1:77) /test/1/catalog1/schema1/MAX/
------
SQL  CREATE TABLE split_scalar80_c.ctas AS SELECT table2.col_int_key AS field1,(SELECT COUNT(col_int_key) FROM split_scalar80_c.t1) AS field2 FROM split_scalar80_c.t1 AS table1 JOIN split_scalar80_c.t1 AS table2 ON table2.col_int_key=table1.col_int_key;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/split_scalar80_c/ctas/ -> Table(1:106~1:125) /test/1/catalog1/split_scalar80_c/t1/
行为 CALL Function(1:82~1:87) /test/1/catalog1/schema1/COUNT/
------
SQL  CREATE TABLE split_scalar80_d.ctas AS SELECT COUNT(q1.id) AS total,(SELECT MIN(q4.s) FROM split_scalar80_d.q AS q4) AS min_s FROM split_scalar80_d.q AS q1 JOIN (split_scalar80_d.q AS q2 JOIN split_scalar80_d.q AS q3 ON TRUE) ON TRUE WHERE 1<>(SELECT COUNT(*) FROM split_scalar80_d.q AS q5);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/split_scalar80_d/ctas/ -> Table(1:90~1:108) /test/1/catalog1/split_scalar80_d/q/
行为 CALL Function(1:45~1:50) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:75~1:78) /test/1/catalog1/schema1/MIN/
------
SQL  CREATE VIEW split_scalar80_e.v_scalar AS SELECT q1.id,q1.v,(SELECT COUNT(*) FROM split_scalar80_e.r AS r1) AS total FROM split_scalar80_e.q AS q1;
行为 CREATE View(1:12~1:37) /test/1/catalog1/split_scalar80_e/v_scalar/ -> [Table(1:81~1:99) /test/1/catalog1/split_scalar80_e/r/ ; Table(1:121~1:139) /test/1/catalog1/split_scalar80_e/q/]
行为 CALL Function(1:67~1:72) /test/1/catalog1/schema1/COUNT/
------
SQL  CREATE VIEW split_scalar80_f.v_complex AS SELECT q1.v FROM split_scalar80_f.q AS q1 JOIN ((split_scalar80_f.q AS q2 JOIN split_scalar80_f.q AS q3 ON 1)) ON q2.v>=(SELECT MIN(q4.v) FROM split_scalar80_f.q AS q4,split_scalar80_f.q AS q5) WHERE EXISTS((SELECT q6.v FROM split_scalar80_f.q AS q6 JOIN split_scalar80_f.q AS q7 ON q7.v=q6.id));
行为 CREATE View(1:12~1:38) /test/1/catalog1/split_scalar80_f/v_complex/ -> Table(1:59~1:77) /test/1/catalog1/split_scalar80_f/q/
行为 CALL Function(1:170~1:173) /test/1/catalog1/schema1/MIN/
------
SQL  CREATE TABLE split_subquery_third.ct_corr SELECT a,b FROM split_subquery_third.src AS o WHERE b=(SELECT MIN(i.b) FROM split_subquery_third.src AS i WHERE i.a=o.a);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/split_subquery_third/ct_corr/ -> Table(1:58~1:82) /test/1/catalog1/split_subquery_third/src/
行为 CALL Function(1:104~1:107) /test/1/catalog1/schema1/MIN/
------
SQL  CREATE TABLE split_subquery_thirteenth.six_copy AS SELECT t1.vkey AS f1,t1.inokey AS f2,t2.vkey AS f3,t1.inokey AS f4,t2.inokey AS f5,t1.vnokey AS f6 FROM split_subquery_thirteenth.six_base AS t1 INNER JOIN split_subquery_thirteenth.six_base AS t2 ON t2.inokey=t1.ikey AND t2.ikey=t1.ikey WHERE t1.vkey=t2.vkey OR t1.pk=154 OR t1.pk<>201 AND (t1.vkey LIKE '%a%' OR t1.vkey LIKE '%b%');
行为 CREATE Table(1:13~1:47) /test/1/catalog1/split_subquery_thirteenth/six_copy/ -> Table(1:155~1:189) /test/1/catalog1/split_subquery_thirteenth/six_base/
------
SQL  CREATE TEMPORARY TABLE u_ctas SELECT a FROM r UNION SELECT a FROM s;
行为 CREATE Table(1:23~1:29) /test/1/catalog1/schema1/u_ctas/ -> [Table(1:44~1:45) /test/1/catalog1/schema1/r/ ; Table(1:66~1:67) /test/1/catalog1/schema1/s/]
------
SQL  CREATE TABLE ctas_scalar AS SELECT CONVERT((SELECT beer FROM tmp_beer) USING binary) AS beer;
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/ctas_scalar/ -> Table(1:61~1:69) /test/1/catalog1/schema1/tmp_beer/
行为 CALL Function(1:35~1:42) /test/1/catalog1/schema1/CONVERT/
------
SQL  CREATE TABLE bit_distinct AS SELECT SQL_SMALL_RESULT DISTINCT b FROM bit_t;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/bit_distinct/ -> Table(1:69~1:74) /test/1/catalog1/schema1/bit_t/
------
SQL  CREATE TEMPORARY TABLE bit_point_ctas(a BIT(60)) ENGINE=InnoDB SELECT a FROM point_t GROUP BY a HAVING a IS NULL ORDER BY a DESC;
行为 CREATE Table(1:23~1:37) /test/1/catalog1/schema1/bit_point_ctas/ -> Table(1:77~1:84) /test/1/catalog1/schema1/point_t/
------
SQL  CREATE TABLE bit_union AS SELECT b FROM bit_t UNION SELECT b FROM bit_t;
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/bit_union/ -> Table(1:40~1:45) /test/1/catalog1/schema1/bit_t/
------
SQL  CREATE VIEW bit_view AS SELECT b+0 AS n,HEX(b) AS h FROM bit_values WHERE b=TRUE UNION ALL SELECT 0,'00';
行为 CREATE View(1:12~1:20) /test/1/catalog1/schema1/bit_view/ -> Table(1:57~1:67) /test/1/catalog1/schema1/bit_values/
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/HEX/
------
SQL  CREATE TABLE temporal_ctas AS SELECT id,dt6,ts6,TIMESTAMP(dt6,TIME'00:00:01') AS shifted FROM datetime_widths;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/temporal_ctas/ -> Table(1:94~1:109) /test/1/catalog1/schema1/datetime_widths/
行为 CALL Function(1:48~1:57) /test/1/catalog1/schema1/TIMESTAMP/
------
SQL  CREATE VIEW temporal_view AS SELECT id,created_at,modified_at,TIMESTAMPDIFF(MICROSECOND,created_at,modified_at) AS elapsed_us FROM temporal_auto;
行为 CREATE View(1:12~1:25) /test/1/catalog1/schema1/temporal_view/ -> Table(1:131~1:144) /test/1/catalog1/schema1/temporal_auto/
行为 CALL Function(1:62~1:75) /test/1/catalog1/schema1/TIMESTAMPDIFF/
------
SQL  CREATE TABLE str_foos (\n  id BIGINT NOT NULL AUTO_INCREMENT,\n  parent_id VARBINARY(16) NOT NULL,\n  text VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  PRIMARY KEY(id),\n  KEY index_foos_on_parent_id(parent_id)\n);
行为 CREATE Index(6:6~6:29) /test/1/catalog1/schema1/index_foos_on_parent_id/ -> Table(1:13~1:21) /test/1/catalog1/schema1/str_foos/
行为 CREATE Constraint(5:2~5:17) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE str_bars (\n  id BINARY(16) NOT NULL,\n  parent_id VARBINARY(16) NOT NULL,\n  PRIMARY KEY(id),\n  KEY index_bars_on_parent_id(parent_id)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
行为 CREATE Index(5:6~5:29) /test/1/catalog1/schema1/index_bars_on_parent_id/ -> Table(1:13~1:21) /test/1/catalog1/schema1/str_bars/
行为 CREATE Constraint(4:2~4:17) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE lob_ctas AS\nSELECT tb,b,mb,lb,tt,t,mt,lt FROM lob_family WHERE t IS NOT NULL;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/lob_ctas/ -> Table(2:34~2:44) /test/1/catalog1/schema1/lob_family/
------
SQL  CREATE VIEW lob_view AS\nSELECT HEX(b) AS b_hex,t,CHAR_LENGTH(mt) AS mt_length FROM lob_family;
行为 CREATE View(1:12~1:20) /test/1/catalog1/schema1/lob_view/ -> Table(2:59~2:69) /test/1/catalog1/schema1/lob_family/
行为 CALL Function(2:7~2:10) /test/1/catalog1/schema1/HEX/
行为 CALL Function(2:25~2:36) /test/1/catalog1/schema1/CHAR_LENGTH/
------
SQL  CREATE VIEW split_type_enum_set.es_view AS\n    SELECT id,e_basic,e_basic+0 AS enum_index,s_basic,s_basic+0 AS set_mask\n    FROM split_type_enum_set.es_core;
行为 CREATE View(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_view/ -> Table(3:9~3:36) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  CREATE VIEW spatial_view AS\nSELECT id,ST_AsText(g) AS g_text,ST_SRID(g) AS g_srid\nFROM spatial_lifecycle\nWHERE MBRIntersects(g,ST_GeomFromText('POLYGON((0 0,0 10,10 10,10 0,0 0))'));
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/spatial_view/ -> Table(3:5~3:22) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(2:10~2:19) /test/1/catalog1/schema1/ST_AsText/
行为 CALL Function(2:33~2:40) /test/1/catalog1/schema1/ST_SRID/
行为 CALL Function(4:6~4:19) /test/1/catalog1/schema1/MBRIntersects/
行为 CALL Function(4:22~4:37) /test/1/catalog1/schema1/ST_GeomFromText/
------
SQL  CREATE TABLE spatial_ctas AS\nSELECT id,g,p,ST_Envelope(g) AS envelope_g\nFROM spatial_lifecycle;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/spatial_ctas/ -> Table(3:5~3:22) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(2:14~2:25) /test/1/catalog1/schema1/ST_Envelope/
------
SQL  CREATE VIEW integer_view AS\nSELECT id,tiny_signed,small_unsigned,medium_signed,int_unsigned,big_signed,\n       tiny_signed+small_unsigned+medium_signed+int_unsigned+big_signed AS integer_total\nFROM integer_lifecycle;
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/integer_view/ -> Table(4:5~4:22) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  CREATE TABLE integer_ctas AS\nSELECT id,tiny_signed,small_signed,medium_signed,int_signed,big_signed,\n       CAST(big_unsigned AS UNSIGNED) AS copied_unsigned\nFROM integer_lifecycle;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/integer_ctas/ -> Table(4:5~4:22) /test/1/catalog1/schema1/integer_lifecycle/
行为 CALL Function(3:7~3:11) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE VIEW numeric_view AS\nSELECT id,decimal_value,numeric_value,fixed_value,float_value,double_value,real_value,\n       decimal_value+numeric_value+fixed_value AS exact_total,\n       float_value+double_value+real_value AS approximate_total\nFROM numeric_lifecycle;
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/numeric_view/ -> Table(5:5~5:22) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  CREATE TABLE numeric_ctas AS\nSELECT id,decimal_value,numeric_value,fixed_value,float_value,double_value,real_value,\n       CAST(decimal_value AS DECIMAL(30,10)) AS widened_decimal\nFROM numeric_lifecycle;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/numeric_ctas/ -> Table(4:5~4:22) /test/1/catalog1/schema1/numeric_lifecycle/
行为 CALL Function(3:7~3:11) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE t4 SELECT t2.*,1,2 FROM t2;
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t4/ -> Table(1:37~1:39) /test/1/catalog1/schema1/t2/
------
SQL  CREATE TABLE t5 SELECT t2.*,d AS x,d AS z FROM t2;
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t5/ -> Table(1:47~1:49) /test/1/catalog1/schema1/t2/
------
SQL  CREATE TABLE conversion_strings_copy SELECT * FROM conversion_strings WHERE c3 = 0;
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/conversion_strings_copy/ -> Table(1:51~1:69) /test/1/catalog1/schema1/conversion_strings/
------
SQL  CREATE EVENT split_event.ev_compound\nON SCHEDULE EVERY (1 + 1) DAY\nSTARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\nENDS CURRENT_TIMESTAMP + INTERVAL 7 DAY\nON COMPLETION PRESERVE\nDISABLE\nCOMMENT 'compound event'\nDO event_block: BEGIN\n  DECLARE done BOOLEAN DEFAULT FALSE;\n  DECLARE v_id INT;\n  DECLARE cur CURSOR FOR SELECT id FROM split_event.event_log;\n  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;\n  OPEN cur;\n  read_loop: LOOP\n    FETCH cur INTO v_id;\n    IF done THEN\n      LEAVE read_loop;\n    END IF;\n    UPDATE split_event.event_log SET note = CONCAT('event-', v_id) WHERE id = v_id;\n  END LOOP read_loop;\n  CLOSE cur;\n  CASE\n    WHEN EXISTS (SELECT 1 FROM split_event.event_log WHERE id = 1) THEN\n      INSERT INTO split_event.event_log VALUES (2, 'created') ON DUPLICATE KEY UPDATE note = VALUES(note);\n    ELSE\n      SET @event_case = 0;\n  END CASE;\nEND event_block;
行为 UPDATE Table(19:11~19:32) /test/1/catalog1/split_event/event_log/ -> Table(11:40~11:61) /test/1/catalog1/split_event/event_log/
行为 CREATE Event(1:13~1:36) /test/1/catalog1/split_event/ev_compound/
行为 CALL Function(19:44~19:50) /test/1/catalog1/schema1/CONCAT/
行为 MERGE Table(24:18~24:39) /test/1/catalog1/split_event/event_log/
行为 CALL Function(24:93~24:99) /test/1/catalog1/schema1/VALUES/
行为 READ ConfigKey(26:10~26:21) /test/1/event_case/
------
SQL  CREATE TRIGGER tr_bi_check_uniqueness_with_nulls\nBEFORE INSERT ON t1 FOR EACH ROW\nBEGIN\n  IF EXISTS (SELECT * FROM t1 WHERE t1.id <=> NEW.id AND t1.ts <=> NEW.ts) THEN\n    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicated values not allowed.';\n  END IF;\nEND;
行为 CREATE Trigger(1:15~1:48) /test/1/catalog1/schema1/tr_bi_check_uniqueness_with_nulls/ -> Table(2:17~2:19) /test/1/catalog1/schema1/t1/
行为 READ Table(4:27~4:29) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE fd_ctas_declared (\n  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),\n  default_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),\n  changed_at DATETIME(6) ON UPDATE CURRENT_TIMESTAMP(6),\n  fixed_at TIMESTAMP(6) NOT NULL DEFAULT '2000-01-01 00:00:00.000001' ON UPDATE CURRENT_TIMESTAMP(6)\n) SELECT id FROM fd_ctas_source;
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/fd_ctas_declared/ -> Table(6:17~6:31) /test/1/catalog1/schema1/fd_ctas_source/
------
SQL  CREATE VIEW fd_collate_view (b) AS\nSELECT a COLLATE latin1_german1_ci FROM fd_view_target;
行为 CREATE View(1:12~1:27) /test/1/catalog1/schema1/fd_collate_view/ -> Table(2:40~2:54) /test/1/catalog1/schema1/fd_view_target/
------
SQL  CREATE PROCEDURE split_select_short.p1()\nBEGIN\n  DECLARE counter INTEGER DEFAULT 0;\n  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN SET counter=counter+1; END;\n  REPEAT\n    IF RAND()>0.5 THEN START TRANSACTION; END IF;\n    IF RAND()>0.5 THEN SELECT COUNT(*) FROM split_select_short.t FOR UPDATE; END IF;\n    UPDATE split_select_short.t SET a=1 WHERE a>=0;\n    SET counter=counter+1;\n  UNTIL counter>=2 END REPEAT;\nEND;
行为 UPDATE Table(8:11~8:31) /test/1/catalog1/split_select_short/t/ -> Table(7:44~7:64) /test/1/catalog1/split_select_short/t/
行为 CREATE Procedure(1:17~1:38) /test/1/catalog1/split_select_short/p1/
行为 CALL Function(6:7~6:11) /test/1/catalog1/schema1/RAND/
行为 CALL Function(7:30~7:35) /test/1/catalog1/schema1/COUNT/
------
SQL  CREATE TRIGGER codex_group_having_audit_trg BEFORE INSERT ON codex_group_having_audit_target FOR EACH ROW INSERT INTO codex_group_having_audit_dst(b) SELECT b FROM codex_group_having_audit_src GROUP BY a;
行为 CREATE Trigger(1:15~1:43) /test/1/catalog1/schema1/codex_group_having_audit_trg/ -> Table(1:61~1:92) /test/1/catalog1/schema1/codex_group_having_audit_target/
行为 INSERT Table(1:118~1:146) /test/1/catalog1/schema1/codex_group_having_audit_dst/ -> Table(1:164~1:192) /test/1/catalog1/schema1/codex_group_having_audit_src/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER split56.trg_bi BEFORE INSERT ON split56.trigger_src FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:38~1:52) /test/1/catalog1/split56/trg_bi/ -> Table(1:70~1:89) /test/1/catalog1/split56/trigger_src/
行为 CALL Function(1:124~1:141) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split56.trg_bu BEFORE UPDATE ON split56.trigger_src FOR EACH ROW SET NEW.note = COALESCE(NEW.note, 'before update');
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split56/trg_bu/ -> Table(1:47~1:66) /test/1/catalog1/split56/trigger_src/
行为 CALL Function(1:95~1:103) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER split56.trg_bd BEFORE DELETE ON split56.trigger_src FOR EACH ROW INSERT INTO split56.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'before delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split56/trg_bd/ -> Table(1:47~1:66) /test/1/catalog1/split56/trigger_src/
行为 INSERT Table(1:92~1:113) /test/1/catalog1/split56/trigger_audit/
------
SQL  CREATE TRIGGER split56.trg_ai AFTER INSERT ON split56.trigger_src FOR EACH ROW INSERT INTO split56.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (NEW.id, 'after insert', NULL, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split56/trg_ai/ -> Table(1:46~1:65) /test/1/catalog1/split56/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split56/trigger_audit/
------
SQL  CREATE TRIGGER split56.trg_au AFTER UPDATE ON split56.trigger_src FOR EACH ROW INSERT INTO split56.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after update', OLD.amount, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split56/trg_au/ -> Table(1:46~1:65) /test/1/catalog1/split56/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split56/trigger_audit/
------
SQL  CREATE TRIGGER split56.trg_ad AFTER DELETE ON split56.trigger_src FOR EACH ROW INSERT INTO split56.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split56/trg_ad/ -> Table(1:46~1:65) /test/1/catalog1/split56/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split56/trigger_audit/
------
SQL  CREATE TRIGGER trg06 BEFORE INSERT ON t1 FOR EACH ROW BEGIN DECLARE n INT DEFAULT ROW_COUNT(); GET DIAGNOSTICS @rows=ROW_COUNT; SET @before=n; END;
行为 CREATE Trigger(1:15~1:20) /test/1/catalog1/schema1/trg06/ -> Table(1:38~1:40) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:82~1:91) /test/1/catalog1/schema1/ROW_COUNT/
行为 READ ConfigKey(1:111~1:116) /test/1/rows/
行为 READ ConfigKey(1:132~1:139) /test/1/before/
------
SQL  CREATE TRIGGER trg07 BEFORE INSERT ON t2 FOR EACH ROW BEGIN DECLARE CONTINUE HANDLER FOR 1062 BEGIN END; INSERT INTO t3 VALUES(NEW.i); INSERT INTO t3 VALUES(NEW.i); END;
行为 CREATE Trigger(1:15~1:20) /test/1/catalog1/schema1/trg07/ -> Table(1:38~1:40) /test/1/catalog1/schema1/t2/
行为 INSERT Table(1:117~1:119) /test/1/catalog1/schema1/t3/
------
SQL  CREATE TRIGGER spatial_trigger_bi\nBEFORE INSERT ON spatial_trigger_target\nFOR EACH ROW\nBEGIN\n  SET NEW.g=COALESCE(NEW.g,ST_GeomFromText('POINT(0 0)'));\n  SET NEW.p=COALESCE(NEW.p,ST_PointFromText('POINT(0 0)'));\nEND;
行为 CREATE Trigger(1:15~1:33) /test/1/catalog1/schema1/spatial_trigger_bi/ -> Table(2:17~2:39) /test/1/catalog1/schema1/spatial_trigger_target/
行为 CALL Function(5:12~5:20) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(5:27~5:42) /test/1/catalog1/schema1/ST_GeomFromText/
行为 CALL Function(6:27~6:43) /test/1/catalog1/schema1/ST_PointFromText/
------
SQL  CREATE TRIGGER numeric_before_update\nBEFORE UPDATE ON numeric_trigger_target\nFOR EACH ROW\nBEGIN\n  SET NEW.decimal_value=ROUND(COALESCE(NEW.decimal_value,OLD.decimal_value),6);\n  SET NEW.float_value=COALESCE(NEW.float_value,OLD.float_value);\n  SET NEW.double_value=COALESCE(NEW.double_value,OLD.double_value);\nEND;
行为 CREATE Trigger(1:15~1:36) /test/1/catalog1/schema1/numeric_before_update/ -> Table(2:17~2:39) /test/1/catalog1/schema1/numeric_trigger_target/
行为 CALL Function(5:24~5:29) /test/1/catalog1/schema1/ROUND/
行为 CALL Function(5:30~5:38) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER t15_ad_cascade AFTER DELETE ON t15 FOR EACH ROW DELETE FROM t111 WHERE f1=OLD.f1;
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/schema1/t15_ad_cascade/ -> Table(1:46~1:49) /test/1/catalog1/schema1/t15/
行为 DELETE Table(1:75~1:79) /test/1/catalog1/schema1/t111/
------
SQL  CREATE TRIGGER trig2 BEFORE UPDATE ON employees FOR EACH ROW UPDATE employees SET employee_name=NULL;
行为 CREATE Trigger(1:15~1:20) /test/1/catalog1/schema1/trig2/ -> Table(1:38~1:47) /test/1/catalog1/schema1/employees/
行为 UPDATE Table(1:68~1:77) /test/1/catalog1/schema1/employees/
------
SQL  CREATE TRIGGER child_after_delete AFTER DELETE ON child FOR EACH ROW BEGIN SET @parent_count=get_parent_count(); DO insert_parent(); END;
行为 CREATE Trigger(1:15~1:33) /test/1/catalog1/schema1/child_after_delete/ -> Table(1:50~1:55) /test/1/catalog1/schema1/child/
行为 READ ConfigKey(1:79~1:92) /test/1/parent_count/
行为 CALL Function(1:93~1:109) /test/1/catalog1/schema1/get_parent_count/
行为 CALL Function(1:116~1:129) /test/1/catalog1/schema1/insert_parent/
------
SQL  CREATE PROCEDURE split_view_do_dml() BEGIN DO (SELECT @next := IFNULL(MAX(bug_table_seq),0)+1 FROM v1); INSERT INTO t1 VALUES(1); END;
行为 INSERT Table(1:116~1:118) /test/1/catalog1/schema1/t1/ -> Table(1:99~1:101) /test/1/catalog1/schema1/v1/
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/split_view_do_dml/
行为 READ ConfigKey(1:54~1:59) /test/1/next/
行为 CALL Function(1:63~1:69) /test/1/catalog1/schema1/IFNULL/
行为 CALL Function(1:70~1:73) /test/1/catalog1/schema1/MAX/
------
SQL  CREATE TRIGGER gap_trigger_call BEFORE INSERT ON gap_t FOR EACH ROW CALL gap_sink();
行为 CREATE Trigger(1:15~1:31) /test/1/catalog1/schema1/gap_trigger_call/ -> Table(1:49~1:54) /test/1/catalog1/schema1/gap_t/
行为 CALL Procedure(1:73~1:81) /test/1/catalog1/schema1/gap_sink/
------
SQL  CREATE TRIGGER gap_trigger_empty BEFORE INSERT ON gap_t FOR EACH ROW BEGIN END;
行为 CREATE Trigger(1:15~1:32) /test/1/catalog1/schema1/gap_trigger_empty/ -> Table(1:50~1:55) /test/1/catalog1/schema1/gap_t/
------
SQL  CREATE TRIGGER gap_trigger_prepare BEFORE INSERT ON gap_t FOR EACH ROW PREPARE s FROM 'SELECT 1';
行为 CREATE Trigger(1:15~1:34) /test/1/catalog1/schema1/gap_trigger_prepare/ -> Table(1:52~1:57) /test/1/catalog1/schema1/gap_t/
行为 ADMIN PrepareStatement(1:79~1:80) /test/1/s/
------
SQL  CREATE DEFINER=root@localhost TRIGGER trg_def_gap BEFORE UPDATE ON t1 FOR EACH ROW SET NEW.a=OLD.a;
行为 CREATE Trigger(1:38~1:49) /test/1/catalog1/schema1/trg_def_gap/ -> Table(1:67~1:69) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TRIGGER split_trigger_event BEFORE INSERT ON split_trigger_source FOR EACH ROW CREATE EVENT split_trigger_event_body ON SCHEDULE EVERY 1 SECOND DO SET @a=5;
行为 CREATE Trigger(1:15~1:34) /test/1/catalog1/schema1/split_trigger_event/ -> Table(1:52~1:72) /test/1/catalog1/schema1/split_trigger_source/
行为 CREATE Event(1:99~1:123) /test/1/catalog1/schema1/split_trigger_event_body/
行为 READ ConfigKey(1:158~1:160) /test/1/a/
------
SQL  CREATE TRIGGER tr_signal_new BEFORE INSERT ON t_signal FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT=NEW.msg;
行为 CREATE Trigger(1:15~1:28) /test/1/catalog1/schema1/tr_signal_new/ -> Table(1:46~1:54) /test/1/catalog1/schema1/t_signal/
------
SQL  CREATE TABLE t1(pk INT, KEY(pk)) ENGINE=MYISAM;
行为 CREATE Index(1:24~1:31) /test/1/catalog1/schema1/ -> Table(1:13~1:15) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE IF NOT EXISTS codex_create_audit_ifne_like LIKE codex_create_audit_src;
行为 CREATE Table(1:27~1:55) /test/1/catalog1/schema1/codex_create_audit_ifne_like/ -> Table(1:61~1:83) /test/1/catalog1/schema1/codex_create_audit_src/
------
SQL  ALTER TABLE child ADD COLUMN uncle_id INT, DROP COLUMN c, ADD CONSTRAINT FOREIGN KEY (uncle_id) REFERENCES uncle(id), ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:17) /test/1/catalog1/schema1/child/ -> Table(1:107~1:112) /test/1/catalog1/schema1/uncle/
行为 CREATE Constraint(1:58~1:116) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE t2 ADD FOREIGN KEY (fk) REFERENCES t1(pk), ENGINE=InnoDB, RENAME TO t3, ALGORITHM=COPY;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t2/ -> Table(1:47~1:49) /test/1/catalog1/schema1/t1/
行为 CREATE Constraint(1:15~1:53) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE codex_constraint_c04 (id INT PRIMARY KEY, parent_id INT, CONSTRAINT fk_c04 FOREIGN KEY (parent_id) REFERENCES codex_constraint_c04(id) ON DELETE SET DEFAULT);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c04/ -> Table(1:123~1:143) /test/1/catalog1/schema1/codex_constraint_c04/
行为 CREATE Constraint(1:42~1:53) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:81~1:87) /test/1/catalog1/schema1/fk_c04/
------
SQL  CREATE TABLE codex_constraint_c10 (parent_id INT REFERENCES codex_constraint_c10p);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c10/ -> Table(1:60~1:81) /test/1/catalog1/schema1/codex_constraint_c10p/
行为 CREATE Constraint(1:49~1:81) /test/1/catalog1/schema1/
------
SQL  CREATE TEMPORARY TABLE tv AS SELECT /*+ SET_VAR(default_tmp_storage_engine=InnoDB) */ id FROM t1;
行为 CREATE Table(1:23~1:25) /test/1/catalog1/schema1/tv/ -> Table(1:94~1:96) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:48~1:74) /test/1/default_tmp_storage_engine/
------
SQL  CREATE OR REPLACE VIEW vv AS SELECT /*+ SET_VAR(sql_select_limit=1) */ id FROM t1;
行为 REPLACE View(1:23~1:25) /test/1/catalog1/schema1/vv/ -> Table(1:79~1:81) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:48~1:64) /test/1/sql_select_limit/
------
SQL  RENAME TABLES codex_rename.r1 TO codex_rename.r1_new, codex_rename.r2 TO codex_rename.r2_new;
行为 RENAME Table(1:14~1:29) /test/1/catalog1/codex_rename/r1/ -> Table(1:33~1:52) /test/1/catalog1/codex_rename/r1_new/
行为 RENAME Table(1:54~1:69) /test/1/catalog1/codex_rename/r2/ -> Table(1:73~1:92) /test/1/catalog1/codex_rename/r2_new/
------
SQL  CREATE TABLE split_table.constraints_common (\n  id BIGINT SERIAL DEFAULT VALUE,\n  required_col INT NOT NULL DEFAULT 0 COMMENT 'required',\n  unique_col INT UNIQUE KEY,\n  text_col VARCHAR(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'x',\n  ts_col TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n  format_col INT COLUMN_FORMAT FIXED STORAGE DISK,\n  parent_inline INT REFERENCES split_table.parent(id) MATCH SIMPLE ON DELETE SET NULL ON UPDATE CASCADE,\n  parent_table INT,\n  CONSTRAINT pk_constraints PRIMARY KEY (id) USING BTREE COMMENT 'primary',\n  CONSTRAINT uk_constraints UNIQUE KEY uq_constraints (unique_col) USING BTREE COMMENT 'unique',\n  CONSTRAINT fk_constraints FOREIGN KEY fk_parent (parent_table) REFERENCES split_table.parent(id) MATCH FULL ON UPDATE NO ACTION ON DELETE RESTRICT,\n  CONSTRAINT chk_constraints CHECK (required_col >= 0),\n  KEY idx_text (text_col(12) DESC) COMMENT 'inline index'\n) ENGINE=InnoDB;
行为 CREATE Index(11:39~11:53) /test/1/catalog1/schema1/uq_constraints/ -> Table(1:13~1:43) /test/1/catalog1/split_table/constraints_common/
行为 CREATE Index(12:40~12:49) /test/1/catalog1/schema1/fk_parent/ -> Table(1:13~1:43) /test/1/catalog1/split_table/constraints_common/
行为 CREATE Index(14:6~14:14) /test/1/catalog1/schema1/idx_text/ -> Table(1:13~1:43) /test/1/catalog1/split_table/constraints_common/
行为 CREATE Table(1:13~1:43) /test/1/catalog1/split_table/constraints_common/ -> Table(8:31~8:49) /test/1/catalog1/split_table/parent/
行为 CREATE Constraint(4:17~4:27) /test/1/catalog1/schema1/
行为 CREATE Constraint(8:20~8:103) /test/1/catalog1/schema1/
行为 CREATE Constraint(10:13~10:27) /test/1/catalog1/schema1/pk_constraints/
行为 CREATE Constraint(11:13~11:27) /test/1/catalog1/schema1/uk_constraints/
行为 CREATE Constraint(12:13~12:27) /test/1/catalog1/schema1/fk_constraints/
行为 CREATE Constraint(13:13~13:28) /test/1/catalog1/schema1/chk_constraints/
------
SQL  CREATE TABLE split_table.match_partial(id INT, parent_id INT, FOREIGN KEY(parent_id) REFERENCES split_table.parent(id) MATCH PARTIAL ON DELETE CASCADE);
行为 CREATE Table(1:13~1:38) /test/1/catalog1/split_table/match_partial/ -> Table(1:96~1:114) /test/1/catalog1/split_table/parent/
行为 CREATE Constraint(1:62~1:150) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_table.like_a LIKE split_table.base;
行为 CREATE Table(1:13~1:31) /test/1/catalog1/split_table/like_a/ -> Table(1:37~1:53) /test/1/catalog1/split_table/base/
------
SQL  CREATE TABLE split_table.like_b (LIKE split_table.base);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/split_table/like_b/ -> Table(1:38~1:54) /test/1/catalog1/split_table/base/
------
SQL  CREATE TABLE split_table.as_select AS SELECT id, note FROM split_table.base;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/split_table/as_select/ -> Table(1:59~1:75) /test/1/catalog1/split_table/base/
------
SQL  CREATE TABLE split_table.as_no_keyword SELECT id, note FROM split_table.base;
行为 CREATE Table(1:13~1:38) /test/1/catalog1/split_table/as_no_keyword/ -> Table(1:60~1:76) /test/1/catalog1/split_table/base/
------
SQL  CREATE TABLE split_table.ignore_select (id INT PRIMARY KEY, note VARCHAR(40)) IGNORE AS SELECT id, note FROM split_table.base;
行为 CREATE Table(1:13~1:38) /test/1/catalog1/split_table/ignore_select/ -> Table(1:109~1:125) /test/1/catalog1/split_table/base/
行为 CREATE Constraint(1:47~1:58) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_table.replace_select (id INT PRIMARY KEY, note VARCHAR(40)) REPLACE SELECT id, note FROM split_table.base;
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_table/replace_select/ -> Table(1:108~1:124) /test/1/catalog1/split_table/base/
行为 CREATE Constraint(1:48~1:59) /test/1/catalog1/schema1/
------
SQL  RENAME TABLE split_table.like_a TO split_table.swap_tmp, split_table.like_b TO split_table.like_a, split_table.swap_tmp TO split_table.like_b;
行为 RENAME Table(1:13~1:31) /test/1/catalog1/split_table/like_a/ -> Table(1:35~1:55) /test/1/catalog1/split_table/swap_tmp/
行为 RENAME Table(1:57~1:75) /test/1/catalog1/split_table/like_b/ -> Table(1:79~1:97) /test/1/catalog1/split_table/like_a/
行为 RENAME Table(1:99~1:119) /test/1/catalog1/split_table/swap_tmp/ -> Table(1:123~1:141) /test/1/catalog1/split_table/like_b/
------
SQL  RENAME TABLE split_table.as_select TO split_table2.moved;
行为 RENAME Table(1:13~1:34) /test/1/catalog1/split_table/as_select/ -> Table(1:38~1:56) /test/1/catalog1/split_table2/moved/
------
SQL  CREATE PROCEDURE split_table_derived.del_rows() DELETE o.* FROM split_table_derived.t1 AS o,(SELECT 1) AS d WHERE o.b<0 OR o.a IN(SELECT i.a+1 FROM split_table_derived.t2 AS i);
行为 DELETE Table(1:55~1:56) /test/1/catalog1/schema1/o/ -> [Table(1:64~1:86) /test/1/catalog1/split_table_derived/t1/ ; Table(1:148~1:170) /test/1/catalog1/split_table_derived/t2/]
行为 CREATE Procedure(1:17~1:45) /test/1/catalog1/split_table_derived/del_rows/
------
SQL  CREATE TABLE split_table_derived.ctas AS SELECT 1 AS c1 FROM split_table_derived.t1 AS o WHERE o.b IN(SELECT 1 FROM split_table_derived.t1 AS i WHERE ('f','f') IN(SELECT 1,COUNT(1) FROM split_table_derived.t1));
行为 CREATE Table(1:13~1:37) /test/1/catalog1/split_table_derived/ctas/ -> Table(1:61~1:83) /test/1/catalog1/split_table_derived/t1/
行为 CALL Function(1:172~1:177) /test/1/catalog1/schema1/COUNT/
------
SQL  CREATE TABLE split_fk.update_null(id INT PRIMARY KEY,parent_id INT,CONSTRAINT fk_update_parent FOREIGN KEY(parent_id) REFERENCES parent_t(id) ON UPDATE SET NULL) ENGINE=InnoDB;
行为 CREATE Table(1:13~1:33) /test/1/catalog1/split_fk/update_null/ -> Table(1:129~1:137) /test/1/catalog1/schema1/parent_t/
行为 CREATE Constraint(1:41~1:52) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:78~1:94) /test/1/catalog1/schema1/fk_update_parent/
------
SQL  CREATE TABLE split_fk.delete_no_action(id INT PRIMARY KEY,parent_id INT,CONSTRAINT fk_delete_parent FOREIGN KEY(parent_id) REFERENCES parent_t(id) ON DELETE NO ACTION) ENGINE=InnoDB;
行为 CREATE Table(1:13~1:38) /test/1/catalog1/split_fk/delete_no_action/ -> Table(1:134~1:142) /test/1/catalog1/schema1/parent_t/
行为 CREATE Constraint(1:46~1:57) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:83~1:99) /test/1/catalog1/schema1/fk_delete_parent/
------
SQL  CREATE TEMPORARY TABLE IF NOT EXISTS tmp_t_like LIKE base_t;
行为 CREATE Table(1:37~1:47) /test/1/catalog1/schema1/tmp_t_like/ -> Table(1:53~1:59) /test/1/catalog1/schema1/base_t/
------
SQL  CREATE TABLE fk_two(c1 INT,c2 INT,FOREIGN KEY(c1) REFERENCES fk_parent1(a),FOREIGN KEY(c2) REFERENCES fk_parent2(b));
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/fk_two/ -> [Table(1:61~1:71) /test/1/catalog1/schema1/fk_parent1/ ; Table(1:102~1:112) /test/1/catalog1/schema1/fk_parent2/]
行为 CREATE Constraint(1:34~1:74) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:75~1:115) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE fk_child_composite(a INT,d INT,b INT,c INT,FOREIGN KEY(a,d) REFERENCES fk_parent_composite(a,d) ON DELETE CASCADE ON UPDATE CASCADE,CONSTRAINT fk_bc FOREIGN KEY(b,c) REFERENCES fk_parent_composite(b,c) ON DELETE CASCADE ON UPDATE CASCADE);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/fk_child_composite/ -> Table(1:84~1:103) /test/1/catalog1/schema1/fk_parent_composite/
行为 CREATE Constraint(1:56~1:144) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:156~1:161) /test/1/catalog1/schema1/fk_bc/
------
SQL  ALTER TABLE split_table.alter_common ADD CONSTRAINT fk_new FOREIGN KEY (parent_id) REFERENCES split_table.parent(id) ON DELETE CASCADE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/ -> Table(1:94~1:112) /test/1/catalog1/split_table/parent/
行为 CREATE Constraint(1:52~1:58) /test/1/catalog1/schema1/fk_new/
------
SQL  ALTER TABLE fk_self ADD CONSTRAINT fk_self_ab FOREIGN KEY(a,b) REFERENCES fk_self(a,b) ON DELETE NO ACTION ON UPDATE NO ACTION;
行为 ALTER Table(1:12~1:19) /test/1/catalog1/schema1/fk_self/ -> Table(1:74~1:81) /test/1/catalog1/schema1/fk_self/
行为 CREATE Constraint(1:35~1:45) /test/1/catalog1/schema1/fk_self_ab/
------
SQL  ALTER TABLE fk_named ADD CONSTRAINT fk_p1 FOREIGN KEY ref1_idx(ref1) REFERENCES fk_named(id1),ALGORITHM=COPY;
行为 CREATE Index(1:54~1:62) /test/1/catalog1/schema1/ref1_idx/ -> Table(1:12~1:20) /test/1/catalog1/schema1/fk_named/
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/fk_named/ -> Table(1:80~1:88) /test/1/catalog1/schema1/fk_named/
行为 CREATE Constraint(1:36~1:41) /test/1/catalog1/schema1/fk_p1/
------
SQL  CREATE VIEW split_subquery_bugs_1800.v1 AS SELECT vt3.c2 AS vc1,vt3.c3 AS vc2,vt4.c1 AS vc3 FROM (((split_subquery_bugs_1800.vt3 LEFT JOIN split_subquery_bugs_1800.vt1 ON vt1.c1=vt3.c5) LEFT JOIN split_subquery_bugs_1800.vt2 ON vt3.c4=vt2.c1) JOIN split_subquery_bugs_1800.vt4);
行为 CREATE View(1:12~1:39) /test/1/catalog1/split_subquery_bugs_1800/v1/ -> [Table(1:100~1:128) /test/1/catalog1/split_subquery_bugs_1800/vt3/ ; Table(1:139~1:167) /test/1/catalog1/split_subquery_bugs_1800/vt1/ ; Table(1:196~1:224) /test/1/catalog1/split_subquery_bugs_1800/vt2/ ; Table(1:248~1:276) /test/1/catalog1/split_subquery_bugs_1800/vt4/]
------
SQL  CREATE VIEW split_subquery_bugs_550.v1 AS SELECT * FROM split_subquery_bugs_550.vb WHERE 5 IN (SELECT 1) IS UNKNOWN;
行为 CREATE View(1:12~1:38) /test/1/catalog1/split_subquery_bugs_550/v1/ -> Table(1:56~1:82) /test/1/catalog1/split_subquery_bugs_550/vb/
------
SQL  CREATE VIEW split_subquery_twelfth.v_nested AS SELECT alias2.col_varchar_nokey FROM split_subquery_twelfth.v_base AS alias1 RIGHT JOIN split_subquery_twelfth.view_base AS alias2 ON 1 WHERE alias2.col_varchar_key IN (SELECT sq1.col_varchar_nokey FROM split_subquery_twelfth.v_base AS sq1 LEFT JOIN split_subquery_twelfth.view_base AS sq2 ON sq2.col_int_key=sq1.pk WHERE sq1.pk<>alias1.col_int_key AND sq1.col_varchar_key>alias1.col_varchar_key);
行为 CREATE View(1:12~1:43) /test/1/catalog1/split_subquery_twelfth/v_nested/ -> [Table(1:84~1:113) /test/1/catalog1/split_subquery_twelfth/v_base/ ; Table(1:135~1:167) /test/1/catalog1/split_subquery_twelfth/view_base/]
------
SQL  CREATE VIEW split_subquery_tenth.v_check AS SELECT r.c AS c FROM split_subquery_tenth.view_left AS l,split_subquery_tenth.view_right AS r WHERE l.id=r.id AND 1 IN (SELECT i.id FROM split_subquery_tenth.view_left AS i) WITH CHECK OPTION;
行为 CREATE View(1:12~1:40) /test/1/catalog1/split_subquery_tenth/v_check/ -> [Table(1:65~1:95) /test/1/catalog1/split_subquery_tenth/view_left/ ; Table(1:101~1:132) /test/1/catalog1/split_subquery_tenth/view_right/]
------
SQL  CREATE OR REPLACE ALGORITHM = MERGE DEFINER = CURRENT_USER SQL SECURITY DEFINER VIEW split_view56.v_base (id, amount) AS\nSELECT id, amount FROM split_view56.src WHERE amount >= 0 WITH CASCADED CHECK OPTION;
行为 REPLACE View(1:85~1:104) /test/1/catalog1/split_view56/v_base/ -> Table(2:23~2:39) /test/1/catalog1/split_view56/src/
------
SQL  ALTER ALGORITHM = UNDEFINED DEFINER = CURRENT_USER SQL SECURITY INVOKER VIEW split_view56.v_base (id, amount) AS\nSELECT id, amount FROM split_view56.src WHERE amount BETWEEN 0 AND 100 WITH LOCAL CHECK OPTION;
行为 ALTER View(1:77~1:96) /test/1/catalog1/split_view56/v_base/ -> Table(2:23~2:39) /test/1/catalog1/split_view56/src/
------
SQL  CREATE ALGORITHM = TEMPTABLE VIEW split_view56.v_joined AS\nSELECT s.id, a.action_name FROM split_view56.src AS s LEFT JOIN split_view56.audit AS a ON a.src_id = s.id;
行为 CREATE View(1:34~1:55) /test/1/catalog1/split_view56/v_joined/ -> [Table(2:32~2:48) /test/1/catalog1/split_view56/src/ ; Table(2:64~2:82) /test/1/catalog1/split_view56/audit/]
------
SQL  ALTER DEFINER=no_such@user_1 VIEW v1 AS SELECT * FROM t1;
行为 ALTER View(1:34~1:36) /test/1/catalog1/schema1/v1/ -> Table(1:54~1:56) /test/1/catalog1/schema1/t1/
------
SQL  CREATE VIEW v_distinct_check AS\nSELECT DISTINCT a FROM t1 WITH CHECK OPTION;
行为 CREATE View(1:12~1:28) /test/1/catalog1/schema1/v_distinct_check/ -> Table(2:23~2:25) /test/1/catalog1/schema1/t1/
------
SQL  ALTER ALGORITHM=MERGE VIEW v_base_gap AS SELECT a FROM t1 WHERE a>=0 WITH CASCADED CHECK OPTION;
行为 ALTER View(1:27~1:37) /test/1/catalog1/schema1/v_base_gap/ -> Table(1:55~1:57) /test/1/catalog1/schema1/t1/
------
SQL  CREATE DEFINER=root@localhost SQL SECURITY INVOKER VIEW v_def_gap AS SELECT a FROM t1;
行为 CREATE View(1:56~1:65) /test/1/catalog1/schema1/v_def_gap/ -> Table(1:83~1:85) /test/1/catalog1/schema1/t1/
------
SQL  CREATE VIEW split_view_order_limit AS SELECT a + 1 FROM split_view_source ORDER BY 1 DESC LIMIT 2;
行为 CREATE View(1:12~1:34) /test/1/catalog1/schema1/split_view_order_limit/ -> Table(1:56~1:73) /test/1/catalog1/schema1/split_view_source/
------
SQL  CREATE VIEW split_view_system_variable (c,d) AS SELECT a, b + @@global.max_user_connections FROM split_view_source;
行为 CREATE View(1:12~1:38) /test/1/catalog1/schema1/split_view_system_variable/ -> Table(1:97~1:114) /test/1/catalog1/schema1/split_view_source/
行为 READ ConfigKey(1:62~1:91) /test/1/max_user_connections/
------
SQL  delete from test1 where id = (select max(id) from test2);
行为 DELETE Table(1:12~1:17) /test/1/catalog1/schema1/test1/ -> Table(1:50~1:55) /test/1/catalog1/schema1/test2/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/max/
------
SQL  delete from test1 where id = (select max(id) from test2 limit 2) limit 1;
行为 DELETE Table(1:12~1:17) /test/1/catalog1/schema1/test1/ -> Table(1:50~1:55) /test/1/catalog1/schema1/test2/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/max/
------
SQL  DELETE LOW_PRIORITY QUICK IGNORE a.*, b.* FROM t1 AS a INNER JOIN t2 AS b ON a.id = b.id WHERE a.id < 0;
行为 DELETE Table(1:33~1:34) /test/1/catalog1/schema1/a/ -> [Table(1:47~1:49) /test/1/catalog1/schema1/t1/ ; Table(1:66~1:68) /test/1/catalog1/schema1/t2/]
行为 DELETE Table(1:38~1:39) /test/1/catalog1/schema1/b/
------
SQL  DELETE LOW_PRIORITY QUICK IGNORE FROM a, b USING t1 AS a INNER JOIN t2 AS b ON a.id = b.id WHERE a.id < 0;
行为 DELETE Table(1:38~1:39) /test/1/catalog1/schema1/a/ -> [Table(1:49~1:51) /test/1/catalog1/schema1/t1/ ; Table(1:68~1:70) /test/1/catalog1/schema1/t2/]
行为 DELETE Table(1:41~1:42) /test/1/catalog1/schema1/b/
------
SQL  DELETE a FROM ptab PARTITION (p0) AS a INNER JOIN t2 AS b ON a.id = b.id WHERE a.id < 0;
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/a/ -> [Table(1:14~1:18) /test/1/catalog1/schema1/ptab/ ; Table(1:50~1:52) /test/1/catalog1/schema1/t2/]
------
SQL  DELETE a FROM articles AS a JOIN aux AS b ON a.id=b.id WHERE MATCH(a.title) AGAINST('database');
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/a/ -> [Table(1:14~1:22) /test/1/catalog1/schema1/articles/ ; Table(1:33~1:36) /test/1/catalog1/schema1/aux/]
------
SQL  DELETE `4.t1` FROM t1 AS `4.t1` WHERE `4.t1`.a = 500;
行为 DELETE Table(1:7~1:13) /test/1/catalog1/schema1/4.t1/ -> Table(1:19~1:21) /test/1/catalog1/schema1/t1/
------
SQL  DELETE FROM `4.t1` USING t1 AS `4.t1` WHERE `4.t1`.a = 500;
行为 DELETE Table(1:12~1:18) /test/1/catalog1/schema1/4.t1/ -> Table(1:25~1:27) /test/1/catalog1/schema1/t1/
------
SQL  DELETE FROM t1.*, codex_delete_native_parallel.t2.*, a.* USING t1, t2, t3 AS a;
行为 DELETE Table(1:12~1:14) /test/1/catalog1/schema1/t1/ -> [Table(1:63~1:65) /test/1/catalog1/schema1/t1/ ; Table(1:67~1:69) /test/1/catalog1/schema1/t2/ ; Table(1:71~1:73) /test/1/catalog1/schema1/t3/]
行为 DELETE Table(1:18~1:49) /test/1/catalog1/codex_delete_native_parallel/t2/
行为 DELETE Table(1:53~1:54) /test/1/catalog1/schema1/a/
------
SQL  DELETE FROM OUTR1.* USING t1 AS OUTR1 LEFT OUTER JOIN t2 AS OUTR2 ON (OUTR1.a = OUTR2.b) WHERE OUTR1.a < (SELECT t3.c FROM t3 WHERE 1 XOR OUTR2.b IS NOT NULL);
行为 DELETE Table(1:12~1:17) /test/1/catalog1/schema1/OUTR1/ -> [Table(1:26~1:28) /test/1/catalog1/schema1/t1/ ; Table(1:54~1:56) /test/1/catalog1/schema1/t2/ ; Table(1:123~1:125) /test/1/catalog1/schema1/t3/]
------
SQL  DELETE IGNORE t1.*, t2.* FROM t1, t2 WHERE c < b OR a != (SELECT 1 UNION SELECT 2);
行为 DELETE Table(1:14~1:16) /test/1/catalog1/schema1/t1/ -> [Table(1:30~1:32) /test/1/catalog1/schema1/t1/ ; Table(1:34~1:36) /test/1/catalog1/schema1/t2/]
行为 DELETE Table(1:20~1:22) /test/1/catalog1/schema1/t2/
------
SQL  DELETE t1, t2, t3, t4 FROM (t1 INNER JOIN t2 USING (a)) LEFT JOIN (t3 INNER JOIN t4 USING (a)) ON t2.b = t4.b;
行为 DELETE Table(1:7~1:9) /test/1/catalog1/schema1/t1/ -> [Table(1:28~1:30) /test/1/catalog1/schema1/t1/ ; Table(1:42~1:44) /test/1/catalog1/schema1/t2/ ; Table(1:67~1:69) /test/1/catalog1/schema1/t3/ ; Table(1:81~1:83) /test/1/catalog1/schema1/t4/]
行为 DELETE Table(1:11~1:13) /test/1/catalog1/schema1/t2/
行为 DELETE Table(1:15~1:17) /test/1/catalog1/schema1/t3/
行为 DELETE Table(1:19~1:21) /test/1/catalog1/schema1/t4/
------
SQL  DELETE p1.* FROM split_derived_common.nm AS p1 INNER JOIN (SELECT n FROM split_derived_common.nm GROUP BY n HAVING COUNT(m)>1) AS p2 ON p1.n=p2.n;
行为 DELETE Table(1:7~1:9) /test/1/catalog1/schema1/p1/ -> Table(1:17~1:40) /test/1/catalog1/split_derived_common/nm/
行为 CALL Function(1:115~1:120) /test/1/catalog1/schema1/COUNT/
------
SQL  DELETE FROM split_subquery_fifth.del1 WHERE del1.EMPNUM NOT IN (SELECT del2.EMPNUM FROM split_subquery_fifth.del2 WHERE del1.EMPNUM=del2.EMPNUM);
行为 DELETE Table(1:12~1:37) /test/1/catalog1/split_subquery_fifth/del1/ -> Table(1:88~1:113) /test/1/catalog1/split_subquery_fifth/del2/
------
SQL  DELETE d7_t1, d7_t2 FROM d7_t1 PARTITION (pNeg), d7_t3, d7_t2 PARTITION (subp3) WHERE d7_t1.a = d7_t3.a AND d7_t3.b = 'subp3' AND d7_t3.a = d7_t2.a;
行为 DELETE Table(1:7~1:12) /test/1/catalog1/schema1/d7_t1/ -> [Table(1:25~1:30) /test/1/catalog1/schema1/d7_t1/ ; Table(1:49~1:54) /test/1/catalog1/schema1/d7_t3/ ; Table(1:56~1:61) /test/1/catalog1/schema1/d7_t2/]
行为 DELETE Table(1:14~1:19) /test/1/catalog1/schema1/d7_t2/
------
SQL  DELETE FROM d8_t2, d8_t3 USING d8_t2 PARTITION (`p0-9`), d8_t3, d8_t1 PARTITION (subp3) WHERE d8_t1.a = d8_t3.a AND d8_t3.b = 'subp3' AND d8_t2.a = d8_t1.a;
行为 DELETE Table(1:12~1:17) /test/1/catalog1/schema1/d8_t2/ -> [Table(1:31~1:36) /test/1/catalog1/schema1/d8_t2/ ; Table(1:57~1:62) /test/1/catalog1/schema1/d8_t3/ ; Table(1:64~1:69) /test/1/catalog1/schema1/d8_t1/]
行为 DELETE Table(1:19~1:24) /test/1/catalog1/schema1/d8_t3/
------
SQL  DELETE d9_t1.* FROM d9_t1 FORCE INDEX(by_val) WHERE val > 1 AND val MOD 1000 = 1;
行为 DELETE Table(1:7~1:12) /test/1/catalog1/schema1/d9_t1/ -> Table(1:20~1:25) /test/1/catalog1/schema1/d9_t1/
------
SQL  DELETE s FROM split_dml_src AS s IGNORE INDEX FOR JOIN (idx_v)\n  JOIN split_dml_aux AS a USE INDEX FOR JOIN (PRIMARY) ON a.id=s.id\n  WHERE EXISTS (SELECT 1 FROM split_dml_dst AS d WHERE d.id=a.id);
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/s/ -> [Table(1:14~1:27) /test/1/catalog1/schema1/split_dml_src/ ; Table(2:7~2:20) /test/1/catalog1/schema1/split_dml_aux/ ; Table(3:30~3:43) /test/1/catalog1/schema1/split_dml_dst/]
------
SQL  DELETE FROM split_type_enum_set.es_core\n    WHERE e_basic IN (\n      SELECT e_basic\n      FROM (SELECT e_basic FROM split_type_enum_set.es_core\n            WHERE FIND_IN_SET('green',s_basic)) AS d\n    )\n      AND (s_basic+0 & 2) <> 0;
行为 DELETE Table(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_core/ -> Table(4:32~4:59) /test/1/catalog1/split_type_enum_set/es_core/
行为 CALL Function(5:18~5:29) /test/1/catalog1/schema1/FIND_IN_SET/
------
SQL  DELETE FROM spatial_lifecycle\nWHERE EXISTS (\n  SELECT 1\n  FROM spatial_lifecycle AS s2\n  WHERE s2.id=spatial_lifecycle.id\n    AND ST_Within(s2.p,ST_Buffer(spatial_lifecycle.p,1))\n);
行为 DELETE Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/ -> Table(4:7~4:24) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(6:8~6:17) /test/1/catalog1/schema1/ST_Within/
行为 CALL Function(6:23~6:32) /test/1/catalog1/schema1/ST_Buffer/
------
SQL  DELETE FROM integer_lifecycle\nWHERE int_signed<0\n  AND EXISTS (\n    SELECT 1\n    FROM integer_lifecycle AS inner_integer\n    WHERE inner_integer.id=integer_lifecycle.id\n      AND inner_integer.big_unsigned=integer_lifecycle.big_unsigned\n  );
行为 DELETE Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/ -> Table(5:9~5:26) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  DELETE FROM numeric_lifecycle\nWHERE float_value<0\n   OR EXISTS (\n     SELECT 1\n     FROM numeric_lifecycle AS inner_numeric\n     WHERE inner_numeric.id=numeric_lifecycle.id\n       AND inner_numeric.decimal_value=numeric_lifecycle.numeric_value\n   );
行为 DELETE Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/ -> Table(5:10~5:27) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  INSERT INTO analyse_int SELECT * FROM analyse_int PROCEDURE ANALYSE();
行为 INSERT Table(1:12~1:23) /test/1/catalog1/schema1/analyse_int/ -> Table(1:38~1:49) /test/1/catalog1/schema1/analyse_int/
------
SQL  INSERT INTO agg_out SELECT grp,MAX(a)+MAX(grp),MAX(c) FROM fg GROUP BY grp;
行为 INSERT Table(1:12~1:19) /test/1/catalog1/schema1/agg_out/ -> Table(1:59~1:61) /test/1/catalog1/schema1/fg/
行为 CALL Function(1:31~1:34) /test/1/catalog1/schema1/MAX/
------
SQL  INSERT INTO codex_func_rand.t SELECT MAX(i1),FLOOR(RAND(0)*1000) FROM codex_func_rand.t;
行为 INSERT Table(1:12~1:29) /test/1/catalog1/codex_func_rand/t/ -> Table(1:70~1:87) /test/1/catalog1/codex_func_rand/t/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:45~1:50) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:51~1:55) /test/1/catalog1/schema1/RAND/
------
SQL  INSERT INTO codex_func_rand.t SELECT MAX(i1),FLOOR(RAND(0)*1000)+EXTRACT(YEAR FROM NOW()) DIV 1000 FROM codex_func_rand.t;
行为 INSERT Table(1:12~1:29) /test/1/catalog1/codex_func_rand/t/ -> Table(1:104~1:121) /test/1/catalog1/codex_func_rand/t/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:45~1:50) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:51~1:55) /test/1/catalog1/schema1/RAND/
行为 CALL Function(1:65~1:72) /test/1/catalog1/schema1/EXTRACT/
行为 CALL Function(1:83~1:86) /test/1/catalog1/schema1/NOW/
------
SQL  INSERT INTO codex_func_rand.t SELECT MAX(i1),FLOOR(RAND(0)*1000)+EXTRACT(YEAR FROM NOW()) DIV 1000 FROM codex_func_rand.t GROUP BY i2;
行为 INSERT Table(1:12~1:29) /test/1/catalog1/codex_func_rand/t/ -> Table(1:104~1:121) /test/1/catalog1/codex_func_rand/t/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:45~1:50) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:51~1:55) /test/1/catalog1/schema1/RAND/
行为 CALL Function(1:65~1:72) /test/1/catalog1/schema1/EXTRACT/
行为 CALL Function(1:83~1:86) /test/1/catalog1/schema1/NOW/
------
SQL  INSERT INTO base64_encoded(encoded_value) SELECT TO_BASE64(binary_value) FROM base64_binary_values;
行为 INSERT Table(1:12~1:26) /test/1/catalog1/schema1/base64_encoded/ -> Table(1:78~1:98) /test/1/catalog1/schema1/base64_binary_values/
行为 CALL Function(1:49~1:58) /test/1/catalog1/schema1/TO_BASE64/
------
SQL  INSERT INTO t_dst SELECT IF(a, 1, 0) FROM t_src;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/t_dst/ -> Table(1:42~1:47) /test/1/catalog1/schema1/t_src/
行为 CALL Function(1:25~1:27) /test/1/catalog1/schema1/IF/
------
SQL  INSERT INTO t_dst SELECT IF(a <> 0, 1, 0) FROM t_src;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/t_dst/ -> Table(1:47~1:52) /test/1/catalog1/schema1/t_src/
行为 CALL Function(1:25~1:27) /test/1/catalog1/schema1/IF/
------
SQL  INSERT INTO t_dst SELECT a | 2147483647 FROM t_src;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/t_dst/ -> Table(1:45~1:50) /test/1/catalog1/schema1/t_src/
------
SQL  INSERT INTO t_dst SELECT CAST(a AS DECIMAL) FROM t_src;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/t_dst/ -> Table(1:49~1:54) /test/1/catalog1/schema1/t_src/
行为 CALL Function(1:25~1:29) /test/1/catalog1/schema1/CAST/
------
SQL  INSERT INTO insert_time_source SELECT MAX(1),NOW() FROM insert_time_source;
行为 INSERT Table(1:12~1:30) /test/1/catalog1/schema1/insert_time_source/ -> Table(1:56~1:74) /test/1/catalog1/schema1/insert_time_source/
行为 CALL Function(1:38~1:41) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/NOW/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/function_defaults_fixed.dat'\nINTO TABLE fd_load_target\nFIELDS TERMINATED BY '' ENCLOSED BY '';
行为 IMPORT Table(2:11~2:25) /test/1/catalog1/schema1/fd_load_target/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/function_defaults_fixed.dat/
------
SQL  INSERT INTO split_scalar80_c.pct SELECT s2.avg_us,IFNULL(SUM(s1.cnt)/NULLIF((SELECT COUNT(*) FROM split_scalar80_c.digests),0),0) FROM split_scalar80_c.lat1 AS s1 JOIN split_scalar80_c.lat2 AS s2 ON s1.avg_us<=s2.avg_us GROUP BY s2.avg_us HAVING IFNULL(SUM(s1.cnt)/NULLIF((SELECT COUNT(*) FROM split_scalar80_c.digests),0),0)>0.95 ORDER BY 2 LIMIT 1;
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_scalar80_c/pct/ -> [Table(1:98~1:122) /test/1/catalog1/split_scalar80_c/digests/ ; Table(1:135~1:156) /test/1/catalog1/split_scalar80_c/lat1/ ; Table(1:168~1:189) /test/1/catalog1/split_scalar80_c/lat2/]
行为 CALL Function(1:50~1:56) /test/1/catalog1/schema1/IFNULL/
行为 CALL Function(1:57~1:60) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:69~1:75) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:84~1:89) /test/1/catalog1/schema1/COUNT/
------
SQL  INSERT INTO split_scalar80_d.dst SELECT COUNT(q1.id),(SELECT MIN(q4.s) FROM split_scalar80_d.q AS q4) FROM split_scalar80_d.q AS q1 JOIN (split_scalar80_d.q AS q2 JOIN split_scalar80_d.q AS q3 ON TRUE) ON TRUE WHERE 1<>(SELECT COUNT(*) FROM split_scalar80_d.q AS q5);
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_scalar80_d/dst/ -> Table(1:76~1:94) /test/1/catalog1/split_scalar80_d/q/
行为 CALL Function(1:40~1:45) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:61~1:64) /test/1/catalog1/schema1/MIN/
------
SQL  INSERT INTO split_management_function_probe(id,status_code) SELECT id,set_firewall_mode('fwuser@localhost','RECORDING') FROM split_management_function_source WHERE mysql_firewall_flush_status()=0;
行为 INSERT Table(1:12~1:43) /test/1/catalog1/schema1/split_management_function_probe/ -> Table(1:125~1:157) /test/1/catalog1/schema1/split_management_function_source/
行为 CONFIGURE Function(1:70~1:87) /test/1/catalog1/schema1/set_firewall_mode/
行为 CALL Function(1:164~1:191) /test/1/catalog1/schema1/mysql_firewall_flush_status/
------
SQL  insert `test_schema`.`table2` (`id`, `b`) values ((select id from table1 limit 1), 1);
行为 INSERT Table(1:7~1:29) /test/1/catalog1/test_schema/table2/ -> Table(1:66~1:72) /test/1/catalog1/schema1/table1/
------
SQL  insert into table2 (customer_id, order_date, total_amount)\nselect customer_id, order_date, total_amount from orders1\nunion\nselect customer_id, order_date, total_amount from orders2;
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/table2/ -> [Table(2:50~2:57) /test/1/catalog1/schema1/orders1/ ; Table(4:50~4:57) /test/1/catalog1/schema1/orders2/]
------
SQL  INSERT HIGH_PRIORITY INTO myisam_t (id, name, val) SELECT id + 20, name, val FROM src;
行为 INSERT Table(1:26~1:34) /test/1/catalog1/schema1/myisam_t/ -> Table(1:82~1:85) /test/1/catalog1/schema1/src/
------
SQL  REPLACE INTO table_target SELECT stations.mexs_id AS mexs_id, datetime AS messzeit FROM table_source INNER JOIN view_stations AS stations ON table_source.id = stations.icao LEFT JOIN table_target AS old USING (mexs_id);
行为 MERGE Table(1:13~1:25) /test/1/catalog1/schema1/table_target/ -> [Table(1:88~1:100) /test/1/catalog1/schema1/table_source/ ; Table(1:112~1:125) /test/1/catalog1/schema1/view_stations/ ; Table(1:183~1:195) /test/1/catalog1/schema1/table_target/]
------
SQL  INSERT INTO t2 SELECT STRAIGHT_JOIN * FROM t1 AS alias1 WHERE EXISTS (SELECT * FROM (SELECT * FROM t1 JOIN t1 AS alias2 USING (f1)) AS alias3 WHERE alias1.f1 < 20);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t2/ -> Table(1:43~1:45) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO split_subquery_third.dest SELECT a,b FROM split_subquery_third.src AS o WHERE b=(SELECT MIN(i.b) FROM split_subquery_third.src AS i WHERE i.a=o.a);
行为 INSERT Table(1:12~1:37) /test/1/catalog1/split_subquery_third/dest/ -> Table(1:54~1:78) /test/1/catalog1/split_subquery_third/src/
行为 CALL Function(1:100~1:103) /test/1/catalog1/schema1/MIN/
------
SQL  INSERT DELAYED IGNORE INTO d1_t1 PARTITION(p0, p1) (c1, c2) SELECT c1, 'a' FROM d1_t2 ON DUPLICATE KEY UPDATE c2 = 'c';
行为 MERGE Table(1:27~1:32) /test/1/catalog1/schema1/d1_t1/ -> Table(1:80~1:85) /test/1/catalog1/schema1/d1_t2/
------
SQL  INSERT IGNORE INTO d2_t2 PARTITION (subp3) SELECT * FROM d2_t1 PARTITION (subp3, `p10-99`, `p100-99999`);
行为 INSERT Table(1:19~1:24) /test/1/catalog1/schema1/d2_t2/ -> Table(1:57~1:62) /test/1/catalog1/schema1/d2_t1/
------
SQL  INSERT INTO d3_t1 SELECT MAX(id) FROM d3_t2 GROUP BY id ORDER BY id ON DUPLICATE KEY UPDATE d3_t1.id = id + 10;
行为 MERGE Table(1:12~1:17) /test/1/catalog1/schema1/d3_t1/ -> Table(1:38~1:43) /test/1/catalog1/schema1/d3_t2/
行为 CALL Function(1:25~1:28) /test/1/catalog1/schema1/MAX/
------
SQL  INSERT INTO split_dml_odku_dst\n  SELECT split_dml_odku_src.a FROM split_dml_odku_src GROUP BY split_dml_odku_src.a\n  ON DUPLICATE KEY UPDATE a = split_dml_odku_dst.a + split_dml_odku_src.b;
行为 MERGE Table(1:12~1:30) /test/1/catalog1/schema1/split_dml_odku_dst/ -> Table(2:35~2:53) /test/1/catalog1/schema1/split_dml_odku_src/
------
SQL  INSERT INTO t2(i) SELECT d FROM t1;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t2/ -> Table(1:32~1:34) /test/1/catalog1/schema1/t1/
------
SQL  INSERT IGNORE INTO bit_t(b) SELECT d FROM bit_double;
行为 INSERT Table(1:19~1:24) /test/1/catalog1/schema1/bit_t/ -> Table(1:42~1:52) /test/1/catalog1/schema1/bit_double/
------
SQL  INSERT INTO bit_insert_dst(b) SELECT SQL_SMALL_RESULT DISTINCT b FROM bit_t;
行为 INSERT Table(1:12~1:26) /test/1/catalog1/schema1/bit_insert_dst/ -> Table(1:70~1:75) /test/1/catalog1/schema1/bit_t/
------
SQL  INSERT INTO str_lifecycle(v,c,fixed_binary,variable_binary,t)\nSELECT CAST(c AS CHAR), CAST(c AS CHAR), CAST(c AS BINARY), CAST(c AS BINARY), CONCAT(c,c) FROM str_ctas;
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/str_lifecycle/ -> Table(2:96~2:104) /test/1/catalog1/schema1/str_ctas/
行为 CALL Function(2:7~2:11) /test/1/catalog1/schema1/CAST/
行为 CALL Function(2:79~2:85) /test/1/catalog1/schema1/CONCAT/
------
SQL  INSERT INTO str_foos(parent_id,text) SELECT id,'correct output' FROM str_parents;
行为 INSERT Table(1:12~1:20) /test/1/catalog1/schema1/str_foos/ -> Table(1:69~1:80) /test/1/catalog1/schema1/str_parents/
------
SQL  INSERT INTO str_bars(id,parent_id) SELECT X'79CEA9AB6FE14A8EBDFED711A7727763',id FROM str_parents;
行为 INSERT Table(1:12~1:20) /test/1/catalog1/schema1/str_bars/ -> Table(1:86~1:97) /test/1/catalog1/schema1/str_parents/
------
SQL  INSERT INTO lob_family(tb,b,mb,lb,tt,t,mt,lt)\nSELECT b,b,b,b,t,t,t,t FROM lob_lifecycle;
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/lob_family/ -> Table(2:28~2:41) /test/1/catalog1/schema1/lob_lifecycle/
------
SQL  INSERT INTO spatial_lifecycle\n  (id,g,p,ls,pg,mp,mls,mpg,gc,p_changed)\nSELECT\n  3,g,p,ls,pg,mp,mls,mpg,gc,p_changed\nFROM spatial_lifecycle\nWHERE id=1;
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/ -> Table(5:5~5:22) /test/1/catalog1/schema1/spatial_lifecycle/
------
SQL  INSERT INTO integer_lifecycle\n  (tiny_signed,tiny_unsigned,small_signed,small_unsigned,medium_signed,medium_unsigned,\n   int_signed,int_unsigned,big_signed,big_unsigned,bool_alias,boolean_alias,\n   tiny_added,small_changed,medium_added,big_changed,note)\nSELECT\n  tiny_signed,tiny_unsigned,small_signed,small_unsigned,medium_signed,medium_unsigned,\n  int_signed,int_unsigned+100,big_signed,big_unsigned,bool_alias,boolean_alias,\n  tiny_added,small_changed,medium_added,big_changed,'select'\nFROM integer_lifecycle\nWHERE int_unsigned=101;
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/ -> Table(9:5~9:22) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  INSERT INTO numeric_lifecycle\n  (id,decimal_value,numeric_value,fixed_value,float_value,float_scale,\n   double_value,real_value,decimal_added,numeric_changed,fixed_added,\n   float_changed,double_added,real_changed,note)\nSELECT\n  id+10,decimal_value,numeric_value,fixed_value,float_value,float_scale,\n  double_value,real_value,decimal_added,numeric_changed,fixed_added,\n  float_changed,double_added,real_changed,'select'\nFROM numeric_lifecycle\nWHERE id=1;
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/ -> Table(9:5~9:22) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  INSERT INTO t3 SELECT t2.*,1,2 FROM t2;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> Table(1:36~1:38) /test/1/catalog1/schema1/t2/
------
SQL  INSERT INTO t3 SELECT t2.*,t2.*,3 FROM t2;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> Table(1:39~1:41) /test/1/catalog1/schema1/t2/
------
SQL  INSERT IGNORE INTO dst SELECT * FROM src LOCK IN SHARE MODE;
行为 INSERT Table(1:19~1:22) /test/1/catalog1/schema1/dst/ -> Table(1:37~1:40) /test/1/catalog1/schema1/src/
------
SQL  INSERT IGNORE INTO dst SELECT * FROM src FOR UPDATE;
行为 INSERT Table(1:19~1:22) /test/1/catalog1/schema1/dst/ -> Table(1:37~1:40) /test/1/catalog1/schema1/src/
------
SQL  INSERT INTO t3 (SELECT a,b FROM t1) UNION (SELECT a,b FROM t2) LIMIT 2;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> [Table(1:32~1:34) /test/1/catalog1/schema1/t1/ ; Table(1:59~1:61) /test/1/catalog1/schema1/t2/]
------
SQL  INSERT t1 SELECT 5,6,30 FROM DUAL ON DUPLICATE KEY UPDATE c=c+100;
行为 MERGE Table(1:7~1:9) /test/1/catalog1/schema1/t1/ -> Table(1:29~1:33) /test/1/catalog1/schema1/DUAL/
------
SQL  INSERT INTO t1(a) SELECT 1 UNION SELECT 2 ON DUPLICATE KEY UPDATE a=(SELECT d FROM t2 GROUP BY 1);
行为 MERGE Table(1:12~1:14) /test/1/catalog1/schema1/t1/ -> Table(1:83~1:85) /test/1/catalog1/schema1/t2/
------
SQL  DELETE FROM split_packet_native.t1,split_packet_native.t2 USING split_packet_native.t1 INNER JOIN split_packet_native.t2 WHERE split_packet_native.t1.c11=split_packet_native.t2.c21 AND split_packet_native.t2.c22 <=> REPEAT('ab',@max_allowed_packet);
行为 DELETE Table(1:12~1:34) /test/1/catalog1/split_packet_native/t1/ -> [Table(1:64~1:86) /test/1/catalog1/split_packet_native/t1/ ; Table(1:98~1:120) /test/1/catalog1/split_packet_native/t2/]
行为 DELETE Table(1:35~1:57) /test/1/catalog1/split_packet_native/t2/
行为 CALL Function(1:216~1:222) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:228~1:247) /test/1/max_allowed_packet/
------
SQL  DELETE IGNORE FROM split_packet_native.t1,split_packet_native.t2 USING split_packet_native.t1 INNER JOIN split_packet_native.t2 WHERE split_packet_native.t1.c11=split_packet_native.t2.c21 AND split_packet_native.t2.c22 <=> REPEAT('ab',@max_allowed_packet);
行为 DELETE Table(1:19~1:41) /test/1/catalog1/split_packet_native/t1/ -> [Table(1:71~1:93) /test/1/catalog1/split_packet_native/t1/ ; Table(1:105~1:127) /test/1/catalog1/split_packet_native/t2/]
行为 DELETE Table(1:42~1:64) /test/1/catalog1/split_packet_native/t2/
行为 CALL Function(1:223~1:229) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:235~1:254) /test/1/max_allowed_packet/
------
SQL  INSERT INTO split_packet_native.t4 SELECT c31,CONCAT(c32,REPEAT('a',@max_allowed_packet-1)) FROM split_packet_native.t3;
行为 INSERT Table(1:12~1:34) /test/1/catalog1/split_packet_native/t4/ -> Table(1:97~1:119) /test/1/catalog1/split_packet_native/t3/
行为 CALL Function(1:46~1:52) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:57~1:63) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:68~1:87) /test/1/max_allowed_packet/
------
SQL  INSERT INTO dml_t () SELECT * FROM dml_t;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/dml_t/ -> Table(1:35~1:40) /test/1/catalog1/schema1/dml_t/
------
SQL  INSERT INTO dml_t SELECT HIGH_PRIORITY * FROM dml_t;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/dml_t/ -> Table(1:46~1:51) /test/1/catalog1/schema1/dml_t/
------
SQL  INSERT INTO dml_t SELECT DISTINCT ALL * FROM dml_t;
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/dml_t/ -> Table(1:45~1:50) /test/1/catalog1/schema1/dml_t/
------
SQL  REPLACE INTO `` SELECT * FROM ``;
行为 MERGE Table(1:13~1:15) /test/1/catalog1/schema1/ -> Table(1:30~1:32) /test/1/catalog1/schema1/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.tsv' INTO TABLE load_innodb;
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD DATA LOW_PRIORITY LOCAL INFILE '/var/lib/mysql-files/split_load.csv' REPLACE INTO TABLE load_innodb CHARACTER SET utf8mb4 FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES STARTING BY '' TERMINATED BY '\n' IGNORE 1 LINES (id, @raw_name, val) SET name = TRIM(@raw_name);
行为 IMPORT Table(1:93~1:104) /test/1/catalog1/schema1/load_innodb/ -> File(1:36~1:73) /test/1/var/lib/mysql-files/split_load.csv/
行为 READ ConfigKey(1:255~1:264) /test/1/raw_name/
行为 CALL Function(1:282~1:286) /test/1/catalog1/schema1/TRIM/
------
SQL  LOAD DATA CONCURRENT INFILE '/var/lib/mysql-files/split_load.csv' IGNORE INTO TABLE load_myisam COLUMNS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n' IGNORE 1 ROWS (id, name, val) SET val = DEFAULT;
行为 IMPORT Table(1:84~1:95) /test/1/catalog1/schema1/load_myisam/ -> File(1:28~1:65) /test/1/var/lib/mysql-files/split_load.csv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.csv' INTO TABLE load_partition PARTITION (p0) FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' IGNORE 1 LINES;
行为 IMPORT Table(1:66~1:80) /test/1/catalog1/schema1/load_partition/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.csv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/codex_load_audit_missing_empty.csv' INTO TABLE codex_load_audit_s.t ();
行为 IMPORT Table(1:86~1:106) /test/1/catalog1/codex_load_audit_s/t/ -> File(1:17~1:74) /test/1/var/lib/mysql-files/codex_load_audit_missing_empty.csv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/codex_load_audit_missing_subquery.csv' INTO TABLE codex_load_audit_s.t (@codex_load_audit_x) SET b = (SELECT 1);
行为 IMPORT Table(1:89~1:109) /test/1/catalog1/codex_load_audit_s/t/ -> File(1:17~1:77) /test/1/var/lib/mysql-files/codex_load_audit_missing_subquery.csv/
行为 READ ConfigKey(1:111~1:130) /test/1/codex_load_audit_x/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/codex_missing.csv' INTO TABLE dml_audit.t (@x) SET v := @x;
行为 IMPORT Table(1:69~1:80) /test/1/catalog1/dml_audit/t/ -> File(1:17~1:57) /test/1/var/lib/mysql-files/codex_missing.csv/
行为 READ ConfigKey(1:82~1:84) /test/1/x/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb;
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML LOW_PRIORITY LOCAL INFILE '/var/lib/mysql-files/split_load.xml' REPLACE INTO TABLE load_innodb ROWS IDENTIFIED BY '<row>' IGNORE 0 ROWS (id, @raw_name, val) SET name = TRIM(@raw_name);
行为 IMPORT Table(1:92~1:103) /test/1/catalog1/schema1/load_innodb/ -> File(1:35~1:72) /test/1/var/lib/mysql-files/split_load.xml/
行为 READ ConfigKey(1:150~1:159) /test/1/raw_name/
行为 CALL Function(1:177~1:181) /test/1/catalog1/schema1/TRIM/
------
SQL  LOAD XML CONCURRENT INFILE '/var/lib/mysql-files/split_load.xml' IGNORE INTO TABLE load_myisam ROWS IDENTIFIED BY '<row>';
行为 IMPORT Table(1:83~1:94) /test/1/catalog1/schema1/load_myisam/ -> File(1:27~1:64) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_partition PARTITION (p0) ROWS IDENTIFIED BY '<row>';
行为 IMPORT Table(1:65~1:79) /test/1/catalog1/schema1/load_partition/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb ROWS IDENTIFIED BY '<row>' COLUMNS TERMINATED BY ',' LINES STARTING BY '<' TERMINATED BY '>';
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/codex_missing.xml' INTO TABLE dml_audit.t CHARACTER SET utf8mb4 ROWS IDENTIFIED BY '<row>';
行为 IMPORT Table(1:68~1:79) /test/1/catalog1/dml_audit/t/ -> File(1:16~1:56) /test/1/var/lib/mysql-files/codex_missing.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/codex_missing.xml' INTO TABLE dml_audit.t ();
行为 IMPORT Table(1:68~1:79) /test/1/catalog1/dml_audit/t/ -> File(1:16~1:56) /test/1/var/lib/mysql-files/codex_missing.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/codex_missing.xml' INTO TABLE dml_audit.t FIELDS TERMINATED BY ',';
行为 IMPORT Table(1:68~1:79) /test/1/catalog1/dml_audit/t/ -> File(1:16~1:56) /test/1/var/lib/mysql-files/codex_missing.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/codex_missing.xml' INTO TABLE dml_audit.t (@x) SET v := @x;
行为 IMPORT Table(1:68~1:79) /test/1/catalog1/dml_audit/t/ -> File(1:16~1:56) /test/1/var/lib/mysql-files/codex_missing.xml/
行为 READ ConfigKey(1:81~1:83) /test/1/x/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 FIELDS TERMINATED BY 'raker';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 FIELDS ENCLOSED BY 0xC3;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 FIELDS ENCLOSED BY '12345';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 FIELDS ESCAPED BY '12345';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET binary FIELDS ENCLOSED BY 'ъ';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET binary FIELDS ESCAPED BY 'ъ';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET binary FIELDS TERMINATED BY 'ъ';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET binary LINES STARTING BY 'ъ';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET binary LINES TERMINATED BY 'ъ';
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET binary;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET latin1;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET koi8r;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET utf8;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 FIELDS ENCLOSED BY 0b00100010;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.txt'\nINTO TABLE load_t FIELDS ESCAPED BY '\\' TERMINATED BY ' ';
行为 IMPORT Table(2:11~2:17) /test/1/catalog1/schema1/load_t/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.txt/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.txt'\nINTO TABLE load_t (a, @raw_b)\nSET b=@raw_b+10, c=CONCAT('b=', @raw_b);
行为 IMPORT Table(2:11~2:17) /test/1/catalog1/schema1/load_t/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.txt/
行为 READ ConfigKey(2:22~2:28) /test/1/raw_b/
行为 CALL Function(3:19~3:25) /test/1/catalog1/schema1/CONCAT/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.txt'\nINTO TABLE load_v(@a,@d) SET a=@a,d=@d;
行为 IMPORT Table(2:11~2:17) /test/1/catalog1/schema1/load_v/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.txt/
行为 READ ConfigKey(2:18~2:20) /test/1/a/
行为 READ ConfigKey(2:21~2:23) /test/1/d/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_v;
行为 IMPORT Table(1:65~1:71) /test/1/catalog1/schema1/load_v/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD DATA INFILE '/tmp/codex_lob.csv'\nINTO TABLE lob_family\nCHARACTER SET binary\nFIELDS TERMINATED BY ','\nLINES TERMINATED BY '\n'\n(@b,@t)\nSET b=UNHEX(@b),t=CONVERT(@t USING utf8mb4);
行为 IMPORT Table(2:11~2:21) /test/1/catalog1/schema1/lob_family/ -> File(1:17~1:37) /test/1/tmp/codex_lob.csv/
行为 READ ConfigKey(6:1~6:3) /test/1/b/
行为 READ ConfigKey(6:4~6:6) /test/1/t/
行为 CALL Function(7:6~7:11) /test/1/catalog1/schema1/UNHEX/
行为 CALL Function(7:18~7:25) /test/1/catalog1/schema1/CONVERT/
------
SQL  LOAD DATA LOCAL INFILE '/tmp/nonexistent-enum-set.csv'\n    INTO TABLE split_type_enum_set.es_core\n    FIELDS TERMINATED BY ','\n    (id,e_basic,e_case,s_basic,s_case,e_added,s_flags);
行为 IMPORT Table(2:15~2:42) /test/1/catalog1/split_type_enum_set/es_core/ -> File(1:23~1:54) /test/1/tmp/nonexistent-enum-set.csv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/codex-spatial.csv'\nINTO TABLE spatial_lifecycle\nFIELDS TERMINATED BY ','\nLINES TERMINATED BY '\n'\n(@id,@wkt)\nSET id=@id,g=ST_GeomFromText(@wkt),p=ST_PointFromText(@wkt);
行为 IMPORT Table(2:11~2:28) /test/1/catalog1/schema1/spatial_lifecycle/ -> File(1:17~1:57) /test/1/var/lib/mysql-files/codex-spatial.csv/
行为 READ ConfigKey(5:1~5:4) /test/1/id/
行为 READ ConfigKey(5:5~5:9) /test/1/wkt/
行为 CALL Function(6:13~6:28) /test/1/catalog1/schema1/ST_GeomFromText/
行为 CALL Function(6:37~6:53) /test/1/catalog1/schema1/ST_PointFromText/
------
SQL  LOAD DATA LOCAL INFILE '/tmp/nonexistent-integer.csv'\nINTO TABLE integer_lifecycle\nFIELDS TERMINATED BY ','\n(tiny_signed,tiny_unsigned,small_signed,small_unsigned,medium_signed,medium_unsigned,\n int_signed,int_unsigned,big_signed,big_unsigned,@bool_value,note)\nSET bool_alias=CAST(@bool_value AS UNSIGNED),boolean_alias=NOT CAST(@bool_value AS UNSIGNED);
行为 IMPORT Table(2:11~2:28) /test/1/catalog1/schema1/integer_lifecycle/ -> File(1:23~1:53) /test/1/tmp/nonexistent-integer.csv/
行为 READ ConfigKey(5:49~5:60) /test/1/bool_value/
行为 CALL Function(6:15~6:19) /test/1/catalog1/schema1/CAST/
------
SQL  LOAD DATA LOCAL INFILE '/tmp/nonexistent-numeric.csv'\nINTO TABLE numeric_lifecycle\nFIELDS TERMINATED BY ','\n(id,@decimal_value,@numeric_value,@fixed_value,@float_value,@double_value,@real_value,note)\nSET decimal_value=CAST(@decimal_value AS DECIMAL(20,6)),\n    numeric_value=CAST(@numeric_value AS DECIMAL(12,4)),\n    fixed_value=CAST(@fixed_value AS DECIMAL(18,2)),\n    float_value=@float_value+0e0,\n    double_value=@double_value+0e0,\n    real_value=@real_value+0e0;
行为 IMPORT Table(2:11~2:28) /test/1/catalog1/schema1/numeric_lifecycle/ -> File(1:23~1:53) /test/1/tmp/nonexistent-numeric.csv/
行为 READ ConfigKey(4:4~4:18) /test/1/decimal_value/
行为 READ ConfigKey(4:19~4:33) /test/1/numeric_value/
行为 READ ConfigKey(4:34~4:46) /test/1/fixed_value/
行为 READ ConfigKey(4:47~4:59) /test/1/float_value/
行为 READ ConfigKey(4:60~4:73) /test/1/double_value/
行为 READ ConfigKey(4:74~4:85) /test/1/real_value/
行为 CALL Function(5:18~5:22) /test/1/catalog1/schema1/CAST/
------
SQL  LOAD DATA INFILE '/tmp/codex_missing_load' INTO TABLE dst (@'raw value') SET id=@'raw value';
行为 IMPORT Table(1:54~1:57) /test/1/catalog1/schema1/dst/ -> File(1:17~1:42) /test/1/tmp/codex_missing_load/
行为 READ ConfigKey(1:59~1:71) /test/1/'raw value'/
------
SQL  LOAD DATA INFILE '/tmp/x' INTO TABLE t FIELDS TERMINATED BY '' ENCLOSED BY '' LINES TERMINATED BY '' IGNORE 1 LINES;
行为 IMPORT Table(1:37~1:38) /test/1/catalog1/schema1/t/ -> File(1:17~1:25) /test/1/tmp/x/
------
SQL  LOAD DATA INFILE 'x' INTO TABLE v(a,d);
行为 IMPORT Table(1:32~1:33) /test/1/catalog1/schema1/v/ -> File(1:17~1:20) /test/1/x/
------
SQL  LOAD DATA INFILE 'loadtest.txt' INTO TABLE t1 PARTITION (pNeg,`p10-99`) CHARACTER SET latin1;
行为 IMPORT Table(1:43~1:45) /test/1/catalog1/schema1/t1/ -> File(1:17~1:31) /test/1/loadtest.txt/
------
SQL  insert into test (id, name) select id, name from test2;
行为 INSERT Table(1:12~1:16) /test/1/catalog1/schema1/test/ -> Table(1:49~1:54) /test/1/catalog1/schema1/test2/
------
SQL  delete a from test a join test2 b on a.id = b.id;
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/a/ -> [Table(1:14~1:18) /test/1/catalog1/schema1/test/ ; Table(1:26~1:31) /test/1/catalog1/schema1/test2/]
------
SQL  UPDATE split_derived_80common.u1 SET a=5 WHERE a IN (SELECT a FROM split_derived_80common.u2 ORDER BY (SELECT a FROM (SELECT SUM(a) FROM split_derived_80common.u1) AS dt));
行为 UPDATE Table(1:7~1:32) /test/1/catalog1/split_derived_80common/u1/ -> [Table(1:67~1:92) /test/1/catalog1/split_derived_80common/u2/ ; Table(1:137~1:162) /test/1/catalog1/split_derived_80common/u1/]
行为 CALL Function(1:125~1:128) /test/1/catalog1/schema1/SUM/
------
SQL  DELETE FROM split_derived_dml57.t1 WHERE id IN (SELECT * FROM (SELECT id FROM split_derived_dml57.t1) AS dt);
行为 DELETE Table(1:12~1:34) /test/1/catalog1/split_derived_dml57/t1/ -> Table(1:78~1:100) /test/1/catalog1/split_derived_dml57/t1/
------
SQL  UPDATE split_derived_dml57.t1 SET d=NULL WHERE id IN (SELECT * FROM (SELECT id FROM split_derived_dml57.t1) AS dt);
行为 UPDATE Table(1:7~1:29) /test/1/catalog1/split_derived_dml57/t1/ -> Table(1:84~1:106) /test/1/catalog1/split_derived_dml57/t1/
------
SQL  INSERT INTO split_derived_dml57.t1 SELECT id+10,d FROM split_derived_dml57.t1;
行为 INSERT Table(1:12~1:34) /test/1/catalog1/split_derived_dml57/t1/ -> Table(1:55~1:77) /test/1/catalog1/split_derived_dml57/t1/
------
SQL  INSERT INTO split_derived_dml57.t1 SELECT id+20,d FROM (SELECT * FROM split_derived_dml57.t1) AS dt;
行为 INSERT Table(1:12~1:34) /test/1/catalog1/split_derived_dml57/t1/ -> Table(1:70~1:92) /test/1/catalog1/split_derived_dml57/t1/
------
SQL  UPDATE split_derived_dml57.users SET position=(SELECT COUNT(pos)+1 FROM (SELECT DISTINCT position AS pos FROM split_derived_dml57.users) AS t2 WHERE t2.pos<users.position) WHERE id=3;
行为 UPDATE Table(1:7~1:32) /test/1/catalog1/split_derived_dml57/users/ -> Table(1:110~1:135) /test/1/catalog1/split_derived_dml57/users/
行为 CALL Function(1:54~1:59) /test/1/catalog1/schema1/COUNT/
------
SQL  UPDATE split_derived_dml57.users SET position=(SELECT COUNT(pos)+1 FROM (SELECT position AS pos FROM split_derived_dml57.users) AS t2 WHERE t2.pos<users.position) WHERE id=3;
行为 UPDATE Table(1:7~1:32) /test/1/catalog1/split_derived_dml57/users/ -> Table(1:101~1:126) /test/1/catalog1/split_derived_dml57/users/
行为 CALL Function(1:54~1:59) /test/1/catalog1/schema1/COUNT/
------
SQL  DELETE a,b FROM split_derived_dml57.t1 AS a LEFT JOIN split_derived_dml57.t2 AS b ON a.id=b.id WHERE a.id IN (SELECT * FROM (SELECT id FROM split_derived_dml57.t1) AS t1sub);
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/a/ -> [Table(1:16~1:38) /test/1/catalog1/split_derived_dml57/t1/ ; Table(1:54~1:76) /test/1/catalog1/split_derived_dml57/t2/]
行为 DELETE Table(1:9~1:10) /test/1/catalog1/schema1/b/
------
SQL  INSERT INTO split_distinct_native_a.facility_dst SELECT DISTINCT facility FROM split_distinct_native_a.facility_src;
行为 INSERT Table(1:12~1:48) /test/1/catalog1/split_distinct_native_a/facility_dst/ -> Table(1:79~1:115) /test/1/catalog1/split_distinct_native_a/facility_src/
------
SQL  INSERT INTO t3 SELECT a,MAX(b) FROM t1 GROUP BY a;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> Table(1:36~1:38) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:24~1:27) /test/1/catalog1/schema1/MAX/
------
SQL  INSERT INTO t3 SELECT 1,(SELECT MAX(b) FROM t1 GROUP BY a HAVING a<2) FROM t1 LIMIT 1;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> Table(1:44~1:46) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:32~1:35) /test/1/catalog1/schema1/MAX/
------
SQL  DELETE FROM t3 WHERE (SELECT MAX(b) FROM t1 GROUP BY a HAVING a<2)>10000;
行为 DELETE Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> Table(1:41~1:43) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:29~1:32) /test/1/catalog1/schema1/MAX/
------
SQL  DELETE FROM t3 WHERE (SELECT (SELECT MAX(b) FROM t1 GROUP BY a HAVING a<2) AS x FROM t1)>10000;
行为 DELETE Table(1:12~1:14) /test/1/catalog1/schema1/t3/ -> Table(1:49~1:51) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/MAX/
------
SQL  DELETE /*+ JOIN_PREFIX(t2,t3,ta2) JOIN_SUFFIX(t3,ta1) */ FROM ta1.* USING t1 AS ta1 JOIN t1 AS ta2 ON 1 RIGHT OUTER JOIN t2 ON ta1.val=t2.val WHERE 9 IN (SELECT val FROM t3);
行为 DELETE Table(1:62~1:65) /test/1/catalog1/schema1/ta1/ -> [Table(1:74~1:76) /test/1/catalog1/schema1/t1/ ; Table(1:121~1:123) /test/1/catalog1/schema1/t2/ ; Table(1:170~1:172) /test/1/catalog1/schema1/t3/]
------
SQL  DELETE /*+ NO_RANGE_OPTIMIZATION(t1 PRIMARY) NO_BNL(t2@sub_q) */ FROM split_opt_hints_native.t1 WHERE a IN (SELECT /*+ QB_NAME(sub_q) */ a FROM split_opt_hints_native.t2 AS t2 WHERE t2.b>10);
行为 DELETE Table(1:70~1:95) /test/1/catalog1/split_opt_hints_native/t1/ -> Table(1:144~1:169) /test/1/catalog1/split_opt_hints_native/t2/
------
SQL  INSERT /*+ NO_ICP(t2@src_q idx_ab) */ INTO split_opt_hints_native.t3(a,b,c) SELECT /*+ QB_NAME(src_q) */ t2.a+20,t2.b,t2.c FROM split_opt_hints_native.t2 AS t2;
行为 INSERT Table(1:43~1:68) /test/1/catalog1/split_opt_hints_native/t3/ -> Table(1:128~1:153) /test/1/catalog1/split_opt_hints_native/t2/
------
SQL  REPLACE /*+ NO_ICP(t2@src_q idx_ab) */ INTO split_opt_hints_native.t3(a,b,c) SELECT /*+ QB_NAME(src_q) */ t2.a+30,t2.b,t2.c FROM split_opt_hints_native.t2 AS t2;
行为 MERGE Table(1:44~1:69) /test/1/catalog1/split_opt_hints_native/t3/ -> Table(1:129~1:154) /test/1/catalog1/split_opt_hints_native/t2/
------
SQL  INSERT /*+ SET_VAR(unique_checks=OFF) */ INTO t1 SELECT /*+ SET_VAR(sort_buffer_size=16M) */ id+100, f1 FROM t2;
行为 INSERT Table(1:46~1:48) /test/1/catalog1/schema1/t1/ -> Table(1:109~1:111) /test/1/catalog1/schema1/t2/
行为 CONFIGURE ConfigKey(1:19~1:32) /test/1/unique_checks/
行为 CONFIGURE ConfigKey(1:68~1:84) /test/1/sort_buffer_size/
------
SQL  INSERT INTO split_select_short.distinct_t (SELECT 'a','a','a','a111','xy1','' FROM split_select_short.t);
行为 INSERT Table(1:12~1:41) /test/1/catalog1/split_select_short/distinct_t/ -> Table(1:83~1:103) /test/1/catalog1/split_select_short/t/
------
SQL  REPLACE INTO split_select_short.distinct_t (SELECT 'a','a','a','a111','xy1','' FROM split_select_short.t);
行为 MERGE Table(1:13~1:42) /test/1/catalog1/split_select_short/distinct_t/ -> Table(1:84~1:104) /test/1/catalog1/split_select_short/t/
------
SQL  DELETE x.*,y.* FROM split_subquery_base.t11a AS x,split_subquery_base.t12a AS y WHERE x.a=y.a AND x.b=(SELECT z.b FROM split_subquery_base.t2 AS z WHERE z.a=x.a);
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/x/ -> [Table(1:20~1:44) /test/1/catalog1/split_subquery_base/t11a/ ; Table(1:50~1:74) /test/1/catalog1/split_subquery_base/t12a/ ; Table(1:119~1:141) /test/1/catalog1/split_subquery_base/t2/]
行为 DELETE Table(1:11~1:12) /test/1/catalog1/schema1/y/
------
SQL  DELETE x.*,y.* FROM split_subquery_base.t11b AS x,split_subquery_base.t12b AS y WHERE x.a=y.a AND x.b=(SELECT z.b FROM split_subquery_base.t12b AS z WHERE z.a=x.a);
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/x/ -> [Table(1:20~1:44) /test/1/catalog1/split_subquery_base/t11b/ ; Table(1:50~1:74) /test/1/catalog1/split_subquery_base/t12b/]
行为 DELETE Table(1:11~1:12) /test/1/catalog1/schema1/y/
------
SQL  INSERT INTO split_subquery_base.ins_t(x) SELECT (SELECT SUM(s.x)+2 FROM split_subquery_base.ins_t AS s) FROM split_subquery_base.t2;
行为 INSERT Table(1:12~1:37) /test/1/catalog1/split_subquery_base/ins_t/ -> [Table(1:72~1:97) /test/1/catalog1/split_subquery_base/ins_t/ ; Table(1:109~1:131) /test/1/catalog1/split_subquery_base/t2/]
行为 CALL Function(1:56~1:59) /test/1/catalog1/schema1/SUM/
------
SQL  INSERT INTO split_subquery_base.ins_values(x) VALUES ((SELECT x FROM split_subquery_base.ins_values));
行为 INSERT Table(1:12~1:42) /test/1/catalog1/split_subquery_base/ins_values/ -> Table(1:69~1:99) /test/1/catalog1/split_subquery_base/ins_values/
------
SQL  REPLACE LOW_PRIORITY INTO split_subquery_base.rep_t(x,y) VALUES ((SELECT a FROM split_subquery_base.t2 WHERE a=1),(SELECT a+1 FROM split_subquery_base.t2 WHERE a=1));
行为 MERGE Table(1:26~1:51) /test/1/catalog1/split_subquery_base/rep_t/ -> Table(1:80~1:102) /test/1/catalog1/split_subquery_base/t2/
------
SQL  REPLACE INTO split_subquery_base.rep_self(x,y) VALUES ((SELECT x FROM split_subquery_base.rep_self),(SELECT a+1 FROM split_subquery_base.t2 WHERE a=1));
行为 MERGE Table(1:13~1:41) /test/1/catalog1/split_subquery_base/rep_self/ -> [Table(1:70~1:98) /test/1/catalog1/split_subquery_base/rep_self/ ; Table(1:117~1:139) /test/1/catalog1/split_subquery_base/t2/]
------
SQL  DELETE FROM split_subquery_next.del_t1 WHERE topic IN (SELECT DISTINCT topic FROM split_subquery_next.del_t2 WHERE NOT EXISTS (SELECT * FROM split_subquery_next.del_t3 WHERE numeropost=topic));
行为 DELETE Table(1:12~1:38) /test/1/catalog1/split_subquery_next/del_t1/ -> [Table(1:82~1:108) /test/1/catalog1/split_subquery_next/del_t2/ ; Table(1:141~1:167) /test/1/catalog1/split_subquery_next/del_t3/]
------
SQL  UPDATE IGNORE split_subquery_next.u1 SET b=(SELECT b FROM split_subquery_next.u2 WHERE u1.a=u2.a);
行为 UPDATE Table(1:14~1:36) /test/1/catalog1/split_subquery_next/u1/ -> Table(1:58~1:80) /test/1/catalog1/split_subquery_next/u2/
------
SQL  UPDATE split_table_derived.t1 AS o SET o.a=o.a*100 WHERE o.b<0 OR o.a IN(SELECT i.a+1 FROM split_table_derived.t2 AS i);
行为 UPDATE Table(1:7~1:29) /test/1/catalog1/split_table_derived/t1/ -> Table(1:91~1:113) /test/1/catalog1/split_table_derived/t2/
------
SQL  DELETE FROM split_table_derived.t1 WHERE t1.b<0 OR t1.a IN(SELECT i.a+1 FROM split_table_derived.t2 AS i);
行为 DELETE Table(1:12~1:34) /test/1/catalog1/split_table_derived/t1/ -> Table(1:77~1:99) /test/1/catalog1/split_table_derived/t2/
------
SQL  DELETE o.* FROM split_table_derived.t1 AS o,(SELECT 1) AS d WHERE o.b<0 OR o.a IN(SELECT i.a+1 FROM split_table_derived.t2 AS i);
行为 DELETE Table(1:7~1:8) /test/1/catalog1/schema1/o/ -> [Table(1:16~1:38) /test/1/catalog1/split_table_derived/t1/ ; Table(1:100~1:122) /test/1/catalog1/split_table_derived/t2/]
------
SQL  INSERT INTO t(a,b) SELECT a+1000,b FROM r UNION ALL SELECT a+2000,b FROM s;
行为 INSERT Table(1:12~1:13) /test/1/catalog1/schema1/t/ -> [Table(1:40~1:41) /test/1/catalog1/schema1/r/ ; Table(1:73~1:74) /test/1/catalog1/schema1/s/]
------
SQL  REPLACE INTO t(a,b) SELECT a+3000,b FROM r UNION ALL SELECT a+4000,b FROM s;
行为 MERGE Table(1:13~1:14) /test/1/catalog1/schema1/t/ -> [Table(1:41~1:42) /test/1/catalog1/schema1/r/ ; Table(1:74~1:75) /test/1/catalog1/schema1/s/]
------
SQL  REPLACE INTO target (id, name, val) SELECT id, name, val FROM src;
行为 MERGE Table(1:13~1:19) /test/1/catalog1/schema1/target/ -> Table(1:62~1:65) /test/1/catalog1/schema1/src/
------
SQL  REPLACE INTO codex_group_having_audit_dst(b) SELECT '' AS b FROM codex_group_having_audit_src GROUP BY VALUES(b);
行为 MERGE Table(1:13~1:41) /test/1/catalog1/schema1/codex_group_having_audit_dst/ -> Table(1:65~1:93) /test/1/catalog1/schema1/codex_group_having_audit_src/
行为 CALL Function(1:103~1:109) /test/1/catalog1/schema1/VALUES/
------
SQL  REPLACE DELAYED INTO d10_t1 PARTITION(p0, p1) (c1, c2) SELECT c1, 'a' FROM d10_t2;
行为 MERGE Table(1:21~1:27) /test/1/catalog1/schema1/d10_t1/ -> Table(1:75~1:81) /test/1/catalog1/schema1/d10_t2/
------
SQL  REPLACE INTO d11_t1 PARTITION(p0, p1) SET c1 = (SELECT c1 FROM d11_t2 LIMIT 1);
行为 MERGE Table(1:13~1:19) /test/1/catalog1/schema1/d11_t1/ -> Table(1:63~1:69) /test/1/catalog1/schema1/d11_t2/
------
SQL  REPLACE INTO dst SELECT * FROM src LOCK IN SHARE MODE;
行为 MERGE Table(1:13~1:16) /test/1/catalog1/schema1/dst/ -> Table(1:31~1:34) /test/1/catalog1/schema1/src/
------
SQL  REPLACE INTO t3 SELECT a,b AS c FROM t1 UNION ALL SELECT a,b FROM t2;
行为 MERGE Table(1:13~1:15) /test/1/catalog1/schema1/t3/ -> [Table(1:37~1:39) /test/1/catalog1/schema1/t1/ ; Table(1:66~1:68) /test/1/catalog1/schema1/t2/]
------
SQL  REPLACE t1 SELECT * FROM t2;
行为 MERGE Table(1:8~1:10) /test/1/catalog1/schema1/t1/ -> Table(1:25~1:27) /test/1/catalog1/schema1/t2/
------
SQL  REPLACE t2 SET a=((1) IN (SELECT * FROM t1));
行为 MERGE Table(1:8~1:10) /test/1/catalog1/schema1/t2/ -> Table(1:40~1:42) /test/1/catalog1/schema1/t1/
------
SQL  REPLACE INTO t2 SELECT grp,a,c FROM t1 LIMIT 2,1;
行为 MERGE Table(1:13~1:15) /test/1/catalog1/schema1/t2/ -> Table(1:36~1:38) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE split_subquery_bugs_550.u3 SET field3=9 WHERE field3 IN (SELECT 1 FROM (SELECT * FROM split_subquery_bugs_550.u2) AS alias1 WHERE EXISTS (SELECT * FROM split_subquery_bugs_550.u1 WHERE field1<>alias1.field2));
行为 UPDATE Table(1:7~1:33) /test/1/catalog1/split_subquery_bugs_550/u3/ -> [Table(1:93~1:119) /test/1/catalog1/split_subquery_bugs_550/u2/ ; Table(1:159~1:185) /test/1/catalog1/split_subquery_bugs_550/u1/]
------
SQL  DELETE outr1.* FROM split_subquery_bugs_550.del1 AS outr1 RIGHT OUTER JOIN split_subquery_bugs_550.del2 AS outr2 ON outr1.col_int=outr2.col_int WHERE (0,3) NOT IN (SELECT innr1.pk,innr1.col_int FROM split_subquery_bugs_550.del2 AS innr1 WHERE outr1.col_int=25);
行为 DELETE Table(1:7~1:12) /test/1/catalog1/schema1/outr1/ -> [Table(1:20~1:48) /test/1/catalog1/split_subquery_bugs_550/del1/ ; Table(1:75~1:103) /test/1/catalog1/split_subquery_bugs_550/del2/]
------
SQL  update table1 set name = 1 where id = avg((select id from table2));
行为 UPDATE Table(1:7~1:13) /test/1/catalog1/schema1/table1/ -> Table(1:58~1:64) /test/1/catalog1/schema1/table2/
行为 CALL Function(1:38~1:41) /test/1/catalog1/schema1/avg/
------
SQL  update `table` set id = (select id2 from test limit 1);
行为 UPDATE Table(1:7~1:14) /test/1/catalog1/schema1/table/ -> Table(1:41~1:45) /test/1/catalog1/schema1/test/
------
SQL  update `table` set id = 2 where table.name = (select id2 from test limit 1) order by aa;
行为 UPDATE Table(1:7~1:14) /test/1/catalog1/schema1/table/ -> Table(1:62~1:66) /test/1/catalog1/schema1/test/
------
SQL  UPDATE t SET a = 1 WHERE EXISTS (SELECT b FROM g WHERE 1 NOT LIKE c FOR UPDATE);
行为 UPDATE Table(1:7~1:8) /test/1/catalog1/schema1/t/ -> Table(1:47~1:48) /test/1/catalog1/schema1/g/
------
SQL  UPDATE codex_group_having_audit_dst SET a=(SELECT '' AS b FROM codex_group_having_audit_src GROUP BY VALUES(b));
行为 UPDATE Table(1:7~1:35) /test/1/catalog1/schema1/codex_group_having_audit_dst/ -> Table(1:63~1:91) /test/1/catalog1/schema1/codex_group_having_audit_src/
行为 CALL Function(1:101~1:107) /test/1/catalog1/schema1/VALUES/
------
SQL  SELECT DISTINCT fruit_id,fruit_name INTO OUTFILE '/var/lib/mysql-files/split_distinct_native_b.tsv' FROM split_distinct_native_b.fruit_t WHERE fruit_name='APPLE';
行为 EXPORT File(1:49~1:99) /test/1/var/lib/mysql-files/split_distinct_native_b.tsv/ -> Table(1:105~1:136) /test/1/catalog1/split_distinct_native_b/fruit_t/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_01.txt' FIELDS TERMINATED BY 'raker' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_01.txt/ -> Table(1:105~1:133) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_02.txt' FIELDS ENCLOSED BY 0xC3 FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_02.txt/ -> Table(1:100~1:128) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_03.txt' FIELDS ENCLOSED BY '12345' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_03.txt/ -> Table(1:103~1:131) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_04.txt' FIELDS ESCAPED BY '12345' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_04.txt/ -> Table(1:102~1:130) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_05.txt' FIELDS ENCLOSED BY 'ъ' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_05.txt/ -> Table(1:99~1:127) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_06.txt' FIELDS ESCAPED BY 'ъ' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_06.txt/ -> Table(1:98~1:126) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_07.txt' FIELDS TERMINATED BY 'ъ' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_07.txt/ -> Table(1:101~1:129) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_08.txt' LINES STARTING BY 'ъ' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_08.txt/ -> Table(1:98~1:126) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_09.txt' LINES TERMINATED BY 'ъ' FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_09.txt/ -> Table(1:100~1:128) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_10.txt' CHARACTER SET binary FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_10.txt/ -> Table(1:97~1:125) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_11.txt' CHARACTER SET latin1 FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_11.txt/ -> Table(1:97~1:125) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_12.txt' CHARACTER SET koi8r FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_12.txt/ -> Table(1:96~1:124) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_13.txt' CHARACTER SET utf8 FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_13.txt/ -> Table(1:95~1:123) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_14.txt' FIELDS ENCLOSED BY 0b00100010 FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_14.txt/ -> Table(1:106~1:134) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  SELECT * INTO DUMPFILE '/var/lib/mysql-files/split_outfile_native_limit.bin' FROM split_outfile_native.t1 LIMIT 1;
行为 EXPORT File(1:23~1:76) /test/1/var/lib/mysql-files/split_outfile_native_limit.bin/ -> Table(1:82~1:105) /test/1/catalog1/split_outfile_native/t1/
------
SQL  EXPLAIN SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_native_explain.txt' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\r\n' FROM split_outfile_native.t1;
行为 EXPORT File(1:30~1:85) /test/1/var/lib/mysql-files/split_outfile_native_explain.txt/ -> Table(1:170~1:193) /test/1/catalog1/split_outfile_native/t1/
------
SQL  SELECT schema_name INTO OUTFILE '/var/lib/mysql-files/split_outfile_native_schema.txt' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n' FROM information_schema.schemata WHERE schema_name LIKE 'split_outfile%' LIMIT 0, 5;
行为 EXPORT File(1:32~1:86) /test/1/var/lib/mysql-files/split_outfile_native_schema.txt/ -> Table(1:169~1:196) /test/1/catalog1/information_schema/schemata/
------
SQL  SELECT a INTO OUTFILE '/var/lib/mysql-files/split_union_derived.out' FROM (SELECT a FROM t1 UNION SELECT a FROM t1 WHERE 0) alias;
行为 EXPORT File(1:22~1:68) /test/1/var/lib/mysql-files/split_union_derived.out/ -> Table(1:89~1:91) /test/1/catalog1/schema1/t1/
------
SQL  SELECT a INTO DUMPFILE '/var/lib/mysql-files/split_union_derived.dump' FROM (SELECT a FROM t1 UNION SELECT a FROM t1 WHERE 0) alias;
行为 EXPORT File(1:23~1:70) /test/1/var/lib/mysql-files/split_union_derived.dump/ -> Table(1:91~1:93) /test/1/catalog1/schema1/t1/
------
SQL  SELECT (SELECT a UNION SELECT a) INTO OUTFILE '/var/lib/mysql-files/split_union_scalar.out' FROM t1;
行为 EXPORT File(1:46~1:91) /test/1/var/lib/mysql-files/split_union_scalar.out/ -> Table(1:97~1:99) /test/1/catalog1/schema1/t1/
------
SQL  SELECT (SELECT a UNION SELECT a) INTO DUMPFILE '/var/lib/mysql-files/split_union_scalar.dump' FROM t1;
行为 EXPORT File(1:47~1:93) /test/1/var/lib/mysql-files/split_union_scalar.dump/ -> Table(1:99~1:101) /test/1/catalog1/schema1/t1/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/codex_func_analyse.out' FROM analyse_int PROCEDURE ANALYSE();
行为 EXPORT File(1:22~1:67) /test/1/var/lib/mysql-files/codex_func_analyse.out/ -> Table(1:73~1:84) /test/1/catalog1/schema1/analyse_int/
------
SQL  SELECT * INTO DUMPFILE '/var/lib/mysql-files/codex_func_analyse.dump' FROM analyse_int PROCEDURE ANALYSE();
行为 EXPORT File(1:23~1:69) /test/1/var/lib/mysql-files/codex_func_analyse.dump/ -> Table(1:75~1:86) /test/1/catalog1/schema1/analyse_int/
------
SQL  SELECT * FROM analyse_int PROCEDURE ANALYSE() INTO OUTFILE '/var/lib/mysql-files/codex_func_analyse_tail.out';
行为 EXPORT File(1:59~1:109) /test/1/var/lib/mysql-files/codex_func_analyse_tail.out/ -> Table(1:14~1:25) /test/1/catalog1/schema1/analyse_int/
------
SQL  SELECT * FROM analyse_int PROCEDURE ANALYSE() INTO DUMPFILE '/var/lib/mysql-files/codex_func_analyse_tail.dump';
行为 EXPORT File(1:60~1:111) /test/1/var/lib/mysql-files/codex_func_analyse_tail.dump/ -> Table(1:14~1:25) /test/1/catalog1/schema1/analyse_int/
------
SQL  EXPLAIN INSERT INTO codex_func_rand.t SELECT MAX(i1),FLOOR(RAND(0)*1000) FROM codex_func_rand.t;
行为 INSERT Table(1:20~1:37) /test/1/catalog1/codex_func_rand/t/ -> Table(1:78~1:95) /test/1/catalog1/codex_func_rand/t/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:53~1:58) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:59~1:63) /test/1/catalog1/schema1/RAND/
------
SQL  EXPLAIN INSERT INTO codex_func_rand.t SELECT MAX(i1),FLOOR(RAND(0)*1000)+EXTRACT(YEAR FROM NOW()) DIV 1000 FROM codex_func_rand.t;
行为 INSERT Table(1:20~1:37) /test/1/catalog1/codex_func_rand/t/ -> Table(1:112~1:129) /test/1/catalog1/codex_func_rand/t/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:53~1:58) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:59~1:63) /test/1/catalog1/schema1/RAND/
行为 CALL Function(1:73~1:80) /test/1/catalog1/schema1/EXTRACT/
行为 CALL Function(1:91~1:94) /test/1/catalog1/schema1/NOW/
------
SQL  EXPLAIN INSERT INTO codex_func_rand.t SELECT MAX(i1),FLOOR(RAND(0)*1000)+EXTRACT(YEAR FROM NOW()) DIV 1000 FROM codex_func_rand.t GROUP BY i2;
行为 INSERT Table(1:20~1:37) /test/1/catalog1/codex_func_rand/t/ -> Table(1:112~1:129) /test/1/catalog1/codex_func_rand/t/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:53~1:58) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:59~1:63) /test/1/catalog1/schema1/RAND/
行为 CALL Function(1:73~1:80) /test/1/catalog1/schema1/EXTRACT/
行为 CALL Function(1:91~1:94) /test/1/catalog1/schema1/NOW/
------
SQL  EXPLAIN DELETE t1 FROM split_select_safe.t1 AS t1 JOIN split_select_safe.t2 AS t2 ON t1.c2=t2.c1;
行为 DELETE Table(1:15~1:17) /test/1/catalog1/schema1/t1/ -> [Table(1:23~1:43) /test/1/catalog1/split_select_safe/t1/ ; Table(1:55~1:75) /test/1/catalog1/split_select_safe/t2/]
------
SQL  SELECT id, grp INTO OUTFILE '/var/lib/mysql-files/split_select_out_a.txt' CHARACTER SET utf8mb4 FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES STARTING BY '' TERMINATED BY '\n' FROM t1 ORDER BY id;
行为 EXPORT File(1:28~1:73) /test/1/var/lib/mysql-files/split_select_out_a.txt/ -> Table(1:209~1:211) /test/1/catalog1/schema1/t1/
------
SQL  SELECT id, grp FROM t1 ORDER BY id INTO OUTFILE '/var/lib/mysql-files/split_select_out_b.txt' COLUMNS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\' LINES TERMINATED BY '\n';
行为 EXPORT File(1:48~1:93) /test/1/var/lib/mysql-files/split_select_out_b.txt/ -> Table(1:20~1:22) /test/1/catalog1/schema1/t1/
------
SQL  SELECT CONCAT(id, ':', grp) INTO DUMPFILE '/var/lib/mysql-files/split_select_dump.bin' FROM t1 WHERE id = 1;
行为 EXPORT File(1:42~1:86) /test/1/var/lib/mysql-files/split_select_dump.bin/ -> Table(1:92~1:94) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:7~1:13) /test/1/catalog1/schema1/CONCAT/
------
SQL  SELECT a,(SELECT MAX(b) FROM split_subquery_next.out_t) INTO OUTFILE '/var/lib/mysql-files/split_subquery_native_out.txt' FROM split_subquery_next.out_t;
行为 EXPORT File(1:69~1:121) /test/1/var/lib/mysql-files/split_subquery_native_out.txt/ -> Table(1:29~1:54) /test/1/catalog1/split_subquery_next/out_t/
行为 CALL Function(1:17~1:20) /test/1/catalog1/schema1/MAX/
------
SQL  GRANT SELECT, INSERT(note), UPDATE ON split_acl57.t TO 'split_acl_57'@'%';
行为 GRANT Table(1:38~1:51) /test/1/catalog1/split_acl57/t/ -> UserOrRole(1:55~1:73) /test/1/split_acl_57@%/
------
SQL  GRANT EXECUTE ON PROCEDURE split_acl57.p TO 'split_acl_57'@'%';
行为 GRANT Procedure(1:27~1:40) /test/1/catalog1/split_acl57/p/ -> UserOrRole(1:44~1:62) /test/1/split_acl_57@%/
------
SQL  GRANT EXECUTE ON FUNCTION split_acl57.f TO 'split_acl_57'@'%';
行为 GRANT Function(1:26~1:39) /test/1/catalog1/split_acl57/f/ -> UserOrRole(1:43~1:61) /test/1/split_acl_57@%/
------
SQL  GRANT ALL PRIVILEGES ON split_acl57.* TO 'split_acl_57'@'%' WITH GRANT OPTION;
行为 GRANT Schema(1:24~1:35) /test/1/catalog1/split_acl57/ -> UserOrRole(1:41~1:59) /test/1/split_acl_57@%/
------
SQL  GRANT PROXY ON 'root'@'%' TO 'split_acl_57'@'%' WITH GRANT OPTION;
行为 GRANT UserOrRole(1:15~1:25) /test/1/root@%/ -> UserOrRole(1:29~1:47) /test/1/split_acl_57@%/
------
SQL  REVOKE SELECT, INSERT(note), UPDATE ON split_acl57.t FROM 'split_acl_57'@'%';
行为 REVOKE Table(1:39~1:52) /test/1/catalog1/split_acl57/t/ -> UserOrRole(1:58~1:76) /test/1/split_acl_57@%/
------
SQL  REVOKE EXECUTE ON PROCEDURE split_acl57.p FROM 'split_acl_57'@'%';
行为 REVOKE Procedure(1:28~1:41) /test/1/catalog1/split_acl57/p/ -> UserOrRole(1:47~1:65) /test/1/split_acl_57@%/
------
SQL  REVOKE EXECUTE ON FUNCTION split_acl57.f FROM 'split_acl_57'@'%';
行为 REVOKE Function(1:27~1:40) /test/1/catalog1/split_acl57/f/ -> UserOrRole(1:46~1:64) /test/1/split_acl_57@%/
------
SQL  REVOKE PROXY ON 'root'@'%' FROM 'split_acl_57'@'%';
行为 REVOKE UserOrRole(1:16~1:26) /test/1/root@%/ -> UserOrRole(1:32~1:50) /test/1/split_acl_57@%/
------
SQL  GRANT SELECT ON split_acl57.* TO 'sg57a'@'%' IDENTIFIED BY 'Grant57!' REQUIRE SSL WITH GRANT OPTION MAX_QUERIES_PER_HOUR 10 MAX_UPDATES_PER_HOUR 20 MAX_CONNECTIONS_PER_HOUR 30 MAX_USER_CONNECTIONS 4;
行为 GRANT Schema(1:16~1:27) /test/1/catalog1/split_acl57/ -> UserOrRole(1:33~1:44) /test/1/sg57a@%/
------
SQL  GRANT USAGE ON *.* TO 'sg57b'@'%' IDENTIFIED BY PASSWORD '*2470C0C06DEE42FD1618BB99005ADCA2EC9D1E19' REQUIRE X509;
行为 GRANT Instance(1:15~1:18) /test/1/ -> UserOrRole(1:22~1:33) /test/1/sg57b@%/
------
SQL  RENAME USER 'cdra57a'@'localhost' TO 'cdra57c'@'localhost', 'cdra57b'@'localhost' TO 'cdra57d'@'localhost';
行为 RENAME User(1:12~1:33) /test/1/cdra57a@localhost/ -> User(1:37~1:58) /test/1/cdra57c@localhost/
行为 RENAME User(1:60~1:81) /test/1/cdra57b@localhost/ -> User(1:85~1:106) /test/1/cdra57d@localhost/
------
SQL  RENAME USER 'split_life_57_b'@'localhost' TO 'split_life_57_c'@'localhost';
行为 RENAME User(1:12~1:41) /test/1/split_life_57_b@localhost/ -> User(1:45~1:74) /test/1/split_life_57_c@localhost/
------
SQL  GRANT ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* TO 'static_57'@'%';
行为 GRANT Instance(1:346~1:349) /test/1/ -> UserOrRole(1:353~1:368) /test/1/static_57@%/
------
SQL  REVOKE ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* FROM 'static_57'@'%';
行为 REVOKE Instance(1:347~1:350) /test/1/ -> UserOrRole(1:356~1:371) /test/1/static_57@%/
------
SQL  /*!50000 GRANT SELECT ON split_exec_comment.* TO 'split_exec_57'@'%' */;
行为 GRANT Schema(1:25~1:43) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:49~1:68) /test/1/split_exec_57@%/
------
SQL  /*!50000 REVOKE SELECT ON split_exec_comment.* FROM 'split_exec_57'@'%' */;
行为 REVOKE Schema(1:26~1:44) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:52~1:71) /test/1/split_exec_57@%/
------
SQL  CREATE TABLE bit_generated(a INT,b BIT(8) AS (a & 255) STORED,KEY idx_b(b));
行为 CREATE Index(1:66~1:71) /test/1/catalog1/schema1/idx_b/ -> Table(1:13~1:26) /test/1/catalog1/schema1/bit_generated/
------
SQL  CREATE TABLE temporal_generated(source_value VARCHAR(32),generated_datetime DATETIME(6) GENERATED ALWAYS AS(CAST(source_value AS DATETIME(6))) STORED,generated_timestamp TIMESTAMP(6) GENERATED ALWAYS AS(CAST(source_value AS DATETIME(6))) VIRTUAL,KEY idx_generated_datetime(generated_datetime),KEY idx_generated_timestamp(generated_timestamp));
行为 CREATE Index(1:250~1:272) /test/1/catalog1/schema1/idx_generated_datetime/ -> Table(1:13~1:31) /test/1/catalog1/schema1/temporal_generated/
行为 CREATE Index(1:297~1:320) /test/1/catalog1/schema1/idx_generated_timestamp/ -> Table(1:13~1:31) /test/1/catalog1/schema1/temporal_generated/
行为 CALL Function(1:108~1:112) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE split_type_enum_set.es_generated (\n      id INT PRIMARY KEY,\n      e ENUM('low','medium','high'),\n      s SET('read','write','admin'),\n      e_index INT GENERATED ALWAYS AS (e+0) STORED,\n      s_mask INT GENERATED ALWAYS AS (s+0) VIRTUAL,\n      INDEX idx_e_index (e_index),\n      INDEX idx_s_mask (s_mask)\n    );
行为 CREATE Index(7:12~7:23) /test/1/catalog1/schema1/idx_e_index/ -> Table(1:13~1:45) /test/1/catalog1/split_type_enum_set/es_generated/
行为 CREATE Index(8:12~8:22) /test/1/catalog1/schema1/idx_s_mask/ -> Table(1:13~1:45) /test/1/catalog1/split_type_enum_set/es_generated/
行为 CREATE Constraint(2:13~2:24) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_type_json.json_core (\n      id INT PRIMARY KEY,\n      doc JSON NOT NULL,\n      payload JSON,\n      doc_name VARCHAR(64)\n        GENERATED ALWAYS AS (JSON_UNQUOTE(doc->'$.name')) STORED,\n      KEY idx_doc_name (doc_name),\n      CHECK (JSON_VALID(doc))\n    );
行为 CREATE Index(7:10~7:22) /test/1/catalog1/schema1/idx_doc_name/ -> Table(1:13~1:38) /test/1/catalog1/split_type_json/json_core/
行为 CREATE Constraint(2:13~2:24) /test/1/catalog1/schema1/
行为 CALL Function(6:29~6:41) /test/1/catalog1/schema1/JSON_UNQUOTE/
行为 CREATE Constraint(8:6~8:29) /test/1/catalog1/schema1/
行为 CALL Function(8:13~8:23) /test/1/catalog1/schema1/JSON_VALID/
------
SQL  CREATE TABLE split_type_json.json_unique_key (doc JSON, UNIQUE KEY uq_doc(doc));
行为 CREATE Index(1:67~1:73) /test/1/catalog1/schema1/uq_doc/ -> Table(1:13~1:44) /test/1/catalog1/split_type_json/json_unique_key/
行为 CREATE Constraint(1:56~1:78) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_type_json.json_ordinary_key (doc JSON, KEY idx_doc(doc));
行为 CREATE Index(1:62~1:69) /test/1/catalog1/schema1/idx_doc/ -> Table(1:13~1:46) /test/1/catalog1/split_type_json/json_ordinary_key/
------
SQL  CREATE TABLE integer_generated (\n  id INT PRIMARY KEY,\n  tiny_value TINYINT,\n  small_value SMALLINT,\n  medium_value MEDIUMINT,\n  int_value INTEGER,\n  big_value BIGINT,\n  signed_total BIGINT GENERATED ALWAYS AS (\n    tiny_value+small_value+medium_value+int_value+big_value\n  ) STORED,\n  unsigned_mask BIGINT UNSIGNED GENERATED ALWAYS AS (\n    CAST(int_value AS UNSIGNED)\n  ) VIRTUAL,\n  KEY idx_integer_generated (signed_total,unsigned_mask)\n);
行为 CREATE Index(14:6~14:27) /test/1/catalog1/schema1/idx_integer_generated/ -> Table(1:13~1:30) /test/1/catalog1/schema1/integer_generated/
行为 CREATE Constraint(2:9~2:20) /test/1/catalog1/schema1/
行为 CALL Function(12:4~12:8) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE numeric_generated (\n  id INT PRIMARY KEY,\n  decimal_value DECIMAL(20,6),\n  float_value FLOAT,\n  double_value DOUBLE,\n  exact_total DECIMAL(30,10) GENERATED ALWAYS AS (\n    decimal_value+CAST(float_value AS DECIMAL(20,8))\n  ) STORED,\n  approximate_total DOUBLE GENERATED ALWAYS AS (\n    float_value+double_value\n  ) VIRTUAL,\n  KEY idx_numeric_generated (exact_total,approximate_total)\n);
行为 CREATE Index(12:6~12:27) /test/1/catalog1/schema1/idx_numeric_generated/ -> Table(1:13~1:30) /test/1/catalog1/schema1/numeric_generated/
行为 CREATE Constraint(2:9~2:20) /test/1/catalog1/schema1/
行为 CALL Function(7:18~7:22) /test/1/catalog1/schema1/CAST/
------
SQL  ALTER TABLE codex_alter_audit_t ADD INDEX i1(d), RENAME INDEX i1 TO i2, DROP INDEX i2;
行为 RENAME Index(1:62~1:64) /test/1/catalog1/schema1/i1/ -> Index(1:68~1:70) /test/1/catalog1/schema1/i2/
行为 CREATE Index(1:42~1:44) /test/1/catalog1/schema1/i1/ -> Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
行为 DROP Index(1:83~1:85) /test/1/catalog1/schema1/i2/ -> Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE split_idx.t_rename RENAME INDEX idx_old TO idx_new;
行为 RENAME Index(1:44~1:51) /test/1/catalog1/schema1/idx_old/ -> Index(1:55~1:62) /test/1/catalog1/schema1/idx_new/
行为 ALTER Table(1:12~1:30) /test/1/catalog1/split_idx/t_rename/
------
SQL  ALTER TABLE split_idx.t_rename RENAME KEY idx_new TO idx_old;
行为 RENAME Index(1:42~1:49) /test/1/catalog1/schema1/idx_new/ -> Index(1:53~1:60) /test/1/catalog1/schema1/idx_old/
行为 ALTER Table(1:12~1:30) /test/1/catalog1/split_idx/t_rename/
------
SQL  CREATE TABLE t_gcol (\n  a INT,\n  b BLOB,\n  c INT GENERATED ALWAYS AS (1) VIRTUAL,\n  d INT,\n  e INT GENERATED ALWAYS AS (LPAD('111', b, '1')) VIRTUAL,\n  UNIQUE KEY (e),\n  KEY(b(1),a,e),\n  KEY(e,b(1))\n);
行为 CREATE Index(8:2~8:15) /test/1/catalog1/schema1/ -> Table(1:13~1:19) /test/1/catalog1/schema1/t_gcol/
行为 CREATE Index(9:2~9:13) /test/1/catalog1/schema1/ -> Table(1:13~1:19) /test/1/catalog1/schema1/t_gcol/
行为 CALL Function(6:29~6:33) /test/1/catalog1/schema1/LPAD/
行为 CREATE Constraint(7:2~7:16) /test/1/catalog1/schema1/
------
SQL  CREATE VIEW split_type_json.json_view AS\n    SELECT id,doc,doc->>'$.name' AS name_text,\n           JSON_EXTRACT(payload,'$.tags') AS tags\n    FROM split_type_json.json_core;
行为 CREATE View(1:12~1:37) /test/1/catalog1/split_type_json/json_view/ -> Table(4:9~4:34) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(3:11~3:23) /test/1/catalog1/schema1/JSON_EXTRACT/
------
SQL  CREATE TRIGGER split57.trg_order_follows BEFORE INSERT ON split57.trigger_src FOR EACH ROW FOLLOWS trg_order_base SET NEW.note = 'follows';
行为 CREATE Trigger(1:15~1:40) /test/1/catalog1/split57/trg_order_follows/ -> Table(1:58~1:77) /test/1/catalog1/split57/trigger_src/
------
SQL  CREATE TRIGGER split57.trg_order_precedes BEFORE INSERT ON split57.trigger_src FOR EACH ROW PRECEDES trg_order_base SET NEW.note = 'precedes';
行为 CREATE Trigger(1:15~1:41) /test/1/catalog1/split57/trg_order_precedes/ -> Table(1:59~1:78) /test/1/catalog1/split57/trigger_src/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER split57.trg_bi BEFORE INSERT ON split57.trigger_src FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:38~1:52) /test/1/catalog1/split57/trg_bi/ -> Table(1:70~1:89) /test/1/catalog1/split57/trigger_src/
行为 CALL Function(1:124~1:141) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split57.trg_bu BEFORE UPDATE ON split57.trigger_src FOR EACH ROW SET NEW.note = COALESCE(NEW.note, 'before update');
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split57/trg_bu/ -> Table(1:47~1:66) /test/1/catalog1/split57/trigger_src/
行为 CALL Function(1:95~1:103) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER split57.trg_bd BEFORE DELETE ON split57.trigger_src FOR EACH ROW INSERT INTO split57.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'before delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split57/trg_bd/ -> Table(1:47~1:66) /test/1/catalog1/split57/trigger_src/
行为 INSERT Table(1:92~1:113) /test/1/catalog1/split57/trigger_audit/
------
SQL  CREATE TRIGGER split57.trg_ai AFTER INSERT ON split57.trigger_src FOR EACH ROW INSERT INTO split57.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (NEW.id, 'after insert', NULL, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split57/trg_ai/ -> Table(1:46~1:65) /test/1/catalog1/split57/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split57/trigger_audit/
------
SQL  CREATE TRIGGER split57.trg_au AFTER UPDATE ON split57.trigger_src FOR EACH ROW INSERT INTO split57.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after update', OLD.amount, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split57/trg_au/ -> Table(1:46~1:65) /test/1/catalog1/split57/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split57/trigger_audit/
------
SQL  CREATE TRIGGER split57.trg_ad AFTER DELETE ON split57.trigger_src FOR EACH ROW INSERT INTO split57.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split57/trg_ad/ -> Table(1:46~1:65) /test/1/catalog1/split57/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split57/trigger_audit/
------
SQL  CREATE TRIGGER trg_follow_gap BEFORE INSERT ON t1 FOR EACH ROW FOLLOWS trg_anchor INSERT INTO t2 VALUES (NEW.a);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/schema1/trg_follow_gap/ -> Table(1:47~1:49) /test/1/catalog1/schema1/t1/
行为 INSERT Table(1:94~1:96) /test/1/catalog1/schema1/t2/
------
SQL  CREATE TRIGGER trg_precede_gap BEFORE INSERT ON t1 FOR EACH ROW PRECEDES trg_anchor BEGIN END;
行为 CREATE Trigger(1:15~1:30) /test/1/catalog1/schema1/trg_precede_gap/ -> Table(1:48~1:50) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE json_gc_idx (f1 JSON, gc VARCHAR(20) CHARACTER SET utf8mb4 AS (JSON_UNQUOTE(JSON_EXTRACT(f1,'$'))) STORED, KEY gc_idx(gc));
行为 CREATE Index(1:124~1:130) /test/1/catalog1/schema1/gc_idx/ -> Table(1:13~1:24) /test/1/catalog1/schema1/json_gc_idx/
行为 CALL Function(1:76~1:88) /test/1/catalog1/schema1/JSON_UNQUOTE/
行为 CALL Function(1:89~1:101) /test/1/catalog1/schema1/JSON_EXTRACT/
------
SQL  ALTER TABLE codex_alter_audit_t ALGORITHM=INPLACE, RENAME KEY kkkk TO k, ALTER COLUMN i SET DEFAULT 100;
行为 RENAME Index(1:62~1:66) /test/1/catalog1/schema1/kkkk/ -> Index(1:70~1:71) /test/1/catalog1/schema1/k/
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t DROP KEY c, RENAME KEY d TO c;
行为 RENAME Index(1:55~1:56) /test/1/catalog1/schema1/d/ -> Index(1:60~1:61) /test/1/catalog1/schema1/c/
行为 DROP Index(1:41~1:42) /test/1/catalog1/schema1/c/ -> Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t RENAME KEY k TO kk, RENAME TO codex_alter_audit_u;
行为 RENAME Index(1:43~1:44) /test/1/catalog1/schema1/k/ -> Index(1:48~1:50) /test/1/catalog1/schema1/kk/
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  CREATE OR REPLACE ALGORITHM = MERGE DEFINER = CURRENT_USER SQL SECURITY DEFINER VIEW split_view57.v_base (id, amount) AS\nSELECT id, amount FROM split_view57.src WHERE amount >= 0 WITH CASCADED CHECK OPTION;
行为 REPLACE View(1:85~1:104) /test/1/catalog1/split_view57/v_base/ -> Table(2:23~2:39) /test/1/catalog1/split_view57/src/
------
SQL  ALTER ALGORITHM = UNDEFINED DEFINER = CURRENT_USER SQL SECURITY INVOKER VIEW split_view57.v_base (id, amount) AS\nSELECT id, amount FROM split_view57.src WHERE amount BETWEEN 0 AND 100 WITH LOCAL CHECK OPTION;
行为 ALTER View(1:77~1:96) /test/1/catalog1/split_view57/v_base/ -> Table(2:23~2:39) /test/1/catalog1/split_view57/src/
------
SQL  CREATE ALGORITHM = TEMPTABLE VIEW split_view57.v_joined AS\nSELECT s.id, a.action_name FROM split_view57.src AS s LEFT JOIN split_view57.audit AS a ON a.src_id = s.id;
行为 CREATE View(1:34~1:55) /test/1/catalog1/split_view57/v_joined/ -> [Table(2:32~2:48) /test/1/catalog1/split_view57/src/ ; Table(2:64~2:82) /test/1/catalog1/split_view57/audit/]
------
SQL  DELETE FROM split_type_json.json_core\n    WHERE JSON_CONTAINS_PATH(doc,'one','$.obsolete')\n       OR EXISTS (\n            SELECT 1\n            FROM split_type_json.json_core AS inner_json\n            WHERE inner_json.id<>json_core.id\n              AND inner_json.doc->>'$.name'=json_core.doc->>'$.name'\n          );
行为 DELETE Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/ -> Table(5:17~5:42) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(2:10~2:28) /test/1/catalog1/schema1/JSON_CONTAINS_PATH/
------
SQL  INSERT INTO split_type_json.json_core(id,doc,payload,meta)\n    SELECT id+10,JSON_MERGE_PATCH(doc,'{"copied":true}'),payload,meta\n    FROM split_type_json.json_core\n    WHERE JSON_EXTRACT(doc,'$.score') >= 20;
行为 INSERT Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/ -> Table(3:9~3:34) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(2:17~2:33) /test/1/catalog1/schema1/JSON_MERGE_PATCH/
行为 CALL Function(4:10~4:22) /test/1/catalog1/schema1/JSON_EXTRACT/
------
SQL  LOAD DATA LOCAL INFILE '/tmp/nonexistent-json.csv'\n    INTO TABLE split_type_json.json_core\n    FIELDS TERMINATED BY ','\n    (id,@doc,@payload)\n    SET doc=CAST(@doc AS JSON),payload=CAST(@payload AS JSON);
行为 IMPORT Table(2:15~2:40) /test/1/catalog1/split_type_json/json_core/ -> File(1:23~1:50) /test/1/tmp/nonexistent-json.csv/
行为 READ ConfigKey(4:8~4:12) /test/1/doc/
行为 READ ConfigKey(4:13~4:21) /test/1/payload/
行为 CALL Function(5:12~5:16) /test/1/catalog1/schema1/CAST/
------
SQL  SELECT * FROM sales.orders INTO OUTFILE '/tmp/orders.csv';
行为 EXPORT File(1:40~1:57) /test/1/tmp/orders.csv/ -> Table(1:14~1:26) /test/1/catalog1/sales/orders/
------
SQL  GRANT PUBLIC TO u1@localhost;
行为 GRANT Role(1:6~1:12) /test/1/PUBLIC/ -> UserOrRole(1:16~1:28) /test/1/u1@localhost/
------
SQL  SET DEFAULT ROLE PUBLIC TO u1@localhost;
行为 ALTER User(1:27~1:39) /test/1/u1@localhost/ -> Role(1:17~1:23) /test/1/PUBLIC/
------
SQL  REVOKE PUBLIC FROM u1@localhost;
行为 REVOKE Role(1:7~1:13) /test/1/PUBLIC/ -> UserOrRole(1:19~1:31) /test/1/u1@localhost/
------
SQL  GRANT SELECT, INSERT(note), UPDATE ON split_acl80.t TO 'split_acl_80'@'%';
行为 GRANT Table(1:38~1:51) /test/1/catalog1/split_acl80/t/ -> UserOrRole(1:55~1:73) /test/1/split_acl_80@%/
------
SQL  GRANT EXECUTE ON PROCEDURE split_acl80.p TO 'split_acl_80'@'%';
行为 GRANT Procedure(1:27~1:40) /test/1/catalog1/split_acl80/p/ -> UserOrRole(1:44~1:62) /test/1/split_acl_80@%/
------
SQL  GRANT EXECUTE ON FUNCTION split_acl80.f TO 'split_acl_80'@'%';
行为 GRANT Function(1:26~1:39) /test/1/catalog1/split_acl80/f/ -> UserOrRole(1:43~1:61) /test/1/split_acl_80@%/
------
SQL  GRANT ALL PRIVILEGES ON split_acl80.* TO 'split_acl_80'@'%' WITH GRANT OPTION;
行为 GRANT Schema(1:24~1:35) /test/1/catalog1/split_acl80/ -> UserOrRole(1:41~1:59) /test/1/split_acl_80@%/
------
SQL  GRANT PROXY ON 'root'@'%' TO 'split_acl_80'@'%' WITH GRANT OPTION;
行为 GRANT UserOrRole(1:15~1:25) /test/1/root@%/ -> UserOrRole(1:29~1:47) /test/1/split_acl_80@%/
------
SQL  REVOKE SELECT, INSERT(note), UPDATE ON split_acl80.t FROM 'split_acl_80'@'%';
行为 REVOKE Table(1:39~1:52) /test/1/catalog1/split_acl80/t/ -> UserOrRole(1:58~1:76) /test/1/split_acl_80@%/
------
SQL  REVOKE EXECUTE ON PROCEDURE split_acl80.p FROM 'split_acl_80'@'%';
行为 REVOKE Procedure(1:28~1:41) /test/1/catalog1/split_acl80/p/ -> UserOrRole(1:47~1:65) /test/1/split_acl_80@%/
------
SQL  REVOKE EXECUTE ON FUNCTION split_acl80.f FROM 'split_acl_80'@'%';
行为 REVOKE Function(1:27~1:40) /test/1/catalog1/split_acl80/f/ -> UserOrRole(1:46~1:64) /test/1/split_acl_80@%/
------
SQL  REVOKE PROXY ON 'root'@'%' FROM 'split_acl_80'@'%';
行为 REVOKE UserOrRole(1:16~1:26) /test/1/root@%/ -> UserOrRole(1:32~1:50) /test/1/split_acl_80@%/
------
SQL  ALTER USER 'split_role_alter'@'%' DEFAULT ROLE 'split_role_alt_a', 'split_role_alt_b';
行为 ALTER User(1:11~1:33) /test/1/split_role_alter@%/ -> [Role(1:47~1:65) /test/1/split_role_alt_a/ ; Role(1:67~1:85) /test/1/split_role_alt_b/]
------
SQL  GRANT SELECT ON *.* TO 'split_grant_to'@'%' AS CURRENT_USER;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:43) /test/1/split_grant_to@%/
------
SQL  GRANT INSERT ON *.* TO 'split_grant_to'@'%' AS CURRENT_USER() WITH ROLE NONE;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:43) /test/1/split_grant_to@%/
------
SQL  GRANT UPDATE ON *.* TO 'split_grant_to'@'%' AS 'root'@'localhost' WITH ROLE 'split_grant_role';
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:43) /test/1/split_grant_to@%/
------
SQL  GRANT SELECT ON *.* TO 'cda11a'@'%' AS CURRENT_USER WITH ROLE ALL;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:35) /test/1/cda11a@%/
------
SQL  GRANT SELECT ON *.* TO 'cda12a'@'%' AS CURRENT_USER WITH ROLE ALL EXCEPT 'cda12r'@'%';
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:35) /test/1/cda12a@%/
------
SQL  GRANT SELECT ON *.* TO 'cda13a'@'%' AS CURRENT_USER WITH ROLE DEFAULT;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:35) /test/1/cda13a@%/
------
SQL  RENAME USER 'cdra80a'@'localhost' TO 'cdra80c'@'localhost', 'cdra80b'@'localhost' TO 'cdra80d'@'localhost';
行为 RENAME User(1:12~1:33) /test/1/cdra80a@localhost/ -> User(1:37~1:58) /test/1/cdra80c@localhost/
行为 RENAME User(1:60~1:81) /test/1/cdra80b@localhost/ -> User(1:85~1:106) /test/1/cdra80d@localhost/
------
SQL  REVOKE IF EXISTS DELETE ON *.* FROM 'split_revoke_to'@'%' IGNORE UNKNOWN USER;
行为 REVOKE Instance(1:27~1:30) /test/1/ -> UserOrRole(1:36~1:57) /test/1/split_revoke_to@%/
------
SQL  REVOKE IF EXISTS SELECT ON *.* FROM 'missing_revoke_user'@'%' IGNORE UNKNOWN USER;
行为 REVOKE Instance(1:27~1:30) /test/1/ -> UserOrRole(1:36~1:61) /test/1/missing_revoke_user@%/
------
SQL  REVOKE IF EXISTS 'split_revoke_role' FROM 'split_revoke_to'@'%' IGNORE UNKNOWN USER;
行为 REVOKE Role(1:17~1:36) /test/1/split_revoke_role/ -> UserOrRole(1:42~1:63) /test/1/split_revoke_to@%/
------
SQL  SET DEFAULT ROLE 'split_role_a', 'split_role_b' TO 'root'@'%';
行为 ALTER User(1:51~1:61) /test/1/root@%/ -> [Role(1:17~1:31) /test/1/split_role_a/ ; Role(1:33~1:47) /test/1/split_role_b/]
------
SQL  GRANT 'cda07r'@'%', 'cda07s'@'%' TO 'cda07a'@'%', 'cda07b'@'%';
行为 GRANT Role(1:6~1:18) /test/1/cda07r@%/ -> [UserOrRole(1:36~1:48) /test/1/cda07a@%/ ; UserOrRole(1:50~1:62) /test/1/cda07b@%/]
行为 GRANT Role(1:20~1:32) /test/1/cda07s@%/ -> [UserOrRole(1:36~1:48) /test/1/cda07a@%/ ; UserOrRole(1:50~1:62) /test/1/cda07b@%/]
------
SQL  GRANT 'split_role_80_a','split_role_80_b' TO 'split_role_u80'@'%' WITH ADMIN OPTION;
行为 GRANT Role(1:6~1:23) /test/1/split_role_80_a/ -> UserOrRole(1:45~1:65) /test/1/split_role_u80@%/
行为 GRANT Role(1:24~1:41) /test/1/split_role_80_b/ -> UserOrRole(1:45~1:65) /test/1/split_role_u80@%/
------
SQL  SET DEFAULT ROLE 'split_role_80_a','split_role_80_b' TO 'split_role_u80'@'%';
行为 ALTER User(1:56~1:76) /test/1/split_role_u80@%/ -> [Role(1:17~1:34) /test/1/split_role_80_a/ ; Role(1:35~1:52) /test/1/split_role_80_b/]
------
SQL  ALTER USER 'split_role_u80'@'%' DEFAULT ROLE 'split_role_80_a','split_role_80_b';
行为 ALTER User(1:11~1:31) /test/1/split_role_u80@%/ -> [Role(1:45~1:62) /test/1/split_role_80_a/ ; Role(1:63~1:80) /test/1/split_role_80_b/]
------
SQL  REVOKE 'split_role_80_a','split_role_80_b' FROM 'split_role_u80'@'%';
行为 REVOKE Role(1:7~1:24) /test/1/split_role_80_a/ -> UserOrRole(1:48~1:68) /test/1/split_role_u80@%/
行为 REVOKE Role(1:25~1:42) /test/1/split_role_80_b/ -> UserOrRole(1:48~1:68) /test/1/split_role_u80@%/
------
SQL  REVOKE 'cda08r'@'%', 'cda08s'@'%' FROM 'cda08a'@'%', 'cda08b'@'%';
行为 REVOKE Role(1:7~1:19) /test/1/cda08r@%/ -> [UserOrRole(1:39~1:51) /test/1/cda08a@%/ ; UserOrRole(1:53~1:65) /test/1/cda08b@%/]
行为 REVOKE Role(1:21~1:33) /test/1/cda08s@%/ -> [UserOrRole(1:39~1:51) /test/1/cda08a@%/ ; UserOrRole(1:53~1:65) /test/1/cda08b@%/]
------
SQL  SET DEFAULT ROLE 'cda09r'@'%', 'cda09s'@'%' TO 'cda09a'@'%', 'cda09b'@'%';
行为 ALTER User(1:47~1:59) /test/1/cda09a@%/ -> [Role(1:17~1:29) /test/1/cda09r@%/ ; Role(1:31~1:43) /test/1/cda09s@%/]
行为 ALTER User(1:61~1:73) /test/1/cda09b@%/ -> [Role(1:17~1:29) /test/1/cda09r@%/ ; Role(1:31~1:43) /test/1/cda09s@%/]
------
SQL  GRANT SELECT ON mydatabase.* TO 'test_user'@'localhost';
行为 GRANT Schema(1:16~1:26) /test/1/catalog1/mydatabase/ -> UserOrRole(1:32~1:55) /test/1/test_user@localhost/
------
SQL  GRANT ALL PRIVILEGES ON mydatabase.* TO 'test_user'@'localhost';
行为 GRANT Schema(1:24~1:34) /test/1/catalog1/mydatabase/ -> UserOrRole(1:40~1:63) /test/1/test_user@localhost/
------
SQL  GRANT SELECT ON mydatabase.* TO 'test_user'@'localhost' WITH GRANT OPTION;
行为 GRANT Schema(1:16~1:26) /test/1/catalog1/mydatabase/ -> UserOrRole(1:32~1:55) /test/1/test_user@localhost/
------
SQL  REVOKE ALL PRIVILEGES ON mydatabase.* FROM 'test_user'@'localhost';
行为 REVOKE Schema(1:25~1:35) /test/1/catalog1/mydatabase/ -> UserOrRole(1:43~1:66) /test/1/test_user@localhost/
------
SQL  GRANT CONNECTION_ADMIN, SYSTEM_VARIABLES_ADMIN, SELECT ON *.* TO u1@localhost;
行为 GRANT Instance(1:58~1:61) /test/1/ -> UserOrRole(1:65~1:77) /test/1/u1@localhost/
------
SQL  GRANT 'role', engineering TO CURRENT_USER();
行为 GRANT Role(1:6~1:12) /test/1/role/ -> UserOrRole(1:29~1:41) /test/1/
行为 GRANT Role(1:14~1:25) /test/1/engineering/ -> UserOrRole(1:29~1:41) /test/1/
------
SQL  GRANT SELECT, INSERT ON *.* TO 'codex_gap7_user'@'%' AS CURRENT_USER();
行为 GRANT Instance(1:24~1:27) /test/1/ -> UserOrRole(1:31~1:52) /test/1/codex_gap7_user@%/
------
SQL  GRANT SELECT ON *.* TO 'codex_gap7_user'@'%' AS CURRENT_USER() WITH ROLE ALL;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:44) /test/1/codex_gap7_user@%/
------
SQL  GRANT SELECT ON *.* TO 'codex_gap7_user'@'%' AS CURRENT_USER() WITH ROLE DEFAULT;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:44) /test/1/codex_gap7_user@%/
------
SQL  GRANT SELECT ON *.* TO 'codex_gap7_user'@'%' AS CURRENT_USER() WITH ROLE 'codex_gap7_role'@'%';
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:44) /test/1/codex_gap7_user@%/
------
SQL  GRANT SELECT ON *.* TO 'split_grant_as'@'localhost' WITH GRANT OPTION AS CURRENT_USER() WITH ROLE NONE;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:51) /test/1/split_grant_as@localhost/
------
SQL  GRANT SELECT, GRANT OPTION ON *.* TO 'split_grant_as'@'localhost' AS CURRENT_USER() WITH ROLE NONE;
行为 GRANT Instance(1:30~1:33) /test/1/ -> UserOrRole(1:37~1:65) /test/1/split_grant_as@localhost/
------
SQL  REVOKE SELECT ON *.* FROM ca_miss IGNORE UNKNOWN USER;
行为 REVOKE Instance(1:17~1:20) /test/1/ -> UserOrRole(1:26~1:33) /test/1/ca_miss/
------
SQL  REVOKE INSERT(i) ON ca_db.t1 FROM ca_sub,ca_res IGNORE UNKNOWN USER;
行为 REVOKE Table(1:20~1:28) /test/1/catalog1/ca_db/t1/ -> [UserOrRole(1:34~1:40) /test/1/ca_sub/ ; UserOrRole(1:41~1:47) /test/1/ca_res/]
------
SQL  REVOKE TABLE_ENCRYPTION_ADMIN ON *.* FROM ca_miss,ca_res IGNORE UNKNOWN USER;
行为 REVOKE Instance(1:33~1:36) /test/1/ -> [UserOrRole(1:42~1:49) /test/1/ca_miss/ ; UserOrRole(1:50~1:56) /test/1/ca_res/]
------
SQL  REVOKE IF EXISTS ENCRYPTION_KEY_ADMIN, APPLICATION_PASSWORD_ADMIN ON *.* FROM ca_sub;
行为 REVOKE Instance(1:69~1:72) /test/1/ -> UserOrRole(1:78~1:84) /test/1/ca_sub/
------
SQL  REVOKE IF EXISTS UPDATE, SELECT ON *.* FROM ca_sub,ca_res,ca_miss;
行为 REVOKE Instance(1:35~1:38) /test/1/ -> [UserOrRole(1:44~1:50) /test/1/ca_sub/ ; UserOrRole(1:51~1:57) /test/1/ca_res/ ; UserOrRole(1:58~1:65) /test/1/ca_miss/]
------
SQL  REVOKE IF EXISTS ca_r1 FROM ca_sub,ca_res,ca_third,ca_r2;
行为 REVOKE Role(1:17~1:22) /test/1/ca_r1/ -> [UserOrRole(1:28~1:34) /test/1/ca_sub/ ; UserOrRole(1:35~1:41) /test/1/ca_res/ ; UserOrRole(1:42~1:50) /test/1/ca_third/ ; UserOrRole(1:51~1:56) /test/1/ca_r2/]
------
SQL  GRANT ca_qa TO ca_consult;
行为 GRANT Role(1:6~1:11) /test/1/ca_qa/ -> UserOrRole(1:15~1:25) /test/1/ca_consult/
------
SQL  GRANT 'ca_eng'@'US' TO 'ca_eng'@'INDIA';
行为 GRANT Role(1:6~1:19) /test/1/ca_eng@US/ -> UserOrRole(1:23~1:39) /test/1/ca_eng@INDIA/
------
SQL  GRANT SELECT ON split_native_gap.* TO split_gap_foo AS split_gap_bar;
行为 GRANT Schema(1:16~1:32) /test/1/catalog1/split_native_gap/ -> UserOrRole(1:38~1:51) /test/1/split_gap_foo/
------
SQL  GRANT SELECT ON split_native_gap.t1 TO split_gap_foo AS split_gap_bar;
行为 GRANT Table(1:16~1:35) /test/1/catalog1/split_native_gap/t1/ -> UserOrRole(1:39~1:52) /test/1/split_gap_foo/
------
SQL  GRANT SELECT(c1) ON split_native_gap.t1 TO split_gap_foo AS split_gap_bar;
行为 GRANT Table(1:20~1:39) /test/1/catalog1/split_native_gap/t1/ -> UserOrRole(1:43~1:56) /test/1/split_gap_foo/
------
SQL  GRANT SELECT ON *.* TO split_gap_foo AS split_gap_bar WITH ROLE split_gap_baz, split_gap_missing;
行为 GRANT Instance(1:16~1:19) /test/1/ -> UserOrRole(1:23~1:36) /test/1/split_gap_foo/
------
SQL  rename user 'old_user'@'localhost' to 'new_user'@'localhost';
行为 RENAME User(1:12~1:34) /test/1/old_user@localhost/ -> User(1:38~1:60) /test/1/new_user@localhost/
------
SQL  grant select on test.* to 'test_user'@'localhost';
行为 GRANT Schema(1:16~1:20) /test/1/catalog1/test/ -> UserOrRole(1:26~1:49) /test/1/test_user@localhost/
------
SQL  grant all privileges on *.* to 'test_user'@'localhost';
行为 GRANT Instance(1:24~1:27) /test/1/ -> UserOrRole(1:31~1:54) /test/1/test_user@localhost/
------
SQL  grant select, insert, update on test.abc to 'test_user'@'localhost';
行为 GRANT Table(1:32~1:40) /test/1/catalog1/test/abc/ -> UserOrRole(1:44~1:67) /test/1/test_user@localhost/
------
SQL  revoke select on test.* from 'test_user'@'localhost';
行为 REVOKE Schema(1:17~1:21) /test/1/catalog1/test/ -> UserOrRole(1:29~1:52) /test/1/test_user@localhost/
------
SQL  revoke all privileges on *.* from 'test_user'@'localhost';
行为 REVOKE Instance(1:25~1:28) /test/1/ -> UserOrRole(1:34~1:57) /test/1/test_user@localhost/
------
SQL  GRANT APPLICATION_PASSWORD_ADMIN, AUDIT_ABORT_EXEMPT, AUDIT_ADMIN, AUTHENTICATION_POLICY_ADMIN, BACKUP_ADMIN, BINLOG_ADMIN, BINLOG_ENCRYPTION_ADMIN, CLONE_ADMIN, CONNECTION_ADMIN, ENCRYPTION_KEY_ADMIN, FIREWALL_EXEMPT, FLUSH_OPTIMIZER_COSTS, FLUSH_STATUS, FLUSH_TABLES, FLUSH_USER_RESOURCES, GROUP_REPLICATION_ADMIN, GROUP_REPLICATION_STREAM, INNODB_REDO_LOG_ARCHIVE, INNODB_REDO_LOG_ENABLE, PASSWORDLESS_USER_ADMIN, PERSIST_RO_VARIABLES_ADMIN, REPLICATION_APPLIER, REPLICATION_SLAVE_ADMIN, RESOURCE_GROUP_ADMIN, RESOURCE_GROUP_USER, ROLE_ADMIN, SENSITIVE_VARIABLES_OBSERVER, SERVICE_CONNECTION_ADMIN, SESSION_VARIABLES_ADMIN, SET_USER_ID, SHOW_ROUTINE, SYSTEM_USER, SYSTEM_VARIABLES_ADMIN, TABLE_ENCRYPTION_ADMIN, TELEMETRY_LOG_ADMIN, XA_RECOVER_ADMIN ON *.* TO 'dyn_all_80'@'%';
行为 GRANT Instance(1:756~1:759) /test/1/ -> UserOrRole(1:763~1:779) /test/1/dyn_all_80@%/
------
SQL  REVOKE APPLICATION_PASSWORD_ADMIN, AUDIT_ABORT_EXEMPT, AUDIT_ADMIN, AUTHENTICATION_POLICY_ADMIN, BACKUP_ADMIN, BINLOG_ADMIN, BINLOG_ENCRYPTION_ADMIN, CLONE_ADMIN, CONNECTION_ADMIN, ENCRYPTION_KEY_ADMIN, FIREWALL_EXEMPT, FLUSH_OPTIMIZER_COSTS, FLUSH_STATUS, FLUSH_TABLES, FLUSH_USER_RESOURCES, GROUP_REPLICATION_ADMIN, GROUP_REPLICATION_STREAM, INNODB_REDO_LOG_ARCHIVE, INNODB_REDO_LOG_ENABLE, PASSWORDLESS_USER_ADMIN, PERSIST_RO_VARIABLES_ADMIN, REPLICATION_APPLIER, REPLICATION_SLAVE_ADMIN, RESOURCE_GROUP_ADMIN, RESOURCE_GROUP_USER, ROLE_ADMIN, SENSITIVE_VARIABLES_OBSERVER, SERVICE_CONNECTION_ADMIN, SESSION_VARIABLES_ADMIN, SET_USER_ID, SHOW_ROUTINE, SYSTEM_USER, SYSTEM_VARIABLES_ADMIN, TABLE_ENCRYPTION_ADMIN, TELEMETRY_LOG_ADMIN, XA_RECOVER_ADMIN ON *.* FROM 'dyn_all_80'@'%';
行为 REVOKE Instance(1:757~1:760) /test/1/ -> UserOrRole(1:766~1:782) /test/1/dyn_all_80@%/
------
SQL  GRANT ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* TO 'static_80'@'%';
行为 GRANT Instance(1:346~1:349) /test/1/ -> UserOrRole(1:353~1:368) /test/1/static_80@%/
------
SQL  REVOKE ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* FROM 'static_80'@'%';
行为 REVOKE Instance(1:347~1:350) /test/1/ -> UserOrRole(1:356~1:371) /test/1/static_80@%/
------
SQL  GRANT FIREWALL_ADMIN, FIREWALL_USER, MASKING_DICTIONARIES_ADMIN, NDB_STORED_USER, SKIP_QUERY_REWRITE, TP_CONNECTION_ADMIN, VERSION_TOKEN_ADMIN ON *.* TO 'env_acl_80'@'%';
行为 GRANT Instance(1:146~1:149) /test/1/ -> UserOrRole(1:153~1:169) /test/1/env_acl_80@%/
------
SQL  REVOKE FIREWALL_ADMIN, FIREWALL_USER, MASKING_DICTIONARIES_ADMIN, NDB_STORED_USER, SKIP_QUERY_REWRITE, TP_CONNECTION_ADMIN, VERSION_TOKEN_ADMIN ON *.* FROM 'env_acl_80'@'%';
行为 REVOKE Instance(1:147~1:150) /test/1/ -> UserOrRole(1:156~1:172) /test/1/env_acl_80@%/
------
SQL  /*!50000 GRANT SELECT ON split_exec_comment.* TO 'split_exec_80'@'%' */;
行为 GRANT Schema(1:25~1:43) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:49~1:68) /test/1/split_exec_80@%/
------
SQL  /*!50000 REVOKE SELECT ON split_exec_comment.* FROM 'split_exec_80'@'%' */;
行为 REVOKE Schema(1:26~1:44) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:52~1:71) /test/1/split_exec_80@%/
------
SQL  CREATE TABLE type_srid (c_point POINT NOT NULL SRID 4326, SPATIAL INDEX (c_point));
行为 CREATE Index(1:58~1:81) /test/1/catalog1/schema1/ -> Table(1:13~1:22) /test/1/catalog1/schema1/type_srid/
------
SQL  CREATE TABLE spatial_modern (\n  id INT PRIMARY KEY,\n  p POINT NOT NULL SRID 4326,\n  g GEOMETRY SRID 0,\n  SPATIAL INDEX sx_p (p)\n);
行为 CREATE Index(5:16~5:20) /test/1/catalog1/schema1/sx_p/ -> Table(1:13~1:27) /test/1/catalog1/schema1/spatial_modern/
行为 CREATE Constraint(2:9~2:20) /test/1/catalog1/schema1/
------
SQL  CREATE INDEX idx_name ON test (name);
行为 CREATE Index(1:13~1:21) /test/1/catalog1/schema1/idx_name/ -> Table(1:25~1:29) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_multi ON test (c1, c2 ASC);
行为 CREATE Index(1:13~1:22) /test/1/catalog1/schema1/idx_multi/ -> Table(1:26~1:30) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_desc ON test (c1 DESC, c2 ASC);
行为 CREATE Index(1:13~1:21) /test/1/catalog1/schema1/idx_desc/ -> Table(1:25~1:29) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_prefix ON test (name(10));
行为 CREATE Index(1:13~1:23) /test/1/catalog1/schema1/idx_prefix/ -> Table(1:27~1:31) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_multi_prefix ON test (c1(10), c2(20));
行为 CREATE Index(1:13~1:29) /test/1/catalog1/schema1/idx_multi_prefix/ -> Table(1:33~1:37) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_comment ON test (c1, c2 ASC) COMMENT 'test index';
行为 CREATE Index(1:13~1:24) /test/1/catalog1/schema1/idx_comment/ -> Table(1:28~1:32) /test/1/catalog1/schema1/test/
------
SQL  CREATE UNIQUE INDEX idx_uniq ON test (email);
行为 CREATE Index(1:20~1:28) /test/1/catalog1/schema1/idx_uniq/ -> Table(1:32~1:36) /test/1/catalog1/schema1/test/
------
SQL  CREATE UNIQUE INDEX idx_uniq_multi ON test (c1, c2);
行为 CREATE Index(1:20~1:34) /test/1/catalog1/schema1/idx_uniq_multi/ -> Table(1:38~1:42) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_btree ON test (id) USING BTREE;
行为 CREATE Index(1:13~1:22) /test/1/catalog1/schema1/idx_btree/ -> Table(1:26~1:30) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_hash ON test (id) USING HASH;
行为 CREATE Index(1:13~1:21) /test/1/catalog1/schema1/idx_hash/ -> Table(1:25~1:29) /test/1/catalog1/schema1/test/
------
SQL  CREATE FULLTEXT INDEX idx_ft ON test (content);
行为 CREATE Index(1:22~1:28) /test/1/catalog1/schema1/idx_ft/ -> Table(1:32~1:36) /test/1/catalog1/schema1/test/
------
SQL  CREATE SPATIAL INDEX idx_sp ON test (geo);
行为 CREATE Index(1:21~1:27) /test/1/catalog1/schema1/idx_sp/ -> Table(1:31~1:35) /test/1/catalog1/schema1/test/
------
SQL  CREATE INDEX idx_schema ON `test_schema`.`test_table` (c1);
行为 CREATE Index(1:13~1:23) /test/1/catalog1/schema1/idx_schema/ -> Table(1:27~1:53) /test/1/catalog1/test_schema/test_table/
------
SQL  CREATE INDEX `idx_backtick` ON `test_schema`.`test_table` (`c1`, `c2`);
行为 CREATE Index(1:13~1:27) /test/1/catalog1/schema1/idx_backtick/ -> Table(1:31~1:57) /test/1/catalog1/test_schema/test_table/
------
SQL  DROP INDEX idx_test ON test;
行为 DROP Index(1:11~1:19) /test/1/catalog1/schema1/idx_test/ -> Table(1:23~1:27) /test/1/catalog1/schema1/test/
------
SQL  DROP INDEX `idx_test` ON `test_schema`.test;
行为 DROP Index(1:11~1:21) /test/1/catalog1/schema1/idx_test/ -> Table(1:25~1:43) /test/1/catalog1/test_schema/test/
------
SQL  DROP INDEX idx_test ON `test_schema`.`test_table`;
行为 DROP Index(1:11~1:19) /test/1/catalog1/schema1/idx_test/ -> Table(1:23~1:49) /test/1/catalog1/test_schema/test_table/
------
SQL  ALTER TABLE test ADD INDEX idx_c1 (c1);
行为 CREATE Index(1:27~1:33) /test/1/catalog1/schema1/idx_c1/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD INDEX (c1, c2);
行为 CREATE Index(1:17~1:35) /test/1/catalog1/schema1/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD INDEX idx_prefix (c1(33));
行为 CREATE Index(1:27~1:37) /test/1/catalog1/schema1/idx_prefix/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD KEY idx_key (c1);
行为 CREATE Index(1:25~1:32) /test/1/catalog1/schema1/idx_key/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD KEY www (c1(33),c2(33)) COMMENT 'sss';
行为 CREATE Index(1:25~1:28) /test/1/catalog1/schema1/www/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD UNIQUE INDEX idx_uniq (email);
行为 CREATE Index(1:34~1:42) /test/1/catalog1/schema1/idx_uniq/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD UNIQUE KEY idx_uk (c1, c2);
行为 CREATE Index(1:32~1:38) /test/1/catalog1/schema1/idx_uk/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD UNIQUE (c1);
行为 CREATE Index(1:17~1:32) /test/1/catalog1/schema1/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD FULLTEXT INDEX idx_ft (content);
行为 CREATE Index(1:36~1:42) /test/1/catalog1/schema1/idx_ft/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ADD SPATIAL INDEX idx_sp (geo);
行为 CREATE Index(1:35~1:41) /test/1/catalog1/schema1/idx_sp/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE `test_schema`.`test_table` ADD INDEX idx_c1 (c1);
行为 CREATE Index(1:49~1:55) /test/1/catalog1/schema1/idx_c1/ -> Table(1:12~1:38) /test/1/catalog1/test_schema/test_table/
------
SQL  ALTER TABLE test DROP INDEX idx_c1;
行为 DROP Index(1:28~1:34) /test/1/catalog1/schema1/idx_c1/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test DROP KEY idx_key;
行为 DROP Index(1:26~1:33) /test/1/catalog1/schema1/idx_key/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE `test_schema`.`test_table` DROP INDEX `idx_c1`;
行为 DROP Index(1:50~1:58) /test/1/catalog1/schema1/idx_c1/ -> Table(1:12~1:38) /test/1/catalog1/test_schema/test_table/
------
SQL  ALTER TABLE test RENAME INDEX old_idx TO new_idx;
行为 RENAME Index(1:30~1:37) /test/1/catalog1/schema1/old_idx/ -> Index(1:41~1:48) /test/1/catalog1/schema1/new_idx/
行为 ALTER Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test RENAME KEY old_key TO new_key;
行为 RENAME Index(1:28~1:35) /test/1/catalog1/schema1/old_key/ -> Index(1:39~1:46) /test/1/catalog1/schema1/new_key/
行为 ALTER Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ALTER INDEX idx_c1 VISIBLE;
行为 ALTER Index(1:29~1:35) /test/1/catalog1/schema1/idx_c1/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  ALTER TABLE test ALTER INDEX idx_c1 INVISIBLE;
行为 ALTER Index(1:29~1:35) /test/1/catalog1/schema1/idx_c1/ -> Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  CREATE TABLE codex_constraint_c07 (id INT, name VARCHAR(64), UNIQUE INDEX uq_c07 ((LOWER(name))));
行为 CREATE Index(1:74~1:80) /test/1/catalog1/schema1/uq_c07/ -> Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c07/
行为 CREATE Constraint(1:61~1:96) /test/1/catalog1/schema1/
行为 CALL Function(1:83~1:88) /test/1/catalog1/schema1/LOWER/
------
SQL  ALTER TABLE codex_c13_22.t ALTER my_row_id SET INVISIBLE, ADD INDEX (f1), ALGORITHM=INPLACE;
行为 CREATE Index(1:58~1:72) /test/1/catalog1/schema1/ -> Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  ALTER TABLE codex_c13_22.t ALTER my_row_id SET VISIBLE, ADD INDEX (f1), ALGORITHM=INPLACE;
行为 CREATE Index(1:56~1:70) /test/1/catalog1/schema1/ -> Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  ALTER TABLE split_idx.t_alter_modern ADD INDEX idx_func ((LOWER(name))) INVISIBLE;
行为 CREATE Index(1:47~1:55) /test/1/catalog1/schema1/idx_func/ -> Table(1:12~1:36) /test/1/catalog1/split_idx/t_alter_modern/
行为 CALL Function(1:58~1:63) /test/1/catalog1/schema1/LOWER/
------
SQL  ALTER TABLE split_idx.t_alter_modern ADD INDEX idx_multi ((CAST(j->'$.ids' AS UNSIGNED ARRAY))) VISIBLE;
行为 CREATE Index(1:47~1:56) /test/1/catalog1/schema1/idx_multi/ -> Table(1:12~1:36) /test/1/catalog1/split_idx/t_alter_modern/
------
SQL  ALTER TABLE split_idx.t_alter_modern ADD INDEX idx_attr(id) ENGINE_ATTRIBUTE='{}' SECONDARY_ENGINE_ATTRIBUTE='{}';
行为 CREATE Index(1:47~1:55) /test/1/catalog1/schema1/idx_attr/ -> Table(1:12~1:36) /test/1/catalog1/split_idx/t_alter_modern/
------
SQL  ALTER TABLE split_idx.t_alter_modern ALTER INDEX idx_old INVISIBLE;
行为 ALTER Index(1:49~1:56) /test/1/catalog1/schema1/idx_old/ -> Table(1:12~1:36) /test/1/catalog1/split_idx/t_alter_modern/
------
SQL  ALTER TABLE split_idx.t_alter_modern ALTER INDEX idx_old VISIBLE;
行为 ALTER Index(1:49~1:56) /test/1/catalog1/schema1/idx_old/ -> Table(1:12~1:36) /test/1/catalog1/split_idx/t_alter_modern/
------
SQL  CREATE INDEX idx_accept_attr ON split_accept.t_attr (id) ENGINE_ATTRIBUTE='{}' SECONDARY_ENGINE_ATTRIBUTE='{}';
行为 CREATE Index(1:13~1:28) /test/1/catalog1/schema1/idx_accept_attr/ -> Table(1:32~1:51) /test/1/catalog1/split_accept/t_attr/
------
SQL  CREATE INDEX idx_func_80 ON split_idx.t_modern ((LOWER(name))) INVISIBLE;
行为 CREATE Index(1:13~1:24) /test/1/catalog1/schema1/idx_func_80/ -> Table(1:28~1:46) /test/1/catalog1/split_idx/t_modern/
行为 CALL Function(1:49~1:54) /test/1/catalog1/schema1/LOWER/
------
SQL  CREATE INDEX idx_multi_80 ON split_idx.t_modern ((CAST(j->'$.ids' AS UNSIGNED ARRAY))) VISIBLE;
行为 CREATE Index(1:13~1:25) /test/1/catalog1/schema1/idx_multi_80/ -> Table(1:29~1:47) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_visible_80 ON split_idx.t_modern (id) VISIBLE;
行为 CREATE Index(1:13~1:27) /test/1/catalog1/schema1/idx_visible_80/ -> Table(1:31~1:49) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_invisible_80 ON split_idx.t_modern (name) INVISIBLE;
行为 CREATE Index(1:13~1:29) /test/1/catalog1/schema1/idx_invisible_80/ -> Table(1:33~1:51) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_attrs_80 ON split_idx.t_modern (id) ENGINE_ATTRIBUTE='{}' SECONDARY_ENGINE_ATTRIBUTE='{}';
行为 CREATE Index(1:13~1:25) /test/1/catalog1/schema1/idx_attrs_80/ -> Table(1:29~1:47) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_func_80 ON split_idx.t_modern;
行为 DROP Index(1:11~1:22) /test/1/catalog1/schema1/idx_func_80/ -> Table(1:26~1:44) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_multi_80 ON split_idx.t_modern;
行为 DROP Index(1:11~1:23) /test/1/catalog1/schema1/idx_multi_80/ -> Table(1:27~1:45) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_visible_80 ON split_idx.t_modern;
行为 DROP Index(1:11~1:25) /test/1/catalog1/schema1/idx_visible_80/ -> Table(1:29~1:47) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_invisible_80 ON split_idx.t_modern;
行为 DROP Index(1:11~1:27) /test/1/catalog1/schema1/idx_invisible_80/ -> Table(1:31~1:49) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_attrs_80 ON split_idx.t_modern;
行为 DROP Index(1:11~1:23) /test/1/catalog1/schema1/idx_attrs_80/ -> Table(1:27~1:45) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE TABLE codex_constraint_c08 (id INT, name VARCHAR(64), INDEX ix_c08 (id, (LOWER(name))));
行为 CREATE Index(1:67~1:73) /test/1/catalog1/schema1/ix_c08/ -> Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c08/
行为 CALL Function(1:80~1:85) /test/1/catalog1/schema1/LOWER/
------
SQL  CREATE TABLE codex_constraint_c09 (id INT, modified DATETIME, j JSON, INDEX ix_c09 (id, (CAST(j->'$.ids' AS UNSIGNED ARRAY)), modified));
行为 CREATE Index(1:76~1:82) /test/1/catalog1/schema1/ix_c09/ -> Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c09/
------
SQL  ALTER TABLE split_type_enum_set.es_roles ADD INDEX idx_admin ((FIND_IN_SET('Admin',roles)));
行为 CREATE Index(1:51~1:60) /test/1/catalog1/schema1/idx_admin/ -> Table(1:12~1:40) /test/1/catalog1/split_type_enum_set/es_roles/
行为 CALL Function(1:63~1:74) /test/1/catalog1/schema1/FIND_IN_SET/
------
SQL  ALTER TABLE split_type_json.json_mvi_alter\n      ADD INDEX idx_tags ((CAST(doc->'$.tags' AS CHAR(20) ARRAY)));
行为 CREATE Index(2:16~2:24) /test/1/catalog1/schema1/idx_tags/ -> Table(1:12~1:42) /test/1/catalog1/split_type_json/json_mvi_alter/
------
SQL  CREATE INDEX idx_dates\n    ON split_type_json.json_mvi_create (\n      (CAST(doc->'$.dates' AS DATE ARRAY))\n    );
行为 CREATE Index(1:13~1:22) /test/1/catalog1/schema1/idx_dates/ -> Table(2:7~2:38) /test/1/catalog1/split_type_json/json_mvi_create/
------
SQL  CREATE UNIQUE INDEX split_geometry_unique_idx ON split_geometry_unique(g);
行为 CREATE Index(1:20~1:45) /test/1/catalog1/schema1/split_geometry_unique_idx/ -> Table(1:49~1:70) /test/1/catalog1/schema1/split_geometry_unique/
------
SQL  CREATE TABLE idx_inline_visibility(a INT,KEY k_a(a) INVISIBLE);
行为 CREATE Index(1:45~1:48) /test/1/catalog1/schema1/k_a/ -> Table(1:13~1:34) /test/1/catalog1/schema1/idx_inline_visibility/
------
SQL  CREATE TABLE spatial_part(location POINT NOT NULL SRID 4326,id INT PRIMARY KEY,SPATIAL INDEX(location)) ENGINE=InnoDB PARTITION BY HASH(id) PARTITIONS 2;
行为 CREATE Index(1:79~1:102) /test/1/catalog1/schema1/ -> Table(1:13~1:25) /test/1/catalog1/schema1/spatial_part/
行为 CREATE Constraint(1:67~1:78) /test/1/catalog1/schema1/
------
SQL  CREATE VIEW view1 AS SELECT name AS table_name FROM dd_table;
行为 CREATE View(1:12~1:17) /test/1/catalog1/schema1/view1/ -> Table(1:52~1:60) /test/1/catalog1/schema1/dd_table/
------
SQL  CREATE VIEW view2 AS SELECT name COLLATE utf8_tolower_ci AS table_name FROM dd_table;
行为 CREATE View(1:12~1:17) /test/1/catalog1/schema1/view2/ -> Table(1:76~1:84) /test/1/catalog1/schema1/dd_table/
------
SQL  CREATE TABLE uuid_bin_to_text_ctas AS SELECT BIN_TO_UUID(bin_value) AS from_binary, BIN_TO_UUID(varbin_value) AS from_varbinary, BIN_TO_UUID(tinyblob_value) AS from_tinyblob, BIN_TO_UUID(tinytext_value) AS from_tinytext, BIN_TO_UUID(blob_value) AS from_blob FROM uuid_storage_types;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/uuid_bin_to_text_ctas/ -> Table(1:263~1:281) /test/1/catalog1/schema1/uuid_storage_types/
行为 CALL Function(1:45~1:56) /test/1/catalog1/schema1/BIN_TO_UUID/
------
SQL  CREATE TABLE uuid_text_generated (uuid_text VARCHAR(100), uuid_binary BINARY(16) AS (UUID_TO_BIN(uuid_text)) VIRTUAL, INDEX (uuid_binary));
行为 CREATE Index(1:118~1:137) /test/1/catalog1/schema1/ -> Table(1:13~1:32) /test/1/catalog1/schema1/uuid_text_generated/
行为 CALL Function(1:85~1:96) /test/1/catalog1/schema1/UUID_TO_BIN/
------
SQL  CREATE TABLE uuid_binary_generated (uuid_binary BINARY(16), uuid_text VARCHAR(36) AS (BIN_TO_UUID(uuid_binary)) VIRTUAL, INDEX (uuid_binary), INDEX (uuid_text));
行为 CREATE Index(1:121~1:140) /test/1/catalog1/schema1/ -> Table(1:13~1:34) /test/1/catalog1/schema1/uuid_binary_generated/
行为 CREATE Index(1:142~1:159) /test/1/catalog1/schema1/ -> Table(1:13~1:34) /test/1/catalog1/schema1/uuid_binary_generated/
行为 CALL Function(1:86~1:97) /test/1/catalog1/schema1/BIN_TO_UUID/
------
SQL  CREATE VIEW split_window_json.v_array AS SELECT i,j,JSON_ARRAYAGG(j) OVER (ORDER BY i DESC ROWS UNBOUNDED PRECEDING) AS a FROM split_window_json.t1;
行为 CREATE View(1:12~1:37) /test/1/catalog1/split_window_json/v_array/ -> Table(1:127~1:147) /test/1/catalog1/split_window_json/t1/
行为 CALL Function(1:52~1:65) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE VIEW split_window_json.v_object AS SELECT i,j,JSON_OBJECTAGG(i,j) OVER (ORDER BY i DESC ROWS UNBOUNDED PRECEDING) AS o FROM split_window_json.t1;
行为 CREATE View(1:12~1:38) /test/1/catalog1/split_window_json/v_object/ -> Table(1:131~1:151) /test/1/catalog1/split_window_json/t1/
行为 CALL Function(1:53~1:67) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  CREATE TABLE split_window_tail.twf AS SELECT RANK() OVER (ORDER BY a) AS rnk FROM split_window_tail.t GROUP BY a;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/split_window_tail/twf/ -> Table(1:82~1:101) /test/1/catalog1/split_window_tail/t/
行为 CALL Function(1:45~1:49) /test/1/catalog1/schema1/RANK/
------
SQL  CREATE TABLE t3 AS SELECT * FROM t1 FOR UPDATE SKIP LOCKED;
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t3/ -> Table(1:33~1:35) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE t3 WITH qn(foo,bar) AS (SELECT a,b FROM t1 LIMIT 2) SELECT bar,foo FROM qn;
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t3/ -> Table(1:53~1:55) /test/1/catalog1/schema1/t1/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER IF NOT EXISTS split_trigger.trg_bi\nBEFORE INSERT ON split_trigger.src\nFOR EACH ROW\nSET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:52~1:72) /test/1/catalog1/split_trigger/trg_bi/ -> Table(2:17~2:34) /test/1/catalog1/split_trigger/src/
行为 CALL Function(4:21~4:38) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split80.trg_order_follows BEFORE INSERT ON split80.trigger_src FOR EACH ROW FOLLOWS trg_order_base SET NEW.note = 'follows';
行为 CREATE Trigger(1:15~1:40) /test/1/catalog1/split80/trg_order_follows/ -> Table(1:58~1:77) /test/1/catalog1/split80/trigger_src/
------
SQL  CREATE TRIGGER split80.trg_order_precedes BEFORE INSERT ON split80.trigger_src FOR EACH ROW PRECEDES trg_order_base SET NEW.note = 'precedes';
行为 CREATE Trigger(1:15~1:41) /test/1/catalog1/split80/trg_order_precedes/ -> Table(1:59~1:78) /test/1/catalog1/split80/trigger_src/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER split80.trg_bi BEFORE INSERT ON split80.trigger_src FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:38~1:52) /test/1/catalog1/split80/trg_bi/ -> Table(1:70~1:89) /test/1/catalog1/split80/trigger_src/
行为 CALL Function(1:124~1:141) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split80.trg_bu BEFORE UPDATE ON split80.trigger_src FOR EACH ROW SET NEW.note = COALESCE(NEW.note, 'before update');
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split80/trg_bu/ -> Table(1:47~1:66) /test/1/catalog1/split80/trigger_src/
行为 CALL Function(1:95~1:103) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER split80.trg_bd BEFORE DELETE ON split80.trigger_src FOR EACH ROW INSERT INTO split80.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'before delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split80/trg_bd/ -> Table(1:47~1:66) /test/1/catalog1/split80/trigger_src/
行为 INSERT Table(1:92~1:113) /test/1/catalog1/split80/trigger_audit/
------
SQL  CREATE TRIGGER split80.trg_ai AFTER INSERT ON split80.trigger_src FOR EACH ROW INSERT INTO split80.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (NEW.id, 'after insert', NULL, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split80/trg_ai/ -> Table(1:46~1:65) /test/1/catalog1/split80/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split80/trigger_audit/
------
SQL  CREATE TRIGGER split80.trg_au AFTER UPDATE ON split80.trigger_src FOR EACH ROW INSERT INTO split80.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after update', OLD.amount, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split80/trg_au/ -> Table(1:46~1:65) /test/1/catalog1/split80/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split80/trigger_audit/
------
SQL  CREATE TRIGGER split80.trg_ad AFTER DELETE ON split80.trigger_src FOR EACH ROW INSERT INTO split80.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split80/trg_ad/ -> Table(1:46~1:65) /test/1/catalog1/split80/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split80/trigger_audit/
------
SQL  CREATE TRIGGER IF NOT EXISTS trg_ifne_gap BEFORE DELETE ON t1 FOR EACH ROW BEGIN END;
行为 CREATE Trigger(1:29~1:41) /test/1/catalog1/schema1/trg_ifne_gap/ -> Table(1:59~1:61) /test/1/catalog1/schema1/t1/
------
SQL  CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `v_biz_model3` AS select `biz_model`.`id` AS `id`,`biz_model`.`created_time` AS `created_time`,`biz_model`.`updated_time` AS `updated_time`,`biz_model`.`content` AS `content` from `biz_model` limit 20;
行为 CREATE View(1:72~1:86) /test/1/catalog1/schema1/v_biz_model3/ -> Table(1:252~1:263) /test/1/catalog1/schema1/biz_model/
------
SQL  CREATE DEFINER=`root`@`%` TRIGGER `check_age_before_insert3` BEFORE INSERT ON `students` FOR EACH ROW BEGIN\n    IF NEW.age < 18 THEN\n        SIGNAL SQLSTATE '45000'\n            SET MESSAGE_TEXT = '年龄不能低于18岁';\n    END IF;\nEND;
行为 CREATE Trigger(1:34~1:60) /test/1/catalog1/schema1/check_age_before_insert3/ -> Table(1:78~1:88) /test/1/catalog1/schema1/students/
------
SQL  RENAME TABLE splitv.rt_swap_old TO splitv.rt_swap_tmp,\n             splitv.rt_swap_new TO splitv.rt_swap_old,\n             splitv.rt_swap_tmp TO splitv.rt_swap_new;
行为 RENAME Table(1:13~1:31) /test/1/catalog1/splitv/rt_swap_old/ -> Table(1:35~1:53) /test/1/catalog1/splitv/rt_swap_tmp/
行为 RENAME Table(2:13~2:31) /test/1/catalog1/splitv/rt_swap_new/ -> Table(2:35~2:53) /test/1/catalog1/splitv/rt_swap_old/
行为 RENAME Table(3:13~3:31) /test/1/catalog1/splitv/rt_swap_tmp/ -> Table(3:35~3:53) /test/1/catalog1/splitv/rt_swap_new/
------
SQL  RENAME TABLE splitv.rt_move_src TO splitv2.rt_move_dst;
行为 RENAME Table(1:13~1:31) /test/1/catalog1/splitv/rt_move_src/ -> Table(1:35~1:54) /test/1/catalog1/splitv2/rt_move_dst/
------
SQL  RENAME TABLE splitv.rv_rename_old TO splitv.rv_rename_new;
行为 RENAME Table(1:13~1:33) /test/1/catalog1/splitv/rv_rename_old/ -> Table(1:37~1:57) /test/1/catalog1/splitv/rv_rename_new/
------
SQL  alter table abc add constraint ptr foreign key (id) references abc2 (id2);
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/ -> Table(1:63~1:67) /test/1/catalog1/schema1/abc2/
行为 CREATE Constraint(1:31~1:34) /test/1/catalog1/schema1/ptr/
------
SQL  alter table abc add foreign key (id) references abc2 (id2);
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/ -> Table(1:48~1:52) /test/1/catalog1/schema1/abc2/
行为 CREATE Constraint(1:16~1:58) /test/1/catalog1/schema1/
------
SQL  alter table abc add constraint fk_test foreign key (id) references abc2 (id2) on delete cascade;
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/ -> Table(1:67~1:71) /test/1/catalog1/schema1/abc2/
行为 CREATE Constraint(1:31~1:38) /test/1/catalog1/schema1/fk_test/
------
SQL  alter table abc add constraint fk_test foreign key (id) references abc2 (id2) on update set null;
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/ -> Table(1:67~1:71) /test/1/catalog1/schema1/abc2/
行为 CREATE Constraint(1:31~1:38) /test/1/catalog1/schema1/fk_test/
------
SQL  alter table abc add constraint fk_test foreign key (id) references abc2 (id2) on delete cascade on update set null;
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/ -> Table(1:67~1:71) /test/1/catalog1/schema1/abc2/
行为 CREATE Constraint(1:31~1:38) /test/1/catalog1/schema1/fk_test/
------
SQL  create table test.abc(id int(4), name varchar(25) not null,constraint ptr foreign key (id) references test.abc2(id2));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/ -> Table(1:102~1:111) /test/1/catalog1/test/abc2/
行为 CREATE Constraint(1:70~1:73) /test/1/catalog1/schema1/ptr/
------
SQL  create table test.abc(id int(4), name varchar(25) not null,key (id));
行为 CREATE Index(1:59~1:67) /test/1/catalog1/schema1/ -> Table(1:13~1:21) /test/1/catalog1/test/abc/
------
SQL  create table test.abc_copy like test.abc;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/test/abc_copy/ -> Table(1:32~1:40) /test/1/catalog1/test/abc/
------
SQL  create table abc_copy like abc;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/abc_copy/ -> Table(1:27~1:30) /test/1/catalog1/schema1/abc/
------
SQL  create table test.abc_select select * from test.abc;
行为 CREATE Table(1:13~1:28) /test/1/catalog1/test/abc_select/ -> Table(1:43~1:51) /test/1/catalog1/test/abc/
------
SQL  rename table abc to cba;
行为 RENAME Table(1:13~1:16) /test/1/catalog1/schema1/abc/ -> Table(1:20~1:23) /test/1/catalog1/schema1/cba/
------
SQL  rename table schema1.abc to schema2.cba;
行为 RENAME Table(1:13~1:24) /test/1/catalog1/schema1/abc/ -> Table(1:28~1:39) /test/1/catalog1/schema2/cba/
------
SQL  create table test.abc(id int(4), name varchar(25) not null, key (id));
行为 CREATE Index(1:60~1:68) /test/1/catalog1/schema1/ -> Table(1:13~1:21) /test/1/catalog1/test/abc/
------
SQL  create table test.abc(id int(4), name varchar(25) not null, constraint ptr foreign key (id) references test.abc2(id2));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/ -> Table(1:103~1:112) /test/1/catalog1/test/abc2/
行为 CREATE Constraint(1:71~1:74) /test/1/catalog1/schema1/ptr/
------
SQL  CREATE TABLE fi_desc (col1 INT, INDEX ((ABS(col1)) DESC));
行为 CREATE Index(1:32~1:56) /test/1/catalog1/schema1/ -> Table(1:13~1:20) /test/1/catalog1/schema1/fi_desc/
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/ABS/
------
SQL  CREATE TABLE fi_json (f1 JSON, INDEX idx1 ((CAST(f1->'$.id' AS UNSIGNED))));
行为 CREATE Index(1:37~1:41) /test/1/catalog1/schema1/idx1/ -> Table(1:13~1:20) /test/1/catalog1/schema1/fi_json/
行为 CALL Function(1:44~1:48) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE gap_def_idx (i INT, b CHAR(255) DEFAULT (MD5(i)), INDEX (b(10)));
行为 CREATE Index(1:63~1:76) /test/1/catalog1/schema1/ -> Table(1:13~1:24) /test/1/catalog1/schema1/gap_def_idx/
行为 CALL Function(1:54~1:57) /test/1/catalog1/schema1/MD5/
------
SQL  CREATE TABLE audit_ctas_table TABLE audit_source;
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/audit_ctas_table/ -> Table(1:36~1:48) /test/1/catalog1/schema1/audit_source/
------
SQL  CREATE TABLE audit_ctas_table_cols(pad INT) TABLE audit_source;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/audit_ctas_table_cols/ -> Table(1:50~1:62) /test/1/catalog1/schema1/audit_source/
------
SQL  ALTER TABLE gap_attr ADD INDEX (m) ENGINE_ATTRIBUTE='{"algo":"inplace"}', ALGORITHM=INPLACE;
行为 CREATE Index(1:21~1:72) /test/1/catalog1/schema1/ -> Table(1:12~1:20) /test/1/catalog1/schema1/gap_attr/
------
SQL  ALTER TABLE gap_idx ADD INDEX ab(a,b), ALTER INDEX ab INVISIBLE;
行为 CREATE Index(1:30~1:32) /test/1/catalog1/schema1/ab/ -> Table(1:12~1:19) /test/1/catalog1/schema1/gap_idx/
行为 ALTER Index(1:51~1:53) /test/1/catalog1/schema1/ab/ -> Table(1:12~1:19) /test/1/catalog1/schema1/gap_idx/
------
SQL  ALTER TABLE gap_idx RENAME INDEX a TO x, ALTER INDEX x INVISIBLE;
行为 RENAME Index(1:33~1:34) /test/1/catalog1/schema1/a/ -> Index(1:38~1:39) /test/1/catalog1/schema1/x/
行为 ALTER Index(1:53~1:54) /test/1/catalog1/schema1/x/ -> Table(1:12~1:19) /test/1/catalog1/schema1/gap_idx/
------
SQL  ALTER TABLE gap_mvi ADD INDEX mv USING HASH ((JSON_LENGTH(j1)),(CAST(j2->'$.k[*]' AS TIME ARRAY))) INVISIBLE;
行为 CREATE Index(1:30~1:32) /test/1/catalog1/schema1/mv/ -> Table(1:12~1:19) /test/1/catalog1/schema1/gap_mvi/
行为 CALL Function(1:46~1:57) /test/1/catalog1/schema1/JSON_LENGTH/
------
SQL  CREATE VIEW split_view80.v_cte AS WITH cte AS (SELECT id, amount FROM split_view80.src) SELECT id, amount FROM cte;
行为 CREATE View(1:12~1:30) /test/1/catalog1/split_view80/v_cte/ -> Table(1:70~1:86) /test/1/catalog1/split_view80/src/
------
SQL  CREATE VIEW split_view80.v_table AS TABLE split_view80.src;
行为 CREATE View(1:12~1:32) /test/1/catalog1/split_view80/v_table/ -> Table(1:42~1:58) /test/1/catalog1/split_view80/src/
------
SQL  CREATE VIEW split_window_minmax.v AS SELECT id,SUM(id) OVER w AS total,MIN(id) OVER w AS min_id,MAX(id) OVER w AS max_id,sex FROM split_window_minmax.people WINDOW w AS (PARTITION BY sex ORDER BY id ROWS BETWEEN 2 PRECEDING AND 1 PRECEDING);
行为 CREATE View(1:12~1:33) /test/1/catalog1/split_window_minmax/v/ -> Table(1:130~1:156) /test/1/catalog1/split_window_minmax/people/
行为 CALL Function(1:47~1:50) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:71~1:74) /test/1/catalog1/schema1/MIN/
行为 CALL Function(1:96~1:99) /test/1/catalog1/schema1/MAX/
------
SQL  CREATE VIEW split_window_stats.v_stats AS SELECT id,SUM(id) OVER w AS total,STD(id) OVER w AS std_id,VARIANCE(id) OVER w AS var_id,sex FROM split_window_stats.people WINDOW w AS (PARTITION BY sex ORDER BY id ROWS BETWEEN 2 PRECEDING AND 1 PRECEDING);
行为 CREATE View(1:12~1:38) /test/1/catalog1/split_window_stats/v_stats/ -> Table(1:140~1:165) /test/1/catalog1/split_window_stats/people/
行为 CALL Function(1:52~1:55) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:76~1:79) /test/1/catalog1/schema1/STD/
行为 CALL Function(1:101~1:109) /test/1/catalog1/schema1/VARIANCE/
------
SQL  CREATE VIEW split_window_view.v_native AS SELECT COUNT(*) OVER w0 AS c0,COUNT(*) OVER w AS c1,COUNT(*) OVER w1 AS c2 FROM split_window_view.t WINDOW w0 AS (),w AS (w0 ORDER BY tm),w1 AS (w RANGE BETWEEN INTERVAL 24 HOUR PRECEDING AND INTERVAL '2:2' MINUTE_SECOND FOLLOWING);
行为 CREATE View(1:12~1:38) /test/1/catalog1/split_window_view/v_native/ -> Table(1:122~1:141) /test/1/catalog1/split_window_view/t/
行为 CALL Function(1:49~1:54) /test/1/catalog1/schema1/COUNT/
------
SQL  create view v_test as select id, name from test;
行为 CREATE View(1:12~1:18) /test/1/catalog1/schema1/v_test/ -> Table(1:43~1:47) /test/1/catalog1/schema1/test/
------
SQL  create or replace view v_test as select id, name from test;
行为 REPLACE View(1:23~1:29) /test/1/catalog1/schema1/v_test/ -> Table(1:54~1:58) /test/1/catalog1/schema1/test/
------
SQL  create view test.v_test as select id, name from test where id > 0;
行为 CREATE View(1:12~1:23) /test/1/catalog1/test/v_test/ -> Table(1:48~1:52) /test/1/catalog1/schema1/test/
------
SQL  create view v_test (col1, col2) as select id, name from test;
行为 CREATE View(1:12~1:18) /test/1/catalog1/schema1/v_test/ -> Table(1:56~1:60) /test/1/catalog1/schema1/test/
------
SQL  create algorithm = merge view v_test as select * from test;
行为 CREATE View(1:30~1:36) /test/1/catalog1/schema1/v_test/ -> Table(1:54~1:58) /test/1/catalog1/schema1/test/
------
SQL  alter view v_test as select id, name, age from test;
行为 ALTER View(1:11~1:17) /test/1/catalog1/schema1/v_test/ -> Table(1:47~1:51) /test/1/catalog1/schema1/test/
------
SQL  alter algorithm = temptable view v_test as select * from test;
行为 ALTER View(1:33~1:39) /test/1/catalog1/schema1/v_test/ -> Table(1:57~1:61) /test/1/catalog1/schema1/test/
------
SQL  ALTER VIEW audit_v AS TABLE audit_source;
行为 ALTER View(1:11~1:18) /test/1/catalog1/schema1/audit_v/ -> Table(1:28~1:40) /test/1/catalog1/schema1/audit_source/
------
SQL  CREATE VIEW v AS SELECT (WITH qn AS (SELECT 'with') SELECT * FROM qn) AS scal_subq FROM dual;
行为 CREATE View(1:12~1:13) /test/1/catalog1/schema1/v/ -> Table(1:88~1:92) /test/1/catalog1/schema1/dual/
------
SQL  DELETE FROM t2 a2 WHERE NOT EXISTS (SELECT * FROM t1 WHERE t1.c1 = a2.c2);
行为 DELETE Table(1:12~1:14) /test/1/catalog1/schema1/t2/ -> Table(1:50~1:52) /test/1/catalog1/schema1/t1/
------
SQL  WITH cte AS (SELECT id FROM table1) DELETE FROM table2 WHERE id IN (SELECT id FROM cte);
行为 DELETE Table(1:48~1:54) /test/1/catalog1/schema1/table2/ -> Table(1:28~1:34) /test/1/catalog1/schema1/table1/
------
SQL  WITH RECURSIVE cte(n) AS (SELECT 999) DELETE t FROM dml_audit.t AS t JOIN cte ON t.id=cte.n;
行为 DELETE Table(1:45~1:46) /test/1/catalog1/schema1/t/ -> Table(1:52~1:63) /test/1/catalog1/dml_audit/t/
------
SQL  DELETE FROM split_window_rpl.t2 WHERE c1 IN (SELECT 1+DENSE_RANK() OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:80~1:84) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:54~1:64) /test/1/catalog1/schema1/DENSE_RANK/
------
SQL  DELETE FROM split_window_rpl.t2 WHERE c2=ANY (SELECT 2+PERCENT_RANK() OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:83~1:87) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:55~1:67) /test/1/catalog1/schema1/PERCENT_RANK/
------
SQL  DELETE FROM split_window_rpl.t2 WHERE c3>SOME (SELECT NTILE(1) OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:76~1:80) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:54~1:59) /test/1/catalog1/schema1/NTILE/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c2<=ALL (SELECT FIRST_VALUE(100) OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:84~1:88) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:54~1:65) /test/1/catalog1/schema1/FIRST_VALUE/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c3=ANY (SELECT LAST_VALUE('Updated') OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:88~1:92) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:53~1:63) /test/1/catalog1/schema1/LAST_VALUE/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c4=ALL (SELECT NTH_VALUE('Updated',1) OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:89~1:93) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:53~1:62) /test/1/catalog1/schema1/NTH_VALUE/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c5 IN (SELECT LAG('2017-01-01',0) OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:85~1:89) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:52~1:55) /test/1/catalog1/schema1/LAG/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c6 IN (SELECT CAST(FIRST_VALUE('2017-01-01 12:30:45') OVER () AS DATETIME) FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:118~1:122) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:52~1:56) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:57~1:68) /test/1/catalog1/schema1/FIRST_VALUE/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c7 IN (SELECT LEAD('Updated') OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:81~1:85) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:52~1:56) /test/1/catalog1/schema1/LEAD/
------
SQL  DELETE FROM split_window_rpl.t4 WHERE c8 IN (SELECT NTH_VALUE('Updated',1) OVER () FROM DUAL);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:88~1:92) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:52~1:61) /test/1/catalog1/schema1/NTH_VALUE/
------
SQL  WITH c AS (SELECT id FROM split_dml_src WHERE id<100)\nDELETE d FROM split_dml_dst PARTITION(p0) AS d FORCE INDEX FOR JOIN (idx_v)\n  JOIN c ON c.id=d.id\n  WHERE EXISTS (SELECT 1 FROM split_dml_aux AS a WHERE a.id=c.id);
行为 DELETE Table(2:7~2:8) /test/1/catalog1/schema1/d/ -> [Table(1:26~1:39) /test/1/catalog1/schema1/split_dml_src/ ; Table(2:14~2:27) /test/1/catalog1/schema1/split_dml_dst/ ; Table(4:30~4:43) /test/1/catalog1/schema1/split_dml_aux/]
------
SQL  WITH cte AS (SELECT alias1.col_int_key AS field1 FROM a AS alias1 LEFT JOIN c AS alias2 ON alias1.col_blob=alias2.col_blob_key WHERE alias2.pk>3 AND alias2.pk<(3+10) OR alias1.col_varchar_key>='z' AND alias1.col_varchar_key<='k') DELETE /*+ NO_MERGE(outrcte) */ LOW_PRIORITY QUICK outr1.*,outr2.* FROM d AS outr1 LEFT JOIN c AS outr2 ON outr1.col_int=outr2.pk JOIN a AS outr3 ON outr1.col_int_key=outr3.pk RIGHT JOIN cte AS outrcte ON outr1.col_int_key=outrcte.field1 WHERE outr1.col_blob_key<>(SELECT innr1.col_blob FROM a AS innr2 INNER JOIN a AS innr1 ON innr2.col_datetime>=innr1.col_datetime RIGHT OUTER JOIN cte AS innrcte ON innr2.col_int_key<innrcte.field1 WHERE innr1.col_datetime='2006-02-24');
行为 DELETE Table(1:281~1:286) /test/1/catalog1/schema1/outr1/ -> [Table(1:54~1:55) /test/1/catalog1/schema1/a/ ; Table(1:76~1:77) /test/1/catalog1/schema1/c/ ; Table(1:302~1:303) /test/1/catalog1/schema1/d/]
行为 DELETE Table(1:289~1:294) /test/1/catalog1/schema1/outr2/
------
SQL  insert into `table2` with tab1Cnt as (select id,name from table1) select * from tab1Cnt;
行为 INSERT Table(1:12~1:20) /test/1/catalog1/schema1/table2/ -> Table(1:58~1:64) /test/1/catalog1/schema1/table1/
------
SQL  INSERT INTO target (id, name, val) TABLE src;
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/target/ -> Table(1:41~1:44) /test/1/catalog1/schema1/src/
------
SQL  INSERT INTO dml_audit.t (id,v) TABLE dml_audit.src ON DUPLICATE KEY UPDATE v='i04u';
行为 MERGE Table(1:12~1:23) /test/1/catalog1/dml_audit/t/ -> Table(1:37~1:50) /test/1/catalog1/dml_audit/src/
------
SQL  INSERT INTO split_window_rpl.t3a SELECT SUM(a) OVER (ROWS UNBOUNDED PRECEDING),AVG(b) OVER (ORDER BY pk ROWS BETWEEN 2 PRECEDING AND 2 FOLLOWING),COUNT(*) OVER (PARTITION BY d ORDER BY pk ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING),BIT_AND(pk) OVER (PARTITION BY d ORDER BY pk ROWS BETWEEN 2 PRECEDING AND UNBOUNDED FOLLOWING),BIT_OR(a) OVER (PARTITION BY d ORDER BY e ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),BIT_XOR(i) OVER (PARTITION BY c),MIN(e) OVER (ORDER BY e ROWS BETWEEN 2 PRECEDING AND UNBOUNDED FOLLOWING),MAX(f) OVER (ORDER BY f ROWS BETWEEN CURRENT ROW AND 3 FOLLOWING),STDDEV_SAMP(a) OVER (ORDER BY pk ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),VAR_SAMP(a) OVER (ORDER BY pk ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),STDDEV_POP(b) OVER (),VAR_POP(a) OVER (),JSON_ARRAYAGG(e) OVER (PARTITION BY d ORDER BY pk ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),JSON_OBJECTAGG(pk,a) OVER (ORDER BY a ROWS BETWEEN CURRENT ROW AND 4 FOLLOWING) FROM split_window_rpl.t1;
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_window_rpl/t3a/ -> Table(1:1011~1:1030) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:79~1:82) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:146~1:151) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:230~1:237) /test/1/catalog1/schema1/BIT_AND/
行为 CALL Function(1:325~1:331) /test/1/catalog1/schema1/BIT_OR/
行为 CALL Function(1:425~1:432) /test/1/catalog1/schema1/BIT_XOR/
行为 CALL Function(1:458~1:461) /test/1/catalog1/schema1/MIN/
行为 CALL Function(1:532~1:535) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:598~1:609) /test/1/catalog1/schema1/STDDEV_SAMP/
行为 CALL Function(1:689~1:697) /test/1/catalog1/schema1/VAR_SAMP/
行为 CALL Function(1:777~1:787) /test/1/catalog1/schema1/STDDEV_POP/
行为 CALL Function(1:799~1:806) /test/1/catalog1/schema1/VAR_POP/
行为 CALL Function(1:818~1:831) /test/1/catalog1/schema1/JSON_ARRAYAGG/
行为 CALL Function(1:926~1:940) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  INSERT INTO split_window_rpl.t3a SELECT SUM(a) OVER (RANGE UNBOUNDED PRECEDING),AVG(b) OVER (ORDER BY pk RANGE BETWEEN 2 PRECEDING AND 2 FOLLOWING),COUNT(*) OVER (PARTITION BY d ORDER BY pk RANGE BETWEEN CURRENT ROW AND 2 FOLLOWING),BIT_AND(pk) OVER (PARTITION BY d ORDER BY pk RANGE BETWEEN 2 PRECEDING AND UNBOUNDED FOLLOWING),BIT_OR(a) OVER (PARTITION BY d ORDER BY e RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),BIT_XOR(i) OVER (PARTITION BY c),MIN(e) OVER (ORDER BY e RANGE INTERVAL 5 YEAR PRECEDING),MAX(f) OVER (ORDER BY f RANGE BETWEEN CURRENT ROW AND INTERVAL 5 YEAR FOLLOWING),STDDEV_SAMP(a) OVER (ORDER BY pk RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),VAR_SAMP(a) OVER (ORDER BY pk RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),STDDEV_POP(b) OVER (),VAR_POP(a) OVER (),JSON_ARRAYAGG(b) OVER (ORDER BY f ROWS BETWEEN CURRENT ROW AND 5 FOLLOWING),JSON_OBJECTAGG(b,e) OVER (PARTITION BY d ORDER BY e ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM split_window_rpl.t1;
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_window_rpl/t3a/ -> Table(1:1014~1:1033) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:80~1:83) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:148~1:153) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:233~1:240) /test/1/catalog1/schema1/BIT_AND/
行为 CALL Function(1:329~1:335) /test/1/catalog1/schema1/BIT_OR/
行为 CALL Function(1:430~1:437) /test/1/catalog1/schema1/BIT_XOR/
行为 CALL Function(1:463~1:466) /test/1/catalog1/schema1/MIN/
行为 CALL Function(1:520~1:523) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:601~1:612) /test/1/catalog1/schema1/STDDEV_SAMP/
行为 CALL Function(1:693~1:701) /test/1/catalog1/schema1/VAR_SAMP/
行为 CALL Function(1:782~1:792) /test/1/catalog1/schema1/STDDEV_POP/
行为 CALL Function(1:804~1:811) /test/1/catalog1/schema1/VAR_POP/
行为 CALL Function(1:823~1:836) /test/1/catalog1/schema1/JSON_ARRAYAGG/
行为 CALL Function(1:899~1:913) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  INSERT INTO split_window_rpl.t3b SELECT SUM(a) OVER (ROWS UNBOUNDED PRECEDING),AVG(b) OVER (ORDER BY pk ROWS BETWEEN 2 PRECEDING AND 2 FOLLOWING),COUNT(*) OVER (PARTITION BY d ORDER BY pk ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING),BIT_AND(pk) OVER (PARTITION BY d ORDER BY pk ROWS BETWEEN 2 PRECEDING AND UNBOUNDED FOLLOWING),BIT_OR(a) OVER (PARTITION BY d ORDER BY e ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),BIT_XOR(i) OVER (PARTITION BY c),MIN(e) OVER (ORDER BY e ROWS BETWEEN 2 PRECEDING AND UNBOUNDED FOLLOWING),MAX(f) OVER (ORDER BY f ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING),STDDEV_SAMP(a) OVER (ORDER BY pk ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),VAR_SAMP(a) OVER (ORDER BY pk ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),STDDEV_POP(b) OVER (),VAR_POP(a) OVER (),JSON_ARRAYAGG(b) OVER (ROWS UNBOUNDED PRECEDING),JSON_OBJECTAGG(pk,f) OVER (PARTITION BY e ORDER BY pk RANGE BETWEEN CURRENT ROW AND 3 FOLLOWING) FROM split_window_rpl.t1;
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_window_rpl/t3b/ -> Table(1:969~1:988) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:79~1:82) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:146~1:151) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:230~1:237) /test/1/catalog1/schema1/BIT_AND/
行为 CALL Function(1:325~1:331) /test/1/catalog1/schema1/BIT_OR/
行为 CALL Function(1:425~1:432) /test/1/catalog1/schema1/BIT_XOR/
行为 CALL Function(1:458~1:461) /test/1/catalog1/schema1/MIN/
行为 CALL Function(1:532~1:535) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:598~1:609) /test/1/catalog1/schema1/STDDEV_SAMP/
行为 CALL Function(1:689~1:697) /test/1/catalog1/schema1/VAR_SAMP/
行为 CALL Function(1:777~1:787) /test/1/catalog1/schema1/STDDEV_POP/
行为 CALL Function(1:799~1:806) /test/1/catalog1/schema1/VAR_POP/
行为 CALL Function(1:818~1:831) /test/1/catalog1/schema1/JSON_ARRAYAGG/
行为 CALL Function(1:867~1:881) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  INSERT INTO split_window_rpl.t3b SELECT SUM(a) OVER (RANGE UNBOUNDED PRECEDING),AVG(b) OVER (ORDER BY pk RANGE BETWEEN 2 PRECEDING AND 2 FOLLOWING),COUNT(*) OVER (PARTITION BY d ORDER BY pk RANGE BETWEEN CURRENT ROW AND 2 FOLLOWING),BIT_AND(pk) OVER (PARTITION BY d ORDER BY f RANGE INTERVAL 2 YEAR PRECEDING),BIT_OR(a) OVER (PARTITION BY d ORDER BY e RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),BIT_XOR(i) OVER (PARTITION BY c),MIN(e) OVER (ORDER BY e RANGE INTERVAL 5 YEAR PRECEDING),MAX(f) OVER (ORDER BY f RANGE BETWEEN CURRENT ROW AND INTERVAL 5 YEAR FOLLOWING),STDDEV_SAMP(a) OVER (ORDER BY pk RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),VAR_SAMP(a) OVER (ORDER BY pk RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING),STDDEV_POP(b) OVER (),VAR_POP(a) OVER (),JSON_ARRAYAGG(e) OVER (ORDER BY e RANGE INTERVAL 4 YEAR PRECEDING),JSON_OBJECTAGG(pk,e) OVER (ORDER BY pk RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM split_window_rpl.t1;
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_window_rpl/t3b/ -> Table(1:974~1:993) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:80~1:83) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:148~1:153) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:233~1:240) /test/1/catalog1/schema1/BIT_AND/
行为 CALL Function(1:310~1:316) /test/1/catalog1/schema1/BIT_OR/
行为 CALL Function(1:411~1:418) /test/1/catalog1/schema1/BIT_XOR/
行为 CALL Function(1:444~1:447) /test/1/catalog1/schema1/MIN/
行为 CALL Function(1:501~1:504) /test/1/catalog1/schema1/MAX/
行为 CALL Function(1:582~1:593) /test/1/catalog1/schema1/STDDEV_SAMP/
行为 CALL Function(1:674~1:682) /test/1/catalog1/schema1/VAR_SAMP/
行为 CALL Function(1:763~1:773) /test/1/catalog1/schema1/STDDEV_POP/
行为 CALL Function(1:785~1:792) /test/1/catalog1/schema1/VAR_POP/
行为 CALL Function(1:804~1:817) /test/1/catalog1/schema1/JSON_ARRAYAGG/
行为 CALL Function(1:871~1:885) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  INSERT INTO split_window_rpl.t2 SELECT ROW_NUMBER() OVER w1 AS c1,RANK() OVER w1 AS c2,DENSE_RANK() OVER w1 AS c3,NTILE(3) OVER w1 AS c4,PERCENT_RANK() OVER w1 AS c5,CUME_DIST() OVER w1 AS c6 FROM split_window_rpl.t1 WINDOW w1 AS (PARTITION BY c ORDER BY f);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:197~1:216) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:49) /test/1/catalog1/schema1/ROW_NUMBER/
行为 CALL Function(1:66~1:70) /test/1/catalog1/schema1/RANK/
行为 CALL Function(1:87~1:97) /test/1/catalog1/schema1/DENSE_RANK/
行为 CALL Function(1:114~1:119) /test/1/catalog1/schema1/NTILE/
行为 CALL Function(1:137~1:149) /test/1/catalog1/schema1/PERCENT_RANK/
行为 CALL Function(1:166~1:175) /test/1/catalog1/schema1/CUME_DIST/
------
SQL  INSERT INTO split_window_rpl.t2 SELECT ROW_NUMBER() OVER (PARTITION BY b) AS c1,RANK() OVER (PARTITION BY a ORDER BY e) AS c2,DENSE_RANK() OVER (PARTITION BY a ORDER BY i) AS c3,NTILE(3) OVER (ORDER BY b) AS c4,PERCENT_RANK() OVER (ORDER BY a,b,c,d) AS c5,CUME_DIST() OVER (ORDER BY e,f,g,h,i) AS c6 FROM split_window_rpl.t1;
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:305~1:324) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:49) /test/1/catalog1/schema1/ROW_NUMBER/
行为 CALL Function(1:80~1:84) /test/1/catalog1/schema1/RANK/
行为 CALL Function(1:126~1:136) /test/1/catalog1/schema1/DENSE_RANK/
行为 CALL Function(1:178~1:183) /test/1/catalog1/schema1/NTILE/
行为 CALL Function(1:211~1:223) /test/1/catalog1/schema1/PERCENT_RANK/
行为 CALL Function(1:256~1:265) /test/1/catalog1/schema1/CUME_DIST/
------
SQL  INSERT INTO split_window_big.cpy SELECT d,SUM(d) OVER w AS summ,NTH_VALUE(d,3) OVER w AS nth,LAG(d,20) OVER w AS lagg FROM split_window_big.td WINDOW w AS (ORDER BY d ROWS BETWEEN 10 PRECEDING AND 10 FOLLOWING);
行为 INSERT Table(1:12~1:32) /test/1/catalog1/split_window_big/cpy/ -> Table(1:123~1:142) /test/1/catalog1/split_window_big/td/
行为 CALL Function(1:42~1:45) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:64~1:73) /test/1/catalog1/schema1/NTH_VALUE/
行为 CALL Function(1:93~1:96) /test/1/catalog1/schema1/LAG/
------
SQL  INSERT INTO split_window_rpl.t4 SELECT FIRST_VALUE(a) OVER w1,FIRST_VALUE(b) OVER w1,FIRST_VALUE(c) OVER w1,FIRST_VALUE(d) OVER w1,FIRST_VALUE(e) OVER w1,FIRST_VALUE(f) OVER w1,FIRST_VALUE(g) OVER (PARTITION BY b ORDER BY pk ROWS 2 PRECEDING),FIRST_VALUE(h) OVER (PARTITION BY b ORDER BY pk RANGE UNBOUNDED PRECEDING) FROM split_window_rpl.t1 WINDOW w1 AS (PARTITION BY b ORDER BY pk);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:323~1:342) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:50) /test/1/catalog1/schema1/FIRST_VALUE/
------
SQL  INSERT INTO split_window_rpl.t4 SELECT LAST_VALUE(a) OVER w1,LAST_VALUE(b) OVER w1,LAST_VALUE(c) OVER w1,LAST_VALUE(d) OVER w2,LAST_VALUE(e) OVER w2,LAST_VALUE(f) OVER w1,LAST_VALUE(g) OVER w2,LAST_VALUE(h) OVER w1 FROM split_window_rpl.t1 WINDOW w1 AS (PARTITION BY c ORDER BY d ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING),w2 AS (ORDER BY c,d);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:220~1:239) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:49) /test/1/catalog1/schema1/LAST_VALUE/
------
SQL  INSERT INTO split_window_rpl.t4 SELECT NTH_VALUE(a,1) OVER w1,NTH_VALUE(b,2) OVER w1,NTH_VALUE(c,3) OVER w1,NTH_VALUE(d,1) OVER w1,NTH_VALUE(e,2) OVER w1,NTH_VALUE(f,3) OVER w1,NTH_VALUE(g,1) OVER (PARTITION BY b ORDER BY pk ROWS 2 PRECEDING),NTH_VALUE(h,2) OVER (PARTITION BY b ORDER BY pk RANGE BETWEEN UNBOUNDED PRECEDING AND 1 FOLLOWING) FROM split_window_rpl.t1 WINDOW w1 AS (PARTITION BY b ORDER BY pk);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:347~1:366) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:48) /test/1/catalog1/schema1/NTH_VALUE/
------
SQL  INSERT INTO split_window_rpl.t4 SELECT LEAD(a,1,3) OVER w1,LEAD(b,2) OVER w1,LEAD(c) OVER w1,LEAD(d,1) OVER w1,LEAD(e,2) OVER w1,LEAD(f,1) OVER w1,LEAD(g,1) OVER w1,LEAD(h,2) OVER w1 FROM split_window_rpl.t1 WINDOW w1 AS (PARTITION BY b ORDER BY pk);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:188~1:207) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:43) /test/1/catalog1/schema1/LEAD/
------
SQL  INSERT INTO split_window_rpl.t4 SELECT LAG(a,1,2) OVER w2,LAG(b,2) OVER w1,LAG(c) OVER w1,LAG(d,1) OVER w2,LAG(e,2) OVER w1,LAG(f,1) OVER w2,LAG(g,1) OVER w1,LAG(h,2) OVER w3 FROM split_window_rpl.t1 WINDOW w1 AS (ORDER BY pk),w2 AS (PARTITION BY g ORDER BY h),w3 AS (PARTITION BY d ORDER BY NULL);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:180~1:199) /test/1/catalog1/split_window_rpl/t1/
行为 CALL Function(1:39~1:42) /test/1/catalog1/schema1/LAG/
------
SQL  INSERT INTO split_dml_dst PARTITION(p0)(id,v)\n  WITH c AS (SELECT id,v FROM split_dml_src WHERE id<100)\n  SELECT id,v FROM c ORDER BY id LIMIT 2\n  ON DUPLICATE KEY UPDATE v=split_dml_dst.v+1;
行为 MERGE Table(1:12~1:25) /test/1/catalog1/schema1/split_dml_dst/ -> Table(2:30~2:43) /test/1/catalog1/schema1/split_dml_src/
------
SQL  INSERT IGNORE INTO split_type_enum_set.es_roles(id,roles)\n    SELECT id,s_basic FROM split_type_enum_set.es_core;
行为 INSERT Table(1:19~1:47) /test/1/catalog1/split_type_enum_set/es_roles/ -> Table(2:27~2:54) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  INSERT INTO t2 SELECT * FROM t1 FOR UPDATE NOWAIT;
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t2/ -> Table(1:29~1:31) /test/1/catalog1/schema1/t1/
------
SQL  INSERT IGNORE INTO dst SELECT * FROM src FOR UPDATE SKIP LOCKED;
行为 INSERT Table(1:19~1:22) /test/1/catalog1/schema1/dst/ -> Table(1:37~1:40) /test/1/catalog1/schema1/src/
------
SQL  INSERT IGNORE INTO dst SELECT s.id FROM src AS s FOR SHARE OF s NOWAIT;
行为 INSERT Table(1:19~1:22) /test/1/catalog1/schema1/dst/ -> Table(1:40~1:43) /test/1/catalog1/schema1/src/
------
SQL  LOAD DATA FROM INFILE '/var/lib/mysql-files/split_load.tsv' INTO TABLE load_innodb;
行为 IMPORT Table(1:71~1:82) /test/1/catalog1/schema1/load_innodb/ -> File(1:22~1:59) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD DATA FROM URL 'https://example.invalid/split.csv' INTO TABLE load_innodb;
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:19~1:54) /test/1/https:/example.invalid/split.csv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.tsv' COUNT 2 IN PRIMARY KEY ORDER INTO TABLE load_innodb;
行为 IMPORT Table(1:95~1:106) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.tsv' INTO TABLE load_innodb ALGORITHM=BULK;
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD XML FROM INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb;
行为 IMPORT Table(1:70~1:81) /test/1/catalog1/schema1/load_innodb/ -> File(1:21~1:58) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML FROM URL 'https://example.invalid/split.xml' INTO TABLE load_innodb;
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:18~1:53) /test/1/https:/example.invalid/split.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' COUNT 2 IN PRIMARY KEY ORDER INTO TABLE load_innodb;
行为 IMPORT Table(1:94~1:105) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb ALGORITHM=BULK;
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD DATA FROM LOCAL INFILE '/tmp/x.csv' INTO TABLE t;
行为 IMPORT Table(1:52~1:53) /test/1/catalog1/schema1/t/ -> File(1:28~1:40) /test/1/tmp/x.csv/
------
SQL  LOAD XML FROM LOCAL INFILE '/tmp/x.xml' INTO TABLE t;
行为 IMPORT Table(1:51~1:52) /test/1/catalog1/schema1/t/ -> File(1:27~1:39) /test/1/tmp/x.xml/
------
SQL  WITH c AS (SELECT id FROM t2 WHERE id < 0) DELETE a FROM t1 AS a JOIN c ON a.id = c.id;
行为 DELETE Table(1:50~1:51) /test/1/catalog1/schema1/a/ -> [Table(1:26~1:28) /test/1/catalog1/schema1/t2/ ; Table(1:57~1:59) /test/1/catalog1/schema1/t1/]
------
SQL  REPLACE INTO table2 (id, name) WITH cte AS (SELECT id, name FROM table1) SELECT id, name FROM cte;
行为 MERGE Table(1:13~1:19) /test/1/catalog1/schema1/table2/ -> Table(1:65~1:71) /test/1/catalog1/schema1/table1/
------
SQL  REPLACE INTO target TABLE src;
行为 MERGE Table(1:13~1:19) /test/1/catalog1/schema1/target/ -> Table(1:26~1:29) /test/1/catalog1/schema1/src/
------
SQL  REPLACE INTO split_dml_dst PARTITION(p0)(id,v)\n  WITH c AS (SELECT id,v FROM split_dml_src WHERE id<100)\n  SELECT id,v FROM c ORDER BY id LIMIT 1;
行为 MERGE Table(1:13~1:26) /test/1/catalog1/schema1/split_dml_dst/ -> Table(2:30~2:43) /test/1/catalog1/schema1/split_dml_src/
------
SQL  REPLACE INTO dst SELECT * FROM src FOR UPDATE SKIP LOCKED;
行为 MERGE Table(1:13~1:16) /test/1/catalog1/schema1/dst/ -> Table(1:31~1:34) /test/1/catalog1/schema1/src/
------
SQL  REPLACE INTO dst (id,v) TABLE src;
行为 MERGE Table(1:13~1:16) /test/1/catalog1/schema1/dst/ -> Table(1:30~1:33) /test/1/catalog1/schema1/src/
------
SQL  UPDATE t3 SET x=CASE WHEN EXISTS (SELECT ref_2.column2 AS c0 FROM t2 AS ref_2 WINDOW w AS (PARTITION BY ref_2.column3 ORDER BY ref_2.column4 DESC)) THEN NULL ELSE '24' END WHERE EXISTS (SELECT DISTINCT * FROM t1);
行为 UPDATE Table(1:7~1:9) /test/1/catalog1/schema1/t3/ -> [Table(1:66~1:68) /test/1/catalog1/schema1/t2/ ; Table(1:209~1:211) /test/1/catalog1/schema1/t1/]
------
SQL  with tab1Cnt as (select * from table1)\n                    update table2 set cc = 1 where id1 in (select id_1 from tab1Cnt) or id2 in (select id_2 from table3);
行为 UPDATE Table(2:27~2:33) /test/1/catalog1/schema1/table2/ -> [Table(1:31~1:37) /test/1/catalog1/schema1/table1/ ; Table(2:113~2:119) /test/1/catalog1/schema1/table3/]
------
SQL  UPDATE split_window_rpl.t2 SET c1=c1+1 WHERE c1 NOT IN (SELECT ROW_NUMBER() OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:89~1:93) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:63~1:73) /test/1/catalog1/schema1/ROW_NUMBER/
------
SQL  UPDATE split_window_rpl.t2 SET c2=c2+2 WHERE c2 IN (SELECT RANK() OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:79~1:83) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:59~1:63) /test/1/catalog1/schema1/RANK/
------
SQL  UPDATE split_window_rpl.t2 SET c3=c3+1 WHERE c3 IN (SELECT CUME_DIST() OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t2/ -> Table(1:84~1:88) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:59~1:68) /test/1/catalog1/schema1/CUME_DIST/
------
SQL  UPDATE split_window_rpl.t4 SET c1=100 WHERE c1 IN (SELECT FIRST_VALUE(221) OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:88~1:92) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:58~1:69) /test/1/catalog1/schema1/FIRST_VALUE/
------
SQL  UPDATE split_window_rpl.t4 SET c2=100 WHERE c2=ANY (SELECT LAST_VALUE(2) OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:86~1:90) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:59~1:69) /test/1/catalog1/schema1/LAST_VALUE/
------
SQL  UPDATE split_window_rpl.t4 SET c3='Updated' WHERE c3=SOME (SELECT NTH_VALUE('Blue',1) OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:99~1:103) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:66~1:75) /test/1/catalog1/schema1/NTH_VALUE/
------
SQL  UPDATE split_window_rpl.t4 SET c4='Updated' WHERE c4=ALL (SELECT LEAD('Russia',0) OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:95~1:99) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:65~1:69) /test/1/catalog1/schema1/LEAD/
------
SQL  UPDATE split_window_rpl.t4 SET c5='2017-01-01' WHERE c5 IN (SELECT LAG('2010-09-20',0) OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:100~1:104) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:67~1:70) /test/1/catalog1/schema1/LAG/
------
SQL  UPDATE split_window_rpl.t4 SET c6='2017-01-01 12:30:45' WHERE c6 IN (SELECT CAST(FIRST_VALUE('2007-08-27 14:06:53') OVER () AS DATETIME) FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:142~1:146) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:76~1:80) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:81~1:92) /test/1/catalog1/schema1/FIRST_VALUE/
------
SQL  UPDATE split_window_rpl.t4 SET c7='Updated' WHERE c7 IN (SELECT LAST_VALUE('Alpha') OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:97~1:101) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:64~1:74) /test/1/catalog1/schema1/LAST_VALUE/
------
SQL  UPDATE split_window_rpl.t4 SET c8='Updated' WHERE c8 IN (SELECT NTH_VALUE('BlobC',1) OVER () FROM DUAL);
行为 UPDATE Table(1:7~1:26) /test/1/catalog1/split_window_rpl/t4/ -> Table(1:98~1:102) /test/1/catalog1/schema1/DUAL/
行为 CALL Function(1:64~1:73) /test/1/catalog1/schema1/NTH_VALUE/
------
SQL  EXPLAIN WITH qn AS (SELECT a+2 AS a,b FROM t2) UPDATE t1 SET t1.a=(SELECT qn.a+10 FROM qn WHERE t1.a-qn.a=0 LIMIT 1);
行为 UPDATE Table(1:54~1:56) /test/1/catalog1/schema1/t1/ -> Table(1:43~1:45) /test/1/catalog1/schema1/t2/
------
SQL  EXPLAIN WITH qn AS (SELECT a+2 AS a,b FROM t2) DELETE /*+ NO_MERGE(qn) */ t1 FROM t1,qn WHERE t1.a-qn.a=0;
行为 DELETE Table(1:74~1:76) /test/1/catalog1/schema1/t1/ -> [Table(1:43~1:45) /test/1/catalog1/schema1/t2/ ; Table(1:82~1:84) /test/1/catalog1/schema1/t1/]
------
SQL  EXPLAIN WITH qn AS (SELECT a+2 AS a,b FROM t2) DELETE FROM t1 WHERE t1.a=(SELECT qn.a FROM qn WHERE t1.a-qn.a=0 LIMIT 1);
行为 DELETE Table(1:59~1:61) /test/1/catalog1/schema1/t1/ -> Table(1:43~1:45) /test/1/catalog1/schema1/t2/
------
SQL  EXPLAIN WITH cte AS (SELECT alias1.col_time_key AS field1 FROM cc AS alias1 LEFT OUTER JOIN view_c AS alias2 ON alias1.col_varchar_key=alias2.col_blob_key WHERE alias2.col_varchar_key>='n' ORDER BY field1 LIMIT 1000 OFFSET 9) DELETE FROM outr1.*,outr2.* USING c AS outr1 RIGHT OUTER JOIN c AS outr2 ON outr1.col_blob_key=outr2.col_blob RIGHT JOIN cte AS outrcte ON outr2.col_blob=outrcte.field1 WHERE outr1.col_blob_key<>(SELECT DISTINCT innr1.col_blob AS y FROM bb AS innr1 LEFT JOIN cte AS innrcte ON innr1.pk<>innrcte.field1);
行为 DELETE Table(1:238~1:243) /test/1/catalog1/schema1/outr1/ -> [Table(1:63~1:65) /test/1/catalog1/schema1/cc/ ; Table(1:92~1:98) /test/1/catalog1/schema1/view_c/ ; Table(1:260~1:261) /test/1/catalog1/schema1/c/ ; Table(1:463~1:465) /test/1/catalog1/schema1/bb/]
行为 DELETE Table(1:246~1:251) /test/1/catalog1/schema1/outr2/
------
SQL  EXPLAIN SELECT 1 INTO DUMPFILE '/tmp/codex_parser_file1' FROM DUAL INTO DUMPFILE '/tmp/codex_parser_file2';
行为 EXPORT File(1:31~1:56) /test/1/tmp/codex_parser_file1/ -> Table(1:62~1:66) /test/1/catalog1/schema1/DUAL/
行为 EXPORT File(1:81~1:106) /test/1/tmp/codex_parser_file2/
------
SQL  EXPLAIN SELECT 1 INTO OUTFILE '/tmp/codex_parser_file3' FROM DUAL INTO OUTFILE '/tmp/codex_parser_file4';
行为 EXPORT File(1:30~1:55) /test/1/tmp/codex_parser_file3/ -> Table(1:61~1:65) /test/1/catalog1/schema1/DUAL/
行为 EXPORT File(1:79~1:104) /test/1/tmp/codex_parser_file4/
------
SQL  SELECT 1 INTO DUMPFILE '/tmp/codex_parser_plain1' FROM DUAL INTO DUMPFILE '/tmp/codex_parser_plain2';
行为 EXPORT File(1:23~1:49) /test/1/tmp/codex_parser_plain1/ -> Table(1:55~1:59) /test/1/catalog1/schema1/DUAL/
行为 EXPORT File(1:74~1:100) /test/1/tmp/codex_parser_plain2/
------
SQL  SELECT 1 INTO OUTFILE '/tmp/codex_parser_plain3' FROM DUAL INTO OUTFILE '/tmp/codex_parser_plain4';
行为 EXPORT File(1:22~1:48) /test/1/tmp/codex_parser_plain3/ -> Table(1:54~1:58) /test/1/catalog1/schema1/DUAL/
行为 EXPORT File(1:72~1:98) /test/1/tmp/codex_parser_plain4/
------
SQL  CREATE INDEX idx_name ON employees(name);
行为 CREATE Index(1:13~1:21) /test/1/catalog1/schema1/idx_name/ -> Table(1:25~1:34) /test/1/catalog1/schema1/employees/
------
SQL  GRANT SELECT(id, name) ON split84.t TO 'u84'@'%';
行为 GRANT Table(1:26~1:35) /test/1/catalog1/split84/t/ -> UserOrRole(1:39~1:48) /test/1/u84@%/
------
SQL  GRANT 'r84' TO 'u84'@'%' WITH ADMIN OPTION;
行为 GRANT Role(1:6~1:11) /test/1/r84/ -> UserOrRole(1:15~1:24) /test/1/u84@%/
------
SQL  SET DEFAULT ROLE 'r84' TO 'u84'@'%';
行为 ALTER User(1:26~1:35) /test/1/u84@%/ -> Role(1:17~1:22) /test/1/r84/
------
SQL  REVOKE IF EXISTS SELECT(id) ON split84.t FROM 'u84'@'%';
行为 REVOKE Table(1:31~1:40) /test/1/catalog1/split84/t/ -> UserOrRole(1:46~1:55) /test/1/u84@%/
------
SQL  REVOKE 'r84' FROM 'u84'@'%';
行为 REVOKE Role(1:7~1:12) /test/1/r84/ -> UserOrRole(1:18~1:27) /test/1/u84@%/
------
SQL  GRANT SELECT, INSERT(note), UPDATE ON split_acl84.t TO 'split_acl_84'@'%';
行为 GRANT Table(1:38~1:51) /test/1/catalog1/split_acl84/t/ -> UserOrRole(1:55~1:73) /test/1/split_acl_84@%/
------
SQL  GRANT EXECUTE ON PROCEDURE split_acl84.p TO 'split_acl_84'@'%';
行为 GRANT Procedure(1:27~1:40) /test/1/catalog1/split_acl84/p/ -> UserOrRole(1:44~1:62) /test/1/split_acl_84@%/
------
SQL  GRANT EXECUTE ON FUNCTION split_acl84.f TO 'split_acl_84'@'%';
行为 GRANT Function(1:26~1:39) /test/1/catalog1/split_acl84/f/ -> UserOrRole(1:43~1:61) /test/1/split_acl_84@%/
------
SQL  GRANT ALL PRIVILEGES ON split_acl84.* TO 'split_acl_84'@'%' WITH GRANT OPTION;
行为 GRANT Schema(1:24~1:35) /test/1/catalog1/split_acl84/ -> UserOrRole(1:41~1:59) /test/1/split_acl_84@%/
------
SQL  GRANT PROXY ON 'root'@'%' TO 'split_acl_84'@'%' WITH GRANT OPTION;
行为 GRANT UserOrRole(1:15~1:25) /test/1/root@%/ -> UserOrRole(1:29~1:47) /test/1/split_acl_84@%/
------
SQL  REVOKE SELECT, INSERT(note), UPDATE ON split_acl84.t FROM 'split_acl_84'@'%';
行为 REVOKE Table(1:39~1:52) /test/1/catalog1/split_acl84/t/ -> UserOrRole(1:58~1:76) /test/1/split_acl_84@%/
------
SQL  REVOKE EXECUTE ON PROCEDURE split_acl84.p FROM 'split_acl_84'@'%';
行为 REVOKE Procedure(1:28~1:41) /test/1/catalog1/split_acl84/p/ -> UserOrRole(1:47~1:65) /test/1/split_acl_84@%/
------
SQL  REVOKE EXECUTE ON FUNCTION split_acl84.f FROM 'split_acl_84'@'%';
行为 REVOKE Function(1:27~1:40) /test/1/catalog1/split_acl84/f/ -> UserOrRole(1:46~1:64) /test/1/split_acl_84@%/
------
SQL  REVOKE PROXY ON 'root'@'%' FROM 'split_acl_84'@'%';
行为 REVOKE UserOrRole(1:16~1:26) /test/1/root@%/ -> UserOrRole(1:32~1:50) /test/1/split_acl_84@%/
------
SQL  RENAME USER 'cdra84a'@'localhost' TO 'cdra84c'@'localhost', 'cdra84b'@'localhost' TO 'cdra84d'@'localhost';
行为 RENAME User(1:12~1:33) /test/1/cdra84a@localhost/ -> User(1:37~1:58) /test/1/cdra84c@localhost/
行为 RENAME User(1:60~1:81) /test/1/cdra84b@localhost/ -> User(1:85~1:106) /test/1/cdra84d@localhost/
------
SQL  GRANT 'split_role_84_a','split_role_84_b' TO 'split_role_u84'@'%' WITH ADMIN OPTION;
行为 GRANT Role(1:6~1:23) /test/1/split_role_84_a/ -> UserOrRole(1:45~1:65) /test/1/split_role_u84@%/
行为 GRANT Role(1:24~1:41) /test/1/split_role_84_b/ -> UserOrRole(1:45~1:65) /test/1/split_role_u84@%/
------
SQL  SET DEFAULT ROLE 'split_role_84_a','split_role_84_b' TO 'split_role_u84'@'%';
行为 ALTER User(1:56~1:76) /test/1/split_role_u84@%/ -> [Role(1:17~1:34) /test/1/split_role_84_a/ ; Role(1:35~1:52) /test/1/split_role_84_b/]
------
SQL  ALTER USER 'split_role_u84'@'%' DEFAULT ROLE 'split_role_84_a','split_role_84_b';
行为 ALTER User(1:11~1:31) /test/1/split_role_u84@%/ -> [Role(1:45~1:62) /test/1/split_role_84_a/ ; Role(1:63~1:80) /test/1/split_role_84_b/]
------
SQL  REVOKE 'split_role_84_a','split_role_84_b' FROM 'split_role_u84'@'%';
行为 REVOKE Role(1:7~1:24) /test/1/split_role_84_a/ -> UserOrRole(1:48~1:68) /test/1/split_role_u84@%/
行为 REVOKE Role(1:25~1:42) /test/1/split_role_84_b/ -> UserOrRole(1:48~1:68) /test/1/split_role_u84@%/
------
SQL  RENAME USER 'split_life_84_b'@'localhost' TO 'split_life_84_c'@'localhost';
行为 RENAME User(1:12~1:41) /test/1/split_life_84_b@localhost/ -> User(1:45~1:74) /test/1/split_life_84_c@localhost/
------
SQL  GRANT ALLOW_NONEXISTENT_DEFINER, APPLICATION_PASSWORD_ADMIN, AUDIT_ABORT_EXEMPT, AUDIT_ADMIN, AUTHENTICATION_POLICY_ADMIN, BACKUP_ADMIN, BINLOG_ADMIN, BINLOG_ENCRYPTION_ADMIN, CLONE_ADMIN, CONNECTION_ADMIN, ENCRYPTION_KEY_ADMIN, FIREWALL_EXEMPT, FLUSH_OPTIMIZER_COSTS, FLUSH_PRIVILEGES, FLUSH_STATUS, FLUSH_TABLES, FLUSH_USER_RESOURCES, GROUP_REPLICATION_ADMIN, GROUP_REPLICATION_STREAM, INNODB_REDO_LOG_ENABLE, OPTIMIZE_LOCAL_TABLE, PASSWORDLESS_USER_ADMIN, PERSIST_RO_VARIABLES_ADMIN, REPLICATION_APPLIER, REPLICATION_SLAVE_ADMIN, RESOURCE_GROUP_ADMIN, RESOURCE_GROUP_USER, ROLE_ADMIN, SENSITIVE_VARIABLES_OBSERVER, SERVICE_CONNECTION_ADMIN, SESSION_VARIABLES_ADMIN, SET_ANY_DEFINER, SHOW_ROUTINE, SYSTEM_USER, SYSTEM_VARIABLES_ADMIN, TABLE_ENCRYPTION_ADMIN, TELEMETRY_LOG_ADMIN, TRANSACTION_GTID_TAG, XA_RECOVER_ADMIN ON *.* TO 'dyn_all_84'@'%';
行为 GRANT Instance(1:824~1:827) /test/1/ -> UserOrRole(1:831~1:847) /test/1/dyn_all_84@%/
------
SQL  REVOKE ALLOW_NONEXISTENT_DEFINER, APPLICATION_PASSWORD_ADMIN, AUDIT_ABORT_EXEMPT, AUDIT_ADMIN, AUTHENTICATION_POLICY_ADMIN, BACKUP_ADMIN, BINLOG_ADMIN, BINLOG_ENCRYPTION_ADMIN, CLONE_ADMIN, CONNECTION_ADMIN, ENCRYPTION_KEY_ADMIN, FIREWALL_EXEMPT, FLUSH_OPTIMIZER_COSTS, FLUSH_PRIVILEGES, FLUSH_STATUS, FLUSH_TABLES, FLUSH_USER_RESOURCES, GROUP_REPLICATION_ADMIN, GROUP_REPLICATION_STREAM, INNODB_REDO_LOG_ENABLE, OPTIMIZE_LOCAL_TABLE, PASSWORDLESS_USER_ADMIN, PERSIST_RO_VARIABLES_ADMIN, REPLICATION_APPLIER, REPLICATION_SLAVE_ADMIN, RESOURCE_GROUP_ADMIN, RESOURCE_GROUP_USER, ROLE_ADMIN, SENSITIVE_VARIABLES_OBSERVER, SERVICE_CONNECTION_ADMIN, SESSION_VARIABLES_ADMIN, SET_ANY_DEFINER, SHOW_ROUTINE, SYSTEM_USER, SYSTEM_VARIABLES_ADMIN, TABLE_ENCRYPTION_ADMIN, TELEMETRY_LOG_ADMIN, TRANSACTION_GTID_TAG, XA_RECOVER_ADMIN ON *.* FROM 'dyn_all_84'@'%';
行为 REVOKE Instance(1:825~1:828) /test/1/ -> UserOrRole(1:834~1:850) /test/1/dyn_all_84@%/
------
SQL  GRANT ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* TO 'static_84'@'%';
行为 GRANT Instance(1:346~1:349) /test/1/ -> UserOrRole(1:353~1:368) /test/1/static_84@%/
------
SQL  REVOKE ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* FROM 'static_84'@'%';
行为 REVOKE Instance(1:347~1:350) /test/1/ -> UserOrRole(1:356~1:371) /test/1/static_84@%/
------
SQL  GRANT FIREWALL_ADMIN, FIREWALL_USER, INNODB_REDO_LOG_ARCHIVE, MASKING_DICTIONARIES_ADMIN, NDB_STORED_USER, SKIP_QUERY_REWRITE, TP_CONNECTION_ADMIN, VERSION_TOKEN_ADMIN ON *.* TO 'env_acl_84'@'%';
行为 GRANT Instance(1:171~1:174) /test/1/ -> UserOrRole(1:178~1:194) /test/1/env_acl_84@%/
------
SQL  REVOKE FIREWALL_ADMIN, FIREWALL_USER, INNODB_REDO_LOG_ARCHIVE, MASKING_DICTIONARIES_ADMIN, NDB_STORED_USER, SKIP_QUERY_REWRITE, TP_CONNECTION_ADMIN, VERSION_TOKEN_ADMIN ON *.* FROM 'env_acl_84'@'%';
行为 REVOKE Instance(1:172~1:175) /test/1/ -> UserOrRole(1:181~1:197) /test/1/env_acl_84@%/
------
SQL  /*!50000 GRANT SELECT ON split_exec_comment.* TO 'split_exec_84'@'%' */;
行为 GRANT Schema(1:25~1:43) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:49~1:68) /test/1/split_exec_84@%/
------
SQL  /*!50000 REVOKE SELECT ON split_exec_comment.* FROM 'split_exec_84'@'%' */;
行为 REVOKE Schema(1:26~1:44) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:52~1:71) /test/1/split_exec_84@%/
------
SQL  CREATE INDEX idx84_name_lower ON split84.idx84_docs ((LOWER(name))) COMMENT 'functional lower name' INVISIBLE;
行为 CREATE Index(1:13~1:29) /test/1/catalog1/schema1/idx84_name_lower/ -> Table(1:33~1:51) /test/1/catalog1/split84/idx84_docs/
行为 CALL Function(1:54~1:59) /test/1/catalog1/schema1/LOWER/
------
SQL  CREATE INDEX idx84_tags_multi ON split84.idx84_docs ((CAST(tags->'$.ids' AS UNSIGNED ARRAY))) VISIBLE;
行为 CREATE Index(1:13~1:29) /test/1/catalog1/schema1/idx84_tags_multi/ -> Table(1:33~1:51) /test/1/catalog1/split84/idx84_docs/
------
SQL  CREATE FULLTEXT INDEX idx84_ft_parser ON split84.idx84_docs (title, body) WITH PARSER ngram COMMENT 'ngram parser';
行为 CREATE Index(1:22~1:37) /test/1/catalog1/schema1/idx84_ft_parser/ -> Table(1:41~1:59) /test/1/catalog1/split84/idx84_docs/
------
SQL  CREATE INDEX idx84_lock_algo ON split84.idx84_docs (created_at DESC, title(12) ASC) ALGORITHM=INPLACE LOCK=NONE;
行为 CREATE Index(1:13~1:28) /test/1/catalog1/schema1/idx84_lock_algo/ -> Table(1:32~1:50) /test/1/catalog1/split84/idx84_docs/
------
SQL  ALTER TABLE split84.idx84_docs ALTER INDEX idx84_name_lower VISIBLE;
行为 ALTER Index(1:43~1:59) /test/1/catalog1/schema1/idx84_name_lower/ -> Table(1:12~1:30) /test/1/catalog1/split84/idx84_docs/
------
SQL  DROP INDEX idx84_lock_algo ON split84.idx84_docs;
行为 DROP Index(1:11~1:26) /test/1/catalog1/schema1/idx84_lock_algo/ -> Table(1:30~1:48) /test/1/catalog1/split84/idx84_docs/
------
SQL  DROP INDEX idx84_ft_parser ON split84.idx84_docs;
行为 DROP Index(1:11~1:26) /test/1/catalog1/schema1/idx84_ft_parser/ -> Table(1:30~1:48) /test/1/catalog1/split84/idx84_docs/
------
SQL  DROP INDEX idx84_tags_multi ON split84.idx84_docs;
行为 DROP Index(1:11~1:27) /test/1/catalog1/schema1/idx84_tags_multi/ -> Table(1:31~1:49) /test/1/catalog1/split84/idx84_docs/
------
SQL  DROP INDEX idx84_name_lower ON split84.idx84_docs;
行为 DROP Index(1:11~1:27) /test/1/catalog1/schema1/idx84_name_lower/ -> Table(1:31~1:49) /test/1/catalog1/split84/idx84_docs/
------
SQL  DROP INDEX idx84_drop_plain ON split84.idx84_drop_docs;
行为 DROP Index(1:11~1:27) /test/1/catalog1/schema1/idx84_drop_plain/ -> Table(1:31~1:54) /test/1/catalog1/split84/idx84_drop_docs/
------
SQL  DROP INDEX idx84_drop_algo ON split84.idx84_drop_docs ALGORITHM=INPLACE LOCK=NONE;
行为 DROP Index(1:11~1:26) /test/1/catalog1/schema1/idx84_drop_algo/ -> Table(1:30~1:53) /test/1/catalog1/split84/idx84_drop_docs/
------
SQL  DROP INDEX idx84_drop_lock_algo ON split84.idx84_drop_docs LOCK DEFAULT ALGORITHM DEFAULT;
行为 DROP Index(1:11~1:31) /test/1/catalog1/schema1/idx84_drop_lock_algo/ -> Table(1:35~1:58) /test/1/catalog1/split84/idx84_drop_docs/
------
SQL  DROP INDEX idx84_drop_copy ON split84.idx84_drop_docs ALGORITHM=COPY LOCK=SHARED;
行为 DROP Index(1:11~1:26) /test/1/catalog1/schema1/idx84_drop_copy/ -> Table(1:30~1:53) /test/1/catalog1/split84/idx84_drop_docs/
------
SQL  DROP INDEX `PRIMARY` ON split84.idx84_drop_pk;
行为 DROP Index(1:11~1:20) /test/1/catalog1/schema1/PRIMARY/ -> Table(1:24~1:45) /test/1/catalog1/split84/idx84_drop_pk/
------
SQL  CREATE VIEW view2 AS SELECT name COLLATE utf8mb3_tolower_ci AS table_name FROM dd_table;
行为 CREATE View(1:12~1:17) /test/1/catalog1/schema1/view2/ -> Table(1:79~1:87) /test/1/catalog1/schema1/dd_table/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER IF NOT EXISTS split84.trg_src_bi\nBEFORE INSERT ON split84.trigger_src\nFOR EACH ROW\nSET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:52~1:70) /test/1/catalog1/split84/trg_src_bi/ -> Table(2:17~2:36) /test/1/catalog1/split84/trigger_src/
行为 CALL Function(4:21~4:38) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split84.trg_src_bi_audit\nBEFORE INSERT ON split84.trigger_src\nFOR EACH ROW FOLLOWS trg_src_bi\nSET NEW.note = COALESCE(NEW.note, 'inserted');
行为 CREATE Trigger(1:15~1:39) /test/1/catalog1/split84/trg_src_bi_audit/ -> Table(2:17~2:36) /test/1/catalog1/split84/trigger_src/
行为 CALL Function(4:15~4:23) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER split84.trg_src_au\nAFTER UPDATE ON split84.trigger_src\nFOR EACH ROW\nBEGIN\n  INSERT INTO split84.trigger_audit(src_id, action_name, old_amount, new_amount)\n  VALUES (OLD.id, 'update', OLD.amount, NEW.amount);\nEND;
行为 CREATE Trigger(1:15~1:33) /test/1/catalog1/split84/trg_src_au/ -> Table(2:16~2:35) /test/1/catalog1/split84/trigger_src/
行为 INSERT Table(5:14~5:35) /test/1/catalog1/split84/trigger_audit/
------
SQL  CREATE TRIGGER split84.trg_order_follows BEFORE INSERT ON split84.trigger_src FOR EACH ROW FOLLOWS trg_order_base SET NEW.note = 'follows';
行为 CREATE Trigger(1:15~1:40) /test/1/catalog1/split84/trg_order_follows/ -> Table(1:58~1:77) /test/1/catalog1/split84/trigger_src/
------
SQL  CREATE TRIGGER split84.trg_order_precedes BEFORE INSERT ON split84.trigger_src FOR EACH ROW PRECEDES trg_order_base SET NEW.note = 'precedes';
行为 CREATE Trigger(1:15~1:41) /test/1/catalog1/split84/trg_order_precedes/ -> Table(1:59~1:78) /test/1/catalog1/split84/trigger_src/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER split84.trg_bi BEFORE INSERT ON split84.trigger_src FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:38~1:52) /test/1/catalog1/split84/trg_bi/ -> Table(1:70~1:89) /test/1/catalog1/split84/trigger_src/
行为 CALL Function(1:124~1:141) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split84.trg_bu BEFORE UPDATE ON split84.trigger_src FOR EACH ROW SET NEW.note = COALESCE(NEW.note, 'before update');
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split84/trg_bu/ -> Table(1:47~1:66) /test/1/catalog1/split84/trigger_src/
行为 CALL Function(1:95~1:103) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER split84.trg_bd BEFORE DELETE ON split84.trigger_src FOR EACH ROW INSERT INTO split84.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'before delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split84/trg_bd/ -> Table(1:47~1:66) /test/1/catalog1/split84/trigger_src/
行为 INSERT Table(1:92~1:113) /test/1/catalog1/split84/trigger_audit/
------
SQL  CREATE TRIGGER split84.trg_ai AFTER INSERT ON split84.trigger_src FOR EACH ROW INSERT INTO split84.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (NEW.id, 'after insert', NULL, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split84/trg_ai/ -> Table(1:46~1:65) /test/1/catalog1/split84/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split84/trigger_audit/
------
SQL  CREATE TRIGGER split84.trg_au AFTER UPDATE ON split84.trigger_src FOR EACH ROW INSERT INTO split84.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after update', OLD.amount, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split84/trg_au/ -> Table(1:46~1:65) /test/1/catalog1/split84/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split84/trigger_audit/
------
SQL  CREATE TRIGGER split84.trg_ad AFTER DELETE ON split84.trigger_src FOR EACH ROW INSERT INTO split84.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split84/trg_ad/ -> Table(1:46~1:65) /test/1/catalog1/split84/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split84/trigger_audit/
------
SQL  CREATE INDEX idx_t84_name_lc ON t84_types (name_lc DESC) COMMENT 'generated column index';
行为 CREATE Index(1:13~1:28) /test/1/catalog1/schema1/idx_t84_name_lc/ -> Table(1:32~1:41) /test/1/catalog1/schema1/t84_types/
------
SQL  CREATE OR REPLACE ALGORITHM=MERGE SQL SECURITY INVOKER VIEW v84_checked AS SELECT id, name, val FROM t84_types WHERE val >= 0 WITH CASCADED CHECK OPTION;
行为 REPLACE View(1:60~1:71) /test/1/catalog1/schema1/v84_checked/ -> Table(1:101~1:110) /test/1/catalog1/schema1/t84_types/
------
SQL  CREATE OR REPLACE ALGORITHM = TEMPTABLE DEFINER = CURRENT_USER SQL SECURITY DEFINER VIEW split84.v84_base (id, amount_label) AS\nSELECT id, CONCAT('amt:', amount) FROM split84.trigger_src WHERE amount >= 0;
行为 REPLACE View(1:89~1:105) /test/1/catalog1/split84/v84_base/ -> Table(2:39~2:58) /test/1/catalog1/split84/trigger_src/
行为 CALL Function(2:11~2:17) /test/1/catalog1/schema1/CONCAT/
------
SQL  ALTER ALGORITHM = MERGE SQL SECURITY INVOKER VIEW split84.v84_base (id, amount) AS\nSELECT id, amount FROM split84.trigger_src WHERE amount BETWEEN 0 AND 100 WITH LOCAL CHECK OPTION;
行为 ALTER View(1:50~1:66) /test/1/catalog1/split84/v84_base/ -> Table(2:23~2:42) /test/1/catalog1/split84/trigger_src/
------
SQL  CREATE VIEW split84.v84_joined AS\nSELECT s.id, a.action_name\nFROM split84.trigger_src AS s\nLEFT JOIN split84.trigger_audit AS a ON a.src_id = s.id;
行为 CREATE View(1:12~1:30) /test/1/catalog1/split84/v84_joined/ -> [Table(3:5~3:24) /test/1/catalog1/split84/trigger_src/ ; Table(4:10~4:31) /test/1/catalog1/split84/trigger_audit/]
------
SQL  CREATE VIEW split84.v84_table_stmt AS TABLE split84.trigger_src;
行为 CREATE View(1:12~1:34) /test/1/catalog1/split84/v84_table_stmt/ -> Table(1:44~1:63) /test/1/catalog1/split84/trigger_src/
------
SQL  CREATE VIEW split84.v84_cte AS\nWITH cte AS (SELECT id, amount FROM split84.trigger_src WHERE amount >= 0)\nSELECT id, amount FROM cte;
行为 CREATE View(1:12~1:27) /test/1/catalog1/split84/v84_cte/ -> Table(2:36~2:55) /test/1/catalog1/split84/trigger_src/
------
SQL  LOAD DATA FROM S3 's3://split-bucket/split.csv' INTO TABLE load_innodb;
行为 IMPORT Table(1:59~1:70) /test/1/catalog1/schema1/load_innodb/ -> File(1:18~1:47) /test/1/s3:/split-bucket/split.csv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.tsv' INTO TABLE load_innodb COMPRESSION='zstd';
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.tsv' INTO TABLE load_innodb PARALLEL=2 MEMORY=1M ALGORITHM=BULK;
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD XML FROM S3 's3://split-bucket/split.xml' INTO TABLE load_innodb;
行为 IMPORT Table(1:58~1:69) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:46) /test/1/s3:/split-bucket/split.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb COMPRESSION='zstd';
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb PARALLEL=2 MEMORY=1M ALGORITHM=BULK;
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_outfile_load_base.txt' INTO TABLE split_outfile_load_native.t2 CHARACTER SET utf8mb3;
行为 IMPORT Table(1:79~1:107) /test/1/catalog1/split_outfile_load_native/t2/ -> File(1:17~1:67) /test/1/var/lib/mysql-files/split_outfile_load_base.txt/
------
SQL  LOAD DATA FROM S3 's3://b/x.csv' INTO TABLE t COMPRESSION='zstd' PARALLEL=2 MEMORY=1M ALGORITHM=BULK;
行为 IMPORT Table(1:44~1:45) /test/1/catalog1/schema1/t/ -> File(1:18~1:32) /test/1/s3:/b/x.csv/
------
SQL  LOAD XML FROM S3 's3://b/x.xml' INTO TABLE t COMPRESSION='zstd' PARALLEL=2 MEMORY=1M ALGORITHM=BULK;
行为 IMPORT Table(1:43~1:44) /test/1/catalog1/schema1/t/ -> File(1:17~1:31) /test/1/s3:/b/x.xml/
------
SQL  SELECT * INTO OUTFILE '/var/lib/mysql-files/split_outfile_load_13.txt' CHARACTER SET utf8mb3 FROM split_outfile_load_native.t1;
行为 EXPORT File(1:22~1:70) /test/1/var/lib/mysql-files/split_outfile_load_13.txt/ -> Table(1:98~1:126) /test/1/catalog1/split_outfile_load_native/t1/
------
SQL  EXPLAIN FORMAT=JSON INTO @plan DELETE t1, t2 FROM t1 JOIN t2 ON t1.id = t2.id;
行为 DELETE Table(1:38~1:40) /test/1/catalog1/schema1/t1/ -> [Table(1:50~1:52) /test/1/catalog1/schema1/t1/ ; Table(1:58~1:60) /test/1/catalog1/schema1/t2/]
行为 READ ConfigKey(1:25~1:30) /test/1/plan/
行为 DELETE Table(1:42~1:44) /test/1/catalog1/schema1/t2/
------
SQL  GRANT SELECT, INSERT(note), UPDATE ON split_acl97.t TO 'split_acl_97'@'%';
行为 GRANT Table(1:38~1:51) /test/1/catalog1/split_acl97/t/ -> UserOrRole(1:55~1:73) /test/1/split_acl_97@%/
------
SQL  GRANT EXECUTE ON PROCEDURE split_acl97.p TO 'split_acl_97'@'%';
行为 GRANT Procedure(1:27~1:40) /test/1/catalog1/split_acl97/p/ -> UserOrRole(1:44~1:62) /test/1/split_acl_97@%/
------
SQL  GRANT EXECUTE ON FUNCTION split_acl97.f TO 'split_acl_97'@'%';
行为 GRANT Function(1:26~1:39) /test/1/catalog1/split_acl97/f/ -> UserOrRole(1:43~1:61) /test/1/split_acl_97@%/
------
SQL  GRANT ALL PRIVILEGES ON split_acl97.* TO 'split_acl_97'@'%' WITH GRANT OPTION;
行为 GRANT Schema(1:24~1:35) /test/1/catalog1/split_acl97/ -> UserOrRole(1:41~1:59) /test/1/split_acl_97@%/
------
SQL  GRANT PROXY ON 'root'@'%' TO 'split_acl_97'@'%' WITH GRANT OPTION;
行为 GRANT UserOrRole(1:15~1:25) /test/1/root@%/ -> UserOrRole(1:29~1:47) /test/1/split_acl_97@%/
------
SQL  REVOKE SELECT, INSERT(note), UPDATE ON split_acl97.t FROM 'split_acl_97'@'%';
行为 REVOKE Table(1:39~1:52) /test/1/catalog1/split_acl97/t/ -> UserOrRole(1:58~1:76) /test/1/split_acl_97@%/
------
SQL  REVOKE EXECUTE ON PROCEDURE split_acl97.p FROM 'split_acl_97'@'%';
行为 REVOKE Procedure(1:28~1:41) /test/1/catalog1/split_acl97/p/ -> UserOrRole(1:47~1:65) /test/1/split_acl_97@%/
------
SQL  REVOKE EXECUTE ON FUNCTION split_acl97.f FROM 'split_acl_97'@'%';
行为 REVOKE Function(1:27~1:40) /test/1/catalog1/split_acl97/f/ -> UserOrRole(1:46~1:64) /test/1/split_acl_97@%/
------
SQL  REVOKE PROXY ON 'root'@'%' FROM 'split_acl_97'@'%';
行为 REVOKE UserOrRole(1:16~1:26) /test/1/root@%/ -> UserOrRole(1:32~1:50) /test/1/split_acl_97@%/
------
SQL  RENAME USER 'cdra97a'@'localhost' TO 'cdra97c'@'localhost', 'cdra97b'@'localhost' TO 'cdra97d'@'localhost';
行为 RENAME User(1:12~1:33) /test/1/cdra97a@localhost/ -> User(1:37~1:58) /test/1/cdra97c@localhost/
行为 RENAME User(1:60~1:81) /test/1/cdra97b@localhost/ -> User(1:85~1:106) /test/1/cdra97d@localhost/
------
SQL  GRANT 'split_role_97_a','split_role_97_b' TO 'split_role_u97'@'%' WITH ADMIN OPTION;
行为 GRANT Role(1:6~1:23) /test/1/split_role_97_a/ -> UserOrRole(1:45~1:65) /test/1/split_role_u97@%/
行为 GRANT Role(1:24~1:41) /test/1/split_role_97_b/ -> UserOrRole(1:45~1:65) /test/1/split_role_u97@%/
------
SQL  SET DEFAULT ROLE 'split_role_97_a','split_role_97_b' TO 'split_role_u97'@'%';
行为 ALTER User(1:56~1:76) /test/1/split_role_u97@%/ -> [Role(1:17~1:34) /test/1/split_role_97_a/ ; Role(1:35~1:52) /test/1/split_role_97_b/]
------
SQL  ALTER USER 'split_role_u97'@'%' DEFAULT ROLE 'split_role_97_a','split_role_97_b';
行为 ALTER User(1:11~1:31) /test/1/split_role_u97@%/ -> [Role(1:45~1:62) /test/1/split_role_97_a/ ; Role(1:63~1:80) /test/1/split_role_97_b/]
------
SQL  REVOKE 'split_role_97_a','split_role_97_b' FROM 'split_role_u97'@'%';
行为 REVOKE Role(1:7~1:24) /test/1/split_role_97_a/ -> UserOrRole(1:48~1:68) /test/1/split_role_u97@%/
行为 REVOKE Role(1:25~1:42) /test/1/split_role_97_b/ -> UserOrRole(1:48~1:68) /test/1/split_role_u97@%/
------
SQL  RENAME USER 'split_life_97_b'@'localhost' TO 'split_life_97_c'@'localhost';
行为 RENAME User(1:12~1:41) /test/1/split_life_97_b@localhost/ -> User(1:45~1:74) /test/1/split_life_97_c@localhost/
------
SQL  GRANT EXECUTE ON LIBRARY codex_library_acl.lib_acl TO 'codex_library_u'@'localhost';
行为 GRANT Library(1:25~1:50) /test/1/catalog1/codex_library_acl/lib_acl/ -> UserOrRole(1:54~1:83) /test/1/codex_library_u@localhost/
------
SQL  REVOKE EXECUTE ON LIBRARY codex_library_acl.lib_acl FROM 'codex_library_u'@'localhost';
行为 REVOKE Library(1:26~1:51) /test/1/catalog1/codex_library_acl/lib_acl/ -> UserOrRole(1:57~1:86) /test/1/codex_library_u@localhost/
------
SQL  GRANT ALLOW_NONEXISTENT_DEFINER, APPLICATION_PASSWORD_ADMIN, AUDIT_ABORT_EXEMPT, AUDIT_ADMIN, AUTHENTICATION_POLICY_ADMIN, BACKUP_ADMIN, BINLOG_ADMIN, BINLOG_ENCRYPTION_ADMIN, CLONE_ADMIN, CONNECTION_ADMIN, CREATE_SPATIAL_REFERENCE_SYSTEM, ENCRYPTION_KEY_ADMIN, FIREWALL_EXEMPT, FLUSH_OPTIMIZER_COSTS, FLUSH_PRIVILEGES, FLUSH_STATUS, FLUSH_TABLES, FLUSH_USER_RESOURCES, GROUP_REPLICATION_ADMIN, GROUP_REPLICATION_STREAM, INNODB_REDO_LOG_ENABLE, MANAGE_DATA_MASKING_POLICY, OPTIMIZE_LOCAL_TABLE, PASSWORDLESS_USER_ADMIN, PERSIST_RO_VARIABLES_ADMIN, REPLICATION_APPLIER, REPLICATION_SLAVE_ADMIN, RESOURCE_GROUP_ADMIN, RESOURCE_GROUP_USER, ROLE_ADMIN, SENSITIVE_VARIABLES_OBSERVER, SERVICE_CONNECTION_ADMIN, SESSION_VARIABLES_ADMIN, SET_ANY_DEFINER, SHOW_ROUTINE, SYSTEM_USER, SYSTEM_VARIABLES_ADMIN, TABLE_ENCRYPTION_ADMIN, TELEMETRY_LOG_ADMIN, TRANSACTION_GTID_TAG, XA_RECOVER_ADMIN ON *.* TO 'dyn_all_97'@'%';
行为 GRANT Instance(1:885~1:888) /test/1/ -> UserOrRole(1:892~1:908) /test/1/dyn_all_97@%/
------
SQL  REVOKE ALLOW_NONEXISTENT_DEFINER, APPLICATION_PASSWORD_ADMIN, AUDIT_ABORT_EXEMPT, AUDIT_ADMIN, AUTHENTICATION_POLICY_ADMIN, BACKUP_ADMIN, BINLOG_ADMIN, BINLOG_ENCRYPTION_ADMIN, CLONE_ADMIN, CONNECTION_ADMIN, CREATE_SPATIAL_REFERENCE_SYSTEM, ENCRYPTION_KEY_ADMIN, FIREWALL_EXEMPT, FLUSH_OPTIMIZER_COSTS, FLUSH_PRIVILEGES, FLUSH_STATUS, FLUSH_TABLES, FLUSH_USER_RESOURCES, GROUP_REPLICATION_ADMIN, GROUP_REPLICATION_STREAM, INNODB_REDO_LOG_ENABLE, MANAGE_DATA_MASKING_POLICY, OPTIMIZE_LOCAL_TABLE, PASSWORDLESS_USER_ADMIN, PERSIST_RO_VARIABLES_ADMIN, REPLICATION_APPLIER, REPLICATION_SLAVE_ADMIN, RESOURCE_GROUP_ADMIN, RESOURCE_GROUP_USER, ROLE_ADMIN, SENSITIVE_VARIABLES_OBSERVER, SERVICE_CONNECTION_ADMIN, SESSION_VARIABLES_ADMIN, SET_ANY_DEFINER, SHOW_ROUTINE, SYSTEM_USER, SYSTEM_VARIABLES_ADMIN, TABLE_ENCRYPTION_ADMIN, TELEMETRY_LOG_ADMIN, TRANSACTION_GTID_TAG, XA_RECOVER_ADMIN ON *.* FROM 'dyn_all_97'@'%';
行为 REVOKE Instance(1:886~1:889) /test/1/ -> UserOrRole(1:895~1:911) /test/1/dyn_all_97@%/
------
SQL  GRANT ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* TO 'static_97'@'%';
行为 GRANT Instance(1:346~1:349) /test/1/ -> UserOrRole(1:353~1:368) /test/1/static_97@%/
------
SQL  REVOKE ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TABLESPACE, CREATE TEMPORARY TABLES, CREATE USER, CREATE VIEW, DELETE, DROP, EVENT, EXECUTE, FILE, GRANT OPTION, INDEX, INSERT, LOCK TABLES, PROCESS, REFERENCES, RELOAD, REPLICATION CLIENT, REPLICATION SLAVE, SELECT, SHOW DATABASES, SHOW VIEW, SHUTDOWN, SUPER, TRIGGER, UPDATE, USAGE ON *.* FROM 'static_97'@'%';
行为 REVOKE Instance(1:347~1:350) /test/1/ -> UserOrRole(1:356~1:371) /test/1/static_97@%/
------
SQL  GRANT EXPORT_QUERY_RESULTS, FIREWALL_ADMIN, FIREWALL_USER, INNODB_REDO_LOG_ARCHIVE, MASKING_DICTIONARIES_ADMIN, NDB_STORED_USER, OPTION_TRACKER_OBSERVER, OPTION_TRACKER_UPDATER, SKIP_QUERY_REWRITE, TP_CONNECTION_ADMIN, VERSION_TOKEN_ADMIN ON *.* TO 'env_acl_97'@'%';
行为 GRANT Instance(1:242~1:245) /test/1/ -> UserOrRole(1:249~1:265) /test/1/env_acl_97@%/
------
SQL  REVOKE EXPORT_QUERY_RESULTS, FIREWALL_ADMIN, FIREWALL_USER, INNODB_REDO_LOG_ARCHIVE, MASKING_DICTIONARIES_ADMIN, NDB_STORED_USER, OPTION_TRACKER_OBSERVER, OPTION_TRACKER_UPDATER, SKIP_QUERY_REWRITE, TP_CONNECTION_ADMIN, VERSION_TOKEN_ADMIN ON *.* FROM 'env_acl_97'@'%';
行为 REVOKE Instance(1:243~1:246) /test/1/ -> UserOrRole(1:252~1:268) /test/1/env_acl_97@%/
------
SQL  /*!50000 GRANT SELECT ON split_exec_comment.* TO 'split_exec_97'@'%' */;
行为 GRANT Schema(1:25~1:43) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:49~1:68) /test/1/split_exec_97@%/
------
SQL  /*!50000 REVOKE SELECT ON split_exec_comment.* FROM 'split_exec_97'@'%' */;
行为 REVOKE Schema(1:26~1:44) /test/1/catalog1/split_exec_comment/ -> UserOrRole(1:52~1:71) /test/1/split_exec_97@%/
------
SQL  CREATE TABLE vector_child(v VECTOR(3),FOREIGN KEY(v) REFERENCES vector_parent(v));
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/vector_child/ -> Table(1:64~1:77) /test/1/catalog1/schema1/vector_parent/
行为 CREATE Constraint(1:38~1:80) /test/1/catalog1/schema1/
------
SQL  CREATE INDEX idx_func_97 ON split_idx.t_modern ((LOWER(name))) INVISIBLE;
行为 CREATE Index(1:13~1:24) /test/1/catalog1/schema1/idx_func_97/ -> Table(1:28~1:46) /test/1/catalog1/split_idx/t_modern/
行为 CALL Function(1:49~1:54) /test/1/catalog1/schema1/LOWER/
------
SQL  CREATE INDEX idx_multi_97 ON split_idx.t_modern ((CAST(j->'$.ids' AS UNSIGNED ARRAY))) VISIBLE;
行为 CREATE Index(1:13~1:25) /test/1/catalog1/schema1/idx_multi_97/ -> Table(1:29~1:47) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_visible_97 ON split_idx.t_modern (id) VISIBLE;
行为 CREATE Index(1:13~1:27) /test/1/catalog1/schema1/idx_visible_97/ -> Table(1:31~1:49) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_invisible_97 ON split_idx.t_modern (name) INVISIBLE;
行为 CREATE Index(1:13~1:29) /test/1/catalog1/schema1/idx_invisible_97/ -> Table(1:33~1:51) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_attrs_97 ON split_idx.t_modern (id) ENGINE_ATTRIBUTE='{}' SECONDARY_ENGINE_ATTRIBUTE='{}';
行为 CREATE Index(1:13~1:25) /test/1/catalog1/schema1/idx_attrs_97/ -> Table(1:29~1:47) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_func_97 ON split_idx.t_modern;
行为 DROP Index(1:11~1:22) /test/1/catalog1/schema1/idx_func_97/ -> Table(1:26~1:44) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_multi_97 ON split_idx.t_modern;
行为 DROP Index(1:11~1:23) /test/1/catalog1/schema1/idx_multi_97/ -> Table(1:27~1:45) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_visible_97 ON split_idx.t_modern;
行为 DROP Index(1:11~1:25) /test/1/catalog1/schema1/idx_visible_97/ -> Table(1:29~1:47) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_invisible_97 ON split_idx.t_modern;
行为 DROP Index(1:11~1:27) /test/1/catalog1/schema1/idx_invisible_97/ -> Table(1:31~1:49) /test/1/catalog1/split_idx/t_modern/
------
SQL  DROP INDEX idx_attrs_97 ON split_idx.t_modern;
行为 DROP Index(1:11~1:23) /test/1/catalog1/schema1/idx_attrs_97/ -> Table(1:27~1:45) /test/1/catalog1/split_idx/t_modern/
------
SQL  CREATE INDEX idx_vector_forbidden ON vector_lifecycle (embedding);
行为 CREATE Index(1:13~1:33) /test/1/catalog1/schema1/idx_vector_forbidden/ -> Table(1:37~1:53) /test/1/catalog1/schema1/vector_lifecycle/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_like LIKE split_keywords.ext_source;
行为 CREATE Table(1:22~1:45) /test/1/catalog1/split_keywords/ext_like/ -> Table(1:51~1:76) /test/1/catalog1/split_keywords/ext_source/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_complex (\n  id INT AUTO_INCREMENT PRIMARY KEY,\n  name VARCHAR(100) NOT NULL,\n  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n  status ENUM('active', 'inactive', 'pending') DEFAULT 'pending',\n  data JSON,\n  INDEX idx_name (name),\n  FULLTEXT idx_data (data)\n);
行为 CREATE Index(7:8~7:16) /test/1/catalog1/schema1/idx_name/ -> Table(1:22~1:48) /test/1/catalog1/split_keywords/ext_complex/
行为 CREATE Index(8:11~8:19) /test/1/catalog1/schema1/idx_data/ -> Table(1:22~1:48) /test/1/catalog1/split_keywords/ext_complex/
行为 CREATE Constraint(2:24~2:35) /test/1/catalog1/schema1/
------
SQL  CREATE EXTERNAL TABLE ext_like_gap (LIKE src);
行为 CREATE Table(1:22~1:34) /test/1/catalog1/schema1/ext_like_gap/ -> Table(1:41~1:44) /test/1/catalog1/schema1/src/
------
SQL  CREATE EXTERNAL TABLE audit_ext_table AS TABLE audit_source;
行为 CREATE Table(1:22~1:37) /test/1/catalog1/schema1/audit_ext_table/ -> Table(1:47~1:59) /test/1/catalog1/schema1/audit_source/
------
SQL  CREATE VIEW vector_view AS\nSELECT id,grp,VECTOR_TO_STRING(embedding) AS embedding_text,VECTOR_DIM(embedding) AS dimensions\nFROM vector_lifecycle;
行为 CREATE View(1:12~1:23) /test/1/catalog1/schema1/vector_view/ -> Table(3:5~3:21) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(2:14~2:30) /test/1/catalog1/schema1/VECTOR_TO_STRING/
行为 CALL Function(2:60~2:70) /test/1/catalog1/schema1/VECTOR_DIM/
------
SQL  CREATE TABLE vector_ctas AS\nSELECT id,grp,embedding,CAST(embedding AS BINARY) AS embedding_binary\nFROM vector_lifecycle;
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/vector_ctas/ -> Table(3:5~3:21) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(2:24~2:28) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TRIGGER split97.trg_order_follows BEFORE INSERT ON split97.trigger_src FOR EACH ROW FOLLOWS trg_order_base SET NEW.note = 'follows';
行为 CREATE Trigger(1:15~1:40) /test/1/catalog1/split97/trg_order_follows/ -> Table(1:58~1:77) /test/1/catalog1/split97/trigger_src/
------
SQL  CREATE TRIGGER split97.trg_order_precedes BEFORE INSERT ON split97.trigger_src FOR EACH ROW PRECEDES trg_order_base SET NEW.note = 'precedes';
行为 CREATE Trigger(1:15~1:41) /test/1/catalog1/split97/trg_order_precedes/ -> Table(1:59~1:78) /test/1/catalog1/split97/trigger_src/
------
SQL  CREATE DEFINER = CURRENT_USER TRIGGER split97.trg_bi BEFORE INSERT ON split97.trigger_src FOR EACH ROW SET NEW.updated_at = CURRENT_TIMESTAMP;
行为 CREATE Trigger(1:38~1:52) /test/1/catalog1/split97/trg_bi/ -> Table(1:70~1:89) /test/1/catalog1/split97/trigger_src/
行为 CALL Function(1:124~1:141) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  CREATE TRIGGER split97.trg_bu BEFORE UPDATE ON split97.trigger_src FOR EACH ROW SET NEW.note = COALESCE(NEW.note, 'before update');
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split97/trg_bu/ -> Table(1:47~1:66) /test/1/catalog1/split97/trigger_src/
行为 CALL Function(1:95~1:103) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE TRIGGER split97.trg_bd BEFORE DELETE ON split97.trigger_src FOR EACH ROW INSERT INTO split97.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'before delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split97/trg_bd/ -> Table(1:47~1:66) /test/1/catalog1/split97/trigger_src/
行为 INSERT Table(1:92~1:113) /test/1/catalog1/split97/trigger_audit/
------
SQL  CREATE TRIGGER split97.trg_ai AFTER INSERT ON split97.trigger_src FOR EACH ROW INSERT INTO split97.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (NEW.id, 'after insert', NULL, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split97/trg_ai/ -> Table(1:46~1:65) /test/1/catalog1/split97/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split97/trigger_audit/
------
SQL  CREATE TRIGGER split97.trg_au AFTER UPDATE ON split97.trigger_src FOR EACH ROW INSERT INTO split97.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after update', OLD.amount, NEW.amount);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split97/trg_au/ -> Table(1:46~1:65) /test/1/catalog1/split97/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split97/trigger_audit/
------
SQL  CREATE TRIGGER split97.trg_ad AFTER DELETE ON split97.trigger_src FOR EACH ROW INSERT INTO split97.trigger_audit(src_id, action_name, old_amount, new_amount) VALUES (OLD.id, 'after delete', OLD.amount, NULL);
行为 CREATE Trigger(1:15~1:29) /test/1/catalog1/split97/trg_ad/ -> Table(1:46~1:65) /test/1/catalog1/split97/trigger_src/
行为 INSERT Table(1:91~1:112) /test/1/catalog1/split97/trigger_audit/
------
SQL  CREATE PROCEDURE p_jdv() BEGIN CREATE JSON DUALITY VIEW dv_in_proc AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1; DROP VIEW dv_in_proc; END;
行为 CREATE View(1:56~1:66) /test/1/catalog1/schema1/dv_in_proc/ -> Table(1:112~1:114) /test/1/catalog1/schema1/t1/
行为 CREATE Procedure(1:17~1:22) /test/1/catalog1/schema1/p_jdv/
行为 CALL Function(1:77~1:96) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
行为 DROP View(1:126~1:136) /test/1/catalog1/schema1/dv_in_proc/
------
SQL  CREATE OR REPLACE JSON DUALITY VIEW codex_group_having_audit_dv AS SELECT JSON_DUALITY_OBJECT('_id':a,'b':b) FROM codex_group_having_audit_src GROUP BY a HAVING a=1;
行为 REPLACE View(1:36~1:63) /test/1/catalog1/schema1/codex_group_having_audit_dv/ -> Table(1:114~1:142) /test/1/catalog1/schema1/codex_group_having_audit_src/
行为 CALL Function(1:74~1:93) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE OR REPLACE JSON DUALITY VIEW split_ext.dv_nested_default AS\nSELECT JSON_DUALITY_OBJECT(\n  '_id' : p.id,\n  'children' : (\n    SELECT JSON_ARRAYAGG(JSON_DUALITY_OBJECT('id' : c.id))\n    FROM split_ext.jdv_child AS c\n    WHERE c.parent_id = p.id\n  )\n)\nFROM split_ext.jdv_parent AS p;
行为 REPLACE View(1:36~1:63) /test/1/catalog1/split_ext/dv_nested_default/ -> [Table(6:9~6:28) /test/1/catalog1/split_ext/jdv_child/ ; Table(10:5~10:25) /test/1/catalog1/split_ext/jdv_parent/]
行为 CALL Function(2:7~2:26) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
行为 CALL Function(5:11~5:24) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE OR REPLACE JSON DUALITY VIEW split_ext.dv_nested_null AS\nSELECT JSON_DUALITY_OBJECT(\n  '_id' : p.id,\n  'children' : (\n    SELECT JSON_ARRAYAGG(JSON_DUALITY_OBJECT('id' : c.id) NULL ON NULL)\n    FROM split_ext.jdv_child AS c\n    WHERE c.parent_id = p.id\n  )\n)\nFROM split_ext.jdv_parent AS p;
行为 REPLACE View(1:36~1:60) /test/1/catalog1/split_ext/dv_nested_null/ -> [Table(6:9~6:28) /test/1/catalog1/split_ext/jdv_child/ ; Table(10:5~10:25) /test/1/catalog1/split_ext/jdv_parent/]
行为 CALL Function(2:7~2:26) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
行为 CALL Function(5:11~5:24) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE OR REPLACE JSON DUALITY VIEW split_ext.dv_nested_absent AS\nSELECT JSON_DUALITY_OBJECT(\n  '_id' : p.id,\n  'children' : (\n    SELECT JSON_ARRAYAGG(JSON_DUALITY_OBJECT('id' : c.id) ABSENT ON NULL)\n    FROM split_ext.jdv_child AS c\n    WHERE c.parent_id = p.id\n  )\n)\nFROM split_ext.jdv_parent AS p;
行为 REPLACE View(1:36~1:62) /test/1/catalog1/split_ext/dv_nested_absent/ -> [Table(6:9~6:28) /test/1/catalog1/split_ext/jdv_child/ ; Table(10:5~10:25) /test/1/catalog1/split_ext/jdv_parent/]
行为 CALL Function(2:7~2:26) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
行为 CALL Function(5:11~5:24) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE JSON DUALITY VIEW splitjdv.jdv_basic AS SELECT JSON_DUALITY_OBJECT('_id' : id, 'name' : name) FROM splitjdv.root_doc;
行为 CREATE View(1:25~1:43) /test/1/catalog1/splitjdv/jdv_basic/ -> Table(1:106~1:123) /test/1/catalog1/splitjdv/root_doc/
行为 CALL Function(1:54~1:73) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  ALTER JSON DUALITY VIEW splitjdv.jdv_basic AS SELECT JSON_DUALITY_OBJECT('_id' : id, 'label' : name) FROM splitjdv.root_doc;
行为 ALTER View(1:24~1:42) /test/1/catalog1/splitjdv/jdv_basic/ -> Table(1:106~1:123) /test/1/catalog1/splitjdv/root_doc/
行为 CALL Function(1:53~1:72) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE OR REPLACE ALGORITHM = MERGE SQL SECURITY INVOKER JSON RELATIONAL DUALITY VIEW splitjdv.jdv_rel AS SELECT JSON_DUALITY_OBJECT(WITH (INSERT, UPDATE, NO DELETE) '_id' : id, 'name' : name) FROM splitjdv.root_doc;
行为 REPLACE View(1:86~1:102) /test/1/catalog1/splitjdv/jdv_rel/ -> Table(1:198~1:215) /test/1/catalog1/splitjdv/root_doc/
行为 CALL Function(1:113~1:132) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE DEFINER = CURRENT_USER SQL SECURITY DEFINER JSON DUALITY VIEW splitjdv.jdv_def AS SELECT JSON_DUALITY_OBJECT('_id' : id, 'name' : name) FROM splitjdv.root_doc;
行为 CREATE View(1:69~1:85) /test/1/catalog1/splitjdv/jdv_def/ -> Table(1:148~1:165) /test/1/catalog1/splitjdv/root_doc/
行为 CALL Function(1:96~1:115) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW IF NOT EXISTS splitjdv.jdv_exists AS SELECT JSON_DUALITY_OBJECT('_id' : id, 'name' : name) FROM splitjdv.root_doc AS r;
行为 CREATE View(1:39~1:58) /test/1/catalog1/splitjdv/jdv_exists/ -> Table(1:121~1:138) /test/1/catalog1/splitjdv/root_doc/
行为 CALL Function(1:69~1:88) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW jdv_single_tag AS SELECT JSON_DUALITY_OBJECT(WITH INSERT '_id' : id, 'name' : name) FROM root_doc;
行为 CREATE View(1:25~1:39) /test/1/catalog1/schema1/jdv_single_tag/ -> Table(1:114~1:122) /test/1/catalog1/schema1/root_doc/
行为 CALL Function(1:50~1:69) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE OR REPLACE ALGORITHM = MERGE DEFINER = CURRENT_USER SQL SECURITY DEFINER VIEW split_view97.v_base (id, amount) AS\nSELECT id, amount FROM split_view97.src WHERE amount >= 0 WITH CASCADED CHECK OPTION;
行为 REPLACE View(1:85~1:104) /test/1/catalog1/split_view97/v_base/ -> Table(2:23~2:39) /test/1/catalog1/split_view97/src/
------
SQL  ALTER ALGORITHM = UNDEFINED DEFINER = CURRENT_USER SQL SECURITY INVOKER VIEW split_view97.v_base (id, amount) AS\nSELECT id, amount FROM split_view97.src WHERE amount BETWEEN 0 AND 100 WITH LOCAL CHECK OPTION;
行为 ALTER View(1:77~1:96) /test/1/catalog1/split_view97/v_base/ -> Table(2:23~2:39) /test/1/catalog1/split_view97/src/
------
SQL  CREATE ALGORITHM = TEMPTABLE VIEW split_view97.v_joined AS\nSELECT s.id, a.action_name FROM split_view97.src AS s LEFT JOIN split_view97.audit AS a ON a.src_id = s.id;
行为 CREATE View(1:34~1:55) /test/1/catalog1/split_view97/v_joined/ -> [Table(2:32~2:48) /test/1/catalog1/split_view97/src/ ; Table(2:64~2:82) /test/1/catalog1/split_view97/audit/]
------
SQL  CREATE VIEW IF NOT EXISTS v_ifne(v_a) AS SELECT a FROM t1;
行为 CREATE View(1:26~1:32) /test/1/catalog1/schema1/v_ifne/ -> Table(1:55~1:57) /test/1/catalog1/schema1/t1/
------
SQL  CREATE ALGORITHM=MERGE DEFINER=CURRENT_USER SQL SECURITY INVOKER VIEW IF NOT EXISTS v_ifne_full(v_a) AS SELECT a FROM t1 WITH CASCADED CHECK OPTION;
行为 CREATE View(1:84~1:95) /test/1/catalog1/schema1/v_ifne_full/ -> Table(1:118~1:120) /test/1/catalog1/schema1/t1/
------
SQL  CREATE VIEW split_view97.v_cte AS WITH cte AS (SELECT id, amount FROM split_view97.src) SELECT id, amount FROM cte;
行为 CREATE View(1:12~1:30) /test/1/catalog1/split_view97/v_cte/ -> Table(1:70~1:86) /test/1/catalog1/split_view97/src/
------
SQL  CREATE VIEW split_view97.v_table AS TABLE split_view97.src;
行为 CREATE View(1:12~1:32) /test/1/catalog1/split_view97/v_table/ -> Table(1:42~1:58) /test/1/catalog1/split_view97/src/
------
SQL  CREATE JSON DUALITY VIEW codex_func_gap2.ann_no_dv AS SELECT JSON_DUALITY_OBJECT(WITH (NO INSERT, NO UPDATE, NO DELETE) '_id':ppk,'pc2':pc2) FROM codex_func_gap2.parent;
行为 CREATE View(1:25~1:50) /test/1/catalog1/codex_func_gap2/ann_no_dv/ -> Table(1:146~1:168) /test/1/catalog1/codex_func_gap2/parent/
行为 CALL Function(1:61~1:80) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_columns(data) AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1;
行为 CREATE View(1:25~1:35) /test/1/catalog1/schema1/dv_columns/ -> Table(1:87~1:89) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:52~1:71) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  ALTER JSON DUALITY VIEW dv_base(data) AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1;
行为 ALTER View(1:24~1:31) /test/1/catalog1/schema1/dv_base/ -> Table(1:83~1:85) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:48~1:67) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE VIEW v_jdv_object AS SELECT JSON_DUALITY_OBJECT('c1':c1) FROM t1;
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/v_jdv_object/ -> Table(1:69~1:71) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:35~1:54) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_multi_projection AS SELECT JSON_DUALITY_OBJECT('_id':c1),1 FROM t1;
行为 CREATE View(1:25~1:44) /test/1/catalog1/schema1/dv_multi_projection/ -> Table(1:92~1:94) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:55~1:74) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_literal AS SELECT JSON_DUALITY_OBJECT('_id':c1,'v':1) FROM t1;
行为 CREATE View(1:25~1:35) /test/1/catalog1/schema1/dv_literal/ -> Table(1:87~1:89) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:46~1:65) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_binary AS SELECT JSON_DUALITY_OBJECT('_id':c1,'v':c1+c2) FROM t1;
行为 CREATE View(1:25~1:34) /test/1/catalog1/schema1/dv_binary/ -> Table(1:90~1:92) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:45~1:64) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_union AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1 UNION SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t2;
行为 CREATE View(1:25~1:33) /test/1/catalog1/schema1/dv_union/ -> [Table(1:79~1:81) /test/1/catalog1/schema1/t1/ ; Table(1:130~1:132) /test/1/catalog1/schema1/t2/]
行为 CALL Function(1:44~1:63) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_intersect AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1 INTERSECT SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t2;
行为 CREATE View(1:25~1:37) /test/1/catalog1/schema1/dv_intersect/ -> [Table(1:83~1:85) /test/1/catalog1/schema1/t1/ ; Table(1:138~1:140) /test/1/catalog1/schema1/t2/]
行为 CALL Function(1:48~1:67) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_except AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1 EXCEPT SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t2;
行为 CREATE View(1:25~1:34) /test/1/catalog1/schema1/dv_except/ -> [Table(1:80~1:82) /test/1/catalog1/schema1/t1/ ; Table(1:132~1:134) /test/1/catalog1/schema1/t2/]
行为 CALL Function(1:45~1:64) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_cte AS WITH q AS (SELECT c1 FROM t1) SELECT JSON_DUALITY_OBJECT('_id':c1) FROM q;
行为 CREATE View(1:25~1:31) /test/1/catalog1/schema1/dv_cte/ -> Table(1:61~1:63) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:72~1:91) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_window AS SELECT JSON_DUALITY_OBJECT('_id':c1,'r':RANK() OVER(ORDER BY c2)) FROM t1;
行为 CREATE View(1:25~1:34) /test/1/catalog1/schema1/dv_window/ -> Table(1:109~1:111) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:45~1:64) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
行为 CALL Function(1:78~1:82) /test/1/catalog1/schema1/RANK/
------
SQL  CREATE ALGORITHM=TEMPTABLE JSON DUALITY VIEW dv_temptable AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1;
行为 CREATE View(1:45~1:57) /test/1/catalog1/schema1/dv_temptable/ -> Table(1:103~1:105) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:68~1:87) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE ALGORITHM=UNDEFINED JSON DUALITY VIEW dv_undefined AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1;
行为 CREATE View(1:45~1:57) /test/1/catalog1/schema1/dv_undefined/ -> Table(1:103~1:105) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:68~1:87) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_check AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1 WITH CHECK OPTION;
行为 CREATE View(1:25~1:33) /test/1/catalog1/schema1/dv_check/ -> Table(1:79~1:81) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:44~1:63) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_local_check AS SELECT JSON_DUALITY_OBJECT('_id':c1) FROM t1 WITH LOCAL CHECK OPTION;
行为 CREATE View(1:25~1:39) /test/1/catalog1/schema1/dv_local_check/ -> Table(1:85~1:87) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:50~1:69) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_alias AS SELECT JSON_DUALITY_OBJECT('_id':c1) AS data_object FROM t1;
行为 CREATE View(1:25~1:33) /test/1/catalog1/schema1/dv_alias/ -> Table(1:94~1:96) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:44~1:63) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE JSON DUALITY VIEW dv_inner_cte AS SELECT JSON_DUALITY_OBJECT('_id':t1.c1,'children':(WITH q AS (SELECT c1 FROM t2) SELECT JSON_ARRAYAGG(JSON_DUALITY_OBJECT('id':c1)) FROM q)) FROM t1;
行为 CREATE View(1:25~1:37) /test/1/catalog1/schema1/dv_inner_cte/ -> [Table(1:118~1:120) /test/1/catalog1/schema1/t2/ ; Table(1:187~1:189) /test/1/catalog1/schema1/t1/]
行为 CALL Function(1:48~1:67) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
行为 CALL Function(1:129~1:142) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE OR REPLACE JSON DUALITY VIEW dv_gap AS SELECT JSON_DUALITY_OBJECT(WITH (INSERT,UPDATE,DELETE) '_id':T1.C1,'T2':(SELECT JSON_DUALITY_OBJECT(WITH DELETE 'C1':T2.C1,'C2':T2.C2) FROM T2 WHERE T1.C1=T2.C1)) FROM T1;
行为 REPLACE View(1:36~1:42) /test/1/catalog1/schema1/dv_gap/ -> [Table(1:186~1:188) /test/1/catalog1/schema1/T2/ ; Table(1:214~1:216) /test/1/catalog1/schema1/T1/]
行为 CALL Function(1:53~1:72) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE MATERIALIZED VIEW audit_mv_table AS TABLE audit_source;
行为 CREATE Materialized(1:25~1:39) /test/1/catalog1/schema1/audit_mv_table/ -> Table(1:49~1:61) /test/1/catalog1/schema1/audit_source/
------
SQL  DELETE FROM vector_lifecycle\nWHERE embedding=TO_VECTOR('[99,99,99]')\n   OR EXISTS (\n     SELECT 1 FROM vector_lifecycle AS v2\n     WHERE v2.id=vector_lifecycle.id\n       AND v2.embedding=vector_lifecycle.embedding\n   );
行为 DELETE Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/ -> Table(4:19~4:35) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(2:16~2:25) /test/1/catalog1/schema1/TO_VECTOR/
------
SQL  INSERT INTO vector_lifecycle\n  (id,grp,embedding,embedding_default,embedding2,note)\nSELECT\n  id+10,grp,embedding,embedding_default,embedding2,'select'\nFROM vector_lifecycle\nWHERE id=1;
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/ -> Table(5:5~5:21) /test/1/catalog1/schema1/vector_lifecycle/
------
SQL  INSERT INTO vector_dst(id,v) SELECT a.id,a.v FROM vector_src a JOIN vector_src b WHERE a.id=b.id AND a.v=b.v;
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/vector_dst/ -> Table(1:50~1:60) /test/1/catalog1/schema1/vector_src/
------
SQL  LOAD DATA FROM URI 'https://example.invalid/split.csv' INTO TABLE load_innodb;
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:19~1:54) /test/1/https:/example.invalid/split.csv/
------
SQL  LOAD XML FROM URI 'https://example.invalid/split.xml' INTO TABLE load_innodb;
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:18~1:53) /test/1/https:/example.invalid/split.xml/
------
SQL  LOAD DATA INFILE '/var/lib/mysql-files/split_load.tsv' INTO TABLE load_innodb COLUMNS NOT ENCLOSED DATE FORMAT '%Y-%m-%d' TIME FORMAT '%H:%i:%s' DATETIME FORMAT '%Y-%m-%d %H:%i:%s' NULL AS 'NULL' EMPTY VALUE '';
行为 IMPORT Table(1:66~1:77) /test/1/catalog1/schema1/load_innodb/ -> File(1:17~1:54) /test/1/var/lib/mysql-files/split_load.tsv/
------
SQL  LOAD XML INFILE '/var/lib/mysql-files/split_load.xml' INTO TABLE load_innodb COLUMNS NOT ENCLOSED DATE FORMAT '%Y-%m-%d' TIME FORMAT '%H:%i:%s' DATETIME FORMAT '%Y-%m-%d %H:%i:%s' NULL AS 'NULL' EMPTY VALUE '';
行为 IMPORT Table(1:65~1:76) /test/1/catalog1/schema1/load_innodb/ -> File(1:16~1:53) /test/1/var/lib/mysql-files/split_load.xml/
------
SQL  LOAD DATA LOCAL INFILE '/tmp/nonexistent-vector.csv'\nINTO TABLE vector_lifecycle\nFIELDS TERMINATED BY ','\n(id,grp,@embedding,@embedding2,note)\nSET embedding=TO_VECTOR(@embedding),\n    embedding_default=TO_VECTOR(@embedding),\n    embedding2=TO_VECTOR(@embedding2);
行为 IMPORT Table(2:11~2:27) /test/1/catalog1/schema1/vector_lifecycle/ -> File(1:23~1:52) /test/1/tmp/nonexistent-vector.csv/
行为 READ ConfigKey(4:8~4:18) /test/1/embedding/
行为 READ ConfigKey(4:19~4:30) /test/1/embedding2/
行为 CALL Function(5:14~5:23) /test/1/catalog1/schema1/TO_VECTOR/
------
SQL  LOAD DATA FROM URI 's3://b/x.csv' INTO TABLE t COLUMNS NOT ENCLOSED DATE FORMAT '%Y-%m-%d' NULL AS 'NULL' EMPTY VALUE '';
行为 IMPORT Table(1:45~1:46) /test/1/catalog1/schema1/t/ -> File(1:19~1:33) /test/1/s3:/b/x.csv/
------
SQL  LOAD XML FROM URI 's3://b/x.xml' INTO TABLE t COLUMNS NOT ENCLOSED DATETIME FORMAT '%Y-%m-%d %H:%i:%s' NULL AS 'NULL';
行为 IMPORT Table(1:44~1:45) /test/1/catalog1/schema1/t/ -> File(1:18~1:32) /test/1/s3:/b/x.xml/
------
SQL  CREATE JSON DUALITY VIEW jdv AS SELECT JSON_DUALITY_OBJECT('_id' : id) FROM root_doc
行为 CREATE View(1:25~1:28) /test/1/catalog1/schema1/jdv/ -> Table(1:76~1:84) /test/1/catalog1/schema1/root_doc/
行为 CALL Function(1:39~1:58) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  create view v1 as select * from source_tab
行为 CREATE View(1:12~1:14) /test/1/catalog1/schema1/v1/ -> Table(1:32~1:42) /test/1/catalog1/schema1/source_tab/
------
SQL  create view dbo.v1 as select * from dbo.t1
行为 CREATE View(1:12~1:18) /test/1/catalog1/dbo/v1/ -> Table(1:36~1:42) /test/1/catalog1/dbo/t1/
------
SQL  create index idx_t1_name on dbo.t1(name)
行为 CREATE Index(1:13~1:24) /test/1/catalog1/dbo/idx_t1_name/ -> Table(1:28~1:34) /test/1/catalog1/dbo/t1/
