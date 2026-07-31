package com.clougence.clouddm.ds.split.mongodb;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicMongoDbSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicMongoDbSplitTextTest(int shardCount, int shardId){
        super("split/mongodb", shardCount, shardId);
    }
}

final class MongoDbSplitShard1Test extends BasicMongoDbSplitTextTest { MongoDbSplitShard1Test(){ super(3, 0); } }
final class MongoDbSplitShard2Test extends BasicMongoDbSplitTextTest { MongoDbSplitShard2Test(){ super(3, 1); } }
final class MongoDbSplitShard3Test extends BasicMongoDbSplitTextTest { MongoDbSplitShard3Test(){ super(3, 2); } }
