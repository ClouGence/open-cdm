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
package com.clougence.sql.doris;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.doris.analysis.behavior.DrBehaviorAnalysisSpi;
import com.clougence.sql.doris.analysis.lineage.DrLineageAnalysisSpi;
import com.clougence.sql.doris.analysis.security.DrSecDomainResolveSpi;
import com.clougence.sql.doris.editor.rewrite.DrRewriteSpi;
import com.clougence.sql.doris.parser.DorisParserConfig;
import com.clougence.sql.doris.parser.DrDslProvider;
import com.clougence.sql.doris.parser.DrSplitAnalysisSpi;

/** @author mode */
public class DrSqlEngineSpi implements SqlEngineSpi {
    public static final String                                NAME               = "Doris SQL";

    private final MetaService                                 metaService;
    private final Map<DorisParserConfig, DrDslProvider>       providers          = new ConcurrentHashMap<>();
    private final Map<DorisParserConfig, SplitAnalysisSpi>    splitAnalysisCache = new ConcurrentHashMap<>();
    private final Map<DorisParserConfig, SecDomainResolveSpi> secDomainCache     = new ConcurrentHashMap<>();
    private final Map<DorisParserConfig, BehaviorAnalysisSpi> behaviorCache      = new ConcurrentHashMap<>();
    private final Map<DorisParserConfig, LineageAnalysisSpi>  lineageCache       = new ConcurrentHashMap<>();
    private final Map<DorisParserConfig, RewriteSpi>          rewriteCache       = new ConcurrentHashMap<>();

    public DrSqlEngineSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        DorisParserConfig config = DorisParserConfig.fromParameters(parameters);
        return provider(config);
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        DorisParserConfig config = DorisParserConfig.fromParameters(parameters);
        return splitAnalysisCache.computeIfAbsent(config, value -> new DrSplitAnalysisSpi(provider(value)));
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        DorisParserConfig config = DorisParserConfig.fromParameters(parameters);
        return secDomainCache.computeIfAbsent(config, value -> new DrSecDomainResolveSpi(metaService, provider(value)));
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        DorisParserConfig config = DorisParserConfig.fromParameters(parameters);
        return behaviorCache.computeIfAbsent(config, value -> new DrBehaviorAnalysisSpi(provider(value)));
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        DorisParserConfig config = DorisParserConfig.fromParameters(parameters);
        return lineageCache.computeIfAbsent(config, value -> new DrLineageAnalysisSpi(metaService, provider(value)));
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        DorisParserConfig config = DorisParserConfig.fromParameters(parameters);
        return rewriteCache.computeIfAbsent(config, value -> new DrRewriteSpi(provider(value)));
    }

    private DrDslProvider provider(DorisParserConfig config) {
        return providers.computeIfAbsent(config, DrDslProvider::new);
    }
}
