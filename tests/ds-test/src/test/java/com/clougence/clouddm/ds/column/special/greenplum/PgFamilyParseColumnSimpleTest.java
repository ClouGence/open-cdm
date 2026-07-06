package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSimpleTest;
import com.clougence.sql.postgres.column.PgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSimpleTest extends PgParseColumnSimpleTest {

    public PgFamilyParseColumnSimpleTest(){
        spi = new PgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
