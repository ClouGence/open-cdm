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

create table if not exists dm_meta_information_cache
(
    id           bigint        not null auto_increment,
    gmt_create   datetime      not null default CURRENT_TIMESTAMP,
    gmt_modified datetime      not null default CURRENT_TIMESTAMP,
    primary_uid  varchar(36)   not null,
    ds_id        bigint        not null,
    path         varchar(512)  not null,
    type         varchar(32)   not null,
    context      longtext      not null,
    primary key (id)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

    create unique index id_path_type_uindex
        on dm_meta_information_cache (ds_id, path, type);

-- sample user and worker (end) --------------------
end
$$

call proc_init();