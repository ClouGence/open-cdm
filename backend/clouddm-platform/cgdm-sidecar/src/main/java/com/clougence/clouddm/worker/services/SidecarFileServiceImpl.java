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
 */package com.clougence.clouddm.worker.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.sdk.service.file.FileService;
import com.clougence.utils.io.FileUtils;

@Service
public class SidecarFileServiceImpl implements FileService {

    // readOnly file service, only for sidecar
    private final class DmFile extends File {

        private boolean tempFile = false;

        public DmFile(boolean tempFile, String pathName){
            super(pathName);
            this.tempFile = tempFile;
        }

        @Override
        public File getAbsoluteFile() {
            if (this.tempFile) {
                return new File(GlobalConfUtils.getTempDataHome(), super.getPath());
            } else {
                return new File(GlobalConfUtils.getAppDataHome(), super.getPath());
            }
        }

        @Override
        public String getAbsolutePath() {
            if (this.tempFile) {
                return GlobalConfUtils.getTempDataHome() + File.separator + super.getPath();
            } else {
                return GlobalConfUtils.getAppDataHome() + File.separator + super.getPath();
            }
        }

        @Override
        public boolean exists() {
            return this.getAbsoluteFile().exists();
        }

        @Override
        public boolean delete() {
            throw new UnsupportedOperationException("DmFile is not supported, use FileService.delete");
        }

        @Override
        public void deleteOnExit() {
            throw new UnsupportedOperationException("DmFile is not supported, use FileService.delete");
        }

        @Override
        public boolean renameTo(File dest) {
            throw new UnsupportedOperationException("DmFile is not supported, use FileService.moveFile");
        }

        @Override
        public boolean mkdir() {
            throw new UnsupportedOperationException("DmFile is not supported.");
        }

        @Override
        public boolean mkdirs() {
            throw new UnsupportedOperationException("DmFile is not supported.");
        }

        @Override
        public boolean createNewFile() throws IOException {
            throw new UnsupportedOperationException("DmFile is not supported, use FileService.createFile");
        }
    }

    @Override
    public File createFileObject(String fileName, boolean tempFile) {
        return new DmFile(tempFile, fileName);
    }

    @Override
    public boolean touchFile(File file) {
        if (!(file instanceof DmFile)) {
            throw new IllegalArgumentException("file must be instance of DmFile");
        }

        File absoluteFile = file.getAbsoluteFile();
        if (absoluteFile.exists()) {
            return true;
        } else {
            try {
                FileUtils.mkdirs(absoluteFile.getParentFile());
                absoluteFile.createNewFile();
                return absoluteFile.exists();
            } catch (IOException e) {
                return false;
            }
        }
    }

    @Override
    public boolean deleteFile(File file) {
        if (!(file instanceof DmFile)) {
            throw new IllegalArgumentException("file must be instance of DmFile");
        }

        File absoluteFile = file.getAbsoluteFile();
        if (absoluteFile.exists()) {
            return absoluteFile.delete();
        } else {
            return true;
        }
    }

    @Override
    public boolean existsFile(File file) {
        if (!(file instanceof DmFile)) {
            throw new IllegalArgumentException("file must be instance of DmFile");
        }

        File absoluteFile = file.getAbsoluteFile();
        return absoluteFile.exists();
    }

    @Override
    public boolean moveFile(File dst, File src) {
        if (!(dst instanceof DmFile) || !(src instanceof DmFile)) {
            throw new IllegalArgumentException("dst and src must be instance of DmFile");
        }

        File srcFile = src.getAbsoluteFile();
        if (!srcFile.exists()) {
            return false;
        }

        File dstFile = dst.getAbsoluteFile();
        if (dstFile.exists()) {
            this.deleteFile(dst);
        }

        FileUtils.mkdirs(dstFile.getParentFile());
        return srcFile.renameTo(dstFile);
    }

    @Override
    public long fileSize(File file) {
        if (!(file instanceof DmFile)) {
            throw new IllegalArgumentException("file must be instance of DmFile");
        }

        File absoluteFile = file.getAbsoluteFile();
        return absoluteFile.length();
    }

    @Override
    public byte[] fileRead(File file, long offset, int length) throws IOException {
        if (!(file instanceof DmFile)) {
            throw new IllegalArgumentException("file must be instance of DmFile");
        }

        File absoluteFile = file.getAbsoluteFile();
        try (RandomAccessFile ioFile = new RandomAccessFile(absoluteFile, "r")) {
            byte[] cache = new byte[length];
            ioFile.seek(offset);
            int read = ioFile.read(cache, 0, length);
            if (read < 0) {
                return null; // EOF
            } else if (read < length) {
                byte[] result = new byte[read];
                System.arraycopy(cache, 0, result, 0, read);
                return result;
            } else {
                return cache;
            }
        }
    }
}
