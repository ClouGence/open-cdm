package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnWithTest;
import com.clougence.clouddm.ds.gauss.sql.gs.column.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnWithTest extends PgParseColumnWithTest {

    public PgFamilyParseColumnWithTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
