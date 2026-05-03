package migration.dm.output;

import com.clougence.utils.ExceptionUtils;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class V202403121434__init_sql extends BaseJavaMigration {

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
            // MySQL / OceanBase 错误码：
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
        sqls.add("CREATE TABLE IF NOT EXISTS `dm_ds_config`\n" +
                "    (\n" +
                "        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `uid`                varchar(255) NOT NULL,\n" +
                "        `data_source_id`     bigint       NOT NULL,\n" +
                "        `data_source_type`   varchar(255) NOT NULL,\n" +
                "        `status`             varchar(128) NULL,\n" +
                "        `status_message`     text         NULL,\n" +
                "        `config_instance_id` varchar(128) NOT NULL,\n" +
                "        `bind_cluster_id`    bigint       NOT NULL,\n" +
                "        `bind_env_id`        bigint       NOT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        unique key ds_id (`data_source_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_ds_statistics`\n" +
                "    (\n" +
                "        `id`               bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `data_source_id`   bigint       not null,\n" +
                "        `data_source_type` varchar(128) not null,\n" +
                "        `last_time`        datetime     NOT NULL,\n" +
                "        `exec_counts`      bigint                default 0,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        unique key ds_id (`data_source_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_ds_tag`\n" +
                "    (\n" +
                "        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `uid`            varchar(128) NOT NULL,\n" +
                "        `data_source_id` bigint       NOT NULL,\n" +
                "        `instance_desc`  varchar(256) NOT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        unique key ds_tag_dsid_uid (`uid`, `data_source_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_ds_kv_base_config`\n" +
                "    (\n" +
                "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `data_source_id`    bigint(20)   NOT NULL,\n" +
                "        `config_name`       varchar(64)  NOT NULL,\n" +
                "        `config_group`      varchar(64)  NOT NULL,\n" +
                "        `display`           int          NOT NULL DEFAULT 1,\n" +
                "        `desc_key`          varchar(512) NOT NULL COMMENT 'config description i18n key',\n" +
                "        `value_require`     int          NOT NULL DEFAULT 0,\n" +
                "        `value_valid_regex` varchar(512)          DEFAULT NULL,\n" +
                "        `config_value`      longtext              DEFAULT NULL,\n" +
                "        `default_value`     varchar(716)          DEFAULT NULL,\n" +
                "        `value_advance`     varchar(716)          DEFAULT NULL,\n" +
                "        `read_only`         int          NOT NULL COMMENT 'is this config can be change',\n" +
                "        `is_secret`         int          NOT NULL DEFAULT 0,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_ds_id_config_name` (`data_source_id`, `config_name`),\n" +
                "        KEY `idx_data_source_id` (`data_source_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `console_job`\n" +
                "    (\n" +
                "        `id`            bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `job_token`     varchar(128)          DEFAULT NULL,\n" +
                "        `label`         varchar(128)          DEFAULT NULL,\n" +
                "        `task_state`    varchar(128)          DEFAULT NULL,\n" +
                "        `launcher`      varchar(128)          DEFAULT NULL,\n" +
                "        `launch_time`   datetime              DEFAULT NULL,\n" +
                "        `finish_time`   datetime              DEFAULT NULL,\n" +
                "        `schedule_ip`   varchar(128) NOT NULL DEFAULT '7.7.7.7',\n" +
                "        `uid`           varchar(255) NOT NULL,\n" +
                "        `resource_type` varchar(255)          DEFAULT NULL,\n" +
                "        `resource_id`   bigint(20)            DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_task_state_label` (`task_state`, `label`),\n" +
                "        KEY `idx_uid` (`uid`(127)),\n" +
                "        KEY `idx_resource_id_type` (`resource_id`, `resource_type`(127))\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `console_task`\n" +
                "    (\n" +
                "        `id`                 bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`         datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`       datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `job_id`             bigint(20) NOT NULL,\n" +
                "        `task_state`         varchar(128)        DEFAULT NULL,\n" +
                "        `handler_bean_name`  varchar(128)        DEFAULT NULL,\n" +
                "        `handler_class_name` varchar(256)        DEFAULT NULL,\n" +
                "        `context`            text                DEFAULT NULL,\n" +
                "        `context_class_name` varchar(256)        DEFAULT NULL,\n" +
                "        `host`               varchar(64)         DEFAULT NULL,\n" +
                "        `execute_order`      int(11)             DEFAULT NULL,\n" +
                "        `execute_time`       datetime            DEFAULT NULL,\n" +
                "        `finish_time`        datetime            DEFAULT NULL,\n" +
                "        `message`            longtext            DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_job_id` (`job_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `data_export_job`\n" +
                "    (\n" +
                "        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `job_name`       varchar(64)  NOT NULL,\n" +
                "        `data_source_id` bigint(20)   NOT NULL,\n" +
                "        `uid`            varchar(255) NOT NULL,\n" +
                "        `user_name`      varchar(255) NOT NULL,\n" +
                "        `biz_type`       varchar(32)  NOT NULL,\n" +
                "        `biz_id`         varchar(127)          DEFAULT NULL,\n" +
                "        `status`         varchar(32)  NOT NULL,\n" +
                "        `status_msg`     text                  DEFAULT NULL,\n" +
                "        `parallel`       tinyint(1)   NULL     DEFAULT FALSE,\n" +
                "        `features`       text         NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `idx_uniq_job_name` (`job_name`),\n" +
                "        KEY `idx_uid` (`uid`(127)),\n" +
                "        KEY `idx_biz_id` (`biz_id`(127))\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `data_export_task`\n" +
                "    (\n" +
                "        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `uid`                varchar(255) NOT NULL,\n" +
                "        `data_export_job_id` bigint(20)   NOT NULL,\n" +
                "        `task_name`          varchar(64)  NOT NULL,\n" +
                "        `exec_order`         int          NOT NULL,\n" +
                "        `worker_id`          bigint(20)   NULL,\n" +
                "        `worker_seq_number`  varchar(128) NULL,\n" +
                "        `data_source_type`   varchar(64)  NOT NULL,\n" +
                "        `data_source_id`     bigint(20)   NOT NULL,\n" +
                "        `export_sql`         text         NOT NULL,\n" +
                "        `status`             varchar(32)  NOT NULL,\n" +
                "        `status_msg`         text                  DEFAULT NULL,\n" +
                "        `gmt_last_start`     datetime              DEFAULT NULL,\n" +
                "        `gmt_last_complete`  datetime              DEFAULT NULL,\n" +
                "        `gmt_last_fail`      datetime              DEFAULT NULL,\n" +
                "        `session_id`         varchar(255) NOT NULL,\n" +
                "        `result_type`        varchar(32)  NOT NULL,\n" +
                "        `result_file_name`   varchar(756)          DEFAULT NULL,\n" +
                "        `affect_row`         bigint(20)            DEFAULT NULL,\n" +
                "        `data_export_biz_id` varchar(128) NULL,\n" +
                "        `features`           text         NULL,\n" +
                "\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `idx_uniq_task_name` (`task_name`),\n" +
                "        KEY `idx_datasource_id` (`data_source_id`),\n" +
                "        KEY `idx_worker_id` (`worker_id`),\n" +
                "        KEY `idx_worker_seq_number` (`worker_seq_number`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_cluster`\n" +
                "    (\n" +
                "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `cluster_name`      varchar(128) NOT NULL,\n" +
                "        `region`            varchar(64)           DEFAULT NULL,\n" +
                "        `cloud_or_idc_name` varchar(128)          DEFAULT NULL,\n" +
                "        `cluster_desc`      varchar(128) NOT NULL,\n" +
                "        `uid`               varchar(255)          DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `idx_unique_name` (`cluster_name`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_worker`\n" +
                "    (\n" +
                "        `id`                         bigint                                  NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `cluster_id`                 bigint                                  NOT NULL,\n" +
                "        `worker_ip`                  varchar(255) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n" +
                "        `cloud_or_idc_name`          varchar(64)                                      DEFAULT NULL,\n" +
                "        `region`                     varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,\n" +
                "        `worker_state`               varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,\n" +
                "        `physic_mem_mb`              bigint                                  NOT NULL DEFAULT '0',\n" +
                "        `physic_core_num`            int                                     NOT NULL DEFAULT '0',\n" +
                "        `physic_disk_gb`             bigint                                  NOT NULL DEFAULT '0',\n" +
                "        `cpu_use_ratio`              decimal(5, 2)                                    DEFAULT '0.00',\n" +
                "        `mem_use_ratio`              decimal(5, 2)                                    DEFAULT '0.00',\n" +
                "        `free_mem_mb`                bigint                                           DEFAULT '0',\n" +
                "        `free_disk_gb`               bigint                                           DEFAULT '0',\n" +
                "        `worker_load`                decimal(5, 2)                                    DEFAULT '0.00',\n" +
                "        `schedule_ip`                varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '7.7.7.7',\n" +
                "        `worker_name`                varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,\n" +
                "        `worker_seq_number`          varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `worker_desc`                varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `install_console_job_id`     bigint                                           DEFAULT NULL,\n" +
                "        `uninstall_console_job_id`   bigint                                           DEFAULT NULL,\n" +
                "        `upgrade_all_console_job_id` bigint                                           DEFAULT NULL,\n" +
                "        `deploy_status`              varchar(64) COLLATE utf8mb4_general_ci           DEFAULT NULL,\n" +
                "        `external_ip`                varchar(255) COLLATE utf8mb4_general_ci          DEFAULT NULL,\n" +
                "        `uid`                        varchar(255) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `console_job_id`             bigint                                           DEFAULT NULL,\n" +
                "        `life_cycle_state`           varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'CREATED',\n" +
                "        `install_or_upgrade_date`    datetime                                         DEFAULT NULL,\n" +
                "        `install_or_upgrade_version` varchar(64) COLLATE utf8mb4_general_ci           DEFAULT NULL,\n" +
                "        `session_pool_use`           int                                              DEFAULT NULL,\n" +
                "        `session_pool_max`           int                                              DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_cluster_id` (`cluster_id`),\n" +
                "        KEY `idx_worker_ip` (`worker_ip`(127)),\n" +
                "        KEY `idx_worker_name` (`worker_name`),\n" +
                "        KEY `idx_worker_seq_number` (`worker_seq_number`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_worker_status`\n" +
                "    (\n" +
                "        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `worker_conn_status` varchar(64)  NOT NULL,\n" +
                "        `uid`                varchar(255) NOT NULL,\n" +
                "        `worker_seq_number`  varchar(128) NOT NULL,\n" +
                "        `console_ip`         varchar(32)           DEFAULT NULL,\n" +
                "        `worker_ip`          varchar(32)           DEFAULT NULL,\n" +
                "        `cluster_id`         bigint(20)   NOT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `idx_unique_wsn` (`worker_seq_number`),\n" +
                "        KEY `idx_wsn` (`worker_seq_number`),\n" +
                "        KEY `idx_uid` (`uid`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_worker_heartbeat`\n" +
                "    (\n" +
                "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `worker_seq_number` varchar(128) NOT NULL,\n" +
                "        `worker_ip`         varchar(255),\n" +
                "        `heartbeat_type`    varchar(128),\n" +
                "        `worker_send_time`  datetime,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_worker_seq_number` (`worker_seq_number`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `alert_config_detail`\n" +
                "    (\n" +
                "        `id`           bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `event_type`   varchar(64)  NOT NULL,\n" +
                "        `uid`          varchar(255),\n" +
                "        `phone`        boolean      NOT NULL DEFAULT FALSE,\n" +
                "        `email`        boolean      NOT NULL DEFAULT FALSE,\n" +
                "        `dingding`     boolean      NOT NULL DEFAULT TRUE,\n" +
                "        `sms`          boolean      NOT NULL DEFAULT FALSE,\n" +
                "        `duplicated`   boolean      NOT NULL DEFAULT FALSE,\n" +
                "        `rule_name`    varchar(255) NOT NULL DEFAULT '默认规则',\n" +
                "        `expression`   varchar(512)          DEFAULT NULL,\n" +
                "        `send_admin`   boolean      NOT NULL DEFAULT FALSE,\n" +
                "        `send_system`  boolean      NOT NULL DEFAULT FALSE,\n" +
                "        `worker_id`    bigint(20)            DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_gmt_create` (`gmt_create`),\n" +
                "        KEY `idx_event_type` (`event_type`),\n" +
                "        KEY `idx_worker_id` (`worker_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_ds_session`\n" +
                "    (\n" +
                "        `id`              bigint auto_increment primary key,\n" +
                "        `uid`             varchar(255)                       not null,\n" +
                "        `session_id`      varchar(255)                       not null,\n" +
                "        `wsn`             varchar(255)                       not null,\n" +
                "        `tx`              tinyint                            not null,\n" +
                "        `config`          text                               null,\n" +
                "        `cluster_id`      varchar(255)                       not null,\n" +
                "        `datasource_id`   varchar(255)                       not null,\n" +
                "        `datasource_type` varchar(255),\n" +
                "        `gmt_create`      datetime default CURRENT_TIMESTAMP not null,\n" +
                "        `gmt_modified`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "        unique key (`session_id`),\n" +
                "        key `conn_session_uid` (`uid`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE `ticket_process`\n" +
                "    (\n" +
                "        `id`            bigint      NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `ticket_id`     bigint      NOT NULL COMMENT '审批流过程关联的工单id',\n" +
                "        `ticket_stage`  varchar(64) NOT NULL COMMENT '审批流阶段名称，总共4个阶段创建、审批、确认、执行、完成',\n" +
                "        `next_id`       bigint               DEFAULT NULL COMMENT '下一个步骤的id，为空表示为最后一个步骤',\n" +
                "        `finish_time`   datetime             DEFAULT NULL COMMENT '该过程执行完成的时间',\n" +
                "        `stage_context` longtext COMMENT '保存每个阶段的上下文信息(如果需要的话)',\n" +
                "        `finished`      tinyint(1)           DEFAULT '0',\n" +
                "        `deleted`       tinyint(1)  NOT NULL DEFAULT '0',\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_data_source_id` (`ticket_id`),\n" +
                "        KEY `ticket_status` (`ticket_id`, `ticket_stage`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table IF NOT EXISTS sql_process\n" +
                "    (\n" +
                "        id           bigint auto_increment primary key,\n" +
                "        gmt_create   datetime   default CURRENT_TIMESTAMP not null,\n" +
                "        gmt_modified datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "        ticket_id    bigint                               not null comment 'sql执行关联的工单id',\n" +
                "        parsed_sql   longtext                             not null comment '执行的sql名称',\n" +
                "        sql_type     varchar(64)                          null comment '平台自动分类的sql类型，可以为dml,ddl,dcl等',\n" +
                "        seq_index    mediumtext                           not null comment '工单执行的序号',\n" +
                "        exec_result  longtext                             null comment '保存单条sql执行的结果',\n" +
                "        finish_time  datetime                             null comment '该sql执行完成的时间',\n" +
                "        finished     tinyint(1) default 0                 null,\n" +
                "        next_id      varchar(1024)                        null comment '下一条sql执行的id，最后一条sql该值为空',\n" +
                "        deleted      tinyint(1) default 0                 not null,\n" +
                "        risk_json    text                                 null,\n" +
                "        key idx_ticket_id (ticket_id)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table if not exists dm_ticket_inst\n" +
                "    (\n" +
                "        id                      bigint auto_increment primary key,\n" +
                "        biz_id                  varchar(32)                            not null comment '帮工单生成一个给前端用户查看的id',\n" +
                "        gmt_create              datetime     default CURRENT_TIMESTAMP not null,\n" +
                "        gmt_modified            datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n" +
                "        owner_uid               varchar(255)                           not null comment '工单发起人uid',\n" +
                "        data_source_id          bigint                                 not null comment '工单关联的数据源id，一个工单只能匹配一个数据源实例',\n" +
                "        target_info             varchar(1024)                          not null comment '工单需要访问的目标信息，例如MySQL是库，ES则是索引,用于前端展示',\n" +
                "        appro_type              varchar(512)                           not null comment '审批流模板来源，一般是钉钉、微信等',\n" +
                "        appro_biz               varchar(64)                            not null comment '审批类型：SQL执行、权限申请',\n" +
                "        appro_identity          varchar(512)                           null comment '工单关联的审批流实例唯一识别号，用于查询审批流实例信息',\n" +
                "        appro_template_name     varchar(256)                           not null comment '保存工单模板的名字',\n" +
                "        appro_template_identity varchar(512)                           not null comment '工单使用的流程模版ID',\n" +
                "        raw_sql                 longtext                               null comment '用户提交的未被解析的原始sql内容',\n" +
                "        request_auth_data       longtext                               null comment '用户提交的权限申请数据',\n" +
                "        session_id              varchar(255)                           null comment '只有在进入自动执行阶段后才会创建session',\n" +
                "        total_count             mediumtext                             null comment '所在执行过程总共的sql条数',\n" +
                "        description             varchar(1024)                          null comment '工单描述',\n" +
                "        expected_affected_rows  bigint                                 null comment '期望影响行数',\n" +
                "        risk_sql_count          int                                    null comment '风险 SQL 数量',\n" +
                "        ticket_title            varchar(512)                           not null comment '工单标题',\n" +
                "        ticket_status           varchar(512)                           not null comment '工单状态，参考TicketStatus',\n" +
                "        expected_exec_time      datetime     default CURRENT_TIMESTAMP not null comment '如果为立即执行，默认为生成工单时的时间。审批通过后发现期望执行时间小于当前执行则立即执行',\n" +
                "        finish_time             datetime                               null comment '工单完成时间',\n" +
                "        immediately             tinyint(1)                             null comment '是否立即执行',\n" +
                "        deleted                 tinyint(1)   default 0                 not null,\n" +
                "        ddl_sql_exec_type       varchar(128) default 'DIRECT'          not null,\n" +
                "        none_ddl_sql_exec_type  varchar(128) default 'DIRECT'          not null,\n" +
                "        error_count             int          default 0                 not null,\n" +
                "        status_message          text                                   null,\n" +
                "        key `idx_data_source_id` (`data_source_id`),\n" +
                "        unique key idx_unique_biz_id (`biz_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `checker_parameter`\n" +
                "    (\n" +
                "        `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "        `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "        `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                "        `datasource_id` varchar(255) NOT NULL COMMENT '关联的数据源',\n" +
                "        `checker_name`  varchar(64)  NOT NULL COMMENT 'checker 名称',\n" +
                "        `enable`        int          NOT NULL COMMENT '是否启用',\n" +
                "        `config`        text         NOT NULL COMMENT '配置参数',\n" +
                "        `category`      varchar(128) NULL COMMENT '分类',\n" +
                "        `group`         varchar(128) NULL COMMENT '分组',\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_checker_parameter_ds_id` (`datasource_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `checker_template`\n" +
                "    (\n" +
                "        `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "        `gmt_create`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "        `gmt_modified`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                "        `datasource_type` varchar(64)  NOT NULL COMMENT '归属数据源类别',\n" +
                "        `checker_name`    varchar(64)  NOT NULL COMMENT 'checker 名称',\n" +
                "        `enable`          int          NOT NULL COMMENT '默认是否启用',\n" +
                "        `config`          text         NOT NULL COMMENT '默认配置参数',\n" +
                "        `def_config`      text         NOT NULL COMMENT '默认配置参数定义',\n" +
                "        `category`        varchar(128) NULL COMMENT '分类',\n" +
                "        `group`           varchar(128) NULL COMMENT '分组',\n" +
                "        PRIMARY KEY (`id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `data_desensitize_rule`\n" +
                "    (\n" +
                "        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `datasource_id`  bigint(20)   NOT NULL,\n" +
                "        `ds_instance_id` varchar(64)  NOT NULL,\n" +
                "        `resource_path`  varchar(512) NOT NULL,\n" +
                "        `rule_type`      varchar(64)  NOT NULL,\n" +
                "        `rule_expr`      varchar(256),\n" +
                "        `disable`        tinyint(1)   NOT NULL DEFAULT 0,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_resource_path` (`resource_path`(127))\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `data_handle_config`\n" +
                "    (\n" +
                "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `datasource_id`     bigint(20)   NOT NULL,\n" +
                "        `ds_instance_id`    varchar(64)  NOT NULL,\n" +
                "        `resource_path`     varchar(512) NOT NULL,\n" +
                "        `package_id`        bigint(20)   NOT NULL,\n" +
                "        `pkg_instance_name` varchar(64)  NOT NULL,\n" +
                "        `pkg_desc`          varchar(512) NOT NULL,\n" +
                "        `disable`           tinyint(1)   NOT NULL DEFAULT 0,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_resource_path` (`resource_path`(127)),\n" +
                "        KEY `idx_package_id` (`package_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `data_handle_package`\n" +
                "    (\n" +
                "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `file_name`         varchar(64)  NOT NULL,\n" +
                "        `md5`               varchar(64)  NOT NULL,\n" +
                "        `pkg_instance_name` varchar(255) NOT NULL comment 'custom data handle pkg pkg id',\n" +
                "        `owner_uid`         varchar(255) NOT NULL comment 'custom data handle pkg owner uid',\n" +
                "        `binary_data`       longblob     NOT NULL,\n" +
                "        `description`       varchar(512) NOT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_uid` (`owner_uid`(127)),\n" +
                "        UNIQUE KEY `uk_pkg_inst_name` (`pkg_instance_name`(127))\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add(" CREATE TABLE IF NOT EXISTS `ds_appro_template`\n" +
                "    (\n" +
                "        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `datasource_id`     bigint(20)   NOT NULL,\n" +
                "        `approval_type`     varchar(64)  NOT NULL,\n" +
                "        `approval_biz`      varchar(64)  NULL,\n" +
                "        `template_name`     varchar(256) NOT NULL,\n" +
                "        `template_identity` varchar(512) NOT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `uk_ds_id_identity` (`datasource_id`, `template_identity`(127), approval_biz),\n" +
                "        KEY `idx_datasource_id` (`datasource_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table if not exists cache_appro_template\n" +
                "    (\n" +
                "        id                   bigint auto_increment primary key,\n" +
                "        gmt_create           datetime default CURRENT_TIMESTAMP not null,\n" +
                "        gmt_modified         datetime default CURRENT_TIMESTAMP not null,\n" +
                "        primary_uid          varchar(128)                       not null,\n" +
                "        approval_type        varchar(64),\n" +
                "        template_name        varchar(256),\n" +
                "        dd_template_identity varchar(512),\n" +
                "        dd_appro_url         text,\n" +
                "        index cache_appro_template_puid (primary_uid)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `dm_res_auth`\n" +
                "    (\n" +
                "        `id`             bigint                                  NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`   datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `owner_uid`      varchar(255) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `res_id`         bigint                                  NOT NULL,\n" +
                "        `res_inst_id`    varchar(512)                            NULL,\n" +
                "        `res_desc`       varchar(512)                            NOT NULL,\n" +
                "        `res_path`       varchar(512) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `level_1`        varchar(512) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `level_2`        varchar(512) COLLATE utf8mb4_general_ci NULL,\n" +
                "        `level_3`        varchar(512) COLLATE utf8mb4_general_ci NULL,\n" +
                "        `level_4`        varchar(512) COLLATE utf8mb4_general_ci NULL,\n" +
                "        `start_time`     datetime                                         DEFAULT NULL,\n" +
                "        `end_time`       datetime                                         DEFAULT NULL,\n" +
                "        `kind_type`      varchar(128) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "        `res_auth_label` text COLLATE utf8mb4_general_ci                  DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        UNIQUE KEY `uk_path` (`res_id`, `kind_type`, `res_path`),\n" +
                "        KEY `idx_level_0` (`res_id`, `kind_type`),\n" +
                "        KEY `idx_level_1` (`res_id`, `kind_type`, `level_1`(127)),\n" +
                "        KEY `idx_level_2` (`res_id`, `kind_type`, `level_1`(127), `level_2`(127)),\n" +
                "        KEY `idx_level_3` (`res_id`, `kind_type`, `level_1`(127), `level_2`(127), `level_3`(127)),\n" +
                "        KEY `idx_owner_uid` (`owner_uid`(127)),\n" +
                "        KEY `idx_res_id` (`res_id`),\n" +
                "        KEY `idx_path` (`res_path`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (1, now(), now(), 'MySQL', 'CreateTableRequireComment', 0, '{}', '{}', '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (2, now(), now(), 'MySQL', 'CreateTableColumnRequireComment', 0, '{}', '{}', '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (3, now(), now(), 'MySQL', 'CreateTableNameCase', 0, '{''case'':''LowerCase''}',\n" +
                "            '[{''label'':''TableNameCase_Case'',''name'':''case'',''type'':''select'',''options'':[{''name'':''LowerCase'',''label'':''LabelLowerCase''},{''name'':''UpperCase'',''label'':''LabelUpperCase''}]}]',\n" +
                "            '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (4, now(), now(), 'MySQL', 'CreateTableColumnNameCase', 0, '{''case'':''LowerCase''}',\n" +
                "            '[{''label'':''ColumnNameCase_Case'',''name'':''case'',''type'':''select'',''options'':[{''name'':''LowerCase'',''label'':''LabelLowerCase''},{''name'':''UpperCase'',''label'':''LabelUpperCase''}]}]',\n" +
                "            '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (5, now(), now(), 'MySQL', 'CreateTableRequireColumn', 0, '{''requireColumns'':''gmt_create,gmt_modified''}',\n" +
                "            '[{''label'':''RequireColumn'',''name'':''requireColumns'',''type'':''input'',''options'':[]}]', '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (6, now(), now(), 'MySQL', 'CreateTableRequirePrimaryKey', 1, '{}', '{}', '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (7, now(), now(), 'MySQL', 'CreateTableColumnTypeBlackList', 1, '{''blackList'':''bob''}',\n" +
                "            '[{''label'':''RequireNotColumn'',''name'':''blackList'',''type'':''input'',''options'':[]}]', '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (8, now(), now(), 'MySQL', 'CreateTableCompositePrimaryKeyLimit', 0, '{''limit'':1}',\n" +
                "            '[{''label'':''CompositePrimaryKeyLimit'',''name'':''limit'',''type'':''input'',''options'':[]}]', '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (9, now(), now(), 'MySQL', 'CreateTableUniqueName', 0, '{''strategy'':''Prefix'',''test'':''uk_''}',\n" +
                "            '[{''label'':''UniqueKeyNameStrategy'',''name'':''strategy'',''type'':''select'',''options'':[{''name'':''Prefix'',''label'':''LabelPrefix''},{''name'':''Endfix'',''label'':''LabelEndfi''},{''name'':''Fixed'',''label'':''LabelFixed''}]},{''label'':''LabelValue'',''name'':''test'',''type'':''input'',''options'':[]}]',\n" +
                "            '', '')");

        sqls.add("REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,\n" +
                "                                   category, `group`)\n" +
                "    VALUES (10, now(), now(), 'MySQL', 'CreateTableIndexName', 0, '{''strategy'':''Prefix'',''test'':''idx_''}',\n" +
                "            '[{''label'':''IndexKeyNameStrategy'',''name'':''strategy'',''type'':''select'',''options'':[{''name'':''Prefix'',''label'':''LabelPrefix''},{''name'':''Endfix'',''label'':''LabelEndfi''},{''name'':''Fixed'',''label'':''LabelFixed''}]},{''label'':''LabelValue'',''name'':''test'',''type'':''input'',''options'':[]}]',\n" +
                "            '', '')");
    }
}