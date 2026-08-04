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
package com.clougence.clouddm.ds.mongodb.dsconf;

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
import com.clougence.clouddm.ds.mongodb.execute.MongoConnectionUri;
import com.clougence.clouddm.ds.mongodb.i18n.MongoConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class MongoConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "27017";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return MongoConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        MongoConfig config = (MongoConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(MongoConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(MongoConfig.Fields.soTimeoutSec), false);
        config.setConnectType(MongoConnectType.of(defaultConfig.get(MongoConfig.Fields.connectType)));
        config.setDefaultSchema(defaultConfig.get(MongoConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        return dsConfig;
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        if (general != null) {
            customizeGeneralPanel(general);
        }
    }

    private void customizeGeneralPanel(UiPanel general) {
        UiPanelField host = general.findField(DataSourceConfig.Fields.host);
        UiPanelField connectType = general.findField(MongoConfig.Fields.connectType);
        UiPanelField securityType = general.findField(DataSourceConfig.Fields.securityType);
        if (host == null || connectType == null || securityType == null) {
            return;
        }

        UiPanelField srvHost = UiPanelField.builder().field(host.getField()).build();
        srvHost.applyTo(host);
        srvHost.setType(UiPanelFieldType.Input);
        srvHost.setChildren(new ArrayList<>());

        List<ValueDef> options = new ArrayList<>();
        options.add(fieldOptionDef(MongoConfigI18nKeys.CONFIG_MONGODB_CONNECT_TYPE_DEFAULT_LABEL, MongoConnectType.DEFAULT.getCode()).addField(host));
        options.add(fieldOptionDef(MongoConfigI18nKeys.CONFIG_MONGODB_CONNECT_TYPE_SRV_LABEL, MongoConnectType.SRV.getCode()).addField(srvHost));
        connectType.setType(UiPanelFieldType.Options);
        connectType.setOptions(options);
        if (connectType.getDefaultValue() == null || connectType.getDefaultValue().asValue() == null
            || StringUtils.isBlank(String.valueOf(connectType.getDefaultValue().asValue()))) {
            connectType.setDefaultValue(strValueDef(MongoConnectType.DEFAULT.getCode()));
        } else {
            connectType.setDefaultValue(strValueDef(MongoConnectType.of(String.valueOf(connectType.getDefaultValue().asValue())).getCode()));
        }

        general.removeField(DataSourceConfig.Fields.host);
        general.removeField(MongoConfig.Fields.connectType);
        general.beforeAddField(connectType, DataSourceConfig.Fields.securityType);
    }

    @Override
    public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
        Map<String, String> data = new LinkedHashMap<>();
        if (uiMap == null || (!uiMap.containsKey(MongoConfig.Fields.connectType) && !uiMap.containsKey(DataSourceConfig.Fields.host)
                              && !uiMap.containsKey(ADDRESS_FIELD) && !uiMap.containsKey(PORT_FIELD))) {
            return data;
        }

        String connectTypeValue = uiMap.get(MongoConfig.Fields.connectType);
        if (StringUtils.isBlank(connectTypeValue)) {
            connectTypeValue = configMap.get(MongoConfig.Fields.connectType);
        }
        MongoConnectType connectType = MongoConnectType.of(connectTypeValue);
        data.put(MongoConfig.Fields.connectType, connectType.getCode());
        if (connectType == MongoConnectType.SRV) {
            String host = StringUtils.trimToNull(uiMap.get(DataSourceConfig.Fields.host));
            if (host == null) {
                host = StringUtils.trimToNull(configMap.get(DataSourceConfig.Fields.host));
            }
            data.put(DataSourceConfig.Fields.host, MongoConnectionUri.normalizeSrvHost(host));
        } else if (uiMap.containsKey(ADDRESS_FIELD) || uiMap.containsKey(PORT_FIELD)) {
            String address = StringUtils.trimToNull(uiMap.get(ADDRESS_FIELD));
            String port = StringUtils.trimToNull(uiMap.get(PORT_FIELD));
            if (port == null) {
                port = defaultPort();
            }
            if (address != null && port != null) {
                data.put(DataSourceConfig.Fields.host, address + ":" + port);
            }
        }
        return data;
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
        return List.of(SslMode.TRUST);
    }

    @Override
    public boolean supportTx() {
        return false;
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public boolean supportSSH() {
        return true;
    }
}
