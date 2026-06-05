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
package com.clougence.drivers.factory;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.clougence.drivers.*;
import com.clougence.drivers.def.FileDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.def.VerDef;
import com.clougence.drivers.factory.prepare.AbstractResourcePreparer;
import com.clougence.drivers.factory.prepare.ClassResourcePreparer;
import com.clougence.drivers.factory.prepare.FileResourcePreparer;
import com.clougence.drivers.testsupport.TestDsFactory;
import com.clougence.drivers.testsupport.TestPrepareMarker;
import com.clougence.utils.loader.AbstractResourceLoader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DefaultDriverLoaderFunctionalTest {

    private Path       tempDir;
    private HttpServer httpServer;

    @Before
    public void setUp() throws Exception {
        this.tempDir = Files.createTempDirectory("cg-drivers-functional-test");
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
    public void loadDriverXml_shouldMergeDuplicateResourcesAndSortVersions() {
        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"functional-driver\" version=\"1.0.0\">" +
                "<driverName>" + TestDsFactory.class.getName() + "</driverName>" +
                "<resource type=\"class\">" + TestPrepareMarker.class.getName() + "</resource>" +
                "</driver>" +
                "<driver driverFamily=\"functional-driver\" version=\"2.0.0\">" +
                "<driverName>" + TestDsFactory.class.getName() + "</driverName>" +
                "<resource type=\"resource\">driver-tests/sample-resource.txt</resource>" +
                "</driver>" +
            "</drivers>"));
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"functional-driver\" version=\"2.0.0\">" +
                "<resource type=\"class\">" + TestPrepareMarker.class.getName() + "</resource>" +
                "<resource type=\"resource\">driver-tests/sample-resource.txt</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:on

        DriverFamily family = loader.findDriver("functional-driver");
        assertNotNull(family);
        assertEquals("2.0.0", family.findVersion(null).getVersion());

        DriverVersion version = loader.findDriver("functional-driver", "2.0.0");
        assertNotNull(version);
        assertEquals(2, version.getResources().size());
    }

    @Test
    public void prepareResources_shouldPrepareBuiltinDriverVersionAndReportProgress() throws Exception {
        byte[] urlBytes = "fake-url-content".getBytes(StandardCharsets.UTF_8);
        String baseUrl = startHttpServer("ignored".getBytes(StandardCharsets.UTF_8), urlBytes);

        Path fileResource = Files.createFile(this.tempDir.resolve("existing-driver-file.txt"));
        Files.write(fileResource, Collections.singletonList("driver-file"), StandardCharsets.UTF_8);

        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"functional-driver\" version=\"1.0.0\">" +
                "<resource type=\"class\">" + TestPrepareMarker.class.getName() + "</resource>" +
                "<resource type=\"resource\">driver-tests/sample-resource.txt</resource>" +
                "<resource type=\"file\">" + fileResource.toUri() + "</resource>" +
                "<resource type=\"url\">" + baseUrl + "/downloads/asset.bin</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:on

        DriverVersion version = loader.findDriver("functional-driver", "1.0.0");
        assertNotNull(version);

        ProgressRecorder progress = new ProgressRecorder();
        loader.prepareDriverVersion(version, resource -> false, progress);

        Path preparedDir = this.tempDir.resolve("functional-driver").resolve("1.0.0");
        assertTrue(Files.exists(preparedDir.resolve("asset.bin")));
        assertFalse(Files.exists(preparedDir.resolve(fileResource.getFileName())));
        assertEquals(2, version.getFiles().size());
        assertTrue(version.getFiles().stream().anyMatch(file -> "asset.bin".equals(file.getRelativePath())));
        assertTrue(version.getFiles()
            .stream()
            .anyMatch(file -> fileResource.getFileName().toString().equals(file.getRelativePath()) && fileResource.toFile().getAbsolutePath().equals(file.getAbsolutePath())));
        assertEquals(4, progress.started.size());
        assertEquals(4, progress.completed.size());
        assertTrue(progress.errors.isEmpty());
        assertTrue(progress.progressEvents.size() >= 1);
    }

    @Test
    public void prepareResources_defaultMethod_shouldNotSkipDriverVersion() throws Exception {
        byte[] urlBytes = "default-url".getBytes(StandardCharsets.UTF_8);
        String baseUrl = startHttpServer("ignored".getBytes(StandardCharsets.UTF_8), urlBytes);

        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"default-prepare-driver\" version=\"1.0\">" +
                "<resource type=\"url\">" + baseUrl + "/downloads/asset.bin</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:on

        DriverVersion version = loader.findDriver("default-prepare-driver", "1.0");
        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {
        });

        assertTrue(Files.exists(this.tempDir.resolve("default-prepare-driver").resolve("1.0").resolve("asset.bin")));
    }

    @Test
    public void prepareDriverVersion_shouldHonorSkipPredicate() throws Exception {
        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"skip-driver\" version=\"1.0\">" +
                "<resource type=\"file\">" + this.tempDir.resolve("missing-file.txt").toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:of

        DriverVersion version = loader.findDriver("skip-driver", "1.0");
        ProgressRecorder progress = new ProgressRecorder();
        loader.prepareDriverVersion(version, resource -> true, progress);

        assertTrue(progress.started.isEmpty());
        assertTrue(progress.completed.isEmpty());
        assertTrue(progress.errors.isEmpty());
    }

    @Test
    public void refreshResources_shouldRefreshPreparedStateWithoutPreparing() throws Exception {
        Path targetFile = this.tempDir.resolve("refresh-file.txt");

        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"refresh-driver\" version=\"1.0\">" +
                "<resource type=\"file\">" + targetFile.toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:on

        DriverVersion version = loader.findDriver("refresh-driver", "1.0");
        assertNotNull(version);
        assertFalse(version.isPrepared());
        assertFalse(version.getResources().get(0).isPrepared());
        assertNotNull(version.getResources().get(0).getFileDefList());
        assertEquals(1, version.getResources().get(0).getFileDefList().size());
        assertFalse(version.getResources().get(0).getFileDefList().get(0).isPrepared());

        Files.write(targetFile, "ok".getBytes(StandardCharsets.UTF_8));

        loader.refreshDriverVersion(version);

        assertTrue(version.isPrepared());
        assertTrue(version.getResources().get(0).isPrepared());
        assertTrue(version.getResources().get(0).getFileDefList().get(0).isPrepared());
        assertEquals(1, version.getFiles().size());
        assertEquals(targetFile.toFile().getAbsolutePath(), version.getFiles().get(0).getAbsolutePath());
        assertEquals(targetFile.getFileName().toString(), version.getFiles().get(0).getRelativePath());
    }

    @Test
    public void fileResourcePreparePre_shouldPopulateResolvedSourcePaths() throws Exception {
        Path absoluteFile = Files.createFile(this.tempDir.resolve("absolute-driver.jar"));

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate("files('" + absoluteFile.toUri() + "','drivers/local-driver.jar')");

        FileResourcePreparer preparer = new FileResourcePreparer(this.tempDir.toFile(), new Properties());
        preparer.analysis(null, resource, null, null);

        assertNotNull(resource.getFileDefList());
        assertEquals(2, resource.getFileDefList().size());
        assertEquals("absolute-driver.jar", resource.getFileDefList().get(0).getRelativePath());
        assertEquals(absoluteFile.toFile().getAbsolutePath(), resource.getFileDefList().get(0).getAbsolutePath());
        assertEquals("drivers/local-driver.jar", resource.getFileDefList().get(1).getRelativePath());
        assertEquals(new java.io.File("drivers/local-driver.jar").getAbsolutePath(), resource.getFileDefList().get(1).getAbsolutePath());
    }

    @Test
    public void fileResourcePreparePre_shouldUsePathRelativeToDriverVersionDir() throws Exception {
        Path versionFile = this.tempDir.resolve("family-a").resolve("1.0").resolve("nested").resolve("driver.jar");
        Files.createDirectories(versionFile.getParent());
        Files.createFile(versionFile);

        VerDef version = new VerDef();
        version.setFamilyName("family-a");
        version.setVersion("1.0");
        version.setLocalDir(this.tempDir.toFile());

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate(versionFile.toString());

        FileResourcePreparer preparer = new FileResourcePreparer(this.tempDir.toFile(), new Properties());
        preparer.analysis(version, resource, null, null);

        assertNotNull(resource.getFileDefList());
        assertEquals(1, resource.getFileDefList().size());
        assertEquals("nested/driver.jar", resource.getFileDefList().get(0).getRelativePath());
        assertEquals(versionFile.toFile().getAbsolutePath(), resource.getFileDefList().get(0).getAbsolutePath());
    }

    @Test
    public void fileResourceResolve_shouldReportProgressAndStopAtFirstMissingFile() throws Exception {
        Path existingFile = Files.createFile(this.tempDir.resolve("existing-driver.jar"));
        Path missingFile = this.tempDir.resolve("missing-driver.jar");

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate("manual");

        FileDef existingFileDef = new FileDef();
        existingFileDef.setRelativePath("existing-driver.jar");
        existingFileDef.setAbsolutePath(existingFile.toFile().getAbsolutePath());

        FileDef missingFileDef = new FileDef();
        missingFileDef.setRelativePath("missing-driver.jar");
        missingFileDef.setAbsolutePath(missingFile.toFile().getAbsolutePath());

        FileDef skippedFileDef = new FileDef();
        skippedFileDef.setRelativePath("skipped-driver.jar");
        skippedFileDef.setAbsolutePath(this.tempDir.resolve("skipped-driver.jar").toFile().getAbsolutePath());

        resource.setFileDefList(Arrays.asList(existingFileDef, missingFileDef, skippedFileDef));

        ResolveProgressRecorder progress = new ResolveProgressRecorder();
        FileResourcePreparer preparer = new FileResourcePreparer(this.tempDir.toFile(), new Properties());

        preparer.resolve(null, resource, null, progress);

        assertTrue(progress.started.isEmpty());
        assertTrue(progress.completed.isEmpty());
        assertEquals(Arrays.asList("existing-driver.jar:1/3", "missing-driver.jar:2/3"), progress.progressEvents);
        assertEquals(Collections.singletonList("manual:IOException"), progress.errors);
        assertTrue(existingFileDef.isPrepared());
        assertFalse(missingFileDef.isPrepared());
        assertFalse(skippedFileDef.isPrepared());
    }

    @Test
    public void fileResourceResolve_shouldCompleteLifecycleWhenAllFilesExist() throws Exception {
        Path firstFile = Files.createFile(this.tempDir.resolve("first-driver.jar"));
        Path secondFile = Files.createFile(this.tempDir.resolve("second-driver.jar"));

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate("manual-success");

        FileDef firstFileDef = new FileDef();
        firstFileDef.setRelativePath("first-driver.jar");
        firstFileDef.setAbsolutePath(firstFile.toFile().getAbsolutePath());

        FileDef secondFileDef = new FileDef();
        secondFileDef.setRelativePath("second-driver.jar");
        secondFileDef.setAbsolutePath(secondFile.toFile().getAbsolutePath());

        resource.setFileDefList(Arrays.asList(firstFileDef, secondFileDef));

        ResolveProgressRecorder progress = new ResolveProgressRecorder();
        FileResourcePreparer preparer = new FileResourcePreparer(this.tempDir.toFile(), new Properties());

        preparer.resolve(null, resource, null, progress);

        assertTrue(progress.started.isEmpty());
        assertTrue(progress.completed.isEmpty());
        assertEquals(Arrays.asList("first-driver.jar:1/2", "second-driver.jar:2/2"), progress.progressEvents);
        assertTrue(progress.errors.isEmpty());
        assertTrue(firstFileDef.isPrepared());
        assertTrue(secondFileDef.isPrepared());
    }

    @Test
    public void refreshResources_shouldExposeFilePathRelativeToDriverVersionDir() throws Exception {
        Path versionFile = this.tempDir.resolve("family-b").resolve("2.0").resolve("nested").resolve("driver.jar");
        Files.createDirectories(versionFile.getParent());
        Files.write(versionFile, "ok".getBytes(StandardCharsets.UTF_8));

        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"family-b\" version=\"2.0\">" + "<resource type=\"file\">" + versionFile.toAbsolutePath()
                                       + "</resource>" + "</driver>" + "</drivers>"));

        DriverVersion version = loader.findDriver("family-b", "2.0");
        assertNotNull(version);

        loader.refreshDriverVersion(version);

        assertTrue(version.isPrepared());
        assertEquals(1, version.getFiles().size());
        assertEquals("nested/driver.jar", version.getFiles().get(0).getRelativePath());
        assertEquals(versionFile.toFile().getAbsolutePath(), version.getFiles().get(0).getAbsolutePath());
    }

    @Test
    public void refreshResources_shouldRecoverPreparedFilesFromFilesIdxAfterRestart() throws Exception {
        DefaultDriverLoader prepareLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        prepareLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        prepareLoader.loadDriverXml(xmlStream(
            "<drivers>"
                + "<driver driverFamily=\"indexed-driver\" version=\"1.0\">"
                + "<resource type=\"downloaded\">asset.bin</resource>"
                + "</driver>"
                + "</drivers>"));

        DriverVersion preparedVersion = prepareLoader.findDriver("indexed-driver", "1.0");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {
        });

        Path versionDir = this.tempDir.resolve("indexed-driver").resolve("1.0");
        Path indexFile = versionDir.resolve("files.idx");
        assertTrue(Files.exists(versionDir.resolve("asset.bin")));
        assertTrue(Files.exists(indexFile));
        assertEquals(1, preparedVersion.getFiles().size());

        DefaultDriverLoader refreshLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        refreshLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        refreshLoader.loadDriverXml(xmlStream(
            "<drivers>"
                + "<driver driverFamily=\"indexed-driver\" version=\"1.0\">"
                + "<resource type=\"downloaded\">asset.bin</resource>"
                + "</driver>"
                + "</drivers>"));

        DriverVersion refreshedVersion = refreshLoader.findDriver("indexed-driver", "1.0");
        assertNotNull(refreshedVersion);
        assertTrue(refreshedVersion.getFiles().isEmpty());

        refreshLoader.refreshDriverVersion(refreshedVersion);

        assertTrue(refreshedVersion.isPrepared());
        assertTrue(refreshedVersion.getResources().get(0).isPrepared());
        assertEquals(1, refreshedVersion.getFiles().size());
        assertEquals("asset.bin", refreshedVersion.getFiles().get(0).getRelativePath());
        assertEquals(versionDir.resolve("asset.bin").toFile().getAbsolutePath(), refreshedVersion.getFiles().get(0).getAbsolutePath());
        assertTrue(refreshedVersion.getFiles().get(0).isPrepared());
    }

    @Test
    public void refreshResources_shouldSkipAnalysisWhenFilesIdxAlreadyExists() throws Exception {
        IndexedDownloadPreparer.resetCounters();

        DefaultDriverLoader prepareLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        prepareLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        prepareLoader.loadDriverXml(xmlStream(
            "<drivers>"
                + "<driver driverFamily=\"indexed-driver\" version=\"1.1\">"
                + "<resource type=\"downloaded\">asset.bin</resource>"
                + "</driver>"
                + "</drivers>"));

        DriverVersion preparedVersion = prepareLoader.findDriver("indexed-driver", "1.1");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {
        });
        assertEquals(1, IndexedDownloadPreparer.analysisCount);

        IndexedDownloadPreparer.resetCounters();

        DefaultDriverLoader refreshLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        refreshLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        refreshLoader.loadDriverXml(xmlStream(
            "<drivers>"
                + "<driver driverFamily=\"indexed-driver\" version=\"1.1\">"
                + "<resource type=\"downloaded\">asset.bin</resource>"
                + "</driver>"
                + "</drivers>"));

        DriverVersion refreshedVersion = refreshLoader.findDriver("indexed-driver", "1.1");
        assertNotNull(refreshedVersion);

        refreshLoader.refreshDriverVersion(refreshedVersion);

        assertEquals(0, IndexedDownloadPreparer.analysisCount);
        assertTrue(refreshedVersion.isPrepared());
        assertEquals(1, refreshedVersion.getFiles().size());
    }

    @Test
    public void refreshResources_shouldRecoverFilesForMatchingResDefOnly() throws Exception {
        DefaultDriverLoader prepareLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        prepareLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        prepareLoader.loadDriverXml(xmlStream(
            "<drivers>"
                + "<driver driverFamily=\"indexed-driver\" version=\"2.0\">"
                + "<resource type=\"downloaded\">asset-a.bin</resource>"
                + "<resource type=\"downloaded\">asset-b.bin</resource>"
                + "</driver>"
                + "</drivers>"));

        DriverVersion preparedVersion = prepareLoader.findDriver("indexed-driver", "2.0");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {
        });

        Path versionDir = this.tempDir.resolve("indexed-driver").resolve("2.0");
        Path indexFile = versionDir.resolve("files.idx");
        assertTrue(Files.exists(indexFile));

        List<String> indexLines = Files.readAllLines(indexFile, StandardCharsets.UTF_8);
        ResDef firstPreparedResource = preparedVersion.getResources().get(0);
        ResDef secondPreparedResource = preparedVersion.getResources().get(1);
        assertTrue(indexLines.contains("relative " + firstPreparedResource.getFilesIndexId() + " asset-a.bin"));
        assertTrue(indexLines.contains("relative " + secondPreparedResource.getFilesIndexId() + " asset-b.bin"));
        assertNotEquals(firstPreparedResource.getFilesIndexId(), secondPreparedResource.getFilesIndexId());

        DefaultDriverLoader refreshLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        refreshLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        refreshLoader.loadDriverXml(xmlStream(
            "<drivers>"
                + "<driver driverFamily=\"indexed-driver\" version=\"2.0\">"
                + "<resource type=\"downloaded\">asset-a.bin</resource>"
                + "<resource type=\"downloaded\">asset-b.bin</resource>"
                + "</driver>"
                + "</drivers>"));

        DriverVersion refreshedVersion = refreshLoader.findDriver("indexed-driver", "2.0");
        assertNotNull(refreshedVersion);

        refreshLoader.refreshDriverVersion(refreshedVersion);

        assertTrue(refreshedVersion.isPrepared());
        assertEquals(2, refreshedVersion.getFiles().size());
        assertEquals(1, refreshedVersion.getResources().get(0).getFileDefList().size());
        assertEquals(1, refreshedVersion.getResources().get(1).getFileDefList().size());
        assertEquals("asset-a.bin", refreshedVersion.getResources().get(0).getFileDefList().get(0).getRelativePath());
        assertEquals("asset-b.bin", refreshedVersion.getResources().get(1).getFileDefList().get(0).getRelativePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fileResourcePreparePre_shouldRejectParentRelativePath() throws Exception {
        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate("../unsafe-driver.jar");

        FileResourcePreparer preparer = new FileResourcePreparer(this.tempDir.toFile(), new Properties());
        preparer.analysis(null, resource, null, null);
    }

    @Test
    public void classResourcePreparePre_shouldMarkPreparedTrue() throws Exception {
        ResDef resource = new ResDef();
        resource.setResourceType("class");
        resource.setCoordinate(TestPrepareMarker.class.getName());

        ClassResourcePreparer preparer = new ClassResourcePreparer(this.tempDir.toFile(), new Properties());
        preparer.analysis(null, resource, this.getClass().getClassLoader(), null);

        assertTrue(resource.isPrepared());
        assertNotNull(resource.getFileDefList());
        assertEquals(1, resource.getFileDefList().size());
        assertTrue(resource.getFileDefList().get(0).isPrepared());
    }

    @Test
    public void refreshResources_shouldKeepClassResourcePrepared() throws Exception {
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"class-refresh-driver\" version=\"1.0\">" + "<resource type=\"class\">"
                                       + TestPrepareMarker.class.getName() + "</resource>" + "</driver>" + "</drivers>"));

        DriverVersion version = loader.findDriver("class-refresh-driver", "1.0");
        assertNotNull(version);

        loader.refreshDriverVersion(version);

        assertTrue(version.isPrepared());
        assertTrue(version.getResources().get(0).isPrepared());
        assertTrue(version.getResources().get(0).getFileDefList().get(0).isPrepared());
    }

    @Test
    public void getFiles_shouldAggregateFileDefsAndPreservePreparedState() throws Exception {
        Path preparedFile = Files.createFile(this.tempDir.resolve("prepared-driver.jar"));
        Path versionFile = this.tempDir.resolve("aggregated-driver").resolve("1.0").resolve("nested").resolve("driver.jar");
        Files.createDirectories(versionFile.getParent());
        Files.write(versionFile, "ok".getBytes(StandardCharsets.UTF_8));

        VerDef version = new VerDef();
        version.setFamilyName("aggregated-driver");
        version.setVersion("1.0");
        version.setLocalDir(this.tempDir.toFile());

        FileDef preparedFileDef = new FileDef();
        preparedFileDef.setAbsolutePath(preparedFile.toFile().getAbsolutePath());
        preparedFileDef.setRelativePath("external/prepared-driver.jar");
        preparedFileDef.setPrepared(true);

        FileDef versionFileDef = new FileDef();
        versionFileDef.setAbsolutePath(versionFile.toFile().getAbsolutePath());
        versionFileDef.setRelativePath("nested/driver.jar");
        versionFileDef.setPrepared(true);

        FileDef missingFileDef = new FileDef();
        missingFileDef.setRelativePath("missing/not-ready.jar");
        missingFileDef.setPrepared(false);

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate("manual");
        resource.setFileDefList(Arrays.asList(preparedFileDef, versionFileDef, missingFileDef));
        version.addResource(resource);

        assertEquals(preparedFile.toFile().getAbsolutePath(), preparedFileDef.getAbsolutePath());
        assertEquals(versionFile.toFile().getAbsolutePath(), versionFileDef.getAbsolutePath());
        assertEquals(this.tempDir.resolve("missing/not-ready.jar").toFile().getAbsolutePath(), missingFileDef.getAbsolutePath());

        List<DriverFile> files = version.getFiles();
        assertEquals(3, files.size());
        assertTrue(files.stream()
            .anyMatch(file -> "external/prepared-driver.jar".equals(file.getRelativePath()) && preparedFile.toFile().getAbsolutePath().equals(file.getAbsolutePath())
                              && file.isPrepared()));
        assertTrue(files.stream()
            .anyMatch(file -> "nested/driver.jar".equals(file.getRelativePath()) && versionFile.toFile().getAbsolutePath().equals(file.getAbsolutePath()) && file.isPrepared()));
        assertTrue(files.stream()
            .anyMatch(file -> "missing/not-ready.jar".equals(file.getRelativePath())
                              && this.tempDir.resolve("missing/not-ready.jar").toFile().getAbsolutePath().equals(file.getAbsolutePath()) && !file.isPrepared()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addResource_shouldRejectParentRelativePathInFileDef() {
        VerDef version = new VerDef();
        version.setLocalDir(this.tempDir.toFile());

        FileDef fileDef = new FileDef();
        fileDef.setRelativePath("../unsafe-driver.jar");

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate("manual");
        resource.setFileDefList(Collections.singletonList(fileDef));

        version.addResource(resource);
    }

    @Test
    public void binding_shouldExposeResourcesAndDriverDsFactory() throws Exception {
        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"binding-driver\" version=\"1.0\">" +
                "<driverName>" + TestDsFactory.class.getName() + "</driverName>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:of

        DriverVersion version = loader.findDriver("binding-driver", "1.0");
        assertNotNull(version);
        assertEquals(TestDsFactory.class.getName(), version.getDsFactory());

        DriverBinding binding = loader.createBinding(this.getClass().getClassLoader(), "binding-driver", "1.0");
        binding.bind(this.getClass().getClassLoader(), "driver-tests", TestPrepareMarker.class.getName());
        binding.bind(new InMemoryResourceLoader("bound/extra.txt", "bound-content"), "bound");

        assertEquals(TestPrepareMarker.class.getName(), binding.asClassLoader().loadClass(TestPrepareMarker.class.getName()).getName());
        assertEquals(TestDsFactory.class.getName(), binding.asClassLoader().loadClass(version.getDsFactory()).getName());
        assertEquals("sample-resource", readToString(binding.asClassLoader().getResourceAsStream("driver-tests/sample-resource.txt")));
        assertEquals("bound-content", readToString(binding.asClassLoader().getResourceAsStream("bound/extra.txt")));
    }

    @Test
    public void binding_shouldLoadPreparedJarFiles() throws Exception {
        Path jarFile = createTestJar("sample.driver.GeneratedDriverMarker", "package sample.driver; public class GeneratedDriverMarker { public String ping() { return \"ok\"; } }");

        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"jar-binding-driver\" version=\"1.0\">" +
                "<driverName>" + TestDsFactory.class.getName() + "</driverName>" +
                "<resource type=\"file\">" + jarFile.toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));

        DriverVersion version = loader.findDriver("jar-binding-driver", "1.0");
        assertNotNull(version);
        ((VerDef) version).setDsFactoryDef(new DsFactoryDef(TestDsFactory.class.getName(), this.getClass().getClassLoader()));

        loader.refreshDriverVersion(version);
        DriverBinding binding = loader.createBinding(this.getClass().getClassLoader(), "jar-binding-driver", "1.0");

        Class<?> generatedClass = binding.asClassLoader().loadClass("sample.driver.GeneratedDriverMarker");
        assertNotNull(generatedClass);
        assertEquals("sample.driver.GeneratedDriverMarker", generatedClass.getName());
    }

    @Test
    public void binding_shouldRejectPreparedNonArchiveDriverFiles() throws Exception {
        Path textFile = Files.createFile(this.tempDir.resolve("prepared-driver.txt"));
        Files.write(textFile, Collections.singletonList("plain-text-driver"), StandardCharsets.UTF_8);

        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"invalid-binding-driver\" version=\"1.0\">" +
                "<driverName>" + TestDsFactory.class.getName() + "</driverName>" +
                "<resource type=\"file\">" + textFile.toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));

        DriverVersion version = loader.findDriver("invalid-binding-driver", "1.0");
        assertNotNull(version);
        ((VerDef) version).setDsFactoryDef(new DsFactoryDef(TestDsFactory.class.getName(), this.getClass().getClassLoader()));

        loader.refreshDriverVersion(version);

        try {
            loader.createBinding(this.getClass().getClassLoader(), "invalid-binding-driver", "1.0");
            fail("createBinding should reject prepared non-archive driver files");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("unsupported archive file"));
            assertTrue(e.getMessage().contains(textFile.toFile().getAbsolutePath()));
        }
    }

    @Test
    public void binding_shouldReportExpiredWhenPreparedJarChanges() throws Exception {
        Path jarFile = createTestJar("sample.driver.GeneratedDriverMarker", "package sample.driver; public class GeneratedDriverMarker { public String ping() { return \"ok\"; } }");

        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"jar-expire-driver\" version=\"1.0\">" +
                "<resource type=\"file\">" + jarFile.toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));

        DriverVersion version = loader.findDriver("jar-expire-driver", "1.0");
        assertNotNull(version);

        loader.refreshDriverVersion(version);
        DriverBinding binding = loader.createBinding(this.getClass().getClassLoader(), "jar-expire-driver", "1.0");
        assertFalse(binding.isExpired());

        createTestJar("sample.driver.GeneratedDriverMarker", "package sample.driver; public class GeneratedDriverMarker { public String ping() { return \"updated-content-for-expire-check\"; } }");

        assertTrue(binding.isExpired());
    }

    @Test
    public void deleteLocalResources_shouldBeOwnedByDriverVersion() throws Exception {
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"delete-driver\" version=\"1.0\">" +
                "</driver>" +
            "</drivers>"));

        DriverVersion version = loader.findDriver("delete-driver", "1.0");
        assertNotNull(version);

        Path versionDir = version.getAbsoluteDir().toPath();
        Files.createDirectories(versionDir);
        Files.write(versionDir.resolve("sample.txt"), Collections.singletonList("x"), StandardCharsets.UTF_8);

        long beforeDelete = version.getTimestamp();
        assertTrue(Files.exists(versionDir.resolve("sample.txt")));

        version.deleteFiles();

        assertFalse(Files.exists(versionDir));
        assertNotEquals(beforeDelete, version.getTimestamp());
    }

    @Test
    public void loadDsFactory_shouldRegisterSpiClassNamesWithoutInstantiatingProviders()throws Exception {
        ExposedDriverLoader loader = new ExposedDriverLoader(this.tempDir.toFile(), new Properties());
        String missingFactoryName = "com.example.clickhouse.MissingFactory";

        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"spi-driver\" version=\"1.0\">" +
                "<driverName>" + missingFactoryName + "</driverName>" +
                "</driver>" +
            "</drivers>"));

        loader.loadDsFactory(new ServiceDescriptorClassLoader(this.getClass().getClassLoader(), missingFactoryName + "\n"));

        assertNotNull(loader.exposedFindDsFactoryDef(missingFactoryName));
        assertEquals(missingFactoryName, loader.findDriver("spi-driver", "1.0").getDsFactory());
    }

    @Test
    public void loadDsFactory_shouldIgnoreUnsupportedResourceTypeUntilPreparerRegistered() throws Exception {
        ExposedDriverLoader loader = new ExposedDriverLoader(this.tempDir.toFile(), new Properties());
        String missingFactoryName = "com.example.clickhouse.MissingFactory";

        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"spi-maven-driver\" version=\"1.0\">" +
                "<driverName>" + missingFactoryName + "</driverName>" +
                "<resource type=\"maven\">com.example:demo-artifact:1.0.0</resource>" +
                "</driver>" +
            "</drivers>"));

        loader.loadDsFactory(new ServiceDescriptorClassLoader(this.getClass().getClassLoader(), missingFactoryName + "\n"));

        DriverVersion version = loader.findDriver("spi-maven-driver", "1.0");
        assertNotNull(version);
        assertFalse(version.isPrepared());
        assertEquals(1, version.getResources().size());
        assertFalse(version.getResources().get(0).isPrepared());
        assertNotNull(loader.exposedFindDsFactoryDef(missingFactoryName));
    }

    private InputStream xmlStream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }

    private Properties props(String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        return properties;
    }

    private String startHttpServer(byte[] mavenBytes, byte[] urlBytes) throws Exception {
        this.httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        this.httpServer.createContext("/repo/com/example/demo-artifact/1.0.0/demo-artifact-1.0.0.jar", new ByteArrayHandler(mavenBytes));
        this.httpServer.createContext("/downloads/asset.bin", new ByteArrayHandler(urlBytes));
        this.httpServer.start();
        return "http://127.0.0.1:" + this.httpServer.getAddress().getPort();
    }

    private String readToString(InputStream inputStream) throws IOException {
        assertNotNull(inputStream);
        try (InputStream source = inputStream; ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[512];
            int len;
            while ((len = source.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, len);
            }
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        }
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

    private Path createTestJar(String className, String sourceCode) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull("system java compiler is required", compiler);

        Path sourceDir = Files.createDirectories(this.tempDir.resolve("generated-src"));
        Path outputDir = Files.createDirectories(this.tempDir.resolve("generated-classes"));
        Path sourceFile = sourceDir.resolve(className.replace('.', '/') + ".java");
        Files.createDirectories(sourceFile.getParent());
        Files.write(sourceFile, sourceCode.getBytes(StandardCharsets.UTF_8));

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(outputDir.toFile()));
            boolean success = compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjects(sourceFile.toFile())).call();
            assertTrue("compile generated source failed", success);
        }

        Path classFile = outputDir.resolve(className.replace('.', '/') + ".class");
        assertTrue(Files.exists(classFile));

        Path jarFile = this.tempDir.resolve("generated-driver.jar");
        try (JarOutputStream jarOutput = new JarOutputStream(Files.newOutputStream(jarFile))) {
            String entryName = className.replace('.', '/') + ".class";
            jarOutput.putNextEntry(new JarEntry(entryName));
            jarOutput.write(Files.readAllBytes(classFile));
            jarOutput.closeEntry();
        }

        return jarFile;
    }

    private static final class ByteArrayHandler implements HttpHandler {

        private final byte[] content;

        private ByteArrayHandler(byte[] content) {
            this.content = content;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, this.content.length);
            exchange.getResponseBody().write(this.content);
            exchange.close();
        }
    }

    private static final class ProgressRecorder implements DriverPrepareProgress {

        private final List<String> started        = new ArrayList<>();
        private final List<String> completed      = new ArrayList<>();
        private final List<String> errors         = new ArrayList<>();
        private final List<String> progressEvents = new ArrayList<>();

        @Override
        public void onStart(DriverVersion driverVersion, ResDef resDef, int index, int totalCount) {
            this.started.add(resDef.getResourceType() + "@" + index + "/" + totalCount);
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

    private static final class ResolveProgressRecorder implements DriverPrepareProgress {

        private final List<String> started        = new ArrayList<>();
        private final List<String> completed      = new ArrayList<>();
        private final List<String> progressEvents = new ArrayList<>();
        private final List<String> errors         = new ArrayList<>();

        @Override
        public void onStart(DriverVersion driverVersion, ResDef resDef, int index, int totalCount) {
            this.started.add(resDef.getResourceType() + "@" + index + "/" + totalCount);
        }

        @Override
        public void onProgress(DriverVersion driverVersion, ResDef resDef, String fileName, long current, long total) {
            this.progressEvents.add(fileName + ":" + current + "/" + total);
        }

        @Override
        public void onComplete(DriverVersion driverVersion, ResDef resource, int index, int totalCount) {
            this.completed.add(resource.getCoordinate() + "@" + index + "/" + totalCount);
        }

        @Override
        public void onError(DriverVersion driverVersion, ResDef resource, Exception exception) {
            this.errors.add(resource.getCoordinate() + ":" + exception.getClass().getSimpleName());
        }
    }

    private static final class InMemoryResourceLoader extends AbstractResourceLoader {

        private final String resourceName;
        private final byte[] content;

        private InMemoryResourceLoader(String resourceName, String content) {
            this.resourceName = resourceName;
            this.content = content.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public <T> List<T> scanResources(MatchType matchType, Scanner<T> scanner, String[] scanPaths) {
            return Collections.emptyList();
        }

        @Override
        public <T> T scanOneResource(MatchType matchType, Scanner<T> scanner, String[] scanPaths) {
            return null;
        }

        @Override
        public URL getResource(String resource) {
            return null;
        }

        @Override
        public InputStream getResourceAsStream(String resource) {
            if (!this.resourceName.equals(resource)) {
                return null;
            }
            return new ByteArrayInputStream(this.content);
        }

        @Override
        public List<URL> getResources(String resource) {
            return Collections.emptyList();
        }

        @Override
        public List<InputStream> getResourcesAsStream(String resource) {
            InputStream inputStream = getResourceAsStream(resource);
            return inputStream == null ? Collections.emptyList() : Collections.singletonList(inputStream);
        }

        @Override
        public boolean exist(String resource) {
            return this.resourceName.equals(resource);
        }

        @Override
        public java.util.jar.Manifest getManifest(String resource) {
            return null;
        }
    }

    private static final class IndexedDownloadPreparer extends AbstractResourcePreparer {

        private static int analysisCount;

        private static void resetCounters() {
            analysisCount = 0;
        }

        private IndexedDownloadPreparer(java.io.File localDir, Properties config) {
            super(localDir, config);
        }

        @Override
        public void analysis(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception {
            analysisCount++;
            resDef.setFileDefList(Collections.emptyList());
            updateFilesIndex(driverVersion, resDef);
        }

        @Override
        public void resolve(DriverVersion driverVersion, ResDef resDef, ClassLoader classLoader, DriverPrepareProgress progress) throws Exception {
            Path versionDir = driverVersion.getAbsoluteDir().toPath();
            Files.createDirectories(versionDir);

            Path assetFile = versionDir.resolve(resDef.getCoordinate());
            Files.write(assetFile, Collections.singletonList("indexed-driver"), StandardCharsets.UTF_8);

            FileDef fileDef = new FileDef();
            fileDef.setRelativePath(resDef.getCoordinate());
            fileDef.setAbsolutePath(assetFile.toFile().getAbsolutePath());
            fileDef.setPrepared(true);
            resDef.setFileDefList(Collections.singletonList(fileDef));
            updateFilesIndex(driverVersion, resDef);
            resDef.setPrepared(true);
        }
    }

    private static final class ExposedDriverLoader extends DefaultDriverLoader {

        private ExposedDriverLoader(java.io.File driverHome, Properties properties) {
            super(driverHome, properties);
        }

        private DsFactoryDef exposedFindDsFactoryDef(String dsFactoryName) {
            return super.findDsFactoryDef(dsFactoryName);
        }
    }

    private static final class ServiceDescriptorClassLoader extends ClassLoader {

        private static final String RESOURCE_NAME = "META-INF/services/com.clougence.drivers.DsFactory";
        private final byte[] serviceContent;

        private ServiceDescriptorClassLoader(ClassLoader parent, String serviceContent) {
            super(parent);
            this.serviceContent = serviceContent.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public java.util.Enumeration<URL> getResources(String name) {
            return Collections.emptyEnumeration();
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            if (!RESOURCE_NAME.equals(name)) {
                return super.getResourceAsStream(name);
            }
            return new ByteArrayInputStream(this.serviceContent);
        }
    }
}