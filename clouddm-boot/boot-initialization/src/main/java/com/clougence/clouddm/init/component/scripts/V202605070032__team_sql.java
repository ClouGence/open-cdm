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

public class V202605070032__team_sql extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
            .of("""
                        alter table `dm_ds_session`
                                add session_type varchar(16) null
                    """, """
                        alter table `dm_ds_session`
                                modify datasource_id varchar(255) null
                    """, """
                        alter table `dm_ds_session`
                                add `attach` text null
                    """, """
                        alter table `dm_ds_session`
                                add `last_query_time` datetime null
                    """, """
                        alter table `dm_ds_session`
                                drop `tx`
                    """, """
                        CREATE TABLE IF NOT EXISTS `dm_async_task`
                            (
                                `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
                                `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `title`              varchar(256) NOT NULL,
                                `description`        text         NULL,
                                `biz_id`             varchar(128) NULL,
                                `biz_type`           varchar(64)  NULL,
                                `console_ip`         varchar(32)  NULL,
                                `depend_on_biz_id`   varchar(128) NULL,
                                `depend_on_biz_type` varchar(64)  NULL,
                                `owner_uid`          varchar(255) NOT NULL,
                                `handler_name`       text         NOT NULL,
                                `handler_type`       text         NOT NULL,
                                `config_data`        text         NULL,
                                `show_in_dock`       tinyint      NOT NULL DEFAULT 0,
                                `process_type`       varchar(64)  NOT NULL DEFAULT 'SCROLL',
                                `process_value`      bigint       NOT NULL DEFAULT 0,
                                `fast_fail`          tinyint      NOT NULL DEFAULT 0,
                                `status`             varchar(32)  NOT NULL,
                                `status_msg`         text         NULL,
                                `time_of_start`      datetime     NULL,
                                `time_of_last`       datetime     NULL,
                                `time_of_finish`     datetime     NULL,
                                PRIMARY KEY (`id`),
                                index dm_async_task_owner_uid (`owner_uid`),
                                unique dm_async_task_biz_idx (`biz_id`, `biz_type`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        create table if not exists `dm_sec_spec`
                            (
                                `id`           bigint(20)   not null auto_increment,
                                `gmt_create`   datetime     not null default current_timestamp,
                                `gmt_modified` datetime     not null default current_timestamp,
                                `owner_uid`    varchar(255) not null,
                                `spec_name`    varchar(255) not null,
                                `description`  text         null,
                                `enable`       tinyint      not null default 0,
                                primary key (`id`),
                                index dm_sec_spec_owner_uid (`owner_uid`)
                            ) engine = innodb
                              default charset = utf8mb4
                    """, """
                        create table if not exists `dm_sec_rules`
                            (
                                `id`           bigint(20)   not null auto_increment,
                                `gmt_create`   datetime     not null default current_timestamp,
                                `gmt_modified` datetime     not null default current_timestamp,
                                `owner_uid`    varchar(255) not null,
                                `rule_name`    varchar(255) not null,
                                `rule_desc`    text,
                                `ds_range`     text         null,
                                `rule_target`  varchar(64)  not null,
                                `rule_type`    varchar(64)  not null,
                                `rule_def`     text,
                                `rule_content` text,
                                `inner_share`  tinyint      not null default 0,
                                primary key (`id`),
                                index dm_sec_rules_owner_uid (`owner_uid`)
                            ) engine = innodb
                              default charset = utf8mb4
                    """, """
                        create table if not exists `dm_sec_referer`
                            (
                                `id`           bigint(20)   not null auto_increment,
                                `gmt_create`   datetime     not null default current_timestamp,
                                `gmt_modified` datetime     not null default current_timestamp,
                                `owner_uid`    varchar(255) not null,
                                `enable`       tinyint      not null,
                                `ref_rule`     bigint       not null,
                                `ref_spec`     bigint       not null,
                                `warn_level`   varchar(64),
                                `rule_param`   text,
                                primary key (`id`),
                                index dm_sec_referer_owner_uid (`owner_uid`),
                                index dm_sec_referer_refs (`ref_rule`, `ref_spec`)
                            ) engine = innodb
                              default charset = utf8mb4
                    """, """
                        CREATE TABLE `dm_ticket_details_inst`
                            (
                                `id`                     bigint       NOT NULL AUTO_INCREMENT,
                                `gmt_create`             datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                `rdp_ticket_ins_id`      varchar(64)  NOT NULL COMMENT 'The corresponding ID in the RDP work order table',
                                `session_id`             varchar(255)          DEFAULT NULL COMMENT 'Session will only be created after entering the automatic execution phase',
                                `risk_sql_count`         int                   DEFAULT NULL COMMENT 'Risk SQL quantity',
                                `raw_sql`                longtext COMMENT 'Unresolved original SQL content submitted by users',
                                `explain_sql_data`       longtext,
                                `total_count`            mediumtext COMMENT 'The total number of SQL statements in the execution process',
                                `expected_affected_rows` bigint                DEFAULT NULL COMMENT 'Expected impact on the number of rows',
                                `expected_exec_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'If executed immediately, it defaults to the time when the work order was generated. If it is found that the expected execution time is less than the current execution time after approval, execute immediately',
                                `immediately`            tinyint(1)            DEFAULT NULL COMMENT 'Do you want to execute it immediately',
                                `ddl_sql_exec_type`      varchar(128) NOT NULL DEFAULT 'DIRECT',
                                `none_ddl_sql_exec_type` varchar(128) NOT NULL DEFAULT 'DIRECT',
                                `roll_back_sql`          longtext,
                                `deleted`                tinyint(1)   NOT NULL DEFAULT '0',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `idx_unique_biz_id` (`rdp_ticket_ins_id`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        drop table `checker_parameter`
                    """, """
                        drop table `checker_template`
                    """, """
                        drop table `console_job`
                    """, """
                        drop table `console_task`
                    """, """
                        drop table `sql_process`
                    """, """
                        drop table `ticket_process`
                    """, """
                        drop table `dm_ticket_inst`
                    """, """
                        drop table `ds_appro_template`
                    """);
    }
}
