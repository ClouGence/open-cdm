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
package com.clougence.clouddm.ds.hana.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.ds.hana.i18n.HanaConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.StringUtils;

public class HanaConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "30015";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return HanaConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        HanaConfig config = (HanaConfig) dsConfig;
        long connectTimeoutMs = 5000;
        int soTimeoutSec = 10;
        try {
            String connectTimeout = defaultConfig.get(HanaConfig.Fields.connectTimeoutMs);
            String communicationTimeout = defaultConfig.get(HanaConfig.Fields.soTimeoutSec);
            if (StringUtils.isNotBlank(connectTimeout)) {
                connectTimeoutMs = Long.parseLong(connectTimeout);
            }
            if (StringUtils.isNotBlank(communicationTimeout)) {
                soTimeoutSec = Integer.parseInt(communicationTimeout);
            }
        } catch (NumberFormatException e) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TIMEOUT_ERROR);
        }

        if (connectTimeoutMs < 0 || connectTimeoutMs > Integer.MAX_VALUE || soTimeoutSec < 0 || soTimeoutSec > Integer.MAX_VALUE / 1000) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TIMEOUT_ERROR);
        }

        config.setDefaultCatalog(defaultConfig.get(HanaConfig.Fields.defaultCatalog));
        config.setDefaultSchema(defaultConfig.get(HanaConfig.Fields.defaultSchema));
        config.setJdbcUrl(defaultConfig.get(HanaConfig.Fields.jdbcUrl));
        config.setHostNameInCertificate(defaultConfig.get(HanaConfig.Fields.hostNameInCertificate));
        config.setConnectTimeoutMs(connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec);
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
    public boolean supportSSL() {
        return true;
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.TRUST, SslMode.CA, SslMode.TRUSTSTORE, SslMode.KEYSTORE_TRUSTSTORE, SslMode.CLIENT_CERT);
    }

    @Override
    public List<String> certificateBinaryFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.TRUSTSTORE || sslMode == SslMode.KEYSTORE_TRUSTSTORE) {
            return super.certificateBinaryFileTypes(sslMode, configName);
        }

        if (DataSourceConfig.Fields.sslClientKeyData.equals(configName)) {
            return List.of("pem", "key", "pk8");
        }

        return List.of("pem", "crt", "cer");
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanelField host = panels.get(DsConfigGroup.GENERAL).findField(DataSourceConfig.Fields.host);
        host.setDescI18N(HanaConfigI18nKeys.CONFIG_HANA_HOST_DESC);
        // A custom JDBC endpoint supplies both address and port; the factory validates either form.
        host.setRequire(false);
        host.findField(ADDRESS_FIELD).setRequire(false);
        host.findField(PORT_FIELD).setRequire(false);
    }

    @Override
    public boolean supportSSH() {
        return true;
    }

    @Override
    public boolean supportTx() {
        return true;
    }

}
