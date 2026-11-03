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

/** TiDB grammar compatibility levels supported by the parser. */
public enum TiDbVersion {
    TIDB_3(3),
    TIDB_4(4),
    TIDB_5(5),
    TIDB_6(6),
    TIDB_7(7),
    TIDB_8(8);

    public static final TiDbVersion LATEST = values()[values().length - 1];

    private final int               major;

    TiDbVersion(int major){
        this.major = major;
    }

    public static TiDbVersion parse(String version) {
        String value = version == null ? "" : version;
        int marker = value.indexOf("Release Version:");
        int offset = marker < 0 ? 0 : marker + "Release Version:".length();
        int tidbMarker = value.toLowerCase().indexOf("tidb-v");
        if (tidbMarker >= 0) {
            offset = tidbMarker + "tidb-v".length();
        }
        int major = firstNumber(value, offset);
        for (TiDbVersion candidate : values()) {
            if (candidate.major == major) {
                return candidate;
            }
        }
        return LATEST;
    }

    public String versionString() {
        return Integer.toString(this.major);
    }

    public static boolean ge(TiDbVersion source, TiDbVersion target) {
        return source.major >= target.major;
    }

    private static int firstNumber(String value, int offset) {
        while (offset < value.length() && !Character.isDigit(value.charAt(offset))) {
            offset++;
        }
        int end = offset;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (offset == end) {
            return -1;
        }
        try {
            return Integer.parseInt(value.substring(offset, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
