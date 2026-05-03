package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnUnionTest;
import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnWithTest extends OraParseColumnUnionTest {

    public OraFamilyParseColumnWithTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
