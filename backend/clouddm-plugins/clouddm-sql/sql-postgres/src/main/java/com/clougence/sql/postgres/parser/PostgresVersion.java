package com.clougence.sql.postgres.parser;

/** PostgreSQL grammar compatibility levels supported by the parser. */
public enum PostgresVersion {
    POSTGRES_12(12),
    POSTGRES_13(13),
    POSTGRES_14(14),
    POSTGRES_15(15),
    POSTGRES_16(16),
    POSTGRES_17(17),
    POSTGRES_18(18);

    public static final PostgresVersion LATEST = values()[values().length - 1];

    /**
     * Parse a version string (e.g. "12", "13", "14", "15", "16", "17", "18") to a {@link PostgresVersion}.
     * Returns {@link #LATEST} if the string is null, blank, or does not match any known version.
     */
    public static PostgresVersion parse(String version) {
        if (version == null || version.isBlank()) {
            return LATEST;
        }
        try {
            String value = version.trim();
            int majorEnd = 0;
            while (majorEnd < value.length() && Character.isDigit(value.charAt(majorEnd))) {
                majorEnd++;
            }
            if (majorEnd == 0) {
                return LATEST;
            }
            if (majorEnd < value.length()) {
                char delimiter = value.charAt(majorEnd);
                if (delimiter != '.' && delimiter != '-' && delimiter != '+' && delimiter != '_' && !Character.isWhitespace(delimiter)) {
                    return LATEST;
                }
            }
            int majorVersion = Integer.parseInt(value.substring(0, majorEnd));
            for (PostgresVersion v : values()) {
                if (v.major == majorVersion) {
                    return v;
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }

    private final int major;

    PostgresVersion(int major){
        this.major = major;
    }

    public int major() {
        return this.major;
    }

    public boolean atLeast(PostgresVersion minimum) {
        return this.major >= minimum.major;
    }

    public boolean atMost(PostgresVersion maximum) {
        return this.major <= maximum.major;
    }

    public boolean between(PostgresVersion minimum, PostgresVersion maximum) {
        return atLeast(minimum) && atMost(maximum);
    }

    public static boolean ge(PostgresVersion source, PostgresVersion target) {
        return source.major >= target.major;
    }

    public static boolean le(PostgresVersion source, PostgresVersion target) {
        return source.major <= target.major;
    }
}
