package com.clougence.clouddm.ds.clickhouse.analysis.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.i18n.ChDsI18nKeys;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.ds.clickhouse.parser.ChSqlDslProvider;
import com.clougence.clouddm.ds.clickhouse.parser.antlr.ClickHouseParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class ChRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(ChSqlDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(ChDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        ClickHouseParser.SelectUnionStmtContext selectCtx = ((ClickHouseParser.QueryStmtQueryContext) astTree).query().selectUnionStmt();
        if (selectCtx != null) {
            List<ClickHouseParser.SelectStmtWithParensContext> parensContexts = selectCtx.selectStmtWithParens();
            if (parensContexts.size() > 1) {
                return false;
            }

            ClickHouseParser.SelectStmtContext querySpec = parensContexts.get(0).selectStmt();
            if (querySpec.fromClause() == null) {
                return false;
            }

            if (querySpec.limitClause() != null) {
                ClickHouseParser.LimitClauseContext limitClause = querySpec.limitClause();
                ClickHouseParser.LimitExprContext limitExprContext = limitClause.limitExpr();
                List<ClickHouseParser.ColumnExprContext> columnExprCtx = limitExprContext.columnExpr();

                ClickHouseParser.ColumnExprContext limitToken = null;
                if (!columnExprCtx.isEmpty()) {
                    limitToken = columnExprCtx.get(0);
                }

                if (limitToken != null) {
                    long sqlLimit = Long.parseLong(limitToken.getText());
                    long newLimit = Math.min(maxLimit, sqlLimit);
                    if (sqlLimit != newLimit) {
                        rewriter.replace(limitToken.getStart(), limitToken.getStop(), newLimit);
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                rewriter.insertAfter(querySpec.getStop(), " LIMIT " + maxLimit);
                return true;
            }
        }
        return false;
    }
}
