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
package com.clougence.clouddm.ds.kingbasees.execute.sqlserver;

import com.clougence.adapter.sqlserver.SqlServerMainVersion;
import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

public enum KingbaseESSQLServerMainVersion implements MainVersion {

    KingbaseES_V009R001C010("KingbaseES V009R001C010", SqlServerMainVersion.SqlServer_2022),;

    private final String               productVersion;
    private final SqlServerMainVersion compatibleVersion;

    KingbaseESSQLServerMainVersion(String productVersion, SqlServerMainVersion compatibleVersion){
        this.productVersion = productVersion;
        this.compatibleVersion = compatibleVersion;
    }

    @Override
    public DsType getDsType() { return compatibleVersion.getDsType(); }

    @Override
    public String getMainVersion() { return compatibleVersion.getMainVersion(); }

    public static KingbaseESSQLServerMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }
        for (KingbaseESSQLServerMainVersion mainVersion : values()) {
            if (StringUtils.containsIgnoreCase(version, mainVersion.productVersion)) {
                return mainVersion;
            }
        }
        throw new UnsupportedOperationException("Unsupported KingbaseES SQLServer version " + version);
    }
}
