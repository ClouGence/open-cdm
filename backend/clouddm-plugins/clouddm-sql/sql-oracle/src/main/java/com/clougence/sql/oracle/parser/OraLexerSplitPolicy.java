/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.oracle.parser;

import java.util.ArrayDeque;
import java.util.Deque;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** Oracle-family procedural and SQL*Plus boundaries derived only from lexer token types. */
public final class OraLexerSplitPolicy implements LexerSplitPolicy {

    public record TokenTypes(int create, int function, int procedure, int trigger, int packageToken, int type, int body,
                             int declare, int begin, int end, int ifToken, int loop, int whileToken, int caseToken,
                             int with, int external, int language, int asToken, int isToken, int semicolon, int solidus,
                             int[] createPreamble) {
    }

    private enum ProgramKind {
        NONE, ROUTINE, PACKAGE, TYPE, DECLARE, ANONYMOUS, INLINE
    }

    private final TokenTypes     types;
    private final Deque<Boolean> beginFrames = new ArrayDeque<>();
    private ProgramKind         programKind = ProgramKind.NONE;
    private boolean             createStatement;
    private boolean             rootObjectSeen;
    private boolean             nestedRoutinePending;
    private boolean             nestedRoutineDefinition;
    private boolean             externalDefinition;
    private boolean             rootEnded;
    private boolean             transactionCandidate;
    private boolean             inlineWithPending;
    private boolean             inlineClause;
    private boolean             typeBody;
    private int                 controlDepth;
    private int                 visibleTokens;
    private int                 lastType = Token.INVALID_TYPE;

    public OraLexerSplitPolicy(TokenTypes types){
        this.types = types;
    }

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL) {
            return null;
        }
        int type = token.getType();
        if (type == this.types.solidus && context.firstVisibleTokenOnLine(token) && context.onlyVisibleTokenOnLine(token)) {
            return LexerSplitBoundary.exclude(context.tokenStartOffset(token), context.physicalLineEndOffset(token));
        }
        if (type == this.types.semicolon) {
            if (canEndAtSemicolon()) {
                return LexerSplitBoundary.include(context.tokenEndOffset(token));
            }
            afterInternalSemicolon();
            return null;
        }
        accept(type);
        return null;
    }

    private void accept(int type) {
        boolean rootDetected = false;
        if (this.visibleTokens == 0) {
            if (type == this.types.create) {
                this.createStatement = true;
            } else if (type == this.types.declare) {
                this.programKind = ProgramKind.DECLARE;
            } else if (type == this.types.begin) {
                this.programKind = ProgramKind.ANONYMOUS;
                this.transactionCandidate = true;
            }
        }
        if (this.createStatement && !this.rootObjectSeen) {
            ProgramKind detected = createdProgramKind(type);
            if (detected != ProgramKind.NONE) {
                this.programKind = detected;
                this.rootObjectSeen = true;
                rootDetected = true;
            } else if (!isCreatePreamble(type)) {
                this.createStatement = false;
            }
        }

        if (type == this.types.with) {
            this.inlineWithPending = true;
        } else if (this.inlineWithPending && isRoutine(type)) {
            if (this.programKind == ProgramKind.NONE) {
                this.programKind = ProgramKind.INLINE;
                this.rootObjectSeen = true;
                rootDetected = true;
                this.inlineClause = true;
            }
            this.inlineWithPending = false;
        } else if (this.inlineClause && this.programKind == ProgramKind.NONE && isRoutine(type)) {
            this.programKind = ProgramKind.INLINE;
            this.rootObjectSeen = true;
            rootDetected = true;
        } else if (this.inlineWithPending) {
            this.inlineWithPending = false;
        }

        if (!rootDetected && isProgramActive() && !this.rootEnded && this.beginFrames.isEmpty()
            && isRoutine(type) && this.visibleTokens > 0) {
            this.nestedRoutinePending = true;
        }
        if (this.nestedRoutinePending && (type == this.types.asToken || type == this.types.isToken)) {
            this.nestedRoutineDefinition = true;
        }
        if (type == this.types.body && this.programKind == ProgramKind.TYPE) {
            this.typeBody = true;
        }
        if (type == this.types.external || type == this.types.language) {
            this.externalDefinition = true;
        }

        if (type == this.types.begin) {
            boolean nested = this.nestedRoutinePending || !this.beginFrames.isEmpty() && this.beginFrames.peek();
            this.beginFrames.push(nested);
            this.nestedRoutinePending = false;
            this.nestedRoutineDefinition = false;
        } else if (isControl(type) && !this.beginFrames.isEmpty() && this.lastType != this.types.end) {
            this.controlDepth++;
        } else if (type == this.types.end) {
            acceptEnd();
        }

        if (this.transactionCandidate && this.visibleTokens > 0 && type != this.types.begin) {
            this.transactionCandidate = false;
        }
        this.lastType = type;
        this.visibleTokens++;
    }

    private ProgramKind createdProgramKind(int type) {
        if (isRoutine(type)) {
            return ProgramKind.ROUTINE;
        }
        if (type == this.types.packageToken) {
            return ProgramKind.PACKAGE;
        }
        if (type == this.types.type) {
            return ProgramKind.TYPE;
        }
        return ProgramKind.NONE;
    }

    private boolean isCreatePreamble(int type) {
        for (int candidate : this.types.createPreamble) {
            if (candidate == type) {
                return true;
            }
        }
        return false;
    }

    private void acceptEnd() {
        if (this.controlDepth > 0) {
            this.controlDepth--;
            return;
        }
        if (!this.beginFrames.isEmpty()) {
            boolean nested = this.beginFrames.pop();
            if (!nested && this.beginFrames.isEmpty()) {
                this.rootEnded = true;
            }
            return;
        }
        if (this.programKind == ProgramKind.PACKAGE || this.programKind == ProgramKind.TYPE && this.typeBody || this.programKind == ProgramKind.DECLARE) {
            this.rootEnded = true;
        }
    }

    private boolean canEndAtSemicolon() {
        if (this.transactionCandidate && this.visibleTokens == 1) {
            return true;
        }
        if (!this.beginFrames.isEmpty() || this.controlDepth > 0) {
            return false;
        }
        if (this.programKind == ProgramKind.NONE) {
            return true;
        }
        if (this.programKind == ProgramKind.INLINE) {
            return false;
        }
        if (this.programKind == ProgramKind.PACKAGE) {
            return this.rootEnded;
        }
        if (this.programKind == ProgramKind.TYPE && !this.typeBody) {
            return true;
        }
        return this.externalDefinition || this.rootEnded;
    }

    private void afterInternalSemicolon() {
        if (this.nestedRoutinePending && !this.nestedRoutineDefinition && this.beginFrames.isEmpty()) {
            this.nestedRoutinePending = false;
        }
        if (this.programKind == ProgramKind.INLINE && this.rootEnded) {
            this.programKind = ProgramKind.NONE;
            this.rootEnded = false;
            this.rootObjectSeen = false;
            this.externalDefinition = false;
        }
    }

    private boolean isProgramActive() {
        return this.programKind != ProgramKind.NONE && this.programKind != ProgramKind.ANONYMOUS;
    }

    private boolean isRoutine(int type) {
        return type == this.types.function || type == this.types.procedure || type == this.types.trigger;
    }

    private boolean isControl(int type) {
        return type == this.types.ifToken || type == this.types.loop || type == this.types.whileToken || type == this.types.caseToken;
    }

    @Override
    public void reset() {
        this.beginFrames.clear();
        this.programKind = ProgramKind.NONE;
        this.createStatement = false;
        this.rootObjectSeen = false;
        this.nestedRoutinePending = false;
        this.nestedRoutineDefinition = false;
        this.externalDefinition = false;
        this.rootEnded = false;
        this.transactionCandidate = false;
        this.inlineWithPending = false;
        this.inlineClause = false;
        this.typeBody = false;
        this.controlDepth = 0;
        this.visibleTokens = 0;
        this.lastType = Token.INVALID_TYPE;
    }
}
