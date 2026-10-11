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
package com.clougence.clouddm.ds.es.dsconf;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsConfigSpi extends AbstractDsConfigSpi {
    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return EsConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> values) {
        EsConfig config = (EsConfig) dsConfig;
        try {
            config.setConnectTimeoutMs(Integer.parseInt(values.getOrDefault(EsConfig.Fields.connectTimeoutMs, "5000")));
            config.setSocketTimeoutMs(Integer.parseInt(values.getOrDefault(EsConfig.Fields.socketTimeoutMs, "30000")));
            config.setRequestTimeoutMs(Integer.parseInt(values.getOrDefault(EsConfig.Fields.requestTimeoutMs, "30000")));
            config.setConnectionRequestTimeoutMs(Integer.parseInt(values.getOrDefault(EsConfig.Fields.connectionRequestTimeoutMs, "5000")));
            config.setMaxConnections(Integer.parseInt(values.getOrDefault(EsConfig.Fields.maxConnections, "20")));
            config.setMaxConnectionsPerRoute(Integer.parseInt(values.getOrDefault(EsConfig.Fields.maxConnectionsPerRoute, "10")));
        } catch (NumberFormatException e) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_LIMITS_INVALID);
        }
        config.validateLimits();
        if (config.getHost() != null && !config.getHost().isBlank()) {
            EsEndpoints.parse(config.getHost());
        }
        return config;
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanelField host = panels.get(DsConfigGroup.GENERAL).findField(DataSourceConfig.Fields.host);
        host.setType(UiPanelFieldType.Input);
        host.setChildren(new ArrayList<>());
        host.setTitleI18N(EsDsI18nKeys.ES_ENDPOINTS_LABEL);
        host.setDescI18N(EsDsI18nKeys.ES_ENDPOINTS_DESC);
        host.setProps(Map.of("descriptionAsPopover", true));
    }

    @Override
    public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
        if (!uiMap.containsKey(DataSourceConfig.Fields.host)) {
            return Map.of();
        }
        var endpoints = EsEndpoints.parse(uiMap.get(DataSourceConfig.Fields.host));
        return Map.of(DataSourceConfig.Fields.host, String.join(",", endpoints.stream().map(Object::toString).toList()));
    }

    @Override
    public void customizeUiMap(Map<String, String> uiMap, Map<String, String> configMap) {
        uiMap.remove(ADDRESS_FIELD);
        uiMap.remove(PORT_FIELD);
    }

    @Override
    public String defaultPort() {
        return "9200";
    }

    @Override
    public boolean supportSSL() {
        return true;
    }

    @Override
    public boolean supportSSH() {
        return false;
    }

    @Override
    public boolean supportTx() {
        return false;
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of(SecurityType.NONE, SecurityType.USER_PASSWD, SecurityType.API_KEY);
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.CA, SslMode.TRUSTSTORE, SslMode.CLIENT_CERT, SslMode.KEYSTORE_TRUSTSTORE);
    }
}
