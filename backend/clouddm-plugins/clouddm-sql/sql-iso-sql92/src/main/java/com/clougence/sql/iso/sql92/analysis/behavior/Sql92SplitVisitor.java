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
package com.clougence.sql.iso.sql92.analysis.behavior;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92ParserBaseVisitor;

/**
 * Maps SQL-92 parse tree statement nodes to StatementType.
 * The SQL-92 grammar uses generic rule names from the BNF, so we
 * detect statement types by examining the first keyword tokens.
 */
public class Sql92SplitVisitor extends Sql92ParserBaseVisitor<StatementType> {

    public static final AbstractParseTreeVisitor<StatementType> INSTANCE = new Sql92SplitVisitor();

    @Override
    public StatementType visitSchemaDefinition(Sql92Parser.SchemaDefinitionContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitDropSchemaStatement(Sql92Parser.DropSchemaStatementContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitTableDefinition(Sql92Parser.TableDefinitionContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAlterTableStatement(Sql92Parser.AlterTableStatementContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitDropTableStatement(Sql92Parser.DropTableStatementContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitViewDefinition(Sql92Parser.ViewDefinitionContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDropViewStatement(Sql92Parser.DropViewStatementContext ctx) {
        return StatementType.DROP_VIEW;
    }

    @Override
    public StatementType visitDirectSelectStatement_MultipleRows(Sql92Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitSelectStatement_SingleRow(Sql92Parser.SelectStatement_SingleRowContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsertStatement(Sql92Parser.InsertStatementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdateStatement_Searched(Sql92Parser.UpdateStatement_SearchedContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitUpdateStatement_Positioned(Sql92Parser.UpdateStatement_PositionedContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDeleteStatement_Searched(Sql92Parser.DeleteStatement_SearchedContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDeleteStatement_Positioned(Sql92Parser.DeleteStatement_PositionedContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitGrantStatement(Sql92Parser.GrantStatementContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeStatement(Sql92Parser.RevokeStatementContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitCommitStatement(Sql92Parser.CommitStatementContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitRollbackStatement(Sql92Parser.RollbackStatementContext ctx) {
        return StatementType.TRANSACTION;
    }
}
