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

    ALTER TABLE `rdp_user` ADD COLUMN `use_mfa` tinyint(1) NOT NULL DEFAULT 0;

    CREATE TABLE IF NOT EXISTS `rdp_user_mfa`
    (
        `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
        `gmt_create`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `user_id`          bigint(20)   NOT NULL,
        `uid`              varchar(127) NOT NULL,
        `mfa_status`       varchar(127) NOT NULL,
        `mfa_key`          varchar(512) NOT NULL,
        `reset_mfa_key`    varchar(512) DEFAULT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_user_id` (`user_id`),
        KEY `idx_uid` (`uid`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;
end
$$

call proc_init();