package com.clougence.clouddm.ds.lineage.adb;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class AdbLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "adb";
    }
}
