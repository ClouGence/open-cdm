package com.clougence.clouddm.ds.lineage.sql92;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class Sql92LineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "sql92";
    }
}
