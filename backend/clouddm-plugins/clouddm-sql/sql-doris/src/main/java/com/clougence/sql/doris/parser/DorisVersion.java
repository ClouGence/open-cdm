/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.doris.parser;

/** Doris major grammar families selected by {@code SqlParserParameters}. */
public enum DorisVersion {
    DORIS_1(1),
    DORIS_2(2),
    DORIS_3(3),
    DORIS_4(4);

    public static final DorisVersion LATEST = values()[values().length - 1];

    private final int major;

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

    public static int parseExactVersion(String version) {
        String value = version == null ? "" : version;
        for (int start = 0; start < value.length(); start++) {
            if (!Character.isDigit(value.charAt(start))) {
                continue;
            }
            int majorEnd = digitEnd(value, start);
            if (majorEnd >= value.length() || value.charAt(majorEnd) != '.') {
                start = majorEnd;
                continue;
            }
            int minorStart = majorEnd + 1;
            int minorEnd = digitEnd(value, minorStart);
            if (minorEnd == minorStart || minorEnd >= value.length() || value.charAt(minorEnd) != '.') {
                start = minorEnd;
                continue;
            }
            int releaseStart = minorEnd + 1;
            int releaseEnd = digitEnd(value, releaseStart);
            if (releaseEnd == releaseStart) {
                start = releaseEnd;
                continue;
            }
            int major = Integer.parseInt(value.substring(start, majorEnd));
            int minor = Integer.parseInt(value.substring(minorStart, minorEnd));
            int release = Integer.parseInt(value.substring(releaseStart, releaseEnd));
            if (major > 99 || minor > 99 || release > 99) {
                break;
            }
            return major * 10000 + minor * 100 + release;
        }
        throw new IllegalArgumentException("Invalid Doris version: " + version);
    }

    public static int parseExactVersionCode(String exactVersion) {
        String value = exactVersion == null ? "" : exactVersion.trim();
        if (value.length() < 5 || value.length() > 6) {
            throw new IllegalArgumentException("Invalid Doris exact version: " + exactVersion);
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                throw new IllegalArgumentException("Invalid Doris exact version: " + exactVersion);
            }
        }
        return Integer.parseInt(value);
    }

    private static int digitEnd(String value, int start) {
        int offset = start;
        while (offset < value.length() && Character.isDigit(value.charAt(offset))) {
            offset++;
        }
        return offset;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }
}
