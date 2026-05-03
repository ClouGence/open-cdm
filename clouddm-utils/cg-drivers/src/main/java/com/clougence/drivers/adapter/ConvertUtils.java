/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.drivers.adapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConversionException;

public final class ConvertUtils {

    private static final LocalDateTime epochDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
    /** The set of strings that are known to map to Boolean.TRUE. */
    private static final String[]      trueStrings   = { "true", "yes", "y", "t", "on", "1" };
    /** The set of strings that are known to map to Boolean.FALSE. */
    private static final String[]      falseStrings  = { "false", "no", "n", "f", "off", "0" };
    private static final String[]      monthStrings1 = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    private static final String[]      monthStrings2 = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    private static final boolean       bigEndian     = true;
    // --------------------------------------------------------- Protected Methods

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/BigInteger
    public static String toBit(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v) ? "1" : "0";
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return null;
            }

            for (int i = 0; i < sv.length(); i++) {
                char c = sv.charAt(i);
                if (!(c == '1' || c == '0')) {
                    throw new ConversionException("Can't convert value '" + sv + "' to a Bit");
                }
            }

            return sv;
        } else if (v instanceof Number) {
            if (v instanceof Byte) {
                return HexadecimalUtils.bytes2bit(new byte[] { (Byte) v });
            } else if (v instanceof Short) {
                return HexadecimalUtils.bytes2bit(buildBytes(2, bb -> bb.putShort(0, (Short) v)));
            } else if (v instanceof Integer) {
                return HexadecimalUtils.bytes2bit(buildBytes(4, bb -> bb.putInt(0, (Integer) v)));
            } else if (v instanceof Long) {
                return HexadecimalUtils.bytes2bit(buildBytes(8, bb -> bb.putLong(0, (Long) v)));
            } else if (v instanceof BigInteger) {
                return HexadecimalUtils.bytes2bit(((BigInteger) v).toByteArray());
            }
        } else if (v instanceof byte[]) {
            return HexadecimalUtils.bytes2bit((byte[]) v);
        }

        throw new IllegalArgumentException("unsupported value type convert to Bit,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    public static Boolean toBoolean(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? false : null;
        }

        if (v instanceof Boolean) {
            return (Boolean) v;
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? false : null;
            }
            for (String trueString : trueStrings) {
                if (trueString.equals(sv)) {
                    return Boolean.TRUE;
                }
            }
            for (String falseString : falseStrings) {
                if (falseString.equals(sv)) {
                    return Boolean.FALSE;
                }
            }
            throw new ConversionException("Can't convert value '" + sv + "' to a Boolean");
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return ((Integer) v) != 0;
            } else if (v instanceof Long) {
                return ((Long) v) != 0;
            } else if (v instanceof Short) {
                return ((Short) v) != 0;
            } else if (v instanceof Byte) {
                return ((Byte) v) != 0;
            } else if (v instanceof Float) {
                return ((Float) v) != 0;
            } else if (v instanceof Double) {
                return ((Double) v) != 0;
            } else if (v instanceof BigDecimal) {
                return !v.equals(BigDecimal.ZERO);
            } else if (v instanceof BigInteger) {
                return !v.equals(BigInteger.ZERO);
            }
        } else if (v instanceof byte[]) {
            for (byte b : (byte[]) v) {
                if (b != 0) {
                    return true;
                }
            }
            return false;
        }

        throw new IllegalArgumentException("unsupported value type convert to Boolean,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] first byte to be return.
    public static Byte toByte(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? (byte) 0 : null;
        }

        if (v instanceof Boolean) {
            return (byte) ((Boolean) v ? 1 : 0);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? (byte) 0 : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return NumberUtils.createNumber(sv).byteValue();
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Byte");
            }

        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return ((Integer) v).byteValue();
            } else if (v instanceof Long) {
                return ((Long) v).byteValue();
            } else if (v instanceof Short) {
                return ((Short) v).byteValue();
            } else if (v instanceof Byte) {
                return (Byte) v;
            } else if (v instanceof Float) {
                return ((Float) v).byteValue();
            } else if (v instanceof Double) {
                return ((Double) v).byteValue();
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).byteValue();
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).byteValue();
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length > 0) {
                return ((byte[]) v)[0];
            } else {
                return (byte) 0;
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Byte,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] need 2 bytes(support BIG_ENDIAN or LITTLE_ENDIAN) to decode.
    public static Short toShort(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? (short) 0 : null;
        }

        if (v instanceof Boolean) {
            return (short) ((Boolean) v ? 1 : 0);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? (short) 0 : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return NumberUtils.createNumber(sv).shortValue();
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Short");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return ((Integer) v).shortValue();
            } else if (v instanceof Long) {
                return ((Long) v).shortValue();
            } else if (v instanceof Short) {
                return (Short) v;
            } else if (v instanceof Byte) {
                return ((Byte) v).shortValue();
            } else if (v instanceof Float) {
                return ((Float) v).shortValue();
            } else if (v instanceof Double) {
                return ((Double) v).shortValue();
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).shortValue();
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).shortValue();
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length >= 2) {
                ByteBuffer bb = ByteBuffer.wrap((byte[]) v);
                bb = bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                return bb.getShort();
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a Short");
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Short,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] need 4 bytes(support BIG_ENDIAN or LITTLE_ENDIAN) to decode.
    public static Integer toInteger(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? 0 : null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? 1 : 0);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? 0 : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return NumberUtils.createNumber(sv).intValue();
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Integer");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return (Integer) v;
            } else if (v instanceof Long) {
                return ((Long) v).intValue();
            } else if (v instanceof Short) {
                return ((Short) v).intValue();
            } else if (v instanceof Byte) {
                return ((Byte) v).intValue();
            } else if (v instanceof Float) {
                return ((Float) v).intValue();
            } else if (v instanceof Double) {
                return ((Double) v).intValue();
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).intValue();
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).intValue();
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length >= 4) {
                ByteBuffer bb = ByteBuffer.wrap((byte[]) v);
                bb = bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                return bb.getInt();
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a Integer");
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Integer,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] need 8 bytes(support BIG_ENDIAN or LITTLE_ENDIAN) to decode.
    public static Long toLong(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? 0L : null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? 1L : 0L);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? 0L : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return NumberUtils.createNumber(sv).longValue();
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Long");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return ((Integer) v).longValue();
            } else if (v instanceof Long) {
                return (Long) v;
            } else if (v instanceof Short) {
                return ((Short) v).longValue();
            } else if (v instanceof Byte) {
                return ((Byte) v).longValue();
            } else if (v instanceof Float) {
                return ((Float) v).longValue();
            } else if (v instanceof Double) {
                return ((Double) v).longValue();
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).longValue();
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).longValue();
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length >= 8) {
                ByteBuffer bb = ByteBuffer.wrap((byte[]) v);
                bb = bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                return bb.getLong();
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a Long");
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Long,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] need 4 bytes(support BIG_ENDIAN or LITTLE_ENDIAN), use IEEE 754 floating-point to decode.
    public static Float toFloat(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? 0F : null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? 1F : 0F);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? 0F : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return NumberUtils.createFloat(sv);
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Float");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return ((Integer) v).floatValue();
            } else if (v instanceof Long) {
                return ((Long) v).floatValue();
            } else if (v instanceof Short) {
                return ((Short) v).floatValue();
            } else if (v instanceof Byte) {
                return ((Byte) v).floatValue();
            } else if (v instanceof Float) {
                return (Float) v;
            } else if (v instanceof Double) {
                return ((Double) v).floatValue();
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).floatValue();
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).floatValue();
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length >= 4) {
                ByteBuffer bb = ByteBuffer.wrap((byte[]) v);
                bb = bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                return Float.intBitsToFloat(bb.getInt());
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a Float");
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Float,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] need 8 bytes(support BIG_ENDIAN or LITTLE_ENDIAN), use IEEE 754 floating-point to decode.
    public static Double toDouble(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? 0D : null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? 1D : 0D);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? 0D : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return NumberUtils.createDouble(sv);
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Double");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return ((Integer) v).doubleValue();
            } else if (v instanceof Long) {
                return ((Long) v).doubleValue();
            } else if (v instanceof Short) {
                return ((Short) v).doubleValue();
            } else if (v instanceof Byte) {
                return ((Byte) v).doubleValue();
            } else if (v instanceof Float) {
                return ((Float) v).doubleValue();
            } else if (v instanceof Double) {
                return (Double) v;
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).doubleValue();
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).doubleValue();
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length >= 8) {
                ByteBuffer bb = ByteBuffer.wrap((byte[]) v);
                bb = bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                return Double.longBitsToDouble(bb.getLong());
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a Double");
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Double,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] can not convert, missing encode.
    public static Character toChar(Object v, boolean primitive) {
        if (v == null) {
            return primitive ? '\0' : null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? 'T' : 'F');
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return primitive ? '\0' : null;
            }
            if (NumberUtils.isNumber(sv)) {
                return (char) NumberUtils.createInteger(sv).intValue();
            } else {
                throw new ConversionException("Can't convert value '" + sv + "' to a Character");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return (char) (((Integer) v).intValue());
            } else if (v instanceof Long) {
                return (char) ((Long) v).intValue();
            } else if (v instanceof Short) {
                return (char) ((Short) v).intValue();
            } else if (v instanceof Byte) {
                return (char) ((Byte) v).intValue();
            } else if (v instanceof Float) {
                return (char) ((Float) v).intValue();
            } else if (v instanceof Double) {
                return (char) ((Double) v).intValue();
            } else if (v instanceof BigDecimal) {
                return (char) ((BigDecimal) v).intValue();
            } else if (v instanceof BigInteger) {
                return (char) ((BigInteger) v).intValue();
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to Character,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] as BigInteger then as BigDecimal.
    public static BigDecimal toBigDecimal(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? BigDecimal.ONE : BigDecimal.ZERO);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return null;
            }
            if (NumberUtils.isNumber(sv)) {
                Number number = NumberUtils.createNumber(sv);
                if (number instanceof BigDecimal) {
                    return (BigDecimal) number;
                } else if (number instanceof BigInteger) {
                    return new BigDecimal((BigInteger) number);
                } else if (number instanceof Byte) {
                    return BigDecimal.valueOf(number.byteValue());
                } else if (number instanceof Short) {
                    return BigDecimal.valueOf(number.shortValue());
                } else if (number instanceof Integer) {
                    return BigDecimal.valueOf(number.intValue());
                } else if (number instanceof Long) {
                    return BigDecimal.valueOf(number.longValue());
                } else if (number instanceof Float) {
                    return BigDecimal.valueOf(number.floatValue());
                } else if (number instanceof Double) {
                    return BigDecimal.valueOf(number.doubleValue());
                }
            }
            throw new ConversionException("Can't convert value '" + sv + "' to a BigDecimal");
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length > 0) {
                return new BigDecimal(new BigInteger((byte[]) v));
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a BigDecimal");
            }
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return BigDecimal.valueOf((Integer) v);
            } else if (v instanceof Long) {
                return BigDecimal.valueOf((Long) v);
            } else if (v instanceof Short) {
                return BigDecimal.valueOf((Short) v);
            } else if (v instanceof Byte) {
                return BigDecimal.valueOf((Byte) v);
            } else if (v instanceof Float) {
                return BigDecimal.valueOf((Float) v);
            } else if (v instanceof Double) {
                return BigDecimal.valueOf((Double) v);
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v);
            } else if (v instanceof BigInteger) {
                return new BigDecimal((BigInteger) v);
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to BigDecimal,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger
    //  - byte[] as BigInteger.
    public static BigInteger toBigInteger(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Boolean) {
            return ((Boolean) v ? BigInteger.ONE : BigInteger.ZERO);
        } else if (v instanceof String) {
            String sv = (String) v;
            if (sv.isEmpty()) {
                return null;
            }
            if (NumberUtils.isNumber(sv)) {
                Number number = NumberUtils.createNumber(sv);
                if (number instanceof BigDecimal) {
                    return ((BigDecimal) number).toBigInteger();
                } else if (number instanceof BigInteger) {
                    return (BigInteger) number;
                } else if (number instanceof Byte) {
                    return BigInteger.valueOf(number.byteValue());
                } else if (number instanceof Short) {
                    return BigInteger.valueOf(number.shortValue());
                } else if (number instanceof Integer) {
                    return BigInteger.valueOf(number.intValue());
                } else if (number instanceof Long) {
                    return BigInteger.valueOf(number.longValue());
                } else if (number instanceof Float) {
                    return BigInteger.valueOf((long) number.floatValue());
                } else if (number instanceof Double) {
                    return BigInteger.valueOf((long) number.doubleValue());
                }
            }
            throw new ConversionException("Can't convert value '" + sv + "' to a BigInteger");

        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return BigInteger.valueOf((Integer) v);
            } else if (v instanceof Long) {
                return BigInteger.valueOf((Long) v);
            } else if (v instanceof Short) {
                return BigInteger.valueOf((Short) v);
            } else if (v instanceof Byte) {
                return BigInteger.valueOf((Byte) v);
            } else if (v instanceof Float) {
                return BigInteger.valueOf(((Float) v).longValue());
            } else if (v instanceof Double) {
                return BigInteger.valueOf(((Double) v).longValue());
            } else if (v instanceof BigDecimal) {
                return ((BigDecimal) v).toBigInteger();
            } else if (v instanceof BigInteger) {
                return (BigInteger) v;
            }
        } else if (v instanceof byte[]) {
            if (((byte[]) v).length > 0) {
                return new BigInteger((byte[]) v);
            } else {
                throw new ConversionException("Can't convert value '" + HexadecimalUtils.bytes2hex((byte[]) v) + "' to a BigInteger");
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to BigDecimal,type:" + v.getClass().getName());
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigDecimal/BigInteger/JdbcArray
    //  - byte[] as HexString.
    public static String toString(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Boolean) {
            return (((Boolean) v).toString());
        } else if (v instanceof String) {
            return (String) v;
        } else if (v instanceof byte[]) {
            return HexadecimalUtils.bytes2hex((byte[]) v);
        } else if (v instanceof BigDecimal) {
            return ((BigDecimal) v).toPlainString();
        } else if (v instanceof BigInteger) {
            return ((BigInteger) v).toString(10);
        } else if (v instanceof JdbcArray) {
            return StringUtils.join(((JdbcArray) v).getArray().toArray(), ", ");
        } else {
            return v.toString();
        }
    }

    // null/Boolean/String/Integer/Long/Short/Byte/byte[]/Float/Double/BigInteger
    //  - float\double use IEEE 754 floating-point to encode. (support BIG_ENDIAN or LITTLE_ENDIAN).
    //  - BigDecimal unsupport.
    //  - boolean as 0 or 1.
    public static byte[] toBytes(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Boolean) {
            return new byte[] { ((Boolean) v ? (byte) 1 : (byte) 0) };
        } else if (v instanceof String) {
            return ((String) v).getBytes();
        } else if (v instanceof byte[]) {
            return (byte[]) v;
        } else if (v instanceof Byte[]) {
            byte[] r = new byte[((Byte[]) v).length];
            for (int i = 0; i < ((Byte[]) v).length; i++) {
                r[i] = ((Byte[]) v)[i];
            }
            return r;
        } else if (v instanceof Number) {
            if (v instanceof Integer) {
                return buildBytes(4, bb -> bb.putInt((Integer) v));
            } else if (v instanceof Long) {
                return buildBytes(8, bb -> bb.putLong((Long) v));
            } else if (v instanceof Short) {
                return buildBytes(2, bb -> bb.putShort((Short) v));
            } else if (v instanceof Byte) {
                return new byte[] { (byte) v };
            } else if (v instanceof Float) {
                return buildBytes(4, bb -> bb.putFloat((Float) v));
            } else if (v instanceof Double) {
                return buildBytes(8, bb -> bb.putDouble((Double) v));
            } else if (v instanceof BigInteger) {
                return ((BigInteger) v).toByteArray();
            }
        }

        throw new IllegalArgumentException("unsupported value type convert to byte[],type:" + v.getClass().getName());
    }

    private static byte[] buildBytes(int size, Consumer<ByteBuffer> v) {
        byte[] bytes = new byte[size];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb = bb.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        v.accept(bb);
        return bytes;
    }

    // like toBytes
    public static Byte[] toBytesWrap(Object v) {
        byte[] bytes = toBytes(v);
        if (bytes == null) {
            return null;
        }

        final Byte[] objects = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            objects[i] = bytes[i];
        }
        return objects;
    }

    public static java.sql.Date toSqlDate(Object v) {
        if (v == null) {
            return null;
        }

        if (v.getClass() == java.sql.Date.class) {
            return (java.sql.Date) v;
        }

        if (v instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date) v).getTime());
        } else {
            return new java.sql.Date(toTimestampNum(v));
        }
    }

    public static java.util.Date toUtilDate(Object v) {
        if (v == null) {
            return null;
        }

        if (v.getClass() == java.util.Date.class) {
            return (java.util.Date) v;
        }

        if (v instanceof java.util.Date) {
            return new java.util.Date(((java.util.Date) v).getTime());
        } else {
            return new java.util.Date(toTimestampNum(v));
        }
    }

    public static java.sql.Time toSqlTime(Object v) {
        if (v == null) {
            return null;
        }

        if (v.getClass() == java.sql.Time.class) {
            return (java.sql.Time) v;
        }

        if (v instanceof java.util.Date) {
            return new java.sql.Time(((java.util.Date) v).getTime());
        } else {
            return new java.sql.Time(toTimestampNum(v));
        }
    }

    public static java.sql.Timestamp toSqlTimestamp(Object v) {
        if (v == null) {
            return null;
        }

        if (v.getClass() == java.sql.Timestamp.class) {
            return (java.sql.Timestamp) v;
        }

        if (v instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) v).getTime());
        } else {
            return java.sql.Timestamp.valueOf(toLocalDateTime(v));
        }
    }

    public static Instant toInstant(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Instant) {
            return (Instant) v;
        }

        if (v instanceof java.util.Date) {
            return ((java.util.Date) v).toInstant();
        } else {
            return Instant.ofEpochMilli(toTimestampNum(v));
        }
    }

    public static Calendar toCalendar(Object v) {
        if (v == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(toTimestampNum(v)));
            return calendar;
        }
    }

    private static long toTimestampNum(Object v) {
        if (v instanceof Temporal) {
            if (v instanceof LocalDateTime) {
                return java.time.Duration.between(epochDateTime, (LocalDateTime) v).toMillis();
            } else if (v instanceof LocalDate) {
                return java.time.Duration.between(epochDateTime, ((LocalDate) v).atStartOfDay()).toMillis();
            } else if (v instanceof LocalTime) {
                return ((LocalTime) v).toNanoOfDay() / 1000000;
            } else if (v instanceof OffsetDateTime) {
                return ((OffsetDateTime) v).toInstant().toEpochMilli();
            } else if (v instanceof OffsetTime) {
                return ((OffsetTime) v).toLocalTime().toNanoOfDay() / 1000000;
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).toInstant().toEpochMilli();
            } else if (v instanceof JapaneseDate) {
                LocalDateTime ldt = LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay()).atStartOfDay();
                return java.time.Duration.between(epochDateTime, ldt).toMillis();
            } else if (v instanceof YearMonth) {
                YearMonth yearMonth = (YearMonth) v;
                LocalDateTime ldt = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth().getValue(), 1).atStartOfDay();
                return java.time.Duration.between(epochDateTime, ldt).toMillis();
            } else if (v instanceof Instant) {
                return ((Instant) v).toEpochMilli();
            } else if (v instanceof Year) {
                LocalDateTime ldt = LocalDate.of(((Year) v).getValue(), 1, 1).atStartOfDay();
                return java.time.Duration.between(epochDateTime, ldt).toMillis();
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) v).getTime();
            } else if (v instanceof java.sql.Date) {
                return ((java.sql.Date) v).getTime();
            } else if (v instanceof java.sql.Time) {
                return ((java.sql.Time) v).getTime();
            } else {
                return ((Date) v).getTime();
            }
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                return NumberUtils.createNumber((String) v).longValue();
            } else {
                return stringToTimestamp((String) v).getTime();
            }
        } else if (v instanceof Calendar) {
            return ((Calendar) v).getTimeInMillis();
        } else if (v instanceof Number) {
            return ((Number) v).longValue();
        }

        throw new ConversionException("Can't convert value '" + v + "' to timestamp, type is " + v.getClass().getName());
    }

    // null/String/Integer/Long/Short/Byte/Float/Double/BigDecimal/BigInteger
    // java.sql.Timestamp/java.sql.Date/java.util.Date/LocalDateTime/LocalDate/JapaneseDate/YearMonth/Instant/Calendar/Year
    //  - return the year field.
    // OffsetDateTime/ZonedDateTime/
    //  - will ignore the time zone field and return the year
    public static Year toYear(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof Year) {
                return (Year) v;
            } else if (v instanceof LocalDateTime) {
                return Year.of(((LocalDateTime) v).getYear());
            } else if (v instanceof LocalDate) {
                return Year.of(((LocalDate) v).getYear());
            } else if (v instanceof OffsetDateTime) {
                return Year.of(((OffsetDateTime) v).getYear());
            } else if (v instanceof ZonedDateTime) {
                return Year.of(((ZonedDateTime) v).getYear());
            } else if (v instanceof JapaneseDate) {
                return Year.of(LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay()).getYear());
            } else if (v instanceof YearMonth) {
                return Year.of(((YearMonth) v).getYear());
            } else if (v instanceof Instant) {
                return calendarField(((Instant) v).toEpochMilli(), c -> Year.of(c.get(Calendar.YEAR) - 1900));
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return calendarField((java.sql.Timestamp) v, c -> Year.of(c.get(Calendar.YEAR) - 1900));
            } else if (v instanceof java.sql.Date) {
                return calendarField((java.sql.Date) v, c -> Year.of(c.get(Calendar.YEAR) - 1900));
            } else if (v instanceof java.sql.Time) {
                // skip
            } else {
                return calendarField((java.util.Date) v, c -> Year.of(c.get(Calendar.YEAR) - 1900));
            }
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                return calendarField(NumberUtils.createNumber((String) v).longValue(), c -> Year.of(c.get(Calendar.YEAR) - 1900));
            } else {
                LocalDateTime ldt = stringToLocalDateTime((String) v);
                if (ldt != null) {
                    return Year.of(ldt.getYear());
                }
            }
        } else if (v instanceof Calendar) {
            return Year.of(((Calendar) v).get(Calendar.YEAR));
        } else if (v instanceof Number) {
            return calendarField(((Number) v).longValue(), c -> Year.of(c.get(Calendar.YEAR) - 1900));
        }

        throw new ConversionException("Can't convert value '" + v + "' to a Year, type is " + v.getClass().getName());
    }

    // null/String/Integer/Long/Short/Byte/Float/Double/BigDecimal/BigInteger
    // java.sql.Timestamp/java.sql.Date/java.util.Date/LocalDateTime/LocalDate/JapaneseDate/YearMonth/Instant/Calendar
    //  - return the year field.
    // OffsetDateTime/ZonedDateTime/
    //  - will ignore the time zone field and return the month
    public static Month toMonth(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof LocalDateTime) {
                return ((LocalDateTime) v).getMonth();
            } else if (v instanceof LocalDate) {
                return ((LocalDate) v).getMonth();
            } else if (v instanceof OffsetDateTime) {
                return ((OffsetDateTime) v).getMonth();
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).getMonth();
            } else if (v instanceof JapaneseDate) {
                return LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay()).getMonth();
            } else if (v instanceof YearMonth) {
                return ((YearMonth) v).getMonth();
            } else if (v instanceof Instant) {
                return calendarField(((Instant) v).toEpochMilli(), c -> Month.of(c.get(Calendar.MONTH) + 1));
            }
        } else if (v instanceof Month) {
            return ((Month) v);
        } else if (v instanceof MonthDay) {
            return ((MonthDay) v).getMonth();
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return calendarField((java.sql.Timestamp) v, c -> Month.of(c.get(Calendar.MONTH) + 1));
            } else if (v instanceof java.sql.Date) {
                return calendarField((java.sql.Date) v, c -> Month.of(c.get(Calendar.MONTH) + 1));
            } else if (v instanceof java.sql.Time) {
                // skip
            } else {
                return calendarField((java.util.Date) v, c -> Month.of(c.get(Calendar.MONTH) + 1));
            }
        } else if (v instanceof String) {
            String str = (String) v;
            for (int i = 0; i < monthStrings1.length; i++) {
                if (StringUtils.equals(str, monthStrings1[i])) {
                    return Month.of(i + 1);
                }
            }
            for (int i = 0; i < monthStrings2.length; i++) {
                if (StringUtils.equals(str, monthStrings2[i])) {
                    return Month.of(i + 1);
                }
            }
            if (NumberUtils.isNumber((String) v)) {
                return calendarField(NumberUtils.createNumber((String) v).longValue(), c -> Month.of(c.get(Calendar.MONTH) + 1));
            } else {
                LocalDateTime ldt = stringToLocalDateTime((String) v);
                if (ldt != null) {
                    return ldt.getMonth();
                }
            }
        } else if (v instanceof Calendar) {
            return Month.of(((Calendar) v).get(Calendar.MONTH));
        } else if (v instanceof Number) {
            return calendarField(((Number) v).longValue(), c -> Month.of(c.get(Calendar.MONTH) + 1));
        }

        throw new ConversionException("Can't convert value '" + v + "' to a Month, type is " + v.getClass().getName());
    }

    public static YearMonth toYearMonth(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof YearMonth) {
                return ((YearMonth) v);
            } else if (v instanceof LocalDateTime) {
                return YearMonth.of(((LocalDateTime) v).getYear(), ((LocalDateTime) v).getMonth());
            } else if (v instanceof LocalDate) {
                return YearMonth.of(((LocalDate) v).getYear(), ((LocalDate) v).getMonth());
            } else if (v instanceof OffsetDateTime) {
                return YearMonth.of(((OffsetDateTime) v).getYear(), ((OffsetDateTime) v).getMonth());
            } else if (v instanceof ZonedDateTime) {
                return YearMonth.of(((ZonedDateTime) v).getYear(), ((ZonedDateTime) v).getMonth());
            } else if (v instanceof JapaneseDate) {
                LocalDate localDate = LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay());
                return YearMonth.of(localDate.getYear(), localDate.getMonth());
            } else if (v instanceof Instant) {
                return calendarField(((Instant) v).toEpochMilli(), c -> {
                    return YearMonth.of(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH) + 1);
                });
            }
        }
        if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return calendarField((java.sql.Timestamp) v, c -> {
                    return YearMonth.of(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH) + 1);
                });
            } else if (v instanceof java.sql.Date) {
                return calendarField((java.sql.Date) v, c -> {
                    return YearMonth.of(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH) + 1);
                });
            } else if (v instanceof java.sql.Time) {
                // skip
            } else {
                return calendarField((java.util.Date) v, c -> {
                    return YearMonth.of(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH) + 1);
                });
            }
        } else if (v instanceof Calendar) {
            Calendar calendar = (Calendar) v;
            return YearMonth.of(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH) + 1);
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                return calendarField(NumberUtils.createNumber((String) v).longValue(), c -> {
                    return YearMonth.of(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH) + 1);
                });
            } else {
                try {
                    return YearMonth.parse((String) v);
                } catch (Exception ignored) {
                    LocalDateTime ldt = stringToLocalDateTime((String) v);
                    if (ldt != null) {
                        return YearMonth.of(ldt.getYear(), ldt.getMonthValue());
                    }
                }
            }
        }

        throw new ConversionException("Can't convert value '" + v + "' to a YearMonth, type is " + v.getClass().getName());
    }

    private static <T> T calendarField(java.util.Date date, Function<Calendar, T> func) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        return func.apply(c);
    }

    private static <T> T calendarField(long milli, Function<Calendar, T> func) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milli);
        return func.apply(c);
    }

    public static LocalDateTime toLocalDateTime(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof Year) {
                return LocalDate.of(((Year) v).getValue(), 1, 1).atStartOfDay();
            } else if (v instanceof LocalDate) {
                return ((LocalDate) v).atStartOfDay();
            } else if (v instanceof LocalDateTime) {
                return (LocalDateTime) v;
            } else if (v instanceof OffsetDateTime) {
                return ((OffsetDateTime) v).toLocalDateTime();
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).toLocalDateTime();
            } else if (v instanceof JapaneseDate) {
                return LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay()).atStartOfDay();
            } else if (v instanceof YearMonth) {
                return LocalDate.of(((YearMonth) v).getYear(), ((YearMonth) v).getMonth(), 1).atStartOfDay();
            } else if (v instanceof Instant) {
                return epochDateTime.plusNanos(((Instant) v).toEpochMilli() * 1_000_000L);
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) v).toLocalDateTime();
            } else if (v instanceof java.sql.Date) {
                return ((java.sql.Date) v).toLocalDate().atStartOfDay();
            } else if (v instanceof java.sql.Time) {
                return ((Time) v).toLocalTime().atDate(epochDateTime.toLocalDate());
            } else {
                long millis = ((java.util.Date) v).getTime();
                return epochDateTime.plusNanos(millis * 1_000_000L);
            }
        } else if (v instanceof Calendar) {
            long millis = ((Calendar) v).toInstant().toEpochMilli();
            return epochDateTime.plusNanos(millis * 1_000_000L);
        } else if (v instanceof Number) {
            long millis = ((Number) v).longValue();
            return epochDateTime.plusNanos(millis * 1_000_000L);
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                long millis = NumberUtils.createNumber((String) v).longValue();
                return epochDateTime.plusNanos(millis * 1_000_000L);
            } else {
                LocalDateTime odt = stringToLocalDateTime((String) v);
                if (odt != null) {
                    return odt;
                }
            }
        }

        throw new ConversionException("Can't convert value '" + v + "' to a LocalDateTime, type is " + v.getClass().getName());
    }

    public static LocalDate toLocalDate(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof Year) {
                return LocalDate.of(((Year) v).getValue(), 1, 1);
            } else if (v instanceof LocalDate) {
                return (LocalDate) v;
            } else if (v instanceof LocalDateTime) {
                return ((LocalDateTime) v).toLocalDate();
            } else if (v instanceof OffsetDateTime) {
                return ((OffsetDateTime) v).toLocalDate();
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).toLocalDate();
            } else if (v instanceof JapaneseDate) {
                return LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay());
            } else if (v instanceof YearMonth) {
                return LocalDate.of(((YearMonth) v).getYear(), ((YearMonth) v).getMonth(), 1);
            } else if (v instanceof Instant) {
                return LocalDate.ofEpochDay(((Instant) v).toEpochMilli() / (24 * 60 * 60 * 1000L));
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) v).toLocalDateTime().toLocalDate();
            } else if (v instanceof java.sql.Date) {
                return ((java.sql.Date) v).toLocalDate();
            } else if (v instanceof java.sql.Time) {
                // skip
            } else {
                return LocalDate.ofEpochDay(((java.util.Date) v).toInstant().toEpochMilli() / (24 * 60 * 60 * 1000L));
            }
        } else if (v instanceof Calendar) {
            return LocalDate.ofEpochDay(((Calendar) v).toInstant().toEpochMilli() / (24 * 60 * 60 * 1000L));
        } else if (v instanceof Number) {
            return LocalDate.ofEpochDay(((Number) v).longValue() / (24 * 60 * 60 * 1000L));
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                return calendarField(NumberUtils.createNumber((String) v).longValue(), c -> {
                    return LocalDate.ofEpochDay(c.getTimeInMillis() / (24 * 60 * 60 * 1000L));
                });
            } else {
                LocalDateTime ldt = stringToLocalDateTime((String) v);
                if (ldt != null) {
                    return ldt.toLocalDate();
                }
            }
        }

        throw new ConversionException("Can't convert value '" + v + "' to a LocalDate, type is " + v.getClass().getName());
    }

    public static LocalTime toLocalTime(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof LocalTime) {
                return (LocalTime) v;
            } else if (v instanceof LocalDateTime) {
                return ((LocalDateTime) v).toLocalTime();
            } else if (v instanceof OffsetDateTime) {
                return ((OffsetDateTime) v).toLocalTime();
            } else if (v instanceof OffsetTime) {
                return ((OffsetTime) v).toLocalTime();
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).toLocalTime();
            } else if (v instanceof Instant) {
                long millisOfDay = ((Instant) v).toEpochMilli() % (24 * 60 * 60 * 1000L);
                return LocalTime.ofNanoOfDay(millisOfDay * 1_000_000L);
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return ((Timestamp) v).toLocalDateTime().toLocalTime();
            } else if (v instanceof java.sql.Date) {
                return calendarField(((java.sql.Date) v).getTime(), c -> LocalTime.ofNanoOfDay(c.getTimeInMillis() * 1_000_000L));
            } else if (v instanceof java.sql.Time) {
                return ((Time) v).toLocalTime();
            } else {
                return calendarField(((java.util.Date) v).getTime(), c -> LocalTime.ofNanoOfDay(c.getTimeInMillis() * 1_000_000L));
            }
        } else if (v instanceof Calendar) {
            return calendarField(((Calendar) v).getTimeInMillis(), c -> LocalTime.ofNanoOfDay(c.getTimeInMillis() * 1_000_000L));
        } else if (v instanceof Number) {
            return LocalTime.ofNanoOfDay(((Number) v).longValue() * 1_000_000L);
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                return calendarField(NumberUtils.createNumber((String) v).longValue(), c -> LocalTime.ofNanoOfDay(c.getTimeInMillis() * 1_000_000L));
            } else {
                LocalDateTime ldt = stringToLocalDateTime((String) v);
                if (ldt != null) {
                    return ldt.toLocalTime();
                }
            }
        }

        throw new ConversionException("Can't convert value '" + v + "' to a LocalTime, type is " + v.getClass().getName());
    }

    public static JapaneseDate toJapaneseDate(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof JapaneseDate) {
            return (JapaneseDate) v;
        } else {
            return JapaneseDate.from(toLocalDate(v));
        }
    }

    public static OffsetTime toOffsetTime(Object v, ZoneOffset defaultZone) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof LocalTime) {
                return ((LocalTime) v).atOffset(defaultZone);
            } else if (v instanceof LocalDateTime) {
                return ((LocalDateTime) v).atOffset(defaultZone).toOffsetTime();
            } else if (v instanceof OffsetDateTime) {
                return ((OffsetDateTime) v).toOffsetTime();
            } else if (v instanceof OffsetTime) {
                return (OffsetTime) v;
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).toOffsetDateTime().toOffsetTime();
            } else if (v instanceof Instant) {
                long millisOfDay = ((Instant) v).toEpochMilli() % (24 * 60 * 60 * 1000L);
                return OffsetTime.of(LocalTime.ofNanoOfDay(millisOfDay * 1_000_000L), defaultZone);
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) v).toInstant().atOffset(defaultZone).toOffsetTime();
            } else if (v instanceof java.sql.Date) {
                return ((java.sql.Date) v).toInstant().atOffset(defaultZone).toOffsetTime();
            } else if (v instanceof java.sql.Time) {
                return ((java.sql.Time) v).toLocalTime().atOffset(defaultZone);
            } else {
                return ((java.util.Date) v).toInstant().atOffset(defaultZone).toOffsetTime();
            }
        } else if (v instanceof Calendar) {
            return calendarField(((Calendar) v).getTimeInMillis(), c -> {
                return LocalTime.ofNanoOfDay(c.getTimeInMillis() * 1_000_000L).atOffset(defaultZone);
            });
        } else if (v instanceof Number) {
            return LocalTime.ofNanoOfDay(((Number) v).longValue() * 1_000_000L).atOffset(defaultZone);
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                return calendarField(NumberUtils.createNumber((String) v).longValue(), c -> {
                    return LocalTime.ofNanoOfDay(c.getTimeInMillis() * 1_000_000L).atOffset(defaultZone);
                });
            } else {
                OffsetDateTime odt = stringToOffsetDateTime((String) v, defaultZone);
                if (odt != null) {
                    return odt.toOffsetTime();
                }
            }
        }

        throw new ConversionException("Can't convert value '" + v + "' to a OffsetTime, type is " + v.getClass().getName());
    }

    public static OffsetDateTime toOffsetDateTime(Object v, ZoneOffset defaultZone) {
        if (v == null) {
            return null;
        }

        if (v instanceof Temporal) {
            if (v instanceof Year) {
                return LocalDate.of(((Year) v).getValue(), 1, 1).atStartOfDay().atOffset(defaultZone);
            } else if (v instanceof LocalDate) {
                return ((LocalDate) v).atStartOfDay().atOffset(defaultZone);
            } else if (v instanceof LocalDateTime) {
                return ((LocalDateTime) v).atOffset(defaultZone);
            } else if (v instanceof OffsetDateTime) {
                return (OffsetDateTime) v;
            } else if (v instanceof ZonedDateTime) {
                return ((ZonedDateTime) v).toOffsetDateTime();
            } else if (v instanceof JapaneseDate) {
                return LocalDate.ofEpochDay(((JapaneseDate) v).toEpochDay()).atStartOfDay().atOffset(defaultZone);
            } else if (v instanceof YearMonth) {
                return LocalDate.of(((YearMonth) v).getYear(), ((YearMonth) v).getMonth(), 1).atStartOfDay().atOffset(defaultZone);
            } else if (v instanceof Instant) {
                return epochDateTime.plusNanos(((Instant) v).toEpochMilli() * 1_000_000L).atOffset(defaultZone);
            }
        } else if (v instanceof java.util.Date) {
            if (v instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) v).toLocalDateTime().atOffset(defaultZone);
            } else if (v instanceof java.sql.Date) {
                return ((java.sql.Date) v).toLocalDate().atStartOfDay().atOffset(defaultZone);
            } else if (v instanceof java.sql.Time) {
                return ((Time) v).toLocalTime().atDate(epochDateTime.toLocalDate()).atOffset(defaultZone);
            } else {
                long millis = ((java.util.Date) v).getTime();
                return epochDateTime.plusNanos(millis * 1_000_000L).atOffset(defaultZone);
            }
        } else if (v instanceof Calendar) {
            long millis = ((Calendar) v).toInstant().toEpochMilli();
            return epochDateTime.plusNanos(millis * 1_000_000L).atOffset(defaultZone);
        } else if (v instanceof Number) {
            long millis = ((Number) v).longValue();
            return epochDateTime.plusNanos(millis * 1_000_000L).atOffset(defaultZone);
        } else if (v instanceof String) {
            if (NumberUtils.isNumber((String) v)) {
                long millis = NumberUtils.createNumber((String) v).longValue();
                return epochDateTime.plusNanos(millis * 1_000_000L).atOffset(defaultZone);
            } else {
                OffsetDateTime odt = stringToOffsetDateTime((String) v, defaultZone);
                if (odt != null) {
                    return odt;
                }
            }
        }

        throw new ConversionException("Can't convert value '" + v + "' to a OffsetDateTime, type is " + v.getClass().getName());
    }

    public static ZonedDateTime toZonedDateTime(Object v, ZoneOffset defaultZone) {
        return v == null ? null : toOffsetDateTime(v, defaultZone).toZonedDateTime();
    }

    private static OffsetDateTime stringToOffsetDateTime(String v, ZoneOffset defaultZone) {
        LocalDateTime localDateTime = stringToLocalDateTime(v);
        ZoneOffset useZone = defaultZone;

        String zoneValue = extractZone(v);
        if (StringUtils.isNotBlank(zoneValue)) {
            return OffsetDateTime.of(localDateTime, ZoneOffset.of(zoneValue));
        } else {
            return OffsetDateTime.of(localDateTime, useZone);
        }
    }

    // 2021-07-09 18:05:15.000000+12:33 -> +12:33
    // 2021-07-09 18:05:15.000000Z      -> +00:00
    //            18:05:15.000000+12:33 -> +12:33
    //            18:05:15.000000Z      -> +00:00
    // 2021-07-09                       ->
    public static String extractZone(String datetime) {
        String onlyTime = datetime;
        if (StringUtils.isBlank(datetime)) {
            return "";
        } else if (onlyTime.charAt(0) == '-') {
            onlyTime = onlyTime.substring(1); // maybe -123:05:15.000000+14:88
        }

        if (datetime.charAt(datetime.length() - 1) == 'Z') {
            return "+00:00";
        }

        int len = onlyTime.length();
        if (len >= 7) {
            char checkCharAt1 = datetime.charAt(len - 2); //+h
            char checkCharAt2 = datetime.charAt(len - 3); //+hh
            char checkCharAt3 = datetime.charAt(len - 6); //+hh:mm
            char checkCharAt4 = datetime.charAt(len - 5); //+hhmm
            char checkCharAt5 = len >= 9 ? datetime.charAt(len - 9) : ' '; //+hh:mm:ss
            char checkCharAt6 = datetime.charAt(len - 7); //+hhmmss

            boolean matchTest1 = (checkCharAt1 == '+' || checkCharAt1 == '-') && len >= 10;
            boolean matchTest2 = (checkCharAt2 == '+' || checkCharAt2 == '-') && len >= 11;
            boolean matchTest3 = ((checkCharAt3 == '+' || checkCharAt3 == '-') && checkCharAt2 == ':');
            boolean matchTest4 = (checkCharAt4 == '+' || checkCharAt4 == '-') && checkCharAt2 == ':'; //+h:mm
            boolean matchTest5 = (checkCharAt5 == '+' || checkCharAt5 == '-') && checkCharAt2 == ':' && checkCharAt3 == ':';
            boolean matchTest6 = (checkCharAt6 == '+' || checkCharAt6 == '-');
            boolean matchTest7 = (checkCharAt4 == '+' || checkCharAt4 == '-');

            if (matchTest1) {
                String zoneData = datetime.substring(len - 2);
                return ZoneOffset.of(zoneData).toString();
            } else if (matchTest2) {
                String zoneData = datetime.substring(len - 3);
                return ZoneOffset.of(zoneData).toString();
            } else if (matchTest3) {
                String zoneData = datetime.substring(len - 6);
                return ZoneOffset.of(zoneData).toString();
            } else if (matchTest4) {
                String zoneData = checkCharAt4 + "0" + datetime.substring(len - 4).trim();
                return ZoneOffset.of(zoneData).toString();
            } else if (matchTest5) {
                String zoneData = datetime.substring(len - 9);
                return ZoneOffset.of(zoneData).toString();
            } else if (matchTest6) {
                String zoneData = datetime.substring(len - 7);
                return ZoneOffset.of(zoneData).toString();
            } else if (matchTest7) {
                String zoneData = datetime.substring(len - 5);
                return ZoneOffset.of(zoneData).toString();
            }
        }
        return "";
    }

    private static Timestamp stringToTimestamp(String v) {
        return Timestamp.valueOf(stringToLocalDateTime(v));
    }

    private static LocalDateTime stringToLocalDateTime(String value) {
        String dateStr = value;
        if (dateStr.trim().length() < 4) {
            if (StringUtils.isNumeric(dateStr)) {
                return LocalDateTime.of(toInt(dateStr), 1, 1, 0, 0, 0, 0);
            } else {
                throw new ConversionException("Can't convert value '" + value + "' to a LocalDateTime");
            }
        }

        // remove zone
        dateStr = removeZone(dateStr);
        int len = dateStr.length();

        if (len == 4) {
            return LocalDateTime.of(toInt(dateStr), 1, 1, 0, 0, 0, 0);
        } else if (dateStr.charAt(4) == '-') {
            switch (len) {
                case 7: {
                    String[] dataParts = dateStr.split("-");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), 1, 0, 0, 0, 0);
                }
                case 10: {
                    String[] dataParts = dateStr.split("-");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), 0, 0, 0, 0);
                }
                case 13: {
                    String[] dataTime = dateStr.split(" ");
                    String[] dataParts = dataTime[0].split("-");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), toInt(dataTime[1]), 0, 0, 0);
                }
                case 16: {
                    String[] dataTime = dateStr.split(" ");
                    if (dataTime.length == 1) {
                        dataTime = dateStr.split("T");
                    }

                    String[] dataParts = dataTime[0].split("-");
                    String[] timeParts = dataTime[1].split(":");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), toInt(timeParts[0]), toInt(timeParts[1]), 0, 0);
                }
                case 19: {
                    String[] dataTime = dateStr.split(" ");
                    if (dataTime.length == 1) {
                        dataTime = dateStr.split("T");
                    }

                    String[] dataParts = dataTime[0].split("-");
                    String[] timeParts = dataTime[1].split(":");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), toInt(timeParts[0]), toInt(timeParts[1]), toInt(timeParts[2]), 0);
                }
                default: {
                    String[] dataTime = dateStr.split(" ");
                    if (dataTime.length == 1) {
                        dataTime = dateStr.split("T");
                    }

                    String[] dataParts = dataTime[0].split("-");
                    String[] timeParts = dataTime[1].split(":");
                    String[] secondParts = timeParts[2].split("\\.");
                    secondParts[1] = StringUtils.rightPad(secondParts[1], 9, "0");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), //
                            toInt(timeParts[0]), toInt(timeParts[1]), toInt(secondParts[0]), toInt(secondParts[1]));
                }
            }
        } else if (dateStr.charAt(2) == ':') {
            switch (len) {
                case 5: {
                    String[] timeParts = dateStr.split(":");
                    return LocalDateTime.of(1970, 1, 1, toInt(timeParts[0]), toInt(timeParts[1]), 0, 0);
                }
                case 8: {
                    String[] timeParts = dateStr.split(":");
                    return LocalDateTime.of(1970, 1, 1, toInt(timeParts[0]), toInt(timeParts[1]), toInt(timeParts[2]), 0);
                }
                default: {
                    String[] timeParts = dateStr.split(":");
                    String[] secondParts = timeParts[2].split("\\.");
                    secondParts[1] = StringUtils.rightPad(secondParts[1], 9, "0");
                    return LocalDateTime.of(1970, 1, 1, toInt(timeParts[0]), toInt(timeParts[1]), toInt(secondParts[0]), toInt(secondParts[1]));
                }
            }
        } else {
            throw new ConversionException("Can't convert value '" + value + "' to a LocalDateTime");
        }
    }

    private static String removeZone(String datetime) {
        if (datetime == null || datetime.isEmpty()) {
            return datetime;
        }
        // remove zone
        int len = datetime.length();
        boolean isUTC = datetime.charAt(len - 1) == 'Z' || datetime.charAt(len - 1) == 'z';
        if (isUTC) {
            return datetime.substring(0, len - 1).trim();
        } else if (len >= 7) {
            char checkCharAt1 = datetime.charAt(len - 2); //+h
            char checkCharAt2 = datetime.charAt(len - 3); //+hh
            char checkCharAt3 = datetime.charAt(len - 6); //+hh:mm
            char checkCharAt4 = datetime.charAt(len - 5); //+hhmm
            char checkCharAt5 = len >= 9 ? datetime.charAt(len - 9) : ' '; //+hh:mm:ss
            char checkCharAt6 = datetime.charAt(len - 7); //+hhmmss

            boolean matchTest1 = (checkCharAt1 == '+' || checkCharAt1 == '-') && len >= 10;
            boolean matchTest2 = (checkCharAt2 == '+' || checkCharAt2 == '-') && len >= 11;
            boolean matchTest3 = ((checkCharAt3 == '+' || checkCharAt3 == '-') && checkCharAt2 == ':');
            boolean matchTest4 = (checkCharAt4 == '+' || checkCharAt4 == '-') && checkCharAt2 == ':'; //+h:mm
            boolean matchTest5 = (checkCharAt5 == '+' || checkCharAt5 == '-') && checkCharAt2 == ':' && checkCharAt3 == ':';
            boolean matchTest6 = (checkCharAt6 == '+' || checkCharAt6 == '-');
            boolean matchTest7 = (checkCharAt4 == '+' || checkCharAt4 == '-');

            if (matchTest1) {
                return datetime.substring(0, len - 2).trim();
            } else if (matchTest2) {
                return datetime.substring(0, len - 3).trim();
            } else if (matchTest3) {
                return datetime.substring(0, len - 6).trim();
            } else if (matchTest4) {
                return datetime.substring(0, len - 5).trim();
            } else if (matchTest5) {
                return datetime.substring(0, len - 9).trim();
            } else if (matchTest6) {
                return datetime.substring(0, len - 7).trim();
            } else if (matchTest7) {
                return datetime.substring(0, len - 5).trim();
            }
        }
        return datetime;
    }

    private static int toInt(String intStr) {
        return Integer.parseInt(intStr);
    }
}
