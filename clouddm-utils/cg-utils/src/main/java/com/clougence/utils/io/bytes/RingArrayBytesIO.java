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
 * 基于字节数组的环形 {@link BytesIO} 实现
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-11-01
 */
final class RingArrayBytesIO extends AbstractBytesIO {
    RingArrayBytesIO(byte[] initData, boolean asWrite) {
        super(initData.length);
        this.target = initData;

        if (!asWrite) {
            this.writerIndex = initData.length;
            this.markedWriterIndex = initData.length;
        }
    }

    // ------------------------------------------------------------------------

    byte[] target;

    @Override
    public BytesIO markReader() {
        if (this.markedReaderIndex != this.readerIndex) {
            this.markedReaderIndex = this.readerIndex;
            this.updateIndex();
        }
        return this;
    }

    private void updateIndex() {
        int capacity = this.target.length;
        if (this.markedReaderIndex >= capacity) {
            this.markedReaderIndex = this.markedReaderIndex - capacity;
            this.markedWriterIndex = this.markedWriterIndex - capacity;
            this.readerIndex = this.readerIndex - capacity;
            this.writerIndex = this.writerIndex - capacity;
        }
    }

    @Override
    protected void _putByte(int offset, byte b) {
        checkFree();

        int offsetSize = offset % this.getMaxCapacity();
        this.target[offsetSize] = b;
    }

    @Override
    protected int _putBytes(int offset, byte[] src, int srcOffset, int srcLen) {
        checkFree();

        int maxCap = this.getMaxCapacity();
        int offsetSize = offset % maxCap;

        if ((offsetSize + srcLen) < maxCap) {
            System.arraycopy(src, srcOffset, this.target, offsetSize, srcLen);
            return srcLen;
        } else {
            int partA = maxCap - offsetSize;
            int partB = srcLen - partA;

            System.arraycopy(src, srcOffset, this.target, offsetSize, partA);

            if (partB > 0) {
                System.arraycopy(src, srcOffset + partA, this.target, 0, partB);
                return partA + partB;
            } else {
                return partA;
            }
        }
    }

    @Override
    protected int _putBytes(int offset, ByteBuffer src, int srcLen) {
        checkFree();

        int maxCap = this.getMaxCapacity();
        int offsetSize = offset % maxCap;
        srcLen = Math.min(src.remaining(), srcLen);

        if ((offsetSize + srcLen) <= maxCap) {
            src.get(this.target, offsetSize, srcLen);
            return srcLen;
        } else {
            int partA = maxCap - offsetSize;
            int partB = srcLen - partA;

            src.get(this.target, offsetSize, partA);

            if (partB > 0) {
                src.get(this.target, 0, partB);
                return partA + partB;
            } else {
                return partA;
            }
        }
    }

    @Override
    protected int _putBytes(int offset, BytesIO src, int srcLen) {
        checkFree();

        int maxCap = this.getMaxCapacity();
        int offsetSize = offset % maxCap;
        srcLen = Math.min(src.readableBytes(), srcLen);

        if ((offsetSize + srcLen) <= maxCap) {
            src.readBytes(this.target, offsetSize, srcLen);
            return srcLen;
        } else {
            int partA = maxCap - offsetSize;
            int partB = srcLen - partA;

            src.readBytes(this.target, offsetSize, partA);

            if (partB > 0) {
                src.readBytes(this.target, 0, partB);
                return partA + partB;
            } else {
                return partA;
            }
        }
    }

    @Override
    protected byte _getByte(int offset) {
        checkFree();

        int offsetSize = offset % this.getMaxCapacity();
        return this.target[offsetSize];
    }

    @Override
    protected int _getBytes(int offset, byte[] dst, int dstOffset, int dstLen) {
        checkFree();

        int maxCap = this.getMaxCapacity();
        int offsetSize = offset % maxCap;

        if ((offsetSize + dstLen) < maxCap) {
            System.arraycopy(this.target, offsetSize, dst, dstOffset, dstLen);
            return dstLen;
        } else {
            int partA = maxCap - offsetSize;
            int partB = dstLen - partA;

            System.arraycopy(this.target, offsetSize, dst, dstOffset, partA);

            if (partB > 0) {
                System.arraycopy(this.target, 0, dst, dstOffset + partA, partB);
                return partA + partB;
            } else {
                return partA;
            }
        }
    }

    @Override
    protected int _getBytes(int offset, ByteBuffer dst, int dstLen) {
        checkFree();

        int maxCap = this.getMaxCapacity();
        int offsetSize = offset % maxCap;

        if ((offsetSize + dstLen) < maxCap) {
            dst.put(this.target, offsetSize, dstLen);
            return dstLen;
        } else {
            int partA = maxCap - offsetSize;
            int partB = dstLen - partA;

            dst.put(this.target, offsetSize, partA);

            if (partB > 0) {
                dst.put(this.target, 0, partB);
                return partA + partB;
            } else {
                return partA;
            }
        }
    }

    @Override
    protected int _getBytes(int offset, BytesIO dst, int dstLen) {
        checkFree();

        int maxCap = this.getMaxCapacity();
        int offsetSize = offset % maxCap;

        if ((offsetSize + dstLen) < maxCap) {
            dst.writeBytes(this.target, offsetSize, dstLen);
            return dstLen;
        } else {
            int partA = maxCap - offsetSize;
            int partB = dstLen - partA;

            dst.writeBytes(this.target, offsetSize, partA);
            if (partB > 0) {
                dst.writeBytes(this.target, 0, partB);
                return partA + partB;
            } else {
                return partA;
            }
        }
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
        return this.getMaxCapacity();
    }

    @Override
    public RingArrayBytesIO copy() {
        checkFree();

        byte[] copyArray = new byte[this.getMaxCapacity()];
        this._getBytes(this.markedReaderIndex, copyArray, 0, copyArray.length);
        RingArrayBytesIO byteBuf = new RingArrayBytesIO(copyArray, true);

        byteBuf.writerIndex = this.writerIndex;
        byteBuf.markedWriterIndex = this.markedWriterIndex;
        byteBuf.readerIndex = this.readerIndex;
        byteBuf.markedReaderIndex = this.markedReaderIndex;
        return byteBuf;
    }

    @Override
    protected String getSimpleName() {
        return "RingArrayBytesIO";
    }
}