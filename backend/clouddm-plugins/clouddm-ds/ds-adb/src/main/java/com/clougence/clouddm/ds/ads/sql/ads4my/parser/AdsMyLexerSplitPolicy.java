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
package com.clougence.clouddm.ds.ads.sql.ads4my.parser;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyLexer;
import com.clougence.sql.common.parser.LexerSplitBoundary;
import com.clougence.sql.common.parser.LexerSplitContext;
import com.clougence.sql.common.parser.LexerSplitPolicy;

final class AdsMyLexerSplitPolicy implements LexerSplitPolicy {
    private boolean prefix;
    private boolean program;
    private boolean complete;
    private boolean afterEnd;
    private int     blocks;
    private int     controls;

    @Override
    public LexerSplitBoundary boundary(Token token, LexerSplitContext context) {
        if (token.getChannel() != Token.DEFAULT_CHANNEL)
            return null;
        int type = token.getType();
        if (type == AdsMyLexer.SEMI) {
            if (program && !complete && (blocks > 0 || controls > 0))
                return null;
            return LexerSplitBoundary.include(context.tokenEndOffset(token));
        }
        if (type == AdsMyLexer.CREATE || type == AdsMyLexer.ALTER)
            prefix = true;
        else if (prefix && (type == AdsMyLexer.PROCEDURE || type == AdsMyLexer.FUNCTION || type == AdsMyLexer.TRIGGER || type == AdsMyLexer.EVENT))
            program = true;
        acceptProgram(type);
        return null;
    }

    private void acceptProgram(int type) {
        if (!program)
            return;
        if (type == AdsMyLexer.BEGIN) {
            blocks++;
            afterEnd = false;
        } else if (type == AdsMyLexer.END) {
            if (controls > 0)
                controls--;
            else if (blocks > 0)
                blocks--;
            complete = blocks == 0 && controls == 0;
            afterEnd = true;
        } else if (type == AdsMyLexer.IF || type == AdsMyLexer.LOOP || type == AdsMyLexer.WHILE || type == AdsMyLexer.REPEAT || type == AdsMyLexer.CASE) {
            if (!afterEnd && blocks > 0)
                controls++;
            afterEnd = false;
        } else
            afterEnd = false;
    }

    @Override
    public void reset() {
        prefix = program = complete = afterEnd = false;
        blocks = controls = 0;
    }
}
