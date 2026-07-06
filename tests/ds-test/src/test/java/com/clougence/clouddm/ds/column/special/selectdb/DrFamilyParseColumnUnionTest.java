package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnUnionTest;
import com.clougence.sql.doris.column.DrSelectColumnAnalysisSpi;

public class DrFamilyParseColumnUnionTest extends DrParseColumnUnionTest {

    public DrFamilyParseColumnUnionTest(){
        spi = new DrSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
