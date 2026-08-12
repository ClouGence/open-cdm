/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.clickhouse.sql.parser;

/** ClickHouse compatibility levels that change EXPLAIN syntax. */
public enum ClickHouseVersion {
    CH_20(20),
    CH_22(22),
    CH_24(24),
    CH_26(26);

    public static final ClickHouseVersion LATEST = values()[values().length - 1];
    private final int                     major;

    ClickHouseVersion(int major){
        this.major = major;
    }

    public static ClickHouseVersion parse(String version) {
        int major = parseMajor(version);
        if (major < 0) {
            return LATEST;
        }
        ClickHouseVersion matched = CH_20;
        for (ClickHouseVersion candidate : values()) {
            if (candidate.major > major) {
                break;
            }
            matched = candidate;
        }
        return matched;
    }

    public static boolean ge(ClickHouseVersion source, ClickHouseVersion target) {
        return source.major >= target.major;
    }

    private static int parseMajor(String version) {
        if (version == null || version.isBlank()) {
            return -1;
        }
        String value = version.trim();
        int end = 0;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return -1;
        }
        try {
            return Integer.parseInt(value.substring(0, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
