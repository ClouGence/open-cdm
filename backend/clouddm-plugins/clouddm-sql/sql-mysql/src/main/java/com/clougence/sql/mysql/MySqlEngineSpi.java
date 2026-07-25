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
package com.clougence.sql.mysql;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.mysql.analysis.behavior.MyBehaviorAnalysisSpi;
import com.clougence.sql.mysql.analysis.column.MySelectColumnAnalysisSpi;
import com.clougence.sql.mysql.analysis.security.MySecDomainResolveSpi;
import com.clougence.sql.mysql.editor.rewrite.MyRewriteSpi;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySplitAnalysisSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlParserConfig.Feature;

/** @author mode */
public class MySqlEngineSpi implements SqlEngineSpi {
    public static final String                         NAME           = "MySQL";

    private final MetaService                          metaService;
    private final Map<String, SplitAnalysisSpi>        splitCache     = new ConcurrentHashMap<>();
    private final Map<String, SecDomainResolveSpi>     secDomainCache = new ConcurrentHashMap<>();
    private final Map<String, BehaviorAnalysisSpi>     behaviorCache  = new ConcurrentHashMap<>();
    private final Map<String, SelectColumnAnalysisSpi> selectColCache = new ConcurrentHashMap<>();
    private final Map<String, RewriteSpi>              rewriteCache   = new ConcurrentHashMap<>();
    private final Map<String, DslProvider>             dslCache       = new ConcurrentHashMap<>();

    public MySqlEngineSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public String name() {
        return NAME;
    }

    private static MySqlParserConfig parserConfig(SqlParserParameters parameters) {
        boolean sqlModeKnown = parameters.contains(SqlParserParameters.SQL_MODE);
        return MySqlParserConfig.of(   //
                parameters.version(),   //
                parameters.get(SqlParserParameters.GRAMMAR_VERSION), //
                parameters.get(SqlParserParameters.EXACT_VERSION),   //
                sqlModeKnown, //
                parserFeatures(parameters));
    }

    private static EnumSet<Feature> parserFeatures(SqlParserParameters parameters) {
        if (!parameters.contains(SqlParserParameters.SQL_MODE)) {
            return EnumSet.noneOf(Feature.class);
        }

        EnumSet<Feature> features = EnumSet.noneOf(Feature.class);
        String sqlMode = parameters.get(SqlParserParameters.SQL_MODE);
        if (sqlMode != null && !sqlMode.isBlank()) {
            for (String modeName : sqlMode.split(",")) {
                String normalized = modeName.trim().toUpperCase(Locale.ROOT);
                if (switch (normalized) {
                    case "ANSI", "DB2", "MAXDB", "MSSQL", "ORACLE", "POSTGRESQL" -> true;
                    default -> false;
                }) {
                    features.add(Feature.ANSI_QUOTES);
                    features.add(Feature.PIPES_AS_CONCAT);
                    features.add(Feature.IGNORE_SPACE);
                    continue;
                }
                try {
                    features.add(Feature.valueOf(normalized));
                } catch (IllegalArgumentException ignored) {
                    // SQL modes unrelated to lexical or grammar behavior do not affect the parser.
                }
            }
        }
        return features;
    }

    private static String parserKey(SqlParserParameters parameters) {
        return parameters.values().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return dslCache.computeIfAbsent(key, value -> new MyDslProvider(parserConfig(parserParameters)));
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return splitCache.computeIfAbsent(key, value -> new MySplitAnalysisSpi((MyDslProvider) dslProvider(parserParameters)));
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return secDomainCache.computeIfAbsent(key, value -> new MySecDomainResolveSpi(metaService, parserConfig(parserParameters)));
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return behaviorCache.computeIfAbsent(key, value -> new MyBehaviorAnalysisSpi(parserConfig(parserParameters)));
    }

    @Override
    public SelectColumnAnalysisSpi selectColumnAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return selectColCache.computeIfAbsent(key, value -> new MySelectColumnAnalysisSpi(metaService, parserConfig(parserParameters)));
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return rewriteCache.computeIfAbsent(key, value -> new MyRewriteSpi(parserConfig(parserParameters)));
    }
}
