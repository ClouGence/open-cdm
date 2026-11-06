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
package com.clougence.clouddm.ds.gauss.sql.parser;

import java.util.LinkedHashSet;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.gauss.sql.analysis.reference.GaussResourceRegistry;
import com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class GaussSplitAnalysisSpi extends PgSplitAnalysisSpi {

    public GaussSplitAnalysisSpi(){
        super(PostgresVersion.LATEST);
    }

    @Override
    protected DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return new GaussSplitVisitor();
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        SplitQueryType type = context.accept(new GaussSplitVisitor());
        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(type == null ? SplitQueryType.UNKNOWN : type);
        collectFunctionTypes(context, types);
        return types;
    }

    public static boolean executesStatement(GaussSqlParser.ExplainstmtContext plan) {
        if (plan.analyze_keyword() != null) {
            return true;
        }
        if (plan.explain_option_list() != null) {
            for (var option : plan.explain_option_list().explain_option_elem()) {
                if (option.explain_option_name().analyze_keyword() != null) {
                    if (option.explain_option_arg() == null) {
                        return true;
                    }
                    String value = option.explain_option_arg().getText();
                    if (value.startsWith("'") && value.endsWith("'")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equals("1");
                }
            }
        }
        return false;
    }

    private void collectFunctionTypes(ParseTree tree, Set<SplitQueryType> types) {
        if (tree instanceof GaussSqlParser.CreatefunctionstmtContext || tree instanceof GaussSqlParser.CreatetrigstmtContext || tree instanceof GaussSqlParser.ViewstmtContext
            || tree instanceof GaussSqlParser.CreatematviewstmtContext view && view.with_data_() != null && view.with_data_().NO() != null) {
            return;
        }
        if (tree instanceof GaussSqlParser.ExplainstmtContext plan) {
            if (!executesStatement(plan)) {
                return;
            }
            SplitQueryType executed = plan.explainablestmt().accept(new GaussSplitVisitor());
            types.add(executed == null ? SplitQueryType.UNKNOWN : executed);
        }
        if (tree instanceof GaussSqlParser.Func_expr_common_subexprContext function) {
            types.addAll(GaussResourceRegistry.instance().functionBehavior(function.getStart().getText(), null).statementTypes());
        }
        if (tree instanceof GaussSqlParser.Func_applicationContext function) {
            types.addAll(GaussResourceRegistry.instance().functionBehavior(function.func_name().getText(), null).statementTypes());
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectFunctionTypes(tree.getChild(i), types);
        }
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((GaussSqlParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context.getParent() instanceof GaussSqlParser.StmtmultiContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new GaussAntlrStatementParser();
    }

}
