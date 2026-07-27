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
package com.clougence.clouddm.sdk.sql.parser;

import lombok.Getter;

@Getter
public enum SplitQueryType {
    // DDL catalog
    CREATE_CATALOG,
    ALTER_CATALOG,
    DROP_CATALOG,
    RENAME_CATALOG,
    COMMENT_CATALOG,

    // DDL schema
    CREATE_SCHEMA,
    ALTER_SCHEMA,
    DROP_SCHEMA,
    RENAME_SCHEMA,
    COMMENT_SCHEMA,

    // DDL tablespace
    CREATE_TABLESPACE,
    ALTER_TABLESPACE,
    DROP_TABLESPACE,
    RENAME_TABLESPACE,
    COMMENT_TABLESPACE,

    // DDL table （创建、修改或删除表及其附属结构，包括字段、约束、索引、分区和表级属性）
    CREATE_TABLE,
    ALTER_TABLE,
    DROP_TABLE,
    RENAME_TABLE,
    COMMENT_TABLE,
    TRUNCATE_TABLE,
    ADMIN_TABLE,

    // DDL column
    ADD_COLUMN,
    ALTER_COLUMN,
    DROP_COLUMN,
    RENAME_COLUMN,
    COMMENT_COLUMN,
    TRUNCATE_COLUMN,

    // DDL constraint pk/uk/fk
    ADD_CONSTRAINT,
    ALTER_CONSTRAINT,
    DROP_CONSTRAINT,
    RENAME_CONSTRAINT,
    COMMENT_CONSTRAINT,

    // DDL index
    ADD_INDEX,
    ALTER_INDEX,
    DROP_INDEX,
    RENAME_INDEX,
    COMMENT_INDEX,

    // DDL partition
    ADD_PARTITION,
    DROP_PARTITION,
    ALTER_PARTITION,
    TRUNCATE_PARTITION,
    ADMIN_PARTITION,
    COMMENT_PARTITION,

    // DDL view
    CREATE_VIEW,
    ALTER_VIEW,
    DROP_VIEW,
    RENAME_VIEW,
    COMMENT_VIEW,

    // DDL sequence
    CREATE_SEQUENCE,
    ALTER_SEQUENCE,
    DROP_SEQUENCE,
    RENAME_SEQUENCE,
    COMMENT_SEQUENCE,

    // DDL type
    CREATE_TYPE,
    ALTER_TYPE,
    DROP_TYPE,
    RENAME_TYPE,
    COMMENT_TYPE,
    ADMIN_TYPE,

    // DDL programming object (function, procedure, aggregate, operator, and package)
    CREATE_PROG_OBJ,
    ALTER_PROG_OBJ,
    DROP_PROG_OBJ,
    RENAME_PROG_OBJ,
    COMMENT_PROG_OBJ,
    CALL_PROG_OBJ,
    ADMIN_PROG_OBJ,

    // DDL trigger
    CREATE_TRIGGER,
    ALTER_TRIGGER,
    DROP_TRIGGER,
    RENAME_TRIGGER,
    COMMENT_TRIGGER,

    // DDL synonym
    CREATE_SYNONYM,
    ALTER_SYNONYM,
    DROP_SYNONYM,
    RENAME_SYNONYM,
    COMMENT_SYNONYM,

    // DDL event
    CREATE_EVENT,
    ALTER_EVENT,
    DROP_EVENT,
    RENAME_EVENT,
    COMMENT_EVENT,

    // DDL ResourceGroup
    CREATE_RESOURCE_GROUP,
    ALTER_RESOURCE_GROUP,
    DROP_RESOURCE_GROUP,
    ADMIN_RESOURCE_GROUP,

    // DDL job
    CREATE_JOB,
    ALTER_JOB,
    DROP_JOB,
    RENAME_JOB,
    COMMENT_JOB,
    ADMIN_JOB,

    // Auth user
    CREATE_USER,
    DROP_USER,
    RENAME_USER,
    ALTER_USER,
    COMMENT_USER,

    // Auth role
    CREATE_ROLE,
    DROP_ROLE,
    ALTER_ROLE,
    RENAME_ROLE,
    COMMENT_ROLE,
    // Auth privilege operations
    GRANT,
    REVOKE,
    TRANSFER_PRIVILEGE,

    // DDL library
    CREATE_LIBRARY,
    ALTER_LIBRARY,
    DROP_LIBRARY,
    COMMENT_LIBRARY,

    // Replication
    CREATE_REPLICATION,
    ALTER_REPLICATION,
    DROP_REPLICATION,
    ADMIN_REPLICATION,

    // Publication/subscription
    CREATE_PUB_SUB,
    ALTER_PUB_SUB,
    DROP_PUB_SUB,
    ADMIN_PUB_SUB,

    // Log
    CREATE_LOG,
    ALTER_LOG,
    DROP_LOG,
    LOG_READ,
    ADMIN_LOG,
    MAINTAIN_LOG,

    // Settings
    SESSION_VARIABLE_RW,
    SESSION_SETTING_WRITE,
    SYSTEM_SETTING_WRITE,
    ADMIN,

    // switch env
    SWITCH_CATALOG,
    SWITCH_SCHEMA,
    SWITCH_USER,
    SWITCH_ROLE,

    // DDL policy
    CREATE_POLICY,
    ALTER_POLICY,
    DROP_POLICY,

    // DQL
    SELECT(true),
    // DML
    INSERT(true),
    UPDATE(true),
    DELETE(true),
    MERGE(true),
    // Stored routine and procedural execution
    BLOCK,
    PROGRAM_CONTROL,
    // Transaction control
    TRANSACTION,
    // Lock
    QUERY_LOCK,
    SESSION_LOCK,
    // Data import and export
    DATA_IMPORT,
    DATA_EXPORT,
    // Metadata
    METADATA,

    // performance
    PERFORMANCE,
    ADMIN_PERFORMANCE,

    // Other
    UNSAFE,
    UNKNOWN;

    private final boolean allowPlan;

    SplitQueryType(){
        this(false);
    }

    SplitQueryType(boolean allowPlan){
        this.allowPlan = allowPlan;
    }
}
