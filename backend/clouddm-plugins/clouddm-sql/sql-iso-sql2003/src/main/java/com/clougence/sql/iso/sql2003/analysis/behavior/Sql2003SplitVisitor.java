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
package com.clougence.sql.iso.sql2003.analysis.behavior;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003ParserBaseVisitor;

/**
 * Maps SQL-2003 parse tree statement nodes to StatementType.
 */
public class Sql2003SplitVisitor extends Sql2003ParserBaseVisitor<StatementType> {

    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new Sql2003SplitVisitor();

    @Override
    public StatementType visitSchemaDefinition(Sql2003Parser.SchemaDefinitionContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitDropSchemaStatement(Sql2003Parser.DropSchemaStatementContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitTableDefinition(Sql2003Parser.TableDefinitionContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAlterTableStatement(Sql2003Parser.AlterTableStatementContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitDropTableStatement(Sql2003Parser.DropTableStatementContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitViewDefinition(Sql2003Parser.ViewDefinitionContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDropViewStatement(Sql2003Parser.DropViewStatementContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitDirectSelectStatement_MultipleRows(Sql2003Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitSelectStatement_SingleRow(Sql2003Parser.SelectStatement_SingleRowContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsertStatement(Sql2003Parser.InsertStatementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdateStatement_Searched(Sql2003Parser.UpdateStatement_SearchedContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitUpdateStatement_Positioned(Sql2003Parser.UpdateStatement_PositionedContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDeleteStatement_Searched(Sql2003Parser.DeleteStatement_SearchedContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDeleteStatement_Positioned(Sql2003Parser.DeleteStatement_PositionedContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCallStatement(Sql2003Parser.CallStatementContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitGrantStatement(Sql2003Parser.GrantStatementContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeStatement(Sql2003Parser.RevokeStatementContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitDropRoleStatement(Sql2003Parser.DropRoleStatementContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitDropTriggerStatement(Sql2003Parser.DropTriggerStatementContext ctx) {
        return StatementType.DROP_TRIGGER;
    }

    @Override
    public StatementType visitDropRoutineStatement(Sql2003Parser.DropRoutineStatementContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitAlterSequenceGeneratorStatement(Sql2003Parser.AlterSequenceGeneratorStatementContext ctx) {
        return StatementType.ALTER_SEQUENCE;
    }

    @Override
    public StatementType visitDropSequenceGeneratorStatement(Sql2003Parser.DropSequenceGeneratorStatementContext ctx) {
        return StatementType.DROP_SEQUENCE;
    }

    @Override
    public StatementType visitCommitStatement(Sql2003Parser.CommitStatementContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitRollbackStatement(Sql2003Parser.RollbackStatementContext ctx) {
        return StatementType.TRANSACTION;
    }
}
