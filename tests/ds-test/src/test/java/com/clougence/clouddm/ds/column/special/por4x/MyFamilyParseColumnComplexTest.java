package com.clougence.clouddm.ds.column.special.por4x;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnComplexTest;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSelectColumnAnalysisSpi;

public class MyFamilyParseColumnComplexTest extends MyParseColumnComplexTest {

    public MyFamilyParseColumnComplexTest(){
        spi = new PorXSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
