package com.clougence.clouddm.ds.split.doris;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class DorisSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "doris"; }
}
