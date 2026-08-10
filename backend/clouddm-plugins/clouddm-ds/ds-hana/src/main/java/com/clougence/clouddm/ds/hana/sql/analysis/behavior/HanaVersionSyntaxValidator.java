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
package com.clougence.clouddm.ds.hana.sql.analysis.behavior;

import java.util.List;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.hana.sql.parser.HanaParserConfig;
import com.clougence.clouddm.ds.hana.sql.parser.HanaVersion;
import com.clougence.clouddm.ds.hana.sql.parser.antlr.HanaLexer;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;

final class HanaVersionSyntaxValidator {

    private static final String ENCRYPTION_KEYS_FUNCTION = "ENCRYPTION_ROOT_KEYS_EXTRACT_ALL_KEYS_FOR_DATABASE";

    private HanaVersionSyntaxValidator(){
    }

    static void validate(String sql, HanaParserConfig config, List<Token> tokens) {
        if (!config.grammarVersion().atMost(HanaVersion.HANA_1)) {
            return;
        }

        int offset = unsupportedFunctionClause(tokens, "JSON_VALUE", "JSON_QUERY");
        if (offset < 0) offset = unsupportedXmlTable(tokens);
        if (offset < 0) offset = unsupportedJsonTable(tokens);
        if (offset < 0) offset = unsupportedSimpleSyntax(tokens);
        if (offset < 0) offset = unsupportedAuditSyntax(tokens);
        if (offset < 0 && containsWord(tokens, ENCRYPTION_KEYS_FUNCTION)
                && (sql.indexOf('\u2018') >= 0 || sql.indexOf('\u2019') >= 0)) {
            offset = wordOffset(tokens, ENCRYPTION_KEYS_FUNCTION);
        }
        if (offset >= 0) {
            throw syntaxException(sql, offset);
        }
    }

    private static int unsupportedFunctionClause(List<Token> tokens, String... functions) {
        for (int index = 0; index + 1 < tokens.size(); index++) {
            if (!isAnyWord(tokens.get(index), functions) || !isSymbol(tokens.get(index + 1), "(")) continue;
            int close = matchingParenthesis(tokens, index + 1);
            if (close < 0) continue;
            int clause = jsonClause(tokens, index + 2, close);
            if (clause >= 0) return tokens.get(clause).getStartIndex();
        }
        return -1;
    }

    private static int unsupportedXmlTable(List<Token> tokens) {
        for (int index = 0; index + 1 < tokens.size(); index++) {
            if (!isWord(tokens.get(index), "XMLTABLE") || !isSymbol(tokens.get(index + 1), "(")) continue;
            int close = matchingParenthesis(tokens, index + 1);
            for (int cursor = index + 2; cursor >= 0 && cursor < close; cursor++) {
                if (isWord(tokens.get(cursor), "PASSING")) return tokens.get(cursor).getStartIndex();
            }
        }
        return -1;
    }

    private static int unsupportedJsonTable(List<Token> tokens) {
        for (int index = 0; index + 2 < tokens.size(); index++) {
            if (isWord(tokens.get(index), "JSON_TABLE") && isSymbol(tokens.get(index + 1), "(")
                    && tokens.get(index + 2).getType() == HanaLexer.STRING_LITERAL) {
                return tokens.get(index).getStartIndex();
            }
        }
        return -1;
    }

    private static int unsupportedSimpleSyntax(List<Token> tokens) {
        for (int index = 0; index < tokens.size(); index++) {
            if (matchesAt(tokens, index, tokens.size(), "BINARY", "(") && index + 3 < tokens.size()
                    && tokens.get(index + 2).getType() == HanaLexer.NUMBER && isSymbol(tokens.get(index + 3), ")")) {
                return tokens.get(index).getStartIndex();
            }
            if ((isWord(tokens.get(index), "CURRENT_TIMESTAMP")
                    || isWord(tokens.get(index), "CURRENT_UTCTIMESTAMP"))
                    && index + 1 < tokens.size() && isSymbol(tokens.get(index + 1), "(")) {
                return tokens.get(index).getStartIndex();
            }
            if (matchesAt(tokens, index, tokens.size(), "ALTER", "DATABASE")) {
                int end = statementEnd(tokens, index + 2);
                for (int cursor = index + 2; cursor + 1 < end; cursor++) {
                    if (isWord(tokens.get(cursor), "ADD")
                            && stringEquals(tokens.get(cursor + 1), "DOCSTORE")) {
                        return tokens.get(index).getStartIndex();
                    }
                }
            }
        }
        return -1;
    }

    private static int unsupportedAuditSyntax(List<Token> tokens) {
        for (int index = 0; index < tokens.size(); index++) {
            if (!matchesAt(tokens, index, tokens.size(), "CREATE", "AUDIT", "POLICY")) continue;
            int end = statementEnd(tokens, index + 3);
            for (int cursor = index + 3; cursor < end; cursor++) {
                if (matchesAt(tokens, cursor, end, "TRAIL", "TYPE", "TABLE")
                        || isWord(tokens.get(cursor), "JWT")
                        || isWord(tokens.get(cursor), "VALIDATE")
                        || isWord(tokens.get(cursor), "CLIENTSIDE")
                        || matchesAt(tokens, cursor, end, "SECURITY", "EVENT")
                        || matchesAt(tokens, cursor, end, "BACKUP", "DATA")
                        || matchesAt(tokens, cursor, end, "ALTER", "ROLE")) {
                    return tokens.get(index).getStartIndex();
                }
            }
        }
        return -1;
    }

    private static int jsonClause(List<Token> tokens, int start, int end) {
        for (int index = start; index < end; index++) {
            Token token = tokens.get(index);
            if (isWord(token, "RETURNING") || isWord(token, "DEFAULT")) return index;
            if ((isWord(token, "NULL") || isWord(token, "ERROR"))
                    && (matchesAt(tokens, index + 1, end, "ON", "EMPTY")
                    || matchesAt(tokens, index + 1, end, "ON", "ERROR"))) return index;
            if (isWord(token, "EMPTY")
                    && (matchesAt(tokens, index + 1, end, "ARRAY", "ON", "EMPTY")
                    || matchesAt(tokens, index + 1, end, "ARRAY", "ON", "ERROR")
                    || matchesAt(tokens, index + 1, end, "OBJECT", "ON", "EMPTY")
                    || matchesAt(tokens, index + 1, end, "OBJECT", "ON", "ERROR"))) return index;
            if (matchesAt(tokens, index, end, "WITHOUT", "WRAPPER")
                    || matchesAt(tokens, index, end, "WITH", "WRAPPER")
                    || matchesAt(tokens, index, end, "WITH", "ARRAY", "WRAPPER")
                    || matchesAt(tokens, index, end, "WITH", "CONDITIONAL", "WRAPPER")
                    || matchesAt(tokens, index, end, "WITH", "UNCONDITIONAL", "WRAPPER")
                    || matchesAt(tokens, index, end, "WITH", "CONDITIONAL", "ARRAY", "WRAPPER")
                    || matchesAt(tokens, index, end, "WITH", "UNCONDITIONAL", "ARRAY", "WRAPPER")) return index;
        }
        return -1;
    }

    private static int matchingParenthesis(List<Token> tokens, int open) {
        int depth = 0;
        for (int index = open; index < tokens.size(); index++) {
            if (isSymbol(tokens.get(index), "(")) {
                depth++;
            } else if (isSymbol(tokens.get(index), ")") && --depth == 0) {
                return index;
            }
        }
        return -1;
    }

    private static int statementEnd(List<Token> tokens, int start) {
        for (int index = start; index < tokens.size(); index++) {
            if (isSymbol(tokens.get(index), ";")) return index;
        }
        return tokens.size();
    }

    private static boolean matchesAt(List<Token> tokens, int start, int end, String... values) {
        if (start < 0 || start + values.length > end) return false;
        for (int index = 0; index < values.length; index++) {
            if (!tokens.get(start + index).getText().equalsIgnoreCase(values[index])) return false;
        }
        return true;
    }

    private static boolean isAnyWord(Token token, String... values) {
        for (String value : values) {
            if (isWord(token, value)) return true;
        }
        return false;
    }

    private static boolean isWord(Token token, String value) {
        return HanaTokenStream.isWord(token, value);
    }

    private static boolean isSymbol(Token token, String value) {
        return token.getText().equals(value);
    }

    private static boolean stringEquals(Token token, String value) {
        if (token.getType() != HanaLexer.STRING_LITERAL) return false;
        String text = token.getText();
        return text.length() == value.length() + 2
                && text.regionMatches(true, 1, value, 0, value.length());
    }

    private static boolean containsWord(List<Token> tokens, String value) {
        return wordOffset(tokens, value) >= 0;
    }

    private static int wordOffset(List<Token> tokens, String value) {
        for (Token token : tokens) {
            if (isWord(token, value)) return token.getStartIndex();
        }
        return -1;
    }

    private static AntlerSyntaxException syntaxException(String sql, int offset) {
        int line = 1;
        int column = 0;
        for (int i = 0; i < offset; i++) {
            if (sql.charAt(i) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return new AntlerSyntaxException(line, column, "syntax is not supported by SAP HANA 1.0");
    }

}
