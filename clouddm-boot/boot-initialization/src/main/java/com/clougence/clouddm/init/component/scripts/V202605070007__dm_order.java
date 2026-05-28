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

public class V202605070007__dm_order extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
            .of("""
                        CREATE TABLE `rdp_ticket_inst`
                            (
                                `id`                      bigint        NOT NULL AUTO_INCREMENT,
                                `biz_id`                  varchar(32)   NOT NULL,
                                `gmt_create`              datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified`            datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                `owner_uid`               varchar(255)  NOT NULL,
                                `bind_ds_id`              bigint        NOT NULL,
                                `target_info`             varchar(1024) NOT NULL,
                                `appro_type`              varchar(512)  NOT NULL,
                                `appro_biz`               varchar(64)   NOT NULL,
                                `appro_identity`          varchar(512)           DEFAULT NULL,
                                `appro_template_name`     varchar(256)           DEFAULT NULL,
                                `appro_template_identity` varchar(512)           DEFAULT NULL,
                                `appro_comment`           text,
                                `description`             varchar(1024)          DEFAULT NULL,
                                `ticket_title`            varchar(512)  NOT NULL,
                                `ticket_status`           varchar(512)  NOT NULL,
                                `finish_time`             datetime               DEFAULT NULL,
                                `deleted`                 tinyint(1)    NOT NULL DEFAULT '0',
                                `status_message`          text,
                                `error_count`             int                    DEFAULT NULL,
                                `primary_uid`             varchar(255)  NOT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `idx_unique_biz_id` (`biz_id`),
                                KEY `idx_data_source_id` (`bind_ds_id`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        CREATE TABLE `rdp_cache_appro_template`
                            (
                                `id`                bigint       NOT NULL AUTO_INCREMENT,
                                `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `primary_uid`       varchar(128) NOT NULL,
                                `approval_type`     varchar(64)           DEFAULT '',
                                `template_name`     varchar(256)          DEFAULT NULL,
                                `template_identity` varchar(512)          DEFAULT NULL,
                                `appro_url`         text,
                                `template_content`  longtext,
                                PRIMARY KEY (`id`),
                                KEY `cache_appro_template_puid` (`primary_uid`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        CREATE TABLE `rdp_async_task`
                            (
                                `id`                 bigint       NOT NULL AUTO_INCREMENT,
                                `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `title`              varchar(256) NOT NULL,
                                `description`        text,
                                `biz_id`             varchar(128)          DEFAULT NULL,
                                `biz_type`           varchar(64)           DEFAULT NULL,
                                `console_ip`         varchar(32)           DEFAULT NULL,
                                `depend_on_biz_id`   varchar(128)          DEFAULT NULL,
                                `depend_on_biz_type` varchar(64)           DEFAULT NULL,
                                `owner_uid`          varchar(256) NOT NULL,
                                `handler_name`       text         NOT NULL,
                                `handler_type`       text         NOT NULL,
                                `config_data`        text,
                                `show_in_dock`       smallint     NOT NULL DEFAULT '0',
                                `process_type`       varchar(64)  NOT NULL DEFAULT 'SCROLL',
                                `process_value`      bigint       NOT NULL DEFAULT '0',
                                `fast_fail`          smallint     NOT NULL DEFAULT '0',
                                `status`             varchar(32)  NOT NULL,
                                `status_msg`         text,
                                `time_of_start`      datetime              DEFAULT NULL,
                                `time_of_last`       datetime              DEFAULT NULL,
                                `time_of_finish`     datetime              DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `rdp_async_task_biz_idx` (`biz_id`, `biz_type`),
                                KEY `rdp_async_task_owner_uid` (`owner_uid`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        CREATE TABLE `rdp_env_param`
                            (
                                `id`           bigint       NOT NULL AUTO_INCREMENT,
                                `gmt_create`   datetime              DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified` datetime              DEFAULT CURRENT_TIMESTAMP,
                                `env_id`       bigint       NOT NULL,
                                `config_key`   varchar(64)  NOT NULL DEFAULT '',
                                `config_value` varchar(256) NOT NULL DEFAULT '',
                                `primary_uid`  varchar(256) NOT NULL DEFAULT '',
                                PRIMARY KEY (`id`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        CREATE TABLE `rdp_ticket_process`
                            (
                                `id`             bigint       NOT NULL AUTO_INCREMENT,
                                `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                `ticket_id`      bigint       NOT NULL COMMENT 'The work order ID associated with the approval process',
                                `ticket_stage`   varchar(64)  NOT NULL COMMENT 'The name of the approval process stage, consisting of four stages: creation, approval, confirmation, execution, and completion',
                                `next_id`        bigint                DEFAULT NULL COMMENT 'The ID of the next step, empty indicates the last step',
                                `appro_biz`      varchar(64)  NOT NULL COMMENT 'Approval types: SQL execution, permission application',
                                `finish_time`    datetime              DEFAULT NULL COMMENT 'The completion time of this process execution',
                                `stage_context`  longtext COMMENT 'Save contextual information for each stage (if necessary)',
                                `deleted`        tinyint(1)   NOT NULL DEFAULT '0',
                                `process_status` varchar(255) NOT NULL DEFAULT 'INIT',
                                PRIMARY KEY (`id`),
                                KEY `idx_data_source_id` (`ticket_id`),
                                KEY `ticket_status` (`ticket_id`, `ticket_stage`)
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """, """
                        CREATE TABLE `rdp_approval_person`
                            (
                                `id`           bigint       NOT NULL AUTO_INCREMENT,
                                `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `ticket_bz_id` varchar(255) NOT NULL,
                                `person_uid`   varchar(255) NOT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_bz_uid` (`ticket_bz_id`, `person_uid`) USING BTREE
                            ) ENGINE = InnoDB
                              DEFAULT CHARSET = utf8mb4
                    """);
    }

}
