package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnJoinTest;
import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnJoinTest extends OraParseColumnJoinTest {

    public OraFamilyParseColumnJoinTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
