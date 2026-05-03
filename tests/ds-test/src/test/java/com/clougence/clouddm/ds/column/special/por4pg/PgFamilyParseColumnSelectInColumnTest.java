package com.clougence.clouddm.ds.column.special.por4pg;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSelectInColumnTest;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSelectInColumnTest extends PgParseColumnSelectInColumnTest {

    public PgFamilyParseColumnSelectInColumnTest(){
        spi = new PorPgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
