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
package com.clougence.clouddm.team.provider.gitee.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.scm.ScmUtils;
import com.clougence.clouddm.team.provider.gitee.constants.GiteeI18nKeys;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.ESupplier;
import com.clougence.utils.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipUtils {

    /**
     * Maximum number of entries extracted from one repository archive, protecting against archive entry floods.
     */
    private static final int  MAX_FILES           = 10_000;
    /**
     * Maximum cumulative uncompressed size of one repository archive, in bytes, protecting against ZIP bombs.
     */
    private static final long MAX_EXTRACTED_BYTES = 2L * 1024 * 1024 * 1024;
    /**
     * Maximum uncompressed size of one SQL file, in bytes, so later SQL parsing does not consume unbounded memory.
     */
    private static final long MAX_SQL_BYTES       = 50L * 1024 * 1024;

    private long              time;
    private String            currentFile;

    private void printProcess(ZipEntry entry, String currentFile, long total) {
        if (!StringUtils.equals(this.currentFile, currentFile)) {
            this.currentFile = currentFile;
            this.time = System.currentTimeMillis();
            log.info("unZip " + entry.getName() + " to " + currentFile);
            return;
        }

        if ((this.time + 2000) <= System.currentTimeMillis()) {
            this.time = System.currentTimeMillis();
            log.info("unZip " + currentFile + "... compression size " + FileUtils.readableFileSize(total));
        }
    }

    public void unZip(File sourceFile, File targetDir, String keepPath, int floor, ESupplier<Boolean, Exception> watchdog) {
        if (!sourceFile.exists()) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_UNZIP_ZIP_NOT_EXIST_ERROR, sourceFile.getAbsolutePath());
        }
        if (targetDir.isFile()) {
            throw ThirdPartyApiException.as().with(GiteeI18nKeys.GITEE_UNZIP_DST_IS_FILE_ERROR, targetDir.getAbsolutePath());
        }

        targetDir.mkdirs();
        log.info("unZip " + sourceFile);

        long start = System.currentTimeMillis();

        try (ZipFile zipFile = ZipFile.builder().setFile(sourceFile).get()) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            long extractedBytes = 0;
            int extractedFiles = 0;
            String targetRoot = targetDir.getCanonicalPath() + File.separator;
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String entryName = entry.getName();
                String currentFile = floorFileName(entryName, floor);

                // skip floor
                if (StringUtils.isBlank(currentFile)) {
                    log.info("unZip " + entryName + " skip. (floor)");
                    continue;
                }

                // skip dir.
                if (entry.isDirectory()) {
                    log.info("unZip " + currentFile + " skip. (isDirectory)");
                    continue;
                }

                // matches script dir
                String normalizedKeepPath = normalizeKeepPath(keepPath);
                boolean marched = StringUtils.isBlank(normalizedKeepPath) || StringUtils.equals(currentFile, normalizedKeepPath)
                                  || StringUtils.startsWith(currentFile, normalizedKeepPath + "/");
                if (!marched) {
                    log.info("unZip " + currentFile + " skip. (matches script)");
                    continue;
                }
                if (entry.isUnixSymlink()) {
                    throw new IOException("symbolic links are not allowed: " + currentFile);
                }

                // unzip script
                extractedFiles++;
                if (extractedFiles > MAX_FILES) {
                    throw new IOException("more than 10000 files");
                }
                File targetFile = new File(targetDir, currentFile);
                if (!targetFile.getCanonicalPath().startsWith(targetRoot)) {
                    throw new IOException("unsafe archive path");
                }
                createFileIfNotExist(targetFile);
                try (InputStream in = zipFile.getInputStream(entry); FileOutputStream out = new FileOutputStream(targetFile)) {
                    long bytesReadTotal = 0;

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        bytesReadTotal = bytesReadTotal + bytesRead;
                        extractedBytes += bytesRead;
                        if (extractedBytes > MAX_EXTRACTED_BYTES) {
                            throw new IOException("extracted archive exceeds 2 GiB");
                        }
                        if (currentFile.toLowerCase(Locale.ROOT).endsWith(".sql") && bytesReadTotal > MAX_SQL_BYTES) {
                            throw new IOException("SQL file exceeds 50 MiB: " + currentFile);
                        }

                        printProcess(entry, currentFile, bytesReadTotal);

                        if (!watchdog.eGet()) {
                            log.info("unZip interrupt cost " + (System.currentTimeMillis() - start) + " ms");
                            throw new IOException("archive extraction interrupted");
                        }
                    }
                }
            }

            log.info("unZip cost " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw ThirdPartyApiException.as().with(e, GiteeI18nKeys.GITEE_UNZIP_ERROR, e.getMessage());
        }
    }

    private static String normalizeKeepPath(String keepPath) throws IOException {
        try {
            return ScmUtils.normalizeDirectoryPath(keepPath);
        } catch (IllegalArgumentException e) {
            throw new IOException("invalid script path", e);
        }
    }

    private static String floorFileName(String fileName, int floor) {
        String[] namePart = StringUtils.split(fileName, "/");
        if (namePart.length > floor) {
            String[] subarray = (String[]) ArrayUtils.subarray(namePart, floor, namePart.length);
            return StringUtils.join(subarray, "/");
        }
        return null;
    }

    private static void createDirIfNotExist(String path) {
        File file = new File(path);
        createDirIfNotExist(file);
    }

    private static void createDirIfNotExist(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void createFileIfNotExist(File file) throws IOException {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

    private static void createParentDirIfNotExist(File file) {
        createDirIfNotExist(file.getParentFile());
    }
}
