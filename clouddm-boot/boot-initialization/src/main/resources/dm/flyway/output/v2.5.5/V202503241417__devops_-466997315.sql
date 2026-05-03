# noinspection SqlNoDataSourceInspectionForFile

DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1061 begin end;
    declare continue handler for 1062 begin end;
    declare continue handler for 1050 begin end;
    declare continue handler for 1072 begin end;
    declare continue handler for 1091 begin end;
    declare continue handler for 1051 begin end;

    alter table dm_ds_config
        add column enable_devops tinyint default 0;
    alter table dm_ticket_details_inst
        drop ddl_sql_exec_type;
    alter table dm_ticket_details_inst
        drop none_ddl_sql_exec_type;
    alter table dm_ticket_details_inst
        drop immediately;

    create table dm_project_scm
    (
        id               bigint      not null auto_increment,
        gmt_create       datetime    not null default CURRENT_TIMESTAMP,
        gmt_modified     datetime    not null default CURRENT_TIMESTAMP,
        owner_uid        varchar(36) not null,
        scm_type         varchar(12) not null,
        scm_display      varchar(64) not null,
        scm_service_url  text        not null,
        scm_access_token text,
        primary key (id)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_messenger
    (
        id           bigint      not null auto_increment,
        gmt_create   datetime    not null default CURRENT_TIMESTAMP,
        gmt_modified datetime    not null default CURRENT_TIMESTAMP,
        owner_uid    varchar(36) not null,
        im_type      varchar(12) not null,
        im_display   varchar(64) not null,
        webhook      text        null,
        secret       text        null,
        enable       tinyint     not null default 1,
        primary key (id)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_project
    (
        id             bigint       not null auto_increment,
        gmt_create     datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified   datetime     not null default CURRENT_TIMESTAMP,
        owner_uid      varchar(36)  not null,
        project_uid    varchar(36)  not null,
        project_code   varchar(64)  not null,
        project_name   varchar(128) not null,
        project_desc   text         null,
        project_status varchar(12)  not null default 'NORMAL',
        project_mark   varchar(12)  not null default '',
        flow_check     varchar(12)  not null default 'Failure',
        flow_approve   varchar(12)  not null default 'Enable',
        flow_execute   varchar(12)  not null default 'Manual',
        options        text         not null,
        primary key (id),
        unique key (project_code)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_project_devops
    (
        id                bigint       not null auto_increment,
        gmt_create        datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified      datetime     not null default CURRENT_TIMESTAMP,
        owner_uid         varchar(36)  not null,
        ref_project_id    bigint       not null,
        ref_scm_id        bigint       not null,
        ref_scm_type      varchar(12)  not null,
        scm_repo_space    varchar(128) not null,
        scm_repo_name     varchar(128) not null,
        scm_repo_url      text         not null,
        scm_repo_branch   varchar(64)  not null,
        scm_repo_event    varchar(128) not null,
        scm_repo_script   varchar(256) not null,
        scm_repo_hook_pwd varchar(256) null,
        enable_hook       tinyint      not null default 1,
        ds_id             bigint       not null,
        ds_type           varchar(64)  not null,
        ds_instance       varchar(64)  not null,
        ds_desc           text         not null,
        ds_path           varchar(128) not null,
        devops_options    text         not null,
        devops_hashcode   varchar(64)  not null,
        enable            tinyint      not null default 1,
        deleted           tinyint      not null default 0,
        callback_url      text         not null,
        callback_method   varchar(32)  not null,
        enable_callback   tinyint      not null default 1,
        enable_trigger    tinyint      not null default 0,
        trigger_token     varchar(64)  not null,
        primary key (id),
        key (devops_hashcode)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_project_msg
    (
        id                   bigint      not null auto_increment,
        gmt_create           datetime    not null default CURRENT_TIMESTAMP,
        gmt_modified         datetime    not null default CURRENT_TIMESTAMP,
        owner_uid            varchar(36) not null,
        ref_project_id       bigint      not null,
        ref_msg_id           bigint      not null,
        ref_msg_type         varchar(12) not null,
        enable               tinyint     not null default 1,
        language             varchar(12) not null default '',
        event_project_status tinyint     not null default 1,
        event_project_config tinyint     not null default 1,
        event_change_life    tinyint     not null default 1,
        event_change_notice  tinyint     not null default 1,
        primary key (id),
        unique key (owner_uid, ref_project_id)
    ) ENGINE = InnoDB
          DEFAULT CHARSET = utf8mb4
          COLLATE = utf8mb4_general_ci;

    create table dm_project_change
    (
        id             bigint       not null auto_increment,
        gmt_create     datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified   datetime     not null default CURRENT_TIMESTAMP,
        owner_uid      varchar(36)  not null,
        ref_project_id bigint       not null,
        ref_devops_id  bigint       not null,
        change_name    varchar(128) not null,
        change_time    datetime     not null,
        change_branch  varchar(256) not null,
        current_step   varchar(36)  not null,
        current_status varchar(36)  not null,
        schedule_time  datetime     null,
        version        int          not null default 0,
        remark         text         null,
        try_times      int          not null default 0,
        last_commit_id varchar(64)  not null,
        lock_status    tinyint      not null default 0,
        flow_walked    text         not null,
        primary key (id),
        key change_idx(ref_project_id, ref_devops_id, last_commit_id)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_project_change_item
    (
        id                   bigint       not null auto_increment,
        gmt_create           datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified         datetime     not null default CURRENT_TIMESTAMP,
        owner_uid            varchar(36)  not null,
        ref_project_id       bigint       not null,
        ref_change_id        bigint       not null,
        ref_change_item_type varchar(36)  not null,
        content_name         text         not null,
        content_index        int          not null,
        content              longtext     not null,
        primary key (id)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_project_version
    (
        id                   bigint       not null auto_increment,
        gmt_create           datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified         datetime     not null default CURRENT_TIMESTAMP,
        owner_uid            varchar(36)  not null,
        ref_project_id       bigint       not null,
        ref_devops_id        bigint       not null,
        ref_change_id        bigint       not null,
        version              datetime     not null,
        commit_id            varchar(128) not null,
        content              longtext     not null,
        type                 varchar(24) not null,
        primary key (id)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    create table dm_project_devops_item
    (
        id                   bigint       not null auto_increment,
        gmt_create           datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified         datetime     not null default CURRENT_TIMESTAMP,
        owner_uid            varchar(36)  not null,
        ref_project_id       bigint       not null,
        ref_devops_id        bigint       not null,
        content_name         text         not null,
        content_index        int          not null,
        content              longtext     not null,
        primary key (id)
    ) ENGINE = InnoDB
          DEFAULT CHARSET = utf8mb4
          COLLATE = utf8mb4_general_ci;

-- sample user and worker (end) --------------------
end
$$

call proc_init();