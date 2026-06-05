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
package com.clougence.utils.io.output;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * <p>Provides a mechanism to accept and Base64 encode bytes into
 * chars which will be flushed to the provided writer as the
 * internal buffer fills.</p>
 */
public class Base64OutputStream extends OutputStream {
    /**
     * The buffer where data is stored.
     */
    private              byte[] buf;
    /**
     * <p>The Base64 encoded bytes as chars; essentially the output
     * buffer</p>
     */
    private              char[] chars;
    /**
     * The number of valid bytes in the buffer.
     */
    private              int    count;
    /**
     * <p>The current position within <code>chars</code>
     */
    private              int    encCount;
    /**
     * <p>Tracks the total number of characters written.</p>
     */
    private              int    totalCharsWritten;
    /**
     * The writer we'll flush the bytes to instead of growing
     * the array.
     */
    private              Writer writer;
    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /**
     * Creates a new byte array output stream, with a buffer capacity of
     * the specified size, in bytes.
     *
     * @param size   the initial size.
     * @param writer the writer we'll flush to once
     *               we reach our capacity
     *
     * @throws IllegalArgumentException if size is negative.
     */
    public Base64OutputStream(final int size, final Writer writer) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        this.buf = new byte[size];
        this.chars = new char[size];
        this.totalCharsWritten = 0;
        this.writer = writer;
    }

    /**
     * Writes the specified byte to this byte array output stream.
     *
     * @param b the byte to be written.
     */
    @Override
    public void write(final int b) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this byte array output stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     */
    @Override
    public void write(final byte b[], final int off, final int len) throws IOException {
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        if (this.count + len > this.buf.length) {
            this.encodePendingBytes(false);
        }
        System.arraycopy(b, off, this.buf, this.count, len);
        this.count += len;
    }

    /**
     * <p>Calls through to {@link #write(byte[], int, int)}/</p>
     * @param b the bytes to write
     * @throws IOException if an error occurs
     */
    @Override
    public void write(final byte b[]) throws IOException {
        this.write(b, 0, b.length);
    }

    /** Closing <tt>Base64OutputStreamWriter</tt> does nothing. */
    @Override
    public void close() throws IOException {
    }

    /**
     * <p>Encodes the remaining bytes and flushes the <code>char[]</code>
     * to the wrapped <code>Writer</code>.</p>
     *
     * @throws IOException if an error occurs writing the remaining bytes
     */
    public void finish() throws IOException {
        this.encodePendingBytes(true);
    }

    /**
     * @return the total number of characters written
     */
    public int getTotalCharsWritten() {
        return this.totalCharsWritten;
    }

    /**
     * <p>Base64 encode any bytes found in <code>buf</code> and
     * store the result as characters in <code>chars</code>.  This method
     * will automatically write the contents of <code>chars</code> when
     * necessary.
     * @param pad flag to signal we're finalizing the encoding processes.
     * @throws IOException if an error occurs
     */
    private void encodePendingBytes(final boolean pad) throws IOException {
        int eLen = this.count / 3 * 3; // Length of even 24-bits.
        for (int s = 0; s < eLen; ) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (this.buf[s++] & 0xff) << 16 | (this.buf[s++] & 0xff) << 8 | this.buf[s++] & 0xff;
            if (this.encCount + 4 > this.chars.length) {
                // we're full, so write the encoded chars
                // and reset the pointer
                this.drainCharBuffer();
            }
            // Encode the int into four chars               
            this.chars[this.encCount++] = Base64OutputStream.CA[i >>> 18 & 0x3f];
            this.chars[this.encCount++] = Base64OutputStream.CA[i >>> 12 & 0x3f];
            this.chars[this.encCount++] = Base64OutputStream.CA[i >>> 6 & 0x3f];
            this.chars[this.encCount++] = Base64OutputStream.CA[i & 0x3f];
        }
        int left = this.count - eLen;
        if (!pad) {
            // push the non-encoded bytes to the beginning of the byte array
            // and set count to the end of those bytes
            System.arraycopy(this.buf, eLen, this.buf, 0, left);
            this.count = left;
        } else {
            this.drainCharBuffer();
            // pad if necessary
            if (left > 0) {
                // Prepare the int
                int i = (this.buf[eLen] & 0xff) << 10 | (left == 2 ? (this.buf[this.count - 1] & 0xff) << 2 : 0);
                // write last four chars
                this.writer.write(Base64OutputStream.CA[i >> 12]);
                this.writer.write(Base64OutputStream.CA[i >>> 6 & 0x3f]);
                this.writer.write(left == 2 ? Base64OutputStream.CA[i & 0x3f] : '=');
                this.writer.write('=');
            }
        }
    }

    /**
     * <p>Write the contents of <code>chars</code> to the
     * wrapped <code>Writer</code> and reset <code>encCount</code>
     * to zero.</p>
     * @throws IOException if an error occurs
     */
    private void drainCharBuffer() throws IOException {
        this.writer.write(this.chars, 0, this.encCount);
        this.totalCharsWritten += this.encCount;
        this.encCount = 0;
    }
    // Test Case: com.sun.faces.io.TestIO
} // END Base64OutputStreamWriter
