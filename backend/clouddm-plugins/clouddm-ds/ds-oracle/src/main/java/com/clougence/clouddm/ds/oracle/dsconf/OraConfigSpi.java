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
package com.clougence.clouddm.ds.oracle.dsconf;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.common.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.ds.oracle.i18n.OraConfigI18nKeys;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class OraConfigSpi extends AbstractDsConfigSpi {

    private static void generalPanel(UiPanel general) {
        // connectType
        List<ValueDef> options = new ArrayList<>();
        options.add(fieldOptionDef(OraConfigI18nKeys.CONFIG_ORACLE_SID_LABEL, OraConnectType.SID.getDriverTypeCode())//
            .addField(general.findField(OraConfig.Fields.sid)));
        options.add(fieldOptionDef(OraConfigI18nKeys.CONFIG_ORACLE_SERVICE_LABEL, OraConnectType.SERVICE.getDriverTypeCode())
            .addField(general.findField(OraConfig.Fields.serviceName)));
        options.add(fieldOptionDef(OraConfigI18nKeys.CONFIG_ORACLE_PDB_LABEL, OraConnectType.PDB.getDriverTypeCode())//
            .addField(general.findField(OraConfig.Fields.pdbName)));
        options.add(fieldOptionDef(OraConfigI18nKeys.CONFIG_ORACLE_TNS_LABEL, OraConnectType.TNS.getDriverTypeCode())//
            .addField(general.findField(OraConfig.Fields.tnsAdmin))
            .addField(general.findField(OraConfig.Fields.tnsName)));

        UiPanelField connectType = general.findField(OraConfig.Fields.connectType);
        connectType.setType(UiPanelFieldType.Options);
        connectType.setOptions(options);
        if (connectType.getDefaultValue() == null ||            //
            connectType.getDefaultValue().asValue() == null ||  //
            StringUtils.isBlank(String.valueOf(connectType.getDefaultValue().asValue()))) {
            connectType.setDefaultValue(strValueDef(OraConnectType.SID.getDriverTypeCode()));
        }

        // readd
        general.removeField(OraConfig.Fields.connectType);
        general.removeField(OraConfig.Fields.sid);
        general.removeField(OraConfig.Fields.serviceName);
        general.removeField(OraConfig.Fields.pdbName);
        general.removeField(OraConfig.Fields.tnsAdmin);
        general.removeField(OraConfig.Fields.tnsName);
        general.beforeAddField(connectType, DataSourceConfig.Fields.securityType);
    }

    @Override
    public String defaultPort() {
        return "1521";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return OraConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        OraConfig config = (OraConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(OraConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(OraConfig.Fields.soTimeoutSec), false);
        OraConnectType connectType = OraConnectType.of(defaultConfig.get(OraConfig.Fields.connectType));
        config.setConnectType(connectType);
        config.setSid(defaultConfig.get(OraConfig.Fields.sid));
        config.setServiceName(defaultConfig.get(OraConfig.Fields.serviceName));
        config.setPdbName(defaultConfig.get(OraConfig.Fields.pdbName));
        config.setTnsAdmin(defaultConfig.get(OraConfig.Fields.tnsAdmin));
        config.setTnsName(defaultConfig.get(OraConfig.Fields.tnsName));
        if (StringUtils.isNotBlank(config.getHost())) {
            String[] ipPort = config.getHost().split(":");
            if (ipPort.length == 3) {
                config.setHost(ipPort[0] + ":" + ipPort[1]);
                switch (connectType) {
                    case SID:
                        config.setSid(ipPort[2]);
                        break;
                    case SERVICE:
                        config.setServiceName(ipPort[2]);
                        break;
                    case PDB:
                        config.setPdbName(ipPort[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("unsupported Oracle connect type:" + connectType);
                }
            } else if (ipPort.length != 2) {
                throw new IllegalArgumentException("unsupported Oracle host format:" + config.getHost());
            }
        }
        if (connectType == OraConnectType.PDB) {
            config.setConnectType(OraConnectType.SERVICE);
            if (StringUtils.isBlank(config.getServiceName())) {
                config.setServiceName(config.getPdbName());
            }
        }

        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);

        boolean excludeOraMaintainedSchemas = StringUtils.isBlank(defaultConfig.get(OraConfig.Fields.excludeOraMaintainedSchemas));
        config.setExcludeOraMaintainedSchemas((excludeOraMaintainedSchemas ? Boolean.FALSE : //
            ConvertUtils.toBoolean(defaultConfig.get(OraConfig.Fields.excludeOraMaintainedSchemas), false)));
        return dsConfig;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.NONE);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.CA, SslMode.CLIENT_CERT);
    }

    @Override
    public boolean supportSSL() {
        return true;
    }

    @Override
    public boolean supportSSH() {
        return true;
    }

    @Override
    public boolean supportTx() {
        return true;
    }

    @Override
    public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
        Map<String, String> data = new LinkedHashMap<>();
        if (uiMap == null || (!uiMap.containsKey(ADDRESS_FIELD) //
                              && !uiMap.containsKey(PORT_FIELD) //
                              && !uiMap.containsKey(OraConfig.Fields.connectType) //
                              && !uiMap.containsKey(OraConfig.Fields.sid) //
                              && !uiMap.containsKey(OraConfig.Fields.serviceName) //
                              && !uiMap.containsKey(OraConfig.Fields.pdbName) //
                              && !uiMap.containsKey(OraConfig.Fields.tnsAdmin) //
                              && !uiMap.containsKey(OraConfig.Fields.tnsName))) {
            return data;
        }

        String address = uiMap.get(ADDRESS_FIELD);
        String port = uiMap.get(PORT_FIELD);
        String host = configMap.get(DataSourceConfig.Fields.host);
        if (StringUtils.isNotBlank(address) && StringUtils.isNotBlank(port)) {
            host = address + ":" + port;
        }

        String connectTypeValue = uiMap.get(OraConfig.Fields.connectType);
        if (StringUtils.isBlank(connectTypeValue)) {
            connectTypeValue = configMap.get(OraConfig.Fields.connectType);
        }
        OraConnectType connectType = OraConnectType.of(connectTypeValue);
        data.put(OraConfig.Fields.connectType, connectType.getDriverTypeCode());
        switch (connectType) {
            case SID:
                String sid = uiMap.get(OraConfig.Fields.sid);
                if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(sid)) {
                    host = host + ":" + sid;
                }
                data.put(OraConfig.Fields.sid, sid);
                data.put(OraConfig.Fields.serviceName, null);
                data.put(OraConfig.Fields.pdbName, null);
                data.put(OraConfig.Fields.tnsAdmin, null);
                data.put(OraConfig.Fields.tnsName, null);
                break;
            case SERVICE:
                String serviceName = uiMap.get(OraConfig.Fields.serviceName);
                if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(serviceName)) {
                    host = host + ":" + serviceName;
                }
                data.put(OraConfig.Fields.sid, null);
                data.put(OraConfig.Fields.serviceName, serviceName);
                data.put(OraConfig.Fields.pdbName, null);
                data.put(OraConfig.Fields.tnsAdmin, null);
                data.put(OraConfig.Fields.tnsName, null);
                break;
            case PDB:
                String pdbName = uiMap.get(OraConfig.Fields.pdbName);
                if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(pdbName)) {
                    host = host + ":" + pdbName;
                }
                data.put(OraConfig.Fields.sid, null);
                data.put(OraConfig.Fields.serviceName, null);
                data.put(OraConfig.Fields.pdbName, pdbName);
                data.put(OraConfig.Fields.tnsAdmin, null);
                data.put(OraConfig.Fields.tnsName, null);
                break;
            case TNS:
                data.put(OraConfig.Fields.sid, null);
                data.put(OraConfig.Fields.serviceName, null);
                data.put(OraConfig.Fields.pdbName, null);
                data.put(OraConfig.Fields.tnsAdmin, uiMap.get(OraConfig.Fields.tnsAdmin));
                data.put(OraConfig.Fields.tnsName, uiMap.get(OraConfig.Fields.tnsName));
                break;
            default:
                throw new IllegalArgumentException("unsupported Oracle connect type:" + connectType);
        }
        data.put(DataSourceConfig.Fields.host, host);
        return data;
    }

    @Override
    public void customizeUiMap(Map<String, String> uiMap, Map<String, String> configMap) {
        String host = configMap.get(DataSourceConfig.Fields.host);
        if (StringUtils.isBlank(host)) {
            return;
        }
        String[] parts = host.split(":");
        if (parts.length < 2) {
            return;
        }
        uiMap.put(ADDRESS_FIELD, parts[0]);
        uiMap.put(PORT_FIELD, parts[1]);

        OraConnectType connectType = OraConnectType.of(configMap.get(OraConfig.Fields.connectType));
        uiMap.put(OraConfig.Fields.connectType, connectType.getDriverTypeCode());
        if (parts.length != 3) {
            return;
        }
        switch (connectType) {
            case SID:
                uiMap.putIfAbsent(OraConfig.Fields.sid, parts[2]);
                break;
            case SERVICE:
                uiMap.putIfAbsent(OraConfig.Fields.serviceName, parts[2]);
                break;
            case PDB:
                uiMap.putIfAbsent(OraConfig.Fields.pdbName, parts[2]);
                break;
            default:
                break;
        }
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        if (general == null) {
            return;
        }

        generalPanel(general);
    }
}
