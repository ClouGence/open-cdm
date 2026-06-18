package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnJoinTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnJoinTest extends MyParseColumnJoinTest {

    public MyFamilyParseColumnJoinTest(){
        spi = new AdbSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
