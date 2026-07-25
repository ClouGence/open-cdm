package com.clougence.sql.mysql.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** MySQL grammar compatibility levels supported by the parser. */
public enum MySqlVersion {
    MYSQL_5_6(5, 6),
    MYSQL_5_7(5, 7),
    MYSQL_8_0(8, 0),
    MYSQL_8_4(8, 4),
    MYSQL_9_7(9, 7);

    public static final MySqlVersion LATEST          = values()[values().length - 1];
    private static final Pattern     VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:[-+_ ].*)?$");

    /**
     * Parse a version string (e.g. "5.7", "8.0", "8.4", "9.7") to a {@link MySqlVersion}.
     * Returns {@link #LATEST} if the string is null, blank, or does not match any known version.
     */
    public static MySqlVersion parse(String version) {
        if (version == null || version.isBlank()) {
            return LATEST;
        }
        Matcher matcher = VERSION_PATTERN.matcher(version.trim());
        if (!matcher.matches()) {
            return LATEST;
        }
        int parsedMajor = Integer.parseInt(matcher.group(1));
        int parsedMinor = Integer.parseInt(matcher.group(2));
        for (MySqlVersion v : values()) {
            int major = v.value / 100;
            int minor = v.value % 100;
            if (parsedMajor == major && parsedMinor == minor) {
                return v;
            }
        }
        return LATEST;
    }

    public static int parseExactVersion(String version) {
        if (version == null || version.isBlank()) {
            return LATEST.exactVersion();
        }
        Matcher matcher = VERSION_PATTERN.matcher(version.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid MySQL version: " + version);
        }
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int release = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
        if (major > 99 || minor > 99 || release > 99) {
            throw new IllegalArgumentException("Invalid MySQL version: " + version);
        }
        return major * 10000 + minor * 100 + release;
    }

    public static int parseExactVersionCode(String exactVersion) {
        String value = exactVersion == null ? "" : exactVersion.trim();
        if (!value.matches("\\d{5,6}")) {
            throw new IllegalArgumentException("Invalid MySQL exact version: " + exactVersion);
        }
        return Integer.parseInt(value);
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

    public boolean atLeast(MySqlVersion minimum) {
        return this.value >= minimum.value;
    }

    public boolean atMost(MySqlVersion maximum) {
        return this.value <= maximum.value;
    }

    public boolean between(MySqlVersion minimum, MySqlVersion maximum) {
        return atLeast(minimum) && atMost(maximum);
    }

    public boolean atLeast(int major, int minor) {
        return this.value >= major * 100 + minor;
    }

    public boolean atMost(int major, int minor) {
        return this.value <= major * 100 + minor;
    }

    public boolean between(int minimumMajor, int minimumMinor, int maximumMajor, int maximumMinor) {
        return atLeast(minimumMajor, minimumMinor) && atMost(maximumMajor, maximumMinor);
    }

}
