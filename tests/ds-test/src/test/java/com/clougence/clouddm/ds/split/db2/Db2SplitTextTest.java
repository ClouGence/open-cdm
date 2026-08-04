package com.clougence.clouddm.ds.split.db2;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class Db2SplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "db2"; }
}
