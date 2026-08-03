/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.doris.parser;

/** Doris major grammar families selected by {@code SqlParserParameters}. */
public enum DorisVersion {
    DORIS_2(2),
    DORIS_3(3),
    DORIS_4(4);

    public static final DorisVersion LATEST = DORIS_4;

    private final int                major;

    DorisVersion(int major){
        this.major = major;
    }

    public int major() {
        return major;
    }

    public boolean atLeast(DorisVersion minimum) {
        return this.major >= minimum.major;
    }

    public boolean atMost(DorisVersion maximum) {
        return this.major <= maximum.major;
    }

    public boolean supportsBitmapIndex() {
        return atMost(DORIS_3);
    }

    public boolean supportsAnnIndex() {
        return atLeast(DORIS_4);
    }

    public static DorisVersion parse(String version) {
        if (version == null || version.isBlank()) {
            return LATEST;
        }
        String value = version.trim();
        if (value.charAt(0) == 'v' || value.charAt(0) == 'V') {
            value = value.substring(1);
        }
        int end = 0;
        while (end < value.length() && !Character.isDigit(value.charAt(end))) {
            end++;
        }
        int start = end;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (end == start) {
            return LATEST;
        }
        int major;
        try {
            major = Integer.parseInt(value.substring(start, end));
        } catch (NumberFormatException e) {
            return LATEST;
        }
        return switch (major) {
            case 2 -> DORIS_2;
            case 3 -> DORIS_3;
            default -> DORIS_4;
        };
    }
}
