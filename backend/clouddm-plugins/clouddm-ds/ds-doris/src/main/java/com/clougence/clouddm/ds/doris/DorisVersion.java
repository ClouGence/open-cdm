/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.doris;

/** Doris major versions used by datasource runtime compatibility logic. */
public enum DorisVersion {
    DORIS_1(1),
    DORIS_2(2),
    DORIS_3(3),
    DORIS_4(4);

    public static final DorisVersion LATEST = values()[values().length - 1];

    private final int                major;

    DorisVersion(int major){
        this.major = major;
    }

    public static DorisVersion parse(String version) {
        String value = version == null ? "" : version;
        int start = 0;
        while (start < value.length() && !Character.isDigit(value.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (start < end) {
            try {
                int major = Integer.parseInt(value.substring(start, end));
                for (DorisVersion candidate : values()) {
                    if (candidate.major == major) {
                        return candidate;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }
}
