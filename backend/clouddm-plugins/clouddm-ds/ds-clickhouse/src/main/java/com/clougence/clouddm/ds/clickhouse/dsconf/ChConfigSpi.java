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
package com.clougence.clouddm.ds.clickhouse.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class ChConfigSpi extends AbstractDsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return ChConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        ChConfig config = (ChConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(ChConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(ChConfig.Fields.soTimeoutSec), false);
        config.setDefaultSchema(defaultConfig.get(ChConfig.Fields.defaultSchema));
        config.setAutoCommit(!"false".equalsIgnoreCase(defaultConfig.get(ChConfig.Fields.autoCommit)));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setSessionTimeout(StringUtils.defaultIfBlank(defaultConfig.get(ChConfig.Fields.sessionTimeout), "15000"));
        config.setClientTimeZone(defaultConfig.get(ChConfig.Fields.clientTimeZone));
        return dsConfig;
    }

    @Override
    public void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels) {
        setDefaultPort(panels, "8123");
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
