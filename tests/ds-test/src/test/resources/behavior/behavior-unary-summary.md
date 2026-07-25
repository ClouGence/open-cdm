# 单主体或仅动作行为汇总（不含 SELECT）

本文件按语句类别收纳不含行为客体 `target` 的非 `SELECT` 行为。`SELECT` 已抽离到独立审计文档。

每条 SQL 只展示一次，随后逐行列出该语句类别下的行为；没有主体时只列语句动作。条目之间使用 `------` 分隔。

SQL 原文中的换行以 `\n` 显示；行为对象保持 fixture 中的 `TargetType(codeLine) resourcePath` 格式。
跨方言或版本完全相同的“语句类别 + SQL + 行为结果”只保留一次；SQL 相同但类别或行为不同的分别保留。

- 来源脚本：304
- 来源 testcase：33439
- 不含客体 testcase occurrence：30617
- 本文语句类别 occurrence：10300
- 本文去重后条目：3041


## CALL_PROG_OBJ

SQL  exec sys.sp_task 1, 'x'
行为 CALL Procedure(1:5~1:16) /test/1/catalog1/sys/sp_task/
------
SQL  call proc1()
行为 CALL Procedure(1:5~1:10) /test/1/catalog1/schema0/proc1/
------
SQL  CALL splitv.call_no_args;
行为 CALL Procedure(1:5~1:24) /test/1/catalog1/splitv/call_no_args/
------
SQL  CALL splitv.call_no_args();
行为 CALL Procedure(1:5~1:24) /test/1/catalog1/splitv/call_no_args/
------
SQL  CALL splitv.call_in_expr(2 + 3, CONCAT('a', 'b'));
行为 CALL Procedure(1:5~1:24) /test/1/catalog1/splitv/call_in_expr/
行为 CALL Function(1:32~1:38) /test/1/catalog1/schema1/CONCAT/
------
SQL  CALL splitv.call_out(@call_version);
行为 CALL Procedure(1:5~1:20) /test/1/catalog1/splitv/call_out/
行为 READ ConfigKey(1:21~1:34) /test/1/call_version/
------
SQL  CALL splitv.call_inout(@call_counter);
行为 CALL Procedure(1:5~1:22) /test/1/catalog1/splitv/call_inout/
行为 READ ConfigKey(1:23~1:36) /test/1/call_counter/
------
SQL  CALL splitv.call_nested(2, 'cq', ABS(test1()));
行为 CALL Procedure(1:5~1:23) /test/1/catalog1/splitv/call_nested/
行为 CALL Function(1:33~1:36) /test/1/catalog1/schema1/ABS/
行为 CALL Function(1:37~1:42) /test/1/catalog1/schema1/test1/
------
SQL  CALL splitv.call_nested(3, 'hz', test3.test());
行为 CALL Procedure(1:5~1:23) /test/1/catalog1/splitv/call_nested/
行为 CALL Function(1:33~1:43) /test/1/catalog1/test3/test/
------
SQL  CALL `proc name`();
行为 CALL Procedure(1:5~1:16) /test/1/catalog1/schema1/proc name/
------
SQL  call sp_test();
行为 CALL Procedure(1:5~1:12) /test/1/catalog1/schema1/sp_test/
------
SQL  call sp_test(1, 'abc');
行为 CALL Procedure(1:5~1:12) /test/1/catalog1/schema1/sp_test/
------
SQL  CALL query_rewrite.flush_rewrite_rules();
行为 CALL Procedure(1:5~1:38) /test/1/catalog1/query_rewrite/flush_rewrite_rules/
------
SQL  call proc1()
行为 CALL Procedure(1:5~1:10) /test/1/catalog1/schema1/proc1/
------
SQL  call sys.ps_setup_reset_to_default(false)
行为 CALL Procedure(1:5~1:34) /test/1/catalog1/sys/ps_setup_reset_to_default/
------
SQL  CALL sys.ps_setup_reset_to_default(FALSE);
行为 CALL Procedure(1:5~1:34) /test/1/catalog1/sys/ps_setup_reset_to_default/
------
SQL  exec sys.sp_cdc_enable_db;exec sys.sp_cdc_disable_db;
行为 CALL Procedure(1:5~1:25) /test/1/catalog1/sys/sp_cdc_enable_db/
------
SQL  exec sys.sp_cdc_enable_db;exec sys.sp_cdc_disable_db;
行为 CALL Procedure(1:31~1:52) /test/1/catalog1/sys/sp_cdc_disable_db/

## SWITCH_ROLE

SQL  SET ROLE PUBLIC;
行为 SWITCH Role(1:0~1:15) /test/1/
------
SQL  SET ROLE DEFAULT;
行为 SWITCH Role(1:0~1:16) /test/1/
------
SQL  SET ROLE NONE;
行为 SWITCH Role(1:0~1:13) /test/1/
------
SQL  SET ROLE 'split_role_a', 'split_role_b';
行为 SWITCH Role(1:0~1:39) /test/1/
------
SQL  SET ROLE ALL;
行为 SWITCH Role(1:0~1:12) /test/1/
------
SQL  SET ROLE ALL EXCEPT 'split_role_c';
行为 SWITCH Role(1:0~1:34) /test/1/
------
SQL  SET ROLE ALL EXCEPT 'split_role_80_a';
行为 SWITCH Role(1:0~1:37) /test/1/
------
SQL  SET ROLE 'split_role_80_a','split_role_80_b';
行为 SWITCH Role(1:0~1:44) /test/1/
------
SQL  SET ROLE ALL EXCEPT 'split_role_84_a';
行为 SWITCH Role(1:0~1:37) /test/1/
------
SQL  SET ROLE 'split_role_84_a','split_role_84_b';
行为 SWITCH Role(1:0~1:44) /test/1/
------
SQL  SET ROLE ALL EXCEPT 'split_role_97_a';
行为 SWITCH Role(1:0~1:37) /test/1/
------
SQL  SET ROLE 'split_role_97_a','split_role_97_b';
行为 SWITCH Role(1:0~1:44) /test/1/

## REVOKE

SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'split_acl_56'@'%';
行为 REVOKE UserOrRole(1:41~1:59) /test/1/split_acl_56@%/
------
SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM CURRENT_USER();
行为 REVOKE UserOrRole(1:41~1:53) /test/1/
------
SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'split_acl_57'@'%';
行为 REVOKE UserOrRole(1:41~1:59) /test/1/split_acl_57@%/
------
SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'split_acl_80'@'%';
行为 REVOKE UserOrRole(1:41~1:59) /test/1/split_acl_80@%/
------
SQL  REVOKE IF EXISTS ALL PRIVILEGES, GRANT OPTION FROM 'split_revoke_missing'@'%' IGNORE UNKNOWN USER;
行为 REVOKE UserOrRole(1:51~1:77) /test/1/split_revoke_missing@%/
------
SQL  REVOKE IF EXISTS PROXY ON 'split_proxy_from'@'%' FROM 'split_proxy_to'@'%' IGNORE UNKNOWN USER;
行为 REVOKE UserOrRole(1:26~1:48) /test/1/split_proxy_from@%/
行为 REVOKE UserOrRole(1:54~1:74) /test/1/split_proxy_to@%/
------
SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'split_revoke_to'@'%' IGNORE UNKNOWN USER;
行为 REVOKE UserOrRole(1:41~1:62) /test/1/split_revoke_to@%/
------
SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'split_acl_84'@'%';
行为 REVOKE UserOrRole(1:41~1:59) /test/1/split_acl_84@%/
------
SQL  REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'split_acl_97'@'%';
行为 REVOKE UserOrRole(1:41~1:59) /test/1/split_acl_97@%/

## CREATE_USER

SQL  CREATE USER 'cda01a'@'%' IDENTIFIED BY 'Cda#One01x9', 'cda01b'@'%' IDENTIFIED BY 'Cda#One01y9';
行为 CREATE User(1:12~1:24) /test/1/cda01a@%/
行为 CREATE User(1:54~1:66) /test/1/cda01b@%/
------
SQL  CREATE USER 'split_life_56_a'@'%' IDENTIFIED BY 'Life56a!';
行为 CREATE User(1:12~1:33) /test/1/split_life_56_a@%/
------
SQL  CREATE USER 'split_life_56_b'@'localhost' IDENTIFIED WITH mysql_native_password AS '*2470C0C06DEE42FD1618BB99005ADCA2EC9D1E19';
行为 CREATE User(1:12~1:41) /test/1/split_life_56_b@localhost/
------
SQL  CREATE USER 'mysqltest_1', 'mysqltest_2' IDENTIFIED BY 'Mysqltest-2', 'mysqltest_3' IDENTIFIED BY PASSWORD '*14074898176C592FD9086364071BDE91F3FE74AC';
行为 CREATE User(1:12~1:25) /test/1/mysqltest_1/
行为 CREATE User(1:27~1:40) /test/1/mysqltest_2/
行为 CREATE User(1:70~1:83) /test/1/mysqltest_3/
------
SQL  CREATE USER noauth@localhost IDENTIFIED WITH 'mysql_no_login';
行为 CREATE User(1:12~1:28) /test/1/noauth@localhost/
------
SQL  /*!50000 CREATE USER 'split_exec_56'@'%' IDENTIFIED BY 'ExecPass1!' */;
行为 CREATE User(1:21~1:40) /test/1/split_exec_56@%/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc57_a'@'%' IDENTIFIED BY 'Acc57a!' REQUIRE NONE PASSWORD EXPIRE DEFAULT ACCOUNT LOCK;
行为 CREATE User(1:26~1:45) /test/1/split_acc57_a@%/
------
SQL  CREATE USER 'split_acc57_b'@'%' IDENTIFIED BY 'Acc57b!' PASSWORD EXPIRE NEVER ACCOUNT UNLOCK;
行为 CREATE User(1:12~1:31) /test/1/split_acc57_b@%/
------
SQL  CREATE USER 'sat57'@'%' IDENTIFIED BY 'Tls57!' REQUIRE CIPHER 'DHE-RSA-AES256-SHA' AND ISSUER '/C=US/O=Example/OU=CA/CN=issuer' AND SUBJECT '/C=US/O=Example/OU=client/CN=user' WITH MAX_QUERIES_PER_HOUR 10 MAX_UPDATES_PER_HOUR 20 MAX_CONNECTIONS_PER_HOUR 30 MAX_USER_CONNECTIONS 4 PASSWORD EXPIRE INTERVAL 90 DAY ACCOUNT LOCK;
行为 CREATE User(1:12~1:23) /test/1/sat57@%/
------
SQL  CREATE USER 'split_x509_57'@'%' REQUIRE X509;
行为 CREATE User(1:12~1:31) /test/1/split_x509_57@%/
------
SQL  CREATE USER 'split_life_57_a'@'%' IDENTIFIED BY 'Life57a!';
行为 CREATE User(1:12~1:33) /test/1/split_life_57_a@%/
------
SQL  CREATE USER 'split_life_57_b'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Life57b!';
行为 CREATE User(1:12~1:41) /test/1/split_life_57_b@localhost/
------
SQL  CREATE USER IF NOT EXISTS u1 IDENTIFIED WITH 'mysql_native_password', u2 IDENTIFIED BY 'pass', u3 IDENTIFIED WITH 'sha256_password';
行为 CREATE User(1:26~1:28) /test/1/u1/
行为 CREATE User(1:70~1:72) /test/1/u2/
行为 CREATE User(1:95~1:97) /test/1/u3/
------
SQL  CREATE USER ca_sub@localhost REQUIRE SUBJECT '/C=CN/O=Example/CN=u';
行为 CREATE User(1:12~1:28) /test/1/ca_sub@localhost/
------
SQL  CREATE USER ca_res@localhost WITH MAX_QUERIES_PER_HOUR 2;
行为 CREATE User(1:12~1:28) /test/1/ca_res@localhost/
------
SQL  CREATE USER 'c4u1'@'%', 'c4u2'@'%' IDENTIFIED BY 'p' REQUIRE SSL WITH MAX_USER_CONNECTIONS 1 PASSWORD EXPIRE NEVER;
行为 CREATE User(1:12~1:22) /test/1/c4u1@%/
行为 CREATE User(1:24~1:34) /test/1/c4u2@%/
------
SQL  /*!50000 CREATE USER 'split_exec_57'@'%' IDENTIFIED BY 'ExecPass1!' */;
行为 CREATE User(1:21~1:40) /test/1/split_exec_57@%/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc80_random'@'%'\n  IDENTIFIED BY RANDOM PASSWORD\n  DEFAULT ROLE 'split_acc_role'\n  PASSWORD HISTORY DEFAULT\n  PASSWORD REUSE INTERVAL DEFAULT\n  PASSWORD REQUIRE CURRENT DEFAULT\n  ACCOUNT UNLOCK\n  COMMENT 'random account';
行为 CREATE User(1:26~1:50) /test/1/split_acc80_random@%/
行为 CREATE Role(3:15~3:31) /test/1/split_acc_role/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc80_module'@'%'\n  IDENTIFIED WITH caching_sha2_password BY RANDOM PASSWORD\n  PASSWORD HISTORY 3\n  PASSWORD REUSE INTERVAL 30 DAY\n  PASSWORD REQUIRE CURRENT OPTIONAL\n  FAILED_LOGIN_ATTEMPTS 4\n  PASSWORD_LOCK_TIME 2\n  ATTRIBUTE '{"team":"dba"}';
行为 CREATE User(1:26~1:50) /test/1/split_acc80_module@%/
------
SQL  CREATE USER 'sat80'@'%' IDENTIFIED BY 'Tls80!' REQUIRE CIPHER 'DHE-RSA-AES256-SHA' AND ISSUER '/C=US/O=Example/OU=CA/CN=issuer' AND SUBJECT '/C=US/O=Example/OU=client/CN=user' WITH MAX_QUERIES_PER_HOUR 10 MAX_UPDATES_PER_HOUR 20 MAX_CONNECTIONS_PER_HOUR 30 MAX_USER_CONNECTIONS 4 PASSWORD EXPIRE INTERVAL 90 DAY ACCOUNT LOCK;
行为 CREATE User(1:12~1:23) /test/1/sat80@%/
------
SQL  CREATE USER 'split_none_80'@'%' REQUIRE NONE;
行为 CREATE User(1:12~1:31) /test/1/split_none_80@%/
------
SQL  CREATE USER 'split_x509_80'@'%' REQUIRE X509;
行为 CREATE User(1:12~1:31) /test/1/split_x509_80@%/
------
SQL  create user 'test_user'@'localhost' identified by 'test_password';
行为 CREATE User(1:12~1:35) /test/1/test_user@localhost/
------
SQL  create user 'test_user'@'localhost';
行为 CREATE User(1:12~1:35) /test/1/test_user@localhost/
------
SQL  create user 'test_user' identified by 'test_password';
行为 CREATE User(1:12~1:23) /test/1/test_user/
------
SQL  create user 'test_user'@'192.168.0.1' identified by 'test_password';
行为 CREATE User(1:12~1:37) /test/1/test_user@192.168.0.1/
------
SQL  CREATE USER 'pwless84'@'localhost' IDENTIFIED WITH 'passwordless_plugin' INITIAL AUTHENTICATION IDENTIFIED BY RANDOM PASSWORD;
行为 CREATE User(1:12~1:34) /test/1/pwless84@localhost/
------
SQL  CREATE USER foo@localhost IDENTIFIED WITH 'caching_sha2_password' AS 0x244124303035240C4D7A6D25436F2C0A08515310644615383E2A123961484C6276734178425A446172436B58446A582F6178544A692E6F644E4F2F4E596E666276454B563336 PASSWORD HISTORY DEFAULT;
行为 CREATE User(1:12~1:25) /test/1/foo@localhost/
------
SQL  CREATE USER 'gap_mfa2'@'%' IDENTIFIED BY 'P1!' AND IDENTIFIED WITH caching_sha2_password BY 'P2!';
行为 CREATE User(1:12~1:26) /test/1/gap_mfa2@%/
------
SQL  CREATE USER 'gap_mfa3'@'%' IDENTIFIED BY 'P1!' AND IDENTIFIED BY 'P2!' AND IDENTIFIED BY RANDOM PASSWORD;
行为 CREATE User(1:12~1:26) /test/1/gap_mfa3@%/
------
SQL  CREATE USER 'gap_pwless1'@'%' IDENTIFIED WITH 'passwordless_plugin' INITIAL AUTHENTICATION IDENTIFIED BY 'Bootstrap1!';
行为 CREATE User(1:12~1:29) /test/1/gap_pwless1@%/
------
SQL  CREATE USER 'gap_pwless2'@'%' IDENTIFIED WITH 'passwordless_plugin' INITIAL AUTHENTICATION IDENTIFIED WITH caching_sha2_password AS 'hash';
行为 CREATE User(1:12~1:29) /test/1/gap_pwless2@%/
------
SQL  CREATE USER 'c3u1'@'%' IDENTIFIED BY 'p', 'c3u2'@'%' DEFAULT ROLE 'c3r1','c3r2';
行为 CREATE User(1:12~1:22) /test/1/c3u1@%/
行为 CREATE User(1:42~1:52) /test/1/c3u2@%/
行为 CREATE Role(1:66~1:72) /test/1/c3r1/
行为 CREATE Role(1:73~1:79) /test/1/c3r2/
------
SQL  CREATE USER 'split_hex_initial'@'%' IDENTIFIED WITH authentication_webauthn INITIAL AUTHENTICATION IDENTIFIED WITH caching_sha2_password AS 0x01;
行为 CREATE User(1:12~1:35) /test/1/split_hex_initial@%/
------
SQL  create user 'test_user'@'localhost' identified by 'password123';
行为 CREATE User(1:12~1:35) /test/1/test_user@localhost/
------
SQL  create user if not exists 'test_user'@'%' identified by 'password123';
行为 CREATE User(1:26~1:41) /test/1/test_user@%/
------
SQL  /*!50000 CREATE USER 'split_exec_80'@'%' IDENTIFIED BY 'ExecPass1!' */;
行为 CREATE User(1:21~1:40) /test/1/split_exec_80@%/
------
SQL  CREATE USER IF NOT EXISTS 'u84'@'%' IDENTIFIED WITH caching_sha2_password BY 'pw' REQUIRE SSL PASSWORD EXPIRE INTERVAL 90 DAY ACCOUNT LOCK;
行为 CREATE User(1:26~1:35) /test/1/u84@%/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc84_random'@'%' IDENTIFIED BY RANDOM PASSWORD DEFAULT ROLE 'split_acc84_role' PASSWORD HISTORY DEFAULT PASSWORD REUSE INTERVAL DEFAULT PASSWORD REQUIRE CURRENT DEFAULT ACCOUNT UNLOCK COMMENT 'random account';
行为 CREATE User(1:26~1:50) /test/1/split_acc84_random@%/
行为 CREATE Role(1:94~1:112) /test/1/split_acc84_role/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc84_module'@'%' IDENTIFIED WITH caching_sha2_password BY RANDOM PASSWORD PASSWORD HISTORY 3 PASSWORD REUSE INTERVAL 30 DAY PASSWORD REQUIRE CURRENT OPTIONAL FAILED_LOGIN_ATTEMPTS 4 PASSWORD_LOCK_TIME 2 ATTRIBUTE '{"team":"dba"}';
行为 CREATE User(1:26~1:50) /test/1/split_acc84_module@%/
------
SQL  CREATE USER 'sat84'@'%' IDENTIFIED BY 'Tls84!' REQUIRE CIPHER 'DHE-RSA-AES256-SHA' AND ISSUER '/C=US/O=Example/OU=CA/CN=issuer' AND SUBJECT '/C=US/O=Example/OU=client/CN=user' WITH MAX_QUERIES_PER_HOUR 10 MAX_UPDATES_PER_HOUR 20 MAX_CONNECTIONS_PER_HOUR 30 MAX_USER_CONNECTIONS 4 PASSWORD EXPIRE INTERVAL 90 DAY ACCOUNT LOCK;
行为 CREATE User(1:12~1:23) /test/1/sat84@%/
------
SQL  CREATE USER 'split_none_84'@'%' REQUIRE NONE;
行为 CREATE User(1:12~1:31) /test/1/split_none_84@%/
------
SQL  CREATE USER 'split_x509_84'@'%' REQUIRE X509;
行为 CREATE User(1:12~1:31) /test/1/split_x509_84@%/
------
SQL  CREATE USER 'split_life_84_a'@'%' IDENTIFIED BY 'Life84a!';
行为 CREATE User(1:12~1:33) /test/1/split_life_84_a@%/
------
SQL  CREATE USER 'split_life_84_b'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Life84b!';
行为 CREATE User(1:12~1:41) /test/1/split_life_84_b@localhost/
------
SQL  /*!50000 CREATE USER 'split_exec_84'@'%' IDENTIFIED BY 'ExecPass1!' */;
行为 CREATE User(1:21~1:40) /test/1/split_exec_84@%/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc97_random'@'%' IDENTIFIED BY RANDOM PASSWORD DEFAULT ROLE 'split_acc97_role' PASSWORD HISTORY DEFAULT PASSWORD REUSE INTERVAL DEFAULT PASSWORD REQUIRE CURRENT DEFAULT ACCOUNT UNLOCK COMMENT 'random account';
行为 CREATE User(1:26~1:50) /test/1/split_acc97_random@%/
行为 CREATE Role(1:94~1:112) /test/1/split_acc97_role/
------
SQL  CREATE USER IF NOT EXISTS 'split_acc97_module'@'%' IDENTIFIED WITH caching_sha2_password BY RANDOM PASSWORD PASSWORD HISTORY 3 PASSWORD REUSE INTERVAL 30 DAY PASSWORD REQUIRE CURRENT OPTIONAL FAILED_LOGIN_ATTEMPTS 4 PASSWORD_LOCK_TIME 2 ATTRIBUTE '{"team":"dba"}';
行为 CREATE User(1:26~1:50) /test/1/split_acc97_module@%/
------
SQL  CREATE USER 'sat97'@'%' IDENTIFIED BY 'Tls97!' REQUIRE CIPHER 'DHE-RSA-AES256-SHA' AND ISSUER '/C=US/O=Example/OU=CA/CN=issuer' AND SUBJECT '/C=US/O=Example/OU=client/CN=user' WITH MAX_QUERIES_PER_HOUR 10 MAX_UPDATES_PER_HOUR 20 MAX_CONNECTIONS_PER_HOUR 30 MAX_USER_CONNECTIONS 4 PASSWORD EXPIRE INTERVAL 90 DAY ACCOUNT LOCK;
行为 CREATE User(1:12~1:23) /test/1/sat97@%/
------
SQL  CREATE USER 'split_none_97'@'%' REQUIRE NONE;
行为 CREATE User(1:12~1:31) /test/1/split_none_97@%/
------
SQL  CREATE USER 'split_x509_97'@'%' REQUIRE X509;
行为 CREATE User(1:12~1:31) /test/1/split_x509_97@%/
------
SQL  CREATE USER 'split_life_97_a'@'%' IDENTIFIED BY 'Life97a!';
行为 CREATE User(1:12~1:33) /test/1/split_life_97_a@%/
------
SQL  CREATE USER 'split_life_97_b'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'Life97b!';
行为 CREATE User(1:12~1:41) /test/1/split_life_97_b@localhost/
------
SQL  /*!50000 CREATE USER 'split_exec_97'@'%' IDENTIFIED BY 'ExecPass1!' */;
行为 CREATE User(1:21~1:40) /test/1/split_exec_97@%/

## ALTER_USER

SQL  ALTER USER 'split_life_56_a'@'%' PASSWORD EXPIRE;
行为 ALTER User(1:11~1:32) /test/1/split_life_56_a@%/
------
SQL  SET PASSWORD = PASSWORD('Self56a!');
行为 ALTER User(1:0~1:35) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' = PASSWORD('For56a!');
行为 ALTER User(1:0~1:57) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_old'@'%' = OLD_PASSWORD('Old56!');
行为 ALTER User(1:0~1:60) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_hash'@'%' = '*2470C0C06DEE42FD1618BB99005ADCA2EC9D1E19';
行为 ALTER User(1:0~1:82) /test/1/
------
SQL  SET PASSWORD FOR CURRENT_USER() = 'NewPass!';
行为 ALTER User(1:0~1:44) /test/1/
------
SQL  SET PASSWORD FOR CURRENT_USER() = PASSWORD('NewPass!');
行为 ALTER User(1:0~1:54) /test/1/
------
SQL  SET PASSWORD TO RANDOM REPLACE 'Split#Old1';
行为 ALTER User(1:0~1:43) /test/1/
------
SQL  ALTER USER 'split_acc57_a'@'%' PASSWORD EXPIRE INTERVAL 90 DAY ACCOUNT UNLOCK;
行为 ALTER User(1:11~1:30) /test/1/split_acc57_a@%/
------
SQL  ALTER USER 'split_acc57_b'@'%' PASSWORD EXPIRE;
行为 ALTER User(1:11~1:30) /test/1/split_acc57_b@%/
------
SQL  ALTER USER 'sat57'@'%' REQUIRE SSL WITH MAX_QUERIES_PER_HOUR 11 MAX_UPDATES_PER_HOUR 21 MAX_CONNECTIONS_PER_HOUR 31 MAX_USER_CONNECTIONS 5 ACCOUNT UNLOCK;
行为 ALTER User(1:11~1:22) /test/1/sat57@%/
------
SQL  ALTER USER 'cda15a'@'%';
行为 ALTER User(1:11~1:23) /test/1/cda15a@%/
------
SQL  ALTER USER 'cda02a'@'%' IDENTIFIED BY 'Cda#New02x9', 'cda02b'@'%' IDENTIFIED BY 'Cda#New02y9' PASSWORD EXPIRE;
行为 ALTER User(1:11~1:23) /test/1/cda02a@%/
行为 ALTER User(1:53~1:65) /test/1/cda02b@%/
------
SQL  ALTER USER USER() IDENTIFIED BY '123456';
行为 ALTER User(1:0~1:40) /test/1/
------
SQL  ALTER USER 'split_life_57_a'@'%' IDENTIFIED BY 'Life57c!';
行为 ALTER User(1:11~1:32) /test/1/split_life_57_a@%/
------
SQL  ALTER USER IF EXISTS u1@localhost IDENTIFIED WITH mysql_native_password BY 'pass_native3';
行为 ALTER User(1:21~1:33) /test/1/u1@localhost/
------
SQL  ALTER USER 20553132_u2@localhost IDENTIFIED BY 'abcd', 20553132_u1@localhost IDENTIFIED BY 'defg' PASSWORD EXPIRE NEVER;
行为 ALTER User(1:11~1:32) /test/1/20553132_u2@localhost/
行为 ALTER User(1:55~1:76) /test/1/20553132_u1@localhost/
------
SQL  ALTER USER ca_sub@localhost REQUIRE ISSUER '/C=CN/O=Example/CN=ca';
行为 ALTER User(1:11~1:27) /test/1/ca_sub@localhost/
------
SQL  ALTER USER ca_sub@localhost IDENTIFIED WITH 'mysql_native_password';
行为 ALTER User(1:11~1:27) /test/1/ca_sub@localhost/
------
SQL  ALTER USER ca_sub@localhost IDENTIFIED WITH 'sha256_password', ca_res@localhost, ca_third@localhost IDENTIFIED BY 'p' PASSWORD EXPIRE DEFAULT;
行为 ALTER User(1:11~1:27) /test/1/ca_sub@localhost/
行为 ALTER User(1:63~1:79) /test/1/ca_res@localhost/
行为 ALTER User(1:81~1:99) /test/1/ca_third@localhost/
------
SQL  SET PASSWORD = 'Self57a!';
行为 ALTER User(1:0~1:25) /test/1/
------
SQL  SET PASSWORD = PASSWORD('Self57b!');
行为 ALTER User(1:0~1:35) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' = 'For57a!';
行为 ALTER User(1:0~1:47) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' = PASSWORD('For57b!');
行为 ALTER User(1:0~1:57) /test/1/
------
SQL  ALTER USER IF EXISTS 'split_acc80_random'@'%'\n  IDENTIFIED BY RANDOM PASSWORD\n  PASSWORD EXPIRE\n  PASSWORD HISTORY 5\n  PASSWORD REUSE INTERVAL 45 DAY\n  PASSWORD REQUIRE CURRENT\n  FAILED_LOGIN_ATTEMPTS 3\n  PASSWORD_LOCK_TIME UNBOUNDED\n  ATTRIBUTE '{"comment":"rotated"}';
行为 ALTER User(1:21~1:45) /test/1/split_acc80_random@%/
------
SQL  ALTER USER IF EXISTS 'split_acc80_module'@'%'\n  IDENTIFIED BY 'Acc80b!'\n  PASSWORD EXPIRE DEFAULT\n  ACCOUNT LOCK\n  COMMENT 'locked module account';
行为 ALTER User(1:21~1:45) /test/1/split_acc80_module@%/
------
SQL  ALTER USER 'sat80'@'%' REQUIRE SSL WITH MAX_QUERIES_PER_HOUR 11 MAX_UPDATES_PER_HOUR 21 MAX_CONNECTIONS_PER_HOUR 31 MAX_USER_CONNECTIONS 5 ACCOUNT UNLOCK;
行为 ALTER User(1:11~1:22) /test/1/sat80@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' ADD 2 FACTOR IDENTIFIED WITH caching_sha2_password BY 'Mfa2!';
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' ADD 2 FACTOR IDENTIFIED WITH caching_sha2_password BY 'Mfa2!' ADD 3 FACTOR IDENTIFIED WITH caching_sha2_password BY 'Mfa3!';
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' MODIFY 2 FACTOR IDENTIFIED WITH caching_sha2_password BY RANDOM PASSWORD;
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' MODIFY 2 FACTOR IDENTIFIED WITH caching_sha2_password BY 'Mfa2!' MODIFY 3 FACTOR IDENTIFIED WITH caching_sha2_password BY 'Mfa3!';
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' DROP 2 FACTOR;
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' DROP 2 FACTOR DROP 3 FACTOR;
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' 2 FACTOR INITIATE REGISTRATION;
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' 2 FACTOR UNREGISTER;
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'split_mfa_accept'@'%' 2 FACTOR FINISH REGISTRATION SET CHALLENGE_RESPONSE AS 'challenge';
行为 ALTER User(1:11~1:33) /test/1/split_mfa_accept@%/
------
SQL  ALTER USER 'codex_c20a'@'localhost' IDENTIFIED BY 'New#C20a9' RETAIN CURRENT PASSWORD, 'codex_c20b'@'localhost' IDENTIFIED BY 'New#C20b9' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:11~1:35) /test/1/codex_c20a@localhost/
行为 ALTER User(1:87~1:111) /test/1/codex_c20b@localhost/
------
SQL  ALTER USER USER() IDENTIFIED BY '123456' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:64) /test/1/
------
SQL  ALTER USER USER() DISCARD OLD PASSWORD;
行为 ALTER User(1:0~1:38) /test/1/
------
SQL  ALTER USER USER() IDENTIFIED BY '123456' REPLACE '123456' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:81) /test/1/
------
SQL  ALTER USER 'split_role_alter'@'%' DEFAULT ROLE ALL;
行为 ALTER User(1:11~1:33) /test/1/split_role_alter@%/
------
SQL  ALTER USER 'split_role_alter'@'%' DEFAULT ROLE NONE;
行为 ALTER User(1:11~1:33) /test/1/split_role_alter@%/
------
SQL  ALTER USER 'split_discard_named'@'%' DISCARD OLD PASSWORD;
行为 ALTER User(1:11~1:36) /test/1/split_discard_named@%/
------
SQL  SET DEFAULT ROLE ALL TO 'root'@'%';
行为 ALTER User(1:24~1:34) /test/1/root@%/
------
SQL  SET DEFAULT ROLE NONE TO 'root'@'%';
行为 ALTER User(1:25~1:35) /test/1/root@%/
------
SQL  SET DEFAULT ROLE ALL TO 'split_role_u80'@'%';
行为 ALTER User(1:24~1:44) /test/1/split_role_u80@%/
------
SQL  SET DEFAULT ROLE NONE TO 'split_role_u80'@'%';
行为 ALTER User(1:25~1:45) /test/1/split_role_u80@%/
------
SQL  ALTER USER 'split_role_u80'@'%' DEFAULT ROLE ALL;
行为 ALTER User(1:11~1:31) /test/1/split_role_u80@%/
------
SQL  ALTER USER 'split_role_u80'@'%' DEFAULT ROLE NONE;
行为 ALTER User(1:11~1:31) /test/1/split_role_u80@%/
------
SQL  ALTER USER usr IDENTIFIED BY RANDOM PASSWORD REPLACE 'qrst';
行为 ALTER User(1:11~1:14) /test/1/usr/
------
SQL  ALTER USER 'split_mix_a'@'localhost' DISCARD OLD PASSWORD, 'split_mix_b'@'localhost' IDENTIFIED BY 'New#MixB9' RETAIN CURRENT PASSWORD, 'split_mix_c'@'localhost' IDENTIFIED BY 'New#MixC9';
行为 ALTER User(1:11~1:36) /test/1/split_mix_a@localhost/
行为 ALTER User(1:59~1:84) /test/1/split_mix_b@localhost/
行为 ALTER User(1:136~1:161) /test/1/split_mix_c@localhost/
------
SQL  ALTER USER CURRENT_USER() IDENTIFIED BY RANDOM PASSWORD REPLACE 'Old1!' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:95) /test/1/
------
SQL  ALTER USER 'gap_mfa3'@'%' 3 FACTOR UNREGISTER;
行为 ALTER User(1:11~1:25) /test/1/gap_mfa3@%/
------
SQL  ALTER USER IF EXISTS USER() 2 FACTOR UNREGISTER;
行为 ALTER User(1:0~1:47) /test/1/
------
SQL  ALTER USER IF EXISTS 'split_hex_registration'@'%' 2 FACTOR FINISH REGISTRATION SET CHALLENGE_RESPONSE AS 0x01;
行为 ALTER User(1:21~1:49) /test/1/split_hex_registration@%/
------
SQL  alter user 'test_user'@'localhost' identified by 'new_password';
行为 ALTER User(1:11~1:34) /test/1/test_user@localhost/
------
SQL  SET PASSWORD = 'Self80a!' REPLACE 'Start80!' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:68) /test/1/
------
SQL  SET PASSWORD TO RANDOM;
行为 ALTER User(1:0~1:22) /test/1/
------
SQL  SET PASSWORD TO RANDOM RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:46) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' = 'For80a!';
行为 ALTER User(1:0~1:47) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' = 'For80b!' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:71) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' TO RANDOM;
行为 ALTER User(1:0~1:45) /test/1/
------
SQL  SET PASSWORD FOR 'split_pw_for'@'%' TO RANDOM RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:69) /test/1/
------
SQL  SET PASSWORD FOR 'cda16a'@'%' = 'Cda#New16x9' REPLACE 'Cda#Old16x9';
行为 ALTER User(1:0~1:67) /test/1/
------
SQL  SET PASSWORD FOR split_gap_foo TO RANDOM REPLACE 'Split#Old1';
行为 ALTER User(1:0~1:61) /test/1/
------
SQL  ALTER USER IF EXISTS 'u84'@'%' IDENTIFIED BY 'pw2' PASSWORD EXPIRE NEVER ACCOUNT UNLOCK;
行为 ALTER User(1:21~1:30) /test/1/u84@%/
------
SQL  ALTER USER IF EXISTS 'split_acc84_random'@'%' IDENTIFIED BY RANDOM PASSWORD PASSWORD EXPIRE PASSWORD HISTORY 5 PASSWORD REUSE INTERVAL 45 DAY PASSWORD REQUIRE CURRENT FAILED_LOGIN_ATTEMPTS 3 PASSWORD_LOCK_TIME UNBOUNDED ATTRIBUTE '{"comment":"rotated"}';
行为 ALTER User(1:21~1:45) /test/1/split_acc84_random@%/
------
SQL  ALTER USER IF EXISTS 'split_acc84_module'@'%' IDENTIFIED BY 'Acc84b!' PASSWORD EXPIRE DEFAULT ACCOUNT LOCK COMMENT 'locked module account';
行为 ALTER User(1:21~1:45) /test/1/split_acc84_module@%/
------
SQL  ALTER USER 'sat84'@'%' REQUIRE SSL WITH MAX_QUERIES_PER_HOUR 11 MAX_UPDATES_PER_HOUR 21 MAX_CONNECTIONS_PER_HOUR 31 MAX_USER_CONNECTIONS 5 ACCOUNT UNLOCK;
行为 ALTER User(1:11~1:22) /test/1/sat84@%/
------
SQL  SET DEFAULT ROLE ALL TO 'split_role_u84'@'%';
行为 ALTER User(1:24~1:44) /test/1/split_role_u84@%/
------
SQL  SET DEFAULT ROLE NONE TO 'split_role_u84'@'%';
行为 ALTER User(1:25~1:45) /test/1/split_role_u84@%/
------
SQL  ALTER USER 'split_role_u84'@'%' DEFAULT ROLE ALL;
行为 ALTER User(1:11~1:31) /test/1/split_role_u84@%/
------
SQL  ALTER USER 'split_role_u84'@'%' DEFAULT ROLE NONE;
行为 ALTER User(1:11~1:31) /test/1/split_role_u84@%/
------
SQL  ALTER USER 'split_life_84_a'@'%' IDENTIFIED BY 'Life84c!';
行为 ALTER User(1:11~1:32) /test/1/split_life_84_a@%/
------
SQL  SET PASSWORD = 'Self84Bb2y' REPLACE 'Start84Aa1x' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:73) /test/1/
------
SQL  SET PASSWORD FOR 'split_for84'@'%' = 'For84Cc3z';
行为 ALTER User(1:0~1:48) /test/1/
------
SQL  SET PASSWORD FOR 'split_for84'@'%' = 'For84Dd4w' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:72) /test/1/
------
SQL  SET PASSWORD FOR 'split_for84'@'%' TO RANDOM;
行为 ALTER User(1:0~1:44) /test/1/
------
SQL  SET PASSWORD FOR 'split_for84'@'%' TO RANDOM RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:68) /test/1/
------
SQL  ALTER USER IF EXISTS 'split_acc97_random'@'%' IDENTIFIED BY RANDOM PASSWORD PASSWORD EXPIRE PASSWORD HISTORY 5 PASSWORD REUSE INTERVAL 45 DAY PASSWORD REQUIRE CURRENT FAILED_LOGIN_ATTEMPTS 3 PASSWORD_LOCK_TIME UNBOUNDED ATTRIBUTE '{"comment":"rotated"}';
行为 ALTER User(1:21~1:45) /test/1/split_acc97_random@%/
------
SQL  ALTER USER IF EXISTS 'split_acc97_module'@'%' IDENTIFIED BY 'Acc97b!' PASSWORD EXPIRE DEFAULT ACCOUNT LOCK COMMENT 'locked module account';
行为 ALTER User(1:21~1:45) /test/1/split_acc97_module@%/
------
SQL  ALTER USER 'sat97'@'%' REQUIRE SSL WITH MAX_QUERIES_PER_HOUR 11 MAX_UPDATES_PER_HOUR 21 MAX_CONNECTIONS_PER_HOUR 31 MAX_USER_CONNECTIONS 5 ACCOUNT UNLOCK;
行为 ALTER User(1:11~1:22) /test/1/sat97@%/
------
SQL  SET DEFAULT ROLE ALL TO 'split_role_u97'@'%';
行为 ALTER User(1:24~1:44) /test/1/split_role_u97@%/
------
SQL  SET DEFAULT ROLE NONE TO 'split_role_u97'@'%';
行为 ALTER User(1:25~1:45) /test/1/split_role_u97@%/
------
SQL  ALTER USER 'split_role_u97'@'%' DEFAULT ROLE ALL;
行为 ALTER User(1:11~1:31) /test/1/split_role_u97@%/
------
SQL  ALTER USER 'split_role_u97'@'%' DEFAULT ROLE NONE;
行为 ALTER User(1:11~1:31) /test/1/split_role_u97@%/
------
SQL  ALTER USER 'split_life_97_a'@'%' IDENTIFIED BY 'Life97c!';
行为 ALTER User(1:11~1:32) /test/1/split_life_97_a@%/
------
SQL  SET PASSWORD = 'Self97Bb2y' REPLACE 'Start97Aa1x' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:73) /test/1/
------
SQL  SET PASSWORD FOR 'split_for97'@'%' = 'For97Cc3z';
行为 ALTER User(1:0~1:48) /test/1/
------
SQL  SET PASSWORD FOR 'split_for97'@'%' = 'For97Dd4w' RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:72) /test/1/
------
SQL  SET PASSWORD FOR 'split_for97'@'%' TO RANDOM;
行为 ALTER User(1:0~1:44) /test/1/
------
SQL  SET PASSWORD FOR 'split_for97'@'%' TO RANDOM RETAIN CURRENT PASSWORD;
行为 ALTER User(1:0~1:68) /test/1/

## DROP_USER

SQL  DROP USER 'split_life_56_a'@'%','split_life_56_c'@'localhost';
行为 DROP User(1:10~1:31) /test/1/split_life_56_a@%/
行为 DROP User(1:32~1:61) /test/1/split_life_56_c@localhost/
------
SQL  DROP USER CURRENT_USER();
行为 DROP User(1:0~1:24) /test/1/
------
SQL  /*!50000 DROP USER 'split_exec_56'@'%' */;
行为 DROP User(1:19~1:38) /test/1/split_exec_56@%/
------
SQL  DROP USER 'split_life_57_a'@'%','split_life_57_c'@'localhost';
行为 DROP User(1:10~1:31) /test/1/split_life_57_a@%/
行为 DROP User(1:32~1:61) /test/1/split_life_57_c@localhost/
------
SQL  DROP USER IF EXISTS 'split_life_57_missing'@'%';
行为 DROP User(1:20~1:47) /test/1/split_life_57_missing@%/
------
SQL  /*!50000 DROP USER 'split_exec_57'@'%' */;
行为 DROP User(1:19~1:38) /test/1/split_exec_57@%/
------
SQL  drop user test_user@'localhost';
行为 DROP User(1:10~1:31) /test/1/test_user@localhost/
------
SQL  drop user 'test_user'@'localhost';
行为 DROP User(1:10~1:33) /test/1/test_user@localhost/
------
SQL  drop user if exists 'test_user'@'localhost';
行为 DROP User(1:20~1:43) /test/1/test_user@localhost/
------
SQL  /*!50000 DROP USER 'split_exec_80'@'%' */;
行为 DROP User(1:19~1:38) /test/1/split_exec_80@%/
------
SQL  DROP USER IF EXISTS 'u84'@'%';
行为 DROP User(1:20~1:29) /test/1/u84@%/
------
SQL  DROP USER 'split_life_84_a'@'%','split_life_84_c'@'localhost';
行为 DROP User(1:10~1:31) /test/1/split_life_84_a@%/
行为 DROP User(1:32~1:61) /test/1/split_life_84_c@localhost/
------
SQL  DROP USER IF EXISTS 'split_life_84_missing'@'%';
行为 DROP User(1:20~1:47) /test/1/split_life_84_missing@%/
------
SQL  /*!50000 DROP USER 'split_exec_84'@'%' */;
行为 DROP User(1:19~1:38) /test/1/split_exec_84@%/
------
SQL  DROP USER 'split_life_97_a'@'%','split_life_97_c'@'localhost';
行为 DROP User(1:10~1:31) /test/1/split_life_97_a@%/
行为 DROP User(1:32~1:61) /test/1/split_life_97_c@localhost/
------
SQL  DROP USER IF EXISTS 'split_life_97_missing'@'%';
行为 DROP User(1:20~1:47) /test/1/split_life_97_missing@%/
------
SQL  /*!50000 DROP USER 'split_exec_97'@'%' */;
行为 DROP User(1:19~1:38) /test/1/split_exec_97@%/

## SESSION_VARIABLE_RW

SQL  SET @plain.name$ = 1, @'hyphen-name' := 2, @"space name" = 3, @`tick-name` := 4;
行为 READ ConfigKey(1:4~1:16) /test/1/plain.name$/
行为 READ ConfigKey(1:22~1:36) /test/1/'hyphen-name'/
行为 READ ConfigKey(1:43~1:56) /test/1/"spacename"/
行为 READ ConfigKey(1:62~1:74) /test/1/`tick-name`/
------
SQL  SET @library := 1;
行为 READ ConfigKey(1:4~1:12) /test/1/library/
------
SQL  SET @aes_iva = REPEAT('a', 16), @aes_ivb = REPEAT('b', 16), @aes_key1 = REPEAT('c', 16), @aes_key2 = REPEAT('d', 16);
行为 READ ConfigKey(1:4~1:12) /test/1/aes_iva/
行为 READ ConfigKey(1:32~1:40) /test/1/aes_ivb/
行为 READ ConfigKey(1:60~1:69) /test/1/aes_key1/
行为 READ ConfigKey(1:89~1:98) /test/1/aes_key2/
行为 CALL Function(1:15~1:21) /test/1/catalog1/schema1/REPEAT/
------
SQL  SET @aes_misc_short_key = RANDOM_BYTES(1), @aes_misc_long_key = RANDOM_BYTES(1024);
行为 READ ConfigKey(1:4~1:23) /test/1/aes_misc_short_key/
行为 READ ConfigKey(1:43~1:61) /test/1/aes_misc_long_key/
行为 CALL Function(1:26~1:38) /test/1/catalog1/schema1/RANDOM_BYTES/
------
SQL  SET @des_nossl_before_flush=DES_DECRYPT(DES_ENCRYPT('hello'));
行为 READ ConfigKey(1:4~1:27) /test/1/des_nossl_before_flush/
行为 CALL Function(1:28~1:39) /test/1/catalog1/schema1/DES_DECRYPT/
行为 CALL Function(1:40~1:51) /test/1/catalog1/schema1/DES_ENCRYPT/
------
SQL  SET @old_collation_connection=@@collation_connection;
行为 READ ConfigKey(1:4~1:29) /test/1/old_collation_connection/
行为 READ ConfigKey(1:30~1:52) /test/1/collation_connection/
------
SQL  SET @func_test_right:='11';
行为 READ ConfigKey(1:4~1:20) /test/1/func_test_right/
------
SQL  SET @uuid_native = UUID();
行为 READ ConfigKey(1:4~1:16) /test/1/uuid_native/
行为 CALL Function(1:19~1:23) /test/1/catalog1/schema1/UUID/
------
SQL  SET @join_outer_row := (SELECT ROW(1, 2) =\n                               ROW((SELECT 1 FROM join_set LEFT JOIN join_set AS joined_set ON 1), 1));
行为 READ ConfigKey(1:4~1:19) /test/1/join_outer_row/
行为 CALL Function(1:31~1:34) /test/1/catalog1/schema1/ROW/
行为 READ Table(2:50~2:58) /test/1/catalog1/schema1/join_set/
------
SQL  SET @max_allowed_packet=@@global.max_allowed_packet;
行为 READ ConfigKey(1:4~1:23) /test/1/max_allowed_packet/
------
SQL  SET @net_buffer_length=@@global.net_buffer_length;
行为 READ ConfigKey(1:4~1:22) /test/1/net_buffer_length/
------
SQL  SET @parse_gcol_expr = 1;
行为 READ ConfigKey(1:4~1:20) /test/1/parse_gcol_expr/
------
SQL  SET @select = @@SESSION.sql_mode;
行为 READ ConfigKey(1:4~1:11) /test/1/select/
行为 READ ConfigKey(1:14~1:32) /test/1/sql_mode/
------
SQL  SET @ps_id = 1;
行为 READ ConfigKey(1:4~1:10) /test/1/ps_id/
------
SQL  SET @ps_id = 2;
行为 READ ConfigKey(1:4~1:10) /test/1/ps_id/
------
SQL  SET @ps_sql = 'UPDATE split84.ps_t SET v = ? WHERE id = ?';
行为 READ ConfigKey(1:4~1:11) /test/1/ps_sql/
------
SQL  SET @ps_v = 99, @ps_id = 1;
行为 READ ConfigKey(1:4~1:9) /test/1/ps_v/
行为 READ ConfigKey(1:16~1:22) /test/1/ps_id/
------
SQL  SET @ps_id = 3, @ps_v = 30, @ps_c = 'c';
行为 READ ConfigKey(1:4~1:10) /test/1/ps_id/
行为 READ ConfigKey(1:16~1:21) /test/1/ps_v/
行为 READ ConfigKey(1:28~1:33) /test/1/ps_c/
------
SQL  SET @ps_id = 3;
行为 READ ConfigKey(1:4~1:10) /test/1/ps_id/
------
SQL  SET @sq_a:=(SELECT a FROM split_subquery_next.var_t);
行为 READ ConfigKey(1:4~1:9) /test/1/sq_a/
行为 READ Table(1:26~1:51) /test/1/catalog1/split_subquery_next/var_t/
------
SQL  SET @maxint=18446744073709551615;
行为 READ ConfigKey(1:4~1:11) /test/1/maxint/
------
SQL  SET @hs=X'41', @ha=X'41'+0, @hc=CAST(X'41' AS UNSIGNED);
行为 READ ConfigKey(1:4~1:7) /test/1/hs/
行为 READ ConfigKey(1:15~1:18) /test/1/ha/
行为 READ ConfigKey(1:28~1:31) /test/1/hc/
行为 CALL Function(1:32~1:36) /test/1/catalog1/schema1/CAST/
------
SQL  SET @'a''b'=1, @"c""d"=2, @`e``f`=3;
行为 READ ConfigKey(1:4~1:11) /test/1/'a''b'/
行为 READ ConfigKey(1:15~1:22) /test/1/"c""d"/
行为 READ ConfigKey(1:26~1:33) /test/1/`e``f`/
------
SQL  SET @'semi;name'=1;
行为 READ ConfigKey(1:4~1:16) /test/1/'semi;name'/
------
SQL  SET @s1=_latin1'abc',@s2=_latin2'abc';
行为 READ ConfigKey(1:4~1:7) /test/1/s1/
行为 READ ConfigKey(1:21~1:24) /test/1/s2/
------
SQL  SET @s=BINARY 'New York';
行为 READ ConfigKey(1:4~1:6) /test/1/s/
------
SQL  set @var = 1;
行为 READ ConfigKey(1:4~1:8) /test/1/var/
------
SQL  SET @join_outer_row := (SELECT ROW(1, 2) =\n                               ROW((SELECT 1 FROM join_set LEFT JOIN join_set AS joined_set ON 1), 1));
行为 READ ConfigKey(1:4~1:19) /test/1/join_outer_row/
行为 READ Table(2:50~2:58) /test/1/catalog1/schema1/join_set/
------
SQL  SET @myvar=(WITH qn AS (SELECT a,SUM(b) AS s FROM t1 GROUP BY a) SELECT s FROM qn GROUP BY a HAVING s IS NOT NULL);
行为 READ ConfigKey(1:4~1:10) /test/1/myvar/
行为 CALL Function(1:33~1:36) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:50~1:52) /test/1/catalog1/schema1/t1/
------
SQL  SET @e:=1;
行为 READ ConfigKey(1:4~1:6) /test/1/e/

## MAINTAIN_LOG

SQL  FLUSH LOCAL BINARY LOGS;
行为 ADMIN Log(1:0~1:23) /test/1/
------
SQL  FLUSH NO_WRITE_TO_BINLOG BINARY LOGS;
行为 ADMIN Log(1:0~1:36) /test/1/
------
SQL  FLUSH BINARY LOGS;
行为 ADMIN Log(1:0~1:17) /test/1/
------
SQL  FLUSH ENGINE LOGS;
行为 ADMIN Log(1:0~1:17) /test/1/
------
SQL  FLUSH ERROR LOGS;
行为 ADMIN Log(1:0~1:16) /test/1/
------
SQL  FLUSH GENERAL LOGS;
行为 ADMIN Log(1:0~1:18) /test/1/
------
SQL  FLUSH LOGS;
行为 ADMIN Log(1:0~1:10) /test/1/
------
SQL  FLUSH RELAY LOGS;
行为 ADMIN Log(1:0~1:16) /test/1/
------
SQL  FLUSH SLOW LOGS;
行为 ADMIN Log(1:0~1:15) /test/1/
------
SQL  FLUSH ERROR LOGS, ENGINE LOGS, GENERAL LOGS, SLOW LOGS, BINARY LOGS, RELAY LOGS, QUERY CACHE, HOSTS, PRIVILEGES, LOGS, STATUS, DES_KEY_FILE, USER_RESOURCES;
行为 ADMIN Log(1:0~1:155) /test/1/
------
SQL  PURGE BINARY LOGS TO 'split-bin.999999';
行为 ADMIN Log(1:0~1:39) /test/1/
------
SQL  PURGE BINARY LOGS BEFORE '2000-01-01 00:00:00';
行为 ADMIN Log(1:0~1:46) /test/1/
------
SQL  PURGE BINARY LOGS BEFORE CURRENT_TIMESTAMP - INTERVAL 1 DAY;
行为 ADMIN Log(1:0~1:59) /test/1/
行为 CALL Function(1:25~1:42) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  PURGE MASTER LOGS TO 'split-bin.999999';
行为 ADMIN Log(1:0~1:39) /test/1/
------
SQL  PURGE MASTER LOGS BEFORE CURRENT_TIMESTAMP - INTERVAL 1 DAY;
行为 ADMIN Log(1:0~1:59) /test/1/
行为 CALL Function(1:25~1:42) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  RESET MASTER;
行为 ADMIN Log(1:0~1:12) /test/1/
------
SQL  FLUSH RELAY LOGS FOR CHANNEL 'split_chan';
行为 ADMIN Log(1:0~1:41) /test/1/
------
SQL  FLUSH ERROR LOGS, ENGINE LOGS, GENERAL LOGS, SLOW LOGS, BINARY LOGS, RELAY LOGS, QUERY CACHE, HOSTS, PRIVILEGES, LOGS, STATUS, DES_KEY_FILE, USER_RESOURCES, OPTIMIZER_COSTS;
行为 ADMIN Log(1:0~1:172) /test/1/
------
SQL  FLUSH LOCAL RELAY LOGS FOR CHANNEL 'codex_gap7_ch';
行为 ADMIN Log(1:0~1:50) /test/1/
------
SQL  FLUSH NO_WRITE_TO_BINLOG RELAY LOGS FOR CHANNEL 'codex_gap7_ch';
行为 ADMIN Log(1:0~1:63) /test/1/
------
SQL  FLUSH ERROR LOGS, ENGINE LOGS, GENERAL LOGS, SLOW LOGS, BINARY LOGS, RELAY LOGS, HOSTS, PRIVILEGES, LOGS, STATUS, USER_RESOURCES, OPTIMIZER_COSTS;
行为 ADMIN Log(1:0~1:145) /test/1/
------
SQL  RESET MASTER TO 7;
行为 ADMIN Log(1:0~1:17) /test/1/
------
SQL  RESET MASTER TO 0x7;
行为 ADMIN Log(1:0~1:19) /test/1/
------
SQL  RESET MASTER TO X'07';
行为 ADMIN Log(1:0~1:21) /test/1/
------
SQL  RESET MASTER TO 0;
行为 ADMIN Log(1:0~1:17) /test/1/
------
SQL  RESET BINARY LOGS AND GTIDS;
行为 ADMIN Log(1:0~1:27) /test/1/
------
SQL  FLUSH ERROR LOGS, ENGINE LOGS, GENERAL LOGS, SLOW LOGS, BINARY LOGS, RELAY LOGS, PRIVILEGES, LOGS, STATUS, USER_RESOURCES, OPTIMIZER_COSTS;
行为 ADMIN Log(1:0~1:138) /test/1/
------
SQL  RESET BINARY LOGS AND GTIDS TO 7;
行为 ADMIN Log(1:0~1:32) /test/1/
------
SQL  RESET BINARY LOGS AND GTIDS TO 0x7;
行为 ADMIN Log(1:0~1:34) /test/1/
------
SQL  RESET BINARY LOGS AND GTIDS TO X'07';
行为 ADMIN Log(1:0~1:36) /test/1/
------
SQL  RESET BINARY LOGS AND GTIDS TO 0;
行为 ADMIN Log(1:0~1:32) /test/1/

## ADMIN_PERFORMANCE

SQL  FLUSH NO_WRITE_TO_BINLOG STATUS;
行为 ADMIN Instance(1:0~1:31) /test/1/
------
SQL  FLUSH STATUS;
行为 ADMIN Instance(1:0~1:12) /test/1/
------
SQL  FLUSH USER_RESOURCES;
行为 ADMIN Instance(1:0~1:20) /test/1/
------
SQL  FLUSH LOCAL STATUS;
行为 ADMIN Instance(1:0~1:18) /test/1/
------
SQL  FLUSH STATUS, USER_RESOURCES;
行为 ADMIN Instance(1:0~1:28) /test/1/
------
SQL  FLUSH HOSTS;
行为 ADMIN Instance(1:0~1:11) /test/1/
------
SQL  CACHE INDEX split84.maint_myisam KEY(idx_v) IN DEFAULT;
行为 ADMIN Index(1:12~1:32) /test/1/catalog1/split84/maint_myisam/
------
SQL  LOAD INDEX INTO CACHE split84.maint_myisam KEY(idx_v) IGNORE LEAVES;
行为 ADMIN Instance(1:0~1:67) /test/1/
------
SQL  CACHE INDEX split_part.maint_part PARTITION (p0) KEY(idx_v) IN DEFAULT;
行为 ADMIN Index(1:12~1:33) /test/1/catalog1/split_part/maint_part/
------
SQL  LOAD INDEX INTO CACHE split_part.maint_part PARTITION (p0) KEY(idx_v) IGNORE LEAVES;
行为 ADMIN Instance(1:0~1:83) /test/1/
------
SQL  RESET QUERY CACHE;
行为 ADMIN Instance(1:0~1:17) /test/1/
------
SQL  CACHE INDEX t3 IN keycache1;
行为 ADMIN Index(1:12~1:14) /test/1/catalog1/schema1/t3/
------
SQL  /*!50000 FLUSH STATUS */;
行为 ADMIN Instance(1:9~1:21) /test/1/
------
SQL  CACHE INDEX `` IN cache_name;
行为 ADMIN Instance(1:0~1:28) /test/1/
------
SQL  CACHE INDEX `` PARTITION (ALL) IN cache_name;
行为 ADMIN Instance(1:0~1:44) /test/1/
------
SQL  LOAD INDEX INTO CACHE `` PARTITION (ALL);
行为 ADMIN Instance(1:0~1:40) /test/1/
------
SQL  LOAD INDEX INTO CACHE ``;
行为 ADMIN Instance(1:0~1:24) /test/1/
------
SQL  FLUSH QUERY CACHE;
行为 ADMIN Instance(1:0~1:17) /test/1/
------
SQL  LOAD INDEX INTO CACHE utility_audit.missing_part PARTITION (p0,p1) KEY (PRIMARY,idx_v);
行为 ADMIN Instance(1:0~1:86) /test/1/
------
SQL  CACHE INDEX utility_audit.myisam_a, utility_audit.myisam_b IN DEFAULT;
行为 ADMIN Index(1:12~1:34) /test/1/catalog1/utility_audit/myisam_a/
行为 ADMIN Index(1:36~1:58) /test/1/catalog1/utility_audit/myisam_b/
------
SQL  LOAD INDEX INTO CACHE utility_audit.myisam_a, utility_audit.myisam_b IGNORE LEAVES;
行为 ADMIN Instance(1:0~1:82) /test/1/
------
SQL  CACHE INDEX utility_audit.myisam_a INDEX (PRIMARY, idx_v) IN DEFAULT;
行为 ADMIN Index(1:12~1:34) /test/1/catalog1/utility_audit/myisam_a/
------
SQL  CACHE INDEX utility_audit.missing_part PARTITION (p0,p1) IN DEFAULT;
行为 ADMIN Index(1:12~1:38) /test/1/catalog1/utility_audit/missing_part/
------
SQL  FLUSH OPTIMIZER_COSTS;
行为 ADMIN Instance(1:0~1:21) /test/1/

## SYSTEM_SETTING_WRITE

SQL  FLUSH PRIVILEGES;
行为 CONFIGURE ConfigKey(1:0~1:16) /test/1/
------
SQL  FLUSH NO_WRITE_TO_BINLOG PRIVILEGES;
行为 CONFIGURE ConfigKey(1:0~1:35) /test/1/
------
SQL  SET GLOBAL old_passwords=1;
行为 CONFIGURE ConfigKey(1:4~1:24) /test/1/old_passwords/
------
SQL  INSTALL PLUGIN split_missing_plugin_56 SONAME 'split_missing_plugin.so';
行为 CONFIGURE ConfigKey(1:0~1:71) /test/1/
------
SQL  UNINSTALL PLUGIN split_missing_plugin_56;
行为 CONFIGURE ConfigKey(1:0~1:40) /test/1/
------
SQL  SET GLOBAL max_allowed_packet=100;
行为 CONFIGURE ConfigKey(1:4~1:29) /test/1/max_allowed_packet/
------
SQL  SET GLOBAL net_buffer_length=100;
行为 CONFIGURE ConfigKey(1:4~1:28) /test/1/net_buffer_length/
------
SQL  SET GLOBAL max_allowed_packet=DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:29) /test/1/max_allowed_packet/
------
SQL  SET GLOBAL net_buffer_length=DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:28) /test/1/net_buffer_length/
------
SQL  SET GLOBAL max_allowed_packet=@max_allowed_packet;
行为 CONFIGURE ConfigKey(1:4~1:29) /test/1/max_allowed_packet/
行为 READ ConfigKey(1:30~1:49) /test/1/max_allowed_packet/
------
SQL  SET GLOBAL net_buffer_length=@net_buffer_length;
行为 CONFIGURE ConfigKey(1:4~1:28) /test/1/net_buffer_length/
行为 READ ConfigKey(1:29~1:47) /test/1/net_buffer_length/
------
SQL  SET @@global.parser_max_mem_size=1000*1000*100;
行为 CONFIGURE ConfigKey(1:4~1:32) /test/1/parser_max_mem_size/
------
SQL  SET @@global.parser_max_mem_size=DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:32) /test/1/parser_max_mem_size/
------
SQL  FLUSH DES_KEY_FILE;
行为 CONFIGURE ConfigKey(1:0~1:18) /test/1/
------
SQL  SET @@PERSIST.max_connections = 1001;
行为 CONFIGURE ConfigKey(1:4~1:29) /test/1/max_connections/
------
SQL  SET @@PERSIST_ONLY.back_log = 1001;
行为 CONFIGURE ConfigKey(1:4~1:27) /test/1/back_log/
------
SQL  SET PERSIST = DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:21) /test/1/
------
SQL  DROP SERVER IF EXISTS split_srv_56;
行为 CONFIGURE ConfigKey(1:22~1:34) /test/1/catalog1/schema1/split_srv_56/
------
SQL  CREATE SERVER split_srv_56 FOREIGN DATA WRAPPER mysql OPTIONS (HOST '127.0.0.1', DATABASE 'split56', USER 'root', PASSWORD '123456', SOCKET '/tmp/mysql.sock', OWNER 'split_owner', PORT 3306);
行为 CONFIGURE ConfigKey(1:14~1:26) /test/1/catalog1/schema1/split_srv_56/
------
SQL  ALTER SERVER split_srv_56 OPTIONS (USER 'split_user', HOST 'localhost', PORT 3307);
行为 CONFIGURE ConfigKey(1:13~1:25) /test/1/catalog1/schema1/split_srv_56/
------
SQL  DROP SERVER split_srv_56;
行为 CONFIGURE ConfigKey(1:12~1:24) /test/1/catalog1/schema1/split_srv_56/
------
SQL  ALTER SERVER audit_srv_missing OPTIONS (PASSWORD 'x');
行为 CONFIGURE ConfigKey(1:13~1:30) /test/1/catalog1/schema1/audit_srv_missing/
------
SQL  CREATE SERVER audit_srv_min FOREIGN DATA WRAPPER mysql OPTIONS (HOST '127.0.0.1');
行为 CONFIGURE ConfigKey(1:14~1:27) /test/1/catalog1/schema1/audit_srv_min/
------
SQL  ALTER SERVER 'audit_srv_q' OPTIONS (USER 'u');
行为 CONFIGURE ConfigKey(1:13~1:26) /test/1/catalog1/schema1/'audit_srv_q'/
------
SQL  CREATE SERVER 'audit_srv_q' FOREIGN DATA WRAPPER 'mysql' OPTIONS (HOST '127.0.0.1');
行为 CONFIGURE ConfigKey(1:14~1:27) /test/1/catalog1/schema1/'audit_srv_q'/
------
SQL  DROP SERVER IF EXISTS 'audit_srv_q';
行为 CONFIGURE ConfigKey(1:22~1:35) /test/1/catalog1/schema1/'audit_srv_q'/
------
SQL  INSTALL PLUGIN split_missing_plugin_57 SONAME 'split_missing_plugin.so';
行为 CONFIGURE ConfigKey(1:0~1:71) /test/1/
------
SQL  UNINSTALL PLUGIN split_missing_plugin_57;
行为 CONFIGURE ConfigKey(1:0~1:40) /test/1/
------
SQL  DROP SERVER IF EXISTS split_srv_57;
行为 CONFIGURE ConfigKey(1:22~1:34) /test/1/catalog1/schema1/split_srv_57/
------
SQL  CREATE SERVER split_srv_57 FOREIGN DATA WRAPPER mysql OPTIONS (HOST '127.0.0.1', DATABASE 'split57', USER 'root', PASSWORD '123456', SOCKET '/tmp/mysql.sock', OWNER 'split_owner', PORT 3306);
行为 CONFIGURE ConfigKey(1:14~1:26) /test/1/catalog1/schema1/split_srv_57/
------
SQL  ALTER SERVER split_srv_57 OPTIONS (USER 'split_user', HOST 'localhost', PORT 3307);
行为 CONFIGURE ConfigKey(1:13~1:25) /test/1/catalog1/schema1/split_srv_57/
------
SQL  DROP SERVER split_srv_57;
行为 CONFIGURE ConfigKey(1:12~1:24) /test/1/catalog1/schema1/split_srv_57/
------
SQL  ALTER INSTANCE ROTATE INNODB MASTER KEY;
行为 CONFIGURE ConfigKey(1:0~1:39) /test/1/
------
SQL  set global max_connections = 200;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/max_connections/
------
SQL  RESET PERSIST IF EXISTS max_connections;
行为 CONFIGURE ConfigKey(1:24~1:39) /test/1/max_connections/
------
SQL  RESET PERSIST;
行为 CONFIGURE ConfigKey(1:0~1:13) /test/1/
------
SQL  RESET PERSIST default.key_buffer_size;
行为 CONFIGURE ConfigKey(1:14~1:37) /test/1/default.key_buffer_size/
------
SQL  RESET PERSIST IF EXISTS default.key_buffer_size;
行为 CONFIGURE ConfigKey(1:24~1:47) /test/1/default.key_buffer_size/
------
SQL  INSTALL COMPONENT 'file://component_validate_password';
行为 CONFIGURE ConfigKey(1:0~1:54) /test/1/
------
SQL  UNINSTALL COMPONENT 'file://component_validate_password';
行为 CONFIGURE ConfigKey(1:0~1:56) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_validate_password', 'file://component_log_filter_dragnet';
行为 CONFIGURE ConfigKey(1:0~1:93) /test/1/
------
SQL  UNINSTALL COMPONENT 'file://component_log_filter_dragnet', 'file://component_validate_password';
行为 CONFIGURE ConfigKey(1:0~1:95) /test/1/
------
SQL  INSTALL PLUGIN split_missing_plugin_80 SONAME 'split_missing_plugin.so';
行为 CONFIGURE ConfigKey(1:0~1:71) /test/1/
------
SQL  UNINSTALL PLUGIN split_missing_plugin_80;
行为 CONFIGURE ConfigKey(1:0~1:40) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_split_missing_a', 'file://component_split_missing_b' SET length = 12;
行为 CONFIGURE ConfigKey(1:0~1:104) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_validate_password' SET length = 12;
行为 CONFIGURE ConfigKey(1:0~1:70) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_validate_password' SET GLOBAL validate_password.length = 12;
行为 CONFIGURE ConfigKey(1:0~1:95) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_validate_password' SET PERSIST validate_password.length = 12;
行为 CONFIGURE ConfigKey(1:0~1:96) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_validate_password' SET length = 12 + 3, validate_password.check_user_name = ON;
行为 CONFIGURE ConfigKey(1:0~1:114) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_validate_password' SET validate_password.length = 16, PERSIST validate_password.number_count = 13;
行为 CONFIGURE ConfigKey(1:0~1:133) /test/1/
------
SQL  SET PERSIST max_connections = 1000;
行为 CONFIGURE ConfigKey(1:4~1:27) /test/1/max_connections/
------
SQL  SET PERSIST_ONLY back_log = 1000;
行为 CONFIGURE ConfigKey(1:4~1:25) /test/1/back_log/
------
SQL  SET GLOBAL max_binlog_cache_size = 1024 * 1024 * 1024;
行为 CONFIGURE ConfigKey(1:4~1:32) /test/1/max_binlog_cache_size/
------
SQL  INSTALL COMPONENT 'file://component_split_missing' SET length=NULL;
行为 CONFIGURE ConfigKey(1:0~1:66) /test/1/
------
SQL  INSTALL COMPONENT 'file://component_split_missing' SET length=@gizmo;
行为 CONFIGURE ConfigKey(1:0~1:68) /test/1/
行为 READ ConfigKey(1:62~1:68) /test/1/gizmo/
------
SQL  INSTALL COMPONENT 'file://component_split_missing' SET length=@@global.max_connections;
行为 CONFIGURE ConfigKey(1:0~1:86) /test/1/
行为 READ ConfigKey(1:62~1:86) /test/1/max_connections/
------
SQL  INSTALL COMPONENT 'file://component_split_missing' SET length=CAST(RAND()*10 AS SIGNED);
行为 CONFIGURE ConfigKey(1:0~1:87) /test/1/
行为 CALL Function(1:62~1:66) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:67~1:71) /test/1/catalog1/schema1/RAND/
------
SQL  INSTALL COMPONENT 'file://component_split_missing' SET length=SUM(100);
行为 CONFIGURE ConfigKey(1:0~1:70) /test/1/
行为 CALL Function(1:62~1:65) /test/1/catalog1/schema1/SUM/
------
SQL  DROP SPATIAL REFERENCE SYSTEM IF EXISTS 70000001;
行为 CONFIGURE ConfigKey(1:40~1:48) /test/1/catalog1/schema1/70000001/
------
SQL  DROP SPATIAL REFERENCE SYSTEM IF EXISTS 70000002;
行为 CONFIGURE ConfigKey(1:40~1:48) /test/1/catalog1/schema1/70000002/
------
SQL  CREATE SPATIAL REFERENCE SYSTEM 70000001\n  NAME 'split_srs_a'\n  DEFINITION 'GEOGCS["split_srs_a",DATUM["split_datum_a",SPHEROID["split_spheroid_a",6378137,298.257223563]],PRIMEM["Greenwich",0],UNIT["degree",0.017453292519943278],AXIS["Lat",NORTH],AXIS["Lon",EAST]]';
行为 CONFIGURE ConfigKey(1:32~1:40) /test/1/catalog1/schema1/70000001/
------
SQL  CREATE OR REPLACE SPATIAL REFERENCE SYSTEM 70000001\n  ORGANIZATION 'SPLIT' IDENTIFIED BY 70000001\n  DESCRIPTION 'split replacement'\n  NAME 'split_srs_a_replace'\n  DEFINITION 'GEOGCS["split_srs_a_replace",DATUM["split_datum_ar",SPHEROID["split_spheroid_ar",6378137,298.257223563]],PRIMEM["Greenwich",0],UNIT["degree",0.017453292519943278],AXIS["Lat",NORTH],AXIS["Lon",EAST]]';
行为 CONFIGURE ConfigKey(1:43~1:51) /test/1/catalog1/schema1/70000001/
------
SQL  CREATE SPATIAL REFERENCE SYSTEM IF NOT EXISTS 70000002\n  DEFINITION 'GEOGCS["split_srs_b",DATUM["split_datum_b",SPHEROID["split_spheroid_b",6378137,298.257223563]],PRIMEM["Greenwich",0],UNIT["degree",0.017453292519943278],AXIS["Lon",EAST],AXIS["Lat",NORTH]]'\n  NAME 'split_srs_b';
行为 CONFIGURE ConfigKey(1:46~1:54) /test/1/catalog1/schema1/70000002/
------
SQL  DROP SPATIAL REFERENCE SYSTEM 70000002;
行为 CONFIGURE ConfigKey(1:30~1:38) /test/1/catalog1/schema1/70000002/
------
SQL  DROP SERVER IF EXISTS split_srv_80;
行为 CONFIGURE ConfigKey(1:22~1:34) /test/1/catalog1/schema1/split_srv_80/
------
SQL  CREATE SERVER split_srv_80 FOREIGN DATA WRAPPER mysql OPTIONS (HOST '127.0.0.1', DATABASE 'split80', USER 'root', PASSWORD '123456', SOCKET '/tmp/mysql.sock', OWNER 'split_owner', PORT 3306);
行为 CONFIGURE ConfigKey(1:14~1:26) /test/1/catalog1/schema1/split_srv_80/
------
SQL  ALTER SERVER split_srv_80 OPTIONS (USER 'split_user', HOST 'localhost', PORT 3307);
行为 CONFIGURE ConfigKey(1:13~1:25) /test/1/catalog1/schema1/split_srv_80/
------
SQL  DROP SERVER split_srv_80;
行为 CONFIGURE ConfigKey(1:12~1:24) /test/1/catalog1/schema1/split_srv_80/
------
SQL  CREATE SPATIAL REFERENCE SYSTEM 70000005 DEFINITION 'GEOGCS["audit_def_only",DATUM["audit_datum",SPHEROID["audit_spheroid",6378137,298.257223563]],PRIMEM["Greenwich",0],UNIT["degree",0.017453292519943278],AXIS["Lat",NORTH],AXIS["Lon",EAST]]';
行为 CONFIGURE ConfigKey(1:32~1:40) /test/1/catalog1/schema1/70000005/
------
SQL  CREATE SPATIAL REFERENCE SYSTEM 70000003;
行为 CONFIGURE ConfigKey(1:32~1:40) /test/1/catalog1/schema1/70000003/
------
SQL  CREATE SPATIAL REFERENCE SYSTEM 70000004 NAME 'audit_name_only';
行为 CONFIGURE ConfigKey(1:32~1:40) /test/1/catalog1/schema1/70000004/
------
SQL  ALTER INSTANCE RELOAD TLS;
行为 CONFIGURE ConfigKey(1:0~1:25) /test/1/
------
SQL  ALTER INSTANCE RELOAD TLS NO ROLLBACK ON ERROR;
行为 CONFIGURE ConfigKey(1:0~1:46) /test/1/
------
SQL  ALTER INSTANCE RELOAD TLS FOR CHANNEL mysql_main;
行为 CONFIGURE ConfigKey(1:0~1:48) /test/1/
------
SQL  ALTER INSTANCE RELOAD TLS FOR CHANNEL mysql_main NO ROLLBACK ON ERROR;
行为 CONFIGURE ConfigKey(1:0~1:69) /test/1/
------
SQL  ALTER INSTANCE RELOAD TLS FOR CHANNEL mysql_admin;
行为 CONFIGURE ConfigKey(1:0~1:49) /test/1/
------
SQL  ALTER INSTANCE RELOAD TLS FOR CHANNEL mysql_admin NO ROLLBACK ON ERROR;
行为 CONFIGURE ConfigKey(1:0~1:70) /test/1/
------
SQL  ALTER INSTANCE RELOAD KEYRING;
行为 CONFIGURE ConfigKey(1:0~1:29) /test/1/
------
SQL  INSTALL PLUGIN split_missing_plugin_84 SONAME 'split_missing_plugin.so';
行为 CONFIGURE ConfigKey(1:0~1:71) /test/1/
------
SQL  UNINSTALL PLUGIN split_missing_plugin_84;
行为 CONFIGURE ConfigKey(1:0~1:40) /test/1/
------
SQL  DROP SERVER IF EXISTS split_srv84;
行为 CONFIGURE ConfigKey(1:22~1:33) /test/1/catalog1/schema1/split_srv84/
------
SQL  CREATE SERVER split_srv84\n  FOREIGN DATA WRAPPER mysql\n  OPTIONS (\n    HOST '127.0.0.1',\n    DATABASE 'split84',\n    USER 'root',\n    PASSWORD '123456',\n    SOCKET '/tmp/mysql.sock',\n    OWNER 'split_owner',\n    PORT 3306\n  );
行为 CONFIGURE ConfigKey(1:14~1:25) /test/1/catalog1/schema1/split_srv84/
------
SQL  ALTER SERVER split_srv84\n  OPTIONS (\n    USER 'split_user',\n    HOST 'localhost',\n    PORT 3307\n  );
行为 CONFIGURE ConfigKey(1:13~1:24) /test/1/catalog1/schema1/split_srv84/
------
SQL  DROP SERVER split_srv84;
行为 CONFIGURE ConfigKey(1:12~1:23) /test/1/catalog1/schema1/split_srv84/
------
SQL  INSTALL PLUGIN split_missing_plugin_97 SONAME 'split_missing_plugin.so';
行为 CONFIGURE ConfigKey(1:0~1:71) /test/1/
------
SQL  UNINSTALL PLUGIN split_missing_plugin_97;
行为 CONFIGURE ConfigKey(1:0~1:40) /test/1/
------
SQL  DROP SERVER IF EXISTS split_srv_97;
行为 CONFIGURE ConfigKey(1:22~1:34) /test/1/catalog1/schema1/split_srv_97/
------
SQL  CREATE SERVER split_srv_97 FOREIGN DATA WRAPPER mysql OPTIONS (HOST '127.0.0.1', DATABASE 'split97', USER 'root', PASSWORD '123456', SOCKET '/tmp/mysql.sock', OWNER 'split_owner', PORT 3306);
行为 CONFIGURE ConfigKey(1:14~1:26) /test/1/catalog1/schema1/split_srv_97/
------
SQL  ALTER SERVER split_srv_97 OPTIONS (USER 'split_user', HOST 'localhost', PORT 3307);
行为 CONFIGURE ConfigKey(1:13~1:25) /test/1/catalog1/schema1/split_srv_97/
------
SQL  DROP SERVER split_srv_97;
行为 CONFIGURE ConfigKey(1:12~1:24) /test/1/catalog1/schema1/split_srv_97/

## ADMIN_TABLE

SQL  FLUSH TABLES;
行为 ADMIN Table(1:0~1:12) /test/1/
------
SQL  FLUSH TABLE;
行为 ADMIN Table(1:0~1:11) /test/1/
------
SQL  FLUSH TABLES split_verify.flush_a, split_verify.flush_b;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split_verify/flush_a/
行为 ADMIN Table(1:35~1:55) /test/1/catalog1/split_verify/flush_b/
------
SQL  FLUSH TABLES WITH READ LOCK;
行为 ADMIN Table(1:0~1:27) /test/1/
------
SQL  FLUSH TABLE split_verify.flush_a WITH READ LOCK;
行为 ADMIN Table(1:12~1:32) /test/1/catalog1/split_verify/flush_a/
行为 LOCK Table(1:12~1:32) /test/1/catalog1/split_verify/flush_a/
------
SQL  FLUSH NO_WRITE_TO_BINLOG TABLES admin_sql_audit.flush_a, admin_sql_audit.flush_b;
行为 ADMIN Table(1:32~1:55) /test/1/catalog1/admin_sql_audit/flush_a/
行为 ADMIN Table(1:57~1:80) /test/1/catalog1/admin_sql_audit/flush_b/
------
SQL  FLUSH TABLES admin_sql_audit.flush_a, admin_sql_audit.flush_b WITH READ LOCK;
行为 ADMIN Table(1:13~1:36) /test/1/catalog1/admin_sql_audit/flush_a/
行为 ADMIN Table(1:38~1:61) /test/1/catalog1/admin_sql_audit/flush_b/
行为 LOCK Table(1:13~1:36) /test/1/catalog1/admin_sql_audit/flush_a/
行为 LOCK Table(1:38~1:61) /test/1/catalog1/admin_sql_audit/flush_b/
------
SQL  ANALYZE TABLE split84.maint_innodb;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLES split84.maint_innodb;
行为 ADMIN Table(1:15~1:35) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE NO_WRITE_TO_BINLOG TABLE split84.maint_innodb;
行为 ADMIN Table(1:33~1:53) /test/1/catalog1/split84/maint_innodb/
------
SQL  CHECK TABLE split84.maint_innodb FOR UPGRADE;
行为 ADMIN Table(1:12~1:32) /test/1/catalog1/split84/maint_innodb/
------
SQL  CHECK TABLES split84.maint_innodb;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_innodb/
------
SQL  CHECK TABLE split84.maint_innodb QUICK FAST MEDIUM EXTENDED CHANGED;
行为 ADMIN Table(1:12~1:32) /test/1/catalog1/split84/maint_innodb/
------
SQL  CHECKSUM TABLE split84.maint_innodb QUICK;
行为 ADMIN Table(1:15~1:35) /test/1/catalog1/split84/maint_innodb/
------
SQL  CHECKSUM TABLE split84.maint_innodb EXTENDED;
行为 ADMIN Table(1:15~1:35) /test/1/catalog1/split84/maint_innodb/
------
SQL  CHECKSUM TABLES split84.maint_innodb QUICK;
行为 ADMIN Table(1:16~1:36) /test/1/catalog1/split84/maint_innodb/
------
SQL  OPTIMIZE NO_WRITE_TO_BINLOG TABLE split84.maint_innodb;
行为 ADMIN Table(1:34~1:54) /test/1/catalog1/split84/maint_innodb/
------
SQL  OPTIMIZE LOCAL TABLES split84.maint_innodb;
行为 ADMIN Table(1:22~1:42) /test/1/catalog1/split84/maint_innodb/
------
SQL  REPAIR LOCAL TABLE split84.maint_myisam QUICK EXTENDED USE_FRM;
行为 ADMIN Table(1:19~1:39) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLES split84.maint_myisam QUICK;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam EXTENDED;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam USE_FRM;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam QUICK USE_FRM;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam EXTENDED USE_FRM;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam EXTENDED QUICK;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam USE_FRM QUICK EXTENDED;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  REPAIR TABLE split84.maint_myisam QUICK QUICK;
行为 ADMIN Table(1:13~1:33) /test/1/catalog1/split84/maint_myisam/
------
SQL  CHECK TABLE ``;
行为 ADMIN Table(1:0~1:14) /test/1/
------
SQL  ANALYZE TABLE utility_audit.maint_a, utility_audit.maint_b;
行为 ADMIN Table(1:14~1:35) /test/1/catalog1/utility_audit/maint_a/
行为 ADMIN Table(1:37~1:58) /test/1/catalog1/utility_audit/maint_b/
------
SQL  CHECK TABLE utility_audit.maint_a, utility_audit.maint_b QUICK;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/utility_audit/maint_a/
行为 ADMIN Table(1:35~1:56) /test/1/catalog1/utility_audit/maint_b/
------
SQL  CHECKSUM TABLE utility_audit.maint_a, utility_audit.maint_b;
行为 ADMIN Table(1:15~1:36) /test/1/catalog1/utility_audit/maint_a/
行为 ADMIN Table(1:38~1:59) /test/1/catalog1/utility_audit/maint_b/
------
SQL  OPTIMIZE TABLE utility_audit.maint_a, utility_audit.maint_b;
行为 ADMIN Table(1:15~1:36) /test/1/catalog1/utility_audit/maint_a/
行为 ADMIN Table(1:38~1:59) /test/1/catalog1/utility_audit/maint_b/
------
SQL  REPAIR NO_WRITE_TO_BINLOG TABLE utility_audit.myisam_a, utility_audit.myisam_b QUICK;
行为 ADMIN Table(1:32~1:54) /test/1/catalog1/utility_audit/myisam_a/
行为 ADMIN Table(1:56~1:78) /test/1/catalog1/utility_audit/myisam_b/
------
SQL  CHECK TABLE split_check_options.t FOR UPGRADE;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/split_check_options/t/
------
SQL  CHECK TABLE split_check_options.t FAST;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/split_check_options/t/
------
SQL  CHECK TABLE split_check_options.t MEDIUM;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/split_check_options/t/
------
SQL  CHECK TABLE split_check_options.t EXTENDED;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/split_check_options/t/
------
SQL  CHECK TABLE split_check_options.t CHANGED;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/split_check_options/t/
------
SQL  CHECK TABLE split_check_options.t QUICK FAST MEDIUM EXTENDED CHANGED;
行为 ADMIN Table(1:12~1:33) /test/1/catalog1/split_check_options/t/
------
SQL  ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v, c WITH 8 BUCKETS;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLES split84.maint_innodb UPDATE HISTOGRAM ON v WITH 4 BUCKETS;
行为 ADMIN Table(1:15~1:35) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLE split84.maint_innodb DROP HISTOGRAM ON v, c;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLE split_native_gap.t_hist UPDATE HISTOGRAM ON c USING DATA '{}';
行为 ADMIN Table(1:14~1:37) /test/1/catalog1/split_native_gap/t_hist/
------
SQL  ANALYZE LOCAL TABLE split84.maint_innodb UPDATE HISTOGRAM ON v, c WITH 8 BUCKETS AUTO UPDATE;
行为 ADMIN Table(1:20~1:40) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v WITH 4 BUCKETS MANUAL UPDATE;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLE t_hist UPDATE HISTOGRAM ON c1,c3 AUTO UPDATE;
行为 ADMIN Table(1:14~1:20) /test/1/catalog1/schema1/t_hist/
------
SQL  ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v MANUAL UPDATE;
行为 ADMIN Table(1:14~1:34) /test/1/catalog1/split84/maint_innodb/
------
SQL  ANALYZE TABLE vector_lifecycle UPDATE HISTOGRAM ON embedding WITH 8 BUCKETS;
行为 ADMIN Table(1:14~1:30) /test/1/catalog1/schema1/vector_lifecycle/

## DATA_EXPORT

SQL  FLUSH TABLES split_verify.flush_a, split_verify.flush_b FOR EXPORT;
行为 EXPORT Table(1:13~1:33) /test/1/catalog1/split_verify/flush_a/
行为 EXPORT Table(1:35~1:55) /test/1/catalog1/split_verify/flush_b/
------
SQL  FLUSH LOCAL TABLES split_verify.flush_a, split_verify.flush_b FOR EXPORT;
行为 EXPORT Table(1:19~1:39) /test/1/catalog1/split_verify/flush_a/
行为 EXPORT Table(1:41~1:61) /test/1/catalog1/split_verify/flush_b/
------
SQL  FLUSH TABLE export FOR EXPORT;
行为 EXPORT Table(1:12~1:18) /test/1/catalog1/schema1/export/
------
SQL  SELECT 1 INTO OUTFILE '/var/lib/mysql-files/split_outfile_native_no_from.txt';
行为 EXPORT File(1:22~1:77) /test/1/var/lib/mysql-files/split_outfile_native_no_from.txt/
------
SQL  SELECT 1 UNION SELECT 2 INTO OUTFILE '/var/lib/mysql-files/codex_load_audit_union.txt';
行为 EXPORT File(1:37~1:86) /test/1/var/lib/mysql-files/codex_load_audit_union.txt/
------
SQL  CLONE LOCAL DATA DIRECTORY = '/tmp/split_clone_local';
行为 ADMIN Instance(1:0~1:53) /test/1/
------
SQL  CLONE LOCAL DATA DIRECTORY '/tmp/split_clone_local_noeq';
行为 ADMIN Instance(1:0~1:56) /test/1/
------
SQL  SELECT 1 INTO OUTFILE URI 's3://split-bucket/out.csv';
行为 EXPORT File(1:26~1:53) /test/1/s3:/split-bucket/out.csv/
------
SQL  SELECT 1 INTO OUTFILE URL 'https://example.com/out.csv';
行为 EXPORT File(1:26~1:55) /test/1/https:/example.com/out.csv/
------
SQL  SELECT 1 AS id, 'alpha' AS name\nINTO OUTFILE URI 's3://split-bucket/full.csv'\nFORMAT CSV\nCOMPRESSION GZIP\nHEADER ON\nCHARACTER SET utf8mb4\nFIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\'\nLINES TERMINATED BY '\n';
行为 EXPORT File(2:17~2:45) /test/1/s3:/split-bucket/full.csv/
------
SQL  SELECT 1 INTO OUTFILE URL 'https://example.com/no-header.csv' HEADER OFF;
行为 EXPORT File(1:26~1:61) /test/1/https:/example.com/no-header.csv/
------
SQL  SELECT 1 INTO OUTFILE WITH PARAMETERS '{"uri":"s3://split-bucket/params.csv","format":"csv"}';
行为 EXPORT File(1:38~1:93) /test/1/{"uri":"s3:/split-bucket/params.csv","format":"csv"}/
------
SQL  SELECT 1 INTO OUTFILE '/tmp/x.csv' FORMAT CSV;
行为 EXPORT File(1:22~1:34) /test/1/tmp/x.csv/
------
SQL  SELECT 1 INTO OUTFILE '/tmp/x.gz' COMPRESSION GZIP;
行为 EXPORT File(1:22~1:33) /test/1/tmp/x.gz/
------
SQL  SELECT 1 INTO OUTFILE '/tmp/x.csv' HEADER ON;
行为 EXPORT File(1:22~1:34) /test/1/tmp/x.csv/

## ADMIN

SQL  KILL 999999;
行为 ADMIN Instance(1:0~1:11) /test/1/
------
SQL  KILL CONNECTION 999998;
行为 ADMIN Instance(1:0~1:22) /test/1/
------
SQL  KILL QUERY 999997;
行为 ADMIN Instance(1:0~1:17) /test/1/
------
SQL  KILL QUERY @split_kill_id;
行为 READ ConfigKey(1:11~1:25) /test/1/split_kill_id/
------
SQL  KILL QUERY 999990 + 1;
行为 ADMIN Instance(1:0~1:21) /test/1/
------
SQL  KILL CONNECTION CAST(999991 AS UNSIGNED);
行为 ADMIN Instance(1:0~1:40) /test/1/
行为 CALL Function(1:16~1:20) /test/1/catalog1/schema1/CAST/
------
SQL  KILL QUERY (SELECT 999992);
行为 ADMIN Instance(1:0~1:26) /test/1/

## SESSION_LOCK

SQL  LOCK TABLES splitv.lock_view lv READ;
行为 LOCK Table(1:12~1:28) /test/1/catalog1/splitv/lock_view/
------
SQL  LOCK TABLES splitv.lock_write LOW_PRIORITY WRITE;
行为 LOCK Table(1:12~1:29) /test/1/catalog1/splitv/lock_write/
------
SQL  LOCK TABLES t1=a READ;
行为 LOCK Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  LOCK TABLE split84.txn_read READ LOCAL, split84.txn_write AS tw WRITE;
行为 LOCK Table(1:11~1:27) /test/1/catalog1/split84/txn_read/
行为 LOCK Table(1:40~1:57) /test/1/catalog1/split84/txn_write/
------
SQL  UNLOCK TABLE;
行为 LOCK Instance(1:0~1:12) /test/1/
------
SQL  LOCK TABLES split84.txn_read AS tr READ, split84.txn_write WRITE;
行为 LOCK Table(1:12~1:28) /test/1/catalog1/split84/txn_read/
行为 LOCK Table(1:41~1:58) /test/1/catalog1/split84/txn_write/
------
SQL  UNLOCK TABLES;
行为 LOCK Instance(1:0~1:13) /test/1/
------
SQL  LOCK TABLES t1 READ, t1 AS t0 WRITE, t1 AS t2 READ;
行为 LOCK Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  LOCK INSTANCE FOR BACKUP;
行为 LOCK Instance(1:0~1:24) /test/1/
------
SQL  UNLOCK INSTANCE;
行为 LOCK Instance(1:0~1:15) /test/1/

## ALTER_REPLICATION

SQL  RESET SLAVE, QUERY CACHE;
行为 ALTER Replication(1:0~1:24) /test/1/
------
SQL  START SLAVE SQL_THREAD;
行为 ADMIN Replication(1:0~1:22) /test/1/
------
SQL  STOP SLAVE SQL_THREAD;
行为 ADMIN Replication(1:0~1:21) /test/1/
------
SQL  STOP SLAVE;
行为 ADMIN Replication(1:0~1:10) /test/1/
------
SQL  START SLAVE IO_THREAD, SQL_THREAD UNTIL MASTER_LOG_FILE='dummy-log.000001', MASTER_LOG_POS=116 USER='root' PASSWORD='secret' DEFAULT_AUTH='auth_test_plugin' PLUGIN_DIR='/usr/lib/mysql/plugin';
行为 ADMIN Replication(1:0~1:191) /test/1/
------
SQL  START SLAVE IO_THREAD, SQL_THREAD UNTIL RELAY_LOG_FILE='dummy-log.000001', RELAY_LOG_POS=116 USER='root' PASSWORD='secret' DEFAULT_AUTH='auth_test_plugin' PLUGIN_DIR='/usr/lib/mysql/plugin';
行为 ADMIN Replication(1:0~1:189) /test/1/
------
SQL  CHANGE MASTER TO MASTER_HOST='127.0.0.1', MASTER_PORT=9, MASTER_USER='split_repl', MASTER_PASSWORD='pw', MASTER_AUTO_POSITION=0;
行为 ALTER Replication(1:0~1:127) /test/1/
------
SQL  START SLAVE IO_THREAD;
行为 ADMIN Replication(1:0~1:21) /test/1/
------
SQL  STOP SLAVE IO_THREAD;
行为 ADMIN Replication(1:0~1:20) /test/1/
------
SQL  RESET SLAVE ALL;
行为 ALTER Replication(1:0~1:15) /test/1/
------
SQL  CHANGE MASTER TO MASTER_LOG_FILE='binlog.000001', MASTER_LOG_POS=4;
行为 ALTER Replication(1:0~1:66) /test/1/
------
SQL  CHANGE MASTER TO MASTER_BIND='', MASTER_CONNECT_RETRY=7, MASTER_RETRY_COUNT=3, MASTER_DELAY=1;
行为 ALTER Replication(1:0~1:93) /test/1/
------
SQL  CHANGE MASTER TO IGNORE_SERVER_IDS=();
行为 ALTER Replication(1:0~1:37) /test/1/
------
SQL  CHANGE MASTER TO MASTER_HEARTBEAT_PERIOD=0.5, IGNORE_SERVER_IDS=(2,3);
行为 ALTER Replication(1:0~1:69) /test/1/
------
SQL  CHANGE MASTER TO RELAY_LOG_FILE='relay-bin.000001', RELAY_LOG_POS=4;
行为 ALTER Replication(1:0~1:67) /test/1/
------
SQL  CHANGE MASTER TO MASTER_SSL=1, MASTER_SSL_CA='', MASTER_SSL_CAPATH='', MASTER_SSL_CERT='', MASTER_SSL_KEY='', MASTER_SSL_CIPHER='', MASTER_SSL_VERIFY_SERVER_CERT=0, MASTER_SSL_CRL='', MASTER_SSL_CRLPATH='';
行为 ALTER Replication(1:0~1:205) /test/1/
------
SQL  START SLAVE SQL_THREAD UNTIL SQL_AFTER_GTIDS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:1';
行为 ADMIN Replication(1:0~1:85) /test/1/
------
SQL  START SLAVE;
行为 ADMIN Replication(1:0~1:11) /test/1/
------
SQL  START SLAVE SQL_THREAD UNTIL SQL_BEFORE_GTIDS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:1';
行为 ADMIN Replication(1:0~1:86) /test/1/
------
SQL  START SLAVE USER='root' PASSWORD='123456' DEFAULT_AUTH='mysql_native_password' PLUGIN_DIR='/usr/lib/mysql/plugin';
行为 ADMIN Replication(1:0~1:113) /test/1/
------
SQL  START SLAVE IO_THREAD UNTIL MASTER_LOG_FILE='binlog.000001', MASTER_LOG_POS=4;
行为 ADMIN Replication(1:0~1:77) /test/1/
------
SQL  START SLAVE UNTIL SQL_AFTER_MTS_GAPS;
行为 ADMIN Replication(1:0~1:36) /test/1/
------
SQL  START SLAVE SQL_THREAD UNTIL RELAY_LOG_FILE='relay-bin.000001', RELAY_LOG_POS=4;
行为 ADMIN Replication(1:0~1:79) /test/1/
------
SQL  START SLAVE IO_THREAD, SQL_THREAD;
行为 ADMIN Replication(1:0~1:33) /test/1/
------
SQL  STOP SLAVE IO_THREAD, SQL_THREAD;
行为 ADMIN Replication(1:0~1:32) /test/1/
------
SQL  START SLAVE UNTIL MASTER_LOG_FILE = 'master-bin.000001', MASTER_LOG_POS = 4;
行为 ADMIN Replication(1:0~1:75) /test/1/
------
SQL  START SLAVE IO_THREAD UNTIL SQL_BEFORE_GTIDS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:4-5';
行为 ADMIN Replication(1:0~1:87) /test/1/
------
SQL  SET SESSION GTID_NEXT = '11111111-1111-1111-1111-111111111111:audit:1';
行为 ALTER ConfigKey(1:4~1:21) /test/1/GTID_NEXT/
------
SQL  SET SESSION GTID_NEXT = 'AUTOMATIC:audit';
行为 ALTER ConfigKey(1:4~1:21) /test/1/GTID_NEXT/
------
SQL  SET GLOBAL GTID_PURGED = '+11111111-1111-1111-1111-111111111111:audit:1';
行为 ALTER ConfigKey(1:4~1:22) /test/1/GTID_PURGED/
------
SQL  SET GLOBAL sql_slave_skip_counter=1;
行为 ALTER ConfigKey(1:4~1:33) /test/1/sql_slave_skip_counter/
------
SQL  SET @@SESSION.pseudo_slave_mode=DEFAULT;
行为 ALTER ConfigKey(1:4~1:31) /test/1/pseudo_slave_mode/
------
SQL  RESET SLAVE ALL FOR CHANNEL 'split', QUERY CACHE;
行为 ALTER Replication(1:0~1:48) /test/1/
------
SQL  START SLAVE FOR CHANNEL 'any_channel';
行为 ADMIN Replication(1:0~1:37) /test/1/
------
SQL  START GROUP_REPLICATION;
行为 ADMIN Replication(1:0~1:23) /test/1/
------
SQL  STOP GROUP_REPLICATION;
行为 ADMIN Replication(1:0~1:22) /test/1/
------
SQL  CHANGE MASTER TO MASTER_HOST='localhost', MASTER_PORT=3306, MASTER_USER='repl', MASTER_PASSWORD='pw', MASTER_AUTO_POSITION=0 FOR CHANNEL 'legacy80';
行为 ALTER Replication(1:0~1:147) /test/1/
------
SQL  START SLAVE IO_THREAD FOR CHANNEL 'legacy80';
行为 ADMIN Replication(1:0~1:44) /test/1/
------
SQL  STOP SLAVE IO_THREAD FOR CHANNEL 'legacy80';
行为 ADMIN Replication(1:0~1:43) /test/1/
------
SQL  RESET SLAVE ALL FOR CHANNEL 'legacy80';
行为 ALTER Replication(1:0~1:38) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=();
行为 ALTER Replication(1:0~1:44) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(audit_db1,audit_db2);
行为 ALTER Replication(1:0~1:63) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_TABLE=();
行为 ALTER Replication(1:0~1:47) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_TABLE=(audit_db1.t1,audit_db2.t2);
行为 ALTER Replication(1:0~1:72) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_IGNORE_DB=();
行为 ALTER Replication(1:0~1:48) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_IGNORE_DB=(audit_db3,audit_db4);
行为 ALTER Replication(1:0~1:67) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_IGNORE_TABLE=();
行为 ALTER Replication(1:0~1:51) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_IGNORE_TABLE=(audit_db1.t3,audit_db2.t4);
行为 ALTER Replication(1:0~1:76) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(audit_db1), REPLICATE_IGNORE_TABLE=(audit_db2.t2), REPLICATE_WILD_DO_TABLE=('audit_db3.%'), REPLICATE_REWRITE_DB=((audit_old,audit_new));
行为 ALTER Replication(1:0~1:179) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(audit_first), REPLICATE_DO_DB=(audit_last);
行为 ALTER Replication(1:0~1:85) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_REWRITE_DB=();
行为 ALTER Replication(1:0~1:49) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_REWRITE_DB=((audit_old1,audit_new1),(audit_old2,audit_new2));
行为 ALTER Replication(1:0~1:96) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_WILD_DO_TABLE=();
行为 ALTER Replication(1:0~1:52) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_WILD_DO_TABLE=('audit_db1.%','audit_db2.t%');
行为 ALTER Replication(1:0~1:80) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_WILD_IGNORE_TABLE=();
行为 ALTER Replication(1:0~1:56) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_WILD_IGNORE_TABLE=('audit_db3.%','audit_db4.t%');
行为 ALTER Replication(1:0~1:84) /test/1/
------
SQL  CHANGE MASTER TO MASTER_TLS_VERSION='TLSv1.2';
行为 ALTER Replication(1:0~1:45) /test/1/
------
SQL  RESET SLAVE FOR CHANNEL 'audit_slave';
行为 ALTER Replication(1:0~1:37) /test/1/
------
SQL  START SLAVE SQL_THREAD FOR CHANNEL 'audit_slave';
行为 ADMIN Replication(1:0~1:48) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(db1,`db,3`);
行为 ALTER Replication(1:0~1:54) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(d1), REPLICATE_IGNORE_DB=(d2), REPLICATE_DO_TABLE=(d1.t1), REPLICATE_IGNORE_TABLE=(d2.t2), REPLICATE_WILD_DO_TABLE=('d3.%'), REPLICATE_WILD_IGNORE_TABLE=('d4.%'), REPLICATE_REWRITE_DB=((old_db,new_db));
行为 ALTER Replication(1:0~1:244) /test/1/
------
SQL  RESET SLAVE, REPLICA, MASTER TO 7;
行为 ALTER Replication(1:0~1:33) /test/1/
------
SQL  START REPLICA SQL_THREAD;
行为 ADMIN Replication(1:0~1:24) /test/1/
------
SQL  STOP REPLICA SQL_THREAD;
行为 ADMIN Replication(1:0~1:23) /test/1/
------
SQL  STOP REPLICA;
行为 ADMIN Replication(1:0~1:12) /test/1/
------
SQL  START REPLICA FOR CHANNEL 'ch1';
行为 ADMIN Replication(1:0~1:31) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(db2,db32), REPLICATE_DO_DB=(db1,my_db3), REPLICATE_IGNORE_DB=(my_initfiledb3) FOR CHANNEL 'channel_1';
行为 ALTER Replication(1:0~1:144) /test/1/
------
SQL  START REPLICA IO_THREAD, SQL_THREAD UNTIL SOURCE_LOG_FILE='dummy-log.000001', SOURCE_LOG_POS=116 USER='root' PASSWORD='secret' DEFAULT_AUTH='auth_test_plugin' PLUGIN_DIR='/usr/lib/mysql/plugin';
行为 ADMIN Replication(1:0~1:193) /test/1/
------
SQL  START REPLICA IO_THREAD, SQL_THREAD UNTIL RELAY_LOG_FILE='dummy-log.000001', RELAY_LOG_POS=116 USER='root' PASSWORD='secret' DEFAULT_AUTH='auth_test_plugin' PLUGIN_DIR='/usr/lib/mysql/plugin';
行为 ADMIN Replication(1:0~1:191) /test/1/
------
SQL  START GROUP_REPLICATION USER='repl_user';
行为 ADMIN Replication(1:0~1:40) /test/1/
------
SQL  START GROUP_REPLICATION PASSWORD='secret';
行为 ADMIN Replication(1:0~1:41) /test/1/
------
SQL  START GROUP_REPLICATION DEFAULT_AUTH='caching_sha2_password';
行为 ADMIN Replication(1:0~1:60) /test/1/
------
SQL  START GROUP_REPLICATION USER='repl_user', PASSWORD='secret';
行为 ADMIN Replication(1:0~1:59) /test/1/
------
SQL  START GROUP_REPLICATION USER='repl_user', DEFAULT_AUTH='caching_sha2_password';
行为 ADMIN Replication(1:0~1:78) /test/1/
------
SQL  START GROUP_REPLICATION PASSWORD='secret', DEFAULT_AUTH='caching_sha2_password';
行为 ADMIN Replication(1:0~1:79) /test/1/
------
SQL  START GROUP_REPLICATION USER='repl_user', PASSWORD='secret', DEFAULT_AUTH='caching_sha2_password';
行为 ADMIN Replication(1:0~1:97) /test/1/
------
SQL  START GROUP_REPLICATION DEFAULT_AUTH='caching_sha2_password', USER='repl_user', PASSWORD='secret';
行为 ADMIN Replication(1:0~1:97) /test/1/
------
SQL  START GROUP_REPLICATION USER='first_user', USER='second_user';
行为 ADMIN Replication(1:0~1:61) /test/1/
------
SQL  START GROUP_REPLICATION USER='';
行为 ADMIN Replication(1:0~1:31) /test/1/
------
SQL  START GROUP_REPLICATION USER='repl_user', PASSWORD='123456789012345678901234567890123';
行为 ADMIN Replication(1:0~1:86) /test/1/
------
SQL  CHANGE REPLICATION FILTER REPLICATE_DO_DB=(audit_channel_db) FOR CHANNEL 'audit_filter';
行为 ALTER Replication(1:0~1:87) /test/1/
------
SQL  RESET REPLICA FOR CHANNEL 'audit_replica';
行为 ALTER Replication(1:0~1:41) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HOST='localhost', SOURCE_PORT=10, PRIVILEGE_CHECKS_USER='replication_applier'@'localhost' FOR CHANNEL '';
行为 ALTER Replication(1:0~1:140) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HOST='localhost', SOURCE_PORT=11, PRIVILEGE_CHECKS_USER=NULL FOR CHANNEL 'ch_priv_null';
行为 ALTER Replication(1:0~1:123) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HOST='localhost', SOURCE_PORT=12, PRIVILEGE_CHECKS_USER='replication_applier'@'localhost', REQUIRE_ROW_FORMAT=1, REQUIRE_TABLE_PRIMARY_KEY_CHECK=ON FOR CHANNEL 'ch_require';
行为 ALTER Replication(1:0~1:208) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO REQUIRE_TABLE_PRIMARY_KEY_CHECK=GENERATE FOR CHANNEL 'ch_require';
行为 ALTER Replication(1:0~1:94) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS=LOCAL FOR CHANNEL 'ch_gtid';
行为 ALTER Replication(1:0~1:95) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' FOR CHANNEL 'ch_gtid_uuid';
行为 ALTER Replication(1:0~1:133) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HOST='localhost', SOURCE_PORT=13, SOURCE_AUTO_POSITION=1, REQUIRE_ROW_FORMAT=1, GTID_ONLY=1 FOR CHANNEL 'ch_gtid_only';
行为 ALTER Replication(1:0~1:154) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_LOG_FILE='binlog.000001', SOURCE_LOG_POS=4 FOR CHANNEL 'ch_pos';
行为 ALTER Replication(1:0~1:99) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HEARTBEAT_PERIOD=20.5, SOURCE_SSL=1, SOURCE_SSL_VERIFY_SERVER_CERT=0 FOR CHANNEL 'ch_ssl';
行为 ALTER Replication(1:0~1:125) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_BIND='', SOURCE_HOST='localhost', SOURCE_USER='root', SOURCE_PASSWORD='123456', SOURCE_PORT=3306, SOURCE_CONNECT_RETRY=5, SOURCE_RETRY_COUNT=2, SOURCE_DELAY=0 FOR CHANNEL 'ch_ext_conn';
行为 ALTER Replication(1:0~1:220) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_SSL=1, SOURCE_SSL_CA='', SOURCE_SSL_CAPATH='', SOURCE_SSL_CERT='', SOURCE_SSL_KEY='', SOURCE_SSL_CIPHER='', SOURCE_SSL_CRL='', SOURCE_SSL_CRLPATH='', SOURCE_TLS_VERSION='TLSv1.2,TLSv1.3', SOURCE_TLS_CIPHERSUITES='TLS_AES_256_GCM_SHA384' FOR CHANNEL 'ch_ext_tls';
行为 ALTER Replication(1:0~1:297) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO GET_SOURCE_PUBLIC_KEY=1, SOURCE_PUBLIC_KEY_PATH='', SOURCE_COMPRESSION_ALGORITHMS='zstd', SOURCE_ZSTD_COMPRESSION_LEVEL=3 FOR CHANNEL 'ch_ext_crypto';
行为 ALTER Replication(1:0~1:178) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_CONNECT_RETRY=1, SOURCE_RETRY_COUNT=1, IGNORE_SERVER_IDS=(), SOURCE_CONNECTION_AUTO_FAILOVER=0 FOR CHANNEL 'ch_ext_failover';
行为 ALTER Replication(1:0~1:160) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HOST='localhost', SOURCE_PORT=3306, NETWORK_NAMESPACE='' FOR CHANNEL 'ch_ext_ns';
行为 ALTER Replication(1:0~1:116) /test/1/
------
SQL  START REPLICA IO_THREAD FOR CHANNEL 'ch_start_stop';
行为 ADMIN Replication(1:0~1:51) /test/1/
------
SQL  START REPLICA IO_THREAD UNTIL SOURCE_LOG_FILE='binlog.000001', SOURCE_LOG_POS=4 USER='root' PASSWORD='123456' DEFAULT_AUTH='mysql_native_password' PLUGIN_DIR='/usr/lib/mysql/plugin' FOR CHANNEL 'ch_ext_start';
行为 ADMIN Replication(1:0~1:208) /test/1/
------
SQL  START REPLICA SQL_THREAD UNTIL SQL_AFTER_GTIDS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:1-2' FOR CHANNEL 'ch_ext_start';
行为 ADMIN Replication(1:0~1:116) /test/1/
------
SQL  START REPLICA UNTIL SQL_AFTER_MTS_GAPS FOR CHANNEL 'ch_ext_start';
行为 ADMIN Replication(1:0~1:65) /test/1/
------
SQL  STOP REPLICA IO_THREAD FOR CHANNEL 'ch_start_stop';
行为 ADMIN Replication(1:0~1:50) /test/1/
------
SQL  STOP REPLICA FOR CHANNEL 'ch_ext_start';
行为 ADMIN Replication(1:0~1:39) /test/1/
------
SQL  RESET REPLICA ALL FOR CHANNEL '';
行为 ALTER Replication(1:0~1:32) /test/1/
------
SQL  RESET REPLICA ALL;
行为 ALTER Replication(1:0~1:17) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS=OFF FOR CHANNEL 'audit_gtid_off';
行为 ALTER Replication(1:0~1:100) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_CONNECTION_AUTO_FAILOVER=1 FOR CHANNEL 'audit_failover';
行为 ALTER Replication(1:0~1:91) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO REQUIRE_ROW_FORMAT=0, GTID_ONLY=0 FOR CHANNEL 'audit_bool_zero';
行为 ALTER Replication(1:0~1:92) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO MASTER_HOST='localhost', MASTER_BIND='', MASTER_USER='root', MASTER_PASSWORD='123456', MASTER_PORT=3306, MASTER_CONNECT_RETRY=5, MASTER_RETRY_COUNT=2, MASTER_DELAY=0 FOR CHANNEL 'split_master_alias_conn';
行为 ALTER Replication(1:0~1:232) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO MASTER_SSL=1, MASTER_SSL_CA='', MASTER_SSL_CAPATH='', MASTER_SSL_CERT='', MASTER_SSL_KEY='', MASTER_SSL_CIPHER='', MASTER_SSL_CRL='', MASTER_SSL_CRLPATH='', MASTER_TLS_VERSION='TLSv1.2,TLSv1.3', MASTER_TLS_CIPHERSUITES='TLS_AES_256_GCM_SHA384', MASTER_SSL_VERIFY_SERVER_CERT=0 FOR CHANNEL 'split_master_alias_tls';
行为 ALTER Replication(1:0~1:342) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO GET_MASTER_PUBLIC_KEY=1, MASTER_PUBLIC_KEY_PATH='', MASTER_COMPRESSION_ALGORITHMS='zstd', MASTER_ZSTD_COMPRESSION_LEVEL=3, MASTER_HEARTBEAT_PERIOD=20.5 FOR CHANNEL 'split_master_alias_crypto';
行为 ALTER Replication(1:0~1:220) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO MASTER_LOG_FILE='binlog.000001', MASTER_LOG_POS=4 FOR CHANNEL 'split_master_alias_pos';
行为 ALTER Replication(1:0~1:115) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO MASTER_AUTO_POSITION=1 FOR CHANNEL 'split_master_alias_auto';
行为 ALTER Replication(1:0~1:89) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO REQUIRE_TABLE_PRIMARY_KEY_CHECK=OFF FOR CHANNEL 'audit_pk_off';
行为 ALTER Replication(1:0~1:91) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO REQUIRE_TABLE_PRIMARY_KEY_CHECK=STREAM FOR CHANNEL 'audit_pk_stream';
行为 ALTER Replication(1:0~1:97) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO RELAY_LOG_FILE='split-relay-bin.000001', RELAY_LOG_POS=4 FOR CHANNEL 'split_relay_accept';
行为 ALTER Replication(1:0~1:118) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO IGNORE_SERVER_IDS=(2,3) FOR CHANNEL 'audit_ids';
行为 ALTER Replication(1:0~1:76) /test/1/
------
SQL  START REPLICA;
行为 ADMIN Replication(1:0~1:13) /test/1/
------
SQL  START REPLICA SQL_THREAD UNTIL SQL_BEFORE_GTIDS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:1';
行为 ADMIN Replication(1:0~1:88) /test/1/
------
SQL  START REPLICA SQL_THREAD UNTIL RELAY_LOG_FILE='relay-bin.000001', RELAY_LOG_POS=4;
行为 ADMIN Replication(1:0~1:81) /test/1/
------
SQL  START REPLICA IO_THREAD, SQL_THREAD;
行为 ADMIN Replication(1:0~1:35) /test/1/
------
SQL  STOP REPLICA IO_THREAD, SQL_THREAD;
行为 ADMIN Replication(1:0~1:34) /test/1/
------
SQL  START REPLICA UNTIL SOURCE_LOG_FILE = 'binlog.000001', SOURCE_LOG_POS = 4 FOR CHANNEL 'codex_gap7_ch';
行为 ADMIN Replication(1:0~1:101) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_TLS_CIPHERSUITES=NULL FOR CHANNEL 'codex_gap';
行为 ALTER Replication(1:0~1:81) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO PRIVILEGE_CHECKS_USER='split_gap_user' FOR CHANNEL "group_replication_applier";
行为 ALTER Replication(1:0~1:107) /test/1/
------
SQL  START REPLICA IO_THREAD UNTIL SQL_BEFORE_GTIDS='aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:4-5';
行为 ADMIN Replication(1:0~1:89) /test/1/
------
SQL  CHANGE REPLICATION SOURCE TO SOURCE_HOST='127.0.0.1', SOURCE_PORT=3306, SOURCE_USER='repl', SOURCE_PASSWORD='pw', SOURCE_AUTO_POSITION=0, GET_SOURCE_PUBLIC_KEY=1 FOR CHANNEL 'chan84';
行为 ALTER Replication(1:0~1:182) /test/1/
------
SQL  START REPLICA SQL_THREAD UNTIL SQL_AFTER_GTIDS = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:1' FOR CHANNEL 'chan84';
行为 ADMIN Replication(1:0~1:110) /test/1/
------
SQL  STOP REPLICA SQL_THREAD FOR CHANNEL 'chan84';
行为 ADMIN Replication(1:0~1:44) /test/1/
------
SQL  RESET REPLICA ALL FOR CHANNEL 'chan84';
行为 ALTER Replication(1:0~1:38) /test/1/
------
SQL  RESET REPLICA, BINARY LOGS AND GTIDS TO 7;
行为 ALTER Replication(1:0~1:41) /test/1/
------
SQL  RESET REPLICA ALL FOR CHANNEL 'split', BINARY LOGS AND GTIDS TO 7;
行为 ALTER Replication(1:0~1:65) /test/1/
------
SQL  RESET REPLICA;
行为 ALTER Replication(1:0~1:13) /test/1/

## SESSION_SETTING_WRITE

SQL  SET SESSION block_encryption_mode = 'aes-128-cfb1';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-192-cfb1';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-256-cfb1';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-128-cfb8';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-192-cfb8';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-256-cfb8';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-128-cfb128';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-192-cfb128';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-256-cfb128';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-128-ofb';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-192-ofb';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-256-ofb';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-128-ecb';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-192-ecb';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-256-ecb';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-128-cbc';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-192-cbc';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION block_encryption_mode = 'aes-256-cbc';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/block_encryption_mode/
------
SQL  SET SESSION old_passwords=0;
行为 CONFIGURE ConfigKey(1:4~1:25) /test/1/old_passwords/
------
SQL  SET old_passwords=2;
行为 CONFIGURE ConfigKey(1:4~1:17) /test/1/old_passwords/
------
SQL  SET optimizer_switch='semijoin=on,materialization=on,firstmatch=on,loosescan=on,index_condition_pushdown=on,mrr=on,mrr_cost_based=off';
行为 CONFIGURE ConfigKey(1:4~1:20) /test/1/optimizer_switch/
------
SQL  SET optimizer_switch=DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:20) /test/1/optimizer_switch/
------
SQL  SET collation_connection='utf8_general_ci';
行为 CONFIGURE ConfigKey(1:4~1:24) /test/1/collation_connection/
------
SQL  SET collation_connection=@old_collation_connection;
行为 CONFIGURE ConfigKey(1:4~1:24) /test/1/collation_connection/
行为 READ ConfigKey(1:25~1:50) /test/1/old_collation_connection/
------
SQL  SET TIMESTAMP=UNIX_TIMESTAMP('2025-01-01 00:00:00');
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/TIMESTAMP/
行为 CALL Function(1:14~1:28) /test/1/catalog1/schema1/UNIX_TIMESTAMP/
------
SQL  SET TIMESTAMP=DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/TIMESTAMP/
------
SQL  SET time_zone='+03:00';
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/time_zone/
------
SQL  SET time_zone=@@global.time_zone;
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/time_zone/
行为 READ ConfigKey(1:14~1:32) /test/1/time_zone/
------
SQL  SET time_zone=UTC;
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/time_zone/
------
SQL  SET SESSION explicit_defaults_for_timestamp=OFF;
行为 CONFIGURE ConfigKey(1:4~1:43) /test/1/explicit_defaults_for_timestamp/
------
SQL  SET TIMESTAMP=1000000019;
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/TIMESTAMP/
------
SQL  SET ENABLE_CASCADE_TRIGGERS=ON;
行为 CONFIGURE ConfigKey(1:4~1:27) /test/1/ENABLE_CASCADE_TRIGGERS/
------
SQL  SET cube = 1;
行为 CONFIGURE ConfigKey(1:4~1:8) /test/1/cube/
------
SQL  SET external = 1;
行为 CONFIGURE ConfigKey(1:4~1:12) /test/1/external/
------
SQL  SET library = 1;
行为 CONFIGURE ConfigKey(1:4~1:11) /test/1/library/
------
SQL  SET sets = 1;
行为 CONFIGURE ConfigKey(1:4~1:8) /test/1/sets/
------
SQL  SET @@session.parser_max_mem_size=DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET @@session.parser_max_mem_size=500*1000*100;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET SESSION parser_max_mem_size=1000*1000*100;
行为 CONFIGURE ConfigKey(1:4~1:31) /test/1/parser_max_mem_size/
------
SQL  SET @@session.parser_max_mem_size='NOT_CHAR_TYPE';
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET @@session.parser_max_mem_size=-10;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET @@session.parser_max_mem_size=0.5;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET @@session.parser_max_mem_size=TRUE;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET @@session.parser_max_mem_size=FALSE;
行为 CONFIGURE ConfigKey(1:4~1:33) /test/1/parser_max_mem_size/
------
SQL  SET default_storage_engine=a.myisam;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = .a.MyISAM;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = a.b.MyISAM;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = `a`.MyISAM;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = `a`.`MyISAM`;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = "a.MYISAM";
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = 'a.MYISAM';
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET default_storage_engine = `a.MYISAM`;
行为 CONFIGURE ConfigKey(1:4~1:26) /test/1/default_storage_engine/
------
SQL  SET SESSION sql_mode='HIGH_NOT_PRECEDENCE';
行为 CONFIGURE ConfigKey(1:4~1:20) /test/1/sql_mode/
------
SQL  SET SESSION sql_mode='PIPES_AS_CONCAT';
行为 CONFIGURE ConfigKey(1:4~1:20) /test/1/sql_mode/
------
SQL  SET sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
行为 CONFIGURE ConfigKey(1:4~1:12) /test/1/sql_mode/
行为 CALL Function(1:21~1:28) /test/1/catalog1/schema1/REPLACE/
行为 READ ConfigKey(1:29~1:39) /test/1/sql_mode/
------
SQL  SET @@LOCAL.generated_random_password_length = '4';
行为 CONFIGURE ConfigKey(1:4~1:44) /test/1/generated_random_password_length/
------
SQL  SET NAMES utf8mb4;
行为 CONFIGURE ConfigKey(1:0~1:17) /test/1/
------
SQL  SET NAMES 'latin1' COLLATE 'latin1_swedish_ci';
行为 CONFIGURE ConfigKey(1:0~1:46) /test/1/
------
SQL  SET NAMES DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:17) /test/1/
------
SQL  SET CHARACTER SET utf8mb4;
行为 CONFIGURE ConfigKey(1:0~1:25) /test/1/
------
SQL  SET CHARSET latin1;
行为 CONFIGURE ConfigKey(1:0~1:18) /test/1/
------
SQL  SET CHARACTER SET DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:25) /test/1/
------
SQL  SET CHARACTER SET cp1250_latin2;
行为 CONFIGURE ConfigKey(1:0~1:31) /test/1/
------
SQL  SET LOCAL generated_random_password_length = '10';
行为 CONFIGURE ConfigKey(1:4~1:42) /test/1/generated_random_password_length/
------
SQL  SET @@SESSION.sql_auto_is_null = 1;
行为 CONFIGURE ConfigKey(1:4~1:30) /test/1/sql_auto_is_null/
------
SQL  SET @@SESSION.sql_auto_is_null = DEFAULT;
行为 CONFIGURE ConfigKey(1:4~1:30) /test/1/sql_auto_is_null/
------
SQL  SET GLOBAL = DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:20) /test/1/
------
SQL  SET LOCAL = DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:19) /test/1/
------
SQL  SET PERSIST_ONLY = DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:26) /test/1/
------
SQL  SET SESSION = DEFAULT;
行为 CONFIGURE ConfigKey(1:0~1:21) /test/1/
------
SQL  SET INSERT_ID=128;
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/INSERT_ID/
------
SQL  SET LAST_INSERT_ID=1;
行为 CONFIGURE ConfigKey(1:4~1:18) /test/1/LAST_INSERT_ID/
------
SQL  SET SESSION debug='d,force_fake_uuid';
行为 CONFIGURE ConfigKey(1:4~1:17) /test/1/debug/
------
SQL  SET AUTOCOMMIT = 0;
行为 CONFIGURE ConfigKey(1:4~1:14) /test/1/AUTOCOMMIT/
------
SQL  SET AUTOCOMMIT = 1;
行为 CONFIGURE ConfigKey(1:4~1:14) /test/1/AUTOCOMMIT/
------
SQL  SET TIMESTAMP=-147490000;
行为 CONFIGURE ConfigKey(1:4~1:13) /test/1/TIMESTAMP/
------
SQL  set session sql_mode = 'STRICT_TRANS_TABLES';
行为 CONFIGURE ConfigKey(1:4~1:20) /test/1/sql_mode/
------
SQL  SET sql_mode='TIME_TRUNCATE_FRACTIONAL';
行为 CONFIGURE ConfigKey(1:4~1:12) /test/1/sql_mode/
------
SQL  set @@max_binlog_cache_size = 1024;
行为 CONFIGURE ConfigKey(1:4~1:27) /test/1/max_binlog_cache_size/

## CREATE_PROG_OBJ

SQL  CREATE FUNCTION split_missing_udf_56 RETURNS STRING SONAME 'split_missing_udf.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_udf_56/
行为 CREATE File(1:59~1:81) /test/1/split_missing_udf.so/
------
SQL  CREATE AGGREGATE FUNCTION split_missing_agg_56 RETURNS REAL SONAME 'split_missing_agg.so';
行为 CREATE Function(1:26~1:46) /test/1/catalog1/schema1/split_missing_agg_56/
行为 CREATE File(1:67~1:89) /test/1/split_missing_agg.so/
------
SQL  CREATE FUNCTION split_missing_int_56 RETURNS INTEGER SONAME 'split_missing_int.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_int_56/
行为 CREATE File(1:60~1:82) /test/1/split_missing_int.so/
------
SQL  CREATE FUNCTION split_missing_decimal_56 RETURNS DECIMAL SONAME 'split_missing_decimal.so';
行为 CREATE Function(1:16~1:40) /test/1/catalog1/schema1/split_missing_decimal_56/
行为 CREATE File(1:64~1:90) /test/1/split_missing_decimal.so/
------
SQL  CREATE PROCEDURE p_types(IN p ENUM('a','b'), OUT q SET('x','y'), INOUT r DECIMAL(10,2)) BEGIN SET q='x'; SET r=COALESCE(r,0); END;
行为 CREATE Procedure(1:17~1:24) /test/1/catalog1/schema1/p_types/
行为 CALL Function(1:111~1:119) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE FUNCTION codex_year4(a YEAR(4)) RETURNS YEAR(4) DETERMINISTIC RETURN a;
行为 CREATE Function(1:16~1:27) /test/1/catalog1/schema1/codex_year4/
------
SQL  CREATE FUNCTION codex_year_plain(a YEAR) RETURNS YEAR DETERMINISTIC RETURN a;
行为 CREATE Function(1:16~1:32) /test/1/catalog1/schema1/codex_year_plain/
------
SQL  CREATE PROCEDURE codex_year_proc(a YEAR(4),b YEAR) BEGIN END;
行为 CREATE Procedure(1:17~1:32) /test/1/catalog1/schema1/codex_year_proc/
------
SQL  CREATE FUNCTION f_float255(a FLOAT(255,0)) RETURNS DOUBLE(12,3) RETURN 1;
行为 CREATE Function(1:16~1:26) /test/1/catalog1/schema1/f_float255/
------
SQL  CREATE PROCEDURE p_float255(IN a FLOAT(255,0),OUT b DOUBLE(12,3)) BEGIN SELECT 1 INTO b; END;
行为 CREATE Procedure(1:17~1:27) /test/1/catalog1/schema1/p_float255/
------
SQL  CREATE FUNCTION f_unsigned(a FLOAT UNSIGNED) RETURNS DOUBLE UNSIGNED RETURN 1;
行为 CREATE Function(1:16~1:26) /test/1/catalog1/schema1/f_unsigned/
------
SQL  CREATE PROCEDURE p_unsigned(IN a FLOAT UNSIGNED,OUT b DOUBLE UNSIGNED) BEGIN SELECT 1 INTO b; END;
行为 CREATE Procedure(1:17~1:27) /test/1/catalog1/schema1/p_unsigned/
------
SQL  CREATE FUNCTION bit_f(a BIT(64)) RETURNS BIT(8) DETERMINISTIC RETURN a;
行为 CREATE Function(1:16~1:21) /test/1/catalog1/schema1/bit_f/
------
SQL  CREATE PROCEDURE bit_p(IN a BIT(3),OUT b BIT(12)) SELECT a INTO b;
行为 CREATE Procedure(1:17~1:22) /test/1/catalog1/schema1/bit_p/
------
SQL  CREATE PROCEDURE bit_local()\nBEGIN\n  DECLARE v BIT(8) DEFAULT b'1';\n  SET v=v | b'10';\n  SELECT HEX(v);\nEND;
行为 CREATE Procedure(1:17~1:26) /test/1/catalog1/schema1/bit_local/
行为 CALL Function(5:9~5:12) /test/1/catalog1/schema1/HEX/
------
SQL  CREATE FUNCTION time_identity(p_value TIME(6)) RETURNS TIME(6) DETERMINISTIC RETURN p_value;
行为 CREATE Function(1:16~1:29) /test/1/catalog1/schema1/time_identity/
------
SQL  CREATE PROCEDURE time_parameters(IN p_input TIME(6),OUT p_output TIME(6),INOUT p_accumulator TIME(3))\nBEGIN\n  DECLARE local_time TIME(6) DEFAULT TIME'00:00:00.000001';\n  SET p_output=ADDTIME(p_input,local_time);\n  SET p_accumulator=SUBTIME(p_accumulator,local_time);\nEND;
行为 CREATE Procedure(1:17~1:32) /test/1/catalog1/schema1/time_parameters/
行为 CALL Function(4:15~4:22) /test/1/catalog1/schema1/ADDTIME/
行为 CALL Function(5:20~5:27) /test/1/catalog1/schema1/SUBTIME/
------
SQL  CREATE FUNCTION datetime_identity(p_value DATETIME(6)) RETURNS DATETIME(6) DETERMINISTIC RETURN p_value;
行为 CREATE Function(1:16~1:33) /test/1/catalog1/schema1/datetime_identity/
------
SQL  CREATE PROCEDURE datetime_parameters(IN p_datetime DATETIME(6),OUT p_timestamp TIMESTAMP(6),INOUT p_accumulator DATETIME(3))\nBEGIN\n  DECLARE local_datetime DATETIME(6) DEFAULT '2000-01-01 00:00:00.000001';\n  DECLARE local_timestamp TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6);\n  SET p_timestamp=local_timestamp;\n  SET p_accumulator=TIMESTAMPADD(MICROSECOND,1,p_datetime);\n  SELECT local_datetime;\nEND;
行为 CREATE Procedure(1:17~1:36) /test/1/catalog1/schema1/datetime_parameters/
行为 CALL Function(4:47~4:64) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
行为 CALL Function(6:20~6:32) /test/1/catalog1/schema1/TIMESTAMPADD/
------
SQL  CREATE PROCEDURE audit_label_cube() cube: BEGIN LEAVE cube; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/audit_label_cube/
------
SQL  CREATE PROCEDURE audit_label_external() external: BEGIN LEAVE external; END;
行为 CREATE Procedure(1:17~1:37) /test/1/catalog1/schema1/audit_label_external/
------
SQL  CREATE PROCEDURE audit_label_library() library: BEGIN LEAVE library; END;
行为 CREATE Procedure(1:17~1:36) /test/1/catalog1/schema1/audit_label_library/
------
SQL  CREATE PROCEDURE audit_label_sets() sets: BEGIN LEAVE sets; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/audit_label_sets/
------
SQL  CREATE PROCEDURE split_keywords.p_label_binlog()\nbinlog: BEGIN\n  LEAVE binlog;\nEND binlog;
行为 CREATE Procedure(1:17~1:46) /test/1/catalog1/split_keywords/p_label_binlog/
------
SQL  CREATE PROCEDURE p1()\nBEGIN\nparse_gcol_expr: LOOP\n  SELECT 1;\n  LEAVE parse_gcol_expr;\nEND LOOP parse_gcol_expr;\nEND;
行为 CREATE Procedure(1:17~1:19) /test/1/catalog1/schema1/p1/
------
SQL  CREATE FUNCTION f_labels() RETURNS INT\nBEGIN\n  ACCOUNT: LOOP LEAVE ACCOUNT; END LOOP ACCOUNT;\n  ALWAYS: LOOP LEAVE ALWAYS; END LOOP ALWAYS;\n  GROUP_REPLICATION: LOOP LEAVE GROUP_REPLICATION; END LOOP GROUP_REPLICATION;\n  INVISIBLE: LOOP LEAVE INVISIBLE; END LOOP INVISIBLE;\n  ROLE: LOOP LEAVE ROLE; END LOOP ROLE;\n  SECONDARY: LOOP LEAVE SECONDARY; END LOOP SECONDARY;\n  SECONDARY_ENGINE: LOOP LEAVE SECONDARY_ENGINE; END LOOP SECONDARY_ENGINE;\n  SECONDARY_LOAD: LOOP LEAVE SECONDARY_LOAD; END LOOP SECONDARY_LOAD;\n  SECONDARY_UNLOAD: LOOP LEAVE SECONDARY_UNLOAD; END LOOP SECONDARY_UNLOAD;\n  VISIBLE: LOOP LEAVE VISIBLE; END LOOP VISIBLE;\n  RETURN 1;\nEND;
行为 CREATE Function(1:16~1:24) /test/1/catalog1/schema1/f_labels/
------
SQL  CREATE PROCEDURE split_keywords.p_restricted_labels()\nBEGIN\n  `ascii`: BEGIN END `ascii`;\n  `begin`: BEGIN END `begin`;\n  `binlog`: BEGIN END `binlog`;\n  `byte`: BEGIN END `byte`;\n  `cache`: BEGIN END `cache`;\n  `charset`: BEGIN END `charset`;\n  `checksum`: BEGIN END `checksum`;\n  `clone`: BEGIN END `clone`;\n  `comment`: BEGIN END `comment`;\n  `commit`: BEGIN END `commit`;\n  `contains`: BEGIN END `contains`;\n  `deallocate`: BEGIN END `deallocate`;\n  `do`: BEGIN END `do`;\n  `end`: BEGIN END `end`;\n  `execute`: BEGIN END `execute`;\n  `flush`: BEGIN END `flush`;\n  `follows`: BEGIN END `follows`;\n  `handler`: BEGIN END `handler`;\n  `help`: BEGIN END `help`;\n  `import`: BEGIN END `import`;\n  `install`: BEGIN END `install`;\n  `language`: BEGIN END `language`;\n  `no`: BEGIN END `no`;\n  `precedes`: BEGIN END `precedes`;\n  `prepare`: BEGIN END `prepare`;\n  `repair`: BEGIN END `repair`;\n  `reset`: BEGIN END `reset`;\n  `restart`: BEGIN END `restart`;\n  `rollback`: BEGIN END `rollback`;\n  `savepoint`: BEGIN END `savepoint`;\n  `shutdown`: BEGIN END `shutdown`;\n  `signed`: BEGIN END `signed`;\n  `slave`: BEGIN END `slave`;\n  `start`: BEGIN END `start`;\n  `stop`: BEGIN END `stop`;\n  `truncate`: BEGIN END `truncate`;\n  `unicode`: BEGIN END `unicode`;\n  `uninstall`: BEGIN END `uninstall`;\n  `xa`: BEGIN END `xa`;\nEND;
行为 CREATE Procedure(1:17~1:51) /test/1/catalog1/split_keywords/p_restricted_labels/
------
SQL  CREATE PROCEDURE p_skip() BEGIN skip: LOOP LEAVE skip; END LOOP skip; END;
行为 CREATE Procedure(1:17~1:23) /test/1/catalog1/schema1/p_skip/
------
SQL  CREATE PROCEDURE p_locked() BEGIN locked: LOOP LEAVE locked; END LOOP locked; END;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/p_locked/
------
SQL  CREATE PROCEDURE p_nowait() BEGIN nowait: LOOP LEAVE nowait; END LOOP nowait; END;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/p_nowait/
------
SQL  CREATE PROCEDURE p_of() BEGIN of: LOOP LEAVE of; END LOOP of; END;
行为 CREATE Procedure(1:17~1:21) /test/1/catalog1/schema1/p_of/
------
SQL  CREATE PROCEDURE p_keyword_contexts() BEGIN DECLARE connection INT DEFAULT 1; authors: LOOP LEAVE authors; END LOOP authors; contributors: LOOP LEAVE contributors; END LOOP contributors; general: LOOP LEAVE general; END LOOP general; ignore_server_ids: LOOP LEAVE ignore_server_ids; END LOOP ignore_server_ids; END;
行为 CREATE Procedure(1:17~1:35) /test/1/catalog1/schema1/p_keyword_contexts/
------
SQL  CREATE PROCEDURE p_master_keyword() BEGIN master_heartbeat_period: LOOP LEAVE master_heartbeat_period; END LOOP master_heartbeat_period; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/p_master_keyword/
------
SQL  CREATE PROCEDURE p_source_keyword() BEGIN source_heartbeat_period: LOOP LEAVE source_heartbeat_period; END LOOP source_heartbeat_period; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/p_source_keyword/
------
SQL  CREATE PROCEDURE analyse_select_proc()\nBEGIN\n  SELECT * FROM analyse_int PROCEDURE ANALYSE();\nEND;
行为 CREATE Procedure(1:17~1:36) /test/1/catalog1/schema1/analyse_select_proc/
行为 READ Table(3:16~3:27) /test/1/catalog1/schema1/analyse_int/
------
SQL  CREATE PROCEDURE analyse_cursor_proc()\nBEGIN\n  DECLARE c1,c2,c3,c4,c5,c6,c7,c8,c9,c10 CHAR(20);\n  DECLARE done INT DEFAULT 0;\n  DECLARE cur1 CURSOR FOR SELECT * FROM analyse_int PROCEDURE ANALYSE();\n  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1;\n  OPEN cur1;\n  read_loop: LOOP\n    FETCH cur1 INTO c1,c2,c3,c4,c5,c6,c7,c8,c9,c10;\n    IF done THEN\n      LEAVE read_loop;\n    END IF;\n  END LOOP;\n  CLOSE cur1;\nEND;
行为 CREATE Procedure(1:17~1:36) /test/1/catalog1/schema1/analyse_cursor_proc/
行为 READ Table(5:40~5:51) /test/1/catalog1/schema1/analyse_int/
------
SQL  CREATE PROCEDURE codex_func_concat.p_concat_ws(a VARCHAR(255),b INT,c INT) SET @query=CONCAT_WS(',',a,b,c);
行为 CREATE Procedure(1:17~1:46) /test/1/catalog1/codex_func_concat/p_concat_ws/
行为 READ ConfigKey(1:79~1:85) /test/1/query/
行为 CALL Function(1:86~1:95) /test/1/catalog1/schema1/CONCAT_WS/
------
SQL  CREATE PROCEDURE codex_func_concat.p_concat_double()\nBEGIN\n  DECLARE v1 DOUBLE(10,3);\n  SET v1=100;\n  SET @s=CONCAT('########################################',40,v1);\n  SELECT @s;\nEND;
行为 CREATE Procedure(1:17~1:50) /test/1/catalog1/codex_func_concat/p_concat_double/
行为 READ ConfigKey(5:6~5:8) /test/1/s/
行为 CALL Function(5:9~5:15) /test/1/catalog1/schema1/CONCAT/
------
SQL  CREATE PROCEDURE p1()\nBEGIN\n  DECLARE dbl DOUBLE;\n  DECLARE deci DECIMAL(10, 2);\n  DECLARE intv INTEGER;\n  DECLARE str VARBINARY(16);\n  SELECT DISTINCT VAR_POP(1) FROM proc_t1 GROUP BY pk INTO dbl;\n  SELECT DISTINCT STDDEV_POP(1) FROM proc_t1 GROUP BY pk INTO dbl;\n  SELECT DISTINCT STDDEV_SAMP(1) FROM proc_t1 GROUP BY pk INTO dbl;\n  SELECT DISTINCT AVG(3.14159) FROM proc_t1 GROUP BY pk INTO deci;\n  SELECT DISTINCT AVG(3.14159E0) FROM proc_t1 GROUP BY pk INTO dbl;\n  SELECT DISTINCT BIT_OR(x'0000') FROM proc_t1 GROUP BY pk INTO str;\n  SELECT DISTINCT BIT_OR(9) FROM proc_t1 GROUP BY pk INTO intv;\nEND;
行为 CREATE Procedure(1:17~1:19) /test/1/catalog1/schema1/p1/
行为 CALL Function(7:18~7:25) /test/1/catalog1/schema1/VAR_POP/
行为 READ Table(7:34~7:41) /test/1/catalog1/schema1/proc_t1/
行为 CALL Function(8:18~8:28) /test/1/catalog1/schema1/STDDEV_POP/
行为 CALL Function(9:18~9:29) /test/1/catalog1/schema1/STDDEV_SAMP/
行为 CALL Function(10:18~10:21) /test/1/catalog1/schema1/AVG/
行为 CALL Function(12:18~12:24) /test/1/catalog1/schema1/BIT_OR/
------
SQL  CREATE FUNCTION math_crc32_func(input_value CHAR(10)) RETURNS BIGINT DETERMINISTIC\nBEGIN\n  DECLARE crc_value BIGINT;\n  SELECT CRC32(input_value) INTO crc_value;\n  RETURN crc_value;\nEND;
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/math_crc32_func/
行为 CALL Function(4:9~4:14) /test/1/catalog1/schema1/CRC32/
------
SQL  CREATE FUNCTION split_r_like(input_text VARCHAR(255)) RETURNS BOOLEAN DETERMINISTIC RETURN REGEXP_LIKE(input_text, 'pattern');
行为 CREATE Function(1:16~1:28) /test/1/catalog1/schema1/split_r_like/
行为 CALL Function(1:91~1:102) /test/1/catalog1/schema1/REGEXP_LIKE/
------
SQL  CREATE FUNCTION split_r_instr(input_text VARCHAR(255)) RETURNS INT DETERMINISTIC RETURN REGEXP_INSTR(input_text, 'pattern');
行为 CREATE Function(1:16~1:29) /test/1/catalog1/schema1/split_r_instr/
行为 CALL Function(1:88~1:100) /test/1/catalog1/schema1/REGEXP_INSTR/
------
SQL  CREATE FUNCTION split_r_replace(input_text VARCHAR(255)) RETURNS VARCHAR(255) DETERMINISTIC RETURN REGEXP_REPLACE(input_text, 'pattern', 'xyz');
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/split_r_replace/
行为 CALL Function(1:99~1:113) /test/1/catalog1/schema1/REGEXP_REPLACE/
------
SQL  CREATE FUNCTION split_r_substr(input_text VARCHAR(255)) RETURNS VARCHAR(255) DETERMINISTIC RETURN REGEXP_SUBSTR(input_text, 'pattern');
行为 CREATE Function(1:16~1:30) /test/1/catalog1/schema1/split_r_substr/
行为 CALL Function(1:98~1:111) /test/1/catalog1/schema1/REGEXP_SUBSTR/
------
SQL  CREATE FUNCTION rb_fail() RETURNS INTEGER MODIFIES SQL DATA\nBEGIN\n  INSERT INTO rb_not_null SET f1=10,f2=10;\n  INSERT INTO rb_not_null SET f1=10,f2=NULL;\n  RETURN 1;\nEND;
行为 CREATE Function(1:16~1:23) /test/1/catalog1/schema1/rb_fail/
行为 INSERT Table(3:14~3:25) /test/1/catalog1/schema1/rb_not_null/
------
SQL  CREATE PROCEDURE split_limit56.proc_limit(IN p_offset INT,IN p_count INT)\nBEGIN\n  SELECT id FROM split_limit56.t ORDER BY id LIMIT p_count;\n  SELECT id FROM split_limit56.t ORDER BY id LIMIT p_offset,p_count;\n  SELECT id FROM split_limit56.t ORDER BY id LIMIT p_count OFFSET p_offset;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/split_limit56/proc_limit/
行为 READ Table(3:17~3:32) /test/1/catalog1/split_limit56/t/
------
SQL  CREATE PROCEDURE p() SELECT 'foo' AS c UNION SELECT 'bar';
行为 CREATE Procedure(1:17~1:18) /test/1/catalog1/schema1/p/
------
SQL  CREATE PROCEDURE str_proc(IN p CHAR(8), OUT q VARCHAR(16), INOUT r VARBINARY(16))\nBEGIN\n  DECLARE local_b BINARY(16);\n  SET local_b = r;\n  SET q = CAST(p AS CHAR);\n  SET r = local_b;\nEND;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/str_proc/
行为 CALL Function(5:10~5:14) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE FUNCTION str_func(p VARBINARY(16)) RETURNS BINARY(16) DETERMINISTIC RETURN p;
行为 CREATE Function(1:16~1:24) /test/1/catalog1/schema1/str_func/
------
SQL  CREATE PROCEDURE lob_proc(\n  IN p_tinyblob TINYBLOB,\n  IN p_blob BLOB,\n  OUT p_mediumtext MEDIUMTEXT,\n  INOUT p_longtext LONGTEXT\n)\nBEGIN\n  DECLARE local_mediumblob MEDIUMBLOB;\n  DECLARE local_text TEXT;\n  SET local_mediumblob=p_blob;\n  SET local_text=p_longtext;\n  SET p_mediumtext=local_text;\nEND;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/lob_proc/
------
SQL  CREATE FUNCTION lob_func(p LONGBLOB) RETURNS LONGTEXT DETERMINISTIC RETURN HEX(p);
行为 CREATE Function(1:16~1:24) /test/1/catalog1/schema1/lob_func/
行为 CALL Function(1:75~1:78) /test/1/catalog1/schema1/HEX/
------
SQL  CREATE PROCEDURE split_routine.proc_handler_conditions(INOUT p_seen INT)\nouter_block: BEGIN\n  DECLARE named_error CONDITION FOR SQLSTATE VALUE '45000';\n  DECLARE CONTINUE HANDLER FOR 1062 SET p_seen = p_seen + 1;\n  DECLARE CONTINUE HANDLER FOR SQLWARNING SET p_seen = p_seen + 10;\n  inner_block: BEGIN\n    DECLARE EXIT HANDLER FOR named_error SET p_seen = p_seen + 100;\n    INSERT INTO split_routine.t VALUES (1, 'first');\n    INSERT INTO split_routine.t VALUES (1, 'duplicate');\n    SIGNAL named_error;\n  END inner_block;\nEND outer_block;
行为 CREATE Procedure(1:17~1:54) /test/1/catalog1/split_routine/proc_handler_conditions/
行为 INSERT Table(8:16~8:31) /test/1/catalog1/split_routine/t/
------
SQL  CREATE PROCEDURE split_routine.proc_handler_sqlstate()\nBEGIN\n  DECLARE v_seen INT DEFAULT 0;\n  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET v_seen = 1;\n  SELECT id INTO @missing_id FROM split_routine.t WHERE id = -1;\nEND;
行为 CREATE Procedure(1:17~1:52) /test/1/catalog1/split_routine/proc_handler_sqlstate/
行为 READ ConfigKey(5:17~5:28) /test/1/missing_id/
行为 READ Table(5:34~5:49) /test/1/catalog1/split_routine/t/
------
SQL  CREATE PROCEDURE proc_compound_common(IN p_limit INT, OUT p_total INT)\nCOMMENT 'compound coverage'\nLANGUAGE SQL\nMODIFIES SQL DATA\nSQL SECURITY INVOKER\nBEGIN\n  DECLARE v_id INT DEFAULT 0;\n  DECLARE v_done BOOL DEFAULT FALSE;\n  DECLARE cur_ids CURSOR FOR SELECT id FROM proc_src ORDER BY id;\n  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = TRUE;\n  SET p_total = 0;\n  OPEN cur_ids;\n  read_loop: LOOP\n    FETCH NEXT FROM cur_ids INTO v_id;\n    IF v_done THEN\n      LEAVE read_loop;\n    END IF;\n    SET p_total = p_total + 1;\n    CASE\n      WHEN p_total < p_limit THEN SET v_id = v_id + 0;\n      ELSE SET v_id = v_id;\n    END CASE;\n    IF p_total < p_limit THEN\n      ITERATE read_loop;\n    END IF;\n  END LOOP read_loop;\n  CLOSE cur_ids;\n  WHILE p_total < p_limit DO\n    SET p_total = p_total + 1;\n  END WHILE;\n  REPEAT\n    SET p_total = p_total - 1;\n  UNTIL p_total <= p_limit\n  END REPEAT;\nEND;
行为 CREATE Procedure(1:17~1:37) /test/1/catalog1/schema1/proc_compound_common/
行为 READ Table(9:44~9:52) /test/1/catalog1/schema1/proc_src/
------
SQL  CREATE FUNCTION fn_compound_common(p_value INT)\nRETURNS INT\nCOMMENT 'function coverage'\nLANGUAGE SQL\nDETERMINISTIC\nNO SQL\nSQL SECURITY INVOKER\nBEGIN\n  DECLARE v_result INT DEFAULT 0;\n  IF p_value IS NULL THEN\n    SET v_result = 0;\n  ELSE\n    SET v_result = p_value + 1;\n  END IF;\n  RETURN v_result;\nEND;
行为 CREATE Function(1:16~1:34) /test/1/catalog1/schema1/fn_compound_common/
------
SQL  CREATE PROCEDURE p_load() SELECT ISNULL(LOAD_FILE('/tmp/codex_func_str_missing.txt')) AS 'is null';
行为 CREATE Procedure(1:17~1:23) /test/1/catalog1/schema1/p_load/
行为 CALL Function(1:33~1:39) /test/1/catalog1/schema1/ISNULL/
行为 CALL Function(1:40~1:49) /test/1/catalog1/schema1/LOAD_FILE/
------
SQL  CREATE PROCEDURE load_locale_format_table()\nBEGIN\n  DECLARE locale_list VARCHAR(1000) DEFAULT '\n  es_AR,es_BO,es_CL,es_CO,es_CR,es_DO,es_EC,es_ES,es_GT,es_HN,\n  es_MX,es_NI,es_PA,es_PE,es_PR,es_PY,es_SV,es_US,es_UY,es_VE';\n  SET @fmt_stmt = 'INSERT INTO locale_format VALUES\n                   (?, FORMAT(12131254123412541,2,?));';\n  PREPARE stmt FROM @fmt_stmt;\n  WHILE locale_list != '' DO\n    SET @locale =\n          TRIM(REPLACE((SUBSTRING_INDEX(locale_list, ',', 1)), '\n',''));\n    EXECUTE stmt USING @locale, @locale;\n    IF LOCATE(',', locale_list) > 0 THEN\n      SET locale_list =\n      SUBSTRING(locale_list, LOCATE(',', locale_list) + 1);\n    ELSE\n      SET locale_list = '';\n    END IF;\n  END WHILE;\n  DEALLOCATE PREPARE stmt;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/schema1/load_locale_format_table/
行为 READ ConfigKey(6:6~6:15) /test/1/fmt_stmt/
行为 ADMIN PrepareStatement(8:10~8:14) /test/1/stmt/
行为 READ ConfigKey(10:8~10:15) /test/1/e/
行为 CALL Function(11:10~11:14) /test/1/catalog1/schema1/TRIM/
行为 CALL Function(11:15~11:22) /test/1/catalog1/schema1/REPLACE/
行为 CALL Function(11:24~11:39) /test/1/catalog1/schema1/SUBSTRING_INDEX/
行为 READ ConfigKey(12:23~12:30) /test/1/locale/
行为 CALL Function(13:7~13:13) /test/1/catalog1/schema1/LOCATE/
行为 CALL Function(15:6~15:15) /test/1/catalog1/schema1/SUBSTRING/
------
SQL  CREATE PROCEDURE proc(ofs INT,count INT) BEGIN DECLARE i INT DEFAULT ofs; WHILE i<count DO IF LOWER(CHAR(i USING utf8))<>LOWER(CHAR(i USING utf8mb4)) THEN SELECT i AS 'found funny character'; END IF; SET i=i+1; END WHILE; END;
行为 CREATE Procedure(1:17~1:21) /test/1/catalog1/schema1/proc/
行为 CALL Function(1:94~1:99) /test/1/catalog1/schema1/LOWER/
行为 CALL Function(1:100~1:104) /test/1/catalog1/schema1/CHAR/
------
SQL  CREATE FUNCTION f_charset(p CHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin) RETURNS CHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DETERMINISTIC NO SQL RETURN p;
行为 CREATE Function(1:16~1:25) /test/1/catalog1/schema1/f_charset/
------
SQL  CREATE PROCEDURE fd_proc_insert() INSERT INTO split_function_defaults_objects.fd_proc_target(a) VALUES(1);
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/fd_proc_insert/
行为 INSERT Table(1:46~1:92) /test/1/catalog1/split_function_defaults_objects/fd_proc_target/
------
SQL  CREATE PROCEDURE fd_proc_update() UPDATE fd_proc_target SET a=2 WHERE a=1;
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/fd_proc_update/
行为 UPDATE Table(1:41~1:55) /test/1/catalog1/schema1/fd_proc_target/
------
SQL  CREATE PROCEDURE p_diagnostics_full()\nBEGIN\n  DECLARE n INT DEFAULT 1;\n  DECLARE t VARCHAR(128);\n  DECLARE s CHAR(5);\n  DECLARE e INT;\n  GET CURRENT DIAGNOSTICS CONDITION n\n    t=CLASS_ORIGIN, t=SUBCLASS_ORIGIN, s=RETURNED_SQLSTATE,\n    t=MESSAGE_TEXT, e=MYSQL_ERRNO, t=CONSTRAINT_CATALOG,\n    t=CONSTRAINT_SCHEMA, t=CONSTRAINT_NAME, t=CATALOG_NAME,\n    t=SCHEMA_NAME, t=TABLE_NAME, t=COLUMN_NAME, t=CURSOR_NAME;\nEND;
行为 CREATE Procedure(1:17~1:35) /test/1/catalog1/schema1/p_diagnostics_full/
------
SQL  CREATE PROCEDURE p_elseif(IN p INT, OUT o INT)\nBEGIN\n  IF p < 0 THEN SET o = -1;\n  ELSEIF p = 0 THEN SET o = 0;\n  ELSE SET o = 1;\n  END IF;\nEND;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/p_elseif/
------
SQL  CREATE PROCEDURE p_handler_list()\nBEGIN\n  DECLARE EXIT HANDLER FOR SQLEXCEPTION, NOT FOUND SET @handled = 1;\n  SET @handled = 0;\nEND;
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/p_handler_list/
行为 READ ConfigKey(3:55~3:63) /test/1/handled/
------
SQL  CREATE PROCEDURE p_resignal_named()\nBEGIN\n  DECLARE c CONDITION FOR SQLSTATE '45000';\n  DECLARE EXIT HANDLER FOR SQLEXCEPTION\n    RESIGNAL c SET MESSAGE_TEXT='mapped named';\n  SIGNAL SQLSTATE '45000';\nEND;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/p_resignal_named/
------
SQL  CREATE PROCEDURE p_resignal_set()\nBEGIN\n  DECLARE EXIT HANDLER FOR SQLEXCEPTION\n    RESIGNAL SET CLASS_ORIGIN='audit', MESSAGE_TEXT='mapped';\n  SIGNAL SQLSTATE '45000';\nEND;
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/p_resignal_set/
------
SQL  CREATE PROCEDURE p_signal_full()\nBEGIN\n  DECLARE c CONDITION FOR SQLSTATE '45000';\n  SIGNAL c SET CLASS_ORIGIN='audit', SUBCLASS_ORIGIN='audit',\n    MESSAGE_TEXT='full signal', MYSQL_ERRNO=45001,\n    CONSTRAINT_CATALOG='def', CONSTRAINT_SCHEMA='audit_mysql_flow',\n    CONSTRAINT_NAME='c1', CATALOG_NAME='def',\n    SCHEMA_NAME='audit_mysql_flow', TABLE_NAME='t1',\n    COLUMN_NAME='c1', CURSOR_NAME='cur1';\nEND;
行为 CREATE Procedure(1:17~1:30) /test/1/catalog1/schema1/p_signal_full/
------
SQL  CREATE PROCEDURE p_simple_case(IN p INT, OUT o VARCHAR(8))\nBEGIN\n  CASE p\n    WHEN 0 THEN SET o = 'zero';\n    WHEN 1 THEN SET o = 'one';\n    ELSE SET o = 'other';\n  END CASE;\nEND;
行为 CREATE Procedure(1:17~1:30) /test/1/catalog1/schema1/p_simple_case/
------
SQL  CREATE DEFINER = CURRENT_USER() PROCEDURE split_routine.proc_options(p_id INT, INOUT p_total BIGINT)\nCOMMENT 'procedure options'\nLANGUAGE SQL\nDETERMINISTIC\nREADS SQL DATA\nSQL SECURITY DEFINER\nBEGIN\n  SET p_total = p_total + p_id;\nEND;
行为 CREATE Procedure(1:42~1:68) /test/1/catalog1/split_routine/proc_options/
------
SQL  CREATE DEFINER = 'root'@'localhost' FUNCTION split_routine.fn_options(p_value INT)\nRETURNS DECIMAL(10,2)\nCOMMENT 'function options'\nLANGUAGE SQL\nNOT DETERMINISTIC\nCONTAINS SQL\nSQL SECURITY DEFINER\nRETURN p_value + 0.50;
行为 CREATE Function(1:45~1:69) /test/1/catalog1/split_routine/fn_options/
------
SQL  CREATE PROCEDURE p01() BEGIN DECLARE done BOOL DEFAULT FALSE; DECLARE a CHAR(16); DECLARE b,c INT; DECLARE c1 CURSOR FOR SELECT id,data FROM t1; DECLARE c2 CURSOR FOR SELECT i FROM t2; DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=TRUE; OPEN c1; OPEN c2; read_loop: LOOP FETCH c1 INTO a,b; FETCH c2 INTO c; IF done THEN LEAVE read_loop; END IF; END LOOP; CLOSE c1; CLOSE c2; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p01/
行为 READ Table(1:141~1:143) /test/1/catalog1/schema1/t1/
行为 READ Table(1:181~1:183) /test/1/catalog1/schema1/t2/
------
SQL  CREATE PROCEDURE p02() BEGIN DECLARE c CURSOR FOR SELECT id,data FROM t1; OPEN c; BEGIN DECLARE v_id CHAR(16); DECLARE v_data INT; DECLARE EXIT HANDLER FOR NOT FOUND BEGIN END; WHILE TRUE DO FETCH c INTO v_id,v_data; END WHILE; END; CLOSE c; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p02/
行为 READ Table(1:70~1:72) /test/1/catalog1/schema1/t1/
------
SQL  CREATE PROCEDURE p03() BEGIN DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' SET @h='state'; DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @h='class'; DROP TABLE missing_t; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p03/
行为 READ ConfigKey(1:79~1:81) /test/1/h/
行为 DROP Table(1:160~1:169) /test/1/catalog1/schema1/missing_t/
------
SQL  CREATE PROCEDURE p04() BEGIN DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' SET @h='outer'; BEGIN DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @h='inner'; DROP TABLE missing_t; END; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p04/
行为 READ ConfigKey(1:79~1:81) /test/1/h/
行为 DROP Table(1:166~1:175) /test/1/catalog1/schema1/missing_t/
------
SQL  CREATE FUNCTION f05() RETURNS TEXT BEGIN DECLARE msg TEXT; DECLARE warn CONDITION FOR SQLSTATE '01234'; DECLARE CONTINUE HANDLER FOR SQLWARNING BEGIN GET DIAGNOSTICS CONDITION 1 msg=MESSAGE_TEXT; END; SIGNAL warn SET MESSAGE_TEXT='message'; RETURN msg; END;
行为 CREATE Function(1:16~1:19) /test/1/catalog1/schema1/f05/
------
SQL  CREATE PROCEDURE p08() BEGIN DECLARE seconds INT UNSIGNED DEFAULT 100; ALTER EVENT e08 ON SCHEDULE EVERY seconds SECOND STARTS '2030-01-01 00:00:00' ENABLE; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p08/
行为 ALTER Event(1:83~1:86) /test/1/catalog1/schema1/e08/
------
SQL  CREATE FUNCTION f10() RETURNS INT BEGIN DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END; BEGIN DECLARE CONTINUE HANDLER FOR SQLEXCEPTION RETURN f10(); BEGIN DECLARE CONTINUE HANDLER FOR SQLEXCEPTION RETURN f10(); RETURN f10(); END; END; RETURN 1; END;
行为 CREATE Function(1:16~1:19) /test/1/catalog1/schema1/f10/
行为 CALL Function(1:148~1:151) /test/1/catalog1/schema1/f10/
------
SQL  CREATE PROCEDURE p13() BEGIN DECLARE done INT DEFAULT 0; DECLARE a,b INT; DECLARE c1 CURSOR FOR SELECT x,y FROM t1 UNION DISTINCT SELECT x,y FROM t2; DECLARE c2 CURSOR FOR SELECT x,y FROM t1 UNION ALL SELECT x,y FROM t2; DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done=1; OPEN c1; FETCH c1 INTO a,b; CLOSE c1; SET done=0; OPEN c2; FETCH c2 INTO a,b; CLOSE c2; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p13/
行为 READ Table(1:112~1:114) /test/1/catalog1/schema1/t1/
行为 READ Table(1:146~1:148) /test/1/catalog1/schema1/t2/
------
SQL  CREATE PROCEDURE p14() BEGIN DECLARE done,a,b,v INT DEFAULT 0; DECLARE c CURSOR FOR SELECT x,(SELECT y FROM t2 WHERE t1.x=t2.x) FROM t1 WHERE x=v; DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1; OPEN c; FETCH c INTO a,b; CLOSE c; FLUSH TABLES; SET v=1; SET done=0; OPEN c; FETCH c INTO a,b; CLOSE c; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p14/
行为 READ Table(1:108~1:110) /test/1/catalog1/schema1/t2/
行为 READ Table(1:133~1:135) /test/1/catalog1/schema1/t1/
------
SQL  CREATE PROCEDURE p_tx() SET SESSION TRANSACTION READ ONLY;
行为 CREATE Procedure(1:17~1:21) /test/1/catalog1/schema1/p_tx/
------
SQL  CREATE PROCEDURE p_label_binlog() binlog: BEGIN LEAVE binlog; END binlog;
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/p_label_binlog/
------
SQL  CREATE FUNCTION split_type_enum_set.es_enum_fn(v CHAR(1))\n    RETURNS ENUM('a','b')\n    DETERMINISTIC NO SQL\n    RETURN v;
行为 CREATE Function(1:16~1:46) /test/1/catalog1/split_type_enum_set/es_enum_fn/
------
SQL  CREATE FUNCTION spatial_identity(input_value GEOMETRY)\nRETURNS GEOMETRY\nDETERMINISTIC\nRETURN input_value;
行为 CREATE Function(1:16~1:32) /test/1/catalog1/schema1/spatial_identity/
------
SQL  CREATE FUNCTION spatial_point(input_x DOUBLE,input_y DOUBLE)\nRETURNS POINT\nDETERMINISTIC\nRETURN POINT(input_x,input_y);
行为 CREATE Function(1:16~1:29) /test/1/catalog1/schema1/spatial_point/
行为 CALL Function(4:7~4:12) /test/1/catalog1/schema1/POINT/
------
SQL  CREATE PROCEDURE spatial_convert(\n  IN input_geometry GEOMETRY,\n  OUT output_point POINT,\n  INOUT output_line LINESTRING\n)\nBEGIN\n  DECLARE local_polygon POLYGON;\n  SET output_point=ST_Centroid(input_geometry);\n  SET output_line=ST_LineStringFromText('LINESTRING(0 0,1 1)');\n  SET local_polygon=ST_PolygonFromText('POLYGON((0 0,0 1,1 1,0 0))');\nEND;
行为 CREATE Procedure(1:17~1:32) /test/1/catalog1/schema1/spatial_convert/
行为 CALL Function(8:19~8:30) /test/1/catalog1/schema1/ST_Centroid/
行为 CALL Function(9:18~9:39) /test/1/catalog1/schema1/ST_LineStringFromText/
行为 CALL Function(10:20~10:38) /test/1/catalog1/schema1/ST_PolygonFromText/
------
SQL  CREATE FUNCTION integer_combine(\n  tiny_value TINYINT,\n  small_value SMALLINT UNSIGNED,\n  medium_value MEDIUMINT,\n  int_value INTEGER UNSIGNED,\n  big_value BIGINT\n)\nRETURNS BIGINT UNSIGNED\nDETERMINISTIC\nRETURN CAST(tiny_value+small_value+medium_value+int_value+big_value AS UNSIGNED);
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/integer_combine/
行为 CALL Function(10:7~10:11) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE PROCEDURE integer_copy(\n  IN tiny_input TINYINT,\n  IN small_input SMALLINT,\n  IN medium_input MEDIUMINT,\n  IN int_input INT,\n  IN big_input BIGINT,\n  OUT big_output BIGINT UNSIGNED,\n  INOUT bool_state BOOLEAN\n)\nBEGIN\n  DECLARE tiny_local TINYINT DEFAULT 0;\n  DECLARE small_local SMALLINT DEFAULT 0;\n  DECLARE medium_local MEDIUMINT DEFAULT 0;\n  DECLARE int_local INTEGER DEFAULT 0;\n  DECLARE big_local BIGINT DEFAULT 0;\n  SET tiny_local=tiny_input;\n  SET small_local=small_input;\n  SET medium_local=medium_input;\n  SET int_local=int_input;\n  SET big_local=big_input;\n  SET big_output=CAST(tiny_local+small_local+medium_local+int_local+big_local AS UNSIGNED);\n  SET bool_state=NOT bool_state;\nEND;
行为 CREATE Procedure(1:17~1:29) /test/1/catalog1/schema1/integer_copy/
行为 CALL Function(21:17~21:21) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE FUNCTION integer_attributes(\n  bool_value BOOLEAN,\n  tiny_value TINYINT(1),\n  tiny_unsigned TINYINT(1) UNSIGNED,\n  tiny_zerofill TINYINT(1) ZEROFILL\n)\nRETURNS BIGINT UNSIGNED\nDETERMINISTIC\nRETURN CAST(bool_value+tiny_value+tiny_unsigned+tiny_zerofill AS UNSIGNED);
行为 CREATE Function(1:16~1:34) /test/1/catalog1/schema1/integer_attributes/
行为 CALL Function(9:7~9:11) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE PROCEDURE integer_attribute_copy(\n  IN medium_input MEDIUMINT(8) ZEROFILL,\n  OUT bool_output BOOL\n)\nBEGIN\n  DECLARE integer_local INTEGER(11) SIGNED DEFAULT 0;\n  DECLARE big_local BIGINT UNSIGNED DEFAULT 0;\n  SET big_local=medium_input;\n  SET bool_output=big_local>=integer_local;\nEND;
行为 CREATE Procedure(1:17~1:39) /test/1/catalog1/schema1/integer_attribute_copy/
------
SQL  CREATE FUNCTION numeric_identity(\n  decimal_input DECIMAL(20,6),\n  numeric_input NUMERIC(12,4),\n  fixed_input FIXED(18,2),\n  float_input FLOAT,\n  double_input DOUBLE PRECISION,\n  real_input REAL\n)\nRETURNS DECIMAL(30,10)\nDETERMINISTIC\nRETURN decimal_input+numeric_input+fixed_input+float_input+double_input+real_input;
行为 CREATE Function(1:16~1:32) /test/1/catalog1/schema1/numeric_identity/
------
SQL  CREATE PROCEDURE numeric_copy(\n  IN decimal_input DECIMAL(20,6),\n  IN numeric_input NUMERIC(12,4),\n  IN fixed_input FIXED(18,2),\n  OUT float_output FLOAT,\n  OUT double_output DOUBLE PRECISION,\n  INOUT real_accumulator REAL\n)\nBEGIN\n  DECLARE decimal_local DECIMAL(30,10) DEFAULT 0;\n  DECLARE numeric_local NUMERIC(20,8) DEFAULT 0;\n  DECLARE fixed_local FIXED(24,6) DEFAULT 0;\n  DECLARE float_local FLOAT DEFAULT 0;\n  DECLARE double_local DOUBLE PRECISION DEFAULT 0;\n  DECLARE real_local REAL DEFAULT 0;\n  SET decimal_local=decimal_input;\n  SET numeric_local=numeric_input;\n  SET fixed_local=fixed_input;\n  SET float_local=decimal_local+numeric_local;\n  SET double_local=fixed_local+float_local;\n  SET real_local=real_accumulator+double_local;\n  SET float_output=float_local;\n  SET double_output=double_local;\n  SET real_accumulator=real_local;\nEND;
行为 CREATE Procedure(1:17~1:29) /test/1/catalog1/schema1/numeric_copy/
------
SQL  CREATE PROCEDURE p_37269() BEGIN DECLARE done INT DEFAULT 0; DECLARE varb INT DEFAULT 0; DECLARE vara INT DEFAULT 0; REPEAT SELECT NOW(); UNTIL done END REPEAT; WHILE varb DO SELECT NOW(); BEGIN SELECT NOW(); REPEAT SELECT NOW(); UNTIL done END REPEAT; IF vara THEN SELECT NOW(); REPEAT SELECT NOW(); LOOP SELECT NOW(); END LOOP; REPEAT SELECT NOW(); label1: WHILE varb DO SELECT NOW(); END WHILE label1; IF vara THEN SELECT NOW(); REPEAT SELECT NOW(); UNTIL done END REPEAT; BEGIN SELECT NOW(); WHILE varb DO SELECT NOW(); label1: WHILE varb DO SELECT NOW(); END WHILE label1; IF vara THEN SELECT NOW(); WHILE varb DO SELECT NOW(); LOOP SELECT NOW(); END LOOP; REPEAT SELECT NOW(); LOOP SELECT NOW(); WHILE varb DO SELECT NOW(); END WHILE; REPEAT SELECT NOW(); label1: LOOP SELECT NOW(); IF vara THEN SELECT NOW(); END IF; END LOOP label1; UNTIL done END REPEAT; END LOOP; UNTIL done END REPEAT; END WHILE; END IF; END WHILE; END; END IF; UNTIL done END REPEAT; UNTIL done END REPEAT; END IF; END; END WHILE; END;
行为 CREATE Procedure(1:17~1:24) /test/1/catalog1/schema1/p_37269/
行为 CALL Function(1:131~1:134) /test/1/catalog1/schema1/NOW/
------
SQL  CREATE PROCEDURE split_case_scope(arg INT) BEGIN CASE arg WHEN 1 THEN BEGIN DECLARE i TINYINT DEFAULT 2; WHILE i>0 DO CASE MOD(i,2) WHEN 0 THEN SET @split_case_scope='even'; ELSE SET @split_case_scope='odd'; END CASE; SET i=i-1; END WHILE; END; ELSE SET @split_case_scope='other'; END CASE; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/split_case_scope/
行为 CALL Function(1:123~1:126) /test/1/catalog1/schema1/MOD/
行为 READ ConfigKey(1:148~1:165) /test/1/split_case_scope/
------
SQL  CREATE PROCEDURE split_drop_trigger() DROP TRIGGER tr1;
行为 CREATE Procedure(1:17~1:35) /test/1/catalog1/schema1/split_drop_trigger/
行为 DROP Trigger(1:51~1:54) /test/1/catalog1/schema1/tr1/
------
SQL  CREATE PROCEDURE split_longprocedure(OUT out1 INT) DETERMINISTIC BEGIN SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1;SELECT COUNT(*) INTO out1 FROM t1; END;
行为 CREATE Procedure(1:17~1:36) /test/1/catalog1/schema1/split_longprocedure/
行为 CALL Function(1:78~1:83) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:102~1:104) /test/1/catalog1/schema1/t1/
------
SQL  CREATE PROCEDURE proc_knn() BEGIN EXPLAIN FORMAT=TREE SELECT ST_Distance(location,POINT(0,0)) AS d FROM cafe ORDER BY d; END;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/proc_knn/
行为 CALL Function(1:61~1:72) /test/1/catalog1/schema1/ST_Distance/
行为 CALL Function(1:82~1:87) /test/1/catalog1/schema1/POINT/
行为 READ Table(1:104~1:108) /test/1/catalog1/schema1/cafe/
------
SQL  CREATE PROCEDURE gap_cond_numeric() BEGIN DECLARE c CONDITION FOR 1146; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/gap_cond_numeric/
------
SQL  CREATE PROCEDURE gap_handler_value() BEGIN DECLARE CONTINUE HANDLER FOR SQLSTATE VALUE '42S02' SET @x=1; DROP TABLE missing_t; END;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/gap_handler_value/
行为 READ ConfigKey(1:99~1:101) /test/1/x/
行为 DROP Table(1:116~1:125) /test/1/catalog1/schema1/missing_t/
------
SQL  CREATE PROCEDURE gap_fetch_from() BEGIN DECLARE x INT; DECLARE c CURSOR FOR SELECT 1; OPEN c; FETCH FROM c INTO x; CLOSE c; END;
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/gap_fetch_from/
------
SQL  CREATE PROCEDURE gap_scalar_default() BEGIN DECLARE x INT DEFAULT (SELECT 1); SELECT x; END;
行为 CREATE Procedure(1:17~1:35) /test/1/catalog1/schema1/gap_scalar_default/
------
SQL  CREATE PROCEDURE gap_direct_repeat() REPEAT SET @x=1; UNTIL @x=1 END REPEAT;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/gap_direct_repeat/
行为 READ ConfigKey(1:48~1:50) /test/1/x/
------
SQL  CREATE PROCEDURE gap_direct_while() w: WHILE 0 DO LEAVE w; END WHILE w;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/gap_direct_while/
------
SQL  CREATE PROCEDURE gap_direct_if() IF 1 THEN SET @x=1; ELSE SET @x=2; END IF;
行为 CREATE Procedure(1:17~1:30) /test/1/catalog1/schema1/gap_direct_if/
行为 READ ConfigKey(1:47~1:49) /test/1/x/
------
SQL  CREATE PROCEDURE gap_direct_case() CASE WHEN 1 THEN SET @x=1; ELSE SET @x=2; END CASE;
行为 CREATE Procedure(1:17~1:32) /test/1/catalog1/schema1/gap_direct_case/
行为 READ ConfigKey(1:56~1:58) /test/1/x/
------
SQL  CREATE PROCEDURE gap_direct_loop() l: LOOP LEAVE l; END LOOP l;
行为 CREATE Procedure(1:17~1:32) /test/1/catalog1/schema1/gap_direct_loop/
------
SQL  CREATE PROCEDURE gap_dynamic_literal() BEGIN PREPARE s FROM 'SELECT 1'; EXECUTE s; DEALLOCATE PREPARE s; END;
行为 CREATE Procedure(1:17~1:36) /test/1/catalog1/schema1/gap_dynamic_literal/
行为 ADMIN PrepareStatement(1:53~1:54) /test/1/s/
------
SQL  CREATE PROCEDURE gap_proc_call() CALL gap_sink();
行为 CREATE Procedure(1:17~1:30) /test/1/catalog1/schema1/gap_proc_call/
行为 CALL Procedure(1:38~1:46) /test/1/catalog1/schema1/gap_sink/
------
SQL  CREATE FUNCTION gap_fn_prepare() RETURNS INT PREPARE s FROM 'SELECT 1';
行为 CREATE Function(1:16~1:30) /test/1/catalog1/schema1/gap_fn_prepare/
行为 ADMIN PrepareStatement(1:53~1:54) /test/1/s/
------
SQL  CREATE PROCEDURE gap_cursor_before_var() BEGIN DECLARE c CURSOR FOR SELECT 1; DECLARE x INT; END;
行为 CREATE Procedure(1:17~1:38) /test/1/catalog1/schema1/gap_cursor_before_var/
------
SQL  CREATE PROCEDURE gap_handler_before_cursor() BEGIN DECLARE CONTINUE HANDLER FOR NOT FOUND SET @x=1; DECLARE c CURSOR FOR SELECT 1; END;
行为 CREATE Procedure(1:17~1:42) /test/1/catalog1/schema1/gap_handler_before_cursor/
行为 READ ConfigKey(1:94~1:96) /test/1/x/
------
SQL  CREATE FUNCTION audit_kill_fn(tid INT) RETURNS INT BEGIN DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END; KILL tid; RETURN (SELECT COUNT(*) = 0 FROM INFORMATION_SCHEMA.PROCESSLIST WHERE ID = tid); END;
行为 CREATE Function(1:16~1:29) /test/1/catalog1/schema1/audit_kill_fn/
行为 CALL Function(1:135~1:140) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:153~1:183) /test/1/catalog1/INFORMATION_SCHEMA/PROCESSLIST/
------
SQL  CREATE PROCEDURE audit_flush_proc() BEGIN DECLARE x INT DEFAULT 1; WHILE x DO SET x = x - 1; FLUSH STATUS; END WHILE; END;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/audit_flush_proc/
------
SQL  CREATE PROCEDURE split_handler_rollback()\nBEGIN\n  DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;\n  START TRANSACTION;\n  INSERT INTO split_handler_table VALUES (1);\n  INSERT INTO split_handler_table VALUES (2);\n  COMMIT WORK;\nEND;
行为 CREATE Procedure(1:17~1:39) /test/1/catalog1/schema1/split_handler_rollback/
行为 INSERT Table(5:14~5:33) /test/1/catalog1/schema1/split_handler_table/
------
SQL  CREATE PROCEDURE split_proc_create_view() CREATE VIEW split_proc_view AS SELECT 1 FROM (SELECT 1) AS d1;
行为 CREATE Procedure(1:17~1:39) /test/1/catalog1/schema1/split_proc_create_view/
行为 CREATE View(1:54~1:69) /test/1/catalog1/schema1/split_proc_view/
------
SQL  CREATE PROCEDURE split_proc_create_event() CREATE EVENT split_proc_event_body ON SCHEDULE EVERY @a SECOND DO SET @a=5;
行为 CREATE Procedure(1:17~1:40) /test/1/catalog1/schema1/split_proc_create_event/
行为 CREATE Event(1:56~1:77) /test/1/catalog1/schema1/split_proc_event_body/
行为 READ ConfigKey(1:96~1:98) /test/1/a/
------
SQL  CREATE PROCEDURE p_cursor() BEGIN DECLARE c CURSOR FOR SELECT a FROM t1 FOR UPDATE; END;
行为 CREATE Procedure(1:17~1:25) /test/1/catalog1/schema1/p_cursor/
行为 READ Table(1:69~1:71) /test/1/catalog1/schema1/t1/
------
SQL  CREATE DEFINER = CURRENT_USER FUNCTION split_info_current_user_fn() RETURNS INT DETERMINISTIC NO SQL RETURN 1;
行为 CREATE Function(1:39~1:65) /test/1/catalog1/schema1/split_info_current_user_fn/
------
SQL  CREATE DEFINER = CURRENT_USER() FUNCTION split_info_current_user_fn_paren() RETURNS INT DETERMINISTIC NO SQL RETURN 2;
行为 CREATE Function(1:41~1:73) /test/1/catalog1/schema1/split_info_current_user_fn_paren/
------
SQL  CREATE PROCEDURE p_signal_param(IN msg TEXT) SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT=msg;
行为 CREATE Procedure(1:17~1:31) /test/1/catalog1/schema1/p_signal_param/
------
SQL  CREATE PROCEDURE p_resignal_param(IN msg TEXT) RESIGNAL SQLSTATE '45000' SET MESSAGE_TEXT=msg;
行为 CREATE Procedure(1:17~1:33) /test/1/catalog1/schema1/p_resignal_param/
------
SQL  CREATE PROCEDURE split_acl_native.split_native_acl_p() SET PASSWORD FOR 'split_acl_0720b'@'%' = '12345';
行为 CREATE Procedure(1:17~1:52) /test/1/catalog1/split_acl_native/split_native_acl_p/
------
SQL  CREATE PROCEDURE split_into_mix_56(x CHAR(16), y INT) BEGIN SELECT id, data INTO x, @z FROM split_into_mix.t1 LIMIT 1; END;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/split_into_mix_56/
行为 READ ConfigKey(1:84~1:86) /test/1/z/
行为 READ Table(1:92~1:109) /test/1/catalog1/split_into_mix/t1/
------
SQL  CREATE FUNCTION split_missing_udf_57 RETURNS STRING SONAME 'split_missing_udf.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_udf_57/
行为 CREATE File(1:59~1:81) /test/1/split_missing_udf.so/
------
SQL  CREATE AGGREGATE FUNCTION split_missing_agg_57 RETURNS REAL SONAME 'split_missing_agg.so';
行为 CREATE Function(1:26~1:46) /test/1/catalog1/schema1/split_missing_agg_57/
行为 CREATE File(1:67~1:89) /test/1/split_missing_agg.so/
------
SQL  CREATE FUNCTION split_missing_int_57 RETURNS INTEGER SONAME 'split_missing_int.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_int_57/
行为 CREATE File(1:60~1:82) /test/1/split_missing_int.so/
------
SQL  CREATE FUNCTION split_missing_decimal_57 RETURNS DECIMAL SONAME 'split_missing_decimal.so';
行为 CREATE Function(1:16~1:40) /test/1/catalog1/schema1/split_missing_decimal_57/
行为 CREATE File(1:64~1:90) /test/1/split_missing_decimal.so/
------
SQL  CREATE FUNCTION f_labels() RETURNS INT\nBEGIN\n  INVISIBLE: LOOP LEAVE INVISIBLE; END LOOP INVISIBLE;\n  ROLE: LOOP LEAVE ROLE; END LOOP ROLE;\n  SECONDARY: LOOP LEAVE SECONDARY; END LOOP SECONDARY;\n  SECONDARY_ENGINE: LOOP LEAVE SECONDARY_ENGINE; END LOOP SECONDARY_ENGINE;\n  SECONDARY_LOAD: LOOP LEAVE SECONDARY_LOAD; END LOOP SECONDARY_LOAD;\n  SECONDARY_UNLOAD: LOOP LEAVE SECONDARY_UNLOAD; END LOOP SECONDARY_UNLOAD;\n  VISIBLE: LOOP LEAVE VISIBLE; END LOOP VISIBLE;\n  RETURN 1;\nEND;
行为 CREATE Function(1:16~1:24) /test/1/catalog1/schema1/f_labels/
------
SQL  CREATE PROCEDURE split_limit57.proc_limit(IN p_offset INT,IN p_count INT)\nBEGIN\n  SELECT id FROM split_limit57.t ORDER BY id LIMIT p_count;\n  SELECT id FROM split_limit57.t ORDER BY id LIMIT p_offset,p_count;\n  SELECT id FROM split_limit57.t ORDER BY id LIMIT p_count OFFSET p_offset;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/split_limit57/proc_limit/
行为 READ Table(3:17~3:32) /test/1/catalog1/split_limit57/t/
------
SQL  CREATE PROCEDURE proc_stacked_diag()\nBEGIN\n  DECLARE EXIT HANDLER FOR SQLEXCEPTION\n  BEGIN\n    GET STACKED DIAGNOSTICS CONDITION 1 @stack_state = RETURNED_SQLSTATE, @stack_message = MESSAGE_TEXT;\n    RESIGNAL;\n  END;\n  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'stacked diagnostics';\nEND;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/proc_stacked_diag/
行为 READ ConfigKey(5:40~5:52) /test/1/stack_state/
行为 READ ConfigKey(5:74~5:88) /test/1/stack_message/
------
SQL  CREATE PROCEDURE p11() BEGIN DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN GET CURRENT DIAGNOSTICS CONDITION 1 @m1=MESSAGE_TEXT; GET STACKED DIAGNOSTICS CONDITION 1 @m2=MESSAGE_TEXT; SELECT 1; GET CURRENT DIAGNOSTICS @n=NUMBER; GET STACKED DIAGNOSTICS CONDITION 1 @m3=MESSAGE_TEXT; END; DROP TABLE missing_t; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p11/
行为 READ ConfigKey(1:109~1:112) /test/1/m1/
行为 READ ConfigKey(1:163~1:166) /test/1/m2/
行为 READ ConfigKey(1:215~1:217) /test/1/n/
行为 READ ConfigKey(1:262~1:265) /test/1/m3/
行为 DROP Table(1:296~1:305) /test/1/catalog1/schema1/missing_t/
------
SQL  CREATE PROCEDURE p12() BEGIN DECLARE CONTINUE HANDLER FOR SQLWARNING BEGIN GET CURRENT DIAGNOSTICS CONDITION 1 @m1=MESSAGE_TEXT; GET STACKED DIAGNOSTICS CONDITION 1 @m2=MESSAGE_TEXT; RESIGNAL SET MYSQL_ERRNO=9999,MESSAGE_TEXT='mapped'; GET CURRENT DIAGNOSTICS CONDITION 1 @m3=MESSAGE_TEXT; GET STACKED DIAGNOSTICS CONDITION 1 @m4=MESSAGE_TEXT; END; SIGNAL SQLSTATE '01001'; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p12/
行为 READ ConfigKey(1:111~1:114) /test/1/m1/
行为 READ ConfigKey(1:165~1:168) /test/1/m2/
行为 READ ConfigKey(1:272~1:275) /test/1/m3/
行为 READ ConfigKey(1:326~1:329) /test/1/m4/
------
SQL  CREATE FUNCTION split_type_json.json_identity(v JSON)\n    RETURNS JSON\n    DETERMINISTIC NO SQL\n    RETURN v;
行为 CREATE Function(1:16~1:45) /test/1/catalog1/split_type_json/json_identity/
------
SQL  CREATE PROCEDURE split_type_json.json_copy(IN p JSON, OUT q JSON)\n    BEGIN\n      DECLARE local_doc JSON;\n      SET local_doc=JSON_SET(p,'$.copied',TRUE);\n      SET q=local_doc;\n    END;
行为 CREATE Procedure(1:17~1:42) /test/1/catalog1/split_type_json/json_copy/
行为 CALL Function(4:20~4:28) /test/1/catalog1/schema1/JSON_SET/
------
SQL  CREATE PROCEDURE split_into_mix_57(x CHAR(16), y INT) BEGIN SELECT id, data INTO x, @z FROM split_into_mix.t1 LIMIT 1; END;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/split_into_mix_57/
行为 READ ConfigKey(1:84~1:86) /test/1/z/
行为 READ Table(1:92~1:109) /test/1/catalog1/split_into_mix/t1/
------
SQL  CREATE FUNCTION split_missing_udf_80 RETURNS STRING SONAME 'split_missing_udf.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_udf_80/
行为 CREATE File(1:59~1:81) /test/1/split_missing_udf.so/
------
SQL  CREATE AGGREGATE FUNCTION split_missing_agg_80 RETURNS REAL SONAME 'split_missing_agg.so';
行为 CREATE Function(1:26~1:46) /test/1/catalog1/schema1/split_missing_agg_80/
行为 CREATE File(1:67~1:89) /test/1/split_missing_agg.so/
------
SQL  CREATE FUNCTION split_missing_int_80 RETURNS INTEGER SONAME 'split_missing_int.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_int_80/
行为 CREATE File(1:60~1:82) /test/1/split_missing_int.so/
------
SQL  CREATE FUNCTION split_missing_decimal_80 RETURNS DECIMAL SONAME 'split_missing_decimal.so';
行为 CREATE Function(1:16~1:40) /test/1/catalog1/schema1/split_missing_decimal_80/
行为 CREATE File(1:64~1:90) /test/1/split_missing_decimal.so/
------
SQL  CREATE FUNCTION IF NOT EXISTS split_missing_ifne_80 RETURNS INTEGER SONAME 'split_missing_ifne.so';
行为 CREATE Function(1:30~1:51) /test/1/catalog1/schema1/split_missing_ifne_80/
行为 CREATE File(1:75~1:98) /test/1/split_missing_ifne.so/
------
SQL  CREATE AGGREGATE FUNCTION IF NOT EXISTS split_missing_agg_ifne_80 RETURNS REAL SONAME 'split_missing_ifne.so';
行为 CREATE Function(1:40~1:65) /test/1/catalog1/schema1/split_missing_agg_ifne_80/
行为 CREATE File(1:86~1:109) /test/1/split_missing_ifne.so/
------
SQL  CREATE FUNCTION f_labels() RETURNS INT\nBEGIN\n  ACCOUNT: LOOP LEAVE ACCOUNT; END LOOP ACCOUNT;\n  ALWAYS: LOOP LEAVE ALWAYS; END LOOP ALWAYS;\n  BACKUP: LOOP LEAVE BACKUP; END LOOP BACKUP;\n  CLOSE: LOOP LEAVE CLOSE; END LOOP CLOSE;\n  FORMAT: LOOP LEAVE FORMAT; END LOOP FORMAT;\n  GROUP_REPLICATION: LOOP LEAVE GROUP_REPLICATION; END LOOP GROUP_REPLICATION;\n  HOST: LOOP LEAVE HOST; END LOOP HOST;\n  INVISIBLE: LOOP LEAVE INVISIBLE; END LOOP INVISIBLE;\n  OPEN: LOOP LEAVE OPEN; END LOOP OPEN;\n  OPTIONS: LOOP LEAVE OPTIONS; END LOOP OPTIONS;\n  OWNER: LOOP LEAVE OWNER; END LOOP OWNER;\n  PARSER: LOOP LEAVE PARSER; END LOOP PARSER;\n  PORT: LOOP LEAVE PORT; END LOOP PORT;\n  REMOVE: LOOP LEAVE REMOVE; END LOOP REMOVE;\n  RESTORE: LOOP LEAVE RESTORE; END LOOP RESTORE;\n  ROLE: LOOP LEAVE ROLE; END LOOP ROLE;\n  SECONDARY: LOOP LEAVE SECONDARY; END LOOP SECONDARY;\n  SECONDARY_ENGINE: LOOP LEAVE SECONDARY_ENGINE; END LOOP SECONDARY_ENGINE;\n  SECONDARY_LOAD: LOOP LEAVE SECONDARY_LOAD; END LOOP SECONDARY_LOAD;\n  SECONDARY_UNLOAD: LOOP LEAVE SECONDARY_UNLOAD; END LOOP SECONDARY_UNLOAD;\n  SECURITY: LOOP LEAVE SECURITY; END LOOP SECURITY;\n  SERVER: LOOP LEAVE SERVER; END LOOP SERVER;\n  SOCKET: LOOP LEAVE SOCKET; END LOOP SOCKET;\n  SONAME: LOOP LEAVE SONAME; END LOOP SONAME;\n  UPGRADE: LOOP LEAVE UPGRADE; END LOOP UPGRADE;\n  VISIBLE: LOOP LEAVE VISIBLE; END LOOP VISIBLE;\n  WRAPPER: LOOP LEAVE WRAPPER; END LOOP WRAPPER;\n  RETURN 1;\nEND;
行为 CREATE Function(1:16~1:24) /test/1/catalog1/schema1/f_labels/
------
SQL  CREATE FUNCTION f_digest() RETURNS LONGTEXT DETERMINISTIC\nBEGIN\n  DECLARE ret LONGTEXT;\n  SELECT STATEMENT_DIGEST_TEXT('DROP TABLE t') INTO ret;\n  RETURN ret;\nEND;
行为 CREATE Function(1:16~1:24) /test/1/catalog1/schema1/f_digest/
行为 CALL Function(4:9~4:30) /test/1/catalog1/schema1/STATEMENT_DIGEST_TEXT/
------
SQL  CREATE PROCEDURE sub1(id CHAR(10) CHARACTER SET utf8) BEGIN SELECT * FROM view1 WHERE table_name=id COLLATE utf8_tolower_ci; EXPLAIN SELECT * FROM view1 WHERE table_name=id COLLATE utf8_tolower_ci; SELECT * FROM view2 WHERE table_name=id; EXPLAIN SELECT * FROM view2 WHERE table_name=id; SELECT * FROM dd_table WHERE name COLLATE utf8_tolower_ci=id; EXPLAIN SELECT * FROM dd_table WHERE name COLLATE utf8_tolower_ci=id; SELECT * FROM view1 WHERE id COLLATE utf8_tolower_ci=table_name; EXPLAIN SELECT * FROM view1 WHERE id COLLATE utf8_tolower_ci=table_name; SELECT * FROM view2 WHERE id=table_name; EXPLAIN SELECT * FROM view2 WHERE id=table_name; SELECT * FROM dd_table WHERE id=name COLLATE utf8_tolower_ci; EXPLAIN SELECT * FROM dd_table WHERE id=name COLLATE utf8_tolower_ci; SELECT * FROM view1 WHERE table_name COLLATE utf8_tolower_ci=id; EXPLAIN SELECT * FROM view1 WHERE table_name COLLATE utf8_tolower_ci=id; END;
行为 CREATE Procedure(1:17~1:21) /test/1/catalog1/schema1/sub1/
行为 READ Table(1:74~1:79) /test/1/catalog1/schema1/view1/
行为 READ Table(1:212~1:217) /test/1/catalog1/schema1/view2/
行为 READ Table(1:302~1:310) /test/1/catalog1/schema1/dd_table/
------
SQL  CREATE PROCEDURE split_limit80.proc_limit(IN p_offset INT,IN p_count INT)\nBEGIN\n  SELECT id FROM split_limit80.t ORDER BY id LIMIT p_count;\n  SELECT id FROM split_limit80.t ORDER BY id LIMIT p_offset,p_count;\n  SELECT id FROM split_limit80.t ORDER BY id LIMIT p_count OFFSET p_offset;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/split_limit80/proc_limit/
行为 READ Table(3:17~3:32) /test/1/catalog1/split_limit80/t/
------
SQL  CREATE PROCEDURE split_window_tail.p_stable(IN n INT) BEGIN DO NTILE(n) OVER(); DO LEAD(1,n) OVER(); DO LAG(1,n) OVER(); END;
行为 CREATE Procedure(1:17~1:43) /test/1/catalog1/split_window_tail/p_stable/
行为 CALL Function(1:63~1:68) /test/1/catalog1/schema1/NTILE/
行为 CALL Function(1:83~1:87) /test/1/catalog1/schema1/LEAD/
行为 CALL Function(1:104~1:107) /test/1/catalog1/schema1/LAG/
------
SQL  CREATE PROCEDURE split_window_tail.p_select() SELECT AVG(a) OVER () FROM split_window_tail.t;
行为 CREATE Procedure(1:17~1:43) /test/1/catalog1/split_window_tail/p_select/
行为 CALL Function(1:53~1:56) /test/1/catalog1/schema1/AVG/
行为 READ Table(1:73~1:92) /test/1/catalog1/split_window_tail/t/
------
SQL  CREATE PROCEDURE IF NOT EXISTS p_ifne() BEGIN END;
行为 CREATE Procedure(1:31~1:37) /test/1/catalog1/schema1/p_ifne/
------
SQL  CREATE FUNCTION IF NOT EXISTS f_ifne() RETURNS INT DETERMINISTIC NO SQL RETURN 0;
行为 CREATE Function(1:30~1:36) /test/1/catalog1/schema1/f_ifne/
------
SQL  CREATE PROCEDURE split_window_bugs.p_window_counts() BEGIN SELECT COUNT(*) OVER (ROWS BETWEEN 5 PRECEDING AND 5 FOLLOWING) AS a,COUNT(*) OVER (ROWS BETWEEN 5 PRECEDING AND 5 FOLLOWING)+1 AS a_plus,COUNT(*) OVER (ROWS BETWEEN 5 PRECEDING AND 5 FOLLOWING)-1 AS a_minus,COUNT(*) OVER () AS b,COUNT(*) OVER ()+1 AS b_plus,COUNT(*) OVER ()-1 AS b_minus FROM split_window_bugs.t_proc; END;
行为 CREATE Procedure(1:17~1:50) /test/1/catalog1/split_window_bugs/p_window_counts/
行为 CALL Function(1:66~1:71) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:353~1:377) /test/1/catalog1/split_window_bugs/t_proc/
------
SQL  CREATE PROCEDURE p15() BEGIN DECLARE done BOOL DEFAULT FALSE; DECLARE n INT; DECLARE c CURSOR FOR WITH RECURSIVE seq(n) AS (SELECT 1 UNION ALL SELECT n+1 FROM seq WHERE n<3) SELECT n FROM seq; DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=TRUE; OPEN c; FETCH c INTO n; CLOSE c; END;
行为 CREATE Procedure(1:17~1:20) /test/1/catalog1/schema1/p15/
------
SQL  CREATE PROCEDURE audit_analyze_proc() ANALYZE TABLE audit_admin_missing UPDATE HISTOGRAM ON missing_col;
行为 CREATE Procedure(1:17~1:35) /test/1/catalog1/schema1/audit_analyze_proc/
行为 ADMIN Table(1:52~1:71) /test/1/catalog1/schema1/audit_admin_missing/
------
SQL  CREATE PROCEDURE split_into_mix_80(x CHAR(16), y INT) BEGIN SELECT id, data INTO x, @z FROM split_into_mix.t1 LIMIT 1; END;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/split_into_mix_80/
行为 READ ConfigKey(1:84~1:86) /test/1/z/
行为 READ Table(1:92~1:109) /test/1/catalog1/split_into_mix/t1/
------
SQL  CREATE FUNCTION split_rg_create_80() RETURNS INT BEGIN CREATE RESOURCE GROUP codex_rg_inner TYPE=USER; RETURN 0; END;
行为 CREATE Function(1:16~1:34) /test/1/catalog1/schema1/split_rg_create_80/
行为 CREATE ResourceGroup(1:77~1:91) /test/1/catalog1/schema1/codex_rg_inner/
------
SQL  CREATE FUNCTION split_rg_set_80() RETURNS INT BEGIN SET RESOURCE GROUP USR_default; RETURN 0; END;
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/split_rg_set_80/
行为 ADMIN ResourceGroup(1:71~1:82) /test/1/catalog1/schema1/USR_default/
------
SQL  CREATE DEFINER=`root`@`%` FUNCTION `GetEmployeeSalary`(emp_id int) RETURNS decimal(10,0)\n    READS SQL DATA\nbegin\n     DECLARE emp_salary DECIMAL(10, 2);\n    SELECT salary INTO emp_salary FROM employees WHERE id = emp_id;\n    RETURN emp_salary;\nend;
行为 CREATE Function(1:35~1:54) /test/1/catalog1/schema1/GetEmployeeSalary/
行为 READ Table(5:39~5:48) /test/1/catalog1/schema1/employees/
------
SQL  CREATE DEFINER=`root`@`%` PROCEDURE `GetEmployeesAboveSalary`(IN min_salary decimal(10, 2))\nbegin\n SELECT * FROM employees WHERE salary > min_salary;\nend;
行为 CREATE Procedure(1:36~1:61) /test/1/catalog1/schema1/GetEmployeesAboveSalary/
行为 READ Table(3:15~3:24) /test/1/catalog1/schema1/employees/
------
SQL  CREATE FUNCTION split_missing_udf_84 RETURNS STRING SONAME 'split_missing_udf.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_udf_84/
行为 CREATE File(1:59~1:81) /test/1/split_missing_udf.so/
------
SQL  CREATE AGGREGATE FUNCTION split_missing_agg_84 RETURNS REAL SONAME 'split_missing_agg.so';
行为 CREATE Function(1:26~1:46) /test/1/catalog1/schema1/split_missing_agg_84/
行为 CREATE File(1:67~1:89) /test/1/split_missing_agg.so/
------
SQL  CREATE FUNCTION split_missing_int_84 RETURNS INTEGER SONAME 'split_missing_int.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_int_84/
行为 CREATE File(1:60~1:82) /test/1/split_missing_int.so/
------
SQL  CREATE FUNCTION split_missing_decimal_84 RETURNS DECIMAL SONAME 'split_missing_decimal.so';
行为 CREATE Function(1:16~1:40) /test/1/catalog1/schema1/split_missing_decimal_84/
行为 CREATE File(1:64~1:90) /test/1/split_missing_decimal.so/
------
SQL  CREATE FUNCTION IF NOT EXISTS split_missing_ifne_84 RETURNS INTEGER SONAME 'split_missing_ifne.so';
行为 CREATE Function(1:30~1:51) /test/1/catalog1/schema1/split_missing_ifne_84/
行为 CREATE File(1:75~1:98) /test/1/split_missing_ifne.so/
------
SQL  CREATE AGGREGATE FUNCTION IF NOT EXISTS split_missing_agg_ifne_84 RETURNS REAL SONAME 'split_missing_ifne.so';
行为 CREATE Function(1:40~1:65) /test/1/catalog1/schema1/split_missing_agg_ifne_84/
行为 CREATE File(1:86~1:109) /test/1/split_missing_ifne.so/
------
SQL  CREATE FUNCTION f_ext_dollar(n INTEGER) RETURNS INTEGER LANGUAGE JAVASCRIPT AS $$ return n; $$;
行为 CREATE Function(1:16~1:28) /test/1/catalog1/schema1/f_ext_dollar/
------
SQL  CREATE FUNCTION f_ext_string(n INTEGER) RETURNS INTEGER LANGUAGE JAVASCRIPT AS 'return n;';
行为 CREATE Function(1:16~1:28) /test/1/catalog1/schema1/f_ext_string/
------
SQL  CREATE FUNCTION codex_tagged(n INTEGER) RETURNS INTEGER DETERMINISTIC LANGUAGE JAVASCRIPT AS $abdc_1234$ const marker = "$$ $other$"; return n; $abdc_1234$;
行为 CREATE Function(1:16~1:28) /test/1/catalog1/schema1/codex_tagged/
------
SQL  CREATE PROCEDURE p_ext_dollar(n INTEGER) LANGUAGE JAVASCRIPT AS $$ let x = n; $$;
行为 CREATE Procedure(1:17~1:29) /test/1/catalog1/schema1/p_ext_dollar/
------
SQL  CREATE PROCEDURE sub1(id CHAR(10) CHARACTER SET utf8mb3) BEGIN SELECT * FROM view1 WHERE table_name=id COLLATE utf8mb3_tolower_ci; EXPLAIN SELECT * FROM view1 WHERE table_name=id COLLATE utf8mb3_tolower_ci; SELECT * FROM view2 WHERE table_name=id; EXPLAIN SELECT * FROM view2 WHERE table_name=id; SELECT * FROM dd_table WHERE name COLLATE utf8mb3_tolower_ci=id; EXPLAIN SELECT * FROM dd_table WHERE name COLLATE utf8mb3_tolower_ci=id; SELECT * FROM view1 WHERE id COLLATE utf8mb3_tolower_ci=table_name; EXPLAIN SELECT * FROM view1 WHERE id COLLATE utf8mb3_tolower_ci=table_name; SELECT * FROM view2 WHERE id=table_name; EXPLAIN SELECT * FROM view2 WHERE id=table_name; SELECT * FROM dd_table WHERE id=name COLLATE utf8mb3_tolower_ci; EXPLAIN SELECT * FROM dd_table WHERE id=name COLLATE utf8mb3_tolower_ci; SELECT * FROM view1 WHERE table_name COLLATE utf8mb3_tolower_ci=id; EXPLAIN SELECT * FROM view1 WHERE table_name COLLATE utf8mb3_tolower_ci=id; END;
行为 CREATE Procedure(1:17~1:21) /test/1/catalog1/schema1/sub1/
行为 READ Table(1:77~1:82) /test/1/catalog1/schema1/view1/
行为 READ Table(1:221~1:226) /test/1/catalog1/schema1/view2/
行为 READ Table(1:311~1:319) /test/1/catalog1/schema1/dd_table/
------
SQL  CREATE PROCEDURE split_limit84.proc_limit(IN p_offset INT,IN p_count INT)\nBEGIN\n  SELECT id FROM split_limit84.t ORDER BY id LIMIT p_count;\n  SELECT id FROM split_limit84.t ORDER BY id LIMIT p_offset,p_count;\n  SELECT id FROM split_limit84.t ORDER BY id LIMIT p_count OFFSET p_offset;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/split_limit84/proc_limit/
行为 READ Table(3:17~3:32) /test/1/catalog1/split_limit84/t/
------
SQL  CREATE PROCEDURE split84.proc_cursor_diag(IN p_limit INT, OUT p_count INT, OUT p_state CHAR(5), OUT p_msg TEXT)\nBEGIN\n  DECLARE v_id INT DEFAULT 0;\n  DECLARE v_data VARCHAR(20) DEFAULT '';\n  DECLARE done BOOL DEFAULT FALSE;\n  DECLARE cur1 CURSOR FOR SELECT id, data FROM split84.proc_src ORDER BY id;\n  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;\n  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n  BEGIN\n    GET STACKED DIAGNOSTICS CONDITION 1 p_state = RETURNED_SQLSTATE, p_msg = MESSAGE_TEXT;\n  END;\n  SET p_count = 0;\n  OPEN cur1;\n  read_loop: LOOP\n    FETCH NEXT FROM cur1 INTO v_id, v_data;\n    IF done THEN\n      LEAVE read_loop;\n    END IF;\n    SET p_count = p_count + 1;\n    IF p_count >= p_limit THEN\n      ITERATE read_loop;\n    END IF;\n  END LOOP read_loop;\n  CLOSE cur1;\n  GET DIAGNOSTICS @diag_count = NUMBER, @diag_rows = ROW_COUNT;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/split84/proc_cursor_diag/
行为 READ Table(6:47~6:63) /test/1/catalog1/split84/proc_src/
行为 READ ConfigKey(25:18~25:29) /test/1/diag_count/
行为 READ ConfigKey(25:40~25:50) /test/1/diag_rows/
------
SQL  CREATE PROCEDURE split84.proc_resignal_info()\nBEGIN\n  DECLARE bad CONDITION FOR SQLSTATE '45000';\n  DECLARE EXIT HANDLER FOR bad\n  BEGIN\n    GET STACKED DIAGNOSTICS CONDITION 1 @rs_errno = MYSQL_ERRNO, @rs_message = MESSAGE_TEXT;\n    RESIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'resignal changed', MYSQL_ERRNO = 1644;\n  END;\n  SIGNAL bad SET MESSAGE_TEXT = 'original signal', MYSQL_ERRNO = 1644;\nEND;
行为 CREATE Procedure(1:17~1:43) /test/1/catalog1/split84/proc_resignal_info/
行为 READ ConfigKey(6:40~6:49) /test/1/rs_errno/
行为 READ ConfigKey(6:65~6:76) /test/1/rs_message/
------
SQL  CREATE PROCEDURE proc(ofs INT,count INT) BEGIN DECLARE i INT DEFAULT ofs; WHILE i<count DO IF LOWER(CHAR(i USING utf8mb3))<>LOWER(CHAR(i USING utf8mb4)) THEN SELECT i AS 'found funny character'; END IF; SET i=i+1; END WHILE; END;
行为 CREATE Procedure(1:17~1:21) /test/1/catalog1/schema1/proc/
行为 CALL Function(1:94~1:99) /test/1/catalog1/schema1/LOWER/
行为 CALL Function(1:100~1:104) /test/1/catalog1/schema1/CHAR/
------
SQL  CREATE PROCEDURE split_into_mix_84(x CHAR(16), y INT) BEGIN SELECT id, data INTO x, @z FROM split_into_mix.t1 LIMIT 1; END;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/split_into_mix_84/
行为 READ ConfigKey(1:84~1:86) /test/1/z/
行为 READ Table(1:92~1:109) /test/1/catalog1/split_into_mix/t1/
------
SQL  CREATE FUNCTION split_rg_create_84() RETURNS INT BEGIN CREATE RESOURCE GROUP codex_rg_inner TYPE=USER; RETURN 0; END;
行为 CREATE Function(1:16~1:34) /test/1/catalog1/schema1/split_rg_create_84/
行为 CREATE ResourceGroup(1:77~1:91) /test/1/catalog1/schema1/codex_rg_inner/
------
SQL  CREATE FUNCTION split_rg_set_84() RETURNS INT BEGIN SET RESOURCE GROUP USR_default; RETURN 0; END;
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/split_rg_set_84/
行为 ADMIN ResourceGroup(1:71~1:82) /test/1/catalog1/schema1/USR_default/
------
SQL  CREATE FUNCTION split_missing_udf_97 RETURNS STRING SONAME 'split_missing_udf.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_udf_97/
行为 CREATE File(1:59~1:81) /test/1/split_missing_udf.so/
------
SQL  CREATE AGGREGATE FUNCTION split_missing_agg_97 RETURNS REAL SONAME 'split_missing_agg.so';
行为 CREATE Function(1:26~1:46) /test/1/catalog1/schema1/split_missing_agg_97/
行为 CREATE File(1:67~1:89) /test/1/split_missing_agg.so/
------
SQL  CREATE FUNCTION split_missing_int_97 RETURNS INTEGER SONAME 'split_missing_int.so';
行为 CREATE Function(1:16~1:36) /test/1/catalog1/schema1/split_missing_int_97/
行为 CREATE File(1:60~1:82) /test/1/split_missing_int.so/
------
SQL  CREATE FUNCTION split_missing_decimal_97 RETURNS DECIMAL SONAME 'split_missing_decimal.so';
行为 CREATE Function(1:16~1:40) /test/1/catalog1/schema1/split_missing_decimal_97/
行为 CREATE File(1:64~1:90) /test/1/split_missing_decimal.so/
------
SQL  CREATE FUNCTION IF NOT EXISTS split_missing_ifne_97 RETURNS INTEGER SONAME 'split_missing_ifne.so';
行为 CREATE Function(1:30~1:51) /test/1/catalog1/schema1/split_missing_ifne_97/
行为 CREATE File(1:75~1:98) /test/1/split_missing_ifne.so/
------
SQL  CREATE AGGREGATE FUNCTION IF NOT EXISTS split_missing_agg_ifne_97 RETURNS REAL SONAME 'split_missing_ifne.so';
行为 CREATE Function(1:40~1:65) /test/1/catalog1/schema1/split_missing_agg_ifne_97/
行为 CREATE File(1:86~1:109) /test/1/split_missing_ifne.so/
------
SQL  /*!90200 CREATE FUNCTION split_exec_fn(n INTEGER) RETURNS INTEGER LANGUAGE JAVASCRIPT USING (split_exec_lib) AS $$ return split_exec_lib.f(n); $$ */;
行为 CREATE Function(1:25~1:38) /test/1/catalog1/schema1/split_exec_fn/
------
SQL  CREATE FUNCTION split_ext.ext_f(n INTEGER)\nRETURNS INTEGER\nDETERMINISTIC\nNO SQL\nLANGUAGE JAVASCRIPT\nUSING (split_ext.lib1 AS imported1, split_ext.lib2 imported2)\nCOMMENT 'external function'\nAS 'return imported1.f(n) + imported2.g(n)';
行为 CREATE Function(1:16~1:31) /test/1/catalog1/split_ext/ext_f/
------
SQL  CREATE PROCEDURE split_ext.ext_p(n INTEGER)\nUSING (split_ext.lib1, split_ext.lib2 AS imported2)\nLANGUAGE JAVASCRIPT\nCOMMENT 'external procedure'\nAS 'let a = n';
行为 CREATE Procedure(1:17~1:32) /test/1/catalog1/split_ext/ext_p/
------
SQL  CREATE PROCEDURE split_limit97.proc_limit(IN p_offset INT,IN p_count INT)\nBEGIN\n  SELECT id FROM split_limit97.t ORDER BY id LIMIT p_count;\n  SELECT id FROM split_limit97.t ORDER BY id LIMIT p_offset,p_count;\n  SELECT id FROM split_limit97.t ORDER BY id LIMIT p_count OFFSET p_offset;\nEND;
行为 CREATE Procedure(1:17~1:41) /test/1/catalog1/split_limit97/proc_limit/
行为 READ Table(3:17~3:32) /test/1/catalog1/split_limit97/t/
------
SQL  CREATE FUNCTION vector_identity(input_value VECTOR(3))\nRETURNS VECTOR(3)\nDETERMINISTIC\nRETURN input_value;
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/vector_identity/
------
SQL  CREATE PROCEDURE vector_copy(\n  IN input_value VECTOR(3),\n  OUT output_value VECTOR(3),\n  INOUT accumulator VECTOR(3)\n)\nBEGIN\n  DECLARE local_value VECTOR(3);\n  SET local_value=input_value;\n  SET output_value=local_value;\n  SET accumulator=COALESCE(accumulator,local_value);\nEND;
行为 CREATE Procedure(1:17~1:28) /test/1/catalog1/schema1/vector_copy/
行为 CALL Function(10:18~10:26) /test/1/catalog1/schema1/COALESCE/
------
SQL  CREATE PROCEDURE split_into_mix_97(x CHAR(16), y INT) BEGIN SELECT id, data INTO x, @z FROM split_into_mix.t1 LIMIT 1; END;
行为 CREATE Procedure(1:17~1:34) /test/1/catalog1/schema1/split_into_mix_97/
行为 READ ConfigKey(1:84~1:86) /test/1/z/
行为 READ Table(1:92~1:109) /test/1/catalog1/split_into_mix/t1/
------
SQL  CREATE FUNCTION split_rg_create_97() RETURNS INT BEGIN CREATE RESOURCE GROUP codex_rg_inner TYPE=USER; RETURN 0; END;
行为 CREATE Function(1:16~1:34) /test/1/catalog1/schema1/split_rg_create_97/
行为 CREATE ResourceGroup(1:77~1:91) /test/1/catalog1/schema1/codex_rg_inner/
------
SQL  CREATE FUNCTION split_rg_set_97() RETURNS INT BEGIN SET RESOURCE GROUP USR_default; RETURN 0; END;
行为 CREATE Function(1:16~1:31) /test/1/catalog1/schema1/split_rg_set_97/
行为 ADMIN ResourceGroup(1:71~1:82) /test/1/catalog1/schema1/USR_default/

## DROP_PROG_OBJ

SQL  DROP FUNCTION split_missing_udf_56;
行为 DROP Function(1:14~1:34) /test/1/catalog1/schema1/split_missing_udf_56/
------
SQL  DROP FUNCTION IF EXISTS split_missing_udf_56;
行为 DROP Function(1:24~1:44) /test/1/catalog1/schema1/split_missing_udf_56/
------
SQL  DROP PROCEDURE proc_lifecycle_local;
行为 DROP Procedure(1:15~1:35) /test/1/catalog1/schema1/proc_lifecycle_local/
------
SQL  DROP PROCEDURE split_types.proc_lifecycle_qualified;
行为 DROP Procedure(1:15~1:51) /test/1/catalog1/split_types/proc_lifecycle_qualified/
------
SQL  DROP PROCEDURE IF EXISTS split_types.proc_lifecycle_missing;
行为 DROP Procedure(1:25~1:59) /test/1/catalog1/split_types/proc_lifecycle_missing/
------
SQL  DROP FUNCTION fn_lifecycle_local;
行为 DROP Function(1:14~1:32) /test/1/catalog1/schema1/fn_lifecycle_local/
------
SQL  DROP FUNCTION split_types.fn_lifecycle_qualified;
行为 DROP Function(1:14~1:48) /test/1/catalog1/split_types/fn_lifecycle_qualified/
------
SQL  DROP FUNCTION IF EXISTS split_types.fn_lifecycle_missing;
行为 DROP Function(1:24~1:56) /test/1/catalog1/split_types/fn_lifecycle_missing/
------
SQL  DROP FUNCTION split_missing_udf_57;
行为 DROP Function(1:14~1:34) /test/1/catalog1/schema1/split_missing_udf_57/
------
SQL  DROP FUNCTION IF EXISTS split_missing_udf_57;
行为 DROP Function(1:24~1:44) /test/1/catalog1/schema1/split_missing_udf_57/
------
SQL  DROP FUNCTION split_missing_udf_80;
行为 DROP Function(1:14~1:34) /test/1/catalog1/schema1/split_missing_udf_80/
------
SQL  DROP FUNCTION IF EXISTS split_missing_udf_80;
行为 DROP Function(1:24~1:44) /test/1/catalog1/schema1/split_missing_udf_80/
------
SQL  DROP FUNCTION split_missing_udf_84;
行为 DROP Function(1:14~1:34) /test/1/catalog1/schema1/split_missing_udf_84/
------
SQL  DROP FUNCTION IF EXISTS split_missing_udf_84;
行为 DROP Function(1:24~1:44) /test/1/catalog1/schema1/split_missing_udf_84/
------
SQL  DROP PROCEDURE dp84_proc_local;
行为 DROP Procedure(1:15~1:30) /test/1/catalog1/schema1/dp84_proc_local/
------
SQL  DROP PROCEDURE split84.dp84_proc_qualified;
行为 DROP Procedure(1:15~1:42) /test/1/catalog1/split84/dp84_proc_qualified/
------
SQL  DROP PROCEDURE IF EXISTS split84.dp84_proc_missing;
行为 DROP Procedure(1:25~1:50) /test/1/catalog1/split84/dp84_proc_missing/
------
SQL  DROP FUNCTION df84_func_local;
行为 DROP Function(1:14~1:29) /test/1/catalog1/schema1/df84_func_local/
------
SQL  DROP FUNCTION split84.df84_func_qualified;
行为 DROP Function(1:14~1:41) /test/1/catalog1/split84/df84_func_qualified/
------
SQL  DROP FUNCTION IF EXISTS split84.df84_func_missing;
行为 DROP Function(1:24~1:49) /test/1/catalog1/split84/df84_func_missing/
------
SQL  DROP FUNCTION split_missing_udf_97;
行为 DROP Function(1:14~1:34) /test/1/catalog1/schema1/split_missing_udf_97/
------
SQL  DROP FUNCTION IF EXISTS split_missing_udf_97;
行为 DROP Function(1:24~1:44) /test/1/catalog1/schema1/split_missing_udf_97/

## TRANSACTION

SQL  /*!50000 START TRANSACTION */;
行为 TRANSACTION
------
SQL  /*!50000 COMMIT */;
行为 TRANSACTION
------
SQL  COMMIT AND NO CHAIN NO RELEASE;
行为 TRANSACTION
------
SQL  COMMIT RELEASE;
行为 TRANSACTION
------
SQL  COMMIT NO RELEASE;
行为 TRANSACTION
------
SQL  ROLLBACK AND NO CHAIN NO RELEASE;
行为 TRANSACTION
------
SQL  ROLLBACK RELEASE;
行为 TRANSACTION
------
SQL  ROLLBACK NO RELEASE;
行为 TRANSACTION
------
SQL  BEGIN;
行为 TRANSACTION
------
SQL  COMMIT;
行为 TRANSACTION
------
SQL  COMMIT AND CHAIN;
行为 TRANSACTION
------
SQL  ROLLBACK;
行为 TRANSACTION
------
SQL  ROLLBACK AND CHAIN;
行为 TRANSACTION
------
SQL  ROLLBACK TO audit_sp;
行为 TRANSACTION
------
SQL  SET GLOBAL TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
行为 TRANSACTION
------
SQL  SET SESSION TRANSACTION READ WRITE, ISOLATION LEVEL READ COMMITTED;
行为 TRANSACTION
------
SQL  SET TRANSACTION ISOLATION LEVEL SERIALIZABLE, READ ONLY;
行为 TRANSACTION
------
SQL  SET TRANSACTION READ ONLY;
行为 TRANSACTION
------
SQL  START TRANSACTION;
行为 TRANSACTION
------
SQL  START TRANSACTION READ ONLY;
行为 TRANSACTION
------
SQL  START TRANSACTION READ ONLY, WITH CONSISTENT SNAPSHOT;
行为 TRANSACTION
------
SQL  START TRANSACTION READ ONLY, WITH CONSISTENT SNAPSHOT, READ ONLY, WITH CONSISTENT SNAPSHOT;
行为 TRANSACTION
------
SQL  SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
行为 TRANSACTION
------
SQL  SET TRANSACTION ISOLATION LEVEL REPEATABLE READ, READ WRITE;
行为 TRANSACTION
------
SQL  START TRANSACTION WITH CONSISTENT SNAPSHOT, READ WRITE;
行为 TRANSACTION
------
SQL  SAVEPOINT sp_tx1;
行为 TRANSACTION
------
SQL  ROLLBACK WORK TO SAVEPOINT sp_tx1;
行为 TRANSACTION
------
SQL  RELEASE SAVEPOINT sp_tx1;
行为 TRANSACTION
------
SQL  COMMIT WORK AND NO CHAIN NO RELEASE;
行为 TRANSACTION
------
SQL  COMMIT WORK AND NO CHAIN RELEASE;
行为 TRANSACTION
------
SQL  BEGIN WORK;
行为 TRANSACTION
------
SQL  ROLLBACK WORK AND NO CHAIN NO RELEASE;
行为 TRANSACTION
------
SQL  ROLLBACK WORK AND NO CHAIN RELEASE;
行为 TRANSACTION
------
SQL  XA START 'xa_tx1', 'bq', 42;
行为 TRANSACTION
------
SQL  XA END 'xa_tx1', 'bq', 42;
行为 TRANSACTION
------
SQL  XA PREPARE 'xa_tx1', 'bq', 42;
行为 TRANSACTION
------
SQL  XA COMMIT 'xa_tx1', 'bq', 42;
行为 TRANSACTION
------
SQL  XA START 'xa_tx4';
行为 TRANSACTION
------
SQL  XA END 'xa_tx4';
行为 TRANSACTION
------
SQL  XA ROLLBACK 'xa_tx4';
行为 TRANSACTION
------
SQL  XA BEGIN 'xa_tx5';
行为 TRANSACTION
------
SQL  XA END 'xa_tx5';
行为 TRANSACTION
------
SQL  XA ROLLBACK 'xa_tx5';
行为 TRANSACTION
------
SQL  XA START 'xa_tx3';
行为 TRANSACTION
------
SQL  XA END 'xa_tx3';
行为 TRANSACTION
------
SQL  XA COMMIT 'xa_tx3' ONE PHASE;
行为 TRANSACTION
------
SQL  XA RECOVER;
行为 TRANSACTION
------
SQL  XA START 'split_xa_join' JOIN;
行为 TRANSACTION
------
SQL  XA START 'split_xa_resume' RESUME;
行为 TRANSACTION
------
SQL  XA END 'split_xa_suspend' SUSPEND;
行为 TRANSACTION
------
SQL  XA END 'split_xa_migrate' SUSPEND FOR MIGRATE;
行为 TRANSACTION
------
SQL  XA START X'61756469745F67', B'01100001';
行为 TRANSACTION
------
SQL  XA START 0x61756469745F67, 0x62;
行为 TRANSACTION
------
SQL  XA START 'audit_g','audit_b';
行为 TRANSACTION
------
SQL  SET GLOBAL TRANSACTION READ ONLY;
行为 TRANSACTION
------
SQL  ROLLBACK WORK AND CHAIN NO RELEASE;
行为 TRANSACTION
------
SQL  XA START 0x7465737462, 0x2030405060, 0xb;
行为 TRANSACTION
------
SQL  XA START'','';
行为 TRANSACTION
------
SQL  SAVEPOINT `my_savepoint`;
行为 TRANSACTION
------
SQL  SAVEPOINT commit;
行为 TRANSACTION
------
SQL  SAVEPOINT library;
行为 TRANSACTION
------
SQL  COMMIT WORK;
行为 TRANSACTION
------
SQL  ROLLBACK WORK;
行为 TRANSACTION
------
SQL  START TRANSACTION READ WRITE;
行为 TRANSACTION
------
SQL  START TRANSACTION READ WRITE, WITH CONSISTENT SNAPSHOT;
行为 TRANSACTION
------
SQL  START TRANSACTION WITH CONSISTENT SNAPSHOT, READ ONLY;
行为 TRANSACTION
------
SQL  ROLLBACK TO SAVEPOINT sp1;
行为 TRANSACTION
------
SQL  XA END 0x636f646578, 0x676170, 0x2a;
行为 TRANSACTION
------
SQL  XA PREPARE 0x636f646578, 0x676170, 0x2a;
行为 TRANSACTION
------
SQL  XA ROLLBACK 0x636f646578, 0x676170, 0x2a;
行为 TRANSACTION
------
SQL  SET TRANSACTION READ ONLY, ISOLATION LEVEL READ COMMITTED;
行为 TRANSACTION
------
SQL  COMMIT AND NO CHAIN;
行为 TRANSACTION
------
SQL  ROLLBACK AND NO CHAIN;
行为 TRANSACTION
------
SQL  XA START "xa1";
行为 TRANSACTION
------
SQL  XA RECOVER CONVERT XID;
行为 TRANSACTION

## ADMIN_REPLICATION

SQL  BINLOG 'U2EZYw8CAAAAdwAAAHsAAAAAAAQANS43LjM2LWxvZwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEzgNAAgAEgAEBAQEEgAAXwAEGggAAAAICAgCAAAACgoKKioAEjQAAYA9mSk=';
行为 ADMIN Log(1:0~1:169) /test/1/
------
SQL  BINLOG '\nSVtYRxMBAAAAKQAAADQBAAAAABAAAAAAAAAABHRlc3QAAnQxAAEDAAE=\nSVtYRxcBAAAAIgAAAFYBAAAQABAAAAAAAAEAAf/+AgAAAA==\n';
行为 ADMIN Log(1:0~4:1) /test/1/

## METADATA

SQL  SHOW SLAVE STATUS;
行为 READ Instance(1:0~1:17) /test/1/
------
SQL  SHOW TABLE STATUS;
行为 READ Instance(1:0~1:17) /test/1/
------
SQL  SHOW TABLE STATUS LIKE 'status\_%';
行为 READ Instance(1:0~1:34) /test/1/
------
SQL  SHOW TABLE STATUS FROM split_show_status256 LIKE 'status_t';
行为 READ Instance(1:0~1:59) /test/1/
------
SQL  SHOW TABLE STATUS IN split_show_status256 WHERE Name='status_v';
行为 READ Instance(1:0~1:63) /test/1/
------
SQL  DESCRIBE splitv56.desc_t;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv56/desc_t/
------
SQL  DESC splitv56.desc_t id;
行为 READ Table(1:5~1:20) /test/1/catalog1/splitv56/desc_t/
------
SQL  DESCRIBE splitv56.desc_t 'v%';
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv56/desc_t/
------
SQL  DESCRIBE splitv56.desc_v;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv56/desc_v/
------
SQL  HELP 'contents';
行为 READ Instance(1:0~1:15) /test/1/
------
SQL  HELP 'data types';
行为 READ Instance(1:0~1:17) /test/1/
------
SQL  HELP 'ascii';
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  HELP 'create table';
行为 READ Instance(1:0~1:19) /test/1/
------
SQL  HELP 'rep%';
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  HELP 'fake';
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  HELP no_such_topic;
行为 READ Instance(1:0~1:18) /test/1/
------
SQL  SHOW SLAVE HOSTS;
行为 READ Instance(1:0~1:16) /test/1/
------
SQL  SHOW DATABASES;
行为 READ Instance(1:0~1:14) /test/1/
------
SQL  SHOW DATABASES WHERE `Database` LIKE 'mysql';
行为 READ Instance(1:0~1:44) /test/1/
------
SQL  SHOW SCHEMAS;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  SHOW SCHEMAS LIKE 'mysql';
行为 READ Instance(1:0~1:25) /test/1/
------
SQL  SHOW FUNCTION STATUS;
行为 READ Instance(1:0~1:20) /test/1/
------
SQL  SHOW PROCEDURE STATUS;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  SHOW CHARACTER SET;
行为 READ Instance(1:0~1:18) /test/1/
------
SQL  SHOW CHARACTER SET WHERE Charset LIKE 'utf8%';
行为 READ Instance(1:0~1:45) /test/1/
------
SQL  SHOW CHARSET;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  SHOW CHARSET LIKE 'utf8%';
行为 READ Instance(1:0~1:25) /test/1/
------
SQL  SHOW CHARSET WHERE Charset LIKE 'utf8%';
行为 READ Instance(1:0~1:39) /test/1/
------
SQL  SHOW COLLATION;
行为 READ Instance(1:0~1:14) /test/1/
------
SQL  SHOW COLLATION LIKE 'utf8%';
行为 READ Instance(1:0~1:27) /test/1/
------
SQL  SHOW VARIABLES;
行为 READ Instance(1:0~1:14) /test/1/
------
SQL  SHOW VARIABLES WHERE Variable_name LIKE 'version%';
行为 READ Instance(1:0~1:50) /test/1/
------
SQL  SHOW GLOBAL VARIABLES;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  SHOW GLOBAL VARIABLES LIKE 'version%';
行为 READ Instance(1:0~1:37) /test/1/
------
SQL  SHOW GLOBAL VARIABLES WHERE Variable_name LIKE 'version%';
行为 READ Instance(1:0~1:57) /test/1/
------
SQL  SHOW SESSION VARIABLES;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  SHOW SESSION VARIABLES WHERE Variable_name LIKE 'version%';
行为 READ Instance(1:0~1:58) /test/1/
------
SQL  SHOW CREATE DATABASE split_show_create56;
行为 READ Schema(1:21~1:40) /test/1/catalog1/split_show_create56/
------
SQL  SHOW CREATE SCHEMA IF NOT EXISTS split_show_create56;
行为 READ Schema(1:33~1:52) /test/1/catalog1/split_show_create56/
------
SQL  SHOW CREATE TABLE split_show_create56.t;
行为 READ Table(1:18~1:39) /test/1/catalog1/split_show_create56/t/
------
SQL  SHOW CREATE VIEW split_show_create56.v;
行为 READ View(1:17~1:38) /test/1/catalog1/split_show_create56/v/
------
SQL  SHOW CREATE FUNCTION split_show_create56.f;
行为 READ Function(1:21~1:42) /test/1/catalog1/split_show_create56/f/
------
SQL  SHOW CREATE PROCEDURE split_show_create56.p;
行为 READ Procedure(1:22~1:43) /test/1/catalog1/split_show_create56/p/
------
SQL  SHOW CREATE TRIGGER split_show_create56.trg;
行为 READ Trigger(1:20~1:43) /test/1/catalog1/split_show_create56/trg/
------
SQL  SHOW CREATE EVENT split_show_create56.e;
行为 READ Event(1:18~1:39) /test/1/catalog1/split_show_create56/e/
------
SQL  SHOW FULL COLUMNS IN maint_a IN utility_audit WHERE Field = 'id';
行为 READ Table(1:21~1:28) /test/1/catalog1/utility_audit/maint_a/
------
SQL  SHOW FULL TABLES IN utility_audit LIKE 'maint%';
行为 READ Instance(1:0~1:47) /test/1/
------
SQL  SHOW FULL TRIGGERS FROM utility_audit LIKE 'trg%';
行为 READ Instance(1:0~1:49) /test/1/
------
SQL  SHOW GRANTS;
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  SHOW GRANTS FOR CURRENT_USER;
行为 READ Instance(1:0~1:28) /test/1/
------
SQL  SHOW GRANTS FOR CURRENT_USER();
行为 READ Instance(1:0~1:30) /test/1/
------
SQL  SHOW GRANTS FOR 'root'@'localhost';
行为 READ Instance(1:0~1:34) /test/1/
------
SQL  SHOW INDEX IN maint_a IN utility_audit WHERE Key_name = 'PRIMARY';
行为 READ Table(1:14~1:21) /test/1/catalog1/utility_audit/maint_a/
------
SQL  SHOW DATABASES LIKE 'split\_%';
行为 READ Instance(1:0~1:30) /test/1/
------
SQL  SHOW SCHEMAS WHERE `Database` LIKE 'split%';
行为 READ Instance(1:0~1:43) /test/1/
------
SQL  SHOW TABLES;
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  SHOW FULL TABLES;
行为 READ Instance(1:0~1:16) /test/1/
------
SQL  SHOW FULL TABLES FROM split_show56 WHERE Table_type = 'BASE TABLE';
行为 READ Schema(1:22~1:34) /test/1/catalog1/split_show56/
------
SQL  SHOW COLUMNS FROM show_t;
行为 READ Table(1:18~1:24) /test/1/catalog1/schema1/show_t/
------
SQL  SHOW FULL FIELDS FROM show_t FROM split_show56 LIKE 'n%';
行为 READ Table(1:22~1:28) /test/1/catalog1/split_show56/show_t/
------
SQL  SHOW INDEX FROM show_t;
行为 READ Table(1:16~1:22) /test/1/catalog1/schema1/show_t/
------
SQL  SHOW KEYS FROM show_t FROM split_show56 WHERE Key_name = 'idx_name';
行为 READ Table(1:15~1:21) /test/1/catalog1/split_show56/show_t/
------
SQL  SHOW PROCEDURE CODE split_accept.p_accept;
行为 READ Instance(1:0~1:41) /test/1/
------
SQL  SHOW FUNCTION CODE split_accept.f_accept;
行为 READ Instance(1:0~1:40) /test/1/
------
SQL  SHOW EVENTS;
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  SHOW EVENTS FROM split_show_status56 LIKE 'e%';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW EVENTS IN split_show_status56 WHERE Name='e_status';
行为 READ Instance(1:0~1:56) /test/1/
------
SQL  SHOW TRIGGERS;
行为 READ Instance(1:0~1:13) /test/1/
------
SQL  SHOW TRIGGERS FROM split_show_status56 LIKE 'rt%';
行为 READ Instance(1:0~1:49) /test/1/
------
SQL  SHOW TRIGGERS IN split_show_status56 WHERE `Table`='rt';
行为 READ Instance(1:0~1:55) /test/1/
------
SQL  SHOW FUNCTION STATUS LIKE 'f_status';
行为 READ Instance(1:0~1:36) /test/1/
------
SQL  SHOW FUNCTION STATUS WHERE Db='split_show_status56';
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW PROCEDURE STATUS LIKE 'p_status';
行为 READ Instance(1:0~1:37) /test/1/
------
SQL  SHOW PROCEDURE STATUS WHERE Db='split_show_status56';
行为 READ Instance(1:0~1:52) /test/1/
------
SQL  SHOW VARIABLES LIKE 'version%';
行为 READ Instance(1:0~1:30) /test/1/
------
SQL  SHOW SESSION VARIABLES LIKE 'character_set%';
行为 READ Instance(1:0~1:44) /test/1/
------
SQL  SHOW ENGINES;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  SHOW STORAGE ENGINES;
行为 READ Instance(1:0~1:20) /test/1/
------
SQL  SHOW PLUGINS;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  SHOW PRIVILEGES;
行为 READ Instance(1:0~1:15) /test/1/
------
SQL  SHOW CHARACTER SET LIKE 'utf8%';
行为 READ Instance(1:0~1:31) /test/1/
------
SQL  SHOW COLLATION WHERE Charset='utf8mb4';
行为 READ Instance(1:0~1:38) /test/1/
------
SQL  SHOW LOCAL VARIABLES;
行为 READ Instance(1:0~1:20) /test/1/
------
SQL  SHOW LOCAL VARIABLES LIKE 'sql_mode';
行为 READ Instance(1:0~1:36) /test/1/
------
SQL  SHOW LOCAL VARIABLES WHERE Variable_name='sql_mode';
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW INDEXES FROM base_t FROM codex_next_audit;
行为 READ Table(1:18~1:24) /test/1/catalog1/codex_next_audit/base_t/
------
SQL  SELECT table_name FROM information_schema.tables LIMIT 1;
行为 READ Table(1:23~1:48) /test/1/catalog1/information_schema/tables/
------
SQL  DESCRIBE json_op;
行为 READ Table(1:9~1:16) /test/1/catalog1/schema1/json_op/
------
SQL  DESC json_op j;
行为 READ Table(1:5~1:12) /test/1/catalog1/schema1/json_op/
------
SQL  HELP 'SELECT';
行为 READ Instance(1:0~1:13) /test/1/
------
SQL  SHOW SLAVE STATUS FOR CHANNEL 'legacy80';
行为 READ Instance(1:0~1:40) /test/1/
------
SQL  SHOW TABLE STATUS FROM split_show_status257 LIKE 'status_t';
行为 READ Instance(1:0~1:59) /test/1/
------
SQL  SHOW TABLE STATUS IN split_show_status257 WHERE Name='status_v';
行为 READ Instance(1:0~1:63) /test/1/
------
SQL  DESCRIBE splitv57.desc_t;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv57/desc_t/
------
SQL  DESC splitv57.desc_t id;
行为 READ Table(1:5~1:20) /test/1/catalog1/splitv57/desc_t/
------
SQL  DESCRIBE splitv57.desc_t 'v%';
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv57/desc_t/
------
SQL  DESCRIBE splitv57.desc_v;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv57/desc_v/
------
SQL  SHOW SLAVE STATUS FOR CHANNEL 'split_chan';
行为 READ Instance(1:0~1:42) /test/1/
------
SQL  SHOW CREATE DATABASE split_show_create57;
行为 READ Schema(1:21~1:40) /test/1/catalog1/split_show_create57/
------
SQL  SHOW CREATE SCHEMA IF NOT EXISTS split_show_create57;
行为 READ Schema(1:33~1:52) /test/1/catalog1/split_show_create57/
------
SQL  SHOW CREATE TABLE split_show_create57.t;
行为 READ Table(1:18~1:39) /test/1/catalog1/split_show_create57/t/
------
SQL  SHOW CREATE VIEW split_show_create57.v;
行为 READ View(1:17~1:38) /test/1/catalog1/split_show_create57/v/
------
SQL  SHOW CREATE FUNCTION split_show_create57.f;
行为 READ Function(1:21~1:42) /test/1/catalog1/split_show_create57/f/
------
SQL  SHOW CREATE PROCEDURE split_show_create57.p;
行为 READ Procedure(1:22~1:43) /test/1/catalog1/split_show_create57/p/
------
SQL  SHOW CREATE TRIGGER split_show_create57.trg;
行为 READ Trigger(1:20~1:43) /test/1/catalog1/split_show_create57/trg/
------
SQL  SHOW CREATE EVENT split_show_create57.e;
行为 READ Event(1:18~1:39) /test/1/catalog1/split_show_create57/e/
------
SQL  SHOW CREATE USER 'split_show_user'@'localhost';
行为 READ User(1:17~1:46) /test/1/split_show_user@localhost/
------
SQL  SHOW CREATE USER CURRENT_USER;
行为 READ User(1:0~1:29) /test/1/
------
SQL  SHOW CREATE USER CURRENT_USER();
行为 READ User(1:0~1:31) /test/1/
------
SQL  SHOW FULL TABLES FROM split_show57 WHERE Table_type = 'BASE TABLE';
行为 READ Schema(1:22~1:34) /test/1/catalog1/split_show57/
------
SQL  SHOW FULL FIELDS FROM show_t FROM split_show57 LIKE 'n%';
行为 READ Table(1:22~1:28) /test/1/catalog1/split_show57/show_t/
------
SQL  SHOW KEYS FROM show_t FROM split_show57 WHERE Key_name = 'idx_name';
行为 READ Table(1:15~1:21) /test/1/catalog1/split_show57/show_t/
------
SQL  SHOW EVENTS FROM split_show_status57 LIKE 'e%';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW EVENTS IN split_show_status57 WHERE Name='e_status';
行为 READ Instance(1:0~1:56) /test/1/
------
SQL  SHOW TRIGGERS FROM split_show_status57 LIKE 'rt%';
行为 READ Instance(1:0~1:49) /test/1/
------
SQL  SHOW TRIGGERS IN split_show_status57 WHERE `Table`='rt';
行为 READ Instance(1:0~1:55) /test/1/
------
SQL  SHOW FUNCTION STATUS WHERE Db='split_show_status57';
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW PROCEDURE STATUS WHERE Db='split_show_status57';
行为 READ Instance(1:0~1:52) /test/1/
------
SQL  select * from information_schema.columns
行为 READ Table(1:14~1:40) /test/1/catalog1/information_schema/columns/
------
SQL  show columns from test;
行为 READ Table(1:18~1:22) /test/1/catalog1/schema1/test/
------
SQL  show create table test;
行为 READ Table(1:18~1:22) /test/1/catalog1/schema1/test/
------
SQL  show index from test;
行为 READ Table(1:16~1:20) /test/1/catalog1/schema1/test/
------
SQL  SELECT STATEMENT_DIGEST(REPEAT('a',character_maximum_length)) IS NULL FROM information_schema.columns WHERE table_name='events_statements_history' AND column_name='digest';
行为 CALL Function(1:7~1:23) /test/1/catalog1/schema1/STATEMENT_DIGEST/
行为 CALL Function(1:24~1:30) /test/1/catalog1/schema1/REPEAT/
行为 READ Table(1:75~1:101) /test/1/catalog1/information_schema/columns/
------
SQL  SHOW TABLE STATUS FROM split_show_status280 LIKE 'status_t';
行为 READ Instance(1:0~1:59) /test/1/
------
SQL  SHOW TABLE STATUS IN split_show_status280 WHERE Name='status_v';
行为 READ Instance(1:0~1:63) /test/1/
------
SQL  DESCRIBE splitv80.desc_t;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv80/desc_t/
------
SQL  DESC splitv80.desc_t id;
行为 READ Table(1:5~1:20) /test/1/catalog1/splitv80/desc_t/
------
SQL  DESCRIBE splitv80.desc_t 'v%';
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv80/desc_t/
------
SQL  DESCRIBE splitv80.desc_v;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv80/desc_v/
------
SQL  SHOW REPLICAS;
行为 READ Instance(1:0~1:13) /test/1/
------
SQL  SHOW REPLICA STATUS;
行为 READ Instance(1:0~1:19) /test/1/
------
SQL  SHOW REPLICA STATUS FOR CHANNEL 'audit_replica';
行为 READ Instance(1:0~1:47) /test/1/
------
SQL  SHOW CREATE DATABASE split_show_create80;
行为 READ Schema(1:21~1:40) /test/1/catalog1/split_show_create80/
------
SQL  SHOW CREATE SCHEMA IF NOT EXISTS split_show_create80;
行为 READ Schema(1:33~1:52) /test/1/catalog1/split_show_create80/
------
SQL  SHOW CREATE TABLE split_show_create80.t;
行为 READ Table(1:18~1:39) /test/1/catalog1/split_show_create80/t/
------
SQL  SHOW CREATE VIEW split_show_create80.v;
行为 READ View(1:17~1:38) /test/1/catalog1/split_show_create80/v/
------
SQL  SHOW CREATE FUNCTION split_show_create80.f;
行为 READ Function(1:21~1:42) /test/1/catalog1/split_show_create80/f/
------
SQL  SHOW CREATE PROCEDURE split_show_create80.p;
行为 READ Procedure(1:22~1:43) /test/1/catalog1/split_show_create80/p/
------
SQL  SHOW CREATE TRIGGER split_show_create80.trg;
行为 READ Trigger(1:20~1:43) /test/1/catalog1/split_show_create80/trg/
------
SQL  SHOW CREATE EVENT split_show_create80.e;
行为 READ Event(1:18~1:39) /test/1/catalog1/split_show_create80/e/
------
SQL  SHOW GRANTS FOR 'split_show_grants'@'%' USING 'split_show_role_a';
行为 READ Instance(1:0~1:65) /test/1/
------
SQL  SHOW GRANTS FOR 'split_show_grants'@'%' USING 'split_show_role_a', 'split_show_role_b';
行为 READ Instance(1:0~1:86) /test/1/
------
SQL  SHOW GRANTS FOR 'split_show_role_a';
行为 READ Instance(1:0~1:35) /test/1/
------
SQL  SHOW FULL TABLES FROM split_show80 WHERE Table_type = 'BASE TABLE';
行为 READ Schema(1:22~1:34) /test/1/catalog1/split_show80/
------
SQL  SHOW FULL FIELDS FROM show_t FROM split_show80 LIKE 'n%';
行为 READ Table(1:22~1:28) /test/1/catalog1/split_show80/show_t/
------
SQL  SHOW KEYS FROM show_t FROM split_show80 WHERE Key_name = 'idx_name';
行为 READ Table(1:15~1:21) /test/1/catalog1/split_show80/show_t/
------
SQL  SHOW EXTENDED INDEX FROM codex_mgmt4.show_t;
行为 READ Table(1:25~1:43) /test/1/catalog1/codex_mgmt4/show_t/
------
SQL  SHOW EXTENDED KEYS FROM codex_mgmt4.show_t;
行为 READ Table(1:24~1:42) /test/1/catalog1/codex_mgmt4/show_t/
------
SQL  SHOW EXTENDED COLUMNS FROM codex_mgmt4.show_t;
行为 READ Table(1:27~1:45) /test/1/catalog1/codex_mgmt4/show_t/
------
SQL  SHOW EVENTS FROM split_show_status80 LIKE 'e%';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW EVENTS IN split_show_status80 WHERE Name='e_status';
行为 READ Instance(1:0~1:56) /test/1/
------
SQL  SHOW TRIGGERS FROM split_show_status80 LIKE 'rt%';
行为 READ Instance(1:0~1:49) /test/1/
------
SQL  SHOW TRIGGERS IN split_show_status80 WHERE `Table`='rt';
行为 READ Instance(1:0~1:55) /test/1/
------
SQL  SHOW FUNCTION STATUS WHERE Db='split_show_status80';
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW PROCEDURE STATUS WHERE Db='split_show_status80';
行为 READ Instance(1:0~1:52) /test/1/
------
SQL  show character set;
行为 READ Instance(1:0~1:18) /test/1/
------
SQL  show character set like 'abc%';
行为 READ Instance(1:0~1:30) /test/1/
------
SQL  show charset;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  show charset like 'abc%';
行为 READ Instance(1:0~1:24) /test/1/
------
SQL  show collation;
行为 READ Instance(1:0~1:14) /test/1/
------
SQL  show collation like 'abc%';
行为 READ Instance(1:0~1:26) /test/1/
------
SQL  show columns from test_table from test_db1;
行为 READ Table(1:18~1:28) /test/1/catalog1/test_db1/test_table/
------
SQL  show columns from test_db1.test_table;
行为 READ Table(1:18~1:37) /test/1/catalog1/test_db1/test_table/
------
SQL  show columns from test_table;
行为 READ Table(1:18~1:28) /test/1/catalog1/schema1/test_table/
------
SQL  show full columns from test_table from test_db like 'abc%';
行为 READ Table(1:23~1:33) /test/1/catalog1/test_db/test_table/
------
SQL  desc test_table;
行为 READ Table(1:5~1:15) /test/1/catalog1/schema1/test_table/
------
SQL  desc test_db.test_table;
行为 READ Table(1:5~1:23) /test/1/catalog1/test_db/test_table/
------
SQL  show create database test_db1;
行为 READ Schema(1:21~1:29) /test/1/catalog1/test_db1/
------
SQL  show create event test_db.test_event;
行为 READ Event(1:18~1:36) /test/1/catalog1/test_db/test_event/
------
SQL  show create event test_event;
行为 READ Event(1:18~1:28) /test/1/catalog1/schema1/test_event/
------
SQL  show create function test_db.test_func;
行为 READ Function(1:21~1:38) /test/1/catalog1/test_db/test_func/
------
SQL  show create function test_func;
行为 READ Function(1:21~1:30) /test/1/catalog1/schema1/test_func/
------
SQL  show create procedure test_db.test_proc;
行为 READ Procedure(1:22~1:39) /test/1/catalog1/test_db/test_proc/
------
SQL  show create procedure test_proc;
行为 READ Procedure(1:22~1:31) /test/1/catalog1/schema1/test_proc/
------
SQL  show create table test_db.test_table;
行为 READ Table(1:18~1:36) /test/1/catalog1/test_db/test_table/
------
SQL  show create table test_table;
行为 READ Table(1:18~1:28) /test/1/catalog1/schema1/test_table/
------
SQL  show create view test_db.test_view;
行为 READ View(1:17~1:34) /test/1/catalog1/test_db/test_view/
------
SQL  show create view test_view;
行为 READ View(1:17~1:26) /test/1/catalog1/schema1/test_view/
------
SQL  show create trigger test_db.test_trigger;
行为 READ Trigger(1:20~1:40) /test/1/catalog1/test_db/test_trigger/
------
SQL  show create trigger test_trigger;
行为 READ Trigger(1:20~1:32) /test/1/catalog1/schema1/test_trigger/
------
SQL  show databases;
行为 READ Instance(1:0~1:14) /test/1/
------
SQL  show schemas;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  show triggers in test_db;
行为 READ Instance(1:0~1:24) /test/1/
------
SQL  show events in test_db;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  show variables;
行为 READ Instance(1:0~1:14) /test/1/
------
SQL  show global variables;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  show session variables;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  show variables like 'abc%';
行为 READ Instance(1:0~1:26) /test/1/
------
SQL  show tables in test_db;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  show full tables in test_db2;
行为 READ Schema(1:20~1:28) /test/1/catalog1/test_db2/
------
SQL  show full tables from test_db2;
行为 READ Schema(1:22~1:30) /test/1/catalog1/test_db2/
------
SQL  show full tables from test_db2 where Table_type = 'BASE TABLE';
行为 READ Schema(1:22~1:30) /test/1/catalog1/test_db2/
------
SQL  show full tables from test_db2 like 'BASE TABLE';
行为 READ Instance(1:0~1:48) /test/1/
------
SQL  show indexes from test_table from test_db;
行为 READ Table(1:18~1:28) /test/1/catalog1/test_db/test_table/
------
SQL  show indexes from test_db.test_table;
行为 READ Table(1:18~1:36) /test/1/catalog1/test_db/test_table/
------
SQL  show indexes from test_table;
行为 READ Table(1:18~1:28) /test/1/catalog1/schema1/test_table/
------
SQL  show function status;
行为 READ Instance(1:0~1:20) /test/1/
------
SQL  show function status like 'abc%';
行为 READ Instance(1:0~1:32) /test/1/
------
SQL  show procedure status;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  show procedure status like 'abc%';
行为 READ Instance(1:0~1:33) /test/1/
------
SQL  show table status;
行为 READ Instance(1:0~1:17) /test/1/
------
SQL  show table status in test_db;
行为 READ Instance(1:0~1:28) /test/1/
------
SQL  SHOW GRANTS FOR CURRENT_USER() USING r1;
行为 READ Instance(1:0~1:39) /test/1/
------
SQL  SHOW GRANTS FOR PUBLIC;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  SHOW GRANTS FOR 'codex_gap_user'@'localhost' USING PUBLIC;
行为 READ Instance(1:0~1:57) /test/1/
------
SQL  SHOW EXTENDED FULL COLUMNS FROM codex_recheck.show_t;
行为 READ Table(1:32~1:52) /test/1/catalog1/codex_recheck/show_t/
------
SQL  WITH metadata_rows AS (SELECT table_name FROM information_schema.tables LIMIT 1) SELECT * FROM metadata_rows;
行为 READ Table(1:46~1:71) /test/1/catalog1/information_schema/tables/
------
SQL  SELECT * FROM information_schema.columns;
行为 READ Table(1:14~1:40) /test/1/catalog1/information_schema/columns/
------
SQL  SHOW REPLICA STATUS FOR CHANNEL 'chan84';
行为 READ Instance(1:0~1:40) /test/1/
------
SQL  SHOW TABLE STATUS FROM split_show_status284 LIKE 'status_t';
行为 READ Instance(1:0~1:59) /test/1/
------
SQL  SHOW TABLE STATUS IN split_show_status284 WHERE Name='status_v';
行为 READ Instance(1:0~1:63) /test/1/
------
SQL  DESCRIBE splitv84.desc_t;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv84/desc_t/
------
SQL  DESC splitv84.desc_t id;
行为 READ Table(1:5~1:20) /test/1/catalog1/splitv84/desc_t/
------
SQL  DESCRIBE splitv84.desc_t 'v%';
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv84/desc_t/
------
SQL  DESCRIBE splitv84.desc_v;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv84/desc_v/
------
SQL  SHOW CREATE DATABASE split_show_create84;
行为 READ Schema(1:21~1:40) /test/1/catalog1/split_show_create84/
------
SQL  SHOW CREATE SCHEMA IF NOT EXISTS split_show_create84;
行为 READ Schema(1:33~1:52) /test/1/catalog1/split_show_create84/
------
SQL  SHOW CREATE TABLE split_show_create84.t;
行为 READ Table(1:18~1:39) /test/1/catalog1/split_show_create84/t/
------
SQL  SHOW CREATE VIEW split_show_create84.v;
行为 READ View(1:17~1:38) /test/1/catalog1/split_show_create84/v/
------
SQL  SHOW CREATE FUNCTION split_show_create84.f;
行为 READ Function(1:21~1:42) /test/1/catalog1/split_show_create84/f/
------
SQL  SHOW CREATE PROCEDURE split_show_create84.p;
行为 READ Procedure(1:22~1:43) /test/1/catalog1/split_show_create84/p/
------
SQL  SHOW CREATE TRIGGER split_show_create84.trg;
行为 READ Trigger(1:20~1:43) /test/1/catalog1/split_show_create84/trg/
------
SQL  SHOW CREATE EVENT split_show_create84.e;
行为 READ Event(1:18~1:39) /test/1/catalog1/split_show_create84/e/
------
SQL  SHOW FULL TABLES FROM split_show84 WHERE Table_type = 'BASE TABLE';
行为 READ Schema(1:22~1:34) /test/1/catalog1/split_show84/
------
SQL  SHOW FULL FIELDS FROM show_t FROM split_show84 LIKE 'n%';
行为 READ Table(1:22~1:28) /test/1/catalog1/split_show84/show_t/
------
SQL  SHOW KEYS FROM show_t FROM split_show84 WHERE Key_name = 'idx_name';
行为 READ Table(1:15~1:21) /test/1/catalog1/split_show84/show_t/
------
SQL  SHOW EVENTS FROM split_show_status84 LIKE 'e%';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW EVENTS IN split_show_status84 WHERE Name='e_status';
行为 READ Instance(1:0~1:56) /test/1/
------
SQL  SHOW TRIGGERS FROM split_show_status84 LIKE 'rt%';
行为 READ Instance(1:0~1:49) /test/1/
------
SQL  SHOW TRIGGERS IN split_show_status84 WHERE `Table`='rt';
行为 READ Instance(1:0~1:55) /test/1/
------
SQL  SHOW FUNCTION STATUS WHERE Db='split_show_status84';
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW PROCEDURE STATUS WHERE Db='split_show_status84';
行为 READ Instance(1:0~1:52) /test/1/
------
SQL  SHOW CREATE MASKING POLICY split_accept_mask;
行为 READ Instance(1:0~1:44) /test/1/
------
SQL  SHOW TABLE STATUS FROM split_show_status297 LIKE 'status_t';
行为 READ Instance(1:0~1:59) /test/1/
------
SQL  SHOW TABLE STATUS IN split_show_status297 WHERE Name='status_v';
行为 READ Instance(1:0~1:63) /test/1/
------
SQL  DESCRIBE splitv97.desc_t;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv97/desc_t/
------
SQL  DESC splitv97.desc_t id;
行为 READ Table(1:5~1:20) /test/1/catalog1/splitv97/desc_t/
------
SQL  DESCRIBE splitv97.desc_t 'v%';
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv97/desc_t/
------
SQL  DESCRIBE splitv97.desc_v;
行为 READ Table(1:9~1:24) /test/1/catalog1/splitv97/desc_v/
------
SQL  SHOW CREATE DATABASE split_show_create97;
行为 READ Schema(1:21~1:40) /test/1/catalog1/split_show_create97/
------
SQL  SHOW CREATE SCHEMA IF NOT EXISTS split_show_create97;
行为 READ Schema(1:33~1:52) /test/1/catalog1/split_show_create97/
------
SQL  SHOW CREATE TABLE split_show_create97.t;
行为 READ Table(1:18~1:39) /test/1/catalog1/split_show_create97/t/
------
SQL  SHOW CREATE VIEW split_show_create97.v;
行为 READ View(1:17~1:38) /test/1/catalog1/split_show_create97/v/
------
SQL  SHOW CREATE FUNCTION split_show_create97.f;
行为 READ Function(1:21~1:42) /test/1/catalog1/split_show_create97/f/
------
SQL  SHOW CREATE PROCEDURE split_show_create97.p;
行为 READ Procedure(1:22~1:43) /test/1/catalog1/split_show_create97/p/
------
SQL  SHOW CREATE TRIGGER split_show_create97.trg;
行为 READ Trigger(1:20~1:43) /test/1/catalog1/split_show_create97/trg/
------
SQL  SHOW CREATE EVENT split_show_create97.e;
行为 READ Event(1:18~1:39) /test/1/catalog1/split_show_create97/e/
------
SQL  SHOW LIBRARY STATUS;
行为 READ Instance(1:0~1:19) /test/1/
------
SQL  SHOW LIBRARY STATUS LIKE 'split_js%';
行为 READ Instance(1:0~1:36) /test/1/
------
SQL  SHOW LIBRARY STATUS WHERE Db = DATABASE();
行为 READ Instance(1:0~1:41) /test/1/
行为 CALL Function(1:31~1:39) /test/1/catalog1/schema1/DATABASE/
------
SQL  SHOW CREATE LIBRARY split_js_lib;
行为 READ Library(1:20~1:32) /test/1/catalog1/schema1/split_js_lib/
------
SQL  SHOW CREATE LIBRARY split97lib.split_js_lib;
行为 READ Library(1:20~1:43) /test/1/catalog1/split97lib/split_js_lib/
------
SQL  SHOW FULL TABLES FROM split_show97 WHERE Table_type = 'BASE TABLE';
行为 READ Schema(1:22~1:34) /test/1/catalog1/split_show97/
------
SQL  SHOW FULL FIELDS FROM show_t FROM split_show97 LIKE 'n%';
行为 READ Table(1:22~1:28) /test/1/catalog1/split_show97/show_t/
------
SQL  SHOW KEYS FROM show_t FROM split_show97 WHERE Key_name = 'idx_name';
行为 READ Table(1:15~1:21) /test/1/catalog1/split_show97/show_t/
------
SQL  SHOW EVENTS FROM split_show_status97 LIKE 'e%';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW EVENTS IN split_show_status97 WHERE Name='e_status';
行为 READ Instance(1:0~1:56) /test/1/
------
SQL  SHOW TRIGGERS FROM split_show_status97 LIKE 'rt%';
行为 READ Instance(1:0~1:49) /test/1/
------
SQL  SHOW TRIGGERS IN split_show_status97 WHERE `Table`='rt';
行为 READ Instance(1:0~1:55) /test/1/
------
SQL  SHOW FUNCTION STATUS WHERE Db='split_show_status97';
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW PROCEDURE STATUS WHERE Db='split_show_status97';
行为 READ Instance(1:0~1:52) /test/1/

## LOG_READ

SQL  SHOW MASTER STATUS;
行为 READ Log(1:0~1:18) /test/1/
------
SQL  SHOW BINLOG EVENTS;
行为 READ Log(1:0~1:18) /test/1/
------
SQL  SHOW BINLOG EVENTS IN 'binlog.000001';
行为 READ Log(1:0~1:37) /test/1/
------
SQL  SHOW BINLOG EVENTS IN 'binlog.000001' LIMIT 1;
行为 READ Log(1:0~1:45) /test/1/
------
SQL  SHOW BINLOG EVENTS LIMIT 0, 1;
行为 READ Log(1:0~1:29) /test/1/
------
SQL  SHOW RELAYLOG EVENTS;
行为 READ Log(1:0~1:20) /test/1/
------
SQL  SHOW RELAYLOG EVENTS IN 'relay-bin.000001';
行为 READ Log(1:0~1:42) /test/1/
------
SQL  SHOW RELAYLOG EVENTS IN 'relay-bin.000001' FROM 4 LIMIT 0, 1;
行为 READ Log(1:0~1:60) /test/1/
------
SQL  SHOW BINLOG EVENTS IN 'mysql-bin.000001' FROM 4 LIMIT 1;
行为 READ Log(1:0~1:55) /test/1/
------
SQL  SHOW BINARY LOGS;
行为 READ Log(1:0~1:16) /test/1/
------
SQL  SHOW BINLOG EVENTS LIMIT 1;
行为 READ Log(1:0~1:26) /test/1/
------
SQL  SHOW BINLOG EVENTS FROM 4 LIMIT 1;
行为 READ Log(1:0~1:33) /test/1/
------
SQL  SHOW ENGINE ALL LOGS;
行为 READ Log(1:0~1:20) /test/1/
------
SQL  SHOW BINLOG EVENTS LIMIT 1 OFFSET 0;
行为 READ Log(1:0~1:35) /test/1/
------
SQL  SHOW RELAYLOG EVENTS LIMIT 1 OFFSET 0;
行为 READ Log(1:0~1:37) /test/1/
------
SQL  SHOW BINLOG EVENTS LIMIT ? OFFSET ?;
行为 READ Log(1:0~1:35) /test/1/
------
SQL  SHOW MASTER LOGS;
行为 READ Log(1:0~1:16) /test/1/
------
SQL  SHOW BINLOG EVENTS FROM 0004 LIMIT 1;
行为 READ Log(1:0~1:36) /test/1/
------
SQL  SHOW BINLOG EVENTS FROM 1.0 LIMIT 1;
行为 READ Log(1:0~1:35) /test/1/
------
SQL  SHOW BINLOG EVENTS FROM 1e1 LIMIT 1;
行为 READ Log(1:0~1:35) /test/1/
------
SQL  SHOW BINLOG EVENTS FROM 18446744073709551615 LIMIT 1;
行为 READ Log(1:0~1:52) /test/1/
------
SQL  SHOW RELAYLOG EVENTS FROM 1.0 LIMIT 1;
行为 READ Log(1:0~1:37) /test/1/
------
SQL  SHOW RELAYLOG EVENTS LIMIT 1;
行为 READ Log(1:0~1:28) /test/1/
------
SQL  SHOW ENGINE INNODB LOGS;
行为 READ Log(1:0~1:23) /test/1/
------
SQL  SELECT audit_log_read('{"max_array_length":5}');
行为 READ Log(1:0~1:47) /test/1/
行为 CALL Function(1:7~1:21) /test/1/catalog1/schema1/audit_log_read/
------
SQL  SELECT audit_log_read_bookmark();
行为 READ Log(1:0~1:32) /test/1/
行为 CALL Function(1:7~1:30) /test/1/catalog1/schema1/audit_log_read_bookmark/
------
SQL  SHOW RELAYLOG EVENTS IN 'relay-bin.000001' LIMIT 1 FOR CHANNEL 'chan';
行为 READ Log(1:0~1:69) /test/1/
------
SQL  SHOW RELAYLOG EVENTS LIMIT 1 FOR CHANNEL '';
行为 READ Log(1:0~1:43) /test/1/
------
SQL  SHOW RELAYLOG EVENTS FOR CHANNEL '';
行为 READ Log(1:0~1:35) /test/1/
------
SQL  SHOW BINLOG EVENTS IN 'binlog.000001' FROM 4 LIMIT 1;
行为 READ Log(1:0~1:52) /test/1/
------
SQL  show binlog events;
行为 READ Log(1:0~1:18) /test/1/
------
SQL  show master status;
行为 READ Log(1:0~1:18) /test/1/
------
SQL  show relaylog events;
行为 READ Log(1:0~1:20) /test/1/
------
SQL  SHOW BINARY LOG STATUS;
行为 READ Log(1:0~1:22) /test/1/

## ADMIN_RESOURCE_GROUP

SQL  SET RESOURCE GROUP split_rg_a;
行为 ADMIN ResourceGroup(1:19~1:29) /test/1/catalog1/schema1/split_rg_a/
------
SQL  SET RESOURCE GROUP split_rg_user_mix FOR 77777,88888;
行为 ADMIN ResourceGroup(1:19~1:36) /test/1/catalog1/schema1/split_rg_user_mix/
------
SQL  SET RESOURCE GROUP ca_rg FOR 77777;
行为 ADMIN ResourceGroup(1:19~1:24) /test/1/catalog1/schema1/ca_rg/

## PERFORMANCE

SQL  GET DIAGNOSTICS @p1 = NUMBER, @p2 = ROW_COUNT;
行为 READ ConfigKey(1:16~1:19) /test/1/p1/
行为 READ ConfigKey(1:30~1:33) /test/1/p2/
------
SQL  GET CURRENT DIAGNOSTICS CONDITION 1 @state = RETURNED_SQLSTATE, @msg = MESSAGE_TEXT, @errno = MYSQL_ERRNO;
行为 READ ConfigKey(1:36~1:42) /test/1/state/
行为 READ ConfigKey(1:64~1:68) /test/1/msg/
行为 READ ConfigKey(1:85~1:91) /test/1/errno/
------
SQL  GET DIAGNOSTICS CONDITION @d_condition @d_state=RETURNED_SQLSTATE;
行为 READ ConfigKey(1:26~1:38) /test/1/d_condition/
行为 READ ConfigKey(1:39~1:47) /test/1/d_state/
------
SQL  EXPLAIN SELECT e FROM split_type_enum_set.es_enum_pk WHERE e=0;
行为 READ Table(1:22~1:52) /test/1/catalog1/split_type_enum_set/es_enum_pk/
------
SQL  EXPLAIN SELECT 1+1,1-1,1+1*2,8/5,8%5,MOD(8,5),MOD(8,5)|0,-(1+1)*-2;
行为 READ Instance(1:0~1:66) /test/1/
行为 CALL Function(1:37~1:40) /test/1/catalog1/schema1/MOD/
------
SQL  EXPLAIN SELECT 1 | (1+1),5 & 3,BIT_COUNT(7);
行为 READ Instance(1:0~1:43) /test/1/
行为 CALL Function(1:31~1:40) /test/1/catalog1/schema1/BIT_COUNT/
------
SQL  EXPLAIN SELECT 1 FROM (SELECT 1) AS a PROCEDURE ANALYSE();
行为 READ Instance(1:0~1:57) /test/1/
------
SQL  EXPLAIN SELECT * FROM analyse_int PROCEDURE ANALYSE();
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/analyse_int/
------
SQL  EXPLAIN SELECT * FROM analyse_int,(SELECT * FROM analyse_int) AS tt1 WHERE analyse_int.a=tt1.a PROCEDURE ANALYSE();
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/analyse_int/
------
SQL  EXPLAIN SELECT UNCOMPRESS(COMPRESS(@test_compress_string));
行为 CALL Function(1:15~1:25) /test/1/catalog1/schema1/UNCOMPRESS/
行为 CALL Function(1:26~1:34) /test/1/catalog1/schema1/COMPRESS/
行为 READ ConfigKey(1:35~1:56) /test/1/test_compress_string/
------
SQL  EXPLAIN SELECT UNCOMPRESSED_LENGTH(COMPRESS(@test_compress_string))=LENGTH(@test_compress_string);
行为 CALL Function(1:15~1:34) /test/1/catalog1/schema1/UNCOMPRESSED_LENGTH/
行为 CALL Function(1:35~1:43) /test/1/catalog1/schema1/COMPRESS/
行为 READ ConfigKey(1:44~1:65) /test/1/test_compress_string/
行为 CALL Function(1:68~1:74) /test/1/catalog1/schema1/LENGTH/
------
SQL  EXPLAIN SELECT * FROM compress_varchar_t WHERE UNCOMPRESS(a) IS NULL;
行为 READ Table(1:22~1:40) /test/1/catalog1/schema1/compress_varchar_t/
行为 CALL Function(1:47~1:57) /test/1/catalog1/schema1/UNCOMPRESS/
------
SQL  EXPLAIN SELECT *,UNCOMPRESS(a) FROM compress_varchar_t;
行为 CALL Function(1:17~1:27) /test/1/catalog1/schema1/UNCOMPRESS/
行为 READ Table(1:36~1:54) /test/1/catalog1/schema1/compress_varchar_t/
------
SQL  EXPLAIN SELECT * FROM (SELECT UNCOMPRESSED_LENGTH(c1) FROM compress_int_t) AS s;
行为 CALL Function(1:30~1:49) /test/1/catalog1/schema1/UNCOMPRESSED_LENGTH/
行为 READ Table(1:59~1:73) /test/1/catalog1/schema1/compress_int_t/
------
SQL  EXPLAIN SELECT CONCAT('gui_',t2.a),t1.d FROM codex_func_concat.join_t2 AS t2 LEFT JOIN codex_func_concat.join_t1 AS t1 ON t1.a=CONCAT('gui_',t2.a) AND t1.b='a' AND t1.c='b';
行为 CALL Function(1:15~1:21) /test/1/catalog1/schema1/CONCAT/
行为 READ Table(1:45~1:70) /test/1/catalog1/codex_func_concat/join_t2/
行为 READ Table(1:87~1:112) /test/1/catalog1/codex_func_concat/join_t1/
------
SQL  EXPLAIN EXTENDED SELECT PASSWORD('idkfa '), OLD_PASSWORD('idkfa');
行为 READ Instance(1:0~1:65) /test/1/
行为 CALL Function(1:24~1:32) /test/1/catalog1/schema1/PASSWORD/
行为 CALL Function(1:44~1:56) /test/1/catalog1/schema1/OLD_PASSWORD/
------
SQL  EXPLAIN SELECT DEFAULT(str), DEFAULT(strnull), DEFAULT(intg), DEFAULT(rel) FROM t1;
行为 CALL Function(1:15~1:22) /test/1/catalog1/schema1/DEFAULT/
行为 READ Table(1:80~1:82) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t1 WHERE str <> DEFAULT(str);
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:38~1:45) /test/1/catalog1/schema1/DEFAULT/
------
SQL  EXPLAIN SELECT 1 FROM (SELECT DISTINCT GROUP_CONCAT(td.a) FROM t1,t1 AS td GROUP BY td.a) AS d,t1;
行为 CALL Function(1:39~1:51) /test/1/catalog1/schema1/GROUP_CONCAT/
行为 READ Table(1:63~1:65) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN EXTENDED SELECT IF(u=1,st,BINARY st) AS s FROM case_t WHERE st LIKE '%a%' ORDER BY s;
行为 CALL Function(1:24~1:26) /test/1/catalog1/schema1/IF/
行为 READ Table(1:55~1:61) /test/1/catalog1/schema1/case_t/
------
SQL  EXPLAIN SELECT NULLIF(u,1) FROM case_t;
行为 CALL Function(1:15~1:21) /test/1/catalog1/schema1/NULLIF/
行为 READ Table(1:32~1:38) /test/1/catalog1/schema1/case_t/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.range_int WHERE a NOT IN (0,2,4,6,8,10,12,14,16,18);
行为 READ Table(1:22~1:45) /test/1/catalog1/codex_func_in/range_int/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.range_dt WHERE a NOT IN ('2006-04-25 10:00:00','2006-04-25 10:02:00','2006-04-25 10:04:00','2006-04-25 10:06:00','2006-04-25 10:08:00');
行为 READ Table(1:22~1:44) /test/1/catalog1/codex_func_in/range_dt/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.range_char WHERE a NOT IN ('foo','barbar','bazbazbaz');
行为 READ Table(1:22~1:46) /test/1/catalog1/codex_func_in/range_char/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.range_dec WHERE a NOT IN (345.67890,43245.34,64224.56344);
行为 READ Table(1:22~1:45) /test/1/catalog1/codex_func_in/range_dec/
------
SQL  EXPLAIN SELECT f1 FROM codex_func_in.mixed_char WHERE f1 IN ('a',1);
行为 READ Table(1:23~1:47) /test/1/catalog1/codex_func_in/mixed_char/
------
SQL  EXPLAIN SELECT f2 FROM codex_func_in.mixed_int WHERE f2 IN (1,'b');
行为 READ Table(1:23~1:46) /test/1/catalog1/codex_func_in/mixed_int/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_int IN (1,NULL,2,NULL,3,NULL);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_decimal IN (NULL,NULL);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_float IN (NULL,1,2,3);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_bit IN (NULL);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_date IN (NULL,'2009-09-01','2009-09-02','2009-09-03');
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_datetime IN (NULL,NULL);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_timestamp IN (NULL,'2009-09-01 00:00:01','2009-09-01 00:00:02','2009-09-01 00:00:03');
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_time IN (NULL,NULL);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_year IN (NULL,1,2,3);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM codex_func_in.explain_types WHERE c_char IN (NULL,NULL);
行为 READ Table(1:22~1:49) /test/1/catalog1/codex_func_in/explain_types/
------
SQL  EXPLAIN SELECT * FROM t3, t1 WHERE t1.col_date_key IS NULL;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:26~1:28) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t3 JOIN t1 ON t1.col_date_key IS NULL;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:30~1:32) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t3 LEFT JOIN t1 ON t1.col_date_key IS NULL;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:35~1:37) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t3 LEFT JOIN t1 ON t1.col_date_key IS NULL WHERE t1.col_date_key IS NULL;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:35~1:37) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t3, t1 WHERE t1.col_date_key IS NOT NULL;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:26~1:28) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t3, t1 WHERE NOT (t1.col_date_key IS NULL);
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:26~1:28) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT * FROM t3, t1 WHERE (t1.col_date_key IS NULL) IS TRUE;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t3/
行为 READ Table(1:26~1:28) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT FLOOR(5.5), CEILING(-5.5), TRUNCATE(52.64,-2), ROUND(5.64,-2);
行为 READ Instance(1:0~1:76) /test/1/
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:27~1:34) /test/1/catalog1/schema1/CEILING/
行为 CALL Function(1:42~1:50) /test/1/catalog1/schema1/TRUNCATE/
行为 CALL Function(1:62~1:67) /test/1/catalog1/schema1/ROUND/
------
SQL  EXPLAIN SELECT MD5('hello');
行为 READ Instance(1:0~1:27) /test/1/
行为 CALL Function(1:15~1:18) /test/1/catalog1/schema1/MD5/
------
SQL  EXPLAIN EXTENDED SELECT s FROM trim_values WHERE TRIM(s)>'ab';
行为 READ Table(1:31~1:42) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN EXTENDED SELECT s FROM trim_values WHERE TRIM('y' FROM s)>'ab';
行为 READ Table(1:31~1:42) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN EXTENDED SELECT s FROM trim_values WHERE TRIM(LEADING 'y' FROM s)>'ab';
行为 READ Table(1:31~1:42) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN EXTENDED SELECT s FROM trim_values WHERE TRIM(TRAILING 'y' FROM s)>'ab';
行为 READ Table(1:31~1:42) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN EXTENDED SELECT s FROM trim_values WHERE TRIM(BOTH 'y' FROM s)>'ab';
行为 READ Table(1:31~1:42) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN EXTENDED SELECT * FROM code_values INNER JOIN code_ids ON code=id WHERE id='a12' AND (LENGTH(code)=5 OR code<'a00');
行为 READ Table(1:31~1:42) /test/1/catalog1/schema1/code_values/
行为 READ Table(1:54~1:62) /test/1/catalog1/schema1/code_ids/
行为 CALL Function(1:94~1:100) /test/1/catalog1/schema1/LENGTH/
------
SQL  EXPLAIN SELECT id FROM str_unsigned WHERE a=16307858876001849059;
行为 READ Table(1:23~1:35) /test/1/catalog1/schema1/str_unsigned/
------
SQL  EXPLAIN SELECT id FROM str_unsigned WHERE a=CONV('e251273eb74a8ee3',16,10);
行为 READ Table(1:23~1:35) /test/1/catalog1/schema1/str_unsigned/
行为 CALL Function(1:44~1:48) /test/1/catalog1/schema1/CONV/
------
SQL  EXPLAIN SELECT c1 FROM func_test_desc_index WHERE c1='abc' AND c2 IN ('def') AND c3 BETWEEN '2022-03-16' AND '2022-03-16' ORDER BY c3 DESC;
行为 READ Table(1:23~1:43) /test/1/catalog1/schema1/func_test_desc_index/
------
SQL  EXPLAIN UPDATE t1 JOIN t2 USING(a) SET t2.a = t2.a + 1 WHERE t1.b > 0;
行为 READ Table(1:15~1:17) /test/1/catalog1/schema1/t1/
行为 READ Table(1:23~1:25) /test/1/catalog1/schema1/t2/
------
SQL  EXPLAIN SELECT COUNT(*) FROM t2 LEFT JOIN t1 ON t2.fkey=t1.id WHERE t1.name LIKE 'A%' OR FALSE;
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:29~1:31) /test/1/catalog1/schema1/t2/
行为 READ Table(1:42~1:44) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT\n(\n  SELECT\n  ROW(t1.b, a) = ROW(ROW(1, t2.c) = ROW(1, d), c) = a\n  FROM t1\n)\nFROM t2 GROUP BY a;
行为 CALL Function(4:2~4:5) /test/1/catalog1/schema1/ROW/
行为 READ Table(5:7~5:9) /test/1/catalog1/schema1/t1/
行为 READ Table(7:5~7:7) /test/1/catalog1/schema1/t2/
------
SQL  EXPLAIN SELECT * FROM t1 FOR UPDATE;
行为 READ Table(1:22~1:24) /test/1/catalog1/schema1/t1/
------
SQL  SHOW OPEN TABLES WHERE f1() = 0;
行为 READ Instance(1:0~1:31) /test/1/
行为 CALL Function(1:23~1:25) /test/1/catalog1/schema1/f1/
------
SQL  EXPLAIN splitv56.desc_t;
行为 READ Table(1:8~1:23) /test/1/catalog1/splitv56/desc_t/
------
SQL  SHOW STATUS;
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  SHOW STATUS LIKE 'Threads%';
行为 READ Instance(1:0~1:27) /test/1/
------
SQL  SHOW GLOBAL STATUS;
行为 READ Instance(1:0~1:18) /test/1/
------
SQL  SHOW GLOBAL STATUS WHERE Variable_name LIKE 'Threads%';
行为 READ Instance(1:0~1:54) /test/1/
------
SQL  SHOW SESSION STATUS;
行为 READ Instance(1:0~1:19) /test/1/
------
SQL  SHOW SESSION STATUS LIKE 'Threads%';
行为 READ Instance(1:0~1:35) /test/1/
------
SQL  SHOW SESSION STATUS WHERE Variable_name LIKE 'Threads%';
行为 READ Instance(1:0~1:55) /test/1/
------
SQL  SHOW COUNT(*) WARNINGS;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  SHOW WARNINGS LIMIT 1;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  SHOW WARNINGS LIMIT 0,2;
行为 READ Instance(1:0~1:23) /test/1/
------
SQL  SHOW ERRORS;
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  SHOW ERRORS LIMIT 1;
行为 READ Instance(1:0~1:19) /test/1/
------
SQL  SHOW ERRORS LIMIT 0,2;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  SHOW COUNT(*) ERRORS;
行为 READ Instance(1:0~1:20) /test/1/
------
SQL  SHOW ENGINE ALL STATUS;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  SHOW ENGINE ALL MUTEX;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  SHOW ENGINE INNODB MUTEX;
行为 READ Instance(1:0~1:24) /test/1/
------
SQL  SHOW WARNINGS LIMIT 2 OFFSET 0;
行为 READ Instance(1:0~1:30) /test/1/
------
SQL  SHOW ERRORS LIMIT 2 OFFSET 0;
行为 READ Instance(1:0~1:28) /test/1/
------
SQL  SHOW PROFILE LIMIT 1 OFFSET 0;
行为 READ Instance(1:0~1:29) /test/1/
------
SQL  SHOW WARNINGS LIMIT ? OFFSET ?;
行为 READ Instance(1:0~1:30) /test/1/
------
SQL  SHOW PROFILE LIMIT ? OFFSET ?;
行为 READ Instance(1:0~1:29) /test/1/
------
SQL  SHOW OPEN TABLES FROM split_show56 LIKE 'show%';
行为 READ Instance(1:0~1:47) /test/1/
------
SQL  SHOW PROFILE FOR QUERY 00000000002147483647 LIMIT 1;
行为 READ Instance(1:0~1:51) /test/1/
------
SQL  SHOW PROCESSLIST;
行为 READ Instance(1:0~1:16) /test/1/
------
SQL  SHOW FULL PROCESSLIST;
行为 READ Instance(1:0~1:21) /test/1/
------
SQL  SHOW PROFILES;
行为 READ Instance(1:0~1:13) /test/1/
------
SQL  SHOW PROFILE;
行为 READ Instance(1:0~1:12) /test/1/
------
SQL  SHOW PROFILE CPU FOR QUERY 1;
行为 READ Instance(1:0~1:28) /test/1/
------
SQL  SHOW PROFILE BLOCK IO, CONTEXT SWITCHES, PAGE FAULTS, SOURCE, SWAPS FOR QUERY 1 LIMIT 5;
行为 READ Instance(1:0~1:87) /test/1/
------
SQL  SHOW PROFILE ALL FOR QUERY 1 LIMIT 5 OFFSET 0;
行为 READ Instance(1:0~1:45) /test/1/
------
SQL  SHOW PROFILE MEMORY FOR QUERY 1 LIMIT 0,5;
行为 READ Instance(1:0~1:41) /test/1/
------
SQL  SHOW GLOBAL STATUS LIKE 'Threads_connected';
行为 READ Instance(1:0~1:43) /test/1/
------
SQL  SHOW STATUS WHERE Variable_name LIKE 'Uptime%';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW ENGINE INNODB STATUS;
行为 READ Instance(1:0~1:25) /test/1/
------
SQL  SHOW WARNINGS;
行为 READ Instance(1:0~1:13) /test/1/
------
SQL  SHOW LOCAL STATUS;
行为 READ Instance(1:0~1:17) /test/1/
------
SQL  SHOW LOCAL STATUS LIKE 'Threads%';
行为 READ Instance(1:0~1:33) /test/1/
------
SQL  SHOW LOCAL STATUS WHERE Variable_name='Uptime';
行为 READ Instance(1:0~1:46) /test/1/
------
SQL  SHOW ENGINE PERFORMANCE_SCHEMA STATUS;
行为 READ Instance(1:0~1:37) /test/1/
------
SQL  SHOW PROFILE IPC FOR QUERY 1;
行为 READ Instance(1:0~1:28) /test/1/
------
SQL  EXPLAIN SELECT * FROM json_op;
行为 READ Table(1:22~1:29) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN FORMAT=TRADITIONAL SELECT * FROM json_op;
行为 READ Table(1:41~1:48) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN FORMAT=JSON SELECT * FROM json_op;
行为 READ Table(1:34~1:41) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN FORMAT=TREE SELECT * FROM json_op;
行为 READ Table(1:34~1:41) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN EXTENDED SELECT * FROM json_op;
行为 READ Table(1:31~1:38) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN PARTITIONS SELECT * FROM json_op;
行为 READ Table(1:33~1:40) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN REPLACE INTO utility_audit.maint_a VALUES (1,1);
行为 MERGE Table(1:21~1:42) /test/1/catalog1/utility_audit/maint_a/
------
SQL  EXPLAIN SELECT /*+ NO_BNL(t1) */ * FROM t1 t1, T1 T1 WHERE T1.f1 BETWEEN 1 AND 3 AND t1.f2=T1.f2;
行为 READ Table(1:40~1:42) /test/1/catalog1/schema1/t1/
行为 READ Table(1:47~1:49) /test/1/catalog1/schema1/T1/
------
SQL  EXPLAIN SELECT /*+ NO_BNL(T1) */ * FROM t1 t1, T1 T1 WHERE T1.f1 BETWEEN 1 AND 3 AND t1.f2=T1.f2;
行为 READ Table(1:40~1:42) /test/1/catalog1/schema1/t1/
行为 READ Table(1:47~1:49) /test/1/catalog1/schema1/T1/
------
SQL  EXPLAIN FORMAT=JSON INSERT INTO codex_explain4.t VALUES (3,'c');
行为 INSERT Table(1:32~1:48) /test/1/catalog1/codex_explain4/t/
------
SQL  EXPLAIN FORMAT=JSON UPDATE codex_explain4.t SET name='x' WHERE id=1;
行为 UPDATE Table(1:27~1:43) /test/1/catalog1/codex_explain4/t/
------
SQL  EXPLAIN FORMAT=JSON DELETE FROM codex_explain4.t WHERE id=2;
行为 DELETE Table(1:32~1:48) /test/1/catalog1/codex_explain4/t/
------
SQL  EXPLAIN DELETE FROM split_select_safe.t1 WHERE c1 IN (1,22);
行为 DELETE Table(1:20~1:40) /test/1/catalog1/split_select_safe/t1/
------
SQL  EXPLAIN UPDATE split_select_safe.t1 SET c1=20 WHERE c1 IN (1,22);
行为 UPDATE Table(1:15~1:35) /test/1/catalog1/split_select_safe/t1/
------
SQL  EXPLAIN UPDATE split_select_safe.t1 AS t1,split_select_safe.t2 AS t2 SET t1.c1=20 WHERE t1.c2=t2.c1;
行为 READ Table(1:15~1:35) /test/1/catalog1/split_select_safe/t1/
行为 READ Table(1:42~1:62) /test/1/catalog1/split_select_safe/t2/
------
SQL  EXPLAIN json_op;
行为 READ Table(1:8~1:15) /test/1/catalog1/schema1/json_op/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(optimizer_switch = 'batched_key_access=on') SET_VAR(optimizer_switch = 'batched_key_access=off') */ * FROM t1;
行为 READ Table(1:134~1:136) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:43) /test/1/optimizer_switch/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(max_join_size=1) SET_VAR(max_join_size=1) */ * FROM t1;
行为 READ Table(1:79~1:81) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:40) /test/1/max_join_size/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(optimizer_switch='batched_key_access=on') SET_VAR(big_tables=on) SET_VAR(big_tables=off) */ * FROM t1;
行为 READ Table(1:126~1:128) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:43) /test/1/optimizer_switch/
行为 CONFIGURE ConfigKey(1:77~1:87) /test/1/big_tables/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(optimizer_switc='batched_key_access=off') */ * FROM t1;
行为 READ Table(1:79~1:81) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:42) /test/1/optimizer_switc/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(optimizer_switch='batched_key_access=yes') */ * FROM t1;
行为 READ Table(1:80~1:82) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:43) /test/1/optimizer_switch/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(max_allowed_packet=1M) */ * FROM t1;
行为 READ Table(1:60~1:62) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:45) /test/1/max_allowed_packet/
------
SQL  EXPLAIN SELECT /*+ SET_VAR(optimizer_switch='batched_key_acces=off') SET_VAR(range_alloc_block_size=amba) */ * FROM t1;
行为 READ Table(1:116~1:118) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:43) /test/1/optimizer_switch/
行为 CONFIGURE ConfigKey(1:77~1:99) /test/1/range_alloc_block_size/
------
SQL  EXPLAIN SELECT /*+ set_var(optimizer_switch='prefer_ordering_index=off') */ * FROM t1;
行为 READ Table(1:83~1:85) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:27~1:43) /test/1/optimizer_switch/
------
SQL  EXPLAIN FORMAT=TREE SELECT ST_Distance(location,POINT(0,0)) AS d FROM cafe ORDER BY d;
行为 CALL Function(1:27~1:38) /test/1/catalog1/schema1/ST_Distance/
行为 CALL Function(1:48~1:53) /test/1/catalog1/schema1/POINT/
行为 READ Table(1:70~1:74) /test/1/catalog1/schema1/cafe/
------
SQL  EXPLAIN FORMAT=TREE SELECT ST_Distance(location1,location2) AS d FROM cafe2 ORDER BY d LIMIT 5;
行为 CALL Function(1:27~1:38) /test/1/catalog1/schema1/ST_Distance/
行为 READ Table(1:70~1:75) /test/1/catalog1/schema1/cafe2/
------
SQL  EXPLAIN FORMAT=TREE SELECT ST_Distance(location1,POINT(0,0)) AS d1,ST_Distance(location2,POINT(0,0)) AS d2 FROM cafe2 ORDER BY d1,d2;
行为 CALL Function(1:27~1:38) /test/1/catalog1/schema1/ST_Distance/
行为 CALL Function(1:49~1:54) /test/1/catalog1/schema1/POINT/
行为 READ Table(1:112~1:117) /test/1/catalog1/schema1/cafe2/
------
SQL  EXPLAIN FORMAT=TREE SELECT a.id,ST_Distance(a.location,POINT(0,0)) AS d FROM cafe a JOIN cafe2 b ON b.id=a.id ORDER BY d;
行为 CALL Function(1:32~1:43) /test/1/catalog1/schema1/ST_Distance/
行为 CALL Function(1:55~1:60) /test/1/catalog1/schema1/POINT/
行为 READ Table(1:77~1:81) /test/1/catalog1/schema1/cafe/
行为 READ Table(1:89~1:94) /test/1/catalog1/schema1/cafe2/
------
SQL  EXPLAIN SELECT * FROM (SELECT t1.c1 FROM split_derived_tail.t AS t1 INNER JOIN split_derived_tail.t AS t2 ON t1.c1=3 AND t2.c2=3 GROUP BY t1.c1,t2.c2) AS a,split_derived_tail.t AS b WHERE b.id BETWEEN 1 AND 10;
行为 READ Table(1:41~1:61) /test/1/catalog1/split_derived_tail/t/
------
SQL  EXPLAIN SELECT * FROM (SELECT t1.id FROM split_derived_tail.t AS t1 INNER JOIN split_derived_tail.t AS t2 INNER JOIN split_derived_tail.t AS t3 ON t1.id=1 AND t1.c1=t2.id AND t2.c1=t3.id GROUP BY t1.id,t2.c2,t3.c2) AS a,split_derived_tail.t AS b WHERE b.id BETWEEN 1 AND 10;
行为 READ Table(1:41~1:61) /test/1/catalog1/split_derived_tail/t/
------
SQL  EXPLAIN SELECT * FROM (SELECT DISTINCT t1.id FROM split_derived_tail.t AS t1 WHERE t1.id=1) AS a,split_derived_tail.t AS b WHERE b.id BETWEEN 1 AND 10;
行为 READ Table(1:50~1:70) /test/1/catalog1/split_derived_tail/t/
------
SQL  EXPLAIN SELECT * FROM (SELECT t1.id+1 FROM split_derived_tail.t AS t1 INNER JOIN split_derived_tail.t AS t2 ON t1.id=1 GROUP BY t1.id+1) AS a,split_derived_tail.t AS b WHERE b.id BETWEEN 1 AND 10;
行为 READ Table(1:43~1:63) /test/1/catalog1/split_derived_tail/t/
------
SQL  EXPLAIN SELECT * FROM (SELECT t1.c1 FROM split_derived_tail.t AS t1 INNER JOIN split_derived_tail.t AS t2 ON t1.c1=3 GROUP BY 1.5) AS a,split_derived_tail.t AS b WHERE b.id BETWEEN 1 AND 10;
行为 READ Table(1:41~1:61) /test/1/catalog1/split_derived_tail/t/
------
SQL  EXPLAIN SELECT * FROM (SELECT t1.id FROM split_derived_tail.t AS t1 INNER JOIN split_derived_tail.t AS t2 ON MOD(t1.id,1000)=1 GROUP BY t1.id) AS a,split_derived_tail.t AS b WHERE b.id BETWEEN 1 AND 10;
行为 READ Table(1:41~1:61) /test/1/catalog1/split_derived_tail/t/
行为 CALL Function(1:109~1:112) /test/1/catalog1/schema1/MOD/
------
SQL  EXPLAIN SELECT * FROM (SELECT v1.a FROM split_derived_tail.v1 LEFT OUTER JOIN split_derived_tail.v2 ON v1.a=v2.b AND v1.a=10 GROUP BY v1.a) AS p,split_derived_tail.t AS q WHERE q.id BETWEEN 1 AND 10;
行为 READ Table(1:40~1:61) /test/1/catalog1/split_derived_tail/v1/
行为 READ Table(1:78~1:99) /test/1/catalog1/split_derived_tail/v2/
行为 READ Table(1:145~1:165) /test/1/catalog1/split_derived_tail/t/
------
SQL  EXPLAIN SELECT 1 AS a FROM t_encode_nullable,(SELECT DECODE(f1,f1) AS b FROM t_encode_nullable) AS encoded_derived;
行为 READ Table(1:27~1:44) /test/1/catalog1/schema1/t_encode_nullable/
行为 CALL Function(1:53~1:59) /test/1/catalog1/schema1/DECODE/
------
SQL  EXPLAIN SELECT 1 AS a FROM t_encode_nullable,(SELECT ENCODE(f1,f1) AS b FROM t_encode_nullable) AS encoded_derived;
行为 READ Table(1:27~1:44) /test/1/catalog1/schema1/t_encode_nullable/
行为 CALL Function(1:53~1:59) /test/1/catalog1/schema1/ENCODE/
------
SQL  GET STACKED DIAGNOSTICS CONDITION 1 @state = RETURNED_SQLSTATE, @msg = MESSAGE_TEXT;
行为 READ ConfigKey(1:36~1:42) /test/1/state/
行为 READ ConfigKey(1:64~1:68) /test/1/msg/
------
SQL  GET STACKED DIAGNOSTICS @utility_number = NUMBER;
行为 READ ConfigKey(1:24~1:39) /test/1/utility_number/
------
SQL  EXPLAIN EXTENDED SELECT PASSWORD('idkfa ');
行为 READ Instance(1:0~1:42) /test/1/
行为 CALL Function(1:24~1:32) /test/1/catalog1/schema1/PASSWORD/
------
SQL  EXPLAIN splitv57.desc_t;
行为 READ Table(1:8~1:23) /test/1/catalog1/splitv57/desc_t/
------
SQL  SHOW OPEN TABLES FROM split_show57 LIKE 'show%';
行为 READ Instance(1:0~1:47) /test/1/
------
SQL  EXPLAIN FOR CONNECTION 1;
行为 READ Instance(1:0~1:24) /test/1/
------
SQL  EXPLAIN FORMAT=TRADITIONAL FOR CONNECTION 999999;
行为 READ Instance(1:0~1:48) /test/1/
------
SQL  EXPLAIN FORMAT=JSON WITH qn(a) AS (SELECT 1 UNION ALL SELECT 2) SELECT * FROM qn WHERE a=(SELECT MIN(a) FROM qn);
行为 READ Instance(1:0~1:112) /test/1/
行为 CALL Function(1:97~1:100) /test/1/catalog1/schema1/MIN/
------
SQL  EXPLAIN FORMAT=TRADITIONAL WITH RECURSIVE qn(n) AS (SELECT 1 UNION ALL SELECT n+1 FROM qn WHERE n<3) SELECT * FROM qn;
行为 READ Instance(1:0~1:117) /test/1/
------
SQL  EXPLAIN WITH qn AS (SELECT a+2 AS a,b FROM t2) UPDATE /*+ NO_MERGE(qn) */ t1,qn SET t1.a=qn.a+10 WHERE t1.a-qn.a=0;
行为 READ Table(1:43~1:45) /test/1/catalog1/schema1/t2/
行为 READ Table(1:74~1:76) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN SELECT IF(u=1,st,BINARY st) AS s FROM case_t WHERE st LIKE '%a%' ORDER BY s;
行为 CALL Function(1:15~1:17) /test/1/catalog1/schema1/IF/
行为 READ Table(1:46~1:52) /test/1/catalog1/schema1/case_t/
------
SQL  EXPLAIN SELECT COUNT(*) FROM prefix_varchar WHERE b LIKE 'abc\%%';
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:29~1:43) /test/1/catalog1/schema1/prefix_varchar/
------
SQL  EXPLAIN SELECT COUNT(*) FROM prefix_varchar WHERE b LIKE '\_\_\_\_%';
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:29~1:43) /test/1/catalog1/schema1/prefix_varchar/
------
SQL  EXPLAIN SELECT b LIKE 'abc%' FROM prefix_varchar WHERE b LIKE 'ab%';
行为 READ Table(1:34~1:48) /test/1/catalog1/schema1/prefix_varchar/
------
SQL  EXPLAIN SELECT COUNT(*) FROM prefix_varchar IGNORE INDEX(k2) WHERE b LIKE 'a%';
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:29~1:43) /test/1/catalog1/schema1/prefix_varchar/
------
SQL  EXPLAIN SELECT b LIKE 'ab%' FROM prefix_varchar FORCE INDEX(k3) WHERE a>4 AND b LIKE 'a%';
行为 READ Table(1:33~1:47) /test/1/catalog1/schema1/prefix_varchar/
------
SQL  EXPLAIN SELECT COUNT(*) FROM prefix_text WHERE b LIKE 'aaaa';
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:29~1:40) /test/1/catalog1/schema1/prefix_text/
------
SQL  EXPLAIN SELECT (f1 LIKE NULL) FROM prefix_blob WHERE f1 LIKE 'a%';
行为 READ Table(1:35~1:46) /test/1/catalog1/schema1/prefix_blob/
------
SQL  EXPLAIN SELECT prefix_lower_2.example,prefix_lower_2.id FROM prefix_lower_2,prefix_lower_1 WHERE prefix_lower_1.example=LOWER(prefix_lower_2.example);
行为 READ Table(1:61~1:75) /test/1/catalog1/schema1/prefix_lower_2/
行为 READ Table(1:76~1:90) /test/1/catalog1/schema1/prefix_lower_1/
行为 CALL Function(1:120~1:125) /test/1/catalog1/schema1/LOWER/
------
SQL  EXPLAIN FORMAT=TREE SELECT 1 WHERE RAND() < RAND();
行为 READ Instance(1:0~1:50) /test/1/
行为 CALL Function(1:35~1:39) /test/1/catalog1/schema1/RAND/
------
SQL  EXPLAIN SELECT 1 WHERE RAND() < RAND();
行为 READ Instance(1:0~1:38) /test/1/
行为 CALL Function(1:23~1:27) /test/1/catalog1/schema1/RAND/
------
SQL  EXPLAIN SELECT s FROM trim_values WHERE TRIM(s)>'ab';
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:40~1:44) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN SELECT s FROM trim_values WHERE TRIM('y' FROM s)>'ab';
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:40~1:44) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN SELECT s FROM trim_values WHERE TRIM(LEADING 'y' FROM s)>'ab';
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:40~1:44) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN SELECT s FROM trim_values WHERE TRIM(TRAILING 'y' FROM s)>'ab';
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:40~1:44) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN SELECT s FROM trim_values WHERE TRIM(BOTH 'y' FROM s)>'ab';
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/trim_values/
行为 CALL Function(1:40~1:44) /test/1/catalog1/schema1/TRIM/
------
SQL  EXPLAIN SELECT * FROM code_values INNER JOIN code_ids ON code=id WHERE id='a12' AND (LENGTH(code)=5 OR code<'a00');
行为 READ Table(1:22~1:33) /test/1/catalog1/schema1/code_values/
行为 READ Table(1:45~1:53) /test/1/catalog1/schema1/code_ids/
行为 CALL Function(1:85~1:91) /test/1/catalog1/schema1/LENGTH/
------
SQL  EXPLAIN SELECT uuid_text, HEX(uuid_binary) FROM uuid_text_generated WHERE UUID_TO_BIN(uuid_text) = X'12345679123456781234567812345678';
行为 CALL Function(1:26~1:29) /test/1/catalog1/schema1/HEX/
行为 READ Table(1:48~1:67) /test/1/catalog1/schema1/uuid_text_generated/
行为 CALL Function(1:74~1:85) /test/1/catalog1/schema1/UUID_TO_BIN/
------
SQL  EXPLAIN SELECT HEX(uuid_binary), uuid_text FROM uuid_binary_generated WHERE BIN_TO_UUID(uuid_binary) = '12345679-1234-5678-1234-567812345678';
行为 CALL Function(1:15~1:18) /test/1/catalog1/schema1/HEX/
行为 READ Table(1:48~1:69) /test/1/catalog1/schema1/uuid_binary_generated/
行为 CALL Function(1:76~1:87) /test/1/catalog1/schema1/BIN_TO_UUID/
------
SQL  EXPLAIN TABLE utility_audit.maint_a;
行为 READ Instance(1:0~1:35) /test/1/
------
SQL  EXPLAIN SELECT\n(\n  SELECT\n  ROW(t1.b, a) = ROW(ROW(1, t2.c) = ROW(1, d), c) = a\n  FROM t1\n)\nFROM t2 GROUP BY a;
行为 READ Table(5:7~5:9) /test/1/catalog1/schema1/t1/
行为 READ Table(7:5~7:7) /test/1/catalog1/schema1/t2/
------
SQL  EXPLAIN SELECT 1 INTO @x FROM DUAL INTO @y;
行为 READ ConfigKey(1:22~1:24) /test/1/x/
行为 READ Table(1:30~1:34) /test/1/catalog1/schema1/DUAL/
行为 READ ConfigKey(1:40~1:42) /test/1/y/
------
SQL  EXPLAIN splitv80.desc_t;
行为 READ Table(1:8~1:23) /test/1/catalog1/splitv80/desc_t/
------
SQL  SHOW OPEN TABLES FROM split_show80 LIKE 'show%';
行为 READ Instance(1:0~1:47) /test/1/
------
SQL  show warnings;
行为 READ Instance(1:0~1:13) /test/1/
------
SQL  show count(*) warnings;
行为 READ Instance(1:0~1:22) /test/1/
------
SQL  show engine innodb mutex;
行为 READ Instance(1:0~1:24) /test/1/
------
SQL  show status;
行为 READ Instance(1:0~1:11) /test/1/
------
SQL  show global status;
行为 READ Instance(1:0~1:18) /test/1/
------
SQL  show session status;
行为 READ Instance(1:0~1:19) /test/1/
------
SQL  show profile cpu;
行为 READ Instance(1:0~1:16) /test/1/
------
SQL  show open tables;
行为 READ Instance(1:0~1:16) /test/1/
------
SQL  show open tables in test_db;
行为 READ Instance(1:0~1:27) /test/1/
------
SQL  EXPLAIN FORMAT=TREE (SELECT b FROM t1 UNION SELECT b FROM t1) UNION ALL SELECT b FROM t1;
行为 READ Table(1:35~1:37) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN (SELECT b FROM t1 UNION SELECT b FROM t1) UNION ALL SELECT b FROM t1;
行为 READ Table(1:23~1:25) /test/1/catalog1/schema1/t1/
------
SQL  EXPLAIN FORMAT=JSON SELECT i,j,SUM(i+j) OVER (ORDER BY j ROWS UNBOUNDED PRECEDING) AS s FROM split_window_explain.t ORDER BY s DESC LIMIT 3;
行为 CALL Function(1:31~1:34) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:93~1:115) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=TRADITIONAL SELECT i,j,SUM(i+j) OVER (ORDER BY j ROWS UNBOUNDED PRECEDING) AS s FROM split_window_explain.t ORDER BY s DESC LIMIT 3;
行为 CALL Function(1:38~1:41) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:100~1:122) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT i,GROUP_CONCAT(j ORDER BY j),SUM(i+1) OVER (ORDER BY i DESC ROWS UNBOUNDED PRECEDING) FROM split_window_explain.t GROUP BY i;
行为 CALL Function(1:29~1:41) /test/1/catalog1/schema1/GROUP_CONCAT/
行为 CALL Function(1:56~1:59) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:118~1:140) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT SUM(j),SUM(SUM(j)) OVER (ORDER BY i ROWS UNBOUNDED PRECEDING) FROM split_window_explain.t GROUP BY i;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:94~1:116) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT x.*,SUM(x.rnk) OVER (ROWS UNBOUNDED PRECEDING) FROM (SELECT i,j,RANK() OVER w AS rnk FROM split_window_explain.t WINDOW w AS (PARTITION BY i ORDER BY j)) x;
行为 CALL Function(1:31~1:34) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:91~1:95) /test/1/catalog1/schema1/RANK/
行为 READ Table(1:117~1:139) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT (ROW_NUMBER() OVER w1*5-1) DIV COUNT(*) OVER w2+1 FROM split_window_explain.t WINDOW w1 AS (ORDER BY j),w2 AS ();
行为 CALL Function(1:28~1:38) /test/1/catalog1/schema1/ROW_NUMBER/
行为 CALL Function(1:58~1:63) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:82~1:104) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT t,COUNT(*) OVER (ORDER BY t RANGE BETWEEN INTERVAL 1 HOUR PRECEDING AND INTERVAL '2:2' MINUTE_SECOND FOLLOWING) FROM split_window_explain.t6;
行为 CALL Function(1:29~1:34) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:144~1:167) /test/1/catalog1/split_window_explain/t6/
------
SQL  EXPLAIN FORMAT=JSON SELECT COUNT(*) OVER w0,COUNT(*) OVER w,COUNT(*) OVER w1 FROM split_window_explain.t6 WINDOW w0 AS (),w AS (w0 ORDER BY t),w1 AS (w RANGE BETWEEN INTERVAL 24 HOUR PRECEDING AND INTERVAL '2:2' MINUTE_SECOND FOLLOWING);
行为 CALL Function(1:27~1:32) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:82~1:105) /test/1/catalog1/split_window_explain/t6/
------
SQL  EXPLAIN FORMAT=JSON SELECT j,CAST(SUM(j) OVER (PARTITION BY i) AS JSON),CAST(SUM(j) OVER () AS JSON) FROM split_window_explain.tj;
行为 CALL Function(1:29~1:33) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:34~1:37) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:106~1:129) /test/1/catalog1/split_window_explain/tj/
------
SQL  EXPLAIN FORMAT=JSON SELECT i,ROW_NUMBER() OVER () FROM split_window_explain.tj UNION ALL SELECT i,ROW_NUMBER() OVER () FROM split_window_explain.tj;
行为 CALL Function(1:29~1:39) /test/1/catalog1/schema1/ROW_NUMBER/
行为 READ Table(1:55~1:78) /test/1/catalog1/split_window_explain/tj/
------
SQL  EXPLAIN FORMAT=JSON SELECT j,JSON_TYPE(j),SUM(CASE WHEN JSON_TYPE(j)='ARRAY' THEN j->'$[0]' ELSE j END) OVER (ORDER BY j ROWS 3 PRECEDING) FROM split_window_explain.tj;
行为 CALL Function(1:29~1:38) /test/1/catalog1/schema1/JSON_TYPE/
行为 CALL Function(1:42~1:45) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:144~1:167) /test/1/catalog1/split_window_explain/tj/
------
SQL  EXPLAIN FORMAT=JSON SELECT * FROM (SELECT COUNT(*) OVER ()+SUM(j) OVER () AS total,i FROM split_window_explain.t) x;
行为 CALL Function(1:42~1:47) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:59~1:62) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:90~1:112) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT AVG(i*(SELECT i*d FROM split_window_explain.u)) OVER (PARTITION BY (SELECT i+d FROM split_window_explain.u) ORDER BY (SELECT d FROM split_window_explain.u)) FROM split_window_explain.t;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/AVG/
行为 READ Table(1:50~1:72) /test/1/catalog1/split_window_explain/u/
行为 READ Table(1:189~1:211) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT SUM(j) OVER w,COUNT(j) OVER (),AVG(j) OVER (w ORDER BY j),FIRST_VALUE(j) OVER w FROM split_window_explain.t WINDOW w AS (PARTITION BY i) ORDER BY LAST_VALUE(j) OVER w,NTH_VALUE(j,1) OVER (),ROW_NUMBER() OVER (PARTITION BY j);
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:41~1:46) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:58~1:61) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:85~1:96) /test/1/catalog1/schema1/FIRST_VALUE/
行为 READ Table(1:112~1:134) /test/1/catalog1/split_window_explain/t/
行为 CALL Function(1:173~1:183) /test/1/catalog1/schema1/LAST_VALUE/
行为 CALL Function(1:194~1:203) /test/1/catalog1/schema1/NTH_VALUE/
行为 CALL Function(1:217~1:227) /test/1/catalog1/schema1/ROW_NUMBER/
------
SQL  EXPLAIN FORMAT=JSON SELECT ROW_NUMBER() OVER () FROM split_window_explain.l1 LEFT JOIN split_window_explain.l2 ON l2.a<=l1.a WHERE l1.a=3 GROUP BY l1.a;
行为 CALL Function(1:27~1:37) /test/1/catalog1/schema1/ROW_NUMBER/
行为 READ Table(1:53~1:76) /test/1/catalog1/split_window_explain/l1/
行为 READ Table(1:87~1:110) /test/1/catalog1/split_window_explain/l2/
------
SQL  EXPLAIN FORMAT=JSON SELECT * FROM split_window_explain.t ORDER BY RANK() OVER (ORDER BY i DESC,j);
行为 READ Table(1:34~1:56) /test/1/catalog1/split_window_explain/t/
行为 CALL Function(1:66~1:70) /test/1/catalog1/schema1/RANK/
------
SQL  EXPLAIN FORMAT=JSON SELECT DISTINCT i,NTILE(3) OVER (ORDER BY i),SUM(i) OVER (),COUNT(*) OVER () FROM split_window_explain.tj ORDER BY NTILE(3) OVER (ORDER BY i);
行为 CALL Function(1:38~1:43) /test/1/catalog1/schema1/NTILE/
行为 CALL Function(1:65~1:68) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:80~1:85) /test/1/catalog1/schema1/COUNT/
行为 READ Table(1:102~1:125) /test/1/catalog1/split_window_explain/tj/
------
SQL  EXPLAIN FORMAT=JSON SELECT * FROM (SELECT SUM(j) OVER (),i FROM split_window_explain.t) x;
行为 CALL Function(1:42~1:45) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:64~1:86) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=JSON SELECT i,j,COUNT(i) OVER w,SUM(i) OVER w,AVG(i) OVER w,LAST_VALUE(i) OVER w FROM split_window_explain.t WINDOW w AS (PARTITION BY i ORDER BY j ROWS BETWEEN 1 PRECEDING AND 2 FOLLOWING);
行为 CALL Function(1:31~1:36) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:47~1:50) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:61~1:64) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:75~1:85) /test/1/catalog1/schema1/LAST_VALUE/
行为 READ Table(1:101~1:123) /test/1/catalog1/split_window_explain/t/
------
SQL  EXPLAIN FORMAT=TREE SELECT SUM(c) OVER (PARTITION BY a ORDER BY b) FROM split_window_orders.t1;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:72~1:94) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT SUM(c) OVER (PARTITION BY a ORDER BY b),SUM(c) OVER (ORDER BY a,b) FROM split_window_orders.t1;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:99~1:121) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT SUM(b) OVER (PARTITION BY a),SUM(c) OVER (ORDER BY a,b) FROM split_window_orders.t1;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:88~1:110) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT SUM(c) OVER (PARTITION BY a ORDER BY b),SUM(c) OVER (ORDER BY b,a) FROM split_window_orders.t1;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:99~1:121) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT SUM(c) OVER (PARTITION BY a) FROM split_window_orders.t1;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:61~1:83) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT a,SUM(c) OVER (PARTITION BY a) FROM split_window_orders.t1 ORDER BY a;
行为 CALL Function(1:29~1:32) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:63~1:85) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT a,SUM(c) OVER (ORDER BY b),AVG(c) OVER (ORDER BY a),SUM(c) OVER (PARTITION BY a) AS x FROM split_window_orders.t1 ORDER BY b,x;
行为 CALL Function(1:29~1:32) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:54~1:57) /test/1/catalog1/schema1/AVG/
行为 READ Table(1:118~1:140) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT DISTINCT a,SUM(c) OVER (ORDER BY b) FROM split_window_orders.t1;
行为 CALL Function(1:38~1:41) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:68~1:90) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT DISTINCT a,b,SUM(b) OVER (PARTITION BY a) FROM split_window_orders.t1 ORDER BY a;
行为 CALL Function(1:40~1:43) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:74~1:96) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT DISTINCT a,SUM(c) OVER (ORDER BY b) FROM split_window_orders.t1 ORDER BY a;
行为 CALL Function(1:38~1:41) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:68~1:90) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT SUM(a) OVER (ORDER BY b) FROM split_window_orders.t1 WHERE b=3;
行为 CALL Function(1:27~1:30) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:57~1:79) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT t1.a,SUM(t1.a) OVER (ORDER BY t1.a),SUM(t2.b) OVER (ORDER BY t2.b) FROM split_window_orders.t1 t1 JOIN split_window_orders.t1 t2 ON t1.a=t2.b;
行为 CALL Function(1:32~1:35) /test/1/catalog1/schema1/SUM/
行为 READ Table(1:99~1:121) /test/1/catalog1/split_window_orders/t1/
------
SQL  EXPLAIN FORMAT=TREE SELECT ROW_NUMBER() OVER (PARTITION BY p1.x) FROM split_window_orders.p1 p1,split_window_orders.p2 p2 WHERE p1.x=p2.pk GROUP BY p1.pk;
行为 CALL Function(1:27~1:37) /test/1/catalog1/schema1/ROW_NUMBER/
行为 READ Table(1:70~1:92) /test/1/catalog1/split_window_orders/p1/
行为 READ Table(1:96~1:118) /test/1/catalog1/split_window_orders/p2/
------
SQL  EXPLAIN splitv84.desc_t;
行为 READ Table(1:8~1:23) /test/1/catalog1/splitv84/desc_t/
------
SQL  SHOW OPEN TABLES FROM split_show84 LIKE 'show%';
行为 READ Instance(1:0~1:47) /test/1/
------
SQL  SHOW PARSE_TREE SELECT 1;
行为 READ Instance(1:0~1:24) /test/1/
------
SQL  SHOW PARSE_TREE UPDATE no_such_table SET id = 1;
行为 UPDATE Table(1:23~1:36) /test/1/catalog1/schema1/no_such_table/
------
SQL  SHOW PARSE_TREE CREATE TABLE parse_tree_t(id INT);
行为 CREATE Table(1:29~1:41) /test/1/catalog1/schema1/parse_tree_t/
------
SQL  EXPLAIN FORMAT=JSON SELECT * FROM explain_t84 WHERE id = 1;
行为 READ Table(1:34~1:45) /test/1/catalog1/schema1/explain_t84/
------
SQL  EXPLAIN FORMAT=TRADITIONAL SELECT * FROM explain_t84 WHERE id = 1;
行为 READ Table(1:41~1:52) /test/1/catalog1/schema1/explain_t84/
------
SQL  EXPLAIN FOR CONNECTION 30;
行为 READ Instance(1:0~1:25) /test/1/
------
SQL  EXPLAIN FORMAT=JSON INTO @codex_load_audit_explain FOR SCHEMA codex_load_audit_s SELECT 1;
行为 READ ConfigKey(1:25~1:50) /test/1/codex_load_audit_explain/
------
SQL  EXPLAIN FORMAT=JSON INTO @codex_load_audit_explain SELECT 1;
行为 READ ConfigKey(1:25~1:50) /test/1/codex_load_audit_explain/
------
SQL  EXPLAIN FORMAT=TRADITIONAL FOR SCHEMA split_native_gap SELECT * FROM t_explain;
行为 READ Table(1:69~1:78) /test/1/catalog1/schema1/t_explain/
------
SQL  EXPLAIN SELECT 1 AS res QUALIFY ROW_NUMBER() OVER () > 10;
行为 READ Instance(1:0~1:57) /test/1/
行为 CALL Function(1:32~1:42) /test/1/catalog1/schema1/ROW_NUMBER/
------
SQL  EXPLAIN FORMAT=TREE SELECT * FROM t WHERE id = 1;
行为 READ Table(1:34~1:35) /test/1/catalog1/schema1/t/
------
SQL  EXPLAIN splitv97.desc_t;
行为 READ Table(1:8~1:23) /test/1/catalog1/splitv97/desc_t/
------
SQL  SHOW OPEN TABLES FROM split_show97 LIKE 'show%';
行为 READ Instance(1:0~1:47) /test/1/

## PROGRAM_CONTROL

SQL  SIGNAL SQLSTATE VALUE '45000' SET MESSAGE_TEXT = 'split signal', MYSQL_ERRNO = 1644;
行为 PROGRAM_CONTROL
------
SQL  RESIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'split resignal';
行为 PROGRAM_CONTROL
------
SQL  RESIGNAL;
行为 PROGRAM_CONTROL
------
SQL  SIGNAL SQLSTATE '45000' SET CLASS_ORIGIN=0, SUBCLASS_ORIGIN=1.5, MESSAGE_TEXT=1e2, MYSQL_ERRNO=TRUE, CONSTRAINT_CATALOG=FALSE, CONSTRAINT_SCHEMA=0x6162, CONSTRAINT_NAME=b'01100001', CATALOG_NAME=_utf8 0x61, SCHEMA_NAME=DATE '2024-01-01', TABLE_NAME=TIME '12:00:00', COLUMN_NAME=TIMESTAMP '2024-01-01 12:00:00', CURSOR_NAME=NULL;
行为 PROGRAM_CONTROL
------
SQL  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT=@signal_user_message, CLASS_ORIGIN=@@version;
行为 READ ConfigKey(1:41~1:61) /test/1/signal_user_message/
行为 READ ConfigKey(1:76~1:85) /test/1/version/
------
SQL  SIGNAL SQLSTATE '45000' SET CLASS_ORIGIN=0, SUBCLASS_ORIGIN=1.5, MESSAGE_TEXT=1e2, MYSQL_ERRNO=TRUE, CONSTRAINT_CATALOG=FALSE, CONSTRAINT_SCHEMA=0x6162, CONSTRAINT_NAME=b'01100001', CATALOG_NAME=_utf8mb4 0x61, SCHEMA_NAME=DATE '2024-01-01', TABLE_NAME=TIME '12:00:00', COLUMN_NAME=TIMESTAMP '2024-01-01 12:00:00', CURSOR_NAME=NULL;
行为 PROGRAM_CONTROL

## DROP_SCHEMA

SQL  DROP database AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA;
行为 DROP Schema(1:14~1:204) /test/1/catalog1/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/
------
SQL  DROP DATABASE IF EXISTS split_db56;
行为 DROP Schema(1:24~1:34) /test/1/catalog1/split_db56/
------
SQL  DROP SCHEMA IF EXISTS split_db56_schema;
行为 DROP Schema(1:22~1:39) /test/1/catalog1/split_db56_schema/
------
SQL  DROP DATABASE split_db56;
行为 DROP Schema(1:14~1:24) /test/1/catalog1/split_db56/
------
SQL  DROP DATABASE IF EXISTS split_db57;
行为 DROP Schema(1:24~1:34) /test/1/catalog1/split_db57/
------
SQL  DROP SCHEMA IF EXISTS split_db57_schema;
行为 DROP Schema(1:22~1:39) /test/1/catalog1/split_db57_schema/
------
SQL  DROP DATABASE split_db57;
行为 DROP Schema(1:14~1:24) /test/1/catalog1/split_db57/
------
SQL  DROP DATABASE split_db80_modern;
行为 DROP Schema(1:14~1:31) /test/1/catalog1/split_db80_modern/
------
SQL  drop database abc;
行为 DROP Schema(1:14~1:17) /test/1/catalog1/abc/
------
SQL  drop database if exists abc;
行为 DROP Schema(1:24~1:27) /test/1/catalog1/abc/
------
SQL  drop schema if exists abc;
行为 DROP Schema(1:22~1:25) /test/1/catalog1/abc/
------
SQL  drop database test;
行为 DROP Schema(1:14~1:18) /test/1/catalog1/test/
------
SQL  drop database if exists test;
行为 DROP Schema(1:24~1:28) /test/1/catalog1/test/
------
SQL  drop schema test_schema;
行为 DROP Schema(1:12~1:23) /test/1/catalog1/test_schema/
------
SQL  DROP schema sampledb;
行为 DROP Schema(1:12~1:20) /test/1/catalog1/sampledb/
------
SQL  DROP DATABASE IF EXISTS split84_db_a;
行为 DROP Schema(1:24~1:36) /test/1/catalog1/split84_db_a/
------
SQL  DROP SCHEMA IF EXISTS split84_db_b;
行为 DROP Schema(1:22~1:34) /test/1/catalog1/split84_db_b/
------
SQL  DROP DATABASE split84_db_a;
行为 DROP Schema(1:14~1:26) /test/1/catalog1/split84_db_a/
------
SQL  DROP DATABASE IF EXISTS split_db97;
行为 DROP Schema(1:24~1:34) /test/1/catalog1/split_db97/
------
SQL  DROP SCHEMA IF EXISTS split_db97_schema;
行为 DROP Schema(1:22~1:39) /test/1/catalog1/split_db97_schema/
------
SQL  DROP DATABASE split_db97;
行为 DROP Schema(1:14~1:24) /test/1/catalog1/split_db97/
------
SQL  DROP DATABASE split_db97_modern;
行为 DROP Schema(1:14~1:31) /test/1/catalog1/split_db97_modern/

## CREATE_TABLE

SQL  CREATE TABLE audit_t (c CHAR(5) ASCII BINARY);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c CHAR(5) BINARY ASCII);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c CHAR(5) BINARY CHARACTER SET latin1);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c CHAR(5) CHARACTER SET latin1 BINARY);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c INT SIGNED ZEROFILL);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c INT ZEROFILL UNSIGNED);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c TIMESTAMP(7));
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE audit_t (c YEAR(2));
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE type_alias_matrix (c_int INT, c_int1 INT1, c_int2 INT2, c_int3 INT3, c_int4 INT4, c_int8 INT8, c_middle MIDDLEINT, c_dec DEC(10,2), c_float4 FLOAT4, c_float8 FLOAT8, c_long LONG, c_char_byte CHAR BYTE, c_nat_varchar NATIONAL VARCHAR(20), c_nchar_varchar NCHAR VARCHAR(20), c_nat_char_varying NATIONAL CHAR VARYING(20), c_year YEAR);
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/type_alias_matrix/
------
SQL  CREATE TABLE type_literal_defaults (\n c_neg INT DEFAULT -1,\n c_unsigned BIGINT UNSIGNED DEFAULT 0,\n c_decimal DECIMAL(10,2) DEFAULT 0.00,\n c_float DOUBLE DEFAULT 1.25,\n c_bit BIT(4) DEFAULT b'1010',\n c_hex BINARY(2) DEFAULT X'4142',\n c_empty VARCHAR(10) DEFAULT '',\n c_char CHAR(1) DEFAULT 'x',\n c_bool BOOLEAN DEFAULT TRUE,\n c_date DATE DEFAULT '2020-01-02',\n c_time TIME(6) DEFAULT '12:34:56.123456',\n c_datetime DATETIME(6) DEFAULT '2020-01-02 12:34:56.123456',\n c_timestamp TIMESTAMP(6) NULL DEFAULT '2020-01-02 12:34:56.123456',\n c_year YEAR DEFAULT 2020,\n c_enum ENUM('a','b') DEFAULT 'b',\n c_set SET('x','y') DEFAULT 'x,y',\n c_nullable INT DEFAULT NULL\n);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/type_literal_defaults/
------
SQL  CREATE TABLE type_numeric_forms (\n c_bit_default BIT,\n c_bit_max BIT(64),\n c_tiny_plain TINYINT,\n c_small_width SMALLINT(5) UNSIGNED,\n c_medium_zero MEDIUMINT(8) ZEROFILL,\n c_int_signed INT(10) SIGNED,\n c_big_zero BIGINT(20) UNSIGNED ZEROFILL,\n c_decimal_default DECIMAL,\n c_decimal_precision DECIMAL(12),\n c_numeric_signed NUMERIC(20,4) SIGNED,\n c_float_default FLOAT,\n c_float_scale FLOAT(10,3) ZEROFILL,\n c_float_precision FLOAT(53) SIGNED,\n c_double_default DOUBLE,\n c_double_scale DOUBLE(30,10) UNSIGNED,\n c_real_default REAL,\n c_real_scale REAL(12,4) ZEROFILL\n);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/type_numeric_forms/
------
SQL  CREATE TABLE type_num_common (\n c_tiny TINYINT(3) UNSIGNED ZEROFILL,\n c_small SMALLINT SIGNED,\n c_medium MEDIUMINT,\n c_int INTEGER(11),\n c_big BIGINT UNSIGNED,\n c_dec DECIMAL(20,6) UNSIGNED ZEROFILL,\n c_num NUMERIC(10),\n c_fixed FIXED(12,2),\n c_float FLOAT(24) UNSIGNED,\n c_double DOUBLE PRECISION(20,5) ZEROFILL,\n c_real REAL(10,3),\n c_bit BIT(8),\n c_bool BOOL,\n c_boolean BOOLEAN,\n c_serial SERIAL\n);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/type_num_common/
------
SQL  CREATE TABLE type_temporal_common (c_date DATE, c_time TIME(6), c_datetime DATETIME(6), c_timestamp TIMESTAMP(6), c_year YEAR(4));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/type_temporal_common/
------
SQL  CREATE TABLE type_year2 (c_year YEAR(2));
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/type_year2/
------
SQL  CREATE TABLE type_float_ai (id FLOAT AUTO_INCREMENT PRIMARY KEY);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/type_float_ai/
行为 CREATE Constraint(1:52~1:63) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE type_spatial_standard (c_geom GEOMETRY, c_point POINT, c_linestring LINESTRING, c_polygon POLYGON, c_multipoint MULTIPOINT, c_multiline MULTILINESTRING, c_multipolygon MULTIPOLYGON, c_collection GEOMETRYCOLLECTION);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/type_spatial_standard/
------
SQL  CREATE TABLE type_string_common (\n c_char CHAR(10) BINARY,\n c_character CHARACTER(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,\n c_varchar VARCHAR(20),\n c_char_varying CHARACTER VARYING(20),\n c_nchar NCHAR(10),\n c_national NATIONAL CHARACTER(10),\n c_national_varying NATIONAL CHARACTER VARYING(20),\n c_nvarchar NVARCHAR(20),\n c_binary BINARY(8),\n c_varbinary VARBINARY(16),\n c_tinyblob TINYBLOB,\n c_blob BLOB(100),\n c_mediumblob MEDIUMBLOB,\n c_longblob LONGBLOB,\n c_tinytext TINYTEXT,\n c_text TEXT(100) BINARY,\n c_mediumtext MEDIUMTEXT,\n c_longtext LONGTEXT,\n c_enum ENUM('a','b') CHARACTER SET utf8mb4,\n c_set SET('x','y') BINARY,\n c_long_varchar LONG VARCHAR,\n c_long_varbinary LONG VARBINARY\n);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/type_string_common/
------
SQL  CREATE TABLE type_string_compat (\n c_char_varying CHAR VARYING(10),\n c_nchar_varying NCHAR VARYING(10),\n c_long_ascii LONG ASCII,\n c_long_varchar_unicode LONG VARCHAR UNICODE COLLATE ucs2_general_ci,\n c_tinytext_ascii TINYTEXT ASCII COLLATE latin1_bin,\n c_mediumtext_unicode MEDIUMTEXT UNICODE COLLATE ucs2_general_ci,\n c_longtext_charset LONGTEXT CHARSET latin1 COLLATE latin1_general_cs,\n c_binary_default BINARY\n);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/type_string_compat/
------
SQL  CREATE TABLE type_string_forms (\n c_char_default CHAR,\n c_char_zero CHAR(0),\n c_char_ascii CHAR(5) ASCII,\n c_varchar_ascii VARCHAR(10) ASCII,\n c_varchar_unicode VARCHAR(10) UNICODE,\n c_varchar_charset VARCHAR(10) CHARSET latin1 COLLATE latin1_bin,\n c_varchar_binary_charset VARCHAR(10) CHARACTER SET binary,\n c_text_ascii TEXT ASCII,\n c_text_charset TEXT CHARACTER SET latin1 COLLATE latin1_general_cs,\n c_enum_collate ENUM('a','b') CHARSET latin1 COLLATE latin1_bin,\n c_set_collate SET('x','y') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,\n c_national_collate NATIONAL CHAR(5) COLLATE utf8_general_ci\n);
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/type_string_forms/
------
SQL  CREATE TABLE type_temporal_forms (\n c_date DATE,\n c_time_default TIME,\n c_time_zero TIME(0),\n c_datetime_default DATETIME,\n c_datetime_zero DATETIME(0),\n c_timestamp_default TIMESTAMP NULL,\n c_timestamp_zero TIMESTAMP(0) NULL,\n c_year_default YEAR,\n c_year_four YEAR(4)\n);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/type_temporal_forms/
------
SQL  CREATE TEMPORARY TABLE t_varcharacter (c VARCHARACTER(10));
行为 CREATE Table(1:23~1:37) /test/1/catalog1/schema1/t_varcharacter/
------
SQL  CREATE TABLE codex_year_options(a YEAR SIGNED,b YEAR ZEROFILL,c YEAR UNSIGNED ZEROFILL,d YEAR ZEROFILL UNSIGNED);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/codex_year_options/
------
SQL  CREATE TABLE codex_decimal_scale_gt(a_dec DECIMAL(0,11));
行为 CREATE Table(1:13~1:35) /test/1/catalog1/schema1/codex_decimal_scale_gt/
------
SQL  CREATE TABLE codex_decimal_precision_64(d DECIMAL(64,0));
行为 CREATE Table(1:13~1:39) /test/1/catalog1/schema1/codex_decimal_precision_64/
------
SQL  CREATE TABLE codex_decimal_precision_66(d DECIMAL(66,0));
行为 CREATE Table(1:13~1:39) /test/1/catalog1/schema1/codex_decimal_precision_66/
------
SQL  CREATE TABLE t1 (c1 DATE DEFAULT 0);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE bit_65(a BIT(65));
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/bit_65/
------
SQL  CREATE TABLE bit_0(a BIT(0));
行为 CREATE Table(1:13~1:18) /test/1/catalog1/schema1/bit_0/
------
SQL  CREATE TABLE bit_defaults(f1 BIT(2) NOT NULL DEFAULT b'10',f2 BIT(14) NOT NULL DEFAULT b'11110000111100') ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/bit_defaults/
------
SQL  CREATE TABLE bit_partition(id INT,b BIT(8),PRIMARY KEY(id,b)) PARTITION BY HASH(b) PARTITIONS 2;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/bit_partition/
行为 CREATE Constraint(1:43~1:60) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE time_fsp_7(t TIME(7));
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/time_fsp_7/
------
SQL  CREATE TABLE time_partition_range(id INT,t TIME(6)) PARTITION BY RANGE COLUMNS(t)(PARTITION p_negative VALUES LESS THAN(TIME'00:00:00.000000'),PARTITION p_positive VALUES LESS THAN(MAXVALUE));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/time_partition_range/
------
SQL  CREATE TABLE datetime_fsp_7(dt DATETIME(7));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/datetime_fsp_7/
------
SQL  CREATE TABLE str_char_edges (c0 CHAR(0), c1 CHAR, c255 CHAR(255));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/str_char_edges/
------
SQL  CREATE TABLE str_varchar_edges (v0 VARCHAR(0), v254 VARCHAR(254), v255 VARCHAR(255), v256 VARCHAR(256), v300 VARCHAR(300));
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/str_varchar_edges/
------
SQL  CREATE TABLE str_varchar_max (v VARCHAR(65535)) CHARACTER SET latin1;
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/str_varchar_max/
------
SQL  CREATE TABLE str_binary_edges (b0 BINARY(0), b1 BINARY, b255 BINARY(255));
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/str_binary_edges/
------
SQL  CREATE TABLE str_varbinary_edges (v0 VARBINARY(0), v255 VARBINARY(255), v256 VARBINARY(256));
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/str_varbinary_edges/
------
SQL  CREATE TABLE str_varbinary_max (v VARBINARY(65535));
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/str_varbinary_max/
------
SQL  CREATE TABLE str_string_attribute_edges (\n  v_binary VARCHAR(16) BINARY,\n  v_binary_ascii VARCHAR(16) BINARY ASCII,\n  c_unicode CHAR(8) UNICODE,\n  n_varying NCHAR VARYING(16)\n);
行为 CREATE Table(1:13~1:39) /test/1/catalog1/schema1/str_string_attribute_edges/
------
SQL  CREATE TABLE str_lifecycle (v VARCHAR(10), c CHAR(10), b BINARY(10), vb VARBINARY(10), t TEXT);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  CREATE TABLE str_char_over_limit (c CHAR(256));
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/str_char_over_limit/
------
SQL  CREATE TABLE str_varchar_over_limit (v VARCHAR(65536));
行为 CREATE Table(1:13~1:35) /test/1/catalog1/schema1/str_varchar_over_limit/
------
SQL  CREATE TABLE str_binary_over_limit (b BINARY(256));
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/str_binary_over_limit/
------
SQL  CREATE TABLE str_varbinary_over_limit (v VARBINARY(65536));
行为 CREATE Table(1:13~1:37) /test/1/catalog1/schema1/str_varbinary_over_limit/
------
SQL  CREATE TABLE lob_family (\n  tb TINYBLOB,\n  b BLOB,\n  mb MEDIUMBLOB,\n  lb LONGBLOB,\n  tt TINYTEXT,\n  t TEXT,\n  mt MEDIUMTEXT,\n  lt LONGTEXT\n);
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/lob_family/
------
SQL  CREATE TABLE blob_length_mapping (\n  b0 BLOB(0),\n  b255 BLOB(255),\n  b256 BLOB(256),\n  b65535 BLOB(65535),\n  b65536 BLOB(65536),\n  b16777215 BLOB(16777215),\n  b16777216 BLOB(16777216)\n);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/blob_length_mapping/
------
SQL  CREATE TABLE text_length_mapping (\n  t0 TEXT(0),\n  t255 TEXT(255),\n  t256 TEXT(256),\n  t65535 TEXT(65535),\n  t65536 TEXT(65536),\n  t16777215 TEXT(16777215),\n  t16777216 TEXT(16777216)\n) CHARACTER SET latin1;
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/text_length_mapping/
------
SQL  CREATE TABLE lob_lifecycle (\n  b BLOB,\n  t TEXT CHARACTER SET latin1,\n  mb MEDIUMBLOB,\n  mt MEDIUMTEXT CHARACTER SET latin1\n);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/lob_lifecycle/
------
SQL  CREATE TABLE split_type_enum_set.es_binary (\n      e_binary ENUM('a','A') BINARY,\n      e_byte ENUM(b'01100001',b'01000001') BYTE,\n      s_binary SET('a','A') BINARY,\n      s_byte SET(b'01100001',b'01000001') BYTE\n    );
行为 CREATE Table(1:13~1:42) /test/1/catalog1/split_type_enum_set/es_binary/
------
SQL  CREATE TABLE split_type_enum_set.es_hex (\n      e_hex ENUM(x'61',x'62'),\n      s_hex SET(x'61',x'62')\n    );
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_type_enum_set/es_hex/
------
SQL  CREATE TABLE split_type_enum_set.es_duplicate (\n      e ENUM('a','a','b'),\n      s SET('x','x','y')\n    );
行为 CREATE Table(1:13~1:45) /test/1/catalog1/split_type_enum_set/es_duplicate/
------
SQL  CREATE TABLE split_type_enum_set.es_comma (s SET('a,b','c'));
行为 CREATE Table(1:13~1:41) /test/1/catalog1/split_type_enum_set/es_comma/
------
SQL  CREATE TABLE split_type_enum_set.es_attributes (\n      e_ascii ENUM('yes','no') ASCII,\n      e_unicode ENUM('yes','no') UNICODE,\n      s_ascii SET('x','y') ASCII,\n      s_unicode SET('x','y') UNICODE,\n      e_collated ENUM('Y','N') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,\n      s_collated SET('Y','N') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin\n    );
行为 CREATE Table(1:13~1:46) /test/1/catalog1/split_type_enum_set/es_attributes/
------
SQL  CREATE TABLE split_type_enum_set.es_set_64 (\n      s SET(\n        'm01','m02','m03','m04','m05','m06','m07','m08',\n        'm09','m10','m11','m12','m13','m14','m15','m16',\n        'm17','m18','m19','m20','m21','m22','m23','m24',\n        'm25','m26','m27','m28','m29','m30','m31','m32',\n        'm33','m34','m35','m36','m37','m38','m39','m40',\n        'm41','m42','m43','m44','m45','m46','m47','m48',\n        'm49','m50','m51','m52','m53','m54','m55','m56',\n        'm57','m58','m59','m60','m61','m62','m63','m64'\n      )\n    );
行为 CREATE Table(1:13~1:42) /test/1/catalog1/split_type_enum_set/es_set_64/
------
SQL  CREATE TABLE split_type_enum_set.es_set_65 (\n      s SET(\n        'm01','m02','m03','m04','m05','m06','m07','m08',\n        'm09','m10','m11','m12','m13','m14','m15','m16',\n        'm17','m18','m19','m20','m21','m22','m23','m24',\n        'm25','m26','m27','m28','m29','m30','m31','m32',\n        'm33','m34','m35','m36','m37','m38','m39','m40',\n        'm41','m42','m43','m44','m45','m46','m47','m48',\n        'm49','m50','m51','m52','m53','m54','m55','m56',\n        'm57','m58','m59','m60','m61','m62','m63','m64','m65'\n      )\n    );
行为 CREATE Table(1:13~1:42) /test/1/catalog1/split_type_enum_set/es_set_65/
------
SQL  CREATE TABLE split_type_enum_set.es_enum_255 (e ENUM('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'));
行为 CREATE Table(1:13~1:44) /test/1/catalog1/split_type_enum_set/es_enum_255/
------
SQL  CREATE TABLE split_type_enum_set.es_enum_256 (e ENUM('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'));
行为 CREATE Table(1:13~1:44) /test/1/catalog1/split_type_enum_set/es_enum_256/
------
SQL  CREATE TABLE split_type_enum_set.es_enum_pk (\n      e ENUM('zero','one','two') PRIMARY KEY,\n      payload INT\n    );
行为 CREATE Table(1:13~1:43) /test/1/catalog1/split_type_enum_set/es_enum_pk/
行为 CREATE Constraint(2:33~2:44) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE double_auto_increment (\n  id DOUBLE AUTO_INCREMENT PRIMARY KEY,\n  payload DECIMAL(10,2)\n);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/double_auto_increment/
行为 CREATE Constraint(2:27~2:38) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE float_precision_edges (\n  f0 FLOAT(0),\n  f23 FLOAT(23),\n  f24 FLOAT(24),\n  f25 FLOAT(25),\n  f53 FLOAT(53),\n  f54 FLOAT(54)\n);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/float_precision_edges/
------
SQL  CREATE TABLE approximate_precision_edges (\n  f_wide FLOAT(200,100),\n  d_wide DOUBLE(200,100),\n  r_wide REAL(200,100)\n);
行为 CREATE Table(1:13~1:40) /test/1/catalog1/schema1/approximate_precision_edges/
------
SQL  CREATE TABLE `semi;table` (`semi;column` INT);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/semi;table/
------
SQL  CREATE TABLE split_numid (1e INT);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/split_numid/
------
SQL  CREATE TABLE dollar_column_a ($one$two INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/dollar_column_a/
------
SQL  CREATE TABLE dollar_column_b ($$id INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/dollar_column_b/
------
SQL  CREATE TABLE dollar_column_c ($$alpha$$ INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/dollar_column_c/
------
SQL  CREATE TABLE dollar_column_d ($tag$alpha$tag$ INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/dollar_column_d/
------
SQL  CREATE TABLE dollar_column_e ($$alpha INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/dollar_column_e/
------
SQL  CREATE TABLE dollar_column_f ($tag$alpha$other$ INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/dollar_column_f/
------
SQL  CREATE TABLE dollar_digit.$1$ (id INT);
行为 CREATE Table(1:13~1:29) /test/1/catalog1/dollar_digit/$1$/
------
SQL  CREATE TABLE dollar_ctx.$$id (id INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/dollar_ctx/$$id/
------
SQL  CREATE TABLE dollar_ctx.$$alpha$$ (id INT);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/dollar_ctx/$$alpha$$/
------
SQL  CREATE TABLE dollar_ctx.$tag$alpha$tag$ (id INT);
行为 CREATE Table(1:13~1:39) /test/1/catalog1/dollar_ctx/$tag$alpha$tag$/
------
SQL  CREATE TABLE dollar_ctx.$$alpha (id INT);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/dollar_ctx/$$alpha/
------
SQL  CREATE TABLE dollar_ctx.$tag$alpha$other$ (id INT);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/dollar_ctx/$tag$alpha$other$/
------
SQL  CREATE TABLE $one$two (id INT);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/$one$two/
------
SQL  CREATE TABLE $$id (id INT);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/$$id/
------
SQL  CREATE TABLE $$alpha$$ (id INT);
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/$$alpha$$/
------
SQL  CREATE TABLE $tag$alpha$tag$ (id INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/$tag$alpha$tag$/
------
SQL  CREATE TABLE $$alpha (id INT);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/$$alpha/
------
SQL  CREATE TABLE $tag$alpha$other$ (id INT);
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/$tag$alpha$other$/
------
SQL  /*!50000 CREATE TABLE t (id INT PRIMARY KEY, note VARCHAR(20)) */;
行为 CREATE Table(1:22~1:23) /test/1/catalog1/schema1/t/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE JSON_ARRAYAGG(a INT);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE TABLE JSON_OBJECTAGG(a INT);
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  CREATE TABLE JSON_DUALITY_OBJECT(a INT);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE TABLE JSON_ARRAYAGG (a INT);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/JSON_ARRAYAGG/
------
SQL  CREATE TABLE JSON_OBJECTAGG (a INT);
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/JSON_OBJECTAGG/
------
SQL  CREATE TABLE JSON_DUALITY_OBJECT (a INT);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/JSON_DUALITY_OBJECT/
------
SQL  CREATE TABLE function_token_gap.JSON_ARRAYAGG(a INT);
行为 CREATE Table(1:13~1:45) /test/1/catalog1/function_token_gap/JSON_ARRAYAGG/
------
SQL  CREATE TABLE function_token_gap.JSON_OBJECTAGG(a INT);
行为 CREATE Table(1:13~1:46) /test/1/catalog1/function_token_gap/JSON_OBJECTAGG/
------
SQL  CREATE TABLE function_token_gap.JSON_DUALITY_OBJECT(a INT);
行为 CREATE Table(1:13~1:51) /test/1/catalog1/function_token_gap/JSON_DUALITY_OBJECT/
------
SQL  CREATE TABLE split_identifiers.1table (1column INT);
行为 CREATE Table(1:13~1:37) /test/1/catalog1/split_identifiers/1table/
------
SQL  CREATE TABLE split_identifiers.$one (id INT);
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_identifiers/$one/
------
SQL  CREATE TABLE split_identifiers.$one$two (id INT);
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_identifiers/$one$two/
------
SQL  CREATE TABLE audit_ident_cube (cube INT);
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/audit_ident_cube/
------
SQL  CREATE TABLE split_keywords.keyword_identifiers (cume_dist INT, dense_rank INT, empty INT, except INT, first_value INT, grouping INT, groups INT, json_table INT, lag INT, last_value INT, lateral INT, lead INT, nth_value INT, ntile INT, of INT, over INT, percent_rank INT, rank INT, recursive INT, row_number INT, system INT, window INT, manual INT, parallel INT, qualify INT, tablesample INT, external INT, library INT, generated INT, optimizer_costs INT, stored INT, virtual INT, intersect INT, function INT, row INT, rows INT);
行为 CREATE Table(1:13~1:47) /test/1/catalog1/split_keywords/keyword_identifiers/
------
SQL  CREATE TABLE split_keywords.files (absent INT, allow_missing_files INT, auto_refresh INT, auto_refresh_source INT, duality INT, external_format INT, file_format INT, file_name INT, file_pattern INT, file_prefix INT, guided INT, header INT, masking INT, materialized INT, parameters INT, policy INT, relational INT, sets INT, strict_load INT, uri INT, validate INT, vector INT, verify_key_constraints INT);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/split_keywords/files/
------
SQL  CREATE TABLE t1 (.i INT);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE .t2 (i INT);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/t2/
------
SQL  CREATE TABLE full(i INT);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/full/
------
SQL  CREATE TABLE ADDDATE(a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/ADDDATE/
------
SQL  CREATE TABLE ADDDATE (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/ADDDATE/
------
SQL  CREATE TABLE BIT_AND (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/BIT_AND/
------
SQL  CREATE TABLE BIT_OR (a int);
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/BIT_OR/
------
SQL  CREATE TABLE BIT_XOR (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/BIT_XOR/
------
SQL  CREATE TABLE CAST (a int);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE CURDATE (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/CURDATE/
------
SQL  CREATE TABLE CURTIME (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/CURTIME/
------
SQL  CREATE TABLE DATE_ADD (a int);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/DATE_ADD/
------
SQL  CREATE TABLE DATE_SUB (a int);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/DATE_SUB/
------
SQL  CREATE TABLE EXTRACT (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/EXTRACT/
------
SQL  CREATE TABLE GROUP_CONCAT (a int);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/GROUP_CONCAT/
------
SQL  CREATE TABLE GROUP_UNIQUE_USERS(a int);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/GROUP_UNIQUE_USERS/
------
SQL  CREATE TABLE GROUP_UNIQUE_USERS (a int);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/GROUP_UNIQUE_USERS/
------
SQL  CREATE TABLE MAX (a int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/MAX/
------
SQL  CREATE TABLE MID (a int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/MID/
------
SQL  CREATE TABLE MIN (a int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/MIN/
------
SQL  CREATE TABLE NOW (a int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/NOW/
------
SQL  CREATE TABLE POSITION (a int);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/POSITION/
------
SQL  CREATE TABLE SESSION_USER(a int);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/SESSION_USER/
------
SQL  CREATE TABLE SESSION_USER (a int);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/SESSION_USER/
------
SQL  CREATE TABLE STD (a int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/STD/
------
SQL  CREATE TABLE STDDEV (a int);
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/STDDEV/
------
SQL  CREATE TABLE STDDEV_POP (a int);
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/STDDEV_POP/
------
SQL  CREATE TABLE STDDEV_SAMP (a int);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/STDDEV_SAMP/
------
SQL  CREATE TABLE SUBDATE(a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/SUBDATE/
------
SQL  CREATE TABLE SUBDATE (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/SUBDATE/
------
SQL  CREATE TABLE SUBSTR (a int);
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/SUBSTR/
------
SQL  CREATE TABLE SUBSTRING (a int);
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/SUBSTRING/
------
SQL  CREATE TABLE SUM (a int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/SUM/
------
SQL  CREATE TABLE SYSDATE (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/SYSDATE/
------
SQL  CREATE TABLE SYSTEM_USER(a int);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/SYSTEM_USER/
------
SQL  CREATE TABLE SYSTEM_USER (a int);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/SYSTEM_USER/
------
SQL  CREATE TABLE TRIM (a int);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/TRIM/
------
SQL  CREATE TABLE UNIQUE_USERS(a int);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/UNIQUE_USERS/
------
SQL  CREATE TABLE UNIQUE_USERS (a int);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/UNIQUE_USERS/
------
SQL  CREATE TABLE VARIANCE (a int);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/VARIANCE/
------
SQL  CREATE TABLE VAR_POP (a int);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/VAR_POP/
------
SQL  CREATE TABLE VAR_SAMP (a int);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/VAR_SAMP/
------
SQL  CREATE TABLE parse_gcol_expr (i INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/parse_gcol_expr/
------
SQL  CREATE TEMPORARY TABLE admin (admin INT);
行为 CREATE Table(1:23~1:28) /test/1/catalog1/schema1/admin/
------
SQL  CREATE TABLE split_parser_key (i INT KEY);
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/split_parser_key/
行为 CREATE Constraint(1:37~1:40) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_parser_unique (i INT UNIQUE);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/split_parser_unique/
行为 CREATE Constraint(1:40~1:46) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_parser_ansi_a ( "blah" INT );
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/split_parser_ansi_a/
------
SQL  CREATE TABLE get_source_public_key(i INT);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/get_source_public_key/
------
SQL  CREATE TABLE source_auto_position(i INT);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/source_auto_position/
------
SQL  CREATE TABLE source_bind(i INT);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/source_bind/
------
SQL  CREATE TABLE source_compression_algorithm(i INT);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/schema1/source_compression_algorithm/
------
SQL  CREATE TABLE source_connect_retry(i INT);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/source_connect_retry/
------
SQL  CREATE TABLE source_delay(i INT);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/source_delay/
------
SQL  CREATE TABLE source_heartbeat_period(i INT);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/source_heartbeat_period/
------
SQL  CREATE TABLE source_host(i INT);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/source_host/
------
SQL  CREATE TABLE source_log_file(i INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/source_log_file/
------
SQL  CREATE TABLE source_log_pos(i INT);
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/source_log_pos/
------
SQL  CREATE TABLE source_password(i INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/source_password/
------
SQL  CREATE TABLE source_port(i INT);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/source_port/
------
SQL  CREATE TABLE source_public_key_path(i INT);
行为 CREATE Table(1:13~1:35) /test/1/catalog1/schema1/source_public_key_path/
------
SQL  CREATE TABLE source_retry_count(i INT);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/source_retry_count/
------
SQL  CREATE TABLE source_ssl(i INT);
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/source_ssl/
------
SQL  CREATE TABLE source_ssl_ca(i INT);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/source_ssl_ca/
------
SQL  CREATE TABLE source_ssl_capath(i INT);
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/source_ssl_capath/
------
SQL  CREATE TABLE source_ssl_cert(i INT);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/source_ssl_cert/
------
SQL  CREATE TABLE source_ssl_cipher(i INT);
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/source_ssl_cipher/
------
SQL  CREATE TABLE source_ssl_crl(i INT);
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/source_ssl_crl/
------
SQL  CREATE TABLE source_ssl_crlpath(i INT);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/source_ssl_crlpath/
------
SQL  CREATE TABLE source_ssl_key(i INT);
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/source_ssl_key/
------
SQL  CREATE TABLE source_ssl_verify_server_cert(i INT);
行为 CREATE Table(1:13~1:42) /test/1/catalog1/schema1/source_ssl_verify_server_cert/
------
SQL  CREATE TABLE source_tls_ciphersuites(i INT);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/source_tls_ciphersuites/
------
SQL  CREATE TABLE source_tls_version(i INT);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/source_tls_version/
------
SQL  CREATE TABLE source_user(i INT);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/source_user/
------
SQL  CREATE TABLE source_zstd_compression_level(i INT);
行为 CREATE Table(1:13~1:42) /test/1/catalog1/schema1/source_zstd_compression_level/
------
SQL  CREATE TABLE t0 (skip INT, locked INT, nowait INT);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t0/
------
SQL  CREATE TABLE keyword_temporal (time TIME,date DATE,timestamp TIMESTAMP,quarter INT,week INT,year INT,timestampadd INT,timestampdiff INT);
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/keyword_temporal/
------
SQL  CREATE TABLE slow (slow INT,general INT,master_heartbeat_period INT,ignore_server_ids INT);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/slow/
------
SQL  CREATE TABLE slow (slow INT,general INT,source_heartbeat_period INT,ignore_server_ids INT);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/slow/
------
SQL  CREATE TABLE binlog (binlog INT);
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/binlog/
------
SQL  CREATE TABLE sets (col1 INT);
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/sets/
------
SQL  CREATE TABLE syntax_audit.c_hash_defs (id INT) ENGINE=InnoDB PARTITION BY HASH(id) PARTITIONS 2 (PARTITION h0, PARTITION h1);
行为 CREATE Table(1:13~1:37) /test/1/catalog1/syntax_audit/c_hash_defs/
------
SQL  CREATE TABLE syntax_audit.c_prefix_key (a VARCHAR(20), b INT, PRIMARY KEY(a(5),b)) ENGINE=InnoDB PARTITION BY KEY() PARTITIONS 2;
行为 CREATE Table(1:13~1:38) /test/1/catalog1/syntax_audit/c_prefix_key/
行为 CREATE Constraint(1:62~1:81) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE syntax_audit.c_range_partitions (id INT) ENGINE=InnoDB PARTITION BY RANGE(id) PARTITIONS 2 (PARTITION p0 VALUES LESS THAN (10), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:44) /test/1/catalog1/syntax_audit/c_range_partitions/
------
SQL  CREATE TABLE syntax_audit.c_sub_hash_non_linear (id INT, grp INT) ENGINE=InnoDB PARTITION BY RANGE(id) SUBPARTITION BY HASH(grp) SUBPARTITIONS 2 (PARTITION p0 VALUES LESS THAN (10), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:47) /test/1/catalog1/syntax_audit/c_sub_hash_non_linear/
------
SQL  CREATE TABLE split_partition.p_range (id INT, d DATE) ENGINE=InnoDB PARTITION BY RANGE (YEAR(d)) (PARTITION p0 VALUES LESS THAN (2020), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/split_partition/p_range/
行为 CALL Function(1:88~1:92) /test/1/catalog1/schema1/YEAR/
------
SQL  CREATE TABLE split_partition.p_range_cols (a INT, b INT) ENGINE=InnoDB PARTITION BY RANGE COLUMNS(a,b) (PARTITION p0 VALUES LESS THAN (10,10), PARTITION p1 VALUES LESS THAN (MAXVALUE,MAXVALUE));
行为 CREATE Table(1:13~1:41) /test/1/catalog1/split_partition/p_range_cols/
------
SQL  CREATE TABLE split_partition.p_list (id INT) ENGINE=InnoDB PARTITION BY LIST (id) (PARTITION p0 VALUES IN (1,3,5), PARTITION p1 VALUES IN (2,4,6));
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_partition/p_list/
------
SQL  CREATE TABLE split_partition.p_list_cols (a INT, b VARCHAR(8)) ENGINE=InnoDB PARTITION BY LIST COLUMNS(a,b) (PARTITION p0 VALUES IN ((1,'a'),(2,'b')), PARTITION p1 VALUES IN ((3,'c'),(NULL,NULL)));
行为 CREATE Table(1:13~1:40) /test/1/catalog1/split_partition/p_list_cols/
------
SQL  CREATE TABLE split_partition.p_hash (id INT) ENGINE=InnoDB PARTITION BY LINEAR HASH(id) PARTITIONS 4;
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_partition/p_hash/
------
SQL  CREATE TABLE split_partition.p_key (id INT, code VARCHAR(8)) ENGINE=InnoDB PARTITION BY LINEAR KEY ALGORITHM=1 (id,code) PARTITIONS 4;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/split_partition/p_key/
------
SQL  CREATE TABLE split_partition.p_key_empty (id INT PRIMARY KEY, code VARCHAR(8)) ENGINE=InnoDB PARTITION BY KEY() PARTITIONS 2;
行为 CREATE Table(1:13~1:40) /test/1/catalog1/split_partition/p_key_empty/
行为 CREATE Constraint(1:49~1:60) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_partition.p_sub_hash (id INT, grp INT) ENGINE=InnoDB PARTITION BY RANGE (id) SUBPARTITION BY LINEAR HASH(grp) SUBPARTITIONS 2 (PARTITION p0 VALUES LESS THAN (10), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_partition/p_sub_hash/
------
SQL  CREATE TABLE split_partition.p_sub_key (id INT, grp INT) ENGINE=InnoDB PARTITION BY LIST (id) SUBPARTITION BY LINEAR KEY ALGORITHM=2 (grp) (PARTITION p0 VALUES IN (1,2) (SUBPARTITION p0s0 ENGINE=InnoDB COMMENT='s0', SUBPARTITION p0s1 ENGINE=InnoDB COMMENT='s1'), PARTITION p1 VALUES IN (3,4) (SUBPARTITION p1s0 ENGINE=InnoDB COMMENT='s2', SUBPARTITION p1s1 ENGINE=InnoDB COMMENT='s3'));
行为 CREATE Table(1:13~1:38) /test/1/catalog1/split_partition/p_sub_key/
------
SQL  CREATE TABLE split_partition.p_opts (id INT) ENGINE=MyISAM PARTITION BY RANGE(id) (PARTITION p0 VALUES LESS THAN (10) ENGINE=MyISAM COMMENT='first' MAX_ROWS=100 MIN_ROWS=1, PARTITION p1 VALUES LESS THAN MAXVALUE ENGINE=MyISAM);
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_partition/p_opts/
------
SQL  CREATE TABLE split_partition.p_storage_engine (id INT) ENGINE=MyISAM PARTITION BY RANGE(id) (PARTITION p0 VALUES LESS THAN (10) STORAGE ENGINE=MyISAM, PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:45) /test/1/catalog1/split_partition/p_storage_engine/
------
SQL  CREATE TABLE split_partition.p_nodegroup (id INT) ENGINE=NDB PARTITION BY KEY(id) (PARTITION p0 NODEGROUP=3);
行为 CREATE Table(1:13~1:40) /test/1/catalog1/split_partition/p_nodegroup/
------
SQL  CREATE TABLE split_partition.p_tablespace (id INT) ENGINE=InnoDB PARTITION BY RANGE(id) (PARTITION p0 VALUES LESS THAN (10) TABLESPACE=ts_missing, PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/split_partition/p_tablespace/
------
SQL  CREATE TABLE split_partition.p_directories (id INT) ENGINE=MyISAM PARTITION BY RANGE(id) (PARTITION p0 VALUES LESS THAN (10) DATA DIRECTORY='/tmp/split_part_data' INDEX DIRECTORY='/tmp/split_part_index' COMMENT='dirs', PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:42) /test/1/catalog1/split_partition/p_directories/
------
SQL  CREATE TABLE subpartitioned(a INT PRIMARY KEY) PARTITION BY RANGE(a) SUBPARTITION BY HASH(a) (PARTITION p0 VALUES LESS THAN(10) (SUBPARTITION sp0 TABLESPACE innodb_file_per_table, SUBPARTITION sp1 TABLESPACE innodb_system));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/subpartitioned/
行为 CREATE Constraint(1:34~1:45) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE datetime_partition_range(id INT,dt DATETIME(6)) PARTITION BY RANGE COLUMNS(dt)(PARTITION p_before VALUES LESS THAN(TIMESTAMP'2000-01-01 00:00:00.000000'),PARTITION p_after VALUES LESS THAN(MAXVALUE));
行为 CREATE Table(1:13~1:37) /test/1/catalog1/schema1/datetime_partition_range/
------
SQL  CREATE TABLE timestamp_partition_range(id INT,ts TIMESTAMP(6) NULL) PARTITION BY RANGE COLUMNS(ts)(PARTITION p_before VALUES LESS THAN(TIMESTAMP'2000-01-01 00:00:00.000000'),PARTITION p_after VALUES LESS THAN(MAXVALUE));
行为 CREATE Table(1:13~1:38) /test/1/catalog1/schema1/timestamp_partition_range/
------
SQL  CREATE TABLE str_partition_binary (a BINARY(32) NOT NULL, PRIMARY KEY(a))\nPARTITION BY KEY(a) (\n  PARTITION p0 MAX_ROWS=20 MIN_ROWS=2,\n  PARTITION p1 MAX_ROWS=30 MIN_ROWS=3\n);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/str_partition_binary/
行为 CREATE Constraint(1:58~1:72) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE str_partition_varbinary (a VARBINARY(64) NOT NULL, PRIMARY KEY(a))\nPARTITION BY LINEAR KEY(a) PARTITIONS 4;
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/str_partition_varbinary/
行为 CREATE Constraint(1:64~1:78) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE str_partition_char (a CHAR(32) NOT NULL, PRIMARY KEY(a))\nPARTITION BY KEY(a) PARTITIONS 2;
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/str_partition_char/
行为 CREATE Constraint(1:54~1:68) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE str_partition_varchar (a VARCHAR(64) NOT NULL, PRIMARY KEY(a))\nPARTITION BY LINEAR KEY ALGORITHM=1(a) PARTITIONS 4;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/str_partition_varchar/
行为 CREATE Constraint(1:60~1:74) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE lob_partition_payload (\n  id INT NOT NULL,\n  binary_payload LONGBLOB,\n  text_payload LONGTEXT,\n  PRIMARY KEY(id)\n)\nPARTITION BY HASH(id) PARTITIONS 2;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/lob_partition_payload/
行为 CREATE Constraint(5:2~5:17) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE lob_partition_payload_range (\n  id INT NOT NULL,\n  binary_payload MEDIUMBLOB,\n  text_payload MEDIUMTEXT,\n  PRIMARY KEY(id)\n)\nPARTITION BY RANGE(id) (\n  PARTITION p0 VALUES LESS THAN (100),\n  PARTITION pmax VALUES LESS THAN MAXVALUE\n);
行为 CREATE Table(1:13~1:40) /test/1/catalog1/schema1/lob_partition_payload_range/
行为 CREATE Constraint(5:2~5:17) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE integer_range_partition (\n  id INT NOT NULL,\n  partition_value MEDIUMINT NOT NULL,\n  tiny_payload TINYINT,\n  small_payload SMALLINT,\n  int_payload INTEGER,\n  big_payload BIGINT,\n  PRIMARY KEY(id,partition_value)\n)\nPARTITION BY RANGE(partition_value) (\n  PARTITION p_negative VALUES LESS THAN (0),\n  PARTITION p_small VALUES LESS THAN (1000),\n  PARTITION p_max VALUES LESS THAN MAXVALUE\n);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/integer_range_partition/
行为 CREATE Constraint(8:2~8:33) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE integer_key_partition (\n  id BIGINT UNSIGNED NOT NULL,\n  partition_value INT UNSIGNED NOT NULL,\n  flag BOOLEAN,\n  PRIMARY KEY(id,partition_value)\n)\nPARTITION BY KEY(partition_value) PARTITIONS 3;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/integer_key_partition/
行为 CREATE Constraint(5:2~5:33) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE numeric_key_partition (\n  id INT NOT NULL,\n  decimal_key DECIMAL(18,9) NOT NULL,\n  numeric_payload NUMERIC(12,4),\n  fixed_payload FIXED(12,2),\n  PRIMARY KEY(id,decimal_key)\n)\nPARTITION BY KEY(decimal_key) PARTITIONS 4;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/numeric_key_partition/
行为 CREATE Constraint(6:2~6:29) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE numeric_range_partition (\n  id INT NOT NULL,\n  decimal_key DECIMAL(18,4) NOT NULL,\n  float_payload FLOAT,\n  double_payload DOUBLE,\n  PRIMARY KEY(id,decimal_key)\n)\nPARTITION BY RANGE(FLOOR(decimal_key))\nSUBPARTITION BY KEY(decimal_key)\nSUBPARTITIONS 2 (\n  PARTITION p_negative VALUES LESS THAN (0),\n  PARTITION p_small VALUES LESS THAN (1000),\n  PARTITION p_max VALUES LESS THAN MAXVALUE\n);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/numeric_range_partition/
行为 CREATE Constraint(6:2~6:29) /test/1/catalog1/schema1/
行为 CALL Function(8:19~8:24) /test/1/catalog1/schema1/FLOOR/
------
SQL  CREATE TABLE approximate_key_partition (\n  id INT NOT NULL,\n  float_key FLOAT NOT NULL,\n  double_key DOUBLE NOT NULL,\n  real_payload REAL,\n  PRIMARY KEY(id,float_key,double_key)\n)\nPARTITION BY KEY(float_key,double_key) PARTITIONS 3;
行为 CREATE Table(1:13~1:38) /test/1/catalog1/schema1/approximate_key_partition/
行为 CREATE Constraint(6:2~6:38) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE p_key_alg(a SERIAL) ENGINE=InnoDB PARTITION BY KEY ALGORITHM=1 () PARTITIONS 3;
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/p_key_alg/
------
SQL  CREATE TABLE IF NOT EXISTS codex_create_audit_ifne_ignore (a INT UNIQUE, b INT) IGNORE SELECT 1 AS a, 1 AS b UNION ALL SELECT 1, 2;
行为 CREATE Table(1:27~1:57) /test/1/catalog1/schema1/codex_create_audit_ifne_ignore/
行为 CREATE Constraint(1:65~1:71) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE IF NOT EXISTS codex_create_audit_ifne_replace (a INT UNIQUE, b INT) REPLACE SELECT 1 AS a, 1 AS b UNION ALL SELECT 1, 2;
行为 CREATE Table(1:27~1:58) /test/1/catalog1/schema1/codex_create_audit_ifne_replace/
行为 CREATE Constraint(1:66~1:72) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE IF NOT EXISTS codex_create_audit_ifne_select (a INT) SELECT 1 AS a;
行为 CREATE Table(1:27~1:57) /test/1/catalog1/schema1/codex_create_audit_ifne_select/
------
SQL  CREATE TABLE codex_create_audit_paren_union (PRIMARY KEY (a)) (SELECT 1 AS a) UNION ALL (SELECT 2 AS a);
行为 CREATE Table(1:13~1:43) /test/1/catalog1/schema1/codex_create_audit_paren_union/
行为 CREATE Constraint(1:45~1:60) /test/1/catalog1/schema1/
------
SQL  CREATE TEMPORARY TABLE IF NOT EXISTS codex_create_audit_temp_ifne_select (a INT) SELECT 1 AS a;
行为 CREATE Table(1:37~1:72) /test/1/catalog1/schema1/codex_create_audit_temp_ifne_select/
------
SQL  CREATE TABLE aes_feedback_modes (a VARCHAR(128), b128 VARCHAR(144), b192 VARCHAR(144), b256 CHAR(144));
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/aes_feedback_modes/
------
SQL  CREATE TABLE aes_cipher_keys (cipher BINARY(16) PRIMARY KEY);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/aes_cipher_keys/
行为 CREATE Constraint(1:48~1:59) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE aes_ecb_modes (a VARCHAR(16), b128 CHAR(16), b192 CHAR(16), b256 CHAR(16));
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/aes_ecb_modes/
------
SQL  CREATE TABLE codex_func_date_add_myisam.history_t (visitor_id int(10) unsigned DEFAULT '0' NOT NULL, group_id int(10) unsigned DEFAULT '0' NOT NULL, hits int(10) unsigned DEFAULT '0' NOT NULL, sessions int(10) unsigned DEFAULT '0' NOT NULL, ts timestamp, PRIMARY KEY (visitor_id,group_id))/*! engine=MyISAM */;
行为 CREATE Table(1:13~1:49) /test/1/catalog1/codex_func_date_add_myisam/history_t/
行为 CREATE Constraint(1:255~1:288) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE t1(id INT,value_col INT);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE t2(id INT,value_col INT);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t2/
------
SQL  CREATE TABLE func_test_type_mix(a DATETIME,b BLOB,c TEXT,d CHAR(10),e BINARY(10),f VARBINARY(10));
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/func_test_type_mix/
------
SQL  CREATE TABLE codex_func_in.ctas_in AS SELECT 1 IN (2,NULL) AS result_value;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/codex_func_in/ctas_in/
------
SQL  CREATE TABLE math_ctas_floor AS SELECT CEILING(CAST(99999999999999999.9 AS DECIMAL(18,1))) AS c, FLOOR(CAST(-99999999999999999.9 AS DECIMAL(18,1))) AS f;
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/math_ctas_floor/
行为 CALL Function(1:39~1:46) /test/1/catalog1/schema1/CEILING/
行为 CALL Function(1:47~1:51) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:97~1:102) /test/1/catalog1/schema1/FLOOR/
------
SQL  CREATE TABLE md5_ctas AS SELECT MD5('a') AS c1;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/md5_ctas/
行为 CALL Function(1:32~1:35) /test/1/catalog1/schema1/MD5/
------
SQL  CREATE TABLE misc_uuid_ctas AS SELECT UUID() AS uuid_value, LENGTH(UUID()) AS uuid_length;
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/misc_uuid_ctas/
行为 CALL Function(1:38~1:42) /test/1/catalog1/schema1/UUID/
行为 CALL Function(1:60~1:66) /test/1/catalog1/schema1/LENGTH/
------
SQL  CREATE TABLE misc_name_const_ctas AS SELECT CONCAT(NAME_CONST('char_col',_cp932'test' COLLATE cp932_japanese_ci),NAME_CONST('num_col',0)) AS a;
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/misc_name_const_ctas/
行为 CALL Function(1:44~1:50) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:51~1:61) /test/1/catalog1/schema1/NAME_CONST/
------
SQL  CREATE TABLE sapdb_time_ctas AS SELECT MAKEDATE(1997,1) AS f1, ADDTIME(CAST('1997-12-31 23:59:59.000001' AS DATETIME),'1 1:1:1.000002') AS f2, ADDTIME(CAST('23:59:59.999999' AS TIME),'1 1:1:1.000002') AS f3, TIMEDIFF('1997-12-31 23:59:59.000001','1997-12-30 01:01:01.000002') AS f4, TIMEDIFF('1997-12-30 23:59:59.000001','1997-12-31 23:59:59.000002') AS f5, MAKETIME(10,11,12) AS f6, TIMESTAMP(CAST('2001-12-01' AS DATE),'01:01:01') AS f7, DATE('1997-12-31 23:59:59.000001') AS f8, TIME('1997-12-31 23:59:59.000001') AS f9;
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/sapdb_time_ctas/
行为 CALL Function(1:39~1:47) /test/1/catalog1/schema1/MAKEDATE/
行为 CALL Function(1:63~1:70) /test/1/catalog1/schema1/ADDTIME/
行为 CALL Function(1:71~1:75) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:208~1:216) /test/1/catalog1/schema1/TIMEDIFF/
行为 CALL Function(1:358~1:366) /test/1/catalog1/schema1/MAKETIME/
行为 CALL Function(1:384~1:393) /test/1/catalog1/schema1/TIMESTAMP/
行为 CALL Function(1:440~1:444) /test/1/catalog1/schema1/DATE/
行为 CALL Function(1:482~1:486) /test/1/catalog1/schema1/TIME/
------
SQL  CREATE TABLE str_function_ctas CHARACTER SET latin1 SELECT BIN(130) AS bin_value, OCT(130) AS oct_value, CONV(130,16,10) AS conv_value, HEX(130) AS hex_value, CHAR(130) AS char_value, FORMAT(130,10) AS format_value, LEFT(_latin2'a',1) AS left_value, RIGHT(_latin2'a',1) AS right_value, LCASE(_latin2'a') AS lower_value, UCASE(_latin2'a') AS upper_value, SUBSTRING(_latin2'a',1,1) AS substring_value, CONCAT(_latin2'a',_latin2'b') AS concat_value, LPAD(_latin2'a',4,_latin2'b') AS lpad_value, RPAD(_latin2'a',4,_latin2'b') AS rpad_value, CONCAT_WS(_latin2'a',_latin2'b') AS concat_ws_value, MAKE_SET(255,_latin2'a',_latin2'b',_latin2'c') AS make_set_value, EXPORT_SET(255,_latin2'y',_latin2'n',_latin2' ') AS export_set_value, TRIM(_latin2' a ') AS trim_value, LTRIM(_latin2' a ') AS ltrim_value, RTRIM(_latin2' a ') AS rtrim_value, REPEAT(_latin2'a',10) AS repeat_value, REVERSE(_latin2'ab') AS reverse_value, QUOTE(_latin2'ab') AS quote_value, SOUNDEX(_latin2'ab') AS soundex_value, SUBSTRING(_latin2'ab',1) AS substring_short_value, INSERT(_latin2'abcd',2,3,_latin2'ef') AS insert_value, REPLACE(_latin2'abcd',_latin2'b',_latin2'B') AS replace_value;
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/str_function_ctas/
行为 CALL Function(1:59~1:62) /test/1/catalog1/schema1/BIN/
行为 CALL Function(1:82~1:85) /test/1/catalog1/schema1/OCT/
行为 CALL Function(1:105~1:109) /test/1/catalog1/schema1/CONV/
行为 CALL Function(1:136~1:139) /test/1/catalog1/schema1/HEX/
行为 CALL Function(1:159~1:163) /test/1/catalog1/schema1/CHAR/
行为 CALL Function(1:184~1:190) /test/1/catalog1/schema1/FORMAT/
行为 CALL Function(1:216~1:220) /test/1/catalog1/schema1/LEFT/
行为 CALL Function(1:250~1:255) /test/1/catalog1/schema1/RIGHT/
行为 CALL Function(1:286~1:291) /test/1/catalog1/schema1/LCASE/
行为 CALL Function(1:320~1:325) /test/1/catalog1/schema1/UCASE/
行为 CALL Function(1:354~1:363) /test/1/catalog1/schema1/SUBSTRING/
行为 CALL Function(1:400~1:406) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:447~1:451) /test/1/catalog1/schema1/LPAD/
行为 CALL Function(1:492~1:496) /test/1/catalog1/schema1/RPAD/
行为 CALL Function(1:537~1:546) /test/1/catalog1/schema1/CONCAT_WS/
行为 CALL Function(1:590~1:598) /test/1/catalog1/schema1/MAKE_SET/
行为 CALL Function(1:656~1:666) /test/1/catalog1/schema1/EXPORT_SET/
行为 CALL Function(1:726~1:730) /test/1/catalog1/schema1/TRIM/
行为 CALL Function(1:760~1:765) /test/1/catalog1/schema1/LTRIM/
行为 CALL Function(1:796~1:801) /test/1/catalog1/schema1/RTRIM/
行为 CALL Function(1:832~1:838) /test/1/catalog1/schema1/REPEAT/
行为 CALL Function(1:871~1:878) /test/1/catalog1/schema1/REVERSE/
行为 CALL Function(1:910~1:915) /test/1/catalog1/schema1/QUOTE/
行为 CALL Function(1:945~1:952) /test/1/catalog1/schema1/SOUNDEX/
行为 CALL Function(1:1035~1:1041) /test/1/catalog1/schema1/INSERT/
行为 CALL Function(1:1090~1:1097) /test/1/catalog1/schema1/REPLACE/
------
SQL  CREATE TABLE str_char_ctas AS SELECT CHAR(0x414243) AS char_value;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/str_char_ctas/
行为 CALL Function(1:37~1:41) /test/1/catalog1/schema1/CHAR/
------
SQL  CREATE TABLE str_null_ctas CHARACTER SET utf8mb4 AS SELECT SUBSTRING('1', DAY(FROM_UNIXTIME(-1))) AS substring_value, LEFT('1', DAY(FROM_UNIXTIME(-1))) AS left_value, RIGHT('1', DAY(FROM_UNIXTIME(-1))) AS right_value, REPEAT('1', DAY(FROM_UNIXTIME(-1))) AS repeat_value, RPAD('hi', DAY(FROM_UNIXTIME(-1)), '?') AS rpad_value, LPAD('hi', DAY(FROM_UNIXTIME(-1)), '?') AS lpad_value;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/str_null_ctas/
行为 CALL Function(1:59~1:68) /test/1/catalog1/schema1/SUBSTRING/
行为 CALL Function(1:74~1:77) /test/1/catalog1/schema1/DAY/
行为 CALL Function(1:78~1:91) /test/1/catalog1/schema1/FROM_UNIXTIME/
行为 CALL Function(1:118~1:122) /test/1/catalog1/schema1/LEFT/
行为 CALL Function(1:167~1:172) /test/1/catalog1/schema1/RIGHT/
行为 CALL Function(1:218~1:224) /test/1/catalog1/schema1/REPEAT/
行为 CALL Function(1:271~1:275) /test/1/catalog1/schema1/RPAD/
行为 CALL Function(1:326~1:330) /test/1/catalog1/schema1/LPAD/
------
SQL  CREATE TABLE str_format_ctas CHARACTER SET utf8mb4 AS SELECT FORMAT(123,2,'no_NO') AS format_value;
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/str_format_ctas/
行为 CALL Function(1:61~1:67) /test/1/catalog1/schema1/FORMAT/
------
SQL  CREATE TABLE str_quote_ctas AS SELECT QUOTE('a') AS quote_value;
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/str_quote_ctas/
行为 CALL Function(1:38~1:43) /test/1/catalog1/schema1/QUOTE/
------
SQL  CREATE TABLE split_func_system.ctas_system (version CHAR(60)) SELECT DATABASE(),USER(),VERSION() AS version;
行为 CREATE Table(1:13~1:42) /test/1/catalog1/split_func_system/ctas_system/
行为 CALL Function(1:69~1:77) /test/1/catalog1/schema1/DATABASE/
行为 CALL Function(1:80~1:84) /test/1/catalog1/schema1/USER/
行为 CALL Function(1:87~1:94) /test/1/catalog1/schema1/VERSION/
------
SQL  CREATE TABLE split_func_system.ctas_charset SELECT CHARSET(_utf8'a'),COLLATION(_utf8'a');
行为 CREATE Table(1:13~1:43) /test/1/catalog1/split_func_system/ctas_charset/
行为 CALL Function(1:51~1:58) /test/1/catalog1/schema1/CHARSET/
行为 CALL Function(1:69~1:78) /test/1/catalog1/schema1/COLLATION/
------
SQL  CREATE TABLE func_test_nullable_self(d VARCHAR(6),k INT);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/func_test_nullable_self/
------
SQL  CREATE TABLE func_test_not_between(a INT,b INT);
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/func_test_not_between/
------
SQL  CREATE TABLE func_test_null_unsigned(c BIGINT NOT NULL);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/func_test_null_unsigned/
------
SQL  CREATE TABLE func_test_unsigned_pair(c1 INT UNSIGNED,c2 INT UNSIGNED);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/func_test_unsigned_pair/
------
SQL  CREATE TABLE func_test_signed_ctas AS SELECT GREATEST(-1,9223372036854775808) AS signed_value,GREATEST(9223372036854775808,9223372036854775808) AS unsigned_value;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/func_test_signed_ctas/
行为 CALL Function(1:45~1:53) /test/1/catalog1/schema1/GREATEST/
------
SQL  CREATE TABLE func_test_type_ctas AS SELECT COALESCE(20010101,DATE '2005-05-05') AS coalesce_date,GREATEST(TIME '20:00:00',120000) AS greatest_time,GREATEST(_utf8mb4 x'c3a5',_binary '1') AS greatest_binary;
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/func_test_type_ctas/
行为 CALL Function(1:43~1:51) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(1:97~1:105) /test/1/catalog1/schema1/GREATEST/
------
SQL  CREATE TABLE to_days_result AS SELECT CAST(TO_DAYS('9999-12-31') AS CHAR) AS x,TO_DAYS('9999-12-31') * -4.0 AS y;
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/to_days_result/
行为 CALL Function(1:38~1:42) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:43~1:50) /test/1/catalog1/schema1/TO_DAYS/
------
SQL  CREATE TABLE now_arithmetic_result AS SELECT IF(0,COALESCE(NULL),NOW(0)) + 0 AS now_number;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/now_arithmetic_result/
行为 CALL Function(1:45~1:47) /test/1/catalog1/schema1/IF/
行为 CALL Function(1:50~1:58) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(1:65~1:68) /test/1/catalog1/schema1/NOW/
------
SQL  CREATE TABLE weight_short_ctas CHARACTER SET latin1 SELECT WEIGHT_STRING('test') AS weight_value;
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/weight_short_ctas/
行为 CALL Function(1:59~1:72) /test/1/catalog1/schema1/WEIGHT_STRING/
------
SQL  CREATE TABLE weight_long_ctas CHARACTER SET latin1 SELECT WEIGHT_STRING(REPEAT('t',66000)) AS weight_value;
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/weight_long_ctas/
行为 CALL Function(1:58~1:71) /test/1/catalog1/schema1/WEIGHT_STRING/
行为 CALL Function(1:72~1:78) /test/1/catalog1/schema1/REPEAT/
------
SQL  CREATE TABLE split_subquery_next.ct SELECT * FROM (SELECT 1 AS a,(SELECT a+0) AS b) AS d;
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_subquery_next/ct/
------
SQL  CREATE TABLE ctas_repeat AS SELECT CONCAT(CAST(REPEAT('9',1000) AS SIGNED)),CONCAT(CAST(REPEAT('9',1000) AS UNSIGNED));
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/ctas_repeat/
行为 CALL Function(1:35~1:41) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:42~1:46) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:47~1:53) /test/1/catalog1/schema1/REPEAT/
------
SQL  CREATE TABLE ctas_overflow AS SELECT CONCAT(CAST(-1 AS UNSIGNED)) AS col1,1.0+CAST(-1 AS UNSIGNED) AS col2,CONCAT(CAST(9223372036854775808 AS SIGNED)) AS col3;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/ctas_overflow/
行为 CALL Function(1:37~1:43) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:44~1:48) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE ctas_nested_using AS SELECT CONVERT(CONVERT(X'F09F8DBA' USING utf8mb4) USING binary) AS beer;
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/ctas_nested_using/
行为 CALL Function(1:41~1:48) /test/1/catalog1/schema1/CONVERT/
------
SQL  CREATE TABLE ctas_mixed_convert AS SELECT CONVERT(CONVERT(X'F09F8DBA',CHAR(1) CHARACTER SET utf8mb4) USING binary) AS beer;
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/ctas_mixed_convert/
行为 CALL Function(1:42~1:49) /test/1/catalog1/schema1/CONVERT/
------
SQL  CREATE TABLE ctas_utf16 AS SELECT CONVERT(X'D83CDF7A' USING utf16) AS beer;
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/ctas_utf16/
行为 CALL Function(1:34~1:41) /test/1/catalog1/schema1/CONVERT/
------
SQL  CREATE TABLE time_literal_ctas AS SELECT TIME'10:20:30.1234567' AS t;
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/time_literal_ctas/
------
SQL  CREATE TABLE str_parents (id BINARY(16) NOT NULL, PRIMARY KEY(id));
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/str_parents/
行为 CREATE Constraint(1:50~1:65) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE case_types AS SELECT CASE WHEN 1 THEN _latin1'a' COLLATE latin1_danish_ci ELSE _latin1'a' END AS c1, CASE WHEN 1 THEN 'a' ELSE 1 END AS c2, CASE WHEN 1 THEN 1.0 ELSE 'a' END AS c3, CASE WHEN 1 THEN 0.1e1 ELSE 1 END AS c4;
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/case_types/
------
SQL  CREATE TABLE codex_ctas_as_paren_union AS (SELECT 1 AS a) UNION ALL (SELECT 2);
行为 CREATE Table(1:13~1:38) /test/1/catalog1/schema1/codex_ctas_as_paren_union/
------
SQL  CREATE TABLE inferred_integer SELECT 000000000000000000000 AS n;
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/inferred_integer/
------
SQL  CREATE TABLE split_storage_memory (c INT STORAGE MEMORY);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/split_storage_memory/
------
SQL  CREATE TABLE fd_default_bit(a BIT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/fd_default_bit/
------
SQL  CREATE TABLE fd_default_tinyint(a TINYINT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/fd_default_tinyint/
------
SQL  CREATE TABLE fd_default_smallint(a SMALLINT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/fd_default_smallint/
------
SQL  CREATE TABLE fd_default_mediumint(a MEDIUMINT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/fd_default_mediumint/
------
SQL  CREATE TABLE fd_default_int(a INT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/fd_default_int/
------
SQL  CREATE TABLE fd_default_bigint(a BIGINT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/fd_default_bigint/
------
SQL  CREATE TABLE fd_default_float(a FLOAT DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/fd_default_float/
------
SQL  CREATE TABLE fd_default_decimal(a DECIMAL DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/fd_default_decimal/
------
SQL  CREATE TABLE fd_default_date(a DATE DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/fd_default_date/
------
SQL  CREATE TABLE fd_default_time(a TIME DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/fd_default_time/
------
SQL  CREATE TABLE fd_default_year(a YEAR DEFAULT CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/fd_default_year/
------
SQL  CREATE TABLE fd_update_bit(a BIT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/fd_update_bit/
------
SQL  CREATE TABLE fd_update_tinyint(a TINYINT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/fd_update_tinyint/
------
SQL  CREATE TABLE fd_update_smallint(a SMALLINT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/fd_update_smallint/
------
SQL  CREATE TABLE fd_update_mediumint(a MEDIUMINT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/fd_update_mediumint/
------
SQL  CREATE TABLE fd_update_int(a INT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/fd_update_int/
------
SQL  CREATE TABLE fd_update_bigint(a BIGINT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/fd_update_bigint/
------
SQL  CREATE TABLE fd_update_float(a FLOAT ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/fd_update_float/
------
SQL  CREATE TABLE fd_update_decimal(a DECIMAL ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/fd_update_decimal/
------
SQL  CREATE TABLE fd_update_date(a DATE ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/fd_update_date/
------
SQL  CREATE TABLE fd_update_time(a TIME ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/fd_update_time/
------
SQL  CREATE TABLE fd_update_year(a YEAR ON UPDATE CURRENT_TIMESTAMP(6));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/fd_update_year/
------
SQL  CREATE TABLE fd_temporal_matrix (\n  a TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),\n  b TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),\n  c TIMESTAMP(6) NOT NULL DEFAULT '2000-01-01 00:00:00.000001' ON UPDATE CURRENT_TIMESTAMP(6),\n  d TIMESTAMP(6) NULL,\n  e DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),\n  f DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),\n  g DATETIME(6) ON UPDATE CURRENT_TIMESTAMP(6),\n  h DATETIME(6)\n);
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/fd_temporal_matrix/
------
SQL  CREATE TABLE count (i INT);
行为 CREATE Table(1:13~1:18) /test/1/catalog1/schema1/count/
------
SQL  CREATE TABLE `count`(i INT);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/count/
------
SQL  CREATE TABLE t1 (f1 INT CHECK (f1 = DEFAULT(f1)));
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
行为 CREATE Constraint(1:24~1:48) /test/1/catalog1/schema1/
行为 CALL Function(1:36~1:43) /test/1/catalog1/schema1/DEFAULT/
------
SQL  CREATE TABLE split_column_format_default (c INT COLUMN_FORMAT DEFAULT);
行为 CREATE Table(1:13~1:40) /test/1/catalog1/schema1/split_column_format_default/
------
SQL  CREATE TABLE split_column_format_dynamic (c INT COLUMN_FORMAT DYNAMIC);
行为 CREATE Table(1:13~1:40) /test/1/catalog1/schema1/split_column_format_dynamic/
------
SQL  CREATE TABLE codex_constraint_c02 (id INT, CONSTRAINT CHECK (id > 0));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c02/
行为 CREATE Constraint(1:43~1:68) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE codex_constraint_c01 (id INT, CONSTRAINT UNIQUE (id));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c01/
行为 CREATE Constraint(1:43~1:65) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE codex_create_audit_enum_byte (a ENUM(b'10010010') BYTE);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/schema1/codex_create_audit_enum_byte/
------
SQL  CREATE TABLE t1 SELECT CEIL(ST_LINESTRINGFROMWKB(1) DIV NULL);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:23~1:27) /test/1/catalog1/schema1/CEIL/
行为 CALL Function(1:28~1:48) /test/1/catalog1/schema1/ST_LINESTRINGFROMWKB/
------
SQL  CREATE TABLE r SELECT 0 AS c, HEX(@a << 0) AS sl, HEX(@a >> 0) AS sr;
行为 CREATE Table(1:13~1:14) /test/1/catalog1/schema1/r/
行为 CALL Function(1:30~1:33) /test/1/catalog1/schema1/HEX/
行为 READ ConfigKey(1:34~1:36) /test/1/a/
------
SQL  CREATE TABLE split_table.parent(id INT PRIMARY KEY) ENGINE=InnoDB;
行为 CREATE Table(1:13~1:31) /test/1/catalog1/split_table/parent/
行为 CREATE Constraint(1:39~1:50) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE IF NOT EXISTS split_table.base (id INT PRIMARY KEY, note VARCHAR(40)) ENGINE=InnoDB;
行为 CREATE Table(1:27~1:43) /test/1/catalog1/split_table/base/
行为 CREATE Constraint(1:52~1:63) /test/1/catalog1/schema1/
------
SQL  CREATE TEMPORARY TABLE IF NOT EXISTS split_table.tmp_session (id INT, note VARCHAR(40));
行为 CREATE Table(1:37~1:60) /test/1/catalog1/split_table/tmp_session/
------
SQL  CREATE TEMPORARY TABLE split_table.tmp_drop_no_if_exists (id INT);
行为 CREATE Table(1:23~1:56) /test/1/catalog1/split_table/tmp_drop_no_if_exists/
------
SQL  CREATE TABLE split_table.options_common (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, note VARCHAR(40)) COMMENT='all common', MAX_ROWS=1000 MIN_ROWS=1 AVG_ROW_LENGTH=128 AUTO_INCREMENT=10 CHECKSUM=2 DELAY_KEY_WRITE=2 KEY_BLOCK_SIZE=8 PACK_KEYS=DEFAULT PASSWORD='secret' ROW_FORMAT=COMPACT STATS_AUTO_RECALC=DEFAULT STATS_PERSISTENT=1 STATS_SAMPLE_PAGES=DEFAULT DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_unicode_ci CONNECTION='mysql://host/db/table' DATA DIRECTORY='/var/lib/mysql-files' INDEX DIRECTORY='/var/lib/mysql-files' INSERT_METHOD=NO STORAGE DISK UNION=(split_table.base) ENGINE=MyISAM;
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_table/options_common/
行为 CREATE Constraint(1:72~1:83) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_row_format.dynamic_t(id INT) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_row_format/dynamic_t/
------
SQL  CREATE TABLE split_row_format.compressed_t(id INT) ENGINE=InnoDB ROW_FORMAT=COMPRESSED;
行为 CREATE Table(1:13~1:42) /test/1/catalog1/split_row_format/compressed_t/
------
SQL  CREATE TABLE split_row_format.redundant_t(id INT) ENGINE=InnoDB ROW_FORMAT=REDUNDANT;
行为 CREATE Table(1:13~1:41) /test/1/catalog1/split_row_format/redundant_t/
------
SQL  CREATE TABLE split_row_format.fixed_t(id INT) ENGINE=MyISAM ROW_FORMAT=FIXED;
行为 CREATE Table(1:13~1:37) /test/1/catalog1/split_row_format/fixed_t/
------
SQL  CREATE TABLE split_merge.first_t(id INT) ENGINE=MRG_MYISAM UNION=(merge_a,merge_b) INSERT_METHOD=FIRST;
行为 CREATE Table(1:13~1:32) /test/1/catalog1/split_merge/first_t/
------
SQL  CREATE TABLE m1(a INT) ENGINE=MERGE UNION=();
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/m1/
------
SQL  CREATE TABLE gap_zip (a INT, b TEXT) ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=4 TABLESPACE gap_ts;
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/gap_zip/
------
SQL  CREATE TABLE split_check_forward (CHECK((f1 + f2) > 10), f1 INT CHECK (f1 < 10), f2 INT);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/split_check_forward/
行为 CREATE Constraint(1:34~1:55) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:64~1:79) /test/1/catalog1/schema1/
------
SQL  CREATE TEMPORARY TABLE split_check_forward_tmp (CHECK((f1 + f2) > 10), f1 INT CHECK (f1 < 12), f2 INT);
行为 CREATE Table(1:23~1:46) /test/1/catalog1/schema1/split_check_forward_tmp/
行为 CREATE Constraint(1:48~1:69) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:78~1:93) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE audit_t (c JSON COLLATE utf8mb4_bin);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE type_json (c_json JSON);
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/type_json/
------
SQL  CREATE TABLE time_generated(raw_value VARCHAR(32),generated_time TIME(6) GENERATED ALWAYS AS(CAST(raw_value AS TIME(6))) STORED);
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/time_generated/
行为 CALL Function(1:93~1:97) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE split_type_json.json_primary_key (doc JSON PRIMARY KEY);
行为 CREATE Table(1:13~1:45) /test/1/catalog1/split_type_json/json_primary_key/
行为 CREATE Constraint(1:56~1:67) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_type_json.json_default_literal (id INT PRIMARY KEY,doc JSON DEFAULT '{}');
行为 CREATE Table(1:13~1:49) /test/1/catalog1/split_type_json/json_default_literal/
行为 CREATE Constraint(1:58~1:69) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE spatial_generated (\n  id INT PRIMARY KEY,\n  p POINT,\n  g GEOMETRY AS (ST_Envelope(p)) STORED\n);
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/spatial_generated/
行为 CREATE Constraint(2:9~2:20) /test/1/catalog1/schema1/
行为 CALL Function(4:17~4:28) /test/1/catalog1/schema1/ST_Envelope/
------
SQL  CREATE TABLE temporal_generated_pair(dt DATETIME PRIMARY KEY,datetxt VARCHAR(10) GENERATED ALWAYS AS (DATE(dt)) STORED,timetxt VARCHAR(10) GENERATED ALWAYS AS (TIME(dt)) STORED) ENGINE=InnoDB;
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/temporal_generated_pair/
行为 CREATE Constraint(1:49~1:60) /test/1/catalog1/schema1/
行为 CALL Function(1:102~1:106) /test/1/catalog1/schema1/DATE/
行为 CALL Function(1:160~1:164) /test/1/catalog1/schema1/TIME/
------
SQL  CREATE TABLE generated_coercibility(a VARCHAR(1024),b INT GENERATED ALWAYS AS (COERCIBILITY(a)) VIRTUAL);
行为 CREATE Table(1:13~1:35) /test/1/catalog1/schema1/generated_coercibility/
行为 CALL Function(1:79~1:91) /test/1/catalog1/schema1/COERCIBILITY/
------
SQL  CREATE TABLE split_keywords.keyword_identifiers (cume_dist INT, dense_rank INT, empty INT, except INT, first_value INT, grouping INT, groups INT, json_table INT, lag INT, last_value INT, lateral INT, lead INT, nth_value INT, ntile INT, of INT, over INT, percent_rank INT, rank INT, recursive INT, row_number INT, system INT, window INT, manual INT, parallel INT, qualify INT, tablesample INT, external INT, library INT, intersect INT, function INT, row INT, rows INT, cube INT);
行为 CREATE Table(1:13~1:47) /test/1/catalog1/split_keywords/keyword_identifiers/
------
SQL  CREATE TABLE gc_part (a INT, b INT GENERATED ALWAYS AS (a+1) VIRTUAL) PARTITION BY HASH(b);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/gc_part/
------
SQL  CREATE TABLE split_type_json.json_partition (\n      id INT NOT NULL,\n      doc JSON,\n      PRIMARY KEY(id)\n    )\n    PARTITION BY HASH(id) PARTITIONS 3;
行为 CREATE Table(1:13~1:43) /test/1/catalog1/split_type_json/json_partition/
行为 CREATE Constraint(4:6~4:21) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE t_generated(x VARCHAR(10), gc INTEGER GENERATED ALWAYS AS (x LIKE 'abba' ESCAPE 'b'));
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/t_generated/
------
SQL  CREATE TABLE md5_generated (a VARCHAR(1024), b VARBINARY(32) GENERATED ALWAYS AS (MD5(a)) VIRTUAL);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/md5_generated/
行为 CALL Function(1:82~1:85) /test/1/catalog1/schema1/MD5/
------
SQL  CREATE TEMPORARY TABLE z2 (SELECT CAST(NULL AS TIME) AS c05 ORDER BY '1') ORDER BY '1';
行为 CREATE Table(1:23~1:25) /test/1/catalog1/schema1/z2/
行为 CALL Function(1:34~1:38) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE str_generated (\n  c CHAR(8),\n  v VARCHAR(16),\n  b BINARY(8),\n  vb VARBINARY(16),\n  gc CHAR(8) GENERATED ALWAYS AS (c) VIRTUAL,\n  gv VARCHAR(16) GENERATED ALWAYS AS (v) STORED,\n  gb BINARY(8) GENERATED ALWAYS AS (b) VIRTUAL,\n  gvb VARBINARY(16) GENERATED ALWAYS AS (vb) STORED\n);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/str_generated/
------
SQL  CREATE TABLE lob_generated (\n  b BLOB,\n  t TEXT,\n  lb LONGBLOB,\n  lt LONGTEXT,\n  payload_length BIGINT GENERATED ALWAYS AS (OCTET_LENGTH(lb)+CHAR_LENGTH(lt)) STORED\n);
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/lob_generated/
行为 CALL Function(6:45~6:57) /test/1/catalog1/schema1/OCTET_LENGTH/
行为 CALL Function(6:62~6:73) /test/1/catalog1/schema1/CHAR_LENGTH/
------
SQL  CREATE TABLE ctas_t AS SELECT 1 AS a WHERE 1;
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/ctas_t/
------
SQL  CREATE TEMPORARY TABLE temp_t AS SELECT 1 AS a GROUP BY 1;
行为 CREATE Table(1:23~1:29) /test/1/catalog1/schema1/temp_t/
------
SQL  CREATE TABLE split_table.c_generated(id INT, g INT GENERATED ALWAYS AS (id + 1) VIRTUAL, s INT AS (id * 2) STORED);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/split_table/c_generated/
------
SQL  CREATE TABLE split_table.options_57 (id INT) COMPRESSION='none', ENCRYPTION='N' TABLESPACE=innodb_system STORAGE DISK ENGINE=InnoDB;
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_table/options_57/
------
SQL  CREATE TABLE json_gc (j JSON, stored_gc JSON GENERATED ALWAYS AS (JSON_EXTRACT(j,'$[0]')) STORED, virtual_gc JSON GENERATED ALWAYS AS (JSON_EXTRACT(j,'$[1]')) VIRTUAL);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/json_gc/
行为 CALL Function(1:66~1:78) /test/1/catalog1/schema1/JSON_EXTRACT/
------
SQL  CREATE TABLE gap_gnull (a INT, b INT GENERATED ALWAYS AS (-a) VIRTUAL NULL, c INT GENERATED ALWAYS AS (a+b) STORED NULL);
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/gap_gnull/
------
SQL  CREATE TABLE gap_gchain (a INT, c INT GENERATED ALWAYS AS (a+2), d INT GENERATED ALWAYS AS (c+2));
行为 CREATE Table(1:13~1:23) /test/1/catalog1/schema1/gap_gchain/
------
SQL  CREATE TABLE audit_t (c POINT SRID 4326 NULL);
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE type_expression_defaults (\n c_float FLOAT DEFAULT (RAND() * RAND()),\n c_binary BINARY(16) DEFAULT (UUID_TO_BIN(UUID())),\n c_date DATE DEFAULT (CURRENT_DATE + INTERVAL 1 YEAR),\n c_point POINT DEFAULT (Point(0,0)),\n c_json JSON DEFAULT (JSON_ARRAY()),\n c_blob BLOB DEFAULT ('abc'),\n c_text TEXT DEFAULT (CONCAT('a','b')),\n c_base INT DEFAULT 2,\n c_ref INT DEFAULT (c_base + 1)\n);
行为 CREATE Table(1:13~1:37) /test/1/catalog1/schema1/type_expression_defaults/
行为 CALL Function(2:24~2:28) /test/1/catalog1/schema1/RAND/
行为 CALL Function(3:30~3:41) /test/1/catalog1/schema1/UUID_TO_BIN/
行为 CALL Function(3:42~3:46) /test/1/catalog1/schema1/UUID/
行为 CALL Function(4:22~4:34) /test/1/catalog1/schema1/CURRENT_DATE/
行为 CALL Function(5:24~5:29) /test/1/catalog1/schema1/Point/
行为 CALL Function(6:22~6:32) /test/1/catalog1/schema1/JSON_ARRAY/
行为 CALL Function(8:22~8:28) /test/1/catalog1/schema1/CONCAT/
------
SQL  CREATE TABLE type_geom_alias (c_collection GEOMCOLLECTION);
行为 CREATE Table(1:13~1:28) /test/1/catalog1/schema1/type_geom_alias/
------
SQL  CREATE TABLE type_spatial_srid_forms (\n c_geom GEOMETRY SRID 0,\n c_point POINT SRID 4326 NOT NULL,\n c_linestring LINESTRING SRID 0,\n c_polygon POLYGON SRID 0,\n c_multipoint MULTIPOINT SRID 0,\n c_multiline MULTILINESTRING SRID 0,\n c_multipolygon MULTIPOLYGON SRID 0,\n c_collection GEOMETRYCOLLECTION SRID 0,\n c_alias GEOMCOLLECTION SRID 0\n);
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/type_spatial_srid_forms/
------
SQL  CREATE TABLE codex_year_generated(c1 YEAR AS (CAST(1985 AS YEAR)));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_year_generated/
行为 CALL Function(1:46~1:50) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE split_keywords.keyword_identifiers (`cume_dist` INT, `dense_rank` INT, `empty` INT, `except` INT, `first_value` INT, `grouping` INT, `groups` INT, `json_table` INT, `lag` INT, `last_value` INT, `lateral` INT, `lead` INT, `nth_value` INT, `ntile` INT, `of` INT, `over` INT, `percent_rank` INT, `rank` INT, `recursive` INT, `row_number` INT, `system` INT, `window` INT, manual INT, parallel INT, qualify INT, tablesample INT, external INT, library INT);
行为 CREATE Table(1:13~1:47) /test/1/catalog1/split_keywords/keyword_identifiers/
------
SQL  /*!80000 CREATE TABLE t_full (id INT PRIMARY KEY, name VARCHAR(50)) */;
行为 CREATE Table(1:22~1:28) /test/1/catalog1/schema1/t_full/
行为 CREATE Constraint(1:37~1:48) /test/1/catalog1/schema1/
------
SQL  /*!80000 CREATE TABLE t_cond (id BIGINT NOT NULL AUTO_INCREMENT, status TINYINT(1) NOT NULL DEFAULT 1, PRIMARY KEY (id)) */;
行为 CREATE Table(1:22~1:28) /test/1/catalog1/schema1/t_cond/
行为 CREATE Constraint(1:103~1:119) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE aes_feedback_modes (a VARBINARY(128), b128 VARBINARY(144), b192 VARBINARY(144), b256 BINARY(144));
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/aes_feedback_modes/
------
SQL  CREATE TABLE aes_ecb_modes (a VARBINARY(16), b128 CHAR(16), b192 CHAR(16), b256 CHAR(16)) CHARACTER SET latin1;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/aes_ecb_modes/
------
SQL  CREATE TABLE digest_small_result AS SELECT STATEMENT_DIGEST_TEXT('select 1, 2, 3') AS digest_text;
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/digest_small_result/
行为 CALL Function(1:43~1:64) /test/1/catalog1/schema1/STATEMENT_DIGEST_TEXT/
------
SQL  CREATE TABLE t1(i8 BIGINT,dc DECIMAL(20,4),r8 DOUBLE,fc CHAR(64),vc VARCHAR(64),d DATE,t TIME,dt DATETIME,j JSON,ji JSON,js JSON);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE digest_hash_result AS SELECT STATEMENT_DIGEST('SELECT 1, 2, 3') AS digest_hash;
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/digest_hash_result/
行为 CALL Function(1:42~1:58) /test/1/catalog1/schema1/STATEMENT_DIGEST/
------
SQL  CREATE TABLE digest_text_result AS SELECT STATEMENT_DIGEST_TEXT('SELECT 1, 2, 3') AS digest_text;
行为 CREATE Table(1:13~1:31) /test/1/catalog1/schema1/digest_text_result/
行为 CALL Function(1:42~1:63) /test/1/catalog1/schema1/STATEMENT_DIGEST_TEXT/
------
SQL  CREATE TABLE digest_generated(query_text VARCHAR(100), digest_hash VARCHAR(64) GENERATED ALWAYS AS (STATEMENT_DIGEST(query_text)));
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/digest_generated/
行为 CALL Function(1:100~1:116) /test/1/catalog1/schema1/STATEMENT_DIGEST/
------
SQL  CREATE TABLE digest_text_generated(query_text VARCHAR(100), digest_text VARCHAR(300) GENERATED ALWAYS AS (STATEMENT_DIGEST_TEXT(query_text)));
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/digest_text_generated/
行为 CALL Function(1:106~1:127) /test/1/catalog1/schema1/STATEMENT_DIGEST_TEXT/
------
SQL  CREATE TABLE dd_table (name VARCHAR(64) COLLATE utf8_tolower_ci, UNIQUE KEY(name));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/dd_table/
行为 CREATE Constraint(1:65~1:81) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE t_uuid_default (col VARCHAR(36) DEFAULT (REPLACE(UUID(), _utf8mb4'-', _utf8mb4'_')));
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/t_uuid_default/
行为 CALL Function(1:54~1:61) /test/1/catalog1/schema1/REPLACE/
行为 CALL Function(1:62~1:66) /test/1/catalog1/schema1/UUID/
------
SQL  CREATE TABLE uuid_text_to_bin_ctas AS SELECT UUID_TO_BIN('c8eb4b15-cb09-48bb-bbb2-e6a0b6b4d5c7') AS plain_order, UUID_TO_BIN('c8eb4b15-cb09-48bb-bbb2-e6a0b6b4d5c7', TRUE) AS swapped_order;
行为 CREATE Table(1:13~1:34) /test/1/catalog1/schema1/uuid_text_to_bin_ctas/
行为 CALL Function(1:45~1:56) /test/1/catalog1/schema1/UUID_TO_BIN/
------
SQL  CREATE TABLE split_window_context.g_bad(a INT,b INT AS (ROW_NUMBER() OVER (ORDER BY a)));
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_window_context/g_bad/
行为 CALL Function(1:56~1:66) /test/1/catalog1/schema1/ROW_NUMBER/
------
SQL  CREATE TABLE codex_ctas_values AS VALUES ROW(1,'a'), ROW(2,'b');
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/codex_ctas_values/
------
SQL  CREATE TABLE codex_ctas_recursive AS WITH RECURSIVE c(n) AS (SELECT 1 UNION ALL SELECT n + 1 FROM c WHERE n < 3) SELECT n FROM c;
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_ctas_recursive/
------
SQL  CREATE TABLE gap_attr_ctas ENGINE_ATTRIBUTE='{"kind":"ctas"}' AS SELECT 1 AS i;
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/gap_attr_ctas/
------
SQL  CREATE TABLE edges(s INT,e INT) WITH RECURSIVE tmp(s,e,d) AS (SELECT 1,2,1 UNION ALL SELECT FLOOR(1+RAND(3565659)*@node_count),FLOOR(1+RAND(2344291)*@node_count),d+1 FROM tmp WHERE d<@edge_count) SELECT s,e FROM tmp;
行为 CREATE Table(1:13~1:18) /test/1/catalog1/schema1/edges/
行为 CALL Function(1:92~1:97) /test/1/catalog1/schema1/FLOOR/
行为 CALL Function(1:100~1:104) /test/1/catalog1/schema1/RAND/
行为 READ ConfigKey(1:114~1:125) /test/1/node_count/
行为 READ ConfigKey(1:183~1:194) /test/1/edge_count/
------
SQL  CREATE TABLE tv VALUES ROW(1), ROW(2);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/tv/
------
SQL  CREATE TABLE ctas_intersect AS SELECT 1 AS c1,1 AS c2 INTERSECT SELECT 2,2;
行为 CREATE Table(1:13~1:27) /test/1/catalog1/schema1/ctas_intersect/
------
SQL  CREATE TABLE ctas_except AS SELECT 1 AS c1,1 AS c2 EXCEPT SELECT 2,2;
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/ctas_except/
------
SQL  CREATE TABLE ctas_values_ordered AS ((VALUES ROW(1,1),ROW(2,2) ORDER BY column_0 LIMIT 2) ORDER BY column_1 LIMIT 1);
行为 CREATE Table(1:13~1:32) /test/1/catalog1/schema1/ctas_values_ordered/
------
SQL  CREATE TABLE split_security_probe.default_select_probe (probe_value VARCHAR(64) DEFAULT ((SELECT DATABASE())));
行为 CREATE Table(1:13~1:54) /test/1/catalog1/split_security_probe/default_select_probe/
行为 CALL Function(1:97~1:105) /test/1/catalog1/schema1/DATABASE/
------
SQL  CREATE TABLE codex_constraint_c03 (id INT CONSTRAINT ck_c03 CHECK (id > 0));
行为 CREATE Table(1:13~1:33) /test/1/catalog1/schema1/codex_constraint_c03/
行为 CREATE Constraint(1:53~1:59) /test/1/catalog1/schema1/ck_c03/
------
SQL  CREATE TABLE split_table.c_default_expr(id INT DEFAULT (RAND() * 10), hidden_col INT INVISIBLE, shown_col INT VISIBLE);
行为 CREATE Table(1:13~1:39) /test/1/catalog1/split_table/c_default_expr/
行为 CALL Function(1:56~1:60) /test/1/catalog1/schema1/RAND/
------
SQL  CREATE TABLE split_table.c_check_enforced(id INT, CONSTRAINT chk_id CHECK (id > 0) ENFORCED);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/split_table/c_check_enforced/
行为 CREATE Constraint(1:61~1:67) /test/1/catalog1/schema1/chk_id/
------
SQL  CREATE TABLE split_table.c_check_not_enforced(id INT, CONSTRAINT chk_id CHECK (id > 0) NOT ENFORCED);
行为 CREATE Table(1:13~1:45) /test/1/catalog1/split_table/c_check_not_enforced/
行为 CREATE Constraint(1:65~1:71) /test/1/catalog1/schema1/chk_id/
------
SQL  CREATE TABLE split_table.c_attr(id INT ENGINE_ATTRIBUTE='{}' SECONDARY_ENGINE_ATTRIBUTE='{}');
行为 CREATE Table(1:13~1:31) /test/1/catalog1/split_table/c_attr/
------
SQL  CREATE TABLE split_table.options_80 (id INT) SECONDARY_ENGINE=NULL ENGINE_ATTRIBUTE='{}' SECONDARY_ENGINE_ATTRIBUTE='{}' START TRANSACTION ENGINE=InnoDB;
行为 CREATE Table(1:13~1:35) /test/1/catalog1/split_table/options_80/
------
SQL  create table abc(id int);
行为 CREATE Table(1:13~1:16) /test/1/catalog1/schema1/abc/
------
SQL  create table `abc`(`id` int) COMMENT 'desc';
行为 CREATE Table(1:13~1:18) /test/1/catalog1/schema1/abc/
------
SQL  create table if not exists test.abc(id int);
行为 CREATE Table(1:27~1:35) /test/1/catalog1/test/abc/
------
SQL  create table if not exists abc(id int);
行为 CREATE Table(1:27~1:30) /test/1/catalog1/schema1/abc/
------
SQL  create temporary table if not exists test.abc(id int);
行为 CREATE Table(1:37~1:45) /test/1/catalog1/test/abc/
------
SQL  create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) auto_increment = 12;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) engine = innodb;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) engine = myisam;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) charset =utf8mb4 collate utf8mb4_unicode_ci;
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4), name varchar(25) not null);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
------
SQL  create table test.abc(id int(4) primary key, name varchar(25) not null);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4), name varchar(25) not null,primary key (id));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:59~1:75) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id double(4,5), name varchar(25) not null);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
------
SQL  create table test.abc(id int(4) unique, name date not null);
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:32~1:38) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4), name varchar(25) not null,unique (id));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:59~1:70) /test/1/catalog1/schema1/
------
SQL  create table test.abc(id int(4), name varchar(25) not null, primary key (id));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/test/abc/
行为 CREATE Constraint(1:60~1:76) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE `test` (`id` bigint(20) NOT NULL PRIMARY KEY);
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/test/
行为 CREATE Constraint(1:46~1:57) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE t2(c1 INT,c2 TEXT) AUTOEXTEND_SIZE 4M;
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t2/
------
SQL  CREATE TABLE f6_t (a INT, b VARCHAR(34) DEFAULT (ETAG(a)));
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/f6_t/
行为 CALL Function(1:49~1:53) /test/1/catalog1/schema1/ETAG/
------
SQL  CREATE TABLE check_order (a INTEGER CHECK (a > 0) NOT ENFORCED NOT NULL);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/check_order/
行为 CREATE Constraint(1:36~1:62) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE autoext_part(c1 INT, c2 TEXT) AUTOEXTEND_SIZE 4M PARTITION BY RANGE(c1) (PARTITION p0 VALUES LESS THAN(20), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 CREATE Table(1:13~1:25) /test/1/catalog1/schema1/autoext_part/
------
SQL  CREATE TABLE gap_def_order (i JSON DEFAULT (JSON_ARRAY(b)), b INT DEFAULT (123*1));
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/gap_def_order/
行为 CALL Function(1:44~1:54) /test/1/catalog1/schema1/JSON_ARRAY/
------
SQL  CREATE TABLE gap_secondary_new (a INT) SECONDARY_ENGINE MOCK;
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/gap_secondary_new/
------
SQL  CREATE TABLE t0 (dt DATETIME,CHECK (CAST(TIMESTAMP '2010-01-01 10:00:00' AT TIME ZONE '+00:00' AS DATETIME)=dt));
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t0/
行为 CREATE Constraint(1:29~1:111) /test/1/catalog1/schema1/
行为 CALL Function(1:36~1:40) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE TABLE split_check_multi (a INTEGER CHECK (a > 0) UNIQUE CHECK (a IS NOT NULL) NULL CHECK (a < 100));
行为 CREATE Table(1:13~1:30) /test/1/catalog1/schema1/split_check_multi/
行为 CREATE Constraint(1:42~1:55) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:56~1:62) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:63~1:84) /test/1/catalog1/schema1/
行为 CREATE Constraint(1:90~1:105) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_geometry_primary (g GEOMETRY SRID 0 PRIMARY KEY);
行为 CREATE Table(1:13~1:35) /test/1/catalog1/schema1/split_geometry_primary/
行为 CREATE Constraint(1:55~1:66) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_info_user_defaults (c_current VARCHAR(288) DEFAULT (CURRENT_USER()), c_session VARCHAR(288) DEFAULT (SESSION_USER()), c_system VARCHAR(288) DEFAULT (SYSTEM_USER()), c_user VARCHAR(288) DEFAULT (USER()));
行为 CREATE Table(1:13~1:37) /test/1/catalog1/schema1/split_info_user_defaults/
行为 CALL Function(1:71~1:83) /test/1/catalog1/schema1/CURRENT_USER/
行为 CALL Function(1:120~1:132) /test/1/catalog1/schema1/SESSION_USER/
行为 CALL Function(1:168~1:179) /test/1/catalog1/schema1/SYSTEM_USER/
行为 CALL Function(1:213~1:217) /test/1/catalog1/schema1/USER/
------
SQL  CREATE TABLE employees (\n    id INT NOT NULL,\n    name VARCHAR(100),\n    position VARCHAR(100),\n    salary DECIMAL(10,2),\n    PRIMARY KEY (id)\n);
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/employees/
行为 CREATE Constraint(6:4~6:20) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_removed_84 (master_bind INT, master_ssl_verify_server_cert INT);
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/split_removed_84/
------
SQL  CREATE TABLE split_keywords.keyword_identifiers (`cume_dist` INT, `dense_rank` INT, `empty` INT, `except` INT, `first_value` INT, `grouping` INT, `groups` INT, `json_table` INT, `lag` INT, `last_value` INT, `lateral` INT, `lead` INT, `nth_value` INT, `ntile` INT, `of` INT, `over` INT, `percent_rank` INT, `rank` INT, `recursive` INT, `row_number` INT, `system` INT, `window` INT, `manual` INT, `parallel` INT, `qualify` INT, `tablesample` INT, external INT, library INT);
行为 CREATE Table(1:13~1:47) /test/1/catalog1/split_keywords/keyword_identifiers/
------
SQL  CREATE TABLE dd_table (name VARCHAR(64) COLLATE utf8mb3_tolower_ci, UNIQUE KEY(name));
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/dd_table/
行为 CREATE Constraint(1:68~1:84) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE split_func_system.ctas_charset SELECT CHARSET(_utf8mb3'a'),COLLATION(_utf8mb3'a');
行为 CREATE Table(1:13~1:43) /test/1/catalog1/split_func_system/ctas_charset/
行为 CALL Function(1:51~1:58) /test/1/catalog1/schema1/CHARSET/
行为 CALL Function(1:72~1:81) /test/1/catalog1/schema1/COLLATION/
------
SQL  CREATE TABLE t84_types (\n  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,\n  bit_col BIT(8),\n  dec_col DECIMAL(12,4),\n  bool_col BOOLEAN,\n  json_col JSON,\n  geom_col POINT SRID 4326 INVISIBLE,\n  name VARCHAR(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,\n  name_lc VARCHAR(40) GENERATED ALWAYS AS (LOWER(name)) STORED,\n  val INT CHECK (val >= 0),\n  created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),\n  PRIMARY KEY (id),\n  CONSTRAINT chk_t84_val CHECK (val < 1000)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='mysql 8.4 type coverage';
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/t84_types/
行为 CALL Function(9:43~9:48) /test/1/catalog1/schema1/LOWER/
行为 CREATE Constraint(10:10~10:26) /test/1/catalog1/schema1/
行为 CREATE Constraint(12:2~12:18) /test/1/catalog1/schema1/
行为 CREATE Constraint(13:13~13:24) /test/1/catalog1/schema1/chk_t84_val/
------
SQL  CREATE TABLE audit_t (c VECTOR(3));
行为 CREATE Table(1:13~1:20) /test/1/catalog1/schema1/audit_t/
------
SQL  CREATE TABLE splitvector.t_vector_default (id INT PRIMARY KEY, embedding VECTOR);
行为 CREATE Table(1:13~1:41) /test/1/catalog1/splitvector/t_vector_default/
行为 CREATE Constraint(1:50~1:61) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE splitvector.t_vector_dims (id INT PRIMARY KEY, embedding VECTOR(3));
行为 CREATE Table(1:13~1:38) /test/1/catalog1/splitvector/t_vector_dims/
行为 CREATE Constraint(1:47~1:58) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE f9_t (pk INT, v VECTOR(3) DEFAULT (TO_VECTOR('[1,2,3]')), PRIMARY KEY (pk));
行为 CREATE Table(1:13~1:17) /test/1/catalog1/schema1/f9_t/
行为 CALL Function(1:48~1:57) /test/1/catalog1/schema1/TO_VECTOR/
行为 CREATE Constraint(1:71~1:87) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE f10_t (pk INT, v VECTOR(1) DEFAULT (UNHEX('aabbccdd')), PRIMARY KEY (pk));
行为 CREATE Table(1:13~1:18) /test/1/catalog1/schema1/f10_t/
行为 CALL Function(1:49~1:54) /test/1/catalog1/schema1/UNHEX/
行为 CREATE Constraint(1:69~1:85) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE f12_tb (pk INT PRIMARY KEY, embedding VECTOR(7498));
行为 CREATE Table(1:13~1:19) /test/1/catalog1/schema1/f12_tb/
行为 CREATE Constraint(1:28~1:39) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE vector_lifecycle (\n  id INT PRIMARY KEY,\n  grp INT,\n  embedding VECTOR(3),\n  embedding_default VECTOR DEFAULT (TO_VECTOR('[0,0,0]')),\n  note VARCHAR(32)\n);
行为 CREATE Table(1:13~1:29) /test/1/catalog1/schema1/vector_lifecycle/
行为 CREATE Constraint(2:9~2:20) /test/1/catalog1/schema1/
行为 CALL Function(5:36~5:45) /test/1/catalog1/schema1/TO_VECTOR/
------
SQL  CREATE TABLE vector_generated_scalar(v VECTOR(4),g INT AS (CHARACTER_LENGTH(v)));
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/vector_generated_scalar/
行为 CALL Function(1:59~1:75) /test/1/catalog1/schema1/CHARACTER_LENGTH/
------
SQL  CREATE TABLE vector_generated_vector(v VECTOR(4),g VECTOR(4) AS (TRIM(v)));
行为 CREATE Table(1:13~1:36) /test/1/catalog1/schema1/vector_generated_vector/
行为 CALL Function(1:65~1:69) /test/1/catalog1/schema1/TRIM/
------
SQL  CREATE TABLE vector_pk(v VECTOR(3),PRIMARY KEY(v));
行为 CREATE Table(1:13~1:22) /test/1/catalog1/schema1/vector_pk/
行为 CREATE Constraint(1:35~1:49) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE vector_unique(v VECTOR(3),UNIQUE(v));
行为 CREATE Table(1:13~1:26) /test/1/catalog1/schema1/vector_unique/
行为 CREATE Constraint(1:39~1:48) /test/1/catalog1/schema1/
------
SQL  CREATE EXTERNAL TABLE split_ext.ext_format (\n  id INT,\n  event_time DATETIME EXTERNAL_FORMAT 'yyyy-MM-dd HH:mm:ss',\n  event_date DATE EXTERNAL_FORMAT 'yyyy-MM-dd'\n);
行为 CREATE Table(1:22~1:42) /test/1/catalog1/split_ext/ext_format/
------
SQL  CREATE TABLE split_ext.regular_format (\n  id INT,\n  event_time DATETIME EXTERNAL_FORMAT 'yyyy-MM-dd HH:mm:ss'\n);
行为 CREATE Table(1:13~1:37) /test/1/catalog1/split_ext/regular_format/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_basic (id INT);
行为 CREATE Table(1:22~1:46) /test/1/catalog1/split_keywords/ext_basic/
------
SQL  CREATE EXTERNAL TABLE IF NOT EXISTS split_keywords.ext_if (id INT, name VARCHAR(20));
行为 CREATE Table(1:36~1:57) /test/1/catalog1/split_keywords/ext_if/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_engine (id INT) ENGINE=InnoDB;
行为 CREATE Table(1:22~1:47) /test/1/catalog1/split_keywords/ext_engine/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_secondary (id INT) SECONDARY_ENGINE=NULL;
行为 CREATE Table(1:22~1:50) /test/1/catalog1/split_keywords/ext_secondary/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_both (id INT) ENGINE=InnoDB SECONDARY_ENGINE=NULL;
行为 CREATE Table(1:22~1:45) /test/1/catalog1/split_keywords/ext_both/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_options (id INT)\nFILE_FORMAT=(FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES STARTING BY '' TERMINATED BY '\n' IGNORE 1 LINES)\nFILES=(URI='s3://bucket/a.csv' FILE_NAME='a.csv' FILE_PATTERN='*.csv' FILE_PREFIX='prefix/' ALLOW_MISSING_FILES=1 STRICT_LOAD=0, URL='https://example.com/b.csv')\nALLOW_MISSING_FILES=DEFAULT\nVERIFY_KEY_CONSTRAINTS=1\nSTRICT_LOAD=DEFAULT\nAUTO_REFRESH=0\nAUTO_REFRESH_SOURCE='split_source';
行为 CREATE Table(1:22~1:48) /test/1/catalog1/split_keywords/ext_options/
------
SQL  CREATE EXTERNAL TABLE split_keywords.ext_opts_none (id INT)\nALLOW_MISSING_FILES=0\nVERIFY_KEY_CONSTRAINTS=DEFAULT\nSTRICT_LOAD=1\nAUTO_REFRESH=DEFAULT\nAUTO_REFRESH_SOURCE=NONE;
行为 CREATE Table(1:22~1:50) /test/1/catalog1/split_keywords/ext_opts_none/
------
SQL  CREATE TABLE split_keywords.keyword_identifiers (`cume_dist` INT, `dense_rank` INT, `empty` INT, `except` INT, `first_value` INT, `grouping` INT, `groups` INT, `json_table` INT, `lag` INT, `last_value` INT, `lateral` INT, `lead` INT, `nth_value` INT, `ntile` INT, `of` INT, `over` INT, `percent_rank` INT, `rank` INT, `recursive` INT, `row_number` INT, `system` INT, `window` INT, `manual` INT, `parallel` INT, `qualify` INT, `tablesample` INT, `external` INT, `library` INT);
行为 CREATE Table(1:13~1:47) /test/1/catalog1/split_keywords/keyword_identifiers/
------
SQL  CREATE TABLE split_keywords.files (absent INT, allow_missing_files INT, auto_refresh INT, auto_refresh_source INT, duality INT, external_format INT, file_format INT, file_name INT, file_pattern INT, file_prefix INT, files INT, guided INT, header INT, json_duality_object INT, masking INT, materialized INT, parameters INT, policy INT, relational INT, sets INT, strict_load INT, uri INT, validate INT, vector INT, verify_key_constraints INT);
行为 CREATE Table(1:13~1:33) /test/1/catalog1/split_keywords/files/
------
SQL  CREATE EXTERNAL TABLE ext_options_gap(id INT) COMMENT='x' AUTO_INCREMENT=100 ENGINE=InnoDB;
行为 CREATE Table(1:22~1:37) /test/1/catalog1/schema1/ext_options_gap/
------
SQL  CREATE EXTERNAL TABLE ext_ctas_gap AS SELECT 1 AS id;
行为 CREATE Table(1:22~1:34) /test/1/catalog1/schema1/ext_ctas_gap/
------
SQL  CREATE EXTERNAL TABLE ext_cols_ctas_gap(id INT) AS SELECT 1;
行为 CREATE Table(1:22~1:39) /test/1/catalog1/schema1/ext_cols_ctas_gap/
------
SQL  CREATE EXTERNAL TABLE ext_empty_format_gap(id INT) FILE_FORMAT=();
行为 CREATE Table(1:22~1:42) /test/1/catalog1/schema1/ext_empty_format_gap/
------
SQL  CREATE EXTERNAL TABLE ext_header_format_gap(id INT) FILE_FORMAT=(HEADER ON CHARACTER SET utf8mb4);
行为 CREATE Table(1:22~1:43) /test/1/catalog1/schema1/ext_header_format_gap/
------
SQL  CREATE EXTERNAL TABLE ext_url_files_gap(id INT) FILES=(URL='https://e/x.csv' FILE_NAME='x.csv' FILE_PATTERN='*.csv' STRICT_LOAD=1);
行为 CREATE Table(1:22~1:39) /test/1/catalog1/schema1/ext_url_files_gap/
------
SQL  CREATE EXTERNAL TABLE ext_uri_defaults_gap(id INT) FILES=(URI='s3://b/x' ALLOW_MISSING_FILES=DEFAULT STRICT_LOAD=DEFAULT);
行为 CREATE Table(1:22~1:42) /test/1/catalog1/schema1/ext_uri_defaults_gap/
------
SQL  CREATE EXTERNAL TABLE audit_ext_values AS VALUES ROW(1,'a');
行为 CREATE Table(1:22~1:38) /test/1/catalog1/schema1/audit_ext_values/
------
SQL  CREATE EXTERNAL TABLE audit_ext_cte AS WITH q AS (SELECT 1 AS id) SELECT id FROM q;
行为 CREATE Table(1:22~1:35) /test/1/catalog1/schema1/audit_ext_cte/
------
SQL  CREATE EXTERNAL TABLE audit_ext_format(id INT) FILE_FORMAT=(FORMAT CSV);
行为 CREATE Table(1:22~1:38) /test/1/catalog1/schema1/audit_ext_format/
------
SQL  CREATE EXTERNAL TABLE audit_ext_compression(id INT) FILE_FORMAT=(COMPRESSION GZIP);
行为 CREATE Table(1:22~1:43) /test/1/catalog1/schema1/audit_ext_compression/
------
SQL  CREATE EXTERNAL TABLE audit_ext_header(id INT) FILE_FORMAT=(HEADER OFF);
行为 CREATE Table(1:22~1:38) /test/1/catalog1/schema1/audit_ext_header/
------
SQL  CREATE TABLE vector_partition_forbidden (\n  id INT,\n  embedding VECTOR(3)\n) PARTITION BY KEY(embedding) PARTITIONS 2;
行为 CREATE Table(1:13~1:39) /test/1/catalog1/schema1/vector_partition_forbidden/
------
SQL  CREATE TABLE vector_part(id INT PRIMARY KEY,embedding VECTOR(3)) PARTITION BY KEY(id);
行为 CREATE Table(1:13~1:24) /test/1/catalog1/schema1/vector_part/
行为 CREATE Constraint(1:32~1:43) /test/1/catalog1/schema1/
------
SQL  CREATE TABLE t1(i VARCHAR(256) MASKING POLICY p1);
行为 CREATE Table(1:13~1:15) /test/1/catalog1/schema1/t1/
------
SQL  CREATE TABLE vector_t (embedding VECTOR(3))
行为 CREATE Table(1:13~1:21) /test/1/catalog1/schema1/vector_t/

## ALTER_TABLE

SQL  ALTER TABLE codex_year_alter MODIFY COLUMN c1 YEAR(2);
行为 ALTER Table(1:12~1:28) /test/1/catalog1/schema1/codex_year_alter/
------
SQL  ALTER TABLE a01 MODIFY COLUMN a FLOAT(255,0);
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/a01/
------
SQL  ALTER TABLE a02 MODIFY COLUMN a DOUBLE PRECISION(42,12);
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/a02/
------
SQL  ALTER TABLE a03 MODIFY COLUMN a REAL(42,12);
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/a03/
------
SQL  ALTER TABLE ai MODIFY COLUMN a FLOAT PRIMARY KEY AUTO_INCREMENT;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/ai/
行为 CREATE Constraint(1:37~1:48) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE datetime_widths MODIFY dt0 TIMESTAMP(0) NULL,MODIFY ts0 DATETIME(0) NULL;
行为 ALTER Table(1:12~1:27) /test/1/catalog1/schema1/datetime_widths/
------
SQL  ALTER TABLE str_lifecycle MODIFY c VARCHAR(20), MODIFY v CHAR(20), MODIFY t VARCHAR(300);
行为 ALTER Table(1:12~1:25) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  ALTER TABLE str_lifecycle CHANGE b fixed_binary BINARY(16), CHANGE vb variable_binary VARBINARY(32);
行为 ALTER Table(1:12~1:25) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  ALTER TABLE lob_lifecycle\n  MODIFY b MEDIUMBLOB,\n  MODIFY t MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,\n  CHANGE mb tiny_b TINYBLOB,\n  CHANGE mt long_t LONGTEXT CHARACTER SET utf8mb4;
行为 ALTER Table(1:12~1:25) /test/1/catalog1/schema1/lob_lifecycle/
------
SQL  ALTER TABLE spatial_lifecycle ADD COLUMN p_added POINT NULL AFTER g;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/
------
SQL  ALTER TABLE spatial_lifecycle MODIFY COLUMN p_added GEOMETRY NULL;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/
------
SQL  ALTER TABLE spatial_lifecycle CHANGE COLUMN p_added p_changed LINESTRING NULL;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/
------
SQL  ALTER TABLE integer_lifecycle\n  ADD COLUMN tiny_added TINYINT UNSIGNED AFTER tiny_unsigned,\n  ADD COLUMN small_added SMALLINT ZEROFILL AFTER small_unsigned,\n  ADD COLUMN medium_added MEDIUMINT UNSIGNED AFTER medium_unsigned,\n  ADD COLUMN big_added BIGINT UNSIGNED AFTER big_unsigned;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  ALTER TABLE integer_lifecycle\n  MODIFY COLUMN tiny_added SMALLINT SIGNED DEFAULT 0,\n  CHANGE COLUMN small_added small_changed MEDIUMINT UNSIGNED ZEROFILL DEFAULT 0,\n  MODIFY COLUMN medium_added INT SIGNED DEFAULT 0,\n  CHANGE COLUMN big_added big_changed BIGINT SIGNED DEFAULT 0;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  ALTER TABLE integer_serial_lifecycle CHANGE COLUMN id id SERIAL;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/schema1/integer_serial_lifecycle/
------
SQL  ALTER TABLE numeric_lifecycle\n  ADD COLUMN decimal_added DECIMAL(65,30) AFTER decimal_value,\n  ADD COLUMN numeric_added NUMERIC(30,15) AFTER numeric_value,\n  ADD COLUMN fixed_added FIXED(24,8) AFTER fixed_value,\n  ADD COLUMN float_added FLOAT(25) AFTER float_value,\n  ADD COLUMN double_added DOUBLE AFTER double_value,\n  ADD COLUMN real_added REAL AFTER real_value;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  ALTER TABLE numeric_lifecycle\n  MODIFY COLUMN decimal_added NUMERIC(40,20) UNSIGNED,\n  CHANGE COLUMN numeric_added numeric_changed FIXED(32,12) ZEROFILL,\n  MODIFY COLUMN fixed_added DECIMAL(30,10) SIGNED,\n  CHANGE COLUMN float_added float_changed DOUBLE PRECISION(20,8),\n  MODIFY COLUMN double_added REAL(18,6) UNSIGNED,\n  CHANGE COLUMN real_added real_changed FLOAT(24) ZEROFILL;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  ALTER TABLE double_auto_increment_alter MODIFY COLUMN id DOUBLE AUTO_INCREMENT;
行为 ALTER Table(1:12~1:39) /test/1/catalog1/schema1/double_auto_increment_alter/
------
SQL  ALTER TABLE split_idx.t_alter ADD PRIMARY KEY(id), DROP PRIMARY KEY;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/split_idx/t_alter/
行为 CREATE Constraint(1:30~1:49) /test/1/catalog1/schema1/
行为 DROP Constraint(1:51~1:67) /test/1/catalog1/schema1/
------
SQL  /*!50000 ALTER TABLE t ADD COLUMN flag INT DEFAULT 0 */;
行为 ALTER Table(1:21~1:22) /test/1/catalog1/schema1/t/
------
SQL  ALTER TABLE t1 CONVERT TO CHARACTER SET DEFAULT;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 CONVERT TO CHARACTER SET DEFAULT COLLATE cp1251_bin;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 RENAME TO ``.t1;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 EXCHANGE PARTITION p0 WITH TABLE ``;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE dml_t;
行为 ALTER Table(1:12~1:17) /test/1/catalog1/schema1/dml_t/
------
SQL  ALTER TABLE dml_t LOCK=SHARED, ALGORITHM=COPY,\n                  LOCK=NONE, ALGORITHM=DEFAULT,\n                  LOCK=EXCLUSIVE, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:17) /test/1/catalog1/schema1/dml_t/
------
SQL  ALTER TABLE codex_c13_22.t DROP COLUMN i, ADD COLUMN i INT DEFAULT 2, ALGORITHM=INPLACE PARTITION BY RANGE(i) (PARTITION p0 VALUES LESS THAN (0), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 ALTER Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  ALTER TABLE syntax_audit.c_reorg REORGANIZE PARTITION;
行为 ALTER Table(1:12~1:32) /test/1/catalog1/syntax_audit/c_reorg/
------
SQL  ALTER TABLE split_partition.p_range ADD PARTITION (PARTITION p2 VALUES LESS THAN (2030));
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_hash ADD PARTITION LOCAL;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash ADD PARTITION PARTITIONS 2;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_range DROP PARTITION p0,p1;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range REBUILD PARTITION NO_WRITE_TO_BINLOG p0,p1;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range OPTIMIZE PARTITION LOCAL ALL;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range OPTIMIZE PARTITION p0 LOCAL;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range OPTIMIZE PARTITION p0 NO_WRITE_TO_BINLOG;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range OPTIMIZE PARTITION LOCAL p0 NO_WRITE_TO_BINLOG;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range ANALYZE PARTITION NO_WRITE_TO_BINLOG p0,p1;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range CHECK PARTITION p0 QUICK FAST MEDIUM EXTENDED CHANGED FOR UPGRADE;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range REPAIR PARTITION LOCAL p0 QUICK EXTENDED USE_FRM;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range REPAIR PARTITION p0 USE_FRM QUICK EXTENDED;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range REPAIR PARTITION p0 QUICK QUICK;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_hash COALESCE PARTITION NO_WRITE_TO_BINLOG 1;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_range REORGANIZE PARTITION LOCAL p1,p2 INTO (PARTITION p1 VALUES LESS THAN (2025), PARTITION p2 VALUES LESS THAN MAXVALUE);
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range TRUNCATE PARTITION ALL;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range EXCHANGE PARTITION p0 WITH TABLE split_partition.p_exchange;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range REMOVE PARTITIONING;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_list PARTITION BY HASH(id) PARTITIONS 2;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_list/
------
SQL  ALTER TABLE codex_rename.part_t TRUNCATE PARTITION p0, p1;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/codex_rename/part_t/
------
SQL  ALTER TABLE subpartitioned ADD PARTITION (PARTITION p1 VALUES LESS THAN MAXVALUE (SUBPARTITION sp2 TABLESPACE innodb_file_per_table, SUBPARTITION sp3 TABLESPACE innodb_system));
行为 ALTER Table(1:12~1:26) /test/1/catalog1/schema1/subpartitioned/
------
SQL  ALTER TABLE subpartitioned REORGANIZE PARTITION p1 INTO (PARTITION p1 VALUES LESS THAN MAXVALUE (SUBPARTITION sp2, SUBPARTITION sp3 TABLESPACE innodb_file_per_table));
行为 ALTER Table(1:12~1:26) /test/1/catalog1/schema1/subpartitioned/
------
SQL  ALTER TABLE tpart ADD PARTITION (PARTITION p2 VALUES LESS THAN(30) TABLESPACE=innodb_file_per_table, PARTITION p3 VALUES LESS THAN(40) TABLESPACE=innodb_file_per_table);
行为 ALTER Table(1:12~1:17) /test/1/catalog1/schema1/tpart/
------
SQL  ALTER TABLE part_all REBUILD PARTITION ALL;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/part_all/
------
SQL  ALTER TABLE part_all ANALYZE PARTITION ALL;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/part_all/
------
SQL  ALTER TABLE part_all CHECK PARTITION ALL EXTENDED;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/part_all/
------
SQL  ALTER TABLE part_all REPAIR PARTITION ALL;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/part_all/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN new_col INT, ORDER BY payoutID,bandID;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t TABLESPACE codex_alter_audit_space;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE fd_alter MODIFY a TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) AFTER b;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/fd_alter/
------
SQL  ALTER TABLE fd_alter MODIFY c DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) FIRST;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/fd_alter/
------
SQL  ALTER TABLE fd_alter ADD COLUMN d TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) AFTER a;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/fd_alter/
------
SQL  ALTER TABLE t1 ADD CONSTRAINT CHECK (c1 > 10), ALGORITHM=COPY;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
行为 CREATE Constraint(1:15~1:45) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE t1 MODIFY COLUMN f1 INT DEFAULT 20, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE split_table2.moved RENAME TO split_table.as_select;
行为 ALTER Table(1:12~1:30) /test/1/catalog1/split_table2/moved/
------
SQL  ALTER TABLE split_table.as_no_keyword RENAME AS split_table.as_renamed;
行为 ALTER Table(1:12~1:37) /test/1/catalog1/split_table/as_no_keyword/
------
SQL  ALTER TABLE m1 UNION=();
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/m1/
------
SQL  ALTER TABLE t1 MODIFY COLUMN l INT STORAGE DISK STORAGE MEMORY;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE storage_table STORAGE MEMORY;
行为 ALTER Table(1:12~1:25) /test/1/catalog1/schema1/storage_table/
------
SQL  ALTER TABLE storage_add ADD COLUMN j INT STORAGE DISK,ADD COLUMN k INT STORAGE MEMORY NOT NULL;
行为 ALTER Table(1:12~1:23) /test/1/catalog1/schema1/storage_add/
------
SQL  ALTER TABLE storage_change CHANGE COLUMN a a INT STORAGE MEMORY;
行为 ALTER Table(1:12~1:26) /test/1/catalog1/schema1/storage_change/
------
SQL  ALTER TABLE storage_modify MODIFY COLUMN m INT STORAGE DISK STORAGE DEFAULT;
行为 ALTER Table(1:12~1:26) /test/1/catalog1/schema1/storage_modify/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN y INT AFTER a, LOCK=NONE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN x INT FIRST, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CHANGE COLUMN c c_new INT AFTER a, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CHANGE COLUMN c c_new INT AFTER a, LOCK=NONE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CHARACTER SET utf8, CHARACTER SET utf8;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CHARACTER SET utf8, CONVERT TO CHARACTER SET utf8;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CONVERT TO CHARACTER SET utf8, CHARACTER SET utf8;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CONVERT TO CHARACTER SET utf8, CONVERT TO CHARACTER SET utf8;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t DROP COLUMN a, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t DROP COLUMN a, LOCK=NONE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t ENABLE KEYS, ALGORITHM=INPLACE, LOCK=EXCLUSIVE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t ALTER COLUMN i SET DEFAULT 11, ALGORITHM=INSTANT, LOCK=NONE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN col4_5 VARCHAR(20) NOT NULL AFTER col4, ADD COLUMN col7 VARCHAR(30) NOT NULL AFTER col5, ADD COLUMN col8 DATETIME NOT NULL, DROP COLUMN to_be_deleted, CHANGE COLUMN col2 fourth VARCHAR(30) NOT NULL AFTER col3, MODIFY COLUMN col6 INT NOT NULL FIRST;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t MODIFY COLUMN e INT AFTER a, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t MODIFY COLUMN e INT AFTER a, LOCK=NONE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t MODIFY a BIGINT, DISABLE KEYS;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t RENAME codex_alter_audit_u, ADD c CHAR(10) COMMENT 'no comment';
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t PACK_KEYS=1 PACK_KEYS=0 STATS_PERSISTENT=1 STATS_PERSISTENT=0 CHECKSUM=1 CHECKSUM=0 DELAY_KEY_WRITE=1 DELAY_KEY_WRITE=0;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE split_table.alter_common;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ADD COLUMN add_first INT FIRST;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ADD add_after INT AFTER id;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ADD (multi_a INT, multi_b VARCHAR(20));
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common CHANGE COLUMN c changed BIGINT NOT NULL DEFAULT 0 AFTER parent_id;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common MODIFY changed BIGINT NULL FIRST;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ALTER changed SET DEFAULT 5;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ALTER COLUMN changed DROP DEFAULT;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ADD CONSTRAINT chk_new CHECK (changed >= 0);
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
行为 CREATE Constraint(1:52~1:59) /test/1/catalog1/schema1/chk_new/
------
SQL  ALTER TABLE split_table.alter_common DROP FOREIGN KEY fk_new;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
行为 DROP Constraint(1:54~1:60) /test/1/catalog1/schema1/fk_new/
------
SQL  ALTER TABLE split_table.alter_common DROP COLUMN multi_b, DROP multi_a RESTRICT;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common DISABLE KEYS;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ENABLE KEYS;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ORDER BY changed DESC, id ASC;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common FORCE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common ALGORITHM=INSTANT, LOCK=NONE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common COMMENT='altered', ROW_FORMAT=COMPACT;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common DISCARD TABLESPACE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER TABLE split_table.alter_common IMPORT TABLESPACE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_common/
------
SQL  ALTER IGNORE TABLE split_table.alter_probe ADD COLUMN c_ignore INT;
行为 ALTER Table(1:19~1:42) /test/1/catalog1/split_table/alter_probe/
------
SQL  ALTER TABLE test.t1 TABLESPACE ts STORAGE DISK ENGINE=NDB;
行为 ALTER Table(1:12~1:19) /test/1/catalog1/test/t1/
------
SQL  ALTER TABLE t1 ADD PARTITION (PARTITION p3 DATA DIRECTORY='G:/mysqltest/p3Data' INDEX DIRECTORY='H:/mysqltest/p3Index');
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE m1 ENGINE=MRG_MyISAM UNION=(t1,t2) INSERT_METHOD=LAST;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/m1/
------
SQL  ALTER TABLE test_ps_auto_recalc STATS_AUTO_RECALC=1;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/test_ps_auto_recalc/
------
SQL  ALTER TABLE test_ps_sample_pages_used STATS_SAMPLE_PAGES=14;
行为 ALTER Table(1:12~1:37) /test/1/catalog1/schema1/test_ps_sample_pages_used/
------
SQL  ALTER TABLE t1 AUTO_INCREMENT=10;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 AVG_ROW_LENGTH=0 CHECKSUM=0 COMMENT="" MIN_ROWS=0 MAX_ROWS=0 PACK_KEYS=DEFAULT DELAY_KEY_WRITE=0 ROW_FORMAT=DEFAULT;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE /*+ MAX_EXECUTION_TIME(100) */ t1 ADD b VARCHAR(200);
行为 ALTER Table(1:43~1:45) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE split_type_enum_set.es_core\n      ADD COLUMN e_added ENUM('new','old') AFTER e_case,\n      ADD COLUMN s_added SET('x','y','z') AFTER s_case;
行为 ALTER Table(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  ALTER TABLE split_type_enum_set.es_core\n      MODIFY COLUMN e_added ENUM('new','old','archived') DEFAULT 'new',\n      CHANGE COLUMN s_added s_flags SET('x','y','z','w') DEFAULT 'x,w';
行为 ALTER Table(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  ALTER TABLE split_type_enum_set.es_alter\n      MODIFY e ENUM('a','b','c','d'),\n      MODIFY s SET('a1','a2','a3','a4'),\n      ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:40) /test/1/catalog1/split_type_enum_set/es_alter/
------
SQL  ALTER TABLE split_type_enum_set.es_alter\n      CHANGE e e ENUM('a','b','c','d','') NOT NULL,\n      MODIFY s SET('a1','a2','a3'),\n      ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:40) /test/1/catalog1/split_type_enum_set/es_alter/
------
SQL  ALTER TABLE fk_drop_child DROP FOREIGN KEY fk_drop1,DROP FOREIGN KEY fk_drop2;
行为 ALTER Table(1:12~1:25) /test/1/catalog1/schema1/fk_drop_child/
行为 DROP Constraint(1:43~1:51) /test/1/catalog1/schema1/fk_drop1/
行为 DROP Constraint(1:69~1:77) /test/1/catalog1/schema1/fk_drop2/
------
SQL  ALTER TABLE split_check_alter DROP COLUMN f1, ADD COLUMN f1 BIGINT, ADD CONSTRAINT CHECK (f1 != 0);
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/split_check_alter/
行为 CREATE Constraint(1:68~1:98) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE spatial_generated\nADD COLUMN centroid POINT AS (ST_Centroid(g)) VIRTUAL;
行为 ALTER Table(1:12~1:29) /test/1/catalog1/schema1/spatial_generated/
行为 CALL Function(2:30~2:41) /test/1/catalog1/schema1/ST_Centroid/
------
SQL  ALTER TABLE t1 EXCHANGE PARTITION p0 WITH TABLE `` WITH VALIDATION;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE split_partition.p_range EXCHANGE PARTITION p0 WITH TABLE split_partition.p_exchange WITH VALIDATION;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range EXCHANGE PARTITION p0 WITH TABLE split_partition.p_exchange WITHOUT VALIDATION;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range DISCARD PARTITION p0 TABLESPACE;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_range IMPORT PARTITION ALL TABLESPACE;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_partition/p_range/
------
SQL  ALTER TABLE split_partition.p_list UPGRADE PARTITIONING;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_list/
------
SQL  ALTER TABLE t1 ALGORITHM=INPLACE, UPGRADE PARTITIONING;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE part_t DISCARD PARTITION p0,p2 TABLESPACE;
行为 ALTER Table(1:12~1:18) /test/1/catalog1/schema1/part_t/
------
SQL  ALTER TABLE part_t DISCARD PARTITION ALL TABLESPACE;
行为 ALTER Table(1:12~1:18) /test/1/catalog1/schema1/part_t/
------
SQL  ALTER TABLE part_t IMPORT PARTITION p0,p2 TABLESPACE;
行为 ALTER Table(1:12~1:18) /test/1/catalog1/schema1/part_t/
------
SQL  ALTER TABLE gap_part ALGORITHM DEFAULT, LOCK EXCLUSIVE, REORGANIZE PARTITION p0,p1 INTO (PARTITION p0 VALUES LESS THAN (128) TABLESPACE innodb_file_per_table);
行为 ALTER Table(1:12~1:20) /test/1/catalog1/schema1/gap_part/
------
SQL  ALTER TABLE codex_alter_audit_t CHANGE COLUMN v1 v1_new INT GENERATED ALWAYS AS(a+b) VIRTUAL AFTER a, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN c DATE GENERATED ALWAYS AS ('1999-09-09') STORED NOT NULL;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN b DATE GENERATED ALWAYS AS ('1999-09-09') VIRTUAL NOT NULL;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t MODIFY COLUMN v1 INT GENERATED ALWAYS AS(a+b) VIRTUAL AFTER a, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE split_native_gap.t_compression COMPRESSION='none';
行为 ALTER Table(1:12~1:42) /test/1/catalog1/split_native_gap/t_compression/
------
SQL  ALTER TABLE split_table.alter_probe WITH VALIDATION;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_table/alter_probe/
------
SQL  ALTER TABLE split_table.alter_probe WITHOUT VALIDATION;
行为 ALTER Table(1:12~1:35) /test/1/catalog1/split_table/alter_probe/
------
SQL  ALTER TABLE t1 ENCRYPTION="Y", ALGORITHM=COPY;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 COMPRESSION="ZLIB" KEY_BLOCK_SIZE=2;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE split_type_json.json_core\n      ADD COLUMN metadata JSON AFTER payload,\n      ADD COLUMN doc_kind VARCHAR(32)\n        GENERATED ALWAYS AS (\n          JSON_UNQUOTE(JSON_EXTRACT(doc,'$.kind'))\n        ) VIRTUAL,\n      MODIFY COLUMN payload JSON NULL,\n      CHANGE COLUMN metadata meta JSON;
行为 ALTER Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(5:10~5:22) /test/1/catalog1/schema1/JSON_UNQUOTE/
行为 CALL Function(5:23~5:35) /test/1/catalog1/schema1/JSON_EXTRACT/
------
SQL  ALTER TABLE split_type_json.json_core\n    ADD CONSTRAINT chk_json_core\n    CHECK (\n      JSON_SCHEMA_VALID(\n        '{"type":"object","required":["name"],"properties":{"name":{"type":"string"}}}',\n        doc\n      )\n    );
行为 ALTER Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/
行为 CREATE Constraint(2:19~2:32) /test/1/catalog1/schema1/chk_json_core/
行为 CALL Function(4:6~4:23) /test/1/catalog1/schema1/JSON_SCHEMA_VALID/
------
SQL  ALTER TABLE spatial_modern\nADD COLUMN p0 POINT SRID 0 NULL AFTER p;
行为 ALTER Table(1:12~1:26) /test/1/catalog1/schema1/spatial_modern/
------
SQL  ALTER TABLE codex_c13_22.t ALTER my_row_id SET INVISIBLE, RENAME COLUMN f1 TO f2, ALGORITHM=INSTANT;
行为 ALTER Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  ALTER TABLE codex_c13_22.t ALTER my_row_id SET VISIBLE, RENAME COLUMN f1 TO f2, ALGORITHM=INSTANT;
行为 ALTER Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  /*!80000 ALTER TABLE t_alter ADD COLUMN new_col INT DEFAULT 0 */;
行为 ALTER Table(1:21~1:28) /test/1/catalog1/schema1/t_alter/
------
SQL  ALTER TABLE codex_c13_22.t RENAME COLUMN i TO j, RENAME COLUMN j TO i, ALGORITHM=INPLACE PARTITION BY RANGE(i) (PARTITION p0 VALUES LESS THAN (0), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 ALTER Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  ALTER TABLE codex_c13_22.t RENAME COLUMN i TO j, RENAME COLUMN j TO i PARTITION BY RANGE(i) (PARTITION p0 VALUES LESS THAN (0), PARTITION p1 VALUES LESS THAN MAXVALUE);
行为 ALTER Table(1:12~1:26) /test/1/catalog1/codex_c13_22/t/
------
SQL  ALTER TABLE split_window_context.g_base ADD b INT AS (ROW_NUMBER() OVER (ORDER BY a));
行为 ALTER Table(1:12~1:39) /test/1/catalog1/split_window_context/g_base/
行为 CALL Function(1:54~1:64) /test/1/catalog1/schema1/ROW_NUMBER/
------
SQL  alter table `test`.`user_table` add column `address` varchar(255);
行为 ALTER Table(1:12~1:31) /test/1/catalog1/test/user_table/
------
SQL  alter table user_table add column address varchar(255) not null default 'unknown';
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table add column address varchar(255) first;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table add column address varchar(255) after name;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table add column (address varchar(255), city varchar(100));
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table drop column address;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table drop address;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table modify column address varchar(512) not null;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table user_table modify address int default 0;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  alter table test change column a1 a123456 char(10);
行为 ALTER Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  alter table test change a1 a123456 char(10) first;
行为 ALTER Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  alter table test change a1 a123456 char(10) after name;
行为 ALTER Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  alter table user_table rename column old_name to new_name;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
------
SQL  ALTER TABLE t1 DROP CHECK ck11, ALTER CHECK ck11 NOT ENFORCED, ADD CONSTRAINT ck11 CHECK (c1 > 10);
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
行为 DROP Constraint(1:26~1:30) /test/1/catalog1/schema1/ck11/
行为 ALTER Constraint(1:44~1:48) /test/1/catalog1/schema1/ck11/
行为 CREATE Constraint(1:78~1:82) /test/1/catalog1/schema1/ck11/
------
SQL  ALTER TABLE t3 DROP CHECK t3_p_ck, ADD CONSTRAINT t3_p_ck CHECK (f1 > 38);
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t3/
行为 DROP Constraint(1:26~1:33) /test/1/catalog1/schema1/t3_p_ck/
行为 CREATE Constraint(1:50~1:57) /test/1/catalog1/schema1/t3_p_ck/
------
SQL  ALTER TABLE t1 DROP CHECK t1_chk_2, DROP COLUMN f1;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
行为 DROP Constraint(1:26~1:34) /test/1/catalog1/schema1/t1_chk_2/
------
SQL  ALTER TABLE t1 MODIFY COLUMN c1 FLOAT(10.3), DROP CHECK t1_chk_1, ADD CONSTRAINT CHECK (c1 > 10.1) ENFORCED;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
行为 DROP Constraint(1:56~1:64) /test/1/catalog1/schema1/t1_chk_1/
行为 CREATE Constraint(1:66~1:107) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_UNLOAD;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  alter table `user_table` add primary key (id);
行为 ALTER Table(1:12~1:24) /test/1/catalog1/schema1/user_table/
行为 CREATE Constraint(1:25~1:45) /test/1/catalog1/schema1/
------
SQL  alter table user_table add primary key (id, name);
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
行为 CREATE Constraint(1:23~1:49) /test/1/catalog1/schema1/
------
SQL  alter table user_table add primary key using btree (id);
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
行为 CREATE Constraint(1:23~1:55) /test/1/catalog1/schema1/
------
SQL  alter table user_table add constraint pk_user primary key (id);
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
行为 CREATE Constraint(1:38~1:45) /test/1/catalog1/schema1/pk_user/
------
SQL  alter table user_table add constraint primary key (id);
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
行为 CREATE Constraint(1:23~1:54) /test/1/catalog1/schema1/
------
SQL  alter table user_table drop primary key;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
行为 DROP Constraint(1:23~1:39) /test/1/catalog1/schema1/
------
SQL  alter table abc drop foreign key `abc_fk`;
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/
行为 DROP Constraint(1:33~1:41) /test/1/catalog1/schema1/abc_fk/
------
SQL  alter table abc drop foreign key fk_test;
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/
行为 DROP Constraint(1:33~1:40) /test/1/catalog1/schema1/fk_test/
------
SQL  alter table abc add constraint chk_age check (age > 0);
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/
行为 CREATE Constraint(1:31~1:38) /test/1/catalog1/schema1/chk_age/
------
SQL  alter table abc drop check chk_age;
行为 ALTER Table(1:12~1:15) /test/1/catalog1/schema1/abc/
行为 DROP Constraint(1:27~1:34) /test/1/catalog1/schema1/chk_age/
------
SQL  alter table user_table drop constraint abc_uk;
行为 ALTER Table(1:12~1:22) /test/1/catalog1/schema1/user_table/
行为 DROP Constraint(1:39~1:45) /test/1/catalog1/schema1/abc_uk/
------
SQL  alter table test.abc comment 'abc' engine MyISAM, rename abc_test;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/test/abc/
------
SQL  alter table test.abc comment 'abc' engine MyISAM, rename test1.abc_test;
行为 ALTER Table(1:12~1:20) /test/1/catalog1/test/abc/
------
SQL  alter table schema1.abc rename to schema2.cba;
行为 ALTER Table(1:12~1:23) /test/1/catalog1/schema1/abc/
------
SQL  ALTER TABLE t1 SECONDARY_ENGINE NULL;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 SECONDARY_ENGINE_ATTRIBUTE='{"table algo":"inplace"}', ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t1 ADD COLUMN m VARCHAR(64) SECONDARY_ENGINE_ATTRIBUTE='{"add column algo":"inplace"}', ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE split_info_user_defaults ADD COLUMN c_current_alt VARCHAR(288) DEFAULT (CURRENT_USER()), ADD COLUMN c_session_alt VARCHAR(288) DEFAULT (SESSION_USER()), ADD COLUMN c_system_alt VARCHAR(288) DEFAULT (SYSTEM_USER()), ADD COLUMN c_user_alt VARCHAR(288) DEFAULT (USER());
行为 ALTER Table(1:12~1:36) /test/1/catalog1/schema1/split_info_user_defaults/
行为 CALL Function(1:84~1:96) /test/1/catalog1/schema1/CURRENT_USER/
行为 CALL Function(1:148~1:160) /test/1/catalog1/schema1/SESSION_USER/
行为 CALL Function(1:211~1:222) /test/1/catalog1/schema1/SYSTEM_USER/
行为 CALL Function(1:271~1:275) /test/1/catalog1/schema1/USER/
------
SQL  ALTER TABLE codex_constraint_c06 ADD CONSTRAINT CHECK (id > 0) NOT ENFORCED;
行为 ALTER Table(1:12~1:32) /test/1/catalog1/schema1/codex_constraint_c06/
行为 CREATE Constraint(1:33~1:75) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE codex_alter_audit_t ADD COLUMN d INT DEFAULT 5, RENAME COLUMN c TO b, DROP COLUMN b;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t CHANGE COLUMN a b INT, RENAME COLUMN b TO c, CHANGE COLUMN c d FLOAT;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t MODIFY fld3 INT GENERATED ALWAYS AS (-fld1) STORED DEFAULT -1;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE codex_alter_audit_t RENAME COLUMN m TO x, RENAME COLUMN b TO y, RENAME COLUMN c TO z;
行为 ALTER Table(1:12~1:31) /test/1/catalog1/schema1/codex_alter_audit_t/
------
SQL  ALTER TABLE t1 ALTER CHECK t1_chk_1 ENFORCED, ALGORITHM=COPY;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
行为 ALTER Constraint(1:27~1:35) /test/1/catalog1/schema1/t1_chk_1/
------
SQL  ALTER TABLE split_table.alter_modern RENAME COLUMN c TO c_new;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
------
SQL  ALTER TABLE split_table.alter_modern ALTER CHECK chk_one NOT ENFORCED;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
行为 ALTER Constraint(1:49~1:56) /test/1/catalog1/schema1/chk_one/
------
SQL  ALTER TABLE split_table.alter_modern ALTER CONSTRAINT chk_two ENFORCED;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
行为 ALTER Constraint(1:54~1:61) /test/1/catalog1/schema1/chk_two/
------
SQL  ALTER TABLE split_table.alter_modern ALTER COLUMN visible_col SET INVISIBLE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
------
SQL  ALTER TABLE split_table.alter_modern ALTER visible_col SET VISIBLE;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
------
SQL  ALTER TABLE split_table.alter_modern DROP CHECK chk_one;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
行为 DROP Constraint(1:48~1:55) /test/1/catalog1/schema1/chk_one/
------
SQL  ALTER TABLE split_table.alter_modern DROP CONSTRAINT chk_two;
行为 ALTER Table(1:12~1:36) /test/1/catalog1/split_table/alter_modern/
行为 DROP Constraint(1:53~1:60) /test/1/catalog1/schema1/chk_two/
------
SQL  ALTER TABLE t3 AUTOEXTEND_SIZE 4M;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t3/
------
SQL  ALTER TABLE split_type_json.json_core\n      ALTER COLUMN payload SET DEFAULT (JSON_ARRAY()),\n      ALTER COLUMN meta SET DEFAULT (JSON_OBJECT());
行为 ALTER Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(2:40~2:50) /test/1/catalog1/schema1/JSON_ARRAY/
行为 CALL Function(3:37~3:48) /test/1/catalog1/schema1/JSON_OBJECT/
------
SQL  ALTER TABLE gap_chk ADD f2 INT CHECK (f2<10) NOT ENFORCED, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:19) /test/1/catalog1/schema1/gap_chk/
行为 CREATE Constraint(1:31~1:57) /test/1/catalog1/schema1/
------
SQL  ALTER TABLE gap_chk ADD CONSTRAINT ck2 CHECK (f>0), DROP CHECK ck2;
行为 ALTER Table(1:12~1:19) /test/1/catalog1/schema1/gap_chk/
行为 CREATE Constraint(1:35~1:38) /test/1/catalog1/schema1/ck2/
行为 DROP Constraint(1:63~1:66) /test/1/catalog1/schema1/ck2/
------
SQL  ALTER TABLE gap_chk ALTER CHECK ck NOT ENFORCED, ALTER CONSTRAINT ck ENFORCED;
行为 ALTER Table(1:12~1:19) /test/1/catalog1/schema1/gap_chk/
行为 ALTER Constraint(1:32~1:34) /test/1/catalog1/schema1/ck/
------
SQL  ALTER TABLE gap_secondary SECONDARY_ENGINE MOCK, ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:25) /test/1/catalog1/schema1/gap_secondary/
------
SQL  ALTER TABLE gap_enc TABLESPACE=gap_ts ENCRYPTION='N', ALGORITHM=INPLACE;
行为 ALTER Table(1:12~1:19) /test/1/catalog1/schema1/gap_enc/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD PARTITION (p0,p1);
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_UNLOAD PARTITION (p0);
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE t84_types ADD COLUMN extra_col INT INVISIBLE, ALGORITHM=INSTANT;
行为 ALTER Table(1:12~1:21) /test/1/catalog1/schema1/t84_types/
------
SQL  ALTER TABLE vector_lifecycle ADD COLUMN embedding4 VECTOR(4) NULL AFTER embedding;
行为 ALTER Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/
------
SQL  ALTER TABLE vector_lifecycle MODIFY COLUMN embedding4 VECTOR(2) NULL;
行为 ALTER Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/
------
SQL  ALTER TABLE vector_lifecycle CHANGE COLUMN embedding4 embedding2 VECTOR(2) NULL;
行为 ALTER Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD VALIDATE ONLY;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD VALIDATE 100 ROWS ONLY;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD VALIDATE ALL ROWS ONLY;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD GUIDED ON;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE split_partition.p_hash SECONDARY_LOAD PARTITION (p0) VALIDATE 10 ROWS ONLY GUIDED OFF;
行为 ALTER Table(1:12~1:34) /test/1/catalog1/split_partition/p_hash/
------
SQL  ALTER TABLE splitmask.t_mask ALTER COLUMN ssn SET MASKING POLICY split_mask;
行为 ALTER Table(1:12~1:28) /test/1/catalog1/splitmask/t_mask/
------
SQL  ALTER TABLE splitmask.t_mask ALTER COLUMN ssn DROP MASKING POLICY;
行为 ALTER Table(1:12~1:28) /test/1/catalog1/splitmask/t_mask/
------
SQL  ALTER TABLE t1 ADD COLUMN j VARCHAR(256) MASKING POLICY p1;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  ALTER TABLE t4 MODIFY COLUMN i INT MASKING POLICY p1 INVISIBLE;
行为 ALTER Table(1:12~1:14) /test/1/catalog1/schema1/t4/

## ALTER_SCHEMA

SQL  ALTER DATABASE DEFAULT CHARACTER SET = utf8mb4;
行为 ALTER Schema(1:0~1:46) /test/1/catalog1/schema1/
------
SQL  ALTER SCHEMA DEFAULT COLLATE = utf8mb4_bin;
行为 ALTER Schema(1:0~1:42) /test/1/catalog1/schema1/
------
SQL  ALTER DATABASE split_db56 DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_bin;
行为 ALTER Schema(1:15~1:25) /test/1/catalog1/split_db56/
------
SQL  ALTER SCHEMA split_db56_schema DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;
行为 ALTER Schema(1:13~1:30) /test/1/catalog1/split_db56_schema/
------
SQL  ALTER DATABASE split_db56 UPGRADE DATA DIRECTORY NAME;
行为 ALTER Schema(1:15~1:25) /test/1/catalog1/split_db56/
------
SQL  ALTER DATABASE split_db57 DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_bin;
行为 ALTER Schema(1:15~1:25) /test/1/catalog1/split_db57/
------
SQL  ALTER SCHEMA split_db57_schema DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;
行为 ALTER Schema(1:13~1:30) /test/1/catalog1/split_db57_schema/
------
SQL  ALTER DATABASE split_db57 UPGRADE DATA DIRECTORY NAME;
行为 ALTER Schema(1:15~1:25) /test/1/catalog1/split_db57/
------
SQL  ALTER DATABASE audit_admin_db READ ONLY = 1 READ ONLY = 0;
行为 ALTER Schema(1:15~1:29) /test/1/catalog1/audit_admin_db/
------
SQL  ALTER DATABASE audit_admin_db READ ONLY = 1 READ ONLY = 1;
行为 ALTER Schema(1:15~1:29) /test/1/catalog1/audit_admin_db/
------
SQL  ALTER DATABASE split_db80_modern READ ONLY = 1;
行为 ALTER Schema(1:15~1:32) /test/1/catalog1/split_db80_modern/
------
SQL  ALTER DATABASE split_db80_modern READ ONLY = 0 DEFAULT ENCRYPTION = 'N';
行为 ALTER Schema(1:15~1:32) /test/1/catalog1/split_db80_modern/
------
SQL  alter schema abc default character set utf8mb4;
行为 ALTER Schema(1:13~1:16) /test/1/catalog1/abc/
------
SQL  alter schema abc default character set utf8mb4 collate utf8mb4_unicode_ci;
行为 ALTER Schema(1:13~1:16) /test/1/catalog1/abc/
------
SQL  alter database test character set utf8mb4;
行为 ALTER Schema(1:15~1:19) /test/1/catalog1/test/
------
SQL  alter database test character set utf8mb4 collate utf8mb4_unicode_ci;
行为 ALTER Schema(1:15~1:19) /test/1/catalog1/test/
------
SQL  ALTER DATABASE split84_db_a READ ONLY = 1;
行为 ALTER Schema(1:15~1:27) /test/1/catalog1/split84_db_a/
------
SQL  ALTER DATABASE split84_db_a READ ONLY = 0 DEFAULT COLLATE utf8mb4_bin;
行为 ALTER Schema(1:15~1:27) /test/1/catalog1/split84_db_a/
------
SQL  ALTER SCHEMA split84_db_b DEFAULT ENCRYPTION = 'N' READ ONLY = DEFAULT;
行为 ALTER Schema(1:13~1:25) /test/1/catalog1/split84_db_b/
------
SQL  ALTER DATABASE split_db97 DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_bin;
行为 ALTER Schema(1:15~1:25) /test/1/catalog1/split_db97/
------
SQL  ALTER SCHEMA split_db97_schema DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;
行为 ALTER Schema(1:13~1:30) /test/1/catalog1/split_db97_schema/
------
SQL  ALTER DATABASE split_db97_modern READ ONLY = 1;
行为 ALTER Schema(1:15~1:32) /test/1/catalog1/split_db97_modern/
------
SQL  ALTER DATABASE split_db97_modern READ ONLY = 0 DEFAULT ENCRYPTION = 'N';
行为 ALTER Schema(1:15~1:32) /test/1/catalog1/split_db97_modern/

## CREATE_SCHEMA

SQL  CREATE DATABASE IF NOT EXISTS split_db56 DEFAULT CHARACTER SET = utf8mb4 DEFAULT COLLATE = utf8mb4_general_ci;
行为 CREATE Schema(1:30~1:40) /test/1/catalog1/split_db56/
------
SQL  CREATE SCHEMA IF NOT EXISTS split_db56_schema CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
行为 CREATE Schema(1:28~1:45) /test/1/catalog1/split_db56_schema/
------
SQL  CREATE DATABASE db345678901234567890123456789012345678901234567890123456789012345;
行为 CREATE Schema(1:16~1:81) /test/1/catalog1/db345678901234567890123456789012345678901234567890123456789012345/
------
SQL  CREATE DATABASE db34567890123456789012345678901234567890123456789012345678901234;
行为 CREATE Schema(1:16~1:80) /test/1/catalog1/db34567890123456789012345678901234567890123456789012345678901234/
------
SQL  CREATE DATABASE IF NOT EXISTS split_db57 DEFAULT CHARACTER SET = utf8mb4 DEFAULT COLLATE = utf8mb4_general_ci;
行为 CREATE Schema(1:30~1:40) /test/1/catalog1/split_db57/
------
SQL  CREATE SCHEMA IF NOT EXISTS split_db57_schema CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
行为 CREATE Schema(1:28~1:45) /test/1/catalog1/split_db57_schema/
------
SQL  CREATE DATABASE split_db80_modern DEFAULT ENCRYPTION = 'N';
行为 CREATE Schema(1:16~1:33) /test/1/catalog1/split_db80_modern/
------
SQL  create database `abc`;
行为 CREATE Schema(1:16~1:21) /test/1/catalog1/abc/
------
SQL  create schema abc;
行为 CREATE Schema(1:14~1:17) /test/1/catalog1/abc/
------
SQL  create schema `abc`;
行为 CREATE Schema(1:14~1:19) /test/1/catalog1/abc/
------
SQL  create database abc default collate utf8mb4_unicode_ci;
行为 CREATE Schema(1:16~1:19) /test/1/catalog1/abc/
------
SQL  create database abc default character set utf8mb4;
行为 CREATE Schema(1:16~1:19) /test/1/catalog1/abc/
------
SQL  create database abc default character set utf8mb4 collate utf8mb4_unicode_ci;
行为 CREATE Schema(1:16~1:19) /test/1/catalog1/abc/
------
SQL  create database if not exists abc;
行为 CREATE Schema(1:30~1:33) /test/1/catalog1/abc/
------
SQL  create database test;
行为 CREATE Schema(1:16~1:20) /test/1/catalog1/test/
------
SQL  create database if not exists test;
行为 CREATE Schema(1:30~1:34) /test/1/catalog1/test/
------
SQL  create database test character set utf8mb4;
行为 CREATE Schema(1:16~1:20) /test/1/catalog1/test/
------
SQL  create database test character set utf8mb4 collate utf8mb4_unicode_ci;
行为 CREATE Schema(1:16~1:20) /test/1/catalog1/test/
------
SQL  create schema test_schema;
行为 CREATE Schema(1:14~1:25) /test/1/catalog1/test_schema/
------
SQL  CREATE schema sampledb;
行为 CREATE Schema(1:14~1:22) /test/1/catalog1/sampledb/
------
SQL  CREATE DATABASE IF NOT EXISTS split84_db_a\n  DEFAULT CHARACTER SET = utf8mb4\n  DEFAULT COLLATE = utf8mb4_0900_ai_ci\n  DEFAULT ENCRYPTION = 'N';
行为 CREATE Schema(1:30~1:42) /test/1/catalog1/split84_db_a/
------
SQL  CREATE SCHEMA IF NOT EXISTS split84_db_b\n  CHARSET utf8mb4\n  ENCRYPTION 'N';
行为 CREATE Schema(1:28~1:40) /test/1/catalog1/split84_db_b/
------
SQL  CREATE DATABASE IF NOT EXISTS split_db97 DEFAULT CHARACTER SET = utf8mb4 DEFAULT COLLATE = utf8mb4_general_ci;
行为 CREATE Schema(1:30~1:40) /test/1/catalog1/split_db97/
------
SQL  CREATE SCHEMA IF NOT EXISTS split_db97_schema CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
行为 CREATE Schema(1:28~1:45) /test/1/catalog1/split_db97_schema/
------
SQL  CREATE DATABASE split_db97_modern DEFAULT ENCRYPTION = 'N';
行为 CREATE Schema(1:16~1:33) /test/1/catalog1/split_db97_modern/

## DROP_TABLE

SQL  DROP TABLE `semi;table`;
行为 DROP Table(1:11~1:23) /test/1/catalog1/schema1/semi;table/
------
SQL  /*!50000 DROP TABLE t */;
行为 DROP Table(1:20~1:21) /test/1/catalog1/schema1/t/
------
SQL  DROP TABLE IF EXISTS split_table.base, split_table.like_a, split_table.like_b;
行为 DROP Table(1:21~1:37) /test/1/catalog1/split_table/base/
行为 DROP Table(1:39~1:57) /test/1/catalog1/split_table/like_a/
行为 DROP Table(1:59~1:77) /test/1/catalog1/split_table/like_b/
------
SQL  DROP TABLES IF EXISTS split_table.drop_plural_a, split_table.drop_plural_b;
行为 DROP Table(1:22~1:47) /test/1/catalog1/split_table/drop_plural_a/
行为 DROP Table(1:49~1:74) /test/1/catalog1/split_table/drop_plural_b/
------
SQL  DROP TABLE split_table.like_a RESTRICT;
行为 DROP Table(1:11~1:29) /test/1/catalog1/split_table/like_a/
------
SQL  DROP TABLE split_table.like_b CASCADE;
行为 DROP Table(1:11~1:29) /test/1/catalog1/split_table/like_b/
------
SQL  DROP TABLE IF EXISTS split_table.as_select, split_table.as_renamed, split_table.ignore_select, split_table.replace_select CASCADE;
行为 DROP Table(1:21~1:42) /test/1/catalog1/split_table/as_select/
行为 DROP Table(1:44~1:66) /test/1/catalog1/split_table/as_renamed/
行为 DROP Table(1:68~1:93) /test/1/catalog1/split_table/ignore_select/
行为 DROP Table(1:95~1:121) /test/1/catalog1/split_table/replace_select/
------
SQL  DROP TEMPORARY TABLE IF EXISTS split_table.tmp_session RESTRICT;
行为 DROP Table(1:31~1:54) /test/1/catalog1/split_table/tmp_session/
------
SQL  DROP TEMPORARY TABLE split_table.tmp_drop_no_if_exists;
行为 DROP Table(1:21~1:54) /test/1/catalog1/split_table/tmp_drop_no_if_exists/
------
SQL  DROP TEMPORARY TABLE IF EXISTS t_tmp CASCADE;
行为 DROP Table(1:31~1:36) /test/1/catalog1/schema1/t_tmp/
------
SQL  /*!80000 DROP TABLE IF EXISTS t_old */;
行为 DROP Table(1:30~1:35) /test/1/catalog1/schema1/t_old/
------
SQL  DROP TABLE splitv.dt_drop_restrict RESTRICT;
行为 DROP Table(1:11~1:34) /test/1/catalog1/splitv/dt_drop_restrict/
------
SQL  DROP TABLES IF EXISTS splitv.dt_drop_plural_a, splitv.dt_drop_plural_b;
行为 DROP Table(1:22~1:45) /test/1/catalog1/splitv/dt_drop_plural_a/
行为 DROP Table(1:47~1:70) /test/1/catalog1/splitv/dt_drop_plural_b/
------
SQL  DROP TABLE splitv.dt_drop_cascade CASCADE;
行为 DROP Table(1:11~1:33) /test/1/catalog1/splitv/dt_drop_cascade/
------
SQL  DROP TABLE IF EXISTS splitv.dt_drop_exists_a, splitv.dt_drop_exists_missing CASCADE;
行为 DROP Table(1:21~1:44) /test/1/catalog1/splitv/dt_drop_exists_a/
行为 DROP Table(1:46~1:75) /test/1/catalog1/splitv/dt_drop_exists_missing/
------
SQL  DROP TEMPORARY TABLE tmp_drop_one;
行为 DROP Table(1:21~1:33) /test/1/catalog1/schema1/tmp_drop_one/
------
SQL  DROP TEMPORARY TABLE IF EXISTS tmp_drop_two, tmp_drop_missing RESTRICT;
行为 DROP Table(1:31~1:43) /test/1/catalog1/schema1/tmp_drop_two/
行为 DROP Table(1:45~1:61) /test/1/catalog1/schema1/tmp_drop_missing/
------
SQL  drop table abc;
行为 DROP Table(1:11~1:14) /test/1/catalog1/schema1/abc/
------
SQL  drop table if exists abc;
行为 DROP Table(1:21~1:24) /test/1/catalog1/schema1/abc/
------
SQL  drop table test.abc, test.def;
行为 DROP Table(1:11~1:19) /test/1/catalog1/test/abc/
行为 DROP Table(1:21~1:29) /test/1/catalog1/test/def/
------
SQL  DROP TABLE employees;
行为 DROP Table(1:11~1:20) /test/1/catalog1/schema1/employees/
------
SQL  DROP TABLE t84_types;
行为 DROP Table(1:11~1:20) /test/1/catalog1/schema1/t84_types/
------
SQL  DROP TABLE splitvector.t_vector_dims;
行为 DROP Table(1:11~1:36) /test/1/catalog1/splitvector/t_vector_dims/
------
SQL  DROP TABLE splitvector.t_vector_default;
行为 DROP Table(1:11~1:39) /test/1/catalog1/splitvector/t_vector_default/

## ADD_INDEX

SQL  CREATE INDEX idx1 ON `` (c1);
行为 CREATE Index(1:13~1:17) /test/1/catalog1/schema1/idx1/

## DROP_INDEX

SQL  DROP INDEX idx1 ON ``;
行为 DROP Index(1:11~1:15) /test/1/catalog1/schema1/idx1/

## CREATE_VIEW

SQL  CREATE VIEW v_hint AS SELECT /*+ QB_NAME(a) */ 1 AS i;
行为 CREATE View(1:12~1:18) /test/1/catalog1/schema1/v_hint/
------
SQL  CREATE VIEW gcat_view(c0) AS SELECT GROUP_CONCAT((SELECT 1),1);
行为 CREATE View(1:12~1:21) /test/1/catalog1/schema1/gcat_view/
行为 CALL Function(1:36~1:48) /test/1/catalog1/schema1/GROUP_CONCAT/
------
SQL  CREATE VIEW math_unary_view AS SELECT -(1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:27) /test/1/catalog1/schema1/math_unary_view/
------
SQL  CREATE VIEW str_char_view AS SELECT CHAR(0x414243) AS char_value;
行为 CREATE View(1:12~1:25) /test/1/catalog1/schema1/str_char_view/
行为 CALL Function(1:36~1:40) /test/1/catalog1/schema1/CHAR/
------
SQL  CREATE VIEW str_lpad_not_in_view AS SELECT LPAD('x', 1 NOT IN (0), 1) AS value_text;
行为 CREATE View(1:12~1:32) /test/1/catalog1/schema1/str_lpad_not_in_view/
行为 CALL Function(1:43~1:47) /test/1/catalog1/schema1/LPAD/
------
SQL  CREATE VIEW str_substr_not_in_view AS SELECT SUBSTR('x', 1, 1 NOT IN (0)) AS value_text;
行为 CREATE View(1:12~1:34) /test/1/catalog1/schema1/str_substr_not_in_view/
行为 CALL Function(1:45~1:51) /test/1/catalog1/schema1/SUBSTR/
------
SQL  CREATE VIEW temporal_precision_view AS SELECT NOW(6) AS now_6,CURTIME(4) AS curtime_4,LOCALTIME(3) AS localtime_3,CURRENT_TIME(2) AS current_time_2,LOCALTIMESTAMP(1) AS localtimestamp_1,UTC_TIMESTAMP(4) AS utc_timestamp_4;
行为 CREATE View(1:12~1:35) /test/1/catalog1/schema1/temporal_precision_view/
行为 CALL Function(1:46~1:49) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:62~1:69) /test/1/catalog1/schema1/CURTIME/
行为 CALL Function(1:86~1:95) /test/1/catalog1/schema1/LOCALTIME/
行为 CALL Function(1:114~1:126) /test/1/catalog1/schema1/CURRENT_TIME/
行为 CALL Function(1:148~1:162) /test/1/catalog1/schema1/LOCALTIMESTAMP/
行为 CALL Function(1:186~1:199) /test/1/catalog1/schema1/UTC_TIMESTAMP/
------
SQL  CREATE VIEW weight_introducer_view AS SELECT WEIGHT_STRING(_latin1 'ab') AS weight_value;
行为 CREATE View(1:12~1:34) /test/1/catalog1/schema1/weight_introducer_view/
行为 CALL Function(1:45~1:58) /test/1/catalog1/schema1/WEIGHT_STRING/
------
SQL  CREATE VIEW time_literal_view AS SELECT TIME'-24:00:00.000001' AS negative_time,CAST('838:59:59.000000' AS TIME(6)) AS maximum_time;
行为 CREATE View(1:12~1:29) /test/1/catalog1/schema1/time_literal_view/
行为 CALL Function(1:80~1:84) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE VIEW v_rpad AS SELECT RPAD('x', 1 NOT IN (0), 1) AS c;
行为 CREATE View(1:12~1:18) /test/1/catalog1/schema1/v_rpad/
行为 CALL Function(1:29~1:33) /test/1/catalog1/schema1/RPAD/
------
SQL  CREATE VIEW v_sha2 AS SELECT SHA2('x', 1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:18) /test/1/catalog1/schema1/v_sha2/
行为 CALL Function(1:29~1:33) /test/1/catalog1/schema1/SHA2/
------
SQL  CREATE VIEW v_substr_pos AS SELECT SUBSTR('x', 1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:24) /test/1/catalog1/schema1/v_substr_pos/
行为 CALL Function(1:35~1:41) /test/1/catalog1/schema1/SUBSTR/
------
SQL  CREATE VIEW v_repeat AS SELECT REPEAT('x', 1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:20) /test/1/catalog1/schema1/v_repeat/
行为 CALL Function(1:31~1:37) /test/1/catalog1/schema1/REPEAT/
------
SQL  CREATE VIEW v_space AS SELECT SPACE(1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:19) /test/1/catalog1/schema1/v_space/
行为 CALL Function(1:30~1:35) /test/1/catalog1/schema1/SPACE/
------
SQL  CREATE VIEW v_left AS SELECT LEFT('x', 1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:18) /test/1/catalog1/schema1/v_left/
行为 CALL Function(1:29~1:33) /test/1/catalog1/schema1/LEFT/
------
SQL  CREATE VIEW v_right AS SELECT RIGHT('x', 1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:19) /test/1/catalog1/schema1/v_right/
行为 CALL Function(1:30~1:35) /test/1/catalog1/schema1/RIGHT/
------
SQL  CREATE VIEW v_str_to_date AS SELECT STR_TO_DATE('2020', 1 NOT IN (0)) AS c;
行为 CREATE View(1:12~1:25) /test/1/catalog1/schema1/v_str_to_date/
行为 CALL Function(1:36~1:47) /test/1/catalog1/schema1/STR_TO_DATE/
------
SQL  CREATE VIEW split_subquery_bugs_860.v_alias AS SELECT 1 FROM (SELECT 1) AS table1(pk) JOIN (SELECT 1) AS table2 ON table1.pk=(SELECT 1) WHERE table1.pk IN ((SELECT 1),2);
行为 CREATE View(1:12~1:43) /test/1/catalog1/split_subquery_bugs_860/v_alias/
------
SQL  CREATE VIEW split_subquery_eleventh.v_predicates AS SELECT 1 LIKE (1 IN (SELECT 1)) AS p1,1 LIKE '%' ESCAPE (1 IN (SELECT 1)) AS p2;
行为 CREATE View(1:12~1:48) /test/1/catalog1/split_subquery_eleventh/v_predicates/
------
SQL  CREATE VIEW codex_load_audit_s.v AS SELECT 1 INTO @codex_load_audit_v;
行为 CREATE View(1:12~1:32) /test/1/catalog1/codex_load_audit_s/v/
行为 READ ConfigKey(1:50~1:69) /test/1/codex_load_audit_v/
------
SQL  CREATE VIEW v_paren_gap AS (SELECT 1 AS a);
行为 CREATE View(1:12~1:23) /test/1/catalog1/schema1/v_paren_gap/
------
SQL  CREATE VIEW v_union_gap AS (SELECT 1 AS a) UNION (SELECT 2 AS a);
行为 CREATE View(1:12~1:23) /test/1/catalog1/schema1/v_union_gap/
------
SQL  CREATE VIEW split_view_outfile AS SELECT 5 INTO OUTFILE '/tmp/split_native_gap_view.txt';
行为 CREATE View(1:12~1:30) /test/1/catalog1/schema1/split_view_outfile/
行为 EXPORT File(1:56~1:88) /test/1/tmp/split_native_gap_view.txt/
------
SQL  CREATE VIEW codex_year_view(c0) AS (SELECT CAST(2048 AS YEAR));
行为 CREATE View(1:12~1:27) /test/1/catalog1/schema1/codex_year_view/
行为 CALL Function(1:43~1:47) /test/1/catalog1/schema1/CAST/
------
SQL  CREATE VIEW split_view80.v_values AS VALUES ROW(1, 10, 'a'), ROW(2, 20, 'b');
行为 CREATE View(1:12~1:33) /test/1/catalog1/split_view80/v_values/
------
SQL  CREATE VIEW v AS SELECT * FROM (WITH qn AS (SELECT 'with') SELECT * FROM qn) AS dt;
行为 CREATE View(1:12~1:13) /test/1/catalog1/schema1/v/
------
SQL  CREATE VIEW json_value_view AS SELECT JSON_VALUE('{"don''t":"panic"}','$."don''t"') AS x,JSON_VALUE('""','$' RETURNING CHAR(10) DEFAULT 'it''s empty' ON EMPTY DEFAULT 'it''s wrong' ON ERROR) AS y,JSON_VALUE('""','$' NULL ON EMPTY ERROR ON ERROR) AS z,JSON_VALUE('""','$' ERROR ON EMPTY NULL ON ERROR) AS w;
行为 CREATE View(1:12~1:27) /test/1/catalog1/schema1/json_value_view/
行为 CALL Function(1:38~1:48) /test/1/catalog1/schema1/JSON_VALUE/
------
SQL  CREATE VIEW split84.v84_values_stmt AS VALUES ROW(1, 'a'), ROW(2, 'b');
行为 CREATE View(1:12~1:35) /test/1/catalog1/split84/v84_values_stmt/
------
SQL  CREATE MATERIALIZED VIEW split_ext.mv_basic AS SELECT 1 AS id;
行为 CREATE Materialized(1:25~1:43) /test/1/catalog1/split_ext/mv_basic/
------
SQL  CREATE VIEW split_view97.v_values AS VALUES ROW(1, 10, 'a'), ROW(2, 20, 'b');
行为 CREATE View(1:12~1:33) /test/1/catalog1/split_view97/v_values/
------
SQL  CREATE MATERIALIZED VIEW IF NOT EXISTS audit_mv_ifne AS SELECT 1 AS id;
行为 CREATE Materialized(1:39~1:52) /test/1/catalog1/schema1/audit_mv_ifne/
------
SQL  CREATE MATERIALIZED DEFINER=CURRENT_USER SQL SECURITY INVOKER VIEW audit_mv_def AS SELECT 1 AS id;
行为 CREATE Materialized(1:67~1:79) /test/1/catalog1/schema1/audit_mv_def/
------
SQL  CREATE MATERIALIZED VIEW audit_mv_values AS VALUES ROW(1), ROW(2);
行为 CREATE Materialized(1:25~1:40) /test/1/catalog1/schema1/audit_mv_values/
------
SQL  CREATE MATERIALIZED VIEW audit_mv_cte AS WITH q AS (SELECT 1 AS id) SELECT id FROM q;
行为 CREATE Materialized(1:25~1:37) /test/1/catalog1/schema1/audit_mv_cte/

## ALTER_VIEW

SQL  ALTER VIEW v_hint AS SELECT /*+ BAD_HINT */ 2 AS i;
行为 ALTER View(1:11~1:17) /test/1/catalog1/schema1/v_hint/
------
SQL  CREATE OR REPLACE VIEW v_where AS SELECT 1 WHERE 1;
行为 CREATE View(1:23~1:30) /test/1/catalog1/schema1/v_where/
------
SQL  ALTER VIEW audit_v AS VALUES ROW(1,'a'), ROW(2,'b');
行为 ALTER View(1:11~1:18) /test/1/catalog1/schema1/audit_v/
------
SQL  ALTER VIEW audit_v AS WITH q AS (SELECT 1 AS id) SELECT id FROM q;
行为 ALTER View(1:11~1:18) /test/1/catalog1/schema1/audit_v/
------
SQL  CREATE OR REPLACE ALGORITHM = MERGE DEFINER = CURRENT_USER SQL SECURITY INVOKER VIEW split_info_current_user_v AS SELECT 1 AS id;
行为 CREATE View(1:85~1:110) /test/1/catalog1/schema1/split_info_current_user_v/
------
SQL  ALTER ALGORITHM = UNDEFINED DEFINER = CURRENT_USER() SQL SECURITY INVOKER VIEW split_info_current_user_v AS SELECT 2 AS id;
行为 ALTER View(1:79~1:104) /test/1/catalog1/schema1/split_info_current_user_v/
------
SQL  CREATE OR REPLACE ALGORITHM=MERGE MATERIALIZED VIEW split_ext.mv_replace AS SELECT 2 AS id;
行为 CREATE Materialized(1:52~1:72) /test/1/catalog1/split_ext/mv_replace/
------
SQL  ALTER MATERIALIZED VIEW split_ext.mv_basic AS SELECT 3 AS id;
行为 ALTER Materialized(1:24~1:42) /test/1/catalog1/split_ext/mv_basic/
------
SQL  ALTER ALGORITHM=TEMPTABLE MATERIALIZED VIEW audit_mv_ifne AS SELECT 2 AS id;
行为 ALTER Materialized(1:44~1:57) /test/1/catalog1/schema1/audit_mv_ifne/
------
SQL  ALTER MATERIALIZED DEFINER=CURRENT_USER SQL SECURITY INVOKER VIEW audit_mv_ifne AS SELECT 2 AS id;
行为 ALTER Materialized(1:66~1:79) /test/1/catalog1/schema1/audit_mv_ifne/

## CREATE_TABLESPACE

SQL  CREATE TABLESPACE split_general_a ADD DATAFILE 'split_general_a.ibd' ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:33) /test/1/catalog1/schema1/split_general_a/
------
SQL  CREATE TABLESPACE split_ts ADD DATAFILE 'split_data.dat' USE LOGFILE GROUP split_lg INITIAL_SIZE=12M ENGINE=NDB;
行为 CREATE Tablespace(1:18~1:26) /test/1/catalog1/schema1/split_ts/
------
SQL  CREATE TABLESPACE split_ts_options_all ADD DATAFILE 'split_ts_options_all.ibd' COMMENT='all options', NODEGROUP=1 MAX_SIZE=64M AUTOEXTEND_SIZE=4M INITIAL_SIZE=16M EXTENT_SIZE=1M WAIT STORAGE ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:38) /test/1/catalog1/schema1/split_ts_options_all/
------
SQL  CREATE TABLESPACE split_ts_ndb_options ADD DATAFILE 'split_data_options.dat' USE LOGFILE GROUP split_lg_options COMMENT='all options', NODEGROUP=1 MAX_SIZE=64M AUTOEXTEND_SIZE=4M INITIAL_SIZE=16M EXTENT_SIZE=1M WAIT STORAGE ENGINE=NDB;
行为 CREATE Tablespace(1:18~1:38) /test/1/catalog1/schema1/split_ts_ndb_options/
------
SQL  CREATE TABLESPACE split_ts_file_block ADD DATAFILE 'split_ts_file_block.ibd' FILE_BLOCK_SIZE=16K STORAGE ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:37) /test/1/catalog1/schema1/split_ts_file_block/
------
SQL  CREATE TABLESPACE audit_ts_no_file ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:34) /test/1/catalog1/schema1/audit_ts_no_file/
------
SQL  CREATE TABLESPACE split_ts_modern_80 ADD DATAFILE 'split_ts_modern_80.ibd' FILE_BLOCK_SIZE=16K, ENCRYPTION='N' ENGINE_ATTRIBUTE='{}' STORAGE ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:36) /test/1/catalog1/schema1/split_ts_modern_80/
------
SQL  CREATE UNDO TABLESPACE split_undo_80 ADD DATAFILE 'split_undo_80.ibu' ENGINE=InnoDB;
行为 CREATE Tablespace(1:23~1:36) /test/1/catalog1/schema1/split_undo_80/
------
SQL  CREATE UNDO TABLESPACE audit_undo ADD DATAFILE 'audit_undo.ibu';
行为 CREATE Tablespace(1:23~1:33) /test/1/catalog1/schema1/audit_undo/
------
SQL  CREATE TABLESPACE split_ts_modern_84 ADD DATAFILE 'split_ts_modern_84.ibd' FILE_BLOCK_SIZE=16K, ENCRYPTION='N' ENGINE_ATTRIBUTE='{}' STORAGE ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:36) /test/1/catalog1/schema1/split_ts_modern_84/
------
SQL  CREATE UNDO TABLESPACE split_undo_84 ADD DATAFILE 'split_undo_84.ibu' ENGINE=InnoDB;
行为 CREATE Tablespace(1:23~1:36) /test/1/catalog1/schema1/split_undo_84/
------
SQL  CREATE TABLESPACE split_ts_modern_97 ADD DATAFILE 'split_ts_modern_97.ibd' FILE_BLOCK_SIZE=16K, ENCRYPTION='N' ENGINE_ATTRIBUTE='{}' STORAGE ENGINE=InnoDB;
行为 CREATE Tablespace(1:18~1:36) /test/1/catalog1/schema1/split_ts_modern_97/
------
SQL  CREATE UNDO TABLESPACE split_undo_97 ADD DATAFILE 'split_undo_97.ibu' ENGINE=InnoDB;
行为 CREATE Tablespace(1:23~1:36) /test/1/catalog1/schema1/split_undo_97/

## DROP_TABLESPACE

SQL  DROP TABLESPACE split_general_a;
行为 DROP Tablespace(1:16~1:31) /test/1/catalog1/schema1/split_general_a/
------
SQL  DROP TABLESPACE split_ts ENGINE=NDB;
行为 DROP Tablespace(1:16~1:24) /test/1/catalog1/schema1/split_ts/
------
SQL  DROP TABLESPACE split_ts_options_all;
行为 DROP Tablespace(1:16~1:36) /test/1/catalog1/schema1/split_ts_options_all/
------
SQL  DROP TABLESPACE split_ts_file_block;
行为 DROP Tablespace(1:16~1:35) /test/1/catalog1/schema1/split_ts_file_block/
------
SQL  DROP TABLESPACE split_ts_modern_80 STORAGE ENGINE=InnoDB;
行为 DROP Tablespace(1:16~1:34) /test/1/catalog1/schema1/split_ts_modern_80/
------
SQL  DROP UNDO TABLESPACE split_undo_80 ENGINE InnoDB;
行为 DROP Tablespace(1:21~1:34) /test/1/catalog1/schema1/split_undo_80/
------
SQL  DROP UNDO TABLESPACE audit_undo_missing;
行为 DROP Tablespace(1:21~1:39) /test/1/catalog1/schema1/audit_undo_missing/
------
SQL  DROP TABLESPACE split_ts_modern_84 STORAGE ENGINE=InnoDB;
行为 DROP Tablespace(1:16~1:34) /test/1/catalog1/schema1/split_ts_modern_84/
------
SQL  DROP UNDO TABLESPACE split_undo_84 ENGINE InnoDB;
行为 DROP Tablespace(1:21~1:34) /test/1/catalog1/schema1/split_undo_84/
------
SQL  DROP TABLESPACE split_ts_modern_97 STORAGE ENGINE=InnoDB;
行为 DROP Tablespace(1:16~1:34) /test/1/catalog1/schema1/split_ts_modern_97/
------
SQL  DROP UNDO TABLESPACE split_undo_97 ENGINE InnoDB;
行为 DROP Tablespace(1:21~1:34) /test/1/catalog1/schema1/split_undo_97/

## ALTER_LOG

SQL  ALTER LOGFILE GROUP audit_lg ADD UNDOFILE 'audit_lg2.dat' ENGINE=NDB;
行为 ALTER Log(1:20~1:28) /test/1/catalog1/schema1/audit_lg/
------
SQL  ALTER LOGFILE GROUP split_lg ADD UNDOFILE 'split_undo2.dat' INITIAL_SIZE=4M ENGINE=NDB;
行为 ALTER Log(1:20~1:28) /test/1/catalog1/schema1/split_lg/
------
SQL  ALTER LOGFILE GROUP split_lg ADD REDOFILE 'split_redo.dat' INITIAL_SIZE=4M ENGINE=NDB;
行为 ALTER Log(1:20~1:28) /test/1/catalog1/schema1/split_lg/
------
SQL  ALTER LOGFILE GROUP split_lg_options ADD UNDOFILE 'split_undo_options_2.dat' WAIT, INITIAL_SIZE=4M STORAGE ENGINE=NDB;
行为 ALTER Log(1:20~1:36) /test/1/catalog1/schema1/split_lg_options/

## CREATE_LOG

SQL  CREATE LOGFILE GROUP audit_lg ADD UNDOFILE 'audit_lg.dat' ENGINE=NDB;
行为 CREATE Log(1:21~1:29) /test/1/catalog1/schema1/audit_lg/
------
SQL  CREATE LOGFILE GROUP split_lg ADD UNDOFILE 'split_undo.dat' INITIAL_SIZE=16M UNDO_BUFFER_SIZE=1M ENGINE=NDB;
行为 CREATE Log(1:21~1:29) /test/1/catalog1/schema1/split_lg/
------
SQL  CREATE LOGFILE GROUP split_lg_options ADD UNDOFILE 'split_undo_options.dat' COMMENT='all options', NODEGROUP=1 REDO_BUFFER_SIZE=2M UNDO_BUFFER_SIZE=1M INITIAL_SIZE=16M WAIT STORAGE ENGINE=NDB;
行为 CREATE Log(1:21~1:37) /test/1/catalog1/schema1/split_lg_options/

## DROP_LOG

SQL  DROP LOGFILE GROUP audit_lg ENGINE NDB;
行为 DROP Log(1:19~1:27) /test/1/catalog1/schema1/audit_lg/
------
SQL  DROP LOGFILE GROUP split_lg ENGINE=NDB;
行为 DROP Log(1:19~1:27) /test/1/catalog1/schema1/split_lg/

## ALTER_TABLESPACE

SQL  ALTER TABLESPACE split_ts ADD DATAFILE 'split_data2.dat' INITIAL_SIZE=4M ENGINE=NDB;
行为 ALTER Tablespace(1:17~1:25) /test/1/catalog1/schema1/split_ts/
------
SQL  ALTER TABLESPACE split_ts DROP DATAFILE 'split_data2.dat' ENGINE=NDB;
行为 ALTER Tablespace(1:17~1:25) /test/1/catalog1/schema1/split_ts/
------
SQL  ALTER TABLESPACE split_ts_legacy CHANGE DATAFILE 'split_ts_legacy.dat' INITIAL_SIZE=1M AUTOEXTEND_SIZE=1M MAX_SIZE=2M;
行为 ALTER Tablespace(1:17~1:32) /test/1/catalog1/schema1/split_ts_legacy/
------
SQL  ALTER TABLESPACE split_ts_legacy READ_ONLY;
行为 ALTER Tablespace(1:17~1:32) /test/1/catalog1/schema1/split_ts_legacy/
------
SQL  ALTER TABLESPACE split_ts_legacy READ_WRITE;
行为 ALTER Tablespace(1:17~1:32) /test/1/catalog1/schema1/split_ts_legacy/
------
SQL  ALTER TABLESPACE split_ts_legacy NOT ACCESSIBLE;
行为 ALTER Tablespace(1:17~1:32) /test/1/catalog1/schema1/split_ts_legacy/
------
SQL  ALTER TABLESPACE split_ts_ndb_options ADD DATAFILE 'split_data_options_2.dat' WAIT, MAX_SIZE=64M AUTOEXTEND_SIZE=4M INITIAL_SIZE=8M STORAGE ENGINE=NDB;
行为 ALTER Tablespace(1:17~1:37) /test/1/catalog1/schema1/split_ts_ndb_options/
------
SQL  ALTER TABLESPACE s_def ADD DATAFILE 'bad2.ibd' NO_WAIT;
行为 ALTER Tablespace(1:17~1:22) /test/1/catalog1/schema1/s_def/
------
SQL  ALTER TABLESPACE split_general_a AUTOEXTEND_SIZE=32M;
行为 ALTER Tablespace(1:17~1:32) /test/1/catalog1/schema1/split_general_a/
------
SQL  ALTER TABLESPACE split_general_a RENAME TO split_general_b;
行为 ALTER Tablespace(1:17~1:32) /test/1/catalog1/schema1/split_general_a/
行为 ALTER Tablespace(1:43~1:58) /test/1/catalog1/schema1/split_general_b/
------
SQL  ALTER TABLESPACE split_ts_modern_80 ENCRYPTION='N', ENGINE_ATTRIBUTE='{}' STORAGE ENGINE=InnoDB WAIT;
行为 ALTER Tablespace(1:17~1:35) /test/1/catalog1/schema1/split_ts_modern_80/
------
SQL  ALTER UNDO TABLESPACE split_undo_80 SET INACTIVE ENGINE=InnoDB;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_80/
------
SQL  ALTER UNDO TABLESPACE split_undo_80 SET ACTIVE ENGINE InnoDB;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_80/
------
SQL  ALTER UNDO TABLESPACE split_undo_80 SET INACTIVE;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_80/
------
SQL  ALTER TABLESPACE split_ts_modern_84 ENCRYPTION='N', ENGINE_ATTRIBUTE='{}' STORAGE ENGINE=InnoDB WAIT;
行为 ALTER Tablespace(1:17~1:35) /test/1/catalog1/schema1/split_ts_modern_84/
------
SQL  ALTER UNDO TABLESPACE split_undo_84 SET INACTIVE ENGINE=InnoDB;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_84/
------
SQL  ALTER UNDO TABLESPACE split_undo_84 SET ACTIVE ENGINE InnoDB;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_84/
------
SQL  ALTER UNDO TABLESPACE split_undo_84 SET INACTIVE;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_84/
------
SQL  ALTER TABLESPACE split_ts_modern_97 ENCRYPTION='N', ENGINE_ATTRIBUTE='{}' STORAGE ENGINE=InnoDB WAIT;
行为 ALTER Tablespace(1:17~1:35) /test/1/catalog1/schema1/split_ts_modern_97/
------
SQL  ALTER UNDO TABLESPACE split_undo_97 SET INACTIVE ENGINE=InnoDB;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_97/
------
SQL  ALTER UNDO TABLESPACE split_undo_97 SET ACTIVE ENGINE InnoDB;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_97/
------
SQL  ALTER UNDO TABLESPACE split_undo_97 SET INACTIVE;
行为 ALTER Tablespace(1:22~1:35) /test/1/catalog1/schema1/split_undo_97/

## DROP_EVENT

SQL  DROP EVENT IF EXISTS split56.ev_once;
行为 DROP Event(1:21~1:36) /test/1/catalog1/split56/ev_once/
------
SQL  DROP EVENT IF EXISTS split56.ev_once_renamed;
行为 DROP Event(1:21~1:44) /test/1/catalog1/split56/ev_once_renamed/
------
SQL  DROP EVENT IF EXISTS split56.ev_repeat;
行为 DROP Event(1:21~1:38) /test/1/catalog1/split56/ev_repeat/
------
SQL  DROP EVENT split56.ev_repeat;
行为 DROP Event(1:11~1:28) /test/1/catalog1/split56/ev_repeat/
------
SQL  DROP EVENT IF EXISTS split57.ev_once;
行为 DROP Event(1:21~1:36) /test/1/catalog1/split57/ev_once/
------
SQL  DROP EVENT IF EXISTS split57.ev_once_renamed;
行为 DROP Event(1:21~1:44) /test/1/catalog1/split57/ev_once_renamed/
------
SQL  DROP EVENT IF EXISTS split57.ev_repeat;
行为 DROP Event(1:21~1:38) /test/1/catalog1/split57/ev_repeat/
------
SQL  DROP EVENT split57.ev_repeat;
行为 DROP Event(1:11~1:28) /test/1/catalog1/split57/ev_repeat/
------
SQL  DROP EVENT IF EXISTS split80.ev_once;
行为 DROP Event(1:21~1:36) /test/1/catalog1/split80/ev_once/
------
SQL  DROP EVENT IF EXISTS split80.ev_once_renamed;
行为 DROP Event(1:21~1:44) /test/1/catalog1/split80/ev_once_renamed/
------
SQL  DROP EVENT IF EXISTS split80.ev_repeat;
行为 DROP Event(1:21~1:38) /test/1/catalog1/split80/ev_repeat/
------
SQL  DROP EVENT split80.ev_repeat;
行为 DROP Event(1:11~1:28) /test/1/catalog1/split80/ev_repeat/
------
SQL  DROP EVENT IF EXISTS split84.ev_once;
行为 DROP Event(1:21~1:36) /test/1/catalog1/split84/ev_once/
------
SQL  DROP EVENT IF EXISTS split84.ev_once_renamed;
行为 DROP Event(1:21~1:44) /test/1/catalog1/split84/ev_once_renamed/
------
SQL  DROP EVENT IF EXISTS split84.ev_repeat;
行为 DROP Event(1:21~1:38) /test/1/catalog1/split84/ev_repeat/
------
SQL  DROP EVENT split84.ev_repeat;
行为 DROP Event(1:11~1:28) /test/1/catalog1/split84/ev_repeat/
------
SQL  DROP EVENT IF EXISTS split97.ev_once;
行为 DROP Event(1:21~1:36) /test/1/catalog1/split97/ev_once/
------
SQL  DROP EVENT IF EXISTS split97.ev_once_renamed;
行为 DROP Event(1:21~1:44) /test/1/catalog1/split97/ev_once_renamed/
------
SQL  DROP EVENT IF EXISTS split97.ev_repeat;
行为 DROP Event(1:21~1:38) /test/1/catalog1/split97/ev_repeat/
------
SQL  DROP EVENT split97.ev_repeat;
行为 DROP Event(1:11~1:28) /test/1/catalog1/split97/ev_repeat/

## CREATE_EVENT

SQL  CREATE DEFINER = CURRENT_USER EVENT IF NOT EXISTS split56.ev_once\n  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR + INTERVAL 1 MINUTE\n  ON COMPLETION PRESERVE\n  DISABLE\n  COMMENT 'one time event'\n  DO UPDATE split56.event_log SET note = 'once' WHERE id = 1;
行为 CREATE Event(1:50~1:65) /test/1/catalog1/split56/ev_once/
行为 UPDATE Table(6:12~6:29) /test/1/catalog1/split56/event_log/
------
SQL  CREATE EVENT split56.ev_repeat\n  ON SCHEDULE EVERY 1 DAY\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 2 DAY\n  ON COMPLETION NOT PRESERVE\n  DISABLE ON SLAVE\n  COMMENT 'repeat event'\n  DO INSERT INTO split56.event_log VALUES (2, 'repeat');
行为 CREATE Event(1:13~1:30) /test/1/catalog1/split56/ev_repeat/
行为 INSERT Table(8:17~8:34) /test/1/catalog1/split56/event_log/
------
SQL  CREATE EVENT split_event.ev_unit_0 ON SCHEDULE EVERY 1 YEAR DO SET @event_unit_0=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_0/
行为 READ ConfigKey(1:67~1:80) /test/1/event_unit_0/
------
SQL  CREATE EVENT split_event.ev_unit_1 ON SCHEDULE EVERY 1 QUARTER DO SET @event_unit_1=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_1/
行为 READ ConfigKey(1:70~1:83) /test/1/event_unit_1/
------
SQL  CREATE EVENT split_event.ev_unit_2 ON SCHEDULE EVERY 1 MONTH DO SET @event_unit_2=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_2/
行为 READ ConfigKey(1:68~1:81) /test/1/event_unit_2/
------
SQL  CREATE EVENT split_event.ev_unit_3 ON SCHEDULE EVERY 1 WEEK DO SET @event_unit_3=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_3/
行为 READ ConfigKey(1:67~1:80) /test/1/event_unit_3/
------
SQL  CREATE EVENT split_event.ev_unit_4 ON SCHEDULE EVERY 1 MINUTE DO SET @event_unit_4=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_4/
行为 READ ConfigKey(1:69~1:82) /test/1/event_unit_4/
------
SQL  CREATE EVENT split_event.ev_unit_5 ON SCHEDULE EVERY 1 SECOND DO SET @event_unit_5=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_5/
行为 READ ConfigKey(1:69~1:82) /test/1/event_unit_5/
------
SQL  CREATE EVENT split_event.ev_unit_6 ON SCHEDULE EVERY '1-2' YEAR_MONTH DO SET @event_unit_6=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_6/
行为 READ ConfigKey(1:77~1:90) /test/1/event_unit_6/
------
SQL  CREATE EVENT split_event.ev_unit_7 ON SCHEDULE EVERY '1 2' DAY_HOUR DO SET @event_unit_7=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_7/
行为 READ ConfigKey(1:75~1:88) /test/1/event_unit_7/
------
SQL  CREATE EVENT split_event.ev_unit_8 ON SCHEDULE EVERY '1 2:3' DAY_MINUTE DO SET @event_unit_8=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_8/
行为 READ ConfigKey(1:79~1:92) /test/1/event_unit_8/
------
SQL  CREATE EVENT split_event.ev_unit_9 ON SCHEDULE EVERY '1 2:3:4' DAY_SECOND DO SET @event_unit_9=1;
行为 CREATE Event(1:13~1:34) /test/1/catalog1/split_event/ev_unit_9/
行为 READ ConfigKey(1:81~1:94) /test/1/event_unit_9/
------
SQL  CREATE EVENT split_event.ev_unit_10 ON SCHEDULE EVERY '2:3' HOUR_MINUTE DO SET @event_unit_10=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_10/
行为 READ ConfigKey(1:79~1:93) /test/1/event_unit_10/
------
SQL  CREATE EVENT split_event.ev_unit_11 ON SCHEDULE EVERY '2:3:4' HOUR_SECOND DO SET @event_unit_11=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_11/
行为 READ ConfigKey(1:81~1:95) /test/1/event_unit_11/
------
SQL  CREATE EVENT split_event.ev_unit_12 ON SCHEDULE EVERY '3:4' MINUTE_SECOND DO SET @event_unit_12=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_12/
行为 READ ConfigKey(1:81~1:95) /test/1/event_unit_12/
------
SQL  CREATE EVENT split_event.ev_unit_13 ON SCHEDULE EVERY 1 MICROSECOND DO SET @event_unit_13=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_13/
行为 READ ConfigKey(1:75~1:89) /test/1/event_unit_13/
------
SQL  CREATE EVENT split_event.ev_unit_14 ON SCHEDULE EVERY '4.000005' SECOND_MICROSECOND DO SET @event_unit_14=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_14/
行为 READ ConfigKey(1:91~1:105) /test/1/event_unit_14/
------
SQL  CREATE EVENT split_event.ev_unit_15 ON SCHEDULE EVERY '3:4.000005' MINUTE_MICROSECOND DO SET @event_unit_15=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_15/
行为 READ ConfigKey(1:93~1:107) /test/1/event_unit_15/
------
SQL  CREATE EVENT split_event.ev_unit_16 ON SCHEDULE EVERY '2:3:4.000005' HOUR_MICROSECOND DO SET @event_unit_16=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_16/
行为 READ ConfigKey(1:93~1:107) /test/1/event_unit_16/
------
SQL  CREATE EVENT split_event.ev_unit_17 ON SCHEDULE EVERY '1 2:3:4.000005' DAY_MICROSECOND DO SET @event_unit_17=1;
行为 CREATE Event(1:13~1:35) /test/1/catalog1/split_event/ev_unit_17/
行为 READ ConfigKey(1:94~1:108) /test/1/event_unit_17/
------
SQL  CREATE EVENT e09 ON SCHEDULE EVERY @secs MINUTE STARTS SYSDATE() ON COMPLETION PRESERVE DO BEGIN DECLARE EXIT HANDLER FOR SQLEXCEPTION SELECT 'handled'; SET @seen=1; END;
行为 CREATE Event(1:13~1:16) /test/1/catalog1/schema1/e09/
行为 READ ConfigKey(1:35~1:40) /test/1/secs/
行为 CALL Function(1:55~1:62) /test/1/catalog1/schema1/SYSDATE/
行为 READ ConfigKey(1:157~1:162) /test/1/seen/
------
SQL  CREATE EVENT gap_event_empty ON SCHEDULE AT CURRENT_TIMESTAMP DO BEGIN END;
行为 CREATE Event(1:13~1:28) /test/1/catalog1/schema1/gap_event_empty/
------
SQL  CREATE EVENT gap_event_ddl ON SCHEDULE AT CURRENT_TIMESTAMP DO DROP TABLE gap_drop;
行为 CREATE Event(1:13~1:26) /test/1/catalog1/schema1/gap_event_ddl/
行为 DROP Table(1:74~1:82) /test/1/catalog1/schema1/gap_drop/
------
SQL  CREATE EVENT ev_enable_gap ON SCHEDULE EVERY '10:20' MINUTE_SECOND ON COMPLETION PRESERVE ENABLE COMMENT 'enabled' DO SELECT 1;
行为 CREATE Event(1:13~1:26) /test/1/catalog1/schema1/ev_enable_gap/
------
SQL  CREATE DEFINER=root@localhost EVENT ev_def_gap ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR DO SELECT CURRENT_USER();
行为 CREATE Event(1:36~1:46) /test/1/catalog1/schema1/ev_def_gap/
行为 CALL Function(1:108~1:120) /test/1/catalog1/schema1/CURRENT_USER/
------
SQL  CREATE EVENT split_event_at_subquery ON SCHEDULE AT (SELECT s1 FROM split_event_source) DO DROP TABLE split_event_target;
行为 CREATE Event(1:13~1:36) /test/1/catalog1/schema1/split_event_at_subquery/
行为 READ Table(1:68~1:86) /test/1/catalog1/schema1/split_event_source/
行为 DROP Table(1:102~1:120) /test/1/catalog1/schema1/split_event_target/
------
SQL  CREATE EVENT split_event_every_subquery ON SCHEDULE EVERY (SELECT s1 FROM split_event_source) SECOND DO DROP TABLE split_event_target;
行为 CREATE Event(1:13~1:39) /test/1/catalog1/schema1/split_event_every_subquery/
行为 READ Table(1:74~1:92) /test/1/catalog1/schema1/split_event_source/
行为 DROP Table(1:115~1:133) /test/1/catalog1/schema1/split_event_target/
------
SQL  CREATE EVENT split_event_starts_subquery ON SCHEDULE EVERY 5 SECOND STARTS (SELECT s1 FROM split_event_source) DO DROP TABLE split_event_target;
行为 CREATE Event(1:13~1:40) /test/1/catalog1/schema1/split_event_starts_subquery/
行为 READ Table(1:91~1:109) /test/1/catalog1/schema1/split_event_source/
行为 DROP Table(1:125~1:143) /test/1/catalog1/schema1/split_event_target/
------
SQL  CREATE EVENT split_event_ends_subquery ON SCHEDULE EVERY 5 SECOND ENDS (SELECT s1 FROM split_event_source) DO DROP TABLE split_event_target;
行为 CREATE Event(1:13~1:38) /test/1/catalog1/schema1/split_event_ends_subquery/
行为 READ Table(1:87~1:105) /test/1/catalog1/schema1/split_event_source/
行为 DROP Table(1:121~1:139) /test/1/catalog1/schema1/split_event_target/
------
SQL  CREATE EVENT split_event_every_union ON SCHEDULE EVERY (SELECT 'abcdef' UNION SELECT 'abcdef') SECOND DO SELECT 1;
行为 CREATE Event(1:13~1:36) /test/1/catalog1/schema1/split_event_every_union/
------
SQL  CREATE EVENT split_event_every_union_arithmetic ON SCHEDULE EVERY (SELECT 'abcdef' UNION SELECT 'abcdef')+10 SECOND DO SELECT 1;
行为 CREATE Event(1:13~1:47) /test/1/catalog1/schema1/split_event_every_union_arithmetic/
------
SQL  CREATE DEFINER = CURRENT_USER EVENT IF NOT EXISTS split57.ev_once\n  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR + INTERVAL 1 MINUTE\n  ON COMPLETION PRESERVE\n  DISABLE\n  COMMENT 'one time event'\n  DO UPDATE split57.event_log SET note = 'once' WHERE id = 1;
行为 CREATE Event(1:50~1:65) /test/1/catalog1/split57/ev_once/
行为 UPDATE Table(6:12~6:29) /test/1/catalog1/split57/event_log/
------
SQL  CREATE EVENT split57.ev_repeat\n  ON SCHEDULE EVERY 1 DAY\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 2 DAY\n  ON COMPLETION NOT PRESERVE\n  DISABLE ON SLAVE\n  COMMENT 'repeat event'\n  DO INSERT INTO split57.event_log VALUES (2, 'repeat');
行为 CREATE Event(1:13~1:30) /test/1/catalog1/split57/ev_repeat/
行为 INSERT Table(8:17~8:34) /test/1/catalog1/split57/event_log/
------
SQL  CREATE DEFINER = CURRENT_USER EVENT IF NOT EXISTS split80.ev_once\n  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR + INTERVAL 1 MINUTE\n  ON COMPLETION PRESERVE\n  DISABLE\n  COMMENT 'one time event'\n  DO UPDATE split80.event_log SET note = 'once' WHERE id = 1;
行为 CREATE Event(1:50~1:65) /test/1/catalog1/split80/ev_once/
行为 UPDATE Table(6:12~6:29) /test/1/catalog1/split80/event_log/
------
SQL  CREATE EVENT split80.ev_repeat\n  ON SCHEDULE EVERY 1 DAY\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 2 DAY\n  ON COMPLETION NOT PRESERVE\n  DISABLE ON SLAVE\n  COMMENT 'repeat event'\n  DO INSERT INTO split80.event_log VALUES (2, 'repeat');
行为 CREATE Event(1:13~1:30) /test/1/catalog1/split80/ev_repeat/
行为 INSERT Table(8:17~8:34) /test/1/catalog1/split80/event_log/
------
SQL  CREATE DEFINER = CURRENT_USER EVENT IF NOT EXISTS split84.ev_once\n  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n  ON COMPLETION PRESERVE\n  DISABLE\n  COMMENT 'one time event'\n  DO UPDATE split84.event_log SET note = 'once' WHERE id = 1;
行为 CREATE Event(1:50~1:65) /test/1/catalog1/split84/ev_once/
行为 UPDATE Table(6:12~6:29) /test/1/catalog1/split84/event_log/
------
SQL  CREATE EVENT split84.ev_repeat\n  ON SCHEDULE EVERY 1 DAY\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 2 DAY\n  ON COMPLETION NOT PRESERVE\n  DISABLE ON REPLICA\n  COMMENT 'repeat event'\n  DO INSERT INTO split84.event_log VALUES (2, 'repeat');
行为 CREATE Event(1:13~1:30) /test/1/catalog1/split84/ev_repeat/
行为 INSERT Table(8:17~8:34) /test/1/catalog1/split84/event_log/
------
SQL  CREATE DEFINER = CURRENT_USER EVENT IF NOT EXISTS split97.ev_once\n  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR + INTERVAL 1 MINUTE\n  ON COMPLETION PRESERVE\n  DISABLE\n  COMMENT 'one time event'\n  DO UPDATE split97.event_log SET note = 'once' WHERE id = 1;
行为 CREATE Event(1:50~1:65) /test/1/catalog1/split97/ev_once/
行为 UPDATE Table(6:12~6:29) /test/1/catalog1/split97/event_log/
------
SQL  CREATE EVENT split97.ev_repeat\n  ON SCHEDULE EVERY 1 DAY\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 2 DAY\n  ON COMPLETION NOT PRESERVE\n  DISABLE ON SLAVE\n  COMMENT 'repeat event'\n  DO INSERT INTO split97.event_log VALUES (2, 'repeat');
行为 CREATE Event(1:13~1:30) /test/1/catalog1/split97/ev_repeat/
行为 INSERT Table(8:17~8:34) /test/1/catalog1/split97/event_log/
------
SQL  CREATE EVENT split97.ev_replica ON SCHEDULE EVERY 1 DAY DISABLE ON REPLICA DO SET @event_seen = 1;
行为 CREATE Event(1:13~1:31) /test/1/catalog1/split97/ev_replica/
行为 READ ConfigKey(1:82~1:93) /test/1/event_seen/

## ALTER_EVENT

SQL  ALTER EVENT split56.ev_once\n  ON SCHEDULE EVERY 2 HOUR\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 1 DAY\n  RENAME TO split56.ev_once_renamed\n  ENABLE\n  COMMENT 'renamed event'\n  DO UPDATE split56.event_log SET note = 'renamed' WHERE id = 1;
行为 ALTER Event(1:12~1:27) /test/1/catalog1/split56/ev_once/
行为 ALTER Event(5:12~5:35) /test/1/catalog1/split56/ev_once_renamed/
行为 UPDATE Table(8:12~8:29) /test/1/catalog1/split56/event_log/
------
SQL  ALTER EVENT split56.ev_repeat DISABLE ON SLAVE;
行为 ALTER Event(1:12~1:29) /test/1/catalog1/split56/ev_repeat/
------
SQL  ALTER EVENT split_event.ev_compound\nON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 DAY + INTERVAL 2 HOUR\nDO BEGIN\n  UPDATE split_event.event_log SET note = 'altered' WHERE id = 1;\n  DELETE FROM split_event.event_log WHERE id > 10;\nEND;
行为 ALTER Event(1:12~1:35) /test/1/catalog1/split_event/ev_compound/
行为 UPDATE Table(4:9~4:30) /test/1/catalog1/split_event/event_log/
行为 DELETE Table(5:14~5:35) /test/1/catalog1/split_event/event_log/
------
SQL  ALTER DEFINER=CURRENT_USER() EVENT e1 ENABLE;
行为 ALTER Event(1:35~1:37) /test/1/catalog1/schema1/e1/
------
SQL  ALTER EVENT gap_event_outer DO ALTER EVENT gap_event_inner ENABLE;
行为 ALTER Event(1:12~1:27) /test/1/catalog1/schema1/gap_event_outer/
行为 ALTER Event(1:43~1:58) /test/1/catalog1/schema1/gap_event_inner/
------
SQL  ALTER EVENT ev_base_gap ON COMPLETION NOT PRESERVE;
行为 ALTER Event(1:12~1:23) /test/1/catalog1/schema1/ev_base_gap/
------
SQL  ALTER EVENT ev_base_gap ON COMPLETION PRESERVE;
行为 ALTER Event(1:12~1:23) /test/1/catalog1/schema1/ev_base_gap/
------
SQL  ALTER EVENT ev_base_gap COMMENT '';
行为 ALTER Event(1:12~1:23) /test/1/catalog1/schema1/ev_base_gap/
------
SQL  ALTER EVENT ev_base_gap RENAME TO audit_db2.ev_moved_gap;
行为 ALTER Event(1:12~1:23) /test/1/catalog1/schema1/ev_base_gap/
行为 ALTER Event(1:34~1:56) /test/1/catalog1/audit_db2/ev_moved_gap/
------
SQL  ALTER DEFINER=root@localhost EVENT ev_base_gap ON SCHEDULE EVERY 1 HOUR;
行为 ALTER Event(1:35~1:46) /test/1/catalog1/schema1/ev_base_gap/
------
SQL  ALTER EVENT ev_def_gap DO SELECT 12;
行为 ALTER Event(1:12~1:22) /test/1/catalog1/schema1/ev_def_gap/
------
SQL  ALTER EVENT split57.ev_once\n  ON SCHEDULE EVERY 2 HOUR\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 1 DAY\n  RENAME TO split57.ev_once_renamed\n  ENABLE\n  COMMENT 'renamed event'\n  DO UPDATE split57.event_log SET note = 'renamed' WHERE id = 1;
行为 ALTER Event(1:12~1:27) /test/1/catalog1/split57/ev_once/
行为 ALTER Event(5:12~5:35) /test/1/catalog1/split57/ev_once_renamed/
行为 UPDATE Table(8:12~8:29) /test/1/catalog1/split57/event_log/
------
SQL  ALTER EVENT split57.ev_repeat DISABLE ON SLAVE;
行为 ALTER Event(1:12~1:29) /test/1/catalog1/split57/ev_repeat/
------
SQL  ALTER EVENT split80.ev_once\n  ON SCHEDULE EVERY 2 HOUR\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 1 DAY\n  RENAME TO split80.ev_once_renamed\n  ENABLE\n  COMMENT 'renamed event'\n  DO UPDATE split80.event_log SET note = 'renamed' WHERE id = 1;
行为 ALTER Event(1:12~1:27) /test/1/catalog1/split80/ev_once/
行为 ALTER Event(5:12~5:35) /test/1/catalog1/split80/ev_once_renamed/
行为 UPDATE Table(8:12~8:29) /test/1/catalog1/split80/event_log/
------
SQL  ALTER EVENT split80.ev_repeat DISABLE ON SLAVE;
行为 ALTER Event(1:12~1:29) /test/1/catalog1/split80/ev_repeat/
------
SQL  ALTER EVENT split84.ev_once\n  ON SCHEDULE EVERY 2 HOUR\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 1 DAY\n  RENAME TO split84.ev_once_renamed\n  ENABLE\n  COMMENT 'renamed event'\n  DO UPDATE split84.event_log SET note = 'renamed' WHERE id = 1;
行为 ALTER Event(1:12~1:27) /test/1/catalog1/split84/ev_once/
行为 ALTER Event(5:12~5:35) /test/1/catalog1/split84/ev_once_renamed/
行为 UPDATE Table(8:12~8:29) /test/1/catalog1/split84/event_log/
------
SQL  ALTER EVENT split84.ev_repeat DISABLE ON SLAVE;
行为 ALTER Event(1:12~1:29) /test/1/catalog1/split84/ev_repeat/
------
SQL  ALTER EVENT split97.ev_once\n  ON SCHEDULE EVERY 2 HOUR\n    STARTS CURRENT_TIMESTAMP + INTERVAL 1 HOUR\n    ENDS CURRENT_TIMESTAMP + INTERVAL 1 DAY\n  RENAME TO split97.ev_once_renamed\n  ENABLE\n  COMMENT 'renamed event'\n  DO UPDATE split97.event_log SET note = 'renamed' WHERE id = 1;
行为 ALTER Event(1:12~1:27) /test/1/catalog1/split97/ev_once/
行为 ALTER Event(5:12~5:35) /test/1/catalog1/split97/ev_once_renamed/
行为 UPDATE Table(8:12~8:29) /test/1/catalog1/split97/event_log/
------
SQL  ALTER EVENT split97.ev_repeat DISABLE ON SLAVE;
行为 ALTER Event(1:12~1:29) /test/1/catalog1/split97/ev_repeat/

## ALTER_PROG_OBJ

SQL  ALTER FUNCTION fn_lifecycle_local COMMENT 'altered function' LANGUAGE SQL READS SQL DATA SQL SECURITY INVOKER;
行为 ALTER Function(1:15~1:33) /test/1/catalog1/schema1/fn_lifecycle_local/
------
SQL  ALTER FUNCTION fn_lifecycle_local NO SQL SQL SECURITY DEFINER COMMENT 'no sql';
行为 ALTER Function(1:15~1:33) /test/1/catalog1/schema1/fn_lifecycle_local/
------
SQL  ALTER PROCEDURE proc_lifecycle_local COMMENT 'altered procedure' LANGUAGE SQL MODIFIES SQL DATA SQL SECURITY DEFINER;
行为 ALTER Procedure(1:16~1:36) /test/1/catalog1/schema1/proc_lifecycle_local/
------
SQL  ALTER PROCEDURE proc_lifecycle_local CONTAINS SQL SQL SECURITY INVOKER COMMENT 'contains sql';
行为 ALTER Procedure(1:16~1:36) /test/1/catalog1/schema1/proc_lifecycle_local/
------
SQL  ALTER FUNCTION missing_external_function LANGUAGE JAVASCRIPT;
行为 ALTER Function(1:15~1:40) /test/1/catalog1/schema1/missing_external_function/
------
SQL  ALTER PROCEDURE missing_external_procedure LANGUAGE JAVASCRIPT;
行为 ALTER Procedure(1:16~1:42) /test/1/catalog1/schema1/missing_external_procedure/
------
SQL  ALTER FUNCTION split84.ai84_func COMMENT 'altered function' LANGUAGE SQL READS SQL DATA SQL SECURITY INVOKER;
行为 ALTER Function(1:15~1:32) /test/1/catalog1/split84/ai84_func/
------
SQL  ALTER FUNCTION split84.ai84_func NO SQL SQL SECURITY DEFINER COMMENT 'no sql';
行为 ALTER Function(1:15~1:32) /test/1/catalog1/split84/ai84_func/
------
SQL  ALTER PROCEDURE split84.ai84_proc COMMENT 'altered procedure' LANGUAGE SQL MODIFIES SQL DATA SQL SECURITY DEFINER;
行为 ALTER Procedure(1:16~1:33) /test/1/catalog1/split84/ai84_proc/
------
SQL  ALTER PROCEDURE split84.ai84_proc CONTAINS SQL SQL SECURITY INVOKER COMMENT 'contains sql';
行为 ALTER Procedure(1:16~1:33) /test/1/catalog1/split84/ai84_proc/
------
SQL  /*!90300 ALTER FUNCTION split_exec_fn COMMENT 'updated executable function' USING (split_exec_comment_lib AS split_exec_lib) */;
行为 ALTER Function(1:24~1:37) /test/1/catalog1/schema1/split_exec_fn/
------
SQL  ALTER FUNCTION split_ext.ext_f USING (split_ext.lib2 AS imported2, split_ext.lib1);
行为 ALTER Function(1:15~1:30) /test/1/catalog1/split_ext/ext_f/
------
SQL  ALTER FUNCTION split_ext.ext_f COMMENT 'updated function' USING (split_ext.lib1 AS imported1);
行为 ALTER Function(1:15~1:30) /test/1/catalog1/split_ext/ext_f/
------
SQL  ALTER PROCEDURE split_ext.ext_p USING ();
行为 ALTER Procedure(1:16~1:31) /test/1/catalog1/split_ext/ext_p/
------
SQL  ALTER PROCEDURE p USING(lib2);
行为 ALTER Procedure(1:16~1:17) /test/1/catalog1/schema1/p/
------
SQL  ALTER FUNCTION audit_f USING ();
行为 ALTER Function(1:15~1:22) /test/1/catalog1/schema1/audit_f/

## DROP_TRIGGER

SQL  DROP TRIGGER IF EXISTS split56.trg_missing;
行为 DROP Trigger(1:23~1:42) /test/1/catalog1/split56/trg_missing/
------
SQL  DROP TRIGGER split56.trg_ad;
行为 DROP Trigger(1:13~1:27) /test/1/catalog1/split56/trg_ad/
------
SQL  DROP TRIGGER IF EXISTS split57.trg_missing;
行为 DROP Trigger(1:23~1:42) /test/1/catalog1/split57/trg_missing/
------
SQL  DROP TRIGGER split57.trg_ad;
行为 DROP Trigger(1:13~1:27) /test/1/catalog1/split57/trg_ad/
------
SQL  DROP TRIGGER IF EXISTS split80.trg_missing;
行为 DROP Trigger(1:23~1:42) /test/1/catalog1/split80/trg_missing/
------
SQL  DROP TRIGGER split80.trg_ad;
行为 DROP Trigger(1:13~1:27) /test/1/catalog1/split80/trg_ad/
------
SQL  DROP TRIGGER IF EXISTS split84.trg_src_bi_audit;
行为 DROP Trigger(1:23~1:47) /test/1/catalog1/split84/trg_src_bi_audit/
------
SQL  DROP TRIGGER IF EXISTS split84.trg_src_bi;
行为 DROP Trigger(1:23~1:41) /test/1/catalog1/split84/trg_src_bi/
------
SQL  DROP TRIGGER IF EXISTS split84.trg_src_au;
行为 DROP Trigger(1:23~1:41) /test/1/catalog1/split84/trg_src_au/
------
SQL  DROP TRIGGER split84.trg_src_au;
行为 DROP Trigger(1:13~1:31) /test/1/catalog1/split84/trg_src_au/
------
SQL  DROP TRIGGER IF EXISTS split97.trg_missing;
行为 DROP Trigger(1:23~1:42) /test/1/catalog1/split97/trg_missing/
------
SQL  DROP TRIGGER split97.trg_ad;
行为 DROP Trigger(1:13~1:27) /test/1/catalog1/split97/trg_ad/

## INSERT

SQL  INSERT INTO t1 VALUES(1);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO aes_feedback_modes (a) VALUES (REPEAT('a', 128)), (REPEAT(0x00313233, 32));
行为 INSERT Table(1:12~1:30) /test/1/catalog1/schema1/aes_feedback_modes/
行为 CALL Function(1:43~1:49) /test/1/catalog1/schema1/REPEAT/
------
SQL  INSERT INTO aes_cipher_keys VALUES (AES_ENCRYPT('a', 'a'));
行为 INSERT Table(1:12~1:27) /test/1/catalog1/schema1/aes_cipher_keys/
行为 CALL Function(1:36~1:47) /test/1/catalog1/schema1/AES_ENCRYPT/
------
SQL  INSERT INTO aes_cipher_keys VALUES (AES_ENCRYPT('b', 'a'));
行为 INSERT Table(1:12~1:27) /test/1/catalog1/schema1/aes_cipher_keys/
行为 CALL Function(1:36~1:47) /test/1/catalog1/schema1/AES_ENCRYPT/
------
SQL  INSERT INTO aes_ecb_modes (a) VALUES ('a'), ('Жоро'), (REPEAT('a', 10));
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/aes_ecb_modes/
行为 CALL Function(1:55~1:61) /test/1/catalog1/schema1/REPEAT/
------
SQL  INSERT INTO aes_misc_cipher VALUES (AES_ENCRYPT(@aes_misc_plain, @aes_misc_key, @aes_misc_iv));
行为 INSERT Table(1:12~1:27) /test/1/catalog1/schema1/aes_misc_cipher/
行为 CALL Function(1:36~1:47) /test/1/catalog1/schema1/AES_ENCRYPT/
行为 READ ConfigKey(1:48~1:63) /test/1/aes_misc_plain/
行为 READ ConfigKey(1:65~1:78) /test/1/aes_misc_key/
行为 READ ConfigKey(1:80~1:92) /test/1/aes_misc_iv/
------
SQL  INSERT INTO compress_columns_t(a,b,c) VALUES(COMPRESS(@test_compress_string),COMPRESS(@test_compress_string),'d ');
行为 INSERT Table(1:12~1:30) /test/1/catalog1/schema1/compress_columns_t/
行为 CALL Function(1:45~1:53) /test/1/catalog1/schema1/COMPRESS/
行为 READ ConfigKey(1:54~1:75) /test/1/test_compress_string/
------
SQL  INSERT compress_blob_t VALUES(COMPRESS(NULL)),('A\0\0\0BBBBBBBB'),(COMPRESS(SPACE(50000))),(SPACE(50000));
行为 INSERT Table(1:7~1:22) /test/1/catalog1/schema1/compress_blob_t/
行为 CALL Function(1:30~1:38) /test/1/catalog1/schema1/COMPRESS/
行为 CALL Function(1:76~1:81) /test/1/catalog1/schema1/SPACE/
------
SQL  INSERT INTO crypt_password VALUES ('tom', PASSWORD('my_pass'));
行为 INSERT Table(1:12~1:26) /test/1/catalog1/schema1/crypt_password/
行为 CALL Function(1:42~1:50) /test/1/catalog1/schema1/PASSWORD/
------
SQL  INSERT INTO codex_func_date_add.invalid_t(d) SELECT DATE_SUB('2000-01-01',INTERVAL 2001 YEAR);
行为 INSERT Table(1:12~1:41) /test/1/catalog1/codex_func_date_add/invalid_t/
行为 CALL Function(1:52~1:60) /test/1/catalog1/schema1/DATE_SUB/
------
SQL  INSERT INTO codex_func_date_add.invalid_t(d) SELECT DATE_ADD('2000-01-01',INTERVAL 8000 YEAR);
行为 INSERT Table(1:12~1:41) /test/1/catalog1/codex_func_date_add/invalid_t/
行为 CALL Function(1:52~1:60) /test/1/catalog1/schema1/DATE_ADD/
------
SQL  INSERT INTO codex_func_date_add.invalid_t VALUES(DATE_ADD(NULL,INTERVAL 1 DAY));
行为 INSERT Table(1:12~1:41) /test/1/catalog1/codex_func_date_add/invalid_t/
行为 CALL Function(1:49~1:57) /test/1/catalog1/schema1/DATE_ADD/
------
SQL  INSERT INTO codex_func_date_add.invalid_t VALUES(DATE_ADD('2000-01-04',INTERVAL NULL DAY));
行为 INSERT Table(1:12~1:41) /test/1/catalog1/codex_func_date_add/invalid_t/
行为 CALL Function(1:49~1:57) /test/1/catalog1/schema1/DATE_ADD/
------
SQL  INSERT INTO t1 VALUES(1,NULL);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO t2 VALUES(1,NULL);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t2/
------
SQL  INSERT INTO func_test_type_mix VALUES('2017-02-02 12:00:00',@a,@a,@a,@a,@a);
行为 INSERT Table(1:12~1:30) /test/1/catalog1/schema1/func_test_type_mix/
行为 READ ConfigKey(1:60~1:62) /test/1/a/
------
SQL  INSERT INTO datetime_t SET t_date=NULLIF(NOW(),'');
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/datetime_t/
行为 CALL Function(1:34~1:40) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:41~1:44) /test/1/catalog1/schema1/NOW/
------
SQL  INSERT INTO func_least_faq VALUES (82,82,1),(82,83,3);
行为 INSERT Table(1:12~1:26) /test/1/catalog1/schema1/func_least_faq/
------
SQL  INSERT INTO func_least_access_rank VALUES (1,2),(2,3),(3,1);
行为 INSERT Table(1:12~1:34) /test/1/catalog1/schema1/func_least_access_rank/
------
SQL  INSERT INTO rb_aux SET f1=1,f2=rb_fail();
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/rb_aux/
行为 CALL Function(1:31~1:38) /test/1/catalog1/schema1/rb_fail/
------
SQL  INSERT INTO rb_aux SELECT 1,rb_fail();
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/rb_aux/
行为 CALL Function(1:28~1:35) /test/1/catalog1/schema1/rb_fail/
------
SQL  INSERT INTO rb_aux VALUES(1,rb_fail());
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/rb_aux/
行为 CALL Function(1:28~1:35) /test/1/catalog1/schema1/rb_fail/
------
SQL  INSERT INTO int_values VALUES ('21474836461','21474836461');
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/int_values/
------
SQL  INSERT INTO int_values VALUES ('-21474836461','-21474836461');
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/int_values/
------
SQL  INSERT INTO func_test_nullable_self VALUES(NULL,2);
行为 INSERT Table(1:12~1:35) /test/1/catalog1/schema1/func_test_nullable_self/
------
SQL  INSERT INTO func_test_not_between VALUES(1,2),(2,3),(3,4),(4,5);
行为 INSERT Table(1:12~1:33) /test/1/catalog1/schema1/func_test_not_between/
------
SQL  INSERT INTO func_test_null_unsigned VALUES(0);
行为 INSERT Table(1:12~1:35) /test/1/catalog1/schema1/func_test_null_unsigned/
------
SQL  INSERT INTO func_test_unsigned_pair VALUES(202,1);
行为 INSERT Table(1:12~1:35) /test/1/catalog1/schema1/func_test_unsigned_pair/
------
SQL  INSERT INTO timestamp_source (f2,f3) VALUES (NOW(),FROM_UNIXTIME('9999999999'));
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/timestamp_source/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:51~1:64) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  INSERT INTO timestamp_source (f2,f3) VALUES (NOW(),ASCII(NULL));
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/timestamp_source/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:51~1:56) /test/1/catalog1/schema1/ASCII/
------
SQL  INSERT INTO timestamp_source (f2,f3) VALUES (NOW(),COALESCE(FROM_UNIXTIME('9999999999'),'0000-00-00 00:00:00'));
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/timestamp_source/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:51~1:59) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(1:60~1:73) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  INSERT INTO timestamp_source (f2,f3) VALUES (NOW(),FROM_UNIXTIME(99999999990));
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/timestamp_source/
行为 CALL Function(1:45~1:48) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:51~1:64) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  INSERT INTO fd_dml_values VALUES (NULL,NULL,1),(DEFAULT,DEFAULT,2);
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/fd_dml_values/
------
SQL  INSERT INTO fd_dml_values VALUES ();
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/fd_dml_values/
------
SQL  INSERT INTO split_management_function_probe(id,status_code) VALUES (1,set_firewall_mode('fwuser@localhost','RECORDING'));
行为 INSERT Table(1:12~1:43) /test/1/catalog1/schema1/split_management_function_probe/
行为 CONFIGURE Function(1:70~1:87) /test/1/catalog1/schema1/set_firewall_mode/
------
SQL  INSERT INTO split_replication_wait_probe(wait_result) VALUES (MASTER_POS_WAIT('missing-binlog.000001',4,0));
行为 INSERT Table(1:12~1:40) /test/1/catalog1/schema1/split_replication_wait_probe/
行为 CALL Function(1:62~1:77) /test/1/catalog1/schema1/MASTER_POS_WAIT/
------
SQL  insert into `test_sch`.`table2` (`id`, `b`) values (2, 1);
行为 INSERT Table(1:12~1:31) /test/1/catalog1/test_sch/table2/
------
SQL  insert `test_schema`.`table2` (`id`, `b`) values (2, 1);
行为 INSERT Table(1:7~1:29) /test/1/catalog1/test_schema/table2/
------
SQL  insert into `test_schema`.`table2` (`id`, `b`) value (null, 1);
行为 INSERT Table(1:12~1:34) /test/1/catalog1/test_schema/table2/
------
SQL  insert ignore into `test_schema`.`table2` (`id`, `b`) values (null, 1);
行为 INSERT Table(1:19~1:41) /test/1/catalog1/test_schema/table2/
------
SQL  INSERT DELAYED INTO myisam_t SET id = 1, name = 'delayed', val = DEFAULT;
行为 INSERT Table(1:20~1:28) /test/1/catalog1/schema1/myisam_t/
------
SQL  INSERT DELAYED myisam_t VALUES (11, 'delayed-no-into', DEFAULT);
行为 INSERT Table(1:15~1:23) /test/1/catalog1/schema1/myisam_t/
------
SQL  INSERT INTO target () VALUES();
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/target/
------
SQL  INSERT INTO t2 VALUES(CONVERT_TZ('2004-01-01 00:00:00','MET',@@time_zone),NULL);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t2/
行为 CALL Function(1:22~1:32) /test/1/catalog1/schema1/CONVERT_TZ/
行为 READ ConfigKey(1:61~1:72) /test/1/time_zone/
------
SQL  INSERT INTO dml_audit.t () VALUES (), ();
行为 INSERT Table(1:12~1:23) /test/1/catalog1/dml_audit/t/
------
SQL  INSERT INTO codex_insert_native_parallel.qualified_target SET codex_insert_native_parallel.qualified_target.c = '1';
行为 INSERT Table(1:12~1:57) /test/1/catalog1/codex_insert_native_parallel/qualified_target/
------
SQL  INSERT INTO t VALUES (1, VALUES(x));
行为 INSERT Table(1:12~1:13) /test/1/catalog1/schema1/t/
行为 CALL Function(1:25~1:31) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT INTO t1 (Ñ, N) VALUES (1, 2);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT DELAYED IGNORE INTO myisam_t VALUES(2,2);
行为 INSERT Table(1:27~1:35) /test/1/catalog1/schema1/myisam_t/
------
SQL  INSERT INTO part_t PARTITION(p0,p1) VALUES(2,3),(12,4);
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/part_t/
------
SQL  INSERT IGNORE INTO base_t SET id=5,c=DEFAULT,d='e';
行为 INSERT Table(1:19~1:25) /test/1/catalog1/schema1/base_t/
------
SQL  INSERT INTO f24 VALUES (1e+10);
行为 INSERT Table(1:12~1:15) /test/1/catalog1/schema1/f24/
------
SQL  INSERT INTO f24 VALUES (-1e-10);
行为 INSERT Table(1:12~1:15) /test/1/catalog1/schema1/f24/
------
SQL  INSERT INTO f52 VALUES (1e+10);
行为 INSERT Table(1:12~1:15) /test/1/catalog1/schema1/f52/
------
SQL  INSERT INTO f52 VALUES (-1e-10);
行为 INSERT Table(1:12~1:15) /test/1/catalog1/schema1/f52/
------
SQL  INSERT INTO dupper VALUES (1.7976931348623157E+308);
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/dupper/
------
SQL  INSERT INTO dupper VALUES (1.7976931348623157E+309);
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/dupper/
------
SQL  INSERT INTO dupper VALUES (-1.7976931348623157E+309);
行为 INSERT Table(1:12~1:18) /test/1/catalog1/schema1/dupper/
------
SQL  INSERT INTO t1 VALUES ('2001-01-01 23:59:59.4');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO t1 VALUES (TIMESTAMP'2001-01-01 23:59:59.4');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO t1 VALUES (20010101235959.4);
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO t1 VALUES (TIME'23:59:59.4');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO t1 VALUES (TIMESTAMP'9999-12-31 23:59:59.9');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO bit_t SET a=1,b=b'111111111',c=b'01';
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/bit_t/
------
SQL  INSERT IGNORE INTO bit_t(a,b,c) VALUES(2,b'1000000000',b'100');
行为 INSERT Table(1:19~1:24) /test/1/catalog1/schema1/bit_t/
------
SQL  INSERT INTO bit_t(a,b,c) VALUES(3,b'',b'');
行为 INSERT Table(1:12~1:17) /test/1/catalog1/schema1/bit_t/
------
SQL  INSERT INTO bit_values(id,b) VALUES(1,b''),(2,b'0'),(3,0x01),(4,7),(5,NULL),(6,DEFAULT);
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/bit_values/
------
SQL  INSERT INTO time_widths(id,t,t0,t1,t2,t3,t4,t5,t6) VALUES(1,'1:23','1112','10:10:10.9','10:10:10.99','10:10:10.999','10:10:10.9999','10:10:10.99999','10:10:10.999999'),(2,'-10 01:22:33.45',-10,-10.0,-10e0,'-24:00:00','-48:00:00','-838:59:59.000000','838:59:59.000000');
行为 INSERT Table(1:12~1:23) /test/1/catalog1/schema1/time_widths/
------
SQL  INSERT IGNORE INTO time_widths(id,t6) VALUES(3,'839:00:00'),(4,'-839:00:00.1'),(5,'10:10:10.9999995'),(6,101010.9999995);
行为 INSERT Table(1:19~1:30) /test/1/catalog1/schema1/time_widths/
------
SQL  INSERT INTO time_widths(id,t6) SELECT 7,CAST('2001-01-01 10:10:10.999999' AS DATETIME(6));
行为 INSERT Table(1:12~1:23) /test/1/catalog1/schema1/time_widths/
行为 CALL Function(1:40~1:44) /test/1/catalog1/schema1/CAST/
------
SQL  INSERT INTO time_widths SET id=9,t6=TIME'00:00:01.0000005'+0.000001;
行为 INSERT Table(1:12~1:23) /test/1/catalog1/schema1/time_widths/
------
SQL  INSERT INTO temporal_auto(id,created_at,modified_at,payload) VALUES(2,DEFAULT,DEFAULT,'defaults');
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/temporal_auto/
------
SQL  INSERT INTO temporal_auto SET id=4,created_at=DEFAULT,modified_at=CURRENT_TIMESTAMP(3),explicit_at=TIMESTAMP'2001-02-03 04:05:06.123456',payload='set-form';
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/temporal_auto/
行为 CALL Function(1:66~1:83) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
------
SQL  INSERT INTO datetime_widths(id,dt1,dt2,dt3,dt4,dt5,dt6,ts1,ts2,ts3,ts4,ts5,ts6) VALUES(1,'2001-01-01 01:01:01.1','2001-01-01 01:01:01.12','2001-01-01 01:01:01.123','2001-01-01 01:01:01.1234','2001-01-01 01:01:01.12345','2001-01-01 01:01:01.123456','2001-01-01 01:01:01.1','2001-01-01 01:01:01.12','2001-01-01 01:01:01.123','2001-01-01 01:01:01.1234','2001-01-01 01:01:01.12345','2001-01-01 01:01:01.123456');
行为 INSERT Table(1:12~1:27) /test/1/catalog1/schema1/datetime_widths/
------
SQL  INSERT INTO str_lifecycle(v,c,fixed_binary,variable_binary,t)\nVALUES ('a ', 'a ', X'4100', _binary'a\0', 'text'), ('b', 'b', 0x4200, X'42', DEFAULT);
行为 INSERT Table(1:12~1:25) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  INSERT IGNORE INTO str_lifecycle SET v='set form', c='c', fixed_binary=X'43', variable_binary=_binary'bytes', t='t';
行为 INSERT Table(1:19~1:32) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  INSERT INTO str_parents VALUES (X'93222796CABA43CA979F5C96EB6898B7');
行为 INSERT Table(1:12~1:23) /test/1/catalog1/schema1/str_parents/
------
SQL  INSERT INTO lob_family(tb,b,mb,lb,tt,t,mt,lt)\nVALUES (X'00',X'4100',_binary'bytes',REPEAT(X'42',4),'tiny','text','medium','long');
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/lob_family/
行为 CALL Function(2:37~2:43) /test/1/catalog1/schema1/REPEAT/
------
SQL  INSERT IGNORE INTO lob_family\nSET tb=X'01',b=0x4142,mb=_binary'medium',lb=X'FF',tt='a ',t='b ',mt='c ',lt='d ';
行为 INSERT Table(1:19~1:29) /test/1/catalog1/schema1/lob_family/
------
SQL  INSERT INTO split_type_enum_set.es_core\n      (id,e_basic,e_case,s_basic,s_case,e_added,s_flags)\n    VALUES\n      (1,'alpha','a','red,blue','a','new','x,z'),\n      (2,'10','A','green','A','old','y,w'),\n      (3,2,1,5,3,3,9),\n      (4,'quote''d','a','quote''d,two words','a','archived','z');
行为 INSERT Table(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  INSERT INTO split_type_enum_set.es_core\n    SET id=5,e_basic='',e_case='A',s_basic='blue,red',\n        s_case='a,A',e_added='new',s_flags='w,x';
行为 INSERT Table(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  INSERT INTO spatial_lifecycle\n  (id,g,p,ls,pg,mp,mls,mpg,gc,p_changed)\nVALUES\n  (1,\n   ST_GeomFromText('POINT(1 1)'),\n   ST_PointFromText('POINT(1 1)'),\n   ST_LineStringFromText('LINESTRING(0 0,1 1)'),\n   ST_PolygonFromText('POLYGON((0 0,0 2,2 2,0 0))'),\n   ST_MultiPointFromText('MULTIPOINT(0 0,1 1)'),\n   ST_MultiLineStringFromText('MULTILINESTRING((0 0,1 1))'),\n   ST_MultiPolygonFromText('MULTIPOLYGON(((0 0,0 2,2 2,0 0)))'),\n   ST_GeometryCollectionFromText('GEOMETRYCOLLECTION(POINT(1 1))'),\n   ST_LineStringFromText('LINESTRING(1 1,2 2)'));
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(5:3~5:18) /test/1/catalog1/schema1/ST_GeomFromText/
行为 CALL Function(6:3~6:19) /test/1/catalog1/schema1/ST_PointFromText/
行为 CALL Function(7:3~7:24) /test/1/catalog1/schema1/ST_LineStringFromText/
行为 CALL Function(8:3~8:21) /test/1/catalog1/schema1/ST_PolygonFromText/
行为 CALL Function(9:3~9:24) /test/1/catalog1/schema1/ST_MultiPointFromText/
行为 CALL Function(10:3~10:29) /test/1/catalog1/schema1/ST_MultiLineStringFromText/
行为 CALL Function(11:3~11:26) /test/1/catalog1/schema1/ST_MultiPolygonFromText/
行为 CALL Function(12:3~12:32) /test/1/catalog1/schema1/ST_GeometryCollectionFromText/
------
SQL  INSERT INTO spatial_lifecycle SET\n  id=2,\n  g=ST_GeomFromText('POINT(2 2)'),\n  p=POINT(2,2),\n  ls=ST_GeomFromText('LINESTRING(2 2,3 3)'),\n  pg=ST_GeomFromText('POLYGON((0 0,0 3,3 3,0 0))'),\n  mp=ST_GeomFromText('MULTIPOINT(2 2,3 3)'),\n  mls=ST_GeomFromText('MULTILINESTRING((2 2,3 3))'),\n  mpg=ST_GeomFromText('MULTIPOLYGON(((0 0,0 3,3 3,0 0)))'),\n  gc=ST_GeomFromText('GEOMETRYCOLLECTION(POINT(2 2))'),\n  p_changed=ST_GeomFromText('LINESTRING(2 2,4 4)');
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(3:4~3:19) /test/1/catalog1/schema1/ST_GeomFromText/
行为 CALL Function(4:4~4:9) /test/1/catalog1/schema1/POINT/
------
SQL  INSERT INTO integer_lifecycle\n  (tiny_signed,tiny_unsigned,small_signed,small_unsigned,medium_signed,medium_unsigned,\n   int_signed,int_unsigned,big_signed,big_unsigned,bool_alias,boolean_alias,\n   tiny_added,small_changed,medium_added,big_changed,note)\nVALUES\n  (-1,1,-2,2,-3,3,-4,101,-5,5,FALSE,TRUE,6,7,8,9,'values-one'),\n  (1,10,2,20,3,30,4,102,5,50,TRUE,FALSE,60,70,80,90,'values-two');
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  INSERT INTO integer_lifecycle SET\n  tiny_signed=-8,tiny_unsigned=8,\n  small_signed=-16,small_unsigned=16,\n  medium_signed=-24,medium_unsigned=24,\n  int_signed=-32,int_unsigned=103,\n  big_signed=-64,big_unsigned=64,\n  bool_alias=1,boolean_alias=0,\n  tiny_added=8,small_changed=16,medium_added=24,big_changed=64,note='set';
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  INSERT INTO integer_range_partition(id,partition_value)\nVALUES (1,-8388608),(2,-1),(3,0),(4,8388607);
行为 INSERT Table(1:12~1:35) /test/1/catalog1/schema1/integer_range_partition/
------
SQL  INSERT INTO integer_key_partition(id)\nVALUES (0),(9223372036854775807),(18446744073709551615);
行为 INSERT Table(1:12~1:33) /test/1/catalog1/schema1/integer_key_partition/
------
SQL  INSERT INTO numeric_lifecycle\n  (id,decimal_value,numeric_value,fixed_value,float_value,float_scale,\n   double_value,real_value,decimal_added,numeric_changed,fixed_added,\n   float_changed,double_added,real_changed,note)\nVALUES\n  (1,1.250000,2.5000,3.75,1e-20,4.125,1e100,5.5,\n   6.125,7.25,8.5,9.75,10.125,11.5,'values-one'),\n  (2,12.500000,13.7500,14.25,1e20,15.375,1e-100,16.5,\n   17.625,18.75,19.5,20.75,21.125,22.5,'values-two');
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  INSERT INTO numeric_lifecycle SET\n  id=3,\n  decimal_value=23.125,\n  numeric_value=24.25,\n  fixed_value=25.5,\n  float_value=2.5e-10,\n  float_scale=26.75,\n  double_value=2.75e50,\n  real_value=27.875,\n  decimal_added=28.125,\n  numeric_changed=29.25,\n  fixed_added=30.5,\n  float_changed=31.75,\n  double_added=32.125,\n  real_changed=33.5,\n  note='set';
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  INSERT /*+ SET_VAR(auto_increment_increment=10) */ INTO t1 VALUES(NULL),(NULL),(NULL),(NULL);
行为 INSERT Table(1:56~1:58) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:19~1:43) /test/1/auto_increment_increment/
------
SQL  /*!50000 INSERT INTO t VALUES (1, 'a', 0), (2, 'b', 0) */;
行为 INSERT Table(1:21~1:22) /test/1/catalog1/schema1/t/
------
SQL  INSERT INTO split_packet_native.t1 VALUES (101,REPEAT('ab',@max_allowed_packet));
行为 INSERT Table(1:12~1:34) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:47~1:53) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:59~1:78) /test/1/max_allowed_packet/
------
SQL  INSERT INTO split_packet_native.t1 SELECT 101,REPEAT('ab',@max_allowed_packet);
行为 INSERT Table(1:12~1:34) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:46~1:52) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:58~1:77) /test/1/max_allowed_packet/
------
SQL  INSERT IGNORE INTO split_packet_native.t1 SELECT 101,REPEAT('ab',@max_allowed_packet);
行为 INSERT Table(1:19~1:41) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:53~1:59) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:65~1:84) /test/1/max_allowed_packet/
------
SQL  INSERT INTO t1 VALUES ('ŁĄŞŻ');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO t1 VALUES ('ŁĄŞŻ' '');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO interval_t VALUES (INTERVAL(3,2,1) + 1, "1997-12-31 23:59:59" + INTERVAL 1 SECOND);
行为 INSERT Table(1:12~1:22) /test/1/catalog1/schema1/interval_t/
行为 CALL Function(1:31~1:39) /test/1/catalog1/schema1/INTERVAL/
------
SQL  insert into test (id, name) values (1, 'abc');
行为 INSERT Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  insert into test values (1, 'abc');
行为 INSERT Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  insert into test (id, name) values (1, 'a'), (2, 'b'), (3, 'c');
行为 INSERT Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  insert into test set id = 1, name = 'abc';
行为 INSERT Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  INSERT INTO split_derived_80common.ref_t(a,b) VALUES(999,(SELECT a UNION SELECT a));
行为 INSERT Table(1:12~1:40) /test/1/catalog1/split_derived_80common/ref_t/
------
SQL  INSERT IGNORE INTO int_values VALUES ('21474836461','21474836461');
行为 INSERT Table(1:19~1:29) /test/1/catalog1/schema1/int_values/
------
SQL  INSERT IGNORE INTO int_values VALUES ('-21474836461','-21474836461');
行为 INSERT Table(1:19~1:29) /test/1/catalog1/schema1/int_values/
------
SQL  INSERT INTO split_type_json.json_core(id,doc,payload,meta) VALUES\n      (1,'{"name":"Ada","kind":"person","score":10}',\n         '{"tags":["sql","json"],"old":true}',JSON_OBJECT('active',TRUE)),\n      (2,JSON_OBJECT('name','Lin','kind','person','score',20),\n         JSON_ARRAY('parser','mysql'),JSON_ARRAY()),\n      (3,CAST('{"name":"Kai","kind":"service","score":30}' AS JSON),\n         NULL,CAST('null' AS JSON));
行为 INSERT Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(3:46~3:57) /test/1/catalog1/schema1/JSON_OBJECT/
行为 CALL Function(5:9~5:19) /test/1/catalog1/schema1/JSON_ARRAY/
行为 CALL Function(6:9~6:13) /test/1/catalog1/schema1/CAST/
------
SQL  INSERT INTO split_type_json.json_core\n    SET id=4,\n        doc=JSON_SET('{}','$.name','Mia','$.kind','person','$.score',40),\n        payload=JSON_OBJECT('tags',JSON_ARRAY('set','syntax')),\n        meta=JSON_OBJECT();
行为 INSERT Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(3:12~3:20) /test/1/catalog1/schema1/JSON_SET/
行为 CALL Function(4:16~4:27) /test/1/catalog1/schema1/JSON_OBJECT/
行为 CALL Function(4:35~4:45) /test/1/catalog1/schema1/JSON_ARRAY/
------
SQL  INSERT INTO spatial_generated (id,p)\nVALUES (1,ST_PointFromText('POINT(1 1)'));
行为 INSERT Table(1:12~1:29) /test/1/catalog1/schema1/spatial_generated/
行为 CALL Function(2:10~2:26) /test/1/catalog1/schema1/ST_PointFromText/
------
SQL  INSERT INTO target_t SELECT 1 WHERE 1;
行为 INSERT Table(1:12~1:20) /test/1/catalog1/schema1/target_t/
------
SQL  INSERT INTO t1 VALUES(1,1.1,1.1e100,'1','1',DATE'2020-01-01',TIME'01:01:01',TIMESTAMP'2020-01-01 01:01:01','{"i":1,"s":"1"}','1','"1"'),(2,2.2,2.2e100,'2','2',DATE'2020-02-02',TIME'02:02:02',TIMESTAMP'2020-02-02 02:02:02','{"i":2,"s":"2"}','2','"2"');
行为 INSERT Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO uuid_binary_rows VALUES (UNHEX('7f9d04ae61b34468ac798ffcc984ab68')), (UUID_TO_BIN('d00653b2-90b9-40d1-93c2-194456bd4f3d'));
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/uuid_binary_rows/
行为 CALL Function(1:37~1:42) /test/1/catalog1/schema1/UNHEX/
行为 CALL Function(1:82~1:93) /test/1/catalog1/schema1/UUID_TO_BIN/
------
SQL  INSERT INTO split_replication_wait_probe(wait_result) VALUES (SOURCE_POS_WAIT('missing-binlog.000001',4,0));
行为 INSERT Table(1:12~1:40) /test/1/catalog1/schema1/split_replication_wait_probe/
行为 CALL Function(1:62~1:77) /test/1/catalog1/schema1/SOURCE_POS_WAIT/
------
SQL  INSERT INTO dml_audit.defaults_t VALUES ROW();
行为 INSERT Table(1:12~1:32) /test/1/catalog1/dml_audit/defaults_t/
------
SQL  INSERT INTO dml_audit.t(id,v) WITH RECURSIVE cte(n) AS (SELECT 31 UNION ALL SELECT n+1 FROM cte WHERE n<32) SELECT n,'i07' FROM cte;
行为 INSERT Table(1:12~1:23) /test/1/catalog1/dml_audit/t/
------
SQL  INSERT INTO ptab PARTITION (p0) (id, name, val) VALUES ROW(3, 'row-one', 30), ROW(4, 'row-two', 40);
行为 INSERT Table(1:12~1:16) /test/1/catalog1/schema1/ptab/
------
SQL  INSERT INTO dml_audit.t (id,v) VALUES (12,'i03') AS new;
行为 INSERT Table(1:12~1:23) /test/1/catalog1/dml_audit/t/
------
SQL  INSERT INTO temporal_modern VALUES(3,'2020-01-03 12:12:12.123456+05:30','2020-01-03 12:12:12.123456-04:00');
行为 INSERT Table(1:12~1:27) /test/1/catalog1/schema1/temporal_modern/
------
SQL  INSERT INTO spatial_modern (id,p,g,p0)\nVALUES (\n  1,\n  ST_GeomFromText('POINT(10 20)',4326),\n  ST_GeomFromText('LINESTRING(0 0,1 1)',0),\n  ST_GeomFromText('POINT(0 0)',0)\n);
行为 INSERT Table(1:12~1:26) /test/1/catalog1/schema1/spatial_modern/
行为 CALL Function(4:2~4:17) /test/1/catalog1/schema1/ST_GeomFromText/
------
SQL  INSERT INTO t VALUES ROW(1),ROW(2) UNION ALL VALUES ROW(2),ROW(3);
行为 INSERT Table(1:12~1:13) /test/1/catalog1/schema1/t/
------
SQL  INSERT INTO split_select_short.distinct_t (WITH RECURSIVE a(i) AS (SELECT 0 UNION ALL SELECT i+1 FROM a WHERE i<2), b(i) AS (SELECT x.i+y.i*10+z.i*100 FROM a AS x,a AS y,a AS z) SELECT 'b','a','a','a211','xy1','' FROM b);
行为 INSERT Table(1:12~1:41) /test/1/catalog1/split_select_short/distinct_t/
------
SQL  INSERT INTO employees (id, name, position, salary) VALUES (1, '张三', '软件工程师', 75000);
行为 INSERT Table(1:12~1:21) /test/1/catalog1/schema1/employees/
------
SQL  INSERT INTO t(id, name, val) VALUES ROW(20, 'rowed', 1);
行为 INSERT Table(1:12~1:13) /test/1/catalog1/schema1/t/
------
SQL  INSERT INTO vector_lifecycle\n  (id,grp,embedding,embedding_default,embedding2,note)\nVALUES\n  (1,1,TO_VECTOR('[1,2,3]'),DEFAULT,TO_VECTOR('[1,2]'),'values'),\n  (2,1,STRING_TO_VECTOR('[2,3,4]'),TO_VECTOR('[4,5,6]'),TO_VECTOR('[2,3]'),'values');
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(4:7~4:16) /test/1/catalog1/schema1/TO_VECTOR/
行为 CALL Function(5:7~5:23) /test/1/catalog1/schema1/STRING_TO_VECTOR/
------
SQL  INSERT INTO vector_lifecycle SET\n  id=3,\n  grp=2,\n  embedding=TO_VECTOR('[3,4,5]'),\n  embedding_default=DEFAULT,\n  embedding2=TO_VECTOR('[3,4]'),\n  note='set';
行为 INSERT Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(4:12~4:21) /test/1/catalog1/schema1/TO_VECTOR/
------
SQL  INSERT INTO splitvector.t_vector_dims VALUES (1, STRING_TO_VECTOR('[1.05, -17.8, 32]'));
行为 INSERT Table(1:12~1:37) /test/1/catalog1/splitvector/t_vector_dims/
行为 CALL Function(1:49~1:65) /test/1/catalog1/schema1/STRING_TO_VECTOR/
------
SQL  INSERT INTO splitvector.t_vector_dims VALUES (2, TO_VECTOR('[2, 3, 5]'));
行为 INSERT Table(1:12~1:37) /test/1/catalog1/splitvector/t_vector_dims/
行为 CALL Function(1:49~1:58) /test/1/catalog1/schema1/TO_VECTOR/

## TRUNCATE_TABLE

SQL  TRUNCATE TABLE split_table.as_select;
行为 ALTER Table(1:15~1:36) /test/1/catalog1/split_table/as_select/
------
SQL  TRUNCATE split_table.as_renamed;
行为 ALTER Table(1:9~1:31) /test/1/catalog1/split_table/as_renamed/
------
SQL  truncate table test;
行为 ALTER Table(1:15~1:19) /test/1/catalog1/schema1/test/
------
SQL  truncate table test.abc;
行为 ALTER Table(1:15~1:23) /test/1/catalog1/test/abc/
------
SQL  truncate abc;
行为 ALTER Table(1:9~1:12) /test/1/catalog1/schema1/abc/

## DROP_VIEW

SQL  DROP VIEW IF EXISTS split_view56.v_base, split_view56.v_joined;
行为 DROP View(1:20~1:39) /test/1/catalog1/split_view56/v_base/
行为 DROP View(1:41~1:62) /test/1/catalog1/split_view56/v_joined/
------
SQL  DROP VIEW IF EXISTS split_view56.v_joined RESTRICT;
行为 DROP View(1:20~1:41) /test/1/catalog1/split_view56/v_joined/
------
SQL  DROP VIEW split_view56.v_base CASCADE;
行为 DROP View(1:10~1:29) /test/1/catalog1/split_view56/v_base/
------
SQL  DROP VIEW IF EXISTS split_view57.v_base, split_view57.v_joined;
行为 DROP View(1:20~1:39) /test/1/catalog1/split_view57/v_base/
行为 DROP View(1:41~1:62) /test/1/catalog1/split_view57/v_joined/
------
SQL  DROP VIEW IF EXISTS split_view57.v_joined RESTRICT;
行为 DROP View(1:20~1:41) /test/1/catalog1/split_view57/v_joined/
------
SQL  DROP VIEW split_view57.v_base CASCADE;
行为 DROP View(1:10~1:29) /test/1/catalog1/split_view57/v_base/
------
SQL  DROP VIEW splitv.dv_drop_cascade_a, splitv.dv_drop_cascade_b CASCADE;
行为 DROP View(1:10~1:34) /test/1/catalog1/splitv/dv_drop_cascade_a/
行为 DROP View(1:36~1:60) /test/1/catalog1/splitv/dv_drop_cascade_b/
------
SQL  DROP VIEW IF EXISTS splitv.dv_drop_if_exists, splitv.dv_drop_missing CASCADE;
行为 DROP View(1:20~1:44) /test/1/catalog1/splitv/dv_drop_if_exists/
行为 DROP View(1:46~1:68) /test/1/catalog1/splitv/dv_drop_missing/
------
SQL  DROP VIEW split_view80.v_cte, split_view80.v_table, split_view80.v_values;
行为 DROP View(1:10~1:28) /test/1/catalog1/split_view80/v_cte/
行为 DROP View(1:30~1:50) /test/1/catalog1/split_view80/v_table/
行为 DROP View(1:52~1:73) /test/1/catalog1/split_view80/v_values/
------
SQL  drop view v_test;
行为 DROP View(1:10~1:16) /test/1/catalog1/schema1/v_test/
------
SQL  drop view if exists v_test;
行为 DROP View(1:20~1:26) /test/1/catalog1/schema1/v_test/
------
SQL  drop view test.v_test;
行为 DROP View(1:10~1:21) /test/1/catalog1/test/v_test/
------
SQL  DROP VIEW v84_checked;
行为 DROP View(1:10~1:21) /test/1/catalog1/schema1/v84_checked/
------
SQL  DROP VIEW IF EXISTS split84.v84_base, split84.v84_joined, split84.v84_table_stmt, split84.v84_values_stmt, split84.v84_cte;
行为 DROP View(1:20~1:36) /test/1/catalog1/split84/v84_base/
行为 DROP View(1:38~1:56) /test/1/catalog1/split84/v84_joined/
行为 DROP View(1:58~1:80) /test/1/catalog1/split84/v84_table_stmt/
行为 DROP View(1:82~1:105) /test/1/catalog1/split84/v84_values_stmt/
行为 DROP View(1:107~1:122) /test/1/catalog1/split84/v84_cte/
------
SQL  DROP VIEW split84.v84_base CASCADE;
行为 DROP View(1:10~1:26) /test/1/catalog1/split84/v84_base/
------
SQL  DROP VIEW IF EXISTS split84.v84_base, split84.v84_joined, split84.v84_table_stmt, split84.v84_values_stmt, split84.v84_cte RESTRICT;
行为 DROP View(1:20~1:36) /test/1/catalog1/split84/v84_base/
行为 DROP View(1:38~1:56) /test/1/catalog1/split84/v84_joined/
行为 DROP View(1:58~1:80) /test/1/catalog1/split84/v84_table_stmt/
行为 DROP View(1:82~1:105) /test/1/catalog1/split84/v84_values_stmt/
行为 DROP View(1:107~1:122) /test/1/catalog1/split84/v84_cte/
------
SQL  DROP VIEW splitjdv.jdv_basic, splitjdv.jdv_rel, splitjdv.jdv_def, splitjdv.jdv_exists;
行为 DROP View(1:10~1:28) /test/1/catalog1/splitjdv/jdv_basic/
行为 DROP View(1:30~1:46) /test/1/catalog1/splitjdv/jdv_rel/
行为 DROP View(1:48~1:64) /test/1/catalog1/splitjdv/jdv_def/
行为 DROP View(1:66~1:85) /test/1/catalog1/splitjdv/jdv_exists/
------
SQL  DROP VIEW IF EXISTS split_view97.v_base, split_view97.v_joined;
行为 DROP View(1:20~1:39) /test/1/catalog1/split_view97/v_base/
行为 DROP View(1:41~1:62) /test/1/catalog1/split_view97/v_joined/
------
SQL  DROP VIEW IF EXISTS split_view97.v_joined RESTRICT;
行为 DROP View(1:20~1:41) /test/1/catalog1/split_view97/v_joined/
------
SQL  DROP VIEW split_view97.v_base CASCADE;
行为 DROP View(1:10~1:29) /test/1/catalog1/split_view97/v_base/
------
SQL  DROP VIEW split_view97.v_cte, split_view97.v_table, split_view97.v_values;
行为 DROP View(1:10~1:28) /test/1/catalog1/split_view97/v_cte/
行为 DROP View(1:30~1:50) /test/1/catalog1/split_view97/v_table/
行为 DROP View(1:52~1:73) /test/1/catalog1/split_view97/v_values/

## DELETE

SQL  delete from test;
行为 DELETE Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  delete ignore from test where id = 1;
行为 DELETE Table(1:19~1:23) /test/1/catalog1/schema1/test/
------
SQL  DELETE LOW_PRIORITY QUICK IGNORE FROM ptab PARTITION (p0) WHERE id < 0 ORDER BY id LIMIT 1;
行为 DELETE Table(1:38~1:42) /test/1/catalog1/schema1/ptab/
------
SQL  DELETE FROM articles WHERE MATCH(title,body) AGAINST('+database' IN BOOLEAN MODE);
行为 DELETE Table(1:12~1:20) /test/1/catalog1/schema1/articles/
------
SQL  DELETE FROM t1 WHERE (@a := f1) ORDER BY f1 LIMIT 1;
行为 DELETE Table(1:12~1:14) /test/1/catalog1/schema1/t1/
行为 READ ConfigKey(1:22~1:24) /test/1/a/
------
SQL  DELETE IGNORE QUICK LOW_PRIORITY FROM dml_audit.t WHERE id=999 ORDER BY id LIMIT 1;
行为 DELETE Table(1:38~1:49) /test/1/catalog1/dml_audit/t/
------
SQL  DELETE FROM part_t PARTITION(p0,p1) WHERE id IN(2,12);
行为 DELETE Table(1:12~1:18) /test/1/catalog1/schema1/part_t/
------
SQL  DELETE FROM bit_values WHERE b=FALSE OR b BETWEEN b'1' AND b'111';
行为 DELETE Table(1:12~1:22) /test/1/catalog1/schema1/bit_values/
------
SQL  DELETE FROM time_widths WHERE t6 IN(TIME'00:00:00.000000',NULL) OR t6 NOT BETWEEN TIME'-24:00:00' AND TIME'24:00:00';
行为 DELETE Table(1:12~1:23) /test/1/catalog1/schema1/time_widths/
------
SQL  DELETE FROM str_lifecycle WHERE variable_binary=X'6279746573' OR c BETWEEN 'a' AND 'z';
行为 DELETE Table(1:12~1:25) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  DELETE FROM lob_family WHERE b=X'4100' OR t COLLATE utf8mb4_bin BETWEEN 'a' AND 'z';
行为 DELETE Table(1:12~1:22) /test/1/catalog1/schema1/lob_family/
------
SQL  DELETE FROM integer_key_partition\nWHERE id IN (0,18446744073709551615);
行为 DELETE Table(1:12~1:33) /test/1/catalog1/schema1/integer_key_partition/
------
SQL  DELETE FROM codex_func_in.nullable_field WHERE field NOT IN (NULL);
行为 DELETE Table(1:12~1:40) /test/1/catalog1/codex_func_in/nullable_field/
------
SQL  DELETE FROM rb_aux WHERE f1=rb_fail();
行为 DELETE Table(1:12~1:18) /test/1/catalog1/schema1/rb_aux/
行为 CALL Function(1:28~1:35) /test/1/catalog1/schema1/rb_fail/
------
SQL  DELETE FROM inet_values ORDER BY INET_NTOA(a) DESC LIMIT 10;
行为 DELETE Table(1:12~1:23) /test/1/catalog1/schema1/inet_values/
行为 CALL Function(1:33~1:42) /test/1/catalog1/schema1/INET_NTOA/
------
SQL  DELETE FROM split_management_function_probe WHERE set_firewall_mode('fwuser@localhost','RECORDING') IS NOT NULL;
行为 DELETE Table(1:12~1:43) /test/1/catalog1/schema1/split_management_function_probe/
行为 CONFIGURE Function(1:50~1:67) /test/1/catalog1/schema1/set_firewall_mode/
------
SQL  DELETE FROM split_replication_wait_probe WHERE MASTER_POS_WAIT('missing-binlog.000001',4,0) IS NULL;
行为 DELETE Table(1:12~1:40) /test/1/catalog1/schema1/split_replication_wait_probe/
行为 CALL Function(1:47~1:62) /test/1/catalog1/schema1/MASTER_POS_WAIT/
------
SQL  /*!50000 DELETE FROM t WHERE id = 2 */;
行为 DELETE Table(1:21~1:22) /test/1/catalog1/schema1/t/
------
SQL  DELETE FROM split_packet_native.t1 WHERE c12 <=> REPEAT('ab',@max_allowed_packet);
行为 DELETE Table(1:12~1:34) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:49~1:55) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:61~1:80) /test/1/max_allowed_packet/
------
SQL  DELETE FROM split_parser_big.t1 WHERE (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=1 and b=2) or (a=2 and b=1);
行为 DELETE Table(1:12~1:31) /test/1/catalog1/split_parser_big/t1/
------
SQL  DELETE QUICK FROM dml_t WHERE i = 0;
行为 DELETE Table(1:18~1:23) /test/1/catalog1/schema1/dml_t/
------
SQL  delete from test where id = 1;
行为 DELETE Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  delete from test where id > 10 order by id limit 5;
行为 DELETE Table(1:12~1:16) /test/1/catalog1/schema1/test/
------
SQL  DELETE FROM t LIMIT 0;
行为 DELETE Table(1:12~1:13) /test/1/catalog1/schema1/t/
------
SQL  DELETE /*+ INDEX(t1 i_a) */ FROM split_opt_hints_index.t1 WHERE a=100 AND b=2 AND c=3;
行为 DELETE Table(1:33~1:57) /test/1/catalog1/split_opt_hints_index/t1/
------
SQL  DELETE /*+ SET_VAR(time_zone='UTC') */ FROM t1 WHERE id=999;
行为 DELETE Table(1:44~1:46) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:19~1:28) /test/1/time_zone/
------
SQL  DELETE FROM split_replication_wait_probe WHERE WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS('',0) IS NULL;
行为 DELETE Table(1:12~1:40) /test/1/catalog1/schema1/split_replication_wait_probe/
行为 CALL Function(1:47~1:80) /test/1/catalog1/schema1/WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS/
------
SQL  DELETE LOW_PRIORITY FROM ptab AS p PARTITION (p0) WHERE p.id < 0;
行为 DELETE Table(1:25~1:29) /test/1/catalog1/schema1/ptab/
------
SQL  DELETE FROM jt_base WHERE EXISTS (SELECT a FROM JSON_TABLE('[{"a":"3"}]', '$[0].a' COLUMNS (a FOR ORDINALITY)) AS q);
行为 DELETE Table(1:12~1:19) /test/1/catalog1/schema1/jt_base/
------
SQL  DELETE FROM split_window_context.t WHERE SUM(b) OVER ()=10;
行为 DELETE Table(1:12~1:34) /test/1/catalog1/split_window_context/t/
行为 CALL Function(1:41~1:44) /test/1/catalog1/schema1/SUM/
------
SQL  DELETE FROM employees WHERE id = 1;
行为 DELETE Table(1:12~1:21) /test/1/catalog1/schema1/employees/

## BLOCK

SQL  DO ST_TRANSFORM(ST_GEOMFROMTEXT('POINT(0 0)'),4326);
行为 CALL Function(1:3~1:15) /test/1/catalog1/schema1/ST_TRANSFORM/
行为 CALL Function(1:16~1:31) /test/1/catalog1/schema1/ST_GEOMFROMTEXT/
------
SQL  DO ST_TRANSFORM(ST_GEOMFROMTEXT('POINT(0 0)',4326),0);
行为 CALL Function(1:3~1:15) /test/1/catalog1/schema1/ST_TRANSFORM/
行为 CALL Function(1:16~1:31) /test/1/catalog1/schema1/ST_GEOMFROMTEXT/
------
SQL  DO 1 + 1, ABS(-2), COALESCE(NULL, 'fallback');
行为 CALL Function(1:10~1:13) /test/1/catalog1/schema1/ABS/
行为 CALL Function(1:19~1:27) /test/1/catalog1/schema1/COALESCE/
------
SQL  DO @do_counter := 0, @do_counter := @do_counter + 1, @do_counter := @do_counter * 10;
行为 READ ConfigKey(1:3~1:14) /test/1/do_counter/
------
SQL  DO RELEASE_LOCK(GET_LOCK('splitv_do_lock', 0));
行为 LOCK Function(1:3~1:15) /test/1/catalog1/schema1/RELEASE_LOCK/
行为 LOCK Function(1:16~1:24) /test/1/catalog1/schema1/GET_LOCK/
------
SQL  DO(CONVERT(CONVERT('',DECIMAL(66,0)), DECIMAL(66,0))), CAST(CONVERT(1,DECIMAL(65,31)) AS DATE);
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:55~1:59) /test/1/catalog1/schema1/CAST/
------
SQL  DO @sq_a:=(SELECT a FROM split_subquery_next.var_t);
行为 READ ConfigKey(1:3~1:8) /test/1/sq_a/
行为 READ Table(1:25~1:50) /test/1/catalog1/split_subquery_next/var_t/
------
SQL  DO EXTRACTVALUE(CONVERT(@f<=>0x2e67ffd1,UNSIGNED),4);
行为 CALL Function(1:3~1:15) /test/1/catalog1/schema1/EXTRACTVALUE/
行为 CALL Function(1:16~1:23) /test/1/catalog1/schema1/CONVERT/
行为 READ ConfigKey(1:24~1:26) /test/1/f/
------
SQL  DO app_func(1);
行为 CALL Function(1:3~1:11) /test/1/catalog1/schema1/app_func/
------
SQL  DO 1 BETWEEN TO_BASE64(PERIOD_ADD(1207980960, 2383)) AND 0x43c98093;
行为 CALL Function(1:13~1:22) /test/1/catalog1/schema1/TO_BASE64/
行为 CALL Function(1:23~1:33) /test/1/catalog1/schema1/PERIOD_ADD/
------
SQL  DO ADDTIME(TIME_FORMAT('-250:07:38.935647','1974-10-18 08:57:35.681107'),1);
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/ADDTIME/
行为 CALL Function(1:11~1:22) /test/1/catalog1/schema1/TIME_FORMAT/
------
SQL  DO NULLIF(CAST(DATABASE() AS TIME),1);
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:10~1:14) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:15~1:23) /test/1/catalog1/schema1/DATABASE/
------
SQL  DO NULLIF(CAST(DATABASE() AS DATE),1);
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:10~1:14) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:15~1:23) /test/1/catalog1/schema1/DATABASE/
------
SQL  DO NULLIF(CAST(DATABASE() AS DATETIME),1);
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:10~1:14) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:15~1:23) /test/1/catalog1/schema1/DATABASE/
------
SQL  DO GROUP_CONCAT(NULLIF(ELT(1,@e),POINT(250,41)) ORDER BY 1);
行为 CALL Function(1:3~1:15) /test/1/catalog1/schema1/GROUP_CONCAT/
行为 CALL Function(1:16~1:22) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:23~1:26) /test/1/catalog1/schema1/ELT/
行为 READ ConfigKey(1:29~1:31) /test/1/e/
行为 CALL Function(1:33~1:38) /test/1/catalog1/schema1/POINT/
------
SQL  DO CAST(UNIX_TIMESTAMP(IF('',3,13339)) AS UNSIGNED);
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:8~1:22) /test/1/catalog1/schema1/UNIX_TIMESTAMP/
行为 CALL Function(1:23~1:25) /test/1/catalog1/schema1/IF/
------
SQL  DO CAST(UNIX_TIMESTAMP(CASE 1 WHEN 1 THEN 42 ELSE 42 END) AS UNSIGNED);
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:8~1:22) /test/1/catalog1/schema1/UNIX_TIMESTAMP/
------
SQL  DO AVG((SELECT POINT(@x, POINT(115, 219)) IS NULL));
行为 CALL Function(1:3~1:6) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/POINT/
行为 READ ConfigKey(1:21~1:23) /test/1/x/
------
SQL  DO ROUND(0xe9b1,NULL), COUNT(DISTINCT ROUND(CAST(SLEEP(0) AS DECIMAL),NULL));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/ROUND/
行为 CALL Function(1:23~1:28) /test/1/catalog1/schema1/COUNT/
行为 CALL Function(1:44~1:48) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:49~1:54) /test/1/catalog1/schema1/SLEEP/
------
SQL  DO DATEDIFF(UUID_TO_BIN(UUID()),0x32df2ce8), (!(SECOND(0xb16beeb7)));
行为 CALL Function(1:3~1:11) /test/1/catalog1/schema1/DATEDIFF/
行为 CALL Function(1:12~1:23) /test/1/catalog1/schema1/UUID_TO_BIN/
行为 CALL Function(1:24~1:28) /test/1/catalog1/schema1/UUID/
行为 CALL Function(1:48~1:54) /test/1/catalog1/schema1/SECOND/
------
SQL  DO (IS_IPV4_MAPPED(BIN_TO_UUID(@misc_value:=34))) <=> (JSON_OBJECTAGG('key2',42) AND RTRIM(''));
行为 CALL Function(1:4~1:18) /test/1/catalog1/schema1/IS_IPV4_MAPPED/
行为 CALL Function(1:19~1:30) /test/1/catalog1/schema1/BIN_TO_UUID/
行为 READ ConfigKey(1:31~1:42) /test/1/misc_value/
行为 CALL Function(1:55~1:69) /test/1/catalog1/schema1/JSON_OBJECTAGG/
行为 CALL Function(1:85~1:90) /test/1/catalog1/schema1/RTRIM/
------
SQL  DO LEAST(0x00,NAME_CONST('a',_tis620'1'));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/LEAST/
行为 CALL Function(1:14~1:24) /test/1/catalog1/schema1/NAME_CONST/
------
SQL  DO BENCHMARK(2,(JSON_ARRAY(UTC_TIME(3)) >= PERIOD_DIFF(32105,2924299961)));
行为 CALL Function(1:3~1:12) /test/1/catalog1/schema1/BENCHMARK/
行为 CALL Function(1:16~1:26) /test/1/catalog1/schema1/JSON_ARRAY/
行为 CALL Function(1:27~1:35) /test/1/catalog1/schema1/UTC_TIME/
行为 CALL Function(1:43~1:54) /test/1/catalog1/schema1/PERIOD_DIFF/
------
SQL  DO 1 REGEXP (MULTILINESTRING(POINT(1,1)));
行为 CALL Function(1:13~1:28) /test/1/catalog1/schema1/MULTILINESTRING/
行为 CALL Function(1:29~1:34) /test/1/catalog1/schema1/POINT/
------
SQL  DO FROM_BASE64(AES_ENCRYPT(RIGHT(CAST(0x5d44f4d736397d92c8267c12 AS DECIMAL),1),RAND(TO_DAYS('2028-12-04 15:50:01.284969'))));
行为 CALL Function(1:3~1:14) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:15~1:26) /test/1/catalog1/schema1/AES_ENCRYPT/
行为 CALL Function(1:27~1:32) /test/1/catalog1/schema1/RIGHT/
行为 CALL Function(1:33~1:37) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:80~1:84) /test/1/catalog1/schema1/RAND/
行为 CALL Function(1:85~1:92) /test/1/catalog1/schema1/TO_DAYS/
------
SQL  DO FROM_BASE64(CAST(RIGHT(11,1) AS BINARY(24)));
行为 CALL Function(1:3~1:14) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:15~1:19) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:20~1:25) /test/1/catalog1/schema1/RIGHT/
------
SQL  DO CONVERT(INET_ATON(CAST(LEFT(-1,1) AS BINARY(30))) USING utf8);
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:11~1:20) /test/1/catalog1/schema1/INET_ATON/
行为 CALL Function(1:21~1:25) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:26~1:30) /test/1/catalog1/schema1/LEFT/
------
SQL  DO FROM_BASE64(CAST(LEFT(COLLATION(4097),(REPEAT('1',32) OR -18772)) AS BINARY(40)));
行为 CALL Function(1:3~1:14) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:15~1:19) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:20~1:24) /test/1/catalog1/schema1/LEFT/
行为 CALL Function(1:25~1:34) /test/1/catalog1/schema1/COLLATION/
行为 CALL Function(1:42~1:48) /test/1/catalog1/schema1/REPEAT/
------
SQL  DO FROM_BASE64(CAST(MID(17653,ROW('-688:20:12.162697',NULL)>=ROW('*.)$',0xc254b6),1) AS BINARY(34)));
行为 CALL Function(1:3~1:14) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:15~1:19) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:20~1:23) /test/1/catalog1/schema1/MID/
行为 CALL Function(1:30~1:33) /test/1/catalog1/schema1/ROW/
------
SQL  DO FROM_BASE64(CAST(MID(UUID(),20,64) AS BINARY(55)));
行为 CALL Function(1:3~1:14) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:15~1:19) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:20~1:23) /test/1/catalog1/schema1/MID/
行为 CALL Function(1:24~1:28) /test/1/catalog1/schema1/UUID/
------
SQL  DO INET_ATON(AES_ENCRYPT(LEFT(@e,1),5));
行为 CALL Function(1:3~1:12) /test/1/catalog1/schema1/INET_ATON/
行为 CALL Function(1:13~1:24) /test/1/catalog1/schema1/AES_ENCRYPT/
行为 CALL Function(1:25~1:29) /test/1/catalog1/schema1/LEFT/
行为 READ ConfigKey(1:30~1:32) /test/1/e/
------
SQL  DO INSERT(1,1,1,CAST(1 AS DECIMAL(1,3)));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/INSERT/
行为 CALL Function(1:16~1:20) /test/1/catalog1/schema1/CAST/
------
SQL  DO CONVERT(NULLIF(@a,'c') USING BINARY);
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:11~1:17) /test/1/catalog1/schema1/NULLIF/
行为 READ ConfigKey(1:18~1:20) /test/1/a/
------
SQL  DO CONCAT('a',CONCAT_WS('a',0x2859,'a',TRIM(PERIOD_ADD('a',1) FROM (1&''))));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:14~1:23) /test/1/catalog1/schema1/CONCAT_WS/
行为 CALL Function(1:39~1:43) /test/1/catalog1/schema1/TRIM/
行为 CALL Function(1:44~1:54) /test/1/catalog1/schema1/PERIOD_ADD/
------
SQL  DO CONCAT('a', CONCAT_WS('a', 0x2859, 'a', TRIM(PERIOD_ADD('a',1) FROM (1 & ''))));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:15~1:24) /test/1/catalog1/schema1/CONCAT_WS/
行为 CALL Function(1:43~1:47) /test/1/catalog1/schema1/TRIM/
行为 CALL Function(1:48~1:58) /test/1/catalog1/schema1/PERIOD_ADD/
------
SQL  DO CONCAT('111','11111111111111111111111111',SUBSTRING_INDEX(UUID(),0,1.111111e+308));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:45~1:60) /test/1/catalog1/schema1/SUBSTRING_INDEX/
行为 CALL Function(1:61~1:65) /test/1/catalog1/schema1/UUID/
------
SQL  DO CONCAT_WS(',','111','11111111111111111111111111',SUBSTRING_INDEX(UUID(),0,1.111111e+308));
行为 CALL Function(1:3~1:12) /test/1/catalog1/schema1/CONCAT_WS/
行为 CALL Function(1:52~1:67) /test/1/catalog1/schema1/SUBSTRING_INDEX/
行为 CALL Function(1:68~1:72) /test/1/catalog1/schema1/UUID/
------
SQL  DO RPAD(_utf16'33',1073741826,_latin1'44');
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/RPAD/
------
SQL  DO UNHEX(-1);
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/UNHEX/
------
SQL  DO UNHEX(-182680438);
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/UNHEX/
------
SQL  DO UNHEX(-2251799813685249);
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/UNHEX/
------
SQL  DO CRC32(CHAR(1.134475e+308));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/CRC32/
行为 CALL Function(1:9~1:13) /test/1/catalog1/schema1/CHAR/
------
SQL  DO ST_ISVALID(INSTR(9223372036854775806 ,0x46bc299f));
行为 CALL Function(1:3~1:13) /test/1/catalog1/schema1/ST_ISVALID/
行为 CALL Function(1:14~1:19) /test/1/catalog1/schema1/INSTR/
------
SQL  DO ST_SRID(LOCATE(0x58ed0b8e,8,135));
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/ST_SRID/
行为 CALL Function(1:11~1:17) /test/1/catalog1/schema1/LOCATE/
------
SQL  DO NOT CAST(UTC_TIME AS CHAR(2877246026) CHARACTER SET ujis);
行为 CALL Function(1:7~1:11) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:12~1:20) /test/1/catalog1/schema1/UTC_TIME/
------
SQL  DO REPEAT(LAST_INSERT_ID(''), 4294967295);
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/REPEAT/
行为 CALL Function(1:10~1:24) /test/1/catalog1/schema1/LAST_INSERT_ID/
------
SQL  DO REPEAT(LAST_INSERT_ID(''), 0x96e4ed7e70a7fec7f11572c8980a);
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/REPEAT/
行为 CALL Function(1:10~1:24) /test/1/catalog1/schema1/LAST_INSERT_ID/
------
SQL  DO SPACE(SHA(UUID_SHORT()));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/SPACE/
行为 CALL Function(1:9~1:12) /test/1/catalog1/schema1/SHA/
行为 CALL Function(1:13~1:23) /test/1/catalog1/schema1/UUID_SHORT/
------
SQL  DO RANDOM_BYTES(CAST(RANDOM_BYTES('?+,$ F5') AS UNSIGNED));
行为 CALL Function(1:3~1:15) /test/1/catalog1/schema1/RANDOM_BYTES/
行为 CALL Function(1:16~1:20) /test/1/catalog1/schema1/CAST/
------
SQL  DO LCASE(LTRIM(FROM_UNIXTIME(0,' %T ')));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/LCASE/
行为 CALL Function(1:9~1:14) /test/1/catalog1/schema1/LTRIM/
行为 CALL Function(1:15~1:28) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  DO _cp852 '' <= LCASE(TRIM(LEADING 1 FROM 12222)) NOT BETWEEN '1' AND '2';
行为 CALL Function(1:16~1:21) /test/1/catalog1/schema1/LCASE/
行为 CALL Function(1:22~1:26) /test/1/catalog1/schema1/TRIM/
------
SQL  DO DECODE(SUBSTRING(SHA1('1'),'11'),25);
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/DECODE/
行为 CALL Function(1:10~1:19) /test/1/catalog1/schema1/SUBSTRING/
行为 CALL Function(1:20~1:24) /test/1/catalog1/schema1/SHA1/
------
SQL  DO ENCODE(MID(SYSDATE(),'5',1),'11');
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/ENCODE/
行为 CALL Function(1:10~1:13) /test/1/catalog1/schema1/MID/
行为 CALL Function(1:14~1:21) /test/1/catalog1/schema1/SYSDATE/
------
SQL  DO UPPER(SUBSTRING(1.111111111111111111 FROM '2n'));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/UPPER/
行为 CALL Function(1:9~1:18) /test/1/catalog1/schema1/SUBSTRING/
------
SQL  DO NULLIF(1,'-' BETWEEN LCASE(RIGHT(11111111,' 7,]')) AND '1');
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:24~1:29) /test/1/catalog1/schema1/LCASE/
行为 CALL Function(1:30~1:35) /test/1/catalog1/schema1/RIGHT/
------
SQL  DO UPPER(RIGHT(198039009115594390000000000000000000000.000000,35));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/UPPER/
行为 CALL Function(1:9~1:14) /test/1/catalog1/schema1/RIGHT/
------
SQL  DO REPLACE(LTRIM(FROM_UNIXTIME(0,' %T ')),'0','1');
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/REPLACE/
行为 CALL Function(1:11~1:16) /test/1/catalog1/schema1/LTRIM/
行为 CALL Function(1:17~1:30) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  DO INSERT(LTRIM(FROM_UNIXTIME(0,' %T ')),2,1,'hi');
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/INSERT/
行为 CALL Function(1:10~1:15) /test/1/catalog1/schema1/LTRIM/
行为 CALL Function(1:16~1:29) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  DO FORMAT(RPAD('111111111.1',1111111,'999999999999999999999999999999999999999999'),0,'be_BY');
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/FORMAT/
行为 CALL Function(1:10~1:14) /test/1/catalog1/schema1/RPAD/
------
SQL  DO ROUND(CONCAT(COALESCE(LINEFROMWKB('2147483648',-b'1111111111111111111111111111111111111111111'),CONVERT('[.DC2.]',DECIMAL(30,30)),BIT_COUNT('')),LPAD(ELT('01','}:K5'),SHA1('P'),((SELECT '-9223372036854775808.1' > ALL (SELECT ''))))));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/ROUND/
行为 CALL Function(1:9~1:15) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:16~1:24) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(1:25~1:36) /test/1/catalog1/schema1/LINEFROMWKB/
行为 CALL Function(1:99~1:106) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:133~1:142) /test/1/catalog1/schema1/BIT_COUNT/
行为 CALL Function(1:148~1:152) /test/1/catalog1/schema1/LPAD/
行为 CALL Function(1:153~1:156) /test/1/catalog1/schema1/ELT/
行为 CALL Function(1:170~1:174) /test/1/catalog1/schema1/SHA1/
------
SQL  DO REPEAT(ROW_COUNT(),(-9223372036854775808 << '{ }'));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/REPEAT/
行为 CALL Function(1:10~1:19) /test/1/catalog1/schema1/ROW_COUNT/
------
SQL  DO (REPEAT(1,9223372036854775808) OR CONVERT(0x6d5b5d8d USING dec8));
行为 CALL Function(1:4~1:10) /test/1/catalog1/schema1/REPEAT/
行为 CALL Function(1:37~1:44) /test/1/catalog1/schema1/CONVERT/
------
SQL  DO CONCAT(EXPORT_SET(1,1,REPEAT('a',31),' $',213));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:10~1:20) /test/1/catalog1/schema1/EXPORT_SET/
行为 CALL Function(1:25~1:31) /test/1/catalog1/schema1/REPEAT/
------
SQL  DO SHA1(DATABASE());
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/SHA1/
行为 CALL Function(1:8~1:16) /test/1/catalog1/schema1/DATABASE/
------
SQL  DO GREATEST(RIGHT(@func_test_right,1),1);
行为 CALL Function(1:3~1:11) /test/1/catalog1/schema1/GREATEST/
行为 CALL Function(1:12~1:17) /test/1/catalog1/schema1/RIGHT/
行为 READ ConfigKey(1:18~1:34) /test/1/func_test_right/
------
SQL  DO LEAST(RIGHT(@func_test_right,1),1);
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/LEAST/
行为 CALL Function(1:9~1:14) /test/1/catalog1/schema1/RIGHT/
行为 READ ConfigKey(1:15~1:31) /test/1/func_test_right/
------
SQL  DO NULLIF((GREATEST(FROM_UNIXTIME(1537024679),_utf32 '*b!')),(FROM_UNIXTIME(1537013301)));
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:11~1:19) /test/1/catalog1/schema1/GREATEST/
行为 CALL Function(1:20~1:33) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  DO DAYOFYEAR(MAKETIME(((0x965a)^((@g :=(1 IS NULL)))),EXP(39988664861.65638662152600787509),((STD(@f))LIKE(1))));
行为 CALL Function(1:3~1:12) /test/1/catalog1/schema1/DAYOFYEAR/
行为 CALL Function(1:13~1:21) /test/1/catalog1/schema1/MAKETIME/
行为 READ ConfigKey(1:34~1:36) /test/1/g/
行为 CALL Function(1:54~1:57) /test/1/catalog1/schema1/EXP/
行为 CALL Function(1:94~1:97) /test/1/catalog1/schema1/STD/
行为 READ ConfigKey(1:98~1:100) /test/1/f/
------
SQL  DO LTRIM(WEIGHT_STRING(1)), RTRIM(WEIGHT_STRING(1));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/LTRIM/
行为 CALL Function(1:9~1:22) /test/1/catalog1/schema1/WEIGHT_STRING/
行为 CALL Function(1:28~1:33) /test/1/catalog1/schema1/RTRIM/
------
SQL  DO WEIGHT_STRING(EXTRACTVALUE('','/*/a'));
行为 CALL Function(1:3~1:16) /test/1/catalog1/schema1/WEIGHT_STRING/
行为 CALL Function(1:17~1:29) /test/1/catalog1/schema1/EXTRACTVALUE/
------
SQL  DO CHAR((WEIGHT_STRING(EXTRACTVALUE((''),('tX')))) USING cp852);
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/CHAR/
行为 CALL Function(1:9~1:22) /test/1/catalog1/schema1/WEIGHT_STRING/
行为 CALL Function(1:23~1:35) /test/1/catalog1/schema1/EXTRACTVALUE/
------
SQL  DO WEIGHT_STRING(EXTRACTVALUE('','/*/a') LEVEL 1 REVERSE);
行为 CALL Function(1:3~1:16) /test/1/catalog1/schema1/WEIGHT_STRING/
行为 CALL Function(1:17~1:29) /test/1/catalog1/schema1/EXTRACTVALUE/
------
SQL  DO CHAR((WEIGHT_STRING(EXTRACTVALUE((''),('tX')) LEVEL 7 DESC)) USING cp852);
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/CHAR/
行为 CALL Function(1:9~1:22) /test/1/catalog1/schema1/WEIGHT_STRING/
行为 CALL Function(1:23~1:35) /test/1/catalog1/schema1/EXTRACTVALUE/
------
SQL  DO TIMESTAMP(DATE_FORMAT('2011-11-11', RIGHT('12345' + 1, 3)));
行为 CALL Function(1:3~1:12) /test/1/catalog1/schema1/TIMESTAMP/
行为 CALL Function(1:13~1:24) /test/1/catalog1/schema1/DATE_FORMAT/
行为 CALL Function(1:39~1:44) /test/1/catalog1/schema1/RIGHT/
------
SQL  DO NULLIF(RTRIM(MAKE_SET(CAST('%S' AS UNSIGNED),POINT(0xaf,''))), '');
行为 CALL Function(1:3~1:9) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(1:10~1:15) /test/1/catalog1/schema1/RTRIM/
行为 CALL Function(1:16~1:24) /test/1/catalog1/schema1/MAKE_SET/
行为 CALL Function(1:25~1:29) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:48~1:53) /test/1/catalog1/schema1/POINT/
------
SQL  DO(SELECT 1 c GROUP BY 1 HAVING 1 ORDER BY COUNT(1));
行为 CALL Function(1:43~1:48) /test/1/catalog1/schema1/COUNT/
------
SQL  DO 1 AS one, 2 two;
行为 BLOCK
------
SQL  DO 1 AS 'one', 2 'two';
行为 BLOCK
------
SQL  DO (SELECT 3) AS scalar_value;
行为 BLOCK
------
SQL  DO LPAD(BIT_XOR(1), ROW_COUNT(), JSON_PRETTY('-$ *?(8}'));
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/LPAD/
行为 CALL Function(1:8~1:15) /test/1/catalog1/schema1/BIT_XOR/
行为 CALL Function(1:20~1:29) /test/1/catalog1/schema1/ROW_COUNT/
行为 CALL Function(1:33~1:44) /test/1/catalog1/schema1/JSON_PRETTY/
------
SQL  DO RPAD(BIT_XOR(1), ROW_COUNT(), JSON_PRETTY('-$ *?(8}'));
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/RPAD/
行为 CALL Function(1:8~1:15) /test/1/catalog1/schema1/BIT_XOR/
行为 CALL Function(1:20~1:29) /test/1/catalog1/schema1/ROW_COUNT/
行为 CALL Function(1:33~1:44) /test/1/catalog1/schema1/JSON_PRETTY/
------
SQL  DO ROUND(CONCAT(COALESCE(ST_LINEFROMWKB('2147483648',-b'1111111111111111111111111111111111111111111'),CONVERT('[.DC2.]',DECIMAL(30,30)),BIT_COUNT('')),LPAD(ELT('01','}:K5'),SHA1('P'),((SELECT '-9223372036854775808.1' > ALL (SELECT ''))))));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/ROUND/
行为 CALL Function(1:9~1:15) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:16~1:24) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(1:25~1:39) /test/1/catalog1/schema1/ST_LINEFROMWKB/
行为 CALL Function(1:102~1:109) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:136~1:145) /test/1/catalog1/schema1/BIT_COUNT/
行为 CALL Function(1:151~1:155) /test/1/catalog1/schema1/LPAD/
行为 CALL Function(1:156~1:159) /test/1/catalog1/schema1/ELT/
行为 CALL Function(1:173~1:177) /test/1/catalog1/schema1/SHA1/
------
SQL  DO ST_CROSSES(@g,SUM(SHA(@g)) OVER ()),UNHEX(SUM(@g) OVER ()),BIT_LENGTH(AVG(@f) OVER ()),COMPRESS(SUM(' >') OVER ()),LTRIM(AVG(LOG2(@c)) OVER ()),~(SUM(@f) OVER ()),IS_UUID(SUM(@e) OVER ()),TO_BASE64(AVG(@d) OVER ());
行为 CALL Function(1:3~1:13) /test/1/catalog1/schema1/ST_CROSSES/
行为 READ ConfigKey(1:14~1:16) /test/1/g/
行为 CALL Function(1:17~1:20) /test/1/catalog1/schema1/SUM/
行为 CALL Function(1:21~1:24) /test/1/catalog1/schema1/SHA/
行为 CALL Function(1:39~1:44) /test/1/catalog1/schema1/UNHEX/
行为 CALL Function(1:62~1:72) /test/1/catalog1/schema1/BIT_LENGTH/
行为 CALL Function(1:73~1:76) /test/1/catalog1/schema1/AVG/
行为 READ ConfigKey(1:77~1:79) /test/1/f/
行为 CALL Function(1:90~1:98) /test/1/catalog1/schema1/COMPRESS/
行为 CALL Function(1:118~1:123) /test/1/catalog1/schema1/LTRIM/
行为 CALL Function(1:128~1:132) /test/1/catalog1/schema1/LOG2/
行为 READ ConfigKey(1:133~1:135) /test/1/c/
行为 CALL Function(1:166~1:173) /test/1/catalog1/schema1/IS_UUID/
行为 READ ConfigKey(1:178~1:180) /test/1/e/
行为 CALL Function(1:191~1:200) /test/1/catalog1/schema1/TO_BASE64/
行为 READ ConfigKey(1:205~1:207) /test/1/d/
------
SQL  DO (SELECT a FROM split_window_tail.t WINDOW w2 AS (w1),w1 AS (ORDER BY a,a));
行为 READ Table(1:18~1:37) /test/1/catalog1/split_window_tail/t/
------
SQL  DO LAG(1,9223372036854775807) OVER(),LEAD(1,9223372036854775807) OVER(),NTILE(9223372036854775807) OVER();
行为 CALL Function(1:3~1:6) /test/1/catalog1/schema1/LAG/
行为 CALL Function(1:37~1:41) /test/1/catalog1/schema1/LEAD/
行为 CALL Function(1:72~1:77) /test/1/catalog1/schema1/NTILE/
------
SQL  DO LAG(1,18446744073709551615) OVER(),LEAD(1,18446744073709551615) OVER(),NTILE(18446744073709551615) OVER();
行为 CALL Function(1:3~1:6) /test/1/catalog1/schema1/LAG/
行为 CALL Function(1:38~1:42) /test/1/catalog1/schema1/LEAD/
行为 CALL Function(1:74~1:79) /test/1/catalog1/schema1/NTILE/
------
SQL  DO LAG(1,@v) OVER(),@v:=20;
行为 CALL Function(1:3~1:6) /test/1/catalog1/schema1/LAG/
行为 READ ConfigKey(1:9~1:11) /test/1/v/
------
SQL  DO LAG(1,@v) OVER(), @v:=20;
行为 CALL Function(1:3~1:6) /test/1/catalog1/schema1/LAG/
行为 READ ConfigKey(1:9~1:11) /test/1/v/
------
SQL  DO NTILE(@v) OVER(), @v:=20;
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/NTILE/
行为 READ ConfigKey(1:9~1:11) /test/1/v/
------
SQL  DO('x' IN (CONVERT(EXP(0xbf40f8f5) USING utf32),UNHEX(@e)));
行为 CALL Function(1:11~1:18) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:19~1:22) /test/1/catalog1/schema1/EXP/
行为 CALL Function(1:48~1:53) /test/1/catalog1/schema1/UNHEX/
行为 READ ConfigKey(1:54~1:56) /test/1/e/
------
SQL  DO FROM_BASE64(CAST(MID(17653,ROW('-688:20:12.162697',NULL)>=ROW('*.)$',0xc254b6),1) AS BINARY(34)));
行为 CALL Function(1:3~1:14) /test/1/catalog1/schema1/FROM_BASE64/
行为 CALL Function(1:15~1:19) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:20~1:23) /test/1/catalog1/schema1/MID/
------
SQL  DO USER() IN (COERCIBILITY(@c), CONVERT(LAST_VALUE(FROM_UNIXTIME(1536999169)) OVER() USING utf32));
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/USER/
行为 CALL Function(1:14~1:26) /test/1/catalog1/schema1/COERCIBILITY/
行为 READ ConfigKey(1:27~1:29) /test/1/c/
行为 CALL Function(1:32~1:39) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:40~1:50) /test/1/catalog1/schema1/LAST_VALUE/
行为 CALL Function(1:51~1:64) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  DO '' SOUNDS LIKE LEAD(DATABASE(), 1, x'cafe') OVER();
行为 CALL Function(1:18~1:22) /test/1/catalog1/schema1/LEAD/
行为 CALL Function(1:23~1:31) /test/1/catalog1/schema1/DATABASE/
------
SQL  DO AVG((SELECT POINT(@x, POINT(115, 219)) IS NULL)) OVER ();
行为 CALL Function(1:3~1:6) /test/1/catalog1/schema1/AVG/
行为 CALL Function(1:15~1:20) /test/1/catalog1/schema1/POINT/
行为 READ ConfigKey(1:21~1:23) /test/1/x/
------
SQL  DO CONVERT(INET_ATON(CAST(LEFT(-1,1) AS BINARY(30))) USING utf8mb3);
行为 CALL Function(1:3~1:10) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:11~1:20) /test/1/catalog1/schema1/INET_ATON/
行为 CALL Function(1:21~1:25) /test/1/catalog1/schema1/CAST/
行为 CALL Function(1:26~1:30) /test/1/catalog1/schema1/LEFT/
------
SQL  DO SPACE(SHA2(UUID_SHORT(), 256));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/SPACE/
行为 CALL Function(1:9~1:13) /test/1/catalog1/schema1/SHA2/
行为 CALL Function(1:14~1:24) /test/1/catalog1/schema1/UUID_SHORT/
------
SQL  DO ROUND(CONCAT(COALESCE(ST_LINEFROMWKB('2147483648',-b'1111111111111111111111111111111111111111111'),CONVERT('[.DC2.]',DECIMAL(30,30)),BIT_COUNT('')),LPAD(ELT('01','}:K5'),SHA2('P',256),((SELECT '-9223372036854775808.1' > ALL (SELECT ''))))));
行为 CALL Function(1:3~1:8) /test/1/catalog1/schema1/ROUND/
行为 CALL Function(1:9~1:15) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:16~1:24) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(1:25~1:39) /test/1/catalog1/schema1/ST_LINEFROMWKB/
行为 CALL Function(1:102~1:109) /test/1/catalog1/schema1/CONVERT/
行为 CALL Function(1:136~1:145) /test/1/catalog1/schema1/BIT_COUNT/
行为 CALL Function(1:151~1:155) /test/1/catalog1/schema1/LPAD/
行为 CALL Function(1:156~1:159) /test/1/catalog1/schema1/ELT/
行为 CALL Function(1:173~1:177) /test/1/catalog1/schema1/SHA2/
------
SQL  DO SHA2(DATABASE(),256);
行为 CALL Function(1:3~1:7) /test/1/catalog1/schema1/SHA2/
行为 CALL Function(1:8~1:16) /test/1/catalog1/schema1/DATABASE/

## UNSAFE

SQL  EXECUTE ps_no_args;
行为 ADMIN PrepareStatement(1:8~1:18) /test/1/ps_no_args/
------
SQL  PREPARE ps_select FROM 'SELECT id, v FROM split84.ps_t WHERE id = ?';
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_select/
------
SQL  EXECUTE ps_select USING @ps_id;
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_select/
行为 READ ConfigKey(1:24~1:30) /test/1/ps_id/
------
SQL  DEALLOCATE PREPARE ps_select;
行为 ADMIN PrepareStatement(1:19~1:28) /test/1/ps_select/
------
SQL  PREPARE ps_update FROM @ps_sql;
行为 READ ConfigKey(1:23~1:30) /test/1/ps_sql/
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_update/
------
SQL  EXECUTE ps_update USING @ps_v, @ps_id;
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_update/
行为 READ ConfigKey(1:24~1:29) /test/1/ps_v/
行为 READ ConfigKey(1:31~1:37) /test/1/ps_id/
------
SQL  DROP PREPARE ps_update;
行为 ADMIN PrepareStatement(1:13~1:22) /test/1/ps_update/
------
SQL  PREPARE ps_insert FROM 'INSERT INTO split84.ps_t(id, v, c) VALUES (?, ?, ?)';
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_insert/
------
SQL  EXECUTE ps_insert USING @ps_id, @ps_v, @ps_c;
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_insert/
行为 READ ConfigKey(1:24~1:30) /test/1/ps_id/
行为 READ ConfigKey(1:32~1:37) /test/1/ps_v/
行为 READ ConfigKey(1:39~1:44) /test/1/ps_c/
------
SQL  DEALLOCATE PREPARE ps_insert;
行为 ADMIN PrepareStatement(1:19~1:28) /test/1/ps_insert/
------
SQL  PREPARE ps_delete FROM 'DELETE FROM split84.ps_t WHERE id = ?';
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_delete/
------
SQL  EXECUTE ps_delete USING @ps_id;
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps_delete/
行为 READ ConfigKey(1:24~1:30) /test/1/ps_id/
------
SQL  DEALLOCATE PREPARE ps_delete;
行为 ADMIN PrepareStatement(1:19~1:28) /test/1/ps_delete/
------
SQL  PREPARE p_bt FROM @`quoted sql`;
行为 READ ConfigKey(1:18~1:31) /test/1/`quoted sql`/
行为 ADMIN PrepareStatement(1:8~1:12) /test/1/p_bt/
------
SQL  PREPARE p_dq FROM @"quoted sql";
行为 READ ConfigKey(1:18~1:31) /test/1/"quoted sql"/
行为 ADMIN PrepareStatement(1:8~1:12) /test/1/p_dq/
------
SQL  PREPARE split_dml_s1 FROM 'INSERT INTO split_dml_p1 VALUES (@split_dml_a),(?)';
行为 ADMIN PrepareStatement(1:8~1:20) /test/1/split_dml_s1/
------
SQL  PREPARE split_dml_s2 FROM 'INSERT INTO split_dml_p2 SELECT 100 LIMIT ?';
行为 ADMIN PrepareStatement(1:8~1:20) /test/1/split_dml_s2/
------
SQL  PREPARE `ps name` FROM 'SELECT 1';
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps name/
------
SQL  EXECUTE `ps name`;
行为 ADMIN PrepareStatement(1:8~1:17) /test/1/ps name/
------
SQL  DEALLOCATE PREPARE `ps name`;
行为 ADMIN PrepareStatement(1:19~1:28) /test/1/ps name/
------
SQL  PREPARE p2 FROM @'sql text';
行为 READ ConfigKey(1:16~1:27) /test/1/'sql text'/
行为 ADMIN PrepareStatement(1:8~1:10) /test/1/p2/
------
SQL  EXECUTE p3 USING @'arg name';
行为 ADMIN PrepareStatement(1:8~1:10) /test/1/p3/
行为 READ ConfigKey(1:17~1:28) /test/1/'arg name'/
------
SQL  PREPARE stmt_set_var FROM 'SELECT /*+ SET_VAR(big_tables=on) */ 1';
行为 ADMIN PrepareStatement(1:8~1:20) /test/1/stmt_set_var/
------
SQL  SHUTDOWN;
行为 UNSAFE
------
SQL  PREPARE stmt1 FROM 'SELECT * FROM t1';
行为 ADMIN PrepareStatement(1:8~1:13) /test/1/stmt1/
------
SQL  EXECUTE stmt1;
行为 ADMIN PrepareStatement(1:8~1:13) /test/1/stmt1/
------
SQL  DEALLOCATE PREPARE stmt1;
行为 ADMIN PrepareStatement(1:19~1:24) /test/1/stmt1/
------
SQL  RESTART;
行为 UNSAFE
------
SQL  PREPARE stmt1 FROM "SELECT a.i,(LAST_VALUE(a.i) OVER outer_window)=a.i FROM (SELECT LAG(i) OVER inner_window AS i_lag,i FROM split_window_bugs.t_prep WINDOW inner_window AS (ORDER BY i)) a WINDOW outer_window AS (ORDER BY a.i)";
行为 ADMIN PrepareStatement(1:8~1:13) /test/1/stmt1/
------
SQL  PREPARE sample_stmt FROM 'SELECT * FROM split_sample.t TABLESAMPLE SYSTEM (?)';
行为 ADMIN PrepareStatement(1:8~1:19) /test/1/sample_stmt/

## UPDATE

SQL  UPDATE default_function_t SET a = DEFAULT(a), b = DEFAULT(b) WHERE id = 1;
行为 UPDATE Table(1:7~1:25) /test/1/catalog1/schema1/default_function_t/
行为 CALL Function(1:34~1:41) /test/1/catalog1/schema1/DEFAULT/
------
SQL  UPDATE aes_feedback_modes SET b128 = AES_ENCRYPT(a, @aes_key1, @aes_iva), b192 = AES_ENCRYPT(a, @aes_key1, @aes_iva), b256 = AES_ENCRYPT(a, @aes_key1, @aes_iva);
行为 UPDATE Table(1:7~1:25) /test/1/catalog1/schema1/aes_feedback_modes/
行为 CALL Function(1:37~1:48) /test/1/catalog1/schema1/AES_ENCRYPT/
行为 READ ConfigKey(1:52~1:61) /test/1/aes_key1/
行为 READ ConfigKey(1:63~1:71) /test/1/aes_iva/
------
SQL  UPDATE aes_ecb_modes SET b128 = AES_ENCRYPT(a, 'a'), b192 = AES_ENCRYPT(a, 'a'), b256 = AES_ENCRYPT(a, 'a');
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/aes_ecb_modes/
行为 CALL Function(1:32~1:43) /test/1/catalog1/schema1/AES_ENCRYPT/
------
SQL  UPDATE des_storage SET des=DES_ENCRYPT('1234');
行为 UPDATE Table(1:7~1:18) /test/1/catalog1/schema1/des_storage/
行为 CALL Function(1:27~1:38) /test/1/catalog1/schema1/DES_ENCRYPT/
------
SQL  UPDATE IGNORE e1 AS outr1, b1 AS outr2\nSET outr1.col_date = JSON_SET(outr1.col_date, CONCAT('$','[',1,']','.','cdate'), '2007-07-12')\nWHERE outr1.pk <= ANY (\n  SELECT DISTINCT innr1.col_int_key AS y\n  FROM bb4 AS innr2\n  LEFT JOIN bb4 AS innr1 ON innr2.col_int_key <> innr1.col_int_key\n  WHERE innr1.col_int_key = 4\n);
行为 READ Table(1:14~1:16) /test/1/catalog1/schema1/e1/
行为 READ Table(1:27~1:29) /test/1/catalog1/schema1/b1/
行为 CALL Function(2:21~2:29) /test/1/catalog1/schema1/JSON_SET/
行为 CALL Function(2:46~2:52) /test/1/catalog1/schema1/CONCAT/
行为 READ Table(5:7~5:10) /test/1/catalog1/schema1/bb4/
------
SQL  UPDATE update_t SET c1=2 WHERE IF(TRUE,'2015-01-01','2015-01-01') IS NOT NULL;
行为 UPDATE Table(1:7~1:15) /test/1/catalog1/schema1/update_t/
行为 CALL Function(1:31~1:33) /test/1/catalog1/schema1/IF/
------
SQL  UPDATE codex_func_in.update_t SET b=1 WHERE a NOT IN (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100);
行为 UPDATE Table(1:7~1:29) /test/1/catalog1/codex_func_in/update_t/
------
SQL  UPDATE rb_aux SET f2=rb_fail()+1;
行为 UPDATE Table(1:7~1:13) /test/1/catalog1/schema1/rb_aux/
行为 CALL Function(1:21~1:28) /test/1/catalog1/schema1/rb_fail/
------
SQL  UPDATE base64_encoded SET encoded_value = TO_BASE64(FROM_BASE64(encoded_value)) WHERE encoded_value IS NOT NULL;
行为 UPDATE Table(1:7~1:21) /test/1/catalog1/schema1/base64_encoded/
行为 CALL Function(1:42~1:51) /test/1/catalog1/schema1/TO_BASE64/
行为 CALL Function(1:52~1:63) /test/1/catalog1/schema1/FROM_BASE64/
------
SQL  UPDATE t_update SET c0=1 WHERE (t_update.c0 IS NULL)>>('');
行为 UPDATE Table(1:7~1:15) /test/1/catalog1/schema1/t_update/
------
SQL  UPDATE t_update SET c0=1 WHERE (t_update.c0 IS NULL)>>('' COLLATE 'utf8mb4_0900_ai_ci');
行为 UPDATE Table(1:7~1:15) /test/1/catalog1/schema1/t_update/
------
SQL  UPDATE timestamp_source SET f2=NOW(),f3=FROM_UNIXTIME('9999999999') WHERE f1=1;
行为 UPDATE Table(1:7~1:23) /test/1/catalog1/schema1/timestamp_source/
行为 CALL Function(1:31~1:34) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:40~1:53) /test/1/catalog1/schema1/FROM_UNIXTIME/
------
SQL  UPDATE fd_update_target STRAIGHT_JOIN fd_update_source\nSET fd_update_target.b=fd_update_target.b+1\nWHERE fd_update_target.a=fd_update_source.a\n  AND fd_update_target.ts>='2000-09-28 00:00:00';
行为 READ Table(1:7~1:23) /test/1/catalog1/schema1/fd_update_target/
行为 READ Table(1:38~1:54) /test/1/catalog1/schema1/fd_update_source/
------
SQL  UPDATE split_management_function_probe SET status_code=mysql_firewall_flush_status() WHERE set_firewall_mode('fwuser@localhost','RECORDING') IS NOT NULL;
行为 UPDATE Table(1:7~1:38) /test/1/catalog1/schema1/split_management_function_probe/
行为 CALL Function(1:55~1:82) /test/1/catalog1/schema1/mysql_firewall_flush_status/
行为 CONFIGURE Function(1:91~1:108) /test/1/catalog1/schema1/set_firewall_mode/
------
SQL  UPDATE split_replication_wait_probe SET wait_result=WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS('',0) WHERE id=1;
行为 UPDATE Table(1:7~1:35) /test/1/catalog1/schema1/split_replication_wait_probe/
行为 CALL Function(1:52~1:85) /test/1/catalog1/schema1/WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS/
------
SQL  /*!50000 UPDATE t SET flag = 1 WHERE id = 1 */;
行为 UPDATE Table(1:16~1:17) /test/1/catalog1/schema1/t/
------
SQL  UPDATE split_packet_native.t1 SET c12=REPEAT('ab',@max_allowed_packet);
行为 UPDATE Table(1:7~1:29) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:38~1:44) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:50~1:69) /test/1/max_allowed_packet/
------
SQL  UPDATE IGNORE split_packet_native.t1 SET c12=REPEAT('ab',@max_allowed_packet);
行为 UPDATE Table(1:14~1:36) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:45~1:51) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:57~1:76) /test/1/max_allowed_packet/
------
SQL  UPDATE split_packet_native.t1,split_packet_native.t2 SET c12=REPEAT('ab',@max_allowed_packet),c22='ab';
行为 READ Table(1:7~1:29) /test/1/catalog1/split_packet_native/t1/
行为 READ Table(1:30~1:52) /test/1/catalog1/split_packet_native/t2/
行为 CALL Function(1:61~1:67) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:73~1:92) /test/1/max_allowed_packet/
------
SQL  UPDATE IGNORE split_packet_native.t1,split_packet_native.t2 SET c12=REPEAT('ab',@max_allowed_packet),c22='ab';
行为 READ Table(1:14~1:36) /test/1/catalog1/split_packet_native/t1/
行为 READ Table(1:37~1:59) /test/1/catalog1/split_packet_native/t2/
行为 CALL Function(1:68~1:74) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:80~1:99) /test/1/max_allowed_packet/
------
SQL  UPDATE split_packet_native.t3 SET c32=CONCAT(c32,REPEAT('a',@max_allowed_packet-1));
行为 UPDATE Table(1:7~1:29) /test/1/catalog1/split_packet_native/t3/
行为 CALL Function(1:38~1:44) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(1:49~1:55) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:60~1:79) /test/1/max_allowed_packet/
------
SQL  UPDATE t3 SET a4={d '1789-07-14'} WHERE a1=0;
行为 UPDATE Table(1:7~1:9) /test/1/catalog1/schema1/t3/
------
SQL  UPDATE assignment_t SET c1 = 2 WHERE c1 = @assign_where := 1;
行为 UPDATE Table(1:7~1:19) /test/1/catalog1/schema1/assignment_t/
行为 READ ConfigKey(1:42~1:55) /test/1/assign_where/
------
SQL  update test set name = 'abc' where id = 1;
行为 UPDATE Table(1:7~1:11) /test/1/catalog1/schema1/test/
------
SQL  update test set name = 'abc', age = 20 where id = 1;
行为 UPDATE Table(1:7~1:11) /test/1/catalog1/schema1/test/
------
SQL  update test a join test2 b on a.id = b.id set a.name = b.name;
行为 READ Table(1:7~1:11) /test/1/catalog1/schema1/test/
行为 READ Table(1:19~1:24) /test/1/catalog1/schema1/test2/
------
SQL  UPDATE (SELECT * FROM split_derived_80early.t1) AS d SET a=5;
行为 READ Table(1:22~1:46) /test/1/catalog1/split_derived_80early/t1/
------
SQL  UPDATE (SELECT * FROM split_derived_80early.t1) AS d,split_derived_80early.t1 AS b SET d.a=5;
行为 READ Table(1:22~1:46) /test/1/catalog1/split_derived_80early/t1/
------
SQL  UPDATE split_derived_dml57.users,(SELECT 1) AS dummy SET position=(SELECT COUNT(pos)+1 FROM (SELECT DISTINCT position AS pos FROM split_derived_dml57.users) AS t2 WHERE t2.pos<users.position) WHERE id=3;
行为 READ Table(1:7~1:32) /test/1/catalog1/split_derived_dml57/users/
行为 CALL Function(1:74~1:79) /test/1/catalog1/schema1/COUNT/
------
SQL  UPDATE split_derived_dml57.users,(SELECT 1) AS dummy SET position=(SELECT COUNT(pos)+1 FROM (SELECT position AS pos FROM split_derived_dml57.users) AS t2 WHERE t2.pos<users.position) WHERE id=3;
行为 READ Table(1:7~1:32) /test/1/catalog1/split_derived_dml57/users/
行为 CALL Function(1:74~1:79) /test/1/catalog1/schema1/COUNT/
------
SQL  UPDATE split_derived_dml57.tbl1 AS t1 INNER JOIN (SELECT a.id,a.rec_id,b.s_date FROM split_derived_dml57.tbl2 AS b,split_derived_dml57.tbl1 AS a WHERE a.id_value2=b.t_id AND a.id=1 ORDER BY s_date DESC) AS t2 ON t1.id=t2.id AND t1.rec_id=t2.rec_id SET t1.rec_id=@ROWNUM:=@ROWNUM+1;
行为 READ Table(1:7~1:31) /test/1/catalog1/split_derived_dml57/tbl1/
行为 READ Table(1:85~1:109) /test/1/catalog1/split_derived_dml57/tbl2/
行为 READ ConfigKey(1:262~1:269) /test/1/ROWNUM/
------
SQL  UPDATE u1 AS A NATURAL JOIN u2 AS B SET B.a2=1;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/u1/
行为 READ Table(1:28~1:30) /test/1/catalog1/schema1/u2/
------
SQL  UPDATE jt AS a NATURAL JOIN jt AS b SET a.dummy='',b.col_check=NULL;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/jt/
------
SQL  UPDATE t SET b=3 LIMIT 0;
行为 UPDATE Table(1:7~1:8) /test/1/catalog1/schema1/t/
------
SQL  UPDATE /*+ INDEX(t1 i_a) */ split_opt_hints_index.t1 AS t1 SET d=2 WHERE a=1 AND b=2 AND c=3;
行为 UPDATE Table(1:28~1:52) /test/1/catalog1/split_opt_hints_index/t1/
------
SQL  UPDATE /*+ JOIN_ORDER(t2,t1) JOIN_FIXED_ORDER() */ t1 JOIN t2 ON t1.val=t2.val SET t1.f1=t2.f1 WHERE t2.id>100;
行为 READ Table(1:51~1:53) /test/1/catalog1/schema1/t1/
行为 READ Table(1:59~1:61) /test/1/catalog1/schema1/t2/
------
SQL  UPDATE /*+ JOIN_SUFFIX(ta1,t2) */ t1 AS ta1 JOIN t1 AS ta2 ON ta1.f1=ta2.f1 RIGHT JOIN t2 ON ta1.f1=t2.f1 SET ta1.f2=ta1.f2,ta2.f3=ta2.f3 WHERE ('n','r') IN (SELECT f3,f3 FROM t3 WHERE f1<>f2);
行为 READ Table(1:34~1:36) /test/1/catalog1/schema1/t1/
行为 READ Table(1:87~1:89) /test/1/catalog1/schema1/t2/
行为 READ Table(1:176~1:178) /test/1/catalog1/schema1/t3/
------
SQL  UPDATE /*+ NO_RANGE_OPTIMIZATION(t1 PRIMARY) */ split_opt_hints_native.t1 SET c='z' WHERE a BETWEEN 1 AND 2;
行为 UPDATE Table(1:48~1:73) /test/1/catalog1/split_opt_hints_native/t1/
------
SQL  UPDATE /*+ SET_VAR(time_zone='UTC') */ t1 SET f1=TIMEDIFF(NOW(), UTC_TIMESTAMP()) WHERE id=1;
行为 UPDATE Table(1:39~1:41) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:49~1:57) /test/1/catalog1/schema1/TIMEDIFF/
行为 CALL Function(1:58~1:61) /test/1/catalog1/schema1/NOW/
行为 CALL Function(1:65~1:78) /test/1/catalog1/schema1/UTC_TIMESTAMP/
行为 CONFIGURE ConfigKey(1:19~1:28) /test/1/time_zone/
------
SQL  UPDATE t SET b=0 ORDER BY (a=@id),b LIMIT 1;
行为 UPDATE Table(1:7~1:8) /test/1/catalog1/schema1/t/
行为 READ ConfigKey(1:29~1:32) /test/1/id/
------
SQL  UPDATE split_subquery_next.update_outer SET i=i+1 WHERE i=(SELECT MAX(i));
行为 UPDATE Table(1:7~1:39) /test/1/catalog1/split_subquery_next/update_outer/
行为 CALL Function(1:66~1:69) /test/1/catalog1/schema1/MAX/
------
SQL  UPDATE split_subquery_next.update_nested SET i=i+(SELECT MAX(i) FROM (SELECT 1 AS i) AS d) WHERE i=(SELECT MAX(i));
行为 UPDATE Table(1:7~1:40) /test/1/catalog1/split_subquery_next/update_nested/
行为 CALL Function(1:57~1:60) /test/1/catalog1/schema1/MAX/
------
SQL  UPDATE split_subquery_next.mt1,split_subquery_next.mt2 SET mt2.name='lenka' WHERE mt2.id IN (SELECT id FROM split_subquery_next.mt1);
行为 READ Table(1:7~1:30) /test/1/catalog1/split_subquery_next/mt1/
行为 READ Table(1:31~1:54) /test/1/catalog1/split_subquery_next/mt2/
------
SQL  UPDATE split_table_derived.t1 AS o,(SELECT 1) AS d SET o.a=o.a*100 WHERE o.b<0 OR o.a IN(SELECT i.a+1 FROM split_table_derived.t2 AS i);
行为 READ Table(1:7~1:29) /test/1/catalog1/split_table_derived/t1/
行为 READ Table(1:107~1:129) /test/1/catalog1/split_table_derived/t2/
------
SQL  UPDATE gis_point_srid SET g=ST_SRID();
行为 UPDATE Table(1:7~1:21) /test/1/catalog1/schema1/gis_point_srid/
行为 CALL Function(1:28~1:35) /test/1/catalog1/schema1/ST_SRID/
------
SQL  UPDATE gis_point_srid SET g=ST_SRID(g,4145,0);
行为 UPDATE Table(1:7~1:21) /test/1/catalog1/schema1/gis_point_srid/
行为 CALL Function(1:28~1:35) /test/1/catalog1/schema1/ST_SRID/
------
SQL  UPDATE split_derived_common.nm AS p1 INNER JOIN (SELECT n FROM split_derived_common.nm GROUP BY n HAVING COUNT(m)>1) AS p2 ON p1.n=p2.n SET p1.m=2;
行为 READ Table(1:7~1:30) /test/1/catalog1/split_derived_common/nm/
行为 CALL Function(1:105~1:110) /test/1/catalog1/schema1/COUNT/
------
SQL  UPDATE split_subquery_bugs_1550.u1,split_subquery_bugs_1550.u2 SET u1.c=0 WHERE u1.c<>(SELECT u3.c FROM split_subquery_bugs_1550.u3 JOIN split_subquery_bugs_1550.u3 AS u3_b ON u3_b.a>u3.a WHERE u3.b<=u3.b XOR u2.pk=3);
行为 READ Table(1:7~1:34) /test/1/catalog1/split_subquery_bugs_1550/u1/
行为 READ Table(1:35~1:62) /test/1/catalog1/split_subquery_bugs_1550/u2/
行为 READ Table(1:104~1:131) /test/1/catalog1/split_subquery_bugs_1550/u3/
------
SQL  update `table` set id = 1 where id = 2 or name = 3 and zz =4;
行为 UPDATE Table(1:7~1:14) /test/1/catalog1/schema1/table/
------
SQL  update table1 set name = 1 where id = cast(id AS SIGNED) order by id desc;
行为 UPDATE Table(1:7~1:13) /test/1/catalog1/schema1/table1/
行为 CALL Function(1:38~1:42) /test/1/catalog1/schema1/cast/
------
SQL  update `table` set id = 1 limit 21;
行为 UPDATE Table(1:7~1:14) /test/1/catalog1/schema1/table/
------
SQL  update ignore `table` set id = 1;
行为 UPDATE Table(1:14~1:21) /test/1/catalog1/schema1/table/
------
SQL  UPDATE LOW_PRIORITY IGNORE ptab PARTITION (p0) AS p SET p.val = DEFAULT WHERE p.id < 10 ORDER BY p.id LIMIT 1;
行为 UPDATE Table(1:27~1:31) /test/1/catalog1/schema1/ptab/
------
SQL  UPDATE LOW_PRIORITY IGNORE t1 JOIN t2 ON t1.id = t2.id SET t1.val = t2.val, t2.name = t1.name WHERE t1.id > 0;
行为 READ Table(1:27~1:29) /test/1/catalog1/schema1/t1/
行为 READ Table(1:35~1:37) /test/1/catalog1/schema1/t2/
------
SQL  UPDATE t1, (SELECT id, val FROM t2) AS d SET t1.val = d.val WHERE t1.id = d.id;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/t1/
行为 READ Table(1:32~1:34) /test/1/catalog1/schema1/t2/
------
SQL  UPDATE articles SET body='changed' WHERE MATCH(title) AGAINST('tutorial' IN NATURAL LANGUAGE MODE);
行为 UPDATE Table(1:7~1:15) /test/1/catalog1/schema1/articles/
------
SQL  UPDATE t1 LEFT JOIN t2 ON t1.c1 = t2.c1 SET t2.c2 = 't2c2-1' WHERE t1.c3 = 10;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/t1/
行为 READ Table(1:20~1:22) /test/1/catalog1/schema1/t2/
------
SQL  UPDATE IGNORE v2 STRAIGHT_JOIN (SELECT 1) AS t0 SET f2 = 400;
行为 READ Table(1:14~1:16) /test/1/catalog1/schema1/v2/
------
SQL  UPDATE IGNORE (SELECT 1) AS t0 STRAIGHT_JOIN v2 SET f2 = 400;
行为 READ Table(1:45~1:47) /test/1/catalog1/schema1/v2/
------
SQL  UPDATE t1 SET token = X'ad';
行为 UPDATE Table(1:7~1:9) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE (SELECT 1 AS a FROM t1 NATURAL JOIN t1 AS t2) AS x, t1 SET t1.e = x.a;
行为 READ Table(1:27~1:29) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE dml_audit.t SET v := 'u01' WHERE id=21;
行为 UPDATE Table(1:7~1:18) /test/1/catalog1/dml_audit/t/
------
SQL  UPDATE t1 SET c1=DATE_ADD(CURDATE(),INTERVAL '1 1:1:1' DAY_SECOND) WHERE c2=CURDATE();
行为 UPDATE Table(1:7~1:9) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:17~1:25) /test/1/catalog1/schema1/DATE_ADD/
行为 CALL Function(1:26~1:33) /test/1/catalog1/schema1/CURDATE/
------
SQL  UPDATE part_t PARTITION(p0,p1) SET c=c+1 WHERE id IN(2,12);
行为 UPDATE Table(1:7~1:13) /test/1/catalog1/schema1/part_t/
------
SQL  UPDATE d4_t1 FORCE INDEX (b) STRAIGHT_JOIN d4_t2 SET d4_t1.b=d4_t1.b+2, d4_t2.b=d4_t1.b+10 WHERE d4_t1.b BETWEEN 3 AND 5 AND d4_t1.a=d4_t2.a+100;
行为 READ Table(1:7~1:12) /test/1/catalog1/schema1/d4_t1/
行为 READ Table(1:43~1:48) /test/1/catalog1/schema1/d4_t2/
------
SQL  UPDATE d5_monitor USE INDEX () SET col = 10 WHERE col = 9;
行为 UPDATE Table(1:7~1:17) /test/1/catalog1/schema1/d5_monitor/
------
SQL  UPDATE d6_t1 PARTITION (p0) AS a JOIN d6_t2 PARTITION (p1) AS b ON a.id = b.id SET a.v = b.v WHERE b.id > 0;
行为 READ Table(1:7~1:12) /test/1/catalog1/schema1/d6_t1/
行为 READ Table(1:38~1:43) /test/1/catalog1/schema1/d6_t2/
------
SQL  UPDATE split_dml_src USE INDEX FOR ORDER BY (idx_v)\n  SET v=v+1 WHERE id>0 ORDER BY v LIMIT 1;
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/split_dml_src/
------
SQL  UPDATE split_dml_src AS s IGNORE INDEX FOR JOIN (idx_v)\n  JOIN split_dml_aux AS a FORCE INDEX FOR JOIN (PRIMARY) ON a.id=s.id\n  SET s.v=a.v WHERE a.id>0;
行为 READ Table(1:7~1:20) /test/1/catalog1/schema1/split_dml_src/
行为 READ Table(2:7~2:20) /test/1/catalog1/schema1/split_dml_aux/
------
SQL  UPDATE t1 SET b = a, c = CAST(a AS SIGNED);
行为 UPDATE Table(1:7~1:9) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:25~1:29) /test/1/catalog1/schema1/CAST/
------
SQL  UPDATE bit_t SET c=CONCAT(b) WHERE a=1;
行为 UPDATE Table(1:7~1:12) /test/1/catalog1/schema1/bit_t/
行为 CALL Function(1:19~1:25) /test/1/catalog1/schema1/CONCAT/
------
SQL  UPDATE bit_values SET b=DEFAULT(b) WHERE b IS NULL OR b=FALSE;
行为 UPDATE Table(1:7~1:17) /test/1/catalog1/schema1/bit_values/
行为 CALL Function(1:24~1:31) /test/1/catalog1/schema1/DEFAULT/
------
SQL  UPDATE time_widths SET t0=t6,t1=t6,t2=t6,t3=t6,t4=t6,t5=t6 WHERE id IN(1,2);
行为 UPDATE Table(1:7~1:18) /test/1/catalog1/schema1/time_widths/
------
SQL  UPDATE time_widths SET t6=-t6 WHERE t6<TIME'00:00:00';
行为 UPDATE Table(1:7~1:18) /test/1/catalog1/schema1/time_widths/
------
SQL  UPDATE time_widths SET t0=t0-INTERVAL 1 SECOND,t3=t3+INTERVAL '1.001' SECOND_MICROSECOND,t6=t6+INTERVAL '1:02:03.000001' HOUR_MICROSECOND WHERE id=1;
行为 UPDATE Table(1:7~1:18) /test/1/catalog1/schema1/time_widths/
------
SQL  UPDATE temporal_auto SET payload=CONCAT(payload,'-updated') WHERE id=1;
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/temporal_auto/
行为 CALL Function(1:33~1:39) /test/1/catalog1/schema1/CONCAT/
------
SQL  UPDATE temporal_auto SET created_at=DEFAULT(created_at),modified_at=DEFAULT WHERE id IN(2,3);
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/temporal_auto/
行为 CALL Function(1:36~1:43) /test/1/catalog1/schema1/DEFAULT/
------
SQL  UPDATE datetime_widths SET dt1=dt6,dt2=ts6,ts1=dt6,ts2=ts6 WHERE id=1;
行为 UPDATE Table(1:7~1:22) /test/1/catalog1/schema1/datetime_widths/
------
SQL  UPDATE str_lifecycle\nSET v=CONCAT(v,'x'), c=RTRIM(c), fixed_binary=CONCAT(fixed_binary,X'00'), variable_binary=CONCAT(variable_binary,X'00')\nWHERE BINARY v = BINARY 'a ';
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/str_lifecycle/
行为 CALL Function(2:6~2:12) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(2:23~2:28) /test/1/catalog1/schema1/RTRIM/
------
SQL  UPDATE lob_family\nSET b=CONCAT(b,X'00'), t=CONCAT(t,' suffix'), mb=COMPRESS(mt), lt=CONCAT_WS(':',tt,t,mt)\nWHERE HEX(tb)='01' OR t LIKE 'b%';
行为 UPDATE Table(1:7~1:17) /test/1/catalog1/schema1/lob_family/
行为 CALL Function(2:6~2:12) /test/1/catalog1/schema1/CONCAT/
行为 CALL Function(2:49~2:57) /test/1/catalog1/schema1/COMPRESS/
行为 CALL Function(2:66~2:75) /test/1/catalog1/schema1/CONCAT_WS/
行为 CALL Function(3:6~3:9) /test/1/catalog1/schema1/HEX/
------
SQL  UPDATE lob_family AS target\nLEFT JOIN lob_family AS source ON target.b=source.b\nSET target.t=source.lt,target.mb=source.lb\nWHERE target.tb=X'01';
行为 READ Table(1:7~1:17) /test/1/catalog1/schema1/lob_family/
------
SQL  UPDATE split_type_enum_set.es_core\n    SET e_basic=2,s_basic=s_basic|4,e_added='archived',s_flags='x,z'\n    WHERE e_basic+0 > 0 AND (s_basic+0 & 1) = 1;
行为 UPDATE Table(1:7~1:34) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  UPDATE spatial_lifecycle\nSET p=ST_SRID(ST_PointFromText('POINT(9 9)'),0),\n    g=ST_Buffer(g,1)\nWHERE ST_Intersects(g,ST_GeomFromText('POINT(1 1)'));
行为 UPDATE Table(1:7~1:24) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(2:6~2:13) /test/1/catalog1/schema1/ST_SRID/
行为 CALL Function(2:14~2:30) /test/1/catalog1/schema1/ST_PointFromText/
行为 CALL Function(3:6~3:15) /test/1/catalog1/schema1/ST_Buffer/
行为 CALL Function(4:6~4:19) /test/1/catalog1/schema1/ST_Intersects/
行为 CALL Function(4:22~4:37) /test/1/catalog1/schema1/ST_GeomFromText/
------
SQL  UPDATE spatial_lifecycle AS a\nJOIN spatial_lifecycle AS b ON a.id=b.id+1\nSET a.g=ST_Union(a.g,b.g),\n    a.p=ST_PointFromText('POINT(8 8)')\nWHERE ST_Distance(a.p,b.p)>=0;
行为 READ Table(1:7~1:24) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(3:8~3:16) /test/1/catalog1/schema1/ST_Union/
行为 CALL Function(4:8~4:24) /test/1/catalog1/schema1/ST_PointFromText/
行为 CALL Function(5:6~5:17) /test/1/catalog1/schema1/ST_Distance/
------
SQL  UPDATE integer_lifecycle\nSET tiny_signed=tiny_signed+1,\n    small_unsigned=small_unsigned|1,\n    medium_signed=medium_signed DIV 2,\n    int_unsigned=int_unsigned+1000,\n    big_signed=-big_signed,\n    bool_alias=NOT bool_alias\nWHERE tiny_unsigned BETWEEN 1 AND 10;
行为 UPDATE Table(1:7~1:24) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  UPDATE integer_lifecycle AS target\nJOIN integer_lifecycle AS source ON target.id=source.id+1\nSET target.tiny_signed=source.tiny_signed,\n    target.small_signed=source.small_signed,\n    target.medium_signed=source.medium_signed,\n    target.int_signed=source.int_signed,\n    target.big_signed=source.big_signed\nWHERE target.big_unsigned>=source.big_unsigned;
行为 READ Table(1:7~1:24) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  UPDATE numeric_lifecycle\nSET decimal_value=ROUND(decimal_value+numeric_value,6),\n    fixed_value=fixed_value*1.10,\n    float_value=float_value/NULLIF(real_value,0),\n    double_value=POW(double_value,2),\n    real_value=SQRT(ABS(real_value))\nWHERE decimal_value BETWEEN 0 AND 1000;
行为 UPDATE Table(1:7~1:24) /test/1/catalog1/schema1/numeric_lifecycle/
行为 CALL Function(2:18~2:23) /test/1/catalog1/schema1/ROUND/
行为 CALL Function(4:28~4:34) /test/1/catalog1/schema1/NULLIF/
行为 CALL Function(5:17~5:20) /test/1/catalog1/schema1/POW/
行为 CALL Function(6:15~6:19) /test/1/catalog1/schema1/SQRT/
行为 CALL Function(6:20~6:23) /test/1/catalog1/schema1/ABS/
------
SQL  UPDATE numeric_lifecycle AS target\nJOIN numeric_lifecycle AS source\n  ON target.id=source.id+1\nSET target.decimal_value=source.numeric_value,\n    target.float_value=source.double_value,\n    target.double_value=source.float_value,\n    target.real_value=source.fixed_value\nWHERE target.decimal_value>=source.fixed_value;
行为 READ Table(1:7~1:24) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  UPDATE split_update_t1 AS a\nINNER JOIN split_update_t2 AS b ON a.i = b.i\nINNER JOIN split_update_t3 AS c ON a.j = c.j AND b.k = c.k\nSET a.x = b.x, a.y = b.y, a.z = (SELECT SUM(z) FROM split_update_t3 WHERE y = 34)\nWHERE b.x = 23;
行为 READ Table(1:7~1:22) /test/1/catalog1/schema1/split_update_t1/
行为 READ Table(2:11~2:26) /test/1/catalog1/schema1/split_update_t2/
行为 READ Table(3:11~3:26) /test/1/catalog1/schema1/split_update_t3/
行为 CALL Function(4:40~4:43) /test/1/catalog1/schema1/SUM/
------
SQL  UPDATE t1=a SET i=0;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE t1 AS A NATURAL JOIN t1 B SET A.pk=5,B.pk=7;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE t1 AS t2 STRAIGHT_JOIN t1 SET t1.c1=t1.c1+1;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE /*+ SET_VAR(sql_safe_updates=1) */ IGNORE v1 SET a=1;
行为 UPDATE Table(1:49~1:51) /test/1/catalog1/schema1/v1/
行为 CONFIGURE ConfigKey(1:19~1:35) /test/1/sql_safe_updates/
------
SQL  UPDATE target_tab SET value_col = app_func(value_col);
行为 UPDATE Table(1:7~1:17) /test/1/catalog1/schema1/target_tab/
行为 CALL Function(1:34~1:42) /test/1/catalog1/schema1/app_func/
------
SQL  UPDATE split_replication_wait_probe SET wait_result=WAIT_FOR_EXECUTED_GTID_SET('',0) WHERE id=1;
行为 UPDATE Table(1:7~1:35) /test/1/catalog1/schema1/split_replication_wait_probe/
行为 CALL Function(1:52~1:78) /test/1/catalog1/schema1/WAIT_FOR_EXECUTED_GTID_SET/
------
SQL  UPDATE split_type_json.json_core\n    SET doc=JSON_SET(\n              JSON_REPLACE(doc,'$.score',JSON_EXTRACT(doc,'$.score')+1),\n              '$.updated',TRUE\n            ),\n        payload=JSON_REMOVE(payload,'$.old'),\n        meta=JSON_INSERT(\n          COALESCE(meta,JSON_OBJECT()),'$.source','update'\n        )\n    WHERE doc->>'$.kind'='person';
行为 UPDATE Table(1:7~1:32) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(2:12~2:20) /test/1/catalog1/schema1/JSON_SET/
行为 CALL Function(3:14~3:26) /test/1/catalog1/schema1/JSON_REPLACE/
行为 CALL Function(3:41~3:53) /test/1/catalog1/schema1/JSON_EXTRACT/
行为 CALL Function(6:16~6:27) /test/1/catalog1/schema1/JSON_REMOVE/
行为 CALL Function(7:13~7:24) /test/1/catalog1/schema1/JSON_INSERT/
行为 CALL Function(8:10~8:18) /test/1/catalog1/schema1/COALESCE/
行为 CALL Function(8:24~8:35) /test/1/catalog1/schema1/JSON_OBJECT/
------
SQL  WITH c AS (SELECT id, val FROM t2) UPDATE t1 JOIN c ON t1.id = c.id SET t1.val = c.val WHERE t1.id > 0;
行为 READ Table(1:31~1:33) /test/1/catalog1/schema1/t2/
行为 READ Table(1:42~1:44) /test/1/catalog1/schema1/t1/
------
SQL  UPDATE split_window_context.t SET a=SUM(b) OVER ();
行为 UPDATE Table(1:7~1:29) /test/1/catalog1/split_window_context/t/
行为 CALL Function(1:36~1:39) /test/1/catalog1/schema1/SUM/
------
SQL  WITH RECURSIVE cte(n) AS (SELECT 21) UPDATE dml_audit.t JOIN cte ON t.id=cte.n SET t.v='u02';
行为 READ Table(1:44~1:55) /test/1/catalog1/dml_audit/t/
------
SQL  UPDATE (VALUES ROW(2)) AS dt(a) JOIN split_native_gap.t_update AS t ON dt.a=t.a SET t.b=dt.a;
行为 READ Table(1:37~1:62) /test/1/catalog1/split_native_gap/t_update/
------
SQL  UPDATE split_window_update.t AS o1 LEFT JOIN split_window_update.t AS o2 ON o1.a=o2.a SET o1.b=0 WHERE o1.a<(SELECT DISTINCT FIRST_VALUE(7) OVER () FROM split_window_update.u WHERE o2.b<=6 ORDER BY x LIMIT 1);
行为 READ Table(1:7~1:28) /test/1/catalog1/split_window_update/t/
行为 CALL Function(1:125~1:136) /test/1/catalog1/schema1/FIRST_VALUE/
行为 READ Table(1:153~1:174) /test/1/catalog1/split_window_update/u/
------
SQL  WITH c AS (SELECT id,v FROM split_dml_src WHERE id<100)\nUPDATE split_dml_dst PARTITION(p0) AS d FORCE INDEX FOR JOIN (idx_v)\n  JOIN c ON c.id=d.id SET d.v=c.v\n  WHERE EXISTS (SELECT 1 FROM split_dml_aux AS a WHERE a.id=c.id);
行为 READ Table(1:28~1:41) /test/1/catalog1/schema1/split_dml_src/
行为 READ Table(2:7~2:20) /test/1/catalog1/schema1/split_dml_dst/
行为 READ Table(4:30~4:43) /test/1/catalog1/schema1/split_dml_aux/
------
SQL  UPDATE t1,JSON_TABLE(t1.c1,'$[*]' COLUMNS(a INT PATH '$.a')) AS jt SET jt.a=1;
行为 READ Table(1:7~1:9) /test/1/catalog1/schema1/t1/
------
SQL  WITH cte AS (SELECT alias2.col_time_key AS field1 FROM E AS alias1 LEFT OUTER JOIN E AS alias2 ON alias1.col_varchar_key=alias2.col_blob_key WHERE alias2.col_varchar_key LIKE 'u' ORDER BY field1 DESC LIMIT 1000) UPDATE E AS OUTR1 JOIN E AS OUTR2 ON OUTR1.col_int=OUTR2.col_int LEFT OUTER JOIN cte AS OUTRcte JOIN cte AS OUTRcte1 ON OUTR1.col_int=OUTRcte.field1 ON OUTR2.col_varchar=OUTRcte1.field1 SET OUTR1.col_varchar_key=0 WHERE OUTRcte.field1 IN (SELECT INNR1.col_varchar AS y FROM E AS INNR1 WHERE OUTRcte1.field1<>3);
行为 READ Table(1:55~1:56) /test/1/catalog1/schema1/E/
------
SQL  update abc set id = @@VERSIOM ;
行为 UPDATE Table(1:7~1:10) /test/1/catalog1/schema1/abc/
行为 READ ConfigKey(1:20~1:29) /test/1/VERSIOM/
------
SQL  UPDATE employees SET salary = 80000 WHERE id = 1;
行为 UPDATE Table(1:7~1:16) /test/1/catalog1/schema1/employees/
------
SQL  UPDATE vector_lifecycle\nSET embedding=TO_VECTOR('[0,0,0]'),\n    embedding2=TO_VECTOR('[0,0]'),\n    note=VECTOR_TO_STRING(embedding)\nWHERE VECTOR_DIM(embedding)=3;
行为 UPDATE Table(1:7~1:23) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(2:14~2:23) /test/1/catalog1/schema1/TO_VECTOR/
行为 CALL Function(4:9~4:25) /test/1/catalog1/schema1/VECTOR_TO_STRING/
行为 CALL Function(5:6~5:16) /test/1/catalog1/schema1/VECTOR_DIM/
------
SQL  UPDATE vector_lifecycle AS a\nJOIN vector_lifecycle AS b ON a.id=b.id+1\nSET a.embedding=b.embedding,\n    a.embedding2=b.embedding2\nWHERE a.embedding<>b.embedding;
行为 READ Table(1:7~1:23) /test/1/catalog1/schema1/vector_lifecycle/
------
SQL  UPDATE vector_assign SET b=a;
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/vector_assign/
------
SQL  UPDATE vector_assign SET a=b;
行为 UPDATE Table(1:7~1:20) /test/1/catalog1/schema1/vector_assign/
------
SQL  EXPLAIN ANALYZE FORMAT=JSON INTO @plan UPDATE t1 SET v = v + 1 WHERE id = 1;
行为 READ ConfigKey(1:33~1:38) /test/1/plan/
行为 UPDATE Table(1:46~1:48) /test/1/catalog1/schema1/t1/

## MERGE

SQL  INSERT INTO func_test_duplicate VALUES(1,13836376518955650385) ON DUPLICATE KEY UPDATE b=GREATEST(b,VALUES(b));
行为 MERGE Table(1:12~1:31) /test/1/catalog1/schema1/func_test_duplicate/
行为 CALL Function(1:89~1:97) /test/1/catalog1/schema1/GREATEST/
行为 CALL Function(1:100~1:106) /test/1/catalog1/schema1/VALUES/
------
SQL  REPLACE INTO fd_dml_values VALUES (DEFAULT,DEFAULT,3);
行为 MERGE Table(1:13~1:26) /test/1/catalog1/schema1/fd_dml_values/
------
SQL  insert into `test_schema`.`table2` (`id`, `b`) values (null, 1) ON DUPLICATE KEY UPDATE b = 1;
行为 MERGE Table(1:12~1:34) /test/1/catalog1/test_schema/table2/
------
SQL  INSERT LOW_PRIORITY IGNORE INTO ptab PARTITION (p0) (id, name, val) VALUES (1, 'a', DEFAULT) ON DUPLICATE KEY UPDATE name = VALUES(name);
行为 MERGE Table(1:32~1:36) /test/1/catalog1/schema1/ptab/
行为 CALL Function(1:124~1:130) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT INTO dml_audit.t SET id := 11, v := 'i02' ON DUPLICATE KEY UPDATE v := 'i02u';
行为 MERGE Table(1:12~1:23) /test/1/catalog1/dml_audit/t/
------
SQL  INSERT INTO base_t VALUES(1,9,'x') ON DUPLICATE KEY UPDATE c=VALUES(c),d=DEFAULT;
行为 MERGE Table(1:12~1:18) /test/1/catalog1/schema1/base_t/
行为 CALL Function(1:61~1:67) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT split_dml_odku1 (f2) VALUES ('test')\n  ON DUPLICATE KEY UPDATE f1 = LAST_INSERT_ID(f1);
行为 MERGE Table(1:7~1:22) /test/1/catalog1/schema1/split_dml_odku1/
行为 CALL Function(2:31~2:45) /test/1/catalog1/schema1/LAST_INSERT_ID/
------
SQL  INSERT INTO temporal_auto(id,payload) VALUES(1,'duplicate') ON DUPLICATE KEY UPDATE created_at=DEFAULT(created_at),modified_at=CURRENT_TIMESTAMP(6),payload=VALUES(payload);
行为 MERGE Table(1:12~1:25) /test/1/catalog1/schema1/temporal_auto/
行为 CALL Function(1:95~1:102) /test/1/catalog1/schema1/DEFAULT/
行为 CALL Function(1:127~1:144) /test/1/catalog1/schema1/CURRENT_TIMESTAMP/
行为 CALL Function(1:156~1:162) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT INTO lob_family(tb,b,mb,lb,tt,t,mt,lt)\nVALUES(X'03',X'41',X'42',X'43','upsert','upsert','upsert','upsert')\nON DUPLICATE KEY UPDATE t=VALUES(t),mt=NULL;
行为 MERGE Table(1:12~1:22) /test/1/catalog1/schema1/lob_family/
行为 CALL Function(3:26~3:32) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT INTO split_type_enum_set.es_core(id,e_basic,s_basic)\n    VALUES (1,'two words','green')\n    ON DUPLICATE KEY UPDATE e_basic=VALUES(e_basic),s_basic=VALUES(s_basic);
行为 MERGE Table(1:12~1:39) /test/1/catalog1/split_type_enum_set/es_core/
行为 CALL Function(3:36~3:42) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT INTO integer_lifecycle\n  (tiny_signed,tiny_unsigned,small_signed,small_unsigned,medium_signed,medium_unsigned,\n   int_signed,int_unsigned,big_signed,big_unsigned,note)\nVALUES (-9,9,-18,18,-27,27,-36,101,-72,72,'upsert')\nON DUPLICATE KEY UPDATE\n  tiny_signed=VALUES(tiny_signed),\n  medium_unsigned=VALUES(medium_unsigned),\n  big_unsigned=VALUES(big_unsigned),\n  note=VALUES(note);
行为 MERGE Table(1:12~1:29) /test/1/catalog1/schema1/integer_lifecycle/
行为 CALL Function(6:14~6:20) /test/1/catalog1/schema1/VALUES/
------
SQL  INSERT INTO numeric_lifecycle\n  (id,decimal_value,numeric_value,fixed_value,float_value,float_scale,\n   double_value,real_value,note)\nVALUES (1,100.125,101.25,102.5,1.03e2,104.75,1.05e2,106.5,'upsert')\nON DUPLICATE KEY UPDATE\n  decimal_value=VALUES(decimal_value),\n  float_value=VALUES(float_value),\n  double_value=VALUES(double_value),\n  note=VALUES(note);
行为 MERGE Table(1:12~1:29) /test/1/catalog1/schema1/numeric_lifecycle/
行为 CALL Function(6:16~6:22) /test/1/catalog1/schema1/VALUES/
------
SQL  REPLACE INTO split_packet_native.t1 VALUES (102,REPEAT('ab',@max_allowed_packet));
行为 MERGE Table(1:13~1:35) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:48~1:54) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:60~1:79) /test/1/max_allowed_packet/
------
SQL  REPLACE INTO split_packet_native.t1 SET c11=102,c12=REPEAT('ab',@max_allowed_packet);
行为 MERGE Table(1:13~1:35) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:52~1:58) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:64~1:83) /test/1/max_allowed_packet/
------
SQL  REPLACE INTO split_packet_native.t1 SELECT 102,REPEAT('ab',@max_allowed_packet);
行为 MERGE Table(1:13~1:35) /test/1/catalog1/split_packet_native/t1/
行为 CALL Function(1:47~1:53) /test/1/catalog1/schema1/REPEAT/
行为 READ ConfigKey(1:59~1:78) /test/1/max_allowed_packet/
------
SQL  replace into test (id, name) values (1, 'abc');
行为 MERGE Table(1:13~1:17) /test/1/catalog1/schema1/test/
------
SQL  replace into test set id = 1, name = 'abc';
行为 MERGE Table(1:13~1:17) /test/1/catalog1/schema1/test/
------
SQL  INSERT /*+ SET_VAR(auto_increment_increment=10) SET_VAR(auto_increment_offset=5) */ INTO t1 VALUES (3,'c') ON DUPLICATE KEY UPDATE f1=VALUES(f1);
行为 MERGE Table(1:89~1:91) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:134~1:140) /test/1/catalog1/schema1/VALUES/
行为 CONFIGURE ConfigKey(1:19~1:43) /test/1/auto_increment_increment/
行为 CONFIGURE ConfigKey(1:56~1:77) /test/1/auto_increment_offset/
------
SQL  INSERT /*+ SET_VAR(foreign_key_checks=OFF) */ INTO t1 VALUES (4,'d') ON DUPLICATE KEY UPDATE f1=VALUES(f1);
行为 MERGE Table(1:51~1:53) /test/1/catalog1/schema1/t1/
行为 CALL Function(1:96~1:102) /test/1/catalog1/schema1/VALUES/
行为 CONFIGURE ConfigKey(1:19~1:37) /test/1/foreign_key_checks/
------
SQL  REPLACE /*+ SET_VAR(unique_checks=OFF) */ INTO t1 VALUES (5,'e');
行为 MERGE Table(1:47~1:49) /test/1/catalog1/schema1/t1/
行为 CONFIGURE ConfigKey(1:20~1:33) /test/1/unique_checks/
------
SQL  INSERT INTO t(a,b) SELECT 20000,'x' UNION SELECT 20001,'y' ON DUPLICATE KEY UPDATE b='updated';
行为 MERGE Table(1:12~1:13) /test/1/catalog1/schema1/t/
------
SQL  REPLACE LOW_PRIORITY INTO ptab PARTITION (p0) (id, name, val) VALUES (2, 'replace', DEFAULT);
行为 MERGE Table(1:26~1:30) /test/1/catalog1/schema1/ptab/
------
SQL  REPLACE DELAYED INTO myisam_t SET id = 2, name = 'replace-delayed', val = DEFAULT;
行为 MERGE Table(1:21~1:29) /test/1/catalog1/schema1/myisam_t/
------
SQL  REPLACE DELAYED myisam_t VALUES (12, 'replace-no-into', DEFAULT);
行为 MERGE Table(1:16~1:24) /test/1/catalog1/schema1/myisam_t/
------
SQL  REPLACE INTO dml_audit.defaults_t () SELECT 1,'x';
行为 MERGE Table(1:13~1:33) /test/1/catalog1/dml_audit/defaults_t/
------
SQL  REPLACE INTO dml_audit.defaults_t () VALUES ();
行为 MERGE Table(1:13~1:33) /test/1/catalog1/dml_audit/defaults_t/
------
SQL  REPLACE INTO dml_audit.t SET id := 22, v := 'r03';
行为 MERGE Table(1:13~1:24) /test/1/catalog1/dml_audit/t/
------
SQL  REPLACE INTO dml_audit.t (id,v) VALUE (21,'r01');
行为 MERGE Table(1:13~1:24) /test/1/catalog1/dml_audit/t/
------
SQL  REPLACE INTO part_t PARTITION(p0,p1) VALUES(3,5),(13,6);
行为 MERGE Table(1:13~1:19) /test/1/catalog1/schema1/part_t/
------
SQL  REPLACE INTO bit_values(id,b) VALUES(7,b'10101010');
行为 MERGE Table(1:13~1:23) /test/1/catalog1/schema1/bit_values/
------
SQL  REPLACE INTO time_widths(id,t6) VALUES(8,TIME'-00:00:00.000001');
行为 MERGE Table(1:13~1:24) /test/1/catalog1/schema1/time_widths/
------
SQL  REPLACE INTO temporal_auto(id,created_at,modified_at,explicit_at,payload) VALUES(4,'2010-01-02 03:04:05.123456',DEFAULT,NULL,'replaced');
行为 MERGE Table(1:13~1:26) /test/1/catalog1/schema1/temporal_auto/
------
SQL  REPLACE INTO str_lifecycle(v,c,fixed_binary,variable_binary,t) VALUES('r','r',X'52',X'5200','replace');
行为 MERGE Table(1:13~1:26) /test/1/catalog1/schema1/str_lifecycle/
------
SQL  REPLACE INTO lob_family(tb,b,mb,lb,tt,t,mt,lt)\nVALUES(X'02',X'52',X'53',X'54','replace','replace','replace','replace');
行为 MERGE Table(1:13~1:23) /test/1/catalog1/schema1/lob_family/
------
SQL  REPLACE INTO split_type_enum_set.es_core\n      (id,e_basic,e_case,s_basic,s_case,e_added,s_flags)\n    VALUES (6,'trailing ','a','red,green','A','old','x,y');
行为 MERGE Table(1:13~1:40) /test/1/catalog1/split_type_enum_set/es_core/
------
SQL  REPLACE INTO spatial_lifecycle\n  (id,g,p,ls,pg,mp,mls,mpg,gc,p_changed)\nVALUES\n  (4,\n   ST_GeomFromText('POINT(4 4)'),\n   ST_PointFromText('POINT(4 4)'),\n   ST_LineStringFromText('LINESTRING(4 4,5 5)'),\n   ST_PolygonFromText('POLYGON((0 0,0 4,4 4,0 0))'),\n   ST_MultiPointFromText('MULTIPOINT(4 4,5 5)'),\n   ST_MultiLineStringFromText('MULTILINESTRING((4 4,5 5))'),\n   ST_MultiPolygonFromText('MULTIPOLYGON(((0 0,0 4,4 4,0 0)))'),\n   ST_GeometryCollectionFromText('GEOMETRYCOLLECTION(POINT(4 4))'),\n   ST_LineStringFromText('LINESTRING(4 4,6 6)'));
行为 MERGE Table(1:13~1:30) /test/1/catalog1/schema1/spatial_lifecycle/
行为 CALL Function(5:3~5:18) /test/1/catalog1/schema1/ST_GeomFromText/
行为 CALL Function(6:3~6:19) /test/1/catalog1/schema1/ST_PointFromText/
行为 CALL Function(7:3~7:24) /test/1/catalog1/schema1/ST_LineStringFromText/
行为 CALL Function(8:3~8:21) /test/1/catalog1/schema1/ST_PolygonFromText/
行为 CALL Function(9:3~9:24) /test/1/catalog1/schema1/ST_MultiPointFromText/
行为 CALL Function(10:3~10:29) /test/1/catalog1/schema1/ST_MultiLineStringFromText/
行为 CALL Function(11:3~11:26) /test/1/catalog1/schema1/ST_MultiPolygonFromText/
行为 CALL Function(12:3~12:32) /test/1/catalog1/schema1/ST_GeometryCollectionFromText/
------
SQL  REPLACE INTO integer_lifecycle\n  (id,tiny_signed,tiny_unsigned,small_signed,small_unsigned,medium_signed,medium_unsigned,\n   int_signed,int_unsigned,big_signed,big_unsigned,note)\nVALUES (20,-1,1,-2,2,-3,3,-4,120,-5,5,'replace');
行为 MERGE Table(1:13~1:30) /test/1/catalog1/schema1/integer_lifecycle/
------
SQL  REPLACE INTO numeric_lifecycle\n  (id,decimal_value,numeric_value,fixed_value,float_value,float_scale,\n   double_value,real_value,note)\nVALUES (20,1.125,2.25,3.5,4.75e-5,5.875,6.125e25,7.5,'replace');
行为 MERGE Table(1:13~1:30) /test/1/catalog1/schema1/numeric_lifecycle/
------
SQL  REPLACE /*+ MAX_EXECUTION_TIME(1) */ INTO t2 SELECT 1;
行为 MERGE Table(1:42~1:44) /test/1/catalog1/schema1/t2/
------
SQL  INSERT INTO split_type_json.json_core(id,doc,payload)\n    VALUES (1,'{"name":"Ada","kind":"person","score":11}',\n            '{"tags":["upsert"]}')\n    ON DUPLICATE KEY UPDATE\n      doc=JSON_MERGE_PATCH(doc,VALUES(doc)),\n      payload=JSON_ARRAY_APPEND(payload,'$.tags','upsert');
行为 MERGE Table(1:12~1:37) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(5:10~5:26) /test/1/catalog1/schema1/JSON_MERGE_PATCH/
行为 CALL Function(5:31~5:37) /test/1/catalog1/schema1/VALUES/
行为 CALL Function(6:14~6:31) /test/1/catalog1/schema1/JSON_ARRAY_APPEND/
------
SQL  REPLACE INTO target_t SELECT 1 WHERE 1;
行为 MERGE Table(1:13~1:21) /test/1/catalog1/schema1/target_t/
------
SQL  REPLACE INTO split_type_json.json_core(id,doc,payload,meta)\n    VALUES (4,'{"name":"Mia","kind":"person","score":41}',\n            '{"tags":["replace"]}',JSON_OBJECT('replaced',TRUE));
行为 MERGE Table(1:13~1:38) /test/1/catalog1/split_type_json/json_core/
行为 CALL Function(3:35~3:46) /test/1/catalog1/schema1/JSON_OBJECT/
------
SQL  insert into target_tab(id, value_col) values (1, 'new') on duplicate key update value_col = values(value_col)
行为 MERGE Table(1:12~1:22) /test/1/catalog1/schema1/target_tab/
行为 CALL Function(1:92~1:98) /test/1/catalog1/schema1/values/
------
SQL  replace into target_tab(id, value_col) values (1, 'replacement')
行为 MERGE Table(1:13~1:23) /test/1/catalog1/schema1/target_tab/
------
SQL  INSERT INTO ptab PARTITION (p0) (id, name, val) VALUES (5, 'alias', 50) AS new(nid, nname, nval) ON DUPLICATE KEY UPDATE name = new.nname, val = new.nval;
行为 MERGE Table(1:12~1:16) /test/1/catalog1/schema1/ptab/
------
SQL  INSERT INTO ptab PARTITION (p0) SET id = 6, name = 'set-alias', val = 60 AS new ON DUPLICATE KEY UPDATE name = new.name;
行为 MERGE Table(1:12~1:16) /test/1/catalog1/schema1/ptab/
------
SQL  INSERT INTO t1 VALUES() AS f2 ON DUPLICATE KEY UPDATE f1 = 1;
行为 MERGE Table(1:12~1:14) /test/1/catalog1/schema1/t1/
------
SQL  INSERT INTO dml_audit.t (id,v) VALUES ROW(13,'i05') ON DUPLICATE KEY UPDATE v='i05u';
行为 MERGE Table(1:12~1:23) /test/1/catalog1/dml_audit/t/
------
SQL  INSERT INTO split_dml_dst(id,v)\n  VALUES (11,10),(12,20) AS new(nid,nv)\n  ON DUPLICATE KEY UPDATE v=new.nv;
行为 MERGE Table(1:12~1:25) /test/1/catalog1/schema1/split_dml_dst/
------
SQL  INSERT INTO t0 SET a=1,b=20 AS n(na,nb) ON DUPLICATE KEY UPDATE b=n.nb;
行为 MERGE Table(1:12~1:14) /test/1/catalog1/schema1/t0/
------
SQL  INSERT INTO t0 VALUES (1,5),(2,7) AS t0 ON DUPLICATE KEY UPDATE b=t0.a;
行为 MERGE Table(1:12~1:14) /test/1/catalog1/schema1/t0/
------
SQL  INSERT INTO t0 VALUES (1,5),(2,7) AS n(a,a) ON DUPLICATE KEY UPDATE b=n.a;
行为 MERGE Table(1:12~1:14) /test/1/catalog1/schema1/t0/
------
SQL  INSERT INTO t0 VALUES (1,5),(2,7) AS n(a) ON DUPLICATE KEY UPDATE b=n.a;
行为 MERGE Table(1:12~1:14) /test/1/catalog1/schema1/t0/
------
SQL  REPLACE INTO split_select_short.distinct_t (WITH RECURSIVE a(i) AS (SELECT 0 UNION ALL SELECT i+1 FROM a WHERE i<2) SELECT 'b','a','a','a211','xy1','' FROM a);
行为 MERGE Table(1:13~1:42) /test/1/catalog1/split_select_short/distinct_t/
------
SQL  REPLACE INTO dml_audit.defaults_t VALUES ROW();
行为 MERGE Table(1:13~1:33) /test/1/catalog1/dml_audit/defaults_t/
------
SQL  REPLACE INTO target VALUES ROW(101, 'rt');
行为 MERGE Table(1:13~1:19) /test/1/catalog1/schema1/target/
------
SQL  INSERT INTO t(id, name, val) VALUES(1, 'dup', 99) AS new ON DUPLICATE KEY UPDATE name = new.name, val = new.val;
行为 MERGE Table(1:12~1:13) /test/1/catalog1/schema1/t/
------
SQL  REPLACE INTO t(id, name, val) SELECT 21, 'replaced', 2;
行为 MERGE Table(1:13~1:14) /test/1/catalog1/schema1/t/
------
SQL  INSERT INTO vector_lifecycle\n  (id,grp,embedding,embedding_default,embedding2,note)\nVALUES\n  (1,9,TO_VECTOR('[9,9,9]'),TO_VECTOR('[8,8,8]'),TO_VECTOR('[9,9]'),'upsert')\nON DUPLICATE KEY UPDATE\n  embedding=VALUES(embedding),\n  embedding_default=VALUES(embedding_default),\n  note=VALUES(note);
行为 MERGE Table(1:12~1:28) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(4:7~4:16) /test/1/catalog1/schema1/TO_VECTOR/
行为 CALL Function(6:12~6:18) /test/1/catalog1/schema1/VALUES/
------
SQL  REPLACE INTO vector_lifecycle\n  (id,grp,embedding,embedding_default,embedding2,note)\nVALUES\n  (4,2,TO_VECTOR('[4,5,6]'),TO_VECTOR('[6,5,4]'),TO_VECTOR('[4,5]'),'replace');
行为 MERGE Table(1:13~1:29) /test/1/catalog1/schema1/vector_lifecycle/
行为 CALL Function(4:7~4:16) /test/1/catalog1/schema1/TO_VECTOR/
------
SQL  SET k1 v1\nGET k1
行为 MERGE Key(1:4~1:6) /test/1/0/k1/

## DATA_IMPORT

SQL  LOAD DATA LOCAL INFILE '' REPLACE INTO TABLE v1;
行为 IMPORT Table(1:45~1:47) /test/1/catalog1/schema1/v1/
------
SQL  CLONE INSTANCE FROM 'split_clone'@'127.0.0.1':9 IDENTIFIED BY 'pw';
行为 ADMIN Instance(1:0~1:66) /test/1/
------
SQL  CLONE INSTANCE FROM 'split_clone'@'127.0.0.1':9 IDENTIFIED BY 'pw' REQUIRE NO SSL;
行为 ADMIN Instance(1:0~1:81) /test/1/
------
SQL  CLONE INSTANCE FROM 'split_clone'@'127.0.0.1':9 IDENTIFIED BY 'pw' DATA DIRECTORY = '/tmp/split_clone_remote';
行为 ADMIN Instance(1:0~1:109) /test/1/
------
SQL  CLONE INSTANCE FROM 'split_clone'@'127.0.0.1':9 IDENTIFIED BY 'pw' DATA DIRECTORY '/tmp/split_clone_remote_noeq' REQUIRE NO SSL;
行为 ADMIN Instance(1:0~1:127) /test/1/
------
SQL  CLONE INSTANCE FROM 'split_clone'@'127.0.0.1':9 IDENTIFIED BY 'pw' DATA DIRECTORY = '/tmp/split_clone_remote_ssl' REQUIRE SSL;
行为 ADMIN Instance(1:0~1:125) /test/1/
------
SQL  IMPORT TABLE FROM '/var/lib/mysql-files/no_such_single.sdi';
行为 IMPORT File(1:0~1:59) /test/1/
------
SQL  IMPORT TABLE FROM '/var/lib/mysql-files/no_such_a.sdi', '/var/lib/mysql-files/no_such_b.sdi';
行为 IMPORT File(1:0~1:92) /test/1/
------
SQL  IMPORT TABLE FROM '/var/lib/mysql-files/no_such_*.sdi';
行为 IMPORT File(1:0~1:54) /test/1/

## SWITCH_SCHEMA

SQL  USE splitv56;
行为 SWITCH Schema(1:4~1:12) /test/1/catalog1/splitv56/
------
SQL  USE `split-v56`;
行为 SWITCH Schema(1:4~1:15) /test/1/catalog1/split-v56/
------
SQL  USE `select56`;
行为 SWITCH Schema(1:4~1:14) /test/1/catalog1/select56/
------
SQL  USE `split space 56`;
行为 SWITCH Schema(1:4~1:20) /test/1/catalog1/split space 56/
------
SQL  USE split_types;
行为 SWITCH Schema(1:4~1:15) /test/1/catalog1/split_types/
------
SQL  USE splitv57;
行为 SWITCH Schema(1:4~1:12) /test/1/catalog1/splitv57/
------
SQL  USE `split-v57`;
行为 SWITCH Schema(1:4~1:15) /test/1/catalog1/split-v57/
------
SQL  USE `select57`;
行为 SWITCH Schema(1:4~1:14) /test/1/catalog1/select57/
------
SQL  USE `split space 57`;
行为 SWITCH Schema(1:4~1:20) /test/1/catalog1/split space 57/
------
SQL  USE splitv80;
行为 SWITCH Schema(1:4~1:12) /test/1/catalog1/splitv80/
------
SQL  USE `split-v80`;
行为 SWITCH Schema(1:4~1:15) /test/1/catalog1/split-v80/
------
SQL  USE `select80`;
行为 SWITCH Schema(1:4~1:14) /test/1/catalog1/select80/
------
SQL  USE `split space 80`;
行为 SWITCH Schema(1:4~1:20) /test/1/catalog1/split space 80/
------
SQL  USE splitv84;
行为 SWITCH Schema(1:4~1:12) /test/1/catalog1/splitv84/
------
SQL  USE `split-v84`;
行为 SWITCH Schema(1:4~1:15) /test/1/catalog1/split-v84/
------
SQL  USE `select84`;
行为 SWITCH Schema(1:4~1:14) /test/1/catalog1/select84/
------
SQL  USE `split space 84`;
行为 SWITCH Schema(1:4~1:20) /test/1/catalog1/split space 84/
------
SQL  USE split84;
行为 SWITCH Schema(1:4~1:11) /test/1/catalog1/split84/
------
SQL  USE splitv97;
行为 SWITCH Schema(1:4~1:12) /test/1/catalog1/splitv97/
------
SQL  USE `split-v97`;
行为 SWITCH Schema(1:4~1:15) /test/1/catalog1/split-v97/
------
SQL  USE `select97`;
行为 SWITCH Schema(1:4~1:14) /test/1/catalog1/select97/
------
SQL  USE `split space 97`;
行为 SWITCH Schema(1:4~1:20) /test/1/catalog1/split space 97/

## CREATE_ROLE

SQL  CREATE ROLE PUBLIC;
行为 CREATE Role(1:12~1:18) /test/1/PUBLIC/
------
SQL  CREATE ROLE IF NOT EXISTS `event`, `file`, `none`, `process`, `proxy`, `reload`, `replication`, `resource`, `super`, `execute`, `restart`, `shutdown`;
行为 CREATE Role(1:26~1:33) /test/1/event/
行为 CREATE Role(1:35~1:41) /test/1/file/
行为 CREATE Role(1:43~1:49) /test/1/none/
行为 CREATE Role(1:51~1:60) /test/1/process/
行为 CREATE Role(1:62~1:69) /test/1/proxy/
行为 CREATE Role(1:71~1:79) /test/1/reload/
行为 CREATE Role(1:81~1:94) /test/1/replication/
行为 CREATE Role(1:96~1:106) /test/1/resource/
行为 CREATE Role(1:108~1:115) /test/1/super/
行为 CREATE Role(1:117~1:126) /test/1/execute/
行为 CREATE Role(1:128~1:137) /test/1/restart/
行为 CREATE Role(1:139~1:149) /test/1/shutdown/
------
SQL  CREATE ROLE 'split_role_80_a','split_role_80_b','split_role_80_c';
行为 CREATE Role(1:12~1:29) /test/1/split_role_80_a/
行为 CREATE Role(1:30~1:47) /test/1/split_role_80_b/
行为 CREATE Role(1:48~1:65) /test/1/split_role_80_c/
------
SQL  CREATE ROLE IF NOT EXISTS 'split_role_80_d';
行为 CREATE Role(1:26~1:43) /test/1/split_role_80_d/
------
SQL  create role test_role;
行为 CREATE Role(1:12~1:21) /test/1/test_role/
------
SQL  CREATE ROLE commit;
行为 CREATE Role(1:12~1:18) /test/1/commit/
------
SQL  CREATE ROLE binlog;
行为 CREATE Role(1:12~1:18) /test/1/binlog/
------
SQL  CREATE ROLE files, vector;
行为 CREATE Role(1:12~1:17) /test/1/files/
行为 CREATE Role(1:19~1:25) /test/1/vector/
------
SQL  CREATE ROLE 'ca_role'@'localhost';
行为 CREATE Role(1:12~1:33) /test/1/ca_role@localhost/
------
SQL  create role 'app_read';
行为 CREATE Role(1:12~1:22) /test/1/app_read/
------
SQL  CREATE ROLE external;
行为 CREATE Role(1:12~1:20) /test/1/external/
------
SQL  CREATE ROLE library;
行为 CREATE Role(1:12~1:19) /test/1/library/
------
SQL  CREATE ROLE sets;
行为 CREATE Role(1:12~1:16) /test/1/sets/
------
SQL  CREATE ROLE IF NOT EXISTS 'r84';
行为 CREATE Role(1:26~1:31) /test/1/r84/
------
SQL  CREATE ROLE 'split_role_84_a','split_role_84_b','split_role_84_c';
行为 CREATE Role(1:12~1:29) /test/1/split_role_84_a/
行为 CREATE Role(1:30~1:47) /test/1/split_role_84_b/
行为 CREATE Role(1:48~1:65) /test/1/split_role_84_c/
------
SQL  CREATE ROLE IF NOT EXISTS 'split_role_84_d';
行为 CREATE Role(1:26~1:43) /test/1/split_role_84_d/
------
SQL  CREATE ROLE 'split_role_97_a','split_role_97_b','split_role_97_c';
行为 CREATE Role(1:12~1:29) /test/1/split_role_97_a/
行为 CREATE Role(1:30~1:47) /test/1/split_role_97_b/
行为 CREATE Role(1:48~1:65) /test/1/split_role_97_c/
------
SQL  CREATE ROLE IF NOT EXISTS 'split_role_97_d';
行为 CREATE Role(1:26~1:43) /test/1/split_role_97_d/

## DROP_ROLE

SQL  DROP ROLE PUBLIC;
行为 DROP Role(1:10~1:16) /test/1/PUBLIC/
------
SQL  DROP ROLE 'split_role_80_a','split_role_80_b','split_role_80_c';
行为 DROP Role(1:10~1:27) /test/1/split_role_80_a/
行为 DROP Role(1:28~1:45) /test/1/split_role_80_b/
行为 DROP Role(1:46~1:63) /test/1/split_role_80_c/
------
SQL  DROP ROLE IF EXISTS 'split_role_80_d';
行为 DROP Role(1:20~1:37) /test/1/split_role_80_d/
------
SQL  drop role role1;
行为 DROP Role(1:10~1:15) /test/1/role1/
------
SQL  DROP ROLE IF EXISTS ca_r1,ca_r2;
行为 DROP Role(1:20~1:25) /test/1/ca_r1/
行为 DROP Role(1:26~1:31) /test/1/ca_r2/
------
SQL  drop role 'app_read';
行为 DROP Role(1:10~1:20) /test/1/app_read/
------
SQL  DROP ROLE IF EXISTS 'r84';
行为 DROP Role(1:20~1:25) /test/1/r84/
------
SQL  DROP ROLE 'split_role_84_a','split_role_84_b','split_role_84_c';
行为 DROP Role(1:10~1:27) /test/1/split_role_84_a/
行为 DROP Role(1:28~1:45) /test/1/split_role_84_b/
行为 DROP Role(1:46~1:63) /test/1/split_role_84_c/
------
SQL  DROP ROLE IF EXISTS 'split_role_84_d';
行为 DROP Role(1:20~1:37) /test/1/split_role_84_d/
------
SQL  DROP ROLE 'split_role_97_a','split_role_97_b','split_role_97_c';
行为 DROP Role(1:10~1:27) /test/1/split_role_97_a/
行为 DROP Role(1:28~1:45) /test/1/split_role_97_b/
行为 DROP Role(1:46~1:63) /test/1/split_role_97_c/
------
SQL  DROP ROLE IF EXISTS 'split_role_97_d';
行为 DROP Role(1:20~1:37) /test/1/split_role_97_d/

## CREATE_RESOURCE_GROUP

SQL  CREATE RESOURCE GROUP split_rg_a TYPE = USER VCPU 0 THREAD_PRIORITY = 0 ENABLE;
行为 CREATE ResourceGroup(1:22~1:32) /test/1/catalog1/schema1/split_rg_a/
------
SQL  CREATE RESOURCE GROUP split_rg_b TYPE = USER DISABLE;
行为 CREATE ResourceGroup(1:22~1:32) /test/1/catalog1/schema1/split_rg_b/
------
SQL  CREATE RESOURCE GROUP split_rg_user_mix TYPE = USER VCPU = 0-1,3 THREAD_PRIORITY = 19 ENABLE;
行为 CREATE ResourceGroup(1:22~1:39) /test/1/catalog1/schema1/split_rg_user_mix/
------
SQL  CREATE RESOURCE GROUP split_rg_sys_low TYPE = SYSTEM THREAD_PRIORITY = -20 DISABLE;
行为 CREATE ResourceGroup(1:22~1:38) /test/1/catalog1/schema1/split_rg_sys_low/

## ALTER_RESOURCE_GROUP

SQL  ALTER RESOURCE GROUP split_rg_a THREAD_PRIORITY = 1 ENABLE;
行为 ALTER ResourceGroup(1:21~1:31) /test/1/catalog1/schema1/split_rg_a/
------
SQL  ALTER RESOURCE GROUP split_rg_b VCPU = 0 DISABLE FORCE;
行为 ALTER ResourceGroup(1:21~1:31) /test/1/catalog1/schema1/split_rg_b/
------
SQL  ALTER RESOURCE GROUP split_rg_b ENABLE;
行为 ALTER ResourceGroup(1:21~1:31) /test/1/catalog1/schema1/split_rg_b/
------
SQL  ALTER RESOURCE GROUP split_rg_user_mix VCPU = 3,0-1 THREAD_PRIORITY = 0 ENABLE;
行为 ALTER ResourceGroup(1:21~1:38) /test/1/catalog1/schema1/split_rg_user_mix/
------
SQL  ALTER RESOURCE GROUP split_rg_sys_low VCPU = 0-2 THREAD_PRIORITY = -1 ENABLE;
行为 ALTER ResourceGroup(1:21~1:37) /test/1/catalog1/schema1/split_rg_sys_low/
------
SQL  ALTER RESOURCE GROUP ca_rg FORCE;
行为 ALTER ResourceGroup(1:21~1:26) /test/1/catalog1/schema1/ca_rg/

## DROP_RESOURCE_GROUP

SQL  DROP RESOURCE GROUP split_rg_a FORCE;
行为 DROP ResourceGroup(1:20~1:30) /test/1/catalog1/schema1/split_rg_a/
------
SQL  DROP RESOURCE GROUP split_rg_b;
行为 DROP ResourceGroup(1:20~1:30) /test/1/catalog1/schema1/split_rg_b/
------
SQL  DROP RESOURCE GROUP split_rg_user_mix FORCE;
行为 DROP ResourceGroup(1:20~1:37) /test/1/catalog1/schema1/split_rg_user_mix/
------
SQL  DROP RESOURCE GROUP split_rg_sys_low FORCE;
行为 DROP ResourceGroup(1:20~1:36) /test/1/catalog1/schema1/split_rg_sys_low/

## ADMIN_LOG

SQL  ALTER INSTANCE DISABLE INNODB REDO_LOG;
行为 CONFIGURE ConfigKey(1:0~1:38) /test/1/
------
SQL  ALTER INSTANCE ENABLE INNODB REDO_LOG;
行为 CONFIGURE ConfigKey(1:0~1:37) /test/1/
------
SQL  ALTER INSTANCE ROTATE BINLOG MASTER KEY;
行为 CONFIGURE ConfigKey(1:0~1:39) /test/1/

## CREATE_LIBRARY

SQL  /*!90200 CREATE LIBRARY split_exec_lib LANGUAGE JAVASCRIPT AS $$ export function f(n) { return n + 1; } $$ */;
行为 CREATE Library(1:24~1:38) /test/1/catalog1/schema1/split_exec_lib/
------
SQL  /*!90300 CREATE LIBRARY split_exec_comment_lib COMMENT 'original executable library' LANGUAGE JAVASCRIPT AS $$ export function f(n) { return n; } $$ */;
行为 CREATE Library(1:24~1:46) /test/1/catalog1/schema1/split_exec_comment_lib/
------
SQL  CREATE LIBRARY split97ddl.split_lib_dollar LANGUAGE JAVASCRIPT AS $$\nexport function plain(n) { return n; }\n$$;
行为 CREATE Library(1:15~1:42) /test/1/catalog1/split97ddl/split_lib_dollar/
------
SQL  CREATE LIBRARY IF NOT EXISTS split97ddl.split_lib_comment LANGUAGE JAVASCRIPT COMMENT 'library comment' AS 'export function comment(n) { return n + 1; }';
行为 CREATE Library(1:29~1:57) /test/1/catalog1/split97ddl/split_lib_comment/
------
SQL  CREATE LIBRARY split97ddl.split_lib_wasm LANGUAGE WASM AS 'AGFzbQEAAAA=';
行为 CREATE Library(1:15~1:40) /test/1/catalog1/split97ddl/split_lib_wasm/
------
SQL  CREATE LIBRARY split97ddl.split_lib_hex LANGUAGE JAVASCRIPT AS _utf8mb4 0x6578706f72742066756e6374696f6e2068657828297b72657475726e20313b7d;
行为 CREATE Library(1:15~1:39) /test/1/catalog1/split97ddl/split_lib_hex/
------
SQL  CREATE LIBRARY split97ddl.split_lib_bin LANGUAGE JAVASCRIPT AS _utf8mb4 B'0110010101111000011100000110111101110010011101000010000001100110011101010110111001100011011101000110100101101111011011100010000001100010011010010110111000101000001010010111101101110010011001010111010001110101011100100110111000100000001100100011101101111101';
行为 CREATE Library(1:15~1:39) /test/1/catalog1/split97ddl/split_lib_bin/
------
SQL  CREATE LIBRARY library_with_comment\nCOMMENT "library comment"\nLANGUAGE JAVASCRIPT\nAS " export function f(n) { return n+1 } ";
行为 CREATE Library(1:15~1:35) /test/1/catalog1/schema1/library_with_comment/
------
SQL  CREATE LIBRARY split97ddl.split_lib_wasm_hex LANGUAGE WASM AS 0x7072696E7466282754686973206973205741534D27293B;
行为 CREATE Library(1:15~1:44) /test/1/catalog1/split97ddl/split_lib_wasm_hex/
------
SQL  CREATE LIBRARY split_lib LANGUAGE JAVASCRIPT AS $$export function f() { return 1; }$$
行为 CREATE Library(1:15~1:24) /test/1/catalog1/schema1/split_lib/

## ALTER_LIBRARY

SQL  /*!90300 ALTER LIBRARY split_exec_comment_lib COMMENT 'updated executable library' */;
行为 ALTER Library(1:23~1:45) /test/1/catalog1/schema1/split_exec_comment_lib/
------
SQL  ALTER LIBRARY split97ddl.split_lib_comment COMMENT 'updated comment';
行为 ALTER Library(1:14~1:42) /test/1/catalog1/split97ddl/split_lib_comment/
------
SQL  ALTER LIBRARY split97ddl.split_lib_comment COMMENT '';
行为 ALTER Library(1:14~1:42) /test/1/catalog1/split97ddl/split_lib_comment/
------
SQL  ALTER LIBRARY split_lib COMMENT 'updated'
行为 ALTER Library(1:14~1:23) /test/1/catalog1/schema1/split_lib/

## DROP_LIBRARY

SQL  /*!90200 DROP LIBRARY split_exec_lib */;
行为 DROP Library(1:22~1:36) /test/1/catalog1/schema1/split_exec_lib/
------
SQL  DROP LIBRARY split97ddl.split_lib_dollar;
行为 DROP Library(1:13~1:40) /test/1/catalog1/split97ddl/split_lib_dollar/
------
SQL  DROP LIBRARY split97ddl.split_lib_wasm;
行为 DROP Library(1:13~1:38) /test/1/catalog1/split97ddl/split_lib_wasm/
------
SQL  DROP LIBRARY IF EXISTS split97ddl.split_lib_comment;
行为 DROP Library(1:23~1:51) /test/1/catalog1/split97ddl/split_lib_comment/
------
SQL  DROP LIBRARY IF EXISTS split97ddl.split_lib_missing;
行为 DROP Library(1:23~1:51) /test/1/catalog1/split97ddl/split_lib_missing/
------
SQL  DROP LIBRARY split97ddl.split_lib_hex;
行为 DROP Library(1:13~1:37) /test/1/catalog1/split97ddl/split_lib_hex/
------
SQL  DROP LIBRARY split97ddl.split_lib_bin;
行为 DROP Library(1:13~1:37) /test/1/catalog1/split97ddl/split_lib_bin/
------
SQL  DROP LIBRARY IF EXISTS split_lib
行为 DROP Library(1:23~1:32) /test/1/catalog1/schema1/split_lib/

## DROP_POLICY

SQL  DROP MASKING POLICY IF EXISTS split_accept_mask;
行为 DROP MaskingPolicy(1:30~1:47) /test/1/catalog1/schema1/split_accept_mask/
------
SQL  DROP MASKING POLICY pol_does_not_exist;
行为 DROP MaskingPolicy(1:20~1:38) /test/1/catalog1/schema1/pol_does_not_exist/

## CREATE_POLICY

SQL  CREATE MASKING POLICY IF NOT EXISTS split_accept_mask(pass_column) CASE WHEN CURRENT_ROLE_IN('admin') THEN pass_column ELSE SHA2(pass_column, 256) END;
行为 CREATE MaskingPolicy(1:36~1:53) /test/1/catalog1/schema1/split_accept_mask/
行为 CALL Function(1:77~1:92) /test/1/catalog1/schema1/CURRENT_ROLE_IN/
行为 CALL Function(1:124~1:128) /test/1/catalog1/schema1/SHA2/
------
SQL  CREATE MASKING POLICY mak_pol(i)\nCASE WHEN CURRENT_ROLE_IN('admin')\n     THEN sha2('abc', 224)\n     ELSE i\nEND;
行为 CREATE MaskingPolicy(1:22~1:29) /test/1/catalog1/schema1/mak_pol/
行为 CALL Function(2:10~2:25) /test/1/catalog1/schema1/CURRENT_ROLE_IN/
行为 CALL Function(3:10~3:14) /test/1/catalog1/schema1/sha2/
