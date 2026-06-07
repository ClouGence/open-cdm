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
package com.clougence.clouddm.console.web.component.file;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.file.resource.DefaultPluginResourceModel;
import com.clougence.clouddm.console.web.component.file.resource.PluginResourceModel;
import com.clougence.clouddm.console.web.component.file.resource.ResourceRoute;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.resource.ResourceCategory;
import com.clougence.clouddm.sdk.resource.ResourceSpi;
import com.clougence.utils.StringUtils;
import com.clougence.utils.loader.ResourceLoader;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

public class PluginResourceManager {

    private static final Map<String, List<PluginResourceDeclaration>> RESOURCE_INDEX = new ConcurrentHashMap<>();
    private static final Object                                       INDEX_LOCK     = new Object();
    private static volatile boolean                                   indexReady     = false;

    private PluginResourceManager(){
    }

    public static PluginResourceModel findResource(String resourceName) {
        ResourceRoute route = ResourceRoute.parse(resourceName);
        refreshIndexIfNecessary();

        List<PluginResourceDeclaration> declarations = RESOURCE_INDEX.get(indexKey(route.getCategory(), route.getModule()));
        if (declarations == null || declarations.isEmpty()) {
            refreshIndex();
            declarations = RESOURCE_INDEX.get(indexKey(route.getCategory(), route.getModule()));
        }
        if (declarations == null || declarations.isEmpty()) {
            return null;
        }

        PluginResourceDeclaration declaration = firstAvailable(declarations);
        return new DefaultPluginResourceModel(route
            .getQualifiedResourceName(), route.getCategory(), route.getModule(), route.getResourcePath(), declaration.resourceSpi, declaration.resourceLoader, cachePath(route));
    }

    public static void refreshIndex() {
        synchronized (INDEX_LOCK) {
            RESOURCE_INDEX.clear();
            for (String resourceModule : PluginManager.getSpiNamesByType(ResourceSpi.class)) {
                ResourceSpi spi = PluginManager.findSpi(ResourceSpi.class, resourceModule);
                registerResourceSpi(resourceModule, spi);
            }
            for (DataSourceType dsType : DataSourceType.values()) {
                DsPluginInfo dsPlugin = PluginManager.findDsPlugin(dsType);
                if (dsPlugin == null) {
                    continue;
                }
                for (ResourceSpi spi : dsPlugin.findSpi(ResourceSpi.class)) {
                    registerResourceSpi(spi);
                }
            }
            indexReady = true;
        }
    }

    private static void registerResourceSpi(ResourceSpi spi) {
        String module = StringUtils.isBlank(spi.name()) ? ResourceRoute.DEFAULT_MODULE : spi.name();
        registerResourceSpi(module, spi);
    }

    private static void registerResourceSpi(String module, ResourceSpi spi) {
        module = StringUtils.isBlank(module) ? ResourceRoute.DEFAULT_MODULE : module;
        ResourceLoader loader = new ClassPathResourceLoader(spi.getClass().getClassLoader(), "");
        PluginResourceDeclaration declaration = new PluginResourceDeclaration(spi, loader);
        for (ResourceCategory category : ResourceCategory.values()) {
            RESOURCE_INDEX.computeIfAbsent(indexKey(category.getCode(), module), key -> new ArrayList<>()).add(declaration);
        }
    }

    private static void refreshIndexIfNecessary() {
        if (!indexReady) {
            refreshIndex();
        }
    }

    private static PluginResourceDeclaration firstAvailable(List<PluginResourceDeclaration> declarations) {
        return declarations.get(0);
    }

    private static String indexKey(String category, String module) {
        return category + "/" + module;
    }

    private static Path cachePath(ResourceRoute route) {
        File cacheRoot = new File(GlobalConfUtils.getAppDataHome(), "cache/plugin-resources");
        return cacheRoot.toPath().resolve(route.getCategory()).resolve(route.getModule()).resolve(route.getResourcePath()).normalize();
    }

    private record PluginResourceDeclaration(ResourceSpi resourceSpi, ResourceLoader resourceLoader) {
    }
}
