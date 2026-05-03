package com.clougence.clouddm.worker.component.result;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

public class FileResultSetOutputStream extends ResultSetOutputStream {

    private final File             fileName;
    private final boolean          append;
    private final long             fileLimit;
    private final RandomAccessFile ioAccessFile;

    public FileResultSetOutputStream(File file, boolean append) throws IOException{
        this(file, append, null, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    public FileResultSetOutputStream(File file, boolean append, ByteOrder order) throws IOException{
        this(file, append, order, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    public FileResultSetOutputStream(File file, boolean append, ByteOrder order, long fileLimit, long dataLimit) throws IOException{
        super(order);
        this.fileName = file;
        this.append = append;
        this.fileLimit = fileLimit;

        if (!this.fileName.exists()) {
            this.fileName.createNewFile();
        }
        this.ioAccessFile = new RandomAccessFile(this.fileName, "rw");
        //this.isFileChannel = this.ioAccessFile.getChannel();

        if (this.append) {
            this.ioAccessFile.seek(this.ioAccessFile.length());
        } else {
            this.ioAccessFile.seek(0);
            this.ioAccessFile.setLength(0);
        }

        this.initStream(append);
    }

    @Override
    protected long initFilePosition() throws IOException {
        return this.ioAccessFile.getFilePointer();
    }

    @Override
    protected void setBytes(long offset, byte b) throws IOException {
        this.setBytes(offset, new byte[] { b }, 0, 1);
    }

    @Override
    protected void setBytes(long offset, byte[] b, int off, int length) throws IOException {
        if (offset + length > this.fileLimit) {
            long overflowSize = offset + length - this.fileLimit;
            throw new ResultSetOverflowException("overflow: " + overflowSize, overflowSize);
        }

        if (this.ioAccessFile.getFilePointer() != offset) {
            this.ioAccessFile.seek(offset);
        }

        this.ioAccessFile.write(b, off, length);
    }

    @Override
    protected void setLength(long length) throws IOException {
        this.ioAccessFile.setLength(length);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        this.ioAccessFile.getFD().sync();
    }
}
