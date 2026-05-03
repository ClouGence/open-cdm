package com.clougence.drivers.def;

import java.io.File;
import java.io.IOException;
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
    private final List<ResDef>             resources = new ArrayList<>();

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
        refreshTimestamp();
        return this.timestamp;
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
                return;
            }
        }

        this.resources.add(resource);
        this.refreshTimestamp();
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
}
