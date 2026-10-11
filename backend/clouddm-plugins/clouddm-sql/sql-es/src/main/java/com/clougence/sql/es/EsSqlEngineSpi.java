/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.es;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

public class EsSqlEngineSpi implements SqlEngineSpi {
    public static final String NAME = "ES DSL";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        // Until the ANTLR grammar is implemented, never treat an ES request as an empty script.
        return (reader, args, baseLine, baseColumn) -> {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_PARSER_UNSUPPORTED);
        };
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        return (reader, levels, baseLine, baseColumn) -> {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_PARSER_UNSUPPORTED);
        };
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return null;
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        return null;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return null;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return null;
    }
}
