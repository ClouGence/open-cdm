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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class OraConfigSpi implements DsConfigSpi {

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
            } else {
                throw new IllegalArgumentException("unsupported Oracle host format:" + config.getHost());
            }
        }

        config.setAutoCommit(!"false".equalsIgnoreCase(defaultConfig.get(OraConfig.Fields.autoCommit)));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);

        boolean excludeOraMaintainedSchemas = StringUtils.isBlank(defaultConfig.get(OraConfig.Fields.excludeOraMaintainedSchemas));
        config.setExcludeOraMaintainedSchemas((excludeOraMaintainedSchemas ? Boolean.FALSE : //
            ConvertUtils.toBoolean(defaultConfig.get(OraConfig.Fields.excludeOraMaintainedSchemas), false)));
        return dsConfig;
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public boolean supportSSH() {
        return true;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.NONE);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }
}
