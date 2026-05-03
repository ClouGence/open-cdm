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

CREATE TABLE IF NOT EXISTS `dm_console_heartbeat`
(
    id                INT(11) NOT NULL AUTO_INCREMENT,
    gmt_create        DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified      DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    console_ip        VARCHAR(32)                           NOT NULL,
    active            BOOLEAN                               NOT NULL DEFAULT FALSE,
    mac_address       VARCHAR(128)                          NOT NULL,
    cpu_stat          TEXT        DEFAULT NULL,
    mem_stat          TEXT        DEFAULT NULL,
    disk_stat         TEXT        DEFAULT NULL,
    version           VARCHAR(32) DEFAULT NULL,
    console_send_time DATETIME    DEFAULT CURRENT_TIMESTAMP NULL,
    hardware_uuid     VARCHAR(127) NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- sample user and worker (end) --------------------
end
$$

call proc_init();