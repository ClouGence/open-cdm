/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.oracle.parser;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.oracle.parser.antlr.PlSqlLexer;

/** Oracle lexer-only SQL statement splitter. */
public class OraSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return OraDslProvider.INSTANCE;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new OraLexerSplitPolicy(new OraLexerSplitPolicy.TokenTypes(//
                PlSqlLexer.CREATE, PlSqlLexer.FUNCTION, PlSqlLexer.PROCEDURE, PlSqlLexer.TRIGGER, PlSqlLexer.PACKAGE,
                PlSqlLexer.TYPE, PlSqlLexer.BODY, PlSqlLexer.DECLARE, PlSqlLexer.BEGIN, PlSqlLexer.END, PlSqlLexer.IF,
                PlSqlLexer.LOOP, PlSqlLexer.WHILE, PlSqlLexer.CASE, PlSqlLexer.WITH, PlSqlLexer.EXTERNAL,
                PlSqlLexer.LANGUAGE, PlSqlLexer.AS, PlSqlLexer.IS, PlSqlLexer.SEMICOLON, PlSqlLexer.SOLIDUS,
                new int[] { PlSqlLexer.CREATE, PlSqlLexer.OR, PlSqlLexer.REPLACE, PlSqlLexer.EDITIONABLE,
                        PlSqlLexer.NONEDITIONABLE }));
    }
}
