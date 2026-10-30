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
package com.clougence.clouddm.ds.etcd.dsconf;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.etcd.i18n.EtcdConfigI18nKeys;
import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.StringUtils;

public class EtcdConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "2379";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return EtcdConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        EtcdConfig config = (EtcdConfig) dsConfig;
        try {
            config.setApiTimeoutMs(Integer.parseInt(defaultConfig.getOrDefault(EtcdConfig.Fields.apiTimeoutMs, "10000")));
        } catch (NumberFormatException e) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_TIMEOUT_ERROR);
        }
        if (config.getApiTimeoutMs() <= 0) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_TIMEOUT_ERROR);
        }
        if (StringUtils.isNotBlank(config.getHost())) {
            config.setHost(normalizeEndpoints(config.getHost()));
        }
        return config;
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        UiPanelField host = general.findField(DataSourceConfig.Fields.host);
        host.setType(UiPanelFieldType.Input);
        host.setChildren(new ArrayList<>());
        host.setTitleI18N(EtcdConfigI18nKeys.CONFIG_ETCD_ENDPOINTS_LABEL);
        host.setDescI18N(EtcdConfigI18nKeys.CONFIG_ETCD_ENDPOINTS_DESC);
        host.setProps(Map.of("descriptionAsPopover", true));
    }

    @Override
    public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
        if (!uiMap.containsKey(DataSourceConfig.Fields.host)) {
            return Map.of();
        }
        return Map.of(DataSourceConfig.Fields.host, normalizeEndpoints(uiMap.get(DataSourceConfig.Fields.host)));
    }

    static String normalizeEndpoints(String value) {
        if (StringUtils.isBlank(value)) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_ENDPOINTS_ERROR);
        }
        List<String> endpoints = new ArrayList<>();
        for (String entry : value.split(",", -1)) {
            String endpoint = entry.trim();
            if (endpoint.startsWith("http://")) {
                endpoint = endpoint.substring("http://".length());
            } else if (endpoint.startsWith("https://")) {
                endpoint = endpoint.substring("https://".length());
            }
            try {
                URI address = URI.create("etcd://" + endpoint);
                if (address.getHost() == null || address.getPort() < 1 || address.getPort() > 65535 || address.getUserInfo() != null || !address.getRawPath().isEmpty()
                    || address.getQuery() != null || address.getFragment() != null) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_ENDPOINTS_ERROR);
            }
            endpoints.add(endpoint);
        }
        if (endpoints.isEmpty()) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_ENDPOINTS_ERROR);
        }
        return String.join(",", endpoints);
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of(SecurityType.NONE, SecurityType.USER_PASSWD);
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.DISABLED, SslMode.CA);
    }

    @Override
    public boolean supportTx() {
        return false;
    }

    @Override
    public boolean supportSSL() {
        return true;
    }

    @Override
    public boolean supportSSH() {
        return false;
    }
}
