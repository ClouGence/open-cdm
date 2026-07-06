package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnSimpleTest;
import com.clougence.sql.doris.column.DrSelectColumnAnalysisSpi;

public class DrFamilyParseColumnSimpleTest extends DrParseColumnSimpleTest {

    public DrFamilyParseColumnSimpleTest(){
        spi = new DrSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
