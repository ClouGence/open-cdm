package com.clougence.utils.reflect.annotations;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.clougence.utils.reflect.Annotation;
import com.clougence.utils.reflect.Annotations;

public class FieldHasArrayTest {

    @Test
    public void booleanArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(FieldHasBooleanArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayBooleanDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasByteArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayByteDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasShortArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayShortDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasIntegerArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayIntegerDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasLongArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayLongDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasFloatArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayFloatDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasDoubleArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayDoubleDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasStringArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayStringDim1.class);

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
        Annotations annotations = Annotations.ofClass(FieldHasClassArrayTestBean.class);
        Annotation annotation = annotations.getField("filed", ArrayClassDim1.class);

        assert annotation.getString("value").equals(FieldHasStringArrayTestBean.class.getName());
        assert annotation.getStringArray("value").size() == 2;
        assert annotation.getStringArray("value").get(0).equals(FieldHasStringArrayTestBean.class.getName());
        assert annotation.getStringArray("value").get(1).equals(FieldHasDoubleArrayTestBean.class.getName());

        ClassLoader loader = TypeHasArrayTest.class.getClassLoader();
        assert annotation.getClass("value", loader) == FieldHasStringArrayTestBean.class;
        assert annotation.getClassArray("value", loader).size() == 2;
        assert annotation.getClassArray("value", loader).get(0) == FieldHasStringArrayTestBean.class;
        assert annotation.getClassArray("value", loader).get(1) == FieldHasDoubleArrayTestBean.class;
    }

    //        FieldHasCharArrayTestBean
    //        FieldHasStringArrayTest
}

class FieldHasBooleanArrayTestBean {

    @ArrayBooleanDim1({ true, false })
    private String filed;
}

class FieldHasByteArrayTestBean {

    @ArrayByteDim1({ 123, Byte.MAX_VALUE, Byte.MIN_VALUE })
    private String filed;
}

class FieldHasShortArrayTestBean {

    @ArrayShortDim1({ 12345, Short.MAX_VALUE, Short.MIN_VALUE })
    private String filed;
}

class FieldHasIntegerArrayTestBean {

    @ArrayIntegerDim1({ 1234567890, Integer.MAX_VALUE, Integer.MIN_VALUE })
    private String filed;
}

class FieldHasLongArrayTestBean {

    @ArrayLongDim1({ 1234567890123456789L, Long.MAX_VALUE, Long.MIN_VALUE })
    private String filed;
}

class FieldHasCharArrayTestBean {

    @ArrayCharDim1({ 'a', 'b' })
    private String filed;
}

class FieldHasFloatArrayTestBean {

    @ArrayFloatDim1({ 1234.5678f, Float.MAX_VALUE, Float.MIN_VALUE })
    private String filed;
}

class FieldHasDoubleArrayTestBean {

    @ArrayDoubleDim1({ 1234.567890123456f, Double.MAX_VALUE, Double.MIN_VALUE })
    private String filed;
}

class FieldHasStringArrayTestBean {

    @ArrayStringDim1({ "123", "321" })
    private String filed;
}

class FieldHasClassArrayTestBean {

    @ArrayClassDim1({ FieldHasStringArrayTestBean.class, FieldHasDoubleArrayTestBean.class })
    private String filed;
}
