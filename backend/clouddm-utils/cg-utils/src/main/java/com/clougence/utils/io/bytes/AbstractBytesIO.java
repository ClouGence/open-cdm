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

import com.clougence.utils.ObjectUtils;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * <pre>
 * +----------+-------------+------------+-------------+----------+
 * | ancient  | discardable | readable   | overlayable | writable |
 * +------------------------+------------+-------------+----------+
 * |          |             |            |             |          |
 * 0   ≤   marked  ≤  readerIndex  ≤  marked  ≤  writerIndex ≤ capacity
 *      readerIndex                writerIndex
 *
 * </pre>
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2022-11-01
 */
public abstract class AbstractBytesIO implements BytesIO, AutoCloseable {
    protected        int       markedReaderIndex;
    protected        int       markedWriterIndex;
    protected        int       readerIndex;
    protected        int       writerIndex;
    private final    int       maxCapacity;
    private volatile boolean   isFree;
    protected        ByteOrder byteOrder;

    protected AbstractBytesIO(int maxCapacity) {
        this.maxCapacity = maxCapacity == -1 ? Integer.MAX_VALUE : maxCapacity;
        this.markedReaderIndex = 0;
        this.markedWriterIndex = 0;
        this.readerIndex = 0;
        this.writerIndex = 0;
        this.isFree = false;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    protected final void checkFree() {
        if (this.isFree) {
            throw new IllegalStateException("has been released.");
        }
    }

    @Override
    public final boolean isFree() {
        return this.isFree;
    }

    @Override
    public void free() {
        if (this.isFree) {
            throw new IllegalStateException("has been released.");
        }

        this.isFree = true;
        this._free();
    }

    @Override
    public void close() throws IOException {
        this.free();
    }

    @Override
    public ByteOrder order() {
        return this.byteOrder;
    }

    @Override
    public BytesIO order(ByteOrder newOrder) {
        this.byteOrder = newOrder;
        return this;
    }

    private boolean isBig() {
        return this.byteOrder == ByteOrder.BIG_ENDIAN;
    }

    protected abstract void _putByte(int offset, byte b);

    protected abstract int _putBytes(int offset, byte[] src, int srcOffset, int srcLen);

    protected abstract int _putBytes(int offset, ByteBuffer src, int srcLen);

    protected abstract int _putBytes(int offset, BytesIO src, int srcLen);

    protected abstract byte _getByte(int offset);

    protected abstract int _getBytes(int offset, byte[] dst, int dstOffset, int dstLen);

    protected abstract int _getBytes(int offset, ByteBuffer dst, int dstLen);

    protected abstract int _getBytes(int offset, BytesIO dst, int dstLen);

    protected abstract void _free();

    @Override
    public int readableBytes() {
        return this.markedWriterIndex - this.readerIndex;
    }

    @Override
    public int readBytes() {
        return this.readerIndex - this.markedReaderIndex;
    }

    @Override
    public int writableBytes() {
        return this.maxCapacity - (this.writerIndex - this.markedReaderIndex);
    }

    @Override
    public int writtenBytes() {
        return this.writerIndex - this.markedWriterIndex;
    }

    /** move writerIndex and returns the writerIndex before changed. */
    protected int nextWritable(int writableBytes) {
        if (writableBytes > this.writableBytes()) {
            throw new BufferOverflowException();
        }

        int oriWriterIndex = this.writerIndex;
        this.writerIndex += writableBytes;
        return oriWriterIndex;
    }

    /** move readerIndex and returns the readerIndex before changed. */
    protected int nextReadable(int readableBytes) {
        if ((this.readerIndex + readableBytes) > this.markedWriterIndex) {
            throw new IndexOutOfBoundsException(String.format("read out of range. length: %d (expected: 0 ~ %d)", readableBytes, this.readableBytes()));
        }

        int oriReadIndex = this.readerIndex;
        this.readerIndex += readableBytes;
        return oriReadIndex;
    }

    protected int offsetWritable(int offset, int writableBytes) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        int offerWritableBytes = this.maxCapacity - (this.markedWriterIndex - this.markedReaderIndex);
        if (writableBytes > offerWritableBytes) {
            throw new IndexOutOfBoundsException(String.format("write out of range. index: %d, length: %d (expected: 0 ~ %d)", offset, writableBytes, offerWritableBytes));
        }

        return this.markedWriterIndex + offset;
    }

    protected int offsetReadable(int offset, int readableBytes) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        if ((this.readerIndex + offset + readableBytes) > this.markedWriterIndex) {
            int expectLimit = this.markedWriterIndex - this.readerIndex;
            throw new IndexOutOfBoundsException(String.format("read out of range. index: %d, length: %d (expected: 0 ~ %d)", offset, readableBytes, expectLimit));
        }

        return this.readerIndex + offset;
    }

    @Override
    public BytesIO skipReadableBytes(int length) {
        nextReadable(length);
        return this;
    }

    @Override
    public BytesIO skipWritableBytes(int length) {
        nextWritable(length);
        return this;
    }

    @Override
    public BytesIO markReader() {
        if (this.markedReaderIndex != this.readerIndex) {
            this.markedReaderIndex = this.readerIndex;
        }
        return this;
    }

    @Override
    public BytesIO resetReader() {
        this.readerIndex = this.markedReaderIndex;
        return this;
    }

    @Override
    public int readerIndex() {
        return this.readerIndex;
    }

    @Override
    public BytesIO markWriter() {
        if (this.markedWriterIndex != this.writerIndex) {
            this.markedWriterIndex = this.writerIndex;
        }
        return this;
    }

    @Override
    public BytesIO resetWriter() {
        this.writerIndex = this.markedWriterIndex;
        return this;
    }

    @Override
    public int writerIndex() {
        return this.writerIndex;
    }

    //    @Override
    //    public ByteBuf asReadOnly() {
    //        return new ReadOnlyByteBuf(this);
    //    }

    @Override
    public void writeByte(byte n) {
        this._putByte(nextWritable(1), n);
    }

    @Override
    public int writeBytes(byte[] src, int off, int len) {
        ObjectUtils.checkPositiveOrZero(len, "len");

        int minLen = Math.min(len, this.writableBytes());
        return this._putBytes(nextWritable(minLen), src, off, minLen);
    }

    @Override
    public void writeInt16(short n) {
        Bits.encodeInt16(this, nextWritable(2), n, isBig());
    }

    @Override
    public void writeInt24(int n) {
        Bits.encodeInt24(this, nextWritable(3), n, isBig());
    }

    @Override
    public void writeInt32(int n) {
        Bits.encodeInt32(this, nextWritable(4), n, isBig());
    }

    @Override
    public void writeUInt32(long n) {
        Bits.encodeInt32(this, nextWritable(4), n, isBig());
    }

    @Override
    public void writeInt64(long n) {
        Bits.encodeInt64(this, nextWritable(8), n, isBig());
    }

    @Override
    public void writeFloat32(float n) {
        this.writeInt32(Float.floatToRawIntBits(n));
    }

    @Override
    public void writeFloat64(double n) {
        this.writeInt64(Double.doubleToRawLongBits(n));
    }

    @Override
    public int writeBuffer(ByteBuffer src, int len) {
        ObjectUtils.checkPositiveOrZero(len, "len");

        int minLen = Math.min(len, this.writableBytes());
        return this._putBytes(nextWritable(minLen), src, minLen);
    }

    @Override
    public int writeBuffer(BytesIO src, int len) {
        ObjectUtils.checkPositiveOrZero(len, "len");

        int minLen = Math.min(len, this.writableBytes());
        return this._putBytes(nextWritable(minLen), src, minLen);
    }

    @Override
    public void setByte(int offset, byte n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        this._putByte(offsetWritable(offset, 1), n);
    }

    @Override
    public void setBytes(int offset, byte[] src) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        this._putBytes(offsetWritable(offset, src.length), src, 0, src.length);
    }

    @Override
    public void setBytes(int offset, byte[] src, int srcOffset, int srcLen) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        ObjectUtils.checkPositiveOrZero(srcOffset, "srcOffset");
        ObjectUtils.checkPositiveOrZero(srcLen, "srcLen");

        this._putBytes(offsetWritable(offset, srcLen), src, srcOffset, srcLen);
    }

    @Override
    public void setInt16(int offset, short n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        Bits.encodeInt16(this, offsetWritable(offset, 2), n, isBig());
    }

    @Override
    public void setInt24(int offset, int n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        Bits.encodeInt24(this, offsetWritable(offset, 3), n, isBig());
    }

    @Override
    public void setInt32(int offset, int n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        Bits.encodeInt32(this, offsetWritable(offset, 4), n, isBig());
    }

    @Override
    public void setInt64(int offset, long n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        Bits.encodeInt64(this, offsetWritable(offset, 8), n, isBig());
    }

    @Override
    public void setFloat32(int offset, float n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        this.setInt32(offset, Float.floatToRawIntBits(n));
    }

    @Override
    public void setFloat64(int offset, double n) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        this.setInt64(offset, Double.doubleToRawLongBits(n));
    }

    @Override
    public int setBuffer(int offset, ByteBuffer src, int srcLen) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        ObjectUtils.checkPositiveOrZero(srcLen, "srcLen");

        return this._putBytes(offsetWritable(offset, srcLen), src, srcLen);
    }

    @Override
    public int setBuffer(int offset, BytesIO src, int srcLen) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        ObjectUtils.checkPositiveOrZero(srcLen, "srcLen");

        return this._putBytes(offsetWritable(offset, srcLen), src, srcLen);
    }

    @Override
    public byte readByte() {
        return this._getByte(nextReadable(1));
    }

    @Override
    public int readBytes(byte[] dst, int off, int len) {
        ObjectUtils.checkPositiveOrZero(off, "off");
        ObjectUtils.checkPositiveOrZero(len, "len");

        int minLen = Math.min(len, this.readableBytes());
        return this._getBytes(nextReadable(minLen), dst, off, minLen);
    }

    @Override
    public short readInt16() {
        return Bits.dencodeInt16(this, nextReadable(2), isBig());
    }

    @Override
    public int readInt24() {
        return Bits.dencodeInt24(this, nextReadable(3), isBig());
    }

    @Override
    public int readInt32() {
        return Bits.dencodeInt32(this, nextReadable(4), isBig());
    }

    @Override
    public long readInt64() {
        return Bits.dencodeInt64(this, nextReadable(8), isBig());
    }

    @Override
    public float readFloat32() {
        return Float.intBitsToFloat(readInt32());
    }

    @Override
    public double readFloat64() {
        return Double.longBitsToDouble(readInt64());
    }

    @Override
    public int readBuffer(ByteBuffer dst, int len) {
        ObjectUtils.checkPositiveOrZero(len, "len");

        int minLen = Math.min(len, this.readableBytes());
        return this._getBytes(nextReadable(minLen), dst, minLen);
    }

    @Override
    public int readBuffer(BytesIO dst, int len) {
        ObjectUtils.checkPositiveOrZero(len, "len");

        int minLen = Math.min(len, this.readableBytes());
        return this._getBytes(nextReadable(minLen), dst, minLen);
    }

    @Override
    public byte getByte(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return this._getByte(offsetReadable(offset, 1));
    }

    @Override
    public int getBytes(int offset, byte[] dst, int dstOffset, int dstLen) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        ObjectUtils.checkPositiveOrZero(dstOffset, "dstOffset");
        ObjectUtils.checkPositiveOrZero(dstLen, "dstLen");

        return this._getBytes(offsetReadable(offset, dstLen), dst, dstOffset, dstLen);
    }

    @Override
    public short getInt16(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeInt16(this, offsetReadable(offset, 2), isBig());
    }

    @Override
    public int getInt24(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeInt24(this, offsetReadable(offset, 3), isBig());
    }

    @Override
    public int getInt32(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeInt32(this, offsetReadable(offset, 4), isBig());
    }

    @Override
    public long getInt64(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeInt64(this, offsetReadable(offset, 8), isBig());
    }

    @Override
    public float getFloat32(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Float.intBitsToFloat(getInt32(offset));
    }

    @Override
    public double getFloat64(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Double.longBitsToDouble(getInt64(offset));
    }

    @Override
    public int getBuffer(int offset, ByteBuffer dst, int dstLen) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        ObjectUtils.checkPositiveOrZero(dstLen, "dstLen");

        return this._getBytes(offsetReadable(offset, dstLen), dst, dstLen);
    }

    @Override
    public int getBuffer(int offset, BytesIO dst, int dstLen) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");
        ObjectUtils.checkPositiveOrZero(dstLen, "dstLen");

        return this._getBytes(offsetReadable(offset, dstLen), dst, dstLen);
    }

    @Override
    public short readUInt8() {
        return Bits.dencodeUInt8(this, nextReadable(1));
    }

    @Override
    public int readUInt16() {
        return Bits.dencodeUInt16(this, nextReadable(2), isBig());
    }

    @Override
    public int readUInt24() {
        return Bits.dencodeUInt24(this, nextReadable(3), isBig());
    }

    @Override
    public long readUInt32() {
        return Bits.dencodeUInt32(this, nextReadable(4), isBig());
    }

    @Override
    public short getUInt8(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeUInt8(this, offsetReadable(offset, 1));
    }

    @Override
    public int getUInt16(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeUInt16(this, offsetReadable(offset, 2), isBig());
    }

    @Override
    public int getUInt24(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeUInt24(this, offsetReadable(offset, 3), isBig());
    }

    @Override
    public long getUInt32(int offset) {
        ObjectUtils.checkPositiveOrZero(offset, "offset");

        return Bits.dencodeUInt32(this, offsetReadable(offset, 4), isBig());
    }

    @Override
    public byte[] asByteArray() {
        checkFree();

        byte[] copyArray = new byte[this.markedWriterIndex - this.markedReaderIndex];
        this._getBytes(this.markedReaderIndex, copyArray, 0, copyArray.length);
        return copyArray;
    }

    @Override
    public String toString() {
        return getSimpleName() + "[rMark=" + this.markedReaderIndex //
                + " -> rIndex=" + this.readerIndex //
                + " -> wMark=" + this.markedWriterIndex //
                + " -> wIndex=" + this.writerIndex //
                + ", capacity=" + this.capacity() + "]";
    }

    protected abstract String getSimpleName();
}
