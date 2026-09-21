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
package com.clougence.sql.postgres;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** Shared cache and version dispatch for PostgreSQL-family SQL engines. */
public abstract class AbstractPgSqlEngineSpi implements SqlEngineSpi {
    private final Map<String, SplitAnalysisSpi>    splitCache     = new ConcurrentHashMap<>();
    private final Map<String, SecDomainResolveSpi> secDomainCache = new ConcurrentHashMap<>();
    private final Map<String, BehaviorAnalysisSpi> behaviorCache  = new ConcurrentHashMap<>();
    private final Map<String, RewriteSpi>          rewriteCache   = new ConcurrentHashMap<>();
    private final Map<String, DslProvider>         dslCache       = new ConcurrentHashMap<>();

    protected abstract PostgresVersion resolveVersion(SqlParserParameters parameters);

    protected abstract DslProvider newDslProvider(PostgresVersion version);

    protected abstract SplitAnalysisSpi newSplitAnalysisSpi(PostgresVersion version);

    protected abstract SecDomainResolveSpi newSecDomainResolveSpi(PostgresVersion version);

    protected abstract BehaviorAnalysisSpi newBehaviorAnalysisSpi(PostgresVersion version);

    protected abstract RewriteSpi newRewriteSpi(PostgresVersion version);

    private static String parserKey(SqlParserParameters parameters) {
        return parameters.values().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return dslCache.computeIfAbsent(key, value -> newDslProvider(resolveVersion(parserParameters)));
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return splitCache.computeIfAbsent(key, value -> newSplitAnalysisSpi(resolveVersion(parserParameters)));
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return secDomainCache.computeIfAbsent(key, value -> newSecDomainResolveSpi(resolveVersion(parserParameters)));
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return behaviorCache.computeIfAbsent(key, value -> newBehaviorAnalysisSpi(resolveVersion(parserParameters)));
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        return LineageAnalysisSpi.EMPTY;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return rewriteCache.computeIfAbsent(key, value -> newRewriteSpi(resolveVersion(parserParameters)));
    }

}
