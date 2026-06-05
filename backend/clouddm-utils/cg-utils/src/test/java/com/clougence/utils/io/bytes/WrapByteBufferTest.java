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
import com.clougence.utils.RandomUtils;
import com.clougence.utils.codec.MD5;
import org.junit.jupiter.api.Test;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class WrapByteBufferTest {
    @Test
    public void basicTest01() {
        BytesIO io1 = BytesIO.wrap(ByteBuffer.allocate(111));
        assert io1.capacity() == 111;
        assert io1.toString().startsWith("WrapBufferBytesIO[rMark=");
        io1.free();
    }

    @Test
    public void basicTest02() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
        assert io.readableBytes() == 4;

        assert io.readByte() == 1;
        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        io.markReader();
        assert io.readableBytes() == 0;

        io.resetReader();
        assert io.readableBytes() == 0;
    }

    @Test
    public void basicTest03() {
        byte[] cacheData = RandomUtils.nextBytes(8192);
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[1024]), true);
        assert io.writeBytes(cacheData) == 1024;
    }

    @Test
    public void basicTest04() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[4]), true);
        ByteBuffer data = ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 });
        data.flip();

        assert data.position() == 0;
        assert data.limit() == 0;
        assert data.capacity() == 4;
        assert io.writeBuffer(data) == 0;
        io.markWriter();
        assert data.position() == 0;
        assert data.limit() == 0;
        assert data.capacity() == 4;
    }

    @Test
    public void basicTest05() {
        BytesIO wrap = BytesIO.wrap(ByteBuffer.wrap(new byte[2]));
        assert wrap.readableBytes() == 2;
    }

    @Test
    public void writeByte_1_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }), true);

        io.writeByte((byte) 1);
        io.writeByte((byte) 2);
        io.writeByte((byte) 3);
        io.writeByte((byte) 4);

        // not markIndex yet
        try {
            io.writeByte((byte) 5);
            assert false;
        } catch (BufferOverflowException e) {
            assert true;
        }
        try {
            io.readByte();
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        io.markWriter();

        assert io.readByte() == 1;
        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        io.markReader();
    }

    @Test
    public void writeBytes_1_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        // not markIndex yet
        try {
            io.writeByte((byte) 5);
            assert false;
        } catch (BufferOverflowException e) {
            assert true;
        }

        byte[] arrayRead = new byte[6];
        io.readBytes(arrayRead);
        io.markReader();
        assert arrayRead[0] == 1;
        assert arrayRead[1] == 2;
        assert arrayRead[2] == 3;
        assert arrayRead[3] == 4;
    }

    @Test
    public void writeBytes_2_1() {
        byte[] arrayRead = new byte[6];
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[4]), true);

        io.writeBytes(new byte[] { 1, 2, 3 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 1;
        assert arrayRead[1] == 2;
        assert arrayRead[2] == 3;

        io.writeBytes(new byte[] { 4 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 1;
        io.markReader();
        assert arrayRead[0] == 4;
    }

    @Test
    public void writeBytes_3_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[4]), true);

        assert io.writeBytes(new byte[] { 1, 2, 3, 4 }, 1, 2) == 2;
        io.markWriter();

        assert io.readByte() == 2;
        io.markReader();
        assert io.readByte() == 3;

        assert io.writeBytes(new byte[] { 5, 6, 7, 8 }, 1, 2) == 2;
        io.markWriter();

        assert io.readByte() == 6;
        assert io.readByte() == 7;
        io.markReader();
    }

    @Test
    public void writeBuffer_1_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[4]), true);
        ByteBuffer data = ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 });

        assert data.position() == 0;
        assert data.limit() == 4;
        assert data.capacity() == 4;
        io.writeBuffer(data);
        io.markWriter();
        assert data.position() == 4;
        assert data.limit() == 4;
        assert data.capacity() == 4;

        assert io.getByte(0) == 1;
        assert io.getByte(1) == 2;
        assert io.getByte(2) == 3;
        assert io.getByte(3) == 4;

        assert io.asByteArray()[0] == 1;
        assert io.asByteArray()[1] == 2;
        assert io.asByteArray()[2] == 3;
        assert io.asByteArray()[3] == 4;

        assert io.readByte() == 1;
        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        io.resetReader();
    }

    @Test
    public void writeBuffer_2_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        ByteBuffer alloc1 = ByteBuffer.allocate(4);
        assert alloc1.limit() == 4;
        assert alloc1.position() == 0;
        assert io.getBuffer(0, alloc1) == 4;
        assert alloc1.limit() == 4;
        assert alloc1.position() == 4;

        assert alloc1.array()[0] == 1;
        assert alloc1.array()[1] == 2;
        assert alloc1.array()[2] == 3;
        assert alloc1.array()[3] == 4;

        try {
            alloc1.get();
            assert false;
        } catch (BufferUnderflowException e) {
            assert true;
        }
        assert alloc1.remaining() == 0;
        alloc1.flip();
        assert alloc1.remaining() == 4;
        assert alloc1.get() == 1;
        assert alloc1.get() == 2;
        assert alloc1.get() == 3;
        assert alloc1.get() == 4;
        assert alloc1.remaining() == 0;

        assert io.asByteArray()[0] == 1;
        assert io.asByteArray()[1] == 2;
        assert io.asByteArray()[2] == 3;
        assert io.asByteArray()[3] == 4;

        ByteBuffer alloc2 = ByteBuffer.allocate(4);
        assert io.readBuffer(alloc2) == 4;
        assert alloc2.array()[0] == 1;
        assert alloc2.array()[1] == 2;
        assert alloc2.array()[2] == 3;
        assert alloc2.array()[3] == 4;

        io.resetReader();
        ByteBuffer alloc3 = ByteBuffer.allocate(4);
        assert alloc3.limit() == 4;
        assert alloc3.position() == 0;
        assert alloc3.remaining() == 4;
        alloc3.position(1);
        assert io.readBuffer(alloc3, 2) == 2;
        assert alloc3.limit() == 4;
        assert alloc3.position() == 3;
        assert alloc3.remaining() == 1;
        alloc3.flip();
        assert alloc3.limit() == 3;
        assert alloc3.position() == 0;
        assert alloc3.remaining() == 3;

        assert alloc3.get() == 0;
        assert alloc3.get() == 1;
        assert alloc3.get() == 2;
        try {
            alloc3.get();
            assert false;
        } catch (BufferUnderflowException e) {
            assert true;
        }
    }

    @Test
    public void writeBuffer_3_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        ByteBuffer alloc1 = ByteBuffer.allocate(4);
        assert io.readableBytes() == 4;
        assert io.getBuffer(0, alloc1) == 4;
        assert io.readableBytes() == 4;

        ByteBuffer alloc2 = ByteBuffer.allocate(4);
        assert io.readableBytes() == 4;
        assert io.readBuffer(alloc2) == 4;
        assert io.readableBytes() == 0;
    }

    @Test
    public void writeBuf_1_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[4]), true);
        BytesIO data = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        assert data.readableBytes() == 4;
        assert data.writableBytes() == 0;
        assert io.readableBytes() == 0;
        assert io.writableBytes() == 4;
        io.writeBuffer(data);
        assert io.readableBytes() == 0;
        assert io.writableBytes() == 0;
        io.markWriter();
        assert data.readableBytes() == 0;
        assert data.writableBytes() == 0;
        assert io.readableBytes() == 4;
        assert io.writableBytes() == 0;

        assert io.getByte(0) == 1;
        assert io.getByte(1) == 2;
        assert io.getByte(2) == 3;
        assert io.getByte(3) == 4;

        assert io.asByteArray()[0] == 1;
        assert io.asByteArray()[1] == 2;
        assert io.asByteArray()[2] == 3;
        assert io.asByteArray()[3] == 4;

        assert io.readByte() == 1;
        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        io.resetReader();
    }

    @Test
    public void writeBuf_2_1() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        BytesIO alloc1 = BytesIO.ring(4);
        assert alloc1.readableBytes() == 0;
        assert alloc1.writableBytes() == 4;
        assert io.getBuffer(0, alloc1) == 4;
        assert alloc1.readableBytes() == 0;
        assert alloc1.writableBytes() == 0;
        alloc1.markWriter();
        assert alloc1.readableBytes() == 4;
        assert alloc1.writableBytes() == 0;

        assert alloc1.readByte() == 1;
        assert alloc1.readByte() == 2;
        assert alloc1.readByte() == 3;
        assert alloc1.readByte() == 4;

        try {
            alloc1.readByte();
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }

        assert alloc1.readableBytes() == 0;
        assert alloc1.writableBytes() == 0;
        alloc1.markReader();
        assert alloc1.readableBytes() == 0;
        assert alloc1.writableBytes() == 4;

        assert io.asByteArray()[0] == 1;
        assert io.asByteArray()[1] == 2;
        assert io.asByteArray()[2] == 3;
        assert io.asByteArray()[3] == 4;

        BytesIO alloc2 = BytesIO.ring(4);
        assert io.readBuffer(alloc2) == 4;
        alloc2.markWriter();
        assert alloc2.asByteArray()[0] == 1;
        assert alloc2.asByteArray()[1] == 2;
        assert alloc2.asByteArray()[2] == 3;
        assert alloc2.asByteArray()[3] == 4;

        io.resetReader();
        BytesIO alloc3 = BytesIO.ring(4);
        assert alloc3.readableBytes() == 0;
        assert alloc3.writableBytes() == 4;
        alloc3.skipWritableBytes(1);
        assert io.readBuffer(alloc3, 2) == 2;
        assert alloc3.readableBytes() == 0;
        assert alloc3.writableBytes() == 1;
        alloc3.markWriter();
        assert alloc3.readableBytes() == 3;
        assert alloc3.writableBytes() == 1;

        assert alloc3.readByte() == 0;
        assert alloc3.readByte() == 1;
        assert alloc3.readByte() == 2;
        try {
            alloc3.readByte();
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

    @Test
    public void freeTest01() {
        WrapBufferBytesIO io = (WrapBufferBytesIO) BytesIO.wrap(ByteBuffer.allocate(111), true);
        io.free();

        assert io.target == null;

        try {
            io.writeByte((byte) 5);
            assert false;
        } catch (IllegalStateException e) {
            assert e.getMessage().equals("has been released.");
        }
    }

    @Test
    public void freeTest02() {
        try {
            WrapBufferBytesIO io = (WrapBufferBytesIO) BytesIO.wrap(ByteBuffer.allocateDirect(111), true);
            io.free();
            assert false;
        } catch (IllegalStateException e) {
            assert e.getMessage().equals("Direct ByteBuffer can't be free, please use the target() method to manually release.");
        }
    }

    @Test
    public void copyTest01() throws NoSuchAlgorithmException {
        WrapBufferBytesIO io1 = (WrapBufferBytesIO) BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
        WrapBufferBytesIO io2 = io1.copy();

        assert io1.target != io2.target;
        assert io1.target.capacity() == io2.target.capacity();

        String hash1 = MD5.encodeMD5(io1.asByteArray());
        String hash2 = MD5.encodeMD5(io2.asByteArray());
        assert hash1.equals(hash2);
    }

    @Test
    public void copyTest02() {
        BytesIO io1 = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        assert io1.readByte() == 1;
        assert io1.readByte() == 2;

        BytesIO io2 = io1.copy();
        assert io2.readByte() == 3;
        assert io2.readByte() == 4;

        assert io1.readByte() == 3;
        assert io1.readByte() == 4;
    }

    @Test
    public void errorTest01() {
        try {
            BytesIO.wrap((ByteBuffer) null);
            assert false;
        } catch (NullPointerException e) {
            assert e.getMessage().equals("buffer is null.");
        }
    }

    @Test
    public void errorTest02() {
        try {
            BytesIO io = BytesIO.wrap(ByteBuffer.allocate(4), true);
            io.getByte(0);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert e.getMessage().startsWith("read out of range. index: 0, length: 1 (expected: 0 ~ 0)");
        }

        try {
            BytesIO io = BytesIO.wrap(ByteBuffer.allocate(4), true);
            io.writeByte((byte) 1);
            io.getByte(0);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert e.getMessage().startsWith("read out of range. index: 0, length: 1 (expected: 0 ~ 0)");
        }
    }

    @Test
    public void errorTest03() {
        try {
            BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
            io.readInt64();
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert e.getMessage().startsWith("read out of range. length: 8 (expected: 0 ~ 4)");
        }
    }

    @Test
    public void errorTest06() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));

        try {
            io.getByte(6);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("read out of range. index: 6, length: 1 (expected: 0 ~ 4)");
        }

        try {
            io.getBytes(2, new byte[6], 1, 5);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().startsWith("read out of range. index: 2, length: 5 (expected: 0 ~ 4)");
        }
    }

    @Test
    public void writeStringTest01() {
        byte[] date = "aaa\nbbb\nccc\n".getBytes(StandardCharsets.US_ASCII);

        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[1024]), true);
        io.writeBytes(date);
        io.markWriter();

        String line1 = io.readExpect("\n", StandardCharsets.US_ASCII);
        String line2 = io.readExpect("\n", StandardCharsets.US_ASCII);
        String line3 = io.readExpect("\n", StandardCharsets.US_ASCII);

        assert line1.equals("aaa");
        assert line2.equals("bbb");
        assert line3.equals("ccc");
    }

    @Test
    public void writeStringTest02() {
        BytesIO io = BytesIO.wrap(ByteBuffer.wrap(new byte[1024]), true);

        io.writeBytes("abc1\r\n".getBytes());
        io.markWriter();

        io.writeBytes("abc2\r\n".getBytes());
        io.markWriter();

        String line1 = io.readExpect("\r\n", StandardCharsets.US_ASCII);
        io.markReader();

        String line2 = io.readExpect("\r\n", StandardCharsets.US_ASCII);
        io.markReader();

        io.writeBytes("abc3\r\n".getBytes());
        io.markWriter();

        String line3 = io.readExpect("\r\n", StandardCharsets.US_ASCII);
        io.markReader();

        assert line1.equals("abc1");
        assert line2.equals("abc2");
        assert line3.equals("abc3");
    }
}