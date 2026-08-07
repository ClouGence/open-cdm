/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.postgres.parser.antlr.PgSqlLexer;

/** PostgreSQL-family statement boundaries derived exclusively from lexer tokens. */
public final class PgLexerSplitPolicy implements LexerSplitPolicy {

    private boolean createPrefix;
    private boolean createRule;
    private boolean createRoutine;
    private boolean beginCandidate;
    private int     parenthesisDepth;
    private int     atomicDepth;
    private int     atomicCaseDepth;

    public PgLexerSplitPolicy(){
    }

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL) {
            return null;
        }
        int type = token.getType();
        if (type == PgSqlLexer.CREATE) {
            this.createPrefix = true;
        } else if (this.createPrefix && type == PgSqlLexer.RULE) {
            this.createRule = true;
        } else if (this.createPrefix && (type == PgSqlLexer.FUNCTION || type == PgSqlLexer.PROCEDURE)) {
            this.createRoutine = true;
        }
        if (this.createRule) {
            if (type == PgSqlLexer.OPEN_PAREN) {
                this.parenthesisDepth++;
            } else if (type == PgSqlLexer.CLOSE_PAREN && this.parenthesisDepth > 0) {
                this.parenthesisDepth--;
            }
        }
        if (this.createRoutine) {
            if (this.beginCandidate) {
                if (type == PgSqlLexer.ATOMIC) {
                    this.atomicDepth++;
                }
                this.beginCandidate = false;
            }
            if (type == PgSqlLexer.BEGIN_P) {
                this.beginCandidate = true;
            } else if (this.atomicDepth > 0 && type == PgSqlLexer.CASE) {
                this.atomicCaseDepth++;
            } else if (type == PgSqlLexer.END_P && this.atomicDepth > 0) {
                if (this.atomicCaseDepth > 0) {
                    this.atomicCaseDepth--;
                } else {
                    this.atomicDepth--;
                }
            }
        }
        if (type != PgSqlLexer.SEMI || this.createRule && this.parenthesisDepth > 0 || this.atomicDepth > 0) {
            return null;
        }
        // Semicolons inside quoted strings, dollar-quoted function/DO bodies, comments and
        // identifiers are absorbed by their lexer tokens and never reach this policy as SEMI.
        // CREATE RULE may contain a parenthesized list of SQL actions separated by semicolons.
        return LexerSplitBoundary.include(context.tokenEndOffset(token));
    }

    @Override
    public void reset() {
        this.createPrefix = false;
        this.createRule = false;
        this.createRoutine = false;
        this.beginCandidate = false;
        this.parenthesisDepth = 0;
        this.atomicDepth = 0;
        this.atomicCaseDepth = 0;
    }
}
