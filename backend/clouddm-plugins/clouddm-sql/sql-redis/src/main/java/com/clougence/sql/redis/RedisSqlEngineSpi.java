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
package com.clougence.sql.redis;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.rewrite.RewriteSpi;
import com.clougence.clouddm.sdk.sql.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.sql.split.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.redis.column.RedisSelectColumnAnalysisSpi;
import com.clougence.sql.redis.parser.RedisDslProvider;
import com.clougence.sql.redis.resource.RedisResAnalysisSpi;
import com.clougence.sql.redis.security.RedisSecDomainResolveSpi;
import com.clougence.sql.redis.split.RedisSplitAnalysisSpi;

/** @author mode */
public class RedisSqlEngineSpi implements SqlEngineSpi {
    public static final String            NAME = "Redis Commands";

    private final SplitAnalysisSpi        splitAnalysisSpi;
    private final SecDomainResolveSpi     secDomainResolveSpi;
    private final ResAnalysisSpi          resAnalysisSpi;
    private final SelectColumnAnalysisSpi selectColumnAnalysisSpi;
    private final RewriteSpi              rewriteSpi;

    public RedisSqlEngineSpi(MetaService metaService){
        this.splitAnalysisSpi = new RedisSplitAnalysisSpi();
        this.secDomainResolveSpi = new RedisSecDomainResolveSpi(metaService);
        this.resAnalysisSpi = new RedisResAnalysisSpi(metaService);
        this.selectColumnAnalysisSpi = new RedisSelectColumnAnalysisSpi(metaService);
        this.rewriteSpi = null;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public DslProvider dslProvider() {
        return RedisDslProvider.INSTANCE;
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
