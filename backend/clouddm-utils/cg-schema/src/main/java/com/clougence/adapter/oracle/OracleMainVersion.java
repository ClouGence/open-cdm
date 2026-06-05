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
package com.clougence.adapter.oracle;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum OracleMainVersion implements MainVersion {

    Oracle_19("19."),
    Oracle_18("18."),
    Oracle_12("12."),
    Oracle_11("11."),
    Oracle_10("10.");

    private final MainVersion mainVersion;

    OracleMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.Oracle, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static OracleMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, Oracle_19)) {
            return Oracle_19;
        } else if (checkInVersion(version, Oracle_18)) {
            return Oracle_18;
        } else if (checkInVersion(version, Oracle_12)) {
            return Oracle_12;
        } else if (checkInVersion(version, Oracle_11)) {
            return Oracle_11;
        } else if (checkInVersion(version, Oracle_10)) {
            return Oracle_10;
        } else {
            throw new UnsupportedOperationException("Unsupported oracle version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.startsWithIgnoreCase(version, referenceVersion);
    }
}
