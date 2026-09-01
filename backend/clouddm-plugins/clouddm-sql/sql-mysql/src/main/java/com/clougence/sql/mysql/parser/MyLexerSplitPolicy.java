/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.mysql.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.mysql.parser.antlr.MySqlLexer;

final class MyLexerSplitPolicy implements LexerSplitPolicy {

    private boolean definitionPrefix;
    private boolean programDefinition;
    private boolean programComplete;
    private boolean afterEnd;
    private boolean ifCandidate;
    private boolean ifNeedsDisambiguation;
    private int     blockDepth;
    private int     controlDepth;

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        int type = token.getType();
        if (type == MySqlLexer.DELIMITER_DIRECTIVE) {
            return LexerSplitBoundary.exclude(context.tokenStartOffset(token), context.tokenEndOffset(token));
        }
        boolean customDelimiter = type == MySqlLexer.SEMI && token.getChannel() == MySqlLexer.MYSQLCOMMENT;
        if (token.getChannel() != Token.DEFAULT_CHANNEL && !customDelimiter) {
            return null;
        }
        if (type == MySqlLexer.SEMI) {
            if (this.programDefinition && !this.programComplete && (this.blockDepth > 0 || this.controlDepth > 0)) {
                // END suffix keywords occur before their semicolon. A control keyword after
                // this delimiter starts a new frame instead of suffixing the preceding END.
                this.afterEnd = false;
                return null;
            }
            int start = context.tokenStartOffset(token);
            int end = context.tokenEndOffset(token);
            return customDelimiter ? LexerSplitBoundary.exclude(start, end) : LexerSplitBoundary.include(end);
        }

        accept(token);
        return null;
    }

    private void accept(Token token) {
        int type = token.getType();
        if (type == MySqlLexer.CREATE || type == MySqlLexer.ALTER) {
            this.definitionPrefix = true;
        } else if (this.definitionPrefix && (type == MySqlLexer.PROCEDURE || type == MySqlLexer.FUNCTION || type == MySqlLexer.TRIGGER || type == MySqlLexer.EVENT)) {
            this.programDefinition = true;
        }

        if (!this.programDefinition) {
            return;
        }
        if (this.ifNeedsDisambiguation && !isKeyword(token, MySqlLexer.IF, "IF")) {
            if (type == MySqlLexer.LR_BRACKET) {
                this.ifCandidate = false;
            }
            this.ifNeedsDisambiguation = false;
        }
        if (isKeyword(token, MySqlLexer.BEGIN, "BEGIN")) {
            this.blockDepth++;
            this.afterEnd = false;
        } else if (isKeyword(token, MySqlLexer.END, "END")) {
            if (this.controlDepth > 0) {
                this.controlDepth--;
            } else if (this.blockDepth > 0) {
                this.blockDepth--;
            }
            this.programComplete = this.blockDepth == 0 && this.controlDepth == 0;
            this.afterEnd = true;
        } else if (isKeyword(token, MySqlLexer.IF, "IF")) {
            this.ifCandidate = !this.afterEnd;
            this.ifNeedsDisambiguation = this.ifCandidate;
            this.afterEnd = false;
        } else if (this.ifCandidate && isKeyword(token, MySqlLexer.THEN, "THEN")) {
            this.controlDepth++;
            this.ifCandidate = false;
            this.afterEnd = false;
        } else if (isControl(token)) {
            if (!this.afterEnd) {
                this.controlDepth++;
            }
            this.afterEnd = false;
        } else {
            this.afterEnd = false;
        }
    }

    private static boolean isControl(Token token) {
        return isKeyword(token, MySqlLexer.LOOP, "LOOP") || isKeyword(token, MySqlLexer.WHILE, "WHILE")
            || isKeyword(token, MySqlLexer.REPEAT, "REPEAT") || isKeyword(token, MySqlLexer.CASE, "CASE");
    }

    private static boolean isKeyword(Token token, int keywordType, String keyword) {
        return token.getType() == keywordType || (token.getType() == MySqlLexer.ID && keyword.equalsIgnoreCase(token.getText()));
    }

    @Override
    public void reset() {
        this.definitionPrefix = false;
        this.programDefinition = false;
        this.programComplete = false;
        this.afterEnd = false;
        this.ifCandidate = false;
        this.ifNeedsDisambiguation = false;
        this.blockDepth = 0;
        this.controlDepth = 0;
    }
}
