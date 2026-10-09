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
package com.clougence.sql.kafka;

import com.clougence.sql.kafka.antlr.KafkaLexer;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

public class KafkaSyntaxErrorListener extends BaseErrorListener {
    public static final KafkaSyntaxErrorListener INSTANCE = new KafkaSyntaxErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int column, String msg, RecognitionException e) {
        Token token = (Token) offendingSymbol;
        String key = KafkaSqlI18nKeys.KAFKA_SYNTAX_ERROR;
        if (token.getType() == KafkaLexer.UNFINISHED_WORD) {
            key = KafkaSqlI18nKeys.KAFKA_UNFINISHED;
        } else if (token.getType() == KafkaLexer.SHELL_OPERATOR) {
            key = KafkaSqlI18nKeys.KAFKA_SHELL_UNSUPPORTED;
        }
        throw new KafkaParseException(token.getStartIndex(), token.getStopIndex() + 1, key, msg);
    }
}
