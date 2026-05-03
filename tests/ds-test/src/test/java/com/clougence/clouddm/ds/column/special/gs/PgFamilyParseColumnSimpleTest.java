package com.clougence.clouddm.ds.column.special.gs;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSimpleTest;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSimpleTest extends PgParseColumnSimpleTest {

    public PgFamilyParseColumnSimpleTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
