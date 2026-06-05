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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static java.math.BigDecimal.ROUND_DOWN;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * <p>Utility library that supplements the standard {@link Random} class.</p>
 *
 * <p>Caveat: Instances of {@link Random} are not cryptographically secure.</p>
 *
 * <p>Please note that the Apache Commons project provides a component
 * dedicated to pseudo-random number generation, namely
 * <a href="https://commons.apache.org/rng">Commons RNG</a>, that may be
 * a better choice for applications with more stringent requirements
 * (performance and/or correctness).</p>
 *
 * @since 3.3
 */
public class RandomUtils {

    /**
     * Random object used by random method. This has to be not local to the
     * random method so as to not return the same value in the same millisecond.
     */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * <p>
     * {@code RandomUtils} instances should NOT be constructed in standard
     * programming. Instead, the class should be used as
     * {@code RandomUtils.nextBytes(5);}.
     * </p>
     *
     * <p>
     * This constructor is public to permit tools that require a JavaBean
     * instance to operate.
     * </p>
     */
    public RandomUtils(){
        super();
    }

    private static void isTrue(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * <p>
     * Returns a random boolean value
     * </p>
     *
     * @return the random boolean
     * @since 3.5
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * <p>
     * Creates an array of random bytes.
     * </p>
     *
     * @param count
     *            the size of the returned array
     * @return the random byte array
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public static byte[] nextBytes(final int count) {
        isTrue(count >= 0, "Count cannot be negative.");

        final byte[] result = new byte[count];
        RANDOM.nextBytes(result);
        return result;
    }

    /**
     * <p>
     * Returns a random integer within the specified range.
     * </p>
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random integer
     */
    public static int nextInt(final int startInclusive, final int endExclusive) {
        isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }

    /**
     * <p> Returns a random int within 0 - Integer.MAX_VALUE </p>
     *
     * @return the random integer
     * @see #nextInt(int, int)
     * @since 3.5
     */
    public static int nextInt() {
        return nextInt(0, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Returns a random long within the specified range.
     * </p>
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random long
     */
    public static long nextLong(final long startInclusive, final long endExclusive) {
        isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return (long) nextDouble(startInclusive, endExclusive);
    }

    /**
     * <p> Returns a random long within 0 - Long.MAX_VALUE </p>
     *
     * @return the random long
     * @see #nextLong(long, long)
     * @since 3.5
     */
    public static long nextLong() {
        return nextLong(0, Long.MAX_VALUE);
    }

    public static BigInteger nextBigInteger(Number startInclusive, Number endInclusive) {
        BigInteger minBig = (startInclusive instanceof BigInteger) ? (BigInteger) startInclusive : (startInclusive instanceof BigDecimal) ? ((BigDecimal) startInclusive)
            .toBigInteger() : (BigInteger.valueOf(startInclusive.longValue()));
        BigInteger maxBig = (endInclusive instanceof BigInteger) ? (BigInteger) endInclusive : (endInclusive instanceof BigDecimal) ? ((BigDecimal) endInclusive)
            .toBigInteger() : (BigInteger.valueOf(endInclusive.longValue()));

        int signum = minBig.signum() == maxBig.signum() ? minBig.signum() : 1;
        BigInteger offset;
        BigInteger bigRange;
        if (signum == -1 || minBig.equals(BigInteger.ZERO)) {
            offset = minBig.abs();
            bigRange = minBig.abs().add(maxBig.abs());
        } else {
            offset = minBig.negate();
            bigRange = maxBig.subtract(minBig);
        }

        StringBuilder result = new StringBuilder();
        boolean inFree = false;
        char[] bitChars = bigRange.toString(2).toCharArray();
        for (char rangeChar : bitChars) {
            if (inFree) {
                result.append(nextBoolean() ? 1 : 0);
            } else {
                boolean oriBit = rangeChar == '1';
                boolean newBit = nextBoolean();
                inFree = oriBit != newBit && oriBit;

                if (inFree) {
                    result.append(0);
                } else {
                    result.append(oriBit ? 1 : 0);
                }
            }
        }

        BigInteger after = new BigInteger(result.toString(), 2);
        if (signum == -1) {
            return after.negate().subtract(offset);
        } else {
            return after.subtract(offset);
        }
    }

    /**
     * <p>
     * Returns a random BigDecimal within the specified range.
     * </p>
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endInclusive the upper bound (included)
     * @throws IllegalArgumentException if {@code startInclusive > endInclusive} or if {@code startInclusive} is negative
     * @return the random BigDecimal
     */
    public static BigDecimal nextDecimal(Number startInclusive, Number endInclusive, Integer scale) {
        BigDecimal minBig = (startInclusive instanceof BigDecimal) ? (BigDecimal) startInclusive : (startInclusive instanceof BigInteger) ? new BigDecimal(
            (BigInteger) startInclusive) : (BigDecimal.valueOf(startInclusive.doubleValue()));
        BigDecimal maxBig = (endInclusive instanceof BigDecimal) ? (BigDecimal) endInclusive : (endInclusive instanceof BigInteger) ? new BigDecimal(
            (BigInteger) endInclusive) : (BigDecimal.valueOf(endInclusive.doubleValue()));

        if (scale == null) {
            BigDecimal[] minParts = minBig.divideAndRemainder(BigDecimal.ONE);
            BigDecimal[] maxParts = maxBig.divideAndRemainder(BigDecimal.ONE);
            scale = Math.max(minParts[1].scale(), maxParts[1].scale());
        }

        BigDecimal result;
        if (scale == 0) {
            result = new BigDecimal(nextBigInteger(minBig, maxBig));
        } else {
            BigDecimal scaleMul = new BigDecimal("1" + StringUtils.repeat("0", scale));
            BigDecimal scaleMin = minBig.multiply(scaleMul);
            BigDecimal scaleMax = maxBig.multiply(scaleMul);
            result = new BigDecimal(nextBigInteger(scaleMin, scaleMax)).divide(scaleMul);
        }

        return result.setScale(scale, ROUND_DOWN);
    }

    /**
     * <p>
     * Returns a random BigDecimal within the specified range.
     * </p>
     *
     * @param precision precision of random number
     * @param scale scale of random number
     * @return the random BigDecimal
     */
    public static BigDecimal nextDecimal(Integer precision, Integer scale) {
        if (precision == null && scale == null) {
            return BigDecimal.valueOf(nextDouble());
        }
        if (precision == null) {
            precision = scale;
        }
        if (scale == null) {
            scale = 0;
        }

        BigDecimal randomDecimal = new BigDecimal(nextBigInteger(0, new BigInteger(StringUtils.repeat("9", precision))));
        BigDecimal divNum = new BigDecimal("1" + StringUtils.repeat("0", scale));
        randomDecimal = randomDecimal.divide(divNum, scale, ROUND_DOWN).stripTrailingZeros();

        return nextBoolean() ? randomDecimal : randomDecimal.negate();
    }

    /**
     * <p>
     * Returns a random double within the specified range.
     * </p>
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endInclusive
     *            the upper bound (included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endInclusive} or if
     *             {@code startInclusive} is negative
     * @return the random double
     */
    public static double nextDouble(final double startInclusive, final double endInclusive) {
        isTrue(endInclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endInclusive) {
            return startInclusive;
        }

        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextDouble());
    }

    /**
     * <p> Returns a random double within 0 - Double.MAX_VALUE </p>
     *
     * @return the random double
     * @see #nextDouble(double, double)
     * @since 3.5
     */
    public static double nextDouble() {
        return nextDouble(0, Double.MAX_VALUE);
    }

    /**
     * <p>
     * Returns a random float within the specified range.
     * </p>
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endInclusive
     *            the upper bound (included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endInclusive} or if
     *             {@code startInclusive} is negative
     * @return the random float
     */
    public static float nextFloat(final float startInclusive, final float endInclusive) {
        isTrue(endInclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endInclusive) {
            return startInclusive;
        }

        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextFloat());
    }

    /**
     * <p> Returns a random float within 0 - Float.MAX_VALUE </p>
     *
     * @return the random float
     * @see #nextFloat()
     * @since 3.5
     */
    public static float nextFloat() {
        return nextFloat(0, Float.MAX_VALUE);
    }
}
