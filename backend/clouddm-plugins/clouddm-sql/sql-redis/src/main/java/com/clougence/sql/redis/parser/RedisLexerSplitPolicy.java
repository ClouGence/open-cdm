/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.redis.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.redis.parser.antlr.RedisLexer;

/** Redis command boundaries: one physical line, with inter-line comments owned by the next command. */
public final class RedisLexerSplitPolicy implements LexerSplitPolicy {

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (!context.hasContent()) {
            return null;
        }
        if (token.getType() == RedisLexer.EOL) {
            return new LexerSplitBoundary(context.lastContentStopOffset(), context.lastContentStopOffset(), false);
        }
        if (token.getLine() > context.lastContentLine()) {
            return new LexerSplitBoundary(context.lastContentStopOffset(), context.lastContentStopOffset(), true);
        }
        return null;
    }
}
