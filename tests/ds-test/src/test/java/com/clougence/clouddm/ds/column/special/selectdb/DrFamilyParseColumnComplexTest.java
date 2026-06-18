package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnComplexTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelSelectColumnAnalysisSpi;

public class DrFamilyParseColumnComplexTest extends DrParseColumnComplexTest {

    public DrFamilyParseColumnComplexTest(){
        spi = new SelSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
