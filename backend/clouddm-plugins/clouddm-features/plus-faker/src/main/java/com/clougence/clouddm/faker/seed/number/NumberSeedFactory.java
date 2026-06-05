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
package com.clougence.clouddm.faker.seed.number;

import static com.clougence.utils.RandomUtils.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Supplier;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.clouddm.faker.utils.RandomRatio;

/**
 * 数值类型的 SeedFactory
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class NumberSeedFactory implements SeedFactory<NumberSeedConfig> {

    @Override
    public NumberSeedConfig newConfig(SeedConfig contextType) {
        return new NumberSeedConfig();
    }

    @Override
    public Supplier<Serializable> createSeed(NumberSeedConfig seedConfig) {
        NumberType numberType = seedConfig.getNumberType();

        Integer precision = seedConfig.getPrecision();
        RandomRatio<MinMax> minmax = seedConfig.getMinMax();
        minmax.forEach(minMax -> {
            BigDecimal min = fixNumber(minMax.getMin(), (numberType == NumberType.Decimal || numberType == NumberType.BigInt) ? null : BigDecimal.ZERO);
            BigDecimal max = fixNumber(minMax.getMax(), (numberType == NumberType.Decimal || numberType == NumberType.BigInt) ? null : BigDecimal.valueOf(100));
            minMax.setMin(min);
            minMax.setMax(max);
        });

        Integer scale = seedConfig.getScale();
        boolean abs = seedConfig.isAbs();

        boolean allowNullable = seedConfig.isAllowNullable();
        Float nullableRatio = seedConfig.getNullableRatio();
        if (allowNullable && nullableRatio == null) {
            throw new IllegalStateException("allowNullable is true but, nullableRatio missing.");
        }

        return () -> {
            if (allowNullable && nextFloat(0, 100) < nullableRatio) {
                return null;
            } else if (precision != null) {
                return toNumber(randomNumber(precision, scale, numberType), numberType, abs);
            } else {
                return toNumber(randomNumber(minmax, scale, numberType), numberType, abs);
            }
        };
    }

    private BigDecimal fixNumber(BigDecimal decimal, BigDecimal defaultValue) {
        if (decimal == null) {
            return defaultValue;
        } else {
            return decimal;
        }
    }

    private Number randomNumber(RandomRatio<MinMax> minmax, Integer scale, NumberType numberType) {
        MinMax mm = minmax.getByRandom();
        Number min = mm.getMin();
        Number max = mm.getMax();

        switch (numberType) {
            case Bool:
            case Byte:
            case Short:
            case Integer:
            case Int:
            case Long:
            case BigInt:
                return nextBigInteger(min, max);
            case Float:
            case Double:
            case Decimal:
                return nextDecimal(min, max, scale);
            default:
                throw new UnsupportedOperationException(numberType + " randomNumber Unsupported.");
        }
    }

    private Number randomNumber(Integer precision, Integer scale, NumberType numberType) {
        switch (numberType) {
            case Bool:
            case Byte:
            case Short:
            case Integer:
            case Int:
            case Long:
            case BigInt:
            case Float:
            case Double:
            case Decimal:
                return nextDecimal(precision, scale);
            default:
                throw new UnsupportedOperationException(numberType + " randomNumber Unsupported.");
        }
    }

    private Number toNumber(Number number, NumberType classType, boolean abs) {
        switch (classType) {
            case Bool:
                return (byte) (number.intValue() > 0 ? 1 : 0);
            case Byte:
                return abs ? (byte) Math.abs(number.byteValue()) : number.byteValue();
            case Short:
                return abs ? (short) Math.abs(number.shortValue()) : number.shortValue();
            case Integer:
            case Int:
                return abs ? (int) Math.abs(number.intValue()) : number.intValue();
            case Long:
                return abs ? (long) Math.abs(number.longValue()) : number.longValue();
            case Float:
                return abs ? (float) Math.abs(number.floatValue()) : number.floatValue();
            case Double:
                return abs ? (double) Math.abs(number.doubleValue()) : number.doubleValue();
            case BigInt: {
                BigInteger result = null;
                if (number instanceof BigInteger) {
                    result = (BigInteger) number;
                } else if (number instanceof BigDecimal) {
                    result = ((BigDecimal) number).toBigInteger();
                } else {
                    result = BigInteger.valueOf(number.longValue());
                }
                return abs ? result.abs() : result;
            }
            case Decimal: {
                BigDecimal result = null;
                if (number instanceof BigDecimal) {
                    result = (BigDecimal) number;
                } else if (number instanceof BigInteger) {
                    result = new BigDecimal((BigInteger) number);
                } else {
                    result = BigDecimal.valueOf(number.doubleValue());
                }
                return abs ? result.abs() : result;
            }
            default:
                throw new UnsupportedOperationException(classType + " toNumber Unsupported.");
        }
    }
}
