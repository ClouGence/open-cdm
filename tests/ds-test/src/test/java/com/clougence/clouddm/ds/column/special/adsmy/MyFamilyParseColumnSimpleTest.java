package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnSimpleTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnSimpleTest extends MyParseColumnSimpleTest {

    public MyFamilyParseColumnSimpleTest(){
        spi = new AdbSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
