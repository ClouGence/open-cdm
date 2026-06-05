package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnComplexTest;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnComplexTest extends OraParseColumnComplexTest {

    public OraFamilyParseColumnComplexTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
