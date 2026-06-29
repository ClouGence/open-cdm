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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SplitAdLdapSsoConfigMigrator {

    private static final String                AUTH_TYPE_KEY      = "accountAuthType";
    private static final String                LDAP_CONFIG_TAG    = "LDAP_CONFIG";
    private static final String                COMMON_CONF_BELONG = "Common";
    private static final String                TEXT_CONF_VAL_TYPE = "TEXT";

    private static final AuthTypeState         EMPTY_AUTH_TYPE    = new AuthTypeState(false, false);

    private static final List<ConfigMapping>   CONFIG_MAPPINGS    = List.of(//
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

    private static final Set<String>           OLD_NAMES          = CONFIG_MAPPINGS.stream().map(ConfigMapping::oldName).collect(Collectors.toUnmodifiableSet());
    private static final Set<String>           NEW_NAMES          = CONFIG_MAPPINGS.stream().map(ConfigMapping::newName).collect(Collectors.toUnmodifiableSet());

    private final Connection                   connection;

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
            UserConfigs configs = loadUserConfigs(uid);
            for (ConfigMapping mapping : CONFIG_MAPPINGS) {
                ConfigRow source = configs.ldapConfigs.get(mapping.oldName);
                boolean adExists = configs.existingAdNames.contains(mapping.newName);
                if (!authType.ldapEnabled && adExists && isLdapValueUntouched(source)) {
                    continue;
                }
                upsertAdConfig(uid, mapping, source);
            }

            if (!authType.ldapEnabled) {
                resetLdapConfigs(uid, configs.ldapConfigs);
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
                    AuthTypeState existing = authTypeByUid.getOrDefault(uid, EMPTY_AUTH_TYPE);
                    AuthTypeState current = parseAuthType(rs.getString("config_value"));
                    authTypeByUid.put(uid, existing.merge(current));
                }
            }
        }
        return authTypeByUid;
    }

    private AuthTypeState parseAuthType(String value) {
        if (value == null || value.isBlank()) {
            return EMPTY_AUTH_TYPE;
        }

        boolean ldapEnabled = false;
        boolean adEnabled = false;
        for (String item : value.split("[,，;；]")) {
            String type = item.trim();
            if ("AD".equalsIgnoreCase(type)) {
                adEnabled = true;
            } else if ("LDAP".equalsIgnoreCase(type)) {
                ldapEnabled = true;
            }
        }
        return new AuthTypeState(ldapEnabled, adEnabled);
    }

    private UserConfigs loadUserConfigs(String uid) throws SQLException {
        Map<String, ConfigRow> ldapConfigs = new HashMap<>();
        Set<String> existingAdNames = new HashSet<>();
        try (PreparedStatement ps = connection.prepareStatement("""
                select gmt_create, gmt_modified, uid, config_name, config_value, default_value,
                       value_range, read_only, user_config_tag_type, conf_val_type, conf_belong, is_secret, desc_key
                from dm_sys_user_conf
                where uid = ?
                """)) {
            ps.setString(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String configName = rs.getString("config_name");
                    if (OLD_NAMES.contains(configName)) {
                        ldapConfigs.put(configName, ConfigRow.from(rs));
                    } else if (NEW_NAMES.contains(configName)) {
                        existingAdNames.add(configName);
                    }
                }
            }
        }
        return new UserConfigs(ldapConfigs, existingAdNames);
    }

    private boolean isLdapValueUntouched(ConfigRow source) {
        return source == null || Objects.equals(source.configValue, source.defaultValue);
    }

    private void upsertAdConfig(String uid, ConfigMapping mapping, ConfigRow source) throws SQLException {
        ConfigRow target = ConfigRow.forAd(uid, mapping, source);
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
        try (PreparedStatement ps = connection.prepareStatement("""
                update dm_sys_user_conf
                set gmt_modified = ?,
                    config_value = ?
                where uid = ?
                  and config_name = ?
                """)) {
            Timestamp now = Timestamp.from(Instant.now());
            for (ConfigMapping mapping : CONFIG_MAPPINGS) {
                ConfigRow source = ldapConfigs.get(mapping.oldName);
                if (source == null) {
                    continue;
                }
                ps.setTimestamp(1, now);
                ps.setString(2, source.defaultValue());
                ps.setString(3, uid);
                ps.setString(4, mapping.oldName);
                ps.executeUpdate();
            }
        }
    }

    private void bindUpdate(PreparedStatement ps, ConfigRow row) throws SQLException {
        ps.setTimestamp(1, row.gmtModified());
        ps.setString(2, row.configValue());
        ps.setString(3, row.defaultValue());
        ps.setString(4, row.valueRange());
        ps.setInt(5, row.readOnly());
        ps.setString(6, row.userConfigTagType());
        ps.setString(7, row.confValType());
        ps.setString(8, row.confBelong());
        ps.setInt(9, row.secret());
        ps.setString(10, row.descKey());
    }

    private void bindInsert(PreparedStatement ps, ConfigRow row) throws SQLException {
        ps.setTimestamp(1, row.gmtCreate());
        ps.setTimestamp(2, row.gmtModified());
        ps.setString(3, row.uid());
        ps.setString(4, row.configName());
        ps.setString(5, row.configValue());
        ps.setString(6, row.defaultValue());
        ps.setString(7, row.valueRange());
        ps.setInt(8, row.readOnly());
        ps.setString(9, row.userConfigTagType());
        ps.setString(10, row.confValType());
        ps.setString(11, row.confBelong());
        ps.setInt(12, row.secret());
        ps.setString(13, row.descKey());
    }

    private record ConfigMapping(String oldName, String newName, String descKey, String defaultValue, String valueRange) {
    }

    private record AuthTypeState(boolean ldapEnabled, boolean adEnabled) {

        AuthTypeState merge(AuthTypeState other) {
            return new AuthTypeState(this.ldapEnabled || other.ldapEnabled, this.adEnabled || other.adEnabled);
        }
    }

    private record UserConfigs(Map<String, ConfigRow> ldapConfigs, Set<String> existingAdNames) {
    }

    private record ConfigRow(Timestamp gmtCreate, Timestamp gmtModified, String uid, String configName, String configValue, String defaultValue,
                             String valueRange, int readOnly, String userConfigTagType, String confValType, String confBelong, int secret, String descKey) {

        static ConfigRow from(ResultSet rs) throws SQLException {
            return new ConfigRow(rs.getTimestamp("gmt_create"), rs.getTimestamp("gmt_modified"), rs.getString("uid"), rs.getString("config_name"),
                    rs.getString("config_value"), rs.getString("default_value"), rs.getString("value_range"), rs.getInt("read_only"),
                    rs.getString("user_config_tag_type"), rs.getString("conf_val_type"), rs.getString("conf_belong"), rs.getInt("is_secret"),
                    rs.getString("desc_key"));
        }

        static ConfigRow forAd(String uid, ConfigMapping mapping, ConfigRow source) {
            Timestamp now = Timestamp.from(Instant.now());
            if (source == null) {
                return new ConfigRow(now, now, uid, mapping.newName, null, mapping.defaultValue, mapping.valueRange, 0, LDAP_CONFIG_TAG,
                        TEXT_CONF_VAL_TYPE, COMMON_CONF_BELONG, 0, mapping.descKey);
            }
            return new ConfigRow(source.gmtCreate, now, source.uid, mapping.newName, source.configValue,
                    defaultString(source.defaultValue, mapping.defaultValue), defaultString(source.valueRange, mapping.valueRange), source.readOnly,
                    defaultString(source.userConfigTagType, LDAP_CONFIG_TAG), defaultString(source.confValType, TEXT_CONF_VAL_TYPE),
                    defaultString(source.confBelong, COMMON_CONF_BELONG), source.secret, defaultString(source.descKey, mapping.descKey));
        }

        private static String defaultString(String value, String defaultValue) {
            return value == null ? defaultValue : value;
        }
    }
}
