/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.db2.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.db2.parser.antlr.Db2SqlLexer;

/** DB2 top-level semicolon boundary policy. */
final class Db2LexerSplitPolicy implements LexerSplitPolicy {

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL || token.getType() != Db2SqlLexer.SEMI) {
            return null;
        }
        return LexerSplitBoundary.include(context.tokenEndOffset(token));
    }
}
