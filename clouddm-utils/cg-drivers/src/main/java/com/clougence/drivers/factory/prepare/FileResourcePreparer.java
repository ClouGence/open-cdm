package com.clougence.drivers.factory.prepare;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.FileDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FilenameUtils;

public class FileResourcePreparer extends AbstractResourcePreparer {

    public FileResourcePreparer(File localDir, Properties config){
        super(localDir, config);
    }

    @Override
    public void analysis(DriverVersion driverVersion, ResDef driverResource, ClassLoader classLoader, DriverPrepareProgress progress) {
        if (driverResource == null) {
            return;
        }

        //
        List<File> sourceFiles = new ArrayList<>();
        for (String pathItem : StringUtils.split(driverResource.getCoordinate(), ",")) {
            String trimmed = StringUtils.trimToNull(pathItem);
            if (trimmed == null) {
                continue;
            }

            if (trimmed.startsWith("file:")) {
                sourceFiles.add(new File(URI.create(trimmed)));
            } else {
                if (!new File(trimmed).isAbsolute()) {
                    String normalized = FilenameUtils.separatorsToUnix(trimmed);
                    for (String segment : normalized.split("/")) {
                        if ("..".equals(segment)) {
                            throw new IllegalArgumentException("unsupported relative path: " + trimmed);
                        }
                    }
                }
                sourceFiles.add(new File(trimmed));
            }
        }

        //
        List<FileDef> fileDefs = new ArrayList<>(sourceFiles.size());
        for (File sourceFile : sourceFiles) {
            FileDef fileDef = new FileDef();
            fileDef.setPrepared(false);

            if (sourceFile.isAbsolute()) {
                fileDef.setRelativePath(sourceFile.getName());
                fileDef.setAbsolutePath(sourceFile.getAbsolutePath());
            } else {
                fileDef.setRelativePath(FilenameUtils.normalize(sourceFile.getPath()));
                fileDef.setAbsolutePath(new File(driverVersion.getRelativeDir(), fileDef.getRelativePath()).getPath());
            }
            fileDefs.add(fileDef);
        }

        driverResource.setFileDefList(fileDefs);
    }

    @Override
    public void resolve(DriverVersion driverVersion, ResDef driverResource,//
                        ClassLoader classLoader, DriverPrepareProgress progress) throws IOException {
        List<FileDef> files = driverResource.getFileDefList();
        if (files == null || files.isEmpty()) {
            return;
        }

        int totalCount = files.size();
        progress.onStart(driverVersion, driverResource, 0, totalCount);

        for (int i = 0; i < totalCount; i++) {
            FileDef fileDef = files.get(i);
            if (fileDef == null) {
                continue;
            }

            String absolutePath = StringUtils.trimToNull(fileDef.getAbsolutePath());
            String progressFileName = StringUtils.defaultIfBlank(fileDef.getRelativePath(), absolutePath);
            progress.onProgress(driverVersion, driverResource, progressFileName, i + 1L, totalCount);

            File targetFile = absolutePath == null ? null : new File(absolutePath);
            if (targetFile == null || !targetFile.exists()) {
                fileDef.setPrepared(false);
                progress.onError(driverVersion, driverResource, new IOException("file not found: " + absolutePath));
                return;
            }

            if (!targetFile.canRead()) {
                fileDef.setPrepared(false);
                progress.onError(driverVersion, driverResource, new IOException("file exists but cannot read: " + absolutePath));
                return;
            }

            fileDef.setPrepared(true);
        }

        progress.onComplete(driverVersion, driverResource, totalCount, totalCount);
    }
}
