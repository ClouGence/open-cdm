package com.clougence.clouddm.ds.column.special.adsmy;

import com.clougence.clouddm.ds.ads.sql.ads4my.column.AdsMySelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.mysql.MyParseColumnExpressionTest;
import com.clougence.clouddm.ds.tidb.sql.column.TiSelectColumnAnalysisSpi;

public class MyFamilyParseColumnExpressionTest extends MyParseColumnExpressionTest {

    public MyFamilyParseColumnExpressionTest(){
        spi = new AdsMySelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
