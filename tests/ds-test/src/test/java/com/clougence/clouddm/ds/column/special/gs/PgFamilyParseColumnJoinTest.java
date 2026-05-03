package com.clougence.clouddm.ds.column.special.gs;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnJoinTest;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnJoinTest extends PgParseColumnJoinTest {

    public PgFamilyParseColumnJoinTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
