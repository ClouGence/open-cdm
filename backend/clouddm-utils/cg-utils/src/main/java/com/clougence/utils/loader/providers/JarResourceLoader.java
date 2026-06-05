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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import com.clougence.utils.StringUtils;
import com.clougence.utils.jar.JarFile;
import com.clougence.utils.loader.AbstractResourceLoader;

import lombok.extern.slf4j.Slf4j;

/**
 * 可以处理 jar 包的 ResourceLoader，并且支持 jar in jar
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class JarResourceLoader extends AbstractResourceLoader {

    private final JarFile               jarFile;
    private final List<JarFile>         nestedJarFile;
    private final Map<String, Manifest> manifestCache = new ConcurrentHashMap<>();

    public JarResourceLoader(File file) throws IOException{
        this.jarFile = new JarFile(file);
        this.nestedJarFile = new ArrayList<>();
    }

    public JarResourceLoader(File file, List<String> nestedList) throws IOException{
        this.jarFile = new JarFile(file);
        this.nestedJarFile = new ArrayList<>();
        for (String nestedJar : nestedList) {
            JarEntry jarEntry = this.jarFile.getJarEntry(nestedJar);
            if (jarEntry != null) {
                this.nestedJarFile.add(this.jarFile.getNestedJarFile(jarEntry));
            }
        }
    }

    public JarResourceLoader(File file, Predicate<JarEntry> nestedPredicate) throws IOException{
        this.jarFile = new JarFile(file);
        this.nestedJarFile = new ArrayList<>();
        if (nestedPredicate != null) {
            for (JarEntry jarEntry : this.jarFile) {
                if (jarEntry != null && nestedPredicate.test(jarEntry)) {
                    this.nestedJarFile.add(this.jarFile.getNestedJarFile(jarEntry));
                }
            }
        }
    }

    public Manifest getManifest() throws IOException { return this.jarFile.getManifest(); }

    @Override
    public Manifest getManifest(String resource) throws IOException {
        if (StringUtils.isEmpty(resource)) {
            return null;
        }
        URL url = getResource(resource);
        if (url == null) {
            return null;
        }
        String path = url.getPath();
        String[] split = path.split("!/");
        if (split.length == 1) {
            return jarFile.getManifest();
        }
        String entryName = split[1];
        Manifest manifest = manifestCache.get(entryName);
        if (manifest == null) {
            for (JarFile jarFile : this.nestedJarFile) {
                String pathFromRoot = jarFile.getPathFromRoot();
                if (StringUtils.isEmpty(pathFromRoot))
                    continue;
                if (pathFromRoot.startsWith("!/")) {
                    pathFromRoot = pathFromRoot.substring(2);
                }
                manifestCache.computeIfAbsent(pathFromRoot, key -> {
                    try {
                        return jarFile.getManifest();
                    } catch (IOException e) {
                        return null;
                    }
                });
            }
            return manifestCache.get(entryName);
        } else {
            return manifest;
        }
    }

    @Override
    public URL getResource(String resource) throws IOException {
        ZipEntry zipEntry = this.jarFile.getEntry(resource);
        if (zipEntry != null) {
            return new URL(this.jarFile.getUrl(), zipEntry.getName());
        } else if (!this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : this.nestedJarFile) {
                ZipEntry nestedZipEntry = nestedJar.getEntry(resource);
                if (nestedZipEntry != null) {
                    return new URL(nestedJar.getUrl(), nestedZipEntry.getName());
                }
            }
        }
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String resource) throws IOException {
        ZipEntry zipEntry = this.jarFile.getEntry(resource);
        if (zipEntry != null) {
            return this.jarFile.getInputStream(zipEntry);
        } else if (!this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : this.nestedJarFile) {
                ZipEntry nestedZipEntry = nestedJar.getEntry(resource);
                if (nestedZipEntry != null) {
                    return nestedJar.getInputStream(nestedZipEntry);
                }
            }
        }
        return null;
    }

    @Override
    public List<URL> getResources(String resource) throws IOException {
        List<URL> result = new ArrayList<>();

        ZipEntry zipEntry = this.jarFile.getEntry(resource);
        if (zipEntry != null) {
            result.add(new URL(this.jarFile.getUrl(), zipEntry.getName()));
        }
        if (!this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : nestedJarFile) {
                ZipEntry nestedZipEntry = nestedJar.getEntry(resource);
                if (nestedZipEntry != null) {
                    result.add(new URL(nestedJar.getUrl(), resource));
                }
            }
        }
        return result;
    }

    @Override
    public List<InputStream> getResourcesAsStream(String resource) throws IOException {
        List<InputStream> result = new ArrayList<>();

        ZipEntry zipEntry = this.jarFile.getEntry(resource);
        if (zipEntry != null) {
            result.add(this.jarFile.getInputStream(zipEntry));
        }
        if (!this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : nestedJarFile) {
                ZipEntry nestedZipEntry = nestedJar.getEntry(resource);
                if (nestedZipEntry != null) {
                    result.add(nestedJar.getInputStream(nestedZipEntry));
                }
            }
        }
        return result;
    }

    @Override
    public boolean exist(String resource) {
        ZipEntry zipEntry = this.jarFile.getEntry(resource);
        if (zipEntry != null) {
            return true;
        } else if (!this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : this.nestedJarFile) {
                ZipEntry nestedZipEntry = nestedJar.getEntry(resource);
                if (nestedZipEntry != null) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean testFound(JarEntry entry, Predicate<JarEntry>[] testPredicates) {
        if (testPredicates == null || testPredicates.length == 0) {
            return true;
        } else {
            for (Predicate<JarEntry> predicate : testPredicates) {
                if (predicate.test(entry)) {
                    return true;
                }
            }
            return false;
        }
    }

    private <T> void scanJarFile(List<T> result, JarFile jarFile, Scanner<T> scanner, Predicate<JarEntry>[] scanPaths, boolean matchOnce) throws IOException {
        Iterator<JarEntry> zipEntry = jarFile.stream().iterator();
        while (zipEntry.hasNext()) {
            JarEntry entry = zipEntry.next();
            if (!testFound(entry, scanPaths)) {
                continue;
            }

            try {
                URI uri = new URL(jarFile.getUrl(), entry.getName()).toURI();
                InputStreamGet inputStream = () -> jarFile.getInputStream(entry);
                T res = scanner.found(new ScanEvent(entry.getName(), entry.getSize(), uri, inputStream));
                if (res != null) {
                    result.add(res);
                }
            } catch (URISyntaxException e) {
                if (log.isDebugEnabled()) {
                    log.warn("scanJarFile :" + e.getMessage(), e);
                } else {
                    log.debug("scanJarFile :" + e.getMessage());
                }
            }
            if (matchOnce && !result.isEmpty()) {
                return;
            }
        }
    }

    @Override
    public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        Predicate<JarEntry>[] tests = buildPredicate(matchType, scanPaths, ZipEntry::getName);

        this.scanJarFile(result, this.jarFile, scanner, tests, false);
        if (!this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : this.nestedJarFile) {
                this.scanJarFile(result, nestedJar, scanner, tests, false);
            }
        }
        return result;
    }

    @Override
    public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        Predicate<JarEntry>[] tests = buildPredicate(matchType, scanPaths, ZipEntry::getName);

        this.scanJarFile(result, this.jarFile, scanner, tests, true);
        if (result.isEmpty() && !this.nestedJarFile.isEmpty()) {
            for (JarFile nestedJar : this.nestedJarFile) {
                if (result.isEmpty()) {
                    this.scanJarFile(result, nestedJar, scanner, tests, true);
                }
            }
        }
        return result.isEmpty() ? null : result.get(0);
    }

}
