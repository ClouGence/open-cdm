package com.clougence.utils.loader.providers;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Driver;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import com.clougence.utils.ObjectUtils;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.jar.JarFile;
import com.clougence.utils.loader.ResourceLoader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JarFile}.
 */
public class JarResourceLoaderTests {

    @Test
    public void jarLoaderTest1() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.exist("app/FooImpl.class");
        assert loader.exist("app/FooImpl.java");
        assert loader.exist("META-INF/jar.mf");
        assert loader.exist("META-INF/ReadMe.txt");
        assert !loader.exist("abc/abc.text");
    }

    @Test
    public void jarLoaderTest2() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.getResource("app/FooImpl.class").toString().endsWith("jars/app-1.jar!/app/FooImpl.class");
        assert loader.getResource("app/FooImpl.java").toString().endsWith("jars/app-1.jar!/app/FooImpl.java");
        assert loader.getResource("META-INF/jar.mf").toString().endsWith("jars/app-1.jar!/META-INF/jar.mf");
        assert loader.getResource("META-INF/ReadMe.txt").toString().endsWith("jars/app-1.jar!/META-INF/ReadMe.txt");
    }

    @Test
    public void jarLoaderTest3() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.class")));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.java")));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/jar.mf")));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/ReadMe.txt")));
        assert loader.getResourceAsStream("abc/abc.text") == null;
    }

    @Test
    public void jarLoaderTest4() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.getResources("app/FooImpl.class").get(0).toString().endsWith("jars/app-1.jar!/app/FooImpl.class");
        assert loader.getResources("app/FooImpl.java").get(0).toString().endsWith("jars/app-1.jar!/app/FooImpl.java");
        assert loader.getResources("META-INF/jar.mf").get(0).toString().endsWith("jars/app-1.jar!/META-INF/jar.mf");
        assert loader.getResources("META-INF/ReadMe.txt").get(0).toString().endsWith("jars/app-1.jar!/META-INF/ReadMe.txt");
    }

    @Test
    public void jarLoaderTest5() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourcesAsStream("app/FooImpl.class").get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourcesAsStream("app/FooImpl.java").get(0)));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourcesAsStream("META-INF/jar.mf").get(0)));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourcesAsStream("META-INF/ReadMe.txt").get(0)));
    }

    @Test
    public void jarLoaderTest6() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/jar.mf"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/ReadMe.txt"));

        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResource("app/FooImpl.class").openStream()));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResource("app/FooImpl.java").openStream()));
        byte[] byteArray = IOUtils.toByteArray(loader.getResource("META-INF/jar.mf").openStream());
        String x = new String(byteArray);
        System.out.println(x);
        String x1 = new String(byteDat3);
        System.out.println(x1);
        System.out.println(x.equals(x1));
        assert Objects.deepEquals(byteDat3, byteArray);
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResource("META-INF/ReadMe.txt").openStream()));
    }

    @Test
    public void jarLoaderTest7() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/FooImpl.java"));
        ResourceLoader.Scanner<InputStream> scanner = event -> {
            if (event.getName().startsWith("app/F")) {
                return event.getStream();
            } else {
                return null;
            }
        };

        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        InputStream inputStream = loader.scanOneResource(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStream));

        List<InputStream> inputStreams = loader.scanResources(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStreams.get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(inputStreams.get(1)));
    }

    @Test
    public void jarLoaderTest8() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.scanOneResource(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }) == null;
        assert loader.scanResources(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }).isEmpty();
    }

    @Test
    public void nestedJar_1() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/basic.jar/app/lib/Foo.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/app/onlyjar/FooOnlyJar.class"));
        byte[] byteDat3 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/onlyzip/FooOnlyZip.class"));

        String file = ResourcesUtils.getResource("/nested-jars/nested-package-all.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file), Arrays.asList("basic.jar", "app-1.jar", "app-2.zip"));

        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("app/lib/Foo.class")));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("app/onlyjar/FooOnlyJar.class")));
        assert Objects.deepEquals(byteDat3, IOUtils.toByteArray(loader.getResourceAsStream("app/onlyzip/FooOnlyZip.class")));
    }

    @Test
    public void nestedJar_2() throws Exception {
        //        String file = ResourcesUtils.getResource("/nested-jars/nested-package-all.jar").getFile();
        //        JarResourceLoader loader = new JarResourceLoader(new File(file), Arrays.asList("basic.jar", "app-1.jar", "app-2.zip"));
        //
        //        Class<?> aClass1 = loader.getClass("app.lib.Foo");
        //        Class<?> aClass2 = loader.getClass("app.onlyjar.FooOnlyJar");
        //        Class<?> aClass3 = loader.getClass("app.onlyzip.FooOnlyZip");
        //
        //        assert aClass1.getClassLoader() == aClass2.getClassLoader();
        //        assert aClass2.getClassLoader() == aClass3.getClassLoader();
        //
        //        assert aClass1.isAssignableFrom(aClass2);
        //        assert aClass1.isAssignableFrom(aClass3);
    }

    @Test
    public void loadServiceNameTest() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-1.jar").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        URL resource = loader.getResource("META-INF/services/java.sql.Driver");
        byte[] bytes1 = IOUtils.toByteArray(resource);

        byte[] bytes2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-1.jar/META-INF/services/java.sql.Driver"));

        assert Objects.deepEquals(bytes1, bytes2);

    }
}
