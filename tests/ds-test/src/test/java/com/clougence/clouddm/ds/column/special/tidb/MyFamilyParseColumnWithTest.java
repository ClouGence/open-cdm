package com.clougence.clouddm.ds.column.special.tidb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnWithTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnWithTest extends MyParseColumnWithTest {

    public MyFamilyParseColumnWithTest(){
        spi = new TiSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
