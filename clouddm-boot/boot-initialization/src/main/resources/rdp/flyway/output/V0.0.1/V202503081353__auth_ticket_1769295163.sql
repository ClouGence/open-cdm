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

    create table if not exists rdp_ticket_auth
    (
        id                bigint auto_increment primary key,
        gmt_create        datetime   default CURRENT_TIMESTAMP not null,
        gmt_modified      datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
        rdp_ticket_ins_id varchar(64)                          not null,
        apply_auth_info   longtext                             not null,
        kind_type         varchar(64)                          not null,
        deleted           tinyint(1) default 0                 not null,
        constraint idx_unique_biz_id unique (rdp_ticket_ins_id)
    );

    alter table rdp_ticket_inst modify bind_ds_id bigint null;

    drop index uk_path on rdp_res_auth;

    create index idx_id_path_type_uid on rdp_res_auth (res_id, res_path, kind_type, owner_uid);
end
$$

call proc_init();