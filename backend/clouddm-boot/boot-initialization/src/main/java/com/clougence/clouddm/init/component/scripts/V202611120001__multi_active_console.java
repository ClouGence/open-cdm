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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import org.flywaydb.core.api.migration.Context;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.component.config.ConsoleClusterProperties;
import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202611120001__multi_active_console extends AbstractUpgradeJavaMigration {

    private static final Pattern SCRIPT_NAME = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.sql");

    @Override
    public List<String> collectScript() {
        return List.of("""
                CREATE TABLE dm_sys_console (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    gmt_create DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                    gmt_modified DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
                    instance_id VARCHAR(64) NOT NULL,
                    console_instance_id VARCHAR(64) COLLATE utf8mb4_bin NULL,
                    advertise_url VARCHAR(512) NOT NULL,
                    state VARCHAR(16) NOT NULL,
                    lease_until DATETIME(3) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_instance_id (instance_id),
                    UNIQUE KEY uk_console_instance_id (console_instance_id),
                    KEY idx_console_state_lease (state, lease_until)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """, """
                ALTER TABLE dm_sys_worker
                    ADD COLUMN owner_instance_id VARCHAR(64) NULL,
                    ADD COLUMN connection_generation BIGINT NOT NULL DEFAULT 0
                """, """
                CREATE TABLE dm_sys_console_scheduler (
                    id INT NOT NULL,
                    owner_instance_id VARCHAR(64) NULL,
                    PRIMARY KEY (id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """, """
                ALTER TABLE dm_exec_session
                    ADD COLUMN active_batch_id VARCHAR(64) NULL
                """, """
                ALTER TABLE dm_exec_async_task
                    ADD COLUMN console_instance_id VARCHAR(64) NULL
                """, """
                ALTER TABLE dm_exec_sql_script
                    ADD COLUMN sql_content LONGTEXT NULL
                """);
    }

    @Override
    public void migrate(Context context) throws Exception {
        String consoleInstanceId = context.getConfiguration().getPlaceholders().get(ConsoleClusterProperties.INSTANCE_ID_PROPERTY);
        if (!ConsoleClusterProperties.isValidInstanceId(consoleInstanceId)) {
            throw new SQLException("A valid " + ConsoleClusterProperties.INSTANCE_ID_PROPERTY + " is required before migrating Console tasks.");
        }

        super.migrate(context);
        backfillLegacyTasks(context.getConnection(), consoleInstanceId);
    }

    @Override
    protected void afterMigrate(Connection connection) throws Exception {
        try (PreparedStatement query = connection.prepareStatement("""
                SELECT session_id
                FROM dm_exec_session
                GROUP BY session_id
                HAVING COUNT(*) > 1
                LIMIT 1
                """); ResultSet duplicate = query.executeQuery()) {
            if (duplicate.next()) {
                throw new SQLException("Duplicate SQL session identity must be reconciled before upgrading: " + duplicate.getString(1));
            }
        }

        safeExecute(connection, "ALTER TABLE dm_exec_session ADD UNIQUE KEY uk_exec_session_identity (session_id)");
        safeExecute(connection, "INSERT IGNORE INTO dm_sys_console_scheduler (id) VALUES (1)");
        // Legacy unsubmitted uploads only existed on their original Console. Make callers upload them again.
        safeExecute(connection, """
                UPDATE dm_sys_attachment
                SET gmt_modified = '1970-01-01 00:00:00'
                WHERE attachment_status = 'EDITING' AND blob_content IS NULL
                """);
        migrateSqlScripts(connection);
    }

    private void backfillLegacyTasks(Connection connection, String consoleInstanceId) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement("""
                UPDATE dm_exec_async_task
                SET console_instance_id = ?
                WHERE console_instance_id IS NULL
                    AND status IN ('INIT', 'WAIT_START', 'BLOCK', 'RUNNING', 'CANCELING', 'PAUSING', 'PAUSE')
                """)) {
            update.setString(1, consoleInstanceId);
            update.executeUpdate();
        }
    }

    private void migrateSqlScripts(Connection connection) throws Exception {
        try (PreparedStatement query = connection.prepareStatement("""
                SELECT id, version, file_uri
                FROM dm_exec_sql_script
                WHERE status = 'Ready' AND sql_content IS NULL
                ORDER BY id
                """); ResultSet scripts = query.executeQuery(); PreparedStatement update = connection.prepareStatement("""
                UPDATE dm_exec_sql_script
                SET sql_content = ?
                WHERE id = ? AND version = ? AND file_uri = ? AND sql_content IS NULL
                """)) {
            while (scripts.next()) {
                long id = scripts.getLong("id");
                String uri = scripts.getString("file_uri");
                if (uri == null || !uri.startsWith("local://sql-script/")) {
                    throw new SQLException("Unsupported stored SQL script location, scriptId=" + id);
                }

                String name = uri.substring("local://sql-script/".length());
                if (!SCRIPT_NAME.matcher(name).matches()) {
                    throw new SQLException("Invalid stored SQL script location, scriptId=" + id);
                }

                Path primary = Path.of(GlobalConfUtils.getUserDataHome(), "sql-scripts", name);
                Path legacy = Path.of(GlobalConfUtils.getUserDataHome(), "sql-script", name);
                Path source = primary;
                if (!Files.isRegularFile(source)) {
                    source = legacy;
                }

                if (!Files.isRegularFile(source)) {
                    throw new SQLException("SQL script file is missing; restore the original data directory, scriptId=" + id);
                }

                String content = Files.readString(source, StandardCharsets.UTF_8);
                if (content.isBlank() || (Files.isRegularFile(primary) && Files.isRegularFile(legacy) && !content.equals(Files.readString(legacy, StandardCharsets.UTF_8)))) {
                    throw new SQLException("SQL script content cannot be verified, scriptId=" + id);
                }

                update.setString(1, content);
                update.setLong(2, id);
                update.setLong(3, scripts.getLong("version"));
                update.setString(4, uri);
                if (update.executeUpdate() != 1) {
                    throw new SQLException("SQL script changed during migration, scriptId=" + id);
                }
            }
        }

    }

}
