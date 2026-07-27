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

public class V202607270001__sql_audit_ack extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table dm_exec_sql_audit
                        add query_id varchar(64) null,
                        add analysis_context longtext null,
                        modify session_id varchar(255) null,
                        modify work_seq_number varchar(255) null
                """, """
                    create unique index uk_exec_sql_audit_query_id
                        on dm_exec_sql_audit (query_id)
                """, """
                    create index idx_exec_sql_audit_session_status
                        on dm_exec_sql_audit (session_id, status)
                """);
    }
}
