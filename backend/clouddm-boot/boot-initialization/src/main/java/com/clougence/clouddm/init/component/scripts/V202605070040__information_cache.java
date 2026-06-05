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

public class V202605070040__information_cache extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    create table if not exists dm_meta_information_cache
                    (
                        id           bigint        not null auto_increment,
                        gmt_create   datetime      not null default CURRENT_TIMESTAMP,
                        gmt_modified datetime      not null default CURRENT_TIMESTAMP,
                        primary_uid  varchar(36)   not null,
                        ds_id        bigint        not null,
                        path         varchar(512)  not null,
                        type         varchar(32)   not null,
                        context      longtext      not null,
                        primary key (id)
                        ) ENGINE = InnoDB
                        DEFAULT CHARSET = utf8mb4
                """, """
                    create unique index id_path_type_uindex
                            on dm_meta_information_cache (ds_id, path, type)
                """);
    }
}
