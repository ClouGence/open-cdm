package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.analysis.ads4my.AdbSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnExpressionTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnExpressionTest extends MyParseColumnExpressionTest {

    public MyFamilyParseColumnExpressionTest(){
        spi = new AdbSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
