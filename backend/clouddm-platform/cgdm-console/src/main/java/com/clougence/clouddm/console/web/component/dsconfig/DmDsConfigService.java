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
package com.clougence.clouddm.console.web.component.dsconfig;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;

/**
 * @author bucketli 2020/11/7 14:25
 */
public interface DmDsConfigService {

    Map<String, String> fetchSettingsMap(List<String> names);

    String fetchDsConfig(long dsId, String configKey);

    void cleanDsConfig(long dsId);

    DataSourceConfig fetchDsConfigFromNotExist(DmDsDO dsDO, Map<String, String> configMap);

    DataSourceConfig fetchDsConfigFromExists(long dsId);

    List<DsConfigKvDef> fetchDsConfigDef(DataSourceType dsType);

    List<DsConfigKvDef> fetchDsConfigDef(DataSourceType dsType, Map<String, String> defaultConfig);

    List<UiPanel> fetchDsConfigPanels(DataSourceType dsType);

    Map<DataSourceType, DsConfig> dsConstantSettings();

    DsConfig dsConstantSettings(DataSourceType dsType);

    DsLevels parseLevels(List<String> levels);

    /** levels need is `/ssss/sss` path */
    DsLevels parseLevels(String levels);
}
