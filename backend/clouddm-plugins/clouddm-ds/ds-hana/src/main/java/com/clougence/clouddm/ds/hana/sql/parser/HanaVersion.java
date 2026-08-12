/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.sql.parser;

/** SAP HANA grammar compatibility levels supported by the parser. */
public enum HanaVersion {
    HANA_1(1),
    HANA_2(2);

    public static final HanaVersion LATEST = values()[values().length - 1];
    private final int               major;

    HanaVersion(int major){
        this.major = major;
    }

    public static HanaVersion parse(String version) {
        String value = version == null ? "" : version.trim();
        int end = 0;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (end > 0) {
            try {
                int major = Integer.parseInt(value.substring(0, end));
                for (HanaVersion candidate : values()) {
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
