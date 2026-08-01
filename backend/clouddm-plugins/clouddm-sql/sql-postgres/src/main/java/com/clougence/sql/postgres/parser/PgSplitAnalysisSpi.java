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
package com.clougence.sql.postgres.parser;

import java.io.Reader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class PgSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final PgDslProvider        provider;
    private final ThreadLocal<Integer> lastStatementStart = new ThreadLocal<>();

    public PgSplitAnalysisSpi(PostgresVersion version){
        this.provider = new PgDslProvider(version);
    }

    public PostgresVersion version() {
        return provider.version();
    }

    protected DslProvider dslProvider() {
        return provider;
    }

    @Override
    public java.util.List<SplitScript> splitScript(Reader reader, List<QueryArg> args, int baseLine, int baseColumn) {
        this.lastStatementStart.remove();
        try {
            return super.splitScript(reader, args, baseLine, baseColumn);
        } finally {
            this.lastStatementStart.remove();
        }
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return new PgSplitVisitor(version());
    }

    @Override
    protected java.util.Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        java.util.Set<SplitQueryType> types = new PgSplitVisitor(version()).collectTypes(context);
        return types.isEmpty() ? java.util.Collections.singleton(SplitQueryType.UNKNOWN) : types;
    }

    @Override
    protected java.util.List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        ParserRuleContext query = viewQuery(context);
        if (query != null) {
            java.util.Set<SplitQueryType> types = new PgSplitVisitor(version()).collectTypes(query);
            if (types.isEmpty()) {
                types = java.util.Collections.singleton(SplitQueryType.UNKNOWN);
            }
            return java.util.List.of(createChild(query, tokens, types, java.util.Collections.emptyList()));
        }
        ParserRuleContext triggerFunction = triggerFunction(context);
        if (triggerFunction != null) {
            return java.util.List.of(createChild(triggerFunction, tokens, java.util.Collections.singleton(SplitQueryType.CALL_PROG_OBJ), java.util.Collections.emptyList()));
        }
        return java.util.Collections.emptyList();
    }

    private ParserRuleContext viewQuery(ParserRuleContext context) {
        if (context instanceof PgSqlParser.ExplainstmtContext) {
            return null;
        }
        if (context instanceof PgSqlParser.ViewstmtContext view) {
            return view.selectstmt();
        }
        if (context instanceof PgSqlParser.CreatematviewstmtContext view) {
            return view.selectstmt();
        }
        for (int i = 0; i < context.getChildCount(); i++) {
            if (context.getChild(i) instanceof ParserRuleContext child) {
                ParserRuleContext query = viewQuery(child);
                if (query != null) {
                    return query;
                }
            }
        }
        return null;
    }

    private ParserRuleContext triggerFunction(ParserRuleContext context) {
        if (context instanceof PgSqlParser.CreatetrigstmtContext trigger) {
            return trigger.func_name();
        }
        if (context instanceof PgSqlParser.CreateeventtrigstmtContext trigger) {
            return trigger.func_name();
        }
        for (int i = 0; i < context.getChildCount(); i++) {
            if (context.getChild(i) instanceof ParserRuleContext child) {
                ParserRuleContext function = triggerFunction(child);
                if (function != null) {
                    return function;
                }
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((PgSqlParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        if (!(context.getParent() instanceof PgSqlParser.StmtmultiContext)) {
            return false;
        }
        int start = context.getStart().getTokenIndex();
        Integer previous = this.lastStatementStart.get();
        if (previous != null && previous == start) {
            return false;
        }
        this.lastStatementStart.set(start);
        return true;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new PgStatementParser();
    }
}
