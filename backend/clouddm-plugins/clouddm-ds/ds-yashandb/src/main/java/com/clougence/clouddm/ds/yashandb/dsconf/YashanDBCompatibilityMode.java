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
package com.clougence.clouddm.ds.yashandb.dsconf;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * YashanDB compatibility mode.
 * <p>
 * The compatibility mode of YashanDB is fixed at <b>installation time</b> (Oracle compatibility mode is the
 * default; MySQL compatibility mode is installed with {@code --mode mysql}), and the two modes behave
 * completely differently: default port, connection driver and JDBC metadata semantics all differ. Therefore
 * each variant binds its own {@code DsFactory} and its own driver family, rather than sharing one factory and
 * passing the expected mode through driver properties as GoldenDB does.
 * </p>
 * <p>
 * Measured on 23.4.1.109: in MySQL compatibility mode system variables such as {@code COMPAT_VECTOR} do
 * <b>not</b> exist ({@code SHOW VARIABLES LIKE '%compat%'} returns nothing and
 * {@code SELECT @@compat_vector} raises {@code YAS-04607 unknown system variable}), so the mode cannot be
 * inferred from a session variable the way GoldenDB does. Connection verification therefore relies on the
 * engine name in {@code information_schema.ENGINES} instead.
 * </p>
 *
 * @author open-cdm
 */
@Getter
public enum YashanDBCompatibilityMode {

    ORACLE(DataSourceType.YashanDBOracle, "oracle", "1688"),
    MYSQL(DataSourceType.YashanDBMySQL, "mysql", "1690");

    private final DataSourceType dataSourceType;
    /** Server-side mode identifier of this mode, used for hints when connection verification fails. */
    private final String         serverMode;
    /** Default port on which this mode serves connections. */
    private final String         defaultPort;

    YashanDBCompatibilityMode(DataSourceType dataSourceType, String serverMode, String defaultPort){
        this.dataSourceType = dataSourceType;
        this.serverMode = serverMode;
        this.defaultPort = defaultPort;
    }

    public static YashanDBCompatibilityMode fromDataSourceType(DataSourceType dataSourceType) {
        for (YashanDBCompatibilityMode mode : values()) {
            if (mode.dataSourceType == dataSourceType) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported YashanDB data source type: " + dataSourceType);
    }

    public static YashanDBCompatibilityMode fromServerMode(String serverMode) {
        String value = StringUtils.trimToEmpty(serverMode);
        for (YashanDBCompatibilityMode mode : values()) {
            if (mode.serverMode.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported YashanDB compatibility mode: " + serverMode);
    }
}
