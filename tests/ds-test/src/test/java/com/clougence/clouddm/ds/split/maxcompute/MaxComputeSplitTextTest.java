package com.clougence.clouddm.ds.split.maxcompute;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class MaxComputeSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "maxcompute"; }
}
