package com.clougence.clouddm.ds.split.clickhouse;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class ClickHouseSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "clickhouse"; }
}
