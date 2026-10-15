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

import java.sql.Statement;
import java.util.List;

import org.flywaydb.core.api.migration.Context;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202609300001__ai_sql_assistant extends AbstractUpgradeJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            for (String sql : collectScript()) {
                statement.execute(sql);
            }
        }
    }

    @Override
    public List<String> collectScript() {
        return List.of("""
                CREATE TABLE dm_ai_provider_config
                (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    provider_code VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    display_name VARCHAR(128) NOT NULL,
                    adapter_type VARCHAR(32) NOT NULL,
                    base_url VARCHAR(2048) NULL,
                    access_token LONGTEXT NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_ai_provider_code (provider_code)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                CREATE TABLE dm_ai_model_config
                (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    model_code VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    label VARCHAR(80) NOT NULL,
                    provider_code VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    upstream_model_name VARCHAR(128) NOT NULL,
                    version INT NOT NULL,
                    verification_status VARCHAR(16) NOT NULL DEFAULT 'NOT_TESTED',
                    verified_version INT NULL,
                    verified_at DATETIME(3) NULL,
                    created_by VARCHAR(64) NOT NULL,
                    updated_by VARCHAR(64) NOT NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_ai_model_code (model_code),
                    UNIQUE KEY uk_ai_engine_name_model (label, upstream_model_name),
                    KEY idx_ai_model_provider (provider_code)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                CREATE TABLE dm_ai_session
                (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    session_id CHAR(36) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    puid VARCHAR(64) NOT NULL,
                    uid VARCHAR(64) NOT NULL,
                    engine_id CHAR(36) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    engine_version INT NOT NULL,
                    engine_name VARCHAR(80) NOT NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    compact_summary LONGTEXT,
                    title VARCHAR(255) DEFAULT NULL,
                    compact_anchor_history_id BIGINT DEFAULT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_ai_session_uuid (session_id),
                    KEY idx_ai_session_owner (uid,puid,gmt_modified,session_id),
                    KEY idx_ai_session_retention (gmt_modified)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                CREATE TABLE dm_ai_history
                (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    uuid CHAR(36) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    session_id BIGINT NOT NULL,
                    request_id CHAR(36) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    message LONGTEXT NOT NULL,
                    context_json LONGTEXT NOT NULL,
                    model VARCHAR(64) NOT NULL,
                    engine_version INT NOT NULL,
                    engine_name VARCHAR(80) NOT NULL,
                    referenced_objects_json LONGTEXT NULL,
                    usage_json JSON NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_ai_history_uuid (uuid),
                    UNIQUE KEY uk_ai_history_request (session_id, request_id),
                    KEY idx_ai_history_session (session_id, id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                CREATE TABLE dm_ai_event
                (
                    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                    session_id BIGINT NOT NULL,
                    history_id BIGINT NOT NULL,
                    event_type VARCHAR(64) NOT NULL,
                    event_data LONGTEXT NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    UNIQUE KEY uk_ai_history_event (history_id, event_type),
                    KEY idx_ai_history_event_session (session_id, id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                CREATE TABLE dm_ai_turn_run
                (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    run_id VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    uid VARCHAR(64) NOT NULL,
                    session_id BIGINT NOT NULL,
                    history_id BIGINT NOT NULL,
                    status VARCHAR(32) NOT NULL,
                    event_seq BIGINT NOT NULL DEFAULT 0,
                    error_code VARCHAR(64) NULL,
                    error_message TEXT NULL,
                    deadline DATETIME(3) NOT NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_ai_run_id (run_id),
                    UNIQUE KEY uk_ai_run_history (history_id),
                    KEY idx_ai_run_session (uid, session_id, status, id),
                    KEY idx_ai_run_deadline (status, deadline)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                CREATE TABLE dm_ai_turn_event
                (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    run_id VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
                    uid VARCHAR(64) NOT NULL,
                    session_id BIGINT NOT NULL,
                    history_id BIGINT NOT NULL,
                    seq BIGINT NOT NULL,
                    event_type VARCHAR(64) NOT NULL,
                    event_data LONGTEXT NULL,
                    created_ms BIGINT NOT NULL,
                    gmt_create DATETIME(3) NOT NULL,
                    gmt_modified DATETIME(3) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_ai_run_seq (run_id, seq),
                    KEY idx_ai_event_session (uid, session_id, id),
                    KEY idx_ai_event_created (created_ms)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
                """, """
                UPDATE dm_auth_role
                SET role_auth_labels = JSON_ARRAY_APPEND(COALESCE(role_auth_labels, JSON_ARRAY()), '$', 'DM_AI_ENGINE_MANAGE')
                WHERE owner_uid = '9999999999999999'
                    AND role_name = 'Manager'
                    AND inner_tag = 1
                    AND JSON_CONTAINS(COALESCE(role_auth_labels, JSON_ARRAY()), '"DM_AI_ENGINE_MANAGE"') = 0
                """);
    }
}
