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
package com.clougence.clouddm.console.web.service.editor.script;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocalSqlScriptStorage implements SqlScriptStorage {

    private static final String  URI_PREFIX    = "local://sql-script/";
    private static final long    ORPHAN_TTL_MS = TimeUnit.HOURS.toMillis(1);
    private static final Pattern FILE_NAME     = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.sql");

    @Override
    public String put(String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        String fileName = UUID.randomUUID() + ".sql";
        Path directory = storageDirectory();
        Path target = directory.resolve(fileName);
        Path staging = directory.resolve(".uploading-" + fileName + "-" + UUID.randomUUID());
        try {
            Files.createDirectories(directory);
            writeDurably(staging, bytes);
            move(staging, target);
            if (!Arrays.equals(bytes, Files.readAllBytes(target))) {
                throw new IOException("published SQL script differs from source");
            }
            return URI_PREFIX + fileName;
        } catch (IOException e) {
            deleteQuietly(staging);
            deleteQuietly(target);
            throw new IllegalStateException("store SQL script failed", e);
        }
    }

    @Override
    public String read(String fileUri) {
        try {
            return Files.readString(resolveExisting(fileUri), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("read SQL script failed", e);
        }
    }

    @Override
    public void touch(String fileUri) {
        try {
            Files.setLastModifiedTime(resolveExisting(fileUri), FileTime.fromMillis(System.currentTimeMillis()));
        } catch (NoSuchFileException e) {
            // A missing file does not prevent replacing or deleting its metadata index.
        } catch (IOException e) {
            throw new IllegalStateException("touch SQL script failed", e);
        }
    }

    @Override
    public void delete(String fileUri) {
        String fileName = fileName(fileUri);
        try {
            Files.deleteIfExists(storageDirectory().resolve(fileName));
            Files.deleteIfExists(legacyStorageDirectory().resolve(fileName));
        } catch (IOException e) {
            throw new IllegalStateException("delete SQL script failed", e);
        }
    }

    @Override
    public void cleanupOrphans(Set<String> referencedFileUris) {
        cleanupOrphans(storageDirectory(), referencedFileUris);
        cleanupOrphans(legacyStorageDirectory(), referencedFileUris);
    }

    private static void cleanupOrphans(Path directory, Set<String> referencedFileUris) {
        if (!Files.isDirectory(directory)) {
            return;
        }
        long expireBefore = System.currentTimeMillis() - ORPHAN_TTL_MS;
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {
            for (Path file : files) {
                if (!Files.isRegularFile(file) || Files.getLastModifiedTime(file).toMillis() > expireBefore) {
                    continue;
                }
                String fileName = file.getFileName().toString();
                if (fileName.startsWith(".uploading-") || (FILE_NAME.matcher(fileName).matches() && !referencedFileUris.contains(URI_PREFIX + fileName))) {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException e) {
                        log.warn("Failed to clean up an orphan SQL script file; it will be retried", e);
                    }
                }
            }
        } catch (IOException e) {
            log.warn("clean orphan SQL scripts failed", e);
        }
    }

    public static Path storageDirectory() {
        return Paths.get(GlobalConfUtils.getUserDataHome(), "sql-scripts").toAbsolutePath().normalize();
    }

    private static Path legacyStorageDirectory() {
        return Paths.get(GlobalConfUtils.getUserDataHome(), "sql-script").toAbsolutePath().normalize();
    }

    private static Path resolveExisting(String fileUri) {
        String fileName = fileName(fileUri);
        Path file = storageDirectory().resolve(fileName);
        if (Files.exists(file)) {
            return file;
        }
        Path legacyFile = legacyStorageDirectory().resolve(fileName);
        if (Files.exists(legacyFile)) {
            return legacyFile;
        }
        return file;
    }

    private static String fileName(String fileUri) {
        if (fileUri == null || !fileUri.startsWith(URI_PREFIX)) {
            throw new IllegalArgumentException("unsupported SQL script URI");
        }
        String fileName = fileUri.substring(URI_PREFIX.length());
        if (!FILE_NAME.matcher(fileName).matches()) {
            throw new IllegalArgumentException("invalid SQL script URI");
        }
        return fileName;
    }

    private static void writeDurably(Path file, byte[] bytes) throws IOException {
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.force(true);
        }
    }

    private static void move(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target);
        }
    }

    private static void deleteQuietly(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // The periodic cleanup retries stale staging and orphan files.
        }
    }
}
