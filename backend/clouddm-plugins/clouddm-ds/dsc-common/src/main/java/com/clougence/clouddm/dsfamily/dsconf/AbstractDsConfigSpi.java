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
package com.clougence.clouddm.dsfamily.dsconf;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.utils.StringUtils;

public abstract class AbstractDsConfigSpi implements DsConfigSpi {

    private static final List<String> TEXT_CERTIFICATE_TYPES   = List.of("pem", "key", "crt", "cer");
    private static final List<String> BINARY_CERTIFICATE_TYPES = List.of("pem", "key", "crt", "cer", "pk8", "p7b", "p12", "pfx", "jks");
    private static final List<String> KEYSTORE_TYPES           = List.of("p12", "pfx", "jks");

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
    }

    @Override
    public List<String> certificateTextFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.TRUSTSTORE || sslMode == SslMode.KEYSTORE_TRUSTSTORE) {
            return List.of();
        }
        return TEXT_CERTIFICATE_TYPES;
    }

    @Override
    public List<String> certificateBinaryFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.TRUSTSTORE || sslMode == SslMode.KEYSTORE_TRUSTSTORE) {
            return KEYSTORE_TYPES;
        }
        return BINARY_CERTIFICATE_TYPES;
    }

    @Override
    public Map<String, String> configMapFromUi(Map<String, String> configMap, Map<String, String> uiMap) {
        return Map.of();
    }

    @Override
    public void customizeUiMap(Map<String, String> uiMap, Map<String, String> configMap) {
        String host = configMap.get(DataSourceConfig.Fields.host);
        if (StringUtils.isBlank(host)) {
            return;
        }
        int index = host.lastIndexOf(':');
        if (host.contains("://") || index <= 0 || index == host.length() - 1 || host.indexOf(':') != index) {
            uiMap.put(ADDRESS_FIELD, host);
        } else {
            uiMap.put(ADDRESS_FIELD, host.substring(0, index));
            uiMap.put(PORT_FIELD, host.substring(index + 1));
        }
    }
}
