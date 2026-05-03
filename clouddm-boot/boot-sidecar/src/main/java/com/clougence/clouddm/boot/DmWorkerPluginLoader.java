package com.clougence.clouddm.boot;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.execute.SessionService;
import com.clougence.clouddm.worker.services.SidecarCacheServiceImpl;
import com.clougence.clouddm.worker.services.SidecarConfigServiceImpl;
import com.clougence.clouddm.worker.services.SidecarSessionServicesImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 12:21
 */
@Slf4j
@Service
public class DmWorkerPluginLoader {

    @Resource
    private SidecarSessionServicesImpl sessionServices;
    @Resource
    private SidecarCacheServiceImpl    cacheService;
    @Resource
    private SidecarConfigServiceImpl   configService;

    public void loadPlugin(ClassLoader parentClassLoader) throws Exception {
        // start service
        this.cacheService.init();
        PluginManager.putService(CacheService.class, this.cacheService);
        PluginManager.putService(SessionService.class, this.sessionServices);
        PluginManager.putService(ConfigService.class, this.configService);

        // load Plugins
        File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
        File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
        PluginLoadHelper.loadPlugins(parentClassLoader, pluginPath1, pluginPath2);
    }
}
