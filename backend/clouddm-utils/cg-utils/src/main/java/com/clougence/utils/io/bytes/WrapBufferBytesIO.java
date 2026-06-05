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
package com.clougence.utils.io.bytes;

import java.nio.ByteBuffer;

/**
 * <pre>
 * +-------------+------------+-------------+----------+
 * | discardable | readable   | overlayable | writable |
 * +-------------+------------+-------------+----------+
 * |             |            |             |          |
 * 0   ≤   readerIndex  ≤  marked  ≤  writerIndex ≤ capacity
 *                      writerIndex
 * </pre>
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-11-01
 */
class WrapBufferBytesIO extends AbstractBytesIO {
    WrapBufferBytesIO(ByteBuffer initData, boolean asWrite) {
        super(initData.limit());
        this.target = initData;

        if (!asWrite) {
            this.writerIndex = initData.limit();
            this.markedWriterIndex = initData.limit();
        }
    }
    // ------------------------------------------------------------------------

    ByteBuffer target;

    @Override
    public int writableBytes() {
        return this.getMaxCapacity() - this.writerIndex;
    }

    @Override
    protected void _putByte(int offset, byte b) {
        checkFree();

        this.target.clear();
        this.target.put(offset, b);
    }

    @Override
    protected int _putBytes(int offset, byte[] src, int srcOffset, int srcLen) {
        checkFree();

        this.target.clear().position(offset);
        this.target.put(src, srcOffset, srcLen);
        return srcLen;
    }

    @Override
    protected int _putBytes(int offset, ByteBuffer src, int srcLen) {
        checkFree();

        this.target.clear().position(offset);
        srcLen = Math.min(src.remaining(), srcLen);

        this.target.put((ByteBuffer) src.duplicate().limit(src.position() + srcLen));
        src.position(src.position() + srcLen);
        return srcLen;
    }

    @Override
    protected int _putBytes(int offset, BytesIO src, int srcLen) {
        checkFree();

        this.target.clear().position(offset);
        srcLen = Math.min(src.readableBytes(), srcLen);

        src.readBuffer(this.target, srcLen);
        return srcLen;
    }

    @Override
    protected byte _getByte(int offset) {
        checkFree();

        this.target.clear();
        return this.target.get(offset);
    }

    @Override
    protected int _getBytes(int offset, byte[] dst, int dstOffset, int dstLen) {
        checkFree();

        this.target.clear().position(offset);
        this.target.get(dst, dstOffset, dstLen);
        return dstLen;
    }

    @Override
    protected int _getBytes(int offset, ByteBuffer dst, int dstLen) {
        checkFree();

        this.target.clear().position(offset).limit(offset + dstLen);
        dst.put(this.target);
        return dstLen;
    }

    @Override
    protected int _getBytes(int offset, BytesIO dst, int dstLen) {
        checkFree();

        this.target.clear().position(offset);
        dst.writeBuffer(this.target, dstLen);
        return dstLen;
    }

    @Override
    protected void _free() {
        if (this.target.isDirect()) {
            throw new IllegalStateException("Direct ByteBuffer can't be free, please use the target() method to manually release.");
        } else {
            this.target = null;
        }
    }

    @Override
    public Object target() {
        return this.target;
    }

    @Override
    public int capacity() {
        return this.getMaxCapacity();
    }

    @Override
    public WrapBufferBytesIO copy() {
        checkFree();

        ByteBuffer copyBuffer;
        if (this.target.isDirect()) {
            copyBuffer = ByteBuffer.allocateDirect(this.getMaxCapacity());
        } else {
            copyBuffer = ByteBuffer.allocate(this.getMaxCapacity());
        }

        this.target.clear();
        copyBuffer.put(this.target);
        WrapBufferBytesIO byteBuf = new WrapBufferBytesIO(copyBuffer, true);

        byteBuf.writerIndex = this.writerIndex;
        byteBuf.markedWriterIndex = this.markedWriterIndex;
        byteBuf.readerIndex = this.readerIndex;
        byteBuf.markedReaderIndex = this.markedReaderIndex;
        return byteBuf;
    }

    @Override
    protected String getSimpleName() {
        return "WrapBufferBytesIO";
    }
}