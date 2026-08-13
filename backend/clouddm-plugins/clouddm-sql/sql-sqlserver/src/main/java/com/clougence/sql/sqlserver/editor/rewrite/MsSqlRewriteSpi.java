/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.sqlserver.editor.rewrite;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.sqlserver.parser.MsSplitVisitor;
import com.clougence.sql.sqlserver.parser.MsSqlDslProvider;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.utils.HashUtils;
import com.clougence.utils.StringUtils;

public class MsSqlRewriteSpi implements RewriteSpi {

    private static final String SHOWPLAN_MARKER_PREFIX = "DM_SHOWPLAN_";

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        long maxLimit = context.getFetchLimit();
        if (maxLimit <= 0) {
            return queryStr;
        }

        List<AstSplitScript> scripts = DslHelper.splitDsl(MsSqlDslProvider.INSTANCE, new StringReader(queryStr));
        if (scripts.size() != 1) {
            return queryStr;
        }

        AstSplitScript script = scripts.get(0);
        if (!(script.getAstTree() instanceof SqlServerParser.Sql_clausesContext sqlClauses) || sqlClauses.dml_clause() == null
            || sqlClauses.dml_clause().select_statement_standalone() == null) {
            return queryStr;
        }

        SqlServerParser.Select_statementContext select = sqlClauses.dml_clause().select_statement_standalone().select_statement();
        SqlServerParser.Query_expressionContext query = select.query_expression();
        if (query.query_specification() == null || !query.sql_union().isEmpty()) {
            return queryStr;
        }
        SqlServerParser.Select_order_by_clauseContext queryOrderBy = query.select_order_by_clause();
        SqlServerParser.Select_order_by_clauseContext statementOrderBy = select.select_order_by_clause();
        if ((queryOrderBy != null && queryOrderBy.offset_exp != null) || (statementOrderBy != null && statementOrderBy.offset_exp != null)) {
            return queryStr;
        }

        Parser parser = script.getParser();
        TokenStreamRewriter rewriter = new TokenStreamRewriter((CommonTokenStream) parser.getTokenStream());
        rewriteTop(rewriter, query.query_specification(), maxLimit);
        return rewriter.getText();
    }

    private static void rewriteTop(TokenStreamRewriter rewriter, SqlServerParser.Query_specificationContext query, long maxLimit) {
        SqlServerParser.Top_clauseContext top = query.top;
        if (top == null) {
            Token insertAfter = query.allOrDistinct == null ? query.SELECT().getSymbol() : query.allOrDistinct;
            rewriter.insertAfter(insertAfter, " TOP (" + maxLimit + ")");
            return;
        }

        if (top.top_percent() != null || top.TIES() != null) {
            return;
        }

        SqlServerParser.Top_countContext topCount = top.top_count();
        Token start;
        Token stop;
        String value;
        if (topCount.count_constant == null) {
            start = topCount.topcount_expression.getStart();
            stop = topCount.topcount_expression.getStop();
            value = topCount.topcount_expression.getText();
        } else {
            start = topCount.count_constant;
            stop = topCount.count_constant;
            value = topCount.count_constant.getText();
        }

        try {
            if (new BigInteger(value).compareTo(BigInteger.valueOf(maxLimit)) > 0) {
                rewriter.replace(start, stop, Long.toString(maxLimit));
            }
        } catch (NumberFormatException ignored) {
            // Dynamic TOP expressions cannot be compared with the configured limit safely.
        }
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        String markerPrefix = "/* " + SHOWPLAN_MARKER_PREFIX;
        if (StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace(queryStr, markerPrefix)) {
            int markerEnd = queryStr.indexOf("*/");
            if (markerEnd < 0) {
                return null;
            }
            queryStr = queryStr.substring(markerEnd + 2).stripLeading();
        }
        List<AstSplitScript> scripts = DslHelper.splitDsl(MsSqlDslProvider.INSTANCE, new StringReader(queryStr));
        if (scripts.size() != 1) {
            return null;
        }

        SplitQueryType type = MsSplitVisitor.INSTANCE.visit(scripts.get(0).getAstTree());
        if (type == null || !type.isAllowPlan()) {
            return null;
        }

        Map<String, String> values = new LinkedHashMap<>(context.getParameters().values());
        values.put(SqlParserParameters.SHOW_PLAN, Boolean.TRUE.toString());
        context.setParameters(new SqlParserParameters(values));
        return showPlanMarker(queryId) + queryStr;
    }

    public static String showPlanMarker(String queryId) {
        return "/* " + SHOWPLAN_MARKER_PREFIX + HashUtils.fnvHash(queryId) + " */ ";
    }
}
