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
