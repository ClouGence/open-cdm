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
package com.clougence.clouddm.ds.oceanbase.dsconf.ob4ora;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class ObForOraConfigSpi extends AbstractDsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return ObOraConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        ObOraConfig config = (ObOraConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(ObOraConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(ObOraConfig.Fields.soTimeoutSec), false);
        config.setTenant(defaultConfig.get(ObOraConfig.Fields.tenant));
        config.setCluster(defaultConfig.get(ObOraConfig.Fields.cluster));
        config.setDefaultSchema(defaultConfig.get(ObOraConfig.Fields.defaultSchema));
        config.setAutoCommit(!"false".equalsIgnoreCase(defaultConfig.get(ObOraConfig.Fields.autoCommit)));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(StringUtils.defaultIfBlank(defaultConfig.get(ObOraConfig.Fields.clientTimeZone), "Asia/Shanghai"));
        config.setConnectionCharset(StringUtils.defaultIfBlank(defaultConfig.get(ObOraConfig.Fields.connectionCharset), "utf8"));
        config.setUseCursorFetch(ConvertUtils.toBoolean(defaultConfig.get(ObOraConfig.Fields.useCursorFetch), false));
        return dsConfig;
    }

    @Override
    public void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        if (general == null) {
            return;
        }

        setDefaultPort(panels, "1521");

        UiPanelField tenant = general.findField(ObOraConfig.Fields.tenant);
        if (tenant != null) {
            tenant.setTitleI18N(ConfigI18nKey.CONFIG_OCEANBASE_TENANT_LABEL.name());
            tenant.setDescI18N(ConfigI18nKey.CONFIG_OCEANBASE_TENANT_DESCRIPTION.name());
        }

        UiPanelField cluster = general.findField(ObOraConfig.Fields.cluster);
        if (cluster != null) {
            cluster.setTitleI18N(ConfigI18nKey.CONFIG_OCEANBASE_CLUSTER_LABEL.name());
            cluster.setDescI18N(ConfigI18nKey.CONFIG_OCEANBASE_CLUSTER_DESCRIPTION.name());
        }
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
        options.add(SecurityType.USER_PASSWD);
        return options;
    }
}
