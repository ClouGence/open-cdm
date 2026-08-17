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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.clougence.drivers.DriverFile;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;

public class DmDriverDownloadTaskTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void resolveTransferFiles_shouldIncludeFilesIndex() throws Exception {
        File versionDir = this.temporaryFolder.newFolder("MySQL Connector J", "8.0.33");
        File driverJar = new File(versionDir, "mysql-connector-j-8.0.33.jar");
        File filesIndex = new File(versionDir, "files.idx");
        Files.writeString(driverJar.toPath(), "jar");
        Files.writeString(filesIndex.toPath(), "relative 1 mysql-connector-j-8.0.33.jar");

        DriverFile driverFile = new DriverFile();
        driverFile.setAbsolutePath(driverJar.getAbsolutePath());
        driverFile.setRelativePath(driverJar.getName());
        driverFile.setPrepared(true);

        DmDriverDownloadTask task = new DmDriverDownloadTask("uid", 1L, "MySQL Connector J", "8.0.33", null, null);
        List<DriverFile> transferFiles = task.resolveTransferFiles(new TestDriverVersion(versionDir, List.of(driverFile)));

        assertEquals(2, transferFiles.size());
        assertTrue(transferFiles.stream().anyMatch(file -> driverJar.getName().equals(file.getRelativePath())));
        assertTrue(transferFiles.stream().anyMatch(file -> "files.idx".equals(file.getRelativePath()) && file.isPrepared()));
    }

    private static class TestDriverVersion implements DriverVersion {

        private final File             versionDir;
        private final List<DriverFile> files;
        private boolean                prepared;

        private TestDriverVersion(File versionDir, List<DriverFile> files){
            this.versionDir = versionDir;
            this.files = new ArrayList<>(files);
        }

        @Override
        public String getFamilyName() { return "family"; }

        @Override
        public String getVersion() { return "version"; }

        @Override
        public File getAbsoluteDir() { return this.versionDir; }

        @Override
        public File getRelativeDir() { return this.versionDir; }

        @Override
        public long getTimestamp() { return 0; }

        @Override
        public String getDsFactory() { return null; }

        @Override
        public boolean isDefault() { return false; }

        @Override
        public boolean isPrepared() { return this.prepared; }

        @Override
        public void setPrepared(boolean prepared) { this.prepared = prepared; }

        @Override
        public List<ResDef> getResources() { return List.of(); }

        @Override
        public void addResource(ResDef resource) {
        }

        @Override
        public List<DriverFile> getFiles() { return this.files; }

        @Override
        public void deleteFiles() {
        }
    }
}
