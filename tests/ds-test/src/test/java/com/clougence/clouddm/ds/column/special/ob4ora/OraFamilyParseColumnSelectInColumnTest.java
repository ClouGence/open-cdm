package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnSelectInColumnTest;
import com.clougence.clouddm.ds.oceanbase.analysis.obforora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnSelectInColumnTest extends OraParseColumnSelectInColumnTest {

    public OraFamilyParseColumnSelectInColumnTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
