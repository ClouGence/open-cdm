package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070001__init_sql extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    CREATE TABLE IF NOT EXISTS `rdp_upgrade_from_biz`
                        (
                            `id`           bigint(20) NOT NULL AUTO_INCREMENT,
                            `upgrade_time` datetime   NOT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_user`
                        (
                            `id`                         bigint                                  NOT NULL AUTO_INCREMENT,
                            `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`                        varchar(127) COLLATE utf8mb4_general_ci NOT NULL,
                            `username`                   varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                            `email`                      varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
                            `phone`                      varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
                            `sub_account`                varchar(128) COLLATE utf8mb4_general_ci          DEFAULT NULL,
                            `company`                    varchar(128) COLLATE utf8mb4_general_ci          DEFAULT NULL,
                            `password`                   varchar(512) COLLATE utf8mb4_general_ci NOT NULL,
                            `op_password`                varchar(512) COLLATE utf8mb4_general_ci          DEFAULT NULL,
                            `role_id`                    bigint                                           DEFAULT NULL,
                            `access_key`                 varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
                            `secret_key`                 varchar(512) COLLATE utf8mb4_general_ci NOT NULL,
                            `last_try_login_time`        datetime                                NOT NULL DEFAULT '1970-01-01 12:00:00',
                            `login_fail_count`           int                                     NOT NULL DEFAULT '0',
                            `login_locked`               tinyint(1)                              NOT NULL DEFAULT '0',
                            `last_try_op_verify_time`    datetime                                NOT NULL DEFAULT '1970-01-01 12:00:00',
                            `op_verify_fail_count`       int                                     NOT NULL DEFAULT '0',
                            `op_locked`                  tinyint(1)                              NOT NULL DEFAULT '0',
                            `account_type`               varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,
                            `user_domain`                varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
                            `disable`                    tinyint(1)                              NOT NULL DEFAULT '0',
                            `parent_id`                  bigint                                           DEFAULT NULL,
                            `maintainer`                 tinyint(1)                              NOT NULL DEFAULT '0',
                            `aliyun_ak`                  varchar(127) COLLATE utf8mb4_general_ci          DEFAULT NULL,
                            `aliyun_sk`                  varchar(512) COLLATE utf8mb4_general_ci          DEFAULT NULL,
                            `last_date_update_aliyun_ak` datetime                                NOT NULL DEFAULT '1970-01-01 12:00:00',
                            `bind_type`                  varchar(64) COLLATE utf8mb4_general_ci           DEFAULT NULL,
                            `bind_account`               varchar(256) COLLATE utf8mb4_general_ci          DEFAULT NULL,
                            `phone_area_code`            varchar(128)                                     DEFAULT NULL,
                            `user_status`                varchar(127)                            NOT NULL DEFAULT 'NORMAL',
                            `src`                        varchar(127)                                     DEFAULT NULL,
                            `client_id`                  varchar(255)                                     DEFAULT NULL,
                            `keyword`                    varchar(255)                                     DEFAULT NULL,
                            `contact_me`                 boolean                                          default false,
                            `country`                    varchar(128)                                     DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `idx_unique_ak` (`access_key`(127)),
                            KEY `idx_username` (`username`(64)),
                            KEY `idx_uid` (`uid`),
                            KEY `idx_sub_account` (`sub_account`),
                            KEY `idx_phone_parent_id` (`phone`, `parent_id`),
                            KEY `idx_email_parent_id` (`email`, `parent_id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_role`
                        (
                            `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `owner_uid`        varchar(127) NOT NULL,
                            `role_name`        varchar(127) NOT NULL,
                            `role_auth_labels` longtext     NOT NULL,
                            `alias_name`       varchar(127) null,
                            `inner_tag`        tinyint(1)   NOT NULL DEFAULT '0',
                            PRIMARY KEY (`id`),
                            KEY `idx_owner_uid` (`owner_uid`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_verify_code`
                        (
                            `id`                    bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `account_type`          varchar(64)  NOT NULL,
                            `uid`                   varchar(127)          DEFAULT NULL,
                            `verify_type`           varchar(128) NOT NULL,
                            `email`                 varchar(512)          DEFAULT NULL,
                            `phone`                 varchar(255)          DEFAULT NULL,
                            `phone_area_code`       varchar(128)          DEFAULT NULL,
                            `verify_code`           varchar(128)          DEFAULT NULL,
                            `verify_code_type`      varchar(128) NOT NULL,
                            `verify_code_send_time` datetime              DEFAULT NULL,
                            `fail_times`            int(11)      NOT NULL DEFAULT 0,
                            `last_fail_date`        datetime              DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_uid` (`uid`),
                            KEY `idx_email` (`email`(127)),
                            KEY `idx_phone` (`phone`(127))
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_user_kv_base_config`
                        (
                            `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`                  varchar(127) NOT NULL,
                            `config_name`          varchar(64)  NOT NULL,
                            `config_value`         longtext              DEFAULT NULL,
                            `default_value`        longtext              DEFAULT NULL,
                            `value_range`          longtext              DEFAULT NULL,
                            `read_only`            tinyint(1)   NOT NULL COMMENT 'is this config can be change',
                            `user_config_tag_type` varchar(128) NOT NULL,
                            `conf_belong`          varchar(128) NOT NULL,
                            `is_secret`            int          NOT NULL DEFAULT 0,
                            `desc_key`             varchar(512) NOT NULL COMMENT 'config description i18n key',
                            PRIMARY KEY (`id`),
                            KEY `idx_uid` (`uid`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_user_auth`
                        (
                            `id`           bigint(20) NOT NULL AUTO_INCREMENT,
                            `gmt_create`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`          varchar(127)        DEFAULT NULL,
                            `auth_id`      bigint(20) NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_uid` (`uid`),
                            KEY `idx_auth_id` (`auth_id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_sys_config`
                        (
                            `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`          varchar(127)          DEFAULT NULL,
                            `config_name`  varchar(127) NOT NULL,
                            `config_value` varchar(512)          DEFAULT NULL,
                            `description`  text                  DEFAULT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_data_source`
                        (
                            `id`                          bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`                  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`                datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`                         varchar(127) NOT NULL,
                            `owner`                       varchar(255) NOT NULL,
                            `deploy_type`                 varchar(128) NOT NULL,
                            `region`                      varchar(128) NOT NULL,
                            `data_source_type`            varchar(128) NOT NULL,
                            `host`                        varchar(512) NOT NULL,
                            `private_host`                varchar(512) NOT NULL,
                            `public_host`                 varchar(512)          DEFAULT NULL,
                            `host_type`                   varchar(64)  NOT NULL,
                            `instance_desc`               varchar(512) NOT NULL DEFAULT 'No description',
                            `version`                     varchar(255)          DEFAULT NULL,
                            `driver`                      varchar(255)          DEFAULT NULL,
                            `instance_id`                 varchar(512)          DEFAULT NULL,
                            `account`                     varchar(512)          DEFAULT NULL,
                            `password`                    varchar(512)          DEFAULT NULL,
                            `access_key`                  varchar(128)          DEFAULT NULL,
                            `secret_key`                  varchar(512)          DEFAULT NULL,
                            `security_file_url`           varchar(255)          DEFAULT NULL COMMENT 'krb5.conf,xxx.jks',
                            `secret_file_url`             varchar(255)          DEFAULT NULL COMMENT 'xxx.keytab',
                            `security_file_store_type`    varchar(64)           DEFAULT NULL,
                            `security_type`               varchar(64)  NOT NULL DEFAULT 'USER_PASSWD',
                            `public_security_type`        varchar(64)           DEFAULT NULL,
                            `client_trust_store_password` varchar(512)          DEFAULT NULL,
                            `life_cycle_state`            varchar(32)  NOT NULL DEFAULT 'CREATED',
                            `console_job_id`              bigint(20)            DEFAULT NULL,
                            `parent_ds_id`                bigint(20)            DEFAULT NULL,
                            `connect_type`                varchar(64)           DEFAULT NULL,
                            `ds_env_id`                   bigint(20)            DEFAULT NULL,
                            `default_db_name`             varchar(255)          DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_host` (`host`(127)),
                            KEY `idx_instance_id` (`instance_id`(127)),
                            KEY `idx_parent_ds_id` (`parent_ds_id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_ds_usage`
                        (
                            `id`              bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `ds_id`           bigint(20)   NOT NULL,
                            `res_type`        varchar(255) NOT NULL,
                            `res_id`          bigint(20)   NOT NULL,
                            `res_instance_id` varchar(255) NOT NULL,
                            `endpoint`        varchar(128) NOT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_ds_kv_base_config`
                        (
                            `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `data_source_id`    bigint(20)   NOT NULL,
                            `config_name`       varchar(64)  NOT NULL,
                            `config_group`      varchar(64)  NOT NULL,
                            `display`           int          NOT NULL DEFAULT 1,
                            `desc_key`          varchar(512) NOT NULL COMMENT 'config description i18n key',
                            `value_require`     int          NOT NULL DEFAULT 0,
                            `value_valid_regex` varchar(512)          DEFAULT NULL,
                            `config_value`      longtext              DEFAULT NULL,
                            `default_value`     varchar(716)          DEFAULT NULL,
                            `value_advance`     varchar(716)          DEFAULT NULL,
                            `read_only`         int          NOT NULL COMMENT 'is this config can be change',
                            `is_secret`         int          NOT NULL DEFAULT 0,
                            PRIMARY KEY (`id`),
                            KEY `idx_ds_id_config_name` (`data_source_id`, `config_name`),
                            KEY `idx_data_source_id` (`data_source_id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_blob_resource`
                        (
                            `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `instance_id`  varchar(64)  NOT NULL,
                            `owner_name`   varchar(255) NOT NULL,
                            `owner_type`   varchar(64)  NOT NULL,
                            `blob_type`    varchar(64)  NOT NULL,
                            `content`      mediumblob   NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_instance_id_owner_type_blob_type` (`instance_id`(32), `owner_type`(32), `blob_type`(32))
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_op_audit`
                        (
                            `id`             bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`            varchar(127) NOT NULL,
                            `operate_date`   datetime     NOT NULL,
                            `source_ip`      varchar(128)          DEFAULT NULL,
                            `resource_type`  varchar(255) NOT NULL,
                            `resource_value` varchar(512) NOT NULL,
                            `security_level` varchar(64)  NOT NULL DEFAULT 'NORMAL',
                            `context_in`     text                  DEFAULT NULL,
                            `context_out`    text                  DEFAULT NULL,
                            `uuid_key`       varchar(255)          DEFAULT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_alert_event_log`
                        (
                            `id`               bigint(20)  NOT NULL AUTO_INCREMENT,
                            `gmt_create`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `status`           varchar(64) NOT NULL,
                            `content`          longtext    NOT NULL,
                            `ip`               varchar(64) NOT NULL,
                            `err_msg`          text,
                            `uid`              varchar(127)         DEFAULT NULL,
                            `alert_media_type` varchar(64)          DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_gmt_create` (`gmt_create`),
                            KEY `idx_uid` (`uid`)
                        ) ENGINE = InnoDB
                          AUTO_INCREMENT = 3
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_data_source_history`
                        (
                            `id`                       bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`               datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`             datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`                      varchar(127) NOT NULL,
                            `owner`                    varchar(255) NOT NULL,
                            `deploy_type`              varchar(128) NOT NULL,
                            `region`                   varchar(128) NOT NULL,
                            `data_source_type`         varchar(128) NOT NULL,
                            `host`                     varchar(512) NOT NULL,
                            `private_host`             varchar(512) NOT NULL,
                            `public_host`              varchar(512)          DEFAULT NULL,
                            `host_type`                varchar(64)  NOT NULL,
                            `instance_desc`            varchar(512) NOT NULL DEFAULT 'No description',
                            `version`                  varchar(255)          DEFAULT NULL,
                            `instance_id`              varchar(512)          DEFAULT NULL,
                            `account`                  varchar(512)          DEFAULT NULL,
                            `password`                 varchar(512)          DEFAULT NULL,
                            `access_key`               varchar(128)          DEFAULT NULL,
                            `secret_key`               varchar(512)          DEFAULT NULL,
                            `auto_create_account`      varchar(64)  NOT NULL DEFAULT 'NOT_CREATE',
                            `security_file_url`        varchar(512)          DEFAULT NULL,
                            `security_file_store_type` varchar(64)           DEFAULT NULL,
                            `security_type`            varchar(64)  NOT NULL DEFAULT 'USER_PASSWD',
                            `public_security_type`     varchar(64)           DEFAULT NULL,
                            `life_cycle_state`         varchar(32)  NOT NULL DEFAULT 'CREATED',
                            `console_job_id`           bigint(20)            DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_host` (`host`(127)),
                            KEY `idx_instance_id` (`instance_id`(127))
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_res_auth`
                        (
                            `id`             bigint                                  NOT NULL AUTO_INCREMENT,
                            `gmt_create`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`   datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `owner_uid`      varchar(127) COLLATE utf8mb4_general_ci NOT NULL,
                            `res_id`         bigint(20)                                  NOT NULL,
                            `res_inst_id`    varchar(512)                            NULL,
                            `res_desc`       varchar(512)                            NOT NULL,
                            `res_path`       varchar(512) COLLATE utf8mb4_general_ci NOT NULL,
                            `level_one`      varchar(512) COLLATE utf8mb4_general_ci NOT NULL,
                            `level_two`      varchar(512) COLLATE utf8mb4_general_ci NULL,
                            `level_three`    varchar(512) COLLATE utf8mb4_general_ci NULL,
                            `level_four`     varchar(512) COLLATE utf8mb4_general_ci NULL,
                            `start_time`     datetime                                         DEFAULT NULL,
                            `end_time`       datetime                                         DEFAULT NULL,
                            `kind_type`      varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
                            `res_auth_label` text COLLATE utf8mb4_general_ci                  DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_path` (`res_id`, `res_path`, `kind_type`, `owner_uid`),
                            KEY `idx_level_one` (`res_id`, `kind_type`),
                            KEY `idx_level_two` (`res_id`, `kind_type`, `level_one`(127)),
                            KEY `idx_level_three` (`res_id`, `kind_type`, `level_one`(127), `level_two`(127)),
                            KEY `idx_level_four` (`res_id`, `kind_type`, `level_one`(127), `level_two`(127), `level_three`(127)),
                            KEY `idx_owner_uid` (`owner_uid`),
                            KEY `idx_res_id` (`res_id`),
                            KEY `idx_path` (`res_path`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_ds_env`
                        (
                            `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `owner_uid`    varchar(127) NOT NULL,
                            `env_name`     varchar(64)  NOT NULL,
                            `description`  varchar(512)          DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_owner_uid` (`owner_uid`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_product_cluster`
                        (
                            `id`              bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `product`         varchar(127) NOT NULL,
                            `product_version` varchar(127) NOT NULL,
                            `cluster_name`    varchar(127) NOT NULL,
                            `cluster_desc`    varchar(728) NOT NULL,
                            `cluster_code`    varchar(127) NOT NULL,
                            `api_addr`        varchar(728) NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_cluster_name` (`cluster_name`),
                            UNIQUE KEY `uk_cluster_code` (`cluster_code`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_web_view_log`
                        (
                            `id`           bigint(20) NOT NULL AUTO_INCREMENT,
                            `gmt_create`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`          varchar(127)        DEFAULT NULL,
                            `src`          varchar(127)        DEFAULT NULL,
                            `keyword`      varchar(255)        DEFAULT NULL,
                            `uri`          varchar(512)        DEFAULT NULL,
                            `client_id`    varchar(255)        DEFAULT NULL,
                            `vb_id`        varchar(512)        DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_src` (`src`),
                            KEY `idx_uid` (`uid`),
                            KEY `idx_uri` (`uri`(127)),
                            KEY `idx_client_id` (`client_id`(127))
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_product`
                        (
                            `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `product_type`         varchar(64)  NOT NULL,
                            `product_version`      varchar(128) NOT NULL,
                            `pkg_md5`              varchar(128) NOT NULL,
                            `oss_bucket`           varchar(64)  NOT NULL,
                            `oss_object_name`      varchar(64)  NOT NULL,
                            `oss_end_point`        varchar(255) NOT NULL,
                            `oss_download_site`    varchar(255) NOT NULL,
                            `product_version_type` varchar(64)           DEFAULT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """, """
                    CREATE TABLE IF NOT EXISTS `rdp_user_download`
                        (
                            `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,
                            `gmt_create`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`                  varchar(127) NOT NULL,
                            `username`             varchar(255) NOT NULL,
                            `company`              varchar(128)          DEFAULT NULL,
                            `product_type`         varchar(64)  NOT NULL,
                            `product_version`      varchar(128) NOT NULL,
                            `product_version_type` varchar(64)           DEFAULT NULL,
                            PRIMARY KEY (`id`)
                        ) ENGINE = InnoDB
                          DEFAULT CHARSET = utf8mb4\
                """);
    }
}
