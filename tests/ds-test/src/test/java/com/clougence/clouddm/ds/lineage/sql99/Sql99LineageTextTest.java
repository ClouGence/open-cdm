package com.clougence.clouddm.ds.lineage.sql99;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class Sql99LineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "sql99";
    }
}
