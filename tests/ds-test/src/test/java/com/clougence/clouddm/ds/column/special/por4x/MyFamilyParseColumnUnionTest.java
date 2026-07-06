package com.clougence.clouddm.ds.column.special.por4x;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnUnionTest;
import com.clougence.clouddm.ds.polardb.sql.porx.column.PorXSelectColumnAnalysisSpi;

public class MyFamilyParseColumnUnionTest extends MyParseColumnUnionTest {

    public MyFamilyParseColumnUnionTest(){
        spi = new PorXSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
