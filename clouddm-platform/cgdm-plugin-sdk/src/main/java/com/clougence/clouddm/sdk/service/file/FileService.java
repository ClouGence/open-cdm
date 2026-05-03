package com.clougence.clouddm.sdk.service.file;

import java.io.File;
import java.io.IOException;

import com.clougence.clouddm.sdk.service.Service;

public interface FileService extends Service {

    File createFileObject(String fileName, boolean tempFile);

    boolean touchFile(File file);

    boolean deleteFile(File file);

    boolean existsFile(File file);

    boolean moveFile(File dst, File src);

    long fileSize(File file);

    byte[] fileRead(File file, long offset, int length) throws IOException;
}
