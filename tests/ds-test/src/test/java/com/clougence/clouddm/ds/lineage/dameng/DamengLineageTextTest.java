package com.clougence.clouddm.ds.lineage.dameng;

import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class DamengLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "dameng";
    }
}
