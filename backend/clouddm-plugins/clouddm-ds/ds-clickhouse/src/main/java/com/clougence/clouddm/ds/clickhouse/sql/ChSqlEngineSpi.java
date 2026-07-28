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
package com.clougence.clouddm.ds.clickhouse.sql;

import com.clougence.clouddm.ds.clickhouse.sql.analysis.behavior.ChBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.sql.analysis.lineage.ChLineageAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.sql.analysis.security.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.sql.editor.rewrite.ChRewriteSpi;
import com.clougence.clouddm.ds.clickhouse.sql.parser.ChSplitAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.sql.parser.ChSqlDslProvider;
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
public class ChSqlEngineSpi implements SqlEngineSpi {
    public static final String        NAME = "ClickHouse SQL";

    private final SplitAnalysisSpi    splitAnalysisSpi;
    private final SecDomainResolveSpi secDomainResolveSpi;
    private final BehaviorAnalysisSpi behaviorAnalysisSpi;
    private final LineageAnalysisSpi  lineageAnalysisSpi;
    private final RewriteSpi          rewriteSpi;

    public ChSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.secDomainResolveSpi = new ChSecDomainResolveSpi(metaService);
        this.behaviorAnalysisSpi = new ChBehaviorAnalysisSpi();
        this.lineageAnalysisSpi = new ChLineageAnalysisSpi(metaService);
        this.rewriteSpi = new ChRewriteSpi();
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return ChSqlDslProvider.INSTANCE;
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi(SqlParserParameters parameters) {
        return splitAnalysisSpi;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi(SqlParserParameters parameters) {
        return secDomainResolveSpi;
    }

    @Override
    public BehaviorAnalysisSpi behaviorAnalysisSpi(SqlParserParameters parameters) {
        return behaviorAnalysisSpi;
    }

    @Override
    public LineageAnalysisSpi lineageAnalysisSpi(SqlParserParameters parameters) {
        return lineageAnalysisSpi;
    }

    @Override
    public RewriteSpi rewriteSpi(SqlParserParameters parameters) {
        return rewriteSpi;
    }

}
