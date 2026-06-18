package com.clougence.clouddm.ds.column.special.tidb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnExpressionTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnExpressionTest extends MyParseColumnExpressionTest {

    public MyFamilyParseColumnExpressionTest(){
        spi = new TiSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
