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
package com.clougence.clouddm.dsfamily.language.completion;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class CompletionStatementState {

    private final String        sqlText;
    private final BlockLocation range;
    private final int           cursorOffset;
    private final String        prefix;
    private final String        qualifier;
    private final char          previousSignificantChar;
    private final List<String>  tokensBeforeCursor;
    private final boolean       cursorInState;

    public CompletionStatementState(String sqlText, BlockLocation range, CompletionRequest request, Integer cursorLineNumber, Integer cursorColNumber,
                                    CompletionDialect dialect, boolean cursorInState){
        this.sqlText = StringUtils.toString(sqlText);
        this.range = range;
        this.cursorOffset = offsetOf(this.sqlText, cursorLineNumber, cursorColNumber);
        this.prefix = extractPrefix(request, this.sqlText, this.cursorOffset, dialect, cursorLineNumber, cursorColNumber);
        this.qualifier = extractQualifier(this.sqlText, this.cursorOffset, this.prefix, dialect);
        this.previousSignificantChar = previousSignificantChar(this.sqlText, this.cursorOffset, this.prefix);
        this.tokensBeforeCursor = tokenize(this.sqlText.substring(0, Math.min(this.cursorOffset, this.sqlText.length())), dialect);
        this.cursorInState = cursorInState;
    }

    public String previousToken() {
        return tokenFromEnd(0);
    }

    public String tokenFromEnd(int index) {
        int offset = tokensBeforeCursor.size() - 1 - index;
        return offset >= 0 ? tokensBeforeCursor.get(offset) : "";
    }

    public boolean matchPrefix(String value) {
        return StringUtils.isBlank(prefix) || StringUtils.toString(value).toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT));
    }

    public boolean hasQualifier() {
        return StringUtils.isNotBlank(qualifier);
    }

    private static String extractPrefix(CompletionRequest request, String sqlText, int offset, CompletionDialect dialect, Integer lineNumber, Integer colNumber) {
        if (StringUtils.isBlank(sqlText) || cursorAfterTrimmedWhitespace(sqlText, lineNumber, colNumber)) {
            return "";
        }
        if (offset <= 0 || Character.isWhitespace(sqlText.charAt(offset - 1))) {
            return "";
        }

        int start = offset;
        while (start > 0 && dialect.isIdentifierChar(sqlText.charAt(start - 1))) {
            start--;
        }
        return sqlText.substring(start, offset);
    }

    private static String extractQualifier(String sqlText, int offset, String prefix, CompletionDialect dialect) {
        int end = Math.clamp(offset - StringUtils.toString(prefix).length(), 0, sqlText.length());
        int dot = end - 1;
        while (dot >= 0 && Character.isWhitespace(sqlText.charAt(dot))) {
            dot--;
        }
        if (dot < 0 || sqlText.charAt(dot) != '.') {
            return "";
        }

        int start = dot;
        while (start > 0 && dialect.isIdentifierChar(sqlText.charAt(start - 1))) {
            start--;
        }
        return dialect.unquoteIdentifier(sqlText.substring(start, dot));
    }

    private static char previousSignificantChar(String sqlText, int offset, String prefix) {
        int index = Math.clamp(offset - StringUtils.toString(prefix).length(), 0, sqlText.length()) - 1;
        while (index >= 0 && Character.isWhitespace(sqlText.charAt(index))) {
            index--;
        }
        return index >= 0 ? sqlText.charAt(index) : 0;
    }

    private static boolean cursorAfterTrimmedWhitespace(String sqlText, Integer lineNumber, Integer colNumber) {
        if (lineNumber == null || colNumber == null) {
            return false;
        }

        String lineText = lineText(sqlText, Math.max(1, lineNumber));
        return lineText != null && Math.max(0, colNumber) > lineText.length();
    }

    static int offsetOf(String sqlText, Integer lineNumber, Integer colNumber) {
        if (lineNumber == null || colNumber == null) {
            return sqlText.length();
        }

        int targetLine = Math.max(1, lineNumber);
        int targetColumn = Math.max(0, colNumber);
        int line = 1;
        int column = 0;
        for (int i = 0; i < sqlText.length(); i++) {
            if (line == targetLine && column == targetColumn) {
                return i;
            }

            char c = sqlText.charAt(i);
            if (c == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return sqlText.length();
    }

    private static String lineText(String sqlText, int targetLine) {
        if (StringUtils.isBlank(sqlText)) {
            return null;
        }

        int line = 1;
        int start = 0;
        for (int i = 0; i < sqlText.length(); i++) {
            if (sqlText.charAt(i) != '\n') {
                continue;
            }

            if (line == targetLine) {
                return sqlText.substring(start, i).replace("\r", "");
            }
            line++;
            start = i + 1;
        }
        return line == targetLine ? sqlText.substring(start).replace("\r", "") : null;
    }

    public static List<String> tokenize(String text, CompletionDialect dialect) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }

        java.util.ArrayList<String> tokens = new java.util.ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (!dialect.isIdentifierChar(c)) {
                i++;
                continue;
            }

            int start = i;
            while (i < text.length() && dialect.isIdentifierChar(text.charAt(i))) {
                i++;
            }
            tokens.add(dialect.unquoteIdentifier(text.substring(start, i)));
        }
        return tokens;
    }
}
