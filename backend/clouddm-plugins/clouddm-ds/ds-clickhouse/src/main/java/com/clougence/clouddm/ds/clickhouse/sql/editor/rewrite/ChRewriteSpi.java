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
package com.clougence.clouddm.ds.clickhouse.sql.editor.rewrite;

import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.sql.parser.ChSqlDslProvider;
import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseParser;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class ChRewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String query, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(ChSqlDslProvider.INSTANCE, new StringReader(query));
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

    @Override
    public String rewriteDmlToQuery(String queryId, String queryStr, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(ChSqlDslProvider.INSTANCE, new StringReader(queryStr));
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();
        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();

        String rewritten = queryStr;
        if (astTree instanceof ClickHouseParser.QueryStmtDeleteContext query) {
            ClickHouseParser.DeleteStmtContext delete = query.deleteStmt();
            rewritten = "EXPLAIN ESTIMATE " + selectForFilter(tokens, delete.nestedIdentifier(), delete.whereClause());
        } else if (astTree instanceof ClickHouseParser.QueryStmtUpdateContext query) {
            ClickHouseParser.UpdateStmtContext update = query.updateStmt();
            rewritten = "EXPLAIN ESTIMATE " + selectForFilter(tokens, update.nestedIdentifier(), update.whereClause());
        } else if (astTree instanceof ClickHouseParser.QueryStmtInsertContext query && query.insertStmt().dataClause() instanceof ClickHouseParser.DataClauseSelectContext select) {
            rewritten = "EXPLAIN ESTIMATE " + tokens.getText(select.selectUnionStmt().getSourceInterval());
        } else if (astTree instanceof ClickHouseParser.QueryStmtQueryContext query && query.query().selectUnionStmt() != null) {
            rewritten = "EXPLAIN ESTIMATE " + queryStr;
        }
        return rewritten;
    }

    private static String selectForFilter(CommonTokenStream tokens, ParseTree table, ClickHouseParser.WhereClauseContext where) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(tokens.getText(table.getSourceInterval()));
        if (where != null) {
            sql.append(' ').append(tokens.getText(where.getSourceInterval()));
        }
        return sql.toString();
    }
}
