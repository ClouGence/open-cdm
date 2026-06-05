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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.junit.jupiter.api.Test;

import com.clougence.utils.format.WellKnowFormat;

public class WellKnowFormatTest {

    @Test
    public void testWKF_0() {
        assert "2021-04-26+08:00".equals(WellKnowFormat.WKF_OFFSET_DATE10.format(OffsetDateTime.parse("2021-04-26T12:12:12+08:00")));
    }

    private static final DateTimeFormatter TIME_TZ_FORMAT = new DateTimeFormatterBuilder()//
        .append(DateTimeFormatter.ofPattern("HH:mm:ss.S"))
        .appendOffset("+HH:mm", "")
        .toFormatter();

    @Test
    public void testWKF_1() {
        OffsetDateTime parse = OffsetDateTime.parse("2021-04-26T12:12:12.12345+08:00");

        System.out.println(WellKnowFormat.WKF_OFFSET_TIME24_S1.format(parse));
        System.out.println(TIME_TZ_FORMAT.format(parse));

    }
}
