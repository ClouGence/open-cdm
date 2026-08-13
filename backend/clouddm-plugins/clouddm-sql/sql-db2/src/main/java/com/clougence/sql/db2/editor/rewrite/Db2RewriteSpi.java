/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.db2.editor.rewrite;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;

import org.antlr.v4.runtime.*;

import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.db2.parser.Db2DslProvider;
import com.clougence.sql.db2.parser.antlr.Db2SqlLexer;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;
import com.clougence.utils.HashUtils;

public class Db2RewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String queryId, String queryStr, RewriteContext context) {
        long maxLimit = context.getFetchLimit();
        if (maxLimit <= 0) {
            return queryStr;
        }

        List<AstSplitScript> scripts = DslHelper.splitDsl(Db2DslProvider.INSTANCE, new StringReader(queryStr));
        if (scripts.size() != 1 || !(scripts.get(0).getAstTree() instanceof Db2SqlParser.Sql_statementContext statement) || statement.select_statement() == null) {
            return queryStr;
        }

        Db2SqlParser.Select_statementContext select = statement.select_statement();
        Db2SqlParser.FullselectContext fullselect = select.fullselect();
        Db2SqlParser.Fetch_clauseContext fetch = select.fetch_clause();
        if (fetch == null) {
            fetch = fullselect.fetch_clause();
        }
        if (fetch == null && fullselect.subselect().size() == 1) {
            fetch = fullselect.subselect(0).fetch_clause();
        }

        Parser parser = scripts.get(0).getParser();
        TokenStreamRewriter rewriter = new TokenStreamRewriter(parser.getTokenStream());
        if (fetch != null) {
            if (fetch.fetch_row_count() == null) {
                return queryStr;
            }
            Token count = fetch.fetch_row_count().getStart();
            BigInteger sqlLimit = new BigInteger(count.getText());
            if (sqlLimit.compareTo(BigInteger.valueOf(maxLimit)) > 0) {
                rewriter.replace(count, count, Long.toString(maxLimit));
                return rewriter.getText();
            }
            return queryStr;
        }

        Db2SqlParser.Isolation_clauseContext isolation = fullselect.isolation_clause();
        if (isolation == null && fullselect.subselect().size() == 1) {
            isolation = fullselect.subselect(0).isolation_clause();
        }
        if (isolation == null) {
            rewriter.insertAfter(fullselect.getStop(), " FETCH FIRST " + maxLimit + " ROWS ONLY");
        } else {
            rewriter.insertBefore(isolation.getStart(), "FETCH FIRST " + maxLimit + " ROWS ONLY ");
        }
        return rewriter.getText();
    }

    @Override
    public String rewriteToExplain(String queryId, String queryStr, RewriteContext context) {
        CommonTokenStream tokens = new CommonTokenStream(Db2DslProvider.INSTANCE.createLexer(CharStreams.fromString(queryStr)));
        tokens.fill();
        List<Token> defaultTokens = tokens.getTokens().stream()
                .filter(token -> token.getChannel() == Token.DEFAULT_CHANNEL && token.getType() != Token.EOF)
                .toList();
        Token first = defaultTokens.isEmpty() ? null : defaultTokens.get(0);
        if (first != null && first.getType() == Db2SqlLexer.EXPLAIN) {
            Token target = explainTarget(defaultTokens);
            if (target == null) {
                return null;
            }
            queryStr = queryStr.substring(target.getStartIndex());
            first = target;
        }
        if (first == null || !isExplainable(first.getType())) {
            return null;
        }
        int queryNo = HashUtils.fnvHash(queryId);
        return "EXPLAIN PLAN SET QUERYNO = " + queryNo + " FOR " + queryStr;
    }

    private static Token explainTarget(List<Token> tokens) {
        for (int i = 1; i + 1 < tokens.size(); i++) {
            if (tokens.get(i).getType() == Db2SqlLexer.FOR && isExplainable(tokens.get(i + 1).getType())) {
                return tokens.get(i + 1);
            }
        }
        return null;
    }

    private static boolean isExplainable(int tokenType) {
        return tokenType == Db2SqlLexer.SELECT ||//
               tokenType == Db2SqlLexer.INSERT ||//
               tokenType == Db2SqlLexer.UPDATE ||//
               tokenType == Db2SqlLexer.DELETE ||//
               tokenType == Db2SqlLexer.MERGE;
    }
}
