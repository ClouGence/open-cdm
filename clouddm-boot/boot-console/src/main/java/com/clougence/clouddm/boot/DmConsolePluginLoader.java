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
package com.clougence.clouddm.boot;

import java.io.File;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.service.sdk.ConsoleCacheServiceImpl;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintService;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.execute.MetaService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmConsolePluginLoader {

    @Resource
    private ConsoleCacheServiceImpl cacheService;
    @Resource
    private ApprovalRefreshService  refreshService;
    @Resource
    private ConsoleConfigService    consoleConfigService;
    @Resource
    private MetaService             metaService;
    @Resource
    private QueryConstraintService  queryConstraintService;

    public void loadPlugin(ClassLoader parentClassLoader) throws Exception {
        this.cacheService.init();
        //PluginManager.putService(SessionService.class, this.sessionServices);
        PluginManager.putService(CacheService.class, this.cacheService);
        PluginManager.putService(MetaService.class, this.metaService);
        PluginManager.putService(QueryConstraintService.class, this.queryConstraintService);
        PluginManager.putService(ApprovalRefreshService.class, this.refreshService);
        PluginManager.putService(ConsoleConfigService.class, this.consoleConfigService);

        // load Plugins
        File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
        File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
        PluginLoadHelper.loadPlugins(parentClassLoader, pluginPath1, pluginPath2);
    }
}
