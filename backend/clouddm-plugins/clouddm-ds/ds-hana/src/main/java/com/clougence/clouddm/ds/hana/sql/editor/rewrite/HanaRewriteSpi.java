/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.sql.editor.rewrite;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;

import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.iso.sql2003.parser.Sql2003DslProvider;
import com.clougence.utils.HashUtils;

public class HanaRewriteSpi implements RewriteSpi {

    @Override
    public String rewriteLimit(String query, RewriteContext context) {
        long maxLimit = context.getFetchLimit();
        if (maxLimit <= 0) {
            return query;
        }

        Lexer lexer = Sql2003DslProvider.INSTANCE.createLexer(CharStreams.fromString(query));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();
        List<Token> tokens = tokenStream.getTokens().stream().filter(token -> token.getChannel() == Token.DEFAULT_CHANNEL && token.getType() != Token.EOF).toList();
        List<Integer> topLevel = topLevelTokenIndexes(tokens);

        int selectPosition = firstWordPosition(tokens, topLevel, "SELECT", 0);
        if (selectPosition < 0 || hasMultipleStatements(tokens, topLevel)) {
            return query;
        }

        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokenStream);
        int limitPosition = firstWordPosition(tokens, topLevel, "LIMIT", selectPosition + 1);
        if (limitPosition >= 0) {
            return rewriteNumericLimit(query, rewriter, tokens, topLevel, limitPosition, maxLimit);
        }

        if (countWord(tokens, topLevel, "SELECT") == 1 && selectPosition + 1 < topLevel.size()) {
            Token next = tokens.get(topLevel.get(selectPosition + 1));
            if (isWord(next, "TOP")) {
                return rewriteNumericLimit(query, rewriter, tokens, topLevel, selectPosition + 1, maxLimit);
            }
        }

        int boundaryPosition = appendBoundaryPosition(tokens, topLevel, selectPosition + 1);
        if (boundaryPosition >= 0) {
            Token boundary = tokens.get(topLevel.get(boundaryPosition));
            if (";".equals(boundary.getText())) {
                rewriter.insertBefore(boundary, " LIMIT " + maxLimit);
            } else {
                rewriter.insertBefore(boundary, "LIMIT " + maxLimit + " ");
            }
        } else {
            rewriter.insertAfter(tokens.get(tokens.size() - 1), " LIMIT " + maxLimit);
        }
        return rewriter.getText();
    }

    private static List<Integer> topLevelTokenIndexes(List<Token> tokens) {
        List<Integer> result = new ArrayList<>();
        int depth = 0;
        boolean quotedIdentifier = false;
        for (int i = 0; i < tokens.size(); i++) {
            String text = tokens.get(i).getText();
            if ("\"".equals(text)) {
                quotedIdentifier = !quotedIdentifier;
                continue;
            }
            if (quotedIdentifier) {
                continue;
            }
            if ("(".equals(text)) {
                depth++;
                continue;
            }
            if (")".equals(text)) {
                depth--;
                continue;
            }
            if (depth == 0) {
                result.add(i);
            }
        }
        return result;
    }

    private static int firstWordPosition(List<Token> tokens, List<Integer> topLevel, String word, int startPosition) {
        for (int i = startPosition; i < topLevel.size(); i++) {
            if (isWord(tokens.get(topLevel.get(i)), word)) {
                return i;
            }
        }
        return -1;
    }

    private static int countWord(List<Token> tokens, List<Integer> topLevel, String word) {
        int count = 0;
        for (int index : topLevel) {
            if (isWord(tokens.get(index), word)) {
                count++;
            }
        }
        return count;
    }

    private static boolean hasMultipleStatements(List<Token> tokens, List<Integer> topLevel) {
        int semicolonPosition = firstWordPosition(tokens, topLevel, ";", 0);
        return semicolonPosition >= 0 && semicolonPosition != topLevel.size() - 1;
    }

    private static int appendBoundaryPosition(List<Token> tokens, List<Integer> topLevel, int startPosition) {
        for (int i = startPosition; i < topLevel.size(); i++) {
            Token token = tokens.get(topLevel.get(i));
            if (";".equals(token.getText()) || isWord(token, "INTO")) {
                return i;
            }
            if (i + 1 >= topLevel.size()) {
                continue;
            }
            Token next = tokens.get(topLevel.get(i + 1));
            if (isWord(token, "WITH") && isWord(next, "HINT")) {
                return i;
            }
            if (isWord(token, "FOR") && (isWord(next, "UPDATE") || isWord(next, "SHARE") || isWord(next, "JSON") || isWord(next, "XML"))) {
                return i;
            }
        }
        return -1;
    }

    private static String rewriteNumericLimit(String query, TokenStreamRewriter rewriter, List<Token> tokens, List<Integer> topLevel, int clausePosition, long maxLimit) {
        if (clausePosition + 1 >= topLevel.size()) {
            return query;
        }
        Token valueToken = tokens.get(topLevel.get(clausePosition + 1));
        String value = valueToken.getText();
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
        rewriter.replace(valueToken, String.valueOf(maxLimit));
        return rewriter.getText();
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

    private static boolean isWord(Token token, String word) {
        return word.equalsIgnoreCase(token.getText());
    }

    @Override
    public String rewriteDmlToQuery(String queryId, String queryStr, RewriteContext context) {
        String statementName = "DM_DML_EXPLAIN_" + HashUtils.fnvHash(queryId);
        return "EXPLAIN PLAN SET STATEMENT_NAME = '" + statementName + "' FOR " + queryStr;
    }
}
