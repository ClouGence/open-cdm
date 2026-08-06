/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

final class TiBehaviorText {

    private TiBehaviorText(){
    }

    static int skipWhitespace(String text, int start) {
        int index = start;
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
        return index;
    }

    static boolean startsWithWord(String text, int start, String word) {
        int end = start + word.length();
        if (start < 0 || end > text.length() || !text.regionMatches(true, start, word, 0, word.length())) {
            return false;
        }
        return (start == 0 || !isWordPart(text.charAt(start - 1))) && (end == text.length() || !isWordPart(text.charAt(end)));
    }

    static int afterStartingWords(String text, String... words) {
        int index = skipWhitespace(text, 0);
        for (int i = 0; i < words.length; i++) {
            if (!startsWithWord(text, index, words[i])) {
                return -1;
            }
            index += words[i].length();
            if (i < words.length - 1) {
                int next = skipWhitespace(text, index);
                if (next == index) {
                    return -1;
                }
                index = next;
            }
        }
        return index;
    }

    static boolean containsWords(String text, String first, String second) {
        int searchFrom = 0;
        while (true) {
            int firstStart = findWord(text, searchFrom, first);
            if (firstStart < 0) {
                return false;
            }
            int secondStart = skipWhitespace(text, firstStart + first.length());
            if (secondStart > firstStart + first.length() && startsWithWord(text, secondStart, second)) {
                return true;
            }
            searchFrom = firstStart + first.length();
        }
    }

    static int findWord(String text, int start, String... words) {
        int index = Math.max(0, start);
        while (index < text.length()) {
            while (index < text.length() && !isWordPart(text.charAt(index))) {
                index++;
            }
            if (index >= text.length()) {
                return -1;
            }
            for (String word : words) {
                if (startsWithWord(text, index, word)) {
                    return index;
                }
            }
            while (index < text.length() && isWordPart(text.charAt(index))) {
                index++;
            }
        }
        return -1;
    }

    static int wordEnd(String text, int start) {
        int index = start;
        while (index < text.length() && isWordPart(text.charAt(index))) {
            index++;
        }
        return index;
    }

    static TiBehaviorTextSpan nextIdentifier(String text, int start, int end, boolean allowStar) {
        int index = Math.max(0, start);
        int limit = Math.min(text.length(), end);
        while (index < limit) {
            TiBehaviorTextSpan span = identifierAt(text, index, limit, allowStar);
            if (span != null) {
                return span;
            }
            index++;
        }
        return null;
    }

    static TiBehaviorTextSpan identifierAt(String text, int start, int end, boolean allowStar) {
        int limit = Math.min(text.length(), end);
        int firstEnd = identifierPartEnd(text, start, limit, allowStar);
        if (firstEnd < 0) {
            return null;
        }
        int resultEnd = firstEnd;
        int dot = skipWhitespace(text, firstEnd);
        if (dot < limit && text.charAt(dot) == '.') {
            int secondStart = skipWhitespace(text, dot + 1);
            int secondEnd = identifierPartEnd(text, secondStart, limit, allowStar);
            if (secondEnd >= 0) {
                resultEnd = secondEnd;
            }
        }
        return new TiBehaviorTextSpan(start, resultEnd);
    }

    static TiBehaviorTextSpan nextQuoted(String text, int start) {
        for (int index = Math.max(0, start); index < text.length(); index++) {
            char quote = text.charAt(index);
            if (quote != '\'' && quote != '"') {
                continue;
            }
            for (int cursor = index + 1; cursor < text.length(); cursor++) {
                char value = text.charAt(cursor);
                if (value == '\\') {
                    cursor++;
                } else if (value == quote) {
                    if (cursor + 1 < text.length() && text.charAt(cursor + 1) == quote) {
                        cursor++;
                    } else {
                        return new TiBehaviorTextSpan(index, cursor + 1);
                    }
                }
            }
            return null;
        }
        return null;
    }

    static String normalizeQualifiedName(String value) {
        StringBuilder result = new StringBuilder(value.length());
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (current == '`') {
                continue;
            }
            if (Character.isWhitespace(current)) {
                int next = skipWhitespace(value, index);
                char before = result.length() == 0 ? 0 : result.charAt(result.length() - 1);
                char after = next < value.length() ? value.charAt(next) : 0;
                if (before == '.' || after == '.') {
                    index = next - 1;
                    continue;
                }
            }
            result.append(current);
        }
        return result.toString();
    }

    static java.util.List<String> qualifiedNameParts(String value) {
        java.util.List<String> result = new java.util.ArrayList<>();
        int start = 0;
        char quote = 0;
        for (int index = 0; index <= value.length(); index++) {
            char current = index < value.length() ? value.charAt(index) : '.';
            if (current == '`') {
                if (quote == 0) {
                    quote = current;
                } else if (index + 1 < value.length() && value.charAt(index + 1) == '`') {
                    index++;
                } else {
                    quote = 0;
                }
            } else if (current == '.' && quote == 0) {
                String part = value.substring(start, index).strip();
                if (!part.isEmpty()) {
                    if (part.length() >= 2 && part.charAt(0) == '`' && part.charAt(part.length() - 1) == '`') {
                        part = part.substring(1, part.length() - 1).replace("``", "`");
                    }
                    result.add(part);
                }
                start = index + 1;
            }
        }
        return result;
    }

    static String collapseSlashes(String value) {
        StringBuilder result = new StringBuilder(value.length());
        boolean slash = false;
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (current == '/') {
                if (slash) {
                    continue;
                }
                slash = true;
            } else {
                slash = false;
            }
            result.append(current);
        }
        return result.toString();
    }

    static String firstWhitespaceToken(String value) {
        if (value == null) {
            return null;
        }
        int start = skipWhitespace(value, 0);
        int end = start;
        while (end < value.length() && !Character.isWhitespace(value.charAt(end))) {
            end++;
        }
        return value.substring(start, end);
    }

    static boolean isIdentifierStart(char value) {
        return value == '_' || value == '$' || isAsciiLetter(value);
    }

    static boolean isIdentifierPart(char value) {
        return isIdentifierStart(value) || isAsciiDigit(value) || value == '$';
    }

    private static boolean isWordPart(char value) {
        return isAsciiLetter(value) || isAsciiDigit(value) || value == '_';
    }

    private static boolean isAsciiLetter(char value) {
        return value >= 'A' && value <= 'Z' || value >= 'a' && value <= 'z';
    }

    private static boolean isAsciiDigit(char value) {
        return value >= '0' && value <= '9';
    }

    private static int identifierPartEnd(String text, int start, int end, boolean allowStar) {
        if (start < 0 || start >= end) {
            return -1;
        }
        char first = text.charAt(start);
        if (allowStar && first == '*') {
            return start + 1;
        }
        if (first == '`') {
            for (int index = start + 1; index < end; index++) {
                if (text.charAt(index) != '`') {
                    continue;
                }
                if (index + 1 < end && text.charAt(index + 1) == '`') {
                    index++;
                } else {
                    return index + 1;
                }
            }
            return -1;
        }
        if (!isIdentifierStart(first)) {
            return -1;
        }
        int index = start + 1;
        while (index < end && isIdentifierPart(text.charAt(index))) {
            index++;
        }
        return index;
    }
}
