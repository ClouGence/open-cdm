package com.clougence.clouddm.ds.column.special.por4x;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnSelectInColumnTest;
import com.clougence.clouddm.ds.polardb.sql.porx.column.PorXSelectColumnAnalysisSpi;

public class MyFamilyParseColumnSelectInColumnTest extends MyParseColumnSelectInColumnTest {

    public MyFamilyParseColumnSelectInColumnTest(){
        spi = new PorXSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
