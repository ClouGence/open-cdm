/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.starrocks.sql.parser;

/** StarRocks grammar compatibility levels supported by the parser. */
public enum StarRocksVersion {
    SR_1(1),
    SR_2(2),
    SR_3(3),
    SR_4(4),
    SR_5(5);

    public static final StarRocksVersion LATEST = values()[values().length - 1];

    private final int                    major;

    StarRocksVersion(int major){
        this.major = major;
    }

    public static StarRocksVersion parse(String version) {
        String value = version == null ? "" : version;
        int start = 0;
        while (start < value.length() && !Character.isDigit(value.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        int major = -1;
        if (start < end) {
            try {
                major = Integer.parseInt(value.substring(start, end));
            } catch (NumberFormatException ignored) {
            }
        }
        for (StarRocksVersion candidate : values()) {
            if (candidate.major == major) {
                return candidate;
            }
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }
}
