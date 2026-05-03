package com.clougence.clouddm.console.web.provider;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.console.web.component.auth.model.EnvCacheEntry;
import com.clougence.clouddm.console.web.component.detectrule.SecCheckerRules;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmToolConfigService;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.rdp.dal.mapper.RdpBlobResourceMapper;
import com.clougence.rdp.dal.model.RdpBlobResourceDO;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class ConfigRServiceProvider extends AbstractBasicProvider implements ConfigRService {

    @Resource
    private DmDsConfigService       dsConfigService;
    @Resource
    private DmToolConfigService     toolConfigService;
    @Resource
    private SecRulesService         secRulesService;
    @Resource
    private RdpBlobResourceMapper blobResourceMapper;
    @Resource
    private ConsoleConfigService  consoleConfigService;

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
            EnvCacheEntry envCache = this.ownerCacheService.queryByEnvId(rules.getEnvId());
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
        RdpBlobResourceDO rdpBlobResourceDO = blobResourceMapper.queryByIdentify(instanceId, resourceType, fileType);
        return rdpBlobResourceDO.getContent();
    }
}
