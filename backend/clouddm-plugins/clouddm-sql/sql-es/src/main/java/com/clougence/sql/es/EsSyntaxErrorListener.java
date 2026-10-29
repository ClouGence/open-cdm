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

import org.antlr.v4.runtime.*;

public class EsSyntaxErrorListener extends BaseErrorListener {
    public static final EsSyntaxErrorListener INSTANCE = new EsSyntaxErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int column, String msg, RecognitionException e) {
        Token token = (Token) offendingSymbol;
        throw new EsParseException(EsSourceRange.of(token, token), EsSqlI18nKeys.ES_SYNTAX_ERROR, line, column + 1, msg);
    }

    public static void install(Parser parser) {
        parser.removeErrorListeners();
        parser.addErrorListener(INSTANCE);
    }
}
