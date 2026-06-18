package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnSimpleTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelSelectColumnAnalysisSpi;

public class DrFamilyParseColumnSimpleTest extends DrParseColumnSimpleTest {

    public DrFamilyParseColumnSimpleTest(){
        spi = new SelSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
