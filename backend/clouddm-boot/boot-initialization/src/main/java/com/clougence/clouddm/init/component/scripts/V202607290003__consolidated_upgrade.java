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
import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202607290003__consolidated_upgrade extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table dm_approval
                        add column content_type varchar(16) not null default 'INLINE'
                            comment 'SQL content source: INLINE or ATTACHMENT'
                """, """
                    alter table dm_approval
                        add column features longtext null
                            comment 'JSON list of enabled approval features'
                """, """
                    create table dm_sys_attachment
                    (
                        id                bigint auto_increment primary key,
                        gmt_create        datetime not null default current_timestamp,
                        gmt_modified      datetime not null default current_timestamp,
                        owner_uid         varchar(64) not null default '',
                        approval_id       bigint null,
                        attachment_type   varchar(32) not null,
                        attachment_status varchar(16) not null,
                        file_name         varchar(512) not null,
                        file_size         bigint not null,
                        file_hash         varchar(64) null,
                        blob_content      longblob null,
                        key idx_attachment_approval (approval_id),
                        key idx_attachment_cleanup (attachment_status, gmt_modified),
                        unique key uk_sys_attachment_approval (approval_id, attachment_type)
                    )
                """, """
                    alter table dm_exec_auto_job
                        modify column uid varchar(127) null,
                        drop column primary_uid
                """, """
                    create index idx_auto_exec_task_job_order
                        on dm_exec_auto_task (auto_exec_job_id, exec_order)
                """, """
                    alter table dm_exec_auto_task
                        modify column exec_sql longtext not null
                """, """
                    alter table dm_exec_auto_task
                        add column query_id varchar(128) null
                """, """
                    create unique index uk_exec_auto_job_query_id
                        on dm_exec_auto_job (query_id)
                """, """
                    alter table dm_approval
                        drop column error_count,
                        drop column session_id,
                        drop column explain_sql_data,
                        drop column risk_sql_count,
                        drop column expected_exec_time,
                        drop column total_count,
                        drop column checked_info,
                        drop column behaviors
                """, """
                    alter table dm_approval_process_activity
                        add column task_status varchar(16) null
                            comment 'Execution state of a PRE_INIT child task'
                """, """
                    alter table dm_change_flow
                        drop column flow_check,
                        drop column flow_approve,
                        drop column flow_execute
                """, """
                    alter table dm_change
                        drop column flow_walked
                """, """
                    alter table dm_exec_auto_job
                        drop index idx_depend_biz,
                        drop column depend_on_biz_type
                """, """
                    create unique index uk_exec_auto_job_depend_biz
                        on dm_exec_auto_job (depend_on_biz_id)
                """);
    }

    @Override
    protected void afterMigrate(Connection connection) throws Exception {
        safeExecute(connection, """
                    update dm_approval
                    set features = '["PRE_INIT"]'
                    where appro_biz = 'DM_QUERY'
                """);
        safeExecute(connection, """
                    update dm_exec_auto_task
                    set query_id = uuid()
                    where query_id is null
                """);
        // The NOT NULL constraint and the unique index must be applied after the
        // query_id backfill above.
        safeExecute(connection, """
                    alter table dm_exec_auto_task
                        modify column query_id varchar(128) not null
                """);
        safeExecute(connection, """
                    create unique index uk_exec_auto_task_query_id
                        on dm_exec_auto_task (query_id)
                """);
        safeExecute(connection, """
                    update dm_change_item
                    set ref_change_item_type = 'CHECKS_DETAIL'
                    where ref_change_item_type = 'CHECKS'
                """);
        safeExecute(connection, """
                    update dm_approval_process_activity
                    set task_status = case json_unquote(json_extract(context, '$.analysisStatus'))
                        when 'FINISHED' then 'FINISHED'
                        when 'FAILED' then 'FAILED'
                        else 'INIT'
                    end
                    where activity_id in ('SQL_RECOGNITION', 'BEHAVIOR_ANALYSIS', 'SECURITY_RULE')
                      and context is not null
                      and json_valid(context)
                """);
        safeExecute(connection, """
                    update dm_approval
                    set ticket_status = case
                        when exists(select 1 from dm_approval_process_activity a where a.ticket_id = dm_approval.id)
                            then 'PRE_INIT_RUN'
                        else 'PRE_INIT_WAIT'
                    end
                    where ticket_status = 'PRE_INIT'
                """);
    }
}
