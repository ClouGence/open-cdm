package com.clougence.clouddm.ds.column.special.por4pg;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSimpleTest;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSimpleTest extends PgParseColumnSimpleTest {

    public PgFamilyParseColumnSimpleTest(){
        spi = new PorPgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
