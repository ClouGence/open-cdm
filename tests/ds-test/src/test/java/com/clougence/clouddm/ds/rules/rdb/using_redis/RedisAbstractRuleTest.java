package com.clougence.clouddm.ds.rules.rdb.using_redis;

import com.clougence.clouddm.ds.redis.analysis.RedisSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class RedisAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new RedisSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.Redis;
    }
}
