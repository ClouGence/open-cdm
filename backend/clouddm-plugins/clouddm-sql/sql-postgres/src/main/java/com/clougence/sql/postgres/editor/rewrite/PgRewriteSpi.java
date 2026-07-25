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
package com.clougence.sql.postgres.editor.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.common.analysis.SqlAnalysisI18nKeys;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class PgRewriteSpi implements RewriteSpi {

    private final PgDslProvider provider;

    public PgRewriteSpi(PostgresVersion version){
        this.provider = new PgDslProvider(version);
    }

    public PostgresVersion version() {
        return provider.version();
    }

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(provider, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(SqlAnalysisI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        PgSqlParser.SelectstmtContext selectStmt = ((PgSqlParser.StmtContext) astTree).selectstmt();
        PgSqlParser.Select_clauseContext clauseContext = selectStmt.select_no_parens().select_clause();

        List<PgSqlParser.Simple_select_pramaryContext> contexts = clauseContext.simple_select_pramary();
        if (contexts.size() > 1) {
            return false;
        }

        PgSqlParser.Simple_select_pramaryContext simple = contexts.get(0);
        if (simple.from_clause() == null) {
            return false;
        }

        PgSqlParser.Select_limitContext limitContext = simple.select_limit();
        if (limitContext != null) {
            PgSqlParser.Select_limit_valueContext limit = limitContext.limit_clause().select_limit_value();

            long sqlLimit = Long.parseLong(limit.getText());
            long newLimit = Math.min(maxLimit, sqlLimit);
            if (sqlLimit != newLimit) {
                rewriter.replace(limit.getStart(), limit.getStop(), newLimit);
                return true;
            } else {
                return false;
            }
        } else {
            rewriter.insertAfter(simple.getStop(), " LIMIT " + maxLimit);
            return true;
        }
    }
}
