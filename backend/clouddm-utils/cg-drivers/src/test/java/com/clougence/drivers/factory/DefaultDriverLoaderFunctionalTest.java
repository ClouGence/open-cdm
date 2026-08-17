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

import java.io.*;
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

public class DefaultDriverLoaderFunctionalTest {

    private Path tempDir;

    @Before
    public void setUp() throws Exception {
        this.tempDir = Files.createTempDirectory("cg-drivers-functional-test");
    }

    @After
    public void tearDown() throws Exception {
        if (this.tempDir != null) {
            deleteRecursively(this.tempDir);
        }
    }

    @Test
    public void loadDriverXml_shouldMergeDuplicateResourcesAndSortVersions() {
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            """
                <drivers>
                    <driver driverFamily="functional-driver" version="1.0.0">
                        <driverName>%s</driverName>
                        <resource type="class">%s</resource>
                    </driver>
                    <driver driverFamily="functional-driver" version="2.0.0" default="true">
                        <driverName>%s</driverName>
                        <resource type="resource">driver-tests/sample-resource.txt</resource>
                    </driver>
                </drivers>
                """.formatted(TestDsFactory.class.getName(), TestPrepareMarker.class.getName(), TestDsFactory.class.getName())));
        loader.loadDriverXml(xmlStream(
            """
                <drivers>
                    <driver driverFamily="functional-driver" version="2.0.0">
                        <resource type="class">%s</resource>
                        <resource type="resource">driver-tests/sample-resource.txt</resource>
                    </driver>
                </drivers>
                """.formatted(TestPrepareMarker.class.getName())));

        DriverFamily family = loader.findDriver("functional-driver");
        assertNotNull(family);
        assertEquals("2.0.0", family.findVersion(null).getVersion());
        assertEquals("2.0.0", family.getDefaultVersion());
        assertTrue(family.findVersion("2.0.0").isDefault());
        assertFalse(family.findVersion("1.0.0").isDefault());

        DriverVersion version = loader.findDriver("functional-driver", "2.0.0");
        assertNotNull(version);
        assertEquals(2, version.getResources().size());
    }

    @Test
    public void prepareResources_shouldPrepareFileResourcesAndReportProgress() throws Exception {
        Path absoluteFile = Files.createFile(this.tempDir.resolve("existing-driver-file.jar"));
        Path versionLocalFile = this.tempDir.resolve("functional-driver").resolve("1.0.0").resolve("nested").resolve("local-driver.jar");
        Files.createDirectories(versionLocalFile.getParent());
        Files.createFile(versionLocalFile);

        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"functional-driver\" version=\"1.0.0\">" +
                "<resource type=\"file\">" + absoluteFile.toUri() + "</resource>" +
                "<resource type=\"file\">nested/local-driver.jar</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:on

        DriverVersion version = loader.findDriver("functional-driver", "1.0.0");
        assertNotNull(version);

        ProgressRecorder progress = new ProgressRecorder();
        loader.prepareDriverVersion(version, resource -> false, progress);

        assertTrue(version.isPrepared());
        assertTrue(Files.exists(this.tempDir.resolve("functional-driver").resolve("1.0.0").resolve("files.idx")));
        assertEquals(2, version.getFiles().size());
        assertTrue(version.getFiles()
            .stream()
            .anyMatch(file -> "existing-driver-file.jar".equals(file.getRelativePath()) && absoluteFile.toFile().getAbsolutePath().equals(file.getAbsolutePath())
                              && file.isPrepared()));
        assertTrue(version.getFiles()
            .stream()
            .anyMatch(file -> "nested/local-driver.jar".equals(file.getRelativePath()) && versionLocalFile.toFile().getAbsolutePath().equals(file.getAbsolutePath())
                              && file.isPrepared()));
        // both prepare and resolve emit lifecycle events: 2 resources x (prepare onStart + resolve onStart).
        assertEquals(4, progress.started.size());
        assertEquals(4, progress.completed.size());
        assertTrue(progress.errors.isEmpty());
    }

    @Test
    public void prepareResources_defaultMethod_shouldNotSkipDriverVersion() throws Exception {
        Path driverFile = Files.createFile(this.tempDir.resolve("default-prepare-driver.jar"));

        // @formatter:off
        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"default-prepare-driver\" version=\"1.0\">" +
                "<resource type=\"file\">" + driverFile.toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));
        // @formatter:on

        DriverVersion version = loader.findDriver("default-prepare-driver", "1.0");
        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {});

        assertTrue(version.isPrepared());
        assertEquals(1, version.getFiles().size());
        assertTrue(version.getFiles().get(0).isPrepared());
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
        // loadDriverXml no longer analyzes resources; file defs only exist after the first prepare.
        assertNull(version.getResources().get(0).getFileDefList());

        loader.refreshDriverVersion(version);
        assertFalse(version.isPrepared());

        Files.write(targetFile, "ok".getBytes(StandardCharsets.UTF_8));
        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {});
        assertTrue(version.isPrepared());

        Files.delete(targetFile);
        loader.refreshDriverVersion(version);
        assertFalse(version.isPrepared());
        assertFalse(version.getResources().get(0).isPrepared());

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

        VerDef version = new VerDef();
        version.setFamilyName("file-analysis-driver");
        version.setVersion("1.0");
        version.setLocalDir(this.tempDir.toFile());

        ResDef resource = new ResDef();
        resource.setResourceType("file");
        resource.setCoordinate(absoluteFile.toUri() + ",drivers/local-driver.jar");

        FileResourcePreparer preparer = new FileResourcePreparer(this.tempDir.toFile(), new Properties());
        preparer.analysis(version, resource, null, null);

        assertNotNull(resource.getFileDefList());
        assertEquals(2, resource.getFileDefList().size());
        assertEquals("absolute-driver.jar", resource.getFileDefList().get(0).getRelativePath());
        assertEquals(absoluteFile.toFile().getAbsolutePath(), resource.getFileDefList().get(0).getAbsolutePath());
        assertEquals("drivers/local-driver.jar", resource.getFileDefList().get(1).getRelativePath());
        assertEquals(new File(version.getAbsoluteDir(), "drivers/local-driver.jar").getAbsolutePath(), resource.getFileDefList().get(1).getAbsolutePath());
    }

    @Test
    public void fileResourcePreparePre_shouldFlattenAbsoluteCoordinateToFileName() throws Exception {
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
        // absolute coordinates keep their original location and expose the plain file name.
        assertEquals("driver.jar", resource.getFileDefList().get(0).getRelativePath());
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

        assertEquals(Collections.singletonList("file@0/3"), progress.started);
        assertTrue(progress.completed.isEmpty());
        assertEquals(Arrays.asList("existing-driver.jar:1/3", "missing-driver.jar:2/3"), progress.progressEvents);
        assertEquals(Collections.singletonList("manual:IOException"), progress.errors);
        assertTrue(existingFileDef.isPrepared());
        assertFalse(missingFileDef.isPrepared());
        assertFalse(skippedFileDef.isPrepared());
        assertFalse(resource.isPrepared());
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

        assertEquals(Collections.singletonList("file@0/2"), progress.started);
        assertEquals(Collections.singletonList("manual-success@2/2"), progress.completed);
        assertEquals(Arrays.asList("first-driver.jar:1/2", "second-driver.jar:2/2"), progress.progressEvents);
        assertTrue(progress.errors.isEmpty());
        assertTrue(firstFileDef.isPrepared());
        assertTrue(secondFileDef.isPrepared());
        assertTrue(resource.isPrepared());
    }

    @Test
    public void refreshResources_shouldExposeFilePathRelativeToDriverVersionDir() throws Exception {
        Path versionFile = this.tempDir.resolve("family-b").resolve("2.0").resolve("nested").resolve("driver.jar");
        Files.createDirectories(versionFile.getParent());
        Files.write(versionFile, "ok".getBytes(StandardCharsets.UTF_8));

        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"family-b\" version=\"2.0\">" + "<resource type=\"file\">nested/driver.jar" + "</resource>"
                                       + "</driver>" + "</drivers>"));

        DriverVersion version = loader.findDriver("family-b", "2.0");
        assertNotNull(version);

        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {});
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
        prepareLoader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"indexed-driver\" version=\"1.0\">" + "<resource type=\"downloaded\">asset.bin</resource>"
                                              + "</driver>" + "</drivers>"));

        DriverVersion preparedVersion = prepareLoader.findDriver("indexed-driver", "1.0");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {});

        Path versionDir = this.tempDir.resolve("indexed-driver").resolve("1.0");
        Path indexFile = versionDir.resolve("files.idx");
        assertTrue(Files.exists(versionDir.resolve("asset.bin")));
        assertTrue(Files.exists(indexFile));
        assertEquals(1, preparedVersion.getFiles().size());

        DefaultDriverLoader refreshLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        refreshLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        refreshLoader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"indexed-driver\" version=\"1.0\">" + "<resource type=\"downloaded\">asset.bin</resource>"
                                              + "</driver>" + "</drivers>"));

        DriverVersion refreshedVersion = refreshLoader.findDriver("indexed-driver", "1.0");
        assertNotNull(refreshedVersion);
        // addResource restores files.idx eagerly, so files are already visible after loadDriverXml.
        assertEquals(1, refreshedVersion.getFiles().size());

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
        prepareLoader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"indexed-driver\" version=\"1.1\">" + "<resource type=\"downloaded\">asset.bin</resource>"
                                              + "</driver>" + "</drivers>"));

        DriverVersion preparedVersion = prepareLoader.findDriver("indexed-driver", "1.1");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {});
        assertEquals(1, IndexedDownloadPreparer.analysisCount);

        IndexedDownloadPreparer.resetCounters();

        DefaultDriverLoader refreshLoader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        refreshLoader.registerPreparer("downloaded", IndexedDownloadPreparer::new);
        refreshLoader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"indexed-driver\" version=\"1.1\">" + "<resource type=\"downloaded\">asset.bin</resource>"
                                              + "</driver>" + "</drivers>"));

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
        prepareLoader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"indexed-driver\" version=\"2.0\">" + "<resource type=\"downloaded\">asset-a.bin</resource>"
                                              + "<resource type=\"downloaded\">asset-b.bin</resource>" + "</driver>" + "</drivers>"));

        DriverVersion preparedVersion = prepareLoader.findDriver("indexed-driver", "2.0");
        assertNotNull(preparedVersion);

        prepareLoader.prepareDriverVersion(preparedVersion, resource -> false, new DriverPrepareProgress() {});

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
        refreshLoader.loadDriverXml(xmlStream("<drivers>" + "<driver driverFamily=\"indexed-driver\" version=\"2.0\">" + "<resource type=\"downloaded\">asset-a.bin</resource>"
                                              + "<resource type=\"downloaded\">asset-b.bin</resource>" + "</driver>" + "</drivers>"));

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
    public void classResourceAnalysis_shouldDeferPreparedStateToRefresh() throws Exception {
        VerDef version = new VerDef();
        version.setFamilyName("class-driver");
        version.setVersion("1.0");
        version.setLocalDir(this.tempDir.toFile());

        ResDef resource = new ResDef();
        resource.setResourceType("class");
        resource.setCoordinate(TestPrepareMarker.class.getName());

        ClassResourcePreparer preparer = new ClassResourcePreparer(this.tempDir.toFile(), new Properties());
        preparer.analysis(version, resource, this.getClass().getClassLoader(), null);

        // analysis only records metadata; the prepared state is decided by refresh against a classloader.
        assertFalse(resource.isPrepared());
        assertNotNull(resource.getFileDefList());
        assertTrue(resource.getFileDefList().isEmpty());

        preparer.refresh(version, resource, this.getClass().getClassLoader(), null);
        assertTrue(resource.isPrepared());

        preparer.refresh(version, resource, new java.net.URLClassLoader(new URL[0], null), null);
        assertFalse(resource.isPrepared());
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

        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {
        });
        DriverBinding binding = loader.createBinding(this.getClass().getClassLoader(), "jar-binding-driver", "1.0");

        Class<?> generatedClass = binding.asClassLoader().loadClass("sample.driver.GeneratedDriverMarker");
        assertNotNull(generatedClass);
        assertEquals("sample.driver.GeneratedDriverMarker", generatedClass.getName());
    }

    @Test
    public void binding_shouldLoadPreparedJarDependencyOutsideIncludePackages() throws Exception {
        Map<String, String> sources = new LinkedHashMap<>();
        sources.put("sample.driver.GeneratedDriverMarker",
            "package sample.driver; public class GeneratedDriverMarker { public String ping() { return sample.dep.Helper.value(); } }");
        sources.put("sample.dep.Helper", "package sample.dep; public class Helper { public static String value() { return \"dep-ok\"; } }");
        Path jarFile = createTestJar("generated-driver-with-dep.jar", sources);

        DefaultDriverLoader loader = new DefaultDriverLoader(this.tempDir.toFile(), new Properties());
        loader.loadDriverXml(xmlStream(
            "<drivers>" +
                "<driver driverFamily=\"jar-binding-dep-driver\" version=\"1.0\">" +
                "<driverName>" + TestDsFactory.class.getName() + "</driverName>" +
                "<resource type=\"file\">" + jarFile.toUri() + "</resource>" +
                "</driver>" +
            "</drivers>"));

        DriverVersion version = loader.findDriver("jar-binding-dep-driver", "1.0");
        assertNotNull(version);
        ((VerDef) version).setDsFactoryDef(new DsFactoryDef(TestDsFactory.class.getName(), this.getClass().getClassLoader()));

        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {
        });
        DriverBinding binding = loader.createBinding(this.getClass().getClassLoader(), "jar-binding-dep-driver", "1.0");
        binding.asClassLoader().addIncludePackages("sample.driver.*");

        Class<?> generatedClass = binding.asClassLoader().loadClass("sample.driver.GeneratedDriverMarker");
        Object generated = generatedClass.getDeclaredConstructor().newInstance();
        assertEquals("dep-ok", generatedClass.getMethod("ping").invoke(generated));
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

        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {
        });

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

        loader.prepareDriverVersion(version, resource -> false, new DriverPrepareProgress() {
        });
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
        return createTestJar("generated-driver.jar", Collections.singletonMap(className, sourceCode));
    }

    private Path createTestJar(String jarName, Map<String, String> sources) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull("system java compiler is required", compiler);

        Path sourceDir = Files.createDirectories(this.tempDir.resolve("generated-src"));
        Path outputDir = Files.createDirectories(this.tempDir.resolve("generated-classes"));
        List<File> sourceFiles = new ArrayList<>();
        for (Map.Entry<String, String> entry : sources.entrySet()) {
            Path sourceFile = sourceDir.resolve(entry.getKey().replace('.', '/') + ".java");
            Files.createDirectories(sourceFile.getParent());
            Files.write(sourceFile, entry.getValue().getBytes(StandardCharsets.UTF_8));
            sourceFiles.add(sourceFile.toFile());
        }

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(outputDir.toFile()));
            boolean success = compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjectsFromFiles(sourceFiles)).call();
            assertTrue("compile generated source failed", success);
        }

        Path jarFile = this.tempDir.resolve(jarName);
        try (JarOutputStream jarOutput = new JarOutputStream(Files.newOutputStream(jarFile))) {
            for (String className : sources.keySet()) {
                Path classFile = outputDir.resolve(className.replace('.', '/') + ".class");
                assertTrue(Files.exists(classFile));
                String entryName = className.replace('.', '/') + ".class";
                jarOutput.putNextEntry(new JarEntry(entryName));
                jarOutput.write(Files.readAllBytes(classFile));
                jarOutput.closeEntry();
            }
        }

        return jarFile;
    }

    private static final class ProgressRecorder implements DriverPrepareProgress {

        private final List<String> started   = new ArrayList<>();
        private final List<String> completed = new ArrayList<>();
        private final List<String> errors    = new ArrayList<>();

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
