package com.clougence.clouddm.ds.column.special.gs;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnExpressionTest;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnExpressionTest extends PgParseColumnExpressionTest {

    public PgFamilyParseColumnExpressionTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
