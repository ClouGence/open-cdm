package com.clougence.clouddm.ds.split.sqlserver;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class SqlServerSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "sqlserver"; }
}
