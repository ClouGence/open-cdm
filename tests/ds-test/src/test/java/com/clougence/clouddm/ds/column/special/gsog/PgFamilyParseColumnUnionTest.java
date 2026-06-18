package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnUnionTest;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSelectColumnAnalysisSpi;

public class PgFamilyParseColumnUnionTest extends PgParseColumnUnionTest {

    public PgFamilyParseColumnUnionTest(){
        spi = new GsogSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
