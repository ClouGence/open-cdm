package com.clougence.clouddm.ds.split.tidb;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class TiDbSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "tidb"; }
}
