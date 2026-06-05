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
package com.clougence.clouddm.dsfamily.language.validate;

import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.DiagnosticSeverity;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.StringUtils;

public final class ValidateDiagnostics {

    private ValidateDiagnostics(){
    }

    public static Diagnostic syntaxError(AntlerSyntaxException e, String sqlText) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setSeverity(DiagnosticSeverity.ERROR);
        diagnostic.setMessage(syntaxErrorMessage(e));
        diagnostic.setRange(errorRange(e, sqlText));
        return diagnostic;
    }

    public static Diagnostic error(String message, BlockLocation range) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setSeverity(DiagnosticSeverity.ERROR);
        diagnostic.setMessage(message);
        diagnostic.setRange(range);
        return diagnostic;
    }

    public static BlockLocation tokenRange(String sqlText, String token, BlockLocation defaultRange) {
        if (StringUtils.isBlank(sqlText) || StringUtils.isBlank(token)) {
            return defaultRange;
        }

        int index = tokenIndex(sqlText, token, false);
        if (index < 0) {
            index = tokenIndex(sqlText, token, true);
        }
        if (index < 0) {
            index = sqlText.indexOf(token);
        }
        if (index < 0) {
            index = sqlText.toLowerCase().indexOf(token.toLowerCase());
        }
        if (index < 0) {
            return defaultRange;
        }

        CodeLocation start = positionAt(sqlText, index);
        CodeLocation end = positionAt(sqlText, index + token.length());
        BlockLocation range = new BlockLocation();
        range.setStartPosition(start);
        range.setEndPosition(end);
        return range;
    }

    private static int tokenIndex(String sqlText, String token, boolean ignoreCase) {
        String source = ignoreCase ? sqlText.toLowerCase() : sqlText;
        String target = ignoreCase ? token.toLowerCase() : token;
        int fromIndex = 0;
        while (fromIndex < source.length()) {
            int index = source.indexOf(target, fromIndex);
            if (index < 0) {
                return -1;
            }
            int end = index + target.length();
            if ((index == 0 || !isTokenChar(sqlText.charAt(index - 1))) && (end >= sqlText.length() || !isTokenChar(sqlText.charAt(end)))) {
                return index;
            }
            fromIndex = index + 1;
        }
        return -1;
    }

    private static String syntaxErrorMessage(AntlerSyntaxException e) {
        if (StringUtils.isNotBlank(e.getMessage())) {
            return e.getMessage();
        }

        return "SQL syntax error at line " + e.getLine() + ", column " + e.getColumn();
    }

    private static BlockLocation errorRange(AntlerSyntaxException e, String sqlText) {
        String token = offendingToken(e.getMessage());
        BlockLocation fallbackRange = eofFallbackRange(e, sqlText, token);
        if (fallbackRange != null) {
            return fallbackRange;
        }

        int startColumn = e.getColumn();
        int endColumn = startColumn + Math.max(1, token == null ? 1 : token.length());

        String lineText = lineText(sqlText, e.getLine());
        if (StringUtils.isNotBlank(lineText)) {
            int[] tokenRange = tokenRange(lineText, startColumn, token);
            startColumn = tokenRange[0];
            endColumn = tokenRange[1];
        }

        CodeLocation start = new CodeLocation(e.getLine(), startColumn);
        CodeLocation end = new CodeLocation(e.getLine(), endColumn);
        BlockLocation range = new BlockLocation();
        range.setStartPosition(start);
        range.setEndPosition(end);
        return range;
    }

    private static BlockLocation eofFallbackRange(AntlerSyntaxException e, String sqlText, String token) {
        if (!isEofToken(token) && StringUtils.isNotBlank(lineText(sqlText, e.getLine()))) {
            return null;
        }

        int offset = offsetAt(sqlText, e.getLine(), e.getColumn());
        int[] tokenRange = previousTokenRange(sqlText, offset);
        if (tokenRange == null) {
            return null;
        }

        BlockLocation range = new BlockLocation();
        range.setStartPosition(positionAt(sqlText, tokenRange[0]));
        range.setEndPosition(positionAt(sqlText, tokenRange[1]));
        return range;
    }

    private static boolean isEofToken(String token) {
        return "<EOF>".equals(token) || "EOF".equalsIgnoreCase(token);
    }

    private static String offendingToken(String message) {
        if (StringUtils.isBlank(message)) {
            return null;
        }

        String[] prefixes = { "mismatched input '", "extraneous input '" };
        for (String prefix : prefixes) {
            int start = message.indexOf(prefix);
            if (start < 0) {
                continue;
            }

            start += prefix.length();
            int end = message.indexOf('\'', start);
            if (end > start) {
                return message.substring(start, end);
            }
        }

        int atIndex = message.lastIndexOf(" at '");
        if (atIndex >= 0) {
            int start = atIndex + " at '".length();
            int end = message.indexOf('\'', start);
            if (end > start) {
                return message.substring(start, end);
            }
        }
        return null;
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

    private static int[] tokenRange(String lineText, int column, String token) {
        if (StringUtils.isNotBlank(token)) {
            int fromIndex = Math.clamp(column, 0, lineText.length());
            int tokenStart = lineText.indexOf(token, fromIndex);
            if (tokenStart < 0) {
                tokenStart = lineText.indexOf(token);
            }
            if (tokenStart >= 0) {
                return new int[] { tokenStart, tokenStart + token.length() };
            }
        }

        int start = Math.clamp(column, 0, lineText.length());
        while (start > 0 && isTokenChar(lineText.charAt(start - 1))) {
            start--;
        }

        int end = Math.clamp(column, 0, lineText.length());
        while (end < lineText.length() && isTokenChar(lineText.charAt(end))) {
            end++;
        }

        if (end <= start) {
            end = Math.min(lineText.length(), start + 1);
        }
        return new int[] { start, end };
    }

    private static CodeLocation positionAt(String sqlText, int offset) {
        int line = 1;
        int column = 0;
        int end = Math.clamp(offset, 0, sqlText.length());
        for (int i = 0; i < end; i++) {
            if (sqlText.charAt(i) == '\n') {
                line++;
                column = 0;
            } else if (sqlText.charAt(i) != '\r') {
                column++;
            }
        }
        return new CodeLocation(line, column);
    }

    private static int offsetAt(String sqlText, int targetLine, int targetColumn) {
        if (StringUtils.isBlank(sqlText)) {
            return 0;
        }

        int line = 1;
        int column = 0;
        int offset = 0;
        int normalizedLine = Math.max(1, targetLine);
        int normalizedColumn = Math.max(0, targetColumn);
        while (offset < sqlText.length()) {
            if (line == normalizedLine && column >= normalizedColumn) {
                return offset;
            }

            char c = sqlText.charAt(offset);
            offset++;
            if (c == '\n') {
                line++;
                column = 0;
            } else if (c != '\r') {
                column++;
            }
        }
        return sqlText.length();
    }

    private static int[] previousTokenRange(String sqlText, int offset) {
        if (StringUtils.isBlank(sqlText)) {
            return null;
        }

        int end = Math.clamp(offset, 0, sqlText.length());
        while (end > 0 && isIgnorableTrailing(sqlText.charAt(end - 1))) {
            end--;
        }
        if (end <= 0) {
            return null;
        }

        int start = end;
        if (isTokenChar(sqlText.charAt(end - 1))) {
            while (start > 0 && isTokenChar(sqlText.charAt(start - 1))) {
                start--;
            }
        } else {
            start--;
        }
        return new int[] { start, end };
    }

    private static boolean isIgnorableTrailing(char c) {
        return Character.isWhitespace(c) || c == ';';
    }

    private static boolean isTokenChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$';
    }
}
