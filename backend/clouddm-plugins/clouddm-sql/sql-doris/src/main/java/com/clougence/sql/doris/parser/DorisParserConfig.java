/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.sql.doris.parser;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;

public record DorisParserConfig(DorisVersion version) {

    public DorisParserConfig{
        version = version == null ? DorisVersion.LATEST : version;
    }

    public static DorisParserConfig fromParameters(SqlParserParameters parameters) {
        SqlParserParameters value = SqlParserParameters.nullToEmpty(parameters);
        String grammarVersion = value.get(SqlParserParameters.GRAMMAR_VERSION);
        return new DorisParserConfig(DorisVersion.parse(grammarVersion == null || grammarVersion.isBlank() ? value.version() : grammarVersion));
    }
}
