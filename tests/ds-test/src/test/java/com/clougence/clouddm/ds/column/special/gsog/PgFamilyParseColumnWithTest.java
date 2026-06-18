package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnWithTest;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSelectColumnAnalysisSpi;

public class PgFamilyParseColumnWithTest extends PgParseColumnWithTest {

    public PgFamilyParseColumnWithTest(){
        spi = new GsogSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
