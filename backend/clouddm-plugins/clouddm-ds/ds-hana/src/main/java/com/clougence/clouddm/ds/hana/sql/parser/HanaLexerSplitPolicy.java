/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.hana.sql.parser.antlr.HanaLexer;
import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** HANA statement boundaries after {@link HanaSplitLexer} has marked SQLScript internals. */
final class HanaLexerSplitPolicy implements LexerSplitPolicy {

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL || token.getType() != HanaLexer.SEMICOLON) {
            return null;
        }
        return LexerSplitBoundary.include(context.tokenEndOffset(token));
    }
}
