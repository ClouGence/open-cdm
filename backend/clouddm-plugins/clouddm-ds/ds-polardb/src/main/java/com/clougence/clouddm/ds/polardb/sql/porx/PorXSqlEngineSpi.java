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

import com.clougence.clouddm.ds.polardb.sql.porx.column.PorXSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.parser.PolarXDslProvider;
import com.clougence.clouddm.ds.polardb.sql.porx.resource.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.rewrite.PorXRewriteSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.security.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.split.PorXSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.sql.split.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;

/** @author mode */
public class PorXSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "PolarDB-X SQL";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final ResAnalysisSpi          resAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    static {
        DslHelper.register(PolarXDslProvider.INSTANCE);
    }

    public PorXSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.secDomainResolveSpi = new PorXSecDomainResolveSpi(metaService);
        this.resAnalysisSpi = new PorXResAnalysisSpi(metaService);
        this.selectColumnAnalysisSpi = new PorXSelectColumnAnalysisSpi(metaService);
        this.rewriteSpi = new PorXRewriteSpi();
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider() {
        return PolarXDslProvider.INSTANCE;
    }

    @Override
    public SplitAnalysisSpi splitAnalysisSpi() {
        return splitAnalysisSpi;
    }

    @Override
    public SecDomainResolveSpi secDomainResolveSpi() {
        return secDomainResolveSpi;
    }

    @Override
    public ResAnalysisSpi resAnalysisSpi() {
        return resAnalysisSpi;
    }

    @Override
    public SelectColumnAnalysisSpi selectColumnAnalysisSpi() {
        return selectColumnAnalysisSpi;
    }

    @Override
    public SecRulesSupportSpi secRulesSupportSpi() {
        return null;
    }

    @Override
    public RewriteSpi rewriteSpi() {
        return rewriteSpi;
    }

}
