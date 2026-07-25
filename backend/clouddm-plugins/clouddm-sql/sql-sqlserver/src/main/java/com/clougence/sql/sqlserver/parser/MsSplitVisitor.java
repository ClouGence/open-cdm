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
package com.clougence.sql.sqlserver.parser;

import static com.clougence.clouddm.sdk.sql.parser.SplitQueryType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

public class MsSplitVisitor extends SqlServerParserBaseVisitor<SplitQueryType> {

    public static final AbstractParseTreeVisitor<SplitQueryType> INSTANCE = new MsSplitVisitor();

    @Override
    public SplitQueryType visitCreate_table(SqlServerParser.Create_tableContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreate_security_policy(SqlServerParser.Create_security_policyContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitDrop_security_policy(SqlServerParser.Drop_security_policyContext ctx) {
        return SplitQueryType.DROP_POLICY;
    }

    @Override
    public SplitQueryType visitExecute_statement(SqlServerParser.Execute_statementContext ctx) {
        SplitQueryType renameType = trySpRenameType(ctx);
        if (renameType != null) {
            return renameType;
        }
        return CALL_PROG_OBJ;
    }

    private SplitQueryType trySpRenameType(SqlServerParser.Execute_statementContext ctx) {
        SqlServerParser.Execute_bodyContext body = ctx.execute_body();
        if (body == null || body.func_proc_name_server_database_schema() == null) {
            return null;
        }
        List<String> procNames = names(body.func_proc_name_server_database_schema());
        if (procNames.isEmpty() || !"sp_rename".equalsIgnoreCase(procNames.get(procNames.size() - 1))) {
            return null;
        }
        List<String> args = executeArgs(body);
        if (args.size() < 3) {
            return null;
        }
        String target = stripQuote(args.get(2)).toUpperCase(Locale.ROOT);
        if (target.contains("COLUMN")) {
            return RENAME_COLUMN;
        }
        if (target.contains("OBJECT")) {
            return RENAME_TABLE;
        }
        return null;
    }

    private List<String> names(ParseTree tree) {
        List<String> names = new ArrayList<>();
        collectNames(tree, names);
        return names;
    }

    private void collectNames(ParseTree tree, List<String> names) {
        if (tree instanceof SqlServerParser.Id_Context) {
            names.add(stripQuote(tree.getText()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), names);
        }
    }

    private List<String> executeArgs(SqlServerParser.Execute_bodyContext body) {
        List<String> args = new ArrayList<>();
        collectExecuteArgs(body.execute_statement_arg(), args);
        return args;
    }

    private void collectExecuteArgs(ParseTree tree, List<String> args) {
        if (tree == null) {
            return;
        }
        if (tree instanceof SqlServerParser.Execute_parameterContext parameter) {
            args.add(stripQuote(parameter.getText()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectExecuteArgs(tree.getChild(i), args);
        }
    }

    private String stripQuote(String text) {
        if (text == null) {
            return "";
        }
        String trimmed = text.trim();
        if ((trimmed.startsWith("[") && trimmed.endsWith("]")) || (trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    @Override
    public SplitQueryType visitDrop_table(SqlServerParser.Drop_tableContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitDrop_aggregate(SqlServerParser.Drop_aggregateContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlter_table(SqlServerParser.Alter_tableContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitBlock_statement(SqlServerParser.Block_statementContext ctx) {
        return BLOCK;
    }

    @Override
    public SplitQueryType visitDeclare_statement(SqlServerParser.Declare_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitReturn_statement(SqlServerParser.Return_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitIf_statement(SqlServerParser.If_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitTry_catch_statement(SqlServerParser.Try_catch_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitWhile_statement(SqlServerParser.While_statementContext ctx) {
        return PROGRAM_CONTROL;
    }

    @Override
    public SplitQueryType visitSet_statement(SqlServerParser.Set_statementContext ctx) {
        return ctx.set_special() == null ? SESSION_VARIABLE_RW : SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreate_schema(SqlServerParser.Create_schemaContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitDrop_schema(SqlServerParser.Drop_schemaContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitDrop_database(SqlServerParser.Drop_databaseContext ctx) {
        return SplitQueryType.DROP_CATALOG;
    }

    @Override
    public SplitQueryType visitCreate_database(SqlServerParser.Create_databaseContext ctx) {
        return SplitQueryType.CREATE_CATALOG;
    }

    @Override
    public SplitQueryType visitDrop_view(SqlServerParser.Drop_viewContext ctx) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitCreate_index(SqlServerParser.Create_indexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitDrop_index(SqlServerParser.Drop_indexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitAlter_index(SqlServerParser.Alter_indexContext ctx) {
        return SplitQueryType.ALTER_INDEX;
    }

    @Override
    public SplitQueryType visitCreate_view(SqlServerParser.Create_viewContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_VIEW : CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_dml_trigger(SqlServerParser.Create_or_alter_dml_triggerContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_TRIGGER : CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_ddl_trigger(SqlServerParser.Create_or_alter_ddl_triggerContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_TRIGGER : CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitDelete_statement(SqlServerParser.Delete_statementContext ctx) {
        return DELETE;
    }

    @Override
    public SplitQueryType visitInsert_statement(SqlServerParser.Insert_statementContext ctx) {
        return INSERT;
    }

    @Override
    public SplitQueryType visitUpdate_statement(SqlServerParser.Update_statementContext ctx) {
        return UPDATE;
    }

    @Override
    public SplitQueryType visitCreate_synonym(SqlServerParser.Create_synonymContext ctx) {
        return CREATE_SYNONYM;
    }

    @Override
    public SplitQueryType visitCreate_sequence(SqlServerParser.Create_sequenceContext ctx) {
        return CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_PROG_OBJ : CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        if (hasDirectToken(ctx, SqlServerParser.OR)) {
            return UNKNOWN;
        }
        return startsWith(ctx, "alter") ? ALTER_PROG_OBJ : CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreate_login_sql_server(SqlServerParser.Create_login_sql_serverContext ctx) {
        return CREATE_USER;
    }

    @Override
    public SplitQueryType visitSelect_statement(SqlServerParser.Select_statementContext ctx) {
        return SELECT;
    }

    @Override
    public SplitQueryType visitSelect_statement_standalone(SqlServerParser.Select_statement_standaloneContext ctx) {
        return SELECT;
    }

    @Override
    public SplitQueryType visitUse_statement(SqlServerParser.Use_statementContext ctx) {
        return SWITCH_CATALOG;
    }

    private boolean startsWith(ParserRuleContext ctx, String keyword) {
        return ctx.getChildCount() > 0 && keyword.equalsIgnoreCase(ctx.getChild(0).getText());
    }

    private boolean hasDirectToken(ParserRuleContext ctx, int tokenType) {
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNodeImpl terminal && terminal.getSymbol().getType() == tokenType) {
                return true;
            }
        }
        return false;
    }
}
