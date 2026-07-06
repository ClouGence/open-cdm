package com.clougence.clouddm.ds.column.special.por4pg;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnWithTest;
import com.clougence.sql.postgres.column.PgSelectColumnAnalysisSpi;

public class PgFamilyParseColumnWithTest extends PgParseColumnWithTest {

    public PgFamilyParseColumnWithTest(){
        spi = new PgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
