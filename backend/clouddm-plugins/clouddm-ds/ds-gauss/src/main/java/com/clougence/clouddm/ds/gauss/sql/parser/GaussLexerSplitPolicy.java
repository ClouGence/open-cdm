/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.gauss.sql.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlLexer;
import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** Gauss statement boundaries derived exclusively from its lexer tokens. */
public final class GaussLexerSplitPolicy implements LexerSplitPolicy {

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL || token.getType() != GaussSqlLexer.SEMI) {
            return null;
        }
        return LexerSplitBoundary.include(context.tokenEndOffset(token));
    }
}
