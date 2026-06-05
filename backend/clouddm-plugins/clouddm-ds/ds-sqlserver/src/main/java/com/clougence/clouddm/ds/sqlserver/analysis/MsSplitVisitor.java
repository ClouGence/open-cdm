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
package com.clougence.clouddm.ds.sqlserver.analysis;

import static com.clougence.clouddm.sdk.security.auth.SecQueryType.*;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

public class MsSplitVisitor extends SqlServerParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new MsSplitVisitor();

    @Override
    public SecQueryType visitCreate_table(SqlServerParser.Create_tableContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitExecute_statement(SqlServerParser.Execute_statementContext ctx) {
        if (ctx.execute_body().func_proc_name_server_database_schema().getText().equalsIgnoreCase("sp_rename")) {
            return CALL;
        }
        return UNKNOWN;
    }

    @Override
    public SecQueryType visitDrop_table(SqlServerParser.Drop_tableContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitAlter_table(SqlServerParser.Alter_tableContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitCreate_schema(SqlServerParser.Create_schemaContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitDrop_schema(SqlServerParser.Drop_schemaContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitDrop_database(SqlServerParser.Drop_databaseContext ctx) {
        return SecQueryType.DROP_CATALOG;
    }

    @Override
    public SecQueryType visitCreate_database(SqlServerParser.Create_databaseContext ctx) {
        return SecQueryType.CREATE_CATALOG;
    }

    @Override
    public SecQueryType visitDrop_view(SqlServerParser.Drop_viewContext ctx) {
        return SecQueryType.DROP_VIEW;
    }

    @Override
    public SecQueryType visitCreate_index(SqlServerParser.Create_indexContext ctx) {
        return SecQueryType.CREATE_INDEX;
    }

    @Override
    public SecQueryType visitDrop_index(SqlServerParser.Drop_indexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitAlter_index(SqlServerParser.Alter_indexContext ctx) {
        return SecQueryType.ALTER_INDEX;
    }

    @Override
    public SecQueryType visitCreate_view(SqlServerParser.Create_viewContext ctx) {
        ParseTree child1 = ctx.getChild(0);
        if (child1 instanceof TerminalNodeImpl) {
            if (child1.getText().equalsIgnoreCase("create")) {
                return CREATE_VIEW;
            } else if (child1.getText().equalsIgnoreCase("alter")) {
                return SecQueryType.ALTER_VIEW;
            }
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitCreate_or_alter_dml_trigger(SqlServerParser.Create_or_alter_dml_triggerContext ctx) {
        for (ParseTree parseTree : ctx.children) {
            if (parseTree instanceof TerminalNodeImpl) {
                TerminalNodeImpl terminalNodeImpl = (TerminalNodeImpl) parseTree;
                if (terminalNodeImpl.getSymbol().getType() == SqlServerParser.ALTER) {
                    return ALTER_TRIGGER;

                }
            }
        }
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitDelete_statement(SqlServerParser.Delete_statementContext ctx) {
        return DELETE;
    }

    @Override
    public SecQueryType visitInsert_statement(SqlServerParser.Insert_statementContext ctx) {
        return INSERT;
    }

    @Override
    public SecQueryType visitUpdate_statement(SqlServerParser.Update_statementContext ctx) {
        return UPDATE;
    }

    @Override
    public SecQueryType visitCreate_synonym(SqlServerParser.Create_synonymContext ctx) {
        return CREATE_SYNONYM;
    }

    @Override
    public SecQueryType visitCreate_sequence(SqlServerParser.Create_sequenceContext ctx) {
        return CREATE_SEQUENCE;
    }

    @Override
    public SecQueryType visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        if (ctx.getChild(0).getText().equalsIgnoreCase("alter")) {
            return UNKNOWN;
        }
        return CREATE_FUNCTION;
    }

    @Override
    public SecQueryType visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        if (ctx.getChild(0).getText().equalsIgnoreCase("alter")) {
            return UNKNOWN;
        }
        return CREATE_PROCEDURE;
    }

    @Override
    public SecQueryType visitCreate_login_sql_server(SqlServerParser.Create_login_sql_serverContext ctx) {
        return CREATE_USER;
    }

    @Override
    public SecQueryType visitSelect_statement(SqlServerParser.Select_statementContext ctx) {
        return SELECT;
    }

    @Override
    public SecQueryType visitSelect_statement_standalone(SqlServerParser.Select_statement_standaloneContext ctx) {
        return SELECT;
    }

    @Override
    public SecQueryType visitUse_statement(SqlServerParser.Use_statementContext ctx) {
        return SWITCH_CATALOG;
    }
}
