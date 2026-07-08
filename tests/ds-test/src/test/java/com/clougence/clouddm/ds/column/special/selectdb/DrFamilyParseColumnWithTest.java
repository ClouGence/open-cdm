package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnWithTest;
import com.clougence.sql.doris.column.DrSelectColumnAnalysisSpi;

public class DrFamilyParseColumnWithTest extends DrParseColumnWithTest {

    public DrFamilyParseColumnWithTest(){
        spi = new DrSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
