/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.parser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.misc.Pair;

/** Low-overhead tokenization for syntax that is unambiguous in a statement splitter. */
public final class SplitLexerFastPath {

    private static final Map<Class<?>, TokenTypes> TYPES = new ConcurrentHashMap<>();

    private SplitLexerFastPath() {
    }

    /** Returns a fast token, or {@code null} when the generated lexer must decide. */
    public static Token nextToken(Lexer lexer, int identifierType, int whitespaceType, CommentSyntax comments) {
        // Non-default modes own stateful constructs such as PostgreSQL dollar strings.
        if (lexer._mode != Lexer.DEFAULT_MODE) {
            return null;
        }
        CharStream input = lexer.getInputStream();
        int first = input.LA(1);
        if (first == IntStream.EOF) {
            return null;
        }
        TokenTypes types = TYPES.computeIfAbsent(lexer.getClass(), ignored -> tokenTypes(lexer.getVocabulary()));
        if (isWhitespace(first)) {
            return scan(lexer, whitespaceType, Token.HIDDEN_CHANNEL, SplitLexerFastPath::isWhitespace);
        }
        if (first == '\'' || first == '"' || first == '`') {
            return quoted(lexer, identifierType, first);
        }
        if (first == '-' && input.LA(2) == '-'
                && (!comments.dashRequiresWhitespace || isWhitespace(input.LA(3)) || input.LA(3) == IntStream.EOF)) {
            return lineComment(lexer, whitespaceType, 2);
        }
        if (comments.hash && first == '#') {
            return lineComment(lexer, whitespaceType, 1);
        }
        if (first == '/' && input.LA(2) == '*') {
            // Executable comments and optimizer hints are datasource syntax, not trivia.
            if (input.LA(3) == '!' || input.LA(3) == '+'
                    || isWordStart(input.LA(3)) && input.LA(4) == '!') {
                return null;
            }
            return blockComment(lexer, whitespaceType, comments.nestedBlocks);
        }
        if (isWordStart(first)) {
            int length = length(input, SplitLexerFastPath::isWordPart);
            StringBuilder word = new StringBuilder(length);
            for (int index = 1; index <= length; index++) {
                word.append((char) input.LA(index));
            }
            int type = types.words().getOrDefault(word.toString().toUpperCase(Locale.ROOT), identifierType);
            return consume(lexer, type, Token.DEFAULT_CHANNEL, length);
        }
        if (first >= '0' && first <= '9') {
            int length = length(input, value -> value >= '0' && value <= '9');
            int next = input.LA(length + 1);
            if (next != '.' && next != 'e' && next != 'E' && !isWordPart(next)) {
                return consume(lexer, identifierType, Token.DEFAULT_CHANNEL, length);
            }
            return null;
        }
        Integer type = types.singleCharacters().get(first);
        if (type != null) {
            return consume(lexer, type, Token.DEFAULT_CHANNEL, 1);
        }
        // Dollar-prefixed strings/parameters and non-ASCII starts remain datasource-owned.
        return first == '$' || first >= 0x80 ? null : consume(lexer, identifierType, Token.DEFAULT_CHANNEL, 1);
    }

    private static Token quoted(Lexer lexer, int tokenType, int quote) {
        CharStream input = lexer.getInputStream();
        int offset = 2;
        while (input.LA(offset) != IntStream.EOF) {
            int value = input.LA(offset++);
            // Backslash escaping is controlled by datasource/version/session settings.
            if (value == '\\') {
                return null;
            }
            if (value == quote) {
                if (input.LA(offset) == quote) {
                    offset++;
                } else {
                    return consume(lexer, tokenType, Token.DEFAULT_CHANNEL, offset - 1);
                }
            }
        }
        // Preserve each generated lexer's error and recovery behavior for unterminated text.
        return null;
    }

    private static Token lineComment(Lexer lexer, int whitespaceType, int prefixLength) {
        CharStream input = lexer.getInputStream();
        int length = prefixLength;
        while (input.LA(length + 1) != IntStream.EOF && input.LA(length + 1) != '\n') {
            length++;
        }
        return consume(lexer, whitespaceType, Token.HIDDEN_CHANNEL, length);
    }

    private static Token blockComment(Lexer lexer, int whitespaceType, boolean nested) {
        CharStream input = lexer.getInputStream();
        int length = 2;
        int depth = 1;
        while (input.LA(length + 1) != IntStream.EOF) {
            if (nested && input.LA(length + 1) == '/' && input.LA(length + 2) == '*') {
                depth++;
                length += 2;
                continue;
            }
            if (input.LA(length + 1) == '*' && input.LA(length + 2) == '/') {
                length += 2;
                if (--depth == 0) {
                    break;
                }
                continue;
            }
            length++;
        }
        return consume(lexer, whitespaceType, Token.HIDDEN_CHANNEL, length);
    }

    private static Token scan(Lexer lexer, int type, int channel, CharacterTest test) {
        return consume(lexer, type, channel, length(lexer.getInputStream(), test));
    }

    private static int length(CharStream input, CharacterTest test) {
        int length = 0;
        while (test.test(input.LA(length + 1))) {
            length++;
        }
        return length;
    }

    private static Token consume(Lexer lexer, int type, int channel, int length) {
        CharStream input = lexer.getInputStream();
        int marker = input.mark();
        int start = input.index();
        int line = lexer.getLine();
        int column = lexer.getCharPositionInLine();
        try {
            for (int index = 0; index < length; index++) {
                lexer.getInterpreter().consume(input);
            }
            return lexer.getTokenFactory().create(new Pair<>(lexer, input), type, null, channel,
                    start, input.index() - 1, line, column);
        } finally {
            input.release(marker);
        }
    }

    private static TokenTypes tokenTypes(Vocabulary vocabulary) {
        Map<String, Integer> words = new HashMap<>();
        Map<Integer, Integer> singleCharacters = new HashMap<>();
        for (int type = 1; type <= vocabulary.getMaxTokenType(); type++) {
            String literal = vocabulary.getLiteralName(type);
            if (literal != null && literal.length() >= 3 && literal.charAt(0) == '\''
                    && literal.charAt(literal.length() - 1) == '\'') {
                String text = literal.substring(1, literal.length() - 1);
                if (text.length() == 1 && isSafeSingleCharacter(text.charAt(0))) {
                    singleCharacters.put((int) text.charAt(0), type);
                } else if (text.chars().allMatch(SplitLexerFastPath::isWordPart)) {
                    words.putIfAbsent(text.toUpperCase(Locale.ROOT), type);
                }
            }
            String symbolic = vocabulary.getSymbolicName(type);
            // A punctuation token can have a word-like symbolic name (SEMI, COMMA,
            // SLASH). Never reinterpret an identifier with that spelling as punctuation.
            if (symbolic != null && symbolic.chars().allMatch(SplitLexerFastPath::isSymbolicWord)
                    && (literal == null || literal.length() >= 3
                    && literal.substring(1, literal.length() - 1).chars().allMatch(SplitLexerFastPath::isWordPart))) {
                words.putIfAbsent(normalizeSymbolic(symbolic), type);
            }
        }
        return new TokenTypes(words, singleCharacters);
    }

    private static String normalizeSymbolic(String symbolic) {
        String normalized = symbolic.toUpperCase(Locale.ROOT);
        for (String suffix : new String[] { "_KEYWORD", "_LITERAL", "_LANGUAGE", "_P" }) {
            if (normalized.endsWith(suffix)) {
                return normalized.substring(0, normalized.length() - suffix.length());
            }
        }
        return normalized;
    }

    private static boolean isWhitespace(int value) {
        return value == ' ' || value == '\t' || value == '\r' || value == '\n' || value == '\f' || value == 0x0b;
    }

    private static boolean isWordStart(int value) {
        return value >= 'a' && value <= 'z' || value >= 'A' && value <= 'Z' || value == '_';
    }

    private static boolean isWordPart(int value) {
        return isWordStart(value) || value >= '0' && value <= '9' || value == '$';
    }

    private static boolean isSymbolicWord(int value) {
        return value >= 'A' && value <= 'Z' || value >= '0' && value <= '9' || value == '_';
    }

    private static boolean isSafeSingleCharacter(char value) {
        return value != '\'' && value != '"' && value != '`' && value != '$' && value < 0x80;
    }

    private record TokenTypes(Map<String, Integer> words, Map<Integer, Integer> singleCharacters) {
    }

    /** Datasource-specific comment rules relevant to statement boundaries. */
    public enum CommentSyntax {
        MYSQL(true, true, false),
        STANDARD(false, false, false),
        POSTGRES(false, false, true);

        private final boolean hash;
        private final boolean dashRequiresWhitespace;
        private final boolean nestedBlocks;

        CommentSyntax(boolean hash, boolean dashRequiresWhitespace, boolean nestedBlocks) {
            this.hash = hash;
            this.dashRequiresWhitespace = dashRequiresWhitespace;
            this.nestedBlocks = nestedBlocks;
        }
    }

    @FunctionalInterface
    private interface CharacterTest {
        boolean test(int value);
    }
}
