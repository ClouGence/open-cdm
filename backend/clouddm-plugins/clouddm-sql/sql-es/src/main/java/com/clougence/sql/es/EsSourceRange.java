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
package com.clougence.sql.es;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/** Offsets and columns count Unicode code points; end positions are exclusive. Lines are one based. */
public record EsSourceRange(int start, int end, int startLine, int startColumn, int endLine, int endColumn) {
    public static EsSourceRange of(ParserRuleContext context) {
        return of(context.getStart(), context.getStop());
    }

    public static EsSourceRange of(Token start, Token stop) {
        int line = stop.getLine();
        int column = stop.getCharPositionInLine();
        if (stop.getType() != Token.EOF) {
            for (int codePoint : stop.getText().codePoints().toArray()) {
                if (codePoint == '\n') {
                    line++;
                    column = 0;
                } else {
                    column++;
                }
            }
        }
        return new EsSourceRange(start.getStartIndex(), stop.getStopIndex() + 1, start.getLine(), start.getCharPositionInLine(), line, column);
    }
}
