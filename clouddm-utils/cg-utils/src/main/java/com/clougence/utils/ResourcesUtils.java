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
package com.clougence.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.clougence.utils.io.FileUtils;
import com.clougence.utils.io.input.AutoCloseInputStream;

/**
 * 资源加载工具类，所有方法均是程序级优先。
 * @version 2010-9-24
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class ResourcesUtils {

    /*------------------------------------------------------------------------------*/
    public static String formatResource(String resourcePath) {
        if (resourcePath != null && resourcePath.length() > 1) {
            if (resourcePath.charAt(0) == '/') {
                resourcePath = resourcePath.substring(1);
            }
        }
        return resourcePath;
    }

    private static ClassLoader getCurrentLoader() { return Thread.currentThread().getContextClassLoader(); }

    /** 合成所有属性文件的配置信息到一个{@link Map}接口中。*/
    public static Map<String, String> getProperty(final String[] resourcePaths) throws IOException {
        return getProperty(Arrays.asList(resourcePaths).iterator());
    }

    /** 合成所有属性文件的配置信息到一个{@link Map}接口中。*/
    public static Map<String, String> getProperty(final Iterator<String> iterator) throws IOException {
        if (iterator == null) {
            return null;
        }
        //
        ClassLoader classLoader = getCurrentLoader();
        Map<String, String> fullData = new HashMap<>();
        while (iterator.hasNext()) {
            String str = iterator.next();
            Map<String, String> att = getProperty(classLoader, str);
            fullData.putAll(att);
        }
        return fullData;
    }

    /** 读取一个属性文件，并且以{@link Map}接口的形式返回。*/
    public static Map<String, String> getProperty(final String resourcePath) throws IOException {
        return getProperty(getCurrentLoader(), resourcePath);
    }

    /** 读取一个属性文件，并且以{@link Map}接口的形式返回。*/
    public static Map<String, String> getProperty(final ClassLoader classLoader, final String resourcePath) throws IOException {
        Properties prop = new Properties();
        InputStream in = getResourceAsStream(classLoader, resourcePath);
        if (in != null) {
            prop.load(in);
        }
        HashMap<String, String> resultData = new HashMap<>();
        for (Object keyObj : prop.keySet()) {
            String key = (String) keyObj;
            String val = prop.getProperty(key);
            resultData.put(key, val);
        }
        return resultData;
    }

    /*------------------------------------------------------------------------------*/

    /** 获取 classpath 中可能存在的资源。*/
    public static URL getResource(String resourcePath) throws IOException {
        if (StringUtils.isBlank(resourcePath)) {
            return null;
        }
        //
        if (resourcePath.startsWith("classpath:")) {
            resourcePath = resourcePath.substring("classpath:".length());
            return getResource(getCurrentLoader(), resourcePath);
        } else if (resourcePath.startsWith("http:") || resourcePath.startsWith("https:") || resourcePath.startsWith("file:") || resourcePath.startsWith("jar:")
                   || resourcePath.startsWith("ftp:")) {
            return new URL(resourcePath);
        } else {
            return getResource(getCurrentLoader(), resourcePath);
        }
    }

    /** 获取 classpath 中可能存在的资源。*/
    public static URL getResource(ClassLoader classLoader, String resourcePath) throws IOException {
        resourcePath = formatResource(resourcePath);
        return classLoader.getResource(resourcePath);
    }

    /** 获取 classpath 中可能存在的资源列表。*/
    public static List<URL> getResources(String resourcePath) throws IOException {
        return getResources(getCurrentLoader(), resourcePath);
    }

    /** 获取 classpath 中可能存在的资源列表。*/
    public static List<URL> getResources(ClassLoader classLoader, String resourcePath) throws IOException {
        if (resourcePath == null) {
            return new ArrayList<>(0);
        }
        //
        resourcePath = formatResource(resourcePath);
        ArrayList<URL> urls = new ArrayList<>();
        Enumeration<URL> urlEnumeration = classLoader.getResources(resourcePath);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            urls.add(url);
        }
        return urls;
    }

    /*------------------------------------------------------------------------------*/

    /** 获取可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(File resourceFile) throws IOException {
        return getResourceAsStream(getCurrentLoader(), resourceFile.toURI().toURL());
    }

    /** 获取classpath中可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(URI resourceURI) throws IOException {
        return getResourceAsStream(getCurrentLoader(), resourceURI.toURL());
    }

    /** 获取classpath中可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(String resourcePath) throws IOException {
        URL resource = getResource(resourcePath);
        if (resource == null) {
            return null;
        }
        return getResourceAsStream(getCurrentLoader(), resource);
    }

    /** 获取classpath中可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(URL resourceURL) throws IOException {
        return getResourceAsStream(getCurrentLoader(), resourceURL);
    }

    /** 获取classpath中可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(ClassLoader classLoader, URI resourceURI) throws IOException {
        return getResourceAsStream(classLoader, resourceURI.toURL());
    }

    /**获取classpath中可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(ClassLoader classLoader, String resourcePath) throws IOException {
        resourcePath = formatResource(resourcePath);
        URL resource;
        if (classLoader != null) {
            resource = classLoader.getResource(resourcePath);
        } else {
            classLoader = getCurrentLoader();
            resource = getResource(resourcePath);
        }

        if (resource == null) {
            return null;
        }
        return getResourceAsStream(classLoader, resource);
    }

    /** 获取classpath中可能存在的资源，以流的形式返回。*/
    public static InputStream getResourceAsStream(ClassLoader classLoader, URL resourceURL) throws IOException {
        String protocol = resourceURL.getProtocol().trim().toLowerCase();
        switch (protocol) {
            case "classpath": {
                String resourcePath = resourceURL.getPath();
                return getResourceAsStream(classLoader, resourcePath);
            }
            case "http":
            case "https":
            case "ftp": {
                return new AutoCloseInputStream(resourceURL.openStream());
            }
            case "file": {
                File targetFile = FileUtils.toFile(resourceURL);
                if (targetFile.exists()) {
                    if (targetFile.canRead() && targetFile.isFile()) {
                        return new AutoCloseInputStream(Files.newInputStream(targetFile.toPath()));
                    } else {
                        throw new IOException("resource " + targetFile.getAbsolutePath() + " can not be read.");
                    }
                }
                return null;
            }
            case "jar": {
                //JAR文件
                JarFile jar = ((JarURLConnection) resourceURL.openConnection()).getJarFile();
                String jarFile = jar.getName().replace("\\", "/");
                String resourcePath = URLDecoder.decode(resourceURL.getPath(), "utf-8");
                int beginIndex = resourcePath.indexOf(jarFile) + jarFile.length();
                String entPath = resourcePath.substring(beginIndex + 2);
                ZipEntry e = jar.getEntry(entPath);
                return new AutoCloseInputStream(jar.getInputStream(e));
            }
            default: {
                return classLoader.getResourceAsStream(resourceURL.toString());
            }
        }
    }

    /** 获取classpath中可能存在的资源列表，以流的形式返回。*/
    public static List<InputStream> getResourceAsStreamList(String resourcePath) throws IOException {
        return getResourceAsStreamList(getCurrentLoader(), resourcePath);
    }

    /** 获取classpath中可能存在的资源列表，以流的形式返回。*/
    public static List<InputStream> getResourceAsStreamList(ClassLoader classLoader, String resourcePath) throws IOException {
        ArrayList<InputStream> iss = new ArrayList<>();
        List<URL> urls = getResources(classLoader, resourcePath);
        for (URL url : urls) {
            InputStream in = getResourceAsStream(classLoader, url);
            if (in != null) {
                iss.add(new AutoCloseInputStream(in));
            }
        }
        return iss;
    }

    /**
     * Loads a class
     * @param className - the class to fetch
     * @return The loaded class
     * @throws ClassNotFoundException If the class cannot be found (duh!)
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return classForName(getCurrentLoader(), className);
    }

    /**
     * Loads a class
     * @param className - the class to fetch
     * @return The loaded class
     * @throws ClassNotFoundException If the class cannot be found (duh!)
     */
    public static Class<?> classForName(ClassLoader loader, String className) throws ClassNotFoundException {
        if (loader == null) {
            return getCurrentLoader().loadClass(className);
        } else {
            return loader.loadClass(className);
        }
    }
}
