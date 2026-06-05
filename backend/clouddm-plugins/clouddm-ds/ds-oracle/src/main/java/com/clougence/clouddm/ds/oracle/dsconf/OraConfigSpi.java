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

import java.util.Map;
import java.util.Objects;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.utils.StringUtils;

public class OraConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new OraConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        OraConfig config = (OraConfig) dsConfig;
        ConnectType connectType = (ConnectType) dsConfigMap.getRdpDsBean().get(RDP_DS_KEY_CONNECT_TYPE);
        config.setConnectType(OraConnectType.valueOfCode(connectType));
        fillConnectionInfo(config, config.getHost(), connectType);
        return dsConfig;
    }

    private void fillConnectionInfo(OraConfig config, String host, ConnectType connectType) {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("DataSource host can not be empty.");
        }

        config.setConnectType(Objects.requireNonNull(OraConnectType.valueOfCode(connectType), "unsupported Oracle connect type:" + connectType));
        String[] ipPort = host.split(":");

        if (ipPort.length == 3) {
            switch (config.getConnectType()) {
                case SID:
                    config.setSid(ipPort[2]);
                    break;
                case SERVICE:
                case PDB:
                    config.setServiceName(ipPort[2]);
                    break;
                default:
                    throw new IllegalArgumentException("unsupported Oracle connect type:" + connectType);
            }
        } else {
            throw new IllegalArgumentException("unsupported Oracle host format:" + host);
        }
    }

}
