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

    ALTER TABLE `rdp_user`
        CHANGE COLUMN `wechat_union_id` `union_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL AFTER `country`,
	    ADD COLUMN `sso_type` varchar(128) NULL AFTER `union_id`;
end
$$

call proc_init();