package com.clougence.clouddm.ds.column.special.greenplum;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSelectInColumnTest;
import com.clougence.sql.postgres.column.PgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSelectInColumnTest extends PgParseColumnSelectInColumnTest {

    public PgFamilyParseColumnSelectInColumnTest(){
        spi = new PgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
