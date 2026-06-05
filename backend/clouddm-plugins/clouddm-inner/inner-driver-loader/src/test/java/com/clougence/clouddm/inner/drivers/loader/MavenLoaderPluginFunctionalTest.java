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
package com.clougence.clouddm.inner.drivers.loader;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.def.VerDef;
import com.clougence.drivers.factory.DefaultDriverLoader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MavenLoaderPluginFunctionalTest {

    private Path       tempDir;
    private HttpServer httpServer;

    @Before
    public void setUp() throws Exception {
        this.tempDir = Files.createTempDirectory("inner-driver-loader-test");
    }

    @After
    public void tearDown() throws Exception {
        if (this.httpServer != null) {
            this.httpServer.stop(0);
            this.httpServer = null;
        }
        if (this.tempDir != null) {
            deleteRecursively(this.tempDir);
        }
    }

    @Test
    public void registerMavenResourcePreparer_onLoaderInstance_shouldEnableMavenPreparation() throws Exception {
        byte[] rootJarBytes = "fake-maven-jar".getBytes(StandardCharsets.UTF_8);
        byte[] dependencyJarBytes = "fake-maven-dependency".getBytes(StandardCharsets.UTF_8);
        String baseUrl = startHttpServer(rootJarBytes, dependencyJarBytes);

        Properties config = new Properties();
        config.setProperty(MavenResourcePreparer.REPOSITORY_KEY, baseUrl + "/repo");

        DefaultDriverLoader unregisteredLoader = new DefaultDriverLoader(this.tempDir.toFile(), config);
        unregisteredLoader.loadDriverXml(TestDriversXml.mavenOnly("plugin-driver", "1.0.0", "com.example:demo-artifact:1.0.0"));
        DriverVersion missingVersion = unregisteredLoader.findDriver("plugin-driver", "1.0.0");
        assertNotNull(missingVersion);
        try {
            unregisteredLoader.prepareDriverVersion(missingVersion, resource -> false, new DriverPrepareProgress() {
            });
            fail("maven resource should require plugin registration");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("unsupported resourceType: maven"));
        }

        unregisteredLoader.registerPreparer("maven", MavenResourcePreparer::new);
        ProgressRecorder progress = new ProgressRecorder();
        unregisteredLoader.prepareDriverVersion(missingVersion, resource -> false, progress);

        Path jarPath = this.tempDir.resolve("plugin-driver").resolve("1.0.0").resolve("demo-artifact-1.0.0.jar");
        Path dependencyPath = this.tempDir.resolve("plugin-driver").resolve("1.0.0").resolve("demo-dependency-1.0.0.jar");
        assertTrue(Files.exists(jarPath));
        assertTrue(Files.exists(dependencyPath));
        assertEquals(2, missingVersion.getFiles().size());
        assertTrue(missingVersion.getFiles().stream().anyMatch(file -> "demo-artifact-1.0.0.jar".equals(file.getRelativePath())));
        assertTrue(missingVersion.getFiles().stream().anyMatch(file -> "demo-dependency-1.0.0.jar".equals(file.getRelativePath())));
        assertEquals(1, progress.started.size());
        assertEquals(1, progress.completed.size());
        assertTrue(progress.errors.isEmpty());
    }

    @Test
    public void mavenPreparePre_shouldCollectJarFilesWithoutDownloadingJars() throws Exception {
        byte[] rootJarBytes = "fake-maven-jar".getBytes(StandardCharsets.UTF_8);
        byte[] dependencyJarBytes = "fake-maven-dependency".getBytes(StandardCharsets.UTF_8);
        String baseUrl = startHttpServer(rootJarBytes, dependencyJarBytes);

        Properties config = new Properties();
        config.setProperty(MavenResourcePreparer.REPOSITORY_KEY, baseUrl + "/repo");

        VerDef version = new VerDef();
        version.setFamilyName("plugin-driver");
        version.setVersion("1.0.0");
        version.setLocalDir(this.tempDir.toFile());

        ResDef resource = new ResDef();
        resource.setResourceType("maven");
        resource.setCoordinate("com.example:demo-artifact:1.0.0");

        MavenResourcePreparer preparer = new MavenResourcePreparer(this.tempDir.toFile(), config);
        preparer.analysis(version, resource, null, null);

        Path versionDir = this.tempDir.resolve("plugin-driver").resolve("1.0.0");
        Path filesIndex = versionDir.resolve("files.idx");
        Path jarPath = this.tempDir.resolve("plugin-driver").resolve("1.0.0").resolve("demo-artifact-1.0.0.jar");
        Path dependencyPath = this.tempDir.resolve("plugin-driver").resolve("1.0.0").resolve("demo-dependency-1.0.0.jar");
        assertTrue(Files.exists(versionDir));
        assertTrue(Files.exists(filesIndex));
        assertFalse(Files.exists(jarPath));
        assertFalse(Files.exists(dependencyPath));

        assertNotNull(resource.getFileDefList());
        assertEquals(2, resource.getFileDefList().size());
        assertTrue(resource.getFileDefList().stream().allMatch(MavenFileDef.class::isInstance));

        MavenFileDef rootFile = resource.getFileDefList()
            .stream()
            .map(MavenFileDef.class::cast)
            .filter(file -> "demo-artifact-1.0.0.jar".equals(file.getRelativePath()))
            .findFirst()
            .orElse(null);
        MavenFileDef dependencyFile = resource.getFileDefList()
            .stream()
            .map(MavenFileDef.class::cast)
            .filter(file -> "demo-dependency-1.0.0.jar".equals(file.getRelativePath()))
            .findFirst()
            .orElse(null);
        assertNotNull(rootFile);
        assertNotNull(dependencyFile);
        assertEquals("demo-artifact-1.0.0.jar", rootFile.getRelativePath());
        assertEquals(this.tempDir.resolve("plugin-driver/1.0.0/demo-artifact-1.0.0.jar").toFile().getAbsolutePath(), rootFile.getAbsolutePath());
        assertEquals(baseUrl + "/repo/com/example/demo-artifact/1.0.0/demo-artifact-1.0.0.jar", rootFile.getUrl());
        assertEquals("demo-dependency-1.0.0.jar", dependencyFile.getRelativePath());
        assertEquals(this.tempDir.resolve("plugin-driver/1.0.0/demo-dependency-1.0.0.jar").toFile().getAbsolutePath(), dependencyFile.getAbsolutePath());
        assertEquals(baseUrl + "/repo/com/example/demo-dependency/1.0.0/demo-dependency-1.0.0.jar", dependencyFile.getUrl());
    }

    @Test
    public void mavenPreparePre_shouldUseSanitizedDriverVersionDir() throws Exception {
        byte[] rootJarBytes = "fake-maven-jar".getBytes(StandardCharsets.UTF_8);
        byte[] dependencyJarBytes = "fake-maven-dependency".getBytes(StandardCharsets.UTF_8);
        String baseUrl = startHttpServer(rootJarBytes, dependencyJarBytes);

        Properties config = new Properties();
        config.setProperty(MavenResourcePreparer.REPOSITORY_KEY, baseUrl + "/repo");

        VerDef version = new VerDef();
        version.setFamilyName("MySQL Connector/J");
        version.setVersion("5.1.49");
        version.setLocalDir(this.tempDir.toFile());

        ResDef resource = new ResDef();
        resource.setResourceType("maven");
        resource.setCoordinate("mysql:mysql-connector-java:5.1.49");

        MavenResourcePreparer preparer = new MavenResourcePreparer(this.tempDir.toFile(), config);
        preparer.analysis(version, resource, null, null);

        assertNotNull(resource.getFileDefList());
        assertFalse(resource.getFileDefList().isEmpty());
        assertTrue(resource.getFileDefList().stream().allMatch(file -> file.getAbsolutePath().contains("MySQL Connector J" + File.separator + "5.1.49")));
        assertTrue(resource.getFileDefList().stream().noneMatch(file -> file.getAbsolutePath().contains("MySQL Connector" + File.separator + "J" + File.separator + "5.1.49")));
    }

    @Test
    public void mavenPreparePre_shouldCacheAnalysisResultInSameRuntime() throws Exception {
        byte[] rootJarBytes = "fake-maven-jar".getBytes(StandardCharsets.UTF_8);
        byte[] dependencyJarBytes = "fake-maven-dependency".getBytes(StandardCharsets.UTF_8);
        AtomicInteger requestCounter = new AtomicInteger();
        String baseUrl = startHttpServer(rootJarBytes, dependencyJarBytes, requestCounter);

        Properties config = new Properties();
        config.setProperty(MavenResourcePreparer.REPOSITORY_KEY, baseUrl + "/repo");

        VerDef version = new VerDef();
        version.setFamilyName("plugin-driver");
        version.setVersion("1.0.0");
        version.setLocalDir(this.tempDir.toFile());

        ResDef firstResource = new ResDef();
        firstResource.setResourceType("maven");
        firstResource.setCoordinate("com.example:demo-artifact:1.0.0");

        MavenResourcePreparer preparer = new MavenResourcePreparer(this.tempDir.toFile(), config);
        preparer.analysis(version, firstResource, null, null);
        int requestCountAfterFirstAnalysis = requestCounter.get();
        assertTrue(requestCountAfterFirstAnalysis > 0);

        ResDef secondResource = new ResDef();
        secondResource.setResourceType("maven");
        secondResource.setCoordinate("com.example:demo-artifact:1.0.0");
        preparer.analysis(version, secondResource, null, null);

        assertEquals(requestCountAfterFirstAnalysis, requestCounter.get());
        assertNotSame(firstResource.getFileDefList(), secondResource.getFileDefList());
        assertEquals(firstResource.getFileDefList().size(), secondResource.getFileDefList().size());
    }

    @Test
    public void refreshDriverVersion_shouldRecoverMavenFilesFromFilesIdxAfterRestart() throws Exception {
        byte[] rootJarBytes = "fake-maven-jar".getBytes(StandardCharsets.UTF_8);
        byte[] dependencyJarBytes = "fake-maven-dependency".getBytes(StandardCharsets.UTF_8);
        String baseUrl = startHttpServer(rootJarBytes, dependencyJarBytes);

        Properties config = new Properties();
        config.setProperty(MavenResourcePreparer.REPOSITORY_KEY, baseUrl + "/repo");

        DefaultDriverLoader prepareLoader = new DefaultDriverLoader(this.tempDir.toFile(), config);
        prepareLoader.registerPreparer("maven", MavenResourcePreparer::new);
        prepareLoader.loadDriverXml(TestDriversXml.mavenOnly("restart-driver", "1.0.0", "com.example:demo-artifact:1.0.0"));

        DriverVersion preparedVersion = prepareLoader.findDriver("restart-driver", "1.0.0");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {
        });

        Path versionDir = this.tempDir.resolve("restart-driver").resolve("1.0.0");
        assertTrue(Files.exists(versionDir.resolve("files.idx")));
        assertEquals(2, preparedVersion.getFiles().size());

        this.httpServer.stop(0);
        this.httpServer = null;

        DefaultDriverLoader refreshLoader = new DefaultDriverLoader(this.tempDir.toFile(), config);
        refreshLoader.registerPreparer("maven", MavenResourcePreparer::new);
        refreshLoader.loadDriverXml(TestDriversXml.mavenOnly("restart-driver", "1.0.0", "com.example:demo-artifact:1.0.0"));

        DriverVersion refreshedVersion = refreshLoader.findDriver("restart-driver", "1.0.0");
        assertNotNull(refreshedVersion);
        assertTrue(refreshedVersion.getFiles().isEmpty());

        refreshLoader.refreshDriverVersion(refreshedVersion);

        assertTrue(refreshedVersion.isPrepared());
        assertEquals(2, refreshedVersion.getFiles().size());
        assertTrue(refreshedVersion.getFiles().stream().allMatch(file -> file.isPrepared()));
        assertTrue(refreshedVersion.getFiles().stream().anyMatch(file -> "demo-artifact-1.0.0.jar".equals(file.getRelativePath())));
        assertTrue(refreshedVersion.getFiles().stream().anyMatch(file -> "demo-dependency-1.0.0.jar".equals(file.getRelativePath())));
    }

    private String startHttpServer(byte[] rootJarBytes, byte[] dependencyJarBytes) throws Exception {
        return startHttpServer(rootJarBytes, dependencyJarBytes, null);
    }

    private String startHttpServer(byte[] rootJarBytes, byte[] dependencyJarBytes, AtomicInteger requestCounter) throws Exception {
        this.httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        this.httpServer.createContext("/repo/com/example/demo-artifact/1.0.0/demo-artifact-1.0.0.jar", new ByteArrayHandler(rootJarBytes, requestCounter));
        this.httpServer.createContext("/repo/com/example/demo-artifact/1.0.0/demo-artifact-1.0.0.pom", new ByteArrayHandler(rootPomBytes(), requestCounter));
        this.httpServer.createContext("/repo/com/example/demo-dependency/1.0.0/demo-dependency-1.0.0.jar", new ByteArrayHandler(dependencyJarBytes, requestCounter));
        this.httpServer.createContext("/repo/com/example/demo-dependency/1.0.0/demo-dependency-1.0.0.pom", new ByteArrayHandler(dependencyPomBytes(), requestCounter));
        this.httpServer.start();
        return "http://127.0.0.1:" + this.httpServer.getAddress().getPort();
    }

    private byte[] rootPomBytes() {
        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                     + "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">" + "<modelVersion>4.0.0</modelVersion>"
                     + "<groupId>com.example</groupId>" + "<artifactId>demo-artifact</artifactId>" + "<version>1.0.0</version>" + "<dependencies>" + "<dependency>"
                     + "<groupId>com.example</groupId>" + "<artifactId>demo-dependency</artifactId>" + "<version>1.0.0</version>" + "</dependency>" + "</dependencies>"
                     + "</project>";
        return pom.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] dependencyPomBytes() {
        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                     + "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">" + "<modelVersion>4.0.0</modelVersion>"
                     + "<groupId>com.example</groupId>" + "<artifactId>demo-dependency</artifactId>" + "<version>1.0.0</version>" + "</project>";
        return pom.getBytes(StandardCharsets.UTF_8);
    }

    private void deleteRecursively(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static final class ProgressRecorder implements DriverPrepareProgress {

        private final List<String> started        = new ArrayList<>();
        private final List<String> completed      = new ArrayList<>();
        private final List<String> errors         = new ArrayList<>();
        private final List<String> progressEvents = new ArrayList<>();

        @Override
        public void onStart(DriverVersion driverVersion, ResDef driverResource, int index, int totalCount) {
            this.started.add(driverResource.getCoordinate());
        }

        @Override
        public void onComplete(DriverVersion driverVersion, ResDef resource, int index, int totalCount) {
            this.completed.add(resource.getCoordinate());
        }

        @Override
        public void onError(DriverVersion driverVersion, ResDef resource, Exception exception) {
            this.errors.add(resource.getCoordinate() + ":" + exception.getClass().getSimpleName());
        }
    }

    private static final class TestDriversXml {

        private static java.io.InputStream mavenOnly(String family, String version, String coordinate) {
            // @formatter:off
            String xml = "<drivers>"
                + "<driver driverFamily=\"" + family + "\" version=\"" + version + "\">"
                + "<resource type=\"maven\">" + coordinate + "</resource>"
                + "</driver>"
                + "</drivers>";
            return new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            // @formatter:on
        }
    }

    private static final class ByteArrayHandler implements HttpHandler {

        private final byte[]        content;
        private final AtomicInteger requestCounter;

        private ByteArrayHandler(byte[] content, AtomicInteger requestCounter){
            this.content = content;
            this.requestCounter = requestCounter;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (this.requestCounter != null) {
                this.requestCounter.incrementAndGet();
            }
            exchange.sendResponseHeaders(200, this.content.length);
            exchange.getResponseBody().write(this.content);
            exchange.close();
        }
    }
}
