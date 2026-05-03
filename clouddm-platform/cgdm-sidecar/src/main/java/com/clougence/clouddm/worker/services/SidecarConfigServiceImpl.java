package com.clougence.clouddm.worker.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.config.ConfigData;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;

@Service
public class SidecarConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigRService configRService;

    @Override
    public List<ConfigData> fetchSettings(String ownerUid, List<String> names) {
        return this.configRService.fetchSettings(ownerUid, names);
    }

    @Override
    public DataSourceConfig fetchDsConfig(long dsId, DataSourceType dsType) {
        return this.configRService.fetchDsConfig(dsId, dsType);
    }

    @Override
    public ToolConfig fetchToolConfig(String toolName) {
        return this.configRService.fetchToolConfig(toolName);
    }

    @Override
    public Dialect findDialectByDsType(DataSourceType dsType) {
        return PluginManager.findDsDialect(dsType);
    }

    @Override
    public SensitiveConfig fetchSensitiveConfigByDs(long dsId) {
        return this.configRService.fetchSensitiveConfigByDs(dsId);
    }

    @Override
    public List<UmiTypes> fetchDsLevelDef(DataSourceType dsType) {
        DsBrowseSpi browseSpi = PluginManager.findDsBrowseSpi(dsType);
        return browseSpi.getLevels();
    }
}
