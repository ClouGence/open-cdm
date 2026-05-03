package com.clougence.clouddm.ds.doris.analysis.rewrite;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.ds.doris.i18n.DrDsI18nKeys;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.analysis.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.ds.doris.parser.DrDslProvider;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParser;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class DrRewriteSpi implements RewriteSpi {

    @Override
    public String rewriterQuery(QueryRequest request, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(DrDslProvider.INSTANCE, request.getQueryBody());
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        long maxLimit = context.getFetchLimit();
        if (maxLimit > 0) {
            if (this.rewriterLimit(rewriter, astTree, maxLimit)) {
                context.addRewriterInfo(DrDsI18nKeys.REWRITE_LIMIT_LABEL);
            }
        }

        return rewriter.getText();
    }

    private boolean rewriterLimit(TokenStreamRewriter rewriter, ParseTree astTree, long maxLimit) {
        DorisParser.StatementDefaultContext dmlStat = (DorisParser.StatementDefaultContext) ((DorisParser.StatementBaseAliasContext) astTree).statementBase();
        if (dmlStat.query() != null) {
            DorisParser.QueryTermContext queryTerm = dmlStat.query().queryTerm();
            if (queryTerm instanceof DorisParser.QueryTermDefaultContext) {
                DorisParser.QueryPrimaryContext primaryContext = ((DorisParser.QueryTermDefaultContext) queryTerm).queryPrimary();
                if (primaryContext instanceof DorisParser.QueryPrimaryDefaultContext) {
                    DorisParser.QuerySpecificationContext querySpec = ((DorisParser.QueryPrimaryDefaultContext) primaryContext).querySpecification();
                    if (querySpec instanceof DorisParser.RegularQuerySpecificationContext) {
                        if (((DorisParser.RegularQuerySpecificationContext) querySpec).fromClause() == null) {
                            return false;
                        }

                        DorisParser.QueryOrganizationContext organizationContext = ((DorisParser.RegularQuerySpecificationContext) querySpec).queryOrganization();
                        if (organizationContext.limitClause() != null) {
                            DorisParser.LimitClauseContext limitClause = organizationContext.limitClause();
                            List<TerminalNode> limitNumber = limitClause.INTEGER_VALUE();

                            TerminalNode limitToken = null;
                            if (limitNumber.size() == 1) {
                                limitToken = limitNumber.get(0);
                            } else if (limitNumber.size() > 1) {
                                limitToken = limitNumber.get(1);
                            }

                            if (limitToken != null) {
                                long sqlLimit = Long.parseLong(limitToken.getText());
                                long newLimit = Math.min(maxLimit, sqlLimit);
                                if (sqlLimit != newLimit) {
                                    rewriter.replace(limitToken.getSymbol(), newLimit);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        } else {
                            rewriter.insertAfter(organizationContext.getStop(), " LIMIT " + maxLimit);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
