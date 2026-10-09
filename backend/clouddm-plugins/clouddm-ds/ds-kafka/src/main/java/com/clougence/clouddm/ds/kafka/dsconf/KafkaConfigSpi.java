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
package com.clougence.clouddm.ds.kafka.dsconf;

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
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.StringUtils;

public class KafkaConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "9092";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return KafkaConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        KafkaConfig config = (KafkaConfig) dsConfig;
        String mechanism = defaultConfig.get(KafkaConfig.Fields.saslMechanism);
        if (StringUtils.isBlank(mechanism)) {
            mechanism = "PLAIN";
        }
        if (!List.of("PLAIN", "SCRAM-SHA-256", "SCRAM-SHA-512").contains(mechanism)) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SASL_ERROR);
        }
        config.setSaslMechanism(mechanism);
        try {
            config.setConnectTimeoutMs(Integer.parseInt(defaultConfig.getOrDefault(KafkaConfig.Fields.connectTimeoutMs, "5000")));
            config.setRequestTimeoutMs(Integer.parseInt(defaultConfig.getOrDefault(KafkaConfig.Fields.requestTimeoutMs, "10000")));
            config.setApiTimeoutMs(Integer.parseInt(defaultConfig.getOrDefault(KafkaConfig.Fields.apiTimeoutMs, "30000")));
        } catch (NumberFormatException e) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_TIMEOUT_ERROR);
        }
        if (config.getConnectTimeoutMs() <= 0 || config.getRequestTimeoutMs() <= 0 || config.getApiTimeoutMs() < config.getRequestTimeoutMs()) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_TIMEOUT_ERROR);
        }
        if (StringUtils.isNotBlank(config.getHost())) {
            config.setHost(normalizeBrokers(config.getHost()));
        }
        return config;
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        UiPanelField host = general.findField(DataSourceConfig.Fields.host);
        host.setType(UiPanelFieldType.Input);
        host.setChildren(new ArrayList<>());
        host.setTitleI18N(KafkaConfigI18nKeys.CONFIG_KAFKA_BROKERS_LABEL);
        host.setDescI18N(KafkaConfigI18nKeys.CONFIG_KAFKA_BROKERS_DESC);
        UiPanelField mechanism = general.findField(KafkaConfig.Fields.saslMechanism);
        mechanism.setType(UiPanelFieldType.Options);
        mechanism.setOptions(List.of(UiUtils.optionDef(KafkaConfigI18nKeys.CONFIG_KAFKA_PLAIN_LABEL, "PLAIN"), UiUtils
            .optionDef(KafkaConfigI18nKeys.CONFIG_KAFKA_SCRAM256_LABEL, "SCRAM-SHA-256"), UiUtils.optionDef(KafkaConfigI18nKeys.CONFIG_KAFKA_SCRAM512_LABEL, "SCRAM-SHA-512")));
    }

    @Override
    public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
        if (!uiMap.containsKey(DataSourceConfig.Fields.host)) {
            return Map.of();
        }
        return Map.of(DataSourceConfig.Fields.host, normalizeBrokers(uiMap.get(DataSourceConfig.Fields.host)));
    }

    @Override
    public void customizeUiMap(Map<String, String> uiMap, Map<String, String> configMap) {
        uiMap.remove(ADDRESS_FIELD);
        uiMap.remove(PORT_FIELD);
    }

    private String normalizeBrokers(String value) {
        if (StringUtils.isBlank(value)) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_BROKERS_ERROR);
        }
        List<String> brokers = new ArrayList<>();
        for (String entry : value.split(",", -1)) {
            String broker = entry.trim();
            try {
                URI address = URI.create("kafka://" + broker);
                if (address.getHost() == null || address.getPort() < 1 || address.getPort() > 65535 || address.getUserInfo() != null || !address.getRawPath().isEmpty()
                    || address.getQuery() != null || address.getFragment() != null) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_BROKERS_ERROR);
            }
            brokers.add(broker);
        }
        return String.join(",", brokers);
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of(SecurityType.NONE, SecurityType.USER_PASSWD);
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.CA, SslMode.TRUSTSTORE, SslMode.KEYSTORE_TRUSTSTORE, SslMode.CLIENT_CERT);
    }

    @Override
    public List<String> certificateTextFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.CA || sslMode == SslMode.CLIENT_CERT) {
            return List.of("pem");
        }
        return super.certificateTextFileTypes(sslMode, configName);
    }

    @Override
    public List<String> certificateBinaryFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.CA || sslMode == SslMode.CLIENT_CERT) {
            return List.of("pem");
        }
        return super.certificateBinaryFileTypes(sslMode, configName);
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
