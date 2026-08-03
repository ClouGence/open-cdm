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
package com.clougence.clouddm.ds.tidb.sql.editor.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.i18n.TiDsI18nKeys;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBDslProvider;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class TiRewriteSpi implements RewriteSpi {

    private final TiDBDslProvider provider;

    public TiRewriteSpi(TiDBDslProvider provider){
        this.provider = provider;
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
                context.addRewriterInfo(TiDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        TiDBParser.QuerySpecificationContext query = findQuerySpecification(astTree);
        if (query == null || query.fromClause() == null) {
            return false;
        }
        if (query.limitClause() != null) {
            TiDBParser.LimitClauseContext limitClause = query.limitClause();
            TiDBParser.UnsignedDecimalIntegerLiteralContext decimalLiteralCtx = limitClause.limit.unsignedDecimalIntegerLiteral();
            if (decimalLiteralCtx == null) {
                return false;
            }

            long sqlLimit = Long.parseLong(decimalLiteralCtx.getText());
            long newLimit = Math.min(maxLimit, sqlLimit);
            if (sqlLimit != newLimit) {
                rewriter.replace(decimalLiteralCtx.getStart(), decimalLiteralCtx.getStop(), newLimit);
                return true;
            } else {
                return false;
            }
        } else {
            rewriter.insertAfter(query.getStop(), " LIMIT " + maxLimit);
            return true;
        }
    }

    private static TiDBParser.QuerySpecificationContext findQuerySpecification(ParseTree tree) {
        if (tree instanceof TiDBParser.QuerySpecificationContext query) {
            return query;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            TiDBParser.QuerySpecificationContext query = findQuerySpecification(tree.getChild(i));
            if (query != null) {
                return query;
            }
        }
        return null;
    }
}
