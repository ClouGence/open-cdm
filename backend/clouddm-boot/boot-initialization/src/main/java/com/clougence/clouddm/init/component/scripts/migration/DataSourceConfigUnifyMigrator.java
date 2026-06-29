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
package com.clougence.clouddm.init.component.scripts.migration;

import java.sql.*;

public class DataSourceConfigUnifyMigrator {

    private final Connection connection;

    public DataSourceConfigUnifyMigrator(Connection connection){
        this.connection = connection;
    }

    public void migrate() throws SQLException {
        migrateDsConfigKv();
        migrateDsFields();
        migrateDsAccount();
    }

    private void migrateDsConfigKv() throws SQLException {
        if (!tableExists("dm_ds_config_kv_4rdp") || !tableExists("dm_ds_config_kv_4dm")) {
            return;
        }
        execute("""
                INSERT INTO dm_ds_config_kv_4dm (
                    gmt_create,
                    gmt_modified,
                    data_source_id,
                    config_name,
                    config_group,
                    display,
                    desc_key,
                    value_require,
                    value_valid_regex,
                    config_value,
                    default_value,
                    value_advance,
                    read_only,
                    is_secret
                )
                SELECT
                    r.gmt_create,
                    r.gmt_modified,
                    r.data_source_id,
                    r.config_name,
                    r.config_group,
                    r.display,
                    r.desc_key,
                    r.value_require,
                    r.value_valid_regex,
                    r.config_value,
                    r.default_value,
                    r.value_advance,
                    r.read_only,
                    r.is_secret
                FROM dm_ds_config_kv_4rdp r
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM dm_ds_config_kv_4dm d
                    WHERE d.data_source_id = r.data_source_id
                      AND d.config_name = r.config_name
                )
                """);
    }

    private void migrateDsFields() throws SQLException {
        if (!tableExists("dm_ds") || !tableExists("dm_ds_config")) {
            return;
        }
        execute("""
                UPDATE dm_ds d
                JOIN dm_ds_config c
                  ON d.id = c.data_source_id
                SET d.status = COALESCE(d.status, c.status),
                    d.status_message = COALESCE(d.status_message, c.status_message),
                    d.bind_cluster_id = COALESCE(d.bind_cluster_id, c.bind_cluster_id),
                    d.ds_env_id = COALESCE(d.ds_env_id, c.bind_env_id)
                """);
    }

    private void migrateDsAccount() throws SQLException {
        if (!tableExists("dm_ds") || !columnExists("dm_ds", "account") || !columnExists("dm_ds", "password")) {
            return;
        }
        execute("""
                UPDATE dm_ds
                SET access_key = COALESCE(NULLIF(access_key, ''), account),
                    secret_key = COALESCE(NULLIF(secret_key, ''), password)
                """);
    }

    private boolean tableExists(String table) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("""
                select count(*)
                from information_schema.tables
                where table_schema = database()
                  and table_name = ?
                """)) {
            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getLong(1) > 0;
            }
        }
    }

    private boolean columnExists(String table, String column) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("""
                select count(*)
                from information_schema.columns
                where table_schema = database()
                  and table_name = ?
                  and column_name = ?
                """)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getLong(1) > 0;
            }
        }
    }

    private void execute(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
