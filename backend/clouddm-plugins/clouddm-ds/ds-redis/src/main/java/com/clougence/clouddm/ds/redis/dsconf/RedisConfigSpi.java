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
package com.clougence.clouddm.ds.redis.dsconf;

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

public class RedisConfigSpi extends AbstractDsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return RedisConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        RedisConfig config = (RedisConfig) dsConfig;
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(RedisConfig.Fields.soTimeoutSec), false);
        config.setDefaultSchema(defaultConfig.get(RedisConfig.Fields.defaultSchema));
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);

        boolean blank = StringUtils.isBlank(defaultConfig.get(RedisConfig.Fields.connAndSoTimeoutMs));
        config.setConnAndSoTimeoutMs((blank ? 5000 : ConvertUtils.toInteger(defaultConfig.get(RedisConfig.Fields.connAndSoTimeoutMs), false)));
        return dsConfig;
    }

    @Override
    public void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels) {
        setDefaultPort(panels, "6379");
    }

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
        options.add(SecurityType.ONLY_PASSWD);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }
}
