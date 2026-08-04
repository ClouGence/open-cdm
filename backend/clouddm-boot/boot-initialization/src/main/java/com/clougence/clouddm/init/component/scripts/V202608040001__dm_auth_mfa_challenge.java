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

public class V202608040001__dm_auth_mfa_challenge extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    CREATE TABLE IF NOT EXISTS dm_auth_mfa_challenge
                    (
                        id                   bigint       NOT NULL AUTO_INCREMENT,
                        gmt_create           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        gmt_modified         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        uid                  varchar(127) NOT NULL,
                        action_type          varchar(64)  NOT NULL,
                        login_type           varchar(32)  NOT NULL,
                        challenge_token_hash varchar(64)  NOT NULL,
                        retry_count          int          NOT NULL DEFAULT 0,
                        expire_time_ms       bigint       NOT NULL,
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_challenge_token_hash (challenge_token_hash),
                        KEY idx_uid_action_type (uid, action_type),
                        KEY idx_expire_time_ms (expire_time_ms)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }
}
