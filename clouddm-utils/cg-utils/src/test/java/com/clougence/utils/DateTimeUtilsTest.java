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
