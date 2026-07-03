package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdsMySelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnWithTest;

public class MyFamilyParseColumnWithTest extends MyParseColumnWithTest {

    public MyFamilyParseColumnWithTest(){
        spi = new AdsMySelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
