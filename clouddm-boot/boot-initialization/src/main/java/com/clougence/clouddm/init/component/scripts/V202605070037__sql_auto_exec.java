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

public class V202605070037__sql_auto_exec extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                       create table if not exists dm_auto_exec_job
                        (
                            id               bigint auto_increment primary key,
                            gmt_create       datetime default CURRENT_TIMESTAMP not null,
                            gmt_modified     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
                            data_source_id   bigint                             not null,
                            levels           varchar(512)                       not null,
                            uid              varchar(127)                       not null,
                            primary_uid      varchar(127)                       not null,
                            biz_id           varchar(128)                       not null,
                            depend_on_biz_type         varchar(32)                        not null,
                            depend_on_biz_id           varchar(127)                       null,
                            query_id         varchar(128)                       null,
                            status           varchar(32)                        not null,
                            end_time         datetime                           null,
                            last_report_time datetime                           null,
                            worker_seq_number              varchar(255)                       null,
                            config           text                               null,
                            exec_type        varchar(32)                        not null,
                            schedule_time    datetime                           not null,
                            normal           tinyint(1)                         not null default 1
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4
                """, """
                    create unique index idx_depend_biz on dm_auto_exec_job (depend_on_biz_id,depend_on_biz_type)
                """, """
                    create unique index idx_biz on dm_auto_exec_job (biz_id)
                """, """
                    create table if not exists dm_auto_exec_task
                        (
                            id                bigint auto_increment primary key,
                            gmt_create        datetime default CURRENT_TIMESTAMP not null,
                            gmt_modified      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
                            biz_id            varchar(128)                       not null,
                            auto_exec_job_id  bigint                             not null,
                            exec_order        int                                not null,
                            exec_sql          text                               not null,
                            gmt_last_start    datetime                           null,
                            gmt_last_end      datetime                           null,
                            status            varchar(32)                        not null,
                            affect_row        bigint                             null,
                            sql_type          varchar(32)                        not null,
                            transactional_group int               default 0      not null,
                            exec_count        int                 default 0      not null
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4
                """, """
                    create index idx_sql_job_id
                            on dm_auto_exec_task (auto_exec_job_id)
                """, """
                    create unique index idx_biz on dm_auto_exec_task (biz_id)
                """, """
                    create table if not exists dm_biz_log
                        (
                            id             bigint auto_increment primary key,
                            gmt_create     datetime default CURRENT_TIMESTAMP not null,
                            gmt_modified   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
                            depend_on_biz_id  varchar(128)                             not null,
                            depend_on_biz_type  varchar(128)                             not null,
                            content        text                               not null,
                            log_level      varchar(32)                      not null
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4
                """, """
                    create index idx_depend_biz on dm_biz_log (depend_on_biz_id,depend_on_biz_type)
                """, """
                    drop table data_export_job
                """, """
                    drop table data_export_task
                """);
    }
}
