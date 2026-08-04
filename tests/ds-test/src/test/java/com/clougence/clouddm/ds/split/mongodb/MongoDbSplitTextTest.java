package com.clougence.clouddm.ds.split.mongodb;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class MongoDbSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "mongodb"; }
}
