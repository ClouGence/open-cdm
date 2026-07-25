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

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Parser;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003ParserBaseVisitor;

/**
 * Maps SQL-2003 parse tree statement nodes to SecQueryType.
 */
public class Sql2003SplitVisitor extends Sql2003ParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new Sql2003SplitVisitor();

    @Override
    public SecQueryType visitSchemaDefinition(Sql2003Parser.SchemaDefinitionContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitDropSchemaStatement(Sql2003Parser.DropSchemaStatementContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitTableDefinition(Sql2003Parser.TableDefinitionContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitAlterTableStatement(Sql2003Parser.AlterTableStatementContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitDropTableStatement(Sql2003Parser.DropTableStatementContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitViewDefinition(Sql2003Parser.ViewDefinitionContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDropViewStatement(Sql2003Parser.DropViewStatementContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitDirectSelectStatement_MultipleRows(Sql2003Parser.DirectSelectStatement_MultipleRowsContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitSelectStatement_SingleRow(Sql2003Parser.SelectStatement_SingleRowContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsertStatement(Sql2003Parser.InsertStatementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdateStatement_Searched(Sql2003Parser.UpdateStatement_SearchedContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitUpdateStatement_Positioned(Sql2003Parser.UpdateStatement_PositionedContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDeleteStatement_Searched(Sql2003Parser.DeleteStatement_SearchedContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDeleteStatement_Positioned(Sql2003Parser.DeleteStatement_PositionedContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCallStatement(Sql2003Parser.CallStatementContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitGrantStatement(Sql2003Parser.GrantStatementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeStatement(Sql2003Parser.RevokeStatementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitDropRoleStatement(Sql2003Parser.DropRoleStatementContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitDropTriggerStatement(Sql2003Parser.DropTriggerStatementContext ctx) {
        return SecQueryType.DROP_TRIGGER;
    }

    @Override
    public SecQueryType visitDropRoutineStatement(Sql2003Parser.DropRoutineStatementContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitAlterSequenceGeneratorStatement(Sql2003Parser.AlterSequenceGeneratorStatementContext ctx) {
        return SecQueryType.ALTER_SEQUENCE;
    }

    @Override
    public SecQueryType visitDropSequenceGeneratorStatement(Sql2003Parser.DropSequenceGeneratorStatementContext ctx) {
        return SecQueryType.DROP_SEQUENCE;
    }

    @Override
    public SecQueryType visitCommitStatement(Sql2003Parser.CommitStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitRollbackStatement(Sql2003Parser.RollbackStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }
}
