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
package com.clougence.sql.doris.editor.rewrite;

import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.doris.parser.DorisVersion;
import com.clougence.sql.doris.parser.DrDslProvider;
import com.clougence.sql.doris.parser.DrSplitVisitor;
import com.clougence.sql.doris.parser.antlr.DorisParser;

public class DrRewriteSpi implements RewriteSpi {

    private static boolean appendWhereForExplain(ParseTree tree, TokenStreamRewriter rewriter) {
        if (tree instanceof DorisParser.UpdateContext update && update.whereClause() == null) {
            rewriter.insertAfter(update.getStop(), " WHERE 1=1");
            return true;
        }
        if (tree instanceof DorisParser.DeleteContext delete && delete.whereClause() == null) {
            rewriter.insertAfter(delete.getStop(), " WHERE 1=1");
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (appendWhereForExplain(tree.getChild(i), rewriter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean supportsDeleteExplain(RewriteContext context) {
        SqlParserParameters parameters = SqlParserParameters.nullToEmpty(context == null ? null : context.getParameters());
        String exactVersion = parameters.get(SqlParserParameters.EXACT_VERSION);
        if (exactVersion != null) {
            try {
                return DorisVersion.parseExactVersionCode(exactVersion) >= 20105;
            } catch (IllegalArgumentException ignored) {
            }
        }
        return DorisVersion.parse(parameters.version()).ordinal() >= DorisVersion.DORIS_3.ordinal();
    }

    private static boolean containsDelete(ParseTree tree) {
        if (tree instanceof DorisParser.DeleteContext) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (containsDelete(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsExplain(ParseTree tree) {
        if (tree instanceof DorisParser.ExplainContext) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (containsExplain(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(DrDslProvider.INSTANCE, new StringReader(queryStr));
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

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(DrDslProvider.INSTANCE, new StringReader(queryStr));
        if (scripts.size() != 1) {
            return null;
        }

        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();
        if (containsDelete(astTree) && !supportsDeleteExplain(context)) {
            return null;
        }
        SplitQueryType type = DrSplitVisitor.INSTANCE.visit(astTree);
        if (type == null || !type.isAllowPlan()) {
            return null;
        }

        TokenStreamRewriter rewriter = new TokenStreamRewriter(parser.getTokenStream());
        boolean appendedWhere = appendWhereForExplain(astTree, rewriter);
        String rewritten = appendedWhere ? rewriter.getText() : queryStr;
        if (containsExplain(astTree)) {
            return rewritten;
        }
        return "EXPLAIN " + rewritten;
    }
}
