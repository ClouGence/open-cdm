/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.hana.sql.parser.antlr.HanaLexer;

final class HanaTokenStream {

    private HanaTokenStream(){
    }

    static LexedSql lex(String sql) {
        HanaLexer lexer = new HanaLexer(CharStreams.fromString(sql));
        lexer.removeErrorListeners();
        List<? extends Token> generated = lexer.getAllTokens();
        List<Token> all = new ArrayList<>(generated.size());
        List<Token> visible = new ArrayList<>(generated.size());
        for (Token token : generated) {
            all.add(token);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) visible.add(token);
        }
        return new LexedSql(List.copyOf(all), List.copyOf(visible));
    }

    static List<String> words(List<Token> tokens, int limit) {
        List<String> result = new ArrayList<>(Math.min(tokens.size(), limit));
        for (Token token : tokens) {
            if (token.getType() == HanaLexer.WORD) {
                result.add(token.getText().toUpperCase(java.util.Locale.ROOT));
                if (result.size() == limit) break;
            }
        }
        return result;
    }

    static boolean isWord(Token token, String value) {
        return token.getType() == HanaLexer.WORD && token.getText().equalsIgnoreCase(value);
    }

    static boolean isType(Token token, int type) {
        return token.getType() == type;
    }

    record LexedSql(List<Token> all, List<Token> visible) {
    }
}
