/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.dameng.sql.editor.rewrite;

import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.parser.DmDslProvider;
import com.clougence.clouddm.ds.dameng.sql.parser.DmVersion;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;

public class DmRewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String query, RewriteContext context) {
        long maxLimit = context.getFetchLimit();
        DmVersion version = DmVersion.parse(context.getDatabaseVersion());
        if (maxLimit <= 0 || !DmVersion.ge(version, DmVersion.DM_7)) {
            return query;
        }

        List<AstSplitScript> scripts = DslHelper.splitDsl(DmDslProvider.INSTANCE, new StringReader(query));
        Parser parser = scripts.get(0).getParser();
        DmSqlParser.SelectStatementContext select = findSelectStatement(scripts.get(0).getAstTree());
        if (select == null || select.selectOperand().selectQuery() == null) {
            return query;
        }

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        DmSqlParser.TopClauseContext top = select.selectOperand().selectQuery().topClause();
        if (top != null) {
            if (top.expression().size() != 1 || top.PERCENT_KEYWORD() != null || top.WITH() != null) {
                return query;
            }
            return rewriteNumericLimit(query, rewriter, top.expression(0), maxLimit);
        }

        DmSqlParser.LimitConditionContext limitCondition = outerLimitCondition(select);
        if (limitCondition == null) {
            DmSqlParser.WithUrClauseContext withUr = outerWithUrClause(select);
            if (withUr == null) {
                rewriter.insertAfter(select.getStop(), " LIMIT " + maxLimit);
            } else {
                rewriter.insertBefore(withUr.getStart(), "LIMIT " + maxLimit + " ");
            }
            return rewriter.getText();
        }
        if (limitCondition.limitClause() != null) {
            DmSqlParser.LimitClauseContext limit = limitCondition.limitClause();
            List<DmSqlParser.ExpressionContext> expressions = limit.expression();
            int countIndex = 0;
            if (limit.COMMA() != null || limit.getStart().getType() == DmSqlParser.OFFSET) {
                countIndex = expressions.size() - 1;
            }
            return rewriteNumericLimit(query, rewriter, expressions.get(countIndex), maxLimit);
        }

        DmSqlParser.RowLimitClauseContext rowLimit = limitCondition.rowLimitClause();
        if (rowLimit.fetchClause() == null) {
            rewriter.insertAfter(rowLimit.getStop(), " FETCH NEXT " + maxLimit + " ROWS ONLY");
            return rewriter.getText();
        }
        DmSqlParser.FetchClauseContext fetch = rowLimit.fetchClause();
        if (fetch.WITH() != null || fetch.fetchCountClause() == null || fetch.fetchCountClause().PERCENT_KEYWORD() != null) {
            return query;
        }
        return rewriteNumericLimit(query, rewriter, fetch.fetchCountClause().expression(), maxLimit);
    }

    private static String rewriteNumericLimit(String query, TokenStreamRewriter rewriter, DmSqlParser.ExpressionContext expression, long maxLimit) {
        String value = expression.getText();
        if (!isUnsignedInteger(value)) {
            return query;
        }
        long sqlLimit;
        try {
            sqlLimit = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return query;
        }
        if (sqlLimit <= maxLimit) {
            return query;
        }
        rewriter.replace(expression.getStart(), expression.getStop(), maxLimit);
        return rewriter.getText();
    }

    private static DmSqlParser.LimitConditionContext outerLimitCondition(DmSqlParser.SelectStatementContext select) {
        DmSqlParser.SelectStatementTailContext tail = select.selectStatementTail();
        if (tail == null || tail.selectTailCore() == null) {
            return null;
        }
        DmSqlParser.SelectTailCoreContext core = tail.selectTailCore();
        if (core.limitCondition() != null) {
            return core.limitCondition();
        }
        if (core.orderFirstTail() != null) {
            return core.orderFirstTail().limitCondition();
        }
        if (core.lockFirstTail() != null) {
            return core.lockFirstTail().limitCondition();
        }
        return null;
    }

    private static DmSqlParser.WithUrClauseContext outerWithUrClause(DmSqlParser.SelectStatementContext select) {
        DmSqlParser.SelectStatementTailContext tail = select.selectStatementTail();
        if (tail == null || tail.selectTailCore() == null) {
            return null;
        }
        return tail.selectTailCore().withUrClause();
    }

    private static DmSqlParser.SelectStatementContext findSelectStatement(ParseTree tree) {
        if (tree instanceof DmSqlParser.SelectStatementContext select) {
            return select;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            DmSqlParser.SelectStatementContext select = findSelectStatement(tree.getChild(i));
            if (select != null) {
                return select;
            }
        }
        return null;
    }

    private static boolean isUnsignedInteger(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String rewriteDmlToQuery(String queryId, String queryStr, RewriteContext context) {
        return "EXPLAIN FOR " + queryStr;
    }
}
