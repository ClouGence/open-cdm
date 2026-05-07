package com.clougence.clouddm.init.component.scripts;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import com.clougence.utils.ExceptionUtils;

public class V202605070007__dm_order extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Connection c = context.getConnection();
        for (String sql : sqls) {
            safeExecute(c, sql);
        }
    }

    private static final Set<Integer> errorCodes = new HashSet<>();
    static {
        errorCodes.add(1060);
        errorCodes.add(1061);
        errorCodes.add(1062);
        errorCodes.add(1050);
        errorCodes.add(1072);
        errorCodes.add(1091);
    }

    private void safeExecute(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            // MySQL / OceanBase / TiDB
            // 1060 = Duplicate column name
            // 1050 = Table exists (for CREATE)
            // 1061 = Duplicate key name
            // 1091 = Can't DROP ... ; check that column/key exists
            if (errorCodes.contains(e.getErrorCode())) {
                System.out.println("Flyway java exec error but skip, msg:" + ExceptionUtils.getRootCauseMessage(e) + ", sql: " + sql);
                return;
            }

            throw new RuntimeException("Failed to execute: " + sql, e);
        }
    }

    private static final List<String> sqls = new ArrayList<>();
    static {
        sqls.add("CREATE TABLE `rdp_ticket_inst`\n" + "    (\n" + "        `id`                      bigint        NOT NULL AUTO_INCREMENT,\n"
                 + "        `biz_id`                  varchar(32)   NOT NULL,\n" + "        `gmt_create`              datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`            datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `owner_uid`               varchar(255)  NOT NULL,\n" + "        `bind_ds_id`              bigint        NOT NULL,\n"
                 + "        `target_info`             varchar(1024) NOT NULL,\n" + "        `appro_type`              varchar(512)  NOT NULL,\n"
                 + "        `appro_biz`               varchar(64)   NOT NULL,\n" + "        `appro_identity`          varchar(512)           DEFAULT NULL,\n"
                 + "        `appro_template_name`     varchar(256)           DEFAULT NULL,\n" + "        `appro_template_identity` varchar(512)           DEFAULT NULL,\n"
                 + "        `appro_comment`           text,\n" + "        `description`             varchar(1024)          DEFAULT NULL,\n"
                 + "        `ticket_title`            varchar(512)  NOT NULL,\n" + "        `ticket_status`           varchar(512)  NOT NULL,\n"
                 + "        `finish_time`             datetime               DEFAULT NULL,\n" + "        `deleted`                 tinyint(1)    NOT NULL DEFAULT '0',\n"
                 + "        `status_message`          text,\n" + "        `error_count`             int                    DEFAULT NULL,\n"
                 + "        `primary_uid`             varchar(255)  NOT NULL,\n" + "        PRIMARY KEY (`id`),\n" + "        UNIQUE KEY `idx_unique_biz_id` (`biz_id`),\n"
                 + "        KEY `idx_data_source_id` (`bind_ds_id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE `rdp_cache_appro_template`\n" + "    (\n" + "        `id`                bigint       NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + "        `primary_uid`       varchar(128) NOT NULL,\n"
                 + "        `approval_type`     varchar(64)           DEFAULT '',\n" + "        `template_name`     varchar(256)          DEFAULT NULL,\n"
                 + "        `template_identity` varchar(512)          DEFAULT NULL,\n" + "        `appro_url`         text,\n" + "        `template_content`  longtext,\n"
                 + "        PRIMARY KEY (`id`),\n" + "        KEY `cache_appro_template_puid` (`primary_uid`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE `rdp_async_task`\n" + "    (\n" + "        `id`                 bigint       NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + "        `title`              varchar(256) NOT NULL,\n"
                 + "        `description`        text,\n" + "        `biz_id`             varchar(128)          DEFAULT NULL,\n"
                 + "        `biz_type`           varchar(64)           DEFAULT NULL,\n" + "        `console_ip`         varchar(32)           DEFAULT NULL,\n"
                 + "        `depend_on_biz_id`   varchar(128)          DEFAULT NULL,\n" + "        `depend_on_biz_type` varchar(64)           DEFAULT NULL,\n"
                 + "        `owner_uid`          varchar(256) NOT NULL,\n" + "        `handler_name`       text         NOT NULL,\n"
                 + "        `handler_type`       text         NOT NULL,\n" + "        `config_data`        text,\n"
                 + "        `show_in_dock`       smallint     NOT NULL DEFAULT '0',\n" + "        `process_type`       varchar(64)  NOT NULL DEFAULT 'SCROLL',\n"
                 + "        `process_value`      bigint       NOT NULL DEFAULT '0',\n" + "        `fast_fail`          smallint     NOT NULL DEFAULT '0',\n"
                 + "        `status`             varchar(32)  NOT NULL,\n" + "        `status_msg`         text,\n"
                 + "        `time_of_start`      datetime              DEFAULT NULL,\n" + "        `time_of_last`       datetime              DEFAULT NULL,\n"
                 + "        `time_of_finish`     datetime              DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        UNIQUE KEY `rdp_async_task_biz_idx` (`biz_id`, `biz_type`),\n" + "        KEY `rdp_async_task_owner_uid` (`owner_uid`)\n" + "    ) ENGINE = InnoDB\n"
                 + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE `rdp_env_param`\n" + "    (\n" + "        `id`           bigint       NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime              DEFAULT CURRENT_TIMESTAMP,\n" + "        `gmt_modified` datetime              DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `env_id`       bigint       NOT NULL,\n" + "        `config_key`   varchar(64)  NOT NULL DEFAULT '',\n"
                 + "        `config_value` varchar(256) NOT NULL DEFAULT '',\n" + "        `primary_uid`  varchar(256) NOT NULL DEFAULT '',\n" + "        PRIMARY KEY (`id`)\n"
                 + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE `rdp_ticket_process`\n" + "    (\n" + "        `id`             bigint       NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `ticket_id`      bigint       NOT NULL COMMENT 'The work order ID associated with the approval process',\n"
                 + "        `ticket_stage`   varchar(64)  NOT NULL COMMENT 'The name of the approval process stage, consisting of four stages: creation, approval, confirmation, execution, and completion',\n"
                 + "        `next_id`        bigint                DEFAULT NULL COMMENT 'The ID of the next step, empty indicates the last step',\n"
                 + "        `appro_biz`      varchar(64)  NOT NULL COMMENT 'Approval types: SQL execution, permission application',\n"
                 + "        `finish_time`    datetime              DEFAULT NULL COMMENT 'The completion time of this process execution',\n"
                 + "        `stage_context`  longtext COMMENT 'Save contextual information for each stage (if necessary)',\n"
                 + "        `deleted`        tinyint(1)   NOT NULL DEFAULT '0',\n" + "        `process_status` varchar(255) NOT NULL DEFAULT 'INIT',\n"
                 + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_data_source_id` (`ticket_id`),\n" + "        KEY `ticket_status` (`ticket_id`, `ticket_stage`)\n"
                 + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE `rdp_approval_person`\n" + "    (\n" + "        `id`           bigint       NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + "        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `ticket_bz_id` varchar(255) NOT NULL,\n" + "        `person_uid`   varchar(255) NOT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        UNIQUE KEY `uk_bz_uid` (`ticket_bz_id`, `person_uid`) USING BTREE\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");
    }
}
