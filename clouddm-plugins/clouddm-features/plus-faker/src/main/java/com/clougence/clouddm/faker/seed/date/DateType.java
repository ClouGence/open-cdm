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

import com.clougence.utils.StringUtils;

/**
 * 生成的时间值最终转换的格式
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DateType {

    String(java.lang.String.class),
    ISO8601(java.lang.String.class),
    JavaDate(java.util.Date.class),
    JavaLong(java.lang.Long.class),
    SqlDate(java.sql.Date.class),
    SqlTime(java.sql.Time.class),
    SqlTimestamp(java.sql.Timestamp.class),

    LocalDate(java.time.LocalDate.class),
    LocalTime(java.time.LocalTime.class),
    LocalDateTime(java.time.LocalDateTime.class),
    Year(java.time.Year.class),
    YearNumber(java.lang.Integer.class),
    YearMonth(java.time.YearMonth.class),
    Month(java.time.Month.class),
    MonthDay(java.time.MonthDay.class),
    DayOfWeek(java.time.DayOfWeek.class),
    OffsetTime(java.time.OffsetTime.class),
    OffsetDateTime(java.time.OffsetDateTime.class),
    ZonedDateTime(java.time.ZonedDateTime.class),
    Instant(java.time.Instant.class);

    private final Class<?> dateType;

    DateType(Class<?> dateType){
        this.dateType = dateType;
    }

    public Class<?> getDateType() { return this.dateType; }

    public static DateType valueOfCode(String name) {
        for (DateType scope : DateType.values()) {
            if (StringUtils.equalsIgnoreCase(scope.name(), name)) {
                return scope;
            }
        }
        return null;
    }
}
