# MySQL DAL 与函数集成验证

## SQL 执行明细

判定口径：CloudDM 能解析并将 SQL 发送到数据库即为通过；数据库返回错误不等于 CloudDM 失败。安全或破坏性操作保留待用户执行状态。

验证状态统一为：通过、CloudDM 失败、未执行、证据不足。通过仅表示已有证据确认 SQL 被解析并发送到数据库，数据库报错保留在执行证据中。历史执行与复测记录均保留，每行反映该次记录，不代表相同 SQL 的最新复测结论。

本表记录数：通过 591；CloudDM 失败 0；未执行 36；证据不足 0。包含重复执行记录，不等同于去重用例数。本次为文档证据整理，未重新执行 SQL。

| 编号 | SQL | 验证状态 | 实际结果/执行证据 | 前提/预期 | 来源/备注 |
| --- | --- | --- | --- | --- | --- |
| 1 | `SELECT AES_ENCRYPT('foo', REPEAT('x', 16), NULL, REPEAT('1', 10000000000));` | 未执行 | 未执行；风险：申请 100 亿字符，可能造成内存/CPU 压力或连接失效 | 独占可重启实例，设置资源限制并准备取消查询 | 风险：申请 100 亿字符，可能造成内存/CPU 压力或连接失效 |
| 2 | `SELECT asynchronous_connection_failover_add_source('split_failover','127.0.0.1',3306,'',50);` | 未执行 | 未执行；风险：修改异步复制故障转移源 | 记录现有复制配置并准备恢复 | 风险：修改异步复制故障转移源 |
| 3 | `SELECT asynchronous_connection_failover_delete_source('split_failover','127.0.0.1',3306,'');` | 未执行 | 未执行；风险：删除异步复制故障转移源 | 确认目标为专用测试条目且允许删除 | 风险：删除异步复制故障转移源 |
| 4 | `SELECT asynchronous_connection_failover_add_managed('split_failover','GroupReplication','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','127.0.0.1',3306,'',80,60);` | 未执行 | 未执行；风险：修改托管复制故障转移配置 | 记录现有复制配置并准备恢复 | 风险：修改托管复制故障转移配置 |
| 5 | `SELECT asynchronous_connection_failover_delete_managed('split_failover','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa');` | 未执行 | 未执行；风险：删除托管复制故障转移配置 | 确认目标为专用测试条目且允许删除 | 风险：删除托管复制故障转移配置 |
| 6 | `SELECT audit_log_encryption_password_set('split_password');` | 未执行 | 未执行；风险：修改审计日志加密密码，属于凭据变更 | 用户在页面接手输入并提交，且已有恢复密码方案 | 风险：修改审计日志加密密码，属于凭据变更 |
| 7 | `SELECT audit_log_filter_flush();` | 未执行 | 未执行；风险：刷新审计过滤器全局状态 | 记录当前审计配置并准备恢复 | 风险：刷新审计过滤器全局状态 |
| 8 | `SELECT audit_log_filter_remove_filter('split_filter');` | 未执行 | 未执行；风险：删除审计过滤器 | 确认过滤器为专用测试对象并可重建 | 风险：删除审计过滤器 |
| 9 | `SELECT audit_log_filter_remove_user('split_user@%');` | 未执行 | 未执行；风险：删除用户审计映射 | 确认映射为专用测试对象并可重建 | 风险：删除用户审计映射 |
| 10 | `SELECT audit_log_filter_set_filter('split_filter','{"filter":{"log":true}}');` | 未执行 | 未执行；风险：修改审计过滤策略 | 记录当前策略并准备恢复 | 风险：修改审计过滤策略 |
| 11 | `SELECT audit_log_filter_set_user('split_user@%','split_filter');` | 未执行 | 未执行；风险：修改用户审计映射 | 记录当前映射并准备恢复 | 风险：修改用户审计映射 |
| 12 | `SELECT audit_log_rotate();` | 未执行 | 未执行；风险：轮转审计日志，影响当前审计文件 | 确认日志可归档且允许轮转 | 风险：轮转审计日志，影响当前审计文件 |
| 13 | `SELECT gen_dictionary_drop('split_dict');` | 未执行 | 未执行；风险：删除数据屏蔽字典 | 确认字典为专用测试对象且有重建数据 | 风险：删除数据屏蔽字典 |
| 14 | `SELECT gen_dictionary_load('/tmp/split_dictionary.txt','split_dict');` | 未执行 | 未执行；风险：从服务器文件加载/覆盖字典状态 | 准备受控文件并记录原字典 | 风险：从服务器文件加载/覆盖字典状态 |
| 15 | `SELECT masking_dictionaries_flush();` | 未执行 | 未执行；风险：刷新数据屏蔽字典全局状态 | 记录当前字典状态并准备恢复 | 风险：刷新数据屏蔽字典全局状态 |
| 16 | `SELECT masking_dictionary_remove('split_dict');` | 未执行 | 未执行；风险：删除数据屏蔽字典 | 确认字典为专用测试对象且有备份 | 风险：删除数据屏蔽字典 |
| 17 | `SELECT masking_dictionary_term_add('split_dict','split_term');` | 未执行 | 未执行；风险：修改数据屏蔽字典内容 | 记录原内容并准备删除测试 term | 风险：修改数据屏蔽字典内容 |
| 18 | `SELECT masking_dictionary_term_remove('split_dict','split_term');` | 未执行 | 未执行；风险：删除数据屏蔽字典条目 | 确认条目为测试数据且可恢复 | 风险：删除数据屏蔽字典条目 |
| 19 | `SELECT keyring_aws_rotate_cmk();` | 未执行 | 未执行；风险：轮转 AWS Keyring 主密钥 | 仅在专用 Keyring 环境及恢复方案就绪后执行 | 风险：轮转 AWS Keyring 主密钥 |
| 20 | `SELECT keyring_aws_rotate_keys();` | 未执行 | 未执行；风险：轮转 AWS Keyring 密钥 | 仅在专用 Keyring 环境及恢复方案就绪后执行 | 风险：轮转 AWS Keyring 密钥 |
| 21 | `SELECT keyring_hashicorp_update_config();` | 未执行 | 未执行；风险：更新 HashiCorp Keyring 配置 | 备份配置并验证回滚路径 | 风险：更新 HashiCorp Keyring 配置 |
| 22 | `SELECT firewall_group_delist('split_group','fwuser@localhost');` | 未执行 | 未执行；风险：修改防火墙组成员 | 记录现有成员并准备恢复 | 风险：修改防火墙组成员 |
| 23 | `SELECT firewall_group_enlist('split_group','fwuser@localhost');` | 未执行 | 未执行；风险：修改防火墙组成员 | 记录现有成员并准备恢复 | 风险：修改防火墙组成员 |
| 24 | `SELECT set_firewall_group_mode('split_group','DETECTING','fwuser@localhost');` | 未执行 | 未执行；风险：修改防火墙组模式 | 记录现有模式并准备恢复 | 风险：修改防火墙组模式 |
| 25 | `SELECT GET_DD_PROPERTY_KEY_VALUE(NULL,NULL), REMOVE_DD_PROPERTY_KEY(NULL,NULL), CONVERT_INTERVAL_TO_USER_INTERVAL(NULL,NULL), INTERNAL_GET_DD_COLUMN_EXTRA(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_GET_USERNAME(), INTERNAL_GET_USERNAME(NULL), INTERNAL_GET_HOSTNAME(), INTERNAL_GET_HOSTNAME(NULL), INTERNAL_GET_ENABLED_ROLE_JSON(), INTERNAL_GET_MANDATORY_ROLES_JSON(), INTERNAL_IS_MANDATORY_ROLE(NULL,NULL), INTERNAL_IS_ENABLED_ROLE(NULL,NULL);` | 未执行 | 未执行；风险：混合调用包含内部 DD 属性删除函数 | 仅在可还原的数据字典测试实例执行 | 风险：混合调用包含内部 DD 属性删除函数 |
| 26 | `SELECT keyring_key_store('split_stored_key','AES','0123456789abcdef');` | 未执行 | 未执行；风险：写入 Keyring 密钥 | 专用 Keyring、无同名正式密钥并准备清理 | 风险：写入 Keyring 密钥 |
| 27 | `SELECT keyring_key_generate('split_generated_key','AES',16);` | 未执行 | 未执行；风险：生成并持久化 Keyring 密钥 | 专用 Keyring、无同名正式密钥并准备清理 | 风险：生成并持久化 Keyring 密钥 |
| 28 | `SELECT keyring_key_remove('split_stored_key'), keyring_key_remove('split_generated_key');` | 未执行 | 未执行；风险：删除 Keyring 密钥且不可由查询恢复 | 确认均为测试密钥且已有备份 | 风险：删除 Keyring 密钥且不可由查询恢复 |
| 29 | `SELECT SOURCE_POS_WAIT('missing-binlog.000001',4);` | 未执行 | 未执行；风险：无超时位点等待，可能长期占用连接 | 使用可取消的独立连接并准备立即取消 | 风险：无超时位点等待，可能长期占用连接 |
| 30 | `SELECT load_rewrite_rules();` | 未执行 | 未执行；风险：重载全局查询改写规则 | 备份规则表并准备恢复/重载原配置 | 风险：重载全局查询改写规则 |
| 31 | `SELECT version_tokens_set('alpha=one;beta=two');` | 未执行 | 未执行；风险：覆盖全局版本令牌状态 | 记录全部现有 token 并准备恢复 | 风险：覆盖全局版本令牌状态 |
| 32 | `SELECT version_tokens_edit('alpha=updated;gamma=three');` | 未执行 | 未执行；风险：修改全局版本令牌状态 | 记录全部现有 token 并准备恢复 | 风险：修改全局版本令牌状态 |
| 33 | `SELECT version_tokens_delete('beta');` | 未执行 | 未执行；风险：删除全局版本令牌 | 确认 token 为测试对象并准备恢复 | 风险：删除全局版本令牌 |
| 34 | `SELECT asynchronous_connection_failover_reset();` | 未执行 | 未执行；风险：重置异步复制故障转移配置 | 完整备份复制配置并具备恢复步骤 | 风险：重置异步复制故障转移配置 |
| 35 | `SELECT audit_log_read('{"max_array_length":1}'), set_firewall_mode('fwuser@localhost','RECORDING') FROM split_management_function_probe;` | 未执行 | 未执行；风险：混合查询包含防火墙模式变更 | 记录用户当前防火墙模式并准备恢复 | 风险：混合查询包含防火墙模式变更 |
| 36 | `SELECT 1;` | 通过 | 返回 1 行，值为 `1`。 | — | — |
| 37 | `SELECT VERSION();` | 通过 | 返回版本结果。 | — | — |
| 38 | `SELECT * FROM orders ORDER BY id;` | 通过 | 返回 3 行测试订单。 | — | — |
| 39 | `SELECT * FROM schema1.orders LIMIT 1;` | 通过 | 返回 1 行，确认 DAL fixture 对象存在。 | — | — |
| 40 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 41 | `` PREPARE dal_stmt FROM 'SELECT id FROM schema1.orders WHERE status IN (?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 42 | `` SET @dal_arg0 = (0 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 43 | `` EXECUTE dal_stmt USING @dal_arg0; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 44 | `` DEALLOCATE PREPARE dal_stmt; `` | 通过 | 成功；0 rows affected | — | — |
| 45 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 46 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 47 | `` PREPARE dal_stmt FROM 'SELECT id FROM schema1.orders WHERE status IN (?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 48 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 49 | `` EXECUTE dal_stmt USING @dal_arg0, @dal_arg1; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 50 | `` DEALLOCATE PREPARE dal_stmt; `` | 通过 | 成功；0 rows affected | — | — |
| 51 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 52 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 53 | `` PREPARE dal_stmt FROM 'SELECT id FROM schema1.orders WHERE status IN (?, ?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 54 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1), @dal_arg2 = (2 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 55 | `` EXECUTE dal_stmt USING @dal_arg0, @dal_arg1, @dal_arg2; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 56 | `` DEALLOCATE PREPARE dal_stmt; `` | 通过 | 成功；0 rows affected | — | — |
| 57 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 58 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 59 | `` PREPARE dal_stmt FROM @dal_sql; `` | 通过 | 成功；0 rows affected | — | — |
| 60 | `` SET @dal_arg0 = (0 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 61 | `` EXECUTE dal_stmt USING @dal_arg0; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 62 | `` DEALLOCATE PREPARE dal_stmt; `` | 通过 | 成功；0 rows affected | — | — |
| 63 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 64 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 65 | `` PREPARE dal_stmt FROM @dal_sql; `` | 通过 | 成功；0 rows affected | — | — |
| 66 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 67 | `` EXECUTE dal_stmt USING @dal_arg0, @dal_arg1; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 68 | `` DEALLOCATE PREPARE dal_stmt; `` | 通过 | 成功；0 rows affected | — | — |
| 69 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 70 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 71 | `` PREPARE dal_stmt FROM @dal_sql; `` | 通过 | 成功；0 rows affected | — | — |
| 72 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1), @dal_arg2 = (2 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 73 | `` EXECUTE dal_stmt USING @dal_arg0, @dal_arg1, @dal_arg2; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 74 | `` DEALLOCATE PREPARE dal_stmt; `` | 通过 | 成功；0 rows affected | — | — |
| 75 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 76 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 77 | `` PREPARE `dal stmt` FROM 'SELECT id FROM schema1.orders WHERE status IN (?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 78 | `` SET @dal_arg0 = (0 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 79 | `` EXECUTE `dal stmt` USING @dal_arg0; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 80 | `` DEALLOCATE PREPARE `dal stmt`; `` | 通过 | 成功；0 rows affected | — | — |
| 81 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 82 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 83 | `` PREPARE `dal stmt` FROM 'SELECT id FROM schema1.orders WHERE status IN (?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 84 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 85 | `` EXECUTE `dal stmt` USING @dal_arg0, @dal_arg1; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 86 | `` DEALLOCATE PREPARE `dal stmt`; `` | 通过 | 成功；0 rows affected | — | — |
| 87 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 88 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 89 | `` PREPARE `dal stmt` FROM 'SELECT id FROM schema1.orders WHERE status IN (?, ?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 90 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1), @dal_arg2 = (2 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 91 | `` EXECUTE `dal stmt` USING @dal_arg0, @dal_arg1, @dal_arg2; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 92 | `` DEALLOCATE PREPARE `dal stmt`; `` | 通过 | 成功；0 rows affected | — | — |
| 93 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 94 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 95 | `` PREPARE `dal stmt` FROM @dal_sql; `` | 通过 | 成功；0 rows affected | — | — |
| 96 | `` SET @dal_arg0 = (0 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 97 | `` EXECUTE `dal stmt` USING @dal_arg0; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 98 | `` DEALLOCATE PREPARE `dal stmt`; `` | 通过 | 成功；0 rows affected | — | — |
| 99 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 100 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 101 | `` PREPARE `dal stmt` FROM @dal_sql; `` | 通过 | 成功；0 rows affected | — | — |
| 102 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 103 | `` EXECUTE `dal stmt` USING @dal_arg0, @dal_arg1; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 104 | `` DEALLOCATE PREPARE `dal stmt`; `` | 通过 | 成功；0 rows affected | — | — |
| 105 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 106 | `` SET @dal_sql = 'SELECT id FROM schema1.orders WHERE status IN (?, ?, ?) AND ''x;--'' = ''x;--'' /* keep ; */'; `` | 通过 | 成功；0 rows affected | — | — |
| 107 | `` PREPARE `dal stmt` FROM @dal_sql; `` | 通过 | 成功；0 rows affected | — | — |
| 108 | `` SET @dal_arg0 = (0 + 1), @dal_arg1 = (1 + 1), @dal_arg2 = (2 + 1); `` | 通过 | 成功；0 rows affected | — | — |
| 109 | `` EXECUTE `dal stmt` USING @dal_arg0, @dal_arg1, @dal_arg2; `` | 通过 | 成功；本隔离数据下返回 0 行 | — | — |
| 110 | `` DEALLOCATE PREPARE `dal stmt`; `` | 通过 | 成功；0 rows affected | — | — |
| 111 | `` SHOW FULL TABLES IN schema1 WHERE Tables_in_schema1 LIKE 'ord%'; `` | 通过 | 成功；返回 2 行表信息 | — | — |
| 112 | `` LOCK INSTANCE FOR BACKUP; `` | 通过 | 0 rows affected | — | — |
| 113 | `` UNLOCK INSTANCE; `` | 通过 | 0 rows affected | — | — |
| 114 | `` RESET PERSIST IF EXISTS max_connections; `` | 通过 | 0 rows affected | — | — |
| 115 | `` RESET PERSIST; `` | 通过 | 0 rows affected | — | — |
| 116 | `` RESET PERSIST default.key_buffer_size; `` | 通过 | 数据库错误：持久化文件中无 default.key_buffer_size | — | — |
| 117 | `` RESET PERSIST IF EXISTS default.key_buffer_size; `` | 通过 | 0 rows affected | — | — |
| 118 | `` SHOW REPLICAS; `` | 通过 | 返回 0 行 | — | — |
| 119 | `` SHOW BINARY LOG STATUS; `` | 通过 | 返回 1 行 | — | — |
| 120 | `` CHANGE REPLICATION SOURCE TO SOURCE_HOST='127.0.0.1', SOURCE_PORT=3306, SOURCE_USER='repl', SOURCE_PASSWORD='pw', SOURCE_AUTO_POSITION=0, GET_SOURCE_PUBLIC_KEY=1 FOR CHANNEL 'chan84'; `` | 通过 | 0 rows affected | — | — |
| 121 | `` SHOW REPLICA STATUS FOR CHANNEL 'chan84'; `` | 通过 | 返回 1 行 | — | — |
| 122 | `` START REPLICA SQL_THREAD UNTIL SQL_AFTER_GTIDS = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa:1' FOR CHANNEL 'chan84'; `` | 通过 | 0 rows affected | — | — |
| 123 | `` STOP REPLICA SQL_THREAD FOR CHANNEL 'chan84'; `` | 通过 | 0 rows affected | — | — |
| 124 | `` RESET REPLICA ALL FOR CHANNEL 'chan84'; `` | 通过 | 0 rows affected | — | — |
| 125 | `` RESET BINARY LOGS AND GTIDS; `` | 通过 | 0 rows affected；隔离容器 binlog/GTID 已重置 | — | — |
| 126 | `` FLUSH OPTIMIZER_COSTS; `` | 通过 | 0 rows affected | — | — |
| 127 | `` FLUSH RELAY LOGS FOR CHANNEL 'split_chan'; `` | 通过 | 数据库错误：split_chan 不存在 | — | — |
| 128 | `` FLUSH ERROR LOGS, ENGINE LOGS, GENERAL LOGS, SLOW LOGS, BINARY LOGS, RELAY LOGS, PRIVILEGES, LOGS, STATUS, USER_RESOURCES, OPTIMIZER_COSTS; `` | 通过 | 0 rows affected | — | — |
| 129 | `` ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v, c WITH 8 BUCKETS; `` | 通过 | 返回 1 行状态结果 | — | — |
| 130 | `` ANALYZE TABLES split84.maint_innodb UPDATE HISTOGRAM ON v WITH 4 BUCKETS; `` | 通过 | 返回 1 行状态结果 | — | — |
| 131 | `` ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v; `` | 通过 | 返回 1 行状态结果 | — | — |
| 132 | `` ANALYZE TABLE split84.maint_innodb DROP HISTOGRAM ON v, c; `` | 通过 | 返回 1 行状态结果 | — | — |
| 133 | `` ANALYZE LOCAL TABLE split84.maint_innodb UPDATE HISTOGRAM ON v, c WITH 8 BUCKETS AUTO UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 134 | `` ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v WITH 4 BUCKETS MANUAL UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 135 | `` ANALYZE TABLE split_native_gap.t_hist UPDATE HISTOGRAM ON c USING DATA '{}'; `` | 通过 | 返回 1 行状态结果 | — | — |
| 136 | `` RESET BINARY LOGS AND GTIDS TO 7; `` | 通过 | 0 rows affected；binlog 索引设为 7 | — | — |
| 137 | `` RESET BINARY LOGS AND GTIDS TO 0x7; `` | 通过 | 0 rows affected；binlog 索引设为 7 | — | — |
| 138 | `` RESET BINARY LOGS AND GTIDS TO X'07'; `` | 通过 | 0 rows affected；binlog 索引设为 7 | — | — |
| 139 | `` RESET REPLICA, BINARY LOGS AND GTIDS TO 7; `` | 通过 | 0 rows affected；隔离容器已重置 | — | — |
| 140 | `` RESTART; `` | 通过 | 数据库错误：mysqld 未由 supervisor 管理 | — | — |
| 141 | `` SHUTDOWN; `` | 通过 | 0 rows affected；容器正常退出；重启后同页签查询 FAIL | — | — |
| 142 | `` GET STACKED DIAGNOSTICS CONDITION 1 @state = RETURNED_SQLSTATE, @msg = MESSAGE_TEXT; `` | 通过 | 数据库错误：handler 未激活 | — | — |
| 143 | `` FLUSH LOCAL RELAY LOGS FOR CHANNEL 'codex_gap7_ch'; `` | 通过 | 数据库错误：codex_gap7_ch 不存在 | — | — |
| 144 | `` FLUSH NO_WRITE_TO_BINLOG RELAY LOGS FOR CHANNEL 'codex_gap7_ch'; `` | 通过 | 数据库错误：codex_gap7_ch 不存在 | — | — |
| 145 | `` ANALYZE TABLE t_hist UPDATE HISTOGRAM ON c1,c3 AUTO UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 146 | `` ANALYZE TABLE split84.maint_innodb UPDATE HISTOGRAM ON v MANUAL UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 147 | `` RESET BINARY LOGS AND GTIDS TO 0; `` | 通过 | 数据库错误：binlog 索引 0 超出允许范围 1–2000000000 | — | — |
| 148 | `` SELECT AES_DECRYPT(AES_ENCRYPT(_UTF8MB3'Жоро', 'a'), 'a') = _UTF8MB3'Жоро', AES_DECRYPT(AES_ENCRYPT('Жоро', 'a'), 'a') = 'Жоро'; `` | 通过 | 返回 1 行 | — | — |
| 149 | `` SELECT TO_BASE64(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf')), LENGTH(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf')), CHARSET(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf')), AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf') = AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf'); `` | 通过 | 返回 1 行 | — | — |
| 150 | `` SELECT AES_ENCRYPT('my_text', REPEAT('x', 32), '', 'hkdf') = AES_ENCRYPT('my_text', REPEAT('y', 32), '', 'hkdf'), AES_ENCRYPT('my_text', REPEAT('x', 32), '', 'hkdf') = AES_ENCRYPT('my_text', '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0', '', 'hkdf'); `` | 通过 | 返回 1 行 | — | — |
| 151 | `` SELECT TO_BASE64(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf')), TO_BASE64(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf', 'salt')); `` | 通过 | 返回 1 行 | — | — |
| 152 | `` SELECT 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf', 10001), 'my_key_string', '', 'hkdf', 10001), 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', '', 'hkdf', 10001, 2000), 'my_key_string', '', 'hkdf', 10001, 2000); `` | 通过 | 返回 1 行 | — | — |
| 153 | `` SELECT AES_ENCRYPT('foo', REPEAT('x', 16), NULL, 'hKdF'); `` | 通过 | 数据库错误：KDF 方法名大小写不被接受 | — | — |
| 154 | `` SELECT 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', @aes_kdf_iv, 'hkdf'), 'my_key_string', @aes_kdf_iv, 'hkdf'), 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', @aes_kdf_iv, 'hkdf', 'salt'), 'my_key_string', @aes_kdf_iv, 'hkdf', 'salt'), 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', @aes_kdf_iv, 'hkdf', 'salt', 'info'), 'my_key_string', @aes_kdf_iv, 'hkdf', 'salt', 'info'); `` | 通过 | 返回 1 行 | — | — |
| 155 | `` SELECT TO_BASE64(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac')), LENGTH(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac')), CHARSET(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac')), AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac') = AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac'); `` | 通过 | 返回 1 行 | — | — |
| 156 | `` SELECT TO_BASE64(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac')), TO_BASE64(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac', 'salt')); `` | 通过 | 返回 1 行 | — | — |
| 157 | `` SELECT AES_ENCRYPT('my_text', REPEAT('x', 32), '') = AES_ENCRYPT('my_text', REPEAT('y', 32), ''), AES_ENCRYPT('my_text', REPEAT('x', 32), '', 'pbkdf2_hmac') = AES_ENCRYPT('my_text', REPEAT('y', 32), '', 'pbkdf2_hmac'), AES_ENCRYPT('my_text', REPEAT('x', 32), '', 'pbkdf2_hmac') = AES_ENCRYPT('my_text', '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0', '', 'pbkdf2_hmac'); `` | 通过 | 返回 1 行 | — | — |
| 158 | `` SELECT 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac', 4000, '10001'), 'my_key_string', '', 'pbkdf2_hmac', 4000, '10001'), 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac', 4000, 10001), 'my_key_string', '', 'pbkdf2_hmac', 4000, 10001); `` | 通过 | 返回 1 行 | — | — |
| 159 | `` SELECT AES_ENCRYPT('my_text', 'my_key_string', '', 'invalid'); `` | 通过 | 数据库错误：KDF 方法名 invalid | — | — |
| 160 | `` SELECT AES_ENCRYPT('my_text', REPEAT('x', 32)), AES_ENCRYPT('my_text', 'my_key'); `` | 通过 | 返回 1 行 | — | — |
| 161 | `` SELECT AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac', 'salt', '100'); `` | 通过 | 数据库错误：pbkdf2_hmac iterations 越界 | — | — |
| 162 | `` SELECT AES_ENCRYPT('my_text', 'my_key_string', '', 'pbkdf2_hmac', 'salt', 'aa'); `` | 通过 | 数据库错误：pbkdf2_hmac iterations 越界 | — | — |
| 163 | `` SELECT AES_ENCRYPT('foo', REPEAT('x', 16), NULL, REPEAT('1', 10000000000)); `` | 通过 | 数据库错误：KDF option 超过 256 字节 | — | — |
| 164 | `` SELECT AES_ENCRYPT('foo', REPEAT('x', 16), NULL, REPEAT('1', 300)); `` | 通过 | 数据库错误：KDF option 超过 256 字节 | — | — |
| 165 | `` SELECT AES_ENCRYPT('foo', REPEAT('x', 16), NULL, 'pbkdf2_HMac'); `` | 通过 | 数据库错误：KDF 方法名大小写不被接受 | — | — |
| 166 | `` SELECT 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', @aes_kdf_iv, 'pbkdf2_hmac'), 'my_key_string', @aes_kdf_iv, 'pbkdf2_hmac'), 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', @aes_kdf_iv, 'pbkdf2_hmac', 'salt'), 'my_key_string', @aes_kdf_iv, 'pbkdf2_hmac', 'salt'), 'my_text' = AES_DECRYPT(AES_ENCRYPT('my_text', 'my_key_string', @aes_kdf_iv, 'pbkdf2_hmac', 'salt', '10001'), 'my_key_string', @aes_kdf_iv, 'pbkdf2_hmac', 'salt', '10001'); `` | 通过 | 返回 1 行 | — | — |
| 167 | `` SELECT STATEMENT_DIGEST('SELECT 1') = STATEMENT_DIGEST('SELECT 1 FROM DUAL'); `` | 通过 | 返回 1 行 | — | — |
| 168 | `` SELECT STATEMENT_DIGEST_TEXT('SELECT 1'), STATEMENT_DIGEST_TEXT('SELECT 1 FROM DUAL'); `` | 通过 | 返回 1 行 | — | — |
| 169 | `` EXPLAIN SELECT IF(u=1,st,BINARY st) AS s FROM case_t WHERE st LIKE '%a%' ORDER BY s; `` | 通过 | 返回 1 行 | — | — |
| 170 | `` SELECT JSON_ARRAYAGG(NULLIF(1,2)); `` | 通过 | 返回 1 行 | — | — |
| 171 | `` SELECT JSON_ARRAYAGG(CASE WHEN TRUE=FALSE THEN NULL ELSE TRUE END); `` | 通过 | 返回 1 行 | — | — |
| 172 | `` SELECT NULLIF(COALESCE(x,NULL),0) FROM enum_t EXCEPT SELECT 1; `` | 通过 | 返回 1 行 | — | — |
| 173 | `` SELECT COALESCE(IF(x,NULL,NULL),x) FROM enum_t EXCEPT SELECT 1; `` | 通过 | 返回 2 行 | — | — |
| 174 | `` SELECT COALESCE(CASE WHEN x THEN NULL ELSE NULL END,x) FROM enum_t EXCEPT SELECT 1; `` | 通过 | 返回 2 行 | — | — |
| 175 | `` SELECT COALESCE(COALESCE(NULL,NULL),x) FROM enum_t EXCEPT SELECT 1; `` | 通过 | 返回 2 行 | — | — |
| 176 | `` SELECT i8 FROM t1 WHERE d IN (DATE'2020-01-01',DATE'2020-02-02') AND t IN (TIME'01:01:01',TIME'02:02:02') AND dt IN (TIMESTAMP'2020-01-01 01:01:01',TIMESTAMP'2020-02-02 02:02:02'); `` | 通过 | 返回 1 行 | — | — |
| 177 | `` SELECT i8 FROM t1 WHERE j IN (CAST('{"i":1,"s":"1"}' AS JSON),CAST('{"i":2,"s":"2"}' AS JSON)); `` | 通过 | 返回 1 行 | — | — |
| 178 | `` SELECT i8 FROM t1 WHERE ji IN (1,2) AND js IN ('1','2'); `` | 通过 | 返回 1 行 | — | — |
| 179 | `` SELECT i8 FROM t1 WHERE (i8,dc,vc) IN ((1,1.1,'1'),(2,2.2,'2')); `` | 通过 | 返回 1 行 | — | — |
| 180 | `` SET @e:=1; `` | 通过 | 0 rows affected | — | — |
| 181 | `` DO('x' IN (CONVERT(EXP(0xbf40f8f5) USING utf32),UNHEX(@e))); `` | 通过 | 数据库错误：EXP 值超出 DOUBLE 范围 | — | — |
| 182 | `` SELECT 27 DIV CAST(1/97 AS DOUBLE) / 17 AS quotient GROUP BY quotient; `` | 通过 | 返回 1 行 | — | — |
| 183 | `` DO DATEDIFF(UUID_TO_BIN(UUID()),0x32df2ce8), (!(SECOND(0xb16beeb7))); `` | 通过 | 0 rows affected | — | — |
| 184 | `` DO (IS_IPV4_MAPPED(BIN_TO_UUID(@misc_value:=34))) <=> (JSON_OBJECTAGG('key2',42) AND RTRIM('')); `` | 通过 | 数据库错误：BIN_TO_UUID 参数格式非法 | — | — |
| 185 | `` EXPLAIN SELECT COUNT(*) FROM prefix_varchar WHERE b LIKE 'abc\%%'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 186 | `` EXPLAIN SELECT COUNT(*) FROM prefix_varchar WHERE b LIKE '\_\_\_\_%'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 187 | `` EXPLAIN SELECT b LIKE 'abc%' FROM prefix_varchar WHERE b LIKE 'ab%'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 188 | `` EXPLAIN SELECT COUNT(*) FROM prefix_varchar IGNORE INDEX(k2) WHERE b LIKE 'a%'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 189 | `` EXPLAIN SELECT b LIKE 'ab%' FROM prefix_varchar FORCE INDEX(k3) WHERE a>4 AND b LIKE 'a%'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 190 | `` EXPLAIN SELECT COUNT(*) FROM prefix_text WHERE b LIKE 'aaaa'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 191 | `` SELECT (f1 LIKE NULL) FROM prefix_blob; `` | 通过 | 返回 1 行 | — | — |
| 192 | `` SELECT 1 FROM prefix_blob WHERE f1 NOT LIKE JSON_MERGE('','+'); `` | 通过 | 数据库错误：JSON_MERGE 第一个参数不是合法 JSON | — | — |
| 193 | `` SELECT 1 FROM prefix_blob WHERE f1 LIKE JSON_CONTAINS('key2','key4'); `` | 通过 | 数据库错误：JSON_CONTAINS 第一个参数不是合法 JSON | — | — |
| 194 | `` SELECT 1 FROM prefix_blob WHERE f1 LIKE JSON_DEPTH(NULL); `` | 通过 | 返回 0 行 | — | — |
| 195 | `` EXPLAIN SELECT (f1 LIKE NULL) FROM prefix_blob WHERE f1 LIKE 'a%'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 196 | `` EXPLAIN SELECT prefix_lower_2.example,prefix_lower_2.id FROM prefix_lower_2,prefix_lower_1 WHERE prefix_lower_1.example=LOWER(prefix_lower_2.example); `` | 通过 | EXPLAIN 返回 2 行 | — | — |
| 197 | `` EXPLAIN FORMAT=TREE SELECT 1 WHERE RAND() < RAND(); `` | 通过 | EXPLAIN FORMAT=TREE 返回 1 行 | — | — |
| 198 | `` EXPLAIN SELECT 1 WHERE RAND() < RAND(); `` | 通过 | 返回 1 行 | — | — |
| 199 | `` SELECT r FROM codex_func_rand.outer_t LEFT JOIN LATERAL (SELECT i,RAND(0) AS r) AS dt ON TRUE; `` | 通过 | 返回 1 行；补建 codex_func_rand.outer_t 后复测 | — | — |
| 200 | `` SELECT STATEMENT_DIGEST((SELECT USER())); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 201 | `` SELECT STATEMENT_DIGEST('This can''t be parsed but yields a digest.') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 202 | `` SELECT STATEMENT_DIGEST(); `` | 通过 | 数据库错误：参数数量不正确 | — | — |
| 203 | `` SELECT STATEMENT_DIGEST('too many','arguments'); `` | 通过 | 数据库错误：参数数量不正确 | — | — |
| 204 | `` SELECT STATEMENT_DIGEST(NULL); `` | 通过 | 返回 1 行 | — | — |
| 205 | `` SELECT STATEMENT_DIGEST('/*') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 206 | `` SELECT STATEMENT_DIGEST('@@') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 207 | `` SELECT STATEMENT_DIGEST('SELECT 1; SELECT 2'); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 208 | `` SELECT STATEMENT_DIGEST('SELECT 1;') IS NULL; `` | 通过 | 返回 1 行 | — | — |
| 209 | `` SELECT STATEMENT_DIGEST(POINT(1,2)) IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 210 | `` SELECT STATEMENT_DIGEST(_utf8mb4 X'e298ba') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 211 | `` SELECT STATEMENT_DIGEST('a\0bc') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 212 | `` SELECT STATEMENT_DIGEST('\0abc') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 213 | `` SELECT STATEMENT_DIGEST(''); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 214 | `` SELECT STATEMENT_DIGEST(STATEMENT_DIGEST(NULL)); `` | 通过 | 返回 1 行 | — | — |
| 215 | `` SELECT STATEMENT_DIGEST(STATEMENT_DIGEST('SELECT 1, 2, 3')); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 216 | `` SELECT STATEMENT_DIGEST('SELECT ?'); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 217 | `` SELECT STATEMENT_DIGEST('INSERT DELAYED INTO t1 VALUES(1)') IS NULL; `` | 通过 | 返回 1 行 | — | — |
| 218 | `` SELECT STATEMENT_DIGEST(REPEAT('a',character_maximum_length)) IS NULL FROM information_schema.columns WHERE table_name='events_statements_history' AND column_name='digest'; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 219 | `` SELECT STATEMENT_DIGEST_TEXT((SELECT USER())); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 220 | `` SELECT STATEMENT_DIGEST_TEXT('not valid SQL') IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 221 | `` SELECT STATEMENT_DIGEST_TEXT(); `` | 通过 | 数据库错误：参数数量不正确 | — | — |
| 222 | `` SELECT STATEMENT_DIGEST_TEXT('too many','arguments'); `` | 通过 | 数据库错误：参数数量不正确 | — | — |
| 223 | `` SELECT STATEMENT_DIGEST_TEXT(POINT(1,2)) IS NULL; `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 224 | `` SELECT STATEMENT_DIGEST_TEXT(STATEMENT_DIGEST_TEXT('SELECT 1, 2, 3')); `` | 通过 | 数据库错误：digest 参数不能解析 | — | — |
| 225 | `` SELECT STATEMENT_DIGEST_TEXT('SELECT a + b, a - b FROM t1, t2, t3 WHERE a = c'), STATEMENT_DIGEST('CREATE VIEW v1 AS SELECT 1'), STATEMENT_DIGEST('CREATE TRIGGER trg BEFORE INSERT ON t1 FOR EACH ROW SET @a := 1'), STATEMENT_DIGEST('LOCK TABLE t1 READ'); `` | 通过 | 返回 1 行 | — | — |
| 226 | `` SELECT STATEMENT_DIGEST_TEXT(CONVERT(CONCAT('SELECT 1 AS ',_latin1 0xC5) USING latin1)), SUBSTR(HEX(STATEMENT_DIGEST_TEXT('SELECT 1 + 1')),-6,4); `` | 通过 | 返回 1 行 | — | — |
| 227 | `` DO CONVERT(INET_ATON(CAST(LEFT(-1,1) AS BINARY(30))) USING utf8mb3); `` | 通过 | 0 rows affected | — | — |
| 228 | `` SELECT CAST(CONCAT('a') AS DOUBLE); `` | 通过 | 返回 1 行 | — | — |
| 229 | `` EXPLAIN SELECT s FROM trim_values WHERE TRIM(s)>'ab'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 230 | `` EXPLAIN SELECT s FROM trim_values WHERE TRIM('y' FROM s)>'ab'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 231 | `` EXPLAIN SELECT s FROM trim_values WHERE TRIM(LEADING 'y' FROM s)>'ab'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 232 | `` EXPLAIN SELECT s FROM trim_values WHERE TRIM(TRAILING 'y' FROM s)>'ab'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 233 | `` EXPLAIN SELECT s FROM trim_values WHERE TRIM(BOTH 'y' FROM s)>'ab'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 234 | `` EXPLAIN SELECT * FROM code_values INNER JOIN code_ids ON code=id WHERE id='a12' AND (LENGTH(code)=5 OR code<'a00'); `` | 通过 | EXPLAIN 返回 2 行 | — | — |
| 235 | `` SELECT HEX(29223372036854775809) AS hex_signed,HEX(CAST(29223372036854775809 AS UNSIGNED)) AS hex_unsigned; `` | 通过 | 返回 1 行 | — | — |
| 236 | `` SELECT CONV(29223372036854775809,-10,16) AS conv_signed,CONV(29223372036854775809,10,16) AS conv_unsigned; `` | 通过 | 返回 1 行 | — | — |
| 237 | `` SELECT HEX(-29223372036854775809) AS hex_signed,HEX(CAST(-29223372036854775809 AS UNSIGNED)) AS hex_unsigned; `` | 通过 | 返回 1 行 | — | — |
| 238 | `` SELECT CONV(-29223372036854775809,-10,16) AS conv_signed,CONV(-29223372036854775809,10,16) AS conv_unsigned; `` | 通过 | 返回 1 行 | — | — |
| 239 | `` SELECT _utf8mb4 0x7373 = _utf8mb3 0xC39F AS c; `` | 通过 | 返回 1 行 | — | — |
| 240 | `` SELECT _utf8mb3 0xC39F = _utf8mb4 0x7373 AS c; `` | 通过 | 返回 1 行 | — | — |
| 241 | `` SELECT LPAD('',42,REPLACE(MD5(c1),c1,'')) FROM t1; `` | 通过 | 返回 1 行 | — | — |
| 242 | `` SELECT TRIM(LEADING _utf8mb4 x'F09F8DA3' FROM _gb18030 x'9439B9376181308B33'); `` | 通过 | 返回 1 行 | — | — |
| 243 | `` SELECT SUBSTRING_INDEX(_utf8mb4 x'C3A6C3B8F09F8DA361C3A6C3B8F09F8DA362',_gb18030 x'81308B339439B937',2); `` | 通过 | 返回 1 行 | — | — |
| 244 | `` SELECT REPLACE(_utf8mb3 0xC3A6C3B8C3A5C3A6C3B8C3A5,_utf16 x'00e5',_gb18030 x'9439B937'); `` | 通过 | 数据库错误：gb18030 字符串不能转换为 utf8mb3 | — | — |
| 245 | `` SELECT JSON_TYPE(BIN(EXP(0x5f022b6c81))); `` | 通过 | 数据库错误：EXP 值超出 DOUBLE 范围 | — | — |
| 246 | `` SELECT BIN(IFNULL(JSON_LENGTH('ba', '{"aabbc":"abbab","bbccb":"a","cbacb":2}'), BIT_OR(ANY_VALUE(MINUTE('2537-01-12 00:52:47') AND TRUE)))); `` | 通过 | 数据库错误：JSON 文本无效；页面出现 2 条重复日志 | — | — |
| 247 | `` SELECT BIN(IFNULL(JSON_LENGTH(5), RELEASE_ALL_LOCKS())); `` | 通过 | 数据库错误：JSON_LENGTH 参数类型无效 | — | — |
| 248 | `` SELECT OCT(IFNULL(UUID_TO_BIN(SIN(0x69)), (-23447) DIV 14115145681444956431)); `` | 通过 | 数据库错误：UUID_TO_BIN 参数无效 | — | — |
| 249 | `` SELECT SUBSTRING_INDEX(IFNULL(SIGN(0xab) AND JSON_QUOTE(ABS(9223372036854775806)), UUID_SHORT()), 4595, 45); `` | 通过 | 数据库错误：JSON_QUOTE 参数类型无效 | — | — |
| 250 | `` SELECT CONVERT(STR_TO_DATE(CURRENT_TIMESTAMP, COALESCE(INET_ATON(32767), LOCATE(0xc4, 20, 27), JSON_STORAGE_SIZE('a'))) USING EUCKR); `` | 通过 | 数据库错误：字符集转换失败 | — | — |
| 251 | `` SELECT LENGTH(RPAD(_utf8mb3 0xD0B1,65536,_utf8mb3 0xD0B2)) AS data; `` | 通过 | 返回 1 行 | — | — |
| 252 | `` SELECT LENGTH(data) AS len FROM (SELECT RPAD(_utf8mb3 0xD0B1,65536,_utf8mb3 0xD0B2) AS data) AS sub; `` | 通过 | 返回 1 行 | — | — |
| 253 | `` SELECT LENGTH(RPAD(_utf8mb3 0xD0B1,65535,_utf8mb3 0xD0B2)) AS data; `` | 通过 | 返回 1 行 | — | — |
| 254 | `` SELECT LENGTH(data) AS len FROM (SELECT RPAD(_utf8mb3 0xD0B1,65535,_utf8mb3 0xD0B2) AS data) AS sub; `` | 通过 | 返回 1 行 | — | — |
| 255 | `` SELECT HEX(TRIM(CONVERT(_gb18030 0x20202081408141208144202020 USING utf32))); `` | 通过 | 返回 1 行 | — | — |
| 256 | `` SELECT CHAR(0xff,0x8f USING utf8mb3); `` | 通过 | 返回 1 行 | — | — |
| 257 | `` SELECT CHAR(0xff,0x8f USING utf8mb3) IS NULL; `` | 通过 | 返回 1 行 | — | — |
| 258 | `` DO ST_ISVALID(INSTR(9223372036854775806 ,0x46bc299f)); `` | 通过 | 数据库错误：二进制字符不能转 utf8mb4 | — | — |
| 259 | `` SELECT (SELECT 1 FROM t_int WHERE CONVERT(1 USING gb18030) <> GROUP_CONCAT(x'a3')); `` | 通过 | 数据库错误：二进制字符不能转 gb18030；补建 t_int 后复测 | — | — |
| 260 | `` DO USER() IN (COERCIBILITY(@c), CONVERT(LAST_VALUE(FROM_UNIXTIME(1536999169)) OVER() USING utf32)); `` | 通过 | 0 rows affected | — | — |
| 261 | `` DO SPACE(SHA(UUID_SHORT())); `` | 通过 | 0 rows affected | — | — |
| 262 | `` DO LPAD(BIT_XOR(1), ROW_COUNT(), JSON_PRETTY('-$ *?(8}')); `` | 通过 | 数据库错误：JSON_PRETTY 输入无效 | — | — |
| 263 | `` DO RPAD(BIT_XOR(1), ROW_COUNT(), JSON_PRETTY('-$ *?(8}')); `` | 通过 | 数据库错误：JSON_PRETTY 输入无效 | — | — |
| 264 | `` DO ROUND(CONCAT(COALESCE(ST_LINEFROMWKB('2147483648',-b'1111111111111111111111111111111111111111111'),CONVERT('[.DC2.]',DECIMAL(30,30)),BIT_COUNT('')),LPAD(ELT('01','}:K5'),SHA1('P'),((SELECT '-9223372036854775808.1' > ALL (SELECT '')))))); `` | 通过 | 数据库错误：SRID 越界 | — | — |
| 265 | `` DO '' SOUNDS LIKE LEAD(DATABASE(), 1, x'cafe') OVER(); `` | 通过 | 0 rows affected | — | — |
| 266 | `` SELECT REVERSE(TIME_FORMAT('', NULL)) AS field1 FROM codex_func_str_modern.t_rollup GROUP BY ROLLUP(REVERSE(TIME_FORMAT('', NULL))); `` | 通过 | 返回 2 行 | — | — |
| 267 | `` DO SHA1(DATABASE()); `` | 通过 | 0 rows affected | — | — |
| 268 | `` SELECT CHARSET(DATABASE()),DATABASE()=_utf8mb3'split_func_system',DATABASE()=_latin1'split_func_system'; `` | 通过 | 返回 1 行 | — | — |
| 269 | `` SELECT USER() LIKE _utf8mb3'%@%',USER() LIKE _latin1'%@%',CHARSET(USER()); `` | 通过 | 返回 1 行 | — | — |
| 270 | `` SELECT VERSION()>=_utf8mb3'3.23.29',VERSION()>=_latin1'3.23.29',CHARSET(VERSION()); `` | 通过 | 返回 1 行 | — | — |
| 271 | `` SELECT CHARSET(CHARSET(_utf8mb3'a')),CHARSET(COLLATION(_utf8mb3'a')),COLLATION(CHARSET(_utf8mb3'a')),COLLATION(COLLATION(_utf8mb3'a')); `` | 通过 | 返回 1 行 | — | — |
| 272 | `` SELECT GREATEST(dt.a,func_test_charset.a) FROM func_test_charset,(SELECT _utf8mb4 'a') AS dt(a); `` | 通过 | 返回 1 行 | — | — |
| 273 | `` SELECT STD(c1 % '2006-08-23 21:41:12.036166') FROM func_test_std_mix GROUP BY BIN_TO_UUID(c1),CAST(LEAST(c1,CONVERT(c1,UNSIGNED)) AS DECIMAL); `` | 通过 | 数据库错误：BIN_TO_UUID 输入不是 UUID | — | — |
| 274 | `` SELECT CAST(1 AS YEAR) UNION SELECT CAST(1 AS YEAR); `` | 通过 | 返回 1 行 | — | — |
| 275 | `` SELECT HEX(UUID_TO_BIN('{c8eb4b15-cb09-48bb-bbb2-e6a0b6b4d5c7}', TRUE)), HEX(UUID_TO_BIN('c8eb4b15cb0948bbbbb2e6a0b6b4d5c7', TRUE)), HEX(UUID_TO_BIN('c8eb4b15-cb09-48bb-bbb2-e6a0b6b4d5c7', TRUE)); `` | 通过 | 返回 1 行 | — | — |
| 276 | `` SELECT UUID_TO_BIN('{c8eb4b15-CB09-48bb-bbb2-e6a0b6b4d5c7}') = X'c8eb4b15cb0948bbbbb2e6a0b6b4d5c7', UUID_TO_BIN('{c8eb4b15-CB09-48bb-bbb2-e6a0b6b4d5c7}', TRUE) = X'48bbcb09c8eb4b15bbb2e6a0b6b4d5c7'; `` | 通过 | 返回 1 行 | — | — |
| 277 | `` SELECT BIN_TO_UUID(UNHEX('7f9d04ae61b34468ac798ffcc984ab68')), BIN_TO_UUID(X'7f9d04ae61b34468ac798ffcc984ab68', TRUE); `` | 通过 | 返回 1 行 | — | — |
| 278 | `` SELECT BIN_TO_UUID(UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db', 0), 1), BIN_TO_UUID(UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db', 1), 0); `` | 通过 | 返回 1 行 | — | — |
| 279 | `` SELECT BIN_TO_UUID(NULL), BIN_TO_UUID(NULL, TRUE), UUID_TO_BIN(NULL), UUID_TO_BIN(NULL, TRUE), IS_UUID(NULL); `` | 通过 | 返回 1 行 | — | — |
| 280 | `` SELECT IS_UUID('{2345678-1234-5678-1234-567812345678}'), IS_UUID('9912345678123456781234567812345678'), IS_UUID('12345678-123456-78-1234-567812345678'); `` | 通过 | 返回 1 行 | — | — |
| 281 | `` SELECT BIN_TO_UUID(2); `` | 通过 | 数据库错误：BIN_TO_UUID 输入无效 | — | — |
| 282 | `` SELECT UUID_TO_BIN(2); `` | 通过 | 数据库错误：UUID_TO_BIN 输入无效 | — | — |
| 283 | `` SELECT BIN_TO_UUID(); `` | 通过 | 数据库错误：BIN_TO_UUID 参数数量无效 | — | — |
| 284 | `` SELECT UUID_TO_BIN(); `` | 通过 | 数据库错误：UUID_TO_BIN 参数数量无效 | — | — |
| 285 | `` SELECT BIN_TO_UUID(X'12345678123456781234567812345678', TRUE, FALSE); `` | 通过 | 数据库错误：BIN_TO_UUID 参数数量无效 | — | — |
| 286 | `` SELECT UUID_TO_BIN('12345678-1234-5678-1234-567812345678', TRUE, FALSE); `` | 通过 | 数据库错误：UUID_TO_BIN 参数数量无效 | — | — |
| 287 | `` SELECT BIN_TO_UUID(X''), BIN_TO_UUID(X'', TRUE); `` | 通过 | 数据库错误：BIN_TO_UUID 空值无效 | — | — |
| 288 | `` SELECT UUID_TO_BIN(''), UUID_TO_BIN('', TRUE); `` | 通过 | 数据库错误：UUID_TO_BIN 空值无效 | — | — |
| 289 | `` SELECT BIN_TO_UUID(UUID_TO_BIN(@uuid_native)) = @uuid_native; `` | 通过 | 返回 1 行 | — | — |
| 290 | `` SELECT BIN_TO_UUID(id), BIN_TO_UUID(id, TRUE), UUID_TO_BIN(BIN_TO_UUID(id)) = id FROM uuid_binary_rows; `` | 通过 | 返回 1 行 | — | — |
| 291 | `` SELECT BIN_TO_UUID(bin_value), BIN_TO_UUID(varbin_value), BIN_TO_UUID(tinyblob_value), BIN_TO_UUID(tinytext_value), BIN_TO_UUID(blob_value) FROM uuid_storage_types; `` | 通过 | 返回 1 行 | — | — |
| 292 | `` EXPLAIN SELECT uuid_text, HEX(uuid_binary) FROM uuid_text_generated WHERE UUID_TO_BIN(uuid_text) = X'12345679123456781234567812345678'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 293 | `` SELECT uuid_text, HEX(uuid_binary) FROM uuid_text_generated WHERE uuid_binary = X'12345679123456781234567812345678'; `` | 通过 | 返回 1 行 | — | — |
| 294 | `` EXPLAIN SELECT HEX(uuid_binary), uuid_text FROM uuid_binary_generated WHERE BIN_TO_UUID(uuid_binary) = '12345679-1234-5678-1234-567812345678'; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 295 | `` SELECT HEX(uuid_binary), uuid_text FROM uuid_binary_generated WHERE uuid_text = '12345679-1234-5678-1234-567812345678'; `` | 通过 | 返回 1 行 | — | — |
| 296 | `` SELECT HEX(WEIGHT_STRING(JSON_UNQUOTE(JSON_SET('{}','$','')))); `` | 通过 | 返回 1 行 | — | — |
| 297 | `` SELECT REGEXP_INSTR('abcabc','b',1); `` | 通过 | 返回 1 行 | — | — |
| 298 | `` SELECT REGEXP_INSTR('abcabc','b',1,2); `` | 通过 | 返回 1 行 | — | — |
| 299 | `` SELECT REGEXP_INSTR('abcabc','b',1,2,0); `` | 通过 | 返回 1 行 | — | — |
| 300 | `` SELECT REGEXP_REPLACE('abcabc','b','X',1); `` | 通过 | 返回 1 行 | — | — |
| 301 | `` SELECT REGEXP_REPLACE('abcabc','b','X',1,0); `` | 通过 | 返回 1 行 | — | — |
| 302 | `` SELECT REGEXP_SUBSTR('abc123','[0-9]+',1); `` | 通过 | 返回 1 行 | — | — |
| 303 | `` SELECT REGEXP_SUBSTR('abc123','[0-9]+',1,1); `` | 通过 | 返回 1 行 | — | — |
| 304 | `` SELECT AES_DECRYPT(AES_ENCRYPT('secret','password','','hkdf','salt','context'),'password','','hkdf','salt','context'); `` | 通过 | 返回 1 行 | — | — |
| 305 | `` SELECT AES_DECRYPT(AES_ENCRYPT('secret','password','','pbkdf2_hmac','salt',1000),'password','','pbkdf2_hmac','salt',1000); `` | 通过 | 返回 1 行 | — | — |
| 306 | `` SELECT ANY_VALUE(1), ANY_VALUE('text'); `` | 通过 | 返回 1 行 | — | — |
| 307 | `` SELECT asynchronous_connection_failover_add_source('split_failover','127.0.0.1',3306,'',50); `` | 通过 | 返回 1 行 | — | — |
| 308 | `` SELECT asynchronous_connection_failover_delete_source('split_failover','127.0.0.1',3306,''); `` | 通过 | 返回 1 行 | — | — |
| 309 | `` SELECT asynchronous_connection_failover_add_managed('split_failover','GroupReplication','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','127.0.0.1',3306,'',80,60); `` | 通过 | 返回 1 行 | — | — |
| 310 | `` SELECT asynchronous_connection_failover_delete_managed('split_failover','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'); `` | 通过 | 返回 1 行 | — | — |
| 311 | `` SELECT audit_log_encryption_password_get(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 312 | `` SELECT audit_log_encryption_password_set('split_password'); `` | 未执行 | 待用户亲自执行：修改审计日志加密密码，未计入已测 | — | — |
| 313 | `` SELECT audit_log_filter_flush(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 314 | `` SELECT audit_log_filter_remove_filter('split_filter'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 315 | `` SELECT audit_log_filter_remove_user('split_user@%'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 316 | `` SELECT audit_log_filter_set_filter('split_filter','{"filter":{"log":true}}'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 317 | `` SELECT audit_log_filter_set_user('split_user@%','split_filter'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 318 | `` SELECT audit_log_read('{"max_array_length":5}'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 319 | `` SELECT audit_log_read_bookmark(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 320 | `` SELECT audit_log_rotate(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 321 | `` SELECT audit_api_message_emit_udf('split_component','split_producer','split message'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 322 | `` SELECT audit_api_message_emit_udf('split_component','split_producer','split message','key1','value1','key2',123,'key3',NULL); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 323 | `` SELECT mysql_query_attribute_string('split_attribute'), mysql_query_attribute_string(''); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 324 | `` SELECT gen_blacklist('Munich','DE_Cities','US_Cities'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 325 | `` SELECT gen_blocklist('Munich','DE_Cities','US_Cities'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 326 | `` SELECT gen_dictionary('split_dict'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 327 | `` SELECT gen_dictionary_drop('split_dict'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 328 | `` SELECT gen_dictionary_load('/tmp/split_dictionary.txt','split_dict'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 329 | `` SELECT gen_range(1,10); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 330 | `` SELECT gen_rnd_email(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 331 | `` SELECT gen_rnd_pan(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 332 | `` SELECT gen_rnd_ssn(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 333 | `` SELECT gen_rnd_us_phone(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 334 | `` SELECT mask_inner('This is a string',5,1); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 335 | `` SELECT mask_outer('This is a string',5,1); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 336 | `` SELECT mask_pan('4111111111111111'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 337 | `` SELECT mask_pan_relaxed('4111111111111111'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 338 | `` SELECT mask_ssn('123-45-6789'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 339 | `` SELECT gen_rnd_canada_sin(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 340 | `` SELECT gen_rnd_iban(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 341 | `` SELECT gen_rnd_uk_nin(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 342 | `` SELECT gen_rnd_uuid(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 343 | `` SELECT mask_canada_sin('123-456-789'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 344 | `` SELECT mask_iban('GB82WEST12345698765432'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 345 | `` SELECT mask_uk_nin('AA123456C'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 346 | `` SELECT mask_uuid('6ccd780c-baba-1026-9564-5b8c656024db'); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 347 | `` SELECT masking_dictionaries_flush(); `` | 通过 | 数据库错误：当前容器未安装对应 UDF | — | — |
| 348 | `` SELECT masking_dictionary_remove('split_dict'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 349 | `` SELECT masking_dictionary_term_add('split_dict','split_term'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 350 | `` SELECT masking_dictionary_term_remove('split_dict','split_term'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 351 | `` SELECT keyring_aws_rotate_cmk(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 352 | `` SELECT keyring_aws_rotate_keys(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 353 | `` SELECT keyring_hashicorp_update_config(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 354 | `` SELECT firewall_group_delist('split_group','fwuser@localhost'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 355 | `` SELECT firewall_group_enlist('split_group','fwuser@localhost'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 356 | `` SELECT read_firewall_group_allowlist('split_group','SELECT 1'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 357 | `` SELECT read_firewall_groups('split_group','RECORDING','fwuser@localhost'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 358 | `` SELECT set_firewall_group_mode('split_group','DETECTING','fwuser@localhost'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 359 | `` SELECT GET_DD_CREATE_OPTIONS(NULL,NULL,NULL), GET_DD_SCHEMA_OPTIONS(NULL), GET_DD_TABLESPACE_PRIVATE_DATA(NULL,NULL), GET_DD_INDEX_PRIVATE_DATA(NULL,NULL), INTERNAL_DD_CHAR_LENGTH(NULL,NULL,NULL,NULL), CAN_ACCESS_DATABASE(NULL), CAN_ACCESS_TABLE(NULL,NULL), CAN_ACCESS_COLUMN(NULL,NULL,NULL), CAN_ACCESS_VIEW(NULL,NULL,NULL,NULL), CAN_ACCESS_TRIGGER(NULL,NULL), CAN_ACCESS_ROUTINE(NULL,NULL,NULL,NULL,NULL), CAN_ACCESS_EVENT(NULL), CAN_ACCESS_USER(NULL,NULL), CAN_ACCESS_RESOURCE_GROUP(NULL), CONVERT_CPU_ID_MASK(NULL), IS_VISIBLE_DD_OBJECT(NULL), IS_VISIBLE_DD_OBJECT(NULL,NULL,NULL); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 360 | `` SELECT INTERNAL_TABLE_ROWS(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_AVG_ROW_LENGTH(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_DATA_LENGTH(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_MAX_DATA_LENGTH(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_INDEX_LENGTH(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_DATA_FREE(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_AUTO_INCREMENT(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_CHECKSUM(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_UPDATE_TIME(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_CHECK_TIME(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_KEYS_DISABLED(NULL), INTERNAL_INDEX_COLUMN_CARDINALITY(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_GET_COMMENT_OR_ERROR(NULL,NULL,NULL,NULL,NULL), INTERNAL_GET_VIEW_WARNING_OR_ERROR(NULL,NULL,NULL,NULL), INTERNAL_GET_PARTITION_NODEGROUP(NULL); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 361 | `` SELECT INTERNAL_TABLESPACE_ID(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_TYPE(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_LOGFILE_GROUP_NAME(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_LOGFILE_GROUP_NUMBER(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_FREE_EXTENTS(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_TOTAL_EXTENTS(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_EXTENT_SIZE(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_INITIAL_SIZE(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_MAXIMUM_SIZE(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_AUTOEXTEND_SIZE(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_VERSION(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_ROW_FORMAT(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_DATA_FREE(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_STATUS(NULL,NULL,NULL,NULL), INTERNAL_TABLESPACE_EXTRA(NULL,NULL,NULL,NULL); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 362 | `` SELECT GET_DD_PROPERTY_KEY_VALUE(NULL,NULL), REMOVE_DD_PROPERTY_KEY(NULL,NULL), CONVERT_INTERVAL_TO_USER_INTERVAL(NULL,NULL), INTERNAL_GET_DD_COLUMN_EXTRA(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), INTERNAL_GET_USERNAME(), INTERNAL_GET_USERNAME(NULL), INTERNAL_GET_HOSTNAME(), INTERNAL_GET_HOSTNAME(NULL), INTERNAL_GET_ENABLED_ROLE_JSON(), INTERNAL_GET_MANDATORY_ROLES_JSON(), INTERNAL_IS_MANDATORY_ROLE(NULL,NULL), INTERNAL_IS_ENABLED_ROLE(NULL,NULL); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 363 | `` SELECT INTERNAL_USE_TERMINOLOGY_PREVIOUS(); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 364 | `` SELECT keyring_key_store('split_stored_key','AES','0123456789abcdef'); `` | 通过 | 数据库错误：未安装 keyring UDF | — | — |
| 365 | `` SELECT HEX(keyring_key_fetch('split_stored_key')), keyring_key_type_fetch('split_stored_key'), keyring_key_length_fetch('split_stored_key'); `` | 通过 | 数据库错误：未安装 keyring UDF | — | — |
| 366 | `` SELECT keyring_key_generate('split_generated_key','AES',16); `` | 通过 | 数据库错误：未安装 keyring UDF | — | — |
| 367 | `` SELECT keyring_key_type_fetch('split_generated_key'), keyring_key_length_fetch('split_generated_key'); `` | 通过 | 数据库错误：未安装 keyring UDF | — | — |
| 368 | `` SELECT keyring_key_remove('split_stored_key'), keyring_key_remove('split_generated_key'); `` | 通过 | 数据库错误：未安装 keyring UDF | — | — |
| 369 | `` SELECT GET_LOCK('split_function_lock_a', 0), GET_LOCK('split_function_lock_b', 0), RELEASE_ALL_LOCKS(); `` | 通过 | 返回 1 行 | — | — |
| 370 | `` SELECT service_get_read_locks('split_read','r1','r2',0), service_get_write_locks('split_write','w1',0), service_release_locks('split_read'), service_release_locks('split_write'); `` | 通过 | 数据库错误：未安装 service lock UDF | — | — |
| 371 | `` SELECT MBREQUALS(ST_GEOMFROMTEXT('POINT(1 1)'),ST_GEOMFROMTEXT('POINT(1 1)')), MBRCOVEREDBY(ST_GEOMFROMTEXT('POINT(1 1)'),ST_GEOMFROMTEXT('POLYGON((0 0,0 2,2 2,2 0,0 0))')), MBRCOVERS(ST_GEOMFROMTEXT('POLYGON((0 0,0 2,2 2,2 0,0 0))'),ST_GEOMFROMTEXT('POINT(1 1)')); `` | 通过 | 返回 1 行 | — | — |
| 372 | `` SELECT REGEXP_LIKE('abc','^a','c'), REGEXP_INSTR('abcabc','b',1,2,0,'c'), REGEXP_REPLACE('abcabc','b','X',1,0,'c'), REGEXP_SUBSTR('abc123','[0-9]+',1,1,'c'); `` | 通过 | 返回 1 行 | — | — |
| 373 | `` SELECT STATEMENT_DIGEST('SELECT 1'), STATEMENT_DIGEST_TEXT('SELECT  1 + 2'), UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db'), BIN_TO_UUID(UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db')), IS_UUID('6ccd780c-baba-1026-9564-5b8c656024db'); `` | 通过 | 返回 1 行 | — | — |
| 374 | `` SELECT CURRENT_ROLE(), ROLES_GRAPHML(), ICU_VERSION(), FORMAT_BYTES(123456789), FORMAT_PICO_TIME(123456789); `` | 通过 | 返回 1 行 | — | — |
| 375 | `` SELECT SOURCE_POS_WAIT('missing-binlog.000001', 4, 0); `` | 通过 | 返回 1 行 | — | — |
| 376 | `` SELECT WAIT_FOR_EXECUTED_GTID_SET('', 0); `` | 通过 | 数据库错误：GTID_MODE = OFF | — | — |
| 377 | `` SELECT WAIT_FOR_EXECUTED_GTID_SET(''); `` | 通过 | 数据库错误：GTID_MODE = OFF | — | — |
| 378 | `` SELECT SOURCE_POS_WAIT('missing-binlog.000001',4); `` | 通过 | 返回 1 行 | — | — |
| 379 | `` SELECT SOURCE_POS_WAIT('missing-binlog.000001',4,0,'missing_channel'); `` | 通过 | 返回 1 行 | — | — |
| 380 | `` SELECT load_rewrite_rules(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 381 | `` SELECT version_tokens_set('alpha=one;beta=two'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 382 | `` SELECT version_tokens_show(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 383 | `` SELECT version_tokens_edit('alpha=updated;gamma=three'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 384 | `` SELECT version_tokens_delete('beta'); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 385 | `` SELECT version_tokens_lock_shared('alpha',0), version_tokens_unlock(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 386 | `` SELECT version_tokens_lock_exclusive('gamma',0), version_tokens_unlock(); `` | 通过 | 数据库错误：未安装对应 UDF | — | — |
| 387 | `` SELECT BIN_TO_UUID(UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db',0),0), BIN_TO_UUID(UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db',1),1); `` | 通过 | 返回 1 行 | — | — |
| 388 | `` SELECT IS_UUID('6ccd780c-baba-1026-9564-5b8c656024db'), IS_UUID('{6ccd780c-baba-1026-9564-5b8c656024db}'), IS_UUID('6ccd780cbaba102695645b8c656024db'), IS_UUID('not-a-uuid'), IS_UUID(NULL); `` | 通过 | 返回 1 行 | — | — |
| 389 | `` SELECT a,b,SUM(v) AS total,GROUPING(a,b) AS grp_mask FROM (SELECT 1 AS a,1 AS b,10 AS v UNION ALL SELECT 1,2,20) AS d GROUP BY a,b WITH ROLLUP HAVING GROUPING(a,b) >= 0 ORDER BY GROUPING(a,b),a,b; `` | 通过 | 返回 4 行 | — | — |
| 390 | `` SELECT mysql.CONTAINS(1,2); `` | 通过 | 数据库错误：对应函数不存在 | — | — |
| 391 | `` SELECT mysql.SRID(1); `` | 通过 | 数据库错误：对应函数不存在 | — | — |
| 392 | `` SELECT OLD_PASSWORD('Select84!'); `` | 通过 | 数据库错误：对应函数不存在 | — | — |
| 393 | `` SELECT GET_DD_COLUMN_PRIVILEGES(NULL, NULL, NULL); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 394 | `` SELECT GET_DD_INDEX_SUB_PART_LENGTH(NULL, NULL, NULL, NULL, NULL); `` | 通过 | 数据库错误：内部原生函数访问被拒绝 | — | — |
| 395 | `` SELECT id, name, age, height FROM f7_t1 WHERE ETAG(id, age) > '13647057490375175604'; `` | 通过 | 数据库错误：对应 ETAG/向量 UDF 未安装 | — | — |
| 396 | `` SELECT department, COUNT(*), AVG(salary), ETAG(department, FORMAT(AVG(salary), 2)) FROM f8_employees GROUP BY department HAVING AVG(salary) > 50000; `` | 通过 | 数据库错误：对应 ETAG/向量 UDF 未安装 | — | — |
| 397 | `` SELECT pk, VECTOR_TO_STRING(v) FROM f9_t; `` | 通过 | 数据库错误：对应 ETAG/向量 UDF 未安装 | — | — |
| 398 | `` SELECT pk, VECTOR_DIM(embedding) FROM f12_tb ORDER BY pk; `` | 通过 | 数据库错误：对应 ETAG/向量 UDF 未安装 | — | — |
| 399 | `` SELECT FROM_VECTOR(embedding) FROM f13_ta; `` | 通过 | 数据库错误：对应 ETAG/向量 UDF 未安装 | — | — |
| 400 | `` SELECT RELEASE_ALL_LOCKS('test'); `` | 通过 | 数据库错误：RELEASE_ALL_LOCKS 参数数量无效 | — | — |
| 401 | `` SELECT asynchronous_connection_failover_reset(); `` | 通过 | 返回 1 行 | — | — |
| 402 | `` SELECT REGEXP_INSTR(?,?,?,?,?); `` | 通过 | 数据库错误：第 1 个占位参数未绑定 | — | — |
| 403 | `` SELECT audit_log_read('{"max_array_length":1}'), set_firewall_mode('fwuser@localhost','RECORDING') FROM split_management_function_probe; `` | 通过 | 数据库错误：审计 UDF 未安装 | — | — |
| 404 | `` SELECT DOC_ID FROM t2 WHERE MATCH(title, text) AGAINST ('+database' IN BOOLEAN MODE) * 100; `` | 通过 | 数据库错误：全文评分溢出 DOUBLE | — | — |
| 405 | `` SELECT FTS_DOC_ID, MATCH(title, text) AGAINST ("data*" IN BOOLEAN MODE) * 100 FROM t1 WHERE MATCH(title, text) AGAINST ("data*" IN BOOLEAN MODE) * 100; `` | 通过 | 返回 0 行 | — | — |
| 406 | `` SELECT x, MATCH (x) AGAINST ('abc')+1 AS score FROM t GROUP BY x; `` | 通过 | 数据库错误：全文评分溢出 DOUBLE | — | — |
| 407 | `` select * from t1 where match a against ("te*" in boolean mode)+0; `` | 通过 | 返回 0 行 | — | — |
| 408 | `` SELECT GTID_SUBSET(GTID_SUBTRACT('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-9', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:5-9'), 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-9') AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 409 | `` EXPLAIN FORMAT = JSON SELECT GTID_SUBSET(GTID_SUBTRACT('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-9', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:5-9'), 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-9') AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 410 | `` SET @pika_result := GTID_SUBSET(GTID_SUBTRACT('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-9', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:5-9'), 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-9'); `` | 通过 | 0 rows affected | — | — |
| 411 | `` SELECT WAIT_FOR_EXECUTED_GTID_SET(CONCAT('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:', '1'), 1) AS pika_result; `` | 通过 | 数据库错误：GTID_MODE = OFF | — | — |
| 412 | `` EXPLAIN FORMAT = JSON SELECT WAIT_FOR_EXECUTED_GTID_SET(CONCAT('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:', '1'), 1) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 413 | `` SET @pika_result := WAIT_FOR_EXECUTED_GTID_SET(CONCAT('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:', '1'), 1); `` | 通过 | 数据库错误：GTID_MODE = OFF | — | — |
| 414 | `` SELECT a,b,c,GROUPING(a,b,c) FROM (SELECT 1 AS a,2 AS b,3 AS c) AS d GROUP BY a,b,c WITH ROLLUP; `` | 通过 | 返回 4 行 | — | — |
| 415 | `` SELECT * FROM JSON_TABLE('{"a":[10,20]}','$' COLUMNS(NESTED PATH '$.a[*]' COLUMNS(ord FOR ORDINALITY,v INT PATH '$'))) AS jt; `` | 通过 | 返回 2 行 | — | — |
| 416 | `` SELECT * FROM JSON_TABLE('{"a":[{}]}','$' COLUMNS(NESTED PATH '$.a[*]' COLUMNS(v INT PATH '$.missing' DEFAULT '7' ON EMPTY NULL ON ERROR))) AS jt; `` | 通过 | 返回 1 行 | — | — |
| 417 | `` SELECT CAST(TIMESTAMP '2024-01-01 00:00:00' AT TIME ZONE '+00:00' AS DATETIME(0)); `` | 通过 | 返回 1 行 | — | — |
| 418 | `` SELECT CAST(1 AT LOCAL AS SIGNED); `` | 通过 | 数据库错误：MySQL 不支持 AT LOCAL | — | — |
| 419 | `` SELECT CAST(_gb2312 X'CAFD' AS NATIONAL CHAR); `` | 通过 | 返回 1 行 | — | — |
| 420 | `` SELECT CONVERT(_gb2312 X'CAFD',NATIONAL CHAR); `` | 通过 | 返回 1 行 | — | — |
| 421 | `` SELECT CAST(1 XOR 0 AT LOCAL AS SIGNED); `` | 通过 | 数据库错误：MySQL 不支持 AT LOCAL | — | — |
| 422 | `` ANALYZE TABLE t1 UPDATE HISTOGRAM ON x MANUAL UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 423 | `` ANALYZE TABLE t1 UPDATE HISTOGRAM ON x AUTO UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 424 | `` ANALYZE TABLE t1 UPDATE HISTOGRAM ON c2 MANUAL UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 425 | `` ANALYZE TABLE t1 UPDATE HISTOGRAM ON x WITH 3 BUCKETS AUTO UPDATE; `` | 通过 | 返回 1 行状态结果 | — | — |
| 426 | `` ANALYZE TABLE foo, foo2 UPDATE HISTOGRAM ON bar WITH 100 BUCKETS; `` | 通过 | 返回 1 行状态结果 | — | — |
| 427 | `` ANALYZE TABLE t1, t2 DROP HISTOGRAM ON col1; `` | 通过 | 返回 1 行状态结果 | — | — |
| 428 | `` GET DIAGNOSTICS CONDITION NULL @var = CLASS_ORIGIN; `` | 通过 | 0 rows affected | — | — |
| 429 | `` SELECT SHA2(CONCAT_WS(':', _utf8mb4'Pika;中', 12.5), 256) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 430 | `` EXPLAIN FORMAT = JSON SELECT SHA2(CONCAT_WS(':', _utf8mb4'Pika;中', 12.5), 256) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 431 | `` SET @pika_result := SHA2(CONCAT_WS(':', _utf8mb4'Pika;中', 12.5), 256); `` | 通过 | 0 rows affected | — | — |
| 432 | `` SELECT SHA2(CONCAT_WS(':', pika_o.pika_name, pika_o.pika_amount), 256) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 3 行 | — | — |
| 433 | `` EXPLAIN FORMAT = JSON SELECT SHA2(CONCAT_WS(':', pika_o.pika_name, pika_o.pika_amount), 256) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 1 行 | — | — |
| 434 | `` SELECT UNCOMPRESS(COMPRESS(CONCAT(_utf8mb4'Pika;中', ';pika'))) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 435 | `` EXPLAIN FORMAT = JSON SELECT UNCOMPRESS(COMPRESS(CONCAT(_utf8mb4'Pika;中', ';pika'))) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 436 | `` SET @pika_result := UNCOMPRESS(COMPRESS(CONCAT(_utf8mb4'Pika;中', ';pika'))); `` | 通过 | 0 rows affected | — | — |
| 437 | `` SELECT UNCOMPRESS(COMPRESS(CONCAT(pika_o.pika_name, ';pika'))) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 3 行 | — | — |
| 438 | `` EXPLAIN FORMAT = JSON SELECT UNCOMPRESS(COMPRESS(CONCAT(pika_o.pika_name, ';pika'))) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 1 行 | — | — |
| 439 | `` SELECT UNCOMPRESSED_LENGTH(COMPRESS(_utf8mb4'Pika;中')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 440 | `` EXPLAIN FORMAT = JSON SELECT UNCOMPRESSED_LENGTH(COMPRESS(_utf8mb4'Pika;中')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 441 | `` SET @pika_result := UNCOMPRESSED_LENGTH(COMPRESS(_utf8mb4'Pika;中')); `` | 通过 | 0 rows affected | — | — |
| 442 | `` SELECT UNCOMPRESSED_LENGTH(COMPRESS(pika_o.pika_name)) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 3 行 | — | — |
| 443 | `` EXPLAIN FORMAT = JSON SELECT UNCOMPRESSED_LENGTH(COMPRESS(pika_o.pika_name)) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 1 行 | — | — |
| 444 | `` SELECT HEX(AES_ENCRYPT(_utf8mb4'Pika;中', 'pika_fixture_key')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 445 | `` EXPLAIN FORMAT = JSON SELECT HEX(AES_ENCRYPT(_utf8mb4'Pika;中', 'pika_fixture_key')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 446 | `` SET @pika_result := HEX(AES_ENCRYPT(_utf8mb4'Pika;中', 'pika_fixture_key')); `` | 通过 | 0 rows affected | — | — |
| 447 | `` SELECT HEX(AES_ENCRYPT(pika_o.pika_name, 'pika_fixture_key')) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 3 行 | — | — |
| 448 | `` EXPLAIN FORMAT = JSON SELECT HEX(AES_ENCRYPT(pika_o.pika_name, 'pika_fixture_key')) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 1 行 | — | — |
| 449 | `` SELECT CONVERT(AES_DECRYPT(AES_ENCRYPT(_utf8mb4'Pika;中', 'pika_fixture_key'), 'pika_fixture_key') USING utf8mb4) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 450 | `` EXPLAIN FORMAT = JSON SELECT CONVERT(AES_DECRYPT(AES_ENCRYPT(_utf8mb4'Pika;中', 'pika_fixture_key'), 'pika_fixture_key') USING utf8mb4) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 451 | `` SET @pika_result := CONVERT(AES_DECRYPT(AES_ENCRYPT(_utf8mb4'Pika;中', 'pika_fixture_key'), 'pika_fixture_key') USING utf8mb4); `` | 通过 | 0 rows affected | — | — |
| 452 | `` SELECT CONVERT(AES_DECRYPT(AES_ENCRYPT(pika_o.pika_name, 'pika_fixture_key'), 'pika_fixture_key') USING utf8mb4) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 3 行 | — | — |
| 453 | `` EXPLAIN FORMAT = JSON SELECT CONVERT(AES_DECRYPT(AES_ENCRYPT(pika_o.pika_name, 'pika_fixture_key'), 'pika_fixture_key') USING utf8mb4) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 1 行 | — | — |
| 454 | `` SELECT CONCAT(MD5(_utf8mb4'Pika;中'), SHA1(_utf8mb4'Pika;中'), SHA(_utf8mb4'Pika;中')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 455 | `` EXPLAIN FORMAT = JSON SELECT CONCAT(MD5(_utf8mb4'Pika;中'), SHA1(_utf8mb4'Pika;中'), SHA(_utf8mb4'Pika;中')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 456 | `` SET @pika_result := CONCAT(MD5(_utf8mb4'Pika;中'), SHA1(_utf8mb4'Pika;中'), SHA(_utf8mb4'Pika;中')); `` | 通过 | 0 rows affected | — | — |
| 457 | `` SELECT CONCAT(MD5(pika_o.pika_name), SHA1(pika_o.pika_name), SHA(pika_o.pika_name)) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 3 行 | — | — |
| 458 | `` EXPLAIN FORMAT = JSON SELECT CONCAT(MD5(pika_o.pika_name), SHA1(pika_o.pika_name), SHA(pika_o.pika_name)) AS pika_result FROM pika_schema.pika_orders AS pika_o WHERE pika_o.pika_id > 0 ORDER BY pika_o.pika_id LIMIT 5; `` | 通过 | 返回 1 行 | — | — |
| 459 | `` SELECT LENGTH(RANDOM_BYTES(16)) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 460 | `` EXPLAIN FORMAT = JSON SELECT LENGTH(RANDOM_BYTES(16)) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 461 | `` SET @pika_result := LENGTH(RANDOM_BYTES(16)); `` | 通过 | 0 rows affected | — | — |
| 462 | `` SELECT STATEMENT_DIGEST(CONCAT('SELECT ', '1 /* pika */')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 463 | `` EXPLAIN FORMAT = JSON SELECT STATEMENT_DIGEST(CONCAT('SELECT ', '1 /* pika */')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 464 | `` SET @pika_result := STATEMENT_DIGEST(CONCAT('SELECT ', '1 /* pika */')); `` | 通过 | 0 rows affected | — | — |
| 465 | `` SELECT STATEMENT_DIGEST_TEXT(CONCAT('SELECT ', '1 + 2')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 466 | `` EXPLAIN FORMAT = JSON SELECT STATEMENT_DIGEST_TEXT(CONCAT('SELECT ', '1 + 2')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 467 | `` SET @pika_result := STATEMENT_DIGEST_TEXT(CONCAT('SELECT ', '1 + 2')); `` | 通过 | 0 rows affected | — | — |
| 468 | `` SELECT BIN_TO_UUID(UUID_TO_BIN('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', 1), 1) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 469 | `` EXPLAIN FORMAT = JSON SELECT BIN_TO_UUID(UUID_TO_BIN('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', 1), 1) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 470 | `` SET @pika_result := BIN_TO_UUID(UUID_TO_BIN('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', 1), 1); `` | 通过 | 0 rows affected | — | — |
| 471 | `` SELECT IS_UUID(UUID()) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 472 | `` EXPLAIN FORMAT = JSON SELECT IS_UUID(UUID()) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 473 | `` SET @pika_result := IS_UUID(UUID()); `` | 通过 | 0 rows affected | — | — |
| 474 | `` SELECT CONCAT('pika_', UUID_SHORT()) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 475 | `` EXPLAIN FORMAT = JSON SELECT CONCAT('pika_', UUID_SHORT()) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 476 | `` SET @pika_result := CONCAT('pika_', UUID_SHORT()); `` | 通过 | 0 rows affected | — | — |
| 477 | `` SELECT INET_NTOA(INET_ATON('127.0.0.1')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 478 | `` EXPLAIN FORMAT = JSON SELECT INET_NTOA(INET_ATON('127.0.0.1')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 479 | `` SET @pika_result := INET_NTOA(INET_ATON('127.0.0.1')); `` | 通过 | 0 rows affected | — | — |
| 480 | `` SELECT INET6_NTOA(INET6_ATON('2001:db8::1')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 481 | `` EXPLAIN FORMAT = JSON SELECT INET6_NTOA(INET6_ATON('2001:db8::1')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 482 | `` SET @pika_result := INET6_NTOA(INET6_ATON('2001:db8::1')); `` | 通过 | 0 rows affected | — | — |
| 483 | `` SELECT IS_IPV4('127.0.0.1') + IS_IPV6('::1') AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 484 | `` EXPLAIN FORMAT = JSON SELECT IS_IPV4('127.0.0.1') + IS_IPV6('::1') AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 485 | `` SET @pika_result := IS_IPV4('127.0.0.1') + IS_IPV6('::1'); `` | 通过 | 0 rows affected | — | — |
| 486 | `` SELECT IS_IPV4_COMPAT(INET6_ATON('::127.0.0.1')) + IS_IPV4_MAPPED(INET6_ATON('::ffff:127.0.0.1')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 487 | `` EXPLAIN FORMAT = JSON SELECT IS_IPV4_COMPAT(INET6_ATON('::127.0.0.1')) + IS_IPV4_MAPPED(INET6_ATON('::ffff:127.0.0.1')) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 488 | `` SET @pika_result := IS_IPV4_COMPAT(INET6_ATON('::127.0.0.1')) + IS_IPV4_MAPPED(INET6_ATON('::ffff:127.0.0.1')); `` | 通过 | 0 rows affected | — | — |
| 489 | `` SELECT COALESCE(SLEEP(0), 0) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 490 | `` EXPLAIN FORMAT = JSON SELECT COALESCE(SLEEP(0), 0) AS pika_result; `` | 通过 | 返回 1 行 | — | — |
| 491 | `` SET @pika_result := COALESCE(SLEEP(0), 0); `` | 通过 | 0 rows affected | — | — |
| 492 | `` SET @pika_result := (SELECT COALESCE(ROUND((SELECT MAX(pika_i.pika_amount) FROM pika_schema.pika_items pika_i WHERE pika_i.pika_id = pika_o.pika_id), 2), pika_o.pika_amount) AS pika_result FROM pika_schema.pika_orders pika_o LIMIT 1); `` | 通过 | 数据库错误：pika_items 缺少 pika_amount 列 | — | — |
| 493 | `` SET @pika_result := (SELECT COALESCE(ROUND((SELECT SUM(pika_i.pika_amount) FROM pika_schema.pika_items pika_i WHERE pika_i.pika_id = pika_o.pika_id), 2), pika_o.pika_amount) AS pika_result FROM pika_schema.pika_orders pika_o LIMIT 1); `` | 通过 | 数据库错误：pika_items 缺少 pika_amount 列 | — | — |
| 494 | `` SET @pika_result := (SELECT COALESCE(ROUND((SELECT AVG(pika_i.pika_amount) FROM pika_schema.pika_items pika_i WHERE pika_i.pika_id = pika_o.pika_id), 2), pika_o.pika_amount) AS pika_result FROM pika_schema.pika_orders pika_o LIMIT 1); `` | 通过 | 数据库错误：pika_items 缺少 pika_amount 列 | — | — |
| 495 | `` SET SESSION wait_timeout = GREATEST(60, LEAST(300, ROUND(120.5, 0))); `` | 通过 | 数据库错误：wait_timeout 参数类型不符 | — | — |
| 496 | `` SET SESSION wait_timeout = CAST(JSON_VALUE('{"pika":120}', '$.pika' RETURNING UNSIGNED) AS UNSIGNED); `` | 通过 | 0 rows affected | — | — |
| 497 | `` SET SESSION wait_timeout = IF(CONNECTION_ID() > 0, 120, 60); `` | 通过 | 0 rows affected | — | — |
| 498 | `` SET LOCAL wait_timeout = GREATEST(60, LEAST(300, ROUND(120.5, 0))); `` | 通过 | 数据库错误：wait_timeout 参数类型不符 | — | — |
| 499 | `` SET LOCAL wait_timeout = CAST(JSON_VALUE('{"pika":120}', '$.pika' RETURNING UNSIGNED) AS UNSIGNED); `` | 通过 | 0 rows affected | — | — |
| 500 | `` SET LOCAL wait_timeout = IF(CONNECTION_ID() > 0, 120, 60); `` | 通过 | 0 rows affected | — | — |
| 501 | `` SET @@SESSION.wait_timeout = GREATEST(60, LEAST(300, ROUND(120.5, 0))); `` | 通过 | 数据库错误：wait_timeout 参数类型不符 | — | — |
| 502 | `` SET @@SESSION.wait_timeout = CAST(JSON_VALUE('{"pika":120}', '$.pika' RETURNING UNSIGNED) AS UNSIGNED); `` | 通过 | 0 rows affected | — | — |
| 503 | `` SET @@SESSION.wait_timeout = IF(CONNECTION_ID() > 0, 120, 60); `` | 通过 | 0 rows affected | — | — |
| 504 | `` SET GLOBAL wait_timeout = GREATEST(60, LEAST(300, ROUND(120.5, 0))); `` | 通过 | 数据库错误：wait_timeout 参数类型不符 | — | — |
| 505 | `` SET GLOBAL wait_timeout = CAST(JSON_VALUE('{"pika":120}', '$.pika' RETURNING UNSIGNED) AS UNSIGNED); `` | 通过 | 0 rows affected | — | — |
| 506 | `` SET GLOBAL wait_timeout = IF(CONNECTION_ID() > 0, 120, 60); `` | 通过 | 0 rows affected | — | — |
| 507 | `` SET PERSIST wait_timeout = GREATEST(60, LEAST(300, ROUND(120.5, 0))); `` | 通过 | 数据库错误：wait_timeout 参数类型不符 | — | — |
| 508 | `` SET PERSIST wait_timeout = CAST(JSON_VALUE('{"pika":120}', '$.pika' RETURNING UNSIGNED) AS UNSIGNED); `` | 通过 | 0 rows affected | — | — |
| 509 | `` SET PERSIST wait_timeout = IF(CONNECTION_ID() > 0, 120, 60); `` | 通过 | 0 rows affected | — | — |
| 510 | `` SET PERSIST_ONLY wait_timeout = GREATEST(60, LEAST(300, ROUND(120.5, 0))); `` | 通过 | 数据库错误：wait_timeout 参数类型不符 | — | — |
| 511 | `` SET PERSIST_ONLY wait_timeout = CAST(JSON_VALUE('{"pika":120}', '$.pika' RETURNING UNSIGNED) AS UNSIGNED); `` | 通过 | 0 rows affected | — | — |
| 512 | `` SET PERSIST_ONLY wait_timeout = IF(CONNECTION_ID() > 0, 120, 60); `` | 通过 | 0 rows affected | — | — |
| 513 | `` SHOW SESSION VARIABLES WHERE LOWER(Variable_name) LIKE CONCAT('pika', '%'); `` | 通过 | 返回 0 行 | — | — |
| 514 | `` SHOW SESSION VARIABLES WHERE REGEXP_LIKE(Variable_name, '^[a-z_]+$', 'i') AND CHAR_LENGTH(Variable_name) > 1; `` | 通过 | 返回 633 行 | — | — |
| 515 | `` SHOW SESSION VARIABLES WHERE COALESCE(NULLIF(Variable_name, ''), 'pika') = LOWER('PIKA'); `` | 通过 | 返回 0 行 | — | — |
| 516 | `` SHOW GLOBAL STATUS WHERE LOWER(Variable_name) LIKE CONCAT('pika', '%'); `` | 通过 | 返回 0 行 | — | — |
| 517 | `` SHOW GLOBAL STATUS WHERE REGEXP_LIKE(Variable_name, '^[a-z_]+$', 'i') AND CHAR_LENGTH(Variable_name) > 1; `` | 通过 | 返回 496 行 | — | — |
| 518 | `` SHOW GLOBAL STATUS WHERE COALESCE(NULLIF(Variable_name, ''), 'pika') = LOWER('PIKA'); `` | 通过 | 返回 0 行 | — | — |
| 519 | `` SHOW FULL COLUMNS FROM pika_schema.pika_orders WHERE LOWER(Field) LIKE CONCAT('pika', '%'); `` | 通过 | 返回 3 行 | — | — |
| 520 | `` SHOW FULL COLUMNS FROM pika_schema.pika_orders WHERE REGEXP_LIKE(Field, '^[a-z_]+$', 'i') AND CHAR_LENGTH(Field) > 1; `` | 通过 | 返回 3 行 | — | — |
| 521 | `` SHOW FULL COLUMNS FROM pika_schema.pika_orders WHERE COALESCE(NULLIF(Field, ''), 'pika') = LOWER('PIKA'); `` | 通过 | 返回 0 行 | — | — |
| 522 | `` SET GLOBAL max_connections = 200; `` | 通过 | 0 rows affected；已恢复 max_connections=151 | — | — |
| 523 | `` SET SESSION sql_mode = 'STRICT_TRANS_TABLES'; `` | 通过 | 0 rows affected；已恢复原 sql_mode | — | — |
| 524 | `` KILL QUERY @target_id; `` | 通过 | 数据库错误：线程 0 不存在 | — | — |
| 525 | `` FLUSH TABLES `schema1`.`orders`, `other_db`.`audit_log`; `` | 通过 | 0 rows affected | — | — |
| 526 | `` EXPLAIN ANALYZE SELECT * FROM orders; `` | 通过 | EXPLAIN 返回 1 行 | — | — |
| 527 | `` DESC ANALYZE SELECT * FROM orders; `` | 通过 | DESC 返回 1 行 | — | — |
| 528 | `` DESCRIBE ANALYZE SELECT * FROM orders; `` | 通过 | DESCRIBE 返回 1 行 | — | — |
| 529 | `` INSTALL /* dal boundary */ COMPONENT 'file://component_validate_password'; `` | 通过 | 0 rows affected；组件安装后已卸载，注册表恢复为空 | — | — |
| 530 | `` KILL /* dal boundary */ QUERY 12345; `` | 通过 | 数据库错误：线程 12345 不存在 | — | — |
| 531 | `` PURGE BINARY LOGS BEFORE DATE_SUB(NOW(), INTERVAL 7 DAY); `` | 通过 | 0 rows affected | — | — |
| 532 | `` PURGE BINARY LOGS BEFORE TIMESTAMP('2026-01-01 00:00:00'); `` | 通过 | 0 rows affected | — | — |
| 533 | `` PURGE BINARY LOGS BEFORE '2026-01-01 00:00:00' - INTERVAL 1 DAY; `` | 通过 | 0 rows affected | — | — |
| 534 | `` RESET /* persisted */ PERSIST max_connections; `` | 通过 | 数据库错误：max_connections 无持久化配置 | — | — |
| 535 | `` RESET /* persisted */ PERSIST IF EXISTS max_connections; `` | 通过 | 0 rows affected | — | — |
| 536 | `` RESET /* persisted */ PERSIST `max_connections`; `` | 通过 | 数据库错误：max_connections 无持久化配置 | — | — |
| 537 | `` RESET /* persisted */ PERSIST IF EXISTS `max_connections`; `` | 通过 | 0 rows affected | — | — |
| 538 | `` RESET /* persisted */ PERSIST `dal_cache`.`key_buffer_size`; `` | 通过 | 数据库错误：dal_cache.key_buffer_size 无持久化配置 | — | — |
| 539 | `` RESET /* persisted */ PERSIST IF EXISTS `dal_cache`.`key_buffer_size`; `` | 通过 | 0 rows affected | — | — |
| 540 | `` SET GLOBAL `dal cache`.key_buffer_size = 1024; `` | 通过 | 0 rows affected | — | — |
| 541 | `` SET @@GLOBAL.`dal cache`.key_buffer_size = 1024; `` | 通过 | 0 rows affected | — | — |
| 542 | `` SET @@`dal cache`.key_buffer_size = 1024; `` | 通过 | 数据库错误：key_buffer_size 为 GLOBAL 变量 | — | — |
| 543 | `` SET @@SESSION.`wait_timeout` = 1024; `` | 通过 | 0 rows affected | — | — |
| 544 | `` SET @@global_connection_memory_tracking = 1024; `` | 通过 | 数据库错误：global_connection_memory_tracking 不接受 1024 | — | — |
| 545 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal'; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 546 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' REQUIRE SSL; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 547 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' REQUIRE NO SSL; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 548 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' DATA DIRECTORY = '/fixtures/dal clone'; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 549 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' DATA DIRECTORY = '/fixtures/dal clone' REQUIRE SSL; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 550 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' DATA DIRECTORY = '/fixtures/dal clone' REQUIRE NO SSL; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 551 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' DATA DIRECTORY '/fixtures/dal;clone'; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 552 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' DATA DIRECTORY '/fixtures/dal;clone' REQUIRE SSL; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 553 | `` CLONE INSTANCE FROM 'dal_user'@'127.0.0.1':3306 IDENTIFIED BY 'fixture;--literal' DATA DIRECTORY '/fixtures/dal;clone' REQUIRE NO SSL; `` | 通过 | 数据库错误：Clone 插件未加载；没有克隆数据 | — | — |
| 554 | `` ALTER /* tls */ INSTANCE RELOAD TLS; `` | 通过 | 0 rows affected | — | — |
| 555 | `` ALTER /* tls */ INSTANCE RELOAD TLS NO ROLLBACK ON ERROR; `` | 通过 | 0 rows affected | — | — |
| 556 | `` ALTER /* tls */ INSTANCE RELOAD TLS FOR CHANNEL mysql_main; `` | 通过 | 0 rows affected | — | — |
| 557 | `` ALTER /* tls */ INSTANCE RELOAD TLS FOR CHANNEL mysql_main NO ROLLBACK ON ERROR; `` | 通过 | 0 rows affected | — | — |
| 558 | `` ALTER /* tls */ INSTANCE RELOAD TLS FOR CHANNEL mysql_admin; `` | 通过 | 0 rows affected | — | — |
| 559 | `` ALTER /* tls */ INSTANCE RELOAD TLS FOR CHANNEL mysql_admin NO ROLLBACK ON ERROR; `` | 通过 | 0 rows affected | — | — |
| 560 | `` KILL /* request */ CONNECTION_ID(); `` | 通过 | 当前连接被中断；刷新页面后 `SELECT 1;` 恢复 | — | — |
| 561 | `` KILL /* request */ (@dal_target + 0); `` | 通过 | 数据库错误：线程 0 不存在 | — | — |
| 562 | `` KILL /* request */ IF(1 = 1, 123, 456); `` | 通过 | 数据库错误：线程 123 不存在 | — | — |
| 563 | `` KILL /* request */ (SELECT MAX(id) FROM schema1.orders); `` | 通过 | 数据库错误：本版本不支持在 KILL 中使用子查询 | — | — |
| 564 | `` KILL /* request */ QUERY CONNECTION_ID(); `` | 通过 | 当前查询被中断，连接保留 | — | — |
| 565 | `` KILL /* request */ QUERY (@dal_target + 0); `` | 通过 | 数据库错误：线程 0 不存在 | — | — |
| 566 | `` KILL /* request */ QUERY IF(1 = 1, 123, 456); `` | 通过 | 数据库错误：线程 123 不存在 | — | — |
| 567 | `` KILL /* request */ QUERY (SELECT MAX(id) FROM schema1.orders); `` | 通过 | 数据库错误：本版本不支持在 KILL 中使用子查询 | — | — |
| 568 | `` KILL /* request */ CONNECTION CONNECTION_ID(); `` | 通过 | 当前连接被中断；刷新页面后恢复 | — | — |
| 569 | `` KILL /* request */ CONNECTION (@dal_target + 0); `` | 通过 | 数据库错误：线程 0 不存在 | — | — |
| 570 | `` KILL /* request */ CONNECTION IF(1 = 1, 123, 456); `` | 通过 | 数据库错误：线程 123 不存在 | — | — |
| 571 | `` KILL /* request */ CONNECTION (SELECT MAX(id) FROM schema1.orders); `` | 通过 | 数据库错误：本版本不支持在 KILL 中使用子查询 | — | — |
| 572 | `` LOAD INDEX INTO CACHE schema1.orders; `` | 通过 | 1 行结果 | — | — |
| 573 | `` LOAD INDEX INTO CACHE schema1.orders, other_db.audit_log KEY (PRIMARY) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 574 | `` LOAD INDEX INTO CACHE schema1.orders IGNORE LEAVES; `` | 通过 | 1 行结果 | — | — |
| 575 | `` LOAD INDEX INTO CACHE schema1.orders IGNORE LEAVES, other_db.audit_log KEY (PRIMARY) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 576 | `` LOAD INDEX INTO CACHE schema1.orders INDEX (PRIMARY, idx_status); `` | 通过 | 1 行结果 | — | — |
| 577 | `` LOAD INDEX INTO CACHE schema1.orders INDEX (PRIMARY, idx_status), other_db.audit_log KEY (PRIMARY) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 578 | `` LOAD INDEX INTO CACHE schema1.orders INDEX (PRIMARY, idx_status) IGNORE LEAVES; `` | 通过 | 1 行结果 | — | — |
| 579 | `` LOAD INDEX INTO CACHE schema1.orders INDEX (PRIMARY, idx_status) IGNORE LEAVES, other_db.audit_log KEY (PRIMARY) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 580 | `` LOAD INDEX INTO CACHE schema1.orders KEY (`idx status`); `` | 通过 | 1 行结果 | — | — |
| 581 | `` LOAD INDEX INTO CACHE schema1.orders KEY (`idx status`), other_db.audit_log KEY (PRIMARY) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 582 | `` LOAD INDEX INTO CACHE schema1.orders KEY (`idx status`) IGNORE LEAVES; `` | 通过 | 1 行结果 | — | — |
| 583 | `` LOAD INDEX INTO CACHE schema1.orders KEY (`idx status`) IGNORE LEAVES, other_db.audit_log KEY (PRIMARY) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 584 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (p0, `p 1`); `` | 通过 | 2 行结果 | — | — |
| 585 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (p0, `p 1`) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 586 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (p0, `p 1`) INDEX (PRIMARY, idx_status); `` | 通过 | 2 行结果 | — | — |
| 587 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (p0, `p 1`) INDEX (PRIMARY, idx_status) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 588 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (p0, `p 1`) KEY (`idx status`); `` | 通过 | 2 行结果 | — | — |
| 589 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (p0, `p 1`) KEY (`idx status`) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 590 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (ALL); `` | 通过 | 2 行结果 | — | — |
| 591 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (ALL) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 592 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (ALL) INDEX (PRIMARY, idx_status); `` | 通过 | 2 行结果 | — | — |
| 593 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (ALL) INDEX (PRIMARY, idx_status) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 594 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (ALL) KEY (`idx status`); `` | 通过 | 2 行结果 | — | — |
| 595 | `` LOAD INDEX INTO CACHE schema1.orders PARTITION (ALL) KEY (`idx status`) IGNORE LEAVES; `` | 通过 | 2 行结果 | — | — |
| 596 | `` CACHE INDEX schema1.orders INDEX (PRIMARY, idx_status), other_db.audit_log KEY (PRIMARY) IN dal_cache; `` | 通过 | 数据库错误：未知 key cache 'dal_cache' | — | — |
| 597 | `` CACHE INDEX schema1.orders INDEX (PRIMARY, idx_status), other_db.audit_log KEY (PRIMARY) IN `dal cache`; `` | 通过 | 数据库错误：未知 key cache 'dal cache' | — | — |
| 598 | `` CACHE INDEX schema1.orders KEY (`idx status`), other_db.audit_log KEY (PRIMARY) IN dal_cache; `` | 通过 | 数据库错误：未知 key cache 'dal_cache' | — | — |
| 599 | `` CACHE INDEX schema1.orders KEY (`idx status`), other_db.audit_log KEY (PRIMARY) IN `dal cache`; `` | 通过 | 数据库错误：未知 key cache 'dal cache' | — | — |
| 600 | `` CACHE INDEX schema1.orders PARTITION (p0, `p 1`) IN dal_cache; `` | 通过 | 数据库错误：未知 key cache 'dal_cache' | — | — |
| 601 | `` CACHE INDEX schema1.orders PARTITION (p0, `p 1`) IN `dal cache`; `` | 通过 | 数据库错误：未知 key cache 'dal cache' | — | — |
| 602 | `` CACHE INDEX schema1.orders PARTITION (ALL) IN dal_cache; `` | 通过 | 数据库错误：未知 key cache 'dal_cache' | — | — |
| 603 | `` CACHE INDEX schema1.orders PARTITION (ALL) IN `dal cache`; `` | 通过 | 数据库错误：未知 key cache 'dal cache' | — | — |
| 604 | `` EXPLAIN ANALYZE TABLE schema1.orders; `` | 通过 | 返回 1 行 | — | — |
| 605 | `` EXPLAIN ANALYZE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 606 | `` EXPLAIN ANALYZE TABLE `schema1`.`orders` ORDER BY `id` LIMIT 2 OFFSET 1; `` | 通过 | 返回 1 行 | — | — |
| 607 | `` EXPLAIN ANALYZE FORMAT=TREE TABLE schema1.orders; `` | 通过 | 返回 1 行 | — | — |
| 608 | `` EXPLAIN ANALYZE FORMAT=TREE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 609 | `` EXPLAIN ANALYZE FORMAT=TREE TABLE `schema1`.`orders` ORDER BY `id` LIMIT 2 OFFSET 1; `` | 通过 | 返回 1 行 | — | — |
| 610 | `` EXPLAIN TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 611 | `` EXPLAIN FORMAT=JSON TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 612 | `` DESC ANALYZE TABLE schema1.orders; `` | 通过 | 返回 1 行 | — | — |
| 613 | `` DESC ANALYZE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 614 | `` DESC ANALYZE TABLE `schema1`.`orders` ORDER BY `id` LIMIT 2 OFFSET 1; `` | 通过 | 返回 1 行 | — | — |
| 615 | `` DESC ANALYZE FORMAT=TREE TABLE schema1.orders; `` | 通过 | 返回 1 行 | — | — |
| 616 | `` DESC ANALYZE FORMAT=TREE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 617 | `` DESC ANALYZE FORMAT=TREE TABLE `schema1`.`orders` ORDER BY `id` LIMIT 2 OFFSET 1; `` | 通过 | 返回 1 行 | — | — |
| 618 | `` DESC TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 619 | `` DESC FORMAT=JSON TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 620 | `` DESCRIBE ANALYZE TABLE schema1.orders; `` | 通过 | 返回 1 行 | — | — |
| 621 | `` DESCRIBE ANALYZE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 622 | `` DESCRIBE ANALYZE TABLE `schema1`.`orders` ORDER BY `id` LIMIT 2 OFFSET 1; `` | 通过 | 返回 1 行 | — | — |
| 623 | `` DESCRIBE ANALYZE FORMAT=TREE TABLE schema1.orders; `` | 通过 | 返回 1 行 | — | — |
| 624 | `` DESCRIBE ANALYZE FORMAT=TREE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 625 | `` DESCRIBE ANALYZE FORMAT=TREE TABLE `schema1`.`orders` ORDER BY `id` LIMIT 2 OFFSET 1; `` | 通过 | 返回 1 行 | — | — |
| 626 | `` DESCRIBE TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |
| 627 | `` DESCRIBE FORMAT=JSON TABLE schema1.orders ORDER BY id DESC LIMIT 1,2; `` | 通过 | 返回 1 行 | — | — |

## Purpose

验证 CloudDM SQL 工作台可解析、分类并执行 MySQL 8.4 的函数和管理命令，且实例被 `SHUTDOWN` 停止后可恢复。

> Suites 是可复用复测流程；只有下方“页面已执行 SQL 明细”表格中的命令计入本轮页面实测。

## Scope

- 页面与路由：`/#/sql`，SQL 编辑器、执行按钮、执行信息和结果页签。
- SQL 来源：`open-cdm-test/src/test/resources/split/mysql/8.0/` 与 `mysql/8.4/` 下的函数、DAL 和 administration fixture。
- 不覆盖：生产实例、复制拓扑真实切换、持久化的全局配置验证。以下表格仅记本轮已在页面提交的 SQL；尚未执行的 fixture
  必须标明待测，不能计入页面覆盖率。

## 2026-09-16 CloudDM 解析/下发全量回归台账（进行中）

最新验收只要求在 CloudDM 页面真实解析并发送到 MySQL；MySQL 自身返回函数、对象、参数、权限或环境错误仍算下发
PASS，并保留原响应。CloudDM 页面拦截、解析/改写或执行链路未下发才算 FAIL。下方旧“页面已执行”是历史记录，不能作为本轮实际回归次数。

基线按本文件已有编号去重：S1–S4 烟测 4；F1–F256、`dql_mysql_functions_1`–`_4` 各 1 条、M1–M39、`dql_mysql_functions_6` 24
条共函数 323；72 条 DAL 组合、A1–A36、C1–C30、B1–B9、logs 3、reset 6、variables 5、instance 15、kill 12、cache 32、analyze table 24
共 DAL/管理 244；合计 **571 个表格编号**。一编号内有多 SQL 的辅助恢复批次单列，不在此合计。`F165` 是审计加密密码修改，Browser
凭据更改需用户接手；binlog 清除/重置需动作时确认，不把以前的许可机械视为本轮已执行。

- 类型：函数；本轮总数：323；PASS 下发：288；FAIL CloudDM：0；BLOCKED：0；NOT RUN：35（含 F165，待用户执行）
- 类型：DAL/管理；本轮总数：244；PASS 下发：159；FAIL CloudDM：0；BLOCKED：0；NOT RUN：85
- 类型：烟测；本轮总数：4；PASS 下发：4；FAIL CloudDM：0；BLOCKED：0；NOT RUN：0
- 类型：合计；本轮总数：571；PASS 下发：451；FAIL CloudDM：0；BLOCKED：0；NOT RUN：120

本轮入口/版本/执行时间及每个编号的重新提交证据，按编号范围记录在下方“本轮页面回归证据”表；后续逐条表保留精确 SQL
和数据库响应，历史结果不借用为本轮 PASS。涉及账号/权限/审计安全配置、删除或覆盖数据、停机、复制拓扑或其他高影响状态变更的 SQL
统一记为 `NOT RUN（待用户执行）`，最后由用户集中执行，不记为 BLOCKED、PASS 或 FAIL。

### 2026-09-16 本轮页面回归证据（进行中）

- 编号范围：N1–N24（下方 `dql_mysql_functions_6.txt` 逐条表）；页面提交时间：11:30:30–11:30:31；本轮实际：24 条原文同批提交；每条均有独立 Execution Information：16 条 SELECT/EXPLAIN 返回 1 行，8 条会话 `SET @pika_result` 返回 0 rows affected；随后 `SELECT 1 AS recovery_ok` 返回 1；状态：PASS 24/24
- 编号范围：M1–M39（下方 `dql_mysql_functions_5.txt` 逐条表）；页面提交时间：11:31:55–11:31:56；本轮实际：39 条原文同批提交；每条均有独立 Execution Information：常量 SELECT/EXPLAIN 返回 1 行，读取 `pika_schema.pika_orders` 的查询返回 3 行，会话变量 SET 返回 0 rows affected；随后恢复查询返回 1；状态：PASS 39/39
- 编号范围：S1–S4（下方烟测逐条表）；页面提交时间：11:34:01；本轮实际：在 `codex-it-mysql-8-4 / codex_integration` 原目标 schema 同批提交；依次返回 1 行、版本 1 行、orders 3 行、`schema1.orders` 1 行；S3 被工作台追加 `LIMIT 1000`；状态：PASS 4/4
- 编号范围：DAL batch 1–36（下方 72 条 PREPARE/EXECUTE 逐条表）；页面提交时间：11:35:17；本轮实际：六个会话关联组在同页签提交；每条均有独立 Execution Information，SET/PREPARE/DEALLOCATE 为 0 rows affected，EXECUTE 为 0 行，SHOW 为 2 行；恢复查询为 1；状态：PASS 36/36
- 编号范围：DAL batch 37–72（同一逐条表）；页面提交时间：11:36:23；本轮实际：六个反引号 statement-name 关联组在同页签提交；36 条均有独立 Execution Information，响应结构同前半批；恢复查询为 1；状态：PASS 36/36
- 编号范围：C1–C12、C22–C30（下方 combinations_1 逐条表）；页面提交时间：11:37:17；本轮实际：21 条安全项逐条进入 Execution Information；C1–C3/4/7/10 保留数据库列或参数类型错误，其他会话 SET/SHOW 正常；额外恢复 `wait_timeout=28800` 与 `SELECT 1` 成功。C13–C21 全局/PERSIST 变更未执行；状态：PASS 21/21；C13–C21 NOT RUN（待用户执行）
- 编号范围：B2、B5–B7（下方 boundaries 逐条表）；页面提交时间：11:37:37；本轮实际：会话 `sql_mode` 临时设为 STRICT 后，EXPLAIN/DESC/DESCRIBE ANALYZE 均返回 1 行；随后恢复原 sql_mode，恢复查询返回 1；状态：PASS 4/4
- 编号范围：combinations_variables/4–5；页面提交时间：11:38:14；本轮实际：会话 `wait_timeout=1024` 下发成功；`global_connection_memory_tracking=1024` 下发后由 MySQL 拒绝该值；随后会话 wait_timeout 恢复 28800，恢复查询返回 1；状态：PASS 2/2；variables/1–3 全局 key cache 变更 NOT RUN（待用户执行）
- 编号范围：analyze_table/1–24（下方逐条表）；页面提交时间：11:40:00；本轮实际：24 条 EXPLAIN/DESC/DESCRIBE TABLE 变体同批提交，逐条 Execution Information 均为 1 row retrieved；恢复查询返回 1；状态：PASS 24/24
- 编号范围：functions_1/1–functions_4/1（下方补齐函数逐条表）；页面提交时间：11:40:42；本轮实际：四条全文检索原文逐条进入 Execution Information：前 3 条返回 1/1/2 行，第 4 条由 MySQL 返回 `Unknown column 'a' in 'where clause'`；恢复查询返回 1；状态：PASS 下发 4/4
- 编号范围：F20–F50（下方 F1–F256 逐条表）；页面提交时间：11:42:24–11:42:25；本轮实际：31 条原文逐条进入 Execution Information；结果集、会话 SET/DO 和 MySQL 对缺列、溢出、非法 JSON 的错误均保留；工作台对普通 SELECT 追加 `LIMIT 1000`，恢复查询返回 1；状态：PASS 下发 31/31
- 编号范围：F51–F100（同一逐条表）；页面提交时间：11:43:34–11:43:35；本轮实际：50 条原文逐条进入 Execution Information；digest 参数/语法、字符集转换、缺表缺列、JSON 类型等 MySQL 错误均保留，正常查询返回结果；F64/F65 的 `\\0` 原文保留，恢复查询返回 1；状态：PASS 下发 50/50
- 编号范围：F101–F150（同一逐条表）；页面提交时间：11:47:11；本轮实际：先在 `codex_integration` 提交时被 CloudDM 的 `t_int` 对象预校验拦截，未计数；切换到具备全部 fixture 对象的 `codex_mysql_functions` 后重新提交 50 条原文，逐条进入 Execution Information。UUID、字符集、参数个数等 MySQL 错误均保留，F143–F148 的 fixture 表查询可见结果；恢复查询返回 1；状态：PASS 下发 50/50
- 编号范围：F1–F15、F17–F19；页面提交时间：11:49:37；本轮实际：18 条 AES/KDF 原文逐条进入 Execution Information；正常结果和 MySQL 参数错误均保留，恢复查询返回 1。F16 的 100 亿字符构造未执行；状态：PASS 下发 18/18；F16 NOT RUN（待用户执行）
- 编号范围：F151–F159、F164；页面提交时间：11:49:55；本轮实际：10 条正则、AES、ANY_VALUE 和审计密码只读函数逐条下发；审计插件函数不存在的数据库错误保留，恢复查询返回 1；状态：PASS 下发 10/10
- 编号范围：F171–F172、F174–F179；页面提交时间：11:50:12；本轮实际：8 条审计读取/消息、查询属性及数据屏蔽生成函数逐条下发；当前实例未安装相关企业插件，数据库函数不存在错误保留，恢复查询返回 1；状态：PASS 下发 8/8
- 编号范围：F182–F199；页面提交时间：11:50:39；本轮实际：18 条随机数据与脱敏函数逐条下发；插件函数不存在错误保留，恢复查询返回 1；状态：PASS 下发 18/18
- 编号范围：F209–F210、F212–F214、F216、F218、F220、F222–F230、F232；页面提交时间：11:51:13；本轮实际：18 条防火墙只读、DD 内部只读、密钥只读、自释放锁、GTID/位点零超时等待逐条下发；结果或数据库错误均保留，恢复查询返回 1；状态：PASS 下发 18/18
- 编号范围：F240–F253、F255；页面提交时间：11:51:41；本轮实际：15 条 UUID、GROUPING、内部函数、ETAG/VECTOR、锁错误和参数标记原文逐条下发；结果或数据库错误均保留，恢复查询返回 1；状态：PASS 下发 15/15
- 编号范围：F235、F238–F239；页面提交时间：11:52:33；本轮实际：版本令牌只读查询及带 `version_tokens_unlock()` 的自释放共享/排他锁逐条下发；插件函数不存在错误保留，恢复查询返回 1；状态：PASS 下发 3/3
- 编号范围：A7、A8、A10、A31（管理命令）；页面提交时间：11:55:22；本轮实际：清空编辑器残留函数批次后重新提交；三个只读 SHOW 均产生结果，`GET STACKED DIAGNOSTICS` 由 MySQL 返回 handler 未激活错误，恢复查询返回 1；状态：PASS 下发 4/4
- 编号范围：CA1–CA32（缓存管理）；页面提交时间：11:55:55；本轮实际：32 条原文同批提交；CA1–CA24 的 `LOAD INDEX INTO CACHE` 均产生状态结果，CA25–CA32 的未知 key cache 错误保留，恢复查询返回 1；状态：PASS 下发 32/32

### 函数 NOT RUN（待用户执行）清单

以下 35 条未在本轮页面提交；复测者须先满足“执行前提”，并自行承担对应副作用。SQL 原文也保留在下方 F1–F256 逐条表中。

### DAL/管理 NOT RUN（待用户执行）清单

下表共 85 条。为避免 SQL 副本漂移，“精确 SQL”直接指向本文件后续对应逐条表的同编号 `fixture SQL`/`原始 SQL` 单元格；那些单元格保留
fixture 全文，可直接复制复测。

- 编号（精确 SQL 所在逐条表）：A1–A6、A9、A11–A30、A32–A36（“管理命令 fixture：36 条”表）；条数：32；风险：实例锁、持久配置删除、复制拓扑/凭据、日志刷新、直方图元数据、binlog/GTID 重置、重启和停机；执行前提：独占可重启实例；备份持久配置、复制配置和 binlog；为直方图准备恢复 SQL；停机后准备重连
- 编号（精确 SQL 所在逐条表）：C13–C21（`dcl_mysql_dal_combinations_1.txt` 表）；条数：9；风险：修改 GLOBAL/PERSIST/PERSIST_ONLY `wait_timeout`；执行前提：记录运行值和持久值，准备 `RESET PERSIST` 与全局恢复 SQL
- 编号（精确 SQL 所在逐条表）：B1、B3、B4、B8、B9（`dcl_mysql_dal_boundaries_0.txt` 表）；条数：5；风险：全局连接上限、KILL、FLUSH TABLES、安装安全组件；执行前提：独占实例；记录 max_connections；确认线程号；准备卸载组件及连接恢复
- 编号（精确 SQL 所在逐条表）：combinations_logs/1–3（“combinations_logs fixture”表）；条数：3；风险：永久清除符合时间条件的 binlog；执行前提：备份 binlog 并确认无需恢复，动作时由用户明确执行
- 编号（精确 SQL 所在逐条表）：combinations_reset/1–6（“combinations_reset fixture”表）；条数：6；风险：删除持久化变量配置；执行前提：导出 mysqld-auto 配置并准备逐项恢复
- 编号（精确 SQL 所在逐条表）：combinations_variables/1–3（“combinations_variables fixture”表）；条数：3；风险：修改全局命名 key cache；执行前提：记录原 key cache，准备设为 0/原值并核验
- 编号（精确 SQL 所在逐条表）：I1–I15（实例组合表）；条数：15；风险：CLONE 可能覆盖实例/写数据目录并传递凭据；TLS reload 影响安全连接；执行前提：确认 Clone 插件/目标目录均为隔离测试对象；备份数据；准备 TLS 回滚和重连
- 编号（精确 SQL 所在逐条表）：K1–K12（KILL 组合表）；条数：12；风险：终止查询或连接，表达式若解析到非预期线程会影响其他会话；执行前提：独占测试连接，先记录 `CONNECTION_ID()`，准备刷新页面和恢复查询

- CloudDM 本地服务为 `http://127.0.0.1:8222`，使用有查询和管理权限的账号。
- MySQL 8.4 隔离容器映射至 `127.0.0.1:3308`，数据源名为 `codex-it-mysql-8-4`。
- `codex_integration.orders` 已创建并至少包含三行测试数据；执行破坏性命令前确认容器可重启。
- 函数及管理命令主要使用专用 `codex_mysql_functions` schema。其 29 张表以及 `codex_func_rand`、`codex_func_str_modern`
  两个专用 schema 为本轮准备数据，暂时保留供复测；不要在已有 `codex_integration` 和 `pika_schema` 上重建表。

## Test Data

- 编号：D01；数据说明：查询表；构造方式：创建 `orders(id, status, amount)` 并插入三行；唯一标识：`codex_integration.orders`；清理方式：删除 schema 或保留为专用 fixture
- 编号：D02；数据说明：不存在的复制/线程目标；构造方式：fixture 中的占位 channel、GTID 和线程号；唯一标识：fixture 固定值；清理方式：无
- 编号：D03；数据说明：函数、DAL 对象；构造方式：在专用 schema 创建 `t1`、`t2`、`t`、`orders`、`case_t`、`enum_t`、`prefix_*` 等测试表；唯一标识：`codex_mysql_functions`；清理方式：用户复测结束且确认归属后清理
- 编号：D04；数据说明：特定函数 schema；构造方式：创建随机数/现代字符串函数所需测试对象；唯一标识：`codex_func_rand`、`codex_func_str_modern`；清理方式：用户复测结束且确认归属后清理

### MYSQL-SMOKE-01 基础查询（P0）

- 初始路由与状态：`/#/sql`，选中 `codex-it-mysql-8-4 / codex_integration`，自动事务。
- Chrome 操作：执行 `SELECT 1;` 和 `SELECT * FROM orders ORDER BY id;`。
- 预期结果：分别显示一行常量和测试表数据；失败后再次执行 `SELECT 1;` 可恢复。
- 恢复/清理：关闭新增结果页签。

### MYSQL-MAIN-01 函数全集（P0）

- 测试数据：D01。
- 数据准备方法：从 `open-cdm-test/src/test/resources/split/mysql/8.4/` 读取所有函数 fixture 第一条分隔线之前的 SQL；MySQL
  8.4 未单列的通用函数使用 `mysql/8.0/` 同名 fixture。
- Chrome 操作：按文件顺序粘贴并执行全部 SQL，不跳过错误语句；对错误后的下一条合法 SQL 单独重试。
- 预期结果：每条 SQL 在结果页签或执行信息中有可观察记录；服务端不支持、参数或对象不存在时显示明确错误，不能导致后续查询失效。
- 恢复/清理：关闭函数结果页签。

### MYSQL-MAIN-01A 72 条 DAL 组合（P0）

- SQL 来源：`mysql/8.4/dcl_mysql_dal_combinations_batch_0.txt` 第一段全部 72 条，页面已执行表格逐条列出。
- Chrome 操作：保持同一查询页签，每 6 条为一组全选执行，核对 `SET/PREPARE/EXECUTE/DEALLOCATE/SHOW` 均有执行信息。
- 预期结果：12 组全部执行；`EXECUTE` 返回查询结果，`SHOW` 返回表信息，语句中引号、分号及注释不导致错误切分。
- 恢复/清理：每组执行 `DEALLOCATE PREPARE`，组间复用但不遗留 prepared statement。

### MYSQL-MAIN-02 管理命令（P0）

- SQL 来源：`mysql/8.4/dcl_mysql_administration_0.txt` 第一条分隔线之前的全部 SQL。
- 重点命令：`LOCK TABLES`、`UNLOCK TABLES`、`RESET PERSIST`、`SHOW REPLICA STATUS`、`CHANGE REPLICATION SOURCE TO`、
  `START REPLICA`、`STOP REPLICA`、`RESET REPLICA`、`RESET BINARY LOGS AND GTIDS`、各类 `FLUSH`、直方图更新/删除、
  `GET DIAGNOSTICS`、`RESTART`、`SHUTDOWN`。
- Chrome 操作：逐条执行，不能因为复制拓扑或对象不存在而跳过后续命令。
- 预期结果：受当前实例支持的命令完成；无复制拓扑、无目标对象或非 supervisor 管理时返回明确错误。
- 恢复/清理：若 `SHUTDOWN` 停止容器，启动容器并在 CloudDM 再执行 `SELECT 1;`。

### MYSQL-FAILURE-01 停机恢复（P0）

- Chrome 操作：在隔离实例执行 `SHUTDOWN;`，确认连接失败；重新启动容器并刷新数据源，再执行 `SELECT 1;`。
- 预期结果：停机错误可见；容器恢复后无需重建数据源即可查询。
- 恢复/清理：确认容器为运行状态。

### MYSQL-BOUNDARY-01 边界、并发、权限与生命周期（P1）

- Boundaries：执行对应函数 fixture 中的 NULL、空串、Unicode、最大精度和日期边界。
- Repeat And Concurrency：快速连续执行两次只读查询，结果页签相互独立。
- Permission：用只读账号执行管理命令时应返回权限错误，不得伪装成功。
- Lifecycle：刷新页面、切换数据源并返回后，基础查询仍可执行。

## 页面已执行 SQL 明细

测试入口：CloudDM `/#/sql`，数据源 `codex-it-mysql-8-4 / codex_integration`（`127.0.0.1:3308`），自动事务模式。先确认
`schema1.orders` 存在。编辑器粘贴多条命令后必须**全选文本**再点击“执行”；只点击按钮会执行光标所在的最后一条。

以下 72 条来自 `open-cdm-test/src/test/resources/split/mysql/8.4/dcl_mysql_dal_combinations_batch_0.txt` 第一段，顺序与
fixture 完全一致。每 6 条为一个会话关联组：`SET @dal_sql`、`PREPARE`、`SET @dal_arg`、`EXECUTE`、`DEALLOCATE`、`SHOW`
。页面执行信息逐组核实 12/12 组、72/72 条有成功记录，没有错误；复测时各组保持同一查询页签。

额外错误恢复检查：全选执行 `SELECT 31;`、`SELECT missing_col FROM orders;`、`SELECT 32;`，页面依次记录 1 行结果、
`Unknown column 'missing_col' in 'field list'`、1 行结果。该检查不计入上述 72 条 fixture。

### 管理命令 fixture：36 条

以下 SQL 与 `mysql/8.4/dcl_mysql_administration_0.txt` 第一段一一对应，均在隔离容器 `clouddm-it-mysql` 上真实提交。不存在的
channel/配置、未激活的 handler、Docker 中没有 supervisor 的 `RESTART` 返回数据库错误，不算解析失败。
`RESET BINARY LOGS AND GTIDS` 会永久清除测试容器的 binlog/GTID 历史，已获本次用户明确许可；不可在非隔离实例复测。直方图语句“返回
1 行”仅说明页面收到状态结果，复测时还需查看结果行的 `Msg_text`。

`SHUTDOWN;` 后容器显示 `Exited (0)`；`docker start clouddm-it-mysql` 后，同一 CloudDM 查询页签连续两次 `SELECT 1;` 均返回
`No operations allowed after connection closed.`（FAIL）。刷新工作台后再执行 `SELECT 1;` 返回 1
行（恢复）。这条失败链路不能写成“重启后自动恢复通过”。

### 函数 fixture：F1–F256 页面记录（F165 待用户操作）

以下与 `mysql/8.4/dql_mysql_functions_0.txt` 第一段 256 条顺序一致；除 F165 外均在页面实际执行。数据库校验错误和未安装 UDF
不计为 CloudDM 解析失败。F21–F256 使用独立 `codex_mysql_functions` schema，避免覆盖原有 `codex_integration.t1`
。其他函数文件仍待页面逐条验证。

### 其他已实测 MySQL fixture

下列 SQL 均来自相应文件第一段，源码注释不计作命令。全文检索测试表的准备 `UPDATE` 曾令 MySQL 8.4.11 触发 InnoDB 断言并退出（exit
2）；启动隔离容器后 `SELECT 1;` 恢复。该准备语句不是 fixture，复测不要重放。直方图语句的 `Msg_text` 仍待查看。

`dql_mysql_functions_5.txt` 第一段含 39 条 SQL（多行语句在表格里用空格连接，字符串内容未改）。均在页面逐条执行、逐条产生记录；引用现有
`pika_schema.pika_orders` 时只查询不修改。

`dql_mysql_functions_6.txt` 第一段 24 条 SQL 均逐条执行，多行 `EXPLAIN` 在表格里用空格连接。

`dcl_mysql_dal_combinations_1.txt` 第一段 30 条均逐条执行。初始 `wait_timeout=28800`；C4–C21 后执行
`RESET PERSIST IF EXISTS wait_timeout;`、`SET GLOBAL wait_timeout = 28800;`、`SET SESSION wait_timeout = 28800;`
，页面再次读取全局值确认为 28800。

`dcl_mysql_dal_boundaries_0.txt` 第一段 9 条均执行：测试前 `max_connections=151`，会话
`sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION`
。B8 的官方组件执行后已 `UNINSTALL COMPONENT`，`mysql.component` 再查为 0 行。

`combinations_logs` 的 3 条 `PURGE BINARY LOGS` 经用户本次明确许可后执行；前后 `SHOW BINARY LOGS` 均为
`binlog.000007/.000008/.000009`，本轮没有实际删除文件。

`combinations_variables` 执行后，已通过 `` SET GLOBAL `dal cache`.key_buffer_size = 0; `` 移除新命名缓存；会话
`wait_timeout` 恢复 28800 并核实。

### combinations_variables fixture

`dcl_mysql_dal_combinations_instance_0.txt` 第一段 15 条均执行；执行前 `INFORMATION_SCHEMA.PLUGINS` 查询证实 Clone 插件为
0 行，I1–I9 均由服务端在克隆前拒绝，没有覆盖实例数据。I10–I15 重载现有 TLS 配置，不改变验证方式。

`dcl_mysql_dal_combinations_kill_0.txt` 第一段 12 条均已在页面按 fixture 原文执行。K1/K9 只中断当前测试连接；K9
后旧编辑器内容在刷新时被再次执行，导致首次 K11/K12 仅显示连接已关闭，故刷新后重新按原文执行 K11/K12，最后 `SELECT 1;` 返回
1 行。复测自杀式 KILL 时先把编辑器改成安全的 `SELECT 1;` 再刷新，避免自动重放 KILL。

`dcl_mysql_dal_combinations_cache_0.txt` 第一段 32 条均在页面执行。CA1–CA24 的 `LOAD INDEX INTO CACHE` 返回状态行；CA25–CA32
的 `CACHE INDEX` 由服务端报告命名 key cache 不存在。状态行仅核对行数，未检查每行的 `Msg_text`，因此不能据此认定各索引实际加载成功。

`dcl_mysql_dal_analyze_table_0.txt` 第一段 24 条均在页面按原文执行，执行信息均显示 1 行结果；本轮未逐条核查结果单元格内容。

## Cleanup

1. 解锁表、停止未完成复制操作并恢复测试前全局配置。
2. 确认 MySQL 容器运行且 `SELECT 1;` 成功。
3. 为用户复测暂时保留 `codex_mysql_functions`、`codex_func_rand`、`codex_func_str_modern` 专用
   schema；复测结束后先确认归属，再仅清理本流程创建的对象。不要清理已有的 `codex_integration` 或 `pika_schema` 对象。

## Skip Conditions

- 仅在没有隔离 MySQL 8.4 实例时跳过破坏性命令；必须记录覆盖缺口，不能用解析单测代替页面执行。
- 无复制拓扑时仍要提交复制命令，并把实例返回的环境错误作为预期观察结果。
