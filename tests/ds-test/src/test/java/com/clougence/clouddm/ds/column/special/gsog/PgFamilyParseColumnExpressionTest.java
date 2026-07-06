package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnExpressionTest;
import com.clougence.clouddm.ds.gauss.sql.gs.column.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnExpressionTest extends PgParseColumnExpressionTest {

    public PgFamilyParseColumnExpressionTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
