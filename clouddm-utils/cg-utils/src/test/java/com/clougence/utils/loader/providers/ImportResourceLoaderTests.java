///*
// * Copyright 2012-2020 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.clougence.utils.loader.providers;
//
//
//import com.clougence.utils.ResourcesUtils;
//import com.clougence.utils.io.IOUtils;
//import com.clougence.utils.loader.ResourceLoader;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//import java.util.function.Function;
//
//import org.junit.jupiter.api.Test;
//
//import com.clougence.utils.ResourcesUtils;
//import com.clougence.utils.io.IOUtils;
//import com.clougence.utils.loader.ResourceLoader;
//
///**
// * Tests for {@link JarFile}.
// */
//public class ImportResourceLoaderTests {
//
//    private ImportResourceLoader loader;
//
//    //    @Before
//    public void beforeImport() throws IOException {
//        String file1 = ResourcesUtils.getResource("/dir/basic.jar").getFile();
//        String file2 = ResourcesUtils.getResource("/dir/app-1.jar").getFile();
//        String file3 = ResourcesUtils.getResource("/dir/app-2.zip").getFile();
//        this.loader = new ImportResourceLoader();
//        this.loader.importResources(0, new PathResourceLoader(new File(file1)));
//        this.loader.importResources(1, "app/onlyjar", new PathResourceLoader(new File(file2)));
//        this.loader.importResources(2, "app/onlyzip", new PathResourceLoader(new File(file3)));
//    }
//
//    @Test
//    public void importLoaderTest1() throws Exception {
//        beforeImport();
//        ImportResourceLoader loader = new ImportResourceLoader();
//        JarResourceLoader jar1 = new JarResourceLoader(new File(ResourcesUtils.getResource("/jars/basic.jar").getFile()));
//        JarResourceLoader jar2 = new JarResourceLoader(new File(ResourcesUtils.getResource("/jars/app-1.jar").getFile()));
//        JarResourceLoader jar3 = new JarResourceLoader(new File(ResourcesUtils.getResource("/jars/app-2.zip").getFile()));
//        PathResourceLoader path1 = new PathResourceLoader(new File(""));
//        loader.importResources(0, jar1);
//        loader.importResources(1, "app/onlyjar", jar2);
//        loader.importResources(2, "app/onlyzip", jar3);
//        loader.importResources(3, ClassPathResourceLoader.INSTANCE);
//        loader.importResources(4, ClassPathResourceLoader.INSTANCE);
//        loader.importResources(5, path1);
//
//        Iterator<ImportResourceLoader.LoaderWrap> wrapLoaders1 = loader.getLoader("app");
//        assert wrapLoaders1.next().loader == jar1;
//        assert wrapLoaders1.next().loader == ClassPathResourceLoader.INSTANCE;
//        assert wrapLoaders1.next().loader == path1;
//
//        Iterator<ImportResourceLoader.LoaderWrap> wrapLoaders2 = loader.getLoader("app/onlyzip");
//        assert wrapLoaders2.next().loader == jar1;
//        assert wrapLoaders2.next().loader == jar3;
//        assert wrapLoaders2.next().loader == ClassPathResourceLoader.INSTANCE;
//        assert wrapLoaders2.next().loader == path1;
//
//        Iterator<ImportResourceLoader.LoaderWrap> wrapLoaders3 = loader.getLoader("app/onlyzip/FooOnlyZip.java");
//        assert wrapLoaders3.next().loader == jar1;
//        assert wrapLoaders3.next().loader == jar3;
//        assert wrapLoaders3.next().loader == ClassPathResourceLoader.INSTANCE;
//        assert wrapLoaders3.next().loader == path1;
//    }
//
//    @Test
//    public void importLoaderTest2() throws IOException {
//        beforeImport();
//        assert !loader.exist("app/FooImpl.java");
//        assert loader.exist("app/onlybasic/FooOnlyBasic.java");
//        assert loader.exist("app/onlyjar/FooOnlyJar.java");
//        assert loader.exist("app/onlyzip/FooOnlyZip.java");
//        assert loader.exist("META-INF/jar.mf");
//        assert loader.exist("META-INF/ReadMe.txt");
//        assert !loader.exist("abc/abc.text");
//    }
//
//    @Test
//    public void importLoaderTest3() throws Exception {
//        beforeImport();
//        assert loader.getResource("app/FooImpl.java") == null;
//        assert loader.getResource("app/onlybasic/FooOnlyBasic.java").toString().endsWith("dir/basic.jar/app/onlybasic/FooOnlyBasic.java");
//        assert loader.getResource("app/onlyjar/FooOnlyJar.java").toString().endsWith("dir/app-1.jar/app/onlyjar/FooOnlyJar.java");
//        assert loader.getResource("app/onlyzip/FooOnlyZip.java").toString().endsWith("dir/app-2.zip/app/onlyzip/FooOnlyZip.java");
//        assert loader.getResource("META-INF/jar.mf").toString().endsWith("dir/basic.jar/META-INF/jar.mf");
//        assert loader.getResource("META-INF/ReadMe.txt").toString().endsWith("dir/basic.jar/META-INF/ReadMe.txt");
//    }
//
//    @Test
//    public void importLoaderTest5() throws Exception {
//        beforeImport();
//        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/app/onlybasic/FooOnlyBasic.java"));
//        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/onlyjar/FooOnlyJar.java"));
//        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-2.zip/app/onlyzip/FooOnlyZip.java"));
//        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/META-INF/jar.mf"));
//        byte[] byteDat5 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/META-INF/ReadMe.txt"));
//
//        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("app/onlybasic/FooOnlyBasic.java")));
//        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("app/onlyjar/FooOnlyJar.java")));
//        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourceAsStream("app/onlyzip/FooOnlyZip.java")));
//        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/jar.mf")));
//        assert Objects.deepEquals(byteDat5, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/ReadMe.txt")));
//    }
//
//    @Test
//    public void importLoaderTest6() throws Exception {
//        beforeImport();
//        assert loader.getResources("app/FooImpl.java").isEmpty();
//        assert loader.getResources("app/onlybasic/FooOnlyBasic.java").get(0).toString().endsWith("dir/basic.jar/app/onlybasic/FooOnlyBasic.java");
//        assert loader.getResources("app/onlyjar/FooOnlyJar.java").get(0).toString().endsWith("dir/app-1.jar/app/onlyjar/FooOnlyJar.java");
//        assert loader.getResources("app/onlyzip/FooOnlyZip.java").get(0).toString().endsWith("dir/app-2.zip/app/onlyzip/FooOnlyZip.java");
//        assert loader.getResources("META-INF/jar.mf").get(0).toString().endsWith("dir/basic.jar/META-INF/jar.mf");
//        assert loader.getResources("META-INF/ReadMe.txt").get(0).toString().endsWith("dir/basic.jar/META-INF/ReadMe.txt");
//
//        assert loader.getResources("META-INF/jar.mf").size() == 1;
//        assert loader.getResources("META-INF/ReadMe.txt").size() == 1;
//    }
//
//    @Test
//    public void importLoaderTest7() throws Exception {
//        beforeImport();
//        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/app/onlybasic/FooOnlyBasic.java"));
//        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/onlyjar/FooOnlyJar.java"));
//        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-2.zip/app/onlyzip/FooOnlyZip.java"));
//        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/META-INF/jar.mf"));
//        byte[] byteDat5 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/META-INF/ReadMe.txt"));
//
//        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourcesAsStream("app/onlybasic/FooOnlyBasic.java").get(0)));
//        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourcesAsStream("app/onlyjar/FooOnlyJar.java").get(0)));
//        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourcesAsStream("app/onlyzip/FooOnlyZip.java").get(0)));
//        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourcesAsStream("META-INF/jar.mf").get(0)));
//        assert Objects.deepEquals(byteDat5, IOUtils.toByteArray(loader.getResourcesAsStream("META-INF/ReadMe.txt").get(0)));
//    }
//
//    @Test
//    public void importLoaderTest8() throws Exception {
//        beforeImport();
//        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/app/onlybasic/FooOnlyBasic.java"));
//        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/onlyjar/FooOnlyJar.java"));
//        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-2.zip/app/onlyzip/FooOnlyZip.java"));
//        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/META-INF/jar.mf"));
//        byte[] byteDat5 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/META-INF/ReadMe.txt"));
//
//        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResource("app/onlybasic/FooOnlyBasic.java").openStream()));
//        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResource("app/onlyjar/FooOnlyJar.java").openStream()));
//        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResource("app/onlyzip/FooOnlyZip.java").openStream()));
//        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResource("META-INF/jar.mf").openStream()));
//        assert Objects.deepEquals(byteDat5, IOUtils.toByteArray(loader.getResource("META-INF/ReadMe.txt").openStream()));
//    }
//
//    @Test
//    public void importLoaderTest9() throws Exception {
//        beforeImport();
//        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/basic.jar/app/onlybasic/FooOnlyBasic.java"));
//        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-1.jar/app/onlyjar/FooOnlyJar.java"));
//        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("dir/app-2.zip/app/onlyzip/FooOnlyZip.java"));
//
//        class Info {
//
//            public final String      name;
//            public final InputStream stream;
//
//            public Info(String name, InputStream stream){
//                this.name = name;
//                this.stream = stream;
//            }
//        }
//
//        ResourceLoader.Scanner<Info> scanner = event -> {
//            if (event.getName().startsWith("app/only") && event.getName().endsWith(".java")) {
//                return new Info(event.getName(), event.getStream());
//            } else {
//                return null;
//            }
//        };
//
//        assert Objects.deepEquals(//
//                IOUtils.toByteArray(loader.scanOneResource(scanner).stream), //
//                IOUtils.toByteArray(loader.scanOneResource(scanner).stream)//
//        );
//
//        Map<String, InputStream> objects = new LinkedHashMap<>();
//        List<Info> inputInfos = loader.scanResources(scanner);
//        for (Info info : inputInfos) {
//            objects.put(info.name, info.stream);
//        }
//
//        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(objects.get("app/onlybasic/FooOnlyBasic.java")));
//        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(objects.get("app/onlyjar/FooOnlyJar.java")));
//        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(objects.get("app/onlyzip/FooOnlyZip.java")));
//    }
//
//    @Test
//    public void importLoaderTest10() throws Exception {
//        beforeImport();
//        assert loader.scanOneResource(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }) == null;
//        assert loader.scanResources(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }).isEmpty();
//    }
//
//    @Test
//    public void importLoaderTest11() throws Exception {
//        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
//        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
//        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
//        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
//
//        ImportResourceLoader loader = new ImportResourceLoader(ISOLATION);
//        loader.importResources("ApplicationForApp1.class", loader1);
//        loader.importResources("ApplicationForApp2.class", loader2);
//
//        Object applicationForApp1 = loader.getClass("ApplicationForApp1").newInstance();
//        Object applicationForApp2 = loader.getClass("ApplicationForApp2").newInstance();
//        assert applicationForApp1 != null;
//        assert applicationForApp2 != null;
//    }
//
//    @Test
//    public void importLoaderTest12() throws Exception {
//        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
//        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
//        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
//        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
//
//        ImportResourceLoader loader = new ImportResourceLoader(ISOLATION);
//        loader.importResources("ApplicationForApp1.class", loader1);
//        loader.importResources("ApplicationForApp2.class", loader2);
//
//        List<Object> list1 = loader.scanResources(ResourceLoader.ScanEvent::getName);
//        assert list1.size() == 2;
//
//        List<Object> list2 = loader.scanResources(ResourceLoader.MatchType.Suffix, ResourceLoader.ScanEvent::getName, new String[] { "App1.class" });
//        assert list2.contains("ApplicationForApp1.class");
//    }
//
//    @Test
//    public void importLoaderTest13() throws Exception {
//        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
//        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
//        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
//        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
//
//        ImportResourceLoader loader = new ImportResourceLoader(ISOLATION);
//        loader.importResources("ApplicationForApp1.class", loader1);
//        loader.importResources("Application.class", loader1);
//        loader.importResources("ApplicationForApp2.class", loader2);
//
//        Object obj1 = loader.getClass("Application").newInstance();
//        try {
//            Object result = ((Function) obj1).apply("hello");
//            assert result.toString().equals("FooForApp1 running...");
//        } catch (Exception e) {
//            assert false;
//        }
//    }
//
//    @Test
//    public void importLoaderTest14() throws Exception {
//        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
//        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
//        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
//        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
//
//        ImportResourceLoader loader = new ImportResourceLoader(ISOLATION);
//        loader.importResources("ApplicationForApp1.class", loader1);
//        loader.importResources("ApplicationForApp2.class", loader2);
//        loader.importResources("Application.class", loader2);
//
//        Object obj1 = loader.getClass("Application").newInstance();
//        try {
//            Object result = ((Function) obj1).apply("hello");
//            assert result.toString().equals("FooForApp2 running...");
//        } catch (Exception e) {
//            assert false;
//        }
//    }
//
//    @Test
//    public void importLoaderTest15() throws Exception {
//        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
//        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
//        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
//        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
//
//        ImportResourceLoader loader = new ImportResourceLoader(MERGE);
//        loader.importResources("ApplicationForApp1.class", loader1);
//        loader.importResources("ApplicationForApp2.class", loader2);
//        loader.importResources("Application.class", loader2);
//
//        Object obj1 = loader.getClass("Application").newInstance();
//        try {
//            Object result = ((Function) obj1).apply("hello");
//            assert result.toString().equals("FooForApp1 running...");
//        } catch (Exception e) {
//            assert false;
//        }
//
//        try {
//            loader.getClass("lib/Foo");
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof ClassNotFoundException;
//        }
//    }
//}
