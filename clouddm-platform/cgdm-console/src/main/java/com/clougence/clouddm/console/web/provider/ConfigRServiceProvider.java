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
package com.clougence.clouddm.console.web.provider;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.console.web.component.detectrule.SecCheckerRules;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmToolConfigService;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.entry.EnvCacheEntry;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsBlobResourceDO;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class ConfigRServiceProvider extends AbstractBasicProvider implements ConfigRService {

    @Resource
    private DataSourceDal        dsDal;
    @Resource
    private DmDsConfigService    dsConfigService;
    @Resource
    private DmToolConfigService  toolConfigService;
    @Resource
    private SecRulesService      secRulesService;
    @Resource
    private ConsoleConfigService consoleConfigService;

    @Override
    public List<ConfigData> fetchSettings(String ownerUid, List<String> names) {
        return this.consoleConfigService.fetchSettings(ownerUid, names);
    }

    @Override
    public DataSourceConfig fetchDsConfig(long dsId, DataSourceType dsType) {
        return this.dsConfigService.fetchDsConfigFromDM(dsId, dsType);
    }

    @Override
    public ToolConfig fetchToolConfig(String toolName) {
        return this.toolConfigService.fetchToolConfig(toolName);
    }

    @Override
    public SensitiveConfig fetchSensitiveConfigByDs(long dsId) {
        SecCheckerRules rules = this.secRulesService.fetchCheckerRulesByDsId(dsId);
        if (!rules.isValid() || CollectionUtils.isEmpty(rules.getSenRuleList())) {
            return null;
        } else {
            EnvCacheEntry envCache = this.cacheDao.queryByEnvId(rules.getEnvId());
            SensitiveConfig config = new SensitiveConfig();
            config.setEnvId(envCache.getEnvNumId());
            config.setEnvName(envCache.getEnvName());
            config.setDsId(rules.getDsId());
            config.setDsName(rules.getDsName());
            config.setDsType(rules.getDsType());
            config.setDsUseSpecName(rules.getDsUseSpecName());
            config.setSenRuleList(rules.getSenRuleList());
            return config;
        }
    }

    @Override
    public byte[] fetchDsFile(String instanceId, ResourceType resourceType, SecurityFileType fileType) {
        DmDsBlobResourceDO rdpBlobResourceDO = dsDal.blobResourceMapper().queryByIdentify(instanceId, resourceType, fileType);
        return rdpBlobResourceDO.getContent();
    }
}
