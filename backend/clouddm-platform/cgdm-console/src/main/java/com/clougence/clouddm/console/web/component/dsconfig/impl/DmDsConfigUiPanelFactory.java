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
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.model.vo.DsSecurityOption;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
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
            configSpi.customizeAddPanels(panels);
        }
        for (UiPanel panel : panels.values()) {
            panel.initI18n(PluginManager.findDsI18nUtil(dsType));
        }
        return new ArrayList<>(panels.values());
    }

    protected String dsConfigGroupTitle(DsConfigGroup group) {
        return DmI18nUtils.getMessage(switch (group) {
            case GENERAL -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_GENERAL;
            case OPTIONS -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_OPTIONS;
            case SSH_SSL -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_SSH_SSL;
            case ADVANCED -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_ADVANCED;
            case SHADOW -> ConfigI18nKey.CONFIG_ADD_DS_GROUP_SHADOW;
        });
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
        panel.addField(simpleField(DsConfigSpi.ENV_ID_FIELD, UiPanelFieldType.EnvironmentSelect, ConfigI18nKey.CONFIG_ADD_DS_ENV_LABEL));
        panel.addField(simpleField(DsConfigSpi.CLUSTER_ID_FIELD, UiPanelFieldType.ClusterSelect, ConfigI18nKey.CONFIG_ADD_DS_CLUSTER_LABEL));

        // driver
        DsConfigKvDef driverVersion = fields.get(DataSourceConfig.Fields.driverVersion);
        if (driverVersion != null) {
            UiPanelField field = createField(DsConfigGroup.GENERAL, driverVersion);
            field.setType(UiPanelFieldType.DriverSelection);
            field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_DRIVER_LABEL));
            field.setDescI18N("");
            field.addField(hiddenField(DataSourceConfig.Fields.dataSourceType, dsType == null ? "" : dsType.name()));
            field.addField(hiddenField(DsConfigGroup.GENERAL, driverVersion));
            panel.addField(field);
        }

        // host&port
        DsConfigKvDef host = fields.get(DataSourceConfig.Fields.host);
        if (host != null) {
            UiPanelField field = createField(DsConfigGroup.GENERAL, host);
            field.setType(UiPanelFieldType.NetworkAddress);
            field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_ADDRESS_LABEL));
            field.setDescI18N("");
            field.addField(hiddenField(DsConfigGroup.GENERAL, host));
            field.addField(hiddenField(DsConfigSpi.PORT_FIELD, ""));
            panel.addField(field);
        }

        // security
        List<DsSecurityOption> securityOptions = securityOptions(dsType);
        DsConfigKvDef securityType = fields.get(DataSourceConfig.Fields.securityType);
        DsConfigKvDef userName = fields.get(DataSourceConfig.Fields.userName);
        DsConfigKvDef password = fields.get(DataSourceConfig.Fields.password);

        UiPanelField securityTypeField = createField(DsConfigGroup.GENERAL, securityType);
        securityTypeField.setType(UiPanelFieldType.Options);
        securityTypeField.setDefaultValue(UiUtils.strValueDef(defaultSecurityType(securityOptions)));
        securityTypeField.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SECURITY_TYPE_LABEL));
        securityTypeField.setDescI18N("");
        if (userName != null) {
            securityTypeField.addField(hiddenField(DsConfigGroup.GENERAL, userName));
        }
        if (password != null) {
            securityTypeField.addField(hiddenField(DsConfigGroup.GENERAL, password));
        }
        List<ValueDef> values = new ArrayList<>();
        for (DsSecurityOption securityOption : securityOptions) {
            if (securityOption == null || securityOption.getSecurityType() == null) {
                continue;
            }
            SecurityType type = securityOption.getSecurityType();
            FieldOptionValueDef option = UiUtils.fieldOptionDef(DmI18nUtils.getMessage(type.getI18nKey()), type.name());
            switch (type) {
                case NONE:
                    break;
                case ONLY_USER:
                    if (userName != null) {
                        UiPanelField field = createField(DsConfigGroup.GENERAL, userName);
                        field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_USER_LABEL));
                        field.setDescI18N("");
                        option.addField(field);
                    }
                    break;
                case ONLY_PASSWD:
                    if (password != null) {
                        UiPanelField field = createField(DsConfigGroup.GENERAL, password);
                        field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_PASSWORD_LABEL));
                        field.setDescI18N("");
                        option.addField(field);
                    }
                    break;
                case USER_PASSWD:
                    if (userName != null) {
                        UiPanelField field = createField(DsConfigGroup.GENERAL, userName);
                        field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_USER_LABEL));
                        field.setDescI18N("");
                        option.addField(field);
                    }
                    if (password != null) {
                        UiPanelField field = createField(DsConfigGroup.GENERAL, password);
                        field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_PASSWORD_LABEL));
                        field.setDescI18N("");
                        option.addField(field);
                    }
                    break;
                case API_KEY:
                    if (password != null) {
                        UiPanelField apiKey = createField(DsConfigGroup.GENERAL, password);
                        apiKey.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_API_KEY_LABEL));
                        apiKey.setDescI18N("");
                        option.addField(apiKey);
                    }
                    break;
                case AK_SK:
                    if (userName != null) {
                        UiPanelField accessKey = createField(DsConfigGroup.GENERAL, userName);
                        accessKey.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_ACCESS_KEY_LABEL));
                        accessKey.setDescI18N("");
                        option.addField(accessKey);
                    }
                    if (password != null) {
                        UiPanelField secretAccessKey = createField(DsConfigGroup.GENERAL, password);
                        secretAccessKey.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SECRET_KEY_LABEL));
                        secretAccessKey.setDescI18N("");
                        option.addField(secretAccessKey);
                    }
                    break;
            }
            values.add(option);
        }
        securityTypeField.setOptions(values);
        panel.addField(securityTypeField);

        return panel;
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
        DsConfigKvDef autoCommit = fields.get(DsConfigSpi.AUTO_COMMIT_FIELD);
        DsConfigKvDef isolation = fields.get(DataSourceConfig.Fields.isolation);
        if (autoCommit != null || isolation != null) {
            UiPanelField field = transactionControlField(dsType, autoCommit, isolation);
            if (autoCommit != null) {
                field.addField(hiddenField(DsConfigGroup.OPTIONS, autoCommit));
            }
            if (isolation != null) {
                field.addField(hiddenField(DsConfigGroup.OPTIONS, isolation));
            }
            panel.addField(field);
        }

        // timeZone
        DsConfigKvDef clientTimeZone = fields.get(DsConfigSpi.CLIENT_TIME_ZONE_FIELD);
        if (clientTimeZone != null) {
            panel.addField(timeZoneField(clientTimeZone));
        }

        return panel;
    }

    protected UiPanelField transactionControlField(DataSourceType dsType, DsConfigKvDef autoCommit, DsConfigKvDef isolation) {
        Map<String, Object> defaultValues = new LinkedHashMap<>();
        defaultValues.put(DsConfigSpi.AUTO_COMMIT_FIELD, autoCommit == null ? "true" : autoCommit.getDefaultValue());
        defaultValues.put(DataSourceConfig.Fields.isolation, isolation == null ? "DEFAULT" : isolation.getDefaultValue());

        UiPanelField.UiPanelFieldBuilder builder = UiPanelField.builder()
            .field(DsConfigSpi.TRANSACTION_CONTROL_FIELD)
            .type(UiPanelFieldType.TransactionControl)
            .titleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_TRANSACTION_CONTROL_LABEL))
            .descI18N("")
            .defaultValue(MapValueDef.builder().data(defaultValues).build());

        UiPanelField field = builder.build();

        List<ValueDef> values = new ArrayList<>();
        values.add(UiUtils.fieldOptionDef(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_TRANSACTION_MODE_AUTO), DsConfigSpi.TRANSACTION_MODE_AUTO_VALUE));
        values.add(UiUtils.fieldOptionDef(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_TRANSACTION_MODE_MANUAL), DsConfigSpi.TRANSACTION_MODE_MANUAL_VALUE));
        if (isolation != null) {
            RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsType);
            if (supportSpi != null && supportSpi.supportIsolation() != null) {
                for (RdbIsolation isolationValue : supportSpi.supportIsolation()) {
                    I18nDmLabelKeys i18nKey = I18nDmLabelKeys.valueOf("RDB_ISOLATION_" + isolationValue.getName());
                    values.add(UiUtils.fieldOptionDef(DmI18nUtils.getMessage(i18nKey.name()), isolationValue.getName()));
                }
            }
        }
        field.setOptions(values);
        return field;
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
        field.setDefaultValue(UiUtils.strValueDef(ZoneId.systemDefault().getId()));
        field.setOptions(values);
        field.setTitleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_TIME_ZONE_LABEL));
        field.setDescI18N("");
        return field;
    }

    protected UiPanel sshSslPanel(DataSourceType dsType, Map<String, DsConfigKvDef> fields) {
        UiPanel panel = newPanel(DsConfigGroup.SSH_SSL);
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        boolean supportSSH = configSpi != null && configSpi.supportSSH();
        boolean supportSSL = configSpi != null && configSpi.supportSSL();
        if (!supportSSH) {
            fields.remove(DataSourceConfig.Fields.sshProxyEnabled);
            fields.remove(DataSourceConfig.Fields.sshConfigId);
        }
        if (!supportSSL) {
            fields.remove(DataSourceConfig.Fields.sslMode);
            fields.remove(DataSourceConfig.Fields.sslCaData);
            fields.remove(DataSourceConfig.Fields.sslClientCertData);
            fields.remove(DataSourceConfig.Fields.sslClientKeyData);
            fields.remove(DataSourceConfig.Fields.sslClientKeyPassword);
        }

        // SSH
        if (supportSSH) {
            DsConfigKvDef sshProxyEnabled = fields.get(DataSourceConfig.Fields.sshProxyEnabled);
            DsConfigKvDef sshConfigId = fields.get(DataSourceConfig.Fields.sshConfigId);
            UiPanelField sshField = UiPanelField.builder()
                .field(DsConfigSpi.SSH_TUNNEL_FIELD)
                .type(UiPanelFieldType.SshTunnel)
                .titleI18N(DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SSH_TUNNEL_LABEL))
                .descI18N("")
                .build();
            if (sshProxyEnabled == null) {
                sshField.addField(hiddenField(DataSourceConfig.Fields.sshProxyEnabled, "false"));
            } else {
                sshField.addField(hiddenField(DsConfigGroup.SSH_SSL, sshProxyEnabled));
            }
            if (sshConfigId == null) {
                sshField.addField(hiddenField(DataSourceConfig.Fields.sshConfigId, ""));
            } else {
                sshField.addField(hiddenField(DsConfigGroup.SSH_SSL, sshConfigId));
            }
            panel.addField(sshField);
        }

        // SSL
        DsConfigKvDef sslMode = supportSSL ? fields.get(DataSourceConfig.Fields.sslMode) : null;
        if (supportSSL && sslMode != null) {
            UiPanelField field = createField(DsConfigGroup.SSH_SSL, sslMode);
            field.setType(UiPanelFieldType.Options);
            field.setDescI18N("");
            DsConfigKvDef sslCaData = fields.get(DataSourceConfig.Fields.sslCaData);
            DsConfigKvDef sslClientCertData = fields.get(DataSourceConfig.Fields.sslClientCertData);
            DsConfigKvDef sslClientKeyData = fields.get(DataSourceConfig.Fields.sslClientKeyData);
            DsConfigKvDef sslClientKeyPassword = fields.get(DataSourceConfig.Fields.sslClientKeyPassword);
            if (sslCaData != null) {
                field.addField(hiddenField(DsConfigGroup.SSH_SSL, sslCaData));
            }
            if (sslClientCertData != null) {
                field.addField(hiddenField(DsConfigGroup.SSH_SSL, sslClientCertData));
            }
            if (sslClientKeyData != null) {
                field.addField(hiddenField(DsConfigGroup.SSH_SSL, sslClientKeyData));
            }
            if (sslClientKeyPassword != null) {
                field.addField(hiddenField(DsConfigGroup.SSH_SSL, sslClientKeyPassword));
            }
            List<ValueDef> values = new ArrayList<>();
            for (SslMode mode : SslMode.values()) {
                String key = switch (mode) {
                    case DISABLED -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_DISABLED;
                    case TRUST -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_TRUST;
                    case CA -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_CA;
                    case CLIENT_CERT -> ConfigI18nKey.CONFIG_ADD_DS_SSL_MODE_CLIENT_CERT;
                };
                FieldOptionValueDef option = UiUtils.fieldOptionDef(DmI18nUtils.getMessage(key), mode.name());
                switch (mode) {
                    case DISABLED:
                    case TRUST:
                        break;
                    case CA:
                        if (sslCaData != null) {
                            option.addField(createField(DsConfigGroup.SSH_SSL, sslCaData));
                        }
                        break;
                    case CLIENT_CERT:
                        if (sslCaData != null) {
                            option.addField(createField(DsConfigGroup.SSH_SSL, sslCaData));
                        }
                        if (sslClientCertData != null) {
                            option.addField(createField(DsConfigGroup.SSH_SSL, sslClientCertData));
                        }
                        if (sslClientKeyData != null) {
                            option.addField(createField(DsConfigGroup.SSH_SSL, sslClientKeyData));
                        }
                        if (sslClientKeyPassword != null) {
                            option.addField(createField(DsConfigGroup.SSH_SSL, sslClientKeyPassword));
                        }
                        break;
                }
                values.add(option);
            }
            field.setOptions(values);
            panel.addField(field);
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

    protected UiPanel newPanel(DsConfigGroup group) {
        UiPanel panel = new UiPanel();
        panel.setKey(group.name());
        panel.setTitleI18N(dsConfigGroupTitle(group));
        panel.setDescI18N(dsConfigGroupTitle(group));
        return panel;
    }

    protected UiPanelField createField(DsConfigGroup group, DsConfigKvDef configDef) {
        String desc = fieldDesc(configDef);
        String title = switch (configDef.getConfigName()) {
            case DsConfigSpi.CONNECT_TIMEOUT_MS_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_TITLE);
            case DsConfigSpi.SO_TIMEOUT_SEC_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_TITLE);
            case DsConfigSpi.CONN_AND_SO_TIMEOUT_MS_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_TITLE);
            case DataSourceConfig.Fields.maxIdleTimeSec -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_MAX_IDLE_TIME_SEC_TITLE);
            case DataSourceConfig.Fields.onlineMaxConnections -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONNECTIONS_TITLE);
            case DataSourceConfig.Fields.onlineMaxQueryTimeoutSec -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_TITLE);
            case DataSourceConfig.Fields.exportMaxConnections -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_TITLE);
            case DataSourceConfig.Fields.exportMaxQueryTimeoutSec -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_TITLE);
            case DsConfigSpi.DEFAULT_CATALOG_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_LABEL);
            case DsConfigSpi.DEFAULT_SCHEMA_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_LABEL);
            default -> configDef.getDescKey();
        };
        UiPanelField.UiPanelFieldBuilder builder = UiPanelField.builder()
            .field(configDef.getConfigName())
            .type(fieldType(configDef))
            .require(configDef.isValueRequire())
            .readOnly(configDef.isReadOnly())
            .hide(group == DsConfigGroup.SHADOW)
            .defaultValue(UiUtils.strValueDef(configDef.getDefaultValue()))
            .activeExpr(activeExpr(configDef))
            .titleI18N(title)
            .descI18N(desc);

        return builder.build();
    }

    protected String fieldDesc(DsConfigKvDef configDef) {
        return switch (configDef.getConfigName()) {
            case DataSourceConfig.Fields.sslCaData -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SSL_CA_DATA_DESC);
            case DataSourceConfig.Fields.sslClientCertData -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SSL_CLIENT_CERT_DATA_DESC);
            case DataSourceConfig.Fields.sslClientKeyData -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SSL_CLIENT_KEY_DATA_DESC);
            case DataSourceConfig.Fields.sslClientKeyPassword -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_ADD_DS_SSL_CLIENT_KEY_PASSWORD_DESC);
            case DsConfigSpi.DEFAULT_CATALOG_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_DESCRIPTION);
            case DsConfigSpi.DEFAULT_SCHEMA_FIELD -> DmI18nUtils.getMessage(ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_DESCRIPTION);
            default -> configDef.getDescKey();
        };
    }

    protected UiPanelField hiddenField(String field, String defaultValue) {
        return UiPanelField.builder().field(field).type(UiPanelFieldType.Input).hide(true).defaultValue(UiUtils.strValueDef(defaultValue)).build();
    }

    protected UiPanelField hiddenField(DsConfigGroup group, DsConfigKvDef configDef) {
        UiPanelField field = createField(group, configDef);
        field.setHide(true);
        return field;
    }

    protected UiPanelField simpleField(String field, UiPanelFieldType type, String labelKey) {
        return UiPanelField.builder().field(field).type(type).require(false).titleI18N(DmI18nUtils.getMessage(labelKey)).descI18N("").build();
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

    protected List<DsSecurityOption> securityOptions(DataSourceType dsType) {
        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        if (configSpi == null) {
            return Collections.emptyList();
        } else {
            return RdpConvertUtils.convertToDsSecurityOptions(configSpi.securityTypes());
        }
    }

    protected String defaultSecurityType(List<DsSecurityOption> securityOptions) {
        if (securityOptions == null || securityOptions.isEmpty()) {
            return "";
        }
        DsSecurityOption first = null;
        for (DsSecurityOption option : securityOptions) {
            if (option == null || option.getSecurityType() == null) {
                continue;
            }
            if (first == null) {
                first = option;
            }
            if (option.isDefaultCheck()) {
                return option.getSecurityType().name();
            }
        }
        return first == null ? "" : first.getSecurityType().name();
    }

    private Map<String, DsConfigKvDef> safeFields(Map<String, DsConfigKvDef> fields) {
        return fields == null ? Collections.emptyMap() : fields;
    }
}
