package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import lombok.Getter;

@Getter
public enum McShowType {

    /* show ddl */
    CREATE_DATABASE(McShowTypeKind.DDL),
    CREATE_EVENT(McShowTypeKind.DDL),
    CREATE_FUNCTION(McShowTypeKind.DDL),
    CREATE_PROCEDURE(McShowTypeKind.DDL),
    CREATE_TABLE(McShowTypeKind.DDL),
    CREATE_TRIGGER(McShowTypeKind.DDL),
    CREATE_USER(McShowTypeKind.DDL),
    CREATE_VIEW(McShowTypeKind.DDL),

    /* object metadata */
    DATABASES(McShowTypeKind.METADATA),
    TABLES(McShowTypeKind.METADATA),
    TABLE_STATUS(McShowTypeKind.METADATA),
    COLUMNS(McShowTypeKind.METADATA), // `show columns from xxx` or `desc xxx`
    INDEX(McShowTypeKind.METADATA),
    TRIGGERS(McShowTypeKind.METADATA),
    EVENTS(McShowTypeKind.METADATA),
    FUNCTION_CODE(McShowTypeKind.METADATA),
    FUNCTION_STATUS(McShowTypeKind.METADATA),
    PROCEDURE_CODE(McShowTypeKind.METADATA),
    PROCEDURE_STATUS(McShowTypeKind.METADATA),

    /* environment info */
    VARIABLES(McShowTypeKind.ENVIRONMENT),
    STATUS(McShowTypeKind.ENVIRONMENT),
    WARNINGS(McShowTypeKind.ENVIRONMENT),
    ERRORS(McShowTypeKind.ENVIRONMENT),

    /* replication client/slave */
    //      -- need `REPLICATION CLIENT`
    BINARY_LOG_STATUS(McShowTypeKind.REPLICATION),
    MASTER_STATUS(McShowTypeKind.REPLICATION), // -- deprecated, use SHOW BINARY LOG STATUS
    REPLICA_STATUS(McShowTypeKind.REPLICATION),
    BINARY_LOGS(McShowTypeKind.REPLICATION),
    //      -- need `REPLICATION SLAVE`
    REPLICAS(McShowTypeKind.REPLICATION),
    RELAYLOG_EVENTS(McShowTypeKind.REPLICATION),
    BINLOG_EVENTS(McShowTypeKind.REPLICATION),

    /* performance */
    PROCESSLIST(McShowTypeKind.PERFORMANCE),
    PROFILE(McShowTypeKind.PERFORMANCE),
    PROFILES(McShowTypeKind.PERFORMANCE),
    OPEN_TABLES(McShowTypeKind.PERFORMANCE),
    PARSE_TREE(McShowTypeKind.PERFORMANCE),

    /* privilege */
    GRANTS(McShowTypeKind.PRIVILEGE),
    PRIVILEGES(McShowTypeKind.PRIVILEGE),

    /* compatibility */
    COLLATION(McShowTypeKind.COMPATIBILITY),
    CHARACTER_SET(McShowTypeKind.COMPATIBILITY),
    ENGINES(McShowTypeKind.COMPATIBILITY),
    ENGINE(McShowTypeKind.COMPATIBILITY),
    PLUGINS(McShowTypeKind.COMPATIBILITY),;

    private final McShowTypeKind typeKind;

    McShowType(McShowTypeKind typeKind){
        this.typeKind = typeKind;
    }
}
