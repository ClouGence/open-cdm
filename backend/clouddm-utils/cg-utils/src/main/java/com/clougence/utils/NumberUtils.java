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
package com.clougence.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * <p>Provides extra functionality for Java Number classes.</p>
 *
 * @author Apache Software Foundation
 * @author <a href="mailto:rand_mcneely@yahoo.com">Rand McNeely</a>
 * @author <a href="mailto:steve.downey@netfolio.com">Steve Downey</a>
 * @author Eric Pugh
 * @author Phil Steitz
 * @since 1.0
 * @version $Id: NumberUtils.java 905636 2010-02-02 14:03:32Z niallp $
 */
public final class NumberUtils {
    // DEPRECATED CLASS !!!

    /**
     * <p><code>NumberUtils</code> instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>NumberUtils.stringToInt("6");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public NumberUtils(){
        super();
    }
    //--------------------------------------------------------------------

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * @param str  the string to convert
     * @return the int represented by the string, or <code>zero</code> if
     *  conversion fails
     */
    public static int stringToInt(String str) {
        return stringToInt(str, 0);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning a
     * default value if the conversion fails.</p>
     *
     * @param str  the string to convert
     * @param defaultValue  the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static int stringToInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static String trimZero(String numberStr) {
        if (!isNumber(numberStr) || numberStr.contains("e") || numberStr.contains("E")) {
            return numberStr;
        }

        if (numberStr.startsWith("-") || numberStr.startsWith("+"))

            numberStr = StringUtils.trim(numberStr, '0');

        if (numberStr.length() == 0) {
            numberStr = "0";
        }
        if (numberStr.startsWith(".")) {
            numberStr = "0" + numberStr;
        }
        if (numberStr.endsWith(".")) {
            numberStr = numberStr + "0";
        }
        return numberStr;
    }
    //--------------------------------------------------------------------

    /**
     * <p>Turns a string value into a java.lang.Number.</p>
     *
     * <p>First, the value is examined for a type qualifier on the end
     * (<code>'f','F','d','D','l','L'</code>).  If it is found, it starts
     * trying to create successively larger types from the type specified
     * until one is found that can hold the value.</p>
     *
     * <p>If a type specifier is not found, it will check for a decimal point
     * and then try successively larger types from <code>Integer</code> to
     * <code>BigInteger</code> and from <code>Float</code> to
     * <code>BigDecimal</code>.</p>
     *
     * <p>If the string starts with <code>0x</code> or <code>-0x</code>, it
     * will be interpreted as a hexadecimal integer.  Values with leading
     * <code>0</code>'s will not be interpreted as octal.</p>
     *
     * @param val String containing a number
     * @return Number created from the string
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Number createNumber(String val) throws NumberFormatException {
        if (val == null) {
            return null;
        }
        if (val.length() == 0) {
            throw new NumberFormatException("\"\" is not a valid number.");
        }
        if (val.length() == 1 && !Character.isDigit(val.charAt(0))) {
            throw new NumberFormatException(val + " is not a valid number.");
        }
        if (val.startsWith("--")) {
            // this is protection for poorness in java.lang.BigDecimal.
            // it accepts this as a legal value, but it does not appear
            // to be in specification of class. OS X Java parses it to
            // a wrong value.
            return null;
        }
        if (val.startsWith("0x") || val.startsWith("-0x")) {
            return createInteger(val);
        }
        char lastChar = val.charAt(val.length() - 1);
        String mant;
        String dec;
        String exp;
        int decPos = val.indexOf('.');
        int expPos = val.indexOf('e') + val.indexOf('E') + 1;
        if (decPos > -1) {
            if (expPos > -1) {
                if (expPos < decPos) {
                    throw new NumberFormatException(val + " is not a valid number.");
                }
                dec = val.substring(decPos + 1, expPos);
            } else {
                dec = val.substring(decPos + 1);
            }
            mant = val.substring(0, decPos);
        } else {
            if (expPos > -1) {
                mant = val.substring(0, expPos);
            } else {
                mant = val;
            }
            dec = null;
        }
        if (!Character.isDigit(lastChar)) {
            if (expPos > -1 && expPos < val.length() - 1) {
                exp = val.substring(expPos + 1, val.length() - 1);
            } else {
                exp = null;
            }
            //Requesting a specific type..
            String numeric = val.substring(0, val.length() - 1);
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case 'l':
                case 'L':
                    if (dec == null && exp == null && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                        try {
                            return createLong(numeric);
                        } catch (NumberFormatException nfe) {
                            //Too big for a long
                        }
                        return createBigInteger(numeric);
                    }
                    throw new NumberFormatException(val + " is not a valid number.");
                case 'f':
                case 'F':
                    try {
                        Float f = NumberUtils.createFloat(numeric);
                        if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                            //If it's too big for a float or the float value = 0 and the string
                            //has non-zeros in it, then float does not have the precision we want
                            return f;
                        }
                    } catch (NumberFormatException e) {
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                case 'd':
                case 'D':
                    try {
                        Double d = NumberUtils.createDouble(numeric);
                        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // empty catch
                    }
                    try {
                        return createBigDecimal(numeric);
                    } catch (NumberFormatException e) {
                        // empty catch
                    }
                    //$FALL-THROUGH$
                default:
                    throw new NumberFormatException(val + " is not a valid number.");
            }
        } else {
            //User doesn't have a preference on the return type, so let's start
            //small and go from there...
            if (expPos > -1 && expPos < val.length() - 1) {
                exp = val.substring(expPos + 1);
            } else {
                exp = null;
            }
            if (dec == null && exp == null) {
                //Must be an int,long,bigint
                try {
                    return createInteger(val);
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                try {
                    return createLong(val);
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                return createBigInteger(val);
            } else {
                //Must be a float,double,BigDec
                boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
                try {
                    Float f = createFloat(val);
                    if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                try {
                    Double d = createDouble(val);
                    if (!(d.isInfinite() || (d == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                return createBigDecimal(val);
            }
        }
    }

    /**
     * <p>Utility method for {@link #createNumber(String)}.</p>
     *
     * <p>Returns <code>true</code> if s is <code>null</code>.</p>
     *
     * @param s the String to check
     * @return if it is all zeros or <code>null</code>
     */
    private static boolean isAllZeros(String s) {
        if (s == null) {
            return true;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '0') {
                return false;
            }
        }
        return s.length() > 0;
    }
    //--------------------------------------------------------------------

    /**
     * <p>Convert a <code>String</code> to a <code>Float</code>.</p>
     *
     * @param val  a <code>String</code> to convert
     * @return converted <code>Float</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Float createFloat(String val) {
        return Float.valueOf(val);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>.</p>
     *
     * @param val  a <code>String</code> to convert
     * @return converted <code>Double</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Double createDouble(String val) {
        return Double.valueOf(val);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Integer</code>, handling
     * hex and octal notations.</p>
     *
     * @param val  a <code>String</code> to convert
     * @return converted <code>Integer</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Integer createInteger(String val) {
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        return Integer.decode(val);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Long</code>.</p>
     *
     * @param val  a <code>String</code> to convert
     * @return converted <code>Long</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Long createLong(String val) {
        return Long.valueOf(val);
    }

    /**
     * <p>Convert a <code>long</code> to a Unsigned <code>Long</code>.</p>
     *
     * @param value  a <code>String</code> to convert
     * @return converted <code>Long</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigInteger createUnsignedLong(long value) {
        if (value >= 0) {
            return BigInteger.valueOf(value);
        } else {
            long lowValue = value & 0x7fffffffffffffffL;
            return BigInteger.valueOf(lowValue).add(BigInteger.valueOf(Long.MAX_VALUE)).add(BigInteger.ONE);
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>BigInteger</code>.</p>
     *
     * @param val  a <code>String</code> to convert
     * @return converted <code>BigInteger</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigInteger createBigInteger(String val) {
        return new BigInteger(val);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>BigDecimal</code>.</p>
     *
     * @param val  a <code>String</code> to convert
     * @return converted <code>BigDecimal</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigDecimal createBigDecimal(String val) {
        return new BigDecimal(val);
    }

    //--------------------------------------------------------------------

    /**
     * <p>Gets the minimum of three <code>long</code> values.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return the smallest of the values
     */
    public static long minimum(long a, long b, long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three <code>int</code> values.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return the smallest of the values
     */
    public static int minimum(int a, int b, int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three <code>long</code> values.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return the largest of the values
     */
    public static long maximum(long a, long b, long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three <code>int</code> values.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return the largest of the values
     */
    public static int maximum(int a, int b, int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }
    //--------------------------------------------------------------------

    /**
     * <p>Gets the between num of min,max <code>long</code> values.</p>
     * @param value  value 1
     * @param min  value 2
     * @param max  value 3
     */
    public static long between(long value, long min, long max) {
        if (min > max) {
            return value;
        }
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    /**
     * <p>Gets the between num of min,max <code>int</code> values.</p>
     * @param value  value 1
     * @param min  value 2
     * @param max  value 3
     */
    public static int between(int value, int min, int max) {
        if (min > max) {
            return value;
        }
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    /**
     * <p>Gets the between num of min,max <code>float</code> values.</p>
     * @param value  value 1
     * @param min  value 2
     * @param max  value 3
     */
    public static float between(float value, float min, float max) {
        if (min > max) {
            return value;
        }
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    /**
     * <p>Gets the between num of min,max <code>double</code> values.</p>
     * @param value  value 1
     * @param min  value 2
     * @param max  value 3
     */
    public static double between(double value, double min, double max) {
        if (min > max) {
            return value;
        }
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    //--------------------------------------------------------------------

    /**
     * <p>Compares two <code>doubles</code> for order.</p>
     *
     * <p>This method is more comprehensive than the standard Java greater
     * than, less than and equals operators.</p>
     * <ul>
     *  <li>It returns <code>-1</code> if the first value is less than the second.
     *  <li>It returns <code>+1</code> if the first value is greater than the second.
     *  <li>It returns <code>0</code> if the values are equal.
     * </ul>
     *
     * <p>
     * The ordering is as follows, largest to smallest:
     * <ul>
     *  <li>NaN
     *  <li>Positive infinity
     *  <li>Maximum double
     *  <li>Normal positive numbers
     *  <li>+0.0
     *  <li>-0.0
     *  <li>Normal negative numbers
     *  <li>Minimum double (-Double.MAX_VALUE)
     *  <li>Negative infinity
     * </ul>
     * </p>
     *
     * <p>Comparing <code>NaN</code> with <code>NaN</code> will
     * return <code>0</code>.</p>
     *
     * @param lhs  the first <code>double</code>
     * @param rhs  the second <code>double</code>
     * @return <code>-1</code> if lhs is less, <code>+1</code> if greater,
     *  <code>0</code> if equal to rhs
     */
    public static int compare(double lhs, double rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return +1;
        }
        // Need to compare bits to handle 0.0 == -0.0 being true
        // compare should put -0.0 < +0.0
        // Two NaNs are also == for compare purposes
        // where NaN == NaN is false
        long lhsBits = Double.doubleToLongBits(lhs);
        long rhsBits = Double.doubleToLongBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        // Something exotic! A comparison to NaN or 0.0 vs -0.0
        // Fortunately NaN's long is > than everything else
        // Also negzeros bits < poszero
        // NAN: 9221120237041090560
        // MAX: 9218868437227405311
        // NEGZERO: -9223372036854775808
        if (lhsBits < rhsBits) {
            return -1;
        } else {
            return +1;
        }
    }

    /**
     * <p>Compares two floats for order.</p>
     *
     * <p>This method is more comprehensive than the standard Java greater than,
     * less than and equals operators.</p>
     * <ul>
     *  <li>It returns <code>-1</code> if the first value is less than the second.
     *  <li>It returns <code>+1</code> if the first value is greater than the second.
     *  <li>It returns <code>0</code> if the values are equal.
     * </ul>
     *
     * <p> The ordering is as follows, largest to smallest:
     * <ul>
     * <li>NaN
     * <li>Positive infinity
     * <li>Maximum float
     * <li>Normal positive numbers
     * <li>+0.0
     * <li>-0.0
     * <li>Normal negative numbers
     * <li>Minimum float (-Float.MAX_VALUE)
     * <li>Negative infinity
     * </ul>
     *
     * <p>Comparing <code>NaN</code> with <code>NaN</code> will return
     * <code>0</code>.</p>
     *
     * @param lhs  the first <code>float</code>
     * @param rhs  the second <code>float</code>
     * @return <code>-1</code> if lhs is less, <code>+1</code> if greater,
     *  <code>0</code> if equal to rhs
     */
    public static int compare(float lhs, float rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return +1;
        }
        //Need to compare bits to handle 0.0 == -0.0 being true
        // compare should put -0.0 < +0.0
        // Two NaNs are also == for compare purposes
        // where NaN == NaN is false
        int lhsBits = Float.floatToIntBits(lhs);
        int rhsBits = Float.floatToIntBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        //Something exotic! A comparison to NaN or 0.0 vs -0.0
        //Fortunately NaN's int is > than everything else
        //Also negzeros bits < poszero
        //NAN: 2143289344
        //MAX: 2139095039
        //NEGZERO: -2147483648
        if (lhsBits < rhsBits) {
            return -1;
        } else {
            return +1;
        }
    }
    //--------------------------------------------------------------------

    /**
     * <p>Checks whether the <code>String</code> contains only
     * digit characters.</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if str contains only unicode numeric
     */
    public static boolean isDigits(String str) {
        if ((str == null) || (str.length() == 0)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks whether the String a valid Java number.</p>
     *
     * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
     * qualifier, scientific notation and numbers marked with a type
     * qualifier (e.g. 123L).</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     */
    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;
            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }

    //
    // ============================================================================================
    //
    public static boolean isByteType(Class<?> targetType) {
        return targetType.equals(Byte.class) || targetType.equals(Byte.TYPE);
    }

    public static boolean isShortType(Class<?> targetType) {
        return targetType.equals(Short.class) || targetType.equals(Short.TYPE);
    }

    public static boolean isIntType(Class<?> targetType) {
        return targetType.equals(Integer.class) || targetType.equals(Integer.TYPE);
    }

    public static boolean isLongType(Class<?> targetType) {
        return targetType.equals(Long.class) || targetType.equals(Long.TYPE);
    }

    public static boolean isFloatType(Class<?> targetType) {
        return targetType.equals(Float.class) || targetType.equals(Float.TYPE);
    }

    public static boolean isDoubleType(Class<?> targetType) {
        return targetType.equals(Double.class) || targetType.equals(Double.TYPE);
    }

    public static boolean isNumber(Object object) {
        return object instanceof Number;
    }

    public static boolean isBoolean(Object object) {
        return object instanceof Boolean;
    }

    public static boolean isByteNumber(Object number) {
        return number instanceof Byte;
    }

    public static boolean isShortNumber(Object number) {
        return number instanceof Short;
    }

    public static boolean isIntegerNumber(Object number) {
        return number instanceof Integer;
    }

    public static boolean isCharacter(Object number) {
        return number instanceof Character;
    }

    public static boolean isLongNumber(Object number) {
        return number instanceof Long;
    }

    public static boolean isFloatNumber(Object number) {
        return number instanceof Float;
    }

    public static boolean isDoubleNumber(Object number) {
        return number instanceof Double;
    }

    //
    // ============================================================================================
    //  数学计算处理工具，提供：加、减、乘、除、整除、求余，尽量考虑在保证性能的前提下不产生精度丢失问题。
    private static final int BOOL   = 1;
    private static final int BYTE   = 2;
    private static final int SHORT  = 3;
    private static final int CHAR   = 4;
    private static final int INT    = 5;
    private static final int LONG   = 6;
    //
    private static final int FLOAT  = 7;
    private static final int DOUBLE = 8;
    //
    private static final int BIGINT = 9;
    private static final int BIGDEC = 10;
    //
    private static final int NONE   = 0;
    //

    /** 对比两个数据类型，返回交大的那个类型作为载体。 */
    private static int getNumericType(Number v1, Number v2) {
        int v1Type = getNumericType(v1);
        int v2Type = getNumericType(v2);
        //
        // .未知类型
        if (v1Type == v2Type) {
            return v1Type;
        }
        if (v1Type == NONE || v2Type == NONE) {
            return NONE;
        }
        // .整数类型的只使用 long or int 作为承载
        if (v1Type <= LONG && v2Type <= LONG) {
            return (v1Type == LONG || v2Type == LONG) ? LONG : INT;
        }
        // .浮点数使用 float or double 作为承载
        if (v1Type <= DOUBLE && v2Type <= DOUBLE) {
            // boolean、byte、short、float  -> float
            // int、char、double            -> double
            boolean useFloat = v1Type <= SHORT || v2Type <= SHORT && (v1Type == FLOAT || v2Type == FLOAT);
            return useFloat ? FLOAT : DOUBLE;
        }
        // .整数 or 浮点
        boolean useDec = v1Type == FLOAT || v1Type == DOUBLE || v2Type == FLOAT || v2Type == DOUBLE;
        return useDec ? BIGDEC : BIGINT;
    }

    public static int getNumericType(Object value) {
        if (isBoolean(value)) {
            return BOOL;
        }
        if (isByteNumber(value)) {
            return BYTE;
        }
        if (isShortNumber(value)) {
            return SHORT;
        }
        if (isCharacter(value)) {
            return CHAR;
        }
        if (isIntegerNumber(value)) {
            return INT;
        }
        if (isLongNumber(value)) {
            return LONG;
        }
        if (value instanceof BigInteger) {
            return BIGINT;
        }
        if (isFloatNumber(value)) {
            return FLOAT;
        }
        if (isDoubleNumber(value)) {
            return DOUBLE;
        }
        if (value instanceof BigDecimal) {
            return BIGDEC;
        }
        return NONE;
    }
    //
    //

    /** 转换为：int */
    private static int intValue(Object value) {
        if (value == null) {
            return 0;
        }
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class) {
            return ((Number) value).intValue();
        }
        if (c == Boolean.class) {
            return (Boolean) value ? 1 : 0;
        }
        if (c == Character.class) {
            return (Character) value;
        }
        return Integer.parseInt(value.toString().trim());
    }

    /** 转换为：long */
    private static long longValue(Object value) {
        if (value == null) {
            return 0L;
        }
        Class<?> c = value.getClass();
        if (c.getSuperclass() == Number.class) {
            return ((Number) value).longValue();
        }
        if (c == Boolean.class) {
            return (Boolean) value ? 1 : 0;
        }
        if (c == Character.class) {
            return (Character) value;
        }
        return Long.parseLong(value.toString().trim());
    }

    /** 转换为：BigInteger */
    private static BigInteger bigIntValue(Object value) {
        if (value == null) {
            return BigInteger.valueOf(0L);
        }
        Class c = value.getClass();
        if (c == BigInteger.class) {
            return (BigInteger) value;
        }
        if (c == BigDecimal.class) {
            return ((BigDecimal) value).toBigInteger();
        }
        if (c.getSuperclass() == Number.class) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        if (c == Boolean.class) {
            return BigInteger.valueOf((Boolean) value ? 1 : 0);
        }
        if (c == Character.class) {
            return BigInteger.valueOf((Character) value);
        }
        return new BigInteger(value.toString().trim());
    }

    /** 转换为：float */
    private static float floatValue(Object value) {
        if (value == null) {
            return 0.0f;
        }
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class) {
            return ((Number) value).floatValue();
        }
        if (c == Boolean.class) {
            return (Boolean) value ? 1.0f : 0.0f;
        }
        if (c == Character.class) {
            return (Character) value;
        }
        String s = value.toString().trim();
        return (s.length() == 0) ? 0.0f : Float.parseFloat(s);
    }

    /** 转换为：double */
    private static double doubleValue(Object value) {
        if (value == null) {
            return 0.0;
        }
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class) {
            return ((Number) value).doubleValue();
        }
        if (c == Boolean.class) {
            return (Boolean) value ? 1 : 0;
        }
        if (c == Character.class) {
            return (Character) value;
        }
        String s = value.toString().trim();
        return (s.length() == 0) ? 0.0 : Double.parseDouble(s);
    }

    /** 转换为：BigDecimal */
    private static BigDecimal bigDecimalValue(Object value) {
        if (value == null) {
            return BigDecimal.valueOf(0L);
        }
        Class c = value.getClass();
        if (c == BigDecimal.class) {
            return (BigDecimal) value;
        }
        if (c == BigInteger.class) {
            return new BigDecimal((BigInteger) value);
        }
        if (c.getSuperclass() == Number.class) {
            return new BigDecimal(((Number) value).doubleValue());
        }
        if (c == Boolean.class) {
            return BigDecimal.valueOf((Boolean) value ? 1 : 0);
        }
        if (c == Character.class) {
            return BigDecimal.valueOf((Character) value);
        }
        return new BigDecimal(value.toString().trim());
    }
    //
    // ============================================================================================
    //

    /** 加 */
    public static Number add(Number obj1, Number obj2) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            return decimalAdd(obj1, obj2);
        } else {
            return integerAdd(obj1, obj2);
        }
    }

    /** 减 */
    public static Number subtract(Number obj1, Number obj2) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            return decimalSubtract(obj1, obj2);
        } else {
            return integerSubtract(obj1, obj2);
        }
    }

    /** 乘 */
    public static Number multiply(Number obj1, Number obj2) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            return decimalMultiply(obj1, obj2);
        } else {
            return integerMultiply(obj1, obj2);
        }
    }

    /** 除 */
    public static Number divide(Number obj1, Number obj2, int precision, RoundingMode roundingEnum) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            if (roundingEnum == null) {
                roundingEnum = RoundingMode.HALF_UP;
            }
            if (precision <= 0) {
                precision = 23;
            }
            return decimalDivide(obj1, obj2, precision, roundingEnum);
        } else {
            return integerDivide(obj1, obj2);
        }
    }

    /** 整除 */
    public static Number aliquot(Number obj1, Number obj2) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            return decimalAliquot(obj1, obj2);
        } else {
            return integerDivide(obj1, obj2);
        }
    }

    /** 求余 */
    public static Number mod(Number obj1, Number obj2) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            return decimalMod(obj1, obj2);
        } else {
            return integerMod(obj1, obj2);
        }
    }

    /** 取反，相当于：value * -1 */
    public static Number negate(Number obj) {
        if (isDecimal(obj)) {
            return decimalNegate(obj);
        } else {
            return integerNegate(obj);
        }
    }

    /** 测试是否为一个小数 */
    public static boolean isDecimal(Number tester) {
        if (tester instanceof BigDecimal) {
            return true;
        }
        if (isFloatNumber(tester)) {
            return true;
        }
        if (isDoubleNumber(tester)) {
            return true;
        }
        return false;
    }

    private static Number newReal(int realType, long value) {
        switch (realType) {
            case BOOL:
                return (value == 0) ? 0 : 1;
            case BYTE:
                return (byte) value;
            case SHORT:
                return (short) value;
            case CHAR:
            case INT:
                return (int) value;
            default:
                return value;
        }
    }

    private static Number newReal(int realType, BigInteger value) {
        switch (realType) {
            case BOOL:
                return BigInteger.ZERO.compareTo(value) == 0 ? 0 : 1;
            case BYTE:
                return value.byteValue();
            case SHORT:
                return value.shortValue();
            case CHAR:
            case INT:
                return value.intValue();
            case LONG:
                return value.longValue();
            default:
                return value;
        }
    }

    private static Number newReal(int realType, double value) {
        if (realType == FLOAT) {
            return (float) value;
        }
        return value;
    }

    private static Number newReal(int realType, BigDecimal value) {
        if (realType == FLOAT) {
            return value.floatValue();
        }
        if (realType == DOUBLE) {
            return value.doubleValue();
        }
        return value;
    }

    /** 整数，加 */
    private static Number integerAdd(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case BOOL:
            case BYTE:
            case SHORT:
            case CHAR:
            case INT:
                return newReal(maxType, intValue(obj1) + intValue(obj2));
            case LONG:
                return newReal(maxType, longValue(obj1) + longValue(obj2));
            default:
                return newReal(maxType, bigIntValue(obj1).add(bigIntValue(obj2)));
        }
    }

    /** 整数，减 */
    private static Number integerSubtract(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case BOOL:
            case BYTE:
            case SHORT:
            case CHAR:
            case INT:
                return newReal(maxType, intValue(obj1) - intValue(obj2));
            case LONG:
                return newReal(maxType, longValue(obj1) - longValue(obj2));
            default:
                return newReal(maxType, bigIntValue(obj1).subtract(bigIntValue(obj2)));
        }
    }

    /** 整数，乘 */
    private static Number integerMultiply(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case BOOL:
            case BYTE:
            case SHORT:
            case CHAR:
            case INT:
                return newReal(maxType, intValue(obj1) * intValue(obj2));
            case LONG:
                return newReal(maxType, longValue(obj1) * longValue(obj2));
            default:
                return newReal(maxType, bigIntValue(obj1).multiply(bigIntValue(obj2)));
        }
    }

    /** 整数，除 or 整除 */
    private static Number integerDivide(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case BOOL:
            case BYTE:
            case SHORT:
            case CHAR:
            case INT:
                return newReal(maxType, intValue(obj1) / intValue(obj2));
            case LONG:
                return newReal(maxType, longValue(obj1) / longValue(obj2));
            default:
                return newReal(maxType, bigIntValue(obj1).divide(bigIntValue(obj2)));
        }
    }

    /** 整数，求余 */
    private static Number integerMod(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case BOOL:
            case BYTE:
            case SHORT:
            case CHAR:
            case INT:
                return newReal(maxType, intValue(obj1) % intValue(obj2));
            case LONG:
                return newReal(maxType, longValue(obj1) % longValue(obj2));
            default:
                return newReal(maxType, bigIntValue(obj1).mod(bigIntValue(obj2)));
        }
    }

    /** 整数，取反 */
    private static Number integerNegate(Number obj) {
        int maxType = getNumericType(obj);
        switch (maxType) {
            case BOOL:
            case BYTE:
            case SHORT:
            case CHAR:
            case INT:
                return newReal(maxType, -intValue(obj));
            case LONG:
                return newReal(maxType, -longValue(obj));
            default:
                return newReal(maxType, bigIntValue(obj).negate());
        }
    }

    /** 小数，加 */
    private static Number decimalAdd(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, floatValue(obj1) + floatValue(obj2));
            case DOUBLE:
                return newReal(maxType, doubleValue(obj1) + doubleValue(obj2));
            default:
                return newReal(maxType, bigDecimalValue(obj1).add(bigDecimalValue(obj2)));
        }
    }

    /** 小数，减 */
    private static Number decimalSubtract(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, floatValue(obj1) - floatValue(obj2));
            case DOUBLE:
                return newReal(maxType, doubleValue(obj1) - doubleValue(obj2));
            default:
                return newReal(maxType, bigDecimalValue(obj1).subtract(bigDecimalValue(obj2)));
        }
    }

    /** 小数，乘 */
    private static Number decimalMultiply(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, floatValue(obj1) * floatValue(obj2));
            case DOUBLE:
                return newReal(maxType, doubleValue(obj1) * doubleValue(obj2));
            default:
                return newReal(maxType, bigDecimalValue(obj1).multiply(bigDecimalValue(obj2)));
        }
    }

    /** 小数，除 */
    private static Number decimalDivide(Number obj1, Number obj2, int precision, RoundingMode roundingEnum) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, floatValue(obj1) / floatValue(obj2));
            case DOUBLE:
                return newReal(maxType, doubleValue(obj1) / doubleValue(obj2));
            default:
                return newReal(maxType, bigDecimalValue(obj1).divide(bigDecimalValue(obj2), precision, roundingEnum));
        }
    }

    /** 小数，整除 */
    private static Number decimalAliquot(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, (int) (floatValue(obj1) / floatValue(obj2)));
            case DOUBLE:
                return newReal(maxType, (long) (doubleValue(obj1) / doubleValue(obj2)));
            default:
                return newReal(maxType, bigDecimalValue(obj1).divideToIntegralValue(bigDecimalValue(obj2)));
        }
    }

    /** 小数，求余 */
    private static Number decimalMod(Number obj1, Number obj2) {
        int maxType = getNumericType(obj1, obj2);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, floatValue(obj1) % floatValue(obj2));
            case DOUBLE:
                return newReal(maxType, doubleValue(obj1) % doubleValue(obj2));
            default:
                return newReal(maxType, bigDecimalValue(obj1).remainder(bigDecimalValue(obj2)));
        }
    }

    /** 小数，取反 */
    private static Number decimalNegate(Number obj) {
        int maxType = getNumericType(obj);
        switch (maxType) {
            case FLOAT:
                return newReal(maxType, -floatValue(obj));
            case DOUBLE:
                return newReal(maxType, -doubleValue(obj));
            default:
                return newReal(maxType, bigDecimalValue(obj).negate());
        }
    }

    //
    // ============================================================================================
    //
    private static int getNumericTypeWithCompare(Number v1, Number v2) {
        int numericType = getNumericType(v1, v2);
        if (numericType == NONE) {
            numericType = (isDecimal(v1) || isDecimal(v2)) ? BIGDEC : BIGINT;
        }
        return numericType;
    }

    /** 相等 */
    public static boolean eq(Number obj1, Number obj2) {
        int numericType = getNumericTypeWithCompare(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) == intValue(obj2);
        }
        if (numericType <= LONG) {
            return longValue(obj1) == longValue(obj2);
        }
        if (numericType <= DOUBLE) {
            return doubleValue(obj1) == doubleValue(obj2);
        }
        if (numericType == BIGINT) {
            return bigIntValue(obj1).compareTo(bigIntValue(obj2)) == 0;
        }
        if (numericType == BIGDEC) {
            return bigDecimalValue(obj1).compareTo(bigDecimalValue(obj2)) == 0;
        }
        return obj1.doubleValue() == obj2.doubleValue(); // 永远不会走到这一步的
    }

    /** 大于 */
    public static boolean gt(Number obj1, Number obj2) {
        int numericType = getNumericTypeWithCompare(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) > intValue(obj2);
        }
        if (numericType <= LONG) {
            return longValue(obj1) > longValue(obj2);
        }
        if (numericType <= DOUBLE) {
            return doubleValue(obj1) > doubleValue(obj2);
        }
        if (numericType == BIGINT) {
            return bigIntValue(obj1).compareTo(bigIntValue(obj2)) > 0;
        }
        if (numericType == BIGDEC) {
            return bigDecimalValue(obj1).compareTo(bigDecimalValue(obj2)) > 0;
        }
        return obj1.doubleValue() > obj2.doubleValue(); // 永远不会走到这一步的
    }

    /** 大于等于 */
    public static boolean gteq(Number obj1, Number obj2) {
        int numericType = getNumericTypeWithCompare(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) >= intValue(obj2);
        }
        if (numericType <= LONG) {
            return longValue(obj1) >= longValue(obj2);
        }
        if (numericType <= DOUBLE) {
            return doubleValue(obj1) >= doubleValue(obj2);
        }
        if (numericType == BIGINT) {
            return bigIntValue(obj1).compareTo(bigIntValue(obj2)) >= 0;
        }
        if (numericType == BIGDEC) {
            return bigDecimalValue(obj1).compareTo(bigDecimalValue(obj2)) >= 0;
        }
        return obj1.doubleValue() >= obj2.doubleValue(); // 永远不会走到这一步的
    }

    /** 小于 */
    public static boolean lt(Number obj1, Number obj2) {
        int numericType = getNumericTypeWithCompare(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) < intValue(obj2);
        }
        if (numericType <= LONG) {
            return longValue(obj1) < longValue(obj2);
        }
        if (numericType <= DOUBLE) {
            return doubleValue(obj1) < doubleValue(obj2);
        }
        if (numericType == BIGINT) {
            return bigIntValue(obj1).compareTo(bigIntValue(obj2)) < 0;
        }
        if (numericType == BIGDEC) {
            return bigDecimalValue(obj1).compareTo(bigDecimalValue(obj2)) < 0;
        }
        return obj1.doubleValue() < obj2.doubleValue(); // 永远不会走到这一步的
    }

    /** 小于等于 */
    public static boolean lteq(Number obj1, Number obj2) {
        int numericType = getNumericTypeWithCompare(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) <= intValue(obj2);
        }
        if (numericType <= LONG) {
            return longValue(obj1) <= longValue(obj2);
        }
        if (numericType <= DOUBLE) {
            return doubleValue(obj1) <= doubleValue(obj2);
        }
        if (numericType == BIGINT) {
            return bigIntValue(obj1).compareTo(bigIntValue(obj2)) <= 0;
        }
        if (numericType == BIGDEC) {
            return bigDecimalValue(obj1).compareTo(bigDecimalValue(obj2)) <= 0;
        }
        return obj1.doubleValue() <= obj2.doubleValue(); // 永远不会走到这一步的
    }

    //
    // ============================================================================================
    //
    private static void checkDecimal(Number obj1, Number obj2) {
        if (isDecimal(obj1) || isDecimal(obj2)) {
            throw new NumberFormatException("value mast not be Decimal.");
        }
    }

    /** 与 */
    public static Number and(Number obj1, Number obj2) {
        checkDecimal(obj1, obj2);
        int numericType = getNumericType(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) & intValue(obj2);
        } else if (numericType <= LONG) {
            return longValue(obj1) & longValue(obj2);
        } else {
            return bigIntValue(obj1).and(bigIntValue(obj2));
        }
    }

    /** 或 */
    public static Number or(Number obj1, Number obj2) {
        checkDecimal(obj1, obj2);
        int numericType = getNumericType(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) | intValue(obj2);
        } else if (numericType <= LONG) {
            return longValue(obj1) | longValue(obj2);
        } else {
            return bigIntValue(obj1).or(bigIntValue(obj2));
        }
    }

    /** 异或 */
    public static Number xor(Number obj1, Number obj2) {
        checkDecimal(obj1, obj2);
        int numericType = getNumericType(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) ^ intValue(obj2);
        } else if (numericType <= LONG) {
            return longValue(obj1) ^ longValue(obj2);
        } else {
            return bigIntValue(obj1).xor(bigIntValue(obj2));
        }
    }

    /** 左位移 */
    public static Number shiftLeft(Number obj1, Number obj2) {
        checkDecimal(obj1, obj2);
        int numericType = getNumericType(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) << intValue(obj2);
        } else if (numericType <= LONG) {
            return longValue(obj1) << longValue(obj2);
        } else {
            return bigIntValue(obj1).shiftLeft(intValue(obj2));
        }
    }

    /** 右位移 */
    public static Number shiftRight(Number obj1, Number obj2) {
        checkDecimal(obj1, obj2);
        int numericType = getNumericType(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) >> intValue(obj2);
        } else if (numericType <= LONG) {
            return longValue(obj1) >> longValue(obj2);
        } else {
            return bigIntValue(obj1).shiftRight(intValue(obj2));
        }
    }

    /** 无符号右位移 */
    public static Number shiftRightWithUnsigned(Number obj1, Number obj2) {
        checkDecimal(obj1, obj2);
        int numericType = getNumericType(obj1, obj2);
        if (numericType <= INT) {
            return intValue(obj1) >>> intValue(obj2);
        } else if (numericType <= LONG) {
            return longValue(obj1) >>> longValue(obj2);
        } else {
            //忽略无符号的右位移运算符（>>>），因为该操作与由此类提供的“无穷大的词大小”抽象结合使用时毫无意义。
            // - 无穷大的词大小 -> BigInteger 理论上可以表示无穷大。
            return bigIntValue(obj1).shiftRight(intValue(obj2));
        }
    }

    // ------------------------------------------------------------------------------------------- round (copy form hutool)

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 => 123.46
     *
     * @param v     值
     * @param scale 保留小数位数
     * @return 新值
     */
    public static BigDecimal round(double v, int scale) {
        return round(v, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 => 123.46
     *
     * @param v     值
     * @param scale 保留小数位数
     * @return 新值
     */
    public static String roundStr(double v, int scale) {
        return round(v, scale).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 => 123.46
     *
     * @param numberStr 数字值的字符串表现形式
     * @param scale     保留小数位数
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale) {
        return round(numberStr, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 => 123.46
     *
     * @param number 数字值
     * @param scale  保留小数位数
     * @return 新值
     * @since 4.1.0
     */
    public static BigDecimal round(BigDecimal number, int scale) {
        return round(number, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 => 123.46
     *
     * @param numberStr 数字值的字符串表现形式
     * @param scale     保留小数位数
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(String numberStr, int scale) {
        return round(numberStr, scale).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 => 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     */
    public static BigDecimal round(double v, int scale, RoundingMode roundingMode) {
        return round(Double.toString(v), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 => 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(double v, int scale, RoundingMode roundingMode) {
        return round(v, scale, roundingMode).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 => 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
        if (StringUtils.isBlank(numberStr)) {
            throw new IllegalArgumentException("this String argument must have text; it must not be null, empty, or blank");
        }
        if (scale < 0) {
            scale = 0;
        }
        return round(createBigDecimal(numberStr), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 => 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = 0;
        }
        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }

        return number.setScale(scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 => 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(String numberStr, int scale, RoundingMode roundingMode) {
        return round(numberStr, scale, roundingMode).toString();
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundHalfEven(Number number, int scale) {
        if (number instanceof BigDecimal) {
            return roundHalfEven(number, scale);
        } else if (number instanceof BigInteger) {
            return roundHalfEven(new BigDecimal((BigInteger) number), scale);
        } else {
            return roundHalfEven(createBigDecimal(number.toString()), scale);
        }
    }

    /**
     * 四舍六入五成双计算法
     * <p> 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。 </p>
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_EVEN);
    }

    /**
     * 保留固定小数位数，舍去多余位数
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundDown(Number number, int scale) {
        if (number instanceof BigDecimal) {
            return roundHalfEven(number, scale);
        } else if (number instanceof BigInteger) {
            return roundHalfEven(new BigDecimal((BigInteger) number), scale);
        } else {
            return roundHalfEven(createBigDecimal(number.toString()), scale);
        }
    }

    /**
     * 保留固定小数位数，舍去多余位数
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundDown(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.DOWN);
    }
}
