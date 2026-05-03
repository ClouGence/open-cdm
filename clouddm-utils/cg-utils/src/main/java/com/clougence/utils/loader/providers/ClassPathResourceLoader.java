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
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;
import com.clougence.utils.io.FileUtils;
import com.clougence.utils.loader.AbstractResourceLoader;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于创建一个可以从 classpath 中获取资源的 ResourceLoader
 * @version : 2021-10-10
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class ClassPathResourceLoader extends AbstractResourceLoader {

    private final ClassLoader classLoader;
    private final List<URL>   classpath;
    private final String[]    ZIP_TYPES = new String[] { ".jar", ".JAR", ".zip", ".ZIP" };
    private Manifest          manifest  = null;

    public ClassPathResourceLoader(String resourcePrefix){
        this.classLoader = Thread.currentThread().getContextClassLoader();
        this.classpath = getClassPath(this.classLoader, resourcePrefix);
    }

    public ClassPathResourceLoader(ClassLoader parent, String resourcePrefix){
        this.classLoader = Objects.requireNonNull(parent);
        this.classpath = getClassPath(this.classLoader, resourcePrefix);
    }

    public ClassLoader getClassLoader() { return this.classLoader; }

    /** 获取ClassPath路径 */
    protected List<URL> getClassPath(ClassLoader classLoader, String resourcePrefix) {
        return ClassUtils.getClassPath(classLoader, resourcePrefix);
    }

    private String formatResourcePath(String resourcePath) {
        resourcePath = resourcePath.replaceAll("/{2}", "/");
        if (resourcePath.charAt(0) == '/') {
            resourcePath = resourcePath.substring(1);
        }
        return resourcePath;
    }

    @Override
    public URL getResource(String resource) throws IOException {
        return this.classLoader.getResource(formatResourcePath(resource));
    }

    @Override
    public InputStream getResourceAsStream(String resource) {
        return this.classLoader.getResourceAsStream(formatResourcePath(resource));
    }

    @Override
    public List<URL> getResources(String resource) throws IOException {
        resource = formatResourcePath(resource);
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> urlEnumeration = this.classLoader.getResources(resource);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            if (url != null) {
                urls.add(url);
            }
        }
        return urls;
    }

    @Override
    public List<InputStream> getResourcesAsStream(String resource) throws IOException {
        resource = formatResourcePath(resource);
        List<InputStream> ins = new ArrayList<>();
        Enumeration<URL> urlEnumeration = this.classLoader.getResources(resource);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            InputStream in = (url != null) ? url.openStream() : null;
            if (in != null) {
                ins.add(in);
            }
        }
        return ins;
    }

    @Override
    public boolean exist(String resource) {
        return this.classLoader.getResource(formatResourcePath(resource)) != null;
    }

    @Override
    public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        Predicate<JarEntry>[] testJar = buildPredicate(matchType, scanPaths, ZipEntry::getName);
        Predicate<String>[] testDir = buildPredicate(matchType, scanPaths, s -> s);

        for (URL url : this.classpath) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                File contextDir = FileUtils.toFile(url);
                if (contextDir == null || !contextDir.exists()) {
                    continue;
                }

                if (contextDir.isDirectory()) {
                    dirScan(result, contextDir, contextDir, testDir, scanner, false);
                } else {
                    String jarName = contextDir.getName();
                    if (!StringUtils.endsWithAny(jarName, ZIP_TYPES)) {
                        continue;
                    }

                    try {
                        JarFile jarFile = new JarFile(contextDir);
                        jarScan(result, jarFile, testJar, scanner, false);
                    } catch (Exception e) {
                        log.error("cannot open jar (" + jarName + ") " + e.getMessage(), e);
                    }
                }
            } else if (protocol.equals("jar")) {
                JarURLConnection jarc = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarc.getJarFile();
                jarScan(result, jarFile, testJar, scanner, false);
            }
        }
        return result;
    }

    @Override
    public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException {
        List<T> result = new ArrayList<>();
        Predicate<JarEntry>[] testJar = buildPredicate(matchType, scanPaths, ZipEntry::getName);
        Predicate<String>[] testDir = buildPredicate(matchType, scanPaths, s -> s);

        for (URL url : this.classpath) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                File contextDir = FileUtils.toFile(url);
                if (contextDir == null || !contextDir.exists()) {
                    continue;
                }

                if (contextDir.isDirectory()) {
                    dirScan(result, contextDir, contextDir, testDir, scanner, true);
                } else {
                    String jarName = contextDir.getName();
                    if (!StringUtils.endsWithAny(jarName, ZIP_TYPES)) {
                        continue;
                    }

                    try {
                        JarFile jarFile = new JarFile(contextDir);
                        jarScan(result, jarFile, testJar, scanner, true);
                    } catch (Exception e) {
                        log.error("cannot open jar (" + jarName + ") " + e.getMessage(), e);
                    }
                }
            } else if (protocol.equals("jar")) {
                JarURLConnection jarc = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarc.getJarFile();
                jarScan(result, jarFile, testJar, scanner, true);
            }

            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private static <T> void jarScan(List<T> result, JarFile jarFile, Predicate<JarEntry>[] jarTest, Scanner<T> scanner, boolean matchOnce) throws IOException {
        URL content = new File(jarFile.getName()).toURI().toURL();
        Iterator<JarEntry> zipEntry = jarFile.stream().iterator();
        while (zipEntry.hasNext()) {
            JarEntry entry = zipEntry.next();
            if (!jarTestFound(entry, jarTest)) {
                continue;
            }

            try {
                URI uri = new URL("jar:" + content + "!/" + entry.getName()).toURI();
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

    private static boolean jarTestFound(JarEntry entry, Predicate<JarEntry>[] jarTest) {
        if (jarTest == null || jarTest.length == 0) {
            return true;
        } else {
            for (Predicate<JarEntry> predicate : jarTest) {
                if (predicate.test(entry)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static <T> void dirScan(List<T> result, File contextDir, File curFile, Predicate<String>[] testDir, Scanner<T> scanner, boolean matchOnce) throws IOException {
        if (matchOnce && !result.isEmpty()) {
            return;
        }
        File[] listFiles = curFile.listFiles();
        if (listFiles == null) {
            return;
        }

        for (File fileItem : listFiles) {
            if (matchOnce && !result.isEmpty()) {
                return;
            }
            if (fileItem.isHidden() || !fileItem.exists()) {
                continue;
            }
            if (fileItem.isDirectory()) {
                dirScan(result, contextDir, fileItem, testDir, scanner, matchOnce);
                continue;
            }

            String resourceName = fileItem.getAbsolutePath().substring(contextDir.getAbsolutePath().length() + 1);
            if (SystemUtils.isWindows()) {
                resourceName = StringUtils.replace(resourceName, File.separator, "/");
            }

            if (!dirTestFound(resourceName, testDir)) {
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

    private static boolean dirTestFound(String entry, Predicate<String>[] dirTest) {
        if (dirTest == null || dirTest.length == 0) {
            return true;
        } else {
            for (Predicate<String> predicate : dirTest) {
                if (predicate.test(entry)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Manifest getManifest(String resource) throws IOException {
        if (manifest != null) {
            return manifest;
        }
        //classpath:/META-INF/MANIFEST.MF, 从classpath中获取
        for (URL url : classpath) {
            if (url.getProtocol().equals("file")) {
                File file = FileUtils.toFile(url);
                if (file != null && file.isDirectory()) {
                    file = new File(file, "META-INF/MANIFEST.MF");
                    if (file.exists()) {
                        try {
                            manifest = new Manifest(Files.newInputStream(file.toPath()));
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        }
        return manifest;
    }
}
