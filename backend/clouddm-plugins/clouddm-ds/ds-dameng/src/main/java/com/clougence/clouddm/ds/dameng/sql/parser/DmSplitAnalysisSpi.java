/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlLexer;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.common.parser.SplitLexerFastPath;
import com.clougence.sql.common.parser.SplitLexerFastPath.CommentSyntax;

/** Dameng lexer-only SQL statement splitter. */
public class DmSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return DmDslProvider.INSTANCE;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new DmLexerSplitPolicy();
    }

    @Override
    protected Lexer createLexer(CharStream source) {
        return new SplitLexer(source);
    }

    private static final class SplitLexer extends DmSqlLexer {
        private SplitLexer(CharStream input){
            super(input);
        }

        @Override
        public Token nextToken() {
            Token token = SplitLexerFastPath.nextToken(this, DmSqlLexer.ID, DmSqlLexer.WS, CommentSyntax.STANDARD);
            return token != null ? token : super.nextToken();
        }
    }
}
