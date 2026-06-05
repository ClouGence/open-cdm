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

public class V202605070041__export extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    create table dm_files
                        (
                            id           bigint       not null auto_increment,
                            gmt_create   datetime     not null default CURRENT_TIMESTAMP,
                            gmt_modified datetime     not null default CURRENT_TIMESTAMP,
                            owner_uid    varchar(36)  not null,
                            user_id      varchar(36)  not null,
                            file_uri     varchar(500) not null,
                            file_format  varchar(200) not null,
                            inner_format tinyint      not null,
                            status       varchar(64)  not null,
                            message      text,
                            unique_id    varchar(36)  not null,
                            heartbeat    datetime     null,
                            PRIMARY KEY (id),
                            unique key unique_id (unique_id)
                        ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }
}
