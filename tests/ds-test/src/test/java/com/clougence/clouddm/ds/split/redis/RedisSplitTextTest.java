package com.clougence.clouddm.ds.split.redis;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class RedisSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "redis"; }
}
