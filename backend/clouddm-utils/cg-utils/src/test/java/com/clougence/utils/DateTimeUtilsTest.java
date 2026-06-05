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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author bucketli 2024/7/5 14:30:10
 */
public class DateTimeUtilsTest {

    //2021-07-09 18:05:15.001230
    @Test
    public void testExtractTime_1() {
        String s = DateTimeUtils.extractTime("2021-07-09 18:05:15.001230", 3);
        Assertions.assertEquals("18:05:15.001", s);
    }

    //2021-07-09 18:05:15.001230+07:00
    @Test
    public void testExtractTime_2() {
        String s = DateTimeUtils.extractTime("2021-07-09 18:05:15.001230+07:00", 3);
        Assertions.assertEquals("18:05:15.001", s);
    }

    //2021-07-09 18:05:15.001230 Asia/Shanghai
    @Test
    public void testExtractTime_3() {
        String s = DateTimeUtils.extractTime("2021-07-09 18:05:15.001230 Asia/Shanghai", 3);
        Assertions.assertEquals("18:05:15.001", s);
    }

    //2021-07-09 18:05:15.001230Asia/Shanghai
    @Test
    public void testExtractTime_4_fail() {
        try {
            String s = DateTimeUtils.extractTime("2021-07-09 18:05:15.001230Asia/Shanghai", 3);
            Assertions.fail();
        } catch (Exception e) {
        }
    }

    //2021-07-09 18:05:15.001
    @Test
    public void testExtractTime_5() {
        String s = DateTimeUtils.extractTime("2021-07-09 18:05:15.001", 2);
        Assertions.assertEquals("18:05:15", s);
    }
}
