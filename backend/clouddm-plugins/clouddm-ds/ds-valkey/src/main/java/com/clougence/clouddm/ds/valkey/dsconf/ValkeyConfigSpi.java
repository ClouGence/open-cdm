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
package com.clougence.clouddm.ds.valkey.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class ValkeyConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "6379";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return ValkeyConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        ValkeyConfig config = (ValkeyConfig) dsConfig;
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(ValkeyConfig.Fields.soTimeoutSec), false);
        config.setDefaultSchema(defaultConfig.get(ValkeyConfig.Fields.defaultSchema));
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);

        boolean blank = StringUtils.isBlank(defaultConfig.get(ValkeyConfig.Fields.connAndSoTimeoutMs));
        config.setConnAndSoTimeoutMs((blank ? 5000 : ConvertUtils.toInteger(defaultConfig.get(ValkeyConfig.Fields.connAndSoTimeoutMs), false)));
        return dsConfig;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.ONLY_PASSWD);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.TRUST, SslMode.CA, SslMode.TRUSTSTORE, SslMode.KEYSTORE_TRUSTSTORE, SslMode.CLIENT_CERT);
    }

    @Override
    public List<String> certificateTextFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.CA || sslMode == SslMode.CLIENT_CERT) {
            if (DataSourceConfig.Fields.sslClientKeyData.equals(configName)) {
                return List.of("pem", "key");
            }
            return List.of("pem", "crt", "cer");
        }
        return super.certificateTextFileTypes(sslMode, configName);
    }

    @Override
    public List<String> certificateBinaryFileTypes(SslMode sslMode, String configName) {
        if (sslMode == SslMode.CA || sslMode == SslMode.CLIENT_CERT) {
            if (DataSourceConfig.Fields.sslClientKeyData.equals(configName)) {
                return List.of("pk8", "der");
            }
            return List.of("der", "crt", "cer", "p7b");
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
        return true;
    }
}
