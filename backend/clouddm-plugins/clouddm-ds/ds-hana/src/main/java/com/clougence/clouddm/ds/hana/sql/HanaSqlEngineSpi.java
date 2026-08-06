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
package com.clougence.clouddm.ds.hana.sql;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.hana.sql.analysis.behavior.HanaBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.hana.sql.parser.HanaParserConfig;
import com.clougence.clouddm.ds.hana.sql.parser.HanaSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

/** @author mode */
public class HanaSqlEngineSpi implements SqlEngineSpi {
    public static final String                     NAME          = "SAP Hana SQL";

    private final Map<String, SplitAnalysisSpi>    splitCache    = new ConcurrentHashMap<>();
    private final Map<String, BehaviorAnalysisSpi> behaviorCache = new ConcurrentHashMap<>();

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        throw new UnsupportedOperationException("SAP Hana does not support DslProvider.");
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return splitCache.computeIfAbsent(key, value -> new HanaSplitAnalysisSpi(parserConfig(parserParameters)));
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return null;
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String key = parserKey(parserParameters);
        return behaviorCache.computeIfAbsent(key, value -> new HanaBehaviorAnalysisSpi(splitAnalysisSpi(parserParameters)));
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        return LineageAnalysisSpi.EMPTY;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return null;
    }

    private static HanaParserConfig parserConfig(SqlParserParameters parameters) {
        return HanaParserConfig.of(parameters.version(), parameters.get(SqlParserParameters.GRAMMAR_VERSION));
    }

    private static String parserKey(SqlParserParameters parameters) {
        return parameters.values().entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
    }

}
