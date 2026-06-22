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
package com.clougence.clouddm.init.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.component.file.PluginResourceManager;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitWebsitePluginLoader {

    private static final String INNER_WEBSITE_PLUGIN_FILE_NAME = "inner-website-lib.jar";

    public void loadPlugin(ClassLoader parentClassLoader) {
        File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
        File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
        int loadedCount = PluginLoadHelper.loadPlugins(parentClassLoader, INNER_WEBSITE_PLUGIN_FILE_NAME, pluginPath1, pluginPath2);
        if (loadedCount > 0) {
            PluginResourceManager.refreshIndex();
            log.info("[InitWebsitePluginLoader] Loaded inner website plugin: {}", INNER_WEBSITE_PLUGIN_FILE_NAME);
            return;
        }
        log.warn("[InitWebsitePluginLoader] Inner website plugin was not found; favicon.ico may be unavailable.");
    }
}
