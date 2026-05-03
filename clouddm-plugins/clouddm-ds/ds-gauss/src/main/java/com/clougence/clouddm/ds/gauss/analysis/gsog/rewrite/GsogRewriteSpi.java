package com.clougence.clouddm.ds.gauss.analysis.gsog.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.gauss.i18n.gsog.GsogDsI18nKeys;
import com.clougence.clouddm.ds.gauss.parser.GaussDslProvider;
import com.clougence.clouddm.ds.gauss.parser.antlr.GaussSqlParser;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class GsogRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(GaussDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(GsogDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        GaussSqlParser.SelectstmtContext selectStmt = ((GaussSqlParser.StmtContext) astTree).selectstmt();
        GaussSqlParser.Select_clauseContext clauseContext = selectStmt.select_no_parens().select_clause();

        List<GaussSqlParser.Simple_select_pramaryContext> contexts = clauseContext.simple_select_pramary();
        if (contexts.size() > 1) {
            return false;
        }

        GaussSqlParser.Simple_select_pramaryContext simple = contexts.get(0);
        if (simple.from_clause() == null) {
            return false;
        }

        GaussSqlParser.Select_limitContext limitContext = simple.select_limit();
        if (limitContext != null) {
            GaussSqlParser.Select_limit_valueContext limit = limitContext.limit_clause().select_limit_value();
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
