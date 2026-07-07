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
package com.clougence.clouddm.ds.gauss.sql.gs;

import com.clougence.clouddm.ds.gauss.sql.gs.column.GsSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.parser.GaussDslProvider;
import com.clougence.clouddm.ds.gauss.sql.gs.resource.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.rewrite.GsRewriteSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.security.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.sql.gs.split.GsSplitAnalysisSpi;
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
public class GsSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "Gauss SQL";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final ResAnalysisSpi          resAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    static {
        DslHelper.register(GaussDslProvider.INSTANCE);
    }

    public GsSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.secDomainResolveSpi = new GsSecDomainResolveSpi(metaService);
        this.resAnalysisSpi = new GsResAnalysisSpi(metaService);
        this.selectColumnAnalysisSpi = new GsSelectColumnAnalysisSpi(metaService);
        this.rewriteSpi = new GsRewriteSpi();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
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
