package com.clougence.clouddm.ds.lineage.postgres;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class PostgresLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "postgres";
    }
}
