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

    CREATE TABLE `rdp_sso_register` (
        `id` bigint NOT NULL AUTO_INCREMENT,
        `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `request_id` varchar(32) NOT NULL,
        `union_id` varchar(32) NOT NULL,
        `nickname` varchar(255) DEFAULT NULL,
        `register_status` varchar(32) NOT NULL,
        PRIMARY KEY (`id`),
        UNIQUE KEY `uk_request_id` (`request_id`) USING BTREE
    );

    alter table rdp_user add column `wechat_union_id` varchar(32) DEFAULT NULL;
end
$$

call proc_init();