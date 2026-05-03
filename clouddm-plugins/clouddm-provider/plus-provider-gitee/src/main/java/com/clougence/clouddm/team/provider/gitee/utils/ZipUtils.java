package com.clougence.clouddm.team.provider.gitee.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.clougence.clouddm.team.provider.gitee.constants.GiteeI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.ArrayUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.ESupplier;
import com.clougence.utils.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipUtils {

    private long   time;
    private String currentFile;

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
            throw ThirdPartyApiException.asDM().with(GiteeI18nKeys.GITEE_UNZIP_ZIP_NOT_EXIST_ERROR, sourceFile.getAbsolutePath());
        }
        if (targetDir.isFile()) {
            throw ThirdPartyApiException.asDM().with(GiteeI18nKeys.GITEE_UNZIP_DST_IS_FILE_ERROR, targetDir.getAbsolutePath());
        }

        targetDir.mkdirs();
        log.info("unZip " + sourceFile);

        long start = System.currentTimeMillis();

        try (ZipFile zipFile = new ZipFile(sourceFile)) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
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
                boolean marched = StringUtils.isBlank(keepPath) || StringUtils.startsWith(currentFile, keepPath);
                if (!marched) {
                    log.info("unZip " + currentFile + " skip. (matches script)");
                    continue;
                }

                // unzip script
                File targetFile = new File(targetDir + "/" + currentFile);
                createFileIfNotExist(targetFile);
                try (InputStream in = zipFile.getInputStream(entry); FileOutputStream out = new FileOutputStream(targetFile)) {
                    long bytesReadTotal = 0;

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        bytesReadTotal = bytesReadTotal + bytesRead;

                        printProcess(entry, currentFile, bytesReadTotal);

                        if (!watchdog.eGet()) {
                            log.info("unZip interrupt cost " + (System.currentTimeMillis() - start) + " ms");
                            return;
                        }
                    }
                }
            }

            log.info("unZip cost " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw ThirdPartyApiException.asDM().with(e, GiteeI18nKeys.GITEE_UNZIP_ERROR, e.getMessage());
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
