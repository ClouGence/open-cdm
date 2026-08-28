/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.oracle.editor.rewrite;

import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.oracle.parser.OraDslProvider;
import com.clougence.sql.oracle.parser.OraSplitVisitor;
import com.clougence.sql.oracle.parser.OracleVersion;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser;
import com.clougence.utils.HashUtils;

public class OraRewriteSpi implements RewriteSpi {

    private static boolean isSafeSelect(PlSqlParser.Select_statementContext select) {
        if (!select.for_update_clause().isEmpty()) {
            return false;
        }
        PlSqlParser.SubqueryContext subquery = select.select_only_statement().subquery();
        if (!subquery.subquery_operation_part().isEmpty()) {
            return false;
        }
        PlSqlParser.Query_blockContext queryBlock = subquery.subquery_basic_elements().query_block();
        return queryBlock != null && queryBlock.from_clause() != null;
    }

    private static PlSqlParser.Fetch_clauseContext outerFetchClause(PlSqlParser.Select_statementContext select) {
        if (!select.fetch_clause().isEmpty()) {
            return select.fetch_clause(select.fetch_clause().size() - 1);
        }
        PlSqlParser.Query_blockContext queryBlock = select.select_only_statement().subquery().subquery_basic_elements().query_block();
        return queryBlock.fetch_clause();
    }

    private static PlSqlParser.Select_statementContext findSelectStatement(ParseTree tree) {
        if (tree instanceof PlSqlParser.Select_statementContext select) {
            return select;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            PlSqlParser.Select_statementContext select = findSelectStatement(tree.getChild(i));
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

    private static boolean hasMultipleValuesRows(ParseTree tree) {
        if (tree instanceof PlSqlParser.Values_clauseContext values) {
            return values.expressions_().size() > 1;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (hasMultipleValuesRows(tree.getChild(i))) {
                return true;
            }
        }
        return false;
    }

    private static PlSqlParser.Explain_statementContext findExplainStatement(ParseTree tree) {
        if (tree instanceof PlSqlParser.Explain_statementContext explain) {
            return explain;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            PlSqlParser.Explain_statementContext explain = findExplainStatement(tree.getChild(i));
            if (explain != null) {
                return explain;
            }
        }
        return null;
    }

    private static ParserRuleContext explainTarget(PlSqlParser.Explain_statementContext explain) {
        if (explain.select_statement() != null) {
            return explain.select_statement();
        }
        if (explain.update_statement() != null) {
            return explain.update_statement();
        }
        if (explain.delete_statement() != null) {
            return explain.delete_statement();
        }
        if (explain.insert_statement() != null) {
            return explain.insert_statement();
        }
        return explain.merge_statement();
    }

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        long maxLimit = context.getFetchLimit();
        OracleVersion version = OracleVersion.parse(context.getParameters().version());
        if (maxLimit <= 0 || !OracleVersion.ge(version, OracleVersion.ORACLE_8)) {
            return queryStr;
        }

        List<AstSplitScript> scripts = DslHelper.splitDsl(OraDslProvider.INSTANCE, new StringReader(queryStr));
        Parser parser = scripts.get(0).getParser();
        ParseTree astTree = scripts.get(0).getAstTree();
        PlSqlParser.Select_statementContext select = findSelectStatement(astTree);
        if (select == null || !isSafeSelect(select)) {
            return queryStr;
        }

        CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        if (!OracleVersion.ge(version, OracleVersion.ORACLE_12)) {
            rewriter.insertBefore(select.getStart(), "SELECT * FROM (");
            rewriter.insertAfter(select.getStop(), ") WHERE ROWNUM <= " + maxLimit);
            return rewriter.getText();
        }

        PlSqlParser.Fetch_clauseContext fetch = outerFetchClause(select);
        if (fetch == null) {
            rewriter.insertAfter(select.getStop(), " FETCH FIRST " + maxLimit + " ROWS ONLY");
            return rewriter.getText();
        }
        if (fetch.PERCENT_KEYWORD() != null || fetch.WITH() != null || fetch.expression() == null) {
            return queryStr;
        }

        String value = fetch.expression().getText();
        if (!isUnsignedInteger(value)) {
            return queryStr;
        }
        long sqlLimit;
        try {
            sqlLimit = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return queryStr;
        }
        if (sqlLimit > maxLimit) {
            rewriter.replace(fetch.expression().getStart(), fetch.expression().getStop(), maxLimit);
            return rewriter.getText();
        }
        return queryStr;
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        List<AstSplitScript> scripts = DslHelper.splitDsl(OraDslProvider.INSTANCE, new StringReader(queryStr));
        if (scripts.size() != 1) {
            return null;
        }

        ParseTree astTree = scripts.get(0).getAstTree();
        PlSqlParser.Explain_statementContext explain = findExplainStatement(astTree);
        if (explain != null) {
            ParserRuleContext target = explainTarget(explain);
            if (target == null) {
                return null;
            }
            queryStr = queryStr.substring(target.getStart().getStartIndex(), target.getStop().getStopIndex() + 1);
        }

        SplitQueryType type = OraSplitVisitor.INSTANCE.visit(astTree);
        if (explain != null) {
            type = OraSplitVisitor.INSTANCE.visit(explainTarget(explain));
        }
        if (type == null || !type.isAllowPlan()) {
            return null;
        }
        OracleVersion version = OracleVersion.parse(context.getParameters().version());
        if (type == SplitQueryType.INSERT && !OracleVersion.ge(version, OracleVersion.ORACLE_23) && hasMultipleValuesRows(astTree)) {
            return null;
        }

        queryStr = queryStr.stripTrailing();
        if (queryStr.endsWith(";")) {
            queryStr = queryStr.substring(0, queryStr.length() - 1);
        }
        int statementId = HashUtils.fnvHash(queryId);
        return "EXPLAIN PLAN SET STATEMENT_ID = '" + statementId + "' FOR " + queryStr;
    }
}
