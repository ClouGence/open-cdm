DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init ()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1062 begin end;
    declare continue handler for 1050 begin end;
    declare continue handler for 1072 begin end;
    declare continue handler for 1091 begin end;

-- your DDLs here
   alter table rdp_ds_kv_base_config add column `conf_val_type` varchar(64) NOT NULL DEFAULT "TEXT";

   alter table rdp_data_source add column `info_fetch_type` varchar(64) DEFAULT NULL;

   alter table rdp_data_source_history add column `info_fetch_type` varchar(64) DEFAULT NULL;
end
$$

call proc_init();
