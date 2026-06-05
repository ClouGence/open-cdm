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
package com.clougence.adapter.mysql;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum MySQLMainVersion implements MainVersion {

    MySQL_5_1("5.1"),
    MySQL_5_5("5.5"),
    MySQL_5_6("5.6"),
    MySQL_5_7("5.7"),
    MySQL_8_0("8.0"),
    MySQL_8_1("8.1"),
    MySQL_8_2("8.2"),
    MySQL_8_3("8.3"),
    MySQL_8_4("8.4"),
    MariaDB_10_0("10.0"),
    MariaDB_10_1("10.1"),
    MariaDB_10_2("10.2"),
    MariaDB_10_3("10.3"),
    MariaDB_10_4("10.4"), // Temporarily classified as a type of MySQL
    MariaDB_10_5("10.5"),
    MariaDB_10_6("10.6"),
    MariaDB_10_7("10.7"),
    MariaDB_10_8("10.8"),
    MariaDB_10_9("10.9"),
    MariaDB_10_10("10.10"),
    MariaDB_10_11("10.11"),
    MariaDB_11_0("11.0"),
    MariaDB_11_1("11.1"),
    MariaDB_11_2("11.2"),
    MariaDB_11("11.");

    private final MainVersion mainVersion;

    MySQLMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.MySQL, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static MySQLMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, MySQL_5_1)) {
            return MySQL_5_1;
        } else if (checkInVersion(version, MySQL_5_5)) {
            return MySQL_5_5;
        } else if (checkInVersion(version, MySQL_5_6)) {
            return MySQL_5_6;
        } else if (checkInVersion(version, MySQL_5_7)) {
            return MySQL_5_7;
        } else if (checkInVersion(version, MySQL_8_0)) {
            return MySQL_8_0;
        } else if (checkInVersion(version, MySQL_8_1)) {
            return MySQL_8_1;
        } else if (checkInVersion(version, MySQL_8_2)) {
            return MySQL_8_2;
        } else if (checkInVersion(version, MySQL_8_3)) {
            return MySQL_8_3;
        } else if (checkInVersion(version, MySQL_8_4)) {
            return MySQL_8_4;
        } else if (checkInVersion(version, MariaDB_10_0)) {
            return MariaDB_10_0;
        } else if (checkInVersion(version, MariaDB_10_1)) {
            return MariaDB_10_1;
        } else if (checkInVersion(version, MariaDB_10_2)) {
            return MariaDB_10_2;
        } else if (checkInVersion(version, MariaDB_10_3)) {
            return MariaDB_10_3;
        } else if (checkInVersion(version, MariaDB_10_4)) {
            return MariaDB_10_4;
        } else if (checkInVersion(version, MariaDB_10_5)) {
            return MariaDB_10_5;
        } else if (checkInVersion(version, MariaDB_10_6)) {
            return MariaDB_10_6;
        } else if (checkInVersion(version, MariaDB_10_7)) {
            return MariaDB_10_7;
        } else if (checkInVersion(version, MariaDB_10_8)) {
            return MariaDB_10_8;
        } else if (checkInVersion(version, MariaDB_10_9)) {
            return MariaDB_10_9;
        } else if (checkInVersion(version, MariaDB_10_10)) {
            return MariaDB_10_10;
        } else if (checkInVersion(version, MariaDB_10_11)) {
            return MariaDB_10_11;
        } else if (checkInVersion(version, MariaDB_11_0)) {
            return MariaDB_11_0;
        } else if (checkInVersion(version, MariaDB_11_1)) {
            return MariaDB_11_1;
        } else if (checkInVersion(version, MariaDB_11_2)) {
            return MariaDB_11_2;
        } else if (checkInVersion(version, MariaDB_11)) {
            return MariaDB_11;
        } else {
            throw new UnsupportedOperationException("Unsupported mysql version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
