package com.clougence.clouddm.ds.oceanbase.analysis.obformysql.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.oceanbase.i18n.ObDsI18nKeys;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.ObMyDslProvider;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class ObRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(ObMyDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(ObDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        ObForMySqlParser.DmlStatementContext dmlStat = ((ObForMySqlParser.SqlStatementContext) astTree).dmlStatement();
        if (dmlStat.selectStatement() != null) {
            ObForMySqlParser.SelectStatementContext s = dmlStat.selectStatement();
            if (s instanceof ObForMySqlParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (ObForMySqlParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        } else if (dmlStat.withSelectStatement() != null) {
            ObForMySqlParser.SelectStatementContext s = dmlStat.withSelectStatement().selectStatement();
            if (s instanceof ObForMySqlParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (ObForMySqlParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        }
        return false;
    }

    private static boolean rewriterLimit(TokenStreamRewriter rewriter, long maxLimit, ObForMySqlParser.SimpleSelectContext s) {
        ObForMySqlParser.QuerySpecificationContext querySpec = s.querySpecification();
        if (querySpec.fromClause() == null) {
            return false;
        }

        if (querySpec.limitClause() != null) {
            ObForMySqlParser.LimitClauseContext limitClause = querySpec.limitClause();
            ObForMySqlParser.DecimalLiteralContext decimalLiteralCtx = limitClause.limit.decimalLiteral();

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
