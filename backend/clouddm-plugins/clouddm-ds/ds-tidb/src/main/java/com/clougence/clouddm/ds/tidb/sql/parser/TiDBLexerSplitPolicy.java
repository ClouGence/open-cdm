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
package com.clougence.clouddm.ds.tidb.sql.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBLexer;
import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** TiDB statement boundaries derived exclusively from TiDB lexer token types. */
final class TiDBLexerSplitPolicy implements LexerSplitPolicy {

    private enum ProgramKind {
        NONE, ROUTINE, TRIGGER, EVENT
    }

    private boolean definitionPrefix;
    private boolean programDefinition;
    private boolean bodyReady;
    private boolean afterEnd;
    private ProgramKind programKind = ProgramKind.NONE;
    private int     signatureParenDepth;
    private int     blockDepth;
    private int     controlDepth;

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL) {
            return null;
        }

        int type = token.getType();
        if (type == TiDBLexer.SEMI) {
            if (this.programDefinition && (this.blockDepth > 0 || this.controlDepth > 0)) {
                return null;
            }
            return LexerSplitBoundary.include(context.tokenEndOffset(token));
        }

        accept(type);
        return null;
    }

    private void accept(int type) {
        if (type == TiDBLexer.CREATE || type == TiDBLexer.ALTER) {
            this.definitionPrefix = true;
        } else if (this.definitionPrefix && isProgramObject(type)) {
            this.programDefinition = true;
            this.programKind = programKind(type);
            return;
        }

        if (!this.programDefinition) {
            return;
        }
        if (!this.bodyReady) {
            acceptHeader(type);
        }
        if (type == TiDBLexer.BEGIN) {
            this.bodyReady = true;
            this.blockDepth++;
            this.afterEnd = false;
        } else if (type == TiDBLexer.END) {
            if (this.controlDepth > 0) {
                this.controlDepth--;
            } else if (this.blockDepth > 0) {
                this.blockDepth--;
            }
            this.afterEnd = true;
        } else if (isControl(type)) {
            // The control keyword following END closes the construct; it does not open another.
            if (this.bodyReady && !this.afterEnd) {
                this.controlDepth++;
            }
            this.afterEnd = false;
        } else {
            this.afterEnd = false;
        }
    }

    private void acceptHeader(int type) {
        if (this.programKind == ProgramKind.ROUTINE) {
            if (type == TiDBLexer.LR_BRACKET) {
                this.signatureParenDepth++;
            } else if (type == TiDBLexer.RR_BRACKET && this.signatureParenDepth > 0) {
                this.signatureParenDepth--;
                this.bodyReady |= this.signatureParenDepth == 0;
            }
        } else if (this.programKind == ProgramKind.TRIGGER && type == TiDBLexer.ROW) {
            this.bodyReady = true;
        } else if (this.programKind == ProgramKind.EVENT && type == TiDBLexer.DO) {
            this.bodyReady = true;
        }
    }

    private static boolean isProgramObject(int type) {
        return type == TiDBLexer.PROCEDURE || type == TiDBLexer.FUNCTION || type == TiDBLexer.TRIGGER || type == TiDBLexer.EVENT;
    }

    private static ProgramKind programKind(int type) {
        if (type == TiDBLexer.TRIGGER) {
            return ProgramKind.TRIGGER;
        }
        if (type == TiDBLexer.EVENT) {
            return ProgramKind.EVENT;
        }
        return ProgramKind.ROUTINE;
    }

    private static boolean isControl(int type) {
        return type == TiDBLexer.IF || type == TiDBLexer.LOOP || type == TiDBLexer.WHILE || type == TiDBLexer.REPEAT || type == TiDBLexer.CASE;
    }

    @Override
    public void reset() {
        this.definitionPrefix = false;
        this.programDefinition = false;
        this.bodyReady = false;
        this.afterEnd = false;
        this.programKind = ProgramKind.NONE;
        this.signatureParenDepth = 0;
        this.blockDepth = 0;
        this.controlDepth = 0;
    }
}
