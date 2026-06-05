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
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class ObjectByteBufTest {
    @Test
    public void intObjectTest01() {
        BytesIO io = BytesIO.auto();

        io.writeInt16((short) 30047);
        io.writeInt24(15793921);
        io.writeInt32(1894780842);
        io.writeInt64(8071575397336023920L);
        io.markWriter();

        assert io.readInt16() == 30047;
        assert io.readInt24() == 15793921;
        assert io.readInt32() == 1894780842;
        assert io.readInt64() == 8071575397336023920L;
    }

    @Test
    public void intObjectTest02() {
        BytesIO io = BytesIO.auto();
        io.skipWritableBytes(40);

        io.setInt16(0, (short) 30047);
        io.setInt24(10, 15793921);
        io.setInt32(20, 1894780842);
        io.setInt64(30, 8071575397336023920L);

        io.markWriter();
        assert io.getInt16(0) == 30047;
        assert io.getInt24(10) == 15793921;
        assert io.getInt32(20) == 1894780842;
        assert io.getInt64(30) == 8071575397336023920L;
    }

    @Test
    public void intLEObjectTest01() {
        BytesIO io = BytesIO.auto();
        io.order(ByteOrder.LITTLE_ENDIAN);

        io.writeInt16((short) 30047);
        io.writeInt24(15793921);
        io.writeInt32(1894780842);
        io.writeInt64(8071575397336023920L);
        io.markWriter();

        assert io.readInt16() == 30047;
        assert io.readInt24() == 15793921;
        assert io.readInt32() == 1894780842;
        assert io.readInt64() == 8071575397336023920L;
    }

    @Test
    public void intLEObjectTest02() {
        BytesIO io = BytesIO.auto();
        io.order(ByteOrder.LITTLE_ENDIAN);
        io.skipWritableBytes(40);

        io.setInt16(0, (short) 30047);
        io.setInt24(10, 15793921);
        io.setInt32(20, 1894780842);
        io.setInt64(30, 8071575397336023920L);

        io.markWriter();
        assert io.getInt16(0) == 30047;
        assert io.getInt24(10) == 15793921;
        assert io.getInt32(20) == 1894780842;
        assert io.getInt64(30) == 8071575397336023920L;
    }

    @Test
    public void floatObjectTest01() {
        BytesIO io = BytesIO.auto();

        io.writeFloat32(999999.999999f);
        io.writeFloat64(123456789123456789.123456789123456789123456789123456789d);
        io.markWriter();

        assert io.readFloat32() == 999999.999999f;
        assert io.readFloat64() == 123456789123456789.123456789123456789123456789123456789d;
    }

    @Test
    public void floatObjectTest02() {
        BytesIO io = BytesIO.auto();
        io.skipWritableBytes(40);

        io.setFloat32(0, 999999.999999f);
        io.setFloat64(10, 123456789123456789.123456789123456789123456789123456789d);
        io.markWriter();

        assert io.getFloat32(0) == 999999.999999f;
        assert io.getFloat64(10) == 123456789123456789.123456789123456789123456789123456789d;
    }

    @Test
    public void floatLEObjectTest01() {
        BytesIO io = BytesIO.auto();
        io.order(ByteOrder.LITTLE_ENDIAN);

        io.writeFloat32(999999.999999f);
        io.writeFloat64(123456789123456789.123456789123456789123456789123456789d);
        io.markWriter();

        assert io.readFloat32() == 999999.999999f;
        assert io.readFloat64() == 123456789123456789.123456789123456789123456789123456789d;
    }

    @Test
    public void floatLEObjectTest02() {
        BytesIO io = BytesIO.auto();
        io.order(ByteOrder.LITTLE_ENDIAN);
        io.skipWritableBytes(40);

        io.setFloat32(0, 999999.999999f);
        io.setFloat64(10, 123456789123456789.123456789123456789123456789123456789d);
        io.markWriter();

        assert io.getFloat32(0) == 999999.999999f;
        assert io.getFloat64(10) == 123456789123456789.123456789123456789123456789123456789d;
    }

    @Test
    public void uIntObjectTest01() {
        BytesIO io = BytesIO.auto();

        io.writeByte((byte) -1);
        io.writeInt16((short) -1);
        io.writeInt24(-1);
        io.writeInt32(-1);
        io.writeUInt32(-1);
        io.writeInt64(-1);
        io.markWriter();

        assert io.getUInt8(0) == 255;
        assert io.getUInt16(1) == 65535;
        assert io.getUInt24(3) == 16777215;
        assert io.getUInt32(5) == 4294967295L;
        assert io.getInt32(9) == -1;
        assert io.getUInt32(9) == 4294967295L;
        assert io.getUInt32(6) == 4294967295L;
        assert io.getInt64(10) == -1;

        assert io.readUInt8() == 255;
        assert io.readUInt16() == 65535;
        assert io.readUInt24() == 16777215;
        assert io.readUInt32() == 4294967295L;
        assert io.readInt64() == -1L;
    }

    @Test
    public void uIntObjectTest02() {
        BytesIO io = BytesIO.auto();
        io.order(ByteOrder.LITTLE_ENDIAN);

        io.writeInt16((short) 36848);
        io.writeInt24(9433258);
        io.writeInt32((int) 2156916906L);
        io.writeUInt32(4294967295L);
        io.markWriter();

        assert io.getUInt16(0) == 36848;
        assert io.getUInt24(2) == 9433258;
        assert io.getUInt32(5) == 2156916906L;
        assert io.getUInt32(9) == 4294967295L;
        assert io.getInt32(9) == -1;

        assert io.readUInt16() == 36848;
        assert io.readUInt24() == 9433258;
        assert io.readUInt32() == 2156916906L;
        assert io.readUInt32() == 4294967295L;
    }

    @Test
    public void stringTest01() {
        BytesIO io = BytesIO.auto();

        io.writeString("hello\n", StandardCharsets.UTF_8);
        io.writeString("word\n", StandardCharsets.UTF_8);
        io.markWriter();

        assert io.readExpect('\n', StandardCharsets.UTF_8).equals("hello");
        assert io.readExpect('\n', StandardCharsets.UTF_8).equals("word");

        io.resetReader();
        assert io.readExpectLast('\n', StandardCharsets.UTF_8).equals("hello\nword");
    }

    @Test
    public void stringTest02() {
        BytesIO io = BytesIO.auto();

        io.writeString("hello\n", StandardCharsets.UTF_8);
        io.writeString("word\n", StandardCharsets.UTF_8);
        io.markWriter();

        assert io.readExpect('\n', StandardCharsets.UTF_8).equals("hello");
        assert io.readExpect('\n', StandardCharsets.UTF_8).equals("word");

        io.resetReader();
        assert io.readExpectLast('\n', StandardCharsets.UTF_8).equals("hello\nword");
    }
}