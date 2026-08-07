/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.doris.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.doris.parser.antlr.DorisLexer;

/** Doris statement boundaries and fixture-preserving leading trivia rules. */
final class DrLexerSplitPolicy implements LexerSplitPolicy {

    private boolean firstStatement = true;

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL || token.getType() != DorisLexer.SEMICOLON) {
            return null;
        }
        if (context.hasContent() && context.tokenStartOffset(token) > context.lastContentStopOffset()) {
            this.firstStatement = false;
            return LexerSplitBoundary.exclude(context.tokenStartOffset(token), context.tokenEndOffset(token));
        }
        this.firstStatement = false;
        return LexerSplitBoundary.include(context.tokenEndOffset(token));
    }

    @Override
    public int leadingTriviaDiscardLength(String trivia) {
        if (this.firstStatement) {
            return 0;
        }

        int lineStart = 0;
        int keepFrom = -1;
        boolean firstSegment = true;
        for (int index = 0; index < trivia.length(); index++) {
            if (trivia.charAt(index) != '\n') {
                continue;
            }
            int lineEnd = index;
            if (lineEnd > lineStart && trivia.charAt(lineEnd - 1) == '\r') {
                lineEnd--;
            }
            if (!firstSegment && isWhitespace(trivia, lineStart, lineEnd)) {
                keepFrom = index + 1;
            }
            firstSegment = false;
            lineStart = index + 1;
        }
        if (keepFrom >= 0) {
            return keepFrom;
        }
        for (int index = 0; index < trivia.length(); index++) {
            if (!Character.isWhitespace(trivia.charAt(index))) {
                return index;
            }
        }
        return trivia.length();
    }

    private static boolean isWhitespace(String value, int start, int end) {
        for (int index = start; index < end; index++) {
            if (!Character.isWhitespace(value.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
