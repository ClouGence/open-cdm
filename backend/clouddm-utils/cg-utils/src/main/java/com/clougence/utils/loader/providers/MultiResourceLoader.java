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
package com.clougence.utils.loader.providers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.Manifest;

import com.clougence.utils.loader.AbstractResourceLoader;
import com.clougence.utils.loader.ResourceLoader;

/**
 * 可以将不同的 ResourceLoader 汇聚到一起，不具备 '可见性' 控制。实现更加简单、效率更高。
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public class MultiResourceLoader extends AbstractResourceLoader {

    private final List<ResourceLoader> loaders = new CopyOnWriteArrayList<>();

    /**
     * Creates a new multi resource Loader that will use the specified loaders.
     * @param loaders the loaders that are used to load resources.
     */
    public MultiResourceLoader(ResourceLoader... loaders){
        if (loaders == null) {
            return;
        }
        for (ResourceLoader loader : loaders) {
            addLoader(loader);
        }
    }

    /**添加一个 {@link ResourceLoader} */
    public void addLoader(ResourceLoader loader) {
        if (loader == null) {
            return;
        }
        if (!this.loaders.contains(loader)) {
            this.loaders.add(loader);
        }
    }

    protected ResourceLoader findLoader(String resource) {
        for (ResourceLoader loads : this.loaders) {
            if (loads != null && loads.exist(resource)) {
                return loads;
            }
        }
        return null;
    }

    @Override
    public URL getResource(String resource) throws IOException {
        ResourceLoader loader = findLoader(resource);
        return loader == null ? null : loader.getResource(resource);
    }

    @Override
    public InputStream getResourceAsStream(String resource) throws IOException {
        ResourceLoader loader = findLoader(resource);
        return loader == null ? null : loader.getResourceAsStream(resource);
    }

    @Override
    public List<URL> getResources(String resource) throws IOException {
        List<URL> result = new ArrayList<>();
        for (ResourceLoader loads : this.loaders) {
            List<URL> resources = loads.getResources(resource);
            result.addAll(resources);
        }
        return result;
    }

    @Override
    public List<InputStream> getResourcesAsStream(String resource) throws IOException {
        List<InputStream> result = new ArrayList<>();
        for (ResourceLoader loads : this.loaders) {
            List<InputStream> resources = loads.getResourcesAsStream(resource);
            result.addAll(resources);
        }
        return result;
    }

    @Override
    public boolean exist(String resource) {
        return findLoader(resource) != null;
    }

    @Override
    public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        for (ResourceLoader loads : this.loaders) {
            List<T> resources = loads.scanResources(matchType, scanner, scanPaths);
            result.addAll(resources);
        }
        return result;
    }

    @Override
    public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        for (ResourceLoader loads : this.loaders) {
            T resources = loads.scanOneResource(matchType, scanner, scanPaths);
            if (resources != null) {
                return resources;
            }
        }
        return null;
    }

    @Override
    public Manifest getManifest(String resource) throws IOException {
        ResourceLoader loader = findLoader(resource);
        return loader == null ? null : loader.getManifest(resource);
    }
}
