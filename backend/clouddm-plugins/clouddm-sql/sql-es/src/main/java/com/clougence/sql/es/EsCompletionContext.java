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
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import com.clougence.sql.es.antlr.EsLexer;

/** Completion tolerates unfinished input, using the same lexer as execution. Offsets here are UTF-16. */
public record EsCompletionContext(EsCompletionKind kind, String prefix, String index, String parent, int start, int end, boolean quoted) {
    public static EsCompletionContext at(String text, int offset) {
        String before = text.substring(0, offset);
        EsLexer lexer = new EsLexer(CharStreams.fromString(before));
        List<? extends Token> tokens = lexer.getAllTokens();
        int header = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == EsLexer.METHOD) {
                header = i;
            }
        }
        int lineStart = before.lastIndexOf('\n') + 1;
        String line = before.substring(lineStart).stripLeading();
        int start = offset - line.length();
        if (line.matches("[A-Z]*") && lexer._mode == EsLexer.DEFAULT_MODE) {
            return new EsCompletionContext(EsCompletionKind.METHOD, line, null, "", start, wordEnd(text, offset), false);
        }
        if (header < 0 || tokens.get(tokens.size() - 1).getType() == EsLexer.COMMENT) {
            return none(offset);
        }
        if (lexer._mode == EsLexer.HEADER) {
            return header(text, offset, before, tokens, header);
        }
        if (lexer._mode != EsLexer.JSON) {
            return none(offset);
        }
        return body(text, offset, before);
    }

    private static EsCompletionContext header(String text, int offset, String before, List<? extends Token> tokens, int header) {
        List<? extends Token> target = tokens.subList(header + 1, tokens.size());
        if (target.stream().anyMatch(token -> token.getType() == EsLexer.HCOMMENT)) {
            return none(offset);
        }
        String headerText = before.substring(before.offsetByCodePoints(0, tokens.get(header).getStartIndex()));
        int space = tokens.get(header).getText().length();
        if (headerText.length() == space) {
            return none(offset);
        }
        String path = headerText.substring(space).stripLeading();
        int pathStart = offset - path.length();
        if (path.contains(" ") || path.contains("\t") || path.startsWith("//")) {
            return none(offset);
        }
        int question = path.indexOf('?');
        if (question >= 0) {
            int parameterStart = Math.max(question, path.lastIndexOf('&')) + 1;
            String prefix = path.substring(parameterStart);
            if (prefix.contains("=")) {
                return none(offset);
            }
            return new EsCompletionContext(EsCompletionKind.PARAMETER, prefix, null, path.substring(0, question), pathStart + parameterStart, wordEnd(text, offset), false);
        }
        int slash = path.lastIndexOf('/');
        if (slash <= 0) {
            int prefixStart = path.startsWith("/") ? 1 : 0;
            return new EsCompletionContext(EsCompletionKind.INDEX, path.substring(prefixStart), null, path, pathStart + prefixStart, wordEnd(text, offset), false);
        }
        if (path.indexOf('/', 1) == slash) {
            return new EsCompletionContext(EsCompletionKind.ENDPOINT,
                path.substring(slash + 1),
                null,
                headerText.substring(0, space),
                pathStart + slash + 1,
                wordEnd(text, offset),
                false);
        }
        return none(offset);
    }

    private static EsCompletionContext body(String text, int offset, String before) {
        // An appended quote lets ANTLR recognize the unfinished string without decoding or rounding its contents.
        List<? extends Token> completed = new EsLexer(CharStreams.fromString(before + '"')).getAllTokens();
        List<Token> significant = new ArrayList<>();
        String prefix = "";
        boolean quoted = false;
        int start = offset;
        int pointOffset = before.codePointCount(0, before.length());
        for (Token token : completed) {
            if (token.getType() == EsLexer.STRING && token.getStopIndex() == pointOffset) {
                start = before.offsetByCodePoints(0, token.getStartIndex()) + 1;
                prefix = before.substring(start);
                quoted = true;
                break;
            }
            if (token.getStartIndex() >= pointOffset) {
                break;
            }
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                significant.add(token);
            }
        }
        List<String> parents = new ArrayList<>();
        List<Integer> containers = new ArrayList<>();
        String property = "";
        String index = null;
        for (int i = 0; i < significant.size(); i++) {
            Token token = significant.get(i);
            int type = token.getType();
            if (type == EsLexer.METHOD) {
                parents.clear();
                containers.clear();
                property = "";
                index = null;
                if (i + 3 < significant.size() && significant.get(i + 2).getType() == EsLexer.SLASH && significant.get(i + 3).getType() == EsLexer.TEXT) {
                    Token indexToken = significant.get(i + 3);
                    try {
                        index = EsUrlCodec.decode(indexToken.getText(), false, EsSourceRange.of(indexToken, indexToken));
                        new EsIndexReference(index, EsSourceRange.of(indexToken, indexToken));
                    } catch (EsParseException e) {
                        return none(offset);
                    }
                }
            } else if (type == EsLexer.LBRACE || type == EsLexer.LBRACKET) {
                if (property.isEmpty() && !containers.isEmpty() && containers.get(containers.size() - 1) == EsLexer.LBRACKET) {
                    property = parents.get(parents.size() - 1);
                }
                parents.add(property);
                containers.add(type);
                property = "";
            } else if (type == EsLexer.RBRACE || type == EsLexer.RBRACKET) {
                if (!parents.isEmpty()) {
                    parents.remove(parents.size() - 1);
                    containers.remove(containers.size() - 1);
                }
                property = "";
            } else if (type == EsLexer.STRING && i + 1 < significant.size() && significant.get(i + 1).getType() == EsLexer.COLON) {
                property = token.getText().substring(1, token.getText().length() - 1);
            }
        }
        if (containers.isEmpty()) {
            return none(offset);
        }
        String parent = parents.get(parents.size() - 1);
        int last = significant.get(significant.size() - 1).getType();
        boolean key = containers.get(containers.size() - 1) == EsLexer.LBRACE && (last == EsLexer.LBRACE || last == EsLexer.COMMA);
        EsCompletionKind kind = EsCompletionKind.NONE;
        if (key) {
            kind = EsCompletionKind.PROPERTY;
            if (Set.of("match", "match_phrase", "term", "terms", "range", "prefix", "wildcard", "sort").contains(parent)) {
                kind = EsCompletionKind.FIELD;
            }
        } else if (Set.of("field", "fields", "_source", "sort", "includes", "excludes").contains(property)
                   || Set.of("fields", "_source", "sort", "includes", "excludes").contains(parent)) {
            kind = EsCompletionKind.FIELD;
        }
        int end = offset;
        if (quoted) {
            end = start;
            while (end < text.length() && text.charAt(end) != '"' && text.charAt(end) != '\n') {
                if (text.charAt(end) == '\\' && end + 1 < text.length()) {
                    end += 2;
                } else {
                    end++;
                }
            }
        }
        return new EsCompletionContext(kind, prefix, index, parent, start, end, quoted);
    }

    private static int wordEnd(String text, int offset) {
        while (offset < text.length() && " /?&=\t\r\n#".indexOf(text.charAt(offset)) < 0) {
            offset++;
        }
        return offset;
    }

    private static EsCompletionContext none(int offset) {
        return new EsCompletionContext(EsCompletionKind.NONE, "", null, "", offset, offset, false);
    }
}
