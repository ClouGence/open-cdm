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
package com.clougence.clouddm.faker.seed.mysqltime;

import java.io.Serializable;
import java.time.*;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.date.DateSeedConfig;
import com.clougence.clouddm.faker.seed.date.DateSeedFactory;
import com.clougence.utils.StringUtils;

/**
 * 可以生成负数的时间，例如， MySQL time 类型的值范围为是 '-838:59:59.000000' to '838:59:59.000000'
 * @version : 2022-09-03
 * @author 赵永春 (zyc@hasor.net)
 */
public class MySqlTimeSeedFactory extends DateSeedFactory {

    @Override
    public MySqlTimeSeedConfig newConfig() {
        return new MySqlTimeSeedConfig();
    }

    @Override
    public MySqlTimeSeedConfig newConfig(SeedConfig contextType) {
        return new MySqlTimeSeedConfig();
    }

    protected LocalDateTime passerDateTime(String dateStr, LocalDateTime defaultDate) {
        if (StringUtils.isBlank(dateStr)) {
            return defaultDate;
        }
        if (dateStr.trim().length() < 5) {
            throw new DateTimeException(dateStr + " format error.");
        }

        boolean ne = dateStr.charAt(0) == '-';
        if (ne) {
            dateStr = dateStr.substring(1);
        }

        String[] timeParts = dateStr.split(":");
        if (timeParts.length == 2) {
            return BASE.plusHours(toInt(ne, timeParts[0])).plusMinutes(toInt(ne, timeParts[1]));
        } else if (timeParts.length == 3) {
            String[] secondParts = timeParts[2].split("\\.");
            if (secondParts.length == 1) {
                return BASE.plusHours(toInt(ne, timeParts[0])).plusMinutes(toInt(ne, timeParts[1])).plusSeconds(toInt(ne, secondParts[0]));
            } else {
                secondParts[1] = StringUtils.rightPad(secondParts[1], 9, "0");
                return BASE.plusHours(toInt(ne, timeParts[0])).plusMinutes(toInt(ne, timeParts[1])).plusSeconds(toInt(ne, secondParts[0])).plusNanos(toInt(ne, secondParts[1]));
            }
        } else {
            throw new DateTimeException(dateStr + " format error.");
        }
    }

    protected static int toInt(boolean ne, String intStr) {
        int i = Integer.parseInt(intStr);
        return ne ? -i : i;
    }

    private static final LocalDateTime BASE       = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
    private static final long          ONE_HOUR   = 60 * 60;
    private static final long          ONE_MINUTE = 60;

    protected Serializable convertType(OffsetDateTime defaultValue, DateSeedConfig seedConfig) {
        LocalDateTime localTime = defaultValue.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();

        if (BASE.isBefore(localTime) || BASE.isEqual(localTime)) {
            Duration duration = Duration.between(BASE, localTime);
            long diffHours = duration.toHours();
            int timeNano = localTime.getNano();
            if (timeNano == 0) {
                return String.format("%02d:%02d:%02d", diffHours, localTime.getMinute(), localTime.getSecond());
            } else {
                timeNano = Integer.parseInt(trimEnd(String.valueOf(localTime.getNano()), '0'));
                return String.format("%02d:%02d:%02d.%s", diffHours, localTime.getMinute(), localTime.getSecond(), timeNano);
            }
        } else {
            Duration duration = Duration.between(localTime, BASE);
            long second = duration.getSeconds();

            long hours = second / ONE_HOUR;
            second = second - hours * ONE_HOUR;

            long minutes = second / ONE_MINUTE;
            second = second - minutes * ONE_MINUTE;

            if (duration.getNano() == 0) {
                return String.format("-%02d:%02d:%02d", hours, minutes, second);
            } else {
                int nano = Integer.parseInt(trimEnd(String.valueOf(duration.getNano()), '0'));
                nano = Integer.parseInt(trimEnd(String.valueOf(nano), '0'));
                return String.format("-%02d:%02d:%02d.%s", hours, minutes, second, nano);
            }
        }
    }

    private static String trimEnd(final String str, char trimChar) {
        if (str == null || str.equals("")) {
            return str;
        }

        char[] val = str.toCharArray();
        int len = val.length;
        int st = 0;

        while ((st < len) && (val[len - 1] <= trimChar)) {
            len--;
        }
        return len < val.length ? str.substring(st, len) : str;
    }
}
