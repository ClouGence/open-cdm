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
package com.clougence.sql.iso.sql99.analysis.behavior;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99Parser;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99ParserBaseVisitor;

/**
 * Maps SQL-99 parse tree statement nodes to StatementType.
 */
public class Sql99SplitVisitor extends Sql99ParserBaseVisitor<StatementType> {

    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new Sql99SplitVisitor();

    @Override
    public StatementType visitSchemaDefinition(Sql99Parser.SchemaDefinitionContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitDropSchemaStatement(Sql99Parser.DropSchemaStatementContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitTableDefinition(Sql99Parser.TableDefinitionContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAlterTableStatement(Sql99Parser.AlterTableStatementContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitDropTableStatement(Sql99Parser.DropTableStatementContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitViewDefinition(Sql99Parser.ViewDefinitionContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDropViewStatement(Sql99Parser.DropViewStatementContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitDirectSelectStatement_MultipleRows(Sql99Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitSelectStatement_SingleRow(Sql99Parser.SelectStatement_SingleRowContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsertStatement(Sql99Parser.InsertStatementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdateStatement_Searched(Sql99Parser.UpdateStatement_SearchedContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitUpdateStatement_Positioned(Sql99Parser.UpdateStatement_PositionedContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDeleteStatement_Searched(Sql99Parser.DeleteStatement_SearchedContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDeleteStatement_Positioned(Sql99Parser.DeleteStatement_PositionedContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCallStatement(Sql99Parser.CallStatementContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitGrantStatement(Sql99Parser.GrantStatementContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeStatement(Sql99Parser.RevokeStatementContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitDropRoleStatement(Sql99Parser.DropRoleStatementContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitDropTriggerStatement(Sql99Parser.DropTriggerStatementContext ctx) {
        return StatementType.DROP_TRIGGER;
    }

    @Override
    public StatementType visitDropRoutineStatement(Sql99Parser.DropRoutineStatementContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitCommitStatement(Sql99Parser.CommitStatementContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitRollbackStatement(Sql99Parser.RollbackStatementContext ctx) {
        return StatementType.TRANSACTION;
    }
}
