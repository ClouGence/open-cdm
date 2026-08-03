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

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
import com.clougence.sql.postgres.analysis.lineage.PgLineageAnalysisSpi;
import com.clougence.sql.postgres.analysis.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.editor.rewrite.PgRewriteSpi;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** @author mode */
public class PgSqlEngineSpi implements SqlEngineSpi {
    public static final String                     NAME           = "PG SQL";

    private final MetaService                      metaService;
    private final Map<String, SplitAnalysisSpi>    splitCache     = new ConcurrentHashMap<>();
    private final Map<String, SecDomainResolveSpi> secDomainCache = new ConcurrentHashMap<>();
    private final Map<String, BehaviorAnalysisSpi> behaviorCache  = new ConcurrentHashMap<>();
    private final Map<String, LineageAnalysisSpi>  lineageCache   = new ConcurrentHashMap<>();
    private final Map<String, RewriteSpi>          rewriteCache   = new ConcurrentHashMap<>();
    private final Map<String, DslProvider>         dslCache       = new ConcurrentHashMap<>();

    public PgSqlEngineSpi(MetaService metaService){
        this.metaService = metaService;
    }

    private PostgresVersion resolveVersion(SqlParserParameters parameters) {
        return PostgresVersion.parse(parameters.version());
    }

    private static String parserKey(SqlParserParameters parameters) {
        return parameters.values().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return dslCache.computeIfAbsent(key, value -> new PgDslProvider(resolveVersion(parserParameters)));
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return splitCache.computeIfAbsent(key, value -> new PgSplitAnalysisSpi(resolveVersion(parserParameters)));
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return secDomainCache.computeIfAbsent(key, value -> new PgSecDomainResolveSpi(metaService, resolveVersion(parserParameters)));
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return behaviorCache.computeIfAbsent(key, value -> new PgBehaviorAnalysisSpi(resolveVersion(parserParameters)));
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return lineageCache.computeIfAbsent(key, value -> new PgLineageAnalysisSpi(metaService, resolveVersion(parserParameters)));
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return rewriteCache.computeIfAbsent(key, value -> new PgRewriteSpi(resolveVersion(parserParameters)));
    }

}
