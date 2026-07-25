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

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92Parser;
import com.clougence.sql.iso.sql92.parser.antlr.Sql92ParserBaseVisitor;

/**
 * Maps SQL-92 parse tree statement nodes to SecQueryType.
 * The SQL-92 grammar uses generic rule names from the BNF, so we
 * detect statement types by examining the first keyword tokens.
 */
public class Sql92SplitVisitor extends Sql92ParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new Sql92SplitVisitor();

    @Override
    public SecQueryType visitSchemaDefinition(Sql92Parser.SchemaDefinitionContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitDropSchemaStatement(Sql92Parser.DropSchemaStatementContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitTableDefinition(Sql92Parser.TableDefinitionContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitAlterTableStatement(Sql92Parser.AlterTableStatementContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitDropTableStatement(Sql92Parser.DropTableStatementContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitViewDefinition(Sql92Parser.ViewDefinitionContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDropViewStatement(Sql92Parser.DropViewStatementContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitDirectSelectStatement_MultipleRows(Sql92Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitSelectStatement_SingleRow(Sql92Parser.SelectStatement_SingleRowContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsertStatement(Sql92Parser.InsertStatementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdateStatement_Searched(Sql92Parser.UpdateStatement_SearchedContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitUpdateStatement_Positioned(Sql92Parser.UpdateStatement_PositionedContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDeleteStatement_Searched(Sql92Parser.DeleteStatement_SearchedContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDeleteStatement_Positioned(Sql92Parser.DeleteStatement_PositionedContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitGrantStatement(Sql92Parser.GrantStatementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeStatement(Sql92Parser.RevokeStatementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCommitStatement(Sql92Parser.CommitStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitRollbackStatement(Sql92Parser.RollbackStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }
}
