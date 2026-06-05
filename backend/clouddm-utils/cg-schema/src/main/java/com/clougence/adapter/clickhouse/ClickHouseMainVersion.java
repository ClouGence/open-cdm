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
package com.clougence.adapter.clickhouse;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2020/4/18 11:42
 */
public enum ClickHouseMainVersion implements MainVersion {

    CK_20_1("20.1"),
    CK_20_2("20.2"),
    CK_20_3("20.3"),
    CK_20_4("20.4"),
    CK_20_5("20.5"),
    CK_20_6("20.6"),
    CK_20_7("20.7"),
    CK_20_8("20.8"),
    CK_20_9("20.9"),
    CK_20_10("20.10"),
    CK_20_11("20.11"),
    CK_20_12("20.12"),
    CK_20_13("20.13"),
    CK_21_1("21.1"),
    CK_21_2("21.2"),
    CK_21_3("21.3"),
    CK_21_4("21.4"),
    CK_21_5("21.5"),
    CK_21_6("21.6"),
    CK_21_7("21.7"),
    CK_21_8("21.8"),
    CK_21_9("21.9"),
    CK_21_10("21.10"),
    CK_21_11("21.11"),
    CK_21_12("21.12"),
    CK_21_13("21.13"),
    CK_22_1("22.1"),
    CK_22_2("22.2"),
    CK_22_3("22.3"),
    CK_22_4("22.4"),
    CK_22_5("22.5"),
    CK_22_6("22.6"),
    CK_22_7("22.7"),
    CK_22_8("22.8"),
    CK_22_9("22.9"),
    CK_22_10("22.10"),
    CK_22_11("22.11"),
    CK_22_12("22.12"),
    CK_23_1("23.1."),
    CK_23_2("23.2"),
    CK_23_3("23.3"),
    CK_23_4("23.4"),
    CK_23_5("23.5"),
    CK_23_6("23.6"),
    CK_23_7("23.7"),
    CK_23_8("23.8"),
    CK_23_9("23.9"),
    CK_23_10("23.10"),
    CK_23_11("23.11"),
    CK_23_12("23.12"),
    CK_24_1("24.1."),
    CK_24_2("24.2."),
    CK_24_3("24.3."),
    CK_24_4("24.4."),
    CK_24_5("24.5."),
    CK_24_6("24.6."),
    CK_24_7("24.7."),
    CK_24_8("24.8."),
    CK_24_9("24.9."),
    CK_24_10("24.10."),
    CK_24_11("24.11."),
    CK_24_12("24.12."),
    CK_25_1("25.1."),
    CK_25_2("25.2."),
    CK_25_3("25.3."),
    CK_25_4("25.4."),
    CK_25_5("25.5."),
    CK_25_6("25.6."),
    CK_25_7("25.7."),
    CK_25_8("25.8."),
    CK_25_9("25.9."),
    CK_25_10("25.10."),
    CK_25_11("25.11."),
    CK_25_12("25.12."),
    CK_26_1("26.1."),
    CK_26_2("26.2."),
    CK_26_3("26.3."),
    CK_26_4("26.4."),
    CK_26_5("26.5."),
    CK_26_6("26.6."),
    CK_26_7("26.7."),
    CK_26_8("26.8."),
    CK_26_9("26.9."),
    CK_26_10("26.10."),
    CK_26_11("26.11."),
    CK_26_12("26.12."),;

    private final MainVersion mainVersion;

    ClickHouseMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.ClickHouse, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static ClickHouseMainVersion parserVersion(String version) {
        for (ClickHouseMainVersion mainVersion : ClickHouseMainVersion.values()) {
            if (mainVersion.name().equals(version)) {
                return mainVersion;
            }
        }

        if (checkInVersion(version, ClickHouseMainVersion.CK_26_12)) {
            return ClickHouseMainVersion.CK_26_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_11)) {
            return ClickHouseMainVersion.CK_26_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_10)) {
            return ClickHouseMainVersion.CK_26_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_9)) {
            return ClickHouseMainVersion.CK_26_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_8)) {
            return ClickHouseMainVersion.CK_26_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_7)) {
            return ClickHouseMainVersion.CK_26_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_6)) {
            return ClickHouseMainVersion.CK_26_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_5)) {
            return ClickHouseMainVersion.CK_26_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_4)) {
            return ClickHouseMainVersion.CK_26_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_3)) {
            return ClickHouseMainVersion.CK_26_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_2)) {
            return ClickHouseMainVersion.CK_26_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_26_1)) {
            return ClickHouseMainVersion.CK_26_1;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_12)) {
            return ClickHouseMainVersion.CK_25_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_11)) {
            return ClickHouseMainVersion.CK_25_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_10)) {
            return ClickHouseMainVersion.CK_25_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_9)) {
            return ClickHouseMainVersion.CK_25_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_8)) {
            return ClickHouseMainVersion.CK_25_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_7)) {
            return ClickHouseMainVersion.CK_25_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_6)) {
            return ClickHouseMainVersion.CK_25_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_5)) {
            return ClickHouseMainVersion.CK_25_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_4)) {
            return ClickHouseMainVersion.CK_25_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_3)) {
            return ClickHouseMainVersion.CK_25_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_2)) {
            return ClickHouseMainVersion.CK_25_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_25_1)) {
            return ClickHouseMainVersion.CK_25_1;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_12)) {
            return ClickHouseMainVersion.CK_24_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_11)) {
            return ClickHouseMainVersion.CK_24_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_10)) {
            return ClickHouseMainVersion.CK_24_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_9)) {
            return ClickHouseMainVersion.CK_24_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_8)) {
            return ClickHouseMainVersion.CK_24_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_7)) {
            return ClickHouseMainVersion.CK_24_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_6)) {
            return ClickHouseMainVersion.CK_24_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_5)) {
            return ClickHouseMainVersion.CK_24_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_4)) {
            return ClickHouseMainVersion.CK_24_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_3)) {
            return ClickHouseMainVersion.CK_24_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_2)) {
            return ClickHouseMainVersion.CK_24_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_24_1)) {
            return ClickHouseMainVersion.CK_24_1;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_12)) {
            return ClickHouseMainVersion.CK_23_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_11)) {
            return ClickHouseMainVersion.CK_23_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_10)) {
            return ClickHouseMainVersion.CK_23_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_9)) {
            return ClickHouseMainVersion.CK_23_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_8)) {
            return ClickHouseMainVersion.CK_23_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_7)) {
            return ClickHouseMainVersion.CK_23_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_6)) {
            return ClickHouseMainVersion.CK_23_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_5)) {
            return ClickHouseMainVersion.CK_23_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_4)) {
            return ClickHouseMainVersion.CK_23_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_3)) {
            return ClickHouseMainVersion.CK_23_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_2)) {
            return ClickHouseMainVersion.CK_23_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_23_1)) {
            return ClickHouseMainVersion.CK_23_1;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_12)) {
            return ClickHouseMainVersion.CK_22_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_11)) {
            return ClickHouseMainVersion.CK_22_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_10)) {
            return ClickHouseMainVersion.CK_22_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_9)) {
            return ClickHouseMainVersion.CK_22_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_8)) {
            return ClickHouseMainVersion.CK_22_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_7)) {
            return ClickHouseMainVersion.CK_22_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_6)) {
            return ClickHouseMainVersion.CK_22_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_5)) {
            return ClickHouseMainVersion.CK_22_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_4)) {
            return ClickHouseMainVersion.CK_22_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_3)) {
            return ClickHouseMainVersion.CK_22_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_2)) {
            return ClickHouseMainVersion.CK_22_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_22_1)) {
            return ClickHouseMainVersion.CK_22_1;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_13)) {
            return ClickHouseMainVersion.CK_21_13;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_12)) {
            return ClickHouseMainVersion.CK_21_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_11)) {
            return ClickHouseMainVersion.CK_21_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_10)) {
            return ClickHouseMainVersion.CK_21_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_9)) {
            return ClickHouseMainVersion.CK_21_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_8)) {
            return ClickHouseMainVersion.CK_21_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_7)) {
            return ClickHouseMainVersion.CK_21_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_6)) {
            return ClickHouseMainVersion.CK_21_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_5)) {
            return ClickHouseMainVersion.CK_21_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_4)) {
            return ClickHouseMainVersion.CK_21_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_3)) {
            return ClickHouseMainVersion.CK_21_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_2)) {
            return ClickHouseMainVersion.CK_21_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_21_1)) {
            return ClickHouseMainVersion.CK_21_1;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_13)) {
            return ClickHouseMainVersion.CK_20_13;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_12)) {
            return ClickHouseMainVersion.CK_20_12;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_11)) {
            return ClickHouseMainVersion.CK_20_11;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_10)) {
            return ClickHouseMainVersion.CK_20_10;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_9)) {
            return ClickHouseMainVersion.CK_20_9;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_8)) {
            return ClickHouseMainVersion.CK_20_8;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_7)) {
            return ClickHouseMainVersion.CK_20_7;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_6)) {
            return ClickHouseMainVersion.CK_20_6;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_5)) {
            return ClickHouseMainVersion.CK_20_5;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_4)) {
            return ClickHouseMainVersion.CK_20_4;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_3)) {
            return ClickHouseMainVersion.CK_20_3;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_2)) {
            return ClickHouseMainVersion.CK_20_2;
        } else if (checkInVersion(version, ClickHouseMainVersion.CK_20_1)) {
            return ClickHouseMainVersion.CK_20_1;
        } else {
            throw new IllegalArgumentException("unsupported ClickHouse version:" + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
