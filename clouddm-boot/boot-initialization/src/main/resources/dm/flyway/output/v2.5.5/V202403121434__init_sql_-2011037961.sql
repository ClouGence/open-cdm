# noinspection SqlNoDataSourceInspectionForFile

DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init()
begin
    declare continue handler for 1060 begin
    end;
    declare continue handler for 1061 begin
    end;
    declare continue handler for 1062 begin
    end;
    declare continue handler for 1050 begin
    end;
    declare continue handler for 1072 begin
    end;
    declare continue handler for 1091 begin
    end;

    -- ----------------------------
    -- DataSource tables --
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS `dm_ds_config`
    (
        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `uid`                varchar(255) NOT NULL,
        `data_source_id`     bigint       NOT NULL,
        `data_source_type`   varchar(255) NOT NULL,
        `status`             varchar(128) NULL,
        `status_message`     text         NULL,
        `config_instance_id` varchar(128) NOT NULL,
        `bind_cluster_id`    bigint       NOT NULL,
        `bind_env_id`        bigint       NOT NULL,
        PRIMARY KEY (`id`),
        unique key ds_id (`data_source_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_ds_statistics`
    (
        `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `data_source_id`   bigint       not null,
        `data_source_type` varchar(128) not null,
        `last_time`        datetime     NOT NULL,
        `exec_counts`      bigint                default 0,
        PRIMARY KEY (`id`),
        unique key ds_id (`data_source_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_ds_tag`
    (
        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `uid`            varchar(128) NOT NULL,
        `data_source_id` bigint       NOT NULL,
        `instance_desc`  varchar(256) NOT NULL,
        PRIMARY KEY (`id`),
        unique key ds_tag_dsid_uid (`uid`, `data_source_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_ds_kv_base_config`
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
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    -- ----------------------------
    -- console job series tables --
    -- ----------------------------
    CREATE TABLE IF NOT EXISTS `console_job`
    (
        `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `job_token`     varchar(128)          DEFAULT NULL,
        `label`         varchar(128)          DEFAULT NULL,
        `task_state`    varchar(128)          DEFAULT NULL,
        `launcher`      varchar(128)          DEFAULT NULL,
        `launch_time`   datetime              DEFAULT NULL,
        `finish_time`   datetime              DEFAULT NULL,
        `schedule_ip`   varchar(128) NOT NULL DEFAULT '7.7.7.7',
        `uid`           varchar(255) NOT NULL,
        `resource_type` varchar(255)          DEFAULT NULL,
        `resource_id`   bigint(20)            DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_task_state_label` (`task_state`, `label`),
        KEY `idx_uid` (`uid`(127)),
        KEY `idx_resource_id_type` (`resource_id`, `resource_type`(127))
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `console_task`
    (
        `id`                 bigint(20) NOT NULL AUTO_INCREMENT,
        `gmt_create`         datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`       datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `job_id`             bigint(20) NOT NULL,
        `task_state`         varchar(128)        DEFAULT NULL,
        `handler_bean_name`  varchar(128)        DEFAULT NULL,
        `handler_class_name` varchar(256)        DEFAULT NULL,
        `context`            text                DEFAULT NULL,
        `context_class_name` varchar(256)        DEFAULT NULL,
        `host`               varchar(64)         DEFAULT NULL,
        `execute_order`      int(11)             DEFAULT NULL,
        `execute_time`       datetime            DEFAULT NULL,
        `finish_time`        datetime            DEFAULT NULL,
        `message`            longtext            DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_job_id` (`job_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `data_export_job`
    (
        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `job_name`       varchar(64)  NOT NULL,
        `data_source_id` bigint(20)   NOT NULL,
        `uid`            varchar(255) NOT NULL,
        `user_name`      varchar(255) NOT NULL,
        `biz_type`       varchar(32)  NOT NULL,
        `biz_id`         varchar(127)          DEFAULT NULL,
        `status`         varchar(32)  NOT NULL,
        `status_msg`     text                  DEFAULT NULL,
        `parallel`       tinyint(1)   NULL     DEFAULT FALSE,
        `features`       text         NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `idx_uniq_job_name` (`job_name`),
        KEY `idx_uid` (`uid`(127)),
        KEY `idx_biz_id` (`biz_id`(127))
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `data_export_task`
    (
        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `uid`                varchar(255) NOT NULL,
        `data_export_job_id` bigint(20)   NOT NULL,
        `task_name`          varchar(64)  NOT NULL,
        `exec_order`         int          NOT NULL,
        `worker_id`          bigint(20)   NULL,
        `worker_seq_number`  varchar(128) NULL,
        `data_source_type`   varchar(64)  NOT NULL,
        `data_source_id`     bigint(20)   NOT NULL,
        `export_sql`         text         NOT NULL,
        `status`             varchar(32)  NOT NULL,
        `status_msg`         text                  DEFAULT NULL,
        `gmt_last_start`     datetime              DEFAULT NULL,
        `gmt_last_complete`  datetime              DEFAULT NULL,
        `gmt_last_fail`      datetime              DEFAULT NULL,
        `session_id`         varchar(255) NOT NULL,
        `result_type`        varchar(32)  NOT NULL,
        `result_file_name`   varchar(756)          DEFAULT NULL,
        `affect_row`         bigint(20)            DEFAULT NULL,
        `data_export_biz_id` varchar(128) NULL,
        `features`           text         NULL,

        PRIMARY KEY (`id`),
        UNIQUE KEY `idx_uniq_task_name` (`task_name`),
        KEY `idx_datasource_id` (`data_source_id`),
        KEY `idx_worker_id` (`worker_id`),
        KEY `idx_worker_seq_number` (`worker_seq_number`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    -- ----------------------
    -- worker series tables--
    -- ----------------------
    CREATE TABLE IF NOT EXISTS `dm_cluster`
    (
        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `cluster_name`      varchar(128) NOT NULL,
        `region`            varchar(64)           DEFAULT NULL,
        `cloud_or_idc_name` varchar(128)          DEFAULT NULL,
        `cluster_desc`      varchar(128) NOT NULL,
        `uid`               varchar(255)          DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `idx_unique_name` (`cluster_name`)
    ) ENGINE = InnoDB
      AUTO_INCREMENT = 2
      DEFAULT CHARSET = utf8mb4;

    CREATE TABLE IF NOT EXISTS `dm_worker`
    (
        `id`                         bigint                                  NOT NULL AUTO_INCREMENT,
        `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `cluster_id`                 bigint                                  NOT NULL,
        `worker_ip`                  varchar(255) COLLATE utf8mb4_general_ci          DEFAULT NULL,
        `cloud_or_idc_name`          varchar(64)                                      DEFAULT NULL,
        `region`                     varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,
        `worker_state`               varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,
        `physic_mem_mb`              bigint                                  NOT NULL DEFAULT '0',
        `physic_core_num`            int                                     NOT NULL DEFAULT '0',
        `physic_disk_gb`             bigint                                  NOT NULL DEFAULT '0',
        `cpu_use_ratio`              decimal(5, 2)                                    DEFAULT '0.00',
        `mem_use_ratio`              decimal(5, 2)                                    DEFAULT '0.00',
        `free_mem_mb`                bigint                                           DEFAULT '0',
        `free_disk_gb`               bigint                                           DEFAULT '0',
        `worker_load`                decimal(5, 2)                                    DEFAULT '0.00',
        `schedule_ip`                varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '7.7.7.7',
        `worker_name`                varchar(64) COLLATE utf8mb4_general_ci  NOT NULL,
        `worker_seq_number`          varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
        `worker_desc`                varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
        `install_console_job_id`     bigint                                           DEFAULT NULL,
        `uninstall_console_job_id`   bigint                                           DEFAULT NULL,
        `upgrade_all_console_job_id` bigint                                           DEFAULT NULL,
        `deploy_status`              varchar(64) COLLATE utf8mb4_general_ci           DEFAULT NULL,
        `external_ip`                varchar(255) COLLATE utf8mb4_general_ci          DEFAULT NULL,
        `uid`                        varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
        `console_job_id`             bigint                                           DEFAULT NULL,
        `life_cycle_state`           varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'CREATED',
        `install_or_upgrade_date`    datetime                                         DEFAULT NULL,
        `install_or_upgrade_version` varchar(64) COLLATE utf8mb4_general_ci           DEFAULT NULL,
        `session_pool_use`           int                                              DEFAULT NULL,
        `session_pool_max`           int                                              DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_cluster_id` (`cluster_id`),
        KEY `idx_worker_ip` (`worker_ip`(127)),
        KEY `idx_worker_name` (`worker_name`),
        KEY `idx_worker_seq_number` (`worker_seq_number`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_worker_status`
    (
        `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `worker_conn_status` varchar(64)  NOT NULL,
        `uid`                varchar(255) NOT NULL,
        `worker_seq_number`  varchar(128) NOT NULL,
        `console_ip`         varchar(32)           DEFAULT NULL,
        `worker_ip`          varchar(32)           DEFAULT NULL,
        `cluster_id`         bigint(20)   NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `idx_unique_wsn` (`worker_seq_number`),
        KEY `idx_wsn` (`worker_seq_number`),
        KEY `idx_uid` (`uid`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_worker_heartbeat`
    (
        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `worker_seq_number` varchar(128) NOT NULL,
        `worker_ip`         varchar(255),
        `heartbeat_type`    varchar(128),
        `worker_send_time`  datetime,
        PRIMARY KEY (`id`),
        KEY `idx_worker_seq_number` (`worker_seq_number`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    -- ----------------
    -- system tables --
    -- ----------------
    CREATE TABLE IF NOT EXISTS `alert_config_detail`
    (
        `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `event_type`   varchar(64)  NOT NULL,
        `uid`          varchar(255),
        `phone`        boolean      NOT NULL DEFAULT FALSE,
        `email`        boolean      NOT NULL DEFAULT FALSE,
        `dingding`     boolean      NOT NULL DEFAULT TRUE,
        `sms`          boolean      NOT NULL DEFAULT FALSE,
        `duplicated`   boolean      NOT NULL DEFAULT FALSE,
        `rule_name`    varchar(255) NOT NULL DEFAULT '默认规则',
        `expression`   varchar(512)          DEFAULT NULL,
        `send_admin`   boolean      NOT NULL DEFAULT FALSE,
        `send_system`  boolean      NOT NULL DEFAULT FALSE,
        `worker_id`    bigint(20)            DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_gmt_create` (`gmt_create`),
        KEY `idx_event_type` (`event_type`),
        KEY `idx_worker_id` (`worker_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_ds_session`
    (
        `id`              bigint auto_increment primary key,
        `uid`             varchar(255)                       not null,
        `session_id`      varchar(255)                       not null,
        `wsn`             varchar(255)                       not null,
        `tx`              tinyint                            not null,
        `config`          text                               null,
        `cluster_id`      varchar(255)                       not null,
        `datasource_id`   varchar(255)                       not null,
        `datasource_type` varchar(255),
        `gmt_create`      datetime default CURRENT_TIMESTAMP not null,
        `gmt_modified`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
        unique key (`session_id`),
        key `conn_session_uid` (`uid`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    -- ----------------
    -- dm ticket --
    -- ----------------
    CREATE TABLE `ticket_process`
    (
        `id`            bigint      NOT NULL AUTO_INCREMENT,
        `gmt_create`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `ticket_id`     bigint      NOT NULL COMMENT '审批流过程关联的工单id',
        `ticket_stage`  varchar(64) NOT NULL COMMENT '审批流阶段名称，总共4个阶段创建、审批、确认、执行、完成',
        `next_id`       bigint               DEFAULT NULL COMMENT '下一个步骤的id，为空表示为最后一个步骤',
        `finish_time`   datetime             DEFAULT NULL COMMENT '该过程执行完成的时间',
        `stage_context` longtext COMMENT '保存每个阶段的上下文信息(如果需要的话)',
        `finished`      tinyint(1)           DEFAULT '0',
        `deleted`       tinyint(1)  NOT NULL DEFAULT '0',
        PRIMARY KEY (`id`),
        KEY `idx_data_source_id` (`ticket_id`),
        KEY `ticket_status` (`ticket_id`, `ticket_stage`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci COMMENT ='保存工单的执行路径，每个工单对应4条执行路径记录，标识4个阶段';

    create table IF NOT EXISTS sql_process
    (
        id           bigint auto_increment primary key,
        gmt_create   datetime   default CURRENT_TIMESTAMP not null,
        gmt_modified datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
        ticket_id    bigint                               not null comment 'sql执行关联的工单id',
        parsed_sql   longtext                             not null comment '执行的sql名称',
        sql_type     varchar(64)                          null comment '平台自动分类的sql类型，可以为dml,ddl,dcl等',
        seq_index    mediumtext                           not null comment '工单执行的序号',
        exec_result  longtext                             null comment '保存单条sql执行的结果',
        finish_time  datetime                             null comment '该sql执行完成的时间',
        finished     tinyint(1) default 0                 null,
        next_id      varchar(1024)                        null comment '下一条sql执行的id，最后一条sql该值为空',
        deleted      tinyint(1) default 0                 not null,
        risk_json    text                                 null,
        key idx_ticket_id (ticket_id)
    ) collate = utf8mb4_general_ci comment '工单对应的SQL执行过程。每一行代表一条SQL的执行。工单创建后会直接初始化对应数量的记录';

    create table if not exists dm_ticket_inst
    (
        id                      bigint auto_increment primary key,
        biz_id                  varchar(32)                            not null comment '帮工单生成一个给前端用户查看的id',
        gmt_create              datetime     default CURRENT_TIMESTAMP not null,
        gmt_modified            datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
        owner_uid               varchar(255)                           not null comment '工单发起人uid',
        data_source_id          bigint                                 not null comment '工单关联的数据源id，一个工单只能匹配一个数据源实例',
        target_info             varchar(1024)                          not null comment '工单需要访问的目标信息，例如MySQL是库，ES则是索引,用于前端展示',
        appro_type              varchar(512)                           not null comment '审批流模板来源，一般是钉钉、微信等',
        appro_biz               varchar(64)                            not null comment '审批类型：SQL执行、权限申请',
        appro_identity          varchar(512)                           null comment '工单关联的审批流实例唯一识别号，用于查询审批流实例信息',
        appro_template_name     varchar(256)                           not null comment '保存工单模板的名字',
        appro_template_identity varchar(512)                           not null comment '工单使用的流程模版ID',
        raw_sql                 longtext                               null comment '用户提交的未被解析的原始sql内容',
        request_auth_data       longtext                               null comment '用户提交的权限申请数据',
        session_id              varchar(255)                           null comment '只有在进入自动执行阶段后才会创建session',
        total_count             mediumtext                             null comment '所在执行过程总共的sql条数',
        description             varchar(1024)                          null comment '工单描述',
        expected_affected_rows  bigint                                 null comment '期望影响行数',
        risk_sql_count          int                                    null comment '风险 SQL 数量',
        ticket_title            varchar(512)                           not null comment '工单标题',
        ticket_status           varchar(512)                           not null comment '工单状态，参考TicketStatus',
        expected_exec_time      datetime     default CURRENT_TIMESTAMP not null comment '如果为立即执行，默认为生成工单时的时间。审批通过后发现期望执行时间小于当前执行则立即执行',
        finish_time             datetime                               null comment '工单完成时间',
        immediately             tinyint(1)                             null comment '是否立即执行',
        deleted                 tinyint(1)   default 0                 not null,
        ddl_sql_exec_type       varchar(128) default 'DIRECT'          not null,
        none_ddl_sql_exec_type  varchar(128) default 'DIRECT'          not null,
        error_count             int          default 0                 not null,
        status_message          text                                   null,
        key `idx_data_source_id` (`data_source_id`),
        unique key idx_unique_biz_id (`biz_id`)
    ) collate = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `checker_parameter`
    (
        `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
        `gmt_create`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `gmt_modified`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
        `datasource_id` varchar(255) NOT NULL COMMENT '关联的数据源',
        `checker_name`  varchar(64)  NOT NULL COMMENT 'checker 名称',
        `enable`        int          NOT NULL COMMENT '是否启用',
        `config`        text         NOT NULL COMMENT '配置参数',
        `category`      varchar(128) NULL COMMENT '分类',
        `group`         varchar(128) NULL COMMENT '分组',
        PRIMARY KEY (`id`),
        KEY `idx_checker_parameter_ds_id` (`datasource_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `checker_template`
    (
        `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
        `gmt_create`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `gmt_modified`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
        `datasource_type` varchar(64)  NOT NULL COMMENT '归属数据源类别',
        `checker_name`    varchar(64)  NOT NULL COMMENT 'checker 名称',
        `enable`          int          NOT NULL COMMENT '默认是否启用',
        `config`          text         NOT NULL COMMENT '默认配置参数',
        `def_config`      text         NOT NULL COMMENT '默认配置参数定义',
        `category`        varchar(128) NULL COMMENT '分类',
        `group`           varchar(128) NULL COMMENT '分组',
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    -- ---------------------
    -- data desensitization and handle
    -- ---------------------
    CREATE TABLE IF NOT EXISTS `data_desensitize_rule`
    (
        `id`             bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `datasource_id`  bigint(20)   NOT NULL,
        `ds_instance_id` varchar(64)  NOT NULL,
        `resource_path`  varchar(512) NOT NULL,
        `rule_type`      varchar(64)  NOT NULL,
        `rule_expr`      varchar(256),
        `disable`        tinyint(1)   NOT NULL DEFAULT 0,
        PRIMARY KEY (`id`),
        KEY `idx_resource_path` (`resource_path`(127))
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `data_handle_config`
    (
        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `datasource_id`     bigint(20)   NOT NULL,
        `ds_instance_id`    varchar(64)  NOT NULL,
        `resource_path`     varchar(512) NOT NULL,
        `package_id`        bigint(20)   NOT NULL,
        `pkg_instance_name` varchar(64)  NOT NULL,
        `pkg_desc`          varchar(512) NOT NULL,
        `disable`           tinyint(1)   NOT NULL DEFAULT 0,
        PRIMARY KEY (`id`),
        KEY `idx_resource_path` (`resource_path`(127)),
        KEY `idx_package_id` (`package_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `data_handle_package`
    (
        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `file_name`         varchar(64)  NOT NULL,
        `md5`               varchar(64)  NOT NULL,
        `pkg_instance_name` varchar(255) NOT NULL comment 'custom data handle pkg pkg id',
        `owner_uid`         varchar(255) NOT NULL comment 'custom data handle pkg owner uid',
        `binary_data`       longblob     NOT NULL,
        `description`       varchar(512) NOT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_uid` (`owner_uid`(127)),
        UNIQUE KEY `uk_pkg_inst_name` (`pkg_instance_name`(127))
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `ds_appro_template`
    (
        `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `datasource_id`     bigint(20)   NOT NULL,
        `approval_type`     varchar(64)  NOT NULL,
        `approval_biz`      varchar(64)  NULL,
        `template_name`     varchar(256) NOT NULL,
        `template_identity` varchar(512) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `uk_ds_id_identity` (`datasource_id`, `template_identity`(127), approval_biz),
        KEY `idx_datasource_id` (`datasource_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table if not exists cache_appro_template
    (
        id                   bigint auto_increment primary key,
        gmt_create           datetime default CURRENT_TIMESTAMP not null,
        gmt_modified         datetime default CURRENT_TIMESTAMP not null,
        primary_uid          varchar(128)                       not null,
        approval_type        varchar(64),
        template_name        varchar(256),
        dd_template_identity varchar(512),
        dd_appro_url         text,
        index cache_appro_template_puid (primary_uid)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    CREATE TABLE IF NOT EXISTS `dm_res_auth`
    (
        `id`             bigint                                  NOT NULL AUTO_INCREMENT,
        `gmt_create`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`   datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `owner_uid`      varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
        `res_id`         bigint                                  NOT NULL,
        `res_inst_id`    varchar(512)                            NULL,
        `res_desc`       varchar(512)                            NOT NULL,
        `res_path`       varchar(512) COLLATE utf8mb4_general_ci NOT NULL,
        `level_1`        varchar(512) COLLATE utf8mb4_general_ci NOT NULL,
        `level_2`        varchar(512) COLLATE utf8mb4_general_ci NULL,
        `level_3`        varchar(512) COLLATE utf8mb4_general_ci NULL,
        `level_4`        varchar(512) COLLATE utf8mb4_general_ci NULL,
        `start_time`     datetime                                         DEFAULT NULL,
        `end_time`       datetime                                         DEFAULT NULL,
        `kind_type`      varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
        `res_auth_label` text COLLATE utf8mb4_general_ci                  DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `uk_path` (`res_id`, `kind_type`, `res_path`),
        KEY `idx_level_0` (`res_id`, `kind_type`),
        KEY `idx_level_1` (`res_id`, `kind_type`, `level_1`(127)),
        KEY `idx_level_2` (`res_id`, `kind_type`, `level_1`(127), `level_2`(127)),
        KEY `idx_level_3` (`res_id`, `kind_type`, `level_1`(127), `level_2`(127), `level_3`(127)),
        KEY `idx_owner_uid` (`owner_uid`(127)),
        KEY `idx_res_id` (`res_id`),
        KEY `idx_path` (`res_path`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (1, now(), now(), 'MySQL', 'CreateTableRequireComment', 0, '{}', '{}', '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (2, now(), now(), 'MySQL', 'CreateTableColumnRequireComment', 0, '{}', '{}', '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (3, now(), now(), 'MySQL', 'CreateTableNameCase', 0, '{''case'':''LowerCase''}',
            '[{''label'':''TableNameCase_Case'',''name'':''case'',''type'':''select'',''options'':[{''name'':''LowerCase'',''label'':''LabelLowerCase''},{''name'':''UpperCase'',''label'':''LabelUpperCase''}]}]',
            '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (4, now(), now(), 'MySQL', 'CreateTableColumnNameCase', 0, '{''case'':''LowerCase''}',
            '[{''label'':''ColumnNameCase_Case'',''name'':''case'',''type'':''select'',''options'':[{''name'':''LowerCase'',''label'':''LabelLowerCase''},{''name'':''UpperCase'',''label'':''LabelUpperCase''}]}]',
            '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (5, now(), now(), 'MySQL', 'CreateTableRequireColumn', 0, '{''requireColumns'':''gmt_create,gmt_modified''}',
            '[{''label'':''RequireColumn'',''name'':''requireColumns'',''type'':''input'',''options'':[]}]', '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (6, now(), now(), 'MySQL', 'CreateTableRequirePrimaryKey', 1, '{}', '{}', '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (7, now(), now(), 'MySQL', 'CreateTableColumnTypeBlackList', 1, '{''blackList'':''bob''}',
            '[{''label'':''RequireNotColumn'',''name'':''blackList'',''type'':''input'',''options'':[]}]', '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (8, now(), now(), 'MySQL', 'CreateTableCompositePrimaryKeyLimit', 0, '{''limit'':1}',
            '[{''label'':''CompositePrimaryKeyLimit'',''name'':''limit'',''type'':''input'',''options'':[]}]', '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (9, now(), now(), 'MySQL', 'CreateTableUniqueName', 0, '{''strategy'':''Prefix'',''test'':''uk_''}',
            '[{''label'':''UniqueKeyNameStrategy'',''name'':''strategy'',''type'':''select'',''options'':[{''name'':''Prefix'',''label'':''LabelPrefix''},{''name'':''Endfix'',''label'':''LabelEndfi''},{''name'':''Fixed'',''label'':''LabelFixed''}]},{''label'':''LabelValue'',''name'':''test'',''type'':''input'',''options'':[]}]',
            '', '');
    REPLACE INTO checker_template (id, gmt_create, gmt_modified, datasource_type, checker_name, enable, config, def_config,
                                   category, `group`)
    VALUES (10, now(), now(), 'MySQL', 'CreateTableIndexName', 0, '{''strategy'':''Prefix'',''test'':''idx_''}',
            '[{''label'':''IndexKeyNameStrategy'',''name'':''strategy'',''type'':''select'',''options'':[{''name'':''Prefix'',''label'':''LabelPrefix''},{''name'':''Endfix'',''label'':''LabelEndfi''},{''name'':''Fixed'',''label'':''LabelFixed''}]},{''label'':''LabelValue'',''name'':''test'',''type'':''input'',''options'':[]}]',
            '', '');

-- sample user and worker (end) --------------------
end
$$

call proc_init();