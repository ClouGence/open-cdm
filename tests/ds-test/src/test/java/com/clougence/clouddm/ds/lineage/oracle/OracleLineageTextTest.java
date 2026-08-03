package com.clougence.clouddm.ds.lineage.oracle;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class OracleLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "oracle";
    }
}
