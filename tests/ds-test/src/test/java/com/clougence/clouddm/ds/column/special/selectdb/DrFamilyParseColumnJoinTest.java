package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnJoinTest;
import com.clougence.sql.doris.column.DrSelectColumnAnalysisSpi;

public class DrFamilyParseColumnJoinTest extends DrParseColumnJoinTest {

    public DrFamilyParseColumnJoinTest(){
        spi = new DrSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
