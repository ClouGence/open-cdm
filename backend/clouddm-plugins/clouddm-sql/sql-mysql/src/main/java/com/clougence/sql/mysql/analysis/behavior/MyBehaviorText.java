/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.behavior;

final class MyBehaviorText {

    private MyBehaviorText(){
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

    static boolean isIdentifierStart(char value) {
        return value == '_' || isAsciiLetter(value);
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
}
