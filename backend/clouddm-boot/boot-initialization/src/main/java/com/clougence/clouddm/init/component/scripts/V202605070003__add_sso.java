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

public class V202605070003__add_sso extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
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
                        )
                """, """
                    alter table rdp_user add column `wechat_union_id` varchar(32) DEFAULT NULL
                """);
    }

}
