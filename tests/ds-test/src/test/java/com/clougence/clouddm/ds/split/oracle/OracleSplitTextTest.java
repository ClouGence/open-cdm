package com.clougence.clouddm.ds.split.oracle;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class OracleSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "oracle"; }
}
