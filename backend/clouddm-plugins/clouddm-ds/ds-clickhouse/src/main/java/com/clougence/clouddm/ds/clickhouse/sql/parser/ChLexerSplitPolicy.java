/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.clickhouse.sql.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.clickhouse.sql.parser.antlr.ClickHouseLexer;
import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** ClickHouse fixture contract excludes the statement semicolon from emitted SQL. */
final class ChLexerSplitPolicy implements LexerSplitPolicy {

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL || token.getType() != ClickHouseLexer.SEMICOLON) {
            return null;
        }
        return LexerSplitBoundary.exclude(context.tokenStartOffset(token), context.tokenEndOffset(token));
    }
}
