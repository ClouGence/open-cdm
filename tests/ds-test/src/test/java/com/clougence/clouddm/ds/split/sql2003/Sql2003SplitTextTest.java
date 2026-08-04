package com.clougence.clouddm.ds.split.sql2003;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class Sql2003SplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "sql2003"; }
}
