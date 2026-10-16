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
package com.clougence.clouddm.ds.kingbasees.dsconf;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public enum KingbaseESCompatibilityMode {

    POSTGRESQL(DataSourceType.KingbaseESPostgreSQL, "pg", false),
    MYSQL(DataSourceType.KingbaseESMySQL, "mysql", false),
    ORACLE(DataSourceType.KingbaseESOracle, "oracle", true),
    SQLSERVER(DataSourceType.KingbaseESSQLServer, "sqlserver", false);

    public static final String   EXPECTED_MODE_PROPERTY = "kingbasees.compatibilityMode";

    private final DataSourceType dataSourceType;
    private final String         serverMode;
    private final boolean        emptyStringIsNull;

    KingbaseESCompatibilityMode(DataSourceType dataSourceType, String serverMode, boolean emptyStringIsNull){
        this.dataSourceType = dataSourceType;
        this.serverMode = serverMode;
        this.emptyStringIsNull = emptyStringIsNull;
    }

    public static KingbaseESCompatibilityMode fromDataSourceType(DataSourceType dataSourceType) {
        for (KingbaseESCompatibilityMode mode : values()) {
            if (mode.dataSourceType == dataSourceType) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported KingbaseES data source type: " + dataSourceType);
    }

    public static KingbaseESCompatibilityMode fromServerMode(String serverMode) {
        String value = StringUtils.trimToEmpty(serverMode);
        for (KingbaseESCompatibilityMode mode : values()) {
            if (mode.serverMode.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported KingbaseES compatibility mode: " + serverMode);
    }
}
