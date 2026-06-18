package com.clougence.clouddm.ds.column.special.gsog;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSelectInColumnTest;
import com.clougence.clouddm.ds.gauss.analysis.gsog.GsogSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSelectInColumnTest extends PgParseColumnSelectInColumnTest {

    public PgFamilyParseColumnSelectInColumnTest(){
        spi = new GsogSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
