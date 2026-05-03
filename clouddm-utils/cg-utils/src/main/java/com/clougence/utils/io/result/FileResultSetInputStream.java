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
