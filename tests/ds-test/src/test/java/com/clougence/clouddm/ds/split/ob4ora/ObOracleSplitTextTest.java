package com.clougence.clouddm.ds.split.ob4ora;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class ObOracleSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "ob4ora"; }
}
