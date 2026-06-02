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
package com.clougence.clouddm.dsfamily.mysql.language.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class MyCompletionContext {

    private final CompletionRequest request;
    private final String            sqlText;
    private final int               cursorOffset;
    private final String            prefix;
    private final String            qualifier;
    private final char              previousSignificantChar;
    private final List<String>      tokensBeforeCursor;

    public MyCompletionContext(CompletionRequest request){
        this.request = request;
        this.sqlText = StringUtils.toString(request.getSqlText());
        this.cursorOffset = offsetOf(this.sqlText, request.getCursorLineNumber(), request.getCursorColNumber());
        this.prefix = extractPrefix(request, this.sqlText, this.cursorOffset);
        this.qualifier = extractQualifier(this.sqlText, this.cursorOffset, this.prefix);
        this.previousSignificantChar = previousSignificantChar(this.sqlText, this.cursorOffset, this.prefix);
        this.tokensBeforeCursor = tokenize(this.sqlText.substring(0, Math.min(this.cursorOffset, this.sqlText.length())));
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

    private static String extractPrefix(CompletionRequest request, String sqlText, int offset) {
        if (StringUtils.isBlank(sqlText) || cursorAfterTrimmedWhitespace(sqlText, request.getCursorLineNumber(), request.getCursorColNumber())) {
            return "";
        }
        if (offset <= 0 || Character.isWhitespace(sqlText.charAt(offset - 1))) {
            return "";
        }

        int start = offset;
        while (start > 0 && isIdentChar(sqlText.charAt(start - 1))) {
            start--;
        }
        return sqlText.substring(start, offset);
    }

    private static String extractQualifier(String sqlText, int offset, String prefix) {
        int end = Math.clamp(offset - StringUtils.toString(prefix).length(), 0, sqlText.length());
        int dot = end - 1;
        while (dot >= 0 && Character.isWhitespace(sqlText.charAt(dot))) {
            dot--;
        }
        if (dot < 0 || sqlText.charAt(dot) != '.') {
            return "";
        }

        int start = dot;
        while (start > 0 && isIdentChar(sqlText.charAt(start - 1))) {
            start--;
        }
        return unquote(sqlText.substring(start, dot));
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

    private static int offsetOf(String sqlText, Integer lineNumber, Integer colNumber) {
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

    public static List<String> tokenize(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }

        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (!isIdentChar(c)) {
                i++;
                continue;
            }

            int start = i;
            while (i < text.length() && isIdentChar(text.charAt(i))) {
                i++;
            }
            tokens.add(unquote(text.substring(start, i)));
        }
        return tokens;
    }

    public static boolean isIdentChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$' || c == '`';
    }

    public static String unquote(String value) {
        String text = StringUtils.toString(value).trim();
        if (text.length() >= 2 && text.startsWith("`") && text.endsWith("`")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
