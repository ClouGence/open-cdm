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

import com.clougence.clouddm.ds.clickhouse.sql.column.ChSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.sql.parser.ChSqlDslProvider;
import com.clougence.clouddm.ds.clickhouse.sql.resource.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.sql.rewrite.ChRewriteSpi;
import com.clougence.clouddm.ds.clickhouse.sql.security.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.sql.split.ChSplitAnalysisSpi;
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
public class ChSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "ClickHouse SQL";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final ResAnalysisSpi          resAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    static {
        DslHelper.register(ChSqlDslProvider.INSTANCE);
    }

    public ChSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.secDomainResolveSpi = new ChSecDomainResolveSpi(metaService);
        this.resAnalysisSpi = new ChResAnalysisSpi(metaService);
        this.selectColumnAnalysisSpi = new ChSelectColumnAnalysisSpi(metaService);
        this.rewriteSpi = new ChRewriteSpi();
    }

    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider() {
        return ChSqlDslProvider.INSTANCE;
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
