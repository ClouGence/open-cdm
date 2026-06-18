package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSimpleTest;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSimpleTest extends PgParseColumnSimpleTest {

    public PgFamilyParseColumnSimpleTest(){
        spi = new GsogSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
