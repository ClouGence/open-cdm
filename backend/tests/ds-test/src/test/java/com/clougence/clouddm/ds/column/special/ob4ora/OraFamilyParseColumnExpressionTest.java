package com.clougence.clouddm.ds.column.special.ob4ora;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.column.family.oracle.OraParseColumnExpressionTest;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.ObForOraSelectColumnAnalysisSpi;

public class OraFamilyParseColumnExpressionTest extends OraParseColumnExpressionTest {

    public OraFamilyParseColumnExpressionTest(){
        spi = new ObForOraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

}
