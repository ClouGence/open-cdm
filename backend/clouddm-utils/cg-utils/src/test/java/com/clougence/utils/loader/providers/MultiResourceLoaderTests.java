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
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.jar.JarFile;
import com.clougence.utils.loader.ResourceLoader;

/**
 * Tests for {@link JarFile}.
 */
public class MultiResourceLoaderTests {

    @Test
    public void multiLoaderTest1() throws Exception {
        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        assert loader.exist("app/FooImpl.class");
        assert loader.exist("app/FooImpl.java");
        assert loader.exist("META-INF/jar.mf");
        assert loader.exist("META-INF/ReadMe.txt");
        assert !loader.exist("abc/abc.text");
    }

    @Test
    public void multiLoaderTest2() throws Exception {
        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        assert loader.getResource("app/FooImpl.class").toString().endsWith("jars/app-1.jar!/app/FooImpl.class");
        assert loader.getResource("app/FooImpl.java").toString().endsWith("jars/app-1.jar!/app/FooImpl.java");
        assert loader.getResource("META-INF/jar.mf").toString().endsWith("jars/app-1.jar!/META-INF/jar.mf");
        assert loader.getResource("META-INF/ReadMe.txt").toString().endsWith("jars/app-1.jar!/META-INF/ReadMe.txt");
    }

    @Test
    public void multiLoaderTest2_1() throws Exception {
        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader2, loader1);

        assert loader.getResource("app/FooImpl.class").toString().endsWith("jars/app-2.zip!/app/FooImpl.class");
        assert loader.getResource("app/FooImpl.java").toString().endsWith("jars/app-2.zip!/app/FooImpl.java");
        assert loader.getResource("META-INF/jar.mf").toString().endsWith("jars/app-1.jar!/META-INF/jar.mf");
        assert loader.getResource("META-INF/ReadMe.txt").toString().endsWith("jars/app-2.zip!/META-INF/ReadMe.txt");
    }

    @Test
    public void multiLoaderTest3() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.class")));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.java")));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/jar.mf")));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/ReadMe.txt")));
        assert loader.getResourceAsStream("abc/abc.text") == null;
    }

    @Test
    public void multiLoaderTest3_1() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/META-INF/ReadMe.txt"));

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader2, loader1);

        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.class")));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.java")));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/jar.mf")));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/ReadMe.txt")));
        assert loader.getResourceAsStream("abc/abc.text") == null;
    }

    @Test
    public void multiLoaderTest4() throws Exception {
        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        assert loader.getResources("app/FooImpl.class").get(0).toString().endsWith("jars/app-1.jar!/app/FooImpl.class");
        assert loader.getResources("app/FooImpl.java").get(0).toString().endsWith("jars/app-1.jar!/app/FooImpl.java");
        assert loader.getResources("META-INF/jar.mf").get(0).toString().endsWith("jars/app-1.jar!/META-INF/jar.mf");
        assert loader.getResources("META-INF/ReadMe.txt").get(0).toString().endsWith("jars/app-1.jar!/META-INF/ReadMe.txt");
        assert loader.getResources("app/FooImpl.class").get(1).toString().endsWith("jars/app-2.zip!/app/FooImpl.class");
        assert loader.getResources("app/FooImpl.java").get(1).toString().endsWith("jars/app-2.zip!/app/FooImpl.java");
        assert loader.getResources("META-INF/jar.mf").size() == 1;
        assert loader.getResources("META-INF/ReadMe.txt").get(1).toString().endsWith("jars/app-2.zip!/META-INF/ReadMe.txt");
    }

    @Test
    public void multiLoaderTest5() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        byte[] byteDat5 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat6 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        byte[] byteDat8 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/META-INF/ReadMe.txt"));

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        List<InputStream> streams1 = loader.getResourcesAsStream("app/FooImpl.class");
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(streams1.get(0)));
        assert Objects.deepEquals(byteDat5, IOUtils.toByteArray(streams1.get(1)));
        List<InputStream> streams2 = loader.getResourcesAsStream("app/FooImpl.java");
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(streams2.get(0)));
        assert Objects.deepEquals(byteDat6, IOUtils.toByteArray(streams2.get(1)));
        List<InputStream> streams3 = loader.getResourcesAsStream("META-INF/ReadMe.txt");
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(streams3.get(0)));
        assert Objects.deepEquals(byteDat8, IOUtils.toByteArray(streams3.get(1)));

        List<InputStream> streams4 = loader.getResourcesAsStream("META-INF/jar.mf");
        assert streams4.size() == 1;
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(streams4.get(0)));
    }

    @Test
    public void multiLoaderTest6() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResource("app/FooImpl.class").openStream()));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResource("app/FooImpl.java").openStream()));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResource("META-INF/jar.mf").openStream()));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResource("META-INF/ReadMe.txt").openStream()));
    }

    @Test
    public void multiLoaderTest6_1() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/META-INF/ReadMe.txt"));

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader2, loader1);

        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResource("app/FooImpl.class").openStream()));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResource("app/FooImpl.java").openStream()));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResource("META-INF/jar.mf").openStream()));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResource("META-INF/ReadMe.txt").openStream()));
    }

    @Test
    public void multiLoaderTest7() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        ResourceLoader.Scanner<InputStream> scanner = event -> {
            if (event.getName().startsWith("app/F")) {
                return event.getStream();
            } else {
                return null;
            }
        };

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader1, loader2);

        InputStream inputStream = loader.scanOneResource(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStream));

        List<InputStream> inputStreams = loader.scanResources(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStreams.get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(inputStreams.get(1)));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(inputStreams.get(2)));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(inputStreams.get(3)));
    }

    @Test
    public void multiLoaderTest7_1() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        ResourceLoader.Scanner<InputStream> scanner = event -> {
            if (event.getName().startsWith("app/F")) {
                return event.getStream();
            } else {
                return null;
            }
        };

        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader2, loader1);

        InputStream inputStream = loader.scanOneResource(scanner);
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(inputStream));

        List<InputStream> inputStreams = loader.scanResources(scanner);
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(inputStreams.get(0)));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(inputStreams.get(1)));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStreams.get(2)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(inputStreams.get(3)));
    }

    @Test
    public void multiLoaderTest8() throws Exception {
        String file1 = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        String file2 = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader1 = new JarResourceLoader(new File(file1));
        JarResourceLoader loader2 = new JarResourceLoader(new File(file2));
        MultiResourceLoader loader = new MultiResourceLoader(loader2, loader1);

        assert loader.scanOneResource(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }) == null;
        assert loader.scanResources(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }).isEmpty();
    }

    @Test
    public void multiLoaderTest9() throws Exception {
        //        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
        //        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
        //
        //        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
        //        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
        //
        //        MultiResourceLoader loader = new MultiResourceLoader();
        //        loader.addLoader(loader1);
        //        loader.addLoader(loader2);
        //
        //        Object obj1 = loader.getClass("ApplicationForApp1").newInstance();
        //        Object obj2 = loader.getClass("ApplicationForApp2").newInstance();
        //
        //        assert obj1 instanceof Function;
        //        assert obj2 instanceof Function;
        //
        //        Object result1 = ((Function) obj1).apply("hello");
        //        assert result1.toString().equals("FooForApp1 running...");
        //
        //        Object result2 = ((Function) obj2).apply("hello");
        //        assert result2.toString().equals("FooForApp2 running...");
    }

    @Test
    public void multiLoaderTest10() throws Exception {
        //        String dir1 = ResourcesUtils.getResource("/dir/isol/app-1").getFile();
        //        PathResourceLoader loader1 = new PathResourceLoader(new File(dir1));
        //
        //        String dir2 = ResourcesUtils.getResource("/dir/isol/app-2").getFile();
        //        PathResourceLoader loader2 = new PathResourceLoader(new File(dir2));
        //
        //        MultiResourceLoader loader = new MultiResourceLoader();
        //        loader.addLoader(loader1);
        //        loader.addLoader(loader2);
        //
        //        Object obj1 = loader.getClass("ApplicationForApp1").newInstance();
        //        Object obj2 = loader.getClass("ApplicationForApp2").newInstance();
        //
        //        assert obj1 instanceof Function;
        //        assert obj2 instanceof Function;
        //
        //        try {
        //            Object result = ((Function) obj1).apply("hello");
        //            assert result.toString().equals("FooForApp1 running...");
        //        } catch (Exception e) {
        //            assert false;
        //        }
        //
        //        try {
        //            ((Function) obj2).apply("hello");
        //            assert false;
        //        } catch (NoSuchMethodError e) {
        //            boolean jvm9Test = e.getMessage().equals("'java.lang.String lib.Foo.run(java.lang.String[])'");
        //            boolean jvm6Test = e.getMessage().equals("lib.Foo.run([Ljava/lang/String;)Ljava/lang/String;");
        //            assert jvm9Test || jvm6Test;
        //        }
    }
}
