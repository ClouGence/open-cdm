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
package com.clougence.clouddm.ds.maxcompute.analysis.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.maxcompute.i18n.McI18nKeys;
import com.clougence.clouddm.ds.maxcompute.parser.McSqlDslProvider;
import com.clougence.clouddm.ds.maxcompute.parser.antlr.McParserParser;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class McRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(McSqlDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(McI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        McParserParser.Select_stmtContext selectStat = ((McParserParser.StmtContext) astTree).select_stmt();
        McParserParser.Order_by_clauseContext orderByContext = selectStat.order_by_clause();
        if (selectStat.T_LIMIT() != null) {
            return doRewriterLimit(rewriter, selectStat.L_INT(), maxLimit);
        }

        McParserParser.Fullselect_stmtContext dmlStat = selectStat.fullselect_stmt();
        List<McParserParser.Fullselect_stmt_itemContext> itemContexts = dmlStat.fullselect_stmt_item();
        if (itemContexts.size() > 1) {
            return false;
        }

        McParserParser.Fullselect_stmt_itemContext itemContext = itemContexts.get(0);
        McParserParser.Subselect_stmtContext subStmtContext = itemContext.subselect_stmt();
        if (subStmtContext != null) {
            if (subStmtContext.from_clause() == null) {
                return false;
            }

            if (subStmtContext.T_LIMIT() != null) {
                return doRewriterLimit(rewriter, subStmtContext.L_INT(), maxLimit);
            } else {
                if (orderByContext != null) {
                    return doAppendLimit(rewriter, orderByContext, maxLimit);
                } else {
                    return doAppendLimit(rewriter, subStmtContext, maxLimit);
                }
            }
        }
        return false;
    }

    private boolean doRewriterLimit(TokenStreamRewriter rewriter, TerminalNode limitToken, long maxLimit) {
        long sqlLimit = Long.parseLong(limitToken.getText());
        long newLimit = Math.min(maxLimit, sqlLimit);
        if (sqlLimit != newLimit) {
            rewriter.replace(limitToken.getSymbol(), newLimit);
            return true;
        } else {
            return false;
        }
    }

    private boolean doAppendLimit(TokenStreamRewriter rewriter, ParserRuleContext token, long maxLimit) {
        rewriter.insertAfter(token.getStop(), " LIMIT " + maxLimit);
        return true;
    }
}
