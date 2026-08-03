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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TiDBVersion {
    TIDB_5(5, "v5.4.3"),
    TIDB_6(6, "v6.5.12"),
    TIDB_7(7, "v7.5.7"),
    TIDB_8(8, "v8.5.7"),
    TIDB_9(9, "v9.0.0-beta.2.pre");

    public static final TiDBVersion LATEST              = TIDB_9;

    private static final Pattern    TIDB_SERVER_VERSION = Pattern.compile("(?i)\\bTiDB\\b(?:[-_\\s]*(?:SERVER|VERSION))?[-_\\s:]*V?(\\d+)");
    private static final Pattern    DIRECT_VERSION      = Pattern.compile("(?i)^\\s*V?(\\d+)");

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
        Matcher serverVersion = TIDB_SERVER_VERSION.matcher(version);
        if (serverVersion.find()) {
            return fromMajor(serverVersion.group(1));
        }
        Matcher directVersion = DIRECT_VERSION.matcher(version);
        return directVersion.find() ? fromMajor(directVersion.group(1)) : LATEST;
    }

    private static TiDBVersion fromMajor(String value) {
        int major = Integer.parseInt(value);
        for (TiDBVersion candidate : values()) {
            if (candidate.major == major) {
                return candidate;
            }
        }
        return LATEST;
    }
}
