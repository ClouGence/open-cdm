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

import static com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi.ADDRESS_FIELD;
import static com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi.PORT_FIELD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.service.upload.UploadService4Certificate;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.drivers.DriverFamily;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

/**
 * Converts data between add/edit datasource UI forms and persisted config kv.
 *
 * @author bucketli 2026/6/27
 */
@Component
public class DmDsConfigUiDataFactory {

    private static final String       DRIVER_FAMILY_FIELD = "driverFamily";

    @Resource
    private UploadService4Certificate uploadService;

    public Map<String, String> toKvMap(String uid, DataSourceType dsType, Map<String, DsConfigKvDef> configDefMap, Map<String, String> uiMap) {
        Map<String, String> kvMap = new LinkedHashMap<>();
        if (configDefMap == null || configDefMap.isEmpty()) {
            return kvMap;
        }

        kvMap.putAll(driverData(dsType, configDefMap, uiMap));
        kvMap.putAll(addressKvData(configDefMap, uiMap));
        kvMap.putAll(authData(configDefMap, uiMap));
        kvMap.putAll(sshSslData(uid, configDefMap, uiMap));

        DsConfigSpi configSpi = PluginManager.findDsConfigSpi(dsType);
        Map<String, String> customData = configSpi.configMapFromUi(kvMap, uiMap);
        if (customData != null) {
            for (Map.Entry<String, String> entry : customData.entrySet()) {
                configDefMap.remove(entry.getKey());
                if (entry.getValue() == null) {
                    kvMap.remove(entry.getKey());
                } else {
                    kvMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        kvMap.putAll(otherData(configDefMap, uiMap, kvMap));
        return kvMap;
    }

    //

    private Map<String, String> driverData(DataSourceType dsType, Map<String, DsConfigKvDef> configDefMap, Map<String, String> input) {
        Map<String, String> data = new LinkedHashMap<>();
        if (input == null || (!input.containsKey(DRIVER_FAMILY_FIELD) && !input.containsKey(DataSourceConfig.Fields.driverVersion))) {
            return data;
        }

        DsConfigKvDef configDef = configDefMap.remove(DataSourceConfig.Fields.driverVersion);
        if (configDef == null) {
            return data;
        }

        String driverFamily = StringUtils.trimToNull(input.get(DRIVER_FAMILY_FIELD));
        String driverVersion = StringUtils.trimToNull(input.get(DataSourceConfig.Fields.driverVersion));
        if (StringUtils.isBlank(driverFamily) || StringUtils.isBlank(driverVersion)) {
            String defaultDriver = defaultDriverSpec(dsType);
            if (StringUtils.isNotBlank(defaultDriver)) {
                data.put(DataSourceConfig.Fields.driverVersion, defaultDriver);
            }
            return data;
        }

        data.put(DataSourceConfig.Fields.driverVersion, JsonUtils.toJson(Arrays.asList(driverFamily, driverVersion)));
        return data;
    }

    private String defaultDriverSpec(DataSourceType dsType) {
        DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
        if (dsPlugin == null || dsPlugin.getBindDrivers() == null || dsPlugin.getBindDrivers().isEmpty()) {
            return null;
        }

        for (String driverFamilyName : dsPlugin.getBindDrivers()) {
            String driverFamily = StringUtils.trimToNull(driverFamilyName);
            if (driverFamily == null) {
                continue;
            }

            DriverFamily family = PluginManager.driverLoader().findDriver(driverFamily);
            if (family == null || family.getVersions() == null || family.getVersions().isEmpty()) {
                continue;
            }

            String driverVersion = StringUtils.trimToNull(family.getVersions().get(0));
            if (driverVersion != null) {
                return JsonUtils.toJson(Arrays.asList(driverFamily, driverVersion));
            }
        }
        return null;
    }

    private Map<String, String> addressKvData(Map<String, DsConfigKvDef> configDefMap, Map<String, String> input) {
        Map<String, String> data = new LinkedHashMap<>();
        if (input == null || (!input.containsKey(ADDRESS_FIELD) && !input.containsKey(PORT_FIELD))) {
            return data;
        }

        DsConfigKvDef configDef = configDefMap.remove(DataSourceConfig.Fields.host);
        if (configDef == null) {
            return data;
        }
        String address = StringUtils.trimToNull(input.get(ADDRESS_FIELD));
        String port = StringUtils.trimToNull(input.get(PORT_FIELD));
        data.put(DataSourceConfig.Fields.host, address + ":" + port);
        return data;
    }

    private Map<String, String> authData(Map<String, DsConfigKvDef> configDefMap, Map<String, String> input) {
        Map<String, String> data = new LinkedHashMap<>();
        putField(data, configDefMap, input, DataSourceConfig.Fields.securityType);
        putField(data, configDefMap, input, DataSourceConfig.Fields.userName);
        putField(data, configDefMap, input, DataSourceConfig.Fields.password);

        if (input != null && input.containsKey(DataSourceConfig.Fields.securityType)) {
            String securityTypeValue = StringUtils.trimToNull(input.get(DataSourceConfig.Fields.securityType));
            SecurityType securityType = securityTypeValue == null ? SecurityType.NONE : SecurityType.valueOf(securityTypeValue);
            switch (securityType) {
                case NONE:
                    configDefMap.remove(DataSourceConfig.Fields.userName);
                    configDefMap.remove(DataSourceConfig.Fields.password);
                    data.put(DataSourceConfig.Fields.userName, null);
                    data.put(DataSourceConfig.Fields.password, null);
                    break;
                case ONLY_USER:
                    configDefMap.remove(DataSourceConfig.Fields.password);
                    data.put(DataSourceConfig.Fields.password, null);
                    break;
                case ONLY_PASSWD:
                case API_KEY:
                    configDefMap.remove(DataSourceConfig.Fields.userName);
                    data.put(DataSourceConfig.Fields.userName, null);
                    break;
                case USER_PASSWD:
                case AK_SK:
                    break;
                default:
                    throw new IllegalArgumentException("unsupported security type:" + securityType);
            }
        }
        return data;
    }

    private Map<String, String> sshSslData(String uid, Map<String, DsConfigKvDef> configDefMap, Map<String, String> input) {
        Map<String, String> data = new LinkedHashMap<>();

        // SSH
        putField(data, configDefMap, input, DataSourceConfig.Fields.sshProxyEnabled);
        putField(data, configDefMap, input, DataSourceConfig.Fields.sshConfigId);
        if (input != null && input.containsKey(DataSourceConfig.Fields.sshProxyEnabled) && !Boolean.parseBoolean(input.get(DataSourceConfig.Fields.sshProxyEnabled))) {
            configDefMap.remove(DataSourceConfig.Fields.sshConfigId);
            data.put(DataSourceConfig.Fields.sshConfigId, null);
        }

        // SSL
        putField(data, configDefMap, input, DataSourceConfig.Fields.sslMode);
        if (input != null && input.containsKey(DataSourceConfig.Fields.sslMode)) {
            String sslModeValue = StringUtils.trimToNull(input.get(DataSourceConfig.Fields.sslMode));
            SslMode sslMode = sslModeValue == null ? SslMode.DISABLED : SslMode.valueOf(sslModeValue);
            switch (sslMode) {
                case DISABLED:
                case TRUST:
                    configDefMap.remove(DataSourceConfig.Fields.sslCaData);
                    configDefMap.remove(DataSourceConfig.Fields.sslCaPassword);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientCertData);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientKeyData);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientKeyPassword);
                    data.put(DataSourceConfig.Fields.sslCaData, null);
                    data.put(DataSourceConfig.Fields.sslCaPassword, null);
                    data.put(DataSourceConfig.Fields.sslClientCertData, null);
                    data.put(DataSourceConfig.Fields.sslClientKeyData, null);
                    data.put(DataSourceConfig.Fields.sslClientKeyPassword, null);
                    break;
                case CA:
                case TRUSTSTORE:
                    if (input.containsKey(DataSourceConfig.Fields.sslCaData)) {
                        configDefMap.remove(DataSourceConfig.Fields.sslCaData);
                        data.put(DataSourceConfig.Fields.sslCaData, //
                                this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslCaData)));
                    }
                    putField(data, configDefMap, input, DataSourceConfig.Fields.sslCaPassword);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientCertData);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientKeyData);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientKeyPassword);
                    data.put(DataSourceConfig.Fields.sslClientCertData, null);
                    data.put(DataSourceConfig.Fields.sslClientKeyData, null);
                    data.put(DataSourceConfig.Fields.sslClientKeyPassword, null);
                    break;
                case KEYSTORE_TRUSTSTORE:
                    if (input.containsKey(DataSourceConfig.Fields.sslCaData)) {
                        configDefMap.remove(DataSourceConfig.Fields.sslCaData);
                        data.put(DataSourceConfig.Fields.sslCaData,//
                                this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslCaData)));
                    }
                    putField(data, configDefMap, input, DataSourceConfig.Fields.sslCaPassword);
                    if (input.containsKey(DataSourceConfig.Fields.sslClientCertData)) {
                        configDefMap.remove(DataSourceConfig.Fields.sslClientCertData);
                        data.put(DataSourceConfig.Fields.sslClientCertData, //
                                this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslClientCertData)));
                    }
                    configDefMap.remove(DataSourceConfig.Fields.sslClientCertData);
                    configDefMap.remove(DataSourceConfig.Fields.sslClientKeyData);
                    data.put(DataSourceConfig.Fields.sslClientKeyData, null);
                    putField(data, configDefMap, input, DataSourceConfig.Fields.sslClientKeyPassword);
                    break;
                case CLIENT_CERT:
                    if (input.containsKey(DataSourceConfig.Fields.sslCaData)) {
                        configDefMap.remove(DataSourceConfig.Fields.sslCaData);
                        data.put(DataSourceConfig.Fields.sslCaData, //
                                this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslCaData)));
                    }
                    configDefMap.remove(DataSourceConfig.Fields.sslCaPassword);
                    data.put(DataSourceConfig.Fields.sslCaPassword, null);
                    if (input.containsKey(DataSourceConfig.Fields.sslClientCertData)) {
                        configDefMap.remove(DataSourceConfig.Fields.sslClientCertData);
                        data.put(DataSourceConfig.Fields.sslClientCertData,//
                                this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslClientCertData)));
                    }
                    if (input.containsKey(DataSourceConfig.Fields.sslClientKeyData)) {
                        configDefMap.remove(DataSourceConfig.Fields.sslClientKeyData);
                        data.put(DataSourceConfig.Fields.sslClientKeyData, //
                                this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslClientKeyData)));
                    }
                    putField(data, configDefMap, input, DataSourceConfig.Fields.sslClientKeyPassword);
                    break;
                default:
                    throw new IllegalArgumentException("unsupported ssl mode:" + sslMode);
            }
        } else {
            if (input != null && input.containsKey(DataSourceConfig.Fields.sslCaData)) {
                configDefMap.remove(DataSourceConfig.Fields.sslCaData);
                data.put(DataSourceConfig.Fields.sslCaData, //
                        this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslCaData)));
            }
            putField(data, configDefMap, input, DataSourceConfig.Fields.sslCaPassword);
            if (input != null && input.containsKey(DataSourceConfig.Fields.sslClientCertData)) {
                configDefMap.remove(DataSourceConfig.Fields.sslClientCertData);
                data.put(DataSourceConfig.Fields.sslClientCertData,//
                        this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslClientCertData)));
            }
            if (input != null && input.containsKey(DataSourceConfig.Fields.sslClientKeyData)) {
                configDefMap.remove(DataSourceConfig.Fields.sslClientKeyData);
                data.put(DataSourceConfig.Fields.sslClientKeyData,//
                        this.readCertificateData(uid, input.get(DataSourceConfig.Fields.sslClientKeyData)));
            }
            putField(data, configDefMap, input, DataSourceConfig.Fields.sslClientKeyPassword);
        }
        return data;
    }

    private String readCertificateData(String uid, String value) {
        return this.uploadService.readCertificateData(uid, value);
    }

    private Map<String, String> otherData(Map<String, DsConfigKvDef> configDefMap, Map<String, String> input, Map<String, String> existingData) {
        Map<String, String> data = new LinkedHashMap<>();
        for (String configName : new ArrayList<>(configDefMap.keySet())) {
            if (existingData.containsKey(configName)) {
                continue;
            }
            putField(data, configDefMap, input, configName);
        }
        return data;
    }

    //

    private void putField(Map<String, String> output, Map<String, DsConfigKvDef> configDefMap, Map<String, String> input, String configName) {
        if (input == null || !input.containsKey(configName)) {
            return;
        }
        DsConfigKvDef configDef = configDefMap.remove(configName);
        if (configDef == null) {
            return;
        }

        String configValue = resolveValue(configDef, input);
        if (configDef.isSecret() && StringUtils.isBlank(configValue)) {
            return;
        }
        output.put(configName, configValue);
    }

    private String resolveValue(DsConfigKvDef configDef, Map<String, String> input) {
        if (configDef == null) {
            return null;
        }
        if (input != null && input.containsKey(configDef.getConfigName())) {
            return input.get(configDef.getConfigName());
        }
        if (configDef.getConfigValue() != null) {
            return configDef.getConfigValue();
        }
        return configDef.getDefaultValue();
    }
}
