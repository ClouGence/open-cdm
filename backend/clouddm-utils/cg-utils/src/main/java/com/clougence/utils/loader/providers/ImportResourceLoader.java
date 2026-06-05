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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;

import com.clougence.utils.loader.AbstractResourceLoader;
import com.clougence.utils.loader.ResourceLoader;

/**
 * 允许将 ResourceLoader 的中的一部分资源设置为 '可见'，并将不同的 ResourceLoader 之间 '可见' 部分汇聚为一个新的 ResourceLoader。
 * --
 * -- ExportResourceLoader 强调的是对外部分可见、ImportResourceLoader 强调的是按需汇聚。
 * @version : 2021-10-10
 * @author 赵永春 (zyc@hasor.net)
 */
public class ImportResourceLoader extends AbstractResourceLoader {

    private final Map<String, List<LoaderWrap>> packageLoaders;

    public ImportResourceLoader(){
        this.packageLoaders = new ConcurrentHashMap<>();
    }

    /** import all path */
    public void importResources(ResourceLoader loader) {
        this.importResources(0, "", loader);
    }

    /** import all path */
    public void importResources(int order, ResourceLoader loader) {
        this.importResources(order, "", loader);
    }

    /** import specified path or resource */
    public void importResources(String importPath, ResourceLoader loader) {
        this.importResources(0, importPath, loader);
    }

    /** import specified path or resource */
    public synchronized void importResources(int order, String importPath, ResourceLoader loader) {
        importPath = pathOrResource(importPath);
        List<LoaderWrap> loaders = this.packageLoaders.computeIfAbsent(importPath, s -> new ArrayList<>());
        LoaderWrap wrap = new LoaderWrap(order, loader);
        if (!loaders.contains(wrap)) {
            loaders.add(wrap);
        }
    }

    private static String pathOrResource(String dirPath) {
        dirPath = dirPath.replace("\\", "/");
        if (dirPath.length() > 0 && dirPath.charAt(0) == '/') {
            dirPath = dirPath.substring(1);
        }
        return dirPath;
    }

    public Set<String> getAllPackages() { return this.packageLoaders.keySet(); }

    protected Iterator<LoaderWrap> getAllLoader() { return this.packageLoaders.values().stream().flatMap(Collection::stream).iterator(); }

    private void mergeInto(List<LoaderWrap> form, List<LoaderWrap> into) {
        if (form == null || form.isEmpty()) {
            return;
        }

        for (LoaderWrap wrap : form) {
            if (!into.contains(wrap)) {
                into.add(wrap);
            }
        }
    }

    protected Iterator<LoaderWrap> getLoader(String resource) {
        String fmtResource = pathOrResource(resource);
        if (fmtResource.length() == 0) {
            return getAllLoader();
        }

        List<LoaderWrap> loaders = new ArrayList<>();

        for (String resourceItem : this.packageLoaders.keySet()) {
            if (resourceItem == null || !resource.startsWith(resourceItem)) {
                continue;
            }

            String tmpResourceItem = resourceItem;
            int index = tmpResourceItem.lastIndexOf("/");

            while (true) {
                mergeInto(this.packageLoaders.get(tmpResourceItem), loaders);

                index = tmpResourceItem.lastIndexOf("/", index);
                if (index == -1) {
                    mergeInto(this.packageLoaders.get(tmpResourceItem), loaders);
                    break;
                } else {
                    tmpResourceItem = tmpResourceItem.substring(0, index);
                }
            }
        }

        mergeInto(this.packageLoaders.get(""), loaders);
        Collections.sort(loaders);

        return loaders.iterator();
    }

    public ResourceLoader findLoader(String resource) {
        Iterator<LoaderWrap> loaders = getLoader(resource);
        while (loaders.hasNext()) {
            LoaderWrap wrap = loaders.next();
            if (wrap.loader.exist(resource)) {
                return wrap.loader;
            }
        }
        return null;
    }

    private <T> Scanner<T> scannerProxy(Scanner<T> scanner) {
        final Set<String> allPackages = this.getAllPackages();
        return event -> {
            for (String has : allPackages) {
                if (event.getName().startsWith(has)) {
                    return scanner.found(event);
                }
            }
            return null;
        };
    }

    @Override
    public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        Scanner<T> scannerProxy = scannerProxy(scanner);
        Iterator<LoaderWrap> loaders = getAllLoader();
        while (loaders.hasNext()) {
            T result = loaders.next().loader.scanOneResource(matchType, scannerProxy, scanPaths);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        Iterator<LoaderWrap> loaders = getAllLoader();
        Scanner<T> scannerProxy = scannerProxy(scanner);
        List<T> result = new ArrayList<>();

        while (loaders.hasNext()) {
            LoaderWrap loader = loaders.next();
            List<T> foundResources = loader.loader.scanResources(matchType, scannerProxy, scanPaths);
            if (foundResources != null) {
                result.addAll(foundResources);
            }
        }
        return result;
    }

    @Override
    public URL getResource(String resource) throws IOException {
        Iterator<LoaderWrap> loaders = getLoader(resource);
        while (loaders.hasNext()) {
            URL resourceURL = loaders.next().loader.getResource(resource);
            if (resourceURL != null) {
                return resourceURL;
            }
        }
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String resource) throws IOException {
        Iterator<LoaderWrap> loaders = getLoader(resource);
        while (loaders.hasNext()) {
            InputStream resourceStream = loaders.next().loader.getResourceAsStream(resource);
            if (resourceStream != null) {
                return resourceStream;
            }
        }
        return null;
    }

    @Override
    public List<URL> getResources(String resource) throws IOException {
        List<URL> result = new ArrayList<>();
        Iterator<LoaderWrap> loaders = getLoader(resource);
        while (loaders.hasNext()) {
            List<URL> resourceURLs = loaders.next().loader.getResources(resource);
            if (resourceURLs != null && !resourceURLs.isEmpty()) {
                result.addAll(resourceURLs);
            }
        }
        return result;
    }

    @Override
    public List<InputStream> getResourcesAsStream(String resource) throws IOException {
        List<InputStream> result = new ArrayList<>();
        Iterator<LoaderWrap> loaders = getLoader(resource);
        while (loaders.hasNext()) {
            List<InputStream> resourceStreams = loaders.next().loader.getResourcesAsStream(resource);
            if (resourceStreams != null && !resourceStreams.isEmpty()) {
                result.addAll(resourceStreams);
            }
        }
        return result;
    }

    @Override
    public boolean exist(String resource) {
        Iterator<LoaderWrap> loaders = getLoader(resource);
        while (loaders.hasNext()) {
            boolean result = loaders.next().loader.exist(resource);
            if (result) {
                return true;
            }
        }
        return false;
    }

    protected static class LoaderWrap implements Comparable<LoaderWrap> {

        int                  order;
        final ResourceLoader loader;

        public LoaderWrap(int order, ResourceLoader loader){
            this.order = order;
            this.loader = loader;
        }

        @Override
        public boolean equals(Object o) {
            return Objects.equals(o, this.loader);
        }

        @Override
        public int hashCode() {
            return this.loader.hashCode();
        }

        @Override
        public String toString() {
            return "{" + "order=" + order + ", loader=" + loader + '}';
        }

        @Override
        public int compareTo(LoaderWrap o) {
            return Integer.compare(this.order, o.order);
        }
    }

    @Override
    public Manifest getManifest(String resource) throws IOException {
        ResourceLoader loader = findLoader(resource);
        return loader == null ? null : loader.getManifest(resource);
    }
}
