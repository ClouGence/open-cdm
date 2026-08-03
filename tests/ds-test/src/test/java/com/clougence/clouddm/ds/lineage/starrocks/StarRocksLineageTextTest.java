package com.clougence.clouddm.ds.lineage.starrocks;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class StarRocksLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "starrocks";
    }
}
