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

public class V202609170001__sql_script_favorites extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                CREATE TABLE dm_exec_sql_script
                (
                    id           bigint       NOT NULL AUTO_INCREMENT,
                    gmt_create   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    gmt_modified datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    owner_uid    varchar(64)  NOT NULL,
                    name         varchar(128) DEFAULT NULL,
                    ds_type      varchar(64)  NOT NULL,
                    file_uri     varchar(500) NOT NULL,
                    status       varchar(16)  NOT NULL DEFAULT 'Ready',
                    version      bigint       NOT NULL DEFAULT 0,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_sql_script_owner_name (owner_uid, name),
                    UNIQUE KEY uk_sql_script_file_uri (file_uri),
                    KEY idx_sql_script_owner_status_modified (owner_uid, status, gmt_modified, id),
                    KEY idx_sql_script_status_modified (status, gmt_modified, id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }
}
