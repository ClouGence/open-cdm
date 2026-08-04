package com.clougence.clouddm.ds.lineage.sql2003;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class Sql2003LineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "sql2003";
    }
}
