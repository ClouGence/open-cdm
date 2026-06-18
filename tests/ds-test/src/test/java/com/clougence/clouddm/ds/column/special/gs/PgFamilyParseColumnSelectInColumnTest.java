package com.clougence.clouddm.ds.column.special.gs;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.postgres.PgParseColumnSelectInColumnTest;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSelectColumnAnalysisSpi;

public class PgFamilyParseColumnSelectInColumnTest extends PgParseColumnSelectInColumnTest {

    public PgFamilyParseColumnSelectInColumnTest(){
        spi = new GsSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
