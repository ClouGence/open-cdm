/*
 * Copyright 2008-2009 the original author or authors.
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

import org.junit.jupiter.api.Test;

import com.clougence.utils.format.DateFormatType;

public class DateFormatTypeTest {

    @Test
    public void equalsIgnoreCase_00() {
        System.out.println("##" + DateTimeUtils.safeToOffsetDateTime("2022-12-21+10", null));
        System.out.println("##" + DateTimeUtils.safeToOffsetDateTime("22:12:21+10", null));
        System.out.println("##" + DateTimeUtils.safeToOffsetDateTime("2022-12-21", null));
    }

    @Test
    public void equalsIgnoreCase_01() {
        DateFormatType formatType1 = DateFormatType.passerType("2001-02-03");
        System.out.println(formatType1.toLocalDateTime("2001-02-03"));

        DateFormatType formatType2 = DateFormatType.passerType("01:02:03.123");
        System.out.println(formatType2.toLocalDateTime("01:02:03.123"));

        DateFormatType formatType3 = DateFormatType.passerType("2001-02-03 01:02:03.123");
        System.out.println(formatType3.toLocalDateTime("2001-02-03 01:02:03.123"));
    }

    @Test
    public void testStrProcess() {
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02Z", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-4", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-14", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-04:11", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-4:11", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-0411", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-04:11:11", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02-041111", null));
        //
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02Z", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02+4", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02+14", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02+04:11", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02+0411", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02+04:11:11", null));
        System.out.println(DateTimeUtils.safeToOffsetDateTime("04:01:02+041111", null));

    }
}
