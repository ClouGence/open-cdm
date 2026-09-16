# 达梦 DAL 与函数集成验证

## SQL 执行明细

判定口径：CloudDM 能解析并将 SQL 发送到数据库即为通过；数据库返回错误不等于 CloudDM 失败。安全或破坏性操作保留待用户执行状态。

验证状态统一为：通过、CloudDM 失败、未执行、证据不足。通过仅表示已有证据确认 SQL 被解析并发送到数据库，数据库报错保留在执行证据中。历史执行与复测记录均保留，每行反映该次记录，不代表相同 SQL 的最新复测结论。

本表记录数：通过 294；CloudDM 失败 1；未执行 6；证据不足 3。包含重复执行记录，不等同于去重用例数。本次为文档证据整理，未重新执行 SQL。

| 编号 | SQL | 验证状态 | 实际结果/执行证据 | 前提/预期 | 来源/备注 |
| --- | --- | --- | --- | --- | --- |
| 1 | `ALTER DATABASE OPEN;` | 通过 | 当前隔离库已为 OPEN，DM8 返回 `System in open status`；不再出现 JDBC PREPARE 错误。不要在其他状态的数据库上照搬执行。 | — | 页面事务模式：自动 |
| 2 | `EXPLAIN SELECT * FROM SYSDBA.CODEX_INTEGRATION_ORDERS;` | 通过 | 返回 3 行执行计划，而非只显示 0 rows affected。 | — | 页面事务模式：自动 |
| 3 | `EXPLAIN FOR SELECT * FROM SYSDBA.CODEX_INTEGRATION_ORDERS;` | 通过 | 返回执行计划行。 | — | 页面事务模式：自动 |
| 4 | `SAVEPOINT codexsp;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 5 | `ROLLBACK TO SAVEPOINT codexsp;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 6 | `RELEASE SAVEPOINT codexsp;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 7 | `COMMIT;` | 通过 | 执行成功，0 rows affected。 | — | 页面事务模式：手动 |
| 8 | `SELECT 1 FROM DUAL;` | 通过 | 返回 `1`，证明事务完成后查询正常。 | — | 页面事务模式：手动 |
| 9 | `BACKUP DATABASE BACKUPSET '/opt/dmdbms/data/backup/dal_full';` | 通过 | 执行信息：`No local or remote archive log`；目标路径未生成 | 生成专用数据库备份集或明确归档环境错误 | — |
| 10 | `BACKUP TABLE SYSDBA.ORDERS BACKUPSET '/opt/dmdbms/data/backup/dal_orders';` | 未执行 | 未提交；`dal_orders` 已存在，不能覆盖未知备份 | 生成订单表备份集 | — |
| 11 | `BACKUP TABLESPACE MAIN BACKUPSET '/opt/dmdbms/data/backup/dal_main';` | 通过 | 执行信息：`No local or remote archive log`；目标路径未生成 | 生成表空间备份集或明确归档环境错误 | — |
| 12 | `BACKUP ARCHIVE LOG ALL BACKUPSET '/opt/dmdbms/data/backup/dal_archive';` | 通过 | 执行信息：`No local or remote archive log`；目标路径未生成 | 生成归档日志备份集或明确环境错误 | — |
| 13 | `RESTORE TABLE SYSDBA.ORDERS FROM BACKUPSET '/opt/dmdbms/data/backup/dal_orders';` | 未执行 | 未提交；会覆盖表数据，先核实备份归属和可恢复性 | 从备份集还原专用表 | — |
| 14 | `CHECK BACKUPSET '/opt/dmdbms/data/backup/dal_full';` | 通过 | 执行信息：`line 1, column 0, nearby [CHECK] has error: Syntax error` | 返回备份集检查结果或缺集错误 | — |
| 15 | `CONFIGURE;` | 通过 | 执行信息：`Member access [CONFIGURE] unresolved` | 返回配置或明确数据库结果 | — |
| 16 | `CONFIGURE DEFAULT DEVICE TYPE DISK;` | 通过 | 执行信息：`line 1, column 10, nearby [DEFAULT] has error: Syntax error` | 设置默认设备类型或明确服务器错误 | — |
| 17 | `CONFIGURE DEFAULT TRACE CLEAR;` | 未执行 | 未提交；该动作会清除已有 TRACE 记录 | 清理默认 TRACE | — |
| 18 | `STAT ON SYSDBA.ORDERS;` | 通过 | 执行信息：`0 rows affected` | 统计订单表 | — |
| 19 | `STAT 50 ON SYSDBA.ORDERS(ID, STATUS);` | 通过 | 执行信息：`0 rows affected` | 采样统计指定列 | — |
| 20 | `STAT 100 ON INDEX SYSDBA.IDX_ORDERS_STATUS;` | 通过 | 执行信息：`0 rows affected` | 统计状态索引 | — |
| 21 | `CHECKPOINT(30);` | 通过 | 执行信息：`0 rows affected` | 触发检查点 | — |
| 22 | `ALTER DATABASE MOUNT;` | 未执行 | 未提交；当前库为 OPEN，模式切换会中断查询 | 数据库状态改为 MOUNT 或明确状态错误 | — |
| 23 | `ALTER DATABASE OPEN;` | 通过 | 执行信息：`System in open status` | 当前 OPEN 状态返回明确错误，不发生 PREPARE 错误 | — |
| 24 | `ALTER DATABASE ARCHIVELOG;` | 未执行 | 未提交；需先记录归档配置并确定恢复动作 | 调整归档模式或明确状态错误 | — |
| 25 | `ALTER SESSION SET NLS_DATE_FORMAT='YYYY-MM-DD';` | 通过 | 执行信息：`0 rows affected` | 会话日期显示格式变更 | — |
| 26 | `ALTER SYSTEM SET 'SORT_BUF_SIZE'=11 MEMORY;` | 通过 | 执行信息：`0 rows affected`；`V$DM_INI` 中 PARA/SESS 由 10→11、FILE 保持 10；已恢复并复查均为 10 | 内存参数变更并可恢复原值 | — |
| 27 | `SET TIME ZONE '+8:00';` | 通过 | 执行信息：`0 rows affected` | 当前会话时区设置成功 | — |
| 28 | `SET SCHEMA SYSDBA;` | CloudDM 失败 | 页面提示“当前版本不支持切换库或模式命令，请直接到指定库/模式下进行操作”；没有 SQL 执行信息 | 切换到 SYSDBA schema | — |
| 29 | `SP_SET_PARA_VALUE(0, 'SORT_BUF_SIZE', 12);` | 通过 | 执行信息：`0 rows affected`；`V$DM_INI` 中 PARA/SESS 由 10→12、FILE 保持 10；已用同过程设置 10 并复查均为 10 | 修改参数并可恢复原值 | — |
| 30 | `SP_AUDIT_STMT('TABLE', 'NULL', 'ALL');` | 未执行 | 未提交；涉及安全审计配置，需先核实规则及恢复动作 | 修改审计规则 | — |
| 31 | `START TRANSACTION READ ONLY;` | 通过 | 手动模式下两次提交均报 `Try to change property before transaction end`；`backend/logs/alone/alone.log` 的 12:58:46 调用栈为 `DmSession.executeStatement` → `DmdbStatement.execute`，证明语句已进入达梦 JDBC 执行链 | 手动事务启动只读事务 | — |
| 32 | `SAVEPOINT dal_savepoint;` | 通过 | 手动模式执行信息：`0 rows affected` | 创建保存点 | — |
| 33 | `ROLLBACK TO SAVEPOINT dal_savepoint;` | 通过 | 手动模式执行信息：`0 rows affected` | 回滚到保存点 | — |
| 34 | `RELEASE SAVEPOINT dal_savepoint;` | 通过 | 手动模式执行信息：`0 rows affected` | 释放保存点 | — |
| 35 | `COMMIT;` | 通过 | 手动模式执行信息：`0 rows affected` | 提交事务 | — |
| 36 | `LOCK TABLE SYSDBA.ORDERS IN SHARE MODE NOWAIT;` | 通过 | 执行信息：`0 rows affected` | 获得共享锁或明确冲突错误 | — |
| 37 | `EXPLAIN SELECT * FROM SYSDBA.ORDERS;` | 通过 | 结果表 3 行，包含 `ORDERS` 表扫描/索引计划 | 显示计划行，不被当作无结果 DAL | — |
| 38 | `SET TIME ZONE LOCAL;` | 通过 | 执行信息：`0 rows affected` | 会话时区恢复客户端默认 | — |
| 39 | `CHECKPOINT(0);` | 通过 | 执行信息：`0 rows affected` | 检查点命令执行 | — |
| 40 | `STAT ON SYSDBA.ORDERS;` | 通过 | 执行信息：`0 rows affected` | 统计表；混合批次第一条 | — |
| 41 | `EXPLAIN FOR SELECT * FROM SYSDBA.ORDERS;` | 通过 | 结果表 3 行，含 `ORDERS` 计划行 | 返回结构化执行计划 | — |
| 42 | `LOCK TABLE SYSDBA.ORDERS IN SHARE MODE NOWAIT;` | 通过 | 执行信息：`0 rows affected` | 获得共享锁或明确冲突错误 | — |
| 43 | `SELECT 1 FROM DUAL;` | 通过 | 结果表 1 行、值 `1`；重写为 `LIMIT 1000` | 混合批次第二条仍可查询 | — |
| 44 | `CONFIGURE;` | 通过 | 执行信息：`Member access [CONFIGURE] unresolved` | 错误链路应显示具体错误且不阻断下一条 | — |
| 45 | `SELECT 1 FROM DUAL;` | 通过 | 结果表 1 行、值 `1`；重写为 `LIMIT 1000` | 前条错误后同批继续查询 | — |
| 46 | `ALTER SESSION ENABLE PARALLEL DML;` | 通过 | `0 rows affected` | 会话级开启，显示成功执行信息 | — |
| 47 | `ALTER SESSION DISABLE PARALLEL DML;` | 通过 | `0 rows affected` | 恢复关闭，显示成功执行信息 | — |
| 48 | `STAT /*+PARALLEL(2)*/ 30 ON SYSDBA.ORDERS(ID, STATUS);` | 通过 | `0 rows affected` | 专用测试表已有两列 | — |
| 49 | `EXPLAIN FOR SELECT ID, STATUS FROM SYSDBA.ORDERS WHERE ID=1;` | 通过 | 结果集 4 行 | 页面显示计划结果 | — |
| 50 | `SP_RESET_SESSION_PARA_VALUE('JOIN_HASH_SIZE');` | 通过 | `0 rows affected`；辅助读取 `V$DM_INI.SESS_VALUE` 为 `500000` | 当前会话重置并显示成功 | — |
| 51 | `BACKUP TABLE SYSDBA.ORDERS BACKUPSET '/opt/dmdbms/data/backup/clouddm_mgmt_orders_20260916';` | 通过 | `0 rows affected` | 目标路径事前不存在，避免覆盖已有备份 | — |
| 52 | `ALTER SYSTEM SWITCH LOGFILE;` | 通过 | `Operation does not support using PREPARE way` | 官方 REDO/归档管理语句，页面成功执行 | — |
| 53 | `ALTER SYSTEM ARCHIVE LOG CURRENT;` | 通过 | `Operation does not support using PREPARE way` | 同类等价语句，页面成功执行 | — |
| 54 | `SELECT 1 FROM DUAL;` | 通过 | 结果集 1 行、值 `1` | L01/L02 错误及 A06 之后连接仍可用；只作恢复核对，不计管理覆盖 | — |
| 55 | `SELECT CONVERT(VARCHAR(64), DATE '2024-02-29', 101) FROM DUAL;` | 通过 | 结果表 1 行：`02/29/2024` | 美式日期 `02/29/2024` | — |
| 56 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59', 108) FROM DUAL;` | 通过 | 结果表 1 行：`11:59:59` | 12 小时时间 `11:59:59` | — |
| 57 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59', 127) FROM DUAL;` | 通过 | 结果表 1 行：`2024-02-29T23:59:59.000000` | ISO 格式 `2024-02-29T23:59:59.000000` | — |
| 58 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59', NULL) FROM DUAL;` | 通过 | 结果表 1 行：`NULL` | `NULL` | — |
| 59 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 126) FROM DUAL;` | 通过 | 结果表 1 行：`2024-02-29T23:59:59.000000+08:00` | 带时区 ISO 格式 | — |
| 60 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 0) FROM DUAL;` | 通过 | 结果表 1 行：`FEB 29 2024 11:59 PM` | 默认英文日期时间 | — |
| 61 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 102) FROM DUAL;` | 通过 | 结果表 1 行：`2024.02.29` | `2024.02.29` | — |
| 62 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 103) FROM DUAL;` | 通过 | 结果表 1 行：`29/02/2024` | `29/02/2024` | — |
| 63 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 110) FROM DUAL;` | 通过 | 结果表 1 行：`02-29-2024` | `02-29-2024` | — |
| 64 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 111) FROM DUAL;` | 通过 | 结果表 1 行：`2024/02/29` | `2024/02/29` | — |
| 65 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 112) FROM DUAL;` | 通过 | 结果表 1 行：`20240229` | `20240229` | — |
| 66 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59 +08:00', 120) FROM DUAL;` | 通过 | 结果表 1 行：`2024-02-29 23:59:59` | `2024-02-29 23:59:59` | — |
| 67 | `SELECT CONVERT(VARCHAR(64), TIMESTAMP '2024-02-29 23:59:59', 126) FROM DUAL;`（既有 `_1` 用例） | 通过 | 执行信息显示 `Invalid date format`，没有结果表 | 本库 style 126 无时区报错 | — |
| 68 | `SELECT FOUND_ROWS();` | 通过 | 结果表 1 行：`0` | 当前会话上次查询的行数 | — |
| 69 | `SELECT FOUND_ROWS() FROM DUAL;` | 通过 | 结果表 1 行：`0` | 当前会话上次查询的行数 | — |
| 70 | `SELECT COALESCE(FOUND_ROWS(), 0) FROM DUAL;` | 通过 | 结果表 1 行：`0` | 当前会话上次查询的行数 | — |
| 71 | `SELECT CONV('ff', 16, 10) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 72 | `SELECT CONV('1010', 2, 10) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 73 | `SELECT CONV('-10', 10, 16) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 74 | `SELECT CONV(255, 10, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 75 | `SELECT CONV('z', 36, 10) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 76 | `SELECT CONV('10', 10, 36) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 77 | `SELECT CONV(NULL, 10, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 78 | `SELECT CONV('10', NULL, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 79 | `SELECT CONV('10', 10, NULL) FROM DUAL;` | 通过 | 执行信息：`Member access [CONV] unresolved` | 按进制参数完成转换 | — |
| 80 | `SELECT FORMAT(1234.567, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 81 | `SELECT FORMAT(-1234.567, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 82 | `SELECT FORMAT(1234.567, 0) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 83 | `SELECT FORMAT(1234, 4) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 84 | `SELECT FORMAT(0, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 85 | `SELECT FORMAT(NULL, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 86 | `SELECT FORMAT(1234.567, NULL) FROM DUAL;` | 通过 | 执行信息：`Member access [FORMAT] unresolved` | 返回格式化后的数值 | — |
| 87 | `SELECT MID('abcdef', 1) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 88 | `SELECT MID('abcdef', 2, 3) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 89 | `SELECT MID('abcdef', -2, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 90 | `SELECT MID('abcdef', 2, 0) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 91 | `SELECT MID('', 1, 1) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 92 | `SELECT MID(NULL, 1, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 93 | `SELECT MID('abcdef', NULL, 2) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 94 | `SELECT MID('abcdef', 2, NULL) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 95 | `SELECT MID(CAST('abcdef' AS CLOB), 2, 3) FROM DUAL;` | 通过 | 执行信息：`Member access [MID] unresolved` | 按位置返回子串或 NULL | — |
| 96 | `SELECT STRING_TO_ARRAY('a,b,c', ',') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 97 | `SELECT STRING_TO_ARRAY('a::b::c', '::') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 98 | `SELECT STRING_TO_ARRAY('a,b,', ',') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 99 | `SELECT STRING_TO_ARRAY('', ',') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 100 | `SELECT STRING_TO_ARRAY('abc', '') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 101 | `SELECT STRING_TO_ARRAY(NULL, ',') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 102 | `SELECT STRING_TO_ARRAY('a,b', NULL) FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 103 | `SELECT STRING_TO_ARRAY(CAST('a,b' AS CLOB), ',') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 104 | `SELECT STRING_TO_ARRAY('甲,乙,丙', ',') FROM DUAL;` | 通过 | 执行信息：`Member access [STRING_TO_ARRAY] unresolved` | 按分隔符返回数组或 NULL | — |
| 105 | `SELECT STR_TO_DATE('2024-02-29', '%Y-%m-%d') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 106 | `SELECT STR_TO_DATE('20240229', '%Y%m%d') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 107 | `SELECT STR_TO_DATE('2024-02-29 23:59:58', '%Y-%m-%d %H:%i:%s') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 108 | `SELECT STR_TO_DATE('29/02/2024', '%d/%m/%Y') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 109 | `SELECT STR_TO_DATE('11:22:33', '%H:%i:%s') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 110 | `SELECT STR_TO_DATE('', '%Y-%m-%d') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 111 | `SELECT STR_TO_DATE(NULL, '%Y-%m-%d') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 112 | `SELECT STR_TO_DATE('2024-02-29', NULL) FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 113 | `SELECT STR_TO_DATE(CAST('2024-02-29' AS CLOB), '%Y-%m-%d') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按格式返回日期时间或 NULL | — |
| 114 | `SELECT COALESCE(NULL, NULL, 0) FROM DUAL;` | 通过 | 结果表 1 行：`0` | 按空值组合返回非空值或 NULL | — |
| 115 | `SELECT COALESCE(NULL, CAST(-999999999999999999 AS DECIMAL(38,0))) FROM DUAL;` | 通过 | 结果表 1 行：`-999999999999999999` | 按空值组合返回非空值或 NULL | — |
| 116 | `SELECT COALESCE(NULL, TIMESTAMP '9999-12-31 23:59:59.999999') FROM DUAL;` | 通过 | 结果表 1 行：`9999-12-31 23:59:59.999999000` | 按空值组合返回非空值或 NULL | — |
| 117 | `SELECT COALESCE(NULL, CAST(0x00FF AS VARBINARY)) FROM DUAL;` | 通过 | 结果表 1 行：`00FF` | 按空值组合返回非空值或 NULL | — |
| 118 | `SELECT IFNULL(NULLIF(1, 1), 0) FROM DUAL;` | 通过 | 结果表 1 行：`0` | 按空值组合返回非空值或 NULL | — |
| 119 | `SELECT ISNULL(NULLIF('a', 'a'), 'fallback') FROM DUAL;` | 通过 | 结果表 1 行：`fallback` | 按空值组合返回非空值或 NULL | — |
| 120 | `SELECT NVL(NULLIF(DATE '2024-02-29', DATE '2024-02-29'), DATE '0001-01-01') FROM DUAL;` | 通过 | 执行信息：`Unsupported date type.` | 返回 `0001-01-01` 或明确的日期值 | — |
| 121 | `SELECT NULLIF(COALESCE(NULL, 1), NVL(NULL, 1)) FROM DUAL;` | 通过 | 结果表 1 行：`NULL` | 按空值组合返回非空值或 NULL | — |
| 122 | `SELECT NULL_EQU(CAST(NULL AS INT), CAST(NULL AS INT)) FROM DUAL;` | 通过 | 结果表 1 行：`1` | 按空值组合返回非空值或 NULL | — |
| 123 | `SELECT NULL_EQU(CAST(NULL AS TIMESTAMP), CAST(NULL AS TIMESTAMP)) FROM DUAL;` | 通过 | 结果表 1 行：`1` | 按空值组合返回非空值或 NULL | — |
| 124 | `SELECT COALESCE(NULL, IFNULL(NULL, NVL(NULL, 7))) FROM DUAL;` | 通过 | 结果表 1 行：`7` | 按空值组合返回非空值或 NULL | — |
| 125 | `SELECT NVL(CAST(NULL AS BLOB), TO_BLOB(0x00)) FROM DUAL;` | 通过 | 结果表 1 行：`00` | 按空值组合返回非空值或 NULL | — |
| 126 | `SELECT IFNULL(CAST(NULL AS VARBINARY), CAST(0x00 AS VARBINARY)) FROM DUAL;` | 通过 | 结果表 1 行：`00` | 按空值组合返回非空值或 NULL | — |
| 127 | `SELECT ISNULL(CAST(NULL AS TIME), TIME '23:59:59.999999') FROM DUAL;` | 通过 | 结果表 1 行：`23:59:59.000000000` | 按空值组合返回非空值或 NULL | — |
| 128 | `SELECT NULLIF(INTERVAL '0-0' YEAR TO MONTH, INTERVAL '0-0' YEAR TO MONTH) FROM DUAL;` | 通过 | 结果表 1 行：`NULL` | 按空值组合返回非空值或 NULL | — |
| 129 | `SELECT NULLIF(INTERVAL '0 00:00:00' DAY TO SECOND, INTERVAL '1 00:00:00' DAY TO SECOND) FROM DUAL;` | 通过 | 结果表 1 行：`INTERVAL '000000000 00:00:00.000000' DAY(9) TO SECOND(6)` | 按空值组合返回非空值或 NULL | — |
| 130 | `SELECT DATE(DATE '2024-02-29') FROM DUAL;` | 通过 | 执行信息：`Member access [DATE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 131 | `SELECT DATE(TIMESTAMP '2024-02-29 23:59:59.999999') FROM DUAL;` | 通过 | 执行信息：`Member access [DATE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 132 | `SELECT DATE(NULL) FROM DUAL;` | 通过 | 执行信息：`Member access [DATE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 133 | `SELECT AGE(DATE '2000-02-29') FROM DUAL;` | 通过 | 执行信息：`Member access [AGE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 134 | `SELECT AGE(DATE '2024-02-29', DATE '2000-02-29') FROM DUAL;` | 通过 | 执行信息：`Member access [AGE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 135 | `SELECT AGE(TIMESTAMP '2024-02-29 23:59:59', TIMESTAMP '2024-02-29 00:00:00') FROM DUAL;` | 通过 | 执行信息：`Member access [AGE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 136 | `SELECT AGE(NULL, DATE '2000-01-01') FROM DUAL;` | 通过 | 执行信息：`Member access [AGE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 137 | `SELECT DATE(STR_TO_DATE('2024-02-29 23:59:59', '%Y-%m-%d %H:%i:%s')) FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 138 | `SELECT AGE(DATE(STR_TO_DATE('2024-02-29', '%Y-%m-%d')), DATE '2000-01-01') FROM DUAL;` | 通过 | 执行信息：`Member access [STR_TO_DATE] unresolved` | 按日期、年龄或日期解析参数返回结果或 NULL | — |
| 139 | `SELECT OBJECT_ID('DUAL') FROM DUAL;` | 通过 | 结果表 1 行：`NULL` | 对象不存在或参数为空时返回 NULL | — |
| 140 | `SELECT OBJECT_ID('SYSDBA.DUAL', 'TABLE') FROM DUAL;` | 通过 | 结果表 1 行：`NULL` | 对象不存在或参数为空时返回 NULL | — |
| 141 | `SELECT OBJECT_ID(NULL, NULL) FROM DUAL;` | 通过 | 结果表 1 行：`NULL` | 对象不存在或参数为空时返回 NULL | — |
| 142 | `SELECT COALESCE(OBJECT_ID('DUAL'), 0) FROM DUAL;` | 通过 | 结果表 1 行：`0` | 按表达式返回确定值 | — |
| 143 | `SELECT DECODE(OBJECT_ID('DUAL'), NULL, 0, OBJECT_ID('DUAL')) FROM DUAL;` | 通过 | 结果表 1 行：`0` | 按表达式返回确定值 | — |
| 144 | `SELECT FIELD(NULL, NULL, 1, 2, 3) FROM DUAL;` | 通过 | 结果表 1 行：`0` | 按表达式返回确定值 | — |
| 145 | `SELECT IF(LNNVL(NULL = 1), 'unknown', 'known') FROM DUAL;` | 通过 | 结果表 1 行：`unknown` | 按表达式返回确定值 | — |
| 146 | `SELECT WIDTH_BUCKET(COALESCE(NULL, 0), -10, 10, 4) FROM DUAL;` | 通过 | 结果表 1 行：`3` | 按表达式返回确定值 | — |
| 147 | `SELECT ORA_HASH(DM_HASH('nested'), 4294967295, 4294967295) FROM DUAL;` | 通过 | 结果表 1 行：`3447595753` | 按表达式返回确定值 | — |
| 148 | `SELECT ISNUMERIC(TO_CHAR(-123.45)) FROM DUAL;` | 通过 | 结果表 1 行：`1` | 按表达式返回确定值 | — |
| 149 | `SELECT ISDATE(TO_CHAR(DATE '9999-12-31', 'YYYY-MM-DD')) FROM DUAL;` | 通过 | 结果表 1 行：`1` | 按表达式返回确定值 | — |
| 150 | `SELECT LENGTHB(TO_BASE64(0x00FF)) FROM DUAL;` | 通过 | 结果表 1 行：`4` | 按表达式返回确定值 | — |
| 151 | `SELECT COUNT(*) FROM APP.AGG_T;` | 通过 | 1 行：3 | 返回聚合结果 | — |
| 152 | `SELECT COUNT(ALL V) FROM APP.AGG_T;` | 通过 | 1 行：2 | 返回聚合结果 | — |
| 153 | `SELECT COUNT(DISTINCT V) FROM APP.AGG_T;` | 通过 | 1 行：2 | 返回聚合结果 | — |
| 154 | `SELECT AVG(V), AVG(ALL V), AVG(DISTINCT V) FROM APP.AGG_T;` | 通过 | 1 行：15、15、15 | 返回聚合结果 | — |
| 155 | `SELECT SUM(V), SUM(ALL V), SUM(DISTINCT V) FROM APP.AGG_T;` | 通过 | 1 行：30、30、30 | 返回聚合结果 | — |
| 156 | `SELECT MAX(V), MAX(ALL V), MAX(DISTINCT V) FROM APP.AGG_T;` | 通过 | 1 行：20、20、20 | 返回聚合结果 | — |
| 157 | `SELECT MIN(V), MIN(ALL V), MIN(DISTINCT V) FROM APP.AGG_T;` | 通过 | 1 行：10、10、10 | 返回聚合结果 | — |
| 158 | `SELECT VAR_POP(V), VAR_SAMP(V), VARIANCE(V) FROM APP.AGG_T;` | 通过 | 1 行：25、50、50 | 返回聚合结果 | — |
| 159 | `SELECT STDDEV_POP(V), STDDEV_SAMP(V), STDDEV(V) FROM APP.AGG_T;` | 通过 | 1 行：5、7.07106781186548、7.07106781186548 | 返回聚合结果 | — |
| 160 | `SELECT COVAR_POP(V, V2), COVAR_SAMP(V, V2), CORR(V, V2) FROM APP.AGG_T;` | 通过 | 1 行：5、10、1 | 返回聚合结果 | — |
| 161 | `SELECT FIRST_VALUE(V), ANY_VALUE(V) FROM APP.AGG_T;` | 通过 | 1 行：10、10 | 返回聚合结果 | — |
| 162 | `SELECT AREA_MAX(V, 0, 100) FROM APP.AGG_T;` | 通过 | 1 行：20 | 返回聚合结果 | — |
| 163 | `SELECT AREA_MAX(D, DATE '2024-01-01', DATE '2024-12-31') FROM APP.AGG_T;` | 通过 | 结果单元格显示 `Unsupported date type.` | 返回聚合值或明确服务端限制 | — |
| 164 | `SELECT AVG(V) KEEP (DENSE_RANK FIRST ORDER BY V2) FROM APP.AGG_T;` | 通过 | 1 行：10 | 返回聚合结果 | — |
| 165 | `SELECT AVG(ALL V) KEEP (DENSE_RANK LAST ORDER BY V2 DESC) FROM APP.AGG_T;` | 通过 | 1 行：10 | 返回聚合结果 | — |
| 166 | `SELECT MAX(V) KEEP (DENSE_RANK FIRST ORDER BY V2, ID) FROM APP.AGG_T;` | 通过 | 1 行：10 | 返回聚合结果 | — |
| 167 | `SELECT MIN(V) KEEP (DENSE_RANK LAST ORDER BY V2, ID) FROM APP.AGG_T;` | 通过 | 1 行：NULL | 返回聚合结果 | — |
| 168 | `SELECT COUNT(V) KEEP (DENSE_RANK FIRST ORDER BY V2) FROM APP.AGG_T;` | 通过 | 1 行：1 | 返回聚合结果 | — |
| 169 | `SELECT SUM(V) KEEP (DENSE_RANK LAST ORDER BY V2) FROM APP.AGG_T;` | 通过 | 1 行：NULL | 返回聚合结果 | — |
| 170 | `SELECT LISTAGG(TXT) FROM APP.AGG_T;` | 通过 | 1 行：`abc` | 返回聚合结果 | — |
| 171 | `SELECT LISTAGG(TXT, ',') WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 172 | `SELECT LISTAGG(DISTINCT TXT, ',') WITHIN GROUP (ORDER BY TXT) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 173 | `SELECT LISTAGG(TXT, ',' ON OVERFLOW ERROR) WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 174 | `SELECT LISTAGG(TXT, ',' ON OVERFLOW TRUNCATE) WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 175 | `SELECT LISTAGG(TXT, ',' ON OVERFLOW TRUNCATE '...' WITH COUNT) WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 176 | `SELECT LISTAGG(TXT, ',' ON OVERFLOW TRUNCATE '...' WITHOUT COUNT) WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 177 | `SELECT LISTAGG2(TXT) FROM APP.AGG_T;` | 通过 | 1 行：`abc` | 返回聚合结果 | — |
| 178 | `SELECT LISTAGG2(TXT, ',') WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 179 | `SELECT LISTAGG2(DISTINCT TXT, ',') WITHIN GROUP (ORDER BY TXT) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 180 | `SELECT WM_CONCAT(TXT) FROM APP.AGG_T;` | 通过 | 1 行：`a,b,c` | 返回聚合结果 | — |
| 181 | `SELECT WM_CONCAT(TXT \|\| ':' \|\| ID) FROM APP.AGG_T;` | 通过 | 1 行：`a:1,b:2,c:3` | 返回聚合结果 | — |
| 182 | `SELECT CAST(COLLECT(DISTINCT V ORDER BY V) AS APP.NUMBER_TABLE) FROM APP.AGG_T;` | 通过 | 执行信息：`Invalid data type` | 返回聚合值或明确服务端限制 | — |
| 183 | `SELECT MEDIAN(V) FROM APP.AGG_T;` | 通过 | 1 行：15 | 返回聚合结果 | — |
| 184 | `SELECT REGR_COUNT(V, V2), REGR_AVGX(V, V2), REGR_AVGY(V, V2) FROM APP.AGG_T;` | 通过 | 1 行：2、3、15 | 返回聚合结果 | — |
| 185 | `SELECT REGR_SLOPE(V, V2), REGR_INTERCEPT(V, V2), REGR_R2(V, V2) FROM APP.AGG_T;` | 通过 | 1 行：5、0、1 | 返回聚合结果 | — |
| 186 | `SELECT REGR_SXX(V, V2), REGR_SYY(V, V2), REGR_SXY(V, V2) FROM APP.AGG_T;` | 通过 | 1 行：2、50、10 | 返回聚合结果 | — |
| 187 | `SELECT PERCENTILE_CONT(0) WITHIN GROUP (ORDER BY V) FROM APP.AGG_T;` | 通过 | 1 行：10 | 返回聚合结果 | — |
| 188 | `SELECT PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY V) FROM APP.AGG_T;` | 通过 | 1 行：15 | 返回聚合结果 | — |
| 189 | `SELECT PERCENTILE_CONT(1) WITHIN GROUP (ORDER BY V DESC) FROM APP.AGG_T;` | 通过 | 1 行：10 | 返回聚合结果 | — |
| 190 | `SELECT PERCENTILE_DISC(0) WITHIN GROUP (ORDER BY V) FROM APP.AGG_T;` | 通过 | 执行信息：`Unsupported SQL type` | 返回聚合值或明确服务端限制 | — |
| 191 | `SELECT PERCENTILE_DISC(0.5) WITHIN GROUP (ORDER BY V) FROM APP.AGG_T;` | 通过 | 执行信息：`Unsupported SQL type` | 返回聚合值或明确服务端限制 | — |
| 192 | `SELECT PERCENTILE_DISC(1) WITHIN GROUP (ORDER BY V DESC) FROM APP.AGG_T;` | 通过 | 执行信息：`Unsupported SQL type` | 返回聚合值或明确服务端限制 | — |
| 193 | `SELECT BIT_AND(I), BIT_OR(I), BIT_XOR(I) FROM APP.AGG_T;` | 通过 | 执行信息：`Member access [BIT_AND] unresolved` | 返回聚合值或明确服务端限制 | — |
| 194 | `SELECT BIT_AND(B), BIT_OR(B), BIT_XOR(B) FROM APP.AGG_T;` | 通过 | 执行信息：`Member access [BIT_AND] unresolved` | 返回聚合值或明确服务端限制 | — |
| 195 | `SELECT G, COUNT(*), SUM(ALL V), AVG(DISTINCT V) FROM APP.AGG_T GROUP BY G;` | 通过 | 2 行：G1=(2,30,15)，G2=(1,NULL,NULL) | 返回聚合结果 | — |
| 196 | `SELECT G, LISTAGG(TXT, ',') WITHIN GROUP (ORDER BY ID) FROM APP.AGG_T GROUP BY G;` | 通过 | 2 行：G1=`a,b`，G2=`c` | 返回聚合结果 | — |
| 197 | `SELECT G, PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY V) FROM APP.AGG_T GROUP BY G;` | 通过 | 2 行：G1=15，G2=NULL | 返回聚合结果 | — |
| 198 | `SELECT MAX(SUM(V)) FROM APP.AGG_T GROUP BY G;` | 通过 | 1 行：30 | 返回聚合结果 | — |
| 199 | `SELECT MIN(AVG(V)) FROM APP.AGG_T GROUP BY G;` | 通过 | 1 行：15 | 返回聚合结果 | — |
| 200 | `SELECT COALESCE(SUM(V), 0) FROM APP.AGG_T;` | 通过 | 1 行：30 | 返回聚合结果 | — |
| 201 | `SELECT SUM(NVL(V, 0)) FROM APP.AGG_T;` | 通过 | 1 行：30 | 返回聚合结果 | — |
| 202 | `SELECT ID, AVG(V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果 | — |
| 203 | `SELECT ID, CORR(V, V2) OVER (PARTITION BY G ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 204 | `SELECT ID, COUNT(*) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 205 | `SELECT ID, COUNT(DISTINCT V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 206 | `SELECT ID, COVAR_POP(V, V2) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 207 | `SELECT ID, COVAR_SAMP(V, V2) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 208 | `SELECT ID, CUME_DIST() OVER (PARTITION BY G ORDER BY V, ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1.0` | 返回 4 行窗口结果 | — |
| 209 | `SELECT ID, DENSE_RANK() OVER (PARTITION BY G ORDER BY V DESC, ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 210 | `SELECT ID, MAX(V) KEEP (DENSE_RANK FIRST ORDER BY V2, ID) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 211 | `SELECT ID, MIN(V) KEEP (DENSE_RANK LAST ORDER BY V2, ID) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 212 | `SELECT ID, FIRST_VALUE(V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 213 | `SELECT ID, LAST_VALUE(V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 214 | `SELECT ID, LAG(V, 1, 0) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 215 | `SELECT ID, LAG(V, 2, NULL) OVER (PARTITION BY G ORDER BY ID DESC) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 216 | `SELECT ID, LEAD(V, 1, 0) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 217 | `SELECT ID, LEAD(V, 2, NULL) OVER (PARTITION BY G ORDER BY ID DESC) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 218 | `SELECT ID, LISTAGG(TXT, ',') WITHIN GROUP (ORDER BY ID) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `d` | 返回 4 行窗口结果 | — |
| 219 | `SELECT ID, NTH_VALUE(V, 2) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 220 | `SELECT ID, MAX(DISTINCT V) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 221 | `SELECT ID, MIN(DISTINCT V) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 222 | `SELECT ID, NTILE(3) OVER (PARTITION BY G ORDER BY V, ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 223 | `SELECT ID, PERCENT_RANK() OVER (PARTITION BY G ORDER BY V, ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0.0` | 返回 4 行窗口结果 | — |
| 224 | `SELECT ID, PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果 | — |
| 225 | `SELECT ID, PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY V DESC) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果 | — |
| 226 | `SELECT ID, PERCENTILE_DISC(0.25) WITHIN GROUP (ORDER BY V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 227 | `SELECT ID, PERCENTILE_DISC(0.75) WITHIN GROUP (ORDER BY V DESC) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果 | — |
| 228 | `SELECT ID, RANK() OVER (PARTITION BY G ORDER BY V DESC, ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 229 | `SELECT ID, RATIO_TO_REPORT(V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 230 | `SELECT ID, ROW_NUMBER() OVER (PARTITION BY G ORDER BY V DESC, ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果 | — |
| 231 | `SELECT ID, STDDEV(V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 232 | `SELECT ID, STDDEV_POP(V) OVER (PARTITION BY G ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 233 | `SELECT ID, STDDEV_SAMP(V) OVER (PARTITION BY G ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 234 | `SELECT ID, SUM(ALL V) OVER (PARTITION BY G ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果 | — |
| 235 | `SELECT ID, VAR_POP(V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 236 | `SELECT ID, VAR_SAMP(V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 237 | `SELECT ID, VARIANCE(V) OVER (PARTITION BY G ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果 | — |
| 238 | `SELECT ID, WM_CONCAT(TXT) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `d` | 返回 4 行窗口结果 | — |
| 239 | `SELECT ID, MEDIAN(V) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 执行信息：`Order by expression is forbidden` | 返回 4 行窗口结果 | — |
| 240 | `SELECT ID, REGR_SLOPE(V, V2) OVER (PARTITION BY G ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果 | — |
| 241 | `SELECT ID, AVG(ALL V) OVER () FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `26.66666666666666666666666666666666666667` | 返回 4 行窗口结果或明确服务端限制 | — |
| 242 | `SELECT ID, AVG(DISTINCT V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 243 | `SELECT ID, COUNT(ALL V) OVER (ORDER BY ID ROWS UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `3` | 返回 4 行窗口结果或明确服务端限制 | — |
| 244 | `SELECT ID, COUNT(DISTINCT V) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 证据不足 | 页面未出现结果表；执行信息文本未复核 | 返回 4 行窗口结果或明确服务端限制 | 现有记录不足以确认下发，不计通过。 |
| 245 | `SELECT ID, MAX(ALL V) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果或明确服务端限制 | — |
| 246 | `SELECT ID, MIN(ALL V) OVER (PARTITION BY G ORDER BY ID RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果或明确服务端限制 | — |
| 247 | `SELECT ID, SUM(DISTINCT V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 248 | `SELECT ID, STDDEV(DISTINCT V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果或明确服务端限制 | — |
| 249 | `SELECT ID, VARIANCE(DISTINCT V) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `0` | 返回 4 行窗口结果或明确服务端限制 | — |
| 250 | `SELECT ID, AVG(V) KEEP (DENSE_RANK FIRST ORDER BY V2) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 251 | `SELECT ID, AVG(V) KEEP (DENSE_RANK LAST ORDER BY V2) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 252 | `SELECT ID, COUNT(V) KEEP (DENSE_RANK FIRST ORDER BY V2) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果或明确服务端限制 | — |
| 253 | `SELECT ID, COUNT(V) KEEP (DENSE_RANK LAST ORDER BY V2) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `1` | 返回 4 行窗口结果或明确服务端限制 | — |
| 254 | `SELECT ID, SUM(V) KEEP (DENSE_RANK FIRST ORDER BY V2) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 255 | `SELECT ID, SUM(V) KEEP (DENSE_RANK LAST ORDER BY V2) OVER (PARTITION BY G) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 256 | `SELECT ID, FIRST_VALUE(V) IGNORE NULLS OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果或明确服务端限制 | — |
| 257 | `SELECT ID, FIRST_VALUE(V RESPECT NULLS) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果或明确服务端限制 | — |
| 258 | `SELECT ID, LAST_VALUE(V) IGNORE NULLS OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果或明确服务端限制 | — |
| 259 | `SELECT ID, LAST_VALUE(V RESPECT NULLS) OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40.00` | 返回 4 行窗口结果或明确服务端限制 | — |
| 260 | `SELECT ID, LAG(V) IGNORE NULLS OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果或明确服务端限制 | — |
| 261 | `SELECT ID, LAG(V, 2, 0 RESPECT NULLS) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 证据不足 | 页面未出现结果表；执行信息文本未复核 | 返回 4 行窗口结果或明确服务端限制 | 现有记录不足以确认下发，不计通过。 |
| 262 | `SELECT ID, LEAD(V) RESPECT NULLS OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果或明确服务端限制 | — |
| 263 | `SELECT ID, LEAD(V, 2, 0 IGNORE NULLS) OVER (PARTITION BY G ORDER BY ID) FROM APP.ANALYTIC_T;` | 证据不足 | 页面未出现结果表；执行信息文本未复核 | 返回 4 行窗口结果或明确服务端限制 | 现有记录不足以确认下发，不计通过。 |
| 264 | `SELECT ID, NTH_VALUE(V, 2) FROM FIRST IGNORE NULLS OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果或明确服务端限制 | — |
| 265 | `SELECT ID, NTH_VALUE(V, 2) FROM LAST RESPECT NULLS OVER (PARTITION BY G ORDER BY ID ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `NULL` | 返回 4 行窗口结果或明确服务端限制 | — |
| 266 | `SELECT ID, SUM(V) OVER (ORDER BY ID ROWS CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 267 | `SELECT ID, SUM(V) OVER (ORDER BY ID ROWS 2 PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `70` | 返回 4 行窗口结果或明确服务端限制 | — |
| 268 | `SELECT ID, SUM(V) OVER (ORDER BY ID RANGE UNBOUNDED PRECEDING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `80` | 返回 4 行窗口结果或明确服务端限制 | — |
| 269 | `SELECT ID, SUM(V) OVER (ORDER BY ID RANGE CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 270 | `SELECT ID, SUM(V) OVER (ORDER BY ID ROWS BETWEEN CURRENT ROW AND CURRENT ROW) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 271 | `SELECT ID, SUM(V) OVER (ORDER BY ID ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `40` | 返回 4 行窗口结果或明确服务端限制 | — |
| 272 | `SELECT ID, SUM(V) OVER (ORDER BY ID ROWS BETWEEN 2 PRECEDING AND 3 FOLLOWING) FROM APP.ANALYTIC_T;` | 通过 | 结果表 4 行；末行函数值 `70` | 返回 4 行窗口结果或明确服务端限制 | — |
| 273 | `SELECT BANNER FROM V$VERSION;` | 通过 | 5 行：`DM Database Server 64 V8`、`DB Version: 0x7000d`、构建 `03134284368-20250821-288894-20149` 等 | 返回服务器 banner | fixture / 官方分类：V$VERSION 服务器版本 |
| 274 | `SELECT SF_GET_PARA_VALUE(1, 'BUFFER') FROM DUAL;` | 通过 | `2000` | 查询当前 BUFFER | fixture / 官方分类：`_0` 参数整数 |
| 275 | `SELECT SF_GET_PARA_DOUBLE_VALUE(1, 'CKPT_FLUSH_RATE') FROM DUAL;` | 通过 | `5.0` | 查询当前值 | fixture / 官方分类：`_0` 参数浮点 |
| 276 | `SELECT SF_GET_PARA_STRING_VALUE(1, 'TEMP_PATH') FROM DUAL;` | 通过 | `/opt/dmdbms/data/DAMENG` | 查询当前路径 | fixture / 官方分类：`_0` 参数字符串 |
| 277 | `SELECT SF_GET_SESSION_PARA_VALUE('JOIN_HASH_SIZE') FROM DUAL;` | 通过 | `500000` | 查询当前连接参数 | fixture / 官方分类：`_0` 会话参数 |
| 278 | `SELECT SF_GET_CASE_SENSITIVE_FLAG() FROM DUAL;` | 通过 | `1` | 当前库大小写标记 | fixture / 官方分类：`_0` 大小写标记 |
| 279 | `SELECT SF_GET_PAGE_SIZE() FROM DUAL;` | 通过 | `16384` | 页字节数 | fixture / 官方分类：`_0` 页面大小 |
| 280 | `SELECT SF_GET_UNICODE_FLAG() FROM DUAL;` | 通过 | `1` | 当前库字符标记 | fixture / 官方分类：`_0` Unicode 标记 |
| 281 | `SELECT SF_GET_LOGIN_APP() FROM DUAL;` | 通过 | `CloudDM Client` | 当前客户端名 | fixture / 官方分类：`_0` 登录应用 |
| 282 | `SELECT SF_VIEW_EXPIRED('SYSDBA', 'TEST_VIEW') FROM DUAL;` | 通过 | `Invalid view name` | 需先创建 `SYSDBA.TEST_VIEW` | fixture / 官方分类：`_1` 视图有效性 |
| 283 | `SELECT SYNONYMDEF('SYSDBA', 'SYSOBJECTS', 0, 1) FROM DUAL;` | 通过 | `CREATE OR REPLACE PUBLIC SYNONYM "SYSOBJECTS" FOR "SYS"."SYSOBJECTS";` | 返回对象定义文本 | fixture / 官方分类：`_1` 同义词定义 |
| 284 | `SELECT SF_GET_BROADCAST_ADDRESS('15.16.193.6', '255.255.248.0') FROM DUAL;` | 通过 | `15.16.199.255` | 计算地址 | fixture / 官方分类：`_1` 广播地址 |
| 285 | `SELECT SF_GET_RT_ARCH_STATUS() FROM DUAL;` | 通过 | `INVALID` | 只读状态 | fixture / 官方分类：`_1` 归档状态读取 |
| 286 | `SELECT SF_GET_SELF_EP_SEQNO() FROM DUAL;` | 通过 | `0` | 单节点可读取 | fixture / 官方分类：`_1` 集群节点序号读取 |
| 287 | `SELECT USER_USED_SPACE('SYSDBA') FROM DUAL;` | 通过 | `192` | 查询空间用量 | fixture / 官方分类：`_1` 用户空间 |
| 288 | `SELECT DB_USED_SPACE() FROM DUAL;` | 通过 | `21760` | 查询空间用量 | fixture / 官方分类：`_1` 数据库空间 |
| 289 | `SELECT TS_FREE_SPACE('MAIN') FROM DUAL;` | 通过 | `7904` | 查询空间余量 | fixture / 官方分类：`_1` 表空间余量 |
| 290 | `SELECT TO_DATETIME(2023, 2, 2, 5, 5) FROM DUAL;` | 通过 | `2023-02-02 05:05:00.000000000` | 日期构造 | fixture / 官方分类：`_2` 五参数日期 |
| 291 | `SELECT DUMP('Dameng') FROM DUAL;` | 通过 | `Typ=2 Len=6: 68,97,109,101,110,103` | 类型/字节详情 | fixture / 官方分类：`_2` DUMP 十进制 |
| 292 | `SELECT DUMP('Dameng', 16) FROM DUAL;` | 通过 | `Typ=2 Len=6: 44,61,6d,65,6e,67` | 不同进制签名 | fixture / 官方分类：`_2` DUMP 十六进制 |
| 293 | `SELECT GETUTCDATE() FROM DUAL;` | 通过 | `2026-09-16 01:53:24.744259000` | 返回当前 UTC | fixture / 官方分类：`_2` UTC 时间 |
| 294 | `SELECT SF_GET_SQL_DATA_TYPE('INT') FROM DUAL;` | 通过 | `4` | 类型码 | fixture / 官方分类：`_2` SQL 类型码 |
| 295 | `SELECT CURRENT_SCHID() FROM DUAL;` | 通过 | `150994945` | 当前 ID | fixture / 官方分类：`_2` 当前 schema ID |
| 296 | `SELECT SF_GET_TABLE_COUNT('SYSDBA', 'T') FROM DUAL;` | 通过 | `Object does not exist` | 需先准备该函数要求的表 `T` | fixture / 官方分类：`_2` 表计数 |
| 297 | `SELECT SF_INET_EQUAL('192.168.1.1', '192.168.2.1') FROM DUAL;` | 通过 | `0` | 不同地址比较 | fixture / 官方分类：`_2` IP 比较 |
| 298 | `SELECT SF_INET_CONTAIN('145.13.255.1/16', '145.13.176.1/16') FROM DUAL;` | 通过 | `0`，不预设网段包含语义 | 函数真实返回值 | fixture / 官方分类：`_2` CIDR 包含 |
| 299 | `SELECT SF_INET_SORT('192.168.100.2/12') FROM DUAL;` | 通过 | `A192160000000012192168100002` | 返回排序编码 | fixture / 官方分类：`_2` IP 排序键 |
| 300 | `SELECT SESS_CASE_SENSITIVE() FROM DUAL;` | 通过 | `1` | M01 之后检查当前会话 | fixture / 官方分类：`_0` 会话大小写读取 |
| 301 | `ALTER SESSION SET CASE_SENSITIVE=DEFAULT;` | 通过 | Execution Information：`0 rows affected` | 会话使用库默认大小写语义 | fixture / 官方分类：会话管理 |
| 302 | `ALTER SESSION SET NLS_SORT=BINARY;` | 通过 | Execution Information：`0 rows affected` | 会话使用默认二进制排序 | fixture / 官方分类：会话管理 |
| 303 | `ALTER SESSION SET NLS_DATE_LANGUAGE=ENGLISH;` | 通过 | Execution Information：`0 rows affected` | 仅当前会话语言 | fixture / 官方分类：会话管理 |
| 304 | `SELECT 1 FROM DUAL;` | 通过 | 返回 `1` | 连接继续可用 | fixture / 官方分类：错误后查询回归 |

## Purpose

验证 DM8 驱动加载、DAL 命令及本分支新增函数覆盖在 CloudDM 页面中的真实执行，并保留数据库能力、解析器能力和工作台执行方式之间的差异。

## Scope

- 路由：`/#/sql`；数据源 `codex-it-dameng-8`，schema `SYSDBA`。
- DAL 来源：`open-cdm-test/src/test/resources/split/dameng/8/dcl_dameng_dal_combinations_batch_0.txt` 第一段 29 条、
  `dcl_dameng_dal_combinations_recovery_1.txt` 第一段 8 条，以及 `dcl_dameng_dal_combinations_batch_1.txt` 2
  条日志切换。下方逐条区分页面已测与待测。
- 函数计划范围：本文件“函数 SQL 清单”列出的 13 个 fixture 第一段，共 423 条；当前页面进度以逐条明细为准，不能将计划数记作已测数。
- 不覆盖：生产备份恢复、归档切换、真实 DSC/DPC/MPP 集群。

当前页面进度（只计下方逐条表格，不重复计烟测、额外混合批次或复核重试）：

- 类型：DAL；fixture 范围：原 29 + 恢复 8 + 日志切换 2 = 39 条；已在页面尝试并入表：33；CloudDM 解析/下发 PASS：29；CloudDM FAIL：4；下发待确认：0；未入表 / NOT RUN：6
- 类型：函数；fixture 范围：原 423 + 新增 CONVERT style 12 = 435 条；已在页面尝试并入表：406；CloudDM 解析/下发 PASS：406；CloudDM FAIL：0；下发待确认：0；未入表 / NOT RUN：29

本轮验收口径只判断 CloudDM 是否解析并将 SQL 正常发给数据库。收到明确的数据库语法、函数、对象、权限、参数或环境错误仍计“CloudDM
下发 PASS”，保留原始响应；它不表示数据库操作完成。页面拦截、改写异常或 PREPARE 执行策略阻断真实 SQL 下发计 CloudDM
FAIL；错误来源没有证据时列“下发待确认”。[达梦官方社区的
`Member access [CURRENT_SCHEMA] unresolved` 实例](https://eco.dameng.com/community/question/ca18c3d89fdc0f5b90a3f652cd510c11)
可佐证这是数据库错误格式；此处仍以 CloudDM 执行信息中的原始数据库响应为主要下发证据。DMRMAN 专用命令虽可到数据库返回语法错误，其业务工具边界仍单列。静态
split 通过仅证明分类/语法，不证明页面下发。
窗口 fixture `_2` 此前不确定的第 4、21、23、33–48 条已在本轮定向重跑；其余 29 条也已重新提交，48 条均有本轮页面证据。

## Preconditions

- DM8 隔离容器映射至 `127.0.0.1:5236`，使用具备相应权限的测试账号。
- CloudDM 已加载 `Dameng JDBC Driver/8.1.3.140`，驱动状态为可用。
- `SYSDBA.CODEX_INTEGRATION_ORDERS`、`SYSDBA.ORDERS`、`APP.AGG_T`、`APP.ANALYTIC_T` 和 `IDX_ORDERS_STATUS` 已准备。
- 执行前记录 `SORT_BUF_SIZE`、`PWD_POLICY`、SQL 日志及集群配置的原值。

## Test Data

- 编号：D01；数据说明：DAL 订单表；构造方式：创建 `SYSDBA.ORDERS(id,status,amount)` 和状态索引；唯一标识：`ORDERS`、`IDX_ORDERS_STATUS`；清理方式：删除表和索引
- 编号：D02；数据说明：聚合函数数据；构造方式：创建含 NULL、分组、文本、日期、二进制列的数据；唯一标识：`APP.AGG_T`；清理方式：删除表/用户
- 编号：D03；数据说明：分析函数数据；构造方式：创建至少两组、四行的窗口函数数据；唯一标识：`APP.ANALYTIC_T`；清理方式：删除表/用户

### DAMENG-SMOKE-01 驱动与查询（P0）

- Chrome 操作：确认驱动可用，选择 `SYSDBA`，执行 `SELECT 1 FROM DUAL;` 和订单表查询。
- 预期结果：连接成功并显示结果；表树可加载。
- 恢复/清理：关闭结果页签。

### DAMENG-MAIN-01 29 条 DAL（P0）

- Chrome 操作：逐条执行 DAL fixture 第一段 SQL，包含 BACKUP/RESTORE、CHECK/CONFIGURE、STAT、CHECKPOINT、ALTER
  DATABASE/SESSION/SYSTEM、时区、`SP_SET_PARA_VALUE`、审计、事务、LOCK 和 EXPLAIN。事务语句先切换手动事务模式；尚未提交的状态变更与还原命令仍保留在表中标为待测。
- 预期结果：每条语句进入执行信息。无归档日志、无审计权限、对象约束或 DM 不支持语法时返回具体错误；`ALTER DATABASE` 和 EXPLAIN
  使用普通 Statement，不出现 `Operation does not support using PREPARE way`；直接 `EXPLAIN SELECT` 返回计划表，而不是被追加
  LIMIT 或只报告 0 rows affected。
- 恢复/清理：将 `SORT_BUF_SIZE` 恢复到执行前值，本隔离环境基线为 `10`。

### DAMENG-MAIN-02 函数 SQL 全量计划（P0）

- SQL 清单（计划执行第一条分隔线之前的全部 SQL；实际进度以上方表格和逐条记录为准）：
    - `dql_dameng_aggregate_function_signature_combinations_0.txt`：51 条。
    - `dql_dameng_analytic_window_atomic_1.txt`：39 条。
    - `dql_dameng_analytic_window_atomic_2.txt`：48 条。
    - `dql_dameng_conversion_function_signature_combinations_1.txt`：22 条。
    - `dql_dameng_datetime_functions_1.txt`：9 条。
    - `dql_dameng_misc_function_signature_combinations_1.txt`：12 条。
    - `dql_dameng_null_function_signature_combinations_1.txt`：16 条。
    - `dql_dameng_numeric_function_signature_combinations_1.txt`：19 条。
    - `dql_dameng_string_remaining_signature_combinations_1.txt`：27 条。
    - `dql_dameng_system_function_signature_combinations_0.txt`：53 条。
    - `dql_dameng_system_function_signature_combinations_1.txt`：65 条。
    - `dql_dameng_system_function_signature_combinations_2.txt`：61 条。
    - `dql_dameng_system_function_signature_combinations_3.txt`：1 条 SELECT。
- Chrome 操作：按清单顺序逐条提交；若某函数阻断后续语句，错误后重试合法查询。
  `CALL SF_WHO_CALLED_ME(:OWNER_NAME, :CALLER_NAME, :LINE_NUM, :CALLER_TYPE);` 保留为后续单独复测项。
- 预期结果：支持的函数产生结果；对象、参数、版本或权限不满足时保留具体错误。MySQL 兼容名 `CONV`、`FORMAT`、`MID`、
  `STRING_TO_ARRAY`、`STR_TO_DATE` 在当前 DM8 返回 unresolved 时，不将其误记为成功。
- 恢复/清理：错误后执行 `SELECT 1 FROM DUAL;`。

### DAMENG-STATE-01 系统函数副作用恢复（P0）

- Chrome 操作：查询并恢复 `SORT_BUF_SIZE`、`PWD_POLICY`、SQL 日志开关及 MAL/MPP 配置；执行 `SELECT 1 FROM DUAL;`。
- 预期结果：参数和集群配置与执行前记录一致，数据库连接正常。
- 恢复/清理：删除可能生成的测试备份目录和提示注入记录。

### DAMENG-BOUNDARY-01 失败、权限与生命周期（P1）

- Boundaries：函数清单覆盖 NULL、空串、Unicode、LOB、二进制、日期/时间、最大精度、窗口 frame 和不存在对象。
- Repeat And Concurrency：重复执行只读函数时结果页签不覆盖，长批次执行按钮显示 loading。
- Permission：无审计/系统权限时显示权限错误。
- Lifecycle：刷新页面后重新选择 `SYSDBA` 并执行 smoke SQL。

## 页面已执行 SQL 明细

选中 `codex-it-dameng-8 / SYSDBA`。按表格顺序每条单独执行；第 4 条之前切换页面到手动事务模式。

### DAL 组合 fixture 第一段：29 条逐条复测表

来源：`open-cdm-test/src/test/resources/split/dameng/8/dcl_dameng_dal_combinations_batch_0.txt`
。下表严格沿用第一段原文和顺序；“待测”不是通过，也不计页面覆盖。页面环境为 `codex-it-dameng-8 / SYSDBA`（`127.0.0.1:5236`）。
`dm8` 容器中备份目录已存在 `dal_orders`，不能把写入同一路径的备份或还原当成无副作用查询。`ALTER DATABASE MOUNT/ARCHIVELOG`
、恢复测试表、审计规则与持久参数变更也应在确认当前状态和可恢复方式后单独执行。

额外混合与失败恢复场景（不计上方 29 条 fixture）：在自动事务模式全选 `STAT ON SYSDBA.ORDERS;` 与 `SELECT 1 FROM DUAL;`
同批执行，执行信息依次为 `0 rows affected`、`1 row retrieved`，SELECT 结果单元格为 `1`，重写显示 `LIMIT 1000`。再全选
`CONFIGURE;` 与 `SELECT 1 FROM DUAL;` 同批执行，第一条报 `Member access [CONFIGURE] unresolved`，第二条仍返回一行 `1`
，没有使后续查询失效。两组均在 CloudDM 页面提交并核对，不是静态 split 测试。

### 新增 DAL 混合与恢复 fixture：8 条

来源：`open-cdm-test/src/test/resources/split/dameng/8/dcl_dameng_dal_combinations_recovery_1.txt` 第一段。定向 split 测试
8/8 通过；下表的页面状态则只根据 CloudDM 可见结果判断。`SET TIME ZONE LOCAL` 和 `EXPLAIN FOR`
对照[达梦官方数据定义语句](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)
与[数据查询语句](https://eco.dameng.com/document/dm/zh-cn/pm/check-phrases.html)语法。第 3/6 条为一次 DAL + SELECT 全选批次，第
7/8 条为一次错误 + SELECT 全选批次；其他条逐条提交。

## 管理 SQL / 管理过程独立覆盖表（CloudDM 页面）

这张表只统计管理语句及直接过程调用；读取系统视图或以 `SELECT`
调用系统函数，最多算元信息查询，不替代对应命令的下发覆盖。官方依据：[DM8 数据定义/管理语句](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)、[备份还原实战（区分 DIsql 与 DMRMAN）](https://eco.dameng.com/document/dm/zh-cn/pm/backup-restore-combat.html)、[审计](https://eco.dameng.com/document/dm/zh-cn/pm/audit.html)、[管理过程附录](https://eco.dameng.com/document/dm/zh-cn/pm/sql-appendix.html)
。这里的“已测”须分 CloudDM 下发、数据库响应与数据库操作成功；源码静态分类通过不得算页面下发。

- 管理能力：会话设置；已有静态资源（不是页面证据）：`admin_dameng_session_config_0.txt`：NLS、时区、大小写、并行 DML、会话参数和 PURGE；CloudDM 成功执行的具体语句 / 页面状态：原 DAL `ALTER SESSION SET NLS_DATE_FORMAT`、`SET TIME ZONE`，增量 M01–M03；本轮 `ALTER SESSION ENABLE PARALLEL DML;` / `DISABLE PARALLEL DML;` 均 `0 rows affected`；未正常执行、待测与真实边界：`PURGE` 会清理服务器保存的执行计划，未页面提交；THAI/KOREAN 排序受字符集/环境限制，其他形式有 fixture 但未逐条页面验证。
- 管理能力：系统 INI / 管理过程；已有静态资源（不是页面证据）：原 DAL、`admin_dameng_session_config_0.txt`、系统过程 fixture；CloudDM 成功执行的具体语句 / 页面状态：`ALTER SYSTEM SET 'SORT_BUF_SIZE'=11 MEMORY;`、`SP_SET_PARA_VALUE(0,'SORT_BUF_SIZE',12);` 原表已成功并恢复 `10`；本轮 `SP_RESET_SESSION_PARA_VALUE('JOIN_HASH_SIZE');` 为 `0 rows affected`；未正常执行、待测与真实边界：静态/SPFILE/持久参数、SQL 日志开关及其他过程未页面成功验证；`SELECT SF_GET_PARA_VALUE` 只能算读取，不代替设置过程。
- 管理能力：REDO/日志归档；已有静态资源（不是页面证据）：`admin_dameng_database_mode_0.txt`、`admin_dameng_instance_lifecycle_0.txt` 有数据库模式/归档命令；本轮 `dcl_dameng_dal_combinations_batch_1.txt` 新补两条系统切换；CloudDM 成功执行的具体语句 / 页面状态：`CHECKPOINT(30);`、`CHECKPOINT(0);` 原表显示成功；未正常执行、待测与真实边界：本轮 `ALTER SYSTEM SWITCH LOGFILE;` 与 `ALTER SYSTEM ARCHIVE LOG CURRENT;` 均页面失败：`Operation does not support using PREPARE way`；静态 split 2/2 PASS 不能代替页面 PASS。`ALTER DATABASE MOUNT/ARCHIVELOG` 待测，当前 OPEN 单节点下切换会中断连接并改变归档状态。
- 管理能力：联机备份 / 表还原；已有静态资源（不是页面证据）：`dcl_dameng_dal_combinations_batch_0.txt` 及多份 BACKUP/RESTORE fixture；CloudDM 成功执行的具体语句 / 页面状态：本轮用未存在的专用路径 `BACKUP TABLE SYSDBA.ORDERS BACKUPSET '/opt/dmdbms/data/backup/clouddm_mgmt_orders_20260916';` 页面 `0 rows affected`，判正常执行；该测试备份路径保留未删除；未正常执行、待测与真实边界：原库/表空间/归档备份均返回 `No local or remote archive log`，未正常执行；原 `dal_orders` 备份路径已有数据，未覆盖。`RESTORE TABLE` 会覆盖测试表数据，未页面提交，不能把备份成功当还原覆盖。脱机 `RESTORE DATABASE` 需 DMRMAN/独立目标库，不属 CloudDM 联机 SQL 页。
- 管理能力：备份检查与 DMRMAN 配置；已有静态资源（不是页面证据）：`CHECK BACKUPSET`、`CONFIGURE` 静态 fixture 已有；CloudDM 成功执行的具体语句 / 页面状态：无 CloudDM SQL 页面成功执行记录；未正常执行、待测与真实边界：官方案例明确写 `RMAN>CHECK BACKUPSET`、`RMAN>CONFIGURE`；原表在 CloudDM SQL 页报语法/未解析错误是工具边界，不应宣称联机备份 SQL 失败，也不应作为普通 CloudDM 管理能力继续追求页面 PASS。`CONFIGURE DEFAULT TRACE CLEAR` 原表未提交，且会清除 TRACE。
- 管理能力：统计 / 执行计划；已有静态资源（不是页面证据）：原 DAL、STAT/EXPLAIN fixture；CloudDM 成功执行的具体语句 / 页面状态：原 `STAT ON...`、列/索引 STAT、`EXPLAIN`；本轮 `STAT /*+PARALLEL(2)*/ 30 ON SYSDBA.ORDERS(ID, STATUS);` 为 `0 rows affected`，`EXPLAIN FOR SELECT ID, STATUS FROM SYSDBA.ORDERS WHERE ID=1;` 展示 4 行计划；未正常执行、待测与真实边界：`STAT ... GLOBAL` 官方限定 MPP/DPC 且使用 GLOBAL 登录，当前单节点未测；统计命令可能提交当前事务，勿用查询结果数量替代事务验证。
- 管理能力：安全权限 / 审计；已有静态资源（不是页面证据）：`dcl_dameng_*privileges*.txt`、`admin_dameng_role_state_0.txt`、系统审计 fixture；CloudDM 成功执行的具体语句 / 页面状态：本轮无新增成功执行；元信息 `SELECT` 不计此类；未正常执行、待测与真实边界：`SP_AUDIT_STMT` 原表待测；`SP_SET_ENABLE_AUDIT`、`SP_AUDIT_OBJECT`、`SP_NOAUDIT_STMT` 有静态用例但未页面验证。改变审计开关、权限或测试角色状态可能改变安全保护，须确定专用测试对象、原值及恢复动作；不应拿读取审计系统视图代替过程调用。
- 管理能力：事务 / 锁；已有静态资源（不是页面证据）：原 DAL 29 条与恢复 8 条；CloudDM 成功执行的具体语句 / 页面状态：手动模式 SAVEPOINT / ROLLBACK TO / RELEASE / COMMIT 和 `LOCK TABLE ... SHARE MODE NOWAIT` 页面 `0 rows affected`；`START TRANSACTION READ ONLY` 已进入达梦 JDBC 执行链并返回明确错误；未正常执行、待测与真实边界：只读事务未成功启动；若需验证成功路径，应先结束现有事务并使用适合达梦驱动的连接状态单独复测。

本轮额外管理 SQL 每条在已登录 CloudDM `codex-it-dameng-8 / SYSDBA` 页签单独提交；不计顶部 39 条 fixture 进度。目标只判断
CloudDM 解析及下发，不继续验证备份内容、统计值或管理动作的业务完整效果。日志切换两条既属于新 fixture，又计顶部 39 条；PREPARE
阻断下发的实际失败必须单列。

L01/L02 的[官方语法](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)已确认，静态资源 2/2 通过。仓库
`backend/clouddm-plugins/clouddm-ds/ds-dameng/src/main/java/com/clougence/clouddm/ds/dameng/execute/DmHooks.java:122-135`
中 `executeStatement` 只将事务、`ALTER DATABASE` 和 `EXPLAIN` 转为普通 `Statement`，其余走 `PreparedStatement`
；页面错误与该执行路径吻合。此处记录缺陷，不把 SQL 弱化为其他写法，也不假称数据库不支持。

## 达梦 CONVERT style 用例（DM8 容器只读验证）

来源：[达梦产品手册“函数”](https://eco.dameng.com/document/dm/zh-cn/pm/function) 中 `CONVERT(type,value[,style])`
的日期格式说明。新增的 12 条 SQL 位于
`open-cdm-test/src/test/resources/split/dameng/8/dql_dameng_conversion_function_signature_combinations_2.txt`，split
解析测试为 12/12 通过。2026-09-15 在 CloudDM `codex-it-dameng-8 / SYSDBA` 自动事务查询页逐条执行下表 13
条；结果栏记录页面可见值，不以容器直连替代页面验证。

### 数值函数 fixture：19 条页面实测

来源：`dql_dameng_numeric_function_signature_combinations_1.txt` 第一段；在 CloudDM `SYSDBA` 页逐条执行，没有跳过。

### 字符串/日期转换 fixture：27 条页面实测

来源：`dql_dameng_string_remaining_signature_combinations_1.txt` 第一段；在 CloudDM `SYSDBA` 页逐条执行，没有跳过。

### 空值函数 fixture：16 条页面实测

来源：`dql_dameng_null_function_signature_combinations_1.txt` 第一段；在 CloudDM `SYSDBA` 页逐条执行，没有跳过。

### 日期函数 fixture：9 条页面实测

来源：`dql_dameng_datetime_functions_1.txt` 第一段；在 CloudDM `SYSDBA` 页逐条执行，没有跳过。

### 杂类函数 fixture：12 条页面实测

来源：`dql_dameng_misc_function_signature_combinations_1.txt` 第一段；在 CloudDM `SYSDBA` 页逐条执行，没有跳过。

### 聚合函数 fixture：51 条页面实测

来源：`dql_dameng_aggregate_function_signature_combinations_0.txt` 第一段；`APP.AGG_T` 页面预查 3 行，按下表原顺序在
CloudDM `SYSDBA` 页逐条提交。下方值来自页面结果表或执行信息；日期 `AREA_MAX` 的错误显示在结果单元格而非执行信息。`COLLECT`、
`PERCENTILE_DISC` 和 `BIT_AND` 的错误已重新提交核对。分组 SQL 页面预查共两组。

### 分析窗口函数 fixture `_1`：39 条页面实测

来源：`dql_dameng_analytic_window_atomic_1.txt` 第一段；`APP.ANALYTIC_T` 页面预查 4 行。在 CloudDM `SYSDBA`
查询页逐条提交，结果栏给出页面行数与末行函数值以便复测核对，不能将 `MEDIAN` 的执行错误算作成功。

### 分析窗口函数 fixture `_2`：前 32 条页面记录

来源：`dql_dameng_analytic_window_atomic_2.txt` 第一段。按原顺序在 CloudDM `SYSDBA` 查询页逐条提交前 32 条。第 4、21、23
条未生成结果表；浏览器连接随后中断，暂不编造其执行信息细节。第 33–48 条的提交状态无法从超时批次确认，须恢复浏览器后逐条核对，当前不计页面已测。

### 官方 DM8 目录增量核对与页面复测（2026-09-16）

对照[达梦函数手册](https://eco.dameng.com/document/dm/zh-cn/pm/function.html)、[系统函数/过程附录](https://eco.dameng.com/document/dm/zh-cn/pm/sql-appendix.html)
和[数据定义/管理语句](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)。服务器版本由页面执行
`SELECT BANNER FROM V$VERSION;` 确认：`DM Database Server 64 V8`、`DB Version: 0x7000d`、
`03134284368-20250821-288894-20149`；`Dameng JDBC Driver/8.1.3.140` 是驱动而不是服务器版本。本轮是对已知目录的定向增量核对，不等同于宣称官网全部函数、命令均已覆盖。

- 官方能力与来源：数值、字符串、转换、日期、空值、聚合/分析函数，[函数手册](https://eco.dameng.com/document/dm/zh-cn/pm/function.html)；静态 fixture / 页面证据：本文原有 `numeric/string/conversion/datetime/null/aggregate/analytic` 明细；部分函数失败；缺口、版本与复测建议：`CONV`、`FORMAT`、`MID` 等官网列出的名字在当前 DM8 构建返回 unresolved；有 fixture 且已测失败，是版本/编译或兼容模式差异待查，不能记“官网不存在”或静态通过。窗口 `_2` 第 33–48 条仍待辨认页面提交边界，其他未入表条目也未测。
- 官方能力与来源：系统函数/过程、元数据、空间、网络/INET、检查和备份，[函数手册](https://eco.dameng.com/document/dm/zh-cn/pm/function.html)及[附录](https://eco.dameng.com/document/dm/zh-cn/pm/sql-appendix.html)；静态 fixture / 页面证据：`dql_dameng_system_function_signature_combinations_{0,1,2,3}.txt` 共 180 条 SELECT，此前没有页面逐条入表；本轮下表 27 条；缺口、版本与复测建议：已有 fixture 但剩余大量未页面验证；设置系统参数、备份集删除、归档删除、MAL/MPP 拓扑及提示注入有副作用，不能当成只读函数批量跑。`SF_VIEW_EXPIRED`、`SF_GET_TABLE_COUNT` 因示例对象不存在返回错误；先创建专属测试对象后再测。`CALL SF_WHO_CALLED_ME` 需页面绑定输出参数。
- 官方能力与来源：会话管理，[数据定义语句 §3.14/3.16](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)；静态 fixture / 页面证据：`ALTER SESSION SET CASE_SENSITIVE=DEFAULT`、`NLS_SORT=BINARY`、`NLS_DATE_LANGUAGE=ENGLISH` 本轮页面执行；原 DAL 表已有 `NLS_DATE_FORMAT`；缺口、版本与复测建议：已有静态会话设置 fixture，但页面未覆盖的语法仍需逐条复测；`PURGE` 可清空服务器全部保存的执行计划，不能作为普通无副作用变体。会话修改只对当前连接有效，复测须同连接做 `SESS_CASE_SENSITIVE()` 并恢复基线。
- 官方能力与来源：系统日志切换，[数据定义语句 §3.17](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)；静态 fixture / 页面证据：核对前两种 `ALTER SYSTEM` 日志切换均无 fixture；已补 `dcl_dameng_dal_combinations_batch_1.txt`，定向 split 2/2 PASS；缺口、版本与复测建议：两种官网形式均已在隔离 CloudDM 页面提交，但都报 `Operation does not support using PREPARE way`，L01/L02 为页面 FAIL；不能用静态 2/2 冒充真实执行成功。
- 官方能力与来源：`STAT ... GLOBAL`，[数据定义语句 §3.18](https://eco.dameng.com/document/dm/zh-cn/pm/definition-statement.html)；静态 fixture / 页面证据：原 DAL 表测普通表/列/索引统计；未测 GLOBAL；缺口、版本与复测建议：文档限定 GLOBAL 用于 MPP/DPC，单节点 DM8 不能把环境错误当通用支持；准备真实集群或独立版只测非 GLOBAL。

下表每行是在已登录 CloudDM 的 `codex-it-dameng-8 / SYSDBA` 页签单独提交；结果数值按本容器环境记录，环境有关的路径、用量、时间不能硬编码为其他库的预期。
`LIMIT 1000` 是页面对 SELECT 的实际 rewrite。编号 F01–F27 对应原 435 条计划中的 fixture，已计入顶部进度；另外 V01 与
M01–M04 是目录/管理额外复测，不重复计数。F09/F23 的数据库对象错误同样证明 CloudDM 已下发，不表示函数在本库成功返回结果。

M01–M03 后又单独重复提交一次，均再次显示 `0 rows affected`；重复不计覆盖。`CASE_SENSITIVE=DEFAULT` 和 `NLS_SORT=BINARY`
回到库默认语义；日期语言设置为 ENGLISH 仅当前会话，不改全局。六条原 DAL 高风险待测状态保持不变；新增两种日志切换形式已按
L01/L02 提交且失败。

## 2026-09-16 CloudDM 当前轮解析/下发回归

本节只统计当前轮在 CloudDM 页面重新提交的证据，不再沿用前半部分的历史逐条结果。435 条函数中的 406 条已重跑；39 条 DAL
fixture 中重跑 24 条普通项，15 条风险项保留 NOT RUN；V01、M01–M04、A01–A07 共 12 个额外编号中重跑 11 条，备份写入 A06 保留
NOT RUN。辅助准备 SQL 与重复提交不加号；A07/M04 均作为不同失败恢复场景各计一次。

- 类型：函数；本轮总数：435；PASS 下发：406；FAIL CloudDM：0；BLOCKED：0；NOT RUN：29
- 类型：DAL/管理 fixture；本轮总数：39；PASS 下发：23；FAIL CloudDM：1；BLOCKED：0；NOT RUN：15
- 类型：额外版本/管理/恢复；本轮总数：12；PASS 下发：11；FAIL CloudDM：0；BLOCKED：0；NOT RUN：1
- 类型：合计；本轮总数：486；PASS 下发：440；FAIL CloudDM：1；BLOCKED：0；NOT RUN：45

当前轮 DAL/管理口径为：原 29 条重跑 16 条（15 PASS、1 FAIL、13 NOT RUN），恢复 8 条全部重跑并 PASS，日志切换 L01/L02 因会改变
REDO/归档状态而不重跑。数据库返回错误计 PASS；`SET SCHEMA` 被页面拦截、没有进入 SQL 执行链，因此计 FAIL CloudDM。
`START TRANSACTION READ ONLY` 虽未成功启动事务，但服务日志调用栈明确落在 `DmSession.executeStatement` →
`DmdbStatement.execute`，按“已下发并获得数据库响应”计 PASS。前半部分仍保留历史 L01/L02 PREPARE 失败，但不计当前轮 39 条结果。

### 本轮续测：固定数据源与缺口函数批次

续测前在 CloudDM 页面明确核对数据源为 `codex-it-dameng-8 / SYSDBA @127.0.0.1:5236`，并执行
`SELECT BANNER FROM V$VERSION;`。页面于 12:11:39 返回 `DM Database Server 64 V8`、
`DB Version: 0x7000d` 和构建号 `03134284368-20250821-288894-20149`；以下结果没有计入 TiDB、MySQL、
PostgreSQL 或 Doris。

- 批次：DMC-01；fixture 与精确范围：`dql_dameng_conversion_function_signature_combinations_1.txt` 第 1–22 条；页面时间：12:12:09；实际结果：22 条全部进入 Execution Information；18 条结果表，4 条达梦错误（区间数据丢失、日期格式、两条 `TRANSLATE ... USING` 语法错误）；本轮判定：22 PASS
- 批次：DMA-02；fixture 与精确范围：`dql_dameng_analytic_window_atomic_2.txt` 第 4、21、23、33–48 条；页面时间：12:12:53；实际结果：19 条全部处理到末条；8 条结果表，11 条达梦窗口语法/函数限制；本轮判定：19 PASS
- 批次：DMS-00；fixture 与精确范围：`dql_dameng_system_function_signature_combinations_0.txt` 第 1–9、15–49 条；页面时间：12:13:29；实际结果：44 条全部处理；39 条结果表，5 条达梦对象/参数错误；本轮判定：44 PASS
- 批次：DMS-01；fixture 与精确范围：`dql_dameng_system_function_signature_combinations_1.txt` 第 4、8–32、34–37、41–42、48–65 条；页面时间：12:14:18；实际结果：50 条全部处理；24 条结果表，26 条达梦对象/环境错误；本轮判定：50 PASS
- 批次：DMS-02；fixture 与精确范围：`dql_dameng_system_function_signature_combinations_2.txt` 第 1–5、7–33、38–61 条；页面时间：12:15:00；实际结果：56 条全部处理；49 条结果表，7 条达梦对象/参数错误；本轮判定：56 PASS
- 批次：DMS-03；fixture 与精确范围：`dql_dameng_system_function_signature_combinations_3.txt` 第 1 条；另加 1 条非计划 CALL；页面时间：12:15:41、12:15:50；实际结果：计划内 `SF_GET_ARCH_SEND_UNTIL_TIME` 到达达梦并返回 `Instance archive config item not found`；额外 `CALL SF_WHO_CALLED_ME(...)` 返回 `0 rows affected`；本轮判定：1 个函数计划 PASS；1 个额外 CALL PASS（不计 435）
- 批次：DMF-01；fixture 与精确范围：`datetime_functions_1` 9 条、`misc_function_signature_combinations_1` 12 条、`null_function_signature_combinations_1` 16 条、`numeric_function_signature_combinations_1` 19 条、`string_remaining_signature_combinations_1` 27 条；页面时间：12:28:31；实际结果：83 条全部进入 Execution Information；31 条结果表，52 条达梦函数/类型错误；本轮判定：83 PASS
- 批次：DMA-01；fixture 与精确范围：`dql_dameng_aggregate_function_signature_combinations_0.txt` 第 1–51 条；页面时间：12:29:22；实际结果：51 条全部处理到末条；45 条结果表，6 条达梦服务端错误；本轮判定：51 PASS
- 批次：DMW-01；fixture 与精确范围：`dql_dameng_analytic_window_atomic_1.txt` 第 1–39 条；页面时间：12:30:04；实际结果：39 条全部处理到末条；38 条结果表，1 条达梦窗口限制；本轮判定：39 PASS
- 批次：DMW-02；fixture 与精确范围：`dql_dameng_analytic_window_atomic_2.txt` 除已定向补测 19 条外的其余 29 条；页面时间：12:30:49；实际结果：29 条均返回 4 行结果表；本轮判定：29 PASS
- 批次：DMC-02；fixture 与精确范围：文档保存的 `dql_dameng_conversion_function_signature_combinations_2.txt` 原 12 条 `CONVERT style` SQL；页面时间：12:31:36；实际结果：12 条均返回结果表；批次末尾 `SELECT 1 FROM DUAL` 同样成功；本轮判定：12 PASS

### 当前轮 DAL/管理普通项证据

以下批次于 **2026-09-16 12:54:42–12:58:46 CST** 在同一 `codex-it-dameng-8 / SYSDBA @127.0.0.1:5236` 页面执行。除结果页签外，已在页面
Execution Information 中滚动到对应时间段，逐条核对无结果集命令的原始影响行数或错误；不能只靠页签数量推断下发。

- 批次：DMD-01；当前轮实际提交：原 29 条中的 #6、#7、#10–#13、#17、#19、#29；页面证据：12:54:42 执行信息逐条显示：`CHECK BACKUPSET` 达梦 Syntax error、`CONFIGURE` Member access unresolved，三条 STAT、CHECKPOINT、ALTER SESSION、SET TIME ZONE 均为 `0 rows affected`，EXPLAIN 返回计划；当前轮判定：9 PASS
- 批次：DMD-02；当前轮实际提交：recovery 8 条 #1–#8；页面证据：12:54:42 执行信息逐条显示 SET TIME ZONE/CHECKPOINT/STAT/LOCK 为 `0 rows affected`、EXPLAIN 返回计划、两条 SELECT 各 `1 row retrieved`；`CONFIGURE` 报达梦错误后末条仍成功；当前轮判定：8 PASS
- 批次：DMD-03；当前轮实际提交：V01、M01–M04、A01–A05、A07；页面证据：12:54:42 执行信息逐条显示 V$VERSION `5 row retrieved`，会话/STAT/过程为 `0 rows affected`，A04 EXPLAIN 与 A07/M04 SELECT 返回结果；DMD-01–03 合计新增 `result157`–`result164`；当前轮判定：11 PASS；A06 未执行
- 批次：DMD-04；当前轮实际提交：原 #20 `SET SCHEMA SYSDBA`，单独点击 Run；页面证据：页面再次显示“当前版本不支持切换库或模式命令”，无执行信息；当前轮判定：1 FAIL CloudDM
- 批次：DMD-05；当前轮实际提交：手动事务模式下原 #23 `START TRANSACTION READ ONLY`；页面证据：12:58:44 开启手动事务模式；12:58:46 页面原始错误 `Try to change property before transaction end`。同一时刻服务日志调用栈为 `DmSession.executeStatement(DmSession.java:52)` → `DefaultRdbSession.execQuery` → `DmdbStatement.execute`，证明 SQL 已进入达梦 JDBC 执行链；当前轮判定：1 PASS 下发；达梦驱动/数据库拒绝，事务未成功启动
- 批次：DMD-06；当前轮实际提交：手动事务模式下原 #24–#28（SAVEPOINT、ROLLBACK TO、RELEASE、LOCK、COMMIT）；页面证据：12:56:27 执行信息逐条显示五条均 `0 rows affected`；12:56:56 恢复自动事务模式。复核 DMD-05 后再次恢复 `Tx: Auto`；当前轮判定：5 PASS

当前轮 16 条 DAL/管理 NOT RUN 明细：

- 范围：原 29 条中的 13 条；NOT RUN SQL/编号：#1–#5 备份/还原、#8 `CONFIGURE DEFAULT DEVICE TYPE`、#9 TRACE 清除、#14–#16 数据库 MOUNT/OPEN/ARCHIVELOG、#18/#21 系统参数修改、#22 审计规则修改；原因：会写备份、覆盖数据、清除诊断信息、改变数据库模式/系统参数/安全审计；留待用户统一执行。
- 范围：日志切换 2 条；NOT RUN SQL/编号：L01 `ALTER SYSTEM SWITCH LOGFILE`、L02 `ALTER SYSTEM ARCHIVE LOG CURRENT`；原因：即使历史页面曾被 PREPARE 阻断，当前轮不能假设仍会失败；若下发成功会切换 REDO/归档，因此不重跑。
- 范围：额外管理 1 条；NOT RUN SQL/编号：A06 `BACKUP TABLE ... clouddm_mgmt_orders_20260916`；原因：历史目标目录已生成，当前轮重复执行可能覆盖/冲突，不重跑。

本节续测实际页面覆盖 406 个计划内函数用例，另执行 1 条不计入 435 计划的 `CALL SF_WHO_CALLED_ME(...)`。406 个计划内用例均由
CloudDM 解析并下发；达梦服务端错误按本流程验收口径记 PASS，不表示函数在当前单节点环境成功返回业务值。批次使用的精确 SQL
就是上表指定 fixture 行，未将静态 split 结果冒充页面证据。

以下 29 条系统函数未混入普通批次，统一保留为 `NOT RUN（待用户执行）`：

- fixture：`system_function_signature_combinations_0`；未执行行：10–14、50–53；风险：修改会话/系统参数、SQL 日志或同步 INI，或清除全局字典缓存
- fixture：`system_function_signature_combinations_1`；未执行行：1–3、5–7、33、38–40、43–47；风险：修改备份目录、删除备份集、修复 HFS、修改 MAL/MPP 拓扑或删除归档日志
- fixture：`system_function_signature_combinations_2`；未执行行：6、34–37；风险：`SF_SI` 携带 `CREATE INDEX` SQL，或创建、删除、修改持久化提示注入

1. 本轮更改的 `SORT_BUF_SIZE` 已恢复并通过 `V$DM_INI` 确认 `PARA_VALUE`、`SESS_VALUE`、`FILE_VALUE` 均为基线 `10`；本轮未变更
   SQL 日志或集群配置。
2. 本轮未生成备份目录，未删除既有 `dal_orders` 备份集或 APP 测试对象；这些专用对象继续留供用户复测，复测结束后再确认归属和清理范围。
3. 已恢复 CloudDM 自动事务模式；错误后的 `SELECT 1 FROM DUAL;` 页面返回 `1`。

## Skip Conditions

- 本轮六条尚未页面提交的 DAL 已逐条列为“待测”，原因是已有备份路径、表还原、清除 TRACE、数据库模式切换或审计配置的状态风险；不能把它们算作执行通过。
- 依赖归档的备份命令本轮仍已提交并记录 `No local or remote archive log`；数据库模式变更需独立确认恢复路径后才能测试。
