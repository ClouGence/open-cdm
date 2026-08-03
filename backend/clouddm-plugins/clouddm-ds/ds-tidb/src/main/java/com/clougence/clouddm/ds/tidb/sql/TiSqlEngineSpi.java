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
package com.clougence.clouddm.ds.tidb.sql;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.ds.tidb.sql.analysis.behavior.TiBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.tidb.sql.analysis.lineage.TiLineageAnalysisSpi;
import com.clougence.clouddm.ds.tidb.sql.analysis.security.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.sql.editor.rewrite.TiRewriteSpi;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBDslProvider;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBParserConfig;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

/** @author mode */
public class TiSqlEngineSpi implements SqlEngineSpi {
    public static final String                               NAME               = "TiDB SQL";

    private final Map<TiDBParserConfig, TiDBDslProvider>     providerCache      = new ConcurrentHashMap<>();
    private final Map<TiDBParserConfig, SplitAnalysisSpi>    splitAnalysisCache = new ConcurrentHashMap<>();
    private final Map<TiDBParserConfig, BehaviorAnalysisSpi> behaviorCache      = new ConcurrentHashMap<>();
    private final Map<TiDBParserConfig, SecDomainResolveSpi> securityCache      = new ConcurrentHashMap<>();
    private final Map<TiDBParserConfig, LineageAnalysisSpi>  lineageCache       = new ConcurrentHashMap<>();
    private final Map<TiDBParserConfig, RewriteSpi>          rewriteCache       = new ConcurrentHashMap<>();
    private final MetaService                                metaService;

    public TiSqlEngineSpi(MetaService metaService){
        this.metaService = metaService;
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return provider(TiDBParserConfig.fromParameters(parameters));
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        TiDBParserConfig config = TiDBParserConfig.fromParameters(parameters);
        return splitAnalysisCache.computeIfAbsent(config, value -> new TiDBSplitAnalysisSpi(provider(value)));
    }

    private TiDBDslProvider provider(TiDBParserConfig config) {
        return providerCache.computeIfAbsent(config, TiDBDslProvider::new);
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        TiDBParserConfig config = TiDBParserConfig.fromParameters(parameters);
        return securityCache.computeIfAbsent(config, value -> new TiSecDomainResolveSpi(metaService, provider(value)));
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        TiDBParserConfig config = TiDBParserConfig.fromParameters(parameters);
        return behaviorCache.computeIfAbsent(config, TiBehaviorAnalysisSpi::new);
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        TiDBParserConfig config = TiDBParserConfig.fromParameters(parameters);
        return lineageCache.computeIfAbsent(config, value -> new TiLineageAnalysisSpi(metaService, provider(value)));
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        TiDBParserConfig config = TiDBParserConfig.fromParameters(parameters);
        return rewriteCache.computeIfAbsent(config, value -> new TiRewriteSpi(provider(value)));
    }

}
