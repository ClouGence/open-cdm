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

import com.clougence.clouddm.ds.hana.sql.column.HanaSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.hana.sql.resource.HanaResAnalysisSpi;
import com.clougence.clouddm.ds.hana.sql.security.HanaSecDomainResolveSpi;
import com.clougence.clouddm.ds.hana.sql.split.HanaSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.sql.split.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;

/** @author mode */
public class HanaSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "SAP Hana SQL";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final ResAnalysisSpi          resAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    public HanaSqlEngineSpi(){
        this.splitAnalysisSpi = new HanaSplitAnalysisSpi();
        this.secDomainResolveSpi = new HanaSecDomainResolveSpi();
        this.resAnalysisSpi = new HanaResAnalysisSpi();
        this.selectColumnAnalysisSpi = new HanaSelectColumnAnalysisSpi();
        this.rewriteSpi = null;
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider() {
        throw new UnsupportedOperationException("SAP Hana does not support DslProvider.");
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
