package com.clougence.clouddm.ds.column.special.por4pg;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnUnionTest;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnUnionTest extends PgParseColumnUnionTest {

    public PgFamilyParseColumnUnionTest(){
        spi = new PorPgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
