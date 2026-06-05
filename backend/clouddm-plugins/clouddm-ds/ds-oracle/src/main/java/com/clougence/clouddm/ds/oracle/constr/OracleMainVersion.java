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
package com.clougence.clouddm.ds.oracle.constr;

import com.clougence.utils.Version;

/**
 * @author bucketli 2024/7/3 09:07:08
 */
public enum OracleMainVersion {

    // 21c
    Oracle_21c("21.0"),

    // 19c
    Oracle_19c("19.0"),
    Oracle_19c_R1("19.1"),
    Oracle_19c_R2("19.2"),
    Oracle_19c_R3("19.3"),
    Oracle_19c_R4("19.4"),
    Oracle_19c_R5("19.5"),
    Oracle_19c_R6("19.6"),
    Oracle_19c_R7("19.7"),
    Oracle_19c_R8("19.8"),
    Oracle_19c_R9("19.9"),
    Oracle_19c_R10("19.10"),
    Oracle_19c_R11("19.11"),
    Oracle_19c_R12("19.12"),
    Oracle_19c_R13("19.13"),

    // 18c
    Oracle_18c("18.0"),
    Oracle_18c_R1("18.1"),
    Oracle_18c_R2("18.2"),
    Oracle_18c_R3("18.3"),
    // 12c
    Oracle_12c("12.0"),
    Oracle_12c_R1("12.1"),
    Oracle_12c_R2("12.2"),
    // 11g
    Oracle_11c("11.0"),
    Oracle_11c_R1("11.1"),
    Oracle_11c_R2("11.2"),
    // 10g
    Oracle_10g("10.0"),
    Oracle_10g_R1("10.1"),
    Oracle_10g_R2("10.2");

    private final String mainVersion;

    OracleMainVersion(String mainVersion){
        this.mainVersion = mainVersion;
    }

    public String getVersionPrefix() { return mainVersion; }

    public static OracleMainVersion getMainVersion(String version) {
        for (OracleMainVersion mainVersion : OracleMainVersion.values()) {
            if (mainVersion.name().equals(version)) {
                return mainVersion;
            }
        }
        for (OracleMainVersion ver : OracleMainVersion.values()) {
            if (checkOracleInVersion(version, ver)) {
                return ver;
            }
        }
        throw new IllegalArgumentException("unsupported Oracle version:" + version);
    }

    private static boolean checkOracleInVersion(String version, OracleMainVersion mainVersion) {
        return version.equals(mainVersion.mainVersion) || version.startsWith(mainVersion.mainVersion);
    }

    public boolean isLe(OracleMainVersion targetVersion) {
        Version source = new Version(this.mainVersion);
        Version target = new Version(targetVersion.mainVersion);
        return source.compareTo(target) >= 0;
    }

    public boolean isGe(OracleMainVersion targetVersion) {
        Version source = new Version(this.mainVersion);
        Version target = new Version(targetVersion.mainVersion);
        return source.compareTo(target) <= 0;
    }
}
