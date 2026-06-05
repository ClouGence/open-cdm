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
package com.clougence.clouddm.base.metadata.ds;

/**
 * @author bucketli 2020/11/5 20:50
 */
public enum ConfigI18nKey {
    CONFIG_DESCRIPTION_EMPTY,
    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.DataSourceConfig
    CONFIG_DS_INSTANCE_ID_DESCRIPTION,
    CONFIG_DS_TYPE_DESCRIPTION,
    CONFIG_RDB_VERSION_DESCRIPTION,
    CONFIG_DS_SECURITY_TYPE_DESCRIPTION,
    CONFIG_DS_SO_TIMEOUT_MS_DESCRIPTION,
    CONFIG_DS_MAX_IDLE_TIME_SEC_DESCRIPTION,
    CONFIG_DS_ONLINE_MAX_CONNECTIONS_DESCRIPTION,
    CONFIG_DS_ONLINE_MAX_CONCURRENT_DESCRIPTION,
    CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION,
    CONFIG_DS_EXPORT_MAX_CONCURRENT_DESCRIPTION,
    CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION,
    CONFIG_DS_READONLY_DESCRIPTION,
    CONFIG_DS_DEPLOY_ALIYUN_INSTANCE_ID_DESCRIPTION,
    CONFIG_RDB_CONFIG_VERSION_DESCRIPTION,
    CONFIG_RDB_STORE_PASSWORD_DESCRIPTION,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.RdbConfig
    CONFIG_RDB_USERNAME_DESCRIPTION,
    CONFIG_RDB_PASSWORD_DESCRIPTION,
    CONFIG_RDB_CONN_HOST_DESCRIPTION,
    CONFIG_RDB_CONN_TIMEOUT_MS_DESCRIPTION,
    CONFIG_RDB_DEFAULT_DB_DESCRIPTION,
    CONFIG_RDB_DEFAULT_SCHEMA_DESCRIPTION,
    CONFIG_RDB_ISOLATION_DESCRIPTION,
    CONFIG_RDB_TRANSACTION_DESCRIPTION,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.mysql.MySqlConfig
    CONFIG_MYSQL_CONN_CHARSET_DESCRIPTION,
    CONFIG_MYSQL_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.adb.mysql.AdsMySqlConfig
    CONFIG_ADSMYSQL_CONN_CHARSET_DESCRIPTION,
    CONFIG_ADSMYSQL_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.polardb.mysql.PolarDBMySqlConfig
    CONFIG_POLARDBMYSQL_CONN_CHARSET_DESCRIPTION,
    CONFIG_POLARDBMYSQL_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.polardb.x.PolarDBXConfig
    CONFIG_POLARDBX_CONN_CHARSET_DESCRIPTION,
    CONFIG_POLARDBX_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.tidb.TiDBConfig
    CONFIG_TIDB_CONN_CHARSET_DESCRIPTION,
    CONFIG_TIDB_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.postgres.config.PostgresConfig

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.polardb.pg.PolarDBPostgresConfig

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.greenplum.GreenplumConfig

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.oracle.OracleConfig
    CONFIG_ORACLE_CONNECT_TYPE_DESCRIPTION,
    CONFIG_ORACLE_DATABASE_DESCRIPTION,
    CONFIG_ORACLE_SID_DESCRIPTION,
    CONFIG_ORACLE_SERVICE_DESCRIPTION,
    CONFIG_ORACLE_PDB_DESCRIPTION,
    CONFIG_ORACLE_TNS_ADMIN_DESCRIPTION,
    CONFIG_ORACLE_TNS_NAME_DESCRIPTION,
    CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESCRIPTION,

    //
    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.ds.redis.dsconf.RedisConfig
    CONFIG_REDIS_CONN_HOST_DESCRIPTION,
    CONFIG_REDIS_USERNAME_DESCRIPTION,
    CONFIG_REDIS_PASSWORD_DESCRIPTION,
    CONFIG_REDIS_MAX_TOTAL_DESCRIPTION,
    CONFIG_REDIS_CON_AND_SO_TIMEOUT_MS_DESCRIPTION,
    CONFIG_REDIS_MAX_IDLE_DESCRIPTION,
    CONFIG_REDIS_MIN_IDLE_DESCRIPTION,
    CONFIG_REDIS_TEST_WHILE_IDLE_DESCRIPTION,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.sqlserver.SqlServerConfig

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.ds.rdb.mongo.MongoConfig
    CONFIG_MONGODB_APPLICATION_NAME_DESCRIPTION,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.starrocks.StarRocksConfig
    CONFIG_STARROCKS_CONN_CHARSET_DESCRIPTION,
    CONFIG_STARROCKS_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.doris.DorisConfig
    CONFIG_DORIS_CONN_CHARSET_DESCRIPTION,
    CONFIG_DORIS_CONN_USE_CURSOR_FETCH,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.clickhouse.ClickHouseConfig
    CONFIG_CLICKHOUSE_SESSION_TIME_OUT,
    CONFIG_CLICKHOUSE_CONNECT_TYPE_HTTP,
    CONFIG_CLICKHOUSE_CONNECT_TYPE_TCP,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.base.metadata.dsconfig.rdb.clickhouse.ObOraConfig
    CONFIG_OCEANBASE_SUB_TENANT,

    // ---------------------------------------------------------------------------------------------------
    // for Type ：com.clougence.clouddm.ds.maxcompute.dsconf.McConfig
    CONFIG_MC_INTERACTIVE_MODE_DESCRIPTION,
    CONFIG_MC_SDK_ENDPOINT_DESCRIPTION,
    CONFIG_MC_SCHEMA_STYLE_DESCRIPTION;

}
