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
package com.clougence.sql.iso.sql92.parser;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92ParserBaseVisitor;

/**
 * Maps SQL-92 parse tree statement nodes to SplitQueryType.
 * The SQL-92 grammar uses generic rule names from the BNF, so we
 * detect statement types by examining the first keyword tokens.
 */
public class Sql92SplitVisitor extends Sql92ParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new Sql92SplitVisitor();

    @Override
    public SplitQueryType visitSchemaDefinition(Sql92Parser.SchemaDefinitionContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitDropSchemaStatement(Sql92Parser.DropSchemaStatementContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitTableDefinition(Sql92Parser.TableDefinitionContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlterTableStatement(Sql92Parser.AlterTableStatementContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitDropTableStatement(Sql92Parser.DropTableStatementContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitViewDefinition(Sql92Parser.ViewDefinitionContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDropViewStatement(Sql92Parser.DropViewStatementContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitDirectSelectStatement_MultipleRows(Sql92Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSelectStatement_SingleRow(Sql92Parser.SelectStatement_SingleRowContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsertStatement(Sql92Parser.InsertStatementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdateStatement_Searched(Sql92Parser.UpdateStatement_SearchedContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitUpdateStatement_Positioned(Sql92Parser.UpdateStatement_PositionedContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDeleteStatement_Searched(Sql92Parser.DeleteStatement_SearchedContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDeleteStatement_Positioned(Sql92Parser.DeleteStatement_PositionedContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitGrantStatement(Sql92Parser.GrantStatementContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeStatement(Sql92Parser.RevokeStatementContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCommitStatement(Sql92Parser.CommitStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitRollbackStatement(Sql92Parser.RollbackStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }
}
