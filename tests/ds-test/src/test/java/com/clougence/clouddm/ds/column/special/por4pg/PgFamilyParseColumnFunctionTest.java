package com.clougence.clouddm.ds.column.special.por4pg;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnFunctionTest;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnFunctionTest extends PgParseColumnFunctionTest {

    public PgFamilyParseColumnFunctionTest(){
        spi = new PorPgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
