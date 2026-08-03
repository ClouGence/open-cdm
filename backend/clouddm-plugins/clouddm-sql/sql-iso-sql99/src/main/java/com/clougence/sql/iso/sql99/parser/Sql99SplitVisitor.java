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

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99Parser;
import com.clougence.sql.iso.sql99.parser.antlr.Sql99ParserBaseVisitor;

/**
 * Maps SQL-99 parse tree statement nodes to SplitQueryType.
 */
public class Sql99SplitVisitor extends Sql99ParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new Sql99SplitVisitor();

    @Override
    public SplitQueryType visitSchemaDefinition(Sql99Parser.SchemaDefinitionContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitDropSchemaStatement(Sql99Parser.DropSchemaStatementContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitTableDefinition(Sql99Parser.TableDefinitionContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlterTableStatement(Sql99Parser.AlterTableStatementContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitDropTableStatement(Sql99Parser.DropTableStatementContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitViewDefinition(Sql99Parser.ViewDefinitionContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDropViewStatement(Sql99Parser.DropViewStatementContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitDirectSelectStatement_MultipleRows(Sql99Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSelectStatement_SingleRow(Sql99Parser.SelectStatement_SingleRowContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsertStatement(Sql99Parser.InsertStatementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdateStatement_Searched(Sql99Parser.UpdateStatement_SearchedContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitUpdateStatement_Positioned(Sql99Parser.UpdateStatement_PositionedContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDeleteStatement_Searched(Sql99Parser.DeleteStatement_SearchedContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDeleteStatement_Positioned(Sql99Parser.DeleteStatement_PositionedContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCallStatement(Sql99Parser.CallStatementContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitGrantStatement(Sql99Parser.GrantStatementContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeStatement(Sql99Parser.RevokeStatementContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitDropRoleStatement(Sql99Parser.DropRoleStatementContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitDropTriggerStatement(Sql99Parser.DropTriggerStatementContext ctx) {
        return SplitQueryType.DROP_TRIGGER;
    }

    @Override
    public SplitQueryType visitDropRoutineStatement(Sql99Parser.DropRoutineStatementContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCommitStatement(Sql99Parser.CommitStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitRollbackStatement(Sql99Parser.RollbackStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }
}
