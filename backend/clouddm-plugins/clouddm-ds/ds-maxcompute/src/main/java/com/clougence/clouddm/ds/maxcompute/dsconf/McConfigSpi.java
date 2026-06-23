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
package com.clougence.clouddm.ds.maxcompute.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class McConfigSpi implements DsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return McConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        McConfig config = (McConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(McConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(McConfig.Fields.soTimeoutSec), false);
        config.setUserName(defaultConfig.get(DataSourceConfig.Fields.userName));
        config.setPassword(defaultConfig.get(DataSourceConfig.Fields.password));
        config.setSdkEndpoint(defaultConfig.get(McConfig.Fields.sdkEndpoint));
        config.setDefaultCatalog(defaultConfig.get(McConfig.Fields.defaultCatalog));
        config.setDefaultSchema(defaultConfig.get(McConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(defaultConfig.get(McConfig.Fields.clientTimeZone));
        config.setInteractiveMode(ConvertUtils.toBoolean(defaultConfig.get(McConfig.Fields.interactiveMode), false));

        boolean blank = StringUtils.isBlank(defaultConfig.get(McConfig.Fields.schemaStyle));
        config.setSchemaStyle((blank ? Boolean.FALSE : ConvertUtils.toBoolean(defaultConfig.get(McConfig.Fields.schemaStyle), false)));
        return dsConfig;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.AK_SK);
        return options;
    }
}
