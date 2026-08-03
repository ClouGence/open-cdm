package com.clougence.clouddm.ds.split.redis;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicRedisSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicRedisSplitTextTest(int shardCount, int shardId){
        super("split/redis", shardCount, shardId);
    }
}

final class RedisSplitShard1Test extends BasicRedisSplitTextTest { RedisSplitShard1Test(){ super(3, 0); } }
final class RedisSplitShard2Test extends BasicRedisSplitTextTest { RedisSplitShard2Test(){ super(3, 1); } }
final class RedisSplitShard3Test extends BasicRedisSplitTextTest { RedisSplitShard3Test(){ super(3, 2); } }
