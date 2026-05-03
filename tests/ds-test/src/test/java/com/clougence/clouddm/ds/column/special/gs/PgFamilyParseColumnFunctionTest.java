package com.clougence.clouddm.ds.column.special.gs;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnFunctionTest;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnFunctionTest extends PgParseColumnFunctionTest {

    public PgFamilyParseColumnFunctionTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
