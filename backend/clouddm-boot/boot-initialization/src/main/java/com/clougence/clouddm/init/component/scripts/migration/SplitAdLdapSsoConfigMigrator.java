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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SplitAdLdapSsoConfigMigrator {

    private static final String AUTH_TYPE_KEY = "accountAuthType";
    private static final String LDAP_CONFIG_TAG = "LDAP_CONFIG";
    private static final String COMMON_CONF_BELONG = "Common";
    private static final String TEXT_CONF_VAL_TYPE = "TEXT";

    private static final List<ConfigMapping> CONFIG_MAPPINGS = List.of(//
        new ConfigMapping("ldapHost", "adHost", "LDAP_CONFIG_HOST", "", ""),//
        new ConfigMapping("ldapPort", "adPort", "LDAP_CONFIG_PORT", "", "LDAP:389 /AD:3268"),//
        new ConfigMapping("ldapNetBIOSRoute", "adNetBIOSRoute", "LDAP_CONFIG_NET_BIOS_ROUTE", "", "Name=IP;Name=IP;"),//
        new ConfigMapping("ldapSoTimeout", "adSoTimeout", "LDAP_CONFIG_SOCKET_TIMEOUT", "3000", ""),//
        new ConfigMapping("ldapBase", "adBase", "LDAP_CONFIG_BASE", "", ""),//
        new ConfigMapping("ldapUser", "adUser", "LDAP_CONFIG_USER", "", ""),//
        new ConfigMapping("ldapPassword", "adPassword", "LDAP_CONFIG_PASSWORD", "", ""),//
        new ConfigMapping("ldapDomain", "adDomain", "LDAP_CONFIG_DOMAIN", "", ""),//
        new ConfigMapping("ldapRoleMap", "adRoleMap", "LDAP_CONFIG_ROLE_MAP", "Developers", ""),//
        new ConfigMapping("ldapUserObjectClass", "adUserObjectClass", "LDAP_CONFIG_USER_OBJECT_CLASS", "posixAccount,sambaSamAccount", "posixAccount / sambaSamAccount / "),//
        new ConfigMapping("ldapFieldLogin", "adFieldLogin", "LDAP_FIELD_LOGIN", "cn", "cn / userPrincipalName / ..."),//
        new ConfigMapping("ldapFieldUser", "adFieldUser", "LDAP_FIELD_USER", "sn", "sn / displayName / ..."),//
        new ConfigMapping("ldapFieldEmail", "adFieldEmail", "LDAP_FIELD_EMAIL", "mail", "mail / ..."),//
        new ConfigMapping("ldapFieldPhone", "adFieldPhone", "LDAP_FIELD_PHONE", "mobile", "mobile / telephoneNumber / ...")//
    );

    private final Connection connection;

    public SplitAdLdapSsoConfigMigrator(Connection connection){
        this.connection = connection;
    }

    public void migrate() throws SQLException {
        Map<String, AuthTypeState> authTypeByUid = loadAuthTypes();
        for (Map.Entry<String, AuthTypeState> entry : authTypeByUid.entrySet()) {
            AuthTypeState authType = entry.getValue();
            if (!authType.adEnabled) {
                continue;
            }

            String uid = entry.getKey();
            Map<String, ConfigRow> ldapConfigs = loadConfigs(uid, oldConfigNames());
            Map<String, ConfigRow> adConfigs = loadConfigs(uid, newConfigNames());
            for (ConfigMapping mapping : CONFIG_MAPPINGS) {
                ConfigRow source = ldapConfigs.get(mapping.oldName);
                ConfigRow target = adConfigs.get(mapping.newName);
                if (!authType.ldapEnabled && target != null && isResetLdapConfig(source)) {
                    continue;
                }
                upsertAdConfig(uid, mapping, source);
            }

            if (!authType.ldapEnabled) {
                resetLdapConfigs(uid, ldapConfigs);
            }
        }
    }

    private Map<String, AuthTypeState> loadAuthTypes() throws SQLException {
        Map<String, AuthTypeState> authTypeByUid = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                select uid, config_value
                from dm_sys_user_conf
                where config_name = ?
                """)) {
            ps.setString(1, AUTH_TYPE_KEY);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String uid = rs.getString("uid");
                    AuthTypeState existing = authTypeByUid.getOrDefault(uid, new AuthTypeState(false, false));
                    AuthTypeState current = parseAuthType(rs.getString("config_value"));
                    authTypeByUid.put(uid, existing.merge(current));
                }
            }
        }
        return authTypeByUid;
    }

    private AuthTypeState parseAuthType(String value) {
        AuthTypeState state = new AuthTypeState(false, false);
        if (value == null || value.isBlank()) {
            return state;
        }

        for (String item : value.split("[,，;；]")) {
            String type = item.trim();
            if ("AD".equalsIgnoreCase(type)) {
                state = new AuthTypeState(state.ldapEnabled, true);
            } else if ("LDAP".equalsIgnoreCase(type)) {
                state = new AuthTypeState(true, state.adEnabled);
            }
        }
        return state;
    }

    private List<String> oldConfigNames() {
        List<String> names = new ArrayList<>();
        for (ConfigMapping mapping : CONFIG_MAPPINGS) {
            names.add(mapping.oldName);
        }
        return names;
    }

    private List<String> newConfigNames() {
        List<String> names = new ArrayList<>();
        for (ConfigMapping mapping : CONFIG_MAPPINGS) {
            names.add(mapping.newName);
        }
        return names;
    }

    private boolean isResetLdapConfig(ConfigRow source) {
        return source == null || Objects.equals(source.configValue, source.defaultValue);
    }

    private Map<String, ConfigRow> loadConfigs(String uid, List<String> configNames) throws SQLException {
        Map<String, ConfigRow> configs = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                select id, gmt_create, gmt_modified, uid, config_name, config_value, default_value,
                       value_range, read_only, user_config_tag_type, conf_val_type, conf_belong, is_secret, desc_key
                from dm_sys_user_conf
                where uid = ?
                order by id
                """)) {
            ps.setString(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String configName = rs.getString("config_name");
                    if (!configNames.contains(configName)) {
                        continue;
                    }
                    configs.put(configName, ConfigRow.from(rs));
                }
            }
        }
        return configs;
    }

    private void upsertAdConfig(String uid, ConfigMapping mapping, ConfigRow source) throws SQLException {
        ConfigRow target = source == null ? ConfigRow.defaultFor(uid, mapping) : source.toAdConfig(mapping);
        int updated;
        try (PreparedStatement ps = connection.prepareStatement("""
                update dm_sys_user_conf
                set gmt_modified = ?,
                    config_value = ?,
                    default_value = ?,
                    value_range = ?,
                    read_only = ?,
                    user_config_tag_type = ?,
                    conf_val_type = ?,
                    conf_belong = ?,
                    is_secret = ?,
                    desc_key = ?
                where uid = ?
                  and config_name = ?
                """)) {
            bindUpdate(ps, target);
            ps.setString(11, uid);
            ps.setString(12, mapping.newName);
            updated = ps.executeUpdate();
        }
        if (updated > 0) {
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement("""
                insert into dm_sys_user_conf(
                    gmt_create, gmt_modified, uid, config_name, config_value, default_value,
                    value_range, read_only, user_config_tag_type, conf_val_type, conf_belong, is_secret, desc_key
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """)) {
            bindInsert(ps, target);
            ps.executeUpdate();
        }
    }

    private void resetLdapConfigs(String uid, Map<String, ConfigRow> ldapConfigs) throws SQLException {
        for (ConfigMapping mapping : CONFIG_MAPPINGS) {
            ConfigRow source = ldapConfigs.get(mapping.oldName);
            if (source == null) {
                continue;
            }
            source.configValue = source.defaultValue;
            source.gmtModified = Timestamp.from(Instant.now());
            try (PreparedStatement ps = connection.prepareStatement("""
                    update dm_sys_user_conf
                    set gmt_modified = ?,
                        config_value = ?
                    where uid = ?
                      and config_name = ?
                    """)) {
                ps.setTimestamp(1, source.gmtModified);
                ps.setString(2, source.configValue);
                ps.setString(3, uid);
                ps.setString(4, mapping.oldName);
                ps.executeUpdate();
            }
        }
    }

    private void bindUpdate(PreparedStatement ps, ConfigRow row) throws SQLException {
        ps.setTimestamp(1, row.gmtModified);
        ps.setString(2, row.configValue);
        ps.setString(3, row.defaultValue);
        ps.setString(4, row.valueRange);
        ps.setInt(5, row.readOnly);
        ps.setString(6, row.userConfigTagType);
        ps.setString(7, row.confValType);
        ps.setString(8, row.confBelong);
        ps.setInt(9, row.secret);
        ps.setString(10, row.descKey);
    }

    private void bindInsert(PreparedStatement ps, ConfigRow row) throws SQLException {
        ps.setTimestamp(1, row.gmtCreate);
        ps.setTimestamp(2, row.gmtModified);
        ps.setString(3, row.uid);
        ps.setString(4, row.configName);
        ps.setString(5, row.configValue);
        ps.setString(6, row.defaultValue);
        ps.setString(7, row.valueRange);
        ps.setInt(8, row.readOnly);
        ps.setString(9, row.userConfigTagType);
        ps.setString(10, row.confValType);
        ps.setString(11, row.confBelong);
        ps.setInt(12, row.secret);
        ps.setString(13, row.descKey);
    }

    private record ConfigMapping(String oldName, String newName, String descKey, String defaultValue, String valueRange) {
    }

    private record AuthTypeState(boolean ldapEnabled, boolean adEnabled) {

        AuthTypeState merge(AuthTypeState other) {
            return new AuthTypeState(this.ldapEnabled || other.ldapEnabled, this.adEnabled || other.adEnabled);
        }
    }

    private static class ConfigRow {

        private Timestamp gmtCreate;
        private Timestamp gmtModified;
        private String    uid;
        private String    configName;
        private String    configValue;
        private String    defaultValue;
        private String    valueRange;
        private int       readOnly;
        private String    userConfigTagType;
        private String    confValType;
        private String    confBelong;
        private int       secret;
        private String    descKey;

        static ConfigRow from(ResultSet rs) throws SQLException {
            ConfigRow row = new ConfigRow();
            row.gmtCreate = rs.getTimestamp("gmt_create");
            row.gmtModified = rs.getTimestamp("gmt_modified");
            row.uid = rs.getString("uid");
            row.configName = rs.getString("config_name");
            row.configValue = rs.getString("config_value");
            row.defaultValue = rs.getString("default_value");
            row.valueRange = rs.getString("value_range");
            row.readOnly = rs.getInt("read_only");
            row.userConfigTagType = rs.getString("user_config_tag_type");
            row.confValType = rs.getString("conf_val_type");
            row.confBelong = rs.getString("conf_belong");
            row.secret = rs.getInt("is_secret");
            row.descKey = rs.getString("desc_key");
            return row;
        }

        static ConfigRow defaultFor(String uid, ConfigMapping mapping) {
            ConfigRow row = new ConfigRow();
            Timestamp now = Timestamp.from(Instant.now());
            row.gmtCreate = now;
            row.gmtModified = now;
            row.uid = uid;
            row.configName = mapping.newName;
            row.configValue = null;
            row.defaultValue = mapping.defaultValue;
            row.valueRange = mapping.valueRange;
            row.readOnly = 0;
            row.userConfigTagType = LDAP_CONFIG_TAG;
            row.confValType = TEXT_CONF_VAL_TYPE;
            row.confBelong = COMMON_CONF_BELONG;
            row.secret = 0;
            row.descKey = mapping.descKey;
            return row;
        }

        ConfigRow toAdConfig(ConfigMapping mapping) {
            ConfigRow row = new ConfigRow();
            row.gmtCreate = this.gmtCreate;
            row.gmtModified = Timestamp.from(Instant.now());
            row.uid = this.uid;
            row.configName = mapping.newName;
            row.configValue = this.configValue;
            row.defaultValue = defaultString(this.defaultValue, mapping.defaultValue);
            row.valueRange = defaultString(this.valueRange, mapping.valueRange);
            row.readOnly = this.readOnly;
            row.userConfigTagType = defaultString(this.userConfigTagType, LDAP_CONFIG_TAG);
            row.confValType = defaultString(this.confValType, TEXT_CONF_VAL_TYPE);
            row.confBelong = defaultString(this.confBelong, COMMON_CONF_BELONG);
            row.secret = this.secret;
            row.descKey = defaultString(this.descKey, mapping.descKey);
            return row;
        }

        private static String defaultString(String value, String defaultValue) {
            return value == null ? defaultValue : value;
        }
    }
}
