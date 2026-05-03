package com.clougence.clouddm.ds.column.special.por4x;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnExpressionTest;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSelectColumnAnalysisSpi;

public class MyFamilyParseColumnExpressionTest extends MyParseColumnExpressionTest {

    public MyFamilyParseColumnExpressionTest(){
        spi = new PorXSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
