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
package com.clougence.utils.io.result;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

public class FileResultSetInputStream extends ResultSetInputStream {

    private final File             fileName;
    private final RandomAccessFile ioAccessFile;

    public FileResultSetInputStream(File file) throws IOException{
        this(file, null);
    }

    public FileResultSetInputStream(File file, ByteOrder order) throws IOException{
        super(order);
        this.fileName = file;

        if (!this.fileName.exists()) {
            throw new IOException("file not exists: " + this.fileName.getAbsolutePath());
        }
        this.ioAccessFile = new RandomAccessFile(this.fileName, "r");
        //this.isFileChannel = this.ioAccessFile.getChannel();

        this.initStream();
    }

    @Override
    protected long initFilePosition() throws IOException {
        return this.ioAccessFile.getFilePointer();
    }

    @Override
    protected long fileLength() throws IOException {
        return this.ioAccessFile.length();
    }

    @Override
    protected int getByte(long offset) throws IOException {
        if (this.ioAccessFile.getFilePointer() != offset) {
            this.ioAccessFile.seek(offset);
        }

        return this.ioAccessFile.readByte();
    }

    @Override
    protected int getBytes(long offset, byte[] b, int off, int length) throws IOException {
        if (this.ioAccessFile.getFilePointer() != offset) {
            this.ioAccessFile.seek(offset);
        }

        return this.ioAccessFile.read(b, off, length);
    }
}
