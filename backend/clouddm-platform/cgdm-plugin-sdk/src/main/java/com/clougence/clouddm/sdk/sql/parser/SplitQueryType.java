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

import java.util.Objects;

import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

import lombok.Getter;

@Getter
public enum SplitQueryType {
    // DDL catalog
    CREATE_CATALOG(SecDataAuthKind.SPACE, TargetType.Catalog, SecQueryKind.CREATE),
    ALTER_CATALOG(SecDataAuthKind.SPACE, TargetType.Catalog, SecQueryKind.ALTER),
    DROP_CATALOG(SecDataAuthKind.SPACE, TargetType.Catalog, SecQueryKind.DROP),
    RENAME_CATALOG(SecDataAuthKind.SPACE, TargetType.Catalog, SecQueryKind.ALTER),
    COMMENT_CATALOG(SecDataAuthKind.SPACE, TargetType.Catalog, SecQueryKind.ALTER),

    // DDL schema
    CREATE_SCHEMA(SecDataAuthKind.SPACE, TargetType.Schema, SecQueryKind.CREATE),
    ALTER_SCHEMA(SecDataAuthKind.SPACE, TargetType.Schema, SecQueryKind.ALTER),
    DROP_SCHEMA(SecDataAuthKind.SPACE, TargetType.Schema, SecQueryKind.DROP),
    RENAME_SCHEMA(SecDataAuthKind.SPACE, TargetType.Schema, SecQueryKind.ALTER),
    COMMENT_SCHEMA(SecDataAuthKind.SPACE, TargetType.Schema, SecQueryKind.ALTER),

    // DDL tablespace
    CREATE_TABLESPACE(SecDataAuthKind.SPACE, TargetType.Tablespace, SecQueryKind.CREATE),
    ALTER_TABLESPACE(SecDataAuthKind.SPACE, TargetType.Tablespace, SecQueryKind.ALTER),
    DROP_TABLESPACE(SecDataAuthKind.SPACE, TargetType.Tablespace, SecQueryKind.DROP),
    RENAME_TABLESPACE(SecDataAuthKind.SPACE, TargetType.Tablespace, SecQueryKind.ALTER),
    COMMENT_TABLESPACE(SecDataAuthKind.SPACE, TargetType.Tablespace, SecQueryKind.ALTER),

    // DDL table （创建、修改或删除表及其附属结构，包括字段、约束、索引、分区和表级属性）
    CREATE_TABLE(SecDataAuthKind.DDL, TargetType.Table, SecQueryKind.CREATE),
    ALTER_TABLE(SecDataAuthKind.DDL, TargetType.Table, SecQueryKind.ALTER),
    DROP_TABLE(SecDataAuthKind.DDL, TargetType.Table, SecQueryKind.DROP),
    RENAME_TABLE(SecDataAuthKind.DDL, TargetType.Table, SecQueryKind.ALTER),
    COMMENT_TABLE(SecDataAuthKind.DDL, TargetType.Table, SecQueryKind.ALTER),
    TRUNCATE_TABLE(SecDataAuthKind.DDL, TargetType.Table, SecQueryKind.ALTER),
    ADMIN_TABLE(SecDataAuthKind.ADMIN, TargetType.Table, SecQueryKind.ADMIN),

    // DDL column
    ADD_COLUMN(SecDataAuthKind.DDL, TargetType.Column, SecQueryKind.ALTER),
    ALTER_COLUMN(SecDataAuthKind.DDL, TargetType.Column, SecQueryKind.ALTER),
    DROP_COLUMN(SecDataAuthKind.DDL, TargetType.Column, SecQueryKind.ALTER),
    RENAME_COLUMN(SecDataAuthKind.DDL, TargetType.Column, SecQueryKind.ALTER),
    COMMENT_COLUMN(SecDataAuthKind.DDL, TargetType.Column, SecQueryKind.ALTER),
    TRUNCATE_COLUMN(SecDataAuthKind.DDL, TargetType.Column, SecQueryKind.ALTER),

    // DDL constraint pk/uk/fk
    ADD_CONSTRAINT(SecDataAuthKind.DDL, TargetType.Constraint, SecQueryKind.ALTER),
    ALTER_CONSTRAINT(SecDataAuthKind.DDL, TargetType.Constraint, SecQueryKind.ALTER),
    DROP_CONSTRAINT(SecDataAuthKind.DDL, TargetType.Constraint, SecQueryKind.ALTER),
    RENAME_CONSTRAINT(SecDataAuthKind.DDL, TargetType.Constraint, SecQueryKind.ALTER),
    COMMENT_CONSTRAINT(SecDataAuthKind.DDL, TargetType.Constraint, SecQueryKind.ALTER),

    // DDL index
    ADD_INDEX(SecDataAuthKind.DDL, TargetType.Index, SecQueryKind.CREATE),
    ALTER_INDEX(SecDataAuthKind.DDL, TargetType.Index, SecQueryKind.ALTER),
    DROP_INDEX(SecDataAuthKind.DDL, TargetType.Index, SecQueryKind.DROP),
    RENAME_INDEX(SecDataAuthKind.DDL, TargetType.Index, SecQueryKind.ALTER),
    COMMENT_INDEX(SecDataAuthKind.DDL, TargetType.Index, SecQueryKind.ALTER),

    // DDL partition
    ADD_PARTITION(SecDataAuthKind.DDL, TargetType.Partition, SecQueryKind.ALTER),
    DROP_PARTITION(SecDataAuthKind.DDL, TargetType.Partition, SecQueryKind.ALTER),
    ALTER_PARTITION(SecDataAuthKind.DDL, TargetType.Partition, SecQueryKind.ALTER),
    TRUNCATE_PARTITION(SecDataAuthKind.DDL, TargetType.Partition, SecQueryKind.ALTER),
    ADMIN_PARTITION(SecDataAuthKind.ADMIN, TargetType.Partition, SecQueryKind.ADMIN),
    COMMENT_PARTITION(SecDataAuthKind.DDL, TargetType.Partition, SecQueryKind.ALTER),

    // DDL view
    CREATE_VIEW(SecDataAuthKind.OBJECT, TargetType.View, SecQueryKind.CREATE),
    ALTER_VIEW(SecDataAuthKind.OBJECT, TargetType.View, SecQueryKind.ALTER),
    DROP_VIEW(SecDataAuthKind.OBJECT, TargetType.View, SecQueryKind.DROP),
    RENAME_VIEW(SecDataAuthKind.OBJECT, TargetType.View, SecQueryKind.ALTER),
    COMMENT_VIEW(SecDataAuthKind.OBJECT, TargetType.View, SecQueryKind.ALTER),

    // DDL sequence
    CREATE_SEQUENCE(SecDataAuthKind.OBJECT, TargetType.Sequence, SecQueryKind.CREATE),
    ALTER_SEQUENCE(SecDataAuthKind.OBJECT, TargetType.Sequence, SecQueryKind.ALTER),
    DROP_SEQUENCE(SecDataAuthKind.OBJECT, TargetType.Sequence, SecQueryKind.DROP),
    RENAME_SEQUENCE(SecDataAuthKind.OBJECT, TargetType.Sequence, SecQueryKind.ALTER),
    COMMENT_SEQUENCE(SecDataAuthKind.OBJECT, TargetType.Sequence, SecQueryKind.ALTER),

    // DDL type
    CREATE_TYPE(SecDataAuthKind.OBJECT, TargetType.Type, SecQueryKind.CREATE),
    ALTER_TYPE(SecDataAuthKind.OBJECT, TargetType.Type, SecQueryKind.ALTER),
    DROP_TYPE(SecDataAuthKind.OBJECT, TargetType.Type, SecQueryKind.DROP),
    RENAME_TYPE(SecDataAuthKind.OBJECT, TargetType.Type, SecQueryKind.ALTER),
    COMMENT_TYPE(SecDataAuthKind.OBJECT, TargetType.Type, SecQueryKind.ALTER),
    ADMIN_TYPE(SecDataAuthKind.ADMIN, TargetType.Type, SecQueryKind.ADMIN),

    // DDL programming object (function, procedure, aggregate, operator, and package)
    CREATE_PROG_OBJ(SecDataAuthKind.OBJECT, TargetType.ProgramObject, SecQueryKind.CREATE),
    ALTER_PROG_OBJ(SecDataAuthKind.OBJECT, TargetType.ProgramObject, SecQueryKind.ALTER),
    DROP_PROG_OBJ(SecDataAuthKind.OBJECT, TargetType.ProgramObject, SecQueryKind.DROP),
    RENAME_PROG_OBJ(SecDataAuthKind.OBJECT, TargetType.ProgramObject, SecQueryKind.ALTER),
    COMMENT_PROG_OBJ(SecDataAuthKind.OBJECT, TargetType.ProgramObject, SecQueryKind.ALTER),
    CALL_PROG_OBJ(SecDataAuthKind.CALL, TargetType.ProgramObject, SecQueryKind.CALL),
    ADMIN_PROG_OBJ(SecDataAuthKind.ADMIN, TargetType.ProgramObject, SecQueryKind.ADMIN),

    // DDL trigger
    CREATE_TRIGGER(SecDataAuthKind.OBJECT, TargetType.Trigger, SecQueryKind.CREATE),
    ALTER_TRIGGER(SecDataAuthKind.OBJECT, TargetType.Trigger, SecQueryKind.ALTER),
    DROP_TRIGGER(SecDataAuthKind.OBJECT, TargetType.Trigger, SecQueryKind.DROP),
    RENAME_TRIGGER(SecDataAuthKind.OBJECT, TargetType.Trigger, SecQueryKind.ALTER),
    COMMENT_TRIGGER(SecDataAuthKind.OBJECT, TargetType.Trigger, SecQueryKind.ALTER),

    // DDL synonym
    CREATE_SYNONYM(SecDataAuthKind.OBJECT, TargetType.Synonym, SecQueryKind.CREATE),
    ALTER_SYNONYM(SecDataAuthKind.OBJECT, TargetType.Synonym, SecQueryKind.ALTER),
    DROP_SYNONYM(SecDataAuthKind.OBJECT, TargetType.Synonym, SecQueryKind.DROP),
    RENAME_SYNONYM(SecDataAuthKind.OBJECT, TargetType.Synonym, SecQueryKind.ALTER),
    COMMENT_SYNONYM(SecDataAuthKind.OBJECT, TargetType.Synonym, SecQueryKind.ALTER),

    // DDL event
    CREATE_EVENT(SecDataAuthKind.OBJECT, TargetType.Event, SecQueryKind.CREATE),
    ALTER_EVENT(SecDataAuthKind.OBJECT, TargetType.Event, SecQueryKind.ALTER),
    DROP_EVENT(SecDataAuthKind.OBJECT, TargetType.Event, SecQueryKind.DROP),
    RENAME_EVENT(SecDataAuthKind.OBJECT, TargetType.Event, SecQueryKind.ALTER),
    COMMENT_EVENT(SecDataAuthKind.OBJECT, TargetType.Event, SecQueryKind.ALTER),

    // DDL ResourceGroup
    CREATE_RESOURCE_GROUP(SecDataAuthKind.ADMIN, TargetType.ResourceGroup, SecQueryKind.CREATE),
    ALTER_RESOURCE_GROUP(SecDataAuthKind.ADMIN, TargetType.ResourceGroup, SecQueryKind.ALTER),
    DROP_RESOURCE_GROUP(SecDataAuthKind.ADMIN, TargetType.ResourceGroup, SecQueryKind.DROP),
    ADMIN_RESOURCE_GROUP(SecDataAuthKind.ADMIN, TargetType.ResourceGroup, SecQueryKind.ADMIN),

    // DDL job
    CREATE_JOB(SecDataAuthKind.ADMIN, TargetType.Job, SecQueryKind.CREATE),
    ALTER_JOB(SecDataAuthKind.ADMIN, TargetType.Job, SecQueryKind.ALTER),
    DROP_JOB(SecDataAuthKind.ADMIN, TargetType.Job, SecQueryKind.DROP),
    RENAME_JOB(SecDataAuthKind.ADMIN, TargetType.Job, SecQueryKind.ALTER),
    COMMENT_JOB(SecDataAuthKind.ADMIN, TargetType.Job, SecQueryKind.ALTER),
    ADMIN_JOB(SecDataAuthKind.ADMIN, TargetType.Job, SecQueryKind.ADMIN),

    // Auth user
    CREATE_USER(SecDataAuthKind.ADMIN, TargetType.User, SecQueryKind.CREATE),
    DROP_USER(SecDataAuthKind.ADMIN, TargetType.User, SecQueryKind.DROP),
    RENAME_USER(SecDataAuthKind.ADMIN, TargetType.User, SecQueryKind.ALTER),
    ALTER_USER(SecDataAuthKind.ADMIN, TargetType.User, SecQueryKind.ALTER),
    COMMENT_USER(SecDataAuthKind.ADMIN, TargetType.User, SecQueryKind.ALTER),

    // Auth role
    CREATE_ROLE(SecDataAuthKind.ADMIN, TargetType.Role, SecQueryKind.CREATE),
    DROP_ROLE(SecDataAuthKind.ADMIN, TargetType.Role, SecQueryKind.DROP),
    ALTER_ROLE(SecDataAuthKind.ADMIN, TargetType.Role, SecQueryKind.ALTER),
    RENAME_ROLE(SecDataAuthKind.ADMIN, TargetType.Role, SecQueryKind.ALTER),
    COMMENT_ROLE(SecDataAuthKind.ADMIN, TargetType.Role, SecQueryKind.ALTER),
    // Auth privilege operations
    GRANT(SecDataAuthKind.ADMIN, TargetType.UserOrRole, SecQueryKind.ALTER),
    REVOKE(SecDataAuthKind.ADMIN, TargetType.UserOrRole, SecQueryKind.ALTER),
    TRANSFER_PRIVILEGE(SecDataAuthKind.ADMIN, TargetType.UserOrRole, SecQueryKind.ALTER),

    // DDL library
    CREATE_LIBRARY(SecDataAuthKind.OBJECT, TargetType.Library, SecQueryKind.CREATE),
    ALTER_LIBRARY(SecDataAuthKind.OBJECT, TargetType.Library, SecQueryKind.ALTER),
    DROP_LIBRARY(SecDataAuthKind.OBJECT, TargetType.Library, SecQueryKind.DROP),
    COMMENT_LIBRARY(SecDataAuthKind.OBJECT, TargetType.Library, SecQueryKind.ALTER),

    // Replication
    CREATE_REPLICATION(SecDataAuthKind.ADMIN, TargetType.Replication, SecQueryKind.CREATE),
    ALTER_REPLICATION(SecDataAuthKind.ADMIN, TargetType.Replication, SecQueryKind.ALTER),
    DROP_REPLICATION(SecDataAuthKind.ADMIN, TargetType.Replication, SecQueryKind.DROP),
    ADMIN_REPLICATION(SecDataAuthKind.ADMIN, TargetType.Replication, SecQueryKind.ADMIN),

    // Publication/subscription
    CREATE_PUB_SUB(SecDataAuthKind.ADMIN, TargetType.PublicationSubscription, SecQueryKind.CREATE),
    ALTER_PUB_SUB(SecDataAuthKind.ADMIN, TargetType.PublicationSubscription, SecQueryKind.ALTER),
    DROP_PUB_SUB(SecDataAuthKind.ADMIN, TargetType.PublicationSubscription, SecQueryKind.DROP),
    ADMIN_PUB_SUB(SecDataAuthKind.ADMIN, TargetType.PublicationSubscription, SecQueryKind.ADMIN),

    // Log
    CREATE_LOG(SecDataAuthKind.ADMIN, TargetType.Log, SecQueryKind.CREATE),
    ALTER_LOG(SecDataAuthKind.ADMIN, TargetType.Log, SecQueryKind.ALTER),
    DROP_LOG(SecDataAuthKind.ADMIN, TargetType.Log, SecQueryKind.DROP),
    LOG_READ(SecDataAuthKind.READ, TargetType.Log, SecQueryKind.READ),
    ADMIN_LOG(SecDataAuthKind.ADMIN, TargetType.Log, SecQueryKind.ADMIN),
    MAINTAIN_LOG(SecDataAuthKind.ADMIN, TargetType.Log, SecQueryKind.ADMIN),

    // Settings
    SESSION_VARIABLE_RW(SecDataAuthKind.ADMIN, TargetType.ConfigKey, SecQueryKind.OTHER),
    SESSION_SETTING_WRITE(SecDataAuthKind.ADMIN, TargetType.ConfigKey, SecQueryKind.OTHER),
    SYSTEM_SETTING_WRITE(SecDataAuthKind.ADMIN, TargetType.ConfigKey, SecQueryKind.OTHER),
    ADMIN(SecDataAuthKind.ADMIN, TargetType.Unknown, SecQueryKind.ADMIN),

    // switch env
    SWITCH_CATALOG(SecDataAuthKind.READ, TargetType.Catalog, SecQueryKind.QUERY),
    SWITCH_SCHEMA(SecDataAuthKind.READ, TargetType.Schema, SecQueryKind.QUERY),
    SWITCH_USER(SecDataAuthKind.READ, TargetType.User, SecQueryKind.OTHER),
    SWITCH_ROLE(SecDataAuthKind.READ, TargetType.Role, SecQueryKind.OTHER),

    // DDL policy
    CREATE_POLICY(SecDataAuthKind.ADMIN, TargetType.Policy, SecQueryKind.CREATE),
    ALTER_POLICY(SecDataAuthKind.ADMIN, TargetType.Policy, SecQueryKind.ALTER),
    DROP_POLICY(SecDataAuthKind.ADMIN, TargetType.Policy, SecQueryKind.DROP),

    // DQL
    SELECT(SecDataAuthKind.READ, TargetType.Query, SecQueryKind.QUERY, true),
    // DML
    INSERT(SecDataAuthKind.WRITE, TargetType.Insert, SecQueryKind.DML, true),
    UPDATE(SecDataAuthKind.WRITE, TargetType.Update, SecQueryKind.DML, true),
    DELETE(SecDataAuthKind.WRITE, TargetType.Delete, SecQueryKind.DML, true),
    MERGE(SecDataAuthKind.WRITE, TargetType.Update, SecQueryKind.DML),
    // Stored routine and procedural execution
    BLOCK(SecDataAuthKind.OTHER, TargetType.Unknown, SecQueryKind.OTHER),
    PROGRAM_CONTROL(SecDataAuthKind.OTHER, TargetType.Unknown, SecQueryKind.OTHER),
    // Transaction control
    TRANSACTION(SecDataAuthKind.OTHER, TargetType.Unknown, SecQueryKind.OTHER),
    // Lock
    QUERY_LOCK(SecDataAuthKind.OTHER, TargetType.Query, SecQueryKind.OTHER),
    SESSION_LOCK(SecDataAuthKind.OTHER, TargetType.Unknown, SecQueryKind.OTHER),
    // Data import and export
    DATA_IMPORT(SecDataAuthKind.WRITE, TargetType.Unknown, SecQueryKind.ADMIN),
    DATA_EXPORT(SecDataAuthKind.READ, TargetType.Unknown, SecQueryKind.ADMIN),
    // Metadata
    METADATA(SecDataAuthKind.READ, TargetType.Unknown, SecQueryKind.QUERY),

    // performance
    PERFORMANCE(SecDataAuthKind.READ, TargetType.Query, SecQueryKind.QUERY),
    ADMIN_PERFORMANCE(SecDataAuthKind.ADMIN, TargetType.Unknown, SecQueryKind.ADMIN),

    // Other
    UNSAFE(SecDataAuthKind.OTHER, TargetType.Unknown, SecQueryKind.OTHER),
    UNKNOWN(SecDataAuthKind.OTHER, TargetType.Unknown, SecQueryKind.OTHER);

    private final TargetType      target;
    private final SecDataAuthKind authKind;
    private final SecQueryKind    auditKind;
    private final boolean         allowPlan;

    SplitQueryType(SecDataAuthKind authKind, TargetType target, SecQueryKind auditKind){
        this.target = Objects.requireNonNull(target);
        this.authKind = Objects.requireNonNull(authKind);
        this.auditKind = auditKind;
        this.allowPlan = false;
    }

    SplitQueryType(SecDataAuthKind authKind, TargetType target, SecQueryKind auditKind, boolean allowPlan){
        this.target = Objects.requireNonNull(target);
        this.authKind = Objects.requireNonNull(authKind);
        this.auditKind = auditKind;
        this.allowPlan = allowPlan;
    }
}
