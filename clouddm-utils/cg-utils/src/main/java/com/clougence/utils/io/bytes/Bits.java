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

class Bits {
    private static byte long7(long x) {
        return (byte) (x >> 56);
    }

    private static byte long6(long x) {
        return (byte) (x >> 48);
    }

    private static byte long5(long x) {
        return (byte) (x >> 40);
    }

    private static byte long4(long x) {
        return (byte) (x >> 32);
    }

    private static byte long3(long x) {
        return (byte) (x >> 24);
    }

    private static byte long2(long x) {
        return (byte) (x >> 16);
    }

    private static byte long1(long x) {
        return (byte) (x >> 8);
    }

    private static byte long0(long x) {
        return (byte) (x);
    }

    private static byte int3(int x) {
        return (byte) (x >> 24);
    }

    private static byte int2(int x) {
        return (byte) (x >> 16);
    }

    private static byte int1(int x) {
        return (byte) (x >> 8);
    }

    private static byte int0(int x) {
        return (byte) (x);
    }

    private static byte short1(short x) {
        return (byte) (x >> 8);
    }

    private static byte short0(short x) {
        return (byte) (x);
    }

    public static void encodeInt16(AbstractBytesIO bb, int offset, short v, boolean bigEndian) {
        if (bigEndian) {
            bb._putByte(offset, short1(v));
            bb._putByte(offset + 1, short0(v));
        } else {
            bb._putByte(offset, short0(v));
            bb._putByte(offset + 1, short1(v));
        }
    }

    public static void encodeInt24(AbstractBytesIO bb, int offset, int v, boolean bigEndian) {
        if (bigEndian) {
            bb._putByte(offset, int2(v));
            bb._putByte(offset + 1, int1(v));
            bb._putByte(offset + 2, int0(v));
        } else {
            bb._putByte(offset, int0(v));
            bb._putByte(offset + 1, int1(v));
            bb._putByte(offset + 2, int2(v));
        }
    }

    public static void encodeInt32(AbstractBytesIO bb, int offset, int v, boolean bigEndian) {
        if (bigEndian) {
            bb._putByte(offset, int3(v));
            bb._putByte(offset + 1, int2(v));
            bb._putByte(offset + 2, int1(v));
            bb._putByte(offset + 3, int0(v));
        } else {
            bb._putByte(offset, int0(v));
            bb._putByte(offset + 1, int1(v));
            bb._putByte(offset + 2, int2(v));
            bb._putByte(offset + 3, int3(v));
        }
    }

    public static void encodeInt32(AbstractBytesIO bb, int offset, long v, boolean bigEndian) {
        if (bigEndian) {
            bb._putByte(offset, long3(v));
            bb._putByte(offset + 1, long2(v));
            bb._putByte(offset + 2, long1(v));
            bb._putByte(offset + 3, long0(v));
        } else {
            bb._putByte(offset, long0(v));
            bb._putByte(offset + 1, long1(v));
            bb._putByte(offset + 2, long2(v));
            bb._putByte(offset + 3, long3(v));
        }
    }

    public static void encodeInt64(AbstractBytesIO bb, int offset, long v, boolean bigEndian) {
        if (bigEndian) {
            bb._putByte(offset, long7(v));
            bb._putByte(offset + 1, long6(v));
            bb._putByte(offset + 2, long5(v));
            bb._putByte(offset + 3, long4(v));
            bb._putByte(offset + 4, long3(v));
            bb._putByte(offset + 5, long2(v));
            bb._putByte(offset + 6, long1(v));
            bb._putByte(offset + 7, long0(v));
        } else {
            bb._putByte(offset, long0(v));
            bb._putByte(offset + 1, long1(v));
            bb._putByte(offset + 2, long2(v));
            bb._putByte(offset + 3, long3(v));
            bb._putByte(offset + 4, long4(v));
            bb._putByte(offset + 5, long5(v));
            bb._putByte(offset + 6, long6(v));
            bb._putByte(offset + 7, long7(v));
        }
    }

    private static short makeSort(byte b1, byte b0) {
        return (short) ((b1 << 8) | (b0 & 0xff));
    }

    private static int makeInt(byte b2, byte b1, byte b0) {
        return (((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | ((b0 & 0xff)));
    }

    private static int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3) << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | ((b0 & 0xff)));
    }

    static private long makeLong(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
        return ((((long) b7) << 56) | //
                (((long) b6 & 0xff) << 48) | //
                (((long) b5 & 0xff) << 40) | //
                (((long) b4 & 0xff) << 32) | //
                (((long) b3 & 0xff) << 24) | //
                (((long) b2 & 0xff) << 16) | //
                (((long) b1 & 0xff) << 8) | //
                (((long) b0 & 0xff)));
    }

    public static short dencodeInt16(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeSort(//
                    bb._getByte(offset), //
                    bb._getByte(offset + 1));
        } else {
            return makeSort(//
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }

    public static int dencodeInt24(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeInt(//
                    bb._getByte(offset), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset + 2));
        } else {
            return makeInt(//
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }

    public static int dencodeInt32(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeInt(//
                    bb._getByte(offset), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 3));
        } else {
            return makeInt(//
                    bb._getByte(offset + 3), //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }

    public static long dencodeInt64(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeLong(//
                    bb._getByte(offset), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 3), //
                    bb._getByte(offset + 4), //
                    bb._getByte(offset + 5), //
                    bb._getByte(offset + 6), //
                    bb._getByte(offset + 7));
        } else {
            return makeLong(//
                    bb._getByte(offset + 7), //
                    bb._getByte(offset + 6), //
                    bb._getByte(offset + 5), //
                    bb._getByte(offset + 4), //
                    bb._getByte(offset + 3), //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }

    public static short dencodeUInt8(AbstractBytesIO bb, int offset) {
        return makeSort((byte) 0, bb._getByte(offset));
    }

    public static int dencodeUInt16(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeInt((byte) 0, //
                    bb._getByte(offset), //
                    bb._getByte(offset + 1));
        } else {
            return makeInt((byte) 0, //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }

    public static int dencodeUInt24(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeInt((byte) 0, //
                    bb._getByte(offset), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset + 2));
        } else {
            return makeInt((byte) 0, //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }

    public static long dencodeUInt32(AbstractBytesIO bb, int offset, boolean bigEndian) {
        if (bigEndian) {
            return makeLong((byte) 0, (byte) 0, (byte) 0, (byte) 0, //
                    bb._getByte(offset), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 3));
        } else {
            return makeLong((byte) 0, (byte) 0, (byte) 0, (byte) 0, //
                    bb._getByte(offset + 3), //
                    bb._getByte(offset + 2), //
                    bb._getByte(offset + 1), //
                    bb._getByte(offset));
        }
    }
}
