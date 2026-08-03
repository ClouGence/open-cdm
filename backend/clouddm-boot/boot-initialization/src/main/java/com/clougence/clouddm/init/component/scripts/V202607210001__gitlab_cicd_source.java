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

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.utils.HashUtils;
import com.clougence.utils.StringUtils;

/** Database changes and data backfill for GitLab Self-Managed CI/CD sources. */
public class V202607210001__gitlab_cicd_source extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                ALTER TABLE dm_change_flow
                    ADD COLUMN `scm_repo_identifier` varchar(512) DEFAULT NULL
                        COMMENT 'SCM immutable repository identifier' AFTER `scm_repo_space`
                """, """
                ALTER TABLE dm_change_flow
                    ADD COLUMN `scm_repo_hook_signing_token` text DEFAULT NULL
                        COMMENT 'Webhook signing token' AFTER `scm_repo_hook_pwd`
                """, """
                CREATE TABLE IF NOT EXISTS dm_change_trigger_receipt
                (
                    id           bigint       NOT NULL AUTO_INCREMENT,
                    gmt_create   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    gmt_modified datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    owner_uid    varchar(36)  NOT NULL,
                    ref_flow_id  bigint       NOT NULL,
                    provider     varchar(12)  NOT NULL,
                    delivery_id  varchar(255) DEFAULT NULL,
                    commit_id    varchar(128) NOT NULL,
                    trigger_type varchar(32)  NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_trigger_delivery(owner_uid, ref_flow_id, delivery_id),
                    UNIQUE KEY uk_trigger_commit(owner_uid, ref_flow_id, commit_id),
                    KEY idx_trigger_receipt_time(gmt_create)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }

    @Override
    protected void afterMigrate(Connection connection) throws Exception {
        backfillRepositoryIdentifiers(connection);
        backfillFlowHashcodes(connection);
        backfillTriggerReceipts(connection);
    }

    private void backfillRepositoryIdentifiers(Connection connection) throws Exception {
        String selectSql = """
                SELECT id, scm_repo_space, scm_repo_name
                FROM dm_change_flow
                WHERE scm_repo_identifier IS NULL OR scm_repo_identifier = ''
                ORDER BY id
                """;
        String updateSql = """
                UPDATE dm_change_flow
                SET scm_repo_identifier = ?
                WHERE id = ?
                """;
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(selectSql);
                PreparedStatement update = connection.prepareStatement(updateSql)) {
            while (rs.next()) {
                String repoName = rs.getString("scm_repo_name");
                if (repoName == null || repoName.isBlank()) {
                    continue;
                }

                String repoIdentifier = repoName;
                String repoSpace = rs.getString("scm_repo_space");
                if (repoSpace != null && !repoSpace.isBlank()) {
                    repoIdentifier = repoSpace + "/" + repoName;
                }

                update.setString(1, repoIdentifier);
                update.setLong(2, rs.getLong("id"));
                update.executeUpdate();
            }
        }
    }

    private void backfillFlowHashcodes(Connection connection) throws Exception {
        String selectSql = """
                SELECT id, ref_scm_id, scm_repo_identifier, scm_repo_url, scm_repo_branch, ds_id, ds_path
                FROM dm_change_flow
                ORDER BY id
                """;
        String updateSql = """
                UPDATE dm_change_flow
                SET flow_hashcode = ?
                WHERE id = ?
                """;
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(selectSql);
                PreparedStatement update = connection.prepareStatement(updateSql)) {
            while (rs.next()) {
                String repoKey = rs.getString("scm_repo_identifier");
                if (StringUtils.isBlank(repoKey)) {
                    repoKey = rs.getString("scm_repo_url");
                }
                String repoBranch = rs.getString("scm_repo_branch");
                String dsId = rs.getString("ds_id");
                String dsPath = rs.getString("ds_path");
                if (StringUtils.isBlank(repoKey) || StringUtils.isBlank(repoBranch) || StringUtils.isBlank(dsId)
                    || dsPath == null) {
                    continue;
                }

                String normalizedDsPath = StringUtils.stripStart(dsPath.trim(), "/");
                String hashSource = rs.getLong("ref_scm_id") + "/" + repoKey.trim() + "/" + repoBranch.trim() + "/"
                                    + dsId + "/[" + normalizedDsPath + "]";
                update.setLong(1, HashUtils.fnvHash(hashSource));
                update.setLong(2, rs.getLong("id"));
                update.executeUpdate();
            }
        }
    }

    private void backfillTriggerReceipts(Connection connection) throws Exception {
        Map<Long, String> providerByFlowId = new HashMap<>();
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("""
                        SELECT id, ref_scm_type
                        FROM dm_change_flow
                        ORDER BY id
                        """)) {
            while (rs.next()) {
                providerByFlowId.put(rs.getLong("id"), rs.getString("ref_scm_type"));
            }
        }

        String selectSql = """
                SELECT id, gmt_create, gmt_modified, owner_uid, ref_flow_id, last_commit_id
                FROM dm_change
                WHERE last_commit_id IS NOT NULL AND last_commit_id != ''
                ORDER BY owner_uid, ref_flow_id, last_commit_id, id
                """;
        String insertSql = """
                INSERT INTO dm_change_trigger_receipt(
                    gmt_create, gmt_modified, owner_uid, ref_flow_id, provider, delivery_id, commit_id, trigger_type
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(selectSql);
                PreparedStatement insert = connection.prepareStatement(insertSql)) {
            boolean hasRow = rs.next();
            while (hasRow) {
                String ownerUid = rs.getString("owner_uid");
                long flowId = rs.getLong("ref_flow_id");
                String commitId = rs.getString("last_commit_id");
                Timestamp earliestCreateTime = rs.getTimestamp("gmt_create");
                Timestamp latestModifiedTime = rs.getTimestamp("gmt_modified");

                hasRow = rs.next();
                while (hasRow && Objects.equals(ownerUid, rs.getString("owner_uid"))
                        && flowId == rs.getLong("ref_flow_id")
                        && Objects.equals(commitId, rs.getString("last_commit_id"))) {
                    Timestamp createTime = rs.getTimestamp("gmt_create");
                    if (createTime != null && (earliestCreateTime == null || createTime.before(earliestCreateTime))) {
                        earliestCreateTime = createTime;
                    }
                    Timestamp modifiedTime = rs.getTimestamp("gmt_modified");
                    if (modifiedTime != null && (latestModifiedTime == null || modifiedTime.after(latestModifiedTime))) {
                        latestModifiedTime = modifiedTime;
                    }
                    hasRow = rs.next();
                }

                String provider = providerByFlowId.get(flowId);
                if (provider == null || provider.isBlank()) {
                    continue;
                }
                insert.setTimestamp(1, earliestCreateTime);
                insert.setTimestamp(2, latestModifiedTime);
                insert.setString(3, ownerUid);
                insert.setLong(4, flowId);
                insert.setString(5, provider);
                insert.setNull(6, Types.VARCHAR);
                insert.setString(7, commitId);
                insert.setString(8, "Legacy");
                insert.executeUpdate();
            }
        }
    }
}
