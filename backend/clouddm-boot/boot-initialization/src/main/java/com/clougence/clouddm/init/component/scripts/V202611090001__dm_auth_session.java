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

public class V202611090001__dm_auth_session extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List
                .of("""
                            CREATE TABLE IF NOT EXISTS dm_auth_session
                            (
                                id              bigint      NOT NULL AUTO_INCREMENT,
                                uid             varchar(127) NOT NULL,
                                token_hash      char(64)    CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                                login_type      varchar(32) NULL,
                                gmt_create      bigint      NOT NULL,
                                expire_time_ms  bigint      NOT NULL,
                                PRIMARY KEY (id),
                                UNIQUE KEY uk_token_hash (token_hash),
                                KEY idx_uid (uid),
                                KEY idx_expire_time_ms (expire_time_ms)
                            ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                        """, """
                            ALTER TABLE dm_auth_user
                                MODIFY COLUMN op_password VARCHAR(512) COLLATE utf8mb4_general_ci DEFAULT NULL
                                    COMMENT 'Deprecated: operation password; retained for downgrade compatibility',
                                MODIFY COLUMN last_try_op_verify_time DATETIME NOT NULL DEFAULT '1970-01-01 12:00:00'
                                    COMMENT 'Deprecated: last operation-password verification time; retained for downgrade compatibility',
                                MODIFY COLUMN op_verify_fail_count INT NOT NULL DEFAULT '0'
                                    COMMENT 'Deprecated: operation-password verification failure count; retained for downgrade compatibility',
                                MODIFY COLUMN op_locked TINYINT(1) NOT NULL DEFAULT '0'
                                    COMMENT 'Deprecated: operation-password verification lock state; retained for downgrade compatibility'
                        """);
    }
}
