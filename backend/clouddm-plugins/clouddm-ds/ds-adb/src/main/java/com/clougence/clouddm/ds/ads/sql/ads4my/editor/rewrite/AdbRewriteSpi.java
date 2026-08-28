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
package com.clougence.clouddm.ds.ads.sql.ads4my.editor.rewrite;

import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.ads.sql.ads4my.parser.AdsMyDslProvider;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyParser;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class AdbRewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(AdsMyDslProvider.INSTANCE, new StringReader(queryStr));
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            this.rewriterLimit(rewriter, astTree, maxLimit);
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        AdsMyParser.DmlStatementContext dmlStat = ((AdsMyParser.SqlStatementContext) astTree).dmlStatement();
        if (dmlStat.selectStatement() != null) {
            AdsMyParser.SelectStatementContext s = dmlStat.selectStatement();
            if (s instanceof AdsMyParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (AdsMyParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        } else if (dmlStat.withSelectStatement() != null) {
            AdsMyParser.SelectStatementContext s = dmlStat.withSelectStatement().selectStatement();
            if (s instanceof AdsMyParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (AdsMyParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        }
        return false;
    }

    private static boolean rewriterLimit(TokenStreamRewriter rewriter, long maxLimit, AdsMyParser.SimpleSelectContext s) {
        AdsMyParser.QuerySpecificationContext querySpec = s.querySpecification();
        if (querySpec.fromClause() == null) {
            return false;
        }

        if (querySpec.limitClause() != null) {
            AdsMyParser.LimitClauseContext limitClause = querySpec.limitClause();
            AdsMyParser.DecimalLiteralContext decimalLiteralCtx = limitClause.limit.decimalLiteral();

            long sqlLimit = Long.parseLong(decimalLiteralCtx.getText());
            long newLimit = Math.min(maxLimit, sqlLimit);
            if (sqlLimit != newLimit) {
                rewriter.replace(decimalLiteralCtx.getStart(), decimalLiteralCtx.getStop(), newLimit);
                return true;
            } else {
                return false;
            }
        } else {
            rewriter.insertAfter(querySpec.getStop(), " LIMIT " + maxLimit);
            return true;
        }
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        return null;
    }

}
