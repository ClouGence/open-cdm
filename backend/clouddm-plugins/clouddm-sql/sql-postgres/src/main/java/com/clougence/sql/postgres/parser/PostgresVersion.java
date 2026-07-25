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
            int majorVersion = Integer.parseInt(version.trim());
            for (PostgresVersion v : values()) {
                if (v.major == majorVersion) {
                    return v;
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return LATEST;
    }

    private final int major;

    PostgresVersion(int major){
        this.major = major;
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
}
