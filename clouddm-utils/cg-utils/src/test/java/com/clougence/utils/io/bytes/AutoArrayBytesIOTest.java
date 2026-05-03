package com.clougence.utils.io.bytes;

import com.clougence.utils.RandomUtils;
import com.clougence.utils.codec.MD5;
import org.junit.jupiter.api.Test;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class AutoArrayBytesIOTest {
    @Test
    public void basicTest01() {
        BytesIO io = BytesIO.auto(111, 111);
        assert io.capacity() == 111;
        assert io.toString().startsWith("AutoArrayBytesIO[rMark=");
    }

    @Test
    public void basicTest02() {
        BytesIO io = BytesIO.auto(4, 4);
        io.writeBytes(new byte[] { 1, 2, 3, 4 });
        io.markWriter();

        assert io.readByte() == 1;
        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        io.markReader();

        io.resetReader();
        assert io.readableBytes() == 0;
    }

    @Test
    public void basicTest03() {
        byte[] cacheData = RandomUtils.nextBytes(8192);
        BytesIO io = BytesIO.auto(1024, 1024);
        assert io.writeBytes(cacheData) == 1024;
    }

    @Test
    public void basicTest04() {
        BytesIO io = BytesIO.auto(4, 4);
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
        AutoArrayBytesIO io = (AutoArrayBytesIO) BytesIO.auto(new byte[4], 5, 10, true);

        io.writeBytes(new byte[] { 1, 2, 3, 4 });
        assert io.capacity() == 4;
        assert io.getMaxCapacity() == 10;

        io.writeBytes(new byte[] { 5, 6, 7, 8 });
        assert io.capacity() == 10; // after writer target size is 8, --> final target size is ((8 / 5) + 1) * 5
        assert io.getMaxCapacity() == 10;

        io.markWriter();

        assert io.readByte() == 1;
        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        assert io.capacity() == 10;
        io.markReader();
        assert io.capacity() == 5;
        assert io.getMaxCapacity() == 10;

        assert io.readByte() == 5;
        assert io.readByte() == 6;
        assert io.readByte() == 7;
        assert io.readByte() == 8;
        io.markReader();
        assert io.capacity() == 5;
        assert io.getMaxCapacity() == 10;
    }

    @Test
    public void writeByte_1_1() {
        BytesIO io = BytesIO.auto(4, 4);

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

        io.writeByte((byte) 5);
        io.writeByte((byte) 6);
        io.writeByte((byte) 7);
        io.writeByte((byte) 8);
        io.markWriter();

        assert io.readByte() == 5;
        assert io.readByte() == 6;
        assert io.readByte() == 7;
        assert io.readByte() == 8;
    }

    @Test
    public void writeByte_1_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

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

        io.writeByte((byte) 5);
        io.writeByte((byte) 6);
        io.writeByte((byte) 7);
        io.writeByte((byte) 8);
        io.markWriter();

        assert io.readByte() == 5;
        assert io.readByte() == 6;
        assert io.readByte() == 7;
        assert io.readByte() == 8;
    }

    @Test
    public void writeBytes_1_1() {
        BytesIO io = BytesIO.auto(4, 4);

        io.writeBytes(new byte[] { 1, 2, 3, 4 });

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

        byte[] arrayRead = new byte[6];
        io.readBytes(arrayRead);
        io.markReader();
        assert arrayRead[0] == 1;
        assert arrayRead[1] == 2;
        assert arrayRead[2] == 3;
        assert arrayRead[3] == 4;

        io.writeBytes(new byte[] { 5, 6, 7, 8 });
        io.markWriter();

        io.readBytes(arrayRead);
        io.markReader();
        assert arrayRead[0] == 5;
        assert arrayRead[1] == 6;
        assert arrayRead[2] == 7;
        assert arrayRead[3] == 8;
    }

    @Test
    public void writeBytes_1_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        io.writeBytes(new byte[] { 1, 2, 3, 4 });

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

        byte[] arrayRead = new byte[6];
        io.readBytes(arrayRead);
        io.markReader();
        assert arrayRead[0] == 1;
        assert arrayRead[1] == 2;
        assert arrayRead[2] == 3;
        assert arrayRead[3] == 4;

        io.writeBytes(new byte[] { 5, 6, 7, 8 });
        io.markWriter();

        io.readBytes(arrayRead);
        io.markReader();
        assert arrayRead[0] == 5;
        assert arrayRead[1] == 6;
        assert arrayRead[2] == 7;
        assert arrayRead[3] == 8;
    }

    @Test
    public void writeBytes_2_1() {
        byte[] arrayRead = new byte[6];
        BytesIO io = BytesIO.auto(4, 4);

        io.writeBytes(new byte[] { 1, 2, 3 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 1;
        assert arrayRead[1] == 2;
        assert arrayRead[2] == 3;

        io.writeBytes(new byte[] { 4, 5, 6 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 4;
        assert arrayRead[1] == 5;
        assert arrayRead[2] == 6;

        io.writeBytes(new byte[] { 7, 8, 9 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 7;
        assert arrayRead[1] == 8;
        assert arrayRead[2] == 9;
    }

    @Test
    public void writeBytes_2_2() {
        byte[] arrayRead = new byte[6];
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        io.writeBytes(new byte[] { 1, 2, 3 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 1;
        assert arrayRead[1] == 2;
        assert arrayRead[2] == 3;

        io.writeBytes(new byte[] { 4, 5, 6 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 4;
        assert arrayRead[1] == 5;
        assert arrayRead[2] == 6;

        io.writeBytes(new byte[] { 7, 8, 9 });
        io.markWriter();

        assert io.readBytes(arrayRead) == 3;
        io.markReader();
        assert arrayRead[0] == 7;
        assert arrayRead[1] == 8;
        assert arrayRead[2] == 9;
    }

    @Test
    public void writeBytes_3_1() {
        BytesIO io = BytesIO.auto(4, 4);

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

        assert io.writeBytes(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 1, 4) == 4;
        io.markWriter();

        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        assert io.readByte() == 5;
        io.markReader();
    }

    @Test
    public void writeBytes_3_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

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

        assert io.writeBytes(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 1, 4) == 4;
        io.markWriter();

        assert io.readByte() == 2;
        assert io.readByte() == 3;
        assert io.readByte() == 4;
        assert io.readByte() == 5;
        io.markReader();
    }

    @Test
    public void writeBytes_4_1() {
        byte[] array = new byte[4];
        BytesIO io = BytesIO.auto(4, 4);

        io.writeBytes(new byte[] { 1, 2, 3, 4 });
        io.markWriter();

        assert io.readBytes(array, 2, 2) == 2;
        io.markReader();
        assert array[0] == 0;
        assert array[1] == 0;
        assert array[2] == 1;
        assert array[3] == 2;

        io.writeBytes(new byte[] { 5, 6 });
        io.markWriter();

        assert io.readBytes(array, 1, 3) == 3;
        io.markReader();
        assert array[0] == 0;
        assert array[1] == 3;
        assert array[2] == 4;
        assert array[3] == 5;

        assert io.readByte() == 6;
        io.markReader();
    }

    @Test
    public void writeBytes_4_2() {
        byte[] array = new byte[4];
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        io.writeBytes(new byte[] { 1, 2, 3, 4 });
        io.markWriter();

        assert io.readBytes(array, 2, 2) == 2;
        io.markReader();
        assert array[0] == 0;
        assert array[1] == 0;
        assert array[2] == 1;
        assert array[3] == 2;

        io.writeBytes(new byte[] { 5, 6 });
        io.markWriter();

        assert io.readBytes(array, 1, 3) == 3;
        io.markReader();
        assert array[0] == 0;
        assert array[1] == 3;
        assert array[2] == 4;
        assert array[3] == 5;

        assert io.readByte() == 6;
        io.markReader();
    }

    @Test
    public void writeBuffer_1_1() {
        BytesIO io = BytesIO.auto(4, 4);
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
    public void writeBuffer_1_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

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
        BytesIO io = BytesIO.auto(4, 4);
        io.writeBuffer(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
        io.markWriter();

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
    public void writeBuffer_2_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        io.writeBuffer(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
        io.markWriter();

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
        BytesIO io = BytesIO.auto(4, 4);
        io.writeBuffer(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
        io.markWriter();

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
    public void writeBuffer_3_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        io.writeBuffer(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }));
        io.markWriter();

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
        BytesIO io = BytesIO.auto(4, 4);
        BytesIO data = BytesIO.wrap(new byte[] { 1, 2, 3, 4 });

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
    public void writeBuf_1_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        BytesIO data = BytesIO.wrap(new byte[] { 1, 2, 3, 4 });

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
        BytesIO io = BytesIO.auto(4, 4);
        io.writeBuffer(BytesIO.wrap(new byte[] { 1, 2, 3, 4 }));
        io.markWriter();

        BytesIO alloc1 = BytesIO.auto(4, 4);
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

        BytesIO alloc2 = BytesIO.auto(4, 4);
        assert io.readBuffer(alloc2) == 4;
        alloc2.markWriter();
        assert alloc2.asByteArray()[0] == 1;
        assert alloc2.asByteArray()[1] == 2;
        assert alloc2.asByteArray()[2] == 3;
        assert alloc2.asByteArray()[3] == 4;

        io.resetReader();
        BytesIO alloc3 = BytesIO.auto(4, 4);
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
    public void writeBuf_2_2() {
        BytesIO io = BytesIO.auto(4, 4);
        io.skipWritableBytes(2);
        io.markWriter();
        io.skipReadableBytes(2);
        io.markReader();

        io.writeBuffer(BytesIO.wrap(new byte[] { 1, 2, 3, 4 }));
        io.markWriter();

        BytesIO alloc1 = BytesIO.auto(4, 4);
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

        BytesIO alloc2 = BytesIO.auto(4, 4);
        assert io.readBuffer(alloc2) == 4;
        alloc2.markWriter();
        assert alloc2.asByteArray()[0] == 1;
        assert alloc2.asByteArray()[1] == 2;
        assert alloc2.asByteArray()[2] == 3;
        assert alloc2.asByteArray()[3] == 4;

        io.resetReader();
        BytesIO alloc3 = BytesIO.auto(4, 4);
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
        BytesIO io = BytesIO.auto(111);
        io.free();

        try {
            io.asByteArray();
            assert false;
        } catch (IllegalStateException e) {
            assert e.getMessage().equals("has been released.");
        }

        try {
            io.writeByte((byte) 5);
            assert false;
        } catch (IllegalStateException e) {
            assert e.getMessage().equals("has been released.");
        }
    }

    @Test
    public void copyTest01() throws NoSuchAlgorithmException {
        AutoArrayBytesIO io1 = (AutoArrayBytesIO) BytesIO.auto(12);
        io1.writeBytes(new byte[] { 1, 2, 3, 4 });

        AutoArrayBytesIO io2 = io1.copy();

        assert io1.target != io2.target;
        assert io1.target.length == io2.target.length;

        String hash1 = MD5.encodeMD5(io1.asByteArray());
        String hash2 = MD5.encodeMD5(io2.asByteArray());
        assert hash1.equals(hash2);
    }

    @Test
    public void copyTest02() {
        AutoArrayBytesIO io1 = (AutoArrayBytesIO) BytesIO.auto(12);
        io1.writeBytes(new byte[] { 1, 2, 3, 4 });
        io1.markWriter();

        assert io1.readByte() == 1;
        assert io1.readByte() == 2;

        AutoArrayBytesIO byteBuf2 = io1.copy();
        assert byteBuf2.readByte() == 3;
        assert byteBuf2.readByte() == 4;

        assert io1.readByte() == 3;
        assert io1.readByte() == 4;
    }

    @Test
    public void errorTest01() {
        try {
            BytesIO.auto(-1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("max: -1 (expected: >= 0)");
        }
    }

    @Test
    public void errorTest02() {
        try {
            BytesIO io = BytesIO.auto(4, 4);
            io.getByte(0);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert e.getMessage().startsWith("read out of range. index: 0, length: 1 (expected: 0 ~ 0)");
        }

        try {
            BytesIO byteBuf = BytesIO.auto(4, 4);
            byteBuf.writeByte((byte) 1);
            byteBuf.getByte(0);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert e.getMessage().startsWith("read out of range. index: 0, length: 1 (expected: 0 ~ 0)");
        }
    }

    @Test
    public void errorTest03() {
        try {
            BytesIO io = BytesIO.auto(4, 4);
            io.writeBytes(new byte[] { 1, 2, 3, 4 });
            io.markWriter();
            io.readInt64();
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert e.getMessage().startsWith("read out of range. length: 8 (expected: 0 ~ 4)");
        }
    }

    @Test
    public void writeStringTest01() {
        byte[] date = "aaa\nbbb\nccc\n".getBytes(StandardCharsets.US_ASCII);

        BytesIO io = BytesIO.auto(1024);
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
        BytesIO io = BytesIO.auto(1024);

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

    @Test
    public void writeStringTest03() {
        BytesIO io = BytesIO.fixed(10);

        io.writeBytes("1234\r\n".getBytes());
        io.markWriter();
        readTest(io);

        io.writeBytes("1234\r\n".getBytes());
        io.markWriter();
        readTest(io);

        io.writeBytes("1234\r\n".getBytes());
        io.markWriter();
        readTest(io);

        io.writeBytes("1234\r\n".getBytes());
        io.markWriter();
        readTest(io);
    }

    private void readTest(BytesIO io) {
        if (io.expect("\r\n", StandardCharsets.US_ASCII) >= 0) {
            String str = io.readExpect("\r\n", StandardCharsets.US_ASCII);
            io.markReader();
            assert str.equals("1234");
        }
    }

    @Test
    public void expWriteBytes_1() {
        BytesIO io = BytesIO.auto(4, 10);

        io.writeBytes(new byte[] { 1, 2, 3, 4 });
        io.markWriter();
        io.writeBytes(new byte[] { 5, 6, 7, 8 });
        io.markWriter();

        byte[] array = new byte[8];
        assert io.readBytes(array) == 8;
        io.markReader();
        assert array[0] == 1;
        assert array[1] == 2;
        assert array[2] == 3;
        assert array[3] == 4;
        assert array[4] == 5;
        assert array[5] == 6;
        assert array[6] == 7;
        assert array[7] == 8;
    }
}