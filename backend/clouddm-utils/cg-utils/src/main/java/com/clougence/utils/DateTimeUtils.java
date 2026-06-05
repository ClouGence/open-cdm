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
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @version : 2011-6-3
 * @author 赵永春 (zyc@hasor.net)
 */
public class DateTimeUtils {

    public static Date plusYearDate(Date date, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, sec);
        return cal.getTime();
    }

    public static Date plusMonthDate(Date date, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, sec);
        return cal.getTime();
    }

    public static Date plusDayDate(Date date, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, sec);
        return cal.getTime();
    }

    public static Date plusHourDate(Date date, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, sec);
        return cal.getTime();
    }

    public static Date plusMinuteDate(Date date, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, sec);
        return cal.getTime();
    }

    public static Date plusSecDate(Date date, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, sec);
        return cal.getTime();
    }

    // 2021-07-09 18:05:15.000000+12:33 -> 2021-07-09
    // 2021-07-09 18:05:15.000000Z      -> 2021-07-09
    //            18:05:15.000000+12:33 ->
    //            18:05:15.000000Z      ->
    // 2021-07-09                       -> 2021-07-09
    public static String extractDate(String datetime) {
        if (datetime.length() >= 10 && datetime.charAt(4) == '-') {
            return datetime.substring(0, 10);
        } else {
            return "";
        }
    }

    // 2021-07-09 18:05:15.000000 -> 18:05:15
    public static String extractTimeWithoutNano(String datetime) {
        String onlyTime = datetime;
        if (StringUtils.isBlank(datetime)) {
            return "";
        } else if (datetime.length() >= 10 && datetime.charAt(4) == '-') {
            if (datetime.length() == 10) {
                return "";
            } else {
                onlyTime = datetime.substring(11);
            }
        }

        onlyTime = removeZone(onlyTime);

        if (onlyTime.indexOf(".") > 0) {
            String[] timed = onlyTime.split("\\.");
            return timed[0];
        } else {
            return onlyTime;
        }
    }

    // 2021-07-09 18:05:15.000000 -> 000000
    public static String extractNano(String datetime) {
        String onlyTime;
        if (StringUtils.isBlank(datetime) || datetime.length() <= 19) {
            return "";
        }
        onlyTime = removeZone(datetime.substring(11));

        if (onlyTime.indexOf(".") > 0) {
            String[] timed = onlyTime.split("\\.");
            String nano = StringUtils.trim(timed[1]);
            int spaceIndex = nano.indexOf(" ");
            if (spaceIndex > 0) {
                return nano.substring(0, spaceIndex);
            }
            return timed[1];
        }
        return "";
    }

    // 2021-07-09 18:05:15.000000 -> 2021-07-09
    public static String extractTime(String datetime, int digits) {
        String onlyTime = datetime;
        if (StringUtils.isBlank(datetime)) {
            return "";
        } else if (datetime.length() >= 10 && datetime.charAt(4) == '-') {
            if (datetime.length() == 10) {
                return "";
            } else {
                onlyTime = datetime.substring(11);
            }
        }

        // remove zone
        onlyTime = removeZone(onlyTime);

        if (onlyTime.indexOf(".") > 0) {
            String[] timed = onlyTime.split("\\.");
            BigDecimal decimal;
            try {
                decimal = new BigDecimal("0." + timed[1]);
                decimal = decimal.setScale(digits, RoundingMode.DOWN);
            } catch (Exception e) {
                //e.g., Asia/Shanghai
                if (timed[1].contains(" ")) {
                    int idx = timed[1].indexOf(" ");
                    if (idx == -1) {
                        throw new IllegalArgumentException(e);
                    }

                    String time = timed[1].substring(0, idx);
                    decimal = new BigDecimal("0." + time);
                    decimal = decimal.setScale(digits, RoundingMode.DOWN);
                } else {
                    throw new IllegalArgumentException(e);
                }
            }

            String decDigits = decimal.stripTrailingZeros().toPlainString();
            return "0".equals(decDigits) ? timed[0] : (timed[0] + "." + decDigits.substring(2));
        } else {
            return onlyTime;
        }
    }

    private static String removeZone(String datetime) {
        if (datetime == null || datetime.length() == 0) {
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

    public static OffsetDateTime safeToOffsetDateTime(Object value) {
        return safeToOffsetDateTime(value, "+00:00");
    }

    public static OffsetDateTime safeToOffsetDateTime(Object value, String defaultZone) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return null;
        } else if (value instanceof OffsetDateTime) {
            return (OffsetDateTime) value;
        } else {
            LocalDateTime localDateTime = safeToLocalDateTime(value);
            String zoneValue = extractZone(value.toString());
            if (StringUtils.isBlank(zoneValue) && StringUtils.isNotBlank(defaultZone)) {
                zoneValue = defaultZone;
            }

            if (StringUtils.isBlank(zoneValue)) {
                return localDateTime.atOffset(ZoneOffset.UTC);
            } else {
                ZoneOffset zoneOffset = ZoneOffset.of(zoneValue);
                return OffsetDateTime.of(localDateTime, zoneOffset);
            }
        }
    }

    // only allow: null,number(4 len),String(yyyy-MM-dd HH:mm:ss.SSSSSS),java.sql.Timestamp,java.sql.Date,java.sql.Time,java.util.Date,LocalDateTime,OffsetDateTime
    public static LocalDateTime safeToLocalDateTime(Object value) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return null;
        } else if (value instanceof Temporal) {
            if (value instanceof LocalDateTime) {
                return (LocalDateTime) value;
            } else if (value instanceof OffsetDateTime) {
                return ((OffsetDateTime) value).toLocalDateTime(); // drop zone
            } else if (value instanceof LocalTime) {
                LocalTime localTime = (LocalTime) value;
                return LocalDateTime.of(LocalDate.of(1970, 1, 1), localTime);
            } else if (value instanceof LocalDate) {
                LocalDate localDate = (LocalDate) value;
                return LocalDateTime.of(localDate, LocalTime.of(0, 0));
            } else {
                throw new UnsupportedOperationException(value.getClass() + ", value '" + value + "' , safeToLocalDateTime failed.");
            }
        } else if (value instanceof Date) {
            if (value instanceof java.sql.Timestamp) {
                long nanos = ((java.sql.Timestamp) value).getNanos();
                long timeMilli = ((java.sql.Timestamp) value).getTime();
                timeMilli = timeMilli - (nanos / 1000000);
                Instant instant = Instant.ofEpochMilli(timeMilli).plusNanos(nanos);
                return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();// first to ZonedDateTime ,then drop zone.
            } else if (value instanceof java.sql.Date) {
                return ((java.sql.Date) value).toLocalDate().atTime(0, 0);
            } else if (value instanceof java.sql.Time) {
                long timeMilli = ((java.sql.Time) value).getTime();
                Instant instant = Instant.ofEpochMilli(timeMilli);
                LocalTime localTime = instant.atZone(ZoneId.systemDefault()).toLocalTime();
                return LocalDateTime.of(LocalDate.of(1970, 1, 1), localTime);// first to ZonedDateTime ,then drop zone.
            } else {
                Instant instant = ((Date) value).toInstant();
                return instant.atZone(ZoneId.systemDefault()).toLocalDateTime(); // first to ZonedDateTime ,then drop zone.
            }
        } else {
            return stringSafeToLocalDateTime(value);
        }
    }

    private static LocalDateTime stringSafeToLocalDateTime(Object value) {
        String dateStr = value.toString();
        if (dateStr.trim().length() < 4) {
            if (StringUtils.isNumeric(dateStr)) {
                return LocalDateTime.of(toInt(dateStr), 1, 1, 0, 0, 0, 0);
            } else {
                throw new DateTimeException(dateStr + " format error.");
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
            throw new UnsupportedOperationException(value.getClass() + ", value '" + value + "' , safeToLocalDateTime failed.");
        }
    }

    private static int toInt(String intStr) {
        return Integer.parseInt(intStr);
    }
}
