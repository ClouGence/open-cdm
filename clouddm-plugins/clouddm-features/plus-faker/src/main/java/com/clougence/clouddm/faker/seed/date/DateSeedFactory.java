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
package com.clougence.clouddm.faker.seed.date;

import static com.clougence.utils.RandomUtils.nextBigInteger;
import static com.clougence.utils.RandomUtils.nextFloat;
import static java.math.BigDecimal.ROUND_DOWN;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.function.Supplier;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.utils.StringUtils;

/**
 * 时间类型的 SeedFactory（BC、AD，不支持 BC）
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class DateSeedFactory implements SeedFactory<DateSeedConfig> {

    private static final BigDecimal ONE_SEC_NANO = new BigDecimal("1000000000"); // 1 sec

    @Override
    public DateSeedConfig newConfig(SeedConfig contextType) {
        return new DateSeedConfig();
    }

    @Override
    public Supplier<Serializable> createSeed(DateSeedConfig seedConfig) {
        boolean allowNullable = seedConfig.isAllowNullable();
        Float nullableRatio = seedConfig.getNullableRatio();
        if (allowNullable && nullableRatio == null) {
            throw new IllegalStateException("allowNullable is true but, nullableRatio missing.");
        }
        Supplier<Boolean> seedNull = () -> allowNullable && nextFloat(0, 100) < nullableRatio;

        switch (seedConfig.getGenType()) {
            case SysData:
                return seedSysData(seedConfig, seedNull);
            case Fixed:
                return seedFixed(seedConfig, seedNull);
            case Random:
                return seedRandom(seedConfig, seedNull);
            case Interval:
                return seedInterval(seedConfig, seedNull);
            default:
                throw new UnsupportedOperationException("genType " + seedConfig.getGenType() + " Unsupported.");
        }
    }

    protected Supplier<Serializable> seedSysData(DateSeedConfig seedConfig, Supplier<Boolean> seedNull) {
        return () -> {
            if (seedNull.get()) {
                return null;
            } else {
                return convertType(OffsetDateTime.now(), seedConfig);
            }
        };
    }

    protected Supplier<Serializable> seedFixed(DateSeedConfig seedConfig, Supplier<Boolean> seedNull) {
        LocalDateTime passedTime = null;
        if (StringUtils.isNotBlank(seedConfig.getRangeForm())) {
            passedTime = passerDateTime(seedConfig.getRangeForm(), LocalDateTime.now());
        } else if (StringUtils.isNotBlank(seedConfig.getRangeTo())) {
            passedTime = passerDateTime(seedConfig.getRangeTo(), LocalDateTime.now());
        } else {
            passedTime = LocalDateTime.now();
        }

        ZoneOffset zoneOffset = null;
        if (StringUtils.isNotBlank(seedConfig.getZoneForm())) {
            zoneOffset = passerZoned(seedConfig.getZoneForm(), ZoneOffset.UTC);
        } else if (StringUtils.isNotBlank(seedConfig.getZoneTo())) {
            zoneOffset = passerZoned(seedConfig.getZoneTo(), ZoneOffset.UTC);
        } else {
            zoneOffset = ZoneOffset.UTC;
        }

        OffsetDateTime zonedDateTime = OffsetDateTime.of(passedTime, zoneOffset);
        Serializable result = convertType(zonedDateTime, seedConfig);
        return () -> {
            if (seedNull.get()) {
                return null;
            } else {
                return result;
            }
        };
    }

    protected Supplier<Serializable> seedRandom(DateSeedConfig seedConfig, Supplier<Boolean> seedNull) {
        LocalDateTime startTime = passerDateTime(seedConfig.getRangeForm(), LocalDateTime.now().plusYears(-10));
        LocalDateTime endTime = passerDateTime(seedConfig.getRangeTo(), LocalDateTime.now().plusYears(10));
        ZoneOffset startZoned = passerZoned(seedConfig.getZoneForm(), ZoneOffset.UTC);
        ZoneOffset endZoned = passerZoned(seedConfig.getZoneTo(), ZoneOffset.UTC);

        return () -> {
            if (seedNull.get()) {
                return null;
            }

            LocalDateTime randomTime = nextNanoTime(startTime, endTime);
            OffsetDateTime randomZoned = nextZonedTime(randomTime, startZoned, endZoned);
            return convertType(randomZoned, seedConfig);
        };
    }

    protected Supplier<Serializable> seedInterval(DateSeedConfig seedConfig, Supplier<Boolean> seedNull) {
        OffsetDateTime startTime = OffsetDateTime.of(passerDateTime(seedConfig.getStartTime(), LocalDateTime.now()), ZoneOffset.UTC);
        int minInterval = fixInt(seedConfig.getMinInterval(), -100);
        int maxInterval = fixInt(seedConfig.getMaxInterval(), +100);
        IntervalScope intervalScope = seedConfig.getIntervalScope();

        return () -> {
            if (seedNull.get()) {
                return null;
            }

            OffsetDateTime passedTime = nextInterval(startTime, minInterval, maxInterval, intervalScope);
            return convertType(passedTime, seedConfig);
        };
    }

    protected LocalDateTime passerDateTime(String dateStr, LocalDateTime defaultDate) {
        if (StringUtils.isBlank(dateStr)) {
            return defaultDate;
        }
        if (dateStr.trim().length() < 4) {
            throw new DateTimeException(dateStr + " format error.");
        }

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
                    String[] dataParts = dataTime[0].split("-");
                    String[] timeParts = dataTime[1].split(":");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), toInt(timeParts[0]), toInt(timeParts[1]), 0, 0);
                }
                case 19: {
                    String[] dataTime = dateStr.split(" ");
                    String[] dataParts = dataTime[0].split("-");
                    String[] timeParts = dataTime[1].split(":");
                    return LocalDateTime.of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), toInt(timeParts[0]), toInt(timeParts[1]), toInt(timeParts[2]), 0);
                }
                default: {
                    String[] dataTime = dateStr.split(" ");
                    String[] dataParts = dataTime[0].split("-");
                    String[] timeParts = dataTime[1].split(":");
                    String[] secondParts = timeParts[2].split("\\.");
                    secondParts[1] = StringUtils.rightPad(secondParts[1], 9, "0");
                    return LocalDateTime
                        .of(toInt(dataParts[0]), toInt(dataParts[1]), toInt(dataParts[2]), toInt(timeParts[0]), toInt(timeParts[1]), toInt(secondParts[0]), toInt(secondParts[1]));
                }
            }

        } else if (dateStr.charAt(2) == ':') {
            switch (len) {
                case 5: {
                    String[] timeParts = dateStr.split(":");
                    return LocalDateTime.of(0, 1, 1, toInt(timeParts[0]), toInt(timeParts[1]), 0, 0);
                }
                case 8: {
                    String[] timeParts = dateStr.split(":");
                    return LocalDateTime.of(0, 1, 1, toInt(timeParts[0]), toInt(timeParts[1]), toInt(timeParts[2]), 0);
                }
                default: {
                    String[] timeParts = dateStr.split(":");
                    String[] secondParts = timeParts[2].split("\\.");
                    secondParts[1] = StringUtils.rightPad(secondParts[1], 9, "0");
                    return LocalDateTime.of(0, 1, 1, toInt(timeParts[0]), toInt(timeParts[1]), toInt(secondParts[0]), toInt(secondParts[1]));
                }
            }
        } else {
            throw new DateTimeException(dateStr + " format error.");
        }
    }

    protected ZoneOffset passerZoned(String zonedStr, ZoneOffset defaultValue) {
        if (StringUtils.isNotBlank(zonedStr)) {
            return ZoneOffset.of(zonedStr);
        } else {
            return defaultValue;
        }
    }

    protected LocalDateTime nextNanoTime(LocalDateTime startTime, LocalDateTime endTime) {
        Instant startInstant = startTime.toInstant(ZoneOffset.UTC);
        Instant endInstant = endTime.toInstant(ZoneOffset.UTC);

        BigDecimal startEpochNano = BigDecimal.valueOf(startInstant.getEpochSecond()).multiply(ONE_SEC_NANO);
        BigDecimal endEpochNano = BigDecimal.valueOf(endInstant.getEpochSecond()).multiply(ONE_SEC_NANO);

        if (startEpochNano.signum() == -1) {
            startEpochNano = startEpochNano.subtract(BigDecimal.valueOf(startInstant.getNano()));
        } else {
            startEpochNano = startEpochNano.add(BigDecimal.valueOf(startInstant.getNano()));
        }
        if (endEpochNano.signum() == -1) {
            endEpochNano = endEpochNano.subtract(BigDecimal.valueOf(endInstant.getNano()));
        } else {
            endEpochNano = endEpochNano.add(BigDecimal.valueOf(endInstant.getNano()));
        }

        BigDecimal randomNano = new BigDecimal(nextBigInteger(startEpochNano, endEpochNano));
        BigDecimal[] timeParts = randomNano.divide(ONE_SEC_NANO, 9, ROUND_DOWN).divideAndRemainder(BigDecimal.ONE);
        timeParts[1] = timeParts[1].multiply(ONE_SEC_NANO);
        return LocalDateTime.ofEpochSecond(timeParts[0].longValue(), timeParts[1].abs().intValue(), ZoneOffset.UTC);
    }

    protected OffsetDateTime nextZonedTime(LocalDateTime dateTime, ZoneOffset startZoned, ZoneOffset endZoned) {
        int startZonedSec = ((startZoned == null) ? ZoneOffset.UTC : startZoned).getTotalSeconds();
        int endZonedSec = ((endZoned == null) ? ZoneOffset.UTC : endZoned).getTotalSeconds();
        int randomZoned = nextBigInteger(BigInteger.valueOf(startZonedSec), BigInteger.valueOf(endZonedSec)).intValue();

        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(randomZoned);
        return OffsetDateTime.of(dateTime, ZoneOffset.UTC).withOffsetSameInstant(zoneOffset);
    }

    protected OffsetDateTime nextInterval(OffsetDateTime startTime, int minInterval, int maxInterval, IntervalScope scope) {
        long interval = nextBigInteger(BigInteger.valueOf(minInterval), BigInteger.valueOf(maxInterval)).intValue();
        switch (scope) {
            case Year:
                return startTime.plusYears(interval);
            case Month:
                return startTime.plusMonths(interval);
            case Day:
                return startTime.plusDays(interval);
            case Week:
                return startTime.plusWeeks(interval);
            case Hours:
                return startTime.plusHours(interval);
            case Minute:
                return startTime.plusMinutes(interval);
            case Second:
                return startTime.plusSeconds(interval);
            case Milli:
                return startTime.plusNanos(interval * 1000000);
            case Micro:
                return startTime.plusNanos(interval * 1000);
            case Nano:
                return startTime.plusNanos(interval);
            default:
                throw new UnsupportedOperationException("intervalScope " + scope + " Unsupported.");
        }
    }

    protected static int toInt(String intStr) {
        return Integer.parseInt(intStr);
    }

    protected static int fixInt(Integer decimal, int defaultValue) {
        if (decimal == null) {
            return defaultValue;
        } else {
            return decimal;
        }
    }

    protected Serializable convertType(OffsetDateTime defaultValue, DateSeedConfig seedConfig) {
        Integer precisionOfSecond = seedConfig.getPrecision();
        if (precisionOfSecond == null || precisionOfSecond > 9) {
            precisionOfSecond = 9;
        }

        BigDecimal nano = new BigDecimal(defaultValue.getNano()).divide(ONE_SEC_NANO, 9, ROUND_DOWN);
        nano = nano.setScale(precisionOfSecond, ROUND_DOWN).multiply(ONE_SEC_NANO).stripTrailingZeros();
        defaultValue = defaultValue.withNano(nano.intValue());

        switch (seedConfig.getDateType()) {
            case JavaDate:
                return new java.util.Date(defaultValue.toInstant().toEpochMilli());
            case JavaLong:
                return defaultValue.toInstant().toEpochMilli();
            case SqlDate:
                return new java.sql.Date(defaultValue.toInstant().toEpochMilli());
            case SqlTime:
                return new java.sql.Time(defaultValue.toInstant().toEpochMilli());
            case SqlTimestamp:
                return new java.sql.Timestamp(defaultValue.toInstant().toEpochMilli());
            case LocalDate:
                return defaultValue.toLocalDateTime().toLocalDate();
            case LocalTime:
                return defaultValue.toLocalDateTime().toLocalTime();
            case LocalDateTime:
                return defaultValue.toLocalDateTime();
            case Year:
                return Year.of(defaultValue.getYear());
            case YearNumber:
                return defaultValue.getYear();
            case YearMonth:
                return YearMonth.of(defaultValue.getYear(), defaultValue.getMonth());
            case Month:
                return defaultValue.getMonth();
            case MonthDay:
                return MonthDay.of(defaultValue.getMonth(), defaultValue.getDayOfMonth());
            case DayOfWeek:
                return defaultValue.getDayOfWeek();
            case OffsetTime:
                return defaultValue.toOffsetTime();
            case OffsetDateTime:
                return defaultValue;
            case Instant:
                return defaultValue.toInstant();
            case String:
                return seedConfig.getDateTimeFormatter().format(defaultValue);
            case ISO8601: {
                String pPart = String.format("P%s-%s-%s", defaultValue.getYear(), defaultValue.getMonthValue(), defaultValue.getDayOfMonth());
                String tPart = String.format("T%s:%s:%s", defaultValue.getHour(), defaultValue.getMinute(), defaultValue.getSecond());
                if (defaultValue.getNano() > 0) {
                    tPart = tPart + "." + trimEnd(String.valueOf(defaultValue.getNano()), '0');
                }
                return pPart + tPart;
            }
            case ZonedDateTime:
                return defaultValue.toZonedDateTime();
            default:
                return defaultValue;
        }
    }

    private static String trimEnd(final String str, char trimChar) {
        if (str == null || str.equals("")) {
            return str;
        }

        char[] val = str.toCharArray();
        int len = val.length;
        int st = 0;

        while ((st < len) && (val[len - 1] == trimChar)) {
            len--;
        }
        return len < val.length ? str.substring(st, len) : str;
    }
}
