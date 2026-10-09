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

import java.util.ArrayList;
import java.util.List;

import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.kafka.antlr.KafkaLexer;
import com.clougence.sql.kafka.antlr.KafkaParser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class KafkaAntlrStatementParser implements AntlrStatementParser {
    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        return new ArrayList<>(((KafkaParser) parser).script().command());
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        int previousStop = -1;
        if (lastTree != null) {
            previousStop = ((ParserRuleContext) lastTree).getStop().getTokenIndex();
        }
        return tokens.getText(scriptStart(tokens, previousStop, startToken), endToken);
    }

    public static Token scriptStart(TokenStream tokens, int previousStop, Token commandStart) {
        int index = previousStop + 1;
        while (index < commandStart.getTokenIndex()) {
            int type = tokens.get(index).getType();
            if (type != KafkaLexer.WS && type != KafkaLexer.EOL && type != KafkaLexer.SEMICOLON) {
                break;
            }
            index++;
        }
        // Keep the complete leading comment prefix: the shared streaming splitter
        // locates this contiguous text in its source window.
        return tokens.get(index);
    }
}
