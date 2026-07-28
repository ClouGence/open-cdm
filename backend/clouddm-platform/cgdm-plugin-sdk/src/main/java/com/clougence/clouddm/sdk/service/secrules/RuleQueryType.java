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
package com.clougence.clouddm.sdk.service.secrules;

import java.util.Objects;

import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

import lombok.Getter;

@Getter
@Deprecated
public enum RuleQueryType {

    // DDL catalog
    CREATE_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.CREATE),
    ALTER_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    DROP_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.DROP),
    RENAME_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    COMMENT_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.ALTER),

    // DDL schema
    CREATE_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.CREATE),
    ALTER_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    DROP_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.DROP),
    RENAME_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    COMMENT_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.ALTER),

    // DDL tablespace
    CREATE_TABLESPACE(TargetType.Tablespace, SecDataAuthKind.SPACE, SecQueryKind.CREATE),
    ALTER_TABLESPACE(TargetType.Tablespace, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    DROP_TABLESPACE(TargetType.Tablespace, SecDataAuthKind.SPACE, SecQueryKind.DROP),
    RENAME_TABLESPACE(TargetType.Tablespace, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    COMMENT_TABLESPACE(TargetType.Tablespace, SecDataAuthKind.SPACE, SecQueryKind.ALTER),

    // DDL table
    CREATE_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    CREATE_TABLE_SELECT(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    CREATE_TABLE_LIKE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_RENAME(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    TRUNCATE_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ADMIN_TABLE(TargetType.Table, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // DDL column
    CREATE_TABLE_ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE_ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_ALTER_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_DROP_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_RENAME_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    RENAME_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    TRUNCATE_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL constraint pk/uk/fk
    CREATE_TABLE_ADD_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE_ADD_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_DROP_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ADD_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    RENAME_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL index
    CREATE_TABLE_ADD_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE_ADD_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ADD_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    CREATE_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL partition
    ADD_PARTITION(TargetType.Partition, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_PARTITION(TargetType.Partition, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_PARTITION(TargetType.Partition, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    TRUNCATE_PARTITION(TargetType.Partition, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ADMIN_PARTITION(TargetType.Partition, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),
    COMMENT_PARTITION(TargetType.Partition, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL view
    CREATE_VIEW(TargetType.View, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_VIEW(TargetType.View, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_VIEW(TargetType.View, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_VIEW(TargetType.View, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_VIEW(TargetType.View, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    CREATE_MATERIALIZED_VIEW(TargetType.Materialized, SecDataAuthKind.DDL, SecQueryKind.CREATE),

    // another object
    CREATE_OBJECT(TargetType.Unknown, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_OBJECT(TargetType.Unknown, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_OBJECT(TargetType.Unknown, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL sequence
    CREATE_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL type
    CREATE_TYPE(TargetType.Type, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TYPE(TargetType.Type, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_TYPE(TargetType.Type, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_TYPE(TargetType.Type, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_TYPE(TargetType.Type, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ADMIN_TYPE(TargetType.Type, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // DDL programming object
    CREATE_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    ALTER_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    DROP_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.PROGRAM, SecQueryKind.DROP),
    RENAME_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    COMMENT_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    CALL_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.CALL, SecQueryKind.CALL),
    ADMIN_PROG_OBJ(TargetType.ProgramObject, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // DDL function
    CREATE_FUNCTION(TargetType.Function, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    ALTER_FUNCTION(TargetType.Function, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    DROP_FUNCTION(TargetType.Function, SecDataAuthKind.PROGRAM, SecQueryKind.DROP),

    // DDL procedure
    CREATE_PROCEDURE(TargetType.Procedure, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    ALTER_PROCEDURE(TargetType.Procedure, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    DROP_PROCEDURE(TargetType.Procedure, SecDataAuthKind.PROGRAM, SecQueryKind.DROP),

    // DDL trigger
    CREATE_TRIGGER(TargetType.Trigger, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    ALTER_TRIGGER(TargetType.Trigger, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    DROP_TRIGGER(TargetType.Trigger, SecDataAuthKind.PROGRAM, SecQueryKind.DROP),
    RENAME_TRIGGER(TargetType.Trigger, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    COMMENT_TRIGGER(TargetType.Trigger, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),

    // DDL synonym
    CREATE_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL event
    CREATE_EVENT(TargetType.Event, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    ALTER_EVENT(TargetType.Event, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    DROP_EVENT(TargetType.Event, SecDataAuthKind.PROGRAM, SecQueryKind.DROP),
    RENAME_EVENT(TargetType.Event, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    COMMENT_EVENT(TargetType.Event, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),

    // DDL resource group
    CREATE_RESOURCE_GROUP(TargetType.ResourceGroup, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    ALTER_RESOURCE_GROUP(TargetType.ResourceGroup, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    DROP_RESOURCE_GROUP(TargetType.ResourceGroup, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    ADMIN_RESOURCE_GROUP(TargetType.ResourceGroup, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // DDL job
    CREATE_JOB(TargetType.Job, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    ALTER_JOB(TargetType.Job, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    DROP_JOB(TargetType.Job, SecDataAuthKind.PROGRAM, SecQueryKind.DROP),
    RENAME_JOB(TargetType.Job, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    COMMENT_JOB(TargetType.Job, SecDataAuthKind.PROGRAM, SecQueryKind.ALTER),
    ADMIN_JOB(TargetType.Job, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // DCL
    CREATE_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    DROP_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    RENAME_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    COMMENT_USER(TargetType.User, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    GRANT(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    REVOKE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    TRANSFER_PRIVILEGE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    CREATE_ROLE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    DROP_ROLE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    ALTER_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    ALTER_ROLE(TargetType.Role, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    RENAME_ROLE(TargetType.Role, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    COMMENT_ROLE(TargetType.Role, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    CONFIG_WRITE(TargetType.ConfigKey, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),

    // DDL library
    CREATE_LIBRARY(TargetType.Library, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    ALTER_LIBRARY(TargetType.Library, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    DROP_LIBRARY(TargetType.Library, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    COMMENT_LIBRARY(TargetType.Library, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),

    // Replication
    CREATE_REPLICATION(TargetType.Replication, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    ALTER_REPLICATION(TargetType.Replication, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    DROP_REPLICATION(TargetType.Replication, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    ADMIN_REPLICATION(TargetType.Replication, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // Publication/subscription
    CREATE_PUB_SUB(TargetType.PublicationSubscription, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    ALTER_PUB_SUB(TargetType.PublicationSubscription, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    DROP_PUB_SUB(TargetType.PublicationSubscription, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    ADMIN_PUB_SUB(TargetType.PublicationSubscription, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // Log
    CREATE_LOG(TargetType.Log, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    ALTER_LOG(TargetType.Log, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    DROP_LOG(TargetType.Log, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    LOG_READ(TargetType.Log, SecDataAuthKind.READ, SecQueryKind.READ),
    ADMIN_LOG(TargetType.Log, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),
    MAINTAIN_LOG(TargetType.Log, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // Settings
    SESSION_VARIABLE_RW(TargetType.ConfigKey, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    SESSION_SETTING_WRITE(TargetType.ConfigKey, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    SYSTEM_SETTING_WRITE(TargetType.ConfigKey, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),

    // switch context
    SWITCH_CATALOG(TargetType.Catalog, SecDataAuthKind.READ, SecQueryKind.QUERY),
    SWITCH_SCHEMA(TargetType.Schema, SecDataAuthKind.READ, SecQueryKind.QUERY),
    SWITCH_USER(TargetType.User, SecDataAuthKind.READ, SecQueryKind.OTHER),
    SWITCH_ROLE(TargetType.Role, SecDataAuthKind.READ, SecQueryKind.OTHER),

    // DDL policy
    CREATE_POLICY(TargetType.Policy, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    ALTER_POLICY(TargetType.Policy, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    DROP_POLICY(TargetType.Policy, SecDataAuthKind.ADMIN, SecQueryKind.DROP),

    // dql and dml and call.
    SELECT(TargetType.Query, SecDataAuthKind.READ, SecQueryKind.QUERY, true),
    INSERT(TargetType.Insert, SecDataAuthKind.WRITE, SecQueryKind.DML, true),
    UPDATE(TargetType.Update, SecDataAuthKind.WRITE, SecQueryKind.DML, true),
    DELETE(TargetType.Delete, SecDataAuthKind.WRITE, SecQueryKind.DML, true),
    MERGE(TargetType.Update, SecDataAuthKind.WRITE, SecQueryKind.DML),
    REPLACE(TargetType.Unknown, SecDataAuthKind.WRITE, SecQueryKind.DML),
    CALL(TargetType.Call, SecDataAuthKind.CALL, SecQueryKind.CALL),
    TRUNCATE(TargetType.Delete, SecDataAuthKind.WRITE, SecQueryKind.DML),
    EXPLAIN(TargetType.Query, SecDataAuthKind.READ, SecQueryKind.QUERY),
    LOAD(TargetType.Insert, SecDataAuthKind.WRITE, SecQueryKind.DML),

    // Stored routine and procedural execution
    BLOCK(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    PROGRAM_CONTROL(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),

    // Lock
    QUERY_LOCK(TargetType.Query, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    SESSION_LOCK(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),

    // Data import and export
    DATA_IMPORT(TargetType.Unknown, SecDataAuthKind.WRITE, SecQueryKind.ADMIN),
    DATA_EXPORT(TargetType.Unknown, SecDataAuthKind.READ, SecQueryKind.ADMIN),

    // Metadata and performance
    METADATA(TargetType.Unknown, SecDataAuthKind.READ, SecQueryKind.QUERY),
    PERFORMANCE(TargetType.Query, SecDataAuthKind.READ, SecQueryKind.QUERY),
    ADMIN_PERFORMANCE(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // common specific
    SHOW(TargetType.Unknown, SecDataAuthKind.READ, SecQueryKind.QUERY),
    READ(TargetType.Unknown, SecDataAuthKind.READ, SecQueryKind.READ),
    WRITE(TargetType.Unknown, SecDataAuthKind.WRITE, SecQueryKind.WRITE),
    ADMIN(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),
    ANALYZE(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    OPTIMIZE(TargetType.Table, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    CHECK_TABLE(TargetType.Table, SecDataAuthKind.OTHER, SecQueryKind.OTHER),

    // transaction
    TRANSACTION(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),

    //
    PREPARE(TargetType.PrepareStatement, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    EXECUTE(TargetType.PrepareStatement, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    DEALLOCATE(TargetType.PrepareStatement, SecDataAuthKind.OTHER, SecQueryKind.OTHER),

    //
    SQL_BLOCK(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),

    // MySQL specific
    MYSQL_FLUSH(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),
    MYSQL_REPLACE_INTO(TargetType.Insert, SecDataAuthKind.WRITE, SecQueryKind.DML),
    MYSQL_ALTER_TABLE_CONVERT(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    REPAIR(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    CREATE_UDF_FUNCTION(TargetType.Function, SecDataAuthKind.PROGRAM, SecQueryKind.CREATE),
    INSTALL_PLUGIN(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    UNINSTALL_PLUGIN(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    RESET(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    PURGE(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    LOAD_INDEX_INTO_CACHE(TargetType.Index, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),
    KILL(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),

    // Redis specific
    REDIS_SCRIPT(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),
    REDIS_MONITOR(TargetType.Unknown, SecDataAuthKind.READ, SecQueryKind.READ),
    REDIS_CONNECTION(TargetType.Unknown, SecDataAuthKind.READ, SecQueryKind.READ),
    REDIS_PUBSUB(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),
    REDIS_TRANSACTION(TargetType.Unknown, SecDataAuthKind.ADMIN, SecQueryKind.ADMIN),

    // PostgreSQL specific
    REFRESH_VIEW(TargetType.Materialized, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    REFRESH_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    REFRESH_SCHEMA(TargetType.Schema, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    REFRESH_CATALOG(TargetType.Catalog, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    //Doris
    COPY_INTO(TargetType.Unknown, SecDataAuthKind.WRITE, SecQueryKind.DML),
    EXPORT(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    SYNC(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    RECOVER(TargetType.Unknown, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // Other
    UNSAFE(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),
    UNKNOWN(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER);

    private final TargetType      target;
    private final SecDataAuthKind authKind;
    private final SecQueryKind    auditKind;
    private final boolean         allowPlan;

    RuleQueryType(TargetType target, SecDataAuthKind authKind, SecQueryKind auditKind){
        this.target = Objects.requireNonNull(target);
        this.authKind = Objects.requireNonNull(authKind);
        this.auditKind = auditKind;
        this.allowPlan = false;
    }

    RuleQueryType(TargetType target, SecDataAuthKind authKind, SecQueryKind auditKind, boolean allowPlan){
        this.target = Objects.requireNonNull(target);
        this.authKind = Objects.requireNonNull(authKind);
        this.auditKind = auditKind;
        this.allowPlan = allowPlan;
    }
}
