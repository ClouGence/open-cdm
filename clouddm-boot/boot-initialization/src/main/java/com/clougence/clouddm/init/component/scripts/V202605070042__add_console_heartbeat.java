package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070042__add_console_heartbeat extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
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
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4\
                """);
    }
}
