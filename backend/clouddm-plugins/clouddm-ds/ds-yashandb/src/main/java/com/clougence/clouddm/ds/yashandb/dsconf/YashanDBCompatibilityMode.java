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
 * 崖山数据库（YashanDB）兼容模式。
 * <p>
 * 崖山的兼容模式在<b>安装期</b>确定（默认 Oracle 兼容模式，MySQL 兼容模式通过 {@code --mode mysql} 安装），
 * 且两种模式对外表现完全不同：默认端口、连接驱动、JDBC 元数据语义都不一样，因此按变体各绑定一个
 * {@code DsFactory} + 一个 driver family，而不是像 GoldenDB 那样两个变体共用一个工厂、靠驱动属性传期望模式。
 * </p>
 * <p>
 * 实测（23.4.1.109）MySQL 兼容模式下<b>不存在</b> {@code COMPAT_VECTOR} 之类的系统变量
 * （{@code SHOW VARIABLES LIKE '%compat%'} 为空、{@code SELECT @@compat_vector} 报
 * {@code YAS-04607 unknown system variable}），故不能像 GoldenDB 那样靠会话变量推断模式，
 * 连接校验改用 {@code information_schema.ENGINES} 中的引擎名。
 * </p>
 *
 * @author open-cdm
 */
@Getter
public enum YashanDBCompatibilityMode {

    ORACLE(DataSourceType.YashanDBOracle, "oracle", "1688"),
    MYSQL(DataSourceType.YashanDBMySQL, "mysql", "1690");

    private final DataSourceType dataSourceType;
    /** 该模式对应的服务端模式标识，用于连接校验失败时的提示。 */
    private final String         serverMode;
    /** 该模式对外提供服务的默认端口。 */
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
