/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.iso.sql2003.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.iso.sql2003.parser.antlr.Sql2003Lexer;

/** SQL:2003 top-level semicolon boundaries. */
public final class Sql2003LexerSplitPolicy implements LexerSplitPolicy {

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        return token.getChannel() == Token.DEFAULT_CHANNEL && token.getType() == Sql2003Lexer.SEMI
                ? LexerSplitBoundary.include(context.tokenEndOffset(token)) : null;
    }
}
