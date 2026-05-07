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

public class V202605070001__init_sql extends BaseJavaMigration {

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
        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_upgrade_from_biz`\n" + "    (\n" + "        `id`           bigint(20) NOT NULL AUTO_INCREMENT,\n"
                 + "        `upgrade_time` datetime   NOT NULL,\n" + "        PRIMARY KEY (`id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_user`\n" + "    (\n" + "        `id`                         bigint                                  NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`                        varchar(127) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `username`                   varchar(255) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `email`                      varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `phone`                      varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `sub_account`                varchar(128) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n"
                 + "        `company`                    varchar(128) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n"
                 + "        `password`                   varchar(512) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `op_password`                varchar(512) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n"
                 + "        `role_id`                    bigint                                           DEFAULT NULL,\n"
                 + "        `access_key`                 varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `secret_key`                 varchar(512) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `last_try_login_time`        datetime                                NOT NULL DEFAULT '1970-01-01 12:00:00',\n"
                 + "        `login_fail_count`           int                                     NOT NULL DEFAULT '0',\n"
                 + "        `login_locked`               tinyint(1)                              NOT NULL DEFAULT '0',\n"
                 + "        `last_try_op_verify_time`    datetime                                NOT NULL DEFAULT '1970-01-01 12:00:00',\n"
                 + "        `op_verify_fail_count`       int                                     NOT NULL DEFAULT '0',\n"
                 + "        `op_locked`                  tinyint(1)                              NOT NULL DEFAULT '0',\n"
                 + "        `account_type`               varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,\n"
                 + "        `user_domain`                varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `disable`                    tinyint(1)                              NOT NULL DEFAULT '0',\n"
                 + "        `parent_id`                  bigint                                           DEFAULT NULL,\n"
                 + "        `maintainer`                 tinyint(1)                              NOT NULL DEFAULT '0',\n"
                 + "        `aliyun_ak`                  varchar(127) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n"
                 + "        `aliyun_sk`                  varchar(512) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n"
                 + "        `last_date_update_aliyun_ak` datetime                                NOT NULL DEFAULT '1970-01-01 12:00:00',\n"
                 + "        `bind_type`                  varchar(64) COLLATE utf8mb4_general_ci           DEFAULT NULL,\n"
                 + "        `bind_account`               varchar(256) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n"
                 + "        `phone_area_code`            varchar(128)                                     DEFAULT NULL,\n"
                 + "        `user_status`                varchar(127)                            NOT NULL DEFAULT 'NORMAL',\n"
                 + "        `src`                        varchar(127)                                     DEFAULT NULL,\n"
                 + "        `client_id`                  varchar(255)                                     DEFAULT NULL,\n"
                 + "        `keyword`                    varchar(255)                                     DEFAULT NULL,\n"
                 + "        `contact_me`                 boolean                                          default false,\n"
                 + "        `country`                    varchar(128)                                     DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        UNIQUE KEY `idx_unique_ak` (`access_key`(127)),\n" + "        KEY `idx_username` (`username`(64)),\n" + "        KEY `idx_uid` (`uid`),\n"
                 + "        KEY `idx_sub_account` (`sub_account`),\n" + "        KEY `idx_phone_parent_id` (`phone`, `parent_id`),\n"
                 + "        KEY `idx_email_parent_id` (`email`, `parent_id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_role`\n" + "    (\n" + "        `id`               bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `owner_uid`        varchar(127) NOT NULL,\n" + "        `role_name`        varchar(127) NOT NULL,\n"
                 + "        `role_auth_labels` longtext     NOT NULL,\n" + "        `alias_name`       varchar(127) null,\n"
                 + "        `inner_tag`        tinyint(1)   NOT NULL DEFAULT '0',\n" + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_owner_uid` (`owner_uid`)\n"
                 + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_verify_code`\n" + "    (\n" + "        `id`                    bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `account_type`          varchar(64)  NOT NULL,\n" + "        `uid`                   varchar(127)          DEFAULT NULL,\n"
                 + "        `verify_type`           varchar(128) NOT NULL,\n" + "        `email`                 varchar(512)          DEFAULT NULL,\n"
                 + "        `phone`                 varchar(255)          DEFAULT NULL,\n" + "        `phone_area_code`       varchar(128)          DEFAULT NULL,\n"
                 + "        `verify_code`           varchar(128)          DEFAULT NULL,\n" + "        `verify_code_type`      varchar(128) NOT NULL,\n"
                 + "        `verify_code_send_time` datetime              DEFAULT NULL,\n" + "        `fail_times`            int(11)      NOT NULL DEFAULT 0,\n"
                 + "        `last_fail_date`        datetime              DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_uid` (`uid`),\n"
                 + "        KEY `idx_email` (`email`(127)),\n" + "        KEY `idx_phone` (`phone`(127))\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_user_kv_base_config`\n" + "    (\n" + "        `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`                  varchar(127) NOT NULL,\n" + "        `config_name`          varchar(64)  NOT NULL,\n"
                 + "        `config_value`         longtext              DEFAULT NULL,\n" + "        `default_value`        longtext              DEFAULT NULL,\n"
                 + "        `value_range`          longtext              DEFAULT NULL,\n"
                 + "        `read_only`            tinyint(1)   NOT NULL COMMENT 'is this config can be change',\n" + "        `user_config_tag_type` varchar(128) NOT NULL,\n"
                 + "        `conf_belong`          varchar(128) NOT NULL,\n" + "        `is_secret`            int          NOT NULL DEFAULT 0,\n"
                 + "        `desc_key`             varchar(512) NOT NULL COMMENT 'config description i18n key',\n" + "        PRIMARY KEY (`id`),\n"
                 + "        KEY `idx_uid` (`uid`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_user_auth`\n" + "    (\n" + "        `id`           bigint(20) NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`          varchar(127)        DEFAULT NULL,\n" + "        `auth_id`      bigint(20) NOT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        KEY `idx_uid` (`uid`),\n" + "        KEY `idx_auth_id` (`auth_id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_sys_config`\n" + "    (\n" + "        `id`           bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`          varchar(127)          DEFAULT NULL,\n" + "        `config_name`  varchar(127) NOT NULL,\n"
                 + "        `config_value` varchar(512)          DEFAULT NULL,\n" + "        `description`  text                  DEFAULT NULL,\n" + "        PRIMARY KEY (`id`)\n"
                 + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_data_source`\n" + "    (\n" + "        `id`                          bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`                  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`                datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`                         varchar(127) NOT NULL,\n" + "        `owner`                       varchar(255) NOT NULL,\n"
                 + "        `deploy_type`                 varchar(128) NOT NULL,\n" + "        `region`                      varchar(128) NOT NULL,\n"
                 + "        `data_source_type`            varchar(128) NOT NULL,\n" + "        `host`                        varchar(512) NOT NULL,\n"
                 + "        `private_host`                varchar(512) NOT NULL,\n" + "        `public_host`                 varchar(512)          DEFAULT NULL,\n"
                 + "        `host_type`                   varchar(64)  NOT NULL,\n" + "        `instance_desc`               varchar(512) NOT NULL DEFAULT 'No description',\n"
                 + "        `version`                     varchar(255)          DEFAULT NULL,\n" + "        `driver`                      varchar(255)          DEFAULT NULL,\n"
                 + "        `instance_id`                 varchar(512)          DEFAULT NULL,\n" + "        `account`                     varchar(512)          DEFAULT NULL,\n"
                 + "        `password`                    varchar(512)          DEFAULT NULL,\n" + "        `access_key`                  varchar(128)          DEFAULT NULL,\n"
                 + "        `secret_key`                  varchar(512)          DEFAULT NULL,\n"
                 + "        `security_file_url`           varchar(255)          DEFAULT NULL COMMENT 'krb5.conf,xxx.jks',\n"
                 + "        `secret_file_url`             varchar(255)          DEFAULT NULL COMMENT 'xxx.keytab',\n"
                 + "        `security_file_store_type`    varchar(64)           DEFAULT NULL,\n"
                 + "        `security_type`               varchar(64)  NOT NULL DEFAULT 'USER_PASSWD',\n"
                 + "        `public_security_type`        varchar(64)           DEFAULT NULL,\n" + "        `client_trust_store_password` varchar(512)          DEFAULT NULL,\n"
                 + "        `life_cycle_state`            varchar(32)  NOT NULL DEFAULT 'CREATED',\n"
                 + "        `console_job_id`              bigint(20)            DEFAULT NULL,\n" + "        `parent_ds_id`                bigint(20)            DEFAULT NULL,\n"
                 + "        `connect_type`                varchar(64)           DEFAULT NULL,\n" + "        `ds_env_id`                   bigint(20)            DEFAULT NULL,\n"
                 + "        `default_db_name`             varchar(255)          DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_host` (`host`(127)),\n"
                 + "        KEY `idx_instance_id` (`instance_id`(127)),\n" + "        KEY `idx_parent_ds_id` (`parent_ds_id`)\n" + "    ) ENGINE = InnoDB\n"
                 + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_ds_usage`\n" + "    (\n" + "        `id`              bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + "        `ds_id`           bigint(20)   NOT NULL,\n"
                 + "        `res_type`        varchar(255) NOT NULL,\n" + "        `res_id`          bigint(20)   NOT NULL,\n"
                 + "        `res_instance_id` varchar(255) NOT NULL,\n" + "        `endpoint`        varchar(128) NOT NULL,\n" + "        PRIMARY KEY (`id`)\n"
                 + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_ds_kv_base_config`\n" + "    (\n" + "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `data_source_id`    bigint(20)   NOT NULL,\n" + "        `config_name`       varchar(64)  NOT NULL,\n"
                 + "        `config_group`      varchar(64)  NOT NULL,\n" + "        `display`           int          NOT NULL DEFAULT 1,\n"
                 + "        `desc_key`          varchar(512) NOT NULL COMMENT 'config description i18n key',\n" + "        `value_require`     int          NOT NULL DEFAULT 0,\n"
                 + "        `value_valid_regex` varchar(512)          DEFAULT NULL,\n" + "        `config_value`      longtext              DEFAULT NULL,\n"
                 + "        `default_value`     varchar(716)          DEFAULT NULL,\n" + "        `value_advance`     varchar(716)          DEFAULT NULL,\n"
                 + "        `read_only`         int          NOT NULL COMMENT 'is this config can be change',\n" + "        `is_secret`         int          NOT NULL DEFAULT 0,\n"
                 + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_ds_id_config_name` (`data_source_id`, `config_name`),\n"
                 + "        KEY `idx_data_source_id` (`data_source_id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_blob_resource`\n" + "    (\n" + "        `id`           bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + "        `instance_id`  varchar(64)  NOT NULL,\n"
                 + "        `owner_name`   varchar(255) NOT NULL,\n" + "        `owner_type`   varchar(64)  NOT NULL,\n" + "        `blob_type`    varchar(64)  NOT NULL,\n"
                 + "        `content`      mediumblob   NOT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        KEY `idx_instance_id_owner_type_blob_type` (`instance_id`(32), `owner_type`(32), `blob_type`(32))\n" + "    ) ENGINE = InnoDB\n"
                 + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_op_audit`\n" + "    (\n" + "        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + "        `uid`            varchar(127) NOT NULL,\n"
                 + "        `operate_date`   datetime     NOT NULL,\n" + "        `source_ip`      varchar(128)          DEFAULT NULL,\n"
                 + "        `resource_type`  varchar(255) NOT NULL,\n" + "        `resource_value` varchar(512) NOT NULL,\n"
                 + "        `security_level` varchar(64)  NOT NULL DEFAULT 'NORMAL',\n" + "        `context_in`     text                  DEFAULT NULL,\n"
                 + "        `context_out`    text                  DEFAULT NULL,\n" + "        `uuid_key`       varchar(255)          DEFAULT NULL,\n"
                 + "        PRIMARY KEY (`id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_alert_event_log`\n" + "    (\n" + "        `id`               bigint(20)  NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + "        `status`           varchar(64) NOT NULL,\n"
                 + "        `content`          longtext    NOT NULL,\n" + "        `ip`               varchar(64) NOT NULL,\n" + "        `err_msg`          text,\n"
                 + "        `uid`              varchar(127)         DEFAULT NULL,\n" + "        `alert_media_type` varchar(64)          DEFAULT NULL,\n"
                 + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_gmt_create` (`gmt_create`),\n" + "        KEY `idx_uid` (`uid`)\n" + "    ) ENGINE = InnoDB\n"
                 + "      AUTO_INCREMENT = 3\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_data_source_history`\n" + "    (\n" + "        `id`                       bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`               datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`             datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`                      varchar(127) NOT NULL,\n" + "        `owner`                    varchar(255) NOT NULL,\n"
                 + "        `deploy_type`              varchar(128) NOT NULL,\n" + "        `region`                   varchar(128) NOT NULL,\n"
                 + "        `data_source_type`         varchar(128) NOT NULL,\n" + "        `host`                     varchar(512) NOT NULL,\n"
                 + "        `private_host`             varchar(512) NOT NULL,\n" + "        `public_host`              varchar(512)          DEFAULT NULL,\n"
                 + "        `host_type`                varchar(64)  NOT NULL,\n" + "        `instance_desc`            varchar(512) NOT NULL DEFAULT 'No description',\n"
                 + "        `version`                  varchar(255)          DEFAULT NULL,\n" + "        `instance_id`              varchar(512)          DEFAULT NULL,\n"
                 + "        `account`                  varchar(512)          DEFAULT NULL,\n" + "        `password`                 varchar(512)          DEFAULT NULL,\n"
                 + "        `access_key`               varchar(128)          DEFAULT NULL,\n" + "        `secret_key`               varchar(512)          DEFAULT NULL,\n"
                 + "        `auto_create_account`      varchar(64)  NOT NULL DEFAULT 'NOT_CREATE',\n" + "        `security_file_url`        varchar(512)          DEFAULT NULL,\n"
                 + "        `security_file_store_type` varchar(64)           DEFAULT NULL,\n" + "        `security_type`            varchar(64)  NOT NULL DEFAULT 'USER_PASSWD',\n"
                 + "        `public_security_type`     varchar(64)           DEFAULT NULL,\n" + "        `life_cycle_state`         varchar(32)  NOT NULL DEFAULT 'CREATED',\n"
                 + "        `console_job_id`           bigint(20)            DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n" + "        KEY `idx_host` (`host`(127)),\n"
                 + "        KEY `idx_instance_id` (`instance_id`(127))\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_res_auth`\n" + "    (\n" + "        `id`             bigint                                  NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`   datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `owner_uid`      varchar(127) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `res_id`         bigint(20)                                  NOT NULL,\n" + "        `res_inst_id`    varchar(512)                            NULL,\n"
                 + "        `res_desc`       varchar(512)                            NOT NULL,\n" + "        `res_path`       varchar(512) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `level_one`      varchar(512) COLLATE utf8mb4_general_ci NOT NULL,\n" + "        `level_two`      varchar(512) COLLATE utf8mb4_general_ci NULL,\n"
                 + "        `level_three`    varchar(512) COLLATE utf8mb4_general_ci NULL,\n" + "        `level_four`     varchar(512) COLLATE utf8mb4_general_ci NULL,\n"
                 + "        `start_time`     datetime                                         DEFAULT NULL,\n"
                 + "        `end_time`       datetime                                         DEFAULT NULL,\n"
                 + "        `kind_type`      varchar(64) COLLATE utf8mb4_general_ci NOT NULL,\n"
                 + "        `res_auth_label` text COLLATE utf8mb4_general_ci                  DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        UNIQUE KEY `uk_path` (`res_id`, `res_path`, `kind_type`, `owner_uid`),\n" + "        KEY `idx_level_one` (`res_id`, `kind_type`),\n"
                 + "        KEY `idx_level_two` (`res_id`, `kind_type`, `level_one`(127)),\n"
                 + "        KEY `idx_level_three` (`res_id`, `kind_type`, `level_one`(127), `level_two`(127)),\n"
                 + "        KEY `idx_level_four` (`res_id`, `kind_type`, `level_one`(127), `level_two`(127), `level_three`(127)),\n"
                 + "        KEY `idx_owner_uid` (`owner_uid`),\n" + "        KEY `idx_res_id` (`res_id`),\n" + "        KEY `idx_path` (`res_path`)\n" + "    ) ENGINE = InnoDB\n"
                 + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_ds_env`\n" + "    (\n" + "        `id`           bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + "        `owner_uid`    varchar(127) NOT NULL,\n"
                 + "        `env_name`     varchar(64)  NOT NULL,\n" + "        `description`  varchar(512)          DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        KEY `idx_owner_uid` (`owner_uid`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_product_cluster`\n" + "    (\n" + "        `id`              bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + "        `product`         varchar(127) NOT NULL,\n"
                 + "        `product_version` varchar(127) NOT NULL,\n" + "        `cluster_name`    varchar(127) NOT NULL,\n"
                 + "        `cluster_desc`    varchar(728) NOT NULL,\n" + "        `cluster_code`    varchar(127) NOT NULL,\n"
                 + "        `api_addr`        varchar(728) NOT NULL,\n" + "        PRIMARY KEY (`id`),\n" + "        UNIQUE KEY `uk_cluster_name` (`cluster_name`),\n"
                 + "        UNIQUE KEY `uk_cluster_code` (`cluster_code`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_web_view_log`\n" + "    (\n" + "        `id`           bigint(20) NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`          varchar(127)        DEFAULT NULL,\n" + "        `src`          varchar(127)        DEFAULT NULL,\n"
                 + "        `keyword`      varchar(255)        DEFAULT NULL,\n" + "        `uri`          varchar(512)        DEFAULT NULL,\n"
                 + "        `client_id`    varchar(255)        DEFAULT NULL,\n" + "        `vb_id`        varchar(512)        DEFAULT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        KEY `idx_src` (`src`),\n" + "        KEY `idx_uid` (`uid`),\n" + "        KEY `idx_uri` (`uri`(127)),\n"
                 + "        KEY `idx_client_id` (`client_id`(127))\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_product`\n" + "    (\n" + "        `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `product_type`         varchar(64)  NOT NULL,\n" + "        `product_version`      varchar(128) NOT NULL,\n"
                 + "        `pkg_md5`              varchar(128) NOT NULL,\n" + "        `oss_bucket`           varchar(64)  NOT NULL,\n"
                 + "        `oss_object_name`      varchar(64)  NOT NULL,\n" + "        `oss_end_point`        varchar(255) NOT NULL,\n"
                 + "        `oss_download_site`    varchar(255) NOT NULL,\n" + "        `product_version_type` varchar(64)           DEFAULT NULL,\n"
                 + "        PRIMARY KEY (`id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_user_download`\n" + "    (\n" + "        `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `gmt_modified`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                 + "        `uid`                  varchar(127) NOT NULL,\n" + "        `username`             varchar(255) NOT NULL,\n"
                 + "        `company`              varchar(128)          DEFAULT NULL,\n" + "        `product_type`         varchar(64)  NOT NULL,\n"
                 + "        `product_version`      varchar(128) NOT NULL,\n" + "        `product_version_type` varchar(64)           DEFAULT NULL,\n"
                 + "        PRIMARY KEY (`id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");
    }
}
