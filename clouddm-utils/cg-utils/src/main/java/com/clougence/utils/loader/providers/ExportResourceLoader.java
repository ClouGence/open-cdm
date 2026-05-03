/*
 * Copyright 2008-2009 the original author or authors.
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
import java.util.jar.Manifest;

import com.clougence.utils.loader.AbstractResourceLoader;
import com.clougence.utils.loader.ResourceLoader;

/**
 * 仅允许将 ResourceLoader 的中的一部分资源设置为 '可见'
 *
 * -- 与 ImportResourceLoader 不同的是 ExportResourceLoader 是对某个特定 ResourceLoader 的增强。
 * -- 可以通过 setExportAll 方法开放所有资源被访问。
 * @version : 2021-10-10
 * @author 赵永春 (zyc@hasor.net)
 */
public class ExportResourceLoader extends AbstractResourceLoader {

    private boolean              exportAll     = false;
    private final Set<String>    exportPathSet = new HashSet<>();
    private final ResourceLoader resourceLoader;

    public ExportResourceLoader(ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }

    public boolean isExportAll() { return this.exportAll; }

    public void setExportAll(boolean exportAll) { this.exportAll = exportAll; }

    public void addExport(String exportResources) {
        if (exportResources != null && exportResources.length() > 0) {
            if (exportResources.charAt(exportResources.length() - 1) != '/') {
                exportResources = exportResources + '/';
            }
            this.exportPathSet.add(fmtDirPath(exportResources));
        }
    }

    public Set<String> getExportSet() { return this.exportPathSet; }

    private static String fmtDirPath(String dirPath) {
        if (dirPath.startsWith("/")) {
            dirPath = dirPath.substring(1);
        }
        // Looking for org/codehaus/werkflow/personality/basic/../common/core-idioms.xml
        //                                               |    i  |
        //                                               +-------+ remove
        int i = dirPath.indexOf("/..");
        // Can't be at the beginning because we have no root to refer to so we start at 1.
        if (i > 0) {
            int j = dirPath.lastIndexOf("/", i - 1);
            if (j >= 0) {
                dirPath = dirPath.substring(0, j) + dirPath.substring(i + 3);
            }
        }

        int j = dirPath.lastIndexOf("/");
        if (j > 0) {
            dirPath = dirPath.substring(0, j);
        }

        dirPath = dirPath.replace("\\", "/");
        if (dirPath.length() > 0 && dirPath.charAt(0) == '/') {
            dirPath = dirPath.substring(1);
        }
        return dirPath;
    }

    protected boolean testPath(String dirPath) {
        if (this.exportAll) {
            return true;
        } else if (this.exportPathSet.isEmpty()) {
            return false;
        }

        dirPath = fmtDirPath(dirPath);
        if (this.exportPathSet.contains(dirPath)) {
            return true;
        }
        int index = dirPath.length();
        while (true) {
            index = dirPath.lastIndexOf("/", index);
            if (index == -1) {
                break;
            }
            dirPath = dirPath.substring(0, index);
            if (this.exportPathSet.contains(dirPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        if (scanPaths.length == 0) {
            scanPaths = exportPathSet.toArray(new String[0]);
        } else {
            scanPaths = Arrays.stream(scanPaths).filter(this::testPath).toArray(String[]::new);
        }
        if (scanPaths.length == 0) {
            return Collections.emptyList();
        }
        return this.resourceLoader.scanResources(matchType, scanner, scanPaths);
    }

    @Override
    public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        if (scanPaths.length == 0) {
            scanPaths = exportPathSet.toArray(new String[0]);
        } else {
            scanPaths = Arrays.stream(scanPaths).filter(this::testPath).toArray(String[]::new);
        }
        if (scanPaths.length == 0) {
            return null;
        }
        return this.resourceLoader.scanOneResource(matchType, scanner, scanPaths);
    }

    @Override
    public URL getResource(String resource) throws IOException {
        if (!testPath(resource)) {
            return null;
        }
        return this.resourceLoader.getResource(resource);
    }

    @Override
    public InputStream getResourceAsStream(String resource) throws IOException {
        if (!testPath(resource)) {
            return null;
        }
        return this.resourceLoader.getResourceAsStream(resource);
    }

    @Override
    public List<URL> getResources(String resource) throws IOException {
        if (!testPath(resource)) {
            return Collections.emptyList();
        }
        return this.resourceLoader.getResources(resource);
    }

    @Override
    public List<InputStream> getResourcesAsStream(String resource) throws IOException {
        if (!testPath(resource)) {
            return Collections.emptyList();
        }
        return this.resourceLoader.getResourcesAsStream(resource);
    }

    @Override
    public boolean exist(String resource) {
        if (!testPath(resource)) {
            return false;
        }
        return this.resourceLoader.exist(resource);
    }

    @Override
    public Manifest getManifest(String resource) throws IOException {
        return resourceLoader.getManifest(resource);
    }
}
