/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.parser;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.common.parser.LexerSplitPolicy;

/** SQL Server lexer-only SQL statement splitter. */
public class MsSqlSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return MsSqlDslProvider.INSTANCE;
    }

    @Override
    protected LexerSplitPolicy createSplitPolicy() {
        return new MsLexerSplitPolicy();
    }
}
