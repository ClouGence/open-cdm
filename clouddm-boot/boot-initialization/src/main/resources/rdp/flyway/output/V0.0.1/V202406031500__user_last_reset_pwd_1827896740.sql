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

    alter table rdp_user add column `last_date_update_pwd` datetime DEFAULT NULL;

    alter table rdp_user_kv_base_config add column `conf_val_type` varchar(64) NOT NULL DEFAULT "TEXT";
end
$$

call proc_init();