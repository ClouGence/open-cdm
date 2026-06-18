package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnComplexTest;
import com.clougence.clouddm.ds.greenplum.analysis.GpSelectColumnAnalysisSpi;

public class PgFamilyParseColumnComplexTest extends PgParseColumnComplexTest {

    public PgFamilyParseColumnComplexTest(){
        spi = new GpSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
