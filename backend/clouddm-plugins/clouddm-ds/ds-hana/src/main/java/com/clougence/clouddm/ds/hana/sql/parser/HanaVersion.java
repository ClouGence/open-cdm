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
package com.clougence.clouddm.ds.hana.sql.parser;

/** SAP HANA grammar compatibility levels supported by the parser. */
public enum HanaVersion {
    HANA_1(1),
    HANA_2(2);

    public static final HanaVersion LATEST = values()[values().length - 1];

    private final int major;

    HanaVersion(int major){
        this.major = major;
    }

    public static HanaVersion parse(String version) {
        if (version == null || version.isBlank()) {
            return LATEST;
        }

        String value = version.trim();
        int end = 0;
        while (end < value.length() && Character.isDigit(value.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return LATEST;
        }
        if (end < value.length() && (value.charAt(end) != '.' || end + 1 >= value.length()
                || !Character.isDigit(value.charAt(end + 1)))) {
            return LATEST;
        }

        int major = Integer.parseInt(value.substring(0, end));
        for (HanaVersion hanaVersion : values()) {
            if (hanaVersion.major == major) {
                return hanaVersion;
            }
        }
        return LATEST;
    }

    public boolean atLeast(HanaVersion minimum) {
        return this.major >= minimum.major;
    }

    public boolean atMost(HanaVersion maximum) {
        return this.major <= maximum.major;
    }
}
