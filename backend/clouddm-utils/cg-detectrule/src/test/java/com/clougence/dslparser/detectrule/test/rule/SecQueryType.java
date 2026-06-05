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
package com.clougence.dslparser.detectrule.test.rule;

import java.util.Objects;

import lombok.Getter;

@Getter
public enum SecQueryType {

    // General
    CREATE_CATALOG(TargetType.Catalog, SecDataAuthKind.DDL),
    CREATE_SCHEMA(TargetType.Schema, SecDataAuthKind.DDL),
    CREATE_TABLE(TargetType.Table, SecDataAuthKind.DDL),
    CREATE_TABLE_ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL),
    CREATE_TABLE_ADD_PRIMARY(TargetType.Constraint, SecDataAuthKind.DDL),
    CREATE_TABLE_ADD_UNIQUE(TargetType.Constraint, SecDataAuthKind.DDL),
    CREATE_TABLE_ADD_INDEX(TargetType.Index, SecDataAuthKind.DDL),
    CREATE_TABLE_SELECT(TargetType.Table, SecDataAuthKind.DDL),
    CREATE_TABLE_LIKE(TargetType.Table, SecDataAuthKind.DDL),
    CREATE_VIEW(TargetType.View, SecDataAuthKind.DDL),

    ALERT_TABLE_ADD_COLUMN(TargetType.Column, SecDataAuthKind.DDL),
    CREATE_INDEX(TargetType.Index, SecDataAuthKind.DDL),
    CREATE_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL),
    CREATE_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL),
    CREATE_FUNCTION(TargetType.Function, SecDataAuthKind.DDL),
    CREATE_PROCEDURE(TargetType.Procedure, SecDataAuthKind.DDL),
    CREATE_TRIGGER(TargetType.Trigger, SecDataAuthKind.DDL),
    CREATE_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL),
    CREATE_EVENT(TargetType.Event, SecDataAuthKind.DDL),

    ALERT_CATALOG(TargetType.Catalog, SecDataAuthKind.DDL),
    ALERT_SCHEMA(TargetType.Schema, SecDataAuthKind.DDL),
    ALERT_TABLE(TargetType.Table, SecDataAuthKind.DDL),
    ALERT_TABLE_ALERT_COLUMN(TargetType.Column, SecDataAuthKind.DDL),
    ALERT_VIEW(TargetType.View, SecDataAuthKind.DDL),
    ALERT_INDEX(TargetType.Index, SecDataAuthKind.DDL),
    ALERT_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL),
    ALERT_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL),
    ALERT_FUNCTION(TargetType.Function, SecDataAuthKind.DDL),
    ALERT_PROCEDURE(TargetType.Procedure, SecDataAuthKind.DDL),
    ALERT_TRIGGER(TargetType.Trigger, SecDataAuthKind.DDL),
    ALERT_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL),
    ALERT_EVENT(TargetType.Event, SecDataAuthKind.DDL),

    DROP_CATALOG(TargetType.Catalog, SecDataAuthKind.DDL),
    DROP_SCHEMA(TargetType.Schema, SecDataAuthKind.DDL),
    DROP_TABLE(TargetType.Table, SecDataAuthKind.DDL),
    DROP_VIEW(TargetType.View, SecDataAuthKind.DDL),
    DROP_COLUMN(TargetType.Column, SecDataAuthKind.DDL),
    DROP_INDEX(TargetType.Index, SecDataAuthKind.DDL),
    DROP_CONSTRAINT(TargetType.Constraint, SecDataAuthKind.DDL),
    DROP_SEQUENCE(TargetType.Sequence, SecDataAuthKind.DDL),
    DROP_FUNCTION(TargetType.Function, SecDataAuthKind.DDL),
    DROP_PROCEDURE(TargetType.Procedure, SecDataAuthKind.DDL),
    DROP_TRIGGER(TargetType.Trigger, SecDataAuthKind.DDL),
    DROP_SYNONYM(TargetType.Synonym, SecDataAuthKind.DDL),
    DROP_EVENT(TargetType.Event, SecDataAuthKind.DDL),

    RENAME_TABLE(TargetType.Table, SecDataAuthKind.DDL),

    SELECT(TargetType.Table, SecDataAuthKind.QUERY),
    WITH(TargetType.Table, SecDataAuthKind.QUERY),

    INSERT(TargetType.Table, SecDataAuthKind.DML),
    INSERT_SELECT(TargetType.Table, SecDataAuthKind.DML),
    UPDATE(TargetType.Table, SecDataAuthKind.DML),
    UPDATE_SET_SELECT(TargetType.Table, SecDataAuthKind.DML),
    DELETE(TargetType.Table, SecDataAuthKind.DML),
    MERGE(TargetType.Table, SecDataAuthKind.DML),
    CALL(TargetType.Procedure, SecDataAuthKind.DML),

    // MySQL specific
    //MYSQL_SHOW(null, ScriptKind.QUERY),
    MYSQL_REPLACE_INTO(TargetType.Table, SecDataAuthKind.DML),

    // Redis specific
    RedisGetCmd(TargetType.Key, SecDataAuthKind.OTHER),
    RedisSetCmd(TargetType.Key, SecDataAuthKind.OTHER),

    // Other
    UNKNOWN(TargetType.Unknown, SecDataAuthKind.OTHER),;

    private final TargetType      target;
    private final SecDataAuthKind kind;

    SecQueryType(TargetType target, SecDataAuthKind kind){
        this.target = Objects.requireNonNull(target);
        this.kind = Objects.requireNonNull(kind);;
    }
}
