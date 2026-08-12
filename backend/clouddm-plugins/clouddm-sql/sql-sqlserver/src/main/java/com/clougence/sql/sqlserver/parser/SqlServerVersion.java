/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.sqlserver.parser;

/** SQL Server grammar compatibility levels supported by the parser. */
public enum SqlServerVersion {
    MSSQL_2000(8, 2000),
    MSSQL_2005(9, 2005),
    MSSQL_2008(10, 2008),
    MSSQL_2012(11, 2012),
    MSSQL_2014(12, 2014),
    MSSQL_2016(13, 2016),
    MSSQL_2017(14, 2017),
    MSSQL_2019(15, 2019),
    MSSQL_2022(16, 2022);

    public static final SqlServerVersion LATEST = values()[values().length - 1];

    private final int                    productMajor;
    private final int                    release;

    SqlServerVersion(int productMajor, int release){
        this.productMajor = productMajor;
        this.release = release;
    }

    public static SqlServerVersion parse(String version) {
        String value = version == null ? "" : version.trim();
        int end = 0;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (end > 0) {
            try {
                int number = Integer.parseInt(value.substring(0, end));
                for (SqlServerVersion candidate : values()) {
                    if (candidate.productMajor == number || candidate.release == number) {
                        return candidate;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.release);
    }
}
