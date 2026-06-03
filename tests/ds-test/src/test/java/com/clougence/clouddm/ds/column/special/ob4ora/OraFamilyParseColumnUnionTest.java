package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnUnionTest;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnUnionTest extends OraParseColumnUnionTest {

    public OraFamilyParseColumnUnionTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
