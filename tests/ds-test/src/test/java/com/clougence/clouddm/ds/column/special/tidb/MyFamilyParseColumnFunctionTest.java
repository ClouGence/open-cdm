package com.clougence.clouddm.ds.column.special.tidb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnFunctionTest;
import com.clougence.clouddm.ds.tidb.sql.column.TiSelectColumnAnalysisSpi;
import com.clougence.sql.mysql.column.MySelectColumnAnalysisSpi;

public class MyFamilyParseColumnFunctionTest extends MyParseColumnFunctionTest {

    public MyFamilyParseColumnFunctionTest(){
        spi = new TiSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
