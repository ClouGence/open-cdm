/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.service.upload.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import com.clougence.clouddm.api.common.GlobalConfUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractUploadService {

    protected static final long   MEGABYTE            = 1024L * 1024L;
    protected static final String STAGING_FILE_PREFIX = ".uploading-";
    private static final int      COPY_BUFFER_SIZE    = 8192;
    private static final String   UPLOAD_DIRECTORY    = "upload";

    protected final Path createStagingFile(String suffix) throws IOException {
        Path directory = Paths.get(GlobalConfUtils.getTempDataHome(), UPLOAD_DIRECTORY);
        Files.createDirectories(directory);
        return Files.createTempFile(directory, STAGING_FILE_PREFIX, suffix);
    }

    protected final void checkFileSize(long fileSize, long maxBytes, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (fileSize > maxBytes) {
            throw exceptionSupplier.get();
        }
    }

    protected final String extractFileName(String originalFileName) {
        String normalized = originalFileName.replace('\\', '/');
        return normalized.substring(normalized.lastIndexOf('/') + 1);
    }

    protected final long copyWithLimit(InputStream source, Path target, long maxBytes, Supplier<? extends RuntimeException> exceptionSupplier) throws IOException {
        long count = 0;
        try (InputStream input = source; OutputStream output = Files.newOutputStream(target)) {
            byte[] buffer = new byte[COPY_BUFFER_SIZE];
            int read;
            while ((read = input.read(buffer)) != -1) {
                count += read;
                if (count > maxBytes) {
                    throw exceptionSupplier.get();
                }
                output.write(buffer, 0, read);
            }
        }
        return count;
    }

    protected final void deleteQuietly(Path file) {
        if (file == null) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.warn("delete upload staging file failed: {}", file, e);
        }
    }
}
