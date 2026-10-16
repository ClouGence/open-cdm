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
import java.util.Optional;

import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.sql.kafka.antlr.KafkaLexer;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

public final class KafkaEditorParser {
    private KafkaEditorParser(){
    }

    // Editor splitting must retain invalid/incomplete statements so each can be
    // diagnosed. Boundaries and quote/comment handling still come from ANTLR.
    public static List<SplitScript> split(String text, int baseLine, int baseColumn) {
        CharStream source = CharStreams.fromString(text);
        KafkaLexer lexer = new KafkaLexer(source);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        List<SplitScript> scripts = new ArrayList<>();
        int previousStop = -1;
        Token start = null;
        Token stop = null;
        for (Token token : tokens.getTokens()) {
            int type = token.getType();
            if (type == KafkaLexer.EOL || type == KafkaLexer.SEMICOLON || type == Token.EOF) {
                if (start != null) {
                    start = KafkaAntlrStatementParser.scriptStart(tokens, previousStop, start);
                    previousStop = stop.getTokenIndex();
                    SplitScript script = new SplitScript();
                    script.setIndex(scripts.size());
                    String body = source.getText(Interval.of(start.getStartIndex(), stop.getStopIndex()));
                    script.setScript(body);
                    int line = Math.max(1, baseLine) + start.getLine() - 1;
                    int column = start.getCharPositionInLine();
                    if (start.getLine() == 1) {
                        column += Math.max(0, baseColumn);
                    }
                    script.setBodyStartCodeLine(line);
                    script.setBodyStartCodeColumn(column);
                    for (int i = 0; i < body.length(); i += Character.charCount(body.codePointAt(i))) {
                        if (body.charAt(i) == '\n') {
                            line++;
                            column = 0;
                        } else {
                            column++;
                        }
                    }
                    script.setBodyEndCodeLine(line);
                    script.setBodyEndCodeColumn(column);
                    scripts.add(script);
                    start = null;
                }
                if (type == Token.EOF) {
                    return scripts;
                }
            } else if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                if (start == null) {
                    start = token;
                }
                stop = token;
            }
        }
        return scripts;
    }

    public static Optional<List<KafkaToken>> completionTokens(String beforeCursor) {
        KafkaLexer lexer = new KafkaLexer(CharStreams.fromString(beforeCursor));
        List<KafkaToken> words = new ArrayList<>();
        boolean disabled = false;
        for (Token token : lexer.getAllTokens()) {
            int type = token.getType();
            if (type == KafkaLexer.EOL || type == KafkaLexer.SEMICOLON) {
                words.clear();
                disabled = false;
                continue;
            }
            if (type == KafkaLexer.COMMENT || type == KafkaLexer.SHELL_OPERATOR || type == KafkaLexer.INVALID) {
                disabled = true;
            }
            if (token.getChannel() != Token.DEFAULT_CHANNEL) {
                continue;
            }
            String value = KafkaWords.decode(token.getText());
            int start = token.getStartIndex();
            if (!words.isEmpty() && words.get(words.size() - 1).end() == start) {
                KafkaToken previous = words.remove(words.size() - 1);
                value = previous.value() + value;
                start = previous.start();
            }
            words.add(new KafkaToken(value, start, token.getStopIndex() + 1));
        }
        if (disabled) {
            return Optional.empty();
        }
        return Optional.of(words);
    }
}
