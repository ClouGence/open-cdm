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
package com.clougence.drivers;

/**
 * @author mode 2021/4/25 16:13
 */
public enum DsConfigKeys {

    ///////////////////
    //    Commons    //
    ///////////////////
    ID("com_id"),
    HOST("com_host"),
    USER("com_user", true),
    PASSWORD("com_password", true),
    DEFAULT_DATABASE("com_default_database"),
    DEFAULT_SCHEMA("com_default_schema"),
    CUSTOM_URL("com_url"),

    LOGIN_TIMEOUT_MS("com_login_timeout_ms"),
    CONNECT_TIMEOUT_MS("com_connect_timeout_ms"),
    SO_TIMEOUT_SEC("com_so_timeout_sec"),
    TCP_KEEP_ALIVE("com_tcpKeepAlive"),
    TCP_RCV_BUFFER("com_tcpReceiveBufferSize"),
    TCP_SND_BUFFER("com_tcpSendBufferSize"),
    CLIENT_NAME("com_client_name"),
    CLIENT_TIME_ZONE("com_client_time_zone"),
    // - if pg set it then allowEncodingChanges well be set true , and new connection execute "set client_encoding=xxx"
    CLIENT_ENCODING("com_client_encoding"),
    AUTO_COMMIT("com_auto_commit"),
    SSL("com_ssl"),
    SSL_CA_FILE("com_ssl_ca_file"),
    SSL_CLIENT_CERT_FILE("com_ssl_client_cert_file"),
    SSL_CLIENT_KEY_FILE("com_ssl_client_key_file"),
    SSL_CLIENT_KEY_PASSWORD("com_ssl_client_key_password"),
    SSL_MODE("com_ssl_mode"),

    //////////////////////////
    //    Only for Mysql    //
    //////////////////////////
    MY_SQL_MODE("my_sql_mode"),
    MY_MAX_ALLOWED_PACKET("my_max_allowed_packet"), // use "set global max_allowed_packet = ?"

    ///////////////////////////////
    //    Only for Postgresql    //
    ///////////////////////////////
    PG_INTERVAL_STYLE("pg_interval_style"), // use "set intervalstyle = 'xxx'"

    ///////////////////////////
    //    Only for Oracle    //
    ///////////////////////////
    ORA_ORACLE_CONNECT_TYPE("ora_oracle_connect_type"), // sid/service/tns/pdb
    ORA_SID("ora_sid"),
    ORA_SERVICE_NAME("ora_service_name"),
    ORA_PDB("ora_pdb"),
    ORA_TNS_ADMIN("ora_tns_admin"),
    ORA_TNS_NAME("ora_tns_name"),

    ///////////////////////////
    //    Only for Dameng    //
    ///////////////////////////
    DM_SSL_FILE_PATH("dm_ssl_file_path"),
    DM_SSL_PASSWORD("dm_ssl_password"),

    /////////////////////////
    //    Only for KUDU    //
    /////////////////////////
    KD_WORKER_COUNT("kd_worker_count"),
    KD_DDL_TIMEOUT_MS("kd_ddl_timeout_ms"),
    KD_DML_TIMEOUT_MS("kd_dml_timeout_ms"),

    ///////////////////////////////
    //    Only for ClickHouse    //
    ///////////////////////////////
    CH_SESSION_TIMEOUT_MS("ch_session_timeout_ms"),

    ///////////////////////////////
    //    Only for OceanBase     //
    ///////////////////////////////
    OB_TENANT("ob_tenant"),
    OB_CLUSTER("ob_cluster"),

    ///////////////////////////////
    //    Only for DuckDB        //
    ///////////////////////////////
    DUCK_MD_TOKEN("motherduck_token"),

    ///////////////////////////////
    //    Only for ODPS          //
    ///////////////////////////////
    ODPS_INTERACTIVE("odps_interactive"),
    ODPS_SCHEMA_STYLE("odps_schema_style"),

    ///////////////////////////////
    //    Only for Redis(Jedis)  //
    ///////////////////////////////
    JEDIS_MAX_TOTAL("jedis_max_total"),
    JEDIS_MAX_IDLE("jedis_max_idle"),
    JEDIS_MIN_IDLE("jedis_min_idle"),
    JEDIS_TEST_WHILE_IDLE("jedis_test_while_idle"),;

    private final String  configKey;
    private final boolean sensitive;

    DsConfigKeys(String configKey){
        this(configKey, false);
    }

    DsConfigKeys(String configKey, boolean sensitive){
        this.configKey = configKey;
        this.sensitive = sensitive;
    }

    public String getConfigKey() { return configKey; }

    public boolean isSensitive() { return sensitive; }
}
