/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.dameng.sql.parser;

/** Dameng grammar compatibility levels supported by the parser. */
public enum DmVersion {
    DM_6(6),
    DM_7(7),
    DM_8(8);

    public static final DmVersion LATEST = values()[values().length - 1];
    private final int             major;

    DmVersion(int major){
        this.major = major;
    }

    public static DmVersion parse(String version) {
        int major = parseMajor(version);
        for (DmVersion candidate : values()) {
            if (candidate.major == major) {
                return candidate;
            }
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }

    public static boolean ge(DmVersion source, DmVersion target) {
        return source.major >= target.major;
    }

    private static int parseMajor(String version) {
        if (version == null || version.isBlank()) {
            return -1;
        }
        String value = version.trim();
        int start = 0;
        while (start < value.length() && !Character.isDigit(value.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (start == end) {
            return -1;
        }
        try {
            return Integer.parseInt(value.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
