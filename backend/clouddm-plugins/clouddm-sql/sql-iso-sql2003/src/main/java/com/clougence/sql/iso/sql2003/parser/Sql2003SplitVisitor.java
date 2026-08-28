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
package com.clougence.sql.iso.sql2003.parser;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003ParserBaseVisitor;

/**
 * Maps SQL-2003 parse tree statement nodes to SplitQueryType.
 */
public class Sql2003SplitVisitor extends Sql2003ParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new Sql2003SplitVisitor();

    @Override
    public SplitQueryType visitSchemaDefinition(Sql2003Parser.SchemaDefinitionContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitDropSchemaStatement(Sql2003Parser.DropSchemaStatementContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitTableDefinition(Sql2003Parser.TableDefinitionContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlterTableStatement(Sql2003Parser.AlterTableStatementContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitDropTableStatement(Sql2003Parser.DropTableStatementContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitViewDefinition(Sql2003Parser.ViewDefinitionContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDropViewStatement(Sql2003Parser.DropViewStatementContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitDirectSelectStatement_MultipleRows(Sql2003Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitExplainPlanStatement(Sql2003Parser.ExplainPlanStatementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSelectStatement_SingleRow(Sql2003Parser.SelectStatement_SingleRowContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsertStatement(Sql2003Parser.InsertStatementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdateStatement_Searched(Sql2003Parser.UpdateStatement_SearchedContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitUpdateStatement_Positioned(Sql2003Parser.UpdateStatement_PositionedContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDeleteStatement_Searched(Sql2003Parser.DeleteStatement_SearchedContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDeleteStatement_Positioned(Sql2003Parser.DeleteStatement_PositionedContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCallStatement(Sql2003Parser.CallStatementContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitGrantStatement(Sql2003Parser.GrantStatementContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeStatement(Sql2003Parser.RevokeStatementContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitDropRoleStatement(Sql2003Parser.DropRoleStatementContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitDropTriggerStatement(Sql2003Parser.DropTriggerStatementContext ctx) {
        return SplitQueryType.DROP_TRIGGER;
    }

    @Override
    public SplitQueryType visitDropRoutineStatement(Sql2003Parser.DropRoutineStatementContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlterSequenceGeneratorStatement(Sql2003Parser.AlterSequenceGeneratorStatementContext ctx) {
        return SplitQueryType.ALTER_SEQUENCE;
    }

    @Override
    public SplitQueryType visitDropSequenceGeneratorStatement(Sql2003Parser.DropSequenceGeneratorStatementContext ctx) {
        return SplitQueryType.DROP_SEQUENCE;
    }

    @Override
    public SplitQueryType visitCommitStatement(Sql2003Parser.CommitStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitRollbackStatement(Sql2003Parser.RollbackStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }
}
