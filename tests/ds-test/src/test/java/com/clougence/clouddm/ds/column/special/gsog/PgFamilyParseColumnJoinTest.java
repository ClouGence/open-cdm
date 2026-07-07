package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnJoinTest;
import com.clougence.clouddm.ds.gauss.sql.gs.column.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnJoinTest extends PgParseColumnJoinTest {

    public PgFamilyParseColumnJoinTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
