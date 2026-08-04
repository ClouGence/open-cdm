package com.clougence.clouddm.ds.lineage.doris;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class DorisLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "doris";
    }
}
