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

public class V202605070027__mfa extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_user` ADD COLUMN `use_mfa` tinyint(1) NOT NULL DEFAULT 0
                """, """
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
                """);
    }
}
