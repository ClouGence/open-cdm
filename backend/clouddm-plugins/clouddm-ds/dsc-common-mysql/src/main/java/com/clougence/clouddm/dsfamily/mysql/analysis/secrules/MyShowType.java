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
package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import lombok.Getter;

@Getter
public enum MyShowType {

    /* show ddl */
    CREATE_DATABASE(MyShowTypeKind.DDL),
    CREATE_EVENT(MyShowTypeKind.DDL),
    CREATE_FUNCTION(MyShowTypeKind.DDL),
    CREATE_PROCEDURE(MyShowTypeKind.DDL),
    CREATE_TABLE(MyShowTypeKind.DDL),
    CREATE_TRIGGER(MyShowTypeKind.DDL),
    CREATE_USER(MyShowTypeKind.DDL),
    CREATE_VIEW(MyShowTypeKind.DDL),

    /* object metadata */
    DATABASES(MyShowTypeKind.METADATA),
    TABLES(MyShowTypeKind.METADATA),
    TABLE_STATUS(MyShowTypeKind.METADATA),
    COLUMNS(MyShowTypeKind.METADATA), // `show columns from xxx` or `desc xxx`
    INDEX(MyShowTypeKind.METADATA),
    TRIGGERS(MyShowTypeKind.METADATA),
    EVENTS(MyShowTypeKind.METADATA),
    FUNCTION_CODE(MyShowTypeKind.METADATA),
    FUNCTION_STATUS(MyShowTypeKind.METADATA),
    PROCEDURE_CODE(MyShowTypeKind.METADATA),
    PROCEDURE_STATUS(MyShowTypeKind.METADATA),

    /* environment info */
    VARIABLES(MyShowTypeKind.ENVIRONMENT),
    STATUS(MyShowTypeKind.ENVIRONMENT),
    WARNINGS(MyShowTypeKind.ENVIRONMENT),
    ERRORS(MyShowTypeKind.ENVIRONMENT),
    SALVE_STATUS(MyShowTypeKind.ENVIRONMENT),

    /* replication client/slave */
    //      -- need `REPLICATION CLIENT`
    BINARY_LOG_STATUS(MyShowTypeKind.REPLICATION),
    MASTER_STATUS(MyShowTypeKind.REPLICATION), // -- deprecated, use SHOW BINARY LOG STATUS
    REPLICA_STATUS(MyShowTypeKind.REPLICATION),
    BINARY_LOGS(MyShowTypeKind.REPLICATION),
    //      -- need `REPLICATION SLAVE`
    REPLICAS(MyShowTypeKind.REPLICATION),
    RELAYLOG_EVENTS(MyShowTypeKind.REPLICATION),
    BINLOG_EVENTS(MyShowTypeKind.REPLICATION),

    /* performance */
    PROCESSLIST(MyShowTypeKind.PERFORMANCE),
    PROFILE(MyShowTypeKind.PERFORMANCE),
    PROFILES(MyShowTypeKind.PERFORMANCE),
    OPEN_TABLES(MyShowTypeKind.PERFORMANCE),
    PARSE_TREE(MyShowTypeKind.PERFORMANCE),

    /* privilege */
    GRANTS(MyShowTypeKind.PRIVILEGE),
    PRIVILEGES(MyShowTypeKind.PRIVILEGE),

    /* compatibility */
    COLLATION(MyShowTypeKind.COMPATIBILITY),
    CHARACTER_SET(MyShowTypeKind.COMPATIBILITY),
    ENGINES(MyShowTypeKind.COMPATIBILITY),
    ENGINE(MyShowTypeKind.COMPATIBILITY),
    PLUGINS(MyShowTypeKind.COMPATIBILITY);

    private final MyShowTypeKind typeKind;

    MyShowType(MyShowTypeKind typeKind){
        this.typeKind = typeKind;
    }
}
