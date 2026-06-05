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
package com.clougence.clouddm.base.metadata.rdp.enumeration;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

/**
 * @author bucketli 2023/10/25 11:29:42
 */
public enum ConnectType {

    DEFAULT(null, "CONNECTION_TYPE_DEFAULT"),

    ORACLE_SID(DataSourceType.Oracle, "CONNECTION_TYPE_ORACLE_SID"),
    ORACLE_SERVICE(DataSourceType.Oracle, "CONNECTION_TYPE_ORACLE_SERVICE"),
    ORACLE_TNS(DataSourceType.Oracle, "CONNECTION_TYPE_ORACLE_TNS"),
    ORACLE_PDB(DataSourceType.Oracle, "CONNECTION_TYPE_ORACLE_PDB"),

    CLICKHOUSE_HTTP(DataSourceType.ClickHouse, "CONNECTION_TYPE_CLICKHOUSE_HTTP"),
    CLICKHOUSE_TCP(DataSourceType.ClickHouse, "CONNECTION_TYPE_CLICKHOUSE_TCP"),;

    private final DataSourceType dsType;

    private final String         i18nKey;

    ConnectType(DataSourceType dsType, String i18nKey){
        this.dsType = dsType;
        this.i18nKey = i18nKey;
    }

    public String getI18nKey() { return i18nKey; }

    public DataSourceType getDsType() { return dsType; }
}
