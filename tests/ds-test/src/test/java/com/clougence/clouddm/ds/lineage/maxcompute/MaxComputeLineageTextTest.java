package com.clougence.clouddm.ds.lineage.maxcompute;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class MaxComputeLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "maxcompute";
    }
}
