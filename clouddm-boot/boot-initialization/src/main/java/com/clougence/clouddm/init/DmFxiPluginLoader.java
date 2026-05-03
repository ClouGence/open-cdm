package com.clougence.clouddm.init;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.service.sdk.ConsoleCacheServiceImpl;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintService;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.execute.MetaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmFxiPluginLoader {

    @Resource
    private ConsoleCacheServiceImpl   cacheService;
    @Resource
    private RdpApprovalConsoleService rdpApprovalService;
    @Resource
    private ConsoleConfigService      rdpConfigService;
    @Resource
    private MetaService               metaService;
    @Resource
    private QueryConstraintService    queryConstraintService;

    public void loadPlugin(ClassLoader parentClassLoader) throws Exception {
        this.cacheService.init();
        PluginManager.putService(CacheService.class, this.cacheService);
        PluginManager.putService(MetaService.class, this.metaService);
        PluginManager.putService(QueryConstraintService.class, this.queryConstraintService);
        PluginManager.putService(RdpApprovalConsoleService.class, this.rdpApprovalService);
        PluginManager.putService(ConsoleConfigService.class, this.rdpConfigService);

        // load Plugins
        File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
        File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
        PluginLoadHelper.loadPlugins(parentClassLoader, pluginPath1, pluginPath2);
    }
}
