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
package com.clougence.drivers.def;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import com.clougence.drivers.DriverFile;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.factory.DsFactoryDef;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FileUtils;
import com.clougence.utils.io.FilenameUtils;
import com.clougence.utils.loader.ResourceLoader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VerDef implements DriverVersion {

    private static final String            FILES_INDEX_NAME = "files.idx";
    private String                         familyName;
    private String                         version;
    private String                         driverName;
    @Getter
    private String                         comment;
    @Getter
    private ResourceLoader                 loader;
    @Getter
    private DsFactoryDef                   dsFactoryDef;
    @Getter
    private Map<ClassLoader, DsFactory<?>> loaderMap;
    @Getter
    private File                           localDir;
    private boolean                        prepared;
    private volatile long                  timestamp;
    private final List<ResDef>             resources        = new ArrayList<>();

    @Override
    public String getFamilyName() { return this.familyName; }

    public void setFamilyName(String familyName) {
        String normalized = StringUtils.trimToNull(familyName);
        if (Objects.equals(this.familyName, normalized)) {
            return;
        }

        this.familyName = normalized;
        this.refreshTimestamp();
    }

    @Override
    public String getVersion() { return this.version; }

    public void setVersion(String version) {
        String normalized = StringUtils.trimToNull(version);
        if (Objects.equals(this.version, normalized)) {
            return;
        }

        this.version = normalized;
        this.refreshTimestamp();
    }

    @Override
    public File getAbsoluteDir() { return new File(this.localDir, this.getRelativeDir().getPath()); }

    @Override
    public File getRelativeDir() {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(version)) {
            return null;
        } else {
            return new File(//
                this.familyName.replace('/', ' ').replace('\\', ' ').trim(),//
                this.version.replace('/', ' ').replace('\\', ' ').trim());
        }
    }

    public void setLocalDir(File localDir) {
        if (Objects.equals(this.localDir, localDir)) {
            return;
        }

        this.localDir = localDir;
        this.refreshTimestamp();
    }

    @Override
    public long getTimestamp() {
        long result = this.timestamp;
        for (ResDef resource : this.resources) {
            if (resource == null || resource.getFileDefList() == null) {
                continue;
            }

            result = 31 * result + Boolean.hashCode(resource.isPrepared());
            for (FileDef fileDef : resource.getFileDefList()) {
                result = appendFileTimestamp(result, fileDef);
            }
        }
        return result;
    }

    @Override
    public String getDsFactory() { return this.driverName; }

    public void setDsFactory(String driverName) {
        String normalized = StringUtils.trimToNull(driverName);
        if (Objects.equals(this.driverName, normalized)) {
            return;
        }

        this.driverName = normalized;
        this.refreshTimestamp();
    }

    public void setComment(String comment) {
        String normalized = StringUtils.trimToNull(comment);
        if (Objects.equals(this.comment, normalized)) {
            return;
        }

        this.comment = normalized;
        this.refreshTimestamp();
    }

    public void setLoader(ResourceLoader loader) {
        if (Objects.equals(this.loader, loader)) {
            return;
        }

        this.loader = loader;
        this.refreshTimestamp();
    }

    public void setDsFactoryDef(DsFactoryDef dsFactoryDef) {
        if (Objects.equals(this.dsFactoryDef, dsFactoryDef)) {
            return;
        }

        this.dsFactoryDef = dsFactoryDef;
        this.refreshTimestamp();
    }

    public void setLoaderMap(Map<ClassLoader, DsFactory<?>> loaderMap) {
        if (Objects.equals(this.loaderMap, loaderMap)) {
            return;
        }

        this.loaderMap = loaderMap;
        this.refreshTimestamp();
    }

    @Override
    public boolean isPrepared() { return this.prepared; }

    public void setPrepared(boolean prepared) {
        if (this.prepared == prepared) {
            return;
        }

        this.prepared = prepared;
        this.refreshTimestamp();
    }

    @Override
    public List<ResDef> getResources() { return Collections.unmodifiableList(this.resources); }

    @Override
    public void addResource(ResDef resource) {
        if (resource == null) {
            return;
        }
        if (StringUtils.isBlank(resource.getResourceType()) || StringUtils.isBlank(resource.getCoordinate())) {
            return;
        }

        this.normalizeResourceFiles(resource);
        for (ResDef exists : this.resources) {
            if (StringUtils.equalsIgnoreCase(exists.getResourceType(), resource.getResourceType())
                && StringUtils.equalsIgnoreCase(exists.getCoordinate(), resource.getCoordinate())) {
                this.restoreFilesIndex(exists);
                return;
            }
        }

        this.resources.add(resource);
        this.restoreFilesIndex(resource);
        this.refreshTimestamp();
    }

    private void restoreFilesIndex(ResDef resource) {
        if (resource == null || (resource.getFileDefList() != null && !resource.getFileDefList().isEmpty())) {
            return;
        }
        if (this.localDir == null || this.getRelativeDir() == null) {
            return;
        }

        File versionDir = this.getAbsoluteDir();
        File indexFile = new File(versionDir, FILES_INDEX_NAME);
        if (!indexFile.isFile()) {
            return;
        }

        String resourceId = Long.toString(resource.getFilesIndexId());
        List<FileDef> fileDefs = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(indexFile.toPath(), StandardCharsets.UTF_8)) {
                FileDef fileDef = parseFilesIndexLine(resourceId, versionDir, line);
                if (fileDef != null) {
                    fileDefs.add(fileDef);
                }
            }
        } catch (IOException e) {
            log.warn("restore driver files index failed, family={}, version={}, resourceType={}, coordinate={}", //
                    this.familyName, this.version, resource.getResourceType(), resource.getCoordinate(), e);
            return;
        }

        if (fileDefs.isEmpty()) {
            return;
        }

        boolean allPrepared = true;
        for (FileDef fileDef : fileDefs) {
            String absolutePath = StringUtils.trimToNull(fileDef.getAbsolutePath());
            fileDef.setPrepared(absolutePath != null && new File(absolutePath).exists());
            allPrepared = allPrepared && fileDef.isPrepared();
        }
        resource.setFileDefList(fileDefs);
        resource.setPrepared(allPrepared);
        this.refreshPreparedState();
    }

    private FileDef parseFilesIndexLine(String resourceId, File versionDir, String line) {
        String trimmed = StringUtils.trimToNull(line);
        if (trimmed == null) {
            return null;
        }

        int firstSeparator = trimmed.indexOf(' ');
        if (firstSeparator < 0) {
            return null;
        }

        String type = StringUtils.trimToNull(trimmed.substring(0, firstSeparator));
        String remainder = StringUtils.trimToNull(trimmed.substring(firstSeparator + 1));
        if (type == null || remainder == null) {
            return null;
        }

        int secondSeparator = remainder.indexOf(' ');
        if (secondSeparator < 0) {
            return null;
        }

        String lineResourceId = StringUtils.trimToNull(remainder.substring(0, secondSeparator));
        String path = StringUtils.trimToNull(remainder.substring(secondSeparator + 1));
        if (!StringUtils.equals(resourceId, lineResourceId) || path == null) {
            return null;
        }

        FileDef fileDef = new FileDef();
        if (StringUtils.equalsIgnoreCase("relative", type)) {
            String relativePath = FilenameUtils.separatorsToUnix(path);
            fileDef.setRelativePath(relativePath);
            fileDef.setAbsolutePath(new File(versionDir, relativePath).getAbsolutePath());
            return fileDef;
        }
        if (StringUtils.equalsIgnoreCase("absolute", type)) {
            fileDef.setAbsolutePath(path);
            fileDef.setRelativePath(new File(path).getName());
            return fileDef;
        }
        return null;
    }

    private void refreshPreparedState() {
        if (this.resources.isEmpty()) {
            this.setPrepared(true);
            return;
        }

        boolean allPrepared = true;
        for (ResDef resource : this.resources) {
            allPrepared = allPrepared && resource != null && resource.isPrepared();
        }
        this.setPrepared(allPrepared);
    }

    private void normalizeResourceFiles(ResDef resource) {
        if (resource == null || resource.getFileDefList() == null) {
            return;
        }

        for (FileDef fileDef : resource.getFileDefList()) {
            String relativePath = StringUtils.trimToNull(fileDef.getRelativePath());
            if (relativePath == null) {
                throw new IllegalArgumentException("relativePath is blank.");
            }
            relativePath = FilenameUtils.separatorsToUnix(relativePath);
            fileDef.setRelativePath(relativePath);
            this.validateRelativePath(relativePath);

            String absolutePath = StringUtils.trimToNull(fileDef.getAbsolutePath());
            if (absolutePath != null && new File(absolutePath).isAbsolute()) {
                return;
            }

            if (this.localDir == null) {
                return;
            }

            fileDef.setAbsolutePath(new File(this.localDir, relativePath).getAbsolutePath());
        }
    }

    private void validateRelativePath(String relativePath) {
        String normalized = FilenameUtils.separatorsToUnix(relativePath);
        for (String segment : normalized.split("/")) {
            if ("..".equals(segment)) {
                throw new IllegalArgumentException("unsupported relative path: " + relativePath);
            }
        }
    }

    @Override
    public List<DriverFile> getFiles() {
        List<DriverFile> files = new ArrayList<>();
        for (ResDef resource : this.resources) {
            if (resource.getFileDefList() == null) {
                continue;
            }

            for (FileDef fileDef : resource.getFileDefList()) {
                DriverFile f = new DriverFile();
                f.setAbsolutePath(fileDef.getAbsolutePath());
                f.setRelativePath(fileDef.getRelativePath());
                f.setPrepared(fileDef.isPrepared());
                files.add(f);
            }
        }

        return files;
    }

    @Override
    public void deleteFiles() {
        try {
            File versionDir = this.getAbsoluteDir();
            if (versionDir != null && versionDir.exists()) {
                FileUtils.forceDelete(versionDir);
            }
        } catch (IOException e) {
            File driverDir = this.getAbsoluteDir();
            log.warn("reset driver resource failed, family={}, version={}, dir={}", familyName, version, driverDir.getAbsolutePath(), e);
        } finally {
            this.refreshTimestamp();
        }
    }

    private void refreshTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    private static long appendFileTimestamp(long timestamp, FileDef fileDef) {
        if (fileDef == null) {
            return 31 * timestamp;
        }

        long result = timestamp;
        result = 31 * result + Boolean.hashCode(fileDef.isPrepared());
        result = 31 * result + Objects.hashCode(fileDef.getRelativePath());
        result = 31 * result + Objects.hashCode(fileDef.getAbsolutePath());

        String absolutePath = StringUtils.trimToNull(fileDef.getAbsolutePath());
        if (absolutePath == null) {
            return result;
        }

        File file = new File(absolutePath);
        result = 31 * result + Boolean.hashCode(file.exists());
        if (file.exists()) {
            result = 31 * result + file.lastModified();
            result = 31 * result + file.length();
        }
        return result;
    }
}
