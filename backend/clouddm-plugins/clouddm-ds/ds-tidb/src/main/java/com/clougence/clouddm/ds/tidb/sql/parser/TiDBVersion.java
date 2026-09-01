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
package com.clougence.clouddm.ds.tidb.sql.parser;

public enum TiDBVersion {
    TIDB_5(5, "v5.4.3"),
    TIDB_6(6, "v6.5.12"),
    TIDB_7(7, "v7.5.7"),
    TIDB_8(8, "v8.5.7"),
    TIDB_9(9, "v9.0.0-beta.2.pre");

    public static final TiDBVersion LATEST              = TIDB_9;

    private final int               major;
    private final String            sourceRef;

    TiDBVersion(int major, String sourceRef){
        this.major = major;
        this.sourceRef = sourceRef;
    }

    public int major() {
        return major;
    }

    public String sourceRef() {
        return sourceRef;
    }

    public boolean atLeast(TiDBVersion minimum) {
        return this.major >= minimum.major;
    }

    public static TiDBVersion parse(String version) {
        if (version == null || version.isBlank()) {
            return LATEST;
        }
        int tidb = findWord(version, "TiDB");
        if (tidb >= 0) {
            int major = majorAfterTiDB(version, tidb + "TiDB".length());
            if (major >= 0) {
                return fromMajor(major);
            }
        }
        int start = skipWhitespace(version, 0);
        if (start < version.length() && (version.charAt(start) == 'v' || version.charAt(start) == 'V')) {
            start++;
        }
        int major = readMajor(version, start);
        return major >= 0 ? fromMajor(major) : LATEST;
    }

    private static TiDBVersion fromMajor(int major) {
        for (TiDBVersion candidate : values()) {
            if (candidate.major == major) {
                return candidate;
            }
        }
        return LATEST;
    }

    private static int majorAfterTiDB(String value, int start) {
        int index = skipSeparators(value, start, false);
        if (startsWithWord(value, index, "SERVER")) {
            index = skipSeparators(value, index + "SERVER".length(), true);
        } else if (startsWithWord(value, index, "VERSION")) {
            index = skipSeparators(value, index + "VERSION".length(), true);
        } else {
            index = skipSeparators(value, index, true);
        }
        if (index < value.length() && (value.charAt(index) == 'v' || value.charAt(index) == 'V')) {
            index++;
        }
        return readMajor(value, index);
    }

    private static int findWord(String value, String word) {
        for (int index = 0; index + word.length() <= value.length(); index++) {
            if (value.regionMatches(true, index, word, 0, word.length())
                && (index == 0 || !isWordPart(value.charAt(index - 1)))
                && (index + word.length() == value.length() || !isWordPart(value.charAt(index + word.length())))) {
                return index;
            }
        }
        return -1;
    }

    private static boolean startsWithWord(String value, int start, String word) {
        int end = start + word.length();
        return start >= 0 && end <= value.length() && value.regionMatches(true, start, word, 0, word.length())
               && (start == 0 || !isWordPart(value.charAt(start - 1))) && (end == value.length() || !isWordPart(value.charAt(end)));
    }

    private static int skipWhitespace(String value, int start) {
        int index = start;
        while (index < value.length() && Character.isWhitespace(value.charAt(index))) {
            index++;
        }
        return index;
    }

    private static int skipSeparators(String value, int start, boolean includeColon) {
        int index = start;
        while (index < value.length()) {
            char current = value.charAt(index);
            if (Character.isWhitespace(current) || current == '-' || current == '_' || includeColon && current == ':') {
                index++;
            } else {
                break;
            }
        }
        return index;
    }

    private static int readMajor(String value, int start) {
        if (start >= value.length() || !Character.isDigit(value.charAt(start))) {
            return -1;
        }
        int result = 0;
        for (int index = start; index < value.length() && Character.isDigit(value.charAt(index)); index++) {
            result = result * 10 + value.charAt(index) - '0';
        }
        return result;
    }

    private static boolean isWordPart(char value) {
        return Character.isLetterOrDigit(value) || value == '_';
    }
}
