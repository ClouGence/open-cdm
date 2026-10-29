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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.clougence.sql.es.antlr.EsLexer;
import com.clougence.sql.es.antlr.EsParser;

/** Syntax-only editor operations also work on requests rejected by execution policy. */
public final class EsEditorParser {
    private EsEditorParser(){
    }

    public static List<EsRequest> parse(String text) {
        EsParser parser = new EsParser(new CommonTokenStream(new EsLexer(CharStreams.fromString(text))));
        EsSyntaxErrorListener.install(parser);
        List<EsRequest> requests = new ArrayList<>();
        for (EsParser.RequestContext context : parser.script().request()) {
            requests.add(EsAstBuilder.from(context, parser.getTokenStream().getText(context)));
        }
        return requests;
    }

    public static String format(String text) {
        List<EsRequest> requests = parse(text);
        StringBuilder result = new StringBuilder();
        int offset = 0;
        for (EsRequest request : requests) {
            if (request.body() == null) {
                continue;
            }
            int start = text.offsetByCodePoints(0, request.body().range().start());
            int end = text.offsetByCodePoints(0, request.body().range().end());
            result.append(text, offset, start);
            result.append(formatBody(text.substring(start, end)));
            offset = end;
        }
        return result.append(text, offset, text.length()).toString();
    }

    private static String formatBody(String body) {
        var tokens = new EsLexer(CharStreams.fromString(body)).getAllTokens().stream().filter(token -> token.getChannel() == Token.DEFAULT_CHANNEL).toList();
        StringBuilder result = new StringBuilder();
        int depth = 0;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            int type = token.getType();
            if (type == EsLexer.LBRACE || type == EsLexer.LBRACKET) {
                result.append(token.getText());
                depth++;
                int next = tokens.get(i + 1).getType();
                if (next != EsLexer.RBRACE && next != EsLexer.RBRACKET) {
                    result.append('\n').append("    ".repeat(depth));
                }
            } else if (type == EsLexer.RBRACE || type == EsLexer.RBRACKET) {
                depth--;
                int previous = tokens.get(i - 1).getType();
                if (previous != EsLexer.LBRACE && previous != EsLexer.LBRACKET) {
                    result.append('\n').append("    ".repeat(depth));
                }
                result.append(token.getText());
            } else if (type == EsLexer.COMMA) {
                result.append(",\n").append("    ".repeat(depth));
            } else if (type == EsLexer.COLON) {
                result.append(": ");
            } else {
                result.append(token.getText());
            }
        }
        return result.toString();
    }
}
