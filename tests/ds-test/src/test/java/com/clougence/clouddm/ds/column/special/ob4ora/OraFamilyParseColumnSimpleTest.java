package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnSimpleTest;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnSimpleTest extends OraParseColumnSimpleTest {

    public OraFamilyParseColumnSimpleTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
