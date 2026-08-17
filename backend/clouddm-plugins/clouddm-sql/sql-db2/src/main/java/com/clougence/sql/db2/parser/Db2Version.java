/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.db2.parser;

/** DB2 grammar compatibility levels supported by the parser. */
public enum Db2Version {
    DB2_9(9),
    DB2_10(10),
    DB2_11(11);

    public static final Db2Version LATEST = values()[values().length - 1];
    private final int              major;

    Db2Version(int major){
        this.major = major;
    }

    public static Db2Version parse(String version) {
        String value = version == null ? "" : version;
        for (Db2Version candidate : values()) {
            String major = Integer.toString(candidate.major);
            if (value.equals(major) || value.startsWith(major + ".") || value.contains("v" + major + ".") || value.contains("V" + major + ".") || value.contains("SQL" + major)) {
                return candidate;
            }
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }
}
