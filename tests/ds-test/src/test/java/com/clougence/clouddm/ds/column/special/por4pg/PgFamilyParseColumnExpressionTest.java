package com.clougence.clouddm.ds.column.special.por4pg;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnExpressionTest;
import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnExpressionTest extends PgParseColumnExpressionTest {

    public PgFamilyParseColumnExpressionTest(){
        spi = new PorPgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
