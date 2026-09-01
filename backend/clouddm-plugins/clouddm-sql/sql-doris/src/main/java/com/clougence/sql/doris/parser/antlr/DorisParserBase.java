/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.doris.parser.antlr;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

import com.clougence.sql.doris.parser.DorisParserConfig;
import com.clougence.sql.doris.parser.DorisVersion;

/** Shares one immutable Doris configuration with the generated parser. */
public abstract class DorisParserBase extends Parser {

    private DorisParserConfig config = new DorisParserConfig(DorisVersion.LATEST);

    protected DorisParserBase(TokenStream input){
        super(input);
    }

    public final void setConfig(DorisParserConfig config) { this.config = config == null ? new DorisParserConfig(DorisVersion.LATEST) : config; }

    public final DorisParserConfig config() {
        return config;
    }

    protected final boolean dorisAtLeast(int minimumMajor) {
        return config.version().major() >= minimumMajor;
    }

    protected final boolean dorisAtMost(int maximumMajor) {
        return config.version().major() <= maximumMajor;
    }
}
