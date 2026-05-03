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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;
import com.clougence.utils.loader.AbstractResourceLoader;

/**
 * 将一个 File 对象所代表的路径作为根路径。
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public class PathResourceLoader extends AbstractResourceLoader {

    private final List<File> dirPathList;
    private Manifest         manifest = null;

    public PathResourceLoader(){
        this(null);
    }

    public PathResourceLoader(File dirFile){
        this.dirPathList = new ArrayList<>();
        if (dirFile != null) {
            this.addPath(dirFile);
        }
    }

    public void addPath(File dirFile) {
        if (dirFile.exists() && dirFile.isDirectory()) {
            this.dirPathList.add(dirFile);
        }
    }

    private List<File> formatResourcePath(final String resource) {
        return dirPathList.stream().filter(File::exists).map(file -> new File(file, resource)).collect(Collectors.toList());
    }

    @Override
    public URL getResource(String resource) throws IOException {
        List<File> resourcePaths = formatResourcePath(resource);
        for (File file : resourcePaths) {
            if (file.exists() && file.isFile()) {
                return file.toURI().toURL();
            }
        }
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String resource) throws IOException {
        List<File> resourcePaths = formatResourcePath(resource);
        for (File file : resourcePaths) {
            if (file.exists() && file.isFile()) {
                return Files.newInputStream(file.toPath());
            }
        }
        return null;
    }

    @Override
    public List<URL> getResources(String resource) throws IOException {
        List<URL> result = new ArrayList<>();
        List<File> resourcePaths = formatResourcePath(resource);
        for (File file : resourcePaths) {
            if (file.exists() && file.isFile()) {
                result.add(file.toURI().toURL());
            }
        }
        return result;
    }

    @Override
    public List<InputStream> getResourcesAsStream(String resource) throws IOException {
        List<InputStream> result = new ArrayList<>();
        List<File> resourcePaths = formatResourcePath(resource);
        for (File file : resourcePaths) {
            if (file.exists() && file.isFile()) {
                result.add(Files.newInputStream(file.toPath()));
            }
        }
        return result;
    }

    @Override
    public boolean exist(String resource) {
        List<File> resourcePaths = formatResourcePath(resource);
        for (File file : resourcePaths) {
            if (file.exists() && file.isFile()) {
                return true;
            }
        }
        return false;
    }

    protected boolean testFound(String entry, Predicate<String>[] testPredicates) {
        if (testPredicates == null || testPredicates.length == 0) {
            return true;
        } else {
            for (Predicate<String> predicate : testPredicates) {
                if (predicate.test(entry)) {
                    return true;
                }
            }
            return false;
        }
    }

    private <T> void scanDirFile(File baseDir, List<T> result, File scanDir, Scanner<T> scanner, Predicate<String>[] scanPaths, boolean matchOnce) throws IOException {
        File[] listFiles = scanDir.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File fileItem : listFiles) {
            if (matchOnce && !result.isEmpty()) {
                return;
            }
            if (fileItem.isHidden() || !fileItem.exists()) {
                // 过滤掉：隐藏和不存在的
                continue;
            }

            String resourceName = fileItem.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1);
            if (SystemUtils.isWindows()) {
                resourceName = StringUtils.replace(resourceName, File.separator, "/");
            }

            if (!testFound(resourceName, scanPaths)) {
                // 匹配失败
                continue;
            }
            if (fileItem.isDirectory()) {
                // 目录继续向下搜索
                scanDirFile(baseDir, result, fileItem, scanner, scanPaths, matchOnce);
                continue;
            }
            InputStreamGet inputStream = () -> {
                if (fileItem.canRead()) {
                    return Files.newInputStream(fileItem.toPath());
                } else {
                    throw new IOException("file cannot be read :" + fileItem);
                }
            };
            T res = scanner.found(new ScanEvent(resourceName, fileItem.length(), fileItem.toURI(), inputStream));
            if (res != null) {
                result.add(res);
            }
        }
    }

    @Override
    public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        Predicate<String>[] tests = buildPredicate(matchType, scanPaths, s -> s);

        for (File baseDir : this.dirPathList) {
            if (baseDir.exists()) {
                scanDirFile(baseDir, result, baseDir, scanner, tests, false);
            }
        }
        return result;
    }

    @Override
    public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        Predicate<String>[] tests = buildPredicate(matchType, scanPaths, s -> s);

        for (File baseDir : this.dirPathList) {
            if (baseDir.exists()) {
                scanDirFile(baseDir, result, baseDir, scanner, tests, true);
            }
        }
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Manifest getManifest(String resource) throws IOException {
        if (manifest != null) {
            return manifest;
        }
        for (File file : dirPathList) {
            if (file.exists() && file.isFile() && file.getName().equalsIgnoreCase("MANIFEST.MF")) {
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    manifest = new Manifest(inputStream);
                }
            }
        }
        return manifest;
    }
}
