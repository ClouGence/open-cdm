package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnFunctionTest;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSelectColumnAnalysisSpi;

public class PgFamilyParseColumnFunctionTest extends PgParseColumnFunctionTest {

    public PgFamilyParseColumnFunctionTest(){
        spi = new GsogSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
