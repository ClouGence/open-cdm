package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnFunctionTest;
import com.clougence.sql.postgres.column.PgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnFunctionTest extends PgParseColumnFunctionTest {

    public PgFamilyParseColumnFunctionTest(){
        spi = new PgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
