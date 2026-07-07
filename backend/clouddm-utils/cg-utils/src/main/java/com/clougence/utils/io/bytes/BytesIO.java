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

import com.clougence.utils.ArrayUtils;
import com.clougence.utils.ObjectUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <pre>
 * +----------+-------------+------------+-------------+----------+
 * | ancient  | discardable | readable   | overlayable | writable |
 * +------------------------+------------+-------------+----------+
 * |          |             |            |             |          |
 * 0   ≤   marked  ≤  readerIndex  ≤  marked  ≤  writerIndex ≤ capacity
 *      readerIndex                writerIndex
 * </pre>
 */
public interface BytesIO extends ByteChannel {

    BytesIO EMPTY = BytesIO.wrap(ArrayUtils.EMPTY_BYTE_ARRAY);

    static BytesIO wrap(byte[] bytes) {
        return wrap(bytes, false);
    }

    static BytesIO wrap(byte[] bytes, boolean asWrite) {
        Objects.requireNonNull(bytes, "bytes is null.");
        return new WrapArrayBytesIO(bytes, asWrite);
    }

    static BytesIO wrap(ByteBuffer buffer) {
        return wrap(buffer, false);
    }

    static BytesIO wrap(ByteBuffer buffer, boolean asWrite) {
        Objects.requireNonNull(buffer, "buffer is null.");
        return new WrapBufferBytesIO(buffer, asWrite);
    }

    static BytesIO fixed(int capacity) {
        ObjectUtils.checkPositiveOrZero(capacity, "capacity");
        return new AutoArrayBytesIO(capacity, 256, new byte[capacity], true);
    }

    static BytesIO auto() {
        return new AutoArrayBytesIO(Integer.MAX_VALUE, 256, new byte[64], true);
    }

    static BytesIO auto(int max) {
        ObjectUtils.checkPositiveOrZero(max, "max");
        return new AutoArrayBytesIO(max, 256, new byte[Math.min(64, max)], true);
    }

    static BytesIO auto(int init, int max) {
        return auto(init, Math.min(init, 64), max);
    }

    static BytesIO auto(int init, int incr, int max) {
        ObjectUtils.checkPositiveOrZero(init, "init");
        ObjectUtils.checkPositiveOrZero(incr, "incr");
        ObjectUtils.checkPositiveOrZero(max, "max");

        if (init > max) {
            throw new IllegalArgumentException("init > max");
        } else {
            return new AutoArrayBytesIO(max, incr, new byte[init], true);
        }
    }

    static BytesIO auto(byte[] init, boolean asWrite) {
        Objects.requireNonNull(init, "initBytes is null.");
        return new AutoArrayBytesIO(Integer.MAX_VALUE, 1024, init, asWrite);
    }

    static BytesIO auto(byte[] init, int incr, int max, boolean asWrite) {
        Objects.requireNonNull(init, "initBytes is null.");
        ObjectUtils.checkPositiveOrZero(incr, "incr");
        ObjectUtils.checkPositiveOrZero(max, "max");

        if (init.length > max) {
            throw new IllegalArgumentException("init > max");
        } else {
            return new AutoArrayBytesIO(max, incr, init, asWrite);
        }
    }

    static BytesIO ring(int capacity) {
        ObjectUtils.checkPositiveOrZero(capacity, "capacity");
        return new RingArrayBytesIO(new byte[capacity], true);
    }

    static BytesIO ring(byte[] bytes, boolean asWrite) {
        Objects.requireNonNull(bytes, "bytes is null.");
        return new RingArrayBytesIO(bytes, asWrite);
    }

    /** Returns the target of this buffer. */
    Object target();

    /** Returns the {@code readerIndex} of this buffer. */
    int readerIndex();

    /** Returns the {@code writerIndex} of this buffer. */
    int writerIndex();

    /** Maximum capacity. */
    int capacity();

    /** Returns this buffer as a byte array. */
    byte[] asByteArray();

    /** Copies this buffer, including buffer data. */
    BytesIO copy();

    /** Byte order. */
    ByteOrder order();

    /** Sets the byte order. */
    BytesIO order(ByteOrder newOrder);

    /** Releases memory occupied by this buffer. */
    void free();

    default void close() throws IOException {
        this.free();
    }

    /** Returns whether this buffer has been freed. */
    boolean isFree();

    /**
     * Returns the number of readable bytes which is equal to
     * {@code (this.markedWriterIndex - this.readerIndex)}.
     */
    int readableBytes();

    /**
     * Returns the number of read bytes which is equal to
     * {@code (readerIndex - markedReaderIndex)}.
     */
    int readBytes();

    /**
     * Returns the number of writable bytes which is equal to
     * {@code (maxCapacity - (writerIndex - markedReaderIndex))}.
     */
    int writableBytes();

    /**
     * Returns the number of written bytes which is equal to
     * {@code (writerIndex - markedWriterIndex)}.
     */
    int writtenBytes();

    /**
     * Marks the current {@code readerIndex} in this buffer.
     * You can reposition the current {@code readerIndex} to the marked {@code readerIndex} by calling {@link #resetReader()}.
     * The initial value of the marked {@code readerIndex} is {@code 0}.
     */
    BytesIO markReader();

    /**
     * Marks the current {@code writerIndex} in this buffer.
     * You can reposition the current {@code writerIndex} to the marked {@code writerIndex} by calling {@link #resetWriter()}.
     * The initial value of the marked {@code writerIndex} is {@code 0}.
     */
    BytesIO markWriter();

    /** Same as markWriter() and markReader(). */
    default BytesIO flush() throws IOException {
        this.markWriter();
        this.markReader();
        return this;
    }

    /** Resets markWriter and skips all readable data. */
    default void clear() {
        this.resetWriter();
        this.skipReadableBytes(this.readableBytes());
        this.markReader();
    }

    /**
     * Repositions the current {@code readerIndex} to the marked
     * {@code readerIndex} in this buffer.
     * @throws IndexOutOfBoundsException if the current {@code writerIndex} is less than the marked {@code readerIndex}
     */
    BytesIO resetReader();

    /**
     * Repositions the current {@code writerIndex} to the marked
     * {@code writerIndex} in this buffer.
     * @throws IndexOutOfBoundsException if the current {@code readerIndex} is greater than the marked {@code writerIndex}
     */
    BytesIO resetWriter();

    BytesIO skipReadableBytes(int length);

    BytesIO skipWritableBytes(int length);

    /**
     * Writes one byte and increments {@code writerIndex} by 1.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has no writable byte.
     */
    void writeByte(byte n);

    /**
     * Writes bytes from {@code src} and increments {@code writerIndex} by the number of bytes written.
     * If the source is larger than the writable space, only the writable portion is written.
     */
    default int writeBytes(byte[] src) {
        return this.writeBytes(src, 0, src.length);
    }

    /**
     * Writes up to {@code len} bytes from {@code src} and increments {@code writerIndex} by the number of bytes written.
     * If {@code len} is larger than the writable space, only the writable portion is written.
     */
    int writeBytes(byte[] src, int off, int len);

    /**
     * Writes a 2-byte short and increments {@code writerIndex} by 2.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 2 writable bytes.
     */
    void writeInt16(short n);

    /**
     * Writes a 3-byte int and increments {@code writerIndex} by 3.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 3 writable bytes.
     */
    void writeInt24(int n);

    /**
     * Writes a 4-byte int and increments {@code writerIndex} by 4.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 4 writable bytes.
     */
    void writeInt32(int n);

    /**
     * Writes a 4-byte unsigned int and increments {@code writerIndex} by 4.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 4 writable bytes.
     */
    void writeUInt32(long n);

    /**
     * Writes an 8-byte long and increments {@code writerIndex} by 8.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 8 writable bytes.
     */
    void writeInt64(long n);

    /**
     * Writes a 4-byte float and increments {@code writerIndex} by 4.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 4 writable bytes.
     */
    void writeFloat32(float n);

    /**
     * Writes an 8-byte double and increments {@code writerIndex} by 8.
     * Throws {@link java.nio.BufferOverflowException} if the buffer has fewer than 8 writable bytes.
     */
    void writeFloat64(double n);

    default int writeBuffer(ByteBuffer src) {
        return this.writeBuffer(src, src.remaining());
    }

    int writeBuffer(ByteBuffer src, int len);

    default int writeBuffer(BytesIO src) {
        return this.writeBuffer(src, src.readableBytes());
    }

    int writeBuffer(BytesIO src, int len);

    /**
     * Converts {@code string} with {@code string.getBytes(charset)}, writes as many bytes as the buffer accepts, and returns the encoded byte length.
     */
    default int writeString(String string, Charset charset) {
        if (string != null && !string.equals("")) {
            byte[] bytes = string.getBytes(charset);
            writeBytes(bytes);
            return bytes.length;
        } else {
            return 0;
        }
    }

    /**
     * Overwrites one byte at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setByte(int offset, byte n);

    /**
     * Overwrites bytes from {@code src} at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setBytes(int offset, byte[] src);

    /**
     * Overwrites bytes from {@code src} at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setBytes(int offset, byte[] src, int srcOffset, int srcLen);

    /**
     * Overwrites a 2-byte short at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setInt16(int offset, short n);

    /**
     * Overwrites a 3-byte int at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setInt24(int offset, int n);

    /**
     * Overwrites a 4-byte int at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setInt32(int offset, int n);

    /**
     * Overwrites an 8-byte long at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setInt64(int offset, long n);

    /**
     * Overwrites a 4-byte float at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setFloat32(int offset, float n);

    /**
     * Overwrites an 8-byte double at {@code offset}; this method does not update {@code writerIndex}.
     * Throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    void setFloat64(int offset, double n);

    default int setBuffer(int offset, ByteBuffer src) {
        return this.setBuffer(offset, src, src.remaining());
    }

    int setBuffer(int offset, ByteBuffer src, int srcLen);

    default int setBuffer(int offset, BytesIO src) {
        return this.setBuffer(offset, src, src.readableBytes());
    }

    int setBuffer(int offset, BytesIO src, int srcLen);

    /**
     * Overwrites bytes from {@code string.getBytes(charset)} at {@code offset}; this method does not update {@code writerIndex}.
     * Returns the number of bytes written, and throws {@link IndexOutOfBoundsException} if the write exceeds the writable range.
     */
    default int setString(int offset, String string, Charset charset) {
        if (string != null && !string.equals("")) {
            byte[] bytes = string.getBytes(charset);
            setBytes(offset, bytes);
            return bytes.length;
        } else {
            return 0;
        }
    }

    /**
     * Reads one byte and increments {@code readerIndex} by 1.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 1 readable byte.
     */
    byte readByte();

    /** Reads bytes into {@code dst} and returns the number of bytes actually read. */
    default int readBytes(byte[] dst) {
        return this.readBytes(dst, 0, dst.length);
    }

    /** Reads up to {@code len} bytes into {@code dst} starting at {@code off}, and returns the number of bytes actually read. */
    int readBytes(byte[] dst, int off, int len);

    /**
     * Reads a 2-byte short and increments {@code readerIndex} by 2.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 2 readable bytes.
     */
    short readInt16();

    /**
     * Reads a 3-byte int and increments {@code readerIndex} by 3.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 3 readable bytes.
     */
    int readInt24();

    /**
     * Reads a 4-byte int and increments {@code readerIndex} by 4.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 4 readable bytes.
     */
    int readInt32();

    /**
     * Reads an 8-byte long and increments {@code readerIndex} by 8.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 8 readable bytes.
     */
    long readInt64();

    /**
     * Reads a 4-byte float and increments {@code readerIndex} by 4.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 4 readable bytes.
     */
    float readFloat32();

    /**
     * Reads an 8-byte double and increments {@code readerIndex} by 8.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 8 readable bytes.
     */
    double readFloat64();

    /** Copies readable bytes to {@code dst}. */
    default int readBuffer(ByteBuffer dst) {
        return this.readBuffer(dst, Math.min(dst.remaining(), this.readableBytes()));
    }

    /** Copies up to {@code len} readable bytes to {@code dst}. */
    int readBuffer(ByteBuffer dst, int len);

    /** Copies readable bytes to {@code dst}. */
    default int readBuffer(BytesIO dst) {
        return this.readBuffer(dst, Math.min(dst.writableBytes(), this.readableBytes()));
    }

    /** Copies up to {@code len} readable bytes to {@code dst}. */
    int readBuffer(BytesIO dst, int len);

    /**
     * Reads up to {@code len} bytes, converts them to a String, and increments {@code readerIndex} by the number of bytes read.
     */
    default String readString(int len, Charset charset) {
        if (len == 0) {
            return "";
        }

        byte[] b = new byte[len];
        int readBytes = this.readBytes(b);
        if (charset == StandardCharsets.US_ASCII) {
            return new String(b, 0, readBytes);
        } else {
            return new String(b, 0, readBytes, charset);
        }
    }

    /**
     * Reads one byte starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 1} exceeds the readable range.
     */
    byte getByte(int offset);

    /** Reads bytes starting at {@code offset} into {@code dst}; this method does not update {@code readerIndex}. */
    default int getBytes(int offset, byte[] dst) {
        return getBytes(offset, dst, 0, dst.length);
    }

    /** Reads {@code dstLen} bytes starting at {@code offset} into {@code dst} at {@code dstOffset}; this method does not update {@code readerIndex}. */
    int getBytes(int offset, byte[] dst, int dstOffset, int dstLen);

    /**
     * Reads a 2-byte short starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 2} exceeds the readable range.
     */
    short getInt16(int offset);

    /**
     * Reads a 3-byte int starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 3} exceeds the readable range.
     */
    int getInt24(int offset);

    /**
     * Reads a 4-byte int starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 4} exceeds the readable range.
     */
    int getInt32(int offset);

    /**
     * Reads an 8-byte long starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 8} exceeds the readable range.
     */
    long getInt64(int offset);

    /**
     * Reads a 4-byte float starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 4} exceeds the readable range.
     */
    float getFloat32(int offset);

    /**
     * Reads an 8-byte double starting at {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 8} exceeds the readable range.
     */
    double getFloat64(int offset);

    /** Copies readable bytes from {@code offset} to {@code dst}; this method does not update {@code readerIndex}. */
    default int getBuffer(int offset, ByteBuffer dst) {
        return this.getBuffer(offset, dst, Math.min(dst.remaining(), this.readableBytes()));
    }

    /** Copies {@code dstLen} bytes from {@code offset} to {@code dst}; this method does not update {@code readerIndex}. */
    int getBuffer(int offset, ByteBuffer dst, int dstLen);

    /** Copies readable bytes from {@code offset} to {@code dst}; this method does not update {@code readerIndex}. */
    default int getBuffer(int offset, BytesIO dst) {
        return this.getBuffer(offset, dst, Math.min(dst.writableBytes(), this.readableBytes()));
    }

    /** Copies {@code dstLen} bytes from {@code offset} to {@code dst}; this method does not update {@code readerIndex}. */
    int getBuffer(int offset, BytesIO dst, int dstLen);

    /**
     * Reads {@code len} bytes from {@code offset} and constructs a String; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + len} exceeds the readable range.
     */
    default String getString(int offset, int len, Charset charset) {
        if (len == 0) {
            return "";
        }

        byte[] b = new byte[len];
        int readBytes = this.getBytes(offset, b);
        if (charset == StandardCharsets.US_ASCII) {
            return new String(b, 0, readBytes);
        } else {
            return new String(b, 0, readBytes, charset);
        }
    }

    /**
     * Reads one unsigned byte, returns a value between 0 and 255, and increments {@code readerIndex} by 1.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 1 readable byte.
     */
    short readUInt8();

    /**
     * Reads a 2-byte unsigned short and increments {@code readerIndex} by 2.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 2 readable bytes.
     */
    int readUInt16();

    /**
     * Reads a 3-byte unsigned int and increments {@code readerIndex} by 3.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 3 readable bytes.
     */
    int readUInt24();

    /**
     * Reads a 4-byte unsigned int and increments {@code readerIndex} by 4.
     * Throws {@link IndexOutOfBoundsException} if the buffer has fewer than 4 readable bytes.
     */
    long readUInt32();

    /**
     * Reads one unsigned byte from {@code offset} and returns a value between 0 and 255; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 1} exceeds the readable range.
     */
    short getUInt8(int offset);

    /**
     * Reads a 2-byte unsigned short from {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 2} exceeds the readable range.
     */
    int getUInt16(int offset);

    /**
     * Reads a 3-byte unsigned int from {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 3} exceeds the readable range.
     */
    int getUInt24(int offset);

    /**
     * Reads a 4-byte unsigned int from {@code offset}; this method does not update {@code readerIndex}.
     * Throws {@link IndexOutOfBoundsException} if {@code offset + 4} exceeds the readable range.
     */
    long getUInt32(int offset);

    /** Finds the next occurrence of {@code expect} using the specified encoding; this method does not update {@code readerIndex}. Returns -1 if not found. */
    default int expect(String expect, Charset charset) {
        int len = expect.getBytes(charset).length;
        int readableBytes = this.readableBytes();

        if (readableBytes >= len) {
            int loopCount = readableBytes - len;
            for (int i = 0; i <= loopCount; i++) {
                String dat = this.getString(i, len, charset);
                if (dat.equals(expect)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /** Finds the next line ending without updating {@code readerIndex}. Returns -1 if no line ending exists. */
    default int expectLine() {
        int available = this.readableBytes();
        if (available == 0) {
            return -1;
        }

        int findIndex = -1;
        for (int i = 0; i < available; i++) {
            if (this.getUInt8(i) == '\n') {
                if (i > 0 && this.getUInt8(i - 1) == '\r') {
                    findIndex = i - 1;
                } else {
                    findIndex = i;
                }
                break;
            }
        }

        return findIndex;
    }

    /** Returns whether a line ending is available. */
    default boolean hasLine() {
        return expectLine() >= 0;
    }

    /** Read a whole line. */
    default String readLine() {
        return this.readLine(StandardCharsets.US_ASCII);
    }

    /** Read a whole line. */
    default String readLine(Charset charset) {
        int available = this.readableBytes();
        if (available == 0) {
            return null;
        }

        int findIndex = -1;
        int skipLength = -1;
        for (int i = 0; i < available; i++) {
            if (this.getUInt8(i) == '\n') {
                if (i > 0 && this.getUInt8(i - 1) == '\r') {
                    findIndex = i - 1;
                    skipLength = 2;
                } else {
                    findIndex = i;
                    skipLength = 1;
                }
                break;
            }
        }

        if (findIndex >= 0) {
            String str = this.readString(findIndex, charset);
            this.skipReadableBytes(skipLength);
            return str;
        } else {
            return null;
        }
    }

    /** Finds the next occurrence of {@code expect} using the specified encoding; this method does not update {@code readerIndex}. Returns -1 if not found. */
    default int expect(char expect, Charset charset) {
        return expect(String.valueOf(expect), charset);
    }

    /**
     * Reads from the current position through the first occurrence of {@code expect}.
     * Returns null if {@code expect} is not found.
     */
    default String readExpect(String expect, Charset charset) {
        int readLen;
        if ((readLen = this.expect(expect, charset)) >= 0) {
            String str = readString(readLen, charset);
            this.skipReadableBytes(expect.getBytes(charset).length);
            return str;
        } else {
            return null;
        }
    }

    /**
     * Reads from the current position through the first occurrence of {@code expect}.
     * Returns null if {@code expect} is not found.
     */
    default String readExpect(char expect, Charset charset) {
        return readExpect(String.valueOf(expect), charset);
    }

    /** Finds the last occurrence of {@code expect} using the specified encoding; this method does not update {@code readerIndex}. Returns -1 if not found. */
    default int expectLast(String expect, Charset charset) {
        int len = expect.getBytes(charset).length;
        int readableBytes = this.readableBytes();

        if (readableBytes >= len) {
            int loopCount = readableBytes - len;
            for (int i = loopCount; i >= 0; i--) {
                String dat = this.getString(i, len, charset);
                if (dat.equals(expect)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /** Finds the last occurrence of {@code expect} using the specified encoding; this method does not update {@code readerIndex}. Returns -1 if not found. */
    default int expectLast(char expect, Charset charset) {
        return expectLast(String.valueOf(expect), charset);
    }

    /**
     * Reads from the current position through the last occurrence of {@code expect}.
     * Returns null if {@code expect} is not found.
     */
    default String readExpectLast(String expect, Charset charset) {
        int readLen = -1;
        if ((readLen = this.expectLast(expect, charset)) >= 0) {
            String str = readString(readLen, charset);
            this.skipReadableBytes(expect.getBytes(charset).length);
            return str;
        } else {
            return null;
        }
    }

    /**
     * Reads from the current position through the last occurrence of {@code expect}.
     * Returns null if {@code expect} is not found.
     */
    default String readExpectLast(char expect, Charset charset) {
        return readExpectLast(String.valueOf(expect), charset);
    }

    /** Implements {@link ReadableByteChannel}. */
    @Override
    default int read(ByteBuffer dst) {
        return this.readBuffer(dst, Math.min(dst.remaining(), this.readableBytes()));
    }

    /** Implements {@link WritableByteChannel}. */
    @Override
    default int write(ByteBuffer src) {
        return this.writeBuffer(src, src.remaining());
    }

    /** Implements {@link Channel}. */
    @Override
    default boolean isOpen() {
        return this.isFree();
    }
}
