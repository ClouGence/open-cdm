/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerLexer;

/** SQL Server semicolon and sqlcmd {@code GO} batch boundary policy. */
final class MsLexerSplitPolicy implements LexerSplitPolicy {

    private boolean createStatement;
    private boolean programDefinition;
    private boolean programBody;
    private boolean programEnded;
    private int     beginDepth;
    private int     caseDepth;
    private int     visibleTokens;

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL) {
            return null;
        }
        int type = token.getType();
        if (type == SqlServerLexer.GO && context.firstVisibleTokenOnLine(token) && context.onlyVisibleTokenOnLine(token)) {
            return LexerSplitBoundary.exclude(context.tokenStartOffset(token), context.physicalLineEndOffset(token));
        }
        if (type == SqlServerLexer.SEMI) {
            if (!this.programDefinition || this.programEnded) {
                return LexerSplitBoundary.include(context.tokenEndOffset(token));
            }
            return null;
        }
        if (this.visibleTokens == 0 && (type == SqlServerLexer.CREATE || type == SqlServerLexer.ALTER)) {
            this.createStatement = true;
        } else if (this.createStatement && (type == SqlServerLexer.PROCEDURE || type == SqlServerLexer.FUNCTION || type == SqlServerLexer.TRIGGER)) {
            this.programDefinition = true;
        }
        if (this.programDefinition) {
            if (type == SqlServerLexer.BEGIN) {
                this.programBody = true;
                this.beginDepth++;
            } else if (type == SqlServerLexer.CASE) {
                this.caseDepth++;
            } else if (type == SqlServerLexer.END) {
                if (this.caseDepth > 0) {
                    this.caseDepth--;
                } else if (this.beginDepth > 0) {
                    this.beginDepth--;
                    this.programEnded = this.programBody && this.beginDepth == 0;
                }
            }
        }
        this.visibleTokens++;
        return null;
    }

    @Override
    public void reset() {
        this.createStatement = false;
        this.programDefinition = false;
        this.programBody = false;
        this.programEnded = false;
        this.beginDepth = 0;
        this.caseDepth = 0;
        this.visibleTokens = 0;
    }
}
