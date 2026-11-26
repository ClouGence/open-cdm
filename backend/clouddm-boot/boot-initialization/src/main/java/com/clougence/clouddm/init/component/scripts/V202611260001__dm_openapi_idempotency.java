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

public class V202611260001__dm_openapi_idempotency extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                CREATE TABLE IF NOT EXISTS dm_openapi_idempotency
                (
                    caller_uid      VARCHAR(127) COLLATE utf8mb4_bin NOT NULL,
                    operation_path  VARCHAR(255) COLLATE utf8mb4_bin NOT NULL,
                    idempotency_key VARCHAR(128) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    request_hash    CHAR(64) CHARACTER SET ascii COLLATE ascii_bin,
                    http_status     SMALLINT UNSIGNED,
                    response_body   MEDIUMTEXT,
                    expire_time_ms  BIGINT,
                    PRIMARY KEY (caller_uid, operation_path, idempotency_key),
                    KEY idx_openapi_idempotency_expire_time (expire_time_ms)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }
}
