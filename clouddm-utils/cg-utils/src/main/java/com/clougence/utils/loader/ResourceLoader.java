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
package com.clougence.utils.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.jar.Manifest;

/**
 * 资源加载器、资源扫描器
 * @version : 2021-09-29
 * @author 赵永春 (zyc@hasor.net)
 */
public interface ResourceLoader {

    default <T> List<T> scanResources(Scanner<T> scanner) throws IOException {
        return scanResources(MatchType.None, scanner, new String[0]);
    }

    default <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner) throws IOException {
        return scanResources(matchType, scanner, new String[0]);
    }

    <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException;

    default <T> T scanOneResource(Scanner<T> scanner) throws IOException {
        return scanOneResource(MatchType.None, scanner, new String[0]);
    }

    default <T> T scanOneResource(MatchType matchType, Scanner<T> scanner) throws IOException {
        return scanOneResource(matchType, scanner, new String[0]);
    }

    <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) throws IOException;

    URL getResource(String resource) throws IOException;

    InputStream getResourceAsStream(String resource) throws IOException;

    CgClassLoader toClassLoader(ClassLoader parent);

    List<URL> getResources(String resource) throws IOException;

    List<InputStream> getResourcesAsStream(String resource) throws IOException;

    boolean exist(String resource);

    Manifest getManifest(String resource) throws IOException;

    interface InputStreamGet {

        InputStream getStream() throws IOException;
    }

    /** 扫描的资源 */
    class ScanEvent {

        private final String         name;
        private final long           size;
        private final URI            resource;
        private final InputStreamGet streamGetter;

        public ScanEvent(String name, long size, URI resource, InputStreamGet streamGetter){
            this.name = name;
            this.size = size;
            this.resource = resource;
            this.streamGetter = streamGetter;
        }

        public String getName() { return this.name; }

        public long getSize() { return this.size; }

        public URI getResource() { return this.resource; }

        public InputStream getStream() throws IOException { return this.streamGetter.getStream(); }
    }

    /** 扫描classpath时找到资源的回调接口方法 */
    interface Scanner<T> {

        /**
         * 找了一个资源
         * @param event 找到资源事件。
         */
        T found(ScanEvent event) throws IOException;
    }

    /** 扫描器资源匹配类型 */
    enum MatchType {
        /** 扫描所有资源 */
        None,
        /** 前缀匹配 */
        Prefix,
        /** 含有字符串 */
        ContainsAny,
        /** 尾缀匹配 */
        Suffix,
        /** 全字匹配 */
        Match,
        /** 正则表达式匹配 */
        Regex,
    }
}
