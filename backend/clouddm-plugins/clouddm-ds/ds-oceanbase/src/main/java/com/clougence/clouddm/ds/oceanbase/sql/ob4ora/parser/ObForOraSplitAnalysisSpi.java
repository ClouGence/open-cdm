/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4ora.parser;

import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForOracleLexer;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.LexerSplitPolicy;
import com.clougence.sql.oracle.parser.OraLexerSplitPolicy;
import com.clougence.sql.oracle.parser.OraSplitAnalysisSpi;

/** OceanBase Oracle-mode lexer-only SQL statement splitter. */
public class ObForOraSplitAnalysisSpi extends OraSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return ObOraDslProvider.INSTANCE;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new OraLexerSplitPolicy(new OraLexerSplitPolicy.TokenTypes(//
                ObForOracleLexer.CREATE, ObForOracleLexer.FUNCTION, ObForOracleLexer.PROCEDURE, ObForOracleLexer.TRIGGER,
                ObForOracleLexer.PACKAGE, ObForOracleLexer.TYPE, ObForOracleLexer.BODY, ObForOracleLexer.DECLARE,
                ObForOracleLexer.BEGIN, ObForOracleLexer.END, ObForOracleLexer.IF, ObForOracleLexer.LOOP,
                ObForOracleLexer.WHILE, ObForOracleLexer.CASE, ObForOracleLexer.WITH, ObForOracleLexer.EXTERNAL,
                ObForOracleLexer.LANGUAGE, ObForOracleLexer.AS, ObForOracleLexer.IS, ObForOracleLexer.SEMICOLON,
                ObForOracleLexer.SOLIDUS,
                new int[] { ObForOracleLexer.CREATE, ObForOracleLexer.OR, ObForOracleLexer.REPLACE,
                        ObForOracleLexer.EDITIONABLE, ObForOracleLexer.NONEDITIONABLE }));
    }
}
