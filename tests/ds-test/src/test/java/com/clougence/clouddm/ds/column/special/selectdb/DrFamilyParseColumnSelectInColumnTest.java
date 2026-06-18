package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnSelectInColumnTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelSelectColumnAnalysisSpi;

public class DrFamilyParseColumnSelectInColumnTest extends DrParseColumnSelectInColumnTest {

    public DrFamilyParseColumnSelectInColumnTest(){
        spi = new SelSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
