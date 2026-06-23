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

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202606190001__dm_ds_config_unify extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    INSERT INTO dm_ds_config_kv_4dm (
                        gmt_create,
                        gmt_modified,
                        data_source_id,
                        config_name,
                        config_group,
                        display,
                        desc_key,
                        value_require,
                        value_valid_regex,
                        config_value,
                        default_value,
                        value_advance,
                        read_only,
                        is_secret
                    )
                    SELECT
                        r.gmt_create,
                        r.gmt_modified,
                        r.data_source_id,
                        r.config_name,
                        r.config_group,
                        r.display,
                        r.desc_key,
                        r.value_require,
                        r.value_valid_regex,
                        r.config_value,
                        r.default_value,
                        r.value_advance,
                        r.read_only,
                        r.is_secret
                    FROM dm_ds_config_kv_4rdp r
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM dm_ds_config_kv_4dm d
                        WHERE d.data_source_id = r.data_source_id
                          AND d.config_name = r.config_name
                    )
                """, """
                    ALTER TABLE dm_ds
                        ADD COLUMN `status` varchar(64) DEFAULT NULL
                        AFTER `life_cycle_state`
                """, """
                    ALTER TABLE dm_ds
                        ADD COLUMN `status_message` text DEFAULT NULL
                        AFTER `status`
                """, """
                    ALTER TABLE dm_ds
                        ADD COLUMN `bind_cluster_id` bigint DEFAULT NULL
                        AFTER `status_message`
                """, """
                    UPDATE dm_ds d
                    JOIN dm_ds_config c
                      ON d.id = c.data_source_id
                    SET d.status = COALESCE(d.status, c.status),
                        d.status_message = COALESCE(d.status_message, c.status_message),
                        d.bind_cluster_id = COALESCE(d.bind_cluster_id, c.bind_cluster_id),
                        d.ds_env_id = COALESCE(d.ds_env_id, c.bind_env_id)
	                """, """
	                    UPDATE dm_ds
	                    SET access_key = COALESCE(NULLIF(access_key, ''), account),
	                        secret_key = COALESCE(NULLIF(secret_key, ''), password)
	                """, """
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
                """, """
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
                """, """
                    ALTER TABLE dm_sys_user_conf
                        DROP COLUMN `default_value`,
                        DROP COLUMN `value_range`,
                        DROP COLUMN `read_only`,
                        DROP COLUMN `user_config_tag_type`,
                        DROP COLUMN `conf_belong`,
                        DROP COLUMN `conf_val_type`,
                        DROP COLUMN `is_secret`,
                        DROP COLUMN `desc_key`
                """, """
                    DROP TABLE IF EXISTS dm_ds_config_kv_4rdp
                """, """
                    DROP TABLE IF EXISTS dm_ds_config
                """, """
                    DROP TABLE IF EXISTS dm_ds_blob_resource
                """, """
                    DROP TABLE IF EXISTS dm_ds_usage
                """, """
                    DROP TABLE IF EXISTS dm_mon_web_view_log
                """);
    }
}
