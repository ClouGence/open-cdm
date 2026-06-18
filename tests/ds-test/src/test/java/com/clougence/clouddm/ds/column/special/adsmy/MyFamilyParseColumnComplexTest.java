package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnComplexTest;

public class MyFamilyParseColumnComplexTest extends MyParseColumnComplexTest {

    public MyFamilyParseColumnComplexTest(){
        spi = new AdbSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
