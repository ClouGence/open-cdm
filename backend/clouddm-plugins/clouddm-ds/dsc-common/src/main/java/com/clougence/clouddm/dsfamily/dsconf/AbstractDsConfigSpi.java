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

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public abstract class AbstractDsConfigSpi implements DsConfigSpi {

    protected void setDefaultPort(Map<DsConfigGroup, UiPanel> panels, String defaultPort) {
        UiPanel general = panels == null ? null : panels.get(DsConfigGroup.GENERAL);
        UiPanelField host = general == null ? null : general.findField(DataSourceConfig.Fields.host);
        UiPanelField port = host == null ? null : host.findField(DsConfigSpi.PORT_FIELD);
        if (port != null) {
            port.setDefaultValue(UiUtils.strValueDef(defaultPort));
        }
    }
}
