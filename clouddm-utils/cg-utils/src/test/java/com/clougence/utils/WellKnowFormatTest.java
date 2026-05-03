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
