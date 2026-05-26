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

public class V202605261837__dm_approval_merge extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
            .of("""
                        rename table rdp_ticket_inst to dm_approval
                    """, """
                        alter table dm_approval
                            add column session_id varchar(255) default null comment 'Session will only be created after entering the automatic execution phase',
                            add column risk_sql_count int default null comment 'Risk SQL quantity',
                            add column raw_sql longtext comment 'Unresolved original SQL content submitted by users',
                            add column explain_sql_data longtext,
                            add column total_count mediumtext comment 'The total number of SQL statements in the execution process',
                            add column expected_affected_rows bigint default null comment 'Expected impact on the number of rows',
                            add column expected_exec_time datetime not null default current_timestamp comment 'If executed immediately, it defaults to the time when the work order was generated',
                            add column roll_back_sql longtext,
                            add column ticket_info text,
                            add column levels varchar(512) null,
                            add column checked_info text null
                    """, """
                        update dm_approval t
                            join dm_ticket_details_inst d on d.rdp_ticket_ins_id = t.biz_id
                        set
                            t.session_id = d.session_id,
                            t.risk_sql_count = d.risk_sql_count,
                            t.raw_sql = d.raw_sql,
                            t.explain_sql_data = d.explain_sql_data,
                            t.total_count = d.total_count,
                            t.expected_affected_rows = d.expected_affected_rows,
                            t.expected_exec_time = d.expected_exec_time,
                            t.roll_back_sql = d.roll_back_sql,
                            t.ticket_info = d.ticket_info,
                            t.levels = d.levels,
                            t.checked_info = d.checked_info
                    """, """
                        drop table dm_ticket_details_inst
                    """, """
                        rename table rdp_ticket_process to dm_approval_process
                    """, """
                        rename table rdp_ticket_process_activity to dm_approval_process_activity
                    """, """
                        rename table rdp_approval_person to dm_approval_person
                    """, """
                        rename table rdp_cache_appro_template to dm_approval_cache_template
                    """);
    }
}
