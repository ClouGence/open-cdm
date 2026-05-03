package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnWithTest;
import com.clougence.clouddm.ds.greenplum.analysis.GpSelectColumnAnalysisSpi;

public class PgFamilyParseColumnWithTest extends PgParseColumnWithTest {

    public PgFamilyParseColumnWithTest(){
        spi = new GpSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
