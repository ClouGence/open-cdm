package com.clougence.clouddm.ds.tidb.analysis.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.i18n.TiDsI18nKeys;
import com.clougence.clouddm.ds.tidb.parser.TiDBDslProvider;
import com.clougence.clouddm.ds.tidb.parser.antlr.TiDBParser;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class TiRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(TiDBDslProvider.INSTANCE, request.getQueryBody());
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
        TiDBParser.DmlStatementContext dmlStat = ((TiDBParser.SqlStatementContext) astTree).dmlStatement();
        if (dmlStat.selectStatement() != null) {
            TiDBParser.SelectStatementContext s = dmlStat.selectStatement();
            if (s instanceof TiDBParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (TiDBParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        } else if (dmlStat.withSelectStatement() != null) {
            TiDBParser.SelectStatementContext s = dmlStat.withSelectStatement().selectStatement();
            if (s instanceof TiDBParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (TiDBParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        }
        return false;
    }

    private static boolean rewriterLimit(TokenStreamRewriter rewriter, long maxLimit, TiDBParser.SimpleSelectContext s) {
        TiDBParser.QuerySpecificationContext querySpec = s.querySpecification();
        if (querySpec.fromClause() == null) {
            return false;
        }

        if (querySpec.limitClause() != null) {
            TiDBParser.LimitClauseContext limitClause = querySpec.limitClause();
            TiDBParser.DecimalLiteralContext decimalLiteralCtx = limitClause.limit.decimalLiteral();

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
}
