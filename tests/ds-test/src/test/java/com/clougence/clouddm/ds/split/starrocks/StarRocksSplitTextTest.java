package com.clougence.clouddm.ds.split.starrocks;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class StarRocksSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "starrocks"; }
}
