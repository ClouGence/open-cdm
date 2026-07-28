package com.clougence.clouddm.ds.lineage.clickhouse;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class ClickHouseLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "clickhouse";
    }
}
