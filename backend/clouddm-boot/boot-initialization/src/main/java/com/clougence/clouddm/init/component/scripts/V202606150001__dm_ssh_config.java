/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.init.component.scripts;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.component.scripts.migration.DataSourceConfigUnifyMigrator;

public class V202606150001__dm_ssh_config extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        List<String> scripts = new ArrayList<>();
        scripts.add("""
                CREATE TABLE IF NOT EXISTS `dm_ssh_config` (
                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                  `cluster_id` bigint DEFAULT NULL COMMENT '绑定集群 ID',
                  `name` varchar(128) NOT NULL COMMENT 'SSH 配置名称',
                  `host` varchar(512) NOT NULL COMMENT 'SSH 主机',
                  `port` int NOT NULL DEFAULT 22 COMMENT 'SSH 端口',
                  `username` varchar(255) NOT NULL COMMENT 'SSH 用户名',
                  `auth_type` varchar(64) NOT NULL COMMENT 'PASSWORD / PRIVATE_KEY',
                  `password` varchar(512) DEFAULT NULL COMMENT '登录密码或交互式认证密码密文',
                  `private_key_data` longtext DEFAULT NULL COMMENT '私钥数据，使用 URI scheme 区分来源',
                  `private_key_passphrase` varchar(512) DEFAULT NULL COMMENT '私钥 passphrase 密文',
                  `con_features` longtext DEFAULT NULL COMMENT 'JSON map for SSH connection parameters',
                  `proxy_type` varchar(64) NOT NULL DEFAULT 'NONE' COMMENT 'NONE / HTTP / SOCKS4 / SOCKS5',
                  `proxy_features` longtext DEFAULT NULL COMMENT 'JSON map for proxy parameters',
                  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
                  PRIMARY KEY (`id`),
                  KEY `idx_host` (`host`(127)),
                  KEY `idx_cluster_id` (`cluster_id`),
                  KEY `idx_proxy_type` (`proxy_type`),
                  KEY `idx_deleted` (`deleted`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);
        scripts.add("""
                ALTER TABLE dm_ssh_config
                    ADD COLUMN `cluster_id` bigint DEFAULT NULL COMMENT '绑定集群 ID' AFTER `gmt_modified`
                """);
        scripts.add("""
                ALTER TABLE dm_ssh_config
                    ADD INDEX `idx_cluster_id` (`cluster_id`)
                """);
        scripts.add("""
                ALTER TABLE dm_ds
                    ADD COLUMN `status` varchar(64) DEFAULT NULL
                    AFTER `life_cycle_state`
                """);
        scripts.add("""
                ALTER TABLE dm_ds
                    ADD COLUMN `status_message` text DEFAULT NULL
                    AFTER `status`
                """);
        scripts.add("""
                ALTER TABLE dm_ds
                    ADD COLUMN `bind_cluster_id` bigint DEFAULT NULL
                    AFTER `status_message`
                """);
        return scripts;
    }

    @Override
    protected void afterMigrate(Connection connection) throws Exception {
        new DataSourceConfigUnifyMigrator(connection).migrate();

        List<String> scripts = new ArrayList<>();
        scripts.add("""
                ALTER TABLE dm_ds
                    DROP COLUMN `security_file_url`,
                    DROP COLUMN `security_file_password`,
                    DROP COLUMN `client_security_file_url`,
                    DROP COLUMN `client_security_file_password`,
                    DROP COLUMN `secret_file_url`,
                    DROP COLUMN `secret_file_password`,
                    DROP COLUMN `security_file_store_type`,
                    DROP COLUMN `public_security_type`,
                    DROP COLUMN `client_trust_store_password`,
                    DROP COLUMN `deploy_type`,
                    DROP COLUMN `info_fetch_type`,
                    DROP COLUMN `connect_type`,
                    DROP COLUMN `default_db_name`,
                    DROP COLUMN `account`,
                    DROP COLUMN `password`,
                    DROP COLUMN `private_host`,
                    DROP COLUMN `public_host`,
                    DROP COLUMN `host_type`,
                    DROP COLUMN `console_job_id`,
                    DROP COLUMN `parent_ds_id`,
                    DROP INDEX `idx_parent_ds_id`
                """);
        scripts.add("""
                ALTER TABLE dm_ds_config_kv_4dm
                    DROP COLUMN `config_group`,
                    DROP COLUMN `display`,
                    DROP COLUMN `desc_key`,
                    DROP COLUMN `value_require`,
                    DROP COLUMN `value_valid_regex`,
                    DROP COLUMN `default_value`,
                    DROP COLUMN `value_advance`,
                    DROP COLUMN `read_only`,
                    DROP COLUMN `is_secret`
                """);
        scripts.add("""
                ALTER TABLE dm_sys_user_conf
                    DROP COLUMN `default_value`,
                    DROP COLUMN `value_range`,
                    DROP COLUMN `read_only`,
                    DROP COLUMN `user_config_tag_type`,
                    DROP COLUMN `conf_belong`,
                    DROP COLUMN `conf_val_type`,
                    DROP COLUMN `is_secret`,
                    DROP COLUMN `desc_key`
                """);
        scripts.add("DROP TABLE IF EXISTS dm_ds_config_kv_4rdp");
        scripts.add("DROP TABLE IF EXISTS dm_ds_config");
        scripts.add("DROP TABLE IF EXISTS dm_ds_blob_resource");
        scripts.add("DROP TABLE IF EXISTS dm_ds_usage");
        scripts.add("DROP TABLE IF EXISTS dm_mon_web_view_log");
        for (String sql : scripts) {
            safeExecute(connection, sql);
        }
    }
}
