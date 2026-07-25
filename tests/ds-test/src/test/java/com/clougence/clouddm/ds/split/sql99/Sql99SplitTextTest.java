package com.clougence.clouddm.ds.split.sql99;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class Sql99SplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "sql99"; }
}
