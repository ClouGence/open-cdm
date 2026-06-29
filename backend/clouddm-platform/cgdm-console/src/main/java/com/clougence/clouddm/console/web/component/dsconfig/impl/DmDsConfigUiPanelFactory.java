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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.base.metadata.ui.form.*;
import com.clougence.clouddm.base.metadata.ui.form.value.FieldOptionValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.MapValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.utils.StringUtils;

public class DmDsConfigUiPanelFactory {

    public List<UiPanel> create(DataSourceType dsType, Map<DsConfigGroup, Map<String, DsConfigKvDef>> fieldsByGroup) {
        Map<DsConfigGroup, UiPanel> panels = new EnumMap<>(DsConfigGroup.class);
        UiPanel general = generalPanel(dsType, fieldsByGroup.get(DsConfigGroup.GENERAL));
        otherFields(general, DsConfigGroup.GENERAL, fieldsByGroup.get(DsConfigGroup.GENERAL));
        panels.put(DsConfigGroup.GENERAL, general);

        UiPanel options = optionsPanel(dsType, fieldsByGroup.get(DsConfigGroup.OPTIONS));
        otherFields(options, DsConfigGroup.OPTIONS, fieldsByGroup.get(DsConfigGroup.OPTIONS));
        panels.put(DsConfigGroup.OPTIONS, options);

        Map<String, DsConfigKvDef> sshSslFields = new LinkedHashMap<>(safeFields(fieldsByGroup.get(DsConfigGroup.SSH_SSL)));
        UiPanel sshSsl = sshSslPanel(dsType, sshSslFields);
        otherFields(sshSsl, DsConfigGroup.SSH_SSL, sshSslFields);
        panels.put(DsConfigGroup.SSH_SSL, sshSsl);

        UiPanel advanced = advancedPanel(dsType, fieldsByGroup.get(DsConfigGroup.ADVANCED));
        otherFields(advanced, DsConfigGroup.ADVANCED, fieldsByGroup.get(DsConfigGroup.ADVANCED));
        panels.put(DsConfigGroup.ADVANCED, advanced);

        UiPanel shadow = shadowPanel(dsType, fieldsByGroup.get(DsConfigGroup.SHADOW));
        otherFields(shadow, DsConfigGroup.SHADOW, fieldsByGroup.get(DsConfigGroup.SHADOW));
        panels.put(DsConfigGroup.SHADOW, shadow);

        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        if (configSpi != null) {
            configSpi.customizePanels(panels);
        }
        for (UiPanel panel : panels.values()) {
            panel.initI18n(PluginManager.findDsI18nUtil(dsType));
        }
        return new ArrayList<>(panels.values());
    }

    protected String dsConfigGroupTitle(DsConfigGroup group) {
        return switch (group) {
            case GENERAL -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_GENERAL;
            case OPTIONS -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_OPTIONS;
            case SSH_SSL -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_SSH_SSL;
            case ADVANCED -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_ADVANCED;
            case SHADOW -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_SHADOW;
        };
    }

    protected void otherFields(UiPanel panel, DsConfigGroup group, Map<String, DsConfigKvDef> fields) {
        Set<String> definedFields = new HashSet<>();
        collectDefinedFields(panel.getChildren(), definedFields);
        for (DsConfigKvDef configDef : safeFields(fields).values()) {
            if (definedFields.contains(configDef.getConfigName())) {
                continue;
            }
            panel.addField(createField(group, configDef));
        }
    }

    protected void collectDefinedFields(List<UiPanelField> fields, Set<String> definedFields) {
        if (fields == null || fields.isEmpty()) {
            return;
        }
        for (UiPanelField field : fields) {
            if (field == null) {
                continue;
            }
            if (!StringUtils.isBlank(field.getField())) {
                definedFields.add(field.getField());
            }
            collectDefinedFields(field.getChildren(), definedFields);
            if (field.getOptions() == null) {
                continue;
            }
            for (ValueDef option : field.getOptions()) {
                if (option instanceof FieldOptionValueDef optionDef) {
                    collectDefinedFields(optionDef.getChildren(), definedFields);
                }
            }
        }
    }

    //

    protected UiPanel generalPanel(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        UiPanel panel = newPanel(DsConfigGroup.GENERAL);
        fields = safeFields(fields);

        // environment & cluster
        panel.addField(simpleField(ENV_ID_FIELD, UiPanelFieldType.EnvironmentSelect, ConfigI18nKey.CONFIG_ADD_DS_ENV_LABEL));
        panel.addField(simpleField(CLUSTER_ID_FIELD, UiPanelFieldType.ClusterSelect, ConfigI18nKey.CONFIG_ADD_DS_CLUSTER_LABEL));

        // driver
        DsConfigKvDef driverVersion = fields.get(DataSourceConfig.Fields.driverVersion);
        UiPanelField driverField = createField(DsConfigGroup.GENERAL, driverVersion);
        driverField.setType(UiPanelFieldType.DriverSelection);
        panel.addField(driverField);

        // host&port
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        DsConfigKvDef host = fields.get(DataSourceConfig.Fields.host);
        String[] hostParts = splitHostPort(host.getConfigValue(), configSpi.defaultPort());
        panel.addField(UiPanelField.builder()
            .field(host.getConfigName())
            .type(UiPanelFieldType.NetworkAddress)
            .require(host.isValueRequire())
            .readOnly(host.isReadOnly())
            .defaultValue(UiUtils.strValueDef(host.getConfigValue()))
            .activeExpr(activeExpr(host))
            .titleI18N(host.getLabelKey())
            .descI18N(host.getDescKey())
            .build()
            .addField(UiPanelField.builder().field(ADDRESS_FIELD).hide(true).defaultValue(UiUtils.strValueDef(hostParts[0])).build())
            .addField(UiPanelField.builder().field(PORT_FIELD).hide(true).defaultValue(UiUtils.strValueDef(hostParts[1])).build()));

        // security
        DsConfigKvDef secTypeDef = fields.get(DataSourceConfig.Fields.securityType);
        List<SecurityType> secTypes = configSpi.securityTypes();
        if (secTypes == null) {
            secTypes = List.of();
        }
        UiPanelField secTypeField = createField(DsConfigGroup.GENERAL, secTypeDef);
        secTypeField.setType(UiPanelFieldType.Options);

        List<ValueDef> options = new ArrayList<>();
        for (SecurityType type : secTypes) {
            ValueDef def = buildSecOption(type, fields);
            if (def != null) {
                options.add(def);
            }
        }
        fields.remove(DataSourceConfig.Fields.userName);
        fields.remove(DataSourceConfig.Fields.password);
        secTypeField.setOptions(options);
        if (!secTypes.isEmpty()) {
            String securityType = StringUtils.isBlank(secTypeDef.getConfigValue()) ? defaultSecurityType(secTypes).name() : secTypeDef.getConfigValue();
            secTypeField.setDefaultValue(UiUtils.strValueDef(securityType));
        }
        panel.addField(secTypeField);

        //
        return panel;
    }

    private SecurityType defaultSecurityType(List<SecurityType> secTypes) {
        for (SecurityType type : List.of(SecurityType.USER_PASSWD, SecurityType.ONLY_USER, SecurityType.ONLY_PASSWD, SecurityType.API_KEY, SecurityType.AK_SK, SecurityType.NONE)) {
            if (secTypes.contains(type)) {
                return type;
            }
        }
        return secTypes.get(0);
    }

    private String[] splitHostPort(String host, String defaultPort) {
        if (StringUtils.isBlank(host)) {
            return new String[] { "", defaultPort };
        }
        int index = host.lastIndexOf(':');
        if (host.contains("://") || index <= 0 || index == host.length() - 1 || host.indexOf(':') != index) {
            return new String[] { host, defaultPort };
        }
        return new String[] { host.substring(0, index), host.substring(index + 1) };
    }

    private ValueDef buildSecOption(SecurityType type, Map<String, DsConfigKvDef> allFields) {
        DsConfigKvDef userName = allFields.get(DataSourceConfig.Fields.userName);
        DsConfigKvDef password = allFields.get(DataSourceConfig.Fields.password);
        return switch (type) {
            case NONE -> fieldOptionDef(SecurityType.NONE.getI18nKey(), SecurityType.NONE.name());
            case ONLY_USER -> fieldOptionDef(SecurityType.ONLY_USER.getI18nKey(), SecurityType.ONLY_USER.name())//
                .addField(createField(DsConfigGroup.GENERAL, userName));
            case ONLY_PASSWD -> fieldOptionDef(SecurityType.ONLY_PASSWD.getI18nKey(), SecurityType.ONLY_PASSWD.name())//
                .addField(createField(DsConfigGroup.GENERAL, password));
            case USER_PASSWD -> fieldOptionDef(SecurityType.USER_PASSWD.getI18nKey(), SecurityType.USER_PASSWD.name())//
                .addField(createField(DsConfigGroup.GENERAL, userName))
                .addField(createField(DsConfigGroup.GENERAL, password));
            case API_KEY -> fieldOptionDef(SecurityType.API_KEY.getI18nKey(), SecurityType.API_KEY.name())//
                .addField(secField(password, ConfigI18nKey.CONFIG_ADD_DS_API_KEY_LABEL));
            case AK_SK -> fieldOptionDef(SecurityType.AK_SK.getI18nKey(), SecurityType.AK_SK.name())//
                .addField(secField(userName, ConfigI18nKey.CONFIG_ADD_DS_ACCESS_KEY_LABEL))
                .addField(secField(password, ConfigI18nKey.CONFIG_ADD_DS_SECRET_KEY_LABEL));
            default -> null;
        };
    }

    private UiPanelField secField(DsConfigKvDef configDef, String labelKey) {
        UiPanelField field = createField(DsConfigGroup.GENERAL, configDef);
        field.setTitleI18N(labelKey);
        return field;
    }

    //

    protected UiPanel optionsPanel(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        UiPanel panel = newPanel(DsConfigGroup.OPTIONS);
        fields = safeFields(fields);

        // readOnly
        DsConfigKvDef readOnly = fields.get(DataSourceConfig.Fields.readOnly);
        if (readOnly != null) {
            panel.addField(createField(DsConfigGroup.OPTIONS, readOnly));
        }

        // tx
        UiPanelField txField = txField(dsType, fields);
        if (txField != null) {
            panel.addField(txField);
        }

        // timeZone
        DsConfigKvDef clientTimeZone = fields.get(CLIENT_TIME_ZONE_FIELD);
        if (clientTimeZone != null) {
            panel.addField(timeZoneField(clientTimeZone));
        }

        return panel;
    }

    protected UiPanelField txField(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        DsConfigKvDef autoCommit = fields.get(DataSourceConfig.Fields.autoCommit);
        DsConfigKvDef isolation = fields.get(DataSourceConfig.Fields.isolation);
        fields.remove(DataSourceConfig.Fields.autoCommit);
        fields.remove(DataSourceConfig.Fields.isolation);
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        if (configSpi == null || !configSpi.supportTx()) {
            return null;
        }

        Map<String, Object> defaultValues = new LinkedHashMap<>();
        defaultValues.put(DataSourceConfig.Fields.autoCommit, autoCommit == null ? "true" : configValue(autoCommit, "true"));
        defaultValues.put(DataSourceConfig.Fields.isolation, isolation == null ? "DEFAULT" : configValue(isolation, "DEFAULT"));

        List<ValueDef> values = new ArrayList<>();
        // always DEFAULT as the first option
        values.add(UiUtils.fieldOptionDef(I18nDmLabelKeys.RDB_ISOLATION_DEFAULT.name(), RdbIsolation.DEFAULT.getName()));
        RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsType);
        if (supportSpi != null && supportSpi.supportIsolation() != null) {
            for (RdbIsolation isolationValue : supportSpi.supportIsolation()) {
                if (isolationValue == RdbIsolation.DEFAULT) {
                    continue;
                }
                I18nDmLabelKeys i18nKey = I18nDmLabelKeys.valueOf("RDB_ISOLATION_" + isolationValue.getName());
                values.add(UiUtils.fieldOptionDef(i18nKey.name(), isolationValue.getName()));
            }
        }

        return UiPanelField.builder()
            .field(TRANSACTION_CONTROL_FIELD)
            .type(UiPanelFieldType.TransactionControl)
            .titleI18N(autoCommit.getLabelKey())
            .descI18N(autoCommit.getDescKey())
            .defaultValue(MapValueDef.builder().data(defaultValues).build())
            .options(values)
            .build();
    }

    protected UiPanelField timeZoneField(DsConfigKvDef clientTimeZone) {
        UiPanelField field = createField(DsConfigGroup.OPTIONS, clientTimeZone);
        Instant now = Instant.now();
        List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        zoneIds.sort(Comparator.comparingInt((String zoneId) -> {
            return ZoneId.of(zoneId).getRules().getOffset(now).getTotalSeconds();
        }).thenComparing(zoneId -> zoneId));

        List<ValueDef> values = new ArrayList<>();
        for (String zoneId : zoneIds) {
            ZoneOffset offset = ZoneId.of(zoneId).getRules().getOffset(now);
            int totalSeconds = offset.getTotalSeconds();
            String sign = totalSeconds < 0 ? "-" : "+";
            int absSeconds = Math.abs(totalSeconds);
            int hours = absSeconds / 3600;
            int minutes = (absSeconds % 3600) / 60;
            String offsetText = minutes == 0 ? sign + hours : String.format("%s%d:%02d", sign, hours, minutes);
            values.add(UiUtils.fieldOptionDef("(" + offsetText + ") " + zoneId, zoneId));
        }

        field.setType(UiPanelFieldType.Options);
        field.setDefaultValue(UiUtils.strValueDef(configValue(clientTimeZone, ZoneId.systemDefault().getId())));
        field.setOptions(values);
        return field;
    }

    private String configValue(DsConfigKvDef configDef, String defaultValue) {
        if (configDef == null || StringUtils.isBlank(configDef.getConfigValue())) {
            return defaultValue;
        }
        return configDef.getConfigValue();
    }

    protected UiPanel sshSslPanel(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        UiPanel panel = newPanel(DsConfigGroup.SSH_SSL);
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);

        // SSH
        boolean supportSSH = configSpi != null && configSpi.supportSSH();
        DsConfigKvDef sshEnableField = fields.get(DataSourceConfig.Fields.sshProxyEnabled);
        DsConfigKvDef sshConfigField = fields.get(DataSourceConfig.Fields.sshConfigId);
        fields.remove(DataSourceConfig.Fields.sshProxyEnabled);
        fields.remove(DataSourceConfig.Fields.sshConfigId);
        if (supportSSH) {
            panel.addField(UiPanelField.builder()
                .field(SSH_TUNNEL_FIELD)
                .type(UiPanelFieldType.SshTunnel)
                .titleI18N(sshEnableField.getLabelKey())
                .descI18N(sshEnableField.getDescKey())
                .build()
                .addField(hiddenField(DsConfigGroup.SSH_SSL, sshEnableField))
                .addField(hiddenField(DsConfigGroup.SSH_SSL, sshConfigField)));
        }

        // SSL
        boolean supportSSL = configSpi != null && configSpi.supportSSL();
        DsConfigKvDef sslModeField = fields.get(DataSourceConfig.Fields.sslMode);
        DsConfigKvDef sslCaDataField = fields.get(DataSourceConfig.Fields.sslCaData);
        DsConfigKvDef sslClientCertDataField = fields.get(DataSourceConfig.Fields.sslClientCertData);
        DsConfigKvDef sslClientKeyDataField = fields.get(DataSourceConfig.Fields.sslClientKeyData);
        DsConfigKvDef sslClientKeyPasswordField = fields.get(DataSourceConfig.Fields.sslClientKeyPassword);
        fields.remove(DataSourceConfig.Fields.sslMode);
        fields.remove(DataSourceConfig.Fields.sslCaData);
        fields.remove(DataSourceConfig.Fields.sslClientCertData);
        fields.remove(DataSourceConfig.Fields.sslClientKeyData);
        fields.remove(DataSourceConfig.Fields.sslClientKeyPassword);
        if (supportSSL) {
            List<ValueDef> values = new ArrayList<>();
            UiPanelField field = createField(DsConfigGroup.SSH_SSL, sslModeField);
            field.setType(UiPanelFieldType.Options);
            field.setOptions(values);
            panel.addField(field);

            Set<SslMode> sslModeSet = new LinkedHashSet<>();
            sslModeSet.add(SslMode.DISABLED);
            sslModeSet.addAll(configSpi.sslModeSet());
            for (SslMode mode : sslModeSet) {
                String key = switch (mode) {
                    case DISABLED -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_DISABLED;
                    case TRUST -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_TRUST;
                    case CA -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_CA;
                    case CLIENT_CERT -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_CLIENT_CERT;
                };
                FieldOptionValueDef option = UiUtils.fieldOptionDef(key, mode.name());
                switch (mode) {
                    case DISABLED:
                    case TRUST:
                        break;
                    case CA:
                        option.addField(createField(DsConfigGroup.SSH_SSL, sslCaDataField));
                        break;
                    case CLIENT_CERT:
                        option.addField(createField(DsConfigGroup.SSH_SSL, sslCaDataField));
                        option.addField(createField(DsConfigGroup.SSH_SSL, sslClientCertDataField));
                        option.addField(createField(DsConfigGroup.SSH_SSL, sslClientKeyDataField));
                        option.addField(createField(DsConfigGroup.SSH_SSL, sslClientKeyPasswordField));
                        break;
                }
                values.add(option);
            }
        }
        return panel;
    }

    //

    protected UiPanel advancedPanel(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        return newPanel(DsConfigGroup.ADVANCED);
    }

    protected UiPanel shadowPanel(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        return newPanel(DsConfigGroup.SHADOW);
    }

    // Utils

    protected UiPanel newPanel(DsConfigGroup group) {
        UiPanel panel = new UiPanel();
        panel.setKey(group.name());
        panel.setTitleI18N(dsConfigGroupTitle(group));
        panel.setDescI18N(dsConfigGroupTitle(group));
        return panel;
    }

    protected UiPanelField createField(DsConfigGroup group, DsConfigKvDef configDef) {
        UiPanelField.UiPanelFieldBuilder builder = UiPanelField.builder()
            .field(configDef.getConfigName())
            .type(fieldType(configDef))
            .require(configDef.isValueRequire())
            .readOnly(configDef.isReadOnly())
            .hide(group == DsConfigGroup.SHADOW)
            .defaultValue(UiUtils.strValueDef(configDef.getConfigValue()))
            .activeExpr(activeExpr(configDef))
            .titleI18N(configDef.getLabelKey())
            .descI18N(configDef.getDescKey());

        return builder.build();
    }

    protected UiPanelField hiddenField(DsConfigGroup group, DsConfigKvDef configDef) {
        UiPanelField field = createField(group, configDef);
        field.setType(UiPanelFieldType.Input);
        field.setHide(true);
        return field;
    }

    protected UiPanelField simpleField(String field, UiPanelFieldType type, String labelKey) {
        return UiPanelField.builder()//
            .field(field)
            .type(type)
            .require(false)
            .titleI18N(labelKey)
            .descI18N("")
            .build();
    }

    protected UiPanelFieldType fieldType(DsConfigKvDef configDef) {
        if (StringUtils.equals(configDef.getConfigName(), DataSourceConfig.Fields.sslCaData)
            || StringUtils.equals(configDef.getConfigName(), DataSourceConfig.Fields.sslClientCertData)
            || StringUtils.equals(configDef.getConfigName(), DataSourceConfig.Fields.sslClientKeyData)) {
            return UiPanelFieldType.CertificateInput;
        }
        if (StringUtils.equals(configDef.getConfigName(), DataSourceConfig.Fields.sslClientKeyPassword)) {
            return UiPanelFieldType.Password;
        }
        if (configDef.getConfValType() == ConfigValType.BOOLEAN) {
            return UiPanelFieldType.Check;
        }
        if (configDef.getConfValType() == ConfigValType.JSON) {
            return UiPanelFieldType.TextArea;
        }
        return UiPanelFieldType.Input;
    }

    protected UiActiveExpr activeExpr(DsConfigKvDef configDef) {
        if (StringUtils.isBlank(configDef.getActiveField())) {
            return null;
        } else {
            return UiUtils.activeWhenEquals(configDef.getActiveField(), configDef.getActiveEquals());
        }
    }

    private Map<String, DsConfigKvDef> safeFields(Map<String, DsConfigKvDef> fields) {
        return fields == null ? Collections.emptyMap() : fields;
    }
}
