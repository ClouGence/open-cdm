/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.utils.io.bytes;

import java.nio.ByteBuffer;

/**
 * 数组自动扩缩容 {@link BytesIO} 实现
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-11-01
 */
final class AutoArrayBytesIO extends AbstractBytesIO {

    AutoArrayBytesIO(int maxCapacity, int extensionSize, byte[] initData, boolean asWrite) {
        super(maxCapacity);
        this.extensionSize = Math.min(extensionSize, maxCapacity);
        this.target = initData;

        if (!asWrite) {
            this.writerIndex = initData.length;
            this.markedWriterIndex = initData.length;
        }
    }

    // ------------------------------------------------------------------------

    byte[] target;
    private final int extensionSize;

    @Override
    public BytesIO markReader() {
        if (this.markedReaderIndex != this.readerIndex) {
            this.markedReaderIndex = this.readerIndex;
            this.recycle();
        }
        return this;
    }

    private int evalSize(int requestSize) {
        int maxCap = this.getMaxCapacity();
        int newSize;
        if ((requestSize % this.extensionSize) > 0) {
            int rate = (requestSize / this.extensionSize) + 1;
            newSize = Math.min(rate * this.extensionSize, maxCap);
        } else {
            newSize = Math.min(requestSize + this.extensionSize, maxCap);
        }
        return Math.min(newSize, maxCap);
    }

    private void checkExtension(int offset, int len) {
        int currentCap = this.capacity();
        int requestSize = offset + len;
        if (requestSize > currentCap) {
            byte[] extension = new byte[evalSize(requestSize)];
            System.arraycopy(this.target, 0, extension, 0, currentCap);
            this.target = extension;
        }
    }

    private void recycle() {
        int requestSize = this.writerIndex - this.markedReaderIndex;
        byte[] recycle = new byte[evalSize(requestSize)];
        System.arraycopy(this.target, this.markedReaderIndex, recycle, 0, requestSize);

        int recyclePos = this.markedReaderIndex;
        this.target = recycle;
        this.writerIndex = this.writerIndex - recyclePos;
        this.markedWriterIndex = this.markedWriterIndex - recyclePos;
        this.readerIndex = this.readerIndex - recyclePos;
        this.markedReaderIndex = 0;
    }

    @Override
    protected void _putByte(int offset, byte b) {
        checkFree();
        checkExtension(offset, 1);

        this.target[offset] = b;
    }

    @Override
    protected int _putBytes(int offset, byte[] src, int srcOffset, int srcLen) {
        checkFree();
        checkExtension(offset, srcLen);

        System.arraycopy(src, srcOffset, this.target, offset, srcLen);
        return srcLen;
    }

    @Override
    protected int _putBytes(int offset, ByteBuffer src, int srcLen) {
        checkFree();

        srcLen = Math.min(src.remaining(), srcLen);
        checkExtension(offset, srcLen);

        src.get(this.target, offset, srcLen);
        return srcLen;
    }

    @Override
    protected int _putBytes(int offset, BytesIO src, int srcLen) {
        checkFree();

        srcLen = Math.min(src.readableBytes(), srcLen);
        checkExtension(offset, srcLen);

        src.readBytes(this.target, offset, srcLen);
        return srcLen;
    }

    @Override
    protected byte _getByte(int offset) {
        checkFree();

        return this.target[offset];
    }

    @Override
    protected int _getBytes(int offset, byte[] dst, int dstOffset, int dstLen) {
        checkFree();

        System.arraycopy(this.target, offset, dst, dstOffset, dstLen);
        return dstLen;
    }

    @Override
    protected int _getBytes(int offset, ByteBuffer dst, int dstLen) {
        checkFree();

        dst.put(this.target, offset, dstLen);
        return dstLen;
    }

    @Override
    protected int _getBytes(int offset, BytesIO dst, int dstLen) {
        checkFree();

        dst.writeBytes(this.target, offset, dstLen);
        return dstLen;
    }

    @Override
    protected void _free() {
        this.target = null;
    }

    @Override
    public Object target() {
        return this.target;
    }

    @Override
    public int capacity() {
        return this.target.length;
    }

    @Override
    public AutoArrayBytesIO copy() {
        checkFree();

        byte[] copyArray = this.target.clone();
        AutoArrayBytesIO byteBuf = new AutoArrayBytesIO(this.getMaxCapacity(), this.extensionSize, copyArray, true);

        byteBuf.writerIndex = this.writerIndex;
        byteBuf.markedWriterIndex = this.markedWriterIndex;
        byteBuf.readerIndex = this.readerIndex;
        byteBuf.markedReaderIndex = this.markedReaderIndex;
        return byteBuf;
    }

    @Override
    protected String getSimpleName() {
        return "AutoArrayBytesIO";
    }
}