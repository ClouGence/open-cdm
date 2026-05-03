package com.clougence.clouddm.ds.column.special.por4x;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnWithTest;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSelectColumnAnalysisSpi;

public class MyFamilyParseColumnWithTest extends MyParseColumnWithTest {

    public MyFamilyParseColumnWithTest(){
        spi = new PorXSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
