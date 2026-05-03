package com.clougence.clouddm.ds.polardb.analysis.porx.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.polardb.i18n.PorMyDsI18nKeys;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.ds.polardb.parser.porx.PolarXDslProvider;
import com.clougence.clouddm.ds.polardb.parser.porx.antlr.PolardbXParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class PorXRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(PolarXDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(PorMyDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        PolardbXParser.DmlStatementContext dmlStat = ((PolardbXParser.SqlStatementContext) astTree).dmlStatement();
        if (dmlStat.selectStatement() != null) {
            PolardbXParser.SelectStatementContext s = dmlStat.selectStatement();
            if (s instanceof PolardbXParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (PolardbXParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        } else if (dmlStat.withSelectStatement() != null) {
            PolardbXParser.SelectStatementContext s = dmlStat.withSelectStatement().selectStatement();
            if (s instanceof PolardbXParser.SimpleSelectContext) {
                return rewriterLimit(rewriter, maxLimit, (PolardbXParser.SimpleSelectContext) s);
            } else {
                // TODO: other select type
            }
        }
        return false;
    }

    private static boolean rewriterLimit(TokenStreamRewriter rewriter, long maxLimit, PolardbXParser.SimpleSelectContext s) {
        PolardbXParser.QuerySpecificationContext querySpec = s.querySpecification();
        if (querySpec.fromClause() == null) {
            return false;
        }

        if (querySpec.limitClause() != null) {
            PolardbXParser.LimitClauseContext limitClause = querySpec.limitClause();
            PolardbXParser.DecimalLiteralContext decimalLiteralCtx = limitClause.limit.decimalLiteral();

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
