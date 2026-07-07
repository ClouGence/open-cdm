package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.sql.ads4my.column.AdsMySelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnJoinTest;
import com.clougence.clouddm.ds.tidb.sql.column.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnJoinTest extends MyParseColumnJoinTest {

    public MyFamilyParseColumnJoinTest(){
        spi = new AdsMySelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
