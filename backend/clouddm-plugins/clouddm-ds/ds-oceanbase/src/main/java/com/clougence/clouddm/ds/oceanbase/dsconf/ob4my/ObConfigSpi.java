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
package com.clougence.clouddm.ds.oceanbase.dsconf.ob4my;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.ds.oceanbase.i18n.ObConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class ObConfigSpi extends AbstractDsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return ObConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        ObConfig config = (ObConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(ObConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(ObConfig.Fields.soTimeoutSec), false);
        config.setTenant(defaultConfig.get(ObConfig.Fields.tenant));
        config.setCluster(defaultConfig.get(ObConfig.Fields.cluster));
        config.setDefaultSchema(defaultConfig.get(ObConfig.Fields.defaultSchema));
        config.setAutoCommit(!"false".equalsIgnoreCase(defaultConfig.get(ObConfig.Fields.autoCommit)));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(StringUtils.defaultIfBlank(defaultConfig.get(ObConfig.Fields.clientTimeZone), "Asia/Shanghai"));
        config.setConnectionCharset(StringUtils.defaultIfBlank(defaultConfig.get(ObConfig.Fields.connectionCharset), "utf8"));
        return dsConfig;
    }

    @Override
    public void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels) {
        setDefaultPort(panels, "2881");
        UiPanel general = panels.get(DsConfigGroup.GENERAL);

        UiPanelField tenant = general.findField(ObConfig.Fields.tenant);
        if (tenant != null) {
            tenant.setTitleI18N(ObConfigI18nKeys.CONFIG_OCEANBASE_TENANT_LABEL);
            tenant.setDescI18N(ObConfigI18nKeys.CONFIG_OCEANBASE_TENANT_DESCRIPTION);
        }

        UiPanelField cluster = general.findField(ObConfig.Fields.cluster);
        if (cluster != null) {
            cluster.setTitleI18N(ObConfigI18nKeys.CONFIG_OCEANBASE_CLUSTER_LABEL);
            cluster.setDescI18N(ObConfigI18nKeys.CONFIG_OCEANBASE_CLUSTER_DESCRIPTION);
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
