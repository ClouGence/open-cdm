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
 */package com.clougence.clouddm.worker.component.session.storage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

import com.clougence.clouddm.sdk.service.file.FileService;
import com.clougence.clouddm.worker.component.result.FileResultSetOutputStream;
import com.clougence.clouddm.worker.component.result.ResultSetOutputStream;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultStorage {

    private final String          resultId;
    private final long            resultBytesLimit;
    private final long            columnBytesLimit;
    private final int             displayChars;

    private boolean               usingCache;
    private ResultSetOutputStream outputStream;
    private FileService           fileService;
    private String                cacheName;
    private File                  cacheFile;

    public ResultStorage(String resultId, long resultBytesLimit, long columnBytesLimit, int displayChars){
        this.resultId = resultId;
        this.resultBytesLimit = resultBytesLimit;
        this.columnBytesLimit = columnBytesLimit;
        this.displayChars = displayChars;
    }

    public void init() {
        this.usingCache = false;
    }

    public void init(FileService fileService, String cacheName, File cacheFile) {
        this.fileService = fileService;
        this.cacheName = cacheName;
        this.cacheFile = cacheFile;
        this.usingCache = true;
    }

    private ResultSetOutputStream getResultOutStream() throws IOException {
        if (this.usingCache && this.outputStream == null) {
            if (!this.cacheFile.exists()) {
                this.fileService.touchFile(this.cacheFile);
            }
            this.outputStream = new FileResultSetOutputStream(//
                this.cacheFile.getAbsoluteFile(),
                false,
                ByteOrder.BIG_ENDIAN,
                this.resultBytesLimit,
                this.columnBytesLimit);
        }

        return this.outputStream;
    }

    public RowStorage nextRsRow() throws IOException {
        ResultSetOutputStream stream = this.getResultOutStream();
        RowStorage storage = new RowStorage(stream, this.displayChars);
        storage.initRow();
        return storage;
    }

    public void flush() {
        if (this.outputStream != null) {
            try {
                this.outputStream.flush();
            } catch (Exception e) {
                log.error("Finish result storage output stream failed, resultId=" + this.resultId, e);
            }
        }
    }

    public void finish() {
        if (this.outputStream != null) {
            try {
                this.outputStream.flush();
            } catch (Exception e) {
                log.error("Finish result storage output stream failed, resultId=" + this.resultId, e);
            } finally {
                IOUtils.closeQuietly(this.outputStream);
            }
        }
    }

    public void free() {
        if (this.outputStream != null) {
            IOUtils.closeQuietly(this.outputStream);
        }
        if (fileService != null) {
            this.fileService.deleteFile(this.cacheFile);
        }
    }
}
