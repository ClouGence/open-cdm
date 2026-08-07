/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.parser;

import java.util.ArrayDeque;
import java.util.Deque;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlLexer;
import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** Dameng client-script and procedural-block boundaries derived only from lexer tokens. */
final class DmLexerSplitPolicy implements LexerSplitPolicy {

    private enum ProgramKind {
        NONE, ROUTINE, PACKAGE, TYPE, CLASS, SCHEMA, DECLARE, ANONYMOUS, INLINE
    }

    private final Deque<Boolean> beginFrames = new ArrayDeque<>();
    private ProgramKind         programKind = ProgramKind.NONE;
    private boolean             createStatement;
    private boolean             rootObjectSeen;
    private boolean             nestedRoutinePending;
    private boolean             nestedRoutineDefinition;
    private boolean             externalDefinition;
    private boolean             aggregateDefinition;
    private boolean             rootEnded;
    private boolean             transactionCandidate;
    private boolean             inlineWithPending;
    private boolean             inlineClause;
    private boolean             typeBody;
    private int                 controlDepth;
    private int                 braceDepth;
    private int                 visibleTokens;
    private int                 lastType = Token.INVALID_TYPE;

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL) {
            return null;
        }
        int type = token.getType();
        if (type == DmSqlLexer.SLASH && context.firstVisibleTokenOnLine(token) && context.onlyVisibleTokenOnLine(token)) {
            return LexerSplitBoundary.exclude(context.tokenStartOffset(token), context.physicalLineEndOffset(token));
        }
        if (type == DmSqlLexer.SEMI) {
            if (canEndAtSemicolon()) {
                return LexerSplitBoundary.include(context.tokenEndOffset(token));
            }
            afterInternalSemicolon();
            return null;
        }
        accept(type);
        if (type == DmSqlLexer.RBRACE && this.programKind == ProgramKind.CLASS
                && this.rootEnded && this.braceDepth == 0) {
            return LexerSplitBoundary.include(context.tokenEndOffset(token));
        }
        return null;
    }

    private void accept(int type) {
        boolean rootDetected = false;
        if (this.visibleTokens == 0) {
            if (type == DmSqlLexer.CREATE) {
                this.createStatement = true;
            } else if (type == DmSqlLexer.DECLARE) {
                this.programKind = ProgramKind.DECLARE;
            } else if (type == DmSqlLexer.BEGIN) {
                this.programKind = ProgramKind.ANONYMOUS;
                this.transactionCandidate = true;
            } else if (type == DmSqlLexer.LBRACE) {
                this.programKind = ProgramKind.CLASS;
            }
        }

        if (this.createStatement && !this.rootObjectSeen) {
            ProgramKind detected = createdProgramKind(type);
            if (detected != ProgramKind.NONE) {
                this.programKind = detected;
                this.rootObjectSeen = true;
                rootDetected = true;
            } else if (!isCreatePreamble(type)) {
                // A later FUNCTION token can be an attribute of another object
                // (for example CREATE OPERATOR (... FUNCTION ...)).
                this.createStatement = false;
            }
        } else if (this.programKind != ProgramKind.NONE && !this.rootEnded && !this.rootObjectSeen
                   && (type == DmSqlLexer.FUNCTION || type == DmSqlLexer.PROCEDURE)) {
            this.rootObjectSeen = true;
        }

        if (type == DmSqlLexer.WITH) {
            this.inlineWithPending = true;
        } else if (this.inlineWithPending && type == DmSqlLexer.RECURSIVE) {
            // WITH RECURSIVE FUNCTION/PROCEDURE keeps the inline-program marker alive.
        } else if (this.inlineWithPending && (type == DmSqlLexer.FUNCTION || type == DmSqlLexer.PROCEDURE)) {
            if (this.programKind == ProgramKind.NONE) {
                this.programKind = ProgramKind.INLINE;
                this.rootObjectSeen = true;
                rootDetected = true;
                this.inlineClause = true;
            }
            this.inlineWithPending = false;
        } else if (this.inlineClause && this.programKind == ProgramKind.NONE
                   && (type == DmSqlLexer.FUNCTION || type == DmSqlLexer.PROCEDURE)) {
            this.programKind = ProgramKind.INLINE;
            this.rootObjectSeen = true;
            rootDetected = true;
        } else if (this.inlineWithPending) {
            this.inlineWithPending = false;
        }

        if (!rootDetected && isContainerProgram() && !this.rootEnded && this.beginFrames.isEmpty()
            && (type == DmSqlLexer.FUNCTION || type == DmSqlLexer.PROCEDURE) && this.visibleTokens > 1) {
            this.nestedRoutinePending = true;
        } else if (!rootDetected && isRoutineProgram() && !this.rootEnded && this.beginFrames.isEmpty()
                   && (type == DmSqlLexer.FUNCTION || type == DmSqlLexer.PROCEDURE) && this.visibleTokens > 0) {
            this.nestedRoutinePending = true;
        }

        if (this.nestedRoutinePending && (type == DmSqlLexer.AS || type == DmSqlLexer.IS)) {
            this.nestedRoutineDefinition = true;
        }

        if (type == DmSqlLexer.BODY && this.programKind == ProgramKind.TYPE) {
            this.typeBody = true;
        }
        if (type == DmSqlLexer.EXTERNAL || type == DmSqlLexer.LANGUAGE) {
            this.externalDefinition = true;
        } else if (type == DmSqlLexer.AGGREGATE) {
            this.aggregateDefinition = true;
        }

        if (type == DmSqlLexer.BEGIN) {
            boolean nested = this.nestedRoutinePending || !this.beginFrames.isEmpty() && this.beginFrames.peek();
            this.beginFrames.push(nested);
            this.nestedRoutinePending = false;
            this.nestedRoutineDefinition = false;
        } else if (isControl(type) && !this.beginFrames.isEmpty() && this.lastType != DmSqlLexer.END) {
            this.controlDepth++;
        } else if (type == DmSqlLexer.END) {
            acceptEnd();
        } else if (type == DmSqlLexer.LBRACE && this.programKind == ProgramKind.CLASS) {
            this.braceDepth++;
        } else if (type == DmSqlLexer.RBRACE && this.programKind == ProgramKind.CLASS && this.braceDepth > 0) {
            this.braceDepth--;
            this.rootEnded |= this.braceDepth == 0;
        }

        if (this.transactionCandidate && this.visibleTokens > 0 && type != DmSqlLexer.BEGIN) {
            this.transactionCandidate = false;
        }
        this.lastType = type;
        this.visibleTokens++;
    }

    private ProgramKind createdProgramKind(int type) {
        return switch (type) {
            case DmSqlLexer.FUNCTION, DmSqlLexer.PROCEDURE, DmSqlLexer.TRIGGER -> ProgramKind.ROUTINE;
            case DmSqlLexer.PACKAGE -> ProgramKind.PACKAGE;
            case DmSqlLexer.TYPE -> ProgramKind.TYPE;
            case DmSqlLexer.CLASS -> ProgramKind.CLASS;
            case DmSqlLexer.SCHEMA -> ProgramKind.SCHEMA;
            default -> ProgramKind.NONE;
        };
    }

    private static boolean isCreatePreamble(int type) {
        return type == DmSqlLexer.CREATE || type == DmSqlLexer.OR || type == DmSqlLexer.REPLACE
               || type == DmSqlLexer.JAVA_LANGUAGE || type == DmSqlLexer.PUBLIC || type == DmSqlLexer.FINAL
               || type == DmSqlLexer.ABSTRACT;
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
        if (this.programKind == ProgramKind.PACKAGE || this.programKind == ProgramKind.CLASS || this.programKind == ProgramKind.SCHEMA
            || this.programKind == ProgramKind.TYPE && this.typeBody || this.programKind == ProgramKind.DECLARE) {
            this.rootEnded = true;
        }
    }

    private boolean canEndAtSemicolon() {
        if (this.transactionCandidate && this.visibleTokens == 1) {
            return true;
        }
        if (!this.beginFrames.isEmpty() || this.controlDepth > 0 || this.braceDepth > 0) {
            return false;
        }
        if (this.programKind == ProgramKind.NONE) {
            return true;
        }
        if (this.programKind == ProgramKind.INLINE) {
            return false;
        }
        if (this.programKind == ProgramKind.SCHEMA) {
            return false;
        }
        if (this.programKind == ProgramKind.PACKAGE || this.programKind == ProgramKind.CLASS) {
            return this.rootEnded;
        }
        if (this.programKind == ProgramKind.TYPE && !this.typeBody) {
            return true;
        }
        return this.externalDefinition || this.aggregateDefinition || this.rootEnded;
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
            this.aggregateDefinition = false;
        }
    }

    private boolean isRoutineProgram() {
        return this.programKind == ProgramKind.ROUTINE || this.programKind == ProgramKind.DECLARE || this.programKind == ProgramKind.INLINE;
    }

    private boolean isContainerProgram() {
        return this.programKind == ProgramKind.PACKAGE || this.programKind == ProgramKind.CLASS || this.programKind == ProgramKind.SCHEMA
               || this.programKind == ProgramKind.TYPE && this.typeBody;
    }

    private static boolean isControl(int type) {
        // FOR and WHILE both open their body at LOOP. Counting WHILE here as
        // well would leave one phantom level after END LOOP and merge the next
        // top-level statement into the current routine.
        return type == DmSqlLexer.IF || type == DmSqlLexer.LOOP || type == DmSqlLexer.CASE;
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
        this.aggregateDefinition = false;
        this.rootEnded = false;
        this.transactionCandidate = false;
        this.inlineWithPending = false;
        this.inlineClause = false;
        this.typeBody = false;
        this.controlDepth = 0;
        this.braceDepth = 0;
        this.visibleTokens = 0;
        this.lastType = Token.INVALID_TYPE;
    }
}
