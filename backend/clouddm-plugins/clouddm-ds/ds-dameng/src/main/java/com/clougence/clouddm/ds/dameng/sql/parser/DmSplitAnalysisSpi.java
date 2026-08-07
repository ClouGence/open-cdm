/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.parser;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;

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
}
