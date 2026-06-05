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
package com.clougence.utils.reflect.annotations;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.clougence.utils.reflect.Annotation;
import com.clougence.utils.reflect.Annotations;

public class TypeHasArrayTest {

    @Test
    public void booleanArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasBooleanArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayBooleanDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 2;
        assert annotation.getBooleanArray("value").get(0);
        assert !annotation.getBooleanArray("value").get(1);
        assert annotation.getByte("value") == 1;
        assert annotation.getByteArray("value").size() == 2;
        assert annotation.getByteArray("value").get(0) == 1;
        assert annotation.getByteArray("value").get(1) == 0;
        assert annotation.getShort("value") == 1;
        assert annotation.getShortArray("value").size() == 2;
        assert annotation.getShortArray("value").get(0) == 1;
        assert annotation.getShortArray("value").get(1) == 0;
        assert annotation.getInt("value") == 1;
        assert annotation.getIntArray("value").size() == 2;
        assert annotation.getIntArray("value").get(0) == 1;
        assert annotation.getIntArray("value").get(1) == 0;
        assert annotation.getLong("value") == 1L;
        assert annotation.getLongArray("value").size() == 2;
        assert annotation.getLongArray("value").get(0) == 1L;
        assert annotation.getLongArray("value").get(1) == 0L;
        assert annotation.getFloat("value") == 1.0f;
        assert annotation.getFloatArray("value").size() == 2;
        assert annotation.getFloatArray("value").get(0) == 1.0f;
        assert annotation.getFloatArray("value").get(1) == 0.0f;
        assert annotation.getDouble("value") == 1.0d;
        assert annotation.getDoubleArray("value").size() == 2;
        assert annotation.getDoubleArray("value").get(0) == 1.0d;
        assert annotation.getDoubleArray("value").get(1) == 0.0d;
        assert annotation.getChar("value") == 't';
        assert annotation.getCharArray("value").size() == 2;
        assert annotation.getCharArray("value").get(0).equals('t');
        assert annotation.getCharArray("value").get(1).equals('f');
        assert annotation.getString("value").equals("true");
        assert annotation.getStringArray("value").size() == 2;
        assert annotation.getStringArray("value").get(0).equals("true");
        assert annotation.getStringArray("value").get(1).equals("false");
    }

    @Test
    public void byteArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasByteArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayByteDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 3;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getBooleanArray("value").get(2);
        assert annotation.getByte("value") == 123;
        assert annotation.getByteArray("value").size() == 3;
        assert annotation.getByteArray("value").get(0) == 123;
        assert annotation.getByteArray("value").get(1) == Byte.MAX_VALUE;
        assert annotation.getByteArray("value").get(2) == Byte.MIN_VALUE;
        assert annotation.getShort("value") == 123;
        assert annotation.getShortArray("value").size() == 3;
        assert annotation.getShortArray("value").get(0) == 123;
        assert annotation.getShortArray("value").get(1) == Byte.MAX_VALUE;
        assert annotation.getShortArray("value").get(2) == Byte.MIN_VALUE;
        assert annotation.getInt("value") == 123;
        assert annotation.getIntArray("value").size() == 3;
        assert annotation.getIntArray("value").get(0) == 123;
        assert annotation.getIntArray("value").get(1) == Byte.MAX_VALUE;
        assert annotation.getIntArray("value").get(2) == Byte.MIN_VALUE;
        assert annotation.getLong("value") == 123;
        assert annotation.getLongArray("value").size() == 3;
        assert annotation.getLongArray("value").get(0) == 123;
        assert annotation.getLongArray("value").get(1) == Byte.MAX_VALUE;
        assert annotation.getLongArray("value").get(2) == Byte.MIN_VALUE;
        assert annotation.getFloat("value") == 123f;
        assert annotation.getFloatArray("value").size() == 3;
        assert annotation.getFloatArray("value").get(0) == 123f;
        assert annotation.getFloatArray("value").get(1) == Byte.MAX_VALUE;
        assert annotation.getFloatArray("value").get(2) == Byte.MIN_VALUE;
        assert annotation.getDouble("value") == 123d;
        assert annotation.getDoubleArray("value").size() == 3;
        assert annotation.getDoubleArray("value").get(0) == 123d;
        assert annotation.getDoubleArray("value").get(1) == Byte.MAX_VALUE;
        assert annotation.getDoubleArray("value").get(2) == Byte.MIN_VALUE;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 3;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('1');
        assert annotation.getCharArray("value").get(2).equals('-');
        assert annotation.getString("value").equals("123");
        assert annotation.getStringArray("value").size() == 3;
        assert annotation.getStringArray("value").get(0).equals("123");
        assert annotation.getStringArray("value").get(1).equals("127");
        assert annotation.getStringArray("value").get(2).equals("-128");
    }

    @Test
    public void shortArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasShortArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayShortDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 3;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getBooleanArray("value").get(2);
        assert annotation.getByte("value") == 0; // out of range
        assert annotation.getByteArray("value").size() == 3;
        assert annotation.getByteArray("value").get(0) == 0; // out of range
        assert annotation.getByteArray("value").get(1) == 0; // out of range
        assert annotation.getByteArray("value").get(2) == 0; // out of range
        assert annotation.getShort("value") == 12345;
        assert annotation.getShortArray("value").size() == 3;
        assert annotation.getShortArray("value").get(0) == 12345;
        assert annotation.getShortArray("value").get(1) == Short.MAX_VALUE;
        assert annotation.getShortArray("value").get(2) == Short.MIN_VALUE;
        assert annotation.getInt("value") == 12345;
        assert annotation.getIntArray("value").size() == 3;
        assert annotation.getIntArray("value").get(0) == 12345;
        assert annotation.getIntArray("value").get(1) == Short.MAX_VALUE;
        assert annotation.getIntArray("value").get(2) == Short.MIN_VALUE;
        assert annotation.getLong("value") == 12345;
        assert annotation.getLongArray("value").size() == 3;
        assert annotation.getLongArray("value").get(0) == 12345;
        assert annotation.getLongArray("value").get(1) == Short.MAX_VALUE;
        assert annotation.getLongArray("value").get(2) == Short.MIN_VALUE;
        assert annotation.getFloat("value") == 12345f;
        assert annotation.getFloatArray("value").size() == 3;
        assert annotation.getFloatArray("value").get(0) == 12345f;
        assert annotation.getFloatArray("value").get(1) == Short.MAX_VALUE;
        assert annotation.getFloatArray("value").get(2) == Short.MIN_VALUE;
        assert annotation.getDouble("value") == 12345d;
        assert annotation.getDoubleArray("value").size() == 3;
        assert annotation.getDoubleArray("value").get(0) == 12345d;
        assert annotation.getDoubleArray("value").get(1) == Short.MAX_VALUE;
        assert annotation.getDoubleArray("value").get(2) == Short.MIN_VALUE;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 3;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('3');
        assert annotation.getCharArray("value").get(2).equals('-');
        assert annotation.getString("value").equals("12345");
        assert annotation.getStringArray("value").size() == 3;
        assert annotation.getStringArray("value").get(0).equals("12345");
        assert annotation.getStringArray("value").get(1).equals("32767");
        assert annotation.getStringArray("value").get(2).equals("-32768");
    }

    @Test
    public void intArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasIntegerArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayIntegerDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 3;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getBooleanArray("value").get(2);
        assert annotation.getByte("value") == 0; // out of range
        assert annotation.getByteArray("value").size() == 3;
        assert annotation.getByteArray("value").get(0) == 0; // out of range
        assert annotation.getByteArray("value").get(1) == 0; // out of range
        assert annotation.getByteArray("value").get(2) == 0; // out of range
        assert annotation.getShort("value") == 0;
        assert annotation.getShortArray("value").size() == 3;
        assert annotation.getShortArray("value").get(0) == 0; // out of range
        assert annotation.getShortArray("value").get(1) == 0; // out of range
        assert annotation.getShortArray("value").get(2) == 0; // out of range
        assert annotation.getInt("value") == 1234567890;
        assert annotation.getIntArray("value").size() == 3;
        assert annotation.getIntArray("value").get(0) == 1234567890;
        assert annotation.getIntArray("value").get(1) == Integer.MAX_VALUE;
        assert annotation.getIntArray("value").get(2) == Integer.MIN_VALUE;
        assert annotation.getLong("value") == 1234567890;
        assert annotation.getLongArray("value").size() == 3;
        assert annotation.getLongArray("value").get(0) == 1234567890;
        assert annotation.getLongArray("value").get(1) == Integer.MAX_VALUE;
        assert annotation.getLongArray("value").get(2) == Integer.MIN_VALUE;
        assert annotation.getFloat("value") == 1234567890f;
        assert annotation.getFloatArray("value").size() == 3;
        assert annotation.getFloatArray("value").get(0) == 1234567890f;
        assert annotation.getFloatArray("value").get(1) == Integer.MAX_VALUE;
        assert annotation.getFloatArray("value").get(2) == Integer.MIN_VALUE;
        assert annotation.getDouble("value") == 1234567890d;
        assert annotation.getDoubleArray("value").size() == 3;
        assert annotation.getDoubleArray("value").get(0) == 1234567890d;
        assert annotation.getDoubleArray("value").get(1) == Integer.MAX_VALUE;
        assert annotation.getDoubleArray("value").get(2) == Integer.MIN_VALUE;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 3;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('2');
        assert annotation.getCharArray("value").get(2).equals('-');
        assert annotation.getString("value").equals("1234567890");
        assert annotation.getStringArray("value").size() == 3;
        assert annotation.getStringArray("value").get(0).equals("1234567890");
        assert annotation.getStringArray("value").get(1).equals("2147483647");
        assert annotation.getStringArray("value").get(2).equals("-2147483648");
    }

    @Test
    public void longArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasLongArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayLongDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 3;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getBooleanArray("value").get(2);
        assert annotation.getByte("value") == 0; // out of range
        assert annotation.getByteArray("value").size() == 3;
        assert annotation.getByteArray("value").get(0) == 0; // out of range
        assert annotation.getByteArray("value").get(1) == 0; // out of range
        assert annotation.getByteArray("value").get(2) == 0; // out of range
        assert annotation.getShort("value") == 0; // out of range
        assert annotation.getShortArray("value").size() == 3;
        assert annotation.getShortArray("value").get(0) == 0; // out of range
        assert annotation.getShortArray("value").get(1) == 0; // out of range
        assert annotation.getShortArray("value").get(2) == 0; // out of range
        assert annotation.getInt("value") == 0; // out of range
        assert annotation.getIntArray("value").size() == 3;
        assert annotation.getIntArray("value").get(0) == 0; // out of range
        assert annotation.getIntArray("value").get(1) == 0; // out of range
        assert annotation.getIntArray("value").get(2) == 0; // out of range
        assert annotation.getLong("value") == 1234567890123456789L;
        assert annotation.getLongArray("value").size() == 3;
        assert annotation.getLongArray("value").get(0) == 1234567890123456789L;
        assert annotation.getLongArray("value").get(1) == Long.MAX_VALUE;
        assert annotation.getLongArray("value").get(2) == Long.MIN_VALUE;
        assert annotation.getFloat("value") == 1234567890123456789f;
        assert annotation.getFloatArray("value").size() == 3;
        assert annotation.getFloatArray("value").get(0) == 1234567890123456789f;
        assert annotation.getFloatArray("value").get(1) == Long.MAX_VALUE;
        assert annotation.getFloatArray("value").get(2) == Long.MIN_VALUE;
        assert annotation.getDouble("value") == 1234567890123456789d;
        assert annotation.getDoubleArray("value").size() == 3;
        assert annotation.getDoubleArray("value").get(0) == 1234567890123456789d;
        assert annotation.getDoubleArray("value").get(1) == Long.MAX_VALUE;
        assert annotation.getDoubleArray("value").get(2) == Long.MIN_VALUE;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 3;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('9');
        assert annotation.getCharArray("value").get(2).equals('-');
        assert annotation.getString("value").equals("1234567890123456789");
        assert annotation.getStringArray("value").size() == 3;
        assert annotation.getStringArray("value").get(0).equals("1234567890123456789");
        assert annotation.getStringArray("value").get(1).equals("9223372036854775807");
        assert annotation.getStringArray("value").get(2).equals("-9223372036854775808");
    }

    @Test
    public void floatArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasFloatArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayFloatDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 3;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getBooleanArray("value").get(2);
        assert annotation.getByte("value") == 0; // out of range
        assert annotation.getByteArray("value").size() == 3;
        assert annotation.getByteArray("value").get(0) == 0; // out of range
        assert annotation.getByteArray("value").get(1) == 0; // out of range
        assert annotation.getByteArray("value").get(2) == 0; // out of range
        assert annotation.getShort("value") == 1234;
        assert annotation.getShortArray("value").size() == 3;
        assert annotation.getShortArray("value").get(0) == 1234;
        assert annotation.getShortArray("value").get(1) == 0; // out of range
        assert annotation.getShortArray("value").get(2) == 0; // out of range
        assert annotation.getInt("value") == 1234;
        assert annotation.getIntArray("value").size() == 3;
        assert annotation.getIntArray("value").get(0) == 1234;
        assert annotation.getIntArray("value").get(1) == 0; // out of range
        assert annotation.getIntArray("value").get(2) == 0; // out of range
        assert annotation.getLong("value") == 1234L;
        assert annotation.getLongArray("value").size() == 3;
        assert annotation.getLongArray("value").get(0) == 1234L;
        assert annotation.getLongArray("value").get(1) == 9223372036854775807L;
        assert annotation.getLongArray("value").get(2) == 0; // out of range
        assert annotation.getFloat("value") == 1234.5678f;
        assert annotation.getFloatArray("value").size() == 3;
        assert annotation.getFloatArray("value").get(0) == 1234.5678f;
        assert annotation.getFloatArray("value").get(1) == Float.MAX_VALUE;
        assert annotation.getFloatArray("value").get(2) == Float.MIN_VALUE;
        assert annotation.getDouble("value") == 1234.5677490234375d;
        assert annotation.getDoubleArray("value").size() == 3;
        assert annotation.getDoubleArray("value").get(0) == 1234.5677490234375d;
        assert annotation.getDoubleArray("value").get(1) == Float.MAX_VALUE;
        assert annotation.getDoubleArray("value").get(2) == Float.MIN_VALUE;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 3;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('3');
        assert annotation.getCharArray("value").get(2).equals('1');
        assert annotation.getString("value").equals("1234.5677");
        assert annotation.getStringArray("value").size() == 3;
        assert annotation.getStringArray("value").get(0).equals("1234.5677");
        assert annotation.getStringArray("value").get(1).equals("3.4028235E38");
        assert annotation.getStringArray("value").get(2).equals("1.4E-45");
    }

    @Test
    public void doubleArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasDoubleArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayDoubleDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 3;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getBooleanArray("value").get(2);
        assert annotation.getByte("value") == 0; // out of range
        assert annotation.getByteArray("value").size() == 3;
        assert annotation.getByteArray("value").get(0) == 0; // out of range
        assert annotation.getByteArray("value").get(1) == 0; // out of range
        assert annotation.getByteArray("value").get(2) == 0; // out of range
        assert annotation.getShort("value") == 1234;
        assert annotation.getShortArray("value").size() == 3;
        assert annotation.getShortArray("value").get(0) == 1234;
        assert annotation.getShortArray("value").get(1) == 0; // out of range
        assert annotation.getShortArray("value").get(2) == 0; // out of range
        assert annotation.getInt("value") == 1234;
        assert annotation.getIntArray("value").size() == 3;
        assert annotation.getIntArray("value").get(0) == 1234;
        assert annotation.getIntArray("value").get(1) == 0; // out of range
        assert annotation.getIntArray("value").get(2) == 0; // out of range
        assert annotation.getLong("value") == 1234L;
        assert annotation.getLongArray("value").size() == 3;
        assert annotation.getLongArray("value").get(0) == 1234L;
        assert annotation.getLongArray("value").get(1) == 9223372036854775807L;
        assert annotation.getLongArray("value").get(2) == 0; // out of range
        assert annotation.getFloat("value") == 1234.5679f;
        assert annotation.getFloatArray("value").size() == 3;
        assert annotation.getFloatArray("value").get(0) == 1234.5679f;
        assert annotation.getFloatArray("value").get(1) == 0; // out of range
        assert annotation.getFloatArray("value").get(2) == 0; // out of range
        assert annotation.getDouble("value") == 1234.56787109375d;
        assert annotation.getDoubleArray("value").size() == 3;
        assert annotation.getDoubleArray("value").get(0) == 1234.56787109375d;
        assert annotation.getDoubleArray("value").get(1) == Double.MAX_VALUE;
        assert annotation.getDoubleArray("value").get(2) == Double.MIN_VALUE;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 3;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('1');
        assert annotation.getCharArray("value").get(2).equals('4');
        assert annotation.getString("value").equals("1234.56787109375");
        assert annotation.getStringArray("value").size() == 3;
        assert annotation.getStringArray("value").get(0).equals("1234.56787109375");
        assert annotation.getStringArray("value").get(1).equals("1.7976931348623157E308");
        assert annotation.getStringArray("value").get(2).equals("4.9E-324");
    }

    @Test
    public void stringArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasStringArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayStringDim1.class);

        assert annotation.getBoolean("value");
        assert annotation.getBooleanArray("value").size() == 2;
        assert annotation.getBooleanArray("value").get(0);
        assert annotation.getBooleanArray("value").get(1);
        assert annotation.getByte("value") == 123; // out of range
        assert annotation.getByteArray("value").size() == 2;
        assert annotation.getByteArray("value").get(0) == 123; // out of range
        assert annotation.getByteArray("value").get(1) == 0; // out of range
        assert annotation.getShort("value") == 123;
        assert annotation.getShortArray("value").size() == 2;
        assert annotation.getShortArray("value").get(0) == 123;
        assert annotation.getShortArray("value").get(1) == 321;
        assert annotation.getInt("value") == 123;
        assert annotation.getIntArray("value").size() == 2;
        assert annotation.getIntArray("value").get(0) == 123;
        assert annotation.getIntArray("value").get(1) == 321;
        assert annotation.getLong("value") == 123;
        assert annotation.getLongArray("value").size() == 2;
        assert annotation.getLongArray("value").get(0) == 123;
        assert annotation.getLongArray("value").get(1) == 321;
        assert annotation.getFloat("value") == 123;
        assert annotation.getFloatArray("value").size() == 2;
        assert annotation.getFloatArray("value").get(0) == 123;
        assert annotation.getFloatArray("value").get(1) == 321;
        assert annotation.getDouble("value") == 123;
        assert annotation.getDoubleArray("value").size() == 2;
        assert annotation.getDoubleArray("value").get(0) == 123;
        assert annotation.getDoubleArray("value").get(1) == 321;
        assert annotation.getChar("value") == '1';
        assert annotation.getCharArray("value").size() == 2;
        assert annotation.getCharArray("value").get(0).equals('1');
        assert annotation.getCharArray("value").get(1).equals('3');
        assert annotation.getString("value").equals("123");
        assert annotation.getStringArray("value").size() == 2;
        assert annotation.getStringArray("value").get(0).equals("123");
        assert annotation.getStringArray("value").get(1).equals("321");
    }

    @Test
    public void classArray_Test() throws IOException, ClassNotFoundException {
        Annotations annotations = Annotations.ofClass(TypeHasClassArrayTestBean.class);
        Annotation annotation = annotations.getAnnotation(ArrayClassDim1.class);

        assert annotation.getString("value").equals(TypeHasStringArrayTestBean.class.getName());
        assert annotation.getStringArray("value").size() == 2;
        assert annotation.getStringArray("value").get(0).equals(TypeHasStringArrayTestBean.class.getName());
        assert annotation.getStringArray("value").get(1).equals(TypeHasDoubleArrayTestBean.class.getName());

        ClassLoader loader = TypeHasArrayTest.class.getClassLoader();
        assert annotation.getClass("value", loader) == TypeHasStringArrayTestBean.class;
        assert annotation.getClassArray("value", loader).size() == 2;
        assert annotation.getClassArray("value", loader).get(0) == TypeHasStringArrayTestBean.class;
        assert annotation.getClassArray("value", loader).get(1) == TypeHasDoubleArrayTestBean.class;
    }

    //        TypeHasCharArrayTestBean
    //        TypeHasStringArrayTest
}

@ArrayBooleanDim1({ true, false })
class TypeHasBooleanArrayTestBean {
}

@ArrayByteDim1({ 123, Byte.MAX_VALUE, Byte.MIN_VALUE })
class TypeHasByteArrayTestBean {
}

@ArrayShortDim1({ 12345, Short.MAX_VALUE, Short.MIN_VALUE })
class TypeHasShortArrayTestBean {
}

@ArrayIntegerDim1({ 1234567890, Integer.MAX_VALUE, Integer.MIN_VALUE })
class TypeHasIntegerArrayTestBean {
}

@ArrayLongDim1({ 1234567890123456789L, Long.MAX_VALUE, Long.MIN_VALUE })
class TypeHasLongArrayTestBean {
}

@ArrayCharDim1({ 'a', 'b' })
class TypeHasCharArrayTestBean {
}

@ArrayFloatDim1({ 1234.5678f, Float.MAX_VALUE, Float.MIN_VALUE })
class TypeHasFloatArrayTestBean {
}

@ArrayDoubleDim1({ 1234.567890123456f, Double.MAX_VALUE, Double.MIN_VALUE })
class TypeHasDoubleArrayTestBean {
}

@ArrayStringDim1({ "123", "321" })
class TypeHasStringArrayTestBean {
}

@ArrayClassDim1({ TypeHasStringArrayTestBean.class, TypeHasDoubleArrayTestBean.class })
class TypeHasClassArrayTestBean {
}
