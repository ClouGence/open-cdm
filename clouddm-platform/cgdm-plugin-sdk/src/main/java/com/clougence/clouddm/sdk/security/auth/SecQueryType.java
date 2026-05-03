package com.clougence.clouddm.sdk.security.auth;

import java.util.Objects;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import lombok.Getter;

@Getter
public enum SecQueryType {

    // DDL catalog
    CREATE_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.CREATE),
    ALTER_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    DROP_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.DROP),
    RENAME_CATALOG(TargetType.Catalog, SecDataAuthKind.SPACE, SecQueryKind.ALTER),

    // DDL schema
    CREATE_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.CREATE),
    ALTER_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.ALTER),
    DROP_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.DROP),
    RENAME_SCHEMA(TargetType.Schema, SecDataAuthKind.SPACE, SecQueryKind.ALTER),

    // DDL table
    CREATE_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    CREATE_TABLE_SELECT(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    CREATE_TABLE_LIKE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_RENAME(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.DROP),
    RENAME_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_TABLE(TargetType.Table, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL column
    CREATE_TABLE_ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE_ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_ALTER_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_DROP_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_RENAME_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    COMMENT_COLUMN(TargetType.Column, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL constraint pk/uk/fk
    CREATE_TABLE_ADD_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE_ADD_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    ALTER_TABLE_DROP_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL, SecQueryKind.ALTER),

    // DDL index
    CREATE_TABLE_ADD_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_TABLE_ADD_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    CREATE_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.CREATE),
    ALTER_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.ALTER),
    DROP_INDEX(TargetType.Index, SecDataAuthKind.DDL, SecQueryKind.DROP),

    // DDL view
    CREATE_VIEW(TargetType.View, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_VIEW(TargetType.View, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_VIEW(TargetType.View, SecDataAuthKind.OBJECT, SecQueryKind.DROP),
    CREATE_MATERIALIZED_VIEW(TargetType.Materialized, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),

    // another object
    CREATE_OBJECT(TargetType.Unknown, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_OBJECT(TargetType.Unknown, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_OBJECT(TargetType.Unknown, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL sequence
    CREATE_SEQUENCE(TargetType.Sequence, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_SEQUENCE(TargetType.Sequence, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_SEQUENCE(TargetType.Sequence, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL function
    CREATE_FUNCTION(TargetType.Function, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_FUNCTION(TargetType.Function, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_FUNCTION(TargetType.Function, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL procedure
    CREATE_PROCEDURE(TargetType.Procedure, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_PROCEDURE(TargetType.Procedure, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_PROCEDURE(TargetType.Procedure, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL trigger
    CREATE_TRIGGER(TargetType.Trigger, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_TRIGGER(TargetType.Trigger, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_TRIGGER(TargetType.Trigger, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL synonym
    CREATE_SYNONYM(TargetType.Synonym, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_SYNONYM(TargetType.Synonym, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_SYNONYM(TargetType.Synonym, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DDL event
    CREATE_EVENT(TargetType.Event, SecDataAuthKind.OBJECT, SecQueryKind.CREATE),
    ALTER_EVENT(TargetType.Event, SecDataAuthKind.OBJECT, SecQueryKind.ALTER),
    DROP_EVENT(TargetType.Event, SecDataAuthKind.OBJECT, SecQueryKind.DROP),

    // DCL
    CREATE_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    DROP_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    RENAME_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    GRANT(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    REVOKE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    CREATE_ROLE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
    DROP_ROLE(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.DROP),
    ALTER_USER(TargetType.UserOrRole, SecDataAuthKind.ADMIN, SecQueryKind.ALTER),
    CONFIG_WRITE(TargetType.ConfigKey, SecDataAuthKind.ADMIN, SecQueryKind.OTHER),

    // switch ctx
    SWITCH_CATALOG(TargetType.Catalog, SecDataAuthKind.READ, SecQueryKind.QUERY),
    SWITCH_SCHEMA(TargetType.Schema, SecDataAuthKind.READ, SecQueryKind.QUERY),

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
    CREATE_UDF_FUNCTION(TargetType.Function, SecDataAuthKind.ADMIN, SecQueryKind.CREATE),
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
    UNKNOWN(TargetType.Unknown, SecDataAuthKind.OTHER, SecQueryKind.OTHER),;

    private final TargetType      target;
    private final SecDataAuthKind authKind;
    private final SecQueryKind    auditKind;
    private final boolean         allowPlan;

    SecQueryType(TargetType target, SecDataAuthKind authKind, SecQueryKind auditKind){
        this.target = Objects.requireNonNull(target);
        this.authKind = Objects.requireNonNull(authKind);
        this.auditKind = auditKind;
        this.allowPlan = false;
    }

    SecQueryType(TargetType target, SecDataAuthKind authKind, SecQueryKind auditKind, boolean allowPlan){
        this.target = Objects.requireNonNull(target);
        this.authKind = Objects.requireNonNull(authKind);
        this.auditKind = auditKind;
        this.allowPlan = allowPlan;
    }
}
