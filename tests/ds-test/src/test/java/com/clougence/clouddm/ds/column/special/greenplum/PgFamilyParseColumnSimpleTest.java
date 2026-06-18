package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSimpleTest;
import com.clougence.clouddm.ds.greenplum.analysis.GpSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSimpleTest extends PgParseColumnSimpleTest {

    public PgFamilyParseColumnSimpleTest(){
        spi = new GpSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
