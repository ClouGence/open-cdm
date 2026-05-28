/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }
}
