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
package com.clougence.rdp.component.dskvconfig.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsConfigService;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.RdpDsKvConfigHelper;
import com.clougence.rdp.component.dskvconfig.RdpDsResourceService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

/**
 * @author bucketli 2022/8/10 09:52:22
 */
@Service
public class RdpDsConfigServiceImpl implements RdpDsConfigService {

    @Resource
    private DataSourceDal        datasourceDal;

    @Resource
    private RdpDsKvConfigHelper  rdpDsKvConfigHelper;

    @Resource
    private RdpDsResourceService rdpDsResourceService;

    @Override
    public void persistDsConfig(DmDsDO dataSourceDO, List<InitDsKvBaseConfigFO> kvConfigs) {
        List<DmDsConfigKv4RdpDO> configs = collectConfig(dataSourceDO, kvConfigs);
        for (DmDsConfigKv4RdpDO config : configs) {
            if (config.isSecret()) {
                config.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(config.getConfigValue()));
            }

            this.datasourceDal.configKv4RdpMapper().insert(config);
        }
    }

    @Override
    public List<DmDsConfigKv4RdpDO> fetchDefaultConfig(long dataSourceId, DataSourceType dataSourceType) {
        RdpDsExtraConfGen dsConfigOperate = this.rdpDsResourceService.getDsExtraConfGen(dataSourceType);
        if (dsConfigOperate == null) {
            return new ArrayList<>();
        }

        DsExtraConfig extraConfig = dsConfigOperate.newDsExtraConfigForDefaultVal(dataSourceType);
        return this.rdpDsKvConfigHelper.collectConfigs(extraConfig, dataSourceId, dataSourceType);
    }

    @Override
    public DsExtraConfig fetchDsExtraConfig(long dataSourceId, DataSourceType dataSourceType) {
        RdpDsExtraConfGen dsConfigOperate = this.rdpDsResourceService.getDsExtraConfGen(dataSourceType);
        if (dsConfigOperate == null) {
            return null;
        }

        List<DmDsConfigKv4RdpDO> source = this.datasourceDal.configKv4RdpMapper().listByDsId(dataSourceId);

        Map<String, String> configMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(source)) {
            for (DmDsConfigKv4RdpDO configDO : source) {
                configMap.put(configDO.getConfigName(), configDO.getConfigValue());
            }
        }

        DsExtraConfig config = dsConfigOperate.newDsExtraConfig();
        this.rdpDsKvConfigHelper.fillFieldValue(config, configMap);
        return config;
    }

    @Override
    public DmDsConfigKv4RdpDO getSpecifiedConfig(long dataSourceId, String configName) {
        DmDsConfigKv4RdpDO configDO = this.datasourceDal.configKv4RdpMapper().queryByDsIdAndConfigName(dataSourceId, configName);
        if (configDO != null && configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
            String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
            configDO.setConfigValue(val);
        }

        return configDO;
    }

    @Override
    public void cleanDsConfig(long dataSourceId) {
        this.datasourceDal.configKv4RdpMapper().deleteDsConfigs(dataSourceId);
    }

    protected List<DmDsConfigKv4RdpDO> collectConfig(DmDsDO dataSourceDO, List<InitDsKvBaseConfigFO> kvConfigs) {
        RdpDsExtraConfGen dsConfigOperate = this.rdpDsResourceService.getDsExtraConfGen(dataSourceDO.getDataSourceType());
        if (dsConfigOperate == null) {
            return new ArrayList<>();
        }

        DsExtraConfig extraConfig = dsConfigOperate.genDsExtraConfig(dataSourceDO, kvConfigs);
        return this.rdpDsKvConfigHelper.collectConfigs(extraConfig, dataSourceDO.getId(), dataSourceDO.getDataSourceType());
    }
}
