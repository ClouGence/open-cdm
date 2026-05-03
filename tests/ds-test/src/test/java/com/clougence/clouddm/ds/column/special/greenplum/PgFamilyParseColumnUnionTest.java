package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnUnionTest;
import com.clougence.clouddm.ds.greenplum.analysis.GpSelectColumnAnalysisSpi;

public class PgFamilyParseColumnUnionTest extends PgParseColumnUnionTest {

    public PgFamilyParseColumnUnionTest(){
        spi = new GpSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
