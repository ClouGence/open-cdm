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

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.sqlserver.analysis.builder.MsBuildFactory;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

public class MsSqlParserVisitor extends SqlServerParserBaseVisitor<Void> {

    private final MsBuildFactory builder;
    private final Parser         parser;

    public MsSqlParserVisitor(MsBuildFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private void visitIfExist(ParserRuleContext ctx) {
        if (ctx != null) {
            ctx.accept(this);
        }
    }

    @Override
    public Void visitSql_clauses(SqlServerParser.Sql_clausesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDdl_clause(SqlServerParser.Ddl_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        if (ctx.getChild(0).getText().equalsIgnoreCase("alter")) {
            throw new UnsupportedOperationException();
        }
        builder.enterCreateFunction();
        builder.enterObjName();
        visitIfExist(ctx.func_proc_name_schema().schema);
        visitIfExist(ctx.func_proc_name_schema().procedure);
        builder.exitObjName();
        builder.exitCreateFunction();
        return null;
    }

    @Override
    public Void visitCreate_login_sql_server(SqlServerParser.Create_login_sql_serverContext ctx) {
        builder.enterCreateUser();
        builder.enterObjName();
        ctx.login_name.accept(this);
        builder.exitObjName();
        if (ctx.password != null) {
            builder.addAttr(CommonAttribute.PASSWORD, ctx.password.getText());
        } else {
            builder.addAttr(CommonAttribute.PASSWORD, ctx.password_hash.getText());
        }
        builder.exitCreateUser();
        return null;
    }

    @Override
    public Void visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        if (ctx.getChild(0).getText().equalsIgnoreCase("alter")) {
            throw new UnsupportedOperationException();
        }
        builder.enterCreateProcedure();
        builder.enterObjName();
        visitIfExist(ctx.func_proc_name_schema().schema);
        visitIfExist(ctx.func_proc_name_schema().procedure);
        builder.exitObjName();
        builder.enterExitProcedure();
        return null;
    }

    @Override
    public Void visitId_(SqlServerParser.Id_Context ctx) {
        builder.addAttr(CommonAttribute.VALUE, getText(ctx));
        return null;
    }

    @Override
    public Void visitCreate_synonym(SqlServerParser.Create_synonymContext ctx) {
        builder.enterCreateSynonym();
        builder.handleObjName(() -> {
            visitIfExist(ctx.schema_name_1);
            ctx.synonym_name.accept(this);
        });
        builder.exitCreateSynonym();
        return null;
    }

    @Override
    public Void visitCreate_sequence(SqlServerParser.Create_sequenceContext ctx) {
        builder.enterCreateSequence();
        builder.enterObjName();
        visitIfExist(ctx.schema_name);
        ctx.sequence_name.accept(this);
        builder.exitObjName();
        builder.exitCreateSequence();
        return null;
    }

    @Override
    public Void visitBatch_level_statement(SqlServerParser.Batch_level_statementContext ctx) {
        return null;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof ParserRuleContext) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText((ParserRuleContext) node));
        }
        throw new UnsupportedOperationException("unsupported SQL: " + node.getText());
    }

    public void dmVisitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            c.accept(this);
        }
    }
}
