/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.parser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** HANA lexer-only SQL statement splitter. */
public class HanaSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final HanaParserConfig config;

    public HanaSplitAnalysisSpi(){
        this(HanaParserConfig.of(null, null));
    }

    public HanaSplitAnalysisSpi(HanaParserConfig config){
        this.config = config;
    }

    public HanaParserConfig config() {
        return config;
    }

    @Override
    protected Lexer createLexer(CharStream source) {
        return new HanaSplitLexer(source);
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new HanaLexerSplitPolicy();
    }

    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseLine, int baseColumn) {
        if (script == null) {
            return Collections.emptyList();
        }
        try (Stream<SplitScript> stream = splitScriptStream(new java.io.StringReader(script), args, baseLine, baseColumn)) {
            return stream.toList();
        }
    }
}
