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
package com.clougence.sql.oracle.parser;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser;

public class OraSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    protected DslProvider dslProvider() {
        return OraDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return OraSplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if ((tree instanceof PlSqlParser.Select_statementContext || tree instanceof PlSqlParser.Select_only_statementContext || tree instanceof PlSqlParser.SubqueryContext)
            && hasDmlOwner(tree)) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof PlSqlParser.Add_column_clauseContext) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (tree instanceof PlSqlParser.Modify_column_clausesContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (tree instanceof PlSqlParser.Drop_column_clauseContext) {
            return SplitQueryType.DROP_COLUMN;
        }
        if (tree instanceof PlSqlParser.Rename_column_clauseContext) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (tree instanceof PlSqlParser.Add_table_constarintContext) {
            return SplitQueryType.ADD_CONSTRAINT;
        }
        if (tree instanceof PlSqlParser.Modify_table_constarintContext || tree instanceof PlSqlParser.Rename_table_constarintContext) {
            return SplitQueryType.ALTER_CONSTRAINT;
        }
        if (tree instanceof PlSqlParser.Drop_table_constarintContext) {
            return SplitQueryType.DROP_CONSTRAINT;
        }
        if (tree instanceof PlSqlParser.Add_table_partitionContext) {
            return SplitQueryType.ADD_PARTITION;
        }
        if (tree instanceof PlSqlParser.Drop_table_partitionContext) {
            return SplitQueryType.DROP_PARTITION;
        }
        if (tree instanceof PlSqlParser.Truncate_table_partitionContext) {
            return SplitQueryType.TRUNCATE_PARTITION;
        }
        if (tree instanceof PlSqlParser.Merge_table_partitionContext || tree instanceof PlSqlParser.Modify_table_partitionContext
            || tree instanceof PlSqlParser.Split_table_partitionContext || tree instanceof PlSqlParser.Exchange_table_partitionContext
            || tree instanceof PlSqlParser.Coalesce_table_partitionContext || tree instanceof PlSqlParser.Alter_interval_partitionContext) {
            return SplitQueryType.ALTER_PARTITION;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, org.antlr.v4.runtime.CommonTokenStream tokens) {
        PlSqlParser.Select_only_statementContext query = viewQuery(context);
        if (query != null) {
            return List.of(createChild(query, tokens, Set.of(SplitQueryType.SELECT), Collections.emptyList()));
        }

        ParseTree owner = programOwner(context);
        if (owner == null) {
            return Collections.emptyList();
        }
        return programChildren(owner, tokens);
    }

    private boolean hasDmlOwner(ParseTree tree) {
        ParseTree parent = tree.getParent();
        boolean dmlOwner = false;
        while (parent != null) {
            if (parent instanceof PlSqlParser.Insert_statementContext || parent instanceof PlSqlParser.Update_statementContext
                || parent instanceof PlSqlParser.Delete_statementContext || parent instanceof PlSqlParser.Merge_statementContext) {
                dmlOwner = true;
            }
            if (parent instanceof PlSqlParser.Create_viewContext || parent instanceof PlSqlParser.Create_materialized_viewContext
                || parent instanceof PlSqlParser.Create_tableContext || parent instanceof PlSqlParser.Create_function_bodyContext
                || parent instanceof PlSqlParser.Create_procedure_bodyContext || parent instanceof PlSqlParser.Create_triggerContext) {
                return false;
            }
            parent = parent.getParent();
        }
        return dmlOwner;
    }

    private PlSqlParser.Select_only_statementContext viewQuery(ParseTree tree) {
        if (tree instanceof PlSqlParser.Create_viewContext ctx) {
            return ctx.select_only_statement();
        }
        if (tree instanceof PlSqlParser.Create_materialized_viewContext ctx) {
            return ctx.select_only_statement();
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            PlSqlParser.Select_only_statementContext result = viewQuery(tree.getChild(i));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private ParseTree programOwner(ParseTree tree) {
        if (tree instanceof PlSqlParser.Create_function_bodyContext || tree instanceof PlSqlParser.Create_procedure_bodyContext || tree instanceof PlSqlParser.Create_triggerContext
            || tree instanceof PlSqlParser.Anonymous_blockContext) {
            return tree;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree result = programOwner(tree.getChild(i));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private List<SplitScript> programChildren(ParseTree owner, org.antlr.v4.runtime.CommonTokenStream tokens) {
        List<SplitScript> children = new ArrayList<>();
        collectDeclarations(owner, tokens, children);

        PlSqlParser.BodyContext body = findContext(owner, PlSqlParser.BodyContext.class);
        if (body == null) {
            return children;
        }
        List<SplitScript> statements = programStatements(body, tokens);
        if (owner instanceof PlSqlParser.Anonymous_blockContext) {
            children.addAll(statements);
        } else {
            children.add(createChild(body, tokens, Set.of(SplitQueryType.BLOCK), statements));
        }
        return children;
    }

    private void collectDeclarations(ParseTree tree, org.antlr.v4.runtime.CommonTokenStream tokens, List<SplitScript> result) {
        if (tree instanceof PlSqlParser.BodyContext) {
            return;
        }
        if (tree instanceof PlSqlParser.Declare_specContext declaration) {
            result.add(createChild(declaration, tokens, Set.of(SplitQueryType.PROGRAM_CONTROL), Collections.emptyList()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDeclarations(tree.getChild(i), tokens, result);
        }
    }

    private List<SplitScript> programStatements(ParseTree tree, org.antlr.v4.runtime.CommonTokenStream tokens) {
        List<PlSqlParser.StatementContext> statements = new ArrayList<>();
        collectDirectStatements(tree, statements);
        return statements.stream().map(statement -> programStatement(statement, tokens)).toList();
    }

    private void collectDirectStatements(ParseTree tree, List<PlSqlParser.StatementContext> result) {
        if (tree instanceof PlSqlParser.StatementContext statement) {
            result.add(statement);
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDirectStatements(tree.getChild(i), result);
        }
    }

    private SplitScript programStatement(PlSqlParser.StatementContext statement, org.antlr.v4.runtime.CommonTokenStream tokens) {
        ParseTree node = firstRuleChild(statement);
        if (node instanceof PlSqlParser.BodyContext body) {
            return createChild(statement, tokens, Set.of(SplitQueryType.BLOCK), programStatements(body, tokens));
        }
        if (node instanceof PlSqlParser.BlockContext block) {
            PlSqlParser.BodyContext body = findContext(block, PlSqlParser.BodyContext.class);
            List<SplitScript> children = body == null ? Collections.emptyList() : programStatements(body, tokens);
            return createChild(statement, tokens, Set.of(SplitQueryType.BLOCK), children);
        }

        Set<SplitQueryType> types = new LinkedHashSet<>();
        if (node instanceof PlSqlParser.Sql_statementContext) {
            SplitQueryType primary = statement.accept(splitVisitor());
            types.add(primary == null ? SplitQueryType.UNKNOWN : primary);
            if (isDml(primary) && containsQuery(node)) {
                types.add(SplitQueryType.SELECT);
            }
        } else if (node instanceof PlSqlParser.Call_statementContext || node instanceof PlSqlParser.General_element_partContext) {
            types.add(SplitQueryType.CALL_PROG_OBJ);
        } else if (node instanceof PlSqlParser.Assignment_statementContext assignment && assignment.bind_variable() != null
                   && assignment.bind_variable().getText().toUpperCase().startsWith(":NEW.")) {
            types.add(SplitQueryType.UPDATE);
        } else {
            types.add(SplitQueryType.PROGRAM_CONTROL);
        }

        List<SplitScript> children = isControlNode(node) ? programStatements(node, tokens) : Collections.emptyList();
        if (isControlNode(node) && containsQueryOutsideStatements(node)) {
            children = new ArrayList<>(children);
            children.add(0, createChild((ParserRuleContext) node, tokens, Set.of(SplitQueryType.SELECT), Collections.emptyList()));
        }
        return createChild(statement, tokens, types, children);
    }

    private boolean isDml(SplitQueryType type) {
        return type == SplitQueryType.INSERT || type == SplitQueryType.UPDATE || type == SplitQueryType.DELETE || type == SplitQueryType.MERGE;
    }

    private boolean containsQuery(ParseTree tree) {
        return findContext(tree, PlSqlParser.Select_statementContext.class) != null || findContext(tree, PlSqlParser.Select_only_statementContext.class) != null
               || findContext(tree, PlSqlParser.SubqueryContext.class) != null;
    }

    private boolean containsQueryOutsideStatements(ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof PlSqlParser.StatementContext) {
                continue;
            }
            if (child instanceof PlSqlParser.Select_statementContext || child instanceof PlSqlParser.Select_only_statementContext || child instanceof PlSqlParser.SubqueryContext
                || containsQueryOutsideStatements(child)) {
                return true;
            }
        }
        return false;
    }

    private boolean isControlNode(ParseTree node) {
        return node instanceof PlSqlParser.If_statementContext || node instanceof PlSqlParser.Loop_statementContext || node instanceof PlSqlParser.Case_statementContext
               || node instanceof PlSqlParser.Forall_statementContext;
    }

    private ParseTree firstRuleChild(ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof ParserRuleContext) {
                return child;
            }
        }
        return tree;
    }

    private <T extends ParserRuleContext> T findContext(ParseTree tree, Class<T> type) {
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T result = findContext(tree.getChild(i), type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((PlSqlParser) parser).sql_script();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context.getParent() instanceof PlSqlParser.Sql_scriptContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new OraStatementParser();
    }
}
