package com.clougence.clouddm.ds.column.special.por4x;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnFunctionTest;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSelectColumnAnalysisSpi;

public class MyFamilyParseColumnFunctionTest extends MyParseColumnFunctionTest {

    public MyFamilyParseColumnFunctionTest(){
        spi = new PorXSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
