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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202610260001__custom_approval_engine extends AbstractUpgradeJavaMigration {
    @Override
    public List<String> collectScript() {
        return List.of("""
                ALTER TABLE dm_approval_template
                    ADD COLUMN template_source VARCHAR(16) NOT NULL DEFAULT 'PROVIDER',
                    ADD COLUMN engine_config_id VARCHAR(36) NULL,
                    ADD COLUMN version BIGINT NOT NULL DEFAULT 1
                """, """
                ALTER TABLE dm_approval_template
                    ADD UNIQUE KEY uk_template_owner_engine (primary_uid, engine_config_id),
                    ADD KEY idx_template_owner_source_modified (primary_uid, template_source, approval_type, gmt_modified, id)
                """, """
                CREATE TABLE dm_approval_node_task (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    primary_uid VARCHAR(127) NOT NULL,
                    process_activity_id BIGINT NOT NULL,
                    approver_uid VARCHAR(127) NOT NULL,
                    approver_name VARCHAR(255) NOT NULL,
                    status VARCHAR(16) NOT NULL DEFAULT 'WAITING',
                    comment TEXT NULL,
                    start_time DATETIME NULL,
                    finish_time DATETIME NULL,
                    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_node_task_activity_user (process_activity_id, approver_uid),
                    KEY idx_node_task_pending (primary_uid, approver_uid, status, gmt_create, id),
                    KEY idx_node_task_history (primary_uid, approver_uid, finish_time, id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci
                """);
    }

    @Override
    protected void afterMigrate(Connection connection) throws Exception {
        safeExecute(connection, """
                UPDATE dm_approval_template
                SET template_source = 'BUILTIN'
                WHERE approval_type IN ('Internal', 'SELFMAKE')
                  AND template_identity = 'PROC-SELFMAKE-000000001'
                  AND template_source = 'PROVIDER'
                """);
        removeDuplicateTemplates(connection);
        // The identity constraint depends on correcting duplicate legacy template caches first.
        safeExecute(connection, """
                ALTER TABLE dm_approval_template
                    ADD UNIQUE KEY uk_template_owner_type_identity (primary_uid, approval_type, template_identity),
                    DROP INDEX cache_appro_template_puid
                """);
    }

    private void removeDuplicateTemplates(Connection connection) throws SQLException {
        Set<Long> processedIds = new HashSet<>();
        try (PreparedStatement query = connection.prepareStatement("""
                SELECT
                    id, primary_uid, approval_type, template_identity, template_source
                FROM
                    dm_approval_template
                WHERE
                    template_source IN ('PROVIDER', 'BUILTIN')
                ORDER BY id
                """); ResultSet templates = query.executeQuery(); PreparedStatement duplicates = connection.prepareStatement("""
                SELECT
                    id
                FROM
                    dm_approval_template
                WHERE
                    primary_uid       = ?
                    AND
                    approval_type     = ?
                    AND
                    template_identity = ?
                    AND
                    template_source   = ?
                ORDER BY id
                """); PreparedStatement delete = connection.prepareStatement("""
                DELETE FROM
                    dm_approval_template
                WHERE
                    id = ?
                """)) {
            while (templates.next()) {
                if (!processedIds.add(templates.getLong("id"))) {
                    continue;
                }

                // Keep database collation semantics when matching template identities.
                duplicates.setString(1, templates.getString("primary_uid"));
                duplicates.setString(2, templates.getString("approval_type"));
                duplicates.setString(3, templates.getString("template_identity"));
                duplicates.setString(4, templates.getString("template_source"));
                try (ResultSet matches = duplicates.executeQuery()) {
                    boolean first = true;
                    while (matches.next()) {
                        long id = matches.getLong("id");
                        processedIds.add(id);
                        if (first) {
                            first = false;
                            continue;
                        }

                        delete.setLong(1, id);
                        delete.addBatch();
                    }
                }
            }

            delete.executeBatch();
        }
    }
}
