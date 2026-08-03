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
package com.clougence.clouddm.ds.polardb.sql.porx;

import com.clougence.clouddm.ds.polardb.sql.porx.analysis.behavior.PorXBehaviorAnalysisSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.analysis.security.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.editor.rewrite.PorXRewriteSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.parser.PolarXDslProvider;
import com.clougence.clouddm.ds.polardb.sql.porx.parser.PorXSplitAnalysisSpi;
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
public class PorXSqlEngineSpi implements SqlEngineSpi {
    public static final String        NAME = "PolarDB-X SQL";

    private final SplitAnalysisSpi    splitAnalysisSpi;
    private final SecDomainResolveSpi secDomainResolveSpi;
    private final BehaviorAnalysisSpi behaviorAnalysisSpi;
    private final LineageAnalysisSpi  lineageAnalysisSpi;
    private final RewriteSpi          rewriteSpi;

    public PorXSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.secDomainResolveSpi = new PorXSecDomainResolveSpi(metaService);
        this.behaviorAnalysisSpi = new PorXBehaviorAnalysisSpi();
        this.lineageAnalysisSpi = LineageAnalysisSpi.EMPTY;
        this.rewriteSpi = new PorXRewriteSpi();
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider(SqlParserParameters parameters) {
        return PolarXDslProvider.INSTANCE;
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
