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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SshConfig;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.console.web.component.detectrule.SecCheckerRules;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmToolConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.service.ssh.SshConfigService;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.entry.EnvCacheEntry;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

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
    @Resource
    private SshConfigService     sshConfigService;

    @Override
    public List<ConfigData> fetchSettings(List<String> names) {
        return this.consoleConfigService.fetchSettings(names);
    }

    @Override
    public DataSourceConfig fetchDsConfig(long dsId) {
        return this.dsConfigService.fetchDsConfigFromExists(dsId);
    }

    @Override
    public List<ConfigData> fetchDsConfig(String instanceId, List<String> names) {
        if (StringUtils.isBlank(instanceId) || CollectionUtils.isEmpty(names)) {
            return Collections.emptyList();
        }

        DmDsDO dsDO = this.dsDal.dsMapper().getByInstanceId(instanceId);
        if (dsDO == null) {
            return Collections.emptyList();
        }

        List<DmDsConfigKv4DmDO> configs = this.dsDal.configKv4DmMapper().listByDsIdAndConfigNames(dsDO.getId(), names);
        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyList();
        }

        Map<String, DsConfigKvDef> configDefMap = this.dsConfigService.fetchDsConfigDef(dsDO.getDataSourceType())//
            .stream()
            .collect(Collectors.toMap(DsConfigKvDef::getConfigName, configDef -> configDef));

        return configs.stream().map(config -> {
            ConfigData result = new ConfigData();
            result.setConfigName(config.getConfigName());
            String configValue = config.getConfigValue();
            DsConfigKvDef configDef = configDefMap.get(config.getConfigName());
            if (configDef != null && configDef.isSecret() && StringUtils.isNotBlank(configValue)) {
                configValue = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configValue);
            }
            result.setConfigValue(configValue);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public SshConfig fetchSshConfig(long sshConfigId) {
        return this.sshConfigService.fetchSshConfig(sshConfigId);
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

}
