package com.clougence.clouddm.ds.lineage.dameng;

import java.util.List;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;

public final class DamengLineageTextTest extends SingleDataSourceLineageTextTest {

    @Override
    protected String datasource() {
        return "dameng";
    }

    @Override
    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles("lineage/dameng/8");
    }
}
