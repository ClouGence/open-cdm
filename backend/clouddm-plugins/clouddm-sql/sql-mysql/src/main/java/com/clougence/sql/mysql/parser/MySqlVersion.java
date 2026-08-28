package com.clougence.sql.mysql.parser;

/** MySQL grammar compatibility levels supported by the parser. */
public enum MySqlVersion {
    MYSQL_5_6(5, 6),
    MYSQL_5_7(5, 7),
    MYSQL_8_0(8, 0),
    MYSQL_8_4(8, 4),
    MYSQL_9_7(9, 7);

    public static final MySqlVersion LATEST = values()[values().length - 1];

    /**
     * Parse a version string (e.g. "5.7", "8.0", "8.4", "9.7") to a {@link MySqlVersion}.
     * Returns {@link #LATEST} if the string is null, blank, or does not match any known version.
     */
    public static MySqlVersion parse(String version) {
        if (version == null || version.isBlank()) {
            return LATEST;
        }
        VersionParts parts = parseParts(version);
        if (parts == null) {
            return LATEST;
        }
        for (MySqlVersion v : values()) {
            int major = v.value / 100;
            int minor = v.value % 100;
            if (parts.major() == major && parts.minor() == minor) {
                return v;
            }
        }
        return LATEST;
    }

    public static int parseExactVersion(String version) {
        if (version == null || version.isBlank()) {
            return LATEST.exactVersion();
        }
        VersionParts parts = parseParts(version);
        if (parts == null) {
            throw new IllegalArgumentException("Invalid MySQL version: " + version);
        }
        int major = parts.major();
        int minor = parts.minor();
        int release = parts.release();
        if (major > 99 || minor > 99 || release > 99) {
            throw new IllegalArgumentException("Invalid MySQL version: " + version);
        }
        return major * 10000 + minor * 100 + release;
    }

    public static int parseExactVersionCode(String exactVersion) {
        String value = exactVersion == null ? "" : exactVersion.trim();
        if (value.length() < 5 || value.length() > 6 || !allDigits(value, 0, value.length())) {
            throw new IllegalArgumentException("Invalid MySQL exact version: " + exactVersion);
        }
        return Integer.parseInt(value);
    }

    private static VersionParts parseParts(String version) {
        if (version == null) {
            return null;
        }
        String value = version.trim();
        int majorEnd = digitEnd(value, 0);
        if (majorEnd == 0 || majorEnd >= value.length() || value.charAt(majorEnd) != '.') {
            return null;
        }
        int minorStart = majorEnd + 1;
        int minorEnd = digitEnd(value, minorStart);
        if (minorEnd == minorStart) {
            return null;
        }
        int release = 0;
        int suffixStart = minorEnd;
        if (minorEnd < value.length() && value.charAt(minorEnd) == '.') {
            int releaseStart = minorEnd + 1;
            int releaseEnd = digitEnd(value, releaseStart);
            if (releaseEnd == releaseStart) {
                return null;
            }
            release = Integer.parseInt(value.substring(releaseStart, releaseEnd));
            suffixStart = releaseEnd;
        }
        if (suffixStart < value.length()) {
            char delimiter = value.charAt(suffixStart);
            if (delimiter != '-' && delimiter != '+' && delimiter != '_' && !Character.isWhitespace(delimiter)) {
                return null;
            }
        }
        return new VersionParts(Integer.parseInt(value.substring(0, majorEnd)), Integer.parseInt(value.substring(minorStart, minorEnd)), release);
    }

    private static int digitEnd(String value, int start) {
        int offset = start;
        while (offset < value.length() && Character.isDigit(value.charAt(offset))) {
            offset++;
        }
        return offset;
    }

    private static boolean allDigits(String value, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private record VersionParts(int major, int minor, int release) {
    }

    private final int value;

    MySqlVersion(int major, int minor){
        this.value = major * 100 + minor;
    }

    public int exactVersion() {
        return this.value * 100;
    }

    public String versionString() {
        return (this.value / 100) + "." + (this.value % 100);
    }

    public static boolean ge(MySqlVersion source, MySqlVersion target) {
        return source.value >= target.value;
    }

    public static boolean le(MySqlVersion source, MySqlVersion target) {
        return source.value <= target.value;
    }

    public static boolean ge(MySqlVersion source, int target) {
        return source.value >= target;
    }

    public static boolean le(MySqlVersion source, int target) {
        return source.value <= target;
    }

}
