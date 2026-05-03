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

    create table dm_files
    (
        id           bigint       not null auto_increment,
        gmt_create   datetime     not null default CURRENT_TIMESTAMP,
        gmt_modified datetime     not null default CURRENT_TIMESTAMP,
        owner_uid    varchar(36)  not null,
        user_id      varchar(36)  not null,
        file_uri     varchar(500) not null,
        file_format  varchar(200) not null,
        inner_format tinyint      not null,
        status       varchar(64)  not null,
        message      text,
        unique_id    varchar(36)  not null,
        heartbeat    datetime     null,
        PRIMARY KEY (id),
        unique key unique_id (unique_id)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;


-- sample user and worker (end) --------------------
end
$$

call proc_init();