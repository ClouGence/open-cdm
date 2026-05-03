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

    create table if not exists dm_sql_audit
    (
        id               bigint        not null auto_increment,
        gmt_create       datetime      not null default CURRENT_TIMESTAMP,
        gmt_modified     datetime      not null default CURRENT_TIMESTAMP,
        operate_time     datetime(3)   not null,
        end_time         datetime(3),
        uid              varchar(36)   not null,
        user_name        varchar(255)  not null,
        primary_uid      varchar(36)   not null,
        affect_line      bigint,

        ds_id            bigint        not null,
        ds_desc          varchar(1024) not null,
        data_source_type varchar(128)  not null,

        session_id       varchar(255)  not null,
        status           varchar(32)   not null,

        log_ip           varchar(255)  not null,
        client_ip        varchar(255),
        work_seq_number  varchar(255)  not null,

        requester        varchar(32)   not null,

        sql_kind         varchar(32)   not null,
        exec_sql         text          not null,
        resource         text          not null,
        message          text,
        primary key (id)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    alter table dm_auto_exec_task
        drop column transactional_group;



-- sample user and worker (end) --------------------
end
$$

call proc_init();