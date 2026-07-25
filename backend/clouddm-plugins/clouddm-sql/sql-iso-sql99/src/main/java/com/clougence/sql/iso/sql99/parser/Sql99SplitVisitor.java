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
package com.clougence.sql.iso.sql99.parser;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99Parser;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99ParserBaseVisitor;

/**
 * Maps SQL-99 parse tree statement nodes to SecQueryType.
 */
public class Sql99SplitVisitor extends Sql99ParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new Sql99SplitVisitor();

    @Override
    public SecQueryType visitSchemaDefinition(Sql99Parser.SchemaDefinitionContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitDropSchemaStatement(Sql99Parser.DropSchemaStatementContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitTableDefinition(Sql99Parser.TableDefinitionContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitAlterTableStatement(Sql99Parser.AlterTableStatementContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitDropTableStatement(Sql99Parser.DropTableStatementContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitViewDefinition(Sql99Parser.ViewDefinitionContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDropViewStatement(Sql99Parser.DropViewStatementContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitDirectSelectStatement_MultipleRows(Sql99Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitSelectStatement_SingleRow(Sql99Parser.SelectStatement_SingleRowContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsertStatement(Sql99Parser.InsertStatementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdateStatement_Searched(Sql99Parser.UpdateStatement_SearchedContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitUpdateStatement_Positioned(Sql99Parser.UpdateStatement_PositionedContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDeleteStatement_Searched(Sql99Parser.DeleteStatement_SearchedContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDeleteStatement_Positioned(Sql99Parser.DeleteStatement_PositionedContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCallStatement(Sql99Parser.CallStatementContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitGrantStatement(Sql99Parser.GrantStatementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeStatement(Sql99Parser.RevokeStatementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitDropRoleStatement(Sql99Parser.DropRoleStatementContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitDropTriggerStatement(Sql99Parser.DropTriggerStatementContext ctx) {
        return SecQueryType.DROP_TRIGGER;
    }

    @Override
    public SecQueryType visitDropRoutineStatement(Sql99Parser.DropRoutineStatementContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitCommitStatement(Sql99Parser.CommitStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitRollbackStatement(Sql99Parser.RollbackStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }
}
