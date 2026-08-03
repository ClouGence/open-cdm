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

import java.util.*;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;

public class MsSqlSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return MsSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return MsSplitVisitor.INSTANCE;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        if (findContext(context, SqlServerParser.Create_viewContext.class) != null || findContext(context, SqlServerParser.Create_or_alter_triggerContext.class) != null
            || findContext(context, SqlServerParser.Create_or_alter_procedureContext.class) != null
            || findContext(context, SqlServerParser.Create_or_alter_functionContext.class) != null) {
            return Set.of(normalizeType(context.accept(splitVisitor()), script));
        }
        return super.collectTypes(context, script);
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof SqlServerParser.Insert_statementContext insert && containsContext(insert.insert_statement_value(), SqlServerParser.Select_statementContext.class)) {
            return SplitQueryType.SELECT;
        }
        if (!(tree instanceof ParserRuleContext context)) {
            return null;
        }
        SqlServerParser.Alter_tableContext alterTable = ancestor(context, SqlServerParser.Alter_tableContext.class);
        if (alterTable == null) {
            return null;
        }
        if (context instanceof SqlServerParser.Column_definitionContext || context instanceof SqlServerParser.Materialized_column_definitionContext) {
            return directToken(alterTable, SqlServerParser.ADD) ? SplitQueryType.ADD_COLUMN : null;
        }
        if (context instanceof SqlServerParser.Table_constraintContext) {
            return SplitQueryType.ADD_CONSTRAINT;
        }
        if (context instanceof SqlServerParser.Column_modifierContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (context == alterTable) {
            if (directToken(alterTable, SqlServerParser.DROP)) {
                return alterTable.CONSTRAINT() == null ? SplitQueryType.DROP_COLUMN : SplitQueryType.DROP_CONSTRAINT;
            }
            if (alterTable.column_definition() != null || alterTable.column_modifier() != null) {
                return SplitQueryType.ALTER_COLUMN;
            }
            if (directToken(alterTable, SqlServerParser.CHECK) || directToken(alterTable, SqlServerParser.NOCHECK)) {
                return directToken(alterTable, SqlServerParser.ADD) ? SplitQueryType.ADD_CONSTRAINT : SplitQueryType.ALTER_CONSTRAINT;
            }
            if (directToken(alterTable, SqlServerParser.ENABLE) || directToken(alterTable, SqlServerParser.DISABLE)) {
                return SplitQueryType.ALTER_TRIGGER;
            }
            if (directToken(alterTable, SqlServerParser.REBUILD)) {
                return SplitQueryType.ADMIN_TABLE;
            }
            if (alterTable.switch_partition() != null) {
                return SplitQueryType.ALTER_PARTITION;
            }
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        SqlServerParser.Create_viewContext view = findContext(context, SqlServerParser.Create_viewContext.class);
        if (view != null) {
            return List.of(statementNode(view.select_statement_standalone(), tokens));
        }

        SqlServerParser.Create_or_alter_dml_triggerContext dmlTrigger = findContext(context, SqlServerParser.Create_or_alter_dml_triggerContext.class);
        if (dmlTrigger != null) {
            return statementNodes(List.of(dmlTrigger.sql_clauses()), tokens);
        }
        SqlServerParser.Create_or_alter_ddl_triggerContext ddlTrigger = findContext(context, SqlServerParser.Create_or_alter_ddl_triggerContext.class);
        if (ddlTrigger != null) {
            return statementNodes(ddlTrigger.sql_clauses(), tokens);
        }
        SqlServerParser.Create_or_alter_procedureContext procedure = findContext(context, SqlServerParser.Create_or_alter_procedureContext.class);
        if (procedure != null) {
            return statementNodes(procedure.sql_clauses(), tokens);
        }
        SqlServerParser.Create_or_alter_functionContext function = findContext(context, SqlServerParser.Create_or_alter_functionContext.class);
        return function == null ? Collections.emptyList() : functionChildren(function, tokens);
    }

    private List<SplitScript> functionChildren(SqlServerParser.Create_or_alter_functionContext function, CommonTokenStream tokens) {
        if (function.func_body_returns_select() != null) {
            SqlServerParser.Func_body_returns_selectContext body = function.func_body_returns_select();
            SqlServerParser.Select_statement_standaloneContext query = body.select_statement_standalone();
            TerminalNode returnToken = directTerminal(body, SqlServerParser.RETURN);
            if (query == null || returnToken == null) {
                return Collections.emptyList();
            }
            SplitScript select = statementNode(query, tokens);
            return List.of(createRangeNode(returnToken.getSymbol(), query.getStop(), tokens, Set.of(SplitQueryType.PROGRAM_CONTROL), List.of(select)));
        }

        ParserRuleContext body = function.func_body_returns_table() != null ? function.func_body_returns_table() : function.func_body_returns_scalar();
        TerminalNode begin = directTerminal(body, SqlServerParser.BEGIN);
        TerminalNode end = directTerminal(body, SqlServerParser.END);
        if (begin == null || end == null) {
            return Collections.emptyList();
        }

        List<SplitScript> children = new ArrayList<>();
        List<SqlServerParser.Sql_clausesContext> clauses = function.func_body_returns_table() != null ? function.func_body_returns_table()
            .sql_clauses() : function.func_body_returns_scalar().sql_clauses();
        children.addAll(statementNodes(clauses, tokens));

        TerminalNode returnToken = directTerminal(body, SqlServerParser.RETURN);
        if (returnToken != null) {
            Token stop = body instanceof SqlServerParser.Func_body_returns_scalarContext scalar ? scalar.ret.getStop() : returnToken.getSymbol();
            children.add(createRangeNode(returnToken.getSymbol(), stop, tokens, Set.of(SplitQueryType.PROGRAM_CONTROL), Collections.emptyList()));
        }
        return List.of(createRangeNode(begin.getSymbol(), end.getSymbol(), tokens, Set.of(SplitQueryType.BLOCK), children));
    }

    private List<SplitScript> statementNodes(List<SqlServerParser.Sql_clausesContext> clauses, CommonTokenStream tokens) {
        List<SplitScript> result = new ArrayList<>();
        for (SqlServerParser.Sql_clausesContext clause : clauses) {
            result.add(statementNode(clause, tokens));
        }
        return List.copyOf(result);
    }

    private SplitScript statementNode(ParserRuleContext context, CommonTokenStream tokens) {
        ParserRuleContext statement = unwrap(context);
        Set<SplitQueryType> types;
        List<SplitScript> children;
        if (statement instanceof SqlServerParser.Block_statementContext block) {
            types = Set.of(SplitQueryType.BLOCK);
            children = statementNodes(block.sql_clauses(), tokens);
        } else if (statement instanceof SqlServerParser.Declare_statementContext || statement instanceof SqlServerParser.Return_statementContext || isProgramControl(statement)
                   || statement instanceof SqlServerParser.Set_statementContext set && set.set_special() == null) {
            types = Set.of(SplitQueryType.PROGRAM_CONTROL);
            children = controlChildren(statement, tokens);
        } else {
            types = new LinkedHashSet<>(collectTypes(statement, tokens.getText(statement.getStart(), statement.getStop())));
            children = Collections.emptyList();
        }
        return createChild(context, tokens, types, children);
    }

    private List<SplitScript> controlChildren(ParserRuleContext context, CommonTokenStream tokens) {
        List<SqlServerParser.Sql_clausesContext> clauses = new ArrayList<>();
        collectDirectClauses(context, context, clauses);
        return statementNodes(clauses, tokens);
    }

    private void collectDirectClauses(ParseTree tree, ParserRuleContext owner, List<SqlServerParser.Sql_clausesContext> result) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof SqlServerParser.Sql_clausesContext clause) {
                result.add(clause);
            } else if (child instanceof ParserRuleContext context && !isProgramBoundary(context, owner)) {
                collectDirectClauses(context, owner, result);
            }
        }
    }

    private boolean isProgramBoundary(ParserRuleContext context, ParserRuleContext owner) {
        return context != owner && (context instanceof SqlServerParser.Block_statementContext || isProgramControl(context));
    }

    private boolean isProgramControl(ParserRuleContext context) {
        return context instanceof SqlServerParser.If_statementContext || context instanceof SqlServerParser.Try_catch_statementContext
               || context instanceof SqlServerParser.While_statementContext || context instanceof SqlServerParser.Break_statementContext
               || context instanceof SqlServerParser.Continue_statementContext || context instanceof SqlServerParser.Goto_statementContext
               || context instanceof SqlServerParser.Raiseerror_statementContext || context instanceof SqlServerParser.Throw_statementContext
               || context instanceof SqlServerParser.Waitfor_statementContext;
    }

    private SplitScript createRangeNode(Token start, Token stop, CommonTokenStream tokens, Set<SplitQueryType> types, List<SplitScript> children) {
        ParserRuleContext range = new ParserRuleContext();
        range.start = start;
        range.stop = stop;
        return createChild(range, tokens, types, children);
    }

    private ParserRuleContext unwrap(ParserRuleContext context) {
        ParserRuleContext current = context;
        while (current instanceof SqlServerParser.Sql_clausesContext || current instanceof SqlServerParser.Dml_clauseContext
               || current instanceof SqlServerParser.Cfl_statementContext || current instanceof SqlServerParser.Another_statementContext
               || current instanceof SqlServerParser.Ddl_clauseContext || current instanceof SqlServerParser.Batch_level_statementContext) {
            current = firstRuleChild(current);
        }
        return current;
    }

    private ParserRuleContext firstRuleChild(ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (tree.getChild(i) instanceof ParserRuleContext context) {
                return context;
            }
        }
        throw new IllegalArgumentException("SQL Server statement has no rule child: " + tree.getClass().getSimpleName());
    }

    private boolean directToken(ParserRuleContext context, int tokenType) {
        return directTerminal(context, tokenType) != null;
    }

    private TerminalNode directTerminal(ParserRuleContext context, int tokenType) {
        for (int i = 0; i < context.getChildCount(); i++) {
            if (context.getChild(i) instanceof TerminalNode terminal && terminal.getSymbol().getType() == tokenType) {
                return terminal;
            }
        }
        return null;
    }

    private <T extends ParserRuleContext> T ancestor(ParserRuleContext context, Class<T> type) {
        ParserRuleContext current = context;
        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getParent();
        }
        return null;
    }

    private boolean containsContext(ParseTree tree, Class<? extends ParserRuleContext> type) {
        return findContext(tree, type) != null;
    }

    private <T extends ParserRuleContext> T findContext(ParseTree tree, Class<T> type) {
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T context = findContext(tree.getChild(i), type);
            if (context != null) {
                return context;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((SqlServerParser) parser).tsql_file();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof SqlServerParser.Sql_clausesContext && context.getParent() instanceof SqlServerParser.Tsql_fileContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new MsSqlStatementParser();
    }

}
