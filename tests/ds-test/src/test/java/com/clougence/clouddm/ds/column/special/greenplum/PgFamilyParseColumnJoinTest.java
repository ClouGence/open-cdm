package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnJoinTest;
import com.clougence.clouddm.ds.greenplum.analysis.GpSelectColumnAnalysisSpi;

public class PgFamilyParseColumnJoinTest extends PgParseColumnJoinTest {

    public PgFamilyParseColumnJoinTest(){
        spi = new GpSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
