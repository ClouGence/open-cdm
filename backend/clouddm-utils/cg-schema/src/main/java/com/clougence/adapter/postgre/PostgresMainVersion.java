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
package com.clougence.adapter.postgre;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum PostgresMainVersion implements MainVersion {

    GaussDBOpenGauss_5_0_6("5.0.6"),
    GaussDBOpenGauss_5_0_5("5.0.5"),
    GaussDBOpenGauss_5_0_4("5.0.4"),
    GaussDBOpenGauss_5_0_3("5.0.3"),
    GaussDBOpenGauss_5_0_2("5.0.2"),
    GaussDBOpenGauss_5_0_1("5.0.1"),
    GaussDBOpenGauss_5_0_0("5.0.0"),
    PostgreSQL_8_4("8.4"),
    PostgreSQL_9_0("9.0"),
    PostgreSQL_9_1("9.1"),
    PostgreSQL_9_2("9.2"),
    PostgreSQL_9_3("9.3"),
    PostgreSQL_9_4("9.4"),
    PostgreSQL_9_5("9.5"),
    PostgreSQL_9_6("9.6"),
    PostgreSQL_10("10"),
    PostgreSQL_11("11"),
    PostgreSQL_12("12"),
    PostgreSQL_13("13"),
    PostgreSQL_14("14"),
    PostgreSQL_15("15"),
    PostgreSQL_16("16"),
    PostgreSQL_17("17"),
    PostgreSQL_18("18");

    private final MainVersion mainVersion;

    PostgresMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.PostgreSQL, mainVersion);
    }

    public static PostgresMainVersion parserVersion(String fullVersion) {
        if (isGaussDB(fullVersion)) {
            if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_0)) {
                return GaussDBOpenGauss_5_0_0;
            } else if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_1)) {
                return GaussDBOpenGauss_5_0_1;
            } else if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_2)) {
                return GaussDBOpenGauss_5_0_2;
            } else if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_3)) {
                return GaussDBOpenGauss_5_0_3;
            } else if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_4)) {
                return GaussDBOpenGauss_5_0_4;
            } else if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_5)) {
                return GaussDBOpenGauss_5_0_5;
            } else if (checkInVersion(fullVersion, GaussDBOpenGauss_5_0_6)) {
                return GaussDBOpenGauss_5_0_6;
            } else {
                throw new UnsupportedOperationException("Unsupported GaussDBOpenGauss version " + fullVersion);
            }
        } else {
            String version = fixedVersion(fullVersion);
            if (checkInVersion(version, PostgreSQL_8_4)) {
                return PostgreSQL_8_4;
            } else if (checkInVersion(version, PostgreSQL_9_0)) {
                return PostgreSQL_9_0;
            } else if (checkInVersion(version, PostgreSQL_9_1)) {
                return PostgreSQL_9_1;
            } else if (checkInVersion(version, PostgreSQL_9_2)) {
                return PostgreSQL_9_2;
            } else if (checkInVersion(version, PostgreSQL_9_3)) {
                return PostgreSQL_9_3;
            } else if (checkInVersion(version, PostgreSQL_9_4)) {
                return PostgreSQL_9_4;
            } else if (checkInVersion(version, PostgreSQL_9_5)) {
                return PostgreSQL_9_5;
            } else if (checkInVersion(version, PostgreSQL_9_6)) {
                return PostgreSQL_9_6;
            } else if (checkInVersion(version, PostgreSQL_10)) {
                return PostgreSQL_10;
            } else if (checkInVersion(version, PostgreSQL_11)) {
                return PostgreSQL_11;
            } else if (checkInVersion(version, PostgreSQL_12)) {
                return PostgreSQL_12;
            } else if (checkInVersion(version, PostgreSQL_13)) {
                return PostgreSQL_13;
            } else if (checkInVersion(version, PostgreSQL_14)) {
                return PostgreSQL_14;
            } else if (checkInVersion(version, PostgreSQL_15)) {
                return PostgreSQL_15;
            } else if (checkInVersion(version, PostgreSQL_16)) {
                return PostgreSQL_16;
            } else if (checkInVersion(version, PostgreSQL_17)) {
                return PostgreSQL_17;
            } else if (checkInVersion(version, PostgreSQL_18)) {
                return PostgreSQL_18;
            } else {
                throw new UnsupportedOperationException("Unsupported postgres version " + version);
            }
        }

    }

    private static boolean isGaussDB(String fullVersion) {
        return fullVersion.toLowerCase().contains("opengauss");
    }

    private static String fixedVersion(String fullVersion) {
        if (StringUtils.isBlank(fullVersion)) {
            return null;
        }
        //PostgreSQL 14.2 (Debian 14.2-1.pgdg110+1) on x86_64-pc-linux-gnu, compiled by gcc (Debian 10.2.1-6) 10.2.1 20210110, 64-bit
        if (fullVersion.startsWith("PostgreSQL ")) {
            fullVersion = fullVersion.substring("PostgreSQL ".length());
        }

        //13.0 (Debian 14.2-1.pgdg110+1) on x86_64-pc-linux-gnu
        String[] vers = fullVersion.trim().split(" ");
        if (vers.length > 0) {
            return vers[0];
        } else {
            return fullVersion;
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.containsIgnoreCase(version, referenceVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

}
