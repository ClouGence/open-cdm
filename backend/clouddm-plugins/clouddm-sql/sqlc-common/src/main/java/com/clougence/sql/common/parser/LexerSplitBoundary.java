/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.parser;

/** Absolute source offsets describing one lexer-discovered statement boundary. */
public record LexerSplitBoundary(int bodyEndOffset, int resumeOffset, boolean reprocessToken) {

    public LexerSplitBoundary{
        if (bodyEndOffset < 0 || resumeOffset < bodyEndOffset) {
            throw new IllegalArgumentException("Invalid lexer split boundary");
        }
    }

    public static LexerSplitBoundary include(int tokenEndOffset) {
        return new LexerSplitBoundary(tokenEndOffset, tokenEndOffset, false);
    }

    public static LexerSplitBoundary exclude(int tokenStartOffset, int tokenEndOffset) {
        return new LexerSplitBoundary(tokenStartOffset, tokenEndOffset, false);
    }
}
