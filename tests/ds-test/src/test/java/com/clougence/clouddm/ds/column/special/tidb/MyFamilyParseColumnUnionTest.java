package com.clougence.clouddm.ds.column.special.tidb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnUnionTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnUnionTest extends MyParseColumnUnionTest {

    public MyFamilyParseColumnUnionTest(){
        spi = new TiSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
