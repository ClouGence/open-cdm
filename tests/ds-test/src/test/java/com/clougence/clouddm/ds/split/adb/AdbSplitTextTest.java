package com.clougence.clouddm.ds.split.adb;

import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class AdbSplitTextTest extends SingleDataSourceSplitTextTest {
    @Override
    protected String datasource() { return "adb"; }
}
