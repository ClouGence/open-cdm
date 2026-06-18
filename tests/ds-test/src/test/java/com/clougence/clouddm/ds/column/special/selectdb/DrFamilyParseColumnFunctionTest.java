package com.clougence.clouddm.ds.column.special.selectdb;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.doris.DrParseColumnFunctionTest;
import com.clougence.clouddm.ds.selectdb.analysis.SelSelectColumnAnalysisSpi;

public class DrFamilyParseColumnFunctionTest extends DrParseColumnFunctionTest {

    public DrFamilyParseColumnFunctionTest(){
        spi = new SelSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
