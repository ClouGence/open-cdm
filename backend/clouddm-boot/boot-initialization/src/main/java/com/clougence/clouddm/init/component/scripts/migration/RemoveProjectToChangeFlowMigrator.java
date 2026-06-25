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
import java.util.*;

public class RemoveProjectToChangeFlowMigrator {

    private static final String[] SOURCE_TABLES = { "dm_project_scm",//
                                                    "dm_project",//
                                                    "dm_project_msg",//
                                                    "dm_project_devops",//
                                                    "dm_project_devops_item",//
                                                    "dm_project_change",//
                                                    "dm_project_change_item",//
                                                    "dm_project_version"//
    };

    private final Connection      connection;

    public RemoveProjectToChangeFlowMigrator(Connection connection){
        this.connection = connection;
    }

    public void migrate() throws SQLException {
        if (hasCompleteSourceTables()) {
            validateDevopsHashcode();
            migrateScmProviders();

            Map<LegacyProjectKey, LegacyProjectRow> legacyProjects = loadLegacyProjects();
            Map<LegacyProjectKey, LegacyProjectMsgRow> legacyMessages = loadLegacyProjectMessages();
            Map<LegacyProjectKey, Integer> devopsCounts = loadDevopsCounts();
            Map<LegacyProjectNameKey, Integer> legacyProjectNameCounts = loadLegacyProjectNameCounts();

            migrateChangeFlows(legacyProjects, legacyMessages, devopsCounts, legacyProjectNameCounts);
            migrateChangeFlowItems();
            migrateChanges();
            migrateChangeItems(loadChangeFlowIds());
            migrateChangeVersions();

            validateMigration();
            resetAutoIncrement();
        }
        migrateRoleAuthLabels();
        migrateUserConfigs();
    }

    private boolean hasCompleteSourceTables() throws SQLException {
        boolean hasAnySourceTable = false;
        List<String> missingTables = new ArrayList<>();
        for (String table : SOURCE_TABLES) {
            if (tableExists(table)) {
                hasAnySourceTable = true;
            } else {
                missingTables.add(table);
            }
        }
        if (!hasAnySourceTable) {
            return false;
        }
        if (!missingTables.isEmpty()) {
            throw new IllegalStateException("Project source tables are partially missing, missingTables=" + missingTables);
        }
        return true;
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

    private void validateDevopsHashcode() throws SQLException {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("select id, devops_hashcode from dm_project_devops")) {
            while (rs.next()) {
                String value = rs.getString("devops_hashcode");
                if (value == null || !value.matches("^-?[0-9]+$")) {
                    throw new IllegalStateException("Invalid dm_project_devops.devops_hashcode, id=" + rs.getLong("id") + ", value=" + value);
                }
            }
        }
    }

    private void migrateScmProviders() throws SQLException {
        String selectSql = """
                select id, gmt_create, gmt_modified, owner_uid, scm_type, scm_display, scm_service_url, scm_access_token
                from dm_project_scm
                order by id
                """;
        String insertSql = """
                insert into dm_git_ops_scm(
                    id, gmt_create, gmt_modified, owner_uid, scm_type, scm_display, scm_service_url, scm_access_token
                ) values (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(selectSql); PreparedStatement ps = connection.prepareStatement(insertSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                if (existsById("dm_git_ops_scm", id)) {
                    continue;
                }
                ps.setLong(1, id);
                ps.setTimestamp(2, rs.getTimestamp("gmt_create"));
                ps.setTimestamp(3, rs.getTimestamp("gmt_modified"));
                ps.setString(4, rs.getString("owner_uid"));
                ps.setString(5, rs.getString("scm_type"));
                ps.setString(6, rs.getString("scm_display"));
                ps.setString(7, rs.getString("scm_service_url"));
                ps.setString(8, rs.getString("scm_access_token"));
                ps.executeUpdate();
            }
        }
    }

    private void migrateChangeFlows(Map<LegacyProjectKey, LegacyProjectRow> legacyProjects, Map<LegacyProjectKey, LegacyProjectMsgRow> legacyMessages,
                                    Map<LegacyProjectKey, Integer> devopsCounts, Map<LegacyProjectNameKey, Integer> legacyProjectNameCounts) throws SQLException {
        String insertSql = """
                insert into dm_change_flow(
                    id, gmt_create, gmt_modified, owner_uid, flow_uid, flow_name, flow_desc, flow_manager_uid,
                    flow_status, flow_check, flow_approve, flow_execute, flow_options, flow_scm_options,
                    ref_scm_id, ref_scm_type, scm_repo_space, scm_repo_name, scm_repo_url, scm_repo_branch,
                    scm_repo_event, scm_repo_script, scm_repo_hook_pwd, enable_hook, enable_trigger, trigger_token,
                    ds_id, ds_type, ds_instance, ds_desc, ds_path, ref_msg_id, ref_msg_type, msg_language,
                    enable_msg, event_flow_status, event_flow_config, event_change_life, event_change_notice,
                    callback_url, callback_method, enable_callback, flow_hashcode, enable, deleted
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select * from dm_project_devops order by id");
                PreparedStatement ps = connection.prepareStatement(insertSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                if (existsById("dm_change_flow", id)) {
                    continue;
                }

                String ownerUid = rs.getString("owner_uid");
                long legacyProjectId = rs.getLong("ref_project_id");
                LegacyProjectKey legacyProjectKey = new LegacyProjectKey(ownerUid, legacyProjectId);
                LegacyProjectRow legacyProject = legacyProjects.get(legacyProjectKey);
                if (legacyProject == null) {
                    throw new IllegalStateException("Missing dm_project for dm_project_devops.id=" + id + ", ref_project_id=" + legacyProjectId);
                }
                LegacyProjectMsgRow legacyMessage = legacyMessages.get(legacyProjectKey);
                boolean legacyProjectDeleted = "DELETE".equals(legacyProject.legacyProjectStatus);
                boolean devopsDeleted = bool(rs, "deleted");
                boolean normalAndActive = "NORMAL".equals(legacyProject.legacyProjectStatus) && !devopsDeleted;

                ps.setLong(1, id);
                ps.setTimestamp(2, rs.getTimestamp("gmt_create"));
                ps.setTimestamp(3, rs.getTimestamp("gmt_modified"));
                ps.setString(4, ownerUid);
                ps.setString(5, "CF-" + id);
                ps.setString(6, buildFlowName(legacyProject, rs, devopsCounts, legacyProjectNameCounts));
                ps.setString(7, legacyProject.flowDesc);
                ps.setString(8, legacyProject.flowManagerUid);
                ps.setString(9, devopsDeleted || legacyProjectDeleted ? "DELETE" : legacyProject.legacyProjectStatus);
                ps.setString(10, defaultString(legacyProject.flowCheck, "Failure"));
                ps.setString(11, defaultString(legacyProject.flowApprove, "Enable"));
                ps.setString(12, defaultString(legacyProject.flowExecute, "Manual"));
                ps.setString(13, defaultString(legacyProject.options, "{}"));
                ps.setString(14, defaultString(rs.getString("devops_options"), "{}"));
                ps.setLong(15, rs.getLong("ref_scm_id"));
                ps.setString(16, rs.getString("ref_scm_type"));
                ps.setString(17, rs.getString("scm_repo_space"));
                ps.setString(18, rs.getString("scm_repo_name"));
                ps.setString(19, rs.getString("scm_repo_url"));
                ps.setString(20, rs.getString("scm_repo_branch"));
                ps.setString(21, rs.getString("scm_repo_event"));
                ps.setString(22, rs.getString("scm_repo_script"));
                ps.setString(23, rs.getString("scm_repo_hook_pwd"));
                ps.setInt(24, bool(rs, "enable_hook") ? 1 : 0);
                ps.setInt(25, bool(rs, "enable_trigger") ? 1 : 0);
                ps.setString(26, rs.getString("trigger_token"));
                ps.setLong(27, rs.getLong("ds_id"));
                ps.setString(28, rs.getString("ds_type"));
                ps.setString(29, rs.getString("ds_instance"));
                ps.setString(30, rs.getString("ds_desc"));
                ps.setString(31, rs.getString("ds_path"));
                setNullableLong(ps, 32, legacyMessage == null ? null : legacyMessage.refMsgId);
                ps.setString(33, legacyMessage == null ? null : legacyMessage.refMsgType);
                ps.setString(34, legacyMessage == null ? null : legacyMessage.language);
                ps.setInt(35, legacyMessage != null && legacyMessage.enable && normalAndActive ? 1 : 0);
                ps.setInt(36, legacyMessage == null || !legacyMessage.eventFlowStatus ? 0 : 1);
                ps.setInt(37, legacyMessage == null || !legacyMessage.eventFlowConfig ? 0 : 1);
                ps.setInt(38, legacyMessage == null || !legacyMessage.eventChangeLife ? 0 : 1);
                ps.setInt(39, legacyMessage == null || !legacyMessage.eventChangeNotice ? 0 : 1);
                ps.setString(40, rs.getString("callback_url"));
                ps.setString(41, rs.getString("callback_method"));
                ps.setInt(42, bool(rs, "enable_callback") ? 1 : 0);
                ps.setLong(43, Long.parseLong(rs.getString("devops_hashcode")));
                ps.setInt(44, normalAndActive && bool(rs, "enable") ? 1 : 0);
                ps.setInt(45, devopsDeleted || legacyProjectDeleted ? 1 : 0);
                ps.executeUpdate();
            }
        }
    }

    private void migrateChangeFlowItems() throws SQLException {
        String insertSql = """
                insert into dm_change_flow_item(id, gmt_create, gmt_modified, owner_uid, ref_flow_id, content_name, content_index, content)
                values (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("""
                select id, gmt_create, gmt_modified, owner_uid, ref_devops_id, content_name, content_index, content
                from dm_project_devops_item
                order by id
                """); PreparedStatement ps = connection.prepareStatement(insertSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                if (existsById("dm_change_flow_item", id)) {
                    continue;
                }
                ps.setLong(1, id);
                ps.setTimestamp(2, rs.getTimestamp("gmt_create"));
                ps.setTimestamp(3, rs.getTimestamp("gmt_modified"));
                ps.setString(4, rs.getString("owner_uid"));
                ps.setLong(5, rs.getLong("ref_devops_id"));
                ps.setString(6, rs.getString("content_name"));
                ps.setInt(7, rs.getInt("content_index"));
                ps.setString(8, rs.getString("content"));
                ps.executeUpdate();
            }
        }
    }

    private void migrateChanges() throws SQLException {
        String insertSql = """
                insert into dm_change(
                    id, gmt_create, gmt_modified, owner_uid, ref_flow_id, change_name, change_time, change_branch,
                    current_step, current_status, schedule_time, version, remark, try_times, last_commit_id, lock_status, flow_walked
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select * from dm_project_change order by id");
                PreparedStatement ps = connection.prepareStatement(insertSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                if (existsById("dm_change", id)) {
                    continue;
                }
                ps.setLong(1, id);
                ps.setTimestamp(2, rs.getTimestamp("gmt_create"));
                ps.setTimestamp(3, rs.getTimestamp("gmt_modified"));
                ps.setString(4, rs.getString("owner_uid"));
                ps.setLong(5, rs.getLong("ref_devops_id"));
                ps.setString(6, rs.getString("change_name"));
                ps.setTimestamp(7, rs.getTimestamp("change_time"));
                ps.setString(8, rs.getString("change_branch"));
                ps.setString(9, rs.getString("current_step"));
                ps.setString(10, rs.getString("current_status"));
                ps.setTimestamp(11, rs.getTimestamp("schedule_time"));
                ps.setInt(12, rs.getInt("version"));
                ps.setString(13, rs.getString("remark"));
                ps.setInt(14, rs.getInt("try_times"));
                ps.setString(15, rs.getString("last_commit_id"));
                ps.setInt(16, bool(rs, "lock_status") ? 1 : 0);
                ps.setString(17, rs.getString("flow_walked"));
                ps.executeUpdate();
            }
        }
    }

    private void migrateChangeItems(Map<OwnerIdKey, Long> changeFlowIds) throws SQLException {
        String insertSql = """
                insert into dm_change_item(
                    id, gmt_create, gmt_modified, owner_uid, ref_flow_id, ref_change_id, ref_change_item_type,
                    content_name, content_index, content
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select * from dm_project_change_item order by id");
                PreparedStatement ps = connection.prepareStatement(insertSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                if (existsById("dm_change_item", id)) {
                    continue;
                }
                String ownerUid = rs.getString("owner_uid");
                long changeId = rs.getLong("ref_change_id");
                Long flowId = changeFlowIds.get(new OwnerIdKey(ownerUid, changeId));
                if (flowId == null) {
                    throw new IllegalStateException("Missing dm_project_change for dm_project_change_item.id=" + id + ", ref_change_id=" + changeId);
                }
                ps.setLong(1, id);
                ps.setTimestamp(2, rs.getTimestamp("gmt_create"));
                ps.setTimestamp(3, rs.getTimestamp("gmt_modified"));
                ps.setString(4, ownerUid);
                ps.setLong(5, flowId);
                ps.setLong(6, changeId);
                ps.setString(7, rs.getString("ref_change_item_type"));
                ps.setString(8, rs.getString("content_name"));
                ps.setInt(9, rs.getInt("content_index"));
                ps.setString(10, rs.getString("content"));
                ps.executeUpdate();
            }
        }
    }

    private void migrateChangeVersions() throws SQLException {
        String insertSql = """
                insert into dm_change_version(id, gmt_create, gmt_modified, owner_uid, ref_flow_id, ref_change_id, version, commit_id, content, type)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select * from dm_project_version order by id");
                PreparedStatement ps = connection.prepareStatement(insertSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                if (existsById("dm_change_version", id)) {
                    continue;
                }
                ps.setLong(1, id);
                ps.setTimestamp(2, rs.getTimestamp("gmt_create"));
                ps.setTimestamp(3, rs.getTimestamp("gmt_modified"));
                ps.setString(4, rs.getString("owner_uid"));
                ps.setLong(5, rs.getLong("ref_devops_id"));
                ps.setLong(6, rs.getLong("ref_change_id"));
                ps.setTimestamp(7, rs.getTimestamp("version"));
                ps.setString(8, rs.getString("commit_id"));
                ps.setString(9, rs.getString("content"));
                ps.setString(10, rs.getString("type"));
                ps.executeUpdate();
            }
        }
    }

    private void validateMigration() throws SQLException {
        assertSameCount("dm_git_ops_scm", "dm_project_scm");
        assertSameCount("dm_change_flow", "dm_project_devops");
        assertSameCount("dm_change_flow_item", "dm_project_devops_item");
        assertSameCount("dm_change", "dm_project_change");
        assertSameCount("dm_change_item", "dm_project_change_item");
        assertSameCount("dm_change_version", "dm_project_version");
        assertZero("""
                select count(*)
                from dm_project_change old_change
                left join dm_change_flow flow on flow.owner_uid = old_change.owner_uid and flow.id = old_change.ref_devops_id
                where flow.id is null
                """, "Missing migrated change flow for old changes");
        assertZero("""
                select count(*)
                from dm_project_change_item old_item
                left join dm_change new_change on new_change.owner_uid = old_item.owner_uid and new_change.id = old_item.ref_change_id
                where new_change.id is null
                """, "Missing migrated change for old change items");
    }

    private void migrateRoleAuthLabels() throws SQLException {
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select id, role_auth_labels from dm_auth_role");
                PreparedStatement ps = connection.prepareStatement("update dm_auth_role set role_auth_labels = ? where id = ?")) {
            while (rs.next()) {
                String oldLabels = rs.getString("role_auth_labels");
                String newLabels = replaceRoleAuthLabels(oldLabels);
                if (Objects.equals(oldLabels, newLabels)) {
                    continue;
                }
                ps.setString(1, newLabels);
                ps.setLong(2, rs.getLong("id"));
                ps.executeUpdate();
            }
        }
    }

    private void resetAutoIncrement() throws SQLException {
        resetAutoIncrement("dm_git_ops_scm");
        resetAutoIncrement("dm_change_flow");
        resetAutoIncrement("dm_change_flow_item");
        resetAutoIncrement("dm_change");
        resetAutoIncrement("dm_change_item");
        resetAutoIncrement("dm_change_version");
    }

    private void migrateUserConfigs() throws SQLException {
        migrateUserConfig("defaultProjectSpace", "defaultCicdWorkspace", "CICD_DEFAULT_WORKSPACE");
        migrateUserConfig("defaultTempSpace", "defaultCicdTempSpace", "CICD_DEFAULT_TEMP_SPACE");
        migrateUserConfig("scmMaxFailedTimes", "cicdMaxFailedTimes", "CICD_SCM_MAX_FAILED_TIMES");
    }

    private void migrateUserConfig(String oldName, String newName, String newDescKey) throws SQLException {
        String selectSql = """
                select gmt_create, gmt_modified, uid, config_value, default_value, value_range, read_only, conf_belong, is_secret
                from dm_sys_user_conf
                where config_name = ?
                """;
        String insertSql = """
                insert into dm_sys_user_conf(
                    gmt_create, gmt_modified, uid, config_name, config_value, default_value,
                    value_range, read_only, user_config_tag_type, conf_belong, is_secret, desc_key
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        String updateSql = """
                update dm_sys_user_conf
                set gmt_modified         = ?,
                    config_value         = ?,
                    default_value        = ?,
                    value_range          = ?,
                    read_only            = ?,
                    user_config_tag_type = ?,
                    conf_belong          = ?,
                    is_secret            = ?,
                    desc_key             = ?
                where uid = ?
                  and config_name = ?
                """;
        String deleteSql = "delete from dm_sys_user_conf where config_name = ?";
        try (PreparedStatement select = connection.prepareStatement(selectSql);
                PreparedStatement insert = connection.prepareStatement(insertSql);
                PreparedStatement update = connection.prepareStatement(updateSql);
                PreparedStatement delete = connection.prepareStatement(deleteSql)) {
            select.setString(1, oldName);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) {
                    String uid = rs.getString("uid");
                    update.setTimestamp(1, rs.getTimestamp("gmt_modified"));
                    update.setString(2, rs.getString("config_value"));
                    update.setString(3, rs.getString("default_value"));
                    update.setString(4, rs.getString("value_range"));
                    update.setInt(5, bool(rs, "read_only") ? 1 : 0);
                    update.setString(6, "CICD");
                    update.setString(7, rs.getString("conf_belong"));
                    update.setInt(8, bool(rs, "is_secret") ? 1 : 0);
                    update.setString(9, newDescKey);
                    update.setString(10, uid);
                    update.setString(11, newName);
                    if (update.executeUpdate() > 0) {
                        continue;
                    }

                    insert.setTimestamp(1, rs.getTimestamp("gmt_create"));
                    insert.setTimestamp(2, rs.getTimestamp("gmt_modified"));
                    insert.setString(3, uid);
                    insert.setString(4, newName);
                    insert.setString(5, rs.getString("config_value"));
                    insert.setString(6, rs.getString("default_value"));
                    insert.setString(7, rs.getString("value_range"));
                    insert.setInt(8, bool(rs, "read_only") ? 1 : 0);
                    insert.setString(9, "CICD");
                    insert.setString(10, rs.getString("conf_belong"));
                    insert.setInt(11, bool(rs, "is_secret") ? 1 : 0);
                    insert.setString(12, newDescKey);
                    insert.executeUpdate();
                }
            }
            delete.setString(1, oldName);
            delete.executeUpdate();
        }
    }

    private Map<LegacyProjectKey, LegacyProjectRow> loadLegacyProjects() throws SQLException {
        Map<LegacyProjectKey, LegacyProjectRow> legacyProjects = new HashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("""
                select id, owner_uid, project_uid, project_name, project_desc, project_status, flow_check, flow_approve, flow_execute, options
                from dm_project
                """)) {
            while (rs.next()) {
                LegacyProjectRow legacyProject = new LegacyProjectRow(rs.getString("owner_uid"),
                    rs.getLong("id"),
                    rs.getString("project_uid"),
                    rs.getString("project_name"),
                    rs.getString("project_desc"),
                    rs.getString("project_status"),
                    rs.getString("flow_check"),
                    rs.getString("flow_approve"),
                    rs.getString("flow_execute"),
                    rs.getString("options"));
                legacyProjects.put(new LegacyProjectKey(legacyProject.ownerUid, legacyProject.id), legacyProject);
            }
        }
        return legacyProjects;
    }

    private Map<LegacyProjectKey, LegacyProjectMsgRow> loadLegacyProjectMessages() throws SQLException {
        Map<LegacyProjectKey, LegacyProjectMsgRow> legacyMessages = new HashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("""
                select owner_uid, ref_project_id, ref_msg_id, ref_msg_type, language, enable,
                       event_project_status, event_project_config, event_change_life, event_change_notice
                from dm_project_msg
                """)) {
            while (rs.next()) {
                LegacyProjectMsgRow legacyMessage = new LegacyProjectMsgRow(rs.getString("owner_uid"),
                    rs.getLong("ref_project_id"),
                    nullableLong(rs, "ref_msg_id"),
                    rs.getString("ref_msg_type"),
                    rs.getString("language"),
                    bool(rs, "enable"),
                    bool(rs, "event_project_status"),
                    bool(rs, "event_project_config"),
                    bool(rs, "event_change_life"),
                    bool(rs, "event_change_notice"));
                legacyMessages.put(new LegacyProjectKey(legacyMessage.ownerUid, legacyMessage.legacyProjectId), legacyMessage);
            }
        }
        return legacyMessages;
    }

    private Map<LegacyProjectKey, Integer> loadDevopsCounts() throws SQLException {
        Map<LegacyProjectKey, Integer> counts = new HashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("""
                select owner_uid, ref_project_id, count(*) as devops_count
                from dm_project_devops
                group by owner_uid, ref_project_id
                """)) {
            while (rs.next()) {
                counts.put(new LegacyProjectKey(rs.getString("owner_uid"), rs.getLong("ref_project_id")), rs.getInt("devops_count"));
            }
        }
        return counts;
    }

    private Map<LegacyProjectNameKey, Integer> loadLegacyProjectNameCounts() throws SQLException {
        Map<LegacyProjectNameKey, Integer> counts = new HashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("""
                select owner_uid, project_name, count(*) as name_count
                from dm_project
                group by owner_uid, project_name
                """)) {
            while (rs.next()) {
                counts.put(new LegacyProjectNameKey(rs.getString("owner_uid"), rs.getString("project_name")), rs.getInt("name_count"));
            }
        }
        return counts;
    }

    private Map<OwnerIdKey, Long> loadChangeFlowIds() throws SQLException {
        Map<OwnerIdKey, Long> changeFlowIds = new HashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("select owner_uid, id, ref_devops_id from dm_project_change")) {
            while (rs.next()) {
                changeFlowIds.put(new OwnerIdKey(rs.getString("owner_uid"), rs.getLong("id")), rs.getLong("ref_devops_id"));
            }
        }
        return changeFlowIds;
    }

    private String buildFlowName(LegacyProjectRow legacyProject, ResultSet devops, Map<LegacyProjectKey, Integer> devopsCounts,
                                 Map<LegacyProjectNameKey, Integer> legacyProjectNameCounts) throws SQLException {
        int devopsCount = devopsCounts.getOrDefault(new LegacyProjectKey(legacyProject.ownerUid, legacyProject.id), 0);
        int nameCount = legacyProjectNameCounts.getOrDefault(new LegacyProjectNameKey(legacyProject.ownerUid, legacyProject.legacyProjectName), 0);
        if (nameCount == 1 && devopsCount == 1) {
            return legacyProject.legacyProjectName;
        }
        if (devopsCount == 1) {
            return legacyProject.legacyProjectName + "-" + legacyProject.id;
        }
        return legacyProject.legacyProjectName + "-" + devops.getString("scm_repo_name") + "-" + devops.getString("scm_repo_branch") + "-" + devops.getString("scm_repo_script")
               + "-" + devops.getLong("id");
    }

    private String replaceRoleAuthLabels(String labels) {
        if (labels == null) {
            return null;
        }
        return labels.replace("\"DM_PROJECT_OPERATE\"", "\"DM_CICD_FLOW_OPERATE\"")
            .replace("\"DM_PROJECT_MANAGE\"", "\"DM_CICD_FLOW_MANAGE\"")
            .replace("\"DM_PROJECT_READ\"", "\"DM_CICD_FLOW_READ\"")
            .replace("\"DM_CICD_MANAGE\"", "\"DM_GIT_OPS_MANAGE\"")
            .replace("\"DM_CICD_READ\"", "\"DM_GIT_OPS_READ\"");
    }

    private void assertSameCount(String newTable, String oldTable) throws SQLException {
        long newCount = count(newTable);
        long oldCount = count(oldTable);
        if (newCount != oldCount) {
            throw new IllegalStateException("Migration count mismatch, " + newTable + "=" + newCount + ", " + oldTable + "=" + oldCount);
        }
    }

    private void assertZero(String sql, String message) throws SQLException {
        long count = queryLong(sql);
        if (count != 0) {
            throw new IllegalStateException(message + ", count=" + count);
        }
    }

    private long count(String table) throws SQLException {
        return queryLong("select count(*) from " + table);
    }

    private long queryLong(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.next()) {
                return 0;
            }
            return rs.getLong(1);
        }
    }

    private boolean existsById(String table, long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("select 1 from " + table + " where id = ? limit 1")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void resetAutoIncrement(String table) throws SQLException {
        long nextId = maxId(table) + 1;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("alter table " + table + " auto_increment = " + nextId);
        }
    }

    private long maxId(String table) throws SQLException {
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("select max(id) from " + table)) {
            if (!rs.next()) {
                return 0;
            }
            long value = rs.getLong(1);
            return rs.wasNull() ? 0 : value;
        }
    }

    private static String defaultString(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    private static boolean bool(ResultSet rs, String column) throws SQLException {
        return rs.getInt(column) != 0;
    }

    private static Long nullableLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

    private static void setNullableLong(PreparedStatement ps, int index, Long value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, value);
        }
    }

    private record LegacyProjectKey(String ownerUid, long legacyProjectId) {
    }

    private record LegacyProjectNameKey(String ownerUid, String legacyProjectName) {
    }

    private record OwnerIdKey(String ownerUid, long id) {
    }

    private record LegacyProjectRow(String ownerUid, long id, String flowManagerUid, String legacyProjectName, String flowDesc, String legacyProjectStatus, String flowCheck,
                                    String flowApprove, String flowExecute, String options) {
    }

    private record LegacyProjectMsgRow(String ownerUid, long legacyProjectId, Long refMsgId, String refMsgType, String language, boolean enable, boolean eventFlowStatus,
                                       boolean eventFlowConfig, boolean eventChangeLife, boolean eventChangeNotice) {
    }
}
