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

public class V202605070025__aws_marketplace extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    CREATE TABLE IF NOT EXISTS `rdp_market_sub`
                        (
                            `id`                         bigint                                  NOT NULL AUTO_INCREMENT,
                            `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `uid`                        varchar(127) NOT NULL,
                            `market_type`                varchar(255) NOT NULL,
                            `aws_customer_id`            varchar(255) DEFAULT NULL,
                            `aws_product_code`           varchar(255) DEFAULT NULL,
                            `aws_account_id`             varchar(255) DEFAULT NULL,
                            `sub_status`                 varchar(64)  DEFAULT 'SUBSCRIBE',
                            PRIMARY KEY (`id`),
                            KEY `idx_uid` (`uid`),
                            KEY `idx_aws_uniq` (`aws_customer_id`, `aws_product_code`,`aws_account_id`)
                        ) ENGINE = InnoDB
                             DEFAULT CHARSET = utf8mb4
                """, """
                    ALTER TABLE `rdp_user`
                            ADD COLUMN `marketplace_type` varchar(64) DEFAULT 'NONE'
                """);
    }
}
