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
package com.clougence.clouddm.ds.starrocks.analysis.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.starrocks.i18n.SrDsI18nKeys;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.ds.starrocks.parser.SrDslProvider;
import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class SrRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(SrDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(SrDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        StarRocksParser.QueryStatementContext dmlStat = ((StarRocksParser.StatementContext) astTree).queryStatement();
        if (dmlStat != null) {
            StarRocksParser.QueryRelationContext queryContext = dmlStat.queryRelation();
            StarRocksParser.QueryNoWithContext queryNoWithContext = queryContext.queryNoWith();

            if (queryNoWithContext != null) {
                if (queryNoWithContext.queryPrimary() instanceof StarRocksParser.QueryPrimaryDefaultContext) {
                    StarRocksParser.QuerySpecificationContext querySpec = ((StarRocksParser.QueryPrimaryDefaultContext) queryNoWithContext.queryPrimary()).querySpecification();
                    if (querySpec.fromClause() == null || StringUtils.isBlank(querySpec.fromClause().getText())) {
                        return false;
                    }
                }

                StarRocksParser.LimitElementContext limitElement = queryNoWithContext.limitElement();
                if (limitElement != null) {
                    StarRocksParser.LimitConstExprContext limitToken = limitElement.limit;
                    TerminalNode limitValue = limitToken.INTEGER_VALUE();

                    long sqlLimit = Long.parseLong(limitValue.getText());
                    long newLimit = Math.min(maxLimit, sqlLimit);
                    if (sqlLimit != newLimit) {
                        rewriter.replace(limitValue.getSymbol(), String.valueOf(newLimit));
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    rewriter.insertAfter(queryNoWithContext.getStop(), " LIMIT " + maxLimit);
                    return true;
                }
            }
        }
        return false;
    }
}
