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

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.clougence.utils.jar.JarFile;
import org.junit.jupiter.api.Test;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.loader.ResourceLoader;

/**
 * Tests for {@link JarFile}.
 */
public class ClassResourceLoaderTests {

    @Test
    public void classpathLoaderTest1() {
        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        assert loader.exist("dir/app-1.jar/app/FooImpl.class");
        assert loader.exist("dir/app-1.jar/app/FooImpl.java");
        assert loader.exist("dir/app-1.jar/META-INF/jar.mf");
        assert loader.exist("dir/app-1.jar/META-INF/ReadMe.txt");
        assert !loader.exist("dir/app-1.jar/abc/abc.text");
    }

    @Test
    public void classpathLoaderTest2() throws Exception {
        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        assert loader.getResource("dir/app-1.jar/app/FooImpl.class").toString().endsWith("dir/app-1.jar/app/FooImpl.class");
        assert loader.getResource("dir/app-1.jar/app/FooImpl.java").toString().endsWith("dir/app-1.jar/app/FooImpl.java");
        assert loader.getResource("dir/app-1.jar/META-INF/ReadMe.txt").toString().endsWith("dir/app-1.jar/META-INF/ReadMe.txt");
    }

    @Test
    public void classpathLoaderTest3() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("dir/app-1.jar/app/FooImpl.class")));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("dir/app-1.jar/app/FooImpl.java")));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourceAsStream("dir/app-1.jar/META-INF/ReadMe.txt")));
        assert loader.getResourceAsStream("abc/abc.text") == null;
    }

    @Test
    public void classpathLoaderTest4() throws Exception {
        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        assert loader.getResources("dir/app-1.jar/app/FooImpl.class").get(0).toString().endsWith("dir/app-1.jar/app/FooImpl.class");
        assert loader.getResources("dir/app-1.jar/app/FooImpl.java").get(0).toString().endsWith("dir/app-1.jar/app/FooImpl.java");
        assert loader.getResources("dir/app-1.jar/META-INF/ReadMe.txt").get(0).toString().endsWith("dir/app-1.jar/META-INF/ReadMe.txt");
    }

    @Test
    public void classpathLoaderTest5() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourcesAsStream("dir/app-1.jar/app/FooImpl.class").get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourcesAsStream("dir/app-1.jar/app/FooImpl.java").get(0)));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourcesAsStream("/dir/app-1.jar/META-INF/ReadMe.txt").get(0)));
    }

    @Test
    public void classpathLoaderTest6() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResource("dir/app-1.jar/app/FooImpl.class").openStream()));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResource("dir/app-1.jar/app/FooImpl.java").openStream()));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResource("dir/app-1.jar/META-INF/ReadMe.txt").openStream()));
    }

    @Test
    public void classpathLoaderTest7() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/FooImpl.java"));
        ResourceLoader.Scanner<InputStream> scanner = event -> {
            if (event.getName().startsWith("dir/app-1.jar/app/F")) {
                return event.getStream();
            } else {
                return null;
            }
        };

        ClassPathResourceLoader loader = new ClassPathResourceLoader("dir/");
        InputStream inputStream = loader.scanOneResource(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStream));

        List<InputStream> inputStreams = loader.scanResources(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStreams.get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(inputStreams.get(1)));
    }

    @Test
    public void classpathLoaderTest8() throws Exception {
        ClassPathResourceLoader loader = new ClassPathResourceLoader("net/hasor/");
        assert loader.scanOneResource(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }) == null;
        assert loader.scanResources(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }).isEmpty();
    }

    //    @Test
    //    public void classpathLoaderTest10() throws IOException {
    //        ClassPathResourceLoader resourceLoader = new ClassPathResourceLoader("com/clougence");
    //
    //        assert resourceLoader.exist("com/clougence/utils/loader/providers/ClassPathResourceLoader.class");
    //        assert resourceLoader.getResource("com/clougence/utils/loader/providers/ClassPathResourceLoader.class") != null;
    //        assert resourceLoader.getResources("com/clougence/utils/loader/providers/ClassPathResourceLoader.class").size() == 1;
    //
    //        Set<Class<?>> classSet = new CgClassLoader(resourceLoader).getClassSet(new String[] { "com.clougence.utils" }, ClassMatchers.subClassesOf(ResourceLoader.class));
    //        assert classSet.contains(ClassPathResourceLoader.class);
    //        assert classSet.contains(MultiResourceLoader.class);
    //        assert classSet.contains(ExportResourceLoader.class);
    //        assert classSet.contains(ImportResourceLoader.class);
    //        assert classSet.contains(AbstractResourceLoader.class);
    //        assert classSet.contains(JarResourceLoader.class);
    //        assert classSet.contains(PathResourceLoader.class);
    //
    //        classSet = new CgClassLoader(resourceLoader).getClassSet(new String[] { "com.clougence.utils"  }, ClassMatchers.featureWith(ResourceLoader.class));
    //        assert classSet.contains(ClassPathResourceLoader.class);
    //        assert classSet.contains(PathResourceLoader.class);
    //        assert classSet.contains(JarResourceLoader.class);
    //        assert classSet.contains(MultiResourceLoader.class);
    //        assert classSet.contains(AbstractResourceLoader.class);
    //        assert classSet.contains(ImportResourceLoader.class);
    //        assert classSet.contains(ExportResourceLoader.class);
    //        assert classSet.contains(ResourceLoader.class);
    //    }
}
