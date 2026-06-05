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
package com.clougence.adapter.ob.obformysql;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author wanshao create time is 2022/1/25
 **/
public enum ObForMySQLMainVersion implements MainVersion {

    OceanBase_3_0("3.0"),
    OceanBase_3_1("3.1"),
    OceanBase_3_2("3.2"),
    OceanBase_4_0("4.0"),
    OceanBase_4_1("4.1"),
    OceanBase_4_2("4.2"),
    OceanBase_4_3("4.3"),
    OceanBase_4_4("4.4"),
    OceanBase_5_0("5.0"),
    OceanBase_5_1("5.1"),
    OceanBase_5_2("5.2"),;

    private final MainVersion mainVersion;

    ObForMySQLMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.OceanBase, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    @Override
    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static ObForMySQLMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, OceanBase_3_0)) {
            return OceanBase_3_0;
        } else if (checkInVersion(version, OceanBase_3_1)) {
            return OceanBase_3_1;
        } else if (checkInVersion(version, OceanBase_3_2)) {
            return OceanBase_3_2;
        } else if (checkInVersion(version, OceanBase_4_0)) {
            return OceanBase_4_0;
        } else if (checkInVersion(version, OceanBase_4_1)) {
            return OceanBase_4_1;
        } else if (checkInVersion(version, OceanBase_4_2)) {
            return OceanBase_4_2;
        } else if (checkInVersion(version, OceanBase_4_3)) {
            return OceanBase_4_3;
        } else if (checkInVersion(version, OceanBase_4_4)) {
            return OceanBase_4_4;
        } else if (checkInVersion(version, OceanBase_5_0)) {
            return OceanBase_5_0;
        } else if (checkInVersion(version, OceanBase_5_1)) {
            return OceanBase_5_1;
        } else if (checkInVersion(version, OceanBase_5_2)) {
            return OceanBase_5_2;
        } else {
            throw new UnsupportedOperationException("Unsupported OceanBase version " + version);
        }
    }

    protected static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion().toLowerCase();
        version = version.toLowerCase();

        if (version.contains("v")) {
            version = version.substring(version.indexOf("v") + 1);
            return version.startsWith(referenceVersion);
        } else {
            return version.startsWith(referenceVersion);
        }

    }
}
