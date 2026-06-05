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
package com.clougence.rdp.component.dskvconfig;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;

/**
 * @author bucketli 2020/11/7 14:25
 */
public interface RdpDsConfigService {

    void persistDsConfig(DmDsDO dataSourceDO, List<InitDsKvBaseConfigFO> kvConfigs);

    List<DmDsConfigKv4RdpDO> fetchDefaultConfig(long dataSourceId, DataSourceType dataSourceType);

    DsExtraConfig fetchDsExtraConfig(long dataSourceId, DataSourceType dataSourceType);

    DmDsConfigKv4RdpDO getSpecifiedConfig(long dataSourceId, String configName);

    void cleanDsConfig(long dataSourceId);

}
