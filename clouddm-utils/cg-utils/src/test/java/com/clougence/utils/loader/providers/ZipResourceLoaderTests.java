package com.clougence.utils.loader.providers;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.jar.JarFile;
import com.clougence.utils.loader.ResourceLoader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JarFile}.
 */
public class ZipResourceLoaderTests {

    @Test
    public void zipLoaderTest1() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.exist("app/FooImpl.class");
        assert loader.exist("app/FooImpl.java");
        assert loader.exist("META-INF/ReadMe.txt");
        assert !loader.exist("abc/abc.text");
    }

    @Test
    public void zipLoaderTest2() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.getResource("app/FooImpl.class").toString().endsWith("jars/app-2.zip!/app/FooImpl.class");
        assert loader.getResource("app/FooImpl.java").toString().endsWith("jars/app-2.zip!/app/FooImpl.java");
        assert loader.getResource("META-INF/ReadMe.txt").toString().endsWith("jars/app-2.zip!/META-INF/ReadMe.txt");
    }

    @Test
    public void zipLoaderTest3() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/META-INF/ReadMe.txt"));

        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.class")));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourceAsStream("app/FooImpl.java")));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourceAsStream("META-INF/ReadMe.txt")));
        assert loader.getResourceAsStream("abc/abc.text") == null;
    }

    @Test
    public void zipLoaderTest4() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.getResources("app/FooImpl.class").get(0).toString().endsWith("jars/app-2.zip!/app/FooImpl.class");
        assert loader.getResources("app/FooImpl.java").get(0).toString().endsWith("jars/app-2.zip!/app/FooImpl.java");
        assert loader.getResources("META-INF/ReadMe.txt").get(0).toString().endsWith("jars/app-2.zip!/META-INF/ReadMe.txt");
    }

    @Test
    public void zipLoaderTest5() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/META-INF/ReadMe.txt"));

        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResourcesAsStream("app/FooImpl.class").get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResourcesAsStream("app/FooImpl.java").get(0)));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResourcesAsStream("META-INF/ReadMe.txt").get(0)));
    }

    @Test
    public void zipLoaderTest6() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        byte[] byteDat4 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/META-INF/ReadMe.txt"));

        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(loader.getResource("app/FooImpl.class").openStream()));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(loader.getResource("app/FooImpl.java").openStream()));
        assert Objects.deepEquals(byteDat4, IOUtils.toByteArray(loader.getResource("META-INF/ReadMe.txt").openStream()));
    }

    @Test
    public void zipLoaderTest7() throws Exception {
        byte[] byteDat1 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.class"));
        byte[] byteDat2 = IOUtils.toByteArray(ResourcesUtils.getResourceAsStream("/dir/app-2.zip/app/FooImpl.java"));
        ResourceLoader.Scanner<InputStream> scanner = event -> {
            if (event.getName().startsWith("app/F")) {
                return event.getStream();
            } else {
                return null;
            }
        };

        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        InputStream inputStream = loader.scanOneResource(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStream));

        List<InputStream> inputStreams = loader.scanResources(scanner);
        assert Objects.deepEquals(byteDat1, IOUtils.toByteArray(inputStreams.get(0)));
        assert Objects.deepEquals(byteDat2, IOUtils.toByteArray(inputStreams.get(1)));
    }

    @Test
    public void zipLoaderTest8() throws Exception {
        String file = ResourcesUtils.getResource("/jars/app-2.zip").getFile();
        JarResourceLoader loader = new JarResourceLoader(new File(file));
        assert loader.scanOneResource(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }) == null;
        assert loader.scanResources(ResourceLoader.MatchType.Prefix, ResourceLoader.ScanEvent::getStream, new String[] { "net.hasor" }).isEmpty();
    }
}
