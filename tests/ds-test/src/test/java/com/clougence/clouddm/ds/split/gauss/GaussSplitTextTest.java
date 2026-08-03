package com.clougence.clouddm.ds.split.gauss;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class GaussSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "gauss"; }
}
