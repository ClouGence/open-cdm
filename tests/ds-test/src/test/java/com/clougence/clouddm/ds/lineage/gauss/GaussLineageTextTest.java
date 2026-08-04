package com.clougence.clouddm.ds.lineage.gauss;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class GaussLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "gauss";
    }
}
