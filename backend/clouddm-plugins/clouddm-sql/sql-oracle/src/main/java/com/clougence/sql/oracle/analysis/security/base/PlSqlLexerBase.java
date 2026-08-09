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
package com.clougence.sql.oracle.analysis.security.base;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

public abstract class PlSqlLexerBase extends Lexer {

    public PlSqlLexerBase self;

    public PlSqlLexerBase(CharStream input){
        super(input);
        self = this;
    }

    protected boolean IsNewlineAtPos(int pos) {
        // The generated REM/PROMPT predicates call this after consuming the command prefix.
        // UnbufferedCharStream intentionally discards characters before the current token and
        // cannot service arbitrary negative LA calls. The token start column carries the exact
        // line-boundary fact needed by those predicates without looking behind the stream.
        if (pos < 0) {
            return this._tokenStartCharPositionInLine == 0;
        }
        int la = _input.LA(pos);
        return la == -1 || la == '\n';
    }
}
