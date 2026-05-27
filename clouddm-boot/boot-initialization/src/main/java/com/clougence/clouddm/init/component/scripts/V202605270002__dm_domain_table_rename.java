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

public class V202605270002__dm_domain_table_rename extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    RENAME TABLE dm_cluster TO dm_sys_cluster
                """, """
                    RENAME TABLE dm_worker TO dm_sys_worker
                """, """
                    RENAME TABLE rdp_ds_env TO dm_sys_env
                """, """
                    RENAME TABLE rdp_env_param TO dm_sys_env_param
                """, """
                    RENAME TABLE rdp_sys_config TO dm_sys_conf
                """, """
                    RENAME TABLE rdp_user_kv_base_config TO dm_sys_user_conf
                """, """
                    RENAME TABLE dm_messenger TO dm_sys_messenger
                """, """
                    RENAME TABLE rdp_data_source TO dm_ds
                """, """
                    RENAME TABLE rdp_ds_kv_base_config TO dm_ds_config_kv_4rdp
                """, """
                    RENAME TABLE rdp_ds_usage TO dm_ds_usage
                """, """
                    RENAME TABLE rdp_blob_resource TO dm_ds_blob_resource
                """, """
                    RENAME TABLE dm_meta_information_cache TO dm_ds_meta_data
                """, """
                    RENAME TABLE dm_ds_session TO dm_exec_session
                """, """
                    RENAME TABLE dm_query_constraints TO dm_exec_query_constraints
                """, """
                    RENAME TABLE dm_sql_audit TO dm_exec_sql_audit
                """, """
                    RENAME TABLE dm_auto_exec_job TO dm_exec_auto_job
                """, """
                    RENAME TABLE dm_auto_exec_task TO dm_exec_auto_task
                """, """
                    RENAME TABLE dm_async_task TO dm_exec_async_task
                """, """
                    RENAME TABLE dm_files TO dm_exec_file
                """, """
                    RENAME TABLE alert_config_detail TO dm_mon_alert_config_detail
                """, """
                    RENAME TABLE dm_biz_log TO dm_mon_biz_log
                """, """
                    RENAME TABLE rdp_alert_event_log TO dm_mon_alert_event_log
                """, """
                    RENAME TABLE rdp_op_audit TO dm_mon_op_audit
                """, """
                    RENAME TABLE rdp_web_view_log TO dm_mon_web_view_log
                """, """
                    RENAME TABLE dm_approval_cache_template TO dm_approval_template
                """);
    }
}
