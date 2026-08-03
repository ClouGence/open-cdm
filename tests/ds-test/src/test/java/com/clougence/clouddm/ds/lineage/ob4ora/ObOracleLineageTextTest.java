package com.clougence.clouddm.ds.lineage.ob4ora;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class ObOracleLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "ob4ora";
    }
}
